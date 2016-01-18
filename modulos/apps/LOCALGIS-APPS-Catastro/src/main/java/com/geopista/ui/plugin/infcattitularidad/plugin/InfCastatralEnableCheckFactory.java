/**
 * InfCastatralEnableCheckFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin.infcattitularidad.plugin;

import javax.swing.JComponent;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;

	public class InfCastatralEnableCheckFactory extends GeopistaEnableCheckFactory
	{
	private WorkbenchContext workbenchContext;
	private ApplicationContext aplicacion=AppContext.getApplicationContext();
	public InfCastatralEnableCheckFactory(WorkbenchContext workbenchContext)
	{
		super(workbenchContext);
	    this.workbenchContext = workbenchContext;
	}

	public EnableCheck createAtLeastConvenioTitularidadMustBeSelectedCheck() {
	  
		return createAtLeastFeaturesInLayerParcelasMustBeSelectedCheck();
	}

	public EnableCheck createAtLeastFeaturesInLayerParcelasMustBeSelectedCheck() {
	    return new EnableCheck() {
	        public String check(JComponent component) {

	            Layer parcelas = workbenchContext.getLayerManager().getLayer("parcelas");
	            if(workbenchContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(parcelas).size() == 0)
	            {
	              return ("Debe seleccionarser al menos 1 Features en la capa de parcelas");
	            }
	            
	            return null;
	       }
	    };
	}


   
}