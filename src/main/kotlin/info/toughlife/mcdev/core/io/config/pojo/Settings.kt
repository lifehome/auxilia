package info.toughlife.mcdev.core.io.config.pojo

import java.io.Serializable

data class Settings(val maxQueuedJobs: Int = 3,
                    val cooldownMinutes: Int = 5,
                    val compressArchive: Boolean = true,
                    val deleteAfterUpload: Boolean = false) : Serializable