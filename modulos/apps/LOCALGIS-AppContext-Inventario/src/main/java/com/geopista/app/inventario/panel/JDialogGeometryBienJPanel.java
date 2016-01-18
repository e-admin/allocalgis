/**
 * JDialogGeometryBienJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.protocol.inventario.Const;
import com.geopista.util.ApplicationContext;

public class JDialogGeometryBienJPanel extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3825062514297960755L;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	private InventarioJPanel inventarioJPanel;
	private JButton okButton;
	private JButton appendButton;
	Logger logger = Logger.getLogger(JDialogGeometryBienJPanel.class);
	public JDialogGeometryBienJPanel(JFrame mainFrame, String i18nString,InventarioJPanel inventarioPanel) {
		super(mainFrame,i18nString);
		this.inventarioJPanel = inventarioPanel; 
		initialize();
	}

	public void setEnabledButtons(boolean locked){
		okButton.setEnabled(!locked);
		appendButton.setEnabled(!locked);
	}
	
	private void initialize() {
		this.addWindowListener(new WindowAdapter(){  
    		public void windowClosing(WindowEvent we){  
    				aplicacion.getBlackboard().remove(Const.ASSOCIATE_FEATURES_BIENES);
    		     }  
    		}); 
		aplicacion.getBlackboard().put(Const.ASSOCIATE_FEATURES_BIENES, this);
		JLabel label = new JLabel(aplicacion.getI18nString("inventario.nonmodal.associatebienesgeom.text"));
		label.setHorizontalAlignment(JLabel.CENTER);
		JButton closeButton = new JButton(aplicacion.getI18nString("inventario.nonmodal.associatebienesgeom.cancel"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				aplicacion.getBlackboard().remove(Const.ASSOCIATE_FEATURES_BIENES);
					inventarioJPanel.desassociatedata();
				}
			});
		okButton = new JButton(aplicacion.getI18nString("inventario.nonmodal.associatebienesgeom.ok"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				aplicacion.getBlackboard().remove(Const.ASSOCIATE_FEATURES_BIENES);
				try{
					inventarioJPanel.associatedata(true);
				}catch(Exception e2){
					logger.error("Error al asociar las geometrias a los bienes",e2);
				}
				}
			});
		appendButton = new JButton(aplicacion.getI18nString("inventario.nonmodal.associatebienesgeom.add"));
		appendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				AppContext.getApplicationContext().getBlackboard().remove(Const.ASSOCIATE_FEATURES_BIENES);
				try{
					inventarioJPanel.associatedata(false);
				}catch(Exception e2){
					logger.error("Error al asociar las geometrias a los bienes",e2);
					e2.printStackTrace();
				}
				}
			});
		JPanel contentPane = new JPanel(new BorderLayout());
		JPanel subContent = new JPanel(new BorderLayout());
		subContent.setBorder(BorderFactory.createEmptyBorder(0,20,10,20));
		subContent.add(okButton,BorderLayout.WEST);
		subContent.add(appendButton,BorderLayout.CENTER);
		subContent.add(closeButton,BorderLayout.EAST);
		contentPane.add(label, BorderLayout.CENTER);
		contentPane.add(subContent, BorderLayout.PAGE_END);
		contentPane.setOpaque(true);
		if (inventarioJPanel.getMapaJPanel()!=null){
			if (inventarioJPanel.getMapaJPanel().getGeopistaEditor()
					.getSelectionManager().getFeatureSelection()
					.getFeaturesWithSelectedItems().size() != 0
					&& inventarioJPanel.getBienesPanel().getBienesSeleccionados() != null 
					&& inventarioJPanel.getBienesPanel().getBienesSeleccionados().size() > 0)
	
			{
				setEnabledButtons(false);
			} else {
				setEnabledButtons(true);
			}
		}
		setContentPane(contentPane);
		setSize(new Dimension(350, 100));
		setLocationRelativeTo(inventarioJPanel.getTipoBienesPanel());
		
	}
	
}
