/**
 * GestionDocumentalJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.cementerios.Constantes;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.util.ApplicationContext;
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
    
    private ElemCementerioBean elemCementerio;

//    /* Botones */
//    private JButton cerrarJButton= new JButton();
	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;

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
    	 
    	 if ((obj!= null) && (obj instanceof ElemCementerioBean)){
    		 elemCementerio = (ElemCementerioBean) obj;
    	 }
    	 
         documentosJPanel= new GestionDocumentalJPanel(obj, true);

         if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR))
            documentosJPanel.setEnabled(true);
         else documentosJPanel.setEnabled(false);

//         cerrarJButton.addActionListener(new java.awt.event.ActionListener() {
//             public void actionPerformed(java.awt.event.ActionEvent evt) {
//                 cerrarJButtonActionPerformed();
//             }
//         });
         botoneraAceptarCancelarJPanel= new BotoneraAceptarCancelarJPanel();
         botoneraAceptarCancelarJPanel.addActionListener(new java.awt.event.ActionListener(){
           public void actionPerformed(ActionEvent e){
 				botoneraAceptarCancelarJPanel_actionPerformed();
 			}
 			});
         
         
         getContentPane().add(documentosJPanel, BorderLayout.CENTER);
//         getContentPane().add(cerrarJButton, BorderLayout.SOUTH);
         getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
         
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


//    private void cerrarJButtonActionPerformed(){
//        this.dispose();
//        fireActionPerformed();
//    }

    public void botoneraAceptarCancelarJPanel_actionPerformed(){
        if((!botoneraAceptarCancelarJPanel.aceptarPressed()) ||
           (botoneraAceptarCancelarJPanel.aceptarPressed() && operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)?!confirmOption():false)){
            elemCementerio= null;
        }
        else{
        	documentosJPanel.actualizarDatos(elemCementerio);
        }
        fireActionPerformed();
    }
    
    public void actualizarDatosElemCementerio(ElemCementerioBean elemCementerio){
    	
    	try {
			documentosJPanel.load(elemCementerio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		documentosJPanel.actualizarDatos(elemCementerio);
    }
    
    /**
     * Método que abre una ventana de confirmacion sobre la operacion que se esta llevando a cabo
     */
    private boolean confirmOption(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("cementerio.optionpane.tag1"), aplicacion.getI18nString("cementerio.optionpane.tag2"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("cementerio.document.tag8"));}catch(Exception e){}
//        try{cerrarJButton.setText(aplicacion.getI18nString("cementerio.document.tag7"));}catch(Exception e){}
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

	public ElemCementerioBean getElemCementerio() {
		return elemCementerio;
	}

	public void setElemCementerio(ElemCementerioBean elemCementerio) {
		this.elemCementerio = elemCementerio;
	}
    
    public GestionDocumentalJPanel getDocumentosJPanel() {
        return documentosJPanel;
    }


}
