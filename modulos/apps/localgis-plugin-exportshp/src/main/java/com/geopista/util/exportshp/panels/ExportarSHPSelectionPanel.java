/**
 * ExportarSHPSelectionPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.exportshp.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.entidades.ComboBoxRendererEntidad;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.model.ExportLayersBean;
import com.geopista.model.ExportLayersFamilyBean;
import com.geopista.model.ExportMapBean;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.ListaEntidades;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.util.exportshp.beans.ExportSHPBean;
import com.geopista.util.exportshp.renderer.ComboBoxRendererSistemaCoordenadas;
import com.geopista.util.exportshp.renderer.MapsListCellRenderer;
import com.geopista.util.exportshp.tree.CheckTreeManager;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;

public class ExportarSHPSelectionPanel extends JPanel{
	private JTree tree =null;
	private JFileChooser fileChooser;   
	private JLabel jLabelTitle = null;
	private List<CoordinateSystem> listaSistemasCoordenadas = null;
	private JScrollPane scrollTreePane = new JScrollPane();

	private JLabel jLabelMapa = null;	
	private JComboBox cmbMapa = null;
	private ExportSHPBean exportSHP = new ExportSHPBean();
	private JLabel jLabelEntidad = null;	
	private JComboBox cmbEntidad = null;
	private JLabel jLabelSistemaCoordenadas = null;	
	private JComboBox cmbSistemaCoordenadas = null;

	private JPanel panelExportarSHP = null;
	private JPanel panelExportaciones = null;
	
	private JLabel jLabelAños = null;	
	private JComboBox cmbAños= null;
	private String añoPadron;

	private CheckTreeManager checkTreeManager = null;

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public ExportarSHPSelectionPanel(GridBagLayout layout){
		 super(layout);
		 cargarEntidades();
		 cargarAños();
		 initializeTree();
	     initialize();
	}

	
	 public ExportSHPBean getExportSHP() {
		return exportSHP;
	}


	public void setExportSHP(ExportSHPBean exportSHP) {
		this.exportSHP = exportSHP;
	}


	private void initialize(){
	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.util.exportshp.language.LocalGISExportSHPi18n",loc,this.getClass().getClassLoader());
	        I18N.plugInsResourceBundle.put("LocalGISExportSHP",bundle);    
	        cargarSistemaCoordenadas();
	        this.add(getPanelExportaciones(),new GridBagConstraints(0, 0, 1, 1, 0.0001, 0.0001,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
	        
	 }
	 
	 private JFileChooser getFileChooser() {
			if (fileChooser == null) {
				fileChooser = new JFileChooser(UserPreferenceConstants.DEFAULT_DATA_PATH);
				fileChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			return fileChooser;
		}
	    
		public JPanel getPanelExportaciones() {
			if(panelExportaciones == null){
				panelExportaciones = new JPanel();
				panelExportaciones.setLayout(new GridBagLayout());
				panelExportaciones.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
				panelExportaciones.add(getPanelExportarSHP(),new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1,
			                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			                new Insets(5, 5, 0, 5), 0, 0));
			        		
			}
			return panelExportaciones;
		}

	 public String getDirectorySelected() {
		 String directory=null;
		 File file=null;
		 if(getFileChooser().getSelectedFile()!=null){
			 file=getFileChooser().getSelectedFile();
			 directory=file.getAbsolutePath();
			 aplicacion.getBlackboard().put(com.geopista.plugin.Constantes.LAST_IMPORT_DIRECTORY,file);
			 
		 }
		 
		return directory;
	}
	 
	
	private void cargarEntidades(){			
		cmbEntidad = getJCmbEntidad();
		cmbEntidad.removeAllItems();
		ListaEntidades listaEntidades = new ListaEntidades();
		if (Constantes.idEntidad == 0){
			listaEntidades = new OperacionesAdministrador(Constantes.url).getListaEntidades();
		}
		else{
			try {
				//System.out.println("URL Acceso Entidad:"+Constantes.url);
				Entidad entidad = new OperacionesAdministrador(Constantes.url).getEntidad(Constantes.idEntidad+"");
				listaEntidades.add(entidad);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cmbEntidad.removeAllItems();
		cmbEntidad = listaEntidades.cargarJComboBox(cmbEntidad);
		cmbEntidad.setRenderer(new ComboBoxRendererEntidad());
		cmbEntidad.setSelectedIndex(0);
		exportSHP.setEntidad((Entidad)cmbEntidad.getSelectedItem());
	}
	
	
	 private JComboBox getJCmbEntidad(){
		if (cmbEntidad == null) {
			cmbEntidad = new JComboBox();
			cmbEntidad.setBounds(new Rectangle(357, 51, 200, 20));

			cmbEntidad.setFocusable(false);
			
			cmbEntidad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportSHP.setEntidad((Entidad)cmbEntidad.getSelectedItem());
					cmbEntidad_actionPerformed(e);					
				}
			});		
		}
		return cmbEntidad;
	}

	 private JComboBox getJCmbAños(){
			if (cmbAños == null) {
				cmbAños = new JComboBox();
				cmbAños.setBounds(new Rectangle(357, 51, 200, 20));

				cmbAños.setFocusable(false);
				
				cmbAños.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						añoPadron=(String)cmbAños.getSelectedItem();				
					}
				});		
			}
			return cmbAños;
		}
	
	 private JComboBox getJCmbMapa(){
		if (cmbMapa == null) {
			cmbMapa = new JComboBox();
			cmbMapa.setBounds(new Rectangle(357, 51, 200, 20));

			MapsListCellRenderer renderer = new MapsListCellRenderer();			
			cmbMapa.setRenderer(renderer);
			cmbMapa.setFocusable(false);
			
			cmbMapa.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportSHP.setExportMap((ExportMapBean)cmbMapa.getSelectedItem());
					cmbLayers_actionPerformed(e);	
				}
			});		
		}
		return cmbMapa;
	}
	 
	 private void cmbEntidad_actionPerformed(ActionEvent e) {			
            final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");

			cmbMapa = getJCmbMapa();
			cmbMapa.removeAllItems();
			try
            {
			    AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
			    	 sUrlPrefix + "/AdministradorCartografiaServlet");
			    /* 
			     * No permitimos que el usuario pueda obtener mapas de otra entidad, si lo quisieramos habria que descomentar esta
			     * linea y comentar la de String idEntidad = "0";
			     * 
			     * String idEntidad = exportSHP.getEntidad().getId();
			     */
			     
			    String idEntidad = "0";
			    ArrayList<ExportMapBean> mapas = administradorCartografiaClient.getMapas(I18N.getLocaleAsObject().getLanguage()+"_"+I18N.getLocaleAsObject().getCountry(), idEntidad);				
				Iterator<ExportMapBean> enumMapas = mapas.iterator();
				while(enumMapas.hasNext()){
					ExportMapBean currentMap = enumMapas.next();
					/* Como vamos a obligar a exportar solo capas del Mapa de la EIEL, nos quedamos solo con este Mapa, si descomentamos lo de idEntidad = 0
					* habria que quitar tambien lo de if (currentMap.getName().equals("Mapa EIEL Export SHP")):
					*/
					if (currentMap.getName().equals("Mapa EIEL Export SHP")) 
						cmbMapa.addItem(currentMap);
				}
            } catch (Exception e1)
            {
                e1.printStackTrace();
            }
		}

	 private void cmbLayers_actionPerformed(ActionEvent e) {	
         final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");

			try
         {
				if (exportSHP.getExportMap()!= null){
				    AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
				    	 sUrlPrefix + "/AdministradorCartografiaServlet");
				    exportSHP.setExportMap(administradorCartografiaClient.getLayersMapa(I18N.getLocaleAsObject().getLanguage()+"_"+I18N.getLocaleAsObject().getCountry(), exportSHP.getExportMap()));				
				    /* Si quitamos la restriccion de idEntidad = 0 y lo de quedarnos solo con el Mapa de la EIEL, habria que poner aqui para actualizar 
				    * el listado de capas al cambiar el mapa.
				    */
				}

         } catch (Exception e1)
         {
             e1.printStackTrace();
         }
		}
	 	  
	 public int abrirJFileChooser(){
		 return getFileChooser().showSaveDialog(this);
	 }
	 

	public JPanel getPanelExportarSHP() {
		if(panelExportarSHP == null){
			panelExportarSHP = new JPanel();
			panelExportarSHP.setLayout(new GridBagLayout());
			
	        jLabelMapa = new JLabel("");
	        jLabelMapa.setText(I18N.get("LocalGISExportSHP", "export.shp.map"));  
	        
	        jLabelEntidad = new JLabel("");
	        jLabelEntidad.setText(I18N.get("LocalGISExportSHP", "export.shp.entidad"));  
	        
	        jLabelSistemaCoordenadas = new JLabel("");
	        jLabelSistemaCoordenadas.setText(I18N.get("LocalGISExportSHP", "export.shp.sistemacoordenadas"));  
	        panelExportarSHP.setName(I18N.get("LocalGISExportSHP", "export.shp.entidad"));
	        
	        //Se incorpora la gestion de años
	        jLabelAños = new JLabel("");
	        jLabelAños.setText(I18N.get("LocalGISExportSHP", "export.shp.agno")); 
	        
	        panelExportarSHP.setLayout(new GridBagLayout());
	        jLabelTitle = new JLabel("", JLabel.LEFT);
	        jLabelTitle.setText(I18N.get("LocalGISExportSHP", "export.shp.label.layers"));       
    	
	        panelExportarSHP.add(jLabelEntidad,  
	        		new GridBagConstraints(0, 0, 1, 1, 20, 150,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
    	
    	
	        panelExportarSHP.add(getJCmbEntidad(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 20, 150,
	    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	    	                new Insets(5, 5, 0, 5), 0, 0));
	        
	        panelExportarSHP.add(jLabelAños,  
	        		new GridBagConstraints(0, 2, 1, 1, 20, 150,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));

	        panelExportarSHP.add(getJCmbAños(),  
	        		new GridBagConstraints(0, 3, 1, 1, 20, 150,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
	        
	        
	        panelExportarSHP.add(jLabelMapa,  
	        		new GridBagConstraints(0, 4, 2, 1, 20, 150,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
    	
    	
	        panelExportarSHP.add(getJCmbMapa(),  
	    	        		new GridBagConstraints(0, 5, 1, 1, 20, 150,
	    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	    	                new Insets(5, 5, 0, 5), 0, 0));
	    	
	        panelExportarSHP.add(jLabelSistemaCoordenadas,  
	        		new GridBagConstraints(0, 6, 1, 1, 20, 150,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
    	
    	
	        panelExportarSHP.add(getJCmbSistemaCoordenadas(),  
	    	        		new GridBagConstraints(0, 7, 1, 1, 20, 150,
	    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	    	                new Insets(5, 5, 0, 5), 0, 0));
	        
	        panelExportarSHP.add(jLabelTitle,  
		        		new GridBagConstraints(0, 8, 1, 1, 20, 150,
		                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0)); 
	        
	        panelExportarSHP.add(getScrollTreePane(),  
	        		new GridBagConstraints(0, 9, 1, 1, 1,0.9,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));

		}
		return panelExportarSHP;
	}

	public void setPanelExportarSHP(JPanel panelExportarSHP) {
		this.panelExportarSHP = panelExportarSHP;
	}
	
	class tableDefaultTableModel extends DefaultTableModel {  
		 
		 public tableDefaultTableModel(Object[][] data, Object[] columnNames) {  
		   super(data, columnNames);  
		 }  
		 
		 public boolean isCellEditable(int row, int col) {  
		   return false;  
		 }  
	}
	
	
	private void initializeTree(){
		
		 DefaultMutableTreeNode top = new DefaultMutableTreeNode("LAYERS");
		 createNodes(top);
		 HideableTreeModel ml = new HideableTreeModel( top );
		 tree = new JTree(ml);
	    scrollTreePane = new JScrollPane(tree);
	   
	    checkTreeManager = new CheckTreeManager(tree); 
	}
		
	private void createNodes(DefaultMutableTreeNode top){ 
		if (exportSHP.getExportMap() != null){
			Enumeration<ExportLayersFamilyBean> enumLayersSHP = exportSHP.getExportMap().getHtLayers().elements();
			while (enumLayersSHP.hasMoreElements()) {
				ExportLayersFamilyBean valor = (ExportLayersFamilyBean) enumLayersSHP.nextElement();
	        	HideableNode padre = new HideableNode(valor);
	        	top.add(padre);
	
	            Iterator<ExportLayersBean> iterador = valor.getExportSHPLayers().iterator();
	     
	            while (iterador.hasNext()){
	            	ExportLayersBean exportLayer = (ExportLayersBean) iterador.next();
	        		DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(exportLayer);
	        		padre.add(hijo);
	        	 }
	        
	        }
		}
    }
	
	 private JScrollPane getScrollTreePane(){
		if (scrollTreePane == null) {
			scrollTreePane = new JScrollPane();
			scrollTreePane.setPreferredSize(new Dimension(100,200));
			scrollTreePane.setFocusable(false);
	
		}
		return scrollTreePane;
	}
	 
	public CheckTreeManager getCheckTreeManager() {
		return checkTreeManager;
	}
	
	public void setCheckTreeManager(CheckTreeManager checkTreeManager) {
		this.checkTreeManager = checkTreeManager;
	}
	
	public void cargarSistemaCoordenadas(){ 
		cmbSistemaCoordenadas = getJCmbSistemaCoordenadas();
		cmbSistemaCoordenadas.removeAllItems();
		if (listaSistemasCoordenadas == null){
			listaSistemasCoordenadas = new ArrayList<CoordinateSystem>();
	        listaSistemasCoordenadas.add(new CoordinateSystem("Seleccione...", 0, null, 0,null));
	        listaSistemasCoordenadas.addAll(CoordinateSystemRegistry.instance(aplicacion.getBlackboard()).getCoordinateSystems());
	        for (int i = 0; i < listaSistemasCoordenadas.size(); i++) {
	            CoordinateSystem cS = (CoordinateSystem) listaSistemasCoordenadas.get(i);
	            if (cS.getName().equals("Unspecified")){
	                listaSistemasCoordenadas.remove(i);
	                break;
	            }               
	        }
		}	
		cmbSistemaCoordenadas.removeAllItems();
		Iterator<CoordinateSystem> itr = listaSistemasCoordenadas.iterator();

        while (itr.hasNext()) {
            cmbSistemaCoordenadas.addItem((CoordinateSystem)itr.next());
        } 
		cmbSistemaCoordenadas.setRenderer(new ComboBoxRendererSistemaCoordenadas());
		cmbSistemaCoordenadas.setSelectedIndex(0);
	}
	
	 private JComboBox getJCmbSistemaCoordenadas(){
		if (cmbSistemaCoordenadas == null) {
			cmbSistemaCoordenadas = new JComboBox();
			cmbSistemaCoordenadas.setBounds(new Rectangle(357, 51, 200, 20));

			cmbSistemaCoordenadas.setFocusable(false);
			
			cmbSistemaCoordenadas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportSHP.getExportMap().setSrid(((CoordinateSystem)cmbSistemaCoordenadas.getSelectedItem()).getEPSGCode());
				}
			});		
		}
		return cmbSistemaCoordenadas;
	}
	 
	 public String getAñoEncuesta(){
		 return añoPadron;
	 }
	 /**
		 * Mostramos el año actual, el año anterior y 5 años mas.
		 */
		private void cargarAños(){			
			cmbAños = getJCmbAños();
			cmbAños.removeAllItems();
					
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			for (int i=-4;i<5;i++){
				Calendar cal = Calendar.getInstance();
				int amount=(i*365);
				cal.add(Calendar.DATE, amount);
				cmbAños.addItem(dateFormat.format(cal.getTime()));	
			}
			cmbAños.setSelectedIndex(3);
			añoPadron=(String)cmbAños.getSelectedItem();
		}
}