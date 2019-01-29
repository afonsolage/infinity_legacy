package com.lagecompany.infinity.world

import com.lagecompany.infinity.logic.utils.Disposable
import com.lagecompany.infinity.math.Vector3I
import com.lagecompany.infinity.utils.Side
import com.lagecompany.infinity.world.buffer.VoxTypeRef

class World : Disposable {
    private val chunks = Array(SIZE) { Chunk(it) }

    companion object {
        const val WIDTH = 10
        const val HEIGHT = 5
        const val DEPTH = 10

        const val SIZE = WIDTH * HEIGHT * DEPTH
        const val X_SIZE = WIDTH
        const val Y_SIZE = HEIGHT
        const val Z_SIZE = DEPTH

        const val Z_UNIT = 1
        const val Y_UNIT = Z_SIZE * Z_UNIT
        const val X_UNIT = Y_SIZE * Y_UNIT

        fun toIndex(vec: Vector3I): Int {
            return toIndex(vec.x, vec.y, vec.z)
        }

        fun toIndex(x: Int, y: Int, z: Int): Int {
            return x * X_UNIT + y * Y_UNIT + z * Z_UNIT
        }

        fun fromIndex(index: Int): Vector3I {
            return Vector3I(index / X_UNIT, (index % X_UNIT) / Y_UNIT, index % Y_UNIT)
        }
    }

    fun generateAllChunks() {
        chunks.forEach {
            generateChunk(it)
        }
        chunks.forEach {
            buildChunkNeighborhood(it)
        }
        chunks.forEach {
            checkVisibility(it)
        }
    }

    internal fun checkVisibility(chunk: Chunk) {
        if (chunk.isEmpty)
            return

        chunk.visibleSides.alloc()

        for (x in 0 until Chunk.SIZE) {
            for (y in 0 until Chunk.SIZE) {
                for (z in 0 until Chunk.SIZE) {
                    if (chunk.types[x, y, z].get() != VoxelType.NONE) {
                        val visibleRef = chunk.visibleSides[x, y, z]
                        val neighborhoodRef = chunk.neighborSides[x, y, z]

                        for (side in Side.allSides) {
                            visibleRef[side] = !VoxelType.isVisible(neighborhoodRef[side].get()?.get())
                        }

                        visibleRef.save()
                    }
                }
            }
        }
    }

    internal fun generateChunk(chunk: Chunk) {
        setChunkPosition(chunk)

        var hasVoxel = false

        val generator = NoiseGenerator.default
        generator.generate(chunk)

        chunk.types.alloc()

        for (i in 0 until Chunk.SIZE * Chunk.SIZE) {
            var height = (generator[i] * Chunk.SIZE) + (Chunk.SIZE / 2)

            val originalHeight = height.toInt()
            if (height >= chunk.y + Chunk.SIZE)
                height = (chunk.y + Chunk.SIZE).toFloat()

            if (!hasVoxel)
                hasVoxel = true

            val (x, z) = NoiseGenerator.fromIndex(i)
            for (y in chunk.y until height.toInt()) {
                val type = when {
                    y < originalHeight / 2 -> VoxelType.ROCK
                    y == originalHeight - 1 -> VoxelType.GRASS
                    else -> VoxelType.DIRT
                }
                chunk.types[x, y % Chunk.SIZE, z].set(type).save()
            }
        }

        if (!hasVoxel)
            chunk.dispose()
    }

    internal fun buildChunkNeighborhood(chunk: Chunk) {
        if (chunk.isEmpty)
            return

        chunk.neighborSides.alloc()

        for (x in 0 until Chunk.SIZE) {
            for (y in 0 until Chunk.SIZE) {
                for (z in 0 until Chunk.SIZE) {
                    if (chunk.types[x, y, z].get() != VoxelType.NONE) {
                        val sidesRef = chunk.neighborSides[x, y, z]

                        for (side in Side.allSides) {
                            sidesRef[side] = getVoxelNeighbor(chunk, x, y, z, side)
                        }

                        sidesRef.save()
                    }
                }
            }
        }
    }

    private fun getVoxelNeighbor(chunk: Chunk, x: Int, y: Int, z: Int, side: Side): VoxTypeRef? {
        val dir = side.toDirection()

        val neighbor = dir.mutable().add(x, y, z)

        if (chunk.isOnBounds(neighbor.x, neighbor.y, neighbor.z)) {
            return chunk.types[neighbor.x, neighbor.y, neighbor.z]
        } else {
            //cx stands for chunk x
            val (cx, cy, cz) = fromIndex(chunk.index)

            //ncx stands for neighbor chunk x
            val ncx = cx + dir.x
            val ncy = cy + dir.y
            val ncz = cz + dir.z

            if (!isOnBounds(ncx, ncy, ncz))
                return null

            val neighborChunk = this[ncx, ncy, ncz]

            assert(neighborChunk !== chunk) {"#$neighborChunk and $chunk can't be the same"}

            if (neighborChunk.isEmpty)
                return null

            neighbor.reverse(0, Chunk.SIZE - 1)
            return neighborChunk.types[neighbor.x, neighbor.y, neighbor.z]
        }
    }

    private fun isOnBounds(x: Int, y: Int, z: Int): Boolean {
        return x in 0 until World.X_SIZE && y in 0 until World.Y_SIZE && z in 0 until World.Z_SIZE
    }

    private fun setChunkPosition(chunk: Chunk) {
        val vec = fromIndex(chunk.index)
        chunk.x = vec.x * Chunk.SIZE
        chunk.y = vec.y * Chunk.SIZE
        chunk.z = vec.z * Chunk.SIZE
    }

    override fun dispose() {
        chunks.forEach { it.dispose() }
    }

    operator fun get(index: Int): Chunk {
        assert(index in 0 until World.SIZE)
        return chunks[index]
    }

    operator fun get(x: Int, y: Int, z: Int): Chunk {
        assert(x in 0 until World.X_SIZE)
        assert(y in 0 until World.Y_SIZE)
        assert(z in 0 until World.Z_SIZE)
        return chunks[toIndex(x, y, z)]
    }
}