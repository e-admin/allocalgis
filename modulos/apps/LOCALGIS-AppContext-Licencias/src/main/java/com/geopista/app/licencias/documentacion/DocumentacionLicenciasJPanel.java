/**
 * DocumentacionLicenciasJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DocumentacionLicencias.java
 *
 * Created on 7 de abril de 2005, 10:36
 */

package com.geopista.app.licencias.documentacion;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CListaAnexosTableModel;
import com.geopista.app.licencias.utilidades.ComboBoxRenderer;
import com.geopista.app.licencias.utilidades.ComboBoxTableEditor;
import com.geopista.app.licencias.utilidades.TextFieldRenderer;
import com.geopista.app.licencias.utilidades.TextFieldTableEditor;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxRendererEstructuras;
import com.geopista.app.utilidades.estructuras.PanelCheckBoxEstructuras;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.ui.AlfrescoExplorer;
import com.geopista.client.alfresco.utils.implementations.LocalgisIntegrationManagerImpl;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.Alegacion;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.Mejora;
import com.geopista.protocol.licencias.tipos.CTipoAnexo;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 *
 * @author  charo
 */
public class DocumentacionLicenciasJPanel extends javax.swing.JPanel {

    Logger logger = Logger.getLogger(DocumentacionLicenciasJPanel.class);

    /** Modelo para el componente listaAnexosJTable */
    private DefaultTableModel _listaAnexosTableModel;
    private DefaultTableModel _listaAnexosAlegacionTableModel;

    /** Estrusturas documentacion obligatoria */
    PanelCheckBoxEstructuras _jPanelCheckBoxEstructura;

    private CSolicitudLicencia _solicitud;
    private CExpedienteLicencia _expediente;

    private Hashtable _hAnexosSolicitud= new Hashtable();
    private Hashtable _hAnexosAnnadidosExpediente = new Hashtable();
    private Hashtable _hAnexosAnnadidosAlegacion= new Hashtable();

    private Hashtable _hAnexosAlegacion= new Hashtable();

    /** Estados en los que se restringe la modificacion de anexos de un expediente */
    private boolean enMejoraDatos= false;
    private boolean enEsperaAlegacion= false;

    private long maxSizeFilesUploaded= 0;
    private String operacion= "";
    private ResourceBundle literales;
    private String tipo;
    
    /** Creates new form DocumentacionLicencias */
    public DocumentacionLicenciasJPanel(ResourceBundle literales, String tipo) {
        this.literales=literales;
        this.tipo=tipo;
        initComponents();
        configureComponents();
        renombrarComponentes(literales);
        cargarTiposAnexosJTable(listaAnexosJTable);
        cargarTiposAnexosJTable(listaAnexosAlegacionJTable);
        cargarDocumentacionObligatoria(tipo);

        /** Annadimos la pestanna de mejoras, como no tenemos solicitud seleccionada, la creamos en blanco */
        //crearTabVacio();
    }
    
    public DocumentacionLicenciasJPanel(ResourceBundle literales) {
    	this(literales,"0");
    }    


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        documentacionJPanel = new javax.swing.JPanel();
        documentacionRequeridaJPanel = new javax.swing.JPanel();
        anexosJPanel = new javax.swing.JPanel();
        listaAnexosJScrollPane = new javax.swing.JScrollPane();
        listaAnexosJTable = new javax.swing.JTable();
        abrirJButton = new javax.swing.JButton();
        annadirJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();
        observacionesJPanel = new javax.swing.JPanel();
        observacionesJScrollPane = new javax.swing.JScrollPane();
        anexosAlegacionJPanel = new javax.swing.JPanel();
        listaAnexosAlegacionJScrollPane = new javax.swing.JScrollPane();
        listaAnexosAlegacionJTable = new javax.swing.JTable();
        abrirAnexoAlegacionJButton = new javax.swing.JButton();
        annadirAnexoAlegacionJButton = new javax.swing.JButton();
        eliminarAnexoAlegacionJButton = new javax.swing.JButton();
        mejoraAnexosJPanel = new javax.swing.JPanel();
        mejorasJTabbedPane = new javax.swing.JTabbedPane();
        abrirAnexoMejoraJButton = new javax.swing.JButton();
        annadirAnexoMejoraJButton = new javax.swing.JButton();
        eliminarAnexoMejoraJButton = new javax.swing.JButton();
        alfrescoJButton = new javax.swing.JButton();
        
        setLayout(new java.awt.BorderLayout());

        documentacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        documentacionRequeridaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        documentacionRequeridaJPanel.setBorder(new javax.swing.border.TitledBorder("Documentacion Requerida"));
        documentacionRequeridaJPanel.setAutoscrolls(true);
        documentacionJPanel.add(documentacionRequeridaJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 200));

        anexosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        anexosJPanel.setBorder(new javax.swing.border.TitledBorder("Anexos"));
        listaAnexosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Tipo", "Fichero", "Observacion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        listaAnexosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaAnexos();
            }
        });

        listaAnexosJScrollPane.setViewportView(listaAnexosJTable);

        anexosJPanel.add(listaAnexosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 470, 100));

        abrirJButton.setIcon(new javax.swing.ImageIcon(""));
        abrirJButton.setToolTipText("");
        abrirJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        abrirJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        abrirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrir();
            }
        });

        anexosJPanel.add(abrirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        annadirJButton.setIcon(new javax.swing.ImageIcon(""));
        annadirJButton.setToolTipText("");
        annadirJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        annadirJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        annadirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadir();
            }
        });

        anexosJPanel.add(annadirJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, 20, 20));

        eliminarJButton.setIcon(new javax.swing.ImageIcon(""));
        eliminarJButton.setToolTipText("");
        eliminarJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        eliminarJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        eliminarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminar();
            }
        });

        anexosJPanel.add(eliminarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 20, 20));

        documentacionJPanel.add(anexosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 520, 130));

        observacionesJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        observacionesJPanel.setBorder(new javax.swing.border.TitledBorder("Observaciones Generales"));
        observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        //observacionesJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 50));

        documentacionJPanel.add(observacionesJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 520, 200));

        anexosAlegacionJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        anexosAlegacionJPanel.setBorder(new javax.swing.border.TitledBorder("Alegaciones"));
        listaAnexosAlegacionJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Tipo", "Fichero", "Observacion", "Almacen"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        listaAnexosAlegacionJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaAnexosAlegacion();
            }
        });

        listaAnexosAlegacionJScrollPane.setViewportView(listaAnexosAlegacionJTable);

        anexosAlegacionJPanel.add(listaAnexosAlegacionJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 470, 80));

        abrirAnexoAlegacionJButton.setIcon(new javax.swing.ImageIcon(""));
        abrirAnexoAlegacionJButton.setToolTipText("");
        abrirAnexoAlegacionJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        abrirAnexoAlegacionJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        abrirAnexoAlegacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirAnexoAlegacion();
            }
        });

        anexosAlegacionJPanel.add(abrirAnexoAlegacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 20, 20));

        annadirAnexoAlegacionJButton.setIcon(new javax.swing.ImageIcon(""));
        annadirAnexoAlegacionJButton.setToolTipText("");
        annadirAnexoAlegacionJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        annadirAnexoAlegacionJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        annadirAnexoAlegacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirAnexoAlegacion();
            }
        });

        anexosAlegacionJPanel.add(annadirAnexoAlegacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, 20, 20));

        eliminarAnexoAlegacionJButton.setIcon(new javax.swing.ImageIcon(""));
        eliminarAnexoAlegacionJButton.setToolTipText("");
        eliminarAnexoAlegacionJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        eliminarAnexoAlegacionJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        eliminarAnexoAlegacionJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarAnexoAlegacion();
            }
        });

        anexosAlegacionJPanel.add(eliminarAnexoAlegacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 20, 20));

        documentacionJPanel.add(anexosAlegacionJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 520, 120));

        mejoraAnexosJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        mejoraAnexosJPanel.setBorder(new javax.swing.border.TitledBorder("Mejoras"));
        mejorasJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        mejorasJTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mejoras();
            }
        });

        //mejoraAnexosJPanel.add(mejorasJTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 470, 160));

        abrirAnexoMejoraJButton.setIcon(new javax.swing.ImageIcon(""));
        abrirAnexoMejoraJButton.setToolTipText("");
        abrirAnexoMejoraJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        abrirAnexoMejoraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        abrirAnexoMejoraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirAnexoMejora();
            }
        });

        mejoraAnexosJPanel.add(abrirAnexoMejoraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, 20, 20));

        annadirAnexoMejoraJButton.setIcon(new javax.swing.ImageIcon(""));
        annadirAnexoMejoraJButton.setToolTipText("");
        annadirAnexoMejoraJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        annadirAnexoMejoraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        annadirAnexoMejoraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirAnexoMejora();
            }
        });

        mejoraAnexosJPanel.add(annadirAnexoMejoraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, 20, 20));

        eliminarAnexoMejoraJButton.setIcon(new javax.swing.ImageIcon(""));
        eliminarAnexoMejoraJButton.setToolTipText("");
        eliminarAnexoMejoraJButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        eliminarAnexoMejoraJButton.setPreferredSize(new java.awt.Dimension(30, 30));
        eliminarAnexoMejoraJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarAnexoMejora();
            }
        });

        mejoraAnexosJPanel.add(eliminarAnexoMejoraJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, 20, 20));
        
        boolean activo=LocalgisIntegrationManagerImpl.verifyStatusAlfresco(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL, "",false),
				String.valueOf(AppContext.getIdMunicipio()),AlfrescoConstants.APP_GENERAL);
        AlfrescoManagerUtils.setAlfrescoActive(activo);
        
        if(AlfrescoManagerUtils.isAlfrescoClientActive()){
        	alfrescoJButton.setEnabled(false); 
	        alfrescoJButton.setText(literales.getString("alfresco.button.documentManager"));
	        alfrescoJButton.setToolTipText(literales.getString("alfresco.button.documentManager"));
	        alfrescoJButton.setPreferredSize(new java.awt.Dimension(150, 25));
	        alfrescoJButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                docManager();
	            }
	        });
	        
	        documentacionJPanel.add(alfrescoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 582, 150, 25));
        }
        
        documentacionJPanel.add(mejoraAnexosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 520, 190));

        add(documentacionJPanel, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents

    private boolean configureComponents() {

		try {
            /** Anexos */
            String[] columnNamesAnexos= {literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column1"),
                                         literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column2"),
                                         literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column3"),
                                         literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column4")};

            CListaAnexosTableModel.setColumnNames(columnNamesAnexos);
            _listaAnexosTableModel = new CListaAnexosTableModel();
            listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listaAnexosJTable.setModel(_listaAnexosTableModel);
            listaAnexosJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            listaAnexosJTable.getTableHeader().setReorderingAllowed(false);
            for (int j=0; j< listaAnexosJTable.getColumnCount(); j++){
                TableColumn column = listaAnexosJTable.getColumnModel().getColumn(j);
                if (j==2){
                    column.setMinWidth(250);
                    column.setMaxWidth(300);
                    column.setWidth(250);
                    column.setPreferredWidth(250);
                }else{
                    column.setMinWidth(150);
                    column.setMaxWidth(300);
                    column.setWidth(150);
                    column.setPreferredWidth(150);
                }
                column.setResizable(true);
            }

            /** Anexos Alegacion */
            String[] columnNamesAnexosAlegacion= {literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column1"),
                                                  literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column2"),
                                                  literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column3"),
                                                  literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column4")};

            CListaAnexosTableModel.setColumnNames(columnNamesAnexosAlegacion);
            _listaAnexosAlegacionTableModel = new CListaAnexosTableModel();
            listaAnexosAlegacionJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listaAnexosAlegacionJTable.setModel(_listaAnexosAlegacionTableModel);
            listaAnexosAlegacionJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            listaAnexosAlegacionJTable.getTableHeader().setReorderingAllowed(false);
            for (int j=0; j< listaAnexosAlegacionJTable.getColumnCount(); j++){
                TableColumn column = listaAnexosAlegacionJTable.getColumnModel().getColumn(j);
                if (j==2){
                    column.setMinWidth(250);
                    column.setMaxWidth(300);
                    column.setWidth(250);
                    column.setPreferredWidth(250);
                }else{
                    column.setMinWidth(150);
                    column.setMaxWidth(300);
                    column.setWidth(150);
                    column.setPreferredWidth(150);
                }
                column.setResizable(true);
            }

            observacionesTPane= new TextPane(254);
            observacionesJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            observacionesJScrollPane.setViewportView(observacionesTPane);
            observacionesJPanel.add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 65));


            abrirAnexoAlegacionJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAbrir);
            abrirAnexoMejoraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAbrir);
            abrirJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAbrir);
            annadirAnexoAlegacionJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAdd);
            annadirAnexoMejoraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAdd);
            annadirJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoAdd);
            eliminarAnexoAlegacionJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoRemove);
            eliminarAnexoMejoraJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoRemove);
            eliminarJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoRemove);

            return true;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }

   }

    private void eliminarAnexoAlegacion() {//GEN-FIRST:event_eliminarAnexoAlegacionJButtonActionPerformed
        /** Comprobamos que haya cargada una solicitud */
        if (_solicitud != null){
            if (eliminarAnexoAlegacionJButton.isEnabled()) {
                int selected = listaAnexosAlegacionJTable.getSelectedRow();
                if (selected != -1) {
                    int ok= JOptionPane.showConfirmDialog(this, literales.getString("Licencias.confirmarBorrado"), literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.NO_OPTION) return;
                    
                    /** Actualizamos el tamanno total de los ficheros a enviar */
                    String nombreAnexo = (String) _listaAnexosAlegacionTableModel.getValueAt(selected, 0);
                    File file = new File(nombreAnexo);
                    if (file.isAbsolute()) {
                        long size= file.length();
                        if (getMaxSizeFilesUploaded() > size){
                           setMaxSizeFilesUploaded(getMaxSizeFilesUploaded() - size);
                        }else{
                            setMaxSizeFilesUploaded(0);
                        }
                    }

                    _listaAnexosAlegacionTableModel.removeRow(selected);

                    /** Comprobamos si algun item de la lista queda seleccionado.
                     *  Si es asi, habilitamos el boton Eliminar, si no, le deshabilitamos
                     */
                    if (listaAnexosAlegacionJTable.getModel().getRowCount() > 0) {
                        if (listaAnexosAlegacionJTable.getSelectedRow() != -1) {
                            eliminarAnexoAlegacionJButton.setEnabled(true);
                        } else {
                            eliminarAnexoAlegacionJButton.setEnabled(false);
                        }
                    } else {
                        eliminarAnexoAlegacionJButton.setEnabled(false);
                        abrirAnexoAlegacionJButton.setEnabled(false);
                    }
                } else { // no ha seleccionado ningun item
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }

    }//GEN-LAST:event_eliminarAnexoAlegacionJButtonActionPerformed

    private void annadirAnexoAlegacion() {//GEN-FIRST:event_annadirAnexoAlegacionJButtonActionPerformed
        /** Comprobamos que haya cargada una solicitud */
        if (_solicitud != null){

            listaAnexosAlegacionJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            JFileChooser chooser = new JFileChooser();
            com.geopista.app.utilidades.GeoPistaFileFilter filter = new com.geopista.app.utilidades.GeoPistaFileFilter();
            filter.addExtension("doc");
            filter.addExtension("txt");
            filter.setDescription("Fichero DOC & TXT");
            chooser.setFileFilter(filter);
            /** Permite multiples selecciones */
            chooser.setMultiSelectionEnabled(true);

            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = chooser.getSelectedFiles();
                System.out.println("Fichero(s) seleccionado " + selectedFiles.length);
                if (selectedFiles.length > 0) {
                    for (int i = 0; i < selectedFiles.length; i++) {
                        System.out.println("\t" + selectedFiles[i].getPath());
                        System.out.println("Abrimos el fichero: " + selectedFiles[i].getAbsolutePath());

                        for (int j = 0; j < _listaAnexosAlegacionTableModel.getRowCount(); j++) {
                            String nombreAnexo = (String) _listaAnexosAlegacionTableModel.getValueAt(j, 0);
                            File file = new File(nombreAnexo);
                            if (file.isAbsolute()) {
                                nombreAnexo = file.getName();
                            }

                            if (nombreAnexo.equalsIgnoreCase(selectedFiles[i].getName())) {
                                /** Ya existe un anexo con ese nombre */
                                mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje6"));
                                return;
                            }
                        }

                        /** comprobamos que no exceda el tamanno maximo permitido */
                        if (selectedFiles[i].isAbsolute()){
                            long sizeFilesUploaded= getMaxSizeFilesUploaded();
                            sizeFilesUploaded=+selectedFiles[i].length();
                            if (sizeFilesUploaded > CConstantesLicencias.totalMaxSizeFilesUploaded){
                                JOptionPane optionPane= new JOptionPane(literales.getString("DocumentacionLicenciasJPanel.mensaje5")+": " +CConstantesLicencias.totalMaxSizeFilesUploaded+" bytes", JOptionPane.ERROR_MESSAGE);
                                JDialog dialog =optionPane.createDialog(this,"ERROR");
                                dialog.show();
                                return;
                            }else{
                                setMaxSizeFilesUploaded(sizeFilesUploaded);
                            }
                        }

                        /** Por defecto selected el primer tipo */
                        JComboBox combox = (JComboBox) ((ComboBoxTableEditor) listaAnexosAlegacionJTable.getCellEditor(_listaAnexosAlegacionTableModel.getRowCount(), 1)).getComponent();
                        Object[] rowData = {selectedFiles[i].getAbsolutePath(), combox.getItemAt(0), ""};
                        _listaAnexosAlegacionTableModel.addRow(rowData);
                        /** Marcamos el fichero como annadido y lo insertamos en un vector auxiliar de annadidos*/

                        if ((_hAnexosAnnadidosAlegacion.get(selectedFiles[i].getName()) == null)) {
                            CAnexo anexo = new CAnexo(new CTipoAnexo(), selectedFiles[i].getName(), "");
                            anexo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                            anexo.setComesFrom("ALEGACION");
                            _hAnexosAnnadidosAlegacion.put(selectedFiles[i].getName(), anexo);
                        }
                    }
                }
            }


            /** Solo se podra seleccionar un elemento de la lista */
            listaAnexosAlegacionJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

    }//GEN-LAST:event_annadirAnexoAlegacionJButtonActionPerformed

    private void abrirAnexoAlegacion() {//GEN-FIRST:event_abrirAnexoAlegacionJButtonActionPerformed
        if ((_solicitud != null) && (_expediente != null)){
            Alegacion alegacion= _expediente.getAlegacion();
            if (alegacion != null){
                int row = listaAnexosAlegacionJTable.getSelectedRow();
                logger.info("row: " + row);

                if (row == -1) {
                    mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje1"));
                    return;
                }

                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                String fileName = (String) listaAnexosAlegacionJTable.getValueAt(row, 0);
                logger.info("fileName: " + fileName);

                String tmpFile= "";
                File f= new File(fileName);

                if (!f.isAbsolute()){

                    /** Dialogo para seleccionar donde dejar el fichero */
                    JFileChooser chooser = new JFileChooser();

                    /** Permite multiples selecciones */
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setSelectedFile(f);

                    int returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile= chooser.getSelectedFile();

                        String tmpDir= "";
                        tmpFile= selectedFile.getAbsolutePath();
                        int index= tmpFile.lastIndexOf(selectedFile.getName());
                        if (index != -1){
                            tmpDir= tmpFile.substring(0, index);
                        }

                       /** Comprobamos si existe el directorio. */
                        try {
                            File dir = new File(tmpDir);
                            logger.info("dir: " + dir);

                            if (!dir.exists()) {
                                logger.warn("Directorio temporal creado.");
                                dir.mkdirs();
                            }
                        } catch (Exception ex) {
                            logger.error("Exception: " + ex.toString());
                        }
                        boolean resultado = false;
                        CAnexo anexo = (CAnexo)_hAnexosSolicitud.get(fileName);
                        if(!AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                        	resultado = CUtilidadesComponentes.GetURLFile(CConstantesComando.anexosLicenciasUrl + _solicitud.getIdSolicitud() + "/" + "alegacion" + "/" + alegacion.getIdAlegacion() + "/" + fileName, tmpFile, "", 0);
                        }
                        else {                        
                        	AlfrescoDocumentClient alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ALFRESCO_WEBAPP_NAME +
                                    ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
                        	try {
                        		String id = CUtilidadesComponentes.getAnexoId(selectedFile.getName(), _solicitud.getIdSolicitud());
                        		if(id != null){
                        			resultado = alfrescoDocumentClient.downloadFile(new AlfrescoKey(id, AlfrescoKey.KEY_UUID), tmpDir, selectedFile.getName());
                        		}
							} catch (Exception e) {
								logger.error(e);
							}
                        }
                        if (!resultado) {
                            mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje2"));
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            return;
                        }
                    }else{
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        return;
                    }
                }else{
                    tmpFile= f.getAbsolutePath();
                }

                /** Visualizamos el fichero descargado si SO es Windows. */
                if (CUtilidadesComponentes.isWindows()){
                    try {
                        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tmpFile + "\"");

                    } catch (Exception ex) {
                        logger.warn("Exception: " + ex.toString());
                        mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje3") + " " + tmpFile);
                    }
                }

                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

    }//GEN-LAST:event_abrirAnexoAlegacionJButtonActionPerformed

    private void eliminarAnexoMejora() {//GEN-FIRST:event_eliminarAnexoMejoraJButtonActionPerformed
        /** Comprobamos que haya cargada una solicitud */
        if (_solicitud != null){
            if (eliminarAnexoMejoraJButton.isEnabled()) {
                MejorasJTabPanel tabPanelSelected= (MejorasJTabPanel)mejorasJTabbedPane.getSelectedComponent();
                tabPanelSelected.eliminarAnexoSeleccionado();
                eliminarAnexoMejoraJButton.setEnabled(false);
                abrirAnexoMejoraJButton.setEnabled(false);
            }
        }

    }//GEN-LAST:event_eliminarAnexoMejoraJButtonActionPerformed

    private void annadirAnexoMejora() {//GEN-FIRST:event_annadirAnexoMejoraJButtonActionPerformed
        /** Comprobamos que haya cargada una solicitud */
        if (_solicitud != null){
            MejorasJTabPanel tabPanelSelected= (MejorasJTabPanel)mejorasJTabbedPane.getSelectedComponent();
            tabPanelSelected.annadirAnexoMejora();
        }
    }//GEN-LAST:event_annadirAnexoMejoraJButtonActionPerformed

    private void abrirAnexoMejora() {//GEN-FIRST:event_abrirAnexoMejoraJButtonActionPerformed
        if (_solicitud != null){
            MejorasJTabPanel tabPanelSelected= (MejorasJTabPanel)mejorasJTabbedPane.getSelectedComponent();
            tabPanelSelected.abrirAnexoMejora(_solicitud.getIdSolicitud());
        }
    }//GEN-LAST:event_abrirAnexoMejoraJButtonActionPerformed

    private void eliminar() {//GEN-FIRST:event_eliminarJButtonActionPerformed
        if (eliminarJButton.isEnabled()) {
            int selected = listaAnexosJTable.getSelectedRow();
            if (selected != -1) {
                int ok= JOptionPane.showConfirmDialog(this, literales.getString("Licencias.confirmarBorrado"), literales.getString("Licencias.tittle"), JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.NO_OPTION) return;

                /** Actualizamos el tamanno total de los ficheros a enviar */
                String nombreAnexo = (String) _listaAnexosTableModel.getValueAt(selected, 0);
                File file = new File(nombreAnexo);
                if (file.isAbsolute()) {
                    long size= file.length();
                    if (getMaxSizeFilesUploaded() > size){
                       setMaxSizeFilesUploaded(getMaxSizeFilesUploaded() - size);
                    }else{
                        setMaxSizeFilesUploaded(0);
                    }
                }
                _listaAnexosTableModel.removeRow(selected);

                /** Comprobamos si algun item de la lista queda seleccionado.
                 *  Si es asi, habilitamos el boton Eliminar, si no, le deshabilitamos
                 */
                if (listaAnexosJTable.getModel().getRowCount() > 0) {
                    if (listaAnexosJTable.getSelectedRow() != -1) {
                        eliminarJButton.setEnabled(true);
                    } else {
                        eliminarJButton.setEnabled(false);
                    }
                } else {
                    eliminarJButton.setEnabled(false);
                    abrirJButton.setEnabled(false);
                }
            } else { // no ha seleccionado ningun item
                Toolkit.getDefaultToolkit().beep();
            }
        }
        
    }//GEN-LAST:event_eliminarJButtonActionPerformed

    private void annadir() {//GEN-FIRST:event_annadirJButtonActionPerformed
         if ((_solicitud != null) || (getOperacion().equalsIgnoreCase("CREACION"))){
            listaAnexosJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            JFileChooser chooser = new JFileChooser();
            com.geopista.app.utilidades.GeoPistaFileFilter filter = new com.geopista.app.utilidades.GeoPistaFileFilter();
            filter.addExtension("doc");
            filter.addExtension("txt");
            filter.setDescription("Fichero DOC & TXT");
            chooser.setFileFilter(filter);
            /** Permite multiples selecciones */
            chooser.setMultiSelectionEnabled(true);

            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = chooser.getSelectedFiles();
                System.out.println("Fichero(s) seleccionado " + selectedFiles.length);
                if (selectedFiles.length > 0) {
                    for (int i = 0; i < selectedFiles.length; i++) {
                        System.out.println("\t" + selectedFiles[i].getPath());
                        System.out.println("Abrimos el fichero: " + selectedFiles[i].getAbsolutePath());

                        for (int j = 0; j < _listaAnexosTableModel.getRowCount(); j++) {
                            String nombreAnexo = (String) _listaAnexosTableModel.getValueAt(j, 0);
                            File file = new File(nombreAnexo);
                            if (file.isAbsolute()) {
                                nombreAnexo = file.getName();
                            }

                            if (nombreAnexo.equalsIgnoreCase(selectedFiles[i].getName())) {
                                /** Ya existe un anexo con ese nombre */
                                mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje6"));
                                return;
                            }
                        }

                        /** comprobamos que no exceda el tamanno maximo permitido */
                        if (selectedFiles[i].isAbsolute()){
                            long sizeFilesUploaded= getMaxSizeFilesUploaded();
                            sizeFilesUploaded=+selectedFiles[i].length();
                            if (sizeFilesUploaded > CConstantesLicencias.totalMaxSizeFilesUploaded){
                                JOptionPane optionPane= new JOptionPane(literales.getString("DocumentacionLicenciasJPanel.mensaje5")+": " +CConstantesLicencias.totalMaxSizeFilesUploaded+" bytes", JOptionPane.ERROR_MESSAGE);
                                JDialog dialog =optionPane.createDialog(this,"ERROR");
                                dialog.show();
                                return;
                            }else{
                                setMaxSizeFilesUploaded(sizeFilesUploaded);
                            }
                        }

                        /** Por defecto selected el primer tipo */
                        JComboBox combox = (JComboBox) ((ComboBoxTableEditor) listaAnexosJTable.getCellEditor(_listaAnexosTableModel.getRowCount(), 1)).getComponent();
                        Object[] rowData = {selectedFiles[i].getAbsolutePath(), combox.getItemAt(0), ""};
                        _listaAnexosTableModel.addRow(rowData);
                        /** Marcamos el fichero como annadido y lo insertamos en un vector auxiliar de annadidos*/

                        if ((_hAnexosAnnadidosExpediente.get(selectedFiles[i].getName()) == null)) {
                            CAnexo anexo = new CAnexo(new CTipoAnexo(), selectedFiles[i].getName(), "");
                            anexo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                            _hAnexosAnnadidosExpediente.put(selectedFiles[i].getName(), anexo);
                        }
                    }
                }
            }


            /** Solo se podra seleccionar un elemento de la lista */
            listaAnexosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

    }//GEN-LAST:event_annadirJButtonActionPerformed


    private void abrir() {//GEN-FIRST:event_abrirJButtonActionPerformed
        if ((_solicitud != null) || (getOperacion().equalsIgnoreCase("CREACION"))){
            int row = listaAnexosJTable.getSelectedRow();
            logger.info("row: " + row);

            if (row == -1) {
                mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje1"));
                return;
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            String fileName= (String) listaAnexosJTable.getValueAt(row, 0);
            logger.info("fileName: " + fileName);

            String tmpFile= "";
            File f= new File(fileName);

            if (!f.isAbsolute()){
                /** Dialogo para seleccionar donde dejar el fichero */
                JFileChooser chooser = new JFileChooser();

                /** Permite multiples selecciones */
                chooser.setMultiSelectionEnabled(false);
                chooser.setSelectedFile(f);

                int returnVal = chooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile= chooser.getSelectedFile();

                    if (selectedFile != null){
                        String tmpDir= "";
                        tmpFile= selectedFile.getAbsolutePath();
                        int index= tmpFile.lastIndexOf(selectedFile.getName());
                        if (index != -1){
                            tmpDir= tmpFile.substring(0, index);
                        }

                       /** Comprobamos si existe el directorio. */
                        try {
                            File dir = new File(tmpDir);
                            logger.info("dir: " + dir);

                            if (!dir.exists()) {
                                logger.warn("Directorio temporal creado.");
                                dir.mkdirs();
                            }
                        } catch (Exception ex) {
                            logger.error("Exception: " + ex.toString());
                        }

                        /** Para no tener problemas con la barra, utilizamos la de Unix. */
                        
                        boolean resultado = false;
                        CAnexo anexo = (CAnexo)_hAnexosSolicitud.get(fileName);
                        if(!AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                        	resultado = CUtilidadesComponentes.GetURLFile(CConstantesComando.anexosLicenciasUrl + _solicitud.getIdSolicitud() + "/" + fileName, tmpFile, "", 0);
                        }
                        else {
                        	AlfrescoDocumentClient alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ALFRESCO_WEBAPP_NAME +
                                    ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
                        	try {
                        		String id = CUtilidadesComponentes.getAnexoId(selectedFile.getName(), _solicitud.getIdSolicitud());
                        		if(id != null){
                        			resultado = alfrescoDocumentClient.downloadFile(new AlfrescoKey(id, AlfrescoKey.KEY_UUID), tmpDir, selectedFile.getName());
                        		}
                        	} catch (Exception e) {
								logger.error(e);
							}
                        }
                        if (!resultado) {
                            mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje2"));
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            return;
                        }
                    }else{
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        return;
                    }
                }else{
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
            }else{
                tmpFile= f.getAbsolutePath();
            }

            /** Visualizamos el fichero descargado si SO es Windows. */
            if (CUtilidadesComponentes.isWindows()){
                try {
                    Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tmpFile + "\"");

                } catch (Exception ex) {
                    logger.warn("Exception: " + ex.toString());
                    mostrarMensaje(literales.getString("DocumentacionLicenciasJPanel.mensaje3") + " " + tmpFile);
                }
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_abrirJButtonActionPerformed


    private void mejoras() {//GEN-FIRST:event_mejorasJTabbedPaneStateChanged
        MejorasJTabPanel tab= (MejorasJTabPanel)mejorasJTabbedPane.getSelectedComponent();
        if (tab != null){
            if ((tab.isFirst()) && (enMejoraDatos())){
                CAnexo anexo= tab.getAnexoSelected();
                if (anexo != null) {
                    /** boton eliminar */
                    if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                        eliminarAnexoMejoraJButton.setEnabled(true);
                    }else{
                        eliminarAnexoMejoraJButton.setEnabled(false);
                    }
                    /** boton annadir */
                    if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                        annadirAnexoMejoraJButton.setEnabled(true);
                    }else{
                        annadirAnexoMejoraJButton.setEnabled(false);
                    }
                     /** boton abrir */
                    abrirAnexoMejoraJButton.setEnabled(true);
                }else{
                    eliminarAnexoMejoraJButton.setEnabled(false);
                    abrirAnexoMejoraJButton.setEnabled(false);
                    if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                        annadirAnexoMejoraJButton.setEnabled(true);
                    }else{
                        annadirAnexoMejoraJButton.setEnabled(false);
                    }
                }
            }else if (tab.getAnexoSelected() != null){
                eliminarAnexoMejoraJButton.setEnabled(false);
                abrirAnexoMejoraJButton.setEnabled(true);
                annadirAnexoMejoraJButton.setEnabled(false);
            }else{
                eliminarAnexoMejoraJButton.setEnabled(false);
                abrirAnexoMejoraJButton.setEnabled(false);
                annadirAnexoMejoraJButton.setEnabled(false);
            }
        }

    }//GEN-LAST:event_mejorasJTabbedPaneStateChanged



    public void anexoMejorasJTabPanelSelected(){

        MejorasJTabPanel tab= (MejorasJTabPanel)mejorasJTabbedPane.getSelectedComponent();
        if ((tab.isFirst() && (enMejoraDatos()))){
            CAnexo anexo= tab.getAnexoSelected();
            if (anexo != null) {
                /** boton eliminar */
                if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                    eliminarAnexoMejoraJButton.setEnabled(true);
                }else{
                    eliminarAnexoMejoraJButton.setEnabled(false);
                }
                /** boton annadir */
                if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                    annadirAnexoMejoraJButton.setEnabled(true);
                }else{
                    annadirAnexoMejoraJButton.setEnabled(false);
                }
                 /** boton abrir */
                 abrirAnexoMejoraJButton.setEnabled(true);
            }else{
                eliminarAnexoMejoraJButton.setEnabled(false);
                abrirAnexoMejoraJButton.setEnabled(false);
                if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                    annadirAnexoMejoraJButton.setEnabled(true);
                }else{
                    annadirAnexoMejoraJButton.setEnabled(false);
                }
            }
        }else{
            eliminarAnexoMejoraJButton.setEnabled(false);
            abrirAnexoMejoraJButton.setEnabled(true);
            annadirAnexoMejoraJButton.setEnabled(false);
        }
    }

    private void listaAnexosAlegacion() {//GEN-FIRST:event_listaAnexosAlegacionJTableFocusGained
         int selected = listaAnexosAlegacionJTable.getSelectedRow();
        if (selected != -1) {
            if ((enEsperaAlegacion()) && (!getOperacion().equalsIgnoreCase("CONSULTA"))) {
                eliminarAnexoAlegacionJButton.setEnabled(true);
            }else{
                eliminarAnexoAlegacionJButton.setEnabled(false);
            }

            abrirAnexoAlegacionJButton.setEnabled(true);
        }

    }//GEN-LAST:event_listaAnexosAlegacionJTableFocusGained


    private void listaAnexos() {//GEN-FIRST:event_listaAnexosJTableFocusGained
        int selected = listaAnexosJTable.getSelectedRow();
        if (selected != -1) {
            if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                eliminarJButton.setEnabled(true);
            }else{
                eliminarJButton.setEnabled(false);
            }

            abrirJButton.setEnabled(true);
        }

    }//GEN-LAST:event_listaAnexosJTableFocusGained

    public void renombrarComponentes(ResourceBundle literales)
    {
        try{

            /** Headers Anexos */
            TableColumn tableColumn= listaAnexosJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column1"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column2"));
            tableColumn= listaAnexosJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column3"));

            anexosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("DocumentacionLicenciasJPanel.anexosJPanel.TitleBorder")));
            annadirJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.annadirJButton.toolTipText.text"));
            eliminarJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.eliminarJButton.toolTipText.text"));
            abrirJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.abrirJButton.toolTipText.text"));

            /** Headers Anexos Alegacion */
            tableColumn= listaAnexosAlegacionJTable.getColumnModel().getColumn(0);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column1"));
            tableColumn= listaAnexosAlegacionJTable.getColumnModel().getColumn(1);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column2"));
            tableColumn= listaAnexosAlegacionJTable.getColumnModel().getColumn(2);
            tableColumn.setHeaderValue(literales.getString("DocumentacionLicenciasJPanel.listaAnexosJTable.column3"));

            anexosAlegacionJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("DocumentacionLicenciasJPanel.anexosAlegacionJPanel.TitleBorder")));
            annadirAnexoAlegacionJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.annadirJButton.toolTipText.text"));
            eliminarAnexoAlegacionJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.eliminarJButton.toolTipText.text"));
            abrirAnexoAlegacionJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.abrirJButton.toolTipText.text"));


            /** Mejoras */
            mejoraAnexosJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("DocumentacionLicenciasJPanel.anexosMejoraJPanel.TitleBorder")));
            annadirAnexoMejoraJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.annadirJButton.toolTipText.text"));
            eliminarAnexoMejoraJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.eliminarJButton.toolTipText.text"));
            abrirAnexoMejoraJButton.setToolTipText(literales.getString("DocumentacionLicenciasJPanel.abrirJButton.toolTipText.text"));

            /** Headers Anexos Mejora */
            for (int i= 0; i < mejorasJTabbedPane.getTabCount(); i++){
                MejorasJTabPanel tab= (MejorasJTabPanel)mejorasJTabbedPane.getComponentAt(i);
                tab.renombrarComponentes(literales);
                mejorasJTabbedPane.setTitleAt(i, literales.getString("MejorasJTabPanel.TitleTab"));
            }

            /* Documentacion requerida */
            documentacionRequeridaJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("DocumentacionLicenciasJPanel.documentacionRequeridaJPanel.TitleBorder")));

            /* Observaciones Generales */
            observacionesJPanel.setBorder(new javax.swing.border.TitledBorder(literales.getString("DocumentacionLicenciasJPanel.observacionesJPanel.TitleBorder")));

        }catch(Exception e){
            logger.error("Error:",e);
        }
    }
                                                                                                               
    public void cargarTiposAnexosJTable(javax.swing.JTable jtable) {
        if (Estructuras.getListaTiposAnexo().getLista().size() > 0){
            // Annadimos a la tabla el editor ComboBox en la segunda columna
            int vColIndexCB = 1;
            TableColumn col2 = jtable.getColumnModel().getColumn(vColIndexCB);
            col2.setCellEditor(new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false)));
            col2.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false));

        } else {
            String[] values = {""};
            // Annadimos a la tabla el editor ComboBox en la segunda columna
            int vColIndexCB = 1;
            TableColumn col2 = jtable.getColumnModel().getColumn(vColIndexCB);
            col2.setCellEditor(new ComboBoxTableEditor(new JComboBox(values)));
            col2.setCellRenderer(new ComboBoxRenderer(values));
        }

        // Annadimos a la tabla el editor TextField en la tercera columna
        int vColIndexTF = 2;
        TableColumn col3 = jtable.getColumnModel().getColumn(vColIndexTF);
        col3.setCellEditor(new TextFieldTableEditor(254));
        col3.setCellRenderer(new TextFieldRenderer());
    }

    public void load(CExpedienteLicencia expediente,CSolicitudLicencia solicitud, boolean bModificacion)
    {
        clearDocumentacionJPanel();
        setSolicitudLicencia(solicitud);
        setExpediente(expediente);
        setDocumentacionObligatoriaSeleccionada(solicitud.getDocumentacionEntregada());
        setObservacionesGenerales(solicitud.getObservacionesDocumentacionEntregada());
        cargarAnexosJTable(solicitud.getAnexos(), expediente.getVU());
        if (bModificacion)
            setBotoneraModificacion();
        else
            setBotoneraConsulta();
        alfrescoJButton.setEnabled(true);
    }

    public void setBotoneraModificacion(){
         /* SOLICITUD */
        setEnabledAnnadirJButton(true);
        setEnabledAbrirJButton(true);
        setEnabledEliminarJButton(false);

        /* MEJORA */
        if (_solicitud.getMejoras() == null){
            setVisibleMejoraAnexosJPanel(false);
        }else{
            setVisibleMejoraAnexosJPanel(true);
            setEnabledAbrirAnexoMejoraJButton(false);
            setEnabledEliminarAnexoMejoraJButton(false);
            setEnMejoraDatos(_expediente.getEstado().getIdEstado() == CConstantesLicencias.EstadoMejoraDatos);
            cargarMejoras(_solicitud.getMejoras());
            setEnabledAnnadirAnexoMejoraJButton(enMejoraDatos());
        }
        /** ALEGACION */
        if (_expediente.getAlegacion() == null){
            setVisibleAnexosAlegacionJPanel(false);
        }else{
            setVisibleAnexosAlegacionJPanel(true);
            setEnabledAbrirAnexoAlegacionJButton(false);
            setEnabledEliminarAnexoAlegacionJButton(false);
            if (_expediente.getEstado().getIdEstado() == CConstantesLicencias.EstadoEsperaAlegaciones){
                setEnEsperaAlegacion(true);
                setEnabledAnnadirAnexoAlegacionJButton(true);
            }else{
                setEnEsperaAlegacion(false);
                setEnabledAnnadirAnexoAlegacionJButton(false);
            }
            /** Los anexos de alegacion no se presentan por vu */
            cargarAnexosAlegacionJTable(_expediente.getAlegacion(), "0", getEnEsperaAlegacion());
        }
    }


     public void setBotoneraConsulta(){
         /* SOLICITUD */
        setEnabledAnnadirJButton(false);
        setEnabledEliminarJButton(false);
        if (_solicitud.getAnexos() != null){
            setEnabledAbrirJButton(true);
        }else{
            setEnabledAbrirJButton(false);
        }

        /* MEJORA */
        if (_solicitud.getMejoras() == null){
            setVisibleMejoraAnexosJPanel(false);
        }else{
            setVisibleMejoraAnexosJPanel(true);
            setEnabledAbrirAnexoMejoraJButton(true);
            setEnabledEliminarAnexoMejoraJButton(false);
            setEnabledAnnadirAnexoMejoraJButton(false);
            cargarMejoras(_solicitud.getMejoras());
        }
        /** ALEGACION */
        if (_expediente.getAlegacion() == null){
            setVisibleAnexosAlegacionJPanel(false);
        }else{
            setVisibleAnexosAlegacionJPanel(true);
            setEnabledAbrirAnexoAlegacionJButton(true);
            setEnabledEliminarAnexoAlegacionJButton(false);
            setEnabledAnnadirAnexoAlegacionJButton(false);
            /** Los anexos de alegacion no se presentan por vu */
            cargarAnexosAlegacionJTable(_expediente.getAlegacion(), "0", getEnEsperaAlegacion());
        }
    }
    public void cargarAnexosJTable(Vector vAnexosSolicitud, String vu) {
        try {
            // Annadimos a la tabla el editor ComboBox en la segunda columna
            int vColIndexCB = 1;
            TableColumn col2 = listaAnexosJTable.getColumnModel().getColumn(vColIndexCB);
            ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false));
            if (getOperacion().equalsIgnoreCase("CONSULTA")){
                comboBoxTableEditor.setEnabled(false);
            }else{
                comboBoxTableEditor.setEnabled(true);
            }
            col2.setCellEditor(comboBoxTableEditor);
            col2.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false));

            // Annadimos a la tabla el editor TextField en la tercera columna
            int vColIndexTF = 2;
            TableColumn col3 = listaAnexosJTable.getColumnModel().getColumn(vColIndexTF);
            TextFieldTableEditor textFieldTableEditor= new TextFieldTableEditor();
            if (getOperacion().equalsIgnoreCase("CONSULTA")){
                textFieldTableEditor.setEnabled(false);
            }else{
                textFieldTableEditor.setEnabled(true);
            }
            col3.setCellEditor(textFieldTableEditor);
            col3.setCellRenderer(new TextFieldRenderer());

            // Annadimos a la tabla el editor TextField en la cuarta columna
            int vColIndexTFStore = 3;
            TableColumn col4 = listaAnexosJTable.getColumnModel().getColumn(vColIndexTF);
            TextFieldTableEditor textFieldTableEditorStore= new TextFieldTableEditor();
            textFieldTableEditorStore.setEnabled(false);
            col4.setCellEditor(textFieldTableEditorStore);
            //col4.setCellRenderer(new TextFieldRenderer());
            
            if (vAnexosSolicitud != null) {
                logger.info("Anexos de la solicitud " + vAnexosSolicitud.elements());
                Enumeration en = vAnexosSolicitud.elements();
                int row = 0;
                while (en.hasMoreElements()) {
                    CAnexo anexo = (CAnexo) en.nextElement();
                    /* Insertamos en _hAnexosSolicitud */
                    /* Esta estructura nos servira para hacer las búsquedas de los anexos por nombre,
                    *  en la construccion del vector con los anexos marcados como borrados y annadidos */
                    _hAnexosSolicitud.put(anexo.getFileName(), anexo);
                    int tipo= 0;
                    if (!vu.equalsIgnoreCase("1")){
                        tipo= ((CTipoAnexo) anexo.getTipoAnexo()).getIdTipoAnexo();
                    }
                    ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) listaAnexosJTable.getCellEditor(row, 1)).getComponent();
                    combox.setSelectedPatron(new Integer(tipo).toString());

                    String store = "LocalGIS";
                    if(AlfrescoManagerUtils.isAlfrescoUuid(anexo.getIdAnexo(), String.valueOf(AppContext.getIdMunicipio()))){
                    	store = "Alfresco";
                	}
                
                    Object[] rowData = {anexo.getFileName(), combox.getSelectedItem(), anexo.getObservacion(), store};
                    _listaAnexosTableModel.addRow(rowData);
                    row++;
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }


    public void cargarAnexosAlegacionJTable(Alegacion alegacion, String vu, boolean b) {
        try {
            // Annadimos a la tabla el editor ComboBox en la segunda columna
            int vColIndexCB = 1;
            TableColumn col2 = listaAnexosAlegacionJTable.getColumnModel().getColumn(vColIndexCB);
            ComboBoxTableEditor comboBoxTableEditor= new ComboBoxTableEditor(new ComboBoxEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false));
            if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                comboBoxTableEditor.setEnabled(b);
            }else{
               comboBoxTableEditor.setEnabled(false);
            }
            col2.setCellEditor(comboBoxTableEditor);
            col2.setCellRenderer(new ComboBoxRendererEstructuras(Estructuras.getListaTiposAnexo(), null, literales.getLocale().toString(), false));

            // Annadimos a la tabla el editor TextField en la tercera columna
            int vColIndexTF = 2;
            TableColumn col3 = listaAnexosAlegacionJTable.getColumnModel().getColumn(vColIndexTF);
            TextFieldTableEditor textFieldTableEditor= new TextFieldTableEditor();
            if (!getOperacion().equalsIgnoreCase("CONSULTA")){
                textFieldTableEditor.setEnabled(b);
            }else{
                textFieldTableEditor.setEnabled(false);
            }
            col3.setCellEditor(textFieldTableEditor);
            col3.setCellRenderer(new TextFieldRenderer());

            Vector vAnexosAlegacion= alegacion.getAnexos();
            if (vAnexosAlegacion != null) {
                Enumeration en = vAnexosAlegacion.elements();
                int row = 0;
                while (en.hasMoreElements()) {
                    CAnexo anexo = (CAnexo) en.nextElement();
                    /** Insertamos en _hAnexosSolicitud */
                    /** Esta estructura nos servira para hacer las búsquedas de los anexos por nombre,
                     *  en la construccion del vector con los anexos marcados como borrados y annadidos
                     */
                    _hAnexosAlegacion.put(anexo.getFileName(), anexo);
                    int tipo= 0;
                    if (!vu.equalsIgnoreCase("1")){
                        tipo= ((CTipoAnexo) anexo.getTipoAnexo()).getIdTipoAnexo();
                    }
                    ComboBoxEstructuras combox = (ComboBoxEstructuras) ((ComboBoxTableEditor) listaAnexosAlegacionJTable.getCellEditor(row, 1)).getComponent();
                    combox.setSelectedPatron(new Integer(tipo).toString());

                    Object[] rowData = {anexo.getFileName(), combox.getSelectedItem(), anexo.getObservacion()};
                    _listaAnexosAlegacionTableModel.addRow(rowData);
                    row++;
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public Vector getAnexos(){
        // Recogemos la lista de documentos anexados a la solicitud
        Vector vListaAnexos= new Vector();
        Hashtable hAnexosAux = new Hashtable();

        int numRows = listaAnexosJTable.getRowCount();
        long totalSizeFilesUploaded= 0;

        for (int i = 0; i < numRows; i++) {
            String nomFichero = (String) listaAnexosJTable.getValueAt(i, 0);

            ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) listaAnexosJTable.getCellRenderer(i, 1);
            listaAnexosJTable.prepareRenderer(renderBox, i, 1);
            int index = new Integer(renderBox.getSelectedPatron()).intValue();

            TextFieldRenderer renderText = (TextFieldRenderer) listaAnexosJTable.getCellRenderer(i, 2);
            listaAnexosJTable.prepareRenderer(renderText, i, 2);
            String descripcion = renderText.getText();

            CTipoAnexo tipoAnexo = new CTipoAnexo(index, "", "");
            CAnexo anexo = new CAnexo(tipoAnexo, nomFichero, descripcion);
            anexo.setComesFrom(null);

            try {
                /** Lectura del contenido del anexo */
                File file = new File(nomFichero);
                if (file.isAbsolute()) {
                    /** comprobamos que no excedan el tamanno maximo permitido */
                    totalSizeFilesUploaded=+file.length();
                    if (totalSizeFilesUploaded > CConstantesLicencias.totalMaxSizeFilesUploaded){
                        JOptionPane optionPane= new JOptionPane(literales.getString("DocumentacionLicenciasJPanel.mensaje5")+": " +CConstantesLicencias.totalMaxSizeFilesUploaded+" bytes", JOptionPane.ERROR_MESSAGE);
                        JDialog dialog =optionPane.createDialog(this,"ERROR");
                        dialog.show();

                        return null;
                    }

                    //System.out.println("nomFichero="+nomFichero+" isAbsolute()");
                    nomFichero = file.getName();
                    anexo.setFileName(nomFichero);
                    /* MultipartPostMethod - en lugar de enviar el contenido, enviamos el path absoluto del fichero -*/
                    anexo.setPath(file.getAbsolutePath());
                    /**/
                }

                /** Comprobamos si se ha marcado como annadido o como borrado */
                if ((_hAnexosSolicitud.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) != null)) {
                    /** Ha sido borrado y posteriormente annadido (actualizado) */
                    //System.out.println(anexo.getFileName() + " ** Ha sido borrado y posteriormente annadido (actualizado) *");
                    anexo.setEstado(CConstantesLicencias.CMD_ANEXO_DELETED);
                    anexo.setIdAnexo(((CAnexo)(_hAnexosSolicitud.get(anexo.getFileName()))).getIdAnexo());
                    /** marcado como borrado */
                    vListaAnexos.add(anexo);
                    /** Necesario, ya que te guarda una referencia al tratarse del mismo objeto*/
                    CAnexo nuevo = new CAnexo(anexo.getTipoAnexo(), anexo.getFileName(), anexo.getObservacion());
                    nuevo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                    /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                    /*
                    byte[] content = getBytesFromFile(file);
                    nuevo.setContent(content);
                    */
                    /** marcado como annadido */
                    vListaAnexos.add(nuevo);
                    hAnexosAux.put(nuevo.getFileName(), nuevo);

                } else if ((_hAnexosSolicitud.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) != null)) {
                    /** El anexo ha sido annadido */
                    //System.out.println(anexo.getFileName() + " ** El anexo ha sido annadido *");
                    anexo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                    /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                    /*
                    byte[] content = getBytesFromFile(file);
                    anexo.setContent(content);
                    */
                    vListaAnexos.addElement(anexo);
                    hAnexosAux.put(anexo.getFileName(), anexo);

                } else if ((_hAnexosSolicitud.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) == null)) {
                    //System.out.println(anexo.getFileName() + " ** El anexo no ha sufrido cambios *");
                    /** El anexo no ha sufrido cambios. Puede que solo haya cambiado el tipo y la observacion. */
                    CAnexo existente= (CAnexo)_hAnexosSolicitud.get(anexo.getFileName());
                    anexo.setIdAnexo(existente.getIdAnexo());
                    vListaAnexos.addElement(anexo);
                    hAnexosAux.put(anexo.getFileName(), anexo);
                } else if ((_hAnexosSolicitud.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosExpediente.get(anexo.getFileName()) == null)) {
                    /** Este caso no se puede dar */
                }
            } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
            }
        }
       
        /** Comprobamos aquellos que han sido marcados como borrados */
        Vector vDeletedFirst= new Vector();
        Enumeration enumerationElement = _hAnexosSolicitud.elements();
        while (enumerationElement.hasMoreElements()) {
            CAnexo a = (CAnexo) enumerationElement.nextElement();
            if (hAnexosAux.get(a.getFileName()) == null) {
                a.setEstado(CConstantesLicencias.CMD_ANEXO_DELETED);
                vDeletedFirst.addElement(a);
            }
        }

        /* Ordenamos la lista de vectores: primero, los marcados como borrados, luego annadimos el resto. */
        for (Enumeration e= vListaAnexos.elements(); e.hasMoreElements();){
            CAnexo a= (CAnexo)e.nextElement();
            vDeletedFirst.addElement(a);
        }

        return vDeletedFirst;

    }
    
    public ArrayList<String> getAnexosUuid(){
    	ArrayList<String> anexosUuid = new ArrayList<String>();
    	Vector<CAnexo> anexos = getAnexos();
    	Iterator<CAnexo> itAnexos = anexos.iterator();
    	while(itAnexos.hasNext()){
    		anexosUuid.add(String.valueOf(itAnexos.next().getIdAnexo()));    		
    	}
    	return anexosUuid;
    }
    
    public Vector getAnexosAlegacion(){

        /* Recogemos la lista de documentos anexados a la mejora */
        Vector vListaAnexos= new Vector();
        Hashtable hAnexosAux = new Hashtable();

        int numRows = listaAnexosAlegacionJTable.getRowCount();
        long totalSizeFilesUploaded= 0;

        for (int i = 0; i < numRows; i++) {
            try{
                String nomFichero = (String) listaAnexosAlegacionJTable.getValueAt(i, 0);

                ComboBoxRendererEstructuras renderBox = (ComboBoxRendererEstructuras) listaAnexosAlegacionJTable.getCellRenderer(i, 1);
                listaAnexosAlegacionJTable.prepareRenderer(renderBox, i, 1);
                int index = new Integer(renderBox.getSelectedPatron()).intValue();

                TextFieldRenderer renderText = (TextFieldRenderer) listaAnexosAlegacionJTable.getCellRenderer(i, 2);
                listaAnexosAlegacionJTable.prepareRenderer(renderText, i, 2);
                String descripcion = renderText.getText();

                CTipoAnexo tipoAnexo = new CTipoAnexo(index, "", "");
                CAnexo anexo = new CAnexo(tipoAnexo, nomFichero, descripcion);
                anexo.setComesFrom("ALEGACION");
                /** Lectura del contenido del anexo */
                File file = new File(nomFichero);
                if (file.isAbsolute()) {
                    /** comprobamos que no excedan el tamanno maximo permitido */
                    totalSizeFilesUploaded=+file.length();
                    if (totalSizeFilesUploaded > CConstantesLicencias.totalMaxSizeFilesUploaded){
                        JOptionPane optionPane= new JOptionPane(literales.getString("DocumentacionLicenciasJPanel.mensaje5")+": " +CConstantesLicencias.totalMaxSizeFilesUploaded+" bytes", JOptionPane.ERROR_MESSAGE);
                        JDialog dialog =optionPane.createDialog(this,"ERROR");
                        dialog.show();

                        return null;
                    }

                    //System.out.println("nomFichero="+nomFichero+" isAbsolute()");
                    nomFichero = file.getName();
                    anexo.setFileName(nomFichero);
                    /* MultipartPostMethod - en lugar de enviar el contenido, enviamos el path absoluto del fichero -*/
                    anexo.setPath(file.getAbsolutePath());
                    /**/
                }

                /** Comprobamos si se ha marcado como annadido o como borrado */
                if ((_hAnexosAlegacion.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosAlegacion.get(anexo.getFileName()) != null)) {
                    /** Ha sido borrado y posteriormente annadido (actualizado) */
                    //System.out.println(anexo.getFileName() + " ** Ha sido borrado y posteriormente annadido (actualizado) *");
                    anexo.setEstado(CConstantesLicencias.CMD_ANEXO_DELETED);
                    /** marcado como borrado */
                    vListaAnexos.add(anexo);
                    /** Necesario, ya que te guarda una referencia al tratarse del mismo objeto*/
                    CAnexo nuevo = new CAnexo(anexo.getTipoAnexo(), anexo.getFileName(), anexo.getObservacion());
                    nuevo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                    nuevo.setComesFrom("ALEGACION");
                    /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                    /*
                    byte[] content = getBytesFromFile(file);
                    nuevo.setContent(content);
                    */
                    /** marcado como annadido */
                    vListaAnexos.add(nuevo);
                    hAnexosAux.put(nuevo.getFileName(), nuevo);

                } else if ((_hAnexosAlegacion.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosAlegacion.get(anexo.getFileName()) != null)) {
                    /** El anexo ha sido annadido */
                    //System.out.println(anexo.getFileName() + " ** El anexo ha sido annadido *");
                    anexo.setEstado(CConstantesLicencias.CMD_ANEXO_ADDED);
                    /** -- MultipartPostMethod: comentamos para no enviar el contenido. Enviamos el file directamente. */
                    /*
                    byte[] content = getBytesFromFile(file);
                    anexo.setContent(content);
                    */
                    vListaAnexos.addElement(anexo);
                    hAnexosAux.put(anexo.getFileName(), anexo);

                } else if ((_hAnexosAlegacion.get(anexo.getFileName()) != null) && (_hAnexosAnnadidosAlegacion.get(anexo.getFileName()) == null)) {
                    //System.out.println(anexo.getFileName() + " ** El anexo no ha sufrido cambios *");
                    /** El anexo no ha sufrido cambios. Puede que solo haya cambiado el tipo y la observacion. */
                    CAnexo existente= (CAnexo)_hAnexosAlegacion.get(anexo.getFileName());
                    anexo.setIdAnexo(existente.getIdAnexo());
                    vListaAnexos.addElement(anexo);
                    hAnexosAux.put(anexo.getFileName(), anexo);
                } else if ((_hAnexosAlegacion.get(anexo.getFileName()) == null) && (_hAnexosAnnadidosAlegacion.get(anexo.getFileName()) == null)) {
                    /** Este caso no se puede dar */
                }
            } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
            }
        }

        /** Comprobamos aquellos que han sido marcados como borrados */
        Vector vDeletedFirst= new Vector();
        Enumeration enumerationElement = _hAnexosAlegacion.elements();
        while (enumerationElement.hasMoreElements()) {
            CAnexo a = (CAnexo) enumerationElement.nextElement();
            if (hAnexosAux.get(a.getFileName()) == null) {
                a.setEstado(CConstantesLicencias.CMD_ANEXO_DELETED);
                vDeletedFirst.addElement(a);
            }
        }

        /** Ordenamos la lista de vectores: primero, los marcados como borrados, luego annadimos el resto */
        for (Enumeration e= vListaAnexos.elements(); e.hasMoreElements();){
            CAnexo a= (CAnexo)e.nextElement();
            vDeletedFirst.addElement(a);
        }

        return vDeletedFirst;
    }


    public Vector getMejoras(){
        Vector v= new Vector();
        for (int i= 0; i < mejorasJTabbedPane.getTabCount(); i++){
            MejorasJTabPanel tab= (MejorasJTabPanel)mejorasJTabbedPane.getComponentAt(i);
            Mejora mejora= tab.getMejora();
            v.add(mejora);
        }

        return v;
    }

    public void actualizarAnexos(Vector anexos){
        _hAnexosSolicitud = new Hashtable();
        if (anexos != null) {
            Enumeration en = anexos.elements();
            while (en.hasMoreElements()) {
                CAnexo anexo = (CAnexo) en.nextElement();
                /** Insertamos en _hAnexosSolicitud */
                /** Esta estructura nos servira para hacer las búsquedas de los anexos por nombre,
                 *  en la construccion del vector con los anexos marcados como borrados y annadidos
                 */
                _hAnexosSolicitud.put(anexo.getFileName(), anexo);
            }
        }

        _hAnexosAnnadidosExpediente = new Hashtable();

    }


    public void cargarMejoras(Vector vMejoras){
        if (vMejoras != null){
            int index= 0;
            for(Enumeration e= vMejoras.elements(); e.hasMoreElements();){
                Mejora mejora= (Mejora)e.nextElement();

                /** Annadimos una pestanna de mejoras */
                MejorasJTabPanel mejorasJTabPanel= new MejorasJTabPanel(this,literales);
                /** comprobamos que sea la primera para cargarla en modo edicion. Se recogen en BD por  fecha desc */
                if ((index == 0) && (enMejoraDatos())){
                    mejorasJTabPanel.setIndexTab(index);
                    mejorasJTabPanel.cargarMejora(mejora, true);
                }
                else{
                    mejorasJTabPanel.setIndexTab(index);
                    mejorasJTabPanel.cargarMejora(mejora, false);
                }
                mejorasJTabbedPane.add(mejorasJTabPanel, index);
                mejorasJTabbedPane.setTitleAt(index, literales.getString("MejorasJTabPanel.TitleTab"));
                index++;
            }
        }

    }


    public void cargarDocumentacionObligatoria(String tipo){
       
    	if(tipo.equalsIgnoreCase("0"))
    		_jPanelCheckBoxEstructura= new PanelCheckBoxEstructuras(Estructuras.getListaDocumentacionObligatoriaObraMayor(), literales.getLocale().toString(), Estructuras.getListaDocumentacionObligatoriaObraMayor().getLista().size(), 1);
    	else
    		_jPanelCheckBoxEstructura= new PanelCheckBoxEstructuras(Estructuras.getListaDocumentacionObligatoriaObraMenor(), literales.getLocale().toString(), Estructuras.getListaDocumentacionObligatoriaObraMenor().getLista().size(), 1);
    	
	        //_jPanelCheckBoxEstructuraObraMayor.setBackground(Color.WHITE);
	        _jPanelCheckBoxEstructura.setAlignmentY(TOP_ALIGNMENT);
	
	        JScrollPane documentacionJScrollPane= new JScrollPane();
	        //documentacionJScrollPane.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
	        documentacionJScrollPane.setViewportView(_jPanelCheckBoxEstructura);
	        documentacionJScrollPane.setBorder(new javax.swing.border.LineBorder(documentacionRequeridaJPanel.getBackground()));
	        /** x, y, w, h */
	        documentacionRequeridaJPanel.add(documentacionJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 500, 150));
    	
    }

    public Vector getDocumentacionObligatoriaSeleccionada(){
        return _jPanelCheckBoxEstructura.getMarcados();
    }

    public void setDocumentacionObligatoriaSeleccionada(Vector v){
        _jPanelCheckBoxEstructura.setMarcados(v);
    }

    public String getObservacionesGenerales(){
        String observaciones= observacionesTPane.getText();
        if (observaciones != null)
            return observaciones.trim();
        else return new String("");
    }

    public void setObservacionesGenerales(String observaciones){
        if (observaciones != null){
            observacionesTPane.setText(observaciones.trim());
        }else observacionesTPane.setText(new String(""));
    }

    public void setMejorasTabEditable(boolean b){

        MejorasJTabPanel tab= null;
        for (int i= 0; i < mejorasJTabbedPane.getTabCount(); i++){
            tab= (MejorasJTabPanel)mejorasJTabbedPane.getComponentAt(i);
            tab.setEnabled(false);
        }

        if (b){
            // Solo editamos la ultima pestanna
            if (tab != null){
                tab.setEnabled(b);
            }
        }
    }

    public void setEnMejoraDatos(boolean b){
        enMejoraDatos= b;
    }
    public boolean enMejoraDatos(){
        return enMejoraDatos;
    }

    public void setEnEsperaAlegacion(boolean b){
        enEsperaAlegacion= b;
    }

    public boolean getEnEsperaAlegacion(){
        return enEsperaAlegacion;
    }

    public boolean enEsperaAlegacion(){
        return enEsperaAlegacion;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void setEnabledAbrirJButton(boolean b){
        abrirJButton.setEnabled(b);
    }

    public void setEnabledAnnadirJButton(boolean b){
        annadirJButton.setEnabled(b);
    }

    public void setEnabledEliminarJButton(boolean b){
        eliminarJButton.setEnabled(b);
    }


    public void setVisibleAbrirJButton(boolean b){
        abrirJButton.setVisible(b);
    }


    public void setEnabledAbrirAnexoMejoraJButton(boolean b){
        abrirAnexoMejoraJButton.setEnabled(b);
    }

    public void setEnabledAnnadirAnexoMejoraJButton(boolean b){
        annadirAnexoMejoraJButton.setEnabled(b);
    }

    public void setEnabledEliminarAnexoMejoraJButton(boolean b){
        eliminarAnexoMejoraJButton.setEnabled(b);
    }


    public void setVisibleAbrirAnexoMejoraJButton(boolean b){
        abrirAnexoMejoraJButton.setVisible(b);
    }
    public void setVisibleEliminarAnexoMejoraJButton(boolean b){
        eliminarAnexoMejoraJButton.setVisible(b);
    }
    public void setVisibleAnnadirAnexoMejoraJButton(boolean b){
        annadirAnexoMejoraJButton.setVisible(b);
    }


    public void setEnabledAbrirAnexoAlegacionJButton(boolean b){
        abrirAnexoAlegacionJButton.setEnabled(b);
    }

    public void setEnabledAnnadirAnexoAlegacionJButton(boolean b){
        annadirAnexoAlegacionJButton.setEnabled(b);
    }

    public void setEnabledEliminarAnexoAlegacionJButton(boolean b){
        eliminarAnexoAlegacionJButton.setEnabled(b);
    }

    public void setEnabledDocumentacionRequerida(boolean b){
        _jPanelCheckBoxEstructura.setEnabled(b);
    }

    public void setEnabledObservacionesTPane(boolean b){
        observacionesTPane.setEnabled(b);
    }

    public void setVisibleAbrirAnexoAlegacionJButton(boolean b){
        abrirAnexoAlegacionJButton.setVisible(b);
    }
    public void setVisibleEliminarAnexoAlegacionJButton(boolean b){
        eliminarAnexoAlegacionJButton.setVisible(b);
    }
    public void setVisibleAnnadirAnexoAlegacionJButton(boolean b){
        annadirAnexoAlegacionJButton.setVisible(b);
    }


    public void setSolicitudLicencia(CSolicitudLicencia s){
        this._solicitud= s;
    }

    public void setExpediente(CExpedienteLicencia e){
        this._expediente= e;
    }

    public void clearDocumentacionJPanel(){
        CUtilidadesComponentes.clearTable(_listaAnexosTableModel);
        _hAnexosSolicitud= new Hashtable();
        _hAnexosAnnadidosExpediente = new Hashtable();
        _solicitud= null;
        maxSizeFilesUploaded= 0;

        observacionesTPane.setText("");
        _jPanelCheckBoxEstructura.setMarcados(new Vector());

        /** Alegaciones */
        CUtilidadesComponentes.clearTable(_listaAnexosAlegacionTableModel);
        _hAnexosAlegacion= new Hashtable();
        _hAnexosAnnadidosAlegacion= new Hashtable();

        /** Mejoras */
        mejorasJTabbedPane.removeAll();
        // creamos una mejora vacia
        //crearTabVacio();
    }

    public void setModificacion()
    {
        setOperacion("MODIFICACION");
        setVisibleAbrirJButton(true);
        setEnabledAbrirJButton(false);
        setEnabledEliminarJButton(false);
        /** Mejora */
        setVisibleMejoraAnexosJPanel(false);
        /** Alegacion */
        setVisibleAnexosAlegacionJPanel(false);
    }
    public void setConsulta()
    {
        setOperacion("CONSULTA");
        setVisibleAbrirJButton(true);
        setEnabledAbrirJButton(false);
        setEnabledEliminarJButton(false);
        setEnabledAnnadirJButton(false);
        setEnabledDocumentacionRequerida(false);
        setEnabledObservacionesTPane(false);
        /** Mejora */
        setVisibleMejoraAnexosJPanel(false);
        /** Alegacion */
        setVisibleAnexosAlegacionJPanel(false);
    }

    public void setCreacion(){
        setOperacion("CREACION");
        setVisibleAbrirJButton(true);
        setEnabledAbrirJButton(false);
        setEnabledEliminarJButton(false);
        // mejora
        setVisibleMejoraAnexosJPanel(false);
        // alegacion
        setVisibleAnexosAlegacionJPanel(false);        
    }



    public void  crearTabVacio(){
        MejorasJTabPanel mejorasJTabPanel= new MejorasJTabPanel(this, literales);
        mejorasJTabbedPane.add(mejorasJTabPanel, 0);
        mejorasJTabbedPane.setTitleAt(0, literales.getString("MejorasJTabPanel.TitleTab"));
        setMejorasTabEditable(false);
    }

    private void docManager() {    
    	if(_solicitud != null && _expediente != null){
	    	String app = getAlfrescoAppName(_solicitud.getTipoLicencia().getIdTipolicencia());	    	
	    	if(app != null){
	    		(new AlfrescoExplorer(AppContext.getApplicationContext().getMainFrame(),
						_solicitud.getIdSolicitud(), getAnexosUuid(), String.valueOf(AppContext.getIdMunicipio()), app))
						.setVisible(true);
	    		recargarAnexos();	    		
	    	}
    	}
	}
    
    /**
     * Recarga los anexos en la tabla de anexos asociados a la solicitud
     * @return Boolean: Resultado de la recarga
     */
    private Boolean recargarAnexos(){
    	try{
    		CResultadoOperacion resultadoOperacion = COperacionesLicencias.getExpedienteLicencia(_expediente.getNumExpediente(), literales.getLocale().toString(), new Vector());
    		if (resultadoOperacion.getResultado() && resultadoOperacion.getSolicitudes()!=null){
    			CSolicitudLicencia solicitudLicencia = (CSolicitudLicencia) resultadoOperacion.getSolicitudes().get(0);
	    		if(solicitudLicencia.getAnexos()!=null && solicitudLicencia.getAnexos().size()>0){
	    			CUtilidadesComponentes.clearTable(_listaAnexosTableModel);
	    			cargarAnexosJTable(solicitudLicencia.getAnexos(), _expediente.getVU());
	    			return true;
	    		}
    		}
    	}
    	catch(Exception ex){
    		logger.error(ex);    		
    	}
    	return false;
    }
    
    private static String getAlfrescoAppName(int idTipoLicencia){
    	if(idTipoLicencia==CConstantesComando.TIPO_OBRA_MAYOR){						
    		return AlfrescoConstants.APP_MAJORWORKLICENSE;
		}
		else if(idTipoLicencia==CConstantesComando.TIPO_OBRA_MENOR){						
			return AlfrescoConstants.APP_MINORWORKLICENSE;
		} 
		else if(idTipoLicencia==CConstantesComando.TIPO_ACTIVIDAD){						
			return AlfrescoConstants.APP_ACTIVITYLICENSE;
		} 	
		else if(idTipoLicencia==CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA){						
			return AlfrescoConstants.APP_NONQUALIFIEDACTIVITYLICENSE;
		} 	
		else if(idTipoLicencia==CConstantesComando.TIPO_OCUPACION){						
			return AlfrescoConstants.APP_OCUPATIONLICENSE;
		} 	
    	return null;
    }

    public void setMaxSizeFilesUploaded(long size){
        this.maxSizeFilesUploaded= size;
    }

    public long getMaxSizeFilesUploaded(){
        return maxSizeFilesUploaded;
    }

    public void setVisibleAnexosAlegacionJPanel(boolean b){
        anexosAlegacionJPanel.setVisible(b);
    }

    public void setVisibleMejoraAnexosJPanel(boolean b){
        mejoraAnexosJPanel.setVisible(b);
    }

    public void setOperacion(String op){
        this.operacion= op;
    }

    public String getOperacion(){
        return operacion;
    }


    // Variables propias
    private TextPane observacionesTPane;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrirAnexoAlegacionJButton;
    private javax.swing.JButton abrirAnexoMejoraJButton;
    private javax.swing.JButton abrirJButton;
    private javax.swing.JPanel anexosAlegacionJPanel;
    private javax.swing.JPanel anexosJPanel;
    private javax.swing.JButton annadirAnexoAlegacionJButton;
    private javax.swing.JButton annadirAnexoMejoraJButton;
    private javax.swing.JButton annadirJButton;
    private javax.swing.JPanel documentacionJPanel;
    private javax.swing.JPanel documentacionRequeridaJPanel;
    private javax.swing.JButton eliminarAnexoAlegacionJButton;
    private javax.swing.JButton eliminarAnexoMejoraJButton;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JButton alfrescoJButton;
    private javax.swing.JScrollPane listaAnexosAlegacionJScrollPane;
    private javax.swing.JTable listaAnexosAlegacionJTable;
    private javax.swing.JScrollPane listaAnexosJScrollPane;
    private javax.swing.JTable listaAnexosJTable;
    private javax.swing.JPanel mejoraAnexosJPanel;
    private javax.swing.JTabbedPane mejorasJTabbedPane;
    private javax.swing.JPanel observacionesJPanel;
    private javax.swing.JScrollPane observacionesJScrollPane;
    // End of variables declaration//GEN-END:variables

}
