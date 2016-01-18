package com.geopista.server.metadatos.db;

import com.geopista.protocol.metadatos.EX_Extent;
import com.geopista.protocol.metadatos.EX_VerticalExtent;
import com.geopista.protocol.metadatos.EX_GeographicBoundingBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 24-ago-2004
 * Time: 16:01:51
 */

public class EX_ExtentDB {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EX_ExtentDB.class);
       EX_Extent  extent;
       public EX_ExtentDB(EX_Extent este)
       {
           extent=este;
       }
       public EX_ExtentDB()
       {
           this(null);
       }
       public String getTable()
       {
           return "EX_EXTENT";
       }

        public EX_Extent getExtent() {
            return extent;
        }

        public void setExtent(EX_Extent extent) {
            this.extent = extent;
        }

        public static boolean save(EX_Extent extent, String identification_id,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,extent.getExtent_id());
                ps.setString(2,identification_id);
                ps.setString(3,extent.getDescription());
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
        public static boolean saveUpdate(EX_Extent extent,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,extent.getDescription());
                ps.setString(2,extent.getExtent_id());

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



        public static boolean saveVertical(EX_VerticalExtent vertical, String extent_id,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,vertical.getId());
                ps.setString(2,extent_id);
                ps.setLong(3,vertical.getMin());
                ps.setLong(4,vertical.getMax());
                ps.setString(5,vertical.getUnit());
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los datos del nuevo EX_VERTICALEXTENT:"+sw.toString());
                return false;
            }
            return true;
        }
        public static boolean saveBox(EX_GeographicBoundingBox box, String extent_id,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,box.getId());
                ps.setString(2,extent_id);
                ps.setInt(3,box.isExtenttypecode()?1:0);
                ps.setFloat(4,box.getWest());
                ps.setFloat(5,box.getEast());
                ps.setFloat(6,box.getSouth());
                ps.setFloat(7,box.getNorth());
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos del nuevo EX_VERTICALEXTENT:"+sw.toString());
                return false;
            }
            return true;
        }
        public static Object load(ResultSet rs)
        {
            EX_Extent result = new EX_Extent();
            try
            {
                result.setExtent_id(rs.getString("extent_id"));
                result.setDescription(rs.getString("description"));
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al recoger el EX_Extent:"+sw.toString());
                 return null;
            }
            return result;
        }
        public static Object loadVertical(ResultSet rs)
        {
                EX_VerticalExtent result = new EX_VerticalExtent();
                try
                {
                    result.setId(rs.getString("verticalextent_id"));
                    result.setMax(rs.getLong("maximumvalue"));
                    result.setMin(rs.getLong("minimumvalue"));
                    result.setUnit(rs.getString("unitofmesure"));
                }catch(Exception e)
                {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     logger.error("ERROR al recoger el EX_Extent:"+sw.toString());
                     return null;
                }
                return result;
         }
            public static Object loadBox(ResultSet rs)
            {
                EX_GeographicBoundingBox result = new EX_GeographicBoundingBox();
                try
                {
                    result.setId(rs.getString("geographicboundingbox_id"));
                    result.setExtenttypecode(rs.getInt("extenttypecode")==1?true:false);
                    result.setNorth(rs.getFloat("northboundlongitude"));
                    result.setEast(rs.getFloat("eastboundlongitude"));
                    result.setSouth(rs.getFloat("southboundlongitude"));
                    result.setWest(rs.getFloat("westboundlongitude"));
                }catch(Exception e)
                {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     logger.error("ERROR al recoger el EX_Extent:"+sw.toString());
                     return null;
                }
                return result;
            }
        public static String getSelect(String identification_id)
        {
            return "Select extent_id, description from EX_EXTENT where identification_id='"
            +identification_id+ "'";
        }
        public static String getSelectVertical(String extent_id)
        {
            return "Select verticalextent_id, minimumvalue,maximumvalue,unitofmesure " +
                    " from EX_VERTICALEXTENT where extent_id='"+extent_id+"'";
        }
        public static String getSelectBox(String extent_id)
        {
            return "select geographicboundingbox_id, extenttypecode, " +
                    " westboundlongitude,eastboundlongitude,southboundlongitude,northboundlongitude " +
                    " from EX_GEOGRAPHICBOUNDINGBOX where extent_id='"+extent_id+"'";
        }

        public static String getInsert()
        {
            return "Insert into EX_EXTENT (extent_id, identification_id, description) " +
               " values (?,?,?)";
        }

        public static String getInsertVertical()
        {
            return "Insert into EX_VERTICALEXTENT (verticalextent_id, extent_id, minimumvalue,maximumvalue,unitofmesure) " +
               " values (?,?,?,?,?)";
        }

        public static String getInsertBox()
        {
            return "Insert into EX_GEOGRAPHICBOUNDINGBOX (geographicboundingbox_id, extent_id, extenttypecode," +
                    "westboundlongitude,eastboundlongitude,southboundlongitude,northboundlongitude) " +
                    "values (?,?,?,?,?,?,?)";
        }
        public static String getDelete(String extent_id)
        {
            return "delete from EX_EXTENT where extent_id='"+extent_id+"'";
        }
         public static String getDeleteBox(String extent_id)
        {
            return "delete from EX_GEOGRAPHICBOUNDINGBOX where extent_id='"+extent_id+"'";
        }
        public static String getDeleteVertical(String extent_id)
        {
            return "delete from EX_VERTICALEXTENT where extent_id='"+extent_id+"'";
        }
        public static String getUpdate()
        {
            return "update EX_EXTENT set description=? where extent_id=?";
        }

        public static boolean exists(String extent_id,Connection connection) throws Exception
        {
           PreparedStatement pS=null;
           ResultSet rsSQL=null;
           if (extent_id==null) return false;
           try
           {
               pS=connection.prepareStatement("select extent_id from EX_EXTENT where extent_id='"+extent_id+"'");
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
                  logger.error("ERROR al comprobar la existencia de extensión:"+sw.toString());
                  throw (e);
             }
       }


}

