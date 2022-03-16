package io.github.krammatik.course.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.dto.TextMediaElementDto
import io.github.krammatik.models.Course
import io.github.krammatik.task.services.ITaskDatabase
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.instance

@Serializable
data class CourseDto(
    val id: String,
    val title: String,
    val author: String,
    val description: TextMediaElementDto,
    val creationDate: Instant,
    val changeDate: Instant,
    val tasks: MutableList<String>
) : IDataTransferable<Course> {

    override fun toTransferable(di: DI): Course {
        val taskService: ITaskDatabase by di.instance()
        return Course(
            this.id,
            this.title,
            this.author,
            this.description.toTransferable(di),
            this.creationDate.toJavaInstant(),
            this.changeDate.toJavaInstant(),
            this.tasks.map { taskService.getTaskById(it)!! }.toMutableList()
        )
    }

}