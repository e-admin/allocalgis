/**
 * ShowEventsLogPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;


import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.geopista.ui.LogEventsTableModel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


 
public class ShowEventsLogPanel extends JPanel implements WizardPanel
{
  
  private WizardContext wizardContext;
  private HashMap events = null;
   

  public ShowEventsLogPanel(HashMap events)
  {
    try
    {
      this.events = events;
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    

  }

 
  

  public void enteredFromLeft(Map dataMap)
  {
    
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
       
      
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
      
    }

    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return this.getName();
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
    	   return "";
    	   
    }

    public boolean isInputValid()
    {
      return true;
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
   private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

  private void jbInit() throws Exception
  {
    LogEventsTableModel logEventsTableModel = new LogEventsTableModel(events);
    JTable jTable1 = new JTable(logEventsTableModel);
    jTable1.setPreferredScrollableViewportSize(new Dimension(600, 400));

    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane(jTable1);

    
    this.add(scrollPane, null);
  }




/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
}
