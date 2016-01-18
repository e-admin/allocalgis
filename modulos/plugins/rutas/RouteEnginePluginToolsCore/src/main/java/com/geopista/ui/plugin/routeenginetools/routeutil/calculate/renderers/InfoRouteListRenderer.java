/**
 * InfoRouteListRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListModel;

import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.RouteStretchInterface;
import com.localgis.util.DescriptionUtils;
import com.vividsolutions.jump.I18N;


/**
 * @author javieraragon
 *
 */
public class InfoRouteListRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8580163221396842914L;
	
	public InfoRouteListRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

	
	 public Component getListCellRendererComponent(JList list,
	           Object value, int i, boolean isSelected, boolean cellHasFocus)
	    {
		 
		 if (isSelected) {
			 setBackground(list.getSelectionBackground());
			 setForeground(list.getSelectionForeground());
		 } else {
			 setBackground(list.getBackground());
			 setForeground(list.getForeground());
		 }
		 


		 this.setText(value.toString());
		 ResourceBundle rb =  (ResourceBundle)I18N.plugInsResourceBundle.get("routedescription");
		 ListModel model = list.getModel();
		 // Get List of beans
		 ArrayList<RouteStretchInterface> beanlist=new ArrayList<RouteStretchInterface>();
		 for (int u=0;u<model.getSize();u++)
		     beanlist.add((RouteStretchInterface) model.getElementAt(u));
		     
		 String htmlText = DescriptionUtils.getTextualDescription((RouteStretchInterface) value,beanlist, rb); 
//		 else if (value instanceof InfoRouteStretchBean){
//			InfoRouteStretchBean vInfo = (InfoRouteStretchBean)value;
//				
//			 htmlText = "<html><ul>" + I18N.get("routedescription","routeengine.route.description.dialog.info1");
//			 
//			 if (vInfo.getTypeStreet()!=null && !vInfo.getTypeStreet().equals("")){
//				 htmlText = htmlText + vInfo.getTypeStreet();
//			 } else{
//				 htmlText = htmlText + "<b>" + I18N.get("routedescription","routeengine.route.description.default.street.type") + "</b>";
//			 }
//			 
//			 if (vInfo.getNameStreet()!=null && !vInfo.getNameStreet().equals("")){
//				 htmlText = htmlText + " " + vInfo.getNameStreet();
//			 }
//			 
//			 if (vInfo.getLenthStreetMeters() > 0){
//				 htmlText = htmlText + I18N.get("routedescription","routeengine.route.description.dialog.info2");
//				 if (vInfo.getLenthStreetMeters() > 600){
//					 htmlText = htmlText + Double.toString(vInfo.getLenthStreetMeters()/1000) + " " + I18N.get("routedescription","routeengine.route.description.dialog.kilometres");
//				 } else{
//					 htmlText = htmlText + Double.toString(vInfo.getLenthStreetMeters()) + " " + I18N.get("routedescription","routeengine.route.description.dialog.metres");
//				 }
//			 }
//						 
//			 htmlText = htmlText + "</ul></html>";
//		 }
		 
		 this.setText("<html>"+htmlText+"</html>");
		 this.setFocusable(true);
		 this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				setBackground(Color.LIGHT_GRAY);
				setForeground(Color.RED);
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		 
//			@Override
//			public void focusLost(FocusEvent e) {
////				 setBackground(list.getBackground());
////				 setForeground(list.getForeground());
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				setBackground(Color.LIGHT_GRAY);
//				setForeground(Color.RED);
//			}
//		});
		 
		 return this;
		
//		 return new JLabel(value.toString());
		 
	    }

}
