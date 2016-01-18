package es.satec.localgismobile.fw.net.communications.exceptions;

/**
 *
 * User: dbenito; XSDDate: 18-mar-2004 Time: 16:52:00
 * To change this template use Options | File Templates.
 *
 * TAGS CVS
 * @author  SATEC
 * @version $Revision: 1.1 $
 *
 * Autor:$Author: satec $
 * Fecha Ultima Modificacion:$XSDDate: 2005/09/12 16:21:15 $
 * $Name:  $ ; $RCSfile: TimeOutException.java,v $ ; $Revision: 1.1 $ ; $Locker:  $
 * $State: Exp $
 */
public class TimeOutException extends Exception
{
	//Para que el CVS guarde la revisión y así decompilar la clase en producción.
	private String Version = "$Revision: 1.1 $";

	public TimeOutException(int nSeg){super("Ha saltado el TimeOut. Segundos: "+nSeg);}
}