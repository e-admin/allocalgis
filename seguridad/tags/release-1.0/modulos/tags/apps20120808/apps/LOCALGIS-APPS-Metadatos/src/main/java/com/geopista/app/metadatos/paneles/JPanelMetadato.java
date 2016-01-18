package com.geopista.app.metadatos.paneles;

import com.geopista.app.metadatos.contactos.CSearchContactos;
import com.geopista.app.metadatos.componentes.*;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.MD_Metadata;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.Calendar;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;

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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 29-jul-2004
 * Time: 11:29:59
 */
public class JPanelMetadato extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelMetadato.class);
    ResourceBundle messages;
    private JFrame framePadre;
    private final String  CHARACTERSET="8859Part1";
    private CI_ResponsibleParty contacto=null;
    private String errorValidacion=null;

    public JPanelMetadato(ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        initComponents();
        changeScreenLang(messages);
    }

    private void initComponents() {

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabelRol = new javax.swing.JLabel();
        jComboBoxRol = new ComboBoxEstructurasObligatorio(Estructuras.getListaRoles(),null);
        jLabelNivel = new javax.swing.JLabel();
        jComboBoxNivel= new ComboBoxEstructurasCondicional(Estructuras.getListaScopeCode(),null);
        jLabelIdioma = new javax.swing.JLabel();
        jComboBoxIdioma = new ComboBoxEstructurasCondicional(Estructuras.getListaLanguage(),null);
        jTextFieldFecha = new FormattedTextFieldObligatorio();

        jCalendarButtonFecha= new CalendarButton(jTextFieldFecha);
        jTextFieldStdName = new TextFieldOptativo(255);
        jTextFieldStdName.setEditable(false);
        jLabelStdName = new javax.swing.JLabel();
        jTextFieldVersion = new TextFieldOptativo(0);
        jTextFieldStdName.setEditable(false);
        jLabelVersion = new javax.swing.JLabel();
        jLabelCodificacion = new javax.swing.JLabel();
        jComboBoxCodificacion = new ComboBoxCondicional();
        jLabelCapa = new javax.swing.JLabel();
        jComboBoxCapa = new ComboBoxEstructurasOptativo(Estructuras.getListaCapas(),null);
        jButtonBuscarContacto = new javax.swing.JButton();
        jPanelContacto = new javax.swing.JPanel();
        jLabelFecha = new javax.swing.JLabel();
        jTextFieldContacto = new TextFieldObligatorio(0);



        jPanelContacto = new javax.swing.JPanel();
        jPanelContacto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelContacto.add(jTextFieldContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, -1));
        jButtonBuscarContacto.setText(">>");
        jButtonBuscarContacto.setToolTipText("");
        jButtonBuscarContacto.setPreferredSize(new java.awt.Dimension(30, 30));
        jButtonBuscarContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarContacto();
            }
        });
        jPanelContacto.add(jButtonBuscarContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 20, 20));
        jPanelContacto.add(jLabelRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));
        jPanelContacto.add(jComboBoxRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 20));
        this.add(jPanelContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 350, 100));
        this.add(jLabelIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));
        this.add(jComboBoxIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, -1));
        jTextFieldFecha.setEditable(false);
        this.add(jTextFieldFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 50, 80, -1));
        this.add(jCalendarButtonFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 50, 20, 20));
        jTextFieldStdName.setText("ISO 19115 Geographic Information - Metadata");
        this.add(jTextFieldStdName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 280, -1));
        this.add(jLabelStdName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, -1, -1));
        jTextFieldVersion.setText("ISO 19115:2003");
        this.add(jTextFieldVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 110, 20));
        this.add(jLabelVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));
        this.add(jLabelNivel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));
        this.add(jComboBoxNivel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));
        this.add(jLabelCodificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 140, -1));
        jComboBoxCodificacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { CHARACTERSET }));
        this.add(jComboBoxCodificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 140, -1));
        this.add(jLabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, -1, -1));
        this.add(jLabelCapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 150, -1, -1));
        this.add(jComboBoxCapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 140, 20));


    }

    public void changeScreenLang(ResourceBundle messages) {
        this.messages=messages;
        jPanelContacto.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelMetadato.jPanelContacto")));
        jPanelContacto.getAccessibleContext().setAccessibleName(messages.getString("JPanelMetadato.jPanelContacto"));
        jLabelRol.setText(messages.getString("JPanelMetadato.jLabelRol"));
        jLabelIdioma.setText(messages.getString("JPanelMetadato.jLabelIdioma"));
        jLabelNivel.setText(messages.getString("JPanelMetadato.jLabelNivel"));
        jLabelIdioma.getAccessibleContext().setAccessibleName(messages.getString("JPanelMetadato.jLabelIdioma"));

        jLabelStdName.setText(messages.getString("JPanelMetadato.jLabelStdName"));
        jLabelVersion.setText(messages.getString("JPanelMetadato.jLabelVersion"));
        jLabelCodificacion.setText(messages.getString("JPanelMetadato.jLabelCodificacion"));
        jLabelFecha.setText(messages.getString("JPanelMetadato.jLabelFecha"));
        jLabelCapa.setText(messages.getString("JPanelMetadato.jLabelCapa"));

    }
    private void buscarContacto() {
           // Aqui añado lo de buscar contactos
           CSearchContactos dialogContactos = new CSearchContactos(framePadre, true, messages);
           dialogContactos.setLocation(50,50);
           dialogContactos.setResizable(false);
           if (contacto!=null) dialogContactos.load(contacto);
           dialogContactos.show();
           if (dialogContactos.isSeleccionado()&&dialogContactos.getContactoSelected()!=null)
           {
               contacto=dialogContactos.getContactoSelected();
               jTextFieldContacto.setText(contacto.toString());
           }
           dialogContactos=null;
    }
    public String getIdLanguage()
    {
        DomainNode idioma=(DomainNode) jComboBoxIdioma.getSelectedItem();
        return idioma.getIdNode();
    }
    private String getLanguage()
    {
        DomainNode idioma=(DomainNode) jComboBoxIdioma.getSelectedItem();
        if (idioma==null) return "";
        return idioma.getTerm(Constantes.Locale);
    }
    public String getCharacterset()
    {
        return CHARACTERSET;
    }
    public Date getDatestamp()
    {
        return jCalendarButtonFecha.getCalendar().getTime();
    }
    public String getStandardname()
    {
        return jTextFieldStdName.getText();
    }
    public String getStandardversion()
    {
        return jTextFieldVersion.getText();
    }
    public CI_ResponsibleParty getResponsibleParty()
    {
        return contacto;
    }
    public String getRolecode_id()
    {
        if (jComboBoxRol.getSelectedIndex()==0)return null;
        DomainNode rol=(DomainNode) jComboBoxRol.getSelectedItem();
        return rol.getIdNode();
    }
    private String getRolecode()
    {
        DomainNode rol=(DomainNode) jComboBoxRol.getSelectedItem();
        if (rol==null) return "";
        return rol.getTerm(Constantes.Locale);
    }

    public String getScopecode_id()
    {
        if (jComboBoxNivel.getSelectedIndex()==0)return null;
        DomainNode scopeCode=(DomainNode) jComboBoxNivel.getSelectedItem();
        return scopeCode.getIdNode();
    }
     public String getId_capa()
    {
        if (jComboBoxCapa.getSelectedIndex()==0)return null;
        DomainNode capa=(DomainNode) jComboBoxCapa.getSelectedItem();
        return capa.getIdNode();
    }
     private String getScopecode()
    {
        DomainNode nivel=(DomainNode) jComboBoxNivel.getSelectedItem();
        if (nivel==null) return "";
        return nivel.getTerm(Constantes.Locale);
    }

    public CI_ResponsibleParty getContacto() {
        return contacto;
    }

    public void setContacto(CI_ResponsibleParty contacto) {
        this.contacto = contacto;
    }

    public boolean validar()
    {
        if (jTextFieldFecha.getText().length()<=0)
        {
            errorValidacion="error.metadatofechanula";
            return false;
        }
        if (contacto==null)
        {
            errorValidacion="error.metadatocontactonulo";
            return false;
        }
        if (jComboBoxRol.getSelectedIndex()==0)
        {
           errorValidacion="error.metadatorolnulo";
           return false;
        }
        return true;
    }

    public void load(MD_Metadata metadato)
    {
         jComboBoxIdioma.setSelected(metadato.getLanguage_id());
         Calendar auxCalendar =Calendar.getInstance();
         if (metadato.getDatestamp()!=null)
            auxCalendar.setTime(metadato.getDatestamp());
         else
            auxCalendar=null;
         jCalendarButtonFecha.setFecha(auxCalendar);
         jTextFieldStdName.setText(metadato.getMetadatastandardname()==null?"":metadato.getMetadatastandardname());
         jTextFieldVersion.setText(metadato.getMetadatastandardversion()==null?"":metadato.getMetadatastandardversion());
         jComboBoxRol.setSelected(metadato.getRolecode_id());
         jComboBoxNivel.setSelected(metadato.getScopecode_id());
         jComboBoxCapa.setSelected(metadato.getId_capa());
         contacto=metadato.getResponsibleParty();
         if (contacto==null)
             jTextFieldContacto.setText("");
         else
             jTextFieldContacto.setText(contacto.toString());
    }

    public String getErrorValidacion() {
        return errorValidacion;
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("CMetadatos.title"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelIdioma"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,getLanguage());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelFecha"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldFecha.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelNivel"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,getScopecode());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelStdName"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldStdName.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelVersion"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldVersion.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelCodificacion"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,CHARACTERSET);
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jPanelContacto"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldContacto.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelRol"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,getRolecode());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelMetadato.jLabelCapa"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printSuperTitulo((Graphics2D)g,pageFormat,pageIndex," ");
        


        return Printable.PAGE_EXISTS;
    }

    private javax.swing.JLabel jLabelCodificacion;
    protected javax.swing.JComboBox jComboBoxCodificacion;
    private javax.swing.JPanel jPanelContacto;
    protected javax.swing.JFormattedTextField jTextFieldFecha;

    protected com.geopista.app.utilidades.estructuras.ComboBoxEstructuras jComboBoxIdioma;
    protected com.geopista.app.utilidades.estructuras.ComboBoxEstructuras jComboBoxRol;
    protected com.geopista.app.utilidades.estructuras.ComboBoxEstructuras jComboBoxNivel;
    private javax.swing.JLabel jLabelIdioma;
    private javax.swing.JLabel jLabelCapa;
    protected com.geopista.app.utilidades.estructuras.ComboBoxEstructuras jComboBoxCapa;
    protected javax.swing.JTextField jTextFieldStdName;
    private javax.swing.JLabel jLabelVersion;
    protected javax.swing.JTextField jTextFieldVersion;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelRol;
    private javax.swing.JLabel jLabelNivel;
    private javax.swing.JLabel jLabelStdName;
    private javax.swing.JButton jButtonBuscarContacto;
    protected javax.swing.JTextField jTextFieldContacto;
    protected CalendarButton jCalendarButtonFecha;






}

