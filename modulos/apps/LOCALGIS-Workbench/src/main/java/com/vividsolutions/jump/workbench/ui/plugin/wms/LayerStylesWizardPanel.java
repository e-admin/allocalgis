/**
 * LayerStylesWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.wms.WMService;

public class LayerStylesWizardPanel extends JPanel implements com.geopista.ui.wizard.WizardPanel {
	public static final String STYLE_KEY = "SRS";
	private LayersStylesPanel jpSelectStyles;
	 private Map dataMap;
	 private String nextID = SRSWizardPanel.class.getName();
	
	
	public LayerStylesWizardPanel(){
		 try {
	            jbInit();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}
	
	/*
	public void add(InputChangedListener listener) {
		jpSelectStyles.add(listener);
    }

    public void remove(InputChangedListener listener) {
    	jpSelectStyles.remove(listener);
    }*/
	
	
	
	
    private void jbInit() throws Exception {
     this.setLayout(new BorderLayout());
    }



	
/**En esta tabla hash tengo entre otras cosas la lista de capas seleccionadas por el usuario
 * en la pantalla que estaba destinada a tal fin.
 */
	public void enteredFromLeft(Map dataMap) {
		jpSelectStyles=new LayersStylesPanel((WMService) dataMap.get(URLWizardPanel.SERVICE_KEY),
				dataMap);
		this.removeAll();
		add(jpSelectStyles, BorderLayout.CENTER);
		this.dataMap = dataMap;		
	}

	public void exiting() {	
	}

	public void exitingToRight() throws Exception {
		
		jpSelectStyles.saveSelectedValues();
		List list=(List) dataMap.get(MapLayerWizardPanel.COMMON_SRS_LIST_KEY);
		
	//	Para probar el OneSRSWizardPanel
	/* List list1=new LinkedList();
		list1.add(list.get(0));
		list=list1;
		dataMap.remove(MapLayerWizardPanel.COMMON_SRS_LIST_KEY);
		dataMap.put(MapLayerWizardPanel.COMMON_SRS_LIST_KEY, list);*/
		//fin probar
		
		 if (list.size() == 1) {
        nextID = OneSRSWizardPanel.class.getName();
    } else {
    	nextID = SRSWizardPanel.class.getName();
    	}
		
	}

	public String getID() {
		return getClass().getName();
	}

	public String getInstructions() {
		 return AppContext.getApplicationContext().getI18nString("PleaseChooseTheChooserLayersStyles");
	}

	public String getNextID() {
		return nextID;
	}

	public String getTitle() {
		return AppContext.getApplicationContext().getI18nString("ChooseWMSLayersStyles");
	}

	
	/**Si este método devuelve true el botón siguiente se habilita*/
	public boolean isInputValid() {
		return true;
	}



	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(WizardContext wd) {
		// TODO Auto-generated method stub
		
	}

	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}
}
