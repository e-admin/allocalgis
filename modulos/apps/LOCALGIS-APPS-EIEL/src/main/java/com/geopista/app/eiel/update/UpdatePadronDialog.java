/**
 * UpdatePadronDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.update;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.utilidades.ProcessCancel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.UpdateTestCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class UpdatePadronDialog extends JDialog {

	static Logger logger = Logger.getLogger(UpdatePadronDialog.class);
	private UpdatePadronSelectionPanel updatePadronSelectionPanel = null;
	private UpdateTestCancelPanel updateTestCancelButton = null;
	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();

	public static final int DIM_X = 250;
	public static final int DIM_Y = 250;

	private UpdateTestCancelPanel getOkCancelPanel() {
		if (updateTestCancelButton == null) {
			updateTestCancelButton = new UpdateTestCancelPanel();
			updateTestCancelButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (updateTestCancelButton.wasUpdatePressed() ||
									updateTestCancelButton.wasTestPressed()) {

								final boolean actualizar;
								if (updateTestCancelButton.wasTestPressed())
									actualizar=false;
								else
									actualizar=true;
								
								if (actualizar){
									if (!mensajeConfirmacion()){
										dispose();
										return;
									}
								}

								
								final String añoPadron=updatePadronSelectionPanel.getAñoPadron();			
								
								final LCGMunicipioEIEL municipioSeleccionado=updatePadronSelectionPanel.getMunicipioSeleccionado();

								updatePadronSelectionPanel.setVisible(false);
								
								if (updatePadronSelectionPanel.abrirJFileChooser() == JFileChooser.APPROVE_OPTION){

									final String path=updatePadronSelectionPanel.getDirectorySelected();
									if(path!=null && !path.equals("")){
									
										final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
												AppContext.getApplicationContext().getMainFrame(), null);
										progressDialog.setTitle("TaskMonitorDialog.Wait");
										progressDialog.addComponentListener(new ComponentAdapter() {
													public void componentShown(
															ComponentEvent e) {
														new Thread(new Runnable() {
															public void run() {
		
																try {
																	
																	progressDialog.report(I18N.get("LocalGISUpdatePadron", "update.padron.actualizando"));  
																	
																	ProcessCancel processCancel=null;			
											        				if (progressDialog!=null){				
											        					if (progressDialog!=null){
											        						processCancel=new ProcessCancel(progressDialog);
											        						processCancel.start();
											        					}
											        				}
											        				dispose();
											        				
											        				
																	ActualizarPadron padron=new ActualizarPadron(añoPadron,municipioSeleccionado,actualizar,path,progressDialog);
																	ResultadoActualizacion resultadoActualizacion=padron.actualizar();
																	
																	if (resultadoActualizacion!=null){
																		
																		if (actualizar)
																			JOptionPane.showMessageDialog(
																					UpdatePadronDialog.this,
													                                "Actualizacion realizada correctamente. \n"+
																					"Puede revisar el fichero de resultados: Padron_"+añoPadron+".log",
													                                null,
													                                JOptionPane.INFORMATION_MESSAGE);
																		else
																			JOptionPane.showMessageDialog(
																					UpdatePadronDialog.this,
													                                "Test de actualizacion realizado correctamente. \n" +
													                                "Puede revisar el fichero de resultados: PadronTest_"+añoPadron+".log",														                                												                                null,
													                                JOptionPane.INFORMATION_MESSAGE);
																	}
																	else{
																		if (actualizar)
																			JOptionPane.showMessageDialog(
																					UpdatePadronDialog.this,
													                                "Error al realizar la actualizacion.\n"+
																					"Puede revisar el fichero de resultados: Padron_"+añoPadron+".log",
													                                null,
													                                JOptionPane.INFORMATION_MESSAGE);
																		else
																			JOptionPane.showMessageDialog(
																					UpdatePadronDialog.this,
													                                "Error al realizar la actualizacion.\n"+
																					"Puede revisar el fichero de resultados: PadronTest_"+añoPadron+".log",
													                                null,
													                                JOptionPane.INFORMATION_MESSAGE);
																	}
																	
		
																} catch (Exception e) {
																	logger.error("Error ", e);
																	ErrorDialog.show(AppContext.getApplicationContext()
																					.getMainFrame(),"ERROR","ERROR",
																					StringUtil.stackTrace(e));
																	return;
																} finally {
																	progressDialog.setVisible(false);
																	progressDialog.dispose();
																	dispose();
																}
															}
														}).start();
													}
												});
										GUIUtil.centreOnWindow(progressDialog);
										progressDialog.setVisible(true);
		
										show();
									}
									else{
				                		JOptionPane.showMessageDialog(
				                				UpdatePadronDialog.this,
				                                I18N.get("LocalGISUpdatePadron", "update.padron.directorio.incorrecto"),
				                                null,
				                                JOptionPane.INFORMATION_MESSAGE);
				                	
				                	}
								}
								else{
									dispose();
								}
							}
							else{
								dispose();
							}

						}
					});

		}// ok
		return updateTestCancelButton;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean mensajeConfirmacion(){
		String mensaje = I18N.get("LocalGISUpdatePadron", "update.padron.confirmar");
		String string1 = aplicacion.getI18nString("general.si"); 
        String string2 = aplicacion.getI18nString("general.no"); 
         Object[] options = {string1, string2};
         
         int n = JOptionPane.showOptionDialog(UpdatePadronDialog.this,
                mensaje.toString(),
               "Actualizacion Padron",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            
         if (n==JOptionPane.NO_OPTION){
        	 return false;
         }
         else
        	 return true;
	}

	/* CONSTRUCTORES */

	public UpdatePadronDialog() {
		super(AppContext.getApplicationContext().getMainFrame());
		initialize();
		this.setLocationRelativeTo(null);

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle
				.getBundle(
						"com.geopista.app.eiel.update.language.LocalGISUpdatePadroni18n",
						loc, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISUpdatePadron", bundle);

		String title = I18N.get("LocalGISUpdatePadron", "update.padron.title");
		this.setModal(false);
		this.setSize(DIM_X, DIM_Y);
		this.setMinimumSize(new Dimension(DIM_X, DIM_Y));

		this.setResizable(true);
		this.setLayout(new GridBagLayout());
		this.add(getUpdatePadronPanel(), new GridBagConstraints(0, 0, 1, 1, 1,
				1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getOkCancelPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0001,
				0.0001, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle(title);
		this.getOkCancelPanel().setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				dispose();
			}
		});
	}

	public UpdatePadronSelectionPanel getUpdatePadronPanel() {
		if (updatePadronSelectionPanel == null) {
			updatePadronSelectionPanel = new UpdatePadronSelectionPanel(
					new GridBagLayout());
		}
		return updatePadronSelectionPanel;
	}

	public void setUpdatePadronSelectionPanel(
			UpdatePadronSelectionPanel updatePadronSelectionPanel) {
		this.updatePadronSelectionPanel = updatePadronSelectionPanel;
	}

}
