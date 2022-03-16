package io.github.krammatik.dto

import org.kodein.di.DI

interface IDataTransferable<T> {

    fun toTransferable(di: DI): T

}