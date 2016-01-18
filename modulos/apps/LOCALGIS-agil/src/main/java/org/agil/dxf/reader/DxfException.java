/**
 * DxfException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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


