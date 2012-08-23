/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * OCR takes in a rectangle to scrape numbers off of
 * @author me
 */
public final class OCR {
    private final Color WHITE = new Color(255, 255, 255);
    
    //windows black
    //private final Color BLACK = new Color(0, 0, 0);
    //private final String font_type = "Windows/Pixelmix";
    
    //ubuntu black
    private final Color BLACK = new Color(18, 0, 0);
    private final String font_type = "Ubuntu/Pixelmix";
    
    //while the font size on the site says 6, and 6 does not do anti-aliasing,
    //the pixel size is 7.
    private final String font_size = "7";

    public OCR()
    {
    }
    
    public int getFontSize()
    {
        return new Integer(this.font_size).intValue();
    }
    
    private String getPath()
    {
        return "/" + this.font_type + "/" + this.font_size + "/";
    }
    
    private BufferedImage loadTextImage(String name)
    {
        String f_path = getPath();
        BufferedImage toget = null;
        
        try {
            toget = ImageIO.read(OCR.class.getResourceAsStream(f_path + name + ".bmp"));
        } catch (IOException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }

        return toget;
    }

    /**
     * Given an image, find "what" based on loaded name
     * @param bi
     * @param what
     * @return [top left x, top left y]
     */
    public int[] getText(BufferedImage bi, String what)
    {
        BufferedImage bi_what = loadTextImage(what);
        int[] coord = new int[2];
        coord[0] = -1;  //x
        coord[1] = -1;  //y
        
        int[] needle_image = pixels(bi_what);
        
        for(int y=0;y<bi.getHeight();y++)
        {
            if(y + bi_what.getHeight() > bi.getHeight())
            {
                break;
            }   
            
            for(int x=0;x<bi.getWidth();x++)
            {
                if(x + bi_what.getWidth() > bi.getWidth())
                {
                    break;
                }
                
                BufferedImage haystack_image = bi.getSubimage(x, y, bi_what.getWidth(), bi_what.getHeight());
                int[] haystack_image_flat = pixels(haystack_image);

                boolean flag = false;
                for(int xx=0;xx<haystack_image_flat.length;xx++)
                {
                    if(haystack_image_flat[xx] != this.BLACK.getRGB())
                    {
                        haystack_image_flat[xx] = this.WHITE.getRGB();
                    }

                    if(haystack_image_flat[xx] != needle_image[xx])
                    {
                        flag = true;
                        break;
                    }
                }

                if(flag)
                {
                    continue;
                }

                //hey, we found it!
                coord[0] = x;
                coord[1] = y;
                
                return coord;
            }
        }
        
        return coord;
    }
    
    /**
     * Used to get RGB for setting up other methods
     * @param what 
     */
    private void debug_color(String what)
    {
        BufferedImage bi_what = loadTextImage(what);
        
        int[] p = pixels(bi_what);
        
        for(int x=0;x<p.length;x++)
        {

                int red = (p[x] >> 16) & 0xFF;
                int green = (p[x] >> 8) & 0xFF;
                int blue = p[x] & 0xFF;
                
                System.out.println("red: " + red + ", green: " + green + ", blue: " + blue);

        }   
    }
    
    public String getTextDigit(BufferedImage bi)
    {
        HashMap<String, BufferedImage> image_map = new HashMap<String, BufferedImage>();
        String result = "";
        BufferedImage temp_image;
        
        image_map.put("0", loadTextImage("0"));
        image_map.put("1", loadTextImage("1"));
        image_map.put("2", loadTextImage("2"));
        image_map.put("3", loadTextImage("3"));
        image_map.put("4", loadTextImage("4"));
        image_map.put("5", loadTextImage("5"));
        image_map.put("6", loadTextImage("6"));
        image_map.put("7", loadTextImage("7"));
        image_map.put("8", loadTextImage("8"));
        image_map.put("9", loadTextImage("9"));
        image_map.put(".", loadTextImage("period"));
        
        for(int x=0;x<bi.getWidth();x++)
        {
            //attempt to find a letter
            for(String character : image_map.keySet())
            {
                BufferedImage img = image_map.get(character);
                
                if(x+img.getWidth() <= bi.getWidth())
                {
                    temp_image = bi.getSubimage(x, 0, img.getWidth(), bi.getHeight());

                    if(Arrays.equals(pixels(temp_image), pixels(img)))
                    {
                        result = result + character;
                        x = x + img.getWidth();

                        break;
                    }
                }
            }
        }
        
        return result;
    }
        
    public String getTextAlpha(BufferedImage bi)
    {
        HashMap<String, BufferedImage> image_map = new HashMap<String, BufferedImage>();
        String result = "";
        BufferedImage temp_image;
        
        image_map.put("A", loadTextImage("A"));
        image_map.put("B", loadTextImage("B"));
        image_map.put("C", loadTextImage("C"));
        image_map.put("D", loadTextImage("D"));
        image_map.put("E", loadTextImage("E"));
        image_map.put("F", loadTextImage("F"));
        image_map.put("G", loadTextImage("G"));
        image_map.put("H", loadTextImage("H"));
        image_map.put("I", loadTextImage("I"));
        image_map.put("J", loadTextImage("J"));
        image_map.put("K", loadTextImage("K"));
        image_map.put("L", loadTextImage("L"));
        image_map.put("M", loadTextImage("M"));
        image_map.put("N", loadTextImage("N"));
        image_map.put("O", loadTextImage("O"));
        image_map.put("P", loadTextImage("P"));
        image_map.put("Q", loadTextImage("Q"));
        image_map.put("R", loadTextImage("R"));
        image_map.put("S", loadTextImage("S"));
        image_map.put("T", loadTextImage("T"));
        image_map.put("U", loadTextImage("U"));
        image_map.put("V", loadTextImage("V"));
        image_map.put("W", loadTextImage("W"));
        image_map.put("X", loadTextImage("X"));
        image_map.put("Y", loadTextImage("Y"));
        image_map.put("Z", loadTextImage("Z"));
        image_map.put(".", loadTextImage("period"));
        image_map.put("$", loadTextImage("$"));
        
        for(int x=0;x<bi.getWidth();x++)
        {
            //attempt to find a letter
            for(String character : image_map.keySet())
            {
                BufferedImage img = image_map.get(character);
                
                if(x+img.getWidth() <= bi.getWidth())
                {
                    temp_image = bi.getSubimage(x, 0, img.getWidth(), bi.getHeight());

                    if(Arrays.equals(pixels(temp_image), pixels(img)))  
                    {
                        result = result + character;
                        x = x + img.getWidth();

                        break;
                    }
                }
            }
        }
        
        return result;
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
    
    private int[] pixels(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        
        try {
            grabber.grabPixels(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pixels;
    }
}
