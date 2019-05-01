package info.toughlife.mcdev.core

import info.toughlife.mcdev.Auxilia
import org.bukkit.Bukkit
import java.util.*

object ConfirmationHandler {
    private val confirmationAwaiting = mutableMapOf<UUID, ConfirmationAction>()

    fun makeConfirmRequest(uniqueId: UUID, action: ConfirmationAction) {
        confirmationAwaiting[uniqueId] = action
        Bukkit.getScheduler().runTaskLaterAsynchronously(Auxilia.instance, Runnable{
            if (confirmationAwaiting.containsKey(uniqueId)) {
                confirmationAwaiting.remove(uniqueId)
            }
        }, 20 * 60)
    }

    fun confirm(uniqueId: UUID) {
        if (confirmationAwaiting.containsKey(uniqueId)) {
            confirmationAwaiting[uniqueId]?.run()
        }
    }

    fun isConfirmationValid(uniqueId: UUID): Boolean {
        return confirmationAwaiting.containsKey(uniqueId)
    }
}

interface ConfirmationAction {
    fun run()
}