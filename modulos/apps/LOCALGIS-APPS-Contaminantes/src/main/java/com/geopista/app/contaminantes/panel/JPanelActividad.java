/**
 * JPanelActividad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes.panel;


import com.geopista.app.contaminantes.InspeccionesTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.contaminantes.Inspeccion;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;


import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.*;
import java.awt.*;
import java.text.SimpleDateFormat;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 15-oct-2004
 * Time: 10:08:37
 */
public class JPanelActividad extends JPanel{
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelActividad.class);
    ResourceBundle messages;
    JFrame parent;
    private TableSorted sorter;
    private Contaminante actividad= new Contaminante();
    private Icon iconPersona;
    InspeccionesTableModel modelInspecciones;
    Inspeccion inspeccionSelected=null;
    boolean modoEdicion=false;
    Hashtable _hInspeccionesActividad= new Hashtable();

    public JPanelActividad(ResourceBundle messages, JFrame frame) {
        super();
        parent=frame;
        this.messages=messages;
        initComponents();
        changeScreenLang(messages);
        enabled(false);
     }

    public Contaminante guardarCambios()
    {
        if (actividad==null) actividad=new Contaminante();
        actividad.setAsunto(jTextPaneAsunto.getText());
        actividad.setId_razon(jComboBoxRazon.getSelectedPatron());
        actividad.setId_tipo(jComboBoxTipo.getSelectedPatron());
        actividad.setTipovia(jComboBoxTipoVia.getSelectedPatron());
        actividad.setCpostal(jTextFieldCodigoPostal.getText());
        if (jTextFieldFechaInicio.getText().length()<=0)
            actividad.setfInicio(null);
        else
            actividad.setfInicio(jButtonFechaInicio.getCalendar().getTime());

        if (jTextFieldFechaFin.getText().length()<=0)
            actividad.setfFin(null);
        else
            actividad.setfFin(jButtonFechaFin.getCalendar().getTime());
        actividad.setNombrevia(jTextFieldNombreVia.getText());
        actividad.setNumeroAdm(jTextFieldNumero.getText());
        actividad.setNumerovia(jTextFieldNumeroVia.getText());
        return actividad;
    }

    public void load(Contaminante actividadCon)
    {
        if (actividadCon!=null)
            this.actividad=(Contaminante)actividadCon.clone();
        else
            this.actividad=new Contaminante();
        jTextPaneAsunto.setText(actividad.getAsunto());
        jComboBoxRazon.setSelectedPatron(actividad.getId_razon());
        jComboBoxTipo.setSelectedPatron(actividad.getId_tipo());
        jComboBoxTipoVia.setSelectedPatron(actividad.getTipovia());
        jTextFieldCodigoPostal.setText(actividad.getCpostal());
        Calendar auxCalendar =Calendar.getInstance();
        if (actividad.getfFin()!=null)
            auxCalendar.setTime(actividad.getfFin());
        else
            auxCalendar=null;
        jButtonFechaFin.setFecha(auxCalendar);
        auxCalendar =Calendar.getInstance();
        if (actividad.getfInicio()!=null)
            auxCalendar.setTime(actividad.getfInicio());
        else
            auxCalendar=null;
        jButtonFechaInicio.setFecha(auxCalendar);
        jTextFieldNombreVia.setText(actividad.getNombrevia());
        jTextFieldNumero.setText(actividad.getNumeroAdm());
        jTextFieldNumeroVia.setText(actividad.getNumerovia());
        actualizarModelo();

        /** rellenamos la hash con las inspecciones de la actividad */
        if (actividad != null){
            _hInspeccionesActividad= new Hashtable();
            if (actividad.getInspecciones() != null){
                for (Enumeration e1=actividad.getInspecciones().elements();e1.hasMoreElements();){
                    Inspeccion inspeccion= (Inspeccion)e1.nextElement();
                    if (inspeccion.getId()!=null)
                    _hInspeccionesActividad.put(inspeccion.getId(), inspeccion);
                }
            }
        }
        loadInfractores();
    }
    private void loadInfractores()
    {
        try
        {
          DefaultListModel  listModel= new DefaultListModel();
          if (actividad.getInfractores()!=null)
          {
                for (Enumeration e=actividad.getInfractores().elements();e.hasMoreElements();)
                    listModel.addElement(e.nextElement());
          }
          jListInfractores.setModel(listModel);
          jListInfractores.setCellRenderer(
            new DefaultListCellRenderer()
            {
                        public Component getListCellRendererComponent(JList l,
                                                          Object value,
                                                          int i,
                                                          boolean s,
                                                          boolean f) {
                        CPersonaJuridicoFisica infractor = (CPersonaJuridicoFisica)value;
                        String sTitulo=" ["+(infractor.getDniCif()==null?"":infractor.getDniCif())+"] "+
                                (infractor.getNombre()==null?"":infractor.getNombre())+ " "+
                                (infractor.getApellido1()==null?"":infractor.getApellido1())+ " "+
                                (infractor.getApellido2()==null?"":infractor.getApellido2());
                          JLabel label =(JLabel) super.getListCellRendererComponent(l,sTitulo,
                                                          i, s, f);

                        try{label.setIcon(iconPersona);}catch(Exception e){}
                        return label;
              }});
          jListInfractores.setModel(listModel);
        }catch(Exception e)
        {
            logger.error("Error al cargar los infractores: ",e);
        }
    }
    private void initComponents()
    {
         jLabelNumero = new javax.swing.JLabel();
         jTextFieldNumero = new com.geopista.app.utilidades.TextField(32);
         jComboBoxTipo = new ComboBoxEstructuras(com.geopista.app.contaminantes.Estructuras.getListaTipo(),
                          null,com.geopista.app.contaminantes.Constantes.Locale);
         jLabelTipo = new javax.swing.JLabel();
         jLabelRazon = new javax.swing.JLabel();
         jComboBoxRazon = new ComboBoxEstructuras(com.geopista.app.contaminantes.Estructuras.getListaRazonEstudio(),
                          null,com.geopista.app.contaminantes.Constantes.Locale);
         jScrollPaneAsunto = new javax.swing.JScrollPane();
         jTextPaneAsunto = new com.geopista.app.utilidades.TextPane(1000);
         jLabelAsunto = new javax.swing.JLabel();
         jPanelInspeciones = new javax.swing.JPanel();
         jPanelInfractores = new javax.swing.JPanel();
         jScrollPaneInspecciones = new javax.swing.JScrollPane();
         jScrollPaneInfractores = new javax.swing.JScrollPane();
         jTableInspeciones = new javax.swing.JTable();
         jListInfractores = new javax.swing.JList();
         jPanelBotoneraInspeciones = new javax.swing.JPanel();
         jPanelBotoneraInfractores = new javax.swing.JPanel();
         jButtonAddInspeccion = new javax.swing.JButton();
         jButtonAddInfractor= new javax.swing.JButton();
         jButtonBorrarInspeccion = new javax.swing.JButton();
         jButtonBorrarInfractor = new javax.swing.JButton();
         jButtonShowInspecion = new javax.swing.JButton();
         jButtonShowInfractor = new javax.swing.JButton();
         jPanelDireccion = new javax.swing.JPanel();
         jComboBoxTipoVia = new ComboBoxEstructuras(com.geopista.app.contaminantes.Estructuras.getListaTipoVia(),
                          null,com.geopista.app.contaminantes.Constantes.Locale);;
         jLabelTipoVia = new javax.swing.JLabel();
         jLabelNombreVia = new javax.swing.JLabel();
         jTextFieldNombreVia = new com.geopista.app.utilidades.TextField(128);
         jLabelNumeroVia = new javax.swing.JLabel();
         jTextFieldNumeroVia = new com.geopista.app.utilidades.TextField(10);
         jLabelCodigoPostal = new javax.swing.JLabel();
         jTextFieldCodigoPostal = new JNumberTextField(JNumberTextField.NUMBER,new Integer(99999));
         jLabelFechaInicio = new javax.swing.JLabel();
         jTextFieldFechaInicio = new JFormattedTextField((new SimpleDateFormat("dd-MM-yyyy")));
         jButtonFechaInicio = new CalendarButton(jTextFieldFechaInicio);
         jLabelFechaFin = new javax.swing.JLabel();
         jTextFieldFechaFin = new JFormattedTextField((new SimpleDateFormat("dd-MM-yyyy")));;
         jButtonFechaFin = new CalendarButton(jTextFieldFechaFin);
         jTabbedPaneAux = new javax.swing.JTabbedPane();
         try
         {
            ClassLoader cl =this.getClass().getClassLoader();
            iconPersona= new javax.swing.ImageIcon(cl.getResource("img/infractor.jpg"));
         }catch(Exception e){}
         jButtonBuscar = new javax.swing.JButton();

        Icon iconoZoom=null;
        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            iconoZoom = new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
            jButtonBuscar.setIcon(iconoZoom);
        }catch(Exception e){}

          setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
          setMinimumSize(new java.awt.Dimension(500, 500));
          add(jLabelNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
          add(jTextFieldNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, -1));
          jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buscarActividad();
                    }
                });
          add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 20, 20));
          add(jComboBoxTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 140, -1));
          add(jLabelTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 70, -1));
          add(jLabelRazon, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));
          add(jComboBoxRazon, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 150, -1));
          jScrollPaneAsunto.setViewportView(jTextPaneAsunto);
          add(jScrollPaneAsunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 480, 60));
          add(jLabelAsunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 380, -1));
          jPanelInspeciones.setLayout(new java.awt.BorderLayout());
          jPanelInfractores.setLayout(new java.awt.BorderLayout());
          jScrollPaneInspecciones.setViewportView(jTableInspeciones);
          ListSelectionModel rowSM = jTableInspeciones.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    seleccionarInspeccion(e);
                }
            });
          jScrollPaneInfractores.setViewportView(jListInfractores);

          jPanelInfractores.add(jScrollPaneInfractores, java.awt.BorderLayout.CENTER);
          jPanelInspeciones.add(jScrollPaneInspecciones, java.awt.BorderLayout.CENTER);
          jPanelBotoneraInspeciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
          jPanelBotoneraInfractores.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
          jButtonAddInspeccion.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        addInspeccion();
                    }
                });
          jButtonAddInfractor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addInfractor();
                }
            });
          jPanelBotoneraInfractores.add(jButtonAddInfractor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 75, -1));
          jPanelBotoneraInspeciones.add(jButtonAddInspeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 75, -1));
          jPanelBotoneraInfractores.add(jButtonBorrarInfractor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 75, -1));
          jPanelBotoneraInspeciones.add(jButtonBorrarInspeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 75, -1));
          jButtonBorrarInfractor.addActionListener(new java.awt.event.ActionListener() {
                                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                                       borrarInfractor();
                                   }
          });

          jButtonBorrarInspeccion.addActionListener(new java.awt.event.ActionListener() {
                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                               borrarInspeccion();
                           }
                       });

          jButtonShowInfractor.addActionListener(new java.awt.event.ActionListener() {
                                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                                      mostrarInfractor();
                                  }
                              });

          jButtonShowInspecion.addActionListener(new java.awt.event.ActionListener() {
                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                               mostrarInspeccion();
                           }
                       });

          jPanelBotoneraInfractores.add(jButtonShowInfractor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 75, -1));
          jPanelBotoneraInspeciones.add(jButtonShowInspecion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 75, -1));
          jPanelInfractores.add(jPanelBotoneraInfractores, java.awt.BorderLayout.EAST);
          jPanelInspeciones.add(jPanelBotoneraInspeciones, java.awt.BorderLayout.EAST);
          jTabbedPaneAux.addTab(messages.getString("JPanelActividad.jPanelInspeciones"),jPanelInspeciones);
          jTabbedPaneAux.addTab(messages.getString("JPanelActividad.jPanelInfractores"), jPanelInfractores);
          add(jTabbedPaneAux, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 500, 210));
          jPanelDireccion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
          jPanelDireccion.add(jComboBoxTipoVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 110, -1));
          jPanelDireccion.add(jLabelTipoVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 80, -1));
          jPanelDireccion.add(jLabelNombreVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 70, -1));
          jPanelDireccion.add(jTextFieldNombreVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 400, -1));
          jPanelDireccion.add(jLabelNumeroVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
          jPanelDireccion.add(jTextFieldNumeroVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 60, -1));
          jPanelDireccion.add(jLabelCodigoPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));
          jPanelDireccion.add(jTextFieldCodigoPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 100, -1));
          add(jPanelDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 500, 110));
          add(jLabelFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 70, -1));
          add(jTextFieldFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 110, -1));
          add(jButtonFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 20, 20));
          add(jLabelFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 70, -1));
          add(jTextFieldFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, 110, -1));
          add(jButtonFechaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 20, 20));
          actualizarModelo();
    }
    private void actualizarModelo()
      {
        Vector vInspecciones= new Vector();
        if (actividad.getInspecciones() != null){
            for (Enumeration e=actividad.getInspecciones().elements();e.hasMoreElements();){
                Inspeccion inspeccion= (Inspeccion)e.nextElement();
                if (inspeccion.getEstado() != com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_DELETED){
                    vInspecciones.addElement(inspeccion);
                }
            }
        }

          modelInspecciones= new InspeccionesTableModel();
          //modelInspecciones.setModelData(actividad.getInspecciones());
          modelInspecciones.setModelData(vInspecciones);
          sorter = new TableSorted(modelInspecciones);
          sorter.setTableHeader(jTableInspeciones.getTableHeader());
          jTableInspeciones.setModel(sorter);
          jTableInspeciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          TableColumn column = jTableInspeciones.getColumnModel().getColumn(0);
          column.setPreferredWidth(5);
          column = jTableInspeciones.getColumnModel().getColumn(InspeccionesTableModel.idIndex);
          column.setPreferredWidth(1);
          column.setWidth(1);
          column.setResizable(false);
          column.setMaxWidth(1);
      }

     public void enabled(boolean bValor)
     {
         modoEdicion=bValor;
         jButtonAddInspeccion.setEnabled(bValor);
         jButtonAddInfractor.setEnabled(bValor);
         jButtonBorrarInspeccion.setEnabled(bValor);
         jButtonBorrarInfractor.setEnabled(bValor);

         jButtonFechaFin.setEnabled(bValor);
         jButtonFechaInicio.setEnabled(bValor);
         jTextFieldCodigoPostal.setEditable(bValor);
         jTextFieldFechaFin.setEditable(bValor);
         jTextFieldFechaInicio.setEditable(bValor);
         jTextFieldNombreVia.setEditable(bValor);
         jTextFieldNumero.setEditable(bValor);
         jTextFieldNumeroVia.setEditable(bValor);
         jTextPaneAsunto.setEnabled(bValor);
         jTextPaneAsunto.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
         jComboBoxTipo.setEnabled(bValor);
         jComboBoxRazon.setEnabled(bValor);
         jComboBoxTipoVia.setEnabled(bValor);
         jComboBoxTipo.setEditable(bValor);
        jComboBoxRazon.setEditable(bValor);
        jComboBoxTipoVia.setEditable(bValor);


     }

    public void changeScreenLang(ResourceBundle messages)
    {
    	//System.err.println("CAMBIAMOS EL IDIOMA...");
        try
        {
            this.messages=messages;
            jLabelNumero.setText(messages.getString("JPanelActividad.jLabelNumero"));//"Numero:");
            jLabelTipo.setText(messages.getString("JPanelActividad.jLabelTipo"));//"Tipo:");
            jLabelRazon.setText(messages.getString("JPanelActividad.jLabelRazon"));//"Raz\u00f3n del estudio:");
            jLabelAsunto.setText(messages.getString("JPanelActividad.jLabelAsunto"));//"Asunto:");
            //jPanelInspeciones.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelActividad.jPanelInspeciones")));//"Inspecciones"));
            jButtonAddInspeccion.setText(messages.getString("JPanelActividad.jButtonAddInspeccion"));//"A\u00f1adir");
            jButtonAddInfractor.setText(messages.getString("JPanelActividad.jButtonAddInspeccion"));//"A\u00f1adir");
            jButtonBorrarInspeccion.setText(messages.getString("JPanelActividad.jButtonBorrarInspeccion"));//"Borrar");
            jButtonBorrarInfractor.setText(messages.getString("JPanelActividad.jButtonBorrarInspeccion"));//"Borrar");
            jButtonShowInspecion.setText(messages.getString("JPanelActividad.jButtonShowInspecion"));//"Mostrar");
            jButtonShowInfractor.setText(messages.getString("JPanelActividad.jButtonShowInspecion"));//"Mostrar");
            jPanelDireccion.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelActividad.jPanelDireccion")));//"Direcci\u00f3n Postal"));
            jLabelTipoVia.setText(messages.getString("JPanelActividad.jLabelTipoVia"));//"Tipo via:");
            jLabelNombreVia.setText(messages.getString("JPanelActividad.jLabelNombreVia"));//"Nombre v\u00eda:");
            jLabelNumeroVia.setText(messages.getString("JPanelActividad.jLabelNumeroVia"));//"Numero:");
            jLabelCodigoPostal.setText(messages.getString("JPanelActividad.jLabelCodigoPostal"));//"Codigo postal:");
            jLabelFechaInicio.setText(messages.getString("JPanelActividad.jLabelFechaInicio"));//"Fecha Inicio:");
            jLabelFechaFin.setText(messages.getString("JPanelActividad.jLabelFechaFin"));//"Fecha Fin:");

            jTabbedPaneAux.setTitleAt(0, messages.getString("JPanelActividad.jPanelInspeciones"));
            jTabbedPaneAux.setTitleAt(1, messages.getString("JPanelActividad.jPanelInfractores"));

            jButtonShowInspecion.setToolTipText(messages.getString("JPanelActividad.jButtonShowInspecion"));
            jButtonShowInfractor.setToolTipText(messages.getString("JPanelActividad.jButtonShowInspecion"));
            jButtonAddInspeccion.setToolTipText(messages.getString("JPanelActividad.jButtonAddInspeccion"));
            jButtonAddInfractor.setToolTipText(messages.getString("JPanelActividad.jButtonAddInspeccion"));
            jButtonBorrarInspeccion.setToolTipText(messages.getString("JPanelActividad.jButtonBorrarInspeccion"));
            jButtonBorrarInfractor.setToolTipText(messages.getString("JPanelActividad.jButtonBorrarInspeccion"));
            jButtonBuscar.setToolTipText(messages.getString("JPanelActividad.jButtonBuscar"));
            jButtonFechaInicio.setToolTipText(messages.getString("JPanelActividad.jButtonFechaInicio"));
            jButtonFechaFin.setToolTipText(messages.getString("JPanelActividad.jButtonFechaFin"));

            TableColumn tableColumn= jTableInspeciones.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(messages.getString("JPanelActividad.InspeccionesTableModel.col0"));
            tableColumn= jTableInspeciones.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(messages.getString("JPanelActividad.InspeccionesTableModel.col1"));
            tableColumn= jTableInspeciones.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(messages.getString("JPanelActividad.InspeccionesTableModel.col2"));
            tableColumn= jTableInspeciones.getColumnModel().getColumn(3);
            tableColumn.setHeaderValue(messages.getString("JPanelActividad.InspeccionesTableModel.col3"));
            tableColumn= jTableInspeciones.getColumnModel().getColumn(4);

            
            //Actualización de los comboBox
            //tipo...
            String selectedIdTipo=jComboBoxTipo.getSelectedId();
            jComboBoxTipo.removeAllItems();
            jComboBoxTipo.setEstructuras(com.geopista.app.contaminantes.Estructuras.getListaTipo(),
                    null,messages.getLocale().toString(),false);
           if(selectedIdTipo!=null)
        	   jComboBoxTipo.setSelected(selectedIdTipo);
           
           //razón...
           String selectedIdRazon=jComboBoxRazon.getSelectedId();
           jComboBoxRazon.removeAllItems();
           jComboBoxRazon.setEstructuras(com.geopista.app.contaminantes.Estructuras.getListaRazonEstudio(),
                   null,messages.getLocale().toString(),false);
          if(selectedIdRazon!=null)
        	  jComboBoxRazon.setSelected(selectedIdRazon);
          
          //vía...
          String selectedIdVia=jComboBoxTipoVia.getSelectedId();
          jComboBoxTipoVia.removeAllItems();
          jComboBoxTipoVia.setEstructuras(com.geopista.app.contaminantes.Estructuras.getListaTipoVia(),
                  null,messages.getLocale().toString(),false);
         if(selectedIdVia!=null)
        	 jComboBoxTipoVia.setSelected(selectedIdVia);
          
        
        }catch(Exception e)
        {
            logger.error("Error al cargar la etiquetas",e);
        }


    }


    private void addInspeccion()
    {
        //JDialogInspeccion dialogInspeccion = new JDialogInspeccion(this.parent, true, messages,vertedero.getResiduos(),modoEdicion);
        JDialogInspeccion dialogInspeccion = new JDialogInspeccion(this.parent,true, messages,new Inspeccion());
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        dialogInspeccion.setSize(675,650);
        dialogInspeccion.setLocation(d.width/2 - dialogInspeccion.getSize().width/2, d.height/2 - dialogInspeccion.getSize().height/2);
        dialogInspeccion.setResizable(false);
        dialogInspeccion.show();
        if (dialogInspeccion.isAceptado())
        {
            inspeccionSelected=dialogInspeccion.getInspeccion();
            inspeccionSelected.setEstado(com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_ADDED);
            actividad.addInspeccion(inspeccionSelected);
            actualizarModelo();
        }
        dialogInspeccion=null;
      }

        private void addInfractor()
        {
            JDialogInfractor dialogInfractor = new JDialogInfractor(this.parent,true, messages, new CPersonaJuridicoFisica());
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialogInfractor.setSize(360,225);
            dialogInfractor.setLocation(d.width/2 - dialogInfractor.getSize().width/2, d.height/2 - dialogInfractor.getSize().height/2);
            dialogInfractor.setResizable(false);
            dialogInfractor.show();
            if (dialogInfractor.isAceptado())
            {
                actividad.addInfractor(dialogInfractor.getPersona());
                loadInfractores();
            }
            dialogInfractor=null;
          }
    private void mostrarInfractor()
    {
          if (jListInfractores.getSelectedIndex()<0) return;
          ListModel auxList=jListInfractores.getModel();
          CPersonaJuridicoFisica infractor=(CPersonaJuridicoFisica)auxList.getElementAt(jListInfractores.getSelectedIndex());
          JDialogInfractor dialogInfractor = new JDialogInfractor(this.parent,true, messages, infractor );
          Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
          dialogInfractor.setSize(360,225);
          dialogInfractor.setLocation(d.width/2 - dialogInfractor.getSize().width/2, d.height/2 - dialogInfractor.getSize().height/2);
          dialogInfractor.setResizable(false);
          dialogInfractor.setEditable(modoEdicion);
          dialogInfractor.show();
          if (dialogInfractor.isAceptado())
          {
                actividad.addInfractor(dialogInfractor.getPersona());
                loadInfractores();
          }
          dialogInfractor=null;
    }

    private void borrarInfractor()
    {
          if (jListInfractores.getSelectedIndex()<0) return;

          int ok= JOptionPane.showConfirmDialog(this, messages.getString("Licencias.confirmarBorrado"), messages.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
          if (ok == JOptionPane.NO_OPTION) return;

          ListModel auxList=jListInfractores.getModel();
          CPersonaJuridicoFisica infractor=(CPersonaJuridicoFisica)auxList.getElementAt(jListInfractores.getSelectedIndex());
          actividad.deleteInfractor(infractor);
          loadInfractores();
    }

    private void borrarInspeccion(){
        if (inspeccionSelected == null) {
            JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoseleccionado"), JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "INFO");
            dialog.show();
            return;
        }
        int ok= JOptionPane.showConfirmDialog(this, messages.getString("Licencias.confirmarBorrado"), messages.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION) return;

        Inspeccion inspeccion= (Inspeccion)_hInspeccionesActividad.get(inspeccionSelected.getId());
        if (inspeccion != null){
             inspeccion.setEstado(com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_DELETED);
            _hInspeccionesActividad.put(inspeccion.getId(), inspeccion);
            Vector v= new Vector();
            for (Enumeration e1=_hInspeccionesActividad.elements();e1.hasMoreElements();){
               Inspeccion ins= (Inspeccion)e1.nextElement();
               v.add(ins);
            }
            actividad.setInspecciones(v);
        }

        actualizarModelo();
    }
    private void buscarActividad()
    {
            // Aqui añado lo de buscar actividades contaminantes
            JDialogBuscarContaminantes dialogBuscar = new JDialogBuscarContaminantes(parent, true, messages,parent);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialogBuscar.setSize(525, 500);
            dialogBuscar.setLocation(d.width / 2 - dialogBuscar.getSize().width / 2, d.height / 2 - dialogBuscar.getSize().height / 2);
            dialogBuscar.setResizable(false);
            dialogBuscar.show();
            dialogBuscar = null;
    }

    private void mostrarInspeccion()
     {
        if (inspeccionSelected == null) {
			JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoseleccionado"), JOptionPane.INFORMATION_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "INFO");
			dialog.show();
			return;
		 }

         JDialogInspeccion dialogInspeccion = new JDialogInspeccion(this.parent,true, messages,inspeccionSelected);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         dialogInspeccion.setSize(675,650);
         dialogInspeccion.setLocation(d.width/2 - dialogInspeccion.getSize().width/2, d.height/2 - dialogInspeccion.getSize().height/2);
         dialogInspeccion.setResizable(false);
         dialogInspeccion.enabled(modoEdicion);
         dialogInspeccion.show();
         if (dialogInspeccion.isAceptado())
         {
             Inspeccion ins=dialogInspeccion.getInspeccion();
             inspeccionSelected.copy(ins);
              actualizarModelo();
         }
         dialogInspeccion=null;
       }

    private void seleccionarInspeccion(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
        } else {
            int selectedRow = lsm.getMinSelectionIndex();
            inspeccionSelected = (Inspeccion) sorter.getValueAt(selectedRow, InspeccionesTableModel.idIndex);
            if (inspeccionSelected == null) {
                JOptionPane optionPane = new JOptionPane(messages.getString("JFrameVertedero.mensaje.vernoencontrado"), JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "INFO");
                dialog.show();
            }
        }
    }

    private javax.swing.JButton jButtonShowInspecion;
    private javax.swing.JButton jButtonShowInfractor;
    private ComboBoxEstructuras jComboBoxTipo;
    private ComboBoxEstructuras jComboBoxTipoVia;
    private ComboBoxEstructuras jComboBoxRazon;
    private javax.swing.JLabel jLabelAsunto;
    private javax.swing.JLabel jLabelCodigoPostal;
    private javax.swing.JLabel jLabelFechaFin;
    private javax.swing.JLabel jLabelFechaInicio;
    private javax.swing.JLabel jLabelNombreVia;
    private javax.swing.JLabel jLabelNumero;
    private javax.swing.JLabel jLabelNumeroVia;
    private javax.swing.JLabel jLabelRazon;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JLabel jLabelTipoVia;
    private javax.swing.JScrollPane jScrollPaneAsunto;
    private javax.swing.JScrollPane jScrollPaneInspecciones;
    private javax.swing.JScrollPane jScrollPaneInfractores;
    private javax.swing.JTable jTableInspeciones;
    private javax.swing.JList jListInfractores;
    private JNumberTextField jTextFieldCodigoPostal;
    private javax.swing.JFormattedTextField jTextFieldFechaFin;
    private javax.swing.JFormattedTextField jTextFieldFechaInicio;
    private com.geopista.app.utilidades.TextField jTextFieldNombreVia;
    private com.geopista.app.utilidades.TextField jTextFieldNumero;
    private com.geopista.app.utilidades.TextField jTextFieldNumeroVia;
    private com.geopista.app.utilidades.TextPane jTextPaneAsunto;
    private javax.swing.JPanel jPanelDireccion;
    private javax.swing.JPanel jPanelInspeciones;
    private javax.swing.JPanel jPanelInfractores;
    private javax.swing.JButton jButtonAddInspeccion;
    private javax.swing.JButton jButtonAddInfractor;
    private javax.swing.JButton jButtonBorrarInspeccion;
    private javax.swing.JButton jButtonBorrarInfractor;
    private CalendarButton jButtonFechaFin;
    private CalendarButton jButtonFechaInicio;
    private javax.swing.JPanel jPanelBotoneraInspeciones;
    private javax.swing.JPanel jPanelBotoneraInfractores;
    private javax.swing.JTabbedPane jTabbedPaneAux;
    private javax.swing.JButton jButtonBuscar;




}
