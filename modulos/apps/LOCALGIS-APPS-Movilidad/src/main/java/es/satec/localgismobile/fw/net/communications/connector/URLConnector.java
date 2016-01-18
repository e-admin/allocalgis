/**
 * URLConnector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.net.communications.exceptions.TimeOutException;

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
//////////////CLASE URL CONNECTOR: tiene la lógica sencilla de conexion.
////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

/** Esta trata FILE, FTP. */
class URLConnector extends Connector {
	
	private URL _url;

	//constructor que nos crea un objeto de tipo URLConnector
	public URLConnector(String szUrl, String szMethod, String szContentType)
			throws MalformedURLException {
		super(szUrl, szMethod, false, szContentType);
		//obtenemos la url que queremos solicitar
		_url = new URL(szUrl);
	}

	//metodo para enviar un array de bytes a traves de la URLConnection
	public void send(byte[] body) throws IOException, TimeOutException,
			NoConnectionException {
		
		OutputStream os = null;
		InputStream is = null;
		
		try {
			
			//abrimos una conexion que en esta caso sera de tipo URLConnection
			final URLConnection urlCnx = _url.openConnection();

			//comprobamos si la conexion es HTTP
			boolean bIsHttp = _url.getProtocol().toLowerCase().equals("http");
			System.out.println("[URLConnector.send] Soy HTTP: " + bIsHttp);

			//añadimos a la conexion las cabeceras que queremos enviar
			this.putRequestHeaders(urlCnx);
			
			urlCnx.setDoInput(true);
			urlCnx.setUseCaches(false);
			
			//indicamos que el Method sea HTTP
			if (bIsHttp) {
				System.out.println("[URLConnector.send] Machaco el Method: "
						+ _sMethod);
				((HttpURLConnection) urlCnx).setRequestMethod(this._sMethod);
			}

			// Enviar el body
			if (body != null) {
				urlCnx.setDoOutput(true);
				PrintWriter pw = new PrintWriter(urlCnx.getOutputStream());
				System.out.println("[URLConnector.send] PrintWriter creado");
				pw.print((new String(body)) + "\n");
				System.out.println("[URLConnector.send] body Enviado");
				pw.flush();
				pw.close();
				System.out.println("[URLConnector.send] PrintWriter cerrado");
			}

			// Recibir la respuesta desde el servidor
			is = urlCnx.getInputStream();
			System.out.println("[URLConnector.send] InputStream pillado");
			
			_aBytesResponse = this.readBlock(is, -1); // Mando un -1 para pasar del Content-Length y que lea TOOO
			
			System.out.println("[URLConnector.send] _aBytesResponse: "+ _aBytesResponse);
			
			//creamos el is xq no se hace dentro del readBlock (MUY IMPORTANTE)
			is.close();
			is = null;

			System.out.println("[URLConnector.send] respuesta leída");
			
			if (bIsHttp)
				_nStatus = ((HttpURLConnection) urlCnx).getResponseCode();

			this.clearResponseHeaders();
			this.setResponseHeader("Content-type", urlCnx.getContentType());
			
			// * TODO: Invocar a la API que gestiona las cookies
			//OK, now we are ready to get the cookies out of the URLConnection
			//Debemos parsear las cabeceras de tipo Set-Cookie ya que podemos
			//recibir varias cookies de un mismo servidor
			/*
			String headerName=null;
			for (int i=1; (headerName = urlCnx.getHeaderFieldKey(i)) != null; i++) {
				
				//comprobamos si nos llego un Set-Cookie
			    if (headerName.equalsIgnoreCase("Set-Cookie")) {
				
				StringTokenizer st = new StringTokenizer(urlCnx.getHeaderField(i), ";");
				
				// the specification dictates that the first name/value pair
				// in the string is the cookie name and value, so let's handle
				// them as a special case: 
				
				if (st.hasMoreTokens()) {
				    String token  = st.nextToken();
				    String name = token.substring(0, token.indexOf('='));
				    String value = token.substring(token.indexOf('=') + 1, token.length());
				    Cookie cookie = new Cookie();
				    CookieManager.getInstance().add(cookie, dominio);
				}
		    
				while (st.hasMoreTokens()) {
				    String token  = st.nextToken();
				    cookie.put(token.substring(0, token.indexOf('=')).toLowerCase(),
				    token.substring(token.indexOf('=') + 1, token.length()));
				    Cookie cookie = new Cookie();
				    CookieManager.getInstance().add(cookie, dominio);
				}
			    }
			}
			*/
			
			/*
			String contenido_cookie = urlCnx.getHeaderField("Set-Cookie");
						
			if(contenido_cookie != null){
				//debemos de crear una nueva cookie con el contenido devuelto
				// * TODO
				Cookie cookie = new Cookie(contenido_cookie);
				
				//añadimos dicha cookie a la hash de cookies
				CookieManager.getInstance().add(cookie,_sHost+":"+_nPort);
				
				System.out.println("[URLConnector.send] Añadimos la cookie ("+_sHost+_nPort+"): "+ contenido_cookie);
			}
			*/
						
			//en el futuro, puede ser necesario añadir la header Set-Cookie a la hash responseHeaders
			// * TODO 

		} catch (Exception e) {
			System.out.println("[URLConnector.send] e: " + e.toString());
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
			try {
				if (os != null)
					os.close();
			} catch (Exception e2) {
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e2) {
			}

			throw (new NoConnectionException());
		}
	}
}
