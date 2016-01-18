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
 * FormatCommandJoinRight.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 *
 */
public class FormatCommandJoinRight extends FormatCommand {
    int actual_x;
    
    {
        operationType = OperationType.JOIN_RIGHT;
    }
    
    public void executeDeeper() {
        // 1 Check which bands to treat:
        Vector used_bands = new Vector();
        
        for (Iterator h = this.getSelectedElements().iterator(); h.hasNext(); ) {
            ReportElement bandRe = (ReportElement) h.next();

            if (!used_bands.contains(bandRe.getBand())) {
                   used_bands.add( bandRe.getBand());
            } else {
                 continue;
            }

            
            //Band b = (Band)bands_enum.nextElement();
            Band b = bandRe.getBand();
            
            Vector myElements = new Vector();
            Enumeration enum2 = this.getSelectedElements().elements();
            
            while (enum2.hasMoreElements()) {
                ReportElement re = (ReportElement)enum2.nextElement();
                if (re.getBand() == b) {
                    // insert this element in the right position...
                    if (myElements.size() == 0) {
                        myElements.add(re);
                    } else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().x > re2.getPosition().x) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted){
                            myElements.addElement(re);
                        }
                        
                    }
                }
            }
            
            // Repositioning elements...
            ReportElement re = (ReportElement) myElements.elementAt(0);
            actual_x = re.getPosition().x + re.getWidth();
            
            // use myELements instead of jrf.getElements()
            processElements( myElements.elements() );
        }
    }
    
    public void modify() {
        
        re.setPosition( new Point(actual_x - re.getWidth(), re.getPosition().y) );
        actual_x -= re.getWidth();
    }
    
}
