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
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.PuntoVertidoEmisario;
import com.geopista.app.eiel.models.PuntosVertidoEmisarioEIELTableModel;
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


public class EmisariosPuntosVertidoPanel extends JPanel implements FeatureExtendedPanel {
    
	private static int DIM_X = 500;
	private static int DIM_Y = 450;
	
	ArrayList lstNucleos = new ArrayList();
	
	private Blackboard Identificadores = AppContext.getApplicationContext().getBlackboard();
	// Panels
	private JPanel jPanelIdentEmisario = null;
	private JPanel jPanelPuntosVertido = null;
	private JPanel jPanelIdentPuntoVertido = null;
	private JPanel jPanelInfoPuntoVertido = null;
	private JPanel jPanelRevisionPuntoVertido = null;
	private JPanel jPanelBotonera = null;
	// Campos Clave
	private JLabel jLabelClave;
	private JLabel jLabelCodProvEmisario;
	private JLabel jLabelCodMunicEmisario;
	private JLabel jLabelCodOrden;
	private TextField jTextFieldClave;
	private JComboBox jComboBoxProvinciaEmisario;
	private JComboBox jComboBoxMunicipioEmisario;
	private TextField jTextFieldCodOrden;
	// Campos Vinculados
	private JLabel jLabelCodProvPuntoVertido;
	private JLabel jLabelCodMunicNucleo;
	private JLabel jLabelClaveSing;
	private JLabel jLabelOrden;
	private JComboBox jComboBoxProvinciaPuntoVertido;
	private JComboBox jComboBoxMunicipioPuntoVertido;
	private JComboBox jComboBoxClavePuntoVertido;
	private JComboBox jComboBoxOrden;
	// Campos de Informacion
	private JLabel jLabelObservaciones;
	private TextField jTextFieldObserv;
	private JLabel jLabelPMI;
	private TextField jTextFieldPMI;
	private JLabel jLabelPMF;
	private TextField jTextFieldPMF;
	private JLabel jLabelFechaRevision;
	private JLabel jLabelEstadoRevision;
	private TextField jTextFieldFechaRevision;
	private ComboBoxEstructuras jComboBoxEstadoRevision = null;
	// Botones y Widgets
	private JButton jButtonAniadir;
	private JButton jButtonEliminar;
	private JButton jButtonLimpiar;
	private JPanel jPanelListaPV;
	private JScrollPane jScrollPaneListaPV;
	private JTable jTableListaPV;
	private Object tableListaNucleosModel;
	
	private String idMunicipioSelected;
    
    
    public EmisariosPuntosVertidoPanel(){
        try{
            jbInit();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
        
    private JPanel getJPanelPuntosVertido(){
    	if (jPanelPuntosVertido == null){
    		jPanelPuntosVertido = new JPanel();
    		jPanelPuntosVertido = new JPanel(new GridBagLayout());
    		jPanelPuntosVertido.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.pv"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jPanelPuntosVertido.add(getJPanelIdentNucleo(), 
            		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));        	
    		jPanelPuntosVertido.add(getJPanelInfoPuntoVertido(), 
            		new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		jPanelPuntosVertido.add(getJPanelRevision(), 
            		new GridBagConstraints(0,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		jPanelPuntosVertido.add(getJPanelBotonera(), 
            		new GridBagConstraints(0,3,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		jPanelPuntosVertido.add(getJPanelListaPuntosVertido(), 
            		new GridBagConstraints(0,4,1,1,0.01, 0.01,GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	}
    	return jPanelPuntosVertido;
    }
    
    private JPanel getJPanelListaPuntosVertido(){
    	if (jPanelListaPV == null){
    		jPanelListaPV = new JPanel();
    		jPanelListaPV.setLayout(new GridBagLayout());
    		jPanelListaPV.setSize(10,10);
    		jPanelListaPV.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.listpuntosvertido"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jPanelListaPV.add(getJScrollPaneListaPuntosVertido(), 
    		new GridBagConstraints(0,0,1,1,1, 1,GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	}
    	return jPanelListaPV;
    }
    
    public JScrollPane getJScrollPaneListaPuntosVertido(){
    	if (jScrollPaneListaPV == null){
    		jScrollPaneListaPV = new JScrollPane();
    		jScrollPaneListaPV.setViewportView(getJTableListaPuntosVertido());
    		jScrollPaneListaPV.setSize(10,10);
    	}
    	return jScrollPaneListaPV;
    }
    
    public JTable getJTableListaPuntosVertido(){
    	if (jTableListaPV  == null){
    		jTableListaPV   = new JTable();
    		tableListaNucleosModel  = new PuntosVertidoEmisarioEIELTableModel();
    		TableSorted tblSorted= new TableSorted((TableModel)tableListaNucleosModel);
    		tblSorted.setTableHeader(jTableListaPV.getTableHeader());
    		jTableListaPV.setModel(tblSorted);
    		jTableListaPV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		jTableListaPV.setCellSelectionEnabled(false);
    		jTableListaPV.setColumnSelectionAllowed(false);
    		jTableListaPV.setRowSelectionAllowed(true);
    		jTableListaPV.getTableHeader().setReorderingAllowed(false);
    		//Dimension d = jTableListaNucleos.getPreferredSize();
    		// Make changes to [i]d[/i] if you like...
    		jTableListaPV.setPreferredScrollableViewportSize(new Dimension(100,100));

    		jTableListaPV.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    loadSelectedItem();
                }
            });
    		((PuntosVertidoEmisarioEIELTableModel)((TableSorted)getJTableListaPuntosVertido().getModel()).getTableModel()).setData(lstNucleos);
    	}
    	return jTableListaPV;
    }
    
    private void loadSelectedItem(){
    	int selectedRow = getJTableListaPuntosVertido().getSelectedRow();
    	Object selectedItem = ((PuntosVertidoEmisarioEIELTableModel)((TableSorted)getJTableListaPuntosVertido().getModel()).getTableModel()).getValueAt(selectedRow);
    	if (selectedItem != null && selectedItem instanceof PuntoVertidoEmisario){
    		loadPuntoVertidoEmisario((PuntoVertidoEmisario)selectedItem);
    	}    	
    }
    
    private void loadPuntoVertidoEmisario(PuntoVertidoEmisario puntoVertidoEmisario){
    	if (puntoVertidoEmisario.getCodProvPuntoVertido()!=null){
        	jComboBoxProvinciaPuntoVertido.setSelectedIndex(
        		provinciaIndexSeleccionar(puntoVertidoEmisario.getCodProvPuntoVertido())
        			);
        }
        else{
        	jComboBoxProvinciaPuntoVertido.setSelectedIndex(0);
        }
        
        if (puntoVertidoEmisario.getCodMunicPuntoVertido() != null){
        	jComboBoxMunicipioPuntoVertido.setSelectedIndex(
        			municipioIndexSeleccionar(puntoVertidoEmisario.getCodMunicPuntoVertido())
        	);
        	
        	
        }
        else{
        	jComboBoxMunicipioPuntoVertido.setSelectedIndex(0);
        }
        
        if (puntoVertidoEmisario.getCodClavePuntoVertido() != null){
        	jComboBoxClavePuntoVertido.setSelectedItem(puntoVertidoEmisario.getCodClavePuntoVertido());
        }
        else{
        	jComboBoxClavePuntoVertido.setSelectedIndex(0);
        }
        
        if (puntoVertidoEmisario.getCodOrdenPuntoVertido() != null){
        	jComboBoxOrden.setSelectedIndex(
        			nucleoPoblacionIndexSeleccionar(puntoVertidoEmisario.getCodOrdenPuntoVertido())
        			);
        	
        }
        else{
        	jComboBoxOrden.setSelectedIndex(0);
        }
        
        if (puntoVertidoEmisario.getObservaciones() != null){
    		jTextFieldObserv.setText(puntoVertidoEmisario.getObservaciones());
    	}
    	else{
    		jTextFieldObserv.setText("");
    	}
       
        if (puntoVertidoEmisario.getFechaRevision() != null && puntoVertidoEmisario.getFechaRevision().equals( new java.util.Date()) ){
    		jTextFieldFechaRevision.setText(puntoVertidoEmisario.getFechaRevision().toString());
    	}
    	else{
    	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);
            
    		jTextFieldFechaRevision.setText(datetime);
    	}
        
        if (puntoVertidoEmisario.getEstadoRevision() != null){
        	jComboBoxEstadoRevision.setSelectedPatron(puntoVertidoEmisario.getEstadoRevision().toString());
    	}
    	else{
    		jComboBoxEstadoRevision.setSelectedIndex(0);
    	}
    	
        if (puntoVertidoEmisario.getPmf() != null){
    		jTextFieldPMF.setText(puntoVertidoEmisario.getPmf().toString());
    	}
    	else{
    		jTextFieldPMF.setText("0");
    	}
        
        if (puntoVertidoEmisario.getPmi() != null){
    		jTextFieldPMI.setText(puntoVertidoEmisario.getPmi().toString());
    	}
    	else{
    		jTextFieldPMI.setText("0");
    	}
        
    }
    
    private void reset(){
    	jComboBoxProvinciaPuntoVertido.setSelectedIndex(0);    	
    	jTextFieldObserv.setText("");
    	jComboBoxEstadoRevision.setSelectedIndex(0);
    	jTextFieldPMF.setText("");
    	jTextFieldPMI.setText("");
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
                    annadirPuntoVertido();
                }
            });
    	}
    	return jButtonAniadir;
    	
    }
    
    private void annadirPuntoVertido(){
    	EdicionOperations operations = new EdicionOperations();
    	if(!datosMinimosYCorrectos()){
    		JOptionPane.showMessageDialog(this,null,I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
    				JOptionPane.ERROR_MESSAGE);
    	} else {
    		operations.savePuntoVertidoEmisario(getPuntoVertidoEmisario());
        	enter();
    	}
    }
    
    private void eliminarPuntoVertidoEmisario(){
    	int selectedRow = getJTableListaPuntosVertido().getSelectedRow();
    	Object selectedItem = ((PuntosVertidoEmisarioEIELTableModel)((TableSorted)getJTableListaPuntosVertido().getModel()).getTableModel()).getValueAt(selectedRow);
    	if (selectedItem != null && selectedItem instanceof PuntoVertidoEmisario){
    		EdicionOperations operations = new EdicionOperations();
    		operations.deletePuntoVertidoEmisario((PuntoVertidoEmisario)selectedItem);
    		reset();
    		enter();
    	}  
    }
    
    public boolean datosMinimosYCorrectos(){
        return  ((jTextFieldClave.getText()!=null && !jTextFieldClave.getText().equalsIgnoreCase("")) &&
        (jTextFieldCodOrden.getText()!=null && !jTextFieldCodOrden.getText().equalsIgnoreCase("")) &&
        (jComboBoxProvinciaEmisario!=null && jComboBoxProvinciaEmisario.getSelectedItem()!=null && jComboBoxProvinciaEmisario.getSelectedIndex()>0) &&
        (jComboBoxProvinciaPuntoVertido!=null && jComboBoxProvinciaPuntoVertido.getSelectedItem()!=null && jComboBoxProvinciaPuntoVertido.getSelectedIndex()>0) &&        
        (jComboBoxMunicipioEmisario!=null && jComboBoxMunicipioEmisario.getSelectedItem()!=null && jComboBoxMunicipioEmisario.getSelectedIndex()>0) && 
        (jComboBoxMunicipioPuntoVertido!=null && jComboBoxMunicipioPuntoVertido.getSelectedItem()!=null && jComboBoxMunicipioPuntoVertido.getSelectedIndex()>0) && 
        (jComboBoxOrden!=null && jComboBoxOrden.getSelectedItem()!=null && jComboBoxOrden.getSelectedIndex()>0)); 
        
    }
    
    private PuntoVertidoEmisario getPuntoVertidoEmisario(){
    	PuntoVertidoEmisario puntoVertidoEmisario = new PuntoVertidoEmisario();
    	puntoVertidoEmisario.setCodProvEmisario((String) jComboBoxProvinciaEmisario.getSelectedItem());
    	puntoVertidoEmisario.setCodMunicEmisario((String) jComboBoxMunicipioEmisario.getSelectedItem());
        if (jTextFieldClave.getText()!=null){
        	puntoVertidoEmisario.setClaveEmisario(jTextFieldClave.getText());
        }
        if (jTextFieldCodOrden.getText()!=null){
        	puntoVertidoEmisario.setCodOrdenEmisario(jTextFieldCodOrden.getText());
        }
        
        puntoVertidoEmisario.setCodProvPuntoVertido(((Provincia) jComboBoxProvinciaPuntoVertido.getSelectedItem()).getIdProvincia());
        puntoVertidoEmisario .setCodMunicPuntoVertido(((Municipio) jComboBoxMunicipioPuntoVertido.getSelectedItem()).getIdIne());
        puntoVertidoEmisario.setCodClavePuntoVertido((String) jComboBoxClavePuntoVertido.getSelectedItem());
        puntoVertidoEmisario.setCodOrdenPuntoVertido(((String) jComboBoxOrden.getSelectedItem())); 
        
        if (jTextFieldObserv.getText()!=null){
        	puntoVertidoEmisario.setObservaciones(jTextFieldObserv.getText());
        }
        if (jTextFieldFechaRevision.getText()!=null && !jTextFieldFechaRevision.getText().equals("")){
        	String fechas=jTextFieldFechaRevision.getText();
        	String anio=fechas.substring(0,4);
        	String mes=fechas.substring(5,7);
        	String dia=fechas.substring(8,10);
        	
        	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
        	puntoVertidoEmisario.setFechaRevision(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate()));

        }  
        else{
        	puntoVertidoEmisario.setFechaRevision(null);
        }
        if (jComboBoxEstadoRevision.getSelectedPatron()!=null) 
        	puntoVertidoEmisario.setEstadoRevision(Integer.parseInt(jComboBoxEstadoRevision.getSelectedPatron()));
        
        if (jTextFieldPMF.getText()!=null && !jTextFieldPMF.getText().equals("")){
        	puntoVertidoEmisario.setPmf(new Float(jTextFieldPMF.getText()));
        }
        else{
        	puntoVertidoEmisario.setPmf(new Float(0));
        }
        if (jTextFieldPMI.getText()!=null && !jTextFieldPMI.getText().equals("")){
        	puntoVertidoEmisario.setPmi(new Float(jTextFieldPMI.getText()));
        }
        else{
        	puntoVertidoEmisario.setPmi(new Float(0));
        }
    	return puntoVertidoEmisario;
    } 
    
    
    public JButton getJButtonEliminar(){
    	if (jButtonEliminar == null){
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.panel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarPuntoVertidoEmisario();
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
    	if (jPanelRevisionPuntoVertido == null){
    		jPanelRevisionPuntoVertido = new JPanel();
    		jPanelRevisionPuntoVertido.setLayout(new GridBagLayout());
    		jPanelRevisionPuntoVertido.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revisionnucleo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jLabelFechaRevision  = new JLabel("", JLabel.CENTER); 
    		jLabelFechaRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
            jLabelEstadoRevision  = new JLabel("", JLabel.CENTER); 
            jLabelEstadoRevision.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
            jPanelRevisionPuntoVertido.add(jLabelFechaRevision,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevisionPuntoVertido.add(getJTextFieldFechaRevision(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevisionPuntoVertido.add(jLabelEstadoRevision, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelRevisionPuntoVertido.add(getjComboBoxEstadoRevision(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));    		
    	}
    	return jPanelRevisionPuntoVertido;
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
    
    private JPanel getJPanelInfoPuntoVertido(){   	
    	if (jPanelInfoPuntoVertido == null){   		
    		jPanelInfoPuntoVertido = new JPanel();
    		jPanelInfoPuntoVertido.setLayout(new GridBagLayout());   		
    		jPanelInfoPuntoVertido.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.infonucleo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
    		jLabelObservaciones = new JLabel("", JLabel.CENTER); 
    		jLabelObservaciones.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.observ")); 
            jLabelPMF = new JLabel("", JLabel.CENTER); 
            jLabelPMF.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.PMF")); 
            jLabelPMI = new JLabel("", JLabel.CENTER); 
            jLabelPMI.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.PMI")); 
            jPanelInfoPuntoVertido.add(jLabelObservaciones,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInfoPuntoVertido.add(getJTextFieldObserv(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInfoPuntoVertido.add(jLabelPMF, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInfoPuntoVertido.add(getJTextFieldPMF(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInfoPuntoVertido.add(jLabelPMI, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInfoPuntoVertido.add(getJTextFieldPMI(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
    	}
    	return jPanelInfoPuntoVertido;
    }

    
    private JTextField getJTextFieldObserv(){
    	if (jTextFieldObserv  == null){
    		jTextFieldObserv  = new TextField(50);
    	}
    	return jTextFieldObserv;
    }
  
    private JTextField getJTextFieldPMF(){
    	if (jTextFieldPMF  == null){
    		jTextFieldPMF  = new TextField(8);
    		jTextFieldPMF.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPMF, 8, AppContext.getApplicationContext().getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPMF;
    }
    
    private JTextField getJTextFieldPMI(){
    	if (jTextFieldPMI  == null){
    		jTextFieldPMI  = new TextField(8);
    		jTextFieldPMI.addCaretListener(new CaretListener()
    		{
    			public void caretUpdate(CaretEvent evt)
    			{
    				EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPMI, 8, AppContext.getApplicationContext().getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPMI;
    }
    
    private JPanel getJPanelIdentEmisario(){
        if (jPanelIdentEmisario == null){   
        	jLabelClave = new JLabel("", JLabel.CENTER); 
            jLabelClave.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave"))); 
            jLabelCodProvEmisario = new JLabel("", JLabel.CENTER); 
            jLabelCodProvEmisario.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov")));             
            jLabelCodMunicEmisario = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicEmisario.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelCodOrden  = new JLabel("", JLabel.CENTER);
            jLabelCodOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));            
            jPanelIdentEmisario = new JPanel(new GridBagLayout());
            jPanelIdentEmisario.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identityemis"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));         	            
            jPanelIdentEmisario.add(jLabelClave,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));           
            jPanelIdentEmisario.add(getJTextFieldClave(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentEmisario.add(jLabelCodProvEmisario, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));           
            jPanelIdentEmisario.add(getJComboBoxProvinciaEmisario(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentEmisario.add(jLabelCodMunicEmisario, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));                       
            jPanelIdentEmisario.add(getJComboBoxMunicipioEmisario(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentEmisario.add(jLabelCodOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));           
            jPanelIdentEmisario.add(getJTextFieldOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));  
        }
        return jPanelIdentEmisario;
    }
    
    private JTextField getJTextFieldOrden(){
    	if (jTextFieldCodOrden   == null){
    		jTextFieldCodOrden = new TextField(3);
    		jTextFieldCodOrden.setEnabled(false);
    	}
    	return jTextFieldCodOrden;
    }
    
    private JTextField getJTextFieldClave(){
        if (jTextFieldClave == null){
            jTextFieldClave = new TextField(2);
            jTextFieldClave.setEnabled(false);
        }
        return jTextFieldClave;
    }
    
    private JComboBox getJComboBoxProvinciaEmisario(){
        if (jComboBoxProvinciaEmisario == null){
        	jComboBoxProvinciaEmisario  = new JComboBox();
        	jComboBoxProvinciaEmisario.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxProvinciaEmisario.setEnabled(false);
        	jComboBoxProvinciaEmisario.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent e){   
            		if (jComboBoxProvinciaEmisario.getSelectedIndex()==0){
            			jComboBoxMunicipioEmisario.removeAllItems();
            		}
            		else{
            			EdicionOperations oper = new EdicionOperations();
            			EdicionUtils.cargarLista(getJComboBoxMunicipioEmisario(), 
            					oper.obtenerIdMunicipios((String)jComboBoxProvinciaEmisario.getSelectedItem(),idMunicipioSelected));
            		}
            	}
            });
        }
        return jComboBoxProvinciaEmisario;
    }
    
    private JComboBox getJComboBoxMunicipioEmisario(){
        if (jComboBoxMunicipioEmisario == null){
        	jComboBoxMunicipioEmisario  = new JComboBox();        	
        	jComboBoxMunicipioEmisario.setRenderer(new UbicacionListCellRenderer());  
        	jComboBoxMunicipioEmisario.setEnabled(false);
        }
        return jComboBoxMunicipioEmisario;
    }
    
    
    private JPanel getJPanelIdentNucleo(){
        if (jPanelIdentPuntoVertido == null){   
        	jPanelIdentPuntoVertido = new JPanel(new GridBagLayout());
        	jPanelIdentPuntoVertido.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identitypuntver"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            jLabelCodProvPuntoVertido = new JLabel("", JLabel.CENTER); 
            jLabelCodProvPuntoVertido.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov")));             
            jLabelCodMunicNucleo = new JLabel("", JLabel.CENTER); 
            jLabelCodMunicNucleo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            jLabelClaveSing  = new JLabel("", JLabel.CENTER);
            jLabelClaveSing.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.clave")));            
            jLabelOrden   = new JLabel("", JLabel.CENTER);
            jLabelOrden.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.orden")));
            
            
            jPanelIdentPuntoVertido.add(jLabelClaveSing, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));      
            jPanelIdentPuntoVertido.add(getJComboBoxClavePuntoVertido(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));  
            
            jPanelIdentPuntoVertido.add(jLabelCodProvPuntoVertido, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));                  
            jPanelIdentPuntoVertido.add(getJComboBoxProvinciaPuntoVertido(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            jPanelIdentPuntoVertido.add(jLabelCodMunicNucleo, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));                 
            jPanelIdentPuntoVertido.add(getJComboBoxMunicipioPuntoVertido(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));           
            
            jPanelIdentPuntoVertido.add(jLabelOrden, 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));           
            jPanelIdentPuntoVertido.add(getJComboBoxOrden(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));   
        }
        return jPanelIdentPuntoVertido;
    }
    
    private JComboBox getJComboBoxProvinciaPuntoVertido()
    {
        if (jComboBoxProvinciaPuntoVertido == null)
        {
        	EdicionOperations oper = new EdicionOperations();
        	ArrayList<Provincia> listaProvincias = oper.obtenerProvinciasConNombre();
        	jComboBoxProvinciaPuntoVertido = new JComboBox(listaProvincias.toArray());
        	jComboBoxProvinciaPuntoVertido.setSelectedIndex(this.provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
        	jComboBoxProvinciaPuntoVertido.setRenderer(new UbicacionListCellRenderer());            
        	jComboBoxProvinciaPuntoVertido.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   
            		if (getJComboBoxMunicipioPuntoVertido() != null){
            			if (jComboBoxProvinciaPuntoVertido.getSelectedIndex()==0)
            			{
            				jComboBoxMunicipioPuntoVertido.removeAllItems();
            				jComboBoxMunicipioPuntoVertido.addItem("");
            			}
            			else
            			{
            				EdicionOperations oper = new EdicionOperations();
            				jComboBoxProvinciaPuntoVertido.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
            				
            				if ( jComboBoxProvinciaPuntoVertido.getSelectedItem() != null ){
            					EdicionUtils.cargarLista(getJComboBoxMunicipioPuntoVertido(), 
            							oper.obtenerTodosMunicipios(((Provincia)jComboBoxProvinciaPuntoVertido.getSelectedItem()).getIdProvincia()) );
            					jComboBoxMunicipioPuntoVertido.setSelectedIndex(
            							municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio)
            							);
            				}
            			}
            		}

            	}
            });

        }

        return jComboBoxProvinciaPuntoVertido;
    }
    
    private JComboBox getJComboBoxMunicipioPuntoVertido()
    {
        if (jComboBoxMunicipioPuntoVertido == null)
        {
        	jComboBoxMunicipioPuntoVertido = new JComboBox();
        	jComboBoxMunicipioPuntoVertido.setRenderer(new UbicacionListCellRenderer());
        	jComboBoxMunicipioPuntoVertido.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   

            		if (jComboBoxMunicipioPuntoVertido.getSelectedIndex()==0)
            		{
            		}
            		else
            		{
            			MunicipioEIEL  municipio = new MunicipioEIEL();  
            			EntidadEIEL entidad = new EntidadEIEL();
            			String clave = "";
            			if (jComboBoxProvinciaPuntoVertido.getSelectedItem() != null){
            				municipio.setCodProvincia(((Provincia) jComboBoxProvinciaPuntoVertido.getSelectedItem()).getIdProvincia());
            				entidad.setCodProvincia(((Provincia) jComboBoxProvinciaPuntoVertido.getSelectedItem()).getIdProvincia());
            			}
            			if (jComboBoxMunicipioPuntoVertido.getSelectedItem() != null){
            				municipio.setCodMunicipio(((Municipio) jComboBoxMunicipioPuntoVertido.getSelectedItem()).getIdIne());
            				entidad.setCodMunicipio(((Municipio) jComboBoxMunicipioPuntoVertido.getSelectedItem()).getIdIne());
            			}
            			
            			if (jComboBoxClavePuntoVertido.getSelectedItem() != null){
            				clave = (String) jComboBoxClavePuntoVertido.getSelectedItem();
            			}
            			
            			EdicionOperations oper = new EdicionOperations();
            			if ((municipio!=null) && (municipio.getCodMunicipio()!=null)){
	            			EdicionUtils.cargarLista(getJComboBoxOrden(), 
	            					oper.obtenerOrdenPuntosVertido(municipio, clave));
            			}
 
            		}
            	}
            });
        }
        return jComboBoxMunicipioPuntoVertido;
    }
    
    private JComboBox getJComboBoxClavePuntoVertido()
    {
        if (jComboBoxClavePuntoVertido  == null)
        {       	
        	jComboBoxClavePuntoVertido = new JComboBox();
        	jComboBoxClavePuntoVertido.addItem("PV");
        	jComboBoxClavePuntoVertido.setEditable(true);
        	jComboBoxClavePuntoVertido.setRenderer(new UbicacionListCellRenderer());
        	
        }
        return jComboBoxClavePuntoVertido;
    }
    
    private JComboBox getJComboBoxOrden(){
        if (jComboBoxOrden  == null){
        	jComboBoxOrden = new JComboBox();
        	jComboBoxOrden.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxOrden;
    }
        
    private void jbInit() throws Exception{
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        this.setName(I18N.get("LocalGISEIEL","localgiseiel.Emisariosnucleo.panel.title"));
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        this.setLayout(new GridBagLayout());
        this.setSize(EmisariosPuntosVertidoPanel.DIM_X,EmisariosPuntosVertidoPanel.DIM_Y);
        this.add(getJPanelIdentEmisario(),  new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        this.add(getJPanelPuntosVertido(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));
        
        EdicionOperations oper = new EdicionOperations();
        if (Identificadores.get("ListaProvincias")==null){
            ArrayList lst = oper.obtenerProvincias();
            Identificadores.put("ListaProvincias", lst);
            EdicionUtils.cargarLista(getJComboBoxProvinciaEmisario(), lst);

            EdicionUtils.cargarLista(getJComboBoxProvinciaPuntoVertido(), 
            		oper.obtenerProvinciasConNombre());
            Provincia p = new Provincia();
            p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
            p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
            getJComboBoxProvinciaPuntoVertido().setSelectedItem(p);
        }
        else{
            EdicionUtils.cargarLista(getJComboBoxProvinciaEmisario(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));    

            EdicionUtils.cargarLista(getJComboBoxProvinciaPuntoVertido(), 
                    oper.obtenerProvinciasConNombre());    
        }
        loadEmisario();
    }
    
    public void loadEmisario(){
    	Object object = AppContext.getApplicationContext().getBlackboard().get("emisario");
    	if (object != null && object instanceof EmisariosEIEL){
    		EmisariosEIEL Emisario = (EmisariosEIEL)object;
    		if (Emisario.getClave() != null){
        		jTextFieldClave.setText(Emisario.getClave());
        	} else {
        		jTextFieldClave.setText("");
        	}
            if (Emisario.getCodINEProvincia()!=null){
            	jComboBoxProvinciaEmisario.setSelectedItem(Emisario.getCodINEProvincia());
            } else {
            	jComboBoxProvinciaEmisario.setSelectedIndex(0);
            }
            if (Emisario.getCodINEMunicipio() != null){
            	jComboBoxMunicipioEmisario.setSelectedItem(Emisario.getCodINEMunicipio());
            } else{
            	jComboBoxMunicipioEmisario.setSelectedIndex(0);
            }
            if (Emisario.getCodOrden() != null){
        		jTextFieldCodOrden.setText(Emisario.getCodOrden());
        	} else{
        		jTextFieldCodOrden.setText("");
        	}
    	}
    }
    
    
    public void enter(){
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        //busca los núcleos de población relacionados con el elemento seleccionado 
        EdicionOperations operations = new EdicionOperations();
        if (emisario != null){
			lstNucleos = operations.getLstPuntosVertidoEmisario(
					emisario.getClave(), 
					emisario.getCodINEProvincia(), 
					emisario.getCodINEMunicipio(), 
					emisario.getCodOrden()
					);        	
        }else{
	        int idEmisario= Integer.parseInt(Identificadores.get("ID_Emisario").toString());
	        // Lista de núcleos de población ordenada: id, nif, codigoderecho, identificaciontitular
	        lstNucleos = operations.getLstPuntosVertidoEmisario(idEmisario);

	        
	        loadDataIdentificacion();
        }
        ((PuntosVertidoEmisarioEIELTableModel)((TableSorted)getJTableListaPuntosVertido().getModel()).getTableModel()).setData(lstNucleos);
        getJTableListaPuntosVertido().updateUI();   
        ((PuntosVertidoEmisarioEIELTableModel)((TableSorted)getJTableListaPuntosVertido().getModel()).getTableModel()).fireTableDataChanged();
     
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

	        	
	        	String tramo_em = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("tramo_em"))!=null){
	        		tramo_em=(feature.getAttribute(esquema.getAttributeByColumn("tramo_em"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	
	        	
	        	loadDataIdentificacion(clave, codprov, codmunic, tramo_em);
			}
			getJTableListaPuntosVertido().updateUI();
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
        	jComboBoxProvinciaEmisario.setSelectedItem(codprov);
        }
        else{
        	jComboBoxProvinciaEmisario.setSelectedIndex(-1);
        }
        
        if (codmunic != null){            	
        	jComboBoxMunicipioEmisario.setSelectedItem(codmunic);
        }
        else{
        	jComboBoxMunicipioEmisario.setSelectedIndex(-1);
        }
                
        if (orden_de != null){
    		jTextFieldCodOrden.setText(orden_de);
    	}
    	else{
    		jTextFieldCodOrden.setText("");
    	}
		
	}
    
    public int provinciaIndexSeleccionar(String provinciaId){
		for (int i = 0; i < jComboBoxProvinciaPuntoVertido.getItemCount(); i ++){
			String provinciaSeleccionada = "";
			if (jComboBoxProvinciaPuntoVertido.getItemAt(i) instanceof Provincia){
				provinciaSeleccionada = ((Provincia)jComboBoxProvinciaPuntoVertido.getItemAt(i)).getIdProvincia();
			} else if (jComboBoxProvinciaPuntoVertido.getItemAt(i) instanceof String){
				provinciaSeleccionada = ((String)jComboBoxProvinciaPuntoVertido.getItemAt(i));
			} else {
				provinciaSeleccionada = jComboBoxProvinciaPuntoVertido.getItemAt(i).toString();
			}
			if (provinciaSeleccionada.equals(provinciaId) ){
				return i;
			}
		}
		return -1;
	}
	
	public int municipioIndexSeleccionar(String municipioId){
		if (!municipioId.equals("")){
			for (int i = 0; i < jComboBoxMunicipioPuntoVertido.getItemCount(); i ++){
				try{
					if (((Municipio)jComboBoxMunicipioPuntoVertido.getItemAt(i)).getIdIne().equals(municipioId.substring(2, 5)) ){
						jComboBoxMunicipioPuntoVertido.setEnabled(false);
						return i;
					}
				}catch (StringIndexOutOfBoundsException e) {
					if (((Municipio)jComboBoxMunicipioPuntoVertido.getItemAt(i)).getIdIne().equals(municipioId) ){
						jComboBoxMunicipioPuntoVertido.setEnabled(false);
						return i;
					}
				}
			}
		}
		
		return -1;
	}
	
	public int nucleoPoblacionIndexSeleccionar(String nucleoPoblacion){
		for (int i = 0; i < jComboBoxOrden.getItemCount(); i ++){
			if (((String)jComboBoxOrden.getItemAt(i)).equals(nucleoPoblacion) ){
				return i;
			}
		}
		
		return -1;
	}
	
	private EmisariosEIEL emisario = null;
	    public void tabbedChanged(EmisariosEIEL emisario){
	    	this.emisario = emisario;
	    	
	    	loadDataIdentificacion(emisario.getClave(),
	    			emisario.getCodINEProvincia(),
	    			emisario.getCodINEMunicipio(),
	    			emisario.getCodOrden());
	    	
	    	enter();
	    }
        
}  