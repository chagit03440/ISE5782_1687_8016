package primitives;
import primitives.Point;
public class Vector extends Point{


    /**
     * Vector constructor
     * @param head of vector
     */

    public Vector(Double3 head) {
        super(head);
        if(head.equals(xyz.ZERO))
        {
            throw new IllegalArgumentException("vector can't be vector0");
        }
        this.xyz = new Double3(head.d1,head.d2,head.d3);
    }

    /**
     * Vector constructor
     * @param x coordinate of vector _head point
     * @param y coordinate of vector _head point
     * @param z coordinate of vector _head point
     */

    public Vector(double x, double y, double z) {
        super(x,y,z);
        Double3 head = new Double3(x, y, z);
        if(head.equals(Double3.ZERO))
        {
            throw new IllegalArgumentException("vector can't be vector0");
        }
        this.xyz = head;
    }


    /**
     * @return _head of the vector
     */

    public Double3 get_head() {
        return xyz;
    }

    /**
     * @param head point of the ray
     */

    public void set_head(Double3 head)
    {
        xyz = new Double3(head.d1,head.d2,head.d3);
    }

    /**
     * @param obj - a vector object
     * @return if two vector object are equals
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector other = (Vector) obj;
        if (xyz == null) {
            if (other.xyz != null)
                return false;
        } else if (!xyz.equals(other.xyz))
            return false;
        return true;
    }

    /**
     * print the details of the vector
     */
    @Override
    public String toString() {
        return "Vector [_head=" + xyz + "]";
    }


    /**
     * @param other vector
     * @return the vector of add between this and other
     */

    public Vector add(Vector other)
    {
        return new Vector(xyz.add(other.xyz));
    }

    /**
     * @param scale double
     * @return the vector of the scale multiplier this
     */

    public Vector scale(double scale)
    {
        return new Vector(this.xyz.scale(scale));
    }

    /**
     * @param other vector
     * @return dotProduct between this and other
     */

    public double dotProduct(Vector other)
    {
        Double3 d=this.xyz.product(other.xyz);
        return (d.d1+d.d2+d.d3);
    }

    /**
     * @param other vector
     * @return crossProduct between this and other
     */

    public Vector crossProduct(Vector other)
    {
        double u1 = xyz.d1;
        double u2 = xyz.d2;
        double u3 = xyz.d3;
        double v1 = other.xyz.d1;
        double v2 = other.xyz.d2;
        double v3 = other.xyz.d3;
        return new Vector(u2*v3 - u3*v2, u3*v1 - u1*v3, u1*v2 - u2*v1);
    }

    /**
     * @return the lengthSquared of vector
     */

    public double lengthSquared()
    {
        double x = xyz.d1;
        double y = xyz.d2;
        double z = xyz.d3;
        return (x*x + y*y + z*z);
    }

    /**
     * @return the length of vector
     */

    public double length()
    {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * @return the vector after normalize (change this)
     */

    public Vector normalize()
    {
        double vectorlen = this.length();
        double x = xyz.d1 / vectorlen;
        double y = xyz.d2 / vectorlen;
        double z = xyz.d3 / vectorlen;
        Double3 xyz=new Double3(x, y, z);
        Vector v=new Vector(xyz);
        return v;
    }



}
