/**
 * BienesRevertiblesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel.bienesRevertibles;


import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-may-2010
 * Time: 12:20:10
 * To change this template use File | Settings | File Templates.
 */

public class BienesRevertiblesPanel extends JPanel{ 

	private static final long serialVersionUID = 1L;
    /* constructor de la clase dd se pinta el panel */

	private BienRevertible bienRevertible;
	private AppContext aplicacion;
	private String locale;
    /**
	 * 
	 */
	
	public BienesRevertiblesPanel( BienRevertible bienRevertible , String locale)throws Exception {
       
         this.aplicacion= (AppContext) AppContext.getApplicationContext();
         this.locale=locale;
         initComponents();
         renombrarComponentes();
         load(bienRevertible);
      
    }
    public void initComponents() throws Exception{

    	fechaInicioJLabel= new InventarioLabel();
    	fechaInicioJTextField = new JFormattedTextField(Constantes.df);
        fechaInicioJButton= new CalendarButton(fechaInicioJTextField);
        
        fechaVencimientoJLabel= new InventarioLabel();
        fechaVencimientoJTextField = new JFormattedTextField(Constantes.df);
        fechaVencimientoJButton= new CalendarButton(fechaVencimientoJTextField);
        
        fechaTransmisionJLabel= new InventarioLabel();
        fechaTransmisionJTextField = new JFormattedTextField(Constantes.df);
        fechaTransmisionJButton= new CalendarButton(fechaTransmisionJTextField);
        
        importeJLabel = new InventarioLabel();
        importeJTextField =  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeJTextField.setSignAllowed(false);
        
        numInventarioJLabel = new InventarioLabel();
        numInventarioJTextField = new InventarioTextField(50);
        numInventarioJTextField.setEditable(true);
        
        poseedorJLabel = new InventarioLabel();
        poseedorJTextField = new InventarioTextField(100);
        
        tituloJLabel = new InventarioLabel();
        tituloJTextField =new InventarioTextField(100);
        
        detallesJLabel = new InventarioLabel();
        detallesJScroolPane = new javax.swing.JScrollPane(); 
        detallesJTextPane = new InventarioTextPane(500);
        detallesJScroolPane.setViewportView(detallesJTextPane);
        
        condicionesJLabel = new InventarioLabel();
        condicionesJScroolPane = new javax.swing.JScrollPane(); 
        condicionesJTextPane = new InventarioTextPane(255);
        condicionesJScroolPane.setViewportView(condicionesJTextPane);
        
        transmisionJLabel = new InventarioLabel();
        transmisionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTransmision(), null,  locale, true);
        					
        nombreJLabel= new InventarioLabel();
        nombreJTextField= new InventarioTextField(255);
        
        organizacionJLabel= new InventarioLabel();
        organizacionJTextField= new InventarioTextField(255);
        
        fechaAprobacionPlenoJLabel= new InventarioLabel();
        fechaAprobacionPlenoJTextField = new JFormattedTextField(Constantes.df);
        fechaAprobacionPlenoJButton= new CalendarButton(fechaAprobacionPlenoJTextField);
        
        descripcionBienJLabel= new InventarioLabel();
        descripcionBienJTextField= new InventarioTextField(255);

        fechaAdquisicionJLabel= new InventarioLabel();
        fechaAdquisicionJTextField = new JFormattedTextField(Constantes.df);
        fechaAdquisicionJButton= new CalendarButton(fechaAdquisicionJTextField);
        
        adquisicionJLabel= new InventarioLabel();
        adquisicionEJCBox = new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        
        diagnosisJLabel= new InventarioLabel();
        diagnosisEJCBox = new ComboBoxEstructuras(Estructuras.getListaDiagnosis(), null, locale, true);
      
        tipoJLabel= new InventarioLabel();
        tipoJTextField= new InventarioTextField(255);
      
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        JPanel datosGenerales = new JPanel();
        datosGenerales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosGenerales.setBorder(BorderFactory.createEtchedBorder());
                   
        datosGenerales.add(organizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 20));
        datosGenerales.add(organizacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 300, 20));
        
        datosGenerales.add(tipoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 100, 20));
        datosGenerales.add(tipoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 35, 300, 20));
        
        datosGenerales.add(numInventarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 20));
        datosGenerales.add(numInventarioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 300, 20));
       
        datosGenerales.add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, 100, 20));
        datosGenerales.add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 85, 300, 20));
       
        datosGenerales.add(fechaAprobacionPlenoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, 20));
        datosGenerales.add(fechaAprobacionPlenoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 100, 20));
        datosGenerales.add(fechaAprobacionPlenoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 20, 20));
       
       
        add(datosGenerales, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 140));
        
        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 100, 20));
        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 300, 20));
        
        add(fechaAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 100, 20));
        add(fechaAdquisicionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 175, 100, 20));
        add(fechaAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 175, 20, 20));
       
        add(descripcionBienJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 100, 20));
        add(descripcionBienJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 300, 20));
        
        add(poseedorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 225, 100, 20));
        add(poseedorJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 225, 300, 20));
        
        add(tituloJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 100, 20));
        add(tituloJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 300, 20));
        
        add(fechaInicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 275, 100, 20));
        add(fechaInicioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 275, 100, 20));
        add(fechaInicioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 275, 20, 20));
        
        add(fechaVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 100, 20));
        add(fechaVencimientoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 100, 20));
        add(fechaVencimientoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 20, 20));
        
        add(fechaTransmisionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 325, 100, 20));
        add(fechaTransmisionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 325, 100, 20));
        add(fechaTransmisionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 20, 20));

        add(transmisionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 100, 20));
        add(transmisionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 100, 20));
        
        add(importeJLabel , new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 375, 100, 20));
        add(importeJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 375, 100, 20)); 
        
        add(diagnosisJLabel , new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 100, 20));
        add(diagnosisEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, 100, 20)); 
        
        add(detallesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 425, 100, 20));
        add(detallesJScroolPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 425, 60));
        
        add(condicionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 515, 100, 20));
        add(condicionesJScroolPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 425, 60)); 
      
           
    }
    
    public void renombrarComponentes(){
    	
    	fechaInicioJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.fechainicio")+":");//"Fecha inicio"+":");
        fechaVencimientoJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.fechavencimiento")+":");//"Fecha vencimiento"+":");
        fechaTransmisionJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.fechatransmision")+":");//"Fecha transmisión"+":");
        importeJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.importe")+":");//"Importe"+":");
        poseedorJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.poseedor")+":");//"Poseedor"+":");
        numInventarioJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.numinventario")+":");//"Núm. Inventario"+":");
        tituloJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.tituloposesion")+":");//"Titulo de posesión"+":");
        detallesJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.detalles")+":");//"Detalles"+":");
        condicionesJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.reversion")+":");//"Condiciones reversion"+":");
        transmisionJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.transmision")+":");//"Transmision"+":");
        nombreJLabel.setText("Nombre:");
        organizacionJLabel.setText("Organización:");
        fechaAprobacionPlenoJLabel.setText("Fecha aprobación pleno:");
        descripcionBienJLabel.setText("Descripción bien:");
        fechaAdquisicionJLabel.setText("Fecha Adquisición:");
        adquisicionJLabel.setText("Forma Adquisición");
        diagnosisJLabel.setText("Diagnosis:");
        tipoJLabel.setText("Tipo:");
        
    }
    
    public void setEnabled(boolean valor){
    	fechaInicioJTextField.setEnabled(valor);
 	    fechaInicioJButton.setEnabled(valor);
 	    
 	    fechaVencimientoJTextField.setEnabled(valor);
 	    fechaVencimientoJButton.setEnabled(valor);
 	    
 	    fechaTransmisionJTextField.setEnabled(valor);
	    fechaTransmisionJButton.setEnabled(valor);
	    
 	    importeJTextField.setEnabled(valor);  
 	    poseedorJTextField.setEnabled(valor);
 	    numInventarioJTextField.setEnabled(valor);
 	    
 	    tituloJTextField.setEnabled(valor);
 	    detallesJTextPane.setEnabled(valor);
 	    condicionesJTextPane.setEnabled(valor);
 	    transmisionEJCBox.setEnabled(valor);
 	    nombreJTextField.setEnabled(valor);
 	    organizacionJTextField.setEnabled(false);
 	    fechaAprobacionPlenoJTextField.setEnabled(valor);
 	    fechaAprobacionPlenoJButton.setEnabled(valor);
        descripcionBienJTextField.setEnabled(valor);
 	    fechaAdquisicionJTextField.setEnabled(valor);
        fechaAdquisicionJButton.setEnabled(valor);
        adquisicionEJCBox.setEnabled(valor);
 	    diagnosisEJCBox.setEnabled(valor);
 	    tipoJTextField.setEnabled(false);
 	    
    }
    /**
     * Muestra en pantalla los datos de un bien revertible
     * @param bienRevertibleAux
     */
    public void load(BienRevertible bienRevertibleAux){
    	if (bienRevertibleAux==null)
    		this.bienRevertible=new BienRevertible();
    	else
    		this.bienRevertible= bienRevertibleAux;
    	tipoJTextField.setText(Estructuras.getListaClasificacionBienesPatrimonio().getDomainNode(Const.SUPERPATRON_REVERTIBLES).getTerm(locale));	
    	numInventarioJTextField.setText(bienRevertible.getNumInventario()==null?"":bienRevertible.getNumInventario());
    	poseedorJTextField.setText(bienRevertible.getPoseedor()==null?"":bienRevertible.getPoseedor());
    	tituloJTextField.setText(bienRevertible.getTituloPosesion()==null?"":bienRevertible.getTituloPosesion());
     	detallesJTextPane.setText(bienRevertible.getDetalles()==null?"":bienRevertible.getDetalles());
     	condicionesJTextPane.setText(bienRevertible.getCondicionesReversion()==null?"":bienRevertible.getCondicionesReversion());
     	
     	try{fechaInicioJTextField.setText(Constantes.df.format(bienRevertible.getFechaInicio()));}catch(Exception e){}
    	try{fechaVencimientoJTextField.setText(Constantes.df.format(bienRevertible.getFechaVencimiento()));}catch(Exception e){}
    	try{fechaTransmisionJTextField.setText(Constantes.df.format(bienRevertible.getFechaTransmision()));}catch(Exception e){}
         	
        try{importeJTextField.setNumber(bienRevertible.getImporte());}catch(Exception e){}
        transmisionEJCBox.setSelectedPatron(bienRevertible.getCatTransmision());
        
        nombreJTextField.setText(bienRevertible.getNombre());
        organizacionJTextField.setText(bienRevertible.getOrganizacion());
        try{fechaAprobacionPlenoJTextField.setText(Constantes.df.format(bienRevertible.getFecha_aprobacion_pleno()));}catch(Exception e){}
        descripcionBienJTextField.setText(bienRevertible.getDescripcion_bien());
        try{fechaAdquisicionJTextField.setText(Constantes.df.format(bienRevertible.getFecha_adquisicion()));}catch(Exception e){}
        adquisicionEJCBox.setSelectedPatron(bienRevertible.getAdquisicion());
        diagnosisEJCBox.setSelectedPatron(bienRevertible.getDiagnosis());
     }
	
    /**
     * Devuelve los cambios producidos en el bien revertible
     * @return
     */
    public BienRevertible getBienRevertible(){
    	bienRevertible.setNumInventario(numInventarioJTextField.getText());
    	bienRevertible.setPoseedor(poseedorJTextField.getText());
       	bienRevertible.setTituloPosesion(tituloJTextField.getText());
        try{bienRevertible.setFechaInicio(Constantes.df.parse(fechaInicioJTextField.getText().trim()));}catch(java.text.ParseException e){}
        try{bienRevertible.setFechaVencimiento(Constantes.df.parse(fechaVencimientoJTextField.getText().trim()));}catch(java.text.ParseException e){}
        try{bienRevertible.setFechaTransmision(Constantes.df.parse(fechaTransmisionJTextField.getText().trim()));}catch(java.text.ParseException e){}
        bienRevertible.setDetalles(detallesJTextPane.getText());
        bienRevertible.setCondicionesReversion(condicionesJTextPane.getText());
        bienRevertible.setCatTransmision(transmisionEJCBox.getSelectedPatron());
        try{bienRevertible.setImporte((Double)importeJTextField.getNumber());}catch(Exception e){}
        bienRevertible.setNombre(nombreJTextField.getText());
        try{bienRevertible.setFecha_aprobacion_pleno(Constantes.df.parse(fechaAprobacionPlenoJTextField.getText().trim()));}catch(java.text.ParseException e){}
        bienRevertible.setDescripcion_bien(descripcionBienJTextField.getText());
        try{bienRevertible.setFecha_adquisicion(Constantes.df.parse(fechaAdquisicionJTextField.getText().trim()));}catch(java.text.ParseException e){}
        bienRevertible.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        bienRevertible.setDiagnosis(diagnosisEJCBox.getSelectedPatron());
        
        return bienRevertible;
    }
    
    private javax.swing.JLabel fechaInicioJLabel;
    private JFormattedTextField  fechaInicioJTextField;
    private CalendarButton fechaInicioJButton;

    private javax.swing.JLabel fechaVencimientoJLabel;
    private JFormattedTextField fechaVencimientoJTextField;
    private CalendarButton fechaVencimientoJButton;
    
    private javax.swing.JLabel fechaTransmisionJLabel;
    private JFormattedTextField fechaTransmisionJTextField;
    private CalendarButton fechaTransmisionJButton;


    private javax.swing.JLabel importeJLabel;
    private com.geopista.app.utilidades.JNumberTextField importeJTextField;
    private javax.swing.JLabel numInventarioJLabel;
    private javax.swing.JTextField numInventarioJTextField;
    
    private javax.swing.JLabel poseedorJLabel;
    private javax.swing.JTextField poseedorJTextField;
    private javax.swing.JLabel tituloJLabel;
    private javax.swing.JTextField tituloJTextField;
    private javax.swing.JLabel detallesJLabel;
    private javax.swing.JScrollPane detallesJScroolPane;
    private TextPane detallesJTextPane;
    private javax.swing.JLabel condicionesJLabel;
    private javax.swing.JScrollPane condicionesJScroolPane;
    
    private javax.swing.JLabel transmisionJLabel;
    private ComboBoxEstructuras transmisionEJCBox;
    
    private javax.swing.JLabel nombreJLabel;
    private javax.swing.JTextField nombreJTextField;
    
    private javax.swing.JLabel organizacionJLabel;
    private javax.swing.JTextField organizacionJTextField;
    
    private javax.swing.JLabel fechaAprobacionPlenoJLabel;
    private JFormattedTextField fechaAprobacionPlenoJTextField;
    private CalendarButton fechaAprobacionPlenoJButton;

    private javax.swing.JLabel descripcionBienJLabel;
    private javax.swing.JTextField descripcionBienJTextField;
    
    private javax.swing.JLabel fechaAdquisicionJLabel;
    private JFormattedTextField fechaAdquisicionJTextField;
    private CalendarButton fechaAdquisicionJButton;

    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;
    
    private javax.swing.JLabel diagnosisJLabel;
    private ComboBoxEstructuras diagnosisEJCBox;
    
    private javax.swing.JLabel tipoJLabel;
    private javax.swing.JTextField tipoJTextField;
    
    
    private TextPane condicionesJTextPane;
    
    

   
}

