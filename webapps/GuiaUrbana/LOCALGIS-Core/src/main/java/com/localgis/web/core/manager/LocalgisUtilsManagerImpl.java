/**
 * LocalgisUtilsManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.geopista.protocol.administrador.EncriptarPassword;
import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.dao.GeopistaAuthenticationDAO;
import com.localgis.web.core.dao.GeopistaMapGenericElementDAO;
import com.localgis.web.core.dao.GeopistaParcelaDAO;
import com.localgis.web.core.dao.IncidenciasAuthenticationDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaParcela;
import com.localgis.web.core.model.Scale;
import com.localgis.web.core.utils.LocalgisUtils;
import com.localgis.web.core.wms.WMSConfigurator;

public class LocalgisUtilsManagerImpl implements LocalgisUtilsManager {
	
	private static Logger logger = Logger.getLogger(LocalgisUtilsManagerImpl.class);

    /**
     * Dao para los elementos genericos de un mapa de geopista
     */
    private GeopistaMapGenericElementDAO geopistaMapGenericElementDAO;

    /**
	 * DAO para las parcelas de Geopista
	 */
	private GeopistaParcelaDAO geopistaParcelaDAO;

	/**
     * DAO para la autenticacion de Geopista
     */
    private GeopistaAuthenticationDAO geopistaAuthenticationDAO;
    
    /**
     * DAO para la autenticacion de Incidencias
     */
    private IncidenciasAuthenticationDAO incidenciasAuthenticationDAO;

    /**
     * Configurador WMS
     */
    private WMSConfigurator wmsConfigurator;
    
	/**
     * Constructor a partir de un DAOManager, un configurador de wms y un LocalgisManagerBuilder
     */
	public LocalgisUtilsManagerImpl(DaoManager daoManager, WMSConfigurator wmsConfigurator){
        this.geopistaMapGenericElementDAO = (GeopistaMapGenericElementDAO) daoManager.getDao(GeopistaMapGenericElementDAO.class);
        this.geopistaParcelaDAO = (GeopistaParcelaDAO) daoManager.getDao(GeopistaParcelaDAO.class);
        this.geopistaAuthenticationDAO = (GeopistaAuthenticationDAO) daoManager.getDao(GeopistaAuthenticationDAO.class);
        this.incidenciasAuthenticationDAO = (IncidenciasAuthenticationDAO) daoManager.getDao(IncidenciasAuthenticationDAO.class);
        this.wmsConfigurator = wmsConfigurator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.manager.LocalgisUtilsManager#getReportScaleForParcelaByReferenciaCatastral(java.lang.String, int, int)
	 */
	public Scale getReportScaleForParcelaByReferenciaCatastral(
			String referenciaCatastral, int width, int height) throws LocalgisDBException {
		GeopistaParcela geopistaParcela = geopistaParcelaDAO.selectParcelaByReferenciaCatastral(referenciaCatastral, new Integer("25830"));
		
        if (geopistaParcela == null) {
            logger.error("La parcela con referencia catastral \""+referenciaCatastral+"\" no existe");
            throw new LocalgisDBException("La parcela con referencia catastral \""+referenciaCatastral+"\" no existe");
        }

        if (geopistaParcela.getIdMunicipio() == null) {
            logger.error("La parcela con referencia catastral \""+referenciaCatastral+"\" no pertenece a ningún municipio");
            throw new LocalgisDBException("La parcela con referencia catastral \""+referenciaCatastral+"\" no pertenece a ningún municipio");
        }
                
        BoundingBox boundingBox = geopistaParcelaDAO.selectBoundingBoxByReferenciaCatastral(referenciaCatastral);
        Scale scale = LocalgisUtils.getScale(boundingBox, width, height);
        return scale;
	}
	
	public Scale getReportScale(String tableName, String identifierColumnName, Object identifierValue, int width, int height, Integer srid) throws LocalgisDBException {
        BoundingBox boundingBox = geopistaMapGenericElementDAO.selectBoundingBoxMapGenericElement(tableName, identifierColumnName, identifierValue, srid);
        Scale scale = LocalgisUtils.getScale(boundingBox, width, height);
        return scale;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.manager.LocalgisUtilsManager#isValidUser(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	public boolean isValidUser(String username, String password, String role, Integer idEntidad) {
	    /*
	     * Comprobamos que todos los parametros sean validos
	     */
	    if (username == null || username.equals("") || password == null || password.equals("") || role == null || role.equals("") || idEntidad == null) {
	        logger.error("Error al autenticar con username = ["+username+"], password = ["+(password != null ? "********" : null) +"], role = ["+role+"], idEntidad = ["+idEntidad+"]");
	        return false;
	    } else {
	        String encryptedPassword1;
	        String encryptedPassword2;
	        try {
	        	
	        	//Por diferentes motivos el primer algoritmo era el DES y luego se paso al AES, pero hay usuarios
	        	//que todavia no se han actualizado y por tanto su contraseña sigue siendo DES. Hay que probar
	        	//con los dos casos.
	        	EncriptarPassword encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE1_ALGORITHM_SPECIAL);
	        	encryptedPassword1=encriptarPassword.encriptar(password);

	        	encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
	        	encryptedPassword2=encriptarPassword.encriptar(password);
	        	
	            //encryptedPassword = encrytGeopistaPassword(password);
	        } catch (Exception e) {
	            logger.error("Error al autenticar al usuario: No se ha podido cifrar la password", e);
	            return false;
	        }
	        boolean result = geopistaAuthenticationDAO.isValidUser(username, encryptedPassword1, encryptedPassword2,role, idEntidad);
            logger.debug("Resultado de autenticar con username = ["+username+"], role = ["+role+"], idEntidad = ["+idEntidad+"]: "+result);
            return result;
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.manager.LocalgisUtilsManager#isValidUserEntity(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Integer isValidUserEntity(String username, String password, String role) {
	    Integer idEntidad = -1;
		
		/*
	     * Comprobamos que todos los parametros sean validos
	     */
	    if (username == null || username.equals("") || password == null || password.equals("") || role == null || role.equals("")) {
	        logger.error("Error al autenticar con username = ["+username+"], password = ["+(password != null ? "********" : null) +"], role = ["+role+"]");
	        return idEntidad;
	    } else {
	        String encryptedPassword1;
	        String encryptedPassword2;
	        try {
	        	//Por diferentes motivos el primer algoritmo era el DES y luego se paso al AES, pero hay usuarios
	        	//que todavia no se han actualizado y por tanto su contraseña sigue siendo DES. Hay que probar
	        	//con los dos casos.
	        	EncriptarPassword encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE1_ALGORITHM_SPECIAL);
	        	encryptedPassword1=encriptarPassword.encriptar(password);

	        	encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
	        	encryptedPassword2=encriptarPassword.encriptar(password);

	            //encryptedPassword = encrytGeopistaPassword(password);
	        } catch (Exception e) {
	            logger.error("Error al autenticar al usuario: No se ha podido cifrar la password", e);
	            return idEntidad;
	        }
	        idEntidad = geopistaAuthenticationDAO.isValidUserEntity(username, encryptedPassword1, encryptedPassword2,role);
            logger.debug("Resultado de autenticar con username = ["+username+"], role = ["+role+"]: " + idEntidad);
            return idEntidad;
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.manager.LocalgisUtilsManager#isValidUserEntityNotPass(java.lang.String, java.lang.String)
	 */
	public Integer isValidUserEntityNotPass(String username, String role) {
	    Integer idEntidad = -1;
		
		/*
	     * Comprobamos que todos los parametros sean validos 
	     */
	    if (username == null || username.equals("") || role == null || role.equals("")) {
	        logger.error("Error al autenticar con username = ["+username+"], role = ["+role+"]");
	        return idEntidad;
	    } else {	        
	        idEntidad = geopistaAuthenticationDAO.isValidUserEntityNotPass(username, role);
            logger.debug("Resultado de autenticar con username = ["+username+"], role = ["+role+"]: " + idEntidad);
            return idEntidad;
	    }
	}
	
	/**
	 * Cifra la password siguiendo el cifrado de geopista
	 * @param password Password a cifrar
	 * @return La password cifrada
	 * @throws Exception Si ocurre algún error
	 */
	/*public String encrytGeopistaPassword(String password) throws Exception {
	    Cipher ecipher;
	    String semilla = "GEOPISTA";
	    byte[] rawkey = semilla.getBytes("UTF8");
	    DESKeySpec keyspec = new DESKeySpec(rawkey);
	    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
	    SecretKey key = keyfactory.generateSecret(keyspec);
	    byte[] utf8 = password.getBytes("UTF8");
	    ecipher = Cipher.getInstance("DES");
	    ecipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] enc = ecipher.doFinal(utf8);
	    //return new sun.misc.BASE64Encoder().encode(enc);
	    return new String(new Base64().encode(enc));
    }*/

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisUtilsManager#getBBoxSpain(java.lang.String)
     */
    public BoundingBox getBoundingBoxSpain(String srid) throws LocalgisConfigurationException {
        return wmsConfigurator.getBoundingBoxSpain(srid);
    }

    public BoundingBox getBoundingBoxComunidad(String srid) throws LocalgisConfigurationException {
        return wmsConfigurator.getBoundingBoxComunidad(srid);
    }

    public LocalgisUtilsManagerImpl(){
    	
    }
    public static void main(String args[]){
    	//String password="xxx";
    	String encrypted;
		try {
			//encrypted = new LocalgisUtilsManagerImpl().encrytGeopistaPassword(password);
			//System.out.println("Encrypted:"+encrypted);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
    
	public boolean isValidUserIncidencias(String username, String password, String role, Integer idEntidad) {
	    /*
	     * Comprobamos que todos los parametros sean validos
	     */
	    if (username == null || username.equals("") || password == null || password.equals("") || role == null || role.equals("") || idEntidad == null) {
	        logger.error("Error al autenticar con username = ["+username+"], password = ["+(password != null ? "********" : null) +"], role = ["+role+"], idEntidad = ["+idEntidad+"]");
	        return false;
	    } else {
	        String encryptedPassword1;
	        String encryptedPassword2;
	        try {
	        	
	        	//Por diferentes motivos el primer algoritmo era el DES y luego se paso al AES, pero hay usuarios
	        	//que todavia no se han actualizado y por tanto su contraseña sigue siendo DES. Hay que probar
	        	//con los dos casos.
	        	EncriptarPassword encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE1_ALGORITHM_SPECIAL);
	        	encryptedPassword1=encriptarPassword.encriptar(password);

	        	encriptarPassword=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
	        	encryptedPassword2=encriptarPassword.encriptar(password);
	            //encryptedPassword = encrytGeopistaPassword(password);
	        	
	        } catch (Exception e) {
	            logger.error("Error al autenticar al usuario: No se ha podido cifrar la password", e);
	            return false;
	        }
	        boolean result = incidenciasAuthenticationDAO.isValidUserIncidencias(username, encryptedPassword1, encryptedPassword2,role, idEntidad);
            logger.debug("Resultado de autenticar con username = ["+username+"], role = ["+role+"], idEntidad = ["+idEntidad+"]: "+result);
            return result;
	    }
	}
	
	public boolean isValidUserIncidenciasExterno(String idUsuario) {
	    /*
	     * Comprobamos que todos los parametros sean validos
	     */
	    if (idUsuario == null || idUsuario.equals("")) {
	        logger.error("Error al comprobar usuario externo con idUsuario = ["+idUsuario+"]");
	        return false;
	    } else {
	        boolean result = incidenciasAuthenticationDAO.isValidUserIncidenciasExterno(idUsuario);
            logger.debug("Resultado de autenticar con username = ["+idUsuario+"]: "+result);
            return result;
	    }
	}
	
}
