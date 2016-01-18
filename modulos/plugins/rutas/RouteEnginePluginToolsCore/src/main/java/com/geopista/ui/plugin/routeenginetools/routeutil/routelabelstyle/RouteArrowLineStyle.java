/**
 * RouteArrowLineStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.arrowimages.ArrowIconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.renderer.style.LineStringSegmentStyle;

/**
 * @author javieraragon
 *
 */
public class RouteArrowLineStyle extends LineStringSegmentStyle{

	private final static double SMALL_ANGLE = 10;
    private final static double MEDIUM_ANGLE = 30;


    private final static double MEDIUM_LENGTH = 10;
    private final static double LARGE_LENGTH = 15;
    private boolean filled;
    private double finAngle;
    protected double finLength;
    private ImageIcon imageIcon;
    public Viewport myViewport = null;
    public Graphics2D mygraphics2D  = null;
    
	
	public RouteArrowLineStyle(String name, Icon icon, double angle, double length,
			Viewport viewport, Graphics2D graphics) {
		super(name, icon);
		// TODO Auto-generated constructor stub
		this.imageIcon = (ImageIcon) icon;
        this.finAngle = angle;
        this.finLength = length;
        this.filled = true;
        this.myViewport = viewport;
    	this.mygraphics2D = graphics;
	}

	public void paintFlecha(Coordinate p0, Coordinate p1){
		
	}
	

    /**
     * @param finAngle degrees
     * @param finLength pixels
     */
    
    
    public void paintLineString(LineString lineString, Viewport viewport,
    		Graphics2D graphics) throws Exception {

    	this.myViewport = viewport;
    	this.mygraphics2D = graphics;
    	Coordinate p0 = lineString.getCoordinateN(0);
    	Coordinate p1 = lineString.getCoordinateN(lineString.getNumPoints() - 1);
    	paint(viewport.toViewPoint(new Point2D.Double(p0.x, p0.y)),
    			viewport.toViewPoint(new Point2D.Double(p1.x, p1.y)), viewport,
    			graphics);
    	    	
//    	for (int i = 0; i < lineString.getNumPoints() - 1; i++) {
//    		paint(lineString.getCoordinateN(i),
//    				lineString.getCoordinateN(i + 1),
//    				viewport, graphics);
//    	}
    }

    public void paint(Coordinate p0, Coordinate p1, Viewport viewport,
    		Graphics2D graphics) throws Exception {
//    	paint(viewport.toViewPoint(new Point2D.Double(p0.x, p0.y)),
//    			viewport.toViewPoint(new Point2D.Double(p1.x, p1.y)), viewport,
//    			graphics);
    }
    

    @Override
    public void paint(Feature feature,Graphics2D g,IViewport viewport){
    	if (feature.getSchema().hasAttribute("regulacionTrafico") && 
    			feature.getSchema().hasAttribute("pintadaRegulacionTrafico") ){
    		// Si que pintamos por que tiene trafficRegulation
    		

    		// Obtenemos si se quiere pintar la feature.
    		boolean painted = false;
    		try{
    			int i = (Integer) feature.getAttribute("pintadaRegulacionTrafico");
    			if (i==1){
    				painted = true;
    			} else{
    				painted = false;
    			}
    		}catch (Exception e) {
    			e.printStackTrace();
    			painted = false;
			}
    		
    		if (painted){
    			// Intentamos Obtener el TrafficRegulation
    			
    	        g.setColor(lineColorWithAlpha);
    	        g.setStroke(stroke);
    	        
    			String regulation = "";
    			try{
    				if (feature.getAttribute("regulacionTrafico") instanceof String){
    					regulation = (String) feature.getAttribute("regulacionTrafico");
    				} else if (feature.getAttribute("regulacionTrafico") instanceof StreetTrafficRegulation){
    					regulation = ((StreetTrafficRegulation) feature.getAttribute("regulacionTrafico")).toString();
    				}

    			}catch (Exception e) {

    			}

    			LineString line = null;
    			if (feature.getGeometry() instanceof LineString ){
    				line = (LineString) feature.getGeometry();
    			}
    			if (line != null){
    				
    				Coordinate p0 = line.getCoordinateN(0);
    				Coordinate p1 = line.getCoordinateN(line.getNumPoints() - 1);
    				
    				Point2D point0 = new Point2D.Double(p0.x, p0.y);
    				Point2D point1 = new Point2D.Double(p1.x, p1.y);

    		        if (p0.equals(p1)) {
    		        	return;
    		        }
    		        
    				GeneralPath directArrowHead  = null;
    				GeneralPath invertArrowHead  = null;
    				
    				if (regulation.equals(StreetTrafficRegulation.DIRECT.toString())){
//    					  directArrowHead = arrowhead(point0, point1, SMALL_ANGLE, LARGE_LENGTH);
    					try {
    						paint(viewport.toViewPoint(point0),
    								viewport.toViewPoint(point1), viewport,
    								g);
    					} catch (NoninvertibleTransformException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				} else if (regulation.equals(StreetTrafficRegulation.BIDIRECTIONAL.toString())){
    					try {
    						paint(viewport.toViewPoint(point0),
    								viewport.toViewPoint(point1), viewport,
    								g);
    						paint(viewport.toViewPoint(point1),
    								viewport.toViewPoint(point0), viewport,
    								g);
    					} catch (NoninvertibleTransformException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
//    					invertArrowHead = arrowhead(point1, point0, SMALL_ANGLE, LARGE_LENGTH);
//    					directArrowHead = arrowhead(point0, point1, SMALL_ANGLE, LARGE_LENGTH);
    				} else if (regulation.equals(StreetTrafficRegulation.INVERSE.toString())){
    					try {
							paint(viewport.toViewPoint(point1),
									viewport.toViewPoint(point0), viewport,
									g);
						} catch (NoninvertibleTransformException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//    					invertArrowHead = arrowhead(point1, point0, SMALL_ANGLE, LARGE_LENGTH);
    				} 

    				if (directArrowHead != null){
    					if (filled) {
    						directArrowHead.closePath();
    						g.fill(directArrowHead);
    					}
    					g.draw(directArrowHead);
    				}

    				if(invertArrowHead != null){
    					if (filled) {
    						invertArrowHead.closePath();
    						g.fill(invertArrowHead);
    					}
    					g.draw(invertArrowHead);
    				}
    			}
    		}
    	}
    	
    }
    
	@Override
    public void paint(Point2D p0, Point2D p1, IViewport viewport,
        Graphics2D graphics) throws NoninvertibleTransformException {
        if (p0.equals(p1)) {
            return;
        }

        graphics.setColor(lineColorWithAlpha);
        graphics.setStroke(stroke);
   
    
        

        GeneralPath directArrowHead = arrowhead(p0, p1, finLength, finAngle);
//        GeneralPath invertArrowHead = arrowhead(p1, p0, finLength, finAngle);

        if (filled) {
            directArrowHead.closePath();
            graphics.fill(directArrowHead);
            
//            invertArrowHead.closePath();
//            graphics.fill(invertArrowHead);
        }

        //#fill isn't affected by line width, but #draw is. Therefore, draw even
        //if we've already filled. [Jon Aquino]
        graphics.draw(directArrowHead);
//        graphics.draw(invertArrowHead);
//        graphics.drawImage(this.imageIcon.getImage(), AffineTransform.getRotateInstance(p0.getX(), p0.getY(), p1.getX(), p1.getY()), new image() );
//        graphics.drawImage(this.imageIcon.getImage(),imageFin(p1, p0, this.finLength, this.finAngle), new image() );
        
        
    }
	
	public void paintDirect(Coordinate p0, Coordinate p1, GeopistaLayer layer) throws NoninvertibleTransformException {
			Viewport viewport = this.myViewport;
			Graphics2D graphics = this.mygraphics2D;
			
			

	        if (p0.equals(p1)) {
	            return;
	        }
	      

//	        graphics.setColor(  
//	    	        GUIUtil.alphaColor(layer.getBasicStyle()
//	                        .getLineColor(),
//	                        layer.getBasicStyle().getAlpha()));
//	        graphics.setStroke(new BasicStroke(layer.getBasicStyle().getLineWidth(),
//	                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
//	        graphics.setColor(lineColorWithAlpha);
//	        graphics.setStroke(stroke);

	        
	        GeneralPath directArrowHead = arrowhead(new Point2D.Double(p0.x,p0.y), new Point2D.Double(p1.x,p1.y), finLength, finAngle);
//	        GeneralPath directArrowHead = arrowhead(viewport.toViewPoint(new Point2D.Double(p0.x,p0.y)), 
//	        		viewport.toViewPoint(new Point2D.Double(p1.x,p1.y)), finLength, finAngle);
//	        GeneralPath invertArrowHead = arrowhead(p1, p0, finLength, finAngle);

	        if (filled) {
	            directArrowHead.closePath();
	            graphics.fill(directArrowHead);
	            
//	            invertArrowHead.closePath();
//	            graphics.fill(invertArrowHead);
	        }

	        //#fill isn't affected by line width, but #draw is. Therefore, draw even
	        //if we've already filled. [Jon Aquino]
	        graphics.draw(directArrowHead);
//	        graphics.draw(invertArrowHead);
//	        graphics.drawImage(this.imageIcon.getImage(), AffineTransform.getRotateInstance(p0.getX(), p0.getY(), p1.getX(), p1.getY()), new image() );
//	        graphics.drawImage(this.imageIcon.getImage(),imageFin(p1, p0, this.finLength, this.finAngle), new image() );
	    }
	
	public void paintBidirect(Coordinate p0, Coordinate p1, GeopistaLayer layer) throws NoninvertibleTransformException{
		paintInvert(p0, p1, layer);
		paintDirect(p0, p1, layer);
	}
	
	public void paintInvert(Coordinate p0, Coordinate p1, GeopistaLayer layer) throws NoninvertibleTransformException {
		
		Viewport viewport = this.myViewport;
		Graphics2D graphics = this.mygraphics2D;
		
	        if (p0.equals(p1)) {
	            return;
	        }

	        graphics.setColor(  
	    	        GUIUtil.alphaColor(layer.getBasicStyle()
	                        .getLineColor(),
	                        layer.getBasicStyle().getAlpha()));
	        graphics.setStroke(new BasicStroke(layer.getBasicStyle().getLineWidth(),
	                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

//	        GeneralPath directArrowHead = arrowhead(p0, p1, finLength, finAngle);
	        GeneralPath invertArrowHead = arrowhead(
	        		viewport.toViewPoint(new Point2D.Double(p1.x,p1.y)),
	        		viewport.toViewPoint(new Point2D.Double(p0.x,p0.y)),
	        		finLength, finAngle);

	        if (filled) {
//	            directArrowHead.closePath();
//	            graphics.fill(directArrowHead);
	            
	            invertArrowHead.closePath();
	            graphics.fill(invertArrowHead);
	        }

	        //#fill isn't affected by line width, but #draw is. Therefore, draw even
	        //if we've already filled. [Jon Aquino]
//	        graphics.draw(directArrowHead);
	        graphics.draw(invertArrowHead);
//	        graphics.drawImage(this.imageIcon.getImage(), AffineTransform.getRotateInstance(p0.getX(), p0.getY(), p1.getX(), p1.getY()), new image() );
//	        graphics.drawImage(this.imageIcon.getImage(),imageFin(p1, p0, this.finLength, this.finAngle), new image() );
	    }
	
	private class image implements ImageObserver{

		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

    /**
     * @param tail the tail of the whole arrow; just used to determine angle
     * @param finLength required distance from the tip to each fin's tip
     */
    public GeneralPath arrowhead(Point2D p0, Point2D p1,
        double finLength, double finAngle)
    {
      Point2D mid = new Point2D.Float( (float) ((p0.getX() + p1.getX()) / 2),
                                       (float) ((p0.getY() + p1.getY()) / 2) );
        GeneralPath arrowhead = new GeneralPath();
        Point2D finTip1 = fin(p1, p0, finLength, finAngle);
        Point2D finTip2 = fin(p1, p0, finLength, -finAngle);
        arrowhead.moveTo((float) finTip1.getX() , (float) finTip1.getY() );
        arrowhead.lineTo((float) p1.getX(), (float) p1.getY());
        arrowhead.lineTo((float) finTip2.getX(), (float) finTip2.getY());

        return arrowhead;
    }

    private Point2D fin(Point2D shaftTip, Point2D shaftTail, double length,
        double angle) {
        double shaftLength = shaftTip.distance(shaftTail);
        Point2D finTail = shaftTip;
        Point2D finTip = GUIUtil.add(GUIUtil.multiply(GUIUtil.subtract(
                        shaftTail, shaftTip), length / shaftLength), finTail);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate((angle * Math.PI) / 180, finTail.getX(),
            finTail.getY());

        return affineTransform.transform(finTip, null);
    }
    
    private AffineTransform imageFin(Point2D p0, Point2D p1, double length,
            double angle) {
                       
            double ang1 = Math.atan2( (p1.getY() - p0.getY()), (p1.getX() - p0.getX()));
            
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate((ang1 * Math.PI));
//            AffineTransform.getRotateInstance(theta);
            affineTransform.translate(p0.getX(), p0.getY());
            return affineTransform;
        }
	
    public static class Direct extends RouteArrowLineStyle {
        public Direct(Viewport viewport, Graphics2D graphics2d) {        	
            super(
            	I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Open"),
            	ArrowIconLoader.directArrowIcon(),SMALL_ANGLE, LARGE_LENGTH,
            	viewport, graphics2d );
        }
    }

    public static class Inverse extends RouteArrowLineStyle {
        public Inverse(Viewport viewport, Graphics2D graphics2d) {
            super(
            	I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Solid"), 
            	ArrowIconLoader.inverseArrowIcon(),SMALL_ANGLE, LARGE_LENGTH,
            	viewport, graphics2d);
        }
    }

    public static class BiDirect extends RouteArrowLineStyle {
        public BiDirect(Viewport viewport, Graphics2D graphics2d) {
            super(
            	I18N.get("ui.renderer.style.ArrowLineStringSegmentStyle.Segment-Mid-Arrow-Solid-Narrow"),
                ArrowIconLoader.biDirectArrowIcon(),MEDIUM_ANGLE , LARGE_LENGTH,
               	viewport,graphics2d);
        }
    }
	
}

