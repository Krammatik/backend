package io.github.krammatik.user.services

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.ConditionalCheckFailedException
import io.github.krammatik.dynamodb.DynamoDB
import io.github.krammatik.plugins.InvalidRequestException
import io.github.krammatik.user.Account
import io.github.krammatik.user.User
import org.slf4j.LoggerFactory

class UserDynamoDatabase : IUserDatabase {

    private val client = DynamoDbClient {
        region = "eu-central-1"
    }
    private val logger = LoggerFactory.getLogger(UserDynamoDatabase::class.java)

    override suspend fun createUser(user: User, password: String): User {
        if (getAccountByName(user.username) != null) {
            throw InvalidRequestException("A user with this name already exits")
        }
        val attributes = DynamoDB.encode(user.toAccount(password))
        try {
            client.putItem {
                tableName = "users"
                item = attributes
                conditionExpression = "username <> :name" // Add user only if username does not exist
                expressionAttributeValues = mapOf(":name" to AttributeValue.S(user.username))
            }
        } catch (e: ConditionalCheckFailedException) {
            logger.error("Error while creating user", e)
            throw InvalidRequestException("Failed to create user")
        }
        return user
    }

    override suspend fun getUserById(id: String): User? {
        val response = client.getItem {
            tableName = "users"
            key = mutableMapOf("id" to AttributeValue.S(id))
        }
        if (response.item == null) {
            return null
        }

        return DynamoDB.decode<Account>(response.item!!).toUser()
    }

    override suspend fun getAccountByName(username: String): Account? {
        val response = client.query {
            tableName = "users"
            indexName = "usernames"
            keyConditionExpression = "username = :username"
            expressionAttributeValues = mutableMapOf(
                ":username" to AttributeValue.S(username)
            )
        }
        if (response.count != 1) {
            return null
        }
        val item = response.items?.firstOrNull() ?: return null
        return DynamoDB.decode(item)
    }
}