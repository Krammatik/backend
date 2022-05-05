package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.task.dto.TaskTypeDto
import org.kodein.di.DI

enum class TaskType : IDataTransferable<TaskTypeDto> {

    /**
     * Question Answer
     */
    DEFAULT {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.DEFAULT
        }
    },
    SINGLE_CHOICE {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.SINGLE_CHOICE
        }
    },
    MULTIPLE_CHOICE {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.MULTIPLE_CHOICE
        }
    },
    FIFTY_FIFTY {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.FIFTY_FIFTY
        }
    },
    GAP_TEXT {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.GAP_TEXT
        }
    },
    WORD_MATCH {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.WORD_MATCH
        }
    },
    SENTENCE_COMPLETION {
        override fun toTransferable(di: DI): TaskTypeDto {
            return TaskTypeDto.SENTENCE_COMPLETION
        }
    }

}