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
 * CSVQueryExecuter.java
 * 
 * Created on August 3, 2006, 4:30 PM
 *
 */

package it.businesslogic.ireport.examples.queryexecuter;

import java.io.StringReader;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.query.JRQueryExecuter;

/**
 *
 * @author gtoffoli
 */
public class CSVQueryExecuter implements JRQueryExecuter {
    
    String fileContent;
    
    /** Creates a new instance of CSVQueryExecuter */
    public CSVQueryExecuter(String fileContent) {
        this.fileContent = fileContent;
    }

    public JRDataSource createDatasource() throws JRException {
        
        JRCsvDataSource csvds = new JRCsvDataSource( new StringReader( fileContent ));
        //csvds.setRecordDelimiter("\n");
        //csvds.setFieldDelimiter(",");
        
        return csvds;
    }

    public void close() {
    }

    public boolean cancelQuery() throws JRException {
        return true;
    }    
}
