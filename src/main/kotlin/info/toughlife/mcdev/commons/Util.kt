package info.toughlife.mcdev.commons

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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