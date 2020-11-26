package kim.minecraft.starlightcore.features

import kim.minecraft.starlightcore.StarLightCore.getConfig
import kim.minecraft.starlightcore.StarLightCore.getLocale
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object BreakableBucket : Listener {

    private val chance = getConfig().getDouble("Features.BreakableBucket.Chance")

    @EventHandler
    fun onUseBucket(e: PlayerBucketEvent) {
        if (Random.nextDouble() <= chance) {
            e.player.sendMessage(getLocale("Features.BreakableBucket.Oops"))
            when (e.bucket) {
                Material.BUCKET -> {
                    e.itemStack = ItemStack(Material.IRON_INGOT, 1)
                    e.isCancelled = true
                }
                Material.LAVA_BUCKET -> {
                    e.itemStack = ItemStack(Material.IRON_INGOT, 1)
                    e.player.location.block.type = Material.LAVA
                    e.isCancelled = true
                }
                else -> {
                    e.itemStack = ItemStack(Material.IRON_INGOT, 1)
                    e.player.location.block.type = Material.WATER
                    e.isCancelled = true
                }
            }
        }
    }
}