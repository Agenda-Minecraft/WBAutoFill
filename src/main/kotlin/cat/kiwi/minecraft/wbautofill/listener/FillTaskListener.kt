package cat.kiwi.minecraft.wbautofill.listener

import cat.kiwi.minecraft.wbautofill.Config
import cat.kiwi.minecraft.wbautofill.WBAutoFillPlugin
import com.wimbli.WorldBorder.WorldFillTask
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class FillTaskListener : Listener {
    companion object {
        var taskPool = listOf<Int>()
    }
    private val logger = Bukkit.getLogger()

    init {
        if (Config.isEnabled) {
            Config.enabledWorlds.map {
                startFill(it, Config.playerQuitDelay * 20)
            }.let { taskPool = it }
        }

    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (Bukkit.getOnlinePlayers().size <= 1) {
            taskPool.map {
                Bukkit.getServer().scheduler.cancelTask(it)
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        taskPool.forEach { taskID ->
            if (taskID != -1) {
                Bukkit.getServer().scheduler.cancelTask(taskID)
            }
            logger.info("Task $taskID cancelled.")
        }

    }

    private fun startFill(fillWorld: String, delayInTicks: Long) = kotlin.run {
        val ticks = 1
        val fillTask = WorldFillTask(
            Bukkit.getServer(),
            null,
            "world",
            Config.paddingDistance,
            Config.fillSpeed / 20,
            ticks,
            false
        )
        var fillTaskID = -1

        if (fillTask.valid()) {
            val task = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(
                WBAutoFillPlugin.plugin,
                fillTask,
                delayInTicks,
                ticks.toLong()
            )
            fillTaskID = task
            logger.info("WorldBorder map generation task for world $fillWorld started.")
        } else {
            logger.info("The world map generation task failed to start.")
        }
        fillTaskID
    }

}