package kr.apo2073.perfix.util

import net.kyori.adventure.text.Component
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

class setPerfix(player: Player, val per:String) {
    init {
        val nmsP=player as CraftPlayer
        nmsP.setDisplayName("$per${nmsP.name}")
        nmsP.setPlayerListName("$per${nmsP.name}")
        nmsP.customName(Component.text("$per${nmsP.name}"))
        nmsP.isCustomNameVisible=true
        nmsP.displayName(Component.text("$per${nmsP.name}"))
        nmsP.playerListName(Component.text("$per${nmsP.name}"))
    }
}