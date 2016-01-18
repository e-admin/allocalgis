/**
 * ImportarUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.plugin.io.dxf.DxfPlugIn.Dxf;
import com.geopista.ui.plugin.io.dxf.GeopistaLoadDxfQueryChooser;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrameImpl;


public class ImportarUtils
{
    public static final String HTML_ROJO="<p><font face=SansSerif size=3 color=#ff0000><b>";
    public static final String HTML_VERDE="<p><font face=SansSerif size=3 color=#009900><b>";
    public static final String HTML_NUEVO_PARRAFO="<p><font face=SansSerif size=3>";
    public static final String HTML_FIN_PARRAFO="</b></font></p>";
    public static final String HTML_SALTO="<BR>";
    public static final boolean BORDERS_OFF = false;
    public static final boolean BORDERS_ON = true; 
    
    public final static String LAST_IMPORT_DIRECTORY = "lastImportDirectory";
    public final static String LAST_IMPORT_DIRECTORY_IMAGENES = "lastImportDirectoryImagenes";
    public final static String FILE_TO_IMPORT ="fileToImport";
    public final static String FILE_TYPE ="fileType";
    public static final String FILE_TXT_MULTILINE = "txtMultiline";
    public final static String LISTA_PARCELAS ="listaParcelas";
    public final static String LISTA_IMAGENES ="listaImagenes";
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    
    
    public static Boolean res =null;
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value Valor a mostrar
     * @return String con la cadena HTML
     */
    public static String getStringHtmlFormattedText (String field, String value)
    {
        return getStringHtmlFormattedText(field, value).toString();
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value Valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, String value)
    {
        StringBuffer sb = new StringBuffer();        
        sb.append("<p><b>").append(field).append(": </b>").append(value).append("</p>");
        return sb;
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value int con el valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, int value)
    {
        return getStringBufferHtmlFormattedText(field, new Integer(value).toString());
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value long con el valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, long value)
    {
        return getStringBufferHtmlFormattedText(field, new Long(value).toString());
    }
    /**
     * Recupera la fecha actual
     * @param frm Patrón de generación de la fecha
     * @return Fecha actual en formato String
     */
    public static String getDate(String frm)
    {
        return (String) new SimpleDateFormat(frm).format(new Date());
        
    }
    /**
     * Recupera la fecha actual con patrón "dd-MMM-yyyy HH:mm:ss"
     * @return Fecha actual en formato String
     */
    public static String getDate()
    {
        return getDate("dd-MMM-yyyy HH:mm:ss");
    }
    
    /**
     * Resalta un texto, creando un nuevo párrafo en negrita y verde, formateado en HTML
     * 
     * @param text Texto a resaltar
     * @return String con el texto resaltado en HTML
     */
    public static String getEnhancedInformationMessage(String text)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(HTML_SALTO).append(HTML_VERDE).append(text).append(HTML_FIN_PARRAFO);
        
        return sb.toString();
    }
    
    
    /**
     * Método para determinar si un fichero de texto tiene el tamaño esperado
     * 
     * @param String path Ruta del fichero a comprobar que sea correcto
     * @param int size Tamaño esperado de cada registro del fichero
     * @return boolean true si es correcto, false en caso contrario
     */
    
    public static boolean testFileSize(String path, int size)
    {
        boolean hasExpectedLength = true;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                if (str.length() != size)
                {
                    hasExpectedLength = false;
                    break;
                }
            }
            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return hasExpectedLength;
        }
        return hasExpectedLength;
    }
    
    /**
     * Devuelve una lista de filas erroneas dentro de un fichero de tipo texto
     * 
     * @param path Ruta donde se encuentra el fichero a comprobar
     * @param lstTypes Lista con los posibles comienzos de registro
     * @param 
     * @return ArrayList con la lista de índices de los registros erroneos
     * 
     */
    public static ArrayList getErroneousRows(String path, ArrayList lstTypes, boolean includeBorders)
    {
        ArrayList lst = new ArrayList();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            int rowIndex = 0;
            while ((str = reader.readLine()) != null)
            {
                rowIndex++;
                
                if (!lstTypes.contains(str.substring(0,2)))
                {
                    lst.add(new Integer(rowIndex));
                }
            }
            
            //Si en la lista de tipos no se han incluido los tipos de la cabecera y de la cola, se
            //eliminan de la lista de erroneos
            if (!includeBorders)
            {
                lst.remove(new Integer (rowIndex));
                lst.remove(new Integer(1));
            }
            
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return lst;
    }
    
    /**
     * Comprueba si un fichero de texto tiene cabecera válida
     * 
     * @param path Fichero a comprobar
     * @param headerType Cadena de inicio del registro de cabecera
     * @return True si el fichero tiene cabecera válida
     */
    public static boolean testHeader(String path, String headerType)
    {
        boolean hasHeader = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            
            if ((str = reader.readLine()) != null)
            {   
                if (str.startsWith(headerType))
                {
                    hasHeader = true;
                }
            }
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return hasHeader;
        
    }
    
    /**
     * Comprueba si un fichero de texto tiene cola válida
     * 
     * @param path Fichero a comprobar
     * @param tailType Cadena de inicio del registro de cola
     * @return True si el fichero tiene cola válida
     */
    public static boolean testTail(String path, String tailType)
    {
        boolean hasTail = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            
            //al recorrer todo el fichero, hasTail tomará el resultado de la comprobación
            //para el último registro
            while ((str = reader.readLine()) != null)
            {   
                if (str.startsWith(tailType))
                {
                    hasTail = true;
                }
            }
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return hasTail;    
    }    
    
   
    
   
    
   
    
    
    public static String doubleToStringWithFactor(double d, int factor)
    {
    	NumberFormat form = new DecimalFormat("##");
		
    	String s =form.format(d*factor);
       if(s.contains(".")){
       	 s=s.substring(0, s.indexOf("."));
       }
        return s;
    }
    public static String floatToStringWithFactor(float f, int factor)
    {
    	NumberFormat form = new DecimalFormat("##");
		
   	 	String s =form.format(f*factor);
       // String s = String.valueOf(f*factor);
        
        s=s.substring(0, s.indexOf("."));
        
        return s;
    }
    
    public static double strToDouble (String value)
    {
        double d = 0;
        if (value.length()!=0)
            d = Double.parseDouble(value);
        
        return d;
    }
    
    public static float strToFloat (String value)
    {
        float f = 0;
        if (value.length()!=0)
            f = Float.parseFloat(value);
        
        return f;
    }
    
    /**
     * Convierte un InputStream en String
     * @param is
     * @return
     */
    public String parseISToString(java.io.InputStream is){
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try{
            String line = null;
            while((line=din.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        return sb.toString();
    }
    
    /**
     * Convierte un String en in InputStream
     * @param xml
     * @return
     */
    public static java.io.InputStream parseStringToIS(String xml){
        if(xml==null) return null;
        xml = xml.trim();
        java.io.InputStream in = null;
        try{
            in = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
        }catch(Exception ex){
        }
        return in;
    }
    
    /**
     * Transforma de base64 a cadena de caracteres ASCII
     * @param base64 Cadena en base 64 binario
     * @return Cadena de caracteres ASCII equivalente a la cadena en base 64
     */
    public static String base64ToAscii (String base64)
    {    
        String ascii = null;
        byte[] bytes;
        try{
            bytes = new sun.misc.BASE64Decoder().decodeBuffer(base64);
            ascii = new String(bytes);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return ascii;
    }
   
    /**
     * Transforma una cadena ascii a base 64
     * @param ascii Cadena en ASCII a transformar
     * @return Cadena de caracteres en base 64 equivalente a la cadena ASCII
     */
    public static String asciiToBase64 (String ascii)
    {         
        return new sun.misc.BASE64Encoder().encode(ascii.getBytes());
    }
    
    
    
    
    
    
   
    
    

	public InputSource resolveEntity(String arg0, String arg1)
			throws SAXException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getImagenCatastroXML(ArrayList lstImagenes){
		
		String lstImagenesXML = "<limg>";
		for (Iterator itImagenes = lstImagenes.iterator();itImagenes.hasNext();){

			ImagenCatastro imagen = (ImagenCatastro)itImagenes.next();
			lstImagenesXML = lstImagenesXML + "<img>";
			
			lstImagenesXML = lstImagenesXML + "<nom>" + imagen.getNombre() + "</nom>";
			lstImagenesXML = lstImagenesXML + "<frmt>" + imagen.getExtension() + "</frmt>";
			lstImagenesXML = lstImagenesXML + "<tdo>" + imagen.getTipoDocumento() + "</tdo>";
			lstImagenesXML = lstImagenesXML + "<foto>" + imagen.getFoto() + "</foto>";
			
			lstImagenesXML = lstImagenesXML + "</img>";

		}
		lstImagenesXML = lstImagenesXML + "</limg>";
		
		return lstImagenesXML;
	}

	public static void reportExceptions(ArrayList exceptions,
			DataSourceQuery dataSourceQuery, WorkbenchContext context) {
		context.getIWorkbench().getFrame().getOutputFrame().addHeader(1,
				exceptions.size() + " problem" + StringUtil.s(exceptions.size()) +
				" loading " + dataSourceQuery.toString() + "." +
				((exceptions.size() > 10) ? " First and last five:" : ""));
		context.getIWorkbench().getFrame().getOutputFrame().addText("See View / Log for stack traces");
		context.getIWorkbench().getFrame().getOutputFrame().append("<ul>");
	
		Collection exceptionsToReport = exceptions.size() <= 10 ? exceptions
				: CollectionUtil.concatenate(Arrays.asList(
						new Collection[] {
								exceptions.subList(0, 5),
								exceptions.subList(exceptions.size() - 5,
										exceptions.size())
						}));
		for (Iterator j = exceptionsToReport.iterator(); j.hasNext();) {
			Exception exception = (Exception) j.next();
			context.getIWorkbench().getGuiComponent().log(StringUtil.stackTrace(exception));
			context.getIWorkbench().getFrame().getOutputFrame().append("<li>");
			context.getIWorkbench().getFrame().getOutputFrame().append(GUIUtil.escapeHTML(
					WorkbenchFrameImpl.toMessage(exception), true, true));
			context.getIWorkbench().getFrame().getOutputFrame().append("</li>");
		}
		context.getIWorkbench().getFrame().getOutputFrame().append("</ul>");
	}

	public static String[] extensions(Class readerWriterDataSourceClass) {
		try {
			return ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass
					.newInstance()).getExtensions();
		} catch (Exception e) {
			Assert.shouldNeverReachHere(e.toString());
			return null;
		}
	}

	public static Geometry obtenerGeometriaParcela(String dxf){
		return obtenerGeometriaParcela(dxf, null);
	}

	public static Geometry obtenerGeometriaParcela(String dxf, WorkbenchContext context){
		
		Geometry geometryParcela = null;
		
		GeopistaLoadDxfQueryChooser dxfLoad = new GeopistaLoadDxfQueryChooser(Dxf.class,
				"GEOPISTA dxf",
				extensions(Dxf.class),
				context);    			
		
		InputStream fileDXF = ImportarUtils_LCGIII.parseStringToIS(dxf);
	    	
		try
		{
			Assert.isTrue(!dxfLoad
					.getDataSourceQueries(fileDXF).isEmpty());
		}
		catch (AssertionFailedException e)
		{
			throw new AssertionFailedException(I18N.get("FileEmpty"));
	
		}
		
		fileDXF = ImportarUtils_LCGIII.parseStringToIS(dxf);
		
		boolean exceptionsEncountered = false;
		for (Iterator i = dxfLoad
				.getDataSourceQueries(fileDXF).iterator(); i.hasNext();) {
			DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
			
			ArrayList exceptions = new ArrayList();
			Assert.isTrue(dataSourceQuery.getDataSource().isReadable());
	
			Connection connection = dataSourceQuery.getDataSource()
			.getConnection();
			try {
				FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),   					
						exceptions, null), null);
				if (dataset != null) {
					    				
					String layerName = dataSourceQuery.toString();
					Geometry geometriaInicial = null;
					GeopistaFeature featureInicial = null;
					
					if(layerName.startsWith("PG-LP")){    					
						//Obtener el borde con las features de la capa
						ArrayList lstFeatures = new ArrayList();
						for(Iterator features = dataset.getFeatures().iterator();features.hasNext();){
							GeopistaFeature feature = (GeopistaFeature)features.next();
							lstFeatures.add(feature);
						}
						ArrayList coordenadas = new ArrayList();
	
						if(lstFeatures!=null && lstFeatures.size()>0){
	
							featureInicial = (GeopistaFeature)lstFeatures.iterator().next();
							lstFeatures.remove(featureInicial);
							geometriaInicial = featureInicial.getGeometry();
							for(int indice=0;indice<geometriaInicial.getCoordinates().length;indice++)
								coordenadas.add(geometriaInicial.getCoordinates()[indice]);
	
							if(geometriaInicial instanceof LineString){
	
								Point puntoFinal = ((LineString)geometriaInicial).getEndPoint();
								GeopistaFeature feature = null;
								Geometry geometria = null;
								int indice;
								
								while(lstFeatures.size()>0){
									boolean encontrado = false;
									Iterator features = lstFeatures.iterator();
									while(features.hasNext()&& !encontrado){
										    								
										feature = (GeopistaFeature)features.next();
										geometria = feature.getGeometry();
										if (geometria instanceof LineString){  
	
											if(puntoFinal.distance(((LineString)geometria).getStartPoint())==0){
	
												for(indice=1;indice<geometria.getCoordinates().length;indice++)
													coordenadas.add(geometria.getCoordinates()[indice]); 
												puntoFinal = ((LineString)geometria).getEndPoint();
												encontrado=true;
	
											}
											else if(puntoFinal.distance(((LineString)geometria).getEndPoint())==0){
												for(indice=geometria.getCoordinates().length-2;indice>=0;indice--)
													coordenadas.add(geometria.getCoordinates()[indice]); 
												
												puntoFinal = ((LineString)geometria).getStartPoint();
												encontrado=true;
											}
	
										}
									
									}
									if(encontrado){
										lstFeatures.remove(feature);
									}
	
	
								}
								Coordinate[] coordenadasParcela = new Coordinate[coordenadas.size()];
								indice = 0;
								for(Iterator coordenada=coordenadas.iterator();coordenada.hasNext();){
									coordenadasParcela[indice]=(Coordinate)coordenada.next();
									indice++;
								}
	
								if(coordenadasParcela[0].equals3D(coordenadasParcela[coordenadasParcela.length-1])){
									LinearRing lineaParcela = geometriaInicial.getFactory().createLinearRing(coordenadasParcela);
									Polygon poligonoParcela = null;
									poligonoParcela = geometriaInicial.getFactory().createPolygon(lineaParcela, null);
									geometryParcela = poligonoParcela;  
								}
							}    						
						}
					}	
					
				}
			} finally {
				connection.close();
			}
			if (!exceptions.isEmpty()) {
				if (!exceptionsEncountered) {
					context.getIWorkbench().getFrame().getOutputFrame().createNewDocument();
					exceptionsEncountered = true;
				}
				reportExceptions(exceptions, dataSourceQuery, context);
			}
		}
		if (exceptionsEncountered) {
			context.getIWorkbench().getGuiComponent().warnUser("Problems were encountered. See Output Window for details.");
		}
		
		return geometryParcela;
	}
}

	
