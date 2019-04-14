package info.toughlife.mcdev.core.io

import org.bukkit.Bukkit
import java.io.File

object FileFetcher {

    fun fetchWorldFiles(worldName: String): Map<String, File>? {
        val world = Bukkit.getWorld(worldName) ?: return null
        val folder = world.worldFolder

        val map = mutableMapOf<String, File>()
        listFiles(folder.absolutePath, "", "$worldName/", map)

        return map
    }

    private fun listFiles(directoryName: String, deepFolder: String, prefix: String, files: MutableMap<String, File>) {
        val directory = File("$directoryName/$deepFolder")
        val fileList = directory.listFiles()

        if (fileList != null) {
            for (file in fileList) {
                if (file.isFile) {
                    files[prefix + file.path
                        .replace(directoryName, "")
                        .replaceFirst("\\", "")] = file
                }
                else if (file.isDirectory) {
                    listFiles(directoryName, file.path
                        .replace(directoryName, "")
                        .replaceFirst("\\", ""), prefix, files)
                }
            }
        }
    }
}