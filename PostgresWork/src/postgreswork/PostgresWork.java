/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postgreswork;

import java.sql.*;
import java.util.ArrayList;
import memcachedlib.MemcachedLib;
//import com.jolbox.bonecp.BoneCP;
//import com.jolbox.bonecp.BoneCPConfig;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

/**
 *
 * @author me
 */
public class PostgresWork {
    private Jdbc3PoolingDataSource conSource;
    
    
    //private BoneCP connectionPool = null;
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
        
     /*   try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println(e);
            
            return;
        }

        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl("jdbc:postgresql://192.168.1.104:5432/stocks"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
        config.setUsername("postgres"); 
        config.setPassword("pUL8K6qjPzJQ9nTYwY9D");
        config.setMinConnectionsPerPartition(5);
        config.setMaxConnectionsPerPartition(10);
        config.setPartitionCount(1);

        try {
            this.connectionPool = new BoneCP(config); // setup the connection pool
        } catch (SQLException ex) {
            Logger.getLogger(PostgresWork.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        
this.conSource = new Jdbc3PoolingDataSource();
this.conSource.setServerName("192.168.1.104:5432");
this.conSource.setDatabaseName("stocks");
this.conSource.setUser("postgres");
this.conSource.setPassword("pUL8K6qjPzJQ9nTYwY9D");
this.conSource.setMaxConnections(5);

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
        //System.out.println(query);
        if(cached && this.enable_cache)
        {
            Object resMem = this.mclib.getVal(query);
            
            if(resMem != null)
            {
                return (ArrayList)resMem;
            }
        }
        
        ArrayList results = new ArrayList();
        Connection con = null;
        
        try {
            con = this.conSource.getConnection();
            
            Statement st = con.createStatement();
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
            System.out.println("There was a problem creating the connection:  " + e.toString() + " : "  + e.getErrorCode());
        }
        finally
        {
            if(con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                    System.out.println("There was a problem closing the connection:  " + e.toString() + " : "  + e.getErrorCode());
                }
            }
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
        Connection con = null;
        
        try {
            con = this.conSource.getConnection();
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            rs.close();
            st.close();
        }
        catch(SQLException e)   {
            if(!e.toString().contains("No results were returned by the query"))  {
                System.out.println("We had a problem inserting.  Here's the error:");
                System.out.println(e.toString());
                System.out.println("For this query:");
                System.out.println(query);
            }
        }
        finally
        {
            if(con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                    System.out.println("We had a problem closing the connection.  Here's the error:");
                    System.out.println(e.toString());
                }
            }
        }        
    }    
    
    /**
     * This function is used in StreamerScraper
     * 
     * @return
     * @throws SQLException
     */
    public void historicalInsert(String query) throws SQLException
    {
        Connection con = null;
        
        try {
            con = this.conSource.getConnection();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.close();
            st.close();
        }
        catch(SQLException e)   {
        }
        finally
        {
            if(con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                }
            }
        }        
    }
    
   
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException(); 
    }
}
