package com.geopista.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 30-jun-2006
 * Time: 12:17:47
 */
public class PoolPS {
    Vector pool;
    public PoolPS()
    {
       pool =new Vector();
    }
    public Iterator iterator()
    {
        return pool.iterator();
    }
    public void clear()
    {
        try
        {
            for (Enumeration e=pool.elements();e.hasMoreElements();)
            {
                GEOPISTAPreparedStatement gps=(GEOPISTAPreparedStatement)e.nextElement();
                gps.close();
            }
            pool.clear();
        }catch(Exception e){}

    }
    public void add(GEOPISTAPreparedStatement ps)
    {
        pool.add(ps);
    }

    public Vector getPool() {
        return pool;
    }

    public void setPool(Vector pool) {
        this.pool = pool;
    }

}
