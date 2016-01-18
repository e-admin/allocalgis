/**
 * PMRStreetNetworkFactoryDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PMRStreetNetworkFactoryDialog extends MultiInputDialog
{
    	public static final String SOURCE_NETWORK_SELECTED_FEATURES = "Features Seleccionadas";
	public static final String SOURCE_NETWORK_FIELDNAME = "source_network";
	public static final String DRAW_LAYER_FIELDNAME = "Dibujar capa";
	public static final String TARGET_NETWORK_NAME_FIELDNAME = "Nombre de Red";
	public static final String SOURCE_LAYER_FIELDNAME = "Capa Destino";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7515232092196481759L;

	public PMRStreetNetworkFactoryDialog(Frame frame, String title, String description,PlugInContext context) {
		super(frame,title,true);
		
		boolean selected=context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems().size()!=0;
		this.addCheckBox(SOURCE_NETWORK_SELECTED_FEATURES, selected);
		getCheckBox(SOURCE_NETWORK_SELECTED_FEATURES).setEnabled(selected);
		
		this.addRow(SOURCE_NETWORK_FIELDNAME, new JLabel("Red de origen"),  new PanelToSelectMemoryNetworks(context), null, null);
		this.addEnableChecks(SOURCE_NETWORK_FIELDNAME, Arrays.asList(new EnableCheck[]{createNetworkSelectedCheck(SOURCE_NETWORK_FIELDNAME)}));
		this.addLabel(I18N.get("genred", "routeengine.capaBase"));
		this.addLayerComboBox(SOURCE_LAYER_FIELDNAME, null, I18N.get("genred", "routeengine.capaBase"),context.getLayerManager());
		this.addLabel("Dibuja resultado");
		this.addCheckBox(DRAW_LAYER_FIELDNAME, true);
		this.addSeparator();
		this.addTextField(TARGET_NETWORK_NAME_FIELDNAME, "aceras", 20, 
			new EnableCheck[]{createTextNotEmptyCheck(TARGET_NETWORK_NAME_FIELDNAME)},
			"Nombre de la red en memoria en la que se guardarán los resultados.");
		
		this.setSideBarDescription(description);
		this.setSideBarImage(getIcon());;
	}
	private EnableCheck createNetworkSelectedCheck(final String fieldname)
	{
	    return new EnableCheck()
	    {

		public String check(JComponent component)
		{
		    PanelToSelectMemoryNetworks panel = (PanelToSelectMemoryNetworks)getComponent(fieldname);
		    JLabel label=(JLabel) getLabel(fieldname);
		    if("".equals(panel.getRedSeleccionada()) && "".equals(panel.getSubredseleccionada()))
		    {
			return "Debe seleccionar alguna red en "+label.getText()+".";
		    }
		else
		    return null;
		}
	    };
	}
	public ImageIcon getIcon() {
		return IconLoader.icon("genPMRred.gif");
	}
	private EnableCheck createTextNotEmptyCheck(final String fieldname)
	{
	    return new EnableCheck()
	    {

		public String check(JComponent component)
		{
		    if(getText(fieldname).trim().length()==0)
		    {
			return "El campo "+fieldname+" no puede quedar vacío.";
		    }
		else
		    return null;
		}
	    };
	}
}
