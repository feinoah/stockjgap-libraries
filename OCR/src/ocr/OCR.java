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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * OCR takes in a rectangle to scrape numbers off of
 * @author me
 */
public final class OCR {    
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

    private Rectangle raw_rectangle;
    
    /**
     * Use this constructor if you wish to use getText(Rectangle r) later
     */
    public OCR()
    {
        this(new Rectangle(0, 0, 0, 0), "TimesNewRoman", "8");
    }
    
    /**
     * Sent in coordinates is used to create a new rectangle on the screen to scrape
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public OCR(int x, int y, int width, int height)
    {
        this(new Rectangle(x, y, width, height), "TimesNewRoman", "8");
    }
    
    /**
     * Sent in rectangle is used to scrape from the screen
     * Font must be "Regular" and not "bold" or anything else with a white background
     * @param r 
     */
    public OCR(Rectangle r, String font_name, String font_size)
    {
        this.raw_rectangle = r;
        
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
        
        String f_path = "/" + font_name + "/" + font_size + "/";
        
        try {
            period1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "period.bmp"));
            zero1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "0.bmp"));
            one1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "1.bmp"));
            two1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "2.bmp"));
            three1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "3.bmp"));
            four1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "4.bmp"));
            five1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "5.bmp"));
            six1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "6.bmp"));
            seven1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "7.bmp"));
            eight1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "8.bmp"));
            nine1 = ImageIO.read(OCR.class.getResourceAsStream(f_path + "9.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(period1 == null || zero1 == null || one1 == null || two1 == null || three1 == null || four1 == null || five1 == null || six1 == null || seven1 == null || eight1 == null || nine1 == null)
        {
            System.out.println("Unable to load a bmp file to check for ocr");
            System.exit(0);
        }

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
    }
    
    private int[] flatten(ArrayList img)
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
    
    /**
     * TODO delete this after i know things are working
     * @param t1 my OCR text
     * @param t2 Sikuli's OCR text
     */
    public long debug(String t1, String t2)
    {
        if(!t1.contentEquals(t2))
        {
            try 
            {
                BufferedWriter out;
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                String x = month + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
                out = new BufferedWriter(new FileWriter("C:\\postgres_backups\\ocr_debug\\" + x + ".txt", true));

                out.write("For " + cal.getTimeInMillis() + ", MY OCR was " + t1 + ", and Sikuli's was " + t2 + ", picture is " + cal.getTimeInMillis() + ".jpg");
                out.newLine();
                out.close();
                
                return cal.getTimeInMillis();
            } 
            catch (IOException ex) 
            {
            }
        }
        
        return 0;
    }
    
    public Rectangle getRectangle()
    {
        return this.raw_rectangle;
    }
    
    public String getText(Rectangle r)
    {
        this.raw_rectangle = r;
        return this.getText();
    }
    
    public String getText()
    {
        String result = "";
        BufferedImage temp_image;
        BufferedImage slot = null;
        
        try {
            slot = new Robot().createScreenCapture(this.raw_rectangle);
        } catch (AWTException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        if(slot == null)
        {
            System.out.println("Unable to scrape the rectangle for OCR");
            System.exit(0);
        }
        
        for(int x=0;x<slot.getWidth();x++)
        {
            //ATTEMPT TO FIND A NINE
            if(x+this.nine.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.nine.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.nine_pixels))
                {
                    result = result + "9";
                    x = x + this.nine.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A EIGHT
            if(x+this.eight.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.eight.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.eight_pixels))
                {
                    result = result + "8";
                    x = x + this.eight.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A SEVEN
            if(x+this.seven.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.seven.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.seven_pixels))
                {
                    result = result + "7";
                    x = x + this.seven.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A SIX
            if(x+this.six.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.six.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.six_pixels))
                {
                    result = result + "6";
                    x = x + this.six.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A FIVE
            if(x+this.five.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.five.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.five_pixels))
                {
                    result = result + "5";
                    x = x + this.five.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A FOUR
            if(x+this.four.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.four.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.four_pixels))
                {
                    result = result + "4";
                    x = x + this.four.getWidth();
                    
                    continue;
                }
            }
            
            //ATTEMPT TO FIND A THREE
            if(x+this.three.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.three.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.three_pixels))
                {
                    result = result + "3";
                    x = x + this.three.getWidth();
                    
                    continue;
                }
            }            
            
            //ATTEMPT TO FIND A TWO
            if(x+this.two.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.two.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.two_pixels))
                {
                    result = result + "2";
                    x = x + this.two.getWidth();
                    
                    continue;
                }
            }          
            
            //ATTEMPT TO FIND A ONE
            if(x+this.one.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.one.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.one_pixels))
                {
                    result = result + "1";
                    x = x + this.one.getWidth();
                    
                    continue;
                }
            }        
            
            //ATTEMPT TO FIND A ZERO
            if(x+this.zero.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.zero.getWidth(), slot.getHeight());
                
                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.zero_pixels))
                {
                    result = result + "0";
                    x = x + this.zero.getWidth();
                    
                    continue;
                }
            }        
            
            //ATTEMPT TO FIND A PERIOD
            if(x+this.period.getWidth() <= slot.getWidth())
            {
                temp_image = slot.getSubimage(x, 0, this.period.getWidth(), slot.getHeight());

                ArrayList cl = this.clean_pixels(this.pixels(temp_image), temp_image.getWidth());
                
                if(cl.isEmpty())
                {
                    continue;
                }
                
                int[] temp_flat = this.flatten(cl);
                
                if(Arrays.equals(temp_flat, this.period_pixels))
                {
                    result = result + ".";
                    x = x + this.period.getWidth();
                    
                    continue;
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
    
    private ArrayList clean_pixels(int[] pixels, int image_width)
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
