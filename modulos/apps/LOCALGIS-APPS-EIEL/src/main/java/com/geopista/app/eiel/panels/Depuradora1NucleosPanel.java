/**
 * Depuradora1NucleosPanel.java
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
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoDepuradora1;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.models.NucleosDepuradora1EIELTableModel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;


public class Depuradora1NucleosPanel extends JPanel implements FeatureExtendedPanel {
    
	private static int DIM_X = 500;
	private static int DIM_Y = 450;
	
	ArrayList lstNucleos = new ArrayList();
	
	private Blackboard Identificadores = AppContext.getApplicationContext().getBlackboard();
	
	private JPanel jPanelIdentDepuradora1 = null;
	private JPanel jPanelNucleos = null;
	private JPanel jPanelIdentNucleo = null;
	private JPanel jPanelInfoNucleo = null;
	private JPanel jPanelRevisionNucleo = null;
	private JPanel jPanelBotonera = null;
	private JLabel jLabelClave;
	private JLabel jLabelCodProvDepuradora1;
	private JLabel jLabelCodMunicDepuradora1;
	private JLabel jLabelCodOrden;
	private TextField jTextFieldClave;
	private JComboBox jComboBoxProvinciaDepuradora1;
	private JComboBox jComboBoxMunicipioDepuradora1;
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
	private TextField jTextFieldObserv;
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
    
    
    public Depuradora1NucleosPanel()
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

    		tableListaNucleosModel  = new NucleosDepuradora1EIELTableModel();

    		TableSorted tblSorted= new TableSorted((TableModel)tableListaNucleosModel);
    		tblSorted.setTableHeader(jTableListaNucleos.getTableHeader());
    		jTableListaNucleos.setModel(tblSorted);
    		jTableListaNucleos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaNucleos.setCellSelectionEnabled(false);
    		jTableListaNucleos.setColumnSelectionAllowed(false);
    		jTableListaNucleos.setRowSelectionAllowed(true);
    		jTableListaNucleos.getTableHeader().setReorderingAllowed(false);
    		
    		jTableListaNucleos.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		
    		((NucleosDepuradora1EIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);

    	}
    	return jTableListaNucleos;
    }
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosDepuradora1EIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoDepuradora1){
    		
    		loadNucleoDepuradora1((NucleoDepuradora1)selectedItem);
    	}    	
    }
    
    private void loadNucleoDepuradora1(NucleoDepuradora1 nucleoDepuradora1){
    	
    	if (nucleoDepuradora1.getCodProvNucleo()!=null){
        	jComboBoxProvinciaNucleo.setSelectedIndex(
        		provinciaIndexSeleccionar(nucleoDepuradora1.getCodProvNucleo())
        			);
        }
        else{
        	jComboBoxProvinciaNucleo.setSelectedIndex(0);
        }
        
        if (nucleoDepuradora1.getCodMunicNucleo() != null){
        	jComboBoxMunicipioNucleo.setSelectedIndex(
        			municipioIndexSeleccionar(nucleoDepuradora1.getCodMunicNucleo())
        	);
        }
        else{
        	jComboBoxMunicipioNucleo.setSelectedIndex(0);
        }
        
        if (nucleoDepuradora1.getCodEntNucleo() != null){
        	jComboBoxEntidad.setSelectedIndex(
        			entidadesSingularesIndexSeleccionar(nucleoDepuradora1.getCodEntNucleo())
        	);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleoDepuradora1.getCodPoblNucleo() != null){
        	jComboBoxNucleo.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(nucleoDepuradora1.getCodPoblNucleo())
        			);
        	
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(0);
        }
        
        if (nucleoDepuradora1.getObservaciones() != null){
    		jTextFieldObserv.setText(nucleoDepuradora1.getObservaciones());
    	}
    	else{
    		jTextFieldObserv.setText("");
    	}
       
        if (nucleoDepuradora1.getFechaRevision() != null){
    		jTextFieldFechaRevision.setText(nucleoDepuradora1.getFechaRevision().toString());
    	}
    	else{
    		jTextFieldFechaRevision.setText("");
    	}
        
        if (nucleoDepuradora1.getEstadoRevision() != null){
        	jComboBoxEstadoRevision.setSelectedPatron(nucleoDepuradora1.getEstadoRevision().toString());
    	}
    	else{
    		jComboBoxEstadoRevision.setSelectedIndex(0);
    	}
    	
    }
    
    private void reset(){

    	
    	jComboBoxProvinciaNucleo.setSelectedIndex(0);    	
    	jTextFieldObserv.setText("");
    	jTextFieldFechaRevision.setText("");
    	jComboBoxEstadoRevision.setSelectedIndex(0);
    	
    	jComboBoxMunicipioNucleo.setEnabled(true);
    	jComboBoxMunicipioNucleo.setSelectedIndex(0);

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
    		JOptionPane.showMessageDialog(this,null,I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
    				JOptionPane.ERROR_MESSAGE);
    		
    	}
    	else{
    		operations.saveNucleoDepuradora1(getNucleoDepuradora1());
        	enter();
    	}
    	
    }
    
    private void eliminarNucleoDepuradora1(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosDepuradora1EIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoDepuradora1){
    		
    		EdicionOperations operations = new EdicionOperations();
    		operations.deleteNucleoDepuradora1((NucleoDepuradora1)selectedItem);
    		reset();
    		enter();
    	}  
    }
    
    public boolean datosMinimosYCorrectos()
    {

        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldCodOrden.getText()!=null && !jTextFieldCodOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvinciaDepuradora1!=null && jComboBoxProvinciaDepuradora1.getSelectedItem()!=null && jComboBoxProvinciaDepuradora1.getSelectedIndex()>0) &&
        (jComboBoxProvinciaNucleo!=null && jComboBoxProvinciaNucleo.getSelectedItem()!=null && jComboBoxProvinciaNucleo.getSelectedIndex()>0) &&        
        (jComboBoxMunicipioDepuradora1!=null && jComboBoxMunicipioDepuradora1.getSelectedItem()!=null && jComboBoxMunicipioDepuradora1.getSelectedIndex()>0) && 
        (jComboBoxMunicipioNucleo!=null && jComboBoxMunicipioNucleo.getSelectedItem()!=null && jComboBoxMunicipioNucleo.getSelectedIndex()>0) &&
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&  
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0)); 
        
    }
    
    private NucleoDepuradora1 getNucleoDepuradora1(){
    	
    	NucleoDepuradora1 nucleoDepuradora1 = new NucleoDepuradora1();
    	
    	nucleoDepuradora1.setCodProvDepuradora1((String) jComboBoxProvinciaDepuradora1.getSelectedItem());
    	nucleoDepuradora1.setCodMunicDepuradora1((String) jComboBoxMunicipioDepuradora1.getSelectedItem());
        
        if (jTextFieldClave.getText()!=null){
        	nucleoDepuradora1.setClaveDepuradora1(jTextFieldClave.getText());
        }
        
        if (jTextFieldCodOrden.getText()!=null){
        	nucleoDepuradora1.setCodOrdenDepuradora1(jTextFieldCodOrden.getText());
        }
        
        nucleoDepuradora1.setCodProvNucleo(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
        nucleoDepuradora1.setCodMunicNucleo(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
        nucleoDepuradora1.setCodEntNucleo(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
        nucleoDepuradora1.setCodPoblNucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
        
        if (jTextFieldObserv.getText()!=null){
        	nucleoDepuradora1.setObservaciones(jTextFieldObserv.getText());
        }
        
        if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
        	String fechas=jTextFieldFechaRevision.getText();
        	String anio=fechas.substring(0,4);
        	String mes=fechas.substring(5,7);
        	String dia=fechas.substring(8,10);
        	
        	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
        	nucleoDepuradora1.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

        }  
        else{
        	nucleoDepuradora1.setFechaRevision(null);
        }
        
        if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
        	nucleoDepuradora1.setEstadoRevision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
        
    	
    	return nucleoDepuradora1;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarNucleoDepuradora1();
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
            
            jPanelInfoNucleo.add(jLabelObservaciones,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInfoNucleo.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            
    	}
    	return jPanelInfoNucleo;
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
    	{
    		jTextFieldObserv  = new TextField(50);
    		
    	}
    	return jTextFieldObserv;
    }
    
    public JPanel getJPanelIdentDepuradora1()
    {
        if (jPanelIdentDepuradora1 == null)
        {   
        	jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            
            jLabelCodProvDepuradora1 = new JLabel("", JLabel.CENTER); 
            jLabelCodProvDepuradora1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunicDepuradora1 = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicDepuradora1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelCodOrden  = new JLabel("", JLabel.CENTER);
            jLabelCodOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            jPanelIdentDepuradora1 = new JPanel(new GridBagLayout());
            jPanelIdentDepuradora1.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identitydepuradora"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            
            jPanelIdentDepuradora1.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentDepuradora1.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentDepuradora1.add(jLabelCodProvDepuradora1, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentDepuradora1.add(getJComboBoxProvinciaDepuradora1(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentDepuradora1.add(jLabelCodMunicDepuradora1, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentDepuradora1.add(getJComboBoxMunicipioDepuradora1(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentDepuradora1.add(jLabelCodOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentDepuradora1.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelIdentDepuradora1;
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
    
    private JComboBox getJComboBoxProvinciaDepuradora1()
    {
        if (jComboBoxProvinciaDepuradora1 == null)
        {
        	jComboBoxProvinciaDepuradora1  = new JComboBox();
        	jComboBoxProvinciaDepuradora1.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxProvinciaDepuradora1.setEnabled(false);
            
        	jComboBoxProvinciaDepuradora1.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   
            		if (jComboBoxProvinciaDepuradora1.getSelectedIndex()==0)
            		{
            			jComboBoxMunicipioDepuradora1.removeAllItems(); 
            			
            		}
            		else
            		{            			
            			EdicionOperations oper = new EdicionOperations();
            			/*EdicionUtils.cargarLista(getJComboBoxMunicipioDepuradora1(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaDepuradora1.getSelectedItem()));*/
            			//Solucionada incidencia EIEL2-19
            			EdicionUtils.cargarLista(getJComboBoxMunicipioDepuradora1(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaDepuradora1.getSelectedItem(),idMunicipioSelected));
            		
            					
            			
            		}


            	}
            });
        }
        return jComboBoxProvinciaDepuradora1;
    }
    
    private JComboBox getJComboBoxMunicipioDepuradora1()
    {
        if (jComboBoxMunicipioDepuradora1 == null)
        {
        	jComboBoxMunicipioDepuradora1  = new JComboBox();        	
        	jComboBoxMunicipioDepuradora1.setRenderer(new UbicacionListCellRenderer());  
        	jComboBoxMunicipioDepuradora1.setEnabled(false);
        }
        return jComboBoxMunicipioDepuradora1;
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
//            			EntidadEIEL entidad = new EntidadEIEL();
            			if (jComboBoxProvinciaNucleo.getSelectedItem() != null){
            				municipio.setCodProvincia(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
//            				entidad.setCodProvincia(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
            			}
            			if (jComboBoxMunicipioNucleo.getSelectedItem() != null){
            				municipio.setCodMunicipio(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
//            				entidad.setCodMunicipio(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
            			}
            			
            			if (municipio.getCodMunicipio()!=null){
	            			EdicionOperations oper = new EdicionOperations();
	            			EdicionUtils.cargarLista(getJComboBoxEntidad(), oper.obtenerEntidadesConNombre(municipio));
//	            			System.out.println("PASO1");
	            			//EdicionUtils.cargarLista(jComboBoxNucleo, oper.obtenerNucleosConNombre(entidad)); 
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
        this.setSize(Depuradora1NucleosPanel.DIM_X,Depuradora1NucleosPanel.DIM_Y);
        
        this.add(getJPanelIdentDepuradora1(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelNucleos(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        EdicionOperations oper = new EdicionOperations();
        if (Identificadores.get("ListaProvinciasSelector")==null)
        {
           
            ArrayList lst = oper.obtenerProvincias(true);
            Identificadores.put("ListaProvinciasSelector", lst);
            EdicionUtils.cargarLista(getJComboBoxProvinciaDepuradora1(), lst);

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                	oper.obtenerProvinciasConNombre());
                Provincia p = new Provincia();
                p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
                p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
                getJComboBoxProvinciaNucleo().setSelectedItem(p);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvinciaDepuradora1(), 
                    (ArrayList)Identificadores.get("ListaProvinciasSelector"));    

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                    oper.obtenerProvinciasConNombre());     
        }
        
        loadDepuradora1();
               
    }
    
    private void loadDepuradora1(){
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("depuradora1");
    	
    	if (object != null && object instanceof Depuradora1EIEL){
    		
    		Depuradora1EIEL depuradora1 = (Depuradora1EIEL)object;
    		
    		if (depuradora1.getClave() != null){
        		jTextFieldClave.setText(depuradora1.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
            
            if (depuradora1.getCodINEProvincia()!=null){
            	jComboBoxProvinciaDepuradora1.setSelectedItem(depuradora1.getCodINEProvincia());
            }
            else{
            	jComboBoxProvinciaDepuradora1.setSelectedIndex(0);
            }
            
            if (depuradora1.getCodINEMunicipio() != null){
            	jComboBoxMunicipioDepuradora1.setSelectedItem(depuradora1.getCodINEMunicipio());
            }
            else{
            	jComboBoxMunicipioDepuradora1.setSelectedIndex(0);
            }
            
            if (depuradora1.getCodOrden() != null){
        		jTextFieldCodOrden.setText(depuradora1.getCodOrden());
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
    	if (depuradora1 != null){
			lstNucleos = operations.getLstNucleosDepuradora1(
					depuradora1.getClave(), 
					depuradora1.getCodINEProvincia(), 
					depuradora1.getCodINEMunicipio(), 
					depuradora1.getCodOrden()
			);

    	}else{
    		int idDepuradora1= Integer.parseInt(Identificadores.get("ID_Depuradora1").toString());

    		//Obtener la lista de núcleos de población        

    		//se recuperan por este orden: id, nif, codigoderecho, identificaciontitular
    		lstNucleos = operations.getLstNucleosDepuradora1(idDepuradora1);        

    		loadDataIdentificacion();
    	}
    	((NucleosDepuradora1EIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);
    	getJTableListaNucleos().updateUI();
    	((NucleosDepuradora1EIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();

    }


    private Depuradora1EIEL depuradora1 = null;
    public void tabbedChanged(Depuradora1EIEL depuradora){
    	this.depuradora1 = depuradora;

    	loadDataIdentificacion(depuradora.getClave(),
    			depuradora.getCodINEProvincia(),
    			depuradora.getCodINEMunicipio(),
    			depuradora.getCodOrden());

    	enter();
    }
    public void tabbedChanged(Depuradora2EIEL depuradora){
    	// Cambiamos la deupradora2 a depuradora1 para 
    	// seguir con el funcionamiento correcto del panel.
    	this.depuradora1 = new Depuradora1EIEL();
    	this.depuradora1.setClave(depuradora.getClave());
    	this.depuradora1.setCodINEMunicipio(depuradora.getCodINEMunicipio());
    	this.depuradora1.setCodINEProvincia(depuradora.getCodINEProvincia());
    	this.depuradora1.setCodOrden(depuradora.getCodOrden());

    	loadDataIdentificacion(depuradora.getClave(),
    			depuradora.getCodINEProvincia(),
    			depuradora.getCodINEMunicipio(),
    			depuradora.getCodOrden());

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

	        	
	        	String orden_de = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))!=null){
	        		orden_de=(feature.getAttribute(esquema.getAttributeByColumn("orden_ed"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_de);
			}
		getJTableListaNucleos().updateUI();
		}
    
    public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_de) {
		
    	idMunicipioSelected=codmunic;
    	
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
        if (codprov != null){
        	jComboBoxProvinciaDepuradora1.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvinciaDepuradora1.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipioDepuradora1.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipioDepuradora1.setSelectedIndex(-1);
        }
                
        if (orden_de != null){
    		jTextFieldCodOrden.setText(orden_de);
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
						return i;
					}
				}catch (StringIndexOutOfBoundsException e) {
					if (((Municipio)jComboBoxMunicipioNucleo.getItemAt(i)).getIdIne().equals(municipioId) ){
						return i;
					}
				}
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
	
	public int nucleoPoblacionIndexSeleccionar(String nucleoPoblacion){
		for (int i = 0; i < jComboBoxNucleo.getItemCount(); i ++){
			if (((NucleosPoblacionEIEL)jComboBoxNucleo.getItemAt(i)).getCodINEPoblamiento().equals(nucleoPoblacion) ){
				return i;
			}
		}
		
		return -1;
	}
}  