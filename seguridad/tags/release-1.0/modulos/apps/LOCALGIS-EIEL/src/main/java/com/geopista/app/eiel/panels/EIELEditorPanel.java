package com.geopista.app.eiel.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.util.exception.CancelException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class EIELEditorPanel extends JPanel{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private JPanel jPanelEditingInfo = null;  
    private EditorPanel jPanelGraphicEditor = null;
    private JSplitPane jSplitPane = null;
	
	private PlugInContext context = null;
	
	public EIELEditorPanel()  {
		
		super();
        initialize();        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle2);
    }

	public void initialize() {

		this.setLayout(new GridBagLayout());
				
		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelEditingInfo(),getJPanelGraphicEditor());
        
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        /*Dimension minimumSize = new Dimension(300, 300);
        getJPanelEditingInfo().setMinimumSize(minimumSize);
        getJPanelGraphicEditor().setMinimumSize(minimumSize);*/
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
        
        ((GeopistaEditorPanel)getJPanelGraphicEditor().getGeopistaEditorPanel()).addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	
				if (ConstantesLocalGISEIEL.EVENT_EIEL_MAP_SELECTION_CHANGED.equals(e.getActionCommand())){
					//System.out.println("SELECTION CHANGED");
					mapaJPanel_actionPerformed(e);
					
            	}
				
				else
					mapaJPanel_actionPerformed(e);
			}
		});

	}
	
	public void restartEditingPanel(){
		jPanelEditingInfo=null;
	}
	public boolean isRestarted(){
		return jPanelEditingInfo==null;
	}
	private void mapaJPanel_actionPerformed(ActionEvent e){

		((EditingInfoPanel)getJPanelEditingInfo()).getJPanelTree().fireActionPerformed(e,e.getActionCommand());	
		
	}
	
	
	
	public JPanel getJPanelEditingInfo()
    {
        if (jPanelEditingInfo == null)
        {
        	jPanelEditingInfo = new EditingInfoPanel();
        }
        return jPanelEditingInfo;
    }

	public EditorPanel getJPanelGraphicEditor()
    {
        if (jPanelGraphicEditor == null)
        {
        	jPanelGraphicEditor = new EditorPanel();
        }
        return jPanelGraphicEditor;
    }
		
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

}
