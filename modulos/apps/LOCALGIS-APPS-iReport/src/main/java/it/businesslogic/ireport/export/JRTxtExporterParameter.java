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
 * JRTxtExporterParameter.java
 * 
 * Created on 16 aprile 2004, 10.28
 *
 */

package it.businesslogic.ireport.export;

import net.sf.jasperreports.engine.JRExporterParameter;

/**
 *
 * @author  Giulio Toffoli
 */
public class JRTxtExporterParameter extends JRExporterParameter
{
	/**
	 *
	 */
	protected JRTxtExporterParameter(String name)
	{
		super(name);
                
	}

	public static final JRTxtExporterParameter PAGE_ROWS = new JRTxtExporterParameter("Rows per page");
        public static final JRTxtExporterParameter PAGE_COLUMNS = new JRTxtExporterParameter("Columns per page");  
        public static final JRTxtExporterParameter ADD_FORM_FEED = new JRTxtExporterParameter("Add FORM FEED");
}
