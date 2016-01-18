/**
 * RegimenesCondicionUrbanisticaAsociacionDeterminacionEntidadDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaRegimenes;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.EdicionUtils;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class RegimenesCondicionUrbanisticaAsociacionDeterminacionEntidadDialog extends JDialog{


	 private ApplicationContext application;
	 
	 private TablaRegimenes tablaRegimenes;

	 private JButton cancelarJButton;
	// private JButton aceptarJButton;
	 
	 private JPanel casoPanel;
	 private JLabel nombreCasoJLabel;
	 private JTextField nombreCasoJTextField;
	 
	 private JPanel regimenesPanel;

	private JPanel asignacionPanel;
	 private JPanel seleccionTipoPanel;
	 private JPanel seleccionValorPanel;
	 private JPanel seleccionValorReferenciaPanel;

	 private JPanel comentarioJPanel;
	 private JPanel regimenEspecificoJPanel;
	 private JPanel determinacionRegimenJPanel;
	 private JPanel regimenJPanel;
	 
	 
	 private JPanel datosRegimenJPanel;
	 private JPanel botoneraAnadirRegimenJPanel;
	 private JPanel tablaRegimenesJPanel;
	 private JButton anadirRegimenJButton;
	 private JButton eliminarRegimeJButton;
	 private JButton modificarRegimeJButton;

	 private JPanel botoneraPanel;
	 
	 private JCheckBox seleccionValorJCheckBox;
	 private JCheckBox seleccionValorReferenciaJCheckBox;
	 
	 private JComboBox valorReferenciaJComboBox;
	 private JTextField valorJTextField;

	 private JLabel seleccioneTipoJLabel;
	 	 
	 private DeterminacionPanelBean[] lstDeterminaciones;
	 private EntidadPanelBean[] lstEntidades;
	 
	 private FipPanelBean fip;
	 private DeterminacionBean[] lstDeterminacionesValorReferencia;
	 private DeterminacionBean[] lstDeterminacionesRegimenDeActos;
	 private DeterminacionBean[] lstDeterminacionesRegimendeUso;
	 
	 private DeterminacionBean[] lstDeterminacionesValorReferenciaAux;
	 
	 //contienes todas las determiacion que son de tipoi Regimen de Uso y Normas de uso
	 // para mostrarlas en el combo Determinacion Regimen.
	 private DeterminacionBean[] lstDeterminacionesRegimendeUsoYNormasUso;
	 
	 private JTextArea comentarioJTextArea;
	 private JScrollPane comentarioPScroll;
	 
	 private JLabel textoRegimenEspecificoJLabel;
	 private JTextArea textoRegimenEspecificoJTextArea;
	 private JScrollPane textoRegimenEspecificoPScroll;
	 private JTextField ordenRegimenEspecificoJTextField;
	 private JLabel ordenRegimenEspecificoCasoJLabel;
	 private JTextField nombreRegimenEspecificoJTextField;
	 private JLabel nombreRegimenEspecificoJLabel;
	 
	 private JTextField superposicionJTextField;
	 private JLabel superposicionJLabel;
	 
	 private JComboBox determinacinRegimenJComboBox;
 

	 private int indiceRegimen = 0;
	 private ArrayList lstCUCR = new ArrayList();
	 private ArrayList lstCUC = new ArrayList();
	 
	 private int valorSecuenciaCUCR = 0 ;
	 //private boolean estadoConsulta = false;


	public RegimenesCondicionUrbanisticaAsociacionDeterminacionEntidadDialog(FipPanelBean fip,
												DeterminacionPanelBean[] lstDeterminaciones, 
												 EntidadPanelBean[] lstEntidades,
												 ArrayList lstCUC,
												 ApplicationContext application){
		 
		 super(application.getMainFrame());

		 this.application = application;
		 this.lstDeterminaciones = lstDeterminaciones;
		 this.lstEntidades = lstEntidades;
		 this.fip = fip;
		 this.lstCUC = lstCUC;
		 this.valorSecuenciaCUCR = 0;
		 
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false, this.application.getMainFrame());
		
	    inicializaElementos();

	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	 
	 
	 
	 /**
		* Inicializa los elementos del panel.
		*/
		private void inicializaElementos(){
			this.setModal(true);
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			int height = (int)screenSize.getHeight();
			int width = (int)screenSize.getWidth();
			this.setSize(width - width / 10, height - height / 10);
			this.setLocationRelativeTo(application.getMainFrame());
			
			this.setTitle(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						"gestorFip.asociaciondeterminacionesentidades.regimenes.title"));
			

			try {
				 lstDeterminacionesValorReferencia = ClientGestorFip.obtenerDeterminacionesByTipoCaracterValorReferencia_EnCondicionesUrbanisticas(
						 			fip, application, ConstantesGestorFIP.CARATERDETERMINACION_VALORREFERENCIA, lstDeterminaciones);
				 lstDeterminacionesRegimenDeActos =	ClientGestorFip.obtenerDeterminacionesByTipoCaracterDeterminacion_EnCondicionesUrbanisticas(
						 			fip, application, ConstantesGestorFIP.CARATERDETERMINACION_REGIMENDEACTOS);
				 lstDeterminacionesRegimendeUso = ClientGestorFip.obtenerDeterminacionesByTipoCaracterDeterminacion_EnCondicionesUrbanisticas(
						 			fip, application, ConstantesGestorFIP.CARATERDETERMINACION_REGIMENDEUSO);
				 
				 if(lstDeterminacionesRegimenDeActos != null && lstDeterminacionesRegimenDeActos.length != 0){
					 for(int j=0; j<lstDeterminacionesRegimenDeActos.length; j++){
						 if(lstDeterminacionesRegimendeUsoYNormasUso == null){
								
							 lstDeterminacionesRegimendeUsoYNormasUso = new DeterminacionBean[1];
							 lstDeterminacionesRegimendeUsoYNormasUso[0] = lstDeterminacionesRegimenDeActos[j];
							
							}
							else{
								lstDeterminacionesRegimendeUsoYNormasUso = (DeterminacionBean[]) Arrays.copyOf(lstDeterminacionesRegimendeUsoYNormasUso, 
										lstDeterminacionesRegimendeUsoYNormasUso.length+1);
							
								lstDeterminacionesRegimendeUsoYNormasUso[lstDeterminacionesRegimendeUsoYNormasUso.length-1] = lstDeterminacionesRegimenDeActos[j];
							}
					 }
				 }
				 
				 
				 if(lstDeterminacionesRegimendeUso != null && lstDeterminacionesRegimendeUso.length != 0){
					 for(int j=0; j<lstDeterminacionesRegimendeUso.length; j++){
						 if(lstDeterminacionesRegimendeUsoYNormasUso == null){
								
							 lstDeterminacionesRegimendeUsoYNormasUso = new DeterminacionBean[1];
							 lstDeterminacionesRegimendeUsoYNormasUso[0] = lstDeterminacionesRegimendeUso[j];
							
							}
							else{
								lstDeterminacionesRegimendeUsoYNormasUso = (DeterminacionBean[]) Arrays.copyOf(lstDeterminacionesRegimendeUsoYNormasUso, 
										lstDeterminacionesRegimendeUsoYNormasUso.length+1);
							
								lstDeterminacionesRegimendeUsoYNormasUso[lstDeterminacionesRegimendeUsoYNormasUso.length-1] = lstDeterminacionesRegimendeUso[j];
								
							}
					 }
				 }
			
				 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e1));
			}
			
			
			this.add(getAsignacionPanel());

			this.addWindowListener(new java.awt.event.WindowAdapter()
	         {
			     public void windowClosing(java.awt.event.WindowEvent e)
			     {
			         dispose();
			     }
			 });
			
		}
		
		public JButton getCancelarJButton() {
			if(cancelarJButton == null){
				cancelarJButton = new JButton();
				cancelarJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.regimenes.cancelar"));
				
				
				cancelarJButton.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent e)
			        {
			        	dispose();
			        }
			    });

			}
			
			return cancelarJButton;
		}
		
		public JCheckBox getSeleccionValorJCheckBox() {
			if(seleccionValorJCheckBox == null){
				seleccionValorJCheckBox = new JCheckBox();
				//buttonGroup.add(radioSeleccionValor);
				seleccionValorJCheckBox.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.regimenes.valor"));
				
				seleccionValorJCheckBox.addActionListener(new ActionListener(){
	                public void actionPerformed(ActionEvent e)
	                {
	                	habilitarZonaValor();
	                	deshabilitarZonaValorReferencia();
	                	getSeleccionValorReferenciaJCheckBox().setSelected(false);
	                	getValorReferenciaJComboBox().setSelectedIndex(0);
	                	
	                	if(!seleccionValorJCheckBox.isSelected()){
	                		getValorJTextField().setText("");
	                		getValorJTextField().setEditable(false);
	                		getValorJTextField().setEnabled(false);
	                	}
	                }
	            });
				
			}
			return seleccionValorJCheckBox;
		}
		
		public void setSeleccionValorJCheckBox(JCheckBox seleccionValorJCheckBox) {
			this.seleccionValorJCheckBox = seleccionValorJCheckBox;
		}

		public JCheckBox getSeleccionValorReferenciaJCheckBox() {
			if(seleccionValorReferenciaJCheckBox == null){
				seleccionValorReferenciaJCheckBox = new JCheckBox();
				//seleccionValorReferenciaJCheckBox.setPreferredSize(new Dimension(200,60));
				//buttonGroup.add(radioSeleccionValorReferencia);
				seleccionValorReferenciaJCheckBox.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						"gestorFip.asociaciondeterminacionesentidades.regimenes.valorReferencia"));
					
				seleccionValorReferenciaJCheckBox.addActionListener(new ActionListener(){
	                public void actionPerformed(ActionEvent e)
	                {
	                	habilitarZonaValorReferencia();
	                	deshabilitarZonaValor();

	                	getSeleccionValorJCheckBox().setSelected(false);             
	                	getValorJTextField().setText("");
	                	
	                	if(!seleccionValorReferenciaJCheckBox.isSelected()){
	                		getValorReferenciaJComboBox().setSelectedIndex(0);
	                		getValorReferenciaJComboBox().setEnabled(false);
	                		
	                	}
	                	getValorReferenciaJComboBox().setEditable(false);
	                }
	            });
				
			}
			return seleccionValorReferenciaJCheckBox;
		}

		public void setSeleccionValorReferenciaJCheckBox(
				JCheckBox seleccionValorReferenciaJCheckBox) {
			this.seleccionValorReferenciaJCheckBox = seleccionValorReferenciaJCheckBox;
		}
		
		 public JComboBox getValorReferenciaJComboBox() {
			 if(valorReferenciaJComboBox == null){
				 valorReferenciaJComboBox = new JComboBox();
				 valorReferenciaJComboBox.setEditable(false);

				 deshabilitarZonaValorReferencia();
				 valorReferenciaJComboBox.addItem(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
									"gestorFip.asociaciondeterminacionesentidades.regimenes.seleccioneValorReferencia"));
			
			
				lstDeterminacionesValorReferenciaAux = lstDeterminacionesValorReferencia ;
				 valorReferenciaJComboBox.setEditable(false);
				 if(lstDeterminacionesValorReferencia != null && lstDeterminacionesValorReferencia.length != 0){
					 for(int i=0; i<lstDeterminacionesValorReferencia.length; i++){
						 if(lstDeterminacionesValorReferencia[i]!=null){
							 valorReferenciaJComboBox.addItem(lstDeterminacionesValorReferencia[i].getNombre());
						 }
					 }
				 }
			 }
				return valorReferenciaJComboBox;
		}

		public void setValorReferenciaJComboBox(JComboBox valorReferenciaJComboBox) {
			this.valorReferenciaJComboBox = valorReferenciaJComboBox;
		}

		public JTextField getValorJTextField() {
			
			if(valorJTextField == null){
				valorJTextField = new JTextField();
				 deshabilitarZonaValor();
				 
				 valorJTextField.addCaretListener(new CaretListener()
		            {
						public void caretUpdate(CaretEvent evt)
						{
							if(valorJTextField.getText().length()>200){
								EdicionUtils.retrocedeCaracter(valorJTextField,200);
							}
							
						}
		             });
				
			}
			
			return valorJTextField;
		}

		public void setValorJTextField(JTextField valorJTextField) {
			this.valorJTextField = valorJTextField;
		}

		public JPanel getSeleccionTipoPanel() {
			if(seleccionTipoPanel == null){
				seleccionTipoPanel = new JPanel();
				 seleccionTipoPanel.setLayout(new GridBagLayout());
				 seleccionTipoPanel.setBorder(new TitledBorder(""));
				 
				 seleccioneTipoJLabel = new JLabel();
				 seleccioneTipoJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
							"gestorFip.asociaciondeterminacionesentidades.regimenes.seleccioneTipoValor"));
				 
				 
				 seleccionTipoPanel.add(seleccioneTipoJLabel, 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 seleccionTipoPanel.add(getSeleccionValorJCheckBox(), 
							new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 seleccionTipoPanel.add(getSeleccionValorReferenciaJCheckBox(), 
							new GridBagConstraints(0,2,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 
			 }
				 
			return seleccionTipoPanel;
		}

		public void setSeleccionTipoPanel(JPanel seleccionTipoPanel) {
			this.seleccionTipoPanel = seleccionTipoPanel;
		}

		public JPanel getSeleccionValorPanel() {
			if(seleccionValorPanel == null){
				seleccionValorPanel = new JPanel();
				 seleccionValorPanel.setLayout(new GridBagLayout());
				 seleccionValorPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.regimenes.introduzcaValor")));
				 
				 seleccionValorPanel.setEnabled(false);

				 seleccionValorPanel.add(getSeleccionValorJCheckBox(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 
				 seleccionValorPanel.add(getValorJTextField(), 
							new GridBagConstraints(1,0,1,1, 0.9, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
			 }
				 
			return seleccionValorPanel;
		}

		public void setSeleccionValorPanel(JPanel seleccionValorPanel) {
			this.seleccionValorPanel = seleccionValorPanel;
		}

		public JPanel getSeleccionValorReferenciaPanel() {
			 if(seleccionValorReferenciaPanel == null){
				 seleccionValorReferenciaPanel = new JPanel();
				// seleccionValorReferenciaPanel.setPreferredSize(new Dimension(50,60));
				 
				 seleccionValorReferenciaPanel.setLayout(new GridBagLayout());
				 seleccionValorReferenciaPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.regimenes.seleccioneValorReferencia")));
				 
				 seleccionValorReferenciaPanel.setEnabled(false);

				 
				 seleccionValorReferenciaPanel.add(getSeleccionValorReferenciaJCheckBox(), 
							new GridBagConstraints(0,0,1,1, 0.001, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 seleccionValorReferenciaPanel.add(getValorReferenciaJComboBox(), 
							new GridBagConstraints(1,0,1,1, 0.001, 1,GridBagConstraints.NORTHEAST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 
			 }
				 
			return seleccionValorReferenciaPanel;
		}

		public void setSeleccionValorReferenciaPanel(
				JPanel seleccionValorReferenciaPanel) {
			this.seleccionValorReferenciaPanel = seleccionValorReferenciaPanel;
		}

		
		public JPanel getBotoneraPanel() {
			 if(botoneraPanel == null){
				 botoneraPanel = new JPanel();
				 botoneraPanel.setLayout(new GridBagLayout());
		 
				 botoneraPanel.add(getCancelarJButton(), 
							new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.EAST,
								GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
				 
			 }
			return botoneraPanel;
		}



		public void setBotoneraPanel(JPanel botoneraPanel) {
			this.botoneraPanel = botoneraPanel;
		}

		 public JPanel getAsignacionPanel() {
			 if(asignacionPanel == null){
				 asignacionPanel = new JPanel();
				 asignacionPanel.setLayout(new GridBagLayout());

				 
				 asignacionPanel.add(getCasoPanel(), 
							new GridBagConstraints(0,0,1,1, 1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
				 
				 asignacionPanel.add(getRegimenesPanel(), 
							new GridBagConstraints(0,1,1,1, 1,  0.9,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,0,0,0),0,0));
				 
				 asignacionPanel.add(getBotoneraPanel(), 
							new GridBagConstraints(0,2,1,1, 1, 0.01,GridBagConstraints.EAST,
								GridBagConstraints.NONE, new Insets(5,0,0,0),0,0));
				 
				 
				 
				 
			 }
			return asignacionPanel;
		}



		public void setAsignacionPanel(JPanel asignacionPanel) {
			this.asignacionPanel = asignacionPanel;
		}

		
		
		private boolean validarZonaRegimenEspecifico(){
			
			boolean datosValidos = true;
			if( !getOrdenRegimenEspecificoJTextField().getText().equals("") || 
					!getNombreRegimenEspecificoJTextField().getText().equals("") ||
					!getTextoRegimenEspecificoJTextArea().getText().equals("")    ){
				
				if(getOrdenRegimenEspecificoJTextField().getText().equals("") &&
						getNombreRegimenEspecificoJTextField().getText().equals("")){
					
					
					datosValidos = false;
				}
				
				else if((getOrdenRegimenEspecificoJTextField().getText().equals("") &&
						!getNombreRegimenEspecificoJTextField().getText().equals("")) || 
						(!getOrdenRegimenEspecificoJTextField().getText().equals("") &&
								getNombreRegimenEspecificoJTextField().getText().equals(""))){
					datosValidos = false;
				}
				
			}
			return datosValidos;
		}
		
		private boolean validarDatos(){
			boolean datosValidos = true;

			if(getSuperposicionJTextField().getText().equals("") &&
					getComentarioJTextArea().getText().equals("") &&

					( getOrdenRegimenEspecificoJTextField().getText().equals("") &&
							getNombreRegimenEspecificoJTextField().getText().equals("") &&
							getTextoRegimenEspecificoJTextArea().getText().equals("")    )
					
					
					&&
					!getSeleccionValorJCheckBox().isSelected() &&
					!getSeleccionValorReferenciaJCheckBox().isSelected()  &&
					getDeterminacinRegimenJComboBox().getSelectedIndex() == 0){
				

				JOptionPane.showMessageDialog(this, 
						I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
									"gestorFip.asociaciondeterminacionesentidades.error.introduzcaDatos"));
				datosValidos = false;
			}
			else{
				if(getSeleccionValorJCheckBox().isSelected() &&
						getValorJTextField().getText().equals("")){

					JOptionPane.showMessageDialog(this, 
							I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.error.introduzcaValor"));
					datosValidos = false;
				
					
				}
				else if(seleccionValorReferenciaJCheckBox.isSelected() &&
						getValorReferenciaJComboBox().getSelectedIndex() == 0){
					
					JOptionPane.showMessageDialog(this, 
							I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.error.seleccioneValorReferencia"));
					datosValidos = false;
				}
					
					
				
				if(!validarZonaRegimenEspecifico()){
					JOptionPane.showMessageDialog(this, 
							I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.error.relleneCampos.RegimenesEspecificos"));
					datosValidos = false;
				}
				
			}
			
			
			
			
			return datosValidos;
		}
		 
//		public void setAceptarJButton(JButton aceptarJButton) {
//			this.aceptarJButton = aceptarJButton;
//		}


		private void habilitarZonaValor(){
			seleccionValorJCheckBox.setEnabled(true);
			valorJTextField.setEnabled(true);
			valorJTextField.setEditable(true);
		}
		
		private void habilitarZonaValorReferencia(){
			seleccionValorReferenciaJCheckBox.setEnabled(true);
			 valorReferenciaJComboBox.setEditable(true);
			 valorReferenciaJComboBox.setEnabled(true);
		}
		
		private void deshabilitarZonaValor(){
			
			valorJTextField.setEnabled(false);
			valorJTextField.setEditable(false);
		}
		
		private void deshabilitarZonaValorReferencia(){
			 valorReferenciaJComboBox.setEditable(false);
			 valorReferenciaJComboBox.setEnabled(false);
		}
		

		public JTextArea getComentarioJTextArea() {
			if(comentarioJTextArea == null){
				comentarioJTextArea = new JTextArea(3, 10);
				
				comentarioJTextArea.setLineWrap(true);

			}
			return comentarioJTextArea;
		}



		public void setComentarioJTextArea(JTextArea comentarioJTextArea) {
			this.comentarioJTextArea = comentarioJTextArea;
		}



		public JScrollPane getComentarioPScroll() {
			if(comentarioPScroll == null){
				
				comentarioPScroll =new JScrollPane(getComentarioJTextArea(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			}
			return comentarioPScroll;
		}



		public void setComentarioPScroll(JScrollPane comentarioPScroll) {
			this.comentarioPScroll = comentarioPScroll;
		}



		public JTextArea getTextoRegimenEspecificoJTextArea() {
			
			if(textoRegimenEspecificoJTextArea == null){
				textoRegimenEspecificoJTextArea = new JTextArea(3, 10);
				
				textoRegimenEspecificoJTextArea.setLineWrap(true);
				
			}
			return textoRegimenEspecificoJTextArea;
		}



		public void setTextoRegimenEspecificoJTextArea(
				JTextArea textoRegimenEspecificoJTextArea) {
			this.textoRegimenEspecificoJTextArea = textoRegimenEspecificoJTextArea;
		}



		public JScrollPane getTextoRegimenEspecificoPScroll() {
			if(textoRegimenEspecificoPScroll == null){
				textoRegimenEspecificoPScroll =new JScrollPane(getTextoRegimenEspecificoJTextArea(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			}
			return textoRegimenEspecificoPScroll;
		}



		public void setTextoRegimenEspecificoPScroll(
				JScrollPane textoRegimenEspecificoPScroll) {
			this.textoRegimenEspecificoPScroll = textoRegimenEspecificoPScroll;
		}



		public JTextField getOrdenRegimenEspecificoJTextField() {
			if(ordenRegimenEspecificoJTextField == null){
				ordenRegimenEspecificoJTextField = new JTextField();
				ordenRegimenEspecificoJTextField.addCaretListener(new CaretListener()
		            {
						public void caretUpdate(CaretEvent evt)
						{
							EdicionUtils.chequeaLongCampoEdit(ordenRegimenEspecificoJTextField, application.getMainFrame());
						}
		             });
			}
			return ordenRegimenEspecificoJTextField;
		}



		public void setOrdenRegimenEspecificoJTextField(
				JTextField ordenRegimenEspecificoJTextField) {
			this.ordenRegimenEspecificoJTextField = ordenRegimenEspecificoJTextField;
		}



		public JTextField getNombreRegimenEspecificoJTextField() {
			if(nombreRegimenEspecificoJTextField == null){
				nombreRegimenEspecificoJTextField = new JTextField();
			}
			return nombreRegimenEspecificoJTextField;
		}



		public void setNombreRegimenEspecificoJTextField(
				JTextField nombreRegimenEspecificoJTextField) {
			this.nombreRegimenEspecificoJTextField = nombreRegimenEspecificoJTextField;
		}



		public JComboBox getDeterminacinRegimenJComboBox() {
			if(determinacinRegimenJComboBox == null){
				determinacinRegimenJComboBox = new JComboBox();
				//determinacinRegimenJComboBox.setSize(30,30);
				//determinacinRegimenJComboBox.setMaximumSize(new Dimension(30, 100));
				//determinacinRegimenJComboBox.setPreferredSize(new Dimension(50, 50));
				
				
				
				determinacinRegimenJComboBox.setEditable(false);
				 deshabilitarZonaValorReferencia();
				 determinacinRegimenJComboBox.addItem(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								"gestorFip.asociaciondeterminacionesentidades.regimenes.determinacionRegimen.seleccionedeterminacion"));
				 
				 determinacinRegimenJComboBox.setEditable(false);
				 if(lstDeterminacionesRegimendeUsoYNormasUso != null && lstDeterminacionesRegimendeUsoYNormasUso.length != 0){
					 for(int i=0; i<lstDeterminacionesRegimendeUsoYNormasUso.length; i++){
						 if(lstDeterminacionesRegimendeUsoYNormasUso[i]!=null){
							 determinacinRegimenJComboBox.addItem(lstDeterminacionesRegimendeUsoYNormasUso[i].getNombre());
						 }
					 }
				 }
				 
				 determinacinRegimenJComboBox.addActionListener (new ActionListener () {
					    public void actionPerformed(ActionEvent e) {
					    	int idDetRegimen = getIdDeterminacionRegimen();

					    	DeterminacionPanelBean[] lstDetRegimen = new DeterminacionPanelBean[1];
					    	DeterminacionPanelBean detReg = new DeterminacionPanelBean();
					    	detReg.setId(idDetRegimen);
					    	
					    	lstDetRegimen[0] = detReg; 

					    	
					    	try {
					    		if(idDetRegimen!= -1 ){
					    			DeterminacionBean[] lstDeterminacionesValRefDetRegimen = ClientGestorFip.obtenerDeterminacionesByTipoCaracterValorReferencia_EnCondicionesUrbanisticas(
											fip, application, ConstantesGestorFIP.CARATERDETERMINACION_VALORREFERENCIA, lstDetRegimen);
					    			lstDeterminacionesValorReferenciaAux = lstDeterminacionesValRefDetRegimen ;
						    		valorReferenciaJComboBox.removeAllItems();
						    		 valorReferenciaJComboBox.addItem(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.regimenes.seleccioneValorReferencia"));
						    		if(lstDeterminacionesValRefDetRegimen != null && lstDeterminacionesValRefDetRegimen.length != 0){
										 for(int i=0; i<lstDeterminacionesValRefDetRegimen.length; i++){
											 if(lstDeterminacionesValRefDetRegimen[i]!=null){
												 valorReferenciaJComboBox.addItem(lstDeterminacionesValRefDetRegimen[i].getNombre());
											 }
										 }
									 }
					    		}
					    		else{
					    			lstDeterminacionesValorReferenciaAux = lstDeterminacionesValorReferencia ;
					    			valorReferenciaJComboBox.removeAllItems();
						    		 valorReferenciaJComboBox.addItem(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.regimenes.seleccioneValorReferencia"));
						    		if(lstDeterminacionesValorReferencia != null && lstDeterminacionesValorReferencia.length != 0){
										 for(int i=0; i<lstDeterminacionesValorReferencia.length; i++){
											 if(lstDeterminacionesValorReferencia[i]!=null){
												 valorReferenciaJComboBox.addItem(lstDeterminacionesValorReferencia[i].getNombre());
											 }
										 }
									 }
					    		}
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    	
					    	
					    }
					});
				 
				 
				
				
			 }
			return determinacinRegimenJComboBox;
		}



		public void setDeterminacinRegimenJComboBox(
				JComboBox determinacinRegimenJComboBox) {
			this.determinacinRegimenJComboBox = determinacinRegimenJComboBox;
		}

		 public JPanel getComentarioJPanel() {
			 
			 if(comentarioJPanel == null){
				 comentarioJPanel = new JPanel();
				 comentarioJPanel.setLayout(new GridBagLayout());
				// comentarioJPanel.setPreferredSize(new Dimension(200,150));
				 
				 comentarioJPanel.setBorder(new TitledBorder(
						 I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								 "gestorFip.asociaciondeterminacionesentidades.regimenes.comentario")));
					
				 comentarioJPanel.add(getComentarioPScroll(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
			 }
			return comentarioJPanel;
		}



		public void setComentarioJPanel(JPanel comentarioJPanel) {
			this.comentarioJPanel = comentarioJPanel;
		}



		public JPanel getRegimenEspecificoJPanel() {
			 if(regimenEspecificoJPanel == null){
				 regimenEspecificoJPanel = new JPanel();
				 regimenEspecificoJPanel.setLayout(new GridBagLayout());
				 
				 regimenEspecificoJPanel.setBorder(new TitledBorder(
						 I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								 "gestorFip.asociaciondeterminacionesentidades.regimenes.regimenesEspecificos")));
					
				 nombreRegimenEspecificoJLabel = new JLabel();
				 nombreRegimenEspecificoJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						 "gestorFip.asociaciondeterminacionesentidades.regimenes.regimenesEspecificos.nombre"));
				 
				 ordenRegimenEspecificoCasoJLabel = new JLabel();
				 ordenRegimenEspecificoCasoJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						 "gestorFip.asociaciondeterminacionesentidades.regimenes.regimenesEspecificos.orden"));
				 
				 textoRegimenEspecificoJLabel = new JLabel();
				 textoRegimenEspecificoJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						 "gestorFip.asociaciondeterminacionesentidades.regimenes.regimenesEspecificos.texto"));
				 
				 regimenEspecificoJPanel.add(ordenRegimenEspecificoCasoJLabel, 
							new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 regimenEspecificoJPanel.add(nombreRegimenEspecificoJLabel, 
							new GridBagConstraints(0,1,1,1, 0.1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 regimenEspecificoJPanel.add(textoRegimenEspecificoJLabel, 
							new GridBagConstraints(0,2,1,1, 0.1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
				 
				 
				 regimenEspecificoJPanel.add(getOrdenRegimenEspecificoJTextField(), 
							new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 regimenEspecificoJPanel.add(getNombreRegimenEspecificoJTextField(), 
							new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 regimenEspecificoJPanel.add(getTextoRegimenEspecificoPScroll(), 
							new GridBagConstraints(1,2,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				 
				 
			 }
			return regimenEspecificoJPanel;
		}



		public void setRegimenEspecificoJPanel(JPanel regimenEspecificoJPanel) {
			this.regimenEspecificoJPanel = regimenEspecificoJPanel;
		}



		public JPanel getDeterminacionRegimenJPanel() {
			 if(determinacionRegimenJPanel == null){
				 determinacionRegimenJPanel = new JPanel();
				 determinacionRegimenJPanel.setLayout(new GridBagLayout());
				// determinacionRegimenJPanel.setPreferredSize(new Dimension(50,60));
					
				 determinacionRegimenJPanel.setBorder(new TitledBorder(
						 I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								 "gestorFip.asociaciondeterminacionesentidades.regimenes.determinacionRegimen")));
					
				 determinacionRegimenJPanel.add(getDeterminacinRegimenJComboBox(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
			 }
			return determinacionRegimenJPanel;
		}



		public void setDeterminacionRegimenJPanel(JPanel determinacionRegimenJPanel) {
			this.determinacionRegimenJPanel = determinacionRegimenJPanel;
		}
		
		


		public JPanel getRegimenJPanel() {
			if(regimenJPanel == null){
				regimenJPanel = new JPanel();
				regimenJPanel.setLayout(new GridBagLayout());
			 
				 regimenJPanel.setBorder(new TitledBorder(""));

				 regimenJPanel.setPreferredSize(new Dimension(600,510));
				 regimenJPanel.add(getComentarioJPanel(), 
							new GridBagConstraints(0,0,1,1, 1, 0.08,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(5,5,0,5),0,0));
				 
				 regimenJPanel.add(getRegimenEspecificoJPanel(), 
							new GridBagConstraints(0,1,1,1, 1, 0.08,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(5,5,0,5),0,0));
				 
				 regimenJPanel.add(getSeleccionValorPanel(), 
							new GridBagConstraints(0,2,1,1, 1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(5,5,0,5),0,0));
				
				 regimenJPanel.add(getSeleccionValorReferenciaPanel(), 
							new GridBagConstraints(0,3,1,1, 1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(5,5,0,5),0,0));
				
				 regimenJPanel.add(getDeterminacionRegimenJPanel(), 
							new GridBagConstraints(0,4,1,1, 1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.BOTH, new Insets(5,5,0,5),0,0));				 
				 
			}
			return regimenJPanel;
		}




		public JPanel getBotoneraAnadirEliminarRegimenJPanel() {
			if(botoneraAnadirRegimenJPanel == null){
				botoneraAnadirRegimenJPanel = new JPanel();
				botoneraAnadirRegimenJPanel.setLayout(new GridBagLayout());
			 
				botoneraAnadirRegimenJPanel.setBorder(new TitledBorder(""));
				 
				 
				botoneraAnadirRegimenJPanel.add(getAnadirRegimenJButton(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
								GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
				
				botoneraAnadirRegimenJPanel.add(getModificarRegimeJButton(), 
							new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.CENTER,
								GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
				 
				botoneraAnadirRegimenJPanel.add(getEliminarRegimenJButton(), 
						new GridBagConstraints(0,2,1,1, 1, 1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
			}
			return botoneraAnadirRegimenJPanel;
		}

		public void setBotoneraAnadirRegimenJPanel(JPanel botoneraAnadirRegimenJPanel) {
			this.botoneraAnadirRegimenJPanel = botoneraAnadirRegimenJPanel;
		}

		public JPanel getTablaRegimenesJPanel() {
			if(tablaRegimenesJPanel == null){
				tablaRegimenesJPanel = new JPanel();
				tablaRegimenesJPanel.setLayout(new GridBagLayout());
			 
				tablaRegimenesJPanel.setBorder(new TitledBorder(""));
				 
				 
				 tablaRegimenesJPanel.add(getTablaRegimenes(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			}
			return tablaRegimenesJPanel;
		}

		public void setTablaRegimenesJPanel(JPanel tablaRegimenesJPanel) {
			this.tablaRegimenesJPanel = tablaRegimenesJPanel;
		}

		public JButton getAnadirRegimenJButton() {
			if(anadirRegimenJButton == null){
				anadirRegimenJButton = new JButton();
				anadirRegimenJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.regimenes.anadir"));
				
				
				anadirRegimenJButton.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent e)
			        {
			        	anadir_actionPerformed();
			        	modificarRegimeJButton.setEnabled(false);
			        	eliminarRegimeJButton.setEnabled(false);
			        }
			    });

			}
			
			return anadirRegimenJButton;
		}
		
		private void anadir_actionPerformed(){
			
			if(validarDatos()){
				
				CondicionUrbanisticaCasoRegimenesBean cucr = new CondicionUrbanisticaCasoRegimenesBean();
				indiceRegimen ++;
				String nombreRegimen = I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.regimenes.nombreRegimen") + " " +String.valueOf(indiceRegimen);

				cucr.setNombre(nombreRegimen);

				valorSecuenciaCUCR --;
				cucr.setId(valorSecuenciaCUCR);
				if(!getSuperposicionJTextField().getText().equals("")){
					cucr.setSuperposicion(Integer.valueOf(getSuperposicionJTextField().getText()));
				}
				cucr.setComentario(getComentarioJTextArea().getText());
				
				CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
				
				if(validarZonaRegimenEspecifico()){
					
					if(!getOrdenRegimenEspecificoJTextField().getText().equals("") && 
							!getNombreRegimenEspecificoJTextField().getText().equals("")){
						int valorSecuenciaCUCRR = -1 ;
						try {
							//valorSecuenciaCUCRR = Test.obtenerValorSecuencia("gestorFip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi", application);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
						}
						
						cucrr = new CondicionUrbanisticaCasoRegimenRegimenesBean();
						
						cucrr.setId(valorSecuenciaCUCRR);
						if(!getOrdenRegimenEspecificoJTextField().getText().equals("")){
							cucrr.setOrden(Integer.valueOf(getOrdenRegimenEspecificoJTextField().getText()));
						}
						cucrr.setNombre(getNombreRegimenEspecificoJTextField().getText());
						cucrr.setTexto(getTextoRegimenEspecificoJTextArea().getText());
						cucrr.setRegimen(cucr.getId());
					}
				}
				cucr.setCucrr(cucrr);
				
				if(seleccionValorJCheckBox.isSelected()){
					cucr.setValor(getValorJTextField().getText());
					cucr.setValorreferencia_determinacionid(-1);
				}
				else if(seleccionValorReferenciaJCheckBox.isSelected()){
					cucr.setValorreferencia_determinacionid(getIdValorReferencia());
					cucr.setValor("");
				}
				
				cucr.setDeterminacionregimen_determinacionid(getIdDeterminacionRegimen());
				
				lstCUCR.add(cucr);

				getTablaRegimenes().cargaDatosTabla(lstCUCR);
				
				clearRegimenesPanel();
				
//				getAceptarJButton().setEnabled(true);
			}
		}
		
		private void clearRegimenesPanel(){
			getSuperposicionJTextField().setText("");
			getComentarioJTextArea().setText("");
			getOrdenRegimenEspecificoJTextField().setText("");
			getNombreRegimenEspecificoJTextField().setText("");
			getTextoRegimenEspecificoJTextArea().setText("");
			
			getValorJTextField().setText("");
			getValorJTextField().setEditable(false);
			getValorJTextField().setEnabled(false);
			
			getValorReferenciaJComboBox().setSelectedIndex(0);
			getValorReferenciaJComboBox().setEnabled(false);
			
			getSeleccionValorJCheckBox().setSelected(false);
			getSeleccionValorReferenciaJCheckBox().setSelected(false);
			
			getDeterminacinRegimenJComboBox().setSelectedIndex(0);
			
		}

		public void setAnadirRegimenJButton(JButton anadirRegimenJButton) {
			this.anadirRegimenJButton = anadirRegimenJButton;
		}

		 public JButton getModificarRegimeJButton() {
			 if(modificarRegimeJButton == null){
				 modificarRegimeJButton = new JButton();
				 modificarRegimeJButton.setEnabled(false);
				 modificarRegimeJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
											"gestorFip.asociaciondeterminacionesentidades.regimenes.modificar"));
					
					
				 modificarRegimeJButton.addActionListener(new ActionListener()
				    {
				        public void actionPerformed(ActionEvent e)
				        {
				        	modificar_actionPerformed();
				        }
				    });

				}
			return modificarRegimeJButton;
		}

		public void setModificarRegimeJButton(JButton modificarRegimeJButton) {
			this.modificarRegimeJButton = modificarRegimeJButton;
		}

		
		private void modificar_actionPerformed(){
			
			if(validarDatos()){

				if(!getSuperposicionJTextField().getText().equals("")){
						getTablaRegimenes().getElementoSeleccionado().setSuperposicion(Integer.valueOf(getSuperposicionJTextField().getText()));
				}
				getTablaRegimenes().getElementoSeleccionado().setComentario(getComentarioJTextArea().getText());
				
				if(validarZonaRegimenEspecifico()){
					
					CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
					
					if(!getOrdenRegimenEspecificoJTextField().getText().equals("") && 
							!getNombreRegimenEspecificoJTextField().getText().equals("")){
						
						if(getTablaRegimenes().getElementoSeleccionado().getCucrr() == null){
							int valorSecuenciaCUCRR = -1 ;
							try {
								valorSecuenciaCUCRR = ClientGestorFip.obtenerValorSecuencia("gestorFip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi", application);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
							}
							cucrr = new CondicionUrbanisticaCasoRegimenRegimenesBean();
							cucrr.setId(valorSecuenciaCUCRR);
						}
						else{
							cucrr = getTablaRegimenes().getElementoSeleccionado().getCucrr();
						}
							
						
						if(!getOrdenRegimenEspecificoJTextField().getText().equals("")){
							cucrr.setOrden(Integer.valueOf(getOrdenRegimenEspecificoJTextField().getText()));
						}
						cucrr.setNombre(getNombreRegimenEspecificoJTextField().getText());
						cucrr.setTexto(getTextoRegimenEspecificoJTextArea().getText());
						cucrr.setRegimen(getTablaRegimenes().getElementoSeleccionado().getId());
					}
					else{
						cucrr = null;
					}
					getTablaRegimenes().getElementoSeleccionado().setCucrr(cucrr);

				}

				if(seleccionValorJCheckBox.isSelected()){
					getTablaRegimenes().getElementoSeleccionado().setValor(getValorJTextField().getText());
					getTablaRegimenes().getElementoSeleccionado().setValorreferencia_determinacionid(-1);
				}
				else {
					getTablaRegimenes().getElementoSeleccionado().setValor("");
				}
				if(seleccionValorReferenciaJCheckBox.isSelected()){
					getTablaRegimenes().getElementoSeleccionado().setValorreferencia_determinacionid(getIdValorReferencia());
					getTablaRegimenes().getElementoSeleccionado().setValor("");
				}
				else {
					getTablaRegimenes().getElementoSeleccionado().setValorreferencia_determinacionid(-1);
				}
				
				getTablaRegimenes().getElementoSeleccionado().setDeterminacionregimen_determinacionid(getIdDeterminacionRegimen());
				
				//lstCUCR.add(cucr);

				//getTablaRegimenes().cargaDatosTabla(lstCUCR);
				
				clearRegimenesPanel();
				
				//getAceptarJButton().setEnabled(true);
			}
			
			getTablaRegimenes().cargaDatosTabla(getTablaRegimenes().getLstCUCR());

		}

		
		public JButton getEliminarRegimenJButton() {
			if(eliminarRegimeJButton == null){
				eliminarRegimeJButton = new JButton();
				eliminarRegimeJButton.setEnabled(false);
				eliminarRegimeJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.regimenes.eliminar"));
				
				
				eliminarRegimeJButton.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent e)
			        {
			        	eliminar_actionPerformed();
			        	eliminarRegimeJButton.setEnabled(false);
			        	clearRegimenesPanel();
			        	activarPanelesRegimenes();

			        }
			    });

			}
			
			return eliminarRegimeJButton;
		}

		public void setEliminarRegimeJButton(JButton eliminarRegimeJButton) {
			eliminarRegimeJButton = eliminarRegimeJButton;
		}
		
		private void eliminar_actionPerformed(){
			
			
			ArrayList  lstIds = getTablaRegimenes().getIdElementosSeleccionados();
			
			ArrayList lstIndicesEliminar = new ArrayList();
			
			for(int i=0; i<lstIds.size();i++){
				
				for(int j=0; j<lstCUCR.size(); j++){
					
					if(((CondicionUrbanisticaCasoRegimenesBean)lstCUCR.get(j)).getId() == 
						((Integer)lstIds.get(i)).intValue()){
						
						lstIndicesEliminar.add(new Integer(j));
					}
					
				}
			}
			
			for(int j=0; j<lstIndicesEliminar.size(); j++){
				lstCUCR.remove(((Integer)lstIndicesEliminar.get(j)).intValue());
			}
			getTablaRegimenes().cargaDatosTabla(lstCUCR);
//			if(lstCUCR.isEmpty()){
//				getAceptarJButton().setEnabled(false);
//			}

		}

		 public TablaRegimenes getTablaRegimenes() {
			 if(tablaRegimenes == null){

		         tablaRegimenes.getElementosTable().getTableHeader().addMouseListener(new MouseListener(){
	
		            public void mouseClicked(MouseEvent e) {
	
		                int columna =  tablaRegimenes.getElementosTable().columnAtPoint(e.getPoint());
		                tablaRegimenes.setSorted(!tablaRegimenes.isSorted());
		                tablaRegimenes.sortAllRowsBy(tablaRegimenes.getModelo(), columna, tablaRegimenes.isSorted());//e.getClickCount()%2 == 0? false : true);
		                
		                clearRegimenesPanel();
		                eliminarRegimeJButton.setEnabled(false);
		                modificarRegimeJButton.setEnabled(false);
		            }
	
		            public void mouseEntered(MouseEvent e) {}
	
		            public void mouseExited(MouseEvent e) {}
	
		            public void mousePressed(MouseEvent e) {}
	
		            public void mouseReleased(MouseEvent e) {}
		        });
				 
				 tablaRegimenes.getElementosTable().addMouseListener(new MouseListener(){

			            public void mouseClicked(MouseEvent e) {
			            	eliminarRegimeJButton.setEnabled(true);
			            	CondicionUrbanisticaCasoRegimenesBean cucrSeleccionado = 
			            				getTablaRegimenes().getElementoSeleccionado();
			            	
			            	cargarDatosPanel(cucrSeleccionado);
			            	modificarRegimeJButton.setEnabled(true);
			            		           	 
			           	 
			            	seleccionValorReferenciaJCheckBox.setEnabled(true);
			            	if(seleccionValorReferenciaJCheckBox.isSelected()){
			            		valorReferenciaJComboBox.setEnabled(true);
			            		
			            		valorJTextField.setEnabled(false);
			            		valorJTextField.setEditable(false);
			            	}
			            	seleccionValorJCheckBox.setEnabled(true);
			            	if(seleccionValorJCheckBox.isSelected()){
			            		valorJTextField.setEnabled(true);
			            		valorJTextField.setEditable(true);
			            		
			            		valorReferenciaJComboBox.setEnabled(false);
			            	}
			            	
			            	if(!seleccionValorReferenciaJCheckBox.isSelected() &&
			            			!seleccionValorJCheckBox.isSelected()){
			            		
			            		valorJTextField.setEnabled(false);
			            		valorJTextField.setEditable(false);
			            		valorReferenciaJComboBox.setEnabled(false);
			            	}
			            }

						
						public void mouseEntered(MouseEvent e) {
						}

						
						public void mouseExited(MouseEvent e) {
						}

						
						public void mousePressed(MouseEvent e) {
						}
						
						public void mouseReleased(MouseEvent e) {
						}
				 });
				 
			 }

			return tablaRegimenes;
		}

		public void setTablaRegimenes(TablaRegimenes tablaRegimenes) {
			this.tablaRegimenes = tablaRegimenes;
		}

		 public JPanel getDatosRegimenJPanel() {
			 if(datosRegimenJPanel == null){
				 datosRegimenJPanel = new JPanel();
				 datosRegimenJPanel.setLayout(new GridBagLayout());
				 
				 datosRegimenJPanel.setBorder(new TitledBorder(""));
				// datosRegimenJPanel.setPreferredSize(new Dimension(650,620));
				 superposicionJLabel = new JLabel();
				 superposicionJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
							"gestorFip.asociaciondeterminacionesentidades.regimenes.superposicion"));
				 
				 
				 datosRegimenJPanel.add(superposicionJLabel, 
							new GridBagConstraints(0,0,1,1, 0.1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 datosRegimenJPanel.add(getSuperposicionJTextField(), 
							new GridBagConstraints(1,0,1,1, 0.1, 0.9,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
					 
				 datosRegimenJPanel.add(getRegimenJPanel(), 
							new GridBagConstraints(0,1,2,1, 0.1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 
				}
			return datosRegimenJPanel;
		}

		public void setDatosRegimenJPanel(JPanel datosRegimenJPanel) {
			this.datosRegimenJPanel = datosRegimenJPanel;
		}
		
		public JTextField getSuperposicionJTextField() {
			 if(superposicionJTextField == null){
				 superposicionJTextField = new JTextField();
				 
				 superposicionJTextField.addCaretListener(new CaretListener()
		            {
						public void caretUpdate(CaretEvent evt)
						{
							EdicionUtils.chequeaNumCampoEdit(superposicionJTextField, application.getMainFrame());
							
						}
		             });
			 }
			return superposicionJTextField;
		}

		public void setSuperposicionJTextField(JTextField superposicionJTextField) {
			this.superposicionJTextField = superposicionJTextField;
		}

		private int  getIdValorReferencia(){
			
			int idDetValorReferencia = -1;
			int indiceComboValorReferencia = getValorReferenciaJComboBox().getSelectedIndex();
			if(indiceComboValorReferencia > 0){
				idDetValorReferencia = lstDeterminacionesValorReferenciaAux[indiceComboValorReferencia-1].getId();
//				idDetValorReferencia = lstDeterminacionesValorReferencia[indiceComboValorReferencia-1].getId();
				
			}
			return idDetValorReferencia;
		}
		
		private int  getIdDeterminacionRegimen(){
			
			int idDetRegimen = -1;
			int indiceComboNormasDeUso = getDeterminacinRegimenJComboBox().getSelectedIndex();
			if(indiceComboNormasDeUso > 0){
				
				idDetRegimen = lstDeterminacionesRegimendeUsoYNormasUso[indiceComboNormasDeUso-1].getId();
			}
			return idDetRegimen;
		}
		
		private void cargarDatosPanel(CondicionUrbanisticaCasoRegimenesBean cucrSeleccionado){
			
			
			if(cucrSeleccionado.getSuperposicion() > 0){
				getSuperposicionJTextField().setText(String.valueOf(cucrSeleccionado.getSuperposicion()));
			}
			else{
				getSuperposicionJTextField().setText("");
			}
			
			if(cucrSeleccionado.getComentario() != null &&
					!cucrSeleccionado.getComentario().equals(null)){
				getComentarioJTextArea().setText(String.valueOf(cucrSeleccionado.getComentario()));
			}
			else{
				getComentarioJTextArea().setText("");
			}
			
			if(cucrSeleccionado.getCucrr() != null){
				
				getOrdenRegimenEspecificoJTextField().setText(
						String.valueOf(cucrSeleccionado.getCucrr().getOrden()));
				
				getNombreRegimenEspecificoJTextField().setText(cucrSeleccionado.getCucrr().getNombre());
				getTextoRegimenEspecificoJTextArea().setText(cucrSeleccionado.getCucrr().getTexto());
				
			}
			else{
				getOrdenRegimenEspecificoJTextField().setText("");
				
				getNombreRegimenEspecificoJTextField().setText("");
				getTextoRegimenEspecificoJTextArea().setText("");
			}
			
			if(cucrSeleccionado.getValor() != null && !cucrSeleccionado.getValor().equals("")){
				getValorJTextField().setText(cucrSeleccionado.getValor());
				getValorJTextField().setEditable(true);
				getValorJTextField().setEnabled(true);
				getSeleccionValorJCheckBox().setEnabled(true);
				getSeleccionValorJCheckBox().setSelected(true);
				
				
				getValorReferenciaJComboBox().setSelectedIndex(0);
				getSeleccionValorReferenciaJCheckBox().setEnabled(false);
				getSeleccionValorReferenciaJCheckBox().setSelected(false);
				getSeleccionValorReferenciaJCheckBox().setEnabled(false);
			}
			else{
				getValorJTextField().setText("");
				getSeleccionValorJCheckBox().setEnabled(false);
				getSeleccionValorJCheckBox().setSelected(false);
			}
			
			
			
			if(cucrSeleccionado.getDeterminacionregimen_determinacionid() > 0 ){
				for(int i=0; i<lstDeterminacionesRegimendeUsoYNormasUso.length;i++){
					if(lstDeterminacionesRegimendeUsoYNormasUso[i].getId() ==
						cucrSeleccionado.getDeterminacionregimen_determinacionid()){
						getDeterminacinRegimenJComboBox().setSelectedIndex(i+1);
					}
				}
			}
			else{
				getDeterminacinRegimenJComboBox().setSelectedIndex(0);
			}
			
			
			if(cucrSeleccionado.getValorreferencia_determinacionid() > 0){
				
				
				
				for(int i=0; i<lstDeterminacionesValorReferenciaAux.length;i++){
					if(lstDeterminacionesValorReferenciaAux[i].getId() ==
						cucrSeleccionado.getValorreferencia_determinacionid()){
						getValorReferenciaJComboBox().setSelectedIndex(i+1);
					}
				}

				getSeleccionValorReferenciaJCheckBox().setEnabled(true);
				getSeleccionValorReferenciaJCheckBox().setSelected(true);
				getValorReferenciaJComboBox().setEnabled(true);
				
				getValorJTextField().setText("");
				getSeleccionValorJCheckBox().setEnabled(false);
				getSeleccionValorJCheckBox().setSelected(false);
				getValorReferenciaJComboBox().setEditable(false);
				getValorReferenciaJComboBox().setEnabled(false);
				
			}
			else{
				getValorReferenciaJComboBox().setSelectedIndex(0);
				getSeleccionValorReferenciaJCheckBox().setEnabled(false);
				getSeleccionValorReferenciaJCheckBox().setSelected(false);
				
			}
		}

		public void cargarDatos(String nombreCaso, ArrayList lstCUCR){
			this.lstCUCR = lstCUCR;
			getTablaRegimenes().cargaDatosTabla(lstCUCR);
			getNombreCasoJTextField().setText(nombreCaso);
			
		}
		
		public void desactivarPanelCaso(){
			getNombreCasoJTextField().setEditable(false);
			getNombreCasoJTextField().setEnabled(false);
		}
		public void activarPanelCaso(){
			getNombreCasoJTextField().setEditable(true);
			getNombreCasoJTextField().setEnabled(true);
		}
		
		public void desactivarPanelesRegimenes(){
			

			superposicionJTextField.setEditable(false);
			superposicionJTextField.setEnabled(false);
			
			getComentarioJTextArea().setEditable(false);
			getComentarioJTextArea().setEnabled(false);
			
			getOrdenRegimenEspecificoJTextField().setEditable(false);
			getOrdenRegimenEspecificoJTextField().setEnabled(false);
			getNombreRegimenEspecificoJTextField().setEditable(false);
			getNombreRegimenEspecificoJTextField().setEnabled(false);
			getTextoRegimenEspecificoJTextArea().setEditable(false);
			getTextoRegimenEspecificoJTextArea().setEnabled(false);
			
			getValorJTextField().setEnabled(false);
			getValorJTextField().setEditable(false);
			getValorReferenciaJComboBox().setEditable(false);
			getValorReferenciaJComboBox().setEnabled(false);
			getSeleccionValorJCheckBox().setEnabled(false);
			getSeleccionValorReferenciaJCheckBox().setEnabled(false);
			
			getDeterminacinRegimenJComboBox().setEditable(false);
			getDeterminacinRegimenJComboBox().setEnabled(false);
		}
		
		public void activarPanelesRegimenes(){
			

			superposicionJTextField.setEditable(true);
			superposicionJTextField.setEnabled(true);
			
			getComentarioJTextArea().setEditable(true);
			getComentarioJTextArea().setEnabled(true);
			
			getOrdenRegimenEspecificoJTextField().setEditable(true);
			getOrdenRegimenEspecificoJTextField().setEnabled(true);
			getNombreRegimenEspecificoJTextField().setEditable(true);
			getNombreRegimenEspecificoJTextField().setEnabled(true);
			getTextoRegimenEspecificoJTextArea().setEditable(true);
			getTextoRegimenEspecificoJTextArea().setEnabled(true);
			

			getSeleccionValorJCheckBox().setEnabled(true);
			getSeleccionValorReferenciaJCheckBox().setEnabled(true);
			
			if(getSeleccionValorJCheckBox().isSelected()){
				getValorJTextField().setEnabled(true);
				getValorJTextField().setEditable(true);
			}
			else{
				getValorJTextField().setEnabled(false);
				getValorJTextField().setEditable(false);
			}
			
			if(getSeleccionValorReferenciaJCheckBox().isSelected()){
				getValorReferenciaJComboBox().setEditable(false);
				getValorReferenciaJComboBox().setEnabled(true);
			}
			else{
				getValorReferenciaJComboBox().setEditable(false);
				getValorReferenciaJComboBox().setEnabled(false);
			}
			
			getDeterminacinRegimenJComboBox().setEditable(false);
			getDeterminacinRegimenJComboBox().setEnabled(true);
		}

		public void  desactivarBotones(){
//			aceptarJButton.setEnabled(false);
			anadirRegimenJButton.setEnabled(false);
			eliminarRegimeJButton.setEnabled(false);

		}

		 public JPanel getCasoPanel() {
			 if(casoPanel == null){
				casoPanel = new JPanel();
				casoPanel.setLayout(new GridBagLayout());
				casoPanel.setBorder(new TitledBorder(""));
				
				nombreCasoJLabel = new JLabel();
				nombreCasoJLabel.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
				"gestorFip.asociaciondeterminacionesentidades.asignarCaso.nombre"));
				
				casoPanel.add(nombreCasoJLabel, 
						new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				casoPanel.add(getNombreCasoJTextField(), 
							new GridBagConstraints(1,0,1,1, 0.9, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
			 }
			 
			return casoPanel;
		}

		public void setCasoPanel(JPanel casoPanel) {
			this.casoPanel = casoPanel;
		}
		
		public JTextField getNombreCasoJTextField() {
			if(nombreCasoJTextField == null){
				nombreCasoJTextField = new JTextField();
			}
			return nombreCasoJTextField;
		}

		public void setNombreCasoJTextField(JTextField nombreCasoJTextField) {
			this.nombreCasoJTextField = nombreCasoJTextField;
		}

		public JPanel getRegimenesPanel() {
			 if(regimenesPanel == null){
				 regimenesPanel = new JPanel();
				 regimenesPanel.setLayout(new GridBagLayout());
				 regimenesPanel.setBorder(new TitledBorder(""));
				 
				 regimenesPanel.add(getDatosRegimenJPanel(), 
							new GridBagConstraints(0,1,1,1, 1, 0.01,GridBagConstraints.NORTHWEST,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 
				 regimenesPanel.add(getBotoneraAnadirEliminarRegimenJPanel(), 
							new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.CENTER,
								GridBagConstraints.CENTER, new Insets(5,5,0,5),0,0));
				 
				 regimenesPanel.add(getTablaRegimenesJPanel(), 
							new GridBagConstraints(2,1,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.VERTICAL, new Insets(5,5,0,5),0,0));
				 
			 }
			return regimenesPanel;
		}

		public void setRegimenesPanel(JPanel regimenesPanel) {
			this.regimenesPanel = regimenesPanel;
		}

		public void establecerTextoNombreCaso(String nombreCaso){
			getNombreCasoJTextField().setText(nombreCaso);
		}
}
