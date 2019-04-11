package info.toughlife.mcdev.core.io.config.pojo

import java.io.Serializable

data class CompressionSettings(val method: String = "7z",
                               val compressionAlghoritm: String = "LZMA2") : Serializable