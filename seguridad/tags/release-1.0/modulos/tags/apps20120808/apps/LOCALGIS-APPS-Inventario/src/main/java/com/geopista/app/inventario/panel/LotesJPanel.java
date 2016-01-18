package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;

import com.geopista.app.utilidades.TextPane;
import com.geopista.protocol.inventario.Observacion;
import com.geopista.protocol.inventario.BienBean;

import javax.swing.*;


/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-ago-2006
 * Time: 12:51:08
 * To change this template use File | Settings | File Templates.
 */
public class LotesJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AppContext aplicacion;





    /**
     * Método que genera el panel de las Observaciones de un bien
     */
    public LotesJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    public void initComponents() throws Exception{

    	nombreJLabel= new javax.swing.JLabel();
    	nombreJTextField= new JTextField();
        seguroJLabel= new javax.swing.JLabel();
        seguroJTextField =new JTextField();
        destinoJLabel= new javax.swing.JLabel();
        destinoJTextField =new JTextField();
        descripcionJLabel= new javax.swing.JLabel();
        descripcionJScroolPane =new javax.swing.JScrollPane();
        descripcionJTextPane =new TextPane(999);
        numeroJLabel = new JLabel();
        numeroJTextField= new JTextField();
        documentosJPanel = new GestionDocumentalJPanel(false);
        
        
          

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 100, 20));
        add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 300, 20));
        add(seguroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 100, 20));
        add(seguroJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 35, 300, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 20));
        add(destinoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 300, 20));
        add(numeroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 300, 20));
        add(numeroJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 85, 30, 20));
        add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, 20));
        descripcionJScroolPane.setViewportView(descripcionJTextPane);
        add(descripcionJScroolPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,135, 400, 60));
        add(documentosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 400, 400));
         
    }

    /**
     * Método que carga la lista de Observaciones de un bien
     * @param bien
     */
    public void load(BienBean bien){
        if (bien==null) return;
    }

    /**
     * Método que visualiza la observacion seleccionada de la lista
     * @param obs seleccionada
     */
    public void load(Observacion obs){
        if (obs==null) return;
    }

    public void actualizarDatos(BienBean bien){
        if (bien==null) return;
        //bien.setObservaciones(observacionesListaPane.getCollection());
    }


    public void clear(){
        nombreJTextField.setText("");
        seguroJTextField.setText("");
        destinoJTextField.setText("");
        descripcionJTextPane.setText("");
      }

    public void setEnabled(boolean b){
    }

    public void renombrarComponentes(){
    	try{nombreJLabel.setText("Nombre:"/*aplicacion.getI18nString("inventario.observaciones.tag2")*/);}catch(Exception e){}
    	try{nombreJLabel.setToolTipText("Nombre"/*aplicacion.getI18nString("inventario.observaciones.tag4")*/);}catch(Exception e){}
        try{seguroJLabel.setText("Seguro:"/*aplicacion.getI18nString("inventario.observaciones.tag3")*/);}catch(Exception e){}
        try{seguroJLabel.setToolTipText("Seguro"/*aplicacion.getI18nString("inventario.observaciones.tag4")*/);}catch(Exception e){}
        try{destinoJLabel.setText("Destino:"/*aplicacion.getI18nString("inventario.observaciones.tag4")*/);}catch(Exception e){}
        try{destinoJLabel.setToolTipText("Destino"/*aplicacion.getI18nString("inventario.observaciones.tag4")*/);}catch(Exception e){}
        try{descripcionJLabel.setText("Descripción:"/*aplicacion.getI18nString("inventario.observaciones.tag5")*/);}catch(Exception e){}
        try{descripcionJLabel.setToolTipText("Descripción"/*aplicacion.getI18nString("inventario.observaciones.tag5")*/);}catch(Exception e){}
        try{numeroJLabel.setText("Número de bienes en el lote:"/*aplicacion.getI18nString("inventario.observaciones.tag5")*/);}catch(Exception e){}
        try{numeroJLabel.setToolTipText("Número de bienes en el lote"/*aplicacion.getI18nString("inventario.observaciones.tag5")*/);}catch(Exception e){}
   
    }

   


    
  
    private javax.swing.JLabel nombreJLabel;
    private javax.swing.JTextField nombreJTextField;
    private javax.swing.JLabel seguroJLabel;
    private javax.swing.JTextField seguroJTextField;
    private javax.swing.JLabel destinoJLabel;
    private javax.swing.JTextField destinoJTextField;
    private javax.swing.JLabel numeroJLabel;
    private javax.swing.JTextField numeroJTextField;
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JScrollPane descripcionJScroolPane;
    private TextPane descripcionJTextPane;
    private GestionDocumentalJPanel documentosJPanel;
    
}
