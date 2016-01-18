package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.eiel.dialogs.ExportMPTDialog;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.plugin.Constantes;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class ExportCuadrosMPTFilePanel extends JPanel{

	private JFileChooser fileChooser;   
	private JLabel jLabelFase = null;
	private JTextField jFieldFase = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public ExportCuadrosMPTFilePanel(GridBagLayout layout){
		 super(layout);
	        initialize();
	}

	 private void initialize(){      
	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
	        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
	        
	        this.setName(I18N.get("LocalGISEIEL","localgiseiel.mpt.validate.panel.title"));
	        
	        this.setLayout(new GridBagLayout());
	        this.setSize(ExportMPTDialog.DIM_X,ExportMPTDialog.DIM_Y);
	        jLabelFase = new JLabel("", JLabel.LEFT);
	        jLabelFase.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.anioencuesta"));       
	        
	        this.add(getFileChooser(), 
	        		new GridBagConstraints(1, 0, 1, 3, 0.1, 0.1,
	                GridBagConstraints.ABOVE_BASELINE, GridBagConstraints.BOTH,
	                new Insets(0, 5, 0, 5), 0, 0));
	        
	        this.add(jLabelFase,  
	        		new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
	                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
	                new Insets(-5, 20, 0, 5), 0, 0));
	    	this.add(getJTextFieldFase(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1,
	    	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	    	                new Insets(-25, 20, 0, 5), 0, 0));
	        
	 }
	 
	 private JFileChooser getFileChooser() {
	        if (fileChooser == null) {
	            fileChooser = new GUIUtil.FileChooserWithOverwritePrompting();
	             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT.save"));
	            GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	            filter.addExtension("pdf");
	            filter.setDescription("Pdf");
	    		fileChooser.setFileHidingEnabled(false);
	    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            fileChooser.setFileFilter(filter);
	            File currentDirectory = (File) aplicacion.getBlackboard().get(Constantes.LAST_IMPORT_DIRECTORY);
	            fileChooser.setCurrentDirectory(currentDirectory);
	        }
	        return (JFileChooser) fileChooser;
	    }
	    
	 private JTextField getJTextFieldFase()
		{
		 if (jFieldFase  == null)
			{
			 jFieldFase  = new JTextField();
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
		
		 return getJTextFieldFase().getText();
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
}
