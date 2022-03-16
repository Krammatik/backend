package io.github.krammatik.models

import io.github.krammatik.course.dto.CourseDto
import io.github.krammatik.dto.IDataTransferable
import org.bson.codecs.pojo.annotations.BsonId
import org.kodein.di.DI
import java.time.Instant
import java.util.*

data class Course(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val description: TextMediaElement,
    val creationDate: Instant,
    val changeDate: Instant,
    val tasks: MutableList<Task>
) : IDataTransferable<CourseDto> {

    override fun toTransferable(di: DI): CourseDto {
        return CourseDto(
            id,
            title,
            author,
            description.toTransferable(di),
            kotlinx.datetime.Instant.fromEpochMilliseconds(this.creationDate.toEpochMilli()),
            kotlinx.datetime.Instant.fromEpochMilliseconds(this.changeDate.toEpochMilli()),
            tasks.map { it.id }.toMutableList()
        )
    }

}
