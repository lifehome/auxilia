package info.toughlife.mcdev.core

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.FileFetcher
import org.bukkit.World

/**
 * A class that holds all functions that can be used ONLY in certain circumstances
 * Generally - stay away from it.
 */
internal object AuxiliaUnsafe {

    fun backupUnsafe(world: World) {
        val time = System.currentTimeMillis()
        val tempFolder = Auxilia.instance.dataFolder.absolutePath + "/temp_$time.7z"

        val worldFiles = FileFetcher.fetchWorldFiles(world.name) ?: return

        FileCompressionManager.compress(tempFolder, worldFiles)
    }

}