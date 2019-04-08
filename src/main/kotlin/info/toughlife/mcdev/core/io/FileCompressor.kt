package info.toughlife.mcdev.core.io

import java.io.File
import java.io.IOException

internal object FileCompressor {

    fun compressLZMA2(file: File): File? {
        return null
    }

    fun compressGZIP(file: File): File? {
        return null
    }

}

fun File.lzma2(): File {
    return FileCompressor.compressLZMA2(this)
        ?: throw IOException("LZMA2 compression was unsuccessfull")
}

fun File.gzip(): File {
    return FileCompressor.compressGZIP(this)
        ?: throw IOException("GZIP compression was unsuccessfull")
}