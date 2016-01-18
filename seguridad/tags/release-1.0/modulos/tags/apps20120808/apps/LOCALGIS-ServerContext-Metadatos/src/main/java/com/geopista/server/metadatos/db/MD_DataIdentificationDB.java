package com.geopista.server.metadatos.db;
import com.geopista.protocol.metadatos.MD_DataIdentification;
import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.server.database.CPoolDatabase;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
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
 * Date: 20-ago-2004
 * Time: 14:36:14
 */

public class MD_DataIdentificationDB  {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_DataIdentificationDB.class);
    MD_DataIdentification identificacion;
    public MD_DataIdentificationDB(MD_DataIdentification este)
    {
        identificacion=este;
    }

    public MD_DataIdentificationDB()
    {
        this(null);
    }


    public String getTable()
    {
        return "MD_DATAIDENTIFICATION";
    }

    public MD_DataIdentification getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(MD_DataIdentification identificacion) {
        this.identificacion = identificacion;
    }


    public static Object load(ResultSet rs)
    {
        MD_DataIdentification result = new MD_DataIdentification();
        try
        {
            result.setIdentification_id(rs.getString("identification_id"));
            result.setResumen(rs.getString("abstract"));
            result.setPurpose(rs.getString("purpose"));
            result.setCharacterset(rs.getString("characterset"));
            String sIdCitation=rs.getString("citation_id");
            if (sIdCitation!=null)
            {
                CI_Citation citacion=new CI_Citation();
                citacion.setCitation_id(sIdCitation);
                result.setCitacion(citacion);
            }
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
		     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	 e.printStackTrace(pw);
             logger.error("ERROR al recoger el MD_DataIdentification:"+sw.toString());
             return null;
        }
        return result;
    }
    public static boolean save(MD_DataIdentification identificacion, String fileidentifier, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,identificacion.getIdentification_id());
            ps.setString(2,fileidentifier);
            if (identificacion.getCitacion()!=null)
            {
                logger.debug("El identificador de la citacion es:"+identificacion.getCitacion().getCitation_id());
                ps.setString(3,identificacion.getCitacion().getCitation_id());
            }
            else
                ps.setString(3,null);
            ps.setString(4,identificacion.getResumen());
            ps.setString(5,identificacion.getPurpose());
            ps.setString(6,identificacion.getCharacterset());

        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo DataIdentification:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveContacto(MD_DataIdentification identificacion, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,identificacion.getIdentification_id());
            ps.setString(2,identificacion.getResponsibleParty()!=null?identificacion.getResponsibleParty().getId():null);
            ps.setString(3,identificacion.getRolecode_id());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del contacto metadato:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveDescriptor(MD_DataIdentification identificacion, String sCodeIdioma, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,identificacion.getIdentification_id());
            ps.setString(2,sCodeIdioma);
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los descriptor de la identificacion:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveGrafico(String sCode,MD_DataIdentification identificacion, String sDescricion, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,sCode);
            ps.setString(2,identificacion.getIdentification_id());
            ps.setString(3,sDescricion);
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los descriptor de la identificacion:"+sw.toString());
            return false;
        }
        return true;
     }
    public static boolean saveResolucion(String sCode,MD_DataIdentification identificacion, PreparedStatement ps)
     {
         try
         {
             if  (ps==null) return false;
             ps.setString(1,sCode);
             ps.setString(2,identificacion.getIdentification_id());
             ps.setLong(3,identificacion.getResolucion().longValue());
         }catch(Exception e)
         {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al introducir la resolucion de la identificacion:"+sw.toString());
             return false;
         }
         return true;

     }


    public static boolean saveUpdate(MD_DataIdentification identificacion,PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               if (identificacion.getCitacion()!=null)
                  ps.setString(1,identificacion.getCitacion().getCitation_id());
               else
                  ps.setString(1,null);
               ps.setString(2,identificacion.getResumen());
               ps.setString(3,identificacion.getPurpose());
               ps.setString(4,identificacion.getCharacterset());
           ps.setString(5,identificacion.getIdentification_id());
           }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos para actualizar el metadato:"+sw.toString());
               return false;
           }
           return true;

       }

    public static String getSelect(String sFileIdentifier)
    {
       return "Select identification_id, citation_id, abstract, purpose,characterset" +
               " from MD_DATAIDENTIFICATION where fileidentifier='"+sFileIdentifier+"'";
    }
     public static String getSelectContacto(String identification_id, Connection conn)
    {
         if (CPoolDatabase.isPostgres(conn))
         {
             return "Select responsibleparty_id, rolecode_id from R_IDENTIFICATION_RESPONSIBLEPARTY where identification_id='"
                     +identification_id+"'";
         }
         else {
             return "Select responsibleparty_id, rolecode_id from R_IDENTIF_RESPONSIBLEPARTY where identification_id='"
                     +identification_id+"'";
         }
    }
    public static String getSelectIdioma(String identification_id)
    {
        return "select language_id from R_IDENTIFICATION_LANGUAGE where identification_id='"+identification_id+"'";
    }
    public static String getSelectRespresentacionEspacial(String identification_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "select spatialrepresentationtypecode_id from R_IDENTIFICATION_SPATIALRETYPECODE where identification_id='"+identification_id+"'";
        }
        else {
            return "select spatialretypecode_id from R_IDENTIF_SPATIALRETYPECODE where identification_id='"+identification_id+"'";
        }
    }
    public static String getSelectCategoria(String identification_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "select topiccategorycode_id from R_IDENTIFICATION_TOPICCATEGORYCODE where identification_id='"+identification_id+"'";
        }
        else {
            return "select topiccategorycode_id from R_IDENTIF_TOPICCATEGORYCODE where identification_id='"+identification_id+"'";
        }
    }
    public static String getSelectGrafico(String identification_id)
   {
        return "select filename from MD_BROWSEGRAPHIC where identification_id='"+identification_id+"'";
   }
     public static String getSelectResolucion(String identification_id)
    {
        return "select denominator from MD_RESOLUTION where  identification_id='"+identification_id+"'";
    }


    public static String getInsert()
    {
       return "Insert into MD_DATAIDENTIFICATION (identification_id, fileidentifier, citation_id, abstract, purpose,characterset) " +
               " values (?,?,?,?,?,?)";
    }
    public static String getInsertContacto(Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Insert into R_IDENTIFICATION_RESPONSIBLEPARTY (identification_id, responsibleparty_id, rolecode_id) " +
                    " values (?,?,?)";
        }
        else {
            return "Insert into R_IDENTIF_RESPONSIBLEPARTY (identification_id, responsibleparty_id, rolecode_id) " +
                    " values (?,?,?)";
        }
    }
    public static String getInsertIdioma()
    {
        return "Insert into R_IDENTIFICATION_LANGUAGE (identification_id, language_id) values (?,?)";
    }
     public static String getInsertRespresentacionEspacial(Connection conn)
    {
         if (CPoolDatabase.isPostgres(conn))
         {
             return "Insert into R_IDENTIFICATION_SPATIALRETYPECODE (identification_id, spatialrepresentationtypecode_id) values (?,?)";
         }
         else {
             return "Insert into R_IDENTIF_SPATIALRETYPECODE (identification_id, spatialretypecode_id) values (?,?)";
         }
    }
    public static String getInsertCategoria(Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Insert into R_IDENTIFICATION_TOPICCATEGORYCODE (identification_id, topiccategorycode_id) values (?,?)";
        }
        else {
            return "Insert into R_IDENTIF_TOPICCATEGORYCODE (identification_id, topiccategorycode_id) values (?,?)";
        }
    }
    public static String getInsertGrafico()
    {
        return "Insert into MD_BROWSEGRAPHIC (BROWSEGRAPHIC_ID, identification_id,filename) values (?,?,?)";
    }

    public static String getInsertResolucion()
    {
        return "Insert into MD_RESOLUTION (RESOLUTION_ID, identification_id,denominator) values (?,?,?)";
    }

    public static String getUpdate()
   {
      return "Update MD_DATAIDENTIFICATION set citation_id=?, abstract=?, purpose = ?,characterset=? " +
             " where identification_id=?";
   }

    public static String getDelete(String identification_id)
    {
        return "Delete from MD_DATAIDENTIFICATION where identification_id='"+identification_id+"'";
    }
    public static String getDeleteContact(String identification_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Delete from R_IDENTIFICATION_RESPONSIBLEPARTY where identification_id='"+identification_id+"'";
        }
        else {
            return "Delete from R_IDENTIF_RESPONSIBLEPARTY where identification_id='"+identification_id+"'";
        }
    }
    public static String getDeleteIdioma(String identification_id)
   {
       return "Delete from R_IDENTIFICATION_LANGUAGE where identification_id='"+identification_id+"'";
   }
    public static String getDeleteRespresentacionEspacial(String identification_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Delete from  R_IDENTIFICATION_SPATIALRETYPECODE where identification_id='"+identification_id+"'";
        }
        else {
            return "Delete from  R_IDENTIF_SPATIALRETYPECODE where identification_id='"+identification_id+"'";
        }
    }
    public static String getDeleteCategoria(String identification_id, Connection conn)
    {
        if (CPoolDatabase.isPostgres(conn))
        {
            return "Delete from R_IDENTIFICATION_TOPICCATEGORYCODE where identification_id='"+identification_id+"'";
        }
        else {
            return "Delete from R_IDENTIF_TOPICCATEGORYCODE where identification_id='"+identification_id+"'";
        }
    }
    public static String getDeleteGrafico(String identification_id)
    {
        return "delete from  MD_BROWSEGRAPHIC where identification_id='"+identification_id+"'";
    }

    public static String getDeleteResolucion(String identification_id)
    {
        return "delete from  MD_RESOLUTION where identification_id='"+identification_id+"'";
    }

    public static boolean exists(String identification_id,Connection connection) throws Exception
    {
         PreparedStatement pS=null;
         ResultSet rsSQL=null;
         if (identification_id==null) return false;
         try
         {
             pS=connection.prepareStatement("select identification_id from MD_DATAIDENTIFICATION where identification_id='"+identification_id+"'");
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
