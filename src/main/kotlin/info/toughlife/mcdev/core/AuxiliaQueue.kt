package info.toughlife.mcdev.core

import org.bukkit.World

class AuxiliaQueue(private val world: World, private val player: String)
    : Thread("AuxiliaBackupThread") {

    override fun run() {
        AuxiliaUnsafe.backupUnsafe(this.world, this.player)
        AuxiliaManager.queueHandler.allowNext = true
        AuxiliaManager.queueHandler.next()
    }

    fun finalize(): AuxiliaQueue {
        AuxiliaManager.queueHandler.addToQueue(this)
        return this
    }

    fun finalizePriority(): AuxiliaQueue {
        AuxiliaManager.queueHandler.addToQueuePriority(this)
        return this
    }
}