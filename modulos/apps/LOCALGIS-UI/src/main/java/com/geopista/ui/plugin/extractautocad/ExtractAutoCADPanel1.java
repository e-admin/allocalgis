/**
 * ExtractAutoCADPanel1.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.extractautocad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.plugin.ExtractPlugIn;
import com.geopista.ui.plugin.extractautocad.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class ExtractAutoCADPanel1 extends JPanel implements WizardPanel
{

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private String localId = null;
	private WizardContext wizardContext;
	private PlugInContext context;
	private HashMap checkBoxMap = null;
	private String nextID = null;
	private JList availableLayerList = null;
	private JScrollPane availableLayerScrollPane = null;
	private JScrollPane layersToExtractScrollPane = null;
	private JList layersToExtractList = null;
	private JPanel buttonsPanel = null;
	private JButton toLeftButton = null;
	private JButton toRightButton1 = null;
	private JPanel layerspanel = null;
	private JPanel jPanelInfo = null;

	public ExtractAutoCADPanel1(String id, String nextId, PlugInContext context)
	{
		this.nextID = nextId;
		this.localId = id;
		this.context = context;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception
	{   
		this.setLayout(new GridBagLayout());

		this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getLayersPanel(), 
				new GridBagConstraints(0,1,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
	}


	private JPanel getLayersPanel(){

		if (layerspanel == null){

			layerspanel = new JPanel(new GridBagLayout());
			layerspanel.setSize(new Dimension(680, 450));
			layerspanel.setPreferredSize(new Dimension(680, 450));
			layerspanel.setMaximumSize(layerspanel.getPreferredSize());
			layerspanel.setMinimumSize(layerspanel.getPreferredSize());

			layerspanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.SelectLayersToExtract"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			layerspanel.add(getAvailableLayerScrollPane(), 
					new GridBagConstraints(0,0,3,1, 0.5, 1.0,GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL, new Insets(10,10,10,10),0,0));

			layerspanel.add(getButtonsPanel(), 
					new GridBagConstraints(3,0,1,1,1.0, 1.0,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));

			layerspanel.add(getLayersToExtractScrollPane(), 
					new GridBagConstraints(4,0,3,1,0.5, 1.0,GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL, new Insets(10,10,10,10),0,0));

			EdicionUtils.crearMallaPanel(1, 7, layerspanel, 1.0, 0.0, 
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
					new Insets(0, 0, 0, 0), 0, 0);

		}
		return layerspanel;
	}


	public void enteredFromLeft(Map dataMap)
	{

	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception
	{
		Enumeration extractLayers = ((DefaultListModel) layersToExtractList.getModel()).elements();
		ArrayList toExtractLayers = new ArrayList();
		ArrayList lockedLayers = new ArrayList();
		ArrayList toUnlockLayers = new ArrayList();

		GeopistaConnection geoconn = null;

		//Comprobar si las capas estan bloqueadas
		while(extractLayers.hasMoreElements())
		{
			GeopistaLayer gl = (GeopistaLayer) extractLayers.nextElement();
			//Se intenta bloquear cada capa
			try
			{
	            final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
                AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(
                        sUrlPrefix+ "/AdministradorCartografiaServlet");
				acClient.lockLayer(gl.getSystemId(), null);
				toExtractLayers.add(gl);
				toUnlockLayers.add(gl);

			} 
			catch (Exception e1)
			{
				// Si falla algo en el bloqueo seguimos con el resto de
				// las capas bloqueadas
				//e1.printStackTrace();
				lockedLayers.add(gl);
			}

		}

		if (lockedLayers.size()>0)
		{

			String string1 = aplicacion.getI18nString("general.si"); 
			String string2 = aplicacion.getI18nString("general.no"); 
			Object[] options = {string1, string2};

			StringBuffer mensaje = new StringBuffer(aplicacion.getI18nString("ExtractMapPlugin.capasbloqueadas"));
			mensaje.append("\n");
			Iterator it = lockedLayers.iterator();

			while (it.hasNext())
			{
				mensaje.append(" - ").append(it.next()).append("\n");
			}

			mensaje.append(I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Warning1"))
			.append("\n").append(I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Warning2"));

			int n = JOptionPane.showOptionDialog(this,
					mensaje.toString(),
					I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Warning3"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,null,options,options[1]);

			if (n==JOptionPane.YES_OPTION)
			{   
				it = lockedLayers.iterator();

				while (it.hasNext())
				{
					GeopistaLayer gl = (GeopistaLayer)(it.next());
					toExtractLayers.add(gl);
					toUnlockLayers.add(gl);
				}

			}
			else if (n==JOptionPane.NO_OPTION)
			{

				ListModel modr = layersToExtractList.getModel();
				ListModel model = availableLayerList.getModel();
				for(int i=0; i<lockedLayers.size(); i++)
				{                        
					if (model instanceof DefaultListModel)
					{
						DefaultListModel mod=(DefaultListModel)model;
						mod.addElement(lockedLayers.get(i));
					}
					if (modr instanceof DefaultListModel)
					{
						DefaultListModel mod=(DefaultListModel)modr;
						mod.removeElement(lockedLayers.get(i));
					}
				}

				wizardContext.inputChanged();

			}            

		}

		if (layersToExtractList.getModel().getSize()==0)
		{
			this.nextID="";

		}
		else
		{
			this.nextID="ExtractAutoCADPanel2";
		}

		Iterator itUnlock = toUnlockLayers.iterator();
		while (itUnlock.hasNext())
		{
			GeopistaLayer gl = (GeopistaLayer)itUnlock.next();

			// Se intenta desbloquear cada capa
			try
			{
	            final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
                AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(
                        sUrlPrefix+ "/AdministradorCartografiaServlet");
				acClient.unlockLayer(gl.getSystemId());                    

			} 
			catch (Exception e1)
			{
				// Si falla algo en el desbloqueo seguimos con el resto de
				// las capas bloqueadas, pero no se extraerá la capa
				//e1.printStackTrace();

				toExtractLayers.remove(gl);

				//Se informa al usuario
				StringBuffer msg = new StringBuffer();
				msg.append(I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Message1")).append(gl.toString())
				.append("\n").append(I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Message2"));

				JOptionPane optionPane= new JOptionPane(msg.toString(),
						JOptionPane.ERROR_MESSAGE);
				JDialog dialog =optionPane.createDialog(this,"");
				dialog.show();                    

			}                
		}


		blackboard.put(ExtractPlugIn.EXTRACTLAYERS,toExtractLayers);


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
		return localId;
	}

	public String getInstructions()
	{
		return " ";
	}

	public boolean isInputValid()
	{
		if (layersToExtractList.getModel().getSize()!=0)
			return true;
		else 
			return false;

	}

	public void setWizardContext(WizardContext wd)
	{
		wizardContext = wd;
	}

	/**
	 * @return null to turn the Next button into a Finish button
	 */

	public JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.Info1"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}

	public void setNextID(String nextID)
	{
		this.nextID=nextID;
	}
	public String getNextID()
	{
		return nextID;
	}


	/**
	 * This method initializes availableLayerList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getAvailableLayerList() {
		if (availableLayerList == null) {
			availableLayerList = new JList();
			GeopistaUtil.initializeLayerList("available",availableLayerList,null,"",
					context.getLayerManager().getLayers());
		}
		return availableLayerList;
	}
	/**
	 * This method initializes availableLayerScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getAvailableLayerScrollPane() {

		if (availableLayerScrollPane == null) {
			availableLayerScrollPane = new JScrollPane();
			availableLayerScrollPane.setSize(new Dimension(288, 560));
			availableLayerScrollPane.setPreferredSize(new Dimension(288, 560));
			availableLayerScrollPane.setMaximumSize(availableLayerScrollPane.getPreferredSize());
			availableLayerScrollPane.setMinimumSize(availableLayerScrollPane.getPreferredSize());
			availableLayerScrollPane.setViewportView(getAvailableLayerList());
			availableLayerScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return availableLayerScrollPane;
	}
	/**
	 * This method initializes layersToExtractScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getLayersToExtractScrollPane() {

		if (layersToExtractScrollPane == null) {
			layersToExtractScrollPane = new JScrollPane();
			layersToExtractScrollPane.setSize(new Dimension(288, 560));
			layersToExtractScrollPane.setPreferredSize(new Dimension(288, 560));
			layersToExtractScrollPane.setMaximumSize(layersToExtractScrollPane.getPreferredSize());
			layersToExtractScrollPane.setMinimumSize(layersToExtractScrollPane.getPreferredSize());
			layersToExtractScrollPane.setViewportView(getLayersToExtractList());
			layersToExtractScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return layersToExtractScrollPane;
	}
	/**
	 * This method initializes layersToExtractList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getLayersToExtractList() {
		if (layersToExtractList == null) {
			layersToExtractList = new JList();
			GeopistaUtil.initializeLayerList("available",layersToExtractList,null,"",
					new Vector());
		}
		return layersToExtractList;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
			buttonsPanel.add(getToRightButton1(), null);
			buttonsPanel.add(getToLeftButton(), null);
		}
		return buttonsPanel;
	}
	/**
	 * This method initializes toLeftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToLeftButton() {

		if (toLeftButton == null) {

			toLeftButton = new JButton();
			toLeftButton.setIcon(IconLoader.icon("Left.gif"));
			toLeftButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					Object[] lay= layersToExtractList.getSelectedValues();
					if (lay!=null)
					{
						ListModel modr = layersToExtractList.getModel();
						ListModel model = availableLayerList.getModel();
						for(int n=0; n<lay.length; n++)
						{

							if (model instanceof DefaultListModel)
							{
								DefaultListModel mod=(DefaultListModel)model;
								mod.addElement(lay[n]);
							}
							if (modr instanceof DefaultListModel)
							{
								DefaultListModel mod=(DefaultListModel)modr;
								mod.removeElement(lay[n]);
							}
						}
					}
					wizardContext.inputChanged();
				}
			});
		}
		return toLeftButton;
	}
	/**
	 * This method initializes toRightButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getToRightButton1() {
		if (toRightButton1 == null) {
			toRightButton1 = new JButton();
			toRightButton1.setIcon(IconLoader.icon("Right.gif"));
			toRightButton1.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    

					Object[] lay=  availableLayerList.getSelectedValues();
					if (lay!=null)
					{
						ListModel modl = availableLayerList.getModel();
						ListModel modr = layersToExtractList.getModel();
						for(int n=0; n<lay.length; n++)
						{
							if (sePuedeExtraerCapa(lay[n])){
	
								if (modr instanceof DefaultListModel)
								{
									DefaultListModel mod=(DefaultListModel)modr;
									mod.addElement(lay[n]);
								}
								if (modl instanceof DefaultListModel)
								{
									DefaultListModel mod=(DefaultListModel)modl;
									mod.removeElement(lay[n]);
								}
							}else{
								JOptionPane optionPane= new JOptionPane(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.VersionedLayers"),
										JOptionPane.WARNING_MESSAGE);								
                                JDialog dialog =optionPane.createDialog(aplicacion.getMainFrame(),"");
                                dialog.show();
							}
						}

					}

					wizardContext.inputChanged();

				}
			});
		}
		return toRightButton1;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting()
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * Método que devuelve un booleano indicando si la capa es editable o no
	 * @param layer
	 * @return
	 */
	private boolean sePuedeExtraerCapa(Object layer){
		if (layer instanceof GeopistaLayer){
			if (((GeopistaLayer)layer).isVersionable()){
				if (((GeopistaLayer)layer).getRevisionActual() != -1)
					return false;
			}
		}
		return true;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"