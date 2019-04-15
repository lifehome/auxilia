package info.toughlife.mcdev.commons

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit



const val CONSOLE_MODIFIER = "~~!CONSOLE!~~"
const val CONSOLE_REPLACEMENT = "CONSOLE"

fun String.colored(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun Player.sendColoredMessage(text: String) {
    this.sendMessage(text.colored())
}

fun CommandSender.sendColoredMessage(text: String) {
    this.sendMessage(text.colored())
}

fun formatInterval(l: Long): String {
    val hr = TimeUnit.MILLISECONDS.toHours(l)
    val min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr))
    val sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min))
    val ms = TimeUnit.MILLISECONDS.toMillis(
        l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec)
    )
    return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms)
}