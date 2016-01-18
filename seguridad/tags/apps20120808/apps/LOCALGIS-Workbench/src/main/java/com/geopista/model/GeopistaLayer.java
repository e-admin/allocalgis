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
 */
package com.geopista.model;

import java.awt.Color;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.plugin.LogFeatutesEvents;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.FlexibleDateParser;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

/**
 * @author juacas
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GeopistaLayer extends Layer implements IGeopistaLayer
{
    public static ApplicationContext appContext = AppContext.getApplicationContext();

    // Contiene el nombre de la clase que implementa los Formularios Especificos
    // de la capa
    // La inicializo a String vacio porque como la serializamos como un atributo
    // de la
    // Capa en XML no puede llevar el valor null.
    private String fieldExtendedForm = "";

    private boolean activa = true;

    private String systemId = "";

    private boolean isLocal = false;
    
    private int id_layer;

    private boolean isExtracted = false;

    private LogFeatutesEvents logFeatutesEvents = null;

    private String logAbsoluteBasePath = null;
    
    private GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
    
    private WKTReader wktReader = new WKTReader(factory);
    
    private FlexibleDateParser dateParser = new FlexibleDateParser();
    
    private DateFormat dateFormatter = DateFormat.getDateInstance();
    
    private int id_LayerDataBase; //identificador de la capa en la bbdd
    
    private boolean isVersionable = false;
    private long revisionActual = -1; 
    private long ultimaRevision = -1;

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getId_LayerDataBase()
	 */
    @Override
	public int getId_LayerDataBase() {
		return id_LayerDataBase;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setId_LayerDataBase(int)
	 */
	@Override
	public void setId_LayerDataBase(int id_LayerDataBase) {
		this.id_LayerDataBase = id_LayerDataBase;
	}

	/**
     * 
     */
    public GeopistaLayer()
        {
            super();
            // TODO Auto-generated constructor stub
        }

    
    
    
    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getId_layer()
	 */
    @Override
	public int getId_layer() {
		return id_layer;
	}




	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setId_layer(int)
	 */
	@Override
	public void setId_layer(int id_layer) {
		this.id_layer = id_layer;
	}




	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#isActiva()
	 */
    @Override
	public boolean isActiva()
    {
        return activa;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setActiva(boolean)
	 */
    @Override
	public void setActiva(boolean activa)
    {
        if (this.activa == activa)
        {
            return;
        }

        // Si la capa se hace inactiva se hace también no editable
        if (activa == false)
            super.setEditable(false);

        this.activa = activa;
        fireLayerChanged(LayerEventType.METADATA_CHANGED);
    }

    /**
     * @param name
     * @param fillColor
     * @param featureCollection
     * @param layerManager
     */
    public GeopistaLayer(String name, Color fillColor,
            FeatureCollection featureCollection, ILayerManager layerManager)
        {
            super(name, fillColor, featureCollection, layerManager);
            boolean firingEvents = layerManager.isFiringEvents();
            layerManager.setFiringEvents(false);
            try
            {
                setEditable(true);
                setActiva(true);
                // setSystemId(String.valueOf(System.currentTimeMillis()));
                setSystemId(name);

                installSLDStyle();
            } finally
            {
                layerManager.setFiringEvents(firingEvents);
            }
        }

    protected void installSLDStyle()
    {
        BasicStyle bstyle = getBasicStyle();
        SLDStyle sldStyle = new SLDStyleImpl(bstyle.getFillColor(),
                bstyle.getLineColor(), getSystemId());
        
        this.removeStyle(bstyle);
        this.addStyle(sldStyle);
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getFieldExtendedForm()
	 */
    @Override
	public String getFieldExtendedForm()
    {
        return fieldExtendedForm;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setFieldExtendedForm(java.lang.String)
	 */
    @Override
	public void setFieldExtendedForm(String newFieldExtendedForm)
    {
        fieldExtendedForm = newFieldExtendedForm;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getSystemId()
	 */
    @Override
	public String getSystemId()
    {
        return systemId;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setSystemId(java.lang.String)
	 */
    @Override
	public void setSystemId(String systemId)
    {
        this.systemId = systemId;

        // <TODO Quitar este parche, solo vale para el curso>
        //if (systemId != null && systemId.equals("inmueblesurbanos"))
        //{
        //    this
        //            .setFieldExtendedForm("com.geopista.app.patrimonio.PatrimonioInmueblesExtendedForm");
        //}
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setName(java.lang.String)
	 */
    @Override
	public void setName(String name)
    {
        super.setName(name);
        if (isLocal() || systemId == null)
            setSystemId(name);
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setEditable(boolean)
	 */

    @Override
	public void setEditable(boolean editable)
    {

        // sobreescribimos el metodo para poder hacer activa la capa cuando la
        // hacemos editable
        if (this.isEditable() == editable)
        {
            return;
        }

        if (editable == true)
        {
            if (activa == false)
            {
                setActiva(true);
            }
        }

        super.setEditable(editable);
        fireLayerChanged(LayerEventType.METADATA_CHANGED);
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#isLocal()
	 */
    @Override
	public boolean isLocal()
    {
        return isLocal;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setLocal(boolean)
	 */
    @Override
	public void setLocal(boolean newIsLocal)
    {
        isLocal = newIsLocal;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#isExtracted()
	 */
    @Override
	public boolean isExtracted()
    {
        return isExtracted;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setExtracted(boolean)
	 */
    @Override
	public void setExtracted(boolean isExtracted)
    {
        this.isExtracted = isExtracted;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getLogger()
	 */
    @Override
	public LogFeatutesEvents getLogger()
    {
        return logFeatutesEvents;
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#activateLogger(com.geopista.model.GeopistaMap)
	 */
    @Override
	public void activateLogger(IGeopistaMap map) throws Exception
    {
        logFeatutesEvents = new LogFeatutesEvents(map, this);
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#activateLogger(java.lang.String)
	 */
    @Override
	public void activateLogger(String logFilePath) throws Exception
    {
        logFeatutesEvents = new LogFeatutesEvents(logFilePath,this);
    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#changeGeopistaSchema(com.vividsolutions.jump.feature.FeatureSchema)
	 */
    @Override
	public void changeGeopistaSchema(FeatureSchema newSchema) throws ConversionException
    {
        ArrayList tempFeatures = new ArrayList();
        // Two-phase commit. Phase 1: check that no conversion errors occur.
        // [Jon Aquino]
        for (Iterator i = getFeatureCollectionWrapper().getFeatures().iterator(); i
                .hasNext();)
        {
            Feature feature = (Feature) i.next();
            tempFeatures.add(convert(feature, newSchema));
        }
        
        List originalFeatures = getFeatureCollectionWrapper().getFeatures();
//      Phase 2: commit. [Jon Aquino]
        for (int i = 0; i < originalFeatures.size(); i++) {
            Feature originalFeature = (Feature) originalFeatures.get(i);
            Feature tempFeature = (Feature) tempFeatures.get(i);

            //Modify existing features rather than creating new features, because
            //there may be references to the existing features (e.g. Attribute Viewers).
            //[Jon Aquino]            
            originalFeature.setSchema(tempFeature.getSchema());
            originalFeature.setAttributes(tempFeature.getAttributes());
        }

        //Non-undoable. [Jon Aquino]
        getLayerManager().getUndoableEditReceiver().getUndoManager()
             .discardAllEdits();
        setFeatureCollection(new FeatureDataset(originalFeatures,
                newSchema));
        fireLayerChanged(LayerEventType.METADATA_CHANGED);
        

    }

    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#convert(com.vividsolutions.jump.feature.Feature, com.vividsolutions.jump.feature.FeatureSchema)
	 */
    @Override
	public Feature convert(Feature oldFeature, FeatureSchema newSchema)
            throws ConversionException
    {

        Feature newFeature = new BasicFeature(newSchema);

        for (int i = 0; i < newSchema.getAttributeCount(); i++)
        {

            try
            {
                newFeature.setAttribute(i,

                convert(oldFeature.getAttribute(oldFeature.getSchema().getAttributeIndex(
                        newSchema.getAttributeName(i))), oldFeature.getSchema().getAttributeType(
                        oldFeature.getSchema().getAttributeIndex(newSchema.getAttributeName(i))), newFeature
                        .getSchema().getAttributeType(i), newFeature.getSchema()
                        .getAttributeName(i), true));
            } catch (IllegalArgumentException e)
            {
                newFeature.setAttribute(i, null);
            }

        }

        return newFeature;
    }

    protected Object convert(Object oldValue, AttributeType oldType, AttributeType newType,
            String name, boolean forcingInvalidConversionsToNull)
            throws ConversionException
    {
        try
        {
            if (oldValue == null)
            {
                return (newType == AttributeType.GEOMETRY) ? factory
                        .createPoint((Coordinate) null) : null;
            }

            if (oldType == AttributeType.STRING)
            {
                String oldString = (String) oldValue;

                if (newType == AttributeType.STRING)
                {
                    return oldString;
                }

                if (newType == AttributeType.INTEGER)
                {
                    try
                    {
                        return new Integer(oldString);
                    } catch (NumberFormatException e)
                    {
                        throw new ConversionException("Cannot convert to integer: \""
                                + limitLength(oldValue.toString()) + "\" (" + name + ")");
                    }
                }

                if (newType == AttributeType.DOUBLE)
                {
                    try
                    {
                        return new Double(oldString);
                    } catch (NumberFormatException e)
                    {
                        throw new ConversionException("Cannot convert to double: \""
                                + limitLength(oldValue.toString()) + "\" (" + name + ")");
                    }
                }

                if (newType == AttributeType.GEOMETRY)
                {
                    try
                    {
                        return wktReader.read(oldString);
                    } catch (ParseException e)
                    {
                        throw new ConversionException("Cannot convert to geometry: \""
                                + limitLength(oldValue.toString()) + "\" (" + name + ")");
                    }
                }

                if (newType == AttributeType.DATE)
                {
                    try
                    {
                        return dateParser.parse(oldString, false);
                    } catch (java.text.ParseException e)
                    {
                        throw new ConversionException("Cannot convert to date: \""
                                + limitLength(oldValue.toString()) + "\" (" + name + ")");
                    }
                }
            }

            if (oldType == AttributeType.INTEGER)
            {
                int oldInt = ((Number) oldValue).intValue();

                if (newType == AttributeType.STRING)
                {
                    return "" + oldInt;
                }

                if (newType == AttributeType.INTEGER)
                {
                    return oldValue;
                }

                if (newType == AttributeType.DOUBLE)
                {
                    return new Double(oldInt);
                }

                if (newType == AttributeType.GEOMETRY)
                {
                    throw new ConversionException("Cannot convert to geometry: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }

                if (newType == AttributeType.DATE)
                {
                    try
                    {
                        return dateParser.parse("" + oldInt, false);
                    } catch (java.text.ParseException e)
                    {
                        throw new ConversionException("Cannot convert to date: \""
                                + limitLength(oldValue.toString()) + "\" (" + name + ")");
                    }
                }
            }

            if (oldType == AttributeType.DOUBLE)
            {
                double oldDouble = ((Double) oldValue).doubleValue();

                if (newType == AttributeType.STRING)
                {
                    return "" + oldDouble;
                }

                if (newType == AttributeType.INTEGER)
                {
                    return new Integer((int) oldDouble);
                }

                if (newType == AttributeType.DOUBLE)
                {
                    return oldValue;
                }

                if (newType == AttributeType.GEOMETRY)
                {
                    throw new ConversionException("Cannot convert to geometry: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }

                if (newType == AttributeType.DATE)
                {
                    throw new ConversionException("Cannot convert to date: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }
            }

            if (oldType == AttributeType.GEOMETRY)
            {
                Geometry oldGeometry = (Geometry) oldValue;

                if (newType == AttributeType.STRING)
                {
                    return oldGeometry.toString();
                }

                if (newType == AttributeType.INTEGER)
                {
                    throw new ConversionException("Cannot convert to integer: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }

                if (newType == AttributeType.DOUBLE)
                {
                    throw new ConversionException("Cannot convert to double: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }

                if (newType == AttributeType.GEOMETRY)
                {
                    return oldGeometry;
                }

                if (newType == AttributeType.DATE)
                {
                    throw new ConversionException("Cannot convert to date: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }
            }

            if (oldType == AttributeType.DATE)
            {
                Date oldDate = (Date) oldValue;

                if (newType == AttributeType.STRING)
                {
                    return dateFormatter.format(oldDate);
                }

                if (newType == AttributeType.INTEGER)
                {
                    return new Integer((int) oldDate.getTime());
                }

                if (newType == AttributeType.DOUBLE)
                {
                    return new Double(oldDate.getTime());
                }

                if (newType == AttributeType.GEOMETRY)
                {
                    throw new ConversionException("Cannot convert to geometry: \""
                            + limitLength(oldValue.toString()) + "\" (" + name + ")");
                }

                if (newType == AttributeType.DATE)
                {
                    return oldValue;
                }
            }

            Assert.shouldNeverReachHere(newType.toString());

            return null;
        } catch (ConversionException e)
        {
            if (forcingInvalidConversionsToNull)
            {
                return (newType == AttributeType.GEOMETRY) ? factory
                        .createPoint((Coordinate) null) : null;
            }

            throw e;
        }
    }


    
    protected String limitLength(String s) {
        //Limit length of values reported in error messages -- WKT is potentially large.
        //[Jon Aquino]
        return StringUtil.limitLength(s, 30);
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#dispose()
	 */
    @Override
	public void dispose(){
    	super.dispose();
    	systemId=null;
        factory=null;        
        wktReader = null;        
        dateParser = null;        
        dateFormatter=null;
        fieldExtendedForm=null;
    }

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#isVersionable()
	 */
	@Override
	public boolean isVersionable() {
		return isVersionable;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setVersionable(boolean)
	 */
	@Override
	public void setVersionable(boolean isVersionable) {
		super.setVersionable(isVersionable);
		this.isVersionable = isVersionable;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getRevisionActual()
	 */
	@Override
	public long getRevisionActual() {
		return revisionActual;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setRevisionActual(long)
	 */
	@Override
	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#getUltimaRevision()
	 */
	@Override
	public long getUltimaRevision() {
		return ultimaRevision;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IGeopistaLayer#setUltimaRevision(long)
	 */
	@Override
	public void setUltimaRevision(long ultimaRevision) {
		this.ultimaRevision = ultimaRevision;
	} 
}
