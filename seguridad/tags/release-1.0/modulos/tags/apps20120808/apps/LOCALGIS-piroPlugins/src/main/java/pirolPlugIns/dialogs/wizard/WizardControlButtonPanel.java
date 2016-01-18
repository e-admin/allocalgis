/*
 * Created on 19.05.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: WizardControlButtonPanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:55 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/wizard/WizardControlButtonPanel.java,v $
 */
package pirolPlugIns.dialogs.wizard;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This is a JPanel with buttons on it, a backward, cancel, foreward
 * and finish button.
 * @author Carsten Schulze
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 */
public class WizardControlButtonPanel extends JPanel {
    /**constant ActionCommand String related to the {@link #backwardButton}.*/
    public final static String BACKWARD_BUTTON_ACTION_COMMAND = new String("BACKWARD_BUTTON_ACTION_COMMAND");
    /**constant ActionCommand String related to the {@link #cancelButton}.*/
    public final static String CANCEL_BUTTON_ACTION_COMMAND = new String("CANCEL_BUTTON_ACTION_COMMAND");
    /**constant ActionCommand String related to the {@link #finishButton}.*/
    public static final String FINISH_BUTTON_ACTION_COMMAND = new String("FINISH_BUTTON_ACTION_COMMAND");
    /**constant ActionCommand String related to the {@link #forwardButton}.*/
    public final static String FOREWARD_BUTTON_ACTION_COMMAND = new String("FOREWARD_BUTTON_ACTION_COMMAND");
    /**constant text String related to the {@link #backwardButton}.*/
    private final static String BACKWARD_BUTTON_TEXT = new String("<<");
    /**constant text String related to the {@link #cancelButton}.*/
    private final static String CANCEL_BUTTON_TEXT = new String("Cancel");
    /**constant text String related to the {@link #finishButton}.*/
    private static final String FINISH_BUTTON_TEXT = new String("Finish");
    /**constant text String related to the {@link #forwardButton}.*/
    private final static String FOREWARD_BUTTON_TEXT = new String(">>");
    /**the ActionListener used by the three buttons.*/
    private ActionListener actionListener;
    /**the button to skip one item back.*/
    private JButton backwardButton;
    /**the button to cancel.*/
    private JButton cancelButton;
    private JButton finishButton;
    /**the button to skip to the following item.*/
    private JButton forwardButton;
    /**
     * constructor.
     */
    public WizardControlButtonPanel() {
        this.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
        this.add(getBackwardButton());
        this.add(getCancelButton());
        this.add(getForwardButton());
        this.add(getFinishButton());
    }
    /**
     * constructor
     * @param listener the ActionListener to register at the buttons
     */
    public WizardControlButtonPanel(ActionListener listener) {
        this();
        this.addActionListener(listener);
    }
    /**
     * Adds an ActionListener to all of the three buttons.
     * @param listener the ActionListener to register at the buttons
     */
    public void addActionListener(ActionListener listener) {
        if(listener != null){
            actionListener = listener;
            
            getBackwardButton().addActionListener(actionListener);
            getForwardButton().addActionListener(actionListener);
            getCancelButton().addActionListener(actionListener);
            getFinishButton().addActionListener(actionListener);
        }
    }
    /**
     * Enables (or disables) the finish button. 
     * @param setEnabled true to enable the button, otherwise false
     */
    public void enableFinishButton(boolean setEnabled) {
        getFinishButton().setEnabled(setEnabled);
    }
    /**
     * Sets the icon for the JButton.
     * @param icon the icon.
     * @see #setCancelButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     * @see #setCancelButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonText(String)
     */
    public void setBackwardButtonIcon(Icon icon) {
        getBackwardButton().setIcon(icon);
    }
    /**
     * Sets the text for the JButton. 
     * If this method isn't called the {@link #BACKWARD_BUTTON_TEXT} is used.
     * @param text the text.
     * @see #setCancelButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     */
    public void setBackwardButtonText(String text) {
        getBackwardButton().setText(text);
    }
    /**
     * Sets the icon for the JButton.
     * @param icon the icon.
     * @see #setBackwardButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     * @see #setCancelButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonText(String)
     */
    public void setCancelButtonIcon(Icon icon) {
        getCancelButton().setIcon(icon);
    }
    /**
     * Sets the text for the JButton.
     * If this method isn't called the {@link #CANCEL_BUTTON_TEXT} is used.
     * @param text the text.
     * @see #setBackwardButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     */
    public void setCancelButtonText(String text) {
        getCancelButton().setText(text);
    }
    /**
     * Sets the icon for the JButton.
     * @param icon the icon.
     * @see #setCancelButtonIcon(Icon)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonText(String)
     */
    public void setFinishButtonIcon(Icon icon) {
        getFinishButton().setIcon(icon);
    }
    /**
     * Sets the text for the JButton.
     * If this method isn't called the {@link #FINISH_BUTTON_TEXT} is used.
     * @param text the text.
     * @see #setCancelButtonText(String)
     * @see #setBackwardButtonText(String)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     */
    public void setFinishButtonText(String text) {
        getFinishButton().setText(text);
    }
    /**
     * Sets the icon for the JButton.
     * @param icon the icon.
     * @see #setCancelButtonIcon(Icon)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonText(String)
     * @see #setForwardButtonText(String)
     * @see #setBackwardButtonText(String)
     */
    public void setForwardButtonIcon(Icon icon) {
        getForwardButton().setIcon(icon);
    }
    /**
     * Sets the text for the JButton.
     * If this method isn't called the {@link #FOREWARD_BUTTON_TEXT} is used.
     * @param text the text.
     * @see #setCancelButtonText(String)
     * @see #setBackwardButtonText(String)
     * @see #setBackwardButtonIcon(Icon)
     * @see #setCancelButtonIcon(Icon)
     * @see #setForwardButtonIcon(Icon)
     */
    public void setForwardButtonText(String text) {
        getForwardButton().setText(text);
    }
    /**
     * Inits the backwardButton
     * @return the backwardButton
     */
    private JButton getBackwardButton() {
        if(backwardButton == null){
            backwardButton = new JButton(BACKWARD_BUTTON_TEXT);
            backwardButton.setActionCommand(BACKWARD_BUTTON_ACTION_COMMAND);
        }
        return backwardButton;
    }
    /**
     * Inits the cancelButton
     * @return the cancelButton
     */
    private JButton getCancelButton() {
        if(cancelButton == null){
            cancelButton = new JButton(CANCEL_BUTTON_TEXT);
            cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
        }
        return cancelButton;
    }
    /**
     * Inits the finishButton
     * @return the finishButton
     */
    private JButton getFinishButton() {
        if(finishButton == null){
            finishButton = new JButton(FINISH_BUTTON_TEXT);
            finishButton.setActionCommand(FINISH_BUTTON_ACTION_COMMAND);
        }
        return finishButton;
    }
    /**
     * Inits the forwardButton
     * @return the forwardButton
     */
    private JButton getForwardButton() {
        if(forwardButton == null){
            forwardButton = new JButton(FOREWARD_BUTTON_TEXT);
            forwardButton.setActionCommand(FOREWARD_BUTTON_ACTION_COMMAND);
        }
        return forwardButton;
    }
}
