/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 28-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.legend;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.graphics.sld.UserStyle;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * Componente que muestra un thumbnail con la simbolización
 * @author juacas
 *
 */
public class SymbolizerPanel extends JPanel
{
	private static final int	BORDER	= 3;
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SymbolizerPanel.class);

	private Rule rule;
	private Symbolizer[] symbolizers;
	/**
	 * This is the default constructor
	 */
	public SymbolizerPanel()
	{
		super();
		initialize();
	}
	public SymbolizerPanel(Layer layer)
	{
	init(layer);
	initialize();
	}
	public void init(Layer layer)
	{
		setSymbolizers(new Vector()); // vacio
		SLDStyle style = (SLDStyle)layer.getStyle(SLDStyle.class);

		
		if (style != null)
		{
			UserStyle uStyle = style.getUserStyle(style.getCurrentStyleName());
			if (uStyle==null)
			{
				if (logger.isDebugEnabled())
				{
				logger.debug("init(layer = " + layer
						+ ") - Este estilo no tiene UserStyle: : uStyle = "
						+ uStyle + ", style = " + style);
				}

				return;
			}
			FeatureTypeStyle[] fts = uStyle.getFeatureTypeStyles();
 			
 			for (int k = 0; k < fts.length; k++)
 			{
 				
 				Rule[] rules = fts[k].getRules(); // distintas reglas de pintado
 				
 				for (int n = 0; n < rules.length; n++)
 				{
 					Rule r= rules[n];
 					
 					addSymbolizers(r.getSymbolizers());
 				}
 			}
		}
	}
	public SymbolizerPanel(Rule rule) {
		super();
		this.rule=rule;
		symbolizers=rule.getSymbolizers();
		initialize();
	}
	public SymbolizerPanel(Symbolizer[] symbs)
	{
		symbolizers=symbs;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
	}
	public void paintComponent(Graphics g) {
		int z = 0;
		int w = getWidth();
		int h = getHeight();
		int w2 = w / 2;
		int h2 = h / 2;
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(java.awt.SystemColor.window);
		g2.fillRect(0,0,getBounds().width-1, getBounds().height-1);
		g2.setColor(Color.GRAY);
		g2.drawRect(0,0,getBounds().width-1, getBounds().height-1);
	//	if (rule==null)return;
		try {
//			Symbolizer symbolizer;
//			
//		for (int i=0;i<rule.getSymbolizers().length;i++)
		for (int i=0;i<symbolizers.length;i++)
		{
			
			Symbolizer symbolizer=symbolizers[i];
			if (symbolizer instanceof LineSymbolizer) {
				SLDStyleImpl.configureGraphicContext(null, g2, (LineSymbolizer)symbolizer,null);
				int xPoints[] = {z+BORDER, w2, w2, w-BORDER};
				int yPoints[] = {z+BORDER, h-BORDER, z+BORDER, h-BORDER};
				GeneralPath line = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
				line.moveTo(xPoints[0], yPoints[0]);
				for ( int u= 1; u < xPoints.length; u++ ) {
					line.lineTo(xPoints[u], yPoints[u]);
				};
				SLDStyleImpl.drawLine(line, g2);
			}
			else if (symbolizer instanceof PointSymbolizer) {
				Image image = SLDStyleImpl.createImage(null, (PointSymbolizer)symbolizer,null);
				SLDStyleImpl.drawPoint(new Rectangle2D.Double(z,z,w,h),g2,image);
			}
			else if (symbolizer instanceof PolygonSymbolizer) {
				org.deegree.graphics.sld.Fill fill = ((PolygonSymbolizer)symbolizer).getFill();
				org.deegree.graphics.sld.Stroke stroke = ((PolygonSymbolizer)symbolizer).getStroke();
				
				int xPoints[] = {z+BORDER, w2/2, w2, 3*w2/2,w-BORDER,z+BORDER};
				int yPoints[] = {h2/2, h2/2,(int)( 0.75*h2), 4*h2/3,h-BORDER,h-BORDER};
				GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
				polygon.moveTo(xPoints[0], yPoints[0]);
				for (int u = 1; u < xPoints.length; u++)
					{
					polygon.lineTo(xPoints[u], yPoints[u]);
					};
				if ( fill != null ) {
					SLDStyleImpl.configureGraphicContextForFilling(null, g2, (PolygonSymbolizer)symbolizer);
//					g2.fill(new Rectangle(z+5,z+5,w-11,h-11));
					g2.fill(polygon);
				}
				if ( stroke != null ) {
					SLDStyleImpl.configureGraphicContextForStroking(null, g2, (PolygonSymbolizer)symbolizer,null);
//					g2.draw(new Rectangle(z+5,z+5,w-11,h-11));
					g2.draw(polygon);
				}
			}
			else if (symbolizer instanceof TextSymbolizer) {
				java.awt.Font font = SLDStyleImpl.createFont(null,(TextSymbolizer)symbolizer,null);
				org.deegree.graphics.sld.Font sldFont = ((TextSymbolizer)symbolizer).getFont();
				g2.setFont(font);
				g2.setColor(sldFont.getColor(null));
				String texto = AppContext.getApplicationContext().getI18nString("Muestra");
				FontMetrics metrics = g2.getFontMetrics();
				int width = metrics.stringWidth( texto );
				int height= metrics.getHeight();
				int descent = (int) metrics.getDescent();
				g2.drawString( texto, w2-width/2, (h - descent)/2);				
			}
		
		}//while
		}
		catch (Exception e) {
		}
		
	}
	/**
	 * @param rule The rule to set.
	 */
	public void setRule(Rule rule)
	{
		this.rule = rule;
	}
	/**
	 * @param symbolizers2
	 */
	public void setSymbolizers(Vector symbolizersVector)
	{
		if (symbolizersVector==null)return;
		
		symbolizers=new Symbolizer[symbolizersVector.size()];
		symbolizersVector.toArray(symbolizers);
	}
	public void addSymbolizers(Symbolizer[] symbs)
	{
		if (symbolizers==null)
			symbolizers=symbs;
		Vector vect=new Vector();
		vect.addAll( Arrays.asList(symbolizers));
		vect.addAll(Arrays.asList(symbs));
		setSymbolizers(vect);
	}
	/**
	 * @return
	 */
	public Symbolizer[] getSymbolizers()
	{
		return symbolizers;
	}
}
