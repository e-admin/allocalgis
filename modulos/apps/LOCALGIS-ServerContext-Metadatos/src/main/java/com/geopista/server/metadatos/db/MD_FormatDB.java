/**
 * MD_FormatDB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.metadatos.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.geopista.protocol.metadatos.MD_Format;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-ago-2004
 * Time: 13:22:10
 */

public class MD_FormatDB {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_FormatDB.class);
    MD_Format  formato;
    public MD_FormatDB(MD_Format este)
    {
        formato=este;
    }
    public MD_FormatDB()
    {
        this(null);
    }
    public String getTable()
    {
        return "MD_FORMAT";
    }

     public MD_Format getFormato() {
         return formato;
     }

     public void setFormato(MD_Format formato) {
         this.formato = formato;
     }
     public static boolean save(MD_Format formato, String fileidentifier,PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,formato.getId());
            ps.setString(2,fileidentifier);
            ps.setString(3,formato.getName());
            ps.setString(4,formato.getVersion());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo MD_FORMAT:"+sw.toString());
            return false;
        }
        return true;
    }
      public static Object load(ResultSet rs)
        {
            MD_Format result = new MD_Format();
            try
            {
                result.setId(rs.getString("format_id"));
                result.setName(rs.getString("name"));
                result.setVersion(rs.getString("version"));
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al recoger el MD_Format:"+sw.toString());
                 return null;
            }
            return result;
        }

    public static String getSelect(String fileIdentifier)
    {
            return "select format_id, name, version from MD_FORMAT where fileidentifier='"+fileIdentifier+"'";
    }
    public static String getInsert()
    {
            return "insert into MD_Format(format_id, fileidentifier,name,version)" +
                    " values (?,?,?,?)";
    }
    public static String getDelete(String fileidentifier)
    {
            return "delete from  MD_Format where fileidentifier = '"+fileidentifier+"'";
    }
}
