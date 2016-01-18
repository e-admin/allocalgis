/**
 * URLImageChooserDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 29-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.components.UrlTextField;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class URLImageChooserDialog extends JDialog
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
	.getLog(URLImageChooserDialog.class);

	boolean isOkpressed=false;
	private javax.swing.JPanel jContentPane = null;

	private JButton browseButton = null;
	private JLabel imagePreviewLabel = null;
	private UrlTextField urlTextField = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JPanel jPanel = null;
	private JFileChooser fileChooser = null;  //  @jve:decl-index=0:visual-constraint="400,63"
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBrowseButton() {
		if (browseButton == null) {
			browseButton = new JButton();
			browseButton.setText("Browse");  // Generated
			browseButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getFileChooser().showOpenDialog(URLImageChooserDialog.this);
					File file=getFileChooser().getSelectedFile();
					try
					{
						URL url=file.toURL();
						getUrlTextField().setText(url.toString());
						setURLIcon(url);
					}
					catch (MalformedURLException e1)
					{
						// TODO Auto-generated catch block
						logger.error(
								"actionPerformed(java.awt.event.ActionEvent)",
								e1);
					}
				}
			});
		}
		return browseButton;
	}
	/**
	 * This method initializes urlTextField	
	 * 	
	 * @return com.geopista.util.UrlTextField	
	 */    
	private UrlTextField getUrlTextField() {
		if (urlTextField == null) {
			urlTextField = new UrlTextField();
			urlTextField.getDocument().addDocumentListener(new DocumentListener()
			{
				public void insertUpdate(DocumentEvent arg0)
				{
					// TODO Auto-generated method stub

				}

				public void removeUpdate(DocumentEvent arg0)
				{
					// TODO Auto-generated method stub

				}

				public void changedUpdate(DocumentEvent arg0)
				{
					try
					{
						setURLIcon(new URL(getUrlTextField().getText()));
					}
					catch (MalformedURLException e)
					{
						// TODO Auto-generated catch block
						logger.error("changedUpdate(DocumentEvent)", e);
					}
				}

			});

		}
		return urlTextField;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");  // Generated
			okButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					isOkpressed=true;
					setVisible(false);
				}
			});
		}
		return okButton;
	}
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");  // Generated
			cancelButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					setVisible(false);
				}
			});
		}
		return cancelButton;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getOkButton(), null);  // Generated
			jPanel.add(getCancelButton(), null);  // Generated
		}
		return jPanel;
	}
	/**
	 * This method initializes jFileChooser	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */    
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
		}
		return fileChooser;
	}
	/**
	 * This method initializes urlTextField	
	 * 	
	 * @return com.geopista.util.UrlTextField	
	 */    

	public static void main(String[] args)
	{
		URLImageChooserDialog dlg=new URLImageChooserDialog();
		dlg.setVisible(true);
	}
	/**
	 * This is the default constructor
	 */
	public URLImageChooserDialog() {
		super();
		initialize();
	}
	/**
	 * @param ge
	 */
	public URLImageChooserDialog(ImageFrame ge)
	{

		super((Frame) ge.getParent(),true);
		setURLIcon(ge.getIconURL());
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setResizable(true);  // Generated
		this.setModal(true);  // Generated
		this.setSize(650, 165);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			imagePreviewLabel = new JLabel();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());  // Generated
			gridBagConstraints10.gridx = 2;  // Generated
			gridBagConstraints10.gridy = 0;  // Generated
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.CENTER;  // Generated
			gridBagConstraints10.insets = new java.awt.Insets(40,5,5,5);  // Generated
			gridBagConstraints11.gridx = 0;  // Generated
			gridBagConstraints11.gridy = 0;  // Generated
			gridBagConstraints11.ipadx = 32;//64;  // Generated
			gridBagConstraints11.ipady = 32;//64;  // Generated
			gridBagConstraints11.gridheight = 3;  // Generated
			gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);  // Generated
			imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			imagePreviewLabel.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/BigWrench.gif")));  // Generated
			imagePreviewLabel.setPreferredSize(new java.awt.Dimension(64,64));  // Generated
			imagePreviewLabel.setMaximumSize(new java.awt.Dimension(64,64));  // Generated
			imagePreviewLabel.setMinimumSize(new java.awt.Dimension(64,64));  // Generated
			imagePreviewLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);  // Generated
			imagePreviewLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);  // Generated
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.SOUTH;  // Generated
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints12.gridx = 1;  // Generated
			gridBagConstraints12.gridy = 0;  // Generated
			gridBagConstraints12.ipadx = 150;  // Generated
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints12.insets = new java.awt.Insets(40,5,5,5);  // Generated
			gridBagConstraints15.gridx = 1;  // Generated
			gridBagConstraints15.gridy = 2;  // Generated
			jContentPane.add(imagePreviewLabel, gridBagConstraints11);  // Generated
			jContentPane.add(getJPanel(), gridBagConstraints15);  // Generated
			jContentPane.add(getBrowseButton(), gridBagConstraints10);  // Generated
			jContentPane.add(getUrlTextField(), gridBagConstraints12);  // Generated
		}
		return jContentPane;
	}
	public void setURLIcon(URL url)
	{
		try
		{
			ImageIcon icono=new ImageIcon(url);
			Icon icon = new ImageIcon(icono.getImage().getScaledInstance(100,100,Image.SCALE_FAST));
			imagePreviewLabel.setIcon(icon);
		}
		catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			logger.error("setURLIcon(URL)");
		}

	}
	public boolean isOkpressed()
	{
		return isOkpressed;
	}
	/**
	 * @return
	 * @throws MalformedURLException
	 */
	public URL getIconURL() throws MalformedURLException
	{

		return new URL(getUrlTextField().getText());
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
