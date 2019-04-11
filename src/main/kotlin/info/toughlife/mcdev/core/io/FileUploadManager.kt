package info.toughlife.mcdev.core.io

import java.io.File

internal object FileUploader {

    fun upload(file: File) {

    }

}

fun File.upload() {
    FileUploader.upload(this)
}