/**
 * ImprimirInformeDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.dialogs;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.PostalDataOT;
import com.localgis.app.gestionciudad.dialogs.interventions.panels.InformeObraCiudadanoPrintable;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class ImprimirInformeDialog extends JDialog {

	private static final long serialVersionUID = -8838279439039379335L;

	private InformeObraCiudadanoPrintable printablePanel = null;
	private JPanel botoneraAndInfoPanel = null;
	private OKCancelPanel okCancelPanel = null;

	private String actuaction = null;
	private String intervention = null;
	private ArrayList<String> calles = null;
	private ArrayList<PostalDataOT> postalData = null;

	public ImprimirInformeDialog(Frame parentFrame, String tittle, boolean modal,
			String intervention, String actuation, ArrayList<String> calles, ArrayList<PostalDataOT> ciudadanos){
		super(parentFrame, tittle, modal);

		this.intervention = intervention;
		this.actuaction = actuation;
		this.calles = calles;
		this.postalData =ciudadanos ;
		this.initialize();

		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


	JScrollPane scrollPane = new JScrollPane();

	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());



		scrollPane.setViewportView(getInformeObraPrintablePanel());

		this.add(scrollPane, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 200));


		this.add(getPrintRendererPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(getOkCancelPanel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
	}


	private InformeObraCiudadanoPrintable getInformeObraPrintablePanel() {
		if (printablePanel == null){
			printablePanel = new InformeObraCiudadanoPrintable(
					this.actuaction,
					this.intervention,
					this.calles);
		}
		return printablePanel;
	}


	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();

			this.okCancelPanel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		return true;
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}


	private JButton imprimirButton = null;
	private JButton cancelPrintButton = null;
	private boolean cancelPrint = false;
	private Thread task = null;
	
	private JButton getImprimirButton(){
		if (imprimirButton == null){
			imprimirButton = new JButton("Imprimir");
			
			imprimirButton.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					task = new Thread( new Runnable() { 
						public void run() {
							try {
								getImprimirButton().setEnabled(false);
								getCancelPrintButton().setEnabled(true);
								if (postalData!=null && !postalData.isEmpty()){
									PrinterJob job = PrinterJob.getPrinterJob();
									job.setPrintable(getInformeObraPrintablePanel());
									boolean aceptar_impresion =job.printDialog();
									if(aceptar_impresion){
										getInformeObraPrintablePanel().prepareToPrint();
										Iterator<PostalDataOT> it = postalData.iterator();
										inicializarBarraDeProgreso(0, postalData.size());
										int i = 0;
										while (it.hasNext() && !cancelPrint){
											getInformeObraPrintablePanel().loadPostalData(it.next());
											i++;
											job.print();
											moveProgressbarFordward(i, postalData.size());
											getBarraCiudadanosProgreso().repaint();
										}
										getInformeObraPrintablePanel().finishPrint();
										getImprimirButton().setEnabled(true);
										getCancelPrintButton().setEnabled(false);
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								getInformeObraPrintablePanel().finishPrint();
								getImprimirButton().setEnabled(true);
								getCancelPrintButton().setEnabled(false);
							}
						}
					}); 
					
					int seleccion = JOptionPane.showConfirmDialog(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.dialog.confirmation.num.prints").replaceFirst("NUMCARTAS", Integer.toString(postalData.size())));
					if (seleccion == 0){
						task.start(); 
					}
				}
			});
		}
		return imprimirButton;
	}



	public void inicializarBarraDeProgreso(int initialValue, int maxValue){
		this.getBarraCiudadanosProgreso().setMinimum(initialValue);
		this.getBarraCiudadanosProgreso().setMaximum(maxValue);
		this.getBarraCiudadanosProgreso().setValue(initialValue);
	}

	private void moveProgressbarFordward(int actual, int ofMax){
		this.getBarraCiudadanosProgreso().setValue(actual);
		this.getBarraCiudadanosProgreso().setString(
				actual + 
				" de " +
				getPrecentageProgressBar()
		);

		this.getPrintRendererLabel().setText(actual + " de " + ofMax);
	}

	private String getPrecentageProgressBar(){
		String resultado = "";
		if (this.getBarraCiudadanosProgreso() != null){
			try{
				resultado = "(" +  (new DecimalFormat("0.0")).format(getBarraCiudadanosProgreso().getPercentComplete()*100) + "%)";
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}

	private JButton getCancelPrintButton(){
		if (cancelPrintButton == null){
			cancelPrintButton = new JButton("Cancelar Impresion");
			cancelPrintButton.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onCancelPrintButtonDo();
				}
			});

			cancelPrintButton.setEnabled(false);
		}
		return cancelPrintButton;
	}

	private void onCancelPrintButtonDo() {
		int seleccion = JOptionPane.showConfirmDialog(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.dialog.confirmation.cancel.print"));
		if (seleccion == 0){
			this.cancelPrint = true;
			this.getInformeObraPrintablePanel().finishPrint();
		}
	}


	private JPanel printRendererPanel = null;
	private JLabel printRendererLabel = null;


	private JPanel getPrintRendererPanel(){
		if (printRendererPanel == null){
			printRendererPanel = new JPanel(new GridBagLayout());

			printRendererPanel.add(getPrintRendererLabel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			printRendererPanel.add(getBarraCiudadanosProgreso(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			JPanel printButtonsPanel = new JPanel();
			printButtonsPanel.add(getImprimirButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			printButtonsPanel.add(getCancelPrintButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			printRendererPanel.add(printButtonsPanel, 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));


		}
		return printRendererPanel;
	}

	private JLabel getPrintRendererLabel(){
		if (printRendererLabel == null){
			printRendererLabel = new JLabel(" ");
		}
		return printRendererLabel;
	}

	private JProgressBar barraCiudadanosProgreso =null;

	private JProgressBar getBarraCiudadanosProgreso(){
		if (barraCiudadanosProgreso == null){
			barraCiudadanosProgreso = new JProgressBar();
			barraCiudadanosProgreso.setSize(0, 1);
			barraCiudadanosProgreso.setStringPainted(true);
			barraCiudadanosProgreso.setValue(0);
			barraCiudadanosProgreso.setMinimum(0);
			barraCiudadanosProgreso.setMaximum(100);


			barraCiudadanosProgreso.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					int percentage = (int) (getBarraCiudadanosProgreso().getPercentComplete()*100); 
					getBarraCiudadanosProgreso().setString(percentage + "%");
				}
			});


		}
		return barraCiudadanosProgreso;
	}




}



