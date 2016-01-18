package com.geopista.ui.plugin.scalebar;

/**
 * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.IPrintableZone;

import com.geopista.plugin.Constantes;
import com.geopista.ui.plugin.print.elements.IMapFrameOnScreen;
import com.vividsolutions.jump.util.MathUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.IncrementChooser;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.MetricSystem;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.RoundQuantity;
import com.vividsolutions.jump.workbench.ui.renderer.SimpleRenderer;

public class GeopistaScaleBarRenderer extends SimpleRenderer {
	public final static String CONTENT_ID = "GEOPISTASCALE_BAR";
	/**
	 *  Height of the increment boxes, in view-space units.
	 */
	private int BAR_HEIGHT = 10;
	private Color FILL2 = new Color(255, 204, 204);
	private Color FILL1 = Color.white;

	/**
	 *  Distance from the right edge, in view-space units.
	 */
	private final static int HORIZONTAL_MARGIN = 3;

	/**
	 *  In view-space units; the actual increment may be a bit larger or smaller
	 *  than this amount.
	 */
	private int idealIncrement = 75;
	private final static Color LINE_COLOR = Color.black;
	private final static int TEXT_BOTTOM_MARGIN = 1;
	private final static int UNIT_TEXT_BOTTOM_MARGIN = 1;
	private final static Color TEXT_COLOR = Color.black;
	private final static Color UNIT_TEXT_COLOR = Color.blue;

	/**
	 *  Distance from the bottom edge, in view-space units.
	 */
	private final static int VERTICAL_MARGIN = 3;
	private final static String ENABLED_KEY = GeopistaScaleBarRenderer.class +" - ENABLED";
	private  int incrementCount = 3;
	private Font FONT = new Font("Dialog", Font.PLAIN, 10);
	private Font UNIT_FONT = new Font("Dialog", Font.BOLD, 11);
	private  int height = 0; // Usado para especificar un tamaño fijo
	private  int width = 0;

	public GeopistaScaleBarRenderer(LayerViewPanel panel) {
		super(CONTENT_ID, panel);
		setStyle(1);
	}


	public static boolean isEnabled(LayerViewPanel panel) {
		return panel.getBlackboard().get(ENABLED_KEY, false);
	}


	public static void setEnabled(boolean enabled, LayerViewPanel panel) {
		panel.getBlackboard().put(ENABLED_KEY, enabled);
	}

	private Stroke stroke = new BasicStroke();
	private boolean	paintingArea=true;
	private int	transparency=128;
	private int	style;

	protected void paint(Graphics2D g) {
		if (!isEnabled(panel))
			return;

		paint(g, panel.getViewport().getScale());
	}

	
	public void paint(Graphics2D g, double scale) {

		//Override dashes set in GridRenderer [Jon Aquino]
		g.setStroke(stroke);

		RoundQuantity increment =
			new IncrementChooser().chooseGoodIncrement(
					new MetricSystem(1).createUnits(),
					idealIncrement / scale);
		if (paintingArea)
			paintAreas(increment, incrementCount,g,scale);
		paintIncrements(increment, incrementCount, g, scale);
		
		//Problemas dependientes de la visualizacion pantalla completa o ancho de página.
		paintScale (increment, incrementCount, g, scale);

	}

	private int barBottom() {
		// si se ha fijado una altura fija devuelve la establecida si no la del panel huesped
		return (height==0?panel.getHeight():height) - VERTICAL_MARGIN;
	}

	private int barTop() {
		return barBottom() - BAR_HEIGHT;
	}

	private TextLayout createTextLayout(String text, Font font, Graphics2D g) {
		return new TextLayout(text, font, g.getFontRenderContext());
	}

	private void paintIncrement(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		if (style==1)
		{
			Rectangle2D.Double shape =

				new Rectangle2D.Double(
						x(i, increment, incrementCount, scale),
						barTop(),
						x(i + 1, increment, incrementCount, scale) - x(i, increment, incrementCount, scale),
						barBottom() - barTop());
			g.setColor(((i % 2) == 0) ? FILL1 : FILL2);
			g.fill(shape);
			g.setColor(LINE_COLOR);
			g.draw(shape);
		}
		else
			if (style==2)
			{
				Rectangle2D.Double shape1 =
					new Rectangle2D.Double(
							x(i, increment, incrementCount, scale),
							barTop(),
							x(i + 1, increment, incrementCount, scale) - x(i, increment, incrementCount, scale),
							(barBottom() - barTop())/4);
				
				Rectangle2D.Double shape2 =
					new Rectangle2D.Double(
							x(i, increment, incrementCount, scale),
							barTop()+(barBottom() - barTop())/4,
							x(i + 1, increment, incrementCount, scale) - x(i, increment, incrementCount, scale),
							(barBottom() - barTop())/4);
				
				g.setColor(((i % 2) == 0) ? Color.BLACK : Color.WHITE);
				g.fill(shape1);
				g.setColor(((i % 2) == 0) ? Color.WHITE : Color.BLACK);
				g.fill(shape2);
				g.setColor(LINE_COLOR);
				g.draw(shape1);
				g.draw(shape2);
			}
	}

	private void paintIncrements(RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		for (int i = 0; i < incrementCount; i++) {
			paintIncrement(i, increment, incrementCount, g, scale);
			paintLabel(i, increment, incrementCount, g, scale);
		}
	}
	
	private void paintArea(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		double xinic = x(0, increment, incrementCount, scale);
		double xfin = x(i + 1, increment, incrementCount, scale);
		double xprev = x(i , increment, incrementCount, scale);

		double inc_x =  xfin - xinic;
		double inc_x_prev = xfin-xprev;

		if (i == (incrementCount - 1)) {
			return;
		}       
		Color fill =(((i % 2) == 0) ? FILL1 : FILL2);

		fill = new Color(fill.getRed(),fill.getGreen(),fill.getBlue(),transparency);
		g.setColor(fill);
//		Dibuja las áreas como dos cuadrados para no superponer con la anterior
//		arriba
		Rectangle2D.Double shape =
			new Rectangle2D.Double(
					xinic,
					barTop()-inc_x,
					inc_x,
					inc_x_prev);
		g.fill(shape);

//		derecha
		shape = new Rectangle2D.Double(
				xfin-inc_x_prev+1,
				barTop()-inc_x+inc_x_prev,
				inc_x_prev-1,
				xfin-inc_x_prev-2);
		g.fill(shape);

//		marco
		GeneralPath shapeborder = new GeneralPath();
		shapeborder.moveTo((float)xinic,(float)(barTop()-inc_x+inc_x_prev));
		shapeborder.lineTo((float)xinic,(float)(barTop()-inc_x));
		shapeborder.lineTo((float)xfin,(float)(barTop()-inc_x));
		shapeborder.lineTo((float)xfin,(float)(barTop()));

		Color lineColor = new Color(LINE_COLOR.getRed(),LINE_COLOR.getGreen(),LINE_COLOR.getBlue(),128);
		g.setColor(lineColor);
		g.draw(shapeborder);
	}
	
	private void paintAreas(RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		for (int i = 0; i < incrementCount; i++) {
			paintArea(i, increment, incrementCount, g, scale);

		}
		for (int i = 0; i < incrementCount; i++) {

			paintAreaLabel(i, increment, incrementCount, g, scale);
		}
	}
	
	private void paintAreaLabel(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		double xinic = x(0, increment, incrementCount, scale);
		double xfin = x(i + 1, increment, incrementCount, scale);
		double xprev = x(i , increment, incrementCount, scale);

		double inc_x =  xfin - xinic;
		double inc_x_prev = xfin-xprev;
		double nsquare = Math.pow(increment.getAmount()*(i+1),2);
		// calcula la mantisa
		double exponente = Math.floor(Math.log(nsquare)/Math.log(10));
		double mantisa = nsquare/Math.pow(10,exponente);

		String text =formatUnit(mantisa,exponente)+increment.getUnit().getName()+"²"; 
		// Externalizar el caracter superíndice al cuadrado si se detecta problemas de portabilidad.
		Font font = FONT;
		Color textColor = new Color(TEXT_COLOR.getRed(),TEXT_COLOR.getGreen(),TEXT_COLOR.getBlue(),transparency);
		g.setColor(textColor);

		int textBottomMargin = TEXT_BOTTOM_MARGIN;

		if (i == (incrementCount - 1)) {
//			text = increment.getUnit().getName();
//			font = UNIT_FONT;
//			g.setColor(UNIT_TEXT_COLOR);
//			textBottomMargin = UNIT_TEXT_BOTTOM_MARGIN;
			return;
		}

		TextLayout layout = createTextLayout(text, font, g);
		double center =
			MathUtil.avg(x(i, increment, incrementCount, scale), x(i + 1, increment, incrementCount, scale));
		layout.draw(
				g,
				(float) Math.max(VERTICAL_MARGIN,(center - (layout.getAdvance()))),
				(float) (barTop() - textBottomMargin - inc_x+inc_x_prev/2));
	}
	private String formatUnit(double mantisa,double exponente) {
		if (mantisa == 0) {
			return "0";
		}
		NumberFormat nf=NumberFormat.getNumberInstance();
		double amount=Math.pow(10,exponente)*mantisa;
		if ((0 <= exponente) && (exponente <= 5)) {
			//new DecimalFormat("#");
			return nf.format(amount);
		}

		if ((-4 <= exponente) && (exponente < 0)) {
			//new DecimalFormat("#.####");

			return nf.format(amount);
		}

		return nf.format(mantisa) + "E" + nf.format(exponente);
	}    
	private void paintLabel(int i, RoundQuantity increment, int incrementCount, Graphics2D g, double scale) {
		String text =
			new RoundQuantity(
					increment.getMantissa() * (i + 1),
					increment.getExponent(),
					increment.getUnit()).getAmountString();
		Font font = FONT;
		g.setColor(TEXT_COLOR);

		int textBottomMargin = TEXT_BOTTOM_MARGIN;

		if (i == (incrementCount - 1)) {
			text = increment.getUnit().getName();
			font = UNIT_FONT;
			g.setColor(UNIT_TEXT_COLOR);
			textBottomMargin = UNIT_TEXT_BOTTOM_MARGIN;            
		}

		TextLayout layout = createTextLayout(text, font, g);
		double center =
			MathUtil.avg(x(i, increment, incrementCount, scale), x(i + 1, increment, incrementCount, scale));
		layout.draw(
				g,
				(float) (center - (layout.getAdvance() / 2)),
				(float) (barBottom() - textBottomMargin));
	}



	private void paintScale (RoundQuantity increment, int incrementCount, 
			Graphics2D g, double scale)
	{		
		double num = panel.getNormalizedScale(scale); ;
		String text = "1:"+ NumberFormat.getIntegerInstance().format(num) ;
		
		System.out.println("\n\n*****\n text original es: " + text);
		try{
			if (panel.getParent() instanceof IPrintableZone)
			{
				double factor = 0.7139689578713969; 
				
//				System.out.println("el otro factor es: " + ((PrintableZone)panel.getParent()).getScaleFactor());
				
				/*((PrintableZone)panel.getParent()).getScaleFactor();
				System.out.println("factor es "+factor);
				if (factor != 1)
				{
					System.out.println("factor no es 1, es " + factor);
					((PrintableZone)panel.getParent()).setScaleFactor(factor);
				}
				*/
				
				int resizedOnView = ((IMapFrameOnScreen)panel).getResizedOnView();
//				System.out.println("Resized on view: " +resizedOnView);
//				System.out.println("Estilo actual: " + ((PrintableZone)panel.getParent()).getPageStyle());
				//System.out.println("resized: " + ((PrintableZone)panel.getParent()).isResized());
				//text = "1:"+ NumberFormat.getIntegerInstance().format(num*factor) ;						
					
				if (resizedOnView == Constantes.PAGE_ENTIERE)					
				{
					System.out.println("pagina entera");
					text = "1:"+ NumberFormat.getIntegerInstance().format(num*factor) ;
				}
				else
				{
					System.out.println("ancho de pagina");
				}
				
			}
		}catch(NumberFormatException ne)
		{
			ne.printStackTrace();
		}
		
		
		Font font = FONT;
		g.setColor(TEXT_COLOR);
		int textBottomMargin = TEXT_BOTTOM_MARGIN;

		TextLayout layout = createTextLayout(text, font, g);
		double x0 = x(incrementCount, increment, incrementCount, scale);

		Rectangle2D.Double shape =      	 
			new Rectangle2D.Double(
					x(incrementCount, increment, incrementCount, scale),
					barTop(),
					layout.getAdvance()+20,
					barBottom() - barTop());

		g.setColor(FILL1);
		g.fill(shape);
		g.setColor(LINE_COLOR);
		g.draw(shape);
		layout.draw(
				g,
				(float) (x0 +10), (float) (barBottom() - textBottomMargin));

	}

	private double x(int i, RoundQuantity increment, int incrementCount, double scale) {
		return HORIZONTAL_MARGIN + (i * increment.getModelValue() * scale);
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getIncrementCount()
	{
		return incrementCount;
	}
	public void setIncrementCount(int increment_count)
	{
		incrementCount = increment_count;
	}
	public boolean isPaintingArea()
	{
		return paintingArea;
	}
	public void setPaintingArea(boolean paintArea)
	{
		this.paintingArea = paintArea;
	}
	public int getTransparency()
	{
		return transparency;
	}
	public void setTransparency(int transparency)
	{
		this.transparency = transparency;
	}
	public int getIdealIncrement()
	{
		return idealIncrement;
	}
	public void setIdealIncrement(int idealIncrement)
	{
		this.idealIncrement = idealIncrement;
	}

	/**
	 * @param i
	 */
	public void setStyle(int i)
	{
		this.style=i;
		if (style==2)
			this.BAR_HEIGHT=20;
		else
			this.BAR_HEIGHT=10;
	}
}


