/**
 * printPanel03.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;
import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**

 */

public class printPanel03 extends javax.swing.JPanel implements WizardPanel{

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext	context;

	private String nextID=null;
	private String localId = null;

	private JLabel mensaje = new JLabel();
	private JPanel panel= new JPanel();
	

	public printPanel03(String id, String nextId, PlugInContext context2) {
		this.context=context2;
		this.nextID = nextId;
		this.localId = id;

		setName((UtilsPrintPlugin.getMessageI18N("PrintPanel03.Name"))); //$NON-NLS-1$
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 * Auto-generated code - any changes you make will disappear.
	 */
	public void initGUI(){
		try {
			preInitGUI();

			panel.removeAll();
			mensaje.setText(UtilsPrintPlugin.getMessageI18N("PrintPanel03.ElegirPlantilla"));	
			mensaje.setBounds(100, 100, 100, 100);
			panel.add(mensaje, null);
			this.add(panel, null);

			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Add your pre-init code in here 	*/
	public void preInitGUI(){
	}

	/** Add your post-init code in here 	*/
	public void postInitGUI(){
	}


	/** Auto-generated main method */
	public static void main(String[] args){
		showGUI();
	}

	/**
	 * This static method creates a new instance of this class and shows
	 * it inside a new JFrame, (unless it is already a JFrame).
	 *
	 * It is a convenience method for showing the GUI, but it can be
	 * copied and used as a basis for your own code.	*
	 * It is auto-generated code - the body of this method will be
	 * re-generated after any changes are made to the GUI.
	 * However, if you delete this method it will not be re-created.	*/
	public static void showGUI(){
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			printPanel03 inst = new printPanel03(null,null,null);
			frame.setContentPane(inst);
			frame.getContentPane().setSize(inst.getSize());
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enteredFromLeft(Map dataMap) {
		try {
		}catch (Exception excp){
		}    
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		//Realizamos accion correspondiente
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		//Procear accion segun configuracion establecida
		UtilsPrintPlugin.realizarAccionPlugin(context, this, config);
	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	public void add(InputChangedListener listener) {
	}

	public void remove(InputChangedListener listener) {
	}

	public String getTitle() {
		return this.getName();
	}

	public String getInstructions() {
		return  (""); 
	}

	public boolean isInputValid() {
		return true;
	}

	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}
	public String getID() {
		return localId;
	}

	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		return nextID;
	}

	public printPanel03()
	{
		try
		{
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setSize(new java.awt.Dimension(300,300));
		this.setLayout(new BorderLayout());
		this.add(panel, java.awt.BorderLayout.NORTH);  // Generated
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub

	}
}
