/**
 * GestorFipUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.utils;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaAcl;
import com.gestorfip.app.planeamiento.IMainLocalGisGestorFip;
import com.gestorfip.app.planeamiento.dialogs.panels.DialogoCalendario;
import com.vividsolutions.jump.feature.Feature;

/**
 * Clase de utilidades que implementa fuciones que pueden usar varias clases. Clase estatica
 * que puede ser accesible desde la parte cliente.
 * */

public class GestorFipUtils
{
	private static Logger logger = Logger.getLogger(GestorFipUtils.class);
	@SuppressWarnings("unchecked")
	private static Hashtable htMapReload = new Hashtable();
	
	public static Icon iconoZoom;

	private final static int TAMANIO_CODIGO = 10;
	
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
	 * Metodo que aï¿½ade un asterisco a la derecha a un String.
	 *
	 * @param s String al que aï¿½adir el asterisco.
	 * @return String El resultado de aï¿½adir el asterisco.
	 */
	public static String annadirAsterisco(String s)
	{
		return "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\" size=\"5\"><b>*</b></FONT></p></html>";
	}

	/**
	 * Metodo que aï¿½ade un asterisco a la izquierda de un label.
	 *
	 * @param s String al que aï¿½adir el asterisco.
	 * @return String El resultado de aï¿½adir el asterisco
	 */
	public static String getLabelConAsterisco(String s)
	{
		return "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + " " + s + "</p></html>";
	}

	/**
	 * Devuelve un booleano indicando si un String tiene formato de fecha o no.
	 *
	 * @param fecha String de la fecha.
	 * @return boolean Indica si es una fecha o no.
	 * */


	/**
	 * Metodo que convierte una fecha en formato Date a una fecha en formato String de la forma DD/MM/AAAA
	 *
	 * @param fecha La fecha que se desea convertir.
	 * @return String El resultado.
	 * */
	public static String formatFecha(Date fecha)
	{
		if (fecha == null)
		{
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}

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
	@SuppressWarnings("unchecked")
	public static boolean showGeopistaMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
			boolean editable, String visible, String novisible, String nameMapa, boolean isReloadMap)
	{
		try
		{
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
			//geopistaEditor.addCursorTool("RPM tool", "com.gestorfip.ui.tool.rpm.RPMTool");

			// comprobamos que ya haya sido cargado el mapa para no volver a hacerlo 
			if ((geopistaEditor.getLayerManager().getLayers() != null) && 
					!getMapToReload(id_map, isReloadMap) &&
					(geopistaEditor.getLayerManager().getLayers().size() > 0) 

			)
			{
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			boolean isNew = false;
			try
			{

				 if((AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorEntidadesGraficas") == null || 
						geopistaEditor == null ||
						geopistaEditor.getLayerManager().getLayers().size() == 0 ||
						nameMapa.equals(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_ENTIDADES_GRAFICAS))){
					isNew = true;
					geopistaEditor.loadMap("geopista:///"+id_map);
					AppContext.getApplicationContext().getBlackboard().put(ConstantesGestorFIP.IS_MAP_RELOAD_ENT_GRAF,false);
				}
				
				else if((AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorAsociacionDeterminacionesEntidades") == null || 
						geopistaEditor == null ||
						geopistaEditor.getLayerManager().getLayers().size() == 0 ||
						nameMapa.equals(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES))){
					isNew = true;
					geopistaEditor.loadMap("geopista:///"+id_map);
					AppContext.getApplicationContext().getBlackboard().put(ConstantesGestorFIP.IS_MAP_RELOAD_ASOC_DET_ENT,false);
				} 
				else if((AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorAsociacionDocumentosEntidadesGraficas") == null || 
						geopistaEditor == null ||
						geopistaEditor.getLayerManager().getLayers().size() == 0 ||
						nameMapa.equals(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DOCUMENTOS_GRAFICOS))){
					isNew = true;
					geopistaEditor.loadMap("geopista:///"+id_map);
					AppContext.getApplicationContext().getBlackboard().put(ConstantesGestorFIP.IS_MAP_RELOAD_DOCUMENTOS,false);
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
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
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

	/**
	 * Metodo que devuelve un arrayList con features que concuerden con los parametros pasados.
	 *
	 * @param geopistaLayer Capa geopista donde buscar.
	 * @param attributeName El nombre del atributo buscado
	 * @param value El valor del atributo buscado.
	 * @return Collection El resultado.
	 * */
	@SuppressWarnings("unchecked")
	public static Collection<Feature> searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value)
	{
		Collection<Feature> finalFeatures = new ArrayList<Feature>();

		List<Feature> allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator<Feature> allFeaturesListIter = allFeaturesList.iterator();
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

//	public static void activarDesactivarMenuPlaneamiento(boolean b, JFrame desktop){
//		if(!ConstantesGestorFIP.permisos.contains("LocalGIS.GestorFip.Consulta")){
//			desktop.getJMenuBar().getComponent(2).setEnabled(false);
//		}
//		else{
//			desktop.getJMenuBar().getComponent(2).setEnabled(b);
//		}
//	}
	
	public static void menuBarSetEnabled(boolean b, JFrame desktop)
	{
		try
		{           
			Component[] c = desktop.getJMenuBar().getComponents();
			for (int i=0; i<c.length; i++)
			{
				if (c[i] instanceof JMenu)
				{
					c[i].setEnabled(b);
				}
			}
				
			if(!ConstantesGestorFIP.permisos.containsKey("LocalGIS.GestorFip.Importador")){
				desktop.getJMenuBar().getComponent(0).setEnabled(false);
			}
			
			if(!ConstantesGestorFIP.permisos.containsKey("LocalGIS.GestorFip.Consulta")){
				desktop.getJMenuBar().getComponent(1).setEnabled(false);
//				desktop.getJMenuBar().getComponent(2).setEnabled(false);
			}

		} catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}
	}

	@SuppressWarnings("unchecked")
	public static void setMapToReload(int idMap, boolean toReload)
	{
		htMapReload.put(idMap, toReload);
	}

	public static boolean getMapToReload (int idMap, boolean isReloadMap)
	{
		boolean toReload = false;
		if (htMapReload.get(idMap)!=null)
			toReload = Boolean.valueOf(htMapReload.get(idMap).toString());
		
		if(isReloadMap)
			toReload = true;
		
		return toReload;
	}


	public static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null){
			return AppContext.getApplicationContext().getMainFrame();
		}
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog){
			return (Window)parentComponent;
		}
		return getWindowForComponent(parentComponent.getParent());
	}
	
    /**
     * Metodo que lanza el dialogo calendario para que el usuario elija una fecha.
     *
     * @param frame Frame que lanza el dialogo.
     * @return Date Fecha elegida por el usuario.
     */
    public static java.util.Date showCalendarDialog(JFrame frame)
    {
		DialogoCalendario dialog = new DialogoCalendario(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
		return dialog.getFecha();
	}
    
    /**
     * Metodo que inicializa en memoria los iconos que se van a visualizar en la aplicacion.
     */
    public static void inicializarIconos()
    {
        ClassLoader cl= (new GestorFipUtils()).getClass().getClassLoader();
     
        iconoZoom = new javax.swing.ImageIcon(cl.getResource("com/gestorfip/app/planeamiento/images/zoom.gif"));
        
    }
    

	public static String completarCodigoConCeros(String codigo){
		
		if(codigo.length()<10){
			String ceroIzquierda = "0";
			ceroIzquierda=ceroIzquierda.concat(codigo);
			codigo = completarCodigoConCeros(ceroIzquierda);
		}
		return codigo;
	}


}
