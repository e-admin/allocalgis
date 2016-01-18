package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.plugin.Constantes;
import com.vividsolutions.jump.I18N;

public class ValidateMPTPanel extends JPanel{

	private JFileChooser fileChooser;   
	private JLabel jLabelFase = null;
	private JTextField jFieldFase = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public ValidateMPTPanel(GridBagLayout layout){
		 super(layout);
	        initialize();
	}

	 private void initialize(){      
	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
	        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
	        
	        this.setName(I18N.get("LocalGISEIEL","localgiseiel.mpt.validate.panel.title"));
	        
	        this.setLayout(new GridBagLayout());
	        //this.setSize(ValidateMPTDialog.DIM_X,ValidateMPTDialog.DIM_Y);
	        jLabelFase = new JLabel("");
	        jLabelFase.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.anioencuesta"));       
        
	        this.add(jLabelFase,  
	        		new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	                new Insets(5, 20, 0, 5), 0, 0));
	    	this.add(getJTextFieldFase(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1,
	    	                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	    	                new Insets(5, 20, 0, 5), 0, 0));
	        
	 }
	 
	 public JFileChooser getFileChooser() {
        if (fileChooser == null) {
        	fileChooser = new JFileChooser();
             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.title"));
             GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	            filter.addExtension("txt");
	            filter.setDescription("Texto");
	    		fileChooser.setFileHidingEnabled(false);
	    		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            fileChooser.setFileFilter(filter);
	            
	         // Se abre el fichero donde se hará la copia
	            Calendar calendar = new GregorianCalendar();

            	// Se abre el fichero donde se hará la copia
				String currentdate = calendar.get(Calendar.DATE)+"-"+
				(calendar.get(Calendar.MONTH)+1)+"-"+
				calendar.get(Calendar.YEAR);
				
				fileChooser.setSelectedFile(new File(File.separator+ConstantesLocalGISEIEL.FILENAME_VALIDACION_MPT+"_"+currentdate));
            //fileChooser.showOpenDialog(this);
        }
        File currentDirectory = (File) aplicacion.getBlackboard().get(Constantes.LAST_IMPORT_DIRECTORY);
        fileChooser.setCurrentDirectory(currentDirectory);
        return (JFileChooser) fileChooser;
	   }
	 public int abrirJFileChooser(){
		 return getFileChooser().showSaveDialog(this);
	 }
	    
	 private JTextField getJTextFieldFase()
		{
		 if (jFieldFase  == null)
			{
			 jFieldFase  = new JTextField();
			 jFieldFase.setText(AppContext.getApplicationContext().getUserPreference("FASE","", true));
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
			AppContext.getApplicationContext().setUserPreference("FASE",anio);
			return anio;
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
