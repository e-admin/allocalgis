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
 * CellAnimationEffect.java
 * 
 * Created on January 24, 2006, 5:21 PM
 *
 */

package it.businesslogic.ireport.crosstab.gui;

import it.businesslogic.ireport.crosstab.CrosstabCell;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author gtoffoli
 */
public class CellAnimationEffect implements Runnable {
    
    private CrosstabEditorPanel editor = null;
    private CrosstabCell noDataCell = null;
    /** Creates a new instance of CellAnimationEffect */
    public CellAnimationEffect() {
    }

    public CrosstabEditorPanel getEditor() {
        return editor;
    }

    public void setEditor(CrosstabEditorPanel editor) {
        this.editor = editor;
    }
    
    public void run()
    {
         Graphics2D g = (Graphics2D)editor.getGraphics();
         g.setColor(new Color(255,168,0,20));
         
         
         System.out.println(editor.getZoomedDim(getNoDataCell().getTop())+","+editor.getZoomedDim(getNoDataCell().getLeft()));
         
         
         for (int j=1; j<=20; ++j)
         {
             g.setColor(new Color(255,168,0,j));
             int i=10;
             g.fillRect(editor.getZoomedDim(getNoDataCell().getLeft())-i+10, editor.getZoomedDim(getNoDataCell().getTop())-i+10,
                        editor.getZoomedDim(getNoDataCell().getWidth())+(i*2), i);

             g.fillRect(editor.getZoomedDim(getNoDataCell().getLeft())-i+10, editor.getZoomedDim(getNoDataCell().getTop())+10,
                       i, editor.getZoomedDim(getNoDataCell().getHeight()));

             g.fillRect(editor.getZoomedDim(getNoDataCell().getLeft())+10-i, editor.getZoomedDim(getNoDataCell().getTop())+editor.getZoomedDim(getNoDataCell().getHeight())+10,
                       editor.getZoomedDim(getNoDataCell().getWidth())+(i*2), i);
             
             g.fillRect(editor.getZoomedDim(getNoDataCell().getLeft())+editor.getZoomedDim(getNoDataCell().getWidth())+10, editor.getZoomedDim(getNoDataCell().getTop())+10,
                       i, editor.getZoomedDim(getNoDataCell().getHeight()));

            try {
             Thread.sleep(20);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            
            
         }
          
         getNoDataCell().setPaintSelection(true);
         getNoDataCell().drawCellBox(g,editor.getZoomFactor(),false,false);
    }

    public CrosstabCell getNoDataCell() {
        return noDataCell;
    }

    public void setNoDataCell(CrosstabCell noDataCell) {
        this.noDataCell = noDataCell;
    }
}
