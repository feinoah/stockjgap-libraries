/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primememcached;

import java.util.ArrayList;
import postgreswork.PostgresWork;

/**
 *  This project primes the memcached database and is run every weekday at 17:00
 * hours.
 * 
 * @author me
 */
public class PrimeMemcached {
    private static ArrayList getQueries()
    {
        ArrayList<String> queries = new ArrayList();
        ArrayList results = PostgresWork.getPWorkObject(true).runQuery("SELECT DISTINCT ticker FROM info");
        
        String ticker = "";
        for(int x = 0; x < results.size(); x++)
        {
            ticker = ((ArrayList)((ArrayList)results.get(x)).get(0)).get(1).toString();

            //queries for backtesting
            queries.add("SELECT time FROM info WHERE ticker = '" + ticker + "' ORDER BY time ASC");
            queries.add("SELECT interval_end FROM onemin_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");
            queries.add("SELECT interval_end FROM fivemin_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");
            queries.add("SELECT interval_end FROM tenmin_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");
            queries.add("SELECT interval_end FROM fifteenmin_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");
            queries.add("SELECT interval_end FROM thirtymin_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");
            queries.add("SELECT interval_end FROM day_interval WHERE ticker = '" + ticker + "' ORDER BY interval_end ASC");

            //queries for strategy execution
        }

        return queries;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> queries = getQueries();

        PostgresWork.getPWorkObject(true).flushMemcache();
        
        for(int x=0; x<queries.size();x++)
        {
            PostgresWork.getPWorkObject(true).primeCache(queries.get(x));
        }
        
        System.exit(0);
    }
}
