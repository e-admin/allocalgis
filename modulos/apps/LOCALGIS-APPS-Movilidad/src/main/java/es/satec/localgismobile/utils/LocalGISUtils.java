/**
 * LocalGISUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.utils;

import java.io.File;

public class LocalGISUtils {

	public static String slashify(String absolutePath, boolean directory) {
		String p = absolutePath;
		if (File.separatorChar != '/') {
			p = p.replace(File.separatorChar, '/');
		}
		if (!p.startsWith("/")) {
			p = "/" + p;
		}
		if (!p.endsWith("/") && directory) {
			p = p + "/";
		}
		return p;
	}
	//Borra un directorio completo
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            
            //Borra todos los ficheros del directorio
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        //Ahora que el directorio está vacío, se puede borrar
        return dir.delete();
    }


}
