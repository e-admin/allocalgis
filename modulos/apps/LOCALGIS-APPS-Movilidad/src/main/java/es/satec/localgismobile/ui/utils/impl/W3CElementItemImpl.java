/**
 * W3CElementItemImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils.impl;

 
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class W3CElementItemImpl extends ItemNode {

	
	Node node;
	public W3CElementItemImpl(String name) {
		super(name);
	}

	
	public W3CElementItemImpl(Node element, String name) {
		super(name);
		this.node  = element;
	}

	public void setContent(String value) {
		
		// Modifico el valor del nodo y meto la marca de que se ha cambiado
		node.setNodeValue(value);
		Node parent = node.getParentNode();
		if ( (parent != null) && (parent instanceof Element)) {
			((Element)( parent)).setAttribute("modified", "true");
		}
	}

}
