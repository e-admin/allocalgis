/**
 * FeatureInfoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.task;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.geopista.app.AppContext;


/**Cuadro de diálogo que muestra los resultados obtenidos de realizar una llamada a
 * GetFeatureInfo para una determinada entidad.
 * @author sgrodriguez
 *
 */
public class FeatureInfoDialog extends JDialog implements HyperlinkListener{
	 private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	 JPanel jpButtonPanel;
	 private JButton jbAceptar;
	 private JScrollPane jspGetFeatureInfoResults;
	 private JEditorPane jepGetFeatureInfoResults;
	 private String text;
	 private String format;
	 
	 /**Constructor
	  * @param frame Marco padre
	  * @param text Url que se pide
	  */
	 public FeatureInfoDialog(Frame frame, String text, String format){
		 super(frame,true);//es un cuadro de diálogo modal por eso se pasa un true
		 try {
			 this.text=text;
			 	this.format=format;
	            jbInit();
	            pack();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	 }//fin del constructor
	 
	 
	 
	 /**Inicializa los componentes.
	  */
	 private void jbInit() throws Exception {
		  this.setPreferredSize(new Dimension(500,300));
		  jbAceptar=new JButton(aplicacion.getI18nString("document.infodocument.botones.aceptar"));
		  jbAceptar.setEnabled(true);
		  jbAceptar.addActionListener(new java.awt.event.ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                	jbAceptar_actionPerformed(e);
	                }
	            });
		  jpButtonPanel=new JPanel();
		  FlowLayout fl=new FlowLayout();
		  fl.setAlignment(FlowLayout.RIGHT);
		  jpButtonPanel.setLayout(fl);
		  jpButtonPanel.add(jbAceptar);
		  getContentPane().add(jpButtonPanel,BorderLayout.SOUTH);
		  
		  
		  jepGetFeatureInfoResults=new JEditorPane();
		  if(format!=null && format.equalsIgnoreCase("text/html")){
			  jepGetFeatureInfoResults.setContentType("text/html");  
			  
			  if(text.contains("<?xml")){
				 int auxIndex= text.indexOf("<?xml");
				  int beginIndex=auxIndex;
				  
				  for(int i=auxIndex;i<text.length();i++){
					  if(text.charAt(i)=='?')
						  if(text.length()>i+1)
							  if(text.charAt(i+1)=='>')
								  beginIndex=i+2;
				  }//fin del for
				  text=text.substring(beginIndex);	
			  }//fin del if contiene xml  
		  }//fin del if es html
		  jepGetFeatureInfoResults.setEditable(false);
		  jepGetFeatureInfoResults.setText(text);
		  jspGetFeatureInfoResults=new JScrollPane(jepGetFeatureInfoResults);
		  jspGetFeatureInfoResults.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		  getContentPane().add(jspGetFeatureInfoResults,BorderLayout.CENTER);
		  jepGetFeatureInfoResults.addHyperlinkListener(this);
	  }//fin del método jbInit
	 

	  /**Acción realizada al pulsar el botón Aceptar
	   */
	  void jbAceptar_actionPerformed(ActionEvent e) {
		  	jbAceptar.setEnabled(false);
	        this.dispose();
	    }
	  
	  
	  
	  public void hyperlinkUpdate(HyperlinkEvent event) {
		    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		      try {
		    	  
//		    	  jepGetFeatureInfoResults.setPage(event.getURL());
		    	  Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + event.getURL());
		    	  
		      } catch(IOException ioe) {
		        warnUser(aplicacion.getI18nString("noUrl") 
		                 +" "+event.getURL().toExternalForm());
		      }
		    }
		  }
	  
	  
	  
	  private void warnUser(String message) {
		    JOptionPane.showMessageDialog(this, message, "Error", 
		                                  JOptionPane.ERROR_MESSAGE);
		  }

	  

	 
	 
	 
	 

}//fin de la clase
