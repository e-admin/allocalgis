/**
 * GeopistaVistaPreliminarPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.dialogs;
import java.awt.BorderLayout;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.MapDocument;
import com.geopista.util.PrintPreviewPanel;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;

public class GeopistaVistaPreliminarPanel extends JPanel implements WizardPanel
{

	private String nextID=null;
	private String localId = null;
	private WizardContext wizardContext;
	private PlugInContext context;
	
	private JSplitPane jSplitPane1 = new JSplitPane();
	private GeopistaLayerNamePanel legendPanel = null;
	private JPanel imagePanel = new JPanel();
	private String accion = null;
	private MapDocument mapDocument = null;
	private Book bookDocument = null;
	private PageFormat pf = null;
	private PrinterJob job = null;
	
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();

	public GeopistaVistaPreliminarPanel(String id, String nextId, PlugInContext context) //throws Exception
	{
		this.nextID = nextId;
		this.localId = id;
		this.context = context;
		setName(UtilsPrintPlugin.getMessageI18N("GeopistaVistaPreliminarPanel.Name"));
		jbInit();

	}


	private void jbInit() //throws Exception
	{
		this.setLayout(new BorderLayout());

		this.add(jSplitPane1, BorderLayout.CENTER);
		//    jSplitPane1.setBounds(new Rectangle(45, 40, 650, 430));
		//    jSplitPane1.setBackground(new Color(212, 211, 211));
		// this.setSize(750,600);  

	}//jbinit

	public void enteredFromLeft(Map dataMap)
	{
		//Obtenemos configuracion establecida
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);

		accion = null;
		job = (PrinterJob) blackboard.get(GeopistaPrintPlugIn.IMPRIMIRJOB);
		pf = (PageFormat) blackboard.get(GeopistaPrintPlugIn.IMPRIMIRPF);

		ILayerViewPanel layerViewPanel = context.getWorkbenchContext().getLayerViewPanel();

		//Miramos si se debe mostrar la leyenda y si no es asi no la creamos
		boolean showLegend = config.isMostrarLeyenda();
		legendPanel = null;
		if(showLegend) {
			legendPanel = new GeopistaLayerNamePanel(
					(LayerManagerProxy)layerViewPanel,
					new GeopistaLayerTreeModel((LayerManagerProxy)layerViewPanel),
					(RenderingManager) layerViewPanel.getRenderingManager(),
					new HashMap());
			jSplitPane1.setLeftComponent(legendPanel);
		}

		jSplitPane1.setLeftComponent(legendPanel);

		mapDocument = new MapDocument(legendPanel, (LayerViewPanel) layerViewPanel, pf, Integer.parseInt(config.getIdEscala()));

		//Sacamos la escala a la que queremos imprimir el Mapa
		bookDocument = new Book();
		bookDocument.append(mapDocument,pf);
		imagePanel = new PrintPreviewPanel(bookDocument);
		jSplitPane1.setRightComponent(imagePanel);
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		accion = "print";
		blackboard.put(GeopistaPrintPlugIn.IMPRIMIRMAPDOCUMENT, bookDocument);
		blackboard.put(GeopistaPrintPlugIn.IMPRIMIRPF,pf);  
		blackboard.put(GeopistaPrintPlugIn.IMPRIMIRACCION,accion);
	}

	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		return nextID;
	}
	public void add(InputChangedListener listener) {
	}
	public void remove(InputChangedListener listener){
	}
	public String getTitle() {
		return this.getName();
	}
	public String getID() {
		return localId;
	}
	public String getInstructions() {
		return UtilsPrintPlugin.getMessageI18N("GeopistaVistaPreliminarPanel.Instrucctions");
	}
	public boolean isInputValid() {
		return true;
	}
	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}
	public void exiting() {
		// TODO Auto-generated method stub
	}
}