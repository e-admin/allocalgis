
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Color;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

//<<TODO:REFACTORING>> Refactor code common to SelectionHandleRenderer and
//VertexRenderer [Jon Aquino]
public class FeatureSelectionRenderer extends AbstractSelectionRenderer {
    public final static String CONTENT_ID = "SELECTED_FEATURES";

   private static ApplicationContext appContext=AppContext.getApplicationContext();

   private static String temp = appContext.getI18nString("coloresRGB");
   private static String[] coloresArray = temp.split("\\;");

   private static int index = Integer.parseInt(UserPreferenceStore.getUserPreference("color.feature.seleccion","0",true));

   private static String temp2 = coloresArray[index];
   private static String[] coloresSeleccionArray = temp2.split("\\,");


    public FeatureSelectionRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel, new Color(Integer.parseInt(coloresSeleccionArray[0]),Integer.parseInt(coloresSeleccionArray[1]),Integer.parseInt(coloresSeleccionArray[2])), true, true);
    }
    
    protected CollectionMap featureToSelectedItemsMap(Layer layer) {
        return panel.getSelectionManager().getFeatureSelection().getFeatureToSelectedItemCollectionMap(layer);
    }    

}
