package es.satec.localgismobile.fw.net.communications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;


import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.net.communications.connector.Connector;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.net.communications.exceptions.TimeOutException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

/**
 * 
 * User: dbenito; XSDDate: 11-mar-2004 Time: 13:29:55 To change this template
 * use Options | File Templates.
 * 
 * TAGS CVS
 * 
 * @author SATEC
 * @version $Revision: 1.1 $
 * 
 * Autor:$Author: satec $ Fecha Ultima Modificacion:$XSDDate: 2005/09/12
 * 16:21:15 $ $Name:  $ ; $RCSfile: HttpManager.java,v $ ; $Revision: 1.1 $ ;
 * $Locker:  $ $State: Exp $
 */



public class HttpManager {
	
	private static Logger log = Global.getLoggerFor(HttpManager.class);
	
	private String Version = "$Revision: 1.1 $";

	//private Frame _frame;

	private String _szUrl;

	private String _szMethod;

	private Connector _conn;

	private String _szContentType;

	private boolean _bUseZip;

	//private Panel _pBotones;

	private Hashtable _headers;
	
	/**
	 * Si bUsezip vale TRUE, entonces el body del método enviar se enviará
	 * comprimido y se recibirá una respuesta comprimida, que estará
	 * descomprimida al recuperarla mediante getRespuestaString o
	 * getRespuestaBytes
	 */
	public HttpManager(/*Frame frame,*/String szUrl, String szMethod,
			String szContentType, boolean bUseZip, Hashtable headers) {

		//_frame = frame;
		_szUrl = szUrl;
		_szMethod = szMethod;
		_bUseZip = bUseZip;
		_szContentType = bUseZip ? "application/x-gzip" : szContentType;

		// _pBotones = core.DataMovil._panelBotones;
		_headers = headers;

		/*
		 * if(_pBotones == null) { System.out.println("[HttpManager.HttpManager]
		 * Soy un Applet"); _pBotones = core.DataMovilApplet._panelBotones; }
		 */
	}

	public void enviar(byte[] body) throws LoginException, RolesException,
			ValidationException, NoConnectionException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

//		System.out.println("[HttpManager.enviar - "
//				+ sdf.format(new java.util.Date()) + "] Primer Intento");

		// llamamos al metodo factoria para obtener el Connect correcto segun los argumentos
		try {
			//BORRAR
			log.error(_szUrl);
			_conn = Connector.getConnector(_szUrl, _szMethod, false/* , _frame */,
					_szContentType, _headers);
			body = this.comprime(body);
			
		} catch (MalformedURLException e) {
			throw new NoConnectionException("MalformedURLException.");
		} catch (IOException e) {
			throw new NoConnectionException("Comprimiendo el body a enviar.");
		}


		boolean bIsFile = _szUrl.startsWith("file");
		boolean bRet = false;

		try {
			
//			System.out.println("[HttpManager.enviar - "
//					+ sdf.format(new java.util.Date()) + "] bIsFile: "
//					+ bIsFile);
			
			if (bIsFile) {
				// Para el caso de PUT FILE, se ha implementado un conector
				// ESPECIAL, cuyo comportamiento será TRANSPARENTE
				this.getFile(body);
			} else {
//				System.out.println("[HttpManager.enviar - "
//						+ sdf.format(new java.util.Date())
//						+ "] antes de getAlgo()");
				this.getAlgo(body);
			}
			
			bRet = true;
			
		} catch (LoginException e) {
//			System.out.println("LANZANDO LOGINEXCEPTION DESDE HTTPMANAGER (ENVIAR)");
			throw (e);
		} catch (RolesException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (Exception e) {
//			System.out.println("[HttpManager.enviar - "
//					+ sdf.format(new java.util.Date()) + "] PETAZO: "
//					+ e.toString());
			log.error("Error al enviar",e);
		}

//		System.out.println("[HttpManager.enviar - "
//				+ sdf.format(new java.util.Date()) + "] PROGRESS PARADA");

		if (!bRet) {
//			System.out
//					.println("[HttpManager.enviar - "
//							+ sdf.format(new java.util.Date())
//							+ "] Han fallado los 3 intentos (o el fichero no existe). LANZAR EXCEPCION.");
			if (bIsFile){
				throw new NoConnectionException("El fichero no existe.");
			}
			else {
				throw new NoConnectionException("Han fallado todos los intentos de conexión.");
			}
		}
		
	}

	private void getFile(byte[] body) throws LoginException, RolesException,
			IOException, TimeOutException, ValidationException,
			NoConnectionException {
//		
//		System.out.println("[HttpManager.getFile - "
//				+ (new SimpleDateFormat("HH:mm:ss.SS"))
//						.format(new java.util.Date()) + "] INICIO");
//		
		_conn.send(body);
	}


	private void getAlgo(byte[] body) throws ValidationException, RolesException, LoginException, TimeOutException, IOException, NoConnectionException  {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SS");

		/*
		 // 1.- Comprobar si existe alguna conexión. OJETE: Puede que Chema pete
		 // porque
		 // estamos ejecutando en un PC y claro, el connection manager no existe.
		 // No se puede abortar a saco porque chema haya cascado.

		 // * TODO: Preguntar a Hugo antes de cargarme todo este código ... puede
		 // ser neceario
		 // * que añadamos la clase IConnectionManager
		 try {
		 System.out.println("[HttpManager.getAlgo - "
		 + sdf.format(new java.util.Date())
		 + "] CHEMA: antes del getInstance");
		 IConnectionManager cm = ConnectionManagerFactory
		 .getConnectionManager();

		 boolean bConnAvl = cm.connectionAvailable();
		 System.out.println("[HttpManager.getAlgo - "
		 + sdf.format(new java.util.Date())
		 + "] CHEMA: Conexion disponible: " + bConnAvl);
		 if (!bConnAvl) {
		 System.out.println("[HttpManager.getAlgo - "
		 + sdf.format(new java.util.Date())
		 + "] CHEMA: Antes del connect");
		 cm.connect(Constantes.CONNECTION_TIMEOUT);
		 System.out.println("[HttpManager.getAlgo - "
		 + sdf.format(new java.util.Date())
		 + "] CHEMA: Después del connect");
		 }
		 } catch (Exception e) {
		 System.out.println("[HttpManager.getAlgo - "
		 + sdf.format(new java.util.Date())
		 + "] Excepción con el Connection Manager: " + e.toString());
		 }
		 */

		// 2.- SUPONEMOS que ya hay conexión. Pues a enviar.
		// A no ser que salte el T.O., reintento.
		boolean bRet = false;
		
		try {
			
//			System.out.println("[HttpManager.getAlgo - "
//					+ sdf.format(new java.util.Date())
//					+ "] INTENTO 1. o socket o url.");

			_conn.send(body);

//			System.out.println("[HttpManager.getAlgo - "
//					+ sdf.format(new java.util.Date()) + "] SEND OK");
//			
			bRet = true;
			
		} catch (LoginException e) {
//			System.out.println("LANZANDO LOGINEXCEPTION DESDE HTTPMANAGER (GETALGO)");
			log.error("Excepcion en el login",e);
			throw (e);
		} catch (RolesException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);			
		} catch (Exception e) {
			
			//si se ha producido una exception distinta a las de validación,
			//es que ha fallado la conexión ... solo lanzo la Expection hacia
			//arriba si es de tipo TimeOutException ... 
//			System.out.println("[HttpManager.getAlgo - " + sdf.format(new java.util.Date())
//					+ "] EXCEPCION 1 (desde el send): " + e.toString());
//			
			log.error("Excepcion",e);
			
			if (e instanceof TimeOutException) {
//				System.out.println("[HttpManager.getAlgo - "
//						+ sdf.format(new java.util.Date())
//						+ "] RELANZANDO TIME_OUT: " + e.toString());
				throw ((TimeOutException) e);
			}
			
		}

		//vamos a volver a reintentar la conexión nuevamente haciendo un nuevo send
		//para un nuevo Connector que crearemos para tal efecto
		
		// 2B.- Ha petado el intento de conexion de arriba -> Fuerzo a usar un
		// Socket como Connector
		if (!bRet || _conn.getStatusCode() == 401) // Si salta un 401 es porque
		// necesito autenticarme:
		// Socket por Pelotas.
		{
//			System.out.println("[HttpManager.getAlgo - "
//					+ sdf.format(new java.util.Date())
//					+ "] INTENTO 2. socket cdc.");
			_conn = Connector.getConnector(_szUrl, _szMethod,
					true/* , _frame */, _szContentType, _headers);
			
			_conn.send(body);
		}
	}

	public void enviar(String szBody) throws LoginException, RolesException,
			ValidationException, NoConnectionException {
		
//		System.out.println("[HttpManager.enviar] INICIO.");
		if (szBody == null || szBody.trim().equals("")) {
//			System.out.println("[HttpManager.getRespuestaBytes] Dentro IF.");
			this.enviar((byte[]) null);
		} else {
//			System.out.println("[HttpManager.getRespuestaBytes] Dentro ELSE.");
			this.enviar(szBody.getBytes());
		}
	}

	//añadimos un método para que ad+ de enviar, nos devuelva la respuesta
	public byte[] enviarYRecibir(byte[] body) throws LoginException,
			RolesException, ValidationException, NoConnectionException {

		//primero enviamos el body que se nos haya indicado
		this.enviar(body);
		
		byte[] result = this.getRespuestaBytes();
				
		return result;
	}
	
	public byte[] getRespuestaBytes() throws NoConnectionException  {
//		System.out.println("[HttpManager.getRespuestaBytes] INICIO.");
		try {
			return this.descomprime(_conn.getResponseBytes());
		} catch (IOException e) {
			throw new NoConnectionException("Descomprimiendo la respuesta recibida");
		}
	}

	public String getRespuestaString() throws NoConnectionException  {
		
//		System.out.println("[HttpManager.getRespuestaString] INICIO");
		
		byte[] respuestaBytes = this.getRespuestaBytes();
		
		if (respuestaBytes != null) {
			String szRet = new String(respuestaBytes);
			// System.err.println("[HttpManager.getRespuestaString]
			// bytes->String ok. chars: "+szRet.length());
			return szRet;
		} else
			return null;
	}

	private byte[] comprime(byte[] bodyIni) throws IOException {
		if ((!_bUseZip) || (bodyIni == null))
			return bodyIni;

//		System.out.println("[HttpManager.trataBody] A comprimir tocan");
		ByteArrayOutputStream baOs = new ByteArrayOutputStream();
		GZIPOutputStream gzOs = new GZIPOutputStream(baOs);
		gzOs.write(bodyIni, 0, bodyIni.length);
		gzOs.finish();
		gzOs.flush();
		gzOs.close();

		baOs.flush();
		baOs.close();

		byte[] byteRet = baOs.toByteArray();
//		System.out.println("[HttpManager.comprime] COMPRIMIDO: "
//				+ bodyIni.length + " -> " + byteRet.length);
//		System.out
//				.println("[HttpManager.trataBody] RATIO: "
//						+ (100 - ((byteRet.length * 100) / (bodyIni.length > 0 ? bodyIni.length
//								: 1)) + "%"));
		return byteRet;
	}

	/**
	 * Esta función comprueba primero la cabecera "Content-type" recibida para
	 * saber si será necesario o no llevar a cabo la descompresión del array de
	 * bytes. En caso negativo, simplemente devuelve el array sin realizar
	 * ningún proceso con él.
	 */
	private byte[] descomprime(byte[] bytesIni) throws IOException {
		// new Dialogo(new Frame(), "TestDownload", "Antes Descomprime", true);
		String szContentType = _conn.getResponseHeader("Content-type");
//		System.out.println("[HttpManager.descomprime] szContentType: "
//				+ szContentType);
		if (szContentType.equalsIgnoreCase("application/x-gzip") == false
				&& _szUrl.endsWith("gz") == false) {
			return bytesIni;
		}

//		System.err
//				.println("[HttpManager.descomprime] A DESCOMPRIMIR. length inicial: "
//						+ bytesIni.length);// +"; recibido (comprimido): "+new
		// String(bytesIni));

		return descomprimeStatic(bytesIni);
	}

	/**
	 * Recibe un array de bytes comprimidos mediante el algoritmo de GZIP y
	 * devuelve los bytes descomprimidos.
	 */
	public static byte[] descomprimeStatic(byte[] bytesIni) throws IOException {
		ByteArrayInputStream baIs = new ByteArrayInputStream(bytesIni);
		GZIPInputStream gzIs = new GZIPInputStream(baIs);
		ByteArrayOutputStream baOs = new ByteArrayOutputStream(
				30 * bytesIni.length);
		byte[] buf = new byte[1024];
		int nCont = 0;
		// new Dialogo(new Frame(), "TestDownload", "Inicio descompresión",
		// true);
		try {
			while ((nCont = gzIs.read(buf)) > 0) {
				// //System.out.println("[HttpManager.descomprime] leido:
				// "+nCont);
				baOs.write(buf, 0, nCont);
				// //System.out.println("[HttpManager.descomprime] tras write:
				// "+new String(buf, 0, nCont));
			}
		} catch (java.io.EOFException e) {
//			System.out.println("[HttpManager.descomprimeStatic] EOFException: "
//					+ e.toString());
		}

		baOs.flush();
		baOs.close();
		gzIs.close();
		baIs.close();
		gzIs = null;
		baIs = null;

		// new Dialogo(new Frame(), "TestDownload", "Fin descompresión", true);
		byte[] bytesInfl = baOs.toByteArray();
		// System.out.println("[HttpManager.descomprimeStatic] " +
		// bytesIni.length + " -> " + bytesInfl.length);
//		System.err
//				.println("[HttpManager.descomprimeStatic] "
//						+ bytesIni.length
//						+ " -> "
//						+ bytesInfl.length
//						+ " (RATIO: "
//						+ (100 - 100 * bytesIni.length
//								/ (bytesInfl.length > 0 ? bytesInfl.length : 1))
//						+ "%)");

		//		new Dialogo(new Frame(), "TestDownload", "Fin generacion array", true);
		return bytesInfl;
	}

	/**
	 * Método que se emplea para comprobar si existe conexion o no para una url dada
	 * @return 
	 * @return Valor booleano que indica si hay o no conexion
	 * @throws Exception 
	 * @throws ValidationException 
	 * @throws RolesException 
	 * @throws LoginException 
	 * @throws Exception 
	 * @throws ValidationException 
	 * @throws RolesException 
	 * @throws LoginException 
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void existeConexion() throws LoginException, RolesException, ValidationException, NoConnectionException {

		//llamamos al metodo factoria para obtener el Connect correcto segun los argumentos
		Connector conn;

		try {
			conn = Connector.getConnector(_szUrl, _szMethod, false/* , _frame */,
					_szContentType, _headers);
		} catch (MalformedURLException e) {
			throw new NoConnectionException("MalformedURLException.");
		}

		//intentamos enviar algo para ver si realmente hay conexion, ya que en
		//caso contrario, se lanzarán las Exceptions oportunas		
		enviar("");

	}
}
