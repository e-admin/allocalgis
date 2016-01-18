/**
 * ProjectFolderNameFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;

public class ProjectFolderNameFilter implements FilenameFilter {

	private static Logger logger = Global.getLoggerFor(ProjectFolderNameFilter.class);
	
	public boolean accept(File dir, String name) {
		try {
			if (dir.isDirectory()) {
				int dot1 = name.indexOf(".", 0);
				if (dot1 > 0) {
					int dot2 = name.indexOf(".", dot1+1);
					if (dot2 - dot1 > 1) {
						String idMunicipio = name.substring(dot1+1, dot2);
						Integer.parseInt(idMunicipio);
						String timestamp = name.substring(dot2+1, name.length());
						Long.parseLong(timestamp);
						return true;
					}
				}
			}
		} catch (Exception e) {}
			logger.warn("Carpeta de proyecto no válida: " + name);
		return false;
	}

}
