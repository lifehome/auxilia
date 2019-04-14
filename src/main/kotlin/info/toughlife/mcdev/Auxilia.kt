package info.toughlife.mcdev

import info.toughlife.mcdev.core.AuxiliaUnsafe
import info.toughlife.mcdev.core.io.config.ConfigFileHandler
import info.toughlife.mcdev.core.io.config.ConfigReader
import info.toughlife.mcdev.core.io.config.configInfo
import info.toughlife.mcdev.core.scheduler.AutoBackupTask
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Auxilia : JavaPlugin() {
    companion object {
        lateinit var instance: Auxilia
            private set
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        logger.info("Loading resources...")
        ConfigFileHandler.createFile()
        ConfigReader.readConfig()

        println(configInfo())

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, AutoBackupTask,
            (configInfo().settings.cooldownMinutes * 60) * 20L,
            (configInfo().settings.cooldownMinutes * 60) * 20L)

        AuxiliaUnsafe.backupUnsafe(Bukkit.getWorld("world")!!)
    }
}