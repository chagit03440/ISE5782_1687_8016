package primitives;

public class Point {
    public Double3  xyz;

    /**
     * Point constructor
     * @param xyz of point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }
    /**
     * Vector constructor
     * @param x coordinate of vector _head point
     * @param y coordinate of vector _head point
     * @param z coordinate of vector _head point
     */

    public Point(double x, double y, double z) {

        Double3 head = new Double3(x, y, z);
        this.xyz = head;
    }
    /**
     * Subtract two floating point triads into a new triad where each couple of
     * numbers is subtracted
     *
     * @param p right handle side operand for addition
     * @return result of add
     */

    public Vector subtract(Point p) {
        Double3 d=xyz.subtract(p.xyz);
        return new Vector(d.d1,d.d2,d.d3);
    }
    /**
     * @param obj - a point object
     * @return if two point object are equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (xyz == null) {
            if (other.xyz != null)
                return false;
        } else if (!xyz.equals(other.xyz))
            return false;
        return true;
    }
    /**
     * Sum two floating point triads into a new triad where each couple of numbers
     * is summarized
     *
     * @param v right handle side operand for addition
     * @return result of add
     */
    public Point add(Vector v) {
        return new Point(xyz.add(v.xyz));
    }
    double distanceSquared(Point p)
    {
       return (xyz.d1-p.xyz.d1)*(xyz.d1-p.xyz.d1)+(xyz.d2-p.xyz.d2)*(xyz.d2-p.xyz.d2)+(xyz.d3-p.xyz.d3)*(xyz.d3-p.xyz.d3);
    }
    double distance(Point p)
    {
        return Math.sqrt(distanceSquared(p));
    }
}
