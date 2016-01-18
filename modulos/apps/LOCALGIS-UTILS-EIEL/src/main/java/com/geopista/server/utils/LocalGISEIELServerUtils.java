/**
 * LocalGISEIELServerUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 19-dic-2006
 * Time: 11:09:28
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase de utilidades que implementa fuciones que pueden usar varias clases. Clase estatica
 * que puede ser accesible desde la parte cliente.
 * */

public class LocalGISEIELServerUtils
{
	private static Logger logger = Logger.getLogger(LocalGISEIELServerUtils.class);
	private static Hashtable htMapReload = new Hashtable();

	/**
	 * Metodo que devuelve un String con la fecha del dia actual, con formato DD/MM/AAAA.
	 *
	 * @return String La fecha actual.
	 */
	public static String showToday()
	{
		Calendar cal = new GregorianCalendar();
		Locale locale = Locale.getDefault();
		cal = Calendar.getInstance(TimeZone.getTimeZone(locale.toString()));
		int anno = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1; // 0 == Enero
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		String sMes = "";
		String sDia = "";
		if (mes < 10)
			sMes = "0" + mes;
		else
			sMes = "" + mes;
		if (dia < 10)
			sDia = "0" + dia;
		else
			sDia = "" + dia;

		return sDia + "/" + sMes + "/" + anno;
	}

	/**
	 * Metodo que no permite que la pantalla se minimize mas de 600 en largo y del ancho pasado por parametro.
	 *
	 * @param desktop La ventana a analizar.
	 * @param wid El ancho pasado.
	 * @param hig El largo pasado.
	 */
	public static void ajustaVentana(JFrame desktop, int wid, int hig)
	{
		if (desktop.getWidth()<wid || desktop.getHeight()<600)
		{
			desktop.setSize(wid, hig);
		}
	}



	/**
	 * Metodo que añade un asterisco a la derecha a un String.
	 *
	 * @param s String al que añadir el asterisco.
	 * @return String El resultado de añadir el asterisco.
	 */
	public static String annadirAsterisco(String s)
	{
		return "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\" size=\"5\"><b>*</b></FONT></p></html>";
	}

	/**
	 * Metodo que añade un asterisco a la izquierda de un label.
	 *
	 * @param s String al que añadir el asterisco.
	 * @return String El resultado de añadir el asterisco
	 */
	public static String getLabelConAsterisco(String s)
	{
		return "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + " " + s + "</p></html>";
	}



	/**
	 * Metodo que borra caracteres de componentes editables.
	 *
	 * @param comp El componente en el que borrar los caracteres.
	 * @param maxLong El tamaño maximo que se quiere en el componente.
	 * */
	public static void retrocedeCaracter(final JTextComponent comp, final int maxLong)
	{
		Runnable checkLength = new Runnable()
		{
			public void run()
			{
				comp.setText(comp.getText().substring(0,maxLong));
			}
		};
		SwingUtilities.invokeLater(checkLength);
	}


	/**
	 * Devuelve un booleano indicando si un String tiene formato de fecha o no.
	 *
	 * @param fecha String de la fecha.
	 * @return boolean Indica si es una fecha o no.
	 * */


	

	/**
	 * Metodo que convierte una hora en formato Time a una hora en formato String de la forma HH:MM:SS
	 *
	 * @param hora La hora que se desea convertir.
	 * @return String El resultado.
	 * */
	public static String formatHora(Time hora)
	{
		if (hora == null)
		{
			return "";
		}
		return new SimpleDateFormat("HH:MM:SS").format(hora);
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
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				panel.add(geopistaEditor);
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			if (ShowMaps.showMap(CConstantesComando.adminCartografiaUrl,geopistaEditor, id_map, editable,padre)!=null)
			{
				try
				{
					for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
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
	public static boolean showGeopistaMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible,TaskMonitor monitor)throws CancelException
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

			
			
			if ((geopistaEditor.getLayerManager().getLayers() != null) && 
					!getMapToReload(id_map) &&
					(geopistaEditor.getLayerManager().getLayers().size() > 0) 
					
			)
			{
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			try
			{
				geopistaEditor.loadMap("geopista:///"+id_map,monitor);

				for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
				{
					GeopistaLayer layer=(GeopistaLayer)it.next();
					layer.setEditable(false);
					layer.setActiva(true);
				}
				
				setMapToReload(id_map, false);
			}
			catch (CancelException e1){
				throw e1;
			
			}catch(Exception e){
				return false;
			}

			ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
			geopistaEditor.getSelectionManager().clear();
			geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
			return true;	
		}
	  catch (CancelException e1){
    	  throw e1;
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
	 * Carga el mapa independiente de que este cargado uno antes, incluye todas las capas del mapa anterior
	 * @param padre
	 * @param geopistaEditor
	 * @param id_map
	 * @param editable
	 * @param visible
	 * @param novisible
	 * @param filtro
	 * @param monitor
	 * @return
	 * @throws CancelException
	 */
	public static boolean forceLoadGeopistaMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible,String[] filtro,TaskMonitor monitor)throws CancelException
	{
		try
		{			
			try
			{								
				geopistaEditor.loadMap("geopista:///"+id_map,filtro,monitor);
				for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
				{
					GeopistaLayer layer=(GeopistaLayer)it.next();
					layer.setEditable(false);
					
					if ((layer.getSystemId().equals("NP")) || (layer.getSystemId().equals("municipios")))
						layer.setActiva(false);
					else
						layer.setActiva(true);
				}
				
				setMapToReload(id_map, false);
			}
			catch (CancelException e1){
				throw e1;
			
			}catch(Exception e){
				return false;
			}

			ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
			geopistaEditor.getSelectionManager().clear();
			geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
			return true;	
		}
	  catch (CancelException e1){
    	  throw e1;
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

	
	public static boolean showMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible)
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
			
			try
			{
				
//				geopistaEditor.loadMapEIEL("geopista:///"+id_map);
				geopistaEditor.loadMap("geopista:///"+id_map);

				for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
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
	 * Metodo que devuelve un arrayList con features que concuerden con los parametros pasados.
	 *
	 * @param geopistaLayer Capa geopista donde buscar.
	 * @param attributeName El nombre del atributo buscado
	 * @param value El valor del atributo buscado.
	 * @return Collection El resultado.
	 * */
	public static Collection searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value)
	{
		Collection finalFeatures = new ArrayList();

		List allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreAtributo = localFeature.getString(attributeName).trim();

			if (nombreAtributo.equals(value))
			{
				finalFeatures.add(localFeature);
			}
		}
		return finalFeatures;
	}

	/**
	 * Metodo que comprueba si un objeto es null, si es devuelve "", si no devuelve el objeto en formato string.
	 *
	 * @param o Objeto a analizar.
	 * @return String El obejto en formato string.
	 * */
	public static String checkNull(Object o)
	{
		if (o == null)
		{
			return "";
		}
		return o.toString();
	}

	public static void setMapToReload(int idMap, boolean toReload)
	{
		htMapReload.put(idMap, toReload);
	}

	public static boolean getMapToReload (int idMap)
	{
		boolean toReload = true;
		if (htMapReload.get(idMap)!=null)
			toReload = Boolean.valueOf(htMapReload.get(idMap).toString());
		return toReload;
	}
	
	

    public static String getNameFromEstructura(String patron) {
		ListaEstructuras lista=Estructuras.getListaTipos();
		Vector vDomainNodes=lista.getListaSorted(UserPreferenceStore
	            .getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES",
	                    false));
       String name=null;
       boolean encontrado=false;
        if (vDomainNodes!=null)
        {
        	Enumeration e=vDomainNodes.elements();
            while (encontrado==false && e.hasMoreElements())
            {
                   DomainNode auxDomain= (DomainNode)e.nextElement();
                   if(auxDomain.getPatron().equals(patron)){
                	   encontrado=true;
                   		name=auxDomain.getFirstTerm();
                   }
                   	
                   
            }
        }
        return name;
	}
	


}
