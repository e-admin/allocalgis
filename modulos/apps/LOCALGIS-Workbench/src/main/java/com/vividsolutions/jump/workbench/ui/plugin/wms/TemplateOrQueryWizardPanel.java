/**
 * TemplateOrQueryWizardPanel.java
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

public class TemplateOrQueryWizardPanel extends JPanel implements com.geopista.ui.wizard.WizardPanel {
	 private Map dataMap;
	 private TemplateOrQueryPanel templateOrQueryPanel;
	 //lista de plantillas
	 List wmsTemplates;
	 
	 //elecciones del usuario
	public static final String TEMPLATE_ID="templateId";
	public static final String IS_NEW_QUERY="isNewQuery";
	 
	 
	 public TemplateOrQueryWizardPanel(List wmsTemplates){
		 this.wmsTemplates=wmsTemplates;
	 }//fin constructor
	
	
	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**Método que se ejecuta a la entrada del panel
	 */
	public void enteredFromLeft(Map dataMap) {
		templateOrQueryPanel=new TemplateOrQueryPanel(wmsTemplates);
		add(templateOrQueryPanel, BorderLayout.CENTER);
		this.dataMap=dataMap;
	}

	public void exiting() {
		// TODO Auto-generated method stub
		
	}

	/**Método que se ejecuta a la salida del panel 
	 * (al pulsar siguiente)
	 */
	public void exitingToRight() throws Exception {
		dataMap.put(IS_NEW_QUERY, new Boolean(templateOrQueryPanel.isNewQuery()));
		if(!templateOrQueryPanel.isNewQuery())
			dataMap.put(TEMPLATE_ID, new Integer(templateOrQueryPanel.getTemplateId()));
	}//fin método exitingToRight

	public String getID() {
		 return getClass().getName();
	}

	public String getInstructions() {
		return AppContext.getApplicationContext().getI18nString("templateOrQueryWizarPanel.instructions");		
	}

	public String getNextID() {
	  if(templateOrQueryPanel!=null)
		if(!templateOrQueryPanel.isNewQuery())
			 return null;
	  return URLWizardPanel.class.getName();		
	}

	public String getTitle() {
		return AppContext.getApplicationContext().getI18nString("templateOrQueryWizarPanel.title");		
	}

	public boolean isInputValid() {
		return true;
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(WizardContext wd) {
		// TODO Auto-generated method stub
		
	}

}
