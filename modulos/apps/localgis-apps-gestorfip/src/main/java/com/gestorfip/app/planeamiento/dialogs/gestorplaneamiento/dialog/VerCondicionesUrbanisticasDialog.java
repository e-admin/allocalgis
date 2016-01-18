/**
 * VerCondicionesUrbanisticasDialog.java
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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.EntidadBean;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaDetEnt;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaCasos;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaRegimenes;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

import es.gestorfip.serviciosweb.ServicesStub.UnidadBean;

public class VerCondicionesUrbanisticasDialog extends JDialog{

	
	private static final long serialVersionUID = 1L;
	
	private AppContext appContext = (AppContext) AppContext.getApplicationContext();
	private ApplicationContext application = (AppContext)AppContext.getApplicationContext();

	public static final int DIM_X = 1300;
	public static final int DIM_Y = 390;
	private JPanel visorCondicionesUrbanisticasPanel = null;
	private JPanel zonaDetEntPanel = null;
	private JPanel zonaCasosPanel = null;
	private JPanel zonaRegimenPanel = null;
	private JPanel zonaRegimenEspecificoPanel = null;
	private JPanel regimenEspecificoPanel = null;

	private TablaDetEnt tablaDetEnt;
	private TablaCasos tablaCasos;
	private TablaRegimenes tablaRegimenes;
	
	
	private JLabel labelDetRegimen = null;
	private JLabel labelValRef = null;
	private JLabel labelValor = null;
	private JLabel labelOrden = null;
	private JLabel labelNombre = null;
	private JLabel labelTexto = null;
	
	private JTextField textDetRegimen = null;
	private JTextField textValRef = null;
	private JTextField textValor = null;
	private JTextField textOrden = null;
	private JTextField textNombre = null;
	private JTextField textUnidad = null;
	
	private JTextArea textoJTextArea;
	private JScrollPane textoScroll;
	private String titleTableDetEnt;
	
	// indica si se esta consultando la lista de determinaciones asociadas a la entidad seleccionada en el gestor de planeamiento valor (1)
	// o si se esta consultando la lista de entidades asociadas a la determinacion seleccionada en el gestor de planeamiento valor (-1)
	
	private int consultaLstDetEnt = 0;
	private int idEntSelected = -1;
	private int idDetSelected = -1;
	
	private Object[] lstEntDet = null;
	
	public VerCondicionesUrbanisticasDialog(EntidadBean[] lstEntidades, int idDetSelected, ApplicationContext application) throws Exception{
		 
		super(application.getMainFrame());
		this.lstEntDet = lstEntidades;
		this.idDetSelected = idDetSelected;
		
		this.titleTableDetEnt = "gestorFip.planeamiento.condicionesurbanisticas.entidades";
		
		consultaLstDetEnt = ConstantesGestorFIP.CONDICION_URBANISTICA_DETERMINACION_SELECTED;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false,this.application.getMainFrame());
		
	    inicializaElementos();
	    seleccionInicial();
	    int posx = (this.application.getMainFrame().size().width - this.getWidth())/2;
		int posy = (this.application.getMainFrame().size().height -this.getHeight())/2;
		this.setLocation(posx, posy);      
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	
	 
	 public VerCondicionesUrbanisticasDialog(DeterminacionBean[] lstDeterminaciones, int idEntSelected,  ApplicationContext application) throws Exception{
		 
		super(application.getMainFrame());
		this.lstEntDet = lstDeterminaciones;
		this.idEntSelected = idEntSelected;
		
		this.titleTableDetEnt = "gestorFip.planeamiento.condicionesurbanisticas.determinaciones";
		
		consultaLstDetEnt = ConstantesGestorFIP.CONDICION_URBANISTICA_ENTIDAD_SELECTED;
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false,this.application.getMainFrame());
		
	    inicializaElementos();
	    seleccionInicial();
	    
	    int posx = (this.application.getMainFrame().size().width - this.getWidth())/2;
		int posy = (this.application.getMainFrame().size().height -this.getHeight())/2;
		this.setLocation(posx, posy);      

	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	
	private void seleccionInicial() throws Exception{
		UnidadBean unidad = null;
		if(lstEntDet != null && lstEntDet.length != 0){
			getTablaDetEnt().getElementosTable().getSelectionModel().setSelectionInterval(0,0);
			if(consultaLstDetEnt == ConstantesGestorFIP.CONDICION_URBANISTICA_ENTIDAD_SELECTED){
        		obtenerCasos(getTablaDetEnt().getIdElementoSeleccionado(), idEntSelected );
        		unidad = ClientGestorFip.obtenerUnidadDeterminacion(getTablaDetEnt().getIdElementoSeleccionado(), application);
        	}
        	else{
        		obtenerCasos(idDetSelected, getTablaDetEnt().getIdElementoSeleccionado() );
        		
        		unidad = ClientGestorFip.obtenerUnidadDeterminacion(idDetSelected, application);
        		
        	}
			getTablaCasos().getElementosTable().getSelectionModel().setSelectionInterval(0,0);

			obtenerRegimenesCaso(getTablaCasos().getIdElementoSeleccionado());
			getTablaRegimenes().getElementosTable().getSelectionModel().setSelectionInterval(0,0);
			
			obtenerDatosRegimen(getTablaRegimenes().getElementoSeleccionado(), unidad);
	
		}
	}
	
	 /**
		* Inicializa los elementos del panel.
		*/
		private void inicializaElementos()
		{
			
			this.setModal(true);
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(DIM_X, DIM_Y);
			this.setMinimumSize(new Dimension(DIM_X, DIM_Y));
			
			
			visorCondicionesUrbanisticasPanel = new JPanel();		
			visorCondicionesUrbanisticasPanel.setLayout(new GridBagLayout());
			

			visorCondicionesUrbanisticasPanel.add(getZonaDetEntPanel(), 
						new GridBagConstraints(0,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));
			

			visorCondicionesUrbanisticasPanel.add(getZonaCasosPanel(), 
						new GridBagConstraints(1,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));
			

			visorCondicionesUrbanisticasPanel.add(getZonaRegimenPanel(), 
						new GridBagConstraints(2,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));
			

			visorCondicionesUrbanisticasPanel.add(getZonaRegimenEspecificoPanel(), 
						new GridBagConstraints(3,0,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(5,0,0,0),0,0));
			
			
	
			this.add(visorCondicionesUrbanisticasPanel);
			this.setTitle(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.condicionesurbanisticas.title"));
			
			this.addWindowListener(new java.awt.event.WindowAdapter()
	         {
			     public void windowClosing(java.awt.event.WindowEvent e)
			     {
			         dispose();
			     }
			 });
						
		}
		
		public JPanel getZonaDetEntPanel() {
			if(zonaDetEntPanel == null){
				zonaDetEntPanel = new JPanel();
				 zonaDetEntPanel.setLayout(new GridBagLayout());

				 zonaDetEntPanel.add(getTablaDetEnt(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));
				 

			 }
			return zonaDetEntPanel;
		}

		public JPanel getZonaCasosPanel() {
			if(zonaCasosPanel== null){
				zonaCasosPanel = new JPanel();
				 zonaCasosPanel.setLayout(new GridBagLayout());
		 
				 zonaCasosPanel.add(getTablaCasos(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));

			 }
			return zonaCasosPanel;
		}

		public JPanel getZonaRegimenPanel() {
			if(zonaRegimenPanel == null){
				zonaRegimenPanel = new JPanel();
				 zonaRegimenPanel.setLayout(new GridBagLayout());

				 zonaRegimenPanel.add(getTablaRegimenes(), 
							new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
								GridBagConstraints.VERTICAL, new Insets(5,0,0,0),0,0));
			 }
			return zonaRegimenPanel;
		}

		public JPanel getZonaRegimenEspecificoPanel() {
			if(zonaRegimenEspecificoPanel == null){
				zonaRegimenEspecificoPanel = new JPanel();
				 zonaRegimenEspecificoPanel.setLayout(new GridBagLayout());
				 zonaRegimenEspecificoPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.condicionesurbanisticas.regimen")));

				 
				 labelDetRegimen= new JLabel();
				 labelDetRegimen.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
												"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.detreg"));
					
				 labelValRef= new JLabel();
				 labelValRef.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
											"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.valorref"));
					
				 labelValor= new JLabel();
				 labelValor.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
											"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.valor"));
					
					
					
				 zonaRegimenEspecificoPanel.add(labelDetRegimen, 
							new GridBagConstraints(0,0,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
				 zonaRegimenEspecificoPanel.add(getTextDetRegimen(), 
							new GridBagConstraints(1,0,2,1, 1, 0.01,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(0,5,0,20),0,0));
				 
				 zonaRegimenEspecificoPanel.add(labelValRef, 
							new GridBagConstraints(0,1,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 zonaRegimenEspecificoPanel.add(getTextValRef(), 
							new GridBagConstraints(1,1,2,1, 1, 0.01,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,20),0,0));
				 
				 zonaRegimenEspecificoPanel.add(labelValor, 
							new GridBagConstraints(0,2,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				 zonaRegimenEspecificoPanel.add(getTextValor(), 
							new GridBagConstraints(1,2,1,1, 1, 1,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,20),0,0));
				 zonaRegimenEspecificoPanel.add(getTextUnidad(), 
							new GridBagConstraints(2,2,1,1, 1, 0.31,GridBagConstraints.NORTH,
								GridBagConstraints.HORIZONTAL, new Insets(5,5,0,20),0,0));
				 
				 
				 zonaRegimenEspecificoPanel.add(getRegimenEspecificoPanel(), 
							new GridBagConstraints(0,3,3,1, 1, 1,GridBagConstraints.NORTH,
								GridBagConstraints.BOTH, new Insets(0,0,0,5),0,0));

				 
			 }
			return zonaRegimenEspecificoPanel;
		}
		
		private void obtenerDatosRegimen(CondicionUrbanisticaCasoRegimenesBean regEspe, UnidadBean unidad){
			
			try {
				CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
				cucrr = ClientGestorFip.obtenerRegimenRegimenesEspecificosCasoCondicionUrbanistica(regEspe.getId(), application);
				if(cucrr != null){
					getTextOrden().setText(String.valueOf(cucrr.getOrden()));
					getTextNombre().setText(cucrr.getNombre());
					getTextoJTextArea().setText(cucrr.getTexto());
				}
				else{
					getTextOrden().setText("");
					getTextNombre().setText("");
					getTextoJTextArea().setText("");
				}
				
				DeterminacionBean detValRef = ClientGestorFip.obtenerDatosDeterminacion(regEspe.getValorreferencia_determinacionid(), application);

				DeterminacionBean detRegimen = ClientGestorFip.obtenerDatosDeterminacion(regEspe.getDeterminacionregimen_determinacionid(), application);
				
				if(detValRef != null){
					getTextValRef().setText(detValRef.getNombre());
				}
				else{
					getTextValRef().setText("");
				}
				
				if(detRegimen != null){
					getTextDetRegimen().setText(detRegimen.getNombre());
				}
				else{
					getTextDetRegimen().setText("");
				}
				getTextValor().setText(regEspe.getValor());
				
				getTextUnidad().setText("");
				if(regEspe.getValor() != null && !regEspe.getValor().equals("") && unidad != null){
					getTextUnidad().setText(unidad.getAbreviatura());
				}
			
				
				SwingUtilities.invokeLater(new Runnable() {
					   public void run() {
					  
					         Point p = new Point(
					            0,
					            0
					         );
					         textoScroll.getViewport().setViewPosition(p);
					      
					   }
					});
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDialog.show(application.getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
			}
		}
		
		private void  obtenerRegimenesCaso(int idCaso){

			CondicionUrbanisticaCasoRegimenesBean[] lstCUCR = null;
        	try {
        		lstCUCR = ClientGestorFip.obtenerRegimenesCasoCondicionUrbanistica(idCaso, application);
				
        		ArrayList arrayCUCR = new ArrayList();
        		if(lstCUCR != null && lstCUCR.length !=0	){
        			for(int i=0; i<lstCUCR.length; i++){
        				arrayCUCR.add(lstCUCR[i]);
					}
        		}
				getTablaRegimenes().cargaDatosTabla(arrayCUCR);
				
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDialog.show(application.getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
			}
        	
			
		}
		
		
		private void obtenerCasos(int idDet, int idEnt){
			
			
			CondicionUrbanisticaCasoBean[] lstcuc;
			try {
				lstcuc = ClientGestorFip.obtenerCasosCondicionUrbanistica( 
						idDet,idEnt, application);
				ArrayList arrayCUC = new ArrayList();
				if(lstcuc != null && lstcuc.length !=0	){
					for(int i=0; i<lstcuc.length; i++){
						arrayCUC.add(lstcuc[i]);
					}
				}
				getTablaCasos().cargaDatosTabla(arrayCUC);	
				
			} catch (Exception e1) {
		
				e1.printStackTrace();
				ErrorDialog.show(application.getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e1));
			}
			
		}
		
		public TablaDetEnt getTablaDetEnt() {
			if(tablaDetEnt == null){
				tablaDetEnt = new TablaDetEnt(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades", titleTableDetEnt), 
												I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.condicionesurbanisticas.nombre"),
													lstEntDet, 250, 300);

			//	tablaDetEnt.getElementosTable().setRowSelectionInterval(0, 0);

				tablaDetEnt.getElementosTable().addMouseListener(new MouseListener(){

			            public void mouseClicked(MouseEvent e) {
			            	getTablaRegimenes().vaciarTabla();
			            	limpiarPanelRegimen();
			            	int idDet = -1;
			            	int idEnt = -1;
			            	if(consultaLstDetEnt == ConstantesGestorFIP.CONDICION_URBANISTICA_ENTIDAD_SELECTED){
			            		idEnt = idEntSelected;
			            		idDet = tablaDetEnt.getIdElementoSeleccionado();
			            	}
			            	else{
			            		idDet = idDetSelected;
			            		idEnt = tablaDetEnt.getIdElementoSeleccionado();
			            	}
			            	
			            	
			            	UnidadBean unidad = null;
							try {
								unidad = ClientGestorFip.obtenerUnidadDeterminacion(idDet, application);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            	
			            	obtenerCasos(idDet, idEnt );
			            	getTablaCasos().getElementosTable().getSelectionModel().setSelectionInterval(0,0);

			    			obtenerRegimenesCaso(getTablaCasos().getIdElementoSeleccionado());
			    			getTablaRegimenes().getElementosTable().getSelectionModel().setSelectionInterval(0,0);
			    			
			    			obtenerDatosRegimen(getTablaRegimenes().getElementoSeleccionado(), unidad);
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
			return tablaDetEnt;
		}
		
		
		

		public TablaCasos getTablaCasos() {
			if(tablaCasos == null){
				tablaCasos = new TablaCasos(null,
						I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
								"gestorFip.planeamiento.condicionesurbanisticas.casos"),
						I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
						"gestorFip.planeamiento.condicionesurbanisticas.caso"));

				tablaCasos.getElementosTable().addMouseListener(new MouseListener(){

		            public void mouseClicked(MouseEvent e) {
		            
		            	obtenerRegimenesCaso(tablaCasos.getIdElementoSeleccionado());

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
			return tablaCasos;
		}
		
		

		public TablaRegimenes getTablaRegimenes() {
			if(tablaRegimenes == null){
				tablaRegimenes = new TablaRegimenes(null, 
							I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
									"gestorFip.planeamiento.condicionesurbanisticas.regimenes"),
							I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
									"gestorFip.planeamiento.condicionesurbanisticas.regimen"));
				
				tablaRegimenes.getElementosTable().addMouseListener(new MouseListener(){

		            public void mouseClicked(MouseEvent e) {
		            
		            	limpiarPanelRegimen();
		            	UnidadBean unidad = null;
						try {
							unidad = ClientGestorFip.obtenerUnidadDeterminacion(idDetSelected, application);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            	obtenerDatosRegimen(tablaRegimenes.getElementoSeleccionado(), unidad);
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
		

		public JTextField getTextDetRegimen() {
			if(textDetRegimen == null){
				textDetRegimen = new JTextField();
				
				textDetRegimen.setEditable(false);
				textDetRegimen.setEnabled(true);
			}
			return textDetRegimen;
		}


		public void setTextDetRegimen(JTextField textDetRegimen) {
			this.textDetRegimen = textDetRegimen;
		}


		public JTextField getTextValRef() {
			if(textValRef == null){
				textValRef = new JTextField();
				
				textValRef.setEditable(false);
				textValRef.setEnabled(true);
			}
			return textValRef;
		}


		public void setTextValRef(JTextField textValRef) {
			this.textValRef = textValRef;
		}


		public JTextField getTextUnidad() {
			if(textUnidad == null){
				textUnidad = new JTextField();
				
				textUnidad.setEditable(false);
				textUnidad.setEnabled(true);
			}
			return textUnidad;
		}


		public void setTextUnidad(JTextField textUnidad) {
			this.textUnidad = textUnidad;
		}
		

		public JTextField getTextValor() {
			if(textValor == null){
				textValor = new JTextField();
				
				textValor.setEditable(false);
				textValor.setEnabled(true);
			}
			return textValor;
		}


		public void setTextValor(JTextField textValor) {
			this.textValor = textValor;
		}


		public JTextField getTextOrden() {
			if(textOrden == null){
				textOrden = new JTextField();
				
				textOrden.setEditable(false);
				textOrden.setEnabled(true);
			}
			return textOrden;
		}


		public void setTextOrden(JTextField textOrden) {
			this.textOrden = textOrden;
		}


		public JTextField getTextNombre() {
			if(textNombre == null){
				textNombre = new JTextField();
				
				textNombre.setEditable(false);
				textNombre.setEnabled(true);
			}
			return textNombre;
		}


		public void setTextNombre(JTextField textNombre) {
			this.textNombre = textNombre;
		}


		
		
		public JScrollPane getTextoScroll() {
			if(textoScroll == null){
				textoScroll =new JScrollPane(getTextoJTextArea(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				
				textoScroll.setVisible(true);

			}
			return textoScroll;
		}
		

		public JTextArea getTextoJTextArea() {
			if(textoJTextArea == null){
				textoJTextArea = new JTextArea(8, 6);
				textoJTextArea.setEditable(false);
				textoJTextArea.setLineWrap(true);
				
			}
			return textoJTextArea;
		}

		public void setRegulacionTextoJTextArea(JTextArea textoJTextArea) {
			this.textoJTextArea = textoJTextArea;
		}
		
		
		public JPanel getRegimenEspecificoPanel() {
			if(regimenEspecificoPanel == null){
				regimenEspecificoPanel = new JPanel();
				regimenEspecificoPanel.setLayout(new GridBagLayout());
				regimenEspecificoPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
																		"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico")));
				 
				
				labelOrden = new JLabel();
				labelOrden.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.orden"));
				
				labelNombre = new JLabel();
				labelNombre.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.nombre"));
				
				labelTexto= new JLabel();
				labelTexto.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.planeamiento.condicionesurbanisticas.regimenespecifico.texto"));
				
				
				
				regimenEspecificoPanel.add(labelOrden, 
						new GridBagConstraints(0,0,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				regimenEspecificoPanel.add(getTextOrden(), 
						new GridBagConstraints(1,0,1,1, 1, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				
				
				regimenEspecificoPanel.add(labelNombre, 
						new GridBagConstraints(0,1,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				regimenEspecificoPanel.add(getTextNombre(), 
						new GridBagConstraints(1,1,1,1, 1, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				
				regimenEspecificoPanel.add(labelTexto, 
						new GridBagConstraints(0,2,1,1, 0.01, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				regimenEspecificoPanel.add(getTextoScroll(), 
						new GridBagConstraints(1,2,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
				
				
			}
			return regimenEspecificoPanel;
		}


		public void setRegimenEspecificoPanel(JPanel regimenEspecificoPanel) {
			this.regimenEspecificoPanel = regimenEspecificoPanel;
		}

		private void limpiarPanelRegimen(){
			
			getTextDetRegimen().setText("");
			getTextNombre().setText("");
			getTextOrden().setText("");
			getTextoJTextArea().setText("");
			getTextValor().setText("");
			getTextValRef().setText("");
		}
}
