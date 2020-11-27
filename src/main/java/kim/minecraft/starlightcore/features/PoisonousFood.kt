package kim.minecraft.starlightcore.features

import io.izzel.taboolib.module.nms.nbt.NBTBase
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import kim.minecraft.starlightcore.StarLightCore
import kim.minecraft.starlightcore.StarLightCore.getConfig
import kim.minecraft.starlightcore.StarLightCore.getLocale
import kim.minecraft.starlightcore.utils.ItemInteraction
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import java.util.*
import kotlin.random.Random

object PoisonousFood : Listener {

    private val minDeathTime = getConfig().getLong("Features.PoisonousFood.MinDeathTime", 600)
    private val maxDeathTime = getConfig().getLong("Features.PoisonousFood.MaxDeathTime", 24000)
    private val intervalTicks: Long = getConfig().getLong("Features.PoisonousFood.IntervalTicks", 100)

    init {
        Material.values().filter { it.isEdible }.forEach { material ->
            ShapelessRecipe(NamespacedKey(StarLightCore.plugin, "PoisonousFood_${material.name}"),
                    ItemStack(material).apply {
                        this.addPoison()
                    })
                    .addIngredient(material)
                    .addIngredient(Material.SPIDER_EYE)
                    .also { shapelessRecipe -> Bukkit.addRecipe(shapelessRecipe) }
        }
        Bukkit.getScheduler().runTaskTimer(StarLightCore.plugin, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                checkDeath(it)
            }
        }, 20, intervalTicks)
    }

    private fun ItemStack.addPoison() {
        ItemInteraction.getNBTCompound(this).also {
            if (it.getDeep("StarLightCore.PoisonousFood") == null)
                it.putDeep("StarLightCore.PoisonousFood", NBTCompound())
            it.saveTo(this)
        }
    }

    @EventHandler
    fun onRecipe(e: CraftItemEvent) {
        ItemInteraction.getNBTCompound(e.currentItem!!).also {
            if (it.getDeep("StarLightCore.PoisonousFood.Killer") != null) return
            if (it.getDeep("StarLightCore.PoisonousFood") != null)
                it.putDeep("StarLightCore.PoisonousFood", NBTCompound().apply {
                    this["TimeFor"] = NBTBase(Random.nextLong(minDeathTime, maxDeathTime))
                    this["Killer"] = NBTBase(e.whoClicked.uniqueId.toString())
                })
            it.saveTo(e.currentItem!!)
        }
    }

    @EventHandler
    fun onEat(e: PlayerItemConsumeEvent) {
        if (!e.item.type.isEdible) return
        val nbt = ItemInteraction.getNBTCompound(e.item)
        if (nbt.getDeep("StarLightCore.PoisonousFood.TimeFor") == null) return
        if (e.player.scoreboardTags.any { it.startsWith("StarLightCore.PoisonousFood.TimeOn") }) return
        e.player.addScoreboardTag("StarLightCore.PoisonousFood.TimeOn:${Bukkit.getWorlds()[0].fullTime + nbt.getDeep("StarLightCore.PoisonousFood.TimeFor").asLong()}")
        e.player.addScoreboardTag("StarLightCore.PoisonousFood.Killer:${nbt.getDeep("StarLightCore.PoisonousFood.Killer").asString()}")
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        checkDeath(e.player)
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        if (!e.entity.scoreboardTags.stream().anyMatch { it.startsWith("StarLightCore.PoisonousFood.TimeOn") }) return
        e.deathMessage = getLocale("Features.PoisonousFood.DeathMessage").replace("{0}", e.entity.displayName).replace("{1}", Bukkit.getOfflinePlayer(UUID.fromString(e.entity.scoreboardTags.find { it.startsWith("StarLightCore.PoisonousFood.Killer") }!!.split(':')[1])).name!!)
        e.entity.removeScoreboardTag(e.entity.scoreboardTags.find { it.startsWith("StarLightCore.PoisonousFood.TimeOn") }!!)
        e.entity.removeScoreboardTag(e.entity.scoreboardTags.find { it.startsWith("StarLightCore.PoisonousFood.Killer") }!!)
    }

    private fun checkDeath(player: Player) {
        if (!player.scoreboardTags.stream().anyMatch { it.startsWith("StarLightCore.PoisonousFood.TimeOn") }) return
        if (player.scoreboardTags.find { it.startsWith("StarLightCore.PoisonousFood.TimeOn") }!!
                        .split(':')[1]
                        .toLong() <= Bukkit.getWorlds()[0].fullTime) player.health = 0.0
    }

}