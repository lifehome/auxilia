package info.toughlife.mcdev.core.io

import info.toughlife.mcdev.core.io.compress.CompressionMethod
import info.toughlife.mcdev.core.io.compress.impl.SevenZCompressor
import info.toughlife.mcdev.core.io.compress.impl.TarCompressor
import info.toughlife.mcdev.core.io.config.configInfo
import java.io.File

object FileCompressionManager {

    private val SEVENZ_COMPRESSOR = SevenZCompressor()
    private val TAR_COMPRESSOR = TarCompressor()

    private val COMPRESSION_METHOD = CompressionMethod.valueOf(configInfo().compressionSettings.method)
    private val COMPRESSION_ALGHORITM = configInfo().compressionSettings.compressionAlghoritm

    fun compress(output: String, vararg files: File) {
        when (COMPRESSION_ALGHORITM) {
            "7z" -> compress7z(output, *files)
            "tar" -> compressTar(output, *files)
        }
    }

    internal fun compress7z(output: String, vararg files: File) {
        SEVENZ_COMPRESSOR.compress(output, COMPRESSION_METHOD, *files)
    }

    internal fun compressTar(output: String, vararg files: File) {
        TAR_COMPRESSOR.compress(output, COMPRESSION_METHOD, *files)
    }

}