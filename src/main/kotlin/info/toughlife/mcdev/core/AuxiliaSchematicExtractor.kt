package info.toughlife.mcdev.core

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.util.io.Closer
import com.sk89q.worldguard.WorldGuard
import info.toughlife.mcdev.Auxilia
import org.bukkit.Bukkit
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


object AuxiliaSchematicExtractor {
    private val TEMP_SCHEMATICS = Auxilia.instance.dataFolder.absolutePath + "/_tempSchematics"

    init {
        File(TEMP_SCHEMATICS).mkdir()
    }

    fun extractSchematics(worldName: String): Map<String, File>? {
        val world = Bukkit.getWorld(worldName) ?: return null
        val map = mutableMapOf<String, File>()

        val container = WorldGuard.getInstance().platform.regionContainer
        val regionManager = container.get(BukkitAdapter.adapt(world))

        for ((id, region) in regionManager!!.regions) {
            val cuboidRegion = CuboidRegion(region.minimumPoint, region.maximumPoint)
            val target = BlockArrayClipboard(cuboidRegion)

            val format = ClipboardFormats.findByAlias("schem")

            val outputFile = File("$TEMP_SCHEMATICS/$id.schem")

            Closer.create().use { closer ->
                val fos = closer.register(FileOutputStream(outputFile))
                val bos = closer.register(BufferedOutputStream(fos))
                val writer = closer.register(format!!.getWriter(bos))
                writer.write(target)
            }

            map["schematics/$id.schem"] = outputFile
        }

        return map
    }
}