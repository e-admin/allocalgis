package com.geopista.app.administrador.usuarios;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.Operacion;
import com.geopista.protocol.administrador.DetallesOperacion;
import com.geopista.protocol.administrador.OperacionesAdministrador;

public class ShowDetalles extends javax.swing.JDialog  {

   private Logger logger = Logger.getLogger(ShowDetalles.class);
	
	private ResourceBundle messages;

	private DetallesTableModel modelDetallesPrevios;
	private DetallesTableModel modelDetallesModificados;
 
	private DetallesOperacion detalles;    
    
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JPanel jPanelDetallesPrevios;
    private javax.swing.JPanel jPanelDetallesVersion;
    
    private javax.swing.JScrollPane jScrollPaneDetallesPrevios;
    private javax.swing.JScrollPane jScrollPaneDetallesVersion;
    
    private javax.swing.JTable jTableDetallesPrevios;
    private javax.swing.JTable jTableDetallesVersion;
    
	 private TableSorted sorter;
	 
	 
	public ShowDetalles(Operacion operacion, ResourceBundle messages, javax.swing.JDialog parent) {
		super(parent, false); 
		
		this.messages=messages;
	    
        initDatos(operacion, parent.getLocale());
		initComponents();
	}
	
    private void initComponents() {
    	setTitle(messages.getString("ShowDetalles.title"));
        
    	jButtonSalir = new JButton();
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  salir();
              }
         });
        jButtonSalir.setText(messages.getString("ShowDetalles.jButtonSalir"));
        jButtonSalir.setToolTipText(messages.getString("ShowDetalles.jButtonSalir"));
    	       
        jPanelDetallesPrevios = new JPanel();
        jPanelDetallesVersion = new JPanel();
        jPanelBotonera = new JPanel();
        jPanelBotonera.add(jButtonSalir);  
        
        jScrollPaneDetallesPrevios = new javax.swing.JScrollPane();
        jTableDetallesPrevios = new javax.swing.JTable();
        jScrollPaneDetallesVersion = new javax.swing.JScrollPane();
        jTableDetallesVersion = new javax.swing.JTable();
        
        TitledBorder title1 = BorderFactory.createTitledBorder(messages.getString("ShowDetalles.ModifyDataTableTitle"));
        jScrollPaneDetallesVersion.setBorder(title1);
        
        TitledBorder title2 = BorderFactory.createTitledBorder(messages.getString("ShowDetalles.prevDataTableTitle"));
        jScrollPaneDetallesPrevios.setBorder(title2);
        
        modelDetallesModificados= new  DetallesTableModel(); 
    	modelDetallesModificados.setModelData(detalles.getDatosAfectadosVersionModificada(), detalles.getNombresColumnasDatosAfectados());
    	sorter = new TableSorted(modelDetallesModificados);
        sorter.setTableHeader(jTableDetallesVersion.getTableHeader());
        jTableDetallesVersion.setModel(sorter);
        jTableDetallesVersion.setAutoResizeMode(getWidth());
         
        jTableDetallesVersion.setPreferredScrollableViewportSize(jTableDetallesVersion.getPreferredSize());      
        
    	modelDetallesPrevios = new  DetallesTableModel(); 
    	modelDetallesPrevios.setModelData(detalles.getDatosAfectadosVersionPrevia(), detalles.getNombresColumnasDatosAfectados());
    	sorter = new TableSorted(modelDetallesPrevios);
        sorter.setTableHeader(jTableDetallesPrevios.getTableHeader());
        jTableDetallesPrevios.setModel(sorter);
        jTableDetallesPrevios.setAutoResizeMode(getWidth());
       
        jTableDetallesPrevios.setPreferredScrollableViewportSize(jTableDetallesVersion.getPreferredSize());
      
        
        jScrollPaneDetallesPrevios.setViewportView(jTableDetallesPrevios);
        jScrollPaneDetallesVersion.setViewportView(jTableDetallesVersion);
       
        jScrollPaneDetallesPrevios.getHorizontalScrollBar().setModel( jScrollPaneDetallesVersion.getHorizontalScrollBar().getModel() );   
        
        // hide one of the scroll bars   
        jScrollPaneDetallesPrevios.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER );   
        jScrollPaneDetallesVersion.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneDetallesVersion.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER );   

      
        jPanelDetallesPrevios.setLayout(new java.awt.BorderLayout());        
        jPanelDetallesVersion.setLayout(new java.awt.BorderLayout());
        
        jPanelDetallesPrevios.add(jScrollPaneDetallesPrevios,java.awt.BorderLayout.CENTER);
        jPanelDetallesVersion.add(jScrollPaneDetallesVersion,java.awt.BorderLayout.CENTER);  
              
        getContentPane().setLayout(new java.awt.GridLayout(3,1));
        getContentPane().add(jPanelDetallesPrevios);
        getContentPane().add(jPanelDetallesVersion);
        getContentPane().add(jPanelBotonera);
     	
        paintDiff();
        pack();
    }
    
    private void initDatos(Operacion op, Locale locale) {
        try {
        	OperacionesAdministrador adminOperations = new OperacionesAdministrador(Constantes.url);
        	
             detalles = adminOperations.getDetallesOperacion(op, locale);
             
             if (detalles==null)
                 logger.error("Ha ocurrido un error al buscar los detalles ");
            
        }
        catch(Exception e) {
		       logger.error("Error al obtener los detalles de la operacion: "+e.toString());
               JOptionPane optionPane= new JOptionPane(messages.getString("ShowDetalles.userFriendlyError"),JOptionPane.ERROR_MESSAGE); 
               JDialog dialog =optionPane.createDialog(this,e.getMessage());
               dialog.setVisible(true);
		 }
    }

    private void salir() {
        dispose();
    }

    private void paintDiff(){
    	 
    	 if(!detalles.getDatosAfectadosVersionModificada().isEmpty() &&
     			!detalles.getDatosAfectadosVersionPrevia().isEmpty()){
     		
     		for(int i=0; i < detalles.getNombresColumnasDatosAfectados().size(); i++ ){
 	    		String valorModificado = (String)detalles.getDatosAfectadosVersionModificada().get(i);
 	    		String valorPrevio = (String)detalles.getDatosAfectadosVersionPrevia().get(i);

 	    		if(!valorModificado.equalsIgnoreCase(valorPrevio))   			
 	    			jTableDetallesVersion.getColumn(detalles.getNombresColumnasDatosAfectados().get(i)).setCellRenderer( new DetalleModificadoRenderer());
 	    			 	    		
 	    	}    
     	}
    	
    }
}

class DetalleModificadoRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		c.setBackground(Color.yellow);
		return c;
	}
}


