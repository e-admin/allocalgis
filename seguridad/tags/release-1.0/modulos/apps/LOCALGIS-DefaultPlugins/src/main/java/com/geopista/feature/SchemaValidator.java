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
 * 
 * Created on 09-may-2004
 *
 * Validación de la concordancia de la feature con su esquema
 *
 */
package com.geopista.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaContext;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;

/**
 * @author juacas
 * 
  */
public class SchemaValidator extends AbstractValidator {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(SchemaValidator.class);

  private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  
	/**
	 * @param context Referencia al Framework
	 */
	public SchemaValidator(GeopistaContext context) {
		super(context);
		
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.AbstractValidator#validateFeature(com.vividsolutions.jump.feature.Feature)
	 */
	public boolean validateFeature(Feature feature) {
    if (!(feature.getSchema() instanceof GeopistaSchema)) return true;
		// Check each attribute against its domain
		GeopistaSchema schema=(GeopistaSchema)feature.getSchema();
		int numAtt = schema.getAttributeCount();
		resetErrorCount();
		for (int i =0;i<numAtt;i++)
			{
			Domain domain= schema.getAttributeDomain(i);
			
			if(domain==null)
				{
				if(schema.getAttributeType(i)==AttributeType.GEOMETRY)
					{
					if(feature instanceof GeopistaFeature)
						{
						int geometryIndex = feature.getSchema().getGeometryIndex();
						int layerGeometryType = ((GeopistaSchema)feature.getSchema()).getColumnByAttribute(geometryIndex).getTable().getGeometryType();
						Geometry geometry = feature.getGeometry();
						
						switch(layerGeometryType) 
						{
						case Table.COLLECTION:
						if(geometry.getClass() != GeometryCollection.class)
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						case Table.MULTIPOLYGON: 
						if(geometry.getClass() != MultiPolygon.class)
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						
						case Table.MULTILINESTRING: 
						if((geometry.getClass() != MultiLineString.class)&&(geometry.getClass()!=LineString.class))
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						
						case Table.MULTIPOINT: 
						if((geometry.getClass() != MultiPoint.class)&&(geometry.getClass()!=Point.class))
						{
						String message = aplicacion.getI18nString("GeometriaNoValida");
						notifyError(schema.getAttributeName(i), message,domain);
						}
						break;
						
						case Table.POINT:
						if(geometry.getClass() != Point.class)
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						
						case Table.LINESTRING:
						if(geometry.getClass() != LineString.class)
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						
						case Table.POLYGON:
						if(geometry.getClass() != Polygon.class)
							{
							String message = aplicacion.getI18nString("GeometriaNoValida");
							notifyError(schema.getAttributeName(i), message,domain);
							}
						break;
						case Table.GEOMETRY:
						break;
						default:
						String message = aplicacion.getI18nString("GeometriaDesconocida");
						notifyError(schema.getAttributeName(i), message,domain);
						break;
						}
						
						}
					}
				 else // No es geometry pero no tiene Dominio asociado
					if (logger.isDebugEnabled())
						{
						logger.debug("validateFeature(Feature) - El atributo "
								+ schema.getAttributeName(i) + " NO tiene dominio");
						}
						
			}
			else if (schema.getAttributeType(i)!=AttributeType.GEOMETRY
					&&
					domain.validate(feature,schema.getAttributeName(i),null)==false)
				{
				String message = domain.getLastError();
				notifyError(schema.getAttributeName(i), message,domain);
				}
			}
	
		return getErrorCount()==0;
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.AbstractValidator#validateFeature(com.vividsolutions.jump.feature.Feature, com.vividsolutions.jump.workbench.model.Layer)
	 */
	public boolean validateFeature(Feature feature, ILayer layer) {
		// TODO Auto-generated method stub
		return false;
	}
}
