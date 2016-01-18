/**
 * CI_CitationDB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.metadatos.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.CI_Date;
import com.geopista.server.database.CPoolDatabase;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 23-ago-2004
 * Time: 12:15:36
 */

public class CI_CitationDB {
     private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_DataIdentificationDB.class);
    CI_Citation  citacion;
    public CI_CitationDB(CI_Citation este)
    {
        citacion=este;
    }

    public CI_CitationDB()
    {
        this(null);
    }


    public String getTable()
    {
        return "CI_CITATION";
    }

    public CI_Citation getCitation() {
        return citacion;
    }

    public void setCitation(CI_Citation citacion) {
        this.citacion = citacion;
    }


    public static Object load(ResultSet rs)
    {
        CI_Citation result = new CI_Citation();
        try
        {
            result.setCitation_id(rs.getString("citation_id"));
            result.setTitle(rs.getString("title"));
            do
            {
                CI_Date auxDate=new CI_Date();
                auxDate.setDate_id(rs.getString("date_id"));
                auxDate.setDate(rs.getDate("date"));
                auxDate.setTipo(rs.getString("datetypecode_id"),rs.getString("pattern"));
                result.addCI_Date(auxDate);
            } while (rs.next());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
		     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	 e.printStackTrace(pw);
             logger.error("ERROR al recoger el CI_Citation:"+sw.toString());
             return null;
        }
        return result;
    }
    public static boolean save(CI_Citation citation, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,citation.getCitation_id());
            ps.setString(2,citation.getTitle());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo CI_CITATION:"+sw.toString());
            return false;
        }
        return true;

    }

     public static boolean saveDate(CI_Date date, String citation_id,PreparedStatement ps)
    {
        try
        {
             if  (ps==null) return false;
            ps.setString(1,date.getDate_id());
            ps.setString(2,citation_id);
            if (date.getTipo()!=null)
                ps.setString(3,date.getTipo().getIdNode());
            else
               ps.setString(3,null);
            ps.setDate(4,new java.sql.Date(date.getDate().getTime()));
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo CI_CITATION:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveUpdate(CI_Citation citation,PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               ps.setString(1,citation.getTitle());
               ps.setString(2,citation.getCitation_id());

                 }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos para actualizar la citacion:"+sw.toString());
               return false;
           }
           return true;

       }

    public static String getSelect(String citation_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "select ci_citation.citation_id as citation_id, ci_citation.title as title" +
                   ", ci_date.date_id as date_id, ci_date.datetypecode_id as datetypecode_id"+
                   ", ci_date.date as date, domainnodes.pattern as pattern from ci_citation," +
                    "ci_date, domainnodes where ci_citation.citation_id="+
                   citation_id +" and ci_citation.citation_id=ci_date.citation_id and " +
                    "ci_date.datetypecode_id=domainnodes.id";
        } else
        {
            return "select ci_citation.citation_id as citation_id, ci_citation.title as title" +
                   ", ci_date.date_id as date_id, ci_date.datetypecode_id as datetypecode_id"+
                   ", ci_date.\"date\" as \"date\", domainnodes.pattern as pattern from ci_citation," +
                    "ci_date, domainnodes where ci_citation.citation_id="+
                   citation_id +" and ci_citation.citation_id=ci_date.citation_id and " +
                    "ci_date.datetypecode_id=domainnodes.id";
        }
    }

    public static String getInsert()
    {
       return "Insert into CI_CITATION (citation_id, title) " +
               " values (?,?)";
    }

    public static String getInsertDate(Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Insert into CI_DATE (date_id, citation_id, datetypecode_id, date) values (?,?,?,?)";
        } else
        {
            return "Insert into CI_DATE (date_id, citation_id, datetypecode_id, \"date\") values (?,?,?,?)";
        }
    }
    public static String getUpdate()
    {
        return "Update CI_CITATION set title=? where citation_id=?";
    }
    public static String getDelete(String citation_id)
    {
        return "Delete from CI_CITATION where citation_id='"+citation_id+"'";
    }
    public static String getDeleteDate(String citation_id)
    {
        return "Delete from CI_DATE where citation_id='"+citation_id+"'";
    }
    public static boolean exists(String citation_id,Connection connection) throws Exception
   {
        PreparedStatement pS=null;
        ResultSet rsSQL=null;
        if (citation_id==null) return false;
        try
        {
            pS=connection.prepareStatement("select citation_id from CI_CITATION where citation_id='"+citation_id+"'");
            rsSQL=pS.executeQuery();
            if (!rsSQL.next())
            {
                try {rsSQL.close();}catch (Exception ex){}
                try {pS.close();}catch (Exception ex){}
                return false;
            }
            try {rsSQL.close();}catch (Exception ex){}
            try {pS.close();}catch (Exception ex){}
            return true;
        }catch(Exception e)
         {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               try {rsSQL.close();}catch (Exception ex){}
               try {pS.close();}catch (Exception ex){}
               logger.error("ERROR al BORRAR la identificación:"+sw.toString());
               throw (e);
          }
     }


}

