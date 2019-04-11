package info.toughlife.mcdev.core.io.config

import info.toughlife.mcdev.core.io.config.pojo.CompressionSettings
import info.toughlife.mcdev.core.io.config.pojo.Settings
import info.toughlife.mcdev.core.io.config.pojo.UploadSettings
import java.io.Serializable

data class ConfigInfo(val version: Int = 1, val settings: Settings = Settings(),
                      val compressionSettings: CompressionSettings = CompressionSettings(),
                      val uploadSettings: UploadSettings = UploadSettings()) : Serializable