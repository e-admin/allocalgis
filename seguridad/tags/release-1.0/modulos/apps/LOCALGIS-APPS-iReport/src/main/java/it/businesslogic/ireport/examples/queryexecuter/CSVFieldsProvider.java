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
 * CSVFieldsProvider.java
 *
 * Created on December 6, 2006, 11:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.examples.queryexecuter;

import it.businesslogic.ireport.FieldsProvider;
import it.businesslogic.ireport.FieldsProviderEditor;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.data.TestDesigner;
import it.businesslogic.ireport.gui.ReportQueryDialog;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.query.JRQueryExecuter;

/**
 *
 * @author gtoffoli
 */
public class CSVFieldsProvider implements FieldsProvider {
    
    /** Creates a new instance of CSVFieldsProvider */
    public CSVFieldsProvider() {
    }

    public JRField[] getFields(IReportConnection con, JRDataset report, Map parameters) throws JRException, UnsupportedOperationException {
    
        java.util.List list = new java.util.ArrayList();
        
        // 1. Instance the query executer....
        JRQueryExecuter executer = new CSVQueryExecuterFactory().createQueryExecuter(report, parameters);
        JRDataSource ds = executer.createDatasource();
        if (ds.next() != false)
        {
            for (int i=0; ; ++i)
            {
                JRDesignField f = new JRDesignField();
                f.setName("COLUMN_" + i);
                try {
                    ds.getFieldValue(f);
                    list.add(f);
                } catch (Exception ex)
                {
                    //ex.printStackTrace();
                    break;
                }
            }
        }
        
        JRField[] fields = new JRField[list.size()];
        for (int i=0; i<fields.length; ++i)
        {
            fields[i] = (JRField)list.get(i);
        }
        
        return fields;
    
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return true;
    }

    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        // Start FREE QUERY BUILDER....
        TestDesigner td = new TestDesigner(reportQueryDialog, true);
        td.setQuery(query);
        td.setVisible(true);
        if (td.getDialogResult() == JOptionPane.OK_OPTION)
        {
            return td.getQuery();
        }
        return null;
    }

    public boolean supportsGetFieldsOperation() {
        return true;
    }

    public boolean hasEditorComponent() {
        return false;
    }

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
        return null;
    }
    
}
