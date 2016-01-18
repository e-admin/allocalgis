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
 * JRSQLExpressionArea.java
 * 
 * Created on 28 aprile 2003, 22.55
 *
 */

package it.businesslogic.ireport.gui;
import org.syntax.jedit.*;

/**
 *
 * @author  Administrator
 */
public class JRSQLExpressionArea extends JEditTextArea {
    
    private int spessore = 0;
    protected boolean viewScrollbars = true;
    
    /** Creates a new instance of JRTextExpressionArea */
    public JRSQLExpressionArea() {
        super();
        spessore = vertical.getWidth();
        super.painter.setLineHighlightColor(java.awt.Color.WHITE);
        this.setDocument(new SyntaxDocument());
        this.setTokenMarker(new org.syntax.jedit.tokenmarker.TSQLTokenMarker());                     
    }
    
    /** Getter for property viewScrollbars.
     * @return Value of property viewScrollbars.
     *
     */
    public boolean isViewScrollbars() {
        return viewScrollbars;
    }
    
    /** Setter for property viewScrollbars.
     * @param viewScrollbars New value of property viewScrollbars.
     *
     */
    public void setViewScrollbars(boolean viewScrollbars) {
        
         //System.out.println("Ops"+viewScrollbars);
        //if (this.viewScrollbars == viewScrollbars) return;
        if (!viewScrollbars)
        {
            //super.vertical.setVisible(false);
            //super.horizontal.setVisible(false);
            
            //super.vertical.setSize(0,0);
        }
        else
        {
            //super.vertical.setVisible(true);
            //super.horizontal.setVisible(true);
            //super.vertical.setSize(spessore,  super.vertical.getHeight());
            //super.horizontal.setSize(spessore,  super.horizontal.getWidth());
        }
        this.viewScrollbars = viewScrollbars;
    }
    

    public void setEnabled(boolean enabled)
    {
        if (super.isEnabled() == enabled) return;
        super.setEnabled(enabled);
        if (!super.isEnabled())
        {
            for (int i=0; i<this.getComponentCount(); ++i)
                this.getComponent(i).setBackground(java.awt.Color.LIGHT_GRAY);
            this.setCaretVisible(false);
           this.getPainter().setLineHighlightEnabled(false);
           
            
        }
        else
        {
            for (int i=0; i<this.getComponentCount(); ++i)
                this.getComponent(i).setBackground(java.awt.Color.WHITE);
            this.setCaretVisible(true);
            this.getPainter().setLineHighlightEnabled(true);
        }  
    }
    
}
