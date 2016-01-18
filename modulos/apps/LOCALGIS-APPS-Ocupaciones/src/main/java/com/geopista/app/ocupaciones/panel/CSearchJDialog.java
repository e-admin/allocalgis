/**
 * CSearchJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JSearch.java
 *
 * Created on 4 de mayo de 2004, 12:04
 */

package com.geopista.app.ocupaciones.panel;

import java.awt.Cursor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.ocupaciones.CConstantesOcupaciones;
import com.geopista.app.ocupaciones.CMainOcupaciones;
import com.geopista.app.ocupaciones.CSearchDialogTableModel;
import com.geopista.app.ocupaciones.CUtilidades;
import com.geopista.app.ocupaciones.CUtilidadesComponentes;
import com.geopista.app.ocupaciones.ColorTableCellRenderer;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.ocupacion.CDatosOcupacion;

/**
 * @author avivar
 */
public class CSearchJDialog extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(CSearchJDialog.class);
	private String selectedExpediente= "";
    private boolean operacionCancelada= false;

    Vector expedientesLicencia= new Vector();
    TableSorted expedientesJTModelSorted= new TableSorted();
    int hiddenCol= 8;

    private JFrame desktop;


	/**
	 * Creates new form JSearch
	 */
	public CSearchJDialog(java.awt.Frame parent, boolean modal, boolean editable) {
		super(parent, modal);
        this.desktop= (JFrame)parent;
		initComponents();
		initComboBoxesEstructuras();
		configureComponents(editable);
		renombrarComponentes();
	}

	public CSearchJDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
        this.desktop= (JFrame)parent;
		initComponents();
		initComboBoxesEstructuras();
		configureComponents(true);
        renombrarComponentes();
	}

	public void initComboBoxesEstructuras() {
		while (!Estructuras.isCargada()) {
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}

		/** Inicializamos los comboBox que llevan estructuras */
		tipoOcupacionEJCBox = new ComboBoxEstructuras(Estructuras.getListaTipoOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
		jPanel1.add(tipoOcupacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 340, 20));

		estadoEJCBox = new ComboBoxEstructuras(Estructuras.getListaEstadosOcupacion(), null, CMainOcupaciones.currentLocale.toString(), true);
		jPanel1.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 340, 20));

	}


	private void configureComponents(boolean editable) {

		CConstantesOcupaciones.searchValue = "";

        expedientesJTableModel = new CSearchDialogTableModel(new String[]{CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column1"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column2"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column3"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column4"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column5"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column6"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column7"),
                                                                          CMainOcupaciones.literales.getString("CSearchJDialog.expedientesJTableModel.text.column8"),
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


		tipoOcupacionEJCBox.setSelectedIndex(0);
		estadoEJCBox.setSelectedIndex(0);

        fechaAperturaHastaJTextField.setEnabled(false);
        fechaAperturaDesdeJTextField.setEnabled(false);

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
        jPanel1 = new javax.swing.JPanel();
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
        borrarFechaDesdeJButton = new javax.swing.JButton();
        fechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        fechaHastaJButton = new javax.swing.JButton();
        buscarNIFJButton = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Di\u00e1logo de busqueda");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });

        jPanel1.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, 90, -1));

        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 380, 90, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, -1, -1));

        jPanel1.add(nifPropietarioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 320, 20));

        estadoExpedienteJLabel.setText("Estado actual del expediente:");
        jPanel1.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 310, 20));

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

        jPanel1.add(expedientesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 760, 180));

        fechaAperturaDesdeJTextField.setEnabled(false);
        jPanel1.add(fechaAperturaDesdeJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 90, 20));

        fechaAperturaJLabel.setText("Fecha de apertura (desde/hasta):");
        jPanel1.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 310, 20));

        tipoLicenciaJLabel.setText("Tipo de licencia:");
        jPanel1.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 310, 20));

        nifPropietarioJLabel.setText("N.I.F. del propietario:");
        jPanel1.add(nifPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 310, 20));

        jPanel1.add(numeroExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 340, 20));

        numeroExpedienteJLabel.setText("N\u00fam. expediente:");
        jPanel1.add(numeroExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 310, 20));

        fechaAperturaHastaJTextField.setEnabled(false);
        jPanel1.add(fechaAperturaHastaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 90, 20));

        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 20, 20));

        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 20, 20));

        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 20, 20));

        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 20, 20));

        buscarNIFJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarNIFJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(buscarNIFJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 20, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 410));

        pack();
    }//GEN-END:initComponents

    private void buscarNIFJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarNIFJButtonActionPerformed
        // TODO add your handling code here:
        CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        CPersonaJuridicoFisica persona = dialog.getPersona();
        if ((persona != null) && (persona.getDniCif() != null)) {
            nifPropietarioJTextField.setText(persona.getDniCif());
        }

    }//GEN-LAST:event_buscarNIFJButtonActionPerformed

    private void fechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
        // TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        String fecha= dialog.getFechaSelected();
        if ((fecha != null) && (!fecha.trim().equals(""))) {
            fechaAperturaHastaJTextField.setText(fecha);
        }

    }//GEN-LAST:event_fechaHastaJButtonActionPerformed

    private void borrarFechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        // TODO add your handling code here:
        fechaAperturaHastaJTextField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void fechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
        // TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        String fecha= dialog.getFechaSelected();
        if ((fecha != null) && (!fecha.trim().equals(""))) {
            fechaAperturaDesdeJTextField.setText(fecha);
        }

    }//GEN-LAST:event_fechaDesdeJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        // TODO add your handling code here:
        fechaAperturaDesdeJTextField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void getExpedienteSeleccionado(){
        int seleccion = expedientesJTable.getSelectedRow();
        if (seleccion != -1){
            selectedExpediente= (String) expedientesJTModelSorted.getValueAt(seleccion, 2);
            numeroExpedienteJTextField.setText(selectedExpediente);
        }
    }

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


    public String getSelectedExpediente(){
       return selectedExpediente;
    }

	private void buscarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarJButtonActionPerformed

		CUtilidadesComponentes.clearTable(expedientesJTableModel);
		selectedExpediente = "";
		Hashtable hash = new Hashtable();

		if (tipoOcupacionEJCBox.getSelectedIndex() != 0) {
			hash.put("H.TIPO_OCUPACION", "" + tipoOcupacionEJCBox.getSelectedPatron().toString());
		}
		hash.put("F.DNI_CIF", "" + nifPropietarioJTextField.getText());
		hash.put("A.NUM_EXPEDIENTE", numeroExpedienteJTextField.getText());
		if (estadoEJCBox.getSelectedIndex() != 0) {
			hash.put("A.ID_ESTADO", "" + estadoEJCBox.getSelectedPatron());
		}
        
        /** comprobamos que la fecha tiene formato valido */
        if ((CUtilidades.parseFechaStringToString(fechaAperturaDesdeJTextField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaAperturaHastaJTextField.getText()) == null)) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CSearchJDialog.mensaje2"));
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
                    mostrarMensaje(CMainOcupaciones.literales.getString("CSearchJDialog.mensaje1"));
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

        expedientesLicencia = COperacionesLicencias.getSearchedExpedientes(hash, null);

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


		if ((expedientesLicencia == null) || (expedientesLicencia.size() == 0)) {
			JOptionPane.showMessageDialog(desktop, "Expediente no encontrado.");
			return;
		}


		logger.info("expedientesLicencia.size(): " + expedientesLicencia.size());

        Vector redRows= new Vector();
		for (int i = 0; i < expedientesLicencia.size(); i++) {
			CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientesLicencia.elementAt(i);
			CDatosOcupacion datosOcupacion=expedienteLicencia.getDatosOcupacion();
			logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());

            /** expediente bloqueado, linea en rojo */
            if (expedienteLicencia.bloqueado()){
                redRows.add(new Integer(i));
            }else redRows.add(null);

            String sEstado="";
            try{sEstado=((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());}catch(Exception e){logger.error("Error al cargar el estado: "+expedienteLicencia.getEstado().getIdEstado(),e);};

            String tipoVia= "";
            try{
                if ((expedienteLicencia.getTipoViaAfecta() != null) && (!expedienteLicencia.getTipoViaAfecta().trim().equalsIgnoreCase(""))){
                    tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(expedienteLicencia.getTipoViaAfecta()).getTerm(CMainOcupaciones.currentLocale.toString());
                }
            }catch(Exception e){
                logger.error("ERROR. No encientra el tipoVia="+expedienteLicencia.getTipoViaAfecta());
            }

			expedientesJTableModel.addRow(new Object[]{((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(datosOcupacion.getTipoOcupacion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
													   "" + expedienteLicencia.getNifPropietario(),
													   expedienteLicencia.getNumExpediente(),
													   sEstado,
													   CConstantesOcupaciones.df.format(expedienteLicencia.getFechaApertura()),
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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(jPanel1, mensaje);
    }


	private void cancelarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarJButtonActionPerformed
		selectedExpediente= "";
        setOperacionCancelada(true);
		dispose();

	}//GEN-LAST:event_cancelarJButtonActionPerformed

    private void aceptarJButtonActionPerformed() {//GEN-FIRST:event_aceptarJButtonActionPerformed
        getExpedienteSeleccionado();
        setOperacionCancelada(false);
		dispose();
	}//GEN-LAST:event_aceptarJButtonActionPerformed

    public boolean getOperacionCancelada(){
        return operacionCancelada;
    }

    public void setOperacionCancelada(boolean b){
        this.operacionCancelada= b;
    }

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new CSearchJDialog(new javax.swing.JFrame(), true).show();
	}

	private ComboBoxEstructuras tipoOcupacionEJCBox;
	private ComboBoxEstructuras estadoEJCBox;

	private DefaultTableModel expedientesJTableModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton buscarNIFJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JScrollPane expedientesJScrollPane;
    private javax.swing.JTable expedientesJTable;
    private javax.swing.JTextField fechaAperturaDesdeJTextField;
    private javax.swing.JTextField fechaAperturaHastaJTextField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nifPropietarioJLabel;
    private javax.swing.JTextField nifPropietarioJTextField;
    private javax.swing.JLabel numeroExpedienteJLabel;
    private javax.swing.JTextField numeroExpedienteJTextField;
    private javax.swing.JLabel tipoLicenciaJLabel;
    // End of variables declaration//GEN-END:variables

	private void renombrarComponentes() {

		try {

            fechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);
            fechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            buscarNIFJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);

			setTitle(CMainOcupaciones.literales.getString("CSearchJDialog.JInternalFrame.title"));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CSearchJDialog.tipoLicenciaJLabel.text"));
			nifPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CSearchJDialog.nifPropietarioJLabel.text"));
			numeroExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CSearchJDialog.numeroExpedienteJLabel.text"));
			estadoExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CSearchJDialog.estadoExpedienteJLabel.text"));
			fechaAperturaJLabel.setText(CMainOcupaciones.literales.getString("CSearchJDialog.fechaAperturaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CSearchJDialog.buscarJButton.text"));
			cancelarJButton.setText(CMainOcupaciones.literales.getString("CSearchJDialog.cancelarJButton.text"));
			aceptarJButton.setText(CMainOcupaciones.literales.getString("CSearchJDialog.aceptarJButton.text"));


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

}
