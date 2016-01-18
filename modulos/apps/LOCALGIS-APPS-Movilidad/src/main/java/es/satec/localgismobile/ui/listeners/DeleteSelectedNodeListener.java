/**
 * DeleteSelectedNodeListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Messages;
import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class DeleteSelectedNodeListener implements SVGViewerLinkListener, Listener {
	SVGLocalGISViewer viewer=null;
	SVGNode nodoSel=null;
	boolean borrar=false;

	public DeleteSelectedNodeListener(SVGLocalGISViewer view) {

		viewer=view;
	}

	public void nodeSelected(SVGNode arg0, Point arg1) {

			nodoSel=arg0;
			borrar=true;
	}
	
	public void handleEvent(Event event) {

		SVGDocument doc=viewer.getSVGDocument();
		if(doc.root.children.count!=0){
			if(viewer.getActiveLayerNode()!=null){
				if(viewer.getActiveLayerNode().isEditable()){
					if(nodoSel == null){
						MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
						mb.setMessage(Messages.getMessage("borrarSeleccion.error"));
						mb.open();
					}
					else{

						if(borrar){
							int pos=nodoSel.parent.children.indexOf(nodoSel, 0);
							nodoSel.parent.removeChildAndRecordEvent(pos);
					        viewer.drawSVG();
						}
						else{
							viewer.cancelDraw();
						}
					}
				}
			}
		}
	}

}
