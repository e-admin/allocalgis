/**
 * DownloadProjectEntry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import java.text.SimpleDateFormat;

/**Se almacenan los datos del proyecto
 * que se reciben del servidor**/
public class DownloadProjectEntry implements Comparable {
	private String idProyecto;
	private String nombreProyecto;
	private String fechaExtraccion;
	private String idMunicipio;
	private String idMap;
	public String getFechaExtraccion() {
		return fechaExtraccion;
	}
	public void setFechaExtraccion(String fechaExtraccion) {
		this.fechaExtraccion = fechaExtraccion;
	}
	public String getIdMap() {
		return idMap;
	}
	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(String idProyecto) {
		this.idProyecto = idProyecto;
	}
	public String getNombreProyecto() {
		return nombreProyecto;
	}
	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}
	public int compareTo(Object o) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try{ 
			return -(sdf.parse(this.getFechaExtraccion()).compareTo(sdf.parse(((DownloadProjectEntry) o).getFechaExtraccion())));
		}catch (Exception e){
			return 0;
		}
	}
}
