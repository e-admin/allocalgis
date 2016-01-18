/**
 * CellInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.session;

import java.util.Enumeration;
import java.util.Vector;

import es.satec.svgviewer.localgis.MetaInfo;

public class CellInfo {
	
	private String selectedCell;
	private Vector metaInfos;

	public CellInfo() {
		
	}
	
	public CellInfo(String selectedCell, Vector metaInfos) {
		this.selectedCell = selectedCell;
		this.metaInfos = metaInfos;
	}

	public Vector getMetaInfos() {
		return metaInfos;
	}
	
	public MetaInfo getMetaInfo(String appName) {
		if (metaInfos == null) return null;
		Enumeration e = metaInfos.elements();
		while (e.hasMoreElements()) {
			MetaInfo mi = (MetaInfo) e.nextElement();
			if (mi.getInfoType().equals(appName)) {
				return mi;
			}
		}
		return null;
	}

	public void setMetaInfos(Vector metaInfos) {
		this.metaInfos = metaInfos;
	}

	public String getSelectedCell() {
		return selectedCell;
	}

	public void setSelectedCell(String selectedCell) {
		this.selectedCell = selectedCell;
	}

}
