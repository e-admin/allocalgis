/*
 * Created on 09-jun-2004
 *
 */
package es.enxenio.util.controller;

/**
 * @author enxenio s.l.
 *
 */
public interface Action {
	
	/**Este método realizará una operación determinada basándose en los parámetros almacenados en un objeto Request
	 * @param request Instancia que contendrá los parámetros necesarios para llevar a cabo la operación
	 * @return Un ActionForward que nos indica la siguiente acción a realizar*/
	public ActionForward doExecute(Request request);

}
