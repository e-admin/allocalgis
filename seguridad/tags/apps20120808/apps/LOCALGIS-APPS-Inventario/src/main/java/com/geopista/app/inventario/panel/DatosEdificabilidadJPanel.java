package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.InmuebleBean;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DatosEdificabilidadJPanel  extends javax.swing.JPanel {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;
    private javax.swing.JFrame desktop;
    private OtroValorJDialog otroValorJDialog;

    private static String CARPINTERIA= "CARPINTERIA";
    private static String FACHADA= "FACHADA";
    private static String CUBIERTA= "CUBIERTA";
    private static String CONSTRUCCION= "CONSTRUCCION";
    private static String CONSERVACION= "CONSERVACION";

    /**
     * Método que genera el panel de los datos de Edificabilidad de un bien inmueble
     */
    public DatosEdificabilidadJPanel(JFrame desktop, String locale) {
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        
    	calificacionJLabel = new InventarioLabel();
        fechaObraJLabel = new InventarioLabel();
        edificabilidadJLabel = new InventarioLabel();
        numPlantasJLabel = new InventarioLabel();
        estadoConservacionJLabel = new InventarioLabel();
        tipoConstruccionJLabel = new InventarioLabel();
        fachadaJLabel= new InventarioLabel();
        cubiertaJLabel= new InventarioLabel();
        carpinteriaExteriorJLabel= new InventarioLabel();
        descripcionJLabel= new InventarioLabel();
        bienInmuebleJCBox = new javax.swing.JCheckBox();
        bienInmuebleJCBox.addItemListener(
        	    new ItemListener() {
        	        public void itemStateChanged(ItemEvent e) {
        	            // Set "ignore" whenever box is checked or unchecked.
        	            if(e.getStateChange() == ItemEvent.SELECTED){
        	            	  bienCatalogadoJCBox.setEnabled(false);
        	            	  bienCatalogadoJCBox.setSelected(false);
        	            }
        	            else
        	            	bienCatalogadoJCBox.setEnabled(true);
        	        }
        	    }
        	);

        bienCatalogadoJCBox= new javax.swing.JCheckBox();        

        conservacionJButton= new JButton();
        conservacionJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        conservacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(CONSERVACION);
            }
        });
        construccionJButton= new JButton();
        construccionJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        construccionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(CONSTRUCCION);
            }
        });
        fachadaJButton= new JButton();
        fachadaJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        fachadaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(FACHADA);
            }
        });
        cubiertaJButton= new JButton();
        cubiertaJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        cubiertaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(CUBIERTA);
            }
        });
        carpinteriaJButton= new JButton();
        carpinteriaJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        carpinteriaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(CARPINTERIA);
            }
        });


        calificacionJTField= new InventarioTextField(50);
        fechaObraJTField= new JFormattedTextField(Constantes.df);
        fechaObraJTField.setEnabled(false);
        
        edificabilidadJNField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(99999), true, 2);
        edificabilidadJNField.setSignAllowed(false);
        fObraJButton= new CalendarButton(fechaObraJTField);

        numPlantasJNField = new InventarioTextField(5);
        
        estadoConservacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);
        estadoConservacionEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CONSERVACION);
            }
        });

        tipoConstruccionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTipoConstruccion(), null, locale, true);
        tipoConstruccionEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CONSTRUCCION);
            }
        });

        fachadaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTipoFachada(), null, locale, true);
        fachadaEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(FACHADA);
            }
        });

        cubiertaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTipoCubierta(), null, locale, true);
        cubiertaEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CUBIERTA);
            }
        });

        carpinteriaExteriorEJCBox= new ComboBoxEstructuras(Estructuras.getListaTipoCarpinteria(), null, locale, true);
        carpinteriaExteriorEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CARPINTERIA);
            }
        });
        descripcionJTField= new InventarioTextField(255);
        conservacionJTField= new InventarioTextField();
        construccionJTField= new InventarioTextField();
        fachadaJTField= new InventarioTextField();
        cubiertaJTField= new InventarioTextField();
        carpinteriaJTField= new InventarioTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(calificacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(fechaObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(edificabilidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(numPlantasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 20));
        add(estadoConservacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(tipoConstruccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(fachadaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(cubiertaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));
        add(carpinteriaExteriorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));
        add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, 20));


        add(calificacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 310, -1));
        add(fechaObraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 140, -1));
        add(fObraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 20, 20));
        add(edificabilidadJNField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, -1));
        add(numPlantasJNField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, -1));
        add(estadoConservacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 140, -1));
        add(conservacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 20, 20));
        add(conservacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 110, 140, -1));
        add(tipoConstruccionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 140, -1));
        add(construccionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 20, 20));
        add(construccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 130, 140, -1));
        add(fachadaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 140, -1));
        add(fachadaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, 20, 20));
        add(fachadaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 150, 140, -1));
        add(cubiertaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 140, -1));
        add(cubiertaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 20, 20));
        add(cubiertaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 170, 140, -1));
        add(carpinteriaExteriorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 140, -1));
        add(carpinteriaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 190, 20, 20));
        add(carpinteriaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 190, 140, -1));
        add(descripcionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 310, -1));
        add(bienInmuebleJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 380, -1));
        add(bienCatalogadoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 380, -1));
        
    }

    private void boxActionPerformed(String s){
        if (s.equalsIgnoreCase(CONSERVACION)){
            if (estadoConservacionEJCBox.getSelectedPatron()!=null) conservacionJTField.setText("");
        }else if (s.equalsIgnoreCase(CONSTRUCCION)){
            if (tipoConstruccionEJCBox.getSelectedPatron()!=null) construccionJTField.setText("");
        }else if (s.equalsIgnoreCase(CUBIERTA)){
            if (cubiertaEJCBox.getSelectedPatron()!=null) cubiertaJTField.setText("");
        }else if (s.equalsIgnoreCase(FACHADA)){
            if (fachadaEJCBox.getSelectedPatron()!=null) fachadaJTField.setText("");
        }else if (s.equalsIgnoreCase(CARPINTERIA)){
            if (carpinteriaExteriorEJCBox.getSelectedPatron()!=null) carpinteriaJTField.setText("");
        }
    }

    private void abrirDialogoJButtonActionPerformed(final String s) {
        if (s.equalsIgnoreCase(CONSERVACION)){
            otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.datosEdificabilidad.tag11"),
                                                   aplicacion.getI18nString("inventario.datosEdificabilidad.tag6"));
        }
        else if (s.equalsIgnoreCase(CONSTRUCCION)){
            otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.datosEdificabilidad.tag12"),
                                                   aplicacion.getI18nString("inventario.datosEdificabilidad.tag7"));
        }else if (s.equalsIgnoreCase(CUBIERTA)){
            otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.datosEdificabilidad.tag13"),
                                                   aplicacion.getI18nString("inventario.datosEdificabilidad.tag9"));
        }else if (s.equalsIgnoreCase(FACHADA)){
            otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.datosEdificabilidad.tag14"),
                                                   aplicacion.getI18nString("inventario.datosEdificabilidad.tag8"));
        }else if (s.equalsIgnoreCase(CARPINTERIA)){
            otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.datosEdificabilidad.tag15"),
                                                   aplicacion.getI18nString("inventario.datosEdificabilidad.tag10"));
        }

        otroValorJDialog.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				otroValorJDialog_actionPerformed(s);
			}
		});
        otroValorJDialog.show();
    }


    private void otroValorJDialog_actionPerformed(String s){
        if (s.equalsIgnoreCase(CONSERVACION)){
            if (otroValorJDialog.getValor()!=null) estadoConservacionEJCBox.setSelectedPatron(null);
            conservacionJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");
        }else if (s.equalsIgnoreCase(CONSTRUCCION)){
            if (otroValorJDialog.getValor()!=null) tipoConstruccionEJCBox.setSelectedPatron(null);
            construccionJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");
        }else if (s.equalsIgnoreCase(CUBIERTA)){
            if (otroValorJDialog.getValor()!=null) cubiertaEJCBox.setSelectedPatron(null);
            cubiertaJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");
        }else if (s.equalsIgnoreCase(FACHADA)){
            if (otroValorJDialog.getValor()!=null) fachadaEJCBox.setSelectedPatron(null);
            fachadaJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");
        }else if (s.equalsIgnoreCase(CARPINTERIA)){
            if (otroValorJDialog.getValor()!=null) carpinteriaExteriorEJCBox.setSelectedPatron(null);
            carpinteriaJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");
        }

       otroValorJDialog.dispose();
    }


    /**
     * Método que actualiza los datos de edificabilidad para un bien inmueble
     * @param bien a actualizar sus datos de edificabilidad
     */
    public void actualizarDatos(Object bien){
        if (bien == null) return;
        if (bien instanceof InmuebleBean){
            ((InmuebleBean)bien).setCalificacion(calificacionJTField.getText().trim());
            try{((InmuebleBean)bien).setFechaObra(Constantes.df.parse(fechaObraJTField.getText().trim()));}catch(java.text.ParseException e){}
            try{((InmuebleBean)bien).setEdificabilidad(((Double)edificabilidadJNField.getNumber()).doubleValue());}catch(Exception e){}
            ((InmuebleBean)bien).setNumPlantas(numPlantasJNField.getText().trim());
            ((InmuebleBean)bien).setEstadoConservacion(estadoConservacionEJCBox.getSelectedPatron());
            ((InmuebleBean)bien).setEstadoConservacionDesc(conservacionJTField.getText());
            ((InmuebleBean)bien).setTipoConstruccion(tipoConstruccionEJCBox.getSelectedPatron());
            ((InmuebleBean)bien).setTipoConstruccionDesc(construccionJTField.getText());
            ((InmuebleBean)bien).setFachada(fachadaEJCBox.getSelectedPatron());
            ((InmuebleBean)bien).setFachadaDesc(fachadaJTField.getText());
            ((InmuebleBean)bien).setCubierta(cubiertaEJCBox.getSelectedPatron());
            ((InmuebleBean)bien).setCubiertaDesc(cubiertaJTField.getText());
            ((InmuebleBean)bien).setCarpinteria(carpinteriaExteriorEJCBox.getSelectedPatron());
            ((InmuebleBean)bien).setCarpinteriaDesc(carpinteriaJTField.getText());
            ((InmuebleBean)bien).setEdificabilidadDesc(descripcionJTField.getText());
            ((InmuebleBean)bien).setBic(bienInmuebleJCBox.isSelected());
            ((InmuebleBean)bien).setCatalogado(bienCatalogadoJCBox.isSelected());

        }

    }

    /**
     * Método que carga en el panel los datos de edificabilidad de un bien inmueble
     * @param bien a cargar en el panel
     */
    public void load(Object bien){
        if (bien == null) return;
        if (bien instanceof InmuebleBean){
            InmuebleBean inmueble= (InmuebleBean)bien;
            calificacionJTField.setText(inmueble.getCalificacion()!=null?inmueble.getCalificacion():"");
            try{fechaObraJTField.setText(Constantes.df.format(inmueble.getFechaObra()));}catch(Exception e){}
            if (inmueble.getEdificabilidad()!=null && inmueble.getEdificabilidad()!=-1)
                try{edificabilidadJNField.setNumber(new Double(inmueble.getEdificabilidad()));}catch(Exception e){}
            else edificabilidadJNField.setText("");
            numPlantasJNField.setText(inmueble.getNumPlantas()!=null?inmueble.getNumPlantas():"");
            estadoConservacionEJCBox.setSelectedPatron(inmueble.getEstadoConservacion()!=null?inmueble.getEstadoConservacion():"");
            conservacionJTField.setText(inmueble.getEstadoConservacionDesc()!=null?inmueble.getEstadoConservacionDesc():"");
            tipoConstruccionEJCBox.setSelectedPatron(inmueble.getTipoConstruccion()!=null?inmueble.getTipoConstruccion():"");
            construccionJTField.setText(inmueble.getTipoConstruccionDesc()!=null?inmueble.getTipoConstruccionDesc():"");
            fachadaEJCBox.setSelectedPatron(inmueble.getFachada()!=null?inmueble.getFachada():"");
            fachadaJTField.setText(inmueble.getFachadaDesc()!=null?inmueble.getFachadaDesc():"");
            cubiertaEJCBox.setSelectedPatron(inmueble.getCubierta()!=null?inmueble.getCubierta():"");
            cubiertaJTField.setText(inmueble.getCubiertaDesc()!=null?inmueble.getCubiertaDesc():"");
            carpinteriaExteriorEJCBox.setSelectedPatron(inmueble.getCarpinteria()!=null?inmueble.getCarpinteria():"");
            carpinteriaJTField.setText(inmueble.getCarpinteriaDesc()!=null?inmueble.getCarpinteriaDesc():"");
            descripcionJTField.setText(inmueble.getEdificabilidadDesc()!=null?inmueble.getEdificabilidadDesc():"");
            bienInmuebleJCBox.setSelected(inmueble.isBic());
            bienCatalogadoJCBox.setSelected(inmueble.isCatalogado());

        }

    }

    public void clear(){
        calificacionJTField.setText("");
        fechaObraJTField.setText("");
        edificabilidadJNField.setText("");
        numPlantasJNField.setText("");
        estadoConservacionEJCBox.setSelectedPatron(null);
        tipoConstruccionEJCBox.setSelectedPatron(null);
        fachadaEJCBox.setSelectedPatron(null);
        cubiertaEJCBox.setSelectedPatron(null);
        carpinteriaExteriorEJCBox.setSelectedPatron(null);
        conservacionJTField.setText("");
        construccionJTField.setText("");
        cubiertaJTField.setText("");
        carpinteriaJTField.setText("");
        fachadaJTField.setText("");
        descripcionJTField.setText("");
        bienInmuebleJCBox.setSelected(false);
        bienCatalogadoJCBox.setSelected(false);
    }



    public void setEnabled(boolean b){
        calificacionJTField.setEnabled(b);
        fechaObraJTField.setEnabled(false);
        edificabilidadJNField.setEnabled(b);
        numPlantasJNField.setEnabled(b);
        estadoConservacionEJCBox.setEnabled(b);
        tipoConstruccionEJCBox.setEnabled(b);
        fachadaEJCBox.setEnabled(b);
        cubiertaEJCBox.setEnabled(b);
        carpinteriaExteriorEJCBox.setEnabled(b);
        fObraJButton.setEnabled(b);

        conservacionJButton.setEnabled(b);
        construccionJButton.setEnabled(b);
        fachadaJButton.setEnabled(b);
        cubiertaJButton.setEnabled(b);
        carpinteriaJButton.setEnabled(b);
        descripcionJTField.setEnabled(b);

        conservacionJTField.setEnabled(false);
        construccionJTField.setEnabled(false);
        fachadaJTField.setEnabled(false);
        cubiertaJTField.setEnabled(false);
        carpinteriaJTField.setEnabled(false);
        
        bienInmuebleJCBox.setEnabled(b);
        bienCatalogadoJCBox.setEnabled(b);

    }


    private void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosEdificabilidad.tag1")));}catch(Exception e){}
        try{calificacionJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag2"));}catch(Exception e){}
        try{fechaObraJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag3"));}catch(Exception e){}
        try{edificabilidadJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag4"));}catch(Exception e){}
        try{numPlantasJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag5"));}catch(Exception e){}
        try{estadoConservacionJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag6"));}catch(Exception e){}
        try{tipoConstruccionJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag7"));}catch(Exception e){}
        try{fachadaJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag8"));}catch(Exception e){}
        try{cubiertaJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag9"));}catch(Exception e){}
        try{carpinteriaExteriorJLabel.setText(aplicacion.getI18nString("inventario.datosEdificabilidad.tag10"));}catch(Exception e){}
        try{descripcionJLabel.setText("Descripción:");}catch(Exception e){}
        try{bienInmuebleJCBox.setText("Bien de Interés Cultural (BIC)");}catch(Exception e){}
        try{bienCatalogadoJCBox.setText("Bien Catalogado (que no es BIC)");}catch(Exception e){}
        
    }


    private javax.swing.JLabel calificacionJLabel;
    private javax.swing.JTextField calificacionJTField;
    private javax.swing.JLabel fechaObraJLabel;
    private JFormattedTextField fechaObraJTField;
    private com.geopista.app.utilidades.JNumberTextField edificabilidadJNField;
    private javax.swing.JLabel edificabilidadJLabel;
    private javax.swing.JLabel numPlantasJLabel;
    private JTextField numPlantasJNField;
    private javax.swing.JLabel estadoConservacionJLabel;
    private ComboBoxEstructuras estadoConservacionEJCBox;
    private javax.swing.JLabel tipoConstruccionJLabel;
    private ComboBoxEstructuras tipoConstruccionEJCBox;
    private javax.swing.JLabel fachadaJLabel;
    private ComboBoxEstructuras fachadaEJCBox;
    private javax.swing.JLabel cubiertaJLabel;
    private ComboBoxEstructuras cubiertaEJCBox;
    private javax.swing.JLabel carpinteriaExteriorJLabel;
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JCheckBox bienInmuebleJCBox;
    private javax.swing.JCheckBox bienCatalogadoJCBox;
    private ComboBoxEstructuras carpinteriaExteriorEJCBox;
    private JButton fObraJButton;
    private JButton conservacionJButton;
    private JButton construccionJButton;
    private JButton fachadaJButton;
    private JButton cubiertaJButton;
    private JButton carpinteriaJButton;
    private javax.swing.JTextField conservacionJTField;
    private javax.swing.JTextField construccionJTField;
    private javax.swing.JTextField fachadaJTField;
    private javax.swing.JTextField cubiertaJTField;
    private javax.swing.JTextField carpinteriaJTField;
    private javax.swing.JTextField descripcionJTField;

}
