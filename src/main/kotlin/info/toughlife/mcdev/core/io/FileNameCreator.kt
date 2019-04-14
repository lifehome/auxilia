package info.toughlife.mcdev.core.io

import java.text.SimpleDateFormat

object FileNameCreator {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd-HHmm")

    internal fun createBackupName(worldName: String, player: String): String {
        val time = dateFormat.format(System.currentTimeMillis())

        return "${time}_${worldName}_$player" + FileCompressionManager.getExtensionSyntax()
    }

}