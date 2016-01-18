/**
 * IntervencionJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.util.ApplicationContext;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IntervencionJDialog.java
 *
 * Created on 17-mar-2011, 16:02:23
 */



/**
 *
 * @author yraton
 */
public class IntervencionJDialog extends javax.swing.JDialog implements PropertyChangeListener {

    private String operacion;
	private String tipo;
    private ApplicationContext aplicacion;
    
    private IntervencionBean intervencion;
    
	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;

    private ArrayList actionListeners= new ArrayList();
    private String locale;
    private javax.swing.JFrame desktop;

   private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	
    /** Creates new form unidadEnterramientoJFrame */
    public IntervencionJDialog  (JFrame desktop, String locale, String operacion,String tipo) throws Exception{
    	super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        this.operacion= operacion;
        this.tipo=tipo;
        inicializar();
    }

    public IntervencionJDialog(JFrame desktop, String locale) throws Exception{
        super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        inicializar();
    }

    /**
     * Inicializar
     */
    private void inicializar() {
    	
    	this.aplicacion = (AppContext) AppContext.getApplicationContext();
        getContentPane().setLayout(new BorderLayout());
        renombrarComponentes();
        setModal(true);

        desktop = new javax.swing.JFrame();
        
        IntervencionJPanel = new javax.swing.JPanel();
        datosGeneralesComunesJPanel = new javax.swing.JPanel();
        entidadJLabel = new javax.swing.JLabel();
        cementerioJLabel = new javax.swing.JLabel();
        entidadJTextField = new javax.swing.JTextField();
        cementerioJTextField = new javax.swing.JTextField();
        datosIntervencionJPanel = new javax.swing.JPanel();
        localizacionJLabel = new javax.swing.JLabel();
        localizacionJTextField = new javax.swing.JTextField();

        fechaFinJLabel = new javax.swing.JLabel();
        fechaFinJDateChooser =  new JDateChooser(new Date());
        fechaFinJDateChooser.setDateFormatString("dd/MM/yyyy");
        
        fechaInicioJLabel = new javax.swing.JLabel();
        fechaInicioJDateChooser =  new JDateChooser(new Date());
        fechaInicioJDateChooser.setDateFormatString("dd/MM/yyyy");
        
        codigoJLabel = new javax.swing.JLabel();
        codigoJTextField = new javax.swing.JTextField();
        informeJLabel = new javax.swing.JLabel();
        informeJScrollPane = new javax.swing.JScrollPane();
        informeJTextArea = new javax.swing.JTextArea();
        JSeparator = new javax.swing.JSeparator();
        estadoJLabel = new javax.swing.JLabel();
        estadoJTextField = new javax.swing.JTextField();

        
        //botonera, tamaño por defecto y 
        
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
        
          //800x 500
        setSize(820, 820);


        IntervencionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosGeneralesComunesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        datosGeneralesComunesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        entidadJLabel.setText("Entidad");
        datosGeneralesComunesJPanel.add(entidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cementerioJLabel.setText("Cementerio");
        datosGeneralesComunesJPanel.add(cementerioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        entidadJTextField.setText("entidad");
        datosGeneralesComunesJPanel.add(entidadJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 750, -1));

        cementerioJTextField.setText("cementerio");
        datosGeneralesComunesJPanel.add(cementerioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 750, -1));

        IntervencionJPanel.add(datosGeneralesComunesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 840, 70));

        datosIntervencionJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        datosIntervencionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        localizacionJLabel.setText("Localizacion");
        datosIntervencionJPanel.add(localizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 22, 65, -1));
        datosIntervencionJPanel.add(localizacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 19, 747, -1));

        fechaFinJLabel.setText("Fecha Fin");
        datosIntervencionJPanel.add(fechaFinJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 48, -1, -1));
        datosIntervencionJPanel.add(fechaFinJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 45, 143, -1));

        fechaInicioJLabel.setText("Fecha Inicio");
        datosIntervencionJPanel.add(fechaInicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 48, -1, -1));
        datosIntervencionJPanel.add(fechaInicioJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 45, 141, -1));

        codigoJLabel.setText("codigo");
        datosIntervencionJPanel.add(codigoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 48, -1, -1));
        datosIntervencionJPanel.add(codigoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(541, 45, 106, -1));

        informeJLabel.setText("Informe");
        datosIntervencionJPanel.add(informeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 101, 49, -1));

        informeJTextArea.setColumns(20);
        informeJTextArea.setRows(5);
        informeJScrollPane.setViewportView(informeJTextArea);

        datosIntervencionJPanel.add(informeJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 101, 758, 70));
        datosIntervencionJPanel.add(JSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 83, 816, 7));

        estadoJLabel.setText("Estado");
        datosIntervencionJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 48, -1, -1));
        datosIntervencionJPanel.add(estadoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 45, 114, -1));

        IntervencionJPanel.add(datosIntervencionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 840, 190));

        getContentPane().add(IntervencionJPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(botoneraAceptarCancelarJPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }

	public void load(IntervencionBean intervencionElem, boolean editable, String operacion) {
		
		if (intervencionElem == null)
			return;
		entidadJTextField.setText(intervencionElem.getEntidad() != null ?  intervencionElem.getEntidad() : "");
		entidadJTextField.setEditable(false);
		cementerioJTextField.setText(intervencionElem.getNombreCementerio() != null ?  intervencionElem.getNombreCementerio() : "");
		cementerioJTextField.setEditable(false);
		
    	setIntervencion(intervencionElem);
        if(operacion == null) return;

        datosGeneralesComunesJPanel.setEnabled(editable);
        datosIntervencionJPanel.setEnabled(editable);
        
        localizacionJTextField.setText(intervencionElem.getLocalizacion()!= null ? intervencionElem.getLocalizacion()  : "");
        localizacionJTextField.setEditable(editable);
        
        fechaInicioJDateChooser.setDate(intervencionElem.getFechaInicio() != null ? intervencionElem.getFechaInicio() : new Date());
        fechaInicioJDateChooser.setEnabled(editable);
        
        fechaFinJDateChooser.setDate(intervencionElem.getFechaFin() != null ? intervencionElem.getFechaFin() : new Date());
        fechaFinJDateChooser.setEnabled(editable);
        
        codigoJTextField.setText(intervencionElem.getCodigo()!= null ? intervencionElem.getCodigo() : "");
        codigoJTextField.setEditable(editable);
        
        estadoJTextField.setText(intervencionElem.getEstado()!= null ? intervencionElem.getEstado() : "");
        estadoJTextField.setEditable(editable);
        
        informeJTextArea.setText(intervencionElem.getInforme()!= null ? intervencionElem.getInforme() : "");
        informeJTextArea.setEnabled(editable);
        	
	}
	
    /**
     * actualizarDatosIntervencion
     * @param intervencion
     */
	public void actualizarDatosIntervencion(IntervencionBean intervencion){	
      
	  	if (intervencion==null) return;
	      
	  	intervencion.setEntidad(entidadJTextField.getText().trim());
	  	intervencion.setNombreCementerio(cementerioJTextField.getText().trim());
	  	
	  	intervencion.setLocalizacion(localizacionJTextField.getText().trim());
	  	intervencion.setFechaInicio(fechaInicioJDateChooser.getDate());
	  	intervencion.setFechaFin(fechaFinJDateChooser.getDate());
	  	intervencion.setCodigo(codigoJTextField.getText().trim());
	  	intervencion.setEstado(estadoJTextField.getText().trim());
	  	intervencion.setInforme(informeJTextArea.getText().trim());

	  	setIntervencion(intervencion);
	      
	  }
	
	/**
	 * botoneraAceptarCancelarJPanel_actionPerformed
	 */
    public void botoneraAceptarCancelarJPanel_actionPerformed(){
        if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
                (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false)){
             	 intervencion= null;
             }
             else{
             	actualizarDatosIntervencion(intervencion);
             }
             fireActionPerformed();
         }
           
    private boolean confirmOption(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("cementerio.optionpane.tag1"), aplicacion.getI18nString("cementerio.optionpane.tag2"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        setIntervencion(null);
        fireActionPerformed();
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

    
	public void setDate(Date date) {
		((JDateChooser) fechaInicioJDateChooser).setDate(date);
		((JDateChooser) fechaFinJDateChooser).setDate(date);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			setDate((Date) evt.getNewValue());
		}
	}
    
	public IntervencionBean getIntervencion() {
		return intervencion;
	}

	public void setIntervencion(IntervencionBean intervencion) {
		this.intervencion = intervencion;
	}
	    
    /**
     * RenombrarComponentes 
     */
     public void renombrarComponentes(){
         try{datosGeneralesComunesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosGenerales.tag1")));}catch(Exception e){}
     }
    
    // Variables declaration - do not modify
    private javax.swing.JPanel IntervencionJPanel;
    private javax.swing.JSeparator JSeparator;
    private javax.swing.JLabel cementerioJLabel;
    private javax.swing.JTextField cementerioJTextField;
    private javax.swing.JLabel codigoJLabel;
    private javax.swing.JTextField codigoJTextField;
    private javax.swing.JPanel datosGeneralesComunesJPanel;
    private javax.swing.JPanel datosIntervencionJPanel;
    private javax.swing.JLabel entidadJLabel;
    private javax.swing.JTextField entidadJTextField;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JTextField estadoJTextField;
    private javax.swing.JLabel fechaFinJLabel;
    private JDateChooser fechaFinJDateChooser;
    private javax.swing.JLabel fechaInicioJLabel;
    private JDateChooser fechaInicioJDateChooser;
    private javax.swing.JLabel informeJLabel;
    private javax.swing.JScrollPane informeJScrollPane;
    private javax.swing.JTextArea informeJTextArea;
    private javax.swing.JLabel localizacionJLabel;
    private javax.swing.JTextField localizacionJTextField;
  

}

