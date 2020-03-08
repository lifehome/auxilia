package info.toughlife.mcdev

import com.google.api.services.drive.Drive
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import info.toughlife.mcdev.core.command.AuxiliaCommand
import info.toughlife.mcdev.core.io.config.ConfigFileHandler
import info.toughlife.mcdev.core.io.config.ConfigReader
import info.toughlife.mcdev.core.io.config.Messages
import info.toughlife.mcdev.core.io.config.configInfo
import info.toughlife.mcdev.core.io.drive.DriveOptions
// import info.toughlife.mcdev.core.io.drive.DrivePersonalOptions
import info.toughlife.mcdev.core.io.drive.DriveTeamOptions
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class Auxilia : JavaPlugin() {
    companion object {
        lateinit var instance: Auxilia
            private set
        lateinit var drive: Drive
            private set
        lateinit var driveOptions: DriveOptions
            private set
        lateinit var worldEdit: WorldEditPlugin
            private set
        lateinit var messages: FileConfiguration
            private set
    }

    override fun onLoad() {
        instance = this
    }

    @Throws(IllegalStateException::class)
    override fun onEnable() {
        logger.info("Loading resources...")
        ConfigFileHandler.createFile()
        ConfigReader.readConfig()

        val metrics = Metrics(this)

        Messages.saveDefault()
        messages = Messages.getConfig()

// HOTFIX: Buggy personal drive implementation
//         Disabled due to no path designator.
//---------------------------------------------------
        if (configInfo().teamDriveId == "") {
            throw IllegalStateException("OMG There is no Shared Drive ID specified.")
//            driveOptions = DrivePersonalOptions()
//            drive = driveOptions.connect()
        }
        else {
            driveOptions = DriveTeamOptions()
            drive = driveOptions.connect()
        }

        worldEdit = Bukkit.getServer().pluginManager.getPlugin("WorldEdit") as WorldEditPlugin

        getCommand("auxilia")!!.setExecutor(AuxiliaCommand)
    }
}