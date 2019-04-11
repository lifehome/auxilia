package info.toughlife.mcdev.core.io.compress

import java.io.File

interface FileCompressor<T> {

    fun compress(output: String, method: CompressionMethod, vararg files: File)
    fun addToArchive(output: T, file: File, dir: String)

}