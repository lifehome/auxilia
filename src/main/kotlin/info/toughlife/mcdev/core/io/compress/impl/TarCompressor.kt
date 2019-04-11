package info.toughlife.mcdev.core.io.compress.impl

import info.toughlife.mcdev.core.io.compress.CompressionMethod
import info.toughlife.mcdev.core.io.compress.FileCompressor
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.compressors.gzip.GzipParameters
import org.apache.commons.compress.utils.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.Deflater

class TarCompressor : FileCompressor<TarArchiveOutputStream>() {
    override fun addToArchive(output: TarArchiveOutputStream, file: File, dir: String) {
        val entry = dir + File.separator + file.name
        if (file.isFile) {
            output.putArchiveEntry(TarArchiveEntry(file, entry))
            FileInputStream(file).use { input -> IOUtils.copy(input, output) }
            output.closeArchiveEntry()
        } else if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (child in children) {
                    addToArchive(output, child, entry)
                }
            }
        }
    }

    override fun compress(output: String, method: CompressionMethod, vararg files: File) {
        val parameters = GzipParameters()
        parameters.compressionLevel = Deflater.BEST_COMPRESSION

        val outputStream = if (method == CompressionMethod.GZIP) {
            GzipCompressorOutputStream(FileOutputStream(output), parameters)
        } else FileOutputStream(output)

        val taos = TarArchiveOutputStream(outputStream)
        taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR)
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
        taos.setAddPaxHeadersForNonAsciiNames(true)

        taos.use { out ->
            for (file in files) {
                addToArchive(out, file, ".")
            }
        }
    }

}