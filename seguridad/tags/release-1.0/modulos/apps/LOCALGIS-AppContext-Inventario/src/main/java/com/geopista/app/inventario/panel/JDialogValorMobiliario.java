package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.BienBean;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.ArrayList;


public class JDialogValorMobiliario extends JDialog implements BienJDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(JDialogValorMobiliario.class);

     private DatosGeneralesValorMobiliarioJPanel datosGenerales2JPanel;
     private DatosGeneralesComunesJPanel datosGenerales1JPanel;
     private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
     private ApplicationContext aplicacion;

     private ValorMobiliarioBean valor;

     private ArrayList actionListeners= new ArrayList();
     private javax.swing.JPanel datosGeneralesJPanel;
     private javax.swing.JTabbedPane datosValorMobiliarioJTabbedPane;
     private DatosSegurosJPanel datosSegurosJPanel;
     private ObservacionesJPanel observacionesJPanel;
     private GestionDocumentalJPanel documentosJPanel;
 	
     private String operacion;
     private String locale;
      

     /**
      * Método que genera el dialogo que muestra los datos de un valor mobiliario
      * @param desktop
      * @param locale
      */
     public JDialogValorMobiliario(JFrame desktop, String locale) throws Exception{
         super(desktop);
         this.aplicacion = (AppContext) AppContext.getApplicationContext();
         this.locale=locale;
         getContentPane().setLayout(new BorderLayout());
         renombrarComponentes();
         setModal(true);

         datosValorMobiliarioJTabbedPane= new javax.swing.JTabbedPane();
         datosValorMobiliarioJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         datosValorMobiliarioJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

         datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
         datosGenerales2JPanel= new DatosGeneralesValorMobiliarioJPanel(desktop, locale);

         /** Montamos el panel de datos generales */
         datosGeneralesJPanel= new JPanel();
         datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));
         datosGeneralesJPanel.setLayout(new BorderLayout());
         datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
         datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

         datosValorMobiliarioJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);
  		
         datosSegurosJPanel= new DatosSegurosJPanel();
         datosValorMobiliarioJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

         observacionesJPanel= new ObservacionesJPanel();
         datosValorMobiliarioJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

         documentosJPanel= new GestionDocumentalJPanel(false);
         datosValorMobiliarioJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
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

         getContentPane().add(datosValorMobiliarioJTabbedPane, BorderLayout.NORTH);
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
   				int indice = datosValorMobiliarioJTabbedPane.getSelectedIndex();
   				String uriRelativa = "";  				
   	 			switch (indice) {
   	 				case 0:
   	 					uriRelativa = "/Geocuenca:Inventario:Valores_Mobiliarios#Datos_Generales";	break;
   	 				case 1:
   	 					uriRelativa = "/Geocuenca:Inventario:Valores_Mobiliarios#Datos_Seguros";break;
   	 				case 2:
   	 					uriRelativa = "/Geocuenca:Inventario:Valores_Mobiliarios#Observaciones";break;
   	 				case 3:
   	 					uriRelativa = "/Geocuenca:Inventario:Valores_Mobiliarios#Documentos";break;
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
      * Método que carga un valor mobiliario en la ventana de dialogo
      * @param valor a cargar
      * @param editable true si el dialogo se abre en modo edicion, false en caso contrario
      */
     public void load(ValorMobiliarioBean valor, boolean editable) throws Exception {
         setValor(valor);
         if(operacion == null) return;
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
         }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
         }
         datosGenerales1JPanel.setEnabled(editable);
         datosGenerales2JPanel.setEnabled(editable);
         datosGenerales1JPanel.load(valor);
         datosGenerales2JPanel.load(valor);

         /** cargamos el seguro */
         datosSegurosJPanel.load(valor);
         datosSegurosJPanel.setEnabledDatos(false);
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) datosSegurosJPanel.setEnabledBotonera(false);
         else{
             datosSegurosJPanel.addJButtonSetEnabled(true);
             datosSegurosJPanel.buscarJButtonSetEnabled(true);
             datosSegurosJPanel.editarJButtonSetEnabled(valor!=null && valor.getSeguro()!=null);
             datosSegurosJPanel.borrarJButtonSetEnabled(valor!=null && valor.getSeguro()!=null);
         }

         /** cargamos las observaciones */
         observacionesJPanel.load(valor);
         observacionesJPanel.setEnabled(editable);
         observacionesJPanel.setOperacion(operacion);
         
         /** cargamos los documentos */
         documentosJPanel.load(valor);
         documentosJPanel.setEnabled(editable);
         
         /**añadimos los bienes revertibles*/
 		if (valor.getBienesRevertibles()!=null && valor.getBienesRevertibles().size()>0){
         	 JTabbedPane auxJPanel=new JTabbedPane();
         	datosValorMobiliarioJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
         	 for (Iterator <BienRevertible>it=valor.getBienesRevertibles().iterator();it.hasNext();)
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
             valor= null;
         else{
             datosGenerales1JPanel.actualizarDatosGeneralesComunes((BienBean)valor);
             datosGenerales2JPanel.actualizarDatosGenerales(valor);

             datosSegurosJPanel.actualizarDatos((BienBean)valor);
             observacionesJPanel.actualizarDatos((BienBean)valor);
             documentosJPanel.actualizarDatos((BienBean)valor);
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

     public void setValor(ValorMobiliarioBean valor){
         this.valor= valor;
     }

     public Object getValor(){
         return valor;
     }


    private void exitForm(java.awt.event.WindowEvent evt) {
        setValor(null);
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
		return valor;
	}




}

