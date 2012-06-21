/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author me
 */
public final class ImageLibrary {    
    private final int[] comma_pixels;
    private final int[] period_pixels;
    private final int[] zero_pixels;
    private final int[] one_pixels;
    private final int[] two_pixels;
    private final int[] three_pixels;
    private final int[] four_pixels;
    private final int[] five_pixels;
    private final int[] six_pixels;
    private final int[] seven_pixels;
    private final int[] eight_pixels;
    private final int[] nine_pixels;
    
    private final BufferedImage comma;
    private final BufferedImage period;
    private final BufferedImage zero;
    private final BufferedImage one;
    private final BufferedImage two;
    private final BufferedImage three;
    private final BufferedImage four;
    private final BufferedImage five;
    private final BufferedImage six;
    private final BufferedImage seven;
    private final BufferedImage eight;
    private final BufferedImage nine;

    public ImageLibrary()
    {
        //ArrayList compare_image = new ArrayList();
        BufferedImage slot = null;
        //BufferedImage base_image = null;
        
        //Rectangle region1LastPrice = new Rectangle(317,123,301,34);
        Rectangle region1LastPrice = new Rectangle(25,150,45,50);
        try {
            slot = new Robot().createScreenCapture(region1LastPrice);
        } catch (AWTException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedImage comma1 = null;
        BufferedImage period1 = null;
        BufferedImage zero1 = null;
        BufferedImage one1 = null;
        BufferedImage two1 = null;
        BufferedImage three1 = null;
        BufferedImage four1 = null;
        BufferedImage five1 = null;
        BufferedImage six1 = null;
        BufferedImage seven1 = null;
        BufferedImage eight1 = null;
        BufferedImage nine1 = null;
        
        try {
            comma1 = ImageIO.read(new File("base_images/comma.BMP"));
            period1 = ImageIO.read(new File("base_images/period.BMP"));
            zero1 = ImageIO.read(new File("base_images/0.BMP"));
            one1 = ImageIO.read(new File("base_images/1.BMP"));
            two1 = ImageIO.read(new File("base_images/2.BMP"));
            three1 = ImageIO.read(new File("base_images/3.BMP"));
            four1 = ImageIO.read(new File("base_images/4.BMP"));
            five1 = ImageIO.read(new File("base_images/5.BMP"));
            six1 = ImageIO.read(new File("base_images/6.BMP"));
            seven1 = ImageIO.read(new File("base_images/7.BMP"));
            eight1 = ImageIO.read(new File("base_images/8.BMP"));
            nine1 = ImageIO.read(new File("base_images/9.BMP"));
        } catch (IOException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(comma1 == null || period1 == null || zero1 == null || one1 == null || two1 == null || three1 == null || four1 == null || five1 == null || six1 == null || seven1 == null || eight1 == null || nine1 == null)
        {
            System.out.println("Unable to load a bmp file to check for ocr");
            System.exit(0);
        }

        this.comma = comma1;
        this.period = period1;
        this.zero = zero1;
        this.one = one1;
        this.two = two1;
        this.three = three1;
        this.four = four1;
        this.five = five1;
        this.six = six1;
        this.seven = seven1;
        this.eight = eight1;
        this.nine = nine1;
        
        this.comma_pixels = this.pixels(this.comma);
        this.period_pixels = this.pixels(this.period);
        this.zero_pixels = this.pixels(this.zero);
        this.one_pixels = this.pixels(this.one);
        this.two_pixels = this.pixels(this.two);
        this.three_pixels = this.pixels(this.three);
        this.four_pixels = this.pixels(this.four);
        this.five_pixels = this.pixels(this.five);
        this.six_pixels = this.pixels(this.six);
        this.seven_pixels = this.pixels(this.seven);
        this.eight_pixels = this.pixels(this.eight);
        this.nine_pixels = this.pixels(this.nine);
        
        //get the pixels with all the white space
        int[] scraped_image = this.pixels(slot);
        
        this.print_image(clean_pixels(scraped_image, slot.getWidth(), slot.getHeight()));
        
        //flatten the whitespace removed pixel list (was 3d)
        //scraped_image = this.flatten(clean_pixels(scraped_image, slot.getWidth(), slot.getHeight()), slot.getWidth(), slot.getHeight());
        
        
        this.print_image(this.comma_pixels, comma.getWidth());
        //this.print_image(this.six_pixels, six.getWidth());
        System.out.println(this.print_ocr(slot));
        //System.out.println(Boolean.toString(this.compare(scraped_image)));
    }
    
    private int[] flatten(ArrayList img, int width, int height)
    {
        int[] flat = new int[width*height];
        int z = 0;
        for(int x = 0; x < img.size(); x++)
        {
            for(int y = 0; y < ((ArrayList)img.get(x)).size(); y++)
            {
                flat[z] = ((Integer)((ArrayList)img.get(x)).get(y)).intValue();
                z++;
            }
        }
        return flat;
    }
    private String print_ocr(BufferedImage scraped_image)
    {
        String result = "";
        BufferedImage temp_image;
        
        for(int x=0;x<scraped_image.getWidth();x++)
        {
            if(x+this.comma.getWidth() <= scraped_image.getWidth())
            {
                //check for comma
                temp_image = scraped_image.getSubimage(x, 0, this.comma.getWidth(), scraped_image.getHeight());
                System.out.println("");
                System.out.println("");
                System.out.println("");
                this.print_image(this.pixels(temp_image), this.comma.getWidth());
                System.out.println("");
                System.out.println("");
                System.out.println("");
                int[] temp_flat = this.flatten(this.clean_pixels(this.pixels(temp_image), temp_image.getWidth(), temp_image.getHeight()), temp_image.getWidth(), temp_image.getHeight());
                //if(this.compare(this.pixels(temp_image), this.pixels(this.comma)))
                if(Arrays.equals(temp_flat, this.comma_pixels))
                {
                    System.out.println("FOUND A COMMA");
                    result = result + ",";
                    continue;
                }
            }
        }
        return result;
    }
        
    
    /**
     * 
     * @param a - the image scraped from the screen that we want to OCR
     * @param b - the image we know what the value is
     * @return 
     */
    private boolean compare(int[] a, int[] b) {
        Arrays.sort(a);

        for (int i : b) {
            if (Arrays.binarySearch(a, i) < 0) {
                return false;
            }
        }
        return true;
    }
    
    
    private void print_image(int[] pixels, int image_width)
    {
        for(int row=0;row<pixels.length;row++)
        {
            if(row % image_width == 0)
            {
                System.out.println("");
            }

            if(pixels[row] == -1)
            {
                System.out.print("-");
            }
            else
            {
                System.out.print("*");
            }
        }
    }
    private void print_image(ArrayList pixels)
    {
        for(int row=0;row<pixels.size();row++)
        {
            for(int column=0;column<((ArrayList)pixels.get(row)).size();column++)
            {
                ArrayList row1 = (ArrayList) pixels.get(row);
                Integer item = (Integer) row1.get(column);

                if(item.intValue() == -1)
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
    }
    
    private ArrayList clean_pixels(int[] pixels, int image_width, int image_height)
    {
        boolean found = false;
        ArrayList<ArrayList> pixel_rows = new ArrayList<ArrayList>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //strip rows that are of -1
        for(int x=0;x<pixels.length;x++)
        {
            temp.add(pixels[x]);
            if(pixels[x] != -1)
            {
                found = true;
            }
            
            if(x % image_width == 0)
            {
                if(found)
                {
                    pixel_rows.add(temp);
                    
                }
                
                found = false;
                temp = new ArrayList();
            }
        }
        
        
        //strip columns that are all -1
        int y = 0;
        while(y < ((ArrayList)pixel_rows.get(0)).size())
        {
            found = false;
            
            for(int x=0;x<pixel_rows.size();x++)
            {
                if(((Integer)((ArrayList)pixel_rows.get(x)).get(y)).intValue() != -1)
                {
                    found = true;
                }

            }

            if(found == false)
            {
                for(int z=0;z<pixel_rows.size();z++)
                {
                    ((ArrayList)pixel_rows.get(z)).remove(y);
                }
                y=0;
            }
            else
            {
                y++;
            }
        }
        
        return pixel_rows;
    }
    
    private int[] pixels(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        
        try {
            grabber.grabPixels(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pixels;
    }
    

}
