/**
 * UrlTextField.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.ui.images.IconLoader;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class UrlTextField extends JPanel
{

	private JButton viewButton = null;
	private JTextField jTextField = null;
	PropertyChangeSupport prop_suport=new PropertyChangeSupport(this);
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton();
			viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			viewButton.setIcon(IconLoader.icon("SmallMagnify.gif"));  // Generated
			viewButton.setVisible(false);
			viewButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					launchReference();
				}
			});
		}
		return viewButton;
	}
	/**
	 * 
	 */
	protected void launchReference()
	{
	GeopistaBrowser.openURL(getJTextField().getText());
	}
	/** intercepts INSERT key to allow INSERT-REPLACE MODE **/
	   static void setOverrideAction(JTextComponent textComponent) {
	        TextAction action = new  DefaultEditorKit.DefaultKeyTypedAction() {
			    public void actionPerformed(ActionEvent e) {
			        JTextComponent target = getTextComponent(e);
			        if ((target != null) && (e != null)) {
			            if ((! target.isEditable()) || (! target.isEnabled())) {
			                return;
			            }
			            String content = e.getActionCommand();
			            int mod = e.getModifiers();
			            if ((content != null) && (content.length() > 0) &&
			                ((mod & ActionEvent.ALT_MASK) == (mod & ActionEvent.CTRL_MASK))) {
			                char c = content.charAt(0);
			                if ((c >= 0x20) && (c != 0x7F)) {
			                    if (target.getSelectionStart() == target.getSelectionEnd()) {
			                        target.setSelectionEnd(target.getSelectionStart() + 1);
			                    }
			                    target.replaceSelection(content);
			                }
			            }
			        }
			    }
		   };
	        ActionMap actionMap = new ActionMap();
	        ActionMap parentActionMap = textComponent.getActionMap();
	        actionMap.setParent(parentActionMap);
	       
	         
	        for (int i = 0; i < actionMap.allKeys().length; i++) {
				
				
				Object key = (Object) actionMap.allKeys()[i];
				
			
	            if (parentActionMap.get(key) instanceof
	                DefaultEditorKit.DefaultKeyTypedAction) {
	                actionMap.put(key, action);
	            }
	        }
	        textComponent.setActionMap(actionMap);
	    }
	   
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			//setOverrideAction(jTextField);
			
			jTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0)
				{
					

				}

				public void insertUpdate(DocumentEvent arg0)
				{
					updateURLButton();
				//	prop_suport.firePropertyChange("value",);
			// Ignoro porqué no funciona el PropertySupport
PropertyChangeListener prolst[] = ((PropertyChangeListener[])prop_suport.getPropertyChangeListeners("value"));
for (int i = 0; i < prolst.length; i++)
	{
	PropertyChangeListener listener = prolst[i];
	listener.propertyChange(new PropertyChangeEvent(this,"value",getText(),getText()));
	}

				}

				public void removeUpdate(DocumentEvent arg0)
				{
					updateURLButton();
//					prop_suport.firePropertyChange("value",getText(),getText());
					PropertyChangeListener prolst[] = ((PropertyChangeListener[])prop_suport.getPropertyChangeListeners("value"));
					for (int i = 0; i < prolst.length; i++)
						{
						PropertyChangeListener listener = prolst[i];
						listener.propertyChange(new PropertyChangeEvent(this,"value",getText(),getText()));
						}
				}
			});
		}
		return jTextField;
	}
     /**
	 *Actualiza el botón en caso de que se pueda interpretar el texto
	 *como un URL 
	 */
	protected void updateURLButton()
	{
		String texto= getJTextField().getText();
		
		boolean file=checkFile(texto);
		boolean url = checkURL(texto);
		if (file || url)
			getViewButton().setVisible(true);
		else
			getViewButton().setVisible(false);
	}
	private boolean checkFile(String texto)
	{
		File file=new File(texto);
		return file.exists();
	}
	private boolean checkFileAccess(String texto)
	{
		File file=new File(texto);
		return file.canRead();
	}
	private boolean checkURLAccess(String texto)
	{
		InputStream stream=null;
		try
		{
		stream = new URL(texto).openStream();
		} catch (MalformedURLException e)
		{
		return false;
		} catch (IOException e)
		{
		return false;
		}
		
		if (stream==null)
		try
		{
		stream.close();
		} catch (IOException e1)
		{
		return false;
		}
		
		return true;
	}
	private boolean checkURL(String texto)
	{
		try
		{
		URL url=new URL(texto);
		} catch (MalformedURLException e)
		{
		return false;
		}
		return true;
	}
		public static void main(String[] args)
	{
   		JDialog dg=new JDialog();
   		dg.setBounds(100,100,500,300);
   		dg.setContentPane(new UrlTextField());
   		dg.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   		dg.setModal(true);
   		dg.show();
   		
	}
	/**
	 * This is the default constructor
	 */
	public UrlTextField() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());  // Generated
		this.setSize(300,200);
		gridBagConstraints1.gridx = 0;  // Generated
		gridBagConstraints1.gridy = 0;  // Generated
		gridBagConstraints1.weightx = 1.0;  // Generated
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
		gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);  // Generated
		gridBagConstraints2.gridx = 1;  // Generated
		gridBagConstraints2.gridy = 0;  // Generated
		gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);  // Generated
		this.add(getJTextField(), gridBagConstraints1);  // Generated
		this.add(getViewButton(), gridBagConstraints2);  // Generated
	}
	public void setText(String text)
	{
		getJTextField().setText(text);
	}
	public String getText()
	{
		return getJTextField().getText();
	}
	public void setColumns(int cols)
	{
		getJTextField().setColumns(cols);
	}
	public void setValue(String val)
	{
		getJTextField().setText(val);
	}
	public void addPropertyChangeListener(String prop,PropertyChangeListener list)
	{
		this.prop_suport.addPropertyChangeListener(prop,list);
	}
	public void setEditable(boolean editable)
	{
		getJTextField().setEditable(editable);
	}
	public void setBorder(Border bd)
	{
		getJTextField().setBorder(bd);
	}
	public void setEnabled(boolean enabled)
	{
		getJTextField().setEnabled(enabled);
	}
	
	public boolean isEnabled()
	{
		return getJTextField().isEnabled();
	}
	public boolean isEditable()
	{
		return getJTextField().isEditable();
	}
		
	public Document getDocument()
	{
	return jTextField.getDocument();
	}
	public Dimension getMinimumSize()
	{
	return new Dimension(250,super.getPreferredSize().height);
	}
}
