package lighting;

import primitives.Color;

class Light {
    private Color intensity;

    public void setIntensity(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * constructor Light
     * @param intensity
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * get Intensity
     * @return
     */
    public Color getIntensity() {
        return intensity;
    }
}
