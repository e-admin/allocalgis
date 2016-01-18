/**
 * GraticuleCreatorEngine.java
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;


public class GraticuleCreatorEngine {
    private Coordinate southwestCornerOfLeftLayer = new Coordinate(1661000, 5092500);
    private int layerHeightInCells = 5;
    private int layerWidthInCells = 8;
    private double cellSideLengthX = 500;
    private double cellSideLengthY = 500;
    private int attribute_id;
    private static String graticuleName = "cuadricula";
    
    public final static String ATR_GEOMETRY = "GEOMETRY";
    public final static String ATR_CELL_ID = "CELL_ID";

	private GeometryFactory factory = new GeometryFactory();

    public GraticuleCreatorEngine() {
    }

    public void setSouthwestCornerOfLeftLayer(
        Coordinate newSouthwestCornerOfLeftLayer) {
        southwestCornerOfLeftLayer = newSouthwestCornerOfLeftLayer;
    }

    public void setLayerHeightInCells(int newLayerHeightInCells) {
        layerHeightInCells = newLayerHeightInCells;
    }

    public void setLayerWidthInCells(int newLayerWidthInCells) {
        layerWidthInCells = newLayerWidthInCells;
    }

    public void setCellSideLengthX(double newCellSideLengthX) {
        cellSideLengthX = newCellSideLengthX;
    }

    public void setCellSideLengthY(double newCellSideLengthY) {
        cellSideLengthY = newCellSideLengthY;
    }

    public Coordinate getSouthwestCornerOfLeftLayer() {
        return southwestCornerOfLeftLayer;
    }

    public int getLayerHeightInCells() {
        return layerHeightInCells;
    }

    public int getLayerWidthInCells() {
        return layerWidthInCells;
    }

    public double getCellSideLengthX() {
        return cellSideLengthX;
    }

    public double getCellSideLengthY() {
        return cellSideLengthY;
    }

    public void execute(PlugInContext context) {
        FeatureSchema featureSchema = new FeatureSchema();
        featureSchema.addAttribute(ATR_GEOMETRY, AttributeType.GEOMETRY);
        featureSchema.addAttribute(ATR_CELL_ID, AttributeType.INTEGER);
        attribute_id = 1;
        FeatureCollection myfeatureCollection = new FeatureDataset(featureSchema);
        addMySquareCells(myfeatureCollection);
        LayerManager layerManager = context.getLayerManager();
        Layer graticuleLayer = layerManager.getLayer(graticuleName);
        //si existe una cuadrícula la borramos
        if(graticuleLayer!=null){
        	layerManager.remove(graticuleLayer);
        }
        context.addLayer(StandardCategoryNames.WORKING, graticuleName,
            myfeatureCollection);
        //modificamos las características de la capa añadida
        graticuleLayer = layerManager.getLayer(graticuleName);
        LabelStyle labelStyle = graticuleLayer.getLabelStyle();
        labelStyle.setColor(Color.RED);
        labelStyle.setHeight(labelStyle.getHeight() * 4);
        labelStyle.setAttribute(ATR_CELL_ID);
        labelStyle.setEnabled(true);
		//evitamos que se modifiquen las cuadrículas
		graticuleLayer.setEditable(false);
    }

    private void addMySquareCells(FeatureCollection myfeatureCollection) {
        addSquareCells(myfeatureCollection, southwestCornerOfLeftLayer);
    }

    private void addSquareCells(FeatureCollection featureCollection,
        Coordinate southwestCornerOfLayer) {
    	for (int j = 0; j < layerHeightInCells; j++) {
        	for (int i = 0; i < layerWidthInCells; i++) {
                add(squareCell(i, j, southwestCornerOfLayer), featureCollection,attribute_id);
                attribute_id = (attribute_id + 1);
            }
        }
    }

    private void add(Polygon polygon, FeatureCollection featureCollection, int attr_id) {
        Feature feature = new BasicFeature(featureCollection.getFeatureSchema());
        int idIndex = featureCollection.getFeatureSchema().getAttributeIndex(ATR_CELL_ID);
        feature.setGeometry(polygon);
        feature.setAttribute(idIndex, new Integer(attr_id));
        featureCollection.add(feature);
    }

    private Polygon squareCell(int i, int j, Coordinate southwestCornerOfLayer) {
        return squareCell(southwestCornerOfLayer.x + (i * cellSideLengthX),
            southwestCornerOfLayer.y + (j * cellSideLengthY));
    }

    private Polygon squareCell(double west, double south) {
        ArrayList coordinates = new ArrayList();
            coordinates.add(new Coordinate(west,south));
            coordinates.add(new Coordinate(west,south+cellSideLengthY));
            coordinates.add(new Coordinate(west+cellSideLengthX,south+cellSideLengthY));
            coordinates.add(new Coordinate(west+cellSideLengthX,south));
         coordinates.add(coordinates.get(0));

        return polygon(coordinates);
    }

    private Polygon polygon(List coordinates) {
        Coordinate[] coordinateArray = (Coordinate[]) coordinates.toArray(new Coordinate[] {

                });

        return factory.createPolygon(factory.createLinearRing(coordinateArray),
            null);
    }
    
    public static String getGraticuleName() {
		return graticuleName;
	}

	public static void setGraticuleName(String graticuleName) {
		graticuleName = graticuleName;
	}
}