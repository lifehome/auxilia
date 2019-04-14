package info.toughlife.mcdev.core.io

import org.bukkit.Bukkit
import java.io.File

object FileFetcher {

    fun fetchWorldFiles(worldName: String): Array<File>? {
        val world = Bukkit.getWorld(worldName) ?: return null
        val folder = world.worldFolder

        return folder.listFiles()
    }

}