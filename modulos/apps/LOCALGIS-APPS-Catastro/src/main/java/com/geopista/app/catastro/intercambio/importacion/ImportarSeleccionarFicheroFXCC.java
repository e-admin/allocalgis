/**
 * ImportarSeleccionarFicheroFXCC.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.paneles.ImportSelectPanelFXCC;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImportarSeleccionarFicheroFXCC extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private WizardContext wizardContext; 
    private String nextID = "3";
   
    
    public static final int DIM_X=700;
    public static final int DIM_Y=450;
        
    private JTextField jTextFieldFileName = null;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    
    private JFileChooser fc = null;
    private JTextField jTextFieldFichDXF = null;
    private JTextField jTextFieldFichASC = null;
    private JComboBox jComboBoxParcela = null;
    private JButton jButtonAsociarContinuar = null;
    private ArrayList lstImagenes = null;
    
    private static String ASC = "asc";
    private static String DXF = "dxf";
    private String nameDXF = null;
    private String nameASC = null;
      
    private ArrayList listaFichFXCC = new ArrayList<FX_CC>();
    private ArrayList listaFinca = new ArrayList();
    private ArrayList listaImagenesAsociar = new ArrayList();
  
    public ImportarSeleccionarFicheroFXCC()
    {   
        try
        {     
        	 Locale loc=I18N.getLocaleAsObject();         
             ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",
                     loc,this.getClass().getClassLoader());
             I18N.plugInsResourceBundle.put("Importacion",bundle);
             jbInit(I18N.get("Importacion","importar.fichero.fxcc.panel.titulo.infografica"));
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit(final String title) throws Exception
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
                        	final ImportSelectPanelFXCC panel = new ImportSelectPanelFXCC(title);
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            jTextFieldFichDXF = panel.getJTextFieldFichDXF();
                            jTextFieldFichASC = panel.getJTextFieldFichASC();
                            jComboBoxParcela = panel.getJComboBoxParcela();
                            
                            jButtonAsociarContinuar = panel.getJButtonAsociarContinuar();
                            
                            jButtonAsociarContinuar.addActionListener(new ActionListener()
                            		{
                                public void actionPerformed(ActionEvent e)
                                {                    
                                	if(validarFicheros()){
                                		lstImagenes = panel.getLstImagenes();
                                		asociarInfoGrafica();
                                		jTextFieldFichASC.setText("");
                                		jTextFieldFichDXF.setText("");
                                		jComboBoxParcela.setSelectedIndex(0);
                                		
                                		ArrayList lista = panel.getLstImagenes();
                                		
                                		panel.limpiarTabla();
                                	}
                                	else{
                                		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
                                				I18N.get("Importacion","importar.infografica.boton.ficherosincorrectos"));
                                		jTextFieldFichASC.setText("");
                                        jTextFieldFichDXF.setText("");
                                	}
                                }
                                    });

                            jComboBoxParcela.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {                    
                                    checkFields();
                                }
                                    });
                            
                            panel.getJButtonFichDXF().addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    try
                                    {
                                        btnOpen_actionPerformed(e, DXF);
                                    }
                                    catch(Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                    }); 
                        	
                            
                            panel.getJButtonFichASC().addActionListener(new ActionListener()
                            	{
                            	public void actionPerformed(ActionEvent e)
		                        {
		                            try
		                            {
		                                btnOpen_actionPerformed(e, ASC);
		                            }
		                            catch(Exception ex)
		                            {
		                                ex.printStackTrace();
		                            }
		                        }
		                            }); 
		                        	
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
    	wizardContext.previousEnabled(true);
    	
        try
        {
        	jComboBoxParcela.removeAllItems();
        	ArrayList listReferencias = new ArrayList();
        	listReferencias = (ArrayList) blackboard.get(ImportarUtils.LISTA_PARCELAS);
 
        	 Iterator it = listReferencias.iterator();
             jComboBoxParcela.addItem(new FincaCatastro());
         	
             while (it.hasNext())
             {
                 Object o = it.next();
                 if (o instanceof FincaCatastro)
                 {
                     jComboBoxParcela.addItem((FincaCatastro)o);
                 }
             }
             jComboBoxParcela.setSelectedIndex(0);
    
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
       
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
		blackboard.put(ImportarUtils.LISTA_PARCELAS, listaFinca);
		blackboard.put(ImportarUtils.FILE_TO_IMPORT, listaFichFXCC);
		blackboard.put(ImportarUtils.LISTA_IMAGENES, listaImagenesAsociar);	
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
    	if(listaFichFXCC != null && !listaFichFXCC.isEmpty() &&
    			listaFinca != null && !listaFinca.isEmpty()){	
    		return true;
    	}
    	else{
    		return false;
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
        
    private void btnOpen_actionPerformed(ActionEvent e, String type) throws KeyStoreException, FileNotFoundException
    {   
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension(type);
        filter.setDescription(I18N.get("Importacion","importar.general.tiposfichero."+type));
        fc = new JFileChooser();
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL FILES(*.*)
        
        File currentDirectory = (File) blackboard.get(ImportarUtils.LAST_IMPORT_DIRECTORY);        
        fc.setCurrentDirectory(currentDirectory);        
        int returnVal = fc.showOpenDialog(this);        
        blackboard.put(ImportarUtils.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String fichero = fc.getSelectedFile().getPath();
            String name = fc.getSelectedFile().getName();
            if (type.equals(DXF)){
                jTextFieldFichDXF.setText(fichero);
                nameDXF = name;
            }
            else if (type.equals(ASC)){
                jTextFieldFichASC.setText(fichero);
                nameASC = name;
            }
            
            checkFields();
        }    
        
    }
     
    public void exiting()
    {   
    }
         
    private void checkFields()
    {
        if((!jTextFieldFichASC.getText().trim().equals("") &&  !jTextFieldFichDXF.getText().trim().equals("") &&
                jComboBoxParcela.getSelectedIndex()>0) ||
           (!jTextFieldFichDXF.getText().trim().equals("") && jComboBoxParcela.getSelectedIndex()>0))
        {
            jButtonAsociarContinuar.setEnabled(true);            
        }
        else
        {
            jButtonAsociarContinuar.setEnabled(false); 
        }
    }
    
    private boolean validarFicheros(){    	
    	
    	String nameDXF = null;
    	String nameASC = null;
    	boolean validator = false;
    	if(this.nameDXF!=null && !this.nameDXF.equals("")){
    		nameDXF = this.nameDXF.substring(0,this.nameDXF.toUpperCase().lastIndexOf(".DXF"));
    	}
    	if(this.nameASC!=null && !this.nameASC.equals("")){
    		nameASC = this.nameASC.substring(0,this.nameASC.toUpperCase().lastIndexOf(".ASC"));
    	}
    	
    	if((nameDXF!=null && nameASC!=null && nameDXF.equals(nameASC)) || 
    			(nameDXF!=null && nameASC==null )){
    		
    		Object o = jComboBoxParcela.getSelectedItem();
            if (o instanceof FincaCatastro
                    && ((FincaCatastro)o).getRefFinca()!=null)
            {

            	//if (nameASC.equals(nameDXF)){
            		validator = true;
            	//}
            }
    	}
    	

    	
    	return validator;
    	
    }
    
    private void asociarInfoGrafica()
    {        
        Object o = jComboBoxParcela.getSelectedItem();
        if (o instanceof FincaCatastro
                && ((FincaCatastro)o).getRefFinca()!=null)
        {
        	FX_CC fxcc = null;
        	File fileASC = null;
        	if(!jTextFieldFichASC.getText().equals("")){
        		fileASC = new File (jTextFieldFichASC.getText());
        		fxcc = new FX_CC(fileASC,
                        new File(jTextFieldFichDXF.getText()));
        	}
        	else{
        		fxcc = new FX_CC(" ",
                        new File(jTextFieldFichDXF.getText()));
        	}
        	
        	
        	
        	if(listaFinca.contains(o)){
        		int indice = listaFinca.indexOf(o);
        		listaFichFXCC.set(indice, fxcc);
        		
        		if(lstImagenes != null && !lstImagenes.isEmpty()){
            		ArrayList lstImgAux = (ArrayList)lstImagenes.clone();
            		listaImagenesAsociar.set(indice,lstImgAux);
            	}else{
            		listaImagenesAsociar.set(indice,new ArrayList());
            	}
        	}
        	else{
        		listaFichFXCC.add(fxcc);
                listaFinca.add(o);
                
                if(lstImagenes != null && !lstImagenes.isEmpty()){
            		ArrayList lstImgAux = (ArrayList)lstImagenes.clone();
            		listaImagenesAsociar.add(lstImgAux);
            	}else{
            		listaImagenesAsociar.add(new ArrayList());
            	}
        	}
        	lstImagenes = new ArrayList();
        	       	
            wizardContext.inputChanged();
        }
    }
    
    protected void close()
    {
        if (this.getParent().getParent() instanceof ScreenComponent)
        {
            this.getParent().getParent().setVisible(false);
            ((ScreenComponent)this.getParent().getParent()).fire(this.getParent().getParent(),
                    ScreenComponent.FINISHED,"finished");
        }
    }    
    
    
}  
