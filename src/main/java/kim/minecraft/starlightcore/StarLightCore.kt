package kim.minecraft.starlightcore

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.loader.PluginBase
import io.izzel.taboolib.metrics.BStats
import io.izzel.taboolib.module.config.TConfig
import kim.minecraft.starlightcore.features.BreakableBucket
import kim.minecraft.starlightcore.features.MultiPassengerVehicles
import kim.minecraft.starlightcore.features.PoisonousFood
import kim.minecraft.starlightcore.features.TreeThunder
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import java.io.File

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

    private fun isFeatureEnable(featureName: String): Boolean {
        return config.getBoolean("Features.$featureName.Enable", false).also {
            if (it) info("已启用特性 $featureName")
            else info("已禁用特性 $featureName")
        }
    }

    override fun onLoad() {
        // override onLoad()
    }

    override fun onEnable() {
        releaseResource()
        registerFeatures()
    }

    private fun releaseResource() {
        if (!File(plugin.dataFolder, "config.yml").exists()) {
            info("释放不存在的配置文件 config.yml")
            plugin.saveResource("config.yml", false)
        }
        if (!File(plugin.dataFolder, "message.yml").exists()) {
            info("释放不存在的配置文件 message.yml")
            plugin.saveResource("message.yml", false)
        }
        info("注册 bStats 监听")
        BStats(plugin)
    }

    private fun registerFeatures() {
        if (isFeatureEnable("BreakableBucket"))
            Bukkit.getPluginManager().registerEvents(BreakableBucket, plugin)
        if (isFeatureEnable("MultiPassengerVehicles"))
            Bukkit.getPluginManager().registerEvents(MultiPassengerVehicles, plugin)
        if (isFeatureEnable("TreeThunder"))
            TreeThunder.run()
        if (isFeatureEnable("PoisonousFood"))
            Bukkit.getPluginManager().registerEvents(PoisonousFood, plugin)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(plugin)
    }
}
