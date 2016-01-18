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
 * TotalObject.java
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
public class TotalObject extends AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/total.png"));
    }
    
    /** Creates a new instance of PageNumberObject */
    public TotalObject() {
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString("gui.library.objects.total","Total");
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        
        TotalObjectDialog tod = new TotalObjectDialog(getReportFrame().getMainFrame(), true );
        tod.setJrf( getReportFrame() );
        tod.setVisible(true);
        if (tod.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
          Object obj = tod.getSelectedObject();
          if (obj == null) return;
          // 1. We must create the variable....
          String clazz = "java.lang.Integer";
          String expression = "";
          String tot_name ="";
          if (obj instanceof JRField)
          {
              clazz = ((JRField)obj).getClassType();
              expression = "$F{" + obj + "}";
              tot_name += obj +"_";
          }
          else if (obj instanceof JRParameter)
          {
              clazz = ((JRParameter)obj).getClassType();
              expression = "$P{" + obj + "}";
              tot_name += obj +"_";
          }
          else if (obj instanceof JRVariable)
          {
              clazz = ((JRVariable)obj).getClassType();
              expression = "$V{" + obj + "}";
              tot_name += obj +"_";
          }
          else
          {
              expression = "" + obj;
          }
          
          String var_name = "SUM_" + tot_name;
          String time = "Report";
          String tmp_name = var_name;
          // 1. Find the first free var name...
          java.util.Vector variables = getReportFrame().getReport().getVariables();
          for (int i=1; ; ++i)
          {
              var_name = tmp_name + i;
              boolean found = false;
              for (int k=0; k<variables.size(); ++k)
              {
                  if ( ((JRVariable)variables.get(k)).getName().equals(var_name))
                  {
                      found = true;
                      break;
                  }
              }
              if (!found) break;
          }
          
          // 2. Find the future band...
          Band b = getReportFrame().getBandAt(dtde.getLocation());
          JRVariable jrv = new JRVariable(var_name,false);
          jrv.setClassType( clazz );
          jrv.setExpression( expression );
          
          if (b.getName().equals("pageFooter"))
          {
              time = "Page";
          }
          else if (b.getName().equals("lastPageFooter"))
          {
              time = "Report";
          }
          else if (b.getName().equals("columnFooter"))
          {
              time = "Column";
          }
          else if (b.getName().endsWith("Footer"))
          {
              time = "Group";
              jrv.setResetGroup( b.getName().substring(0, b.getName().length() - "Footer".length() ));
          }
          
          jrv.setResetType( time );
          jrv.setCalculation("Sum");
          getReportFrame().getReport().addVariable( jrv );
          
          getReportFrame().dropNewTextField( dtde.getLocation(), "$V{" + var_name + "}",  clazz, "Now" );
        }
    }
    
     public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
}
