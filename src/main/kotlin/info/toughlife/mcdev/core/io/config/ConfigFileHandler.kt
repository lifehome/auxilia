package info.toughlife.mcdev.core.io.config

import info.toughlife.mcdev.Auxilia
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigFileHandler {

    private val DATA_FOLDER = Auxilia.instance.dataFolder
    private val FILE = File(DATA_FOLDER.absolutePath + "/config.json")

    fun createFile() {
        DATA_FOLDER.mkdirs()

        if (!FILE.exists()) {
            FILE.createNewFile()
            FILE.writeText(this::class.java.classLoader
                .getResource("config.json").readText())
        }
    }

    fun readFile(): String {
        return FILE.readText(StandardCharsets.UTF_8)
    }

}