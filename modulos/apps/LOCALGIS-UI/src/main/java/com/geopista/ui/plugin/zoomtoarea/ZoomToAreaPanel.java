/**
 * ZoomToAreaPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.zoomtoarea;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.app.utilidades.TextField;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OptionsPanel;

public class ZoomToAreaPanel extends JPanel implements OptionsPanel{
	
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private JPanel jPanelRightPoint = null;  
    private JPanel jPanelLeftPoint = null;
	
	private JLabel jLabelLeftCoordX = null;
	private JLabel jLabelLeftCoordY = null;
	private JTextField jTextFieldLeftCoordX = null;
	private JTextField jTextFieldLeftCoordY = null;
	private JLabel jLabelRightCoordX = null;
	private JLabel jLabelRightCoordY = null;
	private JTextField jTextFieldRightCoordX = null;
	private JTextField jTextFieldRightCoordY = null;
	
	private PlugInContext context = null;
	
	public ZoomToAreaPanel(PlugInContext context) {
		
		super();
		this.context  = context;
        initialize();
        
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.grid.languages.GridOptionsPaneli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GridOptionsPanel",bundle2);
    }

	private void initialize() {

		this.setLayout(new GridBagLayout());
		
		this.add(getJPanelLeftPoint(),          
       		 new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,        			
               new Insets(0, 5, 0, 5), 0, 0));  
		
		this.add(getJPanelRightPoint(),          
	       		 new GridBagConstraints(1, 0, 1, 1, 0.5, 0.5,
	               GridBagConstraints.CENTER, GridBagConstraints.BOTH,        			
	               new Insets(0, 5, 0, 5), 0, 0));  
	}
	
	private JPanel getJPanelLeftPoint()
    {
        if (jPanelLeftPoint == null)
        {
        	jPanelLeftPoint = new JPanel(new GridBagLayout());
            
        	jPanelLeftPoint.setBorder(BorderFactory.createTitledBorder(I18N.get("ZoomToAreaPlugIn", "zoomtoarea.panel.leftpoint.titulo"))); 
                       
        	jLabelLeftCoordX = new JLabel("", JLabel.CENTER);
            jLabelLeftCoordX.setText(I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.coordinatex"));
            jLabelLeftCoordY  = new JLabel("", JLabel.CENTER);
            jLabelLeftCoordY.setText(I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.coordinatey"));
                    
            jPanelLeftPoint.add(jLabelLeftCoordX, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelLeftPoint.add(jLabelLeftCoordY, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        	jPanelLeftPoint.add(getJTextFieldLeftCoordX(), 
                    new GridBagConstraints(1, 0, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
        	jPanelLeftPoint.add(getJTextFieldLeftCoordY(), 
                    new GridBagConstraints(1, 1, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            EdicionUtils.crearMallaPanel(2, 4, jPanelLeftPoint, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0);
            
        }
        return jPanelLeftPoint;
    }

	private Component getJTextFieldLeftCoordX() {
		
		if (jTextFieldLeftCoordX == null)
        {
			jTextFieldLeftCoordX = new TextField();
			jTextFieldLeftCoordX.addKeyListener(new KeyAdapter()
	        {
		           public void keyTyped(KeyEvent e)
		           {
		              char caracter = e.getKeyChar();
		              // Verificar si la tecla pulsada no es un digito
		              if(((caracter < '0') ||
		                 (caracter > '9')) &&
		                 (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.'))
		              {
		            	  e.consume();  // ignorar el evento de teclado                 
		              }              
		        	  
		           }
		        });            
        }
		return jTextFieldLeftCoordX;
	}
	
	private Component getJTextFieldLeftCoordY() {

		if (jTextFieldLeftCoordY == null)
		{
			jTextFieldLeftCoordY = new TextField();
			jTextFieldLeftCoordY.addKeyListener(new KeyAdapter()
	        {
		           public void keyTyped(KeyEvent e)
		           {
		              char caracter = e.getKeyChar();
		              // Verificar si la tecla pulsada no es un digito
		              if(((caracter < '0') ||
		                 (caracter > '9')) &&
		                 (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.'))
		              {
		            	  e.consume();  // ignorar el evento de teclado                 
		              }              
		        	  
		           }
		        });

		}
		return jTextFieldLeftCoordY;
	}
	
	private JPanel getJPanelRightPoint()
    {
        if (jPanelRightPoint == null)
        {
        	jPanelRightPoint = new JPanel(new GridBagLayout());
            
        	jPanelRightPoint.setBorder(BorderFactory.createTitledBorder(I18N.get("ZoomToAreaPlugIn", "zoomtoarea.panel.rightpoint.titulo"))); 
                       
        	jLabelRightCoordX  = new JLabel("", JLabel.CENTER);
            jLabelRightCoordX.setText(I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.coordinatex"));
            jLabelRightCoordY = new JLabel("", JLabel.CENTER);
            jLabelRightCoordY.setText(I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.coordinatey"));
                    
            jPanelRightPoint.add(jLabelRightCoordX, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelRightPoint.add(jLabelRightCoordY, 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelRightPoint.add(getJTextFieldRightCoordX(), 
                    new GridBagConstraints(1, 0, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelRightPoint.add(getJTextFieldRightCoordY(), 
                    new GridBagConstraints(1, 1, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            EdicionUtils.crearMallaPanel(2, 4, jPanelRightPoint, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0);
            
        }
        return jPanelRightPoint;
    }
	
	private Component getJTextFieldRightCoordX() {

		if (jTextFieldRightCoordX  == null)
		{
			jTextFieldRightCoordX = new TextField();
			jTextFieldRightCoordX.addKeyListener(new KeyAdapter()
	        {
		           public void keyTyped(KeyEvent e)
		           {
		              char caracter = e.getKeyChar();
		              // Verificar si la tecla pulsada no es un digito
		              if(((caracter < '0') ||
		                 (caracter > '9')) &&
		                 (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.'))
		              {
		            	  e.consume();  // ignorar el evento de teclado                 
		              }              
		        	  
		           }
		        });
		}
		return jTextFieldRightCoordX;
	}
	
	private Component getJTextFieldRightCoordY() {

		if (jTextFieldRightCoordY  == null)
		{
			jTextFieldRightCoordY = new TextField();
			jTextFieldRightCoordX.addKeyListener(new KeyAdapter()
	        {
		           public void keyTyped(KeyEvent e)
		           {
		              char caracter = e.getKeyChar();
		              // Verificar si la tecla pulsada no es un digito
		              if(((caracter < '0') ||
		                 (caracter > '9')) &&
		                 (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != '.'))
		              {
		            	  e.consume();  // ignorar el evento de teclado                 
		              }              
		        	  
		           }
		        });			

		}
		return jTextFieldRightCoordY;
	}

	public void okPressed() {
		try {
			reportNothingToUndoYet();

			double xLeft = new Double(jTextFieldLeftCoordX.getText()).doubleValue();
			double yLeft = new Double(jTextFieldLeftCoordY.getText()).doubleValue();
			double xRight = new Double(jTextFieldRightCoordX.getText()).doubleValue();
			double yRight = new Double(jTextFieldRightCoordY.getText()).doubleValue();

			Envelope area = new Envelope(xLeft,xRight,yLeft,yRight);

			context.getLayerViewPanel().getViewport().zoom(area);
			
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}
	
	protected void reportNothingToUndoYet() {
        context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

	public String validateInput() {
		
		try {
			
			if(jTextFieldLeftCoordX.getText().equals("")){
        		String errorMessage = "\"" + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage03");
        		return errorMessage;
        	}
			
        	if(Double.isNaN(Double.parseDouble(jTextFieldLeftCoordX.getText()))){
        		String errorMessage = "\"" + jTextFieldLeftCoordX.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage02");
        		return errorMessage;
        	}
        	
            if (Double.parseDouble(jTextFieldLeftCoordX.getText()) < 0) { 
            	String errorMessage = "\"" + jTextFieldLeftCoordX.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage01");
            	
                return errorMessage;
            }
            
            if(jTextFieldLeftCoordY.getText().equals("")){
        		String errorMessage = "\"" + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage03");
        		return errorMessage;
        	}
			
            if(Double.isNaN(Double.parseDouble(jTextFieldLeftCoordY.getText()))){
        		String errorMessage = "\"" + jTextFieldLeftCoordY.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage02");
        		return errorMessage;
        	}
        	
            if (Double.parseDouble(jTextFieldLeftCoordY.getText()) < 0) { 
            	String errorMessage = "\"" + jTextFieldLeftCoordY.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage01");
            	
                return errorMessage;
            }
              
            if(jTextFieldRightCoordX.getText().equals("")){
        		String errorMessage = "\"" + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage03");
        		return errorMessage;
        	}
            
            if(Double.isNaN(Double.parseDouble(jTextFieldRightCoordX.getText()))){
        		String errorMessage = "\"" + jTextFieldRightCoordX.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage02");
        		return errorMessage;
        	}
        	            
            if (Double.parseDouble(jTextFieldRightCoordX.getText()) < 0) { 
            	String errorMessage = "\"" + jTextFieldRightCoordX.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage01");
            	
                return errorMessage;
            }
                       
            if(jTextFieldRightCoordY.getText().equals("")){
        		String errorMessage = "\"" + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage03");
        		return errorMessage;
        	}
            
            if(Double.isNaN(Double.parseDouble(jTextFieldRightCoordY.getText()))){
        		String errorMessage = "\"" + jTextFieldRightCoordY.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage02");
        		return errorMessage;
        	}
        	
            if (Double.parseDouble(jTextFieldRightCoordY.getText()) < 0) { 
            	String errorMessage = "\"" + jTextFieldRightCoordY.getText() +
                "\" " + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage01");
            	
                return errorMessage;
            }
            
            if(jTextFieldLeftCoordX.getText().equals(jTextFieldLeftCoordY.getText())
            		&& jTextFieldLeftCoordY.getText().equals(jTextFieldRightCoordX.getText()) 
            		&& jTextFieldRightCoordX.getText().equals(jTextFieldRightCoordY.getText())){
            	String errorMessage = "\"" + I18N.get("ZoomToAreaPlugIn","zoomtoarea.panel.errorMessage04");
            	
                return errorMessage;
            }
                        
        } catch (NumberFormatException e) {        	
        	
        	String errorMessage = "\"" + appContext.getI18nString(e.getMessage());
        	
            return errorMessage;
        }

		return null;
	}


	public void init() {
		// TODO Auto-generated method stub
		
	}

}
