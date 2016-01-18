package com.localgis.app.gestionciudad.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

public class GestionCiudadOperaciones {

	/**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
    /**
     * Locale que identifica el idioma del usuario
     */
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, AppConstants.DEFAULT_LOCALE, false);

	private String ParentDomainName = "Actuaciones e intervenciones";
    
	 /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
		Connection conn = null;
		try {
			if (!aplicacion.isLogged()){
				aplicacion.login();
					
			}
			conn = aplicacion.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
    }  
    
    // GestionCiudadObtenerTiposActuacion
    // GestionCiudadObtenerTiposIntervencion
    public ListaEstructuras obtenerTiposDeActuaciones() throws DataException{

    	ListaEstructuras resultado = new ListaEstructuras();

    	try
    	{

    		PreparedStatement s = null;
    		ResultSet rs = null;
    		s = getDBConnection().prepareStatement("GestionCiudadObtenerTiposActuacion");
    		s.setString(1, locale);
    		s.setString(2, locale);
    		s.setString(3, locale);
    		s.setString(4, this.ParentDomainName );

    		rs = s.executeQuery();  
    		while (rs.next())
    		{           
    			DomainNode domainNode = new DomainNode();

    			try{
    			domainNode =  new DomainNode(
    					rs.getString("id_node"),
    					rs.getString("id_description"),
                        com.geopista.feature.Domain.CODEDENTRY, null,
                        new Integer(com.geopista.security.SecurityManager.getIdEntidad()).toString(),
                        rs.getString("id_node"),
                        rs.getString("parentpattern"));
    			
    			if (rs.getString("id_description")!=null){
    				domainNode.addTerm(locale, rs.getString("parentdescription"));
    			}
    			} catch (Exception e) {
					// TODO: handle exception
    				e.printStackTrace();
    				domainNode = null;
				}
                resultado.add(domainNode);
    		}

    		s.close();
    		rs.close(); 
//    		conn.close();

//    		dRet = obtenerDominioTipo(idDominio, idTipo, nombreDominio);

    	}
    	catch (SQLException ex)
    	{
    		throw new DataException(ex);
    	}

    	return resultado;    

    }
    
    
    
    public ListaEstructuras obtenerTiposDeIntervenciones(String parentPattern) throws DataException{

    	ListaEstructuras resultado = new ListaEstructuras();
    	PreparedStatement s = null;
		ResultSet rs = null;

    	try
    	{

    		s = getDBConnection().prepareStatement("GestionCiudadObtenerTiposIntervencion");
    		s.setString(1, locale);
    		s.setString(2, locale);
    		s.setString(3, locale);
    		s.setString(4, this.ParentDomainName );
    		s.setString(5, parentPattern);

    		rs = s.executeQuery();  
    		while (rs.next())
    		{           
    			DomainNode domainNode = new DomainNode();

    			try{
    			domainNode =  new DomainNode(
    					rs.getString("id_node"),
    					rs.getString("id_description"),
                        com.geopista.feature.Domain.CODEDENTRY, null,
                        new Integer(com.geopista.security.SecurityManager.getIdEntidad()).toString(),
                        rs.getString("id_node"),
                        rs.getString("pattern"));
    			
    			if (rs.getString("id_description")!=null){
    				domainNode.addTerm(locale, rs.getString("description"));
    			}
    			} catch (Exception e) {
					// TODO: handle exception
    				e.printStackTrace();
    				domainNode = null;
				}
                resultado.add(domainNode);
    		}

    		s.close();
    		rs.close(); 

    	}
    	catch (SQLException ex)
    	{
    		throw new DataException(ex);
    	}

    	return resultado;    

    }
    
    
    
    public String obtenerTraduccionDeActuacion(String parentPattern) throws DataException{

    	String resultado = "";
    	PreparedStatement s = null;
		ResultSet rs = null;
		
    	try
    	{

    		s = getDBConnection().prepareStatement("GestionCiudadObtenerTraduccionesTiposActuacion");
    		s.setString(1, locale);
    		s.setString(2, locale);
    		s.setString(3, locale);
    		s.setString(4, this.ParentDomainName );
    		s.setString(5, parentPattern);

    		rs = s.executeQuery();  
    		while (rs.next())
    		{           
                resultado = rs.getString("parentdescription");
    		}

    		s.close();
    		rs.close(); 

    	}
    	catch (SQLException ex)
    	{
    		throw new DataException(ex);
    	}
    	

    	return resultado;    

    }
    
    public String obtenerTraduccionDeIntervencion(String parentPattern) throws DataException{

    	String resultado = "";
    	PreparedStatement s = null;
		ResultSet rs = null;
		
    	try
    	{

    		s = getDBConnection().prepareStatement("GestionCiudadObtenerTraduccionesTiposIntervencion");
    		s.setString(1, locale);
    		s.setString(2, locale);
    		s.setString(3, locale);
    		s.setString(4, this.ParentDomainName );
    		s.setString(5, parentPattern);

    		rs = s.executeQuery();  
    		while (rs.next())
    		{           
                resultado = rs.getString("description");
    		}

    		s.close();
    		rs.close(); 

    	}
    	catch (SQLException ex)
    	{
    		throw new DataException(ex);
    	}
    	

    	return resultado;    

    }
    
    public Integer getUserIdByUserNameAndIdMunipio(String userName,Integer idMunicipio) throws SQLException{
		
    	
    	Integer userId = -1;
		String sqlQuery = 
			"SELECT iuseruserhdr.id  " + 
			"FROM iuseruserhdr " + 
			"LEFT JOIN entidades_municipios ON entidades_municipios.id_entidad = iuseruserhdr.id_entidad " +  
			"WHERE (entidades_municipios.id_municipio = ? OR iuseruserhdr.id_entidad = 0) AND UPPER(iuseruserhdr.name) = UPPER(?)";
	
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{

			preparedStatement = getDBConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idMunicipio);
			preparedStatement.setString(2, userName);

			rs = preparedStatement.executeQuery();
			if (rs.next()){
				userId = new Integer(rs.getInt(1));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ConnectionUtilities.closeConnection(getDBConnection(), preparedStatement, rs);
		}
		finally{
			ConnectionUtilities.closeConnection(getDBConnection(), preparedStatement, rs);
		}
		return userId;
	}
    
    
    
    
 String tipoCalle = "CALLE";
 String nombreCalle = "[Error al Obtener el nombre de la calle]";
 public String getStreetNameFromVias(int idFeature, int idMunicpio) throws SQLException{
		
	 
	 	String tipo = tipoCalle;
	 	String nombrevia = nombreCalle + " idVia:" + idFeature;
    	
		String sqlQuery = "GestionCiudadObtenerNombreViaConTipo";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{

			preparedStatement = getDBConnection().prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idMunicpio);
			preparedStatement.setInt(2, idFeature);

			rs = preparedStatement.executeQuery();
			if (rs.next()){
				
				if (rs.getString("tipoviaine")!=null){
					if (!rs.getString("tipoviaine").equals("NE")){
						tipo = rs.getString("tipoviaine");
					}
				} else if (rs.getString("tipovianormalizadocatastro")!=null){
					if (!rs.getString("tipovianormalizadocatastro").equals("NE")){
						tipo = rs.getString("tipovianormalizadocatastro");
					}
				}
				
				
				if (rs.getString("nombreviaine")!=null){
					if (!rs.getString("nombreviaine").equals("")){
						nombrevia = rs.getString("nombreviaine");
					}
				} else if (rs.getString("nombrecatastro")!=null){
					if (!rs.getString("nombrecatastro").equals("")){
						nombrevia = rs.getString("nombrecatastro");
					}
				} else if (rs.getString("nombreviacortoine")!=null){
					if (!rs.getString("nombreviacortoine").equals("")){
						nombrevia = rs.getString("nombreviacortoine");
					}
				}
				
				return (tipo + " " + nombrevia);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ConnectionUtilities.closeConnection(getDBConnection(), preparedStatement, rs);
		}
		finally{
			ConnectionUtilities.closeConnection(getDBConnection(), preparedStatement, rs);
		}
		return (tipo + " " + nombrevia);
	}
 
 public ArrayList<String> getStreetNamesByFeatureRelationFromTramoVia(int idLayer,int idFeature) throws SQLException{
	 
	 
	 return new ArrayList<String>();
 }
    
}
