package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableColumn;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.app.cementerios.PlazasUnidadTableModel;
import com.geopista.app.cementerios.RenderComun;
import com.geopista.app.cementerios.ElemJTableModel;
import com.geopista.app.cementerios.utils.Validation;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.protocol.cementerios.UnidadSimple;
import com.geopista.util.ApplicationContext;

/**
 * ConcesionJDialog
 *
 */
public class ConcesionJDialog extends javax.swing.JDialog implements PropertyChangeListener {
	
	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(ConcesionJDialog.class);
	
    
    public static final int ESTADO_ACTIVO= 1;
    public static final String ESTADO_ACTIVO_STR= "Activo";

    private String operacion;
	private String tipo;
	private Vector listaSimple;
	private ArrayList listaTarifas;
    private ApplicationContext aplicacion;
    
	private ConcesionBean concesion;
    
	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;

    private ArrayList actionListeners= new ArrayList();
    private String locale;
    
	//El patron 2 equivale a un alquiler
	private int patronConcesion = 2;
	
    private JList jList;
	private UnidadEnterramientoBean unidadSelected;
	
	/**Tarifas**/
    private ArrayList<TarifaBean> listaTarifasTabla;
    private ElemJTableModel tarifasJTableModel;
    private TableSorted tableSorted;
    private int selectedRow= -1;
	public static final String DOBLE_CLICK="DOBLE_CLICK";

	private TarifaBean tarifaSelected;
//	private Vector vDomainTipoConcesiones;

    
	
   private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	
    /** Creates new form unidadEnterramientoJFrame */
    public ConcesionJDialog (JFrame desktop, String locale, String operacion,String tipo, Vector listasimple, ArrayList listaTarifas) throws Exception{
    	super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        this.operacion= operacion;
        this.tipo=tipo;
        this.listaSimple= listasimple;
        this.listaTarifas = listaTarifas;
        inicializar();
    }

    public ConcesionJDialog(JFrame desktop, String locale) throws Exception{
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
        
        concesionJPanel = new javax.swing.JPanel();
        
        //Panel de datos generales
        datosGeneralesComunesJPanel = new javax.swing.JPanel();
        entidadJLabel = new javax.swing.JLabel();
        cementerioJLabel = new javax.swing.JLabel();
        entidadJTextField = new javax.swing.JTextField();
        cementerioJTextField = new javax.swing.JTextField();

    	//Datos Titular
    	datosTitularJPanel = new javax.swing.JPanel();
        DNIJLabel = new javax.swing.JLabel();
        DNIJTextField = new javax.swing.JTextField();
        nombreJLabel = new javax.swing.JLabel();
        nombreJTextField = new javax.swing.JTextField();
        sexolJLabel = new javax.swing.JLabel();
        apellidosJLabel = new javax.swing.JLabel();
        apellido1JTextField = new javax.swing.JTextField();
        domicilioPostalJLabel = new javax.swing.JLabel();
        domicilioPostalJTextField = new javax.swing.JTextField();
        apellido2JLabel = new javax.swing.JLabel();
        apellido2JTextField = new javax.swing.JTextField();
        estadoCivilJLabel = new javax.swing.JLabel();
        
        mujerJRadioButton = new javax.swing.JRadioButton();
        hombreJRadioButton = new javax.swing.JRadioButton();
        solteroJRadioButton = new javax.swing.JRadioButton();
        casadoJRadioButton = new javax.swing.JRadioButton();

        ButtonGroup groupSexo = new ButtonGroup();
        groupSexo.add(mujerJRadioButton);
        groupSexo.add(hombreJRadioButton);
        
        ButtonGroup groupEstadocivil = new ButtonGroup();
        groupEstadocivil.add(solteroJRadioButton);
        groupEstadocivil.add(casadoJRadioButton);
        
        fechaNacimientoJLabel = new javax.swing.JLabel();
        try {
			fechaNacimientoJDateChooser = new JDateChooser(fecha.parse("01/01/2000"));
		} catch (ParseException e) {
			logger.error("Error estableciendo fechaNacimiento por defecto " + e);
			e.printStackTrace();
		}
        fechaNacimientoJDateChooser.setDateFormatString("dd/MM/yyyy");
        
        telefonoJLabel = new javax.swing.JLabel();
        telefonoJTextField = new javax.swing.JTextField();
        telefonoJTextField.setText("");
        
        poblacionJLabel = new javax.swing.JLabel();
        poblacionJTextField = new javax.swing.JTextField();
        poblacionJTextField.setText("");
        
        //datos de concesion
        datosConcesionJPanel = new javax.swing.JPanel();
        locallizadoJLabel = new javax.swing.JLabel();
        localizadoJTextField = new javax.swing.JTextField();
        tipoconcesionJComboBox = new javax.swing.JComboBox();
        tipoConcesionJLabel = new javax.swing.JLabel();
        codigoJLabel = new javax.swing.JLabel();
        codigoJTextField = new javax.swing.JTextField();
        codigoJTextField.setText("");
        
        fechaInicioJLabel = new javax.swing.JLabel();
        fechaInicioJDateChooser =  new JDateChooser(new Date());
        fechaInicioJDateChooser.setDateFormatString("dd/MM/yyyy");

        fechaFinJLabel = new javax.swing.JLabel();
        fechaFinJDateChooser = new JDateChooser(new Date());
        fechaFinJDateChooser.setDateFormatString("dd/MM/yyyy");

        fechaUltRenoJLabel = new javax.swing.JLabel();
        fechaUltRenoJDateChooser = new JDateChooser(new Date());
        fechaUltRenoJDateChooser.setDateFormatString("dd/MM/yyyy");
        
        estadoJLabel = new javax.swing.JLabel();
        estadoJTextField = new javax.swing.JTextField();
    	estadoJTextField.setText(Const.EstActivaStr);
    	estadoJTextField.setEditable(false);
        
        descripcionJLabel = new javax.swing.JLabel();
        descripcionJTextField = new javax.swing.JTextField();
        descripcionJTextField.setText(" ");
        
        //datos de tarifa
        datostarifaJPanel = new javax.swing.JPanel();
        bonificacionJLabel = new javax.swing.JLabel();
        bonificaionJTextField = new javax.swing.JTextField();
        precioJLabel = new javax.swing.JLabel();
        precioJTextField = new javax.swing.JTextField();
        
        //3 panel
        tarifasJPanel = new javax.swing.JPanel();
        tarifasJScrollPane = new javax.swing.JScrollPane();
        tarifasJTable = new javax.swing.JTable();
        
        columnaJLabel = new javax.swing.JLabel();
        columnaJTextField = new javax.swing.JTextField();
        filaJLabel = new javax.swing.JLabel();
        filaJTextField = new javax.swing.JTextField();
        tipoUnidadJComboBox = new javax.swing.JComboBox();
        tipoUnidadJLabel = new javax.swing.JLabel();
        unidadEnterramientoJPanel = new javax.swing.JPanel();
        
        
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
          
        concesionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
   
        datosGeneralesComunesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosGeneralesComunesJPanel.setPreferredSize(new Dimension(760, 70));
        datosGeneralesComunesJPanel.setMinimumSize(new Dimension(760, 70));

        
        entidadJLabel.setText(aplicacion.getI18nString("cementerio.datosGenerales.tag2"));
        datosGeneralesComunesJPanel.add(entidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cementerioJLabel.setText(aplicacion.getI18nString("cementerio.datosGenerales.tag3"));
        datosGeneralesComunesJPanel.add(cementerioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        
        //marco el borde
        datosGeneralesComunesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosGenerales.tag1")));


        entidadJTextField.setText("");
        datosGeneralesComunesJPanel.add(entidadJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 780, -1));
        cementerioJTextField.setText("");
        datosGeneralesComunesJPanel.add(cementerioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 780, -1));
        concesionJPanel.add(datosGeneralesComunesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 880, 70));

        datosTitularJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(aplicacion.getI18nString("cementerio.datosTitular.tag1")));
        datosTitularJPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        mujerJRadioButton.setText("Mujer");
        mujerJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mujerJCheckBoxActionPerformed(evt);
            }
        });

        hombreJRadioButton.setText("Hombre");
        hombreJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hombreJCheckBoxActionPerformed(evt);
            }
        });
        estadoCivilJLabel.setText("EstadoCivil:");

        solteroJRadioButton.setText("Soltero");
        solteroJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solteroJCheckBoxActionPerformed(evt);
            }
        });

        casadoJRadioButton.setText("Casado");
        casadoJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                casadoJCheckBoxActionPerformed(evt);
            }
        });

        
        datosTitularJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DNIJLabel.setText("DNI/NIF");
        datosTitularJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 24, -1, -1));
        datosTitularJPanel.add(DNIJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 21, 100, -1));

        nombreJLabel.setText("Nombre");
        datosTitularJPanel.add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 24, -1, -1));
        nombreJTextField.setText("");
        datosTitularJPanel.add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 21, 138, -1));

        sexolJLabel.setText("Sexo: ");
        datosTitularJPanel.add(sexolJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 55, -1, -1));

        apellidosJLabel.setText("Apellido1");
        apellido1JTextField.setText("");
        
        datosTitularJPanel.add(apellidosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 24, -1, -1));
        datosTitularJPanel.add(apellido1JTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 21, 158, -1));
        
        domicilioPostalJLabel.setText("Domicilio ");
        datosTitularJPanel.add(domicilioPostalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 81, -1, -1));
        domicilioPostalJTextField.setText(" ");
        datosTitularJPanel.add(domicilioPostalJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 78, 424, -1));
        
        apellido2JLabel.setText("Apellido2");
        apellido2JTextField.setText("");
        datosTitularJPanel.add(apellido2JLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(671, 24, -1, -1));
        datosTitularJPanel.add(apellido2JTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(724, 21, 127, -1));

        mujerJRadioButton.setText("Mujer");
        mujerJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mujerJCheckBoxActionPerformed(evt);
            }
        });
        datosTitularJPanel.add(mujerJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 51, -1, -1));

        hombreJRadioButton.setText("Hombre");
        datosTitularJPanel.add(hombreJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 51, -1, -1));

        estadoCivilJLabel.setText("EstadoCivil:");
        datosTitularJPanel.add(estadoCivilJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 55, -1, -1));

        solteroJRadioButton.setText("Soltero");
        solteroJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solteroJCheckBoxActionPerformed(evt);
            }
        });
        datosTitularJPanel.add(solteroJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 51, -1, -1));

        casadoJRadioButton.setText("Casado");
        datosTitularJPanel.add(casadoJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 51, -1, -1));

        fechaNacimientoJLabel.setText("Fecha Nacimiento");
        datosTitularJPanel.add(fechaNacimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 55, -1, -1));
        datosTitularJPanel.add(fechaNacimientoJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 52, 158, -1));

        telefonoJLabel.setText("Telefono");
        datosTitularJPanel.add(telefonoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(671, 55, -1, -1));
        datosTitularJPanel.add(telefonoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(724, 52, 127, -1));

        poblacionJLabel.setText("Población");
        datosTitularJPanel.add(poblacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 81, -1, -1));
        datosTitularJPanel.add(poblacionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 78, 298, -1));

        concesionJPanel.add(datosTitularJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 880, 120));
        datosConcesionJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(aplicacion.getI18nString("cementerio.datosConcesion.tag1")));
        datosConcesionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        //cargarmos las estructuras..
        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        Vector vDomainTipoUnidades = Estructuras.getListaComboConcesiones(locale);
        tipoconcesionJComboBox = new JComboBox(vDomainTipoUnidades);
        for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
  		DomainNode node = (DomainNode) vDomainTipoUnidades.get(i);
	  		if (node.getPatron().equalsIgnoreCase(String.valueOf(getPatronConcesion()))){
	  			tipoconcesionJComboBox.setSelectedIndex(i);
	  		}
        }
        
        tipoconcesionJComboBox.addActionListener(new java.awt.event.ActionListener(){
  		public void actionPerformed(ActionEvent e){
  			JComboBox cb = (JComboBox)e.getSource();
  			DomainNode dNode= (DomainNode) cb.getSelectedItem();
  			 int patron = Integer.parseInt(dNode.getPatron());
  			 if (patron != 0){
  				 setPatronConcesion(patron);
  			  	  ArrayList lista = getTarifasByConcesionAndUnidad();
  			  	  if (lista != null){
  			  		try {
						loadListaTarifas(lista);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
  			  	  }
  			 }
  		}
  	});

        
        
        datosConcesionJPanel.add(tipoconcesionJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 48, 603, -1));

        tipoConcesionJLabel.setText("Tipo");
        datosConcesionJPanel.add(tipoConcesionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 51, -1, -1));

        codigoJLabel.setText("Código");
        datosConcesionJPanel.add(codigoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(697, 51, -1, -1));
        datosConcesionJPanel.add(codigoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 48, 125, -1));

        fechaInicioJLabel.setText("Fecha Inicio");
        datosConcesionJPanel.add(fechaInicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 82, -1, -1));
        datosConcesionJPanel.add(fechaInicioJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 79, 123, -1));

        fechaFinJLabel.setText("Fecha Fin");
        datosConcesionJPanel.add(fechaFinJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 82, -1, -1));
        datosConcesionJPanel.add(fechaFinJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 79, 136, -1));

        fechaUltRenoJLabel.setText("Fecha Última Renovación");
        datosConcesionJPanel.add(fechaUltRenoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 82, -1, -1));
        datosConcesionJPanel.add(fechaUltRenoJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(543, 79, 136, -1));

        estadoJLabel.setText("Estado");
        datosConcesionJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(697, 82, -1, -1));
        datosConcesionJPanel.add(estadoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 79, 125, -1));

        descripcionJLabel.setText("Descripción");
        datosConcesionJPanel.add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 22, -1, -1));
        datosConcesionJPanel.add(descripcionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 19, 786, -1));

      	//(10, 220, 880, 150));
        concesionJPanel.add(datosConcesionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 880, 120));

        /**Panel Unidad Enterramiento**/
        
        unidadEnterramientoJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(aplicacion.getI18nString("cementerio.datosUnidad.tag1")));
        unidadEnterramientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tipoUnidadJLabel.setText("Unidades de Enterramiento");
        unidadEnterramientoJPanel.add(tipoUnidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 22, -1, -1));


        //cargarmos las estructuras..
        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        Vector vectorDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
        
        for (int i = 0; i < vectorDomainTipoUnidades.size(); i++) {
        	DomainNode node = (DomainNode) vectorDomainTipoUnidades.get(i);
        	 for (int j = 0; j < listaSimple.size(); j++) {
        		 if (node.getPatron().equalsIgnoreCase(String.valueOf(((UnidadEnterramientoBean)listaSimple.get(j)).getTipo_unidad()))){
        		  	  String nodeterm = node.getTerm(locale);
        			  ((UnidadEnterramientoBean)listaSimple.get(j)).setDescripcion(j+1+"- Unidad: " + nodeterm);
        		 }
        	 }
        }
        
        if (listaSimple.size()> 0){
        		setUnidadSelected((UnidadEnterramientoBean)listaSimple.get(0));
        }

        tipoUnidadJComboBox = new JComboBox(listaSimple);
        tipoUnidadJComboBox.setRenderer(new RenderComun(locale));
        
        tipoUnidadJComboBox.addActionListener(new java.awt.event.ActionListener(){
  		public void actionPerformed(ActionEvent e){
  			JComboBox cb = (JComboBox)e.getSource();
  			UnidadEnterramientoBean unidadSimple = (UnidadEnterramientoBean)cb.getSelectedItem();
  			filaJTextField.setText(String.valueOf(unidadSimple.getFila()));
  			filaJTextField.setEditable(false);
  			columnaJTextField.setText(String.valueOf(unidadSimple.getColumna()));
  			columnaJTextField.setEditable(false);
  				 setUnidadSelected(unidadSimple);
  				 concesion.setUnidad(unidadSimple);
  			 }
  		});
        
        //(210, 19, 179, -1) -- (210, 19, 550, -1)
        unidadEnterramientoJPanel.add(tipoUnidadJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 19, 179, -1));

        filaJLabel.setText("Fila de Unidad");
        unidadEnterramientoJPanel.add(filaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 22, -1, -1));

        filaJTextField.setText(String.valueOf(getUnidadSelected().getFila()));
        unidadEnterramientoJPanel.add(filaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 19, 77, -1));

        columnaJLabel.setText("Columna de Unidad");
        unidadEnterramientoJPanel.add(columnaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 22, -1, -1));

        columnaJTextField.setText(String.valueOf(getUnidadSelected().getColumna()));
        unidadEnterramientoJPanel.add(columnaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(716, 19, 76, -1));

        //(10, 380, 880, 50)
        concesionJPanel.add(unidadEnterramientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints (10, 350, 880, 55));

        
        //PANEL TARIFAS
        listaTarifasTabla = new ArrayList<TarifaBean>();
        
        tarifasJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tarifas"));
        tarifasJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tarifasJTable = new ElemTableRender(6); 
        tarifasJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        initHeadersJTable();
        
        /* Ordenacion de la tabla */
        tableSorted= new TableSorted(tarifasJTableModel);
        tarifasJTableModel.setTableSorted(tableSorted);
        tableSorted.setTableHeader(tarifasJTable.getTableHeader());
        tarifasJTable.setModel(tableSorted);
        tarifasJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tarifasJTable.setCellSelectionEnabled(false);
        tarifasJTable.setColumnSelectionAllowed(false);
        tarifasJTable.setRowSelectionAllowed(true);
        tarifasJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tarifasJTable.getTableHeader().setReorderingAllowed(false);
        
        setInvisible(tarifasJTableModel.getColumnCount()-1,tarifasJTable);
        tarifasJTableModel.setTable(tarifasJTable);
        
        ArrayList lista = getTarifasByConcesionAndUnidad();
        if (lista != null){
        	tarifasJTableModel.setModelData(lista);      	
  	  	}
        
        tarifasJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	elemTarifaJTableMouseReleased();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt){
            	if(evt.getClickCount() == 2) {
            		getElemSeleccionado();
            		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                        ActionListener l = (ActionListener) i.next();
                        l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                    }
                }
            }
        });

        tarifasJScrollPane.setViewportView(tarifasJTable);
        
        
        tarifasJScrollPane.setViewportView(tarifasJTable); 
        tarifasJPanel.add(tarifasJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 860, 90));  //10, 10, 860, 100
        
        concesionJPanel.add(tarifasJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 880, 120));       
        
        /**datos de tarifa propios**/
        datostarifaJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(aplicacion.getI18nString("cementerio.datosTarifa.tag1")));
        datostarifaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bonificacionJLabel.setText("Bonificacion");
        bonificaionJTextField.setText("0");
        datostarifaJPanel.add(bonificacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 22, -1, -1));
        datostarifaJPanel.add(bonificaionJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 22, 160, -1));

        precioJLabel.setText("Precio");
        precioJTextField.setText("0");
        datostarifaJPanel.add(precioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 22, -1, -1));
        datostarifaJPanel.add(precioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 22, 160, -1));
        //(10, 350, 880, 80)
        concesionJPanel.add(datostarifaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 535, 880, 60));

        getContentPane().add(concesionJPanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(botoneraAceptarCancelarJPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }

    
    /**
     * Método que hace un columna de la tabla no visible
     */
    private void setInvisible(int column, JTable jTable){
        /** columna hidden no visible */
        TableColumn col= jTable.getColumnModel().getColumn(column);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);
    }
    
    public Vector listUnidadSimpleToString(Vector listaSimpleUnidad){
    	
    	Vector listaFinal = new Vector();
    	
    	for (int i = 0; i < listaSimpleUnidad.size(); i++) {
			UnidadSimple unidadSimple = (UnidadSimple) listaSimpleUnidad.get(i);
	    	listaFinal.add(unidadSimple.getDescripcion());
		}
    	return listaFinal;
    }
    
    private void elemTarifaJTableMouseReleased() {
        Object obj = getElemSeleccionado();
        if (obj instanceof TarifaBean){
        	setTarifaSelected((TarifaBean) getElemSeleccionado());
        }
    }
    
    private void initHeadersJTable(){
	   	this.tarifasJTableModel= new ElemJTableModel(new String[]{"Categoria", "Tipo calculo", "Concepto", "Precio", "Estado", "HIDDEN"},
	   																	new boolean[]{false, false, false, false, false, false}, locale);		
	                                                               
	   }
    
    public Object getElemSeleccionado(){
        
    	selectedRow= tarifasJTable.getSelectedRow();
	    if (selectedRow == -1){ 
	        	return null;
        }else{
        	tarifasJTableModel.setTableSorted(tarifasJTableModel.getTableSorted());
        	tarifasJTableModel.setRows(tarifasJTableModel.getRows());
        	return tarifasJTableModel.getObjetAt(selectedRow);
        }
    }
    
    
    private void mujerJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (mujerJRadioButton.isSelected()){
        	PersonaBean titular;
        	if (concesion.getTitular() != null){
        		titular = concesion.getTitular();
        	}else{
        		titular = new PersonaBean();
        	}
        	titular.setSexo(Constantes.SEXO_MUJER);
        	concesion.setTitular(titular);
        }
    }

    private void hombreJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (hombreJRadioButton.isSelected()){
        	PersonaBean titular;
        	if (concesion.getTitular() != null){
        		titular = concesion.getTitular();
        	}else{
        		titular = new PersonaBean();
        	}
        	titular.setSexo(Constantes.SEXO_HOMBRE);
        	concesion.setTitular(titular);
        }
    }

    private void solteroJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (solteroJRadioButton.isSelected()){
        	PersonaBean titular;
        	if (concesion.getTitular() != null){
        		titular = concesion.getTitular();
        	}else{
        		titular = new PersonaBean();
        	}
        	titular.setEstado_civil(Constantes.ESTADO_CIVIL_SOLTERO);
        	concesion.setTitular(titular);
        }
    }


    private void casadoJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (casadoJRadioButton.isSelected()){
        	PersonaBean titular;
        	if (concesion.getTitular() != null){
        		titular = concesion.getTitular();
        	}else{
        		titular = new PersonaBean();
        	}
        	titular.setEstado_civil(Constantes.ESTADO_CIVIL_CASADO);
        	concesion.setTitular(titular);
        }
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        setConcesion(null);
        fireActionPerformed();
    }
    
    public void botoneraAceptarCancelarJPanel_actionPerformed(){
    	
    	Validation validacion = Validation.getInstance();
    	if (((botoneraAceptarCancelarJPanel.aceptarPressed() && getTarifaSelected() == null)) && (getTarifasByConcesionAndUnidad().size() == 0)){
    		String message = "No hay ninguna tarifa en el sistema para esta categoría y este tipo de calculo.";
			JOptionPane.showMessageDialog(desktop, message, "Información.." ,JOptionPane.WARNING_MESSAGE);
    	}
    	else if ((botoneraAceptarCancelarJPanel.aceptarPressed() && getTarifaSelected() == null)){
    		String message = "Debe seleccionar una Tarifa";
			JOptionPane.showMessageDialog(desktop, message);
    	}
    	else if((!botoneraAceptarCancelarJPanel.aceptarPressed()) || (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) ||
	          (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false)){
	            concesion= null;
	            fireActionPerformed();
	        
    	}
    	else if ((operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR) || 
	    		(operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)) && (validacion.validateDNI(desktop, DNIJTextField.getText().trim())))){
    			 actualizarDatosConcesion(concesion);
    			 fireActionPerformed();
	    }
    	
    }


    public void actualizarDatosConcesion(ConcesionBean concesion){
        
    	if (concesion==null || concesion.getTipo() == null) return;
        
    	concesion.setEntidad(entidadJTextField.getText());
    	
    	//1.titular
    	PersonaBean titular = new PersonaBean();
    	titular.setEntidad(entidadJTextField.getText());
        
    	titular.setDNI(DNIJTextField.getText().trim());
    	titular.setNombre(nombreJTextField.getText().trim());
    	titular.setApellido1(apellido1JTextField.getText().trim());
    	titular.setApellido2(apellido2JTextField.getText().trim());
    	titular.setDomicilio(domicilioPostalJTextField.getText().trim());
    	titular.setFecha_nacimiento(fechaNacimientoJDateChooser.getDate());
		titular.setPoblacion(poblacionJTextField.getText().trim());
		titular.setTelefono(telefonoJTextField.getText().trim());
		titular.setSexo(concesion.getTitular().getSexo());
		titular.setEstado_civil(concesion.getTitular().getEstado_civil());

        concesion.setTitular(titular);
        
        concesion.setBonificacion(bonificaionJTextField.getText().trim());
        concesion.setPrecio_final(precioJTextField.getText().trim());
        
        //2.tarifa
        TarifaBean tarifa = new TarifaBean();
        tarifa.setTipo_tarifa(Constantes.TARIFA_GPROPIEDAD);
        concesion.setTarifa(getTarifaSelected());

        //3.Concesion
        concesion.setTipo_concesion(getPatronConcesion());
    	concesion.setFecha_ini(fechaInicioJDateChooser.getDate());
    	concesion.setFecha_fin(fechaFinJDateChooser.getDate());
    	concesion.setFecha_ultRenovacion(fechaUltRenoJDateChooser.getDate());
    	concesion.setCodigo(codigoJTextField.getText().trim());
		concesion.setDescripcion(descripcionJTextField.getText().trim());
		
		//4. La unidad seleccionada
		concesion.setUnidad(unidadSelected);
		
        //un campo de texto que se pone por defecto
        if 	(concesionEnFecha(concesion.getFecha_fin())){
        	concesion.setEstado(Const.Estado_Activa);
        	estadoJTextField.setText(Const.EstActivaStr);
        }else{
        	concesion.setEstado(Const.Estado_Caducada);
        	estadoJTextField.setText(Const.EstCaducadaStr);
        }
        
        estadoJTextField.setEditable(false);
		
		//Por ultimo hago el set
		setConcesion(concesion);
		
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
    public void loadConcesion (ConcesionBean concesion, boolean editable){
    	
		if (concesion == null)
			return;

		//recupero los datos del titular
		PersonaBean titular = concesion.getTitular();
		if (titular != null) loadDatosTitular (titular, editable);
	    
	    //cargarmos las estructuras..para obtener el tipo de concesion 
	      while (!Estructuras.isCargada()){
	          if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
	          try {Thread.sleep(500);}catch(Exception e){}
	      }

	      Vector vDomainTipoUnidades = Estructuras.getListaComboConcesiones(locale);
	      for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
			DomainNode node = (DomainNode) vDomainTipoUnidades.get(i);
			if (node.getPatron().equalsIgnoreCase(String.valueOf(concesion.getTipo_concesion()))){
				tipoconcesionJComboBox.setSelectedIndex(i);
			}
	      }
  
	    tipoconcesionJComboBox.setEnabled(editable);

		fechaInicioJDateChooser.setDate(concesion.getFecha_ini() != null ? concesion.getFecha_ini() : new Date());
		fechaInicioJDateChooser.setEnabled(editable);
		
		fechaFinJDateChooser.setDate(concesion.getFecha_fin()!= null ? concesion.getFecha_fin() : new Date());
		fechaFinJDateChooser.setEnabled(editable);
		
		fechaUltRenoJDateChooser.setDate(concesion.getFecha_ultRenovacion() != null ? concesion.getFecha_ultRenovacion() : new Date());
		fechaUltRenoJDateChooser.setEnabled(editable);
		
		descripcionJTextField.setText(concesion.getDescripcion()!= null ? concesion.getDescripcion() : "");
		descripcionJTextField.setEditable(editable);
		
		localizadoJTextField.setText(concesion.getLocalizacion()!= null ? concesion.getLocalizacion() : "");
		localizadoJTextField.setEditable(editable);
		codigoJTextField.setText(concesion.getCodigo()!= null ? concesion.getCodigo() : "");
		codigoJTextField.setEditable(editable);
		
        Vector vectorDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
        for (int i = 0; i < vectorDomainTipoUnidades.size(); i++) {
        	DomainNode node = (DomainNode) vectorDomainTipoUnidades.get(i);
        	 for (int j = 0; j < listaSimple.size(); j++) {
        		 if (node.getPatron().equalsIgnoreCase(String.valueOf(concesion.getUnidad().getTipo_unidad()))){
        			 tipoUnidadJComboBox.setSelectedIndex(j);
        		 }
        	 }
        }
        tipoUnidadJComboBox.setEnabled(editable);
        filaJTextField.setText(concesion.getUnidad().getFila()!= 0 ? String.valueOf(concesion.getUnidad().getFila()) : "0");
        filaJTextField.setEditable(false);

        columnaJTextField.setText(concesion.getUnidad().getColumna()!= 0 ? String.valueOf(concesion.getUnidad().getColumna()) : "0");
        columnaJTextField.setEditable(false);
        
	     try {
		    clearTable();
			loadListaTarifas(getTarifasByConcesionAndUnidad());
			} catch (Exception e) {
				 logger.error("loadListaTarifas : "+ e.getMessage());
				e.printStackTrace();
			}
		
		TarifaBean tarifa = concesion.getTarifa();
		if (tarifa != null) loadDatosTarifa (tarifa, editable);

        bonificaionJTextField.setText(concesion.getBonificacion()!= null ? concesion.getBonificacion() : "0");
        bonificaionJTextField.setEditable(editable);
        
        precioJTextField.setText(concesion.getPrecio_final()!= null ? concesion.getPrecio_final() : "0");
        precioJTextField.setEditable(editable);
        
        //un campo de texto que se pone por defecto
        if 	(concesionEnFecha(concesion.getFecha_fin())){
        	concesion.setEstado(Const.Estado_Activa);
        	estadoJTextField.setText(Const.EstActivaStr);
        }else{
        	concesion.setEstado(Const.Estado_Caducada);
        	estadoJTextField.setText(Const.EstCaducadaStr);
        }
        
        estadoJTextField.setEditable(false);

    }
    
    public void clearTable(){

    	tarifasJTableModel.setModelData(new ArrayList());    	
    	tarifasJTableModel.getTableSorted().sortingStatusChanged();    	

    }
    
    /**
     * loadDatosTitular
     * @param titular
     * @param editable
     */
    public void loadDatosTitular (PersonaBean titular, boolean editable){
    	
		
		DNIJTextField.setText(titular.getDNI() != null ? titular.getDNI() : "DNI");
		DNIJTextField.setEditable(editable);
		
		nombreJTextField.setText(titular.getNombre()!= null ? titular.getNombre() : "Nombre");
		nombreJTextField.setEditable(editable);
		
		apellido1JTextField.setText(titular.getApellido1()!= null ? titular.getApellido1() : " 1º Apellido");
		apellido1JTextField.setEditable(editable);
		
		apellido2JTextField.setText(titular.getApellido2()!= null ? titular.getApellido2() : " 2º Apellido");
		apellido2JTextField.setEditable(editable);
		
		if ((titular.getSexo()!= null) && (titular.getSexo().equalsIgnoreCase(Constantes.SEXO_HOMBRE))){
			hombreJRadioButton.setSelected(true);			
		}else{
			mujerJRadioButton.setSelected(true);
		}
		hombreJRadioButton.setEnabled(editable);
		mujerJRadioButton.setEnabled(editable);
		
		if ((titular.getEstado_civil()!= null) && (titular.getEstado_civil().equalsIgnoreCase(Constantes.ESTADO_CIVIL_SOLTERO))){
			solteroJRadioButton.setSelected(true);
		}else{
			casadoJRadioButton.setSelected(true);
		}
		
		solteroJRadioButton.setEnabled(editable);
		casadoJRadioButton.setEnabled(editable);
		
		fechaNacimientoJDateChooser.setDate(titular.getFecha_nacimiento() != null ? titular.getFecha_nacimiento() : new Date());
		fechaNacimientoJDateChooser.setEnabled(editable);
		
		domicilioPostalJTextField.setText(titular.getDomicilio()!= null ? titular.getDomicilio() : "");
		domicilioPostalJTextField.setEditable(editable);
		
		poblacionJTextField.setText(titular.getPoblacion()!= null ? titular.getPoblacion() : "");
		poblacionJTextField.setEditable(editable);
		
		telefonoJTextField.setText(titular.getTelefono()!= null ? titular.getTelefono() : "");
		telefonoJTextField.setEditable(editable);
		
	}
    
   /**
    * loadDatosTarifa
    * @param tarifa
    * @param editable
    */
    public void loadDatosTarifa (TarifaBean tarifa, boolean editable){
    	
    	setTarifaSelected(tarifa);
    	
	}
    
    
    
	public void load(ConcesionBean concesionElem, boolean editable, String operacion) {
		
		if (concesionElem == null)
			return;
		entidadJTextField.setText(concesionElem.getEntidad() != null ?  concesionElem.getEntidad() : "");
		cementerioJTextField.setText(concesionElem.getNombreCementerio() != null ?  concesionElem.getNombreCementerio() : "");
		
    	setConcesion(concesionElem);
        if(operacion == null) return;

        datosGeneralesComunesJPanel.setEnabled(editable);
        datosTitularJPanel.setEnabled(editable);
        datosConcesionJPanel.setEnabled(editable);
        tarifasJPanel.setEnabled(editable);
        unidadEnterramientoJPanel.setEnabled(editable);
        filaJTextField.setEditable(false);
        columnaJTextField.setEditable(false);
        
        if ((operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) || 
        		(operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)))){
        	
        	loadConcesion(concesionElem, editable);
        	
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

    private boolean concesionEnFecha(Date fechaFin){
    	boolean enFecha = true;
    	
    	if (fechaFin.before(new Date())){
    		enFecha = false;
    	}
    	return enFecha;
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
		((JDateChooser) fechaUltRenoJDateChooser).setDate(date);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			setDate((Date) evt.getNewValue());
		}
	}

    

    public ConcesionBean getConcesion() {
		return concesion;
	}

	public void setConcesion(ConcesionBean concesion) {
		this.concesion = concesion;
	}


	private javax.swing.JFrame desktop;
    //Panel general de concesion
    private javax.swing.JPanel concesionJPanel;
    
    private javax.swing.JLabel DNIJLabel;
    private javax.swing.JTextField DNIJTextField;
    private javax.swing.JLabel domicilioPostalJLabel;
    private javax.swing.JLabel nombreJLabel;
    private javax.swing.JTextField apellido1JTextField;
    private javax.swing.JLabel apellido2JLabel;
    private javax.swing.JTextField apellido2JTextField;
    private javax.swing.JLabel apellidosJLabel;
    private javax.swing.JLabel bonificacionJLabel;
    private javax.swing.JTextField bonificaionJTextField;
    private javax.swing.JLabel cementerioJLabel;
    private javax.swing.JTextField cementerioJTextField;
    private javax.swing.JLabel codigoJLabel;
    private javax.swing.JTextField codigoJTextField;
    private javax.swing.JLabel conceptoJLabel;
    private javax.swing.JTextField conceptoJTextField;
    private javax.swing.JPanel datosConcesionJPanel;
    private javax.swing.JPanel datosGeneralesComunesJPanel;
    private javax.swing.JPanel datosTitularJPanel;
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JTextField descripcionJTextField;
    private javax.swing.JTextField domicilioPostalJTextField;
    private javax.swing.JLabel entidadJLabel;
    private javax.swing.JTextField entidadJTextField;
    private javax.swing.JLabel estadoCivilJLabel;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JTextField estadoJTextField;
    private javax.swing.JLabel estadoTarifaJLabel;
    private javax.swing.JTextField estadoTarifaJTextField;
    
    private javax.swing.JLabel fechaFinJLabel;
    private JDateChooser fechaFinJDateChooser;

    private javax.swing.JLabel fechaInicioJLabel;
    private JDateChooser fechaInicioJDateChooser;

    private javax.swing.JLabel fechaUltRenoJLabel;
    private JDateChooser fechaUltRenoJDateChooser;

    private javax.swing.JLabel fechaNacimientoJLabel;
    private JDateChooser fechaNacimientoJDateChooser;

    private javax.swing.JTextField localizadoJTextField;
    private javax.swing.JLabel locallizadoJLabel;
    private javax.swing.JTextField nombreJTextField;
    private javax.swing.JLabel poblacionJLabel;
    private javax.swing.JTextField poblacionJTextField;
    private javax.swing.JLabel precioJLabel;
    private javax.swing.JTextField precioJTextField;
    private javax.swing.JLabel sexolJLabel;
    private javax.swing.JLabel telefonoJLabel;
    private javax.swing.JTextField telefonoJTextField;
    private javax.swing.JLabel tipoCalculoJLabel;
    private javax.swing.JTextField tipoCalculoJTextField;
    private javax.swing.JLabel tipoConcesionJLabel;
    private javax.swing.JComboBox tipoconcesionJComboBox;
    
    private javax.swing.JLabel columnaJLabel;
    private javax.swing.JTextField columnaJTextField;
    private javax.swing.JLabel filaJLabel;
    private javax.swing.JTextField filaJTextField;
    private javax.swing.JComboBox tipoUnidadJComboBox;
    private javax.swing.JLabel tipoUnidadJLabel;
    private javax.swing.JPanel unidadEnterramientoJPanel;
    
    private javax.swing.JRadioButton solteroJRadioButton;
    private javax.swing.JRadioButton casadoJRadioButton;
    private javax.swing.JRadioButton mujerJRadioButton;
    private javax.swing.JRadioButton hombreJRadioButton;

    
    private javax.swing.JScrollPane tarifasJScrollPane;
    private javax.swing.JTable tarifasJTable;
    private javax.swing.JPanel tarifasJPanel;

    private javax.swing.JPanel datostarifaJPanel;
    
	public int getPatronConcesion() {
		return patronConcesion;
	}

	public void setPatronConcesion(int patronConcesion) {
		this.patronConcesion = patronConcesion;
	}

	public UnidadEnterramientoBean getUnidadSelected() {
		return unidadSelected;
	}

	public void setUnidadSelected(UnidadEnterramientoBean unidadSelected) {
		this.unidadSelected = unidadSelected;
	}
	
	public TarifaBean getTarifaSelected() {
		return tarifaSelected;
	}

	public void setTarifaSelected(TarifaBean tarifaSelected) {
		this.tarifaSelected = tarifaSelected;
	}
	
    private void select(){
        int selectedRow = jList.getMinSelectionIndex();
        if(selectedRow < 0) return;
        ListModel auxList = jList.getModel();
        if (auxList.getElementAt(selectedRow) instanceof UnidadEnterramientoBean){
        	unidadSelected= (UnidadEnterramientoBean)auxList.getElementAt(selectedRow);
        }
    }
    
    public ArrayList getListaTarifas() {
		return listaTarifas;
	}

	public void setListaTarifas(ArrayList listaTarifas) {
		this.listaTarifas = listaTarifas;
	}
    
   private ArrayList getTarifasByConcesionAndUnidad (){
	   
	   ArrayList<TarifaBean> listaTarifasWithFilter = new ArrayList<TarifaBean>();
	   
	   for (int i = 0; i < listaTarifas.size(); i++) {
		TarifaBean tarifa = (TarifaBean) listaTarifas.get(i);
		if ((tarifa.getTipo_calculo() == getPatronConcesion()) && (tarifa.getTipo_unidad() == getUnidadSelected().getTipo_unidad())){
			listaTarifasWithFilter.add(tarifa);
		}
		
	}
	   return listaTarifasWithFilter;
	   
   }
   
	public void loadListaTarifas(Collection c) throws Exception{
    	int numElementos=0;
        Collection cRet= new ArrayList();
        if (c != null){
	    	Object[] arrayElems = c.toArray();
	    	int n = arrayElems.length;
	    	if (n>0){
	    		//primer eleme
	    		Object obj = arrayElems[0];
	    		if (obj instanceof TarifaBean){
	    	    	for (int i=0;i<n;i++){
	    	    		TarifaBean elem = (TarifaBean)arrayElems[i];
		    		cRet.add(elem);
		    		numElementos++;
	    	    	}
	    		}
	    }
        }
        tarifasJTableModel.setModelData(cRet);
    }
	
}
