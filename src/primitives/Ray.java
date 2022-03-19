package primitives;

import static primitives.Util.isZero;

public class Ray {

    /**
     * The point from which the ray starts.
     */

    Point p0;

    /**
     * The direction of the ray.
     */

    Vector dir;

    /**
     * Constructor for creating a new Ray of this class
     *
     * @param point  the start of the ray.
     * @param vector the direction of the ray.
     */

    public Ray(Point point, Vector vector) {

        this.p0 = new Point(point.xyz);
        this.dir = new Vector(vector.normalize().xyz);
    }


    /**
     * @param obj - a ray object
     * @return if two ray object are equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ray other = (Ray) obj;
        if (p0 == null) {
            if (other.p0 != null)
                return false;
        } else if (!p0.equals(other.p0))
            return false;
        if (dir == null) {
            if (other.dir != null)
                return false;
        } else if (!dir.equals(other.dir))
            return false;
        return true;
    }

    /**
     * print the details of the ray
     */
    @Override
    public String toString() {
        return "Ray [_POO=" + p0 + ", _direction=" + dir + "]";
    }

    public Point getP0() {
        return p0;
    }

    public Vector getDir() {
        return dir;
    }
    public Point getPoint(double t)
    {
        return isZero(t) ? p0 : new Point(p0.getXyz()).add(dir.scale(t));
    }
}
