package io.github.krammatik.task.dto

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.models.Solution
import kotlinx.serialization.Serializable
import org.kodein.di.DI

@Serializable
data class SolutionDto(
    val value: String, val correct: Boolean
) : IDataTransferable<Solution> {

    override fun toTransferable(di: DI): Solution {
        return Solution(value, correct)
    }

}
