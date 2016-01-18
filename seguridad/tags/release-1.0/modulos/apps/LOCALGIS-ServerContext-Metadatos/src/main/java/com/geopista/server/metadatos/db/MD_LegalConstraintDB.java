package com.geopista.server.metadatos.db;

import com.geopista.protocol.metadatos.MD_LegalConstraint;

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
 * Date: 25-ago-2004
 * Time: 11:38:12
 */

public class MD_LegalConstraintDB {
        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_LegalConstraintDB.class);
        MD_LegalConstraint  constraint;
        public MD_LegalConstraintDB(MD_LegalConstraint este)
        {
            constraint=este;
        }
        public MD_LegalConstraintDB()
        {
            this(null);
        }
        public String getTable()
        {
            return "MD_LEGALCONSTRAINT";
        }

         public MD_LegalConstraint getConstraint() {
             return constraint;
         }

         public void setConstraint(MD_LegalConstraint constraint) {
             this.constraint = constraint;
         }
         public static boolean save(MD_LegalConstraint constraint, String identification_id,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,constraint.getId());
                ps.setString(2,identification_id);
                ps.setString(3,constraint.getUselimitation());
                ps.setString(4,constraint.getOtherconstraint());
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los datos del nuevo MD_LEGALCONSTRAINT:"+sw.toString());
                return false;
            }
            return true;
        }
        public static boolean saveUpdate(MD_LegalConstraint constraint,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,constraint.getUselimitation());
                ps.setString(2,constraint.getOtherconstraint());
                ps.setString(3,constraint.getId());

            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los datos del nuevo MD_LEGALCONSTRAINT:"+sw.toString());
                return false;
            }
            return true;
        }
         public static boolean saveTipos(MD_LegalConstraint constraint, String descriptor,PreparedStatement ps)
        {
            try
            {
                if  (ps==null) return false;
                ps.setString(1,constraint.getId());
                ps.setString(2,descriptor);
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al introducir los tipos de  MD_LEGALCONSTRAINT:"+sw.toString());
                return false;
            }
            return true;
        }

        public static Object load(ResultSet rs)
        {
            MD_LegalConstraint result = new MD_LegalConstraint();
            try
            {
                result.setId(rs.getString("legalconstraint_id"));
                result.setUselimitation(rs.getString("uselimitation"));
                result.setOtherconstraint(rs.getString("otherconstraints"));
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
              return "Select legalconstraint_id, uselimitation,otherconstraints from  MD_LEGALCONSTRAINT "+
                     " where identification_id='"+identification_id+"'";
         }
         public static String getSelectAccess(String legalconstraint_id)
         {
             return "select restrictioncode_id from  R_ACCESSCONSTRAINTS where legalconstraint_id='"+
                     legalconstraint_id+"'";
         }
         public static String getSelectUse(String legalconstraint_id)
         {
              return "select restrictioncode_id from  R_USECONSTRAINTS where legalconstraint_id='"+
                       legalconstraint_id+"'";
         }

         public static String getInsert()
         {
            return "insert into MD_LEGALCONSTRAINT(legalconstraint_id, identification_id,uselimitation,otherconstraints)" +
                    " values (?,?,?,?)";
         }
         public static String getInsertAccess()
         {
             return "insert into R_ACCESSCONSTRAINTS(legalconstraint_id,restrictioncode_id) values (?,?)";
         }
         public static String getInsertUse()
         {
             return "insert into R_USECONSTRAINTS(legalconstraint_id,restrictioncode_id) values (?,?)";
         }
         public static String getDelete(String legalconstraint_id)
         {
             return "delete from MD_LEGALCONSTRAINT where legalconstraint_id='"+legalconstraint_id+"'";
         }
          public static String getDeleteAccess(String legalconstraint_id)
          {
              return "delete from R_ACCESSCONSTRAINTS where legalconstraint_id='"+legalconstraint_id+"'";
          }
         public static String getDeleteUse(String legalconstraint_id)
         {
             return "delete from R_USECONSTRAINTS where legalconstraint_id='"+legalconstraint_id+"'";
         }
         public static String getUpdate()
         {
              return "update MD_LEGALCONSTRAINT set uselimitation=? , otherconstraints =? " +
                      " where legalconstraint_id=?";
         }
         public static boolean exists(String legalconstraint_id,Connection connection) throws Exception
          {
               PreparedStatement pS=null;
               ResultSet rsSQL=null;
               if (legalconstraint_id==null) return false;
               try
               {
                   pS=connection.prepareStatement("select legalconstraint_id from MD_LEGALCONSTRAINT where legalconstraint_id='"+legalconstraint_id+"'");
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
