package io.github.krammatik.dynamodb

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class ListType(val value: KClass<out Any>)
