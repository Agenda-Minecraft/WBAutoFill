package cat.kiwi.minecraft.wbautofill


object Config {
    var paddingDistance = -1
        set(value) = with(WBAutoFillPlugin.instance) {
            config.set("padding-distance", value)
            field = value
            saveConfig()
        }
    var enabledWorlds = linkedSetOf<String>()
    fun addWorld(string: String) = with(WBAutoFillPlugin.instance) {
        enabledWorlds.add(string)
        config.set("enabled-worlds", enabledWorlds.toList())
        saveConfig()
    }

    fun delWorld(string: String) = with(WBAutoFillPlugin.instance) {
        enabledWorlds.remove(string)
        config.set("enabled-worlds", enabledWorlds.toList())
        saveConfig()
    }

    var serverStartDelay = 40L
        set(value) = with(WBAutoFillPlugin.instance) {
            config.set("server-start-delay", value)
            field = value
            saveConfig()
        }
    var playerQuitDelay = 20L
        set(value) = with(WBAutoFillPlugin.instance) {
            config.set("player-quit-delay", value)
            field = value
            saveConfig()
        }
    var fillSpeed = 200
        set(value) = with(WBAutoFillPlugin.instance) {
            config.set("fill-speed", value)
            field = value
            saveConfig()
        }
    var isEnabled = true
        set(value) = with(WBAutoFillPlugin.instance) {
            config.set("enable-auto-fill", value)
            field = value
            saveConfig()
        }

    fun readConfig() =
        with(WBAutoFillPlugin.instance.config) {
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
