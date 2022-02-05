package io.github.krammatik.user

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.github.krammatik.dynamodb.DynamoDB

class UserDynamoDatabase : IUserDatabase {

    private val client = DynamoDbClient {
        region = "eu-central-1"
    }

    override suspend fun createUser(user: User, password: String): User {
        val attributes = DynamoDB.encode(user.toAccount(password))
        client.putItem {
            tableName = "users"
            item = attributes
            conditionExpression = "username <> :username" // Add user only if username does not exist
            expressionAttributeValues = mutableMapOf(
                ":username" to AttributeValue.S(user.username)
            )
        }
        return user
    }

    override suspend fun getUserById(id: String): User? {
        val response = client.getItem {
            tableName = "users"
            key = mutableMapOf("id" to AttributeValue.S("id"))
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