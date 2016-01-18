/**
 * LockFeaturesPlugInRenombrado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 21-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.geopista.ui.plugin;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FeatureLockResult;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.ui.LockListener;
import com.geopista.ui.LockManager;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LockFeaturesPlugInRenombrado extends ThreadedBasePlugIn implements LayerViewPanelListener ,InternalFrameListener, LockListener
{

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();
    private String toolBarCategory = "GeopistaLoadMapPlugIn.category";
    private JToggleButton jToggleButton = null;
    private PlugInContext globalContext = null;
    private LockManager currentLockManager = null; 

    

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
        .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
        .add(checkFactory.createAtLeastNFeaturesMustBeSelectedCheck(1));

    }

    public String getName()
    {
        return "Lock Features";
    }

    public void initialize(PlugInContext context) throws Exception
    {
        globalContext = context;
        currentLockManager = new LockManager(this);
        String pluginCategory = aplicacion.getString(toolBarCategory);
        LockFeaturesPlugIn lockFeaturesPlugIn = new LockFeaturesPlugIn();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent()
                .getToolBar(pluginCategory).setTaskMonitorManager(
                        new TaskMonitorManager());
        jToggleButton = new JToggleButton();
        context.getWorkbenchContext().getIWorkbench().getGuiComponent()
                .getToolBar(pluginCategory).add(
                        jToggleButton,
                        lockFeaturesPlugIn.getName(),
                        lockFeaturesPlugIn.getIcon(),
                        AbstractPlugIn.toActionListener(lockFeaturesPlugIn, context
                                .getWorkbenchContext(), new TaskMonitorManager()), this.createEnableCheck(context.getWorkbenchContext()));
        
        //      instalamos el LockManager de cada uno de los Task
        initializeLockManager(context);

    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("lock_open_big.png");
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);

        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
    	globalContext=context; // Cada ejecución tiene su contexto de ejecución
        LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
        Blackboard blackboard = aplicacion
                .getBlackboard();
        if (lockManager == null)
        {
            return;
        }
        if (blackboard.get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY) == null)
        {
            
            try
            {
                Integer lockID = lockManager.lockSelectedFeatures(new ArrayList(context
                        .getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems()), monitor);
                blackboard.put(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY, lockID);
                
            } catch (ACException e1)
            {
                ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("ErrorAlBloquearFeatures"), aplicacion
                        .getI18nString("ErrorAlBloquearFeatures"), StringUtil
                        .stackTrace(e1));
                return;
            } catch (LockException e1)
            {
            	FeatureLockResult featureLockResult = e1.getFeatureLockResult();
            	if (featureLockResult != null){
            		String errorMsg = null;
            		if (featureLockResult.getLockResultCode() == AdministradorCartografiaClient.LOCK_FEATURE_ERROR){
                		errorMsg = aplicacion.getI18nString("featurelock.error.genericerror");
                	}
                	else if (featureLockResult.getLockResultCode() == AdministradorCartografiaClient.LOCK_FEATURE_OTHER){
                		errorMsg = aplicacion.getI18nString("featurelock.error.alreadylocked");
                		if (featureLockResult.getLockOwnerUserName() != null){
                			errorMsg += "\n " + aplicacion.getI18nString("featurelock.username") + ": "
                				+ featureLockResult.getLockOwnerUserName();    			
                		}
                		if (featureLockResult.getLayer() != null){
                			errorMsg +=  "\n " + aplicacion.getI18nString("featurelock.layer") + ": "
            					+ featureLockResult.getLayer();
                		}
                		if (featureLockResult.getFeatureId() != -1){
                			errorMsg += "\n Feature Id: " + featureLockResult.getFeatureId();
                		}
                	}
                	if (errorMsg != null){
                		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), errorMsg, null, JOptionPane.WARNING_MESSAGE);                		
                	}
            	}
            	else {
            		ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                            .getI18nString("ErrorAlBloquearFeatures"), aplicacion
                            .getI18nString("ErrorAlBloquearFeatures"), StringUtil
                            .stackTrace(e1));	
            	}
                
                return;
            } catch (Exception e1)
            {
                ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("ErrorAlBloquearFeatures"), aplicacion
                        .getI18nString("ErrorAlBloquearFeatures"), StringUtil
                        .stackTrace(e1));
                return;
            }
        } else
        {
            Integer lockId = (Integer) blackboard
                    .get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY);
            lockManager.unlockFeaturesByLockId(lockId, monitor);
            blackboard.put(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY, null);
        }
    }
    
    private void initializeLockManager(PlugInContext context)
    {
        //Instalamos el plugin de bloqueo de Features
        
        
        //Aseguramos que todos los Task iniciales tienen un LockMager
        TaskComponent[] frames = context.getWorkbenchGuiComponent().getInternalTaskComponents();

        if (frames != null)

        {
            for (int i = 0; i < frames.length; i++)
            {

                ensureHasLockManager((TaskComponent) frames[i]);
                ensureHasChangeSelectionListener((TaskComponent) frames[i]);
                
            }
        } 
        
        Blackboard generalBlackboard = aplicacion.getBlackboard();
        if(generalBlackboard.get(LockManager.LOCK_MANAGER_KEY)==null)
        {
            generalBlackboard.put(LockManager.LOCK_MANAGER_KEY,currentLockManager);
        }

        context.getWorkbenchGuiComponent().getDesktopPane().addContainerListener(
                new ContainerAdapter()
                    {
                        public void componentAdded(ContainerEvent e)
                        {
                            if (!(e.getChild() instanceof TaskFrame))
                            {
                                return;
                            }

                            TaskFrame taskFrame = (TaskFrame) e.getChild();
                            ensureHasLockManager(taskFrame);
                            ensureHasChangeSelectionListener(taskFrame);
                        }
                    });
    }

    private void ensureHasLockManager(final TaskComponent taskComponent)
    {
        
         Blackboard currentBlackboard = taskComponent.getLayerViewPanel().getBlackboard();
         if(currentBlackboard.get(LockManager.LOCK_MANAGER_KEY)==null)
         {
             
             
             currentBlackboard.put(LockManager.LOCK_MANAGER_KEY,currentLockManager);
             //Ponemos este listener para controlar el estado del boton
             taskComponent.addInternalFrameListener(this);
         }
         
             
         
    }
    
    private void ensureHasChangeSelectionListener(final TaskComponent taskComponent)
    {
        taskComponent.getLayerViewPanel().addListener(this);  
  
    }
    
    

    /* (non-Javadoc)
     * @see com.vividsolutions.jump.workbench.ui.LayerViewPanelListener#selectionChanged()
     */
    public void selectionChanged()
    {
        if(globalContext.getActiveInternalFrame()==null) return;
      
        Blackboard blackboard = aplicacion.getBlackboard();
        LockManager currentLockManager = (LockManager) blackboard.get(LockManager.LOCK_MANAGER_KEY);
        Integer lockId = (Integer) blackboard.get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY);
        
        if(lockId!=null)
        {
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
                    globalContext.getErrorHandler());
            progressDialog.setTitle(aplicacion.getI18nString("LockFeatures"));
            progressDialog.addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Wait for the dialog to appear before starting the task. Otherwise
                    //the task might possibly finish before the dialog appeared and the
                    //dialog would never close. [Jon Aquino]
                    new Thread(new Runnable(){
                        public void run()
                        {
                            try
                            {
                                
                                Blackboard localBlackboard = aplicacion.getBlackboard();
                                LockManager localCurrentLockManager = (LockManager) localBlackboard.get(LockManager.LOCK_MANAGER_KEY);
                                Integer localLockId = (Integer) localBlackboard.get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY);
                                
                                localCurrentLockManager.unlockFeaturesByLockId(localLockId,progressDialog);
                                localBlackboard.put(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY,null);
                            }catch(Exception e)
                            {
                             }finally
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
        
    }

    /* (non-Javadoc)
     * @see com.vividsolutions.jump.workbench.ui.LayerViewPanelListener#cursorPositionChanged(java.lang.String, java.lang.String)
     */
    public void cursorPositionChanged(String x, String y)
    {
 
        
    }

    /* (non-Javadoc)
     * @see com.vividsolutions.jump.workbench.ui.LayerViewPanelListener#painted(java.awt.Graphics)
     */
    public void painted(Graphics graphics)
    {

        
    }
    

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameActivated(InternalFrameEvent e)
    {
        TaskFrame sourceTask = (TaskFrame) e.getSource(); 
        Integer currentlockId = (Integer) sourceTask.getLayerViewPanel().getBlackboard().get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY);
        
        if(currentlockId==null)
        {
            jToggleButton.setSelected(false);
        }
        else
        {
            jToggleButton.setSelected(true);
        }
        
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameClosed(InternalFrameEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameClosing(InternalFrameEvent e)
    {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameDeactivated(InternalFrameEvent e)
    {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameDeiconified(InternalFrameEvent e)
    {
       
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameIconified(InternalFrameEvent e)
    {
        
    }

    /* (non-Javadoc)
     * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
     */
    public void internalFrameOpened(InternalFrameEvent e)
    {
       
    }
    
    public void updateButton()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Integer lockId = (Integer) globalContext.getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY);
                if(lockId==null)
                {
                    jToggleButton.setSelected(false);
                }
                else
                {
                    jToggleButton.setSelected(true);
                }
            }
        });
                
        
    }
    
    
    public void updateLayerViewPanel()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

            	if (globalContext.getWorkbenchFrame()!=null){
            		JInternalFrame[] frames = globalContext.getWorkbenchFrame().getInternalFrames();

            		if (frames != null)

            		{
            			for (int i = 0; i < frames.length; i++)
            			{
            				if(frames[i] instanceof TaskComponent)
            				{
            					((TaskComponent) frames[i]).getLayerViewPanel().repaint();
            				}
            			}
            		} 
            	}
            }
        });
                
        
    }

}
