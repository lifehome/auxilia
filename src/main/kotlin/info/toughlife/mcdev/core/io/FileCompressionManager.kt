package info.toughlife.mcdev.core.io

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.compress.CompressionMethod
import info.toughlife.mcdev.core.io.compress.impl.SevenZCompressor
import info.toughlife.mcdev.core.io.compress.impl.TarCompressor
import info.toughlife.mcdev.core.io.config.configInfo
import java.io.File

object FileCompressionManager {

    private val SEVENZ_COMPRESSOR = SevenZCompressor()
    private val TAR_COMPRESSOR = TarCompressor()

    private var COMPRESSION_METHOD : CompressionMethod = try {
        CompressionMethod.valueOf(configInfo().compressionSettings.method)
    } catch (e: IllegalArgumentException) {
        CompressionMethod.NONE
    }
    private val COMPRESSION_ALGHORITM = configInfo().compressionSettings.compressionAlghoritm

    init {
        if (COMPRESSION_METHOD == CompressionMethod.NONE) {
            if (COMPRESSION_ALGHORITM == "7z") {
                Auxilia.instance.logger.severe("No compression method is set for 7z compression alghoritm. Defaulting to LZMA2.")
                COMPRESSION_METHOD = CompressionMethod.LZMA2
            }
            else {
                Auxilia.instance.logger.severe("Auxilia is set to not compressing archives, " +
                        "this will significantly increase the file size of each archive, " +
                        "and use up the server disk space very quickly.")
            }
        }
    }

    fun compress(output: String, vararg files: File) {
        when (COMPRESSION_ALGHORITM) {
            "7z" -> compress7z(output, *files)
            "tar" -> compressTar(output, *files)
        }
    }

    private fun compress7z(output: String, vararg files: File) {
        SEVENZ_COMPRESSOR.compress(output, COMPRESSION_METHOD, *files)
    }

    private fun compressTar(output: String, vararg files: File) {
        TAR_COMPRESSOR.compress(output, COMPRESSION_METHOD, *files)
    }

}