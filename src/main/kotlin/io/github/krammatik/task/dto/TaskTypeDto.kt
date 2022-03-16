package io.github.krammatik.task.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.models.TaskType
import org.kodein.di.DI

enum class TaskTypeDto : IDataTransferable<TaskType> {
    DEFAULT,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    FIFTY_FIFTY,
    GAP_TEXT,
    WORD_MATCH,
    SENTENCE_COMPLETION;

    override fun toTransferable(di: DI): TaskType {
        return enumValueOf(this.name)
    }
}