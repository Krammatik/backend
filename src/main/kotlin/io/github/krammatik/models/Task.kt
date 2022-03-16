package io.github.krammatik.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Task(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val type: TaskType,
    val title: String,
    val description: TextMediaElement,
    val score: Double,
    val solutions: List<Solution>,
    val hint: TextMediaElement,
    val recommendations: List<String>,
    val value: String
)