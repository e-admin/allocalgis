/**
 * 
 */
package com.geopista.ui.plugin.geometrytousepoint;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.geopista.app.catastro.model.beans.TipoDestino;


/**
 * @author seilagamo
 *
 */
public class ComboBoxTipoDestinoSelectionManager implements JComboBox.KeySelectionManager {

    public int selectionForKey(char aKey, ComboBoxModel aModel) {
        int i, c;
        int currentSelection = -1;
        Object selectedItem = aModel.getSelectedItem();
        String v;
        String pattern;

        if (selectedItem != null) {
            for (i = 0, c = aModel.getSize(); i < c; i++) {
                if (selectedItem == aModel.getElementAt(i)) {
                    currentSelection = i;
                    break;
                }
            }
        }

        pattern = ("" + aKey).toLowerCase();
        aKey = pattern.charAt(0);

        for (i = ++currentSelection, c = aModel.getSize(); i < c; i++) {
            String elem = ((TipoDestino)aModel.getElementAt(i)).getPatron();
            if (elem != null && elem.toString() != null) {
                v = elem.toLowerCase();
                if (v.length() > 0 && v.charAt(0) == aKey)
                    return i;
            }
        }

        for (i = 0; i < currentSelection; i++) {
            String elem = ((TipoDestino)aModel.getElementAt(i)).getPatron();
            if (elem != null && elem.toString() != null) {
                v = elem.toLowerCase();
                if (v.length() > 0 && v.charAt(0) == aKey)
                    return i;
            }
        }
        return -1;
    }
    

}
