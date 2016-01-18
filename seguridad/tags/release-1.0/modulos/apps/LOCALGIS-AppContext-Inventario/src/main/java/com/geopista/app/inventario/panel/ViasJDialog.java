package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.ViaBean;
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
 * Date: 26-sep-2006
 * Time: 15:17:04
 * To change this template use File | Settings | File Templates.
 */
public class ViasJDialog extends JDialog implements BienJDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(ViasJDialog.class);

     private DatosGeneralesViasJPanel datosGenerales2JPanel;
     private DatosGeneralesComunesJPanel datosGenerales1JPanel;
     private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
     private ApplicationContext aplicacion;

     private ViaBean via;

     private ArrayList actionListeners= new ArrayList();
     private javax.swing.JPanel datosGeneralesJPanel;
     private javax.swing.JTabbedPane datosViaJTabbedPane;
     private DatosSegurosJPanel datosSegurosJPanel;
     private ObservacionesJPanel observacionesJPanel;
     private GestionDocumentalJPanel documentosJPanel;
     private MejorasJPanel mejorasJPanel;
     private ReferenciasCatastralesJPanel refCatastralesJPanel;
     private String operacion;
     private String locale;
 	
 	// Para poder integrar con EIEL:
 	private DatosEIELJPanel eielJPanel;

 	
	private String tipo;

     /**
      * Método que genera el dialogo que muestra los datos de una via urbana y/o rustica
      * @param desktop
      * @param locale
      */
     public ViasJDialog(JFrame desktop, String locale, String tipo) throws Exception{
         super(desktop);
         this.aplicacion = (AppContext) AppContext.getApplicationContext();
         this.locale=locale;
         getContentPane().setLayout(new BorderLayout());
         renombrarComponentes();
         this.tipo=tipo;
         setModal(true);

         datosViaJTabbedPane= new javax.swing.JTabbedPane();
         datosViaJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
         datosViaJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

         datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
         datosGenerales2JPanel= new DatosGeneralesViasJPanel(desktop, locale, tipo);

         /** Montamos el panel de datos generales */
         datosGeneralesJPanel= new JPanel();
         datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));
         datosGeneralesJPanel.setLayout(new BorderLayout());
         datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
         datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);

  		// Para poder integrar con EIEL:
  		eielJPanel = new DatosEIELJPanel(locale);
  		datosViaJTabbedPane.addTab(aplicacion
  				.getI18nString("inventario.inmuebleDialog.tab13"),
  				eielJPanel);
         
         datosSegurosJPanel= new DatosSegurosJPanel();
         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

         observacionesJPanel= new ObservacionesJPanel();
         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

         mejorasJPanel= new MejorasJPanel();
         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab9"), mejorasJPanel);

         refCatastralesJPanel= new ReferenciasCatastralesJPanel();
         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab10"), refCatastralesJPanel);
         
         documentosJPanel= new GestionDocumentalJPanel(false);
         datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
         
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

         getContentPane().add(datosViaJTabbedPane, BorderLayout.NORTH);
         getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
         setSize(470, 730);
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
  				int indice = datosViaJTabbedPane.getSelectedIndex();
  				String uriRelativa = "";
  				String tipoVia="";
  				if (tipo.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
  					tipoVia="Vías_Públicas_Rústicas";
  				else
  					tipoVia="Vías_Públicas_Urbanas";
  				
  	 			switch (indice) {
  	 				case 0:
  	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Datos_Generales";	break;
  	 				case 1:
  	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Datos_Seguros";break;
  	 				case 2:
  	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Observaciones";break;
  	 				case 3:
  	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Mejoras";break;
  	 				case 4:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Referencias_Catastrales";break;
  	 				case 5:
  	 					uriRelativa = "/Geocuenca:Inventario:"+tipoVia+"#Documentos";break;  	 				
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
      * Método que carga una via en la ventana de dialogo
      * @param via a cargar
      * @param editable true si el dialogo se abre en modo edicion, false en caso contrario
      */
     public void load(ViaBean via, boolean editable) throws Exception {
         setVia(via);
         if(operacion == null) return;
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
         }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
             datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
         }
         datosGenerales1JPanel.setEnabled(editable);
         datosGenerales2JPanel.setEnabled(editable);
         datosGenerales1JPanel.load(via);
         datosGenerales2JPanel.load(via);

         /** cargamos el seguro */
         datosSegurosJPanel.load(via);
         datosSegurosJPanel.setEnabledDatos(false);
         if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) datosSegurosJPanel.setEnabledBotonera(false);
         else{
             datosSegurosJPanel.addJButtonSetEnabled(true);
             datosSegurosJPanel.buscarJButtonSetEnabled(true);
             datosSegurosJPanel.editarJButtonSetEnabled(via!=null && via.getSeguro()!=null);
             datosSegurosJPanel.borrarJButtonSetEnabled(via!=null && via.getSeguro()!=null);
         }

         /** cargamos las observaciones */
         observacionesJPanel.load(via);
         observacionesJPanel.setEnabled(editable);
         observacionesJPanel.setOperacion(operacion);

 		// Para poder integrar con EIEL:
 		/** cargamos los datos EIEL */
 		eielJPanel.load(via);
 		eielJPanel.setEnabled(false);	
 		
         /** cargamos las mejoras */
         mejorasJPanel.setEnabled(editable);
         mejorasJPanel.setOperacion(operacion);
         mejorasJPanel.load(via);

         /** cargamos las referencias catastrales */
         refCatastralesJPanel.setEnabled(editable);
         refCatastralesJPanel.load(via);
         refCatastralesJPanel.setOperacion(operacion);
         
         /** cargamos los documentos */
         documentosJPanel.load(via);
         documentosJPanel.setEnabled(editable);
         
         /** cargamos los bienes revertibles */
         if (via.getBienesRevertibles()!=null && via.getBienesRevertibles().size()>0){
         	 JTabbedPane auxJPanel=new JTabbedPane();
        	 datosViaJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
         	 for (Iterator<BienRevertible> it=via.getBienesRevertibles().iterator();it.hasNext();)
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
             via= null;
         else{
             datosGenerales1JPanel.actualizarDatosGeneralesComunes((BienBean)via);
             datosGenerales2JPanel.actualizarDatosGenerales(via);

             datosSegurosJPanel.actualizarDatos((BienBean)via);
             observacionesJPanel.actualizarDatos((BienBean)via);
             mejorasJPanel.actualizarDatos((BienBean)via);
             refCatastralesJPanel.actualizarDatos((ViaBean)via);
             documentosJPanel.actualizarDatos((BienBean)via);
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

     public void setVia(ViaBean via){
         this.via= via;
     }

     public Object getVia(){
         return via;
     }


    private void exitForm(java.awt.event.WindowEvent evt) {
        setVia(null);
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
		return via;
	}



}
