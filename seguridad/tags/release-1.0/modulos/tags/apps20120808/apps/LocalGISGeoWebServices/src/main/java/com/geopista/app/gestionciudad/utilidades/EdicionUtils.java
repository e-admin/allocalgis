package com.geopista.app.gestionciudad.utilidades;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.vividsolutions.jump.I18N;

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
    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que solo contiene numeros.
     *
     * @param comp  El componente editable
     * @param maxLong El tamaño maximo
     */
    public static void chequeaLongYNumCampoEdit(final JTextComponent comp, final int maxLong, JFrame parent)
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            int x=-1;
            try
            {
                x= Integer.parseInt(txt);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(parent, I18N.get("RegistroExpedientes",
                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg4"));
                retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
                retrocedeCaracter(comp, maxLong);
            }
        }
    }

	public static void chequeaLongYDecimalCampoEdit(final JTextComponent comp, final int maxLong, JFrame parent)
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            double x=-1;
            try
            {
                x= Double.parseDouble(txt);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(parent, I18N.get("RegistroExpedientes",
                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg4"));
                retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
                retrocedeCaracter(comp, maxLong);
            }
        }
    }    
    public static void chequeaLongYCharCampoEdit(final JTextComponent comp, final int maxLong, JFrame parent)   
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            if(txt.length()>maxLong )
              retrocedeCaracter(comp, maxLong);
        }
    }
    /**
     * Metodo que borra caracteres de componentes editables.
     *
     * @param comp El componente en el que borrar los caracteres.
     * @param maxLong El tamaño maximo que se quiere en el componente.
     * */
    public static void retrocedeCaracter(final JTextComponent comp, final int maxLong)
    {
        Runnable checkLength = new Runnable()
        {
            public void run()
            {
                comp.setText(comp.getText().substring(0,maxLong));
            }
        };
        SwingUtilities.invokeLater(checkLength);
    }


}