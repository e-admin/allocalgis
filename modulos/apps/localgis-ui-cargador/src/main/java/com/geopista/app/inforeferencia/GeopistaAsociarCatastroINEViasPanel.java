/**
 * GeopistaAsociarCatastroINEViasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * GeopistaAsociarCatastroINEViasPanel : Panel que permite enlazar y relacionar
 * los tramos de via del INE con las Vias de Catastro
 */

public class GeopistaAsociarCatastroINEViasPanel extends JPanel implements
        ListSelectionListener, WizardPanel
{
	
    Logger logger =Logger.getLogger(GeopistaAsociarCatastroINEViasPanel.class);

    public static final String NOESPECIFICADO = "NE";
    public ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private JScrollPane pnlCatastro = new JScrollPane();





    private JScrollPane pnlINE = new JScrollPane();





    private JLabel lblCatastro = new JLabel();

    private JScrollPane pnlEnlazados = new JScrollPane();

    private DefaultListModel listEnlazadosModel = new DefaultListModel();

    private JList lstEnlazados = new JList(listEnlazadosModel);

    private JButton btnAsociar = new JButton();

    private JButton btnEliminar = new JButton();

    private JLabel lblINEVias = new JLabel();

    private JLabel lblEnlazados = new JLabel();

    private JOptionPane OpCuadroDialogo;

    private String nextID = null;

    private JLabel lblErrores = new JLabel();

    private JLabel lblImagen = new JLabel();

    private JLabel lblAsociar = new JLabel();

    private GeopistaEditor geopistaEditor1 = null;

    private WizardContext wizardContext;

    private ViasIneRenderer viasIneRenderer = new ViasIneRenderer();

    private ViasCatastroRenderer viasCatastroRenderer = new ViasCatastroRenderer();

    private ViasCatastroViasINEAssociationRenderer viasCatastroViasINEAssociationRenderer = new ViasCatastroViasINEAssociationRenderer();

    private ArrayList listViasINEInsertadas = null;

    private Connection con = null;

    String nombreCatastro = null;

    String viasIdVia = null;

    String viasCodigoIne = null;

    String viasNombreViaIne = null;

    String viasPosicionTipoViaIne = null;

    String viasTipoViaIne = null;

    GeopistaLayer viasLayer = null;

    DefaultListModel listViaIneModel = new DefaultListModel() {
        public void addElement(Object obj)
        {
            for (int n=0;n<this.size();n++)
            {
                DatosViasINE currentViaIne = (DatosViasINE) getElementAt(n);
                DatosViasINE newViaIne = (DatosViasINE) obj;
                if(newViaIne.getNombreVia().trim().compareTo(currentViaIne.getNombreVia().trim())<0)
                {
                    add(n,obj);
                    return;
                }
            }
            super.addElement(obj);
        }
    };

    DefaultListModel listCatastroModel = new DefaultListModel() {
        public void addElement(Object obj)
        {
            for (int n=0;n<this.size();n++)
            {
                GeopistaFeature newFeature = (GeopistaFeature) obj;
                GeopistaSchema schemaViasLayer = (GeopistaSchema) newFeature.getSchema();
                String newNombreCatastro = schemaViasLayer.getAttributeByColumn("nombrecatastro");
                GeopistaFeature currentViaCatastro = (GeopistaFeature) getElementAt(n);
                GeopistaSchema currentSchemaViasLayer = (GeopistaSchema) currentViaCatastro.getSchema();
                String currentNombreCatastro = currentSchemaViasLayer.getAttributeByColumn("nombrecatastro");

                if(newFeature.getString(nombreCatastro).trim().compareTo(currentViaCatastro.getString(nombreCatastro).trim())<0)
                {
                    add(n,obj);
                    return;
                }
            }
            super.addElement(obj);
        }
    };

    private JList lstViaINE = new JList(listViaIneModel);
    private JList lstCatastro = new JList(listCatastroModel);

    public GeopistaAsociarCatastroINEViasPanel()
        {
            jbInit();
        }

    /**
     * jbInit()
     *
     * Inicia el Panel
     */
    private void jbInit()
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
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
                                    btnEliminar.setEnabled(false);
                                    lstViaINE.setCellRenderer(viasIneRenderer);
                                    lstCatastro.setCellRenderer(viasCatastroRenderer);
                                    lstEnlazados
                                            .setCellRenderer(viasCatastroViasINEAssociationRenderer);
                                    lstViaINE
                                            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    lstCatastro
                                            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    lstEnlazados
                                            .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                                    setName(aplicacion
                                            .getI18nString("importar.asistente.callejero.titulo.5"));
                                    setSize(new Dimension(800, 529));
                                    setLayout(null);
                                    pnlCatastro
                                            .setBounds(new Rectangle(140, 55, 260, 255));

                                    pnlINE.setBounds(new Rectangle(540, 55, 240, 255));

                                    lblCatastro
                                            .setBounds(new Rectangle(140, 10, 280, 20));
                                    lblCatastro.setText(aplicacion
                                            .getI18nString("lblCatastroVias"));
                                    pnlEnlazados.setBounds(new Rectangle(145, 335, 510,
                                            170));

                                    lstCatastro.setBounds(new Rectangle(0, 0, 264, 271));
                                    lstViaINE.setBounds(new Rectangle(0, 0, 281, 271));
                                    btnAsociar.setText(aplicacion
                                            .getI18nString("btnAsociar"));
                                    btnAsociar
                                            .setBounds(new Rectangle(410, 155, 120, 30));
                                    btnAsociar.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnAsociar_actionPerformed(e);
                                            }
                                        });
                                    btnEliminar.setText(aplicacion
                                            .getI18nString("btnEliminar"));
                                    btnEliminar
                                            .setBounds(new Rectangle(665, 420, 115, 35));
                                    btnEliminar.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnEliminar_actionPerformed(e);
                                            }
                                        });

                                    lblINEVias.setText(aplicacion
                                            .getI18nString("lblINEVias"));
                                    lblINEVias.setBounds(new Rectangle(540, 10, 245, 20));
                                    lblEnlazados.setText(aplicacion
                                            .getI18nString("lblEnlazados"));
                                    lblEnlazados.setBounds(new Rectangle(140, 310, 285,
                                            20));
                                    lblErrores
                                            .setBounds(new Rectangle(495, 310, 280, 20));
                                    lblErrores.setForeground(Color.red);
                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));
                                    lblAsociar.setBounds(new Rectangle(140, 20, 640, 30));

                                    pnlCatastro.getViewport().add(lstCatastro, null);
                                    add(lblAsociar, null);
                                    add(lblImagen, null);
                                    add(lblErrores, null);
                                    add(lblEnlazados, null);
                                    add(lblINEVias, null);
                                    add(btnEliminar, null);
                                    add(btnAsociar, null);
                                    pnlEnlazados.getViewport().add(lstEnlazados, null);
                                    add(pnlEnlazados, null);
                                    add(lblCatastro, null);
                                    pnlINE.getViewport().add(lstViaINE, null);
                                    add(pnlINE, null);
                                    add(pnlCatastro, null);




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
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    private void btnAsociar_actionPerformed(ActionEvent e)
    {
        if ((lstCatastro.getSelectedIndex() != -1)
                && (lstViaINE.getSelectedIndex() != -1))
        {
            ViasCatastroViasINEAssociations newAssociation = new ViasCatastroViasINEAssociations(
                    (GeopistaFeature) lstCatastro.getSelectedValue(),
                    (DatosViasINE) lstViaINE.getSelectedValue());

            listEnlazadosModel.addElement(newAssociation);
            //listViaIneModel.remove(lstViaINE.getSelectedIndex());
            listCatastroModel.remove(lstCatastro.getSelectedIndex());

            btnEliminar.setEnabled(true);
        }
    }
    
    private void asociarEnPanel(GeopistaFeature viaCatastro,DatosViasINE viaIne){
    	
            ViasCatastroViasINEAssociations newAssociation = new ViasCatastroViasINEAssociations(
                    viaCatastro,
                    viaIne);

            listEnlazadosModel.addElement(newAssociation);
            //listViaIneModel.removeElement(viaIne);
            //listViaIneModel.remove(lstViaINE.getSelectedIndex());
            //listCatastroModel.remove(lstCatastro.getSelectedIndex());

            btnEliminar.setEnabled(true);        
    }

    private void btnEliminar_actionPerformed(ActionEvent e)
    {
        int[] selectedDeleteIndex = lstEnlazados.getSelectedIndices();
        Object[] selectedValues = lstEnlazados.getSelectedValues();

        for (int contadorLocal = selectedDeleteIndex.length - 1; contadorLocal >= 0; contadorLocal--)
        {
            ViasCatastroViasINEAssociations currentAssociation = (ViasCatastroViasINEAssociations) selectedValues[contadorLocal];
            //listViaIneModel.addElement(currentAssociation.getDatosViasINE());
            listCatastroModel.addElement(currentAssociation.getCatastroFeature());
            listEnlazadosModel.remove(selectedDeleteIndex[contadorLocal]);
        }

        if (listEnlazadosModel.getSize() == 0)
        {
            btnEliminar.setEnabled(false);
        }
    }

    private void btnAplicarCambios_actionPerformed(ActionEvent e)
    {

    }

    // This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e)
    {

    }

    public void enteredFromLeft(Map dataMap)
    {
    	
    	
    	listViaIneModel.removeAllElements();
    	listCatastroModel.removeAllElements();
    	listEnlazadosModel.removeAllElements();
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("Inforeferencia.GuardarCambios5"));
        progressDialog.report(aplicacion
                .getI18nString("Inforeferencia.GuardarCambios5"));
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
                                	//Meter Codigo
                                	//wizardContext.previousEnabled(false);

                                    try
                                    {
                                        if (con == null)
                                        {
                                            con = aplicacion.getConnection();
                                            con.setAutoCommit(false);
                                        }
                                    } catch (SQLException e)
                                    {
                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                                                .getI18nString("NoConexionBaseDatos"));
                                        wizardContext.cancelWizard();
                                        return;
                                    }

                                    boolean firingEvents = false;

                                    geopistaEditor1 = (GeopistaEditor) blackboard
                                            .get("geopistaEditorEnlazarPoliciaCalles");
                                    viasLayer = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer(
                                            GeopistaSystemLayers.VIAS);
                                    GeopistaSchema schemaViasLayer = (GeopistaSchema) viasLayer
                                            .getFeatureCollectionWrapper().getFeatureSchema();

                                    nombreCatastro = schemaViasLayer.getAttributeByColumn("nombrecatastro");
                                    viasIdVia = schemaViasLayer.getAttributeByColumn("id");
                                    viasCodigoIne = schemaViasLayer.getAttributeByColumn("codigoine");
                                    viasNombreViaIne = schemaViasLayer.getAttributeByColumn("nombreviaine");
                                    viasPosicionTipoViaIne = schemaViasLayer.getAttributeByColumn("posiciontipovia");
                                    viasTipoViaIne = schemaViasLayer.getAttributeByColumn("tipoviaine");

                                    listViasINEInsertadas = (ArrayList) blackboard.get("viasINEInsertadas");

                                    //Introducimos la conversion de vias
                                    Iterator listViasINEInsertadasIteratorPrev = listViasINEInsertadas.iterator();
                                    while (listViasINEInsertadasIteratorPrev.hasNext())
                                    {
                                        DatosViasINE currentViaINE = (DatosViasINE) listViasINEInsertadasIteratorPrev.next();
                                        currentViaINE.setNormalized(GeopistaUtil.removeSymbols(currentViaINE.getNombreVia()));
                                    }
                                    // Fin conversion
                                    
                                    Iterator listViasINEInsertadasIterator = listViasINEInsertadas.iterator();

                                    firingEvents = viasLayer.getLayerManager().isFiringEvents();
                                    viasLayer.getLayerManager().setFiringEvents(false);
                                    try
                                    {
                                        while (listViasINEInsertadasIterator.hasNext())
                                        {
                                            DatosViasINE currentViaINE = (DatosViasINE) listViasINEInsertadasIterator
                                                    .next();
                                            Collection viasCoincidentes = GeopistaUtil.searchByAttribute(viasLayer,
                                                    nombreCatastro, currentViaINE.getNombreVia().trim());
                                            if (viasCoincidentes.size()>0){
                            	                Iterator viasCoincidentesIterator = viasCoincidentes.iterator();                                
                            	                while (viasCoincidentesIterator.hasNext())
                            	                {
                            	                    GeopistaFeature currentFeature = (GeopistaFeature) viasCoincidentesIterator
                            	                            .next();
                            	
                            	                    currentFeature.setAttribute(viasCodigoIne, currentViaINE
                            	                            .getCodigoViaINE());
                            	                    currentFeature.setAttribute(viasNombreViaIne, currentViaINE
                            	                            .getNombreVia());
                            	                    currentFeature.setAttribute(viasPosicionTipoViaIne, currentViaINE
                            	                            .getPosicionVia());
                            	                    String tipoVia = currentViaINE.getTipoVia();
                            	                    if(tipoVia.trim().equals("")) tipoVia = NOESPECIFICADO;
                            	                    currentFeature.setAttribute(viasTipoViaIne,tipoVia);
                            	
                            	                    if (validateFeature(viasLayer, currentFeature) == 2)
                            	                        break;
                            	                    else if (validateFeature(viasLayer, currentFeature) == 0)
                            	                        continue;
                            	
                            	                    currentViaINE.setIdVia(currentFeature.getString(viasIdVia));
                            	                    currentFeature.setDirty(true);
                            	                }
                                            }
                                            else{
                                            	//No hay asociaciones para la via. La buscamos sin incluir prefijos y demas historias.
                                                viasCoincidentes = GeopistaUtil.searchByAttributeSpecial(viasLayer,
                                                        nombreCatastro, currentViaINE.getNormalized().trim());
                                                Iterator viasCoincidentesIterator = viasCoincidentes.iterator();                                
                            	                while (viasCoincidentesIterator.hasNext())
                            	                {
                            	                    GeopistaFeature currentFeature = (GeopistaFeature) viasCoincidentesIterator
                            	                            .next();
                            	                    asociarEnPanel(currentFeature,currentViaINE);
                            	                    currentFeature.setAttribute(viasCodigoIne, currentViaINE.getCodigoViaINE());
                            	                    currentFeature.setDirty(true);
                            	                    currentViaINE.setAsociada(true);
                            	                }                            	
                                            }
                                        }
                                    } finally
                                    {
                                        viasLayer.getLayerManager().setFiringEvents(firingEvents);
                                    }

                                    listViasINEInsertadasIterator = listViasINEInsertadas.iterator();
                                    while (listViasINEInsertadasIterator.hasNext())
                                    {
                                        DatosViasINE currentViaINE = (DatosViasINE) listViasINEInsertadasIterator
                                                .next();
                                        if (currentViaINE.getIdVia() == null)
                                        {
                                            listViaIneModel.addElement(currentViaINE);
                                        }
                                    }

                                    Collection allFeatures = viasLayer.getFeatureCollectionWrapper().getFeatures();
                                    Iterator allFeaturesIterator = allFeatures.iterator();
                                    while (allFeaturesIterator.hasNext())
                                    {
                                        GeopistaFeature currentFeature = (GeopistaFeature) allFeaturesIterator.next();
                                        if (currentFeature.getString(viasCodigoIne) == null
                                                || currentFeature.getString(viasCodigoIne).trim().equals(""))
                                        {
                                            listCatastroModel.addElement(currentFeature);
                                        }
                                    }
                                	//Fin Codigo

                                } catch (Exception e)
                                {
                                    logger.error("Error",e);
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
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("RealizadoAsociaciones"));
        progressDialog.report(aplicacion.getI18nString("RealizadoAsociaciones"));
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
                                    int idMunicipio = Integer.parseInt(aplicacion
                                            .getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                                    makeAllAssociations();
                                    makeViasIneAssociations(listViasINEInsertadas,
                                            idMunicipio);
                                    makeViasCatastroAssociations(progressDialog);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                } finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        geopistaEditor1 = null;
    }

    
    //Método que retira las entradas en el fichero de ViasAsociadas repetidas y que no están
    //presentes realmente en la base de datos, porque esos datos se han sobreescrito en ella
	public static void filterFile(String fileOrigen, String fileDestino){
   		java.util.Vector entradas = new java.util.Vector();
   		File fileIni = new File(fileOrigen);
    	try{
	   		if(!fileIni.exists())
	   			return;
	   		File fileFin = new File(fileDestino);
	   		if(!fileFin.exists())
	   			fileFin.createNewFile();
	   		
	   		FileReader reader = new FileReader(fileOrigen);
	   		FileWriter writer = new FileWriter(fileDestino);
	
	   		PrintWriter pw = new PrintWriter(writer);
	   		BufferedReader br = new BufferedReader(reader);	   		
	   		
	   		String linea;
	   		while((linea=br.readLine())!=null){
	   			entradas.add(linea);
	   		}
	   		for(int i=0;i<entradas.size();i++){
	   			linea=(String)entradas.get(i);
	   			int ini = linea.indexOf("|");
				String lineaFilter=linea.substring(ini+1);
				int med= lineaFilter.indexOf("|");
				lineaFilter=linea.substring(ini+1+med+1);
				int med2=lineaFilter.indexOf("|");
				lineaFilter=linea.substring(ini+1+med+1+med2+1);
				int fin1=lineaFilter.indexOf("|");
				lineaFilter=linea.substring(ini+1+med+1+med2+1+fin1+1);
				int fin2=lineaFilter.indexOf("|");
				lineaFilter=linea.substring(0,ini)+linea.substring(ini+1+med,ini+1+med+1+med2)+linea.substring(ini+1+med+1+med2+1+fin1+1+fin2);
	   			boolean repetido=false;
	   			for(int j=i+1;j<entradas.size();j++){
	   				String lineaSig=(String)entradas.get(j);
	   				ini=lineaSig.indexOf("|");
	   				String lineaFilterSig=lineaSig.substring(ini+1);
	   				
	   				med= lineaFilterSig.indexOf("|");
	   				lineaFilterSig=lineaSig.substring(ini+1+med+1);
					med2=lineaFilterSig.indexOf("|");
					lineaFilterSig=lineaSig.substring(ini+1+med+1+med2+1);
					fin1=lineaFilterSig.indexOf("|");
					lineaFilterSig=lineaSig.substring(ini+1+med+1+med2+1+fin1+1);
					fin2=lineaFilterSig.indexOf("|");
					lineaFilterSig=lineaSig.substring(0,ini)+lineaSig.substring(ini+1+med,ini+1+med+1+med2)+lineaSig.substring(ini+1+med+1+med2+1+fin1+1+fin2);

	   				if(lineaFilter.equals(lineaFilterSig)){
	   					repetido=true;
	   					break;
	   				}
	   			}
	   			if(!repetido){
	   				pw.println(linea);
	   			}
	   		}
	   		if(entradas.size()>0)
	   			entradas.removeAllElements();
	   		pw.close();
	   		br.close();
	   		fileIni.delete();
    	}catch(Exception e){
    		System.out.println("No se ha podido generar el fichero de vias enlazadas correctamente.");
	   		if(entradas.size()>0)
	   			entradas.removeAllElements();
	   		if(fileIni.exists())
	   			fileIni.delete();
    	}  		
    }
    
    public static void writeToFile(String codigo, String nombre, String tipo,String tipoCatastro,String nombreCat,String idVia,String folderMunicipio){
	     //Guarda datos de vias asociadas en un fichero
       	 try{
 			String homeCartografia="CartografiaBase" + File.separator	+ "Municipios"+ File.separator+folderMunicipio;

        	File dir = new File(homeCartografia+File.separator+"Resultado");
       		dir.mkdirs();
       		
       		String fileName=homeCartografia+File.separator+"Resultado"+File.separator+"tmpAsociadas.txt";
       		File fileINE = new File(fileName);
       		boolean firstTime=false;
       		if(!fileINE.exists())
       			firstTime=fileINE.createNewFile();
       		
               FileWriter ficheroINE = new FileWriter(fileName,true);
               PrintWriter pw = new PrintWriter(ficheroINE);
               if(firstTime){
            	   pw.print("IdMunicipioINE|");
            	   pw.print("CodigoViaINE|");
            	   pw.print("CodigoViaCatastro|");
            	   pw.print("TipoViaINE|");
            	   pw.print("TipoViaCatastro|");
            	   pw.print("NombreViaINE|");
            	   pw.println("NombreViaCatastro");
               }
               pw.println(folderMunicipio+"|"+codigo+"|"+idVia+"|"+tipo+"|"+tipoCatastro+"|"+nombre+"|"+nombreCat);
               pw.close();
           } catch (Exception e){
               e.printStackTrace();
           }
    }
    private int validateFeature(GeopistaLayer capaTramosVias,
            GeopistaFeature currentFeature)
    {
        boolean manualModification = ((Boolean) blackboard.get("mostrarError"))
                .booleanValue();
        boolean validateResult = false;
        boolean cancelImport = false;

        SchemaValidator validator = new SchemaValidator(null);

        while (!(validateResult = validator.validateFeature(currentFeature)))
        {
            if (!manualModification)
            {
                return 0;
            }
            FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaTramosVias,
                    currentFeature);
            if (featureDialog.wasOKPressed())
            {
                Feature clonefeature = featureDialog.getModifiedFeature();
                currentFeature.setAttributes(clonefeature.getAttributes());
            } else
            {
                Object[] possibleValues = {
                        aplicacion.getI18nString("CancelarEsteElemento"),
                        aplicacion.getI18nString("CancelarTodaLaImportacion"),
                        aplicacion.getI18nString("IgnorarFuturosErrores") };
                int selectedValueCancel = JOptionPane
                        .showOptionDialog(
                                aplicacion.getMainFrame(),
                                aplicacion
                                        .getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
                                aplicacion
                                        .getI18nString("GeopistaImportacionLog.BorrarDuplicar"),
                                0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                if (selectedValueCancel == 2)
                    manualModification = false;
                if (selectedValueCancel == 1)
                    cancelImport = true;
                break;
            }

        }

        if (validateResult)
        {
            return 1;
        }
        if (cancelImport == true)
            return 2;

        return 0;
    }

    private void makeAllAssociations()
    {

        Enumeration elementsEnumerations = listEnlazadosModel.elements();

        boolean firingEvents = viasLayer.getLayerManager().isFiringEvents();
        try
        {
            viasLayer.getLayerManager().setFiringEvents(false);

            while (elementsEnumerations.hasMoreElements())
            {
                ViasCatastroViasINEAssociations viasCatastroViasINEAssociations = (ViasCatastroViasINEAssociations) elementsEnumerations
                        .nextElement();
                GeopistaFeature currentGeopistaFeature = viasCatastroViasINEAssociations
                        .getCatastroFeature();
                DatosViasINE currentDatosViasINE = viasCatastroViasINEAssociations
                        .getDatosViasINE();

                currentGeopistaFeature.setAttribute(viasCodigoIne, currentDatosViasINE
                        .getCodigoViaINE());
                currentGeopistaFeature.setAttribute(viasNombreViaIne, currentDatosViasINE
                        .getNombreVia());
                currentGeopistaFeature.setAttribute(viasPosicionTipoViaIne,
                        currentDatosViasINE.getPosicionVia());
                String tipoVia = currentDatosViasINE.getTipoVia();
                if(tipoVia.equals("")) tipoVia = NOESPECIFICADO;

                currentGeopistaFeature.setAttribute(viasTipoViaIne, tipoVia);
                if (validateFeature(viasLayer, currentGeopistaFeature) == 2)
                    break;
                else if (validateFeature(viasLayer, currentGeopistaFeature) == 0)
                    continue;

                currentDatosViasINE.setIdVia(currentGeopistaFeature.getString(viasIdVia));
                currentGeopistaFeature.setDirty(true);

            }
        } finally
        {
            viasLayer.getLayerManager().setFiringEvents(firingEvents);
        }

    }

    private void makeViasCatastroAssociations(TaskMonitorDialog progressDialog)
            throws Exception
    {
        progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));

        GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) viasLayer
                .getDataSourceQuery().getDataSource();
        Map driverProperties = geopistaServerDataSource.getProperties();
        Object lastResfreshValue = driverProperties
                .get(Constantes.REFRESH_INSERT_FEATURES);
        try
        {
            driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(
                    true));
        	GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();

        	// MOD --> SRID destino, no por defecto.
			ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
        	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
			//String sridDefecto = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
			String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
        	driverPropertiesUpdate.put("srid_destino",sridDefecto);
        	driverPropertiesUpdate.put("srid_inicial",sridDefecto);
        	Iterator itFeature = viasLayer.getFeatureCollectionWrapper().getUltimateWrappee().getFeatures().iterator();
	    	FeatureCollection featureCollectionAux = AddNewLayerPlugIn.createBlankFeatureCollection();
            GeopistaSchema featureSchema = (GeopistaSchema) viasLayer
                .getFeatureCollectionWrapper().getFeatureSchema();
            String attrMuni = featureSchema.getAttributeByColumn("id_municipio");
    	 	String sMunicipio = ((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
	    	while (itFeature.hasNext()){
	    		Feature feature = (Feature)itFeature.next();
	    		// MOD --> sMunicipio de tipo STRING, comparado con INTEGER. Cambiado.
	    	 	if (sMunicipio.equals(feature.getAttribute(attrMuni).toString()))
	    	 		featureCollectionAux.add((Feature)feature/*.clone()*/);

	    	 	// MOD --> Eliminado clone, provoca duplicidad de primary keys en tabla de vias en DB.
	    	}
        	new GeopistaConnection(driverPropertiesUpdate).executeUpdate(
                    viasLayer.getDataSourceQuery().getQuery(),
                    featureCollectionAux,
                    progressDialog);
        } finally
        {
            if (lastResfreshValue != null)
            {
                driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,
                        lastResfreshValue);
            } else
            {
                driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
            }
        }
    }

    private void makeViasIneAssociations(ArrayList localListViasINEInsertadas,
            int idMunicipio) throws SQLException
    {
        PreparedStatement std = null;

        Iterator localListViasINEInsertadasIterator = localListViasINEInsertadas
                .iterator();

        while (localListViasINEInsertadasIterator.hasNext())
        {
            // Obtenemos los valores

            DatosViasINE datosViasIne = (DatosViasINE) localListViasINEInsertadasIterator
                    .next();

            if (datosViasIne.getIdVia() != null)
            {
                try
                {
                    std = con.prepareStatement("viasIneActualizar");
                    std.setInt(1, Integer.parseInt(datosViasIne.getIdVia()));
                    std.setInt(2, Integer.parseInt(datosViasIne.getCodigoViaINE()));
                    std.setInt(3, idMunicipio);
                    std.executeUpdate();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    // si ocurre algun problema en una de la iteraciones
                    // seguimos con la siguiente
                } finally
                {

                    std.close();
                    con.commit();
                }
            }

        }
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
        return "5";
    }

    public String getInstructions()
    {
        return "";
    }

    public boolean isInputValid()
    {
        return true;
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
        this.wizardContext = wd;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        geopistaEditor1 = null;
        viasLayer = null;
        listViasINEInsertadas = null;
        // TODO Auto-generated method stub

    }
    
    public static int validateFeature(GeopistaLayer capaTramosVias,GeopistaFeature currentFeature, Blackboard blackboard, ApplicationContext aplicacion, boolean cancelImport, boolean manualModification)
    {
        boolean validateResult = false;

        SchemaValidator validator = new SchemaValidator(null);

        while (!(validateResult = validator.validateFeature(currentFeature))){
            if (!manualModification){
                return 0;
            }
            FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaTramosVias,currentFeature);
            if (featureDialog.wasOKPressed()){
                Feature clonefeature = featureDialog.getModifiedFeature();
                currentFeature.setAttributes(clonefeature.getAttributes());
            } else{
                Object[] possibleValues = {aplicacion.getI18nString("CancelarEsteElemento"),aplicacion.getI18nString("CancelarTodaLaImportacion"),aplicacion.getI18nString("IgnorarFuturosErrores") };
                int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
                                aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"),0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,possibleValues[0]);
                if (selectedValueCancel == 2)
                    manualModification = false;
                if (selectedValueCancel == 1)
                    cancelImport = true;
                break;
            }
        }

        if (validateResult){
            return 1;
        }
        if (cancelImport == true)
            return 2;
        if(!manualModification)
        	return 3;

        return 0;
    }

    public static void makeAllAssociations(Blackboard blackboard, ApplicationContext aplicacion, GeopistaLayer viasLayer, DefaultListModel listEnlazadosModel, boolean isGraphic)
    {
        GeopistaSchema schemaViasLayer = (GeopistaSchema) viasLayer.getFeatureCollectionWrapper().getFeatureSchema();
		String viasIdVia = schemaViasLayer.getAttributeByColumn("id");
		String viasCodigoIne = schemaViasLayer.getAttributeByColumn("codigoine");
		String viasNombreViaIne = schemaViasLayer.getAttributeByColumn("nombreviaine");
		String viasPosicionTipoViaIne = schemaViasLayer.getAttributeByColumn("posiciontipovia");
		String viasTipoViaIne = schemaViasLayer.getAttributeByColumn("tipoviaine");
        Enumeration elementsEnumerations = listEnlazadosModel.elements();
        boolean firingEvents = viasLayer.getLayerManager().isFiringEvents();
        try{
            viasLayer.getLayerManager().setFiringEvents(false);
            boolean manualModification=false;
        	if(isGraphic)
        		manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
        	boolean cancelImport=false;
            while (elementsEnumerations.hasMoreElements()){
                ViasCatastroViasINEAssociations viasCatastroViasINEAssociations = (ViasCatastroViasINEAssociations) elementsEnumerations.nextElement();
                GeopistaFeature currentGeopistaFeature = viasCatastroViasINEAssociations.getCatastroFeature();
                DatosViasINE currentDatosViasINE = viasCatastroViasINEAssociations.getDatosViasINE();
                currentGeopistaFeature.setAttribute(viasCodigoIne, currentDatosViasINE.getCodigoViaINE());
                currentGeopistaFeature.setAttribute(viasNombreViaIne, currentDatosViasINE.getNombreVia());
                currentGeopistaFeature.setAttribute(viasPosicionTipoViaIne,currentDatosViasINE.getPosicionVia());
                String tipoVia = currentDatosViasINE.getTipoVia();
                if(tipoVia.equals("")) 
                	tipoVia = NOESPECIFICADO;
                
                currentGeopistaFeature.setAttribute(viasTipoViaIne, tipoVia);
		        int validate=validateFeature(viasLayer, currentGeopistaFeature, blackboard, aplicacion,cancelImport, manualModification);
		        if (validate == 2){
		        	cancelImport=true;
		            break;
		        }
		        else if (validate == 0)
		            continue;
		        if(validate==3){
		        	manualModification=false;
		        	continue;
		        }

                currentDatosViasINE.setIdVia(currentGeopistaFeature.getString(viasIdVia));
                currentGeopistaFeature.setDirty(true);

            }
        } finally{
            viasLayer.getLayerManager().setFiringEvents(firingEvents);
        }
    }
    
    public static void makeViasIneAssociations(ArrayList localListViasINEInsertadas,int idMunicipio, Connection con) throws SQLException
    {
        PreparedStatement std = null;
        Iterator localListViasINEInsertadasIterator = localListViasINEInsertadas.iterator();
        while (localListViasINEInsertadasIterator.hasNext()){
            // Obtenemos los valores
            DatosViasINE datosViasIne = (DatosViasINE) localListViasINEInsertadasIterator.next();
            if (datosViasIne.getIdVia() != null){
                try{
                    std = con.prepareStatement("viasIneActualizar");
                    std.setInt(1, Integer.parseInt(datosViasIne.getIdVia()));
                    std.setInt(2, Integer.parseInt(datosViasIne.getCodigoViaINE()));
                    std.setInt(3, idMunicipio);
                    std.executeUpdate();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    // si ocurre algun problema en una de la iteraciones seguimos con la siguiente
                } finally{
                    std.close();
                    con.commit();
                }
            }
        }
    }
    public static void makeViasCatastroAssociations(TaskMonitorDialog progressDialog, GeopistaLayer viasLayer,ApplicationContext aplicacion, boolean isGraphic)throws Exception
    {
    	if(isGraphic)
    		progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
    	else
    		System.out.println(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) viasLayer.getDataSourceQuery().getDataSource();
		Map driverProperties = geopistaServerDataSource.getProperties();
		Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
		try{
		    driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(true));
		    geopistaServerDataSource.getConnection().executeUpdate(viasLayer.getDataSourceQuery().getQuery(),viasLayer.getFeatureCollectionWrapper().getUltimateWrappee(),progressDialog);
		} finally{
		    if (lastResfreshValue != null){
		        driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,lastResfreshValue);
		    } else{
		        driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
		    }
		}
    }
    
    public static void prepararAsociaciones(Blackboard blackboard, ApplicationContext aplicacion, 
			GeopistaLayer viasLayer, ArrayList listViasINEInsertadas, boolean firingEvents, 
			DefaultListModel listViaIneModel, DefaultListModel listCatastroModel, boolean isGraphic, 
			String idMunicipio){
		GeopistaSchema schemaViasLayer = (GeopistaSchema) viasLayer.getFeatureCollectionWrapper().getFeatureSchema();
		String nombreCatastro = schemaViasLayer.getAttributeByColumn("nombrecatastro");
		String tipoCatastro = schemaViasLayer.getAttributeByColumn("tipovianormalizadocatastro");
		String viasIdVia = schemaViasLayer.getAttributeByColumn("id");
		String codCatastro = schemaViasLayer.getAttributeByColumn("codigocatastro");
		String viasCodigoIne = schemaViasLayer.getAttributeByColumn("codigoine");
		String viasNombreViaIne = schemaViasLayer.getAttributeByColumn("nombreviaine");
		String viasPosicionTipoViaIne = schemaViasLayer.getAttributeByColumn("posiciontipovia");
		String viasTipoViaIne = schemaViasLayer.getAttributeByColumn("tipoviaine");
		Iterator listViasINEInsertadasIterator = listViasINEInsertadas.iterator();
		
		try{
			firingEvents = viasLayer.getLayerManager().isFiringEvents();
			viasLayer.getLayerManager().setFiringEvents(false);
			boolean manualModification=false;
			if(isGraphic)
				manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
			boolean cancelImport=false;
			int i=0;
			
			while (listViasINEInsertadasIterator.hasNext()){
				i++;
				DatosViasINE currentViaINE = (DatosViasINE)listViasINEInsertadasIterator.next();
				//Buscar todas las calles de catastro que coinciden con la actual de INE, currentViaINE:
				//Collection viasCoincidentes = GeopistaUtil.searchByAttribute(viasLayer,nombreCatastro, currentViaINE.getNombreVia().trim());
				
				Collection viasCoincidentes = GeopistaUtil.searchBySimilarViaName(viasLayer, nombreCatastro, tipoCatastro, currentViaINE);
				if (viasCoincidentes!=null){
					Iterator viasCoincidentesIterator = viasCoincidentes.iterator();
					int j=0;
					//Para cada calle de catastro que coincide con la de actual de INE, currentViaINE
					while (viasCoincidentesIterator.hasNext()){
						j++;
						//Cogemos calle coincidente
						GeopistaFeature currentFeature = (GeopistaFeature) viasCoincidentesIterator.next();	  
						//le añadimos el código via ine
						currentFeature.setAttribute(viasCodigoIne, currentViaINE.getCodigoViaINE());
						//le añadimos el nombre via ine
						currentFeature.setAttribute(viasNombreViaIne, currentViaINE.getNombreVia());
						//le añadimos posicion de la via ine
						currentFeature.setAttribute(viasPosicionTipoViaIne, currentViaINE.getPosicionVia());
						//le añadimos tipo de via ine
						String tipoVia = currentViaINE.getTipoVia();
						if(tipoVia.trim().equals("")) 
							tipoVia = NOESPECIFICADO;
						currentFeature.setAttribute(viasTipoViaIne,tipoVia);
						
						Coordinate[] myCoords = currentFeature.getGeometry().getCoordinates();
	
						if (((Coordinate)myCoords[0]).x == new Double(0) && ((Coordinate)myCoords[1]).x == new Double(0) && ((Coordinate)myCoords[2]).x == new Double(0) && ((Coordinate)myCoords[3]).x == new Double(0)){
							// we are working with a Carvia street
							if(!isGraphic)
								writeToFile(currentViaINE.getCodigoViaINE(),currentViaINE.getNombreVia(),tipoVia,currentFeature.getString(tipoCatastro),currentFeature.getString(nombreCatastro),currentFeature.getString(codCatastro),idMunicipio);
	
							currentViaINE.setIdVia(currentFeature.getString(viasIdVia)==null || currentFeature.getString(viasIdVia).trim().equals("")?null:currentFeature.getString(viasIdVia));
							currentFeature.setDirty(true);
						}else{
						
							int validate=validateFeature(viasLayer, currentFeature, blackboard, aplicacion,cancelImport, manualModification);
							if (validate == 2){
								cancelImport=true;
								break;
							}
							else if (validate == 0)
								continue;
							if(validate==3){
								manualModification=false;
								continue;
							}
							if(!isGraphic)
								writeToFile(currentViaINE.getCodigoViaINE(),currentViaINE.getNombreVia(),tipoVia,currentFeature.getString(tipoCatastro),currentFeature.getString(nombreCatastro),currentFeature.getString(codCatastro),idMunicipio);
	
							currentViaINE.setIdVia(currentFeature.getString(viasIdVia));
							currentFeature.setDirty(true);
						}
					}
				}
			}
		} finally{
			viasLayer.getLayerManager().setFiringEvents(firingEvents);
			String homeCartografia="CartografiaBase" + File.separator	+ 
						"Municipios"+ File.separator+idMunicipio.substring(0,2)+
						File.separator+idMunicipio;
			filterFile(homeCartografia+File.separator+"Resultado"+File.separator+"tmpAsociadas.txt",
						homeCartografia+File.separator+"Resultado"+File.separator+"vias_Asociadas.txt");
		}
		listViasINEInsertadasIterator = listViasINEInsertadas.iterator();
		while (listViasINEInsertadasIterator.hasNext()){
				DatosViasINE currentViaINE = (DatosViasINE) listViasINEInsertadasIterator.next();
				if (currentViaINE.getIdVia() == null){
					listViaIneModel.addElement(currentViaINE);
				}
		}

		Collection allFeatures = viasLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesIterator = allFeatures.iterator();
		while (allFeaturesIterator.hasNext()){
		GeopistaFeature currentFeature = (GeopistaFeature) allFeaturesIterator.next();
			if (currentFeature.getString(viasCodigoIne) == null || currentFeature.getString(viasCodigoIne).trim().equals("")){
				listCatastroModel.addElement(currentFeature);
			}
		}
	}
}