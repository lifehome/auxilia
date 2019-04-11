package info.toughlife.mcdev.core.io.compress

import java.io.File

abstract class FileCompressor<T> {

    abstract fun compress(output: String, method: CompressionMethod, vararg files: File)
    abstract fun addToArchive(output: T, file: File, dir: String)

}