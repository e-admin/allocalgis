package com.geopista.app.utilidades;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class EdicionUtils
{

    public EdicionUtils()
    {
    }

    public static void crearMallaPanel(int numfila, int numCeldas, JPanel panel, double weightx, double weighty, int anchor, 
            int fill, Insets insets, int ipadx, int ipady)
    {
        for(int i = 0; i < numCeldas; i++)
            panel.add(new JPanel(), new GridBagConstraints(i, numfila, 1, 1, weightx, weighty, anchor, fill, insets, ipadx, ipady));

    }

    public static void cargarLista(JComboBox jComboBox, ArrayList lst)
    {
        jComboBox.removeAllItems();
        for(Iterator it = lst.iterator(); it.hasNext(); jComboBox.addItem(it.next()));
    }
    
}