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
 * FormatCommandShrink.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.TextReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.util.PageSize;

import java.awt.Point;


public class FormatCommandShrink
        extends FormatCommand
{
    
    double reduction;
    String formatOneDown;
    
    FormatCommandShrink()
    {
        operationType = OperationType.SHRINK;
    }
    
    FormatCommandShrink( int operationType)
    {
        this.operationType = operationType;
        setTreatAllElements(true);
    }
    
    void preparation()
    {
        
        double marginSpace = (double) (jrf.getReport().getLeftMargin()) ;
        marginSpace += (double) (jrf.getReport().getRightMargin());
        
        int pageWidth = jrf.getReport().getWidth();
        int pageHeight = jrf.getReport().getHeight();
        formatOneDown = PageSize.findOneDown( pageWidth, pageHeight);
        
        // getMainFrame().logOnConsole("Format one Down " + formatOneDown + "\n");
        
        int newWidth = PageSize.getFormatSize(formatOneDown).x;
        
        pageWidth = jrf.getReport().getWidth() < jrf.getReport().getHeight() ?
            jrf.getReport().getWidth() : jrf.getReport().getHeight();
        reduction = ((double) newWidth - marginSpace) / ((double) pageWidth - marginSpace );
        // getMainFrame().logOnConsole("Reduction: " + reduction + "\n");
        
    }
    
    /**
     * Reduce position by calculated reduction
     * Do the same for height and width.
     * Let's try whether this work.
     */
    void modify()
    {
        // getMainFrame().logOnConsole("Modifying element " + re.toString() + "\n");
        int newWidth;
        
        double innerX = re.getPosition().x - jrf.getReport().getLeftMargin() -10  ;
        double innerY = re.getPosition().y - jrf.getReport().getBandYLocation( re.band) -10  ;
        
        innerX = innerX * reduction + jrf.getReport().getLeftMargin() + 10;
        innerY = innerY * reduction + jrf.getReport().getBandYLocation( re.band) + 10 ;
        
        // TODO: If right edge of element is equal to position of right margin, then
        // calculate the exact width based on the position of the right margin in the new format
        // In such case do not use the reduction.
        // This way small deviations due to typecasting from double to int are corrected
        // in a fashionable way.
        
        if (re.getPosition().x + re.getWidth() -10 == jrf.getReport().getWidth() - jrf.getReport().getRightMargin() )
        {
            
            int oneDownWidth = PageSize.getFormatSize(formatOneDown).x;
            int oneDownHeight = PageSize.getFormatSize(formatOneDown).y;
            //int length =
            int width = ( jrf.getReport().getWidth() < jrf.getReport().getHeight() ? oneDownWidth : oneDownHeight );
            
            // getMainFrame().logOnConsole("      innerX " +       innerX +"\n");
            // getMainFrame().logOnConsole("(int) innerX " + (int) innerX +"\n");
            
            newWidth = width - jrf.getReport().getRightMargin() - (int) innerX + 10;
            
        }
        else
        {
            newWidth = (int) ((double) re.getWidth() * reduction);
        }
        
        int newHeight = (int) ((double) re.getHeight() * reduction );
        
        re.setPosition(new Point( (int) innerX,  (int) innerY) );
        re.trasform(new Point( newWidth - re.getWidth(), newHeight - re.getHeight()), TransformationType.TRANSFORMATION_RESIZE_SE);
        
        if (re instanceof TextReportElement )
        {
            IReportFont iReportFont = ((TextReportElement)re).getIReportFont();
            int fontSize = iReportFont.getFontSize();
            // fontsize never less than 1
            fontSize = Math.max( 1, (int) (((double) fontSize ) * reduction ) );
            iReportFont.setFontSize(fontSize);
            ((TextReportElement)re).setIReportFont(iReportFont );
        }
        
    }
    
    void executeDeeper()
    {
        
        resetEnumeration();
        preparation();

        processElements();
        
        // change pageformat when all elements are selected
        // TODO: set name of format too:
        if ( jrf.getReport().getElements().size() == this.getSelectedElements().size() )
        {
            if ( !formatOneDown.equals( "Custom" ))
                
            {
                undoOp.addElement( jrf);
                
                jrf.getReport().setReportFormat( formatOneDown ) ;
                int oneDownWidth = PageSize.getFormatSize(formatOneDown).x;
                int oneDownHeight = PageSize.getFormatSize(formatOneDown).y;
                if (jrf.getReport().getOrientation().equals("Portrait") )
                {
                    jrf.getReport().setWidth(oneDownWidth);
                    jrf.getReport().setHeight(oneDownHeight);
                    jrf.getReport().recalcColumnWidth();
                    //TODO recalc columnwidth
                }
                else
                {
                    jrf.getReport().setWidth(oneDownHeight);
                    jrf.getReport().setHeight(oneDownWidth);
                    //TODO recalc columnwidth
                    jrf.getReport().recalcColumnWidth();
                }
                undoOp.captureUniqueModified(jrf);
            }
        }
        
    }
    
    
    
}
