package com.geopista.app.catastro.registroExpedientes.gestionExp;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosExpGestionExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosNotificacionCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosPersonalesCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelExpAdminGerencia;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelMapa;
import com.geopista.app.catastro.registroExpedientes.paneles.TablaExpGestionExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.vividsolutions.jump.I18N;

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite al usuario gestionar el expediente. En la
 * pantalla puede ver los datos mas importantes del expediente, las parcelas o bienes inmuebles asociados al expediente
 * y un mapa para visualizar las parcelas. Ademas los botones permiten acceder a todas las pantallas que nos permiten
 * gestionar el expediente. La clase se encarga de cargar los paneles y necesario para funcionar.
 * */

public class GestionDeExpedientes extends JInternalFrame implements IMultilingue
{
    private final JFrame desktop;
    private JPanel getionExpYMapPanel;
    private JPanel gestionExpPanel;
    private JScrollPane gestionExpMapScrollPane;
    private PanelDatosExpGestionExp datosExpPanel;
    private PanelExpAdminGerencia gerenciaPanel;
    private PanelDatosNotificacionCrearExp notificacionPanel;
    private PanelDatosPersonalesCrearExp datosPerPanel;
    private TablaExpGestionExp tablaExpTable;
    private PanelMapa editorMapaPanel;
    private JPanel botonesPanel;
    private JButton asocExp_ConsultaCatastroButton;
    private JButton actualizacionCatastralButton;
	private JButton consultaEstadoExpedienteButton;
	private JButton modificarExpedienteButton;
    private JButton modificarInfCatastralButton;
    private JButton exportarFicheroButton;
    private JButton asociarDatosGrafButton;
    private JButton asociarParcelasYBienesInmueblesButton;
    private JButton verEstadoButton;
    private Expediente expediente;
    private ArrayList referenciasCatastrales;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame y el expediente con el que se esta trabajando.
     *
     * @param desktop  JFrame
     * @param exp Expediente con el que se esta trabajando
     */
    public GestionDeExpedientes(final JFrame desktop, Expediente exp)
    {
    	this.setResizable(true);
        this.desktop= desktop;
        this.expediente= exp;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
        inicializaElementos();
        /*addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame();
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });  */
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.GestionDeExpediente.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Inicializa los elementos del panel.
     *
     */
    private void inicializaElementos()
    {
        getionExpYMapPanel= new JPanel();
        gestionExpMapScrollPane= new JScrollPane();
        getionExpYMapPanel.setLayout(new BorderLayout());
        //getionExpYMapPanel.setPreferredSize(new Dimension(990,620));
        referenciasCatastrales = new ArrayList();
        if(expediente!=null &&expediente.getListaReferencias()!=null)
        {
            referenciasCatastrales.addAll(expediente.getListaReferencias());
        }
        inicializaPanelExp();
        inicializaPanelMap();
        inicializaBotones();        
        getionExpYMapPanel.add(gestionExpPanel,BorderLayout.WEST);
        if (editorMapaPanel!=null)
	        getionExpYMapPanel.add(editorMapaPanel,BorderLayout.CENTER);
        getionExpYMapPanel.add(botonesPanel,BorderLayout.SOUTH);        
        gestionExpMapScrollPane.setViewportView(getionExpYMapPanel);
        getContentPane().add(gestionExpMapScrollPane);
        inicializaDatos();
    }

    /**
     * Inicializa el panel de gestión del expediente.
     *
     */
    private void inicializaPanelExp()
    {
        gestionExpPanel= new JPanel();
        gestionExpPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosExpPanel= new PanelDatosExpGestionExp("Catastro.RegistroExpedientes.GestionDeExpediente.datosExpPanel");
        gerenciaPanel = new PanelExpAdminGerencia("Catastro.RegistroExpedientes.GestionDeExpediente.gerenciaPanel");
        gerenciaPanel.setEditable(false);
        datosPerPanel = new PanelDatosPersonalesCrearExp("Catastro.RegistroExpedientes.GestionDeExpediente.datosPerPanel");
        datosPerPanel.setDesktop(desktop);
        datosPerPanel.setEditable(false);
        notificacionPanel = new PanelDatosNotificacionCrearExp("Catastro.RegistroExpedientes.GestionDeExpediente.notificacionPanel");
        notificacionPanel.setEditable(false);

        if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            tablaExpTable= new TablaExpGestionExp("Catastro.RegistroExpedientes.GestionDeExpediente.tablaExpTable", referenciasCatastrales);            
        }
        else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            tablaExpTable= new TablaExpGestionExp("Catastro.RegistroExpedientes.GestionDeExpediente.tablaExpTableBI", referenciasCatastrales);
        }

        gestionExpPanel.add(datosExpPanel.getDatosExpPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 500, 160));
        gestionExpPanel.add(gerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 165, 500, 90));
        gestionExpPanel.add(datosPerPanel.getDatosPersonalesPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 255, 500, 78));
        gestionExpPanel.add(notificacionPanel.getDatosNotifPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 333, 500, 114));
        gestionExpPanel.add(tablaExpTable.getTablaEepPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 447, 500, 110));
        
        tablaExpTable.getTablaExpTabel().addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                String refCatas="";
                if(referenciasCatastrales.get(tablaExpTable.getParcelaSeleccionada()) instanceof FincaCatastro)
                {
                    refCatas = ((FincaCatastro)referenciasCatastrales.get(tablaExpTable.getParcelaSeleccionada())).getRefFinca().getRefCatastral();
                }
                else if(referenciasCatastrales.get(tablaExpTable.getParcelaSeleccionada()) instanceof BienInmuebleCatastro)
                {
                    refCatas = ((BienInmuebleCatastro)referenciasCatastrales.get(tablaExpTable.getParcelaSeleccionada())).getIdBienInmueble().getParcelaCatastral().getRefCatastral();
                }
                editorMapaPanel.actualizarFeatureSelection("parcelas",refCatas);
            }
        });
    }

    /**
     * Inicializa el panel del editor de mapas.
     *
     */
    private void inicializaPanelMap()
    {
        editorMapaPanel= new PanelMapa("Catastro.RegistroExpedientes.GestionDeExpediente.editorMapaPanel");
        editorMapaPanel.setDesktop(desktop);
        editorMapaPanel.load(false,editorMapaPanel);        
    }

    /**
     * Inicializa los botones del panel.
     */
    private void inicializaBotones()
    {
        botonesPanel= new JPanel();
        botonesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        asocExp_ConsultaCatastroButton = new JButton();
        if(expediente.getIdEstado() ==  ConstantesRegistroExp.ESTADO_REGISTRADO)
        {
        	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarExpedienteButton.hint"));
        	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarExpedienteButton"));
        }
        else{
        //else if(expediente.getIdEstado() ==  ConstantesRegistroExp.ESTADO_ASOCIADO){
        	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("Catastro.RegistroExpedientes.GestionDeExpediente.obtenerInfCatastroButton.hint"));
        	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.obtenerInfCatastroButton"));
        }  
        
        actualizacionCatastralButton = new JButton();
        actualizacionCatastralButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.actualizaCatastroButton.hint"));
        actualizacionCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.actualizaCatastroButton"));
      

        consultaEstadoExpedienteButton = new JButton();  
        consultaEstadoExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton.hint"));
        consultaEstadoExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton"));
        
        modificarExpedienteButton = new JButton();
        if(expediente.getIdEstado() < ConstantesRegistroExp.ESTADO_FINALIZADO)
        {
            modificarExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton.hint"));
            modificarExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton"));
        }
        else{
            modificarExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.consultarExpedienteButton.hint"));
            modificarExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarExpedienteButton"));
        }

        modificarInfCatastralButton = new JButton();
        if(expediente.getIdEstado() < ConstantesRegistroExp.ESTADO_FINALIZADO)
        {
            modificarInfCatastralButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarInfCatastralButton.hint"));
            modificarInfCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarInfCatastralButton"));
        }
        else{
            modificarInfCatastralButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarInfCatastralButton.hint"));
            modificarInfCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarInfCatastralButton"));
        }

        exportarFicheroButton = new JButton();
        exportarFicheroButton.setToolTipText(I18N.get("RegistroExpedientes",  "Catastro.RegistroExpedientes.GestionDeExpediente.exportarFicheroButton.hint"));
        exportarFicheroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.exportarFicheroButton"));

        asociarDatosGrafButton = new JButton();
        asociarDatosGrafButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarDatosGrafButton.hint"));
        asociarDatosGrafButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarDatosGrafButton"));

        asociarParcelasYBienesInmueblesButton = new JButton();
        if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton"));
        }
        else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton"));
        }

        verEstadoButton = new JButton();
        verEstadoButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton.hint"));
        verEstadoButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton"));

        enableBotones();
        if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            asociarDatosGrafButton.setEnabled(false);    
        }
    
        botonesPanel.add(asocExp_ConsultaCatastroButton, new org.netbeans.lib.awtextra.AbsoluteConstraints((desktop.getWidth()/8)-160, 5, 140, 30));
        botonesPanel.add(actualizacionCatastralButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(2*(desktop.getWidth()/8)-180, 5, 140, 30));
        botonesPanel.add(consultaEstadoExpedienteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(3*(desktop.getWidth()/8)-200, 5, 140, 30));
        botonesPanel.add(modificarExpedienteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(4*(desktop.getWidth()/8)-220, 5, 140, 30));
        botonesPanel.add(modificarInfCatastralButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(5*(desktop.getWidth()/8)-240, 5, 140, 30));
        botonesPanel.add(exportarFicheroButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(6*(desktop.getWidth()/8)-260, 5, 140, 30));
        botonesPanel.add(asociarDatosGrafButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(7*(desktop.getWidth()/8)-280, 5, 140, 30));
        botonesPanel.add(asociarParcelasYBienesInmueblesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(8*(desktop.getWidth()/8)-300, 5, 140, 30));
        botonesPanel.add(verEstadoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(9*(desktop.getWidth()/8)-320, 5, 140, 30));
    }

    /**
     * Pone a disponible los botones de consulta, modificacion, exportacion, asociar datos gráficos, asociar
     * parcelas y ver estado de la aplicación.
     *
     */
    public void enableBotones()
    {
        modificarExpedienteButton.setEnabled(ConstantesRegistroExp.botonesGestExp[0]);
        modificarInfCatastralButton.setEnabled(ConstantesRegistroExp.botonesGestExp[1]);
        exportarFicheroButton.setEnabled(ConstantesRegistroExp.botonesGestExp[2]);
        asociarDatosGrafButton.setEnabled(ConstantesRegistroExp.botonesGestExp[3]);
        asociarParcelasYBienesInmueblesButton.setEnabled(ConstantesRegistroExp.botonesGestExp[4]);
        verEstadoButton.setEnabled(ConstantesRegistroExp.botonesGestExp[5]);
        asocExp_ConsultaCatastroButton.setEnabled(ConstantesRegistroExp.botonesGestExp[6]);
        actualizacionCatastralButton.setEnabled(ConstantesRegistroExp.botonesGestExp[7]);
        consultaEstadoExpedienteButton.setEnabled(ConstantesRegistroExp.botonesGestExp[8]);
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

    /**
     * Devuelve boton verEstado
     *
     * @return JButton verEstadoButton.
     */
    public JButton getVerEstadoButton()
    {
        return verEstadoButton;
    }

    /**
     * Devuelve boton asociarParcelasYBienesInmueblesButton
     *
     * @return JButton asociarParcelasYBienesInmueblesButton.
     */
    public JButton getAsociarParcelasYBienesInmuebleButton()
    {
        return asociarParcelasYBienesInmueblesButton;
    }

    /**
     * Devuelve boton asociarExpedienteButton
     *
     * @return JButton asociarExpedienteButton.
     */
    public JButton getAsocExp_ConsultaCatastroButton() {
		return asocExp_ConsultaCatastroButton;
	}

    
	public JButton getConsultaEstadoExpedienteButton() {
		return consultaEstadoExpedienteButton;
	}

	 public JButton getActualizacionCatastralButton() {
			return actualizacionCatastralButton;
	}

	
    /**
     * Devuelve boton modificarExpedienteButton
     *
     * @return JButton modificarExpedienteButton.
     */
    public JButton getModificarExpedienteButton()
    {
        return modificarExpedienteButton;
    }

    /**
     * Devuelve boton modificarInfCatastralButton
     *
     * @return JButton modificarInfCatastralButton.
     */
    public JButton getModificarInfCatastralButton()
    {
        return modificarInfCatastralButton;
    }

    /**
     * Devuelve boton asociarDatosGrafButton
     *
     * @return JButton asociarDatosGrafButton.
     */
    public JButton getAsociarDatosGraficosButton()
    {
        return asociarDatosGrafButton;
    }

    /**
     * Devuelve boton exportarFicheroButton
     *
     * @return JButton exportarFicheroButton.
     */
    public JButton getExportarFicheroButton()
    {
        return exportarFicheroButton;
    }

    /**
     * Devuelve el expediente que se esta gestionando
     *
     * @return Expediente el expediente que se esta gestionando.
     */
    public Expediente getExpediente()
    {
        return expediente;
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.titulo"));
        datosExpPanel.renombrarComponentes();
        datosPerPanel.renombrarComponentes();
        notificacionPanel.renombrarComponentes();
        gerenciaPanel.renombrarComponentes();
        tablaExpTable.renombrarComponentes();
        editorMapaPanel.renombrarComponentes();
        
        asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton.hint"));
        asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton"));

        if(expediente.getIdEstado() < ConstantesRegistroExp.ESTADO_FINALIZADO)
        {
        	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarInfCatastralButton.hint"));
        	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarInfCatastralButton"));
        }
        else{
        	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarInfCatastralButton.hint"));
        	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarInfCatastralButton"));
        }
            
        actualizacionCatastralButton = new JButton();
        actualizacionCatastralButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.actualizaCatastroButton.hint"));
        actualizacionCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.actualizaCatastroButton"));
      

        consultaEstadoExpedienteButton = new JButton();  
        consultaEstadoExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton.hint"));
        consultaEstadoExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton"));
        
        modificarExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton.hint"));
        modificarExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton"));

        if(expediente.getIdEstado() < ConstantesRegistroExp.ESTADO_FINALIZADO)
        {
            modificarInfCatastralButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarInfCatastralButton.hint"));
            modificarInfCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarInfCatastralButton"));
        }
        else{
            modificarInfCatastralButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarInfCatastralButton.hint"));
            modificarInfCatastralButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarInfCatastralButton"));
        }

        exportarFicheroButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.exportarFicheroButton.hint"));
        exportarFicheroButton.setText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.exportarFicheroButton"));

        asociarDatosGrafButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarDatosGrafButton.hint"));
        asociarDatosGrafButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarDatosGrafButton"));

        if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes",  "Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton"));
        }
        else if(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesRegistroExp.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton"));
        }

        verEstadoButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton.hint"));
        verEstadoButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton"));
    }

    
    /**
     * Inicializa los datos del expediente.
     *
     */
    public void inicializaDatos()
    {
       if(expediente!=null)
       {
            Hashtable hashDatos = new Hashtable();

            hashDatos.put(ConstantesRegistroExp.expedienteIdEstado, String.valueOf(expediente.getIdEstado()));
            hashDatos.put(ConstantesRegistroExp.expedienteNumeroExpediente, String.valueOf(expediente.getNumeroExpediente()));
            hashDatos.put(ConstantesRegistroExp.expedienteTipoExpediente, expediente.getTipoExpediente().getCodigoTipoExpediente());
            if (expediente.getFechaRegistro()!=null)
                hashDatos.put(ConstantesRegistroExp.expedienteFechaRegistro, expediente.getFechaRegistro());
            else
                hashDatos.put(ConstantesRegistroExp.expedienteFechaRegistro,"");
            if (expediente.getFechaAlteracion()!=null)
                hashDatos.put(ConstantesRegistroExp.expedienteFechaAlteracion, expediente.getFechaAlteracion());
            else
                hashDatos.put(ConstantesRegistroExp.expedienteFechaAlteracion,"");
            if(expediente.getFechaMovimiento()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteFechaMovimiento, expediente.getFechaMovimiento());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteFechaMovimiento, "");
            }
            if(expediente.getHoraMovimiento()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteHoraMovimiento, expediente.getHoraMovimiento());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteHoraMovimiento, "");
            }
            if(expediente.getAnnoExpedienteAdminOrigenAlteracion()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteAdminOrigenAlteracion, String.valueOf(expediente.getAnnoExpedienteAdminOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteAdminOrigenAlteracion,"");
            }
            hashDatos.put(ConstantesRegistroExp.entidadGeneradoraCodigo, String.valueOf(expediente.getEntidadGeneradora().getCodigo()));
            if(expediente.getAnnoExpedienteGerencia()!=null)  //PREV-NOV
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia, String.valueOf(expediente.getAnnoExpedienteGerencia()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia,"");
            }
            if(expediente.getReferenciaExpedienteGerencia()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia, String.valueOf(expediente.getReferenciaExpedienteGerencia()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia, "");                
            }
            // PREV-NOV if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!=0)
            if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!= null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, String.valueOf(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion,"");
            }
            hashDatos.put(ConstantesRegistroExp.expedienteNifPresentador, expediente.getNifPresentador());
            hashDatos.put(ConstantesRegistroExp.expedienteNombreCompletoPresentador, expediente.getNombreCompletoPresentador());

            hashDatos.put(ConstantesRegistroExp.direccionProvinciaINE, String.valueOf(expediente.getDireccionPresentador().getProvinciaINE()));
            hashDatos.put(ConstantesRegistroExp.direccionMunicipioINE, String.valueOf(expediente.getDireccionPresentador().getMunicipioINE()));
            hashDatos.put(ConstantesRegistroExp.direccionCodigoMunicipioDGC, String.valueOf(expediente.getDireccionPresentador().getCodigoMunicipioDGC()));
            hashDatos.put(ConstantesRegistroExp.direccionNombreProvincia, expediente.getDireccionPresentador().getNombreProvincia());
            hashDatos.put(ConstantesRegistroExp.direccionNombreMunicipio, expediente.getDireccionPresentador().getNombreMunicipio());
            if(expediente.getDireccionPresentador().getNombreEntidadMenor()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionNombreEntidadMenor, expediente.getDireccionPresentador().getNombreEntidadMenor());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionNombreEntidadMenor, "");                
            }
            hashDatos.put(ConstantesRegistroExp.direccionCodigoPostal, String.valueOf(expediente.getDireccionPresentador().getCodigoPostal()));
            if(expediente.getDireccionPresentador().getApartadoCorreos()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.direccionApartadoCorreos, String.valueOf(expediente.getDireccionPresentador().getApartadoCorreos()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionApartadoCorreos,"");
            }

            datosExpPanel.inicializaDatos(hashDatos);
            datosPerPanel.inicializaDatos(hashDatos);
            gerenciaPanel.inicializaDatos(hashDatos);
            notificacionPanel.inicializaDatos(hashDatos);
       }
    }

    /**
     * Devuelve el estado del expediente.
     * @return String con el nombre del estado del expediente.
     */
    public String getNombreEstado()
    {
        if(expediente!=null)
        {
            return Estructuras.getListaEstadosExpediente().getDomainNode(String.valueOf(expediente.getIdEstado())).getTerm(ConstantesRegistroExp.Locale);
        }
        else
        {
            return null;
        }
    }
    
    public void actualizarBotonAsociarExpediente(){
    	
    	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.obtenerInfCatastroButton.hint"));
    	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.obtenerInfCatastroButton"));

    }
    
   /* public boolean  gestionActualizaCatastro(ApplicationContext aplicacion ) throws Exception{
    	boolean isCorrecto = false;
    	COperacionesDGC geoConn= new COperacionesDGC();
    	
		DatosWSResponseBean datosWsResponse = null;
		
		Date fechaMovimiento = new Date();
		expediente.setFechaMovimiento(fechaMovimiento);
		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
		expediente.setHoraMovimiento(formato.format(fechaMovimiento));
		
		CatastroWS catastrows = new CatastroWS();
   	 	String XML = catastrows.buildActualizaCatastroRequest(expediente, geoConn);
		
         TestClient test = new TestClient();
         datosWsResponse = test.actualizacionCatastro(XML, aplicacion);
        
         if(datosWsResponse.getRespuesta().getLstUnidadError() != null &&
        		 !datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
        	 //	se han producido errores
        	  GestionResponseWS gestionResponseWS = new GestionResponseWS();
              String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, aplicacion);
              
              MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.actualizaciónCatastro.title");
              messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
              messageDialog.show();	
         }
         else if (datosWsResponse.getRespuesta().getEstado().equals(DatosWSResponseBean.ESTADO_OK)){
    	 	//se actualiza el estado del expediente
                	
        	//se comprueban los identificadores de dialogo de las parcelas o bienes
        	/*
        	ArrayList lstFincasErroneas = new ArrayList();
        	ArrayList lstBienesErroneas = new ArrayList();
        	for(int i=0; i<datosWsResponse.getControl().getLstIdentificadoresDialogo().size();i++){
        		IdentificadorDialogo identificadorDialogo = (IdentificadorDialogo)datosWsResponse.getControl().getLstIdentificadoresDialogo().get(i);

        		if(identificadorDialogo.getFincaBien() instanceof FincaCatastro){
        			String idDialogo = (String)ConstantesRegistroExp.clienteCatastro.getIdentificadorExpedienteFinca(expediente, identificadorDialogo);		
        			if(!identificadorDialogo.getIdentificadorDialogo().equals(idDialogo)){
        				lstFincasErroneas.add(((FincaCatastro)identificadorDialogo.getFincaBien()).getRefFinca().getRefCatastral());
        			}
        				
        		}
        		else if(identificadorDialogo.getFincaBien() instanceof BienInmuebleCatastro){
        			String idDialogo = (String)ConstantesRegistroExp.clienteCatastro.getIdentificadorExpedienteBienes(expediente, identificadorDialogo);
        			if(!identificadorDialogo.getIdentificadorDialogo().equals(idDialogo)){
        				lstBienesErroneas.add(((BienInmuebleCatastro)identificadorDialogo.getFincaBien()).getIdBienInmueble().getIdBienInmueble());
            		}
        		}
        	}
        	
        	if(!lstFincasErroneas.isEmpty() || !lstBienesErroneas.isEmpty()){
        		// hay fincas o bienes que no estan actualizadas	
        		isCorrecto = false;
        	}
        	else{
        		// fincas o bienes correctos, se actualiza el expediente
        		isCorrecto = true;
        	}
        	*/
    /*    	 isCorrecto = true;
         }
    	return isCorrecto;
    }*/
}
