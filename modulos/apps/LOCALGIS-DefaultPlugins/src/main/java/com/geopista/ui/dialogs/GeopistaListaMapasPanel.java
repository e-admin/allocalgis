/**
 * GeopistaListaMapasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.PublishedMapException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.ui.plugin.layers.DialogoSeleccionVersion;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaListaMapasPanel extends JPanel
{

    private JLabel lblListaMapas = new JLabel();

    private JScrollPane scpListaMapas = new JScrollPane();

    private DefaultListModel listModel = new DefaultListModel();

    private JList lstListaMapas = new JList(listModel);

    private JButton btnAbrir = new JButton();

    private JButton btnEliminar = new JButton();

    private JButton btnOrdenar = new JButton();
 
    private JButton btnCancelar = new JButton();
    
    private JCheckBox chCalendario = new JCheckBox();

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private GeopistaMap mapGeopistaSelected;
    
    private DialogoSeleccionVersion dialogo;
    
    boolean sortUp=true;
    
    public GeopistaListaMapasPanel()
        {

            try
            {
            	iniciarInternacionalizacion();
                jbInit();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    public void iniciarInternacionalizacion(){
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.layers.languages.Versioni18n",loc,this.getClass().getClassLoader());    	
    	I18N.plugInsResourceBundle.put("Version",bundle2);    	
    }

    public void getMapFiles(PlugInContext pluginContext, boolean searchlocal,
            boolean searchremote)
    {
        final ArrayList localMapIds = new ArrayList();

        if (searchlocal)
        {
            final String rutaBase = UserPreferenceStore.getUserPreference(
                    UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH,
                    true);
            ;

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

                                        ArrayList listaMap = GeopistaFunctionUtils
                                                .searchDirectory(rutaBase,progressDialog);

                                        Iterator Iter = listaMap.iterator();

                                        while (Iter.hasNext())
                                        {
                                            GeopistaMap currentMap = (GeopistaMap) Iter
                                                    .next();
                                            
                                            if(!(currentMap.isExtracted() && currentMap.getIdEntidad()!=AppContext.getIdEntidad()))
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
        if (searchremote)
        {
            //final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
        	final String sUrlPrefix;
        	sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");               
            if (aplicacion.isOnline())
            {
                try
                {

                    if (!aplicacion.isLogged())
                    {
                    	aplicacion.setProfile("Geopista"); 
                        aplicacion.login();
                    }

                    if (aplicacion.isLogged())
                    {
                        final TaskMonitorDialog progressDialog2 = new TaskMonitorDialog(
                                aplicacion.getMainFrame(), pluginContext
                                        .getErrorHandler());
                        progressDialog2.setTitle(aplicacion
                                .getI18nString("BuscandoMapas"));
                        progressDialog2.report(aplicacion
                                .getI18nString("BuscandoMapasBaseDatos"));
                        progressDialog2.addComponentListener(new ComponentAdapter()
                            {
                                public void componentShown(ComponentEvent e)
                                {
                                    // Wait for the dialog to appear before
                                    // starting the task. Otherwise
                                    // the task might possibly finish before the
                                    // dialog appeared and the
                                    // dialog would never close. [Jon Aquino]
                                    new Thread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                try
                                                {
                                                    Object[] mapSystemIds = localMapIds
                                                            .toArray();
                                                    Arrays.sort(mapSystemIds);
                                                    AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                                                            sUrlPrefix
                                                                    + "/AdministradorCartografiaServlet");
                                                    Collection lMaps = administradorCartografiaClient
                                                            .getMaps(Locale.getDefault()
                                                                    .toString());
                                                    Iterator lMapsIter = lMaps.iterator();
                                                    while (lMapsIter.hasNext())
                                                    {
                                                        try
                                                        {

                                                            GeopistaMap currentMap = (GeopistaMap) lMapsIter
                                                                    .next();
                                                            int codeIndex = Arrays
                                                                    .binarySearch(
                                                                            mapSystemIds,
                                                                            currentMap
                                                                                    .getSystemId());
                                                            if(codeIndex >= 0) continue; 
                                                            listModel.addElement(currentMap);
                                                        } catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } catch (Exception e)
                                                {

                                                } finally
                                                {
                                                    progressDialog2.setVisible(false);
                                                }
                                            }
                                        }).start();
                                }
                            });
                        if (aplicacion.getMainFrame() == null)
                            GUIUtil.centreOnScreen(progressDialog2);
                        else
                            GUIUtil.centreOnWindow(progressDialog2);
                        progressDialog2.setVisible(true);

                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    private void jbInit() throws Exception
    {
        lstListaMapas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstListaMapas.setCellRenderer(new LoadGeopistaMapRenderer());

        java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        java.awt.GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        lstListaMapas.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseClicked(java.awt.event.MouseEvent e)
                {
                    if (e.getClickCount() == 2)
                        btnAbrir_actionPerformed(null);
                }
            });
        lstListaMapas
                .addListSelectionListener(new javax.swing.event.ListSelectionListener()
                    {
                        public void valueChanged(javax.swing.event.ListSelectionEvent e)
                        {
                            btnAbrir.setEnabled(true);
                            btnEliminar.setEnabled(true);
                        }
                    });
        btnAbrir.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnAbrir_actionPerformed(e);
                }
            });

        btnCancelar.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnCancelar_actionPerformed(e);
                }
            });

        btnEliminar.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnEliminar_actionPerformed(e);
                }
            });
        btnOrdenar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                btnOrdenar_actionPerformed(e);
            }
        });
        chCalendario.addItemListener(new ItemListener(){
	        public void itemStateChanged(ItemEvent evt){
	        	mostrarCalendario();
	        }
	    });
        lblListaMapas.setText(aplicacion
                .getI18nString("GeopistaListaMapasPanel.ListaMapas"));
        btnAbrir.setText(aplicacion.getI18nString("GeopistaListaMapasPanel.Abrir"));
        btnAbrir.setEnabled(false);
        btnEliminar.setText(aplicacion.getI18nString("GeopistaListaMapasPanel.Eliminar"));
        btnEliminar.setEnabled(false);
        btnOrdenar.setText("Ordenar");
        btnOrdenar.setEnabled(true);
        btnCancelar.setText(aplicacion.getI18nString("GeopistaListaMapasPanel.Cancelar"));
        chCalendario.setText(I18N.get("Version","SeleccionVersion"));
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.ipadx = 13;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
        
        gridBagConstraints6.gridx = 1;
        gridBagConstraints6.gridy = 5;
        gridBagConstraints6.ipadx = 13;
        gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);

        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.ipadx = 19;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
        
        //Ordenar
        gridBagConstraints21.gridx = 1;
        gridBagConstraints21.gridy = 3;
        gridBagConstraints21.ipadx = 19;
        gridBagConstraints21.insets = new java.awt.Insets(5, 5, 5, 5);
        

        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.ipadx = 31;
        gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
        
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridheight = 4;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.weighty = 1.0;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.ipadx = 461;
        gridBagConstraints4.ipady = 511;
        gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 0;
        gridBagConstraints5.ipadx = 159;
        gridBagConstraints5.ipady = 5;
        gridBagConstraints5.insets = new java.awt.Insets(10, 15, 2, 235);
        scpListaMapas.setViewportView(lstListaMapas);
        this.add(btnCancelar, gridBagConstraints1);
        this.add(chCalendario, gridBagConstraints6);
        this.add(btnEliminar, gridBagConstraints2);
        this.add(btnAbrir, gridBagConstraints3);
        this.add(btnOrdenar, gridBagConstraints21);
        
        this.add(scpListaMapas, gridBagConstraints4);
        this.add(lblListaMapas, gridBagConstraints5);

        scpListaMapas.getViewport().add(lstListaMapas, null);
    }

    private void btnAbrir_actionPerformed(ActionEvent e)
    {
        GeopistaMap selectMap = (GeopistaMap) lstListaMapas.getSelectedValue();
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

        GeopistaMap selectMap = (GeopistaMap) lstListaMapas.getSelectedValue();

        if (selectMap != null)
        {

            int deleteMapConfirm = JOptionPane.showConfirmDialog(aplicacion
                    .getMainFrame(), aplicacion
                    .getI18nString("GeopistaListaMapasPanel.ConfimarBorradoMapa")
                    + " " + selectMap.getName() + "?", aplicacion
                    .getI18nString("GeopistaListaMapasPanel.BorrarMapa"),
                    JOptionPane.YES_NO_OPTION);
            if (deleteMapConfirm != 0)
                return;
            
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
                                        aplicacion
                                                .getI18nString("GeopistaListaMapasPanel.ElMapaInicioErrorBorrado")
                                                + " "
                                                + selectMap.getName()
                                                + " "
                                                + aplicacion
                                                        .getI18nString("GeopistaListaMapasPanel.NoBorrado"));

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
            	if (selectMap.getIdEntidad()==0){
	            	 deleteMapConfirm = JOptionPane.showConfirmDialog(aplicacion
	                         .getMainFrame(), aplicacion
	                         .getI18nString("GeopistaListaMapasPanel.ConfimarBorradoMapaEntidad0")
	                         + " " + selectMap.getName() + "?", aplicacion
	                         .getI18nString("GeopistaListaMapasPanel.BorrarMapa"),
	                         JOptionPane.YES_NO_OPTION);
	                 if (deleteMapConfirm != 0)
	                     return;
            	}
                 
                String sUrlPrefix; 
                sUrlPrefix = aplicacion.getString("geopista.conexion.servidor"); 
                AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                        sUrlPrefix + "/AdministradorCartografiaServlet");
                try
                {
                    if (!aplicacion.isLogged())
                    {                    
                    	aplicacion.setProfile("Geopista");                    	
                    	aplicacion.login();
                    }

                    if (aplicacion.isLogged())
                    {
                        administradorCartografiaClient.deleteMap(selectMap,Locale.getDefault()
                                .toString());

                    } else
                    {
                        return;
                    }
                } catch (Exception e1)
                {

                    Throwable deleteError = e1.getCause();
                    if (deleteError instanceof PermissionException)
                    {
                        JOptionPane
                                .showMessageDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                                .getI18nString("GeopistaListaMapasPanel.NoPermisosBorrarMapa"));
                    }
                    if (deleteError instanceof SystemMapException)
                    {
                        JOptionPane
                                .showMessageDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                                .getI18nString("GeopistaListaMapasPanel.MapaDeSistemaNoBorrar"));
                    }
                    if (deleteError instanceof SQLException)
                    {
                        JOptionPane
                                .showMessageDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                                .getI18nString("GeopistaListaMapasPanel.MapaNoSePuedeBorrarPublicado"));
                    }
                    if (deleteError instanceof PublishedMapException)
                    {
                        JOptionPane
                                .showMessageDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                                .getI18nString("GeopistaListaMapasPanel.MapaNoSePuedeBorrarPublicado"));
                    }
                    return;
                }

            }
            listModel.removeElement(selectMap);

        }
    }

    /*
     * Ordenacion de mapas.
     */
    private void btnOrdenar_actionPerformed(ActionEvent e){
    	
    	Object[] mapas=getOrdered(sortUp);
    	if (mapas.length>0){
	    	listModel.removeAllElements();
	    	
	    	for (int i=0;i<mapas.length;i++){
	    		GeopistaMap map=(GeopistaMap)mapas[i];    	   
	    		listModel.addElement(map);
	    	}
    	}
	    sortUp=!sortUp;  	
	}
    
	/**
	 * Ordena los mapas por nombre.
	 */
    public Object[] getOrdered(boolean sortUp){
    	Collection lMapas=new ArrayList();    	
    	for (int i=0;i<listModel.size();i++){
    		GeopistaMap map=(GeopistaMap)listModel.elementAt(i);
    		lMapas.add(map);
    	}
        ArrayList al= new ArrayList(lMapas);
        if (sortUp){
        	GeopistaMap.Collator1Comparator cc= new GeopistaMap.Collator1Comparator();
        	Collections.sort(al, cc);
        }
        else{
        	GeopistaMap.Collator2Comparator cc= new GeopistaMap.Collator2Comparator();
        	Collections.sort(al, cc);
        	
        }
        return al.toArray();
    }

    public GeopistaMap getMapGeopistaSelected()
    {
        return mapGeopistaSelected;
    }

	public DialogoSeleccionVersion getDialogo() {
		return dialogo;
	}

	public void setDialogo(DialogoSeleccionVersion dialogo) {
		this.dialogo = dialogo;
	}
	
	public void setCalendario(boolean enabled){
		this.chCalendario.setSelected(enabled);
	}
	
	private void mostrarCalendario(){
		if (this.chCalendario.isSelected()){
	 		dialogo = new DialogoSeleccionVersion(aplicacion.getMainFrame(),this);
			dialogo.setVisible(true);
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
