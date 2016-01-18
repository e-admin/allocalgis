package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.panels.ExportCuadrosMPTFilePanel;
import com.geopista.app.eiel.panels.ExportMPTPanel;
import com.geopista.app.eiel.panels.ValidateMPTPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



public class ExportCuadrosMPTFileDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(ExportCuadrosMPTFileDialog.class);
	private ExportCuadrosMPTFilePanel exportCuadrosMPTFilePanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private String pathreport = "";
    private String fase = "";

	

	public static final int DIM_X = 800;
    public static final int DIM_Y = 500;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
                if (_okCancelPanel.wasOKPressed()){  
                	final String anioEncuesta=exportCuadrosMPTFilePanel.getAnioEncuesta();
                	final String path=exportCuadrosMPTFilePanel.getDirectorySelected();   
                	if(path!=null && !path.equals("") && anioEncuesta!=null && !anioEncuesta.equals("") && anioEncuesta.length() == 4){		          	
                		exportCuadrosMPTFilePanel.setVisible(false);
		    					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		    					progressDialog.setTitle("TaskMonitorDialog.Wait");
		    					progressDialog.addComponentListener(new ComponentAdapter()
		    					{
		    						public void componentShown(ComponentEvent e)
		    						{
		    							new Thread(new Runnable()
		    							{
		    								public void run()
		    								{
		    									
		    									try
		    									{       
		    										dispose();
		    					    										
		    										pathreport = exportCuadrosMPTFilePanel.getDirectorySelected() + File.separator;
		    										fase = anioEncuesta; 
		    	
		    									}
		    									catch(Exception e)
		    									{
		    										logger.error("Error ", e);
		    										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
		    										return;
		    									}
		    									finally
		    									{
		    										progressDialog.setVisible(false);                                
		    										progressDialog.dispose();								
		    										dispose();
		    									}
		    								}
		    							}).start();
		    						}
		    					});
		    					GUIUtil.centreOnWindow(progressDialog);
		    					progressDialog.setVisible(true);
		
		    					show();
			                	}else{
			                		JOptionPane.showMessageDialog(
			                                ExportCuadrosMPTFileDialog.this,
			                                I18N.get("LocalGISEIEL", "localgiseiel.mensajes.mptdatosnocorrectos"),
			                                null,
			                                JOptionPane.INFORMATION_MESSAGE);
			                	}					
		                    }else{//If press Cancel
		                    	dispose();
		                    }
                	
                }
            });        	
            
    }//ok
        return _okCancelPanel;
}
    
    
/* CONSTRUCTORES */    
          
    public ExportCuadrosMPTFileDialog(){
        	 this(ConstantesLocalGISEIEL.RUTA_EXPORT_MPT);
    }
    
    public ExportCuadrosMPTFileDialog(String rutaFichero){
    	 super(AppContext.getApplicationContext().getMainFrame());
    	 initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT"));         
        this.setLocationRelativeTo(null);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title){
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        this.setModal(true);
        this.setSize(DIM_X, DIM_Y);
        this.setContentPane(getExportCuadrosMPTFilePanelPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
        this.setResizable(false);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        ExportCuadrosMPTFileDialog dialog = new ExportCuadrosMPTFileDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public ExportCuadrosMPTFilePanel getExportCuadrosMPTFilePanelPanel(){
        if (exportCuadrosMPTFilePanel == null){
        	exportCuadrosMPTFilePanel = new ExportCuadrosMPTFilePanel(new GridBagLayout());

        	exportCuadrosMPTFilePanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return exportCuadrosMPTFilePanel;
    }
	public String getPathreport() {
		return pathreport;
	}


	public void setPathreport(String pathreport) {
		this.pathreport = pathreport;
	}
	public String getFase() {
		return fase;
	}


	public void setFase(String fase) {
		this.fase = fase;
	}



}
