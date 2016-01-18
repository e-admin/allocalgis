/**
 * ImportarFXCC.java
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
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.paneles.EditingImportarFXCCPanel;
import com.geopista.app.catastro.intercambio.importacion.paneles.PanelDatosParcelaImportarFXCC;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelMapa;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImportarFXCC extends JPanel implements WizardPanel{

	private boolean permiso = true;
	private AppContext application = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard = application.getBlackboard();
	private String nextID = "2";
	private WizardContext wizardContext; 
	
	private PanelDatosParcelaImportarFXCC panelImportarFXCC;
	
	private ArrayList listaParcelas;
	
	 //  Variables utilizadas para las validaciones
    private boolean continuar = false;     
	 
	 
    public ImportarFXCC()
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit(); 
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
 
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());
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
		                	panelInit();
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
        
        GUIUtil.centreOnScreen(progressDialog);
        progressDialog.setVisible(true);
    
    }
    
    private void panelInit() throws Exception
    { 
    	panelImportarFXCC = new PanelDatosParcelaImportarFXCC();

    	add(panelImportarFXCC,  
                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                            GridBagConstraints.BOTH, new Insets(0,0,0,0) ,0,0));
            
    	
    	// anadimos los eventos al boton de asociar la parcela del mapa a la tabla
    	panelImportarFXCC.getJButtonMapaATabla().addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	((EditingImportarFXCCPanel) panelImportarFXCC.getJPanelEditingInfo()).parcelaMapaATablaJButtonActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor());
            	hayParcelasTabla( panelImportarFXCC);
            }
        });
    	
    	// anadimos los eventos al boton de asociar la parcela de la tabla al mapa
    	panelImportarFXCC.getJButtonTablaAMapa().addActionListener(new ActionListener()
        {
    		public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	int parcelaSeleccionada = ((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).getRefCatasTablaBienesInmuebles().getParcelaSeleccionada();
           		((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).parcelaTablaAMapeJButtonActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor(), parcelaSeleccionada);
           		
           		hayParcelasTabla( panelImportarFXCC);
            }
    		
        });
    	
    	// anadimos los eventos al boton de eliminar la parcela de la tabla
    	panelImportarFXCC.getJButtonEliminarTabla().addActionListener(new ActionListener()
        {
    		public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	int parcelaSeleccionada = ((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).getRefCatasTablaBienesInmuebles().getParcelaSeleccionada();
            	((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).eliminarParcelaTablaJButtonActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor(),parcelaSeleccionada);
            	
            	hayParcelasTabla( panelImportarFXCC);
            }
    	
        });
    	
    	// anadimos los eventos al boton de busqueda de parcela por referencia catastral
    	panelImportarFXCC.getJButtonBuscarRefCatastral().addActionListener(new ActionListener()
    	{
    		 public void actionPerformed(java.awt.event.ActionEvent evt)
             {
    			 ((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).linkBotonBuscarRefCatastralActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor());
    			 
    			 hayParcelasTabla( panelImportarFXCC);
             }
    	});
    	
    	//anadimos los eventos al boton de busqueda de parcela por direccion
    	panelImportarFXCC.getJButtonBuscarRefCatastralPorDir().addActionListener(new ActionListener()
    	{
   		 public void actionPerformed(java.awt.event.ActionEvent evt)
            {
    	
   			 ((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).linkBotonBuscarRefCatastralPorDirActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor());
   			 hayParcelasTabla( panelImportarFXCC);
            }
    	});
    	
    	//anadimos los eventos al boton de busqueda de parcela por poligono
    	panelImportarFXCC.getJButtonBuscarRefCatastralPorPoligono().addActionListener(new ActionListener()
    	{
   		 public void actionPerformed(java.awt.event.ActionEvent evt)
            {
   			 ((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).linkBotonBuscarRefCatastralPorPoligonoActionPerformed((PanelMapa) panelImportarFXCC.getJPanelGraphicEditor());
   			 hayParcelasTabla( panelImportarFXCC);
            } 	
    	});

    	listaParcelas = panelImportarFXCC.getListaParcelas();
    	
    }
    
    private void hayParcelasTabla(PanelDatosParcelaImportarFXCC panelImportarFXCC){
    	
    	 if(((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).getReferenciasCatastrales() != null &&
        			!((EditingImportarFXCCPanel)panelImportarFXCC.getJPanelEditingInfo()).getReferenciasCatastrales().isEmpty() ){
        	continuar = true;
    	 }
    	 else{
    		continuar = false;	
    	 }
    	 
    	 wizardContext.inputChanged();
    
    }
    
	public void add(InputChangedListener listener) {
		
	}
	
	public void remove(InputChangedListener listener) {
		
	}


	public void enteredFromLeft(Map dataMap) {

	}

	public void exiting() {

	}

	public void exitingToRight() throws Exception {
		if(listaParcelas != null && !listaParcelas.isEmpty()){
			blackboard.put(ImportarUtils.LISTA_PARCELAS, listaParcelas);
		}
		else{
			JOptionPane.showMessageDialog(application.getMainFrame(), 
					I18N.get("Importacion","importar.fichero.fxcc.AsociarParcelas.msgSeleccionMapa"));
		}
	}

	public String getID() {
		 return "1";
	}

	public String getInstructions() {
		return " ";
	}

	public String getNextID() {
		 return nextID;
	}
	
	public String getTitle() {
		return this.getName();
	}

	public boolean isInputValid() {
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


	public void setNextID(String nextID) {
		 this.nextID = nextID;
	}

	public void setWizardContext(WizardContext wd) {
		 wizardContext = wd;
	}
	
	public PanelDatosParcelaImportarFXCC getPanelImportarFXCC() {
		return panelImportarFXCC;
	}


	public void setPanelImportarFXCC(PanelDatosParcelaImportarFXCC panelImportarFXCC) {
		this.panelImportarFXCC = panelImportarFXCC;
	}


	
}
