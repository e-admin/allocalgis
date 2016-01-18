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
 * JRCSVDataSource.java
 * 
 * Created on 4 luglio 2003, 12.10
 *
 */

package it.businesslogic.ireport.connection;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
/**
 *
 * @author  Administrator
 */
public class JRCSVDataSource implements net.sf.jasperreports.engine.JRDataSource {
    
    String row = "";
    LineNumberReader lineNumberReader;
    
    /** Creates a new instance of JRCVSDataSource */
    public JRCSVDataSource(String cvsFile) {
        try {
            lineNumberReader = new LineNumberReader( new FileReader(cvsFile));
        } catch (Exception ex) { ex.printStackTrace();}
    }
    
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jRField) throws net.sf.jasperreports.engine.JRException {
        String field = jRField.getName();
        int fieldPosition = Integer.parseInt(field.substring(7)); // Strip COLUMN_ 
        StringTokenizer st = new StringTokenizer(row,";");
        while (st.hasMoreTokens())
        {
            fieldPosition--; // The column is not 0 indexed.
            String token = st.nextToken();
            if (fieldPosition == 0) return token;
        }
        return null; // Column not found...
    }
    
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        try {
            row = lineNumberReader.readLine();
            if (row.length()>0) return true;
        } catch (Exception ex) { }
        return false;
    }
    
}
