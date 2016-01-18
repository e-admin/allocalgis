package com.geopista.app.eiel.dialogs;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.ILocalGISMap;
import com.geopista.app.eiel.panels.FichasFilterPanel;
import com.geopista.app.eiel.panels.MapasTematicosPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapasTematicosDialog extends JDialog{
	
	private static final Log logger = LogFactory.getLog(MapasTematicosDialog.class);
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private MapasTematicosPanel mapastematicosPanel;
    private ArrayList actionListeners= new ArrayList();
	private ILocalGISMap localgisMap;


    /**
     * Método que genera el dialogo de filtro
     * @param actionListener 
     * @param traduccionSelected 
     * @param listaFeaturesPorCapa 
     * @param selectedElements 
     */
    public MapasTematicosDialog(JFrame desktop, ILocalGISMap localgisMap) throws Exception{
        super(desktop);
        this.localgisMap=localgisMap;
        aplicacion= (AppContext) AppContext.getApplicationContext();

        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        
    	
    	mapastematicosPanel= new MapasTematicosPanel(desktop,localgisMap);
    	mapastematicosPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	mapasTematicosJPanel_actionPerformed();
            }
        });


        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        getContentPane().add(mapastematicosPanel, BorderLayout.CENTER);

       
       	setSize(767, 250);
        GUIUtil.centreOnWindow(this);

    }

   

    private void renombrarComponentes(){
		try{setTitle(I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"));}catch(Exception e){}
    }    

    private void mapasTematicosJPanel_actionPerformed(){
        fireActionPerformed();
    }

    public void actionPerformed(ActionEvent e){
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }



}