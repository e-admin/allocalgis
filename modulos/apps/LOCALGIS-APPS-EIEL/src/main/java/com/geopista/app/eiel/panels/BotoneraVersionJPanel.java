/**
 * BotoneraVersionJPanel.java
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
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;

public class BotoneraVersionJPanel extends JPanel {
    
    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;
    
    private JButton jButtonVerVersion = null;
    private JButton jButtonFijarVersion = null;
   
    public BotoneraVersionJPanel() {
    	Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        initComponents();
    }
    

    
    public JButton getJButtonVerVersion(){
    	
    	if (jButtonVerVersion == null){
    		
    		jButtonVerVersion = new JButton();
    		jButtonVerVersion.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonverversion"));
    		jButtonVerVersion.setEnabled(false);
    		jButtonVerVersion.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    verVersionJButtonActionPerformed();
                }
            });
    	}
    	return jButtonVerVersion;
    	
    }    
    
    
    public JButton getJButtonFijarVersion(){
    	
    	if (jButtonFijarVersion == null){
    		
    		jButtonFijarVersion = new JButton();
    		jButtonFijarVersion.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonfijarversion"));
    		jButtonFijarVersion.setEnabled(false);
    		jButtonFijarVersion.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    fijarVersionJButtonActionPerformed();
                }
            });
    	}
    	return jButtonFijarVersion;
    }
    
    

    private void initComponents() {
    	
    	this.setLayout(new GridBagLayout());
    	
    	this.add(getJButtonVerVersion(), 
        		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	
        //Si tiene permisos para ver la versión
        if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VERSION_UPDATE)){
        	this.add(getJButtonFijarVersion(), new GridBagConstraints(1,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        }

    }

    private void fijarVersionJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION;
        fireActionPerformed();
    }


    private void verVersionJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_VER_VERSION;
        fireActionPerformed();
    }

    //private static AppContext aplicacion= (AppContext) AppContext.getApplicationContext();
    private static String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
    
    public void setEnabled(boolean b){
    	getJButtonVerVersion().setEnabled(b);
    	getJButtonFijarVersion().setEnabled(b);
    }


    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    public String getBotonPressed(){
        return botonPressed;
    }

    public void setBotonPressed(String s){
        botonPressed= s;
    }

    
    
}
