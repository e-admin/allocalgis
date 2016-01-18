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
 * RegistrationPlugin.java
 * 
 * Created on 16 June 2007
 *
 */

package it.businesslogic.ireport.plugin.jforg;

import it.businesslogic.ireport.plugin.checkupdate.*;
import it.businesslogic.ireport.util.Misc;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Administrator
 */
public class RegistrationPlugin extends it.businesslogic.ireport.plugin.IReportPlugin {
    
    boolean firstTime = true;
    
    
    
    /** Creates a new instance of HelloWorld */
    public RegistrationPlugin() {
        
    }
    
    public void call() {
        
        if (!it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("alreadyRegistered", "false").equals("true"))
        {
            it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().setProperty("alreadyRegistered", "true");
            SwingUtilities.invokeLater( new Runnable()
            {
            
                public void run()
                {
                    try {
                            Misc.openURL("http://www.jasperforge.org/index.php?option=com_comprofiler&task=registers&productreg=ireport&regext=1");
                        } catch (Throwable ex) {
                            ex.printStackTrace();	
                        }
                }
            });
            
       }
       firstTime = false;
    }
    
    public void configure() {
        
    }
    
}
