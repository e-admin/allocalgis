/**
 * ExtractAutoCADPanel2.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.GeopistaBookmark;
import com.geopista.ui.dialogs.GeopistaGestionBookmarksPanel;
import com.geopista.ui.plugin.ExtractPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class ExtractAutoCADPanel2  extends JPanel implements WizardPanel
{
    
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard  = aplicacion.getBlackboard();
    private String localId = null;
    private WizardContext wizardContext;
    private PlugInContext context;
    private ButtonGroup group;
    /**
     * @return null to turn the Next button into a Finish button
     */
    
    private String nextID = null;
    private JLabel jLabelSelect = new JLabel();
    private JRadioButton jTodoElMapaRadioButton = new JRadioButton();
    private JRadioButton jVistoEnPantallaRadioButton = new JRadioButton();
    private JRadioButton jBookmarksButton = new JRadioButton();
    
    private JPanel jPanelSelectArea = null;
    private JPanel jPanelInfo = null;
    
    //private JScrollPane bookMarkScroll = null;
    
    
    private GeopistaGestionBookmarksPanel geopistaGestionBookmarksPanel = null;
    public ExtractAutoCADPanel2 (String id, String nextId, PlugInContext context)
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
    
    private JPanel getJPanelSelectArea(){
    	
    	if (jPanelSelectArea == null){
    		
    		jPanelSelectArea = new JPanel(new GridBagLayout());
    		
    		jPanelSelectArea.setSize(new java.awt.Dimension(250, 450));
    		jPanelSelectArea.setPreferredSize(new Dimension(250, 450));
    		jPanelSelectArea.setMaximumSize(jPanelSelectArea.getPreferredSize());
    		jPanelSelectArea.setMinimumSize(jPanelSelectArea.getPreferredSize());
    		
    		jPanelSelectArea.setBorder(BorderFactory.createTitledBorder
	                  (null,I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.SelectLayersToExtract"), 
	                          TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
	         
            
            jLabelSelect.setText(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.SelectAreaToExtract"));
            
            jTodoElMapaRadioButton.setText(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.AllMap"));
            jTodoElMapaRadioButton.setActionCommand(ExtractPlugIn.ALLMAP);
            
            jVistoEnPantallaRadioButton.setText(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.Screen"));
            jVistoEnPantallaRadioButton.setActionCommand(ExtractPlugIn.SHOWNINSCREEN);
            
            jBookmarksButton.setText(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.BookMark"));
            jBookmarksButton.setActionCommand(ExtractAutoCADPlugIn.BOOKMARKS);
            
            jPanelSelectArea.add(jLabelSelect,
      			  new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.WEST,
      					  GridBagConstraints.NONE, new Insets(5,20,0,5),0,0));
                     
            jPanelSelectArea.add(jTodoElMapaRadioButton,
    			  new GridBagConstraints(0,1,1,1, 1.0, 1.0,GridBagConstraints.WEST,
    					  GridBagConstraints.NONE, new Insets(5,30,0,5),0,0));
            
            jPanelSelectArea.add(jVistoEnPantallaRadioButton,
      			  new GridBagConstraints(0,2,1,1, 1.0, 1.0,GridBagConstraints.WEST,
      					  GridBagConstraints.NONE, new Insets(5,30,0,5),0,0));
            
            jPanelSelectArea.add(jBookmarksButton, 
      			  new GridBagConstraints(0,3,1,1, 1.0, 1.0,GridBagConstraints.WEST,
      					  GridBagConstraints.NONE, new Insets(5,30,0,5),0,0));
              
            jPanelSelectArea.add(getGeopistaGestionBookmarksPanel(),
    			  new GridBagConstraints(0,4,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
    					  GridBagConstraints.NONE, new Insets(5,30,5,5),0,0));
            		
            getGeopistaGestionBookmarksPanel().setCloseVisible(false);        
            
            jBookmarksButton.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{
            		jBookMarkButton3_actionPerformed(e);
            	}
            });
            
            jVistoEnPantallaRadioButton.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{
            		jBookMarkButton2_actionPerformed(e);
            	}
            });

            jTodoElMapaRadioButton.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{
            		jBookMarkButton1_actionPerformed(e);
            	}
            });

            
    	}
    	return jPanelSelectArea;
    }
    
    private void jbInit() throws Exception
    {
        
        this.setLayout(new GridBagLayout());
        
        this.add(getJPanelInfo(), 
  			  new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
  					  GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
        
        this.add(getJPanelSelectArea(), 
  			  new GridBagConstraints(0,1,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
  					  GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

    }
    
	private JPanel getJPanelInfo(){
		if (jPanelInfo == null){
		
			jPanelInfo = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
	                  (null,I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.Info"), 
	                          TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
	          
			JTextArea jTextAreaInfo = new JTextArea(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.Info2"));
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
    
    
    public void enteredFromLeft(Map dataMap)
    {
        
        if(!aplicacion.isLogged())
        {
                aplicacion.login();
        }
        if(!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        
        group = new ButtonGroup();
        group.add(jTodoElMapaRadioButton);
        group.add(jVistoEnPantallaRadioButton);
        group.add(jBookmarksButton);   
        
        
        
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        String finalSelection = group.getSelection().getActionCommand();
        if(finalSelection.equals(ExtractPlugIn.ALLMAP))
        {
            blackboard.put(ExtractPlugIn.EXTRACTZONE,ExtractPlugIn.ALLMAP);
        }
        if(finalSelection.equals(ExtractPlugIn.SHOWNINSCREEN))
        {
            blackboard.put(ExtractPlugIn.EXTRACTZONE,ExtractPlugIn.SHOWNINSCREEN);
        }
        
        if(finalSelection.equals(ExtractPlugIn.BOOKMARKS))
        {
            blackboard.put(ExtractPlugIn.EXTRACTZONE,ExtractPlugIn.BOOKMARKS);
            GeopistaBookmark selectedBookmark = geopistaGestionBookmarksPanel.getSelectedBookmark();
            Envelope bookMarkEnvelope = new Envelope(selectedBookmark.getXmin(),selectedBookmark.getXmax(),selectedBookmark.getYmin(),selectedBookmark.getYmax());
            blackboard.put(ExtractPlugIn.BOOKMARKEXTRACTENVELOPE,bookMarkEnvelope);
        }
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
        //return aplicacion.getI18nString("ExtractPanel02Instructions");
    	return " ";
    }
    
    public boolean isInputValid()
    {        
           for (Enumeration e=group.getElements(); e.hasMoreElements(); ) {
                JRadioButton b = (JRadioButton)e.nextElement();
                if (b.getModel() == group.getSelection()) {
                    
                    if (b.equals(jBookmarksButton))
                    {
                        //Comprueba que esté seleccionado algún bookmark
                       GeopistaBookmark selectedBookmark = geopistaGestionBookmarksPanel.getSelectedBookmark();
                       if (selectedBookmark==null)
                           return false;                        
                        
                    }
                    
                    return true;
                }
            }
            return false;
        
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }
    
    
    
    public void setNextID(String nextID)
    {
        this.nextID=nextID;
    }
    public String getNextID()
    {
        return nextID;
    }
    
    private void jBookMarkButton3_actionPerformed(ActionEvent e)
    {
        getGeopistaGestionBookmarksPanel().setEnabled(true);
        wizardContext.inputChanged();
    }
    
    private void jBookMarkButton2_actionPerformed(ActionEvent e)
    {
        getGeopistaGestionBookmarksPanel().setEnabled(false);
        wizardContext.inputChanged();
    }
    
    private void jBookMarkButton1_actionPerformed(ActionEvent e)
    {
        getGeopistaGestionBookmarksPanel().setEnabled(false);
        wizardContext.inputChanged();
    }
    
    
    
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
    /**
     * This method initializes geopistaGestionBookmarksPanel	
     * 	
     * @return com.geopista.ui.dialogs.GeopistaGestionBookmarksPanel	
     */    
    private GeopistaGestionBookmarksPanel getGeopistaGestionBookmarksPanel() {
    	
        if (geopistaGestionBookmarksPanel == null) {
        	
            geopistaGestionBookmarksPanel = new GeopistaGestionBookmarksPanel(context);
            geopistaGestionBookmarksPanel.setEnabled(false);
            geopistaGestionBookmarksPanel.addListSelectionListener(new javax.swing.event.ListSelectionListener() { 
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {   
                    wizardContext.inputChanged();                    
                }
            });
        }
        return geopistaGestionBookmarksPanel;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  

