package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;
import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.Const;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 12-jul-2006
 * Time: 15:45:06
 * To change this template use File | Settings | File Templates.
 */


public class InmuebleJDialog extends JDialog implements BienJDialog {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

Logger logger= Logger.getLogger(InmuebleJDialog.class);

    private javax.swing.JFrame desktop;
    private DatosGeneralesInmuebleJPanel datosGenerales2JPanel;
    private DatosGeneralesComunesJPanel datosGenerales1JPanel;
    private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
    private ApplicationContext aplicacion;

    private InmuebleBean inmueble;

    private ArrayList actionListeners= new ArrayList();
    private String locale;
    private javax.swing.JPanel datosGeneralesJPanel;
    private javax.swing.JTabbedPane datosInmuebleJTabbedPane;
    private DatosRegistralesJPanel datosRegistralesJPanel;
    private DatosEdificabilidadJPanel datosEdificabilidadJPanel;
    private DatosValoracionJPanel datosValoracionJPanel;
    private DatosAmortizacionJPanel datosAmortizacionJPanel;
    private DatosSegurosJPanel datosSegurosJPanel;
    private DatosFrutosJPanel datosFrutosJPanel;
    private ObservacionesJPanel observacionesJPanel;
    private MejorasJPanel mejorasJPanel;
    private ReferenciasCatastralesJPanel refCatastralesJPanel;
    private UsosFuncionalesJPanel usosFuncionalesJPanel;
    private GestionDocumentalJPanel documentosJPanel;
	
	// Para poder integrar con EIEL:
	private DatosEIELJPanel eielJPanel;

    private String operacion;

	private String tipo;

    /**
     * Método que genera el dialogo que muestra los datos de un bien inmueble
     * @param desktop
     * @param locale
     * @param operacion que se esta realizando
     */
    public InmuebleJDialog(JFrame desktop, String locale, String operacion,String tipo) throws Exception{
        super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        this.operacion= operacion;
        this.tipo=tipo;
        inicializar();
    }

    /**
     * Método que genera el dialogo que muestra los datos de un bien inmueble
     * @param desktop
     * @param locale
     */
    public InmuebleJDialog(JFrame desktop, String locale) throws Exception{
        super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        inicializar();
    }

    private void inicializar()  throws Exception{
        
    	this.aplicacion = (AppContext) AppContext.getApplicationContext();
        getContentPane().setLayout(new BorderLayout());
        renombrarComponentes();
        setModal(true);

        datosInmuebleJTabbedPane= new javax.swing.JTabbedPane();
        datosInmuebleJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        datosInmuebleJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

        datosGenerales1JPanel= new DatosGeneralesComunesJPanel(locale);
        datosGenerales2JPanel= new DatosGeneralesInmuebleJPanel(locale, tipo);

        datosGeneralesJPanel= new JPanel();
        datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));

        datosGeneralesJPanel.setLayout(new BorderLayout());
        datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
        datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab1"), datosGeneralesJPanel);

		// Para poder integrar con EIEL:
		eielJPanel = new DatosEIELJPanel(locale);
		datosInmuebleJTabbedPane.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab13"),
				eielJPanel);
        
        datosRegistralesJPanel= new DatosRegistralesJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab2"), datosRegistralesJPanel);

        datosEdificabilidadJPanel= new DatosEdificabilidadJPanel(desktop, locale);
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab3"), datosEdificabilidadJPanel);

        datosValoracionJPanel= new DatosValoracionJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab4"), datosValoracionJPanel);

        //Añadmos Listener para detectar los cambios en los valores
        datosValoracionJPanel.getinmuebleValorAdquisicionJTField().addFocusListener(new ValorAdqFocusListener());
        datosGenerales2JPanel.getfAdquisicionJTField().addFocusListener(new FechaAdqFocusListener());
        
        datosAmortizacionJPanel= new DatosAmortizacionJPanel(desktop, locale);
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab5"), datosAmortizacionJPanel);

        datosSegurosJPanel= new DatosSegurosJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab6"), datosSegurosJPanel);

        datosFrutosJPanel= new DatosFrutosJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab7"), datosFrutosJPanel);

        observacionesJPanel= new ObservacionesJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab8"), observacionesJPanel);

        mejorasJPanel= new MejorasJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab9"), mejorasJPanel);

        refCatastralesJPanel= new ReferenciasCatastralesJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab10"), refCatastralesJPanel);

        usosFuncionalesJPanel= new UsosFuncionalesJPanel();
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab11"), usosFuncionalesJPanel);

        documentosJPanel= new GestionDocumentalJPanel(false);
        datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.inmuebleDialog.tab12"), documentosJPanel);
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

        getContentPane().add(datosInmuebleJTabbedPane, BorderLayout.NORTH);
        getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
        setSize(470, 700);
        //setLocation(150, 90);
        GUIUtil.centreOnWindow(this);
//        addAyudaOnline();
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
 				int indice = datosInmuebleJTabbedPane.getSelectedIndex();
 				String uriRelativa = "";
 				String tipoInmueble="";
 				if (tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS))
 					tipoInmueble="Inmuebles_Urbanos";
 				else
 					tipoInmueble="Inmuebles_Rústicos";
 				
 	 			switch (indice) {
 	 				case 0:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Generales";	break;
 	 				case 1:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Registrales";break;
 	 				case 2:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Edificabilidad";break;
 	 				case 3:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Valoraci.C3.B3n";break;
 	 				case 4:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Amortizaci.C3.B3n";break;
 	 				case 5:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Seguros";break;
 	 				case 6:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Datos_Frutos_y_Rentas";break;
 	 				case 7:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Observaciones";break;
 	 				case 8:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Mejoras";break;
 	 				case 9:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Referencias_Catastrales";break;
 	 				case 10:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Usos_funcionales";break;
 	 				case 11:
 	 					uriRelativa = "/Geocuenca:Inventario:"+tipoInmueble+"#Documentos";break;
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
     * Método que carga un bien inmueble en la ventana de dialogo
     * @param inm a cargar
     */
    public void load(InmuebleBean inm, boolean editable) throws Exception {
        
    	setInmueble(inm);
        if(operacion == null) return;
        
        if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
            datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
        }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
        }
        datosGenerales1JPanel.setEnabled(editable);
        datosGenerales2JPanel.setEnabled(editable);
        datosRegistralesJPanel.setEnabled(editable);
        datosEdificabilidadJPanel.setEnabled(editable);
        datosValoracionJPanel.setEnabled(editable);
        datosAmortizacionJPanel.setEnabled(editable);
        datosFrutosJPanel.setEnabled(editable);
        observacionesJPanel.setEnabled(editable);
        mejorasJPanel.setEnabled(editable);
        refCatastralesJPanel.setEnabled(editable);
        usosFuncionalesJPanel.setEnabled(editable);
        documentosJPanel.setEnabled(editable);

        if ((inmueble.getTipo()!=null) && (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)))
            datosGenerales2JPanel.componentesRuralesSetEnabled(false);
        else if ((inmueble.getTipo()!=null) && (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)))
            datosGenerales2JPanel.componentesUrbanosSetEnabled(false);

        datosGenerales1JPanel.load(inmueble);
        datosGenerales2JPanel.load(inmueble);
        datosRegistralesJPanel.load(inmueble.getRegistro());
        datosEdificabilidadJPanel.load(inmueble);
        datosValoracionJPanel.load(inmueble);
        datosAmortizacionJPanel.load(inmueble);
        /** cargamos el seguro */
        datosSegurosJPanel.load(inmueble);
        datosSegurosJPanel.setEnabledDatos(false);
        
        if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) 
        	datosSegurosJPanel.setEnabledBotonera(false);
        else{
            datosSegurosJPanel.addJButtonSetEnabled(true);
            datosSegurosJPanel.buscarJButtonSetEnabled(true);
            datosSegurosJPanel.editarJButtonSetEnabled(inmueble!=null && inmueble.getSeguro()!=null);
            datosSegurosJPanel.borrarJButtonSetEnabled(inmueble!=null && inmueble.getSeguro()!=null);
        }

        datosFrutosJPanel.load(inmueble);

        /** cargamos las observaciones */
        observacionesJPanel.load(inmueble);
        observacionesJPanel.setOperacion(operacion);
        
		// Para poder integrar con EIEL:
		/** cargamos los datos EIEL */
		eielJPanel.load(inmueble);
		eielJPanel.setEnabled(false);	
		
        mejorasJPanel.load(inmueble);
        mejorasJPanel.setOperacion(operacion);
        refCatastralesJPanel.load(inmueble);
        refCatastralesJPanel.setOperacion(operacion);
        usosFuncionalesJPanel.load(inmueble);
        usosFuncionalesJPanel.setOperacion(operacion);
        documentosJPanel.load(inmueble);
        
        /**añadimos los bienes revertibles*/
		if (inmueble.getBienesRevertibles()!=null && inmueble.getBienesRevertibles().size()>0){
        	 JTabbedPane auxJPanel=new JTabbedPane();
       	 datosInmuebleJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
        	 for (Iterator <BienRevertible>it=inmueble.getBienesRevertibles().iterator();it.hasNext();)
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
        if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
           (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false))
            inmueble= null;
        else{
        	logger.info("Actualizando Informacion del Bien");
            datosGenerales1JPanel.actualizarDatosGeneralesComunes(inmueble);
            datosGenerales2JPanel.actualizarDatosGenerales(inmueble);
            if (inmueble!=null) inmueble.setRegistro(datosRegistralesJPanel.actualizarDatos());
            datosEdificabilidadJPanel.actualizarDatos(inmueble);
            datosValoracionJPanel.actualizarDatos(inmueble);
            datosAmortizacionJPanel.actualizarDatos(inmueble);
            datosSegurosJPanel.actualizarDatos(inmueble);
            datosFrutosJPanel.actualizarDatos(inmueble);
            observacionesJPanel.actualizarDatos(inmueble);
            mejorasJPanel.actualizarDatos(inmueble);
            refCatastralesJPanel.actualizarDatos(inmueble);
            usosFuncionalesJPanel.actualizarDatos(inmueble);
            documentosJPanel.actualizarDatos(inmueble);
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

    public void setInmueble(InmuebleBean inmueble){
        this.inmueble= inmueble;
    }

    public InmuebleBean getInmueble(){
        return inmueble;
    }

    public void pmsChecked(){
        datosGenerales2JPanel.patrimonioChecked();
    }

    public void setRevertible(){
        datosGenerales2JPanel.setRevertible();
    }

    public GestionDocumentalJPanel getDocumentosJPanel() {
        return documentosJPanel;
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        setInmueble(null);
        fireActionPerformed();
    }

	@Override
	public BienBean getBien() {
		return inmueble;
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
				if (datosValoracionJPanel.getinmuebleValorAdquisicionJTField()
						.getText() != null
						&& !datosValoracionJPanel
								.getinmuebleValorAdquisicionJTField().getText()
								.equalsIgnoreCase("")){
					valor = datosValoracionJPanel
							.getinmuebleValorAdquisicionJTField().getNumber();
				}
				datosAmortizacionJPanel.setCosteAdquisicion((Double) valor);
				if(datosAmortizacionJPanel.getCuentaAmortizacion()!=null)
					datosAmortizacionJPanel.calcularTotalAmortizado(null,inmueble.getFechaAdquisicion(), inmueble.getValorAdquisicionInmueble(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
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
						datosAmortizacionJPanel.calcularTotalAmortizado(null,inmueble.getFechaAdquisicion(), inmueble.getValorAdquisicionInmueble(),datosAmortizacionJPanel.getCuentaAmortizacion(),datosAmortizacionJPanel.getCuentaAmortizacion().getTipoAmortizacion());
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
