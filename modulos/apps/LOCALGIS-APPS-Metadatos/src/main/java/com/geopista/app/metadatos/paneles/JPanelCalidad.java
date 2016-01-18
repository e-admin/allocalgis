/**
 * JPanelCalidad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.metadatos.calidad.ElementosTableModel;
import com.geopista.app.metadatos.componentes.ComboBoxEstructurasObligatorio;
import com.geopista.app.metadatos.componentes.ListOptativo;
import com.geopista.app.metadatos.componentes.TextFieldObligatorio;
import com.geopista.app.metadatos.componentes.TextFieldOptativo;
import com.geopista.app.metadatos.componentes.TextPaneObligatorio;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.DQ_DataQuality;
import com.geopista.protocol.metadatos.DQ_Element;
import com.geopista.protocol.metadatos.LI_Linage;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:53:51
 */
public class JPanelCalidad  extends JPanelPrintable {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelCalidad.class);
    ResourceBundle messages;
    private JFrame framePadre;
    private ElementosTableModel	modelElementos;
    private TableSorted sorter;
    DQ_DataQuality calidad;
    private boolean modoEdicion=false;
    private DQ_Element elementoSelected=null;
    private DQ_Element auxElemento;

    public JPanelCalidad(ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        this.messages= messages;
        initComponents();
        changeScreenLang(messages);
    }

    private void initComponents() {
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTableElementos = new javax.swing.JTable();
        jScrollPaneElementos= new javax.swing.JScrollPane();
        jLabelPasos= new javax.swing.JLabel();
        jPanelLinage= new javax.swing.JPanel();
        jTextPanePasos= new ListOptativo(messages,messages.getString("JPanelCalidad.jTextPanePasos"));//"Paso:");
        jButtonMas= new javax.swing.JButton();
        jButtonMenos= new javax.swing.JButton();
        jTextPaneDes= new TextFieldOptativo(50);
        jTextPaneFuentes= new ListOptativo(messages,messages.getString("JPanelCalidad.jTextPaneFuentes"));//"Descripción de la fuente:"));
        jLabelDes=new javax.swing.JLabel();
        jLabelFuentes= new javax.swing.JLabel();
        jTextPaneExplicacion=new  TextPaneObligatorio(255);
        jTextPaneNombre= new TextFieldObligatorio(50);
        jTextPaneValor= new TextFieldObligatorio(50);
        jLabelNombre= new javax.swing.JLabel();
        jLabelValor= new javax.swing.JLabel();
        jTextFieldEspecificacion = new TextFieldObligatorio(0);
        jButtonEspe = new javax.swing.JButton();
        jPanelDetalle = new javax.swing.JPanel();
        jPanelResultado= new javax.swing.JPanel();
        jLabelEspecificacion= new javax.swing.JLabel();
        jLabelExplicacion = new javax.swing.JLabel();
        jCheckBoxConformidad = new javax.swing.JCheckBox();
        jPanelInformes = new javax.swing.JPanel();
        jLabelTipoInforme= new javax.swing.JLabel();
        jComboBoxTipoInforme= new ComboBoxEstructurasObligatorio(Estructuras.getListaTipoInforme(),null);
        jButtonSalvar = new javax.swing.JButton();
        jButtonCancelar =new javax.swing.JButton();
        jButtonAnadir= new javax.swing.JButton();
        jButtonBorrar= new javax.swing.JButton();
        jButtonMasFuentes= new javax.swing.JButton();
        jButtonMenosFuentes= new javax.swing.JButton();
        this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), new Color(81,181,103)));

        jPanelLinage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelLinage.add(jLabelPasos, new org.netbeans.lib.awtextra.AbsoluteConstraints(280,50, -1, -1));
        JScrollPane jScrollPasos= new JScrollPane(jTextPanePasos);
        jPanelLinage.add(jScrollPasos, new org.netbeans.lib.awtextra.AbsoluteConstraints(280,70,190,100));
        jButtonMas.setText("+");
        jButtonMas.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jTextPanePasos.nuevoElemento();
                    }
            });
        jPanelLinage.add(jButtonMas, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 40, -1));
        jButtonMasFuentes.setText("+");
        jButtonMasFuentes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jTextPaneFuentes.nuevoElemento();
                    }
            });
        jPanelLinage.add(jButtonMasFuentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 40, -1));

        jButtonMenos.setText("-");
        jButtonMenos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jTextPanePasos.removeElement();
                    }
            });
        jPanelLinage.add(jButtonMenos, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 40, -1));

        jButtonMenosFuentes.setText("-");
        jButtonMenosFuentes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jTextPaneFuentes.removeElement();
                    }
            });
        jPanelLinage.add(jButtonMenosFuentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(210,110,40,-1));


        jPanelLinage.add(jTextPaneDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 420, -1));
        jPanelLinage.add(jLabelDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanelLinage.add(jLabelFuentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,50, -1, -1));
        JScrollPane jScrollFuentes= new JScrollPane(jTextPaneFuentes);
        jPanelLinage.add(jScrollFuentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,70,190,100));
        add(jPanelLinage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 540, 180));

        jPanelDetalle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelDetalle.add(jLabelTipoInforme, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,15,-1,-1));
        jPanelDetalle.add(jComboBoxTipoInforme, new org.netbeans.lib.awtextra.AbsoluteConstraints(150,15,160,25));

        jPanelDetalle.add(jTextPaneNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 45, 190, -1));
        jPanelDetalle.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,45, -1, -1));

        jPanelDetalle.add(jTextPaneValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(150,75,190,-1));
        jPanelDetalle.add(jLabelValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,75,-1,-1));

        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        salvar();
                    }});

        jPanelDetalle.add(jButtonSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90,280,90,-1));

        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
                                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                                      cancelar();
                                  }
                              });

        jPanelDetalle.add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200,280,90,-1));


        jPanelResultado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelResultado.add(jTextFieldEspecificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 290, -1));

        jButtonEspe.setText("...");
        jButtonEspe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                  mostrarCitacion();
                    }
            });


        jPanelResultado.add(jButtonEspe, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 20, 20));
        jPanelResultado.add(jCheckBoxConformidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,150,-1,-1));
        JScrollPane jScrollExplicacion= new JScrollPane(jTextPaneExplicacion);
        jPanelResultado.add(jScrollExplicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,90,310,60));
        jPanelResultado.add(jLabelExplicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        jPanelResultado.add(jLabelEspecificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1,-1));
        jPanelDetalle.add(jPanelResultado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100,350,180));

        jPanelInformes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jButtonAnadir.addActionListener(new java.awt.event.ActionListener() {
                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                               anadir();
                           }
                       });

        jPanelInformes.add(jButtonAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,290,80,-1));
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                eliminar(); }  });
        jPanelInformes.add(jButtonBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(85,290,80,-1));
        jPanelInformes.add(jPanelDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(165,10,370,310));
        jScrollPaneElementos.setViewportView(jTableElementos);
        //Para seleccionar una fila
        ListSelectionModel rowSM = jTableElementos.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                seleccionarElemento(e);
            }
        });
        jPanelInformes.add(jScrollPaneElementos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,15,150,265));
        add(jPanelInformes,new org.netbeans.lib.awtextra.AbsoluteConstraints(10,185,540,325));
        enabled(false);
    }

    public void changeScreenLang(ResourceBundle messages)
    {
        this.messages=messages;
        jPanelLinage.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.yellow),messages.getString("JPanelCalidad.jPanelLinage")));//"Linage"));
        jLabelPasos.setText(messages.getString("JPanelCalidad.jLabelPasos"));//"Pasos del proceso:");
        jLabelDes.setText(messages.getString("JPanelCalidad.jLabelDes"));//"Descripci\u00f3n:");
        jLabelFuentes.setText(messages.getString("JPanelCalidad.jLabelFuentes"));//"Fuentes:");
        jPanelDetalle.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelCalidad.jPaneDetalle")));//"Detalle"));
        jPanelInformes.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.yellow),messages.getString("JPanelCalidad.jPaneInformes")));//"Informes"));
        jLabelNombre.setText(messages.getString("JPanelCalidad.jLabelNombre"));//"Nombre de la medida:");
        jLabelValor.setText(messages.getString("JPanelCalidad.jLabelValor"));//"Valor de la medida:");
        jPanelResultado.setBorder(new javax.swing.border.TitledBorder(messages.getString("JPanelCalidad.jPaneResultado")));//"Resultado de la conformidad"));
        jCheckBoxConformidad.setText(messages.getString("JPanelCalidad.jCheckBoxConformidad"));//"Conformidad aprobada");
        jLabelExplicacion.setText(messages.getString("JPanelCalidad.jLabelExplicacion"));//"Explicación:");
        jLabelEspecificacion.setText(messages.getString("JPanelCalidad.jLabelEspecificacion"));//"Especificación:");
        jLabelTipoInforme.setText(messages.getString("JPanelCalidad.jLabelTipoInforme"));//"Tipo informe:");
        jButtonSalvar.setText(messages.getString("JPanelCalidad.jButtonSalvar"));//"Salvar");
        jButtonCancelar.setText(messages.getString("JPanelCalidad.jButtonCancelar"));//"Cancelar");
        jButtonAnadir.setText(messages.getString("JPanelCalidad.jButtonAnadir"));//"Añadir");
        jButtonBorrar.setText(messages.getString("JPanelCalidad.jButtonBorrar"));//"Borrar");

    }
     private void mostrarCitacion() {
            com.geopista.app.metadatos.citacion.ShowCitacion citacionDialog = new com.geopista.app.metadatos.citacion.ShowCitacion(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            citacionDialog.setLocation(d.width/2 - citacionDialog.getSize().width/2, d.height/2 -  citacionDialog.getSize().height/2);
            citacionDialog.setResizable(false);
            citacionDialog.setEnabled(modoEdicion);
            if (auxElemento!=null) citacionDialog.setCitation(auxElemento.getCitation());
            citacionDialog.show();
            if (modoEdicion)
            {
                if (auxElemento==null) auxElemento = new DQ_Element();
                auxElemento.setCitation(citacionDialog.getCitation());
                if (citacionDialog.getCitation()==null)
                    jTextFieldEspecificacion.setText("");
                else
                    jTextFieldEspecificacion.setText(citacionDialog.getCitation().getTitle());
            }
            citacionDialog=null;
    }

    public void load(DQ_DataQuality calidad)
    {
        this.calidad=calidad;
        if (calidad!=null && calidad.getLinage()!=null)
        {
            jTextPaneDes.setText(calidad.getLinage().getStatement());
            jTextPaneFuentes.setModel(calidad.getLinage().getSources());
            jTextPanePasos.setModel(calidad.getLinage().getSteps());
        }
        else
        {
            jTextPaneDes.setText("");
            jTextPaneFuentes.setModel(new Vector());
            jTextPanePasos.setModel(new Vector());
        }
        actualizarModelo();
    }

    public DQ_DataQuality getCalidad() {
        boolean calidadnula=true;
        if ((jTextPaneDes.getText().length()>0) ||
            (jTextPaneFuentes.getVectorModel()!=null&&jTextPaneFuentes.getVectorModel().size()>0) ||
            (jTextPanePasos.getVectorModel()!=null&&jTextPanePasos.getVectorModel().size()>0)
        )
        {
            if (calidad==null) calidad= new DQ_DataQuality();
            if (calidad.getLinage()==null) calidad.setLinage(new LI_Linage());
            if (jTextPaneDes.getText().length()>0)
                calidad.getLinage().setStatement(jTextPaneDes.getText());
            else
                calidad.getLinage().setStatement(null);
            if (jTextPaneFuentes.getVectorModel()!=null&&jTextPaneFuentes.getVectorModel().size()>0)
                calidad.getLinage().setSources(jTextPaneFuentes.getVectorModel());
            else
                calidad.getLinage().setSources(null);
            if  (jTextPanePasos.getVectorModel()!=null&&jTextPanePasos.getVectorModel().size()>0)
                calidad.getLinage().setSteps(jTextPanePasos.getVectorModel());
            else
                calidad.getLinage().setSteps(null);
            calidadnula=false;
        }
        else
        {
            if (calidad!=null) calidad.setLinage(null);
        }
        if ((calidad!=null) && (calidad.getElements()!=null) && (calidad.getElements().size()>0))
            calidadnula=false;

        if (calidadnula) return null;
        else
            logger.debug("La calidad del metadato tiene datos");
        return calidad;
    }

    public void setCalidad(DQ_DataQuality calidad) {
        this.calidad = calidad;
    }
    private void anadir()
    {
            auxElemento = new DQ_Element();
            loadElement(auxElemento);
            enabled(true);
    }
    private void cancelar()
    {
           auxElemento = new DQ_Element();
           loadElement(auxElemento);
           enabled(false);
   }
    private boolean validar()
    {
       if ((jComboBoxTipoInforme.getSelectedIndex()==0)||
           (jTextPaneValor.getText().length()<=0) ||
           (jTextPaneNombre.getText().length()<=0) ||
           (jTextFieldEspecificacion.getText().length()<=0) ||
           (jTextPaneExplicacion.getText().length()<=0))
       {
           JOptionPane optionPane= new JOptionPane(messages.getString("JPanelCalidad.mensaje.informenovalido"),JOptionPane.INFORMATION_MESSAGE);
           JDialog dialog =optionPane.createDialog(this,"");
           dialog.show();
           return false;
       }
        return true;
    }
    private void actualizarModelo()
    {
          modelElementos= new ElementosTableModel();
          modelElementos.setModelData(calidad!=null?calidad.getElements():new Vector());
          sorter = new TableSorted(modelElementos);
          sorter.setTableHeader(jTableElementos.getTableHeader());
          jTableElementos.setModel(sorter);
          jTableElementos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     }
    private void salvar()
    {
      if (!modoEdicion) return;
      try
      {
          if (!validar()) return;
          guardarCambiosElemento(auxElemento);
          elementoSelected=auxElemento;
          if (calidad==null) calidad=new DQ_DataQuality();
          calidad.addElement(elementoSelected);
          enabled(false);
          loadElement(elementoSelected);
          actualizarModelo();
      }catch(Exception ex)
      {
          java.io.StringWriter sw=new java.io.StringWriter();
          java.io.PrintWriter pw=new java.io.PrintWriter(sw);
          ex.printStackTrace(pw);
          logger.error("Excepcion al añadir el elemento: "+sw.toString());
          JOptionPane optionPane= new JOptionPane(ex,JOptionPane.ERROR_MESSAGE);
          JDialog dialog =optionPane.createDialog(this,"");
          dialog.show();
      }
    }
    private void seleccionarElemento(ListSelectionEvent e)
    {
        if (modoEdicion)
       {
              int n = JOptionPane.showOptionDialog(this,
                                               messages.getString("JPanelCalidad.mensaje.desechar"),
                                               "",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE,null,null,null);
              if (n==JOptionPane.NO_OPTION)
                  return;
              else
                  enabled(false);
       }
       ListSelectionModel lsm = (ListSelectionModel)e.getSource();
       if (lsm.isSelectionEmpty()) { }
       else
       {
            int selectedRow = lsm.getMinSelectionIndex();
            String idElemento=(String)sorter.getValueAt(selectedRow,ElementosTableModel.idIndex);
            if (calidad!=null)
                elementoSelected= calidad.getElementByTitle(idElemento);
            loadElement(elementoSelected);
            //jButtonEditar.setEnabled(true);
       }
    }

    public void eliminar()
    {
        if (elementoSelected==null)
        {
              JOptionPane optionPane= new JOptionPane(messages.getString("JPanelCalidad.mensaje.noseleccionado"),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"INFO");
              dialog.show();
              return;
        }
        int n = JOptionPane.showOptionDialog(this,
        messages.getString("JPanelCalidad.mensaje.eliminar"),
                                          "",
                                          JOptionPane.YES_NO_OPTION,
                                          JOptionPane.QUESTION_MESSAGE,null,null,null);
        if (n==JOptionPane.NO_OPTION)
             return;
        else
             eliminarElemento(elementoSelected);
    }
    public void eliminarElemento(DQ_Element elementoEliminado)
    {
        calidad.removeElement(elementoSelected);
        elementoSelected=null;
        enabled(false);
        loadElement(elementoSelected);
        actualizarModelo();
    }

    private void guardarCambiosElemento(DQ_Element elemento)
    {
           if (elemento==null) elemento= new DQ_Element();
           DomainNode tipoInforme=(DomainNode) jComboBoxTipoInforme.getSelectedItem();
           elemento.setSubclass_id(tipoInforme.getIdNode());
           elemento.setValue(jTextPaneValor.getText());
           elemento.setValueunit(jTextPaneNombre.getText());
           elemento.setExplanation(jTextPaneExplicacion.getText());
           elemento.setPass(jCheckBoxConformidad.isSelected());

       }

    private void loadElement(DQ_Element elemento)
   {
         if (elemento==null) elemento= new DQ_Element();
         jComboBoxTipoInforme.setSelected(elemento.getSubclass_id());
         jTextPaneValor.setText(elemento.getValue()==null?"":elemento.getValue());
         jTextPaneNombre.setText(elemento.getValueunit()==null?"":elemento.getValueunit());
         if (elemento.getCitation()!=null && elemento.getCitation().getTitle()!=null)
            jTextFieldEspecificacion.setText(elemento.getCitation().getTitle());
         else
            jTextFieldEspecificacion.setText("");
         jTextPaneExplicacion.setText(elemento.getExplanation()==null?"":elemento.getExplanation());
         jCheckBoxConformidad.setSelected(elemento.isPass());
         auxElemento=elemento;
   }
    public void enabled(boolean bValor)
    {
          modoEdicion=bValor;
          jButtonAnadir.setEnabled(!bValor);
          jButtonBorrar.setEnabled(!bValor);
          jButtonSalvar.setEnabled(bValor);
          jButtonCancelar.setEnabled(bValor);
          jComboBoxTipoInforme.setEnabled(bValor);
          jTextPaneValor.setEditable(bValor);
          jTextPaneNombre.setEditable(bValor);
          jTextFieldEspecificacion.setEditable(bValor);
          jTextPaneExplicacion.setEditable(bValor);
          jTextPaneExplicacion.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);
          jCheckBoxConformidad.setEnabled(bValor);
    }

    public int printPanel(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        printSuperTitulo((Graphics2D)g, pageFormat,pageIndex,messages.getString("CMetadatos.jPanelCalidad"));
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelCalidad.jPanelLinage"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,"");
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelCalidad.jLabelDes"));
        printDetalle((Graphics2D)g,pageFormat,pageIndex,jTextPaneDes.getText());
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelCalidad.jLabelFuentes"));
        if (jTextPaneFuentes.getVectorModel()!=null&& jTextPaneFuentes.getVectorModel().size()>0)
        {
            for (Enumeration e=jTextPaneFuentes.getVectorModel().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex,"");

        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelCalidad.jLabelPasos"));
        if (jTextPanePasos.getVectorModel()!=null&& jTextPanePasos.getVectorModel().size()>0)
        {
            for (Enumeration e=jTextPanePasos.getVectorModel().elements();e.hasMoreElements();)
            {
                printDetalle((Graphics2D)g,pageFormat,pageIndex,(String)e.nextElement());
            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex,"");
        printTitulo((Graphics2D)g,pageFormat,pageIndex,messages.getString("JPanelCalidad.jPaneInformes"));
        if(calidad!=null && calidad.getElements()!=null && calidad.getElements().size()>0)
        {
            for (Enumeration e= calidad.getElements().elements();e.hasMoreElements();)
            {
                 DQ_Element auxElement= (DQ_Element) e.nextElement();
                 if (auxElement.getCitation()!=null)
                 {
                      printDetalle((Graphics2D)g,pageFormat,pageIndex,auxElement.getCitation().getTitle());
                 }

            }
        }
        else
            printDetalle((Graphics2D)g,pageFormat,pageIndex," ");
        printSuperTitulo((Graphics2D)g,pageFormat,pageIndex," ");
        return Printable.PAGE_EXISTS;
    }



    private javax.swing.JTable jTableElementos;
    private javax.swing.JScrollPane jScrollPaneElementos;
    private javax.swing.JLabel jLabelPasos;
    private javax.swing.JPanel jPanelLinage;
    protected ListOptativo jTextPanePasos;
    private javax.swing.JButton jButtonMas;
    private javax.swing.JButton jButtonMenos;
    protected TextFieldOptativo jTextPaneDes;
    protected ListOptativo jTextPaneFuentes;
    private javax.swing.JLabel jLabelDes;
    private javax.swing.JLabel jLabelFuentes;
    protected TextPaneObligatorio jTextPaneExplicacion;
    protected TextFieldObligatorio jTextPaneNombre;
    protected TextFieldObligatorio jTextPaneValor;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelValor;
    private TextFieldObligatorio jTextFieldEspecificacion;
    protected javax.swing.JButton jButtonEspe;
    private javax.swing.JPanel jPanelDetalle;
    private javax.swing.JPanel jPanelResultado;
    private javax.swing.JLabel jLabelEspecificacion;
    private javax.swing.JLabel jLabelExplicacion;
    private javax.swing.JCheckBox jCheckBoxConformidad;
    private javax.swing.JPanel jPanelInformes;
    private javax.swing.JLabel jLabelTipoInforme;
    protected ComboBoxEstructurasObligatorio jComboBoxTipoInforme;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonAnadir;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JButton jButtonMasFuentes;
    private javax.swing.JButton jButtonMenosFuentes;
}