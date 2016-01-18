/*
 * 
 * Created on 12-ago-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.plugin.print.PrintLayoutPlugIn;
import com.geopista.ui.plugin.print.images.IconLoader;
import com.vividsolutions.jump.I18N;
/**
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class NorthIconChooserDialog extends JDialog
{

	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(NorthIconChooserDialog.class);

boolean isOkpressed=false;
	private javax.swing.JPanel jContentPane = null;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private JPanel okCancelPanel = null;
	private JComboBox jComboBox = null;
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
	private JPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			okCancelPanel = new JPanel();
			okCancelPanel.add(getOkButton(), null);  // Generated
			okCancelPanel.add(getCancelButton(), null);  // Generated
		}
		return okCancelPanel;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			initCombo(jComboBox);
			jComboBox.setRenderer(new ListCellRenderer()
				{
				JLabel rep=new JLabel();
					public Component getListCellRendererComponent(JList arg0,
							Object arg1, int arg2, boolean arg3, boolean arg4)
					{
					ImageIcon oc=(ImageIcon) arg1;
					Image im=oc.getImage().getScaledInstance(oc.getIconWidth()*64/oc.getIconHeight(),64,Image.SCALE_SMOOTH);
					rep.setIcon(new ImageIcon(im));
					return (JLabel) rep;
					}
				});
		}
		return jComboBox;
	}
	public static Icon northSymbols[]=
	new Icon[]
	{
	IconLoader.icon("NORTE1.gif"),
	IconLoader.icon("NORTE2.gif"),
	IconLoader.icon("NORTE3.gif"),
	IconLoader.icon("NORTE4.gif"),
	IconLoader.icon("NORTE5.gif"),
	IconLoader.icon("NORTE6.gif")
	};
	
       	/**
	 * @param comboBox
	 */
	private void initCombo(JComboBox comboBox)
	{
	
	
	DefaultComboBoxModel cmodel=new DefaultComboBoxModel(northSymbols);
	comboBox.setModel(cmodel);
	}
		/**
	 * This method initializes urlTextField	
	 * 	
	 * @return com.geopista.util.UrlTextField	
	 */    
	
  	public static void main(String[] args)
	{
	NorthIconChooserDialog dlg=new NorthIconChooserDialog(null);
	dlg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	dlg.setVisible(true);
	
	}
	/**
	 * This is the default constructor
	 */
	public NorthIconChooserDialog(Frame parent) {
		super(parent);
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
	this.setTitle(I18N.get(PrintLayoutPlugIn.name,"printlayout.choosenorthdialogtitle"));
		this.setResizable(false);  // Generated
		this.setModal(true);  // Generated
		this.setSize(203, 177);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());  // Generated
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.SOUTH;  // Generated
			gridBagConstraints15.fill = java.awt.GridBagConstraints.NONE;  // Generated
			gridBagConstraints15.gridx = 0;  // Generated
			gridBagConstraints15.gridy = 2;  // Generated
			gridBagConstraints1.weightx = 1.0;  // Generated
			gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;  // Generated
			jContentPane.add(getOkCancelPanel(), gridBagConstraints15);  // Generated
			jContentPane.add(getJComboBox(), gridBagConstraints1);  // Generated
		}
		return jContentPane;
	}
	
public boolean isOkpressed()
{
return isOkpressed;
}
/**
 * @param northIcon
 */
public void setNorthIcon(int northIcon)
{
getJComboBox().setSelectedIndex(northIcon);
}
/**
 * @return
 */
public Icon getIconSelected()
{
return ((Icon)getJComboBox().getSelectedItem());

}
/**
 * @return
 */
public int getNumberIconSelected()
{

return getJComboBox().getSelectedIndex();
}



}  //  @jve:decl-index=0:visual-constraint="10,10"
