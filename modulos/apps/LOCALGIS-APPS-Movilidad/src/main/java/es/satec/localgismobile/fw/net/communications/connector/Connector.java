/**
 * Connector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications.connector;

//import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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

/**
 *
 * User: dbenito; XSDDate: 10-mar-2004 Time: 17:05:19
 * To change this template use Options | File Templates.
 *
 * TAGS CVS
 * @author  SATEC
 * @version $Revision: 1.1 $
 *
 * Autor:$Author: satec $
 * Fecha Ultima Modificacion:$XSDDate: 2005/09/12 16:21:14 $
 * $Name:  $ ; $RCSfile: Connector.java,v $ ; $Revision: 1.1 $ ; $Locker:  $
 * $State: Exp $
 */
public abstract class Connector
{
	//Para que el CVS guarde la revisión y así decompilar la clase en producción.
	private String Version = "$Revision: 1.1 $";

	protected String _sHost;
	protected int _nPort = -1;
	protected String _sFile;
	protected String _sMethod;

	protected byte[] _aBytesResponse;//Bytes de la respuesta obtenida.

	protected int _nStatus;
    protected boolean _bIsHttps;
    protected String _szContentType;

	//Hash (String -> String) con las cabeceras a enviar con la petición.
	private Hashtable _htRequestHeaders;
	//Hash con las cabeceras recibidas. OJO: String -> String|Vector, ya que pueden recibirse
	//varias cabeceras iguales  (caso del Autenticate), y siempre debo permitir tener las dos
	private Hashtable _htResponseHeaders;

    //La idea es que tras hacer un SUBMISSION mediante un file-PUT no hay que proceder a la
	//secuencia ni del autodownload ni del autosend.
	private static Connector _lastConnector;
	
	/*
	public static boolean wasLastConnectoAFile(){
		return _lastConnector instanceof FilePutConnector;
	}
	*/


    /**MÉTODO FACTORÍA para obtener el conector apropiado según los parametros*/
	public static Connector getConnector(String szUrl, String szMethod, boolean bUseSocket,/* Frame fr, */ String szContentType, Hashtable headers) throws MalformedURLException
	{

		//inicializamos todas las vbles locales a usar
		//pasamos a mayusculas el method recibido
		if(szMethod!=null)	szMethod = szMethod.toUpperCase();
		boolean bIsHttps=false;
		boolean bStWthHttp=false;
		boolean bIsFile=false;
		
		//tramos la URL recibida
		if(szUrl != null)
		{
//			System.out.println("[Connector.getConnector] szUrl: "+szUrl);
//			
			//obtenemos el protocolo que irá dentro de los primeros 5 caracteres
			String szProto =szUrl.substring(0, 5);
//			System.out.println("[Connector.getConnector] szProto: "+szProto);
			
			//según el protocolo, asignamos T o F a las vbles oportunas
			bStWthHttp = szProto.toLowerCase().startsWith("http");
			bIsHttps = bStWthHttp && szProto.toLowerCase().equals("https");
			bIsFile = !bStWthHttp && szProto.toLowerCase().startsWith("file");
			
//			System.out.println("[Connector.getConnector] Soy https: "+bIsHttps);
		}

		//creamos un nuevo objeto Connector
		Connector conn = null;
		
		//si tenemos HTTP, HTTPs o se indica como parametro, creamos un nuevo SocketConnector
		//sino, si se indico FILE y el metodo es POST (es decir, NO es GET), creamos un FilePutConnector
		//sino, como caso ultimo caso, creamos un URLConnector
		if(bIsHttps || bUseSocket || bStWthHttp){
//			System.out.println("[Connector.getConnector] Creamos un SocketConnector");
			conn = new SocketConnector(szUrl, szMethod, bIsHttps, szContentType);
		}
		else if(bIsFile && szMethod.equalsIgnoreCase("put")){
//			System.out.println("[Connector.getConnector] Creamos un FilePutConnector");
			//conn = new FilePutConnector(szUrl, szMethod, szContentType);
		}
		else{
//			System.out.println("[Connector.getConnector] Creamos un URLConnector");
			conn = new URLConnector(szUrl, szMethod, szContentType);
		}

        // añado los headers
		//añadimos a RequestHeaders los headers indicados como parametro para los envios
       if ( headers != null) {
            Enumeration keys = headers.keys();
            while(keys.hasMoreElements()) {
                String header = (String) keys.nextElement();
                String value = (String) headers.get(header);
                conn.setRequestHeader(header, value);
            }
        }
//        System.out.println("[Connector.getConnector] conn:"+conn);
        
        //actualizamos el campo _lastConnector
		_lastConnector = conn;
		return  conn;
	}


	/**
	 * Este constructor NO INICIALIZA nada del objeto. Útil para sobrecargas que no necesitan nada de aquí
	 */
	public Connector()
	{
		this._htRequestHeaders = new Hashtable();
		this._htResponseHeaders = new Hashtable();
		this._nStatus = -1;

//		System.out.println("[Connector.Connector] NO HAGO NADA, A DREDE");
	}


	//constructor al que invocaran las clases hijas de Connector para inicializar ciertos campos que les sean necesarios
	public Connector(String szUrl, String _szMethod, boolean bIsHttps, String szContentType) throws MalformedURLException
	{
		this();

		_bIsHttps = bIsHttps;
		_szContentType = szContentType;

		if(bIsHttps)
		{	szUrl = "http" + szUrl.substring(5);
		}

		URL url = null;
//		try
//		{
			url = new URL(szUrl);
			this._nPort = url.getPort();
			this._sFile = url.getFile();
			this._sHost = url.getHost();
//		} catch (MalformedURLException e)
//		{
//			e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//		}

		this._sMethod = _szMethod;

        if(szContentType!=null && szContentType.equals("")==false)
			this.setRequestHeader("Content-type", szContentType);

//		System.out.println("[Connector.CONSTRUCTOR] host: "+ _sHost + "; port: " + _nPort + "; file: " + _sFile	+ "; method" + _sMethod+"; isHttps: "+bIsHttps);
	}

	//el metodo send se define abstract ya que debe ser redefinido en las clases hijas
	public abstract void send(byte[] body) throws IOException, TimeOutException, LoginException, RolesException, ValidationException, NoConnectionException;


	/**
	 * Asigna una Header de la petición.
	 *
	 * @param header Nombre de la cabecera
	 * @param value Valor de la cabecera
	 * @return El valor antiguo (si es que estaba fijada)
	 */
	public String setRequestHeader(String header, String value) {
		//Estas no pueden ser fijadas. Son Automáticas
		if (header.equalsIgnoreCase("Content-Length") ||
//			header.equalsIgnoreCase("Authorization") ||
			header.equalsIgnoreCase("Host") ||
			header.equalsIgnoreCase("Connection") ||
			header.equalsIgnoreCase("Content-MD5"))
		{ return null;
		}

		//añadimos una nueva cabecera a enviar en la hash de cabeceras a enviar
		Object o = _htRequestHeaders.put(header, value);

		//si todo fue bien, devolvemos el valor anterior para la cabecera introducida
//		System.out.println("[Connector.setRequestHeader] insertado: "+header+"; _htRequestHeaders: "+_htRequestHeaders);
		return (o!=null)? (String) o : null;
	}

	public void setResponseHeader(String header, String value)
	{
		String szHeaderLC = header.toLowerCase();

		//añadimos una nueva cabecera recibida en la hash de cabeceras a recibidas
		//y recuperamos la anterior cabecera almacenada en dicha tabla hash
		Object o =  _htResponseHeaders.put(szHeaderLC, value);
		if(o != null)
		{
			Vector v;
			if(o instanceof String){
				v = new Vector(2);
				v.addElement(o);
			} else { //He recuperado un VECTOR
				v = (Vector) o;
			}

			v.addElement(value);
			_htResponseHeaders.put(szHeaderLC, v);
		}

//		System.out.println("[Connector.setResponseHeader] insertado: "+header+"; _htResponseHeaders: "+_htResponseHeaders);
	}

	public void clearResponseHeaders()
	{
		_htResponseHeaders.clear();
	}

	/**Devuelve el Código devuelto por una petición HTTP*/
	public int getStatusCode()
	{	return _nStatus;
	}

	public boolean isSecure()
	{	return this._bIsHttps;
	}
	/**
	 * Devuelve la respuesta recibida parseada como una string.
	 * @return Un string con la respuesta recibida
	 */
	public String getResponseText()
	{
		String szRet = null;
		if(_aBytesResponse!=null)
			szRet = new String(_aBytesResponse);

//		System.out.println("[Connector.getResponseText] szRet: "+szRet);
		return szRet;
	}

	/**
	 * Devuelve los bytes de la respuesta recibida.
	 * @return Los bytes de la respuesta.
	 */
	public byte[] getResponseBytes()
	{	return _aBytesResponse;
	}

	protected void removeRequestHeader(String header) {
		_htRequestHeaders.remove(header);
	}

	
	//NO DEVUELVE NULL.
	
	private String getResponseHeader(String szHeader, boolean bGetFirst)
	{
		String szRet = "";
		Object o = _htResponseHeaders.get(szHeader.toLowerCase());
		if(o != null)
		{
			if(o instanceof String){
				szRet = (String) o;
//				System.out.println("NOS LLEGA UNA HEADER SIMPLE:"+szRet);
			}
			else //Es un VECTOR
			{
				Vector v = (Vector) o;
				szRet = (String) (bGetFirst? v.firstElement() : v.lastElement()) ;
//				System.out.println("NOS LLEGA UNA HEADER MULTIPLE: "+v.firstElement()+" - "+v.lastElement());
			}
		}

		return szRet;
	}
		
	//metodo que nos devuelve todas los valores de la headers... es necesario
	//para los casos en los que tenga varias cookies para un mismo servidor
	public Object getResponseHeaderObject(String szHeader)
	{
		
		Object o = _htResponseHeaders.get(szHeader.toLowerCase());
		
		if(o != null)
		{
			if(o instanceof String){
				String szRet = (String) o;
//				System.out.println("NOS LLEGA UNA HEADER SIMPLE:"+szRet);
			}
			else //Es un VECTOR
			{
				Vector v = (Vector) o;
				//szRet = (String) (bGetFirst? v.firstElement() : v.lastElement()) ;
//				System.out.println("NOS LLEGA UNA HEADER MULTIPLE: "+v.firstElement()+" - "+v.lastElement());
			}
		}

		return o;
	}
	
	public String getResponseHeaderInverse(String szHeader)
	{	return this.getResponseHeader(szHeader, false);
	}

	public String getResponseHeader(String szHeader)
	{	return this.getResponseHeader(szHeader, true);
	}

	//MÉTODOS AUXILIARES.
	/**
	 * Lee el InputStream hasta el final, aunque no lo cierra -> TENDRÁ QUE HACERLO EL QUE LO USE.
	 * @param is InputStream del que se lee -> NO SE CERRARÁ.
	 * @param nContentLength <0: PASANDO ; >0 Tamaño a leer desde el is. Si el is se acaba, pues no se leerá más.
	 * @throws TimeOutException Si salta el TimeOut en la primera lectura.
	 * @throws Exception Si se produce algún error extraño
	 */
	protected byte[] readBlock(final InputStream is, int nContentLength) throws Exception, TimeOutException
	{
		//Ahora se mete un timeOut a la primera lectura.
		
		//Se crea una RunnableException que será 'ejecutada' por un hilo 
		//creado en la clase Runna
		RunnableException re = new RunnableException(){
			public Object run() throws Exception
			{
				//este método Run lo que hará es leer un bloque de 512 bytes
//				System.out.println(System.currentTimeMillis() + "[Connector.RunnableException.run()] Leyendo el primer Bloque");
				byte [] aBData = new byte [512];
				int count = is.read(aBData, 0, 512);
				ByteArrayOutputStream baOS = new ByteArrayOutputStream();
//				System.out.println(System.currentTimeMillis() + "[Connector.RunnableException.run()] Bytes leidos: "+count);
				if(count >0)
					baOS.write(aBData, 0, count);
				return baOS;
			}
		};
		
		//cramos un objeto Runna que dentro ejecutara un hilo independiente que a su vez ejecutará
		//el método 'run' indicado en el constructor del la vble re de tipo RunnableException
		Runna r = new Runna(re, Constantes.CONNECTION_TIMEOUT);
		
		ByteArrayOutputStream baOS = (ByteArrayOutputStream) r.getResult();

		//Una vez que se ha conseguido leer el primer bloque, se intenta con los siguientes.
		if(nContentLength==-1 || baOS.size()<nContentLength){
			byte [] aBData = new byte [512];
			int count = 0;
			try{
				//leemos un nuevo bloque de 512 bytes del is indicado...
				while ((nContentLength != baOS.size())
					&& ((count = is.read(aBData, 0, 512)) != -1))
				{
//					System.out.println(System.currentTimeMillis() + "[Connector.readBlock] leido: "+count);
					baOS.write(aBData, 0, count);
				}
//				System.out.println(System.currentTimeMillis() +"[Connector.readBlock] Última lectura: "+count);
			}catch(EOFException e){
//				System.out.println("[Connector.readBlock] Se acabaron los datos: "+e.toString());
			}
		}
		baOS.flush();
		baOS.close();
//		System.out.println("[Connector.readBlock] CERRADO baOS");
        return baOS.toByteArray();
	}

	protected void putRequestHeaders(URLConnection urlCnx)
	{
		Enumeration en = _htRequestHeaders.keys();
		
		while(en.hasMoreElements())
		{
			String header = (String) en.nextElement();
			String value = (String) _htRequestHeaders.get(header);

			urlCnx.setRequestProperty(header, value);
			
		}
		
		//además de estas cabeceras, debe de comprobar si existe en memoria una cookie para 
		//el host y el puerto indicado en la url de la peticion
		// * TODO: Debemos recuperar la cookie (si existe) y añadirla a RequestHeaders
		
		/*
		Vector v = CookieManager.getInstance().get(_sHost+":"+_nPort);
		
		if(v!=null){
			
			for(int i=0;i<v.size();i++){
				
				Cookie cookie = (Cookie)v.elementAt(i);
				System.out.println("[Connector.putRequestHeaders] Cookie != NULL");
				System.out.println("[Connector.putRequestHeaders(urlCnx)] Escribimos Cookie("+_sHost+_nPort+"): "+cookie.getValue());
				urlCnx.setRequestProperty("Cookie", cookie.toString());
			}						
		}
		
		/*
		Cookie cookie = CookieManager.getInstance().get(_sHost+":"+_nPort);
		
		if(cookie!=null){
			System.out.println("[Connector.putRequestHeaders] Cookie != NULL");
			System.out.println("[Connector.putRequestHeaders(urlCnx)] Escribimos Cookie("+_sHost+_nPort+"): "+cookie.getValue());
			urlCnx.setRequestProperty("Cookie", cookie.toString());			
		}
		*/
		
	}

	protected String putRequestHeaders()
	{
		String request = "";
		Enumeration en = _htRequestHeaders.keys();
		while(en.hasMoreElements())
		{
			String header = (String) en.nextElement();
			String value = (String) _htRequestHeaders.get(header);

			request += header + ": " + value + "\r\n";
		}
		
		//además de estas cabeceras, debe de comprobar si existe en memoria una cookie para 
		//el host y el puerto indicado en la url de la peticion
		// * TODO: Debemos recuperar la cookie (si existe) y añadirla a RequestHeaders
		
		String domain = _sHost + ":" + _nPort;
		String uri = domain + _sFile;
		Vector v = CookieManager.getInstance().get(domain, uri);
		////System.out.println("+++++++++++++++*!!!!!!!!!!!!!nPort!!!?!!!!++++++: " + v.size());
		
		if(v!=null){
		
		//String myCookies = "userId=igbrown; sessionId=SID77689211949; isAuthenticated=true";
		request += "Cookie: ";
		
			for(int i=0;i<v.size();i++){
				
				Cookie cookie = (Cookie)v.elementAt(i);
				
//				System.out.println("[Connector.putRequestHeaders] ToString:"+cookie.toString());
//				System.out.println("[Connector.putRequestHeaders] Cookie != NULL");
//				
				if(i!=0)
					request += "; ";
				
				request += cookie.toString();
			}
			
			request += "\r\n";
		}
		
		/*
		Cookie cookie = CookieManager.getInstance().get(_sHost+":"+_nPort);
		
		if(cookie!=null){

			System.out.println("[Connector.putRequestHeaders] ToString:"+cookie.toString());
			
			System.out.println("[Connector.putRequestHeaders] Cookie != NULL");
			request += "Cookie" + ": " + cookie.toString() + "\r\n";
		}
		*/		
		
//        System.out.println("[Connector.putRequestHeaders] retorno: "+request);
		return request;
	}
}

