/**
 * FiltroVersionJPanel.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.ui.components.DateField;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;

//public class FiltroVersionJPanel extends JPanel {
public class FiltroVersionJPanel extends JPanel implements PropertyChangeListener{

    
    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;
    
//	private JComboBox operadorJCBox;
	private JLabel jLabelFechaInicio;
	private JLabel jLabelHoraInicio;	
	private JLabel jLabelFechaFin;
	private JLabel jLabelHoraFin;	
	private JLabel jLabelAccion;
//	private JLabel jLabelUsuario;
	private DateField jTextFieldFechaInicio;
	private JFormattedTextField jTextHoraInicio;
	private DateField jTextFieldFechaFin;
	private JFormattedTextField jTextHoraFin;
	private JComboBox jComboAccion;
//	private JComboBox jComboUsuario;
	private JLabel jLabelExplicacion;	
	private JLabel jLabelExplicacion2;	
	
    private JButton jButtonFiltrar= null;
	private JPanel panelBotones;
	
	
   
    public FiltroVersionJPanel() {
    	Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        initComponents();
    }
     
    
    public DateField getJTextFieldFechaInicio(){
    	if (jTextFieldFechaInicio  == null){
    		jTextFieldFechaInicio  = new DateField((java.util.Date) null, 3);  
    		jTextFieldFechaInicio.setDate(null);
    	}
    	return jTextFieldFechaInicio;
    }
    public JFormattedTextField getJTextFieldHoraInicio(){
    	if (jTextHoraInicio  == null){
	    	try{
	    	    MaskFormatter mascara = new MaskFormatter("##:##:##");
	    	    jTextHoraInicio = new JFormattedTextField(mascara);
	    	    jTextHoraInicio.setText("12:00:00");
	    	    jTextHoraInicio.addPropertyChangeListener("value", this);
	    	}catch(ParseException e1){
	                JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e1.getMessage());
	    	}
    	}
    	return jTextHoraInicio;
    }
    public DateField getJTextFieldFechaFin(){
    	if (jTextFieldFechaFin == null){
    		jTextFieldFechaFin  = new DateField((java.util.Date) null, 3);  
    		jTextFieldFechaFin.setDate(null);
    	}
    	return jTextFieldFechaFin;
    }
    
    public JFormattedTextField getJTextFieldHoraFin(){
    	if (jTextHoraFin== null){
	    	try{
	    	    MaskFormatter mascara = new MaskFormatter("##:##:##");
	    	    jTextHoraFin = new JFormattedTextField(mascara);
	    	    jTextHoraFin.setText("12:00:00");
	    	    jTextHoraFin.addPropertyChangeListener("value", this);
//	    	    Dimension d = new Dimension(100,20);
//	    	    jTextHora.setPreferredSize(d);
//	    	    jTextHora.setSize(d);
	    	}catch(ParseException e1){
	                JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e1.getMessage());
	    	}
    	}
    	return jTextHoraFin;
    }
    
    public JComboBox getJComboAccion(){
    	if (jComboAccion  == null){
    		String[] acciones = { 
    				"", 
    				ConstantesLocalGISEIEL.OPERACION_VERSIONADO_INSERT,
    				ConstantesLocalGISEIEL.OPERACION_VERSIONADO_UPDATE, 
    				ConstantesLocalGISEIEL.OPERACION_VERSIONADO_DELETE};

    		jComboAccion = new JComboBox(acciones);
    		
    	}
    	return jComboAccion;
    }
    

    
    public JButton getJButtonFiltrar(){
    	
    	if (jButtonFiltrar == null){
    		
    		jButtonFiltrar = new JButton();
    		jButtonFiltrar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonfiltroversion"));
    		jButtonFiltrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filtroJButtonActionPerformed();
                }
            });
    	}
    	return jButtonFiltrar;
    	
    }  
	
    
    public JPanel getJPanelBotones(){
    	if (panelBotones == null){
    	  panelBotones = new JPanel(new GridBagLayout());
          jLabelFechaInicio = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelFechaInicio"), JLabel.CENTER);
          jLabelHoraInicio = new JLabel("Hora Inicio", JLabel.CENTER);
          jLabelFechaFin= new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelFechaFin"), JLabel.CENTER);
          jLabelHoraFin = new JLabel("Hora fin", JLabel.CENTER);
//          jLabelHora = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelHora"), JLabel.CENTER);
          jLabelAccion = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelAccion"), JLabel.CENTER);
//          jLabelUsuario = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelUsuario"), JLabel.CENTER);
      	
          panelBotones.add(jLabelFechaInicio, new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
          panelBotones.add(getJTextFieldFechaInicio(), new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
          
          panelBotones.add(jLabelHoraInicio, new GridBagConstraints(0,1,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
          panelBotones.add(getJTextFieldHoraInicio(), new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));          
          
          panelBotones.add(jLabelFechaFin, new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
          panelBotones.add(getJTextFieldFechaFin(), new GridBagConstraints(1,2,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
          panelBotones.add(jLabelHoraFin, new GridBagConstraints(0,3,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
          panelBotones.add(getJTextFieldHoraFin(), new GridBagConstraints(1,3,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));          
          panelBotones.add(jLabelAccion, new GridBagConstraints(0,4,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
          panelBotones.add(getJComboAccion(), new GridBagConstraints(1,4,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));

//          panelBotones.add(jLabelUsuario, new GridBagConstraints(0,2,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
//          panelBotones.add(getJComboUsuario(), new GridBagConstraints(1,2,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
          
    	}
    	return panelBotones;
    }
    private void filtroJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_FILTRAR_VERSION;
        fireActionPerformed();
    }
    
    private void initComponents() {
    	
    	this.setLayout(new GridBagLayout());
//    	this.setBorder(BorderFactory.createTitledBorder(null, I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.Title"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
    	
        this.add(getJPanelBotones() , new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
    	this.add(getJButtonFiltrar(), new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(2,0,0,5),0,0));	
        

    }

    
//    private void populateJCBox(ArrayList<String> operadores){
//    	operadorJCBox.removeAllItems();
//        Iterator<String> it=operadores.iterator();
//        while (it.hasNext()){
//        	String operador=it.next();
//        	operadorJCBox.addItem(operador);
//        }        
//    }
    
//    private void fechaJButtonActionPerformed() {
//        botonPressed= ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION;
//        fireActionPerformed();
//    }

    

//    private void verVersionJButtonActionPerformed() {
//        botonPressed= ConstantesLocalGISEIEL.OPERACION_VER_VERSION;
//        fireActionPerformed();
//    }

    private static AppContext aplicacion= (AppContext) AppContext.getApplicationContext();
    private static String locale = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
    
    public void setEnabled(boolean b){
//    	getJButtonVerVersion().setEnabled(b);
//    	getJButtonFijarVersion().setEnabled(b);
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


	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		String valor;
		int horas;
		int minutos;
		int segundos;
		if (source == jTextHoraInicio) {
			valor = ((String) jTextHoraInicio.getValue());
			if (valor!=null){
				horas = Integer.valueOf(valor.substring(0, 2));
				minutos = Integer.valueOf(valor.substring(3, 5));
				segundos = Integer.valueOf(valor.substring(6, 8));
				if (!((0 <= horas && horas < 24) && (0 <= minutos && minutos < 60) && (0 <= segundos && segundos < 60))) {
					jTextHoraInicio.setValue("12:00:00");
				}				
			}
		}
		else  if (source == jTextHoraFin) {
			valor = ((String) jTextHoraFin.getValue());
			if (valor!=null){
			horas = Integer.valueOf(valor.substring(0, 2));
			minutos = Integer.valueOf(valor.substring(3, 5));
			segundos = Integer.valueOf(valor.substring(6, 8));
			if (!((0 <= horas && horas < 24) && (0 <= minutos && minutos < 60) && (0 <= segundos && segundos < 60))) {
					jTextHoraFin.setValue("12:00:00");
				}				
			}

		}
	}

    
}
