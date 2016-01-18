package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2;
import com.vividsolutions.jump.I18N;

public class GeoMarketingDataJListRenderer extends JPanel implements ListCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2806711418732249276L;

	public GeoMarketingDataJListRenderer() {
        setOpaque(true);
//        setHorizontalAlignment(LEFT);
//        setVerticalAlignment(CENTER);
    }

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		JPanel panel = new JPanel();

		if (value instanceof GeoMarketingOT2){

			panel.setLayout(new GridBagLayout());			
			panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

			panel.add(newRendererLabel(((GeoMarketingOT2) value).getAttName()), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.BOTH, 
							new Insets(2, 2, 0, 2), 0, 0));
			panel.add(newRendererLabel(Integer.toString(((GeoMarketingOT2) value).getExternalValue())), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.BOTH, 
							new Insets(0, 2, 2, 2), 0, 0));
			
			if (isSelected) {
				if (((GeoMarketingOT2)value).getAttName().equals(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.totalitem"))){
					setPanelBackGroundForeGround(panel, Color.YELLOW, Color.RED, true);
				} else{
					setPanelBackGroundForeGround(panel,list.getSelectionBackground(), list.getSelectionForeground(), true);
				}
			} else {				
				if (((GeoMarketingOT2)value).getAttName().equals(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.totalitem"))){
					setPanelBackGroundForeGround(panel, list.getBackground(), Color.RED, false);
				} else{
					setPanelBackGroundForeGround(panel, list.getBackground(), list.getForeground(), false);
				}
			}
			panel.repaint();

		}
		
		
		
		return panel;
	}
	
	private void setPanelBackGroundForeGround(JPanel panel, Color selectionBackground,
			Color selectionForeground, boolean labelBold) {
		panel.setBackground(selectionBackground);
		panel.setForeground(selectionForeground);
		
		for(int i=0; i < panel.getComponents().length; i++){
			panel.getComponent(i).setBackground(selectionBackground);
			panel.getComponent(i).setForeground(selectionForeground);
			if (panel.getComponent(i) instanceof JLabel && labelBold){
				((JLabel)panel.getComponent(i)).setText(
						"<html><b>" +
						((JLabel)panel.getComponent(i)).getText() +
						"</html></b>");
			}
		}
	}

	private JLabel newRendererLabel(String text){
		JLabel label = new JLabel(text);
		label.setOpaque(true);
//		label.setHorizontalAlignment(JLabel.CENTER);
//        label.setVerticalAlignment(JLabel.CENTER);
				
		return label;
	}

	//	/**
	//	 * 
	//	 */
	//	private static final long serialVersionUID = 2806711418732249276L;
	//	protected String method;



	//	public Component getListCellRendererComponent(JList list, Object value,
	//			int index, boolean isSelected, boolean cellHasFocus) {
	//	
	//		  JLabel label = (JLabel)super.getListCellRendererComponent(
	//		            list,value,index, isSelected, cellHasFocus);
	//		  
	//		  try {
	//	            Method meth = value.getClass().getMethod(method,null);
	//	            if(meth != null) {
	//	                Object retval = meth.invoke(value,null);
	//	                label.setText(""+retval);
	//	            }
	//	        } catch (Exception ex) {
	//	            System.out.println("got an execption: " + ex);
	//	            ex.printStackTrace();
	//	        }
	//
	//	        return label;

	//		 if (isSelected) {
	//	            setBackground(list.getSelectionBackground());
	//	            setForeground(list.getSelectionForeground());
	//	        } else {
	//	            setBackground(list.getBackground());
	//	            setForeground(list.getForeground());
	//	        }
	//		 
	//		 setBackground(isSelected ? Color.red : Color.white);
	//         setForeground(isSelected ? Color.white : Color.black);
	//		
	//		if (value instanceof GeoMarketingOT2){
	//			this.setText(((GeoMarketingOT2)value).getAttributeName());
	//		}
	//		
	//		return this;

	//	}

//	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
//
//	public Component getListCellRendererComponent(JList list, Object value,
//			int index, boolean isSelected, boolean cellHasFocus) {
//		//	    Font theFont = null;
//		Color theForeground = null;
//		Icon theIcon = null;
//		String theText = null;
//
//		JLabel renderer = (JLabel) defaultRenderer
//		.getListCellRendererComponent(list, value, index, isSelected,
//				cellHasFocus);
//
//		//	    if (value instanceof Object[]) {
//		//	      Object values[] = (Object[]) value;
//		//	      theFont = (Font) values[0];
//		//	      theForeground = (Color) values[1];
//		//	      theIcon = (Icon) values[2];
//		//	      theText = (String) values[3];
//		//	    } else {
//		//	      theFont = list.getFont();
//		//	      theForeground = list.getForeground();
//		//	      theText = "";
//		//	    }
//		//	    if (!isSelected) {
//		//	      renderer.setForeground(theForeground);
//		//	    }
//		//	    if (theIcon != null) {
//		//	      renderer.setIcon(theIcon);
//		//	    }
////		renderer.setText(theText);
////		renderer.setFont(theFont);
//		
//		if (value instanceof GeoMarketingOT2){
//			renderer.setText(((GeoMarketingOT2)value).getAttributeName());
//		}
//		return renderer;
//	}
}



