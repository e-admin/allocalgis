package com.geopista.ui.plugin.edit;

import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.task.TaskMonitor;
import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.InventarioDialog;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.GeopistaUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 11-jul-2006
 * Time: 14:01:22
 * To change this template use File | Settings | File Templates.
 */
public class InventarioManagerPlugIn  extends ThreadedBasePlugIn{
    private static final Log logger = LogFactory.getLog(InventarioManagerPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaFeatureSchemaPlugIn.category";

    private Feature localFeature = null;


    static public final ImageIcon ICON = IconLoader.icon("patrimonio.gif");

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

        JPopupMenu popupMenu = LayerViewPanel.popupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addPopupMenuItem(popupMenu, this, aplicacion
                .getI18nString(getName()), false, GUIUtil.toSmallIcon(ICON),
                createEnableCheck(context.getWorkbenchContext()));

        //Añadido por aso
        Blackboard Identificadores= aplicacion.getBlackboard();
        Vector vExtendedForm= (Vector)Identificadores.get(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM);
        if (vExtendedForm==null) vExtendedForm=new Vector();
        vExtendedForm.add(new com.geopista.app.inventario.InventarioExtendedForm());

//        Identificadores.put(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM,vExtendedForm);

    }

    public boolean execute(PlugInContext context) throws Exception
    {
        if (!aplicacion.isOnline())
        {
            JOptionPane.showMessageDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
            return false;
        }

        List capasVisibles = context.getWorkbenchContext().getLayerNamePanel()
                .getLayerManager().getVisibleLayers(true);
        Iterator capasVisiblesIter = capasVisibles.iterator();

        final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

        localFeature=null;
        while (capasVisiblesIter.hasNext())
        {
            Layer capaActual = (Layer) capasVisiblesIter.next();
            Collection featuresSeleccionadas = context.getWorkbenchContext()
                    .getLayerViewPanel().getSelectionManager()
                    .getFeaturesWithSelectedItems(capaActual);
            // Almacenamos en este ArrayList el resultado de la operacion de
            // bloqueo
            Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
            boolean cancelWhile = false;
            if (capaActual instanceof GeopistaLayer && (((GeopistaLayer)capaActual).isLocal() || ((GeopistaLayer)capaActual).isExtracted()))
               continue;

            while (featuresSeleccionadasIter.hasNext())
            {
                if (cancelWhile == true)//En la feature anterior se cancelo, ahora se pide confirmación
                {
                    if (JOptionPane.showConfirmDialog((Component) context.getWorkbenchGuiComponent(),
                                    aplicacion.getI18nString("GeopistaFeatureSchemaPlugIn.RestoFeatures"),
                                    aplicacion.getI18nString("GeopistaFeatureSchemaPlugIn.EditarMultiplesEntidades"),
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
                        return false;
                    else
                        cancelWhile = false;
                }
                localFeature = (Feature) featuresSeleccionadasIter.next();
                String systemId = ((GeopistaFeature)localFeature).getSystemId();
                   final ArrayList lockResultaArrayList = new ArrayList();
                // capa de sistema. La feature debe bloquearse
                boolean bloquear=false;
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


                Vector feature=new Vector();
                feature.add(localFeature);
                InventarioDialog inventarioDialog = new InventarioDialog(GeopistaUtil
                        .getFrame(context.getWorkbenchGuiComponent()), "", true,
                        feature, context.getWorkbenchContext().getLayerViewPanel());
                try{
                    ImageIcon icon = IconLoader.icon("logo_geopista.png");
                    inventarioDialog.setSideBarImage(icon);
                } catch (NullPointerException e){
                        logger.error("Error el icono logo_geopista.png", e);
                }
                inventarioDialog.buildDialog();
                if (bloquear) inventarioDialog.setLock();
                inventarioDialog.setVisible(true);
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
        }



        }
        if (localFeature==null)
            JOptionPane.showMessageDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),aplicacion.getI18nString("mensaje.no.capasistema"));
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
