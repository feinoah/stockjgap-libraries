/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.plaf.synth.Region;
import java.util.Arrays;

/**
 *
 * @author me
 */
public class ImageLibrary {
    public ImageLibrary()
    {
        BufferedImage slot = null;
        BufferedImage base_image = null;
        
        Rectangle region1LastPrice = new Rectangle(317,123,301,34);
        try {
            slot = new Robot().createScreenCapture(region1LastPrice);
        } catch (AWTException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            base_image = ImageIO.read(new File("base_images/6.BMP"));
        } catch (IOException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int[] a = this.pixels(slot);
        int[] b = this.pixels(base_image);
        
        System.out.println(Boolean.toString(this.compare(a, b)));
    }
    
    private int[] pixels(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        int[][] matrix_pixels = new int[image.getWidth()][image.getHeight()];
        
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        
        try {
            grabber.grabPixels(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //build the matrix
        int z = 0;
        for(int row=0;row<image.getHeight();row++)
        {
            int[] temp_column = new int[image.getWidth()];
            boolean found = false;
            for(int column=0;column<image.getWidth();column++)
            {
                temp_column[column] = pixels[z];
                if(pixels[z] != -1)
                {
                    found = true;
                }
                
                z++;
            }
            //ok the row had a pixel that wasn't white, add it to the matrix_pixels
            if(found)
            {
                for(int column=0;column<image.getWidth();column++)
                {
                    matrix_pixels[column][row] = temp_column[column];
                }
            }
            
        }

        
        /**
         * PRINT THE RESULTING IMAGE (debugging)
         */
        for(int row=0;row<matrix_pixels[0].length;row++)
        {
            for(int column=0;column<matrix_pixels.length;column++)
            {
                if(matrix_pixels[column][row] == -1)
                {
                    System.out.print("-");
                }
                else
                {
                    System.out.print("*");
                }
            }
            System.out.println("");
        }
        
        return pixels;
    }
    
    private boolean compare(int[] a, int[] b) {
        Arrays.sort(a);

        for (int i : b) {
            if (Arrays.binarySearch(a, i) < 0) {
                return false;
            }
        }
        return true;
    }
}
