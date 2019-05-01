package info.toughlife.mcdev.core.command

import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.commons.CONSOLE_MODIFIER
import info.toughlife.mcdev.commons.formatInterval
import info.toughlife.mcdev.commons.sendColoredMessage
import info.toughlife.mcdev.core.AuxiliaManager
import info.toughlife.mcdev.core.ConfirmationAction
import info.toughlife.mcdev.core.ConfirmationHandler
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.config.configInfo
import info.toughlife.mcdev.core.io.config.getConfigList
import info.toughlife.mcdev.core.io.config.message
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object AuxiliaCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String,
                           args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            for (str in getConfigList("commands.help")) {
                sender.sendColoredMessage(str)
            }
            return true
        }

        when (args[0]) {
            "backup" -> {
                if (AuxiliaManager.queueHandler.size() > configInfo().settings.maxQueuedJobs) {
                    sender.sendColoredMessage(message("commands.backup.maxQueues")
                        .replace("{LIMIT}", configInfo().settings.maxQueuedJobs.toString()))
                    return true
                }
                var now = false
                if (args.size > 1) {
                    if (args[1] == "now") {
                        now = true
                    }
                }

                var allWorlds = false
                var noConfirm = false
                var startId = 1
                if (now)
                    startId = 2

                if (args.size > startId) {
                    if (args[startId] == "--allworlds") {
                        if (!sender.hasPermission("auxilia.backup.fullbackup")) {
                            sender.sendColoredMessage(message("other.noPermission"))
                            return true
                        }
                        allWorlds = true
                    }
                }
                if (args.size > startId + 1) {
                    if (args[startId + 1] == "--noconfirm")
                        noConfirm = true
                }

                if (allWorlds) {
                    if (sender !is Player || noConfirm) {
                        backupAllWorlds(sender, now)
                        return true
                    }
                    sender.sendColoredMessage(message("commands.backup.pleaseConfirm"))
                    ConfirmationHandler.makeConfirmRequest(sender.uniqueId, object : ConfirmationAction {
                        override fun run() {
                            backupAllWorlds(sender, now)
                        }
                    })
                    return true
                }
                if (!sender.hasPermission("auxilia.backup.currentworld")) {
                     sender.sendColoredMessage(message("other.noPermission"))
                    return true
                }
                if (sender !is Player) {
                    sender.sendColoredMessage(message("other.mustBePlayer"))
                    return true
                }
                if (noConfirm) {
                    backupWorld(sender, now)
                    return true
                }
                sender.sendColoredMessage(message("commands.backup.pleaseConfirm"))
                ConfirmationHandler.makeConfirmRequest(sender.uniqueId, object : ConfirmationAction {
                    override fun run() {
                        backupWorld(sender, now)
                    }
                })
                return true
            }
            "confirm" -> {
                if (sender !is Player)
                    return true

                if (ConfirmationHandler.isConfirmationValid(sender.uniqueId)) {
                    sender.sendColoredMessage(message("commands.confirm.nothingToConfirm"))
                    return true
                }

                ConfirmationHandler.confirm(sender.uniqueId)
                return true
            }
            "status" -> {
                if (!sender.hasPermission("auxilia.status")) {
                     sender.sendColoredMessage(message("other.noPermission"))
                    return true
                }
                val current = AuxiliaManager.queueHandler.current
                if (current == null) {
                    sender.sendColoredMessage(message("commands.status.noBackups"))
                    return true
                }
                val elapsedTime = formatInterval(System.currentTimeMillis() - current.started)
                for (str in getConfigList("commands.status.info")) {
                    sender.sendColoredMessage(str
                        .replace("{NAME}", current.name)
                        .replace("{ID}", current.id.toString())
                        .replace("{ELAPSED}", elapsedTime)
                        .replace("{STATUS}", current.currentAction.fancyText)
                        .replace("{STATUS_INDEX}", current.currentAction.index.toString())
                        .replace("{WORLD}", current.world.name)
                        .replace("{PLAYER}", current.player)
                        .replace("{COMPRESSION_METHOD}", FileCompressionManager.COMPRESSION_METHOD)
                        .replace("{COMPRESSION_ALGHORITM}", FileCompressionManager.COMPRESSION_ALGHORITM.name)
                        .replace("{FILE}", current.fileName))
                }
                return true
            }
            "pendings" -> {
                if (!sender.hasPermission("auxilia.pendings")) {
                     sender.sendColoredMessage(message("other.noPermission"))
                    return true
                }
                if (AuxiliaManager.queueHandler.queue.isEmpty()) {
                    sender.sendColoredMessage(message("commands.pendings.noBackups"))
                    return true
                }
                sender.sendColoredMessage(message("commands.pendings.header"))
                for ((index, queue) in AuxiliaManager.queueHandler.queue.withIndex()) {
                    sender.sendColoredMessage(message("commands.pendings.indexStyle")
                        .replace("{INDEX}", index.toString())
                        .replace("{WORLD}", queue.world.name)
                        .replace("{PLAYER}", queue.player))
                }
                return true
            }
            "cancel" -> {
                var all = false
                if (args.size > 1) {
                    if (args[1] == "--all") {
                        all = true
                    }
                }
                if (!all) {
                    if (!sender.hasPermission("auxilia.backup.cancel")) {
                         sender.sendColoredMessage(message("other.noPermission"))
                        return true
                    }
                    if (AuxiliaManager.queueHandler.current == null) {
                        sender.sendColoredMessage(message("commands.cancel.noTasks"))
                        return true
                    }
                    val current = AuxiliaManager.queueHandler.current

                    current!!.interrupt()

                    sender.sendColoredMessage(message("commands.cancel.success"))
                }
                else {
                    if (!sender.hasPermission("auxilia.backup.cancel.all")) {
                         sender.sendColoredMessage(message("other.noPermission"))
                        return true
                    }

                    AuxiliaManager.queueHandler.cancelAll()
                    sender.sendColoredMessage(message("commands.cancel.successAll"))
                }

            }
            "list" -> {
                if (!sender.hasPermission("auxilia.list")) {
                     sender.sendColoredMessage(message("other.noPermission"))
                    return true
                }
                Bukkit.getScheduler().runTaskAsynchronously(Auxilia.instance, Runnable {
                    val list = Auxilia.driveOptions.listFiles()

                    var startIndex = 0
                    var pageNumber = 1
                    val pageSize = 10
                    val pages = if ((list.size / pageSize) == 0) 1 else list.size / pageSize

                    if (args.size == 2) {
                        pageNumber = args[1].toInt()
                        startIndex = (pageNumber-1) * pageSize
                    }

                    if (startIndex >= list.size) {
                        sender.sendColoredMessage(message("commands.list.invalidPage")
                            .replace("{PAGE}", pageNumber.toString()))
                        return@Runnable
                    }
                    val sub = list.subList(startIndex, Math.min(list.size, startIndex + pageSize))

                    if (sub.isEmpty()) {
                        sender.sendColoredMessage(message("commands.list.noBackups"))
                        return@Runnable
                    }

                    sender.sendColoredMessage(message("commands.list.header")
                        .replace("{PAGE}", pageNumber.toString())
                        .replace("{PAGES}", pages.toString()))
                    for ((i, backup) in sub.withIndex()) {
                        sender.sendColoredMessage(message("commands.list.indexStyle")
                            .replace("{INDEX}", (i+1).toString())
                            .replace("{BACKUP}", backup))
                    }
                })
            }
        }
        return true
    }

    private fun backupAllWorlds(sender: CommandSender, now: Boolean) {
        val worldsAmount = Bukkit.getWorlds().size
        if (sender is ConsoleCommandSender)
            AuxiliaManager.backupAll(CONSOLE_MODIFIER, now)
        else AuxiliaManager.backupAll(sender.name, now)

        sender.sendColoredMessage(message("commands.backup.allWorldsSuccess"))
            if (AuxiliaManager.queueHandler.size() > (worldsAmount + 1)) {
            sender.sendColoredMessage(message("commands.backup.pendingInfo")
                .replace("{PENDING}", (AuxiliaManager.queueHandler.size()-(worldsAmount + 1)).toString()))
        }
    }

    private fun backupWorld(sender: Player, now: Boolean) {
        val world = sender.world

        AuxiliaManager.backup(world, sender.name, now)
        sender.sendColoredMessage(message("commands.backup.worldSuccess"))
        if (AuxiliaManager.queueHandler.size() > 1) {
            sender.sendColoredMessage(message("commands.backup.pendingInfo")
                .replace("{PENDING}", (AuxiliaManager.queueHandler.size()-1).toString()))
        }
    }
}