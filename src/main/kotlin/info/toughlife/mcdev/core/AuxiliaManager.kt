package info.toughlife.mcdev.core

import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.commons.CONSOLE_REPLACEMENT
import org.bukkit.World

object AuxiliaManager {

    internal val queueHandler = AuxiliaQueueHandler()

    fun backup(world: World, player: String) {
        if (player == CONSOLE_MODIFIER) {
            AuxiliaQueue(world, CONSOLE_REPLACEMENT).finalizePriority()
            return
        }

        AuxiliaQueue(world, player).finalize()
    }
}