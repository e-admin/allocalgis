/**
 * IAdministradorCartografiaClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 03-jun-2005 by juacas
 *
 * 
 */
package com.geopista.server.administradorCartografia;

import java.util.Collection;
import java.util.List;

import com.geopista.model.IGeopistaLayer;
import com.geopista.model.IGeopistaMap;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface IAdministradorCartografiaClient
{
	public static final int	LOCK_LAYER_ERROR			= -1;

	public static final int	LOCK_LAYER_LOCKED			= 0;

	public static final int	LOCK_LAYER_OWN				= 1;

	public static final int	LOCK_LAYER_OTHER			= 2;

	public static final int	UNLOCK_LAYER_ERROR			= -1;

	public static final int	UNLOCK_LAYER_UNLOCKED		= 0;

	public static final int	UNLOCK_LAYER_NOTLOCKED		= 3;

	public static final int	LOCK_FEATURE_ERROR			= -1;

	public static final int	LOCK_FEATURE_LOCKED			= 0;

	public static final int	LOCK_FEATURE_OWN			= 4;

	public static final int	LOCK_FEATURE_OTHER			= 5;

	public static final int	UNLOCK_FEATURE_ERROR		= -1;

	public static final int	UNLOCK_FEATURE_UNLOCKED		= 0;

	public static final int	UNLOCK_FEATURE_NOTLOCKED	= 6;

	public static final int	MAX_NUM_FEATURE_UPLOAD		= 100;

	/** Carga la lista de mapas del Administrador de Cartografia */
	public abstract Collection getMaps(String sLocale);

	/** Carga de un mapa del administrador de cartografia */
	public abstract IGeopistaMap loadMap(IGeopistaMap map, String sLocale,
			Geometry g, FilterNode fn,TaskMonitor monitor) throws ACException, Exception;

	/** Bloquea y carga una region de una capa en el administrador de cartografia, aplicando
	 *  un filtro adicional.
	 */
	public abstract IGeopistaLayer extractLayer(String sLayer, String sLocale,
			Geometry g, FilterNode fn) throws LockException, ACException,
			NoIDException,CancelException;

	public abstract IGeopistaLayer loadLayer(String sLayer, String sLocale,
			Geometry g, FilterNode fn) throws ACException, NoIDException,CancelException;

	/** Carga de una capa del administrador de cartografia con filtros geometricos y alfanumerico. */
	public abstract IGeopistaLayer loadLayer(IGeopistaMap gpMap, String sLayer,
			String sLocale, Geometry g, FilterNode fn) throws ACException,
			NoIDException,CancelException;
	
	/** Carga de una capa del administrador de cartografia con filtros geometricos y alfanumerico. */
    public abstract IGeopistaLayer loadLayer(IGeopistaMap gpMap, String sLayer,
            String sLocale, Geometry g, FilterNode fn, Integer sridDestino) throws ACException,
            NoIDException,CancelException;
    
    public abstract IGeopistaLayer loadLayer(IGeopistaMap gpMap, String sLayer,
            String sLocale, Geometry g, FilterNode fn, Integer sridDestino,TaskMonitor monitor) throws ACException,
            NoIDException,CancelException;
    
    public abstract IGeopistaLayer loadLayer(IGeopistaMap gpMap, IGeopistaLayer sLayer,
            String sLocale, Geometry g, FilterNode fn, boolean bValidateDate, Integer sridDestino) throws ACException,
            NoIDException,CancelException;

	/** Bloquea una capa del administrador de cartografia
	 *  */
	public abstract int lockLayer(String sLayer, Geometry g)
			throws LockException, ACException;

	/** Desbloquea una capa del administrador */
	public abstract int unlockLayer(String sLayer) throws ACException;

	/** Devuelve una lista de features de una layer cuando se le pasan los ids 
	 * @throws Exception*/
	public List loadFeatures(IGeopistaLayer layer, List featureIds, String sLocale, Integer sridDestino)
			throws Exception;

	/** Bloquea una lista de features del administrador de cartografia 
	 * @throws Exception*/
	public FeatureLockResult lockFeature(List layers, List featureIds)
			throws Exception;

    public String getSRIDDefecto(boolean defecto, int idEntidad) throws Exception;
    	
    
	/* Desbloqueo de una lista de features */
	public abstract List unlockFeature(
			List layers, List features) throws Exception;

	/** Guarda en el servidor un array de features */
	public abstract Collection uploadFeatures(String sLocale,
			Object[] aGeopistaFeatures) throws ACException, NoIDException;

	/** Guarda en el servidor un array de features */
	public abstract Collection uploadFeatures(String sLocale,
			Object[] sourceGeopistaFeatures, boolean bLoadData,
			TaskMonitor monitor) throws ACException, NoIDException;
	
	/** Guarda en el servidor un array de features */
	public abstract Collection uploadFeatures(String sLocale,
			Object[] sourceGeopistaFeatures, boolean bLoadData, boolean bValidateData,
			TaskMonitor monitor) throws ACException, NoIDException;

	   /** Guarda en el servidor un array de features */
    public abstract Collection uploadFeatures(String sLocale,
            Object[] sourceGeopistaFeatures, boolean bLoadData, boolean bValidateData,
            TaskMonitor monitor, Integer srid_destino) throws ACException, NoIDException;
    
 
	/** Guarda un estilo en la BD (puede afectar a mas de una capa) */
	public abstract void uploadStyle(IGeopistaLayer gLayer) throws ACException;

	/** Actualiza los features modificados, agregados o borrados de una capa y la
	 desbloquea. Devuelve los features insertados */
	public abstract Collection synchronize(IGeopistaLayer gLayer,
			String sLocale, Geometry g) throws ACException, LockException,
			NoIDException;

	/** Carga de los IDs de features modificados de una capa */
	public abstract int[] getModifiedFeatureIDs(int iLayer, long lDate);

	/** Carga de los IDs de las layerfamilies de un mapa */
	public abstract int[] getLayerFamilyIDs(IGeopistaMap map);

	/** Carga de los IDs de las layerfamilies de un mapa */
	public abstract int[] getLayerFamilyIDs();

	public abstract IACLayerFamily[] getLayerFamilies();

	/** Carga de los IDs de layers de una layerfamily */
	public abstract String[] getLayerIDs(IGeopistaMap map,
			int iLayerFamilyPosition) throws ACException, Exception;

	/** Carga de los IDs de layers de una layerfamily */
	public abstract String[] getLayerIDs(int iLayerFamilyID) throws ACException, Exception;

	/** Asigna el ID de mapa devuelto en caso de insercion */
	public abstract IGeopistaMap saveMap(IGeopistaMap map, String sLocale)
			throws ACException;

	public abstract void deleteMap(IGeopistaMap map, String sLocale) throws ACException;


    //Devuelve la posicion de una dirección postal
    public abstract FeatureCollection getGeoRefAddress(String tipoVia, String via, String num_poli, String sLocale);
    
    //Devuelve la geometría que tiene un determinado municipio
    public abstract Geometry obtenerGeometriaMunicipio(int codMunicipio);


	/****** METODOS MODIFICADOS DE EnviarSeguro *****/
	/* TODO: Añadirlos al EnviarSeguro de verdad */
	public static final String	mensajeXML	= "mensajeXML";

	public static final String	idMunicipio	= "idMunicipio";

	public static final String	IdApp		= "IdApp";

}