package io.github.krammatik.course.services

import com.mongodb.client.MongoClient
import io.github.krammatik.course.dto.CourseCreationRequest
import io.github.krammatik.models.Course
import org.kodein.di.DI
import org.kodein.di.instance
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.updateOne

class CourseDatabase(val di: DI) : ICourseDatabase {

    private val mongoClient: MongoClient by di.instance()

    private val database get() = this.mongoClient.getDatabase("krammatik")
    private val courseCollection get() = this.database.getCollection<Course>("courses")

    override fun getCourseById(id: String): Course? {
        return this.courseCollection.findOne(Course::id eq id)
    }

    override fun updateCourse(course: Course): Course {
        this.courseCollection.updateOne(Course::id eq course.id, course)
        return course
    }

    override fun getCourses(): List<Course> {
        return this.courseCollection.find().toList()
    }

    override fun createCourse(request: CourseCreationRequest): Course {
        val course = request.toTransferable(di)
        this.courseCollection.insertOne(course)
        return course
    }

}