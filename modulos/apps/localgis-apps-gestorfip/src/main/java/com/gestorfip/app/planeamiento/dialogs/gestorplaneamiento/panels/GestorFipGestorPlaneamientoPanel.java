/**
 * GestorFipGestorPlaneamientoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.EntidadBean;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.DeterminacionesPanelTree;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.EntidadesPanelTree;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog.VerCondicionesUrbanisticasDialog;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog.VerPropiedadesDeterminacionDialog;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog.VerPropiedadesEntidadDialog;
import com.gestorfip.app.planeamiento.images.IconLoader;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class GestorFipGestorPlaneamientoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private ApplicationContext aplicacion = (AppContext)AppContext.getApplicationContext();
	
    private JSplitPane jSplitPane = null;
    private JSplitPane listadosJSplitPane = null;
    
    private static GestorFipModuloPlaneamientoMapPanel gestorFipModuloPlaneamientoMapPanel = null;

	private PlugInContext context = null;
	
	private EntidadesPanelTree entidadesJPanelTreePlaneamientoVigente = null;
	private DeterminacionesPanelTree determinacionesJPanelTreePlaneamientoVigente = null;
	private JTree entidadesTreePlaneamientoVigente = null;
	private JTree determinacionesTreePlaneamientoVigente = null;
	private HideableTreeModel entidadesModelPlaneamientoVigente = null;
	private HideableTreeModel determinacionesModelPlaneamientoVigente = null;
	private JScrollPane entidadesJScrollPanePlaneamientoVigente = null;
	private JScrollPane determinacionesJScrollPanePlaneamientoVigente = null;

	
	private JPanel arbolDeterminacionesPanel = null;
	private JPanel arbolEntidadesPanel = null;
	private JPanel arbolesDeterminacionesPanel = null;
	private JPanel arbolesEntidadesPanel = null;
	private JPanel gestionEntidadesGraficasPanel = null;
	
	private JPanel busquedaDetPanel = null;
	private JPanel busquedaEntPanel = null;
	
	private JButton btnVerEntidadesAsociadas = null;
	private JButton btnVerDeterminacionesAsociadas = null;
	private JButton btnBusquedaDeterminacion = null;
	private JButton btnBusquedaEntidad = null;
	private JButton btnVerPropiedadesDeterminacion = null;
	private JButton btnVerPropiedadesEntidad = null;


	private JTextField textBusquedaEntidad = null;
	private JTextField textBusquedaDeterminacion = null;
	
	private FipPanelBean fipPanelBean;
	
	private EntidadPanelBean[] lstArbolEntidadesPlaneamientoVigente;
	private DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente;
	public static final String LOCALGIS_LOGO = "geopista.gif";
	
	private DeterminacionesTreeListener determinacionesTreeListener;
	private EntidadesTreeListener entidadesTreeListener;


	private DefaultMutableTreeNode nodeSeleccionadoDeterminaciones = null;
	private DefaultMutableTreeNode nodeSeleccionadoEntidades = null;
	
	private String arboSeleccionadoDeterminaciones;
	private String arboSeleccionadoEntidades;

	

	public GestorFipGestorPlaneamientoPanel(FipPanelBean fipPanelBean) {
		
		super();
		this.fipPanelBean = fipPanelBean;

		this.lstArbolEntidadesPlaneamientoVigente = fipPanelBean.getLstArbolEntidadesPlaneamientoVigente();

		this.lstArbolDeterminacionesPlaneamientoVigente = fipPanelBean.getLstArbolDeterminacionesPlaneamientoVigente();
		
		cargarDatos();
        initialize();  
       
        Locale loc=Locale.getDefault();      	
 	
    }

	private void initialize() {
		
		this.setLayout(new GridBagLayout());
		
		jSplitPane = new JSplitPane();

		jSplitPane.setLeftComponent(getGestionAsociacionDeterminacionesEntidadesPanel());
		
		jSplitPane.setRightComponent(getGestorFipModuloPlaneamientoMapPanel());
		
		jSplitPane.setEnabled(true);
		jSplitPane.setResizeWeight(0);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        
        this.setVisible(true);
        
        determinacionesTreeListener = new DeterminacionesTreeListener(this) ;
    	
    	entidadesTreeListener = new EntidadesTreeListener(this);
   
     // Listener de los cambios en el arbol
        getDeterminacionesJPanelTreePlaneamientoVigente().getTree().addTreeSelectionListener(determinacionesTreeListener);
        getEntidadesJPanelTreePlaneamientoVigente().getTree().addTreeSelectionListener(entidadesTreeListener);
        
    	jSplitPane.setDividerLocation(0);
    	
    	// introducimos el jsplitpane en el blacboar para poder acceder a el desde el plugin de OpenGestorFip
    	aplicacion.getBlackboard().put(ConstantesGestorFIP.OPEN_GESTORFIP, jSplitPane);

    	
	}
	
	private void cargarDatos(){
		
		CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacionPanelBean = null;
		
		
		try {
			lstCaracteresDeterminacionPanelBean = ClientGestorFip.obtenerCararteresDeterminaciones(aplicacion, fipPanelBean.getId());
			aplicacion.getBlackboard().put(ConstantesGestorFIP.LISTA_TIPOS_CARACTER_DETERMINACION, lstCaracteresDeterminacionPanelBean);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

		
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }
	
	public JPanel getGestionAsociacionDeterminacionesEntidadesPanel() {
	  if(gestionEntidadesGraficasPanel == null){
		  
		  gestionEntidadesGraficasPanel = new JPanel();
		  gestionEntidadesGraficasPanel.setLayout(new GridBagLayout());
		  
		  listadosJSplitPane = new JSplitPane();

		  listadosJSplitPane.setLeftComponent(getArbolDeterminacionesPanel());
			
		  listadosJSplitPane.setRightComponent(getArbolEntidadesPanel());
	
		  listadosJSplitPane.setEnabled(true);
		  listadosJSplitPane.setResizeWeight(0);
		  listadosJSplitPane.setOneTouchExpandable(true);
		  listadosJSplitPane.setDividerSize(10);
	        
		  gestionEntidadesGraficasPanel.add(listadosJSplitPane, 
	                new GridBagConstraints(0, 0, 1, 1, 1, 1,
	                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                        new Insets(0, 0, 0, 0), 0, 0));
		  
	  }
	
	  return gestionEntidadesGraficasPanel;
	}


	public JPanel getBusquedaDetPanel() {
		if(busquedaEntPanel == null){
			busquedaDetPanel = new JPanel();		
			
			busquedaDetPanel.setLayout(new GridBagLayout());
		
			busquedaDetPanel.add(getTextBusquedaDeterminacion(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			busquedaDetPanel.add(getBtnBusquedaDeterminacion(), 
					new GridBagConstraints(1,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
		}
		return busquedaDetPanel;
	}

	public void setBusquedaDetPanel(JPanel busquedaDetPanel) {
		this.busquedaDetPanel = busquedaDetPanel;
	}

	public JPanel getBusquedaEntPanel() {
		if(busquedaEntPanel == null){
			busquedaEntPanel = new JPanel();		
			
			busquedaEntPanel.setLayout(new GridBagLayout());
		
			busquedaEntPanel.add(getTextBusquedaEntidad(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			busquedaEntPanel.add(getBtnBusquedaEntidad(), 
					new GridBagConstraints(1,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
		}
		return busquedaEntPanel;
	}

	public void setBusquedaEntPanel(JPanel busquedaEntPanel) {
		this.busquedaEntPanel = busquedaEntPanel;
	}



	public JTextField getTextBusquedaDeterminacion() {
		if(textBusquedaDeterminacion == null){
			textBusquedaDeterminacion = new JTextField();
			textBusquedaDeterminacion.addCaretListener(new CaretListener() {
				
			
				public void caretUpdate(CaretEvent e) {
					// TODO Auto-generated method stub
					 if (textBusquedaDeterminacion.getText().length() == 0){
			        	 getBtnBusquedaDeterminacion().setEnabled(false);
			         }      
			         else{
			        	 getBtnBusquedaDeterminacion().setEnabled(true);
			         }
				}
			});
		
		}
		return textBusquedaDeterminacion;
	}

	public void setTextBusquedaDeterminacion(JTextField textBusquedaDeterminacion) {
		this.textBusquedaDeterminacion = textBusquedaDeterminacion;
	}

	public JButton getBtnBusquedaDeterminacion() {
		if(btnBusquedaDeterminacion == null){
			btnBusquedaDeterminacion = new JButton();
			btnBusquedaDeterminacion.setIcon(IconLoader.icon("SmallMagnify.gif"));
			btnBusquedaDeterminacion.setEnabled(false);
			btnBusquedaDeterminacion.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        
		        	ArrayList lstPaths = determinacionesJPanelTreePlaneamientoVigente.searchTree(determinacionesTreePlaneamientoVigente, 
		        			determinacionesTreePlaneamientoVigente.getPathForRow(0), getTextBusquedaDeterminacion().getText(), new ArrayList());
		        	TreePath[] paths = null;
		        	if(!lstPaths.isEmpty()){
		        		paths = new TreePath[lstPaths.size()];
		        		for(int i=0; i<lstPaths.size(); i++){
			        		paths[i] = (TreePath)lstPaths.get(i);
			        	}
			        	determinacionesJPanelTreePlaneamientoVigente.selectPaths(paths);
		        	}
		        	else{
		        		getBtnVerPropiedadesDeterminacion().setEnabled(false);
		        		getBtnVerEntidadesAsociadas().setEnabled(false);
		        		determinacionesJPanelTreePlaneamientoVigente.getTree().setSelectionRow(0);
		        	}
		        
				 }
		    });
		}
		return btnBusquedaDeterminacion;
	}

	public void setBtnBusquedaDeterminacion(JButton btnBusquedaDeterminacion) {
		this.btnBusquedaDeterminacion = btnBusquedaDeterminacion;
	}

	public JTextField getTextBusquedaEntidad() {
		if(textBusquedaEntidad == null){
			textBusquedaEntidad = new JTextField();
			textBusquedaEntidad.addCaretListener(new CaretListener() {
				
				
				public void caretUpdate(CaretEvent e) {
					// TODO Auto-generated method stub
					 if (textBusquedaEntidad.getText().length() == 0){
			        	 getBtnBusquedaEntidad().setEnabled(false);
			         }      
			         else{
			        	 getBtnBusquedaEntidad().setEnabled(true);
			         }
				}
			});

		}
		return textBusquedaEntidad;
	}

	public void setTextBusquedaEntidad(JTextField textBusquedaEntidad) {
		this.textBusquedaEntidad = textBusquedaEntidad;
	}

	public JButton getBtnBusquedaEntidad() {
		if(btnBusquedaEntidad == null){
			btnBusquedaEntidad = new JButton();
			btnBusquedaEntidad.setIcon(IconLoader.icon("SmallMagnify.gif"));
			btnBusquedaEntidad.setEnabled(false);
			btnBusquedaEntidad.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        	
		        	
		        	ArrayList lstPaths = entidadesJPanelTreePlaneamientoVigente.searchTree(entidadesTreePlaneamientoVigente, 
		        					entidadesTreePlaneamientoVigente.getPathForRow(0), getTextBusquedaEntidad().getText(), new ArrayList());
		        	TreePath[] paths = null;
		        	if(!lstPaths.isEmpty()){
		        		paths = new TreePath[lstPaths.size()];
		        		for(int i=0; i<lstPaths.size(); i++){
			        		paths[i] = (TreePath)lstPaths.get(i);
			        	}
			        	entidadesJPanelTreePlaneamientoVigente.selectPaths(paths);
		        	}
		        	else{
		        		getBtnVerPropiedadesEntidad().setEnabled(false);
		        		getBtnVerDeterminacionesAsociadas().setEnabled(false);
		        		entidadesJPanelTreePlaneamientoVigente.getTree().setSelectionRow(0);
		        	}
		        	

		        }
		    });
		}
		return btnBusquedaEntidad;
	}

	public void setBtnBusquedaEntidad(JButton btnBusquedaEntidad) {
		this.btnBusquedaEntidad = btnBusquedaEntidad;
	}

	public void searchEntidadesSelectedMap(int idLayer, int idFeature){
		

		ArrayList lstPaths = entidadesJPanelTreePlaneamientoVigente.searchTreeLayer(entidadesTreePlaneamientoVigente, 
				entidadesTreePlaneamientoVigente.getPathForRow(0), idLayer, idFeature, new ArrayList());
		
		TreePath[] paths = null;
    	if(!lstPaths.isEmpty()){
    		paths = new TreePath[lstPaths.size()];
    		for(int i=0; i<lstPaths.size(); i++){
        		paths[i] = (TreePath)lstPaths.get(i);
        	}
        	entidadesJPanelTreePlaneamientoVigente.selectPaths(paths);
    	}
    	else{
    		getBtnVerPropiedadesEntidad().setEnabled(false);
    		getBtnVerDeterminacionesAsociadas().setEnabled(false);
    		entidadesJPanelTreePlaneamientoVigente.getTree().setSelectionRow(0);
    	}
		
	}
	public JButton getBtnVerEntidadesAsociadas() {
		if(btnVerEntidadesAsociadas == null){
			btnVerEntidadesAsociadas = new JButton();
			btnVerEntidadesAsociadas.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.ver.entidadesAsociadas"));
			btnVerEntidadesAsociadas.setEnabled(false);
			btnVerEntidadesAsociadas.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        
		          btnVerEntidadesAsociadas_actionPerformed();
		        }
		    });
		}
		return btnVerEntidadesAsociadas;
	}

	public void setBtnVerEntidadesAsociadas(JButton btnVerEntidadesAsociadas) {
		this.btnVerEntidadesAsociadas = btnVerEntidadesAsociadas;
	}

	public JButton getBtnVerDeterminacionesAsociadas() {
		if(btnVerDeterminacionesAsociadas == null){
			btnVerDeterminacionesAsociadas = new JButton();
			btnVerDeterminacionesAsociadas.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.ver.determinacionesAsociadas"));
			btnVerDeterminacionesAsociadas.setEnabled(false);
			btnVerDeterminacionesAsociadas.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        	btnVerDeterminacionesAsociadas_actionPerformed();
		        }
		    });
		}
		return btnVerDeterminacionesAsociadas;
	}

	public void setBtnVerDeterminacionesAsociadas(
			JButton btnVerDeterminacionesAsociadas) {
		this.btnVerDeterminacionesAsociadas = btnVerDeterminacionesAsociadas;
	}
	

	public JButton getBtnVerPropiedadesDeterminacion() {
		if(btnVerPropiedadesDeterminacion == null){
			btnVerPropiedadesDeterminacion = new JButton();
			btnVerPropiedadesDeterminacion.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.ver.propdet"));
			btnVerPropiedadesDeterminacion.setEnabled(false);
			btnVerPropiedadesDeterminacion.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        	btnVerPropiedadesDetermiacion_actionPerformed();
		        }
		    });
		}
		return btnVerPropiedadesDeterminacion;
	}

	public void setBtnVerPropiedadesDeterminacion(JButton btnVerPropiedadesDeterminacion) {
		this.btnVerPropiedadesDeterminacion = btnVerPropiedadesDeterminacion;
	}

	public JButton getBtnVerPropiedadesEntidad() {
		if(btnVerPropiedadesEntidad == null){
			btnVerPropiedadesEntidad = new JButton();
			btnVerPropiedadesEntidad.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
					"gestorFip.asociaciondeterminacionesentidades.ver.propent"));
			btnVerPropiedadesEntidad.setEnabled(false);
			btnVerPropiedadesEntidad.addActionListener(new ActionListener()
		    {
		        public void actionPerformed(ActionEvent e)
		        {
		        	btnVerPropiedadesEntidad_actionPerformed();
		        }
		    });
		}
		return btnVerPropiedadesEntidad;
	}

	public void setBtnVerPropiedadesEntidad(JButton btnVerPropiedadesEntidad) {
		this.btnVerPropiedadesEntidad = btnVerPropiedadesEntidad;
	}

	
	
	
	public JPanel getArbolDeterminacionesPanel() {
		if(arbolDeterminacionesPanel == null){
			arbolDeterminacionesPanel = new JPanel();		
			
			arbolDeterminacionesPanel.setLayout(new GridBagLayout());
			arbolDeterminacionesPanel.setBorder(new TitledBorder(
					I18N.get("LocalGISGestorFip",
						"gestorFip.asociaciondeterminacionesentidades.gestion.listadoDeterminaciones")));
			
			arbolDeterminacionesPanel.add(getArbolesDeterminacionesPanel(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			
			arbolDeterminacionesPanel.add(getBusquedaDetPanel(), 
					new GridBagConstraints(0,1,1,1, 1, 0.01,GridBagConstraints.EAST,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			
			arbolDeterminacionesPanel.add(getBtnVerEntidadesAsociadas(), 
					new GridBagConstraints(0,2,1,1, 1, 0.01,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));

			arbolDeterminacionesPanel.add(getBtnVerPropiedadesDeterminacion(), 
					new GridBagConstraints(0,3,1,1, 1, 0.01,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
		}
		return arbolDeterminacionesPanel;
	}

	public void setArbolDeterminacionesPanel(JPanel arbolDeterminacionesPanel) {
		this.arbolDeterminacionesPanel = arbolDeterminacionesPanel;
	}

	public JPanel getArbolEntidadesPanel() {
		if(arbolEntidadesPanel == null){
			arbolEntidadesPanel = new JPanel();
			arbolEntidadesPanel.setLayout(new GridBagLayout());
			arbolEntidadesPanel.setBorder(new TitledBorder(
					I18N.get("LocalGISGestorFip",
						"gestorFip.asociaciondeterminacionesentidades.gestion.listadoEntidades")));
			
			arbolEntidadesPanel.add(getArbolesEntidadesPanel(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			
			arbolEntidadesPanel.add(getBusquedaEntPanel(), 
					new GridBagConstraints(0,1,1,1, 1, 0.01,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			
			arbolEntidadesPanel.add(getBtnVerDeterminacionesAsociadas(), 
					new GridBagConstraints(0,2,1,1, 1, 0.01,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			arbolEntidadesPanel.add(getBtnVerPropiedadesEntidad(), 
					new GridBagConstraints(0,3,1,1, 1, 0.01,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			

		}
		return arbolEntidadesPanel;
	}

	public void setArbolEntidadesPanel(JPanel arbolEntidadesPanel) {
		this.arbolEntidadesPanel = arbolEntidadesPanel;
	}


	
//	private JScrollPane getDeterminacionesJScrollPaneCatalogoSistematizado() {
//		if (determinacionesJScrollPaneCatalogoSistematizado == null) {
//			determinacionesJScrollPaneCatalogoSistematizado = new JScrollPane();
//			determinacionesJScrollPaneCatalogoSistematizado.setViewportView(getDeterminacionesJPanelTreeCatalogoSistematizado());
//			determinacionesJScrollPaneCatalogoSistematizado.setPreferredSize(new Dimension(200,210));
//			determinacionesJScrollPaneCatalogoSistematizado.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
//							"gestorFip.asociaciondeterminacionesentidades.gestion.catalogosistematizado")));
//		}
//		return determinacionesJScrollPaneCatalogoSistematizado;
//	}
	
	private JScrollPane getDeterminacionesJScrollPanePlaneamientoVigente() {
		if (determinacionesJScrollPanePlaneamientoVigente == null) {
			determinacionesJScrollPanePlaneamientoVigente = new JScrollPane();
			determinacionesJScrollPanePlaneamientoVigente.setViewportView(getDeterminacionesJPanelTreePlaneamientoVigente());
			determinacionesJScrollPanePlaneamientoVigente.setPreferredSize(new Dimension(200,210));
			determinacionesJScrollPanePlaneamientoVigente.setBorder(new TitledBorder(I18N.get("LocalGISGestorFip",
							"gestorFip.asociaciondeterminacionesentidades.gestion.planeamientovigente")));
		}
		return determinacionesJScrollPanePlaneamientoVigente;
	}
	

	private JScrollPane getEntidadesJScrollPanePlaneamientoVigente() {
		if (entidadesJScrollPanePlaneamientoVigente == null) {
			entidadesJScrollPanePlaneamientoVigente = new JScrollPane();
			entidadesJScrollPanePlaneamientoVigente.setViewportView(getEntidadesJPanelTreePlaneamientoVigente());
			entidadesJScrollPanePlaneamientoVigente.setPreferredSize(new Dimension(200,210));
			entidadesJScrollPanePlaneamientoVigente.setBorder(new TitledBorder(I18N.get("LocalGISGestorFip",
							"gestorFip.asociaciondeterminacionesentidades.gestion.planeamientovigente")));
		}
		return entidadesJScrollPanePlaneamientoVigente;
	}

	private EntidadesPanelTree getEntidadesJPanelTreePlaneamientoVigente() {
		if (entidadesJPanelTreePlaneamientoVigente == null) {
			entidadesJPanelTreePlaneamientoVigente = new EntidadesPanelTree(
					TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION, lstArbolEntidadesPlaneamientoVigente);
			
			entidadesJPanelTreePlaneamientoVigente.getTree().setName(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_ENTIDADES);
			entidadesTreePlaneamientoVigente = entidadesJPanelTreePlaneamientoVigente.getTree();
			entidadesTreePlaneamientoVigente.clearSelection();
			entidadesModelPlaneamientoVigente = (HideableTreeModel) entidadesTreePlaneamientoVigente.getModel();
		}

		return entidadesJPanelTreePlaneamientoVigente;
	}
	
	private DeterminacionesPanelTree getDeterminacionesJPanelTreePlaneamientoVigente() {
		if (determinacionesJPanelTreePlaneamientoVigente == null) {
			determinacionesJPanelTreePlaneamientoVigente = new DeterminacionesPanelTree(
					TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION, lstArbolDeterminacionesPlaneamientoVigente);
			
			determinacionesJPanelTreePlaneamientoVigente.getTree().setName(ConstantesGestorFIP.TRAMITE_PLANEAMIENTO_VIGENTE_DETERMINACIONES);
			determinacionesTreePlaneamientoVigente = determinacionesJPanelTreePlaneamientoVigente.getTree();
			determinacionesTreePlaneamientoVigente.clearSelection();
			determinacionesModelPlaneamientoVigente = (HideableTreeModel) determinacionesTreePlaneamientoVigente.getModel();
		}

		return determinacionesJPanelTreePlaneamientoVigente;
	}
	
	public GestorFipModuloPlaneamientoMapPanel getGestorFipModuloPlaneamientoMapPanel() {
		
		if(gestorFipModuloPlaneamientoMapPanel == null){
			gestorFipModuloPlaneamientoMapPanel = new GestorFipModuloPlaneamientoMapPanel(this);
		}
		
		return gestorFipModuloPlaneamientoMapPanel;
	}

	public void setGestorFipEditorEntidadesGraficasMapPanel(
			GestorFipModuloPlaneamientoMapPanel gestorFipEditorAsociacionDeterminacionesEntidadesMapPanel) {
		this.gestorFipModuloPlaneamientoMapPanel = gestorFipEditorAsociacionDeterminacionesEntidadesMapPanel;
	}
		
	
	private void verCondicionesUrbanisticasEntidad(DeterminacionBean[] lstDeterminaciones, int idEntSelected) throws Exception{
		
		VerCondicionesUrbanisticasDialog verCondicionesUrbanisticasDialog = 
				new VerCondicionesUrbanisticasDialog(lstDeterminaciones,idEntSelected,  aplicacion);
			
			verCondicionesUrbanisticasDialog.show();
	}
	
	private void verCondicionesUrbanisticasDeterminacion(EntidadBean[] lstEntidades, int idDetSelected) throws Exception{
		

		VerCondicionesUrbanisticasDialog verCondicionesUrbanisticasDialog = 
			new VerCondicionesUrbanisticasDialog(lstEntidades, idDetSelected,  aplicacion);
		
		verCondicionesUrbanisticasDialog.show();
		
	}
	
	
	private void btnVerEntidadesAsociadas_actionPerformed(){

		DefaultMutableTreeNode node = getNodeSeleccionadoDeterminaciones();
		
		if (node != null) {
			
			Object nodeInfo = node.getUserObject();	
			if(nodeInfo instanceof DeterminacionPanelBean){
				
				try {
					EntidadBean[] lstEntidades = ClientGestorFip.obtenerEntidadesAsociadasToDeterminacion(
							((DeterminacionPanelBean)nodeInfo).getId(), aplicacion);
//					ArrayList lstNombreElementos = new ArrayList();
					if(lstEntidades != null && lstEntidades.length !=0){

						verCondicionesUrbanisticasDeterminacion(lstEntidades, ((DeterminacionPanelBean)nodeInfo).getId());
					}
					else{
						JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
								I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.verAsociados.sinEntidadesAsociada"));
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
				}
				
				
			}
		}
		
	}
	
	
	private void btnVerDeterminacionesAsociadas_actionPerformed(){
		
		DefaultMutableTreeNode node = getNodeSeleccionadoEntidades();
		if (node != null) {
		
			Object nodeInfo = node.getUserObject();	
			if(nodeInfo instanceof EntidadPanelBean){
		
				try {
					DeterminacionBean[] lstDeterminaciones = ClientGestorFip.obtenerDeterminacionesAsociadasToEntidad(
							((EntidadPanelBean)nodeInfo).getId(), aplicacion);
					
					if(lstDeterminaciones != null && lstDeterminaciones.length != 0){
						
						verCondicionesUrbanisticasEntidad(lstDeterminaciones, ((EntidadPanelBean)nodeInfo).getId());

					}
					else{
						JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
								I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
										"gestorFip.asociaciondeterminacionesentidades.verAsociados.sinDeterminacionesAsociada"));
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
				}
					
			}
		}
	}
	
	private void btnVerPropiedadesDetermiacion_actionPerformed(){
		
		DefaultMutableTreeNode node = getNodeSeleccionadoDeterminaciones();
		
		if (node != null) {
			
			Object nodeInfo = node.getUserObject();	
			if(nodeInfo instanceof DeterminacionPanelBean){

				FipPanelBean fip = (FipPanelBean)aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO);
				showPropDeterminacionMainPanel();
				
			}
		}
	}
	
	private void btnVerPropiedadesEntidad_actionPerformed(){
		DefaultMutableTreeNode node = getNodeSeleccionadoEntidades();
		if (node != null) {
		
			Object nodeInfo = node.getUserObject();	
			if(nodeInfo instanceof EntidadPanelBean){

				//EntidadesUtils.inicializarIdiomaEntidadesPanels();
				 
				FipPanelBean fip = (FipPanelBean)aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO);
				showPropEntidadMainPanel();
				
				
			}
		}
	}
	
	
	public void showPropEntidadMainPanel()
	{	
		EntidadPanelBean epb = null;
		for(int i=0; i<getEntidadesTreePlaneamientoVigente().getSelectionPaths().length; i++){
			DefaultMutableTreeNode nodeEntidad = (DefaultMutableTreeNode)getEntidadesTreePlaneamientoVigente().getSelectionPaths()[i].getLastPathComponent();	
			Object nodeInfoEntidad = nodeEntidad.getUserObject();	
			epb = (EntidadPanelBean)nodeInfoEntidad;
		}

		VerPropiedadesEntidadDialog verPropiedadesEntidadDialog = 
				new VerPropiedadesEntidadDialog(epb, aplicacion);

		verPropiedadesEntidadDialog.show();

	}
	
	
	public void showPropDeterminacionMainPanel(){
		DeterminacionPanelBean dpb = null;
		for(int i=0; i<getDeterminacionesTreePlaneamientoVigente().getSelectionPaths().length; i++){
			DefaultMutableTreeNode nodeDeterminacion = (DefaultMutableTreeNode)getDeterminacionesTreePlaneamientoVigente().getSelectionPaths()[i].getLastPathComponent();	
			Object nodeInfoDeterminacion = nodeDeterminacion.getUserObject();	
			dpb = (DeterminacionPanelBean)nodeInfoDeterminacion;
		}

		VerPropiedadesDeterminacionDialog verPropiedadesDeterminacionDialog = 
				new VerPropiedadesDeterminacionDialog(dpb, aplicacion);
	
		verPropiedadesDeterminacionDialog.show();
	
	}
	
	
	
	
	
	
	
	
	private void buscarEntidadYAsociarDatosGraficos(EntidadPanelBean[] lstEntidadPanelBean,
			Object nodeInfo, int idFeature, int idLayerDataBase){
		
		if(lstEntidadPanelBean != null ){
			for(int i=0; i<lstEntidadPanelBean.length; i++){
			
				if(lstEntidadPanelBean[i].getId() ==
					((EntidadPanelBean)nodeInfo).getId()){
					lstEntidadPanelBean[i].setIdFeature(idFeature);
					lstEntidadPanelBean[i].setIdLayer(idLayerDataBase);
					lstEntidadPanelBean[i].setModificada(true);
				}
				else if(lstEntidadPanelBean[i].getLstEntidadesHijas() != null &&
						lstEntidadPanelBean[i].getLstEntidadesHijas().length != 0){
					
					buscarEntidadYAsociarDatosGraficos(lstEntidadPanelBean[i].getLstEntidadesHijas(),
							nodeInfo, idFeature, idLayerDataBase);
				}
			}
		}
	}
	
	
	public void seleccionarEntidadTreeToMapa(JTree treeEntidadesSeleccionado){
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)  treeEntidadesSeleccionado.getLastSelectedPathComponent();
		
		GeopistaEditor geopistaEditor = (GeopistaEditor)aplicacion.getBlackboard().get(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES);
		if (node != null) {
			
			
			Object nodeInfo = node.getUserObject();	
			if(nodeInfo instanceof EntidadPanelBean){
				

				EntidadPanelBean entidad = (EntidadPanelBean)nodeInfo;
				int idLayer = entidad.getIdLayer();
				int idFeature = entidad.getIdFeature();
				if(idFeature > 0 && idLayer >0){
					//se selecciona la geometria en el mapa.
					refreshFeatureSelection(idFeature, idLayer);

				}
				else{
					// la entidad no tiene geometria asociada

					geopistaEditor.getSelectionManager().clear();

				}
				
			}
			else{
				geopistaEditor.getSelectionManager().clear();

			}
		}
		else{
			geopistaEditor.getSelectionManager().clear();
		}
	}

	
	
	 /**
     * Metodo que hace zoom en el mapa a la lista de ids de features que recibe como parametro
     * @param featuresId
     */
    public void refreshFeatureSelection(Object featuresId, Object layersId) {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			GeopistaEditor geopistaEditor = (GeopistaEditor)aplicacion.getBlackboard().get(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES);
			
			geopistaEditor.getSelectionManager().clear();
		

			Iterator iterLayer = geopistaEditor.getLayerManager().getLayers().iterator();
          
			for (Iterator iterator = geopistaEditor.getLayerManager().getLayers().iterator(); iterator.hasNext();) {
				GeopistaLayer geopistaLayer = (GeopistaLayer) iterator.next();
				
				
	            if (!(geopistaLayer instanceof DynamicLayer)){
		            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
		            while (allFeatures.hasNext()) {
		                Feature feature= (Feature)allFeatures.next();
	                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(String.valueOf(featuresId)) && 
	                    		geopistaLayer.getId_LayerDataBase() == (Integer.parseInt(String.valueOf(layersId)))){
	                        geopistaEditor.select(geopistaLayer, feature);

	                    }
		                
		            }
	            }else{
	            	java.util.List listaFeatures = new ArrayList();
	      
	        		if (geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId)))
	        			listaFeatures.add(featuresId);
	
	            	if (listaFeatures.size() > 0)
	            		selectDynamicFeatures(geopistaLayer, listaFeatures.toArray());
	            }
            
            
	            geopistaEditor.zoomToSelected();
			}

		} catch (Exception ex) {
			//logger.error("Exception: " ,ex);
			System.out.println(ex);
		}finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

	}
    
    /**
     * Mï¿½todo que selecciona features en capas dinï¿½micas
     */
    private void selectDynamicFeatures(GeopistaLayer geopistaLayer, Object[] featuresId){
        GeopistaConnection geopistaConnection = (GeopistaConnection) geopistaLayer.getDataSourceQuery().getDataSource().getConnection();
    	CoordinateSystem inCoord = geopistaLayer.getLayerManager().getCoordinateSystem();
        DriverProperties driverProperties = geopistaConnection.getDriverProperties();
    	driverProperties.put("srid_destino",inCoord.getEPSGCode());
    	java.util.List listaFeatures = geopistaConnection.loadFeatures(geopistaLayer,featuresId);
    	Iterator itFeatures = listaFeatures.iterator();
    	while (itFeatures.hasNext()){
    		GeopistaEditor geopistaEditor = (GeopistaEditor)aplicacion.getBlackboard().get(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES);
    		geopistaEditor.select(geopistaLayer, (GeopistaFeature)itFeatures.next());
    	}
    }
	
	public JTree getDeterminacionesTreePlaneamientoVigente() {
		return determinacionesTreePlaneamientoVigente;
	}

	public void setDeterminacionesTreePlaneamientoVigente(JTree determinacionesTreePlaneamientoVigente) {
		this.determinacionesTreePlaneamientoVigente = determinacionesTreePlaneamientoVigente;
	}
	
	public void setEntidadesJPanelTreePlaneamientoVigente(EntidadesPanelTree entidadesJPanelTreePlaneamientoVigente) {
		this.entidadesJPanelTreePlaneamientoVigente = entidadesJPanelTreePlaneamientoVigente;
	}

	public JTree getEntidadesTreePlaneamientoVigente() {
		return entidadesTreePlaneamientoVigente;
	}

	public JPanel getArbolesDeterminacionesPanel() {
		
		 if(arbolesDeterminacionesPanel == null){
			  
			 arbolesDeterminacionesPanel = new JPanel();
			 arbolesDeterminacionesPanel.setLayout(new GridBagLayout());
		 
			 arbolesDeterminacionesPanel.add(getDeterminacionesJScrollPanePlaneamientoVigente(), 
		                new GridBagConstraints(0, 0, 1, 1, 1, 0.3,
		                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                        new Insets(0, 1, 0, 0), 0, 0));		 
			 
		 }
		return arbolesDeterminacionesPanel;
	}

	public void setArbolesDeterminacionesPanel(JPanel arbolesDeterminacionesPanel) {
		this.arbolesDeterminacionesPanel = arbolesDeterminacionesPanel;
	}

	public JPanel getArbolesEntidadesPanel() {
		
		 if(arbolesEntidadesPanel == null){
			  
			 arbolesEntidadesPanel = new JPanel();
			 arbolesEntidadesPanel.setLayout(new GridBagLayout());
	 
			 arbolesEntidadesPanel.add(getEntidadesJScrollPanePlaneamientoVigente(),
		                new GridBagConstraints(0, 0, 1, 1, 1, 0.3,
		                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                        new Insets(0, 1, 0, 0), 0, 0));		 
		 }
		return arbolesEntidadesPanel;
	}

	public void setArbolesEntidadesPanel(JPanel arbolesEntidadesPanel) {
		this.arbolesEntidadesPanel = arbolesEntidadesPanel;
	}
	

	public DefaultMutableTreeNode getNodeSeleccionadoDeterminaciones() {
		return nodeSeleccionadoDeterminaciones;
	}

	public void setNodeSeleccionadoDeterminaciones(
			DefaultMutableTreeNode nodeSeleccionadoDeterminaciones) {
		this.nodeSeleccionadoDeterminaciones = nodeSeleccionadoDeterminaciones;
	}

	public DefaultMutableTreeNode getNodeSeleccionadoEntidades() {
		return nodeSeleccionadoEntidades;
	}

	public void setNodeSeleccionadoEntidades(
			DefaultMutableTreeNode nodeSeleccionadoEntidades) {
		this.nodeSeleccionadoEntidades = nodeSeleccionadoEntidades;
	}


	public String getArboSeleccionadoDeterminaciones() {
		return arboSeleccionadoDeterminaciones;
	}

	public void setArboSeleccionadoDeterminaciones(
			String arboSeleccionadoDeterminaciones) {
		this.arboSeleccionadoDeterminaciones = arboSeleccionadoDeterminaciones;
	}

	public String getArboSeleccionadoEntidades() {
		return arboSeleccionadoEntidades;
	}

	public void setArboSeleccionadoEntidades(String arboSeleccionadoEntidades) {
		this.arboSeleccionadoEntidades = arboSeleccionadoEntidades;
	}

}
