package com.geopista.ui.reports.scale;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.engine.JRImage;

import com.geopista.app.reports.ScaleUtils;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.util.MathUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.IncrementChooser;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.MetricSystem;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.RoundQuantity;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.ScaleBarRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.SimpleRenderer;

public class PrintSectionRenderer extends SimpleRenderer {

	public final static String CONTENT_ID = "PRINT_SECTION";

    private final static Color FILL_COLOR = new Color(255,255,255, 85);
    private final static Color LINE_COLOR = Color.black;
    
    private int width;
    private int height;	
    
    /** 
     *  Distance from the bottom edge, in view-space units.
     */
    private final static String ENABLED_KEY = PrintSectionRenderer.class +" - ENABLED";

    public PrintSectionRenderer(LayerViewPanel panel, int width, int height) {
        super(CONTENT_ID, panel);
        this.width = width;
        this.height = height;        
    }

    public static boolean isEnabled(LayerViewPanel panel) {
        return panel.getBlackboard().get(ENABLED_KEY, false);  
    }

    public static void setEnabled(boolean enabled, LayerViewPanel panel) {
        panel.getBlackboard().put(ENABLED_KEY, enabled);
    }
    
    private Stroke stroke = new BasicStroke();
    
    protected void paint(Graphics2D g) {
        paintNow(g);
    }
    
    public void paintNow(Graphics2D graphics2d) {
        if (!isEnabled(panel)) {
            return;
        }
        //Override dashes set in GridRenderer [Jon Aquino]
        graphics2d.setStroke(stroke);

		Envelope currentEnvelope = panel.getViewport().getEnvelopeInModelCoordinates();
		//Envelope printEnvelope = ReportUtils.getReportPrintEnvelope(panel, jrImage);
		Envelope printEnvelope = ScaleUtils.getReportPrintEnvelope(panel, height, width);
		
		if (printEnvelope == null){
			return;
		}
		
		int panelWidth = panel.getWidth();
		int panelHeight = panel.getHeight();
	
		double currentScale = currentEnvelope.getWidth() / panelWidth;
		double originHorizontalDistance = printEnvelope.getMinX() - currentEnvelope.getMinX();
		double originVerticalDistance = printEnvelope.getMinY() - currentEnvelope.getMinY();
		
		double limitsOriginX;
		if (originHorizontalDistance < 0){
			limitsOriginX = 0;
		}
		else {
			limitsOriginX = originHorizontalDistance / currentScale;
		}
		
		double limitsOriginY;
		if (originVerticalDistance < 0){
			limitsOriginY = 0;
		}
		else {
			limitsOriginY = originVerticalDistance / currentScale;
		}
		
		double width = printEnvelope.getWidth() / currentScale;
		double height = printEnvelope.getHeight() / currentScale;
		
		Rectangle2D.Double shape =
            new Rectangle2D.Double(limitsOriginX, limitsOriginY, width, height);
		graphics2d.setColor(FILL_COLOR);
		graphics2d.fill(shape);
		graphics2d.setColor(LINE_COLOR);
		graphics2d.draw(shape);
    }
}
