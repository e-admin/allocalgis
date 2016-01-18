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
 * PageXYObject.java
 * 
 * Created on 17 settembre 2004, 19.15
 *
 */

package it.businesslogic.ireport.gui.library.objects;
import it.businesslogic.ireport.gui.library.*;
import it.businesslogic.ireport.*;

/**
 *
 * @author  Administrator
 */
public class PageXYObject extends AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/page_number.png"));
    }
    
    /** Creates a new instance of PageNumberObject */
    public PageXYObject() {
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString("gui.library.objects.pagexy","Page X of Y");
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        
        String exp = it.businesslogic.ireport.util.I18n.getString("gui.library.objects.pagexy","Page X of Y");
        String exp1 = exp.substring(0, exp.indexOf("Y"));
        String exp2 = exp.substring(exp.indexOf("Y"));
        exp1 = it.businesslogic.ireport.util.Misc.string_replace("\\\"", "\"", exp1);
        exp1 = "\"" + exp1 + "\"";
        exp1 = it.businesslogic.ireport.util.Misc.string_replace("\" + $V{PAGE_NUMBER} + \"", "X", exp1);
        
        exp2 = it.businesslogic.ireport.util.Misc.string_replace("\\\"", "\"", exp2);
        exp2 = "\"" + exp2 + "\"";
        exp2 = it.businesslogic.ireport.util.Misc.string_replace("\" + $V{PAGE_NUMBER} + \"", "Y", exp2);
        
        TextFieldReportElement re = getReportFrame().dropNewTextField( dtde.getLocation(), exp1,  "java.lang.String", "Now" );
        java.awt.Point location = dtde.getLocation();
        re.setAlign("Right");
        location.x += re.getWidth();
        getReportFrame().dropNewTextField( location, exp2,  "java.lang.String", "Report" );
        
        
        getReportFrame().addSelectedElement( re );
    }
    
     public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
}
