/**
 * VerDeterminacionesAsociadasToEntidadDialog.java
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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaCasos;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaElementosAsociados;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class VerDeterminacionesAsociadasToEntidadDialog extends JDialog{
	
	
	 private ApplicationContext application;
	 
	 private JPanel asociadosPanel;
	 private JPanel tablaAsociadosPanel;
	 private JPanel botoneraPanel;
	 
	 private TablaElementosAsociados tablaElementosAsociados;
	 private JButton cerrarJButton;
	 private JButton verRegimenesJButton;
	
	 private JPanel tablaCasosPanel;
	 private TablaCasos tablaCasos;
	 
	 
	 private String titleCampo1;
	 private String title;
	 private int idElementoSelectedArbolEntidades;
	 private FipPanelBean fipPanelBean;
	 
	 public static final int DIM_X = 630;
	 public static final int DIM_Y = 460;
	 
	 
	 public VerDeterminacionesAsociadasToEntidadDialog(FipPanelBean fipPanelBean, 
			 int idElementoSelectedArbolEntidades, 
			 String title , String titleCampo1,
			 ApplicationContext application){
		 
		 super(application.getMainFrame());

		 this.application = application;
		 this.titleCampo1 = titleCampo1;
		 this.title = title;
		 this.idElementoSelectedArbolEntidades = idElementoSelectedArbolEntidades;
		 this.fipPanelBean = fipPanelBean;

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false,this.application.getMainFrame());
		
	    inicializaElementos();
	    
	    DeterminacionBean[] lstDeterminaciones = consultarDeterminacionesAsociadasToDeterminacion();
	    if(lstDeterminaciones != null){
	    	tablaElementosAsociados.cargaDatosTabla(lstDeterminaciones);
	    }
	    else{
	    	dispose();
	    }
	     
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }

	 /**
	* Inicializa los elementos del panel.
	*/
	private void inicializaElementos()
	{
		
		this.setModal(false);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(DIM_X, DIM_Y);
		
		
		asociadosPanel = new JPanel();

		
		asociadosPanel.setLayout(new GridBagLayout());
		
	     
		asociadosPanel.add(getTablaAsociadosPanel(), 
				new GridBagConstraints(0,0,1,1, 1, 0.5,GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
		
		asociadosPanel.add(getBotoneraPanel(), 
				new GridBagConstraints(0,1,0,1, 1, 0.5,GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
		
		asociadosPanel.add(getTablaCasosPanel(), 
				new GridBagConstraints(1,0,1,1, 1, 0.5,GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
	
		this.add(asociadosPanel);
		this.setTitle(title);
		
		this.addWindowListener(new java.awt.event.WindowAdapter()
         {
		     public void windowClosing(java.awt.event.WindowEvent e)
		     {
		         dispose();
		     }
		 });
				
	}
	
	public JPanel getBotoneraPanel() {
		 if(botoneraPanel == null){
			 botoneraPanel = new JPanel();
			 botoneraPanel.setLayout(new GridBagLayout());
			 
			
		
			 botoneraPanel.add(getCerrarJButton(), 
						new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.EAST,
							GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
			 
		 }
		return botoneraPanel;
	}
	
	public JButton getCerrarJButton() {
		if(cerrarJButton == null){
			cerrarJButton = new JButton();
			cerrarJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
									"gestorFip.asociaciondeterminacionesentidades.verAsociados.cerrar"));
			
			
			cerrarJButton.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        	dispose();
		        }
		    });

		}
		
		return cerrarJButton;
	}

	public void setCerrarJButton(JButton cerrarJButton) {
		this.cerrarJButton = cerrarJButton;
	}
	
	public JPanel getTablaAsociadosPanel() {
		 if(tablaAsociadosPanel == null){
			 tablaAsociadosPanel = new JPanel();
			 tablaAsociadosPanel.setLayout(new GridBagLayout());
			 tablaAsociadosPanel.setBorder(new TitledBorder(""));
		 
			 tablaAsociadosPanel.add(getTablaElementosAsociados(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
			 
		 }
		return tablaAsociadosPanel;
	}
	
	public void setTablaAsociadosPanel(JPanel tablaAsociadosPanel) {
		this.tablaAsociadosPanel = tablaAsociadosPanel;
	}

	

		
	 public TablaElementosAsociados getTablaElementosAsociados() {
		 if(tablaElementosAsociados == null){
			 tablaElementosAsociados = new TablaElementosAsociados( titleCampo1);
			 tablaElementosAsociados.getElementosTable().addMouseListener(new MouseListener(){

		            public void mouseClicked(MouseEvent e) {
		            	
		            	verRegimenesJButton.setEnabled(false);
		            	verCasos_actionPerformed();
		            }

					
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
			 });
			 
		 }
		 
		return tablaElementosAsociados;
	}

	public void setTablaElementosAsociados(
			TablaElementosAsociados tablaElementosAsociados) {
		this.tablaElementosAsociados = tablaElementosAsociados;
	}
		
	private void verCasos_actionPerformed(){
		int[] lstIdSelected = getTablaElementosAsociados().getIdElementoSeleccionado();
		if(lstIdSelected.length >1){
			JOptionPane.showMessageDialog(this, 
					I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								"gestorFip.asociaciondeterminacionesentidades.error.seleccioneUnElemento"));
			getTablaCasos().cargaDatosTabla(null);
			verRegimenesJButton.setEnabled(true);
			
		}
		else{
			
			try {
				CondicionUrbanisticaCasoBean[] lstcuc = ClientGestorFip.obtenerCasosCondicionUrbanistica( 
						lstIdSelected[0],idElementoSelectedArbolEntidades, application);
				
				ArrayList arrayCUC = new ArrayList();
				if(lstcuc != null || lstcuc.length !=0	){
					for(int i=0; i<lstcuc.length; i++){
						arrayCUC.add(lstcuc[i]);
					}
				}
				getTablaCasos().cargaDatosTabla(arrayCUC);
			
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
			}
		}
	}
	
	
	private DeterminacionBean[] consultarDeterminacionesAsociadasToDeterminacion(){
		
		DeterminacionBean[] lstDeterminaciones = null;
		try {
			lstDeterminaciones = ClientGestorFip.obtenerDeterminacionesAsociadasToEntidad(
					idElementoSelectedArbolEntidades, application);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
		}
		return lstDeterminaciones;
	}
	
	 public JPanel getTablaCasosPanel() {
		 if(tablaCasosPanel == null){
			 tablaCasosPanel = new JPanel();
			 tablaCasosPanel.setLayout(new GridBagLayout());
			 tablaCasosPanel.setBorder(new TitledBorder(""));
				
		 
			 tablaCasosPanel.add(getTablaCasos(), 
					new GridBagConstraints(0,0,2,1, 1, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));

			 tablaCasosPanel.add(getVerRegimenesJButton(), 
						new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.EAST,
							GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));

			 
			 
		 }
		return tablaCasosPanel;
	}

	public void setTablaCasosPanel(JPanel tablaCasosPanel) {
		this.tablaCasosPanel = tablaCasosPanel;
	}
	
	 public TablaCasos getTablaCasos() {
		 if(tablaCasos == null){
			 tablaCasos.getElementosTable().addMouseListener(new MouseListener(){

		            public void mouseClicked(MouseEvent e) {
		            	
		            	verRegimenesJButton.setEnabled(true);
		            }

					
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
			 });
		 }
		return tablaCasos;
	}

	public void setTablaCasos(TablaCasos tablaCasos) {
		this.tablaCasos = tablaCasos;
	}
	
	 public JButton getVerRegimenesJButton() {
		 
		 if(verRegimenesJButton == null){
			 verRegimenesJButton = new JButton();
			 verRegimenesJButton.setEnabled(false);
			 verRegimenesJButton.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.verAsociados.verRegimenes"));
				
				
			 verRegimenesJButton.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent e)
			        {
			        	verRegimenes_actionPerformed();
			        }
			    });

			}
		return verRegimenesJButton;
	}

	public void setVerRegimenesJButton(JButton verRegimenesJButton) {
		this.verRegimenesJButton = verRegimenesJButton;
	}
	
	private void verRegimenes_actionPerformed(){
		
		ArrayList lstIdCasos = getTablaCasos().getIdElementosSeleccionados();
		if(lstIdCasos.size() == 1){
			
			
			CondicionUrbanisticaCasoRegimenesBean[] lstCUCR = null;
			try {
				lstCUCR = ClientGestorFip.obtenerRegimenesCasoCondicionUrbanistica((Integer)lstIdCasos.get(0), application);
				for (int h=0; h<lstCUCR.length; h++){

					CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
					try {
						cucrr = ClientGestorFip.obtenerRegimenRegimenesEspecificosCasoCondicionUrbanistica(lstCUCR[h].getId(), application);
						lstCUCR[h].setCucrr(cucrr);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
			}
			
			if(lstCUCR != null && lstCUCR.length!= 0){
				ArrayList lstCUCR_Array = new ArrayList();
				for(int i=0; i<lstCUCR.length; i++ ){
					lstCUCR_Array.add(lstCUCR[i]);
				}
			
				DeterminacionPanelBean[] lstDet = new DeterminacionPanelBean[1];
				DeterminacionPanelBean det = new DeterminacionPanelBean();
				
				int[] lstIdSelected = getTablaElementosAsociados().getIdElementoSeleccionado();
				det.setId(lstIdSelected[0]);
	
				lstDet[0] = det;
				
				EntidadPanelBean[] lstEnt = new EntidadPanelBean[1];
				EntidadPanelBean ent = new EntidadPanelBean();
				ent.setId(idElementoSelectedArbolEntidades);
			
				lstEnt[0] = ent;

				ArrayList lstCUC_Array = new  ArrayList();
				lstCUC_Array.add(getTablaCasos().getCasoSeleccionado((Integer)lstIdCasos.get(0)));
				
				RegimenesCondicionUrbanisticaAsociacionDeterminacionEntidadDialog asignarCondUba = 
					new RegimenesCondicionUrbanisticaAsociacionDeterminacionEntidadDialog(
							fipPanelBean, 
							lstDet, 
							lstEnt,
							lstCUC_Array, 
							application);
				
				asignarCondUba.setSize(new Dimension(1120,700));
				asignarCondUba.cargarDatos(getTablaCasos().getNombreElementoSeleccionado(),lstCUCR_Array);
				asignarCondUba.getNombreCasoJTextField().setEditable(false);
				asignarCondUba.getNombreCasoJTextField().setEditable(false);

				
				asignarCondUba.show();
			}
		}
		else{
			JOptionPane.showMessageDialog(this, 
					I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								"gestorFip.asociaciondeterminacionesentidades.asignarCaso.seleccioneUnCaso"));
		}
		
		
		
	}
	

}
