package kim.minecraft.starlightcore

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.loader.PluginBase
import io.izzel.taboolib.module.config.TConfig
import kim.minecraft.starlightcore.features.BreakableBucket
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

object StarLightCore : Plugin() {

    private val config: TConfig by lazy {
        TConfig.create(plugin, "config.yml")
    }

    private val message: TConfig by lazy {
        TConfig.create(plugin, "message.yml")
    }

    fun Any.getPlugin(): PluginBase = plugin

    fun Any.getConfig(): TConfig = config

    fun Any.getLocale(key: String): String = message.getStringColored(key, "lost locale for $key in message.yml")

    fun Any.getLocaleList(key: String): List<String> = message.getStringListColored(key)

    fun Any.info(msg: String) = plugin.logger.info(msg)

    fun Any.getAdminPermission(): String = config.getString("Global.AdminPermission", "starlightcore.admin")!!

    private fun isFeatureEnable(featureName: String) = config.getBoolean("Features.$featureName.Enable", false)

    override fun onLoad() {
        // override onLoad()
    }

    override fun onEnable() {
        releaseResource()
        registerFeatures()
    }

    private fun releaseResource() {
        if (plugin.getResource("config.yml") == null) {
            info("释放不存在的配置文件 config.yml")
            plugin.saveResource("config.yml", false)
        }
        if (plugin.getResource("message.yml") == null) {
            info("释放不存在的配置文件 message.yml")
            plugin.saveResource("message.yml", false)
        }
    }

    private fun registerFeatures() {
        if (isFeatureEnable("BreakableBucket"))
            Bukkit.getPluginManager().registerEvents(BreakableBucket, plugin)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(plugin)
    }
}
