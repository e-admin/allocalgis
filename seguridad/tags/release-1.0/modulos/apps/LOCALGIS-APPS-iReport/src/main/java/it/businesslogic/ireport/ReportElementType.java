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
 * ReportElementType.java
 * 
 * Created on 22 febbraio 2003, 12.33
 *
 */

package it.businesslogic.ireport;

public class ReportElementType {
    public static final int NONE = -1;
    public static final int NULL_ELEMENT = 0;
    public static final int RECTANGLE_ELEMENT = 1;
    public static final int IMAGE_ELEMENT = 2;
    public static final int STATICTEXT_ELEMENT = 3;
    public static final int TEXTFIELD_ELEMENT = 4;
    public static final int LINE_ELEMENT = 5;
    public static final int SUBREPORT_ELEMENT = 6;
    public static final int ELLIPSE_ELEMENT = 7;
    public static final int ROUND_RECTANGLE_ELEMENT = 8;
    public static final int CHART_ELEMENT = 9;
    public static final int BARCODE_ELEMENT = 10;
    public static final int FRAME_ELEMENT = 11;
    public static final int CROSSTAB_ELEMENT = 12;
    public static final int BREAK_ELEMENT = 13;
    
    // Anadido para geopista
    public static final int MAP_IMAGE_ELEMENT = 14;
}

