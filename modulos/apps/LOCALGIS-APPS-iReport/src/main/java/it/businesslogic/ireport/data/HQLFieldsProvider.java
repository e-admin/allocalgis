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
 * SQLFieldsProvider.java
 *
 * Created on December 7, 2006, 9:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.data;

import it.businesslogic.ireport.FieldsProvider;
import it.businesslogic.ireport.FieldsProviderEditor;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.gui.ReportQueryDialog;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author gtoffoli
 */
public class HQLFieldsProvider implements FieldsProvider {
    
    private BeanInspectorPanel bip = null;
    
    /** Creates a new instance of SQLFieldsProvider */
    public HQLFieldsProvider() {
    }

    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation() {
        return false;
    }
    
    public JRField[] getFields(IReportConnection con, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        return null;
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return false;
    }

    public boolean hasEditorComponent() {
        return true;
    }

    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        return null;
    }

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
        
        if (bip == null)
        {
            bip = new BeanInspectorPanel();
            bip.setReportQueryDialog(reportQueryDialog);
            if (reportQueryDialog != null)
            {
                bip.setJTableFields( reportQueryDialog.getFieldsTable() );
            }
        }
        return bip;
    }
    
}
