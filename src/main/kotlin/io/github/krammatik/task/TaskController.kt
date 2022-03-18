package io.github.krammatik.task

import io.github.krammatik.plugins.ForbiddenException
import io.github.krammatik.task.dto.CreateTaskRequest
import io.github.krammatik.task.services.ITaskDatabase
import io.github.krammatik.user.allowed
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class TaskController(application: Application) : AbstractDIController(application) {

    private val taskDatabase: ITaskDatabase by di.instance()

    private fun Route.createTask() = post {
        if (!call.allowed("creator")) {
            throw ForbiddenException()
        }
        val request = call.receive<CreateTaskRequest>()
        val task = taskDatabase.createTask(request)
        call.respond(HttpStatusCode.Created, task)
    }

    private fun Route.getTasks() = get {
        val tasks = taskDatabase.getTasks()
        call.respond(tasks)
    }

    private fun Route.getTaskById() = get("/{id}") {
        
    }

    override fun Route.getRoutes() {
        authenticate {
            createTask()
            getTasks()
        }
    }

}