package com.geopista.ui.plugin.inventario.edit.compare.geometries;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.utilidades.JNumberTextField;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

@SuppressWarnings("unchecked")
public class CompareGeometriesDialog extends JDialog {

	private JPanel jContentPane = null;
	protected OKCancelPanel oKCancelPanel = null;
	protected boolean okPressed = false;
//	private JComboBox capasJComboBox;
//	private JComboBox superficieJComboBox;
	private JNumberTextField toleranciaJTextField;

	public CompareGeometriesDialog() {
		super();
		initialize();
	}

	public CompareGeometriesDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		initialize();
	}


	private void initialize() {

		this.setSize(300, 150);
		this.setLayout(new GridBagLayout());
		this.add(getFiltroPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.NORTHWEST, GridBagConstraints.NORTH,new Insets(0, 10, 0, 0), 0, 0));
		this.add(getOKCancelPanel(), new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.NORTHWEST, GridBagConstraints.SOUTH,new Insets(0, 10, 0, 0), 0, 0));
		this.setContentPane(getJContentPane());
		// this.getContentPane().add(getReferenciaCatastralPanel());
		this.setTitle(I18N.get(CompareGeometriesPlugIn.CompareGeometriesI18N,
				"CompareGeometriesDialog.BusquedaCompareGeometries"));

		this.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void componentShown(ComponentEvent arg0) {

				GUIUtil.centreOnScreen(CompareGeometriesDialog.this);

			}

		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getFiltroPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.NORTHWEST, GridBagConstraints.NORTH,new Insets(20, 20, 0, 5), 0, 0));
			jContentPane.add(getOKCancelPanel(), new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.SOUTH,new Insets(5, 5, 2, 5), 0, 0));
		}
		return jContentPane;
	}

	private JPanel getFiltroPanel() {
//		JLabel  capaJLabel = new JLabel(I18N.get(CompareGeometriesPlugIn.CompareGeometriesI18N,
//		"CompareGeometriesDialog.CapaJLabel"));
//		JLabel  superficieJLabel= new JLabel(I18N.get(CompareGeometriesPlugIn.CompareGeometriesI18N,
//		"CompareGeometriesDialog.SuperficieJLabel"));
		JLabel  toleranciaJLabel = new JLabel(I18N.get(CompareGeometriesPlugIn.CompareGeometriesI18N,
		"CompareGeometriesDialog.ToleranciaJLabel"));
		
		JLabel  toleranciaJLabel2 = new JLabel("%"); 
		
//		capasJComboBox = new JComboBox();
//		addLayers(layers);
//		superficieJComboBox = new JComboBox();
//		addSuperdicies();
//		toleranciaJTextField = new JNumberTextField();
		toleranciaJTextField= new JNumberTextField(JNumberTextField.REAL, new Long(9), true, 1);
		toleranciaJTextField.setSignAllowed(false);

		Dimension d = new Dimension(60, 20);
//		capasJComboBox.setSize(d);
//		capasJComboBox.setPreferredSize(d);
//		superficieJComboBox.setSize(d);
//		superficieJComboBox.setPreferredSize(d);
		toleranciaJTextField.setSize(d);
		toleranciaJTextField.setPreferredSize(d );
		toleranciaJTextField.setText("0,10");
		
		JPanel panel = new JPanel(new GridBagLayout());
//		panel.add(capaJLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(0, 10, 0, 0), 0, 0));
//		panel.add(capasJComboBox, new GridBagConstraints(1, 0, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(5, 10, 0, 0), 0, 0));
//		panel.add(superficieJLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(0, 10, 0, 0), 0, 0));
//		panel.add(superficieJComboBox, new GridBagConstraints(1, 1, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(5, 10, 0, 0), 0, 0));
		panel.add(toleranciaJLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(0, 10, 0, 0), 0, 0));
		panel.add(toleranciaJTextField, new GridBagConstraints(1, 1, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(5, 10, 0, 0), 0, 0));
		panel.add(toleranciaJLabel2, new GridBagConstraints(2, 1, 1, 1, 0, 0,GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,new Insets(0, 10, 0, 0), 0, 0));
		
		return panel;
	}

	
//    private void addSuperdicies() {
//    	DefaultComboBoxModel listModel = (DefaultComboBoxModel) getSuperficieJComboBox().getModel();
//    	listModel.addElement("Medición Oficial");
//    	listModel.addElement("Medición Catastral");
//    	listModel.addElement("Medición Registral");
//		
//	}

//	public void addLayers(Collection layers)
//    {
//        if(layers.isEmpty()) return;
//        DefaultComboBoxModel listModel = (DefaultComboBoxModel) getCapasJComboBox().getModel();
//        Iterator layersIterator = layers.iterator();
//        while(layersIterator.hasNext()){
//            Layer currentLayer = (Layer) layersIterator.next();
//            listModel.addElement(currentLayer);
//        }        
//        
//        
//    }
	/**
	 * @return the oKCancelPanel
	 */
	public OKCancelPanel getOKCancelPanel() {
		if (oKCancelPanel == null) {
			oKCancelPanel = new OKCancelPanel();
			oKCancelPanel.setBounds(new Rectangle(105, 74, 206, 36));
			oKCancelPanel
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (oKCancelPanel.wasOKPressed()) {

								setOkPressed(true);
								setVisible(false);
							}
							setVisible(false);
						}
					});

		}
		return oKCancelPanel;
	}

	/**
	 * @return the okPressed
	 */
	public boolean wasOkPressed() {
		return okPressed;
	}

	/**
	 * @param okPressed
	 *            the okPressed to set
	 */
	protected void setOkPressed(boolean okPressed) {
		this.okPressed = okPressed;
	}

//	public JComboBox getCapasJComboBox() {
//		return capasJComboBox;
//	}
//
//	public void setCapasJComboBox(JComboBox capasJComboBox) {
//		this.capasJComboBox = capasJComboBox;
//	}

//	public JComboBox getSuperficieJComboBox() {
//		return superficieJComboBox;
//	}
//
//	public void setSuperficieJComboBox(JComboBox superficieJComboBox) {
//		this.superficieJComboBox = superficieJComboBox;
//	}

	public com.geopista.app.utilidades.JNumberTextField getToleranciaJTextField() {
		return toleranciaJTextField;
	}

	public void setToleranciaJTextField(com.geopista.app.utilidades.JNumberTextField toleranciaJTextField) {
		this.toleranciaJTextField = toleranciaJTextField;
	}

}
