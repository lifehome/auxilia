package info.toughlife.mcdev.core

import org.bukkit.World

object AuxiliaManager {

    internal val queueHandler = AuxiliaQueueHandler()

    fun backup(world: World) {
        AuxiliaQueue(world).finalize()
    }

}