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
 * PercentageObject.java
 * 
 * Created on 17 settembre 2004, 19.15
 *
 */

package it.businesslogic.ireport.gui.library.objects;
import it.businesslogic.ireport.gui.library.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import java.util.Vector;
/**
 *
 * @author  Administrator
 */
public class PercentageObject extends AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/percentage.png"));
    }
    
    /** Creates a new instance of PageNumberObject */
    public PercentageObject() {
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString("gui.library.objects.percentage","Percentage");
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        
        
        // We are in a band?
        
        PercentageObjectDialog tod = new PercentageObjectDialog(getReportFrame().getMainFrame(), true );
        tod.setFields( getReportFrame().getReport().getFields() );
        Vector resetTypes = new Vector();
        resetTypes.add("Report");
        resetTypes.add("Page");
        resetTypes.add("Column");
        resetTypes.addAll(getReportFrame().getReport().getGroups() );
        tod.setResetTypes(resetTypes);
        
        tod.setVisible(true);
        if (tod.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
          JRField obj = tod.getField();
          
          String clazz = "java.lang.Integer";
          String expression = "";
          String tot_name ="";
          
          clazz = obj.getClassType();
          expression = "$F{" + obj + "}";
          tot_name += obj +"_";
          
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
          
          time = tod.getResetType();
          if (!time.equals("Report") &&
              !time.equals("Page") &&
              !time.equals("Column"))
          {
            time = "Group";
            jrv.setResetGroup( tod.getResetType() );
          }
          jrv.setResetType( time );
          jrv.setCalculation("Sum");
          getReportFrame().getReport().addVariable( jrv );
          
          String field_exp = "$F{" + obj + "}.doubleValue()";
          String var_name_exp = "$V{" + var_name + "}.doubleValue()";
          
          TextFieldReportElement re = getReportFrame().dropNewTextField( dtde.getLocation(), "new Double(" + field_exp + " / " + var_name_exp + ")",  "java.lang.Double", "Auto" );
          re.setPattern("#,##0.00 %");
          getReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getReportFrame(), re, ReportElementChangedEvent.CHANGED));
        }
    }
    
     public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
}
