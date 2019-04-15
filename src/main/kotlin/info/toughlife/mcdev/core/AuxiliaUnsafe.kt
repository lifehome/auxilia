package info.toughlife.mcdev.core

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.DriveManager
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.FileFetcher
import info.toughlife.mcdev.core.io.FileNameCreator
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

    fun backupUnsafe(queue: AuxiliaQueue, world: World, player: String) {
        val fileName = FileNameCreator.createBackupName(world.name, player)
        val outputName = TEMP_PATH + fileName
        queue.fileName = fileName

        queue.currentAction = AuxiliaQueueAction.FETCH
        val worldFiles = FileFetcher.fetchWorldFiles(world.name) ?: return
        val schematics = AuxiliaSchematicExtractor.extractSchematics(world.name) ?: return

        val result = worldFiles.toMutableMap()
        result.putAll(schematics)

        queue.currentAction = AuxiliaQueueAction.COMPRESS
        FileCompressionManager.compress(outputName, result)
        val file = java.io.File(outputName)

        queue.currentAction = AuxiliaQueueAction.UPLOAD
        DriveManager.upload(file)

        queue.currentAction = AuxiliaQueueAction.CLEANUP
        file.delete()
    }

}