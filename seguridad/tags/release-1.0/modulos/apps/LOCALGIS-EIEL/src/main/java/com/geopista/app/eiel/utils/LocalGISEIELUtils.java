package com.geopista.app.eiel.utils;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.IMainLocalGISEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.panels.EditorPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.editor.GeopistaEditor;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
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

public class LocalGISEIELUtils
{
	private static Logger logger = Logger.getLogger(LocalGISEIELUtils.class);
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

			if (b)
			{
				GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
				((IMainLocalGISEIEL)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
			}

		} catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}
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
	
	/*
	 * Devuelve values en el mismo orden separados por separator e inserta como primer valor la fase
	 * Si algún valor es null no se inserta.
	 */
	
	public static String getRowToMPT(String fase,String separator,ArrayList values){
		String cadena="";
		
		cadena=fase+separator;
		for (int i = 0; i < values.size()-1; i++) {
			if(values.get(i)!=null){
				if(values.get(i) instanceof java.lang.Double){
				
					cadena+=Double.toString((Double)values.get(i)).replace(".", ",")+separator;
				}else
					cadena+=values.get(i)+separator;		
			}else
				cadena+=separator;
		}
		if(values.get(values.size()-1)!=null)
			if(values.get(values.size()-1) instanceof java.lang.Double){
				
				cadena+=Double.toString((Double)values.get(values.size()-1)).replace(".", ",");
			}else
				cadena+=values.get(values.size()-1);	
		
		return cadena;
	}
	
	public static double redondear(double numero,int digitos)
	{
	      int cifras=(int) Math.pow(10,digitos);
	      return Math.rint(numero*cifras)/cifras;
	}

    public static String getNameFromEstructura(String patron) {
		ListaEstructuras lista=Estructuras.getListaTipos();
		Vector vDomainNodes=lista.getListaSorted(AppContext.getApplicationContext()
	            .getUserPreference(AppContext.PREFERENCES_LOCALE_KEY, "es_ES",
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
	
    
    /**
	 * Lista de municipios del nucleo.
	 * @return
	 */
	public static void storeNucleosMunicipio(){
		
		HashMap nucleosMunicipio=new HashMap();
		Collection c=null;
        try {
			c = ConstantesLocalGISEIEL.clienteLocalGISEIEL.getNucleosEIEL();
			//Los ordenamos alfabeticamente		
			Object[] arrayNodos= c.toArray();
	    	Arrays.sort(arrayNodos,new NodoComparatorByDenominacion());
	
	    	//Los devolvemos ordenados
	    	c.clear();
			for (int i = 0; i < arrayNodos.length; ++i) {
				LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) arrayNodos[i];
				c.add(nucleoEIEL);
			}
			
			java.util.Iterator it=c.iterator();
	        while (it.hasNext()){
				LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) it.next();
				String clave=nucleoEIEL.getCodentidad()+"_"+nucleoEIEL.getCodpoblamiento();
				nucleosMunicipio.put(clave,nucleoEIEL);
				
				
			}
	        ApplicationContext aplicacion = (AppContext) AppContext.getApplicationContext();
	       
			aplicacion.getBlackboard().put("HASH_NUCLEOS_MUNICIPIO",nucleosMunicipio);
			aplicacion.getBlackboard().put("VECTOR_NUCLEOS_MUNICIPIO",c);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	
    
    public static void inicializarConstantesMunicipio(){
		ConstantesRegistro.IdMunicipio = AppContext.getIdMunicipio();
		com.geopista.app.administrador.init.Constantes.idEntidad = AppContext.getIdEntidad();
		com.geopista.app.administrador.init.Constantes.idMunicipio = AppContext.getIdMunicipio();
		com.geopista.app.administrador.init.Constantes.url = AppContext	.getApplicationContext().getString("geopista.conexion.servidorurl")+ "geopista";

		ConstantesLocalGISEIEL.idProvincia = String.valueOf(AppContext.getIdMunicipio()).substring(0, 2);
		ConstantesLocalGISEIEL.idMunicipio = String.valueOf(AppContext.getIdMunicipio());
		
    }
	
    
    /**
     * Al realizar un cambio de municipio es preciso realizar una serie de cambios en las variables del sistema
     * @param idMunicipio
     * @param editorPanel 
     */
    public static void actualizarInformacionMunicipioSeleccionado(String idMunicipio, EditorPanel editorPanel){
    	
    	ConstantesLocalGISEIEL.idMunicipio=idMunicipio;
    	
    	//Inicializamos las constantes del municipio
    	inicializarConstantesMunicipio();
    	
    	//Recargamos la lista de nucleos del municipio
    	LocalGISEIELUtils.storeNucleosMunicipio();
    	
    	//Recargamos la lista de dominios del municipio
    	//TODO
    	
    	try {
			//Realizamos un zoom al municipio
			if (AppContext.getApplicationContext().getBlackboard().get(AppContext.SEL_MUNI_AUTO)!=null
					&& ((Boolean)AppContext.getApplicationContext().getBlackboard().get(AppContext.SEL_MUNI_AUTO)).booleanValue()){
				//En el caso de automatico no hacemos nada, porque sería un lio realizar zooms automaticos
			}
			else{
			    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPA_MUNICIPIOS);
			    if (geopistaLayer!=null){
			    	GeopistaConnection geopistaConnection = (GeopistaConnection) geopistaLayer.getDataSourceQuery().getDataSource().getConnection();
			    	String sridDefecto=null;
				 	if (AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA) != null){
				 		sridDefecto=(String)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA);
				 	}else{	           				
				 		ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
				 		sridDefecto = geopistaConnection.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
				 	}
				 	Geometry geom =  geopistaConnection.obtenerGeometriaMunicipio(Integer.parseInt(idMunicipio));
					geom.setSRID(Integer.parseInt(sridDefecto));
					
					CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
			    	CoordinateSystem inCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt("4230"));
			    	Reprojector.instance().reproject(geom,inCoord, outCoord);
			    	

					
					Envelope envelopeFeature = geom.getEnvelopeInternal();

					// ESTABLECEMOS UN FACTOR DE TOLERANCIA PARA QUE EL ZOOM
					// SE AJUSTE EXACTAMENTE A LA FEATURE Y NOS IMPIDA VER
					// EL ENTORNO DE LA FEATURE

					int tolerancia = 5;

					// CALCULAMOS EL ENVELOPE DE LA FEATURE
					double minX = envelopeFeature.getMinX() - (envelopeFeature.getWidth() / tolerancia);
					double maxX = envelopeFeature.getMaxX() + (envelopeFeature.getWidth() / tolerancia);
					double minY = envelopeFeature.getMinY() - (envelopeFeature.getHeight() / tolerancia);
					double maxY = envelopeFeature.getMaxY() + (envelopeFeature.getHeight() / tolerancia);

					// INICIALIZAMOS EL ENVELOPE
					envelopeFeature.init(minX, maxX, minY, maxY);

					// ZOOM AL ENVELOPE DE LA FEATURE
					GeopistaEditor editor=((GeopistaEditorPanel)editorPanel.getGeopistaEditorPanel()).getEditor();
					editor.getLayerViewPanel().getViewport().zoom(envelopeFeature);
			    }
			}
    	}
    	catch (Exception e){
    		
    	}
    }

}
