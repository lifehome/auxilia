package info.toughlife.mcdev.core

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.FileFetcher
import info.toughlife.mcdev.core.io.FileNameCreator
import info.toughlife.mcdev.core.io.FileUploader
import org.bukkit.World
import java.io.File

/**
 * A class that holds all functions that can be used ONLY in certain circumstances
 * Generally - stay away from it.
 */
internal object AuxiliaUnsafe {

    val TEMP_PATH = Auxilia.instance.dataFolder.absolutePath + "/_temp/"

    init {
        File(TEMP_PATH).mkdir()
    }

    fun backupUnsafe(world: World, player: String) {
        val outputName = TEMP_PATH +
                FileNameCreator.createBackupName(world.name, player)

        val worldFiles = FileFetcher.fetchWorldFiles(world.name) ?: return
        val wgFiles = FileFetcher.fetchWorldGuardFiles(world.name) ?: return

        val result = worldFiles.toMutableMap()
        result.putAll(wgFiles)

        FileCompressionManager.compress(outputName, result)
        val file = java.io.File(outputName)

        FileUploader.upload(file)

        file.delete()
    }

}