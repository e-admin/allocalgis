/**
 * DoubleEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * First modified on 23.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: DoubleEditor.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/DoubleEditor.java,v $
 */
package pirolPlugIns.utilities;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * From the Sun tutorials: formerly IntegerEditor:
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 *
 * @author Sun (modified for PIROL)
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class DoubleEditor extends DefaultCellEditor {
    
    private static final long serialVersionUID = 2796535192484117090L;
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    JFormattedTextField ftf;
    NumberFormat doubleFormat;
    
    public DoubleEditor() {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField)getComponent();
        
        //Set up the editor for the integer cells.
        doubleFormat = PirolPlugInSettings.getDefaultNumberFormat();
        NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
        //doubleFormatter.setFormat(doubleFormat);
        
        
        ftf.setFormatterFactory(
                new DefaultFormatterFactory(doubleFormatter));
        
        ftf.setHorizontalAlignment(JTextField.TRAILING);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);
        

        ftf.getInputMap().put(KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0), "check");
        ftf.getActionMap().put("check", new AbstractAction() {
            
            private static final long serialVersionUID = -5274988589127072059L;
            
            public void actionPerformed(ActionEvent e) {
                if (!ftf.isEditValid()) { //The text is invalid.
                    if (userSaysRevert()) { //reverted
                        ftf.postActionEvent(); //inform the editor
                    }
                } else try {              //The text is valid,
                    ftf.commitEdit();     //so use it.
                    ftf.postActionEvent(); //stop editing
                } catch (java.text.ParseException exc) { 
                    logger.printWarning(exc.getMessage());
                }
            }
        });
    }
    
    //Override to invoke setValue on the formatted text field.
    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected,
            int row, int column) {
        JFormattedTextField ftf =
            (JFormattedTextField)super.getTableCellEditorComponent(
                    table, value, isSelected, row, column);
        ftf.setValue(value);
        return ftf;
    }
    
    //Override to ensure that the value remains an Integer.
    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        Object value = ftf.getValue();
        if (value instanceof Double) {
            logger.printDebug("o is a Double");
            return value;
        } else if (value instanceof Number) {
            logger.printWarning("o is a Number:" + value.getClass().getName() + ", " + ftf.getText());
            
            return new Double(Double.parseDouble(ftf.getText()));
        } else {
            logger.printWarning("o isn't a Number");
            
            
            try {
                return doubleFormat.parseObject(value.toString());
            } catch (ParseException exc) {
                logger.printError("can't parse o: " + value);
                return null;
            }
        }
    }
    
    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version 
    //of this method so that everything gets cleaned up.
    public boolean stopCellEditing() {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        if (ftf.isEditValid()) {
            try {
                ftf.commitEdit();
            } catch (java.text.ParseException exc) { }
            
        } else { //text is invalid
            if (!userSaysRevert()) { //user wants to edit
                return false; //don't let the editor go away
            } 
        }
        return super.stopCellEditing();
    }
    
    /** 
     * Lets the user know that the text they entered is 
     * bad. Returns true if the user elects to revert to
     * the last good value.  Otherwise, returns false, 
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();
        Object[] options = { PirolPlugInMessages.getString("edit"), PirolPlugInMessages.getString("revert")};
        int answer = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(ftf),
                PirolPlugInMessages.getString("invalid-value-edit-or-revert"),
                PirolPlugInMessages.getString("invalid-input"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);
        
        if (answer == 1) { //Revert!
            ftf.setValue(ftf.getValue());
            return true;
        }
        return false;
    }
}
