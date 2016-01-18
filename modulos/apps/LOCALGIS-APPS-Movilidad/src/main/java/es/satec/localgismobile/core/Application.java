/**
 * Application.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.core;

import java.util.ArrayList;
import java.util.Vector;

public class Application {

	private String name;
	private String layerId;
	//private String keyAttribute;
	private ArrayList keyAttribute;
	private Vector options;

	public Application() {
		
	}

	public Application(String name, String layerId, ArrayList keyAttribute, Vector options) {
		this.name = name;
		this.layerId = layerId;
		this.keyAttribute = keyAttribute;
		this.options = options;
	}

	public ArrayList getKeyAttribute() {
		return keyAttribute;
	}

	public void setKeyAttribute(ArrayList keyAttribute) {
		this.keyAttribute = keyAttribute;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Vector getOptions() {
		return options;
	}
	
	public void setOptions(Vector options) {
		this.options = options;
	}
}
