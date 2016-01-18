/**
 * RemoteLocalException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.remote.exceptions;

import es.satec.localgismobile.fw.common.remote.exception.RemoteException;

/**
 * Esta clase se usa para saber que la excepcion no se ha generado en el servidor,
 * sino que ha sido generado en local.
 * @version 1.0
 * @created 11-dic-2006 18:05:22
 */
public class RemoteLocalException extends RemoteException {

	/**
	 * Constructor por defecto
	 *
	 */
	public RemoteLocalException(){

	}
	
	/**
	 * Constructor al que se le indica un mensaje
	 * @param mensaje
	 */
	public RemoteLocalException(String mensaje){
		super(mensaje);
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}