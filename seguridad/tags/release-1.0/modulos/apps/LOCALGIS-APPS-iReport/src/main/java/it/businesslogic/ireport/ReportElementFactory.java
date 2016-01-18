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
 * ReportElementFactory.java
 * 
 * Created on 22 July 2004, 22:55
 *
 */

package it.businesslogic.ireport;

import com.geopista.reports.gui.MapImageReportElement;

import it.businesslogic.ireport.crosstab.CrosstabCell;



/**
 *
 * @author  Robert Lamping
 */
public class ReportElementFactory {
    
    /** Creates a new instance of ElementFactory */
    public ReportElementFactory () {
    }
    
    public static ReportElement create ( int newObjectType, int originX, int originY, int width, int height) {
        // width and height can be positive and negative.
        // most elements will abs the widht and height.
        // Lines won't, because they need the direction.
        // Many ReportElement classes must all be adapted to Math.abs all incoming width and height
        
        ReportElement re = null;
        
        switch (newObjectType) {
            case ReportElementType.RECTANGLE_ELEMENT :
                re = new RectangleReportElement ( originX,originY,width,height);
                break;
            case ReportElementType.ROUND_RECTANGLE_ELEMENT:
                re = new RoundRectangleReportElement ( originX,originY,width,height);
                break;
            case  ReportElementType.ELLIPSE_ELEMENT:
                re = new EllipseReportElement ( originX,originY,width,height);
                break;
            case ReportElementType.SUBREPORT_ELEMENT:
                re = new SubReportElement ( originX,originY,width,height);
                break;
            case  ReportElementType.IMAGE_ELEMENT:
                re = new ImageReportElement ( originX,originY,width,height);
                break;
            // Anadido para geopista
            // Este elemento se corresponde con un mapa de geopista
            case  ReportElementType.MAP_IMAGE_ELEMENT:
                re = new MapImageReportElement ( originX,originY,width,height);
                break;
            case ReportElementType.CHART_ELEMENT:
                re = new ChartReportElement2 ( originX,originY,width,height);
                break;
            case  ReportElementType.STATICTEXT_ELEMENT:
                re = new StaticTextReportElement ( originX,originY,width,height);
                break;
            case ReportElementType.LINE_ELEMENT:
                re = new LineReportElement ( originX,originY, width, height);
                break;
            case ReportElementType.TEXTFIELD_ELEMENT:
                re = new TextFieldReportElement ( originX,originY,width,height);
                break;
            case ReportElementType.BARCODE_ELEMENT:
                re = new BarcodeReportElement (originX, originY, width, height);
                break;
            case ReportElementType.FRAME_ELEMENT:
                re = new FrameReportElement (originX, originY, width, height);
                break;
            case ReportElementType.CROSSTAB_ELEMENT:
                re = new CrosstabReportElement (originX, originY, width, height);
                CrosstabCell cell = new CrosstabCell();
                cell.setWidth(30);
                cell.setHeight(25);
                cell.setParent((CrosstabReportElement)re);
                cell.setType( cell.DETAIL_CELL);
                ((CrosstabReportElement)re).getCells().add(cell);
                break;
            case ReportElementType.BREAK_ELEMENT:
                re = new BreakReportElement (originX, originY, width, height);
                break;
            default:
                re = new ReportElement ( originX,originY,width,height);
        }
        return re;
    }
    
}




