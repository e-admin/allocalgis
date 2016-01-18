/**
 * LegendRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.JTree;

import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.vividsolutions.jump.workbench.ui.FirableTreeModelWrapper;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.RoundQuantity;
import com.vividsolutions.jump.workbench.ui.renderer.SimpleRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

public class LegendRenderer extends SimpleRenderer {
    public final static String CONTENT_ID = "LEGEND_SHOW";
    public final static int MAXCHILDS = 10;
    /**
     *  Height of the increment boxes, in view-space units.
     */
    private final static int SEPARACION_EN_Y = 20;
    private final static int BAR_HEIGHT = 10;
    private final static Color FILL2 = new Color(255, 204, 204);
    private final static Color FILL1 = Color.white;

    /**
     *  Distance from the right edge, in view-space units.
     */
    private final static int HORIZONTAL_MARGIN = 200;

    /**
     *  In view-space units; the actual increment may be a bit larger or smaller
     *  than this amount.
     */
    private final static int IDEAL_INCREMENT = 75;
    private final static Color LINE_COLOR = Color.black;
    private final static int TEXT_BOTTOM_MARGIN = 1;
    private final static int UNIT_TEXT_BOTTOM_MARGIN = 1;
    private final static Color TEXT_COLOR = Color.black;
    private final static Color UNIT_TEXT_COLOR = Color.blue;

    /**
     *  Distance from the bottom edge, in view-space units.
     */
    private final static int VERTICAL_MARGIN = 3;
    private final static String ENABLED_KEY = LegendRenderer.class +" - ENABLED";
    private final static int INCREMENT_COUNT = 3;
    private Font FONT = new Font("Dialog", Font.PLAIN, 10);
    private Font UNIT_FONT = new Font("Dialog", Font.BOLD, 11);
    private static int height = 0; // Usado para especificar un tamaño fijo
    private static int width = 0;

    public LegendRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel);

    }

    public LegendRenderer(LayerViewPanel panel, int height, int width) {
        super(CONTENT_ID, panel);
        this.height = height;
        this.width = width;
    }

    public static boolean isEnabled(ILayerViewPanel panel) {
        return panel.getBlackboard().get(ENABLED_KEY, false);
    }


    public static void setEnabled(boolean enabled, LayerViewPanel panel) {
        panel.getBlackboard().put(ENABLED_KEY, enabled);
    }
    
    private Stroke stroke = new BasicStroke();
    
    protected  void paint(Graphics2D g) {
        
    }

    public  void paint(Graphics2D g, GeopistaLayerNamePanel layerNamePanel) {
        if (!isEnabled(panel)) {
            return;
        }

        JTree tree = layerNamePanel.getTree();
        FirableTreeModelWrapper actualTreeModel = (FirableTreeModelWrapper) tree.getModel();

        GeopistaLayerTreeModel.Root root = (GeopistaLayerTreeModel.Root) actualTreeModel.getRoot();

             
        //Override dashes set in GridRenderer [Jon Aquino]
        g.setStroke(stroke);

        int nodosNivel = actualTreeModel.getChildCount(root);

        int totalNodos = 0;
         for (int conta=0; conta<nodosNivel;conta++)
        {
          Object actualChild = (Object) actualTreeModel.getChild(root,conta);
          totalNodos++;
             for(int conta2=0;conta2<actualTreeModel.getChildCount(actualChild)&&conta2<MAXCHILDS;conta2++)
            {
              //TODO: hacer bucle para los nodos seleccionados
              totalNodos++;
            }
        }

        g.translate(panel.getWidth()-130,panel.getHeight()-20*(totalNodos+1));




        int posicion = 0;
        for (int conta=0; conta<nodosNivel;conta++)
        {
        
          
          Object actualChild = (Object) actualTreeModel.getChild(root,conta);

          String leyenda = actualChild.toString();
          if(leyenda.length()>15)
          {
             leyenda = leyenda.substring(0,14);
          }
          String retangleText = leyenda;
          
          Color rectangleColor = ((GeopistaLayer) actualChild).getBasicStyle().getFillColor();
          paintLegend(posicion, g, rectangleColor, retangleText);
          posicion++;
          
            for (int conta2=0; conta2<actualTreeModel.getChildCount(actualChild)&&conta2<MAXCHILDS;conta2++)
            {
        
              Object actualChild2 = (Object) actualTreeModel.getChild(actualChild,conta2);
              leyenda = (((Map.Entry) actualChild2).getKey()).toString();
              if(leyenda.length()>15)
              {
                leyenda = leyenda.substring(0,14);
              }
              retangleText = leyenda;
              rectangleColor = ((BasicStyle)((Map.Entry) actualChild2).getValue()).getFillColor();
//              
              paintLegend(posicion, g, rectangleColor, retangleText);
              posicion++;
            }
          
        }
        g.drawRect(0,0,110,20*totalNodos);

        g.translate(-(panel.getWidth()-130),-(panel.getHeight()-20*(totalNodos+1)));
        
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

    private void paintLegend(int posicion, Graphics2D g, Color rectangleColor, String retangleText) {
        Rectangle2D.Double shape =
            new Rectangle2D.Double(
                5,
                5 + LegendRenderer.SEPARACION_EN_Y*posicion,
                25,
                10);
        g.setColor(rectangleColor);
        g.fill(shape);
        g.setColor(Color.BLACK);
        g.draw(shape);


        Font font = new Font("Dialog", Font.PLAIN, 10);
        g.setColor(Color.BLACK);

        int textBottomMargin = 20;

       

        TextLayout layout = createTextLayout(retangleText, font, g);

        layout.draw(
            g,
            35,
            13 + LegendRenderer.SEPARACION_EN_Y*posicion
            );
     
  
    }

    
    
    

    private double x(int i, RoundQuantity increment, int incrementCount, double scale) {
        return HORIZONTAL_MARGIN + (i * increment.getModelValue() * scale);
    }
}
