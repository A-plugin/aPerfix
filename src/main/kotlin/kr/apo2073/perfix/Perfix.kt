package kr.apo2073.perfix

import kr.apo2073.perfix.cmds.PerfixBookCmds
import kr.apo2073.perfix.cmds.PerfixUIcmds
import kr.apo2073.perfix.events.onPlayer
import org.bukkit.plugin.java.JavaPlugin

class Perfix : JavaPlugin() {

    override fun onEnable() {
        if (instance!=null) return
        instance=this
        PerfixBookCmds().PerfixBookCmds(this)
        PerfixUIcmds().PerfixUIcmds()
        server.pluginManager.registerEvents(onPlayer(), this)
    }

    companion object {
        var instance:Perfix?=null
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
