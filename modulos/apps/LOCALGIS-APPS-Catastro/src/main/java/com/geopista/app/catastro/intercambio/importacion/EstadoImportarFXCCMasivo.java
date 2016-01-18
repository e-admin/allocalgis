/**
 * EstadoImportarFXCCMasivo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
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

/**
 * @author alvarosanz
 *
 */
public class EstadoImportarFXCCMasivo extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private WizardContext wizardContext; 
    private String nextID = null;
    
    private static StringBuffer strBuf = new StringBuffer();  
    private String mensajeValidacion = new String();  
    
    private static JEditorPane jEditorPaneErrores = new JEditorPane();
    private JFileChooser fc = null;
    private JTextField jTextFieldDirectoryName = null;
    
    //  Variables utilizadas para las validaciones
    private boolean continuar = false;       
    
    
    public static final int DIM_X=700;
    public static final int DIM_Y=450;
        
    //private static String refCatastralFinca = null;
    static HashMap hashFicheros_ASC_DFX = new HashMap();
    
    private final static String NOMBRE_FICHERO_ASC = "ficheroASC";
    private final static String NOMBRE_FICHERO_DXF = "ficheroDXF";
    
    private static ArrayList listParcelas_BD = new ArrayList();
    private static ArrayList listFX_CC_BD = new ArrayList();
    private static ArrayList listParcelas_No_BD = new ArrayList();
    
    
    public EstadoImportarFXCCMasivo()
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
        this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        
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
    	
    	// se recibe la lista de parcelas que existen y a las que se asocia los ficheros ASC y DXF
    	final ArrayList listParcelas = (ArrayList) blackboard.get(ImportarUtils.LISTA_PARCELAS);
    	// se reciben los fichero ASC y DXF para asociarlos a las parcelas en la tabla fxcc
    	final ArrayList listFX_CC = (ArrayList) blackboard.get(ImportarUtils.FILE_TO_IMPORT);
    	// se reciben la lista de imagenes
    	final ArrayList listImagenes = (ArrayList) blackboard.get(ImportarUtils.LISTA_IMAGENES);
        
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
                        	almacenarFicheroBD(listParcelas, listFX_CC, listImagenes);
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
    	
    	continuar = true;
    	wizardContext.inputChanged();
    	
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
        return "2";
    }
    
    public String getInstructions()
    {
        return " ";
    }
    
    public boolean isInputValid()
    {        
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
            if (!continuar)            
                return false;             
            else                         
               return true;
        }
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
    
    
    private static void almacenarFicheroBD(ArrayList listParcelas, ArrayList listFX_CC, ArrayList listImagenes){
    	    	
    	StringBuffer strBuf = new StringBuffer(); 
    	jEditorPaneErrores.setText("");
    	
		ImportacionOperations oper = new ImportacionOperations();
		
		for(int i=0; i<listParcelas.size(); i++){
			FincaCatastro finca = (FincaCatastro)listParcelas.get(i);
			try {
				oper.insertarDatosGraficos(finca,(FX_CC)listFX_CC.get(i));
				
				ImportacionOperations oper2 = new ImportacionOperations();
    			if(listImagenes != null && !listImagenes.isEmpty()){
    				oper2.insertarDatosImagenesImportadorFXCC(finca, (ArrayList)listImagenes.get(i));
    			}
				
				strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
				.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.insertar.parcela"))
				.append(" ")
				.append(finca.getRefFinca().getRefCatastral())
				.append(ImportarUtils.HTML_FIN_PARRAFO);
				//jEditorPaneErrores.setText(strBuf.toString());
			}
			catch (Exception e) {
				strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
				.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.error.insertar.parcela"))
				.append(" ")
				.append(finca.getRefFinca().getRefCatastral())
				.append(ImportarUtils.HTML_FIN_PARRAFO);
				//jEditorPaneErrores.setText(strBuf.toString());
			}					
		}
		
		 strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
      	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.finalizada"))
      	.append(ImportarUtils.HTML_FIN_PARRAFO);
		 jEditorPaneErrores.setText(strBuf.toString());
	}
    	    
    
    public void exiting()
    {   
    }
}  
