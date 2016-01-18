/**
 * DQ_DataQualityDB.java
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
import com.geopista.protocol.metadatos.DQ_DataQuality;
import com.geopista.protocol.metadatos.DQ_Element;
import com.geopista.protocol.metadatos.LI_Linage;
import com.geopista.server.database.CPoolDatabase;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 25-ago-2004
 * Time: 16:04:28
 */

public class DQ_DataQualityDB {
       private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DQ_DataQualityDB.class);
       DQ_DataQuality  quality;
       public DQ_DataQualityDB(DQ_DataQuality este)
       {
           quality=este;
       }
       public DQ_DataQualityDB()
       {
           this(null);
       }
       public String getTable()
       {
           return "DQ_DATAQUALITY";
       }

        public DQ_DataQuality getQuality() {
            return quality;
        }

        public void setQuality(DQ_DataQuality extent) {
            this.quality = extent;
        }
        public static boolean save(DQ_DataQuality calidad, String sFileIdentifier, PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               ps.setString(1,calidad.getId());
               ps.setString(2,sFileIdentifier);
               ps.setString(3,calidad.getLinage()!=null?calidad.getLinage().getId():null);
           }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos del nuevo DQ_DATAQUALITY:"+sw.toString());
               return false;
           }
           return true;

       }

        public static boolean saveLinage(LI_Linage linage,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,linage.getId());
                ps.setString(2,linage.getStatement());
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los datos del nuevo LI_LINAGE:"+sw.toString());
                return false;
            }
            return true;

        }
        public static boolean saveLinageDescription(String sCode, LI_Linage linage,
                                                    String sDescription,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,sCode);
                ps.setString(2,linage.getId());
                ps.setString(3,sDescription);
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los datos del nuevo LINAGE DESCRIPTION:"+sw.toString());
                return false;
            }
            return true;

        }
        public static boolean saveElement(DQ_Element element, DQ_DataQuality calidad,PreparedStatement ps)
      {
          try
          {
              if  (ps==null) return false;
              ps.setString(1,element.getId());
              ps.setString(2,calidad.getId());
              ps.setString(3,element.getSubclass_id());
              ps.setString(4,element.getValueunit());
              ps.setString(5,element.getValue());
              ps.setString(6,(element.getCitation()!=null?element.getCitation().getCitation_id():null));
              ps.setString(7,element.getExplanation());
              ps.setInt(8,element.isPass()?1:0);

          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al introducir los datos del nuevo DQ_ELEMENT:"+sw.toString());
              return false;
          }
          return true;

        }

        public static Object load(ResultSet rs)
        {
            DQ_DataQuality result = new DQ_DataQuality();
            try
            {
                result.setId(rs.getString("dataquality_id"));
                String sIdLinage=rs.getString("linage_id");
                if (sIdLinage!=null)
                {
                    LI_Linage linage= new LI_Linage();
                    linage.setId(sIdLinage);
                    result.setLinage(linage);
                }
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al recoger el DQ_DATAQUALITY:"+sw.toString());
                 return null;
            }
            return result;
        }

            public static Object loadElement(ResultSet rs)
            {
                 DQ_Element result = new DQ_Element();
                try
                {
                    result.setId(rs.getString("element_id"));
                    result.setSubclass_id(rs.getString("subclass_id"));
                    result.setValueunit(rs.getString("valueunit"));
                    result.setValue(rs.getString("value"));
                    result.setExplanation(rs.getString("explanation"));
                    result.setPass(rs.getInt("pass")==1?true:false);
                    String sIdCitation=rs.getString("citation_id");
                    if (sIdCitation!=null)
                    {
                        CI_Citation citacion= new CI_Citation();
                        citacion.setCitation_id(sIdCitation);
                        result.setCitation(citacion);
                    }
                }catch(Exception e)
                {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     logger.error("ERROR al recoger el DQ_DATAQUALITY:"+sw.toString());
                     return null;
                }
                return result;
            }
          public static Object loadLinage(ResultSet rs)
            {
                 LI_Linage result = new LI_Linage();
                try
                {
                    result.setId(rs.getString("linage_id"));
                    result.setStatement(rs.getString("statement"));
                    
                }catch(Exception e)
                {
                     java.io.StringWriter sw=new java.io.StringWriter();
                     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                     e.printStackTrace(pw);
                     logger.error("ERROR al recoger el LI_LINAGE:"+sw.toString());
                     return null;
                }
                return result;
            }

        public static String getSelect(String fileidentifier)
        {
            return "select dataquality_id, linage_id from  DQ_DATAQUALITY where "+
                    " fileidentifier='"+fileidentifier+"'";
        }
        public static String getSelectElement(String dataquality_id)
        {
            return "select element_id, subclass_id,valueunit,value,citation_id,explanation,pass " +
                    "from  DQ_ELEMENT where dataquality_id='"+dataquality_id+"'";
        }
        public static String getSelectLinage(String linage_id, Connection conn)
        {
            if (CPoolDatabase.isPostgres(conn))
            {
                return "select linage_id, statement from LI_LINAGE where linage_id='"+linage_id+"'";
            } else
            {
                return "select linage_id, \"statement\" from LI_LINAGE where linage_id='"+linage_id+"'";
            }
        }
        public static String getSelectSource(String linage_id)
        {
            return "select description from LI_SOURCE where linage_id='"+linage_id+"'";
        }
        public static String getSelectStep(String linage_id)
        {
            return "select description from LI_PROCESSSTEP where linage_id='"+linage_id+"'";
        }

        public static String getInsert()
        {
            return "insert into DQ_DATAQUALITY (dataquality_id, fileidentifier, linage_id) values (?,?,?)";
        }
        public static String getInsertLinage(Connection conn)
        {
            if (CPoolDatabase.isPostgres(conn))
            {
                return "insert into LI_LINAGE (linage_id, statement) values (?,?)";
            } else
            {
                return "insert into LI_LINAGE (linage_id, \"statement\") values (?,?)";
            }
        }
        public static String getInsertSource()
        {
            return "insert into LI_SOURCE (source_id, linage_id, description) values (?,?,?)";
        }
        public static String getInsertStep()
        {
            return "insert into LI_PROCESSSTEP (processstep_id, linage_id, description) values (?,?,?)";
        }
        public static String getInsertElement()
        {
            return "insert into DQ_ELEMENT (element_id,dataquality_id, subclass_id,valueunit,value,citation_id,explanation,pass)" +
                    " values (?,?,?,?,?,?,?,?)";
        }
        public static String getDelete(String dataquality_id)
        {
              return "delete from DQ_DATAQUALITY where dataquality_id='"+dataquality_id+"'";
        }

        public static String getDeleteElement(String dataquality_id)
        {
              return "delete from DQ_ELEMENT where dataquality_id='"+dataquality_id+"'";
        }
        public static String getDeleteLinage(String linage_id)
        {
                return "delete from  LI_LINAGE where linage_id='"+linage_id+"'";
        }
        public static String getDeleteSource(String linage_id)
        {
                   return "delete from  LI_SOURCE where linage_id='"+linage_id+"'";
        }
        public static String getDeleteStep(String linage_id)
        {
                   return "delete from  LI_PROCESSSTEP where linage_id='"+linage_id+"'";
        }

}
