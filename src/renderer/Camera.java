package renderer;

import primitives.Point;
import primitives.Vector;

import static primitives.Util.isZero;

public class Camera {
    /**
     * Camera's location.
     */
    private Point p0;
    /**
     * Camera's upper direction.
     */
    private Vector vUp;
    /**
     * Camera's forward direction.
     */
    private Vector vTo;
    /**
     * Camera's right direction
     */
    private Vector vRight;
    /**
     * View plane's width.
     */
    private double width;
    /**
     * View plane's height.
     */
    private double height;
    /**
     * The distance between the camera and the view plane.
     */
    private double distance;

    /**
     * Returns the camera location.
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Returns the camera's upper direction.
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Returns the camera's forward direction.
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Returns the camera's right direction.
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Returns the view plane's width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the view plane's height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the distance between the camera and the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Constructs a camera with location, to and up vectors.
     * The right vector is being calculated by the to and up vectors.
     *
     * @param p0  The camera's location.
     * @param vTo The direction to where the camera is looking.
     * @param vUp The direction of the camera's upper direction.
     * @throws IllegalArgumentException When to and up vectors aren't orthogonal.
     */
    public Camera(Point p0, Vector vTo, Vector vUp) {
        if (!isZero(vTo.dotProduct(vUp))) {
            throw new IllegalArgumentException("Up vector is not Orthogonal with To vector");
        }
        this.p0 = p0;
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();
        vRight = vTo.crossProduct(vUp);
    }

    /**
     * Chaining method for setting the view plane's size.
     *
     * @param width  The new view plane's width.
     * @param height The new view plane's height.
     * @return The camera itself.
     */
    public Camera setVPSize(double width, double height) {
        if (width <= 0) {

            throw new IllegalArgumentException("Illegal value of width");
        }
        this.width = width;
        if (height <= 0) {

            throw new IllegalArgumentException("Illegal value of width");
        }
        this.height = height;
        return this;
    }

    public Camera setVPDistance(double distance)
    {
        return  this;
    }

}
