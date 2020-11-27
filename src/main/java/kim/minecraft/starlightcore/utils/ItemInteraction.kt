package kim.minecraft.starlightcore.utils

import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import org.bukkit.inventory.ItemStack

object ItemInteraction {
    fun getNBTCompound(itemStack: ItemStack): NBTCompound {
        return NMS.handle().loadNBT(itemStack)
    }
}