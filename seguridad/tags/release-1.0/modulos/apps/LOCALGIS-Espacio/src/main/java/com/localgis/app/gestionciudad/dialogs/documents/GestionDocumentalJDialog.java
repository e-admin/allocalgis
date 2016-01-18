package com.localgis.app.gestionciudad.dialogs.documents;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 04-sep-2006
 * Time: 16:45:10
 * To change this template use File | Settings | File Templates.
 */
public class GestionDocumentalJDialog  extends JDialog{
	
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1047171970233946322L;

	Logger logger= Logger.getLogger(GestionDocumentalJDialog.class);

    private javax.swing.JFrame desktop;
    private ApplicationContext aplicacion;
    private ArrayList actionListeners= new ArrayList();

    private GestionDocumentalJPanel documentosJPanel;
    private String operacion;

    /* Botones */
    private JButton cerrarJButton= new JButton();
    
    private NotesInterventionsEditionTypes tipoEdicion = NotesInterventionsEditionTypes.VIEW;

     /**
      * Método que genera el dialogo que muestra los documentos asociados a un bien de inventario
      * @param desktop
      * @param obj bien de inventario
      * @param operacion (ANEXAR, CONSULTA_ANEXOS)
      * @throws Exception
      */
     public GestionDocumentalJDialog(JFrame desktop, final Object obj, String operacion, NotesInterventionsEditionTypes tipoEdicion) throws Exception{
         super(desktop);
         this.tipoEdicion = tipoEdicion;
         this.desktop= desktop;
         this.aplicacion= (AppContext) AppContext.getApplicationContext();
         this.operacion= operacion;
         getContentPane().setLayout(new BorderLayout());
         setModal(true);
         init(obj);
         addAyudaOnline();
     }

     public void init(Object obj) throws Exception{

         documentosJPanel= new GestionDocumentalJPanel(obj, true, this.tipoEdicion);

         documentosJPanel.setEnabled(true);


         cerrarJButton.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 cerrarJButtonActionPerformed();
             }
         });

         getContentPane().add(documentosJPanel, BorderLayout.CENTER);
         
         JPanel cerrarButtonPanel = new JPanel(new GridBagLayout());
         cerrarButtonPanel.add(cerrarJButton, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
         getContentPane().add(cerrarButtonPanel, BorderLayout.SOUTH);

         renombrarComponentes();

         addWindowListener(new java.awt.event.WindowAdapter() {
             public void windowClosing(java.awt.event.WindowEvent evt) {
                 exitForm(evt);
             }
         });


         setSize(700, 550);
         setLocation(150, 90);

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
                String uriRelativa = "/Geocuenca:Inventario:Anexar";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }


    private void cerrarJButtonActionPerformed(){
        this.dispose();
        fireActionPerformed();
    }


    public void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("inventario.document.tag8"));}catch(Exception e){}
        try{cerrarJButton.setText(aplicacion.getI18nString("inventario.document.tag7"));}catch(Exception e){}
    }

    public void addActionListener(ActionListener l){
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

    private void exitForm(java.awt.event.WindowEvent evt) {
        fireActionPerformed();
    }

    public Collection getListaDocumentos(){
    	return this.documentosJPanel.getListaDocumentos();
    }

}
