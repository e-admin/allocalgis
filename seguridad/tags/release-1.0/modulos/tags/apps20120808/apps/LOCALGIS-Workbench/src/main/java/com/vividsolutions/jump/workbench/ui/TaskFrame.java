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
package com.vividsolutions.jump.workbench.ui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.editor.TaskComponent;
import com.geopista.model.DynamicLayer;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITaskNameListener;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.LayerTreeModel;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.cursortool.DummyTool;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

public class TaskFrame
    extends JInternalFrame
    implements
        TaskFrameProxy,
        CloneableInternalFrame,
        LayerViewPanelProxy,
        LayerNamePanelProxy,
        LayerManagerProxy,
        SelectionManagerProxy,
        ITaskNameListener, TaskComponent, ITaskFrame {
    BorderLayout borderLayout = new BorderLayout();
    JSplitPane splitPane = new JSplitPane();

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getTaskFrame()
	 */
    @Override
	public TaskComponent getTaskFrame() {
        return this;
    }

    /** @deprecated */
    private int cloneIndex;
    private InfoFrame infoFrame = null;
    private LayerNamePanel layerNamePanel = new DummyLayerNamePanel();
    private LayerViewPanel layerViewPanel;
    private Task task;
    protected WorkbenchContext workbenchContext;

    public TaskFrame(Task task, WorkbenchContext workbenchContext) {
        this(task, 0, workbenchContext);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getSelectionManager()
	 */
    @Override
	public SelectionManager getSelectionManager() {
        return getLayerViewPanel().getSelectionManager();
    }

    private ILayerManager layerManager;

    public TaskFrame(
        Task task,
        int cloneIndex,
        final WorkbenchContext workbenchContext) {
        this.task = task;
        this.layerManager = task.getLayerManager();
        this.cloneIndex = cloneIndex;
        this.workbenchContext = workbenchContext;
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameDeactivated(InternalFrameEvent e) {
                //Deactivate the current CursorTool. Otherwise, the following problem
                //can occur:
                //  --  Start drawing a linestring on a task frame. Don't double-click
                //      to end the gesture.
                //  --  Open a new task frame. You're still drawing the linestring!
                //      This shouldn't happen; instead, the drawing should be cancelled.
                //[Jon Aquino]
                layerViewPanel.setCurrentCursorTool(new DummyTool());
            }

            public void internalFrameClosed(InternalFrameEvent e) {
                try {
                    layerViewPanel.dispose();
                    layerNamePanel.dispose();
                } catch (Throwable t) {
                    workbenchContext.getIWorkbench().getGuiComponent().handleThrowable(t);
                }
            }
            public void internalFrameOpened(InternalFrameEvent e) {
                //Set the layerNamePanel when the frame is opened, not in the constructor,
                //because #createLayerNamePanel may be overriden in a subclass, and the
                //subclass has not yet been constructed -- weird things happen, like variables
                //are unexpectedly null. [Jon Aquino]
                splitPane.remove((Component) layerNamePanel);
                layerNamePanel = createLayerNamePanel();
                splitPane.add((Component) layerNamePanel, JSplitPane.LEFT);
                layerNamePanel.addListener(
                    workbenchContext
                        .getIWorkbench()
                        .getGuiComponent()
                        .getLayerNamePanelListener());
            }

        });
        layerViewPanel =
            new LayerViewPanel(
                task.getLayerManager(),
                (LayerViewPanelContext) workbenchContext.getIWorkbench().getGuiComponent());

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        layerViewPanel.addListener(
            workbenchContext.getIWorkbench().getGuiComponent().getLayerViewPanelListener());
        layerViewPanel.getViewport().addListener(
        		(ViewportListener)workbenchContext.getIWorkbench().getGuiComponent());
        task.add(this);
        installAnimator();
    }

    protected LayerNamePanel createLayerNamePanel() {
        TreeLayerNamePanel treeLayerNamePanel =
            new TreeLayerNamePanel(
                this,
                new LayerTreeModel(this),
                this.layerViewPanel.getRenderingManager(),
                new HashMap());
        Map nodeClassToPopupMenuMap =
            this.workbenchContext.getIWorkbench().getGuiComponent().getNodeClassToPopupMenuMap();
        for (Iterator i = nodeClassToPopupMenuMap.keySet().iterator(); i.hasNext();) {
            Class nodeClass = (Class) i.next();
            treeLayerNamePanel.addPopupMenu(nodeClass, (JPopupMenu) nodeClassToPopupMenuMap.get(nodeClass));
        }
        return treeLayerNamePanel;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getLayerManager()
	 */
    @Override
	public ILayerManager getLayerManager() {
        return task.getLayerManager();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getInfoFrame()
	 */
    @Override
	public InfoFrame getInfoFrame() {
        if (infoFrame == null || infoFrame.isClosed()) {
            infoFrame = new PrimaryInfoFrame(workbenchContext, this, this);
        }
        return infoFrame;
    }

    @Override
	public LayerNamePanel getLayerNamePanel() {
        return layerNamePanel;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getLayerViewPanel()
	 */
    @Override
	public LayerViewPanel getLayerViewPanel() {
        return layerViewPanel;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getTask()
	 */
    @Override
	public Task getTask() {
        return  task;
    }

    private int nextCloneIndex() {
        String key = getClass().getName() + " - LAST_CLONE_INDEX";
        task.getLayerManager().getBlackboard().put(
            key,
            1 + task.getLayerManager().getBlackboard().get(key, 0));

        return task.getLayerManager().getBlackboard().getInt(key);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#internalFrameClone()
	 */
    @Override
	public JInternalFrame internalFrameClone() {
        TaskFrame clone = new TaskFrame(task, nextCloneIndex(), workbenchContext);
        clone.splitPane.setDividerLocation(0);
        clone.setSize(300, 300);

        if (task.getLayerManager().size() > 0) {
            clone.getLayerViewPanel().getViewport().initialize(
                getLayerViewPanel().getViewport().getScale(),
                getLayerViewPanel().getViewport().getOriginInModelCoordinates());
            clone.getLayerViewPanel().setViewportInitialized(true);
        }

        return clone;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#taskNameChanged(java.lang.String)
	 */
    @Override
	public void taskNameChanged(String name) {
        updateTitle();
    }

    //The border around the tree layer panel looks a bit thick under JDK 1.4.
    //Remedied by removing the split pane's border. [Jon Aquino]
    private void jbInit() throws Exception {
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);

        //Allow some of the background to show so that user sees this is an MDI app
        //[Jon Aquino]
        this.setSize(680, 380);
        this.getContentPane().setLayout(borderLayout);
        splitPane.setBorder(null);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.add((Component) layerNamePanel, JSplitPane.LEFT);
        splitPane.add(layerViewPanel, JSplitPane.RIGHT);
        splitPane.setDividerLocation(200);
        updateTitle();
    }

    private void updateTitle() {
        String title = task.getName();
        if (cloneIndex > 0) {
            title += " (View " + (cloneIndex + 1) + ")";
        }
        setTitle(title);
    }
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getSplitPane()
	 */
    @Override
	public JSplitPane getSplitPane() {
        return splitPane;
    }

    private void installAnimator() {
        Timer timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (wmsRenderingInProgress()) {
                    repaint();
                    showingClocks = true;
                } else if (showingClocks) {
                    repaint();
                    showingClocks = false;
                }
            }
            private boolean showingClocks = false;
            private boolean wmsRenderingInProgress() {
                for (Iterator i = layerManager.getLayerables(WMSLayer.class).iterator();
                    i.hasNext();
                    ) {
                    WMSLayer wmsLayer = (WMSLayer) i.next();
                    Renderer renderer =
                        layerViewPanel.getRenderingManager().getRenderer(wmsLayer);
                    if (renderer != null && renderer.isRendering()) {
                        return true;
                    }
                }
                for (Iterator i = layerManager.getLayerables(DynamicLayer.class).iterator();
                i.hasNext();
                ) {
	                DynamicLayer wmsLayer = (DynamicLayer) i.next();
		            Renderer renderer =
		                layerViewPanel.getRenderingManager().getRenderer(wmsLayer);
		            if (renderer != null && renderer.isRendering()) {
		                return true;
	                }
	            }
                return false;
            }
        });
        timer.setCoalesce(true);
        timer.start();
    }

    @Override
	public void add(ITaskNameListener nameListener)
    {
      
    }

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.TaskFrameProxy#getTaskComponent()
	 */
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ITaskFrame#getTaskComponent()
	 */
	@Override
	public TaskComponent getTaskComponent()
	{
	
	return this;
	}

}
