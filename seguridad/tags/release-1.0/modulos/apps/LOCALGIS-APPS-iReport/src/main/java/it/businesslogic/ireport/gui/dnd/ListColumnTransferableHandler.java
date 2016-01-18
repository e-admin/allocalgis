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
 * ListColumnTransferableHandler.java
 * 
 * Created on 21 maggio 2003, 18.48
 *
 */

package it.businesslogic.ireport.gui.dnd;

/**
 *
 * @author  Administrator
 */
import  it.businesslogic.ireport.gui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class ListColumnTransferableHandler extends ColumnTransferableHandler {
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    private JSQLTablesPane jSQLTablePane=null;
    public void setSQLTablesPane(JSQLTablesPane jSQLTablePane)
    {
        this.jSQLTablePane = jSQLTablePane;
    }
    public JSQLTablesPane getSQLTablesPane()
    {
        return this.jSQLTablePane;
    }
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    protected String exportString(JComponent c) {
        JList list = (JList)c;
        indices = list.getSelectedIndices();
        Object[] values = list.getSelectedValues();
        
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
                buff.append("\n");
            }
        }
        
        return buff.toString();
    }

    //Take the incoming string and wherever there is a
    //newline, break it into a separate item in the list.
    protected void importString(JComponent c, String str) {
        JList target = (JList)c;
        DefaultListModel listModel = (DefaultListModel)target.getModel();
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //So this is not allowed.
        /*
        if (indices != null && index >= indices[0] - 1 &&
              index <= indices[indices.length - 1]) {
            indices = null;
            return;
        }

        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
         **/
        addIndex = index;
        String[] values = str.split("\n");
        if (values.length > 0)
        {
            System.out.println("Link source."+values[0]+" <-> destination."+ target.getSelectedValue());
        }
        /*
        addCount = values.length;
        for (int i = 0; i < values.length; i++) {
            listModel.add(index++, values[i]);
        }
         */
        
        
    }

    //If the remove argument is true, the drop has been
    //successful and it's time to remove the selected items 
    //from the list. If the remove argument is false, it
    //was a Copy operation and the original list is left
    //intact.
    protected void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            JList source = (JList)c;
            DefaultListModel model  = (DefaultListModel)source.getModel();
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            /*
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
             ***/
        }
             
        indices = null;
        addCount = 0;
        addIndex = -1;
             
    }
}
