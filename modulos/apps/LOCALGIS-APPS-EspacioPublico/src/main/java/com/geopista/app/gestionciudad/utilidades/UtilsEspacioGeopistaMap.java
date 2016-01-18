/**
 * UtilsEspacioGeopistaMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.gestionciudad.utilidades;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.GeopistaAcl;
import com.localgis.app.gestionciudad.LocalGISGestionCiudad;
import com.localgis.app.gestionciudad.dialogs.main.GeopistaEditorPanel;
import com.localgis.app.gestionciudad.dialogs.main.PrintEditorPanel;



public class UtilsEspacioGeopistaMap {

	private static Logger logger = Logger.getLogger(UtilsEspacioGeopistaMap.class);

	private static Hashtable htMapReload = new Hashtable();
	

	/**
	 * Metodo que carga un mapa en un frame pasado por parametro. LLama a los metodos de geopista y carga los plugin
	 * que no se pueden cargar desde el workbench.
	 *
	 * @param padre Frame que donde se carga.
	 * @param panel Panel donde se carga.
	 * @param geopistaEditor Editor de geopista.
	 * @param id_map El id del mapa.
	 * @param editable Booleano que indica si se podra editar el mapa.
	 * @param visible
	 * @param novisible
	 * @return boolean Devuelve un boolean indicando si se ha realizado correctamente o no.
	 * */
	@SuppressWarnings("unchecked")
	public static boolean showGeopistaMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible,Boolean isPrint)
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			//			geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

			// comprobamos que ya haya sido cargado el mapa para no volver a hacerlo 
			if ((geopistaEditor.getLayerManager().getLayers() != null) &&  
					!getMapToReload(id_map) && (geopistaEditor.getLayerManager().getLayers().size() > 0) 

					)
			{
				//Incidencia 0000146. Dejamos que las capas se fijen tal cual se definieron en el mapa
				//ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			boolean isNew = false;
			try
			{

				if((AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor") == null || 
						((GeopistaEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor() == null ||
						((GeopistaEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor().getLayerManager().getLayers().size() == 0
						) && 
						!isPrint){
					isNew = true;
					geopistaEditor.loadMap("geopista:///"+id_map);
				}
				if((AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint") == null || 
						((PrintEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint")).getEditor() == null ||
						((PrintEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint")).getEditor().getLayerManager().getLayers().size() == 0
						) && 
						isPrint){
					isNew = true;
					geopistaEditor.loadMap("geopista:///"+id_map);
				}
				for(Iterator<GeopistaLayer> it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
				{
					GeopistaLayer layer=(GeopistaLayer)it.next();
					layer.setEditable(false);
					layer.setActiva(true);
				}

				setMapToReload(id_map, false);

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			if(isNew){
				//ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				//Incidencia 0000146. Dejamos que las capas se fijen tal cual se definieron en el mapa
				geopistaEditor.getSelectionManager().clear();
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
			}
			return true;	
		}
		catch(Exception e)
		{
			String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
			Throwable t=e.getCause();
			int i=0;
			while (t!=null&&i<10)
			{
				sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
				t=t.getCause();
				i++;
			}
			JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
			logger.error("Error al cargar el mapa de Registro de expediente: ",e);
			return false;
		}
	}

	/**
	 * Metodo que carga un mapa en un frame pasado por parametro. LLama a los metodos de geopista y carga los plugin
	 * que no se pueden cargar desde el workbench.
	 *
	 * @param padre Frame que donde se carga.
	 * @param panel Panel donde se carga.
	 * @param geopistaEditor Editor de geopista.
	 * @param id_map El id del mapa.
	 * @param editable Booleano que indica si se podra editar el mapa.
	 * @param visible
	 * @param novisible
	 * @return boolean Devuelve un boolean indicando si se ha realizado correctamente o no.
	 * */
	@SuppressWarnings("unchecked")
	public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible)
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

			/** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo */
			if ((geopistaEditor.getLayerManager().getLayers() != null) && 
					(geopistaEditor.getLayerManager().getLayers().size() > 0) &&
					!getMapToReload(id_map)
					)
			{
				//Incidencia 0000146. Dejamos que las capas se fijen tal cual se definieron en el mapa
				//ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				panel.add(geopistaEditor);
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			if (ShowMaps.showMap(CConstantesComando.adminCartografiaUrl,geopistaEditor, id_map, editable,padre)!=null)
			{
				try
				{
					for(Iterator<GeopistaLayer> it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
					{
						GeopistaLayer layer=(GeopistaLayer)it.next();
						layer.setEditable(false);
						layer.setActiva(false);
					}
				}catch(Exception e){}
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				panel.add(geopistaEditor);
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			return false;

		}
		catch(Exception e)
		{
			String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
			Throwable t=e.getCause();
			int i=0;
			while (t!=null&&i<10)
			{
				sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
				t=t.getCause();
				i++;
			}
			JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
			logger.error("Error al cargar el mapa de Registro de expediente: ",e);
			return false;
		}
	}



	@SuppressWarnings("unchecked")
	public static boolean showMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible)
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			//			geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

			try
			{

				geopistaEditor.loadMap("geopista:///"+id_map);

				for(Iterator<GeopistaLayer> it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
				{
					GeopistaLayer layer=(GeopistaLayer)it.next();
					layer.setEditable(false);
					layer.setActiva(true);
				}

				setMapToReload(id_map, false);

			}catch(Exception e){
				return false;
			}

			ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
			geopistaEditor.getSelectionManager().clear();
			geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
			return true;	
		}
		catch(Exception e)
		{
			String sMensaje=(e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
			Throwable t=e.getCause();
			int i=0;
			while (t!=null&&i<10)
			{
				sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
				t=t.getCause();
				i++;
			}
			JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
			logger.error("Error al cargar el mapa de Registro de expediente: ",e);
			return false;
		}
	}
	
	public static void setMapToReload(int idMap, boolean toReload) {
		htMapReload.put(idMap, toReload);
	}
	
	public static boolean getMapToReload (int idMap) {
		boolean toReload = false;
		if (htMapReload.get(idMap)!=null)
			toReload = Boolean.valueOf(htMapReload.get(idMap).toString());
		return toReload;
	}
}
