package com.geopista.app.eiel.panels;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
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
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoTratamientosPotabilizacion;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.models.NucleosTratamientosPotabilizacionEIELTableModel;
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


public class TratamientosPotabilizacionNucleosPanel extends JPanel implements FeatureExtendedPanel {
    
	private static int DIM_X = 500;
	private static int DIM_Y = 450;
	
	ArrayList lstNucleos = new ArrayList();
	
	private Blackboard Identificadores = AppContext.getApplicationContext().getBlackboard();
	
	private JPanel jPanelIdentTratamientosPotabilizacion = null;
	private JPanel jPanelNucleos = null;
	private JPanel jPanelIdentNucleo = null;
	private JPanel jPanelInfoNucleo = null;
	private JPanel jPanelRevisionNucleo = null;
	private JPanel jPanelBotonera = null;
	private JLabel jLabelClave;
	private JLabel jLabelCodProvTratamientosPotabilizacion;
	private JLabel jLabelCodMunicTratamientosPotabilizacion;
	private JLabel jLabelCodOrden;
	private TextField jTextFieldClave;
	private JComboBox jComboBoxProvinciaTratamientosPotabilizacion;
	private JComboBox jComboBoxMunicipioTratamientosPotabilizacion;
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
    
    
    public TratamientosPotabilizacionNucleosPanel()
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

    		tableListaNucleosModel  = new NucleosTratamientosPotabilizacionEIELTableModel();

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
    		
    		((NucleosTratamientosPotabilizacionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);

    	}
    	return jTableListaNucleos;
    }
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosTratamientosPotabilizacionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoTratamientosPotabilizacion){
    		
    		loadNucleoTratamientosPotabilizacion((NucleoTratamientosPotabilizacion)selectedItem);
    	}    	
    }
    
    private void loadNucleoTratamientosPotabilizacion(NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion){
    	
    	if (nucleoTratamientosPotabilizacion.getCodProvNucleo()!=null){
        	jComboBoxProvinciaNucleo.setSelectedIndex(
        		provinciaIndexSeleccionar(nucleoTratamientosPotabilizacion.getCodProvNucleo())
        			);
        }
        else{
        	jComboBoxProvinciaNucleo.setSelectedIndex(0);
        }
        
        if (nucleoTratamientosPotabilizacion.getCodMunicNucleo() != null){
        	jComboBoxMunicipioNucleo.setSelectedIndex(
        			municipioIndexSeleccionar(nucleoTratamientosPotabilizacion.getCodMunicNucleo())
        	);
        	
        	
        }
        else{
        	jComboBoxMunicipioNucleo.setSelectedIndex(0);
        }
        
        if (nucleoTratamientosPotabilizacion.getCodEntNucleo() != null){
        	jComboBoxEntidad.setSelectedIndex(
        			entidadesSingularesIndexSeleccionar(nucleoTratamientosPotabilizacion.getCodEntNucleo())
        	);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleoTratamientosPotabilizacion.getCodPoblNucleo() != null){
        	jComboBoxNucleo.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(nucleoTratamientosPotabilizacion.getCodPoblNucleo())
        			);
        	
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(0);
        }
        
       
        
        if (nucleoTratamientosPotabilizacion.getObservaciones() != null){
    		jTextFieldObserv.setText(nucleoTratamientosPotabilizacion.getObservaciones());
    	}
    	else{
    		jTextFieldObserv.setText("");
    	}
       
        if (nucleoTratamientosPotabilizacion.getFechaRevision() != null && nucleoTratamientosPotabilizacion.getFechaRevision().equals( new java.util.Date()) ){
    		jTextFieldFechaRevision.setText(nucleoTratamientosPotabilizacion.getFechaRevision().toString());
    	}
    	else{
    	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
    		jTextFieldFechaRevision.setText(datetime);
    	}
        
        if (nucleoTratamientosPotabilizacion.getEstadoRevision() != null){
        	jComboBoxEstadoRevision.setSelectedPatron(nucleoTratamientosPotabilizacion.getEstadoRevision().toString());
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
    		operations.saveNucleoTratamientosPotabilizacion(getNucleoTratamientosPotabilizacion());
        	enter();
    	}
    	
    }
    
    private void eliminarNucleoTratamientosPotabilizacion(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosTratamientosPotabilizacionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoTratamientosPotabilizacion){
    		
    		EdicionOperations operations = new EdicionOperations();
    		operations.deleteNucleoTratamientosPotabilizacion((NucleoTratamientosPotabilizacion)selectedItem);
    		reset();
    		enter();
    	}  
    }
    
    public boolean datosMinimosYCorrectos()
    {

        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldCodOrden.getText()!=null && !jTextFieldCodOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvinciaTratamientosPotabilizacion!=null && jComboBoxProvinciaTratamientosPotabilizacion.getSelectedItem()!=null && jComboBoxProvinciaTratamientosPotabilizacion.getSelectedIndex()>0) &&
        (jComboBoxProvinciaNucleo!=null && jComboBoxProvinciaNucleo.getSelectedItem()!=null && jComboBoxProvinciaNucleo.getSelectedIndex()>0) &&        
        (jComboBoxMunicipioTratamientosPotabilizacion!=null && jComboBoxMunicipioTratamientosPotabilizacion.getSelectedItem()!=null && jComboBoxMunicipioTratamientosPotabilizacion.getSelectedIndex()>0) && 
        (jComboBoxMunicipioNucleo!=null && jComboBoxMunicipioNucleo.getSelectedItem()!=null && jComboBoxMunicipioNucleo.getSelectedIndex()>0) && 
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&  
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0)); 
        
    }
    
    private NucleoTratamientosPotabilizacion getNucleoTratamientosPotabilizacion(){
    	
    	NucleoTratamientosPotabilizacion nucleoTratamientosPotabilizacion = new NucleoTratamientosPotabilizacion();
    	
    	nucleoTratamientosPotabilizacion.setCodProvTratamientosPotabilizacion((String) jComboBoxProvinciaTratamientosPotabilizacion.getSelectedItem());
    	nucleoTratamientosPotabilizacion.setCodMunicTratamientosPotabilizacion((String) jComboBoxMunicipioTratamientosPotabilizacion.getSelectedItem());
        
        if (jTextFieldClave.getText()!=null){
        	nucleoTratamientosPotabilizacion.setClaveTratamientosPotabilizacion(jTextFieldClave.getText());
        }
        
        if (jTextFieldCodOrden.getText()!=null){
        	nucleoTratamientosPotabilizacion.setCodOrdenTratamientosPotabilizacion(jTextFieldCodOrden.getText());
        }
        
        nucleoTratamientosPotabilizacion.setCodProvNucleo(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
        nucleoTratamientosPotabilizacion.setCodMunicNucleo(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
        nucleoTratamientosPotabilizacion.setCodEntNucleo(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
        nucleoTratamientosPotabilizacion.setCodPoblNucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
        
        if (jTextFieldObserv.getText()!=null){
        	nucleoTratamientosPotabilizacion.setObservaciones(jTextFieldObserv.getText());
        }
        
        if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
        	String fechas=jTextFieldFechaRevision.getText();
        	String anio=fechas.substring(0,4);
        	String mes=fechas.substring(5,7);
        	String dia=fechas.substring(8,10);
        	
        	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
        	nucleoTratamientosPotabilizacion.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

        }  
        else{
        	nucleoTratamientosPotabilizacion.setFechaRevision(null);
        }
        
        if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
        	nucleoTratamientosPotabilizacion.setEstadoRevision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
     
    	
    	return nucleoTratamientosPotabilizacion;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarNucleoTratamientosPotabilizacion();
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
                    null, Identificadores.get(AppContext.GEOPISTA_LOCALE_KEY,"es_ES").toString(), true);
        
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
    
    private JPanel getJPanelIdentTratamientosPotabilizacion()
    {
        if (jPanelIdentTratamientosPotabilizacion == null)
        {   
        	jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            
            jLabelCodProvTratamientosPotabilizacion = new JLabel("", JLabel.CENTER); 
            jLabelCodProvTratamientosPotabilizacion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunicTratamientosPotabilizacion = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicTratamientosPotabilizacion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelCodOrden  = new JLabel("", JLabel.CENTER);
            jLabelCodOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            jPanelIdentTratamientosPotabilizacion = new JPanel(new GridBagLayout());
            jPanelIdentTratamientosPotabilizacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identitytratpot"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            
            jPanelIdentTratamientosPotabilizacion.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTratamientosPotabilizacion.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTratamientosPotabilizacion.add(jLabelCodProvTratamientosPotabilizacion, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTratamientosPotabilizacion.add(getJComboBoxProvinciaTratamientosPotabilizacion(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTratamientosPotabilizacion.add(jLabelCodMunicTratamientosPotabilizacion, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTratamientosPotabilizacion.add(getJComboBoxMunicipioTratamientosPotabilizacion(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTratamientosPotabilizacion.add(jLabelCodOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTratamientosPotabilizacion.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelIdentTratamientosPotabilizacion;
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
    
    private JComboBox getJComboBoxProvinciaTratamientosPotabilizacion()
    {
        if (jComboBoxProvinciaTratamientosPotabilizacion == null)
        {
        	jComboBoxProvinciaTratamientosPotabilizacion  = new JComboBox();
        	jComboBoxProvinciaTratamientosPotabilizacion.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxProvinciaTratamientosPotabilizacion.setEnabled(false);
            
        	jComboBoxProvinciaTratamientosPotabilizacion.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxProvinciaTratamientosPotabilizacion.getSelectedIndex()==0)
            		{
            			jComboBoxMunicipioTratamientosPotabilizacion.removeAllItems();
            		}
            		else
            		{
            			EdicionOperations oper = new EdicionOperations();
            			EdicionUtils.cargarLista(getJComboBoxMunicipioTratamientosPotabilizacion(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaTratamientosPotabilizacion.getSelectedItem(),idMunicipioSelected));
            		}


            	}
            });
        }
        return jComboBoxProvinciaTratamientosPotabilizacion;
    }
    
    private JComboBox getJComboBoxMunicipioTratamientosPotabilizacion()
    {
        if (jComboBoxMunicipioTratamientosPotabilizacion == null)
        {
        	jComboBoxMunicipioTratamientosPotabilizacion  = new JComboBox();        	
        	jComboBoxMunicipioTratamientosPotabilizacion.setRenderer(new UbicacionListCellRenderer());  
        	jComboBoxMunicipioTratamientosPotabilizacion.setEnabled(false);
        }
        return jComboBoxMunicipioTratamientosPotabilizacion;
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
//	            			
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
        this.setSize(TratamientosPotabilizacionNucleosPanel.DIM_X,TratamientosPotabilizacionNucleosPanel.DIM_Y);
        
        this.add(getJPanelIdentTratamientosPotabilizacion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelNucleos(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        EdicionOperations oper = new EdicionOperations();
        if (Identificadores.get("ListaProvincias")==null)
        {
            ArrayList lst = oper.obtenerProvincias();
            Identificadores.put("ListaProvincias", lst);
            EdicionUtils.cargarLista(getJComboBoxProvinciaTratamientosPotabilizacion(), lst);


            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                	oper.obtenerProvincias());
                Provincia p = new Provincia();
                p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
                p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
                getJComboBoxProvinciaNucleo().setSelectedItem(p);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvinciaTratamientosPotabilizacion(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));    

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                    oper.obtenerProvinciasConNombre());     
        }
        
        loadTratamientoPotabilizacion();
               
    }
    
    private void loadTratamientoPotabilizacion(){
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("tratamientos");
    	
    	if (object != null && object instanceof TratamientosPotabilizacionEIEL){
    		
    		TratamientosPotabilizacionEIEL tratamientos = (TratamientosPotabilizacionEIEL)object;
    		
    		if (tratamientos.getClave() != null){
        		jTextFieldClave.setText(tratamientos.getClave());
        	}
        	else{
        		jTextFieldClave.setText("");
        	}
            
            if (tratamientos.getCodINEProvincia()!=null){
            	jComboBoxProvinciaTratamientosPotabilizacion.setSelectedItem(tratamientos.getCodINEProvincia());
            }
            else{
            	jComboBoxProvinciaTratamientosPotabilizacion.setSelectedIndex(0);
            }
            
            if (tratamientos.getCodINEMunicipio() != null){
            	jComboBoxMunicipioTratamientosPotabilizacion.setSelectedItem(tratamientos.getCodINEMunicipio());
            }
            else{
            	jComboBoxMunicipioTratamientosPotabilizacion.setSelectedIndex(0);
            }
            
            if (tratamientos.getOrdenPotabilizadora() != null){
        		jTextFieldCodOrden.setText(tratamientos.getOrdenPotabilizadora());
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
        if (tratamientoPotabilizacion != null){
			lstNucleos = operations.getLstNucleosTratamientosPotabilizacion(
					tratamientoPotabilizacion.getClave(), 
					tratamientoPotabilizacion.getCodINEProvincia(), 
					tratamientoPotabilizacion.getCodINEMunicipio(), 
					tratamientoPotabilizacion.getOrdenPotabilizadora()
					);
        }else{
        int idTratamientos= Integer.parseInt(Identificadores.get("ID_Tratamientos").toString());
        
        //Obtener la lista de núcleos de población        
        
        //se recuperan por este orden: id, nif, codigoderecho, identificaciontitular
        lstNucleos = operations.getLstNucleosTratamientosPotabilizacion(idTratamientos);
        
        loadDataIdentificacion();
        }
        ((NucleosTratamientosPotabilizacionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);
        getJTableListaNucleos().updateUI();
        ((NucleosTratamientosPotabilizacionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
    }
    
    private TratamientosPotabilizacionEIEL tratamientoPotabilizacion = null;
    public void tabbedChanged(TratamientosPotabilizacionEIEL trtPotbilizaion){
    	this.tratamientoPotabilizacion = trtPotbilizaion;
    	
    	loadDataIdentificacion(trtPotbilizaion.getClave(),
    			trtPotbilizaion.getCodINEProvincia(),
    			trtPotbilizaion.getCodINEMunicipio(),
    			trtPotbilizaion.getOrdenPotabilizadora());
    	
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
	        	
	        	String orden_tp = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("orden_tp"))!=null){
	        		orden_tp=(feature.getAttribute(esquema.getAttributeByColumn("orden_tp"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_tp);      	
		}
		getJTableListaNucleos().updateUI();
    }

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_tp) {
		
		idMunicipioSelected=codmunic;
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
        if (codprov != null){
        	jComboBoxProvinciaTratamientosPotabilizacion.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvinciaTratamientosPotabilizacion.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipioTratamientosPotabilizacion.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipioTratamientosPotabilizacion.setSelectedIndex(-1);
        }

        if (orden_tp != null){
    		jTextFieldCodOrden.setText(orden_tp);
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