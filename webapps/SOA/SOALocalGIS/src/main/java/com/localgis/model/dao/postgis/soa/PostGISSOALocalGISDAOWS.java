/**
 * PostGISSOALocalGISDAOWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao.postgis.soa;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.codehaus.xfire.MessageContext;
import org.mortbay.util.Credential;
import com.ibatis.dao.client.DaoException;
import com.localgis.exception.AclNoExistenteException;
import com.localgis.exception.ColumnsNotFoundException;
import com.localgis.exception.LayersNotFoundException;
import com.localgis.exception.MunicipiosNotFoundException;
import com.localgis.exception.NoPermisoException;
import com.localgis.exception.PasswordNoValidoException;
import com.localgis.exception.PeticionNumeroErroneaException;
import com.localgis.exception.PeticionViaErroneaException;
import com.localgis.exception.PoiExistenteException;
import com.localgis.exception.PoiNoExistenteException;
import com.localgis.exception.SubtipoNoExistenteException;
import com.localgis.exception.TipoNoExistenteException;
import com.localgis.exception.URLNoExistenteException;
import com.localgis.exception.URLReportMapNotFoundException;
import com.localgis.exception.UsuarioNoExistenteException;
import com.localgis.model.dao.ISOALocalGISDAOWS;
import com.localgis.model.ot.BienInmuebleOT;
import com.localgis.model.ot.BienPreAltaOT;
import com.localgis.model.ot.CalleOT;
import com.localgis.model.ot.CapaOT;
import com.localgis.model.ot.ConstruccionOT;
import com.localgis.model.ot.CultivoOT;
import com.localgis.model.ot.EntidadOT;
import com.localgis.model.ot.MunicipioOT;
import com.localgis.model.ot.NumeroOT;
import com.localgis.model.ot.NumeroPolicia;
import com.localgis.model.ot.ParcelaOT;
import com.localgis.model.ot.PoiOT;
import com.localgis.model.ot.ProvinciaOT;
import com.localgis.model.ot.SubtipoOT;
import com.localgis.model.ot.TipoViaOT;
import com.localgis.model.ot.URLsPlano;
import com.localgis.model.ot.Via;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.DomainNode;
import com.localgis.util.EncriptarPassword;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderFactory;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.Point;
import com.localgis.web.core.model.Scale;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.vividsolutions.jump.util.java2xml.Java2XML;

public class PostGISSOALocalGISDAOWS implements ISOALocalGISDAOWS
{	
	private static LocalgisManagerBuilder localgisManagerBuilder = null;
	private static String SRID_ENTRADA = "23030";
	private static String SRID_SALIDA = "4230";
	    
    public PostGISSOALocalGISDAOWS() throws NamingException
    {
        Context initCtx = new InitialContext( );
    }
     
    private LocalgisManagerBuilder getLocalgisManagerBuilder() 
    throws LocalgisConfigurationException, LocalgisInitiationException{
    	
    	if (localgisManagerBuilder == null){
    		
    		localgisManagerBuilder = LocalgisManagerBuilderFactory.createLocalgisManagerBuilder();
    	}
    	
    	return localgisManagerBuilder;
    }
        
    public URLsPlano verPlanoPorCoordenadas(Connection connection, int idPlano,
    		double coordX, double coordY, int alturaPlano,
    		int anchoPlano, int escala, int idEntidad) throws SQLException,
    		ParseException, LocalgisInvalidParameterException, LocalgisDBException, 
    		LocalgisConfigurationException, LocalgisInitiationException, 
    		LocalgisMapNotFoundException, URLNoExistenteException {

    	int idPlanoPublicado = 0;
    	boolean mapaPublicado = false; 
    	
    	LocalgisMapsConfigurationManager guiaUrbanaManager = getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager();
 
    	List localgisPublishedMapsList = (List) getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getPublishedMaps(new Integer(idEntidad));
    	if (localgisPublishedMapsList!=null){
    		for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
    			LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
    			if (localgisMap.getMapidgeopista().intValue() == idPlano){
    				idPlanoPublicado = localgisMap.getMapid();  
    				mapaPublicado = true;
    				break;
    			}    		
    		}
    	}
    	else{
    		throw new LocalgisMapNotFoundException("No hay mapas publicados");
    	}
    	
    	if (!mapaPublicado){
			throw new LocalgisMapNotFoundException("El mapa con el id " + idPlano + " y con el id de Entidad " + idEntidad + ", no está publicado ");
		}
    	
    	String[] urlMap = null;
    
    	urlMap = localgisManagerBuilder.getLocalgisURLsManager().getURLMapByXAndY(idPlanoPublicado, coordX, coordY, new Scale(1, escala), alturaPlano, anchoPlano);
    	
    	URLsPlano urlsPlano = new URLsPlano();
    	
    	if (urlMap != null ){
    		if (urlMap.length > 0){
    			urlsPlano.setUrlMapServer(urlMap[0]);
    			if (urlMap.length > 1){
    				urlsPlano.setUrlGuiaUrbana(urlMap[1]);
    			}
    		}
    	}
    	else{
    		throw new URLNoExistenteException("No existe la imagen solicitada " +
    				"o no puede ser devuelta");
    	}    	

    	return urlsPlano;
    }
        
    public URLsPlano verPlanoPorReferenciaCatastral(Connection connection, int idPlano,
    		String refCatastral, int alturaPlano,
    		int anchoPlano, int escala, int idEntidad) throws SQLException,
    		ParseException, LocalgisInvalidParameterException, LocalgisDBException, 
    		LocalgisConfigurationException, LocalgisInitiationException, 
    		LocalgisMapNotFoundException, URLNoExistenteException {

    	int idPlanoPublicado = 0;
    	boolean mapaPublicado = false; 
 
    	LocalgisMapsConfigurationManager guiaUrbanaManager = getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager();
    	
    	List localgisPublishedMapsList = (List) getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getPublishedMaps(new Integer(idEntidad));
    	if (localgisPublishedMapsList!=null){
    		for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
    			LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
    			if (localgisMap.getMapidgeopista().intValue() == idPlano){
    				idPlanoPublicado = localgisMap.getMapid();  
    				mapaPublicado = true;
    				break;
    			}    		
    		}
    	}
    	else{
    		throw new LocalgisMapNotFoundException("No hay mapas publicados");
    	}
    	
    	if (!mapaPublicado){
			throw new LocalgisMapNotFoundException("El mapa con el id " + idPlano + " y con el id de Entidad " + idEntidad + ", no está publicado ");
		}
    	
    	String[] urlMap = null;

    	urlMap = localgisManagerBuilder.getLocalgisURLsManager().getURLMapByReferenciaCatastral(idPlanoPublicado, refCatastral, new Scale(1, escala), alturaPlano, anchoPlano);
    	
    	URLsPlano urlsPlano = new URLsPlano();
    	
    	if (urlMap != null ){
    		if (urlMap.length > 0){
    			urlsPlano.setUrlMapServer(urlMap[0]);
    			if (urlMap.length > 1){
    				urlsPlano.setUrlGuiaUrbana(urlMap[1]);
    			}
    		}
    	}
    	else{
    		throw new URLNoExistenteException("No existe la imagen solicitada " +
    				"o no puede ser devuelta");
    	}    	 
    	
    	return urlsPlano;
    }
        
    public URLsPlano verPlanoPorIdVia(Connection connection, int idPlano,
    		int idVia, int alturaPlano,
    		int anchoPlano, int escala, int idEntidad) throws SQLException,
    		ParseException, LocalgisInvalidParameterException, LocalgisDBException, 
    		LocalgisConfigurationException, LocalgisInitiationException, 
    		LocalgisMapNotFoundException, URLNoExistenteException {

    	int idPlanoPublicado = 0;
    	boolean mapaPublicado = false; 
 
    	LocalgisMapsConfigurationManager guiaUrbanaManager = getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager();
   	 
    	List localgisPublishedMapsList = (List) getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getPublishedMaps(new Integer(idEntidad));
    	if (localgisPublishedMapsList!=null){
    		for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
    			LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
    			if (localgisMap.getMapidgeopista().intValue() == idPlano){
    				idPlanoPublicado = localgisMap.getMapid();  
    				mapaPublicado = true;
    				break;
    			}    		
    		}
    	}
    	else{
    		throw new LocalgisMapNotFoundException("No hay mapas publicados");
    	}
    	
    	if (!mapaPublicado){
			throw new LocalgisMapNotFoundException("El mapa con el id " + idPlano + " y con el id de Entidad " + idEntidad + ", no está publicado ");
		}
    	
    	String[] urlMap = null;

    	urlMap = localgisManagerBuilder.getLocalgisURLsManager().getURLMapByIdVia(idPlanoPublicado, idVia, new Scale(1, escala), alturaPlano, anchoPlano);

    	URLsPlano urlsPlano = new URLsPlano();
    	
    	if (urlMap != null ){
    		if (urlMap.length > 0){
    			urlsPlano.setUrlMapServer(urlMap[0]);
    			if (urlMap.length > 1){
    				urlsPlano.setUrlGuiaUrbana(urlMap[1]);
    			}
    		}
    	}
    	else{
    		throw new URLNoExistenteException("No existe la imagen solicitada " +
    				"o no puede ser devuelta");
    	}    	 
    	
    	return urlsPlano;
    }
    
    public URLsPlano verPlanoPorIdNumeroPolicia(Connection connection, int idPlano,
    		int idNumeroPolicia, int alturaPlano,
    		int anchoPlano, int escala, int idEntidad) throws SQLException,
    		ParseException, LocalgisInvalidParameterException, LocalgisDBException, 
    		LocalgisConfigurationException, LocalgisInitiationException, 
    		LocalgisMapNotFoundException, URLNoExistenteException {

    	int idPlanoPublicado = 0;
    	boolean mapaPublicado = false; 
 
    	LocalgisMapsConfigurationManager guiaUrbanaManager = getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager();
   	 
    	List localgisPublishedMapsList = (List) getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getPublishedMaps(new Integer(idEntidad));
    	if (localgisPublishedMapsList!=null){
    		for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
    			LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
    			if (localgisMap.getMapidgeopista().intValue() == idPlano){
    				idPlanoPublicado = localgisMap.getMapid();  
    				mapaPublicado = true;
    				break;
    			}    		
    		}
    	}
    	else{
    		throw new LocalgisMapNotFoundException("No hay mapas publicados");
    	}
    	
    	if (!mapaPublicado){
			throw new LocalgisMapNotFoundException("El mapa con el id " + idPlano + " y con el id de Entidad " + idEntidad + ", no está publicado ");
		}
    	
    	String[] urlMap = null;

    	urlMap = localgisManagerBuilder.getLocalgisURLsManager().getURLMapByIdNumeroPolicia(idPlanoPublicado, idNumeroPolicia, new Scale(1, escala), alturaPlano, anchoPlano);

    	URLsPlano urlsPlano = new URLsPlano();
    	
    	if (urlMap != null ){
    		if (urlMap.length > 0){
    			urlsPlano.setUrlMapServer(urlMap[0]);
    			if (urlMap.length > 1){
    				urlsPlano.setUrlGuiaUrbana(urlMap[1]);
    			}
    		}
    	}
    	else{
    		throw new URLNoExistenteException("No existe la imagen solicitada " +
    				"o no puede ser devuelta");
    	}    	 
    	
    	return urlsPlano;
    }
    
    public Collection verPlanosPublicados(Connection connection, 
    		int idEntidad) throws LocalgisConfigurationException, 
    		LocalgisInitiationException, LocalgisInvalidParameterException, 
    		LocalgisDBException, LocalgisMapNotFoundException 
    		{

    	ArrayList listaMapasPublicados = new ArrayList();

    	listaMapasPublicados = (ArrayList)getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getPublishedMaps(new Integer(idEntidad));
    	
    	if ( listaMapasPublicados==null || listaMapasPublicados.size()<=0){    	
    		throw new LocalgisMapNotFoundException("No hay mapas publicados");
    	}    	  
    	
    	return listaMapasPublicados;
    }
    
    public HashMap getURLReportMap(Connection connection,String imageKey, String idMap, String table, String column,
    		Object selectionId, String scale, int height, int width, String style, String idEntidad, String publicMap, String layerName) 
    	throws URLReportMapNotFoundException, NumberFormatException, LocalgisInvalidParameterException, 
    	LocalgisConfigurationException, LocalgisDBException, LocalgisInitiationException 
    		{
    	
    	HashMap urlReportMap=null;
    	//TODO. Ver implementacion
    	/*HashMap urlReportMap = getLocalgisManagerBuilder().getLocalgisURLsManager().getURLReportMap(idMap, table, column,
    		selectionId,scale, height, width, style, new Integer(idMunicipio), new Boolean(publicMap), layerName);*/
    	
    	return urlReportMap;
    }
	
    public Collection verMunicipiosPublicados(Connection connection) throws LocalgisDBException, LocalgisConfigurationException, 
    LocalgisInitiationException, MunicipiosNotFoundException {

    	ArrayList listaMunicipiosPublicados = new ArrayList();

    	//TODO. Ver implementacion actual
    	//listaMunicipiosPublicados = (ArrayList)getLocalgisManagerBuilder().getLocalgisMapsConfigurationManager().getMunicipios();
    	
    	if ( listaMunicipiosPublicados==null || listaMunicipiosPublicados.size()<=0){    	
    		throw new MunicipiosNotFoundException("No hay municipios publicados");
    	}    	  
    	
    	return listaMunicipiosPublicados;
    }
    
    public Collection selectLayersByIdMap(Connection connection, Integer idMap) throws LayersNotFoundException,
    	LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException {

    	ArrayList listaLayers = new ArrayList();

    	listaLayers = (ArrayList)getLocalgisManagerBuilder().getLocalgisMapManager().getMapLayers(idMap);
    	
    	if ( listaLayers==null || listaLayers.size()<=0){    	
    		throw new LayersNotFoundException("No hay capas en el mapa");
    	}    	  
    	
    	return listaLayers;
    }
    
    public Collection selectColumnsBylayerTranslated(Connection connection, Integer idLayer, String locale) 
    	throws ColumnsNotFoundException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException {

    	ArrayList listaColumns = new ArrayList();

    	listaColumns = (ArrayList)getLocalgisManagerBuilder().getLocalgisLayerManager().getColumnsLayer(idLayer, locale);

    	if ( listaColumns==null || listaColumns.size()<=0){    	
    		throw new ColumnsNotFoundException("No hay columnas en la capa");
    	}    	  

    	return listaColumns;
    }
    
    public String comprobarPermiso(Connection connection, Integer idUsuario, String constPermiso) throws SQLException, UsuarioNoExistenteException, PasswordNoValidoException, NumberFormatException, AclNoExistenteException, NoPermisoException {

		ArrayList listaPermisos = null;

		if (idUsuario!=null){			

			listaPermisos = obtenerPermisosUsuario(connection, idUsuario);
		}

		for (Iterator iterPermisos = listaPermisos.iterator(); iterPermisos.hasNext();){
			String permiso = (String)iterPermisos.next();
			if(constPermiso.equals(permiso))
				return new String("El usuario tiene el permiso " + constPermiso);
		}       

		throw new NoPermisoException("El usuario no tiene el permiso " + constPermiso);
	}
		
	public ArrayList obtenerPermisosUsuario(Connection connection, Integer idUsuario) throws SQLException, AclNoExistenteException {

		int idACL = 0; 
		ArrayList listaPermisos = new ArrayList();

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		try{
			
			String senteciaSQL = "select def from usrgrouperm where idperm in " +
					"(select idperm from r_group_perm,iusergroupuser,acl where " +
					"r_group_perm.GROUPID = iusergroupuser.GROUPID and " +
					"iusergroupuser.userid=? and r_group_perm.idacl = acl.idacl) " +
					"and idperm not in (select idperm from r_usr_perm where " +
					"r_usr_perm.userid=? and r_usr_perm.aplica=0) or idperm in " +
					"(select idperm from r_usr_perm, acl where r_usr_perm.userid=? " +
					"and r_usr_perm.idacl = acl.idacl and (r_usr_perm.aplica<>0 or " +
					"r_usr_perm.aplica is null))";
						
			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setInt(1, idUsuario.intValue());
			preparedStatement.setInt(2, idUsuario.intValue());
			preparedStatement.setInt(3, idUsuario.intValue());
			
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String permiso = rs.getString("def");
				listaPermisos.add(permiso);
			}        

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		return listaPermisos;
	}
	
	public Boolean validarReferencia(Connection connection,
			String refCatastral) throws SQLException {
		
		Boolean validate = null;
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{    		
    		String senteciaSQL = "select referencia_catastral from parcelas where referencia_catastral=?";
    		preparedStatement = connection.prepareStatement(senteciaSQL);  
    		preparedStatement.setString(1, refCatastral);
    		rs = preparedStatement.executeQuery();
    		
    		if (rs.next()){
    			validate = new Boolean(true);
			}
    		else{
    			validate = new Boolean(false);
    		}

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}
    	return validate;
	}

	public Collection consultarCatastro(Connection connection, String refCatastral)
			throws SQLException {
		
		ArrayList listaParcelas = new ArrayList();
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		
    		String sentenciaSQL = "select parcelas.codigo_postal, parcelas.referencia_catastral, " +
    				"parcelas.superficie_finca, parcelas.superficie_construida_total, " +
    				"parcelas.primer_numero, parcelas.primera_letra, " +
    				"parcelas.segundo_numero, parcelas.segunda_letra, parcelas.id_municipio," +
    				"parcelas.kilometro, parcelas.bloque, municipios.nombreoficial " +
    				"as nombreoficial_municipio, provincias.nombreoficial " +
    				"as nombreoficial_provincia, vias.nombreviaine, vias.nombrecatastro " +
    				"from parcelas left join municipios on " +
    				"parcelas.id_municipio=municipios.id left join provincias on " +
    				"parcelas.codigo_provinciaine=provincias.id left join vias on " +
    				"parcelas.id_via=vias.codigocatastro and " +
    				"parcelas.id_municipio=vias.id_municipio where " +
    				"parcelas.referencia_catastral=?"; 
    		
    		preparedStatement = connection.prepareStatement(sentenciaSQL);  
    		preparedStatement.setString(1, refCatastral);
    		rs = preparedStatement.executeQuery();
    		
    		while (rs.next()) {
    			
    			ParcelaOT parcela = new ParcelaOT();
    			
    			parcela.getDireccion().setBloque(rs.getString("bloque"));
    			parcela.getDireccion().setCodigoPostal(new Integer(rs.getInt("codigo_postal")));
    			parcela.getDireccion().setKilometro(new Double(rs.getDouble("kilometro")));
    			parcela.getDireccion().setPrimeraLetra(rs.getString("primera_letra"));
    			parcela.getDireccion().setPrimerNumero(new Integer(rs.getInt("primer_numero")));
    			parcela.getDireccion().setSegundaLetra(rs.getString("segunda_letra"));
    			parcela.getDireccion().setSegundoNumero(new Integer(rs.getInt("segundo_numero")));
    			parcela.getDireccion().setNombreMunicipio(rs.getString("nombreoficial_municipio"));
    			parcela.getDireccion().setNombreProvincia(rs.getString("nombreoficial_provincia"));
    			
    			String nombreVia = rs.getString("nombreviaine");
    			if (nombreVia != null && !nombreVia.equals("")){
    				parcela.getDireccion().setNombreVia(nombreVia);
    			}
    			else{
    				nombreVia = rs.getString("nombrecatastro");
    				if (nombreVia != null && !nombreVia.equals("")){
        				parcela.getDireccion().setNombreVia(nombreVia);
    				}
    			}
    			   			
    			parcela.setRefCatastral(rs.getString("referencia_catastral"));
    			parcela.setSuperficie(new Double(rs.getDouble("superficie_finca")));
    			parcela.setSuperficieConstruida(new Double(rs.getDouble("superficie_construida_total")));
    			
    			Integer idMunicipio = new Integer(rs.getInt("id_municipio"));
    			parcela.setLstBienesInmuebles((ArrayList) obtenerBienesInmuebles(connection, refCatastral, idMunicipio));
    			
				listaParcelas.add(parcela);
			}    

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}
    	return listaParcelas;
	}
	
	public Collection obtenerBienesInmuebles(Connection connection, String refCatastral, Integer idMunicipio) throws SQLException{
		
		ArrayList listaBienesInmuebles = new ArrayList();
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		
    		String sentenciaSQL = "select bien_inmueble.identificador, bien_inmueble.bloque, bien_inmueble.codigo_postal, " +
    				"bien_inmueble.kilometro, bien_inmueble.primera_letra, bien_inmueble.segunda_letra, " +
    				"bien_inmueble.primer_numero, bien_inmueble.segundo_numero, bien_inmueble.clase_bieninmueble, " +
    				"bien_inmueble.superficie_finca, bien_inmueble.numero_cargo," +
    				"bien_inmueble.clave_uso_dgc, municipios.nombreoficial as nombreoficial_municipio, " +
    				"provincias.nombreoficial as nombreoficial_provincia, vias.nombreviaine, vias.nombrecatastro " +
    				"from bien_inmueble left join municipios on municipios.id=? " +
    				"left join provincias on municipios.id_provincia=provincias.id " +
    				"left join vias on bien_inmueble.id_via=vias.codigocatastro and " +
    				"vias.id_municipio=? where bien_inmueble.parcela_catastral=?"; 
    		
    		preparedStatement = connection.prepareStatement(sentenciaSQL);  
    		preparedStatement.setInt(1, idMunicipio.intValue());
    		preparedStatement.setInt(2, idMunicipio.intValue());
    		preparedStatement.setString(3, refCatastral);
    		rs = preparedStatement.executeQuery();
    		
    		while (rs.next()) {
    			
    			BienInmuebleOT bienInmueble = new BienInmuebleOT();
    			
    			bienInmueble.getDireccionLocalizacion().setBloque(rs.getString("bloque"));
    			bienInmueble.getDireccionLocalizacion().setCodigoPostal(new Integer(rs.getInt("codigo_postal")));
    			bienInmueble.getDireccionLocalizacion().setKilometro(new Double(rs.getDouble("kilometro")));
    			bienInmueble.getDireccionLocalizacion().setPrimeraLetra(rs.getString("primera_letra"));
    			bienInmueble.getDireccionLocalizacion().setPrimerNumero(new Integer(rs.getInt("primer_numero")));
    			bienInmueble.getDireccionLocalizacion().setSegundaLetra(rs.getString("segunda_letra"));
    			bienInmueble.getDireccionLocalizacion().setSegundoNumero(new Integer(rs.getInt("segundo_numero")));
    			bienInmueble.getDireccionLocalizacion().setNombreMunicipio(rs.getString("nombreoficial_municipio"));
    			bienInmueble.getDireccionLocalizacion().setNombreProvincia(rs.getString("nombreoficial_provincia"));
    			
    			String nombreVia = rs.getString("nombreviaine");
    			if (nombreVia != null && !nombreVia.equals("")){
    				bienInmueble.getDireccionLocalizacion().setNombreVia(nombreVia);
    			}
    			else{
    				nombreVia = rs.getString("nombrecatastro");
    				if (nombreVia != null && !nombreVia.equals("")){
    					bienInmueble.getDireccionLocalizacion().setNombreVia(nombreVia);
    				}
    			}
    			   			
    			bienInmueble.setReferencia_catastral(rs.getString("identificador"));
    			bienInmueble.setSuperficie(new Double(rs.getDouble("superficie_finca")));
    			
    			String numeroOrdenBienInmueble = rs.getString("numero_cargo");
    			if (numeroOrdenBienInmueble!=null){
    				bienInmueble.setLstConstrucciones((ArrayList) obtenerConstrucciones(connection, refCatastral, numeroOrdenBienInmueble));
    				bienInmueble.setLstCultivos((ArrayList) obtenerCultivos(connection, refCatastral, numeroOrdenBienInmueble));
    			}
    			
				listaBienesInmuebles.add(bienInmueble);
			}    

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}
    	return listaBienesInmuebles;

	}
	
	public Collection obtenerConstrucciones(Connection connection, String refCatastral, String numeroOrdenBienInmueble) throws SQLException{
		
		ArrayList listaConstrucciones = new ArrayList();
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		
    		String sentenciaSQL = "select construccion.codigo_uso_predominante, " +
    				"construccion.escalera, construccion.planta, " +
    				"construccion.puerta, construccion.superficie_total_local " +
    				"where construccion.parcela_catastral=? " +
    				"and construccion.numero_orden_bieninmueble=?"; 
    		
    		preparedStatement = connection.prepareStatement(sentenciaSQL);  
    		preparedStatement.setString(1, refCatastral);
    		preparedStatement.setString(2, numeroOrdenBienInmueble);
    		rs = preparedStatement.executeQuery();
    		
    		while (rs.next()) {
    			
    			ConstruccionOT construccion = new ConstruccionOT();
    			
    			construccion.setCodigoUso(rs.getString("codigo_uso_predominante"));
    			construccion.setEscalera(rs.getString("escalera"));
    			construccion.setPlanta(rs.getString("planta"));
    			construccion.setPuerta(rs.getString("puerta"));
    			construccion.setSuperficieTotal(rs.getDouble("superficie_total_local"));
    			    			
				listaConstrucciones.add(construccion);
			}    

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}
    	return listaConstrucciones;

	}
	
	public Collection obtenerCultivos(Connection connection, String refCatastral, String numeroOrdenBienInmueble) throws SQLException{
		
		ArrayList listaCultivos = new ArrayList();
		ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		
    		String sentenciaSQL = "select cultivos.calificacion_cultivo, " +
    				"cultivos.denominacion_cultivo, cultivos.intensidad_productiva, " +
    				"cultivos.superficie_subparcela, cultivos.identificador where " +
    				"cultivos.parcela_catastral=? and cultivos.numero_orden=?"; 
    		
    		preparedStatement = connection.prepareStatement(sentenciaSQL);  
    		preparedStatement.setString(1, refCatastral);
    		preparedStatement.setString(2, numeroOrdenBienInmueble);
    		rs = preparedStatement.executeQuery();
    		
    		while (rs.next()) {
    			
    			CultivoOT cultivo = new CultivoOT();
    			
    			cultivo.setCalificacion(rs.getString("calificacion_cultivo"));
    			cultivo.setDenominacion(rs.getString("denominacion_cultivo"));
    			cultivo.setIntensidadProductiva(new Integer(rs.getInt("intensidad_productiva")));
    			cultivo.setSuperficie(new Double(rs.getDouble("superficie_subparcela")));
    			cultivo.setIdentificador(rs.getString("identificador"));
    			    			
				listaCultivos.add(cultivo);
			}    

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}
    	return listaCultivos;

	}
	
	public Collection obtenerTiposDeVia(Connection connection) throws SQLException{
		
		ArrayList lstTipoVia = new ArrayList();
		TipoViaOT tipoVia = null;
		
		String locale = System.getProperties().get("user.language").toString().toLowerCase() + "_" + System.getProperties().get("user.language").toString().toUpperCase();
		Hashtable lista = obtenerListaDominios(connection, "Tipo de vía", 0, 4);
    	
    	for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
    		DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();
            	tipoVia = new TipoViaOT();
            	tipoVia.setPatron(tipoDomainNode.getPatron());
            	
            	if (tipoDomainNode.gethDict().get(locale)!=null)
            		tipoVia.setDescripcion((String)tipoDomainNode.gethDict().get(locale));
            	else
            		tipoVia.setDescripcion((String)tipoDomainNode.gethDict().elements().nextElement());
            	
            	lstTipoVia.add(tipoVia);
            }
        }
    	
    	return lstTipoVia;
		
	}
	
	public Collection obtenerListaCapas(Connection connection, int idMunicipio) throws SQLException, ParseException {

    	ArrayList listaCapas = new ArrayList();
    	CapaOT capaoOT = null;
    	SubtipoOT subtipoOT = null;
    	String locale = System.getProperties().get("user.language").toString().toLowerCase() + "_" + System.getProperties().get("user.language").toString().toUpperCase();
    	
    	Hashtable lista = obtenerListaDominios(connection, "Pois", idMunicipio, 2);
    	    	
    	for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();
            	capaoOT = new CapaOT();
            	capaoOT.setPatron(tipoDomainNode.getPatron());
            	
            	if (tipoDomainNode.gethDict().get(locale)!=null)
            		capaoOT.setNombreCapa((String)tipoDomainNode.gethDict().get(locale));
            	else
            		capaoOT.setNombreCapa((String)tipoDomainNode.gethDict().elements().nextElement());
            	
            	for (Enumeration enumSubtipoDomain = tipoDomainNode.getlHijos().gethDom().elements();enumSubtipoDomain.hasMoreElements();){            		
            		subtipoOT = new SubtipoOT();
            		DomainNode subtipoDomainNode=(DomainNode) enumSubtipoDomain.nextElement();
            		subtipoOT.setPatron(subtipoDomainNode.getPatron());
            		
            		if (subtipoDomainNode.gethDict().get(locale)!=null)
            			subtipoOT.setNombreSubtipo((String)subtipoDomainNode.gethDict().get(locale));
            		else
            			subtipoOT.setNombreSubtipo((String)subtipoDomainNode.gethDict().elements().nextElement());
            		
            		capaoOT.getSubtipos().add(subtipoOT);
            	}
            	listaCapas.add(capaoOT);            	
            }
        }
    	
    	return listaCapas;

    }

	
    public String bajaPOI(Connection connection, int idContenido) throws SQLException,
    		ParseException, PoiNoExistenteException{

    	if(!comprobarExistenciaPoi(connection, idContenido))
    		throw new PoiNoExistenteException("El poi con id "+ idContenido + " no existe");

    	ResultSet rs = null;
    	PreparedStatement preparedStatement = null;

    	try
    	{
    		//escribir query 
    		String senteciaSQL = "delete from pois where id_contenido = ?";
    		preparedStatement = connection.prepareStatement(senteciaSQL);  
    		preparedStatement.setInt(1, idContenido);

    		preparedStatement.executeUpdate();

    	}finally
    	{
    		ConnectionUtilities.closeConnection(null, preparedStatement, null);
    	}

    	String respuesta = "Se ha eliminado correctamente el poi con id " + idContenido;
    	return respuesta;
    }

    public Hashtable obtenerListaDominios(Connection connection, String dominioPadre, int idMunicipio, int tipo) throws SQLException
	{
		Hashtable listaDominios = new Hashtable();
		DomainNode dominio = obtenerDominioPadre(connection, dominioPadre, idMunicipio, tipo);

		if(dominio!=null && dominio.getIdNode()!=null){
			listaDominios.put(dominio.getIdNode(),dominio);    
		}
		return listaDominios;       
	}

	public DomainNode obtenerDominiosHijo(Connection connection, DomainNode dominioPadre, int idMunicipio) throws SQLException
	{		
		boolean hayDatos = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null; 

		try {
			
			String sentenciaSQL = "select id_node, id_vocablo, parentdomain, id_domain, " +
					"pattern, locale, traduccion, tipo from v_domain where " +
					"parentdomain = ? and id_domain_node = id_domain and " +
					"id_description = id_vocablo and id_municipio is null";
			
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1,dominioPadre.getIdNode());

			rs =preparedStatement.executeQuery();
			while (rs.next()){

				hayDatos = true;
				DomainNode childDomainNode=null;
				String newChildIdDomainNode=rs.getString("id_node");
				if ((childDomainNode==null) || (!childDomainNode.getIdNode().equals(newChildIdDomainNode)))
				{
					childDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_vocablo"),
							rs.getInt("tipo"), new Integer(rs.getInt("parentdomain")).toString(),
							new Integer(idMunicipio).toString(),
							rs.getString("id_domain"),rs.getString("pattern"));
					if (rs.getString("id_vocablo")!=null)
						childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

					childDomainNode = obtenerDominiosHijo(connection, childDomainNode, idMunicipio);
					dominioPadre.addHijo(childDomainNode);
				}
				else
				{
					childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
				}

			}

			if(hayDatos)return dominioPadre;
			
			sentenciaSQL = "select id_node, id_vocablo, parentdomain, id_domain, " +
			"pattern, locale, traduccion, tipo from v_domain where " +
			"parentdomain = ? and id_domain_node = id_domain and " +
			"id_description = id_vocablo and id_municipio = ?";
			
			preparedStatement = connection.prepareStatement(sentenciaSQL);
	
			preparedStatement.setString(1,dominioPadre.getIdNode());
			preparedStatement.setInt(2,idMunicipio);
						
			rs =preparedStatement.executeQuery();
			while (rs.next()){

				DomainNode childDomainNode=null;
				String newChildIdDomainNode=rs.getString("id_node");
				if ((childDomainNode==null) || (!childDomainNode.getIdNode().equals(newChildIdDomainNode)))
				{
					childDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_vocablo"),
							rs.getInt("tipo"), new Integer(rs.getInt("parentdomain")).toString(),
							new Integer(idMunicipio).toString(),
							rs.getString("id_domain"),rs.getString("pattern"));
					if (rs.getString("id_vocablo")!=null)
						childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

					childDomainNode = obtenerDominiosHijo(connection, childDomainNode, idMunicipio);
					dominioPadre.addHijo(childDomainNode);
				}
				else
				{
					childDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
				}

			}
			return dominioPadre;

		} 
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

	}
	
	public Collection<MunicipioOT> obtenerListaMunicipios(Connection connection, int idEntidad) throws SQLException{
		
		List<MunicipioOT> lstMunicipios = new ArrayList<MunicipioOT>();
		
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;
		
		try{

			String sentenciaSQL = "select * from entidades_municipios where id_entidad = ?";

			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setInt(1, idEntidad);
			rs =preparedStatement.executeQuery();
			
			while(rs.next()){
				
				MunicipioOT municipio = new MunicipioOT();
				municipio.setCodigoINE(rs.getInt("id_municipio"));
				lstMunicipios.add(municipio);
			}
		}				
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			
		}		
		
		return lstMunicipios;
	}

	public Collection obtenerProvincias(Connection connection) throws SQLException{
		
		ArrayList lstProvincias = new ArrayList();
		
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;
		
		try{

			String sentenciaSQL = "select id, nombreoficial, nombrecooficial, comunidad from provincias";

			preparedStatement = connection.prepareStatement(sentenciaSQL);
			rs =preparedStatement.executeQuery();
			
			while(rs.next()){
				
				ProvinciaOT provincia = new ProvinciaOT();
				
				provincia.setCodigoINE(new Integer(rs.getInt("id")));
				provincia.setNombreOficial(rs.getString("nombreoficial"));
				provincia.setNombreCoOficial(rs.getString("nombrecooficial"));
				provincia.setComunidadAutonoma(rs.getString("comunidad"));
				
				lstProvincias.add(provincia);
			}
		}				
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			
		}		
		
		return lstProvincias;
	}
	
	public DomainNode obtenerDominioPadre(Connection connection, String dominio, int idMunicipio, int tipo) throws SQLException{

		DomainNode oldDomainNode=null;
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;

		try {

			String sentenciaSQL = "select id_node, id_vocablo, parentdomain, id_domain," +
					" pattern, locale, traduccion from v_domain where " +
					"upper(nombre)=upper(?) and tipo = ? and " +
					"id_domain_node=id_domain and id_description = id_vocablo " +
					"and id_municipio is null";
			
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			
			preparedStatement.setString(1,dominio);
			preparedStatement.setInt(2,tipo);
			rs =preparedStatement.executeQuery();
			
			boolean hayDatos=true;
			if (!rs.next())
			{		
				sentenciaSQL = "select id_node, id_vocablo, parentdomain, id_domain," +
				" pattern, locale, traduccion from v_domain where " +
				"upper(nombre)=upper(?) and tipo = ? and " +
				"id_domain_node=id_domain and id_description = id_vocablo " +
				"and id_municipio = ?";
				
				preparedStatement = connection.prepareStatement(sentenciaSQL);
				preparedStatement.setString(1,dominio);
				preparedStatement.setInt(2,2);
				preparedStatement.setString(3, null);
				rs =preparedStatement.executeQuery();
				
				if (!rs.next())
				{
					hayDatos=false;					
				}
			}
			if (hayDatos)
			{ 
				do{
					String newIdDomainNode=rs.getString("id_node");
					if ((oldDomainNode==null) || (!oldDomainNode.getIdNode().equals(newIdDomainNode)))
					{
						oldDomainNode= new DomainNode(rs.getString("id_node"),rs.getString("id_vocablo"),
								2, rs.getString("parentdomain"),
								new Integer(idMunicipio).toString(),
								rs.getString("id_domain"),rs.getString("pattern"));						
						if (rs.getString("id_vocablo")!=null)
							oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));

						oldDomainNode = obtenerDominiosHijo(connection, oldDomainNode, idMunicipio);
					}
					else
					{
						oldDomainNode.addTerm(rs.getString("locale"), rs.getString("traduccion"));
					}
				}while (rs.next());
			}

		}
		finally{
			
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return oldDomainNode;
	}
	
	public String altaPOI(Connection connection, PoiOT poiOT)
			throws SQLException, ParseException, PoiExistenteException, TipoNoExistenteException, SubtipoNoExistenteException {
				
		long a = System.currentTimeMillis();
		
		Hashtable lista = obtenerListaDominios(connection, "Pois", poiOT.getIdMunicpio(), 2);
		if (!comprobarTipo(lista, poiOT.getTipo()))
			throw new TipoNoExistenteException("El nombre de capa " + poiOT.getTipo() + " no existe");
		
		if (!comprobarSubtipo(lista, poiOT.getSubtipo()))
			throw new SubtipoNoExistenteException("El subtipo " + poiOT.getSubtipo() + " no existe");
		
		if(comprobarExistenciaPoi(connection, poiOT.getIdContenido()))
			throw new PoiExistenteException("El poi con id "+ poiOT.getIdContenido() + " ya existe");
		
        PreparedStatement preparedStatement = null;

        String senteciaSQL = "insert into pois " +
        		" (id,tipo, subtipo, id_contenido, url_contenido, direccion, id_municipio, \"GEOMETRY\") " +
        		" values (nextval('seq_pois'),?,?,?,?,?,?,transform(GeometryFromText('POINT(" + poiOT.getCoordX() + " " + poiOT.getCoordY() + ")',"+SRID_ENTRADA+"),"+SRID_SALIDA+"))";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setString(1, poiOT.getTipo() );
            preparedStatement.setString(2, poiOT.getSubtipo());
            preparedStatement.setInt(3, poiOT.getIdContenido());
            preparedStatement.setString(4, poiOT.getUrlContenido());
            preparedStatement.setString(5, poiOT.getDireccion());
            preparedStatement.setInt(6, poiOT.getIdMunicpio());
    
            preparedStatement.executeUpdate();
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }

		String response = "El punto de interés con idContenido " + poiOT.getIdContenido() + " se ha insertado con éxito";	
		
		return response;
	}

	private boolean comprobarSubtipo(Hashtable lista, String subtipo) {		
		for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();
            	
            	for (Enumeration enumSubtipoDomain = tipoDomainNode.getlHijos().gethDom().elements();enumSubtipoDomain.hasMoreElements();){            		
            		
            		DomainNode subtipoDomainNode=(DomainNode) enumSubtipoDomain.nextElement();
            		if(subtipo.equals(subtipoDomainNode.getPatron())) return true;
            	}
            	     	
            }
        }
		return false;
	}

	private boolean comprobarTipo(Hashtable lista, String tipo) {

		for (Enumeration e=lista.elements();e.hasMoreElements();)
        {
            DomainNode auxDomainNode=(DomainNode) e.nextElement();
            for (Enumeration enumTipoDomain = auxDomainNode.getlHijos().gethDom().elements();enumTipoDomain.hasMoreElements();){
            	//Tenemos el Dominio con cada tipo
            	DomainNode tipoDomainNode=(DomainNode) enumTipoDomain.nextElement();            	
            	if(tipo.equals(tipoDomainNode.getPatron())) return true;         	
            }
        }
		return false;
	}

	public boolean comprobarExistenciaPoi(Connection connection, int id_contenido) throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select id from pois where id_contenido = ?";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setInt(1, id_contenido );
            
    
            rs = preparedStatement.executeQuery();
            
            if(rs.next()) return true;
            else return false;
            	
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
	}
	
	public boolean comprobarExistenciaVia(Connection connection, int idVia, 
			int idMunicipio, Date fechaEjecucion) throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select id_via from vias_INE_temporal where id_via = ? " +
        		"and id_ municipio=? and fecha_ejecucion=?";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setInt(1, idVia );
            preparedStatement.setInt(2, idMunicipio );  
            preparedStatement.setDate(3, fechaEjecucion);
    
            rs = preparedStatement.executeQuery();
            
            if(rs.next()) return true;
            else return false;
            	
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
	}
	
	public boolean comprobarExistenciaNumero(Connection connection, int idNumero, 
			int idMunicipio, Date fechaEjecucion) throws SQLException
	{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select id_numero from numeros_INE_temporal where id_via = ?" +
        		" and id_ municipio=? and fecha_ejecucion=?";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setInt(1, idNumero );
            preparedStatement.setInt(2, idMunicipio );   
            preparedStatement.setDate(3, fechaEjecucion);
    
            rs = preparedStatement.executeQuery();
            
            if(rs.next()) return true;
            else return false;
            	
            
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
	}

	public Integer obtenerUsuario(Connection connection, String nombreUsuario, String passwordUsuario, MessageContext context) throws PasswordNoValidoException, SQLException, UsuarioNoExistenteException{
		
		String password = null;
		Integer idUsuario = null;
		
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

        String senteciaSQL = "select password,id from IUSERUSERHDR where " +        
        	"upper(name)=upper(?)";

        try
        {
            preparedStatement = connection.prepareStatement(senteciaSQL);
            preparedStatement.setString(1, nombreUsuario );
                       
            rs = preparedStatement.executeQuery();
            
            if(rs.next()){
            	idUsuario = new Integer(rs.getInt("id"));
            	password = rs.getString("password");
            }
            else {
            	throw new UsuarioNoExistenteException("El usuario " + nombreUsuario + " no existe");
            }
           
            String passwordDesencriptado = null;
            try
            {
		    	passwordDesencriptado = (new EncriptarPassword()).undoEncrip(password);
            }catch(Exception e)
            {
                passwordDesencriptado = password;
            }
                       
            Credential credential = Credential.getCredential(passwordDesencriptado);

			if(!credential.check(passwordUsuario))
				throw new PasswordNoValidoException("El password " + passwordUsuario + " no es válido");
			
        }finally
        {
            ConnectionUtilities.closeConnection(null, preparedStatement, null);
        }
		
		return idUsuario;
	}

	public String altaCallejero(Connection connection, CalleOT calle) throws SQLException, PeticionViaErroneaException {
				
		PreparedStatement preparedStatement = null;

		String senteciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		StringWriter stringWriterXml;
		try
		{
			stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML(); 
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, Via.getDateCustonConverter());
			
			Via via = new Via();
			via.setCodigoINEMunicipio(calle.getCodigoINEMunicipio());
			via.setNombreViaIne(calle.getDenominacion());
			via.setTipoViaIne(calle.getClaseVia());
			via.setCodigoIne(calle.getCodigoTipoVia());
			via.setCodigoCatastro(calle.getCodigoDGC().toString());
			via.setNombreViaCortoIne(calle.getDenominacionNormalizada());
			via.setFechaGrabacionAyto(calle.getFechaGrabacionAyto());
			via.setFechaGrabacionCierre(calle.getFechaGrabacionCierre());
			via.setFechaEjecucion(calle.getFechaEjecucion());
			via.setIdalp(calle.getIdacl());
						
			try {
				converter.write(via,"Via",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionViaErroneaException("La petición de la vía es errónea");
			}
									
			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setString(1, "AV");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, via.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();
		} catch (Throwable e1) {
			
		e1.printStackTrace();		
		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "La via con el Identificador " + calle.getIdacl() + " se ha dado de alta con éxito";	

		return response;
	}
	
	public String altaNumerero(Connection connection, NumeroOT numero, 
			String tipoVia, String nombreVia) throws PeticionNumeroErroneaException, SQLException{
		
		PreparedStatement preparedStatement = null;

		String sentenciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		try
		{
			StringWriter stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML();
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, NumeroPolicia.getDateCustonConverter());
			
			NumeroPolicia numeroPolicia = new NumeroPolicia();
			numeroPolicia.setCodigoINEMunicipio(numero.getCodigoINEMunicipio());
			numeroPolicia.setNumero(numero.getNumero());
			numeroPolicia.setCalificador(numero.getCalificador());
			numeroPolicia.setId_via(numero.getIdVia().toString());
			numeroPolicia.setFechaEjecucion(numero.getFechaEjecucion());
			numeroPolicia.setIdalp(new Integer(numero.getIdacl()).toString());
			numeroPolicia.setTipoVia(tipoVia);
			numeroPolicia.setNombreVia(nombreVia);
						
			try {
				converter.write(numeroPolicia,"NumeroPolicia",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionNumeroErroneaException("La petición del número es errónea");
			}
									
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, "AN");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, numeroPolicia.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "El número con el Identificador " + numero.getIdacl() + " se ha dado de alta con éxito";	

		return response;
	}

	public String bajaCallejero(Connection connection, int idVia,
			int idMunicipio, String idalp, String claseVia, String denominacion, 
			Date fechaEjecucion) throws PeticionViaErroneaException, SQLException {
		
		PreparedStatement preparedStatement = null;

		String sentenciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		try
		{
			StringWriter stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML();
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, Via.getDateCustonConverter());
			
			Via via = new Via();
			via.setId(new Integer(idVia).toString());
			via.setIdalp(new Integer(idalp));
			via.setTipoViaIne(claseVia);
			via.setNombreViaIne(denominacion);
			via.setCodigoINEMunicipio(new Integer(idMunicipio));
			via.setFechaEjecucion(fechaEjecucion);
						
			try {
				converter.write(via,"Via",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionViaErroneaException("La petición de la vía es errónea");
			}
									
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, "BV");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, via.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "La via con el Identificador " + idalp + " se ha dado de baja con éxito";	

		return response;
	}
	
	public String modificacionCallejero(Connection connection, int idVia, 
			CalleOT calle) throws SQLException, PeticionViaErroneaException {
		
		PreparedStatement preparedStatement = null;

		String senteciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		try
		{
			StringWriter stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML();
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, Via.getDateCustonConverter());
			
			Via via = new Via();
			via.setCodigoINEMunicipio(calle.getCodigoINEMunicipio());
			via.setNombreViaIne(calle.getDenominacion());
			via.setTipoViaIne(calle.getClaseVia());
			via.setCodigoIne(calle.getCodigoTipoVia());
			via.setCodigoCatastro(calle.getCodigoDGC().toString());
			via.setNombreViaCortoIne(calle.getDenominacionNormalizada());
			via.setFechaGrabacionAyto(calle.getFechaGrabacionAyto());
			via.setFechaGrabacionCierre(calle.getFechaGrabacionCierre());
			via.setFechaEjecucion(calle.getFechaEjecucion());
			via.setIdalp(calle.getIdacl());
			via.setId(new Integer(idVia).toString());
			
			try {
				converter.write(via,"Via",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionViaErroneaException("La petición de la vía es errónea");
			}
									
			preparedStatement = connection.prepareStatement(senteciaSQL);
			preparedStatement.setString(1, "MV");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, via.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "La via con el Identificador " + calle.getIdacl() + " se ha modificado con éxito";	

		return response;
	}
	
	public String bajaNumerero(Connection connection, NumeroOT numero, 
			String tipoVia, String nombreVia) throws PeticionNumeroErroneaException, SQLException{
		
		PreparedStatement preparedStatement = null;

		String sentenciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		try
		{
			StringWriter stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML();
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, NumeroPolicia.getDateCustonConverter());
			
			NumeroPolicia numeroPolicia = new NumeroPolicia();
			numeroPolicia.setCodigoINEMunicipio(numero.getCodigoINEMunicipio());
			numeroPolicia.setNumero(numero.getNumero());
			numeroPolicia.setCalificador(numero.getCalificador());
			numeroPolicia.setId_via(numero.getIdVia().toString());
			numeroPolicia.setFechaEjecucion(numero.getFechaEjecucion());
			numeroPolicia.setIdalp(new Integer(numero.getIdacl()).toString());
			numeroPolicia.setTipoVia(tipoVia);
			numeroPolicia.setNombreVia(nombreVia);
						
			try {
				converter.write(numeroPolicia,"NumeroPolicia",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionNumeroErroneaException("La petición del número es errónea");
			}
									
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, "BN");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, numeroPolicia.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "El número con Identificador " + numero.getIdacl() + " se ha dado de baja con éxito";	

		return response;
	}

	public String modificacionNumerero(Connection connection,  
			NumeroOT numero, String tipoVia, String nombreVia) 
	throws PeticionNumeroErroneaException, SQLException{
		
		PreparedStatement preparedStatement = null;

		String sentenciaSQL = "insert into alp_temporal (id, " +
				"tipo_operacion, fecha_operacion, xml, idmunicipio) " +
				"values (nextval('seq_alp_temporal'),?,?,?,?)"; 

		try
		{
			StringWriter stringWriterXml = new StringWriter();
			Java2XML converter = new Java2XML();
			converter.removeCustomConverter(java.util.Date.class);
			converter.addCustomConverter(Date.class, NumeroPolicia.getDateCustonConverter());
			
			NumeroPolicia numeroPolicia = new NumeroPolicia();
			numeroPolicia.setCodigoINEMunicipio(numero.getCodigoINEMunicipio());
			numeroPolicia.setNumero(numero.getNumero());
			numeroPolicia.setCalificador(numero.getCalificador());
			numeroPolicia.setId_via(numero.getIdVia().toString());
			numeroPolicia.setFechaEjecucion(numero.getFechaEjecucion());
			numeroPolicia.setIdalp(new Integer(numero.getIdacl()).toString());
			numeroPolicia.setTipoVia(tipoVia);
			numeroPolicia.setNombreVia(nombreVia);
						
			try {
				converter.write(numeroPolicia,"NumeroPolicia",stringWriterXml);
			} catch (Exception e) {
				throw new PeticionNumeroErroneaException("La petición del número es errónea");
			}
									
			preparedStatement = connection.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, "MN");
			preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, stringWriterXml.toString());
			preparedStatement.setInt(4, numeroPolicia.getCodigoINEMunicipio());
			
			preparedStatement.executeUpdate();

		}finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}

		String response = "El número con identificador " + numero.getIdacl() + " se ha modificado con éxito";	

		return response;
	}

	public Collection obtenerMunicipios(Connection connection,
			Integer codigoProvinciaINE) throws SQLException {
		
		ArrayList lstMunicipios = new ArrayList();
		
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;
		
		try{

			if (codigoProvinciaINE != null){
				
				String sentenciaSQL = "select id, nombreoficial, nombrecooficial from municipios where id_provincia=?";
				preparedStatement = connection.prepareStatement(sentenciaSQL);
				preparedStatement.setInt(1, codigoProvinciaINE.intValue());
			}
			else{
				
				String sentenciaSQL = "select id, nombreoficial, nombrecooficial from municipios";
				preparedStatement = connection.prepareStatement(sentenciaSQL);				
			}
			
			rs =preparedStatement.executeQuery();
			
			while(rs.next()){
				
				MunicipioOT municipio = new MunicipioOT();
				
				municipio.setCodigoINE(new Integer(rs.getInt("id")));
				municipio.setNombreOficial(rs.getString("nombreoficial"));
				municipio.setNombreCoOficial(rs.getString("nombrecooficial"));
				
				lstMunicipios.add(municipio);
			}
		}
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			
		}		
		
		return lstMunicipios;
	}
	
	public Collection obtenerEntidadMunicipios(Connection connection,
			Integer codigoINE) throws SQLException {
		
		ArrayList lstMunicipios = new ArrayList();
		
		PreparedStatement preparedStatement = null;
		ResultSet  rs = null;
		
		try{

			if (codigoINE != null){				
				String sentenciaSQL = "select es.id_entidad as id_entidad, es.nombreoficial as nombreoficial, (select count(*) from entidades_municipios em2 where em2.id_entidad=em.id_entidad) as municipiosCount from entidad_supramunicipal es inner join entidades_municipios em on(es.id_entidad=em.id_entidad) where em.id_municipio=? order by municipiosCount limit 1";
				preparedStatement = connection.prepareStatement(sentenciaSQL);
				preparedStatement.setInt(1, codigoINE.intValue());				
			}
			else{				
				String sentenciaSQL = "select id_entidad, nombreoficial from entidad_supramunicipal";
				preparedStatement = connection.prepareStatement(sentenciaSQL);				
			}
			
			rs =preparedStatement.executeQuery();
			
			while(rs.next()){
				
				EntidadOT entidad = new EntidadOT();
				
				entidad.setCodigo(new Integer(rs.getInt("id_entidad")));
				entidad.setNombreOficial(rs.getString("nombreoficial"));
				
				lstMunicipios.add(entidad);
			}
		}
		finally
		{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			
		}		
		
		return lstMunicipios;
	}

	public String insertaBienPAalta(Connection connection, BienPreAltaOT bienPAOT)
	throws Exception, SQLException,ParseException{
		
        PreparedStatement ps = null;

		String sSQL = "insert into bien_PreAlta (id, nombre, descripcion, id_municipio, fecha_adquisicion, coste_adquisicion, tipo_bien) " +
		"values((select nextval('\"seq_bien_prealta\"')), ?,?,?,?,?,?);";

        try{
        	
			ps = connection.prepareStatement(sSQL);
			ps.setString(1, bienPAOT.getNombre());
			ps.setString(2, bienPAOT.getDescripcion());
			ps.setLong(3, bienPAOT.getIdMunicipio());
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date fec = formatoDelTexto.parse(bienPAOT.getFechaAdquisicion());
			Timestamp ts= new Timestamp(fec.getTime());
			ps.setTimestamp(4, ts);
			ps.setDouble(5, bienPAOT.getCosteAdquisicion());
			ps.setInt(6, bienPAOT.getTipo());

			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("No se ha podido insertar el bien en Prealta", e);
        }finally{
            ConnectionUtilities.closeConnection(null, ps, null);
        }

		String response = "El Bien en Prealta " + bienPAOT.getNombre() + " se ha insertado con éxito";	
		
		return response;
	}

}
