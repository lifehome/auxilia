package info.toughlife.mcdev.core

import org.bukkit.World

object AuxiliaManager {

    internal val queueHandler = AuxiliaQueueHandler()

    fun backup(world: World, player: String) {
        AuxiliaQueue(world, player).finalize()
    }
}