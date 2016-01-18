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
 * JMDIMenuBar.java
 * 
 * Created on 5 febbraio 2003, 22.57
 *
 */

package it.businesslogic.ireport.gui;

/**
 * Similar to a javax.swing.JMenuBar, this menu bar is able to handle close, minimize e AltMaximize buttons for
 * JMDIFrame contained in a JMDIDesktopPane.
 * @author  Administrator
 */
public class JMDIMenuBar extends javax.swing.JMenuBar implements java.awt.event.ComponentListener, java.awt.event.ActionListener {
    
    javax.swing.JButton closeButton = null;
    javax.swing.JButton resizeButton = null;
    javax.swing.JButton iconifyButton = null;
    JMDIFrame   activeFrame = null;
    boolean isMaximized = false;
    int closeCount=0;
        
    /** Creates a new instance of JMDIMenuBar */
    public JMDIMenuBar() {
        closeButton = new javax.swing.JButton(javax.swing.plaf.metal.MetalIconFactory.getInternalFrameCloseIcon(16));
        closeButton.setBorder( new javax.swing.border.EmptyBorder( new  java.awt.Insets( 0,0,0,0 ) ));
        resizeButton = new javax.swing.JButton(javax.swing.plaf.metal.MetalIconFactory.getInternalFrameAltMaximizeIcon(16));
        resizeButton.setBorder( new javax.swing.border.EmptyBorder( new  java.awt.Insets( 0,0,0,0 ) ));
        iconifyButton = new javax.swing.JButton(javax.swing.plaf.metal.MetalIconFactory.getInternalFrameMinimizeIcon(16) );
        iconifyButton.setBorder( new javax.swing.border.EmptyBorder( new  java.awt.Insets( 0,0,0,0 ) ));
 
        this.addComponentListener(this);
    }
       
       public void componentHidden(java.awt.event.ComponentEvent e) {}
       public void componentMoved(java.awt.event.ComponentEvent e) {}
       public  void componentResized(java.awt.event.ComponentEvent e) {
       if (this.getParent()!= null && closeButton != null) closeButton.setBounds(this.getParent().getWidth()-16-2,(this.getHeight()-16)/2,16,16); 
       if (this.getParent()!= null && closeButton != null) resizeButton.setBounds(this.getParent().getWidth()-32-6,(this.getHeight()-16)/2,16,16); 
       if (this.getParent()!= null && closeButton != null) iconifyButton.setBounds(this.getParent().getWidth()-48-6,(this.getHeight()-16)/2,16,16); 
    }
    
    public void removeFrameButtons()
    {
           isMaximized = false;
           activeFrame = null;
           closeButton.removeActionListener(this);
           resizeButton.removeActionListener(this);
           iconifyButton.removeActionListener(this);
           this.remove( closeButton);
           this.remove( resizeButton);
           this.remove( iconifyButton);
           //restoreButtonsPosition();
           this.update(this.getGraphics());
    }

    public void setMaximizedFrame(JMDIFrame jf)
    {
        if (jf == null)
        {
           removeFrameButtons();
        }
        else
        {
           
            if (!isMaximized)
            {
                resizeButton.addActionListener(this);
                iconifyButton.addActionListener(this);
                closeButton.addActionListener(this);
                this.add ( iconifyButton );
                this.add( resizeButton);
                this.add( closeButton);
                this.restoreButtonsPosition();
                this.update(this.getGraphics());
            }
            activeFrame = jf;
            isMaximized = true;
        }
    }
    
    public JMDIFrame getMaximizedFrame()
    {
        return this.activeFrame;
    }

    public void componentShown(java.awt.event.ComponentEvent e)  {}

    /** Paints this component.
     * <p>
     * This method is called when the contents of the component should
     * be painted in response to the component first being shown or
     * damage needing repair.  The clip rectangle in the
     * <code>Graphics</code> parameter will be set to the area
     * which needs to be painted.
     * Subclasses of Component that override this method need not call
     * super.paint(g).
     * <p>
     * For performance reasons, Components with zero width or height
     * aren't considered to need painting when they are first shown,
     * and also aren't considered to need repair.
     *
     * @param g the graphics context to use for painting
     * @see       #update
     * @since     JDK1.0
     *
     */
    public void paint(java.awt.Graphics g) {
        componentResized(null);
        super.paint(g);
    }
    
    /** Invoked when an action occurs.
     *    This method handle the minimize,alt-maximize and close buttons
     *    for frames maximized in JMDIDestopPane
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        this.activeFrame = MainFrame.getMainInstance().getActiveReportFrame();
        // The simpler case...
        closeCount++;
        if (e. getSource() == resizeButton)
        {
            if (this.activeFrame != null)
            {
                try {
                   activeFrame.setMaximum( false);
                   activeFrame.getDesktopPane().getDesktopManager().activateFrame(activeFrame);
                } catch (Exception ex){}
            }
        }
        // A bit more complex...
        else if (e. getSource() == iconifyButton)
        {
            if (this.activeFrame != null)
            {
                try {
                   //activeFrame.setMaximum( false);
                   activeFrame.setIcon(true);
                } catch (Exception ex){}
            }
        }
        // the camplex case....the user want close this form!
        // What we have to do?
        //
        else if (e. getSource() == closeButton)
        {
            if (this.activeFrame != null)
            {
                /*
                if (activeFrame instanceof JReportFrame)
                {
                    JReportFrame jrf = (JReportFrame)activeFrame;
                    //System.out.println( activeFrame.getTitle() );
                    boolean saveIt = true;
                    if (jrf.getReport().isModified() && !MainFrame.getMainInstance().getProperties().getProperty("AskToSave","true").equals("false"))
                    {
                        String message = "Would you like to save the report before exiting?";
                        String caption = "Unsaved file" + (jrf.getReport().isModified()?" (Changed)": " (Unchanged)") ;
                        int ret = javax.swing.JOptionPane.showConfirmDialog(this, message, caption, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
                        
                        switch(ret)
                        {
                            case javax.swing.JOptionPane.YES_OPTION:
                                saveIt = true;
                                break;
                            case javax.swing.JOptionPane.NO_OPTION:
                                saveIt = false;
                                break;
                            default:
                                return;
                        }
                    }
                    if (saveIt)
                    {
                        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent(this,0, "");
                        MainFrame.getMainInstance().jMenuItemSaveActionPerformed(ae);
                        if (jrf.getReport().isModified()) return;
                    }
                }
                */
                try {
                    
                    //System.out.println(closeCount+" "+activeFrame.getTitle());
                    //System.out.flush();
                    
                    javax.swing.JInternalFrame f = activeFrame;
                    int frames = 0;
                    if (activeFrame != null && activeFrame.getDesktopPane() != null)
                    {
                       frames = activeFrame.getDesktopPane().getAllFrames().length;
                    }
                    f.doDefaultCloseAction();
                    if (f.isClosed() && frames <= 1)
                    {
                        removeFrameButtons();
                    }
                    
                    MainFrame.getMainInstance().updateMenuWindowList();
                    //activeFrame.setMaximum( false);
                    //activeFrame.setIcon(true);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void restoreButtonsPosition()
    {
        componentResized(null);
    }
    
    public void closeFrame( javax.swing.JInternalFrame frameToClose)
    {
         closeFrame( frameToClose, false );
    }
    
    public void closeFrame( javax.swing.JInternalFrame frameToClose, boolean force )
    {
        try {
            
        javax.swing.JInternalFrame oldActive = this.activeFrame;
        
        if (oldActive == frameToClose) oldActive = null;
        if (frameToClose != null)
            {
                
                //if (!((JMDIFrame)frameToClose).closingFrame(force)) return;
                //System.out.println("Closing...!!!");
                try {
                    
                    //System.out.println(closeCount+" "+activeFrame.getTitle());
                    //System.out.flush();
                    int frames = frameToClose.getDesktopPane().getAllFrames().length;
                    frameToClose.setDefaultCloseOperation(frameToClose.DISPOSE_ON_CLOSE);
                    frameToClose.doDefaultCloseAction();
                    
                    if (frameToClose.isClosed() && frames <= 1)
                    {
                        removeFrameButtons();
                    }
                    
                    //activeFrame.setMaximum( false);
                    //activeFrame.setIcon(true);
                } catch (Exception ex){
                    ex.printStackTrace();;
                }
            }

            if (oldActive != null && oldActive instanceof JReportFrame)
            {
                MainFrame.getMainInstance().setActiveReportForm( (JReportFrame)oldActive );
            }
        } finally
        {
            MainFrame.getMainInstance().updateMenuWindowList();
        }
    }
}
