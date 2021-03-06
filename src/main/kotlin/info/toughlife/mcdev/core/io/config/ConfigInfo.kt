package info.toughlife.mcdev.core.io.config

import info.toughlife.mcdev.core.io.config.pojo.CompressionSettings
import info.toughlife.mcdev.core.io.config.pojo.Settings
import java.io.Serializable

data class ConfigInfo(val version: Int = 1,
                      val teamDriveId: String = "",
                      val settings: Settings = Settings(),
                      val compressionSettings: CompressionSettings = CompressionSettings()) : Serializable