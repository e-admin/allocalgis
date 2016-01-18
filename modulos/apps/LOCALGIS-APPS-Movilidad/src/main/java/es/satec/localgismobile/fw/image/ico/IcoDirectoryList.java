/**
 * IcoDirectoryList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.image.ico;

import java.util.Vector;

/**
 * Clase que almacena la lista de entradas de directorio presente
 * en el icono. Cada una de las entradas de directorio contiene
 * información sobre cada una de las imágenes presentes en el icono
 * @author jpolo
 *
 */
public class IcoDirectoryList {
	/**
	 * Tipo de icono
	 */
	protected int type;
	/**
	 * Número de entradas de directorio
	 */
	protected int numberOfEntries; // idCount en MSDN
	/**
	 * Vector con cada una de las entradas de directorio
	 */
	protected Vector directoryEntries;
	
	public IcoDirectoryList() {
		directoryEntries = new Vector();
	}
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Vector getDirectoryEntries() {
		return directoryEntries;
	}
	public void setDirectoryEntries(Vector directoryEntries) {
		this.directoryEntries = directoryEntries;
	}	
	
	public IcoDirectoryEntry getDirectoryEntry(int index){
		IcoDirectoryEntry directoryEntry = (IcoDirectoryEntry) directoryEntries.elementAt(index);
		return directoryEntry;
	}
	
}
