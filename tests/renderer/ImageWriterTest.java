package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Vector;
import renderer.ImageWriter;

import static org.junit.jupiter.api.Assertions.*;

import  primitives.Color;

import java.awt.*;

public class ImageWriterTest {
    /**
     * Test method for ImageWriter class
     */
    @Test
    public void test() {
        ImageWriter imageWriter = new ImageWriter("ImageWriterTest", 800, 500);
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        //for all pixels of imageWriter
        for(int i = 0; i < nY; i++)
            for(int j = 0; j < nX; j++)
                imageWriter.writePixel(j, i, j % 50 == 0 || i % 50 == 0 ? Color.BLACK:new Color(20,200,200) );
        imageWriter.writeToImage();


    }
}
