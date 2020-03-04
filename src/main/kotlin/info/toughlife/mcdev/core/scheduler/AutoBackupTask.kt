package info.toughlife.mcdev.core.scheduler

import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.core.AuxiliaManager
import org.bukkit.Bukkit

object AutoBackupTask : Runnable {
    override fun run() {
        AuxiliaManager.backupAll(CONSOLE_MODIFIER, false)
    }
}