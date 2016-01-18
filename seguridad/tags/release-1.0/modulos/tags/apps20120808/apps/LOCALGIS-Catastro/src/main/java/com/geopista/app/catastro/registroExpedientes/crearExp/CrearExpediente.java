package com.geopista.app.catastro.registroExpedientes.crearExp;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosDeclaracion;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosNotificacionCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosPersonalesCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDireccion;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelExpAdminGerencia;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelInformacionNotarialCrearExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.vividsolutions.jump.I18N;


/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite al usuario crear un nuevo expediente y lo guarda
 * en base de datos. A partir de esta pantalla se puede ir a asociar parcelas o a gestion del expediente.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */

public class CrearExpediente extends JInternalFrame implements IMultilingue
{
    private JFrame desktop;
    private JPanel crearExpedientePanel;
    private JScrollPane crearExpedienteScrollPane;
    private JTabbedPane crearExpedienteTabPanel;
    private JPanel datosPanelTodo;
    private PanelDatosCrearExp datosPanel;
    private PanelExpAdminGerencia expAdminGerenciaPanel;
    private PanelInformacionNotarialCrearExp infNotariaPanel;
    private PanelDatosDeclaracion datosDeclaracionPanel;
    private PanelDatosPersonalesCrearExp presentadorDatosPerPanel;
    private PanelDatosNotificacionCrearExp datosNotifPanel;
    private PanelDireccion direccionPanel;
    //private PanelLocalizacionInterna localizacionInterPanel;
    private JPanel presentadorPanel;
    private JButton botonGuardar;
    private JButton botonSiguiente;
    private Expediente expediente;
    private Hashtable codigoNombreProvincia;
    private Hashtable codigoNombreMunicipio;
    private int height;
    private String codigoDGCMunicipio = null;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame
     *
     * @param desktop  JFrame
     * @param height largo de la ventana
     */
    public CrearExpediente(final JFrame desktop, int height)
    {
        this.desktop = desktop;
        this.expediente = null;
        this.height=height;
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
                        "Catastro.RegistroExpedientes.CrearExpediente.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        crearExpedienteTabPanel= new JTabbedPane();
        crearExpedientePanel= new JPanel();
        crearExpedienteScrollPane= new JScrollPane();
        botonGuardar= new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.botonGuardar"));
        botonSiguiente= new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.botonSiguente"));
        if(ConstantesRegistroExp.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
        	botonSiguiente.setEnabled(false);
        }
        else{
        	botonSiguiente.setEnabled(true);
        }
        
        inicializaDatos();
        inicializaPresentador();

        crearExpedienteTabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        crearExpedienteTabPanel.addTab(UtilRegistroExp.annadirAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.crearExpedienteTabPanel.Datos")),UtilRegistroExp.iconoExpediente ,datosPanelTodo);
        crearExpedienteTabPanel.addTab(UtilRegistroExp.annadirAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.crearExpedienteTabPanel.Presentador")),UtilRegistroExp.iconoPersona ,presentadorPanel);
        crearExpedientePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        crearExpedientePanel.add(crearExpedienteTabPanel,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 575));
        crearExpedientePanel.add(botonGuardar,new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 585, 85, 25));
        crearExpedientePanel.add(botonSiguiente,new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 585, 85, 25));
        crearExpedientePanel.setPreferredSize(new Dimension(550,600));

        crearExpedienteScrollPane.setViewportView(crearExpedientePanel);
        getContentPane().add(crearExpedienteScrollPane);
        if(height>710)
        {
            height=710;
        }
        setSize(new Dimension(580,height));
        this.setMaximizable(false);
        this.setMaximumSize(new Dimension(580,660));
    }

   /**
    * Inicializa el panel de datos, la primera pestaña.
    */
    private void inicializaDatos()
    {
        datosPanelTodo= new JPanel();
        datosPanelTodo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        datosPanel = new PanelDatosCrearExp("Catastro.RegistroExpedientes.CrearExpediente.datosPanel");
        datosPanel.setDesktop(desktop);
        //datosPanelTodo.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 500, 130));
        datosPanelTodo.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 5, 500, 111));
        expAdminGerenciaPanel= new PanelExpAdminGerencia("Catastro.RegistroExpedientes.CrearExpediente.expAdminGerenciaPanel");
        //datosPanelTodo.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 145, 500, 110));
        datosPanelTodo.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 124, 500, 89));
        infNotariaPanel= new PanelInformacionNotarialCrearExp("Catastro.RegistroExpedientes.CrearExpediente.infNotariaPanel");
        //datosPanelTodo.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 255, 500, 100));
        datosPanelTodo.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 221, 500, 94));
        datosDeclaracionPanel= new PanelDatosDeclaracion("Catastro.RegistroExpedientes.CrearExpediente.datosDeclaracionPanel");
        datosPanelTodo.add(datosDeclaracionPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 323, 500, 205));
    }

   /**
    * Inicializa el panel de datos del presentador, la segunda pestaña.
    */
    private void inicializaPresentador()
    {
        presentadorPanel= new JPanel();
        presentadorDatosPerPanel= new PanelDatosPersonalesCrearExp("Catastro.RegistroExpedientes.CrearExpediente.presentadorDatosPerPanel");
        presentadorDatosPerPanel.setDesktop(desktop);
        datosNotifPanel= new PanelDatosNotificacionCrearExp("Catastro.RegistroExpedientes.CrearExpediente.datosNotifPanel");
        direccionPanel = new PanelDireccion("Catastro.RegistroExpedientes.CrearExpediente.direccionPanel", true);
        //localizacionInterPanel = new PanelLocalizacionInterna("Catastro.RegistroExpedientes.CrearExpediente.localizacionInterPanel");
        presentadorPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        //presentadorPanel.add(presentadorDatosPerPanel.getDatosPersonalesPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 500, 110));
        presentadorPanel.add(presentadorDatosPerPanel.getDatosPersonalesPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 5, 500, 85));
        presentadorPanel.add(datosNotifPanel.getDatosNotifPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 118, 500, 117));
        presentadorPanel.add(direccionPanel.getDatosDirPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 275, 500, 253));
        //presentadorPanel.add(localizacionInterPanel.getDatosLIPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 455, 500, 110));
        //direccionPanel.cargarTiposVias(getTiposViasBD());
        getCodigosNombresProvinciasBD();
        datosNotifPanel.cargaProvincias(codigoNombreProvincia);
        datosNotifPanel.getNombreProvinciaJCBox().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent act)
            {
                getCodigosNombresMucnicipiosBD();
                datosNotifPanel.cargaMunicipios(codigoNombreMunicipio);
            }
        });
    }

    /**
     * Devuelve el boton siguiente.
     *
     * @return JButton botonSiguiente
     */
    public JButton getBotonSiguiente()
    {
        return botonSiguiente;
    }

    /**
     * Devuelve el boton guardar.
     *
     * @return JButton botonGuardar
     */
    public JButton getBotonGuardar()
    {
        return botonGuardar;
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
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.titulo"));
        botonGuardar.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.botonGuardar"));
        botonSiguiente.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.botonSiguente"));
        crearExpedienteTabPanel.setTitleAt(0,UtilRegistroExp.annadirAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.crearExpedienteTabPanel.Datos")));
        datosPanel.renombrarComponentes();
        presentadorDatosPerPanel.renombrarComponentes();
        datosNotifPanel.renombrarComponentes();
        expAdminGerenciaPanel.renombrarComponentes();
        infNotariaPanel.renombrarComponentes();
        datosDeclaracionPanel.renombrarComponentes();
        direccionPanel.renombrarComponentes();
        //localizacionInterPanel.renombrarComponentes();
        crearExpedienteTabPanel.setTitleAt(1,UtilRegistroExp.annadirAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CrearExpediente.crearExpedienteTabPanel.Presentador")));
    }

    /**
     * Recopila todos los datos que se han introducido en los paneles que carga la clase. Se utiliza una hashtable para
     * llamar a todos los metodos de los paneles y recoger los datos. Tambien se comprueba en cada metodo si los
     * campos necesarios han sido introducidos.
     *
     * @return boolean indica si la operacion ha finalizado bien o no.
     */
    public boolean recopilarDatos() {
        Hashtable hashDatos = new Hashtable();

        if((datosPanel.recopilaDatosPanel(hashDatos))
                && (infNotariaPanel.recopilaDatosPanel(hashDatos))
                && (datosDeclaracionPanel.recopilaDatosPanel(hashDatos))
                && (presentadorDatosPerPanel.recopilaDatosPanel(hashDatos))
                && (direccionPanel.recopilaDatosPanel(hashDatos))/*&&(localizacionInterPanel.recopilaDatosPanel(hashDatos))*/
                && (datosNotifPanel.recopilaDatosPanel(hashDatos))
                && (expAdminGerenciaPanel.recopilaDatosPanel(hashDatos)))
        {

            expediente = new Expediente();

            //Datos del PanelDatosCreaExp
            expediente.setNumeroExpediente((String)hashDatos.get(ConstantesRegistroExp.expedienteNumeroExpediente));
            expediente.setTipoExpediente(UtilRegistroExp.getTipoExpediente((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoExpediente)));
            expediente.setIdEstado(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteIdEstado)));
            expediente.setFechaRegistro((Date)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro));

            //Datos del PanelexpAdminGerenciaPanel
            if(!hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia).equals(""))
                //PREV-NOV expediente.setAnnoExpedienteGerencia(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia)));
                expediente.setAnnoExpedienteGerencia(Integer.valueOf((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia).equals(""))
                expediente.setReferenciaExpedienteGerencia((String)hashDatos.get(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion).equals(""))
                //PREV-NOV expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
                expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));    


            //Datos del Panel InformacionNotarial
            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodProvinciaNotaria).equals(""))
                expediente.setCodProvinciaNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodProvinciaNotaria)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodPoblacionNotaria).equals(""))
                expediente.setCodPoblacionNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodPoblacionNotaria)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteCodNotaria).equals(""))
                expediente.setCodNotaria(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteCodNotaria)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial).equals(""))
                //PREV-NOV expediente.setAnnoProtocoloNotarial(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial)));
                expediente.setAnnoProtocoloNotarial((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial));
            if(!hashDatos.get(ConstantesRegistroExp.expedienteProtocoloNotarial).equals(""))
                expediente.setProtocoloNotarial(
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteProtocoloNotarial)));


            //Datos del Panel Declaracion
            expediente.setTipoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteTipoDocumentoOrigenAlteracion));
            expediente.setInfoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteInfoDocumentoOrigenAlteracion));
            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos).equals(""))
                expediente.setNumBienesInmueblesUrbanos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesUrbanos)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos).equals(""))
                expediente.setNumBienesInmueblesRusticos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesRusticos)));

            if(!hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp).equals(""))
                expediente.setNumBienesInmueblesCaractEsp(Integer.parseInt
                        ((String)hashDatos.get(ConstantesRegistroExp.expedienteNumBienesInmueblesCaractEsp)));

            expediente.setCodigoDescriptivoAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoDescriptivoAlteracion));
            expediente.setDescripcionAlteracion((String)hashDatos.get(ConstantesRegistroExp.expedienteDescripcionAlteracion));

            //Datos del Panel datosPersonales
            expediente.setNifPresentador((String)hashDatos.get(ConstantesRegistroExp.expedienteNifPresentador));
            expediente.setNombreCompletoPresentador((String)hashDatos.get(ConstantesRegistroExp.expedienteNombreCompletoPresentador));

            //Creamos y completamos la direccion.
            DireccionLocalizacion dir = new DireccionLocalizacion();

            //Datos del panel DatosNotificacion
            ArrayList codigosProv = (ArrayList)codigoNombreProvincia.get("codigos");
            ArrayList codigosMunic = (ArrayList)codigoNombreMunicipio.get("codigos");
            dir.setProvinciaINE((String)codigosProv.get(datosNotifPanel.getProvinciaSelect()));
            dir.setMunicipioINE((String)codigosMunic.get(datosNotifPanel.getMunicipioSelect()));
            
            String codigoMunicipioDGC = getCodigoDGCMunicipiosBD(dir.getMunicipioINE(), dir.getProvinciaINE());
            if (codigoMunicipioDGC!=null && !codigoMunicipioDGC.equals("")){
            	dir.setCodigoMunicipioDGC(codigoMunicipioDGC);
            }
            else{
            	dir.setCodigoMunicipioDGC("001");
            }

            dir.setNombreProvincia((String)hashDatos.get(ConstantesRegistroExp.direccionNombreProvincia));
            dir.setNombreMunicipio((String)hashDatos.get(ConstantesRegistroExp.direccionNombreMunicipio));
            dir.setNombreEntidadMenor((String)hashDatos.get(ConstantesRegistroExp.direccionNombreEntidadMenor));
            dir.setCodigoPostal((String)hashDatos.get(ConstantesRegistroExp.direccionCodigoPostal));

            if(!hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos).equals(""))
                dir.setApartadoCorreos(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos)));

            //Datos del panel Direccion
            dir.setTipoVia((String)hashDatos.get(ConstantesRegistroExp.direccionTipoVia));
            dir.setNombreVia((String)hashDatos.get(ConstantesRegistroExp.direccionNombreVia));
            if(!hashDatos.get(ConstantesRegistroExp.direccionPrimerNumero).equals(""))
                dir.setPrimerNumero(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.direccionPrimerNumero)));

            dir.setPrimeraLetra(((String)hashDatos.get(ConstantesRegistroExp.direccionPrimeraLetra)));
            if(!hashDatos.get(ConstantesRegistroExp.direccionSegundoNumero).equals(""))
                dir.setSegundoNumero(Integer.parseInt((String)hashDatos.get(ConstantesRegistroExp.direccionSegundoNumero)));

            dir.setSegundaLetra(((String)hashDatos.get(ConstantesRegistroExp.direccionSegundaLetra)));
            if(!hashDatos.get(ConstantesRegistroExp.direccionKilometro).equals(""))
                dir.setKilometro(Double.parseDouble((String)hashDatos.get(ConstantesRegistroExp.direccionKilometro)));

            dir.setDireccionNoEstructurada((String)hashDatos.get(ConstantesRegistroExp.direccionDireccionNoEstructurada));

            //Datos del panel Localizacion interna
            dir.setBloque((String)hashDatos.get(ConstantesRegistroExp.direccionBloque));
            dir.setEscalera((String)hashDatos.get(ConstantesRegistroExp.direccionEscalera));
            dir.setPlanta((String)hashDatos.get(ConstantesRegistroExp.direccionPlanta));
            dir.setPuerta((String)hashDatos.get(ConstantesRegistroExp.direccionPuerta));

            //Metemos la Direccion
            expediente.setDireccionPresentador(dir);

            expediente.setIdMunicipio(ConstantesRegistroExp.IdMunicipio);
            expediente.setReferenciaExpedienteAdminOrigen(expediente.getNumeroExpediente());
            return true;
        }
        return false;
    }

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los tipos de vias
     * para mostrarselas al usuario y que elija. Esto se hace porque al guardar se compara la calle y el tipo de via
     * y si no es correcto se avisa. Asi el usuario el tipo de via lo selecciona de un ComboBox.
     *
     * @return ArrayList con los valores de los tipos de vias.
     */
    /*  
    private ArrayList getTiposViasBD()
    {
        try
        {
            return (ArrayList)ConstantesRegistroExp.clienteCatastro.getTiposVias();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    } */

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres y los codigos
     * de las provincias. La consulta devuelver una hashtable con dos arrayList, un con key codigos y otro con key
     * nombres.
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
     * nombres.
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
    
    private String getCodigoDGCMunicipiosBD(String codigoINE, String codigoProvincia){
    	
    	String codigoMunicipioDGC = null;
    	
        try
        {
        	if (codigoINE != null && !codigoINE.equals("") && codigoProvincia != null && !codigoProvincia.equals("")){
	            codigoDGCMunicipio= ConstantesRegistroExp.clienteCatastro.getCodigoDGCMunicipio(codigoINE, codigoProvincia);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return codigoMunicipioDGC;
    }


}
