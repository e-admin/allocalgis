package com.geopista.app.inventario;

import com.geopista.protocol.inventario.CampoFiltro;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 06-oct-2006
 * Time: 13:36:35
 * To change this template use File | Settings | File Templates.
 */
public class ComboBoxSelectionManager implements JComboBox.KeySelectionManager {
    long lastKeyTime = 0;
    String pattern = "";

    public int selectionForKey(char aKey, ComboBoxModel model) {
        // Find index of selected item
        int selIx = 01;
        Object sel = model.getSelectedItem();
        if (sel != null) {
            for (int i=0; i<model.getSize(); i++) {
                if (sel.equals(model.getElementAt(i))) {
                    selIx = i;
                    break;
                }
            }
        }

        // Get the current time
        long curTime = System.currentTimeMillis();

        // If last key was typed less than 300 ms ago, append to current pattern
        if (curTime - lastKeyTime < 300) {
            pattern += ("" + aKey).toLowerCase();
        } else {
            pattern = ("" + aKey).toLowerCase();
        }

        // Save current time
        lastKeyTime = curTime;

        // Search forward from current selection
        for (int i=selIx+1; i<model.getSize(); i++) {
            String s= ((CampoFiltro)model.getElementAt(i)).getDescripcion()!=null?((CampoFiltro)model.getElementAt(i)).getDescripcion().toLowerCase():((CampoFiltro)model.getElementAt(i)).getNombre().toLowerCase();
            if (s.startsWith(pattern)) {
                return i;
            }
        }

        // Search from top to current selection
        for (int i=0; i<selIx ; i++) {
            if (model.getElementAt(i) != null) {
                String s= ((CampoFiltro)model.getElementAt(i)).getDescripcion()!=null?((CampoFiltro)model.getElementAt(i)).getDescripcion().toLowerCase():((CampoFiltro)model.getElementAt(i)).getNombre().toLowerCase();
                if (s.startsWith(pattern)) {
                    return i;
                }
            }
        }
        return -1;
    }

}
