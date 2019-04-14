package info.toughlife.mcdev.core

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.FileFetcher
import info.toughlife.mcdev.core.io.FileNameCreator
import org.bukkit.World

/**
 * A class that holds all functions that can be used ONLY in certain circumstances
 * Generally - stay away from it.
 */
internal object AuxiliaUnsafe {

    fun backupUnsafe(world: World, player: String) {
        val outputName = Auxilia.instance.dataFolder.absolutePath + "/" +
                FileNameCreator.createBackupName(world.name, player)

        val worldFiles = FileFetcher.fetchWorldFiles(world.name) ?: return

        FileCompressionManager.compress(outputName, worldFiles)
    }

}