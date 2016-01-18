package com.geopista.app.metadatos.paneles;

import com.geopista.app.metadatos.componentes.*;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.metadatos.EX_Extent;
import com.geopista.protocol.metadatos.EX_GeographicBoundingBox;
import com.geopista.protocol.metadatos.EX_VerticalExtent;

import javax.swing.*;
import java.util.ResourceBundle;
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
 * Time: 12:53:51
 */
public class JPanelExtension  extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelExtension.class);
    ResourceBundle messages;
    private EX_Extent extent;

    public JPanelExtension(ResourceBundle messages) {
        super();
        initComponents();
        changeScreenLang(messages);
    }

    private void initComponents() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTextFieldEscala = new NumberTextFieldOptativo();
        jLabelEscala = new javax.swing.JLabel();
        jTextPaneDes = new TextPaneCondicional(255);
        jLabelDescripcion = new javax.swing.JLabel();
        jPanelCoordenadas = new javax.swing.JPanel();
        jTextFieldNorte = new NumberTextFieldObligatorio(JNumberTextField.REAL,new Float(99999.99));
        jTextFieldSur = new NumberTextFieldObligatorio(JNumberTextField.REAL,new Float(99999.99));
        jTextFieldOeste = new NumberTextFieldObligatorio(JNumberTextField.REAL,new Float(99999.99));
        jTextFieldEste = new NumberTextFieldObligatorio(JNumberTextField.REAL,new Float(99999.99));
        jLabelNorte = new javax.swing.JLabel();
        jLabelEste = new javax.swing.JLabel();
        jLabelSur = new javax.swing.JLabel();
        jLabelOeste = new javax.swing.JLabel();
        jRadioButtonExclusion = new javax.swing.JRadioButton();
        jRadioButtonInclusion = new javax.swing.JRadioButton();
        jLabelValorMinimo = new javax.swing.JLabel();
        jLabelValorMaximo = new javax.swing.JLabel();
        jLabelUnidades= new javax.swing.JLabel();
        jTextFieldValorMinimo= new NumberTextFieldObligatorio(JNumberTextField.NUMBER,new Integer(999999999));
        jTextFieldValorMaximo= new NumberTextFieldObligatorio(JNumberTextField.NUMBER,new Integer(999999999));
        jTextFieldUnidades=new TextFieldObligatorio(50);
        jPanelVertical=new javax.swing.JPanel();
          this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), new Color(81,181,103)));
        add(jTextFieldEscala, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 70, -1));
        add(jLabelEscala, new org.netbeans.lib.awtextra.AbsoluteConstraints(200,0,-1, -1));


        JScrollPane scrollPaneDes =new JScrollPane(jTextPaneDes);
        add(scrollPaneDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, 30));
        add(jLabelDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jPanelCoordenadas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelCoordenadas.add(jTextFieldNorte, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 60, -1));
        jPanelCoordenadas.add(jTextFieldSur, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 60, -1));
        jPanelCoordenadas.add(jTextFieldOeste, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 60, -1));
        jPanelCoordenadas.add(jTextFieldEste, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 60, -1));

        jLabelNorte.setText("N");
        jPanelCoordenadas.add(jLabelNorte, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));
        jLabelEste.setText("E");
        jPanelCoordenadas.add(jLabelEste, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, -1));

        jLabelSur.setText("S");
        jPanelCoordenadas.add(jLabelSur, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, -1, -1));

        jLabelOeste.setText("O");
        jPanelCoordenadas.add(jLabelOeste, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButtonExclusion);
        group.add(jRadioButtonInclusion);
        jRadioButtonInclusion.setSelected(true);
        jPanelCoordenadas.add(jRadioButtonExclusion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 115, -1, -1));
        jPanelCoordenadas.add(jRadioButtonInclusion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 115, -1, -1));

        add(jPanelCoordenadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 240, 150));

        jPanelVertical.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelVertical.add(jLabelValorMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,20,-1,-1));
        jPanelVertical.add(jLabelValorMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,50,-1,-1));
        jPanelVertical.add(jLabelUnidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,80,-1,-1));

        jPanelVertical.add(jTextFieldValorMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 140,-1));
        jPanelVertical.add(jTextFieldValorMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100,50,140,-1));
        jPanelVertical.add(jTextFieldUnidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(140,80,100,-1));

        add(jPanelVertical, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,50,250,110));


    }

    public Long getResolucion() throws Exception
    {
        try
        {
            if (jTextFieldEscala.getText().length()>0)
                return new Long(jTextFieldEscala.getNumber().longValue());
        }catch (Exception e)
        {
            throw e;
        }

        return null;
    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jLabelEscala.setText(messages.getString("JPanelExtension.jLabelEscala"));//"Escala:");
        jLabelDescripcion.setText(messages.getString("JPanelExtension.jLabelDescripcion"));//"Descripcion");
        jPanelCoordenadas.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.yellow),messages.getString("JPanelExtension.jPanelCoordenadas")));//"Coordenadas de la Extensi\u00f3n"));
        jRadioButtonExclusion.setText(messages.getString("JPanelExtension.jRadioButtonExclusion"));//"Exclusi\u00f3n");
        jRadioButtonInclusion.setText(messages.getString("JPanelExtension.jRadioButtonInclusion"));//"Inclusi\u00f3n");
        jPanelVertical.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.yellow),messages.getString("JPanelExtension.jPanelVertical")));//"Extensión vertical"));
        jLabelValorMinimo.setText(messages.getString("JPanelExtension.jLabelValorMinimo"));//"Valor mínimo:");
        jLabelValorMaximo.setText(messages.getString("JPanelExtension.jLabelValorMaximo"));//"Valor máximo:");
        jLabelUnidades.setText(messages.getString("JPanelExtension.jLabelUnidades"));//"Unidades de medida:");



    }

     public boolean validar(){
        if(jTextFieldEste.getText().length()>0 ||
                    jTextFieldOeste.getText().length()>0 ||
                    jTextFieldNorte.getText().length()>0 ||
                    jTextFieldSur.getText().length()>0)
        {

                if(jTextFieldEste.getText().length()>0 &&
                    jTextFieldOeste.getText().length()>0 &&
                    jTextFieldNorte.getText().length()>0 &&
                    jTextFieldSur.getText().length()>0)
                {
                    logger.debug("Bounding box bien configurado");
                }
                else return false;
        }
         if (jTextFieldValorMaximo.getText().length()>0 ||
                     jTextFieldValorMinimo.getText().length()>0 ||
                     jTextFieldUnidades.getText().length()>0)
         {
                if (jTextFieldValorMaximo.getText().length()>0 &&
                   jTextFieldValorMinimo.getText().length()>0 &&
                   jTextFieldUnidades.getText().length()>0)
                {
                    logger.debug("Vertical extension bien configurado");
                }
                else
                   return false;
         }
        return true;
    }
    public void load(EX_Extent extent, Long lResolucion)
    {
        this.extent=extent;
        if (extent!=null)
        {
            jTextPaneDes.setText(extent.getDescription());
            if (extent.getBox()!=null)
            {
                jTextFieldEste.setNumber(new Float(extent.getBox().getEast()));
                jTextFieldOeste.setNumber(new Float(extent.getBox().getWest()));
                jTextFieldNorte.setNumber(new Float(extent.getBox().getNorth()));
                jTextFieldSur.setNumber(new Float(extent.getBox().getSouth()));

                jRadioButtonInclusion.setSelected(extent.getBox().isExtenttypecode());
                jRadioButtonExclusion.setSelected(!extent.getBox().isExtenttypecode());
            }else
            {
                jTextFieldEste.setText("");
                jTextFieldOeste.setText("");
                jTextFieldNorte.setText("");
                jTextFieldSur.setText("");
            }
            if (extent.getVertical()!=null)
            {
                jTextFieldValorMaximo.setNumber(new Long(extent.getVertical().getMax()));
                jTextFieldValorMinimo.setNumber(new Long(extent.getVertical().getMin()));
                jTextFieldUnidades.setText(extent.getVertical().getUnit());
            }
            else
            {
                jTextFieldValorMaximo.setText("");
                jTextFieldValorMinimo.setText("");
                jTextFieldUnidades.setText("");
            }

        }else
        {
            jTextPaneDes.setText("");
            jTextFieldEste.setText("");
            jTextFieldOeste.setText("");
            jTextFieldNorte.setText("");
            jTextFieldSur.setText("");
            jTextFieldValorMaximo.setText("");
            jTextFieldValorMinimo.setText("");
            jTextFieldUnidades.setText("");
        }
        if (lResolucion!=null) jTextFieldEscala.setNumber(lResolucion);
        else jTextFieldEscala.setText("");

    }
    public EX_Extent getExtent() throws Exception{
        boolean extentnulo=true;
        if (jTextPaneDes.getText().length()>0)
        {
            if (extent==null) extent= new EX_Extent();
            extent.setDescription(jTextPaneDes.getText());
            extentnulo=false;
        }
        else
            if (extent!=null) extent.setDescription(null);

        if(jTextFieldEste.getText().length()>0 &&
           jTextFieldOeste.getText().length()>0 &&
           jTextFieldNorte.getText().length()>0 &&
           jTextFieldSur.getText().length()>0)
        {
            if (extent==null) extent= new EX_Extent();
            if (extent.getBox()==null) extent.setBox(new EX_GeographicBoundingBox());
            extent.getBox().setEast(jTextFieldEste.getNumber().floatValue());
            extent.getBox().setWest(jTextFieldOeste.getNumber().floatValue());
            extent.getBox().setNorth(jTextFieldNorte.getNumber().floatValue());
            extent.getBox().setSouth(jTextFieldSur.getNumber().floatValue());
            extent.getBox().setExtenttypecode(jRadioButtonInclusion.isSelected());

            extentnulo=false;
        }
        else
            if (extent!=null) extent.setBox(null);

        if (jTextFieldValorMaximo.getText().length()>0 &&
            jTextFieldValorMinimo.getText().length()>0 &&
            jTextFieldUnidades.getText().length()>0)
        {
            if (extent==null) extent= new EX_Extent();
            if (extent.getVertical()==null) extent.setVertical(new EX_VerticalExtent());
            extent.getVertical().setMax(jTextFieldValorMaximo.getNumber().longValue());
            extent.getVertical().setMin(jTextFieldValorMinimo.getNumber().longValue());
            extent.getVertical().setUnit(jTextFieldUnidades.getText());
            extentnulo=false;
        }
        else
            if (extent!=null) extent.setVertical(null);

        if (extentnulo) extent=null;
        return extent;
    }

    public void setExtent(EX_Extent extent) {
        this.extent = extent;
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("JPanelIdentificacion.jPanelExtension"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jLabelDescripcion"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextPaneDes.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jLabelEscala"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldEscala.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jPanelVertical"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jLabelValorMinimo"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldValorMinimo.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jLabelValorMaximo"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldValorMaximo.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jLabelUnidades"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldUnidades.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jPanelCoordenadas"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jTextFieldNorte"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldNorte.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jTextFieldSur"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldSur.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jTextFieldEste"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldEste.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jTextFieldOeste"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextFieldOeste.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelExtension.jRadioButtonInclusion"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,(jRadioButtonInclusion.isSelected()?messages.getString("JPanelExtension.si")
                                                         :messages.getString("JPanelExtension.no")));
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex," ");




        return Printable.PAGE_EXISTS;
    }

     private NumberTextFieldObligatorio jTextFieldNorte;
     private NumberTextFieldObligatorio jTextFieldOeste;
     private NumberTextFieldObligatorio jTextFieldSur;
     private TextPaneCondicional jTextPaneDes;
     private NumberTextFieldOptativo jTextFieldEscala;
     private NumberTextFieldObligatorio jTextFieldEste;
     private javax.swing.JPanel jPanelCoordenadas;
     private javax.swing.JLabel jLabelNorte;
     private javax.swing.JLabel jLabelOeste;
     private javax.swing.JLabel jLabelSur;
     private javax.swing.JRadioButton jRadioButtonExclusion;
     private javax.swing.JRadioButton jRadioButtonInclusion;
     private javax.swing.JLabel jLabelEste;
     private javax.swing.JLabel jLabelDescripcion;
     private javax.swing.JLabel jLabelEscala;
     private javax.swing.JLabel jLabelValorMinimo;
     private javax.swing.JLabel jLabelValorMaximo;
     private javax.swing.JLabel jLabelUnidades;
     private NumberTextFieldObligatorio jTextFieldValorMinimo;
     private NumberTextFieldObligatorio jTextFieldValorMaximo;
     private TextFieldObligatorio jTextFieldUnidades;
     private javax.swing.JPanel jPanelVertical;
}