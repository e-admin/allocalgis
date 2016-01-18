/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

package com.geopista.app.metadatos.contactos;
import java.util.ResourceBundle;
import java.awt.*;
import java.io.StringWriter;
import java.io.PrintWriter;


import org.apache.log4j.Logger;
import com.geopista.protocol.metadatos.ListaContactos;
import com.geopista.protocol.metadatos.OperacionesMetadatos;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.CI_OnLineResource;

import com.geopista.protocol.administrador.dominios.DomainNode;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.metadatos.componentes.*;
import com.geopista.app.utilidades.TableSorted;

import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class CSearchContactos extends javax.swing.JDialog {
	private Logger logger = Logger.getLogger(CSearchContactos.class);
    private ResourceBundle messages;
    private ListaContactos listaContactos;
    private ContactosTableModel	modelContactos;
    private TableSorted sorter;
    private boolean modoEdicion=false;
    private boolean modoNuevo=false;
    private boolean seleccionado=false;
    private CI_ResponsibleParty contactoSelected=null;
    private CI_ResponsibleParty auxContacto;

	/**
	 * Creates new form JSearch
	 */
	public CSearchContactos(java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {

		super(parent, modal);
        this.messages=messages;
	    initComponents();
        initDatos();
        changeScreenLang(messages);

	}

    private void initDatos()
    {
        try
        {
            if (com.geopista.security.SecurityManager.isLogged())
            {
                listaContactos= new OperacionesMetadatos(Constantes.url).getContactos();
                actualizarModelo();
            }
        }catch (Exception e)
        {
            logger.error("Error de contactos al inicializar los datos: "+e.toString());
        }
    }
    private void actualizarModelo()
    {
         modelContactos= new ContactosTableModel();
         modelContactos.setModelData(listaContactos);
         sorter = new TableSorted(modelContactos);
         sorter.setTableHeader(jTableContactos.getTableHeader());
         jTableContactos.setModel(sorter);
         TableColumn column = jTableContactos.getColumnModel().getColumn(ContactosTableModel.idIndex);
         column.setPreferredWidth(5);
         column=jTableContactos.getColumnModel().getColumn(ContactosTableModel.idEmpresa);
         column.setPreferredWidth(15);
         jTableContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void anadir()
    {
            modoNuevo=true;
            auxContacto = new CI_ResponsibleParty();
            load(auxContacto);
            enabled(true);
    }

    public void eliminar()
    {
         if (contactoSelected==null)
            {
                  JOptionPane optionPane= new JOptionPane(messages.getString("CSearchContactos.mensaje.connoseleccionado"),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(this,"INFO");
                  dialog.show();
                  return;
            }
            int n = JOptionPane.showOptionDialog(this,
            messages.getString("CSearchContactos.mensaje.eliminarcontacto"),
                                              "",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,null,null,null);
            if (n==JOptionPane.NO_OPTION)
                 return;
            else
                 eliminarContacto(contactoSelected);
    }

    public void eliminarContacto(CI_ResponsibleParty contactoEliminado)
    {
           CResultadoOperacion result;
           try
           {
               result=(new OperacionesMetadatos(com.geopista.app.metadatos.init.Constantes.url)).eliminarContacto(contactoEliminado);
           }catch(Exception e)
           {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               e.printStackTrace(pw);
               logger.error("Exception al eliminar contacto en base de datos: " + sw.toString());
               result= new CResultadoOperacion(false,e.getMessage());
           }
           if (result.getResultado())
           {
                  listaContactos.remove(contactoEliminado);
                  contactoSelected=null;
                  JOptionPane optionPane= new JOptionPane(messages.getString("CSearchContactos.mensaje.contactoeliminado"),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(this,"");
                  dialog.show();
                  enabled(false);
                  load(contactoSelected);
                  actualizarModelo();
            }
            else
            {
                    JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"");
                    dialog.show();
               }
           }

    private void initComponents() {//GEN-BEGIN:initComponents
        jTableContactos = new javax.swing.JTable();
        jPanelPrincipal = new javax.swing.JPanel();
        jButtonSeleccionar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jPanelListaContactos = new javax.swing.JPanel();
        jButtonAnadir = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonEditar = new javax.swing.JButton();
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombre = new TextFieldCondicional(255);
        jLabelOrganizacion = new javax.swing.JLabel();
        jTextFieldOrganizacion = new TextFieldCondicional(50);
        jLabelCargo = new javax.swing.JLabel();
        jTextFieldCargo = new TextFieldCondicional(50);
        jTextPaneHorario = new TextPaneOptativo(50);
        jLabelHorario = new javax.swing.JLabel();
        jTextPaneIntrucciones = new TextPaneOptativo(255);
        jLabelInstruciones = new javax.swing.JLabel();
        jPanelDireccion = new javax.swing.JPanel();
        jLabelPais = new javax.swing.JLabel();
        jTextFieldPais = new TextFieldOptativo(50);
        jLabelTelefono = new javax.swing.JLabel();
        jTextPaneTelefonos = new ListOptativo(messages,messages.getString("CSearchContactos.jLabelTelefono"));
        jLabelTelefonoFax = new javax.swing.JLabel();
        jTextPaneFax = new ListOptativo(messages,messages.getString("CSearchContactos.jLabelTelefonoFax"));
        jLabelMails = new javax.swing.JLabel();
        jTextPanelMails = new ListOptativo(messages,messages.getString("CSearchContactos.jLabelMails"));
        jLabelPuntos = new javax.swing.JLabel();
        jTextPanePuntos = new ListOptativo(messages,messages.getString("CSearchContactos.jLabelPuntos"));
        jTextFieldCiudad = new TextFieldOptativo(50);
        jLabelCiudad = new javax.swing.JLabel();
        jLabelAreaAdministrativa = new javax.swing.JLabel();
        jTextFieldAreaAdministrativa = new TextFieldOptativo(50);
        jTextFieldCodigoPostal = new TextFieldOptativo(10);
        jLabelCodigoPostal = new javax.swing.JLabel();
        jPanelRecursos = new javax.swing.JPanel();
        jLabelEnlace = new javax.swing.JLabel();
        jTextFieldEnlace = new TextFieldOptativo(255);
        jLabelFuncion = new javax.swing.JLabel();
        jComboBoxFuncion = new ComboBoxEstructurasOptativo(Estructuras.getListaFunctionCode(),null);

        jButtonSalvar = new javax.swing.JButton();
        jButtonValidar = new javax.swing.JButton();
        jScrollPaneListaContactos= new javax.swing.JScrollPane();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelPrincipal.setBorder(new javax.swing.border.EtchedBorder());

        jPanelPrincipal.setDoubleBuffered(false);
        jButtonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionar();
            }
        });

        jPanelPrincipal.add(jButtonSeleccionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 440, 110, -1));

        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar();
            }
        });

        jPanelPrincipal.add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, -1, -1));

        jPanelListaContactos.setLayout(new java.awt.BorderLayout());

        jPanelListaContactos.add(jScrollPaneListaContactos, java.awt.BorderLayout.CENTER);

        jButtonAnadir.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        anadir();
                    }
                });

        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                eliminar();
                            }
                        });

        JPanel botoneraPanel=new JPanel();
        botoneraPanel.setLayout(new java.awt.GridLayout());
        botoneraPanel.add(jButtonAnadir);
        botoneraPanel.add(jButtonEliminar);
        jPanelListaContactos.add(botoneraPanel, java.awt.BorderLayout.SOUTH);
        jPanelPrincipal.add(jPanelListaContactos, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 193, 450));

        jLabelNombre.setFocusCycleRoot(true);
        jPanelPrincipal.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 120, -1));


        jPanelPrincipal.add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 310, -1));

        jLabelOrganizacion.setFocusCycleRoot(true);
        jPanelPrincipal.add(jLabelOrganizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        jPanelPrincipal.add(jTextFieldOrganizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 310, -1));

        jLabelCargo.setFocusCycleRoot(true);
        jPanelPrincipal.add(jLabelCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));


        jPanelPrincipal.add(jTextFieldCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 310, -1));


        JScrollPane auxScrollPaneHorario = new JScrollPane(jTextPaneHorario);
        jPanelPrincipal.add(auxScrollPaneHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 200, 60));

        jLabelHorario.setFocusCycleRoot(true);
        jPanelPrincipal.add(jLabelHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 200, -1));

        JScrollPane auxScrollIntruc = new JScrollPane(jTextPaneIntrucciones);
        jPanelPrincipal.add(auxScrollIntruc, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 220, 60));

        jLabelInstruciones.setFocusCycleRoot(true);
        jPanelPrincipal.add(jLabelInstruciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 220, -1));

        jPanelDireccion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelDireccion.setBorder(new javax.swing.border.TitledBorder("Direccion:"));

        jPanelDireccion.add(jLabelPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanelDireccion.add(jTextFieldPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 80, -1));

        jPanelDireccion.add(jLabelTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        JScrollPane auxPanel=new JScrollPane(jTextPaneTelefonos);

        jPanelDireccion.add(auxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 90, 70));

        jPanelDireccion.add(jLabelTelefonoFax, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, -1, -1));

        JScrollPane auxPanelFax=new JScrollPane(jTextPaneFax);

        jPanelDireccion.add(auxPanelFax, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 90, 70));

        jPanelDireccion.add(jLabelMails, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 110, -1));

        JScrollPane auxPanelMails=new JScrollPane(jTextPanelMails);
        jPanelDireccion.add(auxPanelMails, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 110, 70));

        jPanelDireccion.add(jLabelPuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 110, -1));

        JScrollPane auxPanelPuntos=new JScrollPane(jTextPanePuntos);
        jPanelDireccion.add(auxPanelPuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 110, 70));

        jPanelDireccion.add(jTextFieldCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 110, -1));

        jPanelDireccion.add(jLabelCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        jPanelDireccion.add(jLabelAreaAdministrativa, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 120, -1));

        jPanelDireccion.add(jTextFieldAreaAdministrativa, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 130, -1));

        jPanelDireccion.add(jTextFieldCodigoPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 80, -1));

        jPanelDireccion.add(jLabelCodigoPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jPanelPrincipal.add(jPanelDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 450, 170));

        jPanelRecursos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelRecursos.add(jLabelEnlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanelRecursos.add(jTextFieldEnlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 260, -1));

        jPanelRecursos.add(jLabelFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        jPanelRecursos.add(jComboBoxFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 130, -1));

        jPanelPrincipal.add(jPanelRecursos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 360, 450, 70));

        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvar();
            }});
        jPanelPrincipal.add(jButtonSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 440, -1, -1));
        jButtonValidar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        validarAccion();
                    }});


        jPanelPrincipal.add(jButtonValidar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 440, -1, -1));

        jButtonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editar();
            }});
        jPanelPrincipal.add(jButtonEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, -1, -1));

        jScrollPaneListaContactos.setViewportView(jTableContactos);

        //Para seleccionar una fila
        ListSelectionModel rowSM = jTableContactos.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                seleccionarContacto(e);
            }
        });
        getContentPane().add(jPanelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 470));
        enabled(false);
        pack();
    }//GEN-END:initComponents


    private void cancelar() {
        if (modoEdicion)
        {
                         int n = JOptionPane.showOptionDialog(this,
                                                          messages.getString("CSearchContactos.mensaje.desechar"),
                                                          "",
                                                          JOptionPane.YES_NO_OPTION,
                                                          JOptionPane.QUESTION_MESSAGE,null,null,null);
                         if (n==JOptionPane.NO_OPTION)
                             return;
                         else
                             enabled(false);
                         modoNuevo=false;
        }
        dispose();
    }

     private void seleccionar() {
		if (!validar()) return;
        if (contactoSelected==null)
        {
            contactoSelected = new CI_ResponsibleParty();
            guardarCambios(contactoSelected);
        }
        seleccionado=true;
		dispose();
     }


    private void seleccionarContacto(ListSelectionEvent e)
    {
            if (modoEdicion)
           {
                  int n = JOptionPane.showOptionDialog(this,
                                                   messages.getString("CSearchContactos.mensaje.desechar"),
                                                   "",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE,null,null,null);
                  if (n==JOptionPane.NO_OPTION)
                      return;
                  else
                      enabled(false);
                  modoNuevo=false;
           }
           ListSelectionModel lsm = (ListSelectionModel)e.getSource();
           if (lsm.isSelectionEmpty()) { }
           else
           {
                int selectedRow = lsm.getMinSelectionIndex();
                String idContacto=(String)sorter.getValueAt(selectedRow,ContactosTableModel.idIndex);
                contactoSelected= listaContactos.get(idContacto);
                if (contactoSelected==null)
                {
                    JOptionPane optionPane= new JOptionPane(
                            messages.getString("CSearchContactos.mensaje.connoencontrado"),JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"");
                    dialog.show();
                }
                load(contactoSelected);
                jButtonEditar.setEnabled(true);
           }
       }


    public void enabled(boolean bValor)
    {
          modoEdicion=bValor;
          jButtonEditar.setEnabled(bValor);
          jButtonAnadir.setEnabled(!bValor);
          jButtonSeleccionar.setEnabled(!bValor||!com.geopista.security.SecurityManager.isLogged());
          jButtonEliminar.setEnabled(!bValor);
          jButtonSalvar.setEnabled(bValor&&com.geopista.security.SecurityManager.isLogged());
          jButtonValidar.setEnabled(bValor);
          jComboBoxFuncion.setEnabled(bValor);
          jTextFieldAreaAdministrativa.setEditable(bValor);
          jTextFieldCargo.setEditable(bValor);
          jTextFieldCiudad.setEditable(bValor);
          jTextFieldCodigoPostal.setEditable(bValor);
          jTextFieldEnlace.setEditable(bValor);
          jTextFieldNombre.setEditable(bValor);
          jTextFieldOrganizacion.setEditable(bValor);
          jTextFieldPais.setEditable(bValor);
          jTextPaneFax.setEnabled(bValor);
          jTextPaneFax.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jTextPaneHorario.setEditable(bValor);
          jTextPaneHorario.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jTextPaneIntrucciones.setEditable(bValor);
          jTextPaneIntrucciones.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jTextPanePuntos.setEnabled(bValor);
          jTextPanePuntos.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jTextPaneTelefonos.setEnabled(bValor);
          jTextPaneTelefonos.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jTextPanelMails.setEnabled(bValor);
          jTextPanelMails.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);

    }

    private void editar()
    {
        if (contactoSelected==null)
        {
              JOptionPane optionPane= new JOptionPane(messages.getString("CSearchContactos.mensaje.connoseleccionado"),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"INFO");
              dialog.show();
              return;
        }
        enabled(true);
        jButtonEditar.setEnabled(false);

    }

    private void validarAccion()
    {
        if (validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("CSearchContactos.mensaje.valido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
        }
    }
    private boolean validar()
    {
        if ((jTextFieldNombre.getText()==null || jTextFieldNombre.getText().length()<=0) &&
            (jTextFieldOrganizacion.getText()==null || jTextFieldOrganizacion.getText().length()<=0) &&
            (jTextFieldCargo.getText()==null || jTextFieldCargo.getText().length()<=0))
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("CSearchContactos.mensaje.novalido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
        }
        return true;
    }
    private void guardarCambios(CI_ResponsibleParty contact)
    {
        contact.setAddressAdministrativeArea(jTextFieldAreaAdministrativa.getText());
        contact.setPositionName(jTextFieldCargo.getText());
        contact.setAddressCity(jTextFieldCiudad.getText());
        contact.setAddressPostalCode(jTextFieldCodigoPostal.getText());
        contact.setIndividualName(jTextFieldNombre.getText());
        contact.setOrganisationName(jTextFieldOrganizacion.getText());
        contact.setAddressCountry(jTextFieldPais.getText());
        contact.setCi_telephone_facsimile(jTextPaneFax.getVectorModel());
        contact.setHoursOfService(jTextPaneHorario.getText());
        contact.setContactInstructions(jTextPaneIntrucciones.getText());
        contact.setDeliveryPoint(jTextPanePuntos.getVectorModel());
        contact.setCi_telephone_voice(jTextPaneTelefonos.getVectorModel());
        contact.setElectronicMailAdress(jTextPanelMails.getVectorModel());

       if (contact.getOnLineResource()!=null)
       {
                       //jComboBoxFuncion.setEnabled();
              contact.getOnLineResource().setLinkage(jTextFieldEnlace.getText());
              DomainNode function=(DomainNode) jComboBoxFuncion.getSelectedItem();
              if (function.getIdDomain()!=null) contact.getOnLineResource().setIdOnLineFunctionCode(function.getIdNode());
              logger.debug("Id Function code seleccionado: "+contact.getOnLineResource().getIdOnLineFunctionCode());
       }else
       {
            if (jTextFieldEnlace.getText()!=null&&jTextFieldEnlace.getText().length()>0)
            {
                contact.setOnLineResource(new CI_OnLineResource());
                contact.getOnLineResource().setLinkage(jTextFieldEnlace.getText());
                DomainNode function=(DomainNode) jComboBoxFuncion.getSelectedItem();
                if (function.getIdDomain()!=null) contact.getOnLineResource().setIdOnLineFunctionCode(function.getIdNode());
            }
       }
    }
    private void salvar()
    {
        if (!modoEdicion) return;
        try
        {
            if (!validar())
            {
                return;
            }

            guardarCambios(auxContacto);
            CResultadoOperacion result=null;
            String sMensaje="";
            try
            {
                if (modoNuevo)
                {
                   result=(new OperacionesMetadatos(Constantes.url)).nuevoContacto(auxContacto);
                   sMensaje=messages.getString("CSearchContactos.mensaje.contactoinsertado");
               }
               else
               {
                   logger.debug("Actualizando usuario: "+auxContacto +" Grupos: \n");
                   result=(new OperacionesMetadatos(Constantes.url)).actualizarContacto(auxContacto);
                   sMensaje=messages.getString("CSearchContactos.mensaje.contactoactualizado");
               }
           }catch(Exception e)
           {
                 StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("Exception al grabar en base de datos un nuevo contacto: " + sw.toString());
                 result= new CResultadoOperacion(false,e.getMessage());
           }
           if (result.getResultado())
           {
              try
              {
                  auxContacto= (CI_ResponsibleParty)result.getVector().elementAt(0);}
              catch(Exception e)
              {logger.error("Error al coger el objeto contacto: "+e.toString());}

              logger.debug("Identificador del contacto insertado:"+auxContacto.getId());
              if (auxContacto.getOnLineResource()!=null)
                  logger.debug("Identificador del recurtos insertado:"+auxContacto.getOnLineResource().getId());

              contactoSelected=auxContacto;
              listaContactos.add(contactoSelected);
              JOptionPane optionPane= new JOptionPane(sMensaje,JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();
              enabled(false);
              modoNuevo=false;
              load(contactoSelected);
              actualizarModelo();
           }
           else
           {
                JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
           }
        }catch(Exception ex)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	ex.printStackTrace(pw);
            logger.error("Excepcion al añadir el contacto: "+sw.toString());
            JOptionPane optionPane= new JOptionPane(ex,JOptionPane.ERROR_MESSAGE);
            JDialog dialog =optionPane.createDialog(this,"");
            dialog.show();
        }

    }
    public void load(CI_ResponsibleParty contact)
       {
             if (contact==null) contact= new CI_ResponsibleParty();
             jTextFieldAreaAdministrativa.setText(contact.getAddressAdministrativeArea()==null?"":contact.getAddressAdministrativeArea());
             jTextFieldCargo.setText(contact.getPositionName()==null?"":contact.getPositionName());
             jTextFieldCiudad.setText(contact.getAddressCity()==null?"":contact.getAddressCity());
             jTextFieldCodigoPostal.setText(contact.getAddressPostalCode()==null?"":contact.getAddressPostalCode());
             jTextFieldNombre.setText(contact.getIndividualName()==null?"":contact.getIndividualName());
             jTextFieldOrganizacion.setText(contact.getOrganisationName()==null?"":contact.getOrganisationName());
             jTextFieldPais.setText(contact.getAddressCountry()==null?"":contact.getAddressCountry());
             jTextPaneFax.setModel(contact.getCi_telephone_facsimile());
             jTextPaneHorario.setText(contact.getHoursOfService());
             jTextPaneIntrucciones.setText(contact.getContactInstructions());
             jTextPanePuntos.setModel(contact.getDeliveryPoint());
             jTextPaneTelefonos.setModel(contact.getCi_telephone_voice());
             jTextPanelMails.setModel(contact.getElectronicMailAdress());
             if (contact.getOnLineResource()!=null)
             {
                    jTextFieldEnlace.setText(contact.getOnLineResource().getLinkage()==null?"":
                                            contact.getOnLineResource().getLinkage());
                    logger.debug("ID function code: "+contact.getOnLineResource().getIdOnLineFunctionCode());
                    jComboBoxFuncion.setSelected(contact.getOnLineResource().getIdOnLineFunctionCode());
             }
             else
             {
                 jTextFieldEnlace.setText("");
                 jComboBoxFuncion.setSelectedIndex(0);
             }
             auxContacto=contact;

       }

    public CI_ResponsibleParty getContactoSelected() {
        return contactoSelected;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void changeScreenLang(ResourceBundle messages) {
        this.messages=messages;
        setTitle(messages.getString("CSearchContactos.title"));
        jPanelPrincipal.setToolTipText(messages.getString("CSearchContactos.jPanelPrincipal"));
        jButtonSeleccionar.setText(messages.getString("CSearchContactos.jButtonSeleccionar"));
        jButtonCancelar.setText(messages.getString("CSearchContactos.jButtonCancelar"));
        jPanelListaContactos.setToolTipText(messages.getString("CSearchContactos.jPanelListaContactos"));
        jButtonAnadir.setText(messages.getString("CSearchContactos.jButtonAnadir"));
        jButtonEliminar.setText(messages.getString("CSearchContactos.jButtonEliminar"));
        jButtonEditar.setText(messages.getString("CSearchContactos.jButtonEditar"));
        jLabelNombre.setText(messages.getString("CSearchContactos.jLabelNombre"));
        jLabelOrganizacion.setText(messages.getString("CSearchContactos.jLabelOrganizacion"));
        jLabelCargo.setText(messages.getString("CSearchContactos.jLabelCargo"));
        jLabelHorario.setText(messages.getString("CSearchContactos.jLabelHorario"));
        jLabelInstruciones.setText(messages.getString("CSearchContactos.jLabelInstruciones"));
        jLabelPais.setText(messages.getString("CSearchContactos.jLabelPais"));
        jLabelTelefono.setText(messages.getString("CSearchContactos.jLabelTelefono"));
        jLabelTelefonoFax.setText(messages.getString("CSearchContactos.jLabelTelefonoFax"));
        jLabelMails.setText(messages.getString("CSearchContactos.jLabelMails"));
        jLabelPuntos.setText(messages.getString("CSearchContactos.jLabelPuntos"));
        jLabelCiudad.setText(messages.getString("CSearchContactos.jLabelCiudad"));
        jLabelAreaAdministrativa.setText(messages.getString("CSearchContactos.jLabelAreaAdministrativa"));
        jLabelCodigoPostal.setText(messages.getString("CSearchContactos.jLabelCodigoPostal"));
        jPanelRecursos.setBorder(new javax.swing.border.TitledBorder(messages.getString("CSearchContactos.jPanelRecursos")));
        jLabelEnlace.setText(messages.getString("CSearchContactos.jLabelEnlace"));
        jLabelFuncion.setText(messages.getString("CSearchContactos.jLabelFuncion"));
        jPanelListaContactos.setBorder(new javax.swing.border.TitledBorder(messages.getString("CSearchContactos.jPanelListaContactos")));
        jButtonValidar.setText(messages.getString("CSearchContactos.jButtonValidar"));
        jButtonSalvar.setText(messages.getString("CSearchContactos.jButtonSalvar"));

     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonEditar;
    private javax.swing.JButton jButtonAnadir;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonSeleccionar;
    private javax.swing.JButton jButtonValidar;
    private ComboBoxEstructurasOptativo jComboBoxFuncion;
    private javax.swing.JLabel jLabelAreaAdministrativa;
    private javax.swing.JLabel jLabelCargo;
    private javax.swing.JLabel jLabelCiudad;
    private javax.swing.JLabel jLabelCodigoPostal;
    private javax.swing.JLabel jLabelEnlace;
    private javax.swing.JLabel jLabelFuncion;
    private javax.swing.JLabel jLabelHorario;
    private javax.swing.JLabel jLabelInstruciones;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelOrganizacion;
    private javax.swing.JLabel jLabelPais;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTelefonoFax;
    private javax.swing.JLabel jLabelMails;
    private javax.swing.JLabel jLabelPuntos;
    private javax.swing.JPanel jPanelDireccion;
    private javax.swing.JPanel jPanelListaContactos;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JPanel jPanelRecursos;
    private javax.swing.JTextField jTextFieldAreaAdministrativa;
    private javax.swing.JTextField jTextFieldCargo;
    private javax.swing.JTextField jTextFieldCiudad;
    private javax.swing.JTextField jTextFieldCodigoPostal;
    private javax.swing.JTextField jTextFieldEnlace;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldOrganizacion;
    private javax.swing.JTextField jTextFieldPais;
    private ListOptativo jTextPaneFax;
    private TextPaneOptativo jTextPaneHorario;
    private TextPaneOptativo jTextPaneIntrucciones;
    private ListOptativo jTextPanePuntos;
    private ListOptativo jTextPaneTelefonos;
    private ListOptativo jTextPanelMails;
    private javax.swing.JTable jTableContactos;
    private javax.swing.JScrollPane jScrollPaneListaContactos;
    // End of variables declaration//GEN-END:variables

}
