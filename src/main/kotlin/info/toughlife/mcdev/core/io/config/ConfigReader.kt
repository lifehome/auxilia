package info.toughlife.mcdev.core.io.config

import com.jsoniter.JsonIterator
import info.toughlife.mcdev.Auxilia

object ConfigReader {

    private val CONFIG = Auxilia.instance.config

    private lateinit var configInfo: ConfigInfo

    internal fun readConfig() {
        val json = ConfigFileHandler.readFile()

        this.configInfo = JsonIterator.deserialize<ConfigInfo>(json, ConfigInfo::class.java)
    }

    internal fun getDefaults(): ConfigInfo {
        return ConfigInfo()
    }
}