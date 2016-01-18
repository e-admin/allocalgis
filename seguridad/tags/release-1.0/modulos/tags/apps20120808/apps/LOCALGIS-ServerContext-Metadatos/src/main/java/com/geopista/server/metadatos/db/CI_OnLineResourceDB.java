package com.geopista.server.metadatos.db;

import com.geopista.protocol.metadatos.CI_OnLineResource;
import com.geopista.protocol.metadatos.OperacionesMetadatos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * Date: 23-jul-2004
 * Time: 11:08:52
 */

public class CI_OnLineResourceDB {
        private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CI_OnLineResourceDB.class);
        CI_OnLineResource onLineResource;
        public CI_OnLineResourceDB(CI_OnLineResource este)
        {
            onLineResource=este;
        }

        public CI_OnLineResourceDB()
        {
            this(null);
        }


        public String getTable()
        {
            return "CI_OnlineResource";
        }

        public CI_OnLineResource getOnLineResource()
        {
            return onLineResource;
        }

        public static Object load(ResultSet rs)
        {
            try
            {
                CI_OnLineResource result = new CI_OnLineResource();
                result.setId(rs.getString("OnLineResource_id"));
                result.setLinkage(rs.getString("linkage"));
                result.setIdOnLineFunctionCode(rs.getString("onlinefunctionCode_id"));
                return result;
            }catch(Exception e)
            {
                java.io.StringWriter sw=new java.io.StringWriter();
		        java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	    e.printStackTrace(pw);
                logger.error("ERROR al recoger el CI_OnLineResource:"+sw.toString());
                return null;
            }
        }
        public static String getSelect(String fileIdentifier)
        {
            return "select CI_OnLineResource.onlineresource_id as onlineresource_id, CI_OnLineResource.linkage as " +
                    " linkage, CI_OnLineResource.onlinefunctioncode_id as onlinefunctioncode_id from " +
                    " MD_DIGITALTRANSFEROPTIONS, CI_ONLINERESOURCE where MD_DIGITALTRANSFEROPTIONS.fileidentifier='" +
                    fileIdentifier+ "' and MD_DIGITALTRANSFEROPTIONS.onlineresource_id=CI_ONLINERESOURCE.onlineresource_id";
        }
        public static String getInsert()
        {
            return "insert into CI_OnLineResource (onlineresource_id, linkage, onlinefunctioncode_id) values (?,?,?)";
        }
        public static String getInsertRecursoMetadata()
        {
            return "insert into MD_DIGITALTRANSFEROPTIONS (fileidentifier,onlineresource_id) values (?,?)";
        }
        public static String getUpdate()
        {
            return "update CI_OnLineResource set linkage=? , onlinefunctioncode_id=? where onlineresource_id=?";
        }
        public static String getDelete()
        {
            return "delete from CI_OnLineResource where onlineresource_id=?";
        }
        public static String getDeleteRecursoMetadata(String fileidentifier)
        {
            return "delete from MD_DIGITALTRANSFEROPTIONS where fileidentifier='"+fileidentifier+"'";
        }

        public static boolean saveRecursoMetadata(String fileidentifier,CI_OnLineResource resource, PreparedStatement ps)
       {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,fileidentifier);
            ps.setString(2,resource.getId());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos de un nuevo recurso en un metadato:"+sw.toString());
            return false;
        }
            return true;
        }
       public static boolean save(CI_OnLineResource resource, PreparedStatement ps)
       {
        try
        {
            if  (ps==null) return false;
            ps.setString(1,resource.getId());
            ps.setString(2,resource.getLinkage());
            ps.setString(3,resource.getIdOnLineFunctionCode());
        }catch(Exception e)
        {
             java.io.StringWriter sw=new java.io.StringWriter();
             java.io.PrintWriter pw=new java.io.PrintWriter(sw);
             e.printStackTrace(pw);
             logger.error("ERROR al introducir los datos del nuevo On line resource:"+sw.toString());
            return false;
        }
            return true;
        }

            public static boolean saveUpdate(CI_OnLineResource resource, PreparedStatement ps)
             {
              try
              {
                  if  (ps==null) return false;
                  ps.setString(1,resource.getLinkage());
                  ps.setString(2,resource.getIdOnLineFunctionCode());
                  ps.setString(3,resource.getId());

              }catch(Exception e)
              {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("ERROR al introducir los datos del nuevo On line resource:"+sw.toString());
                  return false;
              }

        return true;
        }
        public static boolean exists(String onlineresource_id,Connection connection) throws Exception
        {
               PreparedStatement pS=null;
               ResultSet rsSQL=null;
               if (onlineresource_id==null) return false;
               try
               {
                   pS=connection.prepareStatement("select onlineresource_id from CI_ONLINERESOURCE where onlineresource_id='"+onlineresource_id+"'");
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
