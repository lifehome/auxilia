package info.toughlife.mcdev

import info.toughlife.mcdev.core.AuxiliaUnsafe
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Auxilia : JavaPlugin() {
    companion object {
        private lateinit var instance: Auxilia
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        logger.info("Loading resources...")
    }
}