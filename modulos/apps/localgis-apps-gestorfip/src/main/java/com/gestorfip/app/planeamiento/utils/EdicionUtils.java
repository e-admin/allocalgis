/**
 * EdicionUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import com.vividsolutions.jump.I18N;

public class EdicionUtils {

	
	 /**
     * Checkea que el valor introducido en el campo solo contiene numeros.
     *
     * @param comp  El componente editable
     */
    public static void chequeaNumCampoEdit(final JTextComponent comp, JFrame parent)
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
                JOptionPane.showMessageDialog(parent, I18N.get("LocalGISGestorFip","gestorFip.validacion.campos.msg1"));
//                retrocedeCaracter(comp,txt.length()-1);
                eliminarTexto(comp);
            }
           
        }
    }
    
    /**
     * Checkea que el valor introducido en el campo solo contiene numeros y es un long.
     *
     * @param comp  El componente editable
     */
    public static void chequeaLongCampoEdit(final JTextComponent comp, JFrame parent)
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            long x=-1;
            try
            {
                x= Long.parseLong(txt);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(parent, I18N.get("LocalGISGestorFip","gestorFip.validacion.campos.msg1"));
              //  retrocedeCaracter(comp,txt.length()-1);
                eliminarTexto(comp);
            }
           
        }
    }
    
    public static void chequeaDecimalCampoEdit(final JTextComponent comp, JFrame parent)
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
                JOptionPane.showMessageDialog(parent, I18N.get("LocalGISGestorFip",
                					"gestorFip.validacion.campos.msg1"));
//                retrocedeCaracter(comp,txt.length()-1);
                eliminarTexto(comp);
            }
        }
    }    
    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro.
     *
     * @param comp  El componente editable
     * @param maxLong El tamaï¿½o maximo
     */
    public static void chequeaTamanioCampoEdit(final JTextComponent comp, int maxLong, JFrame parent)
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
        	 if(txt.length()>maxLong )
             {
                 retrocedeCaracter(comp, maxLong);
             }
           
        }
    }
    
    /**
     * Metodo que borra caracteres de componentes editables.
     *
     * @param comp El componente en el que borrar los caracteres.
     * @param maxLong El tamaï¿½o maximo que se quiere en el componente.
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
    
    /**
     * Metodo que borra el texto completo de componentes editables.
     *
     * @param comp El componente en el que borrar los caracteres.
     * @param maxLong El tamaï¿½o maximo que se quiere en el componente.
     * */
    public static void eliminarTexto(final JTextComponent comp)
    {
        Runnable checkLength = new Runnable()
        {
            public void run()
            {
                comp.setText(comp.getText().substring(0,0));
            }
        };
        SwingUtilities.invokeLater(checkLength);
    }
}
