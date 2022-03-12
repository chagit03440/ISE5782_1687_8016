package primitives;
import static java.lang.System.out;
import static primitives.Util.isZero;
import geometries.Polygon;
import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);
    Vector v3 = new Vector(0, 3, -2);

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        assertEquals( new Vector(0, 4, 6), new Vector(-1, 2, 3).add(v1),"ERROR: Vector + Vector does not work correctly");
        assertEquals(new Vector(-1, 0, 0), new Vector(-2, -2, -3).add(v1),"ERROR: Vector + Vector does not work correctly");
        assertEquals(new Vector(2, 0, 0), new Vector(1, -2, -3).add(v1),"ERROR: Vector + Vector does not work correctly");
        Vector v2 = new Vector(-1, 2, 3);
        assertEquals(new Vector(-2, 0, 0), new Vector(-1, -2, -3).add(v2),"ERROR: Vector + Vector does not work correctly");
        assertEquals(new Vector(1, 0, 0), new Vector(2, -2, -3).add(v2),"ERROR: Vector + Vector does not work correctly");
        Vector v3 = new Vector(-1, -2, -3);
        assertEquals(new Vector(0, -4, -6), new Vector(1, -2, -3).add(v3),"ERROR: Point + Vector does not work correctly");
        // =============== Boundary Values Tests ==================
        assertEquals( new Vector(3, 5, 7), new Vector(1, 2, 3).add(new Vector(2, 3, 4)),"ERROR: Vector + Vector does not work correctly");
        assertEquals(new Vector(-3, 5, 7), new Vector(-1, 2, 3).add( new Vector(-2, 3, 4)),"ERROR: Vector + Vector does not work correctly");
        assertEquals( new Vector(-3, -5, -7), new Vector(-1, -2, -3).add( new Vector(-2, -3, -4)),"ERROR: Vector + Vector does not work correctly");
        assertEquals(new Vector(3, -4, -6), new Vector(1, -2, -3).add( new Vector(2, -2, -3)),"ERROR: Vector + Vector does not work correctly");
        try {
            new Vector(1, 2, 3).add(new Vector(-1, -2, -3));
            fail("result of add is vector 0");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(4, 4, 2), new Vector(2, 2, 1).scale(2),"ERROR: scale() does not work correctly");
        assertEquals( new Vector(-2, 4, 4), new Vector(-1, 2, 2).scale(2),"ERROR: scale() does not work correctly");
        assertEquals( new Vector(-4, -2, -4), new Vector(-2, -1, -2).scale(2),"ERROR: scale() does not work correctly");
        assertEquals(new Vector(2, -4, -4), new Vector(1, -2, -2).scale(2),"ERROR: scale() does not work correctly");

        assertEquals(new Vector(-4, -4, -2), new Vector(2, 2, 1).scale(-2),"ERROR: scale() does not work correctly");
        assertEquals(new Vector(2, -4, -4), new Vector(-1, 2, 2).scale(-2),"ERROR: scale() does not work correctly");
        assertEquals( new Vector(4, 2, 4), new Vector(-2, -1, -2).scale(-2),"ERROR: scale() does not work correctly");
        assertEquals( new Vector(-2, 4, 4), new Vector(1, -2, -2).scale(-2),"ERROR: scale() does not work correctly");

        try {// test zero scale
            new Vector(1, 2, 3).scale(0);
            fail("ERROR: scale() with zero does not throw an exception");
        } catch (Exception e) {}
    }
    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        assertTrue ( isZero(v1.dotProduct(v3)),"ERROR: dotProduct() for orthogonal vectors is not zero");
        assertTrue ( isZero(v1.dotProduct(v2) + 28),"ERROR: dotProduct() wrong value");


        Vector v1 = new Vector(1, 2, 3);
        assertTrue ( isZero(new Vector(-1, 2, 3).dotProduct(v1) - 12),"ERROR: dotProduct() does not work correctly");
        assertTrue (isZero(new Vector(-2, -2, -3).dotProduct(v1) + 15),"ERROR: dotProduct() does not work correctly");
        assertTrue ( isZero(new Vector(1, -2, -3).dotProduct(v1) + 12),"ERROR: dotProduct() does not work correctly");
        Vector v2 = new Vector(-1, 2, 3);
        assertTrue (isZero(new Vector(-1, -2, -3).dotProduct(v2) + 12),"ERROR: dotProduct() does not work correctly");
        assertTrue (isZero(new Vector(2, -2, -3).dotProduct(v2)+15),"ERROR: dotProduct() does not work correctly");
        Vector v3 = new Vector(-1, -2, -3);
        assertTrue (isZero(new Vector(1, -2, -3).dotProduct(v3) - 12),"ERROR: dotProduct() does not work correctly");
        // =============== Boundary Values Tests ==================
        assertTrue ( isZero(new Vector(1, 2, 3).dotProduct(new Vector(2, 3, 4)) - 20),"ERROR: dotProduct() does not work correctly");
        assertTrue (isZero(new Vector(-1, 2, 3).dotProduct( new Vector(-2, 3, 4)) - 20),"ERROR: dotProduct() does not work correctly");
        assertTrue ( isZero(new Vector(-1, -2, -3).dotProduct( new Vector(-2, -3, -4)) - 20 ),"ERROR: dotProduct() does not work correctly");
        assertTrue (isZero(new Vector(1, -2, -3).dotProduct( new Vector(2, -2, -3)) - 15),"ERROR: dotProduct() does not work correctly");
        assertTrue ( isZero(new Vector(1, 2, 3).dotProduct(new Vector(0, 3, -2))),"ERROR: dotProduct() for orthogonal vectors is not zero");

    }
    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {

        Vector v1 = new Vector(1, 2, 3);
        assertEquals( new Vector(0, 6, -4), new Vector(-1, 2, 3).crossProduct(v1),"ERROR: crossProduct() does not work correctly");
        assertEquals(new Vector(0, 3, -2), new Vector(-2, -2, -3).crossProduct(v1),"ERROR: crossProduct() does not work correctly");
        assertEquals(new Vector(0, -6, 4), new Vector(1, -2, -3).crossProduct(v1),"ERROR: crossProduct() does not work correctly");
        Vector v2 = new Vector(-1, 2, 3);
        assertEquals(new Vector(0, 6, -4), new Vector(-1, -2, -3).crossProduct(v2),"ERROR: crossProduct() does not work correctly");
        assertEquals( new Vector(0, -3, 2), new Vector(2, -2, -3).crossProduct(v2),"ERROR: crossProduct() does not work correctly");
        Vector v3 = new Vector(-1, -2, -3);
        assertEquals( new Vector(0, 6, -4), new Vector(1, -2, -3).crossProduct(v3),"ERROR: crossProduct() does not work correctly");
        // =============== Boundary Values Tests ==================
        assertEquals(new Vector(-1, 2, -1), new Vector(1, 2, 3).crossProduct(new Vector(2, 3, 4)),"ERROR: crossProduct() does not work correctly");
        assertEquals(new Vector(-1, -2, 1), new Vector(-1, 2, 3).crossProduct( new Vector(-2, 3, 4)),"ERROR: crossProduct() does not work correctly");
        assertEquals( new Vector(-1, 2, -1), new Vector(-1, -2, -3).crossProduct( new Vector(-2, -3, -4)),"ERROR: crossProduct() does not work correctly");
        assertEquals( new Vector(0, -3, 2), new Vector(1, -2, -3).crossProduct( new Vector(2, -2, -3)),"ERROR: crossProduct() does not work correctly");
        try {// test zero vector
            new Vector(1, 2, 3).crossProduct(new Vector(-2, -4, -6));
            fail("ERROR: crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {}


        // ============ Equivalence Partitions Tests ==============
        Vector vr = new Vector(1, 2, 3).crossProduct(new Vector(0, 3, -2));
        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertTrue ( isZero(vr.length() - new Vector(1, 2, 3).length() * new Vector(0, 3, -2).length()),"ERROR: crossProduct() wrong result length");
        // Test cross-product result orthogonality to its operands
        assertTrue (isZero(vr.dotProduct(new Vector(1, 2, 3))) && isZero(vr.dotProduct(new Vector(0, 3, -2))),"ERROR: crossProduct() result is not orthogonal to its operands");
    }
    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        assertTrue (isZero(new Vector(1, 2, 3).lengthSquared() - 14),"ERROR: lengthSquared() wrong value");
        assertTrue (isZero(new Vector(-1, 2, 3).lengthSquared() - 14),"ERROR: lengthSquared() wrong value");
        assertTrue (isZero(new Vector(-1, -2, -3).lengthSquared() - 14),"ERROR: lengthSquared() wrong value");
        assertTrue ( isZero(new Vector(1, -2, -3).lengthSquared() - 14),"ERROR: lengthSquared() wrong value");
    }
    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        assertTrue (isZero(new Vector(2, 2, 1).length() - 3),"ERROR: length() wrong value");
        assertTrue ( isZero(new Vector(-1, 2, 2).length() - 3),"ERROR: length() wrong value");
        assertTrue (isZero(new Vector(-2, -1, -2).length() - 3),"ERROR: length() wrong value");
        assertTrue ( isZero(new Vector(1, -2, -2).length() - 3),"ERROR: length() wrong value");
    }
    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();
        // =============== Boundary Values Tests ==================
        assertEquals(1, u.length(), 0.00001, "ERROR: the normalized vector is not a unit vector");
        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(u),
                "ERROR: the normalized vector is not parallel to the original one");
        assertTrue((v.dotProduct(u) >= 0), "ERROR: the normalized vector is opposite to the original one");
    }
}