package info.toughlife.mcdev.commons

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun String.colored(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun Player.sendColoredMessage(text: String) {
    this.sendMessage(text.colored())
}

fun CommandSender.sendColoredMessage(text: String) {
    this.sendMessage(text.colored())
}