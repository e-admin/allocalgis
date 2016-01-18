package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.panels.ExportMPTPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



public class ExportMPTDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(ExportMPTDialog.class);
	private ExportMPTPanel exportMPTPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    
    public static final int DIM_X = 200;
    public static final int DIM_Y = 180;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
                if (_okCancelPanel.wasOKPressed()){  
                	final String anioEncuesta=exportMPTPanel.getAnioEncuesta();
                	if(anioEncuesta!=null && !anioEncuesta.equals("") && anioEncuesta.length() == 4){
                		dispose();
                		if (exportMPTPanel.abrirJFileChooser() == JFileChooser.APPROVE_OPTION){
                			
	                		final String path=exportMPTPanel.getDirectorySelected();
	                		if(path!=null && !path.equals("")){
	                			exportMPTPanel.setVisible(false);               		
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
		    										setVisible(false);
//		    										dispose();
		    										byte[] zipFile=ConstantesLocalGISEIEL.clienteLocalGISEIEL.exportMPT(anioEncuesta);
													
		    										
		    										
		    					         			FileOutputStream fileOutput = new FileOutputStream (path);
		    					         			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
		    					        			
		    					        			bufferedOutput.write(zipFile);
		
		    					        			// Cierre de los ficheros
		   
		    					        			bufferedOutput.close();
		    	
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
		    										setVisible(false);
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
			                                ExportMPTDialog.this,
			                                I18N.get("LocalGISEIEL", "localgiseiel.mensajes.mptdatosnocorrectos"),
			                                null,
			                                JOptionPane.INFORMATION_MESSAGE);
			                	}	
                		}          		
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
          
    public ExportMPTDialog(){
    	super(AppContext.getApplicationContext().getMainFrame());
   	 initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT.save"));         
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
        this.setContentPane(getExportMPTPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
//        this.setResizable(false);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        ExportMPTDialog dialog = new ExportMPTDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public ExportMPTPanel getExportMPTPanel(){
        if (exportMPTPanel == null){
        	exportMPTPanel = new ExportMPTPanel(new GridBagLayout());
        	exportMPTPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return exportMPTPanel;
    }
    
   
}
