/**
 * LayerFamiliesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Panel que permite realizar operaciones de manipulación sobre las layerfamilies
 * de GeoPISTA
 * 
 * @author cotesa
 *
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.images.IconLoader;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.LayersListCellRenderer;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class LayerFamiliesPanel extends JPanel implements FeatureExtendedPanel, TreeSelectionListener
{
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private static final String ICONO_FLECHA_DERECHA= "flecha_derecha.gif";
    private static final String ICONO_FLECHA_IZQUIERDA= "flecha_izquierda.gif";
    private static final String ICONO_SUBIR= "subir.gif";
    private static final String ICONO_BAJAR= "bajar.gif";
    
    private JScrollPane scrLayers = null;
    private JList lstLayers = null;
    private JPanelLayerFamilies jPanelLayerFamilies = null;
    private JButton btnSalir = null;
    private JButton btnGrabar = null;
    private JButton btnAnadirLayerFamily = null;
    private JButton btnEliminarLayerFamily = null;
    private JButton btnAnadirLayer = null;
    private JButton btnEliminarLayer = null;
    private JButton btnSubir = null;
    private JButton btnBajar = null;
    private JTree treeLayerFamilies = new JTree();
    
    private DefaultListModel listmodel = new DefaultListModel();
    private int[] selectedListRow;
    private LayerFamilyTable familiaSeleccionada;
    private LayerTable capaSeleccionada;
    private LayerTable nuevasCapas [] = new LayerTable[1];
    private int posicion = -1;
    private TreeNode padre;
    
    String string1 = I18N.get("GestorCapas","general.si"); 
    String string2 = I18N.get("GestorCapas","general.no"); 
    Object[] options = {string1, string2};
 
    String string1mapa = I18N.get("GestorCapas","layerfamilies.mapas.eliminarfamily");
    String string2mapa = I18N.get("GestorCapas","layerfamilies.mapas.cancelareliminar");
    Object[] optionsMapa = {string1mapa, string2mapa};
    
    private boolean nosale = false;
    
    private Set familiasModificadas = new HashSet();
    
    /**
     * This is the default constructor
     */
    public LayerFamiliesPanel()
    {            
        initialize();
        familiasModificadas = LayerOperations.familiasModificadas;
    }
    /**
     * Inicializa el panel
     *
     */
    private void initialize()
    {
        this.setLayout(null);
        this.setSize(1023, 607);
        
        this.add(getBtnSalir(), null);
        this.add(getBtnAnadirLayer(), null);
        this.add(getBtnEliminarLayer(), null);
        this.add(getBtnSubir(), null);
        this.add(getBtnBajar(), null);
        this.add(getBtnGrabar(), null);
        this.add(getScrLayers(), null);
        this.add(getJPanelLayerFamilies(), null);
        this.add(getBtnAnadirLayerFamily(), null);
        this.add(getBtnEliminarLayerFamily(), null);
        
    }
    /**
     * This method initializes this
     * 
     * @return void
     */
    public void enter()
    {         
        nosale = true;
        
        if (((Boolean)Identificadores.get("CapasModificadas")).booleanValue())        
        {
            lstLayers = null;
            Identificadores.put("CapasModificadas", false);  
            this.remove(scrLayers);
            this.remove(jPanelLayerFamilies);
            scrLayers = null;
            jPanelLayerFamilies = null;        
            this.add(getScrLayers(), null);
            this.add(getJPanelLayerFamilies(), null);
        }
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"Pestania4FamiliaCapas", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    /**
     * This method initializes scrLayers	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrLayers()
    {
        if (scrLayers == null)
        {
            scrLayers = new JScrollPane();
            scrLayers.setSize(new java.awt.Dimension(222,467));
            scrLayers.setLocation(new java.awt.Point(44,44));
            scrLayers.setViewportView(getLstLayers());
            scrLayers.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","layerfamilies.panellayers.titulo")));
        }
        else
        {
            scrLayers.setViewportView(getLstLayers());
        }
        return scrLayers;
    }
    
    /**
     * This method initializes lstLayers	
     * 	
     * @return javax.swing.JList	
     */
    private JList getLstLayers()
    {
        if (lstLayers == null)
        { 
            lstLayers = new JList(listmodel);
            
            LayersListCellRenderer renderer = new LayersListCellRenderer();
            lstLayers.setCellRenderer(renderer);
            
            LayerOperations operaciones = new LayerOperations();
            LayerTable[] lt=null;
            try
            {
                lt = operaciones.obtenerLayerTable(true);
            } catch (DataException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            listmodel.removeAllElements();
            if (lt!=null){
	            for (int i=0; i< lt.length; i++)
	            {
	                listmodel.addElement(lt[i]);   
	            }
            }
            
            
            ListSelectionModel rowSM = lstLayers.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) 
                    {                       
                        btnAnadirLayer.setEnabled(false);                        
                    } 
                    else 
                    {
                        if (familiaSeleccionada!=null || capaSeleccionada !=null)
                            btnAnadirLayer.setEnabled(true);
                        selectedListRow= lstLayers.getSelectedIndices();
                        nuevasCapas = new LayerTable[selectedListRow.length];
                        Object[] obj = lstLayers.getSelectedValues();
                        System.arraycopy( obj, 0, nuevasCapas, 0, obj.length );                        
                    }
                }
            });  
        }
        else
        {
            LayerOperations operaciones = new LayerOperations();
            LayerTable[] lt=null;
            try
            {
                lt = operaciones.obtenerLayerTable(true);
            } catch (DataException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (listmodel!=null && listmodel.getSize()!=0)
                listmodel.removeAllElements();
            for (int i=0; i< lt.length; i++)
            {
                listmodel.addElement(lt[i]);   
            }
        }
        return lstLayers;
    }
    
    /**
     * This method initializes jPanelLayerFamilies	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanelLayerFamilies getJPanelLayerFamilies()
    {
        jPanelLayerFamilies = new JPanelLayerFamilies(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jPanelLayerFamilies.setBounds(new java.awt.Rectangle(361,45,373,466));
        jPanelLayerFamilies.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","layerfamilies.panellayerfamilies.titulo")));
        
        treeLayerFamilies = jPanelLayerFamilies.getTree();
        treeLayerFamilies.addTreeSelectionListener(this);            
        treeLayerFamilies.setEditable(true);
        
        treeLayerFamilies.setCellRenderer(new LayerFamilyTreeRenderer());
        treeLayerFamilies.setCellEditor(new LayerFamilyTreeCellEditor());  
        treeLayerFamilies.setRowHeight(18);
        
        
        return jPanelLayerFamilies;
    }
    
    /**
     * This method initializes btnSalir 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnSalir()
    {
        if (btnSalir == null)
        {
            btnSalir = new JButton();
            btnSalir.setBounds(new Rectangle(875,555,100,25));
            btnSalir.setText(I18N.get("GestorCapas","general.boton.salir"));
            btnSalir.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    jButtonSalirActionPerformed(); 
                }
                    });
        }
        return btnSalir;
    }
    
    
    /**
     * This method initializes btnGrabar    
     *  
     * @return javax.swing.JButton  
     */
    protected JButton getBtnGrabar()
    {
        if (btnGrabar == null)
        {
            btnGrabar = new JButton();
            btnGrabar.setBounds(new Rectangle(875,523,100,25));
            btnGrabar.setText(I18N.get("GestorCapas","general.boton.grabar"));
            btnGrabar.setEnabled(false);
            btnGrabar.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnGrabar_actionPerformed(e);
                }
                    });
        }
        return btnGrabar;
    }
    
    /**
     * This method initializes btnGrabar    
     *  
     * @return javax.swing.JButton  
     */
    protected JButton getBtnAnadirLayerFamily()
    {
        if (btnAnadirLayerFamily == null)
        {
            btnAnadirLayerFamily = new JButton();
            btnAnadirLayerFamily.setBounds(new Rectangle(875,459,100,25));
            btnAnadirLayerFamily.setText(I18N.get("GestorCapas","general.boton.anadir"));
            btnAnadirLayerFamily.setEnabled(true);
            btnAnadirLayerFamily.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnAnadirLayerFamily_actionPerformed(e);
                }
                    });
        }
        return btnAnadirLayerFamily;
    }
    
    private void btnAnadirLayerFamily_actionPerformed(ActionEvent e)
    {   
        JDialogLayerFamily dlgLayerfamily = new JDialogLayerFamily(aplicacion.getMainFrame(),
                true, null, true);
        
        dlgLayerfamily.setLocationRelativeTo(this);
        dlgLayerfamily.show();
        
        
        LayerFamilyTable newLayerfamily=dlgLayerfamily.getLayerFamily();
        if (newLayerfamily!=null)
        {
            LayerFamilyOperations operaciones = new LayerFamilyOperations();
            try {
				if (operaciones.comprobarLayerFamily(newLayerfamily) >0){
					{                    
				        JOptionPane optionPane= 
				            new JOptionPane(I18N.get("GestorCapas","general.mensaje.existe.familia"),
				                    JOptionPane.INFORMATION_MESSAGE);
				        JDialog dialog =optionPane.createDialog(this,"");
				        dialog.show();                
				    }
				}
				else{
				    try
				    {
				        if(operaciones.crearLayerFamily (newLayerfamily)>=0)
				        {                    
				            JOptionPane optionPane= 
				                new JOptionPane(I18N.get("GestorCapas","general.mensaje.fin.operacion"),
				                        JOptionPane.INFORMATION_MESSAGE);
				            JDialog dialog =optionPane.createDialog(this,"");
				            dialog.show();                
				        }
				    } 
				    catch (HeadlessException e1)
				    {
				        // TODO Auto-generated catch block
				        e1.printStackTrace();
				    } 
				    catch (DataException e1)
				    {
				        // TODO Auto-generated catch block
				        e1.printStackTrace();
				    }
				    //recarga la pagina para actualizar el arbol de layerfamilies            
				    this.remove(jPanelLayerFamilies);   
				    jPanelLayerFamilies = null;
				    this.add(getJPanelLayerFamilies(), null);          
				    
				    btnAnadirLayer.setEnabled(false);
				    btnEliminarLayer.setEnabled(false);
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }        
    }
        
    /**
     * This method initializes btnEliminarLayerFamily    
     *  
     * @return javax.swing.JButton  
     */
    protected JButton getBtnEliminarLayerFamily()
    {
        if (btnEliminarLayerFamily == null)
        {
            btnEliminarLayerFamily = new JButton();
            btnEliminarLayerFamily.setBounds(new Rectangle(875,491,100,25));
            btnEliminarLayerFamily.setText(I18N.get("GestorCapas","general.boton.eliminar"));
            btnEliminarLayerFamily.setEnabled(false);
            btnEliminarLayerFamily.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    //Llamar al metodo correspondiente
                    btnEliminarLayerFamily_actionPerformed();
                }
                    });
        }
        return btnEliminarLayerFamily;
    }
    
    /**
     * Acción realizada cuando el usuario pulsa el botón Eliminar: se elimina la 
     * layerfamily previamente seleccionada. ESta misma acción es la que se lanza al
     * pulsar sobre el botón Suprimir con una layerfamily seleccionada
     */
    private void btnEliminarLayerFamily_actionPerformed()
    {   
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","layerfamilies.eliminar.mensaje1"),
                I18N.get("GestorCapas","general.advertencia"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("GestorCapas","layerfamilies.progreso.eliminacion.titulo"));
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
                            
                            if(familiaSeleccionada!=null)
                            {                                   
                                //comprueba si la layerfamily forma parte de algún mapa
                                LayerFamilyOperations operaciones = new LayerFamilyOperations();
                                
                                int mapas = JOptionPane.YES_NO_OPTION;
                                Vector vcIdMapas = 
                                    operaciones.buscarMapasWithLayerFamily(familiaSeleccionada.getIdLayerFamily());
                                if (!vcIdMapas.isEmpty())
                                {
                                    mapas = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                                            new StringBuffer(I18N.get("GestorCapas","layerfamilies.avisomapa.mensaje1")).append(" ")
                                            .append(vcIdMapas.size()).append(" ")
                                            .append(I18N.get("GestorCapas","layerfamilies.avisomapa.mensaje2")).append("\n")
                                            .append(I18N.get("GestorCapas","general.opciones")),
                                            I18N.get("GestorCapas","general.advertencia"),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,     
                                            optionsMapa,  
                                            optionsMapa[1]);   
                                }            
                                
                                if (mapas == JOptionPane.YES_OPTION)
                                {   
                                    progressDialog.report(I18N.get("GestorCapas","layerfamilies.progreso.eliminacion.relacionmapas"));
                                    if (operaciones.eliminarRelacionesMapas(familiaSeleccionada.getIdLayerFamily()))
                                    {
                                        progressDialog.report(I18N.get("GestorCapas","layerfamilies.progreso.eliminacion.layerfamily"));
                                        if(operaciones.eliminarLayerFamily(familiaSeleccionada)>0)
                                        {
                                            jPanelLayerFamilies.removeCurrentNode();
                                            treeLayerFamilies.clearSelection();
                                            familiaSeleccionada=null;
                                        }   
                                    }        
                                }                                
                            }
                        }
                        catch (DataException e1)
                        {               
                            e1.printStackTrace();
                        }                         
                        catch(Exception e)
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
    
    /**
     * Acción que se realiza en el momento en que el usuario pulsa el botón Salir
     *
     */
    private void jButtonSalirActionPerformed()
    {
        String string1 = I18N.get("GestorCapas","general.si"); 
        String string2 = I18N.get("GestorCapas","general.no"); 
        Object[] options = {string1, string2};
        
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","general.salir.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        aplicacion.getMainFrame().dispose();
        System.exit(0);        
    }
    
    /**
     * This method initializes btnAnadirLayer	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnAnadirLayer()
    {
        if (btnAnadirLayer == null)
        {
            btnAnadirLayer = new JButton();
            btnAnadirLayer.setBounds(new java.awt.Rectangle(285,165,30,30));
            btnAnadirLayer.setIcon(IconLoader.icon(ICONO_FLECHA_DERECHA));
            btnAnadirLayer.setEnabled(false);
            btnAnadirLayer.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    btnAnadirLayer_actionPerformed(e);
                }
                    });
        }
        return btnAnadirLayer;
    }
    
    /**
     * This method initializes btnEliminarLayer	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnEliminarLayer()
    {
        if (btnEliminarLayer == null)
        {
            btnEliminarLayer = new JButton();
            btnEliminarLayer.setBounds(new java.awt.Rectangle(284,223,30,30));
            btnEliminarLayer.setIcon(IconLoader.icon(ICONO_FLECHA_IZQUIERDA));
            btnEliminarLayer.setEnabled(false);
            btnEliminarLayer.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    btnEliminarLayer_actionPerformed(e, posicion);
                }
                    });
        }
        return btnEliminarLayer;
    }
    
    /**
     * This method initializes btnSubir	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnSubir()
    {
        if (btnSubir == null)
        {
            btnSubir = new JButton();
            btnSubir.setBounds(new java.awt.Rectangle(748,165,30,30));
            btnSubir.setIcon(IconLoader.icon(ICONO_SUBIR));
            btnSubir.setEnabled(false);
            btnSubir.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    btnSubir_actionPerformed(e);
                }
                    });
        }
        return btnSubir;
    }
    
    /**
     * This method initializes btnBajar	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnBajar()
    {
        if (btnBajar == null)
        {
            btnBajar = new JButton();
            btnBajar.setBounds(new java.awt.Rectangle(750,225,30,30));
            btnBajar.setIcon(IconLoader.icon(ICONO_BAJAR));
            btnBajar.setEnabled(false);
            btnBajar.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    btnBajar_actionPerformed(e);
                }
                    });
        }
        return btnBajar;
    }
    
    /**
     * Acción que se realiza en cuanto el usuario pulsa el botón destinado a eliminar
     * una capa de la layerfamily
     * @param e Evento
     * @param pos Posición de la capa eliminada
     */
    private void btnEliminarLayer_actionPerformed (java.awt.event.ActionEvent e, int pos)
    {
        btnGrabar.setEnabled(true);
        
        if (familiaSeleccionada == null)
        {               
            jPanelLayerFamilies.removeCurrentNode();
            
            LayerFamilyTable familia = (LayerFamilyTable)((DefaultMutableTreeNode)padre).getUserObject(); 
            
            
            //Si se borra el ultimo nodo, se queda seleccionada una layerfamily o ningun nodo (posicion=-1)
            //Se hace que se seleccione el ultimo nodo de la layerfamily padre
            if (posicion==-1)
            {
                jPanelLayerFamilies.selectLastNode((DefaultMutableTreeNode)padre);
                
            }
            
            HashMap posLayers = familia.getHmPosLayers();
            HashMap nuevaPosLayers = new HashMap();
            
            posLayers.remove(new Integer (pos+1));
            
            Iterator itValores = posLayers.values().iterator();
            Iterator itClaves = posLayers.keySet().iterator();
            
            while (itClaves.hasNext()) 
            {  
                Integer key = new Integer(itClaves.next().toString());
                LayerTable value = (LayerTable)itValores.next();
                
                if (key.intValue() > pos+1)
                {
                    nuevaPosLayers.put(new Integer(key.intValue()-1 ), value);
                }
                else if (key.intValue()== pos+1)
                {
                    //no hace nada
                }
                
                else
                {
                    nuevaPosLayers.put(key, value);
                }
                
            }
            
            familia.setHmPosLayers(nuevaPosLayers);
            familiasModificadas.add(familia);
            
            
        }
        
    }
    
    /**
     * Acción a realizar en cuanto el usuario pulsa el botón de Añadir una capa
     * a una layerfamily
     * @param e Evento capturado
     */
    private void btnAnadirLayer_actionPerformed (java.awt.event.ActionEvent e)
    {   
        btnGrabar.setEnabled(true);
        
        //Si se selecciona una familia, la capa se añade en la última posicion
        if (familiaSeleccionada != null)
        {   
            for (int i=0; i< selectedListRow.length; i++)
            {    
                if (familiaSeleccionada.getHmPosLayers()== null ||
                        !familiaSeleccionada.getHmPosLayers().containsValue(nuevasCapas[i]))
                {
                    familiaSeleccionada.addLayer(nuevasCapas[i]);
                    jPanelLayerFamilies.addObject(nuevasCapas[i], familiaSeleccionada);
                }
                
            }  
            
            familiasModificadas.add(familiaSeleccionada);
            
        }
        
        //si se selecciona una capa, las nuevas capas se añaden detrás de ésta en el orden de selección
        else 
        {
            LayerFamilyTable familia = (LayerFamilyTable)((DefaultMutableTreeNode)padre).getUserObject(); 
            
            HashMap posLayers = familia.getHmPosLayers();
            HashMap nuevaPosLayers = new HashMap();
            
            if(posLayers!=null)
            {
                Iterator itValores = posLayers.values().iterator();
                Iterator itClaves = posLayers.keySet().iterator();
                
                //Guarda las nuevas capas
                int indice = 0;
                for (int i=0; i< nuevasCapas.length; i++)
                {
                    
                    if (!familia.getHmPosLayers().containsValue(nuevasCapas[i]))
                    {
                        nuevaPosLayers.put(new Integer(posicion+2+indice), nuevasCapas[i]);
                        jPanelLayerFamilies.addObject(nuevasCapas[i], capaSeleccionada, posicion+indice+1);
                        indice++;
                    }
                }
                
                
                //Guarda las que ya existian
                while (itClaves.hasNext()) 
                {  
                    Integer key = new Integer(itClaves.next().toString());
                    LayerTable value = (LayerTable)itValores.next();
                    
                    if (key.intValue() < posicion+2)
                    {
                        nuevaPosLayers.put(key, value);
                    }
                    else
                    {
                        nuevaPosLayers.put(new Integer (key.intValue()+ indice), value);                        
                    }                    
                }                
                familia.setHmPosLayers(nuevaPosLayers);
            }
            familiasModificadas.add(familia);
        }        
    }
    
    /**
     * Acción realizada cuando el usuario pulsa el botón para subir una 
     * posición la de la capa seleccionada dentro de la layerfamily a 
     * la que pertenece
     * @param e
     */
    private void btnSubir_actionPerformed (java.awt.event.ActionEvent e)
    {
        btnGrabar.setEnabled(true);
        
        if (familiaSeleccionada == null)
        {               
            jPanelLayerFamilies.moveCurrentNode(JPanelLayerFamilies.UP);
            posicion++;
            
            LayerFamilyTable familia = (LayerFamilyTable)((DefaultMutableTreeNode)padre).getUserObject(); 
            
            HashMap posLayers = familia.getHmPosLayers();                        
            posLayers.put(new Integer(0), posLayers.get(new Integer(posicion)));
            posLayers.put(new Integer(posicion), posLayers.get(new Integer(posicion+1)));
            posLayers.put(new Integer(posicion+1), posLayers.get(new Integer(0)));
            posLayers.remove(new Integer(0));
            
            familia.setHmPosLayers(posLayers);
            familiasModificadas.add(familia);            
            posicion--;            
        }        
    }
    
    /**
     * Acción realizada cuando el usuario pulsa el botón para bajar una posición
     * la de la capa seleccionada dentro de la layerfamily a la que pertenece 
     * @param e
     */
    private void btnBajar_actionPerformed (java.awt.event.ActionEvent e)
    {        
        btnGrabar.setEnabled(true);
        
        if (familiaSeleccionada == null)
        {               
            jPanelLayerFamilies.moveCurrentNode(JPanelLayerFamilies.DOWN);
            posicion--;
            
            LayerFamilyTable familia = (LayerFamilyTable)((DefaultMutableTreeNode)padre).getUserObject(); 
            
            HashMap posLayers = familia.getHmPosLayers();            
            posLayers.put(new Integer(0), posLayers.get(new Integer(posicion+2)));
            posLayers.put(new Integer(posicion+2), posLayers.get(new Integer(posicion+1)));
            posLayers.put(new Integer(posicion+1), posLayers.get(new Integer(0)));
            posLayers.remove(new Integer(0));
            
            familia.setHmPosLayers(posLayers);
            familiasModificadas.add(familia);            
            posicion++;            
        }
    }
    
    /**
     * Acción realizada en cuanto el usuario pulsa el botón de Grabar los cambios
     * @param e
     */
    private void btnGrabar_actionPerformed(java.awt.event.ActionEvent e)
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("GestorCapas","layerfamilies.progreso.grabacion.titulo"));
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
                        
                        
                        int numFamModif = familiasModificadas.size(); 
                        if (numFamModif !=0)
                        {            
                            Iterator it = familiasModificadas.iterator();
                            
                            while (it.hasNext())
                            {
                                LayerFamilyTable lft = (LayerFamilyTable)it.next();
                                
                                //1. Guardar cambios en el nombre de la layerfamily (dictionary)
                                progressDialog.report(I18N.get("GestorCapas","layerfamilies.progreso.grabacion.actualizar.diccionario"));
                                
                                LayerOperations operaciones = new LayerOperations();
                                int idVocablo=0;
                                try
                                {
                                    idVocablo = operaciones.actualizarDictionary(lft.getHtNombre(),Integer.parseInt(lft.getLayerFamily().getDescription()));
                                } catch (NumberFormatException e1)
                                {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                } catch (DataException e1)
                                {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                if (idVocablo!=0)
                                {
                                    lft.getLayerFamily().setDescription(String.valueOf(idVocablo));              
                                }
                                else
                                {
                                    lanzarError(I18N.get("GestorCapas","layerfamilies.error.actualizacion.layerfamily.nombres"));
                                    return;
                                }                     
                                
                                //2. Añadir/actualizar relaciones entre layerfamily y layers (layerfamilies_layer_relations)
                                progressDialog.report(I18N.get("GestorCapas","layerfamilies.progreso.grabacion.actualizar.relaciones"));
                                int idLayerFamily = Integer.parseInt(lft.getLayerFamily().getSystemId());
                                
                                ArrayList layers = (ArrayList)lft.getListaLayerTable();
                                if (layers!=null)
                                {
                                    try
                                    {
                                        operaciones.eliminarRelacionesLayerFamily(idLayerFamily);
                                    } catch (DataException e1)
                                    {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Iterator itLayerTable = layers.iterator();
                                    
                                    while (itLayerTable.hasNext())
                                    {
                                        Object lt = itLayerTable.next();
                                        if (lt!=null)
                                        {
                                            try
                                            {
                                                operaciones.asociarLayerFamilyLayer(idLayerFamily, ((LayerTable)lt).getIdLayer());
                                            } catch (DataException e1)
                                            {
                                                numFamModif --;
                                                // TODO Auto-generated catch block
                                                e1.printStackTrace();
                                            }                            
                                        }
                                    }                              
                                }
                                else
                                {
                                    try
                                    {
                                        progressDialog.report(I18N.get("GestorCapas","layerfamilies.progreso.grabacion.eliminar.relaciones"));
                                        operaciones.eliminarRelacionesLayerFamily(idLayerFamily);
                                    } catch (DataException e1)
                                    {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                }                                
                            }            
                            
                            familiasModificadas.clear();            
                        }
                        
                        JOptionPane optionPane= new JOptionPane(I18N.get("GestorCapas","general.mensaje.fin.grabacion")+ 
                                "\n" +I18N.get("GestorCapas","layerfamilies.numero.familias.modificadas")+
                                " "+ numFamModif,JOptionPane.INFORMATION_MESSAGE);
                        JDialog dialog =optionPane.createDialog(aplicacion.getMainFrame(),"");
                        dialog.show();
                        repaint();
                        btnGrabar.setEnabled(false);
                        progressDialog.setVisible(false);                                               
                        
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);    
        
        
        
    }
    /**
     * Acción realizada cuando se detecta algún cambio en la selección dentro del 
     * árbol de layerfamilies y layers
     */
    public void valueChanged (TreeSelectionEvent e){
        
        if (e==null || !(e.getSource() instanceof JTree)) 
            return;
        JTree arbol= (JTree)e.getSource();
        boolean esRaiz = false;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)arbol.getLastSelectedPathComponent();
        
        if(node!=null)
        {            
            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof LayerFamilyTable)
            {
                familiaSeleccionada = (LayerFamilyTable)nodeInfo;                
                Identificadores.put("FamiliaSeleccionada", familiaSeleccionada);                
                capaSeleccionada = null;
                
                btnSubir.setEnabled(false);
                btnBajar.setEnabled(false);
                btnEliminarLayer.setEnabled(false);
                btnEliminarLayerFamily.setEnabled(true);
                
                if (selectedListRow!=null && selectedListRow.length>0)
                    btnAnadirLayer.setEnabled(true);
                else
                    btnAnadirLayer.setEnabled(false);
                
                posicion = -1;
                
            }       
            else if (nodeInfo instanceof LayerTable)
            {     
                familiaSeleccionada = null;
                capaSeleccionada = (LayerTable)nodeInfo;
                
                btnSubir.setEnabled(true);
                btnBajar.setEnabled(true);
                btnEliminarLayer.setEnabled(true);
                btnEliminarLayerFamily.setEnabled(false);
                
                if (selectedListRow!=null && selectedListRow.length>0)
                    btnAnadirLayer.setEnabled(true);
                else
                    btnAnadirLayer.setEnabled(false);
                
                padre = node.getParent();
                posicion = padre.getIndex(node);
                
                if (posicion ==0)
                    btnSubir.setEnabled(false);
                if(posicion == padre.getChildCount()-1)
                    btnBajar.setEnabled(false);
                
            }
            else
            {
                posicion = -1;
                esRaiz=true;
                btnEliminarLayerFamily.setEnabled(false);
            }
        }
        else
        {
            posicion =-1;
        }
        
        if (esRaiz)
        {
            btnEliminarLayer.setEnabled(false);
            btnAnadirLayer.setEnabled(false);
            btnSubir.setEnabled(false);
            btnBajar.setEnabled(false);            
        }
    }
    
    /**
     * Devuelve el path completo de un nodo
     * @param node Nodo
     * @return Path completo
     */
    public TreePath getPath(TreeNode node) {
        List list = new ArrayList();        
        
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);        
        
        return new TreePath(list.toArray());
    }
    
    /**
     * Lanza un mensaje de error 
     * @param mensaje Mensaje de error mostrado
     */
    private void lanzarError(String mensaje)
    {
        JOptionPane optionPane= new JOptionPane(mensaje,JOptionPane.ERROR_MESSAGE);
        JDialog dialog =optionPane.createDialog(this,"");
        dialog.show();        
    }
    
    /**
     * Acciones realizadas cuando se sale de la pantalla de layerfamilies
     */
    public void exit()
    {
        jPanelLayerFamilies.getTree().clearSelection();
        familiaSeleccionada = null;  
        
        lstLayers.clearSelection();
        capaSeleccionada = null;
        
        selectedListRow = null;
        
        //lstLayers= null;
        listmodel = new DefaultListModel();
        
        if (nosale && !familiasModificadas.isEmpty())
        {
            int n = JOptionPane.showOptionDialog(this,
                    "Se perderá cualquier cambio que no haya grabado. ¿Realmente desea abandonar el panel?",
                    "",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            
            //Sale de la pestaña
            if (n==JOptionPane.YES_OPTION)
            {
                Container c = this.getRootPane().getParent();
                
                if (c!=null && c instanceof GestorCapas)
                {
                    int indice =   ((GestorCapas)c).getPestanaTables().indexOfComponent(this);
                    LayerFamiliesPanel lfp = new LayerFamiliesPanel();
                    lfp.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    ((GestorCapas)c).getPestanaTables().insertTab(I18N.get("GestorCapas","general.pestana.layerfamilies"), null, lfp, null, indice);
                    ((GestorCapas)c).getPestanaTables().remove(this);     
                    familiasModificadas.clear();
                }
                
                return;
                
            }
            //Se queda en esta pestaña
            else
            {            
                Container c = this.getRootPane().getParent();
                
                if (c!=null && c instanceof GestorCapas)
                {
                    int indice =   ((GestorCapas)c).getPestanaTables().indexOfComponent(this);
                    c.getPropertyChangeListeners();
                    nosale=false;
                    ((GestorCapas)c).getPestanaTables().setSelectedIndex(indice);
                }
            }
        }    
    }    
    
}  //  @jve:decl-index=0:visual-constraint="28,36"
