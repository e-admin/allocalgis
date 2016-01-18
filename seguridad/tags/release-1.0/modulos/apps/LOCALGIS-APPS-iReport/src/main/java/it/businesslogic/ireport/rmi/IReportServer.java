/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * IReportServer.java
 * 
 */

package it.businesslogic.ireport.rmi;

import java.io.File;
import java.rmi.*;

public interface IReportServer extends Remote
{
    /**
   * Used to check if iReport is alive
   */
  public boolean ping() throws RemoteException;
  
   /**
   * Used to show the main window and bring the iReport window on top...
   */
  public boolean setVisible(boolean b) throws RemoteException;
  
  /**
   * Open the file passed as parameter...
   */
  public boolean openFile(String file) throws RemoteException;
  
  public boolean runWizard(String destFile) throws RemoteException;
}

