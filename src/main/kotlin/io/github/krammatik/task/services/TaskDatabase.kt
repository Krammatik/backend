package io.github.krammatik.task.services

import com.mongodb.client.MongoClient
import io.github.krammatik.models.Task
import io.github.krammatik.task.dto.CreateTaskRequest
import org.kodein.di.DI
import org.kodein.di.instance
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

class TaskDatabase(val di: DI) : ITaskDatabase {

    private val mongoClient: MongoClient by di.instance()

    private val database get() = this.mongoClient.getDatabase("krammatik")
    private val taskCollection get() = this.database.getCollection<Task>("tasks")

    override fun getTaskById(id: String): Task? {
        return taskCollection.findOne(Task::id eq id)
    }

    override fun getTasksByTitle(title: String): List<Task> {
        return this.taskCollection.find(Task::title eq title).toList()
    }

    override fun getTasks(): List<Task> {
        return taskCollection.find().toList()
    }

    override fun createTask(createTaskRequest: CreateTaskRequest): Task {
        val task = createTaskRequest.toTransferable(di)
        this.taskCollection.insertOne(task)
        return task
    }

}