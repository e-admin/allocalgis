package com.geopista.app.eiel.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.vividsolutions.jump.I18N;

public class EdicionUtils
{
    
    public static final int TAM_REFCATASTRAL=14;
    public static final int TAM_NUMORDEN_SUBPARCELA = 4;
    private static String porDefinir = "<POR DEFINIR>";
    
    
    public static String getRefParcela1(String refCatastral)
    {
        if (refCatastral!=null)
        {
            String ref1 = new String();
            refCatastral = paddingString(refCatastral,TAM_REFCATASTRAL,'0' ,true);
            
            ref1 = refCatastral.substring(0,TAM_REFCATASTRAL/2);
            
            return ref1;
        }
        else 
            return "";
        
    }
    
    public static String getRefParcela2(String refCatastral)
    {
        if (refCatastral!=null)
        {
            String ref2 = new String();
            refCatastral = paddingString(refCatastral,TAM_REFCATASTRAL,'0' ,true);
            
            ref2 = refCatastral.substring(TAM_REFCATASTRAL/2,TAM_REFCATASTRAL);
            
            return ref2;
        }
        else 
            return "";
    }
    
    public static void main (String[] args)
    {
        String ref1 = getRefParcela1("4567890ABCD");
        String ref2 = getRefParcela2("4567890ABCD");
        
        System.out.println ("");
    }
    
    
    /**
     ** Rellena una cadena de caracteres S hasta un número N de caracteres con caracteres C
     ** a la izquierda (true) o la derecha (false)
     **/
    public static String paddingString ( String s, int n, char c , boolean paddingLeft  ) {
        if (s==null)
            return null;
        
        StringBuffer str = new StringBuffer(s);
        int strLength  = str.length();
        if ( n > 0 && n > strLength ) {
            for ( int i = 0; i <= n ; i ++ ) {
                if ( paddingLeft ) {
                    if ( i < n - strLength ) str.insert( 0, c );
                }
                else {
                    if ( i > strLength ) str.append( c );
                }
            }
        }
        return str.toString();
    }
    
    
    public static void disabledButtons(JComponent jComponent, String patron)
    {
    	for (int i=0; i<jComponent.getComponentCount(); i++)
    	{   

    		if(jComponent.getComponent(i) instanceof JButton){
    			if (jComponent.getComponent(i).getName()!=null && jComponent.getComponent(i).getName().startsWith(patron))
    				((JButton)jComponent.getComponent(i)).setEnabled(false) ;

    		}                                   
    		else if (jComponent.getComponent(i) instanceof Container
    				&& jComponent.getComponent(i) instanceof JComponent)
    		{
    			disabledButtons((JComponent)jComponent.getComponent(i), patron);
    		}                 
    	}  
    }  
    
    public static void enabledButtons(JComponent jComponent, String patron)
    {
    	for (int i=0; i<jComponent.getComponentCount(); i++)
    	{   

    		if(jComponent.getComponent(i) instanceof JButton){
    			if (jComponent.getComponent(i).getName()!=null && jComponent.getComponent(i).getName().startsWith(patron))
    				((JButton)jComponent.getComponent(i)).setEnabled(true) ;

    		}                                   
    		else if (jComponent.getComponent(i) instanceof Container
    				&& jComponent.getComponent(i) instanceof JComponent)
    		{
    			enabledButtons((JComponent)jComponent.getComponent(i), patron);
    		}                 
    	}  
    }  
    
    public static void editablePanel(JComponent jComponent, boolean editable)
    {
        for (int i=0; i<jComponent.getComponentCount(); i++)
        {   
        	
            if (jComponent.getComponent(i) instanceof JTextField){
            	((JTextField)jComponent.getComponent(i)).setEditable(editable) ;
            }
            else if(jComponent.getComponent(i) instanceof JComboBox){
            	((JComboBox)jComponent.getComponent(i)).setEditable(editable) ;
            }
            else if (jComponent.getComponent(i) instanceof JCheckBox)
            {
                ((JCheckBox)jComponent.getComponent(i)).setSelected(editable) ;                   
            } 
            else if(jComponent.getComponent(i) instanceof JRadioButton){
            	((JRadioButton)jComponent.getComponent(i)).setSelected(editable) ;  
            }            
            else if (jComponent.getComponent(i) instanceof Container
                    && jComponent.getComponent(i) instanceof JComponent)
            {
                editablePanel((JComponent)jComponent.getComponent(i), editable);
            }                 
        }        
    }  
    
    public static void enableComponent(JComponent jComponent, boolean enable, String name)
    {
        for (int i=0; i<jComponent.getComponentCount(); i++)
        {   
        	
        	if (jComponent.getComponent(i) instanceof JTextField){
        		if(((JTextField)jComponent.getComponent(i)).getName()!=null)
        			if(((JTextField)jComponent.getComponent(i)).getName().equals(name)){
        				((JTextField)jComponent.getComponent(i)).setEnabled(enable) ;
        				((JTextField)jComponent.getComponent(i)).setBackground(enable?Color.WHITE:Color.LIGHT_GRAY) ;
        			}
            }
        	else if(jComponent.getComponent(i) instanceof JComboBox){
        		if(((JComboBox)jComponent.getComponent(i)).getName()!=null)
        			if(((JComboBox)jComponent.getComponent(i)).getName().equals(name))
        				((JComboBox)jComponent.getComponent(i)).setEnabled(enable) ;
        	}
        	else if (jComponent.getComponent(i) instanceof JCheckBox){
        		if(((JCheckBox)jComponent.getComponent(i)).getName()!=null)
        			if(((JCheckBox)jComponent.getComponent(i)).getName().equals(name))
        				((JCheckBox)jComponent.getComponent(i)).setEnabled(enable) ;                   
        	} 
        	else if(jComponent.getComponent(i) instanceof JRadioButton){
        		if(((JRadioButton)jComponent.getComponent(i)).getName()!=null)
        			if(((JRadioButton)jComponent.getComponent(i)).getName().equals(name))
        				((JRadioButton)jComponent.getComponent(i)).setEnabled(enable) ;  
            }  
        	else if(jComponent.getComponent(i) instanceof JButton){
        		if(((JButton)jComponent.getComponent(i)).getName()!=null)
        			if(((JButton)jComponent.getComponent(i)).getName().equals(name))
        				((JButton)jComponent.getComponent(i)).setEnabled(enable) ; 
        	}
            else if (jComponent.getComponent(i) instanceof Container
                    && jComponent.getComponent(i) instanceof JComponent)
            {
                enableComponent((JComponent)jComponent.getComponent(i), enable, name);
            }                 
        }        
    }  
    

	public static void clearPanel(JComponent jComponent)
    {
        for (int i=0; i<jComponent.getComponentCount(); i++)
        {           	
            if ((jComponent.getComponent(i) instanceof JTextField)&& jComponent.getComponent(i).isEnabled()){
            	((JTextField)jComponent.getComponent(i)).setText("");
            }
            else if ((jComponent.getComponent(i) instanceof JComboBox)&& jComponent.getComponent(i).isEnabled()){
                {
                    if (((JComboBox)jComponent.getComponent(i)).getItemCount()!=0)
                        ((JComboBox)jComponent.getComponent(i)).setSelectedIndex(0);
                }
            }
            else if((jComponent.getComponent(i) instanceof JTable)&& jComponent.getComponent(i).isEnabled()){
            	((JTable)jComponent.getComponent(i)).removeAll();
            }
            else if ((jComponent.getComponent(i) instanceof JCheckBox)&& jComponent.getComponent(i).isEnabled()){
            	((JCheckBox)jComponent.getComponent(i)).setSelected(false);
            }
            else if ((jComponent.getComponent(i) instanceof JRadioButton)&& jComponent.getComponent(i).isEnabled()){
            	((JRadioButton)jComponent.getComponent(i)).setSelected(false);            
            }                    
            else if (jComponent.getComponent(i) instanceof Container
                    && jComponent.getComponent(i) instanceof JComponent && jComponent.getComponent(i).isEnabled())
            {
                clearPanel((JComponent)jComponent.getComponent(i));                
            }                 
        }        
    }  
        
    public static void cargarLista (JComboBox jComboBox, ArrayList lst)
    {
        jComboBox.removeAllItems();
        
        Iterator it = lst.iterator();
        while (it.hasNext())
            jComboBox.addItem(it.next());
    }
          
    public static String getStringValue(Object o)
    {
        String value ="";
        if(o!=null)
            value = String.valueOf(o);
        return value;
    }
    
    public static String getStringValue(int i)
    {
        return getStringValue(new Integer(i));        
    }
    public static String getStringValue(float f)
    {
        return getStringValue(new Float(f));        
    }
    public static String getStringValue(double d)
    {
        return getStringValue(new Double(d));        
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
                UtilRegistroExp.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
                UtilRegistroExp.retrocedeCaracter(comp, maxLong);
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
                UtilRegistroExp.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
                UtilRegistroExp.retrocedeCaracter(comp, maxLong);
            }
        }
    }    
    public static void chequeaLongYCharCampoEdit(final JTextComponent comp, final int maxLong, JFrame parent)   
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            if(txt.length()>maxLong )
              UtilRegistroExp.retrocedeCaracter(comp, maxLong);
        }
    }
    
    public static void crearMallaPanel(int numfila, int numCeldas, JPanel panel, double weightx, 
    		double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady){
    	
    	for(int i=0;i<numCeldas;i++){
    		
    		panel.add(new JPanel(), 
            new GridBagConstraints(i, numfila, 1, 1, weightx, weighty,
                    anchor, fill,insets, ipadx, ipady));
    	}
    }
    public static void enablePanel(JComponent jComponent, boolean enabled)
    {
        for (int i=0; i<jComponent.getComponentCount(); i++)
        {        	
            if (jComponent.getComponent(i) instanceof JTextField
                    || jComponent.getComponent(i) instanceof JComboBox
                    || jComponent.getComponent(i) instanceof JTable)
            {
	
                ((JComponent)jComponent.getComponent(i)).setEnabled(enabled) ;
                ((JComponent)jComponent.getComponent(i)).setBackground(enabled?Color.WHITE:Color.LIGHT_GRAY) ;
            }                
            else if (jComponent.getComponent(i) instanceof JLabel)
            {
                ((JLabel)jComponent.getComponent(i)).setForeground(enabled?Color.BLACK:Color.GRAY) ;
            }
            else if (jComponent.getComponent(i) instanceof JCheckBox
                    || jComponent.getComponent(i) instanceof JRadioButton)
            {
                ((JComponent)jComponent.getComponent(i)).setEnabled(enabled) ;                   
            }   
            else if (jComponent.getComponent(i) instanceof JButton){            	
            	((JButton)jComponent.getComponent(i)).setEnabled(enabled);
            }
            else if (jComponent.getComponent(i) instanceof Container
                    && jComponent.getComponent(i) instanceof JComponent)
            {
                enablePanel((JComponent)jComponent.getComponent(i), enabled);
            }                 
        }        
    }  
    
}
