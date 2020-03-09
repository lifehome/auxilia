package info.toughlife.mcdev.core

import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.commons.CONSOLE_REPLACEMENT
import org.bukkit.Bukkit
import org.bukkit.World

object AuxiliaManager {

    internal val queueHandler = AuxiliaQueueHandler()

    fun backup(world: World, player: String, now: Boolean) {
        if (now) {
//            AuxiliaUnsafe.backupUnsafeUrgent(world, player)
        }
        if (player == CONSOLE_MODIFIER) {
            AuxiliaQueue(world, CONSOLE_REPLACEMENT).finalizePriority()
            return
        }
        AuxiliaQueue(world, player).finalize()
    }

    fun backupAll(player: String, now: Boolean) {
        for (world in Bukkit.getWorlds()) {
            if (now) {
//                AuxiliaUnsafe.backupUnsafeUrgent(world, player)
                continue
            }
            backup(world, player, false)
        }
    }

}