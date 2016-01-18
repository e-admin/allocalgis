/**
 * CSearchJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias;


import java.awt.Cursor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.actividad.MainActividad;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CSearchDialogTableModel;
import com.geopista.app.licencias.utilidades.ColorTableCellRenderer;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;

/**
 * @author avivar
 */
public class CSearchJDialog extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(CSearchJDialog.class);
	String selectedExpediente= "";

    private JFrame desktop;
    private String selectedPatronTipoLicencia= null;
    private String selectedPatronTipoObra= null;
    private String selectedPatronEstado= null;
    private String selectedDniSolicitante= "";
    private ResourceBundle literales;
    private boolean buscarObra= false;

    TableSorted expedientesJTModelSorted= new TableSorted();
    int hiddenCol= 9;
    Vector expedientesLicencia= new Vector();

	/**
	 * Creates new form JSearch
	 */
	public CSearchJDialog(java.awt.Frame parent, boolean modal,boolean editable, ResourceBundle literales) {
		super(parent, modal);
        this.literales=literales;
        this.desktop= (JFrame)parent;
		initComponents();
		initComboBoxesEstructuras(false);
		configureComponents(editable);
		renombrarComponentes(literales);
	}

	 public CSearchJDialog(java.awt.Frame parent, boolean modal,boolean editable, String patronLicencia, String patronObra, String patronEstado, String dni, ResourceBundle literales) {
        super(parent, modal);
        this.literales=literales;
        desktop= (JFrame)parent;
        selectedPatronTipoLicencia= patronLicencia;
        selectedPatronTipoObra= patronObra;
        selectedPatronEstado= patronEstado;
        selectedDniSolicitante= dni;
        initComponents();
        initComboBoxesEstructuras(false);
        configureComponents(editable);
        renombrarComponentes(literales);
    }

    public CSearchJDialog(java.awt.Frame parent, boolean modal,boolean editable, String patronLicencia, String patronObra, String patronEstado, String dni, ResourceBundle literales, boolean buscarObra) {
       super(parent, modal);
       this.literales=literales;
       desktop= (JFrame)parent;
       this.buscarObra= buscarObra;
       selectedPatronTipoLicencia= patronLicencia;
       selectedPatronTipoObra= patronObra;
       selectedPatronEstado= patronEstado;
       selectedDniSolicitante= dni;
       initComponents();
       initComboBoxesEstructuras(buscarObra);
       configureComponents(editable);
       renombrarComponentes(literales);
   }



	public void initComboBoxesEstructuras(boolean buscarObra){
        try
        {
		    while (!Estructuras.isCargada())
		    {
			    if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			    try {Thread.sleep(500);}catch(Exception e){}
		    }

            if ((desktop instanceof CMainLicencias) || ((desktop instanceof MainActividad) && (buscarObra)))
            {
                tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaLicencias(), null, literales.getLocale().toString(), true);
                if (selectedPatronTipoLicencia != null){
                    tipoLicenciaEJCBox.setSelectedPatron(selectedPatronTipoLicencia);
                }else tipoLicenciaEJCBox.setSelectedIndex(0);

		        jPanelBusqueda.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 340, 20));
                ((JComboBox)tipoLicenciaEJCBox).addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tipoLicenciaEJCBoxActionPerformed();
                }});
                if (tipoLicenciaEJCBox.getSelectedIndex() == 0) {
                    tipoObraEJCBox= new ComboBoxEstructuras(new ListaEstructuras(), null, literales.getLocale().toString(), true);
                    tipoObraEJCBox.setSelectedIndex(0);
                    tipoObraEJCBox.setEnabled(false);
                }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMayor).toString())){
                    tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObra(), null, literales.getLocale().toString(), true);
                    if (selectedPatronTipoObra!=null)
                        tipoObraEJCBox.setSelectedPatron(selectedPatronTipoObra);
                    else
                        tipoObraEJCBox.setSelectedIndex(0);

                    tipoObraEJCBox.setEnabled(true);
                }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMenor).toString())){
                    tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObraMenor(), null, literales.getLocale().toString(), true);
                    if (selectedPatronTipoObra!=null)
                        tipoObraEJCBox.setSelectedPatron(selectedPatronTipoObra);
                    else
                        tipoObraEJCBox.setSelectedIndex(0);

                    tipoObraEJCBox.setEnabled(true);
                }
            }
            else if (desktop instanceof MainActividad)
            {
                tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposLicenciaActividad(), null, literales.getLocale().toString(), true);
                if (selectedPatronTipoObra==null)
                    tipoLicenciaEJCBox.setSelectedIndex(0);
                else
                    tipoLicenciaEJCBox.setSelectedPatron(selectedPatronTipoObra);
                tipoLicenciaEJCBox.setEnabled(true);
	            jPanelBusqueda.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 340, 20));
                tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposActividad(), null, literales.getLocale().toString(), true);
                tipoObraEJCBox.setEnabled(true);
            }

            jPanelBusqueda.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 340, 20));
            estadoEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, literales.getLocale().toString(), true);
            if (selectedPatronEstado!=null)
                estadoEJCBox.setSelectedPatron(selectedPatronEstado);
            else
                estadoEJCBox.setSelectedIndex(0);

            jPanelBusqueda.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 340, 20));
        }catch(Exception ex)
        {
            logger.error("Error al cargar las estructuras:",ex);
        }
	}


	private void configureComponents(boolean editable) {
        try
        {

            CConstantesLicencias.searchValue = "";

            expedientesJTableModel = new CSearchDialogTableModel(new String[]{literales.getString("CSearchJDialog.expedientesJTableModel.text.column1"),
                                                                        (buscarObra?literales.getString("CSearchJDialog.expedientesJTableModel.text.column10"):literales.getString("CSearchJDialog.expedientesJTableModel.text.column9")),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column2"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column3"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column4"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column5"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column6"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column7"),
                                                                        literales.getString("CSearchJDialog.expedientesJTableModel.text.column8"),
                                                                        "HIDDEN"});

            expedientesJTModelSorted= new TableSorted(expedientesJTableModel);
            expedientesJTModelSorted.setTableHeader(expedientesJTable.getTableHeader());
            expedientesJTable.setModel(expedientesJTModelSorted);
            expedientesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            expedientesJTable.setCellSelectionEnabled(false);
            expedientesJTable.setColumnSelectionAllowed(false);
            expedientesJTable.setRowSelectionAllowed(true);
            expedientesJTable.getTableHeader().setReorderingAllowed(false);

            ((TableSorted)expedientesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    reColorearBloqueo();
                }
            });
            ((TableSorted)expedientesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    reColorearBloqueo();
                }
            });
            TableColumn col= expedientesJTable.getColumnModel().getColumn(hiddenCol);
            col.setResizable(false);
            col.setWidth(0);
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setPreferredWidth(0);

            tipoLicenciaEJCBox.setEnabled(editable);

            fechaAperturaHastaJTextField.setEnabled(false);
            fechaAperturaDesdeJTextField.setEnabled(false);

            nifPropietarioJTextField.setText(selectedDniSolicitante);
        }catch(Exception e)
        {
            logger.error("Error al configurar:",e);
        }
	}

    private void reColorearBloqueo(){
          Vector redRows= new Vector();
          for (int i=0; i < expedientesJTModelSorted.getRowCount(); i++){
              int pos= ((Integer)expedientesJTModelSorted.getValueAt(i, hiddenCol)).intValue();
              CExpedienteLicencia e= (CExpedienteLicencia)expedientesLicencia.get(pos);
              if (e.bloqueado()){
                  redRows.add(new Integer(i));
              }else redRows.add(null);
          }

        /** Re-Pintamos en rojo, los expedientes que esten bloqueados */
        for (int j=0; j < expedientesJTableModel.getColumnCount(); j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= expedientesJTable.getColumnModel().getColumn(j);
            ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
            col.setCellRenderer(colorTableCellRenderer);
        }

    }

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanelBusqueda = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        buscarJButton = new javax.swing.JButton();
        nifPropietarioJTextField = new javax.swing.JTextField();
        estadoExpedienteJLabel = new javax.swing.JLabel();
        expedientesJScrollPane = new javax.swing.JScrollPane();
        expedientesJTable = new javax.swing.JTable();
        fechaAperturaDesdeJTextField = new javax.swing.JTextField();
        fechaAperturaJLabel = new javax.swing.JLabel();
        tipoLicenciaJLabel = new javax.swing.JLabel();
        nifPropietarioJLabel = new javax.swing.JLabel();
        numeroExpedienteJTextField = new javax.swing.JTextField();
        numeroExpedienteJLabel = new javax.swing.JLabel();
        fechaAperturaHastaJTextField = new javax.swing.JTextField();
        tipoObraJLabel = new javax.swing.JLabel();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        buscarFechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        buscarFechaHastaJButton = new javax.swing.JButton();
        buscarDNIJButton = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Di\u00e1logo de busqueda");
        jPanelBusqueda.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelBusqueda.setBorder(new javax.swing.border.EtchedBorder());
        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 380, -1, -1));

        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 380, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, -1, -1));

        jPanelBusqueda.add(nifPropietarioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 320, 20));

        estadoExpedienteJLabel.setText("Estado actual del expediente:");
        jPanelBusqueda.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 310, 20));

        expedientesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tipo de licencia", "N.I.F. del propietario", "Núm. expediente", "Estado actual del expediente", "Fecha de apertura", "Tipo de la vía", "Nombre de la vía", "Número de la vía"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expedientesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyReleased(evt);
            }
        });


        expedientesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                expedientesJTableFocusGained();
            }
        });
        expedientesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2){
                    aceptarJButtonActionPerformed();
                }else{
                    expedientesJTableMouseClicked();
                }
            }
        });

        expedientesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                expedientesJTableMouseDragged();
            }
        });

        expedientesJScrollPane.setViewportView(expedientesJTable);

        jPanelBusqueda.add(expedientesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 760, 180));

        jPanelBusqueda.add(fechaAperturaDesdeJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 130, 20));

        fechaAperturaJLabel.setText("Fecha de apertura (desde/hasta):");
        jPanelBusqueda.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 310, 20));

        tipoLicenciaJLabel.setText("Tipo de licencia:");
        jPanelBusqueda.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 310, 20));

        nifPropietarioJLabel.setText("N.I.F. del propietario:");
        jPanelBusqueda.add(nifPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 310, 20));

        jPanelBusqueda.add(numeroExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 340, 20));

        numeroExpedienteJLabel.setText("N\u00fam. expediente:");
        jPanelBusqueda.add(numeroExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 310, 20));

        jPanelBusqueda.add(fechaAperturaHastaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 110, 20));

        tipoObraJLabel.setText("Tipo Obra:");
        jPanelBusqueda.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 310, 20));

        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 20, 20));

        buscarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarFechaDesdeJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(buscarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, 20, 20));

        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, 20, 20));

        buscarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarFechaHastaJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(buscarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 120, 20, 20));

        buscarDNIJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNIJButtonActionPerformed();
            }
        });

        jPanelBusqueda.add(buscarDNIJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 20, 20));

        getContentPane().add(jPanelBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 410));

        pack();
    }//GEN-END:initComponents

    private void expedientesJTableMouseClicked() {//GEN-FIRST:event_expedientesJTableMouseClicked
        getExpedienteSeleccionado();
    }//GEN-LAST:event_expedientesJTableMouseClicked
    private void expedientesJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        getExpedienteSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void expedientesJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        getExpedienteSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void expedientesJTableKeyTyped(java.awt.event.KeyEvent evt) {
        getExpedienteSeleccionado();
    }
    private void expedientesJTableKeyPressed(java.awt.event.KeyEvent evt){
        getExpedienteSeleccionado();
    }
    private void expedientesJTableKeyReleased(java.awt.event.KeyEvent evt){
        getExpedienteSeleccionado();
    }

    private void buscarDNIJButtonActionPerformed() {//GEN-FIRST:event_buscarDNIJButtonActionPerformed
        CUtilidadesComponentes.showPersonaDialog(desktop, literales);

        if (CConstantesLicencias_LCGIII.persona != null) {
            nifPropietarioJTextField.setText(CConstantesLicencias_LCGIII.persona.getDniCif());
        }


    }//GEN-LAST:event_buscarDNIJButtonActionPerformed

    private void buscarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_buscarFechaHastaJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);

        if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
            fechaAperturaHastaJTextField.setText(CConstantesLicencias.calendarValue);
        }

    }//GEN-LAST:event_buscarFechaHastaJButtonActionPerformed

    private void borrarFechaHastaJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        fechaAperturaHastaJTextField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void buscarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_buscarFechaDesdeJButtonActionPerformed
        CUtilidadesComponentes.showCalendarDialog(desktop);

        if ((CConstantesLicencias.calendarValue != null) && (!CConstantesLicencias.calendarValue.trim().equals(""))) {
            fechaAperturaDesdeJTextField.setText(CConstantesLicencias.calendarValue);
        }

    }//GEN-LAST:event_buscarFechaDesdeJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed() {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        fechaAperturaDesdeJTextField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed


	private void buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed

		CUtilidadesComponentes.clearTable(expedientesJTableModel);
		selectedExpediente = "";
		Hashtable hash = new Hashtable();

        if (tipoLicenciaEJCBox.getSelectedIndex()!=0) {
            hash.put("E.ID_TIPO_LICENCIA", "" + tipoLicenciaEJCBox.getSelectedPatron().toString());
        }
        if (tipoObraEJCBox.getSelectedIndex()!=0) {
			hash.put("E.ID_TIPO_OBRA", tipoObraEJCBox.getSelectedPatron());
		}
		hash.put("F.DNI_CIF", "" + nifPropietarioJTextField.getText());
		hash.put("A.NUM_EXPEDIENTE", numeroExpedienteJTextField.getText());

        if (estadoEJCBox.getSelectedIndex()!=0) {
			hash.put("A.ID_ESTADO", "" + estadoEJCBox.getSelectedPatron() );
		}

        /** comprobamos que la fecha tiene formato valido */
        if ((CUtilidades.parseFechaStringToString(fechaAperturaDesdeJTextField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaAperturaHastaJTextField.getText()) == null)) {
            mostrarMensaje(literales.getString("CSearchJDialog.mensaje2"));
            return;
        } else {

            //Between entre fechas
            Date fechaDesde = CUtilidades.parseFechaStringToDate(fechaAperturaDesdeJTextField.getText());
            if (fechaAperturaDesdeJTextField.getText().trim().equals("")) {
                fechaDesde = new Date(1);
            }
            Date fechaHasta = CUtilidades.parseFechaStringToDate(fechaAperturaHastaJTextField.getText().trim());
            if (fechaAperturaHastaJTextField.getText().trim().equals("")) {
                fechaHasta = new Date();
            }

            if ((fechaDesde != null) && (fechaHasta != null)) {
                /** comprobamos que fechaDesde sea menor que fechaHasta */
                long millisDesde= fechaDesde.getTime();
                long millisHasta= fechaHasta.getTime();
                if (millisDesde > millisHasta){
                    mostrarMensaje(literales.getString("CSearchJDialog.mensaje1"));
                    return;
                }
                String fechaDesdeFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
                long millis = fechaHasta.getTime();
                /** annadimos un dia */
                millis += 24 * 60 * 60 * 1000;
                fechaHasta = new Date(millis);
                String fechaHastaFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

                hash.put("BETWEEN*A.FECHA_APERTURA", fechaDesdeFormatted + "*" + fechaHastaFormatted);
            }
        }

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Vector tiposLicencia= new Vector();
        if (buscarObra){
            tiposLicencia= ((IMainLicencias)desktop).getTiposLicenciaObra();
        }else{
            tiposLicencia= ((IMainLicencias)desktop).getTiposLicencia();
        }
		expedientesLicencia=enviar(hash, tiposLicencia);

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		if ((expedientesLicencia == null) || (expedientesLicencia.size() == 0)) {
			logger.info("expedientesLicencia: " + expedientesLicencia);
			JOptionPane.showMessageDialog(desktop, literales.getString("CSearchJDialog.mensaje3"));
			return;
		}
		logger.info("expedientesLicencia.size(): " + expedientesLicencia.size());

        Vector redRows= new Vector();
		for (int i = 0; i < expedientesLicencia.size(); i++) {
			CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientesLicencia.elementAt(i);
			logger.debug("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());
            /** expediente bloqueado, linea en rojo */
            if (expedienteLicencia.bloqueado()){
                redRows.add(new Integer(i));
            }else redRows.add(null);

            String tipoVia= "";
            try{
                if ((expedienteLicencia.getTipoViaAfecta() != null) && (!expedienteLicencia.getTipoViaAfecta().trim().equalsIgnoreCase(""))){
                    tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(expedienteLicencia.getTipoViaAfecta()).getTerm(literales.getLocale().toString());
                }
            }catch(Exception e){
                logger.error("[CSearchJDialog.buscarJButtonActionPerformed]expedienteLicencia.getTipoViaAfecta()="+expedienteLicencia.getTipoViaAfecta());
            }
            String patronTipoLicencia= expedienteLicencia.getTipoLicenciaDescripcion();
            String sTipoObra= "";
            String sTipoLicencia="";
            try{
                if (patronTipoLicencia != null){
                    if (patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMayor).toString())) {
                        sTipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(expedienteLicencia.getPatronTipoObraSolicitud()).toString())).getTerm(literales.getLocale().toString());
                        sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).toString())).getTerm(literales.getLocale().toString());
                    }else if (patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMenor).toString())) {
                        sTipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(expedienteLicencia.getPatronTipoObraSolicitud()).toString())).getTerm(literales.getLocale().toString());
                        sTipoLicencia=((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).toString())).getTerm(literales.getLocale().toString());
                    }else if (patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesLicencias.Actividades).toString())||patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesLicencias.ActividadesNoCalificadas).toString())) {
                        sTipoObra= ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(expedienteLicencia.getPatronTipoObraSolicitud()).toString())).getTerm(literales.getLocale().toString());
                        sTipoLicencia=((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).toString())).getTerm(literales.getLocale().toString());
                    }
                }
            }catch(Exception e){
                logger.error("ERROR. Al sacar los tipos de Obra. Obra: "+ expedienteLicencia.getPatronTipoObraSolicitud()+
                        "Tipo licencia: "+ expedienteLicencia.getTipoLicenciaDescripcion(),e);
            }




			expedientesJTableModel.addRow(new Object[]{sTipoLicencia,
                                                       sTipoObra,
													   "" + expedienteLicencia.getNifPropietario(),
													   expedienteLicencia.getNumExpediente(),
													   ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()),
													   CConstantesLicencias.df.format(expedienteLicencia.getFechaApertura()),
													   tipoVia,
													   expedienteLicencia.getNombreViaAfecta(),
													   expedienteLicencia.getNumeroViaAfecta(),
                                                       new Integer(i)});


		}
        /** Pintamos en rojo, los expedientes que esten bloqueados */
        for (int j=0; j < expedientesJTableModel.getColumnCount(); j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= expedientesJTable.getColumnModel().getColumn(j);
            ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
            col.setCellRenderer(colorTableCellRenderer);
        }

	}//GEN-LAST:event_buscarJButtonActionPerformed
	
	protected Vector enviar(Hashtable hash, Vector tiposLicencia){
		return COperacionesLicencias.getSearchedExpedientes(hash, tiposLicencia);
	}

	private void cancelarJButtonActionPerformed() {//GEN-FIRST:event_cancelarJButtonActionPerformed

		CConstantesLicencias.searchValue= "";
        /** null, cancela */
        CConstantesLicencias.patronSelectedTipoLicencia= null;
        CConstantesLicencias.patronSelectedTipoObra= null;
        CConstantesLicencias.patronSelectedEstado= null;
        CConstantesLicencias.selectedDniSolicitante= null;

        CConstantesLicencias.searchCanceled= true;

		dispose();

	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
        //getExpedienteSeleccionado();
		CConstantesLicencias.searchValue= selectedExpediente;
        CConstantesLicencias.searchCanceled= false;

        if ((selectedExpediente != null) && (selectedExpediente.trim().length() > 0)){
            CConstantesLicencias.patronSelectedTipoLicencia= tipoLicenciaEJCBox.getSelectedPatron();
            CConstantesLicencias.patronSelectedTipoObra= tipoObraEJCBox.getSelectedPatron();
            CConstantesLicencias.patronSelectedEstado= estadoEJCBox.getSelectedPatron();
            CConstantesLicencias.selectedDniSolicitante= nifPropietarioJTextField.getText();
        }else{
            CConstantesLicencias.patronSelectedTipoLicencia= null;
            CConstantesLicencias.patronSelectedTipoObra= null;
            CConstantesLicencias.patronSelectedEstado= null;
            CConstantesLicencias.selectedDniSolicitante= null;
        }

        this.dispose();

	}//GEN-LAST:event_aceptarJButtonActionPerformed


    public int getExpedienteSeleccionado(){
        int seleccion= expedientesJTable.getSelectedRow();
        if (seleccion != -1){
            String valor= (String) expedientesJTModelSorted.getValueAt(seleccion, 3);
            selectedExpediente= valor;
            numeroExpedienteJTextField.setText(valor);
        }else{
           selectedExpediente= "";
        }

        return seleccion;
    }


    private void tipoLicenciaEJCBoxActionPerformed() {

        if (tipoLicenciaEJCBox.getSelectedIndex() == 0) {
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(new ListaEstructuras(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(false);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMayor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObra(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesLicencias.ObraMenor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObraMenor(), null, literales.getLocale().toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }

    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(jPanelBusqueda, mensaje);
    }




	private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;
    private ComboBoxEstructuras estadoEJCBox;

	private CSearchDialogTableModel expedientesJTableModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton buscarDNIJButton;
    private javax.swing.JButton buscarFechaDesdeJButton;
    private javax.swing.JButton buscarFechaHastaJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JScrollPane expedientesJScrollPane;
    private javax.swing.JTable expedientesJTable;
    private javax.swing.JTextField fechaAperturaDesdeJTextField;
    private javax.swing.JTextField fechaAperturaHastaJTextField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JPanel jPanelBusqueda;
    private javax.swing.JLabel nifPropietarioJLabel;
    private javax.swing.JTextField nifPropietarioJTextField;
    private javax.swing.JLabel numeroExpedienteJLabel;
    private javax.swing.JTextField numeroExpedienteJTextField;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel tipoObraJLabel;
    //private javax.swing.JTextField jTextFieldTipoLicencia;
    // End of variables declaration//GEN-END:variables

	private void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;
       	try {

            buscarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
            buscarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaHastaJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoDeleteParcela);
            buscarDNIJButton.setIcon(com.geopista.app.licencias.CUtilidadesComponentes.iconoZoom);

			setTitle(literales.getString("CSearchJDialog.JInternalFrame.title"));

			tipoLicenciaJLabel.setText(literales.getString("CSearchJDialog.tipoLicenciaJLabel.text"));
            if (buscarObra)
                tipoObraJLabel.setText(literales.getString("CSearchJDialog.tipoObraJLabel1.text"));
            else
                tipoObraJLabel.setText(literales.getString("CSearchJDialog.tipoObraJLabel2.text"));

			nifPropietarioJLabel.setText(literales.getString("CSearchJDialog.nifPropietarioJLabel.text"));
			numeroExpedienteJLabel.setText(literales.getString("CSearchJDialog.numeroExpedienteJLabel.text"));
			estadoExpedienteJLabel.setText(literales.getString("CSearchJDialog.estadoExpedienteJLabel.text"));
			fechaAperturaJLabel.setText(literales.getString("CSearchJDialog.fechaAperturaJLabel.text"));

			buscarJButton.setText(literales.getString("CSearchJDialog.buscarJButton.text"));
			cancelarJButton.setText(literales.getString("CSearchJDialog.cancelarJButton.text"));
			aceptarJButton.setText(literales.getString("CSearchJDialog.aceptarJButton.text"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

}
