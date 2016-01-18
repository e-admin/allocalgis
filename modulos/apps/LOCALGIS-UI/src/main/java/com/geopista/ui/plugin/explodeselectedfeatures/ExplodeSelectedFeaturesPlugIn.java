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

package com.geopista.ui.plugin.explodeselectedfeatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.explodeselectedfeatures.images.IconLoader;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class ExplodeSelectedFeaturesPlugIn extends AbstractPlugIn {
	
	private boolean selectExplodeButtonAdded = false;
	
	public ExplodeSelectedFeaturesPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.explodeselectedfeatures.languages.ExplodeSelectedFeaturesi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ExplodeSelectedFeatures",bundle2);
    }
			
	public void initialize(PlugInContext context) throws Exception {
          
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar().addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
        
        //Líneas necesarias para añadir el PlugIn a la caja de Edición
        
        /*GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
      	geopistaEditingPlugIn.addAditionalPlugIn(this);*/
    }
	
    public boolean execute(final PlugInContext context) throws Exception {
        final ArrayList transactions = new ArrayList();
        for (Iterator i =
            context
                .getLayerViewPanel()
                .getSelectionManager()
                .getLayersWithSelectedItems()
                .iterator();
            i.hasNext();
            ) {
            Layer layerWithSelectedItems = (Layer) i.next();
            transactions.add(createTransaction(layerWithSelectedItems, context));
        }
        return EditTransaction.commit(transactions, new EditTransaction.SuccessAction() {
            public void run() {
                //SuccessActions don't run after redos, which is the behaviour we want here. [Jon Aquino]
                for (Iterator i = transactions.iterator(); i.hasNext();) {
                    EditTransaction transaction = (EditTransaction) i.next();
                    context
                        .getLayerViewPanel()
                        .getSelectionManager()
                        .getFeatureSelection()
                        .selectItems(
                        transaction.getLayer(),
                        newFeatures(transaction));
                }
            }

        });
    }

    private Collection newFeatures(EditTransaction transaction) {
        ArrayList newFeatures = new ArrayList();
        for (int i = 0; i < transaction.size(); i++) {
            if (!transaction.getGeometry(i).isEmpty()) {
                newFeatures.add(transaction.getFeature(i));
            }
        }
        return newFeatures;
    }

    private EditTransaction createTransaction(Layer layer, PlugInContext context) {
        Collection intactFeatures =
            context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
        EditTransaction transaction =
            new EditTransaction(
                new ArrayList(),
                getName(),
                layer,
                isRollingBackInvalidEdits(context),
                true,
                context.getLayerViewPanel());
        for (Iterator i = intactFeatures.iterator(); i.hasNext();) {
            Feature intactFeature = (Feature) i.next();
            transaction.deleteFeature(intactFeature);
        }
        for (Iterator i = explode(intactFeatures).iterator(); i.hasNext();) {
            Feature explodedFeature = (Feature) i.next();
            transaction.createFeature(explodedFeature);
        }
        return transaction;
    }
    private List explode(Collection features) {
        ArrayList explodedFeatures = new ArrayList();
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            GeometryCollection collection = (GeometryCollection) feature.getGeometry();
            for (int j = 0; j < collection.getNumGeometries(); j++) {
                Feature explodedFeature = (Feature) feature.clone();
                explodedFeature.setGeometry(collection.getGeometryN(j));
                explodedFeatures.add(explodedFeature);
            }
        }
        return explodedFeatures;
    }
    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
            .add(checkFactory.createAtLeastNFeaturesMustHaveSelectedItemsCheck(1))
            .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck())
            .add(new EnableCheck() {
            public String check(JComponent component) {
                Collection featuresWithSelectedItems =
                    workbenchContext
                        .getLayerViewPanel()
                        .getSelectionManager()
                        .getFeaturesWithSelectedItems();
                for (Iterator i = featuresWithSelectedItems.iterator(); i.hasNext();) {
                    Feature feature = (Feature) i.next();
                    if (!(feature.getGeometry() instanceof GeometryCollection)) {
                        return I18N.get("ExplodeSelectedFeatures","SelectedFeature")
                            + StringUtil.s(featuresWithSelectedItems.size())
                            + I18N.get("ExplodeSelectedFeatures","MustBeGeometryCollection")
                            + StringUtil.s(featuresWithSelectedItems.size());
                    }
                }
                return null;
            }
        });
    }
    
    public ImageIcon getIcon() {
        return IconLoader.icon("explosion.gif");
    }
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectExplodeButtonAdded)
        {
        	toolbox.addToolBar();
        	ExplodeSelectedFeaturesPlugIn explode = new ExplodeSelectedFeaturesPlugIn();                 
            toolbox.addPlugIn(explode, null, explode.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectExplodeButtonAdded = true;
        }
    }
}
