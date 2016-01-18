package com.geopista.ui.plugin;

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


import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.I18NUtils;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class NewMapPlugIn extends AbstractPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private String toolBarCategory = "GeopistaNewMapPlugIn.category";
    public NewMapPlugIn() {
    }

    public String getName() {
        return aplicacion.getI18nString("NewMap");
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);

        List listaSistemasCoordenadas = new ArrayList();
        listaSistemasCoordenadas.add(new CoordinateSystem("Seleccione...", 0, null, 0,null));
        listaSistemasCoordenadas.addAll(CoordinateSystemRegistry.instance(
                context.getWorkbenchContext().getBlackboard()).getCoordinateSystems());
        for (int i = 0; i < listaSistemasCoordenadas.size(); i++) {
            CoordinateSystem cS = (CoordinateSystem) listaSistemasCoordenadas.get(i);
            if (cS.getName().equals("Unspecified")){
                listaSistemasCoordenadas.remove(i);
                break;
            }               
        }
        
        CoordinateSystem destination = (CoordinateSystem) JOptionPane.showInputDialog(
                (Component) context.getWorkbenchGuiComponent(), AppContext.getApplicationContext().getI18nString("SelectCoordinateReferenceSystem"),
                getName(), JOptionPane.PLAIN_MESSAGE, null, listaSistemasCoordenadas.toArray(),
                listaSistemasCoordenadas.get(0));
                        
        if (destination == null || destination.getEPSGCode() == 0) {
            return false;
        }        
        context.getWorkbenchGuiComponent().addTaskFrame();
        context.getLayerManager().setCoordinateSystem(destination);
        return true;
    }

    public void initialize(PlugInContext context) throws Exception
    {
    	String pluginCategory = aplicacion.getString(toolBarCategory);
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("File"),
            I18NUtils.i18n_getname(this.getName()) + "...", null, null);
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory).addPlugIn(this.getIcon(),
                this,
                null,
                context.getWorkbenchContext());
    }
    public ImageIcon getIcon() {
        return IconLoader.icon("Nuevo_mapa.GIF");
    }
}

