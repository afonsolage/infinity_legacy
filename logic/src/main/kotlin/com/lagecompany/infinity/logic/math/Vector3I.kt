package com.lagecompany.infinity.math

import com.jme3.math.FastMath
import com.jme3.math.Vector3f
import java.io.Serializable

class MutVector3I(override var x: Int, override var y: Int, override var z: Int) : Vector3I(x, y, z) {

    constructor() : this(0, 0, 0)
    constructor(vector: Vector3I) : this(vector.x, vector.y, vector.z)

    fun set(x: Int, y: Int, z: Int): MutVector3I {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun set(vector: Vector3I): MutVector3I {
        return set(vector.x, vector.y, vector.z)
    }

    fun setZero(): MutVector3I {
        return set(0, 0, 0)
    }

    fun scl(scalar: Int): MutVector3I {
        return set(x * scalar, y * scalar, z * scalar)
    }

    fun scl(scalar: Float): MutVector3I {
        return set((x * scalar).toInt(), (y * scalar).toInt(), (z * scalar).toInt())
    }

    fun setLength2(len2: Int): MutVector3I {
        val oldLen2 = len2()
        return if (oldLen2 == 0 || oldLen2 == len2) this else scl(Math.sqrt(len2 / oldLen2.toDouble()).toInt())
    }

    fun add(x: Int, y: Int, z: Int): MutVector3I {
        return set(this.x + x, this.y + y, this.z + z)
    }

    fun mul(vector: Vector3I): MutVector3I {
        return set(x * vector.x, y * vector.y, z * vector.z)
    }

    fun add(vector: Vector3I): MutVector3I {
        return add(vector.x, vector.y, vector.z)
    }

    fun setToRandomDirection(): MutVector3I {
        var rx = FastMath.nextRandomInt(0, 1)
        var ry = FastMath.nextRandomInt(0, 1)
        var rz = FastMath.nextRandomInt(0, 1)

        if (rx == 0) rx--
        if (ry == 0) ry--
        if (rz == 0) rz--

        return set(rx, ry, rz)
    }

    fun clamp(min: Int, max: Int): MutVector3I {
        return set(FastMath.clamp(x.toFloat(), min.toFloat(), max.toFloat()).toInt(),
            FastMath.clamp(y.toFloat(), min.toFloat(), max.toFloat()).toInt(),
            FastMath.clamp(z.toFloat(), min.toFloat(), max.toFloat()).toInt())
    }

    fun setLength(len: Int): MutVector3I {
        return setLength2(len * len)
    }

    fun sub(vector: Vector3I): MutVector3I {
        return sub(vector.x, vector.y, vector.z)
    }

    fun sub(x: Int, y: Int, z: Int): MutVector3I {
        return set(this.x - x, this.y - y, this.z - z)
    }

    override fun cpy(): MutVector3I {
        return MutVector3I(this)
    }

    override fun mutable(): MutVector3I {
        return this
    }

    fun inv(): MutVector3I {
        return scl(-1)
    }

    fun immutable(): Vector3I {
        return Vector3I(this)
    }

    fun reverse(begin: Int, end: Int): Vector3I {
        val rev = { v: Int ->
            when {
                v < begin -> end + (v + begin + 1)
                v > end -> begin + (v - end - 1)
                else -> v
            }
        }

        x = rev(x)
        y = rev(y)
        z = rev(z)

        return this
    }
}

open class Vector3I(open val x: Int, open val y: Int, open val z: Int) : Serializable {

    constructor() : this(0, 0, 0)
    constructor(vector: Vector3I) : this(vector.x, vector.y, vector.z)

    companion object {
        val X = Vector3I(1, 0, 0)
        val Y = Vector3I(0, 1, 0)
        val Z = Vector3I(0, 0, 1)
        val Zero = Vector3I()
        val One = Vector3I(1, 1, 1)

        fun len2(x: Int, y: Int, z: Int): Int {
            return x * x + y * y + z * z
        }
    }


    val isUnit get() = Math.abs(len2()) == 1

    val isZero get() = x == 0 && y == 0 && z == 0

    fun len2(): Int {
        return len2(x, y, z)
    }

    fun isCollinearOpposite(other: Vector3I): Boolean {
        return isOnLine(other) && hasOppositeDirection(other)
    }

    fun isPerpendicular(other: Vector3I): Boolean {
        return dot(other) == 0
    }

    fun isCollinear(other: Vector3I): Boolean {
        return isOnLine(other) && hasSameDirection(other)
    }

    fun hasSameDirection(other: Vector3I): Boolean {
        return dot(other) > 0
    }

    fun dst(v: Vector3I): Int {
        val a = v.x - x
        val b = v.y - y
        val c = v.z - z
        return Math.sqrt((a * a + b * b + c * c).toDouble()).toInt()
    }

    fun hasOppositeDirection(other: Vector3I): Boolean {
        return dot(other) < 0
    }

    fun dot(vector: Vector3I): Int {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun len(): Int {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toInt()
    }

    fun isOnLine(other: Vector3I): Boolean {
        return len2(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x) <= 0
    }

    open fun cpy(): Vector3I {
        return Vector3I(this)
    }

    open fun mutable(): MutVector3I {
        return MutVector3I(this)
    }

    fun equals(other: MutVector3I): Boolean {
        return x == other.x && y == other.y && z == other.z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is Vector3I) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    operator fun component1(): Int {
        return x
    }

    operator fun component2(): Int {
        return y
    }

    operator fun component3(): Int {
        return z
    }

    override fun toString(): String {
        return "(x=$x, y=$y, z=$z)"
    }

    fun toVector3(): Vector3f {
        return Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    }
}