/**
 * NoConnectionException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications.exceptions;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;

public class NoConnectionException extends Exception {
	
	private static Logger log = Global.getLoggerFor(NoConnectionException.class);
	
	private int httpCode;
	
	public NoConnectionException(){		
		super("ERROR de CONEXIÓN.");
		log.error("ERROR de CONEXIÓN.");
		//se pone -1 por indicar algún número, pero no tiene 
		//ningún valor semántico sobre el error de conexión
		this.httpCode = -1;
	}
	
	public NoConnectionException(int code){
		super("ERROR de CONEXIÓN. HTTP Code == " + code);
		log.error("ERROR de CONEXIÓN. HTTP Code == " + code);
		this.httpCode = code;
	}
	
	public NoConnectionException(String mensaje){
		super("ERROR de CONEXIÓN: "+mensaje);
		log.error("ERROR de CONEXIÓN: "+mensaje);
		//se pone -1 por indicar algún número, pero no tiene 
		//ningún valor semántico sobre el error de conexión
		this.httpCode = -1;
	}

	public int getHttpCode() {
		return httpCode;
	}

}
