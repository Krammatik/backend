package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.task.dto.SolutionDto
import org.kodein.di.DI

data class Solution(
    val value: String,
    val correct: Boolean
) : IDataTransferable<SolutionDto> {
    override fun toTransferable(di: DI): SolutionDto {
        return SolutionDto(value, correct)
    }

}