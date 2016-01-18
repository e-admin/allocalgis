/**
 * GeneratePavementDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.pavementfactory;

import java.awt.Frame;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsCore;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

import edu.emory.mathcs.backport.java.util.Arrays;

public class GeneratePavementDialog extends MultiInputDialog
{

	public static final String SOURCE_NETWORK_FIELDNAME = "network";
	public static final String TARGET_LAYER_FIELDNAME = "Capa destino";
	public static final String TIPO_PASO_CEBRA_FIELDNAME = "tipoPasoCebra";
	/**
	 * 
	 */
	private static final long serialVersionUID = 5224329718344856621L;
	public static final String ACERASPMR_SYSTEMLAYERNAME = "aceraspmr";
	private JComboBox tipoPasos = null;
	
	private static String RED = "RedPMR";
	private JPanel rootPanel = null; 
	private JPanel featuresSelectPanel = null;
	private OKCancelPanel okCancelPanel = null;
	private PanelToSelectMemoryNetworks redesPanel = null;
	private boolean bPasosCebra = false;

	protected PlugInContext context;
	private boolean targetLayer;
	private String description;

//	public GeneratePavementDialog(Frame frame, String nombreRed, PlugInContext context){
//		this(frame, nombreRed, context,"", false, false);
//	}
	public GeneratePavementDialog(Frame frame, String nombreRed, PlugInContext context, String description,boolean pasosZebra, boolean targetLayer){
		super(frame, "", true);
		this.context = context;

//		this.setSize(440, 175);
//		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.bPasosCebra = pasosZebra;
		this.targetLayer = targetLayer;
		this.description = description;
		this.initialize();
		Blackboard blackboard = AppContext.getApplicationContext().getBlackboard();
		if (blackboard != null && blackboard.get(RED) != null)
			redesPanel.setSelected(blackboard.get(RED).toString());
	}


	protected void initialize() {
		
	    	redesPanel = new PanelToSelectMemoryNetworks(context);
	    	addLabel("Red que contiene las aceras");
		this.addRow(SOURCE_NETWORK_FIELDNAME, new JLabel(""),  redesPanel, null, null);
		this.addEnableChecks(SOURCE_NETWORK_FIELDNAME, Arrays.asList(new EnableCheck[]{NetworkModuleUtilToolsCore.createEnableNetworkSelectedCheck(this,SOURCE_NETWORK_FIELDNAME)}));
		
		if (bPasosCebra)
		    {
			java.util.List tiposCebra = Arrays.asList(new String[]{"CON REBAJE","SIN REBAJE"});
			addLabel("Tipo de pasos de cebra:");
			this.addComboBox(TIPO_PASO_CEBRA_FIELDNAME, tiposCebra.get(0), (Collection) tiposCebra, "");
			
		    }
		if (targetLayer)
		    {
			this.addLabel("Elija la capa en la que se depositarán los resultados. Recomendada la capa de sistema \""+ACERASPMR_SYSTEMLAYERNAME+"\"");
			Layer pmrLayer = context.getLayerManager().getLayer(ACERASPMR_SYSTEMLAYERNAME);
			this.addLayerComboBox(TARGET_LAYER_FIELDNAME, pmrLayer, I18N.get("genred", "routeengine.capaBase"),context.getLayerManager());
		    }
		this.setSideBarDescription(getPluginDescription());
		this.setSideBarImage(getIcon());	

	}
	private String getPluginDescription()
	{
	   return description;
	}
	public Icon getIcon() {
	        return IconLoader.icon("cebraAuto.gif");
	    }
	public String getRedSeleccionada()
	{
	    return redesPanel.getRedSeleccionada();
	}
	public String getSubredseleccionada()
	{
	    return redesPanel.getSubredseleccionada();
	}
	
}
