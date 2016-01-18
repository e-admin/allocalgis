/**
 * SigmManagerPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.plugin.sigm;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.TextDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class SigmManagerPlugin extends ThreadedBasePlugIn
{
    private static final Log logger = LogFactory.getLog(SigmManagerPlugin.class);
 
    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaFeatureSchemaPlugIn.category";

    private Feature localFeature = null;

    static public final ImageIcon ICON = IconLoader.icon("texto.gif");

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithSelectionManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));

    }

    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent())
                .getToolBar(pluginCategory).addPlugIn(getIcon(), this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext());
/*
        JPopupMenu popupMenu = LayerViewPanel.popupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());
        if (popupMenu!=null){
        	featureInstaller.addPopupMenuItem(popupMenu, this, aplicacion
                .getI18nString(getName()), false, GUIUtil.toSmallIcon(ICON),
                createEnableCheck(context.getWorkbenchContext()));
        }
*/
        //Añadido por aso
        Blackboard Identificadores = aplicacion.getBlackboard();
        Vector vExtendedForm = (Vector)Identificadores.get(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM);
        if (vExtendedForm==null) vExtendedForm=new Vector();
        vExtendedForm.add(new com.localgis.app.sigm.SigmExtendedForm());

        Identificadores.put(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM,vExtendedForm);


    }

    public boolean execute(PlugInContext context) throws Exception
    {
        if (!aplicacion.isOnline())
        {
            JOptionPane.showMessageDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
            return false;
        }

        List capasVisibles = context.getWorkbenchContext().getLayerNamePanel()
                .getLayerManager().getVisibleLayers(true);
        Iterator capasVisiblesIter = capasVisibles.iterator();

        final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

        Vector features= new Vector();
        final ArrayList lockResultaArrayList = new ArrayList();
        boolean bloquear = false; //Si alguna de las features esta en una capa
                                           // capa de sistema. La feature debe bloquearse

        while (capasVisiblesIter.hasNext())
        {
            Layer capaActual = (Layer) capasVisiblesIter.next();
            Collection featuresSeleccionadas = context.getWorkbenchContext()
                    .getLayerViewPanel().getSelectionManager()
                    .getFeaturesWithSelectedItems(capaActual);
            // Almacenamos en este ArrayList el resultado de la operacion de
            // bloqueo
            Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
            while (featuresSeleccionadasIter.hasNext())
            {
                localFeature = (Feature) featuresSeleccionadasIter.next();
                String systemId = ((GeopistaFeature)localFeature).getSystemId();
                if (!((GeopistaLayer)capaActual).isLocal() && !((GeopistaLayer)capaActual).isExtracted())
                     features.add(localFeature);
                // capa de sistema. La feature debe bloquearse
                if( capaActual instanceof GeopistaLayer   &&
                        !((GeopistaLayer)capaActual).isLocal() &&
                        !((GeopistaLayer)capaActual).isExtracted() &&
                        capaActual.isEditable()&& systemId!=null &&
                        !((GeopistaFeature)localFeature).isTempID() &&
                        !systemId.equals(""))
                {

                       final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                        .getMainFrame(), context.getErrorHandler());
                       progressDialog.setTitle(aplicacion.getI18nString("LockFeatures"));
                       progressDialog.addComponentListener(new ComponentAdapter()
                       {
                            public void componentShown(ComponentEvent e)
                            {
                                   new Thread(new Runnable(){
                                        public void run(){
                                            try{
                                                Integer lockID = lockManager.lockFeature(localFeature, progressDialog);
                                                lockResultaArrayList.add(lockID);
                                            } catch (Exception e){}
                                            finally{
                                                progressDialog.setVisible(false);
                                            }
                                        }
                                }).start();
                        }});
                        GUIUtil.centreOnWindow(progressDialog);
                        progressDialog.setVisible(true);
                }else
                    bloquear=true;
            }
        }
        if (features.size()==0)
       {
           JOptionPane.showMessageDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                   aplicacion.getI18nString("mensaje.no.capasistema"));
           return false;
       }

        TextDialog textDialog = new TextDialog(GeopistaFunctionUtils
                        .getFrame(context.getWorkbenchGuiComponent()), "Atributos", true,
                        features, context.getWorkbenchContext().getLayerViewPanel());

        try{
                ImageIcon icon = IconLoader.icon("logo_geopista.png");
                textDialog.setSideBarImage(icon);
        } catch (NullPointerException e){
                logger.error("Error el icono logo_geopista.png", e);
        }
        textDialog.buildDialog();
        if (bloquear) textDialog.setLock();
          textDialog.setVisible(true);
          // Hay que desbloquear los bloqueados
          for (Iterator it=lockResultaArrayList.iterator();it.hasNext();)
          {
                final Integer lockID=(Integer)it.next();
                final TaskMonitorDialog progressDialogFinal = new TaskMonitorDialog(
                            aplicacion.getMainFrame(), context.getErrorHandler());
                progressDialogFinal.setTitle(aplicacion
                            .getI18nString("UnlockFeatures"));
                progressDialogFinal.addComponentListener(new ComponentAdapter(){
                    public void componentShown(ComponentEvent e)
                    {
                        new Thread(new Runnable() {
                                        public void run()
                                        {
                                            try
                                            {
                                                    lockManager.unlockFeaturesByLockId(
                                                        lockID, progressDialogFinal);
                                            } catch (Exception e){
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
        return false;
    }

    public ImageIcon getIcon()
    {
        return ICON;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
