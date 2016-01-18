package com.geopista.ui.plugin;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.security.SecurityManager;
import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;

public class MunicipalitiesPlugIn extends AbstractPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(MunicipalitiesPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "MunicipalitiesPlugIn.category";

    private Vector listeners = new Vector();

    public MunicipalitiesPlugIn(){
    }


    public String getName()
    {
        return "Lista Municipios";
    }

    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        WorkbenchToolBar workbenchToolBar = ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory);
        workbenchToolBar.addPlugIn(null, this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext(),getPanelMunicipalities());
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        aplicacion.addAppContextListener(new AppContextListener()
        {

            public void connectionStateChanged(GeopistaEvent e)
            {
            	JComboBox jComboMunicipios = (JComboBox)aplicacion.getBlackboard().get(AppContext.MUNI_COMBO);
                jComboMunicipios.setEnabled(true);
/*                switch (e.getType())
                    {
                    case GeopistaEvent.DESCONNECTED:
    	                jComboMunicipios.removeAllItems();
    	                jComboMunicipios.setEnabled(false);
                        break;
                    case GeopistaEvent.RECONNECTED:
    	                jComboMunicipios.setEnabled(true);
                        break;                    
                    }*/
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
    private JComboBox getComboMunicipalities() {
        JComboBox comboMunicipios = new JComboBox();
        aplicacion.getBlackboard().put(AppContext.MUNI_COMBO, comboMunicipios);
    	comboMunicipios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JComboBox cb = (JComboBox)evt.getSource();
    	        StringTokenizer st = new StringTokenizer((String)cb.getSelectedItem(),"-");
    	        if (st.hasMoreTokens()){
    	        	String idMunicipio = (String)st.nextToken();
    	        	AppContext.setIdMunicipio(Integer.parseInt(idMunicipio));
    	        	SecurityManager.setIdMunicipio(idMunicipio);
    	        	fireEvent(idMunicipio);
    	        }
            }
        });
    	return comboMunicipios;
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
    private JPanel getPanelMunicipalities(){
    	JPanel jPanel = new JPanel();
    	jPanel.add(this.getComboMunicipalities());
    	jPanel.add(getToggleButton());
    	return jPanel;
    }
    
    private void fireEvent(String idMunicipio)
    {
        Enumeration listener = listeners.elements();
        MunicipalitiesListener toCall = null;
        while (listener.hasMoreElements())
        {
            toCall = (MunicipalitiesListener) listener.nextElement();
            if (toCall != null) toCall.municipalitiesChanged(idMunicipio);
        }
    }
    
    public void addMunicipalitiesListener(MunicipalitiesListener listener)
    {
        listeners.add(listener);
    }
    public void removeMunicipalitiesListener(MunicipalitiesListener listener)
    {
        listeners.remove(listener);
    }

}
