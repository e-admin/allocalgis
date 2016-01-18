/**
 * EditSelectedFeaturePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 31-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin.edit;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.LockManager;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.WorkbenchException;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.EnterWKTDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.WKTDisplayHelper;
import com.vividsolutions.jump.workbench.ui.plugin.WKTPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class EditSelectedFeaturePlugIn extends WKTPlugIn {
    private Feature globalFeature;
    private Integer tempLockID = null;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    public EditSelectedFeaturePlugIn() {}
    protected Layer layer(PlugInContext context) {
        return (Layer) context
            .getLayerViewPanel()
            .getSelectionManager()
            .getLayersWithSelectedItems()
            .iterator()
            .next();
    }
    
    public void initialize(PlugInContext context) throws Exception {
        
    JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
    FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
    featureInstaller.addPopupMenuItem(popupMenu, this,
    		GeopistaFunctionUtils.i18n_getname(this.getName()), false, null,
    EditSelectedFeaturePlugIn.createEnableCheck(context.getWorkbenchContext()));
    
    featureInstaller.addMainMenuItem(this, GeopistaFunctionUtils.i18n_getname("Edit"),
    		GeopistaFunctionUtils.i18n_getname(this.getName()), null,
            EditSelectedFeaturePlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        
    }
    public String getName() {
        return "View / Edit Selected Feature";
    }
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
            .add(checkFactory.createExactlyNFeaturesMustHaveSelectedItemsCheck(1));
    }
    public boolean execute(PlugInContext context) throws Exception {
        return execute(
            context,
            (Feature) context
                .getLayerViewPanel()
                .getSelectionManager()
                .getFeaturesWithSelectedItems()
                .iterator()
                .next(),
            true);
    }
    public boolean execute(PlugInContext context, Feature feature, boolean editable)
        throws Exception {
        this.globalFeature = feature;
        
        String systemID = ((GeopistaFeature) globalFeature).getSystemId();
        tempLockID = null;
        final LockManager lockManager = (LockManager) aplicacion.getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
        
        
        if(systemID!=null&&!((GeopistaFeature) globalFeature).isTempID()&&!systemID.equals("")&&layer(context).isEditable())
        {
         
        final ArrayList lockResultaArrayList = new ArrayList();
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), context.getErrorHandler());

        progressDialog.setTitle(aplicacion.getI18nString("LockFeatures"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {

                                try
                                {

                                    Integer lockID = lockManager.lockFeature(
                                            globalFeature, progressDialog);
                                    lockResultaArrayList.add(lockID);
                                } catch (Exception e)
                                {

                                } finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
        

        
        
        Iterator lockResultaArrayListIter = lockResultaArrayList.iterator();
        Object tempObjectLockId = null;
        if (lockResultaArrayListIter.hasNext()) tempObjectLockId = lockResultaArrayListIter.next();
            
        if(tempObjectLockId!=null && tempObjectLockId instanceof Integer)
        {
           tempLockID = (Integer) tempObjectLockId;
            
        }
        
        }
        
        
        
        reportNothingToUndoYet(context);
        
        
        boolean result = super.execute(context);
        
        if (tempLockID != null)
        {
            final TaskMonitorDialog progressDialogFinal = new TaskMonitorDialog(
                    aplicacion.getMainFrame(), context.getErrorHandler());

            progressDialogFinal.setTitle(aplicacion
                    .getI18nString("UnlockFeatures"));
            progressDialogFinal.addComponentListener(new ComponentAdapter()
                {
                    public void componentShown(ComponentEvent e)
                    {

                        // Wait for the dialog to appear before starting
                        // the
                        // task. Otherwise
                        // the task might possibly finish before the
                        // dialog
                        // appeared and the
                        // dialog would never close. [Jon Aquino]
                        new Thread(new Runnable()
                            {
                                public void run()
                                {

                                    try
                                    {

                                        lockManager.unlockFeaturesByLockId(
                                                tempLockID, progressDialogFinal);
                                    } catch (Exception e)
                                    {

                                    } finally
                                    {
                                        progressDialogFinal.setVisible(false);
                                    }
                                }
                            }).start();
                    }
                });
            GUIUtil.centreOnWindow(progressDialogFinal);
            progressDialogFinal.setVisible(true);
        }
            
            return result;
    }
    protected void apply(String wkt, PlugInContext context) throws Exception {
        
        boolean locked = true;
        if(globalFeature instanceof GeopistaFeature)
        {
            String systemID = ((GeopistaFeature) globalFeature).getSystemId();
            if(systemID==null||((GeopistaFeature) globalFeature).isTempID()||systemID.equals(""))
            {
                locked = false;
            }
            else
            {
                if(tempLockID == null)
                {
                    locked = true;
                }
                else
                {
                    locked = false;
                }
            }
        }
        if (!layer(context).isEditable()||(locked==true)) {
            return;
        }
        super.apply(wkt, context);
    }
    protected void apply(FeatureCollection c, PlugInContext context)
        throws WorkbenchException {
        if (c.size() != 1) {
            throw new WorkbenchException("Expected 1 feature but found " + c.size());
        }
        EditTransaction transaction =
            new EditTransaction(
                Arrays.asList(new Feature[] { globalFeature }),
                getName(),
                layer,
                isRollingBackInvalidEdits(context),
                false,
                context.getWorkbenchGuiComponent());
        //Can't simply pass the LayerViewPanel to the transaction because if there is
        //an attribute viewer up and its TaskFrame has been closed, the LayerViewPanel's
        //LayerManager will be null. [Jon Aquino]
        transaction.setGeometry(0, ((Feature) c.iterator().next()).getGeometry());
        transaction.commit();
    }
    protected EnterWKTDialog createDialog(PlugInContext context) {
        
        
        
        EnterWKTDialog d = super.createDialog(context);
        d.setTitle(
            (layer(context).isEditable() ? "Edit " : "")
                + "Feature "
                + globalFeature.getID()
                + " In "
                + layer
                + (layer(context).isEditable() ? "" : " (layer is uneditable)"));
        boolean locked = true;
        if(globalFeature instanceof GeopistaFeature)
        {
            String systemID = ((GeopistaFeature) globalFeature).getSystemId();
            if(systemID==null||((GeopistaFeature) globalFeature).isTempID()||systemID.equals(""))
            {
                locked = false;
            }
            else
            {
                if(tempLockID == null)
                {
                    locked = true;
                }
                else
                {
                    locked = false;
                }
            }
        }
        
        
        boolean editableFeature = layer(context).isEditable()&&locked==false;
        d.setEditable(editableFeature);
        d.setText(helper.format(globalFeature.getGeometry().toString()));
        return d;
    }
    private WKTDisplayHelper helper = new WKTDisplayHelper();
}

