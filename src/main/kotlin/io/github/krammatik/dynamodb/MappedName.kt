package io.github.krammatik.dynamodb

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MappedName(val value: String)
