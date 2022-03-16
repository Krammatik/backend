package io.github.krammatik.task.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.dto.TextMediaElementDto
import io.github.krammatik.models.Task
import kotlinx.serialization.Serializable
import org.kodein.di.DI

@Serializable
data class TaskDto(
    val id: String,
    val type: TaskTypeDto,
    val title: String,
    val description: TextMediaElementDto,
    val score: Double,
    val solutions: List<SolutionDto>,
    val hint: TextMediaElementDto,
    val recommendations: List<String>,
    val value: String
) : IDataTransferable<Task> {

    override fun toTransferable(di: DI): Task {
        return Task(
            this.id,
            this.type.toTransferable(di),
            this.title,
            this.description.toTransferable(di),
            this.score,
            this.solutions.map { it.toTransferable(di) },
            this.hint.toTransferable(di),
            this.recommendations,
            this.value
        )
    }

}
