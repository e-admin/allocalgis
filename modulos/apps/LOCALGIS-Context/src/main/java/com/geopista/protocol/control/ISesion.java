/**
 * ISesion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.control;

import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.geopista.protocol.Version;
import com.geopista.security.GeopistaAcl;

public interface ISesion {

	public abstract Date getFechaConexion();

	public abstract void setFechaConexion(Date fechaConexion);

	public abstract String getIdUser();

	public abstract void setIdSesion(String idSesion);

	public abstract void setIdApp(String idApp);

	public abstract void setIdUser(String idUser);

	public abstract void setUserPrincipal(Principal userPrincipal);

	public abstract void setRoleGroup(Group roleGroup);

	public abstract String getIdSesion();

	public abstract Principal getUserPrincipal();

	public abstract Group getRoleGroup();

	public abstract String getIdApp();

	public abstract Vector getConjuncion(Vector vPermisos);

	public abstract List getAlMunicipios();

	public abstract void setAlMunicipios(List alMunicipios);

	public abstract String getIdEntidad();


	public abstract void setIdEntidad(String idEntidad);

	public abstract String getIdCurrentEntidad();

	public abstract void setIdCurrentEntidad(String idCurrentEntidad);
	
	public abstract String getIdMunicipio();

	public abstract void setIdMunicipio(String idMunicipio);
	public abstract String getIdCurrentMunicipio();

	public abstract void setIdCurrentMunicipio(String idCurrentMunicipio);
	
	public abstract GeopistaAcl getPerfil(long idAcl, Connection conn)
			throws java.lang.Exception;
	public abstract GeopistaAcl getPerfil(long idAcl)
			throws java.lang.Exception;
	public abstract Version getVersion(); 
	
	public abstract void setVersion(Version version); 
	
	public abstract void setEstadoValidacion(int estadoValidacion);

	public int getEstadoValidacion();
	
	public abstract void setLoadFeatureLayer(int loadFeatureLayers);
	
	public int getLoadFeatureLayer();
	public void setIP(String ip);
	public String getIP();

}