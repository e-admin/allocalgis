/*
 * CustomColumnControlButton.java
 * 
 * Created on May 17, 2007, 10:49:26 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.table;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.ColumnControlPopup;

/**
 *
 * @author gtoffoli
 */
public class CustomColumnControlButton extends ColumnControlButton {

    private JXTable table = null;
    
    
    
    public CustomColumnControlButton(JXTable table, Icon icon) {
        
        super(table, icon);
        this.table = table;
        
        ColumnControlPopup cc = null;
        cc = getColumnControlPopup();
        Action resetOrderAction = new AbstractAction("Reset order"){

            public void actionPerformed(ActionEvent e) {
                resetOrder();
            }
        };

        List actions = new java.util.ArrayList();
        actions.add(resetOrderAction);

        cc.addAdditionalActionItems(actions);
        
    }
    
    public void resetOrder()
    {
        table.resetSortOrder();
    }
}
