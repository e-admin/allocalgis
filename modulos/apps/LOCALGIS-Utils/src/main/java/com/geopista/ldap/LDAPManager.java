/**
 * LDAPManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ldap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv3;
import netscape.ldap.util.ConnectionPool;

import org.apache.log4j.Logger;



/**
 * <p>
 * Título: LDAPManager.java
 * </p>
 * <p>
 * Descripción: Clase que proporciona el manejo del LDAP.
 * </p>
 */
public class LDAPManager {

    /**
     * Define una variable estática que tiene una referencia a una instancia de
     * Logger llamada "LDAPManager".
     */
    private static Logger logger = Logger.getLogger(LDAPManager.class);

    /**
     * Instancia para implementar el patrón Singleton
     */
    private static LDAPManager instance = new LDAPManager();

    /**
     * Pool de conexiones con el LDAP
     */
    private ConnectionPool connectionPool;
    
	private String  base, rol, rootdn, passwroot, atributoRol, minSizePool, maxSizePool, host, port; 

	private static final String PROPERTIES_FILE = "ldap.properties";
	private static final String BASEDN_PROPERTY = "BASEDN";
	private static final String ATRIBUTOROL="ATRIBUTOROL";
	private static final String ROOTDN="ROOTDN";
	private static final String ROOTPW="ROOTPW";
	private static final String ROL = "ROL";
	private static final String MINSIZEPOOL = "MINSIZEPOOL";
	private static final String MAXSIZEPOOL = "MAXSIZEPOOL";
	private static final String HOST = "HOST";
	private static final String PORT = "PORT";

    /**
     * Contructor por defecto privado para implementar el patrón Singleton
     */
    private LDAPManager() {
        String prefixLog = "LDAPManager: ";
        logger.debug(prefixLog + "Inicializando LDAPManager");
        
        //Se cargan las propiedades de fichero
        cargarPropiedades();
        
        /*
         * Se inicializa el pool
         */
        try {
            LDAPConnection connection = new LDAPConnection();
            
            connection.connect(3, host, Integer.parseInt(port), rootdn, passwroot);
            connectionPool = new ConnectionPool(Integer.parseInt(minSizePool),
                    							Integer.parseInt(maxSizePool), connection);
    
        } catch (LDAPException e) {
            logger.error(prefixLog + "No se ha podido conectar con el LDAP", e);
        }
        logger.debug(prefixLog + "LDAPManager inicializado correctamente");
    }

    /**
     * Devuelve una instancia de la clase LDAPManager
     * 
     * @return Una instancia de la clase LDAPManager
     */
    public static LDAPManager getInstance() {
        return instance;
    }

    /**
     * Devuleve si un usuario dado con su password se autentica correctamente y posee el rol de la aplicacion correspondiente
     * @param user, usuario para validar
     * @param password, del usuario
     * @return true/false si se autentica correctamente o no
     * @throws LDAPException
     */
    public boolean isValidUser(String user, String password) throws LDAPException {

		LDAPConnection connection = connectionPool.getConnection();

		String attributeName = "uid";
		String filter = "uid=" + user + "," + base;

		 /* Se realiza la autenticacion del usuario con la password*/
		connection.authenticate(filter, password);

		/* si es correcta se obtienen los atributos del usuario*/
		LDAPSearchResults results = connection.search(base, LDAPv3.SCOPE_SUB,
				"(" + attributeName + "=" + user + ")", null, false);

		/* Se obtienen los valores para el atributo rol*/
		LDAPAttribute ldapAttribute = null;
		LDAPEntry ldapEntry = null;
		while (results.hasMoreElements()) {
			ldapEntry = (LDAPEntry) results.next();
			ldapAttribute = ldapEntry.getAttribute(atributoRol);
		}
		//ahora no vendrian en lista separadas por coma si no la lista
		String[] lista = ldapAttribute.getStringValueArray();
		
		 /* se cierra la conexion*/
		connectionPool.close(connection);

		
		 /* si comprueba si pertenece al rol correspondiente para la aplicacion*/
		boolean resultado = perteneceGrupo(lista, rol);

		if (resultado) {
			logger.info("Autenticacion Ldap correcta");
		} else {
			logger.info("Error de autenticación contra ldap");
		}
		return resultado;
	}
    
    /**
	 * Devuelve si en la lista de valores se encuentra el grupo correspondiente
	 * dado como atributo
	 * 
	 * @param lista
	 * @param grupo
	 * @return true/false
	 */
    public boolean perteneceGrupo(String[] lista, String grupo) {
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].equals(grupo)) {
				return true;
			}
		}
		return false;
	}
    
 
  public Vector<LDAPUser> getUserListLDAP() throws LDAPException {

		LDAPConnection connection = connectionPool.getConnection();
		
		/*patron para la busqueda de todos los usuarios*/
		String attb = "(" + atributoRol + "=" + rol +")";

		/*buequeda de todos los usuarios que cumplan el patron*/
		LDAPSearchResults results = connection.search(base, LDAPv3.SCOPE_SUB,
				attb, null, false);

		LDAPAttribute ldapAttribute = null;
		LDAPUser usuario = null;
		LDAPEntry ldapEntry = null;
		
		Vector<LDAPUser> listUsers = new Vector<LDAPUser>();
		
		/*guardamos los usuarios en un array de usuarios con la información que nos interesa*/
		while (results.hasMoreElements()) {
			usuario = new LDAPUser();
			usuario = inicilizaUsuario(usuario);
			ldapEntry = (LDAPEntry) results.next();
			ldapAttribute = ldapEntry.getAttribute("uid");
			usuario.setName(ldapAttribute.getStringValues().nextElement().toString().toUpperCase());
			ldapAttribute = ldapEntry.getAttribute("cn");
			usuario.setNombreCompleto(ldapAttribute.getStringValues().nextElement().toString());
			ldapAttribute = ldapEntry.getAttribute("sn");
			usuario.setNombre(ldapAttribute.getStringValues().nextElement().toString());
			ldapAttribute = ldapEntry.getAttribute("mail");
			if (ldapAttribute!=null){
				usuario.setMail(ldapAttribute.getStringValues().nextElement().toString());
			}
			
			listUsers.add(usuario);
		}

		connectionPool.close(connection);

		return listUsers;
	}
  

    /**
     * Finaliza la instancia única de LDAPManager. Una vez llamado a este metodo
     * el LDAPManager no se podrá volver a usar
     * 
     */
    public synchronized void destroy() {
        String prefixLog = "destroy: ";

        logger.debug(prefixLog + "Finalizando LDAPManager");

        connectionPool.destroy();
        instance = null;

        logger.debug(prefixLog + "LDAPManager finalizado correctamente");
    }
    
	/**
	 * Carga las propiedades del fichero de propiedades ldap.properties situado
	 * en el mismo paquete que la clase.		 
	 */
    private void cargarPropiedades() {

		Properties propiedades = new Properties();
		/* Carga del fichero de propiedades*/
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				"config" + File.separator + PROPERTIES_FILE);

		try {
			propiedades.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.base = propiedades.getProperty(BASEDN_PROPERTY);
		this.rol = propiedades.getProperty(ROL);
		this.rootdn = propiedades.getProperty(ROOTDN);
		this.passwroot = propiedades.getProperty(ROOTPW);
		this.atributoRol = propiedades.getProperty(ATRIBUTOROL);
		this.host = propiedades.getProperty(HOST);
		this.port = propiedades.getProperty(PORT);
		this.minSizePool = propiedades.getProperty(MINSIZEPOOL);
		this.maxSizePool = propiedades.getProperty(MAXSIZEPOOL);
		
		try {
			is.close();
		} catch (IOException e) {
			logger.error("Al realizar el close del fichero de propiedades");
		}
	}
    
    private LDAPUser inicilizaUsuario (LDAPUser usuario){
    	LDAPUser  us = new LDAPUser();
    	/*us.setDepID(null);
    	us.setDescripcion("-");
    	us.setId(null);
    	us.setName("-");
    	us.setNif(null);
    	us.setNombreCompleto("-");
    	us.setEmail("-");
    	us.setNombre("-");*/
    	
    	usuario = us;
    	return usuario;
    }
    
}
