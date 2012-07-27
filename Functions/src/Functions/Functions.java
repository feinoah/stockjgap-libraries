package Functions;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.math.BigDecimal;

/**
 *
 * @author me
 */
public class Functions {
    public static long calculateADL(BigDecimal close, BigDecimal low, BigDecimal high, Long volInterval)
    {
        //calculate the money flow multiplier:
        BigDecimal numerator = new BigDecimal((close.doubleValue() - low.doubleValue()) - (high.doubleValue() - close.doubleValue()));
        BigDecimal denominator = new BigDecimal((high.doubleValue() - low.doubleValue()));
        
        BigDecimal moneyFlowMult;
        if(denominator.equals(BigDecimal.ZERO))
        {
            moneyFlowMult = BigDecimal.ZERO;
        }
        else
        {
            moneyFlowMult = numerator.divide(denominator, BigDecimal.ROUND_HALF_UP);
        }
        
        BigDecimal moneyFlowVolume = moneyFlowMult.multiply(new BigDecimal(volInterval));

        return moneyFlowVolume.longValue();
    }
    
    public static String getTableName(int interval_step)
    {
        String table = null;
        
        if(interval_step == 1)
        {
            table = "onemin_interval";
        }
        else if(interval_step == 5)
        {
            table = "fivemin_interval";
        }
        else if(interval_step == 10)
        {
            table = "tenmin_interval";
        }
        else if(interval_step == 15)
        {
            table = "fifteenmin_interval";
        }
        else if(interval_step == 30)
        {
            table = "thirtymin_interval";
        }
        else if(interval_step == 390)
        {
            table = "day_interval";
        }
        else
        {
            System.out.println("Bad interval given: " + interval_step);
            System.exit(0);
        }
        
        return table;
    }    
}
