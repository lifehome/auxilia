package info.toughlife.mcdev.core

import org.bukkit.World

class AuxiliaQueue(val world: World, val player: String)
    : Thread("AuxiliaBackupThread") {

    var currentAction = AuxiliaQueueAction.STARTING
    var started: Long = 0
    var fileName: String = ""

    override fun run() {
        started = System.currentTimeMillis()
        AuxiliaUnsafe.backupUnsafe(this, this.world, this.player)
        AuxiliaManager.queueHandler.current = null
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