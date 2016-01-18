/**
 * ValidateMPTDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.panels.ValidateSelectionMPTPanel;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.server.database.validacion.beans.ValidacionesMPTBean;
import com.geopista.server.database.validacion.beans.ValidacionesPorCuadrosMPTBean;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



public class ValidateMPTDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(ValidateMPTDialog.class);
	private ValidateSelectionMPTPanel validateSelectionMPTPanel = null;  
	private OKCancelPanel _okCancelPanel = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public static final int DIM_X = 710;
    public static final int DIM_Y = 550;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
                if (_okCancelPanel.wasOKPressed()){  
                	
                	final String anioEncuesta=validateSelectionMPTPanel.getAnioEncuesta();
                	if(anioEncuesta!=null && !anioEncuesta.equals("") && anioEncuesta.length() == 4){
                	//	dispose();
                		
                		final ArrayList lstIdValidaciones = new ArrayList();
                		ArrayList lstValCuados = new ArrayList();
                		final Hashtable hs = new Hashtable();
                		TreePath checkedPaths[] = validateSelectionMPTPanel.getCheckTreeManager().getSelectionModel().getSelectionPaths(); 
                		
                		if(checkedPaths != null){
                			dispose();
                			
                			
                			for(int i=0; i<checkedPaths.length; i++){
                				
                				if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
                						ValidacionesMPTBean){
                					// Seleccionados todas las validaciones del cuardro
                					lstIdValidaciones.add(((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId());
                					ValidacionesMPTBean vmpt = (ValidacionesMPTBean) validateSelectionMPTPanel.getLstValidacionesBBDD().get(
                								((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId());
                					lstValCuados = new ArrayList();
                					for (int z=0; z<vmpt.getLstvalidacuadros().size(); z++){
                						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)vmpt.getLstvalidacuadros().get(z)).getNombre());
                					}
                					hs.put(vmpt.getId(),lstValCuados);
                				}
                				else if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
                						ValidacionesPorCuadrosMPTBean){
                					
                					if(!lstIdValidaciones.contains(((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId())){
                						lstValCuados = new ArrayList();
                	
                						lstIdValidaciones.add((((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId()));
                	
                						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject()).getNombre());
                	
                					}
                					else{				
                						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject()).getNombre());
                					}
                					
                					hs.put(lstIdValidaciones.get(lstIdValidaciones.size()-1),
                							lstValCuados);
                					
                				}
                				else{
                					//estan seleccionados todos
                				
                					for (int h=0; h<validateSelectionMPTPanel.getLstValidacionesBBDD().size(); h++){
                						ValidacionesMPTBean vmpt = (ValidacionesMPTBean)validateSelectionMPTPanel.getLstValidacionesBBDD().get(h);
                						lstIdValidaciones.add(vmpt.getId());
                						lstValCuados = new ArrayList();
                						for (int z=0; z<vmpt.getLstvalidacuadros().size(); z++){
                							
                							lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)vmpt.getLstvalidacuadros().get(z)).getNombre());
                							
                						}
                						hs.put(vmpt.getId(),lstValCuados);
                					}
                				}
                			}
		
	                		if (validateSelectionMPTPanel.abrirJFileChooser() == JFileChooser.APPROVE_OPTION){
	                          		
		                		final String path=validateSelectionMPTPanel.getDirectorySelected();
		                		if(path!=null && !path.equals("")){
		                			validateSelectionMPTPanel.setVisible(false);
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
			    										progressDialog.report(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.validando"));  
			    										StringBuffer strb = new StringBuffer();
			    										for(int i=0; i<lstIdValidaciones.size(); i++){
			    											
			    											byte[]  bloque = InitEIEL.clienteLocalGISEIEL.validacionMPT(anioEncuesta,
			    													(Integer)lstIdValidaciones.get(i), (ArrayList)hs.get(lstIdValidaciones.get(i)));
			    											String strbloque = new String(bloque, "UTF8");
			    											strb.append(strbloque  + "\n");
			    										}
			    										
			    										FileWriter fichero = new FileWriter(path + ".txt");
			    										fichero.write(strb.toString() + "\r\n");
			    										 fichero.close();
  										
			    									}
			    									catch(Exception e)
			    									{
			    										logger.error("Error ", e);
			    										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
			    										return;
			    									}
			    									finally
			    									{
			    										JOptionPane.showMessageDialog(
			    												ValidateMPTDialog.this,
			    				                                "Proceso de validación finalizado",
			    				                                null,
			    				                                JOptionPane.INFORMATION_MESSAGE);
			    										System.out.println((new StringBuilder("CERRAMOS  VALIDACION MPT--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
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
			                                ValidateMPTDialog.this,
			                                I18N.get("LocalGISEIEL", "localgiseiel.mpt.directorio.nocorrectos"),
			                                null,
			                                JOptionPane.INFORMATION_MESSAGE);
			                	
			                	}
	                		}
                		}
                		else{
                			JOptionPane.showMessageDialog(
	                                ValidateMPTDialog.this,
	                                I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.seleccione.validaciones"),
	                                null,
	                                JOptionPane.INFORMATION_MESSAGE);
                		}
                	}
            		else{
            			JOptionPane.showMessageDialog(
                                ValidateMPTDialog.this,I18N.get("LocalGISEIEL", "localgiseiel.mpt.anio.nocorrectos"),
                                null, JOptionPane.INFORMATION_MESSAGE);
            		}
            	}
            	else{
            		dispose();
            	}
            	
            }
                	

        });        	
            
    }//ok
        return _okCancelPanel;
}
    
    
/* CONSTRUCTORES */    
          
    public ValidateMPTDialog(){
        	 this(ConstantesLocalGISEIEL.RUTA_EXPORT_MPT);
    }
    
    public ValidateMPTDialog(String rutaFichero){
    	 super(AppContext.getApplicationContext().getMainFrame());
    	 initialize(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.title"));         
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
        this.setModal(false);
        this.setSize(DIM_X, DIM_Y);
        this.setMinimumSize(new Dimension(DIM_X, DIM_Y));

        this.setResizable(true);
        this.setLayout(new GridBagLayout());
        this.add(getValidateSelectionMPTPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(getOkCancelPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0001, 0.0001,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        ValidateMPTDialog dialog = new ValidateMPTDialog();
        dialog.setVisible(true);
    }
    
    public ValidateSelectionMPTPanel getValidateSelectionMPTPanel() {
    	if (validateSelectionMPTPanel == null){
    		validateSelectionMPTPanel = new ValidateSelectionMPTPanel(new GridBagLayout());
    	}
		return validateSelectionMPTPanel;
	}


	public void setValidateSelectionMPTPanel(
			ValidateSelectionMPTPanel validateSelectionMPTPanel) {
		this.validateSelectionMPTPanel = validateSelectionMPTPanel;
	}
  
}
