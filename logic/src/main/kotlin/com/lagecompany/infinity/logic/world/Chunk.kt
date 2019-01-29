package com.lagecompany.infinity.world

import com.lagecompany.infinity.logic.utils.Disposable
import com.lagecompany.infinity.world.buffer.VoxelNeighborTypeBuffer
import com.lagecompany.infinity.world.buffer.VoxelSideBuffer
import com.lagecompany.infinity.world.buffer.VoxelTypeBuffer

class Chunk(val index: Int) : Disposable {

    companion object {
        const val SIZE = 16
        const val BUFFER_SIZE = SIZE * SIZE * SIZE
        const val SIZE_SHIFT_Y = 4
        const val SIZE_SHIFT_X = 8
    }

    val types = VoxelTypeBuffer()
    val visibleSides = VoxelSideBuffer()
    val neighborSides = VoxelNeighborTypeBuffer()

    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    val isEmpty get() = types.isEmpty

    override fun dispose() {
        types.free()
        visibleSides.free()
        neighborSides.free()
    }

    fun isOnBounds(x: Int, y: Int, z: Int): Boolean {
        return x in 0 until Chunk.SIZE && y in 0 until Chunk.SIZE && z in 0 until Chunk.SIZE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chunk

        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        return index
    }

    override fun toString(): String {
        return "Chunk($x,$y,$z)"
    }


}