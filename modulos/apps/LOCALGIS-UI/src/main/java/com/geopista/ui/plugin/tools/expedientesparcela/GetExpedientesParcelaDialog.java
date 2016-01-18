/**
 * GetExpedientesParcelaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.geopista.app.AppContext;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.Estado;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.TipoExpediente;
import com.vividsolutions.jump.I18N;

/**
 * Clase que muestra la ventana al pulsar en el plugin de Obtener Expedientes de una parcela.
 * 
 * @author fjcastro
 *
 */
public class GetExpedientesParcelaDialog extends JDialog{

	JLabel tiposExpedienteJLabel;
	JLabel estadosInformeJLabel;
	JComboBox tiposExpedienteJComboBox;
	JComboBox estadosInformeJComboBox;
	JButton buscarJButton;
	JScrollPane scrollPane;
	JTable tabla;
	DefaultTableModel modelo;
	
	JLabel datosCatastroJLabel;
	JLabel datosDireccionJLabel = null;
	
	TipoExpediente[] tiposExpediente;
	Estado[] estadosInforme;
	CReferenciaCatastral refCatastral;
	
	AppContext aplicacion;
	
	public GetExpedientesParcelaDialog(AppContext aplicacion, TipoExpediente[] tiposExpediente, Estado[] estadosInforme, CReferenciaCatastral referenciaCatastral) {

		super(aplicacion.getMainFrame());
		
		this.tiposExpediente = tiposExpediente;
		this.estadosInforme = estadosInforme;
		this.aplicacion = aplicacion;
		this.refCatastral = referenciaCatastral;
		
	    Locale loc=Locale.getDefault();

	    ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.tools.expedientesparcela.languages.GetExpedientesParcelaPlugIni18n",loc,this.getClass().getClassLoader());    	
	    I18N.plugInsResourceBundle.put("GetExpedientesParcelaPlugIn",bundle2);
	        
	    initComponents();
	    initActions();

	}
	 
	private void initComponents() {
		 
		tiposExpedienteJLabel = new JLabel();
		estadosInformeJLabel = new JLabel();
		tiposExpedienteJComboBox = new JComboBox();
		estadosInformeJComboBox = new JComboBox();
		buscarJButton = new JButton();
		getContentPane().setLayout(null);
		 
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		 
		// Creamos la etiqueta de los tipos de expedientes:
		tiposExpedienteJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.tiposInformeLabel"));
		getContentPane().add(tiposExpedienteJLabel);
		tiposExpedienteJLabel.setBounds(55, 40, 100, 20);
		 
		// Creamos la etiqueta del estado de un expediente:
		estadosInformeJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.estadosInformeLabel"));
		getContentPane().add(estadosInformeJLabel);
		estadosInformeJLabel.setBounds(425, 40, 55, 20);
		 
		// Sacamos el tipo de los expedientes:
		int longitud = tiposExpediente.length;
		String[] tiposExpedienteString = new String[longitud];
		for (int i = 0; i < longitud; i++){
			tiposExpedienteString[i] = tiposExpediente[i].getDescripcion();
		}
		 
		// Creamos el objeto combo de los tipos de expedientes y lo rellenamos con los tipos de expedientes
		tiposExpedienteJComboBox.setModel(new DefaultComboBoxModel(tiposExpedienteString));
		getContentPane().add(tiposExpedienteJComboBox);
		tiposExpedienteJComboBox.setBounds(155, 40, 230, 22);
		 
		// Sacamos el nombre de los distintos estados de un expediente:	 
		longitud = estadosInforme.length;
		String[] estadosInformeString = new String[longitud];
		for (int j = 0; j < longitud; j++){
			estadosInformeString[j] = estadosInforme[j].getNombreEstado();
		}
		 
		// Creamos el objeto combo de los estados de un expediente y lo rellenamos con los estados de un expediente:
		estadosInformeJComboBox.setModel(new DefaultComboBoxModel(estadosInformeString));
		getContentPane().add(estadosInformeJComboBox);
		estadosInformeJComboBox.setBounds(475, 40, 250, 22);
	
		// Creamos el boton buscar:
		buscarJButton.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.buscarButton"));
		getContentPane().add(buscarJButton);
		buscarJButton.setBounds(755, 40, 80, 23);
/*	         
		// Creamos la etiqueta con la referencia catastral:
		datosCatastroJLabel = new JLabel();
 	    datosCatastroJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.catastroLabel")+" "+refCatastral.getReferenciaCatastral());
		getContentPane().add(datosCatastroJLabel);
		datosCatastroJLabel.setBounds(100, 80, 300, 20);
	
		// Creamos la etiqueta con los datos de la dirección:
		datosDireccionJLabel = new JLabel(); 
		String via = refCatastral.getNombreVia()+" "+refCatastral.getPrimerNumero();
		datosDireccionJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.direccionLabel")+" "+via);
		 	
 	 	getContentPane().add(datosDireccionJLabel);
	 	datosDireccionJLabel.setBounds(400, 80, 500, 20);
*/		 
		// Creamos la tabla, con una barra de scroll, y la llenamos con los datos (modelo):
        modelo = new GetExpParcelaTableModel();      	
        tabla = new JTable(modelo);
        scrollPane = new JScrollPane(tabla);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);		 
		 
        getContentPane().add(scrollPane);
		scrollPane.setVisible(true);
		scrollPane.setBounds(25, 130, 850, 410);
			
    	// Metemos los títulos de las columnas de la tabla en modelo:
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.numExpediente"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.DNI_CIF"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.nombreApellidos"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.fechaSolicitud"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.fechaFinObra"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.tipoObra"));
    	modelo.addColumn(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.resolucion"));

		TableColumn column = null;
		for (int i = 0; i < 7; i++) {
		    column = tabla.getColumnModel().getColumn(i);
		    switch (i){
		    	case 1:
		    		column.setPreferredWidth(70);
		     		break;
		     	case 2:
		     		column.setPreferredWidth(210);
		     		break;
		     	case 3:
		     		column.setPreferredWidth(80);
		     		break;
		     	case 5:
		     		column.setPreferredWidth(150);
		     		break;
		     	case 6:
		     		column.setPreferredWidth(90);
		     		break;			     		
		     	default:
		     		column.setPreferredWidth(100);
		     		break;     		
		    }
		}
    	 
		pack();
		setTitle(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.titulo"));
		setSize(900,600);
		setResizable(false);
		centreOnScreen(this);
		setVisible(true);
		 
	}
	
	/** 
	 * Metodo para centrar un componente en la pantalla. 
	 */
	private  void centreOnScreen(Component componentToMove) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		componentToMove.setLocation((screenSize.width -
		componentToMove.getWidth())/2, (screenSize.height - componentToMove.getHeight()) / 2);
	}

	/** 
	 * Metodo para inicializar las acciones. 
	 */
	private void initActions() {
	    	
		buscarJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					
					int idEstado = estadosInforme[estadosInformeJComboBox.getSelectedIndex()].getIdEstado().intValue();
					int idTipoExp = tiposExpediente[tiposExpedienteJComboBox.getSelectedIndex()].getIdTipoExpediente().intValue();
			         
					buscarExpedientes(idEstado, idTipoExp);
			}
			
		});
		
	}
	
	/** 
	 * Metodo para vaciar una tabla si se ha insertado previamente alguna fila. 
	 */
	private void vaciaTabla(){
        int numFilas = modelo.getRowCount();
        if (numFilas > 0){
       	 for (int i = 0; i < numFilas; i++)
       		 modelo.removeRow(0);
        }
	}
	
	/**
	 * Metodo para buscar expedientes, se le tiene que indicar el estado y el tipo de expediente.
	 * @param idEstado
	 * @param idTipoExp
	 */
	private void buscarExpedientes(int idEstado, int idTipoExp) {
		try {
			 Connection conexion = aplicacion.getConnection();
			 PreparedStatement ps = null;
	         ResultSet resultSet = null;	         		
	    	 if (((idTipoExp == 0) || (idTipoExp == 1))&&(idEstado != -1)){
	    		 ps = conexion.prepareStatement("GEPfindObraMayorOMenorConEstado");
	    		 ps.setInt(1, idEstado);
	    	     ps.setString(2, refCatastral.getReferenciaCatastral());
	    		 ps.setInt(3, idTipoExp);
	    	 }
	    	 else{
		    	 if (((idTipoExp == 2) || (idTipoExp == 3))&&(idEstado != -1)){
		    		 ps = conexion.prepareStatement("GEPfindCalificadaONoCalificadaConEstado");
		    		 ps.setInt(1, idEstado);
		    	     ps.setString(2, refCatastral.getReferenciaCatastral());
		    		 ps.setInt(3, idTipoExp);
		    	 }
		    	 else{	    		 
		    		 if (((idTipoExp == 0) || (idTipoExp == 1))&&(idEstado == -1)){
		    			 ps = conexion.prepareStatement("GEPfindObraMayorOMenorSinEstado");
		    			 ps.setString(1, refCatastral.getReferenciaCatastral());
		    			 ps.setInt(2, idTipoExp);
		    		 }
		    		 else{
			    		 if (((idTipoExp == 2) || (idTipoExp == 3))&&(idEstado == -1)){
			    			 ps = conexion.prepareStatement("GEPfindCalificadaONoCalificadaSinEstado");
			    			 ps.setString(1, refCatastral.getReferenciaCatastral());
			    			 ps.setInt(2, idTipoExp);
			    		 }
			    		 else{
    						 if ((idTipoExp == -1)&&(idEstado != -1)){
    							 ps = conexion.prepareStatement("GEPfindSinTipoExpConEstado");
    							 ps.setInt(1, idEstado);
    							 ps.setString(2, refCatastral.getReferenciaCatastral());
    							 ps.setInt(3, idEstado);
    							 ps.setString(4, refCatastral.getReferenciaCatastral());
    						 }
    						 else{
    							 if ((idTipoExp == -1)&&(idEstado == -1)){
    								 ps = conexion.prepareStatement("GEPfindSinTipoExpSinEstado");
    								 ps.setString(1, refCatastral.getReferenciaCatastral());
    								 ps.setString(2, refCatastral.getReferenciaCatastral());
    							 }
    						 }
			    		 }
		    		 }
		    	 }
	    	 }
             resultSet = ps.executeQuery();
             
             // Vacio la tabla 
             vaciaTabla();
             
             while (resultSet.next()){       	 
           	    // Se crea un array que será una de las filas de la tabla.
           	    Object [] fila = new Object[GetExpedientesParcelaConstantes.TAMANIO_TABLA]; // Hay tres columnas en la tabla

           	    // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.

           	    	fila[0] = resultSet.getObject("num_expediente");
           	    	fila[1] = resultSet.getObject("dni_cif");
           	    	fila[2] = resultSet.getObject("nombre")+" "+resultSet.getObject("apellido1")+" "+resultSet.getObject("apellido2");
           	    	if ((resultSet.getObject("fecha")!=null)&&(!resultSet.getObject("fecha").equals(""))){
               	    	SimpleDateFormat fechaSolicitud = new SimpleDateFormat("dd/MM/yyyy");
           	    		fila[3] = fechaSolicitud.format(resultSet.getObject("fecha"));
           	    	}
           	    	else
           	    		fila[3] = "";
           	    	
           	    	if ((resultSet.getObject("fecha_limite_obra")!=null)&&(!resultSet.getObject("fecha_limite_obra").equals(""))){
           	    		SimpleDateFormat fechaFinObra = new SimpleDateFormat("dd/MM/yyyy");
           	    		fila[4] = fechaFinObra.format(resultSet.getObject("fecha_limite_obra"));
           	    	}
           	    	else
           	    		fila[4] = "";
           	    	
           	    	Integer id_tipo_licencia = resultSet.getInt("id_tipo_licencia");
           	    	if (id_tipo_licencia==GetExpedientesParcelaConstantes.IDOBRAMAYOR)
           	    		fila[5] = I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.obraMayor")+" / "+resultSet.getObject("descripcion");
           	    	else
               	    	if (id_tipo_licencia==GetExpedientesParcelaConstantes.IDOBRAMENOR)
               	    		fila[5] = I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.obraMenor")+" / "+resultSet.getObject("descripcion");
               	    	else
                   	    	if (id_tipo_licencia==GetExpedientesParcelaConstantes.IDACTIVIDADCALIFICADA)
                   	    		fila[5] = I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.actividadCalificada")+" / "+resultSet.getObject("descripcion");
                   	    	else
                       	    	if (id_tipo_licencia==GetExpedientesParcelaConstantes.IDACTIVIDADNOCALIFICADA)
                       	    		fila[5] = I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.actividadNoCalificada")+" / "+resultSet.getObject("descripcion");
           	    	
           	    	if ((resultSet.getString("a_favor")!=null)&&(!resultSet.getString("a_favor").equals("0"))&&(!resultSet.getString("a_favor").equals("")))
           	    		fila[6] = (Object) I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.favorable");
           	    	else
           	    		fila[6] = (Object) I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.desfavorable");
            	    // Se añade al modelo la fila completa.
            	    modelo.addRow(fila);

            	    if (datosDireccionJLabel==null){
	            	    
	            	    String nombreVia = "";
	            	    if (resultSet.getString("nombre_via") != null)
	            	    	nombreVia = " "+resultSet.getString("nombre_via");
	            	    	
	            	    String numeroVia = "";
	            	    if (resultSet.getString("numero") != null)
	            	    	numeroVia = ", "+resultSet.getString("numero");
	            	    
	            	    String via = nombreVia+numeroVia;
	                    datosCatastroJLabel = new JLabel();
	            	    datosCatastroJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.catastroLabel")+" "+refCatastral.getReferenciaCatastral());
	       			 	getContentPane().add(datosCatastroJLabel);
	       			 	datosCatastroJLabel.setBounds(100, 80, 300, 20);
	       			 	
	       			 	datosDireccionJLabel = new JLabel();
	       			 	datosDireccionJLabel.setText(I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela.direccionLabel")+" "+via);
	       			 	

	    			 	getContentPane().add(datosDireccionJLabel);
	    			 	datosDireccionJLabel.setBounds(400, 80, 500, 20);
            	    }
            	    
             }
				
             ps.close();
			 resultSet.close();             
             
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	 
}
