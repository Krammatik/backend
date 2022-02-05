package io.github.krammatik.dynamodb

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.github.krammatik.user.Account
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DynamoDBTest {

    @Test
    fun encode() {
        val testUser = Account("helloWorld", "xXxSuperMasterxXx", "oewkogkwoekgowkeo", emptyList())
        val expectedMapping = mapOf(
            "id" to AttributeValue.S("helloWorld"),
            "username" to AttributeValue.S("xXxSuperMasterxXx"),
            "password" to AttributeValue.S("oewkogkwoekgowkeo"),
            "groups" to AttributeValue.Ss(emptyList())
        )
        val mapped = DynamoDB.encode(testUser)
        assertEquals(expectedMapping, mapped)
    }

    @Test
    fun decode() {
        val expected = Account("helloWorld", "xXxSuperMasterxXx", "oewkogkwoekgowkeo", emptyList())
        val mapped = mapOf(
            "id" to AttributeValue.S("helloWorld"),
            "username" to AttributeValue.S("xXxSuperMasterxXx"),
            "password" to AttributeValue.S("oewkogkwoekgowkeo"),
            "groups" to AttributeValue.Ss(emptyList())
        )
        assertEquals(expected, DynamoDB.decode(mapped))
    }
}