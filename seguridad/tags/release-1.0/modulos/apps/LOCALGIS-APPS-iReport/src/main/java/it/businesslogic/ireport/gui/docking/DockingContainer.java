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
 * DockingContainer.java
 * 
 * Created on January 25, 2006, 5:22 PM
 *
 */

package it.businesslogic.ireport.gui.docking;

import it.businesslogic.ireport.gui.event.TabPaneChangedEvent;
import it.businesslogic.ireport.gui.event.TabPaneChangedListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class DockingContainer extends javax.swing.JPanel implements TabPaneChangedListener, ActionListener {
    
    public static final int INSERT_MODE_SHAREDPOSTION = 0;
    public static final int INSERT_MODE_NEWPOSITION = 1;
    
    private int oldWidth = 0;
    private boolean compressed = false;
    
    private Vector panelViews = new Vector();
    private Vector tabPanes = new Vector();
    private int positions = 0;
    
    private DropTarget dropTarget;
    private DropTargetListener dtListener;
  
    public static final int POSITION_RIGHT = 0;
    public static final int POSITION_LEFT = 1;
    
    private int position = POSITION_LEFT;
    
    private int preferredDividerLocation = -1;
    private int realCurrentSize = 0;
    
    /** Creates new form DockingContainer */
    public DockingContainer() {
        this(POSITION_LEFT);
    }
    
    
    public DockingContainer(int position) {
        initComponents();
        
        this.setSize(new Dimension(200,100));
        this.setPreferredSize(new Dimension(200,100));
        
        
        this.dtListener = new DTListener();
        // component, ops, listener, accepting
       
        this.setDropTarget( new DropTarget(this, 
            DnDConstants.ACTION_MOVE,
            new DTListener())); 
       this.setBorder(null);
       applyI18n();
       
    }
       
    
    private JButton createButton(PanelView pv)
    {
        
        JButton jButton = new JButton();
        
        Icon icon = new VTextIcon(jButton1,pv.getName(),  (getPosition() == POSITION_LEFT) ? VTextIcon.ROTATE_LEFT : VTextIcon.ROTATE_RIGHT);
        jButton.setIcon( icon );
        jButton.setPreferredSize( new Dimension(20, icon.getIconHeight()+ 28));
        jButton.setMinimumSize( new Dimension(20, icon.getIconHeight()+ 28));
        
        jButton.setActionCommand( pv.getId()+"" );
        jButton.addActionListener( this );
        
       return jButton;
    }
    
    public void actionPerformed(ActionEvent e) {
    
        // Button pressed...
        String s= e.getActionCommand();
        try {
        PanelView pv = getPanelViewById( Integer.parseInt( s ));
        
        pv.setMinimized(false);
        pv.setPosition( positions);
        
        recreateAll();
        } catch (Exception ex) {
        
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Add a component under the other
     *
     */
    public void addPanel(String name, Component component, boolean closable)
    {
        insertPanel(positions, name, component, closable);
        
    }
    /*
    public void addDropTarget(Component comp)
    {
        System.out.println( comp );
        if (comp.getDropTarget() == null)
        {
            comp.setDropTarget( this.getDropTarget() );
        }
        
        if (comp instanceof JComponent)
        {
            JComponent component = (JComponent)comp;
            if (component.getDropTarget() == null)
            {
                component.setDropTarget( this.getDropTarget() );
            }

            for (int i=0; i<component.getComponentCount(); ++i)
            {
                addDropTarget( component.getComponent(i) );
            }
        }
    }
     */
    
    public boolean contains(Component c)
    {
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.getComponent() == c)
            {
                return  true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the panel view containing the Component c.
     * Null if the component is not found.
     *
     */
    public PanelView getPanelView(Component c)
    {
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.getComponent() == c)
            {
                return  pv;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the panel view containing the Component c.
     * Null if the component is not found.
     *
     */
    public PanelView getPanelViewById(int id)
    {
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.getId() == id)
            {
                return  pv;
            }
        }
        
        return null;
    }
    
    public void insertPanel(int position, String name, Component component, int insertMode, boolean closable)
    {
        if (component == null) return;
        if (name == null) name = "Panel";
        
        // Check if the component is already present somewhere...
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.getComponent() == component)
            {
                return;
            }
        }
        
        //addDropTarget( component );
        
        //if (component.getDropTarget() == null)
        //{
        //    component.setDropTarget(new DropTarget(component, new GenericDragTargetListener()));
        //}
        
        //System.out.println("Added panel " + name + " at position " + position);        
        if (position >= positions)
        {
            position = positions;
            positions++;
        }
        else if (insertMode == INSERT_MODE_NEWPOSITION)
        {
            for (int i=0; i<getPanelViews().size(); ++i)
            {
                PanelView pv = (PanelView)getPanelViews().elementAt(i);

                if (pv.getPosition() >= position)
                {
                    pv.setPosition( pv.getPosition()+1 );
                }
            }
            positions++;
        }

        panelViews.add( new PanelView(name, component, position, closable) );
        recreateAll();
        
    }
    /**
     * Add a component at position
     *
     */
    public void insertPanel(int position, String name, Component component, boolean closable)
    {
        insertPanel(position, name, component, INSERT_MODE_NEWPOSITION, closable);
    }
    
    /**
     * Add a component at position
     *
     */
    public void removePanel(Component component)
    {
        
        int position = -1;
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            
            if (pv.getComponent() == component)
            {
                position = pv.getPosition();
                //System.out.println("removing panel " + pv.getName() + " at position " + pv.getPosition());
                getPanelViews().remove(pv);
                break;
            }
        }
        
        
        
        // If "position" is not used, normalize all...
        if (position >= 0)
        {
            boolean found = false;
            for (int i=0; i<getPanelViews().size(); ++i)
            {
                PanelView pv = (PanelView)getPanelViews().elementAt(i);
                if (pv.isMinimized()) continue;
                if (pv.getPosition() == position)
                {
                    found = true;
                    break;
                }
            }
            
            if (!found)
            {
                for (int i=0; i<getPanelViews().size(); ++i)
                {
                    PanelView pv = (PanelView)getPanelViews().elementAt(i);
                    if (pv.isMinimized()) continue;
                    if (pv.getPosition() > position)
                    {
                        pv.setPosition( pv.getPosition()-1);
                    }
                }
                positions--;
            }
            recreateAll();
        }
        this.updateUI();
    }
    
    
    private void normalizePositions()
    {
        // 1. Find the number of different positions....
        HashMap map = new HashMap();
        int differentPositions = 0;
        
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.isMinimized()) continue;
            map.put(new Integer(pv.getPosition()),"");
        }
        
        differentPositions = map.size();
        
        for (int k=0; k<differentPositions; ++k)
        {
            
            while (getPanelCount(k) == 0)
            {
                // position = position -1 for all panels with position >= k
                downPosition(k);
            }
        }
        
        this.positions = differentPositions;
    }
    
    /*  if position > minimumPosition -> position = position - 1;
     * 
     */
    private void downPosition(int minimumPosition)
    {
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.isMinimized()) continue;
            if (pv.getPosition() > minimumPosition)
            {
                pv.setPosition( pv.getPosition()-1);
            }
        }
    }
    
    
    /**
     * Use this panelView to describe the panel to move. Only component and name are used
     * to move the component in the newPosition
     * panelView.component is removed then inserted again in newPosition with label panelView.name
     */
    public void moveComponent(PanelView panelView, int newPosition)
    {
        moveComponent(panelView, newPosition, INSERT_MODE_NEWPOSITION);
    }
    
    /**
     * Use this panelView to describe the panel to move. Only component and name are used
     * to move the component in the newPosition
     * panelView.component is removed then inserted again in newPosition with label panelView.name
     */
    public void moveComponent(PanelView panelView, int newPosition, int mode)
    {
        removePanel(panelView.getComponent());
        insertPanel(newPosition,panelView.getName(), panelView.getComponent(), mode, panelView.isClosable());
    }
    
    /**
     * Use this panelView to describe the panel to move. Only component and name are used
     * to move the component in the newPosition
     * panelView.component is removed then inserted again in newPosition with label panelView.name
     */
    public void mergePosition(int newPosition)
    {
        if (positions == 1) return;
        if (newPosition >= positions) return;
        if ( newPosition == 0 && positions>1) newPosition=1;
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            
            if (pv.isMinimized()) continue;
            
            if (pv.getPosition() >= newPosition)
            {
                pv.setPosition( pv.getPosition()-1);
            }
            
        }
        
        positions--;
        recreateAll();
    }
    
    
    
    public void recreateAll()
    {
        this.removeAll();
        
        
        jPanel1.removeAll();
        
        int numPositions = positions;
        
        normalizePositions();
                        
        if (positions > 0)
        {
            if (compressed)
            {
                compressed = false;
                this.setSize(getOldWidth(), 1);
            }
            
            
            tabPanes.removeAllElements();
            Vector tabbedPanes = new Vector();
            if (positions == 0) return;
        
            add( addComponents(0),java.awt.BorderLayout.CENTER);
            this.updateUI();
        }
        else
        {
            compressed = true;
            setOldWidth( getSize().width);
            this.setSize(0, 1);
        }
        
        int minimizedButton = 0;
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.isMinimized())
            {
                JButton b = createButton(pv);
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 0;
                jPanel1.add(b,gridBagConstraints);
                minimizedButton++;
            }
        }
        
        if (minimizedButton > 0)
        {
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            jPanel1.add(new JPanel(), gridBagConstraints);
            add( jPanel1,  (getPosition() == POSITION_LEFT) ? java.awt.BorderLayout.WEST : java.awt.BorderLayout.EAST);
        }
        
        if (this.getParent() != null &&
            this.getParent() instanceof JSplitPane)
        {
            JSplitPane sp = (JSplitPane)this.getParent();           
            int currentWidth = realCurrentSize;
            if (positions == 0)
            {
                
                //this.setPreferredSize(new Dimension(24,0));
                
                if (getPosition() == POSITION_LEFT)
                {
                    sp.setDividerLocation( jPanel1.getWidth() );
                }
                else
                {
                    
                    //sp.setDividerLocation(1.0);
                    this.setSize(new Dimension(24,0));
                    sp.setDividerLocation( (int)(sp.getBounds().getWidth())-sp.getDividerSize()- 24 );
                }
            }
            else
            {
                int width = currentWidth;
                if (width <= 150 + 24)
                {
                    width = 150+ 24;
                }
                //this.setPreferredSize(new Dimension(width,0));
                //this.setSize(new Dimension(width,0));
                //this.setMinimumSize(new Dimension(width,0));
                if (getPosition() == POSITION_LEFT)
                {
                    sp.setDividerLocation( width );
                }
                else
                {
                    sp.setDividerLocation( getPreferredDividerLocation() - ( width - currentWidth));
                    
                    
                    //sp.setDividerLocation();
                    //sp.setDividerLocation( sp.getWidth() - width );
                }
            }

            sp.updateUI();

        }
        this.updateUI();
    }
    
    public void setSelectedComponent(Component component)
    {
        for (int i=0; i<tabPanes.size(); ++i)
        {
            JDraggableTabbedPane tab = (JDraggableTabbedPane)tabPanes.get(i);
                        
            try {
               tab.setSelectedComponent( component );
               break;
            } catch (Exception ex) {}
        }
    }
    
    public Component addComponents(int pos)
    {
        
        JDraggableTabbedPane jTabbedPane = new JDraggableTabbedPane();
        jTabbedPane.setPosition(pos);
        jTabbedPane.setOrientation( getPosition());
        jTabbedPane.setDockingContainer(this);
        jTabbedPane.addTabPaneChangedListener( this );
        
        tabPanes.add(jTabbedPane);
        
                
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.isMinimized()) continue;
            if (pv.getPosition() == pos)
            {
                jTabbedPane.addTab(pv.getName(), pv.getComponent(), pv.isClosable());
            }
            
            //System.out.println("Adding " + pv.getName() + " on position " + pos + "/" + positions);
        }
        
        if (pos < positions-1)
        {
            javax.swing.JSplitPane jSplitPane = new javax.swing.JSplitPane();
            jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            jSplitPane.setBorder(null);
            //jSplitPane.setDividerSize(6);
            
            jSplitPane.setResizeWeight( 1.0/(positions-pos));
            jSplitPane.setLeftComponent(jTabbedPane);
            jSplitPane.setRightComponent( addComponents(pos+1) );
            jSplitPane.setDividerSize(5);
            return jSplitPane;
        }
        else
        {
            return jTabbedPane;
        }
    }
    
    
    public Vector getPanelViews() {
        return panelViews;
    }
    
    public int getPanelCount(int position)
    {
        int p_number = 0;
        for (int i=0; i<getPanelViews().size(); ++i)
        {
            PanelView pv = (PanelView)getPanelViews().elementAt(i);
            if (pv.isMinimized()) continue;
            if (pv.getPosition() == position)
            {
                p_number++;
            }
        }
        
        return p_number;
    }

    public void setPanelViews(Vector panelViews) {
        this.panelViews = panelViews;
    }

    public int getOldWidth() {
        return oldWidth;
    }

    public void setOldWidth(int oldWidth) {
        this.oldWidth = oldWidth;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(24, 10));
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setMaximumSize(new java.awt.Dimension(33, 500));
        jButton1.setMinimumSize(new java.awt.Dimension(33, 100));
        jButton1.setPreferredSize(new java.awt.Dimension(33, 100));
        jButton1.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jButton1, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.WEST);

    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        realCurrentSize = (int)evt.getComponent().getSize().getWidth();
        //System.out.println("Current size: " + );
    }//GEN-LAST:event_formComponentResized

    public void tabPaneChanged(TabPaneChangedEvent evt) {
        
        if (evt.getOperation() == evt.CLOSED)
        {
            this.removePanel( evt.getTabComponent() );
            
        }
        else if (evt.getOperation() == evt.MINIMIZED)
        {
            // Look for the right panel view..
            PanelView pv = getPanelView(evt.getTabComponent());
            pv.setMinimized(true);
            recreateAll();
        }
        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
 
class DTListener implements DropTargetListener {



    /**
     * start "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragEnter(DropTargetDragEvent e) {
      e.acceptDrag(e.getDropAction());        
    }

    /**
     * continue "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragOver(DropTargetDragEvent e) {
      e.acceptDrag(e.getDropAction());    
    }
    
    public void dropActionChanged(DropTargetDragEvent e) {
    }
    
    public void dragExit(DropTargetEvent e) {
    }

    /**
     * perform action from getSourceActions on
     * the transferrable
     * invoke acceptDrop or rejectDrop
     * invoke dropComplete
     * if its a local (same JVM) transfer, use StringTransferable.localStringFlavor
     * find a match for the flavor
     * check the operation
     * get the transferable according to the chosen flavor
     * do the transfer
     */
    public void drop(DropTargetDropEvent dtde) {
        try {
           
            DropTargetContext context = dtde.getDropTargetContext();

            Transferable tr = dtde.getTransferable();

            DataFlavor[] df = tr.getTransferDataFlavors();

            if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.gui.docking.PanelView"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.gui.docking.PanelView.class, it.businesslogic.ireport.gui.docking.PanelView.class.getName());
                it.businesslogic.ireport.gui.docking.PanelView panelView = (it.businesslogic.ireport.gui.docking.PanelView)tr.getTransferData( myFlavor );
                
                if (DockingContainer.this != null)
                {
                    if (panelView.getDockingContainer() != DockingContainer.this)
                    {
                        panelView.getDockingContainer().removePanel(panelView.getComponent());
                        DockingContainer.this.insertPanel(0, panelView.getName(), panelView.getComponent(), panelView.isClosable());
                    }
                }
            }
           
            context.dropComplete(true);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


  }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPreferredDividerLocation() {
        return preferredDividerLocation;
    }

    public void setPreferredDividerLocation(int preferredDividerLocation) {
        this.preferredDividerLocation = preferredDividerLocation;
    }

    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
    	
    }
}
