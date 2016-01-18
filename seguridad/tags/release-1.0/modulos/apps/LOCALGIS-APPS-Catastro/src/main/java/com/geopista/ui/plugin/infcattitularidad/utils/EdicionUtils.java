package com.geopista.ui.plugin.infcattitularidad.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.ui.plugin.infcattitularidad.dialogs.FincaPanel;
import com.geopista.util.GeopistaFunctionUtils;
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
    
    public static void enablePanel(JComponent jComponent, boolean enabled)
    {
        for (int i=0; i<jComponent.getComponentCount(); i++)
        {        	
            if (jComponent.getComponent(i) instanceof JTextField
                    || jComponent.getComponent(i) instanceof JComboBox
                    || jComponent.getComponent(i) instanceof JTable)
            {
                ((JComponent)jComponent.getComponent(i)).setEnabled(enabled) ;
                ((JComponent)jComponent.getComponent(i)).setBackground(enabled?Color.WHITE:Color.WHITE) ;
            }                
            else if (jComponent.getComponent(i) instanceof JLabel)
            {
                ((JLabel)jComponent.getComponent(i)).setForeground(enabled?Color.BLACK:Color.GRAY) ;
            }
            else if (jComponent.getComponent(i) instanceof JCheckBox
                    || jComponent.getComponent(i) instanceof JRadioButton)
            {
                ((JComponent)jComponent.getComponent(i)).setEnabled(enabled) ;
                ((JComponent)jComponent.getComponent(i)).setBackground(enabled?Color.WHITE:Color.WHITE) ;                  
            }   
            else if(jComponent.getComponent(i) instanceof JButton
            		&& jComponent.getComponent(i).getName()!=null
            		&& jComponent.getComponent(i).getName().startsWith("_C")){
            	
            	JComponent panelDatos = null;
            	panelDatos = obtenerPanel(jComponent,i);
            	            	
            	if(isPanelEmpty((JComponent)panelDatos)){
            		((JButton)jComponent.getComponent(i)).setEnabled(false) ;
            	}
            	else{
            		((JButton)jComponent.getComponent(i)).setEnabled(true) ;
            	}
            }
            else if (jComponent.getComponent(i) instanceof JButton
                    && jComponent.getComponent(i).getName()!=null
                    && jComponent.getComponent(i).getName().startsWith("_"))
            {                
                if (jComponent.getComponent(i).getName().startsWith("_borrar")){
                	enabledButton(jComponent,i);
                }
                else{
                	((JButton)jComponent.getComponent(i)).setEnabled(enabled) ;   
                }
            }  
            else if (jComponent.getComponent(i) instanceof JButton){            	
            	((JButton)jComponent.getComponent(i)).setEnabled(enabled);
            }
            else if (jComponent.getComponent(i) instanceof Container
                    && jComponent.getComponent(i) instanceof JComponent)
            {
                enablePanel((JComponent)jComponent.getComponent(i), enabled);
                if (jComponent.getComponent(i) instanceof FincaPanel)
                ((FincaPanel)jComponent.getComponent(i)).setEditable(enabled);
            }                 
        }        
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
    
    private static void enabledButton(JComponent jComponent, int i) {
    	    	
    	if(jComponent.getComponent(i).getName().equals("_borrarfinca")){
    		if(((FincaPanel)jComponent.getParent().getParent()).getJPanelDatosFinca().getParent()!=null)
    			jComponent.getComponent(i).setEnabled(true);
    		else
    			jComponent.getComponent(i).setEnabled(false);
    	}
    	else if(jComponent.getComponent(i).getName().equals("_borrarbien")){
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosBienInmueble().getParent()!=null)
    			jComponent.getComponent(i).setEnabled(true);
    		else
    			jComponent.getComponent(i).setEnabled(false);
    	}
    	else if(jComponent.getComponent(i).getName().equals("_borrarlocal")){
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosLocal().getParent()!=null)
    			jComponent.getComponent(i).setEnabled(true);
    		else
    			jComponent.getComponent(i).setEnabled(false);
    	}
    	else if(jComponent.getComponent(i).getName().equals("_borrartitular")){
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosTitular().getParent()!=null)
    			jComponent.getComponent(i).setEnabled(true);
    		else
    			jComponent.getComponent(i).setEnabled(false);
    	}
    	else if(jComponent.getComponent(i).getName().equals("_borrarcultivo")){
    		if (jComponent.getParent()!=null && jComponent.getParent().getParent()!=null && jComponent.getParent().getParent().getParent() != null 
    				&& ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosCultivo()!=null)
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosCultivo().getParent()!=null)
    			jComponent.getComponent(i).setEnabled(true);
    		else
    			jComponent.getComponent(i).setEnabled(false);
    	}
    	return;
		
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
    
    public static JComponent obtenerPanel(JComponent jComponent, int i){
    	
    	JComponent panelDatos = null;
    	
    	if(jComponent.getComponent(i).getName().equals("_Cmasdatosfinca")){
    		panelDatos = ((FincaPanel)jComponent.getParent().getParent()).getJPanelDatosFinca();
    	}
    	else if(jComponent.getComponent(i).getName().equals("_Cmasdatosinmueble")){
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosBienInmueble().getParent()!=null){
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosBienInmueble();    		
    		}
    		else{
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJScrollPaneBienInmueble();
    		}
    	}
    	else if(jComponent.getComponent(i).getName().equals("_Cmasdatoslocales")){
    		if (jComponent.getParent()!=null && jComponent.getParent().getParent()!=null && jComponent.getParent().getParent().getParent()!=null && ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosLocal()!=null){
    			if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosLocal().getParent()!=null){
    				panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosLocal();
    			}
    			else{
    				panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJScrollPaneLocal();
    			}
    		}
    	}
    	else if(jComponent.getComponent(i).getName().equals("_Cmasdatostitular")){
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosTitular().getParent()!=null){
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosTitular();
    		}
    		else{
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJScrollPaneTitular();
    		}
    	}
    	else if(jComponent.getComponent(i).getName().equals("_Creparto") || jComponent.getComponent(i).getName().equals("_Cmasdatoscultivo")){
    		if (jComponent.getParent()!= null && jComponent.getParent().getParent() != null && 
    				jComponent.getParent().getParent().getParent() !=null 
    				&& ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosCultivo()!= null)
    		if(((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosCultivo().getParent()!=null){
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJPanelDatosCultivo();
    		}
    		else{
    			panelDatos = ((FincaPanel)jComponent.getParent().getParent().getParent()).getJScrollPaneCultivo();
    		}
    	}
    	return panelDatos;
    }
    
    public static boolean isPanelEmpty(JComponent jComponent)
    {
    	boolean isEmpty = true;
    	
    	if (jComponent != null){
    		for (int i=0; i<jComponent.getComponentCount(); i++)
    		{   
    			if (jComponent.getComponent(i) instanceof JTextField){
    				if (!((JTextField)jComponent.getComponent(i)).getText().equals(null)
    						&& !((JTextField)jComponent.getComponent(i)).getText().equals("0")
    						&& !((JTextField)jComponent.getComponent(i)).getText().equals("")
    						&& !((JTextField)jComponent.getComponent(i)).getText().equals(porDefinir)){
    					return false;
    				}
    			}
    			else if(jComponent.getComponent(i) instanceof JComboBox)                    
    			{
    				if(!(((JComboBox)jComponent.getComponent(i)).getSelectedIndex()==0)){
    					if(!(((JComboBox)jComponent.getComponent(i)).getSelectedItem()==null)){
    						if(!(((JComboBox)jComponent.getComponent(i)).getSelectedItem() instanceof ComunidadBienes)){
    							return false;            	
    						}
    					}
    				}
    			}
    			else if (jComponent.getComponent(i) instanceof JTable){
    				return true;
    			}

    			else if (jComponent.getComponent(i) instanceof Container
    					&& jComponent.getComponent(i) instanceof JComponent)
    			{
    				isEmpty = isPanelEmpty((JComponent)jComponent.getComponent(i));
    				if(!isEmpty){
    					return false;
    				}

    			}                 
    		}   
    	}
        return isEmpty;
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

    public static ConstruccionCatastro getConstruccionRepartos(ConstruccionCatastro cons, 
            ArrayList lstReparto, ArrayList lstBienesInmuebles, ArrayList lstConstrucciones)
    {
        
        //Dependiendo del tipo de reparto, busca en bienes inmuebles o en construcciones
        char modo=' ';
        try{
            modo = cons.getDatosEconomicos().getCodModalidadReparto().charAt(1);
        }catch(Exception e){}
        
        ArrayList lstPosibles = new ArrayList();
        if((lstReparto!=null)&&(!lstReparto.isEmpty())){
        	Iterator itRepartos = lstReparto.iterator();
        	while (itRepartos.hasNext())
        	{
        		RepartoCatastro rc = (RepartoCatastro)itRepartos.next();

        		String numOrdenConstruccion = GeopistaFunctionUtils.completarConCeros(cons.getNumOrdenConstruccion(), 4);
        		
        		//Se cogen solo los repartos que afecten a la construccion actual
        		if (rc.getIdOrigen() != null){
	        		if (rc.getIdOrigen().equals(cons.getRefParcela())
	        				&& rc.getNumOrdenConsRepartir() !=null
	        				&& rc.getNumOrdenConsRepartir().equals(numOrdenConstruccion))
	        		{                	        				
	        			lstPosibles.add(rc);
	        			cons.setLstRepartos(lstPosibles);
	        		}            
        		}
        	}
        }
        return cons;
    }
   

      
    private static ArrayList getParticionesConstrucciones(ReferenciaCatastral refCat, String numOrden, ArrayList lst)
    {
        Iterator it = lst.iterator();
        ArrayList lstPosibles = new ArrayList();
        Set hsPosibles = new HashSet();
        while (it.hasNext())
        {
            Object o = it.next();
            if (o instanceof BienInmuebleJuridico)
            {
                BienInmuebleJuridico bij = (BienInmuebleJuridico)o;
                if (bij.getBienInmueble().getIdBienInmueble().getParcelaCatastral().equals(refCat)
                        && bij.getBienInmueble().getIdBienInmueble().getNumCargo().equals(numOrden))
                    hsPosibles.add(bij);     
            }
            else if (o instanceof ConstruccionCatastro)
            {
                ConstruccionCatastro cons = (ConstruccionCatastro)o;
                if (cons.getRefParcela().equals(refCat)
                        && cons.getNumOrdenConstruccion().equals(numOrden))
                    hsPosibles.add(cons);     
            }        
        }        
        lstPosibles.addAll(hsPosibles);
        return lstPosibles;
    }
    
    private static ArrayList getParticionesCultivos(ReferenciaCatastral refCat, String numCargo, ArrayList lst)
    {
        Iterator it = lst.iterator();
        ArrayList lstPosibles = new ArrayList();
        while (it.hasNext())
        {           
            BienInmuebleJuridico bij = (BienInmuebleJuridico)it.next();
            if (bij.getBienInmueble().getIdBienInmueble().getNumCargo().equals(numCargo))
                lstPosibles.add(bij); 
        }
        
        return lstPosibles;
    }

    public static Cultivo getCultivosRepartos(Cultivo cultivo, 
            ArrayList lstReparto, ArrayList lstCargos)
    {
        ArrayList lstPosibles = new ArrayList();

        if(lstReparto!=null){
        	Iterator itRepartos = lstReparto.iterator();
        	while (itRepartos.hasNext())
        	{
        		RepartoCatastro rc = (RepartoCatastro)itRepartos.next();

        		if (rc.getIdOrigen().equals(cultivo.getIdCultivo().getParcelaCatastral())
        				&& rc.getCodSubparcelaElementoRepartir()!=null
        				&& rc.getCodSubparcelaElementoRepartir().equals(cultivo.getCodSubparcela())
        				&& rc.getCalifCatastralElementoRepartir()!=null
        				&& rc.getCalifCatastralElementoRepartir().equals(cultivo.getIdCultivo().getCalifCultivo()))
        		{
        			//Dependiendo del tipo de reparto, busca en bienes inmuebles o en construcciones
        			char modo=' ';
        			try{
        				modo = cultivo.getCodModalidadReparto().charAt(1);
        			}catch(Exception e){}

        			lstPosibles.add(rc);
        			cultivo.setLstRepartos(lstPosibles);
        		}            

        	}
        }
        return cultivo;
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
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
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
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
            }
        }
    }    
    public static void chequeaLongYCharCampoEdit(final JTextComponent comp, final int maxLong, JFrame parent)   
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            if(!((txt.charAt(0)>='A' && txt.charAt(0)<='Z') || (txt.charAt(0)>='a' && txt.charAt(0)<='z') || txt.charAt(0)=='ñ' || txt.charAt(0)=='Ñ')){
              JOptionPane.showMessageDialog(parent, I18N.get("RegistroExpedientes",
              "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
              "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg7"));
              UtilRegistroExp_LCGIII.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
        }
    }
    
    // checque que en el campo  solo se puedan intruducir letra y el guion.
    public static void chequeaLongYCharCampoEditYGuion(final JTextComponent comp, final int maxLong, JFrame parent)   
    {
        String txt= comp.getText();
        if(!txt.equals(""))
        {
            if(!((txt.charAt(0)>='A' && txt.charAt(0)<='Z') || (txt.charAt(0)>='a' && txt.charAt(0)<='z') || txt.charAt(0)=='ñ' || txt.charAt(0)=='Ñ' || txt.charAt(0)=='-')){
              JOptionPane.showMessageDialog(parent, I18N.get("RegistroExpedientes",
              "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
              "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg7"));
              UtilRegistroExp_LCGIII.retrocedeCaracter(comp,txt.length()-1);
            }
            if(txt.length()>maxLong )
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
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
}
