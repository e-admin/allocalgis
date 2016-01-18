package com.geopista.app.metadatos.paneles;

import com.geopista.app.metadatos.componentes.TextPaneOptativo;
import com.geopista.app.metadatos.componentes.TextPaneCondicional;
import com.geopista.app.metadatos.componentes.PanelCheckBoxEstructurasOptativo;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.metadatos.MD_LegalConstraint;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.Printable;

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
 * Date: 30-jul-2004
 * Time: 13:10:31
 */
public class JPanelRestricciones extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelRestricciones.class);
    ResourceBundle messages;
     private MD_LegalConstraint constraint;

    public JPanelRestricciones(ResourceBundle messages) {
        super();
        initComponents();
        changeScreenLang(messages);
    }


    private void initComponents() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelLegales = new javax.swing.JPanel();
        jTextPaneOtras = new TextPaneCondicional(255);
        jLabelOtras = new javax.swing.JLabel();
        jTextPaneAcceso = new PanelCheckBoxEstructurasOptativo(Estructuras.getListaRestriction(),Estructuras.getListaRestriction().getLista().size(),1);
        jLabelAcceso = new javax.swing.JLabel();
        jTextPaneRUso = new PanelCheckBoxEstructurasOptativo(Estructuras.getListaRestriction(),Estructuras.getListaRestriction().getLista().size(),1);
        jLabelRUso = new javax.swing.JLabel();
        jTextPaneUso = new TextPaneOptativo(255);
        jLabelLimitaciones = new javax.swing.JLabel();

        JScrollPane auxScrollUso = new JScrollPane(jTextPaneUso);
        add(auxScrollUso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 120));

        add(jLabelLimitaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jPanelLegales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JScrollPane auxScrollOtras = new JScrollPane(jTextPaneOtras);

        jPanelLegales.add(auxScrollOtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 125, 255, 30));

        jPanelLegales.add(jLabelOtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 250, -1));

        JScrollPane auxScrollAcceso = new JScrollPane(jTextPaneAcceso);
        auxScrollAcceso.setBackground(new java.awt.Color(255, 255, 255));
        jTextPaneAcceso.setBackground(new java.awt.Color(255, 255, 255));
        jTextPaneAcceso.setFont(new Font("TimesNewRoman", Font.PLAIN, 10));

        jPanelLegales.add(auxScrollAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 33, 190, 90));

        jPanelLegales.add(jLabelAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, -1));

        JScrollPane auxScrollRUso = new JScrollPane(jTextPaneRUso);
        auxScrollRUso.setBackground(new java.awt.Color(255, 255, 255));
        jTextPaneRUso.setFont(new Font("TimesNewRoman", Font.PLAIN, 10));
        jTextPaneRUso.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLegales.add(auxScrollRUso, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 33, 190, 90));

        jPanelLegales.add(jLabelRUso, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 15, 150, -1));

        add(jPanelLegales, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 400, 160));


    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jPanelLegales.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelRestricciones.title")));
        jLabelLimitaciones.setText(messages.getString("JPanelRestricciones.jLabelLimitaciones"));
        jLabelOtras.setText(messages.getString("JPanelRestricciones.jLabelOtras"));
        jLabelRUso.setText(messages.getString("JPanelRestricciones.jLabelRUso"));
        jLabelAcceso.setText(messages.getString("JPanelRestricciones.jLabelAcceso"));

    }

    public void load(MD_LegalConstraint constraint)
    {
        this.constraint=constraint;
        if (constraint!=null)
        {
            jTextPaneUso.setText(constraint.getUselimitation());
            jTextPaneOtras.setText(constraint.getOtherconstraint());
            jTextPaneAcceso.setMarcados(constraint.getAccess());
            jTextPaneRUso.setMarcados(constraint.getUse());
        }
        else
        {
            jTextPaneUso.setText("");
            jTextPaneOtras.setText("");
            jTextPaneAcceso.setMarcados(null);
            jTextPaneRUso.setMarcados(null);
        }
    }
    public MD_LegalConstraint getConstraint() throws Exception{
        boolean constraintnulo=true;
        if (jTextPaneUso.getText().length()>0)
        {
            if (constraint==null) constraint= new MD_LegalConstraint();
            constraint.setUselimitation(jTextPaneUso.getText());
            constraintnulo=false;
        }
        else
            if (constraint!=null) constraint.setUselimitation(null);

        if (jTextPaneOtras.getText().length()>0)
        {
            if (constraint==null) constraint= new MD_LegalConstraint();
            constraint.setOtherconstraint(jTextPaneOtras.getText());
            constraintnulo=false;
        }
        else
            if (constraint!=null) constraint.setOtherconstraint(null);

        if (jTextPaneAcceso.getMarcados()!=null && jTextPaneAcceso.getMarcados().size()>0)
        {
            if (constraint==null) constraint= new MD_LegalConstraint();
            constraint.setAccess(jTextPaneAcceso.getMarcados());
            constraintnulo=false;
        }
        else
            if (constraint!=null) constraint.setAccess(null);

        if (jTextPaneRUso.getMarcados()!=null && jTextPaneRUso.getMarcados().size()>0)
       {
           if (constraint==null) constraint= new MD_LegalConstraint();
           constraint.setUse(jTextPaneRUso.getMarcados());
           constraintnulo=false;
       }
       else
           if (constraint!=null) constraint.setUse(null);


        if (constraintnulo) constraint=null;
        return constraint;
    }
    public void setConstraint(MD_LegalConstraint constraint)
    {
        this.constraint=constraint;
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelRestricciones"));
        printTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelRestricciones.jLabelLimitaciones"));
        printDetalle((Graphics2D)g, pageFormat,pageIndex,jTextPaneUso.getText());
        printTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelRestricciones.jLabelAcceso"));
        if (jTextPaneAcceso.getStringMarcados()!=null&&jTextPaneAcceso.getStringMarcados().size()>0)
         {
             for (Enumeration e=jTextPaneAcceso.getStringMarcados().elements();e.hasMoreElements();)
             {
                 printDetalle((Graphics2D)g, pageFormat,pageIndex,(String)e.nextElement());
             }
         }
         else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");


        printTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelRestricciones.jLabelRUso"));
        if (jTextPaneRUso.getStringMarcados()!=null&&jTextPaneRUso.getStringMarcados().size()>0)
        {
            for (Enumeration e=jTextPaneRUso.getStringMarcados().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g, pageFormat,pageIndex,(String)e.nextElement());
            }
        }
        else
            printDetalle((Graphics2D)g, pageFormat,pageIndex,"");

        printTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelRestricciones.jLabelOtras"));
        printDetalle((Graphics2D)g, pageFormat,pageIndex,jTextPaneOtras.getText());
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex," ");
        return Printable.PAGE_EXISTS;
    }

     private javax.swing.JLabel jLabelOtras;
     private javax.swing.JLabel jLabelRUso;
     private TextPaneCondicional jTextPaneOtras;
     private PanelCheckBoxEstructurasOptativo jTextPaneAcceso;
     private PanelCheckBoxEstructurasOptativo jTextPaneRUso;
     private TextPaneOptativo jTextPaneUso;
     private javax.swing.JPanel jPanelLegales;
     private javax.swing.JLabel jLabelAcceso;
     private javax.swing.JLabel jLabelLimitaciones;


}
