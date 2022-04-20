package io.github.krammatik.course.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.dto.TextMediaElementDto
import io.github.krammatik.models.Course
import io.github.krammatik.models.TextMediaElement
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import java.time.Instant

@Serializable
data class CourseCreationRequest(
    val title: String,
    val description: TextMediaElementDto
) : IDataTransferable<Course> {

    var userId: String = ""

    override fun toTransferable(di: DI): Course {
        return Course(
            title = title,
            author = userId,
            description = description.toTransferable(di),
            creationDate = Instant.now(),
            changeDate = Instant.now(),
            tasks = ArrayList()
        )
    }

}
