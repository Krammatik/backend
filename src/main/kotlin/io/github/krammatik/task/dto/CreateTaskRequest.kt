package io.github.krammatik.task.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.dto.TextMediaElementDto
import io.github.krammatik.models.Task
import org.kodein.di.DI

data class CreateTaskRequest(
    val title: String,
    val type: TaskTypeDto,
    val description: TextMediaElementDto,
    val score: Double,
    val solutions: List<SolutionDto>,
    val hint: TextMediaElementDto,
    val recommendations: List<String>,
    val value: String
) : IDataTransferable<Task> {

    override fun toTransferable(di: DI): Task {
        return Task(
            type = this.type.toTransferable(di),
            title = this.title,
            description = this.description.toTransferable(di),
            score = this.score,
            solutions = this.solutions.map { it.toTransferable(di) },
            hint = this.hint.toTransferable(di),
            recommendations = this.recommendations,
            value = this.value
        )
    }

}
