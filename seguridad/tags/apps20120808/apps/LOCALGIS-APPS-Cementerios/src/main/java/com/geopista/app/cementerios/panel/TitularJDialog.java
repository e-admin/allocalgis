package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.utils.Validation;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.util.ApplicationContext;


/**
 *
 * @author yraton
 */
public class TitularJDialog extends javax.swing.JDialog {


    public static final String ESTADO_CIVIL_SOLTERO= "soltero";
    public static final String ESTADO_CIVIL_CASADO= "casado";
    public static final String SEXO_MUJER= "mujer";
    public static final String SEXO_HOMBRE= "hombre";
    
	
    private String operacion;
	private String tipo;
    private ApplicationContext aplicacion;
    
    private PersonaBean titular;
    
	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
    
    private ArrayList actionListeners= new ArrayList();
    private String locale;

   private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	
    /** Creates new form unidadEnterramientoJFrame */
    public TitularJDialog (JFrame desktop, String locale, String operacion,String tipo) throws Exception{
    	super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        this.operacion= operacion;
        this.tipo=tipo;
        inicializar();
    }

    public TitularJDialog(JFrame desktop, String locale) throws Exception{
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
        
        TitularJPanel = new javax.swing.JPanel();
        
        //Panel de datos generales
        datosGeneralesComunesJPanel = new javax.swing.JPanel();
        entidadJLabel = new javax.swing.JLabel();
        cementerioJLabel = new javax.swing.JLabel();
        entidadJTextField = new javax.swing.JTextField();
        cementerioJTextField = new javax.swing.JTextField();

        //Panel de Datos de titular
        TitularJPanel = new javax.swing.JPanel();
        datosGeneralesComunesJPanel = new javax.swing.JPanel();
        entidadJLabel = new javax.swing.JLabel();
        cementerioJLabel = new javax.swing.JLabel();
        entidadJTextField = new javax.swing.JTextField();
        cementerioJTextField = new javax.swing.JTextField();
        datosTitularJPanel = new javax.swing.JPanel();
        DNIJLabel = new javax.swing.JLabel();
        DNIJTextField = new javax.swing.JTextField();
        NombreJLabel = new javax.swing.JLabel();
        nombreJTextField = new javax.swing.JTextField();
        sexolJLabel = new javax.swing.JLabel();
        apellidosJLabel = new javax.swing.JLabel();
        apellido1JTextField = new javax.swing.JTextField();
        DomicilioPostalJLabel = new javax.swing.JLabel();
        domicilioPostalJTextField = new javax.swing.JTextField();
        apellido2JLabel = new javax.swing.JLabel();
        apellido2JTextField = new javax.swing.JTextField();
        mujerJCheckBox = new javax.swing.JCheckBox();
        hombreJCheckBox = new javax.swing.JCheckBox();
        estadoCivilJLabel = new javax.swing.JLabel();
        solteroJCheckBox = new javax.swing.JCheckBox();
        casadoJCheckBox = new javax.swing.JCheckBox();
        fechaNacimientoJLabel = new javax.swing.JLabel();
        fechaNacimientoJTextField = new javax.swing.JTextField();
        telefonoJLabel = new javax.swing.JLabel();
        telefonoJTextField = new javax.swing.JTextField();
        poblacionJLabel = new javax.swing.JLabel();
        poblacionJTextField = new javax.swing.JTextField();

        
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
        setSize(800, 500);
        
        
        TitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        datosGeneralesComunesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        datosGeneralesComunesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        entidadJLabel.setText("Entidad");
        datosGeneralesComunesJPanel.add(entidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cementerioJLabel.setText("Cementerio");
        datosGeneralesComunesJPanel.add(cementerioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        entidadJTextField.setText("entidad");
        datosGeneralesComunesJPanel.add(entidadJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 780, -1));

        cementerioJTextField.setText("cementerio");
        datosGeneralesComunesJPanel.add(cementerioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 780, -1));

        TitularJPanel.add(datosGeneralesComunesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 880, 70));

        datosTitularJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        datosTitularJPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        datosTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DNIJLabel.setText("DNI/NIF");
        datosTitularJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 24, -1, -1));

        DNIJTextField.setText("DNI");
        datosTitularJPanel.add(DNIJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 21, 100, -1));

        NombreJLabel.setText("Nombre");
        datosTitularJPanel.add(NombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 24, -1, -1));

        nombreJTextField.setText("Nombre");
        datosTitularJPanel.add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 21, 138, -1));

        sexolJLabel.setText("Sexo: ");
        datosTitularJPanel.add(sexolJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 55, -1, -1));

        apellidosJLabel.setText("Apellido1");
        datosTitularJPanel.add(apellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 24, -1, -1));
        datosTitularJPanel.add(apellido1JTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 21, 158, -1));

        DomicilioPostalJLabel.setText("Domicilio ");
        datosTitularJPanel.add(DomicilioPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 81, -1, -1));

        domicilioPostalJTextField.setText("domicilio");
        datosTitularJPanel.add(domicilioPostalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 78, 424, -1));

        apellido2JLabel.setText("Apellido2");
        datosTitularJPanel.add(apellido2JLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(671, 24, -1, -1));
        datosTitularJPanel.add(apellido2JTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(724, 21, 127, -1));

        mujerJCheckBox.setText("Mujer");
        datosTitularJPanel.add(mujerJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 51, -1, -1));

        hombreJCheckBox.setText("Hombre");
        datosTitularJPanel.add(hombreJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 51, -1, -1));

        estadoCivilJLabel.setText("EstadoCivil:");
        datosTitularJPanel.add(estadoCivilJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 55, -1, -1));

        solteroJCheckBox.setText("Soltero");
        datosTitularJPanel.add(solteroJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 51, -1, -1));

        casadoJCheckBox.setText("Casado");
        datosTitularJPanel.add(casadoJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 51, -1, -1));

        fechaNacimientoJLabel.setText("Fecha Nacimiento");
        datosTitularJPanel.add(fechaNacimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 55, -1, -1));
        datosTitularJPanel.add(fechaNacimientoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 52, 158, -1));

        telefonoJLabel.setText("Telefono");
        datosTitularJPanel.add(telefonoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(671, 55, -1, -1));
        datosTitularJPanel.add(telefonoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(724, 52, 127, -1));

        poblacionJLabel.setText("Poblacion");
        datosTitularJPanel.add(poblacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 81, 50, -1));
        datosTitularJPanel.add(poblacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 78, 290, -1));


        mujerJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mujerJCheckBoxActionPerformed(evt);
            }
        });

        hombreJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hombreJCheckBoxActionPerformed(evt);
            }
        });
        estadoCivilJLabel.setText("EstadoCivil:");

        solteroJCheckBox.setText("Soltero");
        solteroJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solteroJCheckBoxActionPerformed(evt);
            }
        });

        casadoJCheckBox.setText("Casado");
        casadoJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                casadoJCheckBoxActionPerformed(evt);
            }
        });

        
        TitularJPanel.add(datosTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 880, 120));

        getContentPane().add(TitularJPanel, java.awt.BorderLayout.LINE_END);
        getContentPane().add(botoneraAceptarCancelarJPanel, java.awt.BorderLayout.SOUTH);

        pack();

    }// </editor-fold>

    private void mujerJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (mujerJCheckBox.isSelected()){
        	titular.setSexo(SEXO_MUJER);
        }
    }

    private void hombreJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (hombreJCheckBox.isSelected()){
        	titular.setSexo(SEXO_HOMBRE);
        }
    }

    private void solteroJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (solteroJCheckBox.isSelected()){
        	titular.setEstado_civil(ESTADO_CIVIL_SOLTERO);
        }
    }


    private void casadoJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (casadoJCheckBox.isSelected()){
        	titular.setEstado_civil(ESTADO_CIVIL_CASADO);
        }
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        setTitular(null);
        fireActionPerformed();
    }
    
    public void botoneraAceptarCancelarJPanel_actionPerformed(){
    	Validation validacion = Validation.getInstance();
	        if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
	           (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false)){
	            titular= null;
	        }
		    else if ((operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR) || 
		    		(operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)) && (validacion.validateDNI(desktop, DNIJTextField.getText().trim())))){
		    	actualizarDatosTitular(titular);
		    }else{
	        	actualizarDatosTitular(titular);
	        }
	        fireActionPerformed();
    }


    public void actualizarDatosTitular(PersonaBean titular){
        
    	if (titular==null || titular.getTipo() == null) return;
        
    	titular.setEntidad(entidadJTextField.getText());
        
    	titular.setDNI(DNIJTextField.getText().trim());
    	titular.setNombre(nombreJTextField.getText().trim());
    	titular.setApellido1(apellido1JTextField.getText().trim());
    	titular.setApellido2(apellido2JTextField.getText().trim());
    	
    	titular.setDomicilio(domicilioPostalJTextField.getText().trim());
    	titular.setPoblacion(poblacionJTextField.getText().trim());
    	
    	try {
    		titular.setFecha_nacimiento(fecha.parse(fechaNacimientoJTextField.getText().trim()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
		titular.setPoblacion(poblacionJTextField.getText().trim());
		titular.setTelefono(telefonoJTextField.getText().trim());
		
    }

    
   /**
    * RenombrarComponentes 
    */
    public void renombrarComponentes(){
        try{datosGeneralesComunesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosGenerales.tag1")));}catch(Exception e){}
    }
    
   /**
    * loadDatosTitular 
    * @param titular
    * @param editable
    */
    public void loadDatosTitular (PersonaBean titular, boolean editable){
    	
		if (titular == null)
			return;
		
		DNIJTextField.setText(titular.getDNI() != null ? titular.getDNI() : "DNI");
		DNIJTextField.setEditable(editable);
		
		nombreJTextField.setText(titular.getNombre()!= null ? titular.getNombre() : "Nombre");
		nombreJTextField.setEditable(editable);
		
		apellido1JTextField.setText(titular.getApellido1()!= null ? titular.getApellido1() : " 1º Apellido");
		apellido1JTextField.setEditable(editable);
		
		apellido2JTextField.setText(titular.getApellido2()!= null ? titular.getApellido2() : " 2º Apellido");
		apellido2JTextField.setEditable(editable);
		
		if ((titular.getSexo()!= null) && (titular.getSexo().equalsIgnoreCase(SEXO_HOMBRE))){
			hombreJCheckBox.setSelected(true);			
		}else{
			mujerJCheckBox.setSelected(true);
		}
		hombreJCheckBox.setEnabled(editable);
		mujerJCheckBox.setEnabled(editable);
		
		if ((titular.getEstado_civil()!= null) && (titular.getEstado_civil().equalsIgnoreCase(ESTADO_CIVIL_SOLTERO))){
			solteroJCheckBox.setSelected(true);
		}else{
			casadoJCheckBox.setSelected(true);
		}
		
		solteroJCheckBox.setEnabled(editable);
		casadoJCheckBox.setEnabled(editable);
		
		fechaNacimientoJTextField.setText(titular.getFecha_nacimiento()!= null ? 
				fecha.format(titular.getFecha_nacimiento()) : "");
		
		fechaNacimientoJTextField.setEditable(editable);
		fechaNacimientoJTextField.setEditable(editable);
		
		domicilioPostalJTextField.setText(titular.getDomicilio()!= null ? titular.getDomicilio() : "");
		domicilioPostalJTextField.setEditable(editable);
		
		poblacionJTextField.setText(titular.getPoblacion()!= null ? titular.getPoblacion() : "");
		poblacionJTextField.setEditable(editable);
		
		telefonoJTextField.setText(titular.getTelefono()!= null ? titular.getTelefono() : "");
		telefonoJTextField.setEditable(editable);
		
	}
    
	public void load(PersonaBean titularElem, boolean editable, String operacion) {
		
		if (titularElem == null)
			return;
		cementerioJTextField.setText(titularElem.getNombreCementerio() != null ?  titularElem.getNombreCementerio() : "");
		cementerioJTextField.setEditable(editable);
		entidadJTextField.setText(titularElem.getEntidad() != null ?  titularElem.getEntidad() : "");
		entidadJTextField.setEditable(editable);
		
    	setTitular(titularElem);
        if(operacion == null) return;

        datosGeneralesComunesJPanel.setEnabled(editable);
        datosTitularJPanel.setEnabled(editable);    
        
        if ((operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) || 
        		(operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)))){
        	
        	loadDatosTitular(titularElem, editable);
        	
        	cementerioJTextField.setEditable(editable);
        	entidadJTextField.setEditable(editable);
        }
	}
	
    /**
     * Método que abre una ventana de confirmacion sobre la operacion que se esta llevando a cabo
     */
    private boolean confirmOption(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("cementerio.optionpane.tag1"), aplicacion.getI18nString("cementerio.optionpane.tag2"), JOptionPane.YES_NO_OPTION);
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

    public PersonaBean getTitular() {
		return titular;
	}

	public void setTitular(PersonaBean titular) {
		this.titular = titular;
	}

    private javax.swing.JFrame desktop;
    
    private javax.swing.JLabel DNIJLabel;
    private javax.swing.JTextField DNIJTextField;
    private javax.swing.JLabel DomicilioPostalJLabel;
    private javax.swing.JLabel NombreJLabel;
    private javax.swing.JPanel TitularJPanel;
    private javax.swing.JTextField apellido1JTextField;
    private javax.swing.JLabel apellido2JLabel;
    private javax.swing.JTextField apellido2JTextField;
    private javax.swing.JLabel apellidosJLabel;
    private javax.swing.JCheckBox casadoJCheckBox;
    private javax.swing.JLabel cementerioJLabel;
    private javax.swing.JTextField cementerioJTextField;
    private javax.swing.JPanel datosGeneralesComunesJPanel;
    private javax.swing.JPanel datosTitularJPanel;
    private javax.swing.JTextField domicilioPostalJTextField;
    private javax.swing.JLabel entidadJLabel;
    private javax.swing.JTextField entidadJTextField;
    private javax.swing.JLabel estadoCivilJLabel;
    private javax.swing.JLabel fechaNacimientoJLabel;
    private javax.swing.JTextField fechaNacimientoJTextField;
    private javax.swing.JCheckBox hombreJCheckBox;
    private javax.swing.JCheckBox mujerJCheckBox;
    private javax.swing.JTextField nombreJTextField;
    private javax.swing.JLabel poblacionJLabel;
    private javax.swing.JTextField poblacionJTextField;
    private javax.swing.JLabel sexolJLabel;
    private javax.swing.JCheckBox solteroJCheckBox;
    private javax.swing.JLabel telefonoJLabel;
    private javax.swing.JTextField telefonoJTextField;

}
