package com.geopista.server.licencias.workflow;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:32:02 $
 *          $Name:  $
 *          $RCSfile: CConstantesWorkflow.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CConstantesWorkflow {

    /**Dias por defecto para el silencio administrativo.
     * Lo normal es que este valor sea igual a 30.
     */
	public static long diasSilencioAdministrativo= 30;

	/** Dias para crear un evento indicando que se va a producir
     * un cambio de estado lo normal es que sea 5.
     */
	public static long diasActivacionEvento= 5;

    /** Tiempo en segundos en que se va a revisar si se ha
     * producido un cambio de evento lo normal son 300 segundos.
     */
	public static int segundosWorkflowThread=300;


}
