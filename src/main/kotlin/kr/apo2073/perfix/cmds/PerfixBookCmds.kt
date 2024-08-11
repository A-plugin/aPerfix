package kr.apo2073.perfix.cmds

import kr.apo2073.aLib.Items.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class PerfixBookCmds:TabExecutor {
    fun PerfixBookCmds(plugin: JavaPlugin) {
        plugin.getCommand("perfix")?.setExecutor(this::onCommand)
        plugin.getCommand("perfix")?.setTabCompleter(this::onTabComplete)
    }
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return false
        val player=p0
        if (!player.hasPermission("perfix.make")) {
            player.sendMessage(Component.text("§l§c[*] §r해당 명령어에대한 권한이 없습니다!"))
            return true
        }
        if (p3.isNullOrEmpty()) {
            player.sendMessage(Component.text("§l§c[*] §r/칭호 [칭호 이름(&로 컬러코드)]"))
            return true
        }
        val perfix=p3[0].replace("&", "§")
        val item=ItemBuilder(Material.KNOWLEDGE_BOOK)
            .setDisplayName("§r§f[ §a칭호 §f]")
            .setLore(listOf("§f$perfix"))
            .build()
        player.inventory.addItem(item)
        player.sendMessage(Component.text("§l§a[*] §r새로운 칭호북을 만들었습니다."))
        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String> {
        val tab= mutableListOf<String>()
        for (op in Bukkit.getOnlinePlayers()) {
            tab.add(op.name)
        }
        return tab
    }
}