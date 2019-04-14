package info.toughlife.mcdev.core.io.compress.impl

import info.toughlife.mcdev.core.io.compress.CompressionAlghoritm
import info.toughlife.mcdev.core.io.compress.FileCompressor
import org.apache.commons.compress.archivers.sevenz.SevenZMethod
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile
import java.io.File
import java.io.FileInputStream

class SevenZCompressor : FileCompressor<SevenZOutputFile> {
    override fun addToArchive(output: SevenZOutputFile, file: File, dir: String) {
        if (file.isFile) {
            val entry = output.createArchiveEntry(file, dir)
            output.putArchiveEntry(entry)

            val inputStream = FileInputStream(file)
            val b = ByteArray(1024)
            while (true) {
                val count = inputStream.read(b)
                if (count <= 0)
                    break
                output.write(b, 0, count)
            }
            output.closeArchiveEntry()
        } else if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (child in children) {
                    addToArchive(output, child, dir)
                }
            }
        }
    }

    override fun compress(output: String, alghoritm: CompressionAlghoritm, map: Map<String, File>) {
        SevenZOutputFile(File(output)).use { out ->
            out.setContentCompression(SevenZMethod.LZMA2)
            for ((localPath, file) in map) {
                addToArchive(out, file, localPath)
            }
        }
    }

}