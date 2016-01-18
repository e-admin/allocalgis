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
package com.geopista.ui.cursortool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.RegisterPlugInManager;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.snap.SnapManager;
import com.vividsolutions.jump.workbench.ui.snap.SnapPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToFeaturesPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToGridPolicy;
import com.vividsolutions.jump.workbench.ui.snap.SnapToVerticesPolicy;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


/**
 *  A tool that draws an XOR visual indicator. Subclasses need not keep track of
 *  the XOR state of the indicator -- that logic is all handled by this class.
 *  Even if the LayerViewPanel is repainted while the XOR indicator is on-screen.
 */
public abstract class AbstractCursorTool implements CursorTool {
    private boolean snappingConfigured = false;
    private boolean configuringSnapping = false;
    private boolean controlPressed;   
    private boolean shiftPressed;
    private Color color = Color.red;
    private boolean filling = false;
    private Shape lastShapeDrawn;
    
    protected ToolboxDialog toolbox = null;
	protected JPanel addPanel = null;	
	protected Dimension initialSize = null;
	
    private LayerViewPanelListener layerViewPanelListener = new LayerViewPanelListener() {
            public void cursorPositionChanged(String x, String y) {
            }

            public void selectionChanged() {
            }

            public void fenceChanged() {
            }

            public void painted(Graphics graphics) {
                try {
                    //If panel is repainted, force a redraw of the shape. Examples of when the
                    //panel is repainted: (1) the user Alt-Tabs away from the app
                    //(2) the user fires an APPEARANCE_CHANGED LayerEvent. [Jon Aquino]
                    if (shapeOnScreen) {
                        setShapeOnScreen(false);
                        redrawShape((Graphics2D) graphics);
                    }
                } catch (Throwable t) {
                    panel.getContext().handleThrowable(t);
                }
            }
        };

    private Color originalColor;
    private Stroke originalStroke;
    private LayerViewPanel panel;
    private boolean shapeOnScreen = false;
    private SnapManager snapManager = new SnapManager();
    private Stroke stroke = new BasicStroke(1);
    private ArrayList listeners = new ArrayList();

    public AbstractCursorTool() {
    }

    /**
     * Makes this CursorTool obey the snapping settings in the Options dialog.
     */
    public void allowSnapping() {
        configuringSnapping = true;
    }

    protected boolean wasShiftPressed() {
        return shiftPressed;
    }

    protected boolean wasControlPressed() {
        return controlPressed;
    }

    /**
     * The cursor will look best if the image is a 32 x 32 transparent GIF.
     */
    public static Cursor createCursor(Image image) {
        //<<TODO>> Compute image center rather than hardcoding 16, 16. [Jon Aquino]
        return createCursor(image, new Point(16, 16));
    }

    public static Cursor createCursor(Image image, Point hotSpot) {
        if (null == image) {
            return Cursor.getDefaultCursor();
        }

        if (Toolkit.getDefaultToolkit().getBestCursorSize(32, 32).equals(new Dimension(
                        0, 0))) {
            return Cursor.getDefaultCursor();
        }

        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot,
            "JCS Workbench Custom Cursor");
    }

    public Cursor getCursor() {
        return Cursor.getDefaultCursor();
    }

    /**
     *  Used by OrCompositeTool to determine whether a CursorTool is busy
     *  interacting with the user.
     */
    public boolean isGestureInProgress() {
        //For most CursorTools, the presence of the shape on the screen indicates
        //that the user is making a gesture. An exception, however, is
        //SnapIndicatorTool -- it provides its own implementation of this method.
        //[Jon Aquino]
        return isShapeOnScreen();
    }

    public boolean isRightMouseButtonUsed() {
        return false;
    }

    /**
     *  Important for XOR drawing. Even if #getShape returns null, this method
     *  will return true between calls of #redrawShape and #clearShape.
     */
    public boolean isShapeOnScreen() {
        return shapeOnScreen;
    }

    public void activate(ILayerViewPanel layerViewPanel) {
        this.panel = (LayerViewPanel)layerViewPanel;
        if (workbenchFrame(this.panel ) != null) {
            workbenchFrame(this.panel ).log("Activating " + getName());
        }

        if (this.panel != null) {
            this.panel.removeListener(layerViewPanelListener);
        }

        this.panel.addListener(layerViewPanelListener);

        if (configuringSnapping && !snappingConfigured) {
            //Must wait until now because #getWorkbench needs the panel to be set. [Jon Aquino]
            getSnapManager().addPolicies(createStandardSnappingPolicies(
                    getIWorkbench().getBlackboard()));
            snappingConfigured = true;
        }
    }

    public static WorkbenchFrame workbenchFrame(LayerViewPanel layerViewPanel) {
        Window window = SwingUtilities.windowForComponent(layerViewPanel);

        //Will not be a WorkbenchFrame in apps that don't use the workbench
        //e.g. LayerViewPanelDemoFrame. [Jon Aquino]
        return (window instanceof WorkbenchFrame) ? (WorkbenchFrame) window : null;
    }

    public static List createStandardSnappingPolicies(Blackboard blackboard) {
        return Arrays.asList(new SnapPolicy[] {
                new SnapToVerticesPolicy(blackboard),
                new SnapToFeaturesPolicy(blackboard),
                new SnapToGridPolicy(blackboard)
            });
    }

    protected boolean isRollingBackInvalidEdits() {
        return getIWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY,
            false);
    }

    public void deactivate() {
        cancelGesture();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        controlPressed = e.isControlDown();
        shiftPressed = e.isShiftDown();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void setColor(Color color) {
        this.color = color;
    }

    protected void setFilling(boolean filling) {
        this.filling = filling;
    }

    /**
     * @deprecated Use #setStroke instead.
     */
    protected void setStrokeWidth(int strokeWidth) {
        setStroke(new BasicStroke(strokeWidth));
    }

    protected void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    protected void setup(Graphics2D graphics) {
        originalColor = graphics.getColor();
        originalStroke = graphics.getStroke();
        graphics.setColor(color);
        graphics.setXORMode(Color.white);
        graphics.setStroke(stroke);
    }

    protected LayerViewPanel getPanel() {
        return panel;
    }

    /**
     *@return                null if nothing should be drawn
     */
    protected abstract Shape getShape() throws Exception;

    protected void cleanup(Graphics2D graphics) {
        graphics.setPaintMode();
        graphics.setColor(originalColor);
        graphics.setStroke(originalStroke);
    }

    protected void clearShape() {
        clearShape(getGraphics2D());
    }

    private Graphics2D getGraphics2D() {
        Graphics2D g = (Graphics2D) panel.getGraphics();

        if (g != null) {
            //Not sure why g is null sometimes [Jon Aquino]
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        }

        return g;
    }

    public void cancelGesture() {
        clearShape();
    }

    protected void drawShapeXOR(Graphics2D g) throws Exception {
        Shape newShape = getShape();
        drawShapeXOR(newShape, g);
        getPanel().firePropertyChange("ShapeOnScreen", null, newShape);
        lastShapeDrawn = newShape;
    }

    protected void drawShapeXOR(Shape shape, Graphics2D graphics) {
        setup(graphics);

        try {
            //Pan tool returns a null shape. [Jon Aquino]
            if (shape != null) {
                //Can't both draw and fill, because we're using XOR. [Jon Aquino]
                if (filling) {
                    graphics.fill(shape);
                } else {
                    graphics.draw(shape);
                }
            }
        } finally {
            cleanup(graphics);
        }
    }

    protected void redrawShape() throws Exception {
        redrawShape(getGraphics2D());
    }

    protected Coordinate snap(Point2D viewPoint)
        throws NoninvertibleTransformException {
        return snap(getPanel().getViewport().toModelCoordinate(viewPoint));
    }

    protected Coordinate snap(Coordinate modelCoordinate) {
        return snapManager.snap(getPanel(), modelCoordinate);
    }

    private void setShapeOnScreen(boolean shapeOnScreen) {
        this.shapeOnScreen = shapeOnScreen;
    }

    private void clearShape(Graphics2D graphics) {
        if (!shapeOnScreen) {
            return;
        }

        drawShapeXOR(lastShapeDrawn, graphics);
        setShapeOnScreen(false);
    	getPanel().firePropertyChange("ShapeOnScreen", lastShapeDrawn, null);
    }

    private void redrawShape(Graphics2D graphics) throws Exception {
        clearShape(graphics);
        drawShapeXOR(graphics);

        //<<TODO:INVESTIGATE>> Race conditions on the shapeOnScreen field?
        //Might we need synchronization? [Jon Aquino]
        setShapeOnScreen(true);
    }

    /**
     * @return null if the LayerViewPanel is not inside a TaskFrame
     */
    protected TaskComponent getTaskFrame() {
        return (TaskComponent) SwingUtilities.getAncestorOfClass(TaskComponent.class,
           (JComponent) getPanel());
    }

    public WorkBench getIWorkbench() {
        return iWorkbench(getPanel());
    }

    public static WorkBench iWorkbench(LayerViewPanel panel) {
    	WorkBench workBench = null;
    	WorkbenchGuiComponent workbenchGuiComponent = ((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
            panel));
        if (workbenchGuiComponent != null)
        	workBench = workbenchGuiComponent.getContext().getIWorkbench();
        else
        	workBench = ((WorkbenchGuiComponent)panel.getContext()).getContext().getIWorkbench();
        return workBench;         
    }
    /**  
     * For compatibility with JUMP ans its plugins
     * @return
     */
    public JUMPWorkbench getWorkbench() {
	return workbench(getPanel());
}   

public static JUMPWorkbench workbench(LayerViewPanel panel) {
	return ((WorkbenchFrame) SwingUtilities.getAncestorOfClass(
			WorkbenchFrame.class, panel)).getContext().getWorkbench();
}

    protected abstract void gestureFinished() throws Exception;

    protected void fireGestureFinished() throws Exception {
        getPanel().getContext().setStatusMessage("");

       /* if (getTaskFrame() != null) {
            ((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                (JComponent) getTaskFrame())).log("Gesture Finished: " + getName());
        }*/

        getPanel().getLayerManager().getUndoableEditReceiver().startReceiving();

        try {
            gestureFinished();
        } finally {
            getPanel().getLayerManager().getUndoableEditReceiver()
                .stopReceiving();
        }

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener listener = (Listener) i.next();
            listener.gestureFinished();
        }
    }

    public void add(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Optional means of execution, with undoability.
     */
    protected void execute(UndoableCommand command) {
        AbstractPlugIn.execute(command, getPanel());
    }

    /**
     * Notifies the UndoManager that this PlugIn did not modify any model states,
     * and therefore the undo history should remain unchanged. Call this method
     * inside #execute(PlugInContext).
     */
    protected void reportNothingToUndoYet() {
        getPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

    public String toString() {
        return getName();
    }

    public String getName() {
        return name(this);
    }

    public static String name(CursorTool tool) {
        return StringUtil.toFriendlyName(tool.getClass().getName(), "Tool");
    }

    protected boolean check(EnableCheck check) {
       String warning = check.check(null);

        if (warning != null) {
            getPanel().getContext().warnUser(warning);

            return false;
        }

        return true;
    }

    public SnapManager getSnapManager() {
        return snapManager;
    }

    public Color getColor() {
        return color;
    }

    public static interface Listener {
        public void gestureFinished();
    }
    
    public ToolboxDialog getPanelToolBox(){
    	
    	AppContext appContext=(AppContext) AppContext.getApplicationContext();
	    Blackboard blackboard = appContext.getBlackboard();	    
	    PlugInManager pluginManager = getIWorkbench().getPlugInManager();
	    
	    RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
	    GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
	    
	    Collection collectionToolBox = registerPlugInManager.getPlugInOfClass(pluginManager, geopistaEditingPlugIn.getClass());
	    geopistaEditingPlugIn = (GeopistaEditingPlugIn) collectionToolBox.iterator().next();
	    	
    	return  geopistaEditingPlugIn.getToolbox(getIWorkbench().getContext());
    }  
    
    public Component getComponentPanel(JPanel panel,String nameComponent){    	
        Component[] componentsPanel = panel.getComponents();        
        for (int i = 0;i<componentsPanel.length; i++) {
        	
        	if(componentsPanel[i].getName()==nameComponent){
        		return componentsPanel[i];
        	}        	
        }    	
    	return null;
    }
    
    public JPanel getAddPanel(ToolboxDialog toolbox, JPanel addPanel){    	    
    	if(toolbox!=null){
	        Component[] components = toolbox.getContentPane().getComponents();
	        for (int i = 0;i<components.length; i++) {
	        	
	        	if(components[i].equals(addPanel)){
	        		return (JPanel)components[i];
	        	}
	        }        
    	}
    	return null;
    }

	public void removePanel(ToolboxDialog toolbox, JPanel addPanel, Dimension initialSize){
		
		int index=-1;
        Component[] components = toolbox.getContentPane().getComponents();
        for (int i = 0;i<components.length; i++) {
        	
        	if(components[i].equals(addPanel)){
        		index = i;
        	}
        }
        if(index!=-1){
        	toolbox.getContentPane().remove(index);
        	toolbox.setSize(initialSize);        	
        }
        
	}
	
	protected boolean checkExactlyNItemsMustBeSelected(int n,String languageTool){
  	   
  	   if(getPanel().getSelectionManager().getSelectedItems().size()!=n){
  			getPanel().getContext().warnUser(I18N.get(languageTool,"Exactly")+ n +I18N.get(languageTool,"NItemsMustBeSelected"));
  			return false;
  		}
  	   return true;
     }
	
	protected boolean checkItemsSelectedIsNotEmpty(String languageTool){
		   
		   if(((Geometry)getPanel().getSelectionManager().getSelectedItems().iterator().next()).isEmpty()){
				getPanel().getContext().warnUser(I18N.get(languageTool,"TheItemSelectedIsEmpty"));
				return false;
			}
		   return true;
	   }
	   
	   protected boolean checkLayersSelectedIsEditable(String language){
		   for (Iterator i =
	      	    getPanel()
	      	    .getSelectionManager()
	      	    .getLayersWithSelectedItems()
	      	    .iterator();
		   	i.hasNext();
		   ) {
			   Layer layer = (Layer) i.next();

		      if (!layer.isEditable()) {
		   	   getPanel().getContext().warnUser(I18N.get(language,"SelectedItemsLayersMustBeEditable") + " (" + layer.getName() + ")");
		   	   return false;
		      }
		   }
		   return true;
	   }
	   
	   protected boolean checkAtLeastNItemsMustBeSelected(int n,String languageTool){
	  	   
	  	   if(getPanel().getSelectionManager().getSelectedItems().size()<n){
	  			getPanel().getContext().warnUser(I18N.get(languageTool,"AtLeast")+ n +I18N.get(languageTool,"NItemsMustBeSelected"));
	  			return false;
	  		}
	  	   return true;
	     }
    
}
