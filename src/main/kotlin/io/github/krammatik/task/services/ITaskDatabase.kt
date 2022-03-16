package io.github.krammatik.task.services

import io.github.krammatik.models.Task
import io.github.krammatik.task.dto.CreateTaskRequest

interface ITaskDatabase {

    fun getTaskById(id: String): Task?

    fun createTask(createTaskRequest: CreateTaskRequest): Task

}
