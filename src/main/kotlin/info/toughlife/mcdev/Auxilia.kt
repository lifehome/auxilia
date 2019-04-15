package info.toughlife.mcdev

import com.google.api.services.drive.Drive
import info.toughlife.mcdev.core.AuxiliaManager
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
        lateinit var drive: Drive
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

        drive = GoogleDriveAPI.createService()
        // test
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "1")
    }
}