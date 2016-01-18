package com.geopista.ui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.ExtractPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


public class ExtractPanel02  extends JPanel implements WizardPanel
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
    private JLabel jLabel1 = new JLabel();
    private JRadioButton jTodoElMapaRadioButton = new JRadioButton();
    private JRadioButton jVistoEnPantallaRadioButton = new JRadioButton();
    private JRadioButton jBookmarksButton = new JRadioButton();
    
    //private JScrollPane bookMarkScroll = null;
    
    
    private GeopistaGestionBookmarksPanel geopistaGestionBookmarksPanel = null;
    public ExtractPanel02 (String id, String nextId, PlugInContext context)
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
        
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(497,404));
        
        jLabel1.setText(aplicacion.getI18nString("ExtractPanel02.SeleccioneZonaExtraer"));
        jTodoElMapaRadioButton.setText(aplicacion.getI18nString("ExtractPanel02.TodoMapa"));
        jTodoElMapaRadioButton.setActionCommand(ExtractPlugIn.ALLMAP);
        jVistoEnPantallaRadioButton.setText(aplicacion.getI18nString("ExtractPanel02.VistoPantalla"));
        jVistoEnPantallaRadioButton.setActionCommand(ExtractPlugIn.SHOWNINSCREEN);
        jBookmarksButton.setText(aplicacion.getI18nString("ExtractPanel02.BookMark"));
        jBookmarksButton.setActionCommand(ExtractPlugIn.BOOKMARKS);
        gridBagConstraints1.gridx = 0;  // Generated
        gridBagConstraints1.gridy = 3;  // Generated
        gridBagConstraints1.ipadx = 64;  // Generated
        gridBagConstraints1.ipady = -1;  // Generated
        gridBagConstraints1.insets = new java.awt.Insets(0,5,5,5);  // Generated
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;  // Generated
        gridBagConstraints2.gridx = 0;  // Generated
        gridBagConstraints2.gridy = 2;  // Generated
        gridBagConstraints2.ipadx = 104;  // Generated
        gridBagConstraints2.ipady = 4;  // Generated
        gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);  // Generated
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;  // Generated
        gridBagConstraints3.gridx = 0;  // Generated
        gridBagConstraints3.gridy = 1;  // Generated
        gridBagConstraints3.ipadx = 84;  // Generated
        gridBagConstraints3.ipady = -1;  // Generated
        gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);  // Generated
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;  // Generated
        gridBagConstraints4.gridx = 0;  // Generated
        gridBagConstraints4.gridy = 0;  // Generated
        gridBagConstraints4.ipadx = 230;  // Generated
        gridBagConstraints4.ipady = 25;  // Generated
        gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);  // Generated
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;  // Generated
        gridBagConstraints5.gridx = 0;  // Generated
        gridBagConstraints5.gridy = 4;  // Generated
        gridBagConstraints5.insets = new java.awt.Insets(5,25,5,5); 
        this.add(jBookmarksButton, gridBagConstraints1);  // Generated
        this.add(jVistoEnPantallaRadioButton, gridBagConstraints2);  // Generated
        this.add(jTodoElMapaRadioButton, gridBagConstraints3);  // Generated
        this.add(jLabel1, gridBagConstraints4);  // Generated
        this.add(getGeopistaGestionBookmarksPanel(), gridBagConstraints5);  // Generated
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
        return aplicacion.getI18nString("ExtractPanel02Instructions");
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
                    //System.out.println("valueChanged()"); // TODO Auto-generated Event stub valueChanged()
                    wizardContext.inputChanged();                    
                }
            });
        }
        return geopistaGestionBookmarksPanel;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  

