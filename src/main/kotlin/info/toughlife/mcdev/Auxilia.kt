package info.toughlife.mcdev

import com.google.api.services.drive.Drive
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import info.toughlife.mcdev.core.command.AuxiliaCommand
import info.toughlife.mcdev.core.io.DriveManager
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
        lateinit var worldEdit: WorldEditPlugin
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

        worldEdit = Bukkit.getServer().pluginManager.getPlugin("WorldEdit") as WorldEditPlugin

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, AutoBackupTask,
            (configInfo().settings.cooldownMinutes * 60) * 20L,
            (configInfo().settings.cooldownMinutes * 60) * 20L)

        getCommand("auxilia")!!.setExecutor(AuxiliaCommand)

        drive = GoogleDriveAPI.createService()

        println(DriveManager.listFiles())
        // test
        //AuxiliaManager.backup(Bukkit.getWorld("world")!!, "1")
    }
}