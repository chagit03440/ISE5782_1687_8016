package primitives;
import static primitives.Util.isZero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        assertEquals(new Vector(-2, 0, 0), new Point(-1, 2, 3).subtract(p1),"ERROR: Point - Point does not work correctly");
        assertEquals( new Vector(-2, -4, -6), new Point(-1, -2, -3).subtract(p1),"ERROR: Point - Point does not work correctly");
        assertEquals( new Vector(0, -4, -6), new Point(1, -2, -3).subtract(p1),"ERROR: Point - Point does not work correctly");
        Point p2 = new Point(-1, 2, 3);
        assertEquals( new Vector(0, -4, -6), new Point(-1, -2, -3).subtract(p2),"ERROR: Point - Point does not work correctly");
        assertEquals(new Vector(2, -4, -6), new Point(1, -2, -3).subtract(p2),"ERROR: Point - Point does not work correctly");
        Point p3 = new Point(-1, -2, -3);
        assertEquals( new Vector(2, 0, 0), new Point(1, -2, -3).subtract(p3),"ERROR: Point - Point does not work correctly");
        // =============== Boundary Values Tests ==================
        assertEquals(new Vector(-1, -1, -1), new Point(1, 2, 3).subtract(new Point(2, 3, 4)),"ERROR: Point - Point does not work correctly");
        assertEquals(new Vector(1, -1, -1), new Point(-1, 2, 3).subtract( new Point(-2, 3, 4)),"ERROR: Point - Point does not work correctly");
        assertEquals(new Vector(1, 1, 1), new Point(-1, -2, -3).subtract( new Point(-2, -3, -4)),"ERROR: Point - Point does not work correctly");
        assertEquals( new Vector(-1, 0, 0), new Point(1, -2, -3).subtract( new Point(2, -2, -3)),"ERROR: Point - Point does not work correctly");
        try {
            new Point(1, 2, 3).subtract(new Point(1, 2, 3));
            fail("resulte of sub is vector 0");
        } catch (IllegalArgumentException e) {}
    }
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(new Point(0, 4, 6), new Point(-1, 2, 3).add(v1),"ERROR: Point + Vector does not work correctly");
        assertEquals(new Point(-1, 0, 0), new Point(-2, -2, -3).add(v1),"ERROR: Point + Vector does not work correctly");
        assertEquals( new Point(2, 0, 0), new Point(1, -2, -3).add(v1),"ERROR: Point + Vector does not work correctly");
        Vector v2 = new Vector(-1, 2, 3);
        assertEquals(new Point(-2, 0, 0), new Point(-1, -2, -3).add(v2),"ERROR: Point + Vector does not work correctly");
        assertEquals( new Point(1, 0, 0), new Point(2, -2, -3).add(v2),"ERROR: Point + Vector does not work correctly");
        Vector v3 = new Vector(-1, -2, -3);
        assertEquals(new Point(0, -4, -6), new Point(1, -2, -3).add(v3),"ERROR: Point + Vector does not work correctly");
        // =============== Boundary Values Tests ==================
        assertEquals(new Point(3, 5, 7), new Point(1, 2, 3).add(new Vector(2, 3, 4)),"ERROR: Point + Vector does not work correctly");
        assertEquals(new Point(-3, 5, 7), new Point(-1, 2, 3).add( new Vector(-2, 3, 4)),"ERROR: Point + Vector does not work correctly");
        assertEquals( new Point(-3, -5, -7), new Point(-1, -2, -3).add( new Vector(-2, -3, -4)),"ERROR: Point + Vector does not work correctly");
        assertEquals( new Point(3, -4, -6), new Point(1, -2, -3).add( new Vector(2, -2, -3)),"ERROR: Point + Vector does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point v1 = new Point(1, 2, 3);
        assertTrue( isZero(new Point(-1, 2, 3).distanceSquared(v1) - 4),"ERROR: distanceSquared() does not work correctly");
        assertTrue( isZero(new Point(-2, -2, -3).distanceSquared(v1) - 61),"ERROR: distanceSquared() does not work correctly");
        assertTrue( isZero(new Point(1, -2, -3).distanceSquared(v1) - 52),"ERROR: distanceSquared() does not work correctly");
        Point v2 = new Point(-1, 2, 3);
        assertTrue( isZero(new Point(-1, -2, -3).distanceSquared(v2) - 52),"ERROR: distanceSquared() does not work correctly");
        assertTrue(isZero(new Point(2, -2, -3).distanceSquared(v2) - 61),"ERROR: distanceSquared() does not work correctly" );
        Point v3 = new Point(-1, -2, -3);
        assertTrue(isZero(new Point(1, -2, -3).distanceSquared(v3) - 4),"ERROR: distanceSquared() does not work correctly");
        // =============== Boundary Values Tests ==================
        assertTrue( isZero(new Point(1, 2, 3).distanceSquared(new Point(2, 3, 4)) - 3),"ERROR: distanceSquared() does not work correctly");
        assertTrue(isZero(new Point(-1, 2, 3).distanceSquared( new Point(-2, 3, 4)) - 3),"ERROR: distanceSquared() does not work correctly");
        assertTrue( isZero(new Point(-1, -2, -3).distanceSquared( new Point(-2, -3, -4)) - 3),"ERROR: distanceSquared() does not work correctly");
        assertTrue( isZero(new Point(1, -2, -3).distanceSquared( new Point(2, -2, -3)) - 1),"ERROR: distanceSquared() does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        Point v1 = new Point(1, 2, 3);
        assertTrue(isZero(new Point(-1, 2, 3).distance(v1) - Math.sqrt(4)),"ERROR: distance() does not work correctly");
        assertTrue(isZero(new Point(-2, -2, -3).distance(v1) - Math.sqrt(61)),"ERROR: distance() does not work correctly");
        assertTrue(isZero(new Point(1, -2, -3).distance(v1) - Math.sqrt(52)),"ERROR: distance() does not work correctly");
        Point v2 = new Point(-1, 2, 3);
        assertTrue(isZero(new Point(-1, -2, -3).distance(v2) - Math.sqrt(52)),"ERROR: distance() does not work correctly");
        assertTrue(isZero(new Point(2, -2, -3).distance(v2) - Math.sqrt(61)),"ERROR: distance() does not work correctly");
        Point v3 = new Point(-1, -2, -3);
        assertTrue(isZero(new Point(1, -2, -3).distance(v3) - Math.sqrt(4)),"ERROR: distanceSquared() does not work correctly");
        // =============== Boundary Values Tests ==================
        assertTrue(isZero(new Point(1, 2, 3).distance(new Point(2, 3, 4)) - Math.sqrt(3)),"ERROR: distance() does not work correctly");
        assertTrue( isZero(new Point(-1, 2, 3).distance( new Point(-2, 3, 4)) - Math.sqrt(3)),"ERROR: distance() does not work correctly");
        assertTrue( isZero(new Point(-1, -2, -3).distance( new Point(-2, -3, -4)) - Math.sqrt(3)),"ERROR: distance() does not work correctly");
        assertTrue(isZero(new Point(1, -2, -3).distance( new Point(2, -2, -3)) - Math.sqrt(1)),"ERROR: distance() does not work correctly");
    }
}