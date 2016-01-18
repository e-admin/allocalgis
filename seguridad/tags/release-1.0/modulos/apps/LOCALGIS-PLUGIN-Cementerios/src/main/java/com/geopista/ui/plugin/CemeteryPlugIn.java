package com.geopista.ui.plugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.security.SecurityManager;
import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.cementerios.Constantes;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;

public class CemeteryPlugIn extends AbstractPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log logger	= LogFactory.getLog(CemeteryPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private String toolBarCategory = "CemeteryPlugIn.category";


    public CemeteryPlugIn(){}


    public String getName()
    {
        return "Lista de Cementerios";
    }

    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        WorkbenchToolBar workbenchToolBar = ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory);
        workbenchToolBar.addPlugIn(null, this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext(),getPanelCementerios());
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        aplicacion.addAppContextListener(new AppContextListener()
        {

            public void connectionStateChanged(GeopistaEvent e)
            {
            	JComboBox jComboMunicipios = (JComboBox)aplicacion.getBlackboard().get(Constantes.COMBO_CEMENT);
                jComboMunicipios.setEnabled(true);
                
            }
        });

        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        //EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        GeopistaEnableCheckFactory checkFactoryGeopista = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
        		checkFactoryGeopista.createWindowWithLayerManagerMustBeActiveCheck()).add(
//                checkFactoryGeopista.createDisconnectedDisableMunicipalities());//.add(
                checkFactoryGeopista.createWindowWithAssociatedTaskFrameMustBeActiveCheck());

    }
    
    //Se visualizará una lista con los municipios que forma parte de la entidad supramunicipal del
    //usuario, en la que se podrá seleccionar en todo momento un municipio por defecto
    private JComboBox getComboCementerios() {
        JComboBox comboCementerios = new JComboBox();
        aplicacion.getBlackboard().put(Constantes.COMBO_CEMENT, comboCementerios);
        
    	return comboCementerios;
    }
    
    /**
     * Si el botón está apretado, se seleccionará automáticamente en el combo el municipio en el que nos encontremos.
     * En caso contrario, la selección se realizará de forma manual
     * @return JToggleButton
     */
    private JToggleButton getToggleButton (){
    	JToggleButton jToggleButton = new JToggleButton(aplicacion.getI18nString("SeleccionAutomatica"));
    	jToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (((JToggleButton)evt.getSource()).isSelected())
            		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, true);
            	else
            		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, false);
            }
    	});
		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, false);
    	return jToggleButton;
    }
    
    private JPanel getPanelCementerios(){
    	JPanel jPanel = new JPanel();
    	jPanel.add(this.getComboCementerios());
    	/* Por el momento no se seleccionaran automaticamente los cementerios
    	 *	jPanel.add(getToggleButton());
    	 */
    	return jPanel;
    }
    

}

