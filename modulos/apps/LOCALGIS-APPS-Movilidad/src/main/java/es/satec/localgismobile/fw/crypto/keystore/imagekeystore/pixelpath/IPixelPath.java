/**
 * IPixelPath.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.crypto.keystore.imagekeystore.pixelpath;

/**
 * Genera un recorrido a través de los píxeles de una imagen
 * @author jpolo
 *
 */
public interface IPixelPath {
	/**
	 * Obtiene el índice del siguiente pixel
	 * @return El índice del siguiente pixel
	 */
	public int getNextPathIndex();
	
	/**
	 * Resetea el recorrido, situándose al comienzo
	 * del mismo
	 *
	 */
	public void resetPath();
	
	/**
	 * Obtiene la posición enésima del recorrido de píxeles
	 * @param index Índice de la posición del recorrido de la imagen
	 * de la que se quiere conocer el píxel correspondiente
	 * @return El píxel correspondiente a la posición indicada
	 * del recorrido de la imagen
	 */
	public int getPathIndex(int index);
}
