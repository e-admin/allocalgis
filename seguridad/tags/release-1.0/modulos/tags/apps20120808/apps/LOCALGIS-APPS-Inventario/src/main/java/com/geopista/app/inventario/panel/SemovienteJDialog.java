package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 25-sep-2006
 * Time: 17:42:45
 * To change this template use File | Settings | File Templates.
 */
public class SemovienteJDialog extends JDialog implements BienJDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(SemovienteJDialog.class);

     private DatosGeneralesSemovienteJPanel datosGenerales2JPanel;
     private DatosGeneralesComunesJPanel datosGenerales1JPanel;
     private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
     private ApplicationContext aplicacion;

     private SemovienteBean semoviente;

     private ArrayList actionListeners= new ArrayList();
     private javax.swing.JPanel datosGeneralesJPanel;
     private javax.swing.JTabbedPane datosSemovienteJTabbedPane;
     private DatosSegurosJPanel datosSegurosJPanel;
     private ObservacionesJPanel observacionesJPanel;
     private GestionDocumentalJPanel documentosJPanel;
     private String locale;
 	
     private String operacion;

     /**
      * Método que genera el dialogo que muestra los datos de un semoviente
      * @param desktop
      * @param locale
      */
     public SemovienteJDialog(JFrame desktop, String locale) throws Exception{
         super(desktop);
         this.aplicacion = (AppContext) AppContext.getApplicationContext();
         this.locale=locale;
         getContentPane().setLayout(new BorderLayout());
         renombrarComponentes();
         setModal(true);

         datosSemovienteJTabbedPane= new javax.swing.JTabbedPane();
         datosSemovienteJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         datosSemovienteJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

         datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
         datosGenerales2JPanel= new DatosGeneralesSemovienteJPanel(desktop, locale);

         /** Montamos el panel de datos generales */
         datosGeneralesJPanel= new JPanel();
         datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));
         datosGeneralesJPanel.setLayout(new BorderLayout());
         datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
         datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

         datosSemovienteJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);

         datosSegurosJPanel= new DatosSegurosJPanel();
         datosSemovienteJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

         observacionesJPanel= new ObservacionesJPanel();
         datosSemovienteJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

         documentosJPanel= new GestionDocumentalJPanel(false);
         datosSemovienteJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
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

         getContentPane().add(datosSemovienteJTabbedPane, BorderLayout.NORTH);
         getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
         setSize(470, 750);
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
  				int indice = datosSemovienteJTabbedPane.getSelectedIndex();
  				String uriRelativa = "";  				
  	 			switch (indice) {
  	 				case 0:
  	 					uriRelativa = "/Geocuenca:Inventario:Semovientes#Datos_Generales";	break;
  	 				case 1:
  	 					uriRelativa = "/Geocuenca:Inventario:Semovientes#Datos_Seguros";break;
  	 				case 2:
  	 					uriRelativa = "/Geocuenca:Inventario:Semovientes#Observaciones";break;
  	 				case 3:
  	 					uriRelativa = "/Geocuenca:Inventario:Semovientes#Documentos";break;
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
      * Método que carga un semoviente en la ventana de dialogo
      * @param semoviente a cargar
      * @param editable true si el dialogo se abre en modo edicion, false en caso contrario
      */
     public void load(SemovienteBean semoviente, boolean editable) throws Exception {
         setSemoviente(semoviente);
         if(operacion == null) return;
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
         }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
         }
         datosGenerales1JPanel.setEnabled(editable);
         datosGenerales2JPanel.setEnabled(editable);
         datosGenerales1JPanel.load(semoviente);
         datosGenerales2JPanel.load(semoviente);

         /** cargamos el seguro */
         datosSegurosJPanel.load(semoviente);
         datosSegurosJPanel.setEnabledDatos(false);
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) datosSegurosJPanel.setEnabledBotonera(false);
         else{
             datosSegurosJPanel.addJButtonSetEnabled(true);
             datosSegurosJPanel.buscarJButtonSetEnabled(true);
             datosSegurosJPanel.editarJButtonSetEnabled(semoviente!=null && semoviente.getSeguro()!=null);
             datosSegurosJPanel.borrarJButtonSetEnabled(semoviente!=null && semoviente.getSeguro()!=null);
         }

         /** cargamos las observaciones */
         observacionesJPanel.load(semoviente);
         observacionesJPanel.setEnabled(editable);
         observacionesJPanel.setOperacion(operacion);

         /** cargamos los documentos */
         documentosJPanel.load(semoviente);
         documentosJPanel.setEnabled(editable);
         
         /**añadimos los bienes revertibles*/
  		if (semoviente.getBienesRevertibles()!=null && semoviente.getBienesRevertibles().size()>0){
          	 JTabbedPane auxJPanel=new JTabbedPane();
          	datosSemovienteJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
          	 for (Iterator <BienRevertible>it=semoviente.getBienesRevertibles().iterator();it.hasNext();)
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
             semoviente= null;
         else{
             datosGenerales1JPanel.actualizarDatosGeneralesComunes((BienBean)semoviente);
             datosGenerales2JPanel.actualizarDatosGenerales(semoviente);

             datosSegurosJPanel.actualizarDatos((BienBean)semoviente);
             observacionesJPanel.actualizarDatos((BienBean)semoviente);
             documentosJPanel.actualizarDatos((BienBean)semoviente);
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

     public void setSemoviente(SemovienteBean semoviente){
         this.semoviente= semoviente;
     }

     public Object getSemoviente(){
         return semoviente;
     }


    private void exitForm(java.awt.event.WindowEvent evt) {
        setSemoviente(null);
        fireActionPerformed();
    }

    public GestionDocumentalJPanel getDocumentosJPanel() {
        return documentosJPanel;
    }
    public void pmsChecked(){
    	datosGenerales2JPanel.patrimonioChecked();
    }
	@Override
	public BienBean getBien() {
		return semoviente;
	}

}
