/**
 * MD_MetadataDB.java
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

import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.metadatos.MD_MetadataMini;
import com.geopista.protocol.metadatos.PeticionBusqueda;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 20-ago-2004
 * Time: 10:44:57
 */

public class MD_MetadataDB {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MD_MetadataDB.class);
    MD_Metadata metadato;
    public MD_MetadataDB(MD_Metadata este)
    {
        metadato=este;
    }

    public MD_MetadataDB()
    {
        this(null);
    }


    public String getTable()
    {
        return "MD_METADATA";
    }

    public MD_Metadata getMetadato() {
        return metadato;
    }

    public void setMetadato(MD_Metadata metadato) {
        this.metadato = metadato;
    }


    public static Object load(ResultSet rs)
    {
        MD_Metadata result = new MD_Metadata();
        try
        {
            result.setFileidentifier(rs.getString("fileidentifier"));
            result.setDatestamp(rs.getDate("datestamp"));
            result.setLanguage_id(rs.getString("language_id"));
            result.setCharacterset(rs.getString("characterset"));
            result.setMetadatastandardname(rs.getString("metadatastandardname"));
            result.setMetadatastandardversion(rs.getString("metadatastandardversion"));
            result.setScopecode_id(rs.getString("scopecode_id"));
            result.setId_capa(rs.getString("id_layer"));
            result.setId_acl_capa(rs.getString("id_acl"));
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
		     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	 e.printStackTrace(pw);
             logger.error("ERROR al recoger el MD_Metadata:"+sw.toString());
             return null;
        }
        return result;
    }
    public static Object loadParcial(ResultSet rs)
    {
        MD_MetadataMini result = new MD_MetadataMini();
        try
        {
            result.setId(rs.getString("id"));
            result.setFecha(rs.getDate("fecha"));
            result.setNombre(rs.getString("nombre"));
            result.setOrganizacion(rs.getString("organizacion"));
            result.setCargo(rs.getString("cargo"));
            result.setTitulo(rs.getString("titulo"));
            result.setResumen(rs.getString("resumen"));
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
		     java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	 e.printStackTrace(pw);
             logger.error("ERROR al recoger el MD_Metadata:"+sw.toString());
             return null;
        }
        return result;
    }
    public static boolean save(MD_Metadata metadato, String sIdMunicipio,PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            logger.debug("Fecha del metadato:"+metadato.getDatestamp());
            ps.setString(1,metadato.getFileidentifier());
            ps.setLong(2,Long.parseLong(metadato.getLanguage_id()));
            ps.setString(3,metadato.getCharacterset());
             ps.setDate(4,new java.sql.Date(metadato.getDatestamp().getTime()));
            ps.setString(5,metadato.getMetadatastandardname());
            ps.setString(6,metadato.getMetadatastandardversion());
            ps.setLong(7,Long.valueOf(sIdMunicipio));
            ps.setLong(8,Long.valueOf(metadato.getScopecode_id()));
            ps.setLong(9,Long.valueOf(metadato.getId_capa()));
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo metadato:"+sw.toString());
            return false;
        }
        return true;

    }
    public static boolean saveContacto(MD_Metadata metadato, PreparedStatement ps)
    {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,metadato.getFileidentifier());
            if (metadato.getResponsibleParty()==null)
            	ps.setNull(2, java.sql.Types.NUMERIC);
            else
            	ps.setLong(2,Long.valueOf(metadato.getResponsibleParty().getId()));
            ps.setLong(3,Long.parseLong(metadato.getRolecode_id()));
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
    public static boolean saveUpdate(MD_Metadata metadato, PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               ps.setString(1,metadato.getLanguage_id());
               ps.setString(2,metadato.getCharacterset());
               ps.setDate(3,(new java.sql.Date(metadato.getDatestamp().getTime())));
               ps.setString(4,metadato.getMetadatastandardname());
               ps.setString(5,metadato.getMetadatastandardversion());
               ps.setString(6,metadato.getScopecode_id());
               ps.setString(7,metadato.getId_capa());
               ps.setString(8,metadato.getFileidentifier());

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

       public static boolean saveId(String sFileIdentifier, PreparedStatement ps)
       {
           try
           {
               if  (ps==null) return false;
               ps.setString(1,sFileIdentifier);
           }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos para obtener el metadato:"+sw.toString());
               return false;
           }
           return true;

       }
       public static boolean saveReference(MD_Metadata metadato, String sIdReference, PreparedStatement ps)
       {
           try
           {
               if  (ps==null||metadato==null) return false;
               ps.setString(1,metadato.getFileidentifier());
               ps.setString(2,sIdReference);
           }catch(Exception e)
           {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al introducir los datos para salvar un Sistema de referencia:"+sw.toString());
               return false;
           }
           return true;

       }
    public static String getSelect()
    {
       return "Select fileidentifier,language_id,characterset,datestamp,metadatastandardname,metadatastandardversion, " +
               "scopecode_id, MD_METADATA.id_layer as id_layer, LAYERS.acl as id_acl from " +
               "           MD_METADATA left outer join  LAYERS on MD_METADATA.id_layer=LAYERS.id_layer where fileidentifier=?";

    }


    public static String getSelectContacto()
    {
       return "Select responsibleparty_id, rolecode_id from R_METADATA_RESPONSIBLEPARTY where fileidentifier=?";
    }
    public static String getSelectReference (String sFileIdentifier)
      {
         return "Select referencesystem_id from R_METADATA_REFERENCESYSTEM where fileidentifier='"+sFileIdentifier+"'";
      }

    public static String getDeleteReference (String sFileIdentifier)
         {
            return "Delete from R_METADATA_REFERENCESYSTEM where fileidentifier='"+sFileIdentifier+"'";
         }

    public static PreparedStatement getSelectParcial(PeticionBusqueda peticion, Connection connection) throws java.sql.SQLException
    {
        String sPeticion= "select md_metadata.fileidentifier as id, md_metadata.datestamp as fecha, ci_responsibleparty.individualname as nombre, "+
               "ci_responsibleparty.organisationname as organizacion, ci_responsibleparty.positionname as cargo, "+
               "ci_citation.title as titulo, md_dataidentification.abstract as resumen from "+
               "md_metadata, md_dataidentification, ci_citation, ci_responsibleparty, r_metadata_responsibleparty "+
               "where md_dataidentification.fileidentifier=md_metadata.fileidentifier and "+
               "md_dataidentification.citation_id= ci_citation.citation_id and " +
               "(r_metadata_responsibleparty.responsibleparty_id= ci_responsibleparty.responsibleparty_id and "+
               "r_metadata_responsibleparty.fileidentifier=md_metadata.fileidentifier) ";
        PreparedStatement preparedStatement = null;

        if (peticion.getId_contact()!=null)
            sPeticion+=" and r_metadata_responsibleparty.responsibleparty_id ='"+peticion.getId_contact()+"' ";
        //if (peticion.getId_capa()!=null)
        if (peticion.getId_metadato()!=null)
            sPeticion+=" and md_metadata.fileidentifier ='"+peticion.getId_metadato()+"' ";
        if (peticion.getF_desde()!=null)
            sPeticion+=" and md_metadata.datestamp>? ";
        if (peticion.getF_hasta()!=null)
            sPeticion+=" and md_metadata.datestamp<? ";
        if (peticion.getTitulo()!=null)
            sPeticion+=" and ci_citation.title like ?";
        if (peticion.getId_capa()!=null)
                    sPeticion+=" and md_metadata.id_layer=? ";


        preparedStatement= connection.prepareStatement(sPeticion);
        int ipos=0;
        if (peticion.getF_desde()!=null)
        {    ipos++;
             preparedStatement.setDate(ipos, new java.sql.Date(peticion.getF_desde().getTime()));
        }
        if (peticion.getF_hasta()!=null)
        {    ipos++;
             preparedStatement.setDate(ipos, new java.sql.Date(peticion.getF_hasta().getTime()));
        }
        if (peticion.getTitulo()!=null)
        {
            ipos++;
            preparedStatement.setString(ipos, "%"+peticion.getTitulo()+"%");
        }
        if (peticion.getId_capa()!=null)
        {
            ipos++;
            preparedStatement.setString(ipos, peticion.getId_capa());
        }

        return preparedStatement;

    }
    public static String getInsert()
    {
       return "Insert into MD_METADATA (fileidentifier,language_id,characterset,datestamp,metadatastandardname,metadatastandardversion,id_municipio,scopecode_id,id_layer) " +
               " values (?,?,?,?,?,?,?,?,?)";
    }
    public static String getInsertContacto()
    {
       return "Insert into R_METADATA_RESPONSIBLEPARTY (fileidentifier, responsibleparty_id, rolecode_id) " +
               " values (?,?,?)";
    }
    public static String getInsertReference()
    {
       return "Insert into R_METADATA_REFERENCESYSTEM (fileidentifier, referencesystem_id) " +
               " values (?,?)";
    }
    public static String getUpdate()
   {
      return "update MD_METADATA set language_id=? ,characterset=? ,datestamp=?, metadatastandardname=? ,metadatastandardversion= ? , scopecode_id = ? , id_layer=? " +
              " where fileidentifier=? ";
   }
    public static String getDeleteContacto()
    {
        return "delete from R_METADATA_RESPONSIBLEPARTY where fileidentifier=?";
    }
    public static String getDelete()
    {
        return "delete from MD_Metadata where fileidentifier=?";
    }

    public static boolean exists(String fileIdentifier,Connection connection) throws Exception
       {
            PreparedStatement pS=null;
            ResultSet rsSQL=null;
            if (fileIdentifier==null) return false;
            try
            {
                pS=connection.prepareStatement("select fileidentifier from MD_METADATA where fileidentifier='"+fileIdentifier+"'");
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
