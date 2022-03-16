package io.github.krammatik.task

import io.github.krammatik.plugins.ForbiddenException
import io.github.krammatik.user.allowed
import io.ktor.application.*
import io.ktor.routing.*
import org.kodein.di.ktor.controller.AbstractDIController

class TaskController(application: Application) : AbstractDIController(application) {

    private fun Route.createTask() = post {
        if (!call.allowed("creator")) {
            throw ForbiddenException()
        }
    }

    override fun Route.getRoutes() {
        createTask()
    }

}