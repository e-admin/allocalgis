/**
 * JPanelRoles.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.administrador.usuarios;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.App;
import com.geopista.protocol.administrador.ListaAcl;
import com.geopista.protocol.administrador.ListaApp;
import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.ListaRoles;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.Permiso;
import com.geopista.protocol.administrador.Rol;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 31-may-2004
 * Time: 18:52:11
 */
public class JPanelRoles extends javax.swing.JPanel {
    private ListaRoles listaRoles =null;
    private Logger logger = Logger.getLogger(JPanelRoles.class);
    private Rol rolSelected=null;
    private Rol auxRol=null;
    private App appSelected=null;
    private Acl aclSelected=null;
    private ListaApp listaApp=null;
    private ListaAcl listaAcl=null;
    private ResourceBundle mensajesRol;
    private boolean modoEdicion=false;
    private boolean modoNuevo=false;
    private static final String IDPERM_CODE="idPerm";
    private TableSorted sorter;
    private RolesTableModel	modelRoles;
     private JFrame framePadre;
    public JPanelRoles(ResourceBundle messages, JFrame framePadre) {
        this.framePadre=framePadre;
        initComponents(messages);
    }

    private void initComponents(ResourceBundle messages)
    {
        mensajesRol=messages;
        JScrollPaneListaRoles = new javax.swing.JScrollPane();
        jTableRoles = new javax.swing.JTable();
        jButtonRolAnadir = new javax.swing.JButton();
        jButtonRolEliminar = new javax.swing.JButton();
        jButtonRolEditar = new javax.swing.JButton();
        jButtonVerPermisos= new javax.swing.JButton();
        jButtonMarcar = new javax.swing.JButton();
        jButtonDesMarcar =new javax.swing.JButton();
        jPanelEditarRol = new javax.swing.JPanel();
        jLabelBusquedaRol = new javax.swing.JLabel();
        jLabelNombreRol = new javax.swing.JLabel();
        jPanelListaRoles = new javax.swing.JPanel();
        jTextFieldBusquedaRol = new com.geopista.app.utilidades.TextField(64);
        jTextFieldNombre = new com.geopista.app.utilidades.TextField(32);
        jLabelRolDes = new javax.swing.JLabel();
        jTextFieldRolDes = new com.geopista.app.utilidades.TextField(254);
        jScrollPaneRolPer = new javax.swing.JScrollPane();
        jPanelPermisosRol = new javax.swing.JPanel();
        jPanelDescripcion = new javax.swing.JPanel();
        jPanelPermisosTotal = new javax.swing.JPanel();
        jButtonRolAceptar = new javax.swing.JButton();
        jButtonRolCancelar = new javax.swing.JButton();
        jLabelAppRol = new javax.swing.JLabel();
        jLabelACLRol = new javax.swing.JLabel();
        jComboBoxACLs = new javax.swing.JComboBox();
        jComboBoxApps = new javax.swing.JComboBox();
        jButtonRolTodos = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelListaRoles.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelListaRoles.add(jLabelBusquedaRol,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 28, 200,
						-1));
        
        jTextFieldBusquedaRol.addKeyListener(new KeyListener(){

			
			public void keyTyped(KeyEvent e) {
			}

			
			public void keyPressed(KeyEvent e) {				
			}

			
			public void keyReleased(KeyEvent e) {			
				actualizarModelo(jTextFieldBusquedaRol.getText());
			}
		});	
        
        jPanelListaRoles.add(jTextFieldBusquedaRol,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 25, 200,
						-1));
        
        jButtonRolTodos.addMouseListener(new MouseListener(){
			
			public void mouseClicked(MouseEvent e) {
				jTextFieldBusquedaRol.setText("");
				actualizarModelo("");
			}

			
			public void mousePressed(MouseEvent e) {
			}

			
			public void mouseReleased(MouseEvent e) {
			}

			
			public void mouseEntered(MouseEvent e) {
			}

			
			public void mouseExited(MouseEvent e) {
			}
			
		});        
        
        jPanelListaRoles.add(jButtonRolTodos,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 24, 80,
						-1));

        JScrollPaneListaRoles.setViewportView(jTableRoles);
        jPanelListaRoles.add(JScrollPaneListaRoles, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 350, 450));
        jButtonRolAnadir.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
           {  anadirRolActionListener(); }
        });
        jPanelListaRoles.add(jButtonRolAnadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 75, -1));
        jButtonRolEliminar.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
           {  eliminarRolActionListener(); }
        });
        jPanelListaRoles.add(jButtonRolEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 520, 75, -1));
        jButtonRolEditar.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
           {  editarRol(); }
        });
        jPanelListaRoles.add(jButtonRolEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 520, 75, -1));
        jButtonVerPermisos.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
          {  verPermisos(); }
        });
        jPanelListaRoles.add(jButtonVerPermisos, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 520, -1, -1));

        add(jPanelListaRoles, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 370, 555));
        jPanelEditarRol.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelEditarRol.add(jLabelNombreRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));
        jTextFieldNombre.setEditable(false);
        jPanelEditarRol.add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 280, -1));
        jPanelEditarRol.add(jLabelRolDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));
        jTextFieldRolDes.setEditable(false);
        jPanelEditarRol.add(jTextFieldRolDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 280, -1));
        jScrollPaneRolPer.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneRolPer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanelPermisosRol.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelPermisosRol.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDescripcion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPermisosTotal.setLayout(new BorderLayout());
        jPanelPermisosTotal.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPermisosTotal.add(jPanelPermisosRol, BorderLayout.EAST);
        jPanelPermisosTotal.add(jPanelDescripcion, BorderLayout.WEST);


        jScrollPaneRolPer.setViewportView(jPanelPermisosTotal);
        jPanelEditarRol.add(jLabelAppRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 93, 190, -1));
        jPanelEditarRol.add(jLabelACLRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 123, 190, -1));
        //jComboBoxACLs.setEnabled(false);
        jPanelEditarRol.add(jComboBoxApps, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 190, -1));
        jPanelEditarRol.add(jComboBoxACLs, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 190, -1));

        jPanelEditarRol.add(jScrollPaneRolPer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 185, 450, 325));
        jPanelEditarRol.add(jButtonRolAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 520, -1, -1));
        jButtonRolAceptar.setEnabled(false);
        jButtonRolAceptar.addActionListener(new java.awt.event.ActionListener()
                {  public void actionPerformed(java.awt.event.ActionEvent evt)
                   {  aceptar(); }
                });

        jButtonRolCancelar.setEnabled(false);
        jButtonRolCancelar.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
           {  cancelar(); }
        });


        jPanelEditarRol.add(jButtonRolCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 520, -1, -1));
        add(jPanelEditarRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 490, 555));

        //Para seleccionar una fila
        ListSelectionModel rowSM = jTableRoles.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                seleccionarRol(e);
            }
        });

        jButtonMarcar.setEnabled(false);
        jButtonMarcar.addActionListener(new java.awt.event.ActionListener()
        {  public void actionPerformed(java.awt.event.ActionEvent evt)
           {  marcar(true); }
        });
        jPanelEditarRol.add(jButtonMarcar,  new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 125, -1));

        jButtonDesMarcar.setEnabled(false);
        jButtonDesMarcar.addActionListener(new java.awt.event.ActionListener()
        {   public void actionPerformed(java.awt.event.ActionEvent evt)
           {  marcar(false); }
        });

        jPanelEditarRol.add(jButtonDesMarcar,  new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 135, -1));



  }
  public void editable(boolean b)
  {
      jButtonRolAnadir.setEnabled(b);
      jButtonRolEliminar.setEnabled(b);
      jButtonRolEditar.setEnabled(b);
      //jButtonVerPermisos.setEnabled(b);

      /** Incidencia [308]: si un usuario tiene el permiso Geopista.Administracion.View,
          independientemente de que tenga permiso de edicion (Geopista.Administracion.Edit), el boton
          Ver Permisos debe estar habilitado. */

      /** Si mostramos el panel de usuarios es porque tenemos el permiso de Geopista.Administracion.View
          (opcion de menu Gestion de Usuarios habilitada). Aun asi, hacemos la comprobacion. */
      try{
          com.geopista.security.GeopistaAcl aclAdministracion= com.geopista.security.SecurityManager.getPerfil("Administracion");
          jButtonVerPermisos.setEnabled(aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_ADMINITRACION)));
      }catch (Exception e){
          jButtonVerPermisos.setEnabled(true);
      }
      
  }
  public void changeScreenLang(ResourceBundle messages) {
      mensajesRol=messages;
      jPanelListaRoles.setBorder(new javax.swing.border.TitledBorder(messages.getString("CUsuariosFrame.jPanelListaRoles")));
      jScrollPaneRolPer.setBorder(new javax.swing.border.TitledBorder(messages.getString("CUsuariosFrame.jScrollPaneRolPer")));
      jPanelEditarRol.setBorder(new javax.swing.border.TitledBorder(messages.getString("CUsuariosFrame.jPanelRol")));
      jLabelRolDes.setText(messages.getString("CUsuariosFrame.jLabelRolDes"));
      jButtonRolAceptar.setText(messages.getString("CUsuariosFrame.jButtonRolAceptar"));
      jButtonRolCancelar.setText(messages.getString("CUsuariosFrame.jButtonRolCancelar"));
      jButtonRolAnadir.setText(messages.getString("CUsuariosFrame.jButtonRolAnadir"));
      jButtonRolEliminar.setText(messages.getString("CUsuariosFrame.jButtonRolEliminar"));
      jButtonRolEditar.setText(messages.getString("CUsuariosFrame.jButtonRolEditar"));
      jButtonVerPermisos.setText(messages.getString("CUsuariosFrame.jButtonVerPermisos"));
      jLabelNombreRol.setText(messages.getString("CUsuariosFrame.jLabelNombre"));
      jLabelBusquedaRol.setText(messages
				.getString("CUsuariosFrame.jLabelBusquedaRol"));
      jLabelAppRol.setText(messages
				.getString("CUsuariosFrame.jLabelAppRol"));
      jLabelACLRol.setText(messages.getString("CUsuariosFrame.jLabelACLUsuario"));
      jButtonMarcar.setText(messages.getString("CUsuariosFrame.jButtonMarcar"));//"Marcar todos");
      jButtonDesMarcar.setText(messages.getString("CUsuariosFrame.jButtonDesMarcar"));//"Desmarcar todos");
      jButtonRolTodos.setText(messages
				.getString("CUsuariosFrame.jButtonRolTodos"));
      jButtonRolAceptar.setToolTipText(messages.getString("CUsuariosFrame.jButtonRolAceptar"));
      jButtonRolAnadir.setToolTipText(messages.getString("CUsuariosFrame.jButtonRolAnadir"));
      jButtonRolCancelar.setToolTipText(messages.getString("CUsuariosFrame.jButtonRolCancelar"));
      jButtonRolEditar.setToolTipText(messages.getString("CUsuariosFrame.jButtonRolEditar"));
      jButtonVerPermisos.setToolTipText(messages.getString("CUsuariosFrame.jButtonVerPermisos"));
      jButtonRolEliminar.setToolTipText(messages.getString("CUsuariosFrame.jButtonRolEliminar"));
      jButtonMarcar.setToolTipText(messages.getString("CUsuariosFrame.jButtonMarcar"));
      jButtonDesMarcar.setToolTipText(messages.getString("CUsuariosFrame.jButtonDesMarcar"));

      TableColumn tableColumn= jTableRoles.getColumnModel().getColumn(0);
      tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col0"));
      tableColumn= jTableRoles.getColumnModel().getColumn(1);
      tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col1"));
      tableColumn= jTableRoles.getColumnModel().getColumn(2);
      tableColumn.setHeaderValue(messages.getString("CUsuariosFrame.col2"));

  }
  private void seleccionarRol(ListSelectionEvent e)
  {
      if (modoEdicion)
      {
             int n = JOptionPane.showOptionDialog(this,
                                              mensajesRol.getString("JPanelRoles.mensaje.desechar"),
                                              "",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,null,null,null);
             if (n==JOptionPane.NO_OPTION)
                 return;
             else
                 enabledComponents(false);
      }
      //if (e.getValueIsAdjusting()) return;
      ListSelectionModel lsm = (ListSelectionModel)e.getSource();
      if (lsm.isSelectionEmpty()) { }
      else
      {
           int selectedRow = lsm.getMinSelectionIndex();
           String idRol=(String)sorter.getValueAt(selectedRow,RolesTableModel.idIndex);
           rolSelected= listaRoles.get(idRol);
           if (rolSelected==null)
           {
               JOptionPane optionPane= new JOptionPane("Rol no encontrado",JOptionPane.INFORMATION_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,"Error al seleccionar");
               dialog.show();
           }
            mostrarRol(rolSelected);
      }
  }
  public void mostrarRol(Rol miRol)
  {
      if (miRol!=null)
      {
        auxRol=(Rol)miRol.clone();
        jTextFieldNombre.setText(auxRol.getNombre());
        jTextFieldRolDes.setText(auxRol.getDescripcion());
        inicializarPermisosRol(auxRol);
      }
      else//Limpio los datos
      {
          jTextFieldNombre.setText("");
          jTextFieldRolDes.setText("");
          jComboBoxApps.setSelectedIndex(0);
          jComboBoxACLs.setSelectedIndex(0);
          jPanelPermisosRol.removeAll();
          jPanelDescripcion.removeAll();
          jPanelPermisosTotal.validate();
          jPanelPermisosTotal.invalidate();
      }
  }
    
     
     private void editarRol()
    {
        if (rolSelected==null)
        {
              JOptionPane optionPane= new JOptionPane(mensajesRol.getString("JPanelRoles.mensaje.norol"),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"INFO");
              dialog.show();
              return;
        }
       enabledComponents(true);
    }

    public ListaRoles getListaRoles() {
        return listaRoles;
    }

    private void eliminarRolActionListener()
    {
        if (rolSelected==null)
        {
              JOptionPane optionPane= new JOptionPane(mensajesRol.getString("JPanelRoles.mensaje.norol"),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"INFO");
              dialog.show();
              return;
        }
        int n = JOptionPane.showOptionDialog(this,
                                            mensajesRol.getString("JPanelRoles.mensaje.eliminarrol")+" "+rolSelected.getNombre()+"?",
                                              "",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,null,null,null);
        if (n==JOptionPane.NO_OPTION)
             return;
        else
             eliminarRol(rolSelected);
    }
    public void anadirRolActionListener()
    {
        modoNuevo=true;
        auxRol=new Rol();
        mostrarRol(null);
        enabledComponents(true);
    }
    public void eliminarRol(Rol rolEliminado)
    {
        CResultadoOperacion result;
        try
        {
            result=(new OperacionesAdministrador(Constantes.url)).eliminarRol(rolEliminado);
        }catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception al eliminar ROL en base de datos: " + sw.toString());
            result= new CResultadoOperacion(false,e.getMessage());
        }
        if (result.getResultado())
        {
               listaRoles.remove(rolEliminado);
               rolSelected=null;
               JOptionPane optionPane= new JOptionPane(mensajesRol.getString("JPanelRoles.mensaje.roleliminado"),JOptionPane.INFORMATION_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,"");
               dialog.show();
               enabledComponents(false);
               mostrarRol(rolSelected);
               actualizarModelo();
            }
            else
            {
                 JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                 JDialog dialog =optionPane.createDialog(this,"");
                 dialog.show();
            }
        }


    private void enabledComponents(boolean bEnabled)
    {
       modoEdicion=bEnabled;

       jButtonRolEditar.setEnabled(!bEnabled);
       jButtonRolAnadir.setEnabled(!bEnabled);
       jButtonRolEliminar.setEnabled(!bEnabled);
       jButtonVerPermisos.setEnabled(!bEnabled);

       jButtonRolAceptar.setEnabled(bEnabled);
       jButtonRolCancelar.setEnabled(bEnabled);

       jButtonMarcar.setEnabled(bEnabled);
       jButtonDesMarcar.setEnabled(bEnabled);

       jTextFieldNombre.setEditable(bEnabled);
       jTextFieldRolDes.setEditable(bEnabled);
       int iTotal=jPanelPermisosRol.getComponentCount();
       for (int i=0;i<iTotal;i++)
       {
           Component auxComponent=jPanelPermisosRol.getComponent(i);
           if (auxComponent instanceof JCheckBox)
           {
                 JCheckBox jCheckBoxPermitir =(JCheckBox)auxComponent;
                 jCheckBoxPermitir.setEnabled(bEnabled);
             }
         }
        this.invalidate();
        this.validate();
    }
    private void marcar(boolean bActivar)
    {
        int iTotal=jPanelPermisosRol.getComponentCount();
        for (int i=0;i<iTotal;i++)
        {
            Component auxComponent=jPanelPermisosRol.getComponent(i);
            if (auxComponent instanceof JCheckBox)
            {
                  JCheckBox jCheckBoxPermitir =(JCheckBox)auxComponent;
                  jCheckBoxPermitir.setSelected(bActivar);
              }
          }
         this.invalidate();
         this.validate();
     }

    public void inicializarPermisosRol(Rol rolMuestra)
    {
        if ((rolSelected==null) || (aclSelected==null))
        {
            logger.info("No hay rol o acl seleccionado");
            return;
        }
        int iTotal=jPanelPermisosRol.getComponentCount();
        ListaPermisos permisosRol=rolMuestra.getPermisos();
        if (permisosRol==null)
        {
            logger.info("El rol: "+rolSelected.getNombre()+" no tiene permisos");
            return;
        }
        for (int i=0;i<iTotal;i++)
        {
            Component auxComponent=jPanelPermisosRol.getComponent(i);
            if (auxComponent instanceof JCheckBox)
            {
                 JCheckBox jCheckBoxPermitir =(JCheckBox)auxComponent;
                 /*
                 Permiso auxPermiso=permisosRol.get((String)jCheckBoxPermitir.getClientProperty(IDPERM_CODE));
                 if ((auxPermiso!=null)&&(auxPermiso.getIdAcl().equals(aclSelected.getId())))
                    jCheckBoxPermitir.setSelected(true);
                 else
                    jCheckBoxPermitir.setSelected(false);
                 */

                /** Incidencia [308] - acl distintos con los mismos idperm */
                Permiso auxPermiso=permisosRol.get((String)jCheckBoxPermitir.getClientProperty(IDPERM_CODE), aclSelected.getId());
                 if (auxPermiso!=null)
                     jCheckBoxPermitir.setSelected(true);
                 else
                     jCheckBoxPermitir.setSelected(false);
          }
        }
    }
    public void pintarPermisosAcls(ListaPermisos listaPermisos)
       {
           try
           {
                  jPanelPermisosRol.removeAll();
                  jPanelDescripcion.removeAll();
                  if (listaPermisos==null)
                  {
                      jPanelPermisosRol.invalidate();
                      jPanelPermisosRol.validate();
                      return;
                  }
                  int i=0;
                  for (int j=0;j<listaPermisos.gethPermisosSorted().size();j++){
      				Permiso permiso = (Permiso) listaPermisos.gethPermisosSorted().get(j);
                  //for (Enumeration e=listaPermisos.gethPermisos().elements();e.hasMoreElements();)
                  //{
                      //Permiso permiso= (Permiso)e.nextElement();
                      JCheckBox jCheckBoxPermitir = new JCheckBox("Permitir");
                      jCheckBoxPermitir.setBackground(new java.awt.Color(255, 255, 255));
                      jCheckBoxPermitir.setText("Permitir");
                      jCheckBoxPermitir.setEnabled(modoEdicion);
                      jCheckBoxPermitir.putClientProperty(IDPERM_CODE,permiso.getIdPerm());
                      jPanelPermisosRol.add(jCheckBoxPermitir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, i, -1, -1));

                      JLabel jLabelPermiso=new JLabel();
                      jLabelPermiso.setText(permiso.getDef());
                      jPanelDescripcion.add(jLabelPermiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3+i, -1, -1));
                      i+=20;

                  }
                  jPanelPermisosTotal.repaint();
                  jPanelPermisosTotal.invalidate();
                  jPanelPermisosTotal.validate();
                  jScrollPaneRolPer.repaint();
                  jScrollPaneRolPer.invalidate();
                  jScrollPaneRolPer.validate();
       } catch (Exception e)
           {
               logger.error("Error al pedir los permisos:" + e.toString());
           }
       }
    
    public void pintarAclsApps(ListaAcl listaAcls)
    {
        try
        {
               jPanelPermisosRol.removeAll();
               jPanelDescripcion.removeAll();
               if (listaAcls==null)
               {
                   jPanelPermisosRol.invalidate();
                   jPanelPermisosRol.validate();
                   return;
               }
               int i=0;
               for (Enumeration e=listaAcls.gethAcls().elements();e.hasMoreElements();)
               {
                   Permiso permiso= (Permiso)e.nextElement();
                   JCheckBox jCheckBoxPermitir = new JCheckBox("Permitir");
                   jCheckBoxPermitir.setBackground(new java.awt.Color(255, 255, 255));
                   jCheckBoxPermitir.setText("Permitir");
                   jCheckBoxPermitir.setEnabled(modoEdicion);
                   jCheckBoxPermitir.putClientProperty(IDPERM_CODE,permiso.getIdPerm());
                   jPanelPermisosRol.add(jCheckBoxPermitir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, i, -1, -1));

                   JLabel jLabelPermiso=new JLabel();
                   jLabelPermiso.setText(permiso.getDef());
                   jPanelDescripcion.add(jLabelPermiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3+i, -1, -1));
                   i+=20;

               }
               jPanelPermisosTotal.repaint();
               jPanelPermisosTotal.invalidate();
               jPanelPermisosTotal.validate();
               jScrollPaneRolPer.repaint();
               jScrollPaneRolPer.invalidate();
               jScrollPaneRolPer.validate();
    } catch (Exception e)
        {
            logger.error("Error al pedir los permisos:" + e.toString());
        }
    }

    private void guardarCambios()
    {
        if (!modoEdicion) return;
        try
        {
            auxRol.setNombre(jTextFieldNombre.getText());
            auxRol.setDescripcion(jTextFieldRolDes.getText());
            int iTotal=jPanelPermisosRol.getComponentCount();
            for (int i=0;i<iTotal;i++)
            {
                Component auxComponent=jPanelPermisosRol.getComponent(i);
                if (auxComponent instanceof JCheckBox)
                {
                     JCheckBox jCheckBoxPermitir =(JCheckBox)auxComponent;
                     String sIdPerm = (String)jCheckBoxPermitir.getClientProperty(IDPERM_CODE);
                     if (jCheckBoxPermitir.isSelected())
                         auxRol.addPermiso(sIdPerm, aclSelected.getId());
                     else
                         auxRol.deletePermiso(sIdPerm, aclSelected.getId());
              }
            }
        }catch(Exception e)
        {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            logger.error("Error al guardar los cambios:"+sw.toString());
        }

    }
    
	private void jComboBoxAppsActionPerformed() {
		guardarCambios();
		appSelected = (App) jComboBoxApps.getSelectedItem();
		pintarAcls(appSelected.getAcls());
		inicializarPermisosRol(auxRol);
	}
	
	private void jComboBoxACLsActionPerformed() {
		guardarCambios();
		aclSelected = (Acl) jComboBoxACLs.getSelectedItem();
		if(aclSelected!=null){
			pintarPermisosAcls(aclSelected.getPermisos());
			inicializarPermisosRol(auxRol);
		}
	}
	
	public void pintarApps(ListaApp listaApps) {
		try {
			AppComparator myComparator = new AppComparator();
			this.listaApp = listaApps;
			Hashtable hApps = listaApps.gethApps();
			jComboBoxApps.addItem(new App("", "              "));

			boolean sortApp=true;
			if (sortApp){			
				Object[] myArray = hApps.entrySet().toArray();
		        Arrays.sort(myArray,(Comparator)myComparator);
		 
		        for(int i=0;i<myArray.length;++i) {
		        	App auxApp = (App)((Entry)myArray[i]).getValue();
		        	jComboBoxApps.addItem(auxApp);
		        }
			}
			else{			
				for (Enumeration e = hApps.elements(); e.hasMoreElements();) {
					App auxApp = (App) e.nextElement();
					jComboBoxApps.addItem(auxApp);
				}
			}
			
			jComboBoxApps.setRenderer(new ComboBoxRendererApp());
			jComboBoxApps
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxAppsActionPerformed();
						}
					});

		} catch (Exception e) {
			logger.error("Error al pintar los acls: " + e.toString());
		}
	}
	
    public void pintarAcls(ListaAcl listaAcls)
       {
              try
              {
            	    jComboBoxACLs.removeAllItems();
            	    AclComparator myComparator = new AclComparator();
                    this.listaAcl=listaAcls;
                    Hashtable hRoles=listaAcls.gethAcls();
                    jComboBoxACLs.addItem(new Acl("","              ",""));
                    
                    
                    boolean sortAcl=true;
        			if (sortAcl){			
        				Object[] myArray = hRoles.entrySet().toArray();
        		        Arrays.sort(myArray,(Comparator)myComparator);
        		 
        		        for(int i=0;i<myArray.length;++i) {
        		        	Acl auxAcl = (Acl)((Entry)myArray[i]).getValue();
        					jComboBoxACLs.addItem(auxAcl);
        		        }
        			}
        			else{
	                    for (Enumeration e=hRoles.elements();e.hasMoreElements();)
	                    {
	                       Acl auxAcl= (Acl)e.nextElement();
	                       jComboBoxACLs.addItem(auxAcl);
	                    }
        			}
                    jComboBoxACLs.setRenderer(new ComboBoxRendererAcl());
                    jComboBoxACLs.addActionListener(new java.awt.event.ActionListener()
                    {
                         public void actionPerformed(java.awt.event.ActionEvent evt)
                         {
                                 jComboBoxACLsActionPerformed();
                         }
                    }
              );

               }catch(Exception e)
               {
                   logger.error("Error al pintar los permisos: "+e.toString());
               }
       }

    public void pintarRoles(ListaRoles listaRoles)
    {
        this.listaRoles =listaRoles;
        actualizarModelo();
    }

    public void cancelar()
    {
        modoNuevo=false;
       enabledComponents(false);
       mostrarRol(rolSelected);
    }
    public void aceptar()
    {
        guardarCambios();
        CResultadoOperacion result;
        String sMensaje="";
        try
        {
        	//Comprobamos si el rol es vacio.
        	if (auxRol.getNombre().toString().trim().equals("")){
        		sMensaje=mensajesRol.getString("JPanelRoles.mensaje.rolblanco");
        		result= (new CResultadoOperacion(false,sMensaje));
        		 JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                 JDialog dialog =optionPane.createDialog(this,"");
                 dialog.show();
                 return;
        	}
        	
            if (modoNuevo)
            {
            	Hashtable list = listaRoles.gethRoles();
            	Rol aux;
            	Enumeration runList = list.elements();
            	while(runList.hasMoreElements()){
            		aux = (Rol)runList.nextElement();
            		if (aux.getNombre().toString().equals(auxRol.getNombre().toString().trim())){
            			
            		}
            		if (aux.getNombre().toString().equals(auxRol.getNombre().toString().trim())){
            			sMensaje=mensajesRol.getString("JPanelRoles.mensaje.rolrepetido");
            			result= (new CResultadoOperacion(false,sMensaje));
                        JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                        JDialog dialog =optionPane.createDialog(this,"");
                        dialog.show();
                        return;
            		}
            	}
	                result=(new OperacionesAdministrador(Constantes.url)).nuevoRol(auxRol);
	                sMensaje=mensajesRol.getString("JPanelRoles.mensaje.rolinsertado");	               
            }
            else
            {
                result=(new OperacionesAdministrador(Constantes.url)).actualizarRol(auxRol);
                sMensaje=mensajesRol.getString("JPanelRoles.mensaje.rolactualizado");
                jTextFieldBusquedaRol.setText("");
            }
        }catch(Exception e)
        {
              StringWriter sw = new StringWriter();
			  PrintWriter pw = new PrintWriter(sw);
			  e.printStackTrace(pw);
			  logger.error("Exception al grabar en base de datos: " + sw.toString());
              result= new CResultadoOperacion(false,e.getMessage());
        }
        if (result.getResultado())
        {
           rolSelected=auxRol;
           listaRoles.add(rolSelected);
           JOptionPane optionPane= new JOptionPane(sMensaje,JOptionPane.INFORMATION_MESSAGE);
           JDialog dialog =optionPane.createDialog(this,"");
           dialog.show();
           enabledComponents(false);
           modoNuevo=false;
           mostrarRol(rolSelected);
           actualizarModelo();
        }
        else
        {
             JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
             JDialog dialog =optionPane.createDialog(this,"");
             dialog.show();
        }
    }
   
    private void actualizarModelo()
    {
    	actualizarModelo("");
    }
    
    private void actualizarModelo(String filtro)
    {
        modelRoles= new RolesTableModel();
        modelRoles.setModelData(listaRoles, filtro);
        sorter = new TableSorted(modelRoles);
        sorter.setTableHeader(jTableRoles.getTableHeader());
        jTableRoles.setModel(sorter);
        TableColumn column = jTableRoles.getColumnModel().getColumn(RolesTableModel.idIndex);
        column.setPreferredWidth(5);
        column=jTableRoles.getColumnModel().getColumn(RolesTableModel.idNombre);
        column.setPreferredWidth(15);
        jTableRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Ordenamos por nombre de Rol que es la columna 1
    	sorter.setSortingStatus(1, 1);

    }

     private void verPermisos()
     {
            if (rolSelected!=null)
            {
                ShowPermisos permisosDialog = new ShowPermisos(rolSelected.getPermisos(),listaAcl,framePadre, true, mensajesRol);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                permisosDialog.setLocation(d.width/2 - 400/2, d.height/2 - 400/2);
                permisosDialog.setSize(400,400);
                permisosDialog.show();
            }
     }




     private javax.swing.JLabel jLabelRolDes;
     private javax.swing.JButton jButtonRolAceptar;
     private javax.swing.JButton jButtonRolAnadir;
     private javax.swing.JButton jButtonRolCancelar;
     private javax.swing.JButton jButtonRolEditar;
     private javax.swing.JButton jButtonVerPermisos;
     private javax.swing.JScrollPane JScrollPaneListaRoles;
     private javax.swing.JTable jTableRoles;
     private javax.swing.JButton jButtonRolEliminar;
     private javax.swing.JLabel jLabelNombreRol;
     private javax.swing.JPanel jPanelEditarRol;
     private javax.swing.JPanel jPanelPermisosRol;
     private javax.swing.JPanel jPanelListaRoles;
     private javax.swing.JScrollPane jScrollPaneRolPer;
     private com.geopista.app.utilidades.TextField jTextFieldBusquedaRol;
     private com.geopista.app.utilidades.TextField jTextFieldNombre;
     private com.geopista.app.utilidades.TextField jTextFieldRolDes;
     private javax.swing.JComboBox jComboBoxApps;
     private javax.swing.JComboBox jComboBoxACLs;
     private javax.swing.JLabel jLabelBusquedaRol;
     private javax.swing.JLabel jLabelAppRol;
     private javax.swing.JLabel jLabelACLRol;
     private javax.swing.JPanel jPanelPermisosTotal;
     private javax.swing.JPanel jPanelDescripcion;
     private javax.swing.JButton jButtonMarcar;
     private javax.swing.JButton jButtonDesMarcar;
     private javax.swing.JButton jButtonRolTodos;







}