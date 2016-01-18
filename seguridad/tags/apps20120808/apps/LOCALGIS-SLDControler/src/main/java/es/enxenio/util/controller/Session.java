/*
 * Created on 09-jun-2004
 *
 */
package es.enxenio.util.controller;

/**
 * @author enxenio s.l.
 *
 */
public interface Session {
	
	/**Este método recuperará el valor de un parámetro almacenado en el Mapa
	 * @param paramKey La clave que identifica al parámetro
	 * @return El objeto que contiene el valor del parámetro
	 */
	public Object getAttribute(String paramKey);
	
	/**Este método añadirá una nueva entrada al HashMap
	 * @param paramKey La clave que identificará a la nueva entrada
	 * @param paramValue El objeto que queremos almacenar en el HashMap
	 */
	public void setAttribute(String paramKey, Object paramValue);
	
	/**Este método eliminará una entrada del HashMap
	 * @param paramKey La clave de la entrada que deseamos eliminar
	 */
	public void removeAttribute(String paramKey);

	/**Este caso de uso limpiará por completo todas las variables que se encuentren en la Session
	 */
	public void clean();

}
