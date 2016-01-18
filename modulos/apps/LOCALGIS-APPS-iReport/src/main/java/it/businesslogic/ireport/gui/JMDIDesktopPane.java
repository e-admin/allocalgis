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
 * JMDIDesktopPane.java
 * 
 * Created on 7 febbraio 2003, 23.06
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.gui.event.InternalFrameActivatedEvent;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JInternalFrame;

/**
 * JMDIDesktopPane is an evolution of the very trivial javax.swing.JDesktopPane.
 * This class add a lot of event for really handle a DesktopPane. With this
 * improvements we will able to handle internal windows menu list, activated window events,
 * and more...  
 * 
 *
 * @author  Administrator
 */
public class JMDIDesktopPane extends javax.swing.JDesktopPane implements java.io.Serializable {
    
   private DropTarget dropTarget;
   private DropTargetListener dtListener;
   private int acceptableActions = DnDConstants.ACTION_COPY_OR_MOVE;  

  
    private static final String PROP_SAMPLE_PROPERTY = "SampleProperty";
    
    private String sampleProperty;
    
//iR20    private ImageIcon background = new javax.swing.ImageIcon(JMDIDesktopPane.class.getResource("/it/businesslogic/ireport/gui/wp.jpg" ));
    private PropertyChangeSupport propertySupport;
    
    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList =  null;
    
    /** Creates new JMDIDesktopPane */
    public JMDIDesktopPane() {
        super();
        propertySupport = new PropertyChangeSupport( this );
        
        this.dtListener = new DTListener();
    
        // component, ops, listener, accepting
        this.dropTarget = new DropTarget(this, 
            this.acceptableActions,
            this.dtListener,
        true);
        
//iR20        this.setOpaque(false);
    }
    
//iR20    protected void paintComponent(Graphics g) {
//iR20    
//iR20        if (background != null)
//iR20        {
//iR20            g.drawImage(background.getImage(), 0,0, this);
//iR20        }
//iR20        super.paintComponents(g);
//iR20    }
    
    
    public void internalFrameActivated(JMDIFrame jMDIFrame)
    {
        this.fireInternalFrameActivatedListenerInternalFrameActivated(new InternalFrameActivatedEvent(jMDIFrame));
    }
    
    public void internalFrameClosed(JMDIFrame jMDIFrame)
    {      
        if (this.getAllFrames().length == 1)
        {
            this.fireInternalFrameActivatedListenerInternalFrameActivated(new InternalFrameActivatedEvent(null) );
        }
        this.fireInternalFrameActivatedListenerInternalFrameActivated(new InternalFrameActivatedEvent(jMDIFrame, InternalFrameActivatedEvent.CLOSED ) );
    }
    
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null || propertySupport == null) return;
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Registers InternalFrameActivatedListener to receive events.
     * @param listener The listener to register.
     *
     */
    public synchronized void addInternalFrameActivatedListener(it.businesslogic.ireport.gui.event.InternalFrameActivatedListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(it.businesslogic.ireport.gui.event.InternalFrameActivatedListener.class, listener);
    }
    
    /** Removes InternalFrameActivatedListener from the list of listeners.
     * @param listener The listener to remove.
     *
     */
    public synchronized void removeInternalFrameActivatedListener(it.businesslogic.ireport.gui.event.InternalFrameActivatedListener listener) {
        listenerList.remove(it.businesslogic.ireport.gui.event.InternalFrameActivatedListener.class, listener);
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireInternalFrameActivatedListenerInternalFrameActivated(it.businesslogic.ireport.gui.event.InternalFrameActivatedEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.InternalFrameActivatedListener.class) {
                ((it.businesslogic.ireport.gui.event.InternalFrameActivatedListener)listeners[i+1]).internalFrameActivated(event);
            }
        }
    }
    
    public void cascade()
    {
        JInternalFrame[] frames = this.getAllFrames();
        if (frames.length == 0) return;
        
        JInternalFrame activeframe = this.getSelectedFrame();
        
        int position =0;
        for (int i=0; i<frames.length; ++i)
        {
            try {
                    frames[i].setMaximum(false);
                    frames[i].setIcon(false);
                    frames[i].moveToFront();
                } catch (Exception ex){}
            if (frames[i] != activeframe)
            {
                frames[i].setLocation(position,position);
                
                position += 24;
            }
        }
        
        if (activeframe != null)
        {   
            activeframe.moveToFront();
            activeframe.setLocation(position,position);
        }
    }
    
    /* This method is ported from kdevelop'QextMdiChildArea
     */
    public void tileVertically()
    {   
        JInternalFrame[] frames_tmp = this.getAllFrames();
        
        if (frames_tmp.length == 0) return;

        JInternalFrame[] frames = new JInternalFrame[frames_tmp.length];
        if (this.getSelectedFrame() == null && frames_tmp[0].isIcon())
        {
            try {
            frames_tmp[0].setIcon(false);
            frames_tmp[0].setSelected(true);
            } catch (Exception ex){ return; }
        }        
        frames[0] = this.getSelectedFrame();
        
        int k=1;
        for (int i=0; i<frames_tmp.length; ++i)
        {
            if (frames_tmp[i] == frames[0]) continue;
            frames[k] = frames_tmp[i];
            k++;
        }

        //adjust the arry to have the selected win in the first position....
        
        
        int w = getWidth() / frames.length;
	int lastWidth = 0;
	if( frames.length > 1)
            lastWidth = getWidth() - (w * (frames.length - 1));
        else
            lastWidth = w;
	
        int h = getHeight();
	int posX = 0;
	int countVisible = 0;

        for (int i=0; i<frames.length; ++i)
        {
            try {
                    frames[i].setMaximum(false);
                    frames[i].setIcon(false);
                    frames[i].moveToFront();
                } catch (Exception ex){}
            
            if( i < frames.length) {
                frames[i].setLocation(posX, 0);
                frames[i].setSize(w, h);
                posX += w;
            }
            else { // last visible childframe
                frames[i].setLocation(posX, 0);
                frames[i].setSize(lastWidth, h);
            }
        }     
    }
    
    /* This method is ported from kdevelop'QextMdiChildArea
     */
    public void tileHorizontally()
    {
        JInternalFrame[] frames_tmp = this.getAllFrames();
        
        if (frames_tmp.length == 0) return;

        JInternalFrame[] frames = new JInternalFrame[frames_tmp.length];
         if (this.getSelectedFrame() == null && frames_tmp[0].isIcon())
        {
            try {
            frames_tmp[0].setIcon(false);
            frames_tmp[0].setSelected(true);
            } catch (Exception ex){ return; }
        }
        frames[0] = this.getSelectedFrame();
        
        int k=1;
        for (int i=0; i<frames_tmp.length; ++i)
        {
            if (frames_tmp[i] == frames[0]) continue;
            else
            {
                frames[k] = frames_tmp[i];
                k++;
            }
        }

        //adjust the arry to have the selected win in the first position....
        
        
        int w = getWidth() ;
	
	
        int h = getHeight() / frames.length;
        
        int lastHeight = 0;
	if( frames.length > 1)
            lastHeight = getHeight() - (h * (frames.length - 1));
        else
            lastHeight = h;
        
	int posY = 0;
	int countVisible = 0;

        for (int i=0; i<frames.length; ++i)
        {
            try {
                    frames[i].setMaximum(false);
                    frames[i].setIcon(false);
                    frames[i].moveToFront();
                } catch (Exception ex){}
            
            if( i < frames.length) {
                frames[i].setLocation(0,posY);
                frames[i].setSize(w, h);
                posY += h;
            }
            else { // last visible childframe
                frames[i].setLocation(0,posY);
                frames[i].setSize(w,lastHeight);
            }
        }   
        
    }
    
    /* This method is ported from kdevelop'QextMdiChildArea
     */
    public void tileAnodine()
    {
        JInternalFrame[] frames_tmp = this.getAllFrames();
        
        if (frames_tmp.length == 0) return;

        JInternalFrame[] frames = new JInternalFrame[frames_tmp.length];
        
        if (this.getSelectedFrame() == null && frames_tmp[0].isIcon())
        {
            try {
            frames_tmp[0].setIcon(false);
            frames_tmp[0].setSelected(true);
            } catch (Exception ex){ return; }
        }
        
        frames[0] = this.getSelectedFrame();    
        int k=1;
        for (int i=0; i<frames_tmp.length; ++i)
        {
            if (frames_tmp[i] == frames[0]) continue;
            frames[k] = frames_tmp[i];
            k++;
        }
        
	int numVisible=frames.length;
	if(numVisible<1)return;
	int numCols= (int)Math.sqrt(numVisible); // set columns to square root of visible count
	// create an array to form grid layout
	int[] numRows=new int[numCols];
	int numCurCol=0;
	while(numCurCol<numCols){
		numRows[numCurCol]=numCols; // create primary grid values
		numCurCol++;
	}
	int numDiff=numVisible-(numCols*numCols); // count extra rows
	int numCurDiffCol=numCols; // set column limiting for grid updates
	while(numDiff>0){
		numCurDiffCol--;
		numRows[numCurDiffCol]++; // add extra rows to column grid
		if(numCurDiffCol<1)numCurDiffCol=numCols; // rotate through the grid
		numDiff--;
	}
	numCurCol=0;
	int numCurRow=0;
	int curX=0;
	int curY=0;
	// the following code will size everything based on my grid above
	// there is no limit to the number of windows it will handle
	// it's great when a kick-ass theory works!!!                      // Pragma :)
	int xQuantum= getWidth()/numCols;
	int yQuantum= getHeight()/numRows[numCurCol];
               
	for (int i=0; i<frames.length; ++i)
        {
               try {
                    frames[i].setMaximum(false);
                    frames[i].setIcon(false);
                    frames[i].moveToFront();
                } catch (Exception ex){}

                frames[i].setLocation(curX,curY);
                frames[i].setSize(xQuantum, yQuantum);
		numCurRow++;
		curY+=yQuantum;
		if(numCurRow==numRows[numCurCol]){
				numCurRow=0;
				numCurCol++;
				curY=0;
				curX+=xQuantum;
				if(numCurCol!=numCols)yQuantum= getHeight()/numRows[numCurCol];
			}
        }
    }
    

    
}
  
