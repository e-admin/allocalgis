/**
 * GraticuleCreatorPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*************************************************
 * created on   :     19.01.2006
 * last modified:     22.01.2006
 *
 * author:            Ruggero Valentinotti
 *               
 *   http://digilander.libero.it/valruggero/
 *   
 *   
 * LICENZE:	GPL 2.0 or (at your option) any later version
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 *   
 *************************************************/


package com.geostaf.ui.plugin.generate;

import java.awt.Frame;

import com.geopista.ui.dialogs.global.Constants;
import com.localgis.mobile.svg.Utils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/**
 *  Creates a polygon-grid layer
 */
public class GraticuleCreatorPlugIn extends AbstractPlugIn {
	public static final String SOUTHWEST_CORNER_OF_LEFT_LAYER = "SouthwestCornerOfLeftLayer";
	public static final String LAYER_WIDTH_IN_CELLS = "LayerWidthInCells";
	public static final String LAYER_HEIGHT_IN_CELLS = "LayerHeightInCells";
	public static final String CELL_SIDE_LENGTH_Y = "CellSideLengthY";
	public static final String CELL_SIDE_LENGTH_X = "CellSideLengthX";

	//logica del plugin
    private static GraticuleCreatorEngine engine = new GraticuleCreatorEngine();
    
    //texto de la pantalla
    private final static String celdasAnchoStr = "Ancho (celdas)";
    private final static String celdasAltoStr = "Alto (celdas)";
    private final static String minXStr = "Min X";
    private final static String minYStr = "Min Y";

    //cuadro de creación de la cuadrícula
	private double maxXValue;
	private double maxYValue;
	private double minXValue;
	private double minYValue;
	
    private Blackboard blackboard  = Constants.APLICACION.getBlackboard();

    Frame parent = null;
    
    public GraticuleCreatorPlugIn() {}
    
    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(this, new String[] { "GeoSTAF"}, getName() + "...");
    }

    public boolean execute(PlugInContext context) throws Exception {
    	MultiInputDialog dialog = new MultiInputDialog(context.getWorkbenchGuiComponent().getMainFrame(), "Creador de cuadrículas", true);
    	setDialogValues(dialog, context);    
        //GUIUtil.centreOnWindow(dialog);      
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }

        getDialogValues(dialog);
        engine.execute(context);

        return true;
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
//        dialog.setTitle("Graticule Creator");
//        dialog.setSideBarImage(new ImageIcon(getClass().getResource("GraticuleCreator.gif")));
//        dialog.setSideBarDescription("Creates a graticule.                   \n\n\n      By valruggero");
//        
//        dialog.addPositiveIntegerField(
//        	"Layer Width  (cells)",
//            engine.getLayerWidthInCells(),
//            6);
//        dialog.addPositiveIntegerField(
//        	"Layer Height (cells)",
//            engine.getLayerHeightInCells(),
//            6);
//        dialog.addPositiveDoubleField(
//        	"Cell Side Length X",
//            engine.getCellSideLengthX(),
//            6);
//        dialog.addPositiveDoubleField(
//        	"Cell Side Length Y",
//            engine.getCellSideLengthY(),
//            6);
//        dialog.addDoubleField("Min X", engine.getSouthwestCornerOfLeftLayer().x, 6);
//        dialog.addDoubleField("Min Y", engine.getSouthwestCornerOfLeftLayer().y, 6);
    	
      //obtenemos lo que estamos viendo
      Envelope e = context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
  
      dialog.setTitle("Creador de cuadrículas");
      //dialog.setSideBarImage(new ImageIcon(getClass().getResource("GraticuleCreator.gif")));
      dialog.setSideBarDescription("Crea una cuadrícula para exportar el mapa visualizado a un dispositivo movil.\n\n\nLocalGIS");
      
      dialog.addPositiveIntegerField(celdasAnchoStr, engine.getLayerWidthInCells(), 6);
      dialog.addPositiveIntegerField(celdasAltoStr, engine.getLayerHeightInCells(), 6);
//      dialog.addPositiveDoubleField(
//      	"Cell Side Length X",
//          engine.getCellSideLengthX(),
//          6);
//      dialog.addPositiveDoubleField(
//      	"Cell Side Length Y",
//          engine.getCellSideLengthY(),
//          6);
      
      this.maxXValue = e.getMaxX();
      this.maxYValue = e.getMaxY();
      this.minXValue = e.getMinX();
      this.minYValue = e.getMinY();
      dialog.addDoubleField(minXStr, Utils.redondear(e.getMinX(), 4), 10, "", false);
      dialog.addDoubleField(minYStr, Utils.redondear(e.getMinY(), 4), 10, "", false);
    }

    private void getDialogValues(MultiInputDialog dialog) {
    	//coordenada inferior izquierda de lo visualizado
        engine.setSouthwestCornerOfLeftLayer(new Coordinate(minXValue, minYValue));
        int xCells = dialog.getInteger(celdasAnchoStr);
        int yCells = dialog.getInteger(celdasAltoStr);
        engine.setLayerWidthInCells(xCells);
        engine.setLayerHeightInCells(yCells);
        //cálculo del tamaño de las celdas según el número de celdas que nos hayan indicado
        double xSize = maxXValue - minXValue;
        double ySize = maxYValue - minYValue;
        engine.setCellSideLengthX(xSize / xCells);
        engine.setCellSideLengthY(ySize / yCells);
        
        //metemos en el clipboard la última cuadrícula
        blackboard.put(CELL_SIDE_LENGTH_X, engine.getCellSideLengthX());
        blackboard.put(CELL_SIDE_LENGTH_Y, engine.getCellSideLengthY());
        blackboard.put(LAYER_HEIGHT_IN_CELLS, engine.getLayerHeightInCells());
        blackboard.put(LAYER_WIDTH_IN_CELLS, engine.getLayerWidthInCells());
        blackboard.put(SOUTHWEST_CORNER_OF_LEFT_LAYER, engine.getSouthwestCornerOfLeftLayer());
    }
    
    public static MultiEnableCheck createEnableCheck(
            WorkbenchContext workbenchContext) {
            EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

            return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }
    
	public double getMaxXValue() {
		return maxXValue;
	}

	public void setMaxXValue(double maxXValue) {
		this.maxXValue = maxXValue;
	}

	public double getMaxYValue() {
		return maxYValue;
	}

	public void setMaxYValue(double maxYValue) {
		this.maxYValue = maxYValue;
	}

	public double getMinXValue() {
		return minXValue;
	}

	public void setMinXValue(double minXValue) {
		this.minXValue = minXValue;
	}

	public double getMinYValue() {
		return minYValue;
	}

	public void setMinYValue(double minYValue) {
		this.minYValue = minYValue;
	}
}