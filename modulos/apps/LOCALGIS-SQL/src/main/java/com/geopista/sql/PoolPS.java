/**
 * PoolPS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.sql;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

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
