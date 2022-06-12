package renderer;

import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera {
    private boolean adaptiveSuperSampling;
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

    //for the thread
    private int _threads = 1;
    private final int SPARE_THREADS = 2;
    private boolean _print = false;
    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each thread.
     * @author Dan
     *
     */
    private class Pixel {
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        public volatile int row = 0;
        public volatile int col = -1;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Camera.this._print) System.out.printf("\r %02d%%", _percents);
        }

        /**
         *  Default constructor for secondary Pixel objects
         */
        public Pixel() {}

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.<br/>
         * The function provides next pixel number each call.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_print&&_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                target.row = this.row;
                target.col = this.col;
                if (_print&&_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (percents > 0)
                if (Camera.this._print) System.out.printf("\r %02d%%", percents);
            if (percents >= 0)
                return true;
            if (Camera.this._print) System.out.printf("\r %02d%%", 100);
            return false;
        }
    }
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
        this.adaptiveSuperSampling=false;
        vRight = vTo.crossProduct(vUp);
    }
    /**
     * Constructs a camera with location, to and up vectors.
     * The right vector is being calculated by the to and up vectors.
     *
     * @param p0                    The camera's location.
     * @param vTo                   The direction to where the camera is looking.
     * @param vUp                   The direction of the camera's upper direction.
     * @param adaptiveSuperSampling
     * @throws IllegalArgumentException When to and up vectors aren't orthogonal.
     */
    public Camera(Point p0, Vector vTo, Vector vUp, boolean adaptiveSuperSampling) {
        this.adaptiveSuperSampling = adaptiveSuperSampling;
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
    public Camera renderImage() {
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
            final Pixel thePixel = new Pixel(nY, nX);




            //for each point (i,j) in the view plane // i is pixel row number and j is pixel in the row number
            // Generate threads
            Thread[] threads = new Thread[_threads];
            for (int i = _threads - 1; i >= 0; --i) {
                threads[i] = new Thread(() -> {
                    Pixel pixel = new Pixel();
                    while (thePixel.nextPixel(pixel)) {
                        if(this.adaptiveSuperSampling == true)//if we want to apply the adaptiveSuperSampling enhancement
                        {
                            iM.writePixel(pixel.col, pixel.row, adaptiveSuperSampling(pixel.col, pixel.row));
                        }
                        else//if we don't want to apply the adaptiveSuperSampling enhancement
                        {
                            Ray ray = constructRay(nX, nY, pixel.col, pixel.row);
                            GeoPoint closestPoint = rT.findClosestIntersection(ray);
                            Color pixelColor = rT.traceRay(ray);
                            iM.writePixel(pixel.col, pixel.row, pixelColor);


                        }
                    }
                });
            }

            // Start threads
            for (Thread thread : threads) thread.start();

            // Wait for all threads to finish
            for (Thread thread : threads) try { thread.join(); } catch (Exception e) {}
            if (_print) System.out.printf("\r100%%\n");


        }
        catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
        return this;
    }
    /**
     * Set multithreading <br>
     * - if the parameter is 0 - number of coress less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Camera setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading patameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            if (cores <= 2)
                _threads = 1;
            else
                _threads = cores;
        }
        return this;
    }
    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Camera setDebugPrint() {
        _print = true;
        return this;
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
    /**
     *
     * @param Nx
     * @param Ny
     * @param j
     * @param i
     * @return
     * improving the jagged edges - mini project 1
     */
    public List<Ray> constructRays(int Nx, int Ny, int j, int i)
    {
        //Image center
        Point Pc = p0.add(vTo.scale(distance));

        //Ratio (pixel width & height)
        double Ry =height/ Ny;
        double Rx = width/Nx;

        //delta values for going to Pixel[i,j] from Pc
        double yI =  -(i - (Ny -1)/2)* Ry;
        double xJ =  (j - (Nx -1)/2)* Rx;

        if (! isZero(xJ) )
        {
            Pc = Pc.add(vRight.scale(xJ));
        }

        if (! isZero(yI))
        {
            Pc = Pc.add(vUp.scale(yI));
        }
        List<Ray> rays=new ArrayList<>();

        /**
         * puts the pixel center in the first place on the list.
         */
        rays.add(new Ray(p0,Pc.subtract(p0)));

        /**
         * creating Ry*Rx rays for each pixel.
         */
        Point newPoint=new Point(Pc.getX()-Rx/2,Pc.getY()+Rx/2,Pc.getZ());
        for (double t = newPoint.getY(); t >newPoint.getY()-Ry; t-=0.01)
        {
            for (double k = newPoint.getX(); k < newPoint.getX()+Rx; k+=0.01)
            {
                rays.add(new Ray(p0,new Point(k,t,Pc.getZ()).subtract(p0)));
            }
        }

        return rays;
    }
    /**
     * Shell function for adaptive Super Sampling to improve the algorithm
     * @param j index of pixel j
     * @param i index of pixel i
     * @return the color of the pixel j,i
     */
    public Color adaptiveSuperSampling(int j, int i)
    {
        int level = 1;
        int place = 0;
        return adaptiveSuperSampling(j, i, level, place);
    }

    /**
     * adaptive Super Sampling to improve the algorithm
     * @param j index of pixel j
     * @param i index of pixel i
     * @param level of adaptive Super Sampling - we used 4
     * @param place witch part and place in the pixel
     * @return the color of the pixel j,i
     */
    public Color adaptiveSuperSampling(int j, int i, int level, int place)
    {
        Color background = rT.scene.getGackground();
        int nX = iM.getNx();
        int nY = iM.getNy();
        final double dist = distance;

        //flag - to know if are no intersection points to the 4 rays
        boolean flag = true;
        //To know how to divide the X axis and the Y axis
        double power = Math.pow(2, level);
        power = (int)power;
        //dividing the pixel appropriate to the level and make 4 rays in the 4 corners of the pixel
        List<Ray> rays = this.constructRays(nX, nY, j, i  );
        ArrayList<Color> raysColor = new ArrayList<Color>();
        //create a list of the colors of the 4 rays
        for (Ray ray: rays)
        {
            GeoPoint closestPoint = rT.findClosestIntersection(ray);
            //if intersection points to the ray
            if(closestPoint != null)
            {
                flag = false;
                raysColor.add(rT.calcColor(closestPoint, ray));
            }
            else//if no intersection points to the ray
                raysColor.add(background);
        }
        //if are no intersection points to the 4 rays return the background color
        if(flag)
            return background;
        //if its the last call to the function
        if(level == 5)
        {
            //return the average color between the 4 rays
            return averageOfColors(raysColor);
        }
        //if all the colors are equals
        if(isEqualsColors(raysColor))
        {
            return raysColor.get(0);
        }

        //go in to the recursion and calculate the color of the j, i pixels
        //0.25 * color of ray1 + 0.25 * color of ray2 + 0.25 * color of ray3 + 0.25 * color of ray4
        return adaptiveSuperSampling(j, i, level+1, (int)(0 + power * place)).scale(0.25).add(adaptiveSuperSampling(j, i, level+1, (int)(1 + power * place)).scale(0.25).add(adaptiveSuperSampling(j, i, level+1, (int)(2 + power * place)).scale(0.25).add(adaptiveSuperSampling(j, i, level+1, (int)(3 + power * place)).scale(0.25))));

    }
    /**
     *
     * @param raysColor a list of 4 Color objects
     * @return the average of the colors
     * */
    public Color averageOfColors(ArrayList<Color> raysColor)
    {
        double sum_r = 0;
        double sum_g = 0;
        double sum_b = 0;
        for(Color color: raysColor)
        {
            sum_r += color.getColor().getRed();
            sum_g += color.getColor().getGreen();
            sum_b += color.getColor().getBlue();
        }
        return new Color(sum_r / 4, sum_g / 4, sum_b / 4);
    }
    /**
     *
     * @param raysColor a list of 4 Color objects
     * @return if all the colors are equals
     * */
    public boolean isEqualsColors(ArrayList<Color> raysColor)
    {
        for(int i = 0; i < 3; i++)
        {
            if(!raysColor.get(i).equals(raysColor.get(i + 1)))
                return false;
        }
        return true;
    }

}