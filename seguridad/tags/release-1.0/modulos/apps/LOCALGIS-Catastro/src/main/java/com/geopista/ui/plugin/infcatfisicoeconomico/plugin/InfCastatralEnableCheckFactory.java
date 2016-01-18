/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/


package com.geopista.ui.plugin.infcatfisicoeconomico.plugin;

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
	
	public EnableCheck createAtLeastConvenioFisicoEconomicoMustBeSelectedCheck() {
		  
		return createAtLeastFeaturesInLayerParcelasMustBeSelectedCheck();
		
	}

    public EnableCheck createAtLeastFeaturesInLayerParcelasMustBeSelectedCheck() {
        return new EnableCheck() {
            public String check(JComponent component) {

            	Layer parcelas = new Layer();
                parcelas = workbenchContext.getLayerManager().getLayer("parcelas");
                if(workbenchContext.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(parcelas).size() == 0)
                {
                  return ("Debe seleccionar al menos 1 Features en la capa de parcelas");
                }
                
                return null;
           }
        };
    }


   
}