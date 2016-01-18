/**
 * GestionDeExpedientes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosExpGestionExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosNotificacionCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosPersonalesCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelExpAdminGerencia;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelMapa;
import com.geopista.app.catastro.registroExpedientes.paneles.TablaExpGestionExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
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

    private Hashtable codigoDigitoControlDni;
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

        getCodigosDigitoControlDniBD();
        datosPerPanel.setDigitoContolDniHash(codigoDigitoControlDni);
        
        
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            tablaExpTable= new TablaExpGestionExp("Catastro.RegistroExpedientes.GestionDeExpediente.tablaExpTable", referenciasCatastrales);            
        }
        else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            tablaExpTable= new TablaExpGestionExp("Catastro.RegistroExpedientes.GestionDeExpediente.tablaExpTableBI", referenciasCatastrales);
        }

        gestionExpPanel.add(datosExpPanel.getDatosExpPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 500, 205));
        gestionExpPanel.add(gerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 500, 90));
        gestionExpPanel.add(datosPerPanel.getDatosPersonalesPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 500, 78));
        gestionExpPanel.add(notificacionPanel.getDatosNotifPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 378, 500, 114));
        gestionExpPanel.add(tablaExpTable.getTablaEepPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 492, 500, 110));
        
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
        if(expediente.getIdEstado() ==  ConstantesCatastro.ESTADO_REGISTRADO)
        {
        	asocExp_ConsultaCatastroButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarExpedienteButton.hint"));
        	asocExp_ConsultaCatastroButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarExpedienteButton"));
        }
        else{
        //else if(expediente.getIdEstado() ==  ConstantesCatastro.ESTADO_ASOCIADO){
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
        if(expediente.getIdEstado() < ConstantesCatastro.ESTADO_FINALIZADO)
        {
            modificarExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton.hint"));
            modificarExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.modificarExpedienteButton"));
        }
        else{
            modificarExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.consultarExpedienteButton.hint"));
            modificarExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultarExpedienteButton"));
        }

        modificarInfCatastralButton = new JButton();
        if(expediente.getIdEstado() < ConstantesCatastro.ESTADO_FINALIZADO)
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
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton"));
        }
        else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarBienesInmueblesButton"));
        }

        verEstadoButton = new JButton();
        verEstadoButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton.hint"));
        verEstadoButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.verEstadoButton"));

        enableBotones();
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
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
        modificarExpedienteButton.setEnabled(ConstantesCatastro.botonesGestExp[0]);
        modificarInfCatastralButton.setEnabled(ConstantesCatastro.botonesGestExp[1]);
        exportarFicheroButton.setEnabled(ConstantesCatastro.botonesGestExp[2]);
        asociarDatosGrafButton.setEnabled(ConstantesCatastro.botonesGestExp[3]);
        asociarParcelasYBienesInmueblesButton.setEnabled(ConstantesCatastro.botonesGestExp[4]);
        verEstadoButton.setEnabled(ConstantesCatastro.botonesGestExp[5]);
        asocExp_ConsultaCatastroButton.setEnabled(ConstantesCatastro.botonesGestExp[6]);
        actualizacionCatastralButton.setEnabled(ConstantesCatastro.botonesGestExp[7]);
        consultaEstadoExpedienteButton.setEnabled(ConstantesCatastro.botonesGestExp[8]);
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

        if(expediente.getIdEstado() < ConstantesCatastro.ESTADO_FINALIZADO)
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

        if(expediente.getIdEstado() < ConstantesCatastro.ESTADO_FINALIZADO)
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

        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                &&(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)))
        {
            asociarParcelasYBienesInmueblesButton.setToolTipText(I18N.get("RegistroExpedientes",  "Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton.hint"));
            asociarParcelasYBienesInmueblesButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.asociarParcelasButton"));
        }
        else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
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

            hashDatos.put(ConstantesCatastro.expedienteIdEstado, String.valueOf(expediente.getIdEstado()));
            hashDatos.put(ConstantesCatastro.expedienteNumeroExpediente, String.valueOf(expediente.getNumeroExpediente()));
            hashDatos.put(ConstantesCatastro.expedienteTipoExpediente, expediente.getTipoExpediente().getCodigoTipoExpediente());
            hashDatos.put(ConstantesCatastro.expedienteTipoTramitacion,expediente.isTipoTramitaExpSitFinales());
            hashDatos.put(ConstantesCatastro.expedienteEntidadGeneradora,expediente.getEntidadGeneradora());
            if (expediente.getFechaRegistro()!=null)
                hashDatos.put(ConstantesCatastro.expedienteFechaRegistro, expediente.getFechaRegistro());
            else
                hashDatos.put(ConstantesCatastro.expedienteFechaRegistro,"");
            if (expediente.getFechaAlteracion()!=null)
                hashDatos.put(ConstantesCatastro.expedienteFechaAlteracion, expediente.getFechaAlteracion());
            else
                hashDatos.put(ConstantesCatastro.expedienteFechaAlteracion,"");
            if(expediente.getFechaMovimiento()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteFechaMovimiento, expediente.getFechaMovimiento());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteFechaMovimiento, "");
            }
            if(expediente.getHoraMovimiento()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteHoraMovimiento, expediente.getHoraMovimiento());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteHoraMovimiento, "");
            }
            if(expediente.getAnnoExpedienteAdminOrigenAlteracion()!=0)
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoExpedienteAdminOrigenAlteracion, String.valueOf(expediente.getAnnoExpedienteAdminOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoExpedienteAdminOrigenAlteracion,"");
            }
            hashDatos.put(ConstantesCatastro.entidadGeneradoraCodigo, String.valueOf(expediente.getEntidadGeneradora().getCodigo()));
            if(expediente.getAnnoExpedienteGerencia()!=null)  //PREV-NOV
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoExpedienteGerencia, String.valueOf(expediente.getAnnoExpedienteGerencia()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoExpedienteGerencia,"");
            }
            if(expediente.getReferenciaExpedienteGerencia()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteReferenciaExpedienteGerencia, String.valueOf(expediente.getReferenciaExpedienteGerencia()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteReferenciaExpedienteGerencia, "");                
            }
            // PREV-NOV if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!=0)
            if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!= null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, String.valueOf(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion,"");
            }
            if(expediente.getNifPresentador().length() == 9){
            	hashDatos.put(ConstantesCatastro.expedienteNifPresentador, expediente.getNifPresentador().substring(0, 8));
            	hashDatos.put(ConstantesCatastro.expedienteDigitoControlNifPresentador, expediente.getNifPresentador().substring(8, 9));
            }
            hashDatos.put(ConstantesCatastro.expedienteNombreCompletoPresentador, expediente.getNombreCompletoPresentador());

            hashDatos.put(ConstantesCatastro.direccionProvinciaINE, String.valueOf(expediente.getDireccionPresentador().getProvinciaINE()));
            hashDatos.put(ConstantesCatastro.direccionMunicipioINE, String.valueOf(expediente.getDireccionPresentador().getMunicipioINE()));
            hashDatos.put(ConstantesCatastro.direccionCodigoMunicipioDGC, String.valueOf(expediente.getDireccionPresentador().getCodigoMunicipioDGC()));
            hashDatos.put(ConstantesCatastro.direccionNombreProvincia, expediente.getDireccionPresentador().getNombreProvincia());
            hashDatos.put(ConstantesCatastro.direccionNombreMunicipio, expediente.getDireccionPresentador().getNombreMunicipio());
            if(expediente.getDireccionPresentador().getNombreEntidadMenor()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionNombreEntidadMenor, expediente.getDireccionPresentador().getNombreEntidadMenor());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionNombreEntidadMenor, "");                
            }
            hashDatos.put(ConstantesCatastro.direccionCodigoPostal, String.valueOf(expediente.getDireccionPresentador().getCodigoPostal()));
            if(expediente.getDireccionPresentador().getApartadoCorreos()!=0)
            {
                hashDatos.put(ConstantesCatastro.direccionApartadoCorreos, String.valueOf(expediente.getDireccionPresentador().getApartadoCorreos()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionApartadoCorreos,"");
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
            return Estructuras.getListaEstadosExpediente().getDomainNode(String.valueOf(expediente.getIdEstado())).getTerm(ConstantesCatastro.Locale);
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
    
    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los codigos
     * del calculo del digito de control del DNI. 
     * La consulta devuelve una hashtable, la key es el resto y el valor es la letra asociada
     */
    private void getCodigosDigitoControlDniBD()
    {
        try
        {
        	codigoDigitoControlDni= ConstantesRegExp.clienteCatastro.getCodigoDigitoControlDni();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
}
