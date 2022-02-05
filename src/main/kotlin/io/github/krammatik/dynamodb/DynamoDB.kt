package io.github.krammatik.dynamodb

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

/**
 * Quick and Dirty DynamoDB mapper because
 * the aws kotlin sdk didn't have one.
 */
@OptIn(ExperimentalStdlibApi::class)
object DynamoDB {

    fun <T : Any> encode(t: T): Map<String, AttributeValue> {
        val kClass = t::class
        val annotations = kClass.findAnnotations(Mappable::class)
        if (annotations.isEmpty()) {
            throw MappingException("Add the annotation @Mappable to the class ${kClass.qualifiedName}")
        }
        return kClass.memberProperties.associate { property ->
            val mappedName = property.findAnnotations(MappedName::class).firstOrNull()?.value ?: property.name
            val propertyValue =
                property.getter.call(t) ?: throw MappingException("Value of ${property.name} can't be null")
            mappedName to when (propertyValue) {
                is String -> AttributeValue.S(propertyValue)
                is Number -> AttributeValue.N(propertyValue.toString())
                is List<*> -> {
                    if (propertyValue.isEmpty()) {
                        AttributeValue.Null(true)
                    }
                    val annotationType = property.findAnnotations(ListType::class).firstOrNull()
                        ?: throw MappingException("Missing @ListType annotation for ${property.name}")
                    when (annotationType.value) {
                        String::class -> AttributeValue.Ss(propertyValue as List<String>)
                        Float::class -> AttributeValue.Ns((propertyValue as List<Float>).map { it.toString() })
                        Int::class -> AttributeValue.Ns((propertyValue as List<Int>).map { it.toString() })
                        Double::class -> AttributeValue.Ns((propertyValue as List<Double>).map { it.toString() })
                        else -> throw MappingException("Type List<${annotationType.value.simpleName}> of $mappedName is not supported")
                    }
                }
                else -> throw MappingException("Type ${propertyValue::class.simpleName} of $mappedName is not supported")
            }
        }
    }

    inline fun <reified T> decode(values: Map<String, AttributeValue>): T {
        val kClass = T::class
        val annotations = kClass.findAnnotations(Mappable::class)
        if (annotations.isEmpty()) {
            throw MappingException("Add the annotation @Mappable to the class ${kClass.qualifiedName}")
        }
        val fieldNames = kClass.members.filter { it is KProperty }.associate {
            val mappedName = it.findAnnotations(MappedName::class).firstOrNull()?.value ?: it.name
            mappedName to it.name
        }
        val parameters = HashMap<KParameter, Any?>()
        fieldNames.forEach { (mappedName, realName) ->
            val value = values[mappedName] ?: throw MappingException("No value for $mappedName provided!")
            val parameter = kClass.constructors.first().findParameterByName(realName)
                ?: throw MappingException("Parameter $realName not found in constructor")
            parameters[parameter] = when (value) {
                is AttributeValue.S -> value.value
                is AttributeValue.N -> {
                    when (parameter.type.jvmErasure) {
                        Float::class -> value.value.toFloat()
                        Int::class -> value.value.toInt()
                        Double::class -> value.value.toDouble()
                        else -> throw MappingException("Type ${parameter.type.jvmErasure.simpleName} of $mappedName is not supported")
                    }
                }
                is AttributeValue.Ss -> value.value
                is AttributeValue.Ns -> {
                    val annotationType = parameter.findAnnotations(ListType::class).firstOrNull()
                        ?: throw MappingException("Missing @ListType annotation for ${parameter.name}")
                    when (annotationType.value) {
                        Float::class -> value.value.map { it.toFloat() }
                        Int::class -> value.value.map { it.toInt() }
                        Double::class -> value.value.map { it.toDouble() }
                        else -> throw MappingException("Type List<${annotationType.value.simpleName}> of $mappedName is not supported")
                    }
                }
                else -> throw MappingException("Type ${value::class.simpleName} of $mappedName is not supported")
            }
        }
        return kClass.constructors.first().callBy(parameters)
    }
}

class MappingException(message: String) : RuntimeException(message)
