/**
 * SelectorMunicipioJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.vividsolutions.jump.I18N;

public class SelectorMunicipioJPanel extends JPanel {
    
	private AppContext aplicacion;
	private ArrayList actionListeners= new ArrayList();

    private JLabel municipioJLabel;
	private JComboBox municipioJCBox;
	
	private JLabel nucleoJLabel;
	private JComboBox nucleoJCBox;

  
    public SelectorMunicipioJPanel() {
    	aplicacion= (AppContext) AppContext.getApplicationContext();
    	
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
				fireActionPerformed("MUNICIPIO");
				getNucleosMunicipiosEIEL();
			}
		});
	   add(municipioJLabel, 
               new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                       GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                       new Insets(5, 5, 5, 5), 0, 0));           
       
       add(municipioJCBox, 
               new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                       GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                       new Insets(5, 5, 5, 5), 0, 0));
       
       
       // Añadimos el filtro por nucleo
       nucleoJLabel = new javax.swing.JLabel();
       nucleoJLabel.setText("Nucleo");

		nucleoJCBox=new JComboBox();                    
		nucleoJCBox.setRenderer(new UbicacionListCellRenderer());


		nucleoJCBox.addActionListener(new java.awt.event.ActionListener(){
		       public void actionPerformed(ActionEvent e){
					fireActionPerformed("NUCLEO");
					}
				});
	   add(nucleoJLabel, 
	          new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
	                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	                  new Insets(5, 5, 5, 5), 0, 0));           
	  
	   add(nucleoJCBox, 
	          new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
	                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	                  new Insets(5, 5, 5, 5), 0, 0));

	   //Cargamos los nucleos del municipio por defecto
	   getNucleosMunicipiosEIEL();
    }
    
    public void reload(){
   	 ArrayList<ActionListener> listeners=disableEvents();
   	municipioJCBox.removeAllItems();
   	
   	ArrayList array=getMunicipiosEIEL();
   	Iterator it=array.iterator();
		 while (it.hasNext()){
			 LCGMunicipioEIEL municipio=(LCGMunicipioEIEL)it.next();
			 municipioJCBox.addItem(municipio);
		 }
		 
		 enableEvents(listeners);
   }
    
    /**
     * Obtiene la lista de municipios
     * @return
     */
    private ArrayList getMunicipiosEIEL(){
    	return getMunicipiosEIEL(true);
    }
    
    private ArrayList getMunicipiosEIEL(boolean cache){

    	ArrayList listaMunicipios=null;
    	
    	if (aplicacion.getBlackboard().get(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS)!=null && cache){
    		listaMunicipios=(ArrayList)aplicacion.getBlackboard().get(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS);
    	}
    	else{    	
    		Collection<Object> c=null;
    		Collection<Object> c1=null;

	        try {
				c = InitEIEL.clienteLocalGISEIEL.getMunicipiosEIEL();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        listaMunicipios=new ArrayList();
	    	listaMunicipios.add(new LCGMunicipioEIEL(0,""));
	    	listaMunicipios.addAll(c);
	    	
	    	
	    	//Añadimos los municipios con los que tengamos algun tipo de relacion porque usamos
	    	//sus infraestructuras y ademas no pertenecen a nuestra provincia.
	    	try {
				c1 = InitEIEL.clienteLocalGISEIEL.getMunicipiosEIELOtraProvincia();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	listaMunicipios.add(new LCGMunicipioEIEL(-1,"---------------"));
	    	listaMunicipios.addAll(c1);
	    	
	        aplicacion.getBlackboard().put(ConstantesLocalGISEIEL.ESTRUCTURA_MUNICIPIOS,listaMunicipios);
    	}
    	return listaMunicipios;
    	
    	
    }
    
    /**
     * Obtiene la lista de municipios
     * @return
     */
    private void getNucleosMunicipiosEIEL(){

    	try {
			ArrayList listaNucleos=null;
			
			LCGMunicipioEIEL lcgMunicipio=null;			
			if (getMunicipioSeleccionado()==null || ((LCGMunicipioEIEL)getMunicipioSeleccionado()).getIdMunicipio()==0){
				String idMunicipioShort=String.valueOf(AppContext.getIdMunicipio()).substring(2);
				lcgMunicipio=new LCGMunicipioEIEL(AppContext.getIdMunicipio(),"");
			}
			else{
				lcgMunicipio=(LCGMunicipioEIEL)getMunicipioSeleccionado();
			}
			    	
			 HashMap map=(HashMap)aplicacion.getBlackboard().get("HASH_NUCLEOS_POR_MUNICIPIO");
			 
			 String idMunicipioShort=String.valueOf(lcgMunicipio.getIdMunicipio()).substring(2);
			 listaNucleos=(ArrayList)map.get(idMunicipioShort);
			 
			 if (listaNucleos!=null){
				 ArrayList<ActionListener> listeners=disableEventsNucleo();
				 nucleoJCBox.removeAllItems();
				 //Elemento temporal para que al principio no este seleccionado ninguno
				 LCGNucleoEIEL temp=new LCGNucleoEIEL();
				 temp.setCodentidad("0");
				 nucleoJCBox.addItem(temp);
				 Iterator it=listaNucleos.iterator();
				 while (it.hasNext()){
					 LCGNucleoEIEL nucleo=(LCGNucleoEIEL)it.next();
					 nucleoJCBox.addItem(nucleo);
				 }
				 enableEventsNucleo(listeners);
				 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 
    	
    	
    }
    
    public Object getMunicipioSeleccionado(){
       return municipioJCBox.getSelectedItem();
    }

    public Object getNucleoSeleccionado(){
        return nucleoJCBox.getSelectedItem();
     }

    public void setEnabled(boolean b){
    	
    	if (!b){
    		if (!municipioJCBox.isEnabled() && !nucleoJCBox.isEnabled())
    			return;
    	}
    	else{
    		if (municipioJCBox.isEnabled() && nucleoJCBox.isEnabled())
    			return;
    	}
    	
    	ArrayList<ActionListener> listeners=disableEvents();
    	ArrayList<ActionListener> listeners2=disableEventsNucleo();
    	if (!b){
    		municipioJCBox.setSelectedIndex(0); 
    		if (nucleoJCBox.getItemCount()>0)
    			nucleoJCBox.setSelectedIndex(0);   
    	}
        municipioJCBox.setEnabled(b);
        nucleoJCBox.setEnabled(b);
        
        enableEvents(listeners);
        enableEventsNucleo(listeners2);
    }
    
    public void setEnabledMunicipio(boolean b){
    	ArrayList<ActionListener> listeners=disableEvents();
    	if (!b){
    		municipioJCBox.setSelectedIndex(0);    	
    	}
        municipioJCBox.setEnabled(b);
        
        enableEvents(listeners);
    }
    
    public void setEnabledNucleo(boolean b){
    	ArrayList<ActionListener> listeners=disableEventsNucleo();
    	if (!b){
    		nucleoJCBox.setSelectedIndex(0);
    	}
        nucleoJCBox.setEnabled(b);
        
        enableEventsNucleo(listeners);
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
    
    private ArrayList<ActionListener> disableEventsNucleo(){
    	ActionListener[] listeners = nucleoJCBox.getActionListeners();
    	ArrayList<ActionListener> copiaListeners=new ArrayList<ActionListener>();
    	
    	for(ActionListener al:listeners) {
    		copiaListeners.add(al);
    		nucleoJCBox.removeActionListener(al);
    	}
    	return copiaListeners;
    }
    
    private void enableEventsNucleo(ArrayList<ActionListener> listeners){

    	Iterator<ActionListener> it=listeners.iterator();
    	while (it.hasNext()){
    		ActionListener al=it.next();
    		nucleoJCBox.addActionListener(al);
    	}
    }
    
    

    private void fireActionPerformed(String command) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, command));
        }
    }
    
}
