package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.workbench.WorkbenchException;
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
