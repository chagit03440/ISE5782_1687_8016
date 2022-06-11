package primitives;

/**
 * Class represents the material of a Geometry
 */
public class Material {
    // Field represents the density attenuation factor
    public Double3 kD = Double3.ZERO;
    // Field represents the
    public Double3 kS = Double3.ZERO;
    // Field represents the shininess of the material
    public int nShininess = 0;
    // Field represents the transparency attenuation factor
    public Double3 kT=Double3.ZERO;
    // Field represents the reflectivity attenuation factor
    public Double3 kR=Double3.ZERO;
    // Field represents the Glossy attenuation factor
    public double kG=1 ;

    /**
     * Builder patterns setter for field kD
     * @param kD parameter for kD
     * @return Material object
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Builder patterns setter for field kD
     * @param value parameter for kD constructor
     * @return Material object
     */
    public Material setKd(double value) {
        this.kD = new Double3(value);
        return this;
    }

    /**
     * Builder patterns setter for field kS
     * @param kS parameter for kS
     * @return Material object
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Builder patterns setter for field kS
     * @param value parameter for kS constructor
     * @return Material object
     */
    public Material setKs(double value) {
        this.kS = new Double3(value);
        return this;
    }

    /**
     * Builder patterns setter for field nShininess
     * @param nShininess parameter for nShininess
     * @return Material object
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
    /**
     * Builder patterns setter for field kR
     * @param kR parameter for kT constructor
     * @return Material object
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }
    public Material setKr(double kR) {
        this.kR =new Double3(kR);
        return this;
    }

    public Double3 getkR() {
        return kR;
    }

    /**
     * Builder patterns setter for field kT
     * @param kT parameter for kT constructor
     * @return Material object
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    public Double3 getkT() {
        return kT;
    }

    /**
     * Chaining method for setting the glossiness of the material.
     * @param kG the glossiness to set, value in range [0,1]
     * @return the current material
     */
    public Material setkG(double kG) {
        this.kG = Math.pow(kG, 0.5);
        return this;
    }

    public double getkG() {
        return kG;
    }
}