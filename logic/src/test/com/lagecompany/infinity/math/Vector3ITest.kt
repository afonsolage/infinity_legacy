package com.lagecompany.infinity.math

import com.jme3.math.FastMath
import com.jme3.math.Vector3f
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class Vector3ITest {

    fun rnd(): Vector3I {
        return Vector3I(FastMath.nextRandomInt(-100, 100), FastMath.nextRandomInt(-100, 100), FastMath.nextRandomInt(-100, 100))
    }

    @Test
    fun isZero() {
        Assertions.assertTrue(Vector3I.Zero.isZero)
        Assertions.assertFalse(Vector3I.X.isZero)
        Assertions.assertFalse(Vector3I.Y.isZero)
        Assertions.assertFalse(Vector3I.Z.isZero)

        Assertions.assertTrue(Vector3I().isZero)

        Assertions.assertFalse(Vector3I(1, 2, 3).isZero)
        Assertions.assertFalse(Vector3I(1, -1, 1).isZero)
        Assertions.assertFalse(Vector3I(1, -1, 0).isZero)
    }

    @Test
    fun len2() {
        repeat(30) {
            val (x, y, z) = rnd()
            Assertions.assertEquals(Vector3I(x, y, z).len2(), Vector3f(x.toFloat(), y.toFloat(), z.toFloat()).lengthSquared().toInt())
        }
    }

    @Test
    fun len21() {
        repeat(30) {
            val (x, y, z) = rnd()
            Assertions.assertEquals(Vector3I.len2(x, y, z), Vector3f(x.toFloat(), y.toFloat(), z.toFloat()).lengthSquared().toInt())
        }
    }

    @Test
    fun isCollinearOpposite() {
        for (x in -5..5) {
            for (y in -5..5) {
                for (z in -5..5) {
                    val v1 = Vector3I(x, y, z)

                    if (v1.isZero)
                        continue;

                    val v2 = Vector3I(x, y, z).mutable().inv()

                    Assertions.assertTrue(v1.isCollinearOpposite(v2)) { "$v1 != $v2" }
                }
            }
        }

        for (x in -5..5) {
            for (y in -5..5) {
                for (z in -5..5) {
                    val v1 = Vector3I(x, y, z)

                    if (v1.isZero)
                        continue

                    val v2 = Vector3I(x, y, z).mutable().scl(FastMath.nextRandomInt(0, 10))

                    Assertions.assertFalse(v1.isCollinearOpposite(v2)) { "$v1 != $v2" }
                }
            }
        }
    }

    @Test
    fun isPerpendicular() {
        Assertions.assertTrue(Vector3I(0, 1, 0).isPerpendicular(Vector3I(0, 0, 1)))
        Assertions.assertTrue(Vector3I(0, 1, 1).isPerpendicular(Vector3I(0, 1, -1)))
        Assertions.assertFalse(Vector3I(1, 1, 1).isPerpendicular(Vector3I(-1, 1, -1)))
    }

    @Test
    fun isCollinear() {
        for (x in -5..5) {
            for (y in -5..5) {
                for (z in -5..5) {
                    val v1 = Vector3I(x, y, z)

                    if (v1.isZero)
                        continue

                    val v2 = Vector3I(x, y, z).mutable().scl(FastMath.nextRandomInt(0, 10))

                    if (v2.isZero)
                        continue

                    Assertions.assertTrue(v1.isCollinear(v2)) { "$v1 != $v2" }
                }
            }
        }
    }

    @Test
    fun hasSameDirection() {
        Assertions.assertTrue(Vector3I.X.hasSameDirection(Vector3I(10, 0, 0)))
        Assertions.assertTrue(Vector3I.Y.hasSameDirection(Vector3I(0, 2, 0)))
        Assertions.assertTrue(Vector3I.Z.hasSameDirection(Vector3I(0, 0, 9)))

        Assertions.assertFalse(Vector3I.X.hasSameDirection(Vector3I(-10, 0, 0)))
        Assertions.assertFalse(Vector3I.Y.hasSameDirection(Vector3I(0, -2, 0)))
        Assertions.assertFalse(Vector3I.Z.hasSameDirection(Vector3I(0, 0, -9)))

        Assertions.assertTrue(Vector3I(1, 1, 0).hasSameDirection(Vector3I(10, 5, 0)))
        Assertions.assertTrue(Vector3I(1, 0, 1).hasSameDirection(Vector3I(11, 0, 9)))
        Assertions.assertTrue(Vector3I(0, 1, 1).hasSameDirection(Vector3I(0, 2, 4)))

        Assertions.assertFalse(Vector3I(0, 0, 0).hasSameDirection(Vector3I(10, 5, 0)))

        Assertions.assertTrue(Vector3I(-1, 1, 0).hasSameDirection(Vector3I(-10, 5, 0)))
        Assertions.assertTrue(Vector3I(1, 0, -1).hasSameDirection(Vector3I(11, 0, -9)))
        Assertions.assertTrue(Vector3I(0, -1, 1).hasSameDirection(Vector3I(0, -2, 4)))

        Assertions.assertFalse(Vector3I(-1, 1, 0).hasSameDirection(Vector3I(10, 5, 0)))
    }

    @Test
    fun dst() {
        repeat(10) {
            val v1 = rnd()
            val v2 = rnd()
            Assertions.assertEquals(v1.dst(v2), v1.toVector3().distance(v2.toVector3()).toInt())
        }
    }

    @Test
    fun hasOppositeDirection() {
        Assertions.assertFalse(Vector3I.X.hasOppositeDirection(Vector3I(10, 0, 0)))
        Assertions.assertFalse(Vector3I.Y.hasOppositeDirection(Vector3I(0, 2, 0)))
        Assertions.assertFalse(Vector3I.Z.hasOppositeDirection(Vector3I(0, 0, 9)))

        Assertions.assertTrue(Vector3I.X.hasOppositeDirection(Vector3I(-10, 0, 0)))
        Assertions.assertTrue(Vector3I.Y.hasOppositeDirection(Vector3I(0, -2, 0)))
        Assertions.assertTrue(Vector3I.Z.hasOppositeDirection(Vector3I(0, 0, -9)))

        Assertions.assertFalse(Vector3I(1, 1, 0).hasOppositeDirection(Vector3I(10, 5, 0)))
        Assertions.assertFalse(Vector3I(1, 0, 1).hasOppositeDirection(Vector3I(11, 0, 9)))
        Assertions.assertFalse(Vector3I(0, 1, 1).hasOppositeDirection(Vector3I(0, 2, 4)))

        Assertions.assertFalse(Vector3I(0, 0, 0).hasOppositeDirection(Vector3I(10, 5, 0)))

        Assertions.assertFalse(Vector3I(-1, 1, 0).hasOppositeDirection(Vector3I(-10, 5, 0)))
        Assertions.assertFalse(Vector3I(1, 0, -1).hasOppositeDirection(Vector3I(11, 0, -9)))
        Assertions.assertFalse(Vector3I(0, -1, 1).hasOppositeDirection(Vector3I(0, -2, 4)))

        Assertions.assertTrue(Vector3I(-1, 1, 0).hasOppositeDirection(Vector3I(10, 5, 0)))
    }

    @Test
    fun dot() {
        repeat(10) {
            val v1 = rnd()
            val v2 = rnd()
            Assertions.assertEquals(v1.dot(v2), v1.toVector3().dot(v2.toVector3()).toInt())
        }
    }

    @Test
    fun len() {
        repeat(10) {
            val v1 = rnd()
            Assertions.assertEquals(v1.len(), v1.toVector3().length().toInt())
        }
    }

    @Test
    fun cpy() {
        repeat(10) {
            val v1 = rnd()
            val v2 = v1.cpy()
            Assertions.assertEquals(v1, v2)
            Assertions.assertFalse(v1 === v2)
        }
    }

    @Test
    fun isUnit() {
        repeat(10) {
            val v1 = rnd()
            Assertions.assertEquals(v1.isUnit, v1.toVector3().isUnitVector)
        }
    }

    @Test
    operator fun component1() {
        val rnd = rnd()
        val (x, _, _) = rnd
        Assertions.assertEquals(rnd.x, x)
    }

    @Test
    operator fun component2() {
        val rnd = rnd()
        val (_, y, _) = rnd
        Assertions.assertEquals(rnd.y, y)
    }

    @Test
    operator fun component3() {
        val rnd = rnd()
        val (_, _, z) = rnd
        Assertions.assertEquals(rnd.z, z)
    }
}