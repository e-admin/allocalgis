/**
 * FileGeoreferencePanel00.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georeference.panel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**
 * 
 * @author jvaca
 * 
 * Primer panel del wizardPanel. Aparece la selección de georreferenciar o 
 * completar
 */


public class FileGeoreferencePanel00 extends javax.swing.JPanel implements WizardPanel
{
    
    private String localId = null;
    private String nextID = null;



    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard  = aplicacion.getBlackboard();
    private WizardContext wizardContext;
    private JRadioButton rdbGeoreferenciar = null;
    private JRadioButton rdbCompletar = null;
    private ButtonGroup bgOrden= new ButtonGroup();

    private JLabel lblLayerName = null;
    private JLabel lblExistente = null;


    
    public FileGeoreferencePanel00(String id, String nextId, PlugInContext context2) {
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
            setName(I18N.get("Georreferenciacion","georeference.panel00.titlePanel"));
            initialize();
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("F1" ), "action F1" );
        this.getActionMap().put("action F1", new AbstractAction() 
        {
            public void actionPerformed(ActionEvent ae) { 
                GeopistaBrowser.openURL("http://192.168.47.15/docinternal/index.php/Geocuenca:Georreferenciacion");
            } 
        }); 

        
        lblExistente = new JLabel();
        lblExistente.setText(I18N.get("Georreferenciacion","georeference.panel00.labelGeoreference"));
        lblExistente.setLocation(new Point(280, 177));
        lblExistente.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
        lblExistente.setSize(new java.awt.Dimension(118,21));
        lblLayerName = new JLabel();
        lblLayerName.setText(I18N.get("Georreferenciacion","georeference.panel00.labelComplete"));
        lblLayerName.setLocation(new Point(280, 220));
        lblLayerName.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
        lblLayerName.setSize(new java.awt.Dimension(125,21));
       
        
        this.setSize(new Dimension(600, 550));
        this.setLayout(null);
        this.add(getRdbGeoreferenciar(), null);
        this.add(getRdbCompletar(), null);
        this.add(lblLayerName, null);
        this.add(lblExistente, null);
        
        bgOrden.add(getRdbGeoreferenciar());
        bgOrden.add(getRdbCompletar());
        ButtonModel model = getRdbCompletar().getModel();
        bgOrden.setSelected(model, true);
    		
    }
    /**
     * Called when the user presses Next on this panel's previous panel
     */
    public void enteredFromLeft(Map dataMap)
    { 
        
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   
        if(rdbGeoreferenciar.isSelected()){
            wizardContext.setData("Select00","0");
        }else{
            wizardContext.setData("Select00","1");
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
      return I18N.get("Georreferenciacion","georeference.panel00.title");
    }
    public String getInstructions()
    {
        return I18N.get("Georreferenciacion","georeference.panel00.instructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
  
        return true;
       
    }
    public void setWizardContext(WizardContext wd)
    {
        wizardContext =wd;
    }
    public String getID()
    {
      return localId;
    }
    public void setNextID(String nextID)
    {
       
    }
    public String getNextID()
    {
      
       
      return nextID;
    }
    public void exiting()
    {
        
    }

    /**
     * This method initializes rdbDireccion2	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRdbGeoreferenciar()
    {
        if (rdbGeoreferenciar == null)
        {
            rdbGeoreferenciar = new JRadioButton();
            rdbGeoreferenciar.setLocation(new Point(238, 177));
            rdbGeoreferenciar.setMnemonic(KeyEvent.VK_UNDEFINED);
            rdbGeoreferenciar.setSize(new java.awt.Dimension(21,21));
            rdbGeoreferenciar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    if(rdbGeoreferenciar.isSelected()){
                        
                    }
                }
            });
        }
        return rdbGeoreferenciar;
    }

    /**
     * This method initializes rdbCalleNumero	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRdbCompletar()
    {
        if (rdbCompletar == null)
        {
            rdbCompletar = new JRadioButton();
            rdbCompletar.setLocation(new Point(238, 220));
            rdbCompletar.setSize(new java.awt.Dimension(21,21));
            rdbCompletar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    if(rdbCompletar.isSelected()){

;                    }
                }
            });
            
        }
        return rdbCompletar;
    }



}  //  @jve:decl-index=0:visual-constraint="31,16"
