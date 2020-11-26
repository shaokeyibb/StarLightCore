package kim.minecraft.starlightcore.utils

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity

object WorldInteraction {

    fun LivingEntity.isUnderTree(): Boolean {
        val maxDistanceBetweenLogAndLeaves = 5
        val treeLeavesNumberAtLeast = 6
        val treeLogUpTo = 1
        val treeLogDownTo = 1

        val highestBlock = world.getHighestBlockAt(location)

        if (highestBlock.y <= location.y) return false
        if (!highestBlock.type.name.endsWith("LEAVES")) return false

        val blockList = highestBlock.location.get3DBlocksAround(maxDistanceBetweenLogAndLeaves)
        if (!blockList.stream().anyMatch { it.type.name.endsWith("LOG") }) return false
        if (blockList.filter { it.type.name.endsWith("LEAVES") }.count() < treeLeavesNumberAtLeast) return false

        val log = blockList.find { it.type.name.endsWith("LOG") }!!
        if (!log.location.getYBlocks(treeLogUpTo, treeLogDownTo).all { it.type.name.endsWith("LEAVES") || it.type == log.type }) return false

        return true
    }


    fun Location.get3DBlocksAround(radius: Int): List<Block> {
        val blockList = mutableListOf<Block>()
        val max = clone().add(radius.toDouble(), radius.toDouble(), radius.toDouble())
        val min = clone().subtract(radius.toDouble(), radius.toDouble(), radius.toDouble())
        for (loop_x in min.x.toInt()..max.x.toInt()) {
            for (loop_y in min.y.toInt()..max.y.toInt()) {
                for (loop_z in min.z.toInt()..max.z.toInt()) {
                    val block = Location(world, loop_x.toDouble(), loop_y.toDouble(), loop_z.toDouble()).block
                    if (!block.type.name.endsWith("AIR"))
                        blockList.add(block)
                }
            }
        }
        return blockList
    }

    fun Location.getYBlocks(up: Int, down: Int): List<Block> {
        val blockList = mutableListOf<Block>()
        for (loop_y in (-down)..up) {
            val block = clone().add(0.0, loop_y.toDouble(), 0.0).block
            if (!block.type.name.endsWith("AIR"))
                blockList.add(block)
        }
        return blockList
    }
}