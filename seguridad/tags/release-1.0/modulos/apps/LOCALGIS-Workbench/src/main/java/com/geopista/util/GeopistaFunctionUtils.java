package com.geopista.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.enhydra.jdbc.standard.StandardXAPreparedStatement;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.sql.GEOPISTAPreparedStatement;
import com.geopista.ui.GeopistaLayerNameRenderer;
import com.geopista.util.expression.FeatureExpresionParser;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.LangUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.model.UndoableEditReceiver;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 * Funciones útiles
 */
public class GeopistaFunctionUtils extends I18NUtils
{
	/**
	 * Logger for this class
	 */
	//private static final Log	logger	= LogFactory.getLog(GeopistaFunctionUtils.class);
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GeopistaFunctionUtils.class);

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	//CTES HTTP
	public static final String HTTP_FILE_TYPE_HEADER = "fileType";
	public static final String HTTP_ZIP_REFER_HEADER = "zipReference";
	public static final String HTTP_PROP_HEADER = "PROPERTIES";
	public static final String HTTP_ZIP_HEADER = "ZIP";	

    /**
     * Obtiene un Frame
     * @param componenteGui 
     * @return 
     */
	public static JFrame getFrame(WorkbenchGuiComponent componenteGui)
	{
		JFrame framePadre;
		try
		{
			framePadre = (JFrame) componenteGui;

			return framePadre;
		} catch (Exception e)
		{
			try
			{
				framePadre = (JFrame) SwingUtilities.getAncestorOfClass(Frame.class, (Component) componenteGui);
			} catch (ClassCastException e1)
			{
				JFrame centrar = new JFrame();
				centrar.setLocation(300, 300);
				return centrar;
			}

			return framePadre;
		}
	}

    /**
     * Obtiene printer preferida
     * @param x 
     * @param y 
     * @param layerViewPanel 
     * @return 
     */
	public static Envelope getPreferredPrintEnvelope(int x, int y, ILayerViewPanel layerViewPanel)
	{
		int altoPanel = layerViewPanel.getViewport().getPanel().getHeight();
		int anchoPanel = layerViewPanel.getViewport().getPanel().getWidth();
		double imgratio = (double) x / (double) y;
		double viewratio = (double) anchoPanel / (double) altoPanel;

		// BufferedImage image = new BufferedImage(x,y,
				// BufferedImage.TYPE_INT_RGB);
		// Graphics2D g = image.createGraphics();

		Envelope envelope = layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
		Envelope newenvelope = null;
		if (viewratio > 1) // apaisado
		{
			// calcula el zoom necesario para obtener el nuevo ancho
			newenvelope = new Envelope(envelope.getMinX() - envelope.getHeight() * imgratio / 2, envelope.getMaxX() + envelope.getHeight() * imgratio / 2, envelope.getMinY(),

					envelope.getMaxY());

		} else
			// portrait
		{

			newenvelope = new Envelope(envelope.getMinX(), envelope.getMaxX(), envelope.getMinY() - envelope.getWidth() / imgratio / 2,

					envelope.getMaxY() + envelope.getWidth() / imgratio / 2);

		}
		return envelope;
	}

    /**
     * Obtiene printer preferida
     * @param x 
     * @param y 
     * @param layerViewPanel 
     * @return 
     */
	public static Image printMap(int x, int y, ILayerViewPanel layerViewPanel) 
	{
		int w=layerViewPanel.getWidth();
		int h=layerViewPanel.getHeight();
//		Envelope newenvelope = getPreferredPrintEnvelope(x, y, layerViewPanel);
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
//		Envelope oldenvelope= layerViewPanel.getViewport().getEnvelopeInModelCoordinates();

		BufferedImage image2 = new BufferedImage(layerViewPanel.getWidth(),
				layerViewPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		layerViewPanel.paintComponent((Graphics2D) image2.getGraphics());


		// escala llenando todo el thumbnail recortando si es necesario
		double scalex=((double)w)/x;
		double scaley=((double)h)/y;
		double scale=Math.min(scalex,scaley);
		int newW = (int)(w/scale);
		int newH = (int)(h/scale);
		Image scaledImage=image2.getScaledInstance(newW,newH,BufferedImage.SCALE_SMOOTH);
		int newX = (int)(x/2-w/scale/2);
		int newY = (int)( y/2-h/scale/2);
		graphics.drawImage(scaledImage,newX,newY,newW,newH,Color.WHITE,null);
		//print(graphics, layerViewPanel.getLayerManager().getVisibleLayerables(Layerable.class, false), newenvelope, x, y);
		return image;
	}
    /**
     * Imprime el mapa sin redimensionar ni cortar
     * @param layerViewPanel 
     * @return 
     */
	public static Image printMapNoResize(LayerViewPanel layerViewPanel) 
	{
		BufferedImage image2 = new BufferedImage(layerViewPanel.getWidth(),
				layerViewPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		layerViewPanel.paintComponent((Graphics2D) image2.getGraphics());

		return image2;
	}


	private static void paintBackground(Graphics2D graphics, int extentW, int extentH)
	{
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, extentW, extentH);
	}

	private String addExtension(String path, String extension)
	{
		if (path.toUpperCase().endsWith(extension.toUpperCase()))
		{
			return path;
		}
		if (path.endsWith("."))
		{
			return path + extension;
		}
		return path + "." + extension;
	}

	/*
	 * Esta clase devuelve un ArrayList con la ruta de los ficheros de 
	 * extension .gpc a partir de una rutaBase dada
	 */
    /**
     * Busca en un directorio
     * @param rutaBase 
     * @param progressDialog 
     * @return 
     */
	public static ArrayList searchDirectory(String rutaBase, TaskMonitorDialog progressDialog)
	{
		// rutaBase SERÁ LA RUTA A PARTIR DE LA CUAL SE BUSCARAN
		// LOS FICHEROS

		File dir = new File(rutaBase);


		// FILTRO PARA DIRECTORIOS
		FileFilter fileFilter = new FileFilter()
		{
			public boolean accept(File file)
			{
				return file.isDirectory();
			}
		};
		// LISTAR DIRECTORIOS
		File[] files = dir.listFiles(fileFilter);

		// CREAMOS UN ARRAYLIST CON LOS OBJETOS GEOPISTAMAPLIST
		ArrayList listaMap = new ArrayList();
		for (int i = 0; i < files.length; i++)
		{
			GeopistaMap mapaDeLista = GeopistaFunctionUtils.searchFile(files[i].getPath(), "gpc", "thumb.png");
			if (progressDialog!=null && progressDialog.isCancelRequested())
				return listaMap;
			if (mapaDeLista != null)
			{
				if (progressDialog!=null)

					listaMap.add(mapaDeLista);
				progressDialog.report(listaMap.size(),-1,mapaDeLista.getName());
			}
		}

		/*
		 * Iterator Iter = listaMap.iterator(); while (Iter.hasNext()) {
		 * GeopistaMapList mapa = (GeopistaMapList)Iter.next(); }
		 */

		return listaMap;

	}

    /**
     * Busca un archivo
     * @param rutaBase 
     * @param extension 
     * @param thumb 
     * @return 
     */
	public static GeopistaMap searchFile(String rutaBase, String extension, final String thumb)
	{


		final String ext = "." + extension;
		GeopistaMap mapa = null;
		// rutaBase SERÁ LA RUTA A PARTIR DE LA CUAL SE BUSCARAN
		// LOS FICHEROS
		File dir = new File(rutaBase);


		// VARIABLES QUE SE PASAN AL GEOPISTAMAPLIST



		// FILTRO PARA EXTENSION
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return name.endsWith(ext);
			}
		};
		// LISTAR DIRECTORIOS
		File[] files = dir.listFiles(filter);

		for (int i = 0; i < files.length; i++)
		{
			// EN CASO DE EXISTIR EL FICHERO DEL PROYECTO .gpc MIRAMOS
			// LA EXISTENCIA DEL FICHERO DE THUMBNAIL ->
			FilenameFilter filterThumb = new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.equalsIgnoreCase(thumb);
				}
			};
			// BUSCAR EL THUMBNAIL
			File[] thumbs = dir.listFiles(filterThumb);

			mapa = null;
			try
			{
				 mapa = GeopistaMap.getMapISO88591(files[i].getPath());
				mapa.loadThumbnail();
			} catch (Exception e)
			{

				continue;
			}

			// ALMACENAMOS EN GEOPISTAMAPLIST



		}
		return mapa;

	}

	/**
	 * searchByAttribute(LayerManager layerManager, String layerName, String
	 * attributeName, String value) Método que busca las features que cumplen
	 * que un atributo es igual a un determinado valor dentro de una capa
	 * 
	 * @param layerManager :
	 *            Manejador de Capas
	 * @param layerName :
	 *            Nombre de la capa en que se realiza la búsqueda
	 * @param attributeName :
	 *            Nombre del atributo de la capa
	 * @param value :
	 *            Valor del atributo de la capa
	 * @return Devuelve un objeto de tipo Collection que contiene las features
	 *         que cumplen el criterio de búsqueda
	 */

	public static Collection searchByAttribute(LayerManager layerManager, String layerName, String attributeName, String value)
	{

		Collection finalFeatures = new ArrayList();

		Layer localLayer = layerManager.getLayer(layerName);
		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreFeature = localFeature.getString(attributeName).trim();

			if (nombreFeature.equals(value))
			{
				finalFeatures.add(localFeature);
			}
		}

		return finalFeatures;
	}


	/**
	 * searchByAttribute(Layer layer, String
	 * attributeName, String value) Método que busca las features que cumplen
	 * que un atributo es igual a un determinado valor dentro de una capa
	 * 
	 * @param layer :
	 *            capa en que se realiza la búsqueda
	 * @param attributeName :
	 *            Nombre del atributo de la capa
	 * @param value :
	 *            Valor del atributo de la capa
	 * @return Devuelve un objeto de tipo Collection que contiene las features
	 *         que cumplen el criterio de búsqueda
	 */

	public static Collection searchByAttribute(Layer layer, String attributeName, String value)
	{

		Collection finalFeatures = new ArrayList();


		List allFeaturesList = layer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreFeature = localFeature.getString(attributeName).trim();

			if (nombreFeature.equals(value.trim()))
			{
				finalFeatures.add(localFeature);
			}
		}

		return finalFeatures;
	}

	public static Collection searchByAttributeSpecial(Layer layer, String attributeName, String value)
	{

		Collection finalFeatures = new ArrayList();


		List allFeaturesList = layer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreFeature = localFeature.getString(attributeName).trim();
			nombreFeature=GeopistaFunctionUtils.removeSymbols(nombreFeature);

			if (compareSpecial(nombreFeature,value.trim()))
			{
				finalFeatures.add(localFeature);
			}
		}

		return finalFeatures;
	}
	
	public static String removeSymbols(String cadena){
		String[] removeSymbols={};
		String[] removeWords={};

		String strRemoveSimbols=aplicacion.getString("cargador.removeSymbols");
		String strRemoveWords=aplicacion.getString("cargador.removeWords");
		if (strRemoveSimbols!=null)
			removeSymbols = strRemoveSimbols.split(";");
		if (strRemoveWords!=null)
			removeWords= strRemoveWords.split(";");

		
		if (cadena!=null){
			cadena=cadena.toUpperCase();
			for (int i=0;i<removeSymbols.length;i++){
				cadena=cadena.replace(removeSymbols[i], " ");
			}
			
			for (int i=0;i<removeWords.length;i++){
				cadena=cadena.replaceAll(" "+removeWords[i]+" ", " ");
				cadena=cadena.replaceAll(" "+removeWords[i]+"$", " ");
				cadena=cadena.replaceAll(" "+removeWords[i]+"\\.", " ");
				cadena=cadena.replaceAll("^"+removeWords[i]+" ", " ");
				cadena=cadena.replaceAll("^"+removeWords[i]+"\\.", " ");
			}
		}
		return cadena;
	}
	
	public static boolean compareSpecial(String cadena1, String cadena2){
				
		if ((cadena1!=null && (cadena2!=null))){
			
			//System.out.println("Comparando:"+cadena1+" con "+ cadena2);
			//Si ambos son blancos no los casamos porque seria un error. 
			if ((cadena1.trim().equals(""))&& (cadena2.trim().equals(""))){				
					return false;
			}
			
			if (cadena1.trim().equals(cadena2.trim())){
				
				return true;
			}
			else{
				//Los nombres de catastro tienen una longitud maximo de mas o menos 19-20 caracteres. Los nombres
				//del INE son mas largos por lo que alguna vez en catastro se cortan. Si
				//literalmente no coinciden intentamos hacer un matching de 19-20 caracteres

				//Comprobamos si esta contenido el string. Señal de que el nombre puede estar cortado en catastro
				cadena1=cadena1.trim();
				cadena2=cadena2.trim();
				if (cadena2.length()>cadena1.length()){
					String subcadena=cadena2.substring(0,cadena1.length());
					if ((cadena1.trim().equals(""))&& (subcadena.trim().equals(""))){	
						return false;
					}
					else{
						if (cadena1.equals(subcadena))
							return true;
						else
							return false;
					}
				}
				return false;
			}
		}
		else
			return false;
				
	}
	public static void main(String args[]){
		
		
		String cadena1="FUENTE DE LA";
		String cadena2="ACEBO";
		
		cadena1=removeSymbols(cadena1);
		cadena2=removeSymbols(cadena2);
		boolean resultado=compareSpecial(cadena1,cadena2);
		System.out.println("Resultado:"+resultado);
		System.exit(1);
	}
	

	/**
     * searchByAttributes(LayerManager layerManager, String layerName, ArrayList
     * attributeNames, ArrayList values) Método que busca las features que
     * cumplen que varios atributos son iguales a unos determinado valores
     * dentro de una capa
     * 
     * 
     * @return Devuelve un objeto de tipo Collection que contiene las features
     *         que cumplen el criterio de búsqueda
     * @param attributeNames 
     * @param layerManager :
     *            Manejador de Capas
     * @param layerName :
     *            Nombre de la capa en que se realiza la búsqueda
     * @param values :
     *            Lista de Valores de los atributos por los que se buscará en la
     *            capa
     */

	public static Collection searchByAttributes(LayerManager layerManager, String layerName, ArrayList attributeNames, ArrayList values)
	{
		Collection finalFeatures = new ArrayList();

		Layer localLayer = layerManager.getLayer(layerName);
		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			Iterator atributosIter = attributeNames.iterator();
			Iterator valuesIter = values.iterator();
			boolean flag = true;
			boolean interruptor = true;
			String localAtributo = "";
			String valorFeature = "";
			String value = "";
			int i=0;
			while (atributosIter.hasNext())
			{
				localAtributo = (String) atributosIter.next();
				try
				{
					valorFeature = localFeature.getString(localAtributo).trim();

				} catch (Exception e)
				{
					logger.error("searchByAttributes(LayerManager, String, ArrayList, ArrayList)",e);
					break;
				}

				value = (String) valuesIter.next();
				// SI TODOS LOS ATRIBUTOS COINCIDEN flag es true

				valorFeature = valorFeature.replaceAll(" ","").toUpperCase();
				value = value.replaceAll(" ","").toUpperCase();

				//esta opcion nos dice si las cadenas son coincidentes desde el principiosin necesidad de escribir el valor entero
				//boolean valor3=valorFeature.regionMatches(0,value,0,value.length());

				//esta opcion em principio esta descartada ya que solo se utilizacia para cadenas que coincidieran exactamente
				//valorFeature.equalsIgnoreCase(value)

				//esta opcion se empleariamos si quisieramos buscar un valor en cualquier parte de la cadena
				//int val=valorFeature.indexOf(value);

				if (value.equals("") || valorFeature.equals("")){
					break;
				}else{

					if (valorFeature.regionMatches(0,value,0,value.length()))
					{
						flag = true;
						i++;

					} else
					{
						interruptor = false;
					}
				}


			}

			if (flag == true && i>0 && interruptor == true)
			{
				finalFeatures.add(localFeature);
				//break;
			}
		}
		// System.out.println("finalFeatures :"+finalFeatures.size());
		return finalFeatures;
	}
	/**
     * searchByAttributes(LayerManager layerManager, String layerName, ArrayList
     * attributeNames, ArrayList values) Método que busca las features que
     * cumplen que varios atributos son iguales a unos determinado valores
     * dentro de una capa
     * 
     * 
     * @return Devuelve un objeto de tipo Collection que contiene las features
     *         que cumplen el criterio de búsqueda
     * @param attributeNames 
     * @param layerManager :
     *            Manejador de Capas
     * @param layerName :
     *            Nombre de la capa en que se realiza la búsqueda
     * @param values :
     *            Lista de Valores de los atributos por los que se buscará en la
     *            capa
     * @param valscreen :
     *            Valor introducido por pantalla
     * @param screenAttrib :
     *            Atributo que corresponde con el valor introducido por pantalla
     * @param textliteral :
     *            Se Trata el de un valor pasado mediante la seleccion de un checkbox por pantalla
     */

	public static Collection searchByAttributes(LayerManager layerManager, String layerName, ArrayList attributeNames, ArrayList values,String valscreen,String screenAttrib,boolean textliteral)
	{
		Collection finalFeatures = new ArrayList();

		Layer localLayer = layerManager.getLayer(layerName);
		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			Iterator atributosIter = attributeNames.iterator();
			Iterator valuesIter = values.iterator();
			String localAtributo = "";
			String valorFeature = "";
			String valorFeature2="";
			String value = "";

			if (valscreen==null){
				while (atributosIter.hasNext())
				{
					localAtributo = (String) atributosIter.next();
					try
					{
						valorFeature = localFeature.getString(localAtributo).trim();

					} catch (Exception e)
					{
						logger.error("searchByAttributes(LayerManager, String, ArrayList, ArrayList)",e);
						break;
					}

					value = (String) valuesIter.next();
					// SI TODOS LOS ATRIBUTOS COINCIDEN flag es true

					valorFeature = valorFeature.replaceAll(" ","").toUpperCase();
					value = value.replaceAll(" ","").toUpperCase();

					//esta opcion nos dice si las cadenas son coincidentes desde el principiosin necesidad de escribir el valor entero
					//boolean valor3=valorFeature.regionMatches(0,value,0,value.length());

					//esta opcion em principio esta descartada ya que solo se utilizacia para cadenas que coincidieran exactamente
					//valorFeature.equalsIgnoreCase(value)

					//esta opcion se empleariamos si quisieramos buscar un valor en cualquier parte de la cadena
					//int val=valorFeature.indexOf(value);

					if (value.equals("") || valorFeature.equals("")){
						break;
					}else{
						if(textliteral){
							if (valorFeature.equalsIgnoreCase(value))
							{
								finalFeatures.add(localFeature);
							}
						}else{
							if (valorFeature.regionMatches(0,value,0,value.length()))
							{
								finalFeatures.add(localFeature);
							}
						}
					}
				}
			}else{
				while (atributosIter.hasNext())
				{
					localAtributo = (String) atributosIter.next();
					try
					{
						valorFeature = localFeature.getString(localAtributo).trim();
						valorFeature2 = localFeature.getString(screenAttrib).trim();

					} catch (Exception e)
					{
						logger.error("searchByAttributes(LayerManager, String, ArrayList, ArrayList)",e);
						break;
					}
					value = (String) valuesIter.next();
					// SI TODOS LOS ATRIBUTOS COINCIDEN flag es true
					valorFeature = valorFeature.replaceAll(" ","").toUpperCase();
					value = value.replaceAll(" ","").toUpperCase();

					//esta opcion nos dice si las cadenas son coincidentes desde el principiosin necesidad de escribir el valor entero
					//boolean valor3=valorFeature.regionMatches(0,value,0,value.length());

					//esta opcion em principio esta descartada ya que solo se utilizacia para cadenas que coincidieran exactamente
					//valorFeature.equalsIgnoreCase(value)

					//esta opcion se empleariamos si quisieramos buscar un valor en cualquier parte de la cadena
					//int val=valorFeature.indexOf(value);

					if (value.equals("") || valorFeature.equals("") || valorFeature2.equals("") ){
						break;
					}else{
						if(textliteral){
							if ((valorFeature.equalsIgnoreCase(value)) && (valorFeature2.equalsIgnoreCase(valscreen)))
							{
								finalFeatures.add(localFeature);
							}
						}else{
							if ((valorFeature.regionMatches(0,value,0,value.length()) && (valorFeature2.equalsIgnoreCase(valscreen))))
							{
								finalFeatures.add(localFeature);
							}
						}
					}
				}
			}
		}
		return finalFeatures;
	}
	/**
	 * searchByAttributes(LayerManager layerManager, String layerName, ArrayList
	 * attributeNames, ArrayList values) Método que busca las features que
	 * cumplen que varios atributos son iguales a unos determinado valores
	 * dentro de una capa
	 * 
	 * @param layerManager :
	 *            Manejador de Capas
	 * @param layerName :
	 *            Nombre de la capa en que se realiza la búsqueda
	 * @param values :
	 *            Lista de Valores de los atributos por los que se buscará en la
	 *            capa
	 * @param Attrib :
	 *            atributo para buscar dentro de la capa
	 * @return Devuelve un objeto de tipo Collection que contiene las features
	 *         que cumplen el criterio de búsqueda
	 */

	public static Collection searchByAttributes(LayerManager layerManager, String layerName,List values,String Attrib)
	{
		Collection finalFeatures = new ArrayList();
		Layer localLayer = layerManager.getLayer(layerName);
		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();
			Iterator valuesIter = values.iterator();
			String valorFeature = "";
			String value = "";
			while (valuesIter.hasNext())
			{
				value = (String) valuesIter.next();
				try
				{
					valorFeature = localFeature.getString(Attrib).trim();

				} catch (Exception e)
				{
					logger.error("searchByAttributes(LayerManager, String, ArrayList, ArrayList)",e);
					break;
				}
				valorFeature = valorFeature.replaceAll(" ","").toUpperCase();
				value = value.replaceAll(" ","").toUpperCase();

				if (valorFeature.equalsIgnoreCase(value))
				{
					finalFeatures.add(localFeature);
				}

			}
		}
		return finalFeatures;
	}
	/**
     * Busca las features que contengan la referencia catastral indicada
     * 
     * @return features que contienen la referencia catastral pedida en el attributo attrib
     * @param attrib 
     * @param layer capa en la que se hace la búsqueda
     * @param referencia_catastral referencia catastral a buscar
     */
	public static Collection searchReferenciaCatastral(Layer layer,String referencia_catastral,String attrib)
	{
		Collection finalFeatures = new ArrayList();
		List allFeaturesList = layer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String valorFeature = "";

			valorFeature = localFeature.getString(attrib).trim();

			if (valorFeature.indexOf(referencia_catastral.trim())!=-1)
			{
				finalFeatures.add(localFeature);
			}
		}
		return finalFeatures;
	}
    
	/**
     * searchByAttributes(LayerManager layerManager, String layerName, ArrayList
     * attributeNames, ArrayList values, String logicOperator) Método que busca
     * las features que cumplen que varios atributos son iguales a unos
     * determinado valores dentro de una capa
     * 
     * 
     * @return Devuelve un objeto de tipo Collection que contiene las features
     *         que cumplen el criterio de búsqueda
     * @param attributeNames 
     * @param layerManager :
     *            Manejador de Capas
     * @param layerName :
     *            Nombre de la capa en que se realiza la búsqueda
     * @param values :
     *            Lista de Valores de los atributos por los que se buscará en la
     *            capa
     * @param logicOperator :
     *            AND indica que deben cumplirse todos los criterios, OR indica
     *            que puede cumplirse cualquiera
     */
	public static Collection searchByAttributes(LayerManager layerManager, String layerName, ArrayList attributeNames, ArrayList values, String logicOperator)
	{
		Collection finalFeatures = new ArrayList();

		Layer localLayer = layerManager.getLayer(layerName);
		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			Iterator atributosIter = attributeNames.iterator();

			Iterator valuesIter = values.iterator();
			boolean flag = true;
			int i = 0;
			String localAtributo = "";
			String valorFeature = "";
			String value = "";

			if (logicOperator.equals("AND"))
			{
				while (atributosIter.hasNext())
				{
					localAtributo = (String) atributosIter.next();
					try
					{
						valorFeature = localFeature.getString(localAtributo).trim().toLowerCase();
					} catch (Exception e)
					{
						logger
						.error(
								"searchByAttributes(LayerManager, String, ArrayList, ArrayList, String)",
								e);
						break;
					}
					// SI TODOS LOS ATRIBUTOS COINCIDEN flag es true
					value = (String) valuesIter.next().toString().toLowerCase();
					Pattern pattern = Pattern.compile(value);
					Matcher matcher = pattern.matcher(valorFeature);
					boolean matchFound = matcher.find();
					if ((matchFound) && (flag == true))

					{
						flag = true;
						// System.out.println("i :"+i+ " Flag :" +flag+ "
								// Atributo: "+localAtributo+ " Valor: "+ value);
					} else
					{
						// System.out.println("i :"+i+ " Flag :" +flag+ "
						// Atributo: "+localAtributo+ " Valor: "+ value);
						flag = false;
					}
					i++;

				}

				if (flag == true)
				{
					finalFeatures.add(localFeature);

				}
			} else
			{
				while (atributosIter.hasNext())
				{
					localAtributo = (String) atributosIter.next();
					try
					{
						valorFeature = localFeature.getString(localAtributo).trim().toLowerCase();
					} catch (Exception e)
					{
						logger
						.error(
								"searchByAttributes(LayerManager, String, ArrayList, ArrayList, String)",
								e);
						break;
					}

					// SI TODOS LOS ATRIBUTOS COINCIDEN flag es true
					value = (String) valuesIter.next().toString().toLowerCase();
					Pattern pattern = Pattern.compile(value);
					Matcher matcher = pattern.matcher(valorFeature);
					boolean matchFound = matcher.find();
					if (matchFound)
					{
						Iterator f = finalFeatures.iterator();
						if (f.hasNext())
						{
							while (f.hasNext())
							{
								Feature tempFeature = (Feature) f.next();
								if (!tempFeature.getAttribute("ID_Inmueble").equals(localFeature.getAttribute("ID_Inmueble")))
								{
									finalFeatures.add(localFeature);
									break;
								}
							}
						} else
						{
							finalFeatures.add(localFeature);
						}

					}

				}

			}
		}

		return finalFeatures;
	}

	// MODIFICACION DE LA FUNCION ZOOOMTOFEATURE
    /**
     * 
     * @param context 
     * @param feature 
     * @throws java.awt.geom.NoninvertibleTransformException 
     */
	public static void zoomTo(PlugInContext context, Feature feature) throws NoninvertibleTransformException
	{

		// RECUPERAMOS EL ENVELOPE DE LA FEATURE

		Envelope envelopeFeature = feature.getGeometry().getEnvelopeInternal();

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
		context.getLayerViewPanel().getViewport().zoom(envelopeFeature);

	}

    /**
     * 
     * @param layer 
     * @param categoria 
     * @param layerManager 
     * @param task 
     * @throws java.lang.Exception 
     * @return 
     */
	public static Layer loadData(String layer, String categoria, LayerManager layerManager, Task task) throws Exception
	{
		ArrayList layers = new ArrayList();
		ArrayList categoriaArray = new ArrayList();
		layers.add(layer);
		categoriaArray.add(categoria);
		ArrayList nuevaCapa = loadData(layers, categoriaArray, layerManager, task);
		return (Layer) nuevaCapa.get(0);
	}

    /**
     * 
     * @param layers 
     * @param categorias 
     * @param layerManager 
     * @param task 
     * @throws java.lang.Exception 
     * @return 
     */
	public static ArrayList loadData(ArrayList layers, ArrayList categorias, LayerManager layerManager, Task task) throws Exception
	{
		removeAllCategories(layerManager);

		Iterator layersIter = layers.iterator();
		Iterator categoriasIter = categorias.iterator();
		ArrayList nuevasCapas = new ArrayList();
		while (layersIter.hasNext())
		{

			File fileLayer = new File((String) layersIter.next());
			String categoria = (String) categoriasIter.next();
			String extension = getExtension(fileLayer);
			String driver = aplicacion.getString(extension);

			DataSourceQuery dataSourceQuery = toDataSourceQuery(fileLayer, Class.forName(driver));

			Connection connection = dataSourceQuery.getDataSource().getConnection();
			FeatureCollection dataset = connection.executeQuery(dataSourceQuery.getQuery(), null);

			Layer nueva = layerManager.addLayer(categoria, dataSourceQuery.toString(), dataset);
			nueva.setDataSourceQuery(dataSourceQuery);
			nueva.setFeatureCollectionModified(false);
			task.getLayerManager().addCategory(categoria);
			Category nuevaCategoria = task.getLayerManager().getCategory(categoria);
			nuevaCategoria.addPersistentLayerable((Layer) nueva);
			List t = nuevaCategoria.getPersistentLayerables();

			nuevasCapas.add(nueva);

		}
		return nuevasCapas;
	}

    /**
     * 
     * @param f 
     * @return 
     */
	public static String getExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
		{
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

    /**
     * 
     * @param file 
     * @param dataSourceClass 
     * @return 
     */
	protected static DataSourceQuery toDataSourceQuery(File file, Class dataSourceClass)
	{

		DataSource dataSource = (DataSource) LangUtil.newInstance(dataSourceClass);
		dataSource.setProperties(toProperties(file));
		return new DataSourceQuery(dataSource, null, GUIUtil.nameWithoutExtension(file));
	}

    /**
     * 
     * @param file 
     * @return 
     */
	protected static Map toProperties(File file)
	{
		HashMap properties = new HashMap();
		properties.put(DataSource.FILE_KEY, file.getPath());
		properties.put(DataSource.COORDINATE_SYSTEM_KEY, CoordinateSystem.UNSPECIFIED.getName());
		return properties;
	}

	private static void removeAllCategories(LayerManager layerManager)
	{
		for (Iterator i = layerManager.getCategories().iterator(); i.hasNext();)
		{
			Category cat = (Category) i.next();
			layerManager.removeIfEmpty(cat);
		}
	}


	private static FeatureCollection executeQuery(String query, DataSource dataSource, CoordinateSystemRegistry registry, TaskMonitor monitor) throws Exception
	{
		Connection connection = dataSource.getConnection();
		try
		{
			return dataSource.installCoordinateSystem(connection.executeQuery(query, monitor), registry);
		} finally
		{
			connection.close();
		}
	}

    /**
     * 
     * @param layerViewPanel 
     * @param pf 
     * @return 
     */
	public static double pointsMeter(LayerViewPanel layerViewPanel, PageFormat pf)
	{
		/*
		 * double scale = layerViewPanel.getViewport().getScale(); Envelope
		 * envelope =
		 * layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
		 * System.out.println("envelope: "+envelope.getMinX());
		 * System.out.println("envelope: "+envelope.getMaxX());
		 * System.out.println("envelopey: "+envelope.getMinY());
		 * System.out.println("envelopey: "+envelope.getMaxY()); double
		 * puntosPorMetro =
		 * pf.getImageableWidth()/(envelope.getMaxX()-envelope.getMinX());
		 * System.out.println("puntosPorMetro: "+puntosPorMetro);
		 * 
		 * double puntosPorMetroY =
		 * pf.getImageableHeight()/(envelope.getMaxY()-envelope.getMinY());
		 * System.out.println("puntosPorMetroY: "+puntosPorMetroY);
		 * System.out.println("pf.getImageableHeight():
		 * "+pf.getImageableHeight());
		 * System.out.println("pf.getImageableWidth():
		 * "+pf.getImageableWidth());
		 * 
		 * System.out.println("caculando escala: "+scale);
		 */
		return 0;
	}

    /**
     * 
     * @param plugIn 
     * @param workbenchContext 
     * @param taskMonitorManager 
     */
	public static void executePlugIn(PlugIn plugIn, WorkbenchContext workbenchContext, TaskMonitorManager taskMonitorManager)
	{
		try
		{
			workbenchContext.getIWorkbench().getGuiComponent().setStatusMessage("");
			workbenchContext.getIWorkbench().getGuiComponent().log("Executing " + plugIn.getName());

			PlugInContext plugInContext = workbenchContext.createPlugInContext();
			// Cache the UndoableEditReceiver, because the "topmost"
			// layer manager before the edit may be different from the
			// topmost layer manager after (e.g. NewTaskPlugIn). [Jon Aquino]
			UndoableEditReceiver undoableEditReceiver = workbenchContext.getLayerManager() != null ? workbenchContext.getLayerManager().getUndoableEditReceiver() : null;
			if (undoableEditReceiver != null)
			{
				undoableEditReceiver.startReceiving();
			}

			try
			{
				boolean executeComplete = plugIn.execute(plugInContext);

				if (plugIn instanceof ThreadedPlugIn && executeComplete && (taskMonitorManager != null))
				{
					taskMonitorManager.execute((ThreadedPlugIn) plugIn, plugInContext);
				}
			} finally
			{
				if (undoableEditReceiver != null)
				{
					undoableEditReceiver.stopReceiving();
				}
			}

			workbenchContext.getIWorkbench().getGuiComponent().log("Done. Current committed memory: " + workbenchContext.getIWorkbench().getGuiComponent().getMBCommittedMemory() + " MB");
		} catch (Throwable t)
		{
			logger
			.error(
					"executePlugIn(PlugIn, WorkbenchContext, TaskMonitorManager)",
					t);
			workbenchContext.getErrorHandler().handleThrowable(t);
		}
	}

	
	

    /**
     * 
     * @param name 
     * @param list 
     * @param initialValue 
     * @param toolTipText 
     * @param layers 
     * @return 
     */
	public static JList initializeLayerList(String name, JList list, Layer initialValue, String toolTipText, Collection layers)
	{
		DefaultListModel mod = new DefaultListModel();
		list.setModel(mod);
		for (Iterator iter = layers.iterator(); iter.hasNext();)
			mod.addElement(iter.next());
		list.setSelectedValue(initialValue, true);
		list.setCellRenderer(new LayerNameRenderer());
		if (name != null)
			list.setName(name);
		list.setToolTipText(toolTipText);
		return list;
	}
	
    /**
     * Muestra únicamente las capas que no son locales
     * @param name 
     * @param list 
     * @param initialValue 
     * @param toolTipText 
     * @param layers 
     * @return 
     */
	public static JList initializeNoLocalLayerList(String name, JList list, Layer initialValue, String toolTipText, Collection layers)
	{
		DefaultListModel mod = new DefaultListModel();
		list.setModel(mod);
		Layer layer = null; 
		for (Iterator iter = layers.iterator(); iter.hasNext();){
			layer = (Layer) iter.next();
			if (layer == null){continue;}
			if(!layer.isVisible()){continue;} 
			if (layer instanceof GeopistaLayer){
				if(((IGeopistaLayer) layer).isLocal()==false){
					mod.addElement(layer);
				}
			}
		}
		list.setSelectedValue(initialValue, true);
		list.setCellRenderer(new LayerNameRenderer());
		if (name != null){
			list.setName(name);
		}
		list.setToolTipText(toolTipText);
		return list;
	}
	
    /**
     * Muestra únicamente las capas WMS
     * @param name 
     * @param list 
     * @param initialValue 
     * @param toolTipText 
     * @param layers 
     * @return 
     */
	public static JList initializeWMSLayerList(String name, JList list, Layer initialValue, String toolTipText, Collection layers)
	{
		DefaultListModel mod = new DefaultListModel();
		list.setModel(mod);
		Layerable layer = null; 
		for (Iterator iter = layers.iterator(); iter.hasNext();){
			layer = (Layerable) iter.next();
			if (layer == null){continue;}
			if (!layer.isVisible()){continue;}
			if (layer instanceof WMSLayer){
				mod.addElement(layer);
			}
		}
		list.setSelectedValue(initialValue, true);
		list.setCellRenderer(new LayerNameRenderer());
		if (name != null){
			list.setName(name);
		}
		list.setToolTipText(toolTipText);
		return list;
	}

	/**
     * Devuelve una coleccion con las features que dan un resultado !=0 en la
     * evaluación de la expresión configurada en el analizador
     * 
     * 
     * @return 
     * @see FeatureExpresionParser
     * @param localLayer 
     * @param expParser 
     */
	public static Collection searchByExpression(Layer localLayer, FeatureExpresionParser expParser)
	{
		Collection finalFeatures = new ArrayList();

		List allFeaturesList = localLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			expParser.setFeature(localFeature);
			double resultado = expParser.getValue();
			if (resultado != 0)
				finalFeatures.add(localFeature);

		}
		return finalFeatures;

	}




	/**
     * Extract from URL.
     * Carga un JAR desde el URL e intenta extraer los recursos que coincidan con la expresion.
     * 
     * @param urljar 
     * @param patternString 
     * @param dest 
     * @param tm 
     * @throws MalformedURLException 
     * @throws IOException 
     */
	public static void extractResources(String patternString, File dest, URL urljar,TaskMonitor tm) throws MalformedURLException, IOException
	{
		
		//Crea el directorio textures si no existe
		boolean fileExists = dest.exists();

		if(fileExists && !dest.isDirectory()) throw new IllegalArgumentException("El destino es un fichero");
		if (!fileExists) {
			boolean isCreated = dest.mkdirs();

		}

		Pattern pattern = Pattern.compile(patternString);
		System.out.println("ExtractResources() - Listing Resources from: "+urljar+"->"+patternString);
		List resources = new ArrayList();
		if (urljar.getFile().endsWith(".jar")) {

			listJarResources(new URL("jar:" + urljar.toExternalForm() + "!/"),
					resources, pattern,tm);
		}

		int cont=0;
		for (Iterator iter = resources.iterator(); iter.hasNext();)
		{

			URL url = (URL) iter.next();
			String urlString = url.toExternalForm();
			System.out.println("URL Acceso:"+urlString);

			if (pattern.matcher(urlString).matches())
			{
				if (urlString.indexOf("informes") != -1){
					System.out.println("extractResources(String, File, Class) - Found resource: "
							+ urlString);

					// Si el jar contiene archivos de informes, el nombre de estos 
					// se formara incluyendo la ruta del recurso dentro del jar
					String resourcePath = urlString.substring(urlString.indexOf('!') + 1);
					resourcePath = resourcePath.replace('/', File.separator.charAt(0));

					File destFile = new File(dest,resourcePath);

					if (tm!=null)
						tm.report(cont++, resources.size(),"CopiandoFicheros.");    				

					// Comprobamos si la url correponde a recursos de informes
					// en ese caso se copian los ficheros creando los directorios
					// de destino
					GeopistaFunctionUtils.copyFileCreatingDirectories(destFile,url);
				}
				else {
					System.out.println("extractResources(String, File, Class) - Found resource: "
							+ urlString);
					File origFile = new File(url.getFile());
					if (origFile.getName().equals("textures"))
						continue;
					File destFile = new File(dest,origFile.getName());

					if (tm!=null)
						tm.report(cont++, resources.size(),"CopiandoFicheros.");

					GeopistaFunctionUtils.copyFile(destFile,url);
				}
			}

		}
	}

	/**
	 * Intenta extraer los recursos que coincidan con el patrón indicado del
	 * JAR que contenga la clase rootClass
	 * No funciona con JNLPClassLoader
	 * 
	 * @param patternString
	 * @param dest
	 * @param rootClass
	 * @param tm
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void extractResources(String patternString, File dest, Class rootClass, TaskMonitor tm) throws MalformedURLException, IOException
	{
		Pattern pattern = Pattern.compile(patternString);
		System.out.println("ExtractResources() - Listing Resources from: "+rootClass.getClassLoader()+"->"+patternString);
		List resources = listResources(rootClass.getClassLoader(),pattern,  tm);
		int cont=0;
		for (Iterator iter = resources.iterator(); iter.hasNext();)
		{

			URL url = (URL) iter.next();
			String urlString = url.toExternalForm();

			if (pattern.matcher(urlString).matches()) {

				if (urlString.indexOf("informes") != -1){
					System.out.println("extractResources(String, File, Class) - Found resource: "
							+ urlString);

					// Si el jar contiene archivos de informes, el nombre de estos 
					// se formara incluyendo la ruta del recurso dentro del jar
					String resourcePath = urlString.substring(urlString.indexOf('!') + 1);
					resourcePath = resourcePath.replace('/', File.separator.charAt(0));

					File destFile = new File(dest,resourcePath);

					if (tm!=null)
						tm.report(cont++, resources.size(),"CopiandoFicheros.");    				

					// Comprobamos si la url correponde a recursos de informes
					// en ese caso se copian los ficheros creando los directorios
					// de destino
					GeopistaFunctionUtils.copyFileCreatingDirectories(destFile,url);
				}
				else {
					System.out.println("extractResources(String, File, Class) - Found resource: "
							+ urlString);
					File origFile = new File(url.getFile());

					if (origFile.getName().equals("textures"))
						continue;

					File destFile = new File(dest,origFile.getName());
					tm.report(cont++, resources.size(),"CopiandoFicheros.");
					GeopistaFunctionUtils.copyFile(destFile,url);
				}			
			}
		}
	}

	private static List listResources(ClassLoader cl,Pattern pattern, TaskMonitor tm) throws IOException, MalformedURLException {
		List resources = new ArrayList();
		while (cl != null) {
			if (cl instanceof URLClassLoader) {
				URLClassLoader ucl = (URLClassLoader) cl;
				URL[] urls = ucl.getURLs();
				for (int i = 0; i < urls.length; i++) {
					URL url = urls[i];
					System.out.println("Listing: "+url);
					if (url.getFile().endsWith(".jar")) {

						listJarResources(new URL("jar:" + url.toExternalForm() + "!/"),
								resources, pattern,tm);
					}
					else if (url.getProtocol().equals("file")) {
						File file = new File(url.getFile());
						if (file.isDirectory()) {

							listDirResources(file, resources,pattern,tm);
						}
					}
				}
			}
			cl = cl.getParent();
		}
		return resources;
	}
	private static void listDirResources(File dir, List resources, Pattern pattern, TaskMonitor tm)
	throws MalformedURLException {
		if (tm!=null)
			tm.report("Buscando en el directorio:"+dir.getPath());
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String urlString = file.toURL().toExternalForm();
			if (pattern.matcher(urlString).matches())
				resources.add(file.toURL());
			if (file.isDirectory()) {
				listDirResources(file, resources, pattern,tm);
			}
		}
	}

	private static void listJarResources(URL jarUrl, List resources, Pattern pattern, TaskMonitor tm) 
	throws IOException, MalformedURLException {
		if (tm!=null)
			tm.report("Buscando en el archivo:"+jarUrl.getFile());
		JarURLConnection jarConnection = 
			(JarURLConnection) jarUrl.openConnection();

		for (Enumeration entries = jarConnection.getJarFile().entries();
		entries.hasMoreElements(); ) {
			JarEntry entry = (JarEntry) entries.nextElement();
			URL url= new URL(jarUrl, entry.getName());
			String urlString = url.toExternalForm();
			if (pattern.matcher(urlString).matches())
				resources.add(url);
		}
	}

    /**
     * 
     * @param destination 
     * @param source 
     */
	public static void copyFile(java.io.File destination, java.io.File source)  {

		try
		{
			java.io.FileInputStream inStream=new java.io.FileInputStream(source);
			java.io.FileOutputStream outStream=new java.io.FileOutputStream(destination);

			int len;
			byte[] buf=new byte[2048];

			while ((len=inStream.read(buf))!=-1) {
				outStream.write(buf,0,len);
			}
			outStream.close();
			inStream.close();
		}
		catch (FileNotFoundException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}
		catch (IOException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}



	}
    /**
     * 
     * @param destination 
     * @param source 
     */
	static void copyFile(java.io.File destination, URL source)  {

		try
		{
			InputStream inStream=source.openStream();
			java.io.FileOutputStream outStream=new java.io.FileOutputStream(destination);

			int len;
			byte[] buf=new byte[2048];

			while ((len=inStream.read(buf))!=-1) {
				outStream.write(buf,0,len);
			}
		}
		catch (FileNotFoundException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}
		catch (IOException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}
	}

    /**
     * 
     * @param destination 
     * @param source 
     */
	static void copyFileCreatingDirectories(java.io.File destination, URL source)  {

		try
		{
			if (source.getFile().lastIndexOf('/') == (source.getFile().length() - 1)){
				// Es un directorio y por tanto lo creamos si no existe
				if (!destination.exists()){
					destination.mkdirs();
				}
			}
			else {
				int index = destination.getAbsolutePath().lastIndexOf(File.separator);
				if (index != -1){
					// Creo los directorios del path si no existen
					String baseDir = destination.getAbsolutePath().substring(0, index);
					File baseDirFile = new File(baseDir);
					if (!baseDirFile.exists()){
						baseDirFile.mkdirs();
					}
				}
				InputStream inStream=source.openStream();
				java.io.FileOutputStream outStream=new java.io.FileOutputStream(destination);

				int len;
				byte[] buf=new byte[2048];

				while ((len=inStream.read(buf))!=-1) {
					outStream.write(buf,0,len);
				}
			}
		}
		catch (FileNotFoundException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}
		catch (IOException e)
		{

			logger.error("copyFile(java.io.File, java.io.File)", e);
		}
	}

    
    /**
     * 
     * @param baseDir 
     * @param mapName 
     * @throws java.io.IOException 
     */
	public static void makeMapBackup(String baseDir, String mapName) throws IOException
	{
		File baseDirFile = new File(baseDir, "map.backups");
		File targetFile = new File(baseDirFile, mapName);
		File sourceDir = new File(baseDir, mapName);

		if (!baseDirFile.exists())
		{

			boolean createBackupDirectory = baseDirFile.mkdirs();
			if (!createBackupDirectory)
				throw new IOException(aplicacion
						.getI18nString("ImposibleCrearDirectorioParaCopiaDeSeguridad"));
		}

		if (targetFile.exists())
		{
			deleteDirectory(targetFile);
		}
		if (sourceDir.exists())
		{
			boolean renameResult = sourceDir.renameTo(targetFile);
			if (!renameResult)
				throw new IOException(aplicacion
						.getI18nString("ErrorAlHacerLaCopiaDeSeguridad"));

		}

	}

    /**
     * 
     * @param directory 
     * @throws java.io.IOException 
     * @return 
     */
	public static boolean deleteDirectory(File directory) throws IOException
	{
		File[] deleteFiles = directory.listFiles();
		if (deleteFiles != null)
		{

			for (int n = 0; n < deleteFiles.length; n++)
			{

				File currentDeletingFile = deleteFiles[n];
				if (currentDeletingFile.isDirectory())
				{
					deleteDirectory(currentDeletingFile);
				}

				boolean deleteCurrenFile = currentDeletingFile.delete();
				if (!deleteCurrenFile)
					throw new IOException(aplicacion
							.getI18nString("ErrorAlBorrarDirectorioParaCopiaSeguridad"));

			}
		}
		boolean deleteCurrenDirectory = directory.delete();
		if (!deleteCurrenDirectory)
			throw new IOException(aplicacion
					.getI18nString("ErrorAlBorrarDirectorioParaCopiaSeguridad"));

		return true;
	}

    /**
     * 
     * @param baseDir 
     * @param mapName 
     * @throws java.io.IOException 
     */
	public static void restoreBackup(String baseDir, String mapName) throws IOException
	{
		File baseDirFile = new File(baseDir, "map.backups");
		File targetFile = new File(baseDirFile, mapName);
		File baseDirectory = new File(baseDir);
		File restoreDirectory = new File(baseDir, mapName);

		if (!baseDirectory.exists())
		{
			boolean createRestoreDirectory = restoreDirectory.mkdirs();
			if (!createRestoreDirectory)
				throw new IOException(aplicacion
						.getI18nString("ImposibleCrearDirectorioBase"));

		} else
		{
			if (restoreDirectory.exists())
			{
				GeopistaFunctionUtils.deleteDirectory(restoreDirectory);
			}
		}

		if (targetFile.exists())
		{
			boolean renameResult = targetFile.renameTo(restoreDirectory);
			if (!renameResult)
				throw new IOException(aplicacion
						.getI18nString("ErrorAlRestaurarLaCopiaDeSeguridad"));

		} else
		{
			throw new IOException(aplicacion.getI18nString("MapaBackupNoExiste"));
		}
	}

    /**
     * 
     * @param ps 
     */
	public static void generarSQL (PreparedStatement ps)
	{
		boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();

		if (genSQL)
		{   
			try
			{              
				String rutaFich = aplicacion.getString("ruta.base.mapas");
				File fichLog = new File (rutaFich, "BDqueries.sql");
				OutputStream out = new FileOutputStream(fichLog, true);
				String stQuery = new String(); 

				if (ps instanceof GEOPISTAPreparedStatement)
				{

					PreparedStatement s = null;
					ResultSet r = null;
					java.sql.Connection conn=  aplicacion.getConnection();
					conn.setAutoCommit(false);            

					s = conn.prepareStatement("getQueryById");
					s.setString(1, ((GEOPISTAPreparedStatement)ps).getQueryId());                
					r = s.executeQuery();

					if (r.next())
					{
						stQuery= r.getString(1);
						Object[] oData=(Object[])(new ObjectInputStream(new ByteArrayInputStream(((GEOPISTAPreparedStatement)ps).getParams())).readObject());


						for (int i=0;i<oData.length;i++)
						{
							String cadSustitucion = new String();

							if (oData[i]!=null)
							{
								cadSustitucion = oData[i].toString();
								cadSustitucion = cadSustitucion.replaceAll("\\?", "\\!");

								if (oData[i] instanceof String)
									cadSustitucion = "'"+cadSustitucion+"'";
							}
							else
							{
								cadSustitucion="null";
							}
							stQuery = stQuery.replaceFirst("\\?", cadSustitucion);

						}  
						stQuery= stQuery.replaceAll("\\!", "\\?") +";\n";
						out.write(stQuery.getBytes());                      

					}
				}
				else if (ps instanceof StandardXAPreparedStatement)
				{   
					java.sql.Connection conex=CPoolDatabase.getConnection();
					if (((org.enhydra.jdbc.core.CoreConnection)conex).con instanceof org.postgresql.PGConnection)
					{
						stQuery = ((((StandardXAPreparedStatement)ps).ps)).toString()+ ";\n";;

						out.write(stQuery.getBytes());                     

					}
					conex.close();
					CPoolDatabase.releaseConexion();               
				}
				out.close();
			}
			catch (SQLException ex)
			{
				ex.printStackTrace();
				//ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));                  
				return;
			}

			catch (Exception ex)
			{            
				ex.printStackTrace();
				return;
			}            
		}
	}

    
	
	
		/**
     * 
     * @param s 
     */
	  public static void generarSQL (String stQuery)
	  {
	      boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();
	      
	      if (genSQL)
	      {  
	          try
	          {
	              String rutaFich = aplicacion.getString("ruta.base.mapas");
	              File fichLog = new File (rutaFich, "BDqueries.sql");
	              OutputStream out =  new FileOutputStream(fichLog, true);
	              stQuery = stQuery.replaceAll(";", ";\n");
	                  
	              out.write(stQuery.getBytes());
	              out.close();
	          } catch (FileNotFoundException e)
	          {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          }
	          catch (IOException e)
	          {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	          }
	          
	          catch (Exception ex)
	          {            
	              ex.printStackTrace();
	              return;

	          }
	          
	      }
	  }
	  
	  /**
     * 
     * @param s 
     */
	public static void generarSQL (Statement s)
	{
		boolean genSQL = new Boolean(aplicacion.getString("geopista.generarsql")).booleanValue();

		if (genSQL)
		{  
			try
			{
				String rutaFich = aplicacion.getString("ruta.base.mapas");
				File fichLog = new File (rutaFich, "BDqueries.sql");
				OutputStream out =  new FileOutputStream(fichLog, true);

				String stQuery = new String(); 
//
//				if (s instanceof Jdbc2Statement)
//				{   
					stQuery = s.toString();
					stQuery = stQuery.replaceAll(";", ";\n");

					out.write(stQuery.getBytes());

//				}
				out.close();

			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}

	}  
	
	

    /**
     * 
     * @param cadena 
     * @param longitud 
     * @return 
     */
	public static String completarConCeros(String cadena, int longitud){

		while (cadena.length()< longitud){
			cadena = '0' + cadena;
		}
		return cadena;
	}

	/*
	 * Esta clase devuelve un ArrayList con la ruta de los ficheros de 
	 * extension .dxf a partir de una rutaBase dada
	 */
    /**
     * 
     * @param rutaBase 
     * @param progressDialog 
     * @return 
     */
	public static ArrayList searchDirectoryAutoCAD(String rutaBase, TaskMonitorDialog progressDialog)
	{
		// rutaBase SERÁ LA RUTA A PARTIR DE LA CUAL SE BUSCARAN
		// LOS FICHEROS

		File dir = new File(rutaBase);


		// FILTRO PARA DIRECTORIOS
		FileFilter fileFilter = new FileFilter()
		{
			public boolean accept(File file)
			{
				return file.isDirectory();
			}
		};
		// LISTAR DIRECTORIOS
		File[] files = dir.listFiles(fileFilter);

		// CREAMOS UN ARRAYLIST CON LOS OBJETOS GEOPISTAMAPLIST
		ArrayList listaMap = new ArrayList();
		for (int i = 0; i < files.length; i++)
		{
			GeopistaMap mapaDeLista = GeopistaFunctionUtils.searchFileAutoCAD(files[i].getPath(), "dxf", "thumb.png");
			if (progressDialog!=null && progressDialog.isCancelRequested())
				return listaMap;
			if (mapaDeLista != null)
			{
				if (progressDialog!=null)

					listaMap.add(mapaDeLista);
				progressDialog.report(listaMap.size(),-1,mapaDeLista.getName());
			}
		}

		/*
		 * Iterator Iter = listaMap.iterator(); while (Iter.hasNext()) {
		 * GeopistaMapList mapa = (GeopistaMapList)Iter.next(); }
		 */

		return listaMap;

	}

    /**
     * 
     * @param rutaBase 
     * @param extension 
     * @param thumb 
     * @return 
     */
	public static GeopistaMap searchFileAutoCAD(String rutaBase, String extension, final String thumb)
	{


		final String ext = "." + extension;
		GeopistaMap mapa = null;
		File dir = new File(rutaBase);

		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return name.endsWith(ext);
			}
		};

		File[] files = dir.listFiles(filter);

		for (int i = 0; i < files.length; i++)
		{          
			FilenameFilter filterThumb = new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.equalsIgnoreCase(thumb);
				}
			};
			File[] thumbs = dir.listFiles(filterThumb);

			mapa = null;
			try
			{
				mapa = GeopistaMap.getResumeMap(files[i].getPath());
				mapa.loadThumbnail();
			} catch (Exception e)
			{

				continue;
			}

		}
		return mapa;

	}

    /**
     * 
     * @param context 
     * @return 
     */
	public static String chooseCategory(PlugInContext context) {

		if ((context.getLayerNamePanel()!=null)&&
				(context.getLayerNamePanel().getSelectedCategories()!=null)&&
				!(context.getLayerNamePanel().getSelectedCategories().isEmpty())){

			return context.getLayerNamePanel().getSelectedCategories().iterator().next().toString();
		}
		else {
			return StandardCategoryNames.WORKING;
		}
	}


    /**
     * 
     * @param readerWriterDataSourceClass 
     * @return 
     */
	public static String[] extensions(Class readerWriterDataSourceClass) {
		try {
			return ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass
					.newInstance()).getExtensions();
		} catch (Exception e) {
			Assert.shouldNeverReachHere(e.toString());
			return null;
		}
	}

	/**
	 * Sube a un servidor por HTTP los ficheros comprimidos
	 * @param url
	 * @param files
	 * @throws Exception 
	 */
	public static void httpUploadFiles(String url, List<File> files, List<String> paramsNames, List<String> paramsValues) throws Exception {
		if(paramsNames!=null && paramsValues !=null){
			if(paramsNames.size()!=paramsValues.size()){
				logger.error("Debe haber el mismo número de nombres parámetros y sus valores en la request");
				return;
			}
		}
		try {
			HttpClient client = new HttpClient();
		    MultipartPostMethod mPost = new MultipartPostMethod(url);
		    //client.setConnectionTimeout(8000);
		    client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
		    //recolección de archivos
		    File f = null;
		    for(int i=0; i<files.size(); i++){
		    	f = files.get(i);
		    	logger.debug("File Length = " + f.length());
		    	mPost.addParameter(f.getName(), f);
		    }
		    //añadimos los headers
		    if(paramsNames!=null && paramsValues !=null){
			    for (int i = 0; i < paramsNames.size(); i++) {
			    	mPost.addRequestHeader(paramsNames.get(i), paramsValues.get(i));
				}
		    }
		    //envío por HTTP
		    int statusCode1 = client.executeMethod(mPost);
		    logger.info("statusLine>>> " + mPost.getStatusLine() + " || statusCode>>> "+statusCode1);
		    mPost.releaseConnection();
		} catch (Exception e) {
			logger.error("No se ha podido enviar los ficheros por HTTP. " + e, e);
			throw e;
		}
	}

	/**
	 * Inicializa un comboBox para que muestre una lista de capas del workbench
	 * 
	 * @param name
	 * @param comboBox
	 *            componente a configurar
	 * @param initialValue
	 *            capa seleccionada
	 * @param toolTipText
	 * @param layers
	 *            capas que se desea que aparezcan
	 * @return
	 */
	public static JComboBox initializeLayerComboBox(String name, JComboBox comboBox, Layer initialValue, String toolTipText, Collection layers)
	{

		Vector vL = new Vector (layers);
		comboBox.setModel(new DefaultComboBoxModel(vL));
		comboBox.setSelectedItem(initialValue);
		GeopistaLayerNameRenderer rdr = new GeopistaLayerNameRenderer();
		rdr.setCheckBoxVisible(false);

		comboBox.setRenderer(rdr);
		if (name != null)
			comboBox.setName(name);
		comboBox.setToolTipText(toolTipText);
		return comboBox;
	}
	public static void initializeCollatorSortedLayerComboBox(
			String name, JComboBox comboBox, Layer initialValue,
			String toolTipText, Collection layers) {		
		
		Comparator comparator = new Comparator(){
            public int compare(Object o1, Object o2) {
                Layer l1 = (Layer) o1;
                Layer l2 = (Layer) o2;
                String desc1="";
                String desc2="";
                if (l1!=null && l1.getName()!=null)
                	desc1 = l1.getName();
                if (l2!=null && l2.getName()!=null)
                	desc2 = l2.getName();                
                
                Collator myCollator=Collator.getInstance(new Locale("es_ES"));
                myCollator.setStrength(Collator.PRIMARY);
                return myCollator.compare(desc1, desc2);
                
                //return desc1.compareToIgnoreCase(desc2);
            }
        };
        ArrayList lst = new ArrayList(layers);
        Collections.sort(lst, comparator);
        
        initializeLayerComboBox(name, comboBox, initialValue, toolTipText, new Vector(lst));		
		
	}	


}