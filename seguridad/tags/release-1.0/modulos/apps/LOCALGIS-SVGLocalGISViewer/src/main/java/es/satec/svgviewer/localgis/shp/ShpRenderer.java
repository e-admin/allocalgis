package es.satec.svgviewer.localgis.shp;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class ShpRenderer {

	private static DRectangle scr_box;
	private static double scr_cx;
	private static double scr_cy;
	private static double map_cx;
	private static double map_cy;
	private static double map_to_scr_width_ratio;
	private static double map_to_scr_height_ratio;
	private Legend legend;

	private int S;
	private Color foreColor;
	private Color backColor;
	private Color labelColor;

	private GC G;
	private Image offscreen;

	private boolean disable_aniflag;

	private int width;
	private int height;

	public ShpRenderer() {

		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK); 
		foreColor = black;
		backColor = black;
		labelColor = black;
		disable_aniflag = false;

	}

	protected void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	protected Color getForeColor() {
		return foreColor;
	}
	
	protected Color getBackColor() {
		return backColor;
	}
	
	protected Color getLabelColor() {
		return labelColor;
	}
	
	protected void setColor(Color color) {
		if (foreColor != null && !foreColor.isDisposed())
			foreColor.dispose();
		if (backColor != null && !backColor.isDisposed())
			backColor.dispose();
		if (labelColor != null && !labelColor.isDisposed())
			labelColor.dispose();
		foreColor = color;
		backColor = color;
		labelColor = color;
	}
	
	private void calc_map_scr() {

		scr_box = new DRectangle(new DPoint(0, 0), new DPoint(width, height));
//		double d = 0.0D;
//		double d1 = 0.0D;
//		if(legend.whratio > 1.0D)
//			d1 = scr_box.dheight - 12D - (scr_box.dwidth - 12D) / legend.whratio;
//		else
//			d = scr_box.dwidth - 12D - (scr_box.dheight - 12D) * legend.whratio;
		scr_cx = scr_box.min.dx + scr_box.dwidth / 2D;
		scr_cy = scr_box.min.dy + scr_box.dheight / 2D;
		map_cx = legend.map_box.min.dx + legend.map_box.dwidth / 2D;
		map_cy = legend.map_box.min.dy + legend.map_box.dheight / 2D;
		if(legend.map_box.dwidth != 0.0D)
			map_to_scr_width_ratio = legend.map_box.dwidth / (scr_box.dwidth/* - 12D - d*/);
		else
			map_to_scr_width_ratio = 0.0D;
		if(legend.map_box.dheight != 0.0D)
		{
			map_to_scr_height_ratio = legend.map_box.dheight / (scr_box.dheight/* - 12D - d1*/);
			return;
		} else
		{
			map_to_scr_height_ratio = 0.0D;
			return;
		}
	}

//	public static DRectangle normal_map_box(DRectangle drectangle) {
//		double d = drectangle.dwidth;
//		double d1 = drectangle.dheight;
//		if(ShpReader.legend.whratio != 0.0D)
//			if(drectangle.dwidth >= drectangle.dheight)
//				d1 = d / ShpReader.legend.whratio;
//			else
//				d = d1 * ShpReader.legend.whratio;
//		DPoint dpoint = new DPoint(drectangle.min.dx + d, drectangle.min.dy + d1);
//		return new DRectangle(drectangle.min, dpoint);
//	}

	private DPoint map_to_scr_dp(DPoint dpoint)
	{
		double d;
		if(map_to_scr_width_ratio != 0.0D)
			d = scr_cx + (dpoint.dx - map_cx) / map_to_scr_width_ratio;
		else
			d = scr_cx;
		double d1;
		if(map_to_scr_height_ratio != 0.0D)
			d1 = scr_cy + (map_cy - dpoint.dy) / map_to_scr_height_ratio;
		else
			d1 = scr_cy;
		return new DPoint(d, d1);
	}

	private DPoint scr_to_map_dp(DPoint dpoint)
	{
		double d = map_cx + (dpoint.dx - scr_cx) * map_to_scr_width_ratio;
		double d1 = map_cy + (scr_cy - dpoint.dy) * map_to_scr_height_ratio;
		return new DPoint(d, d1);
	}

	private DRectangle scr_to_map_drect(DRectangle drectangle)
	{
		DPoint dpoint = scr_to_map_dp(drectangle.min);
		DPoint dpoint1 = scr_to_map_dp(drectangle.max);
		return new DRectangle(dpoint, dpoint1);
	}

	private DRectangle map_to_scr_drect(DRectangle drectangle)
	{
		DPoint dpoint = map_to_scr_dp(drectangle.min);
		DPoint dpoint1 = map_to_scr_dp(drectangle.max);
		return new DRectangle(dpoint, dpoint1);
	}

	private void draw_point_feature(DPoint dpoint, Color color, int i, int j, boolean flag)
	{
		DPoint dpoint1 = map_to_scr_dp(dpoint);
//		if(j == 0)
//		{
			int k = i / 2;
			G.setForeground(color);
			G.fillOval(dpoint1.x - k, dpoint1.y - k, i, i);
			return;
//		}
//		//TODO
//		//PointImageObject pointimageobject = (PointImageObject)jshape.PIOVector.elementAt(j - 1);
//		Image image = pointimageobject.ProcessImage(color, flag);
//		int l = 0;//pointimageobject.GetBS();
//		int i1 = (image.getBounds().width * i) / l;
//		int j1 = (image.getBounds().height * i) / l;
//		if(i1 < 1)
//			i1 = 1;
//		if(j1 < 1)
//			j1 = 1;
//		if(image != null)
//			G.drawImage(image, 0, 0, image.getBounds().x, image.getBounds().y, dpoint1.x - i1 / 2, dpoint1.y - j1 / 2, i1, j1);
	}

	private void proc_point_theme(Theme theme)
	{
		int i = theme.pio;
		DRectangle drectangle = new DRectangle(new DPoint(0, width), new DPoint(height, 0));
		DRectangle drectangle1 = scr_to_map_drect(drectangle);
		int j = theme.feature_vector.size();
		for(int l = 0; l < j; l++)
		{
			PTFeature ptfeature = (PTFeature)theme.feature_vector.elementAt(l);
			ptfeature.viewsize = S;
			if(drectangle1.inside(ptfeature.dp))
			{
				int k = S;
				Color color;
				if(((Feature) (ptfeature)).selected && !theme.thematic_enable)
				{
					color = ((Feature) (ptfeature)).selected_color;
					if(((Feature) (ptfeature)).selected_size != 0)
						k = ((Feature) (ptfeature)).selected_size;
					i = theme.pio;
				} else
				{
					color = foreColor;
					//                    if(theme.thematic_enable)
						//                    {
						//                        String s = (String)theme.info_vector.elementAt(l);
						//                        jshape.ResetThematic();
					//                        if(jshape.thematicdl("check", jshape.GetAttributeField(s, theme.thematic_field)) != 0)
					//                        {
					//                            if(jshape.ThematicForeground != null)
					//                                color = jshape.ThematicForeground;
					//                            if(jshape.ThematicSize > 0)
					//                                k = jshape.ThematicSize;
					//                            if(((Feature) (ptfeature)).selected)
					//                                color = ((Feature) (ptfeature)).selected_color;
					//                            i = jshape.ThematicPIO;
					//                        }
					//                    }
				}
				ptfeature.viewsize = k;
				draw_point_feature(ptfeature.dp, color, k, i, ((Feature) (ptfeature)).selected);
			}
		}

		//        if(theme.label_flag && theme.info_vector.size() > 0)
		//        {
		//            for(int i1 = 0; i1 < j; i1++)
		//            {
		//                PTFeature ptfeature1 = (PTFeature)theme.feature_vector.elementAt(i1);
		//                if(drectangle1.inside(ptfeature1.dp))
		//                {
		//                    G.setForeground(L);
		//                    DPoint dpoint = map_to_scr_dp(ptfeature1.dp);
		//                    String s1 = (String)theme.info_vector.elementAt(i1);
		//                    String s2 = jshape.GetAttributeField(s1, theme.label_field);
		//                    if(s2 != null)
		//                        G.drawString(s2, dpoint.x + ptfeature1.viewsize / 2 + 3, dpoint.y + 6);
		//                }
		//            }
		//
		//        }
	}

	private void draw_arc_feature(Vector vector, int i, int j, Color color, int k)
	{
		G.setForeground(color);
		for(int l = i; l < j; l++)
		{
			if(l + 1 >= j)
				break;
			DPoint dpoint = map_to_scr_dp((DPoint)vector.elementAt(l));
			DPoint dpoint1 = map_to_scr_dp((DPoint)vector.elementAt(l + 1));
			if(dpoint.x != dpoint1.x || dpoint.y != dpoint1.y)
				if(k == 1)
				{
					G.drawLine(dpoint.x, dpoint.y, dpoint1.x, dpoint1.y);
				} else
				{
					int[] polygon = new int[8];
					double d = dpoint1.x - dpoint.x;
					double d1 = dpoint1.y - dpoint.y;
					double d2;
					double d3;
					if(d != 0.0D)
					{
						double d4 = d1 / d;
						d3 = (double)k / Math.sqrt(1.0D + d4 * d4);
						d2 = d3 * d4;
						if(d > 0.0D && d1 > 0.0D)
							d2 = -d2;
						if(d > 0.0D && d1 < 0.0D)
							d3 = -d3;
						if(d < 0.0D && d1 > 0.0D)
							d2 = -d2;
						if(d < 0.0D && d1 < 0.0D)
							d3 = -d3;
					} else
					{
						d3 = 0.0D;
						if(d1 > 0.0D)
							d2 = -k;
						else
							d2 = k;
					}
					int i1 = (int)Math.round(d2);
					int j1 = (int)Math.round(d3);
					polygon[0] = dpoint.x; polygon[1] = dpoint.y;
					polygon[2] = dpoint.x + i1; polygon[3] = dpoint.y + j1;
					polygon[4] = dpoint1.x + i1; polygon[5] = dpoint1.y + j1;
					polygon[6] = dpoint1.x; polygon[7] = dpoint1.y;
					G.drawPolygon(polygon);
					G.fillPolygon(polygon);
				}
		}

	}

	private void proc_arc_theme(Theme theme)
	{
		DRectangle drectangle = new DRectangle(new DPoint(0, height), new DPoint(width, 0));
		DRectangle drectangle1 = scr_to_map_drect(drectangle);
		int i = theme.feature_vector.size();
		for(int i1 = 0; i1 < i; i1++)
		{
			ArcFeature arcfeature = (ArcFeature)theme.feature_vector.elementAt(i1);
			if(drectangle1.intersects(((Feature) (arcfeature)).feature_box))
			{
				for(int j1 = 0; j1 < arcfeature.part_vector.size(); j1++)
				{
					int j = ((Integer)arcfeature.part_vector.elementAt(j1)).intValue();
					int k;
					if(j1 + 1 < arcfeature.part_vector.size())
						k = ((Integer)arcfeature.part_vector.elementAt(j1 + 1)).intValue();
					else
						k = arcfeature.point_vector.size();
					int l = S;
					Color color;
					if(((Feature) (arcfeature)).selected && !theme.thematic_enable)
					{
						color = ((Feature) (arcfeature)).selected_color;
						if(((Feature) (arcfeature)).selected_size != 0)
							l = ((Feature) (arcfeature)).selected_size;
					} else
					{
						color = foreColor;
						//                        if(theme.thematic_enable)
							//                        {
							//                            String s = (String)theme.info_vector.elementAt(i1);
							//                            jshape.ResetThematic();
							//                            if(jshape.thematicdl("check", jshape.GetAttributeField(s, theme.thematic_field)) != 0)
						//                            {
						//                                if(jshape.ThematicForeground != null)
						//                                    color = jshape.ThematicForeground;
						//                                if(jshape.ThematicSize > 0)
						//                                    l = jshape.ThematicSize;
						//                                if(((Feature) (arcfeature)).selected)
						//                                    color = ((Feature) (arcfeature)).selected_color;
						//                            }
						//                        }
					}
					draw_arc_feature(arcfeature.point_vector, j, k, color, l);
				}

			}
		}

		if(theme.label_flag && theme.info_vector.size() > 0)
		{
			FontMetrics fontmetrics = G.getFontMetrics();//Toolkit.getDefaultToolkit().getFontMetrics(G.getFont());
			for(int i2 = 0; i2 < i; i2++)
			{
				ArcFeature arcfeature1 = (ArcFeature)theme.feature_vector.elementAt(i2);
				if(drectangle1.intersects(((Feature) (arcfeature1)).feature_box))
				{
					G.setForeground(labelColor);
					DPoint dpoint = map_to_scr_dp(new DPoint((((Feature) (arcfeature1)).feature_box.min.dx + ((Feature) (arcfeature1)).feature_box.max.dx) / 2D, (((Feature) (arcfeature1)).feature_box.min.dy + ((Feature) (arcfeature1)).feature_box.max.dy) / 2D));
					String s1 = (String)theme.info_vector.elementAt(i2);
					//                    String s2 = jshape.GetAttributeField(s1, theme.label_field);
					//                    if(s2 != null)
					//                    {
					//                        int k1 = fontmetrics.getAverageCharWidth()*s2.length() /*.stringWidth(s2)*/ / 2;
					//                        int l1 = fontmetrics.getHeight() / 2;
					//                        G.drawString(s2, dpoint.x - k1, dpoint.y + l1);
					//                    }
				}
			}

		}
	}

	private void draw_polygon_feature(Vector vector, int i, int j, Color color, Color color1, int k)
	{
// Nunca dibujaremos los poligonos con relleno
//		if(color1 == ShpReader.TransparentColor)
//		{
//			draw_arc_feature(vector, i, j, color, k);
//			return;
//		}
//		int[] polygon = new int[(j-i)*2];
//		for(int l = i; l < j; l++)
//		{
//			DPoint dpoint = map_to_scr_dp((DPoint)vector.elementAt(l));
//			polygon[l-i] = dpoint.x; polygon[l-i+1] = dpoint.y;
//		}
//
//		G.setBackground(color1);
//		G.fillPolygon(polygon);
		draw_arc_feature(vector, i, j, color, k);
	}

	private void proc_polygon_theme(Theme theme)
	{
		DRectangle drectangle = new DRectangle(new DPoint(0, height), new DPoint(width, 0));
		DRectangle drectangle1 = scr_to_map_drect(drectangle);
		int i = theme.feature_vector.size();
		for(int i1 = 0; i1 < i; i1++)
		{
			PGFeature pgfeature = (PGFeature)theme.feature_vector.elementAt(i1);
			if(drectangle1.intersects(((Feature) (pgfeature)).feature_box))
			{
				for(int j1 = 0; j1 < pgfeature.part_vector.size(); j1++)
				{
					int j = ((Integer)pgfeature.part_vector.elementAt(j1)).intValue();
					int k;
					if(j1 + 1 < pgfeature.part_vector.size())
						k = ((Integer)pgfeature.part_vector.elementAt(j1 + 1)).intValue();
					else
						k = pgfeature.point_vector.size();
					int l = S;
					Color color;
					Color color1;
					if(((Feature) (pgfeature)).selected && !theme.thematic_enable)
					{
						color = foreColor;
						color1 = ((Feature) (pgfeature)).selected_color;
						if(((Feature) (pgfeature)).selected_size != 0)
							l = ((Feature) (pgfeature)).selected_size;
					} else
					{
						color = foreColor;
						color1 = backColor;
						//                        if(theme.thematic_enable)
							//                        {
							//                            String s = (String)theme.info_vector.elementAt(i1);
							//                            jshape.ResetThematic();
							//                            if(jshape.thematicdl("check", jshape.GetAttributeField(s, theme.thematic_field)) != 0)
								//                            {
						//                                if(jshape.ThematicForeground != null)
						//                                    color = jshape.ThematicForeground;
						//                                if(jshape.ThematicBackground != null)
						//                                    color1 = jshape.ThematicBackground;
						//                                if(jshape.ThematicSize > 0)
						//                                    l = jshape.ThematicSize;
						//                                if(((Feature) (pgfeature)).selected)
						//                                    color = ((Feature) (pgfeature)).selected_color;
						//                            }
						//                        }
					}
					draw_polygon_feature(pgfeature.point_vector, j, k, color, color1, l);
				}

			}
		}

		if(theme.label_flag && theme.info_vector.size() > 0)
		{
			FontMetrics fontmetrics = G.getFontMetrics();//Toolkit.getDefaultToolkit().getFontMetrics(G.getFont());
			for(int i2 = 0; i2 < i; i2++)
			{
				PGFeature pgfeature1 = (PGFeature)theme.feature_vector.elementAt(i2);
				if(drectangle1.intersects(((Feature) (pgfeature1)).feature_box))
				{
					G.setForeground(labelColor);
					DPoint dpoint = map_to_scr_dp(new DPoint((((Feature) (pgfeature1)).feature_box.min.dx + ((Feature) (pgfeature1)).feature_box.max.dx) / 2D, (((Feature) (pgfeature1)).feature_box.min.dy + ((Feature) (pgfeature1)).feature_box.max.dy) / 2D));
					String s1 = (String)theme.info_vector.elementAt(i2);
					//                    String s2 = jshape.GetAttributeField(s1, theme.label_field);
					//                    if(s2 != null)
					//                    {
					//                        int k1 = fontmetrics.getAverageCharWidth()*s2.length() / 2;
					//                        int l1 = fontmetrics.getHeight() / 2;
					//                        G.drawString(s2, dpoint.x - k1, dpoint.y + l1);
					//                    }
				}
			}

		}
	}

	private void proc_image_theme(Theme theme)
	{
		Rectangle rectangle2 = new Rectangle(0, 0, width, height);
		if(theme.dlflag == 0 && !theme.mrmflag)
		{
			if(theme.image == null)
				return;
			DRectangle drectangle = map_to_scr_drect(theme.theme_box);
			Rectangle rectangle = new Rectangle(drectangle.min.x, drectangle.max.y, drectangle.max.x - drectangle.min.x, drectangle.min.y - drectangle.max.y);
			if(rectangle.width == 0 || rectangle.height == 0)
				return;
			Rectangle rectangle3 = rectangle.intersection(rectangle2);
			if(rectangle3 != null)
			{
				Image image = theme.image;
				//ImageObserverAgent imageobserveragent = new ImageObserverAgent(this);
				G.drawImage(image, rectangle3.x, rectangle3.y, rectangle3.x + rectangle3.width, rectangle3.y + rectangle3.height, (image.getBounds().width * (rectangle3.x - rectangle.x)) / rectangle.width, (image.getBounds().height * (rectangle3.y - rectangle.y)) / rectangle.height, (image.getBounds().width * ((rectangle3.x + rectangle3.width) - rectangle.x)) / rectangle.width, (image.getBounds().height * ((rectangle3.y + rectangle3.height) - rectangle.y)) / rectangle.height);
				return;
			}
		} else
		{
			if(theme.mrmflag)
			{
				theme.map_extent_vector.removeAllElements();
				theme.map_image_vector.removeAllElements();
				//                jshape.DLTheme = theme;
				//                jshape.mrmdl("load", "");
				//                theme = jshape.DLTheme;
			}
			for(int i = 0; i < theme.map_extent_vector.size(); i++)
			{
				DRectangle drectangle1 = map_to_scr_drect((DRectangle)theme.map_extent_vector.elementAt(i));
				Rectangle rectangle1 = new Rectangle(drectangle1.min.x, drectangle1.max.y, drectangle1.max.x - drectangle1.min.x, drectangle1.min.y - drectangle1.max.y);
				if(rectangle1.width != 0 && rectangle1.height != 0)
				{
					Rectangle rectangle4 = rectangle1.intersection(rectangle2);
					if(rectangle4 != null)
					{
						Image image1 = (Image)theme.map_image_vector.elementAt(i);
						//ImageObserverAgent imageobserveragent1 = new ImageObserverAgent(this);
						G.drawImage(image1, rectangle4.x, rectangle4.y, rectangle4.x + rectangle4.width, rectangle4.y + rectangle4.height, (image1.getBounds().width * (rectangle4.x - rectangle1.x)) / rectangle1.width, (image1.getBounds().height * (rectangle4.y - rectangle1.y)) / rectangle1.height, (image1.getBounds().width * ((rectangle4.x + rectangle4.width) - rectangle1.x)) / rectangle1.width, (image1.getBounds().height * ((rectangle4.y + rectangle4.height) - rectangle1.y)) / rectangle1.height);
					}
				}
			}

		}
	}

	private Image draw_map(int count) {
		if (offscreen != null && !offscreen.isDisposed()) {
			offscreen.dispose();
		}

		ImageData imData = new ImageData(width, height, 2, new PaletteData(new RGB[] {ShpReader.TransparentColor.getRGB(), foreColor.getRGB()}));
		imData.transparentPixel = 0;

		offscreen = new Image(Display.getCurrent(), imData);

		G = new GC(offscreen);
		G.setClipping(0, 0, width, height);
		for(int j = 0; j < count; j++) {
			Theme theme = (Theme)legend.theme_vector.elementAt(j);

			if(theme.enable && (!disable_aniflag || !theme.aniflag)) {
				S = theme.symbol_size;
				//foreColor = theme.foreground;
				//B = theme.background;
				//L = theme.label_color;
				switch(theme.shape_type)
				{
				case 1: // '\001'
				proc_point_theme(theme);
				break;

				case 3: // '\003'
				proc_arc_theme(theme);
				break;

				case 5: // '\005'
					proc_polygon_theme(theme);
					break;

				case 99: // 'c'
					proc_image_theme(theme);
					break;
				}
			}
		}

		G.dispose();
		
		return offscreen;
	}

	public Image paint(int count)
	{
		if (ShpReader.legend == null) {
			return null;
		}
		else {
			legend = ShpReader.legend;
		}

		calc_map_scr();

		return draw_map(count);
	}
}
