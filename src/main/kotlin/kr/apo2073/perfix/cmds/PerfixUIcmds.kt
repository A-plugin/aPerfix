package kr.apo2073.perfix.cmds

import kr.apo2073.perfix.Perfix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

class PerfixUIcmds:CommandExecutor {
    val perfix= Perfix.instance!!
    fun PerfixUIcmds() {
        perfix.getCommand("칭호")?.setExecutor(this::onCommand)
    }
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return false
        p0.openPerfixUI()
        return true
    }

    fun Player.openPerfixUI() {
        val inv= Bukkit.createInventory(null, 9*4,"§f칭호")

        val file= File("${perfix.dataFolder}/perfix", "${this.uniqueId}.yml")
        val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        val section = config.getConfigurationSection("perfix")
        if (section != null) {
            for (slot in section.getKeys(false)) {
                val itemStack = section.getItemStack(slot)
                if (itemStack != null) {
                    inv.setItem(slot.toInt(), itemStack)
                }
            }
        }

        this.openInventory(inv)
    }
}