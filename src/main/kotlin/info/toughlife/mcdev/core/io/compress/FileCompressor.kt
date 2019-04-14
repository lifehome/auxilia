package info.toughlife.mcdev.core.io.compress

import java.io.File

interface FileCompressor<T> {

    fun compress(output: String, alghoritm: CompressionAlghoritm, map: Map<String, File>)
    fun addToArchive(output: T, file: File, dir: String)

}