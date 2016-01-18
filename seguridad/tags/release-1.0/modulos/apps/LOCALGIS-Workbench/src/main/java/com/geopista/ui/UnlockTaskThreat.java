/*
 * Created on 27-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UnlockTaskThreat extends Thread
{

    public int confirmResultError = 0;



    private TaskFrame taskFrame = null;

    private TaskMonitorDialog progressDialog = null;

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    public UnlockTaskThreat(TaskFrame taskFrame,
            TaskMonitorDialog progressDialog)
        {
         
            this.taskFrame = taskFrame;
            this.progressDialog = progressDialog;
        }

    public void run()
    {
        try
        {
            LockManager localLockManager = (LockManager) taskFrame
                    .getLayerViewPanel().getBlackboard()
                    .get(LockManager.LOCK_MANAGER_KEY);
            localLockManager.unlockAllLockedFeatures((LayerManager)taskFrame.getLayerManager(), progressDialog);
            taskFrame.getLayerViewPanel().getBlackboard().put(
                    LockManager.SELECTION_TOOL_LOCK_GROUP_ID_KEY, null);
        } catch (Exception e)
        {
            Object[] optionsError = { aplicacion.getI18nString("ContinuarCerrando"),
                    aplicacion.getI18nString("OKCancelPanel.Cancel") };
            confirmResultError = JOptionPane.showOptionDialog((Component) aplicacion
                    .getMainFrame(), aplicacion.getI18nString("ErrorAlDesbloquear"), null,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    optionsError, optionsError[0]);
            taskFrame.getLayerViewPanel().getBlackboard().put(
                    "UnlockTaskComponentResult", new Integer(confirmResultError));
        } finally
        {
            progressDialog.setVisible(false);
        }
    }
}
