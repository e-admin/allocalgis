/**
 * MessageDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;

public class MessageDialog extends JDialog {
	
	 private JPanel resultadoPanel;
	 private JEditorPane jEditorPaneResultado = null;
	 private JScrollPane jScrollPaneResultado = null;
	 private JLabel resultado = null;
	 private JButton aceptarJButton;
	 private String title;
	 
	 
	 public static final int DIM_X=600;
	 public static final int DIM_Y=400;

	  public MessageDialog(ApplicationContext application, String title) {
	    	super(application.getMainFrame());
	    	this.title = title;
	    	initialize();
	    }
	  
	  private void initialize() {
	       
		this.setModal(false);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(DIM_X, DIM_Y);
        
		resultadoPanel = new JPanel();
		resultadoPanel.setLayout(new GridBagLayout());
		
		resultado  = new JLabel();
        resultado.setText(I18N.get("ComunicacionWSCatastro", "comunicacion.catastro.resultado"));
        
		resultadoPanel.add(resultado, 
				new GridBagConstraints(0,0,1,1, 1, 0.5,GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
		resultadoPanel.add(getJScrollPaneResultado(), 
                new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 5), 0, 0));
		
		resultadoPanel.add(getAceptarJButton(), 
                new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(resultadoPanel);
		this.setTitle(I18N.get("ComunicacionWSCatastro", title));
        this.addWindowListener(new java.awt.
        		event.WindowAdapter()
        {
		     public void windowClosing(java.awt.event.WindowEvent e)
		     {
		         dispose();
		     }
		 });
    }
	  
	  public JButton getAceptarJButton() {
			if(aceptarJButton == null){
				aceptarJButton = new JButton();
				aceptarJButton.setText(I18N.get("ComunicacionWSCatastro",
										"comunicacion.catastro.resultado.aceptar"));
				
				
				aceptarJButton.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent e)
			        {
			        	dispose();
			        }
			    });

			}
			
			return aceptarJButton;
		}

	  
	  private JScrollPane getJScrollPaneResultado()
	    {
	        if (jScrollPaneResultado == null)
	        {
	            jScrollPaneResultado = new JScrollPane();
	            jScrollPaneResultado.getViewport().add(getJEditorPaneResultadoImportacion());
	            jScrollPaneResultado.setPreferredSize(new Dimension(100, 300));
	            
	        }
	        return jScrollPaneResultado;
	    }
	  
	  public JEditorPane getJEditorPaneResultadoImportacion()
	    {
	        if (jEditorPaneResultado == null)
	        {
	            jEditorPaneResultado = new JEditorPane();
	            jEditorPaneResultado.setText("<br><br>");
	            jEditorPaneResultado.setContentType("text/html");
	            jEditorPaneResultado.setEditable(false);
	            jEditorPaneResultado.setPreferredSize(new Dimension(100, 100));
	        }
	        return jEditorPaneResultado;
	    }

}

