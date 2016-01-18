/*
 * Created on 09-jun-2004
 *
 */
package es.enxenio.util.controller;

import java.io.InputStreamReader;

/**
 * @author enxenio s.l.
 *
 */
public interface FrontController {

	/**Este método recuperará una acción del controlador en base a su identificador
	 * @param actionKey Clave que acción identifica a la acción del controlador
	 * @return La acción del controlador
	 */
	public Action getAction(String actionKey);
	
	/**Este método insertará en el FrontController una nueva acción del controlador
	 * @param actionKey La clave que identificará a la acción en el FrontController
	 * @param actionName El nombre completo de la clase Acción
	 */
	public void setAction(String actionKey, String actionName);
	
	/**Este método recuperará un ActionForward almacenado en el Front Controller
	 * @param forwardKey La clave que identifica al ActionForward
	 * @return El ActionForward*/
	public ActionForward getForward(String forwardKey);
	
	/**Este método añadira´en el FrontController un nuevo ActionForward
	 * @param forwardKey La clave que identificará al ActionForward en el FrontController
	 * @param actionForward El nombre del ActionForward
	 */
	public void setForward(String forwardKey, String actionForward);
	
	/**Inserta un conjunto de acciones en el controlador
	 * @param isr Origen de las acciones
	 */
	public void addActions(InputStreamReader isr);
	
	/**Inserta un conjunto de ActionForwards en el frontcontroller
	 * @param url Origen de las acciones
	 */
	public void addActionForwards(InputStreamReader isr);
	
	/**Este método borra todas las acciones*/
	public void clearActions();

	/**Este método borra todas las ActionForwards*/
	public void clearActionForwards();
}
