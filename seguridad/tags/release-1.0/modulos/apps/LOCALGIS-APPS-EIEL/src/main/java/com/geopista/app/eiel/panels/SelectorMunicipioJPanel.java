/*
 * BotoneraJPanel.java
 *
 * Created on 7 de julio de 2006, 9:48
 */

package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.filter.LCGFilter;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.vividsolutions.jump.I18N;

public class SelectorMunicipioJPanel extends JPanel {
    
	private AppContext aplicacion;
	private ArrayList actionListeners= new ArrayList();

    private JLabel municipioJLabel;
	private JComboBox municipioJCBox;

    
    private LocalGISEIELClient eielClient;    

   
    public SelectorMunicipioJPanel() {
    	aplicacion= (AppContext) AppContext.getApplicationContext();
    	
    	eielClient= new LocalGISEIELClient(aplicacion.getString("geopista.conexion.servidorurl")+"EIEL/EIELServlet");
    	Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        initComponents();
        
    }

    private void initComponents() {
    	
    	this.setLayout(new GridBagLayout());
    	
    	municipioJLabel = new javax.swing.JLabel();
    	municipioJLabel.setText("Municipio Alternativo");
 
    	municipioJCBox=new JComboBox(getMunicipiosEIEL().toArray());                    
    	municipioJCBox.setRenderer(new UbicacionListCellRenderer());
    	
    	
    	municipioJCBox.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				fireActionPerformed();
			}
		});
	   add(municipioJLabel, 
               new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                       GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                       new Insets(5, 5, 5, 5), 0, 0));           
       
       add(municipioJCBox, 
               new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                       GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                       new Insets(5, 5, 5, 5), 0, 0));

    }
    
    /**
     * Obtiene la lista de municipios
     * @return
     */
    private ArrayList getMunicipiosEIEL(){

    	ArrayList listaMunicipios=null;
    	
    	if (aplicacion.getBlackboard().get(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS)!=null){
    		listaMunicipios=(ArrayList)aplicacion.getBlackboard().get(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS);
    	}
    	else{    	
    		Collection<Object> c=null;
	        try {
				c = eielClient.getMunicipiosEIEL();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        listaMunicipios=new ArrayList();
	    	listaMunicipios.add(new LCGMunicipioEIEL(0,""));
	    	listaMunicipios.addAll(c);
	    	
	        aplicacion.getBlackboard().put(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS,listaMunicipios);
    	}
    	return listaMunicipios;
    	
    	
    }
    
    public Object getMunicipioSeleccionado(){
       return municipioJCBox.getSelectedItem();
    }
    
    public void setEnabled(boolean b){
    	ArrayList<ActionListener> listeners=disableEvents();
    	if (!b)
    		municipioJCBox.setSelectedIndex(0);
        municipioJCBox.setEnabled(b);
        
        enableEvents(listeners);
    }


    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }
    
    
    private ArrayList<ActionListener> disableEvents(){
    	ActionListener[] listeners = municipioJCBox.getActionListeners();
    	ArrayList<ActionListener> copiaListeners=new ArrayList<ActionListener>();
    	
    	for(ActionListener al:listeners) {
    		copiaListeners.add(al);
    		municipioJCBox.removeActionListener(al);
    	}
    	return copiaListeners;
    }
    
    private void enableEvents(ArrayList<ActionListener> listeners){

    	Iterator<ActionListener> it=listeners.iterator();
    	while (it.hasNext()){
    		ActionListener al=it.next();
    		municipioJCBox.addActionListener(al);
    	}
    }
    
    

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }
    
}
