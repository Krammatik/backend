package io.github.krammatik.course

import io.github.krammatik.course.dto.CourseCreationRequest
import io.github.krammatik.course.services.ICourseDatabase
import io.github.krammatik.plugins.ForbiddenException
import io.github.krammatik.plugins.InvalidRequestException
import io.github.krammatik.plugins.NotFoundException
import io.github.krammatik.task.services.ITaskDatabase
import io.github.krammatik.user.allowed
import io.github.krammatik.user.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class CourseController(application: Application) : AbstractDIController(application) {

    private val courseDatabase: ICourseDatabase by di.instance()
    private val taskDatabase: ITaskDatabase by di.instance()

    private fun Route.createCourse() = post {
        if (!call.allowed("creator")) {
            throw ForbiddenException()
        }
        val request = call.receive<CourseCreationRequest>()
        request.userId = call.userId()
        val course = courseDatabase.createCourse(request)
        call.respond(HttpStatusCode.Created, course)
    }

    private fun Route.addTask() = post("/{id}/task") {
        if (!call.allowed("creator")) {
            throw ForbiddenException()
        }
        val courseId = call.parameters["id"] ?: throw InvalidRequestException("no id provided")
        var course =
            courseDatabase.getCourseById(courseId) ?: throw NotFoundException("course with id $courseId not found")
        val taskIds = call.receiveOrNull<List<String>>() ?: throw InvalidRequestException("no tasks provided")
        for (taskId in taskIds) {
            val task =
                taskDatabase.getTaskById(taskId) ?: throw InvalidRequestException("task with id $taskId not found")
            course.tasks.add(task)
        }
        course = courseDatabase.updateCourse(course)
        call.respond(HttpStatusCode.OK, course)
    }

    override fun Route.getRoutes() {
        authenticate {
            createCourse()
            addTask()
        }
    }

}