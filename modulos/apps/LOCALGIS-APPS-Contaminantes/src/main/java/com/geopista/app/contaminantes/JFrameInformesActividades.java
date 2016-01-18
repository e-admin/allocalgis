/**
 * JFrameInformesActividades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JFrameInformesActividades.java
 *
 * Created on 22 de octubre de 2004, 11:29
 */

package com.geopista.app.contaminantes;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.reports.GenerarInformeExterno;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.CConstantes;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;

/**
 *
 * @author  charo
 */
public class JFrameInformesActividades extends javax.swing.JInternalFrame {
	Logger logger = Logger.getLogger(JFrameInformesActividades.class);

	private JFrame desktop;
	private ResourceBundle messages;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private ActividadesTableModel _listaActividadesTableModel;
	Vector _vActividades=null;
	private TableSorted sorter;
	
	/*
	 * Almacenamos el listado con el num_expediente que utilizaremos en el ireport:
	 * */
	private String lista_expedientes = "";


	/** Creates new form JFrameInformesActividades */
	public JFrameInformesActividades() {
		initComponents();
	}


	public JFrameInformesActividades(ResourceBundle messages, JFrame frame, Vector actividades) {
		this.desktop=frame;
		this.messages = messages;
		_vActividades= actividades;

		initComponents();
		initComboBoxesEstructuras();
		String[] columnNames = {messages.getString("jMenuItemActividadesInformes.jTableListado.column1"),
				messages.getString("jMenuItemActividadesInformes.jTableListado.column2"),
				messages.getString("jMenuItemActividadesInformes.jTableListado.column3"),
				messages.getString("jMenuItemActividadesInformes.jTableListado.column4")};

		ActividadesTableModel.setColumnNames(columnNames);
		_listaActividadesTableModel= new ActividadesTableModel();
		actividadesJTable.setModel(_listaActividadesTableModel);

		UtilsContaminantes.cargarPlantillas(CConstantes.PLANTILLAS_PATH_ACTIVIDADES, plantillaJCBox);
		cargarActividades();
		changeScreenLang(messages);

	}

	public void initComboBoxesEstructuras(){
		while (!Estructuras.isCargada())
		{
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {Thread.sleep(500);}catch(Exception e){}
		}

		//        formatoSalidaEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormatosSalida(), null, com.geopista.app.contaminantes.init.Constantes.Locale, false);
		//jPanelDatos.add(formatoSalidaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 70, 290, -1));
		//        jPanelDatos.add(formatoSalidaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 50, 290, -1));
	}

	private void cargarActividades(){

		try{
			/** solicitamos todas las actividades para el municipio (este se coge de sesion) */
			if (_vActividades == null){
				_vActividades= ((CResultadoOperacion)(new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).getSearchedActividadesContaminantes(new Hashtable())).getVector();
			}
			if (_vActividades == null){
				logger.warn("No existen actividades contaminantes en el sistema");
				UtilsContaminantes.mostrarMensaje(jPanelDatos, messages.getString("jMenuItemActividadesInformes.mensaje1"), messages.getString("jMenuItemActividadesInformes.mensajeTittle1"));
				jButtonGenerarInformes.setEnabled(false);
			}else{
				lista_expedientes = "";
				for (int i = 0; i < _vActividades.size(); i++) {
					Contaminante contaminante = (Contaminante) _vActividades.elementAt(i);
					lista_expedientes += "'"+contaminante.getId()+"'";
					if (i < _vActividades.size()-1){
						lista_expedientes += ", ";
					}
				}         	  
				actualizarModelo();
			}

		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}





	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		jPanelPrincipal = new javax.swing.JPanel();
		jPanelTotal = new javax.swing.JPanel();
		//jPanelBotonera = new javax.swing.JPanel();
		jButtonGenerarInformes= new javax.swing.JButton();
		salirJButton= new javax.swing.JButton();
		datosJTabbedPane = new javax.swing.JTabbedPane();
		jPanelDatos = new javax.swing.JPanel();
		jPanelListado = new javax.swing.JPanel();
		jScrollPaneListado = new javax.swing.JScrollPane();
		actividadesJTable= new javax.swing.JTable();
		/*
        nombreJLabel= new javax.swing.JLabel();
        nombreInformeJTField= new javax.swing.JTextField();
		 */
		plantillaJCBox = new javax.swing.JComboBox();
		plantillaJLabel= new javax.swing.JLabel();
		//        formatoJLabel= new javax.swing.JLabel();
		//formatoSalidaEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormatosSalida(), null, com.geopista.app.contaminantes.init.Constantes.Locale, false);

		jPanelPrincipal.setLayout(new java.awt.BorderLayout());
		/*
        jPanelBotonera.add(jButtonGenerarInformes);
        jButtonGenerarInformes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarInforme();
            }
        });
		 */
		salirJButton.setText(messages.getString("jMenuItemActividadesInformes.jButtonSalir"));
		salirJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salir();
			}
		});
		jPanelTotal.add(salirJButton);
		//jPanelPrincipal.add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
		datosJTabbedPane.setBorder(new javax.swing.border.EtchedBorder());
		jPanelDatos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		/*
        nombreJLabel.setText(messages.getString("jMenuItemActividadesInformes.nombreInformeJLabel"));
        jPanelDatos.add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));
        //2303jPanelDatos.add(nombreInformeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 290, -1));
        jPanelDatos.add(nombreInformeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 30, 290, -1));
		 */

		jButtonGenerarInformes.setText(messages.getString("jMenuItemActividadesInformes.jButtonGenerarInformes"));
		//2303jPanelDatos.add(jButtonGenerarInformes, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));
		jPanelDatos.add(jButtonGenerarInformes, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, -1, -1));
		jButtonGenerarInformes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generarInforme();
			}
		});

		plantillaJLabel.setText(messages.getString("jMenuItemActividadesInformes.plantillaJLabel"));
		//jPanelDatos.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
		jPanelDatos.add(plantillaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));
		//jPanelDatos.add(plantillaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 50, 290, -1));
		jPanelDatos.add(plantillaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 30, 290, -1));

		//        formatoJLabel.setText(messages.getString("jMenuItemActividadesInformes.formatoJLabel"));
		//jPanelDatos.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
		//        jPanelDatos.add(formatoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

		jPanelListado.setLayout(new java.awt.BorderLayout());
		jScrollPaneListado.setViewportView(actividadesJTable);
		jPanelListado.add(jScrollPaneListado, java.awt.BorderLayout.CENTER);
		JPanel auxPanel= new JPanel();
		auxPanel.setLayout(new java.awt.BorderLayout());
		auxPanel.add(jPanelDatos,java.awt.BorderLayout.SOUTH);
		auxPanel.add(jPanelListado,java.awt.BorderLayout.CENTER);
		datosJTabbedPane.addTab(messages.getString("jMenuItemActividadesInformes.jTabbedPaneDatos"), auxPanel);
		jPanelPrincipal.add(datosJTabbedPane, java.awt.BorderLayout.CENTER);
		jPanelPrincipal.setMinimumSize(new Dimension(600,600));

		//getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.WEST);
		getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);
		getContentPane().add(jPanelTotal, java.awt.BorderLayout.SOUTH);

		pack();
	}

	private void salir(){
		/** volvemos a registrar el driver GEOPISTADriver */
		String classForName= "com.geopista.sql.GEOPISTADriver";
		try{
			Driver dPista= (Driver)Class.forName(classForName).newInstance();
			DriverManager.registerDriver(dPista);
		}catch(Exception e){
			logger.debug(".................Class.forNmae " + classForName + " " + e.getMessage());
		}

		this.dispose();
	}

	private void salirJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirJButtonActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_salirJButtonActionPerformed

	private void generarInformeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarInformeJButtonActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_generarInformeJButtonActionPerformed

	
	private void generarInforme() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			if ((_vActividades != null) && (_vActividades.size() > 0) )
			{
				String report = ((String)plantillaJCBox.getSelectedItem());
				if (report != null && !report.equals("")){    
					Map parametros = obtenerParametros();
					String reportFile = UtilsContaminantes.getLocalPathPlantillas() + report;
					GenerarInformeExterno giep=new GenerarInformeExterno(reportFile, parametros);
				}
			}
			else
				UtilsContaminantes.mostrarMensaje(jPanelDatos, messages.getString("jMenuItemActividadesInformes.mensaje4"), messages.getString("jMenuItemActividadesInformes.mensajeTittle1"));
		}catch(Exception e){
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			UtilsContaminantes.mostrarMensaje(jPanelDatos, messages.getString("jMenuItemActividadesInformes.mensaje2"), messages.getString("jMenuItemActividadesInformes.mensajeTittle1"));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void actualizarModelo(){
		_listaActividadesTableModel.setModelData(_vActividades);
		sorter = new TableSorted(_listaActividadesTableModel);
		sorter.setTableHeader(actividadesJTable.getTableHeader());
		actividadesJTable.setModel(sorter);
		actividadesJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void changeScreenLang(ResourceBundle messages){
		try
		{
			this.messages=messages;
			jButtonGenerarInformes.setText(messages.getString("jMenuItemActividadesInformes.jButtonGenerarInformes"));
			salirJButton.setText(messages.getString("jMenuItemActividadesInformes.jButtonSalir"));
			plantillaJLabel.setText(messages.getString("jMenuItemActividadesInformes.plantillaJLabel"));
			//            formatoJLabel.setText(messages.getString("jMenuItemActividadesInformes.formatoJLabel"));

			jButtonGenerarInformes.setToolTipText(messages.getString("jMenuItemActividadesInformes.jButtonGenerarInformes"));
			salirJButton.setToolTipText(messages.getString("jMenuItemActividadesInformes.jButtonSalir"));

			/** Headers de la tabla eventos */
			TableColumn tableColumn= actividadesJTable.getColumnModel().getColumn(0);
			tableColumn.setHeaderValue(messages.getString("jMenuItemActividadesInformes.jTableListado.column1"));
			tableColumn= actividadesJTable.getColumnModel().getColumn(1);
			tableColumn.setHeaderValue(messages.getString("jMenuItemActividadesInformes.jTableListado.column2"));
			tableColumn= actividadesJTable.getColumnModel().getColumn(2);
			tableColumn.setHeaderValue(messages.getString("jMenuItemActividadesInformes.jTableListado.column3"));
			tableColumn= actividadesJTable.getColumnModel().getColumn(3);
			tableColumn.setHeaderValue(messages.getString("jMenuItemActividadesInformes.jTableListado.column4"));

			datosJTabbedPane.setTitleAt(0, messages.getString("jMenuItemActividadesInformes.jTabbedPaneDatos"));
		}catch(Exception e)
		{
			logger.error("Error al cargar las etiquetas: ",e);
		}
	}



	//    	private ComboBoxEstructuras formatoSalidaEJCBox;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonGenerarInformes;
	private javax.swing.JButton salirJButton;
	private javax.swing.JLabel plantillaJLabel;
	private javax.swing.JComboBox plantillaJCBox;
	//        private javax.swing.JLabel formatoJLabel;
	/*
        private javax.swing.JLabel nombreJLabel;
        private javax.swing.JTextField nombreInformeJTField;
	 */

	//private javax.swing.JPanel jPanelBotonera;
	private javax.swing.JPanel jPanelDatos;
	private javax.swing.JPanel jPanelListado;
	private javax.swing.JPanel jPanelPrincipal;
	private javax.swing.JPanel jPanelTotal;
	private javax.swing.JScrollPane jScrollPaneListado;
	private javax.swing.JTabbedPane datosJTabbedPane;
	private javax.swing.JTable actividadesJTable;
	// End of variables declaration//GEN-END:variables

	/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane actividadesJScrollPane;
    private javax.swing.JTable actividadesJTable;
    private javax.swing.JPanel datosFicheroJPanel;
    private javax.swing.JTabbedPane datosJTabbedPane;
    private javax.swing.JLabel formatoJLabel;
    private javax.swing.JButton generarInformeJButton;
    private javax.swing.JPanel listaActividadesJPanel;
    private javax.swing.JTextField nombreFicheroJTField;
    private javax.swing.JLabel nombreJLabel;
    private javax.swing.JComboBox plantillaJCBox;
    private javax.swing.JLabel plantillaJLabel;
    private javax.swing.JButton salirJButton;
    private javax.swing.JPanel templateJPanel;
    // End of variables declaration//GEN-END:variables
	 */

	private Map obtenerParametros(){

		Map parametros = new HashMap();
		parametros.put("lista_expedientes", lista_expedientes);
		parametros.put("locale", aplicacion.getI18NResource().getLocale().toString());
		parametros.put("id_municipio", aplicacion.getIdMunicipio());
		return parametros;
	}

}
