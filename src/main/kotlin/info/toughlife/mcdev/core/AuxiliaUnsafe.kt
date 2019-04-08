package info.toughlife.mcdev.core

import org.bukkit.World
import java.io.File

/**
 * A class that holds all functions that can be used ONLY in certain circumstances
 * Generally - stay away from it.
 */
internal object AuxiliaUnsafe {

    fun backupUnsafe(world: World) {
        val time = System.currentTimeMillis()

        // get files
        // compress files
        // upload files
        // profit?
    }

    fun getWorldFiles(world: World): List<File> {
        return listOf()
    }

    fun getRegionFiles(world: World): List<File> {
        return listOf()
    }

    fun compressUnsafe(file: File): File? {
        return null
    }

    fun uploadUnsafe(file: File) {

    }

}