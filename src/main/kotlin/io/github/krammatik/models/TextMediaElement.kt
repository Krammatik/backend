package io.github.krammatik.models

import io.github.krammatik.dto.IDataTransferable
import io.github.krammatik.dto.TextMediaElementDto
import org.kodein.di.DI

data class TextMediaElement(
    val text: String,
    val imgData: List<String>
) : IDataTransferable<TextMediaElementDto> {

    override fun toTransferable(di: DI): TextMediaElementDto {
        return TextMediaElementDto(text, imgData)
    }

}