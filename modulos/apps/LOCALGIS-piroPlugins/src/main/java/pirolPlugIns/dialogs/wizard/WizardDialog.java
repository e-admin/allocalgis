/**
 * WizardDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19.05.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: WizardDialog.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:55 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/wizard/WizardDialog.java,v $
 */
package pirolPlugIns.dialogs.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * This dialog is a container for any kind of install-wizard-like dialogs.
 * At the bottom of the dialog there will be a panel with control buttons 
 * (<code>{@link WizardControlButtonPanel}</code>), at the top of the dialog
 * is a header area with an image and a brief description of the content centered
 * in the dialog (<code>{@link WizardHeaderPanel}</code>).
 * @author Carsten Schulze
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement 
 */
public class WizardDialog extends JDialog implements ActionListener, WindowListener{
    /**Return value from class method if CANCEL is chosen.*/
    public static final int CANCEL_OPTION = -1;
    /**Return value from class method if FINISH is chosen.*/
    public static final int FINISH_OPTION = 0;
//    /**
//     * This method will initialize the {@link WizardHeaderPanel} with an empty
//     * text, image and a white background before setting the dialog to visible 
//     * true. If the given content provides information about the background color,
//     * image and/or text, the information will be used.
//     * @param content the content to be displayed.
//     * @param owner the <code>non-null</code> Frame from which the dialog is displayed
//     * @param title the <code>String</code> to display in the dialog's title bar
//     * @return {@link #CANCEL_OPTION} if the user has pressed the cancel button
//     * or {@link #FINISH_OPTION} if the user has pressed the finish button.
//     */
//    public static int showDialog(Frame owner, String title, WizardContentPanel[] content) {
//        WizardDialog wd = new WizardDialog(owner, title, content);
//        wd.setVisible(true);
//        while(wd.isVisible()){/*just wait*/}
//        return wd.returnValue;
//    }
    /**The possible content of the wizard. This array will be displayed step by 
     * step if the user clicks the buttons.*/
    private WizardContentPanel[] contentArray;
    /**This is the container for the current item in the {@link #contentArray}.*/
    private JPanel contentPanel;
    /**the buttons at the bottom of this dialog.*/
    private WizardControlButtonPanel controlButtonPanel;
    private boolean controlFinishButton = true;
    /**The index of the current displayed content.*/
    private int currentIndex = -999;
    private PersonalLogger debugLogger  = new PersonalLogger(DebugUserIds.USER_Carsten);
    /**the text and image area displayed above the content.*/
    private WizardHeaderPanel headerPanel;
    private int returnValue = -999; 
    
    /**
     * Constructor.
     * @param owner the <code>non-null</code> Frame from which the dialog is displayed
     * @param title the <code>String</code> to display in the dialog's title bar 
     * @param content the content to be displayed.
     */
    public WizardDialog(Frame owner, String title, WizardContentPanel[] content) {
       super(owner,title,true);
       
       if(content == null){
           throw new NullPointerException(
                   "The Wizard is nothing without content. So create one!");
       }
       
       contentArray = content;

       for(int i=0; i<contentArray.length; i++){
           contentArray[i].setWizardDialog(this);
       }
       
       Dimension dim = contentArray[0].getPreferredSize();
       
       headerPanel = new WizardHeaderPanel(
               contentArray[0].getHeaderText(),
               contentArray[0].getHeaderImage());
       
       headerPanel.setBackground(contentArray[0].getHeaderColor());
       
       controlButtonPanel = new WizardControlButtonPanel(this);
       
       contentPanel = new JPanel(new GridLayout(1,1,10,10));
       contentPanel.setSize(dim.width+10, dim.height+10);
       contentPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.LIGHT_GRAY));
       contentPanel.add(contentArray[0]);
       currentIndex = 0;
       this.enableFinishButton(false);
       
       this.getContentPane().setLayout(new BorderLayout());
       this.getContentPane().add(contentPanel,BorderLayout.CENTER);
       this.getContentPane().add(controlButtonPanel,BorderLayout.SOUTH);
       this.getContentPane().add(headerPanel,BorderLayout.NORTH);
       
       this.pack();
       
       this.addWindowListener(this);
       
       this.centerOnScreen();
    }
    
    /**
     * Invoked when an action occurs.
     * @param e the event
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
//System.out.println(ac);
        if(WizardControlButtonPanel.BACKWARD_BUTTON_ACTION_COMMAND.equals(ac)){
            showPrevious();
            this.repaint();
        }
        else if(WizardControlButtonPanel.CANCEL_BUTTON_ACTION_COMMAND.equals(ac)){
            this.setVisible(false);
            returnValue = CANCEL_OPTION;
        }
        else if(WizardControlButtonPanel.FOREWARD_BUTTON_ACTION_COMMAND.equals(ac)){
            if(contentArray[currentIndex].canProceed()){
                showNext();
                this.repaint();
            }
        }
        else if(WizardControlButtonPanel.FINISH_BUTTON_ACTION_COMMAND.equals(ac)){
            if(contentArray[currentIndex].canFinish()){
                returnValue = FINISH_OPTION;
                this.setVisible(false);
            }
        }
    }

    /**
     * Enables (or disables) the finish button. 
     * @param setEnabled true to enable the button, otherwise false
     */
    public void enableFinishButton(boolean setEnabled){
        controlButtonPanel.enableFinishButton(setEnabled);
    }
    /**
     * Enables (or disables) if the WizardDialog should control the finish button. 
     * @param setEnabled true to enable the control, otherwise false
     */
    public void enableFinishButtonControl(boolean setEnabled){
        controlFinishButton = setEnabled;
    }
    
    /**
     * Replaces the content at the specified index with the given panel.
     * @param index the index of the panel to replace.
     * @param newPanel the ne panel.
     */
    public void replaceContentAt(int index, WizardContentPanel newPanel){
    	contentArray[index] = newPanel;
    }
    /**
     * Sets the background color of the header.
     * @param color the color
     * @see WizardHeaderPanel#setBackground(Color)
     */
    public void setHeaderBackgroundColor(Color color) {
        headerPanel.setBackground(color);
    }
    /**
     * Sets the description text of the header.
     * @param text the description
     * @see WizardHeaderPanel#setDescriptionText(String)
     */
    public void setHeaderDescriptionText(String text) {
        headerPanel.setDescriptionText(text);
    }
    /**
     * Sets the image of the header.
     * @param image the image
     * @see WizardHeaderPanel#setImage(ImageIcon)
     */
    public void setHeaderImage(ImageIcon image) {
        headerPanel.setImage(image);
    }
    
    /**
     * This method will set the wizard to visible.
     * @return {@link #CANCEL_OPTION} if the user has pressed the cancel button
     * or {@link #FINISH_OPTION} if the user has pressed the finish button.
     */
    public int showDialog() {
        this.setVisible(true);
        while(this.isVisible()){/*just wait*/}
        return this.returnValue;
    }
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent arg0) {}
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent arg0) {}
    /**
     * Treats this event in the same way as if the user has pressed the cancel 
     * button.
     * @param e the event
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
        returnValue = CANCEL_OPTION;
        this.setVisible(false);
    }
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent arg0) {}
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent arg0) {}
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent arg0) {}
    /**
     * Does nothing.
     * @param arg0 ignored
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent arg0) {}
    /**
     * This method centers the window or subclasses of it on the screen.
     */
    private void centerOnScreen(){
        Dimension screen = this.getToolkit().getScreenSize();
        Dimension window = this.getSize();
        int x = (screen.width / 2) - (window.width / 2);
        int y = (screen.height / 2) - (window.height / 2);
        
        this.setLocation(x,y);
    }
    /**
     * This will display the next item inside the {@link #contentArray}.
     * If the {@link #controlFinishButton} flag is set, this method will
     * enable or disable the button if needed.
     */
    private void showNext(){
        if(currentIndex < contentArray.length-1){
            currentIndex++;
            contentPanel.removeAll();
            contentPanel.add(contentArray[currentIndex]);
            
            debugLogger.printDebug(contentArray[currentIndex].toString(),false);
            
            setHeaderDescriptionText(
                    contentArray[currentIndex].getHeaderText());
            
            setHeaderBackgroundColor(
                    contentArray[currentIndex].getHeaderColor());
            
            setHeaderImage(
                    contentArray[currentIndex].getHeaderImage());
            
            contentPanel.revalidate();
        }
        if(controlFinishButton){
	        if(currentIndex >= contentArray.length-1){
	            controlButtonPanel.enableFinishButton(true);
	        }
	        else{
	            controlButtonPanel.enableFinishButton(false); 
	        }
    	}
    }
    /**
     * This will display the previous item inside the {@link #contentArray}.
     * If the {@link #controlFinishButton} flag is set, this method will
     * enable or disable the button if needed.
     */
    private void showPrevious(){
        if(currentIndex >= 1){
            currentIndex--;
            contentPanel.removeAll();
            contentPanel.add(contentArray[currentIndex]);
            
            setHeaderDescriptionText(
                    contentArray[currentIndex].getHeaderText());
            
            setHeaderBackgroundColor(
                    contentArray[currentIndex].getHeaderColor());
            
            setHeaderImage(
                    contentArray[currentIndex].getHeaderImage());
            
            contentPanel.revalidate();
        }
        if(controlFinishButton){
	        controlButtonPanel.enableFinishButton(false);
    	} 
    }
}
