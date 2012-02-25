/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeconstraints;

import java.util.Calendar;

/**
 *
 * @author me
 */
public class TimeConstraints {
    /*
     * used in Backtest_GP setting the end time in MyEvaluate
     */
    public static Calendar returnEndTime()
    {
        Calendar yesterday_date = Calendar.getInstance();
        
        yesterday_date.add(Calendar.DAY_OF_MONTH, -1);
        yesterday_date.set(Calendar.HOUR_OF_DAY, 16);
        yesterday_date.set(Calendar.MINUTE, 1);
        yesterday_date.set(Calendar.SECOND, 0);
        yesterday_date.set(Calendar.MILLISECOND, 0);
        
        return yesterday_date;
    }
    
    /*
     * used in Backtest_GP setting the start time in MyEvaluate
     */
    public static Calendar returnStartTime()
    {
        Calendar one_week_ago = Calendar.getInstance();
        
        one_week_ago.add(Calendar.DAY_OF_MONTH, -14);
        one_week_ago.set(Calendar.HOUR_OF_DAY, 9);
        one_week_ago.set(Calendar.MINUTE, 29);
        one_week_ago.set(Calendar.SECOND, 0);
        one_week_ago.set(Calendar.MILLISECOND, 0);
        
        return one_week_ago;
    }
    
    /**
     * used by the Strategy Execution script and the memcache primer
     * @return 
     */
    public static long returnEndTimeMillis()
    {
        Calendar tc = TimeConstraints.returnEndTime();
        
        return tc.getTimeInMillis();
    }
    
    public static long returnStartTimeMillis()
    {
        Calendar tc = TimeConstraints.returnStartTime();
        
        return tc.getTimeInMillis();
    }
    
    public static String getFilenameFormat()
    {
        Calendar c1 = Calendar.getInstance();
        
        return (c1.get(Calendar.YEAR) + "-" + (c1.get(Calendar.MONTH) + 1) + "-" + c1.get(Calendar.DAY_OF_MONTH));
    }
}
