
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

package com.vividsolutions.jump.workbench.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.RegisterPlugInManager;
import com.geopista.util.exception.DuplicatedRegisteredPlugInException;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.model.UndoableEditReceiver;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * Default implementation of PlugIn, with useful functions for auto-generating
 * a name, converting a PlugIn into an ActionListener (for use with JButtons, 
 * for example), and supporting undo.
 */
public abstract class AbstractPlugIn implements PlugIn {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractPlugIn.class);
    
    AppContext appContext=(AppContext) AppContext.getApplicationContext();
    Blackboard blackboard = appContext.getBlackboard();
    
    protected void execute(UndoableCommand command, PlugInContext context) {
        execute(command, (LayerManagerProxy)context.getLayerViewPanel());
    }
    
    public boolean equals (Object obj)
    {
            return this.getClass().equals(obj.getClass());
    }

    public AbstractPlugIn() {
    }
    
    public AbstractPlugIn(String name) {
        this.name = name;
    }

    public void initialize(PlugInContext context) throws Exception {
        
//      No lanzamos la excepcion para no alterar el resto del codigo
        try
        {
            registerPlugin(this,context); 
        }catch(DuplicatedRegisteredPlugInException e)
        {
            logger.debug("initialize(PlugInContext)", e);
        }
    }

    public boolean execute(PlugInContext context) throws Exception {
        return true;
    }

    /**
     * @return the class name, minus "PlugIn", with
     * spaces inserted at the appropraite point before each
     * uppercase+lowercase and lowercase+uppercase combination.
     */
    public String getName() {
        return name == null ? createName(getClass()) : name;
    }
    
    private String name;

    public static String createName(Class plugInClass) {
        return StringUtil.toFriendlyName(plugInClass.getName(), "PlugIn");
    }

    /**
     * @param taskMonitorManager can be null if you do not wish to use the
     * Task Monitor progress-reporting framework
     */
    public static ActionListener toActionListener(
        final PlugIn plugIn,
        final WorkbenchContext workbenchContext,
        final TaskMonitorManager taskMonitorManager) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    workbenchContext
                        .getIWorkbench()
                        .getGuiComponent()
                        .setStatusMessage(
                        "");
                    workbenchContext.getIWorkbench().getGuiComponent().log(
                        "Executing " + plugIn.getName());

                    PlugInContext plugInContext =
                        workbenchContext.createPlugInContext();
                    //Cache the UndoableEditReceiver, because the "topmost"
                    //layer manager before the edit may be different from the
                    //topmost layer manager after (e.g. NewTaskPlugIn). [Jon Aquino]                        
                    UndoableEditReceiver undoableEditReceiver =
                        workbenchContext.getLayerManager() != null
                            ? workbenchContext
                                .getLayerManager()
                                .getUndoableEditReceiver()
                            : null;
                    if (undoableEditReceiver != null) {
                        undoableEditReceiver.startReceiving();
                    }

                    try {
                    	//System.out.println("Ejecutando plugin"+plugIn.getName());
                        boolean executeComplete = plugIn.execute(plugInContext);

                        if (plugIn instanceof ThreadedPlugIn
                            && executeComplete
                            && (taskMonitorManager != null)) {
                        	//System.out.println("Ejecutando plugin");
                            taskMonitorManager.execute(
                                (ThreadedPlugIn) plugIn,
                                plugInContext);
                        }/*else{
                        	System.out.println("El plugin no se puede ejecutar");
                        }*/
                    } finally {
                        if (undoableEditReceiver != null) {
                            undoableEditReceiver.stopReceiving();
                        }
                    }

                    workbenchContext.getIWorkbench().getGuiComponent().log(
                        "Done. Current committed memory: "
                            + workbenchContext
                                .getIWorkbench()
                                .getGuiComponent()
                                .getMBCommittedMemory()
                            + " MB");
                } catch (Throwable t) {
                t.printStackTrace();
                    workbenchContext.getErrorHandler().handleThrowable(t);
                }
            }
        };
    }

    public static void execute(
        UndoableCommand command,
        LayerManagerProxy layerManagerProxy) {
        //Used to do nothing if command or panel were null, but that seems to me now
        //like a dangerous thing to do. So I've taken it out, and hopefully will receive
        //a complaint from someone about a NullPointerException. When I find out
        //why, I'll be sure to document the reason! [Jon Aquino]
        boolean exceptionOccurred = true;
        try {
            command.execute();
            exceptionOccurred = false;
        } finally {
            //Funny logic because I want to avoid adding a throws clause to this method,
            //so that existing code will not break [Jon Aquino 12/5/2003]
            if (exceptionOccurred) {
                layerManagerProxy.getLayerManager().getUndoableEditReceiver()
                        .getUndoManager().discardAllEdits();
            }
        }
        if ((layerManagerProxy!=null) && (layerManagerProxy.getLayerManager()!=null))
        layerManagerProxy.getLayerManager().getUndoableEditReceiver()
            .receive(command.toUndoableEdit());        
    }

    public String toString() {
        return getName();
    }

    /**
     * Indicates that this plug-in either (1) is undoable but hasn't modified the
     * system yet or (2) does not modify the system. In either case, the undo history
     * will be preserved. If this method is not called, then this plug-in will be assumed
     * to be non-undoable, and the undo history will be truncated.
     */
    protected void reportNothingToUndoYet(PlugInContext context) {
        //The LayerManager can be null if for example there are no TaskFrames and
        //the user selects File / New Task. When we get to this point, LayerManager
        //will be null. [Jon Aquino]
        if (context.getLayerManager() == null) {
            return;
        }
        context
            .getLayerManager()
            .getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

    protected boolean isRollingBackInvalidEdits(PlugInContext context) {
        return context
            .getWorkbenchContext()
            .getIWorkbench()
            .getBlackboard()
            .get(
            EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY,
            false);
    }
    
    protected void registerPlugin(PlugIn newPlugin, PlugInContext context) throws DuplicatedRegisteredPlugInException
    {
        if(isRegisteredPlugin(newPlugin,context)) throw new DuplicatedRegisteredPlugInException("El Plugin ya ha sido Registrado"+newPlugin);
        
        
        RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
        if(registerPlugInManager == null)
        {
            registerPlugInManager = new RegisterPlugInManager();
            blackboard.put("RegisterPlugInManager",registerPlugInManager);
        }
        
        
//      Comprobamos si el PlugIn esta ya instalado en este menu y si
//      es asi no lo volvemos a instalar
        
        
        if(registerPlugInManager.isRegisteredPlugIn(context.getWorkbenchContext().getIWorkbench().getPlugInManager(),newPlugin))
        {
            return;
        }
//      Si el PlugIn no esta instalado lo registramos en la lista de PlugIn
        registerPlugInManager.registerPlugIn(context.getWorkbenchContext().getIWorkbench().getPlugInManager(),newPlugin);

    }
    
    protected boolean isRegisteredPlugin(PlugIn newPlugin, PlugInContext context)
    {
        RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
        if(registerPlugInManager == null)
        {
            registerPlugInManager = new RegisterPlugInManager();
            blackboard.put("RegisterPlugInManager",registerPlugInManager);
        }
        return registerPlugInManager.isRegisteredPlugIn(context.getWorkbenchContext().getIWorkbench().getPlugInManager(),newPlugin);
    }
    
    public void addButton(final ToolboxDialog toolbox)
    {
    	
    }
      
}
