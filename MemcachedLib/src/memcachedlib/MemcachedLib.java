/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memcachedlib;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.spy.memcached.MemcachedClient;

/**
 * ip_address is the ip address of the machine that has the memcache server
 * port is the port that it needs to talk to
 * 
 * javadoc:
 * http://docs.couchbase.org/spymemcached/2.6/
 * 
 * @author me
 */
public class MemcachedLib {
    private final String ip_address = "192.168.1.106";
    private final int port = 11211;
    private final MemcachedClient mc;
    private static MemcachedLib mlib;
    
    /*
     * Constructor
     */
    private MemcachedLib() throws IOException
    {
        mc = new MemcachedClient(new InetSocketAddress(this.ip_address, this.port));
    }
    
    public static synchronized MemcachedLib getMemcachedObject()
    {
        if(mlib == null)
        {
            try {
                mlib = new MemcachedLib();
            } catch (IOException ex) {
                Logger.getLogger(MemcachedLib.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return mlib;
    }
    /*
     * Takes a String of plain text and MD5's it here
     */
    public void setVal(String key, int expires, Object o)
    {
        try {
            this.mc.set(this.toMD5(key), expires, o);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MemcachedLib.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Takes a String of plain text and MD5's it here and returns an Object
     */
    public Object getVal(String key)
    {
        try {
            Future<Object> item = this.mc.asyncGet(this.toMD5(key));
            
            try {
                return item.get(1, TimeUnit.SECONDS);
            } catch (TimeoutException e)  {
                item.cancel(false);
            } catch (java.lang.InterruptedException e)  {
                item.cancel(false);
            } catch (java.util.concurrent.ExecutionException e) {
                item.cancel(false);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MemcachedLib.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /*
     * used only by the PrimeMemcached script
     */
    public void flushCache()
    {
        this.mc.flush();
    }
    
    /*
     * Creates a key MD5 based on the query
     */
    private String toMD5(String in) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] def = in.getBytes();
        
        md.reset();
        md.update(def);
        byte messageDigest[] = md.digest();
        
        StringBuilder hexString = new StringBuilder();
	for (int i=0;i<messageDigest.length;i++) {
		hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	}
        
        return hexString.toString();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException(); 
    }    
}
