package kr.apo2073.perfix.events

import kr.apo2073.aLib.Inventorys.add
import kr.apo2073.perfix.Perfix
import kr.apo2073.perfix.util.setPerfix
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.IOException

class onPlayer:Listener {
    val perfix=Perfix.instance!!

    @EventHandler
    fun onUse(e:PlayerInteractEvent) {
        val player=e.player
        val item=e.item?: return
        val action=e.action
        if (item.itemMeta.hasDisplayName() && item.itemMeta.displayName.contains("§r§f[ §a칭호 §f]")) {
            save(player, item)
            player.sendMessage(Component.text("§l[§a*§f] §r칭호 ${getPerfix(item)}(이)가 추가되었습니다"))
            player.playSound(player.location, Sound.BLOCK_END_PORTAL_FRAME_FILL,1.0F, 1.0F)
            player.inventory.setItemInMainHand(null)
        }

    }

    @EventHandler
    fun onClickPerfixUI(e:InventoryClickEvent) {
        val ci=e.clickedInventory ?: return
        val citem=e.currentItem ?: return
        val view=e.view
        val i=e.inventory
        val player =e.whoClicked as Player
        if (ci.type.equals(InventoryType.PLAYER)) return
        if (!view.title.contains("§f칭호")) return
        if (citem.itemMeta.hasDisplayName() && citem.itemMeta.displayName.contains("§r§f[ §a칭호 §f]")) {
            e.isCancelled=true
            if (e.click.isKeyboardClick) {
                if (citem.itemMeta.hasEnchants()) {
                    val cmeta=citem.itemMeta
                    for (enc in cmeta.enchants.keys) {
                        cmeta.removeEnchant(enc)
                        setPerfix(e.whoClicked as Player,"")
                    }
                    citem.itemMeta=cmeta
                }
                if (player.inventory.isFull()) {
                    player.world.dropItem(player.location, citem)
                } else {
                    player.inventory add citem
                }
                ci.setItem(e.slot, null)
                e.whoClicked.sendMessage(Component.text("§l[§c*§f] 칭호를 제거했습니다"))
                player.playSound(player.location, Sound.BLOCK_ANVIL_HIT,1.0F, 1.0F)
            } else {
                if (citem.itemMeta.hasEnchants()) {
                    val cmeta=citem.itemMeta
                    for (enc in cmeta.enchants.keys) {
                        cmeta.removeEnchant(enc)
                        setPerfix(e.whoClicked as Player,"")
                        e.whoClicked.sendMessage(Component.text("§l[§a*§f] 칭호를 해제했습니다."))
                        player.playSound(player.location, Sound.BLOCK_BEACON_DEACTIVATE,1.0F, 1.0F)
                    }
                    citem.itemMeta=cmeta
                } else {
                    for (slot in ci) {
                        if (slot==null) continue
                        if (slot.itemMeta.hasEnchants()) {
                            val sm=slot.itemMeta
                            for (enc in sm.enchants.keys) {
                                sm.removeEnchant(enc)
                            }
                            slot.itemMeta=sm
                        }
                    }
                    val cmeta=citem.itemMeta
                    cmeta.addEnchant(Enchantment.LOYALTY ,1,true)
                    cmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    citem.itemMeta=cmeta

                    setPerfix(e.whoClicked as Player, getPerfix(citem))
                    player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE,1.0F, 1.0F)
                    e.whoClicked.sendMessage(Component.text("§l[§a*§f] 칭호가 ${getPerfix(citem)}(으)로 설정되었습니다."))
                }
            }
            try {
                val inv= e.inventory
                val file=File("${this.perfix.dataFolder}/perfix", "${player.uniqueId}.yml")
                val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
                val section = config.getConfigurationSection("perfix")

                val content=inv.contents
                for (slot in content.indices) {
                    if (content[slot]==null) config.set("perfix.$slot", null)
                    else config.set("perfix.$slot", content[slot])
                }
                config.save(file)
            } catch (er: IOException) {
                er.printStackTrace()
            }
        }
    }

    fun getPerfix(item: ItemStack):String {
        val meta=item.itemMeta
        val lore=meta.lore
        return lore?.first().toString()+"§f"
    }

    fun save(player:Player, perfix:ItemStack) {
        val inv= Bukkit.createInventory(null, 9*4,"§f칭호")
        val file=File("${this.perfix.dataFolder}/perfix", "${player.uniqueId}.yml")
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
        if (!inv.isFull()) {
            inv.addItem(perfix)
        } else {
            player.sendMessage(Component.text("§l[§c*§f] §r더 이상 칭호를 추가할 수 없습니다!"))
            player.inventory.addItem(perfix)
            return
        }


        val content=inv.contents
        for (slot in content.indices) {
            if (content[slot]==null) config.set("perfix.$slot", null)
            else config.set("perfix.$slot", content[slot])
        }

        try {
            config.save(file)
        } catch (er: IOException) {
            er.printStackTrace()
        }
    }
}

fun Inventory.isFull():Boolean {
    val inv: Inventory = this
    for (slot in inv.contents) {
        if (slot == null) return false
        if (slot.type.isAir) return false
        if (slot.amount < slot.maxStackSize) return false
    }
    return true
}
