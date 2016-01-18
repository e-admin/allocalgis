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
 * EJBQLBeanInspectorPanel.java
 *
 * Created on December 7, 2006, 1:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.data;

import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.data.ejbql.EJBQLFieldsReader;
import it.businesslogic.ireport.gui.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author gtoffoli
 */
public class EJBQLBeanInspectorPanel extends BeanInspectorPanel {
    
    /** Creates a new instance of EJBQLBeanInspectorPanel */
    public EJBQLBeanInspectorPanel() {
        super();
        
    }
    
    /**
     * Ad hoc queryChanged method for EJBQL queries....
     */
    public void queryChanged(String newQuery) {
    
        lastExecution++;
        int thisExecution = lastExecution;
        // Execute a thread to perform the query change...
        
        String error_msg = "";
        lastExecution++;
            
        int in = lastExecution;
            
        getReportQueryDialog().getJLabelStatusSQL().setText("Executing EJBQL query....");
        /////////////////////////////
            
        try {
        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
        if (in < lastExecution) return; //Abort, new execution requested
        
        EJBQLFieldsReader ejbqlFR = new EJBQLFieldsReader(newQuery, getReportQueryDialog().getSubDataset().getParameters());
            
            try {
                Vector fields = ejbqlFR.readFields();
                
                List columns = new ArrayList();
                for (int i=0; i<fields.size(); ++i)
                {
                    JRField field = (JRField)fields.elementAt(i);
                    columns.add( new Object[]{field, field.getClassType(), field.getDescription()} );
                }
                Vector v = null;
                if (ejbqlFR.getSingleClassName() != null)
                {
                    v = new Vector();
                    v.add( ejbqlFR.getSingleClassName() );
                }
                
                System.out.println("Single class name: " + ejbqlFR.getSingleClassName());
                
                setBeanExplorerFromWorker(v,true,false);
                setColumnsFromWorker(columns);
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
                setBeanExplorerFromWorker(null,true,false);
                setColumnErrorFromWork( "Error: " +  ex.getMessage() );
            }
        
        getReportQueryDialog().getJLabelStatusSQL().setText("Ready");
    }
    
}
