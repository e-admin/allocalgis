/*
 * Created on 19.05.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: WizardContentPanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:55 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/wizard/WizardContentPanel.java,v $
 */
package pirolPlugIns.dialogs.wizard;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;
/**
 * The <code>WizardContentPanel</code>
 * @author Carsten Schulze
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 */
public abstract class WizardContentPanel extends JPanel {
	/**debug object*/
	protected PersonalLogger debugLogger = null;
    /**The containing dialog*/
    protected WizardDialog wizardDialog = null;
    /**
     * Constructor.
     * This constructor will initialize the size and the preferred size to
     * 400 x 300 pixel.
     */
    public WizardContentPanel(){
        this.setSize(400,250);
        this.setPreferredSize(this.getSize());
    }
    /**
     * If the finish button has been pressed, this method will be invoked to
     * check if the dialog can be made unvisible and the return.
     * @return true if every user input is ok, false otherwise.
     */
    public abstract boolean canFinish();
    /**
     * If the forward button has been pressed, this method will be invoked to
     * check if the next page of the dialog can be displayed.
     * @return true if every user input is ok, false otherwise.
     */
    public abstract boolean canProceed();
    /**
     * This method should return the color used as background color in the header 
     * panel of the dialog.
     * @return the background color
     */
    public abstract Color getHeaderColor();
    /**
     * This method should return the <code>ImageIcon</code> shown in the header 
     * panel of the dialog.
     * @return the image. The Image should not be bigger than 400 x 75 pixel.
     */
    public abstract ImageIcon getHeaderImage();
    /**
     * This method should return the description shown in the header panel of
     * the dialog.
     * @return the description
     */
    public abstract String getHeaderText();
    
    /**
     * Sets the containing WizardDialog. This method will be called inside the
     * WizardDialogs constructor.
     * @param wizardDialog The wizardDialog to set.
     */
    public void setWizardDialog(WizardDialog wizardDialog) {
        this.wizardDialog = wizardDialog;
    }
}
