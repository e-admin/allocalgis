/**
 * VehiculoJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 27-sep-2006
 * Time: 11:48:50
 * To change this template use File | Settings | File Templates.
 */
public class VehiculoJDialog extends JDialog implements BienJDialog{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(VehiculoJDialog.class);

     private DatosGeneralesVehiculoJPanel datosGenerales2JPanel;
     private DatosGeneralesComunesJPanel datosGenerales1JPanel;
     private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
     private ApplicationContext aplicacion;

     private VehiculoBean vehiculo;

     private ArrayList actionListeners= new ArrayList();
     private javax.swing.JPanel datosGeneralesJPanel;
     private javax.swing.JTabbedPane datosVehiculoJTabbedPane;
     private DatosSegurosJPanel datosSegurosJPanel;
     private ObservacionesJPanel observacionesJPanel;
     private GestionDocumentalJPanel documentosJPanel;
     private DatosAmortizacionJPanel datosAmortizacionJPanel;
 	
     private String operacion;
     private String locale;

     /**
      * Método que genera el dialogo que muestra los datos de un vehiculo
      * @param desktop
      * @param locale
      */
     public VehiculoJDialog(JFrame desktop, String locale) throws Exception{
         super(desktop);
         this.aplicacion = (AppContext) AppContext.getApplicationContext();
         this.locale =locale;
         getContentPane().setLayout(new BorderLayout());
         renombrarComponentes();
         setModal(true);

         datosVehiculoJTabbedPane= new javax.swing.JTabbedPane();
         datosVehiculoJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         datosVehiculoJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

         datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
         datosGenerales2JPanel= new DatosGeneralesVehiculoJPanel(locale);

         /** Montamos el panel de datos generales */
         datosGeneralesJPanel= new JPanel();
         datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));
         datosGeneralesJPanel.setLayout(new BorderLayout());
         datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
         datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

         datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);

         //Añadmos Listener para detectar los cambios en los valores
         datosGenerales2JPanel.getCosteAdquisicionJTField().addFocusListener(new ValorAdqFocusListener());
         datosGenerales2JPanel.getfAdquisicionJTField().addFocusListener(new FechaAdqFocusListener());
         
     
         datosAmortizacionJPanel= new DatosAmortizacionJPanel(desktop, locale);
         datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab5"), datosAmortizacionJPanel);

         datosSegurosJPanel= new DatosSegurosJPanel();
         datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

         observacionesJPanel= new ObservacionesJPanel();
         datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

         documentosJPanel= new GestionDocumentalJPanel(false);
         datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
         if ((operacion!= null) && (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))){
             documentosJPanel.modificarJButtonSetEnabled(false);
         }
         
         botoneraAceptarCancelarJPanel= new BotoneraAceptarCancelarJPanel();
         botoneraAceptarCancelarJPanel.addActionListener(new java.awt.event.ActionListener(){
             public void actionPerformed(ActionEvent e){
                 botoneraAceptarCancelarJPanel_actionPerformed();
             }
         });

         addWindowListener(new java.awt.event.WindowAdapter() {
             public void windowClosing(java.awt.event.WindowEvent evt) {
                 exitForm(evt);
             }
         });

         getContentPane().add(datosVehiculoJTabbedPane, BorderLayout.NORTH);
         getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
         //setSize(470, 710);
         setSize(570, 710);
         //setLocation(150, 90);
         GUIUtil.centreOnWindow(this);
         addAyudaOnline();
     }
   	/**
   	 * Ayuda Online
   	 * 
   	 */
   	private void addAyudaOnline() {
   		
   		getRootPane()
   				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
   				.put(KeyStroke.getKeyStroke("F1"), "action F1");

   		getRootPane().getActionMap().put("action F1", new AbstractAction() {
   			public void actionPerformed(ActionEvent ae) {
   				int indice = datosVehiculoJTabbedPane.getSelectedIndex();
   				String uriRelativa = "";  				
   	 			switch (indice) {
   	 				case 0:
   	 					uriRelativa = "/Geocuenca:Inventario:Vehículos#Datos_Generales";	break;
   	 				case 1:
   	 					uriRelativa = "/Geocuenca:Inventario:Vehículos#Datos_Amortizaci.C3.B3n";break;
   	 				case 2:
   	 					uriRelativa = "/Geocuenca:Inventario:Vehículos#Datos_Seguros";break;
   	 				case 3:
   	 					uriRelativa = "/Geocuenca:Inventario:Vehículos#Observaciones";break;
   	 				case 4:
   	 					uriRelativa = "/Geocuenca:Inventario:Vehículos#Documentos";break;
   	 				default:
   	 					break;
   	 				} 		          		          				
   				GeopistaBrowser.openURL(aplicacion
   						.getString("ayuda.geopista.web")
   						+ uriRelativa);
   			}
   		});

   	}

     /**
      * Método que actualiza la operacion que se esta realizando desde el panel padre
      * @param s operacion
      */
     public void setOperacion(String s){
         this.operacion= s;
     }

     /**
      * Método que carga un vehiculo en la ventana de dialogo
      * @param vehiculo a cargar
      * @param editable true si el dialogo se abre en modo edicion, false en caso contrario
      */
     public void load(VehiculoBean vehiculo, boolean editable) throws Exception {
         setVehiculo(vehiculo);
         if(operacion == null) return;
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
         }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
         }
         datosGenerales1JPanel.setEnabled(editable);
         datosGenerales2JPanel.setEnabled(editable);
         datosGenerales1JPanel.load(vehiculo);
         datosGenerales2JPanel.load(vehiculo);

         /** cargamos los datos de amortizacion */
         datosAmortizacionJPanel.setEnabled(editable);
         datosAmortizacionJPanel.load(vehiculo);

         /** cargamos el seguro */
         datosSegurosJPanel.load(vehiculo);
         datosSegurosJPanel.setEnabledDatos(false);
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) datosSegurosJPanel.setEnabledBotonera(false);
         else{
             datosSegurosJPanel.addJButtonSetEnabled(true);
             datosSegurosJPanel.buscarJButtonSetEnabled(true);
             datosSegurosJPanel.editarJButtonSetEnabled(vehiculo!=null && vehiculo.getSeguro()!=null);
             datosSegurosJPanel.borrarJButtonSetEnabled(vehiculo!=null && vehiculo.getSeguro()!=null);
         }

         /** cargamos las observaciones */
         observacionesJPanel.load(vehiculo);
         observacionesJPanel.setEnabled(editable);
         observacionesJPanel.setOperacion(operacion);
 		
         /** cargamos los documentos */
         documentosJPanel.load(vehiculo);
         documentosJPanel.setEnabled(editable);
         
         if (vehiculo.getBienesRevertibles()!=null && vehiculo.getBienesRevertibles().size()>0){
         	 JTabbedPane auxJPanel=new JTabbedPane();
        	 datosVehiculoJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
         	 for (Iterator it=vehiculo.getBienesRevertibles().iterator();it.hasNext();)
        	 {
        		 BienesRevertiblesPanel bienesJPanel= new BienesRevertiblesPanel((BienRevertible)it.next(),locale);
        		 bienesJPanel.setEnabled(false);
        		 auxJPanel.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienrevertible") , bienesJPanel);
        	 }
         }
      
     }

     public void renombrarComponentes(){
         try{datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));}catch(Exception e){}
     }

     public void botoneraAceptarCancelarJPanel_actionPerformed(){
         if(!botoneraAceptarCancelarJPanel.aceptarPressed() ||
             (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false))
             vehiculo= null;
         else{
             datosGenerales1JPanel.actualizarDatosGeneralesComunes((BienBean)vehiculo);
             datosGenerales2JPanel.actualizarDatosGenerales(vehiculo);

             datosAmortizacionJPanel.actualizarDatos((BienBean)vehiculo);
             datosSegurosJPanel.actualizarDatos((BienBean)vehiculo);
             observacionesJPanel.actualizarDatos((BienBean)vehiculo);
             documentosJPanel.actualizarDatos((BienBean)vehiculo);
         }
         fireActionPerformed();
     }

     /**
      * Método que abre una ventana de confirmacion sobre la operacion que se esta llevando a cabo
      */
     private boolean confirmOption(){
         int ok= -1;
         ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.optionpane.tag1"), aplicacion.getI18nString("inventario.optionpane.tag2"), JOptionPane.YES_NO_OPTION);
         if (ok == JOptionPane.NO_OPTION){
             return false;
         }
         return true;
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

     public void setVehiculo(VehiculoBean vehiculo){
         this.vehiculo= vehiculo;
     }

     public Object getVehiculo(){
         return vehiculo;
     }

     public BienBean getBien(){
    	 return vehiculo;
     }

    private void exitForm(java.awt.event.WindowEvent evt) {
        setVehiculo(null);
        fireActionPerformed();
    }

    public GestionDocumentalJPanel getDocumentosJPanel() {
        return documentosJPanel;
    }
    public void pmsChecked(){
    	datosGenerales2JPanel.patrimonioChecked();
    }


    class ValorAdqFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			Number valor = 0.0;
			try {
				// Cambiamos el valor de adquisicion
				if (datosGenerales2JPanel.getCosteAdquisicionJTField()
						.getText() != null
						&& !datosGenerales2JPanel
								.getCosteAdquisicionJTField().getText()
								.equalsIgnoreCase("")){
					valor = datosGenerales2JPanel
							.getCosteAdquisicionJTField().getNumber();
				}
		         
				datosAmortizacionJPanel.setCosteAdquisicion((Double) valor);
				if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
					datosAmortizacionJPanel.calcularTotalAmortizado(null,vehiculo.getFechaAdquisicion(), vehiculo.getCosteAdquisicion(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
						} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}
	class FechaAdqFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			String valor = null;
			try {
				
				//Cambiamos la fecha de adquisicion
				if (datosGenerales2JPanel.getfAdquisicionJTField()
						.getText() != null
						&& !datosGenerales2JPanel.getfAdquisicionJTField().getText()
								.equalsIgnoreCase("")){
					valor =  datosGenerales2JPanel.getfAdquisicionJTField().getText().trim();
					datosAmortizacionJPanel.setFechaAdquisicion(Constantes.df.parse(valor));
					if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
						datosAmortizacionJPanel.calcularTotalAmortizado(null,vehiculo.getFechaAdquisicion(), vehiculo.getCosteAdquisicion(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
				}

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}
    
}
