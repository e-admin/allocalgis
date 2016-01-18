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
 * FormatCommandEqualsSpaceV.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;


/**
 *
 *
 */
public class FormatCommandEqualsSpaceV extends FormatCommand {
    int actualY;
    int interDistance;
    Band activeBand ;
    ReportElement fardestElement;
    Vector bands = null;
    
    {
        operationType = OperationType.EQUALS_SPACE_V;
    }
        
    boolean preCondition(){
        activeBand = (Band) ((ReportElement) this.getSelectedElements().firstElement()).getBand();
        int counter = 0;
        
        for (Iterator i = this.getSelectedElements().iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            if (re.getBand() == activeBand) {
                counter++;
            }
        }
        return ( counter > 1 );
        
    }
    
    void executeDeeper(){
        
        resetEnumeration();

        preparation();
                
        bands = getBands();
        
        for (Iterator h = bands.iterator(); h.hasNext(); ) {
            Band b = (Band) h.next();
            Vector bandElements = getBandElements(b);
            bandElements = sortYX( bandElements.elements());
            
            if (bandElements.size()>1) {
                ReportElement re = (ReportElement) bandElements.firstElement();
                actualY = re.getPosition().y + re.getHeight() + interDistance;
                
                // the highest element in the band doesn't have to be moved
                bandElements.removeElement(re);
                
                // remove bottom element as well if this band is the active band
                if (b == activeBand) {
                    bandElements.remove( bandElements.indexOf(fardestElement) );
                }
                
                processElements(bandElements.elements());
            }
            
        }
        
    }
    
    /*
     * Calculate available height
     */
    void preparation() {
        
        // calculate the average interdistance of the selected elements in the active Band
        // The active Band is the band with the first selected element.
        // The active band is set in the preCondition() method.
        
        Band b = activeBand;
        
        int counter = 0;
        int usedSize = 0;
        int minY = 0;
        int maxY = 0;
        
        ReportElement re = null;
        
        for (Iterator i = this.getSelectedElements().iterator(); i.hasNext(); ) {
            re = (ReportElement) i.next();
            
            if( re.getBand() == b){
                counter++;
                usedSize += re.getHeight();
                
                if (counter == 1) {
                    minY = re.getPosition().y;
                    maxY = re.getPosition().y + re.getHeight();
                    fardestElement = re;
                } else {
                    if (minY > re.getPosition().y ) {
                        minY = re.getPosition().y;
                    }
                    if (maxY < re.getPosition().y + re.getHeight()) {
                        maxY = re.getPosition().y + re.getHeight();
                        fardestElement = re;  // fardestElement of active Band
                    }
                }
            }
        }
        
        interDistance = ( maxY - minY - usedSize )/ (counter - 1);
        
    }
    
    public void modify() {
        re.setPosition(new Point(re.getPosition().x, actualY) );
        actualY += re.getHeight() + interDistance;
    }
    
}
