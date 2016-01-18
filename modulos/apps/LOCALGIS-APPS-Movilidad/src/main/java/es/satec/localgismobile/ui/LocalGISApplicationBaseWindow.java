/**
 * LocalGISApplicationBaseWindow.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Shell;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGNode;

import es.satec.svgviewer.localgis.MetaInfo;

public abstract class LocalGISApplicationBaseWindow extends LocalGISWindow {

	protected SVGNode currentNode;
	protected MetaInfo metaInfo;
	protected GeopistaSchema metaInfoSchema;
	protected Hashtable params;

	public LocalGISApplicationBaseWindow(Shell parent, SVGNode currentNode, MetaInfo metaInfo, GeopistaSchema metaInfoSchema, Hashtable params) {
		super(parent);
		this.currentNode = currentNode;
		this.metaInfo = metaInfo;
		this.metaInfoSchema = metaInfoSchema;
		this.params = params;
	}

}
