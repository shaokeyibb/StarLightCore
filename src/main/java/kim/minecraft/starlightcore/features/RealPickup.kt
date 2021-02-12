package kim.minecraft.starlightcore.features

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object RealPickup : Listener {
    @EventHandler
    fun onPickupItem(e: EntityPickupItemEvent) {
        if (e.entityType != EntityType.PLAYER) return
        if (!(e.entity as Player).isSneaking) e.isCancelled = true
    }
}