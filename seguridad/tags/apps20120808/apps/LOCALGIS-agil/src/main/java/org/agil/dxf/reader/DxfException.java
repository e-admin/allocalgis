/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */

package org.agil.dxf.reader;


/** 
 *  Generic dedicated Exception class using resourced messages.
 *  
 *  @version   1.00beta0
 */
public class DxfException extends Exception 
{

  String tag;
  String args[] = null;
  
  /**
   *  Create an exception with the message for the given
   *  resource tag.
   *  @param  tag   resource tag
   */
  public DxfException(String tag) {
    super(tag);
	this.tag = tag;
  }

  /**
   *  Create an exception with the message for the given
   *  resource tag with the given parameters.
   *  @param  tag   resource tag
   *  @param  args  sadditional parameters
   */
  public DxfException(String tag, String[] args) {
  	super(tag);
	this.tag = tag;
	this.args = args;	
  }
  
  
  public String getMessage() 
  {
    String msg = tag + " : ";
	if (args != null)
	{
  	  for (int i = 0; i < args.length; i++)
	    msg = msg + (args[i] + " / "); 
	}	
    return msg;
  }
  
}


