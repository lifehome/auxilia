package info.toughlife.mcdev

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

        // test
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "1")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "2")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "3")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "4")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "5")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "6")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "7")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "8")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "9")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "10")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "11")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "12")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "13")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "14")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "15")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "16")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "17")
        AuxiliaManager.backup(Bukkit.getWorld("world")!!, "18")
    }
}