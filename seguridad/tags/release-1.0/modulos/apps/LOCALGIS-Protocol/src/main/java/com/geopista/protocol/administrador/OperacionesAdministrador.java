package com.geopista.protocol.administrador;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.Category;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.administrador.dominios.ListaCategories;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.protocol.administrador.dominios.ListaDomainNode;
import com.geopista.protocol.control.ListaSesionesSimple;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.sql.GEOPISTAConnection;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

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
 * Date: 25-may-2004
 * Time: 16:38:48
 */
public class OperacionesAdministrador {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperacionesAdministrador.class);
    private String url;
    private final String servletName="/CServletDB";
    private final String servletNameActualizarRol="/UpdateRol";
    private final String servletNameEliminarRol="/DeleteRol";
    private final String servletNameNuevoRol="/NewRol";
    private final String servletNameActualizarUsuario="/UpdateUser";
    private final String servletNameNuevoUsuario="/NewUser";
    private final String servletNameEliminarUsuario="/DeleteUser";
    private final String servletNameNuevoDomainNode="/NewDomainNode";
    private final String servletNameEliminarDomainNode="/DeleteDomainNode";
    private final String servletNameActualizarDomainNode="/UpdateDomainNode";
    private final String servletNameGetSesiones="/GetSesions";
    private final String servletNameNuevoDomain="/NewDomain";
    private final String servletNameEliminarDomain="/DeleteDomain";
    private final String servletNameNuevaEntidad="/NewEntidad";
    private final String servletNameEliminarEntidad="/DeleteEntidad";
    private final String servletNameActualizarEntidad="/UpdateEntidad";
    private final String servletNameNuevoEntidadMunicipio="/NewEntidadMunicipio";
    private final String servletNameEliminarEntidadMunicipio="/DeleteEntidadMunicipio";
    private final String servletNameManageSesiones="/ManageSessions";
    private TaskMonitorDialog taskMonitor;
	

    public OperacionesAdministrador(String sUrl)
    {
        url=sUrl;

    }
	public ListaPermisos getPermisos() throws Exception{
		PreparedStatement ps = null;
		ResultSet  rs = null;
		Connection conn = null;
        try {
             Class.forName("com.geopista.sql.GEOPISTADriver");
             String sConn = "jdbc:pista:"+url+servletName;
             conn = DriverManager.getConnection(sConn);
                // en vez de la sentencia SQL.
                // select idperm, def, type from UsrGrouPerm
                ps = conn.prepareStatement("allpermisos");
                rs = ps.executeQuery();
                ListaPermisos auxListaPermisos =new ListaPermisos();
                while (rs.next()) {
                    Permiso auxPermiso= new Permiso(rs.getString("idperm"),rs.getString("def"), null, rs.getString("type"));
                    auxListaPermisos.add(auxPermiso);
                }
                return auxListaPermisos;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger los permisos:"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
       return null;
    }

    public ListaUsuarios getUsuarios(int iIdEntidad) throws Exception{
    	ResultSet rs = null;
    	PreparedStatement ps = null;
    	Connection conn = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);

                // Primero buscamos los roles
                //Select id, name, nombrecompleto, password, remarks,mail, deptid FROM IUSERUSERHDR where borrado!=1 and id_municipio=?
                ps = conn.prepareStatement("allusuarios");
                ps.setInt(1,iIdEntidad);
                rs =ps.executeQuery();
                ListaUsuarios auxListaUsuarios =new ListaUsuarios();
                EncriptarPassword  ep=new EncriptarPassword();
                while (rs.next())
                {
                	String id=rs.getString("id");
                	String name=rs.getString("name");
                	String password=ep.undoEncrip(rs.getString("password"));
                	String deptid=rs.getString("deptid");
                	String email=rs.getString("mail");
                	String nif=rs.getString("nif");
                	String remarks=rs.getString("remarks");
                	String nombreCompleto= rs.getString("nombrecompleto");
                	String id_entidad = rs.getString("id_entidad");
                	Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif, id_entidad);
                	//System.out.println("Añadiendo usuario: "+name);
                    auxListaUsuarios.add(usuario);
                }
                //System.out.println("Finalizamos añadiendo usuarios");
                rs.close();
			    ps.close();
                // en vez de la sentencia SQL.
                //select r_usr_perm.userid userid , r_usr_perm.idperm idperm, r_usr_perm.idacl idacl, r_usr_perm.aplica aplica from r_usr_perm order by userid
                ps = conn.prepareStatement("allusuariospermisos");
                rs =ps.executeQuery();
                while (rs.next())
                {
                    Permiso auxPermiso=new Permiso(rs.getString("idPerm"),rs.getString("idAcl"),!(rs.getString("aplica")!=null&&rs.getString("aplica").equals("0")));
                    String sIdUsuario=rs.getString("userid");
                    auxListaUsuarios.addPermiso(sIdUsuario,auxPermiso);
                }
                rs.close();
			    ps.close();
                //allUsuariosroles
                //select iusergroupuser.userid userid , iusergroupuser.groupid groupid  from iusergroupuser order by userid
                ps = conn.prepareStatement("allusuariosroles");
                rs =ps.executeQuery();
                while (rs.next())
                {
                     auxListaUsuarios.addGrupo(rs.getString("userid"),rs.getString("groupid"));
                }
                return auxListaUsuarios;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los usuarios :"+sw.toString());
            throw e;
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
    }

    public ListaUsuarios getListaUsuarios() throws Exception{
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);

                
                //SELECT id, name, nombrecompleto, password, remarks,mail, deptid, nif, id_entidad FROM iuseruserhdr WHERE borrado!=1
                ps = conn.prepareStatement("getListaUsuarios");

                rs =ps.executeQuery();
                ListaUsuarios auxListaUsuarios =new ListaUsuarios();
                EncriptarPassword  ep=new EncriptarPassword();
                while (rs.next())
                {
                	String id=rs.getString("id");
                	String name=rs.getString("name");
                	String password=ep.undoEncrip(rs.getString("password"));
                	String deptid=rs.getString("deptid");
                	String email=rs.getString("mail");
                	String nif=rs.getString("nif");
                	String remarks=rs.getString("remarks");
                	String nombreCompleto= rs.getString("nombrecompleto");
                	String id_entidad = rs.getString("id_entidad");
                	boolean bloqueado = rs.getBoolean("bloqueado");
                	Usuario usuario=new Usuario(id,name,password,deptid,email,remarks,nombreCompleto,nif, id_entidad, bloqueado);
                	//System.out.println("Añadiendo usuario: "+name);
                    auxListaUsuarios.add(usuario);
                }
                //System.out.println("Finalizamos añadiendo usuarios");
                rs.close();
			    ps.close();
                // en vez de la sentencia SQL.
                //select r_usr_perm.userid userid , r_usr_perm.idperm idperm, r_usr_perm.idacl idacl, r_usr_perm.aplica aplica from r_usr_perm order by userid
                ps = conn.prepareStatement("allusuariospermisos");
                rs =ps.executeQuery();
                while (rs.next())
                {
                    Permiso auxPermiso=new Permiso(rs.getString("idPerm"),rs.getString("idAcl"),!(rs.getString("aplica")!=null&&rs.getString("aplica").equals("0")));
                    String sIdUsuario=rs.getString("userid");
                    auxListaUsuarios.addPermiso(sIdUsuario,auxPermiso);
                }
                rs.close();
			    ps.close();
                //allUsuariosroles
                //select iusergroupuser.userid userid , iusergroupuser.groupid groupid  from iusergroupuser order by userid
                ps = conn.prepareStatement("allusuariosroles");
                rs =ps.executeQuery();
                while (rs.next())
                {
                     auxListaUsuarios.addGrupo(rs.getString("userid"),rs.getString("groupid"));
                }
                return auxListaUsuarios;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los usuarios :"+sw.toString());
            throw e;

       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
    }
    
    public ListaPermisos getPermisosUsuario(String sIdUsuario) throws Exception{
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);

                // Primero buscamos los roles
                //select idacl,idperm from r_group_perm,iusergroupuser where r_group_perm.GROUPID = iusergroupuser.GROUPID and iusergroupuser.userid=? and
                //idperm||'-'||idacl not in (select idperm||'-'||idacl from r_usr_perm where r_usr_perm.userid=? and r_usr_perm.aplica=0) union
                //select idacl,idperm from r_usr_perm where r_usr_perm.userid=? and (r_usr_perm.aplica<>0 or r_usr_perm.aplica is null) order by idacl, idperm;

                ps = conn.prepareStatement("getPermisosUsuario");
                ps.setString(1,sIdUsuario);
                ps.setString(2,sIdUsuario);
                ps.setString(3,sIdUsuario);
                rs =ps.executeQuery();
                ListaPermisos auxListaPermisos =new ListaPermisos();
                while (rs.next())
                {
                    auxListaPermisos.add(new Permiso(rs.getString("idperm"),rs.getString("idacl"),true));
                }
                return auxListaPermisos;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los usuarios :"+sw.toString());
            throw e;
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
    }
    
    //NUEVO
    public ListaApp getApps() throws Exception{
   	 PreparedStatement ps = null;
   	 ResultSet  rs = null;
   	 Connection conn = null;
       try {
               Class.forName("com.geopista.sql.GEOPISTADriver");
               String sConn = "jdbc:pista:"+url+servletName;
               conn = DriverManager.getConnection(sConn);

               // Para crear un PreparedStatement lo haremos con la ID del catalogo
               // en vez de la sentencia SQL.
               ps = conn.prepareStatement("allapps");
               rs = ps.executeQuery();
               App oldApp=null;
               Acl oldAcl=null;
               ListaApp auxListaApp =new ListaApp();
               while (rs.next()) {
                   String newIdApp=rs.getString("idapp");  
                   String newIdAcl=null;
                   if ((oldApp==null) || (!oldApp.getId().equals(newIdApp)))
                   {
                       oldApp = new App(rs.getString("idapp"),rs.getString("nameapp"));                       
                       newIdAcl=rs.getString("idacl");
                       if ((oldAcl==null) || (!oldAcl.getId().equals(newIdAcl)))
                       {
                    	   oldAcl = new Acl(rs.getString("idacl"),rs.getString("nameacl"),rs.getString("idapp"));   
                    	   oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null,rs.getString("idacl")));
                    	   oldApp.addAcls(oldAcl); 
                       }
                       else{
                    	   oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null,rs.getString("idacl")));
                       }      
                       
                       auxListaApp.add(oldApp);
	               }
	               else
	               {	            	   
	            	   newIdAcl=rs.getString("idacl");
	            	   if ((oldAcl==null) || (!oldAcl.getId().equals(newIdAcl)))
                       {
	            		   oldAcl = new Acl(rs.getString("idacl"),rs.getString("nameacl"),rs.getString("idapp"));
                    	   oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null, rs.getString("idacl"))); 
                    	   oldApp.addAcls(oldAcl);
                       }
                       else{
                    	   oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null/*getTranslate(rs.getInt("permname"))*/, rs.getString("idacl")));
                       }  
                	   
	            	   
	               }
               }
           return auxListaApp;
      } catch (Exception e) {
           java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
           logger.error("ERROR al recoger la lista de ACLs:"+sw.toString());
      }finally{
   	   try{rs.close();}catch(Exception e){};
   	   try{ps.close();}catch(Exception e){};
   	   try{conn.close();}catch(Exception e){};
      }
       return null;
   }
  //FIN NUEVO
    
     public ListaAcl getAcls() throws Exception{
    	 PreparedStatement ps = null;
    	 ResultSet  rs = null;
    	 Connection conn = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);

                // Para crear un PreparedStatement lo haremos con la ID del catalogo
                // en vez de la sentencia SQL.
                ps = conn.prepareStatement("allaclspermisos");
                rs = ps.executeQuery();
                Acl oldAcl=null;
                ListaAcl auxListaAcl =new ListaAcl();
                while (rs.next()) {
                    String newIdAcl=rs.getString("idacl");
                    if ((oldAcl==null) || (!oldAcl.getId().equals(newIdAcl)))
                    {
                        oldAcl = new Acl(rs.getString("idacl"),rs.getString("name"),"");
                        oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null,rs.getString("idacl")));
                        auxListaAcl.add(oldAcl);
                }
                else
                {
                     oldAcl.addPermiso(new Permiso(rs.getString("idperm"),rs.getString("def"), null, rs.getString("idacl")));
                }
         	}
            return auxListaAcl;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger la lista de ACLs:"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
        return null;
    }
    
     public CResultadoOperacion actualizarRol(Rol rol) throws Exception
    {
         try {  /*
                StringWriter sw = new StringWriter();
			    Marshaller.marshal(rol, sw);
                */
                 StringWriter sw = new StringWriter();
                 Marshaller marshaller = new Marshaller(sw);
                 marshaller.setEncoding("ISO-8859-1");
                 marshaller.marshal(rol);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameActualizarRol, sw.toString() );
		        if (resultado.getResultado())
		        	rol.setId(resultado.getDescripcion());
	            return resultado;

		 }
         catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception acturalizar rol: " + sw.toString());
			throw ex;
        }
    }
     

     public CResultadoOperacion eliminarRol(Rol rol) throws Exception
       {
            try {
                    /*
                    StringWriter sw = new StringWriter();
                    Marshaller.marshal(rol, sw);
                    */
                    StringWriter sw = new StringWriter();
                    Marshaller marshaller = new Marshaller(sw);
                    marshaller.setEncoding("ISO-8859-1");
                    marshaller.marshal(rol);

                   CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameEliminarRol, sw.toString() );
                   return resultado;                 
            }
            catch (Exception ex) {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               ex.printStackTrace(pw);
               logger.error("Exception al eliminar: " + sw.toString());
               throw ex;
           }
       }
    
     public CResultadoOperacion nuevoRol(Rol rol) throws Exception
    {
         try {  /*
                StringWriter sw = new StringWriter();
			    Marshaller.marshal(rol, sw);
                */
                 StringWriter sw = new StringWriter();
                 Marshaller marshaller = new Marshaller(sw);
                 marshaller.setEncoding("ISO-8859-1");
                 marshaller.marshal(rol);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameNuevoRol, sw.toString() );
                if (resultado.getResultado())
                    rol.setId(resultado.getDescripcion());
                return resultado;
		 }
         catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al crear rol: " + sw.toString());
			throw ex;
        }
    }

     public ListaRoles getRoles(int iIdEntidad) throws Exception{
    	 PreparedStatement ps = null;
    	 Connection conn = null;
    	 ResultSet  rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);

                // Primero buscamos los roles
                // select iusergrouphdr.id id, iusergrouphdr.name name,  iusergrouphdr.remarks remarks from iusergrouphdr where id_municipio=?
                ps = conn.prepareStatement("allroles");
                ps.setInt(1,iIdEntidad);
                rs =ps.executeQuery();
                ListaRoles auxListaRoles =new ListaRoles();
                while (rs.next())
                {
                    auxListaRoles.add(new Rol(rs.getString("id"),rs.getString("name"),rs.getString("remarks")));
                }
                rs.close();
			    ps.close();
                // en vez de la sentencia SQL.
                //select iusergrouphdr.id id, iusergrouphdr.name name, iusergrouphdr.remarks remarks, r_group_perm.idperm idperm, r_group_perm.idacl idacl from iusergrouphdr,r_group_perm where iusergrouphdr.id_municipio=? and iusergrouphdr.id=r_group_perm.groupid order by iusergrouphdr.id
                ps = conn.prepareStatement("allrolespermisos");
                ps.setInt(1,iIdEntidad);
                rs =ps.executeQuery();
                Rol oldRol=null;
                while (rs.next()) {
                    String newIdRol=rs.getString("id");
                    if ((oldRol==null) || (!oldRol.getId().equals(newIdRol)))
                    {
                        oldRol = new Rol(rs.getString("id"),rs.getString("name"),rs.getString("remarks"));
                        oldRol.addPermiso(rs.getString("idPerm"), rs.getString("idAcl"));
                        auxListaRoles.add(oldRol);
                }
                else
                {
                     oldRol.addPermiso(rs.getString("idPerm"),rs.getString("idAcl"));
                }
         	}

            return auxListaRoles;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR al coger los roles :"+sw.toString());
            throw e;
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }

    }

     public CResultadoOperacion actualizarUsuario(Usuario usuario) throws Exception
    {
         try {
                /*
                StringWriter sw = new StringWriter();
			    Marshaller.marshal(usuario, sw);
                */
                 StringWriter sw = new StringWriter();
                 Marshaller marshaller = new Marshaller(sw);
                 marshaller.setEncoding("ISO-8859-1");
                 marshaller.marshal(usuario);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameActualizarUsuario, sw.toString() );
                return resultado;
		 }
         catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception acturalizar Usuario: " + sw.toString());
			throw ex;
        }
     }
     
     public CResultadoOperacion nuevoUsuario(Usuario usuario) throws Exception
        {
             try {
                 /*
                 StringWriter sw = new StringWriter();
			     Marshaller.marshal(usuario, sw);
                 */
                 StringWriter sw = new StringWriter();
                 Marshaller marshaller = new Marshaller(sw);
                 marshaller.setEncoding("ISO-8859-1");
                 marshaller.marshal(usuario);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameNuevoUsuario, sw.toString() );
			    System.out.println("Esto es lo que se envía: "+url+servletNameNuevoUsuario+sw.toString());
                if (resultado.getResultado())
                    usuario.setId(resultado.getDescripcion());
                return resultado;
		    }
            catch (Exception ex) {
			    StringWriter sw = new StringWriter();
			    PrintWriter pw = new PrintWriter(sw);
			    ex.printStackTrace(pw);
			    logger.error("Exception al crear rol: " + sw.toString());
			    throw ex;
            }
        }
        
     public CResultadoOperacion eliminarUsuario(Usuario usuario) throws Exception
         {
              try {
                     /*
                     StringWriter sw = new StringWriter();
                     Marshaller.marshal(usuario, sw);
                     */
                     StringWriter sw = new StringWriter();
                     Marshaller marshaller = new Marshaller(sw);
                     marshaller.setEncoding("ISO-8859-1");
                     marshaller.marshal(usuario);

                     CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameEliminarUsuario, sw.toString() );
                     return resultado;
              }
              catch (Exception ex) {
                 StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw);
                 ex.printStackTrace(pw);
                 logger.error("Exception al eliminar: " + sw.toString());
                 throw ex;
             }
         }
   
     public boolean getDominios(String idCategoria, ListaDomain listaGeneral, ListaDomain listaParticular) throws Exception{
    		Connection conn = null;
    		PreparedStatement ps = null;
    		ResultSet rs = null;
            try {

                    Class.forName("com.geopista.sql.GEOPISTADriver");
                    String sConn = "jdbc:pista:"+url+servletName;
                    conn = DriverManager.getConnection(sConn);
                    ((GEOPISTAConnection)conn).setTaskMonitor(taskMonitor);
                    logger.info("Realizando peticion de dominios");
                    
                    if (idCategoria==null)
                            ps=conn.prepareStatement("alldominios");
                    else
                    {
                            ps=conn.prepareStatement("alldominioscategoria");
                            ps.setString(1,idCategoria);
                    }

                    rs=ps.executeQuery();
                    while (rs.next())
                    {
                         listaGeneral.add(new Domain(rs.getString("id_domain"),rs.getString("name"),rs.getString("id_category")));
                         listaParticular.add(new Domain(rs.getString("id_domain"),rs.getString("name"),rs.getString("id_category")));
                    }
                    logger.info("Realizando peticion de nodos");
                    if (idCategoria==null)
                    {
                        ps = conn.prepareStatement("alldominionodes");
                        ps.setString(1,com.geopista.security.SecurityManager.getIdEntidad());
                    }
                    else
                    {
                        ps = conn.prepareStatement("alldominionodescategoria");
                        ps.setString(1,com.geopista.security.SecurityManager.getIdEntidad());
                        ps.setString(2,idCategoria);
                    }
                    //select domainnodes.id_domain as id_domain, domainnodes.id as id_node,
                    //domainnodes.pattern as pattern,domainnodes.ID_DESCRIPTION as id_descripcion,
                    //domainnodes.type as type, domainnodes.PARENTDOMAIN as parentdomain,dictionary.locale as locale, dictionary.traduccion as traduccion  from  domainnodes LEFT OUTER JOIN dictionary ON (domainnodes.id_description=dictionary.id_vocablo)
                    //where domainnodes.id_municipio=1 order by id_domain, id_node
                    rs =ps.executeQuery();
                    ListaDomainNode auxListNodesParticular=new ListaDomainNode();
                    ListaDomainNode auxListNodesGeneral=new ListaDomainNode();
                    DomainNode oldDomainNode=null;
                    while (rs.next())
                    {
                         String newIdDomainNode=rs.getString("id_node");
                         if ((oldDomainNode==null) || (!oldDomainNode.getIdNode().equals(newIdDomainNode)))
                         {
                                 oldDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_descripcion"),
                                                                  new Integer(rs.getString("type")).intValue(), rs.getString("parentdomain"), rs.getString("id_municipio"),rs.getString("id_domain"),rs.getString("pattern"));
                                 if (rs.getString("id_descripcion")!=null)
                                    oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                                 if (rs.getString("id_municipio")!=null)
                                    auxListNodesParticular.add(oldDomainNode);
                                 else
                                    auxListNodesGeneral.add(oldDomainNode);
                         }
                         else
                         {
                                 oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                         }
                    }
                    logger.info("Fin peticion nodos");
                    auxListNodesGeneral.restructurar();
                    auxListNodesParticular.restructurar();
                    logger.info("Fin Restructurar");

                    for (Enumeration e=auxListNodesGeneral.gethDom().elements();e.hasMoreElements();)
                    {
                         DomainNode auxDomainNode=(DomainNode)e.nextElement();
                         if (auxDomainNode.getIdDomain()!=null)
                             listaGeneral.get(auxDomainNode.getIdDomain()).addNode(auxDomainNode);
                    }
                    for (Enumeration e=auxListNodesParticular.gethDom().elements();e.hasMoreElements();)
                    {
                         DomainNode auxDomainNode=(DomainNode)e.nextElement();
                         if (auxDomainNode.getIdDomain()!=null)
                             listaParticular.get(auxDomainNode.getIdDomain()).addNode(auxDomainNode);
                    }
                    return true;
           } catch (Exception e) {
                java.io.StringWriter sw=new java.io.StringWriter();
                java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al recoger los dominios:"+sw.toString());
                throw e;
           }finally{
        	   try{rs.close();}catch(Exception e){};
        	   try{ps.close();}catch(Exception e){};
        	   try{conn.close();}catch(Exception e){};
           }
         }
      
        public CResultadoOperacion nuevoDomainNode(DomainNode auxDomainNode) throws Exception
         {
              try {
                  /*
                  StringWriter sw = new StringWriter();
			      Marshaller.marshal(auxDomainNode, sw);
                  */
                  StringWriter sw = new StringWriter();
                  Marshaller marshaller = new Marshaller(sw);
                  marshaller.setEncoding("ISO-8859-1");
                  marshaller.marshal(auxDomainNode);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameNuevoDomainNode, sw.toString() );
                if (resultado.getResultado())
                {
                    try
                    {
                        auxDomainNode=(DomainNode)resultado.getVector().get(0);
                    }catch(Exception e)
                    {
                        auxDomainNode.setIdNode(resultado.getDescripcion());
                    }
                }

                return resultado;
		    }
            catch (Exception ex) {
			    StringWriter sw = new StringWriter();
			    PrintWriter pw = new PrintWriter(sw);
			    ex.printStackTrace(pw);
			    logger.error("Exception al crear el DomainNode: " + sw.toString());
			    throw ex;
            }
         }
       
        public CResultadoOperacion nuevoDomain(Domain auxDomain) throws Exception
           {
                try {
                  /*
                  StringWriter sw = new StringWriter();
                  Marshaller.marshal(auxDomain, sw);
                  */
                  StringWriter sw = new StringWriter();
                  Marshaller marshaller = new Marshaller(sw);
                  marshaller.setEncoding("ISO-8859-1");
                  marshaller.marshal(auxDomain);

                  CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameNuevoDomain, sw.toString() );
                  if (resultado.getResultado())
                  {
                      try
                      {
                          auxDomain=(Domain)resultado.getVector().get(0);
                      }catch(Exception e)
                      {
                          auxDomain.setIdDomain(resultado.getDescripcion());
                      }
                  }
                  logger.debug("Identificador del Domain insertado:"+auxDomain.getIdDomain());
                  return resultado;
              }
              catch (Exception ex) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  ex.printStackTrace(pw);
                  logger.error("Exception al crear el Domain: " + sw.toString());
                  throw ex;
              }
           }

         public CResultadoOperacion eliminarDomainNode(DomainNode nodo) throws Exception
         {
              try {
                     /*
                     StringWriter sw = new StringWriter();
                     Marshaller.marshal(nodo, sw);
                     */
                     StringWriter sw = new StringWriter();
                     Marshaller marshaller = new Marshaller(sw);
                     marshaller.setEncoding("ISO-8859-1");
                     marshaller.marshal(nodo);

                     CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameEliminarDomainNode, sw.toString() );
                     return resultado;
              }
              catch (Exception ex) {
                 StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw);
                 ex.printStackTrace(pw);
                 logger.error("Exception al eliminar el nodo: " + sw.toString());
                 throw ex;
             }
         }

          public CResultadoOperacion eliminarDomain(Domain nodo) throws Exception
         {
              try {
                     /*
                     StringWriter sw = new StringWriter();
                     Marshaller.marshal(nodo, sw);
                     */
                     StringWriter sw = new StringWriter();
                     Marshaller marshaller = new Marshaller(sw);
                     marshaller.setEncoding("ISO-8859-1");
                     marshaller.marshal(nodo);

                     CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameEliminarDomain, sw.toString() );
                     return resultado;
              }
              catch (Exception ex) {
                 StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw);
                 ex.printStackTrace(pw);
                 logger.error("Exception al eliminar el domain: " + sw.toString());
                 throw ex;
             }
         }
        
          public CResultadoOperacion actualizarDomainNode(DomainNode auxDomainNode) throws Exception
         {
              try {
                /*
                StringWriter sw = new StringWriter();
			    Marshaller.marshal(auxDomainNode, sw);
                */
                StringWriter sw = new StringWriter();
                Marshaller marshaller = new Marshaller(sw);
                marshaller.setEncoding("ISO-8859-1");
                marshaller.marshal(auxDomainNode);

			    CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameActualizarDomainNode, sw.toString() );
                return resultado;
		    }
            catch (Exception ex) {
			    StringWriter sw = new StringWriter();
			    PrintWriter pw = new PrintWriter(sw);
			    ex.printStackTrace(pw);
			    logger.error("Exception al modificar el DomainNode: " + sw.toString());
			    throw ex;
            }
         }

         public ListaSesionesSimple getSesiones() throws Exception
             {
                  try {

                      StringReader sr=EnviarSeguro.enviarPlano(url+servletNameGetSesiones,"");
                      ListaSesionesSimple listaSesiones=(ListaSesionesSimple)Unmarshaller.unmarshal(ListaSesionesSimple.class,sr);
                      return listaSesiones;
                }
                catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    logger.error("Exception al obtener las sesiones: " + sw.toString());
                    throw ex;
                }
             }

             public ListaCategories getListaCategorias(){
            	 
            	 Connection conn = null;
            	 PreparedStatement ps = null;
            	 ResultSet  rs = null;
            	 
                 ListaCategories lista= new ListaCategories();
                 String sConn = "jdbc:pista:"+url+servletName;
                try {
                        Class.forName("com.geopista.sql.GEOPISTADriver");
                        conn = DriverManager.getConnection(sConn);
                        // en vez de la sentencia SQL.
                        // "'select domaincategory.id as id,domaincategory.id_description as iddes,dictionary.locale, dictionary.traduccion\n" +
                        // "                         from domaincategory,dictionary where  domaincategory.id_description= dictionary.id_vocablo\n" +
                        // "                         order by domaincategory.id');"

                        ps = conn.prepareStatement("getcategorias");
                        rs =ps.executeQuery();
                        Category  oldCategory=null;
                        while (rs.next()){
                            String newId=rs.getString("id");
                             if ((oldCategory==null) || (!oldCategory.getId().equals(newId)))
                             {
                                     oldCategory= new Category(rs.getString("id"),rs.getString("iddes"));
                                     if (oldCategory.getIdDes()!=null)
                                        oldCategory.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                                     lista.add(oldCategory);
                             }
                             else
                             {
                                     oldCategory.addTerm(rs.getString("locale"), rs.getString("traduccion"));
                             }
                        }

       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("ERROR al recoger la estructura las categorias conectando a "+sConn+" :"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
        return lista;
    }
    
    public Vector getConexiones(String sIdUsuario) throws Exception{
    	 Connection conn = null;
    	 PreparedStatement ps = null;
    	 ResultSet rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);
                // en vez de la sentencia SQL.
                // select iusercnt.userid, appgeopista.def, iusercnt.timestamp , iusercnt.timeclose
                // from iusercnt,appgeopista where iusercnt.appid=appgeopista.appid and
                // iusercnt.userid=?
                ps = conn.prepareStatement("conexiones");
                ps.setString(1,sIdUsuario);
                rs = ps.executeQuery();
                Vector listaConexiones =new Vector();
                while (rs.next()) {
                    Conexion conexion= new Conexion(rs.getString("id"),
                    		rs.getString("userid"),
                    		rs.getString("def"),
                    		rs.getTimestamp("timestamp"),
                    		rs.getTimestamp("timeclose"));
                    
                    if(rs.getString("array_to_string") != null){
                    	String aplicaciones = rs.getString("array_to_string");
                    	if(aplicaciones != null && !aplicaciones.equalsIgnoreCase(""))
                    		conexion.setIdApp("SSO (" +aplicaciones+ ")");
                    }
                    
                    listaConexiones.add(conexion);
                }
                return listaConexiones;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los permisos:"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
        return null;
    }
   
    public Municipio getMunicipio(String sIdMunicipio) throws Exception{
   	 	Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	ResultSet rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);
                // en vez de la sentencia SQL.
                //select m.nombreoficial as nombre, p.id as idprovincia, p.nombreoficial as provincia from municipios m,provincias p
                //where m.id_provincia=p.id and m.id=?
                ps = conn.prepareStatement("getMunicipio");
                ps.setString(1,sIdMunicipio);
                rs =ps.executeQuery();
                Municipio municipio=null;
                if  (rs.next()) {
                    municipio= new Municipio(sIdMunicipio, rs.getString("idprovincia"),rs.getString("nombre"),rs.getString("provincia"));
                }
                return municipio;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos del municipio:"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
        return null;
    }
    
    /**
     * Se obtiene información sobre una entidad supramunicipal
     * @param sIdEntidad
     * @return
     * @throws Exception
     */
    public Entidad getEntidad(String sIdEntidad) throws Exception{
    	Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	ResultSet rs = null;
        try {
                Class.forName("com.geopista.sql.GEOPISTADriver");
                String sConn = "jdbc:pista:"+url+servletName;
                conn = DriverManager.getConnection(sConn);
                ps = conn.prepareStatement("getEntidad");
                ps.setString(1,sIdEntidad);
                rs =ps.executeQuery();
                Entidad entidad=null;
                if  (rs.next()) {
                	entidad= new Entidad(sIdEntidad, rs.getString("nombreoficial"), Integer.toString(rs.getInt("srid")));
                }
                return entidad;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de la entidad:"+sw.toString());
       }finally{
    	   try{rs.close();}catch(Exception e){};
    	   try{ps.close();}catch(Exception e){};
    	   try{conn.close();}catch(Exception e){};
       }
        return null;
    }
    
    /**
     * Obtiene una coleción de todas las entidades supramunicipales.
     * La colección contiene objetos de tipo Entidad
     * @return
     */
    public Collection getEntidadesSortedById() {
        Collection<Entidad> entidades = new ArrayList<Entidad>();
        Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	PreparedStatement psAux = null;
   	 	ResultSet rs = null;
   	 	ResultSet rsAux = null;
   	 	Hashtable<String,String> entidadesEncontradas=new Hashtable<String,String>();
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:" + url + servletName;
            conn = DriverManager.getConnection(sConn);
            
            ps = conn.prepareStatement("getEntidadesSortedByIdEntidad");
            try{
	            rs = ps.executeQuery();
	            //Entidad entidad = null;
	            while (rs.next()) {
	            	
	                Entidad entidad = new Entidad(rs.getString("id_entidad"), rs.getString("nombreoficial"), Integer.toString(rs.getInt("srid")), rs.getInt("backup"), rs.getInt("aviso"), rs.getInt("periodicidad"), rs.getInt("intentos"));
	                String idMunicipio=rs.getString("id_municipio");
	                String nombremunicipio=rs.getString("nombremunicipio");
	                entidadesEncontradas.put(rs.getString("id_entidad"),rs.getString("id_entidad"));

	                //Si la entidad ya existe en el listado actualizamos el municipio.
	                boolean encontrado=false;
	                if (entidadesEncontradas.contains(rs.getString("id_entidad"))){
	                	java.util.Iterator<Entidad> it=entidades.iterator();
	                	while (it.hasNext()){
	                		Entidad entidadBusqueda=it.next();
	                		if (entidadBusqueda.getId().equals(entidad.getId())){
	                			Set<Municipio> municipios=entidadBusqueda.getMunicipios();
	                			Municipio municipio=new Municipio(idMunicipio,idMunicipio.substring(0,2),nombremunicipio,"");
	                			municipios.add(municipio);
	    	                	entidad.setMunicipios(municipios);
	    	                	encontrado=true;
	    	                	break;
	                		}
	                	}	   
	                }
	                
	                if (!encontrado){
	                	Set<Municipio> municipios=new HashSet<Municipio>();
	                	Municipio municipio=new Municipio(idMunicipio,idMunicipio.substring(0,2),nombremunicipio,"");
	                	municipios.add(municipio);
	                	entidad.setMunicipios(municipios);
	                	entidades.add(entidad);
	                }
	            }
            }
            catch (Exception e1){
            	ps = conn.prepareStatement("getEntidadesSortedById");
	            rs = ps.executeQuery();
	            //Entidad entidad = null;
	            while (rs.next()) {
	                Entidad entidad = new Entidad(rs.getString("id_entidad"), rs.getString("nombreoficial"), Integer.toString(rs.getInt("srid")), rs.getInt("backup"), rs.getInt("aviso"), rs.getInt("periodicidad"), rs.getInt("intentos"));
	                entidades.add(entidad);
	            }
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de la entidad:" + sw.toString());
        }finally{
     	   try{rs.close();}catch(Exception e){};
     	   try{ps.close();}catch(Exception e){};
     	   try{conn.close();}catch(Exception e){};
        }
        return entidades;
    }
    
    
    /**
     * Obtiene un objeto de tipo ListaEntidades que contiene todas las entidades supramunicipales.
     * @return
     */
    public ListaEntidades getListaEntidades() {
        ListaEntidades auxListaEntidades = null;
        Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	PreparedStatement psAux = null;
   	 	ResultSet rs = null;        
   	 	ResultSet rsAux = null;  
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:"+url+servletName;
            conn = DriverManager.getConnection(sConn);
            ps = conn.prepareStatement("getEntidadesSortedByName");
            rs = ps.executeQuery();
            auxListaEntidades = new ListaEntidades();
            Entidad entidad = null;
            while (rs.next()) {
                entidad = new Entidad(rs.getString("id_entidad"), rs.getString("nombreoficial"), Integer.toString(rs.getInt("srid")), rs.getInt("backup"), rs.getInt("aviso"), rs.getInt("periodicidad"), rs.getInt("intentos"));
                auxListaEntidades.add(entidad);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de la entidad:" + sw.toString());
        }finally{
     	   try{rs.close();}catch(Exception e){};
     	   try{ps.close();}catch(Exception e){};
     	   try{conn.close();}catch(Exception e){};
        }
        return auxListaEntidades;
    }
    
    //NUEVO
    /**
     * Obtiene un objeto de tipo ListaEntidades cuyas entidades contienen el código de entidad externo (SIGM) que contiene todas las entidades supramunicipales.
     * @return
     */
    public ListaEntidades getListaEntidadesWithExt() {
        ListaEntidades auxListaEntidades = null;
        Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	PreparedStatement psAux = null;
   	 	ResultSet rs = null;        
   	 	ResultSet rsAux = null;  
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:"+url+servletName;
            conn = DriverManager.getConnection(sConn);
            ps = conn.prepareStatement("getEntidadesWithExtSortedByName");
            rs = ps.executeQuery();
            auxListaEntidades = new ListaEntidades();
            Entidad entidad = null;
            while (rs.next()) {
                entidad = new Entidad(rs.getString("id_entidad"), rs.getString("nombreoficial"), Integer.toString(rs.getInt("srid")), rs.getInt("backup"), rs.getInt("aviso"), rs.getInt("periodicidad"), rs.getInt("intentos"), rs.getString("id_entidadext"));
                auxListaEntidades.add(entidad);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de la entidad:" + sw.toString());
        }finally{
     	   try{rs.close();}catch(Exception e){};
     	   try{ps.close();}catch(Exception e){};
     	   try{conn.close();}catch(Exception e){};
        }
        return auxListaEntidades;
    }
    //FIN NUEVO   
    
    /**
     * Obtiene los municipios que tienen asociados entidades. Se obtiene un conjunto (Set) que 
     * contiene Strings con los códigos de los municipios.
     * @return
     */
    public Collection getMunicipiosAsociadosEntidades () {
        Collection municipios = new ArrayList();
        Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	ResultSet rs = null;
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:" + url + servletName;
            conn = DriverManager.getConnection(sConn);
            ps = conn.prepareStatement("getMunicipiosAsociadosEntidades");
            rs = ps.executeQuery();
            Municipio municipio = null;
            while (rs.next()) {
                municipio = new Municipio (rs.getString ("id_municipio"), rs.getString("id_provincia"), rs.getString("nombre_municipio"), rs.getString("nombre_provincia"));
                municipios.add(municipio);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de los municipios:" + sw.toString());
        }finally{
      	   try{rs.close();}catch(Exception e){};
      	   try{ps.close();}catch(Exception e){};
      	   try{conn.close();}catch(Exception e){};
         }
        return municipios;
    }
    
    /**
     * Obtiene los municipios asociados a una determinada entidad. Se obtiene un conjunto (Set) que 
     * contiene Strings con los códigos de los municipios.
     * @param id_entidad
     * @return
     */
    public Set getMunicipiosEntidad(String id_entidad) {
        Set municipiosEntidad = new HashSet();
        Connection conn = null;
   	 	PreparedStatement ps = null;
   	 	ResultSet rs = null;
        try {
            Class.forName("com.geopista.sql.GEOPISTADriver");
            String sConn = "jdbc:pista:" + url + servletName;
            conn = DriverManager.getConnection(sConn);
            ps = conn.prepareStatement("getMunicipiosPorEntidad");
            ps.setString(1, id_entidad);
            rs = ps.executeQuery();
            Municipio municipio = null;
            while (rs.next()) {
                municipio = new Municipio(rs.getString ("id_municipio"), rs.getString("id_provincia"), rs.getString("nombre_municipio"), rs.getString("nombre_provincia"));
                municipiosEntidad.add(municipio);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos de los municipios de la entidad:" + sw.toString());
        }finally{
      	   try{rs.close();}catch(Exception e){};
      	   try{ps.close();}catch(Exception e){};
      	   try{conn.close();}catch(Exception e){};
         }
                       
        return municipiosEntidad;
    }
    
    /**
     * Inserta una nueva entidad supramunicipal
     * @param entidad
     * @return
     * @throws Exception
     */
    public CResultadoOperacion nuevaEntidad(Entidad entidad) throws Exception {
        CResultadoOperacion resultado = null; 
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(entidad);

            resultado = EnviarSeguro.enviar(url + servletNameNuevaEntidad, sw
                    .toString());
            System.out.println("Esto es lo que se envía: " + url + servletNameNuevaEntidad
                    + sw.toString());
            if (resultado.getResultado())
                entidad.setId(resultado.getDescripcion());            
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al crear la entidad: " + sw.toString());
            throw ex;
        }
        return resultado;
    }
    
    /**
     * Elimina una entidad supramunicipal
     * @param entidad
     * @return
     * @throws Exception
     */
    public CResultadoOperacion eliminarEntidad(Entidad entidad) throws Exception {
        CResultadoOperacion resultado = null; 
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(entidad);
            resultado = EnviarSeguro.enviar(url + servletNameEliminarEntidad, sw
                    .toString());
            System.out.println("Esto es lo que se envía: " + url + servletNameEliminarEntidad
                    + sw.toString());
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al eliminar la entidad: " + sw.toString());
            throw ex;
        }
        
        return resultado;
    }
    
    /**
     * Modifica una entidad supramunicipal
     * @param entidad
     * @return
     * @throws Exception
     */
    public CResultadoOperacion modificarEntidad(Entidad entidad) throws Exception {
        CResultadoOperacion resultado = null; 
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(entidad);
            resultado = EnviarSeguro.enviar(url + servletNameActualizarEntidad, sw
                    .toString());
            System.out.println("Esto es lo que se envía: " + url + servletNameActualizarEntidad
                    + sw.toString());
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al eliminar la entidad: " + sw.toString());
            throw ex;
        }
        
        return resultado;
    }
    
    /**
     * Inserta una nueva relación entidad-municipio
     * @param entidadMunicipio
     * @return
     * @throws Exception
     */
    public CResultadoOperacion nuevaRelacionEntidadMunicipio(EntidadMunicipio entidadMunicipio)
            throws Exception {
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(entidadMunicipio);

            CResultadoOperacion resultado = EnviarSeguro.enviar(url
                    + servletNameNuevoEntidadMunicipio, sw.toString());
            System.out.println("Esto es lo que se envía: " + url + servletNameNuevoEntidadMunicipio
                    + sw.toString());
            if (resultado.getResultado())
                entidadMunicipio.setId_entidad(resultado.getDescripcion());
            return resultado;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al crear rol: " + sw.toString());
            throw ex;
        }
    }
    
    /**
     * Elimina una relación entidad-municipio
     * @param entidadMunicipio
     * @return
     * @throws Exception
     */
    public CResultadoOperacion eliminarRelacionEntidadMunicipio(EntidadMunicipio entidadMunicipio)
            throws Exception {
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = new Marshaller(sw);
            marshaller.setEncoding("ISO-8859-1");
            marshaller.marshal(entidadMunicipio);

            CResultadoOperacion resultado = EnviarSeguro.enviar(url
                    + servletNameEliminarEntidadMunicipio, sw.toString());
            System.out.println("Esto es lo que se envía: " + url
                    + servletNameEliminarEntidadMunicipio + sw.toString());
            
            return resultado;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception al crear rol: " + sw.toString());
            throw ex;
        }
    }
	public void addTaskMonitor(TaskMonitorDialog taskMonitor) {
		this.taskMonitor=taskMonitor;
		
	}
	
	public static void registerGeopistaDrivers(){
		 
		try {
			String url = AppContext.getApplicationContext().getString("conexion.url");
			String driverClass = AppContext.getApplicationContext().getString("geopista.conexion.driver");
			Enumeration enDrivers = DriverManager.getDrivers();
			while(enDrivers.hasMoreElements()){
				DriverManager.deregisterDriver((java.sql.Driver)enDrivers.nextElement());
			}      
			java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();			
			DriverManager.registerDriver(driver);
			//DriverManager.setLogWriter(new PrintWriter((System.err)));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Hashtable<String, Operacion> getOperaciones(String idConexion, String sIdUsuario) throws Exception {
		Hashtable<String, Operacion> listaOperaciones = new Hashtable<String, Operacion>();	
		listaOperaciones.putAll(getOperacionesCapa(idConexion, sIdUsuario, 0));
		listaOperaciones.putAll(getOperacionesTabla(idConexion, sIdUsuario, listaOperaciones.size()));
		return listaOperaciones;
	}
	
	private Hashtable<String, Operacion> getOperacionesTabla(String idConexion, String sIdUsuario, int opCount) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.geopista.sql.GEOPISTADriver");
			String sConn = "jdbc:pista:" + url + servletName;
			conn = DriverManager.getConnection(sConn);

			ps = conn.prepareStatement("SM_TableOperationsBySession");
			ps.setString(1, idConexion);
			ps.setString(2, sIdUsuario);
			rs = ps.executeQuery();
			
			Hashtable<String, Operacion> listaOperaciones = new Hashtable<String, Operacion>();
			
			while (rs.next()) {
				Operacion op = new Operacion(Operacion.TIPO_TABLA);
				op.setFechaOperacion(rs.getTimestamp("fecha"));
				op.setCapaAfectada(rs.getString("id_table_versionada"));
				op.setOperacionRealizada(rs.getString("tipoCambio"));
				op.setRevision(rs.getInt("revision"));
				
				opCount++;
				String operationCounter = String.valueOf(opCount);
				if(opCount < 10)
					operationCounter = "0"+operationCounter;
				
				listaOperaciones.put(operationCounter, op);
			}

			return listaOperaciones;
			
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR :" + sw.toString());
			return null;
			
		} finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { ps.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { conn.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}
		
	}
	
	private Hashtable<String, Operacion> getOperacionesCapa(String idConexion, String sIdUsuario, int opCount) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.geopista.sql.GEOPISTADriver");
			String sConn = "jdbc:pista:" + url + servletName;
			conn = DriverManager.getConnection(sConn);

			ps = conn.prepareStatement("SM_LayerOperationsBySession");
			ps.setString(1, idConexion);
			ps.setString(2, sIdUsuario);
			rs = ps.executeQuery();
			
			Hashtable<String, Operacion> listaConexiones = new Hashtable<String, Operacion>();
			while (rs.next()) {
				
				Operacion op = new Operacion(Operacion.TIPO_CAPA);
				op.setMunicipio(rs.getString("municipio"));
				op.setIdFeature(rs.getString("feature"));
				op.setFechaOperacion(rs.getTimestamp("ts"));
				op.setCapaAfectada(rs.getString("layer"));
				op.setAccion(rs.getInt("action"));
				
				opCount++;
				String operationCounter = String.valueOf(opCount);
				if(opCount < 10)
					operationCounter = "0"+operationCounter;
				
				listaConexiones.put(operationCounter, op);
			}

			return listaConexiones;
			
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR :" + sw.toString());
			return null;
			
		} finally {
			try { rs.close(); } catch (Exception e) {logger.error("Error al cerrar rs: " + e.toString());}
			try { ps.close(); } catch (Exception e) {logger.error("Error al cerrar ps: " + e.toString());}
			try { conn.close(); } catch (Exception e) {logger.error("Error al cerrar conn: " + e.toString());}
		}	
	}
	
	public DetallesOperacion getDetallesOperacion(Operacion op, Locale locale) throws Exception {
		try {
			op.setLocale(locale.toString());
			
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.setEncoding("ISO-8859-1");
			marshaller.marshal(op);

			CResultadoOperacion resultado = EnviarSeguro.enviar(url+servletNameManageSesiones, sw.toString());
			System.out.println("Esto es lo que se envía: " + url+ servletNameManageSesiones + sw.toString());

			if(resultado.getResultado()){
				DetallesOperacion detallesObtenidos = (DetallesOperacion) resultado.getVector().get(0);
				
				return detallesObtenidos;
			}
			else{
				logger.error("Error al obtener detalles de una op: " + resultado.getDescripcion());
				throw new Exception();
			}
			
		} 
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			throw ex;
		}

	}
	
	//NUEVO
	public HashMap<String,String> getTranslate(int idVocable) throws Exception{
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet  r = null;
		try{
			 Class.forName("com.geopista.sql.GEOPISTADriver");
			 String sConn = "jdbc:pista:"+url+servletName;
	         conn = DriverManager.getConnection(sConn);	          
		     s = conn.prepareStatement("LMtraducciones");
		     s.setInt(1, idVocable);
		            
		     HashMap<String,String> hm = new HashMap<String,String>();
		         
		     r = s.executeQuery();  
		     while (r.next()){
		    	 hm.put(r.getString(1), r.getString(2));       
		     }  
		     return hm;
		} catch (Exception e) {
		     java.io.StringWriter sw=new java.io.StringWriter();
			 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
			 e.printStackTrace(pw);
		     logger.error("ERROR al recoger las traducciones:"+sw.toString());
		}finally{
		     try{r.close();}catch(Exception e){};
		     try{s.close();}catch(Exception e){};
		     try{conn.close();}catch(Exception e){};
		}		
		return null;
	}
	//FIN NUEVO

	
}

