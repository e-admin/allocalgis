/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * unidadEnterramientoJFrame.java
 *
 * Created on 11-mar-2011, 14:44:38
 */

package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.app.cementerios.PlazasUnidadTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.LocalizacionBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.util.ApplicationContext;

public class UnidadEnterramientoJDialog extends javax.swing.JDialog implements PropertyChangeListener {

	private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(UnidadEnterramientoJDialog.class);
	
	private static final long serialVersionUID = 1L;
	
	private String operacion;
	private String tipo;
    private ApplicationContext aplicacion;
    
    private UnidadEnterramientoBean unidadEnterramiento;
    private ArrayList<PlazaBean> listaPlazasTabla;
	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;
	
    private PlazasUnidadTableModel plazasJTableModel;
    private TableSorted tableSorted;
    private int selectedRow= -1;
	public static final String DOBLE_CLICK="DOBLE_CLICK";

	private PlazaBean plazaSelected;
	private Vector vDomainTipoUnidades;
	private Vector vDomainTipoConcesiones;
	
	//El patron 3 equivale a sepultura o fosa termino mayor usuado
	private int patronUnidad = 1;

	private ArrayList actionListeners= new ArrayList();
    private String locale;

   private SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	
    /** Creates new unidadEnterramientoJDialog */
    public UnidadEnterramientoJDialog (JFrame desktop, String locale, String operacion,String tipo) throws Exception{
    	super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        this.operacion= operacion;
        this.tipo=tipo;
        inicializar();
    }

    /**
     * UnidadEnterramientoJDialog
     * @param desktop
     * @param locale
     * @throws Exception
     */
    public UnidadEnterramientoJDialog(JFrame desktop, String locale) throws Exception{
        super(desktop);
        this.desktop= desktop;
        this.locale= locale;
        inicializar();
    }
    
    /**
     * inicializar
     */
    private void inicializar() {

    	this.aplicacion = (AppContext) AppContext.getApplicationContext();
        getContentPane().setLayout(new BorderLayout());
        renombrarComponentes();
        setModal(true);

        desktop = new javax.swing.JFrame();
        
        UnidadEnterramientoJPanel = new javax.swing.JPanel();
        
        //Panel de datos generales
        datosGeneralesComunesJPanel = new javax.swing.JPanel();
        entidadJLabel = new javax.swing.JLabel();
        cementerioJLabel = new javax.swing.JLabel();
        entidadJTField = new javax.swing.JTextField();
        cementerioJTField = new javax.swing.JTextField();

        //Panel de Datos de unidad de enterramiento
        datosUnidadEnterramientoJPanel = new javax.swing.JPanel();
        localizacionJLabel = new javax.swing.JLabel();
        jLocalizacionField = new javax.swing.JTextField();
        filaJLabel = new javax.swing.JLabel();
        jFilaField = new javax.swing.JTextField();
        columnaJLabel = new javax.swing.JLabel();
        jColumnaField = new javax.swing.JTextField();
        codigoJLabel = new javax.swing.JLabel();
        jCodigoField = new javax.swing.JTextField();
        descripcionJLabel = new javax.swing.JLabel();
        descripcionJTField = new  javax.swing.JTextField();
        
        tipoUnidadJLabel = new javax.swing.JLabel();
        tipoUnidadJComboBox = new javax.swing.JComboBox();
        
        fechaConsJLabel = new javax.swing.JLabel();
        fechaConsJDateChooser =  new JDateChooser(new Date());
        fechaConsJDateChooser.setDateFormatString("dd/MM/yyyy");
        
        fechaReformaJLabel = new javax.swing.JLabel();
        fechaReformaJDateChooser = new JDateChooser(new Date());
        fechaReformaJDateChooser.setDateFormatString("dd/MM/yyyy");

        estadoJLabel = new javax.swing.JLabel();
        jEstadoField = new javax.swing.JTextField();

        //3 panel
        plazasJLabel = new  javax.swing.JLabel();
        plazasJSpinner = new javax.swing.JSpinner();
        plazasSpinnerModel = new SpinnerNumberModel();   

        tablaJPanel = new javax.swing.JPanel();
        plazasTabbedPane = new javax.swing.JTabbedPane();
        plazasJScrollPane = new javax.swing.JScrollPane();
        plazasJTable = new javax.swing.JTable();
        
        concesionJPanel = new javax.swing.JPanel();
        DNIJLabel = new javax.swing.JLabel();
        DNIJTextField = new javax.swing.JTextField();
        NombreJLabel = new javax.swing.JLabel();
        nombreJTextField = new javax.swing.JTextField();
        tipoConcesionJLabel = new javax.swing.JLabel();
        TipoConcesionJComboBox = new javax.swing.JComboBox();
        fechaFinJLabel = new javax.swing.JLabel();
        fechaFinJTextField = new javax.swing.JTextField();


        
        plazasJSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				plazas_actionPerformed();
			}
		});

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
      
      setSize(800, 720);
        
      UnidadEnterramientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
 
      datosGeneralesComunesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
      //Panel de datos Comunes (760,70)
      datosGeneralesComunesJPanel.setPreferredSize(new Dimension(756, 70));
      datosGeneralesComunesJPanel.setMinimumSize(new Dimension(756, 70));

      //1. entidad 
      entidadJLabel.setText(aplicacion.getI18nString("cementerio.datosGenerales.tag2"));
      datosGeneralesComunesJPanel.add(entidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 16, -1, -1));

      entidadJTField.setText(" ");
      datosGeneralesComunesJPanel.add(entidadJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 13, 652, -1));

      //2.cementerio
      cementerioJLabel.setText(aplicacion.getI18nString("cementerio.datosGenerales.tag3"));
      datosGeneralesComunesJPanel.add(cementerioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 42, -1, -1));

      cementerioJTField.setText("Cementerio Municipal ");
      datosGeneralesComunesJPanel.add(cementerioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 42, 652, -1));

      //marco el borde
      datosGeneralesComunesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosGenerales.tag1")));

      //(12, 28, -1, -1)
      UnidadEnterramientoJPanel.add(datosGeneralesComunesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 20, -1, -1));

      //Panel unidad enterramiento
      datosUnidadEnterramientoJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
      
      datosUnidadEnterramientoJPanel.setPreferredSize(new Dimension(760, 130));
      datosUnidadEnterramientoJPanel.setMinimumSize(new Dimension(760, 130));


      localizacionJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag2"));
      //AbsoluteConstraints(int x, int y, int width, int height) (12, 16, -1, -1)
      datosUnidadEnterramientoJPanel.add(localizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 26, -1, -1));

      //(100, 13, 388, -1)
      jLocalizacionField.setText(" "); 
      datosUnidadEnterramientoJPanel.add(jLocalizacionField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 23, 388, -1));
      //(506, 16, -1, -1)
      filaJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag3"));
      datosUnidadEnterramientoJPanel.add(filaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(506, 26, -1, -1));
      //(544, 13, 38, -1)
      jFilaField.setText("0");
      datosUnidadEnterramientoJPanel.add(jFilaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(544, 23, 38, -1));
      //(615, 16, -1, -1)
      columnaJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag4"));
      datosUnidadEnterramientoJPanel.add(columnaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(615, 26, -1, -1));
      //(677, 13, 38, -1)
      jColumnaField.setText("0");
      datosUnidadEnterramientoJPanel.add(jColumnaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 23, 38, -1));

      codigoJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag5"));
      datosUnidadEnterramientoJPanel.add(codigoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 55, -1, -1));

      jCodigoField.setText(" ");
      datosUnidadEnterramientoJPanel.add(jCodigoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 52, 50, -1));

      descripcionJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag6"));
      datosUnidadEnterramientoJPanel.add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 55, -1, -1));

      descripcionJTField.setText(" ");
      datosUnidadEnterramientoJPanel.add(descripcionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 52, 312, -1));

      plazasJLabel.setText("Plazas");
      datosUnidadEnterramientoJPanel.add(plazasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(615, 55, 48, -1));

      plazasJSpinner.setValue(new Integer(0));
      datosUnidadEnterramientoJPanel.add(plazasJSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 52, 40, -1 ));
      
      
      tipoUnidadJLabel.setText(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag8"));
      datosUnidadEnterramientoJPanel.add(tipoUnidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 95, -1, -1));

      //Dominios tipo unidad enterramiento
    
      //cargarmos las estructuras..
      while (!Estructuras.isCargada()){
          if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
          try {Thread.sleep(500);}catch(Exception e){}
      }

      vDomainTipoUnidades = Estructuras.getListaCombosSorted(locale);
      tipoUnidadJComboBox = new JComboBox(vDomainTipoUnidades);
      for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
		DomainNode node = (DomainNode) vDomainTipoUnidades.get(i);
		if (node.getPatron().equalsIgnoreCase(String.valueOf(getPatronUnidad()))){
			tipoUnidadJComboBox.setSelectedIndex(i);
		}
	}
      
      
      tipoUnidadJComboBox.addActionListener(new java.awt.event.ActionListener(){
		public void actionPerformed(ActionEvent e){
			JComboBox cb = (JComboBox)e.getSource();
			DomainNode dNode= (DomainNode) cb.getSelectedItem();
			 int patron = Integer.parseInt(dNode.getPatron());
			 if (patron != 0){
				 setPatronUnidad(patron);
			 }
		}
	});
      
      datosUnidadEnterramientoJPanel.add(tipoUnidadJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 112, -1));

      fechaConsJLabel.setText("F.Construccion");
      datosUnidadEnterramientoJPanel.add(fechaConsJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 95, -1, -1));
      datosUnidadEnterramientoJPanel.add(fechaConsJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 92, -1, -1));

      fechaReformaJLabel.setText("F.Reforma");
      datosUnidadEnterramientoJPanel.add(fechaReformaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 95, -1, -1));
      datosUnidadEnterramientoJPanel.add(fechaReformaJDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 92, -1, -1));//517

      estadoJLabel.setText("Estado");
      datosUnidadEnterramientoJPanel.add(estadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(615, 95, 56, -1));

      jEstadoField.setText(" ");
      jEstadoField.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              estadoActionPerformed();
          }
      });
      datosUnidadEnterramientoJPanel.add(jEstadoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(676, 92, 60, -1));

      //marco el borde
      datosUnidadEnterramientoJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosUEnterramiento.tag1")));
      UnidadEnterramientoJPanel.add(datosUnidadEnterramientoJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 90, 756, -1));

      //EL panel de concesion solo muestra los datos de la concesión asociada a la unidad de enterramiento si existe.
      concesionJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.UEnterramiento.concesion")));
      concesionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      DNIJLabel.setText(aplicacion.getI18nString("cementerio.label.dninif"));
      concesionJPanel.add(DNIJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 24, 41, -1));

      DNIJTextField.setText("");
      DNIJTextField.setEditable(false);
      concesionJPanel.add(DNIJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 21, 85, -1));

      NombreJLabel.setText(aplicacion.getI18nString("cementerio.label.nombre"));;
      concesionJPanel.add(NombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 24, 40, -1));

      nombreJTextField.setText("");
      nombreJTextField.setEditable(false);
      concesionJPanel.add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 21, 140, -1));

      tipoConcesionJLabel.setText(aplicacion.getI18nString("cementerio.label.tipoconcesion"));;
      concesionJPanel.add(tipoConcesionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 22, -1, -1));

      //cargarmos las estructuras..
      while (!Estructuras.isCargada()){
          if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
          try {Thread.sleep(500);}catch(Exception e){}
      }

      vDomainTipoConcesiones = Estructuras.getListaComboConcesiones(locale);
       
      TipoConcesionJComboBox = new JComboBox(vDomainTipoConcesiones);
      TipoConcesionJComboBox.setSelectedIndex(-1);
      TipoConcesionJComboBox.setEnabled(false);

      concesionJPanel.add(TipoConcesionJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 19, 114, -1));

      fechaFinJLabel.setText(aplicacion.getI18nString("cementerio.label.fechafin"));;
      concesionJPanel.add(fechaFinJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 22, -1, -1));

      fechaFinJTextField.setText("");
      fechaFinJTextField.setEditable(false);
      concesionJPanel.add(fechaFinJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 19, 112, -1));

      UnidadEnterramientoJPanel.add(concesionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 220, 752, 50));
      
      //PANEL PLAZAS
      listaPlazasTabla = new ArrayList<PlazaBean>();
      
      tablaJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
      tablaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      plazasJTable = new ElemTableRender(6); 
      plazasJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
      
      initHeadersJTable();
      
      /* Ordenacion de la tabla */
      tableSorted= new TableSorted(plazasJTableModel);
      plazasJTableModel.setTableSorted(tableSorted);
      tableSorted.setTableHeader(plazasJTable.getTableHeader());
      plazasJTable.setModel(tableSorted);
      plazasJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      plazasJTable.setCellSelectionEnabled(false);
      plazasJTable.setColumnSelectionAllowed(false);
      plazasJTable.setRowSelectionAllowed(true);
      plazasJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

      plazasJTable.getTableHeader().setReorderingAllowed(false);
      
      setInvisible(plazasJTableModel.getColumnCount()-1,plazasJTable);
      plazasJTableModel.setTable(plazasJTable);
      
      
      if (unidadEnterramiento!= null){
    	  ArrayList<PlazaBean> plazas = unidadEnterramiento.getPlazas();
    	  if (plazas != null){
    		  plazasJTableModel.setModelData(plazas);
    	  }
      }
      
      plazasJTable.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mouseReleased(java.awt.event.MouseEvent evt) {
          	elemPlazaJTableMouseReleased();
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

      plazasJScrollPane.setViewportView(plazasJTable);
      
      
      plazasJScrollPane.setViewportView(plazasJTable);
      plazasTabbedPane.addTab("Plazas", plazasJScrollPane);
      tablaJPanel.add(plazasTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 740, 150));

      UnidadEnterramientoJPanel.add(tablaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 280, 752, 170));
      
      getContentPane().add(UnidadEnterramientoJPanel, java.awt.BorderLayout.CENTER);
      getContentPane().add(botoneraAceptarCancelarJPanel, java.awt.BorderLayout.SOUTH);
      
      pack();
    }

    private void estadoActionPerformed() {
        if 	(!todasAsignadas(getListaPlazasTabla())){
        	unidadEnterramiento.setEstado(Const.Estado_libre);
        	jEstadoField.setText(Const.EstlibreStr);
        }else{
        	unidadEnterramiento.setEstado(Const.Estado_Completa);
        	jEstadoField.setText(Const.EstcompletaStr);
        }
        jEstadoField.setEditable(false);
    }
    
    private void initHeadersJTable(){
	   	this.plazasJTableModel= new PlazasUnidadTableModel(new String[]{"Descripcion", "Situacion", "Estado", "Modificado", "Usuario", "HIDDEN"},
	   																	new boolean[]{false, false, false, false, false, false}, locale);		
	                                                               
	   }
    
    

    private void elemPlazaJTableMouseReleased() {
        Object obj = getElemSeleccionado();
        if (obj instanceof PlazaBean){
        	setPlazaSelected((PlazaBean) getElemSeleccionado());
        }
    }

    /**
     * Método que recoge el elemento seleccionado de la tabla
     * @return el elemento seleccionado de la tabla 
     */
    public Object getElemSeleccionado(){
       
    	selectedRow= plazasJTable.getSelectedRow();
	    if (selectedRow == -1){ 
	        	return null;
        }else{
        	plazasJTableModel.setTableSorted(plazasJTableModel.getTableSorted());
        	plazasJTableModel.setRows(plazasJTableModel.getRows());
        	return plazasJTableModel.getObjetAt(selectedRow);
        }
    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        setUnidadEnterramiento(null);
        fireActionPerformed();
    }
    
    public void botoneraAceptarCancelarJPanel_actionPerformed(){
        if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
           (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false)){
            unidadEnterramiento= null;
            listaPlazasTabla = new ArrayList<PlazaBean>();
        }
        else{
        	actualizarDatosUnidadEnterramiento(unidadEnterramiento);
        }
        fireActionPerformed();
    }

    
    public void plazas_actionPerformed(){
	    //Pongo el numero de plazas que hay dadas de alta
    	if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)) {
		    plazasSpinnerModel.setMinimum(unidadEnterramiento.getNumMin_Plazas());
		    plazasJSpinner.setModel(plazasSpinnerModel);
    	}
	    
    	ArrayList<PlazaBean> listaPlazas = null;
    	int numPlazas = (Integer) plazasJSpinner.getValue(); 
    	listaPlazas= crearPlazasUnidadEnterramiento (unidadEnterramiento, numPlazas);
    	try {
			loadListaPlazas(listaPlazas);
		    estadoActionPerformed();
		} catch (Exception e) {
			logger.error("loadlistaPlazas" + e.getMessage());
		}
    }

    
    /**
     * Método que carga en la tabla una lista de elementos
     * @param c Collection de elementos a cargar
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void loadListaPlazas(Collection c) throws Exception{
    	int numElementos=0;
        Collection cRet= new ArrayList();
        if (c != null){
	    	Object[] arrayElems = c.toArray();
	    	int n = arrayElems.length;
	    	if (n>0){
	    		Object obj = arrayElems[0];
	    		if (obj instanceof PlazaBean){
	    	    	for (int i=0;i<n;i++){
	    	    		PlazaBean elem = (PlazaBean)arrayElems[i];
		    		cRet.add(elem);
		    		numElementos++;
	    	    	}
	    		}
	    }
        }
        plazasJTableModel.setModelData(cRet);
    }

    
    public void clearTable(){

    	plazasJTableModel.setModelData(new ArrayList());    	
    	plazasJTableModel.getTableSorted().sortingStatusChanged();    	

    }

    /**
     * Método que añade un elemento
     * @param obj a cargar
     */
    public void addPlazaTabla(Object obj){
    	plazasJTableModel.annadirRow(obj);
    }

    /**
     * Método que actualiza un elemento en la tabla
     * @param obj a actualizar
     */
    public void updatePlazaTabla(Object obj){
    	plazasJTableModel.actualizarRow(selectedRow, obj);
    }

    /**
     * Método que borra un elem de la tabla
     */
    public void deletePlazaTabla(Object obj){
    	plazasJTableModel.deleteRow(selectedRow, obj);
    }

    
    
    /**
     * Método que crea las plazas necesarias para la unidad de enterramiento según el numero de plazas introducido 
     * en el formulario.
     * @param unidadEnterramiento
     * @param numPlazas
     */
	public ArrayList<PlazaBean> crearPlazasUnidadEnterramiento (UnidadEnterramientoBean unidadEnterramiento, int numPlazas){
		ArrayList<PlazaBean> listaPlazas = unidadEnterramiento.getPlazas();
		
		if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){ 
			numPlazas = 0;
		}
		
		else if ((operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))){
			listaPlazas = unidadEnterramiento.getPlazas();
			if (listaPlazas == null ) listaPlazas = new ArrayList<PlazaBean>();
    	
	    	for (int i = 0; i < numPlazas; i++) {
				PlazaBean plaza = new PlazaBean();
				plaza.setIdUnidadEnterramiento((int)unidadEnterramiento.getIdUe());
				plaza.setEstado(false);
				plaza.setDescripcion("Plaza creada");
				plaza.setModificado(new Date());
				plaza.setSituacion("Sin Asignar");
				//Asigno un id temporal antes de insertarlo en bbdd
				plaza.setIdPlaza(i);
				
				listaPlazas.add(plaza);
				
				plazasJTableModel.annadirRow(plaza);
			}
		} //fin annadir
		else if ((operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR))){
			
			/**Como mucho se restan las plazas que no estan asignadas y 
			 * no se puede bajar el spiner del numero de plazas asignadas*/
			
			//El num de plazas final es numPlazas del spinner - las plazas q hbía
			//el spinner no permite bajar el munplazas al las plazas asignadas..
			numPlazas = numPlazas - unidadEnterramiento.getPlazas().size(); 
			
			/**Si el numero de plazas es negativo esque tenemos que borrar plazas!**/
			if (numPlazas <0){
				//para hacer las plazas positivas
				int numplazasBorrar = numPlazas * (-1);
				int index = listaPlazas.size()-1; 
		    	
		    	if (listaPlazas == null ) return listaPlazas;
		    	while ((index >= 0) && (numplazasBorrar >0)){
		    		PlazaBean plaza = listaPlazas.get(index);
		    		//si no esta asignada la borramos
		    		if (!plaza.isEstado()){
		    			numplazasBorrar = numplazasBorrar -1;
		    			listaPlazas.remove(index);
		    			
		    		}
		    		index --;
		    	}
			}else{
		    	listaPlazas = unidadEnterramiento.getPlazas();
		    	if (listaPlazas == null ) listaPlazas = new ArrayList<PlazaBean>();
		    	
		    	for (int i = 0; i < numPlazas; i++) {
					PlazaBean plaza = new PlazaBean();
					plaza.setIdUnidadEnterramiento((int)unidadEnterramiento.getIdUe());
					plaza.setEstado(false);
					plaza.setDescripcion("Plaza creada");
					plaza.setModificado(new Date());
					plaza.setSituacion("Sin Asignar");
					//Asigno un id temporal antes de insertarlo en bbdd
					plaza.setIdPlaza(i);
					
					listaPlazas.add(plaza);
					
					plazasJTableModel.annadirRow(plaza);
				}
			}
		}//FIN MODIFICAR
		
		setListaPlazasTabla(listaPlazas);
		return listaPlazas;
    }
    
    

    public void actualizarDatosUnidadEnterramiento(UnidadEnterramientoBean unidad){
        
    	if (unidad==null || unidad.getTipo() == null) return;
        
        unidad.setEntidad(entidadJTField.getText().trim());
        unidad.setNombreCementerio(cementerioJTField.getText().trim());
        
        unidad.setDescripcion(descripcionJTField.getText().trim());
        unidad.setColumna(Integer.parseInt(jColumnaField.getText().trim()));
        unidad.setFila(Integer.parseInt(jFilaField.getText().trim()));
        
        //un campo de texto que se pone por defecto
        if 	(!todasAsignadas(unidad.getPlazas())){
        	unidad.setEstado(Const.Estado_libre);
        	jEstadoField.setText(Const.EstlibreStr);
        }else{
        	unidad.setEstado(Const.Estado_Completa);
        	jEstadoField.setText(Const.EstcompletaStr);
        }
        jEstadoField.setEditable(false);
        
        unidad.setCodigo(jCodigoField.getText().trim());
        
		unidad.setFecha_construccion(fechaConsJDateChooser.getDate());
		unidad.setFecha_UltimaRef(fechaReformaJDateChooser.getDate());
		
		unidad.setTipo_unidad(getPatronUnidad());

		if (getListaPlazasTabla() != null || getListaPlazasTabla().size()!= 0 ){
			unidad.setPlazas(getListaPlazasTabla());
		}
		
		unidad.setCodigo(jCodigoField.getText().trim());
		unidad.setDescripcion(descripcionJTField.getText().trim());
		
		//Busco la localizacion:
        
		LocalizacionBean localizacion = unidad.getLocalizacion();
		if (localizacion == null) localizacion = new LocalizacionBean();
        localizacion.setDescripcion(jLocalizacionField.getText());
        unidad.setLocalizacion(localizacion);
        
        //hago el set
        setUnidadEnterramiento(unidad);
        
    }
    
    private int returnEstado(String estadoStr){
    
    	if (estadoStr.equalsIgnoreCase("ok")){
    		return 0;
    	}
    	return 1;
    }
    
    
    public void renombrarComponentes(){
        try{datosGeneralesComunesJPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.datosGenerales.tag1")));}catch(Exception e){}
    }
    
    
    public void loadDatosUnidadEnterramiento (UnidadEnterramientoBean unidadEnterramiento, boolean editable,
    		BloqueBean bloque, ConcesionBean concesion){
    	
		if (unidadEnterramiento == null)
			return;
		jLocalizacionField.setText(unidadEnterramiento.getDescripcion() != null ? 
				unidadEnterramiento.getLocalizacion().getDescripcion() : "Localizacion ");
		jLocalizacionField.setEditable(editable);

		if (bloque != null){
			if ((unidadEnterramiento.getFila() != 0) && (unidadEnterramiento.getFila() <= bloque.getNumFilas())){
				jFilaField.setText(String.valueOf(unidadEnterramiento.getFila()));
			}else{
				jFilaField.setText(String.valueOf(bloque.getNumFilas()));
			}
			jFilaField.setEditable(editable);
	
			if ((unidadEnterramiento.getColumna() != 0) && (unidadEnterramiento.getColumna() <= bloque.getNumColumnas())){
				jColumnaField.setText(String.valueOf(unidadEnterramiento.getColumna()));
			}else{
				jColumnaField.setText(String.valueOf(bloque.getNumColumnas()));
			}
			jColumnaField.setEditable(editable);
			
		}else{
			jFilaField.setText(String.valueOf(unidadEnterramiento.getFila()));
			jFilaField.setEditable(editable);
			jColumnaField.setText(String.valueOf(unidadEnterramiento.getColumna()));
			jColumnaField.setEditable(editable);
		}
		
	    jCodigoField.setText(unidadEnterramiento.getCodigo() != null ? unidadEnterramiento.getCodigo() : "codigo ");
	    jCodigoField.setEditable(editable);
	    
	    descripcionJTField.setText(unidadEnterramiento.getDescripcion() != null ? unidadEnterramiento.getDescripcion() : "Descripcion");
	    descripcionJTField.setEditable(editable);
	    
	    if (concesion!= null){
	    	DNIJTextField.setText(concesion.getTitular().getDNI() != null ? concesion.getTitular().getDNI() : "" );
	    	DNIJTextField.setEditable(false);
	    	nombreJTextField.setText(concesion.getTitular().getNombre() != null ? concesion.getTitular().getNombre() : "" );
	    	nombreJTextField.setEditable(false);
	    	fechaFinJTextField.setText(fecha.format(concesion.getFecha_fin()!= null ? concesion.getFecha_fin() : new Date()));
	    	fechaFinJTextField.setEditable(false);
	    	
			for (int i = 0; i < vDomainTipoConcesiones.size(); i++) {
				DomainNode node = (DomainNode) vDomainTipoConcesiones.get(i);
				if (node.getPatron().equalsIgnoreCase(String.valueOf(concesion.getTipo_concesion()))){
					TipoConcesionJComboBox.setSelectedIndex(i);
				}
			}
			TipoConcesionJComboBox.setEnabled(false);
	    }
	    
	    //Pongo el numero de plazas que hay dadas de alta
	    plazasSpinnerModel.setMinimum(unidadEnterramiento.getNumMin_Plazas());
	    plazasJSpinner.setModel(plazasSpinnerModel);
	    if (unidadEnterramiento.getPlazas().size() != 0){
		    plazasJSpinner.setValue(unidadEnterramiento.getPlazas().size());
		    
	    }else{
		    plazasJSpinner.setValue(new Integer(0));
	    }
	    plazasJSpinner.setEnabled(editable);

	    
		if (bloque!= null){
			if (unidadEnterramiento.getTipo_unidad()<=3) {
				//por defecto le pongo tipo 4 = nicho
				unidadEnterramiento.setTipo_unidad(4);
			}
		}
		for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
			DomainNode node = (DomainNode) vDomainTipoUnidades.get(i);
			if (node.getPatron().equalsIgnoreCase(String.valueOf(unidadEnterramiento.getTipo_unidad()))){
				tipoUnidadJComboBox.setSelectedIndex(i);
				setPatronUnidad(unidadEnterramiento.getTipo_unidad());
			}
		}
	  
		tipoUnidadJComboBox.setEnabled(editable);

	    fechaConsJDateChooser.setDate(unidadEnterramiento.getFecha_construccion()!= null ? unidadEnterramiento.getFecha_construccion() : new Date());
	    fechaConsJDateChooser.setEnabled(editable);
	     
	    fechaReformaJDateChooser.setDate(unidadEnterramiento.getFecha_UltimaRef()!= null ?  unidadEnterramiento.getFecha_UltimaRef() : new Date());
	    fechaReformaJDateChooser.setEnabled(editable);
	     
	     jEstadoField.setText("estado");
	     if (unidadEnterramiento.getPlazas() != null && unidadEnterramiento.getPlazas().size()< 6 ){//unidadEnterramiento.getNumMax_Plazas()){
	        unidadEnterramiento.setEstado(Const.Estado_libre);
	        jEstadoField.setText(Const.EstlibreStr);
	     }else{
	        unidadEnterramiento.setEstado(Const.Estado_libre);
	        jEstadoField.setText(Const.EstlibreStr);
	     }
	     jEstadoField.setEditable(editable);
	     
	     //cargar las plazas asociadas a la unidad
	     
	     try {
	    	clearTable();
			loadListaPlazas(unidadEnterramiento.getPlazas());
		} catch (Exception e) {
			 logger.error("loadListaPlazas de unidadEnterramiento : "+ e.getMessage());
			e.printStackTrace();
		}
	     
	}

    /**
     * Load:  Método que carga en el panel los valores del a unidad de enterramiento
     * @param unidadEnterramientoElem
     * @param editable
     * @param operacion
     */
	public void load(UnidadEnterramientoBean unidadEnterramientoElem, boolean editable, String operacion,
			BloqueBean bloqueElem, ConcesionBean concesionElem) {
		
		if (unidadEnterramientoElem == null)
			return;

		entidadJTField.setText(unidadEnterramientoElem.getEntidad() != null ?  unidadEnterramientoElem.getEntidad() : "");
		cementerioJTField.setText(unidadEnterramientoElem.getNombreCementerio() != null ?  unidadEnterramientoElem.getNombreCementerio() : "");
		
		if (unidadEnterramientoElem.getFila()!= 0){
			jFilaField.setText(String.valueOf(unidadEnterramientoElem.getFila()));
			jFilaField.setEditable(true);
		}else{
			jFilaField.setEditable(false);
		}
		if (unidadEnterramientoElem.getColumna()!= 0){
			jColumnaField.setText(String.valueOf(unidadEnterramientoElem.getColumna()));
			jColumnaField.setEditable(true);
		}else{
			jColumnaField.setEditable(false);
		}
		
		if (bloqueElem!= null){
		
	       for (int i = 0; i < vDomainTipoUnidades.size(); i++) {
	    	DomainNode node = (DomainNode) vDomainTipoUnidades.get(i);
			if (node.getPatron().equalsIgnoreCase(String.valueOf(unidadEnterramientoElem.getTipo_unidad()))){
				tipoUnidadJComboBox.setSelectedIndex(i);
				setPatronUnidad(unidadEnterramientoElem.getTipo_unidad());
			}
	      }
	       tipoUnidadJComboBox.setEnabled(editable);
		}
		unidadEnterramientoElem.setSuperPatron(Const.SUPERPATRON_GELEMENTOS);
		unidadEnterramientoElem.setPatron(Const.PATRON_UENTERRAMIENTO);
    	setUnidadEnterramiento(unidadEnterramientoElem);
        if(operacion == null) return;

        datosGeneralesComunesJPanel.setEnabled(editable);
        datosUnidadEnterramientoJPanel.setEnabled(editable);    
        
        if ((operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) || 
        		(operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)))){

        	loadDatosUnidadEnterramiento(unidadEnterramientoElem, editable, bloqueElem, concesionElem);
        	
        	cementerioJTField.setEditable(editable);
        	entidadJTField.setEditable(editable);
        }
        
        //un campo de texto que se pone por defecto
        if 	(!todasAsignadas(unidadEnterramientoElem.getPlazas())){
        	unidadEnterramientoElem.setEstado(Const.Estado_libre);
        	jEstadoField.setText(Const.EstlibreStr);
        }else{
        	unidadEnterramientoElem.setEstado(Const.Estado_Completa);
        	jEstadoField.setText(Const.EstcompletaStr);
        }
        unidadEnterramientoElem.setEstado(unidadEnterramiento.getEstado());
        jEstadoField.setEditable(false);
	}
	
	/**
	 * 
	 */
	public void clear() {
		entidadJTField.setText("");
		cementerioJTField.setText("");		
	}

    /**
     * Método que actualiza la operacion que se esta realizando desde el panel padre
     * @param s operacion
     */
    public void setOperacion(String s){
        this.operacion= s;
    }

    private javax.swing.JFrame desktop;
    
    private javax.swing.JPanel UnidadEnterramientoJPanel;
    private javax.swing.JPanel datosGeneralesComunesJPanel;
    private javax.swing.JPanel datosUnidadEnterramientoJPanel;
    private javax.swing.JLabel cementerioJLabel;
    private javax.swing.JLabel codigoJLabel;
    private javax.swing.JLabel columnaJLabel;
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JLabel entidadJLabel;
    private javax.swing.JLabel estadoJLabel;
    private javax.swing.JLabel filaJLabel;
    private javax.swing.JTextField cementerioJTField;
    private javax.swing.JTextField jCodigoField;
    private javax.swing.JTextField jColumnaField;
    private javax.swing.JComboBox tipoUnidadJComboBox;
    private javax.swing.JTextField entidadJTField;
    private javax.swing.JTextField jEstadoField;

    private javax.swing.JLabel fechaConsJLabel;
    private JDateChooser fechaConsJDateChooser;
   
    private javax.swing.JLabel fechaReformaJLabel;
    private JDateChooser fechaReformaJDateChooser;

    private javax.swing.JTextField jFilaField;
    private javax.swing.JTextField jLocalizacionField;

    private javax.swing.JTextField descripcionJTField;
    private javax.swing.JLabel localizacionJLabel;
    private javax.swing.JLabel tipoUnidadJLabel;
    
    private javax.swing.JLabel plazasJLabel;
    private javax.swing.JSpinner plazasJSpinner;
    private javax.swing.SpinnerNumberModel plazasSpinnerModel; 
    
    private javax.swing.JScrollPane plazasJScrollPane;
    private javax.swing.JTable plazasJTable;
    private javax.swing.JTabbedPane plazasTabbedPane;
    private javax.swing.JPanel tablaJPanel;

    private javax.swing.JPanel concesionJPanel;
    private javax.swing.JLabel DNIJLabel;
    private javax.swing.JTextField DNIJTextField;
    private javax.swing.JLabel NombreJLabel;
    private javax.swing.JTextField nombreJTextField;
    private javax.swing.JLabel tipoConcesionJLabel;
    private javax.swing.JComboBox TipoConcesionJComboBox;
    private javax.swing.JLabel fechaFinJLabel;
    private javax.swing.JTextField fechaFinJTextField;
    
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
    
    public boolean todasAsignadas(ArrayList listaPlazas){
    	boolean asignadas = true;
    	if ((listaPlazas == null) || (listaPlazas.size()== 0)) return false; 
    	
    	for (int i = 0; i < listaPlazas.size(); i++) {
			PlazaBean plaza = (PlazaBean) listaPlazas.get(i);
			// Está asignada
			if (!plaza.isEstado()){
				asignadas = false;
				break;
				
			}
		}
    	return asignadas;
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

    public UnidadEnterramientoBean getUnidadEnterramiento() {
		return this.unidadEnterramiento;
	}

	public void setUnidadEnterramiento(UnidadEnterramientoBean unidadEnterramiento) {
		this.unidadEnterramiento = unidadEnterramiento;
	}

	public void setDate(Date date) {
		((JDateChooser) fechaConsJDateChooser).setDate(date);
		((JDateChooser) fechaReformaJDateChooser).setDate(date);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			setDate((Date) evt.getNewValue());
		}
	}

	public int getPatronUnidad() {
		return patronUnidad;
	}

	public void setPatronUnidad(int patronUnidad) {
		this.patronUnidad = patronUnidad;
	}

	public ArrayList<PlazaBean> getListaPlazasTabla() {
		return listaPlazasTabla;
	}

	public void setListaPlazasTabla(ArrayList<PlazaBean> listaPlazasTabla) {
		this.listaPlazasTabla = listaPlazasTabla;
	}
	
	public PlazaBean getPlazaSelected() {
		return plazaSelected;
	}

	public void setPlazaSelected(PlazaBean plazaSelected) {
		this.plazaSelected = plazaSelected;
	}
	
}
