package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

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
    private  ImageWriter iM;
    private  RayTracerBase rT;

    public Camera setImageWriter(ImageWriter iM) {
        this.iM = iM;
        return this;
    }

    public Camera setRayTracer(RayTracerBase rT) {
        this.rT = rT;
        return this;
    }

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

    /**
     * Chaining method for setting the distance between the camera and the view plane.
     *
     * @param distance The new distance between the camera and the view plane.
     * @return The camera itself.
     * @throws IllegalArgumentException When distance illegal.
     */
    public Camera setVPDistance(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Illegal value of distance");
        }

        this.distance = distance;
        return this;
    }



    /**
     * The function calculate the center point of the pixel.
     *
     * @param nX Total number of pixels in the x dimension.
     * @param nY Total number of pixels in the y dimension.
     * @param j  The index of the pixel on the x dimension.
     * @param i  The index of the pixel on the y dimension.
     * @return the center point in the pixel.
     */
    private Point CalculateCenterPointInPixel(int nX, int nY, int j, int i) {
        Point pC = p0.add(vTo.scale(distance));
        Point pIJ = pC;

        double rY = height / nY;
        double rX = width / nX;

        double yI = -(i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;

        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }
        return pIJ;
    }


    /**
     * Constructs a ray through a given pixel on the view plane.
     *
     * @param nX Total number of pixels in the x dimension.
     * @param nY Total number of pixels in the y dimension.
     * @param j The index of the pixel on the x dimension.
     * @param i The index of the pixel on the y dimension.
     * @return
     */

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pCenterPixel = CalculateCenterPointInPixel(nX, nY, j, i);
        return new Ray(p0, pCenterPixel.subtract(p0));
    }
    /**
     * For each pixel a ray will be built and for each ray we will get a color from the ray tracer.
     * Put the color in a suitable pixel of the image writer
     */
    public void renderImage() {
        try {
            //check that no null value has been assigned in imageWriter and rayTracer fields
            if (iM == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (rT == null) {
                throw new MissingResourceException("missing resource",RayTracerBase.class.getName(), "");
            }

            //rendering the image
            int nX = iM.getNx();
            int nY = iM.getNy();
            for (int i = 0; i < nX; i++) {
                for (int j = 0; j < nY; j++) {
                    Ray ray = constructRay(nX, nY, j, i);
                    Color pixelColor = rT.traceRay(ray);
                    iM.writePixel(j, i, pixelColor);
                }
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
    }
    /**
     * create a grid of lines in the view plane
     * @param interval is the interval space between each line
     * @param color is the color of each line
     */
    public void printGrid(int interval, Color color) {
        iM.printGrid(interval,color);
    }
    /**
     * call writeToImage in ImageWriter that produces unoptimized png file of the image
     * according to the pixel color matrix in the directory of the project
     */
    public void writeToImage() {
        //check that no null value has been assigned to imageWriter
        if (iM == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        iM.writeToImage();
    }
}


