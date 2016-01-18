package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.Constantes;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
    Logger logger= Logger.getLogger(GestionDocumentalJDialog.class);

    private javax.swing.JFrame desktop;
    private ApplicationContext aplicacion;
    private ArrayList actionListeners= new ArrayList();

    private GestionDocumentalJPanel documentosJPanel;
    private String operacion;

    /* Botones */
    private JButton cerrarJButton= new JButton();

     /**
      * Método que genera el dialogo que muestra los documentos asociados a un bien de inventario
      * @param desktop
      * @param obj bien de inventario
      * @param operacion (ANEXAR, CONSULTA_ANEXOS)
      * @throws Exception
      */
     public GestionDocumentalJDialog(JFrame desktop, final Object obj, String operacion) throws Exception{
         super(desktop);
         this.desktop= desktop;
         this.aplicacion= (AppContext) AppContext.getApplicationContext();
         this.operacion= operacion;
         getContentPane().setLayout(new BorderLayout());
         setModal(true);
         init(obj);
         addAyudaOnline();
     }

     public void init(Object obj) throws Exception{

         documentosJPanel= new GestionDocumentalJPanel(obj, true);

         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR))
            documentosJPanel.setEnabled(true);
         else documentosJPanel.setEnabled(false);

         cerrarJButton.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 cerrarJButtonActionPerformed();
             }
         });

         getContentPane().add(documentosJPanel, BorderLayout.CENTER);
         JPanel aux = new JPanel();
         aux.setLayout(new FlowLayout());
         aux.add(cerrarJButton);
         getContentPane().add(aux, BorderLayout.SOUTH);

         renombrarComponentes();

         addWindowListener(new java.awt.event.WindowAdapter() {
             public void windowClosing(java.awt.event.WindowEvent evt) {
                 exitForm(evt);
             }
         });


         setSize(700, 550);
         //setLocation(150, 90);
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


}
