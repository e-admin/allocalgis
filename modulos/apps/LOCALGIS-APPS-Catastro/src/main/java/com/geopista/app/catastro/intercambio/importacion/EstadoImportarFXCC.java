/**
 * EstadoImportarFXCC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.paneles.EstadoImportFXCCPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class EstadoImportarFXCC extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private Connection con=null;

    private WizardContext wizardContext; 
    private String nextID = null;
    
    private StringBuffer strBuf = new StringBuffer();  
    private String mensajeValidacion = new String();  
    
    private JEditorPane jEditorPaneErrores = new JEditorPane();

    
    //  Variables utilizadas para las validaciones
    private boolean continuar = false;       

    
    public static final int DIM_X=700;
    public static final int DIM_Y=450;
    
    
    public EstadoImportarFXCC()
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit(I18N.get("Importacion","importar.fichero.fxcc.panel.titulo.estado.importacion.fxcc"));
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit(final String title) throws Exception
    {  
        this.setLayout(new GridBagLayout());
       // this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.report(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {
                        	EstadoImportFXCCPanel panel = new EstadoImportFXCCPanel(title);
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                                                        
                            jEditorPaneErrores = panel.getJEditorPaneErrores();
                            
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
                            
                            
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        } 
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
    }
    
    public void enteredFromLeft(Map dataMap)
    {
    	wizardContext.previousEnabled(false);
    	
       // final ArrayList listReferencias = new ArrayList();
        final ArrayList listReferencias = (ArrayList) blackboard.get(ImportarUtils.LISTA_PARCELAS);
    	
    	//final ArrayList listFichero = new ArrayList();
    	final ArrayList listFichero = (ArrayList) blackboard.get(ImportarUtils.FILE_TO_IMPORT);
    	
    	//final ArrayList listImagenes = new ArrayList();
    	final ArrayList listImagenes = (ArrayList) blackboard.get(ImportarUtils.LISTA_IMAGENES);
    	
    	int i=0;
    	try{
    		  final JFrame desktop= new JFrame();
              final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);                    
              progressDialog.setTitle("TaskMonitorDialog.Wait");
              progressDialog.report(I18N.get("Importacion","importar.infografica.guardando.datos"));
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
    		
    		
                            	  asociarFicherosFxccFinca(listReferencias, listFichero, listImagenes);
                            	  	
                              }
                              catch(Exception e)
                              {
                                  ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                  return;
                              }
                              finally
                              {
                                  progressDialog.setVisible(false);
                                  progressDialog.dispose();
                              }
                          }
                    }).start();
                }
             });
             GUIUtil.centreOnScreen(progressDialog);
             progressDialog.setVisible(true);
    	}
    	catch (Exception e){
    		new DataExceptionCatastro(I18N.get("Importacion","importar.error.graficos"), e);
    	}
    	
    	
    }
       
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return "3";
    }
    
    public String getInstructions()
    {
        return " ";
    }
    
    public boolean isInputValid()
    {        
    	return continuar;
       
    }
    
    
    
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }
    
 
    public void exiting()
    {   
    }
    
    
    private void asociarFicherosFxccFinca(ArrayList listReferencias , ArrayList listFichero, ArrayList listImagenes) throws Exception{
    	
    	try
    	{  
    		
    		jEditorPaneErrores.setText("");
    		strBuf.append(ImportarUtils.HTML_VERDE);
    		
    		for(int i=0; i<listReferencias.size(); i++){
    			ImportacionOperations oper = new ImportacionOperations();
    		
    			strBuf.append(I18N.get("Importacion","importar.fichero.fxcc.AsociarParcelas.refCatasTablaBienesInmuebles"))
    			.append(" - ")
    			.append(((FincaCatastro) listReferencias.get(i)).getRefFinca().getRefCatastral());
    			
    			oper.insertarDatosGraficos(((FincaCatastro) listReferencias.get(i)),
    				((FX_CC) listFichero.get(i)));
    			
    			ImportacionOperations oper2 = new ImportacionOperations();
    			if(listImagenes != null && !listImagenes.isEmpty()){
    				oper2.insertarDatosImagenesImportadorFXCC(((FincaCatastro) listReferencias.get(i)), (ArrayList)listImagenes.get(i));
    			}
    			
    			strBuf.append(" - ")
        		.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.correcta"))
        		.append(ImportarUtils.HTML_SALTO);
    			
    			mensajeValidacion = mensajeValidacion + strBuf.toString();
        		strBuf = new StringBuffer();
    		}
    		
    		strBuf.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.finalizada"))
    		.append(" - ")
    		.append(ImportarUtils.HTML_FIN_PARRAFO);
    		jEditorPaneErrores.setText(mensajeValidacion + strBuf.toString());
    		strBuf = new StringBuffer();
    		
    		continuar = true;
    		wizardContext.inputChanged();
    		
    	}
    	catch (DataExceptionCatastro e1)
        {   
    		strBuf.append(ImportarUtils.HTML_FIN_PARRAFO)
    		.append(ImportarUtils.HTML_ROJO)
    		.append(ImportarUtils.HTML_SALTO)
    		.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.noCorrecta"))
    		.append(ImportarUtils.HTML_FIN_PARRAFO);
    		jEditorPaneErrores.setText(mensajeValidacion + strBuf.toString());
            e1.printStackTrace();
        }                       
    	
    }
    
    private void createStatement(PreparedStatement s, ArrayList listReferencias , ArrayList listFichero) throws SQLException
    {
    	
    	 for (int i=0; i< listReferencias.size(); i++)
         {
    		 
         }
    	
    }
    
    private Connection getDBConnection () throws SQLException
    {     
    	if(!application.isLogged()) 
    		throw new SQLException("No Connection");
    	if (con==null)
    	{
    		con=  application.getConnection();
            con.setAutoCommit(false);
    	}
         
        return con;
    }  
}  
