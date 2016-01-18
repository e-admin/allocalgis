/**
 * 
 */
package com.geopista.app.utilidades.comboBoxTipo;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;


/**
 * @author seilagamo
 *
 */
public class ComboBoxKeyTipoDestinoSelectionManager implements JComboBox.KeySelectionManager {

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
        	String elem = "";
        	if(aModel.getElementAt(i) instanceof EstructuraDB){
        		elem = ((EstructuraDB)aModel.getElementAt(i)).getPatron();
        	}
        	else if (aModel.getElementAt(i) instanceof Municipio){
        		elem = ((Municipio)aModel.getElementAt(i)).getNombreOficial();
        	}
        	else if (aModel.getElementAt(i) instanceof Provincia){
        		elem = ((Provincia)aModel.getElementAt(i)).getIdProvincia();
        	}
            if (elem != null && elem.toString() != null) {
                v = elem.toLowerCase();
                if (v.length() > 0 && v.charAt(0) == aKey)
                    return i;
            }
        }

        for (i = 0; i < currentSelection; i++) {
        	String elem = "";
        	if(aModel.getElementAt(i) instanceof EstructuraDB){
        		elem = ((EstructuraDB)aModel.getElementAt(i)).getPatron();
        	}
        	else if (aModel.getElementAt(i) instanceof Municipio){
        		elem = ((Municipio)aModel.getElementAt(i)).getNombreOficial();
        	}
        	else if (aModel.getElementAt(i) instanceof Provincia){
        		elem = ((Provincia)aModel.getElementAt(i)).getIdProvincia();
        	}
            if (elem != null && elem.toString() != null) {
                v = elem.toLowerCase();
                if (v.length() > 0 && v.charAt(0) == aKey)
                    return i;
            }
        }
        return -1;
    }
    

}
