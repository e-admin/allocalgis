/*
 * Created on 27-sep-2005 by juacas
 */
package com.geopista.feature;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.*;

public class SearchableFeatureCollection extends FeatureCollectionWrapper
{
	private static final String	SYSTEMID	= "SearchableFeatureCollection.SystemId";
    private Hashtable	indexCache	= new Hashtable();
    private Hashtable	indexCacheMulti	= new Hashtable();
    private FeatureSchema featureSchema=null;
	/**
	 * Crea un FeatureCollection con posibilidades de buscar por ciertos
	 * atributos. Inicialmente no contiene indices. Se generan en su primer uso
	 * o al llamar a las funciones de inicializacion.
	 * 
	 * @param fc
	 */
	public SearchableFeatureCollection(FeatureCollection fc)
	{
	super(fc);

	}

    /**
         * Crea un FeatureCollection con posibilidades de buscar por ciertos
         * atributos. Inicialmente no contiene indices. Se generan en su primer uso
         * o al llamar a las funciones de inicializacion.
         *
         * @param fc
         * @param fs
         */
        public SearchableFeatureCollection(FeatureCollection fc, FeatureSchema fs)
        {
            super(fc);
            featureSchema=fs;
        }

	/**
	 * No soportado.
	 * 
	 */
	public void add(Feature feature)
	{
	throw new UnsupportedOperationException("Index cannot be modified");
	}

	/**
	 * No soportado.
	 * 
	 */
	public void remove(Feature feature)
	{
	throw new UnsupportedOperationException("Index cannot be modified");
	}
	TreeMap	map;

	/**
	 * Search for a specific value on collection
	 * @param att
	 * @param val
	 * @return
	 */
	public Feature query(String att, Object val)
	{
	return (Feature) getIndex(att).get(val);
	}
    /**
     * Devuelve una colección de features que cumplen la
     * condicion att==val
     * @param att atributo buscado
     * @param val valor buscado
     * @return
     */
    public FeatureCollection queryMulti(String att, Object val)
	{
	    return (FeatureCollection) getIndexMulti(att).get(val);
	}
	public Feature query(String sysId)
	{
	return (Feature)getIndex(SearchableFeatureCollection.SYSTEMID).get(sysId);
	}

	/**
	 * manages indexes for fast searches
	 * Stores indexes for different attributes.
	 * SearchableFeatureCollection.SYSTEMID is a reserved attributeName that identifies the primaryKey of a GeopistaFeature
	 * @see SearchableFeatureCollection.SYSTEMID
	 * @param att
	 * @return
	 */
	private Map getIndex(String att)
	{
	Map map = (Map) indexCache.get(att);
	if (map == null) // makes index
		{
		map = new TreeMap();
		indexCache.put(att, map);
		List featuresToIndex = getFeatures();
		for (Iterator feature = featuresToIndex.iterator(); feature.hasNext();)
			{
			Feature element = (Feature) feature.next();
			if (element instanceof GeopistaFeature && SYSTEMID.equals(att))
			    map.put( ((GeopistaFeature)element).getSystemId(),element);
			else if (element.getAttribute(att)!=null) //aso añade porque falla
                map.put(element.getAttribute(att), element);
			}

		}
	return map;
	}

   /**
    * Devuelve un Map con el atributo buscado
    * el valor de este mapa serán la features cuyo
    * atributo sea igual
    * @param att
    * @return
    */
	private Map getIndexMulti(String att )
	{
	    Map map = (Map) indexCacheMulti.get(att);
	    if (map == null) // makes index
		{
		    map = new TreeMap();
		    indexCacheMulti.put(att, map);
		    List featuresToIndex = getFeatures();
		    for (Iterator feature = featuresToIndex.iterator(); feature.hasNext();)
			{
			    Feature element = (Feature) feature.next();
			    if (element instanceof GeopistaFeature && SYSTEMID.equals(att)) //lo normal es que no se haga una busqueda multi para SystemID
                {
                    FeatureCollection fc=(FeatureCollection) map.get(((GeopistaFeature)element).getSystemId());
                    if (fc==null) fc=new FeatureDataset(featureSchema);
                    fc.add(element);
                    map.put(((GeopistaFeature)element).getSystemId(), fc);
                }
			    else if (element.getAttribute(att)!=null)
                {
                    FeatureCollection fc=(FeatureCollection) map.get(element.getAttribute(att));
                    if (fc==null) fc=new FeatureDataset(featureSchema);
                    fc.add(element);
                    map.put(element.getAttribute(att), fc);
                }
			}

		}
	    return map;
	}
	/**
	 * No soportado.
	 * 
	 */
	public void addAll(Collection features)
	{
	throw new UnsupportedOperationException("Index cannot be modified");
	}

	/**
	 * No soportado.
	 * 
	 */
	public Collection remove(Envelope env)
	{
	throw new UnsupportedOperationException("Index cannot be modified");
	}

	/**
	 * No soportado.
	 * 
	 */
	public void removeAll(Collection features)
	{
	throw new UnsupportedOperationException("Index cannot be modified");
	}
}
