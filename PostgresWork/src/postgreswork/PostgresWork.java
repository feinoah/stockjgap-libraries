/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postgreswork;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import memcachedlib.MemcachedLib;

/**
 *
 * @author me
 */
public class PostgresWork {
    private Connection conSource;
    private MemcachedLib mclib;
    private static PostgresWork pWork;
    private boolean enable_cache;
    
    private PostgresWork(boolean use_cac)   
    {
        this.enable_cache = use_cac;
        
        if(this.enable_cache)
        {
            this.mclib = MemcachedLib.getMemcachedObject();
        }
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PostgresWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.conSource = DriverManager.getConnection("jdbc:postgresql://192.168.1.104:6432/stocks_pool?user=testuser&password=testpass");
        } catch (SQLException ex) {
            Logger.getLogger(PostgresWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized PostgresWork getPWorkObject(boolean use_cac)
    {
        if(pWork == null)
        {
            pWork = new PostgresWork(use_cac);
        }
        
        return pWork;
    }
    /*
     * New runQuery function, takes in a cached val.
     */
    public ArrayList runQuery(String query, boolean cached)
    {
        if(cached && this.enable_cache)
        {
            Object resMem = this.mclib.getVal(query);
            
            if(resMem != null)
            {
                return (ArrayList)resMem;
            }
        }
        
        ArrayList results = new ArrayList();
        
        try {
            Statement st = this.conSource.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next())   {
                ResultSetMetaData res = rs.getMetaData();

                int noCols = res.getColumnCount();

                ArrayList tempList = new ArrayList();
                
                for(int x = 1; x <= noCols; x++) {
                    if(res.isSearchable(x)) {
                        ArrayList col = new ArrayList();

                        String cName = res.getColumnName(x);
                        Object obj = rs.getObject(cName);

                        col.add(cName);
                        col.add(obj);
                        tempList.add(col);
                    }
                }

                results.add(tempList);
            }

            rs.close();
            st.close();
        }
        catch(SQLException e)   {
            System.out.println("CON: " + this.conSource);
            System.out.println("There was a problem creating the connection:  " + e.toString() + " : "  + e.getErrorCode());
            System.out.println("QUERY:");
            System.out.println(query);
        }


        /*
        System.out.println("Query:  " + query);
        for(int x = 0; x < results.size(); x++) {
            ArrayList temp = (ArrayList)results.get(x);
            for(int y=0; y< temp.size(); y++) {
                System.out.println("Index " + x + ":  " + temp.get(y).toString());
            }
        }
        */

        
        return results;
    }
    
    
    /**
     * The main SELECT function.  By default this will not cache anything
     * 
     * @return
     * @throws SQLException
     */
    public ArrayList runQuery(String query)
    {
        return this.runQuery(query, false);
    }
    
    /*
     * this is ONLY used by the "PrimeMemcached" class
     */
    public void primeCache(String query)
    {
        //cache for a day
        if(this.enable_cache)   
        {
            this.mclib.setVal(query, 86400, this.runQuery(query));
        }
    }
    
    /**
     * used only by the PrimeMemcached script
     */
    public void flushMemcache()
    {
        if(this.enable_cache)
            this.mclib.flushCache();
    }
    
    /*
     * This function is used in buildTables
     */
    public void insertQuery(String query)
    {
        try {
            Statement st = this.conSource.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            rs.close();
            st.close();
        }
        catch(SQLException e)   {
            if(!e.toString().contains("No results were returned by the query"))  {
                System.out.println("ERROR:");
                System.out.println(e.toString());
                System.out.println("QUERY:");
                System.out.println(query);
            }
        }
    }    
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException(); 
    }
}
