package kim.minecraft.starlightcore.features

import kim.minecraft.starlightcore.StarLightCore.getConfig
import kim.minecraft.starlightcore.StarLightCore.getLocale
import kim.minecraft.starlightcore.utils.WorldInteraction.isUnderTree
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object BreakableBucket : Listener {

    private val chance = getConfig().getDouble("Features.BreakableBucket.Chance", 0.3)

    @EventHandler
    fun onFillBucket(e: PlayerBucketFillEvent) {
        if (Random.nextDouble() <= chance) {
            e.player.sendMessage(getLocale("Features.BreakableBucket.Oops"))
            if (e.blockClicked.type == Material.LAVA) {
                e.itemStack = ItemStack(Material.IRON_INGOT, 1)
                if (e.player.location.block.type.name.endsWith("AIR"))
                    e.player.location.block.type = Material.LAVA
            } else if (e.blockClicked.type == Material.WATER) {
                e.itemStack = ItemStack(Material.IRON_INGOT, 1)
                if (e.player.location.block.type.name.endsWith("AIR"))
                    e.player.location.block.type = Material.WATER
            }
        }
    }
}