package kim.minecraft.starlightcore.utils

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity

object WorldInteraction {

    fun LivingEntity.isUnderTree(): Boolean {
        val maxDistanceBetweenLogAndLeaves = 5
        val treeLeavesNumberAtLeast = 6
        val treeLogUpTo = 1
        val treeLogDownTo = 3

        val highestBlock = world.getHighestBlockAt(location)

        if (highestBlock.y <= location.y) return false
        if (!highestBlock.type.name.endsWith("LEAVES")) return false

        val blockList = highestBlock.location.get2DBlocksAround(maxDistanceBetweenLogAndLeaves)
        if (blockList.filter { it.type.name.endsWith("LOG") }.count() < treeLeavesNumberAtLeast) return false

        val log = blockList.find { it.type.name.endsWith("LOG") }!!
        if (!log.location.getYBlocks(treeLogUpTo, treeLogDownTo).all { it.type == log.type }) return false

        return true
    }


    fun Location.get2DBlocksAround(radius: Int): List<Block> {
        val blockList = mutableListOf<Block>()
        val max = add(radius.toDouble(), 0.0, radius.toDouble())
        val min = subtract(radius.toDouble(), 0.0, radius.toDouble())
        for (loop_x in min.x.toInt()..max.x.toInt()) {
            for (loop_z in min.z.toInt()..max.z.toInt()) {
                blockList.add(Location(world, loop_x.toDouble(), y, loop_z.toDouble()).block)
            }
        }
        return blockList
    }

    fun Location.getYBlocks(up: Int, down: Int): List<Block> {
        val blockList = mutableListOf<Block>()
        for (loop_y in (-down)..up) {
            blockList.add(clone().add(0.0, loop_y.toDouble(), 0.0).block)
        }
        return blockList
    }
}