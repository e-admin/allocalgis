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
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UnlockAllFeaturesThreat extends Thread
{
    
    public int confirmResultError = 0;

    

    

    private TaskMonitorDialog progressDialog = null;

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    public UnlockAllFeaturesThreat(TaskMonitorDialog progressDialog)
        {
            
            
            this.progressDialog = progressDialog;
        }

    public void run()
    {
        try
        {
            LockManager localLockManager = (LockManager) aplicacion.getBlackboard()
                    .get(LockManager.LOCK_MANAGER_KEY);
            localLockManager.unlockAllLockedFeatures(progressDialog);
            
        } catch (Exception e)
        {
            Object[] optionsError = { aplicacion.getI18nString("ContinuarCerrando"),
                    aplicacion.getI18nString("OKCancelPanel.Cancel") };
            confirmResultError = JOptionPane.showOptionDialog((Component) aplicacion
                    .getMainFrame(), aplicacion.getI18nString("ErrorAlDesbloquear"), null,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    optionsError, optionsError[0]);
            
        } finally
        {
            progressDialog.setVisible(false);
        }
    }

}
