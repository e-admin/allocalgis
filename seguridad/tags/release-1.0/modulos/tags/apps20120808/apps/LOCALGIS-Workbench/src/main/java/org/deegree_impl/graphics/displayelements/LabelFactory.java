package org.deegree_impl.graphics.displayelements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.displayelements.Label;
import org.deegree.graphics.displayelements.LabelDisplayElement;
import org.deegree.graphics.sld.Halo;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.LinePlacement;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.graphics.transformation.GeoTransform;
import org.deegree.model.feature.Feature;
import org.deegree.model.geometry.GM_Curve;
import org.deegree.model.geometry.GM_Exception;
import org.deegree.model.geometry.GM_LineString;
import org.deegree.model.geometry.GM_MultiCurve;
import org.deegree.model.geometry.GM_MultiSurface;
import org.deegree.model.geometry.GM_Object;
import org.deegree.model.geometry.GM_Point;
import org.deegree.model.geometry.GM_Position;
import org.deegree.model.geometry.GM_Surface;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.tools.Debug;

/**
 * Does the labeling, i.e. creates (screen) <code>Label</code> representations
 * from <code>LabelDisplayElement</code>s.
 * 
 * Different geometry-types (of the LabelDisplayElement) imply different
 * strategies concerning the way the <code>Labels</code> are generated.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 1.1 $ $Date: 2009/07/03 12:32:05 $
 */
public class LabelFactory {

    /**
     * @param caption
     * @param font
     * @param color
     * @param metrics
     * @param feature
     * @param halo
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rotation
     * @param anchorPointX
     * @param anchorPointY
     * @param displacementX
     * @param displacementY
     * @return
     */
    public static Label createLabel(String caption, Font font, Color color,
            LineMetrics metrics, Feature feature, Halo halo, int x, int y,
            int w, int h, double rotation, double anchorPointX,
            double anchorPointY, double displacementX, double displacementY) {
        if (rotation == 0.0) {
            return new HorizontalLabel(caption, font, color, metrics, feature,
                    halo, x, y, w, h,
                    new double[] { anchorPointX, anchorPointY }, new double[] {
                            displacementX, displacementY });
        }

        return new RotatedLabel(caption, font, color, metrics, feature, halo,
                x, y, w, h, rotation,
                new double[] { anchorPointX, anchorPointY }, new double[] {
                        displacementX, displacementY });
    }

    /**
     * @param caption
     * @param font
     * @param color
     * @param metrics
     * @param feature
     * @param halo
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rotation
     * @param anchorPoint
     * @param displacement
     * @return
     */
    public static Label createLabel(String caption, Font font, Color color,
            LineMetrics metrics, Feature feature, Halo halo, int x, int y,
            int w, int h, double rotation, double[] anchorPoint,
            double[] displacement) {
        if (rotation == 0.0) {
            return new HorizontalLabel(caption, font, color, metrics, feature,
                    halo, x, y, w, h, anchorPoint, displacement);
        }

        return new RotatedLabel(caption, font, color, metrics, feature, halo,
                x, y, w, h, rotation, anchorPoint, displacement);
    }

    /**
     * Generates <code>Label</code> -representations for a given
     * <code>LabelDisplayElement</code>.
     * 
     * @param element
     * @param projection
     * @param g
     * @return
     * @throws Exception
     */
    public static Label[] createLabels(LabelDisplayElement element,
            GeoTransform projection, Graphics2D g) throws Exception {

        Label[] labels = new Label[0];
        Feature feature = element.getFeature();
        String caption = element.getLabel().evaluate(feature);

        // sanity check: empty labels are ignored
        if (caption == null || caption.trim().equals("")) {
            return labels;
        }

        GM_Object geometry = element.getGeometry();
        TextSymbolizer symbolizer = (TextSymbolizer) element.getSymbolizer();

        // gather font information
        org.deegree.graphics.sld.Font sldFont = symbolizer.getFont();
        java.awt.Font font = new java.awt.Font(sldFont.getFamily(feature),
                sldFont.getStyle(feature) | sldFont.getWeight(feature), sldFont
                        .getSize(feature));
        g.setFont(font);
        FontRenderContext frc = g.getFontRenderContext();
        //Rectangle2D bounds = font.getStringBounds(caption, frc);
        LineMetrics metrics = font.getLineMetrics(caption, frc);
    	/****************************
		 * JPC: support multiline string
		 *///     JPC: manage multiple lines captions
        String[] captionlines=caption.split("\\n|\\\\n");
        Rectangle2D bounds = font.getStringBounds(captionlines[0], frc);
        for (int i = 1; i < captionlines.length; i++)
			{
			Rectangle2D boundofline = font.getStringBounds(captionlines[i], frc);
			bounds.add(bounds.getMaxX()>boundofline.getMaxX()?0:boundofline.getMaxX()-bounds.getMaxX(),
					boundofline.getHeight());			
			}
        // end JPC
        int w = (int) bounds.getWidth();
        int h = (int) bounds.getHeight();
        //int descent = (int) metrics.getDescent ();

        if (geometry instanceof GM_Point) {

            // get screen coordinates
            int[] coords = calcScreenCoordinates(projection, geometry);
            int x = coords[0];
            int y = coords[1];

            // default placement information
            double rotation = 0.0;
            double[] anchorPoint = { 0.0, 0.5 };
            double[] displacement = { 0.0, 0.0 };

            // use placement information from SLD
            LabelPlacement lPlacement = symbolizer.getLabelPlacement();

            if (lPlacement != null) {
                PointPlacement pPlacement = lPlacement.getPointPlacement();
                anchorPoint = pPlacement.getAnchorPoint(feature);
                displacement = pPlacement.getDisplacement(feature);
                rotation = pPlacement.getRotation(feature);
            }
            labels = new Label[] { createLabel(caption, font, sldFont
                    .getColor(feature), metrics, feature, symbolizer.getHalo(),
                    x, y, w, h, rotation, anchorPoint, displacement) };
        } else if (geometry instanceof GM_Surface
                || geometry instanceof GM_MultiSurface) {

            // get screen coordinates
            int[] coords = calcScreenCoordinates(projection, geometry);
            int x = coords[0];
            int y = coords[1];

            // default placement information
            double rotation = 0.0;
            double[] anchorPoint = { 0.5, 0.5 };
            double[] displacement = { 0.0, 0.0 };

            // use placement information from SLD
            LabelPlacement lPlacement = symbolizer.getLabelPlacement();

            if (lPlacement != null) {
                PointPlacement pPlacement = lPlacement.getPointPlacement();

                // check if the label is to be centered within the intersection
                // of
                // the screen surface and the polygon geometry
                if (pPlacement.isAuto()) {
                    GM_Surface screenSurface = GeometryFactory
                            .createGM_Surface(projection.getSourceRect(), null);
                    GM_Object intersection = screenSurface
                            .intersection(geometry);
                    if (intersection != null) {
                        GM_Position source = intersection.getCentroid()
                                .getPosition();
                        x = (int) (projection.getDestX(source.getX()) + 0.5);
                        y = (int) (projection.getDestY(source.getY()) + 0.5);
                    }
                }
                anchorPoint = pPlacement.getAnchorPoint(feature);
                displacement = pPlacement.getDisplacement(feature);
                rotation = pPlacement.getRotation(feature);
            }

            labels = new Label[] { createLabel(caption, font, sldFont
                    .getColor(feature), metrics, feature, symbolizer.getHalo(),
                    x, y, w, h, rotation, anchorPoint, displacement)

            };
        } else if (geometry instanceof GM_Curve
                || geometry instanceof GM_MultiCurve) {
            GM_Surface screenSurface = GeometryFactory.createGM_Surface(
                    projection.getSourceRect(), null);
            GM_Object intersection = screenSurface.intersection(geometry);
            if (intersection != null) {
                List list = null;
                if (intersection instanceof GM_Curve) {
                    list = createLabels((GM_Curve) intersection, element, g,
                            projection);
                } else if (intersection instanceof GM_MultiCurve) {
                    list = createLabels((GM_MultiCurve) intersection, element,
                            g, projection);
                } else {
                    throw new Exception(
                            "Intersection produced unexpected geometry type: '"
                                    + intersection.getClass().getName() + "'!");
                }
                labels = new Label[list.size()];
                for (int i = 0; i < labels.length; i++) {
                    Label label = (Label) list.get(i);
                    labels[i] = label;
                }
            }
        } else {
            throw new Exception("LabelFactory does not implement generation "
                    + "of Labels from geometries of type: '"
                    + geometry.getClass().getName() + "'!");
        }
        return labels;
    }

    /**
     * Determines positions on the given <code>GM_MultiCurve</code> where a
     * caption could be drawn. For each of this positons, three candidates are
     * produced; one on the line, one above of it and one below.
     * 
     * @param multiCurve
     * @param element
     * @param g
     * @param projection
     * @return ArrayList containing Arrays of Label-objects
     * @throws FilterEvaluationException
     */
    public static List createLabels(GM_MultiCurve multiCurve,
            LabelDisplayElement element, Graphics2D g, GeoTransform projection)
            throws FilterEvaluationException {

        List placements = Collections.synchronizedList(new ArrayList());
        for (int i = 0; i < multiCurve.getSize(); i++) {
            GM_Curve curve = multiCurve.getCurveAt(i);
            placements.addAll(createLabels(curve, element, g, projection));
        }
        return placements;
    }

    /**
     * Determines positions on the given <code>GM_Curve</code> where a caption
     * could be drawn. For each of this positons, three candidates are produced;
     * one on the line, one above of it and one below.
     * 
     * @param curve
     * @param element
     * @param g
     * @param projection
     * @return ArrayList containing Arrays of Label-objects
     * @throws FilterEvaluationException
     */
    public static ArrayList createLabels(GM_Curve curve,
            LabelDisplayElement element, Graphics2D g, GeoTransform projection)
            throws FilterEvaluationException {

        Feature feature = element.getFeature();

        // determine the placement type and parameters from the TextSymbolizer
        double perpendicularOffset = 0.0;
        int placementType = LinePlacement.TYPE_ABSOLUTE;
        double lineWidth = 3.0;
        int gap = 6;
        TextSymbolizer symbolizer = ((TextSymbolizer) element.getSymbolizer());
        if (symbolizer.getLabelPlacement() != null) {
            LinePlacement linePlacement = symbolizer.getLabelPlacement()
                    .getLinePlacement();
            if (linePlacement != null) {
                placementType = linePlacement.getPlacementType(element
                        .getFeature());
                perpendicularOffset = linePlacement
                        .getPerpendicularOffset(element.getFeature());
                lineWidth = linePlacement.getLineWidth(element.getFeature());
                gap = linePlacement.getGap(element.getFeature());
            }
        }

        // get width & height of the caption
        String caption = element.getLabel().evaluate(element.getFeature());
        org.deegree.graphics.sld.Font sldFont = symbolizer.getFont();
        java.awt.Font font = new java.awt.Font(sldFont.getFamily(element
                .getFeature()), sldFont.getStyle(element.getFeature())
                | sldFont.getWeight(element.getFeature()), sldFont
                .getSize(element.getFeature()));
        g.setFont(font);
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(caption, frc);
        LineMetrics metrics = font.getLineMetrics(caption, frc);
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        // get screen coordinates of the line
        int[][] pos = calcScreenCoordinates(projection, curve);

        // ideal distance from the line
        double delta = height / 2.0 + lineWidth / 2.0;

        // walk along the linestring and "collect" possible placement positions
        int w = (int) width;
        int lastX = pos[0][0];
        int lastY = pos[1][0];
        int count = pos[2][0];
        int boxStartX = lastX;
        int boxStartY = lastY;

        ArrayList labels = new ArrayList(100);
        List eCandidates = Collections.synchronizedList(new ArrayList(100));
        int i = 0;
        int kk = 0;
        while (i < count && kk < 100) {
            kk++;
            int x = pos[0][i];
            int y = pos[1][i];

            // segment found where endpoint of box should be located?
            if (getDistance(boxStartX, boxStartY, x, y) >= w) {

                int[] p0 = new int[] { boxStartX, boxStartY };
                int[] p1 = new int[] { lastX, lastY };
                int[] p2 = new int[] { x, y };

                int[] p = findPointWithDistance(p0, p1, p2, w);
                x = p[0];
                y = p[1];

                lastX = x;
                lastY = y;
                int boxEndX = x;
                int boxEndY = y;

                // does the linesegment run from right to left?
                if (x <= boxStartX) {
                    boxEndX = boxStartX;
                    boxEndY = boxStartY;
                    boxStartX = x;
                    boxStartY = y;
                    x = boxEndX;
                    y = boxEndY;
                }

                double rotation = getRotation(boxStartX, boxStartY, x, y);
                double[] deviation = calcDeviation(new int[] { boxStartX,
                        boxStartY }, new int[] { boxEndX, boxEndY },
                        eCandidates);

                Label label = null;
                
                switch (placementType) {
                case LinePlacement.TYPE_ABSOLUTE: {
                    label = createLabel(caption, font, sldFont
                            .getColor(feature), metrics, feature, symbolizer
                            .getHalo(), boxStartX, boxStartY, (int) width,
                            (int) height, rotation, 0.0, 0.5, (w - width) / 2,
                            perpendicularOffset);
                    break;
                }
                case LinePlacement.TYPE_ABOVE: {
                    label = createLabel(caption, font, sldFont
                            .getColor(feature), metrics, feature, symbolizer
                            .getHalo(), boxStartX, boxStartY, (int) width,
                            (int) height, rotation, 0.0, 0.5, (w - width) / 2,
                            delta + deviation[0]);
                    break;
                }
                case LinePlacement.TYPE_BELOW:
                case LinePlacement.TYPE_AUTO: {
                    label = createLabel(caption, font, sldFont
                            .getColor(feature), metrics, feature, symbolizer
                            .getHalo(), boxStartX, boxStartY, (int) width,
                            (int) height, rotation, 0.0, 0.5, (w - width) / 2,
                            -delta - deviation[1]);
                    break;
                }
                case LinePlacement.TYPE_CENTER: {
                    label = createLabel(caption, font, sldFont
                            .getColor(feature), metrics, feature, symbolizer
                            .getHalo(), boxStartX, boxStartY, (int) width,
                            (int) height, rotation, 0.0, 0.5, (w - width) / 2,
                            0.0);
                    break;
                }
                default: {
                }
                }
                labels.add(label);
                boxStartX = lastX;
                boxStartY = lastY;
                eCandidates.clear();
            } else {
                eCandidates.add(new int[] { x, y });
                lastX = x;
                lastY = y;
                i++;
            }
        }

        // pick lists of boxes on the linestring
        ArrayList pick = new ArrayList(100);
        int n = labels.size();
        for (int j = n / 2; j < labels.size(); j += (gap + 1)) {
            pick.add(labels.get(j));
        }
        for (int j = n / 2 - (gap + 1); j > 0; j -= (gap + 1)) {
            pick.add(labels.get(j));
        }
        return pick;
    }

    /**
     * Calculates the maximum deviation that points on a linestring have to the
     * ideal line between the starting point and the end point.
     * 
     * The ideal line is thought to be running from left to right, the left
     * deviation value generally is above the line, the right value is below.
     * 
     * @param start
     *            starting point of the linestring
     * @param end
     *            end point of the linestring
     * @param points
     *            points in between
     * @return
     */
    public static double[] calcDeviation(int[] start, int[] end, List points) {

        // extreme deviation to the left
        double d1 = 0.0;
        // extreme deviation to the right
        double d2 = 0.0;
        Iterator it = points.iterator();

        // eventually swap start and end point
        if (start[0] > end[0]) {
            int[] tmp = start;
            start = end;
            end = tmp;
        }

        if (start[0] != end[0]) {
            // label orientation is not completly vertical
            if (start[1] != end[1]) {
                // label orientation is not completly horizontal
                while (it.hasNext()) {
                    int[] point = (int[]) it.next();
                    double u = ((double) end[1] - (double) start[1])
                            / ((double) end[0] - (double) start[0]);
                    double x = (u * u * start[0] - u
                            * ((double) start[1] - (double) point[1]) + point[0])
                            / (1.0 + u * u);
                    double y = (x - start[0]) * u + start[1];
                    double d = getDistance(point, new int[] { (int) (x + 0.5),
                            (int) (y + 0.5) });
                    if (y >= point[1]) {
                        // candidate for left extreme value
                        if (d > d1) {
                            d1 = d;
                        }
                    } else if (d > d2) {
                        // candidate for right extreme value
                        d2 = d;
                    }
                }
            } else {
                // label orientation is completly horizontal
                while (it.hasNext()) {
                    int[] point = (int[]) it.next();
                    double d = point[1] - start[1];
                    if (d < 0) {
                        // candidate for left extreme value
                        if (-d > d1) {
                            d1 = -d;
                        }
                    } else if (d > d2) {
                        // candidate for left extreme value
                        d2 = d;
                    }
                }
            }
        } else {
            // label orientation is completly vertical
            while (it.hasNext()) {
                int[] point = (int[]) it.next();
                double d = point[0] - start[0];
                if (d < 0) {
                    // candidate for left extreme value
                    if (-d > d1) {
                        d1 = -d;
                    }
                } else if (d > d2) {
                    // candidate for right extreme value
                    d2 = d;
                }
            }
        }
        return new double[] { d1, d2 };
    }

    /**
     * Finds a point on the line between p1 and p2 that has a certain distance
     * from point p0 (provided that there is such a point).
     * 
     * @param p0
     *            point that is used as reference point for the distance
     * @param p1
     *            starting point of the line
     * @param p2
     *            end point of the line
     * @param d
     *            distance
     * @return
     */
    public static int[] findPointWithDistance(int[] p0, int[] p1, int[] p2,
            int d) {

        double x, y;
        double x0 = p0[0];
        double y0 = p0[1];
        double x1 = p1[0];
        double y1 = p1[1];
        double x2 = p2[0];
        double y2 = p2[1];

        if (x1 != x2) {
            // line segment does not run vertical
            double u = (y2 - y1) / (x2 - x1);
            double p = -2 * (x0 + u * u * x1 - u * (y1 - y0)) / (u * u + 1);
            double q = ((y1 - y0) * (y1 - y0) + u * u * x1 * x1 + x0 * x0 - 2
                    * u * x1 * (y1 - y0) - d * d)
                    / (u * u + 1);
            double minX = x1;
            double maxX = x2;
            double minY = y1;
            double maxY = y2;
            if (minX > maxX) {
                minX = x2;
                maxX = x1;
            }
            if (minY > maxY) {
                minY = y2;
                maxY = y1;
            }
            x = -p / 2 - Math.sqrt((p / 2) * (p / 2) - q);
            if (x < minX || x > maxX) {
                x = -p / 2 + Math.sqrt((p / 2) * (p / 2) - q);
            }
            y = (x - x1) * u + y1;
        } else {
            // vertical line segment
            x = x1;
            double minY = y1;
            double maxY = y2;

            if (minY > maxY) {
                minY = y2;
                maxY = y1;
            }

            double p = -2 * y0;
            double q = y0 * y0 + (x1 - x0) * (x1 - x0) - d * d;

            y = -p / 2 - Math.sqrt((p / 2) * (p / 2) - q);
            if (y < minY || y > maxY) {
                y = -p / 2 + Math.sqrt((p / 2) * (p / 2) - q);
            }
        }
        return new int[] { (int) (x + 0.5), (int) (y + 0.5) };
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getRotation(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        Debug.debugMethodEnd();
        return Math.toDegrees(Math.atan(dy / dx));
    }

    /**
     * @param p1
     * @param p2
     * @return
     */
    public static double getDistance(int[] p1, int[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the screen coordinates of the given <code>GM_Curve</code>.
     */
    public static int[][] calcScreenCoordinates(GeoTransform projection,
            GM_Curve curve) {

        GM_LineString lineString = null;
        try {
            lineString = curve.getAsLineString();
        } catch (GM_Exception e) {
        }

        int count = lineString.getNumberOfPoints();
        int[][] pos = new int[3][];
        pos[0] = new int[count];
        pos[1] = new int[count];
        pos[2] = new int[1];

        int k = 0;
        for (int i = 0; i < count; i++) {
            GM_Position position = lineString.getPositionAt(i);
            double tx = projection.getDestX(position.getX());
            double ty = projection.getDestY(position.getY());
            if (i > 0) {
                if (getDistance(tx, ty, pos[0][k - 1], pos[1][k - 1]) > 1) {
                    pos[0][k] = (int) (tx + 0.5);
                    pos[1][k] = (int) (ty + 0.5);
                    k++;
                }
            } else {
                pos[0][k] = (int) (tx + 0.5);
                pos[1][k] = (int) (ty + 0.5);
                k++;
            }
        }
        pos[2][0] = k;

        Debug.debugMethodEnd();

        return pos;
    }

    /**
     * Returns the physical (screen) coordinates.
     */
    public static int[] calcScreenCoordinates(GeoTransform projection,
            GM_Object geometry) {

        int[] coords = new int[2];

        GM_Position source = null;
        if (geometry instanceof GM_Point) {
            source = ((GM_Point) geometry).getPosition();
        } else if (geometry instanceof GM_Curve
                || geometry instanceof GM_MultiCurve) {
            source = geometry.getCentroid().getPosition();
        } else {
            source = geometry.getCentroid().getPosition();
        }

        coords[0] = (int) (projection.getDestX(source.getX()) + 0.5);
        coords[1] = (int) (projection.getDestY(source.getY()) + 0.5);
        return coords;
    }
}