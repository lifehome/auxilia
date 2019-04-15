package info.toughlife.mcdev.core.io

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.compress.CompressionAlghoritm
import info.toughlife.mcdev.core.io.compress.impl.SevenZCompressor
import info.toughlife.mcdev.core.io.compress.impl.TarCompressor
import info.toughlife.mcdev.core.io.config.configInfo
import java.io.File

object FileCompressionManager {

    private val SEVENZ_COMPRESSOR = SevenZCompressor()
    private val TAR_COMPRESSOR = TarCompressor()

    var COMPRESSION_ALGHORITM : CompressionAlghoritm = try {
        CompressionAlghoritm.valueOf(configInfo().compressionSettings.alghoritm)
    } catch (e: IllegalArgumentException) {
        CompressionAlghoritm.NONE
    }
    val COMPRESSION_METHOD = configInfo().compressionSettings.method

    init {
        Auxilia.instance.logger.info("Selected compression alghoritm: ${COMPRESSION_ALGHORITM.toString().toLowerCase()}")
        Auxilia.instance.logger.info("Selected compression method: ${COMPRESSION_METHOD.toLowerCase()}")
        if (COMPRESSION_ALGHORITM == CompressionAlghoritm.NONE) {
            if (COMPRESSION_METHOD == "7z") {
                Auxilia.instance.logger.severe("No compression method is set for 7z compression alghoritm. Defaulting to LZMA2.")
                COMPRESSION_ALGHORITM = CompressionAlghoritm.LZMA2
            }
            else {
                Auxilia.instance.logger.severe("Auxilia is set to not compressing archives, " +
                        "this will significantly increase the file size of each archive, " +
                        "and use up the server disk space very quickly.")
            }
        }
    }

    fun compress(output: String, map: Map<String, File>) {
        when (COMPRESSION_METHOD) {
            "7z" -> compress7z(output, map)
            "tar" -> compressTar(output, map)
        }
    }

    fun getExtensionSyntax(): String {
        return when (COMPRESSION_METHOD) {
            "7z" -> ".7z"
            "tar" -> {
                if (COMPRESSION_ALGHORITM == CompressionAlghoritm.GZIP) {
                    ".tar.gz"
                }
                else ".tar"
            }
            else -> ".err"
        }
    }

    fun getMimeType(): String {
        return when (COMPRESSION_METHOD) {
            "7z" -> "application/x-7z-compressed"
            "tar" -> {
                if (COMPRESSION_ALGHORITM == CompressionAlghoritm.GZIP) {
                    "application/tar+gzip"
                }
                else "application/tar"
            }
            else -> "error"
        }
    }

    private fun compress7z(output: String, map: Map<String, File>) {
        SEVENZ_COMPRESSOR.compress(output, COMPRESSION_ALGHORITM, map)
    }

    private fun compressTar(output: String, map: Map<String, File>) {
        TAR_COMPRESSOR.compress(output, COMPRESSION_ALGHORITM, map)
    }

}