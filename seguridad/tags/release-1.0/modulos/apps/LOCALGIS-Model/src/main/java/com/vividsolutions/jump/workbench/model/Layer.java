/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.vividsolutions.jump.workbench.model;

import java.awt.Color;
import java.util.*;

import javax.swing.SwingUtilities;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.ui.renderer.style.*;

/**
* Adds colour, line-width, and other stylistic information to a Feature Collection.
 * <p>
 * When adding or removing multiple features to this Layer's FeatureCollection,
 * prefer #addAll and #removeAll to #add and #remove -- fewer events will be fired. 
 *
 * Una Layer encapsula a una FeatureCollection, que representa un origen
 * de datos en memoria, añadiendo otro tipo de información de interés como:
 *  -Simbología (ArrayList de Styles)
 *  -Gestión de eventos sobre el Layer (LayerListener)
 *
 *
  * The only time #fireAppearanceChanged must be called is when a party
 * modifies an attribute of one of the Styles, because Styles don't notify their
 * layer when they change. But if a party adds, removes, or applies an EditTransaction
 * to a feature, #fireAppearanceChanged will be called automatically.
 */
public class Layer extends AbstractLayerable implements LayerManagerProxy, ILayer {
    private String description = "";
    private boolean drawingLast = false;
    private FeatureCollectionWrapper featureCollectionWrapper;
    private ArrayList styles = new ArrayList();
    private boolean synchronizingLineColor = true;
    private boolean editable = false;
    private boolean dinamica = false;
    private boolean versionable = false;
    private String url = null;
    private String srid = null;
    private String validator = null;
    private LayerListener layerListener = null;
    private Blackboard blackboard = new Blackboard() {
		{
			put(FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE, true);
		}
	};
    private boolean featureCollectionModified = false;
    private DataSourceQuery dataSourceQuery;

    /**
     *  Called by Java2XML
     */
    public Layer() {
    }

    public Layer(String name, Color fillColor,
        FeatureCollection featureCollection, ILayerManager layerManager) {
        super(name, layerManager);
        Assert.isTrue(featureCollection != null);

        //Can't fire events because this Layerable hasn't been added to the
        //LayerManager yet. [Jon Aquino]
        boolean firingEvents = layerManager.isFiringEvents();
        layerManager.setFiringEvents(false);

        try {
            addStyle(new BasicStyle());
            addStyle(new SquareVertexStyle());
            addStyle(new LabelStyle());
        } finally {
            layerManager.setFiringEvents(firingEvents);
        }

        getBasicStyle().setFillColor(fillColor);
        getBasicStyle().setLineColor(defaultLineColor(fillColor));
        getBasicStyle().setAlpha(150);
        setFeatureCollection(featureCollection);        
    }

    /**
     *@return            a darker version of the given fill colour, for use as the
     *      line colour
     */
    public static Color defaultLineColor(Color fillColor) {
        return fillColor.darker();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setDescription(java.lang.String)
	 */
    @Override
	public void setDescription(String description) {
        Assert.isTrue(description != null,
            "Java2XML requires that the description be non-null. Use an empty string if necessary.");
        this.description = description;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setDrawingLast(boolean)
	 */
    @Override
	public void setDrawingLast(boolean drawingLast) {
        this.drawingLast = drawingLast;
        fireAppearanceChanged();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setFeatureCollection(com.vividsolutions.jump.feature.FeatureCollection)
	 */
    @Override
	public void setFeatureCollection(final FeatureCollection featureCollection) {
        final FeatureCollection oldFeatureCollection = featureCollectionWrapper != null
            ? featureCollectionWrapper.getUltimateWrappee()
            : createBlankFeatureCollection();
        ObservableFeatureCollection observableFeatureCollection = new ObservableFeatureCollection(featureCollection);
        observableFeatureCollection.checkNotWrappingSameClass();
        observableFeatureCollection.add(new ObservableFeatureCollection.Listener() {
                public void featuresAdded(Collection features) {
                    getLayerManager().fireFeaturesChanged(features,
                        FeatureEventType.ADDED, Layer.this);
                }

                public void featuresRemoved(Collection features) {
                    getLayerManager().fireFeaturesChanged(features,
                        FeatureEventType.DELETED, Layer.this);
                }
            });

        if ((getLayerManager() != null) &&
                getLayerManager().getLayers().contains(this)) {
            //Don't fire APPEARANCE_CHANGED immediately, to avoid the
            //following problem:
            //(1) Add fence layer
            //(2) LAYER_ADDED event will be called
            //(3) APPEARANCE_CHANGED will be fired in this method (before
            //the JTree receives its LAYER_ADDED event)
            //(4) The JTree will complain because it gets the APPEARANCE_CHANGED
            //event before the LAYER_ADDED event:
            //            java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
            //                at java.util.Vector.elementAt(Vector.java:412)
            //                at javax.swing.tree.DefaultMutableTreeNode.getChildAt(DefaultMutableTreeNode.java:226)
            //                at javax.swing.tree.VariableHeightLayoutCache.treeNodesChanged(VariableHeightLayoutCache.java:369)
            //                at javax.swing.plaf.basic.BasicTreeUI$TreeModelHandler.treeNodesChanged(BasicTreeUI.java:2339)
            //                at javax.swing.tree.DefaultTreeModel.fireTreeNodesChanged(DefaultTreeModel.java:435)
            //                at javax.swing.tree.DefaultTreeModel.nodesChanged(DefaultTreeModel.java:318)
            //                at javax.swing.tree.DefaultTreeModel.nodeChanged(DefaultTreeModel.java:251)
            //                at com.vividsolutions.jump.workbench.model.LayerTreeModel.layerChanged(LayerTreeModel.java:292)
            //[Jon Aquino]

            //Modificamos Layer para que no lance el evento de borrando de features una vez que se ha cerrado la layer
            if(getLayerManager().isFiringEvents())
            {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        //Changed APPEARANCE_CHANGED event to FEATURE_DELETED and
                        //FEATURE_ADDED events, but I think the lengthy comment above
                        //still applies. [Jon Aquino]
                        if (!oldFeatureCollection.isEmpty()) {
                            getLayerManager().fireFeaturesChanged(oldFeatureCollection.getFeatures(),
                                FeatureEventType.DELETED, Layer.this);
                        }

                        if (!featureCollection.isEmpty()) {
                            getLayerManager().fireFeaturesChanged(featureCollection.getFeatures(),
                                FeatureEventType.ADDED, Layer.this);
                        }
                    }
                });
            }
        }

        setFeatureCollectionWrapper(observableFeatureCollection);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setEditable(boolean)
	 */
    @Override
	public void setEditable(boolean editable) {
        if (this.editable == editable) {
            return;
        }

        this.editable = editable;
        fireLayerChanged(LayerEventType.METADATA_CHANGED);
    }
   

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isEditable()
	 */
    @Override
	public boolean isEditable() {
        return editable;
    }
   
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setDinamica(boolean)
	 */
    @Override
	public void setDinamica(boolean dinamica) {
        this.dinamica = dinamica;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isDinamica()
	 */
    @Override
	public boolean isDinamica() {
        return dinamica;
    }
    
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setUrl(java.lang.String)
	 */
    @Override
	public void setUrl(String url) {
        this.url = url;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getUrl()
	 */
    @Override
	public String getUrl() {
        return url;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setSynchronizingLineColor(boolean)
	 */
    @Override
	public void setSynchronizingLineColor(boolean synchronizingLineColor) {
        this.synchronizingLineColor = synchronizingLineColor;
        fireAppearanceChanged();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getBasicStyle()
	 */
    @Override
	public BasicStyle getBasicStyle() {
        return (BasicStyle) getStyle(BasicStyle.class);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getVertexStyle()
	 */
    @Override
	public VertexStyle getVertexStyle() {
        return (VertexStyle) getStyle(VertexStyle.class);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getLabelStyle()
	 */
    @Override
	public LabelStyle getLabelStyle() {
        return (LabelStyle) getStyle(LabelStyle.class);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getDescription()
	 */
    @Override
	public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getFeatureCollectionWrapper()
	 */
    @Override
	public FeatureCollectionWrapper getFeatureCollectionWrapper() {
        return featureCollectionWrapper;
    }

    protected void setFeatureCollectionWrapper(
        FeatureCollectionWrapper featureCollectionWrapper) {
        this.featureCollectionWrapper = featureCollectionWrapper;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getStyle(java.lang.Class)
	 */
    @Override
	public Style getStyle(Class c) {
        for (Iterator i = styles.iterator(); i.hasNext();) {
            Style p = (Style) i.next();

            if (c.isInstance(p)) {
                return p;
            }
        }

        return null;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getStyles()
	 */
    @Override
	public List getStyles() {
        return Collections.unmodifiableList(styles);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#hasReadableDataSource()
	 */
    @Override
	public boolean hasReadableDataSource() {
        return dataSourceQuery != null &&
        dataSourceQuery.getDataSource().isReadable();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isDrawingLast()
	 */
    @Override
	public boolean isDrawingLast() {
        return drawingLast;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isSynchronizingLineColor()
	 */
    @Override
	public boolean isSynchronizingLineColor() {
        return synchronizingLineColor;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#addStyle(com.vividsolutions.jump.workbench.ui.renderer.style.Style)
	 */
    @Override
	public void addStyle(Style style) {
        styles.add(style);
        fireAppearanceChanged();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#dispose()
	 */
    @Override
	public void dispose() {
        //Don't just call FeatureCollection#removeAll, because it may be a database
        //table, and we don't want to delete its contents! [Jon Aquino]
        setFeatureCollection(createBlankFeatureCollection());
        /*if (blackboard!=null)
        	blackboard.removeAll();
        blackboard=null;*/
    }
    
    public static FeatureCollection createBlankFeatureCollection() {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        return new FeatureDataset(featureSchema);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#removeStyle(com.vividsolutions.jump.workbench.ui.renderer.style.Style)
	 */
    @Override
	public void removeStyle(Style p) {
        Assert.isTrue(styles.remove(p));
        fireAppearanceChanged();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#cloneStyles()
	 */
    @Override
	public Collection cloneStyles() {
        ArrayList styleClones = new ArrayList();

        for (Iterator i = getStyles().iterator(); i.hasNext();) {
            Style style = (Style) i.next();
            styleClones.add(style.clone());
        }

        return styleClones;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setStyles(java.util.Collection)
	 */
    @Override
	public void setStyles(Collection newStyles) {
        boolean firingEvents = getLayerManager().isFiringEvents();
        getLayerManager().setFiringEvents(false);

        try {
            //new ArrayList to prevent ConcurrentModificationException [Jon Aquino]
            for (Iterator i = new ArrayList(getStyles()).iterator();
                    i.hasNext();) {
                Style style = (Style) i.next();
                removeStyle(style);
            }

            for (Iterator i = newStyles.iterator(); i.hasNext();) {
                Style style = (Style) i.next();
                addStyle(style);
            }
        } finally {
            getLayerManager().setFiringEvents(firingEvents);
        }

        fireAppearanceChanged();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setLayerManager(com.vividsolutions.jump.workbench.model.LayerManager)
	 */
    @Override
	public void setLayerManager(ILayerManager layerManager) {
        if (layerManager != null) {
            layerManager.removeLayerListener(getLayerListener());
        }

        super.setLayerManager(layerManager);
        layerManager.addLayerListener(getLayerListener());
    }

    private LayerListener getLayerListener() {
        //Need to create layerListener lazily because it will be called by the
        //superclass constructor. [Jon Aquino]
        if (layerListener == null) {
            layerListener = new LayerListener() {
                        public void featuresChanged(FeatureEvent e) {
                            if (e.getLayer() == Layer.this) {
                                setFeatureCollectionModified(true);

                                //Before I wasn't firing appearance-changed on an attribute
                                //change. But now with labelling and colour theming,
                                //I have to. [Jon Aquino]
                                if (e.getType() != FeatureEventType.ATTRIBUTES_MODIFIED ||
                                        getBlackboard().get(FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE, true)) {
                                    //Fixed bug above -- wasn't supplying a default value to
                                    //Blackboard#getBoolean, resulting in a NullPointerException
                                    //when the Layer was created using the parameterless
                                    //constructor (because that constructor doesn't initialize
                                    //FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE 
                                    //on the blackboard [Jon Aquino 10/21/2003]
                                    fireAppearanceChanged();
                                }
                            }
                        }

                        public void layerChanged(LayerEvent e) {
                        }

                        public void categoryChanged(CategoryEvent e) {
                        }
                    };
        }

        return layerListener;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getBlackboard()
	 */
    @Override
	public Blackboard getBlackboard() {
        return blackboard;
    }

    /**
     * Enables a layer to be changed undoably. Since the layer's features are saved,
     * only use this method for layers with few features.
     */
    public static UndoableCommand addUndo(final String layerName,
        final LayerManagerProxy proxy, final UndoableCommand wrappeeCommand) {
        return new UndoableCommand(wrappeeCommand.getName()) {
                private ILayer layer;
                private String categoryName;
                private Collection features;
                private boolean visible;

                private ILayer currentLayer() {
                    return proxy.getLayerManager().getLayer(layerName);
                }

                public void execute() {
                    layer = currentLayer();

                    if (layer != null) {
                        features = new ArrayList(layer.getFeatureCollectionWrapper()
                                                      .getFeatures());
                        categoryName = layer.getName();
                        visible = layer.isVisible();
                    }

                    wrappeeCommand.execute();
                }

                public void unexecute() {
                    wrappeeCommand.unexecute();

                    if ((layer == null) && (currentLayer() != null)) {
                        proxy.getLayerManager().remove(currentLayer());
                    }

                    if ((layer != null) && (currentLayer() == null)) {
                        proxy.getLayerManager().addLayer(categoryName, layer);
                    }

                    if (layer != null) {
                        layer.getFeatureCollectionWrapper().clear();
                        layer.getFeatureCollectionWrapper().addAll(features);
                        layer.setVisible(visible);
                    }
                }
            };
    }

    /**
     * Does nothing if the underlying feature collection is not a FeatureDataset.
     */
    public static void tryToInvalidateEnvelope(Layer layer) {
        if (layer.getFeatureCollectionWrapper().getUltimateWrappee() instanceof FeatureDataset) {
            ((FeatureDataset) layer.getFeatureCollectionWrapper()
                                   .getUltimateWrappee()).invalidateEnvelope();
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getDataSourceQuery()
	 */
    @Override
	public DataSourceQuery getDataSourceQuery() {
        return dataSourceQuery;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setDataSourceQuery(com.vividsolutions.jump.io.datasource.DataSourceQuery)
	 */
    @Override
	public Layer setDataSourceQuery(DataSourceQuery dataSourceQuery) {
        this.dataSourceQuery = dataSourceQuery;

        return this;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isFeatureCollectionModified()
	 */
    @Override
	public boolean isFeatureCollectionModified() {
        return featureCollectionModified;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setFeatureCollectionModified(boolean)
	 */
    @Override
	public Layer setFeatureCollectionModified(boolean featureCollectionModified) {
        if (this.featureCollectionModified == featureCollectionModified) {
            return this;
        }

        this.featureCollectionModified = featureCollectionModified;
        fireLayerChanged(LayerEventType.METADATA_CHANGED);

        return this;
    }

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.Layerable#getEnvelope()
	 */
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getEnvelope()
	 */
	@Override
	public Envelope getEnvelope()
	{
		
		return getFeatureCollectionWrapper().getEnvelope();
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getSrid()
	 */
	@Override
	public String getSrid() {
		return srid;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setSrid(java.lang.String)
	 */
	@Override
	public void setSrid(String srid) {
		this.srid = srid;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#isVersionable()
	 */
	@Override
	public boolean isVersionable() {
		return versionable;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setVersionable(boolean)
	 */
	@Override
	public void setVersionable(boolean versionable) {
		this.versionable = versionable;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#getValidator()
	 */
	@Override
	public String getValidator() {
		return validator;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.ILayer#setValidator(java.lang.String)
	 */
	@Override
	public void setValidator(String validator) {
		this.validator = validator;
	}
}
