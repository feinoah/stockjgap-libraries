/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * OCR takes in a rectangle to scrape numbers off of
 * @author me
 */
public final class OCR {    
    private static String getPath()
    {
        //WINDOWS
        //String font_type = "TimesNewRoman";
        //String font_size = "8";

        //UBUNTU
        String font_type = "Courier";
        String font_size = "10";
        
        return "/" + font_type + "/" + font_size + "/";
    }
    
    private static BufferedImage loadTextImage(String name)
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
    
    private static int[] flatten(ArrayList img)
    {
        if(img.isEmpty())
        {
            return new int[0];
        }
        
        int width = ((ArrayList)img.get(0)).size();
        int height = img.size();
        
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

    public static String getText(BufferedImage bi)
    {
        String result = "";
        BufferedImage temp_image;
        BufferedImage period = loadTextImage("period");
        BufferedImage zero = loadTextImage("0");
        BufferedImage one = loadTextImage("1");
        BufferedImage two = loadTextImage("2");
        BufferedImage three = loadTextImage("3");
        BufferedImage four = loadTextImage("4");
        BufferedImage five = loadTextImage("5");
        BufferedImage six = loadTextImage("6");
        BufferedImage seven = loadTextImage("7");
        BufferedImage eight = loadTextImage("8");
        BufferedImage nine = loadTextImage("9");
        
        for(int x=0;x<bi.getWidth();x++)
        {
            //ATTEMPT TO FIND A NINE
            if(x+nine.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, nine.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(nine)))
                {
                    result = result + "9";
                    x = x + nine.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A EIGHT
            if(x+eight.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, eight.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(eight)))
                {
                    result = result + "8";
                    x = x + eight.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A SEVEN
            if(x+seven.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, seven.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(seven)))
                {
                    result = result + "7";
                    x = x + seven.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A SIX
            if(x+six.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, six.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(six)))
                {
                    result = result + "6";
                    x = x + six.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A FIVE
            if(x+five.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, five.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(five)))
                {
                    result = result + "5";
                    x = x + five.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A FOUR
            if(x+four.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, four.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(four)))
                {
                    result = result + "4";
                    x = x + four.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A THREE
            if(x+three.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, three.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(three)))
                {
                    result = result + "3";
                    x = x + three.getWidth();
                    
                    continue;
                }
            }            
            
            //ATTEMPT TO FIND A TWO
            if(x+two.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, two.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(two)))
                {
                    result = result + "2";
                    x = x + two.getWidth();
                    
                    continue;
                }
            }          
            
            //ATTEMPT TO FIND A ONE
            if(x+one.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, one.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(one)))
                {
                    result = result + "1";
                    x = x + one.getWidth();
                    
                    continue;
                }
            }        
            
            //ATTEMPT TO FIND A ZERO
            if(x+zero.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, zero.getWidth(), bi.getHeight());
                
                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(zero)))
                {
                    result = result + "0";
                    x = x + zero.getWidth();
                    
                    continue;
                }
            }        
            
            //ATTEMPT TO FIND A PERIOD
            if(x+period.getWidth() <= bi.getWidth())
            {
                temp_image = bi.getSubimage(x, 0, period.getWidth(), bi.getHeight());

                ArrayList cl = clean_pixels(pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = flatten(cl);
                
                if(Arrays.equals(temp_flat, pixels(period)))
                {
                    result = result + ".";
                    x = x + period.getWidth();
                    
                    continue;
                }
            }            
        }
        
        return result;
    }
        
    
    
    private static void print_image(int[] pixels, int image_width)
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
    private static void print_image(ArrayList pixels)
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
    
    private static ArrayList clean_pixels(int[] pixels, int image_width)
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
            
            if((x + 1) % image_width == 0)
            {
                if(found)
                {
                    pixel_rows.add(temp);
                    
                }
                
                found = false;
                temp = new ArrayList<Integer>();
            }
        }
        
        if(pixel_rows.isEmpty())
        {
            return new ArrayList();
        }
        
        //strip columns that are all -1
        for(int y = 0; y < ((ArrayList)pixel_rows.get(0)).size(); y++)
        {
            found = false;
            
            if(((Integer)((ArrayList)pixel_rows.get(0)).get(y)).intValue() == -1)
            {
                for(int x=0;x<pixel_rows.size();x++)
                {
                    if(((Integer)((ArrayList)pixel_rows.get(x)).get(y)).intValue() != -1)
                    {
                        found = true;
                        break;
                    }
                    
                }
                
                if(!found)
                {
                    for(int x=0;x<pixel_rows.size();x++)
                    {
                        ((ArrayList)pixel_rows.get(x)).remove(y);
                    }
                    y=0;
                }
            }
        }
        
        return pixel_rows;
    }
    
    private static int[] pixels(BufferedImage image) {
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
