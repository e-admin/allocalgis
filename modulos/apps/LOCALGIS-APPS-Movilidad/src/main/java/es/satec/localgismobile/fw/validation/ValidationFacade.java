/**
 * ValidationFacade.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.validation;

//import idepa.fw.certification.DigitalCertificate;
import es.satec.localgismobile.fw.validation.exceptions.LoginCancelException;
import es.satec.localgismobile.fw.validation.exceptions.RolesException;
import es.satec.localgismobile.fw.validation.exceptions.ValidationException;
import es.satec.localgismobile.fw.validation.implementations.Validation;

/**
 * Agrupa la funcionalidad de validación contra servidores remotos.
 * <p>
 * Permite la validación offline cuando no existe conexión con el servidor
 * remoto.
 *
 */
public class ValidationFacade {

	/**
	 * Realiza el login contra el servidor indicado devolviendo si el login
	 * fue correcto o no.
	 * 
	 * @param url Url del servidor remoto contra el que realizar la validación
	 * @param user Nombre de usuario con el que realizar el login
	 * @param password Clave con la que realizar el login
	 * @return Cierto si la validación fue correcta, falso en caso contrario
	 * @throws RolesException Si el usuario para el que se realiza la validación
	 * no tiene los roles suficientes
	 * @throws ValidationException Si se produce un error durante el proceso
	 * de validación
	 */
	public boolean valida(String url,String user, String password) throws ValidationException, RolesException, LoginCancelException {
		Validation v=new Validation();
		return v.valida(url,user, password);		
	}
	
	/**
	 * Realiza el login contra el servidor indicado devolviendo si el login
	 * fue correcto o no.
	 * 
	 * @param protocol Protocolo de acceso al servidor (http o https)
	 * @param ip Dirección del servidor (ip o nombre de dominio)
	 * @param port Puerto del servidor
	 * @param uri Path donde se encuentra la aplicación servidora en la que
	 * realizar el logueo
	 * @param user Nombre del usuario con el que realizar el login
	 * @param password Clave con la que realizar el login
	 * @return Cierto si la validación fue correcta, falso en caso contrario
	 * @throws RolesException Si el usuario para el que se realiza la validación
	 * no tiene los roles suficientes
	 * @throws ValidationException Si se produce un error durante el proceso
	 * de validación
	 */
	public boolean valida(String protocol, String ip, int port, String uri,
			String user, String password) throws RolesException, ValidationException, LoginCancelException {
		
		String url = protocol + "://" + ip + ":" + port + uri;
		Validation v=new Validation();		
		
		return v.valida(url,user, password);		
	}

	/**
	 * Realiza la validación de un usuario haciendo uso de un certificado
	 * digital en lugar de un usuario y clave
	 * 
	 * @param url Url del servidor remoto contra el que realizar la validación
	 * @param certificate Certificado digital a utilizar para realizar la
	 * validación 
	 * @return Cierto si la validación fue correcta, falso en caso contrario
	 * @throws RolesException Si el usuario para el que se realiza la validación
	 * no tiene los roles suficientes
	 * @throws ValidationException Si se produce un error durante el proceso
	 * de validación
	 */
	/*public boolean valida(String url,DigitalCertificate certificate) throws RolesException,ValidationException{
		CertificateValidation v= new CertificateValidation();
		return v.valida(url,certificate);		
	}*/
	
	/**
	 * Método que se debe de emplear para que un usuario pueda validarse contra la
	 * dirección que indique indicando un certificado digital
	 * 
	 * @param protocol Protocolo de acceso al servidor (http o https)
	 * @param ip Dirección del servidor (ip o nombre de dominio)
	 * @param port Puerto del servidor
	 * @param uri Path donde se encuentra la aplicación servidora en la que
	 * realizar el logueo
	 * @param certificate Certificado digital a utilizar para realizar la
	 * validación 
	 * @return Cierto si la validación fue correcta, falso en caso contrario
	 * @throws RolesException Si el usuario para el que se realiza la validación
	 * no tiene los roles suficientes
	 * @throws ValidationException Si se produce un error durante el proceso
	 * de validación
	 */
	/*public boolean valida(String protocol, String ip, int port, String uri, DigitalCertificate certificate) throws RolesException, ValidationException{
		
		String url = protocol + "://" + ip + ":" + port + uri;	
		CertificateValidation v = new CertificateValidation();
		
		return v.valida(url,certificate);
	}*/
	
	/**
	 * Cierra la sesión en el servidor remoto para el usuario logueado en
	 * la aplicación
	 * @param address Ip o nombre de dominio del servidor
	 * @param port Puerto del servidor
	 * @param baseUri Path donde se encuentra la aplicación servidora en la que
	 * cerrar la sesión
	 */
	public void logout(String address, int port, String baseUri){
		Validation validationManager = new Validation();
		
		validationManager.logout(address, port, baseUri);
	}
}
