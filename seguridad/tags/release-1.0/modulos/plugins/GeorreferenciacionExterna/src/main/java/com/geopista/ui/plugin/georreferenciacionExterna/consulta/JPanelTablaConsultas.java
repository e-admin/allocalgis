package com.geopista.ui.plugin.georreferenciacionExterna.consulta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.dialog.GeorreferenciaExternaConsultaDialog;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanel01;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanel02;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanel03;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanel04;
import com.geopista.ui.plugin.georreferenciacionExterna.paneles.GeorreferenciacionExternaPanel05;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.localgis.ws.servidor.SQLExceptionException0;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.ws.cliente.Test;

public class JPanelTablaConsultas extends JPanel{
	
	private PlugInContext context;
	private ResourceBundle mensajesUsuario;
	private JFrame framePadre;
	
	private JPanel tablaConsultas  = null;
	private JPanel jPanelBotoneraBusquedaConsultas = null;
	private JPanel jPanelTablaBusquedaConsultas = null;
	private javax.swing.JScrollPane listaConsultasJScrollPane;
	private javax.swing.JTable jTableConsultas;
	
    private JButton borrarJButton;
	private JButton nuevaJButton;
    private JButton ejecutarConsultaJButton;
    private JButton modificarJButton;
    
    private TableSorted sorter;
	private ConsultaTableModel consultaTableModel;

    
	private ArrayList listaConsultas = null;
	private ConsultaDatosBean consultaSelected = null;
	private GeorreferenciaExternaConsultaDialog geoDialog;
    
	public JPanelTablaConsultas( PlugInContext context, GeorreferenciaExternaConsultaDialog geoDialog) {
		this.context = context;
		this.geoDialog = geoDialog;
		initComponents();
	}
    private void initComponents() {
    	
    	tablaConsultas = new JPanel();
    	jTableConsultas = new JTable();
    	
    	this.setLayout(new GridBagLayout());

    	listaConsultasJScrollPane = new javax.swing.JScrollPane();
    	
    	this.add(getPanelTablaBusquedaConsultas(), 
				new GridBagConstraints(0,0,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	this.add(getPanelBotonera(), 
				new GridBagConstraints(0,1,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	
    	
    	
    }
    
    public void pintarConsultas(ArrayList lstConsultas) {
		this.listaConsultas = lstConsultas;
		actualizarModelo();
	}
    
    private void actualizarModelo() {
    	consultaTableModel = new ConsultaTableModel();
    	consultaTableModel.setModelData(listaConsultas);
		sorter = new TableSorted(consultaTableModel);
		sorter.setTableHeader(jTableConsultas.getTableHeader());
		jTableConsultas.setModel(sorter);
		TableColumn column = jTableConsultas.getColumnModel().getColumn(
				ConsultaTableModel.idIndex);
		column.setPreferredWidth(5);
		column = jTableConsultas.getColumnModel().getColumn(
				ConsultaTableModel.idNombreConsulta);
		column.setPreferredWidth(15);
		column = jTableConsultas.getColumnModel().getColumn(
				ConsultaTableModel.idDescripcion);
		column.setPreferredWidth(25);
		column = jTableConsultas.getColumnModel().getColumn(
				ConsultaTableModel.idUsuario);
		column.setPreferredWidth(15);
		jTableConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}
    
    
    
	 private JPanel getPanelTablaBusquedaConsultas(){
	    	
	    	if (jPanelTablaBusquedaConsultas == null){
	    		jPanelTablaBusquedaConsultas = new JPanel();
	    		jPanelTablaBusquedaConsultas.setLayout(new GridBagLayout());
	    		jPanelTablaBusquedaConsultas.setBorder(BorderFactory.createTitledBorder
						(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.consultas"), 
								TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

	    		
	    		listaConsultasJScrollPane.setViewportView(jTableConsultas);
	    		listaConsultasJScrollPane.setPreferredSize(new Dimension(650, 490));

	    		jPanelTablaBusquedaConsultas.add(listaConsultasJScrollPane, 
	    				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
	    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
	    		
	    		// Para seleccionar una fila
	    		ListSelectionModel rowSM = jTableConsultas.getSelectionModel();
	    		rowSM.addListSelectionListener(new ListSelectionListener() {
	    			public void valueChanged(ListSelectionEvent e) {
	    				seleccionarConsulta(e);
	    			}
	    		});
	    		
	    		
	    	}
    	
	    	return jPanelTablaBusquedaConsultas;
	 }
	 
	 private void seleccionarConsulta(ListSelectionEvent e) {
		 
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty()) {
			int selectedRow = lsm.getMinSelectionIndex();
			String idConsulta = (String) sorter.getValueAt(selectedRow,
					ConsultaTableModel.idIndex);
			
			for(int i=0; i<listaConsultas.size();i++){
				ConsultaDatosBean consultaDatosBean = (ConsultaDatosBean)listaConsultas.get(i);
				if(consultaDatosBean.getId() == Integer.valueOf(idConsulta)){
					consultaSelected = consultaDatosBean;
				}
			}
			
			
			
		}
	 }
 
    private JPanel getPanelBotonera(){
    	
        
    	if (jPanelBotoneraBusquedaConsultas == null){
    		
    		jPanelBotoneraBusquedaConsultas  = new JPanel();

	    	jPanelBotoneraBusquedaConsultas.setLayout(new GridBagLayout());

	    	jPanelBotoneraBusquedaConsultas.add(getBorrarJButton(), 
					new GridBagConstraints(0,0,1,1, 0.25, 0.25,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
	    	jPanelBotoneraBusquedaConsultas.add(getNuevaJButton(), 
					new GridBagConstraints(1,0,1,1, 0.25, 0.25,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
	    	jPanelBotoneraBusquedaConsultas.add(getModificarJButton(), 
					new GridBagConstraints(2,0,1,1, 0.25, 0.25,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
	    	jPanelBotoneraBusquedaConsultas.add(getEjecutarConsultaJButton(), 
					new GridBagConstraints(3,0,1,1, 0.25, 0.25,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	}
    	
    	return jPanelBotoneraBusquedaConsultas;
    	
    }
    public JButton getBorrarJButton() {
    	borrarJButton = new JButton();
    	if(borrarJButton != null){
    		borrarJButton.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.borrar"));
    		borrarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            	{
            		eliminarConsultaActionListener();
            	}
    		});
    		
    	}
    	
		return borrarJButton;
	}
    
    
	public void setBorrarJButton(JButton borrarJButton) {
		this.borrarJButton = borrarJButton;
	}
	
	
	public JButton getNuevaJButton() {
		
		nuevaJButton = new JButton();
    	if(nuevaJButton != null){
    		nuevaJButton.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.nueva"));
    		nuevaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            	{
            		geoDialog.setVisible(false);
            		nuevaConsulta();
            	}
    		});
    		
    	}
    	
		return nuevaJButton;
	}
	public void setNuevaJButton(JButton nuevaJButton) {
		this.nuevaJButton = nuevaJButton;
	}
	public JButton getEjecutarConsultaJButton() {
		
		ejecutarConsultaJButton = new JButton();
    	if(ejecutarConsultaJButton != null){
    		ejecutarConsultaJButton.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.ejecutar"));
    		ejecutarConsultaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            	{
            		ejecutarConsultaActionListener();
            	}
    		});
    		
    	}
    	
		return ejecutarConsultaJButton;
	}
	public void setEjecutarConsultaJButton(JButton ejecutarConsultaJButton) {
		this.ejecutarConsultaJButton = ejecutarConsultaJButton;
	}
	public JButton getModificarJButton() {
		
		modificarJButton = new JButton();
    	if(modificarJButton != null){
    		modificarJButton.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.modificar"));
    		modificarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            	{
            		
            		modificarConsultaActionListener();
            	}
    		});
    		
    	}
		return modificarJButton;
	}
	public void setModificarJButton(JButton modificarJButton) {
		this.modificarJButton = modificarJButton;
	}
	
	
	private void ejecutarConsultaActionListener(){
		if(consultaSelected != null){
	
			geoDialog.setVisible(false);
				
			WizardDialog dialog = new WizardDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),
	        		I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel.wizardtitlegeoreference"), context.getErrorHandler());
	        // se le pasa un true en ejecutar ya que directamente se ejecutará la consulta y no se debera
			//guardar ningun dato de la consulta en la BDDD
			boolean ejecutar = true;
	        dialog.init(new WizardPanel[] {	new GeorreferenciacionExternaPanel04("1","2",context, consultaSelected),
	        								new GeorreferenciacionExternaPanel05("2",null,context, consultaSelected, ejecutar)
	                                  });
	        /*dialog.init(new WizardPanel[] {new GeorreferenciacionExternaPanel05("1",null,context, consultaSelected, ejecutar)
            });*/
	
	        dialog.setSize(650,550);
	        dialog.getContentPane().remove(dialog.getInstructionTextArea());
	        GUIUtil.centreOnWindow(dialog);
	        dialog.setVisible(true);

		}
		else{
			JOptionPane.showMessageDialog(this, 
					I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.noitemseleccionado"));
		}
	}
	
	
	private void eliminarConsultaActionListener(){
		
		if(consultaSelected != null){
			int n = JOptionPane.showOptionDialog(this, 
					I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.confirmBorrar")
					+ " " + consultaSelected.getNombreConsulta() , "",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					null, null);
			if (n == JOptionPane.NO_OPTION)
				return;
			else
				eliminarUsuario(consultaSelected);
		}
		else{
			JOptionPane.showMessageDialog(this, 
					I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.noitemseleccionado"));
		}
	
	}
	
	public void eliminarUsuario(ConsultaDatosBean consultaSelected) {
		
		try {
			boolean estadoEliminado = Test.eliminarConsulta(consultaSelected.getId());
			if(estadoEliminado){
				//se actualiza la tabla
				//se borra el objeto de la lista con la que estamos tratando
				
				if(listaConsultas.contains(consultaSelected)){
					listaConsultas.remove(consultaSelected);
					pintarConsultas(listaConsultas);
					if(listaConsultas.isEmpty()){
						deshabilitarBotones();
					}
					else{
						habilitarBotones();
					}
				}
			}
			
		} catch (SQLExceptionException0 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void modificarConsultaActionListener(){
		
		if(consultaSelected == null){
			JOptionPane.showMessageDialog(this, 
						I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.GeorreferenciacionExternaPanelBusquedaConsulta.noitemseleccionado"));
		}
		else{
			geoDialog.setVisible(false);
			iniciarWizardDialog();
		}
	}
	
	
	private void nuevaConsulta(){
		// la consultaSelected == null indica que es una nueva consulta
		consultaSelected = null;
		iniciarWizardDialog();
		
	}
	
	private void iniciarWizardDialog(){
		
		WizardDialog dialog = new WizardDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),
        		I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel.wizardtitlegeoreference"), context.getErrorHandler());
        
		 // se le pasa un false ya que es una modificacion o una nueva consulta, para que haga las operaciones
		//correspondientes para guardar o actualizar la consulta en la BBDD
		boolean ejecutar = false;
        dialog.init(new WizardPanel[] {new GeorreferenciacionExternaPanel01("1","2",context, consultaSelected),
                                  new GeorreferenciacionExternaPanel02("2","3",context, consultaSelected),
                                  new GeorreferenciacionExternaPanel03("3","4",context, consultaSelected),
                                  new GeorreferenciacionExternaPanel04("4","5",context, consultaSelected),
                                  new GeorreferenciacionExternaPanel05("5",null,context, consultaSelected, ejecutar)
                                  });

        dialog.setSize(650,550);
        dialog.getContentPane().remove(dialog.getInstructionTextArea());
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
	}
	
	public void deshabilitarBotones(){
		borrarJButton.setEnabled(false);
		ejecutarConsultaJButton.setEnabled(false);
	    modificarJButton.setEnabled(false);
		
	}
	

	public void habilitarBotones(){
		borrarJButton.setEnabled(true);
		ejecutarConsultaJButton.setEnabled(true);
		modificarJButton.setEnabled(true);
		
	}

}
