package com.geopista.protocol.administrador;

import java.util.Vector;
import java.util.Enumeration;

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
 * Time: 15:13:51
 */
public class Usuario implements java.io.Serializable, Cloneable{
    private String id;
    private String name;
    private String password;
    private String depID;
    private String email;
    private String nif;
    private String descripcion;
    private String nombreCompleto;
    private Vector grupos;
    private ListaPermisos permisos;
    private String id_entidad;
    private boolean bloqueado=false;
    private boolean passwordCambiado=true;
    private boolean userCambiado=true;
    private boolean permisoEdicionCompleta=true;

    public boolean isPermisoEdicionCompleta() {
		return permisoEdicionCompleta;
	}
	public void setPermisoEdicionCompleta(boolean permisoEdicionCompleta) {
		this.permisoEdicionCompleta = permisoEdicionCompleta;
	}
	public Usuario()
    {
         grupos = new Vector();
        permisos = new ListaPermisos();
    }
    public Usuario (String sId, String sName, String sPassword, String
                    sDepID, String sEmail, String sDescripcion, String sNombreCompleto,String nif, String id_entidad)
    {
        id=sId;
        name=sName;
        password=sPassword;
        depID=sDepID;
        email=sEmail;
        descripcion=sDescripcion;
        nombreCompleto=sNombreCompleto;
        this.nif=nif;
        grupos = new Vector();
        permisos = new ListaPermisos();
        this.id_entidad = id_entidad;

    }

    public Usuario (String sId, String sName, String sPassword, String
            sDepID, String sEmail, String sDescripcion, String sNombreCompleto,String nif, String id_entidad, boolean bloqueado)
		{
		id=sId;
		name=sName;
		password=sPassword;
		depID=sDepID;
		email=sEmail;
		descripcion=sDescripcion;
		nombreCompleto=sNombreCompleto;
		this.nif=nif;
		grupos = new Vector();
		permisos = new ListaPermisos();
		this.id_entidad = id_entidad;
		this.bloqueado = bloqueado;
		
		}    
    public Usuario (String sId, String sName, String sPassword, String
                    sDepID, String sEmail, String sDescripcion, String sNombreCompleto, String id_entidad,
                    Vector vGrupos, ListaPermisos vPermisos)
    {
        id=sId;
        name=sName;
        password=sPassword;
        depID=sDepID;
        email=sEmail;
        descripcion=sDescripcion;
        nombreCompleto=sNombreCompleto;
        grupos = vGrupos;
        permisos = vPermisos;
        this.id_entidad = id_entidad;
    }

    public Usuario(String sId, String sName, String sPassword, String
            sDepID, String sEmail, String sDescripcion, String sNombreCompleto, String id_entidad,
            Vector vGrupos, ListaPermisos vPermisos, boolean bloqueado, boolean passwordCambiado, boolean permisoEdicionCompleta) {
    	id=sId;
        name=sName;
        password=sPassword;
        depID=sDepID;
        email=sEmail;
        descripcion=sDescripcion;
        nombreCompleto=sNombreCompleto;
        grupos = vGrupos;
        permisos = vPermisos;
        this.id_entidad = id_entidad;
        this.bloqueado = bloqueado;
        this.passwordCambiado = passwordCambiado;
        this.permisoEdicionCompleta = permisoEdicionCompleta;
    }
	
    public boolean isPasswordCambiado() {
		return passwordCambiado;
	}
	public void setPasswordCambiado(boolean passwordCambiado) {
		this.passwordCambiado = passwordCambiado;
	}
    public boolean isUserCambiado() {
		return userCambiado;
	}
	public void setUserCambiado(boolean userCambiado) {
		this.userCambiado = userCambiado;
	}

	
	public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getDepID() {
        return depID;
    }

    public String getEmail() {
        return email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Vector getGrupos() {
        return grupos;
    }

    public ListaPermisos getPermisos() {
        return permisos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepID(String depID) {
        this.depID = depID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setGrupos(Vector grupos) {
        this.grupos = grupos;
    }

    public void setPermisos(ListaPermisos permisos) {
        this.permisos = permisos;
    }
    public void addPermiso(Permiso permiso)
    {
        permisos.add(permiso);
    }
     public void addGrupo(String sIdGrupo)
    {
        grupos.add(sIdGrupo);
    }

    public boolean containsGrupo(String sIdGrupo)
    {
        return grupos.contains(sIdGrupo);
    }
     public Object clone()
    {
        Usuario obj=null;
        try{
             obj=(Usuario)super.clone();
             obj.setPermisos((ListaPermisos)this.getPermisos().clone());
             obj.setGrupos((Vector)this.getGrupos().clone());
         }catch(CloneNotSupportedException ex){
          }
         return obj;
    }
    public boolean tienePermisoRol(String sIdPermiso, String sIdAcl, ListaRoles listaRoles)
    {
        for (Enumeration e=this.getGrupos().elements();e.hasMoreElements();)
        {
            String sIdRol= (String)e.nextElement();
            try { if (listaRoles.get(sIdRol).containsPermiso(sIdPermiso,sIdAcl))
                     return true;
            }catch(Exception ex){}
        }
        return false;
    }
    public StringBuffer printGrupos()
    {
        StringBuffer auxBuffer= new StringBuffer();
        auxBuffer.append("---------------GRUPOS--------------\n");
        for (Enumeration e=this.getGrupos().elements();e.hasMoreElements();)
         {
                   auxBuffer.append((String)e.nextElement()+"\n");
        }
        auxBuffer.append("------------FIN GRUPOS--------------\n");
        return auxBuffer;
    }
	
    
	public void setNif(String nif) {
		this.nif=nif;
		
	}
	public String getNif() {
		return nif;
	}
    
    /**
     * @return the id_entidad
     */
    public String getId_entidad() {
        return id_entidad;
    }
    
    /**
     * @param id_entidad the id_entidad to set
     */
    public void setId_entidad(String id_entidad) {
        this.id_entidad = id_entidad;
    }
    public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
    public boolean isBloqueado(){
    	return bloqueado;
    }
}
