package cat.kiwi.minecraft.wbautofill

import java.lang.ClassCastException

object Config {
    var paddingDistance = -1
        set(value) {
            WBAutoFillPlugin.plugin.config.set("padding-distance", value)
            field = value
        }
    var enabledWorlds = linkedSetOf<String>()
    fun addWorld(string: String) =
        with(WBAutoFillPlugin.plugin) {
            enabledWorlds.add(string)
            config.set("enabled-worlds", enabledWorlds.toList())
        }

    fun delWorld(string: String) =
        with(WBAutoFillPlugin.plugin) {
            enabledWorlds.remove(string)
            config.set("enabled-worlds", enabledWorlds.toList())
        }

    var serverStartDelay = 40L
        set(value) {
            WBAutoFillPlugin.plugin.config.set("server-start-delay", value)
            field = value
        }
    var playerQuitDelay = 20L
        set(value) {
            WBAutoFillPlugin.plugin.config.set("player-quit-delay", value)
            field = value
        }
    var fillSpeed = 200
        set(value) {
            WBAutoFillPlugin.plugin.config.set("fill-speed", value)
            field = value
        }
    var isEnabled = true
        set(value) {
            WBAutoFillPlugin.plugin.config.set("enable-auto-fill", value)
            field = value
        }

    fun readConfig() =
        with(WBAutoFillPlugin.plugin.config) {
            options().copyDefaults(true)
            paddingDistance = getInt("padding-distance")

            enabledWorlds = try {
                getStringList("enabled-worlds").toSet() as LinkedHashSet<String>
            } catch (e: ClassCastException) {
                linkedSetOf<String>(getString("enabled-worlds")!!.split("[")[1].split("]")[0])
            }

            serverStartDelay = getLong("server-start-delay")
            playerQuitDelay = getLong("player-quit-delay")
            fillSpeed = getInt("fill-speed")
            isEnabled = getBoolean("enable-auto-fill")
        }
}