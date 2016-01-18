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
 * TotalPagesObject.java
 * 
 * Created on 17 settembre 2004, 19.15
 *
 */

package it.businesslogic.ireport.gui.library.objects;
import it.businesslogic.ireport.gui.library.AbstractLibraryObject;
/**
 *
 * @author  Administrator
 */
public class TotalPagesObject extends AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/page_number.png"));
    }
    
    /** Creates a new instance of PageNumberObject */
    public TotalPagesObject() {
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString("gui.library.objects.totalpages","Total pages");
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        
        getReportFrame().dropNewTextField( dtde.getLocation(), "$V{PAGE_NUMBER}",  "java.lang.Integer", "Report" );
        
    }
    
     public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
}
