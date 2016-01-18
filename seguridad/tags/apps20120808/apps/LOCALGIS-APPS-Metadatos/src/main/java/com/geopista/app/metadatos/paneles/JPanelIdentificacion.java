package com.geopista.app.metadatos.paneles;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.Printable;

import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.metadatos.componentes.*;
import com.geopista.app.metadatos.contactos.CSearchContactos;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.protocol.metadatos.MD_DataIdentification;
import com.geopista.protocol.metadatos.CI_Date;
import com.geopista.protocol.administrador.dominios.DomainNode;

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
 * Time: 16:17:03
 */
public class JPanelIdentificacion extends JPanelPrintable {
    Logger logger = Logger.getLogger(JPanelIdentificacion.class);
    ResourceBundle messages;
    private JFrame framePadre;
    private MD_DataIdentification identificacion;
    private final String  CHARACTERSET="8859Part1";
    private String errorValidacion;
    //private CI_Citation citation;

    public JPanelIdentificacion(ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        initComponents(messages);
        changeScreenLang(messages);
    }

    private void initComponents(ResourceBundle messages) {

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabelCitacion = new javax.swing.JLabel();
        jTextFieldCitacion = new TextFieldObligatorio(0);
        jButtonCitacion = new javax.swing.JButton();

        jTextPanelResumen = new TextPaneObligatorio(255);

        jLabelResumen = new javax.swing.JLabel();
        jTextPanelProposito = new TextPaneOptativo(255);
        jLabelProposito = new javax.swing.JLabel();

        jPanelIdiomas = new PanelCheckBoxEstructurasObligatorio(Estructuras.getListaLanguage(),6,1);

        jLabelCaracteres = new javax.swing.JLabel();
        jComboBoxCaracteres = new ComboBoxCondicional();
        jTextFieldContacto = new TextFieldOptativo(0);
        jLabelRol = new javax.swing.JLabel();
        jComboBoxRol =   new ComboBoxEstructurasOptativo(Estructuras.getListaRoles(),null);
        jButtonContacto = new javax.swing.JButton();
        jPanelRepresentacion = new PanelCheckBoxEstructurasOptativo(Estructuras.getListaSpatialRepresentation(),7,1);
        jTabbedPaneGraficos = new javax.swing.JTabbedPane();
        jPanelContacto= new javax.swing.JPanel();
        jPanelCategoria= new JPanelCategoria(messages);
        jPanelGraficos= new JPanelGraficos(messages);
        jPanelExtension = new JPanelExtension(messages);
        jPanelRestricciones = new JPanelRestricciones(messages);


        this.add(jLabelCitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        this.add(jTextFieldCitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10,430, -1));
        jButtonCitacion.setText(">>");
        jButtonCitacion.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonCitacion.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        mostrarCitacion();
                    }
        });

        this.add(jButtonCitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, -1));
        JScrollPane auxScrollResumen = new JScrollPane(jTextPanelResumen);
        add(auxScrollResumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 450, 60));
        add(jLabelResumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        JScrollPane auxScrollProposito = new JScrollPane(jTextPanelProposito);
        add(auxScrollProposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 450, 60));

        add(jLabelProposito, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        add(jPanelIdiomas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 130));
        add(jLabelCaracteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));

        jComboBoxCaracteres.setModel(new javax.swing.DefaultComboBoxModel(new String[] { CHARACTERSET }));
        add(jComboBoxCaracteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 100, -1));

        jPanelContacto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelContacto.add(jTextFieldContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 190, -1));
        jPanelContacto.add(jLabelRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanelContacto.add(jComboBoxRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 170, -1));

        jButtonContacto.setText(">>");
        jButtonContacto.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarContacto();
            }
        });
        jPanelContacto.add(jButtonContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 20, 20));
        add(jPanelContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 240, 90));

        add(jPanelRepresentacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 150, 150));

        jTabbedPaneGraficos.addTab(messages.getString("JPanelIdentificacion.jPanelCategoria"), jPanelCategoria);
        jTabbedPaneGraficos.addTab(messages.getString("JPanelIdentificacion.jPanelExtension"), jPanelExtension);
        jTabbedPaneGraficos.addTab(messages.getString("JPanelIdentificacion.jPanelGraficos"), jPanelGraficos);
        jTabbedPaneGraficos.addTab(messages.getString("JPanelIdentificacion.jPanelRestricciones"), jPanelRestricciones);
        add(jTabbedPaneGraficos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 540, 190));



    }
    public void changeScreenLang(ResourceBundle messages) {
        this.messages=messages;
        jLabelCitacion.setText(messages.getString("JPanelIdentificacion.jLabelCitacion"));
        jLabelResumen.setText(messages.getString("JPanelIdentificacion.jLabelResumen"));
        jLabelProposito.setText(messages.getString("JPanelIdentificacion.jLabelProposito"));
        jPanelIdiomas.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelIdentificacion.jPanelIdiomas")));
        jLabelCaracteres.setText(messages.getString("JPanelIdentificacion.jLabelCaracteres"));
        jPanelContacto.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelIdentificacion.jPanelContacto")));
        jLabelRol.setText(messages.getString("JPanelIdentificacion.jLabelRol"));//"Rol:");
        jPanelRepresentacion.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelIdentificacion.jPanelRepresentacion")));//"Representaci\u00f3n espacial"));
        if (jPanelExtension!=null) jPanelExtension.changeScreenLang(messages);
        if (jPanelRestricciones!=null) jPanelRestricciones.changeScreenLang(messages);


    }
    public String getRolecode_id()
    {
        if (jComboBoxRol.getSelectedIndex()==0)return null;
        DomainNode rol=(DomainNode) jComboBoxRol.getSelectedItem();
        return rol.getIdNode();
    }
    private String getRolecode()
   {
       if (jComboBoxRol.getSelectedIndex()==0)return "";
       DomainNode rol=(DomainNode) jComboBoxRol.getSelectedItem();
       return rol.getTerm(Constantes.Locale);
   }


    private void mostrarCitacion() {
            // Aqui es donde tengo que mostrar la información de la cita
            com.geopista.app.metadatos.citacion.ShowCitacion citacionDialog = new com.geopista.app.metadatos.citacion.ShowCitacion(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            citacionDialog.setLocation(d.width/2 - citacionDialog.getSize().width/2, d.height/2 -  citacionDialog.getSize().height/2);
            citacionDialog.setResizable(false);
            if (identificacion!=null) citacionDialog.setCitation(identificacion.getCitacion());
            citacionDialog.show();
            if (identificacion==null) identificacion = new MD_DataIdentification();
            identificacion.setCitacion(citacionDialog.getCitation());
            if (citacionDialog.getCitation()==null)
                jTextFieldCitacion.setText("");
            else
                jTextFieldCitacion.setText(citacionDialog.getCitation().getTitle());
            citacionDialog=null;
     }

    private void buscarContacto() {
              // Aqui añado lo de buscar contactos
              CSearchContactos dialogContactos = new CSearchContactos(framePadre, true, messages);
              Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
              dialogContactos.setLocation(d.width/2 - dialogContactos.getSize().width/2, d.height/2 - dialogContactos.getSize().height/2);
              dialogContactos.setResizable(false);
              if ((identificacion!=null) && (identificacion.getResponsibleParty()!=null)) dialogContactos.load(identificacion.getResponsibleParty());
              dialogContactos.show();
              if (dialogContactos.isSeleccionado()&&dialogContactos.getContactoSelected()!=null)
               {
                   if (identificacion==null) identificacion=new MD_DataIdentification();
                   identificacion.setResponsibleParty(dialogContactos.getContactoSelected());
                   jTextFieldContacto.setText(identificacion.getResponsibleParty().toString());
               }

              dialogContactos=null;
       }
       public boolean validar()
       {
            if (identificacion==null ||
                    identificacion.getCitacion()==null ||
                    identificacion.getCitacion().getTitle()==null || identificacion.getCitacion().getTitle().length()<=0)
            {
                errorValidacion="error.identificacioncitacionnula";
                return false;
            }
            if (jPanelIdiomas.getMarcados()==null || jPanelIdiomas.getMarcados().size()<=0)
            {
                errorValidacion="error.identificacionnoidiomas";
                return false;
            }
            if (jTextPanelResumen.getText().length()<=0)
            {
                 errorValidacion="error.identificacionresumennulo";
                 return false;
            }
            if (!jPanelExtension.validar())
            {
                 errorValidacion="error.identificacionextension";
                 return false;
            }
        return true;
    }


    public void load(MD_DataIdentification identificacion)
    {
        this.identificacion=identificacion;
        if (identificacion !=null)
        {
            if (identificacion.getCitacion()!=null)
                jTextFieldCitacion.setText(identificacion.getCitacion().getTitle());
            else
                jTextFieldCitacion.setText("");
            jTextPanelResumen.setText(identificacion.getResumen());
            jTextPanelProposito.setText(identificacion.getPurpose());
            jPanelIdiomas.setMarcados(identificacion.getIdiomas());
            if (identificacion.getResponsibleParty()!=null)
                jTextFieldContacto.setText(identificacion.getResponsibleParty().toString());
            else
                jTextFieldContacto.setText("");
            jPanelRepresentacion.setMarcados(identificacion.getrEspacial());
            jPanelCategoria.setMarcados(identificacion.getCategorias());
            jComboBoxRol.setSelected(identificacion.getRolecode_id());
            jPanelGraficos.load(identificacion.getGraficos());
            jPanelExtension.load(identificacion.getExtent(),identificacion.getResolucion());
            jPanelRestricciones.load(identificacion.getConstraint());

        }else
        {
            jTextPanelResumen.setText("");
            jTextPanelProposito.setText("");
            jPanelIdiomas.setMarcados(null);
            jPanelRepresentacion.setMarcados(null);
            jPanelCategoria.setMarcados(null);
            jComboBoxRol.setSelected(null);
            jTextFieldCitacion.setText("");
            jTextFieldContacto.setText("");
            jPanelGraficos.load(null);
            jPanelExtension.load(null,null);
            jPanelRestricciones.load(null);
        }
    }


    private void guardarCambios() throws Exception
     {
         if (identificacion==null) identificacion = new MD_DataIdentification();
         identificacion.setResumen(jTextPanelResumen.getText());
         identificacion.setPurpose(jTextPanelProposito.getText());
         identificacion.setCharacterset(CHARACTERSET);
         if (identificacion.getResponsibleParty()!=null)
             identificacion.setRolecode_id(getRolecode_id());
         else
             identificacion.setRolecode_id(null);
         identificacion.setIdiomas(jPanelIdiomas.getMarcados());
         identificacion.setrEspacial(jPanelRepresentacion.getMarcados());
         identificacion.setCategorias(jPanelCategoria.getMarcados());
         identificacion.setGraficos(jPanelGraficos.getGraficos());
         identificacion.setResolucion(jPanelExtension.getResolucion());
         identificacion.setExtent(jPanelExtension.getExtent());
         identificacion.setConstraint(jPanelRestricciones.getConstraint());
     }

    public MD_DataIdentification getIdentificacion() throws Exception{
        guardarCambios();
        return identificacion;
    }

    public void setIdentificacion(MD_DataIdentification identificacion) {

        jPanelExtension.setExtent(identificacion.getExtent());
        jPanelRestricciones.setConstraint(identificacion.getConstraint());
        this.identificacion = identificacion;
    }

    public String getErrorValidacion() {
        return errorValidacion;
    }

    public void setErrorValidacion(String errorValidacion) {
        this.errorValidacion = errorValidacion;
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelIdentificacion.title"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jLabelCitacion"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldCitacion.getText());
        if (identificacion!=null && identificacion.getCitacion()!=null && identificacion.getCitacion().getCI_Dates()!=null)
        {
            printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("ShowFechaCitacion.jLabelFecha"));
            for (Enumeration e=identificacion.getCitacion().getCI_Dates().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,((CI_Date)e.nextElement()).toString());
            }
        }

        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jLabelResumen"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextPanelResumen.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jLabelProposito"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextPanelProposito.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelIdiomas"));
        if (jPanelIdiomas.getStringMarcados()!=null && jPanelIdiomas.getStringMarcados().size()>0)
        {
              for  (Enumeration e=jPanelIdiomas.getStringMarcados().elements();e.hasMoreElements();)
              {
                  printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
              }
        }
        else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");

        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jLabelCaracteres"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,CHARACTERSET);
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelRepresentacion"));
        if (jPanelRepresentacion.getStringMarcados()!=null&&jPanelRepresentacion.getStringMarcados().size()>0)
        {
              for  (Enumeration e=jPanelRepresentacion.getStringMarcados().elements();e.hasMoreElements();)
              {
                  printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
              }
        }
        else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");

        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelContacto"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldContacto.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jLabelRol"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,getRolecode());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelCategoria"));
        if (jPanelCategoria.getStringMarcados()!=null&&jPanelCategoria.getStringMarcados().size()>0)
        {
              for  (Enumeration e=jPanelCategoria.getStringMarcados().elements();e.hasMoreElements();)
              {
                  printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
              }
        }
        else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");

        printSuperTitulo((Graphics2D)g,pageFormat,pageIndex," ");
        jPanelExtension.printPanel(g, pageFormat, pageIndex);
        jPanelGraficos.printPanel(g, pageFormat, pageIndex);
        jPanelRestricciones.printPanel(g, pageFormat, pageIndex);
        return Printable.PAGE_EXISTS;
    }

     private javax.swing.JLabel jLabelCitacion;
     private javax.swing.JTextField jTextFieldCitacion;
     protected javax.swing.JButton jButtonCitacion;
     protected TextPaneObligatorio jTextPanelResumen;
     protected TextPaneOptativo jTextPanelProposito;
     private TextFieldOptativo jTextFieldContacto;
     protected PanelCheckBoxEstructurasObligatorio jPanelIdiomas;
     protected PanelCheckBoxEstructurasOptativo jPanelRepresentacion;
      private javax.swing.JPanel jPanelContacto;
     private javax.swing.JLabel jLabelResumen;
     private javax.swing.JLabel jLabelRol;
     private javax.swing.JLabel jLabelProposito;

     private javax.swing.JLabel jLabelCaracteres;
     protected javax.swing.JButton jButtonContacto;
     protected javax.swing.JComboBox jComboBoxCaracteres;
     protected ComboBoxEstructuras jComboBoxRol;

     protected javax.swing.JTabbedPane jTabbedPaneGraficos;
     protected JPanelCategoria jPanelCategoria;
     protected JPanelGraficos jPanelGraficos;
     protected JPanelExtension jPanelExtension;
     protected JPanelRestricciones jPanelRestricciones;




}