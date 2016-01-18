/**
 * UpdatePadronSelectionPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.update;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.vividsolutions.jump.I18N;

public class UpdatePadronSelectionPanel extends JPanel{
	private JFileChooser fileChooser;   

	private JLabel jLabelAños = null;	
	private JComboBox cmbAños= null;

	private JLabel jLabelMunicipios = null;	
	private JComboBox cmbMunicipios= null;

	private JPanel panelActualizaciones=null;
	private JPanel panelUpdatePadron = null;
	
	
	private String añoPadron;
	
	private LCGMunicipioEIEL municipioSeleccionado;
	private LocalGISEIELClient eielClient;
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public UpdatePadronSelectionPanel(GridBagLayout layout){
		 super(layout);
		 
			//eielClient= new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + ServletConstants.EIEL_SERVLET_NAME);
			 eielClient = new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) +	WebAppConstants.EIEL_WEBAPP_NAME+ ServletConstants.EIEL_SERVLET_NAME);

		 cargarAños();
		 cargarMunicipios();
	     initialize();
	}

	public JPanel getPanelActualizaciones() {
		if(panelActualizaciones == null){
			panelActualizaciones = new JPanel();
			panelActualizaciones.setLayout(new GridBagLayout());
			panelActualizaciones.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			panelActualizaciones.add(getPanelUpdatePadron(),new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0));
		        		
		}
		return panelActualizaciones;
	}
	
	 private JFileChooser getFileChooser() {
			if (fileChooser == null) {
				fileChooser = new JFileChooser(UserPreferenceConstants.DEFAULT_DATA_PATH);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Directorio de resultados de actualizacion");
			}
			return fileChooser;
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
	 
	 public int abrirJFileChooser(){
		 return getFileChooser().showSaveDialog(this);
	 }

	private void initialize(){
		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.geopista.app.eiel.update.language.LocalGISUpdatePadroni18n",loc, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISUpdatePadron", bundle);
		 this.add(getPanelActualizaciones(),new GridBagConstraints(0, 0, 1, 1, 0.0001, 0.0001,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
	 }
	 
		 
	
	/**
	 * Mostramos el año actual, el año anterior y 5 años mas.
	 */
	private void cargarAños(){			
		cmbAños = getJCmbAños();
		cmbAños.removeAllItems();
				
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		for (int i=-2;i<5;i++){
			Calendar cal = Calendar.getInstance();
			int amount=(i*365);
			cal.add(Calendar.DATE, amount);
			cmbAños.addItem(dateFormat.format(cal.getTime()));	
		}
		cmbAños.setSelectedIndex(1);
		añoPadron=(String)cmbAños.getSelectedItem();
	}
	/**
	 * Mostramos el año actual, el año anterior y 5 años mas.
	 */
	private void cargarMunicipios(){		
		
		cmbMunicipios = getJCmbMunicipios();
		cmbMunicipios.removeAllItems();
		
		Collection<Object> c=null;
        try {
			c = eielClient.getMunicipiosEIEL(33);
			//c = eielClient.getMunicipiosEIEL();
		} catch (Exception e) {
			e.printStackTrace();
		}

        cmbMunicipios.addItem(new LCGMunicipioEIEL(0,""));
    	Iterator it=c.iterator();
    	while (it.hasNext()){
    		LCGMunicipioEIEL municipio=(LCGMunicipioEIEL)it.next();
    		cmbMunicipios.addItem(municipio);
    		
    	}
		
		cmbMunicipios.setSelectedIndex(0);
		municipioSeleccionado=(LCGMunicipioEIEL)cmbMunicipios.getSelectedItem();
		
		cmbMunicipios.setRenderer(new UbicacionListCellRenderer());
	}
	
	
	 private JComboBox getJCmbAños(){
		if (cmbAños == null) {
			cmbAños = new JComboBox();
			cmbAños.setBounds(new Rectangle(357, 51, 200, 20));

			cmbAños.setFocusable(false);
			
			cmbAños.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					añoPadron=(String)cmbAños.getSelectedItem();
					//exportSHP.setEntidad((Entidad)cmbEntidad.getSelectedItem());
					cmbAños_actionPerformed(e);					
				}
			});		
		}
		return cmbAños;
	}
	 
	 private JComboBox getJCmbMunicipios(){
			if (cmbMunicipios == null) {
				cmbMunicipios = new JComboBox();
				cmbMunicipios.setBounds(new Rectangle(357, 51, 200, 20));

				cmbMunicipios.setFocusable(false);
				
				cmbMunicipios.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						municipioSeleccionado=(LCGMunicipioEIEL)cmbMunicipios.getSelectedItem();				
					}
				});		
			}
			return cmbMunicipios;
		}
	
	 /**
	  * Devuelve el año del padron
	  * @return
	  */
	 public String getAñoPadron(){
		 return añoPadron;
	 }
	
	 public LCGMunicipioEIEL getMunicipioSeleccionado(){
		 return municipioSeleccionado;
	 }
	 
	 private void cmbAños_actionPerformed(ActionEvent e) {			
           
	}

	 public JPanel getPanelUpdatePadron() {
			if(panelUpdatePadron == null){
				panelUpdatePadron = new JPanel();
				panelUpdatePadron.setLayout(new GridBagLayout());
				
		        jLabelAños = new JLabel("");
		        jLabelAños.setText(I18N.get("LocalGISUpdatePadron", "update.padron.agno"));  
		        
		        panelUpdatePadron.add(jLabelAños,  
		        		new GridBagConstraints(0, 0, 1, 1, 0, 0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0));
	    	
	    	
		        panelUpdatePadron.add(getJCmbAños(),  
		    	        		new GridBagConstraints(0, 1, 1, 1, 1, 0,
		    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		    	                new Insets(5, 5, 0,0), 0, 10));
		        
		        jLabelMunicipios = new JLabel("");
		        jLabelMunicipios.setText(I18N.get("LocalGISUpdatePadron", "update.padron.municipios"));  
		        
		        panelUpdatePadron.add(jLabelMunicipios,  
		        		new GridBagConstraints(0, 2, 1, 1, 0, 0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0));
	    	
	    	
		        panelUpdatePadron.add(getJCmbMunicipios(),  
		    	        		new GridBagConstraints(0, 3, 1, 1, 1, 0,
		    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		    	                new Insets(5, 5, 0,0), 0, 10));
			}
			return panelUpdatePadron;
		}

		public void setPanelUpdatePadron(JPanel panelUpdatePadron) {
			this.panelUpdatePadron = panelUpdatePadron;
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