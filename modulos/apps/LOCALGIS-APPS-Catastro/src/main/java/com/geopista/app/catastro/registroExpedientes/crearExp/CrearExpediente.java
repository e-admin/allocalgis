/**
 * CrearExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Escalera;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Planta;
import com.geopista.app.catastro.model.beans.Puerta;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosDeclaracion;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosNotificacionCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosPersonalesCrearExp;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDireccion;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelExpAdminGerencia;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelInformacionNotarialCrearExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
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
    private Hashtable codigoDigitoControlDni;
    private ArrayList lstEntidadGeneradora;
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
        
        if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
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
        crearExpedientePanel.add(crearExpedienteTabPanel,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 595));
        crearExpedientePanel.add(botonGuardar,new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 600, 85, 25));
        crearExpedientePanel.add(botonSiguiente,new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 600, 85, 25));
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
        datosPanelTodo.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 5, 500, 153));
        expAdminGerenciaPanel= new PanelExpAdminGerencia("Catastro.RegistroExpedientes.CrearExpediente.expAdminGerenciaPanel");
        //datosPanelTodo.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 145, 500, 110));
        datosPanelTodo.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 158, 500, 89));
        infNotariaPanel= new PanelInformacionNotarialCrearExp("Catastro.RegistroExpedientes.CrearExpediente.infNotariaPanel");
        //datosPanelTodo.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 255, 500, 100));
        datosPanelTodo.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 248, 500, 94));
        datosDeclaracionPanel= new PanelDatosDeclaracion("Catastro.RegistroExpedientes.CrearExpediente.datosDeclaracionPanel");
        datosPanelTodo.add(datosDeclaracionPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 343, 500, 205));
        
        
        getEntidadGeneradoraBD();
        datosPanel.cargarDatosEntidadGeneradora(lstEntidadGeneradora);
        
        // cuando se selecciona un Cod en el combo de codigo descriptivo de alteracion se selecciona
        // la descripción en el otro combo de codigo descriptivo
        datosDeclaracionPanel.getCodCodigoDescriptivoAltJCBox().addActionListener(new ActionListener() 
        {
			
			public void actionPerformed(ActionEvent e) {
				String valorCodCodigoDescriptivo = (String)datosDeclaracionPanel.getCodCodigoDescriptivoAltJCBox().getSelectedItem();
				if(!valorCodCodigoDescriptivo.equals("")){
					datosDeclaracionPanel.getCodigoDescriptivoAltJCBox().setSelectedPatron(valorCodCodigoDescriptivo);
				}
				else{
					datosDeclaracionPanel.getCodigoDescriptivoAltJCBox().setSelectedIndex(0);
					
				}
				
			}
		});
        
        // cuando se selecciona una descripcion en el combo de codigo descriptivo de alteracion se selecciona
        // el codigo en el otro combo de codigo descriptivo
        datosDeclaracionPanel.getCodigoDescriptivoAltJCBox().addActionListener(new ActionListener() 
        {
			
			public void actionPerformed(ActionEvent e) {
				String valorDescripcionCodigoDescriptivo = (String)datosDeclaracionPanel.getCodigoDescriptivoAltJCBox().getSelectedPatron();
				if(valorDescripcionCodigoDescriptivo!= null && !valorDescripcionCodigoDescriptivo.equals("")){
					datosDeclaracionPanel.getCodCodigoDescriptivoAltJCBox().setSelectedItem(valorDescripcionCodigoDescriptivo);
				}
				else{
					datosDeclaracionPanel.getCodCodigoDescriptivoAltJCBox().setSelectedIndex(0);
					
				}
			}
		});

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
         presentadorPanel.add(datosNotifPanel.getDatosNotifPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 115, 500, 117));
         presentadorPanel.add(direccionPanel.getDatosDirPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 258, 500, 270));
         //presentadorPanel.add(localizacionInterPanel.getDatosLIPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 455, 500, 110));
         //direccionPanel.cargarTiposVias(getTiposViasBD());
         
         getCodigosDigitoControlDniBD();
         presentadorDatosPerPanel.setDigitoContolDniHash(codigoDigitoControlDni);
         
         
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
         
         presentadorDatosPerPanel.getTitularesButton().addActionListener(new ActionListener()
         {
             public void actionPerformed(java.awt.event.ActionEvent evt)
             {
            	 
            	presentadorDatosPerPanel.busquedaTitularesButtonActionPerformed();
             	
             	if(presentadorDatosPerPanel.getDatosPersonales() != null){
 	            	// se actualizan los datos del panel de direccion
 	            	direccionPanel.getLocalizacionInterPanel().getBloqueJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getBloque());
 	            	direccionPanel.getPrimerNumeroJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getPrimerNumero());
 	            	direccionPanel.getPrimeraLetraJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getPrimeraLetra());
 	            	direccionPanel.getSegundoNumeroJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getSegundoNumero());
 	            	direccionPanel.getSegundaLetraJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getSegundoLetra());
 	            	direccionPanel.getKilometroJTField().setText(String.valueOf(presentadorDatosPerPanel.getDatosPersonales().getKilometro().intValue()));
 	            	direccionPanel.getDirNoEstrucJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getDireccionNoEstrucutrada());
             	
             	
 	            	ArrayList lstEscalera = direccionPanel.getLocalizacionInterPanel().getLstEscalera();
 	            	for(int h=0; h<lstEscalera.size();h++){
 	            		Escalera escalera = (Escalera)lstEscalera.get(h);
 	            		if(escalera.getPatron().equals(presentadorDatosPerPanel.getDatosPersonales().getEscalera())){
 	            			direccionPanel.getLocalizacionInterPanel().getjComboBoxEscalera().setSelectedIndex(h);
 	            		}
 	            	}
 	            	
 	            	ArrayList lstPlanta = direccionPanel.getLocalizacionInterPanel().getLstPlanta();
 	            	for(int h=0; h<lstPlanta.size();h++){
 	            		Planta planta = (Planta)lstPlanta.get(h);
 	            		if(planta.getPatron().equals(presentadorDatosPerPanel.getDatosPersonales().getPlanta())){
 	            			direccionPanel.getLocalizacionInterPanel().getjComboBoxPlanta().setSelectedIndex(h);
 	            		}
 	            	}
 	            	
 	            	ArrayList lstPuerta = direccionPanel.getLocalizacionInterPanel().getLstPuerta();
 	            	for(int h=0; h<lstPuerta.size();h++){
 	            		Puerta puerta = (Puerta)lstPuerta.get(h);
 	            		if(puerta.getPatron().equals(presentadorDatosPerPanel.getDatosPersonales().getPuerta())){
 	            			direccionPanel.getLocalizacionInterPanel().getjComboBoxPuerta().setSelectedIndex(h);
 	            		}
 	            	}
 	            	
             	
 	            	// se actualizan los datos del panel de notificacion
 	            	datosNotifPanel.getCodigoPostJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getCodigo_postal());
 	            	String apartadoCorreos = String.valueOf(presentadorDatosPerPanel.getDatosPersonales().getApartado_correos().intValue());
 	            	if(!apartadoCorreos.equalsIgnoreCase("-1")){
 	            		datosNotifPanel.getApartadoCorreoJTField().setText(String.valueOf(presentadorDatosPerPanel.getDatosPersonales().getApartado_correos().intValue()));
 	            	}
 	            	else{
 	            		datosNotifPanel.getApartadoCorreoJTField().setText("");
 	            	}
 	            	datosNotifPanel.getNombreEntidadMenorJTField().setText(presentadorDatosPerPanel.getDatosPersonales().getEntidadMenor());
             	
 	            	//selecionamos la provincia del combo
 	            	String nombreProvinciaSeleccionado = "";
 	            	ArrayList idComboProvincia = (ArrayList)codigoNombreProvincia.get("codigos");
 	            	ArrayList nombresComboProvincia = (ArrayList)codigoNombreProvincia.get("nombres");
 	            	for(int j=0; j < idComboProvincia.size();j++){
 	            		String idProvincia = (String)idComboProvincia.get(j);
 	            		
 	            		if (idProvincia.equals(String.valueOf(presentadorDatosPerPanel.getDatosPersonales().getCodigoProvincia()))){
 	            			//datosNotifPanel.getNombreProvinciaJCBox().setSelectedIndex(j);
 	            			nombreProvinciaSeleccionado = (String)nombresComboProvincia.get(j);
 	            		}
 	            			
 	            	}
 	            	
 	            	for (int j=0; j<datosNotifPanel.getNombreProvinciaJCBox().getItemCount(); j++){
 	            		
 	            		if(nombreProvinciaSeleccionado.equals(datosNotifPanel.getNombreProvinciaJCBox().getItemAt(j))){
 	            			datosNotifPanel.getNombreProvinciaJCBox().setSelectedIndex(j);
 	            		}
 	            	}
 	            	
 	            	
 	            	getCodigosNombresMucnicipiosBD();
 	            	
 	            	// se selecciona el municipio en el combo
 	            	ArrayList listCodigos = (ArrayList)codigoNombreMunicipio.get("codigos");
 					ArrayList listNombres = (ArrayList)codigoNombreMunicipio.get("nombres");
 					ArrayList listId = (ArrayList)codigoNombreMunicipio.get("id");
 					Integer id_municipio = null;
 					for(int i=0; i<listCodigos.size();i++){
 						Integer codigo = new Integer((String)listCodigos.get(i));
 						if (codigo.intValue() == presentadorDatosPerPanel.getDatosPersonales().getCodigoMunicipio().intValue()){
 							datosNotifPanel.getNombreMunicipioJCBoxJTField().setSelectedIndex(i);
 						
 							id_municipio = (Integer)listId.get(i);
 						}
 					}
             	
 					DireccionLocalizacion direccionLocalizacion = getViaMunicipios(id_municipio, presentadorDatosPerPanel.getDatosPersonales().getCodigoVia());
 					direccionPanel.getNombreViaJTField().setText(direccionLocalizacion.getNombreVia());
 					direccionPanel.getTipoViaJCBox().setSelectedPatron(direccionLocalizacion.getTipoVia());
             	
 				// se selecciona el la via		 
 					if(direccionLocalizacion.getCodigoVia() == -1){
 						direccionPanel.getCodigoViaJTField().setText("");
 					}
 					else{
 						direccionPanel.getCodigoViaJTField().setText(String.valueOf(direccionLocalizacion.getCodigoVia()));
 					}
 					
             	}
                                
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
            expediente.setNumeroExpediente((String)hashDatos.get(ConstantesCatastro.expedienteNumeroExpediente));
            expediente.setTipoExpediente(UtilRegistroExp.getTipoExpediente((String)hashDatos.get(ConstantesCatastro.expedienteTipoExpediente)));
            expediente.setIdEstado(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteIdEstado)));
            expediente.setFechaRegistro((Date)hashDatos.get(ConstantesCatastro.expedienteFechaRegistro));
            expediente.setTipoTramitaExpSitFinales((Boolean)hashDatos.get(ConstantesCatastro.expedienteTipoTramitacion));
            expediente.setEntidadGeneradora((EntidadGeneradora)hashDatos.get(ConstantesCatastro.expedienteEntidadGeneradora));
            
            //Datos del PanelexpAdminGerenciaPanel
            if(!hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia).equals(""))
                //PREV-NOV expediente.setAnnoExpedienteGerencia(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia)));
                expediente.setAnnoExpedienteGerencia(Integer.valueOf((String)hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia)));

            if(!hashDatos.get(ConstantesCatastro.expedienteReferenciaExpedienteGerencia).equals(""))
                expediente.setReferenciaExpedienteGerencia((String)hashDatos.get(ConstantesCatastro.expedienteReferenciaExpedienteGerencia));

            if(!hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion).equals(""))
                //PREV-NOV expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
                expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf((String)hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));    


            //Datos del Panel InformacionNotarial
            if(!hashDatos.get(ConstantesCatastro.expedienteCodProvinciaNotaria).equals(""))
                expediente.setCodProvinciaNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodProvinciaNotaria)));

            if(!hashDatos.get(ConstantesCatastro.expedienteCodPoblacionNotaria).equals(""))
                expediente.setCodPoblacionNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodPoblacionNotaria)));

            if(!hashDatos.get(ConstantesCatastro.expedienteCodNotaria).equals(""))
                expediente.setCodNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodNotaria)));

            if(!hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial).equals(""))
                //PREV-NOV expediente.setAnnoProtocoloNotarial(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial)));
                expediente.setAnnoProtocoloNotarial((String)hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial));
            if(!hashDatos.get(ConstantesCatastro.expedienteProtocoloNotarial).equals(""))
                expediente.setProtocoloNotarial(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteProtocoloNotarial)));


            //Datos del Panel Declaracion
            expediente.setTipoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion));
            expediente.setInfoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion));
            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos).equals(""))
                expediente.setNumBienesInmueblesUrbanos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos)));

            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesRusticos).equals(""))
                expediente.setNumBienesInmueblesRusticos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesRusticos)));

            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp).equals(""))
                expediente.setNumBienesInmueblesCaractEsp(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp)));

            expediente.setCodigoDescriptivoAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion));
            expediente.setDescripcionAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteDescripcionAlteracion));

            //Datos del Panel datosPersonales
            expediente.setNifPresentador((String)hashDatos.get(ConstantesCatastro.expedienteNifPresentador)+
            							(String)hashDatos.get(ConstantesCatastro.expedienteDigitoControlNifPresentador));
            expediente.setNombreCompletoPresentador((String)hashDatos.get(ConstantesCatastro.expedienteNombreCompletoPresentador));

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

            dir.setNombreProvincia((String)hashDatos.get(ConstantesCatastro.direccionNombreProvincia));
            dir.setNombreMunicipio((String)hashDatos.get(ConstantesCatastro.direccionNombreMunicipio));
            dir.setNombreEntidadMenor((String)hashDatos.get(ConstantesCatastro.direccionNombreEntidadMenor));
            dir.setCodigoPostal((String)hashDatos.get(ConstantesCatastro.direccionCodigoPostal));

            if(!hashDatos.get(ConstantesCatastro.direccionApartadoCorreos).equals(""))
                dir.setApartadoCorreos(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.direccionApartadoCorreos)));

            //Datos del panel Direccion
            if(!hashDatos.get(ConstantesCatastro.direccionCodigoVia).equals(""))
            	dir.setCodigoVia(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.direccionCodigoVia)));
 
            dir.setTipoVia((String)hashDatos.get(ConstantesCatastro.direccionTipoVia));
            dir.setNombreVia((String)hashDatos.get(ConstantesCatastro.direccionNombreVia));
            if(!hashDatos.get(ConstantesCatastro.direccionPrimerNumero).equals(""))
                dir.setPrimerNumero(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.direccionPrimerNumero)));

            dir.setPrimeraLetra(((String)hashDatos.get(ConstantesCatastro.direccionPrimeraLetra)));
            if(!hashDatos.get(ConstantesCatastro.direccionSegundoNumero).equals(""))
                dir.setSegundoNumero(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.direccionSegundoNumero)));

            dir.setSegundaLetra(((String)hashDatos.get(ConstantesCatastro.direccionSegundaLetra)));
            if(!hashDatos.get(ConstantesCatastro.direccionKilometro).equals(""))
                dir.setKilometro(Double.parseDouble((String)hashDatos.get(ConstantesCatastro.direccionKilometro)));

            dir.setDireccionNoEstructurada((String)hashDatos.get(ConstantesCatastro.direccionDireccionNoEstructurada));

            //Datos del panel Localizacion interna
            dir.setBloque((String)hashDatos.get(ConstantesCatastro.direccionBloque));
            dir.setEscalera((String)hashDatos.get(ConstantesCatastro.direccionEscalera));
            dir.setPlanta((String)hashDatos.get(ConstantesCatastro.direccionPlanta));
            dir.setPuerta((String)hashDatos.get(ConstantesCatastro.direccionPuerta));

            //Metemos la Direccion
            expediente.setDireccionPresentador(dir);

            expediente.setIdMunicipio(ConstantesCatastro.IdMunicipio);
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
            return (ArrayList)ConstantesCatastro.clienteCatastro.getTiposVias();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    } */

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
    
    
    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres
     * de la entidades generadoras. 
     * La consulta devuelve una ArrayList de objetos EntidadGeneradora
     */
    private void getEntidadGeneradoraBD()
    {
        try
        {
        	lstEntidadGeneradora = ConstantesRegExp.clienteCatastro.getEntidadGeneradora();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres y los codigos
     * de las provincias. La consulta devuelver una hashtable con dos arrayList, un con key codigos y otro con key
     * nombres.
     */
    private void getCodigosNombresProvinciasBD()
    {
        try
        {
             codigoNombreProvincia= ConstantesRegExp.clienteCatastro.getCodigoNombreProvincia();
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
	            codigoNombreMunicipio= ConstantesRegExp.clienteCatastro.getCodigoNombreMunicipio(codigoProvincia);
            }//fin if
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//fin del método
    
    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres 
     * de la via
     */
    private DireccionLocalizacion getViaMunicipios(Integer idMunicipio, Integer codigoVia){
    	DireccionLocalizacion dirLocalizacion = null;
        try
        {
        	dirLocalizacion = ConstantesRegExp.clienteCatastro.getVia(idMunicipio,codigoVia);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dirLocalizacion;
    }//fin del método
    
    private String getCodigoDGCMunicipiosBD(String codigoINE, String codigoProvincia){
    	
    	String codigoMunicipioDGC = null;
    	
        try
        {
        	if (codigoINE != null && !codigoINE.equals("") && codigoProvincia != null && !codigoProvincia.equals("")){
	            codigoDGCMunicipio= ConstantesRegExp.clienteCatastro.getCodigoDGCMunicipio(codigoINE, codigoProvincia);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return codigoMunicipioDGC;
    }
   
}
