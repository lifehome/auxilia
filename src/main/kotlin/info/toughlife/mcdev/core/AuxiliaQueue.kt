package info.toughlife.mcdev.core

import org.bukkit.World

class AuxiliaQueue(private val world: World)
    : Thread("AuxiliaBackupQueue") {

    override fun run() {
        AuxiliaUnsafe.backupUnsafe(this.world)
    }

    fun finalize(): AuxiliaQueue {
        AuxiliaManager.queueHandler.addToQueue(this)
        return this
    }
}