package kim.minecraft.starlightcore.features

import kim.minecraft.starlightcore.StarLightCore.getConfig
import kim.minecraft.starlightcore.StarLightCore.getLocale
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object BreakableBucket : Listener {

    private val chance = getConfig().getDouble("Features.BreakableBucket.Chance", 0.3)

    @EventHandler
    fun onFillBucket(e: PlayerBucketFillEvent) {
        if (Random.nextDouble() <= chance) {
            e.player.sendMessage(getLocale("Features.BreakableBucket.Oops"))
            e.player.playSound(e.player.location, Sound.ENTITY_ITEM_BREAK, SoundCategory.VOICE, 1.0f, 1.0f)
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