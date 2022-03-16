package io.github.krammatik.dto

import io.github.krammatik.models.TextMediaElement
import kotlinx.serialization.Serializable
import org.kodein.di.DI

@Serializable
data class TextMediaElementDto(
    val text: String,
    val imgData: List<String>
) : IDataTransferable<TextMediaElement> {

    override fun toTransferable(di: DI): TextMediaElement {
        return TextMediaElement(this.text, this.imgData)
    }

}