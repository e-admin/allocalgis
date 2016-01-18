/**
 * ListaSesiones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;

import com.geopista.server.database.CPoolDatabase;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 04-may-2004
 * Time: 15:04:05
 * To change this template use Options | File Templates.
 */
public class ListaSesiones implements Serializable {

	Hashtable<String, ISesion> lista = new Hashtable<String, ISesion>();

    public ListaSesiones() {
    }

    public Hashtable<String, ISesion> getLista() {
        return lista;
    }

    public void setLista(Hashtable<String, ISesion> lista) {
        this.lista = lista;
    }

    public boolean existeSesion(String sIdSesion)
    {
        return (lista.containsKey(sIdSesion));
    }
    public Sesion getSesion(String sIdSesion)
    {
        return (Sesion)lista.get(sIdSesion);
    }
    public synchronized void add(ISesion sSesion)
    {

		// En MODELO se verifica que dos usuarios no puedan acceder
		// desde la misma IP
    	/*String newUser=sSesion.getUserPrincipal().getName();
    	String newIP=sSesion.getIP();
    	try {
			for (Enumeration e=lista.elements();e.hasMoreElements();)
			 {
			     Sesion sesion=(Sesion) e.nextElement();
			 	 String oldUser=sesion.getUserPrincipal().getName();
				 String oldIP=sesion.getIP();
				 if (newUser!=null && newIP!=null){
					 if (oldUser!=null && oldIP!=null){
						 if (newUser.equals(oldUser) && (newIP.equals(oldIP))){
							 logger.info("El usuario "+newUser+" con IP:"+newIP+" ya existia en la lista de usuarios. Borrando usuario");
							 System.out.println("El usuario "+newUser+" con IP:"+newIP+" ya existia en la lista de usuarios. Borrando usuario");
							 PasarelaAdmcar.listaSesiones.delete(sesion);        	             
							 COperacionesControl.grabarLogout(sesion.getIdSesion());
						 }
					 }
				 }
			 }
		} catch (Exception e) {
			logger.error("Se ha producido un error al verificar la disponibilidad del usuario en la lista de sesiones",e);
		}*/
        lista.put(sSesion.getIdSesion(),sSesion);
    }
    public void delete(Sesion sSesion)
    {
        lista.remove(sSesion.getIdSesion());
    }
    public Object delete(String sIdSesion)
    {
            return lista.remove(sIdSesion);
    }

    public Sesion getSesion(String sUserName, String sIdApp)
    {
        Sesion auxSesion= getSesion(sUserName);
        if ((auxSesion!=null)&&(auxSesion.getIdApp().equalsIgnoreCase(sIdApp))) return auxSesion;
        for (Enumeration<ISesion> e=lista.elements();e.hasMoreElements();)
        {
            auxSesion= (Sesion)e.nextElement();
            try
            {
                 System.out.println("Evaluando:"+auxSesion.getUserPrincipal().getName());
                 if ((auxSesion.getUserPrincipal().getName().equalsIgnoreCase(sUserName))
                   && auxSesion.getIdApp().equals(sIdApp))
                 {
                     return auxSesion;
                 }
            }catch(Exception ex)
            {
                System.out.println("Excepcion"+ex.toString());
            }
        }
        return null;
    }
    public ListaSesionesSimple getListaSesionesSimple(String idMunicipio)
    {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = CPoolDatabase.getConnection();
			if (conn == null) {
				throw new Exception("No se puede obtener la conexiï¿½n");
			}

			String query = "select  distinct (iusercnt.id), iuseruserhdr.name,iusercnt.timestamp, appgeopista.def"
					+ " from iusercnt  LEFT JOIN iuseruserhdr ON iuseruserhdr.id = iusercnt.userid"
					+ " left join appgeopista on appgeopista.appid = iusercnt.appid"
					+ " LEFT JOIN session_app ON iusercnt.id = session_app.id where iusercnt.timeclose is null"
					+ " order by iusercnt.timestamp desc";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			Hashtable listaSimple = new Hashtable();
			while (rs.next()) {
				listaSimple.put(
						rs.getString("id"),
						new SesionSimple(rs.getString("id"), rs
								.getString("name"), rs.getString("def"), rs
								.getTimestamp("timestamp")));
			}

			return new ListaSesionesSimple(listaSimple);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}

		}
	}
  
}
