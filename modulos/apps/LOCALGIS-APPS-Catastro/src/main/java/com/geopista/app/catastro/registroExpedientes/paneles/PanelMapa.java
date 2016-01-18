/**
 * PanelMapa.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 23-ene-2007
 * Time: 11:57:05
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones. En este caso el panel añade un mapa de Geopista.
 */

public class PanelMapa extends JPanel implements IMultilingue
{
    private String etiqueta;
    private javax.swing.JFrame desktop;
    private final int mapa=2;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelMapa(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     * Inicializa todos los elementos del panel y los coloca en su posicion. Crea un editor de geopista si no
     * estaba ya creado. Se hace asi para ahorrar en memoria y tiempo de carga. Se cargan los plugin del
     * workbench-properties-simple.xml
     */
    public void inicializaPanel()
    {
        setLayout(new java.awt.GridLayout(1, 0));
        if(ConstantesRegistroExp.geopistaEditor==null)
        {
        	ConstantesRegistroExp.geopistaEditor= new GeopistaEditor("workbench-properties-simple.xml");
        	//ConstantesRegistroExp.geopistaEditor= new GeopistaEditor("workbench-properties-catastro-simple.xml");
        }
        
        renombrarComponentes();
    }

   /**
    * Asigna el jFrame del padre.
    *
    * @param desktop El Jframe padre.
    * */
    public void setDesktop(JFrame desktop)
    {
        this.desktop = desktop;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
    }

    /**
     * Metodo que carga el mapa en el panel. Llama muestra un dialogo de cargando el mapa y llama a la funcion
     * showGeopistaMap para cargar el mapa. Se carga la layer de parcelas. El mapa a cargar es una constante de la
     * clase.
     *
     * @param editable Si queremos que el mapa sea editable
     * @param panel Panel padre
     */
    public void load(final boolean editable, final JPanel panel){
        /** cargamos el mapa */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop, null);
        //progressDialog.setTitle(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.PanelMapa.CargandoMapa"));
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
                                  progressDialog.report(I18N.get("RegistroExpedientes",
                                        "Catastro.RegistroExpedientes.PanelMapa.CargandoMapa"));
                                  if (UtilRegistroExp.showGeopistaMap(desktop, panel, ConstantesRegistroExp.geopistaEditor, mapa, editable, null, null,null))
                                  {
                                      GeopistaLayer layer=(GeopistaLayer)ConstantesRegistroExp.geopistaEditor.getLayerManager().getLayer("parcelas");
                                      if (layer!=null)
                                      {
                                          layer.setEditable(editable);
                                          layer.setActiva(true);

                                      }
                                      else
                                      {
                                          new JOptionPane(I18N.get("RegistroExpedientes",
                                            "Catastro.RegistroExpedientes.PanelMapa.NoExisteMapa"),
                                                  JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                      }
                                  }
                                  else new JOptionPane(I18N.get("RegistroExpedientes",
                                        "Catastro.RegistroExpedientes.PanelMapa.NoExisteMapa"),
                                          JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                              }
                              catch(Exception e)
                              {
                                ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
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

      /*
        geopistaEditor.addGeopistaListener(new GeopistaListener() {
            public void selectionChanged(AbstractSelection abtractSelection) {
                //fireActionPerformed();
            }
            public void featureAdded(FeatureEvent e) {
            }
            public void featureRemoved(FeatureEvent e) {
            }
            public void featureModified(FeatureEvent e) {
            }
            public void featureActioned(AbstractSelection abtractSelection) {
                // doble click
                //fireActionPerformed();
            }
        }); */

    }

    /**
     * Funcion que realiza zoom sobre una parcela catastral pasada por parametro sobre un capa pasada por parametro
     * tambien.
     *
     * @param capa La layer
     * @param refCatastral La referencia a la parcela
     * @return boolean
     */
    public boolean actualizarFeatureSelection(String capa, String refCatastral) {

		try
        {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ConstantesRegistroExp.geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) ConstantesRegistroExp.geopistaEditor.getLayerManager().getLayer(capa);
            if(refCatastral!=null)
            {
                //TODO estas dos lineas la he añadido para que encuentre la traduccion de referencia_catastral, sino casca.
                GeopistaSchema schema = (GeopistaSchema) (geopistaLayer.getFeatureCollectionWrapper().getFeatureSchema());
                String srefCatastroAttri = schema.getAttributeByColumn("referencia_catastral");
                Collection collection = UtilRegistroExp.searchByAttribute(geopistaLayer, srefCatastroAttri, refCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					ConstantesRegistroExp.geopistaEditor.select(geopistaLayer, feature);
				}

                ConstantesRegistroExp.geopistaEditor.zoomToSelected();

                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return true;
            }
            return false;
            /*for (int i=0; i < referenciasCatastralesJTable.getModel().getRowCount(); i++){
                String refCatastral= (String) referenciasCatastralesJTable.getValueAt(i,0);
                Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					CMainLicencias.geopistaEditor.select(geopistaLayer, feature);
				}
			}

			CMainLicencias.geopistaEditor.zoomToSelected();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;*/
		}
        catch (Exception ex)
        {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return false;
		}
	}

    /**
     * Metodo que devuelve una collection con los features que el usuario a seleccionado.
     *
     * @return Collection La seleccion del usuario.
     */
    public Collection getFeaturesSelecionados()
    {
        return ConstantesRegistroExp.geopistaEditor.getSelection();
    }
}
