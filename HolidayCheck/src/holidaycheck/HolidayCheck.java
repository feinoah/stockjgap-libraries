/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holidaycheck;

import java.util.Calendar;
/**
 *
 * @author me
 */
public final class HolidayCheck
{
    private final Calendar cal;
    
    public HolidayCheck()
    {
        this.cal = Calendar.getInstance();
    }

    public HolidayCheck(Calendar c)
    {
        this.cal = (Calendar)c.clone();
    }

    public HolidayCheck(long milli)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milli);
        
        this.cal = (Calendar)c.clone();
    }

    public Calendar getCalInstance()
    {
        Calendar nc = Calendar.getInstance();
        nc.setTimeInMillis(this.cal.getTimeInMillis());
        
        return nc;
    }

    public String isHolidayCheck()
    {
        return isHolidayCheck(getCalInstance());
    }

    public String isHolidayCheck(Calendar c)
    {
        int year = c.get(Calendar.YEAR);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int dow = c.get(Calendar.DAY_OF_WEEK);
        
        if(year == 2011)
        {
            if(month == 4 && dom == 22 || month == 5 && dom == 30 || month == 7 && dom == 4 || month == 9 && dom == 5 || month == 11 && dom == 24 || month == 12 && dom == 26)
            {
                return "1";
            }
            if(month == 11 && dom == 25)
            {
                return "2";
            }
        } 
        else if(year == 2012)
        {
            if(month == 1 && dom == 2 || month == 1 && dom == 16 || month == 2 && dom == 20 || month == 4 && dom == 6 || month == 5 && dom == 28 || month == 7 && dom == 4 || month == 9 && dom == 3 || month == 11 && dom == 22 || month == 12 && dom == 25)
            {
                return "1";
            }
            if(month == 7 && dom == 3 || month == 11 && dom == 23 || month == 12 && dom == 24)
            {
                return "2";
            }
        }
        
        
        if(dow == Calendar.SATURDAY || dow == Calendar.SUNDAY)
        {
            return "1";
        }
        else
        {
            return "0";
        }
    }

    public boolean isMarketOpen()
    {
        return isMarketOpen(this.cal);
    }

    public boolean isMarketOpen(long milli)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milli);
        
        return isMarketOpen(c);
    }

    public boolean isMarketOpen(Calendar c)
    {
        String isHolidayFlag = isHolidayCheck(c);
        if(isHolidayFlag.contentEquals("1"))
        {
            return false;
        }
        
        int hourEnd = 15;
        if(isHolidayFlag.contentEquals("2"))
        {
            hourEnd = 12;
        }
        
        Calendar openCal = (Calendar)c.clone();
        Calendar closeCal = (Calendar)c.clone();
        
        openCal.set(Calendar.HOUR_OF_DAY, 9);
        openCal.set(Calendar.MINUTE, 30);
        openCal.set(Calendar.SECOND, 0);
        openCal.set(Calendar.MILLISECOND, 0);
        
        closeCal.set(Calendar.HOUR_OF_DAY, hourEnd);
        closeCal.set(Calendar.MINUTE, 59);
        closeCal.set(Calendar.SECOND, 59);
        closeCal.set(Calendar.MILLISECOND, 999);
        
        return (c.compareTo(openCal) >= 0 && c.compareTo(closeCal) <= 0);
    }

    public int getHour()
    {
        return this.cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute()
    {
        return this.cal.get(Calendar.MINUTE);
    }

    public int getSecond()
    {
        return this.cal.get(Calendar.SECOND);
    }

    public int getMillisecond()
    {
        return this.cal.get(Calendar.MILLISECOND);
    }
}    
