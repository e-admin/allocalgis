package com.geopista.app.alptolocalgis.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class AlpToLocalGISPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private JPanel jPanelEditingInfo = null;  
    private JPanel jPanelGraphicEditor = null;
    private JSplitPane jSplitPane = null;
	
	private PlugInContext context = null;
	
	public AlpToLocalGISPanel() {
		
		super();
        initialize();        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.app.alptolocalgis.languages.AlpToLocalGISi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("AlpToLocalGIS",bundle2);
    }

	private void initialize() {

		this.setLayout(new GridBagLayout());
				
		jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getJPanelEditingInfo(),getJPanelGraphicEditor());
        
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        this.add(jSplitPane, 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

	}
	
	public JPanel getJPanelEditingInfo()
    {
        if (jPanelEditingInfo == null)
        {
        	jPanelEditingInfo = new EditingInfoPanel();
        }
        return jPanelEditingInfo;
    }

	public JPanel getJPanelGraphicEditor()
    {
        if (jPanelGraphicEditor == null)
        {
        	jPanelGraphicEditor = new GraphicEditorPanel();
        }
        return jPanelGraphicEditor;
    }
		
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

}
