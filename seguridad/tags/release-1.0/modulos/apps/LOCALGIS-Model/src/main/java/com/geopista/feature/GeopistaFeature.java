/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 23-may-2004 by juacas
 *
 * Created on 02-may-2004
 */
package com.geopista.feature;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AbstractBasicFeature;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.FeatureEventType;

/**
 * @author juacas
 * 
 * TODO document
 */

public class GeopistaFeature extends AbstractBasicFeature
{
	public Object clone()
	{
	return clone(true);
	}
	public Feature clone(boolean deep)
	{
	 GeopistaFeature clone = new GeopistaFeature(this.getSchema());
     for (int i = 0; i < this.getSchema().getAttributeCount(); i++) {
         if (this.getSchema().getAttributeType(i) == AttributeType.GEOMETRY && this.getGeometry()!=null) {
         	
         
             clone.setAttribute(i, deep ? this.getGeometry().clone() : this.getGeometry());
         } else {
             clone.setAttribute(i, this.getAttribute(i));
         }
     }
     
    IGeopistaLayer capa = this.getLayer();
     if (capa==null && this.getSchema() instanceof GeopistaSchema)
     {
         capa = ((GeopistaSchema)this.getSchema()).getGeopistalayer();
         //setLayer(capa);
     }
     clone.setLayer(capa);
     
     clone.setSystemId(this.getSystemId());
     //clone.setSchema(this.getSchema());
     //((GeopistaSchema)this.getSchema()).getGeopistalayer().getLayerManager().setFiringEvents(firingEvents);
     return clone;  
	
	}
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(GeopistaFeature.class);

    private IGeopistaLayer layer; // Capa a la que esta Feature pertenece.

    public static final String SYSTEM_ID_FEATURE_SIN_INICIALIZAR = "NoInicializado";

    private String systemId = null;

    private boolean isNew = true;

    private boolean deleted;

    private boolean lockedFeature = false;

    private boolean fireDirtyEvents = true;

    /**
     * @param featureSchema
     */
    private boolean dirty = false; // modified flag

    // bloqueamos o desbloquemos la feature para que se utilizar los tipos
    // de Acceso
    // retringido

    public void setLockedFeature(boolean looked)
    {
        this.lockedFeature = looked;
    }

    public boolean isLockedFeature()
    {
        return this.lockedFeature;
    }
public boolean isTempID()
{
    //return getSystemId().substring(0,SYSTEM_ID_FEATURE_SIN_INICIALIZAR.length()).equalsIgnoreCase(SYSTEM_ID_FEATURE_SIN_INICIALIZAR);
    return getSystemId().toUpperCase().startsWith(GeopistaFeature.SYSTEM_ID_FEATURE_SIN_INICIALIZAR.toUpperCase());
}
    /**
     * @return Returns the dirty.
     */
    public boolean isDirty()
    {
        return dirty;
    }

    /**
     * @param dirty
     *            The dirty to set.
     */
    public void setDirty(boolean dirty)
    {
        if (dirty == true && this.dirty != true)
        {
            this.dirty = dirty;
            if (layer != null)
            {
                layer.getLayerManager().fireFeaturesChanged(
                        Arrays.asList(new Feature[] { this }),
                        FeatureEventType.ATTRIBUTES_MODIFIED, layer);
            }
        } else
        {
            this.dirty = dirty;
        }
    }

    /**
     * @return Returns the layer.
     */
    public IGeopistaLayer getLayer()
    {
        return layer;
    }

    /**
     * @param layer
     *            The layer to set.
     */
    public void setLayer(IGeopistaLayer layer)
    {
        this.layer = layer;
    }

    private Object[] attributes;

    /**
     * Constructs a BasicFeature with the given FeatureSchema specifying the
     * attribute names and types.
     */
    public GeopistaFeature(FeatureSchema featureSchema)
        {
            super(featureSchema);
            setSystemId(SYSTEM_ID_FEATURE_SIN_INICIALIZAR+System.currentTimeMillis());
            if (featureSchema == null)
                return;
            attributes = new Object[featureSchema.getAttributeCount()];
        }

    /**
     * Constructor vacío para Java2XML
     * 
     */
    public GeopistaFeature()
        {
            this(null);
            setSystemId(SYSTEM_ID_FEATURE_SIN_INICIALIZAR+System.currentTimeMillis());
        }
    
    public void setAttributesRaw(Object[] attributes)
    {
        this.attributes = attributes;
    }

    /**
     * A low-level accessor that is not normally used. It is called by
     * ViewSchemaPlugIn.
     */
    public void setAttributes(Object[] attributes)
    {

        boolean localIsDirty = false;

        if (super.getSchema() instanceof GeopistaSchema)
        {
            if (this.attributes == null || this.attributes.length == 0)
            {
                this.attributes = attributes;
            } else
            {
                Object tempArray[] = new Object[attributes.length];
                for (int n = 0; n < attributes.length; n++)
                {
                    String attributeAccess = ((GeopistaSchema) super
                            .getSchema()).getAttributeAccess(n);
                    if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                            || attributeAccess.equals(GeopistaSchema.READ_ONLY))
                    {
                        tempArray[n] = this.attributes[n];
                    } else
                    {
                        if (attributes[n] != null
                                && !(attributes[n].equals(this.attributes[n])))
                        {
                            localIsDirty = true;
                            tempArray[n] = attributes[n];
                        } else
                        {
                            if (this.attributes[n] != null)
                            {
                                localIsDirty = true;
                                tempArray[n] = attributes[n];
                            }
                        }
                    }
                }
                if (localIsDirty == true)
                {
                    this.attributes = tempArray;
                    
                    
                    /**/
                     if (this.getLayer()==null &&
                             this.getSchema()!=null &&
                             this.getSchema()instanceof GeopistaSchema &&
                             ((GeopistaSchema)this.getSchema()).getGeopistalayer()!=null &&
                             ((GeopistaSchema)this.getSchema()).getGeopistalayer().isExtracted()
                             )
                      
                     {
                         this.setLayer(((GeopistaSchema)this.getSchema()).getGeopistalayer());
                         if (fireDirtyEvents)
                         {
                             this.dirty=false;
                             setDirty(true);
                         }
                         this.setLayer(null);
                     
                     }
                     
                     else
                     {
                     /**/
                    
                    if (fireDirtyEvents)
                    {
                        setDirty(true);
                    }
                    
                     }
                }
            }
        } else
        {
            this.attributes = attributes;
        }

    }

    /**
     * Sets the specified attribute.
     * 
     * @param attributeIndex
     *            the array index at which to put the new attribute
     * @param newAttribute
     *            the new attribute
     */
    public void setAttribute(int attributeIndex, Object newAttribute)
    {

        boolean localIsDirty = false;

        if (super.getSchema() instanceof GeopistaSchema)
        {
            // Comprobamos si el attributo se puede escribir antes de cambiarlo
            String attributeAccess = ((GeopistaSchema) super.getSchema())
                    .getAttributeAccess(attributeIndex);
            if (attributeAccess.equals(GeopistaSchema.READ_WRITE)
                    || !lockedFeature)
            {
         
                Object oldAttribute = this.getAttribute(attributeIndex);
                if (newAttribute != null && !newAttribute.equals(oldAttribute))
                {
                    localIsDirty = true;
                } else
                {
                    if (oldAttribute != null)
                    {
                        localIsDirty = true;
                    }
                }
                if (localIsDirty == true)
                {
                    attributes[attributeIndex] = newAttribute;
                    
                    //parche temporal para solucionar que no venga un autoiddomain en las capas
                    Column attColumn = ((GeopistaSchema) getSchema()).getColumnByAttribute(attributeIndex);
                    Domain defDomain = null;
                    if (attColumn.getName().equalsIgnoreCase("ID")&&attColumn.getDomain()==null)
                    {
                        defDomain = new AutoFieldDomain("ID", "Dominio Por Defecto");
                        attColumn.setDomain(defDomain);                                                            
                    }
                    
                    if(attColumn.getDomain() instanceof AutoFieldDomain)
                    {
                        if(isTempID() && attColumn.getDomain().getPattern().equalsIgnoreCase("ID"))
                        {
                            setSystemId((getAttributesDirectly()[attributeIndex]).toString());
                        }
                    }
                    
                    
                    if (fireDirtyEvents)
                    {
                        setDirty(true);
                    }
                }
            }
        } else
        {
            attributes[attributeIndex] = newAttribute;
        }
    }

    // sobrescribimos el metodo de la clase super para comprobar si se puede
    // cambiar el atributo
    public void setAttribute(String attributeName, Object newAttribute)
    {
    super.setAttribute(attributeName,newAttribute);

    }

    /**
     * Returns the specified attribute.
     * 
     * @param i
     *            the index of the attribute to get
     * @return the attribute
     */
    public Object getAttribute(int i)
    {
        if (super.getSchema() instanceof GeopistaSchema)
        {
            String attributeAccess = ((GeopistaSchema) super.getSchema())
                    .getAttributeAccess(i);
            if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                    && (lockedFeature))
            {
                return null;
            }
        
        // Comprueba si es un AutoDomain
        
            
            Domain dom = ((GeopistaSchema)getSchema()).getColumnByAttribute(i).getDomain();
            if (dom==null)
            	{
            	if (getSchema().getAttributeType(i)!= AttributeType.GEOMETRY)
				logger.debug(
						"getAttribute() - El atributo: "+getSchema().getAttributeName(i)+" no tiene dominio. : Domain dom = "
								+ dom, null);
            	}
            else
            if (dom.getType()==Domain.AUTO && 
            		((AutoFieldDomain)dom).getRightValue(this,i)!=null)
            	return((AutoFieldDomain)dom).getRightValue(this,i);
        }
            return attributes[i];

        // We used to eat ArrayOutOfBoundsExceptions here. I've removed this
        // behaviour
        // because ArrayOutOfBoundsExceptions are bugs and should be
        // exposed. [Jon Aquino]
    }

    // sobreescribimos la funcion del super para hacer las comprobaciones de
    // acceso
    public Object getAttribute(String name)
    {
        if (super.getSchema() instanceof GeopistaSchema)
        {
            String attributeAccess = ((GeopistaSchema) super.getSchema())
                    .getAttributeAccess(name);
            if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                    && (lockedFeature))
            {
                return null;
            }
        }
        return super.getAttribute(name);
    }

    // sobreescribimos la funcion del super para hacer las comprobaciones de
    // acceso
    public String getString(int attributeIndex)
    {
        if (super.getSchema() instanceof GeopistaSchema)
        {
            String attributeAccess = ((GeopistaSchema) super.getSchema())
                    .getAttributeAccess(attributeIndex);
            if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                    && (lockedFeature))
            {
                return null;
            }
        }
        return super.getString(attributeIndex).trim();
    }

    // sobreescribimos la funcion del super para hacer las comprobaciones de
    // acceso
    public String getString(String attributeName)
    {
        if (super.getSchema() instanceof GeopistaSchema)
        {
            String attributeAccess = ((GeopistaSchema) super.getSchema())
                    .getAttributeAccess(attributeName);
            if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                    && (lockedFeature))
            {
                return "";
            }
        }
        return super.getString(attributeName).trim();
    }

    /**
     * A low-level accessor that is not normally used. It is called by
     * ViewSchemaPlugIn.
     */
    public Object[] getAttributes()
    {
        if (super.getSchema() instanceof GeopistaSchema)
        {
            Object tempArray[] = new Object[attributes.length];
            for (int n = 0; n < attributes.length; n++)
            {
                String attributeAccess = ((GeopistaSchema) super.getSchema())
                        .getAttributeAccess(n);
                if (attributeAccess.equals(GeopistaSchema.NO_ACCESS)
                        && (lockedFeature))
                {
                    tempArray[n] = null;
                } else
                {
                    tempArray[n] = getAttribute(n);
                }
            }
            return tempArray;
        } else
        {
            return attributes;
        }
    }

    public String getSystemId()
    {
        return systemId;
    }

    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean newDeleted)
    {
        deleted = newDeleted;
    }
    
    public String getErrorGeometria(Geometry geometry){
    	
    	String errorGeometria=null;
    
    	if (getSchema() instanceof GeopistaSchema)
        {
            GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
            GeopistaSchema geopistaSchema = (GeopistaSchema) getSchema();
            int geometryIndex = getSchema().getGeometryIndex();

            int layerGeometryType = geopistaSchema.getColumnByAttribute(
                    geometryIndex).getTable().getGeometryType();

            switch (layerGeometryType)
            {
            case Table.COLLECTION:
            	errorGeometria= "Capa definida como COLLECTION"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.MULTIPOLYGON:
            	errorGeometria= "Capa definida como MULTIPOLYGON"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.MULTILINESTRING:
            	errorGeometria= "Capa definida como MULTILINESTRING"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.MULTIPOINT:
            	errorGeometria= "Capa definida como MULTIPOINT"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.POINT:
            	errorGeometria= "Capa definida como POINT"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.LINESTRING:
            	errorGeometria= "Capa definida como LINESTRING"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.POLYGON:
            	errorGeometria= "Capa definida como POLYGON"+" vs geometria encontrada:"+geometry.getClass();
                break;
            case Table.GEOMETRY:
                break;

            default:

            }
        }
        return errorGeometria;  
    }
    

    public void setGeometry(Geometry geometry)
    {
        if (getSchema() instanceof GeopistaSchema)
        {
            GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
            GeopistaSchema geopistaSchema = (GeopistaSchema) getSchema();
            int geometryIndex = getSchema().getGeometryIndex();

            int layerGeometryType = geopistaSchema.getColumnByAttribute(
                    geometryIndex).getTable().getGeometryType();

            switch (layerGeometryType)
                {
                case Table.COLLECTION:
                    if (geometry.getClass() != GeometryCollection.class)
                    {
                        Geometry[] arrayGeometryCollection = new Geometry[] { geometry };
                        geometry = factory
                                .createGeometryCollection(arrayGeometryCollection);
                    }
                    break;
                case Table.MULTIPOLYGON:
                    if (geometry.getClass() == Polygon.class)
                    {
                        Polygon[] arrayGeometryMultiPoligon = new Polygon[] { (Polygon) geometry };
                        geometry = factory
                                .createMultiPolygon(arrayGeometryMultiPoligon);
                    } else if (geometry.getClass() == MultiPolygon.class)
                    {

                    } else
                    {
                        throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    break;

                case Table.MULTILINESTRING:
                    if (geometry.getClass() == LineString.class)
                    {
                        LineString[] arrayGeometryMultiLineString = new LineString[] { (LineString) geometry };
                        geometry = factory
                                .createMultiLineString(arrayGeometryMultiLineString);
                    } else if (geometry.getClass() == MultiLineString.class)
                    {

                    } else
                    {
                        throw new IllegalArgumentException();
                    }
                    break;

                case Table.MULTIPOINT:
                    if (geometry.getClass() == Point.class)
                    {
                        Point[] arrayGeometryMultiPoint = new Point[] { (Point) geometry };
                        geometry = factory
                                .createMultiPoint(arrayGeometryMultiPoint);
                    } else if (geometry.getClass() == MultiPoint.class)
                    {
// Do nothing
                    } else
                    {
                        throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    break;

                case Table.POINT:
                    if (geometry.getClass() != Point.class)
                    {
                        throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    break;

                case Table.LINESTRING:
                    if (geometry.getClass() != LineString.class)
                    {
                        throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    break;

                case Table.POLYGON:
                    if (geometry.getClass() != Polygon.class)
                    {
                    	//logger.error("Tabla de tipo poligono pero geometria es de tipo:"+geometry.getClass());
                        throw new IllegalArgumentException(AppContext.getMessage("TipoGeometriaNoPermitida"));
                    }
                    break;
                case Table.GEOMETRY:

                    break;

                default:
                    throw new IllegalArgumentException("Geometria desconocida");

                }
            setAttribute(geometryIndex, geometry);
        } else
        {
            super.setGeometry(geometry);
        }
    }

    /**
     * @return Returns the fireDirtyEvents.
     */
    public boolean isFireDirtyEvents()
    {
        return fireDirtyEvents;
    }

    /**
     * @param fireDirtyEvents
     *            The fireDirtyEvents to set.
     */
    public void setFireDirtyEvents(boolean fireDirtyEvents)
    {
        this.fireDirtyEvents = fireDirtyEvents;
    }
    
    public void addExtractId(Object value)
    {
        List newAttributesList = Arrays.asList(this.attributes);
        newAttributesList.add(value);
        this.attributes = newAttributesList.toArray();
    }
    
    public void removeExtractId()
    {
        
    }
	public double getDouble(int attributeIndex)
	{
	
        Object currentAttribute = getAttribute(attributeIndex); 
        if(currentAttribute instanceof Number) return ((Number) currentAttribute).intValue(); 
        if(currentAttribute instanceof String) return Double.parseDouble((String) currentAttribute);
        
        throw new IllegalArgumentException("El tipo de atributo no se puede convertir a double");
        
	  
	}
	public int getInteger(int attributeIndex)
	{
        Object currentAttribute = getAttribute(attributeIndex); 
	    if(currentAttribute instanceof Number) return ((Number) currentAttribute).intValue(); 
	    if(currentAttribute instanceof String) return Integer.parseInt((String) currentAttribute);
        
        throw new IllegalArgumentException("El tipo de atributo no se puede convertir a int");
        
	}
    
    public Object[] getAttributesDirectly()
    {
        return this.attributes;
    }
    public String toString(){
    	return "FID:"+getSystemId();
    }
    
    private Geometry empty = null;
    
    public boolean isGeometryEmpty(){
    	return empty!=null;
    }

    public boolean setGeometryEmpty()
    {
    	if (getSchema() instanceof GeopistaSchema)
    	{
    		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
    		GeopistaSchema geopistaSchema = (GeopistaSchema) getSchema();
    		int geometryIndex = getSchema().getGeometryIndex();

    		int layerGeometryType = geopistaSchema.getColumnByAttribute(
    				geometryIndex).getTable().getGeometryType();

    		if (empty == null){

    			switch (layerGeometryType)
    			{
    			case Table.COLLECTION:
    				empty = factory.createGeometryCollection(null);
    				break;
    			case Table.MULTIPOLYGON:
    				empty = factory.createMultiPolygon(null);
    				break;

    			case Table.MULTILINESTRING:
    				empty = factory.createMultiLineString(null);
    				break;

    			case Table.MULTIPOINT:
    				empty = factory.createMultiPoint((Point[]) null);
    				break;

    			case Table.POINT:
    				empty = factory.createPoint((Coordinate)null);
    				break;

    			case Table.LINESTRING:
    				empty = factory.createLineString((Coordinate[]) null);
    				break;

    			case Table.POLYGON:
    				empty = factory.createPolygon(null, null);
    				break;
    			case Table.GEOMETRY:
    				empty = factory.createGeometryCollection(null);
    				break;

    			default:
    				throw new IllegalArgumentException("Geometria desconocida");

    			} 

    			if (empty != null){
    				setAttribute(geometryIndex, empty);
    				return true;
    			}
    			else{
    				return false;
    			}
    		}
    		else{
    			setAttribute(geometryIndex, empty);
    			return true;
    		}

    	}
    	else{
    		return false;
    	}

    }
}
