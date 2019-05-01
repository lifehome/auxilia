package info.toughlife.mcdev.core.scheduler

import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.core.AuxiliaManager
import org.bukkit.Bukkit

object AutoBackupTask : Runnable {
    override fun run() {
        for (world in Bukkit.getWorlds()) {
            world.save()
        }
        AuxiliaManager.backupAll(CONSOLE_MODIFIER, false)
        for (player in Bukkit.getOnlinePlayers()) {

        }
    }
}