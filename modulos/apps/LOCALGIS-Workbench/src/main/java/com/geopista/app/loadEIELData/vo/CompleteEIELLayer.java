/**
 * CompleteEIELLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * CompleteEIELLayer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.loadEIELData.vo;

import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;

import java.awt.Color;
import java.awt.Paint;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.AttributeExpression;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.openjump.util.CustomTexturePaint;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.WKTReader;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.BitmapVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.CircleVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.CrossVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.EIELSquareVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.EIELStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.EIELVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.TriangleVertexStyle;







public class CompleteEIELLayer  implements java.io.Serializable {
    private java.lang.String SLDStyle;

    private com.geopista.app.loadEIELData.vo.EIELColumn[] eielColumns;

    private com.geopista.app.loadEIELData.vo.EIELFeature[] eielFeatures;

    private java.lang.String geometryField;

    private java.lang.String id;

    private java.lang.String idField;

    private java.lang.String lang_name;

    private java.lang.String table;

    public CompleteEIELLayer() {
    }

    public CompleteEIELLayer(
           java.lang.String SLDStyle,
           com.geopista.app.loadEIELData.vo.EIELColumn[] eielColumns,
           com.geopista.app.loadEIELData.vo.EIELFeature[] eielFeatures,
           java.lang.String geometryField,
           java.lang.String id,
           java.lang.String idField,
           java.lang.String lang_name,
           java.lang.String table) {
           this.SLDStyle = SLDStyle;
           this.eielColumns = eielColumns;
           this.eielFeatures = eielFeatures;
           this.geometryField = geometryField;
           this.id = id;
           this.idField = idField;
           this.lang_name = lang_name;
           this.table = table;
    }
    
    
    
    
	 /**Crea la FeatureCollection a partir de las features de la capa
	  */
	 public FeatureCollection createFeatureCollection(){
		 FeatureSchema schema=createFeatureSchema();
		 
		 List features = new LinkedList();
		 
		 try {
		 for(int i=0;i<eielFeatures.length;i++){
			 //1. Para empezar...
			 BasicFeature feature=new BasicFeature(schema);
			 feature.setLockedFeature(false);
			 feature.setFireDirtyEvents(false);
		
			 //2.Rellenamos la feature con sus atributos...
			HashMap atts=eielFeatures[i].getAttributes();
			
			if(atts.get(geometryField)==null)
				return AddNewLayerPlugIn.createBlankFeatureCollection();
				
			
			for (Iterator enumerationElement =atts.keySet().iterator(); enumerationElement.hasNext(); ){
   			String columnName=(String)enumerationElement.next();
   			try{
   			AttributeType type=schema.getAttributeType(columnName);
   			
   			if(type!=null){
   				
   				if(columnName.equalsIgnoreCase(geometryField)){//columna geométrica
   					 WKTReader wktReader=new WKTReader();
   					 FeatureCollection fc = wktReader.read(new StringReader((String) atts.get(columnName)));
   	        		 Feature jumpFeature=(Feature)fc.iterator().next();
   	        		 feature.setAttribute(columnName,jumpFeature.getGeometry());
   				}
   				else if(columnName.equalsIgnoreCase(idField)){//columna ID
   					Object oValue=atts.get(columnName);
   					feature.setAttribute(columnName,oValue);
   					feature.setSystemId(oValue.toString());
   				}
   				else{//resto de columnas
   					Object oValue=atts.get(columnName);
   					feature.setAttribute(columnName,oValue);
   				}
   			}
   			}catch(IllegalArgumentException e){
   				//Esta excepción se produce cuando intentamos acceder al tipo de una de las columnas geométricas que hemos ignorado 
   				//(aquéllas que no sean la geometryField).
   				//Por tanto no se trata de un error, no hacemos nada.
   			}
   		}//fin del for que recorre los atributos de una feature

   		
			//3. Para terminar...
			 feature.setLockedFeature(false);
			 feature.setFireDirtyEvents(false);
			 features.add(feature);		 
		 }//fin del for que recorre las features
		 
		 FeatureCollection collection = new FeatureDataset(features, schema);
		 return collection;
		 
		 } catch (Exception e) {
			 e.printStackTrace();
				return null;
			}
	 }
	 
	 
	 
	 /**Crea el FeatureSchema a partir de la información de las columnas de la capa
	  */
	 private  FeatureSchema createFeatureSchema(){
		 FeatureSchema schema=new FeatureSchema();
		 
		 for(int i=0;i<eielColumns.length;i++){
			 EIELColumn column=eielColumns[i];
			 String columnName=column.getName();
			 String columnType=column.getType();
			 
			 if(columnType==null){
				 //el tipo de la columna nunca debería de ser nulo
				  Assert.shouldNeverReachHere();
			 }
			 
			 
			 if(columnType!=null&&
					((columnType.equalsIgnoreCase("geometry")&&columnName.equalsIgnoreCase(geometryField))
					 || (!columnType.equalsIgnoreCase("geometry"))
					 )){
				 //las columnas geométricas que no sean la que nos interesa (geometry_column) se ignoran
				 schema.addAttribute(columnName, getAttributeType(columnType)); 
			 }//fin if
		 }//fin for
		 
		 
		 return schema;
	 }//fin del método
	 
	 
	
	 /**Devuelve una instancia de AttributeType dado el nombre de su clase envoltorio
	  */
	 private static AttributeType getAttributeType(String javaClassName){
		  AttributeType attType=null;
		  
		  if(javaClassName.equals("java.lang.String"))
			  attType=AttributeType.STRING;
			  
		  else if(javaClassName.equals("geometry"))
			  attType=AttributeType.GEOMETRY;
		  
		  else if(javaClassName.equals("java.lang.Integer"))
			  attType=AttributeType.INTEGER;
			  
		  else if(javaClassName.equals("java.lang.Long"))
			  attType=AttributeType.LONG;
			  
		  else if(javaClassName.equals("java.sql.Date"))
			  attType=AttributeType.DATE;
			  
		  else if(javaClassName.equals("java.lang.Double"))
			  attType=AttributeType.DOUBLE;
		  
		  else if(javaClassName.equals("java.lang.Float"))
			  attType=AttributeType.FLOAT;
		  
		  else attType=AttributeType.OBJECT;
		  
         return attType;
	 }//fin del método 
	 
	 
	 
    
    /**A partir del estilo SLD de la capa obtiene una lista de objetos Style de jump, para poder representarlos en el
     * editor GIS.
     */
	 public List getStyle(){
		 	SLDParser parser = new SLDParser(StyleFactoryFinder.createStyleFactory());
		 	if(SLDStyle!=null){
			parser.setInput(new StringReader(SLDStyle));
			Style[] estilos = parser.readXML();
			Style estilo = estilos[0];
			
			// recuperamos las features
			FeatureTypeStyle[] arrayFeatures = estilo.getFeatureTypeStyles();
			// cogemos la primera de todas ellas
			FeatureTypeStyle feature = null;
			if (arrayFeatures.length > 0) {
				feature = arrayFeatures[0];

				// recuperamos las reglas de la primera feature
				Rule[] arrayRules = feature.getRules();
				// cogemos la primera de las reglas
				Rule rule = null;
				if (arrayRules.length > 0) {
					rule = arrayRules[0];

					// recuperamos los symbolizers
					Symbolizer[] arraySymbolizers = rule.getSymbolizers();
					// cogemos el primer symbolizer
					Symbolizer symbolizer = null;
					if (arraySymbolizers.length > 0) {
						symbolizer = arraySymbolizers[0];
												
						return transformSymbolizerToStyles(symbolizer);

					} else {

					}
				} else {
					//no hay reglas				
					Assert.shouldNeverReachHere();
				}
			} else {
				//no hay features
				Assert.shouldNeverReachHere();
			}
		 	}
			return null;
    
	 	}//fin del método
	 
	 
	 
	 
	 
		/**Devuelve estilo jump
		 */
		private List transformSymbolizerToStyles(Symbolizer symbolizer) {
			if(this.lang_name.equalsIgnoreCase("Captaciones")){
				System.out.println("Aquí nos paramos");
			}
			
			//LÍNEA...
			if (symbolizer instanceof LineSymbolizer) {
				LineSymbolizer ls = (LineSymbolizer) symbolizer;
				
				EIELStyle style = new EIELStyle();
				
				Stroke stroke = ls.getStroke();
				
				double opacity = Double.parseDouble((((org.geotools.filter.Expression)stroke.getOpacity()).getValue(null).toString()));
				Color color = Color.decode((String) ((org.geotools.filter.Expression)stroke.getColor()).getValue(null));
				int strokeWidth=Math.round(Float.parseFloat(((org.geotools.filter.Expression)stroke.getWidth()).getValue(
						null).toString()));

				 style.setLineColor(color);
				 style.setAlpha((int) (255 * opacity));
				 style.setLineWidth(strokeWidth);
				 style.setEnabled(true);
				 			
				 LinkedList styles = new LinkedList();
				 styles.add(style);
				 
					
					return styles;
			}//fin if: LineSymbolizer
			
			
			
			//PUNTO...
			 else if (symbolizer instanceof PointSymbolizer) {
				
					PointSymbolizer ps = (PointSymbolizer) symbolizer;
					
					int tamano = Math.round(Float.parseFloat(((org.geotools.filter.Expression)ps.getGraphic().getSize()).getValue(null).toString()));
					
					ExternalGraphic[] graficosExternos = ps.getGraphic().getExternalGraphics();
					Mark[] marcas = ps.getGraphic().getMarks();

					
					//Tratamiento de gráficos externos
					if (graficosExternos != null && graficosExternos.length > 0) {
						ExternalGraphic eg = graficosExternos[0];
						URI uri = null;
						try {
							URL url = eg.getLocation();
							BitmapVertexStyle vertexStyle = new BitmapVertexStyle(url.getFile());
							vertexStyle.setSize(tamano);
							vertexStyle.setEnabled(true);
							
							BasicStyle b = new BasicStyle();
						
							LinkedList styles = new LinkedList();
							styles.add(vertexStyle);
							styles.add(b);
							
							return styles;
						} catch (MalformedURLException e) {
							System.out
									.println("-> Error al recuperar la URL de un ExternalGraphics de un PointSymbolizer");
							e.printStackTrace();
						}
					} //fin del tratamiento de los gráficos externos
					
					
					//Tratamos el elemento Mark
					else if (marcas != null && marcas.length > 0) {
						Mark m = marcas[0];

						
						Fill fill = m.getFill();
						float opacity = Float.parseFloat(((org.geotools.filter.Expression)fill.getOpacity()).getValue(null)
								.toString());
						Color color = Color.decode((String) ((org.geotools.filter.Expression)fill.getColor()).getValue(null));
						
						EIELVertexStyle eielVertexStyle = null;
						String tipo = (String) ((org.geotools.filter.Expression)m.getWellKnownName()).getValue(null);
						if (tipo.toLowerCase().equals("circle")) {
							eielVertexStyle = new CircleVertexStyle();
						} else if (tipo.toLowerCase().equals("square")) {
							eielVertexStyle = new EIELSquareVertexStyle();
						} else if (tipo.toLowerCase().equals("triangle")) {
							eielVertexStyle = new TriangleVertexStyle();
						} else if (tipo.toLowerCase().equals("cross")) {
							eielVertexStyle = new CrossVertexStyle();
						}	
						
					
						eielVertexStyle.setSize(tamano);
						eielVertexStyle.setFillColor(color);
						eielVertexStyle.setLineColor(color);
						eielVertexStyle.setAlpha((int) (255 * opacity));
						eielVertexStyle.setEnabled(true);
												
						BasicStyle b = new BasicStyle();
						b.setFillColor(color);
						b.setLineColor(color);
						b.setAlpha((int) (255 * opacity));
						b.setEnabled(true);
						
						LinkedList styles = new LinkedList();
						
						styles.add(eielVertexStyle);
						styles.add(b);
						
						return styles;
					}//fin del trato del elemento Mark
				}//fin if: PointSymbolizer
			
			
			
			//POLÍGONO...
			else if (symbolizer instanceof PolygonSymbolizer) {
				PolygonSymbolizer ps = (PolygonSymbolizer) symbolizer;
				
				EIELStyle style = new EIELStyle();
				
				// 1. Relleno
				Fill fill = ps.getFill();
				if (fill != null) {
					float opacity = Float.parseFloat(((org.geotools.filter.Expression)fill.getOpacity()).getValue(null).toString());				
					Color fillColor = Color.decode((String) ((org.geotools.filter.Expression)fill.getColor()).getValue(null));

					style.setFillColor(fillColor);
					style.setAlpha((int) (255 * opacity));

					Graphic g = fill.getGraphicFill();
					if (g != null) {
						ExternalGraphic[] graficosExternos = g
								.getExternalGraphics();
						if (graficosExternos != null && graficosExternos.length > 0) {
							ExternalGraphic eg = graficosExternos[0];						
							try {
								URL url = eg.getLocation();
								
								if(url!=null){
									Paint p = new CustomTexturePaint(url);
									style.setFillPattern(p);
									style.setRenderingFillPattern(true);
								}
								
							} catch (MalformedURLException e) {
								System.out.println("Error al recuperar la URL de un ExternalGraphics de un PolygonSymbolizer");
								e.printStackTrace();
						}
					}
				}
				}//fin tratamiento del relleno
				
				// 2. Contorno
				Stroke stroke = ps.getStroke();
				if (stroke != null) {
					float opacity = Float.parseFloat(((org.geotools.filter.Expression)stroke.getOpacity()).getValue(null).toString());
					Color lineColor = Color.decode((String) ((org.geotools.filter.Expression)stroke.getColor()).getValue(null));
					float strokeWidth = Float.parseFloat(((org.geotools.filter.Expression)stroke.getWidth()).getValue(null).toString());
	
					 style.setLineColor(lineColor);
					 style.setAlpha((int) (255 * opacity));
					 style.setLineWidth(strokeWidth);
				}//fin tratamiento del contorno
				
				style.setEnabled(true);
				
								
				LinkedList styles = new LinkedList();
				styles.add(style);
				
				return styles;
			}//fin if: PolygonSymbolizer
			 
			 
			 
			 else if (symbolizer instanceof TextSymbolizer) {
				TextSymbolizer ts = (TextSymbolizer) symbolizer;
				
				LabelStyle style = new LabelStyle();

				Fill fill = ts.getFill();
				Font[] fuentes = ts.getFonts();
				String nombreGeometria = ts.getGeometryPropertyName(); 
				LabelPlacement posicionEtiqueta = ts.getLabelPlacement();
				String etiqueta = ((AttributeExpression) ts.getLabel()).getAttributePath();
				
				style.setAttribute(etiqueta);
				
				if (fill != null) {
					float opacity = Float.parseFloat(((org.geotools.filter.Expression)fill.getOpacity()).getValue(null).toString());
					Color c = Color.decode((String) ((org.geotools.filter.Expression)fill.getColor()).getValue(null));
					style.setColor(c);
				}
				
				

				Font fuente = null;
				if (fuentes != null && fuentes.length > 0) {
					fuente = fuentes[0];
					String familia = ((org.geotools.filter.Expression)fuente.getFontFamily()).getValue(null).toString();
					float tamano = Float.parseFloat(((org.geotools.filter.Expression)fuente.getFontSize()).getValue(null).toString());
					String weight = (String) ((org.geotools.filter.Expression)fuente.getFontWeight()).getValue(null);
					String fontStyle = (String) ((org.geotools.filter.Expression)fuente.getFontStyle()).getValue(null);
					
					int fStyle=0;
					
			                if (fontStyle.equalsIgnoreCase("normal")) {
			                    fStyle |= PLAIN;
			                }
			                if (fontStyle.equalsIgnoreCase("italic")) {
			                    fStyle |= ITALIC;
			                }
			            

			                if (weight.equalsIgnoreCase("normal")) {
			                    fStyle |= PLAIN;
			                }
			                if (weight.equalsIgnoreCase("bold")) {
			                    fStyle |= BOLD;
			                }
					
					java.awt.Font fuenteJava = new java.awt.Font(familia,fStyle, Math.round(tamano));
					style.setFont(fuenteJava);
					style.setEnabled(true);
					
				} else {
					System.out.println("--> Encontrado un text simbolizer sin fuente asociada");
				}
				style.setHorizontalAlignment(LabelStyle.JUSTIFY_RIGHT);
				style.setVerticalAlignment(LabelStyle.ON_LINE);
				
				LinkedList styles = new LinkedList();
				styles.add(style);
				
				return styles;
			}//fin if: TextSymbolizer
			
			return null;
		}
	 
	 
    


    /**
     * Gets the SLDStyle value for this CompleteEIELLayer.
     * 
     * @return SLDStyle
     */
    public java.lang.String getSLDStyle() {
        return SLDStyle;
    }


    /**
     * Sets the SLDStyle value for this CompleteEIELLayer.
     * 
     * @param SLDStyle
     */
    public void setSLDStyle(java.lang.String SLDStyle) {
        this.SLDStyle = SLDStyle;
    }


    /**
     * Gets the eielColumns value for this CompleteEIELLayer.
     * 
     * @return eielColumns
     */
    public com.geopista.app.loadEIELData.vo.EIELColumn[] getEielColumns() {
        return eielColumns;
    }


    /**
     * Sets the eielColumns value for this CompleteEIELLayer.
     * 
     * @param eielColumns
     */
    public void setEielColumns(com.geopista.app.loadEIELData.vo.EIELColumn[] eielColumns) {
        this.eielColumns = eielColumns;
    }


    /**
     * Gets the eielFeatures value for this CompleteEIELLayer.
     * 
     * @return eielFeatures
     */
    public com.geopista.app.loadEIELData.vo.EIELFeature[] getEielFeatures() {
        return eielFeatures;
    }


    /**
     * Sets the eielFeatures value for this CompleteEIELLayer.
     * 
     * @param eielFeatures
     */
    public void setEielFeatures(com.geopista.app.loadEIELData.vo.EIELFeature[] eielFeatures) {
        this.eielFeatures = eielFeatures;
    }


    /**
     * Gets the geometryField value for this CompleteEIELLayer.
     * 
     * @return geometryField
     */
    public java.lang.String getGeometryField() {
        return geometryField;
    }


    /**
     * Sets the geometryField value for this CompleteEIELLayer.
     * 
     * @param geometryField
     */
    public void setGeometryField(java.lang.String geometryField) {
        this.geometryField = geometryField;
    }


    /**
     * Gets the id value for this CompleteEIELLayer.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this CompleteEIELLayer.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the idField value for this CompleteEIELLayer.
     * 
     * @return idField
     */
    public java.lang.String getIdField() {
        return idField;
    }


    /**
     * Sets the idField value for this CompleteEIELLayer.
     * 
     * @param idField
     */
    public void setIdField(java.lang.String idField) {
        this.idField = idField;
    }


    /**
     * Gets the lang_name value for this CompleteEIELLayer.
     * 
     * @return lang_name
     */
    public java.lang.String getLang_name() {
        return lang_name;
    }


    /**
     * Sets the lang_name value for this CompleteEIELLayer.
     * 
     * @param lang_name
     */
    public void setLang_name(java.lang.String lang_name) {
        this.lang_name = lang_name;
    }


    /**
     * Gets the table value for this CompleteEIELLayer.
     * 
     * @return table
     */
    public java.lang.String getTable() {
        return table;
    }


    /**
     * Sets the table value for this CompleteEIELLayer.
     * 
     * @param table
     */
    public void setTable(java.lang.String table) {
        this.table = table;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CompleteEIELLayer)) return false;
        CompleteEIELLayer other = (CompleteEIELLayer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.SLDStyle==null && other.getSLDStyle()==null) || 
             (this.SLDStyle!=null &&
              this.SLDStyle.equals(other.getSLDStyle()))) &&
            ((this.eielColumns==null && other.getEielColumns()==null) || 
             (this.eielColumns!=null &&
              java.util.Arrays.equals(this.eielColumns, other.getEielColumns()))) &&
            ((this.eielFeatures==null && other.getEielFeatures()==null) || 
             (this.eielFeatures!=null &&
              java.util.Arrays.equals(this.eielFeatures, other.getEielFeatures()))) &&
            ((this.geometryField==null && other.getGeometryField()==null) || 
             (this.geometryField!=null &&
              this.geometryField.equals(other.getGeometryField()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idField==null && other.getIdField()==null) || 
             (this.idField!=null &&
              this.idField.equals(other.getIdField()))) &&
            ((this.lang_name==null && other.getLang_name()==null) || 
             (this.lang_name!=null &&
              this.lang_name.equals(other.getLang_name()))) &&
            ((this.table==null && other.getTable()==null) || 
             (this.table!=null &&
              this.table.equals(other.getTable())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSLDStyle() != null) {
            _hashCode += getSLDStyle().hashCode();
        }
        if (getEielColumns() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEielColumns());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEielColumns(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEielFeatures() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEielFeatures());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEielFeatures(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getGeometryField() != null) {
            _hashCode += getGeometryField().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdField() != null) {
            _hashCode += getIdField().hashCode();
        }
        if (getLang_name() != null) {
            _hashCode += getLang_name().hashCode();
        }
        if (getTable() != null) {
            _hashCode += getTable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CompleteEIELLayer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "CompleteEIELLayer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SLDStyle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "SLDStyle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eielColumns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "eielColumns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "EIELColumn"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.loadEIELData.app.geopista.com", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eielFeatures");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "eielFeatures"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "EIELFeature"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.loadEIELData.app.geopista.com", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("geometryField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "geometryField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "idField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lang_name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "lang_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("table");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "table"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
