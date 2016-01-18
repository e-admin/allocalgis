/**
 * ImprimirSerieVistaPreliminarPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.dialogs;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.MapDocument;
import com.geopista.util.PrintPreviewPanel;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class ImprimirSerieVistaPreliminarPanel extends JPanel implements WizardPanel
{
	private String localId = null;
	private String nextID=null;
	
	private WizardContext wizardContext;
	private Blackboard blackboard  = ((AppContext) AppContext.getApplicationContext()).getBlackboard();
	
	private JSplitPane jSplitPane1 = new JSplitPane();
	private GeopistaLayerNamePanel legendPanel = null;
	private JPanel imagePanel = new JPanel();
	private String accion = null;
	
	private PlugInContext context;
	private PageFormat pf = null;
	private PrinterJob job = null;
	private Book cuadriculasBook = null;

	public ImprimirSerieVistaPreliminarPanel(String id, String nextID, PlugInContext context) throws Exception
	{
		this.nextID = nextID;
		this.localId = id;
		this.context = context;
		setName(UtilsPrintPlugin.getMessageI18N("SerieVistaPreliminarPanel.Name"));
		jbInit();
	}


	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());
		//  jSplitPane1.setBounds(new Rectangle(45, 40, 650, 430));
		jSplitPane1.setBackground(new Color(212, 211, 211));
		// this.setSize(750,600);  
		add(jSplitPane1, BorderLayout.CENTER);
	}//jbinit


	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		return nextID;
	}

	public void enteredFromLeft(Map dataMap)
	{
		//Obtener cobfiguracion establecida
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		Layer selectLayer = config.getCapaIteracion();

		accion = null;

		job = (PrinterJob) blackboard.get(SeriePrintPlugIn.IMPRIMIRJOB);
		if (job == null)
			job = PrinterJob.getPrinterJob();
		pf = job.defaultPage();

		ILayerViewPanel layerViewPanel = context.getWorkbenchContext().getLayerViewPanel();

		//Miramos si se debe mostrar la leyenda y si no es asi no la creamos
		legendPanel = null;
		if(config.isMostrarLeyenda())
		{
			legendPanel = new GeopistaLayerNamePanel(
					(LayerManagerProxy) layerViewPanel,
					new GeopistaLayerTreeModel((LayerManagerProxy) layerViewPanel),
					layerViewPanel.getRenderingManager(),
					new HashMap());
			jSplitPane1.setLeftComponent(legendPanel);
		}

		jSplitPane1.setLeftComponent(legendPanel);
		
		//Almacenamos valor actual de "visible" de la capa seleccionada para restaurar posteriormente
		boolean  selectLayerVisible = selectLayer.isVisible();
		//Eliminar la capa actual de la vista para generar la informacion sin visualizar la capa seleccionada
		selectLayer.setVisible(false);
		cuadriculasBook = new Book();
		
		//Obtener escala
		int escala = Integer.parseInt(config.getIdEscala());
		
		Feature actualCuadricula = null;
		Envelope actualEnvelope = null;
		MapDocument mapDocument = null;
		//Procesar cada cuadricula
		Iterator cuadriculasFeaturesIter = config.getLstCuadriculas().iterator();
		while(cuadriculasFeaturesIter.hasNext()) {
			actualCuadricula = (Feature) cuadriculasFeaturesIter.next();
			actualEnvelope = actualCuadricula.getGeometry().getEnvelopeInternal();
			mapDocument = new MapDocument(legendPanel, layerViewPanel, pf, escala, actualEnvelope);
			cuadriculasBook.append(mapDocument, pf);
		}

		//Restauramos valor original visible
		selectLayer.setVisible(selectLayerVisible);
		imagePanel = new PrintPreviewPanel(cuadriculasBook);

		jSplitPane1.setRightComponent(imagePanel);
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception
	{
		blackboard.put(SeriePrintPlugIn.IMPRIMIR_BOOK, cuadriculasBook);
		accion = "print";
		blackboard.put(SeriePrintPlugIn.IMPRIMIRACCION, accion);
	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
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
		return UtilsPrintPlugin.getMessageI18N("SerieVistaPreliminarPanel.Instrucctions");

	}
	public boolean isInputValid() {
		return true;
	}
	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}


	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}
}
