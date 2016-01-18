/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.CardinalDirections;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.TurnRouteStreetchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.VirtualInfoRouteStretchBean;
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
		 String htmlText = I18N.get("routedescription","routeengine.route.description.dialog.no.stretch.info");
		 
		 if (value instanceof TurnRouteStreetchBean){
			 if (((TurnRouteStreetchBean)value).getTurnNode() != null){
				 htmlText = "<html><ul>" + ((TurnRouteStreetchBean)value).getDescription() + "</html></ul>";
			 } else{
				 htmlText = ((TurnRouteStreetchBean)value).getDescription();
			 }
		 } else if (value instanceof InfoRouteStretchBean){
			 InfoRouteStretchBean info = (InfoRouteStretchBean )value;
			
			 htmlText = "<html><ul>" + I18N.get("routedescription","routeengine.route.description.dialog.info1");
			 
			 if (info.getTypeStreet()!=null && !info.getTypeStreet().equals("")){
				 htmlText = htmlText + (info.getTypeStreet());
			 } else{
				 if (info.hasStreetEdges()){
					 htmlText = htmlText + " " + I18N.get("routedescription","routeengine.route.description.dialog.info1_2") + " ";
					 htmlText = htmlText + I18N.get("routedescription","routeengine.route.description.default.street.type") + " ";
				 }
			 }
			 
			 if (info.getNameStreet()!=null && !info.getNameStreet().equals("")){

				 htmlText = htmlText + " " + info.getNameStreet() + " ";
			 }
			 
			 DecimalFormat doubleLengthFormatter = new DecimalFormat("#0.0");
			 if (info.getLenthStreetMeters() >= 0){
				 htmlText = htmlText + I18N.get("routedescription","routeengine.route.description.dialog.info2");
				 if (info.getLenthStreetMeters() > 600){
					 try{
						 htmlText = htmlText + doubleLengthFormatter.format(info.getLenthStreetMeters() / 1000.0 ) + " "+ I18N.get("routedescription","routeengine.route.description.dialog.kilometres");
					 }catch (ArithmeticException e) {
						 htmlText = htmlText + (info.getLenthStreetMeters() / 1000.0 ) + " "+ I18N.get("routedescription","routeengine.route.description.dialog.kilometres");
					}
				 } else{
					 htmlText = htmlText + info.getLenthStreetMeters() + " " + I18N.get("routedescription","routeengine.route.description.dialog.metres");
				 }
			 }
			 
			 if (info instanceof VirtualInfoRouteStretchBean){
				 VirtualInfoRouteStretchBean vInfo = (VirtualInfoRouteStretchBean) info;
				 
				 if (vInfo.getStretchCardinalDirection()!=null){
					 CardinalDirections cardinalDirection = vInfo.getStretchCardinalDirection();
					 htmlText = htmlText + "<br>";
					 if (cardinalDirection.equals(CardinalDirections.NORTH)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.north");
					 } else if (cardinalDirection.equals(CardinalDirections.NORTHWEST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.northwest");
					 } else if (cardinalDirection.equals(CardinalDirections.WEST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.west");
					 } else if (cardinalDirection.equals(CardinalDirections.SOUTHWEST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.southwest");
					 } else if (cardinalDirection.equals(CardinalDirections.SOUTH)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.south");
					 } else if (cardinalDirection.equals(CardinalDirections.SOUTHEAST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.southeast");
					 } else if (cardinalDirection.equals(CardinalDirections.EAST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.east");
					 } else if (cardinalDirection.equals(CardinalDirections.NORTHEAST)){
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.northeast");
					 } else{
						 htmlText = htmlText +
						 I18N.get("routedescription","routeengine.route.description.dialog.info3") 
						 + I18N.get("routedescription","routeengine.route.description.dialog.no.cardinaldirection.defined");
					 }
				 }
				 
				 Object nextValue = null;
				 if (i+1 <list.getModel().getSize() ){
					 nextValue = list.getModel().getElementAt(i + 1);
					 if (					 nextValue != null && nextValue instanceof InfoRouteStretchBean){
						 InfoRouteStretchBean nextInfo = (InfoRouteStretchBean) nextValue;

						 if (nextInfo.getNameStreet()!=null && !nextInfo.getNameStreet().equals("")){
							 htmlText = htmlText + "  hacia ";
							 if (nextInfo.getTypeStreet()!=null && !nextInfo.getTypeStreet().equals("")){
								 htmlText = htmlText + nextInfo.getTypeStreet() + " "; 
							 } else{
								 if (nextInfo.hasStreetEdges()){
									 htmlText = htmlText + I18N.get("routedescription","routeengine.route.description.default.street.type") + " ";
								 }
							 }
							 htmlText = htmlText + nextInfo.getNameStreet() ;
						 }

					 }
				 }
				 
				 
			 }
			 
			 htmlText = htmlText + ".</ul></html>";
		 } 
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
		 
		 this.setText(htmlText);
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
