/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 * Created on 19-nov-2004 by juacas
 *
 *
 */
/*
 * Created on 22-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin.zoom;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class ZoomToLayerPlugIn extends AbstractPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

    public ZoomToLayerPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        context.getLayerViewPanel().getViewport().zoom(EnvelopeUtil.bufferByFraction(envelopeOfSelectedLayers(context), 0.03));
        return true;
    }

    private Envelope envelopeOfSelectedLayers(PlugInContext context) throws Exception {
        Envelope envelope = new Envelope();

        for (Iterator i = Arrays.asList(
				context.getLayerNamePanel().getSelectedLayers()).iterator();
        		i.hasNext();)
		{
			Layer layer = (Layer) i.next();
			Envelope envelope2 = null;
			if (layer instanceof DynamicLayer){
			    GeopistaConnection geopistaConnection = (GeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();

			    //Miro en qué srid por defecto se almacenan las features en la base de datos
			    String sridDefecto = geopistaConnection.getSRIDDefecto(true, -1);

		    	CoordinateSystem outCoord = layer.getLayerManager().getCoordinateSystem();
		    	CoordinateSystem inCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));

		    	List listaMunicipios = AppContext.getAlMunicipios();
		    	int n = listaMunicipios.size();
		    	Geometry geom = null;
		        for (int j=0;j<n;j++){
		        	Municipio municipio = (Municipio)listaMunicipios.get(j);
		        	Geometry geom1 =  geopistaConnection.obtenerGeometriaMunicipio(municipio.getId());
			        geom1.setSRID(Integer.parseInt(sridDefecto));
	            	Reprojector.instance().reproject(geom1,inCoord, outCoord);
		        	if (geom != null)
		        		geom = geom.union(geom1);
		        	else
		        		geom = geom1;
		        }
			    geom.setSRID(outCoord.getEPSGCode());
			    envelope2 = geom.getEnvelopeInternal();
			}else
				envelope2 = layer.getEnvelope();
			if (envelope2!=null)
				envelope.expandToInclude(envelope2);
	    }
        return envelope;
    }

    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                     .add(checkFactory.createAtLeastNLayerablesMustBeSelectedCheck(1, Layerable.class))
									 .add(new EnableCheck() {
                public String check(JComponent component) {
                    ((JMenuItem) component).setText(aplicacion.getI18nString(getName()) +
                        StringUtil.s(
                            workbenchContext.getLayerNamePanel()
                                            .getSelectedLayers().length));

                    return null;
                }
            });
    }

    public void initialize(PlugInContext context) throws Exception {

        JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
        .getGuiComponent()
        .getLayerNamePopupMenu();

        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
                this, aplicacion.getI18nString(this.getName()), false, null,
                this.createEnableCheck(context.getWorkbenchContext()));
    }


}
