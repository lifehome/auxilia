package info.toughlife.mcdev.core.io.config

import com.jsoniter.output.JsonStream
import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.commons.prettyPrint
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigFileHandler {

    private val DATA_FOLDER = Auxilia.instance.dataFolder
    private val FILE = File(DATA_FOLDER.absolutePath + "/config.json")

    fun createFile() {
        DATA_FOLDER.mkdirs()

        if (!FILE.exists()) {
            FILE.createNewFile()

            val default = ConfigReader.getDefaults()
            FILE.writeText(JsonStream.serialize(default).prettyPrint())
        }
    }

    fun readFile(): String {
        return FILE.readText(StandardCharsets.UTF_8)
    }

}