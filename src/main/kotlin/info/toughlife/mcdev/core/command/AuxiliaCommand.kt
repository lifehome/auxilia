package info.toughlife.mcdev.core.command

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.commons.formatInterval
import info.toughlife.mcdev.commons.sendColoredMessage
import info.toughlife.mcdev.core.AuxiliaManager
import info.toughlife.mcdev.core.ConfirmationAction
import info.toughlife.mcdev.core.ConfirmationHandler
import info.toughlife.mcdev.core.io.DriveManager
import info.toughlife.mcdev.core.io.FileCompressionManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object AuxiliaCommand : CommandExecutor {
    const val DELIMITER = "&9&l<====================================>"
    override fun onCommand(sender: CommandSender, command: Command, label: String,
                           args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendColoredMessage(DELIMITER)
            sender.sendColoredMessage("&f* &9/auxilia &3backup &f[--allworlds | --noconfirm]")
            sender.sendColoredMessage("&f* &9/auxilia &3status")
            sender.sendColoredMessage("&f* &9/auxilia &3pendings")
            sender.sendColoredMessage("&f* &9/auxilia &3cancel &f[--all]")
            sender.sendColoredMessage("&f* &9/auxilia &3urgent")
            sender.sendColoredMessage("&f* &9/auxilia &3list")
            return true
        }

        when (args[0]) {
            "backup" -> {
                var allWorlds = false
                var noConfirm = false
                if (args.size > 1) {
                    if (args[1] == "--allworlds") {
                        if (!sender.hasPermission("auxilia.backup.fullbackup")) {
                            sender.sendColoredMessage("&cInsufficient permissions.")
                            return true
                        }
                        allWorlds = true
                    }
                }
                if (args.size > 2) {
                    if (args[2] == "--noconfirm")
                        noConfirm = true
                }

                if (allWorlds) {
                    if (sender !is Player || noConfirm) {
                        backupAllWorlds(sender)
                        return true
                    }
                    sender.sendColoredMessage("&aPlease confirm backuping all worlds using &l/auxilia confirm &a(60 seconds left)")
                    ConfirmationHandler.makeConfirmRequest(sender.uniqueId, object : ConfirmationAction {
                        override fun run() {
                            backupAllWorlds(sender)
                        }
                    })
                    return true
                }
                if (!sender.hasPermission("auxilia.backup.currentworld")) {
                    sender.sendColoredMessage("&cInsufficient permissions.")
                    return true
                }
                if (sender !is Player) {
                    sender.sendColoredMessage("&cYou need to be a player to do this!")
                    return true
                }
                if (noConfirm) {
                    backupWorld(sender)
                    return true
                }
                sender.sendColoredMessage("&aPlease confirm a backup using &l/auxilia confirm &a(60 seconds left)")
                ConfirmationHandler.makeConfirmRequest(sender.uniqueId, object : ConfirmationAction {
                    override fun run() {
                        backupWorld(sender)
                    }
                })
                return true
            }
            "confirm" -> {
                if (sender !is Player)
                    return true

                ConfirmationHandler.confirm(sender.uniqueId)
                return true
            }
            "status" -> {
                if (!sender.hasPermission("auxilia.status")) {
                    sender.sendColoredMessage("&cInsufficient permissions.")
                    return true
                }
                val current = AuxiliaManager.queueHandler.current
                if (current == null) {
                    sender.sendColoredMessage("&cNo backup currently in progress.")
                    return true
                }
                val elapsedTime = formatInterval(System.currentTimeMillis() - current.started)
                sender.sendColoredMessage(DELIMITER)
                sender.sendColoredMessage("&9Information about &7${current.name} &9(id: &7${current.id}&9)")
                sender.sendColoredMessage("&f* &9Time elapsed: &f$elapsedTime")
                sender.sendColoredMessage("&f* &9Status: ${current.currentAction.fancyText} &9(&f${current.currentAction.index}&9/&f5&9)")
                sender.sendColoredMessage("&f* &9World: &f${current.world.name}")
                sender.sendColoredMessage("&f* &9Executor: &f${current.player}")
                sender.sendColoredMessage("&f* &9Compression method: &f${FileCompressionManager.COMPRESSION_METHOD}&9, " +
                        "alghoritm: &f${FileCompressionManager.COMPRESSION_ALGHORITM}")
                sender.sendColoredMessage("&f* &9File name: &f${current.fileName}")
                return true
            }
            "pendings" -> {
                if (!sender.hasPermission("auxilia.pendings")) {
                    sender.sendColoredMessage("&cInsufficient permissions.")
                    return true
                }
                if (AuxiliaManager.queueHandler.queue.isEmpty()) {
                    sender.sendColoredMessage("&cNo pending backups.")
                    return true
                }
                sender.sendColoredMessage(DELIMITER)
                for ((index, queue) in AuxiliaManager.queueHandler.queue.withIndex()) {
                    sender.sendColoredMessage(" &f$index&9. &fBackup of &9${queue.world.name} &f(&9${queue.player}&f)")
                }
                return true
            }
            "cancel" -> {

            }
            "list" -> {
                if (!sender.hasPermission("auxilia.list")) {
                    sender.sendColoredMessage("&cInsufficient permissions.")
                    return true
                }
                Bukkit.getScheduler().runTaskAsynchronously(Auxilia.instance, Runnable {
                    var startIndex = 0
                    val pageSize = 10
                    if (args.size == 2) {
                        startIndex = (args[1].toInt()-1) * pageSize
                    }

                    val list = DriveManager.listFiles()
                    if (startIndex >= list.size) {
                        sender.sendColoredMessage("&cInvalid page (" + args[1] + ")")
                        return@Runnable
                    }
                    val sub = list.subList(startIndex, Math.min(list.size, startIndex + pageSize))

                    if (sub.isEmpty()) {
                        sender.sendColoredMessage("&cNo recent backups.")
                        return@Runnable
                    }

                    sender.sendColoredMessage("&9&l<==================[&f${args[1]}&9/&f$pageSize&9]==================>")
                    for ((i, backup) in sub.withIndex()) {
                        sender.sendColoredMessage("&f${i+1}&9. &f$backup")
                    }
                })
            }
        }
        return true
    }

    private fun backupAllWorlds(sender: CommandSender) {
        val worldsAmount = Bukkit.getWorlds().size
        if (sender is ConsoleCommandSender)
            AuxiliaManager.backupAll(CONSOLE_MODIFIER)
        else AuxiliaManager.backupAll(sender.name)

        sender.sendColoredMessage("&aBackup requests were added to the queue.")
        if (AuxiliaManager.queueHandler.size() > (worldsAmount + 1)) {
            sender.sendColoredMessage("&aAwaiting, ${AuxiliaManager.queueHandler.size()-(worldsAmount + 1)} backup(s) in progress left.")
        }
    }

    private fun backupWorld(sender: Player) {
        val world = sender.world

        AuxiliaManager.backup(world, sender.name)
        sender.sendColoredMessage("&aBackup request was added to the queue.")
        if (AuxiliaManager.queueHandler.size() > 1) {
            sender.sendColoredMessage("&aAwaiting, ${AuxiliaManager.queueHandler.size()-1} backup(s) in progress left.")
        }
    }
}