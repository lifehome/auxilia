package info.toughlife.mcdev.core.io.config

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.commons.sendColoredMessage
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object Messages {

    private val FILE = File(Auxilia.instance.dataFolder.absolutePath + "/messages.yml")

    fun saveDefault() {
        if (!FILE.exists())
            FILE.createNewFile()

        FILE.writeText(this::class.java.classLoader
            .getResource("messages.yml").readText())
    }

    fun getConfig(): FileConfiguration {
        return YamlConfiguration.loadConfiguration(FILE)
    }
}

fun CommandSender.sendIO(messagePath: String) {
    this.sendColoredMessage(message(messagePath))
}

fun Player.sendIO(messagePath: String) {
    this.sendColoredMessage(message(messagePath))
}

fun message(messagePath: String): String {
    return Auxilia.messages.getString(messagePath)!!
}

fun getConfigList(listPath: String): List<String> {
    return Auxilia.messages.getStringList(listPath)
}