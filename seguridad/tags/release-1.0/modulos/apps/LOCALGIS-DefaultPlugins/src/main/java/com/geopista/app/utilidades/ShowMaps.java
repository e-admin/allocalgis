package com.geopista.app.utilidades;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.control.ISesion;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaOptionsPlugIn;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarPlugIn;
import com.geopista.ui.snap.GeopistaInstallGridPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;


/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 18-oct-2004
 * Time: 11:19:45
 */
public class ShowMaps {
	private static Logger logger = Logger.getLogger(ShowMaps.class);

	 public static GeopistaEditor showMap(String url, GeopistaEditor geopistaEditor, int id_map,boolean editable,
				JFrame parent) throws CancelException{
		return  showMap(url, geopistaEditor, id_map,  editable, parent, null);
		
	 }

   public static GeopistaEditor showMap(String url, GeopistaEditor geopistaEditor, int id_map,boolean editable,
		   				JFrame parent,TaskMonitor monitor) throws CancelException{

       /* AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(url + "AdministradorCartografiaServlet");

        Collection aoMaps=acClient.getMaps("es_ES");
        for(Iterator i=aoMaps.iterator();i.hasNext();)
        {
             GeopistaMap mapa=(GeopistaMap)i.next();
             if(mapa.getName()!=null&&mapa.getName().equals(mapName))
             {*/
                 //GeopistaMap mapa = (GeopistaMap)i.next();
                //TODO: mirar a ver como hay q hacer esto
                 try
                 {
                        inicializaEditor(geopistaEditor,editable);
                        geopistaEditor.loadMap("geopista:///"+id_map,monitor);
                        return geopistaEditor;
                 }
                 catch (CancelException e1){
                	 throw e1;
                 }
                 catch (Exception e)
                 {
                     String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
                     Throwable t=e.getCause();
                     int j=0;
                     while (t!=null&&j<10)
                     {
                           sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
                           t=t.getCause();
                           j++;
                      }
                      logger.error("Exception al cargar el mapa " +id_map+": " ,e);
                       JOptionPane optionPane= new JOptionPane("Error al cargar el mapa\n"+sMensaje,JOptionPane.ERROR_MESSAGE);
                       JDialog dialog =optionPane.createDialog(parent,"ERROR");
                       dialog.show();

                 }
             /*}
        }      */
        return null;
    }


	public static void inicializaEditor(GeopistaEditor geopistaEditor, boolean editable) {
		// geopistaEditor.showLayerName(false);

		// ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new
		// ZoomToFullExtentPlugIn();
		// geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);
		GeopistaPrintPlugIn geopistaPrintPlugIn = new GeopistaPrintPlugIn();
		geopistaEditor.addPlugIn(geopistaPrintPlugIn);
		if (editable) {
			GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
			geopistaEditor.addPlugIn(geopistaEditingPlugIn);
		}
		GeopistaInstallGridPlugIn geopistaInstallGridPlugIn = new GeopistaInstallGridPlugIn();
		GeopistaOptionsPlugIn geopistaOptionsPlugIn = new GeopistaOptionsPlugIn();
		geopistaEditor.addPlugIn(geopistaOptionsPlugIn);
		geopistaEditor.addPlugIn(geopistaInstallGridPlugIn);

		// GeopistaFeatureSchemaPlugIn schema = new
		// GeopistaFeatureSchemaPlugIn();
		// geopistaEditor.addPlugIn(schema);
		GeopistaValidatePlugin geopistaValidatePlugin = new GeopistaValidatePlugin();
		geopistaEditor.addPlugIn(geopistaValidatePlugin);
		SeriePrintPlugIn seriePrintPlugIn = new SeriePrintPlugIn();
		geopistaEditor.addPlugIn(seriePrintPlugIn);

		GeopistaScaleBarPlugIn geopistaScaleBarPlugIn = new GeopistaScaleBarPlugIn();
		geopistaEditor.addPlugIn(geopistaScaleBarPlugIn);

		geopistaEditor.addCursorTool("Zoom In/Out","com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
		geopistaEditor.addCursorTool("Pan","com.vividsolutions.jump.workbench.ui.zoom.PanTool");
		geopistaEditor.addCursorTool("Measure","com.geopista.ui.cursortool.GeopistaMeasureTool");
		geopistaEditor.addCursorTool("Select tool","com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

		// eopistaEditor.addPlugIn("com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers.SelectItemsByCircleFromSelectedLayersPlugIn","com.geopista.ui.images.selecteditemsbycircle.gif");
	}


   	public static GeopistaLayer showLayer(String sUrl,GeopistaEditor geopistaEditor, String layerName,boolean editable, String[] layers, Color color) {
		try {

                GeopistaLayer layerEspecial=null;
		        inicializaEditor(geopistaEditor,editable);
                com.geopista.security.SecurityManager.setHeartBeatTime(6000000);
			    AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(sUrl+ "/AdministradorCartografiaServlet");
                for (int i=0;i<layers.length;i++)
                {
                    try{
                        GeopistaLayer layer = acClient.loadLayer(layers[i], "es_ES", null, FilterLeaf.equal("1", new Integer(1)));
                        if (layer==null) continue;

                        if (layerName!=null&&layerName.equalsIgnoreCase(layers[i]))
                        {
                            layer.setEditable(true);
                            layer.setActiva(true);
                            layer.addStyle(new BasicStyle(color));
                            layerEspecial=layer;
                        }
                        else
                        {
                            layer.setEditable(false);
                            layer.setActiva(false);
                            layer.addStyle(new BasicStyle(new Color(255, 128, 69)));
                        }
                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.SquareVertexStyle());
                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle());
                        layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
                        geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
                    }catch(Exception e)
                    {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        logger.error("Exception: " + sw.toString());
                        JOptionPane optionPane = new JOptionPane(e.getMessage()+"Error al cargar la capa: "+layerName, JOptionPane.ERROR_MESSAGE);
                        JDialog dialog = optionPane.createDialog(null, "ERROR");
                        dialog.show();
                    }
                }


			return layerEspecial;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

			return null;

		}


	}
    public static GeopistaFeature saveFeature(Feature feature, GeopistaLayer layer, String layerName, String url)
    {
        GeopistaFeature geoFe=null;
        AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(url + "/AdministradorCartografiaServlet");
        try
        {
            //Feature f = (Feature) new WKTReader().read(new StringReader("POLYGON ((385586 4701116, 397937 4701116, 397937 4723004, 385586 4723004, 385586 4701116))")).getFeatures().get(0);
          //  Geometry gMunicipio = f.getGeometry();
            //acClient.lockLayer(layerName,gMunicipio);
            acClient.lockLayer(layerName,null);
            GeopistaFeature[] array= new GeopistaFeature[1];
            geoFe= (GeopistaFeature)feature;
            geoFe.setNew(true);
            geoFe.setSystemId(null);
            geoFe.setLayer(layer);
            array[0]=geoFe;
            Integer srid = null;
            try{
            	srid = geoFe.getLayer().getLayerManager().getCoordinateSystem().getEPSGCode();
            }catch (UnsupportedOperationException e){
				ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
            	srid = new Integer(acClient.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad())));
            }

            Collection cFeature =  acClient.uploadFeatures("es_ES",array,srid);
            if (cFeature.iterator().hasNext())
            {
                GeopistaFeature aux = (GeopistaFeature) cFeature.iterator().next();
                geoFe=aux;
            }
        }catch(Exception e)
        {
            System.out.println("Error al grabar el feature:"+e.toString());
        }
        finally
        {
            try{acClient.unlockLayer(layerName);}catch(Exception e){}
        }
        return geoFe;

    }
    public static GeopistaFeature updateFeature(Feature feature, GeopistaLayer layer, String name, String url)
    {
           GeopistaFeature geoFe=null;
           AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(url + "/AdministradorCartografiaServlet");
           try
           {
               GeopistaFeature[] array= new GeopistaFeature[1];
               geoFe= (GeopistaFeature)feature;
               geoFe.setNew(false);
               geoFe.setDirty(true);
               geoFe.setLayer(layer);
               array[0]=geoFe;
               acClient.lockFeature(name,new Integer(geoFe.getSystemId()).intValue());
               Collection cFeature =  acClient.uploadFeatures("es_ES",array);
               if (cFeature.iterator().hasNext())
               {
                   GeopistaFeature aux = (GeopistaFeature) cFeature.iterator().next();
                   geoFe=aux;
               }
           }catch(Exception e)
           {
               System.out.println("Error al grabar el feature:"+e.toString()+"\nURL: "+url);
           }
           finally
           {
              try{acClient.unlockFeature(name,new Integer(geoFe.getSystemId()).intValue());}catch(Exception ex){}
           }
           return geoFe;
       }
       public static GeopistaFeature deleteFeature(Feature feature, GeopistaLayer layer, String name, String url)
      {
           GeopistaFeature geoFe=null;
           AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(url + "/AdministradorCartografiaServlet");
           try
           {
               GeopistaFeature[] array= new GeopistaFeature[1];
               geoFe= (GeopistaFeature)feature;
               geoFe.setNew(false);
               geoFe.setDirty(true);
               geoFe.setDeleted(true);
               geoFe.setLayer(layer);
               array[0]=geoFe;
               acClient.lockFeature(name,new Integer(geoFe.getSystemId()).intValue());
               Collection cFeature =  acClient.uploadFeatures("es_ES",array);
               if (cFeature.iterator().hasNext())
               {
                   GeopistaFeature aux = (GeopistaFeature) cFeature.iterator().next();
                   geoFe=aux;
               }
           }catch(Exception e)
           {
               System.out.println("Error al grabar el feature:"+e.toString());
           }
           finally
           {
              try{acClient.unlockFeature(name,new Integer(geoFe.getSystemId()).intValue());}catch(Exception ex){}
           }
           return geoFe;
       }

    public static void deleteFeature(GeopistaEditor geopistaEditor)
    {
         geopistaEditor.deleteSelectedFeatures();
    }

    public static boolean refreshFeatureSelection(GeopistaEditor geopistaEditor,GeopistaLayer geopistaLayer,String id) {

		try {
            if (id==null||geopistaEditor==null)return false;
            geopistaEditor.getSelectionManager().clear();
            Collection collection = searchByAttribute(geopistaLayer, "Id", id);
			Iterator it = collection.iterator();
			if (it.hasNext()) {
				Feature feature = (Feature) it.next();
				geopistaEditor.select(geopistaLayer, feature);
			}
			geopistaEditor.zoomToSelected();
			return true;
		} catch (Exception ex) {
			logger.error("Exception al seleccionar la feature: ",ex);
			return false;
		}
	}
      public static Collection searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value) {
            Collection finalFeatures = new ArrayList();

            java.util.List allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
            Iterator allFeaturesListIter = allFeaturesList.iterator();
            while (allFeaturesListIter.hasNext()) {
                Feature localFeature = (Feature) allFeaturesListIter.next();

                String nombreAtributo = localFeature.getString(attributeName).trim();

                if (nombreAtributo.equals(value)) {
                    finalFeatures.add(localFeature);
                }
            }

            return finalFeatures;
        }
        public static String checkNull(Object o) {
		if (o == null) {
			return "";
		}
    	return o.toString();
	}

    public static void setLayerVisible(GeopistaEditor editor, Object[] layersname, boolean b){
        if ((editor == null) || (layersname == null)) return;
        for (int i=0; i<layersname.length; i++){
            Layer layer= editor.getLayerManager().getLayer((String)layersname[i]);
            if (layer != null){
                layer.setVisible(b);
                layer.setEditable(b);
            }
        }
    }

    public static void allLayersVisible(GeopistaEditor editor){
        if (editor == null) return;
        java.util.List list= editor.getLayerManager().getLayers();
        for (int i=0; i<list.size(); i++){
            Layer layer= (Layer)list.get(i);
            layer.setVisible(true);
        }
    }

    public static void allLayersEditable(GeopistaEditor editor, boolean value){
        if (editor == null) return;
        java.util.List list= editor.getLayerManager().getLayers();
        for (int i=0; i<list.size(); i++){
            Layer layer= (Layer)list.get(i);
            layer.setEditable(value);
            //((GeopistaLayer)layer).setActiva(value);
        }
    }

    public static void setLayersVisible(GeopistaEditor editor, Object[] visibles, Object[] novisibles){
        if ((visibles == null) && (novisibles == null)) allLayersVisible(editor);
        else{
            setLayerVisible(editor, visibles, true);
            setLayerVisible(editor, novisibles, false);
        }
    }

    public static void setLayersVisible(GeopistaEditor editor, String visible, String novisible){
        if ((visible == null) && (novisible == null)) allLayersVisible(editor);
        else{
            Object[] visibles= new Object[1];
            visibles[0]= visible;
            Object[] novisibles= new Object[1];
            novisibles[0]= novisible;
            setLayerVisible(editor, visibles, true);
            setLayerVisible(editor, novisibles, false);
        }
    }


}
