package info.toughlife.mcdev.core.io

import java.io.File
import java.io.IOException
import java.util.zip.GZIPOutputStream
import java.io.FileOutputStream
import java.io.FileInputStream

internal object FileCompressor {

    fun compressLZMA2(file: File, outputPath: String): File? {
        return null
    }

    fun compressGZIP(file: File, outputPath: String): File? {
        val inputStream = FileInputStream(file)
        val outputStream = FileOutputStream(outputPath)
        val gzipOutputStream = GZIPOutputStream(outputStream)
        val buffer = ByteArray(1024)
        while (true) {
            val len = inputStream.read(buffer)
            if (len == -1)
                break
            gzipOutputStream.write(buffer, 0, len)
        }
        gzipOutputStream.close()
        outputStream.close()
        inputStream.close()
        return null
    }

}

fun File.lzma2(outputPath: String): File {
    return FileCompressor.compressLZMA2(this, outputPath)
        ?: throw IOException("LZMA2 compression was unsuccessfull")
}

fun File.gzip(outputPath: String): File {
    return FileCompressor.compressGZIP(this, outputPath)
        ?: throw IOException("GZIP compression was unsuccessfull")
}