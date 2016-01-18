/**
 * ImportarInfoGraficaPanel.java
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
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.GraphicsImportSelectPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImportarInfoGraficaPanel extends JPanel 
{    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    
    private JFileChooser fc = null;
    private JTextField jTextFieldFichDXF = null;
    private JTextField jTextFieldFichASC = null;
    private JComboBox jComboBoxParcela = null;
    
    private JPanel jPanelBotones = null;
    private JButton jButtonAsociarContinuar = null;
    private JButton jButtonAsociarCerrar = null;    
    
    private static String ASC = "asc";
    private static String DXF = "dxf";
    private ArrayList lstReferencias;
    private Expediente expediente;
    
    private String nameDXF = null;
    private String nameASC = null;
    
    private ArrayList actionListeners = new ArrayList();
    
    public ImportarInfoGraficaPanel(Expediente exp)
    {   
        try
        {   
            this.expediente = exp;
            this.lstReferencias = exp.getListaReferencias();
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",
                    loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit(I18N.get("ModuloCatastral","panel.titulo.infografica"));
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit(final String title) throws Exception
    {  
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(550, 250));
        
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
                            GraphicsImportSelectPanel panel = 
                                new GraphicsImportSelectPanel(title);
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            jTextFieldFichDXF = panel.getJTextFieldFichDXF();
                            jTextFieldFichASC = panel.getJTextFieldFichASC();
                            jComboBoxParcela = panel.getJComboBoxParcela();
                            
                            Iterator it = lstReferencias.iterator();
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
                            
                            jComboBoxParcela.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {                    
                                    checkFields();
                                }
                                    });
                            
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.SMALL_PICTURE_LOCATION));
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
    
    private void checkFields()
    {
        if(//!jTextFieldFichASC.getText().trim().equals("") && 
                !jTextFieldFichDXF.getText().trim().equals("") &&
                jComboBoxParcela.getSelectedIndex()>0)
        {
            jButtonAsociarCerrar.setEnabled(true);
            jButtonAsociarContinuar.setEnabled(true);            
        }
        else
        {
            jButtonAsociarCerrar.setEnabled(false);
            jButtonAsociarContinuar.setEnabled(false); 
        }
        
        
    }
    
    public JPanel getJPanelBotones()
    {
        if (jPanelBotones == null)
        {
            jPanelBotones = new JPanel();
            jPanelBotones.setLayout(new GridBagLayout());
            jPanelBotones.add(getJButtonAsociarCerrar(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 20, 0, 0), 0, 0));
            jPanelBotones.add(getJButtonAsociarContinuar(), new GridBagConstraints(1, 0, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 20, 0, 0), 0, 0));
            
            //jPanelBotones.add(getJButtonSalir(), new GridBagConstraints(3, 0, 1, 1, 0, 0,
            //        GridBagConstraints.CENTER, GridBagConstraints.NONE,
            //        new Insets(0, 20, 0, 0), 0, 0));
        }
        
        return jPanelBotones;
    }
    
    /**
     * This method initializes jButtonAsociarContinuar   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonAsociarContinuar()
    {
        if (jButtonAsociarContinuar == null)
        {
            jButtonAsociarContinuar = new JButton();
            jButtonAsociarContinuar.setPreferredSize(new Dimension(150,25));
            jButtonAsociarContinuar.setText(I18N.get("Importacion","importar.infografica.boton.asociarcontinuar"));
            jButtonAsociarContinuar.setEnabled(false);
            jButtonAsociarContinuar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                	if(validarFicheros()){
                		asociarInfoGrafica();
                		jTextFieldFichASC.setText("");
                		jTextFieldFichDXF.setText("");
                		jComboBoxParcela.setSelectedIndex(0);
                	}
                	else{
                		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
                				I18N.get("Importacion","importar.infografica.boton.ficherosincorrectos"));
                		jTextFieldFichASC.setText("");
                        jTextFieldFichDXF.setText("");
                        nameDXF= "";
                        nameASC= "";
                	}
                }
                    });
        }
        return jButtonAsociarContinuar;
    }
    
    /**
     * This method initializes jButtonAsociarCerrar   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonAsociarCerrar()
    {
        if (jButtonAsociarCerrar == null)
        {
            jButtonAsociarCerrar = new JButton();
            jButtonAsociarCerrar.setPreferredSize(new Dimension(150,25));
            jButtonAsociarCerrar.setText(I18N.get("Importacion","importar.infografica.boton.asociarcerrar"));
            jButtonAsociarCerrar.setEnabled(false);
            jButtonAsociarCerrar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                	if (validarFicheros()){
                		asociarInfoGrafica();
                		close();        
                	}
                	else{
                		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
                				I18N.get("Importacion","importar.infografica.boton.ficherosincorrectos"));
                		jTextFieldFichASC.setText("");
                        jTextFieldFichDXF.setText("");
                	}
                }
                    });
            
        }
        return jButtonAsociarCerrar;
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
    
    private void asociarInfoGrafica()
    {        
        Object o = jComboBoxParcela.getSelectedItem();
        if (o instanceof FincaCatastro
                && ((FincaCatastro)o).getRefFinca()!=null)
        {
            ImportacionOperations oper = new ImportacionOperations();
            
            FX_CC fxcc = null;
        	File fileASC = null;
        	if(!jTextFieldFichASC.getText().equals("")){
        		fileASC = new File (jTextFieldFichASC.getText());
        		fxcc = new FX_CC(fileASC,
                        new File(jTextFieldFichDXF.getText()));
        	}
        	else{
        		fxcc = new FX_CC("",
                        new File(jTextFieldFichDXF.getText()));
        	}

            try
            {
                oper.asociarDatosGraficosTemporal(expediente.getIdExpediente(),
                        ((FincaCatastro)o), fxcc);
            } catch (DataExceptionCatastro e1)
            {   
                e1.printStackTrace();
            }                       
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
            	String referencia = ((FincaCatastro)o).getRefFinca().getRefCatastral().trim();
            	
            	if ((nameDXF!=null && nameASC==null && referencia.startsWith(nameDXF)) ||
            			nameDXF.equals(nameASC) && referencia.startsWith(nameDXF)){

          //  	if (nameASC.equals(nameDXF) && nameDXF.equals(referencia)){
            		validator = true;
            	}
            }
    	}
    	
    	//Comentar esta línea al finalizar las pruebas
    	//validator = true;
    	
    	return validator;
    	
    }
}  
