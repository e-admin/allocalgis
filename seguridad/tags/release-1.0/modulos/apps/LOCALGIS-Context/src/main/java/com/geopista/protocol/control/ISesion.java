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

	public abstract String getIdMunicipio();

	public abstract void setIdMunicipio(String idMunicipio);
	
	public abstract Version getVersion(); 
	
	public abstract void setVersion(Version version); 
	
	public abstract void setEstadoValidacion(int estadoValidacion);

	public int getEstadoValidacion();
	
	public abstract void setLoadFeatureLayer(int loadFeatureLayers);
	
	public int getLoadFeatureLayer();

}