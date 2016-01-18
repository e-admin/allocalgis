package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingUtils;
import com.localgis.app.gestionciudad.utils.LocalGISObraCivilUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class SelectRadiusDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2489464477454831309L;
	
	
	private OKCancelPanel okCancelPanel = null;
	private JPanel rootPanel = null;
	private JComboBox radiusComboBox = null;
	
	private String[] radius = {"10.0","20.0","50.0","100.0","200.0","300.0","500.0","700.0","900.0","1000.0","2000.0","3000.0",
			"5000.0","10000.0"};
	private NumberFormat radiusDoubelFormat = new DecimalFormat("#.##");
	private double selectedRadius = -1.0;
	
	public SelectRadiusDialog(Component parentComponent){
		super((Frame) LocalGISObraCivilUtils.getWindowForComponent(parentComponent),
				I18N.get("geomarketing","localgis.geomarketing.panels.radius.dialogtittle"),true);
		GeoMarketingUtils.inicializarIdiomaGeoMarketing();
		initialize();
		this.pack();
		this.setLocationRelativeTo(parentComponent);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args){
		System.err.println("Prueba");
		while (true){
			SelectRadiusDialog dialog = new SelectRadiusDialog(AppContext.getApplicationContext().getMainFrame());
			if (dialog.wasOKPressed()){
				System.out.println(dialog.getSelectedRadius());
			}
		}
//		System.exit(-1);
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private JPanel getRootPanel(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
			
			rootPanel.add(new JLabel(I18N.get("geomarketing","localgis.geomarketing.panels.radius.label")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			rootPanel.add(this.getRadiusComboBox(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return rootPanel;
	}
	
	
	private JComboBox getRadiusComboBox(){
		if (radiusComboBox == null){
			radiusComboBox = new JComboBox(this.radius);
			radiusComboBox.setEditable(true);
			radiusComboBox.setSelectedItem("20.0");
			}
		return radiusComboBox;
	}
	
	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		if (!allDataIscorrect()){
			return false;
		}
		return true; 
	}


	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private boolean allDataIscorrect() {
		
		DecimalFormat df = new DecimalFormat (  ) ; 
		df.setMaximumFractionDigits ( 2 ) ;  
		df.setMinimumFractionDigits ( 2 ) ; 
		df.setDecimalSeparatorAlwaysShown(false);
		
		if (this.radiusComboBox != null){
			if (radiusComboBox.getSelectedItem() != null){
				if (radiusComboBox.getSelectedItem() instanceof String  && !radiusComboBox.getSelectedItem().equals("")){
				try {
					selectedRadius = Double.valueOf((String) radiusComboBox.getSelectedItem());
					System.out.println(radiusDoubelFormat.format(selectedRadius));
					return true;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
			}
		}
		JOptionPane.showMessageDialog(this, "Radio no válido. Debe estar compuesto sólo de caracteres numéricos separados por un punto '.'");
		return false;
	}
	
	
	public double getSelectedRadius(){
		return selectedRadius;
	}

}
