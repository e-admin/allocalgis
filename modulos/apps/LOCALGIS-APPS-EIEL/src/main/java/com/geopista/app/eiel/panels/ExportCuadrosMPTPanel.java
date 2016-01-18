/**
 * ExportCuadrosMPTPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.dialogs.ExportMPTDialog;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.plugin.Constantes;
import com.geopista.server.database.cuadros.CuadrosMPTBean;
import com.geopista.server.database.cuadros.Poblamiento_bean;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class ExportCuadrosMPTPanel extends JPanel{

	private JFileChooser fileChooser;   
	private JLabel jLabelTitle = null;
	private JRadioButton municipioJRadioButton = null;
	private JRadioButton nucleoJRadioButton = null;
	private ButtonGroup group = new ButtonGroup();
	private JTable jTableMunicipios;
	private JTable jTableCuadros;
	
	private JScrollPane jScrollPaneMunicipio;
	private JScrollPane jScrollPaneCuadro;
	
	private DefaultTableModel datosMunicipios ;
	private DefaultTableModel datosCuadros ;
	
	private ArrayList lstCuadrosTxt = new ArrayList();
	private ArrayList lstMunicipios= new ArrayList();
	private ArrayList lstPoblamientos = new ArrayList();
	private ArrayList  lstCuadrosBBDD = null;

	private JLabel jLabelFase = null;
	private JTextField jFieldFase = null;
	
	private LocalGISEIELClient eielClient;
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public ExportCuadrosMPTPanel(GridBagLayout layout){
		 super(layout);
		 
		 //eielClient= new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + "/EIELServlet");
		 eielClient = new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) +	WebAppConstants.EIEL_WEBAPP_NAME+ ServletConstants.EIEL_SERVLET_NAME);

		 obtenerPoblamientos();
		 rellenarCuadros();
	     initialize();
	}

	 private void initialize(){      
	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
	        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
	        
	        jLabelFase = new JLabel("");
	        jLabelFase.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.anioencuesta"));  
	        
	        this.setName(I18N.get("LocalGISEIEL","localgiseiel.mpt.validate.panel.title"));
	        
	        this.setLayout(new GridBagLayout());
	        this.setSize(ExportMPTDialog.DIM_X,ExportMPTDialog.DIM_Y);
	        jLabelTitle = new JLabel("", JLabel.LEFT);
	        jLabelTitle.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.exportcuadros.title"));       
	        
	        this.add(jLabelTitle,  
	        		new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
	                new Insets(5, 20, 0, 5), 0, 0));
	       
	    	this.add(getMunicipioJRadioButton(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 1, 0.1,
	    	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	    	                new Insets(5, 20, 0, 5), 0, 0));
	    	
	    	this.add(getNucleoJRadioButton(),  
	        		new GridBagConstraints(0, 2, 1, 1, 1, 0.1,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	                new Insets(5, 20, 0, 5), 0, 0));
	    	
	    	this.add(jLabelFase,  
	        		new GridBagConstraints(1, 1, 1, 1, 1, 0.1,
	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	                new Insets(5, 20, 0, 5), 0, 0));
    	
    	
	    	this.add(getJTextFieldFase(),  
	    	        		new GridBagConstraints(1, 2, 1, 1, 1.0, 0.1,
	    	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	    	                new Insets(5, 20, 0, 5), 0, 0));
	    	
	    	this.add(getjScrollPaneMunicipio(),
	        		new GridBagConstraints(0, 3, 1, 1, 1, 0.1,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	                new Insets(5, 20, 0, 5), 0, 0));
	    	
	    	this.add(getjScrollPaneCuadro(),  
	        		new GridBagConstraints(1, 3, 1, 1, 1,0.9,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	                new Insets(5, 20, 0, 5), 0, 0));
	    	
	    	group.add(getMunicipioJRadioButton());
	    	group.add(getNucleoJRadioButton());
	 }
	 
	 private JFileChooser getFileChooser() {
	        if (fileChooser == null) {
	            fileChooser = new GUIUtil.FileChooserWithOverwritePrompting();
	             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT.save"));
	            GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	            filter.addExtension("txt");
	            filter.setDescription("Texto");
	    		fileChooser.setFileHidingEnabled(false);
	    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            fileChooser.setFileFilter(filter);
	            File currentDirectory = (File) aplicacion.getBlackboard().get(Constantes.LAST_IMPORT_DIRECTORY);
	            fileChooser.setCurrentDirectory(currentDirectory);
	        }
	        return (JFileChooser) fileChooser;
	    }
	    
	

	 public String getDirectorySelected() {
		 String directory=null;
		 File file=null;
		 if(getFileChooser().getSelectedFile()!=null){
			 file=getFileChooser().getSelectedFile();
			 directory=file.getAbsolutePath();
			 aplicacion.getBlackboard().put(Constantes.LAST_IMPORT_DIRECTORY,file);
			 
		 }
		 
		return directory;
	}
	 
	 
	public JRadioButton getMunicipioJRadioButton() {
		if(municipioJRadioButton == null){
			municipioJRadioButton = new JRadioButton(I18N.get("LocalGISEIEL","localgiseiel.mpt.exportcuadros.panel.municipios"));
			municipioJRadioButton.setSelected(true);
			
			municipioJRadioButton.addActionListener(new ActionListener()
		      {
		        public void actionPerformed(ActionEvent e)
		        {
		          municipioSelected_actionPerformed(e);
		        }
		      });
		}
		return municipioJRadioButton;
	}

	public void setMunicipioJRadioButton(JRadioButton municipioJRadioButton) {
		this.municipioJRadioButton = municipioJRadioButton;
	}

	public JRadioButton getNucleoJRadioButton() {
		if(nucleoJRadioButton == null){
			nucleoJRadioButton = new JRadioButton(I18N.get("LocalGISEIEL","localgiseiel.mpt.exportcuadros.panel.nucleos"));

			nucleoJRadioButton.addActionListener(new ActionListener()
		      {
		        public void actionPerformed(ActionEvent e)
		        {
		         nucleoSelected_actionPerformed(e);
		        }
		      });
			
		}
		return nucleoJRadioButton;
	}

	public void setNucleoJRadioButton(JRadioButton nucleoJRadioButton) {
		this.nucleoJRadioButton = nucleoJRadioButton;
	}
	
	public JTable getjTableMunicipios() {
		if(jTableMunicipios == null){

			 String [] columnNames = {""}; 
			 
			 Object [][] data=null;
			 
			 //TODO. Habria que parametrizar el tema del codigo de entidad;
			 List municipios=AppContext.getAlMunicipios();			 
			 if ((municipios==null) || (AppContext.getAlMunicipios().size()==0)){
				 try {
					 int codProvincia=33;
					Collection listaMunicipios=eielClient.getMunicipiosEIEL(codProvincia);
					
					data = new Object[listaMunicipios.size()][1];
					Iterator it=listaMunicipios.iterator();
					int i=0;
					while (it.hasNext()){
						LCGMunicipioEIEL municipio=(LCGMunicipioEIEL)it.next();
						 data[i][0] = municipio.getNombreOficial();
						 i++;
					}
				} catch (Exception e) {
				}
			 }
			 else{
				 
				 data = new Object[AppContext.getAlMunicipios().size()][1];
		    
				 for(int i=0; i< AppContext.getAlMunicipios().size(); i++){
				    data[i][0] = ((Municipio)AppContext.getAlMunicipios().get(i)).getNombreOficial();			
				  }
			 }
    
			datosMunicipios = new tableDefaultTableModel(data,columnNames);
		    jTableMunicipios = new JTable(datosMunicipios);
		    jTableMunicipios.setRowSelectionAllowed(true);
			jTableMunicipios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTableMunicipios.setPreferredScrollableViewportSize(new Dimension(100, 100));
			jTableMunicipios.setEnabled(true);
			
		}
		return jTableMunicipios;
	}

	public void setjTableMunicipios(JTable jTableMunicipios) {
		this.jTableMunicipios = jTableMunicipios;
	}

	public JTable getjTableCuadros() {
		if(jTableCuadros == null){
	    
		    String [] columnNames = {""}; 
		    Object [][] data = new Object[80][1];
    
		    for(int i=0; i< lstCuadrosTxt.size(); i++){
		    	data[i][0] = (String)lstCuadrosTxt.get(i);			
		   }

		    datosCuadros = new tableDefaultTableModel(data,columnNames);
		    jTableCuadros = new JTable(datosCuadros);
			jTableCuadros.setRowSelectionAllowed(true);
			jTableCuadros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    jTableCuadros.setPreferredScrollableViewportSize(new Dimension(100, 100));
		    jTableCuadros.setEnabled(true);
		}
		return jTableCuadros;
	}

	public void setjTableCuadros(JTable jTableCuadros) {
		this.jTableCuadros = jTableCuadros;
	}
	
	public JScrollPane getjScrollPaneMunicipio() {
		
		if(jScrollPaneMunicipio == null){
			jScrollPaneMunicipio = new JScrollPane(getjTableMunicipios());
			jScrollPaneMunicipio.setPreferredSize(new Dimension(100, 100));		
			
		}
		return jScrollPaneMunicipio;
	}

	public void setjScrollPaneMunicipio(JScrollPane jScrollPaneMunicipio) {
		this.jScrollPaneMunicipio = jScrollPaneMunicipio;
	}

	public JScrollPane getjScrollPaneCuadro() {
		if(jScrollPaneCuadro == null){
			 jScrollPaneCuadro = new JScrollPane(getjTableCuadros());
			 jScrollPaneCuadro.setPreferredSize(new Dimension(100, 100));
		}
		return jScrollPaneCuadro;
	}

	public void setjScrollPaneNucleo(JScrollPane jScrollPaneCuadro) {
		this.jScrollPaneCuadro = jScrollPaneCuadro;
	}

	public boolean getRadioSelected(){
		boolean radioMunicipio = true;
		if(municipioJRadioButton.isSelected()){
			radioMunicipio = true;  
		}
		else{
			radioMunicipio = false; 
		}
		return radioMunicipio;
	}
	
	public ArrayList getLstMunicipiosSelected(){
		ArrayList lstIdMuniSelected = new ArrayList();
		int[] municipiosSelected = jTableMunicipios.getSelectedRows();

		if(getMunicipioJRadioButton().isSelected()){
			for (int i= 0; i<municipiosSelected.length; i++){
				lstIdMuniSelected.add(AppContext.getAlMunicipios().get(municipiosSelected[i]));
			}
		}
		else{
			for (int i= 0; i<municipiosSelected.length; i++){

				lstIdMuniSelected.add(lstPoblamientos.get(municipiosSelected[i]));
			}
		}
	
		return lstIdMuniSelected;
	}
		
		
	public ArrayList getLstNucleosSelected(){
		ArrayList lstIdMuniSelected = new ArrayList();
		int[] municipiosSelected = jTableMunicipios.getSelectedRows();

		if(municipioJRadioButton.isSelected()){
			for (int i= 0; i<municipiosSelected.length; i++){
				
				lstIdMuniSelected.add(((Municipio)AppContext.getAlMunicipios().get(i)).getId());
			}
		}
		else{
			for (int i= 0; i<municipiosSelected.length; i++){
				
				lstIdMuniSelected.add(((Municipio)AppContext.getAlMunicipios().get(i)).getId());
			}
		}
	
		return lstIdMuniSelected;
	}
	
	public ArrayList getLstCuadrosSelected(){
		ArrayList lstCuadrosSelected = new ArrayList();
		int[] cuadrosSelected = jTableCuadros.getSelectedRows();
		
		for (int i= 0; i<cuadrosSelected.length; i++){
			lstCuadrosSelected.add(lstCuadrosBBDD.get(cuadrosSelected[i]));
		}
		return lstCuadrosSelected;
	}

	private void municipioSelected_actionPerformed(ActionEvent e){
		
		//modificamos la tabla de muncipios para mostrar solo los municipios
		String [] columnNamesMunicipios = {""}; 
	    Object [][] dataMunicipios = new Object[AppContext.getAlMunicipios().size()][1];

	    for(int i=0; i< AppContext.getAlMunicipios().size(); i++){
	    	dataMunicipios[i][0] = ((Municipio)AppContext.getAlMunicipios().get(i)).getNombreOficial();		
	    }
		jTableMunicipios.setModel(new DefaultTableModel(dataMunicipios,columnNamesMunicipios));
		jTableMunicipios.setRowSelectionAllowed(true);
		jTableMunicipios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jTableMunicipios.setPreferredScrollableViewportSize(new Dimension(100, 100));
		
			
		// modificamos la tabla de cuadros
		   String [] columnNames = {""}; 
		    Object [][] data = new Object[80][1];
   
		    for(int i=0; i< lstCuadrosTxt.size(); i++){
		    	data[i][0] = (String)lstCuadrosTxt.get(i);			
		   }

		    jTableCuadros.setModel(new DefaultTableModel(data,columnNames));
			jTableCuadros.setRowSelectionAllowed(true);
			jTableCuadros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    jTableCuadros.setPreferredScrollableViewportSize(new Dimension(100, 100));
		
		
	}
		
	private void nucleoSelected_actionPerformed(ActionEvent e){
		
			String [] columnNamesPoblamientos = {""}; 
		    Object [][] dataPoblamientos = new Object[lstPoblamientos.size()][1];
	
		    for(int i=0; i< lstPoblamientos.size(); i++){
		    	Poblamiento_bean poblamientoBean = (Poblamiento_bean)lstPoblamientos.get(i);
		    	String nombreMuni = "";
		    	if(poblamientoBean.getDenominacion() != null && !poblamientoBean.getDenominacion().equals("null")){
		    		nombreMuni = poblamientoBean.getDenominacion();
		    	}
		    	
		    	dataPoblamientos[i][0] = poblamientoBean.getMunicipio()+
		    			poblamientoBean.getEntidad()+poblamientoBean.getPoblamient() + " - " + nombreMuni;
		    }
			jTableMunicipios.setModel(new DefaultTableModel(dataPoblamientos,columnNamesPoblamientos));
			jTableMunicipios.setRowSelectionAllowed(true);
			jTableMunicipios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jTableMunicipios.setPreferredScrollableViewportSize(new Dimension(100, 100));
			
		//modificamos la tabla de cuadros
		   String [] columnNames = {""}; 
		    Object [][] data = new Object[66][1];
   
		    for(int i=0; i<=65; i++){
		    	data[i][0] = (String)lstCuadrosTxt.get(i);			
		   }
			   
		    jTableCuadros.setModel(new DefaultTableModel(data,columnNames));

		    //jTableCuadros = new JTable(datosCuadros);
			jTableCuadros.setRowSelectionAllowed(true);
			jTableCuadros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    jTableCuadros.setPreferredScrollableViewportSize(new Dimension(100, 100));

	}
		
	private void obtenerPoblamientos(){
		try {
		
			ArrayList lstIdMunicipios = new ArrayList();
			String provincia = "";
			
			List municipios=AppContext.getAlMunicipios();
			if ((municipios!=null) && (municipios.size()>0)){
				for(int i=0; i<AppContext.getAlMunicipios().size(); i++){
					provincia= String.valueOf(((Municipio)AppContext.getAlMunicipios().get(i)).getId()).substring(0, 2);
					String id = String.valueOf(((Municipio)AppContext.getAlMunicipios().get(i)).getId()).substring(2, 
							String.valueOf(((Municipio)AppContext.getAlMunicipios().get(i)).getId()).length());
					lstIdMunicipios.add(id);
					
				}
			}
			else{
				provincia="33";
				Collection listaMunicipios=eielClient.getMunicipiosEIEL(Integer.parseInt(provincia));
								
				municipios=new ArrayList();
				Iterator it=listaMunicipios.iterator();
				while (it.hasNext()){
					LCGMunicipioEIEL municipio=(LCGMunicipioEIEL)it.next();
					Municipio municipioCatastro=new Municipio(String.valueOf(municipio.getIdMunicipio()),municipio.getNombreOficial(),"");
					municipios.add(municipioCatastro);
					String shortedMunicipio=String.valueOf(municipio.getIdMunicipio()).substring(2,5);
					lstIdMunicipios.add(shortedMunicipio);
				}
				AppContext.setAlMunicipios(municipios);
			}
			lstPoblamientos = InitEIEL.clienteLocalGISEIEL.obtenerPoblamientosMPT(String.valueOf(provincia) ,lstIdMunicipios);

		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void rellenarCuadros(){
		
		try {
			lstCuadrosBBDD =   InitEIEL.clienteLocalGISEIEL.obtenerCuadrosMPT();
			
			for (int i=0; i<lstCuadrosBBDD.size(); i++){
				if(i<= 65){
					int count = i+1;
					String nameCuadro = "(Cuadro"+count+") - "+((CuadrosMPTBean)lstCuadrosBBDD.get(i)).getNombre();
					lstCuadrosTxt.add(nameCuadro);
				}
				if(i>65){
					String nameCuadro = "(Cuadro"+((CuadrosMPTBean)lstCuadrosBBDD.get(i)).getNombre()+") - "+
							((CuadrosMPTBean)lstCuadrosBBDD.get(i)).getTabla().substring(2, ((CuadrosMPTBean)lstCuadrosBBDD.get(i)).getTabla().length());
					lstCuadrosTxt.add(nameCuadro);
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
	 private JTextField getJTextFieldFase()
		{
		 if (jFieldFase  == null)
			{
			 jFieldFase  = new JTextField();
			 jFieldFase.setText(UserPreferenceStore.getUserPreference("FASE","", true));
			 jFieldFase.addCaretListener(new CaretListener()
				{
					public void caretUpdate(CaretEvent evt)
					{
						EdicionUtils.chequeaLongYNumCampoEdit(jFieldFase, 4, aplicacion.getMainFrame());
					}
				});
			}
			return jFieldFase;
		}
	 
	 public String getAnioEncuesta() {
			String anio=getJTextFieldFase().getText();
			UserPreferenceStore.setUserPreference("FASE",anio);
			return anio;
	}

	 class tableDefaultTableModel extends DefaultTableModel {  
		 
		 public tableDefaultTableModel(Object[][] data, Object[] columnNames) {  
		   super(data, columnNames);  
		 }  
		 
		 public boolean isCellEditable(int row, int col) {  
		   return false;  
		 }  
	}  

}
