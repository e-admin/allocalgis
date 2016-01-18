/**
 * VertederoNucleosPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoVertedero;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.models.NucleosVertederoEIELTableModel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.components.DateField;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;


public class VertederoNucleosPanel extends JPanel implements FeatureExtendedPanel {
    
	private static int DIM_X = 500;
	private static int DIM_Y = 450;
	
	ArrayList lstNucleos = new ArrayList();
	
	private Blackboard Identificadores = AppContext.getApplicationContext().getBlackboard();
	
	private JPanel jPanelIdentVertedero = null;
	private JPanel jPanelNucleos = null;
	private JPanel jPanelIdentNucleo = null;
	private JPanel jPanelInfoNucleo = null;
	private JPanel jPanelRevisionNucleo = null;
	private JPanel jPanelBotonera = null;
	private JLabel jLabelClave;
	private JLabel jLabelCodProvVertedero;
	private JLabel jLabelCodMunicVertedero;
	private JLabel jLabelCodOrden;
	private TextField jTextFieldClave;
	private JComboBox jComboBoxProvinciaVertedero;
	private JComboBox jComboBoxMunicipioVertedero;
	private TextField jTextFieldCodOrden;
	private JLabel jLabelCodProvNucleo;
	private JLabel jLabelCodMunicNucleo;
	private JLabel jLabelEntSing;
	private JLabel jLabelNucleo;
	private JComboBox jComboBoxProvinciaNucleo;
	private JComboBox jComboBoxMunicipioNucleo;
	private JComboBox jComboBoxEntidad;
	private JComboBox jComboBoxNucleo;
	private JLabel jLabelObservaciones;
	private JLabel jLabelFechaAlta;
	private TextField jTextFieldObserv;
	private DateField jTextFieldFechaAlta;
	private JLabel jLabelFechaRevision;
	private JLabel jLabelEstadoRevision;
	private TextField jTextFieldFechaRevision;
	private ComboBoxEstructuras jComboBoxEstadoRevision = null;
	private JButton jButtonAniadir;
	private JButton jButtonEliminar;
	private JButton jButtonLimpiar;
	private JPanel jPanelListaNucleos;
	private JScrollPane jScrollPaneListaNucleos;
	private JTable jTableListaNucleos;
	private Object tableListaNucleosModel;
	
	private String idMunicipioSelected;
    
    
    
    public VertederoNucleosPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
        
    private JPanel getJPanelNucleos(){
    	
    	if (jPanelNucleos == null){
    		
    		jPanelNucleos = new JPanel();
    		
    		jPanelNucleos = new JPanel(new GridBagLayout());
    		jPanelNucleos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.nucleos"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
    		jPanelNucleos.add(getJPanelIdentNucleo(), 
            		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        	
    		jPanelNucleos.add(getJPanelInfoNucleo(), 
            		new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelNucleos.add(getJPanelRevision(), 
            		new GridBagConstraints(0,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelNucleos.add(getJPanelBotonera(), 
            		new GridBagConstraints(0,3,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelNucleos.add(getJPanelListaNucleos(), 
            		new GridBagConstraints(0,4,1,1,0.01, 0.01,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	}
    	return jPanelNucleos;
    }
    
    private JPanel getJPanelListaNucleos(){
    	
    	if (jPanelListaNucleos == null){
    		
    		jPanelListaNucleos = new JPanel();
    		jPanelListaNucleos.setLayout(new GridBagLayout());
    		jPanelListaNucleos.setSize(10,10);
    		
    		jPanelListaNucleos.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listnucleos"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
    		
    		jPanelListaNucleos.add(getJScrollPaneListaNucleos(), 
    		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	}
    	return jPanelListaNucleos;
    }
    
    public JScrollPane getJScrollPaneListaNucleos(){

    	if (jScrollPaneListaNucleos == null){

    		jScrollPaneListaNucleos = new JScrollPane();
    		jScrollPaneListaNucleos.setViewportView(getJTableListaNucleos());
    		jScrollPaneListaNucleos.setSize(10,10);

    	}
    	return jScrollPaneListaNucleos;
    }
    
    public JTable getJTableListaNucleos()
    {
    	if (jTableListaNucleos  == null)
    	{
    		jTableListaNucleos   = new JTable();

    		tableListaNucleosModel  = new NucleosVertederoEIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaNucleosModel);
    		tblSorted.setTableHeader(jTableListaNucleos.getTableHeader());
    		jTableListaNucleos.setModel(tblSorted);
    		jTableListaNucleos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaNucleos.setCellSelectionEnabled(false);
    		jTableListaNucleos.setColumnSelectionAllowed(false);
    		jTableListaNucleos.setRowSelectionAllowed(true);
    		jTableListaNucleos.getTableHeader().setReorderingAllowed(false);
    		//Dimension d = jTableListaNucleos.getPreferredSize();
    		// Make changes to [i]d[/i] if you like...
    		jTableListaNucleos.setPreferredScrollableViewportSize(new Dimension(100,100));

    		jTableListaNucleos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		
    		((NucleosVertederoEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);

    	}
    	return jTableListaNucleos;
    }
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosVertederoEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoVertedero){
    		
    		loadNucleoVertedero((NucleoVertedero)selectedItem);
    	}    	
    }
    
    private void loadNucleoVertedero(NucleoVertedero nucleoVertedero){
    	
    	if (nucleoVertedero.getCodProvNucleo()!=null){
        	jComboBoxProvinciaNucleo.setSelectedIndex(
        		provinciaIndexSeleccionar(nucleoVertedero.getCodProvNucleo())
        			);
        }
        else{
        	jComboBoxProvinciaNucleo.setSelectedIndex(0);
        }
        
        if (nucleoVertedero.getCodMunicNucleo() != null){
        	jComboBoxMunicipioNucleo.setSelectedIndex(
        			municipioIndexSeleccionar(nucleoVertedero.getCodMunicNucleo())
        	);
        	
        	
        }
        else{
        	jComboBoxMunicipioNucleo.setSelectedIndex(0);
        }
        
        if (nucleoVertedero.getCodEntNucleo() != null){
        	jComboBoxEntidad.setSelectedIndex(
        			entidadesSingularesIndexSeleccionar(nucleoVertedero.getCodEntNucleo())
        	);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleoVertedero.getCodPoblNucleo() != null){
        	jComboBoxNucleo.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(nucleoVertedero.getCodPoblNucleo())
        			);
        	
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(0);
        }

        
       
        
        if (nucleoVertedero.getObservaciones() != null){
    		jTextFieldObserv.setText(nucleoVertedero.getObservaciones());
    	}
    	else{
    		jTextFieldObserv.setText("");
    	}
       
        if (nucleoVertedero.getFechaAlta() != null){
    		jTextFieldFechaAlta.setDate(nucleoVertedero.getFechaAlta());
    	}
    	else{
    		jTextFieldFechaAlta.setDate(null);
    	}
        
        if (nucleoVertedero.getFechaRevision() != null && nucleoVertedero.getFechaRevision().equals( new java.util.Date()) ){
    		jTextFieldFechaRevision.setText(nucleoVertedero.getFechaRevision().toString());
    	}
    	else{
    	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
    		jTextFieldFechaRevision.setText(datetime);
    	}
        
        if (nucleoVertedero.getEstadoRevision() != null){
        	jComboBoxEstadoRevision.setSelectedPatron(nucleoVertedero.getEstadoRevision().toString());
    	}
    	else{
    		jComboBoxEstadoRevision.setSelectedIndex(0);
    	}
    	
    }
    
    private void reset(){

    	
    	jComboBoxProvinciaNucleo.setSelectedIndex(0);    	
    	jTextFieldObserv.setText("");
    	jTextFieldFechaAlta.setDate(null);
    	jTextFieldFechaRevision.setText("");
    	jComboBoxEstadoRevision.setSelectedIndex(0);

    }
    
    private JPanel getJPanelBotonera(){
    	
    	if (jPanelBotonera == null){
    		
    		jPanelBotonera = new JPanel();
    		jPanelBotonera.setLayout(new GridBagLayout());
    		
    		jPanelBotonera.add(getJButtonAniadir(), 
            		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
        	
    		jPanelBotonera.add(getJButtonEliminar(), 
            		new GridBagConstraints(1,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
        	
    		jPanelBotonera.add(getJButtonLimpiar(), 
            		new GridBagConstraints(2,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelBotonera;
    }
    
    public JButton getJButtonAniadir(){
    	
    	if (jButtonAniadir == null){
    		
    		jButtonAniadir = new JButton();
    		jButtonAniadir.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttonsave"));
    		jButtonAniadir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    annadirNucleo();
                }
            });
    	}
    	return jButtonAniadir;
    	
    }
    
    private void annadirNucleo(){
    	
    	EdicionOperations operations = new EdicionOperations();
    	
    	if(!datosMinimosYCorrectos())
    	{
    		JOptionPane.showMessageDialog(this,I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
    				JOptionPane.ERROR_MESSAGE);
    		
    	}
    	else{
    		operations.saveNucleoVertedero(getNucleoVertedero());
        	enter();
    	}
    	
    }
    
    private void eliminarNucleoVertedero(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosVertederoEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoVertedero){
    		
    		EdicionOperations operations = new EdicionOperations();
    		operations.deleteNucleoVertedero((NucleoVertedero)selectedItem);
    		reset();
    		enter();
    	}  
    }
    
    public boolean datosMinimosYCorrectos()
    {

        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldCodOrden.getText()!=null && !jTextFieldCodOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvinciaVertedero!=null && jComboBoxProvinciaVertedero.getSelectedItem()!=null && jComboBoxProvinciaVertedero.getSelectedIndex()>0) &&
        (jComboBoxProvinciaNucleo!=null && jComboBoxProvinciaNucleo.getSelectedItem()!=null && jComboBoxProvinciaNucleo.getSelectedIndex()>0) &&        
        (jComboBoxMunicipioVertedero!=null && jComboBoxMunicipioVertedero.getSelectedItem()!=null && jComboBoxMunicipioVertedero.getSelectedIndex()>0) && 
        (jComboBoxMunicipioNucleo!=null && jComboBoxMunicipioNucleo.getSelectedItem()!=null && jComboBoxMunicipioNucleo.getSelectedIndex()>0) &&
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&  
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0)); 
        
    }
    
    private NucleoVertedero getNucleoVertedero(){
    	
    	NucleoVertedero nucleoVertedero = new NucleoVertedero();
    	
    	nucleoVertedero.setCodProvVertedero((String) jComboBoxProvinciaVertedero.getSelectedItem());
    	nucleoVertedero.setCodMunicVertedero((String) jComboBoxMunicipioVertedero.getSelectedItem());
        
        if (jTextFieldClave.getText()!=null){
        	nucleoVertedero.setClaveVertedero(jTextFieldClave.getText());
        }
        
        if (jTextFieldCodOrden.getText()!=null){
        	nucleoVertedero.setCodOrdenVertedero(jTextFieldCodOrden.getText());
        }
        
        nucleoVertedero.setCodProvNucleo(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
        nucleoVertedero.setCodMunicNucleo(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
        nucleoVertedero.setCodEntNucleo(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
        nucleoVertedero.setCodPoblNucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
        
        if (jTextFieldObserv.getText()!=null){
        	nucleoVertedero.setObservaciones(jTextFieldObserv.getText());
        }
        
       
        if (jTextFieldFechaAlta.getDate()!=null && !jTextFieldFechaAlta.getDate().toString().equals("")){
        	java.sql.Date sqlDate = new java.sql.Date(
        			getJTextFieldFechaAlta().getDate().getYear(),
        			getJTextFieldFechaAlta().getDate().getMonth(),
        			getJTextFieldFechaAlta().getDate().getDate()
        			);
        	
        	nucleoVertedero.setFechaAlta(sqlDate);
        }  
        else{
        	nucleoVertedero.setFechaAlta(null);
        }
        
        if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
        	String fechas=jTextFieldFechaRevision.getText();
        	String anio=fechas.substring(0,4);
        	String mes=fechas.substring(5,7);
        	String dia=fechas.substring(8,10);
        	
        	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
        	nucleoVertedero.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

        }  
        else{
        	nucleoVertedero.setFechaRevision(null);
        }
        
        if (jComboBoxEstadoRevision.getSelectedPatron()!=null)  
        	nucleoVertedero.setEstadoRevision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
  
    	
    	return nucleoVertedero;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarNucleoVertedero();
                }
            });
    	}
    	return jButtonEliminar;
    }
    
    public JButton getJButtonLimpiar(){
    	
    	if (jButtonLimpiar == null){
    		
    		jButtonLimpiar = new JButton();
    		jButtonLimpiar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttonclean"));
    		jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    reset();
                }
            });
    	}
    	return jButtonLimpiar;
    }
    
    private JPanel getJPanelRevision(){
    	
    	if (jPanelRevisionNucleo == null){
    		
    		jPanelRevisionNucleo = new JPanel();
    		jPanelRevisionNucleo.setLayout(new GridBagLayout());
    		
    		jPanelRevisionNucleo.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revisionnucleo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
    		
    		jLabelFechaRevision  = new JLabel("", JLabel.CENTER); 
    		jLabelFechaRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            
            jLabelEstadoRevision  = new JLabel("", JLabel.CENTER); 
            jLabelEstadoRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
                        
            jPanelRevisionNucleo.add(jLabelFechaRevision,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevisionNucleo.add(getJTextFieldFechaRevision(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelRevisionNucleo.add(jLabelEstadoRevision, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelRevisionNucleo.add(getjComboBoxEstadoRevision(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
    		
    	}
    	return jPanelRevisionNucleo;
    }
    
    private JTextField getJTextFieldFechaRevision()
    {

    	if (jTextFieldFechaRevision  == null)
    	{
    		jTextFieldFechaRevision  = new TextField();    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		java.util.Date date = new java.util.Date();
    		String datetime = dateFormat.format(date);
    		jTextFieldFechaRevision.setText(datetime);

    	}
    	return jTextFieldFechaRevision;

    }
    
    private ComboBoxEstructuras getjComboBoxEstadoRevision()
    { 
        if (jComboBoxEstadoRevision == null)
        {
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            jComboBoxEstadoRevision = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, Identificadores.get(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES").toString(), true);
        
            jComboBoxEstadoRevision.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstadoRevision;        
    }
    
    private JPanel getJPanelInfoNucleo(){
    	
    	if (jPanelInfoNucleo == null){
    		
    		jPanelInfoNucleo = new JPanel();
    		jPanelInfoNucleo.setLayout(new GridBagLayout());
    		
    		jPanelInfoNucleo.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.infonucleo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
    		
    		jLabelObservaciones = new JLabel("", JLabel.CENTER); 
    		jLabelObservaciones.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ")); 
            
            jLabelFechaAlta = new JLabel("", JLabel.CENTER); 
            jLabelFechaAlta.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.datealta")); 
            
            jPanelInfoNucleo.add(jLabelObservaciones,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInfoNucleo.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInfoNucleo.add(jLabelFechaAlta, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelInfoNucleo.add(getJTextFieldFechaAlta(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
    		
    	}
    	return jPanelInfoNucleo;
    }
    
    private DateField getJTextFieldFechaAlta()
    {
    	if (jTextFieldFechaAlta == null){
    		jTextFieldFechaAlta = new DateField( (java.util.Date) null, 0);
    		jTextFieldFechaAlta.setEditable(true);
    	}
    	return jTextFieldFechaAlta;
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
    	{
    		jTextFieldObserv  = new TextField(50);
    		
    	}
    	return jTextFieldObserv;
    }
    
    private JPanel getJPanelIdentVertedero()
    {
        if (jPanelIdentVertedero == null)
        {   
        	jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            
            jLabelCodProvVertedero = new JLabel("", JLabel.CENTER); 
            jLabelCodProvVertedero.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunicVertedero = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicVertedero.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelCodOrden  = new JLabel("", JLabel.CENTER);
            jLabelCodOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            jPanelIdentVertedero = new JPanel(new GridBagLayout());
            jPanelIdentVertedero.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identityvertedero"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            
            jPanelIdentVertedero.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentVertedero.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentVertedero.add(jLabelCodProvVertedero, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentVertedero.add(getJComboBoxProvinciaVertedero(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentVertedero.add(jLabelCodMunicVertedero, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentVertedero.add(getJComboBoxMunicipioVertedero(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentVertedero.add(jLabelCodOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentVertedero.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelIdentVertedero;
    }
    
    private JTextField getJTextFieldOrden()
    {
    	if (jTextFieldCodOrden   == null)
    	{
    		jTextFieldCodOrden = new TextField(3);
    		jTextFieldCodOrden.setEnabled(false);
    	}
    	return jTextFieldCodOrden;
    }
    
    private JTextField getJTextFieldClave()
    {
        if (jTextFieldClave == null)
        {
            jTextFieldClave = new TextField(2);
            jTextFieldClave.setEnabled(false);
        }
        return jTextFieldClave;
    }
    
    private JComboBox getJComboBoxProvinciaVertedero()
    {
        if (jComboBoxProvinciaVertedero == null)
        {
        	jComboBoxProvinciaVertedero  = new JComboBox();
        	jComboBoxProvinciaVertedero.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxProvinciaVertedero.setEnabled(false);
            
        	jComboBoxProvinciaVertedero.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxProvinciaVertedero.getSelectedIndex()==0)
            		{
            			jComboBoxMunicipioVertedero.removeAllItems();
            		}
            		else
            		{
            			EdicionOperations oper = new EdicionOperations();
            			EdicionUtils.cargarLista(getJComboBoxMunicipioVertedero(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaVertedero.getSelectedItem(),idMunicipioSelected));
            		}


            	}
            });
        }
        return jComboBoxProvinciaVertedero;
    }
    
    private JComboBox getJComboBoxMunicipioVertedero()
    {
        if (jComboBoxMunicipioVertedero == null)
        {
        	jComboBoxMunicipioVertedero  = new JComboBox();        	
        	jComboBoxMunicipioVertedero.setRenderer(new UbicacionListCellRenderer());  
        	jComboBoxMunicipioVertedero.setEnabled(false);
        }
        return jComboBoxMunicipioVertedero;
    }
    
    
    private JPanel getJPanelIdentNucleo()
    {
        if (jPanelIdentNucleo == null)
        {   
        	jPanelIdentNucleo = new JPanel(new GridBagLayout());
        	jPanelIdentNucleo.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identity"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            jLabelCodProvNucleo = new JLabel("", JLabel.CENTER); 
            jLabelCodProvNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunicNucleo = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelEntSing  = new JLabel("", JLabel.CENTER);
            jLabelEntSing.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.entsing")));
            
            jLabelNucleo   = new JLabel("", JLabel.CENTER);
            jLabelNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.nucleo")));
            
            
            jPanelIdentNucleo.add(jLabelCodProvNucleo, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentNucleo.add(getJComboBoxProvinciaNucleo(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentNucleo.add(jLabelCodMunicNucleo, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
            jPanelIdentNucleo.add(getJComboBoxMunicipioNucleo(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentNucleo.add(jLabelEntSing, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentNucleo.add(getJComboBoxEntidad(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentNucleo.add(jLabelNucleo, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentNucleo.add(getJComboBoxNucleo(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelIdentNucleo;
    }
    
    private JComboBox getJComboBoxProvinciaNucleo()
    {
        if (jComboBoxProvinciaNucleo == null)
        {
        	EdicionOperations oper = new EdicionOperations();
        	ArrayList<Provincia> listaProvincias = oper.obtenerProvinciasConNombre();
        	jComboBoxProvinciaNucleo = new JComboBox(listaProvincias.toArray());
        	jComboBoxProvinciaNucleo.setSelectedIndex(this.provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
        	jComboBoxProvinciaNucleo.setRenderer(new UbicacionListCellRenderer());            
        	jComboBoxProvinciaNucleo.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   
            		if (getJComboBoxMunicipioNucleo() != null){
            			if (jComboBoxProvinciaNucleo.getSelectedIndex()==0)
            			{
            				jComboBoxMunicipioNucleo.removeAllItems();
            				jComboBoxMunicipioNucleo.addItem("");
            			}
            			else
            			{
            				EdicionOperations oper = new EdicionOperations();
            				jComboBoxProvinciaNucleo.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
            				
            				if ( jComboBoxProvinciaNucleo.getSelectedItem() != null ){
            					EdicionUtils.cargarLista(getJComboBoxMunicipioNucleo(), 
            							oper.obtenerTodosMunicipios(((Provincia)jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia()) );
            					jComboBoxMunicipioNucleo.setSelectedIndex(
            							municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio)
            							);
            					jComboBoxMunicipioNucleo.setEnabled(true);
            				}
            			}
            		}

            	}
            });

        }

        return jComboBoxProvinciaNucleo;
    }
    
    private JComboBox getJComboBoxMunicipioNucleo()
    {
        if (jComboBoxMunicipioNucleo == null)
        {
        	jComboBoxMunicipioNucleo = new JComboBox();
        	jComboBoxMunicipioNucleo.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxMunicipioNucleo.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxMunicipioNucleo.getSelectedIndex()==0)
            		{
            			jComboBoxEntidad.removeAllItems();
            			jComboBoxEntidad.addItem("");

            		}
            		else
            		{
            			MunicipioEIEL  municipio = new MunicipioEIEL();  
            			if (jComboBoxProvinciaNucleo.getSelectedItem() != null){
            				municipio.setCodProvincia(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
            			}
            			if (jComboBoxMunicipioNucleo.getSelectedItem() != null){
            				municipio.setCodMunicipio(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
            			}
            			if (municipio.getCodMunicipio()!=null){
	            			EdicionOperations oper = new EdicionOperations();
	            			EdicionUtils.cargarLista(getJComboBoxEntidad(), oper.obtenerEntidadesConNombre(municipio));
            			}
            		}
            	}
            });
        }
        return jComboBoxMunicipioNucleo;
    }
    
    private JComboBox getJComboBoxEntidad()
    {
        if (jComboBoxEntidad  == null)
        {
        	jComboBoxEntidad = new JComboBox();
//        	jComboBoxEntidad.setEditable(true);
        	jComboBoxEntidad.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxEntidad.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxEntidad.getSelectedIndex()==0)
            		{
            			jComboBoxNucleo.removeAllItems();
            		}
            		else
            		{
            			EntidadesSingularesEIEL entidad = new EntidadesSingularesEIEL();
            			
            			if (jComboBoxProvinciaNucleo.getSelectedItem() != null)
            				entidad.setCodINEProvincia(((Provincia)jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
            			if (jComboBoxMunicipioNucleo.getSelectedItem() != null && !jComboBoxMunicipioNucleo.getSelectedItem().equals(""))
            				entidad.setCodINEMunicipio(
            						((Municipio)jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne()
            						);
            			if (jComboBoxEntidad.getSelectedItem() != null && !jComboBoxEntidad.getSelectedItem().equals(""))
            				entidad.setCodINEEntidad(((EntidadesSingularesEIEL)jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
            			if (entidad.getCodINEEntidad()!=null){
            				EdicionOperations oper = new EdicionOperations();
                			EdicionUtils.cargarLista(getJComboBoxNucleo(), 
                					oper.obtenerNucleosConNombre(entidad));
            			}
            		}
            	}
            });
        }
        return jComboBoxEntidad;
    }
    
    private JComboBox getJComboBoxNucleo()
    {
        if (jComboBoxNucleo  == null)
        {
        	jComboBoxNucleo = new JComboBox();
        	jComboBoxNucleo.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxNucleo;
    }
        
    
    private void jbInit() throws Exception
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.captacionesnucleo.panel.title"));
                
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(VertederoNucleosPanel.DIM_X,VertederoNucleosPanel.DIM_Y);
        
        this.add(getJPanelIdentVertedero(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelNucleos(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));

        EdicionOperations oper = new EdicionOperations();
        if (Identificadores.get("ListaProvinciasSelector")==null)
        {
            ArrayList lst = oper.obtenerProvinciasConNombre();
            Identificadores.put("ListaProvinciasSelector", lst);
            EdicionUtils.cargarLista(getJComboBoxProvinciaVertedero(), lst);

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                	oper.obtenerProvinciasConNombre());
                Provincia p = new Provincia();
                p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
                p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
                getJComboBoxProvinciaNucleo().setSelectedItem(p);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvinciaVertedero(), 
                    (ArrayList)Identificadores.get("ListaProvinciasSelector"));    

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                    oper.obtenerProvinciasConNombre());    
        }
        
        loadVertedero();
               
    }
    
    private void loadVertedero(){
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("vertedero");
    	
    	if (object != null && object instanceof VertederosEIEL){
    		
    		VertederosEIEL vertedero = (VertederosEIEL)object;
    		
    		if (vertedero.getClave() != null){
        		jTextFieldClave.setText(vertedero.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
            
            if (vertedero.getCodINEProvincia()!=null){
            	jComboBoxProvinciaVertedero.setSelectedItem(vertedero.getCodINEProvincia());
            }
            else{
            	jComboBoxProvinciaVertedero.setSelectedIndex(0);
            }
            
            if (vertedero.getCodINEMunicipio() != null){
            	jComboBoxMunicipioVertedero.setSelectedItem(vertedero.getCodINEMunicipio());
            }
            else{
            	jComboBoxMunicipioVertedero.setSelectedIndex(0);
            }
            
            if (vertedero.getCodOrden() != null){
        		jTextFieldCodOrden.setText(vertedero.getCodOrden());
        	}
        	else{
        		jTextFieldCodOrden.setText("");
        	}
    	}
    }
    
    
    public void enter()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
                       
        //busca los núcleos de población relacionados con el elemnto seleccionado 
        EdicionOperations operations = new EdicionOperations();
        if (vertedero != null){
			lstNucleos = operations.getLstNucleosVertedero(
					vertedero.getClave(), 
					vertedero.getCodINEProvincia(), 
					vertedero.getCodINEMunicipio(), 
					vertedero.getCodOrden()
					);
        }else{
        int idVertedero= Integer.parseInt(Identificadores.get("ID_Vertedero").toString());
        
        //Obtener la lista de núcleos de población        
        
        //se recuperan por este orden: id, nif, codigoderecho, identificaciontitular
        lstNucleos = operations.getLstNucleosVertedero(idVertedero);
        
        loadDataIdentificacion();
        }
        
        ((NucleosVertederoEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);
        getJTableListaNucleos().updateUI();
        ((NucleosVertederoEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
        
    }
    
    
    
    private VertederosEIEL vertedero = null;
    public void tabbedChanged(VertederosEIEL vertedero){
    	this.vertedero = vertedero;
    	
    	loadDataIdentificacion(vertedero.getClave(),
    			vertedero.getCodINEProvincia(),
    			vertedero.getCodINEMunicipio(),
    			vertedero.getCodOrden());
    	
    	enter();
    }
    
    public void exit()
    {
    	
    }
    
	public void loadDataIdentificacion(){

		Object obj = AppContext.getApplicationContext().getBlackboard().get("featureDialog");
		if (obj != null && obj instanceof FeatureDialog){
			FeatureDialog featureDialog = (FeatureDialog) obj;
			Feature feature = featureDialog.get_fieldPanel().getModifiedFeature();
										
				GeopistaSchema esquema = (GeopistaSchema)feature.getSchema();
				feature.getAttribute(esquema.getAttributeByColumn("id"));
							
				String clave = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("clave"))!=null){
	        		clave=(feature.getAttribute(esquema.getAttributeByColumn("clave"))).toString();
	        	}
	        	
	        	String codprov = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
	        		codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
	        	}
	        	
	        	String codmunic = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
	        		codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
	        	}	        	 
	        	
	        	String orden_vt = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_vt"))!=null){
	        		orden_vt=(feature.getAttribute(esquema.getAttributeByColumn("orden_vt"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_vt);       	
		}
		getJTableListaNucleos().updateUI();
	}
	
	
	

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_vt) {
		
		idMunicipioSelected=codmunic;
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
        if (codprov != null){
        	jComboBoxProvinciaVertedero.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvinciaVertedero.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipioVertedero.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipioVertedero.setSelectedIndex(-1);
        }
        
        if (orden_vt != null){
    		jTextFieldCodOrden.setText(orden_vt);
    	}
    	else{
    		jTextFieldCodOrden.setText("");
    	}

		
	}
	
	public int provinciaIndexSeleccionar(String provinciaId){
		for (int i = 0; i < jComboBoxProvinciaNucleo.getItemCount(); i ++){
			if (((Provincia)jComboBoxProvinciaNucleo.getItemAt(i)).getIdProvincia().equals(provinciaId) ){
				return i;
			}
		}
		
		return -1;
	}
	
	public int municipioIndexSeleccionar(String municipioId){
		if (!municipioId.equals("")){
			for (int i = 0; i < jComboBoxMunicipioNucleo.getItemCount(); i ++){
				try{
					if (((Municipio)jComboBoxMunicipioNucleo.getItemAt(i)).getIdIne().equals(municipioId.substring(2, 5)) ){
						jComboBoxMunicipioNucleo.setEnabled(false);
						return i;
					}
				}catch (StringIndexOutOfBoundsException e) {
					if (((Municipio)jComboBoxMunicipioNucleo.getItemAt(i)).getIdIne().equals(municipioId) ){
						jComboBoxMunicipioNucleo.setEnabled(false);
						return i;
					}
				}
			}
			
		}
		
		return -1;
	}
	
	public int nucleoPoblacionIndexSeleccionar(String nucleoPoblacion){
		for (int i = 0; i < jComboBoxNucleo.getItemCount(); i ++){
			if (((NucleosPoblacionEIEL)jComboBoxNucleo.getItemAt(i)).getCodINEPoblamiento().equals(nucleoPoblacion) ){
				return i;
			}
		}
		
		return -1;
	}
	
	public int entidadesSingularesIndexSeleccionar(String entidadSingular){
		for (int i = 0; i < jComboBoxEntidad.getItemCount(); i ++){
			if (((EntidadesSingularesEIEL)jComboBoxEntidad.getItemAt(i)).getCodINEEntidad().equals(entidadSingular) ){
				return i;
			}
		}
		
		return -1;
	}
        
}  