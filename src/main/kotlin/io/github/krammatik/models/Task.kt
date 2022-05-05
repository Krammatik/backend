package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.task.dto.TaskDto
import org.bson.codecs.pojo.annotations.BsonId
import org.kodein.di.DI
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
) : IDataTransferable<TaskDto> {
    override fun toTransferable(di: DI): TaskDto {
        return TaskDto(
            id,
            type.toTransferable(di),
            title,
            description.toTransferable(di),
            score,
            solutions.map { it.toTransferable(di) },
            hint.toTransferable(di),
            recommendations,
            value
        )
    }

}