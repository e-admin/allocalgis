package com.vividsolutions.jump.workbench.ui.task;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;


/**Cuadro de diálogo que pregunta sobre el formato en el que el usuario desea que
 * se le muestren los resultados de la petición: getFeatureInfo.
 * @author sgrodriguez
 *
 */
public class FormatFeatureInfoDialog extends JDialog{
//formato seleccionado por el usuario
	private static String format;
	private List formats;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	JPanel jpButtonPanel;
	private JButton jbAceptar;
	private JPanel jpFormat;
	private JLabel jlFormat;
	private JComboBox jcbFormat;
	 
	 
	 
	 /**Constructor
	  * @param frame Marco padre
	  */
	 public FormatFeatureInfoDialog(Frame frame,List formats){
		 super(frame,true);
		 this.formats=formats;
		 try {
			 jbInit();
	            pack();
	        } catch (Exception ex) {
	            ex.printStackTrace();
		 }
	 }//fin del constructor
	
	
	
	 
	 
	 /**Inicializa los componentes.
	  */
	 private void jbInit() throws Exception {
		 this.setTitle(aplicacion.getI18nString("getFeatureInfo.format"));
		  this.setPreferredSize(new Dimension(300,100));
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
		  
		  
		  jpFormat=new JPanel();
		  jlFormat=new JLabel(aplicacion.getI18nString("getFeatureInfo.formatString"));
		  FlowLayout fl1=new FlowLayout();
		  fl1.setAlignment(FlowLayout.LEFT);
		  jpFormat.setLayout(fl1);
		  jpFormat.add(jlFormat);
		  jcbFormat=new JComboBox();
		  
		  //añadimos los formatos disponibles al combo
		  Iterator it=formats.iterator();
		  while(it.hasNext()){
			  String format=(String)it.next();
			  jcbFormat.addItem(format);
		  }//fin while
		  jlFormat.setLabelFor(jcbFormat);
		  jpFormat.add(jcbFormat);
		  
		  getContentPane().add(jpFormat,BorderLayout.CENTER);
		  
	 }//fin del método jbInit
	 
	 
	 
	 
	 public static String getFormat() {
		return format;
	}


	/**Acción realizada al pulsar el botón Aceptar
	   */
	  void jbAceptar_actionPerformed(ActionEvent e) {
		  	format=(String) jcbFormat.getSelectedItem();
		  	jbAceptar.setEnabled(false);
	        this.dispose();
	    }

	 
	 
	 
}//fin de la clase
