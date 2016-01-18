package es.satec.localgismobile.fw.validation.implementations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.node.SimpleNode;
import com.japisoft.fastparser.node.ViewableNode;
import com.japisoft.fastparser.walker.TreeWalker;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.crypto.CipherStreamFactory;
import es.satec.localgismobile.fw.net.communications.HttpManager;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.net.cookie.CookieManager;
import es.satec.localgismobile.fw.validation.bean.ValidationBean;
import es.satec.localgismobile.fw.validation.exceptions.ConectionValidationException;
import es.satec.localgismobile.fw.validation.exceptions.FileValidationException;
import es.satec.localgismobile.fw.validation.exceptions.LoginCancelException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

/**
 * Clase que se encarga de comprobar qué tipo de validación debe realizarse, on-line u off-line
 * y realizar la validación en cada caso.
 */

public class Validation {

	private ValidationBean bean = new ValidationBean();
	private static int contador;
	
	protected static Logger logger = Global.getLoggerFor(Validation.class);

	/**
	 * Método para validar el usuario.
	 * 
	 * @param url URL contra la que validar.
	 * @param user Nombre del usuario.
	 * @param password Password del usuario.
	 * @return true si la validación es correcta; false en caso contrario.
	 * @throws ValidationException si la validacion es incorrecta
	 */
	public boolean valida(String url, String user, String password) throws ValidationException, RolesException, LoginCancelException {
		logger.debug("Entra en valida. Contador=" + contador);
		if (validaOnLine(url, user, password)) {
			try {
				guardaValidacionOnLine();
			} catch (FileValidationException e) {}
		}
		else {
			validaOffLine(user, password);
		}
		
		return true;
	}

	/**
	 * Intenta realizar la validación on-line contra la url recibida.
	 * En caso de que sea correcta la validación se guardan los datos del usuario en un bean B_Validation.
	 * 
	 * @param url URL contra la que se valida.
	 * @param user Nombre del usuario.
	 * @param password Password del usuario.
	 * @return true si hay conexion y la validación es correcta; false si no hay conexión.
	 * @throws ValidationException si la validacion es incorrecta
	 */
	private boolean validaOnLine(String url, String user, String password) throws ValidationException, RolesException, LoginCancelException {

		String urlF = url + Global.LOGIN_ACTION_URI;
		HttpManager mng = new HttpManager(urlF, "POST", "application/x-www-form-urlencoded", false, null);
		try {
			String device_id=System.getProperty("device_id");
			//BORRAR
			device_id = "20";
			/* Encriptar la contraseña */
			/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStream encryptedOutputStream = CipherStreamFactory.getOutputStream(baos);
			ObjectOutputStream oos = new ObjectOutputStream(encryptedOutputStream);
			oos.writeObject(password);
			String encryptedPassword = baos.toString();
			oos.close();*/

			mng.enviar("j_username="+user+"&j_password="+password+"&device_id="+device_id);

			String XML = mng.getRespuestaString();
			logger.debug("XML Respuesta: " + XML);
			//Leemos el XML para guardar los datos del usuario en el bean
			if ((XML==null)||(XML.equals(""))) {
				throw new LoginCancelException("No se pudo realizar la validación: error en el servidor");
			}
			
			Vector datosIncorrectos = get_tag((String) XML, "incorrectpassword");
			if (!datosIncorrectos.isEmpty()) throw new ValidationException("Datos de usuario incorrectos");
			
			Vector permisoDenegado = get_tag((String) XML, "permissiondenied");
			if (!permisoDenegado.isEmpty()) throw new RolesException("No tiene permiso para ejecutar la aplicacion");
			
			
			logger.debug("Validación on-line correcta");

			Hashtable permisos = getPermisos(XML);

			Vector userId = get_tag((String) XML, "userid");
			Vector name = get_tag((String) XML, "name");

			bean.setPermisos(permisos);
			bean.setPassword(password);
			bean.setUsuario(user);
			if (userId!=null && !userId.isEmpty()) bean.setIdUsuario((String) userId.elementAt(0));
			if (name!=null && !name.isEmpty()) bean.setNombre((String) name.elementAt(0));
			bean.setDispositivo(Global.getDeviceId());
					
			//indicamos que el último tipo de validación empleado fue el clasico
			bean.setCertValidation(false);

		} catch (LoginCancelException e) {
			throw e;
		} catch (RolesException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (NoConnectionException cve) {
			logger.debug("No hay conexion");
			return false;
		} catch (Exception e) {
			logger.error("Error en la validación", e);
			return false;
		}

		return true;
	}

	/**
	 * Método que busca etiquetas con el nombre que se le pasa por parámetro de un XML.
	 * 
	 * @param xml XML que se quiere leer.
	 * @param nombre Nombre de la etiqueta que se quiere buscar.
	 * @return Vector con los campos de las etiquetas señaladas por el parámetro nombre.
	 */
	private Vector get_tag(String xml, String nombre) {
		
		Parser p = new Parser();
		try {
			// pasar un string a imputstream para parsear
			ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
			p.setInputStream(stream);
			p.parse();
		} catch (Exception e) {
			logger.error("Error al parsear el XML", e);
		}
		SimpleNode root = (SimpleNode) p.getDocument().getRoot();
		TreeWalker t = new TreeWalker(root);

		Enumeration e = t.getTagNodeByName(nombre, true);
		Vector cont=new Vector();
		while (e.hasMoreElements()) {
			ViewableNode g = ((SimpleNode) e.nextElement()).getViewChildAt(0);
			if (g!=null) cont.addElement(g.toString());
		}
		return cont;
	}

	private Hashtable getPermisos(String xml) {
		Parser p = new Parser();
		try {
			// pasar un string a imputstream para parsear
			ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
			p.setInputStream(stream);
			p.parse();
		} catch (Exception e) {
			logger.error("Error al parsear el XML", e);
		}
		SimpleNode root = (SimpleNode) p.getDocument().getRoot();
		Enumeration e = root.getNodeByName("layer", true);
		Hashtable layerPerms = new Hashtable();
		while (e.hasMoreElements()) {
			SimpleNode layer = ((SimpleNode) e.nextElement());
			String name = layer.getAttribute("name");
			int numPerms = layer.childCount();
			int[] idPerms = new int[numPerms];
			
			Enumeration e2 = layer.getNodeByName("idPerm", true);
			for (int i=0; i<numPerms && e2.hasMoreElements(); i++) {
				SimpleNode idPerm = ((SimpleNode) e2.nextElement());
				String idPermAsString = idPerm.getViewChildAt(0).toString();
				int idPermAsInt = Integer.valueOf(idPermAsString).intValue();
				idPerms[i] = idPermAsInt;
			}
			
			layerPerms.put(name, idPerms);
		}
		return layerPerms;
	}
	
	/**
	 * Método que guarda en un fichero encriptado los datos del usuario en caso de que
	 * haya sido correcta la validación on line.
	 * 
	 * @throws FileValidationException Si no se puede escribir el fichero.
	 */
	private void guardaValidacionOnLine() throws FileValidationException {
		//Guardamos que ha sido una autenticacion on-line
        Global.setOnlineValidation(true);
		CipherStreamFactory csf=new CipherStreamFactory();
		try {
			File fBusqueda=new File(Global.APP_PATH+File.separator+Global.DB_PATH);
			
			if (!fBusqueda.exists())
			{
				logger.debug("Crea el directorio");
				//Si no existe el directorio lo creamos para poder guardar
				fBusqueda.mkdirs();
			}
			
			FileOutputStream outputStreamOrig = new FileOutputStream(Global.APP_PATH+File.separator+Global.DB_PATH+Global.DB_NAME);

			OutputStream encryptedOutputStream = csf.getOutputStream(outputStreamOrig);
			ObjectOutputStream oos = new ObjectOutputStream(encryptedOutputStream);

			//El bean tiene que ser serializable para poder escribir en el ObjectOutputStream
			logger.debug("EL BEAN es ........" + bean);
			
			// Almacenar el bean en un objeto global para que el cliente pueda acceder a él
			Global.setValidationBean(bean);
			
			oos.writeObject(bean);
			oos.close();
			
		} catch (IOException e) {
			logger.warn("Error al escribir fichero en disco", e);
			throw new FileValidationException("Error al escribir fichero en disco");
		}	
	}

	/**
	 * Método que realiza la validación off-line contra el fichero guardado en disco.
	 * En caso de no existir devuelve una ConectionValidationException.
	 * 
	 * @param user Nombre del usuario.
	 * @param password Password del usuario.
	 * @throws ValidationException Si la validación es incorrecta.
	 * @throws ConectionValidationException Si no existe el fichero.
	 * @throws FileValidationException Si se produce un error al leer el fichero.
	 */
	private void validaOffLine(String user, String password) throws ValidationException, ConectionValidationException, FileValidationException {
		
		// Guardamos que ha sido una autenticacion off-line
		Global.setOnlineValidation(false);

		// Buscamos el fichero
		File fBusqueda = new File(Global.APP_PATH+File.separator+Global.DB_PATH+Global.DB_NAME);
		if (!fBusqueda.exists()) {
			//Devolver excepcion de conexion
			String msg="Se ha producido un error de conexión al realizar la validación";
			logger.debug("El fichero no existe");
			logger.warn(msg);
			throw new ConectionValidationException(msg);
		}
		
		CipherStreamFactory csf=new CipherStreamFactory();
		try {
			FileInputStream inputStreamOrig = new FileInputStream(fBusqueda.getAbsolutePath());
			InputStream encryptedInputStream = csf.getInputStream(inputStreamOrig);

			ObjectInputStream ois=new ObjectInputStream(encryptedInputStream);	
			ValidationBean bean=(ValidationBean) ois.readObject();
			ois.close();
			
			//tras recuperar el B_validation, comprobamos si la última validación online fue con certificado o no
			if(bean.isCertValidation()){
				throw new ValidationException("La última validación ON-LINE no se ha realizado con login y password.");
			}

			// Almacenar el bean en Global
			Global.setValidationBean(bean);			

			//Comparamos los datos de usuario
			if ((bean.getUsuario().equals(user))&&(bean.getPassword().equals(password))) {
				//Esta bien validado asi que ponemos el contador de validaciones erroneas a 0
				logger.debug("La validacion off-line es correcta ");
				contador=0;
			}
			else {
				//Pedir datos al usuario, 
				if (contador==(Global.VECES-1)) {
					//Borrar el archivo de disco
					File f = new File(fBusqueda.getAbsolutePath());
					f.delete();
					logger.debug("Se ha borrado el fichero  "+f.getAbsolutePath());
				}
				contador++;
				logger.debug("La validacion off-line es incorrecta. Contador="+contador);
				throw new ValidationException("Validación off-line incorrecta");
			}
		} catch (Exception e) {
			String msg="Error al leer fichero de disco";
			logger.warn(msg, e);
			throw new FileValidationException(msg);
		}
		
	}

	public void logout(String address, int port, String baseUri) {
		CookieManager cookieManager = CookieManager.getInstance();
		
		String domain = address + ":" + port;
		
		cookieManager.clearSitePathCookies(domain, baseUri);
	}
}