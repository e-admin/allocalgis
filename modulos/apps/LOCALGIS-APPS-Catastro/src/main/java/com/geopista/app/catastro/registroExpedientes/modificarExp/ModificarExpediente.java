/**
 * ModificarExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.modificarExp;

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

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
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
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

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
    private Hashtable codigoDigitoControlDni;
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

        if(expediente.getIdEstado() > ConstantesCatastro.ESTADO_MODIFICADO)
            guardarJButton.setEnabled(false);

        modificacionExpScrollPane.setViewportView(modificacionExpPanel);
        getContentPane().add(modificacionExpScrollPane);
        inicializaEventos();      
        inicializaDatos();
        datosPanel.getEntidadGeneradoraJCBox().setEnabled(false);
    }

   /**
    * Inicializa los elementos del panel de datos del expediente.
    */
    private void inicializaPanelesExp()
    {
        datosPanel = new PanelDatosCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.datosPanel");
        datosPanel.setDesktop(desktop);
        datosPanel.setEditable(false);
       // datosPanel.getTipoTramitacionCBox().setEnabled(false);

        modificacionExpPanel.add(datosPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 500, 155));

        expAdminGerenciaPanel= new PanelExpAdminGerencia("Catastro.RegistroExpedientes.ModificarExpediente.expAdminGerenciaPanel");
        expAdminGerenciaPanel.setEditable(false);
        modificacionExpPanel.add(expAdminGerenciaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 160, 500, 90));

        infNotariaPanel= new PanelInformacionNotarialCrearExp("Catastro.RegistroExpedientes.ModificarExpediente.infNotariaPanel");
        modificacionExpPanel.add(infNotariaPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 253, 500, 90));

        datosDeclaracionPanel= new PanelDatosDeclaracion("Catastro.RegistroExpedientes.ModificarExpediente.datosDeclaracionPanel");
        modificacionExpPanel.add(datosDeclaracionPanel.getDatosPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 345, 500, 205));

        if(expediente.getIdEstado() > ConstantesCatastro.ESTADO_MODIFICADO){
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
        modificacionExpPanel.add(direccionPanel.getDatosDirPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 277, 500, 273));
        //modificacionExpPanel.add(localizacionInterPanel.getDatosLIPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 445, 500, 110));
        if(expediente.getIdEstado() > ConstantesCatastro.ESTADO_MODIFICADO){
            presentadorDatosPerPanel.setEditable(false);
            datosNotifPanel.setEditable(false);
            direccionPanel.setEditable(false);        
        }
        getCodigosDigitoControlDniBD();
        presentadorDatosPerPanel.setDigitoContolDniHash(codigoDigitoControlDni);
        
        //direccionPanel.cargarTiposVias(getTiposViasBD());
        getCodigosNombresProvinciasBD();
        datosNotifPanel.cargaProvincias(codigoNombreProvincia);
        
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
				direccionPanel.getCodigoViaJTField().setText(String.valueOf(presentadorDatosPerPanel.getDatosPersonales().getCodigoVia().intValue()));
            	}

            }
        });
        
        

    }

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
    /**
     * Asocia el evento para el comboBox de provicias, para que cargue los municipios de la provincia al cambiar esta.
     */
    private void inicializaEventos()
    {
        if(expediente.getIdEstado() < ConstantesCatastro.ESTADO_FINALIZADO){
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
            hashDatos.put(ConstantesCatastro.expedienteIdEstado, Estructuras.getListaEstadosExpediente().
                    getDomainNode(String.valueOf(expediente.getIdEstado())).getTerm(ConstantesCatastro.Locale));
            hashDatos.put(ConstantesCatastro.expedienteNumeroExpediente, String.valueOf(expediente.getNumeroExpediente()));
            hashDatos.put(ConstantesCatastro.expedienteTipoExpediente, expediente.getTipoExpediente().getCodigoTipoExpediente());
            hashDatos.put(ConstantesCatastro.expedienteFechaRegistro, expediente.getFechaRegistro());
            hashDatos.put(ConstantesCatastro.expedienteTipoTramitacion, expediente.isTipoTramitaExpSitFinales());
            hashDatos.put(ConstantesCatastro.expedienteEntidadGeneradora, expediente.getEntidadGeneradora());


            //Panel Expediente Administrativo de la Gerencia
            if(expediente.getAnnoExpedienteGerencia()!=null) //PREV-NOV
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
            //PREV-NOV if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!=0)
            if(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()!= null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, String.valueOf(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion,"");
            }

            //Panel Informacion Notarial
            if(expediente.getCodProvinciaNotaria()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodProvinciaNotaria, expediente.getCodProvinciaNotaria());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodProvinciaNotaria, "");
            }
            if(expediente.getCodPoblacionNotaria()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodPoblacionNotaria, expediente.getCodPoblacionNotaria());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodPoblacionNotaria, "");
            }
            if(expediente.getCodNotaria()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodNotaria, expediente.getCodNotaria());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodNotaria, "");
            }
            if(expediente.getAnnoProtocoloNotarial()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoProtocoloNotarial, String.valueOf(expediente.getAnnoProtocoloNotarial()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteAnnoProtocoloNotarial, "");
            }
            if(expediente.getProtocoloNotarial()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteProtocoloNotarial, expediente.getProtocoloNotarial());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteProtocoloNotarial, "");
            }

            //Panel datos de la declaracion
            if(expediente.getTipoDocumentoOrigenAlteracion()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion, expediente.getTipoDocumentoOrigenAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion, "");
            }
            if(expediente.getInfoDocumentoOrigenAlteracion()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion, expediente.getInfoDocumentoOrigenAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion, "");
            }
            if(expediente.getNumBienesInmueblesUrbanos()!=0)
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos, String.valueOf(expediente.getNumBienesInmueblesUrbanos()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos, "");                
            }
            if(expediente.getNumBienesInmueblesRusticos()!=0)
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesRusticos, String.valueOf(expediente.getNumBienesInmueblesRusticos()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesRusticos, "");
            }
            if(expediente.getNumBienesInmueblesCaractEsp()!=0)
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp, String.valueOf(expediente.getNumBienesInmueblesCaractEsp()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp, "");
            }
            if(expediente.getCodigoDescriptivoAlteracion()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion, expediente.getCodigoDescriptivoAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion, "");
            }
            if(expediente.getDescripcionAlteracion()!=null)
            {
                hashDatos.put(ConstantesCatastro.expedienteDescripcionAlteracion, expediente.getDescripcionAlteracion());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.expedienteDescripcionAlteracion, "");
            }

            //Panel Datos Personales Presentador
            hashDatos.put(ConstantesCatastro.expedienteNifPresentador, expediente.getNifPresentador().substring(0, 8));
            hashDatos.put(ConstantesCatastro.expedienteDigitoControlNifPresentador, expediente.getNifPresentador().substring(8, 9));
            hashDatos.put(ConstantesCatastro.expedienteNombreCompletoPresentador, expediente.getNombreCompletoPresentador());

            //Panel Datos Notificacion
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
            
            
            
            if (expediente.getDireccionPresentador().getCodigoPostal() != null){
            	hashDatos.put(ConstantesCatastro.direccionCodigoPostal, expediente.getDireccionPresentador().getCodigoPostal());
            }
            else{
            	hashDatos.put(ConstantesCatastro.direccionCodigoPostal, "");
            }
            if(expediente.getDireccionPresentador().getApartadoCorreos()!=0)
            {
                hashDatos.put(ConstantesCatastro.direccionApartadoCorreos, String.valueOf(expediente.getDireccionPresentador().getApartadoCorreos()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionApartadoCorreos,"");
            }

            //Panel Direccion
            hashDatos.put(ConstantesCatastro.direccionCodigoVia, new Integer(expediente.getDireccionPresentador().getCodigoVia()).toString());
            
            if(expediente.getDireccionPresentador().getTipoVia()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionTipoVia, expediente.getDireccionPresentador().getTipoVia());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionTipoVia,"");
            }
            if(expediente.getDireccionPresentador().getNombreVia()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionNombreVia, expediente.getDireccionPresentador().getNombreVia());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionNombreVia, "");
            }
            if(expediente.getDireccionPresentador().getPrimerNumero()>0)
            {
                hashDatos.put(ConstantesCatastro.direccionPrimerNumero, String.valueOf(expediente.getDireccionPresentador().getPrimerNumero()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionPrimerNumero, "");                
            }
            if(expediente.getDireccionPresentador().getPrimeraLetra()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionPrimeraLetra, expediente.getDireccionPresentador().getPrimeraLetra());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionPrimeraLetra, "");                
            }
            if(expediente.getDireccionPresentador().getSegundoNumero()>0)
            {
                hashDatos.put(ConstantesCatastro.direccionSegundoNumero, String.valueOf(expediente.getDireccionPresentador().getSegundoNumero()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionSegundoNumero, "");
            }
            if(expediente.getDireccionPresentador().getSegundaLetra()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionSegundaLetra, expediente.getDireccionPresentador().getSegundaLetra());                
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionSegundaLetra, "");
            }
            if(expediente.getDireccionPresentador().getKilometro()>0)
            {
                hashDatos.put(ConstantesCatastro.direccionKilometro, String.valueOf((long)expediente.getDireccionPresentador().getKilometro()));
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionKilometro, "");
            }
            if(expediente.getDireccionPresentador().getDireccionNoEstructurada()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionDireccionNoEstructurada, expediente.getDireccionPresentador().getDireccionNoEstructurada());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionDireccionNoEstructurada, "");
            }

            //Panel Localizacion Interna
            if(expediente.getDireccionPresentador().getBloque()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionBloque, expediente.getDireccionPresentador().getBloque());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionBloque, "");
            }
            if(expediente.getDireccionPresentador().getEscalera()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionEscalera, expediente.getDireccionPresentador().getEscalera());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionEscalera, "");
            }
            if(expediente.getDireccionPresentador().getPlanta()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionPlanta, expediente.getDireccionPresentador().getPlanta());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionPlanta, "");
            }
            if(expediente.getDireccionPresentador().getPuerta()!=null)
            {
                hashDatos.put(ConstantesCatastro.direccionPuerta, expediente.getDireccionPresentador().getPuerta());
            }
            else
            {
                hashDatos.put(ConstantesCatastro.direccionPuerta, "");
            }

            datosPanel.inicializaDatos(hashDatos);
            expAdminGerenciaPanel.inicializaDatos(hashDatos);
            infNotariaPanel.inicializaDatos(hashDatos);
            datosDeclaracionPanel.inicializaDatos(hashDatos);
            presentadorDatosPerPanel.inicializaDatos(hashDatos);
            datosNotifPanel.inicializaDatos(hashDatos);
            direccionPanel.inicializaDatos(hashDatos);
            //localizacionInterPanel.inicializaDatos(hashDatos);
            
            

            datosNotifPanel.inicializaDatosMunicipio(hashDatos);
       }
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

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener los nombres y los codigos
     * de las provincias. La consulta devuelver una hashtable con dos arrayList, un con key codigos y otro con key
     * nombres. En este caso el municipio del expediente se selecciona entre los resultados.
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
	            codigoNombreMunicipio= ConstantesRegExp.clienteCatastro.getCodigoNombreMunicipio(codigoProvincia);
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
            expediente.setNumeroExpediente((String)hashDatos.get(ConstantesCatastro.expedienteNumeroExpediente));
            expediente.setTipoExpediente(UtilRegistroExp.getTipoExpediente((String)hashDatos.get(ConstantesCatastro.expedienteTipoExpediente)));
            expediente.setIdEstado(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteIdEstado)));
            expediente.setFechaRegistro((Date)hashDatos.get(ConstantesCatastro.expedienteFechaRegistro));

            //Datos del PanelexpAdminGerenciaPanel
            if(!hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia).equals(""))
            {
                // PREV-NOV expediente.setAnnoExpedienteGerencia(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia)));
                expediente.setAnnoExpedienteGerencia(Integer.valueOf((String)hashDatos.get(ConstantesCatastro.expedienteAnnoExpedienteGerencia)));
            }
            if(!hashDatos.get(ConstantesCatastro.expedienteReferenciaExpedienteGerencia).equals(""))
            {
                expediente.setReferenciaExpedienteGerencia((String)hashDatos.get(ConstantesCatastro.expedienteReferenciaExpedienteGerencia));
            }
            if(!hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion).equals(""))
            {
                // PREV-NOV expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
                expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf((String)hashDatos.get(ConstantesCatastro.expedienteCodigoEntidadRegistroDGCOrigenAlteracion)));
            }

            //Datos del Panel InformacionNotarial
            if(!hashDatos.get(ConstantesCatastro.expedienteCodProvinciaNotaria).equals(""))
            {
                expediente.setCodProvinciaNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodProvinciaNotaria)));
            }
            else{
            	 expediente.setCodProvinciaNotaria("");
            }
            
            if(!hashDatos.get(ConstantesCatastro.expedienteCodPoblacionNotaria).equals(""))
            {
                expediente.setCodPoblacionNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodPoblacionNotaria)));
            }
            else{
            	expediente.setCodPoblacionNotaria("");
            }
            
            if(!hashDatos.get(ConstantesCatastro.expedienteCodNotaria).equals(""))
            {
                expediente.setCodNotaria(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteCodNotaria)));
            }
            else{
            	  expediente.setCodNotaria(
                          ((String)hashDatos.get(ConstantesCatastro.expedienteCodNotaria)));
            }
            
            if(!hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial).equals(""))
            {
                //PREV-NOV
                //expediente.setAnnoProtocoloNotarial(Integer.parseInt((String)hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial)));
                expediente.setAnnoProtocoloNotarial((String)hashDatos.get(ConstantesCatastro.expedienteAnnoProtocoloNotarial));

            }
            else{
	 			expediente.setAnnoProtocoloNotarial("");

            }
            
            if(!hashDatos.get(ConstantesCatastro.expedienteProtocoloNotarial).equals(""))
            {
                expediente.setProtocoloNotarial(
                        ((String)hashDatos.get(ConstantesCatastro.expedienteProtocoloNotarial)));
            }
            else{
            	 expediente.setProtocoloNotarial("");
            }

            //Datos del Panel Declaracion
            expediente.setTipoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion));
            expediente.setInfoDocumentoOrigenAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion));
            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos).equals(""))
            {
                expediente.setNumBienesInmueblesUrbanos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos)));
            }
            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesRusticos).equals(""))
            {
                expediente.setNumBienesInmueblesRusticos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesRusticos)));
            }
            if(!hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp).equals(""))
            {
                expediente.setNumBienesInmueblesCaractEsp(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp)));
            }
            expediente.setCodigoDescriptivoAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion));
            expediente.setDescripcionAlteracion((String)hashDatos.get(ConstantesCatastro.expedienteDescripcionAlteracion));

            //Datos del Panel datosPersonales
            expediente.setNifPresentador((String)hashDatos.get(ConstantesCatastro.expedienteNifPresentador)+
            		(String)hashDatos.get(ConstantesCatastro.expedienteDigitoControlNifPresentador));
            expediente.setNombreCompletoPresentador((String)hashDatos.get(ConstantesCatastro.expedienteNombreCompletoPresentador));

            //Creamos y completamos la direccion.
            DireccionLocalizacion dir = expediente.getDireccionPresentador();

            //Datos del panel DatosNotificacion
        
            ArrayList codigosProv = (ArrayList)codigoNombreProvincia.get("codigos");
            ArrayList codigosMunic = (ArrayList)codigoNombreMunicipio.get("codigos");
            dir.setProvinciaINE((String)codigosProv.get(datosNotifPanel.getProvinciaSelect()));
            dir.setMunicipioINE((String)codigosMunic.get(datosNotifPanel.getMunicipioSelect()));
            dir.setNombreProvincia((String)hashDatos.get(ConstantesCatastro.direccionNombreProvincia));
            dir.setNombreMunicipio((String)hashDatos.get(ConstantesCatastro.direccionNombreMunicipio));


            dir.setNombreEntidadMenor((String)hashDatos.get(ConstantesCatastro.direccionNombreEntidadMenor));
            dir.setCodigoPostal((String)hashDatos.get(ConstantesCatastro.direccionCodigoPostal));

            if(!hashDatos.get(ConstantesCatastro.direccionApartadoCorreos).equals(""))
            {
                dir.setApartadoCorreos(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.direccionApartadoCorreos)));
            }

            //Datos del panel Direccion
            dir.setTipoVia((String)hashDatos.get(ConstantesCatastro.direccionTipoVia));
            dir.setNombreVia((String)hashDatos.get(ConstantesCatastro.direccionNombreVia));
            if(!hashDatos.get(ConstantesCatastro.direccionPrimerNumero).equals(""))
            {
                dir.setPrimerNumero(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.direccionPrimerNumero)));
            }
            dir.setPrimeraLetra(((String)hashDatos.get(ConstantesCatastro.direccionPrimeraLetra)));
            if(!hashDatos.get(ConstantesCatastro.direccionSegundoNumero).equals(""))
            {
                dir.setSegundoNumero(Integer.parseInt
                        ((String)hashDatos.get(ConstantesCatastro.direccionSegundoNumero)));
            }
            dir.setSegundaLetra(((String)hashDatos.get(ConstantesCatastro.direccionSegundaLetra)));
            if(!hashDatos.get(ConstantesCatastro.direccionKilometro).equals(""))
            {
                dir.setKilometro(Double.parseDouble((String)hashDatos.get(ConstantesCatastro.direccionKilometro)));
            }
            dir.setDireccionNoEstructurada((String)hashDatos.get(ConstantesCatastro.direccionDireccionNoEstructurada));

            //Datos del panel Localizacion interna
            dir.setBloque((String)hashDatos.get(ConstantesCatastro.direccionBloque));
            dir.setEscalera((String)hashDatos.get(ConstantesCatastro.direccionEscalera));
            dir.setPlanta((String)hashDatos.get(ConstantesCatastro.direccionPlanta));
            dir.setPuerta((String)hashDatos.get(ConstantesCatastro.direccionPuerta));

            return true;
        }
        return false;
    }
}
