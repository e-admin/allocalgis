/**
 * CustomColumnControlButton.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
