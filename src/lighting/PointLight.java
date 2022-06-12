package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

public class PointLight extends Light implements LightSource{
   private static final Random RND=new Random();
    /**
     * The position of the light
     */
    protected  Point position;
    /**
     * Promises that the light will not be strengthened but will be weakened
     */
    private double KC=1;
    /**
     * reduce the attenuation factor of linear dependence
     */
    private double KL=0;
    /**
     * Reduce the attenuation factor of quadratic dependence
     */
    private double KQ=0;

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }




    public void setPosition(Point position) {
        this.position = position;
    }

    public PointLight setKc(double KC) {
        this.KC = KC;
        return this;
    }

    public PointLight setKl(double KL) {
        this.KL = KL;
        return this;
    }

    public PointLight setKq(double KQ) {
        this.KQ = KQ;
        return this;
    }

    /**
     * constructor Light
     *
     * @param intensity
     */
    protected PointLight(Color intensity) {
        super(intensity);
    }

    /**
     * Calculates the intensity
     * @param p The position of the object
     * @return
     */
    @Override
    public Color getIntensity(Point p) {
        double dist = p.distance(position);

        if(dist <= 0){
            return getIntensity();
        }

        double factor = (KC + dist * KL + (dist * dist) * KQ);

        return getIntensity().reduce(factor);
    }

    /**
     * Returns the vector-direction between the point and the light source
     * @param p calculate the intensity in the point
     * @return
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
    @Override
    public List<Vector> getBeamL(Point p, double radius, int amount) {
        if (p.equals(position)) {
            return null;
        }
        LinkedList<Vector> beam = new LinkedList<>();

        //from pointlight position to p point
        Vector v = this.getL(p);
        beam.add(v);
        if (amount <= 1) {
            return beam;
        }

        double distance = this.position.distance(p);

        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        Point lightHead = new Point(v.getX(),v.getY(),v.getZ());
        Vector normX;

        // if v._head == (0,0,w) then normX.head ==(-w,0,0)
        // otherwise normX.head == (-y,x,0)
        if (isZero(lightHead.getX()) && isZero(lightHead.getY())) {
            normX = new Vector(lightHead.getZ() * -1, 0, 0).normalize();
        } else {
            normX = new Vector(lightHead.getY() * -1, lightHead.getX(), 0).normalize();
        }

        Vector normY = v.crossProduct(normX).normalize();
        double cosTheta;
        double sinTheta;

        double d;
        double x;
        double y;

        for (int counter = 0; counter < amount; counter++) {
            Point newPoint = new Point(this.position.getX(),this.position.getY(),this.position.getZ());
            // randomly coose cosTheta and sinTheta in the range (-1,1)
            cosTheta = 2 * RND.nextDouble() - 1;
            sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

            //d ranged between -radius and  +radius
            d = radius * (2 * RND.nextDouble() - 1);
            //d ranged between -radius and  +radius
            if (isZero(d)) { //Thanks to Michael Shachor
                counter--;
                continue;
            }
            x = d * cosTheta;
            y = d * sinTheta;

            if (!isZero(x)) {
                newPoint = newPoint.add(normX.scale(x));
            }
            if (!isZero(y)) {
                newPoint = newPoint.add(normY.scale(y));
            }
            beam.add(p.subtract(newPoint).normalize());
        }
        return beam;

    }
}
