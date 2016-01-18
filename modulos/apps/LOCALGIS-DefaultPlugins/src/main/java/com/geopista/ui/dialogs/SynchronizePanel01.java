/**
 * SynchronizePanel01.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.dialogs;

import java.awt.Image;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaMap;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SynchronizePanel01 extends JPanel implements WizardPanel
{

    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Blackboard blackboardLayers = aplicacion.getBlackboard();

    private String localId = null;

    private WizardContext wizardContext;

    private PlugInContext context;

    private String nextID = null;

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JList jList1 = null;

    private DefaultListModel layerFamiliesModel = new DefaultListModel();

    public SynchronizePanel01(String id, String nextId, PlugInContext context)
        {
            this.nextID = nextId;
            this.localId = id;
            this.context = context;

            try
            {
                jbInit();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    private void jbInit() throws Exception
    {

        this.setLayout(null);

        jScrollPane1.setBounds(new java.awt.Rectangle(65,40,350,273));

        
        

        jScrollPane1.setViewportView(getJList1());
        this.add(jScrollPane1, null);

        this.setSize(437, 339);
    }

    public void enteredFromLeft(Map dataMap)
    {
        blackboardLayers.put("SelectedSynchronizeGeopistaMap",null);
        jList1.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent e)
            {
                wizardContext.inputChanged();
            }});
        jList1.setCellRenderer(new LoadGeopistaMapRenderer());
        jList1.setModel(layerFamiliesModel);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JInternalFrame[] loaderMaps = context.getWorkbenchGuiComponent()
                .getInternalFrames();

        if (loaderMaps != null)
        {
            int thumbSizeX = Integer.parseInt(aplicacion.getString("thumbSizeX"));
            int thumbSizeY = Integer.parseInt(aplicacion.getString("thumbSizeY"));
            for (int i = 0; i < loaderMaps.length; i++)
            {
                if (loaderMaps[i] instanceof GeopistaTaskFrame)
                {
                    GeopistaMap CurrentMap = ((GeopistaMap) (((GeopistaTaskFrame) loaderMaps[i])
                            .getTaskFrame()).getTask());
                    Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
                           (LayerViewPanel) (((GeopistaTaskFrame) loaderMaps[i])
                                    .getTaskFrame()).getLayerViewPanel());
                    CurrentMap.setThumbnail(thumbnail);
                    if (CurrentMap.isSystemMap())
                    {
                        layerFamiliesModel.addElement(CurrentMap);
                    }
                }
            }
        }

    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        Object GeopistaMap = (Object) jList1.getSelectedValue();

        blackboardLayers.put("SelectedSynchronizeGeopistaMap", GeopistaMap);

    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {

    }

    public void remove(InputChangedListener listener)
    {

    }

    public String getTitle()
    {
        return aplicacion.getI18nString("SynchronizePanel01Title");
    }

    public String getID()
    {
        return localId;
    }

    public String getInstructions()
    {
        return aplicacion.getI18nString("SynchronizePanel01Instructions");
    }

    public boolean isInputValid()
    {
        Object GeopistaMap = (Object) jList1.getSelectedValue();
        if(GeopistaMap!=null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

    public String getNextID()
    {
        return nextID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub

    }
    
    /**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getJList1() {
		if (jList1 == null) {
			jList1= new JList();
		}
		return jList1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"