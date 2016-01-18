/**
 * FieldsContainerTransferHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * FieldsTableTransferHandler.java
 * 
 * Created on May 15, 2007, 4:31:24 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.dnd;

import it.businesslogic.ireport.JRField;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author gtoffoli
 */
public class FieldsContainerTransferHandler  extends TransferHandler  {

    private FieldsContainer fieldsContainer = null;

    public FieldsContainer getFieldsContainer() {
        return fieldsContainer;
    }

    public void setFieldsContainer(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }
    
    public FieldsContainerTransferHandler(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                
                JRField field = (JRField)t.getTransferData(t.getTransferDataFlavors()[0]);
                
                if (fieldsContainer != null)
                {
                    fieldsContainer.addField(field);
                }
                
                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }

        return false;
    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].getMimeType().equals("application/x-java-serialized-object; class=it.businesslogic.ireport.JRField")) {
                return true;
            }
        }
        return false;
    }


}
