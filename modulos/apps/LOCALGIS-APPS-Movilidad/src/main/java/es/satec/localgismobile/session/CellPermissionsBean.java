/**
 * CellPermissionsBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.session;

import java.io.File;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.utils.PropertiesReader;

public class CellPermissionsBean {

	/**
	 * Fichero de properties con los permisos que habrá en cada celda
	 */
	private PropertiesReader prPermisos;

	private Logger logger = (Logger) Logger.getInstance(CellPermissionsBean.class);
	
	public CellPermissionsBean(String nombreArchivoPath, String nombreArchivoPermisosCeldas){
		try{
			prPermisos = new PropertiesReader(Config.prLocalgis.getProperty("proyectos.ruta")+File.separator+nombreArchivoPath+File.separator+nombreArchivoPermisosCeldas);
		}
		catch(Exception e){
			logger.error(e);
		}
	}
		
	public boolean permisoUsuCelda(String celda, String Usuario){
		boolean permiso=false;
		try{
			String valor=prPermisos.getProperty(celda);
			if(valor!=null){
				if(valor.equals(Usuario)){
					permiso=true;
				}
			}
		} catch (MissingResourceException e) {
			logger.error(e);
		}
			
		return permiso;
	}
}
