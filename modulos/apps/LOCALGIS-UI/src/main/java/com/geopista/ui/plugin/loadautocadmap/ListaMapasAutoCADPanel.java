/**
 * ListaMapasAutoCADPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.loadautocadmap;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ListaMapasAutoCADPanel extends JPanel
{
    private JScrollPane jScrollpaneMapasAutoCAD = null;

    private DefaultListModel listModel = null;

    private JList lstListaMapasAutoCAD = null;

    private JButton jButtonAbrir = null;

    private JButton jButtonCancelar = null;

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private GeopistaMap mapGeopistaSelected;

	private JPanel jPanelMapasAutoCAD = null;

	private JPanel jPanelBotoneraMapasAutoCAD = null;

	private JButton jButtonEliminar = null;

	 private Blackboard blackboard = aplicacion.getBlackboard();
	public static final String DIRTOLOAD = "dirToLoad";
	
    public ListaMapasAutoCADPanel()
    {

    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.loadautocadmap.languages.LoadAutoCADMapPlugIni18n",loc,this.getClass().getClassLoader());    	
    	I18N.plugInsResourceBundle.put("LoadAutoCADMapPlugIn",bundle2);

    	try
    	{
    		jbInit();
    		
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    	}

    }

    public void getMapFiles(PlugInContext pluginContext, boolean searchlocal,
            boolean searchremote)
    {
        final ArrayList localMapIds = new ArrayList();

        if (searchlocal)
        {
        	 final String rutaBase = (String) blackboard.get(DIRTOLOAD); 
        	
//            final String rutaBase = UserPreferenceStore.getUserPreference(
//                    AppContext.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH,
//                    true) + "autocad"
//                    + ((String)System.getProperties().getProperty("file.separator"));
//           
            // Comprobamos si existe la ruta base y en caso de que no exista la
            // creamos
            File rutaBaseFile = new File(rutaBase);
            if (!rutaBaseFile.exists())
            {
                rutaBaseFile.mkdirs();
            }

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), pluginContext.getErrorHandler());
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("BuscandoMapas"));
            progressDialog.report(aplicacion.getI18nString("BuscandoMapasLocales"));
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

                                        ArrayList listaMap = GeopistaFunctionUtils
                                                .searchDirectoryAutoCAD(rutaBase,progressDialog);

                                        Iterator Iter = listaMap.iterator();

                                        while (Iter.hasNext())
                                        {
                                            GeopistaMap currentMap = (GeopistaMap) Iter
                                                    .next();
                                            
                                            if(!(currentMap.isExtracted() && currentMap.getIdMunicipio()!=AppContext.getIdMunicipio()))
                                            {
                                                listModel.addElement(currentMap);
                                                String systemId = currentMap.getSystemId();
                                                if(systemId.startsWith("e")) systemId=systemId.substring(1);
                                                localMapIds.add(systemId);
                                            }
                                        }
                                    } catch (Exception e)
                                    {

                                    } finally
                                    {
                                        progressDialog.setVisible(false);
                                    }
                                }
                            }).start();
                    }
                });
            if (aplicacion.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);

        }        
    }

    private void jbInit() throws Exception
    {
    	this.setLayout(new GridBagLayout());
    	
    	this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("LoadAutoCADMapPlugIn","LoadAutoCADMap.ListAutoCADMaps"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    	
    	this.add(getJPanelMapasAutoCAD(),
				new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
        	
    	this.add(getJPanelBotoneraMapasAutoCAD(),
				new GridBagConstraints(1,0,1,1, 0.1, 0.1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	        
    }
    
    private JList getListaMapas(){

    	if(lstListaMapasAutoCAD == null){

    		listModel = new DefaultListModel();
    		lstListaMapasAutoCAD = new JList(listModel);

    		lstListaMapasAutoCAD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    		lstListaMapasAutoCAD.setCellRenderer(new LoadAutoCADMapRenderer());

    		lstListaMapasAutoCAD.addMouseListener(new java.awt.event.MouseAdapter()
    		{
    			public void mouseClicked(java.awt.event.MouseEvent e)
    			{
    				if (e.getClickCount() == 2)
    					btnAbrir_actionPerformed(null);
    			}
    		});
    		lstListaMapasAutoCAD.addListSelectionListener(new javax.swing.event.ListSelectionListener()
    		{
    			public void valueChanged(javax.swing.event.ListSelectionEvent e)
    			{
    				jButtonAbrir.setEnabled(true);
    				jButtonEliminar.setEnabled(true);
    			}
    		});
    	}
    	return lstListaMapasAutoCAD;
    }
    
    private JScrollPane getJScrollPaneMapasAutoCAD(){
    	
    	if(jScrollpaneMapasAutoCAD == null){
    		
    		jScrollpaneMapasAutoCAD = new JScrollPane();
    		jScrollpaneMapasAutoCAD.setViewportView(getListaMapas());
    		jScrollpaneMapasAutoCAD.getViewport().add(getListaMapas(), new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
    	}
    	return jScrollpaneMapasAutoCAD;
    }
    
    private JPanel getJPanelMapasAutoCAD(){
    	
    	if (jPanelMapasAutoCAD  == null){
    		
    		jPanelMapasAutoCAD = new JPanel(new GridBagLayout());
    		
    		jPanelMapasAutoCAD.add(getJScrollPaneMapasAutoCAD(),
    				new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.WEST,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
           
    	}
    	return jPanelMapasAutoCAD;
    }

    private JPanel getJPanelBotoneraMapasAutoCAD(){

    	if (jPanelBotoneraMapasAutoCAD  == null){

    		jPanelBotoneraMapasAutoCAD = new JPanel(new GridBagLayout());
    		
    		jPanelBotoneraMapasAutoCAD.add(getJButtonAbrir(),
    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTH,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelBotoneraMapasAutoCAD.add(getJButtonEliminar(),
    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.NORTH,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    		
    		jPanelBotoneraMapasAutoCAD.add(getJButtonCancelar(),
    				new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.NORTH,
    						GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
            	    		
    	}
    	return jPanelBotoneraMapasAutoCAD;
    }
    
    private JButton getJButtonAbrir(){
    	
    	if(jButtonAbrir == null){
    		
    		jButtonAbrir = new JButton();
    		
    		jButtonAbrir.setText(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.Open"));
            jButtonAbrir.setEnabled(false);
    		
    		jButtonAbrir.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnAbrir_actionPerformed(e);
                }
            });
    	}
    	return jButtonAbrir;
    }
    
    private JButton getJButtonEliminar(){
    	
    	if(jButtonEliminar  == null){
    		
    		jButtonEliminar = new JButton();

    		jButtonEliminar.setText(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.Delete"));
    		jButtonEliminar.setEnabled(false);
    		
    		jButtonEliminar.addActionListener(new ActionListener()
             {
                 public void actionPerformed(ActionEvent e)
                 {
                     btnEliminar_actionPerformed(e);
                 }
             });        
         
    	}
    	return jButtonEliminar;
    }
    
    private JButton getJButtonCancelar(){
    	
    	if(jButtonCancelar  == null){
    		
    		jButtonCancelar = new JButton();

    		jButtonCancelar.setText(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.Cancel"));
       
    		jButtonCancelar.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnCancelar_actionPerformed(e);
                }
            });
       
    	}
    	return jButtonCancelar;
    }

    private void btnAbrir_actionPerformed(ActionEvent e)
    {
        GeopistaMap selectMap = (GeopistaMap) getListaMapas().getSelectedValue();
        mapGeopistaSelected = selectMap;
        if (selectMap != null)
        {
            JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(
                    JDialog.class, this);
            dialogPadre.setVisible(false);
        }
    }

    private void btnCancelar_actionPerformed(ActionEvent e)
    {
        JDialog dialogPadre = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,
                this);
        dialogPadre.setVisible(false);
    }

    private void btnEliminar_actionPerformed(ActionEvent e)
    {

    	GeopistaMap selectMap = (GeopistaMap) getListaMapas().getSelectedValue();

        if (selectMap != null)
        {

            int deleteMapConfirm = JOptionPane.showConfirmDialog(aplicacion
                    .getMainFrame(), 
                    I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.ConfirmToDeleteMap")
                    + " " + selectMap.getName() + "?", 
                    I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.DeleteMap"),
                    JOptionPane.YES_NO_OPTION);
            
            if (deleteMapConfirm != 0){
                return;
            }
            
            String mapDeleteSelected = selectMap.getBasePath();

            if (!selectMap.isSystemMap())
            {
                File dirBaseMake = new File(mapDeleteSelected);

                File[] deleteFiles = dirBaseMake.listFiles();
                if (deleteFiles != null)
                {
                    for (int n = 0; n < deleteFiles.length; n++)
                    {
                        try
                        {
                            File actualDeleteFile = deleteFiles[n];
                            boolean borrado = actualDeleteFile.delete();
                            if (!borrado)
                                throw new IOException(
                                		I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.MapInitErrorDeleted")
                                		+ " " + selectMap.getName() + " "
                                        + I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.NotDelete"));

                            if (dirBaseMake != null)
                            {
                                borrado = dirBaseMake.delete();
                            }

                        } catch (Exception e1)
                        {
                            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), e1
                                    .getMessage());
                        }
                    }

                }

            } else
            {            	
            	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
            			I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.MapCanNotDelete"));

            	return;
            }

            listModel.removeElement(selectMap);

        }
    }

    public GeopistaMap getMapGeopistaSelected()
    {
        return mapGeopistaSelected;
    }

}
