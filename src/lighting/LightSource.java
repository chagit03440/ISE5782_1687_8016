package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

public interface LightSource {
    /**
     * Returns light intensity at given point
     * @param p the point
     * @return Color object
     */
    public Color getIntensity(Point p);

    /**
     * Returns the direction of the light at given point
     * @param p the point
     * @return Vector object
     */
    public Vector getL(Point p);

    /**
     * Returns the distance between a light source and a point
     * @param point the point
     * @return distance in double
     */
    public double getDistance(Point point);
    public List<Vector> getBeamL(Point p, double radius, int amount);
}
