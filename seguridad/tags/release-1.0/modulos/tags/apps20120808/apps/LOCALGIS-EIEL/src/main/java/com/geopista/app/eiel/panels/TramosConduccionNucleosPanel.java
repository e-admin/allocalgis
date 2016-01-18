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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.TableModel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoTramosConduccion;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.models.NucleosTramosConduccionEIELTableModel;
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


public class TramosConduccionNucleosPanel extends JPanel implements FeatureExtendedPanel {
    
	private static int DIM_X = 500;
	private static int DIM_Y = 450;
	
	ArrayList lstNucleos = new ArrayList();
	
	private Blackboard Identificadores = AppContext.getApplicationContext().getBlackboard();
	
	private JPanel jPanelIdentTramosConduccion = null;
	private JPanel jPanelNucleos = null;
	private JPanel jPanelIdentNucleo = null;
	private JPanel jPanelInfoNucleo = null;
	private JPanel jPanelRevisionNucleo = null;
	private JPanel jPanelBotonera = null;	
	private JLabel jLabelCodProvTramosConduccion;
	private JLabel jLabelCodMunicTramosConduccion;
	private JLabel jLabelCodOrden;
	private JComboBox jComboBoxProvinciaTramosConduccion;
	private JComboBox jComboBoxMunicipioTramosConduccion;
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
	private JLabel jLabelClave;
	private JLabel jLabelPmi;
	private JLabel jLabelPmf;
	private TextField jTextFieldObserv;
	private TextField jTextFieldClave;
	private TextField jTextFieldPmi;
	private TextField jTextFieldPmf;
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
    
    
    public TramosConduccionNucleosPanel()
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

    		tableListaNucleosModel  = new NucleosTramosConduccionEIELTableModel();

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
    		
    		((NucleosTramosConduccionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);

    	}
    	return jTableListaNucleos;
    }
    
    private void loadSelectedItem(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosTramosConduccionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoTramosConduccion){
    		
    		loadNucleoTramosConduccion((NucleoTramosConduccion)selectedItem);
    	}    	
    }
    
    private void loadNucleoTramosConduccion(NucleoTramosConduccion nucleoTramosConduccion){
    	
    	if (nucleoTramosConduccion.getCodProvNucleo()!=null){
        	jComboBoxProvinciaNucleo.setSelectedIndex(
        		provinciaIndexSeleccionar(nucleoTramosConduccion.getCodProvNucleo())
        			);
        }
        else{
        	jComboBoxProvinciaNucleo.setSelectedIndex(0);
        }
        
        if (nucleoTramosConduccion.getCodMunicNucleo() != null){
        	jComboBoxMunicipioNucleo.setSelectedIndex(
        			municipioIndexSeleccionar(nucleoTramosConduccion.getCodMunicNucleo())
        	);
        	
        	
        }
        else{
        	jComboBoxMunicipioNucleo.setSelectedIndex(0);
        }
        
        if (nucleoTramosConduccion.getCodEntNucleo() != null){
        	jComboBoxEntidad.setSelectedIndex(
        			entidadesSingularesIndexSeleccionar(nucleoTramosConduccion.getCodEntNucleo())
        	);
        }
        else{
        	jComboBoxEntidad.setSelectedIndex(0);
        }
        
        if (nucleoTramosConduccion.getCodPoblNucleo() != null){
        	jComboBoxNucleo.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(nucleoTramosConduccion.getCodPoblNucleo())
        			);
        	
        }
        else{
        	jComboBoxNucleo.setSelectedIndex(0);
        }
        
        if (nucleoTramosConduccion.getObservaciones() != null){
    		jTextFieldObserv.setText(nucleoTramosConduccion.getObservaciones());
    	}
    	else{
    		jTextFieldObserv.setText("");
    	}
       
        if (nucleoTramosConduccion.getClaveTramosConduccion() != null){
    		jTextFieldClave.setText(nucleoTramosConduccion.getClaveTramosConduccion());
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
        if (nucleoTramosConduccion.getFechaRevision() != null && nucleoTramosConduccion.getFechaRevision().equals( new java.util.Date()) ){
    		jTextFieldFechaRevision.setText(nucleoTramosConduccion.getFechaRevision().toString());
    	}
    	else{
    	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
    		jTextFieldFechaRevision.setText(datetime);
    	}
        
        if (nucleoTramosConduccion.getEstadoRevision() != null){
        	jComboBoxEstadoRevision.setSelectedPatron(nucleoTramosConduccion.getEstadoRevision().toString());
    	}
    	else{
    		jComboBoxEstadoRevision.setSelectedIndex(0);
    	}
        
        if (nucleoTramosConduccion.getPmi() != null){
    		jTextFieldPmi.setText(nucleoTramosConduccion.getPmi().toString());
    	}
    	else{
    		jTextFieldPmi.setText("");
    	}
        
        if (nucleoTramosConduccion.getPmf() != null){
    		jTextFieldPmf.setText(nucleoTramosConduccion.getPmf().toString());
    	}
    	else{
    		jTextFieldPmf.setText("");
    	}
    	
    }
    
    private void reset(){

    	
    	jComboBoxProvinciaNucleo.setSelectedIndex(0);    	
    	jTextFieldObserv.setText("");
    	jTextFieldClave.setText("");
    	jTextFieldFechaRevision.setText("");
    	jComboBoxEstadoRevision.setSelectedIndex(0);
    	jTextFieldPmi.setText("");
    	jTextFieldPmf.setText("");

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
    		operations.saveNucleoTramosConduccion(getNucleoTramosConduccion());
        	enter();
    	}
    	
    }
    
    private void eliminarNucleoTramosConduccion(){
    	
    	int selectedRow = getJTableListaNucleos().getSelectedRow();
    	Object selectedItem = ((NucleosTramosConduccionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).getValueAt(selectedRow);
    	
    	if (selectedItem != null && selectedItem instanceof NucleoTramosConduccion){
    		
    		EdicionOperations operations = new EdicionOperations();
    		operations.deleteNucleoTramosConduccion((NucleoTramosConduccion)selectedItem);
    		reset();
    		enter();
    	}  
    }
    
    public boolean datosMinimosYCorrectos()
    {

        return  ((jTextFieldCodOrden.getText()!=null && !jTextFieldCodOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvinciaTramosConduccion!=null && jComboBoxProvinciaTramosConduccion.getSelectedItem()!=null && jComboBoxProvinciaTramosConduccion.getSelectedIndex()>0) &&
        (jComboBoxProvinciaNucleo!=null && jComboBoxProvinciaNucleo.getSelectedItem()!=null && jComboBoxProvinciaNucleo.getSelectedIndex()>0) &&        
        (jComboBoxMunicipioTramosConduccion!=null && jComboBoxMunicipioTramosConduccion.getSelectedItem()!=null && jComboBoxMunicipioTramosConduccion.getSelectedIndex()>0) && 
        (jComboBoxMunicipioNucleo!=null && jComboBoxMunicipioNucleo.getSelectedItem()!=null && jComboBoxMunicipioNucleo.getSelectedIndex()>0) &&
        (jComboBoxEntidad!=null && jComboBoxEntidad.getSelectedItem()!=null && jComboBoxEntidad.getSelectedIndex()>0) &&  
        (jComboBoxNucleo!=null && jComboBoxNucleo.getSelectedItem()!=null && jComboBoxNucleo.getSelectedIndex()>0)); 
        
    }
    
    private NucleoTramosConduccion getNucleoTramosConduccion(){
    	
    	NucleoTramosConduccion nucleoTramosConduccion = new NucleoTramosConduccion();
    	
    	nucleoTramosConduccion.setCodProvTramosConduccion((String) jComboBoxProvinciaTramosConduccion.getSelectedItem());
    	nucleoTramosConduccion.setCodMunicTramosConduccion((String) jComboBoxMunicipioTramosConduccion.getSelectedItem());
        
        if (jTextFieldCodOrden.getText()!=null){
        	nucleoTramosConduccion.setCodOrdenTramosConduccion(jTextFieldCodOrden.getText());
        }
        
        nucleoTramosConduccion.setCodProvNucleo(((Provincia) jComboBoxProvinciaNucleo.getSelectedItem()).getIdProvincia());
        nucleoTramosConduccion.setCodMunicNucleo(((Municipio) jComboBoxMunicipioNucleo.getSelectedItem()).getIdIne());
        nucleoTramosConduccion.setCodEntNucleo(((EntidadesSingularesEIEL) jComboBoxEntidad.getSelectedItem()).getCodINEEntidad());
        nucleoTramosConduccion.setCodPoblNucleo(((NucleosPoblacionEIEL) jComboBoxNucleo.getSelectedItem()).getCodINEPoblamiento()); 
        
        if (jTextFieldObserv.getText()!=null){
        	nucleoTramosConduccion.setObservaciones(jTextFieldObserv.getText());
        }
        
        if (jTextFieldClave.getText()!=null){
        	nucleoTramosConduccion.setClaveTramosConduccion(jTextFieldClave.getText());
        }
        
        if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
        	String fechas=jTextFieldFechaRevision.getText();
        	String anio=fechas.substring(0,4);
        	String mes=fechas.substring(5,7);
        	String dia=fechas.substring(8,10);
        	
        	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
        	nucleoTramosConduccion.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

        }  
        else{
        	nucleoTramosConduccion.setFechaRevision(null);
        }
        
       if (jComboBoxEstadoRevision.getSelectedPatron()!=null)
    	   nucleoTramosConduccion.setEstadoRevision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
       
        
        if (jTextFieldPmi.getText()!=null && !jTextFieldPmi.getText().equals("")){
        	nucleoTramosConduccion.setPmi(new Float(jTextFieldPmi.getText()));
        }
        else
        {
        	float valor=0;
        	nucleoTramosConduccion.setPmi(valor);
        }
        
        if (jTextFieldPmf.getText()!=null && !jTextFieldPmf.getText().equals("")){
        	nucleoTramosConduccion.setPmf(new Float(jTextFieldPmf.getText()));
        }
        else
        {
        	float valor=0;
        	nucleoTramosConduccion.setPmf(valor);
        }
    	
    	return nucleoTramosConduccion;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarNucleoTramosConduccion();
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
    		
    		jLabelClave = new JLabel("", JLabel.CENTER); 
    		jLabelClave.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"));
    		
    		jLabelPmi = new JLabel("", JLabel.CENTER); 
    		jLabelPmi.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pmi"));
    		
    		jLabelPmf = new JLabel("", JLabel.CENTER); 
    		jLabelPmf.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pmf"));
            
            jPanelInfoNucleo.add(jLabelObservaciones,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelInfoNucleo.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1,1, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
//            jPanelInfoNucleo.add(jLabelClave,
//                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
//                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//                            new Insets(5, 5, 5, 5), 0, 0));
//            
//            jPanelInfoNucleo.add(getJTextFieldClave(), 
//                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
//                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//                            new Insets(5, 5, 5, 5), 0, 0));
//                        
            jPanelInfoNucleo.add(jLabelPmf, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelInfoNucleo.add(getJTextFieldPmf(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
    		
            jPanelInfoNucleo.add(jLabelPmi, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));          
            
            jPanelInfoNucleo.add(getJTextFieldPmi(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
    	}
    	return jPanelInfoNucleo;
    }
    
    private JTextField getJTextFieldPmi()
    {
    	if (jTextFieldPmi  == null)
    	{
    		jTextFieldPmi  = new TextField(8);    	
    		jTextFieldPmi.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPmi, 8, AppContext.getApplicationContext().getMainFrame());
    			}
    		});
    		
    	}
    	return jTextFieldPmi;
    }
    
    private JTextField getJTextFieldPmf()
    {
    	if (jTextFieldPmf  == null)
    	{
    		jTextFieldPmf  = new TextField(8);    	
    		jTextFieldPmf.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPmf, 8, AppContext.getApplicationContext().getMainFrame());
    			}
    		});
    		
    	}
    	return jTextFieldPmf;
    }
    
    private JTextField getJTextFieldObserv()
    {
    	if (jTextFieldObserv  == null)
    	{
    		jTextFieldObserv  = new TextField(50);
    		jTextFieldObserv.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldObserv, 50, ((AppContext) AppContext.getApplicationContext()).getMainFrame());
    			}
    		});
    	}
    	return jTextFieldObserv;
    }
    
    private JTextField getJTextFieldClave()
    {
    	if (jTextFieldClave  == null)
    	{
    		jTextFieldClave  = new TextField(2);
    		jTextFieldClave.setEnabled(false);

    		jTextFieldClave.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldClave, 2, ((AppContext) AppContext.getApplicationContext()).getMainFrame());
    			}
    		});
    	}
    	return jTextFieldClave;
    }
    
    private JPanel getJPanelIdentTramosConduccion()
    {
        if (jPanelIdentTramosConduccion == null)
        {   
        	jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            
        	jLabelCodProvTramosConduccion = new JLabel("", JLabel.CENTER); 
            jLabelCodProvTramosConduccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            
            jLabelCodMunicTramosConduccion = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicTramosConduccion.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 

            jLabelCodOrden  = new JLabel("", JLabel.CENTER);
            jLabelCodOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            jPanelIdentTramosConduccion = new JPanel(new GridBagLayout());
            jPanelIdentTramosConduccion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identitytramocn"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
        	
            
            jPanelIdentTramosConduccion.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTramosConduccion.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTramosConduccion.add(jLabelCodProvTramosConduccion, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTramosConduccion.add(getJComboBoxProvinciaTramosConduccion(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTramosConduccion.add(jLabelCodMunicTramosConduccion, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTramosConduccion.add(getJComboBoxMunicipioTramosConduccion(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentTramosConduccion.add(jLabelCodOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
            jPanelIdentTramosConduccion.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            
        }
        return jPanelIdentTramosConduccion;
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
    
   private JComboBox getJComboBoxProvinciaTramosConduccion()
    {
        if (jComboBoxProvinciaTramosConduccion == null)
        {
        	jComboBoxProvinciaTramosConduccion  = new JComboBox();
        	jComboBoxProvinciaTramosConduccion.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxProvinciaTramosConduccion.setEnabled(false);
            
        	jComboBoxProvinciaTramosConduccion.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxProvinciaTramosConduccion.getSelectedIndex()==0)
            		{
            			jComboBoxMunicipioTramosConduccion.removeAllItems();
            		}
            		else
            		{
            			EdicionOperations oper = new EdicionOperations();
            			EdicionUtils.cargarLista(getJComboBoxMunicipioTramosConduccion(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaTramosConduccion.getSelectedItem(),idMunicipioSelected));
            		}


            	}
            });
        }
        return jComboBoxProvinciaTramosConduccion;
    }
    
    private JComboBox getJComboBoxMunicipioTramosConduccion()
    {
        if (jComboBoxMunicipioTramosConduccion == null)
        {
        	jComboBoxMunicipioTramosConduccion  = new JComboBox();        	
        	jComboBoxMunicipioTramosConduccion.setRenderer(new UbicacionListCellRenderer());  
        	jComboBoxMunicipioTramosConduccion.setEnabled(false);
        }
        return jComboBoxMunicipioTramosConduccion;
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
	            			//System.out.println("PASO1");
	            			//EdicionUtils.cargarLista(jComboBoxNucleo, oper.obtenerNucleosConNombre(entidad)); 
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
        this.setSize(TramosConduccionNucleosPanel.DIM_X,TramosConduccionNucleosPanel.DIM_Y);
        
        this.add(getJPanelIdentTramosConduccion(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
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
            EdicionUtils.cargarLista(getJComboBoxProvinciaTramosConduccion(), lst);

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                	oper.obtenerProvincias());
            Provincia p = new Provincia();
            p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
            p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
            getJComboBoxProvinciaNucleo().setSelectedItem(p);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvinciaTramosConduccion(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));    

            EdicionUtils.cargarLista(getJComboBoxProvinciaNucleo(), 
                    oper.obtenerProvinciasConNombre());     
        }
        
        loadTramosConduccion();
               
    }
    
    private void loadTramosConduccion(){
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("conduccion");
    	
    	if (object != null && object instanceof TramosConduccionEIEL){
    		
    		TramosConduccionEIEL conduccion = (TramosConduccionEIEL)object;
    		
    		if (conduccion.getCodINEProvincia()!=null){
            	jComboBoxProvinciaTramosConduccion.setSelectedItem(conduccion.getCodINEProvincia());
            }
            else{
            	jComboBoxProvinciaTramosConduccion.setSelectedIndex(0);
            }
            
            if (conduccion.getCodINEMunicipio() != null){
            	jComboBoxMunicipioTramosConduccion.setSelectedItem(conduccion.getCodINEMunicipio());
            }
            else{
            	jComboBoxMunicipioTramosConduccion.setSelectedIndex(0);
            }
            
            if (conduccion.getTramo_cn() != null){
        		jTextFieldCodOrden.setText(conduccion.getTramo_cn());
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
        if (tramosConduccion != null){
			lstNucleos = operations.getLstNucleosTramosConduccion(
					tramosConduccion.getClave(), 
					tramosConduccion.getCodINEProvincia(), 
					tramosConduccion.getCodINEMunicipio(), 
					tramosConduccion.getTramo_cn()
					);
        }else{
	        int idConduccion= Integer.parseInt(Identificadores.get("ID_Conduccion").toString());
	        
	        //Obtener la lista de núcleos de población        
	        
	        //se recuperan por este orden: id, nif, codigoderecho, identificaciontitular
	        lstNucleos = operations.getLstNucleosTramosConduccion(idConduccion);
	        
	        loadDataIdentificacion();
        }
        ((NucleosTramosConduccionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).setData(lstNucleos);
        getJTableListaNucleos().updateUI();
        ((NucleosTramosConduccionEIELTableModel)((TableSorted)getJTableListaNucleos().getModel()).getTableModel()).fireTableDataChanged();
        
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
	        	if (feature.getAttribute(esquema.getAttributeByColumn("tramo_cn"))!=null){
	        		orden_tp=(feature.getAttribute(esquema.getAttributeByColumn("tramo_cn"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();        	
	        	loadDataIdentificacion(clave, codprov, codmunic, orden_tp);      	
		}
		getJTableListaNucleos().updateUI();
	}

	public void loadDataIdentificacion(String clave, String codprov,
			String codmunic, String orden_tp) {
		
		//Datos identificacion
    	if (clave != null){
    		jTextFieldClave.setText(clave);
    	}
    	else{
    		jTextFieldClave.setText("");
    	}
        
        if (codprov != null){
        	jComboBoxProvinciaTramosConduccion.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvinciaTramosConduccion.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipioTramosConduccion.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipioTramosConduccion.setSelectedIndex(-1);
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
	
	  private TramosConduccionEIEL tramosConduccion = null;
	    public void tabbedChanged(TramosConduccionEIEL tramosConduccion){
	    	this.tramosConduccion = tramosConduccion;
	    	
	    	loadDataIdentificacion(tramosConduccion.getClave(),
	    			tramosConduccion.getCodINEProvincia(),
	    			tramosConduccion.getCodINEMunicipio(),
	    			tramosConduccion.getTramo_cn());
	    	
	    	enter();
	    }
        
}  