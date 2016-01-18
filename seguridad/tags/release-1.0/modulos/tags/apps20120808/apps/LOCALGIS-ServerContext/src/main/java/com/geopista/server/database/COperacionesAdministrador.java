package com.geopista.server.database;
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
 * @author SATEC
 * @version $Revision: 1.20 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/05/04 07:41:51 $
 *          $Name:  $
 *          $RCSfile: COperacionesAdministrador.java,v $
 *          $Revision: 1.20 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import javax.measure.unit.SI;

import org.postgis.PGgeometry;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.EntidadMunicipio;

import com.geopista.protocol.administrador.DatosCapa;
import com.geopista.protocol.administrador.DetallesOperacion;
import com.geopista.protocol.administrador.Operacion;
import com.geopista.protocol.administrador.Permiso;
import com.geopista.protocol.administrador.Rol;
import com.geopista.protocol.administrador.SegPassword;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.control.ISesion;
import com.geopista.util.LCGIII_GeopistaUtil;
import com.vividsolutions.jts.geom.Geometry;

public class COperacionesAdministrador{

	public final static int INTENTOS_MINIMOS = 5;
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesAdministrador.class);

    public static CResultadoOperacion ejecutarActualizarRol(Rol rol, String sIdUser, String sIdEntidad)
      {
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          CResultadoOperacion resultado;
          try
          {
                 logger.debug("Inicio actualizar rol:"+rol.getNombre());
                 connection = CPoolDatabase.getConnection();
                 if (connection == null)
                 {
                     logger.warn("No se puede obtener la conexión");
                     return new CResultadoOperacion(false, "No se puede obtener la conexión");
                 }
                 connection.setAutoCommit(false);
                 
                 /*Comprobación si el rol es de sistema*/
                /* ResultSet rsSQL;
                 preparedStatement = connection.prepareStatement("SELECT * FROM IUSERGROUPHDR WHERE id=? AND id_municipio=?");
                 preparedStatement.setString(1,rol.getId());
                 preparedStatement.setString(2,"0");
                 rsSQL = preparedStatement.executeQuery();
                 while (rsSQL.next()){
                	 sIdMunicipio = "0";
                	 }*/
                 preparedStatement = connection.prepareStatement("update IUSERGROUPHDR set name=?, remarks=?, updrid=? ,upddate=? where id=?");
                 preparedStatement.setString(1,rol.getNombre());
                 preparedStatement.setString(2,rol.getDescripcion());
                 //preparedStatement.setString(3,sIdUser);
                 preparedStatement.setLong(3,Long.parseLong(sIdUser)); 
                 Calendar auxCalendar= Calendar.getInstance();
                 preparedStatement.setTimestamp(4,new java.sql.Timestamp(auxCalendar.getTime().getTime()));
                 //preparedStatement.setString(5,rol.getId());
                 preparedStatement.setLong(5,Long.parseLong(rol.getId()));
                 int iResult = preparedStatement.executeUpdate();
                 if (iResult>0)
                 {
                     try{preparedStatement.close();} catch (Exception ex2) {}
                     preparedStatement = connection.prepareStatement("delete from r_group_perm where groupid=?");
                     preparedStatement.setString(1,rol.getId());
                     preparedStatement.execute();

                     for (Enumeration e=rol.getPermisos().gethPermisos().elements();e.hasMoreElements();)
                     {
                         Permiso auxPermiso= (Permiso)e.nextElement();
                         try{preparedStatement.close();} catch (Exception ex2) {}
                         preparedStatement = connection.prepareStatement("insert into r_group_perm (groupid,idperm,idacl)values(?,?,?)");
                         //preparedStatement.setString(1,rol.getId());
                         preparedStatement.setLong(1,Long.parseLong(rol.getId()));
                         //preparedStatement.setString(2,auxPermiso.getIdPerm());
                         preparedStatement.setLong(2,Long.parseLong(auxPermiso.getIdPerm()));
                         //preparedStatement.setString(3,auxPermiso.getIdAcl());
                         preparedStatement.setLong(3,Long.parseLong(auxPermiso.getIdAcl()));
                         preparedStatement.execute();
                     }
                     connection.commit();
                     resultado=new CResultadoOperacion(true, rol.getId());
                 }
                 else
                 {
                	 
                     resultado=new CResultadoOperacion(false, "Rol "+rol.getNombre()+ " no existe. Recargue datos");
                 }
          }catch(Exception e)
          {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al actualizar el rol:"+sw.toString());
              resultado=new CResultadoOperacion(false, e.getMessage());
              try {connection.rollback();} catch (Exception ex2) {}
          }finally{
              try{preparedStatement.close();} catch (Exception ex2) {}
              try{connection.close(); CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
          }
         return resultado;
     }
    
    public static SegPassword getIntentosUsuario(String usuario)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CResultadoOperacion resultado;
        ResultSet rsSQL=null;
        SegPassword segPassword = new SegPassword();
        segPassword.setIntentos(INTENTOS_MINIMOS);
        try
        {
               logger.debug("Inicio getIntentosUsuario");
               connection = CPoolDatabase.getConnection();
               if (connection == null)
               {
                   logger.warn("No se puede obtener la conexión");
                 //  return new CResultadoOperacion(false, "No se puede obtener la conexión");
                   return segPassword;
               }
               connection.setAutoCommit(false);
               
               /*Comprobación si el rol es de sistema*/
              /* ResultSet rsSQL;
               preparedStatement = connection.prepareStatement("SELECT * FROM IUSERGROUPHDR WHERE id=? AND id_municipio=?");
               preparedStatement.setString(1,rol.getId());
               preparedStatement.setString(2,"0");
               rsSQL = preparedStatement.executeQuery();
               while (rsSQL.next()){
              	 sIdMunicipio = "0";
              	 }*/

               preparedStatement = connection.prepareStatement("select intentos, bloqueado, intentos_reiterados from entidad_supramunicipal, iuseruserhdr where iuseruserhdr.id_entidad = entidad_supramunicipal.id_entidad and iuseruserhdr.\"name\" = ?");
               preparedStatement.setString(1,usuario.toUpperCase());
               rsSQL=preparedStatement.executeQuery();
               if (rsSQL.next())
               {
            	   
            	   segPassword.setIntentos(rsSQL.getInt("intentos"));
            	   segPassword.setBloqueado(rsSQL.getBoolean("bloqueado"));
            	   segPassword.setIntentos_reiterados(rsSQL.getInt("intentos_reiterados"));
            	   segPassword.setUsuarioNoExiste(false);
                   return segPassword;
               }
               else{
            	// Si no encuentra el usuario, puede ser que sea un superusuario: id_entidad = 0, lo comprobamos
                   preparedStatement = connection.prepareStatement("select bloqueado, intentos_reiterados from iuseruserhdr where iuseruserhdr.\"name\" = ? and id_entidad = 0");
                   preparedStatement.setString(1,usuario.toUpperCase());
                   rsSQL=preparedStatement.executeQuery();
                   if (rsSQL.next())
                   {
                	   
                	   segPassword.setIntentos(INTENTOS_MINIMOS);
                	   segPassword.setBloqueado(rsSQL.getBoolean("bloqueado"));
                	   segPassword.setIntentos_reiterados(rsSQL.getInt("intentos_reiterados"));
                	   segPassword.setUsuarioNoExiste(false);
                       return segPassword;
                   }
                   else // El usuario no existe en la BBDD
                	   segPassword.setIntentos(INTENTOS_MINIMOS);
            	   segPassword.setBloqueado(false);
            	   segPassword.setIntentos_reiterados(0);
            	   segPassword.setUsuarioNoExiste(true);
                   return segPassword;
               }
        }catch(Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al actualizar el rol:"+sw.toString());
            resultado=new CResultadoOperacion(false, e.getMessage());
            try {connection.rollback();} catch (Exception ex2) {}
        }finally{
            try{preparedStatement.close();} catch (Exception ex2) {}
            try{connection.close(); CPoolDatabase.releaseConexion();rsSQL.close();} catch (Exception ex2) {}

        }
       return segPassword;
   }
    
    public static void bloqueaUsuario(String user) throws Exception{

    	Connection conn = CPoolDatabase.getConnection();

        PreparedStatement ps = conn.prepareStatement("update iuseruserhdr set bloqueado = true, intentos_reiterados = 0 where \"name\" = ?");

        try {             
            ps.setString(1, user.toUpperCase());
            ps.executeUpdate();


       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR en bloqueaUsuario :"+sw.toString());
            throw e;

        }finally{
        	try{ps.close();} catch (Exception e) {}
            try{conn.close(); CPoolDatabase.releaseConexion();} catch (Exception e) {}
        }
    }
    
    public static void reiniciaIntentos(String user) throws Exception{

    	Connection conn = CPoolDatabase.getConnection();

        PreparedStatement ps = conn.prepareStatement("update iuseruserhdr set intentos_reiterados = 0 where \"name\" = ?");

        try {             
            ps.setString(1, user.toUpperCase());
            ps.executeUpdate();


       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR en reiniciaIntentos: "+sw.toString());
            throw e;

        }finally{
        	try{ps.close();} catch (Exception e) {}
            try{conn.close(); CPoolDatabase.releaseConexion();} catch (Exception e) {}
        }
    }
    
    public static void actualizaIntentos(String user) throws Exception{
    	if (user != null){
	    	Connection conn = CPoolDatabase.getConnection();
	
	        PreparedStatement ps = conn.prepareStatement("update iuseruserhdr set intentos_reiterados = intentos_reiterados+1 where \"name\" = ?");
	
	        try {             
	            ps.setString(1, user.toUpperCase());
	            ps.executeUpdate();
	
	
	       } catch (Exception e) {
	            java.io.StringWriter sw=new java.io.StringWriter();
			    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
		    	e.printStackTrace(pw);
			    logger.error("ERROR en actualizaIntentos: "+sw.toString());
	            throw e;
	
	        }finally{
	        	try{ps.close();} catch (Exception e) {}
	            try{conn.close(); CPoolDatabase.releaseConexion();} catch (Exception e) {}
	        }
    	}
    }
    
    public static CResultadoOperacion ejecutarDeleteRol(Rol rol, String sIdEntidad)
      {
          Connection connection = null;
          PreparedStatement preparedStatement = null;
          ResultSet rsSQL=null;
          CResultadoOperacion resultado;
          try
          {
                 logger.debug("Inicio borrar rol:"+rol.getNombre());
                 connection = CPoolDatabase.getConnection();
                 if (connection == null)
                 {
                     logger.warn("No se puede obtener la conexión");
                     return new CResultadoOperacion(false, "No se puede obtener la conexión");
                 }
                 connection.setAutoCommit(false);
                 preparedStatement = connection.prepareStatement("select * from iusergroupuser where groupid=?");
                 preparedStatement.setString(1,rol.getId());
                 rsSQL=preparedStatement.executeQuery();
                 if (rsSQL.next())
                 {
                     resultado=new CResultadoOperacion(false, "Rol "+rol.getNombre()+ " tiene usuarios asociados. No se puede eliminar");
                 }
                 else
                 {
                      try{preparedStatement.close();} catch (Exception ex2) {}
                      preparedStatement = connection.prepareStatement("select * from IUSERGROUPHDR where id=? and (id_entidad=? OR id_entidad = 0)");
                      preparedStatement.setString(1,rol.getId());
                      preparedStatement.setString(2,sIdEntidad);
                      rsSQL=preparedStatement.executeQuery();
                      
                      if (!rsSQL.next())
                      {
                    	  
                          resultado=new CResultadoOperacion(false, "Rol "+rol.getNombre()+ " no existe. Recargue la aplicacion");
                      }
                      else
                      {
                    	  if( rsSQL.getInt("id_entidad") == 0){
                    		  resultado=new CResultadoOperacion(false, "Rol "+rol.getNombre()+ " es un rol de sistema. No se puede borrar.");
                    	  }else{
                            try{preparedStatement.close();} catch (Exception ex2) {}
                            preparedStatement = connection.prepareStatement("delete from r_group_perm where groupid=?");
                            preparedStatement.setString(1,rol.getId());
                            preparedStatement.execute();

                            try{preparedStatement.close();} catch (Exception ex2) {}
                            preparedStatement = connection.prepareStatement("delete from IUSERGROUPHDR where id=?");
                            preparedStatement.setString(1,rol.getId());
                            preparedStatement.execute();
                            connection.commit();
                            resultado=new CResultadoOperacion(true, "Rol borrado con exito "+rol.getNombre());
                            
                    	  }
                    }
                 }
          }catch(Exception e)
          {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("ERROR al borrar el rol:"+sw.toString());
              resultado=new CResultadoOperacion(false, e.getMessage());
              try {connection.rollback();} catch (Exception ex2) {}
          }finally{
              try{rsSQL.close();} catch (Exception ex2) {}
              try{preparedStatement.close();} catch (Exception ex2) {}
              try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
          }
         return resultado;
    }
    public static CResultadoOperacion ejecutarNewRol(Rol rol, String sIdUser, String sIdEntidad)
    {
         Connection connection = null;
         PreparedStatement preparedStatement = null;
         ResultSet rsSQL=null;
         CResultadoOperacion resultado;
         try
         {
             logger.debug("Inicio añadir rol:"+rol.getNombre());
             connection = CPoolDatabase.getConnection();
             if (connection == null)
             {
                    logger.warn("No se puede obtener la conexión");
                     return new CResultadoOperacion(false, "No se puede obtener la conexión");
             }
             connection.setAutoCommit(false);
             //long lIdRol=COperacionesDatabase.getTableSequence();
             long lIdRol=CPoolDatabase.getNextValue("IUSERGROUPHDR","id");
             preparedStatement = connection.prepareStatement("insert into IUSERGROUPHDR (id,name,remarks,crtrid, crtndate,mgrid,\"type\",id_entidad) values (?,?, ?, ?,?,?,0,?)");

             preparedStatement.setLong(1,lIdRol);
             preparedStatement.setString(2,rol.getNombre());
             preparedStatement.setString(3,rol.getDescripcion());
             if (sIdUser != null)
            	 preparedStatement.setLong(4,Long.parseLong(sIdUser));
             else
            	 preparedStatement.setNull(4,java.sql.Types.NUMERIC);

             Calendar auxCalendar=Calendar.getInstance();
             preparedStatement.setTimestamp(5,new java.sql.Timestamp(auxCalendar.getTime().getTime()));
             if (sIdUser != null)
            	 preparedStatement.setLong(6,Long.parseLong(sIdUser));
             else
            	 preparedStatement.setNull(6,java.sql.Types.NUMERIC);
             if (sIdEntidad != null)
            	 preparedStatement.setLong(7,Long.parseLong(sIdEntidad));
             else
            	 preparedStatement.setNull(7,java.sql.Types.NUMERIC);
             preparedStatement.execute();

             for (Enumeration e=rol.getPermisos().gethPermisos().elements();e.hasMoreElements();)
             {
                   Permiso auxPermiso= (Permiso)e.nextElement();
                   try{preparedStatement.close();} catch (Exception ex2) {}
                   preparedStatement = connection.prepareStatement("insert into r_group_perm (groupid,idperm,idacl)values(?,?,?)");
                   preparedStatement.setLong(1,lIdRol);
                   preparedStatement.setLong(2,Long.parseLong(auxPermiso.getIdPerm()));
                   preparedStatement.setLong(3,Long.parseLong(auxPermiso.getIdAcl()));
                   preparedStatement.execute();
             }
             connection.commit();
             logger.debug("Rol con id: "+lIdRol+" creado con exito");
             resultado=new CResultadoOperacion(true, new Long(lIdRol).toString());

       }catch(Exception e)
       {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al CREAR el rol:"+sw.toString());
            resultado=new CResultadoOperacion(false, e.getMessage());
            try {connection.rollback();} catch (Exception ex2) {}
       }finally{
                 try{rsSQL.close();} catch (Exception ex2) {}
                 try{preparedStatement.close();} catch (Exception ex2) {}
                 try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
             }
            return resultado;
        }


    public static CResultadoOperacion ejecutarActualizarUser(Usuario user, String sIdUserActualizador, String sIdMunicipio)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CResultadoOperacion resultado;
        try
        {
            logger.debug("Inicio actualizar usuario: "+user.getName());
            connection = CPoolDatabase.getConnection();
            if (connection == null)
            {
                logger.warn("Actualizar Usuario: No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            
            String sql = null;
            boolean mismoUsuario = false;
            
            if (sIdUserActualizador.equals(user.getId())){
            	mismoUsuario = true;
            }
            if (user.isPasswordCambiado() && !mismoUsuario) 
            	sql = "update IUSERUSERHDR set name=?, nombrecompleto=?, password=?, remarks=?, mail=?, deptid=?, updrid=?,upddate=?, nif=?, id_entidad=?, bloqueado=?, fecha_proxima_modificacion = current_date - 1, intentos_reiterados = 0 where id=?";
            else{
            	if ((user.isPasswordCambiado() && mismoUsuario)){
            		//BUG. PG8.4. Casting de tipos
                	sql = "update IUSERUSERHDR set name=?, nombrecompleto=?, password=?, remarks=?, mail=?, deptid=?, updrid=?,upddate=?, nif=?, id_entidad=?, bloqueado=?, fecha_proxima_modificacion = current_date + (select cast (periodicidad as integer) from entidad_supramunicipal where id_entidad = ? ), intentos_reiterados = 0 where id=?";            		
            	}
            	else{
            		sql = "update IUSERUSERHDR set name=?, nombrecompleto=?, password=?, remarks=?, mail=?, deptid=?, updrid=?,upddate=?, nif=?, id_entidad=?, bloqueado=?, intentos_reiterados = 0 where id=?";
            	}
            }
            	
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getName().toUpperCase());
            logger.debug("Nombre: "+user.getName());
            preparedStatement.setString(2,user.getNombreCompleto());
            logger.debug("Nombre completo: "+user.getNombreCompleto());
            preparedStatement.setString(3,user.getPassword());
            logger.debug("Password: "+user.getPassword());
            preparedStatement.setString(4,user.getDescripcion());
            logger.debug("Descripcion: "+user.getDescripcion());
            preparedStatement.setString(5,user.getEmail());
            logger.debug("mail: "+user.getEmail());
            
            
          //BUG. PG8.4. Casting de tipos
            if (user.getDepID()!=null)
            	preparedStatement.setInt(6,Integer.parseInt(user.getDepID(),10));
            else
            	preparedStatement.setNull(6,java.sql.Types.NUMERIC);
            //preparedStatement.setString(6,user.getDepID());
            
            logger.debug("DepId: "+user.getDepID());
            
          //BUG. PG8.4. Casting de tipos
            if (sIdUserActualizador!=null)
            	preparedStatement.setInt(7,Integer.parseInt(sIdUserActualizador,10));
            else
            	preparedStatement.setNull(7,java.sql.Types.NUMERIC);
            //preparedStatement.setString(7,sIdUserActualizador);
            
            
            logger.debug("Actualizador: "+sIdUserActualizador);
            Calendar auxCalendar= Calendar.getInstance();
            preparedStatement.setDate(8,new java.sql.Date(auxCalendar.getTime().getTime()));
            logger.debug("nif: "+user.getNif());
            preparedStatement.setString(9,user.getNif());
            if (user.getId_entidad().equals(""))
            	user.setId_entidad("0");
            logger.debug("id_entidad: "+user.getId_entidad());
            preparedStatement.setInt(10,Integer.parseInt(user.getId_entidad()));
            preparedStatement.setBoolean(11, user.isBloqueado());
            if ((user.isPasswordCambiado() && mismoUsuario)){
            	preparedStatement.setInt(12,Integer.parseInt(user.getId_entidad()));
            	preparedStatement.setString(13,user.getId());
            }
            else
            	preparedStatement.setString(12,user.getId());
            
            //preparedStatement.setString(10,sIdMunicipio);
            logger.debug("ID: "+user.getId());

            int iResult = preparedStatement.executeUpdate();
            if (iResult<1)
            {
                resultado=new CResultadoOperacion(false, "Usuario "+user.getName()+ " no existe. Recargue datos");
            }
            else
            {
                //borramos los permisos
                try{preparedStatement.close();} catch (Exception ex2) {}
                preparedStatement = connection.prepareStatement("delete from r_usr_perm where userid=?");
                preparedStatement.setString(1,user.getId());
                preparedStatement.execute();
                for (Enumeration e=user.getPermisos().gethPermisos().elements();e.hasMoreElements();)
                {
                    Permiso auxPermiso= (Permiso)e.nextElement();
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    preparedStatement = connection.prepareStatement("insert into r_usr_perm (userid,idperm,idacl,aplica)values(?,?,?,?)");
                    preparedStatement.setLong(1,Long.parseLong(user.getId()));
                    preparedStatement.setLong(2,Long.parseLong(auxPermiso.getIdPerm()));
                    preparedStatement.setLong(3,Long.parseLong(auxPermiso.getIdAcl()));
                    preparedStatement.setInt(4,(auxPermiso.getAplica()?1:0));
                    preparedStatement.execute();
                }
                //Borramos los grupos
                try{preparedStatement.close();} catch (Exception ex2) {}
                preparedStatement = connection.prepareStatement("delete from iusergroupuser where userid=?");
                preparedStatement.setString(1,user.getId());
                preparedStatement.execute();
                for (Enumeration e=user.getGrupos().elements();e.hasMoreElements();)
                {
                    String grupo= (String)e.nextElement();
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    preparedStatement = connection.prepareStatement("insert into iusergroupuser (userid,groupid)values(?,?)");
                    
                  //BUG. PG8.4. Casting de tipos
                    if (user.getId()!=null)
                		preparedStatement.setInt(1,Integer.parseInt(user.getId(),10));
                    else
                    	preparedStatement.setNull(1,java.sql.Types.NUMERIC);
                    //preparedStatement.setString(1,user.getId());
                    
                    if (grupo!=null)
                		preparedStatement.setInt(2,Integer.parseInt(grupo,10));
                    else
                    	preparedStatement.setNull(2,java.sql.Types.NUMERIC);
                    //preparedStatement.setString(2,grupo);
                    //preparedStatement.setString(2,grupo);
                    preparedStatement.execute();
                }
                connection.commit();
                resultado=new CResultadoOperacion(true, "Usuario actualizado con exito "+user.getName());
            }
             }catch(Exception e)
             {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al actualizar el usuario:"+sw.toString());
                 resultado=new CResultadoOperacion(false, e.getMessage());
                 try {connection.rollback();} catch (Exception ex2) {}
             }finally{
                 try{preparedStatement.close();} catch (Exception ex2) {}
                 try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
             }
            return resultado;
        }
        public static CResultadoOperacion ejecutarNewUser(Usuario user, String sIdUserActualizador,String sIdMunicipio)
       {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL=null;
            CResultadoOperacion resultado;
            try
            {
                logger.debug("Inicio añadir usuario:"+user.getName());
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                       logger.warn("No se puede obtener la conexión");
                        return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                connection.setAutoCommit(false);
                //long lIdUsuario=COperacionesDatabase.getTableSequence();
                preparedStatement = connection.prepareStatement("select * from iuseruserhdr where upper(name)=upper(?)");
                preparedStatement.setString(1,user.getName());
                rsSQL=preparedStatement.executeQuery();
                if (rsSQL.next())
                {
                    resultado=new CResultadoOperacion(false, "El usuario "+user.getName()+ " ya existe.");
                }
                else
                {
	                long lIdUsuario=CPoolDatabase.getNextValue("IUSERUSERHDR","id");
	                preparedStatement = connection.prepareStatement("insert into IUSERUSERHDR (id,name,nombrecompleto," +
	                        " password, remarks,mail,deptid,borrado,crtrid,crtndate,flags,stat,numbadcnts,nif,id_entidad, fecha_proxima_modificacion, bloqueado, intentos_reiterados)\n" +
	                        "values (?,?,?,?,?,?,?,0,?,?,0,0,0,?,?, current_date-1, false, 0)" );
	
	                preparedStatement.setLong(1,lIdUsuario);
	                preparedStatement.setString(2,user.getName().toUpperCase());
	                preparedStatement.setString(3,user.getNombreCompleto());
	                preparedStatement.setString(4,user.getPassword());
	                preparedStatement.setString(5,user.getDescripcion());
	                preparedStatement.setString(6,user.getEmail());
	                
	              //BUG. PG8.4. Casting de tipos
	                if (user.getDepID()!=null)
	                	preparedStatement.setInt(7,Integer.parseInt(user.getDepID(),10));
	                else
	                	preparedStatement.setNull(7,java.sql.Types.NUMERIC);	                
	                //preparedStatement.setString(7,user.getDepID());
	                
	              //BUG. PG8.4. Casting de tipos
	                if (sIdUserActualizador!=null)
	                	preparedStatement.setInt(8,Integer.parseInt(sIdUserActualizador,10));
	                else
	                	preparedStatement.setNull(8,java.sql.Types.NUMERIC);	                
	                //preparedStatement.setString(8,sIdUserActualizador);
	                
	                
	                Calendar auxCalendar= Calendar.getInstance();
	                preparedStatement.setDate(9,new java.sql.Date(auxCalendar.getTime().getTime()));
	                preparedStatement.setString(10,user.getNif());
	                if (user.getId_entidad() != null)
	                	preparedStatement.setLong(11,Long.parseLong(user.getId_entidad()));
	                else
	                	preparedStatement.setNull(11, java.sql.Types.NUMERIC);
	                preparedStatement.execute();
	
	                for (Enumeration e=user.getPermisos().gethPermisos().elements();e.hasMoreElements();)
	                {
	                     Permiso auxPermiso= (Permiso)e.nextElement();
	                     try{preparedStatement.close();} catch (Exception ex2) {}
	                     preparedStatement = connection.prepareStatement("insert into r_usr_perm (userid,idperm,idacl,aplica)values(?,?,?,?)");
	                     preparedStatement.setLong(1,lIdUsuario);
	                     preparedStatement.setLong(2,Long.parseLong(auxPermiso.getIdPerm()));
	                     preparedStatement.setLong(3,Long.parseLong(auxPermiso.getIdAcl()));
	                     preparedStatement.setInt(4,(auxPermiso.getAplica()?1:0));
	                     preparedStatement.execute();
	               }
	               for (Enumeration e=user.getGrupos().elements();e.hasMoreElements();)
	               {
	                    String grupo= (String)e.nextElement();
	                    try{preparedStatement.close();} catch (Exception ex2) {}
	                    preparedStatement = connection.prepareStatement("insert into iusergroupuser (userid,groupid)values(?,?)");
	                    preparedStatement.setLong(1,lIdUsuario);
	                    //preparedStatement.setString(2,grupo);
	                    preparedStatement.setLong(2,Long.parseLong(grupo));
	                    preparedStatement.execute();
	               }
	               connection.commit();
	               logger.debug("Usuario "+user.getName()+" con id: "+lIdUsuario+" creado con exito");
	               resultado=new CResultadoOperacion(true, new Long(lIdUsuario).toString());
                } 
          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al CREAR el rol:"+sw.toString());
               resultado=new CResultadoOperacion(false, e.getMessage());
               try {connection.rollback();} catch (Exception ex2) {}
          }finally{
               try{rsSQL.close();} catch (Exception ex2) {}
               try{preparedStatement.close();} catch (Exception ex2) {}
               try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
          }
          return resultado;
        }
        
        public static CResultadoOperacion ejecutarDeleteUser(Usuario user, String sIdMunicipio)
        {
              Connection connection = null;
              PreparedStatement preparedStatement = null;
              ResultSet rsSQL=null;
              CResultadoOperacion resultado;
              try
              {
                     logger.debug("Inicio borrar usuario: "+user.getName());
                     connection = CPoolDatabase.getConnection();
                     if (connection == null)
                     {
                         logger.warn("No se puede obtener la conexión");
                         return new CResultadoOperacion(false, "No se puede obtener la conexión");
                     }

                     connection.setAutoCommit(false);

                     preparedStatement = connection.prepareStatement("select id from iUserUserHdr where id=?");
                     preparedStatement.setString(1, user.getId());
                     ResultSet rs=preparedStatement.executeQuery();
                     if (!rs.next())
                     {
                         resultado=new CResultadoOperacion(false, "Usuario "+user.getName()+" no encontrado. Recargue la aplicación");
                     }
                     else
                     {
                         try{preparedStatement.close();} catch (Exception ex2) {}
                          preparedStatement = connection.prepareStatement("delete from iusergroupuser where userid=?");
                         preparedStatement.setString(1, user.getId());
                         preparedStatement.execute();

                         try{preparedStatement.close();} catch (Exception ex2) {}
                         preparedStatement = connection.prepareStatement("delete from r_usr_perm where userid=?");
                         preparedStatement.setString(1,user.getId());
                         preparedStatement.execute();
                         Calendar cal=Calendar.getInstance();
                         try{preparedStatement.close();} catch (Exception ex2) {}
                         preparedStatement = connection.prepareStatement("update iuseruserhdr set borrado=1 , name=? where id=?");
                         preparedStatement.setString(1,new Long(cal.getTime().getTime()).toString());
                         preparedStatement.setString(2,user.getId());

                         preparedStatement.execute();
                         resultado=new CResultadoOperacion(true, "Usuario borrado con exito "+user.getName());
                         connection.commit();
                     }

              }catch(Exception e)
              {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("ERROR al borrar el usuario:"+sw.toString());
                  resultado=new CResultadoOperacion(false, e.getMessage());
                  try {connection.rollback();} catch (Exception ex2) {}
              }finally{
                  try{rsSQL.close();} catch (Exception ex2) {}
                  try{preparedStatement.close();} catch (Exception ex2) {}
                  try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
              }
             return resultado;
        }
       public static CResultadoOperacion ejecutarNewDomainNode(DomainNode nodo)
       {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL=null;
            CResultadoOperacion resultado;
            try
            {
                logger.debug("Inicio añadir nodo:"+nodo.getFirstTerm());
                connection = CPoolDatabase.getConnection();
                if (connection == null)
                {
                   logger.error("No se puede obtener la conexión");
                   return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
                connection.setAutoCommit(false);
                //long lIdDomainNode=COperacionesDatabase.getTableSequence();
                long lIdDomainNode=CPoolDatabase.getNextValue("DOMAINNODES","ID");
                nodo.setIdNode(new Long(lIdDomainNode).toString());
                //long lIdDomainDescripcion=COperacionesDatabase.getTableSequence();
                long lIdDomainDescripcion=CPoolDatabase.getNextValue("DICTIONARY","ID_VOCABLO");
                nodo.setIdDes(new Long(lIdDomainDescripcion).toString());

                for (Enumeration e=nodo.gethDict().keys();e.hasMoreElements();)
                {
                    String sLocale=(String)e.nextElement();
                    String sTerm=(String)nodo.gethDict().get(sLocale);
                    preparedStatement = connection.prepareStatement("insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(?,?,?)");
                    preparedStatement.setLong(1,lIdDomainDescripcion);
                    preparedStatement.setString(2,sLocale);
                    preparedStatement.setString(3,sTerm);
                    preparedStatement.execute();
                    
                    LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                    
                    try{preparedStatement.close();} catch (Exception ex2) {}
              }
                 preparedStatement = connection.prepareStatement("insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, \"type\",id_municipio, parentdomain)\n" +
                        "  values(?,?,?,?,?,?,?)");

                preparedStatement.setLong(1,lIdDomainNode);
                //BUG. PG8.4. Casting de tipos
                //preparedStatement.setString(2,nodo.getIdDomain());
                if (nodo.getIdDomain()!=null)
                	preparedStatement.setLong(2,Long.parseLong(nodo.getIdDomain()));
                else
                    preparedStatement.setNull(2,java.sql.Types.NUMERIC);
                
                preparedStatement.setString(3,nodo.getPatron());
                preparedStatement.setLong(4,lIdDomainDescripcion);
                preparedStatement.setInt(5,nodo.getType());
                //BUG. PG8.4. Casting de tipos
                //preparedStatement.setString(6,nodo.getIdMuni());
                if (nodo.getIdMuni()!=null)
                	preparedStatement.setLong(6,Long.parseLong(nodo.getIdMuni()));
                else
                	preparedStatement.setNull(6,java.sql.Types.NUMERIC);
                //BUG. PG8.4. Casting de tipos
                //preparedStatement.setString(7,nodo.getIdParent());
                if (nodo.getIdParent()!=null)
                	preparedStatement.setLong(7,Long.parseLong(nodo.getIdParent()));
                else
                	preparedStatement.setNull(7,java.sql.Types.NUMERIC);
                preparedStatement.execute();
                
                LCGIII_GeopistaUtil.generarSQL(preparedStatement);

                connection.commit();
                logger.debug("Nuevo Domain con id: "+lIdDomainNode+" creado con exito");
                resultado=new CResultadoOperacion(true, new Long(lIdDomainNode).toString());

          }catch(Exception e)
          {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
               resultado=new CResultadoOperacion(false, e.getMessage());
               try {connection.rollback();} catch (Exception ex2) {}
          }finally{
                    try{rsSQL.close();} catch (Exception ex2) {}
                    try{preparedStatement.close();} catch (Exception ex2) {}
                    try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                }
               return resultado;
           }

        public static CResultadoOperacion ejecutarNewDomain(Domain nodo)
           {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet rsSQL=null;
                CResultadoOperacion resultado = null;
                try
                {
                    logger.debug("Inicio añadir nodo:"+nodo.getName());
                    connection = CPoolDatabase.getConnection();
                    if (connection == null)
                    {
                           logger.warn("No se puede obtener la conexión");
                            return new CResultadoOperacion(false, "No se puede obtener la conexión");
                    }
                    boolean insertado=false;
                    do
                    {
                        connection.setAutoCommit(true);
                        try
                        {
                            //long lIdDomainNode=COperacionesDatabase.getTableSequence();
                            long lIdDomain=CPoolDatabase.getNextValue("DOMAINS","ID");
                            nodo.setIdDomain(new Long(lIdDomain).toString());

                            preparedStatement = connection.prepareStatement("insert into Domains (ID, NAME,ID_CATEGORY)values(?,?,?)");
                            preparedStatement.setLong(1,lIdDomain);
                            preparedStatement.setString(2,nodo.getName());
                             //BUG. PG8.4. Casting de tipos
                            //preparedStatement.setString(3,nodo.getIdCategory());
                            preparedStatement.setLong(3,Long.parseLong(nodo.getIdCategory()));
                            preparedStatement.execute();
                            
                            LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                            
                            try{preparedStatement.close();} catch (Exception ex2) {}
                            insertado=true;
                            logger.debug("New Domain con id: "+lIdDomain+" creado con exito");
                            resultado=new CResultadoOperacion(true, new Long(lIdDomain).toString());
                        }catch(Exception e)
                        {
                            if (e.getMessage()==null || e.getMessage().toLowerCase().indexOf("duplicate key")<0)
                                throw(e);
                            Thread.sleep(100);
                        }
                     }while (!insertado);
                     connection.commit();
              }catch(Exception e)
              {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
                   resultado=new CResultadoOperacion(false, e.getMessage());
                   try {connection.rollback();} catch (Exception ex2) {
                	   e.printStackTrace();
                	   }
              }finally{
                        try{rsSQL.close();} catch (Exception ex2) {}
                        try{preparedStatement.close();} catch (Exception ex2) {}
                        try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                    }
             return resultado;
        }

        public static CResultadoOperacion ejecutarDeleteDomainNode(DomainNode nodo)
        {
              Connection connection = null;
              PreparedStatement preparedStatement = null;
              ResultSet rsSQL=null;
              CResultadoOperacion resultado=null;
              try
              {
                     logger.debug("Inicio borrar dominio: "+nodo.getFirstTerm());
                     connection = CPoolDatabase.getConnection();
                     if (connection == null)
                     {
                         logger.warn("No se puede obtener la conexión");
                         return new CResultadoOperacion(false, "No se puede obtener la conexión");
                     }

                     connection.setAutoCommit(false);
                     try
                     {
                        //Primero miramos si se esta utilizando en alguna tabla
                        preparedStatement = connection.prepareStatement("select t.name as tabla, c.name as columna from columns as c,tables as t,domainnodes as d \n" +
                        "                  where d.id=? and c.id_table=t.id_table and c.id_domain=d.id_domain;");
                        preparedStatement.setString(1, nodo.getIdNode());
                        rsSQL=preparedStatement.executeQuery();
                        while (rsSQL.next())
                        {
                            String tabla=rsSQL.getString("tabla");
                            String columna=rsSQL.getString("columna");
                            PreparedStatement preparedStatement2 = connection.prepareStatement("select "+columna+ " from "+ tabla + " where "
                                                                + columna +"=?");
                            preparedStatement2.setString(1, nodo.getPatron());
                            rsSQL=preparedStatement2.executeQuery();
                            if (rsSQL.next())
                            {
                                if (nodo.getForceDelete())
                                	logger.warn("El nodo con patron:"+nodo.getPatron()+" esta siendo utilizado pero lo borramos");
                                else	
                                	resultado=new CResultadoOperacion(false, "El dominio esta siendo utilizado en el sistema. No se puede borrar");                                		
                            }
                            try{preparedStatement2.close();} catch (Exception ex2) {}

                        }
                     }catch (Exception e) {}
                     if (resultado==null)
                     {
                        try{preparedStatement.close();} catch (Exception ex2) {}
                        preparedStatement = connection.prepareStatement("delete from dictionary where id_vocablo=?");
                        preparedStatement.setString(1, nodo.getIdDes());
                        preparedStatement.execute();
                        
                        LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                        
                        try{preparedStatement.close();} catch (Exception ex2) {}
                        preparedStatement = connection.prepareStatement("delete from DomainNodes where id=?");
                        preparedStatement.setString(1,nodo.getIdNode());
                        preparedStatement.execute();
                        
                        LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                        
                        resultado=new CResultadoOperacion(true, "DomainNode borrado con exito:"+nodo.getFirstTerm());
                     }
                     connection.commit();

              }catch(Exception e)
              {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("DeleteDomainNode.ERROR al borrar el dominio:",e);
                  logger.error("DeleteDomainNode.ERROR al borrar el dominio:"+sw.toString());
                  String error="";
                  if (e.toString().toUpperCase().indexOf("FOREIGN")>=0)
                      error="El dominio esta siendo utilizado en el sistema. No se puede borrar";
                  else
                      error=e.getMessage();
                  resultado=new CResultadoOperacion(false, error);
                  try {connection.rollback();} catch (Exception ex2) {}
              }finally{
                  try{rsSQL.close();} catch (Exception ex2) {}
                  try{preparedStatement.close();} catch (Exception ex2) {}
                  try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
              }
             return resultado;
        }
        public static CResultadoOperacion ejecutarDeleteDomain(Domain nodo)
        {
              Connection connection = null;
              PreparedStatement preparedStatement = null;
              ResultSet rsSQL=null;
              CResultadoOperacion resultado;
              try
              {
                     logger.debug("Inicio borrar el dominio: "+nodo.getName());
                     connection = CPoolDatabase.getConnection();
                     if (connection == null)
                     {
                         logger.warn("No se puede obtener la conexión");
                         return new CResultadoOperacion(false, "No se puede obtener la conexión");
                     }

                     connection.setAutoCommit(false);

                     preparedStatement = connection.prepareStatement("delete from Domains where id=?");
                     preparedStatement.setString(1,nodo.getIdDomain());
                     preparedStatement.execute();
                     
                     LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                     
                     connection.commit();
                     resultado=new CResultadoOperacion(true, "Domain borrado con exito "+nodo.getName());

              }catch(Exception e)
              {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("DeleteDomain.ERROR al borrar el dominio",e);
                  logger.error("DeleteDomain.ERROR al borrar el dominio:"+sw.toString());
                  String error="";
                  if (e.toString().toUpperCase().indexOf("FOREIGN")>=0)
                      error="El dominio esta siendo utilizado en el sistema. No se puede borrar";
                  else
                      error=e.getMessage();
                  resultado=new CResultadoOperacion(false, error);
                  try {connection.rollback();} catch (Exception ex2) {}
              }finally{
                  try{rsSQL.close();} catch (Exception ex2) {}
                  try{preparedStatement.close();} catch (Exception ex2) {}
                  try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
              }
             return resultado;
        }

        public static CResultadoOperacion ejecutarUpdateDomainNode(DomainNode nodo)
         {
              Connection connection = null;
              PreparedStatement preparedStatement = null;
              ResultSet rsSQL=null;
              CResultadoOperacion resultado;
              try
              {
                  logger.debug("Inicio actualizar nodo:"+nodo.getFirstTerm());
                  connection = CPoolDatabase.getConnection();
                  if (connection == null)
                  {
                         logger.warn("No se puede obtener la conexión");
                          return new CResultadoOperacion(false, "No se puede obtener la conexión");
                  }
                  connection.setAutoCommit(false);

                  preparedStatement = connection.prepareStatement("select id_description from domainnodes where id=?");
                  preparedStatement.setString(1, nodo.getIdNode());
                  ResultSet rs=preparedStatement.executeQuery();
                  if (!rs.next())
                  {
                      resultado=new CResultadoOperacion(false, "No existe el dominio "+nodo.getFirstTerm()+". Recargue la aplicación");
                  }
                  else
                  {
                      String sIdDescripcion=rs.getString(1);
                      if ((sIdDescripcion==null) && (nodo.gethDict().size()>0))
                          sIdDescripcion= new Long(CPoolDatabase.getNextValue("DOMAINNODES","ID")).toString();
                    //Borramos el diccionario
                      try{preparedStatement.close();} catch (Exception ex2) {}
                      preparedStatement = connection.prepareStatement("delete from dictionary where id_vocablo=?");
                      preparedStatement.setString(1, sIdDescripcion);
                      preparedStatement.execute();
                      
                      LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                      
                      for (Enumeration e=nodo.gethDict().keys();e.hasMoreElements();)
                      {
                          String sLocale=(String)e.nextElement();
                          String sTerm=(String)nodo.gethDict().get(sLocale);
                          try{preparedStatement.close();} catch (Exception ex2) {}
                          preparedStatement = connection.prepareStatement("insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(?,?,?)");
                          preparedStatement.setInt(1,Integer.parseInt(sIdDescripcion,10));
                          preparedStatement.setString(2,sLocale);
                          preparedStatement.setString(3,sTerm);
                          preparedStatement.execute();
                          
                          LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                      }
                      try{preparedStatement.close();} catch (Exception ex2) {}
                      preparedStatement = connection.prepareStatement("update domainnodes set pattern=?, id_description=? where id=?");
                      preparedStatement.setString(1,nodo.getPatron());
                      preparedStatement.setInt(2,Integer.parseInt(sIdDescripcion,10));
                      preparedStatement.setInt(3,Integer.parseInt(nodo.getIdNode(),10));
                      preparedStatement.execute();
                      
                      LCGIII_GeopistaUtil.generarSQL(preparedStatement);
                      
                      connection.commit();
                      logger.debug("Rol con id: "+nodo.getIdNode()+" creado con exito");
                      resultado=new CResultadoOperacion(true, "Nodo Actualizado con exito");
                  }
            }catch(Exception e)
            {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("ERROR al CREAR el Domain Node:"+sw.toString());
                 resultado=new CResultadoOperacion(false, e.getMessage());
                 try {connection.rollback();} catch (Exception ex2) {}
            }finally{
                      try{rsSQL.close();} catch (Exception ex2) {}
                      try{preparedStatement.close();} catch (Exception ex2) {}
                      try{connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
                  }
                 return resultado;
             }

    public static CResultadoOperacion ejecutarNewEntidad(Entidad entidad) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado = null;
        try {
            logger.debug("Inicio añadir entidad: " + entidad.getNombre());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            long lIdEntidad = CPoolDatabase.getNextValue("ENTIDAD_SUPRAMUNICIPAL", "ID_ENTIDAD");
            preparedStatement = connection
                    .prepareStatement("insert into ENTIDAD_SUPRAMUNICIPAL (id_entidad, nombreoficial, srid, backup, aviso, periodicidad, intentos) values (?,?,?,?,?,?,?)");
            preparedStatement.setLong(1, lIdEntidad);
            preparedStatement.setString(2, entidad.getNombre());
            preparedStatement.setInt(3, Integer.parseInt(entidad.getSrid()));
            preparedStatement.setInt(4, entidad.isBackup()?1:0);
            preparedStatement.setInt(5, entidad.getAviso());
            preparedStatement.setInt(6, entidad.getPeriodicidad());
            preparedStatement.setInt(7, entidad.getIntentos());
            preparedStatement.execute();
            connection.commit();
                        
            //NUEVO
            insertUpdateEntidadExt(connection, String.valueOf(lIdEntidad), entidad.getEntidadExt());
            //FIN NUEVO
            
            logger.debug("Entidad con id: " + lIdEntidad + " creado con exito");
            resultado = new CResultadoOperacion(true, new Long(lIdEntidad).toString());

        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al CREAR la Entidad " + sw.toString());
            resultado = new CResultadoOperacion(false, e.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
            }
        } finally {
            try {
                rsSQL.close();
            } catch (Exception ex2) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            try {
                connection.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception ex2) {
            }
        }
        return resultado;

    }
    
    //NUEVO
    private static boolean insertUpdateEntidadExt(Connection connection, String idEntidad, String idEntidadExt){
    	if(connection != null && idEntidad != null && idEntidadExt != null){
	    	PreparedStatement preparedStatement;
	        try{        	
	            preparedStatement = connection.prepareStatement("INSERT INTO localgisguiaurbana.entidadlocalgis_entidadext (id_entidad, id_entidadext) VALUES (?, ?)");
	            preparedStatement.setInt(1, Integer.valueOf(idEntidad));
	            preparedStatement.setString(2, idEntidadExt);
	            preparedStatement.execute();
	            connection.commit();
	        }catch(Exception ex){
	        	try{
	        		 connection.rollback();
	            	 preparedStatement = connection.prepareStatement("UPDATE localgisguiaurbana.entidadlocalgis_entidadext SET id_entidadext=? WHERE id_entidad=?");
	            	 preparedStatement.setString(1, idEntidadExt);
	            	 preparedStatement.setInt(2, Integer.valueOf(idEntidad));
	            	 preparedStatement.execute();
	                 connection.commit();	                 
	        	}catch(Exception e){
	        		  logger.error("ERROR al modificar la entidad externa: " + e.getMessage());
	        	}
	        }
	        return true;
    	}
        return false;
    }
    //FIN NUEVO
    
    //NUEVO
    private static boolean deleteEntidadExt(Connection connection, Integer idEntidad){
    	if(connection != null && idEntidad != null){
	    	PreparedStatement preparedStatement;
	        try{        	
	            preparedStatement = connection.prepareStatement("DELETE FROM localgisguiaurbana.entidadlocalgis_entidadext WHERE id_entidad=?");
	            preparedStatement.setInt(1, idEntidad);
	            preparedStatement.execute();
	            connection.commit();
	            return true;
	        }catch(Exception ex){
	        	logger.error("ERROR al borrar la entidad externa: " + ex.getMessage());        	
	        }
	    }
        return false;
    }
    //FIN NUEVO
    
    public static CResultadoOperacion ejecutarDeleteEntidad(Entidad entidad) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado = null;
        try {
            logger.debug("Inicio eliminar entidad: " + entidad.getId());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("select * from entidades_municipios where id_entidad = ?");            
            preparedStatement.setString(1, entidad.getId());
            rsSQL=preparedStatement.executeQuery();
            if (rsSQL.next()) {
                try {
                    rsSQL.close();
                } catch (Exception ex2) {
                }
        	try {
                    preparedStatement.close();
                } catch (Exception ex2) {
                }
                preparedStatement = connection.prepareStatement("delete from ENTIDADES_MUNICIPIOS where id_entidad = ?");
                preparedStatement.setString(1, entidad.getId());
                preparedStatement.execute();
        
                LCGIII_GeopistaUtil.generarSQL(preparedStatement);              
            } 
                
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            preparedStatement = connection
                    .prepareStatement("delete from entidad_supramunicipal where id_entidad = ?");
            preparedStatement.setString(1, entidad.getId());
            preparedStatement.execute();
            connection.commit();
            
            //NUEVO
            deleteEntidadExt(connection, Integer.valueOf(entidad.getId()));
            //FIN NUEVO
            
            resultado = new CResultadoOperacion(true, "Entidad borrada con exito " + entidad.getId());
            
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al eliminar la Entidad " + sw.toString());
            resultado = new CResultadoOperacion(false, e.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
            }
        } finally {
            try {
                rsSQL.close();
            } catch (Exception ex2) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            try {
                connection.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception ex2) {
            }
        }
        return resultado;

    }
    
    public static CResultadoOperacion ejecutarUpdateEntidad(Entidad entidad) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado = null;
        try {
            logger.debug("Inicio actualizar entidad: " + entidad.getId());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("update entidad_supramunicipal set nombreoficial = ?, srid = ? , backup=?, aviso=?, periodicidad=?, intentos=? where id_entidad = ?");
            preparedStatement.setString(1, entidad.getNombre());
            preparedStatement.setInt(2, Integer.parseInt(entidad.getSrid()));
            preparedStatement.setInt(3, entidad.isBackup()?1:0);
            preparedStatement.setInt(4, entidad.getAviso());
            preparedStatement.setInt(5, entidad.getPeriodicidad());
            preparedStatement.setInt(6, entidad.getIntentos());
            preparedStatement.setString(7, entidad.getId());
            
            preparedStatement.execute();
            connection.commit();
            
            //NUEVO
            insertUpdateEntidadExt(connection, entidad.getId(), entidad.getEntidadExt());
            //FIN NUEVO
            
            logger.debug("Entidad con id: " + entidad.getId() + " modificada con exito");
            resultado = new CResultadoOperacion(true, entidad.getNombre());
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al modificar la Entidad " + sw.toString());
            resultado = new CResultadoOperacion(false, e.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
            }
        } finally {
            try {
                rsSQL.close();
            } catch (Exception ex2) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            try {
                connection.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception ex2) {
            }
        }
        return resultado;
    }
    
    public static CResultadoOperacion ejecutarNewEntidadMunicipio(EntidadMunicipio entidadMunicipio) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado = null;
        try {
            logger.debug("Inicio añadir entidad-municipio: " + entidadMunicipio.getId_municipio());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("insert into ENTIDADES_MUNICIPIOS (id_entidad, id_municipio) values (?,?)");
            preparedStatement.setInt(1, Integer.parseInt(entidadMunicipio.getId_entidad()));
            preparedStatement.setInt(2, Integer.parseInt(entidadMunicipio.getId_municipio()));
            preparedStatement.execute();
		
		    connection.commit();
            logger.debug("Relación Entidad con id: " + entidadMunicipio.getId_entidad()
                    + " y Municipio con Id: " + entidadMunicipio.getId_municipio()
                    + " creada con exito");
            resultado = new CResultadoOperacion(true, entidadMunicipio.getId_entidad());

        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al CREAR la relación Entidad-Municipio " + sw.toString());
            resultado = new CResultadoOperacion(false, e.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
            }
        } finally {
            try {
                rsSQL.close();
            } catch (Exception ex2) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            try {
                connection.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception ex2) {
            }
        }
        return resultado;
    }
    
    public static CResultadoOperacion ejecutarDeleteEntidadMunicipio(EntidadMunicipio entidadMunicipio) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado = null;
        try {
            logger.debug("Inicio eliminar entidad-municipio: " + entidadMunicipio.getId_municipio());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            connection.setAutoCommit(false);
            preparedStatement = connection
                    .prepareStatement("delete from ENTIDADES_MUNICIPIOS where id_entidad = ? and id_municipio = ?");
            preparedStatement.setInt(1, Integer.parseInt(entidadMunicipio.getId_entidad()));
            preparedStatement.setInt(2, Integer.parseInt(entidadMunicipio.getId_municipio()));
            preparedStatement.execute();
            
            LCGIII_GeopistaUtil.generarSQL(preparedStatement);
            connection.commit();
            logger.debug("Relación Entidad con id: " + entidadMunicipio.getId_entidad()
                    + " y Municipio con Id: " + entidadMunicipio.getId_municipio()
                    + " eliminada");
            resultado = new CResultadoOperacion(true, entidadMunicipio.getId_entidad());

        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al eliminar la relación Entidad-Municipio " + sw.toString());
            resultado = new CResultadoOperacion(false, e.getMessage());
            try {
                connection.rollback();
            } catch (Exception ex2) {
            }
        } finally {
            try {
                rsSQL.close();
            } catch (Exception ex2) {
            }
            try {
                preparedStatement.close();
            } catch (Exception ex2) {
            }
            try {
                connection.close();
                CPoolDatabase.releaseConexion();
            } catch (Exception ex2) {
            }
        }
        return resultado;
    }

    public static CResultadoOperacion ejecutarSelectDetallesOperacion(Operacion op)  {
    	CResultadoOperacion resultado = null;
    	
    	try{
    		
    		DetallesOperacion detallesObtenidos = null;
	    	if(op.getTipoOperacion().equals(Operacion.TIPO_CAPA))
	    		detallesObtenidos = getDetallesOperacionCapa(op);
	    	else
	    		detallesObtenidos = getDetallesOperacionTabla(op);
	    	
	    	
	    	resultado = new CResultadoOperacion(true, "");
    		Vector listaDetalles = new Vector();
    		listaDetalles.add(detallesObtenidos);
    		resultado.setVector(listaDetalles);
    		
			return resultado;
	    	
    	}
    	catch(Exception e){
    		logger.error("[ejecutarSelectDetallesOperacion] ERROR al buscar detalles de una operación " + e.toString());
    		resultado = new CResultadoOperacion(false, e.getMessage());
    		return resultado;
    	}
		
	}
	
	private static DetallesOperacion getDetallesOperacionCapa(Operacion op) throws Exception {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		Integer revision = null;
		String query = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
		DetallesOperacion detalles = new DetallesOperacion(op);
		
		// Obtenermos los datos de la Capa Afectada (tabla, id, etc) 
		detalles.setDatosCapa(getDatosCapa(detalles));
		
		try {
			conn = CPoolDatabase.getConnection();
			if (conn == null) {
				logger.warn("No se puede obtener la conexión");
				throw new Exception("No se puede obtener la conexión");
			}

			st = conn.createStatement();

			if (detalles.getDatosCapa().getVersionable() == 1) {
				// Si la capa es versionada obtenemos la revision generada para la feature
				revision = getRevisionFeatureCapa(detalles);

				if (revision != null) {
					detalles.getOperacion().setRevision(revision);

					// Si la capa es versionada y sea cual sea la operacion obtenemos
					// los datos previos (revision_actual = revision)
					// y los modificados (revision_expirada = revision)
					query = "select * from "
							+ detalles.getDatosCapa().getNombreTabla()
							+ " where id = " + op.getIdFeature()
							+ " and (revision_actual = " + revision + " or "
							+ " revision_expirada = " + revision + " )";
				} else {
					query = " select * from "
							+ detalles.getDatosCapa().getNombreTabla()
							+ " where id = " + op.getIdFeature();
				}

				rs = st.executeQuery(query);
			} else {

				// Si la capa no es versionada, solo recuperamos las modificaciones 
				// Si la operacion no ha sido un borrado
				if (!detalles.getOperacion().getOperacionRealizada().equals(Operacion.ACCION_ELIMINAR)) {

					
					query = " select * from "
							+ detalles.getDatosCapa().getNombreTabla()
							+ " where id = " + op.getIdFeature();

					rs = st.executeQuery(query);
				}
			}

			if (rs == null) 
				return detalles;
				
			for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					detalles.addItemList(DetallesOperacion.listaColumnas, rs
							.getMetaData().getColumnLabel(i));
			}
			

			while (rs.next()) {
				if ((detalles.getDatosCapa().getVersionable() == 1)
						&& (revision != null)
						&& (rs.getObject("revision_expirada") != null)
						&& (rs.getObject("revision_actual") != null)) {

					if (rs.getInt("revision_actual") == detalles.getOperacion().getRevision()) {

						// recogemos los datos de la version que el usuario genero (la version modificada).
						for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
							Object obj = rs.getObject(i);
							if (obj == null)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,"");
							else if (obj instanceof BigDecimal)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,
										((BigDecimal) obj).toString());
							else if (obj instanceof Integer)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,
										String.valueOf((Integer) obj));
							else if (obj instanceof Date)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,
										formatter.format((Date) obj));
							else if (obj instanceof String)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,
										(String) obj);
							else if (obj instanceof PGgeometry)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,
										((PGgeometry) obj).getValue());
							else
								// en caso de que sea un blob o algun dato complejo
								detalles.addItemList(DetallesOperacion.listaVersionModificada,"");
						}
					} else {

						// recogemos los datos que habia previa modificacion (la version previa).
						for (int j = 1; j < rs.getMetaData().getColumnCount() + 1; j++) {
							Object obj = rs.getObject(j);
							if (obj == null)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,"");
							else if (obj instanceof BigDecimal)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,
										((BigDecimal) obj).toString());
							else if (obj instanceof Integer)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,
										String.valueOf((Integer) obj));
							else if (obj instanceof Date)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,
										formatter.format((Date) obj));
							else if (obj instanceof String)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,
										(String) obj);
							else if (obj instanceof PGgeometry)
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,
										((PGgeometry) obj).getValue());
							else
								// en caso de que sea un Geometry o un blob o algun dato complejo
								detalles.addItemList(DetallesOperacion.listaVersionPrevia,"");
						}
					}
				} else {
					// recogemos los datos que modifico el usuario (la version modificada).
					// siempre y cuando no haya sido un borrado
					if (!detalles.getOperacion().getOperacionRealizada()
							.equals(Operacion.ACCION_ELIMINAR)) {

						for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
							Object obj = rs.getObject(i);
							if (obj == null)
								detalles.addItemList(DetallesOperacion.listaVersionModificada,"");
							else if (obj instanceof BigDecimal)
								detalles.addItemList(
										DetallesOperacion.listaVersionModificada,
										((BigDecimal) obj).toString());
							else if (obj instanceof Integer)
								detalles.addItemList(
										DetallesOperacion.listaVersionModificada,
										String.valueOf((Integer) obj));
							else if (obj instanceof Date)
								detalles.addItemList(
										DetallesOperacion.listaVersionModificada,
										formatter.format((Date) obj));
							else if (obj instanceof String)
								detalles.addItemList(
										DetallesOperacion.listaVersionModificada,
										(String) obj);
							else if (obj instanceof PGgeometry)
								detalles.addItemList(
										DetallesOperacion.listaVersionModificada,
										((PGgeometry) obj).getValue());
							else
								// en caso de que sea un blob o un dato complejo
								detalles.addItemList(DetallesOperacion.listaVersionModificada,"");
						}
					}
				}
			}

			return detalles;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[getDetallesOperacionCapa] ERROR :" + e.toString());
			throw new Exception("Se ha producido un error al intentar acceder a los datos");
		} finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { st.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { conn.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}
	}

	private static DetallesOperacion getDetallesOperacionTabla(Operacion op) throws Exception {
		  Connection connection = null;
	      Statement st = null;
	      ResultSet rs = null;
		
	      SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
	      
		//Todas las operaciones se obteienn sobre tablas versionadas (tablas de la EIEL) 
		try {
			 connection = CPoolDatabase.getConnection();
	         if (connection == null) {
	        	 logger.warn("No se puede obtener la conexión");
	        	 throw new Exception("No se puede obtener la conexión");
	         }
	            
			st = connection.createStatement();
			String query = "select * from " +op.getCapaAfectada()+ 
					" where revision_expirada = "+op.getRevision()+
					" or revision_actual = " +op.getRevision();
			
			rs = st.executeQuery(query);
			
			DetallesOperacion detalles = new DetallesOperacion(op);
			
			for(int i=1; i < rs.getMetaData().getColumnCount()+1; i++){							
				detalles.addItemList(DetallesOperacion.listaColumnas, rs.getMetaData().getColumnLabel(i));
			}
			
			System.out.println("COLUMNAS = " +detalles.getNombresColumnasDatosAfectados().size()+ 
					" " +detalles.getNombresColumnasDatosAfectados() );
			
			while (rs.next()) {
				
				System.out.println("revision actual = " +rs.getInt("revision_actual") +" rev_oper = " +detalles.getOperacion().getRevision());
			
				if(rs.getInt("revision_actual") == detalles.getOperacion().getRevision()){
					
					//recogemos los datos de la version que el usuario genero (la version modificada).
					for(int i=1; i < rs.getMetaData().getColumnCount()+1; i++){	
						Object obj = rs.getObject(i);
						if(obj == null)
							detalles.addItemList(DetallesOperacion.listaVersionModificada, "" );
						else if(obj instanceof BigDecimal)
							detalles.addItemList(DetallesOperacion.listaVersionModificada, ((BigDecimal) obj).toString() );
						else if(obj instanceof Integer)						
							detalles.addItemList(DetallesOperacion.listaVersionModificada, String.valueOf( (Integer) obj) );
						 else if(obj instanceof Date)						
								detalles.addItemList(DetallesOperacion.listaVersionModificada, formatter.format((Date)obj) );
						else if(obj instanceof String)
							detalles.addItemList(DetallesOperacion.listaVersionModificada,  (String) obj );
						else //en caso de que sea un blob o algun dato complejo
							detalles.addItemList(DetallesOperacion.listaVersionModificada, "" );
					}
				}
				else{
					
					//recogemos los datos que habia previa modificacion (la version previa).
					for(int j=1; j < rs.getMetaData().getColumnCount()+1; j++){	
						Object obj = rs.getObject(j);
						if(obj == null)
							detalles.addItemList(DetallesOperacion.listaVersionPrevia, "" );
						else if(obj instanceof BigDecimal)
							detalles.addItemList(DetallesOperacion.listaVersionPrevia, ((BigDecimal) obj).toString() );
					   else if(obj instanceof Integer)						
							detalles.addItemList(DetallesOperacion.listaVersionPrevia, String.valueOf( (Integer) obj) );
					   else if(obj instanceof Date)						
							detalles.addItemList(DetallesOperacion.listaVersionPrevia, formatter.format((Date)obj) );
					   else if(obj instanceof String)	
							detalles.addItemList(DetallesOperacion.listaVersionPrevia,  (String) obj );
					   else //en caso de que sea un Geometry o un blob o algun dato complejo
						   detalles.addItemList(DetallesOperacion.listaVersionPrevia,  "" );
					}
				}
			
			}

			return detalles;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[getDetallesOperacionTabla] ERROR :" + e.toString());
			throw new Exception("Se ha producido un error al intentar acceder a los datos");
			
		} finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { st.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { connection.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}	
	}
	
	private static DatosCapa getDatosCapa(DetallesOperacion detalles) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = CPoolDatabase.getConnection();
	         if (conn == null) {
	        	 logger.warn("No se puede obtener la conexión");
	        	 throw new Exception("No se puede obtener la conexión");
	         }
		
			String query = "select distinct(t.id_table), t.name, l.id_layer, d.traduccion, l.versionable " +
					" from layers l, attributes a, columns c, tables t, dictionary d" +
					" where l.id_layer = a.id_layer and a.id_column = c.id and c.id_table = t.id_table " +
					" and l.id_name = d.id_vocablo and t.external=0 and t.id_table not in (9999, 10004) and l.name= ? and d.locale= ?";
			
			ps = conn.prepareStatement(query);
	        ps.setString(1, detalles.getOperacion().getCapaAfectada());
			ps.setString(2, detalles.getOperacion().getLocale());
			rs = ps.executeQuery();
				
			DatosCapa datos = new DatosCapa(detalles.getOperacion().getCapaAfectada());
			
			while (rs.next()) {
				datos.setIdTabla(rs.getInt("id_table"));
				datos.setNombreTabla(rs.getString("name"));
				datos.setIdCapa(rs.getInt("id_layer"));
				datos.setTraduccionCapa(rs.getString("traduccion"));
				datos.setVersionable(rs.getInt("versionable"));
			}
			
			return datos;
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[getDatosCapa] ERROR :" + e.toString());
			return null;
		} 
		finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { ps.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { conn.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}
			
	}
	
	private static Integer getRevisionFeatureCapa(DetallesOperacion detalles) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = CPoolDatabase.getConnection();
	         if (conn == null) {
	        	 logger.warn("No se puede obtener la conexión");
	        	 throw new Exception("No se puede obtener la conexión");
	         }
			 
	         String query = "select revision from versiones where id_autor=? " +
	        		 " and id_table_versionada=? and age(fecha, to_timestamp(?,'yyyy-MM-dd HH24:MI:ss')) = (select min(age(v.fecha, to_timestamp(?,'yyyy-MM-dd HH24:MI:ss'))) "+
	        		 " from iusercnt c, versiones v where c.userid = v.id_autor and " +
	        		 " v.fecha BETWEEN c.timestamp AND c.timeclose and c.id=?  and v.id_autor=? )";
			
			ps = conn.prepareStatement(query);
	        ps.setString(1, detalles.getOperacion().getIdUsuario());
			ps.setInt(2, detalles.getDatosCapa().getIdTabla());
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			ps.setString(3, formatter.format(detalles.getOperacion().getFechaOperacion()));
			ps.setString(4, formatter.format(detalles.getOperacion().getFechaOperacion()));
			ps.setString(5, detalles.getOperacion().getIdConexion());
			ps.setInt(6, Integer.valueOf(detalles.getOperacion().getIdUsuario()));
			
			rs = ps.executeQuery();
			
			Integer revision = null;
			
			if (rs.next()) 
				revision = rs.getInt("revision");
				
			return revision;
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[getRevisionFeatureCapa] ERROR :" + e.toString());
			return null;
		} 
		finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { ps.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { conn.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}
			
	}
	
}

