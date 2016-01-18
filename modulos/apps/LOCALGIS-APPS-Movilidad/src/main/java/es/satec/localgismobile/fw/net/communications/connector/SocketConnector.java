/**
 * SocketConnector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications.connector;

import iaik.me.keymgmt.KeyAndCert;
import iaik.me.security.ssl.SSLContext;
import iaik.me.security.ssl.SSLException;
import iaik.me.security.ssl.SSLTransport;
import iaik.me.x509.X509Certificate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.net.communications.BasicAuthen;
import es.satec.localgismobile.fw.net.communications.DigestAuthen;
import es.satec.localgismobile.fw.net.communications.Runna;
import es.satec.localgismobile.fw.net.communications.RunnableException;
import es.satec.localgismobile.fw.net.communications.core.Constantes;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.net.communications.exceptions.TimeOutException;
import es.satec.localgismobile.fw.net.cookie.Cookie;
import es.satec.localgismobile.fw.net.cookie.CookieManager;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
//////////////CLASE SOCKET CONNECTOR: tiene la lógica chunga de user y password, SSL...
////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

/** Esta está especializada en tratar HTTPS */
public class SocketConnector extends Connector {
		
	Logger log = Global.getLoggerFor(SocketConnector.class);
	
	private String _sUser, _sPass;

	// Estas 3 implemente un "Single Sign On" cuando hay que autenticarse. (solo
	// se usan en Socket Connector)
	private static String userLogin = null;

	private static int numberfailes = 0;

	private static String passLogin = null;

	// Sustituídas por la Hashtable de la clase madre.
	// private Vector _vResponseHeaderNames;
	// private Vector _vResponseHeaderValues;

	private String _sStatusText;

	// private String _sResponseText; --> Ahora se leen bytes. Ya se formará un
	// String para aquel que lo quiera.

	private String _sResponseHeaders;

	private boolean _bIsSecure = false;

	private Socket _normalSocket;

	private SSLTransport _secureSocket;

	private boolean _bPassHidden = false;
	
	private boolean noClientCertificate = false;

	public SocketConnector(String szUrl, String method, boolean bIsHttps,
			String szContentType) throws MalformedURLException {

		super(szUrl, method, bIsHttps, szContentType);

		this._bIsSecure = bIsHttps || _nPort == 443;

		if (_bIsSecure && (_nPort == 80 || _nPort < 0))
			_nPort = 443; // Usar el puerto seguro por defecto si no está
		// definido o es el 80

		Init();
	}

	/**
	 * Private function called by both <code>open</code> methods to initialize
	 * a few internal variables.
	 */

	private void Init() {

		// _sResponseText = null;
		_aBytesResponse = null;
		_sStatusText = null;
		_sResponseHeaders = null;
		// _vResponseHeaderNames = new Vector();
		// _vResponseHeaderValues = new Vector();

		_sUser = null;
		_sPass = null;
		if (userLogin != null) {
			_bPassHidden = true;
		}
	}

	private void setAuthentication(String u, String p) {

		// aqui debemos de incluir en la cookie el u:p indicado
		_sUser = new String(u);
		_sPass = new String(p);
	}

	// private String getAllHeaders() {
	// return new String(_sResponseHeaders);
	// }

	// public String getResponseHeader(String header) {
	// String szRet = super.getResponseHeader(header);
	// return ((szRet==null) ? "" : szRet);
	// int indx = findResponseHeader(header);
	// if (indx < 0)
	// return "";
	// else
	// return (String) _vResponseHeaderValues.elementAt(indx);
	// }
	// public String getResponseHeaderInverse(String header) {
	// return this.getResponseHeader(header);
	// int indx = findRequestHeaderInverse(header);
	// if (indx < 0)
	// return "";
	// else
	// return (String) _vResponseHeaderValues.elementAt(indx);
	// }

	// private int findResponseHeader(String header) {
	// int indx;
	// for (indx = 0; indx < _vResponseHeaderNames.size(); indx++) {
	// String u = (String) _vResponseHeaderNames.elementAt(indx);
	// if (u.equalsIgnoreCase(header)) return indx;
	// }
	// return -1;
	// }
	//
	// private int findRequestHeaderInverse(String header) {
	//
	// int indx;
	// for (indx = (_vResponseHeaderNames.size()-1); indx >= 0; indx--) {
	// String u = (String) _vResponseHeaderNames.elementAt(indx);
	// if (u.equalsIgnoreCase(header)) return indx;
	// }
	// return -1;
	// }

	// basicamente, lo que hace este metodo es invocar al metodo sendRequest
	// y luego comprobar el codigo devuelto y actuar en consecuencia
	public void send(byte[] body) throws LoginException, RolesException,
			IOException, TimeOutException, ValidationException,
            NoConnectionException {
		
		//System.out.println("[SocketConector.send] INICIO. body: " + body);
		boolean auth = false;

		int code = 0;

		// bucle infinito para ejecutarse mientras no se ha return
		while (true) {

			// invocamos al metodo sendRequest de esta misma clase
			code = sendRequest(body);
			
			//System.out.println("[SocketConnector.send] codeeeeeeeeeeeeeeeee : "	+ code);
			_nStatus = code;

			if (code == -1)
				throw (new NoConnectionException(code)); // return false;

			if (code / 100 <= 1)
				return; // throw (new Exception("HTTP Code == "+code)); //return
			// false;
			// if (code / 100 <= 1) throw (new Exception("HTTP Code ==
			// "+code)); //return false;
			if (code / 100 == 2) {
				// Hemos entrado guardamos u y p para el futuro
				if (_sUser != null) {

					// * TODO: puede que en este punto sea necesario leer de la
					// cookie u:p
					userLogin = _sUser;
					passLogin = _sPass;
					numberfailes = 0;
				}
				return; // return true;
			}
			if (code / 100 >= 5)
				throw (new NoConnectionException(code)); // return false;
			if (code / 100 == 4 && code != 401 && code != 407)
				throw (new NoConnectionException(code)); // return false;
			if (code == 401 && auth)
				throw (new NoConnectionException(code)); // return false;
			if (code == 401) {
				Authenticate();
				if (numberfailes > 2) {
					// Permitimos 3 reintentos si hay password antes de mostrar
					// de nuevo el password
					auth = true;
				}
			}
			if (code == 401 && _sUser == null)
				throw (new NoConnectionException(code)); // return false;
			if (code == 401 && _sUser.length() == 0)
				throw (new NoConnectionException(code)); // return false;
			if (code == 302)// REDIRECT
			{
				//System.out.println("[SocketConector.send] MARCHANDO UN REDIRECT");

				String szNewLoc = null;
				int nIni = _sResponseHeaders.indexOf("Location:");

				if (nIni > 0) {

					nIni += 10; // Saltar "Location: "
					int nFin = _sResponseHeaders.indexOf("\n", nIni);
					szNewLoc = _sResponseHeaders.substring(nIni, nFin);
//					System.out.println("[SocketConnector.send] szNewLoc: "
//							+ szNewLoc);
					// try {
					// URL url = new URL(szNewLoc);
					int currentPos = 5;
					String protocol = szNewLoc.substring(0, currentPos)
							.toLowerCase();
					if (protocol.equals("http:"))
					{
						currentPos = 4;
					}
					currentPos += 3;

					boolean defaultPort = false;
					boolean rootFolder = false;
					int separatorIndex = szNewLoc.indexOf(':', currentPos);
					if (separatorIndex != -1) {
						this._sHost = szNewLoc.substring(currentPos,
								separatorIndex);
						currentPos = separatorIndex + 1;
					}
					else {
						separatorIndex = szNewLoc.indexOf('/', currentPos);
						if (separatorIndex != -1 ){
							this._sHost = szNewLoc.substring(currentPos,
									separatorIndex);
							currentPos = separatorIndex;
							defaultPort = true;
						}
						else {
							this._sHost = szNewLoc.substring(currentPos,
									szNewLoc.length());
							defaultPort = true;
							rootFolder = true;
						}
					}								

					//System.out.println("HOST!!!!!!!!!!!!!!!!! "+_sHost);
					
					if (defaultPort == false){
						separatorIndex = szNewLoc.indexOf('/', currentPos);
						if (separatorIndex != -1) {
							this._nPort = Integer.parseInt(szNewLoc.substring(
									currentPos, separatorIndex));
							currentPos = separatorIndex;
						}
						else {
							this._nPort = Integer.parseInt(szNewLoc.substring(
									currentPos, szNewLoc.length()-1));
							rootFolder = true;
						}
					}

					this._sFile = "";
					if (rootFolder == false){
						separatorIndex = szNewLoc.indexOf(';', currentPos);
						if (separatorIndex != -1){
							this._sFile = szNewLoc.substring(currentPos, separatorIndex);							
							currentPos = separatorIndex+1;
							
							separatorIndex = szNewLoc.indexOf('?', currentPos);	
							if (separatorIndex != -1){
								currentPos = separatorIndex;
								this._sFile += szNewLoc.substring(currentPos, szNewLoc.length());
							}
						}
						else {
							this._sFile = szNewLoc.substring(currentPos, szNewLoc.length());
						}
						
					}

//					System.out.println("[SocketConnector.send] Url: "
//							+ protocol + " " + this._sHost + " " + this._nPort
//							+ " " + this._sFile);

					// } catch (MalformedURLException e) {
					// System.out
					// .println("[SocketConnector.send] Error en direccion donde
					// redireccionar: "
					// + e.toString());
					// e.printStackTrace(); // To change body of catch
					// // statement use Options | File
					// // Templates.
					// throw new Exception("Error en Location to Redirect: "
					// + szNewLoc + ". Base Exception: "
					// + e.toString()); // return false;
					// }
				}
			}

		}// fin while(true)
	}

	// metodo para enviar un Array de bytes por la request
	private int sendRequest(byte[] body) throws IOException, TimeOutException,
			 ValidationException {
		
//		System.out.println(System.currentTimeMillis()
//				+ "[SocketConnector.sendRequest] INICIO: body: "
//				+ ((body != null) ? new String(body) : "NULLLLL"));

		if (_nPort < 0)
			_nPort = 80;

		// nos conectamos a la parte servidora...
		boolean bRet = connect();
		if (!bRet)
			return -1;

//		System.out.println(System.currentTimeMillis()
//				+ "[SocketConnector.sendRequest] tras connect.");

		String request;
		String line;
		int len;

		_nStatus = -1;

		// _vResponseHeaderNames.removeAllElements();
		// _vResponseHeaderValues.removeAllElements();

		// limpiamos los headers del response
		this.clearResponseHeaders();
		_sResponseHeaders = "";
		_sStatusText = "";

		len = (body != null) ? body.length : 0;
//		System.out.println(System.currentTimeMillis()
//				+ "[SocketConnector.sendRequest] len: " + len);

		// componemos la request a enviar al servidor
		request = _sMethod + " " + _sFile + " HTTP/1.0\r\n";
		// request += "Accept: image/gif, image/x-xbitmap, image/jpeg,
		// image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel,
		// application/vnd.ms-powerpoint, application/msword, */*\r\n";
		// request += "Accept-Encoding: gzip, deflate\r\n";
		// request += "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT
		// 5.1; SV1; .NET CLR 1.1.4322)\r\n";
		request += "Host: " + _sHost /* + ":" + _nPort */+ "\r\n";
		// request += "Host: " + _sHost + "\r\n";
		request += "Content-Length: " + len + "\r\n";
		// request += "Connection: Keep-Alive\r\n";
		// request += "Cache-Control: no-cache\r\n";
		// request += "Accept-Language: es\r\n";

		// añadimos las headers de la request que nos hayan pasado
		// en el constructor como parametro
		request += this.putRequestHeaders();
		request += "\r\n";

		// ///////////////////
		// Enviamos lo que tenemos
		// //////////////////
//		System.out.println(System.currentTimeMillis()
//				+ "[SocketConnector.sendRequest] request: " + request);
//		
		
		 try
		 {
			 
		// obtenemos el OutputStream donde escribir la peticion
		OutputStream os = outputPipe();
		os.write(request.getBytes());
		
		//---------------------------------------------------------------------------------------------------------------
		
		//Añadiendo bytes enviados a la clase que lleva las estadísticas

		if (body != null)
			os.write(body);
		os.flush();
		
		// os.close();
		
		 } catch (Exception e)
		 {
		 	 log.error("Error enviando peticion al servidor.",e);
		 }


		// //////////////////
		// 1,2,3... ¡¡A leer esta vez!!
		// //////////////////

		 //System.out.println(System.currentTimeMillis()+ "[SocketConnector.sendRequest]
		//¡¡¡A LEER -cabeceras- TOCAN!!!");

		// obtenemos el InputStream de donde leer la respuesta
		
		InputStream is = inputPipe();

		// No se puede usar un Buffered porque consume el InputStream y luego me
		// cruje para leer como bytes lo que venga después de las cabeceras.
		// InputStreamReader in = getReader(is);
		try {
			// 1.- OBTENEMOS EL STATUS_CODE -> _nStatus
			_sStatusText = this.readLine(is);
//			System.out.println(System.currentTimeMillis()
//					+ "[SocketConnector.sendRequest] _sStatusText: "
//					+ _sStatusText);
			// Me puede llegar un 100 Continue, lo que significa que debo seguir
			// leyendo cabeceras tras leer una cabecera vacía
			parseStatusCode();
			if (100 == _nStatus) {
				// Debo leer hasta línea vacía y entonces volver a cargar el
				// Status
				do {
					line = this.readLine(is);
				} while (line.length() > 0);
				_sStatusText = this.readLine(is);
//				System.out.println(System.currentTimeMillis()
//						+ "[SocketConnector.sendRequest] _sStatusText(2): "
//						+ _sStatusText);
			}


			// 2.- LEEMOS LOS HEADERS -> Los 'headers' son la lineas que hay
			// hasta un ""
			
			line = this.readLine(is);
//			System.out.println(System.currentTimeMillis()
//					+ "[SocketConnector.sendRequest] line (a): " + line);
			while (line.length() > 0) {
				if (line != null)
					_sResponseHeaders += line + "\n";
				//// System.out.println ("Line respo->" + line);
				line = this.readLine(is);
//				System.out.println(System.currentTimeMillis()
//						+ "[SocketConnector.sendRequest] line (b): " + line);
			}

			// 3.- PARSEAMOS LOS HEADERS Y EL STATUS CODE
			parseHeaders();
//			System.out
//					.println("[SocketConnector.sendRequest] Tras parseHeaders");
			parseStatusCode();
//			System.out
//					.println("[SocketConnector.sendRequest] Tras parseStatusCode");
//			

			// 4.- LEEMOS EL CUERPO DE LA RESPUESTA: Debemos conocer primero el
			// Content-Length
			
			int nContentLength;
			try {

				String szContentLength = this
						.getResponseHeader("Content-length");
				if (szContentLength == null
						|| szContentLength.trim().length() == 0)
					nContentLength = -1;
				else
					nContentLength = Integer.parseInt(szContentLength);

				// además, leemos Set-Cookie por si nos lo enviase el servidor
				Object contenido_cookie = this
						.getResponseHeaderObject("Set-Cookie");

				String cookiePath = null;
				String cookieDomain = null;
				String cookieString = null;
				
				if (contenido_cookie != null) {

					// antes de añadir cualquier cookie, debo de limpiar la hash
					// de cookies
					// de la clase cookie manager para no acomular varias veces
					// la misma cookie
					// CookieManager.getInstance().clearSitePathCookies(_sHost +
					// ":" + _nPort, "yo");

					if (contenido_cookie instanceof String) {
						Cookie cookie = new Cookie((String) contenido_cookie);

						cookieDomain = _sHost + ":" + _nPort;
						cookiePath = cookie.getPath();

						CookieManager.getInstance().clearSitePathCookies(
							cookieDomain, cookiePath);

						// debemos de crear una nueva cookie con el contenido
						// devuelto
						// * TODO
//						System.out
//								.println("[SocketConnector.sendRequest] Leido Set-Cookie: "
//										+ contenido_cookie);
						
						// añadimos dicha cookie a la hash de cookies
						CookieManager.getInstance().add(cookie, cookieDomain,
							cookiePath);

//						System.out
//								.println("[SocketConnector.sendRequest] ToString:"
//										+ cookie.toString());

//						System.out
//								.println("[SocketConnector.sendRequest] Añadimos la cookie ("
//										+ _sHost
//										+ ":"
//										+ _nPort
//										+ "): "
//										+ cookie.toString());

					} else {
						// Es un vector
						// por cada elementos del vector, debemos de crear una
						// cookie
						Vector v = (Vector) contenido_cookie;
						Hashtable cleanedPaths = new Hashtable();

						for (int i = 0; i < v.size(); i++) {
//							System.out
//									.println("[SocketConnector.sendRequest] Leido Set-Cookie: "
//											+ (String) v.elementAt(i));

							// creamos una nueva cookie
							Cookie cookie = new Cookie((String) v.elementAt(i));

							// obtenemos la informacion de la cookie
							cookieDomain = _sHost + ":" + _nPort;
							cookiePath = cookie.getPath();

							// Compruebo si ya limpie el path para esta serie de
							// cookies
							// y si no lo limpio
							if (cleanedPaths.get(cookiePath) == null) {
								CookieManager.getInstance()
										.clearSitePathCookies(cookieDomain,
											cookiePath);
								cleanedPaths.put(cookiePath, cookiePath);
							}

							// añadimos dicha cookie a la hash de cookies
							CookieManager.getInstance().add(cookie,
								cookieDomain, cookiePath);

//							System.out
//									.println("[SocketConnector.sendRequest] ToString:"
//											+ cookie.toString());
//
//							System.out
//									.println("[SocketConnector.sendRequest] Añadimos la cookie "
//											+ i
//											+ " ("
//											+ _sHost
//											+ ":"
//											+ _nPort
//											+ "):" + cookie.toString());
						}// fin for
					}
				}

			} catch (Exception e) {
				nContentLength = -1;
			}

			// * Recoger de la cabecera información acerca de la validación,
			// * si se ha enviado
			String xValidation = this.getResponseHeader("X-Validation");

			if (xValidation != null && xValidation.trim().length() != 0) {
				// * Existe información sobre validación en la cabecera
				// * En función de esta información, lanzar una excepción
				if (xValidation.equals("login_required")) {
//					System.out.println("LANZANDO LOGINEXCEPTION");
					throw new LoginException(
							"Se ha producido un error al logearse el usuario");
				}
				if (xValidation.equals("no_required_roles"))
					throw new RolesException(
							"Se ha producido un error con los roles del usuario");

				if (xValidation.equals("error"))
					throw new ValidationException(
							"Se ha producido un error de validación");

				else
					// * Cadena no reconocida
					// * Se lanza también un error de validación
					throw new ValidationException(
							"Error desconocido en validación");
			}

//			System.out
//					.println(System.currentTimeMillis()
//							+ "[SocketConnector.sendRequest] A LEER EL CUERPO DE LA RESPUESTAAAAAAA. Content-length: "
//							+ nContentLength);
			_aBytesResponse = this.readBlock(is, nContentLength);
			

//			System.out.println(System.currentTimeMillis()
//					+ "[SocketConnector.sendRequest] Leido.toString: "
//					+ new String(_aBytesResponse));
			//// System.out.println(System.currentTimeMillis()+"[SocketConnector.sendRequest]
			// BytesLeidos: " + _aBytesResponse.length);

			//// System.out.println("PETICION COMPLETA\n" + request + body);
			disconnect();
			// s.close();

		} catch (TimeOutException e) {
			throw (e);
		} catch (LoginException e) {
			throw (e);
		} catch (RolesException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

//		System.out
//				.println("[SocketConnector.sendRequest] He sobrevivido al catch.");

		// El status se habrá parseado justo tras leer las cabeceras (y antes de
		// leer el cuerpo)

		return _nStatus;
	}

	private String readLine(InputStream r) throws IOException {
		StringBuffer sb = new StringBuffer("");
		int nCar = r.read();
		////System.out.println("[HttpConector.readLine] ncar: #"+nCar+"#");
		boolean bFin = false;
		while (nCar != -1 && !bFin) {
			////System.out.println("[HttpConector.readLine] nCar: "+nCar);
			char ch = (char) nCar;
			// Un salto de línea está formado por \r y por \n (retorno + salto)
			if (ch != '\n') {
				if (ch != '\r')
					sb.append(ch);
				nCar = r.read();
			} else
				bFin = true;
		}
		////System.out.println("[HttpConector.readLine] sb: #"+sb+"#");
		return sb.toString();
	}

	/**
	 * Used internally to parse the status line returned from the server.
	 */

	private void parseStatusCode() {
		StringTokenizer u = new StringTokenizer(_sStatusText, " ");
		u.nextToken();
		_nStatus = new Integer(u.nextToken()).intValue();
	}

	/**
	 * Used internally to parse the headers returned from the server into an
	 * internal table.
	 */

	private void parseHeaders() {
		StringTokenizer u = new StringTokenizer(_sResponseHeaders, "\n");
		while (u.hasMoreTokens()) {
			String header = u.nextToken();
			StringTokenizer v = new StringTokenizer(header, ":");

			String szName = v.nextToken().toLowerCase();
			// _vResponseHeaderNames.addElement(v.nextToken().toLowerCase());
			v.nextToken(" ");
			String szValue = v.nextToken("").trim();
			// _vResponseHeaderValues.addElement(v.nextToken("").trim());
			this.setResponseHeader(szName, szValue);
		}

	}

	/**
	 * Processes the HTTP response to adjust the request for HTTP authentication
	 */

	private void Authenticate() {
		String wwwauthen = "";
		String auth = "";

		if (_nStatus == 401) {
			wwwauthen = getResponseHeader("WWW-Authenticate");
			// Podemos encontrarnos que el servidor envia los dos
			// desafios. Siempre hay que elegir el más fuerte
			String temp = getResponseHeaderInverse("WWW-Authenticate");
			if (!wwwauthen.equals(temp)) {
				if (wwwauthen.indexOf("Digest") == -1) {
					wwwauthen = temp;
				}
			}
		}

		if (wwwauthen == "") {
			return;
		}

		if (_nStatus == 401 && (_sUser == null || _sUser.length() == 0)) {
			if (_bPassHidden && numberfailes < 2) {
				// Si tenemos alguno guardado
				setAuthentication(userLogin, passLogin);

			} else {
				/**
				 * Si no existe o tenemos un problema con los reintentos
				 */

				userLogin = null;
				passLogin = null;
				numberfailes = 0;

				// * TODO: en este punto, deberían de leerse el login y la pass
				// de la cookie
				// dialogPass();

			}

		} else if (_nStatus == 401 && (_sUser != null && _sUser.length() > 0)) {
			if (!(_bPassHidden && numberfailes < 2)) {
				// Le damos alguna oportunidad mÁ
				// * TODO: en este punto, deberían de leerse el login y la pass
				// de la cookie
				// dialogPass();
			}
		}

		if (_nStatus == 401)
			removeRequestHeader("Authorization");

		StringTokenizer u = new StringTokenizer(wwwauthen, " ");
		String authtype = u.nextToken();

		if (authtype.compareTo("Basic") == 0) {
			BasicAuthen ba = new BasicAuthen();
			auth = ba.createHeader(_sUser, _sPass);
		} else if (authtype.compareTo("Digest") == 0) {
			Random rand = new Random(new Date().getTime());
			StringTokenizer v = new StringTokenizer(_sFile, "?");
			DigestAuthen da = new DigestAuthen(_sUser, _sPass, _sMethod, v
					.nextToken());
			da.parseHeader(wwwauthen);

			String d = "";

			for (int y = 0; y < 16; y++) {
				String q = Integer.toHexString(rand.nextInt());
				if (q.length() == 1)
					d += "0";
				d += q;
			}

			da.setCNonce(d);
			auth = da.createHeader();
		}

		if (_nStatus == 401) {
			this.setRequestHeader("Authorization", auth);
			numberfailes++;
		}

		return;
	}

	/*
	 * private void dialogPass() {
	 * 
	 * DialogLogin ll = new DialogLogin(); String userpass =
	 * ll.getPasswordAuthentication(_frame, "goose"); StringTokenizer v = new
	 * StringTokenizer(userpass, "#$#"); String u = v.nextToken(); String p =
	 * v.nextToken(); setAuthentication(u, p);
	 * 
	 * System.out.println("[SocketConnector.dialogPass] _progress: " +
	 * _progress); if (_progress != null) _progress.toFront(); }
	 */

	private boolean connectSSL() throws LoginException {
		
		//// System.out.println("context");

		final SSLContext context = new SSLContext();
		KeyAndCert kac = null;
	
		//// System.out.println("empty contex");
		context.setSendEmptyFragment(false);
		SSLTransport socket = null;
		

		try {
//			System.out.println("[SocketConnector.connectSSL]  SSLTransport="
//					+ _sHost + ":" + _nPort);

			RunnableException rr = new RunnableException() {
				public Object run() throws Exception {

					return new SSLTransport(_sHost, _nPort, context);
				}

			};

			Runna r = new Runna(rr, Constantes.CONNECTION_TIMEOUT);

			socket = (SSLTransport) r.getResult();

			// socket = new SSLTransport(_sHost, _nPort, context);

			// r.getSocket();
			// context.setChainVerifier(null);

			//// System.out.println("SSLTransportXX");
			// we manually start the handshake and print some socket debugging
			// info
						
			socket.setDebugStream(new java.io.OutputStream(){
				
				   private StringBuffer sb = new StringBuffer();
				   
	               public void write(int b) throws IOException {

					// escribimos el caracter actual por la salida de error
					System.err.write(b);

					// ir acumulando hasta que llegue un '\n'
					if ((char) b == '\n') {
						//System.err.println(sb.toString());
						sb = new StringBuffer();
					} else {
						
						//añadimos al Buffer el caracter actual
						sb.append((char)b);

						// comprobar lo acumulado con "No client certificate
						// available, sending empty certificate message"
						String cadena = sb.toString();
						if (cadena.indexOf("No client certificate available, sending empty certificate message...")!=-1){
							setNoClientCertificate(true);
						}
						
					}

				}
	        });
			
			// socket.setAutoHandshake(true);
			
			try{
				socket.startHandshake();
			} catch (SSLException e) {
            	log.error("Error durante el handShake del protocolo SSL.",e);            
            }
			
			//// System.out.println("SSLTransport1");
			//System.out.println("[SocketConnector.connectSSL] Fin del meneo de manos");

		} catch (Exception e) {
									
			// Este bloque es para "DESASUSTARNOS" cuando caduque un
			// certificado.
			if (socket != null) {
										
				//comprobamos si se ha producido un error de handshake por certificado no valido del cliente
				if(this.noClientCertificate==true)
					throw new LoginException("Certificado de cliente no indicado o inválido.");
				
				//Intentemos extraer algo de información extra...
				X509Certificate certs[] = socket.getPeerCertificateChain();
							
				if (certs != null) {
					
					log.error("Número de certificados: "+ certs.length);
					
					for (int i = 0; i < certs.length; i++) {
						log.error("CERTIFICADO NÚMERO: "	+ i);
						X509Certificate cert = certs[i];
						log.error("ISSUER DN: " + cert.getIssuerDN());
						log.error("NOT BEFORE: " + cert.getNotBefore());
						log.error("NOT AFTER: " + cert.getNotAfter());
					}
				}
					
			} else {
				log.error("SecureSocket es nullllll!!!!",e);
			}

			//// System.out.println("SSLTransport3");
			log.error("Error creando conexión SSL.",e);
			//e.printStackTrace(System.out);
			_secureSocket = null;
			

			return false;

		}
		
///		 now the connection is secure, let's display information about the
//		 server
//		 System.out.println("Active protocol: " +
//		 socket.getActiveProtocolVersion());
//		 System.out.println("Active cipher suite: " +
//		 socket.getActiveCipherSuite());
//		 System.out.println("\nServer certificate chain:");
//		 X509Certificate[] chain = socket.getPeerCertificateChain();
//		 if( chain == null ) {
//		 System.out.println("Anonymous server.");
//		 } else {
//		 for (int i=0; i<chain.length; i++) {
//		 System.out.println("Certificate " + i + ": " +
//		 chain[i].getSubjectDN());
//		 }
//		 }
		
//		  System.out.println("Active protocol: " + socket.getActiveProtocolVersion());
//		  System.out.println("Active cipher suite: " + socket.getActiveCipherSuite());
//		  System.out.println("\nServer certificate chain:");
		
		     X509Certificate[] chain = socket.getPeerCertificateChain();
		     if( chain == null ) {
		    	 log.debug("Anonymous server.");
		     } else {
		    	 
		       for (int i=0; i<chain.length; i++) {
		         log.debug("certificate " + i + ": " + chain[i].getSubjectDN());
		         log.debug("\tissuer dn: "+chain[i].getIssuerDN());
		         log.debug("\tnot before: "+chain[i].getNotBefore());
		         log.debug("\tnot after: "+chain[i].getNotAfter());
		       }
		     }
		     
		_secureSocket = socket;
		return true;
	}

	private boolean conectNormalSocket() {
		Socket conn = null;
		boolean resultadoOK = true;
//		System.out
//				.println("[SocketConnector.conectNormalSocket] dentro de conectNormalSocket");
		try {

			RunnableException rr = new RunnableException() {
				public Object run() throws Exception {
					// Thread.sleep (40000);
					return new Socket(_sHost, _nPort);

				}

			};
			Runna r = new Runna(rr, Constantes.CONNECTION_TIMEOUT);

			conn = (Socket) r.getResult();
			conn.setSoTimeout(Constantes.CONNECTION_TIMEOUT * 1000);
		} catch (UnknownHostException e) {
			resultadoOK = false;
			System.err.println(e.toString());
		} catch (NoRouteToHostException rte) {
			resultadoOK = false;
			System.err.println(rte.toString());
		} catch (IOException e) {
			resultadoOK = false;
			System.err.println(e.toString());
		} catch (Exception e) {
			resultadoOK = false;

			System.err.println(e.toString());
		}

//		System.out
//				.println("[SocketConnector.conectNormalSocket] fin dentro de conectNormalSocket. conn: "
//						+ conn);
		_normalSocket = conn;
		
		return resultadoOK;
	}

	private boolean connect() throws LoginException {
		// dependiendo del tipo de conexion, invocamos a un metodo o a otro
		if (_bIsSecure) {
			return connectSSL();
		} else {
			return conectNormalSocket();
		}
	}

	private boolean closeNormalSocket() {
		try {
			_normalSocket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
			return false;
		}
	}

	private boolean closeSecureSocket() {
		try {
			_secureSocket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
			return false;
		}
	}

	private boolean disconnect() {
		if (_bIsSecure) {
			return closeSecureSocket();
		} else {
			return closeNormalSocket();
		}

	}

	// private InputStreamReader getReader(InputStream is)
	// {
	// //Sea lo que sea, devuelvo un InputStreamReader
	// return new InputStreamReader(is);
	// }

	private InputStream inputPipe() {
		try {
			//stopwatch.start(ClientWatchTimeConstants.SC_GET_INPUT_PIPE, "SC Obteniendo stream de lectura de socket");
			InputStream ip = _bIsSecure ? _secureSocket.getInputStream() : _normalSocket
					.getInputStream();
			//stopwatch.stop(ClientWatchTimeConstants.SC_GET_INPUT_PIPE);
			return ip;
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
			return null;
		}

	}

	private OutputStream outputPipe() throws IOException {
		//stopwatch.start(ClientWatchTimeConstants.SC_GET_OUTPUT_PIPE, "SC Obteniendo stream de escritura en el socket");
		OutputStream op = (_bIsSecure) ? _secureSocket.getOutputStream() : _normalSocket
				.getOutputStream();
		//stopwatch.stop(ClientWatchTimeConstants.SC_GET_OUTPUT_PIPE);
		
		return op;

		// if (_bIsSecure) {
		// try {
		// return new
		// PrintWriter(Utils.getASCIIWriter(_secureSocket.getOutputStream()));
		// } catch (IOException e) {
		// e.printStackTrace(); //To change body of catch statement use Options
		// | File Templates.
		// return null;
		// }
		// } else {
		// try {
		// return new PrintWriter(_normalSocket.getOutputStream());
		// } catch (IOException e) {
		// e.printStackTrace(); //To change body of catch statement use Options
		// | File Templates.
		// return null;
		// }
		// }
	}

	/**
	 * Método setter del campo noClientCertificate
	 * @param value
	 */
	private void setNoClientCertificate(boolean value){
		this.noClientCertificate = value;
	}
		
}
