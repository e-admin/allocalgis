package com.geopista.app.licencias;
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

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.io.StringWriter;
import java.io.PrintWriter;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.licencias.tableModels.CSearchDialogTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author avivar
 */
public class CReferenciaCatastralJDialog extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(CReferenciaCatastralJDialog.class);
    private ResourceBundle literales;
    private Vector referenciasCatastrales= new Vector();
    TableSorted resultadosJTModelSorted= new TableSorted();

	/**
	 * Creates new form JSearch
	 */
	public CReferenciaCatastralJDialog(java.awt.Frame parent, boolean modal, ResourceBundle literales, int modoSeleccion) {
		super(parent, modal);
        this.literales=literales;
		initComponents();
        configureComponents(modoSeleccion);
		renombrarComponentes(literales);
	}
  
    private void configureComponents(int modoSeleccion) {
        try
        {
            resultadosJTableModel = new CSearchDialogTableModel(new String[]{literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab6"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column1"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab7"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column2"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab8"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column3"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab9"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column4"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab10"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column5"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab11"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column6"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab12"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column7"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab13"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column8"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab14"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column9"),
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab15"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column10")});

            if (modoSeleccion==-1)
                resultadosJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            else resultadosJTable.setSelectionMode(modoSeleccion);

            resultadosJTable.setRowSelectionAllowed(true);
            resultadosJTable.setColumnSelectionAllowed(false);
            resultadosJTable.getTableHeader().setReorderingAllowed(false);
            resultadosJTModelSorted= new TableSorted(resultadosJTableModel);
            resultadosJTModelSorted.setTableHeader(resultadosJTable.getTableHeader());
            resultadosJTable.setModel(resultadosJTModelSorted);

            // Tamanno para la columna de Referencia catastral
            TableColumn column = resultadosJTable.getColumnModel().getColumn(0);
            column.setMinWidth(150);
            column.setMaxWidth(300);
            column.setWidth(150);
            column.setPreferredWidth(150);

        }catch(Exception e)
        {
            logger.error("Error al configurar:",e);
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
        registroCatastralJTextField = new javax.swing.JTextField();
        registroCatastralJLabel = new javax.swing.JLabel();
        resultadosJScrollPane = new javax.swing.JScrollPane();
        resultadosJTable = new javax.swing.JTable();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Di\u00e1logo de busqueda");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar();
            }
        });

        jPanel1.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 360, -1, -1));

        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar();
            }
        });

        jPanel1.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 360, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscar();
            }
        });

        jPanel1.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, -1, -1));

        jPanel1.add(registroCatastralJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 420, -1));

        registroCatastralJLabel.setText("Referencia catastal:");
        jPanel1.add(registroCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, 20));

        resultadosJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Referencia catastral", "Tipo vía", "Nombre calle", "Número", "Letra"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });


        resultadosJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultadosJTable.setFocusCycleRoot(true);
        resultadosJTable.setSurrendersFocusOnKeystroke(true);
        resultadosJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
        });
        resultadosJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
        });

        resultadosJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
        });
        resultadosJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mostrarReferenciaCatastralSeleccionada();
            }
        });


        resultadosJScrollPane.setViewportView(resultadosJTable);

        jPanel1.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 770, 250));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 400));

        pack();
    }//GEN-END:initComponents



    private void buscar() {//GEN-FIRST:event_buscarJButtonActionPerformed

    	AppContext app =(AppContext) AppContext.getApplicationContext();
    	final JFrame desktop= (JFrame)app.getMainFrame();
    	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
    	progressDialog.addComponentListener(new ComponentAdapter()
    	{
    		public void componentShown(ComponentEvent e) 
    		{
    			new Thread(new Runnable()
    			{
    				public void run()  //throws Exception
    				{

    					try {

    						CUtilidadesComponentes.clearTable(resultadosJTableModel);
    						Hashtable hash = new Hashtable();
    						hash.put("referencia_catastral", registroCatastralJTextField.getText());
    						referenciasCatastrales= COperacionesLicencias.getSearchedReferenciasCatastrales(hash);
    						logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
    						com.geopista.app.licencias.estructuras.Estructuras.cargarLista(com.geopista.app.licencias.estructuras.Estructuras.getListaTiposViaINE(), AppContext.getApplicationContext().getString("geopista.conexion.servidorurl")+"licencias");
							ListaEstructuras lstEstructuras = Estructuras.getListaTiposViaINE();
						
    						if (referenciasCatastrales.size() > 0){
    							for (int i = 0; i < referenciasCatastrales.size(); i++) {
    								CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);

    								String tipoVia= "";
    								logger.debug("Tipo de via:" +referenciaCatastral.getTipoVia());
    								if ((referenciaCatastral.getTipoVia() != null) && (!referenciaCatastral.getTipoVia().trim().equalsIgnoreCase(""))){
    									DomainNode domainNode = lstEstructuras.getDomainNode(referenciaCatastral.getTipoVia());
    									String cadenaTermino = literales==null?((AppContext)AppContext.getApplicationContext()).getMainFrame().getLocale().toString():literales.getLocale().toString();
    									if (domainNode != null){
    										tipoVia = domainNode.getTerm(cadenaTermino);
    									}

    								}

    								//"Ref.", "Tipo vía", "Nombre vía", "Nº", "Letra","Bloque","Esc.","Planta","Puerta"
    								resultadosJTableModel.addRow(new String[]{referenciaCatastral.getReferenciaCatastral(),
    										tipoVia,
    										referenciaCatastral.getNombreVia(),
    										referenciaCatastral.getPrimerNumero(),
    										referenciaCatastral.getPrimeraLetra(),
    										referenciaCatastral.getBloque(),
    										referenciaCatastral.getEscalera(),
    										referenciaCatastral.getPlanta(),
    										referenciaCatastral.getPuerta(), referenciaCatastral.getCPostal()});
    							}
    						}else{
    							mostrarMensaje(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab16"):literales.getString("CReferenciaCatastralJDialog.mensaje1"));
    						}

    					} catch (Exception ex) {

    						StringWriter sw = new StringWriter();
    						PrintWriter pw = new PrintWriter(sw);
    						ex.printStackTrace(pw);
    						logger.error("Exception: " + sw.toString());
    					}
    					finally
    					{
    						progressDialog.setVisible(false);
    					}
    				}
    			}).start();
    		}
    	});
    	GUIUtil.centreOnWindow(progressDialog);
    	progressDialog.setVisible(true);

    }//GEN-LAST:event_buscarJButtonActionPerformed

	private void cancelar() {//GEN-FIRST:event_cancelarJButtonActionPerformed
        CConstantesLicencias.referenciasCatastrales= new Hashtable();
		dispose();

	}//GEN-LAST:event_cancelarJButtonActionPerformed

    private void mostrarReferenciaCatastralSeleccionada(){
        if(resultadosJTable.getSelectedRowCount() == 1){
            int seleccion = resultadosJTable.getSelectedRow();
            if (seleccion != -1){
                registroCatastralJTextField.setText((String) resultadosJTModelSorted.getValueAt(seleccion, 0));
            }
        }

    }

	private void aceptar() {//GEN-FIRST:event_aceptarJButtonActionPerformed

        CConstantesLicencias.referenciasCatastrales= new Hashtable();

		for (int i = 0; i < resultadosJTable.getSelectedRows().length; i++) {
			int selectedRow = resultadosJTable.getSelectedRows()[i];

			CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral();
			referenciaCatastral.setReferenciaCatastral((String) resultadosJTModelSorted.getValueAt(selectedRow, 0));
			
            try{
                referenciaCatastral.setTipoVia(Estructuras.getListaTiposViaINE().getDomainNodeByTraduccion(((String) resultadosJTModelSorted.getValueAt(selectedRow, 1))).getPatron());
            }catch (Exception e){}
			referenciaCatastral.setNombreVia((String) resultadosJTModelSorted.getValueAt(selectedRow, 2));
			referenciaCatastral.setPrimerNumero((String) resultadosJTModelSorted.getValueAt(selectedRow, 3));
			referenciaCatastral.setPrimeraLetra((String) resultadosJTModelSorted.getValueAt(selectedRow, 4));
            referenciaCatastral.setBloque((String) resultadosJTModelSorted.getValueAt(selectedRow, 5));
            referenciaCatastral.setEscalera((String) resultadosJTModelSorted.getValueAt(selectedRow, 6));
            referenciaCatastral.setPlanta((String) resultadosJTModelSorted.getValueAt(selectedRow, 7));
            referenciaCatastral.setPuerta((String) resultadosJTModelSorted.getValueAt(selectedRow, 8));
            referenciaCatastral.setCPostal((String) resultadosJTModelSorted.getValueAt(selectedRow, 9));

			CConstantesLicencias.referenciasCatastrales.put(resultadosJTModelSorted.getValueAt(selectedRow, 0),referenciaCatastral);

		}

		dispose();

	}//GEN-LAST:event_aceptarJButtonActionPerformed

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this.getParent(), mensaje);
    }



	private CSearchDialogTableModel resultadosJTableModel;
     // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel registroCatastralJLabel;
    private javax.swing.JTextField registroCatastralJTextField;
    private javax.swing.JScrollPane resultadosJScrollPane;
    private javax.swing.JTable resultadosJTable;

    // End of variables declaration//GEN-END:variables

		private void renombrarComponentes(ResourceBundle literales) {
        this.literales=literales;
		try {
			setTitle(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab1"):literales.getString("CReferenciaCatastralJDialog.JInternalFrame.title"));

			registroCatastralJLabel.setText(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab2"):literales.getString("CReferenciaCatastralJDialog.registroCatastralJLabel.text"));

			buscarJButton.setText(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab3"):literales.getString("CReferenciaCatastralJDialog.buscarJButton.text"));
			cancelarJButton.setText(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab4"):literales.getString("CReferenciaCatastralJDialog.cancelarJButton.text"));
			aceptarJButton.setText(literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab5"):literales.getString("CReferenciaCatastralJDialog.aceptarJButton.text"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


}
