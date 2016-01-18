/**
 * GridToCellLinkListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.listeners;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;

import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.session.CellInfo;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISMainWindow;
import es.satec.svgviewer.event.SVGViewerLinkListener;

public class GridToCellLinkListener implements SVGViewerLinkListener {
	
	private static String cellIdAtt = "CELL_ID";
	
	private static Logger logger = Global.getLoggerFor(GridToCellLinkListener.class);

	public void nodeSelected(SVGNode node, Point pt) {
		if (node != null && node.nameAtts != null && !node.nameAtts.isEmpty()) {
			String cell = null;
			try {
				int ind = node.parent.getPosByNameLayertAtt(cellIdAtt);
				cell = (String) node.nameAtts.elementAt(ind);
				logger.debug("Celda seleccionada: " + cell);
				String projectPath = SessionInfo.getInstance().getProjectInfo().getPath();
				File cellFile = new File(projectPath + File.separator + cell + ".svg");

				if (cellFile.exists()) {
					CellInfo cellInfo = new CellInfo();
					cellInfo.setSelectedCell(cell);
					SessionInfo.getInstance().setCellInfo(cellInfo);
					LocalGISMobile.getMainWindow().go(LocalGISMainWindow.CELL_SCREEN_COMPOSITE);
				}
				else {
					logger.debug("No hay contenido para la celda seleccionada");
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
					mb.setMessage(Messages.getMessage("GridScreen.celdaVacia"));
					mb.open();
				}
			} catch (Exception e) {
				logger.error("Error al pasar de cuadricula a la celda " + cell, e);
			}

		}
	}
}
