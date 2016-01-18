package com.geopista.app.catastro.registroExpedientes.modificarExp;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.paneles.*;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 23-mar-2007
 * Time: 10:39:43
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite al usuario modificar los parametros introducidos
 * en la pantalla de crear expediente excepto ciertos campos obligatorios.
 * La clase se encarga de cargar los paneles necesarios para funcionar.
 * */

public class ModificarExpediente  extends JInternalFrame implements IMultilingue
{
    private final JFrame desktop;
    private JPanel modificacionExpPanel;
    private JScrollPane modificacionExpScrollPane;
    private PanelDatosCrearExp datosPanel;
    private PanelExpAdminGerencia expAdminGerenciaPanel;
    private PanelInformacionNotarialCrearExp infNotariaPanel;
    private PanelDatosDeclaracion datosDeclaracionPanel;
    private PanelDatosPersonalesCrearExp presentadorDatosPerPanel;
    private PanelDatosNotificacionCrearExp datosNotifPanel;
    private PanelDireccion direccionPanel;
    //private PanelLocalizacionInterna localizacionInterPanel;
    private Expediente expediente;
    private Hashtable codigoNombreProvincia;
    private Hashtable codigoNombreMunicipio;
    private JButton guardarJButton;
    private JButton salirJButton;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame y el expediente que se va a mostrar y que puede ser modificado.
     *
     * @param desktop  JFrame
     * @param exp Expediente que puede ser modificado
     */
    public ModificarExpediente(final JFrame desktop, Expediente exp)
    {
        this.desktop= desktop;
        this.expediente= exp;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
        inicializaElementos();
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
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
        });
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ModificarExpediente.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        modificacionExpPanel= new JPanel();
        modificacionExpScrollPane= new JScrollPane();
        modificacionExpPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        modificacionExpPanel.setPreferredSize(new Dimension(1010,600));
        inicializaPanelesExp();
        inicializaPanelesDireccion();

        guardarJButton = new JButton();
        salirJButton = new JButton();

        guardarJButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.guardarJButton"));
        guardarJButton.setToolTipText(I18N.get("RegistroExpedientes",  "Catastro.RegistroExpedientes.ModificarExpediente.guardarJButton.hint"));

        salirJButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.salirJButton"));
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.salirJButton.hint"));

        modificacionExpPanel.add(guardarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 565, 120, 30));
        modificacionExpPanel.add(salirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 565, 120, 30));

        if(expediente.getIdEstado() > ConstantesRegistroExp.ESTADO_MODIFICADO)
            guardarJButton.setEnabled(false);

        modificacionExpScrollPane.setViewportView(modificacionExpPanel);
        getContentPane().add(modificacionExpScrollPane);
        inicializaEventos();        
        inicializaDatos();

    }

   /**
    * Inicializa los elementos del panel de datos del expediente.
    */
    private void inicializaPanelesExp()
    {
        datosPanel = new PanelDatosCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.datosPanel");
        datosPanel.setDesktop(desktop);
        datosPanel.setEditable(false);

        modificacionExpPanel.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 500, 110));

        expAdminGerenciaPanel= new PanelExpAdminGerencia("Catastro.RegistroExpedientes.ModificarExpediente.expAdminGerenciaPanel");
        expAdminGerenciaPanel.setEditable(false);
        modificacionExpPanel.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 125, 500, 90));

        infNotariaPanel= new PanelInformacionNotarialCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.infNotariaPanel");
        modificacionExpPanel.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 230, 500, 90));

        datosDeclaracionPanel= new PanelDatosDeclaracion("Catastro.RegistroExpedientes.ModificarExpediente.datosDeclaracionPanel");
        modificacionExpPanel.add(datosDeclaracionPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 330, 500, 205));

        if(expediente.getIdEstado() > ConstantesRegistroExp.ESTADO_MODIFICADO){
            datosDeclaracionPanel.setEditable(false);
            infNotariaPanel.setEditable(false);
        }
    }

   /**
    * Inicializa los elementos del panel de direccion del expedientes
    */
    private void inicializaPanelesDireccion()
    {
        presentadorDatosPerPanel= new PanelDatosPersonalesCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.presentadorDatosPerPanel");
        presentadorDatosPerPanel.setDesktop(desktop);

        datosNotifPanel= new PanelDatosNotificacionCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.datosNotifPanel");
        direccionPanel = new PanelDireccion("Catastro.RegistroExpedientes.ModificarExpediente.direccionPanel", true);
        //localizacionInterPanel = new PanelLocalizacionInterna("Catastro.RegistroExpedientes.ModificarExpediente.localizacionInterPanel");

        modificacionExpPanel.add(presentadorDatosPerPanel.getDatosPersonalesPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 5, 500, 82));
        modificacionExpPanel.add(datosNotifPanel.getDatosNotifPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 125, 500, 115));
        modificacionExpPanel.add(direccionPanel.getDatosDirPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 292, 500, 243));
        //modificacionExpPanel.add(localizacionInterPanel.getDatosLIPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 445, 500, 110));
        if(expediente.getIdEstado() > ConstantesRegistroExp.ESTADO_MODIFICADO){
            presentadorDatosPerPanel.setEditable(false);
            datosNotifPanel.setEditable(false);
            direccionPanel.setEditable(false);        
        }
        //direccionPanel.cargarTiposVias(getTiposViasBD());
        getCodigosNombresProvinciasBD();
        datosNotifPanel.cargaProvincias(codigoNombreProvincia);

    }

    /**
     * Asocia el evento para el comboBox de provicias, para que cargue los municipios de la provincia al cambiar esta.
     */
    private void inicializaEventos()
    {
        if(expediente.getIdEstado() < ConstantesRegistroExp.ESTADO_FINALIZADO){
            datosNotifPanel.getNombreProvinciaJCBox().addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent act)
                {
                    getCodigosNombresMucnicipiosBD();
                    datosNotifPanel.cargaMunicipios(codigoNombreMunicipio);
                }
            });
        }
    }

    /**
     * Devuelve el expediente creado
     *
     * @return Expediente el expediente creado.
     */
    public Expediente getExpediente()
    {
        return expediente;
    }

    /**
     * Devuelve el boton guardar.
     *
     * @return JButton guardarJButton
     */
    public JButton getGuardarJButton()
    {
        return guardarJButton;
    }

    /**
     * Devuelve el boton salir.
     *
     * @return JButton salirJButton
     */
    public JButton getSalirJButton()
    {
        return salirJButton;
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ModificarExpediente.titulo"));        
        datosPanel.renombrarComponentes();
        presentadorDatosPerPanel.renombrarComponentes();
        datosNotifPanel.renombrarComponentes();
        expAdminGerenciaPanel.renombrarComponentes();
        infNotariaPanel.renombrarComponentes();
        datosDeclaracionPanel.renombrarComponentes();
        direccionPanel.renombrarComponentes();
        //localizacionInterPanel.renombrarComponentes();
        guardarJButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.guardarJButton"));
        guardarJButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.guardarJButton.hint"));

        salirJButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.salirJButton"));
        salirJButton.setToolTipText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.ModificarExpediente.salirJButton.hint"));
    }

    /**
     * Introduce todos los datos que se van a mostrar en una hashtable y llama a los diferenctes paneles con la hash,
     * los cuales cogen los elementos que necesitar y los muestran.
     */
    private void inicializaDatos()
    {
       if(expediente!=null)
       {
            Hashtable hashDatos = new Hashtable();

            //Panel datos
            hashDatos.put(ConstantesRegistroExp.expedienteIdEstado, Estructuras.getListaEstadosExpediente().
                    getDomainNode(String.valueOf(expediente.getIdEstado())).getTerm(ConstantesRegistroExp.Locale));
            hashDatos.put(ConstantesRegistroExp.expedienteNumeroExpediente, String.valueOf(expediente.getNumeroExpediente()));
            hashDatos.put(ConstantesRegistroExp.expedienteTipoExpediente, expediente.getTipoExpediente().getCodigoTipoExpediente());
            hashDatos.put(ConstantesRegistroExp.expedienteFechaRegistro, expediente.getFechaRegistro());


            //Panel Expediente Administrativo de la Gerencia
            if(expediente.getAnnoExpedienteGerencia()!=null) //PREV-NOV
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
            //PREV-NOV if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!=0)
            if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!= null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, String.valueOf(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion,"");
            }

            //Panel Informacion Notarial
            if(expediente.getCodProvinciaNotaria()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodProvinciaNotaria, expediente.getCodProvinciaNotaria());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodProvinciaNotaria, "");
            }
            if(expediente.getCodPoblacionNotaria()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodPoblacionNotaria, expediente.getCodPoblacionNotaria());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodPoblacionNotaria, "");
            }
            if(expediente.getCodNotaria()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodNotaria, expediente.getCodNotaria());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodNotaria, "");
            }
            if(expediente.getAnnoProtocoloNotarial()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial, String.valueOf(expediente.getAnnoProtocoloNotarial()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial, "");
            }
            if(expediente.getProtocoloNotarial()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteProtocoloNotarial, expediente.getProtocoloNotarial());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteProtocoloNotarial, "");
            }

            //Panel datos de la declaracion
            if(expediente.getTipoDocumentoOrigenAlteracion()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion, expediente.getTipoDocumentoOrigenAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion, "");
            }
            if(expediente.getInfoDocumentoOrigenAlteracion()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion, expediente.getInfoDocumentoOrigenAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion, "");
            }
            if(expediente.getNumBienesInmueblesUrbanos()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos, String.valueOf(expediente.getNumBienesInmueblesUrbanos()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos, "");                
            }
            if(expediente.getNumBienesInmueblesRusticos()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos, String.valueOf(expediente.getNumBienesInmueblesRusticos()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos, "");
            }
            if(expediente.getNumBienesInmueblesCaractEsp()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp, String.valueOf(expediente.getNumBienesInmueblesCaractEsp()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp, "");
            }
            if(expediente.getCodigoDescriptivoAlteracion()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion, expediente.getCodigoDescriptivoAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion, "");
            }
            if(expediente.getDescripcionAlteracion()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.expedienteDescripcionAlteracion, expediente.getDescripcionAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.expedienteDescripcionAlteracion, "");
            }

            //Panel Datos Personales Presentador
            hashDatos.put(ConstantesRegistroExp.expedienteNifPresentador, expediente.getNifPresentador());
            hashDatos.put(ConstantesRegistroExp.expedienteNombreCompletoPresentador, expediente.getNombreCompletoPresentador());

            //Panel Datos Notificacion
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
            
            
            
            if (expediente.getDireccionPresentador().getCodigoPostal() != null){
            	hashDatos.put(ConstantesRegistroExp.direccionCodigoPostal, expediente.getDireccionPresentador().getCodigoPostal());
            }
            else{
            	hashDatos.put(ConstantesRegistroExp.direccionCodigoPostal, "");
            }
            if(expediente.getDireccionPresentador().getApartadoCorreos()!=0)
            {
                hashDatos.put(ConstantesRegistroExp.direccionApartadoCorreos, String.valueOf(expediente.getDireccionPresentador().getApartadoCorreos()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionApartadoCorreos,"");
            }

            //Panel Direccion
            if(expediente.getDireccionPresentador().getTipoVia()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionTipoVia, expediente.getDireccionPresentador().getTipoVia());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionTipoVia,"");
            }
            if(expediente.getDireccionPresentador().getNombreVia()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionNombreVia, expediente.getDireccionPresentador().getNombreVia());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionNombreVia, "");
            }
            if(expediente.getDireccionPresentador().getPrimerNumero()>0)
            {
                hashDatos.put(ConstantesRegistroExp.direccionPrimerNumero, String.valueOf(expediente.getDireccionPresentador().getPrimerNumero()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionPrimerNumero, "");                
            }
            if(expediente.getDireccionPresentador().getPrimeraLetra()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionPrimeraLetra, expediente.getDireccionPresentador().getPrimeraLetra());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionPrimeraLetra, "");                
            }
            if(expediente.getDireccionPresentador().getSegundoNumero()>0)
            {
                hashDatos.put(ConstantesRegistroExp.direccionSegundoNumero, String.valueOf(expediente.getDireccionPresentador().getSegundoNumero()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionSegundoNumero, "");
            }
            if(expediente.getDireccionPresentador().getSegundaLetra()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionSegundaLetra, expediente.getDireccionPresentador().getSegundaLetra());                
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionSegundaLetra, "");
            }
            if(expediente.getDireccionPresentador().getKilometro()>0)
            {
                hashDatos.put(ConstantesRegistroExp.direccionKilometro, String.valueOf((long)expediente.getDireccionPresentador().getKilometro()));
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionKilometro, "");
            }
            if(expediente.getDireccionPresentador().getDireccionNoEstructurada()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionDireccionNoEstructurada, expediente.getDireccionPresentador().getDireccionNoEstructurada());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionDireccionNoEstructurada, "");
            }

            //Panel Localizacion Interna
            if(expediente.getDireccionPresentador().getBloque()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionBloque, expediente.getDireccionPresentador().getBloque());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionBloque, "");
            }
            if(expediente.getDireccionPresentador().getEscalera()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionEscalera, expediente.getDireccionPresentador().getEscalera());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionEscalera, "");
            }
            if(expediente.getDireccionPresentador().getPlanta()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionPlanta, expediente.getDireccionPresentador().getPlanta());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionPlanta, "");
            }
            if(expediente.getDireccionPresentador().getPuerta()!=null)
            {
                hashDatos.put(ConstantesRegistroExp.direccionPuerta, expediente.getDireccionPresentador().getPuerta());
            }
            else
            {
                hashDatos.put(ConstantesRegistroExp.direccionPuerta, "");
            }

            datosPanel.inicializaDatos(hashDatos);
            expAdminGerenciaPanel.inicializaDatos(hashDatos);
            infNotariaPanel.inicializaDatos(hashDatos);
            datosDeclaracionPanel.inicializaDatos(hashDatos);
            presentadorDatosPerPanel.inicializaDatos(hashDatos);
            datosNotifPanel.inicializaDatos(hashDatos);
            direccionPanel.inicializaDatos(hashDatos);
            //localizacionInterPanel.inicializaDatos(hashDatos);
       }
    }

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres y los codigos
     * de las provincias. La consulta devuelver una hashtable con dos arrayList, un con key codigos y otro con key
     * nombres. En este caso el municipio del expediente se selecciona entre los resultados.
     */
    private void getCodigosNombresProvinciasBD()
    {
        try
        {
             codigoNombreProvincia= ConstantesRegistroExp.clienteCatastro.getCodigoNombreProvincia();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres y los codigos
     * de los municipios, para lo que se tiene que haber seleccionado una provincia, ya que busca los municipios de la
     * provincia seleccionada. La consulta devuelver una hashtable con dos arrayList, un con key codigos y otro con key
     * nombres. En este caso el nombre del municipio del expediente se selecciona.
     */
    private void getCodigosNombresMucnicipiosBD(){
        try
        {
        	ArrayList codigoProv = (ArrayList)codigoNombreProvincia.get("codigos");
            int provIndex=datosNotifPanel.getProvinciaSelect();
            if(provIndex>=0&&provIndex<codigoProv.size()){
	           	//System.out.println("Cargamos los municipios de la provincia con index: "+provIndex);
	            String codigoProvincia = (String)codigoProv.get(provIndex);
	            codigoNombreMunicipio= ConstantesRegistroExp.clienteCatastro.getCodigoNombreMunicipio(codigoProvincia);
            }//fin if
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//fin del método

    /**
     * Recopila todos los datos que se han introducido en los paneles que carga la clase. Se utiliza una hashtable para
     * llamar a todos los metodos de los paneles y recoger los datos. Tambien se comprueba en cada metodo si los
     * campos necesarios han sido introducidos. Con los datos se modifica los parametros del expediente que se han
     * modificado.
     *
     * @return boolean indica si la operacion ha finalizado bien o no.
     */
    public boolean recopilarDatos()
    {
        Hashtable hashDatos = new Hashtable();
        if((datosPanel.recopilaDatosPanel(hashDatos))&&(infNotariaPanel.recopilaDatosPanel(hashDatos))
                &&(datosDeclaracionPanel.recopilaDatosPanel(hashDatos))&&(presentadorDatosPerPanel.recopilaDatosPanel(hashDatos))
                &&(direccionPanel.recopilaDatosPanel(hashDatos))/*&&(localizacionInterPanel.recopilaDatosPanel(hashDatos))*/
                &&(datosNotifPanel.recopilaDatosPanel(hashDatos))&&(expAdminGerenciaPanel.recopilaDatosPanel(hashDatos)))
        {

            //Datos del PanelDatosCreaExp
            expediente.setNumeroExpediente((String)hashDatos.get(ConstantesRegistroExp.expedienteNumeroExpediente));
            expediente.setTipoExpediente(UtilRegistroExp.getTipoExpediente((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoExpediente)));
            expediente.setIdEstado(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteIdEstado)));
            expediente.setFechaRegistro((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro));

            //Datos del PanelexpAdminGerenciaPanel
            if(!hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia).equals(""))
            {
                // PREV-NOV expediente.setAnnoExpedienteGerencia(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia)));
                expediente.setAnnoExpedienteGerencia(Integer.valueOf((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia).equals(""))
            {
                expediente.setReferenciaExpedienteGerencia((String)hashDatos.get(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion).equals(""))
            {
                // PREV-NOV expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
                expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
            }

            //Datos del Panel InformacionNotarial
            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodProvinciaNotaria).equals(""))
            {
                expediente.setCodProvinciaNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodProvinciaNotaria)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodPoblacionNotaria).equals(""))
            {
                expediente.setCodPoblacionNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodPoblacionNotaria)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodNotaria).equals(""))
            {
                expediente.setCodNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodNotaria)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial).equals(""))
            {
                //PREV-NOV
                //expediente.setAnnoProtocoloNotarial(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial)));
                expediente.setAnnoProtocoloNotarial((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial));

            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteProtocoloNotarial).equals(""))
            {
                expediente.setProtocoloNotarial(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteProtocoloNotarial)));
            }

            //Datos del Panel Declaracion
            expediente.setTipoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion));
            expediente.setInfoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion));
            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos).equals(""))
            {
                expediente.setNumBienesInmueblesUrbanos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos).equals(""))
            {
                expediente.setNumBienesInmueblesRusticos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos)));
            }
            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp).equals(""))
            {
                expediente.setNumBienesInmueblesCaractEsp(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp)));
            }
            expediente.setCodigoDescriptivoAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion));
            expediente.setDescripcionAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteDescripcionAlteracion));

            //Datos del Panel datosPersonales
            expediente.setNifPresentador((String)hashDatos.get(ConstantesRegistroExp.expedienteNifPresentador));
            expediente.setNombreCompletoPresentador((String)hashDatos.get(ConstantesRegistroExp.expedienteNombreCompletoPresentador));

            //Creamos y completamos la direccion.
            DireccionLocalizacion dir = expediente.getDireccionPresentador();

            //Datos del panel DatosNotificacion
        
            ArrayList codigosProv = (ArrayList)codigoNombreProvincia.get("codigos");
            ArrayList codigosMunic = (ArrayList)codigoNombreMunicipio.get("codigos");
            dir.setProvinciaINE((String)codigosProv.get(datosNotifPanel.getProvinciaSelect()));
            dir.setMunicipioINE((String)codigosMunic.get(datosNotifPanel.getMunicipioSelect()));
            dir.setNombreProvincia((String)hashDatos.get(ConstantesRegistroExp.direccionNombreProvincia));
            dir.setNombreMunicipio((String)hashDatos.get(ConstantesRegistroExp.direccionNombreMunicipio));


            dir.setNombreEntidadMenor((String)hashDatos.get(ConstantesRegistroExp.direccionNombreEntidadMenor));
            dir.setCodigoPostal((String)hashDatos.get(ConstantesRegistroExp.direccionCodigoPostal));

            if(!hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos).equals(""))
            {
                dir.setApartadoCorreos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos)));
            }

            //Datos del panel Direccion
            dir.setTipoVia((String)hashDatos.get(ConstantesRegistroExp.direccionTipoVia));
            dir.setNombreVia((String)hashDatos.get(ConstantesRegistroExp.direccionNombreVia));
            if(!hashDatos.get(ConstantesRegistroExp.direccionPrimerNumero).equals(""))
            {
                dir.setPrimerNumero(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.direccionPrimerNumero)));
            }
            dir.setPrimeraLetra(((String)hashDatos.get(ConstantesRegistroExp.direccionPrimeraLetra)));
            if(!hashDatos.get(ConstantesRegistroExp.direccionSegundoNumero).equals(""))
            {
                dir.setSegundoNumero(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.direccionSegundoNumero)));
            }
            dir.setSegundaLetra(((String)hashDatos.get(ConstantesRegistroExp.direccionSegundaLetra)));
            if(!hashDatos.get(ConstantesRegistroExp.direccionKilometro).equals(""))
            {
                dir.setKilometro(Double.parseDouble((String)hashDatos.get(ConstantesRegistroExp.direccionKilometro)));
            }
            dir.setDireccionNoEstructurada((String)hashDatos.get(ConstantesRegistroExp.direccionDireccionNoEstructurada));

            //Datos del panel Localizacion interna
            dir.setBloque((String)hashDatos.get(ConstantesRegistroExp.direccionBloque));
            dir.setEscalera((String)hashDatos.get(ConstantesRegistroExp.direccionEscalera));
            dir.setPlanta((String)hashDatos.get(ConstantesRegistroExp.direccionPlanta));
            dir.setPuerta((String)hashDatos.get(ConstantesRegistroExp.direccionPuerta));

            return true;
        }
        return false;
    }
}
