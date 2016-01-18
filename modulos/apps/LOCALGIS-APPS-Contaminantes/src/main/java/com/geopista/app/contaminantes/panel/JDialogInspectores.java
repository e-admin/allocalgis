/**
 * JDialogInspectores.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JDialogInspectores.java
 *
 * Created on 14 de octubre de 2004, 13:06
 */

package com.geopista.app.contaminantes.panel;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;
import java.io.StringWriter;
import java.io.PrintWriter;

import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.contaminantes.CMainContaminantes;
import com.geopista.app.contaminantes.InspectoresTableModel;
import com.geopista.protocol.contaminantes.Inspector;
import com.geopista.protocol.contaminantes.OperacionesContaminantes;
import com.geopista.protocol.CResultadoOperacion;

import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author  angeles
 */
public class JDialogInspectores extends javax.swing.JDialog {

    private Logger logger = Logger.getLogger(JDialogInspectores.class);
    private ResourceBundle messages;
    private Vector listaInspectores;
    private InspectoresTableModel	modelInspectores;
    private TableSorted sorter;
    private boolean modoEdicion=false;
    private boolean modoNuevo=false;
    private boolean seleccionado=false;
    private Inspector inspectorSelected=null;
    private Inspector auxInspector;

    /** Creates new form JDialogInspectores */
    public JDialogInspectores(java.awt.Frame parent, boolean modal,
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
            CResultadoOperacion result= new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url).getInspectores();
            if (result.getResultado())
            {
                listaInspectores = result.getVector();
                actualizarModelo();
            }
            else
            {
                JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"INFO");
                dialog.show();
                return;

            }
            if (listaInspectores==null) listaInspectores= new Vector();
        }catch (Exception e)
        {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Al obtener los datos de los inspectores: " + sw.toString());
            JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "ERROR");
            dialog.show();
        }
    }
    private void actualizarModelo()
    {
            modelInspectores= new InspectoresTableModel();
            modelInspectores.setModelData(listaInspectores);
            sorter = new TableSorted(modelInspectores);
            sorter.setTableHeader(jTableListado.getTableHeader());
            jTableListado.setModel(sorter);
            TableColumn column = jTableListado.getColumnModel().getColumn(0);
            column.setPreferredWidth(5);
            jTableListado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public void anadir()
    {
            modoNuevo=true;
            auxInspector = new Inspector();
            load(auxInspector);
            enabled(true);
    }
    public void eliminar()
    {
         if (inspectorSelected==null)
            {
                  JOptionPane optionPane= new JOptionPane(messages.getString("JDialogInspectores.mensaje.insnoseleccionado"),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(this,"INFO");
                  dialog.show();
                  return;
            }
            int n = JOptionPane.showOptionDialog(this,
            messages.getString("JDialogInspectores.mensaje.eliminarinspector")+" "+inspectorSelected.getNombre()
                               +" "+inspectorSelected.getApellido1()+ " "+inspectorSelected.getApellido2()+"?",
                                              "",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,null,null,null);
            if (n==JOptionPane.NO_OPTION)
                 return;
            else
                 eliminarInspector(inspectorSelected);
    }
      public void eliminarInspector(Inspector inspectorEliminado)
    {
           CResultadoOperacion result;
           try
           {
               result=(new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).deleteInspector(inspectorEliminado);
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
                  listaInspectores.remove(inspectorEliminado);
                  inspectorSelected=null;
                  JOptionPane optionPane= new JOptionPane(messages.getString("JDialogInspectores.mensaje.inspectoreliminado"),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(this,"");
                  dialog.show();
                  enabled(false);
                  load(inspectorSelected);
                  actualizarModelo();
            }
            else
            {
                    JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"ERROR");
                    dialog.show();
               }
           }
     public void load(String idInspector)
     {
        load(getInspector(idInspector));
    }

      public void load(Inspector inspe)
       {
             if (inspe==null) inspe= new Inspector();
             jTextFieldApellido1.setText(inspe.getApellido1()==null?"":inspe.getApellido1());
             jTextFieldApellido2.setText(inspe.getApellido2()==null?"":inspe.getApellido2());
             jTextFieldNombre.setText(inspe.getNombre()==null?"":inspe.getNombre());
             jTextFieldDireccion.setText(inspe.getDireccion()==null?"":inspe.getDireccion());
             jTextFieldEmpresa.setText(inspe.getEmpresa()==null?"":inspe.getEmpresa());
             jTextFieldPuesto.setText(inspe.getPuesto()==null?"":inspe.getPuesto());
             jTextFieldTelefono.setText(inspe.getTelefono()==null?"":inspe.getTelefono());
             jTextPaneOtros.setText(inspe.getOtrosdatos()==null?"":inspe.getOtrosdatos());
             auxInspector=inspe;

       }
       public void enabled(boolean bValor)
       {
              modoEdicion=bValor;
              boolean permitido=true;
              if (CMainContaminantes.acl!=null)
                  permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	          jButtonEdit.setEnabled(!bValor&&permitido);
              jButtonAdd.setEnabled(!bValor&&permitido);
              jButtonSelect.setEnabled(!bValor);
              jButtonDelete.setEnabled(!bValor&&permitido);
              jButtonSalvar.setEnabled(bValor&&permitido);
              jTextFieldApellido1.setEditable(bValor);
              jTextFieldApellido2.setEditable(bValor);
              jTextFieldNombre.setEditable(bValor);
              jTextFieldDireccion.setEditable(bValor);
              jTextFieldEmpresa.setEditable(bValor);
              jTextFieldPuesto.setEditable(bValor);
              jTextFieldTelefono.setEditable(bValor);
              jTextPaneOtros.setEnabled(bValor);
              jTextPaneOtros.setBackground(!bValor?Color.LIGHT_GRAY:Color.WHITE);

        }
        private void cancelar()
        {
            if (modoEdicion)
            {
                         int n = JOptionPane.showOptionDialog(this,
                                                          messages.getString("JDialogInspectores.mensaje.desechar"),
                                                          "INFO",
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
       private void seleccionar()
       {
            if (inspectorSelected==null)
            {
                 JOptionPane optionPane= new JOptionPane(messages.getString("JDialogInspectores.mensaje.insnoseleccionado"),JOptionPane.INFORMATION_MESSAGE);
                 JDialog dialog =optionPane.createDialog(this,"INFO");
                 dialog.show();
                 return;
            }

            if (!validar()) return;
            if (inspectorSelected==null)
            {
                inspectorSelected = new Inspector();
                guardarCambios(inspectorSelected);
            }
            seleccionado=true;
            dispose();
         }
         public Inspector getInspector(String idInspector)
         {
                if (listaInspectores==null || idInspector==null) return null;
                for (Enumeration e=listaInspectores.elements();e.hasMoreElements();)
                {
                    Inspector aux=(Inspector) e.nextElement();
                    if (idInspector.equalsIgnoreCase(aux.getId()))
                        return aux;
                }
                return null;
         }
       private void seleccionarInspector(ListSelectionEvent e)
       {
               if (modoEdicion)
              {
                     int n = JOptionPane.showOptionDialog(this,
                                                      messages.getString("JDialogInspectores.mensaje.desechar"),
                                                      "INFO",
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
                   String idInspector=(String)sorter.getValueAt(selectedRow,InspectoresTableModel.idIndex);
                   inspectorSelected= getInspector(idInspector);
                   if (inspectorSelected==null)
                   {
                       JOptionPane optionPane= new JOptionPane(
                               messages.getString("JDialogInspectores.mensaje.insnoencontrado"),JOptionPane.INFORMATION_MESSAGE);
                       JDialog dialog =optionPane.createDialog(this,"INFO");
                       dialog.show();
                   }
                   load(inspectorSelected);
                   boolean permitido=true;
                   if (CMainContaminantes.acl!=null)
                        permitido=CMainContaminantes.acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_CONTAMINANTES));
	               jButtonEdit.setEnabled(permitido);
              }
      }

      private void editar()
      {
            if (inspectorSelected==null)
            {
                  JOptionPane optionPane= new JOptionPane(messages.getString("JDialogInspectores.mensaje.insnoseleccionado"),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(this,"INFO");
                  dialog.show();
                  return;
            }
            enabled(true);
            jButtonEdit.setEnabled(false);

        }

    private void guardarCambios(Inspector inspector)
    {
        inspector.setApellido1(jTextFieldApellido1.getText());
        inspector.setApellido2(jTextFieldApellido2.getText());
        inspector.setDireccion(jTextFieldDireccion.getText());
        inspector.setEmpresa(jTextFieldEmpresa.getText());
        inspector.setNombre(jTextFieldNombre.getText());
        inspector.setOtrosdatos(jTextPaneOtros.getText());
        inspector.setPuesto(jTextFieldPuesto.getText());
        inspector.setTelefono(jTextFieldTelefono.getText());
    }
    private boolean validar()
    {
        if ((jTextFieldNombre.getText().length()<=0) &&
            (jTextFieldApellido1.getText().length()<=0) &&
            (jTextFieldApellido2.getText().length()<=0) &&
            (jTextFieldDireccion.getText().length()<=0) &&
            (jTextFieldEmpresa.getText().length()<=0) &&
            (jTextPaneOtros.getText().length()<=0) &&
            (jTextFieldPuesto.getText().length()<=0) &&
            (jTextFieldTelefono.getText().length()<=0)
          )
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("JDialogInspectores.mensaje.novalido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
        }
        return true;
    }
    private void salvar()
    {
        if (!modoEdicion) return;
        try
        {
            if (!validar()){return;}
            guardarCambios(auxInspector);
            CResultadoOperacion result=null;
            String sMensaje="";
            try
            {
                result=(new OperacionesContaminantes(com.geopista.app.contaminantes.Constantes.url)).saveInspector(auxInspector);
                if (modoNuevo)
                   sMensaje=messages.getString("JDialogInspectores.mensaje.inspectorinsertado");
                else
                   sMensaje=messages.getString("JDialogInspectores.mensaje.inspectoractualizado");

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
                  auxInspector= (Inspector)result.getVector().elementAt(0);}
              catch(Exception e)
              {
                  java.io.StringWriter sw=new java.io.StringWriter();
                  java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                  e.printStackTrace(pw);
                  logger.error("Error al coger el objeto inspector: "+sw.toString());
              }

              logger.debug("Identificador del inspector insertado:"+auxInspector.getId());
              inspectorSelected=auxInspector;
              if(modoNuevo)
                listaInspectores.add(inspectorSelected);
              else
              {
                  Inspector ins=getInspector(inspectorSelected.getId());
                  ins.copy(inspectorSelected);
              }
              JOptionPane optionPane= new JOptionPane(sMensaje,JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"INFO");
              dialog.show();
              enabled(false);
              modoNuevo=false;
              load(inspectorSelected);
              actualizarModelo();
           }
           else
           {
                JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"INFO");
                dialog.show();
           }
        }catch(Exception ex)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Excepcion al añadir el inspector: "+sw.toString());
            JOptionPane optionPane= new JOptionPane(ex,JOptionPane.ERROR_MESSAGE);
            JDialog dialog =optionPane.createDialog(this,"ERROR");
            dialog.show();
        }

    }
    public Inspector getInspectorSelected() {
            return inspectorSelected;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

    private void initComponents() {//GEN-BEGIN:initComponents
        jSplitPanePrincipal = new javax.swing.JSplitPane();
        jPanelListado = new javax.swing.JPanel();
        jPanelBotoneraListado = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jScrollPaneListado = new javax.swing.JScrollPane();
        jTableListado = new javax.swing.JTable();
        jPanelDatos = new javax.swing.JPanel();
        jPanelBotonera = new javax.swing.JPanel();
        jButtonEdit = new javax.swing.JButton();
        jButtonSalvar = new javax.swing.JButton();
        jButtonSelect = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanelDetalle = new javax.swing.JPanel();
        jLabelApellido1 = new javax.swing.JLabel();
        jTextFieldApellido1 = new com.geopista.app.utilidades.TextField(60);
        jLabelApellido2 = new javax.swing.JLabel();
        jTextFieldApellido2 = new com.geopista.app.utilidades.TextField(60);
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombre = new com.geopista.app.utilidades.TextField(60);
        jLabelEmpresa = new javax.swing.JLabel();
        jTextFieldEmpresa = new com.geopista.app.utilidades.TextField(50);
        jLabelPuesto = new javax.swing.JLabel();
        jTextFieldPuesto = new  com.geopista.app.utilidades.TextField(50);
        jLabelTelefono = new javax.swing.JLabel();
        jTextFieldTelefono = new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER,new Integer(999999999));
        jLabelDireccion = new javax.swing.JLabel();
        jTextFieldDireccion = new com.geopista.app.utilidades.TextField(250);
        jLabelOtros = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneOtros = new TextPane(500);


        setResizable(true);
        jPanelListado.setLayout(new java.awt.BorderLayout());
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        anadir();
                    }
                });
        jPanelBotoneraListado.add(jButtonAdd);
        jPanelBotoneraListado.add(jButtonEdit);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        eliminar();
                                    }
                                });
        jPanelBotoneraListado.add(jButtonDelete);

        jPanelListado.add(jPanelBotoneraListado, java.awt.BorderLayout.SOUTH);
        jScrollPaneListado.setViewportView(jTableListado);
        ListSelectionModel rowSM = jTableListado.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                seleccionarInspector(e);
            }
        });
        jPanelListado.add(jScrollPaneListado, java.awt.BorderLayout.CENTER);
        jSplitPanePrincipal.setLeftComponent(jPanelListado);
        jSplitPanePrincipal.setDividerSize(10);

        jPanelDatos.setLayout(new java.awt.BorderLayout());
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editar();
            }});
        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvar();
            }});
        jPanelBotonera.add(jButtonSalvar);
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionar();
            }
        });
        jPanelBotonera.add(jButtonSelect);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar();
            }
        });
        jPanelBotonera.add(jButtonCancel);
        jPanelDatos.add(jPanelBotonera, java.awt.BorderLayout.SOUTH);
        jPanelDetalle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelDetalle.add(jLabelApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanelDetalle.add(jTextFieldApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 160, -1));
        jPanelDetalle.add(jLabelApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));
        jPanelDetalle.add(jTextFieldApellido2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 160, -1));
        jPanelDetalle.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanelDetalle.add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 400, -1));
        jPanelDetalle.add(jLabelEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        jPanelDetalle.add(jTextFieldEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 150, -1));
        jPanelDetalle.add(jLabelPuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, -1, -1));
        jPanelDetalle.add(jTextFieldPuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 160, -1));
        jPanelDetalle.add(jLabelTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));
        jPanelDetalle.add(jTextFieldTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 90, -1));
        jPanelDetalle.add(jLabelDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));
        jPanelDetalle.add(jTextFieldDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 400, -1));
        jPanelDetalle.add(jLabelOtros, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 220, -1));
        jScrollPane1.setViewportView(jTextPaneOtros);
        jPanelDetalle.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 460, 170));
        jPanelDatos.add(jPanelDetalle, java.awt.BorderLayout.CENTER);
        jSplitPanePrincipal.setRightComponent(jPanelDatos);
        getContentPane().add(jSplitPanePrincipal, java.awt.BorderLayout.CENTER);
        enabled(false);
        pack();
    }//GEN-END:initComponents

    public void changeScreenLang(ResourceBundle messages) {
        try
        {
            this.messages=messages;
            setTitle(messages.getString("JDialogInspectores.title"));//Inspectores");
            jButtonDelete.setText(messages.getString("JDialogInspectores.jButtonDelete"));//"Borrar");
            jButtonAdd.setText(messages.getString("JDialogInspectores.jButtonAdd"));//"A\u00f1adir");
            jButtonEdit.setText(messages.getString("JDialogInspectores.jButtonEdit"));//"Editar");
            jButtonSalvar.setText(messages.getString("JDialogInspectores.jButtonSalvar"));//Salvar");
            jButtonSelect.setText(messages.getString("JDialogInspectores.jButtonSelect"));//Seleccionar
            jButtonCancel.setText(messages.getString("JDialogInspectores.jButtonCancel"));//"Cancelar");
            jLabelApellido1.setText(messages.getString("JDialogInspectores.jLabelApellido1"));//"Apellido 1:");
            jLabelApellido2.setText(messages.getString("JDialogInspectores.jLabelApellido2"));//"Apellido 2:");
            jLabelNombre.setText(messages.getString("JDialogInspectores.jLabelNombre"));//Nombre:");
            jLabelEmpresa.setText(messages.getString("JDialogInspectores.jLabelEmpresa"));//"Empresa:");
            jLabelOtros.setText(messages.getString("JDialogInspectores.jLabelOtros"));//Otros datos:
            jLabelDireccion.setText(messages.getString("JDialogInspectores.jLabelDireccion"));//"Direcci\u00f3n:");
            jLabelTelefono.setText(messages.getString("JDialogInspectores.jLabelTelefono"));//"Telefono:");
            jLabelPuesto.setText(messages.getString("JDialogInspectores.jLabelPuesto"));//"Puesto:");

            jButtonDelete.setToolTipText(messages.getString("JDialogInspectores.jButtonDelete"));
            jButtonAdd.setToolTipText(messages.getString("JDialogInspectores.jButtonAdd"));
            jButtonEdit.setToolTipText(messages.getString("JDialogInspectores.jButtonEdit"));
            jButtonSalvar.setToolTipText(messages.getString("JDialogInspectores.jButtonSalvar"));
            jButtonSelect.setToolTipText(messages.getString("JDialogInspectores.jButtonSelect"));
            jButtonCancel.setToolTipText(messages.getString("JDialogInspectores.jButtonCancel"));
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas:",e);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonSelect;
    private javax.swing.JLabel jLabelApellido1;
    private javax.swing.JLabel jLabelApellido2;
    private javax.swing.JLabel jLabelPuesto;
    private javax.swing.JLabel jLabelDireccion;
    private javax.swing.JLabel jLabelEmpresa;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelOtros;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JPanel jPanelBotoneraListado;
    private javax.swing.JPanel jPanelDatos;
    private javax.swing.JPanel jPanelDetalle;
    private javax.swing.JPanel jPanelListado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneListado;
    private javax.swing.JSplitPane jSplitPanePrincipal;
    private javax.swing.JTable jTableListado;
    private javax.swing.JTextField jTextFieldApellido1;
    private javax.swing.JTextField jTextFieldApellido2;
    private javax.swing.JTextField jTextFieldDireccion;
    private javax.swing.JTextField jTextFieldEmpresa;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldPuesto;
    private JNumberTextField jTextFieldTelefono;
    private TextPane jTextPaneOtros;
    // End of variables declaration//GEN-END:variables
    
}
