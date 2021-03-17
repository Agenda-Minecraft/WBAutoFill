package cat.kiwi.minecraft.wbautofill.cmd

import cat.kiwi.minecraft.wbautofill.Config
import cat.kiwi.minecraft.wbautofill.WBAutoFillPlugin
import cat.kiwi.minecraft.wbautofill.listener.FillTaskListener
import cat.kiwi.minecraft.wbautofill.listener.startFill
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WBAFCmd : CommandExecutor {
    val helpInfo = infoPrefix("/wbaf enable - Enable auto fill.\n") +
            infoPrefix("/wbaf disable - Disable auto fill.\n") +
            infoPrefix("/wbaf padding <distance> - Set padding distance.\n") +
            infoPrefix("/wbaf add <world> - Add a world to WBAF task.\n") +
            infoPrefix("/wbaf del <world> - Remove a world from WBAF task.\n") +
            infoPrefix("/wbaf server <second> - Set task start delay after server started.\n") +
            infoPrefix("/wbaf quit <second> - Set task start delay after player left.\n") +
            infoPrefix("/wbaf speed <chunks> - Chunks fill per second.\n") +
            infoPrefix("/wbaf reload - Reload Config file.")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) =

        with(WBAutoFillPlugin.plugin) {
            if (args.isEmpty()) {
                println(helpInfo)
            }
            try {
                when (args[0]) {
                    "enable" -> Config.isEnabled = true.also {
                        if (Bukkit.getOnlinePlayers().isEmpty()) {
                            Config.enabledWorlds.map {
                                startFill(it, 0)
                            }.let { FillTaskListener.taskPool = it }
                        }
                        println(infoPrefix("enabled."))
                        println(infoPrefix("Enabled worlds detected: ${Config.enabledWorlds.joinToString(", ")}"))
                    }
                    "disable" -> Config.isEnabled = false.also {
                        FillTaskListener.taskPool.forEach {
                            Bukkit.getServer().scheduler.cancelTask(it)
                        }
                        println(infoPrefix("disabled."))
                    }
                    "padding" -> Config.paddingDistance = args[1].toInt().also {
                        println(infoPrefix("Padding distance set to $it."))
                    }
                    "add" -> Config.addWorld(args[1].also {
                        println(infoPrefix("World: $it added."))
                    })
                    "del" -> Config.delWorld(args[1].also {
                        println(infoPrefix("World: $it removed."))
                    })
                    "server" -> Config.serverStartDelay = args[1].toLong().also {
                        println(infoPrefix("Task will delay for $it seconds after server started."))
                    }
                    "quit" -> Config.playerQuitDelay = args[1].toLong().also {
                        println(infoPrefix("Task will delay for $it seconds after player left."))
                    }
                    "speed" -> Config.fillSpeed = args[1].toInt().also {
                        println(infoPrefix("Fill speed changed to $it chunks per second."))
                    }
                    "reload" -> Config.readConfig().also {
                        config.options().copyDefaults(true)
                        println(infoPrefix("Config file reloaded."))
                    }
                    else -> println(helpInfo)
                }
            } catch (e: Exception) {
                println(errorPrefix("Please check args."))
            }
            true
        }

    fun infoPrefix(string: String) =
        with(WBAutoFillPlugin.plugin) {
            "${ChatColor.AQUA} [WBAutoFill] ${ChatColor.GOLD}$string"
        }

    fun errorPrefix(string: String) =
        with(WBAutoFillPlugin.plugin) {
            "${ChatColor.RED} [WBAutoFill] ${ChatColor.GRAY}$string"
        }
}