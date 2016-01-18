/**
 * CSearchMetadatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.busqueda;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.geopista.app.metadatos.CMainMetadatos;
import com.geopista.app.metadatos.CMetadatos;
import com.geopista.app.metadatos.calidad.ElementosTableModel;
import com.geopista.app.metadatos.contactos.CSearchContactos;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.OperacionesMetadatos;
import com.geopista.protocol.metadatos.PeticionBusqueda;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-may-2004
 * Time: 18:14:48
 */

public class CSearchMetadatos extends javax.swing.JDialog {
	private Logger logger = Logger.getLogger(CSearchMetadatos.class);
    private ResourceBundle messages;
    private Vector listaMetadatos=null;
    private MetadatosTableModel	modelMetadatos;
    private TableSorted sorter;
    private JFrame framePadre;
    private CI_ResponsibleParty contacto=null;
    private String sFileIdentifierSelected=null;

	/**
	 * Creates new form JSearch
	 */
	public CSearchMetadatos(java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {

		super(parent, modal);
        this.messages=messages;
        this.framePadre=(JFrame)parent;
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
                 PeticionBusqueda peticion= new PeticionBusqueda();
                 listaMetadatos= new OperacionesMetadatos(Constantes.url).getMetadatosParcial(peticion);
                 actualizarModelo();
             }
         }catch (Exception e)
         {
              logger.error("Error de contactos al inicializar los datos: "+e.toString());
              JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();
         }
     }
       private void actualizarModelo()
    {
         modelMetadatos= new MetadatosTableModel();
         modelMetadatos.setModelData(listaMetadatos);
         sorter = new TableSorted(modelMetadatos);
         sorter.setTableHeader(jTableMetadatos.getTableHeader());
         jTableMetadatos.setModel(sorter);
         jTableMetadatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanelPrincipal = new javax.swing.JPanel();
        jButtonMostrar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonNuevo = new javax.swing.JButton();
        jButtonModificarTodos= new javax.swing.JButton();
        jButtonMostrarTodos = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jScrollPaneMetadatos = new javax.swing.JScrollPane();
        jTableMetadatos = new javax.swing.JTable();
        jButtonLoadFile = new javax.swing.JButton();
        jButtonExportar = new javax.swing.JButton();
        jButtonImportar = new javax.swing.JButton();
        jLabelCapa = new javax.swing.JLabel();
        jComboBoxCapas = new com.geopista.app.utilidades.estructuras.ComboBoxEstructuras(Estructuras.getListaCapas(),null
                            ,com.geopista.app.metadatos.init.Constantes.Locale);
        jPanelMetadato = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelIdentificador = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabelResponsable = new javax.swing.JLabel();
        jTextFieldTitulo = new javax.swing.JTextField();
        jTextFieldIdentificador = new javax.swing.JTextField();
        jTextFieldFechaDesde = new javax.swing.JFormattedTextField();
        jTextFieldFechaHasta = new javax.swing.JFormattedTextField();
        jCalendarButtonDesde= new CalendarButton(jTextFieldFechaDesde);
        jCalendarButtonHasta= new CalendarButton(jTextFieldFechaHasta);


        jTextFieldResponsable = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSave2File = new javax.swing.JButton();
        jButtonResponsable= new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelPrincipal.setBorder(new javax.swing.border.EtchedBorder());
        jButtonMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrar();
            }
        });
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminar();
            }
        });
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevo();
            }
        });
        jButtonModificarTodos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        modificarTodos();
                    }
                });

        jButtonMostrarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarTodos();
            }
        });
        jPanelPrincipal.add(jButtonMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 400, 90, -1));
        jPanelPrincipal.add(jButtonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 400, 85, -1));
        jPanelPrincipal.add(jButtonNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 80, -1));
        jPanelPrincipal.add(jButtonModificarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, -1, -1));

        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar();
            }
        });

        jPanelPrincipal.add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 400, 90, -1));
        jScrollPaneMetadatos.setViewportView(jTableMetadatos);
        jPanelPrincipal.add(jScrollPaneMetadatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 460, 130));

        jPanelPrincipal.add(jButtonLoadFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 335, 140, -1));
        jButtonExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportar();
            }
        });
        jPanelPrincipal.add(jButtonExportar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 120, -1));
        jButtonImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importar();
            }
        });
        jPanelPrincipal.add(jButtonImportar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 125, -1));

        jPanelPrincipal.add(jLabelCapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 130, 20));

        jPanelPrincipal.add(jComboBoxCapas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 240, -1));

        jPanelMetadato.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelMetadato.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 20));

        jPanelMetadato.add(jLabelIdentificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 130, 20));

        jPanelMetadato.add(jLabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 20));
        jPanelMetadato.add(jLabelResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, 20));
        jPanelMetadato.add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 240, -1));
        jPanelMetadato.add(jTextFieldIdentificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 240, -1));
        jTextFieldFechaDesde.setEditable(false);
        jPanelMetadato.add(jTextFieldFechaDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, 90, -1));
        jPanelMetadato.add(jCalendarButtonDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 20, 20));
        jTextFieldFechaHasta.setEditable(false);
        jPanelMetadato.add(jTextFieldFechaHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 90, -1));
        jPanelMetadato.add(jCalendarButtonHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 60, 20, 20));
        jPanelMetadato.add(jTextFieldResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 290, -1));
        jButtonResponsable.setText(">>");
               jButtonResponsable.setPreferredSize(new java.awt.Dimension(20, 20));
               jButtonResponsable.addActionListener(new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                       buscarContacto();
                   }
               });
        jPanelMetadato.add(jButtonResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));

        jPanelPrincipal.add(jPanelMetadato, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 470, 120));

        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscar();
            }
        });

        jPanelPrincipal.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 160, 90, -1));
        jPanelPrincipal.add(jButtonMostrarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 160, 90, -1));

        jButtonLoadFile.addActionListener(new java.awt.event.ActionListener() {
                           public void actionPerformed(java.awt.event.ActionEvent evt) {
                               loadFile();
                           }
                       });

        jButtonSave2File.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        save2File();
                    }
                });

        jPanelPrincipal.add(jButtonSave2File, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 335, 140, -1));
        getContentPane().add(jPanelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 430));
           //Para seleccionar una fila
        ListSelectionModel rowSM = jTableMetadatos.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                seleccionarElemento(e);
            }
        });
        pack();
    }//GEN-END:initComponents
    public String getId_capa()
    {
        if (jComboBoxCapas.getSelectedIndex()==0)return null;
        DomainNode capa=(DomainNode) jComboBoxCapas.getSelectedItem();
        return capa.getIdNode();
    }
    public void modificarTodos()
    {
        JDialogModificarTodos modificarDialog = new JDialogModificarTodos(framePadre, true, messages);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        modificarDialog.setLocation(d.width/2 - 350/2, d.height/2 - 200/2);
        modificarDialog.setSize(350,200);
        modificarDialog.show();
    }
	private void buscar() {
        try
        {
                if (!com.geopista.security.SecurityManager.isLogged()) return;
                PeticionBusqueda peticion= new PeticionBusqueda();
                if (jTextFieldIdentificador.getText().length()>0) peticion.setId_metadato(jTextFieldIdentificador.getText());
                if (jTextFieldFechaDesde.getText().length()>0) peticion.setF_desde(jCalendarButtonDesde.getCalendar().getTime());
                if (jTextFieldFechaHasta.getText().length()>0) peticion.setF_hasta(jCalendarButtonHasta.getCalendar().getTime());
                if (contacto!=null) peticion.setId_contact(contacto.getId());
                if (jTextFieldTitulo.getText().length()>0) peticion.setTitulo(jTextFieldTitulo.getText());
                if (getId_capa()!=null) peticion.setId_capa(getId_capa());

                listaMetadatos= new OperacionesMetadatos(Constantes.url).getMetadatosParcial(peticion);
                actualizarModelo();
        }catch (Exception e){
            logger.error("Error al hacer la peticion de busqueda: "+e.toString());
            JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog =optionPane.createDialog(this,"");
            dialog.show();
        }
	}
    private void eliminar()
    {
         try
          {
               if(sFileIdentifierSelected!=null)
               {
                    CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
                    metadatosFrame.delete(sFileIdentifierSelected);
                    buscar();
                }
       }catch (Exception e){
           logger.error("Error al hacer la peticion: "+e.toString());
       }
    }

    private void mostrarTodos() {
        try
        {
                if (!com.geopista.security.SecurityManager.isLogged()) return;
                PeticionBusqueda peticion= new PeticionBusqueda();
                listaMetadatos= new OperacionesMetadatos(Constantes.url).getMetadatosParcial(peticion);
                actualizarModelo();
        }catch (Exception e){
            logger.error("Error al hacer la peticion: "+e.toString());
        }
    }

    private void save2File()
    {
        if(sFileIdentifierSelected!=null)
        {
           CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
           if (metadatosFrame.load(sFileIdentifierSelected))
           {
                metadatosFrame.save2File();
           }
        }
    }

    private void exportar()
    {
        if(sFileIdentifierSelected!=null)
        {
           CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
           if (metadatosFrame.load(sFileIdentifierSelected))
           {
                metadatosFrame.exportar();
           }
        }
    }

    private void loadFile()
    {
        CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
        if (metadatosFrame.loadFile())
        {
             ((CMainMetadatos)framePadre).mostrarMetadato(metadatosFrame);
             dispose();
         }
    }
    private void importar()
    {
        CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
        if (metadatosFrame.importar())
        {
             ((CMainMetadatos)framePadre).mostrarMetadato(metadatosFrame);
             dispose();
         }
    }
    private void mostrar()
    {
        if(sFileIdentifierSelected!=null)
        {
           CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
           if (metadatosFrame.load(sFileIdentifierSelected))
           {
                ((CMainMetadatos)framePadre).mostrarMetadato(metadatosFrame);
                dispose();
           }
        }
    }
    private void nuevo()
    {
      CMetadatos metadatosFrame= new CMetadatos(messages,framePadre);
      ((CMainMetadatos)framePadre).mostrarMetadato(metadatosFrame);
      dispose();
    }
	private void cancelar()
    {
        dispose();
	}
       private void buscarContacto() {
              // Aqui añado lo de buscar contactos
              CSearchContactos dialogContactos = new CSearchContactos(framePadre, true, messages);
              Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
              dialogContactos.setLocation(d.width/2 - dialogContactos.getSize().width/2, d.height/2 - dialogContactos.getSize().height/2);
              dialogContactos.setResizable(false);
              dialogContactos.show();
              if (dialogContactos.isSeleccionado()&&dialogContactos.getContactoSelected()!=null)
               {
                   contacto=dialogContactos.getContactoSelected();
                   jTextFieldResponsable.setText(contacto.getIndividualName()+
                                      (contacto.getOrganisationName()!=null&&contacto.getOrganisationName().length()>0?" ["+contacto.getOrganisationName()+"] ":"")+
                                      (contacto.getPositionName()!=null&&contacto.getPositionName().length()>0?" ["+contacto.getPositionName()+"] ":""));
               }

              dialogContactos=null;
       }
      public void enabled(boolean b)
      {
          jButtonNuevo.setEnabled(b);
          jButtonEliminar.setEnabled(b);
          jButtonModificarTodos.setEnabled(b);
      }
     public void changeScreenLang(ResourceBundle messages)
     {
        this.messages=messages;
        setTitle(messages.getString("CSearchMetadatos.title"));//""Consultar Metadatos");
        jButtonBuscar.setText(messages.getString("CSearchMetadatos.jButtonBuscar"));//"Buscar");
        jButtonCancelar.setText(messages.getString("CSearchMetadatos.jButtonCancelar"));//"Cancelar");
        jButtonMostrar.setText(messages.getString("CSearchMetadatos.jButtonMostrar"));//"Mostrar");
        jButtonEliminar.setText(messages.getString("CSearchMetadatos.jButtonEliminar"));
        jButtonNuevo.setText(messages.getString("CSearchMetadatos.jButtonNuevo"));
        jButtonMostrarTodos.setText(messages.getString("CSearchMetadatos.jButtonMostrarTodos"));
        jButtonExportar.setText(messages.getString("CSearchMetadatos.jButtonExportar"));//"Exportar XML");
        jButtonImportar.setText(messages.getString("CSearchMetadatos.jButtonImportar"));//"Importar XML");
        jLabelCapa.setText(messages.getString("CSearchMetadatos.jLabelCapa"));//"Capa:");
        jLabelTitulo.setText(messages.getString("CSearchMetadatos.jLabelTitulo"));//"Titulo:");
        jPanelMetadato.setBorder(new javax.swing.border.TitledBorder(messages.getString("CSearchMetadatos.jPanelMetadato")));//"Metadato"));
        jLabelResponsable.setText(messages.getString("CSearchMetadatos.jLabelResponsable"));//"Responsable:");
        jButtonSave2File.setText(messages.getString("CSearchMetadatos.jButtonSave2File"));//"Imprimir");
        jButtonLoadFile.setText(messages.getString("CSearchMetadatos.jButtonLoadFile"));//"Imprimir");
        jButtonModificarTodos.setText(messages.getString("CSearchMetadatos.jButtonModificarTodos"));

        jLabelIdentificador.setText(messages.getString("CSearchMetadatos.jLabelIdentificador"));//"Identificador:");
        jLabelFecha.setText(messages.getString("CSearchMetadatos.jLabelFecha"));//"Fecha (desde/hasta):");
    }

    private void seleccionarElemento(ListSelectionEvent e)
    {
       ListSelectionModel lsm = (ListSelectionModel)e.getSource();
       if (lsm.isSelectionEmpty()) { }
       else
       {
            int selectedRow = lsm.getMinSelectionIndex();
            sFileIdentifierSelected=(String)sorter.getValueAt(selectedRow,ElementosTableModel.idIndex);
       }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonMostrar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonMostrarTodos;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonModificarTodos;

    private javax.swing.JButton jButtonLoadFile;
    private javax.swing.JButton jButtonExportar;
    private javax.swing.JButton jButtonImportar;
    private javax.swing.JButton jButtonSave2File;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonResponsable;

    private javax.swing.JFormattedTextField jTextFieldFechaDesde;
    private javax.swing.JFormattedTextField jTextFieldFechaHasta;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelIdentificador;
    private javax.swing.JTextField jTextFieldIdentificador;
    private com.geopista.app.utilidades.estructuras.ComboBoxEstructuras jComboBoxCapas;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JPanel jPanelMetadato;
    private javax.swing.JScrollPane jScrollPaneMetadatos;
    private javax.swing.JTable jTableMetadatos;
    private javax.swing.JLabel jLabelResponsable;
    private javax.swing.JTextField jTextFieldResponsable;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelCapa;
    private javax.swing.JTextField jTextFieldTitulo;
    private CalendarButton jCalendarButtonDesde;
    private CalendarButton jCalendarButtonHasta;
    // End of variables declaration//GEN-END:variables
    }
