package com.geopista.app.eiel.dialogs;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.panels.FichasFilterPanel;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FichasFilterDialog extends JDialog{
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String tipo;
    private FichasFilterPanel fichasFilterPanel;
    private ArrayList actionListeners= new ArrayList();

	private HashMap<String,String> listaFiltros;

    /**
     * Método que genera el dialogo de filtro
     * @param traduccionSelected 
     * @param listaFeaturesPorCapa 
     * @param selectedElements 
     */
    public FichasFilterDialog(JFrame desktop, String tipo,  HashMap<String,String> listaFiltros, String locale) throws Exception{
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        AppContext.getApplicationContext().getBlackboard().put(AppContext.idAppType, "eiel" );
        this.locale= locale;
        this.tipo= tipo;
        this.listaFiltros=listaFiltros;

        initComponents();
        renombrarComponentes();
        addAyudaOnline();
    }

    private void initComponents() throws Exception{
        
    	
    	fichasFilterPanel= new FichasFilterPanel(desktop, tipo,listaFiltros,locale);
        fichasFilterPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                informesJPanel_actionPerformed();
            }
        });


        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        getContentPane().add(fichasFilterPanel, BorderLayout.CENTER);

        //Tamaño del panel de informes
        
        if (!tipo.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL) )      	
        	setSize(767, 550);
        else
        	setSize(767, 300);
        //setLocation(100, 0);
        GUIUtil.centreOnWindow(this);

    }

    /**
     * Ayuda Online
     *
     */
    private void addAyudaOnline() {
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("F1"), "action F1");

        getRootPane().getActionMap().put("action F1", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                String uriRelativa = "/Geocuenca:Inventario:Informes";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }


    private void renombrarComponentes(){
    	if (!tipo.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL) )      	
    		try{setTitle(aplicacion.getI18nString("inventario.informes.tag1"));}catch(Exception e){}
    	else
    		try{setTitle(aplicacion.getI18nString("eiel.informes.tag1"));}catch(Exception e){}
    }    

    private void informesJPanel_actionPerformed(){
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