package info.toughlife.mcdev.core.io.config.pojo

import java.io.Serializable

data class UploadSettings(val clientId: String = "",
                          val clientSecret: String = "",
                          val pathToChroot: String = "") : Serializable