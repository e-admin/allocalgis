/*
 * Created on 07-jun-2004
 *
 */
package com.geopista.style.sld.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.UserStyle;
import org.deegree.services.wfs.filterencoding.Filter;

import com.vividsolutions.jump.workbench.model.Layer;

import es.enxenio.util.exceptions.InternalErrorException;


/**
 * @author enxenio s.l.
 *
 */
public interface SLDFacade {
	
	/**Este caso de uso devolverá un userStyle a partir de un nombre de estilo
	 * @param styleName El nombre del estilo
	 * @param userStyleList La lista de UserStyles
	 * @return El UserStyle asociado a ese nombre
	 */
	public UserStyle getUserStyle(String styleName, ArrayList userStyleList);
	
	/**Este caso de uso devolverá una regla asociada a un FeatureTypeStyle
	 * @param position La posición de la regla
	 * @param ruleList La lista de reglas
	 * @return La regla deseada
	 */
	public Rule getRule(int position, ArrayList ruleList);
	
	/**Este caso de uso creará un UserStyle por defecto
	 * @return El UserStyle creado
	 */
	public UserStyle createDefaultUserStyle();
	
	/**Este caso de uso creará una Regla inicializada con los valores por defecto
	 * @return Una Regla inicializada con los valores por defecto
	 */
	public Rule createDefaultRule();
	
	/**Este caso de uso eliminará un UserStyle
	 * @param styleName El nombre del UserStyle
	 * @param userStyleList La lista de UserStyles
	 */
	public void deleteUserStyle(String styleName, ArrayList userStyleList);
	
	/**Este caso de uso eliminará una Regla
	 * @param position La posición que ocupa la Regla en la lista
	 * @param ruleList Lista de reglas
	 */
	public void deleteRule(int position, ArrayList ruleList);
	
	/**Caso de uso en el que se genera una lista de objetos ScaleRange a partir de un objeto FeatureTypeStyle
	 * @param featureTypeStyle El FeatureTypeStyle a partir del cual se crea la lista de objetos ScaleRange
	 * @return Una lista de objetos ScaleRange
	 */
	public List getScaleRangeList(FeatureTypeStyle featureTypeStyle);
	
	/**Caso de uso en el que se devuelve un objeto ScaleRange inicializado con sus valores por defecto
	 * @return El objeto ScaleRange inicilizado
	 */
	public ScaleRange createDefaultScaleRange();
	
	/**Caso de uso en el que se borra un ScaleRange de la lista de ScaleRanges
	 * @param position Posición que ocupa el objeto ScaleRange en la lista
	 * @param scaleRangeList La lista de objetos ScaleRange
	 */
	public void deleteScaleRange(int position, List scaleRangeList);
	
	/**Caso de uso en el que se recupera un objeto ScaleRange
	 * @param position Una posición en la lista de ScaleRanges
	 * @param scaleRangeList La lista de objetos ScaleRange
	 * @return Un objeto ScaleRange
	 */
	public ScaleRange getScaleRange(int position, List scaleRangeList);
	
	/**Caso de uso en el que se actualiza la definición de un ScaleRange
	 * @param minScale La escala mínima
	 * @param maxScale La escala máxima
	 * @param insert Especifica si se está creando un nuevo ScaleRange o bien se está actualizando uno ya existente
	 * @param scaleRange El objeto que va a ser actualizado
	 * @param scaleRangeList La lista de ScaleRanges existentes
	 * @return El nuevo scaleRange actualizado con los nuevos parámetros
	 */
	public ScaleRange updateScaleRange(Double minScale, Double maxScale,Integer insert,
		 ScaleRange scaleRange,List scaleRangeList);

	/**Caso de uso en el que se va a actualizar la definición de una regla
	 * @param ruleName El nombre de la regla
	 * @param scaleRange El objeto ScaleRange que se va a actualizar
	 * @param style Un HashMap en el que se encuentra codificado el simbolizador
	 * @param filter Un filtro asociado a la regla
	 * @param insert Especifica si la regla es nueva o bien si es una ya existente
	 * @param rule La regla a ser actualizada
	 * @return La regla actualizada
	 */
	public Rule updateCustomRule(String ruleName,ScaleRange scaleRange,
		HashMap style, Filter filter, Integer insert, Rule rule);	
		

	/**Caso de uso en el que se va a crear una nueva regla
	 * @param ruleName El nombre de la regla
	 * @param style Un HashMap en el que se encuentra codificado el simbolizador
	 * @param propertyName El nombre del atributo del Feature (para crear el Filtro)
	 * @param operationID El identificador del operador del filtro
	 * @param expressions Lista con una o dos expresiones para formar el filtro
	 * @param insert Especifica si la regla es nueva o bien si es una ya existente
	 * @param scaleRange La instancia de ScaleRange a modificar
	 * @return La regla actualizada
	 */
	public Rule createRule(String ruleName,
		HashMap style, String propertyName, Integer operationID,
		List expressions, Integer insert, ScaleRange scaleRange);
		
	/**Caso de uso en el que se va a actualizar la definición de un UserStyle
	 * @param styleName Nombre del estilo
	 * @param styleTitle Título del estilo
	 * @param styleAbstract Descripción del estilo
	 * @param isDefault Es estilo por defecto o no
	 * @param ftsList Lista de featureTypeStyles (en este caso sólo debe contener uno)
	 * @param scaleRangeList Lista de scaleRanges 
	 * @param insert Especifica si el UserStyle es nuevo o ya existe
	 * @param userStyle UserStyle a ser actualizado
	 * @param userStyleList Lista de userStyles existentes
	 * @return El UserStyle actualizado
	 */
	public UserStyle updateCustomUserStyle(String styleName,String styleTitle,String styleAbstract,
		Boolean isDefault,List ftsList,List scaleRangelist, Integer insert, 
		UserStyle userStyle, List userStyleList);

	/**Caso de uso que inserta en la base de datos una colección de UserStyles
	 * @param layerName El nombre de la capa
	 * @param userStyles La lista de UserStyles que se van a insertar
	 */
	public void insertUserStyles(String layerName,  List userStyles)
		throws InternalErrorException;
		
	/**Caso de uso que devuelve un HashMap en el que para cada Feature se asocian los atributos que lo forman
	 * @param layer La capa
	 * @return Un hashMap 
	 */
	public HashMap getFeatureAttributes(Layer layer);

	/**Este caso de uso actualizará el SLDStyle añadiendole la lista de UserStyles actualizada
	 * @param sldStyle El SLDStyle que se va a actualizar
	 * @param userStyleList La lista de UserStyles actualizada
	 * @return El SLDStyle actualizado*/
	public SLDStyle updateSLDStyle(SLDStyle sldStyle, ArrayList userStylesList);
	
	/**Caso de uso en el que se cambiará de posición una regla dentro de la lista que la contiene
	 * @param initialPosition Posición que ocupa la regla
	 * @param finalPosition Posición que pasará a ocupar la regla
	 * @param ruleList La lista de reglas
	 */
	public void moveRule(int initialPosition,int finalPosition,List ruleList);

}
