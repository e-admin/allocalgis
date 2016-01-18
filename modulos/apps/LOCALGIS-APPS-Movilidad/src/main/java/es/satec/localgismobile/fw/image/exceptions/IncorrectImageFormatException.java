/**
 * IncorrectImageFormatException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.exceptions;

/**
 * Excepción que indica que la imagen que se está procesando
 * no tiene el formato esperado
 * @author jpolo
 *
 */
public class IncorrectImageFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor por defecto de la excepción. Pone el mensaje
	 * adecuado
	 *
	 */
	public IncorrectImageFormatException(){
		super("Formato de imagen incorrecto");
	}
	
	/**
	 * Construye la excepción con el mensaje que se le indique
	 * @param msg Mensaje descriptivo de la excepción
	 */
	public IncorrectImageFormatException(String msg){
		super(msg);
	}
}
