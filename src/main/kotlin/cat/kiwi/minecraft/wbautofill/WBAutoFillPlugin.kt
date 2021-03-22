package cat.kiwi.minecraft.wbautofill

import cat.kiwi.minecraft.wbautofill.cmd.WBAFCmd
import cat.kiwi.minecraft.wbautofill.listener.FillTaskListener
import org.bukkit.plugin.java.JavaPlugin

class WBAutoFillPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: WBAutoFillPlugin
    }

    override fun onEnable() = kotlin.run {
        logger.info("WBAutoFill is enabled!")
        instance = this
        try {
            Config.readConfig()
        } catch (e: Exception) {
            logger.info("Cannot read configuration file!")
        }
        getCommand("wbaf")!!.setExecutor(WBAFCmd())
        getCommand("worldborderautofill")!!.setExecutor(WBAFCmd())

        saveDefaultConfig()
        server.pluginManager.registerEvents(FillTaskListener(), this)
    }

    override fun onDisable() = kotlin.run {
        logger.info("WBAutoFill is disabled!")
    }
}