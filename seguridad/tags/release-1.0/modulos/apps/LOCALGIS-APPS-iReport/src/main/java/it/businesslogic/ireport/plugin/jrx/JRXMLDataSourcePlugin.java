/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * JRXMLDataSourcePlugin.java
 *
 * Created on March 28, 2007, 11:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.plugin.jrx;

import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.queryexecuters.QueryExecuterDef;
import it.businesslogic.ireport.plugin.IReportPlugin;

/**
 *
 * @author gtoffoli
 */
public class JRXMLDataSourcePlugin extends IReportPlugin {
    
    /**
     * Creates a new instance of JRXMLDataSourcePlugin
     */
    public JRXMLDataSourcePlugin() {
        MainFrame.getMainInstance().addConnectionImplementation("it.businesslogic.ireport.plugin.jrx.JRXMLDataSourceConnection");
        QueryExecuterDef qed = new QueryExecuterDef("xpath2","com.jaspersoft.jrx.query.JRXPathQueryExecuterFactory");
        MainFrame.getMainInstance().addQueryExecuterDef(qed, true);
    }

    public void call() {
    }
    
}
