package com.geopista.app.inventario.panel;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.io.StringWriter;
import java.io.PrintWriter;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.licencias.CReferenciaCatastral;

import com.geopista.app.inventario.Constantes;
import com.geopista.app.licencias.CUtilidadesComponentes_LCGIII;
import com.geopista.app.licencias.tableModels.CSearchDialogTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author avivar
 */
public class CReferenciaCatastralInventarioJDialog extends javax.swing.JDialog {
	
	Logger logger = Logger.getLogger(CReferenciaCatastralInventarioJDialog.class);
    private ResourceBundle literales;
    private ArrayList referenciasCatastrales= new ArrayList();
    private CReferenciaCatastral refCatastral = null;
    TableSorted resultadosJTModelSorted= new TableSorted();
    private InventarioClient inventarioClient = null;

	/**
	 * Creates new form JSearch
	 */
	public CReferenciaCatastralInventarioJDialog(java.awt.Frame parent, boolean modal, ResourceBundle literales, int modoSeleccion) {
		
		super(parent, modal);
        this.literales=literales;
        inventarioClient= new InventarioClient(AppContext.getApplicationContext().getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);
		initComponents();
        configureComponents(modoSeleccion);
		renombrarComponentes(literales);
	}
	
	private JTable jTableReferenciasCatastrales = null;
	private TableReferenciasCatastralesModel tableReferenciasCatastralesModel;
	
	private JTable getJTableRefrenciaCatastral()
    {
        if (jTableReferenciasCatastrales  == null)
        {
        	jTableReferenciasCatastrales = new JTable();

        	tableReferenciasCatastralesModel = new TableReferenciasCatastralesModel();
            
            TableSorted tblSorted= new TableSorted(tableReferenciasCatastralesModel);
            tblSorted.setTableHeader(jTableReferenciasCatastrales.getTableHeader());
            jTableReferenciasCatastrales.setModel(tblSorted);
            jTableReferenciasCatastrales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableReferenciasCatastrales.setCellSelectionEnabled(false);
            jTableReferenciasCatastrales.setColumnSelectionAllowed(false);
            jTableReferenciasCatastrales.setRowSelectionAllowed(true);
            jTableReferenciasCatastrales.getTableHeader().setReorderingAllowed(false);
            
            jTableReferenciasCatastrales.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            jTableReferenciasCatastrales.setFocusCycleRoot(true);
            jTableReferenciasCatastrales.setSurrendersFocusOnKeystroke(true);
            jTableReferenciasCatastrales.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    mostrarReferenciaCatastralSeleccionada();
                }
            });
            jTableReferenciasCatastrales.addKeyListener(new java.awt.event.KeyAdapter() {
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

            jTableReferenciasCatastrales.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mostrarReferenciaCatastralSeleccionada();
                }
            });
            jTableReferenciasCatastrales.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    mostrarReferenciaCatastralSeleccionada();
                }
            });
                        
        }
        return jTableReferenciasCatastrales;
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
                                                                         literales==null?((AppContext)AppContext.getApplicationContext()).getI18nString("inventario.referenciasDialog.tab15"):literales.getString("CReferenciaCatastralJDialog.resultadosJTableModel.text.column10")});
            
            resultadosJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        resultadosJScrollPane.setViewportView(getJTableRefrenciaCatastral());

        jPanel1.add(resultadosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 770, 250));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 400));

        pack();
    }//GEN-END:initComponents

    public void loadTableRefrenciasCatastrales(ArrayList lstRef)
    {
        ((TableReferenciasCatastralesModel)((TableSorted)getJTableRefrenciaCatastral().getModel()).
                getTableModel()).setData(lstRef);     
    }

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

    						CUtilidadesComponentes_LCGIII.clearTable(resultadosJTableModel);

    						String patron = registroCatastralJTextField.getText();
    						
    						referenciasCatastrales= (ArrayList) inventarioClient.getReferenciasCatastrales(Const.ACTION_GET_REFERENCIAS_CATASTRALES, patron);

    						if (referenciasCatastrales!= null && referenciasCatastrales.size() > 0){
    							
    							Estructuras.cargarEstructura("Tipo de vía");
								ListaEstructuras lstEstructuras = Estructuras.getListaTipos();
								
								for (Iterator iter = referenciasCatastrales.iterator(); iter.hasNext();) {

									CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) iter.next();

									if (referenciaCatastral != null){

										String tipoVia= "";
										if ((referenciaCatastral.getTipoVia() != null) && (!referenciaCatastral.getTipoVia().trim().equalsIgnoreCase(""))){

											if (lstEstructuras != null){
												DomainNode domainNode = lstEstructuras.getDomainNode(referenciaCatastral.getTipoVia());
												String cadenaTermino = literales==null?((AppContext)AppContext.getApplicationContext()).getMainFrame().getLocale().toString():literales.getLocale().toString();
												if (domainNode != null){
													tipoVia = domainNode.getTerm(cadenaTermino);
												}
											}

										}

									}
								}
								
								loadTableRefrenciasCatastrales(referenciasCatastrales);
								resultadosJScrollPane.repaint();
								resultadosJScrollPane.updateUI();
								
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
        refCatastral= null;
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

		int selectedRow = getJTableRefrenciaCatastral().getSelectedRow();
		if (selectedRow > 0){

			CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral();
			if (tableReferenciasCatastralesModel != null){
				referenciaCatastral = tableReferenciasCatastralesModel.getValueAt(selectedRow);
			}
	
			refCatastral = referenciaCatastral;

		}

		dispose();

	}

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
		
		public CReferenciaCatastral getReferenciasCatastrales(){
			return refCatastral;
		}


}
