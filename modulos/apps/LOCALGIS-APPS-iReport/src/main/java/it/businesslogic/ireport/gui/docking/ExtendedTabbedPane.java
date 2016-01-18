/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ExtendedTabbedPane.java
 * 
 * Created on March 18, 2006, 8:17 PM
 *
 */

package it.businesslogic.ireport.gui.docking;

import it.businesslogic.ireport.gui.event.TabPaneChangedEvent;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

// #21380.
/**
 * Copy of original ExtendedTabbedPane from the NetBeans 3.4 winsys.  Old code never dies.
 *
 * @author Tran Duc Trung
 *
 */
class ExtendedTabbedPane extends JTabbedPane implements ChangeListener, Runnable {

    private final Image closeTabImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabclose.png"))).getImage() ; 
    private final Image closeTabInactiveImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabcloseinactive.png"))).getImage() ; 
    private final Image closeTabActiveImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabcloseactive.png"))).getImage() ; 
    
    private final Image minimizeTabImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabminimize.png"))).getImage() ; 
    private final Image minimizeTabInactiveImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabminimizeinactive.png"))).getImage() ; 
    private final Image minimizeTabActiveImage = (new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/docking/tabminimizeactive.png"))).getImage() ; 

    // List of index of tabs without the close button...
    private java.util.List disabledCloseButtons = new java.util.ArrayList();
    
    private int orientation = DockingContainer.POSITION_LEFT;

    ExtendedTabbedPane() {
        addChangeListener(this);
        
        CloseButtonListener.install();
        MinimizeButtonListener.install();
        
        //Bugfix #28263: Disable focus.
        setFocusable(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder());
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new CBTPPolicy());
    }

    private Component sel() {
        Component c = getSelectedComponent();
        return c == null ? this : c;
    }

    private class CBTPPolicy extends FocusTraversalPolicy {
        public Component getComponentAfter(Container aContainer, Component aComponent) {
            return sel();
        }

        public Component getComponentBefore(Container aContainer, Component aComponent) {
            return sel();
        }

        public Component getFirstComponent(Container aContainer) {
            return sel();
        }

        public Component getLastComponent(Container aContainer) {
            return sel();
        }

        public Component getDefaultComponent(Container aContainer) {
            return sel();
        }
    }

    public int tabForCoordinate(int x, int y) {
        return getUI().tabForCoordinate(this, x, y);
    }

    private int pressedCloseButtonIndex = -1;
    private int mouseOverCloseButtonIndex = -1;
    
    
    private int pressedMinimizeButtonIndex = -1;
    private int mouseOverMinimizeButtonIndex = -1;
    
    private boolean draggedOut = false;
    private boolean draggedOutMinimize = false;
    
    public boolean isClosable(int tabIndex)
    {
        return !disabledCloseButtons.contains( new Integer(tabIndex));
    }
    
    public void stateChanged (ChangeEvent e) {
        reset();
        resetMinimized();
        //if (getSelectedComponent() instanceof AbstractOutputPane) {
        //    ((AbstractOutputPane) getSelectedComponent()).ensureCaretPosition();
        //}
    }
    
    public Component add (Component c) {
        Component result = super.add(c);
        String s = c.getName();
        if (s != null) {
            s += " ";
        }
        setTitleAt (getComponentCount() - 1, s);
        return result;
    }
    
    public Component addTab(String s, Component c, boolean closeable) {
        Component result = super.add(c);       
        if (!closeable)
        {
        	disabledCloseButtons.add( new Integer(getComponentCount() - 1));
        }
        setTitleAt (getComponentCount() - 1, s);

        return result;
    }

    public void setTitleAt(int idx, String title) {
        
        String s = title;
        if (s == null) s = "";
        s = s.trim()+"   ";
        if (s != null)
        {
            s += "  ";
        }
        
        if (isClosable(idx)) s += "   ";
        
        
        if (!s.equals(getTitleAt(idx))) {
            super.setTitleAt(idx, s);
        }
    }

    private void reset() {
        setMouseOverCloseButtonIndex(-1);
        setPressedCloseButtonIndex(-1);
        
        draggedOut = false;
    }
    
    private void resetMinimized() {
        
        setMouseOverMinimizeButtonIndex(-1);
        setPressedMinimizeButtonIndex(-1);
        draggedOutMinimize = false;
    }

    private Rectangle getCloseButtonBoundsAt(int i) {
        Rectangle b = getBoundsAt(i);
        if (b == null || disabledCloseButtons.contains(new Integer(i)))
            return null;
        else {
            b = new Rectangle(b);
            fixGetBoundsAt(b);

            Dimension tabsz = getSize();
            if (b.x + b.width >= tabsz.width
                || b.y + b.height >= tabsz.height)
                return null;

            return new Rectangle(b.x + b.width - 14,
                                 b.y + b.height / 2 - 5,
                                 8,
                                 8);
        }
    }
    
    private Rectangle getMinimizeButtonBoundsAt(int i) {
        Rectangle b = getBoundsAt(i);
        if (b == null)
            return null;
        else {
            b = new Rectangle(b);
            fixGetBoundsAt(b);

            Dimension tabsz = getSize();
            if (b.x + b.width >= tabsz.width
                || b.y + b.height >= tabsz.height)
                return null;

            return new Rectangle(b.x + b.width - ( (disabledCloseButtons.contains(new Integer(i))) ? 12 : 24),
                                 b.y + b.height / 2 - 5,
                                 8,
                                 8);
        }
    }


    /** Checks whether current L&F sets used keys for colors.
     * If not puts default values. */
    private static void checkUIColors() {
        if(UIManager.getColor("Button.shadow") == null) { 
            UIManager.put("Button.shadow", 
                new ColorUIResource(153, 153, 153));
        }
        if(UIManager.getColor("Button.darkShadow") == null) { 
            UIManager.put("Button.darkShadow", 
                new ColorUIResource(102, 102, 102));
        }
        if(UIManager.getColor("Button.highlight") == null) { 
            UIManager.put("Button.highlight", 
                new ColorUIResource(Color.white));
        }
        if(UIManager.getColor("Button.background") == null) { 
            UIManager.put("Button.background", 
                new ColorUIResource(204, 204, 204));
        }
    }
    
    public void paint(Graphics g) {
        super.paint(g);

        // #29181 All L&F doesn't support the colors used.
        checkUIColors();

        // Have a look at
        // http://ui.netbeans.org/docs/ui/closeButton/closeButtonUISpec.html
        // to see how the buttons are specified to be drawn.

        int selectedIndex = getSelectedIndex();
        for (int i = 0, n = getTabCount(); i < n; i++) {
        	
            drawButton(  g,
            		 getMinimizeButtonBoundsAt(i), 
            		 pressedMinimizeButtonIndex, 
            		 minimizeTabInactiveImage,
            		 minimizeTabImage,
            		 minimizeTabActiveImage,
            		 selectedIndex == i,
            		 mouseOverMinimizeButtonIndex,
            		 i, draggedOutMinimize, getOrientation() == 0);
            
            if (!disabledCloseButtons.contains(new Integer(i)))
            	 {		 
            drawButton(  g,
            		 getCloseButtonBoundsAt(i), 
            		 pressedCloseButtonIndex, 
            		 closeTabInactiveImage,
            		 closeTabImage,
            		 closeTabActiveImage,
            		 selectedIndex == i,
            		 mouseOverCloseButtonIndex,
            		 i, draggedOut, false);
            }
        }
    }

	protected void drawButton(  Graphics g,
            		 Rectangle r, 
            		 int pressedButtonIndex, 
            		 Image tabInactiveImage,
            		 Image tabImage,
            		 Image tabActiveImage,
            		 boolean selected,
            		 int mouseOverButtonIndex,
            		 int tabIndex,
            		 boolean isDraggedOut,
                         boolean flipImage)
        {
            if (r == null) return;
            
            if(tabIndex == pressedButtonIndex && !isDraggedOut) {
                g.setColor(UIManager.getColor("Button.shadow")); //NOI18N
                g.fillRect(r.x , r.y, r.width, r.height);
            }
            
            // img coords 
            int dx1 = (flipImage) ? r.x+r.width : r.x;
            int dy1 = r.y;
            int dx2 = (flipImage) ? r.x : r.x+r.width;
            int dy2 = r.y+r.height;
            
            int sx1 = 0;
            int sy1 = 0;
            int sx2 = r.width;
            int sy2 = r.height;
            
            
            if (selected)
                g.drawImage(tabImage,dx1,dy1,dx2,dy2, sx1,sy1,sx2,sy2,this);
            else
                g.drawImage(tabInactiveImage, dx1,dy1,dx2,dy2, sx1,sy1,sx2,sy2, this);
            
            if (tabIndex == mouseOverButtonIndex
            || (tabIndex == pressedButtonIndex && isDraggedOut)) {
            	
            	g.drawImage(tabActiveImage, dx1,dy1,dx2,dy2, sx1,sy1,sx2,sy2, this);
            	
                //g.setColor(Color.BLACK); //NOI18N
                //g.drawLine(r.x-1, r.y+8, r.x + r.width+1, r.y+8);
                //g.setColor(selected
                //    ? UIManager.getColor("Button.highlight") //NOI18N
                //    : UIManager.getColor("Button.background")); //NOI18N
                //g.drawRect(r.x, r.y, r.width, r.height);
                
                // Draw the dots.
                //g.setColor (UIManager.getColor ("Button.highlight").brighter()); //NOI18N
                //g.drawLine(r.x + r.width, r.y + 1, r.x + r.width, r.y + 1);
                //g.drawLine(r.x + 1, r.y + r.height, r.x + 1, r.y + r.height);
            } else if (tabIndex == pressedButtonIndex) {
            	
            	g.drawImage(tabImage, dx1,dy1,dx2,dy2, sx1,sy1,sx2,sy2, this);
            	
            	/*
                g.setColor(UIManager.getColor("Button.shadow")); //NOI18N
                g.drawRect(r.x, r.y, r.width, r.height);
                g.setColor(selected
                    ? UIManager.getColor("Button.highlight") //NOI18N
                    : UIManager.getColor("Button.background")); //NOI18N
                g.drawLine(r.x + 1,
                           r.y + r.height + 1,
                           r.x + r.width + 1,
                           r.y + r.height + 1);
                g.drawLine(r.x + r.width + 1,
                           r.y + 1,
                           r.x + r.width + 1,
                           r.y + r.height + 1);
                
                // Draw the lines.
                g.setColor(UIManager.getColor("Button.background")); //NOI18N
                g.drawLine(r.x + 1, r.y + 1, r.x + r.width, r.y + 1);
                g.drawLine(r.x + 1, r.y + 1, r.x + 1, r.y + r.height);
                */
            }
        }
    private void setPressedCloseButtonIndex(int index) {
        if (pressedCloseButtonIndex == index)
            return;

        if (pressedCloseButtonIndex >= 0
        && pressedCloseButtonIndex < getTabCount()) {
            Rectangle r = getCloseButtonBoundsAt(pressedCloseButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);

            JComponent c = (JComponent)
                getComponentAt(pressedCloseButtonIndex);
            setToolTipTextAt(pressedCloseButtonIndex, c.getToolTipText());
        }

        pressedCloseButtonIndex = index;

        if (pressedCloseButtonIndex >= 0
        && pressedCloseButtonIndex < getTabCount()) {
            Rectangle r = getCloseButtonBoundsAt(pressedCloseButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            setMouseOverCloseButtonIndex(-1);
            setToolTipTextAt(pressedCloseButtonIndex, null);
        }
    }

    private void setMouseOverCloseButtonIndex(int index) {
        if (mouseOverCloseButtonIndex == index)
            return;

        if (mouseOverCloseButtonIndex >= 0
        && mouseOverCloseButtonIndex < getTabCount()) {
            Rectangle r = getCloseButtonBoundsAt(mouseOverCloseButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            JComponent c =  (JComponent)
                getComponentAt(mouseOverCloseButtonIndex);
            setToolTipTextAt(mouseOverCloseButtonIndex, c.getToolTipText());
        }

        mouseOverCloseButtonIndex = index;

        if (mouseOverCloseButtonIndex >= 0
        && mouseOverCloseButtonIndex < getTabCount()) {
            Rectangle r = getCloseButtonBoundsAt(mouseOverCloseButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            setPressedCloseButtonIndex(-1);
            setToolTipTextAt(mouseOverCloseButtonIndex, null);
        }
    }
    
        private void setPressedMinimizeButtonIndex(int index) {
        if (pressedMinimizeButtonIndex == index)
            return;

        if (pressedMinimizeButtonIndex >= 0
        && pressedMinimizeButtonIndex < getTabCount()) {
            Rectangle r = getMinimizeButtonBoundsAt(pressedMinimizeButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);

            JComponent c = (JComponent)
                getComponentAt(pressedMinimizeButtonIndex);
            setToolTipTextAt(pressedMinimizeButtonIndex, c.getToolTipText());
        }

        pressedMinimizeButtonIndex = index;

        if (pressedMinimizeButtonIndex >= 0
        && pressedMinimizeButtonIndex < getTabCount()) {
            Rectangle r = getMinimizeButtonBoundsAt(pressedMinimizeButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            setMouseOverMinimizeButtonIndex(-1);
            setToolTipTextAt(pressedMinimizeButtonIndex, null);
        }
    }
    
    private void setMouseOverMinimizeButtonIndex(int index) {
        if (mouseOverMinimizeButtonIndex == index)
            return;

        if (mouseOverMinimizeButtonIndex >= 0
        && mouseOverMinimizeButtonIndex < getTabCount()) {
            Rectangle r = getMinimizeButtonBoundsAt(mouseOverMinimizeButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            JComponent c =  (JComponent)
                getComponentAt(mouseOverMinimizeButtonIndex);
            setToolTipTextAt(mouseOverMinimizeButtonIndex, c.getToolTipText());
        }

        mouseOverMinimizeButtonIndex = index;

        if (mouseOverMinimizeButtonIndex >= 0
        && mouseOverMinimizeButtonIndex < getTabCount()) {
            Rectangle r = getMinimizeButtonBoundsAt(mouseOverMinimizeButtonIndex);
            repaint(r.x, r.y, r.width + 2, r.height + 2);
            setPressedMinimizeButtonIndex(-1);
            setToolTipTextAt(mouseOverMinimizeButtonIndex, null);
        }
    }

    private void fireCloseRequest(Component c) {
        //firePropertyChange("close", null, c);
        fireTabPaneChangedListenerTabPaneChanged(new TabPaneChangedEvent(TabPaneChangedEvent.CLOSED, c,  indexOfComponent(c)));
    }
    
    private void fireMinimizeRequest(Component c) {
        fireTabPaneChangedListenerTabPaneChanged(new TabPaneChangedEvent(TabPaneChangedEvent.MINIMIZED, c,  indexOfComponent(c)));
    }

    public static void fixGetBoundsAt(Rectangle b) {
        if (b.y < 0)
            b.y = -b.y;
        if (b.x < 0)
            b.x = -b.x;
    }

    public static int findTabForCoordinate(JTabbedPane tab, int x, int y) {
        for (int i = 0; i < tab.getTabCount(); i++) {
            Rectangle b = tab.getBoundsAt(i);
            if (b != null) {
                b = new Rectangle(b);
                fixGetBoundsAt(b);

                if (b.contains(x, y)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    boolean closingTab = false;
    boolean minimizingTab = false;
    public void doLayout() {
        //JDK 1.5, Win L&F - we cannot do the layout synchronously when we've
        //just removed a tab - the layout will have out of sync cache data
        if (closingTab || minimizingTab) {
            SwingUtilities.invokeLater (this);
        } else {
            super.doLayout();
        }
    }
    
    public void run() {
        doLayout();
        closingTab = false;
        minimizingTab = false;
        repaint();
    }

    protected void processMouseEvent (MouseEvent me) {
        try {
            super.processMouseEvent (me);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            //Bug in BasicTabbedPaneUI$Handler:  The focusIndex field is not
            //updated when tabs are removed programmatically, so it will try to
            //repaint a tab that's not there
            //ErrorManager.getDefault().annotate(aioobe, "Suppressed " + //NOI18N
            //        "AIOOBE bug in BasicTabbedPaneUI"); //NOI18N
            //ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, aioobe);
        }
    }


    private static class CloseButtonListener implements AWTEventListener
    {
        private static boolean installed = false;

        private CloseButtonListener() {}

        private static synchronized void install() {
            if (installed)
                return;

            installed = true;
            Toolkit.getDefaultToolkit().addAWTEventListener(
                new CloseButtonListener(),
                AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }

        public void eventDispatched (AWTEvent ev) {
            MouseEvent e = (MouseEvent) ev;

            Component c = (Component) e.getSource();
            while (c != null && !(c instanceof ExtendedTabbedPane))
                c = c.getParent();
            if (c == null)
                return;
            final ExtendedTabbedPane tab = (ExtendedTabbedPane) c;

            Point p = SwingUtilities.convertPoint((Component) e.getSource(),
                                                  e.getPoint(),
                                                  tab);

            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                //Not interested in clicked, and it can cause an NPE
                return;
            }
            
            int index = findTabForCoordinate(tab, p.x, p.y);

            Rectangle r = null;
            if (index >= 0)
                r = tab.getCloseButtonBoundsAt(index);
            if (r == null)
                r = new Rectangle(0,0,0,0);

            switch(e.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                    if (r.contains(p)) {
                    	tab.setPressedCloseButtonIndex(index);
                        tab.draggedOut = false;
                        e.consume();
                        return;
                    }
                    break;

                case MouseEvent.MOUSE_RELEASED:
                    if (r.contains(p) && tab.pressedCloseButtonIndex >= 0) {
                        tab.closingTab = true;
                        Component tc =
                            tab.getComponentAt(tab.pressedCloseButtonIndex);
                        tab.reset();

                        tab.fireCloseRequest(tc);
                        e.consume();
                        return;
                    }
                    else {
                        tab.reset();
                    }
                    break;

                case MouseEvent.MOUSE_ENTERED:
                    break;

                case MouseEvent.MOUSE_EXITED:
                    //tab.reset();

                    // XXX(-ttran) when the user clicks on the close button on
                    // an unfocused (internal) frame the focus is transferred
                    // to the frame and an unexpected MOUSE_EXITED event is
                    // fired.  If we call reset() at every MOUSE_EXITED event
                    // then when the mouse button is released the tab is not
                    // closed.  See bug #24450
                    
                    break;

                case MouseEvent.MOUSE_MOVED:
                    if (r.contains(p)) {
                        tab.setMouseOverCloseButtonIndex(index);
                        tab.draggedOut = false;
                        e.consume();
                        return;
                    }
                    else if (tab.mouseOverCloseButtonIndex >= 0) {
                        tab.setMouseOverCloseButtonIndex(-1);
                        tab.draggedOut = false;
                        e.consume();
                    }
                    break;

                case MouseEvent.MOUSE_DRAGGED:
                    if (tab.pressedCloseButtonIndex >= 0) {
                        if (tab.draggedOut != !r.contains(p)) {
                            tab.draggedOut = !r.contains(p);
                            tab.repaint(r.x, r.y, r.width + 2, r.height + 2);
                        }
                        e.consume();
                        return;
                    }
                    break;
            }
        }
    }
    
    
    private static class MinimizeButtonListener implements AWTEventListener
    {
        private static boolean installed = false;

        private MinimizeButtonListener() {}

        private static synchronized void install() {
            if (installed)
                return;

            installed = true;
            Toolkit.getDefaultToolkit().addAWTEventListener(
                new MinimizeButtonListener(),
                AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }

        public void eventDispatched (AWTEvent ev) {
            MouseEvent e = (MouseEvent) ev;

            Component c = (Component) e.getSource();
            while (c != null && !(c instanceof ExtendedTabbedPane))
                c = c.getParent();
            if (c == null)
                return;
            final ExtendedTabbedPane tab = (ExtendedTabbedPane) c;

            Point p = SwingUtilities.convertPoint((Component) e.getSource(),
                                                  e.getPoint(),
                                                  tab);

            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                //Not interested in clicked, and it can cause an NPE
                return;
            }
            
            int index = findTabForCoordinate(tab, p.x, p.y);

            Rectangle r = null;
            if (index >= 0)
                r = tab.getMinimizeButtonBoundsAt(index);
            if (r == null)
                r = new Rectangle(0,0,0,0);

            switch(e.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                
                    if (r.contains(p)) {
                    	tab.setPressedMinimizeButtonIndex(index);
                        tab.draggedOutMinimize = false;
                        e.consume();
                        return;
                    }
                    break;

                case MouseEvent.MOUSE_RELEASED:
                    
                    if (r.contains(p) && tab.pressedMinimizeButtonIndex >= 0) {
                        tab.minimizingTab = true;
                        Component tc =
                            tab.getComponentAt(tab.pressedMinimizeButtonIndex);
                        tab.resetMinimized();
                        tab.fireMinimizeRequest(tc);
                        e.consume();
                        return;
                    }
                    else {
                        tab.resetMinimized();
                    }
                    break;

                case MouseEvent.MOUSE_ENTERED:
                    break;

                case MouseEvent.MOUSE_EXITED:
                    //tab.reset();

                    // XXX(-ttran) when the user clicks on the close button on
                    // an unfocused (internal) frame the focus is transferred
                    // to the frame and an unexpected MOUSE_EXITED event is
                    // fired.  If we call reset() at every MOUSE_EXITED event
                    // then when the mouse button is released the tab is not
                    // closed.  See bug #24450
                    
                    break;

                case MouseEvent.MOUSE_MOVED:
                    if (r.contains(p)) {
                        tab.setMouseOverMinimizeButtonIndex(index);
                        tab.draggedOutMinimize = false;
                        e.consume();
                        return;
                    }
                    else if (tab.mouseOverMinimizeButtonIndex >= 0) {
                        tab.setMouseOverMinimizeButtonIndex(-1);
                        tab.draggedOutMinimize = false;
                        e.consume();
                    }
                    break;

                case MouseEvent.MOUSE_DRAGGED:
                    if (tab.pressedMinimizeButtonIndex >= 0) {
                        if (tab.draggedOutMinimize != !r.contains(p)) {
                            tab.draggedOutMinimize = !r.contains(p);
                            tab.repaint(r.x, r.y, r.width + 2, r.height + 2);
                        }
                        e.consume();
                        return;
                    }
                    break;
            }
        }
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    /**
     * Registers TabPaneChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addTabPaneChangedListener(it.businesslogic.ireport.gui.event.TabPaneChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.TabPaneChangedListener.class, listener);
    }

    /**
     * Removes TabPaneChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeTabPaneChangedListener(it.businesslogic.ireport.gui.event.TabPaneChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.TabPaneChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireTabPaneChangedListenerTabPaneChanged(it.businesslogic.ireport.gui.event.TabPaneChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.TabPaneChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.TabPaneChangedListener)listeners[i+1]).tabPaneChanged (event);
            }
        }
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}


