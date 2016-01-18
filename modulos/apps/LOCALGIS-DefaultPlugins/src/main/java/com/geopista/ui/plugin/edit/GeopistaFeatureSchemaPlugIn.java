/**
 * GeopistaFeatureSchemaPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edit;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.external.LayerInfo;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaFeatureSchemaPlugIn extends AbstractPlugIn
{

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaFeatureSchemaPlugIn.category";

    private Feature clonefeature = null;

    private Feature localFeature = null;

    static public final ImageIcon ICON = IconLoader.icon("Sheet.gif");

    public static final String IDENT_EXTENDED_FORM="IDENT_EXTENDED_FORM";

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

        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addPopupMenuItem(popupMenu, this, aplicacion
                .getI18nString(getName()), false, GUIUtil.toSmallIcon(ICON),
                createEnableCheck(context.getWorkbenchContext()));

        
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        List capasVisibles = context.getWorkbenchContext().getLayerNamePanel()
                .getLayerManager().getVisibleLayers(true);
        Iterator capasVisiblesIter = capasVisibles.iterator();
        boolean cancelWhile = false;

        final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

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
                if (cancelWhile == true)
                {
                    if (JOptionPane
                            .showConfirmDialog(
                                    (Component) context.getWorkbenchGuiComponent(),
                                    aplicacion
                                            .getI18nString("GeopistaFeatureSchemaPlugIn.RestoFeatures"),
                                    aplicacion
                                            .getI18nString("GeopistaFeatureSchemaPlugIn.EditarMultiplesEntidades"),
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
                        return false;
                    else
                        cancelWhile = false;
                }

                localFeature = (Feature) featuresSeleccionadasIter.next();
                String systemId = ((GeopistaFeature)localFeature).getSystemId();
                final ArrayList lockResultaArrayList = new ArrayList();
                // capa de sistema. La feature debe bloquearse
                if( capaActual instanceof GeopistaLayer
                        &&
                        !((GeopistaLayer)capaActual).isLocal()
                        &&
                        !((GeopistaLayer)capaActual).isExtracted()
                        &&
                        capaActual.isEditable()
                        &&
                        systemId!=null
                        &&
                        !((GeopistaFeature)localFeature).isTempID()
                        &&
                        !systemId.equals(""))
                {
                
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
                                                    localFeature, progressDialog);
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
                }

                FeatureDialog featureDialog = new FeatureDialog(GeopistaFunctionUtils
                        .getFrame(context.getWorkbenchGuiComponent()), "Atributos", true,
                        localFeature, context.getWorkbenchContext().getLayerViewPanel(),capaActual);
                
                Integer tempLockID = null;
                Iterator lockResultaArrayListIter = lockResultaArrayList.iterator();
                if (lockResultaArrayListIter.hasNext())
                {
                    tempLockID = (Integer) lockResultaArrayListIter.next();
                }
                final Integer lockID = tempLockID;

                try
                {

                    ImageIcon icon = IconLoader.icon("logo_geopista.png");

                    featureDialog.setSideBarImage(null);
                } catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                if( capaActual instanceof GeopistaLayer
                        &&
                        !((GeopistaLayer)capaActual).isLocal()
                        &&
                        !((GeopistaLayer)capaActual).isExtracted())
                {
                	//System.out.println(capaActual.getClass().getName());
                    String extendedForm = ((GeopistaLayer) capaActual)
                            .getFieldExtendedForm();
                    if (extendedForm == null)
                        extendedForm = "";
                    if (!extendedForm.equals(""))
                    {

                        featureDialog.setExtendedForm(extendedForm);
                    }                   
                    
                    String layerName = ((GeopistaLayer)capaActual).getSystemId();
                    LayerInfo layerInfo = new LayerInfo(layerName);
                    Hashtable queries = layerInfo.getQueries();
                    if (queries != null && queries.size()>0) {
                    	Blackboard blackboard = aplicacion.getBlackboard();
                    	blackboard.put("ExternalQueries",queries);
                    	featureDialog.setExtendedForm("com.geopista.ui.plugin.external.ExternalInformationForm");
                    }
                    
                }

                //Este es el metodo que tarda tanto porque tiene que construir todas las pestañas
                featureDialog.buildDialog();
                if ((capaActual instanceof GeopistaLayer
                        &&
                        (((GeopistaLayer)capaActual).isLocal() ||
                                ((GeopistaLayer)capaActual).isExtracted())
                        &&
                        !capaActual.isEditable()
                        )||
                        (lockID == null
                        &&
                        capaActual instanceof GeopistaLayer
                        &&
                        !((GeopistaLayer)capaActual).isLocal()
                        &&
                        !((GeopistaLayer)capaActual).isExtracted()
                        &&
                        !capaActual.isEditable()
                        &&
                        systemId!=null
                        &&
                        !((GeopistaFeature)localFeature).isTempID()
                        &&
                        !systemId.equals("")))
                {
                    featureDialog.setLock();
                }
                featureDialog.setVisible(true);
                // solo para GeopistaLayer
                
                if (featureDialog.wasOKPressed())
                {
                    if (capaActual.isEditable())
                    {
                        
                        if (capaActual instanceof GeopistaLayer && 
                        		!((GeopistaLayer)capaActual).isLocal() &&
								lockID==null) continue;
                    	// obtenemos la feature con los cambios introducidos por
                        // el usuario
                        clonefeature = featureDialog.getModifiedFeature();
                        // Actualiza los parámetros
                        execute(new UndoableCommand(getName())
                            {

                                public void execute()
                                {
                                    
                                    localFeature.setAttributes(clonefeature
                                            .getAttributes());
                                }

                                public void unexecute()
                                {
                                    ((GeopistaFeature) clonefeature)
                                            .setFireDirtyEvents(false);
                                    clonefeature.setAttributes(localFeature
                                            .getAttributes());
                                    ((GeopistaFeature) clonefeature)
                                            .setFireDirtyEvents(true);
                                }
                            }, context);

                    }

                } else
                {
                    // el usuario ha pedido cancelar la edición
                    cancelWhile = true;

                }

                if (lockID != null)
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
                                                        lockID, progressDialogFinal);
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

            }
        }

        return false;
    }

    public ImageIcon getIcon()
    {
        return ICON;
    }
    
	public boolean hasExternalQueries(String name) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/geopista","geopista","geopista");
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(selectQuery + name + "'");
			if (resultSet.next())  {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static final String selectQuery = "SELECT * FROM QUERIES_EXTERNOS WHERE campo_interno_enlace like '%.";	
 }

