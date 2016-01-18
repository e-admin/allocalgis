/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */

package org.agil.dxf;

import java.awt.Graphics;
import java.awt.Color;

import org.agil.*;
import org.agil.dxf.math.*;
import org.agil.dxf.reader.*;


/**
 *  DrawAble is an abstract base class for everything which can be drawn on a screen.
 *  In the moment there are only lines & groups, but maybe in the future we can show
 *  surfaces when there's a 3d api available.
 *
 */
public abstract class DrawAble {
  protected DxfLAYER       layer;   // layer reference
  protected short          color;   // DXF color

  /**
   *  Draw everything.
   *  @param  g           the graphics context
   *  @param  mat         transformation matrix
   *  @param  insLayer    layer set in the last INSERT (maybe null)
   *  @param  insColor    the color set in the last INSERT
   */
  public abstract void draw(Graphics g, Matrix4D mat, DxfLAYER insLayer,
			    DxfColorModel colorModel, short insColor);

  /**
   *  Draw reduced.
   *  @param  g           the graphics context
   *  @param  mat         transformation matrix
   *  @param  insLayer    layer set in the last INSERT (maybe null)
   *  @param  insColor    the color set in the last INSERT
   *  @param  nr          counter to see whether we draw or not (draw if nr==0)
   *  @param  maxNr       reducing
   */
  public abstract void draw(Graphics g, Matrix4D mat, DxfLAYER insLayer,
			    DxfColorModel colorModel, short insColor, int nr, int maxNr);

  /**
   *  Calculate the bounding box and the number of lines.
   *  @param  min         minimal corner so far
   *  @param  max         maximal corner so far
   *  @param  mat         transformation matrix
   *  @param  insLayer    layer set in the last INSERT (maybe null)
   *  @param  onlyVisible if <code>true</code> use only elements on
   *                      visible layers for the calculation
   *  @return number of lines
   */
  public abstract int calcBB(Point3D min, Point3D max, Matrix4D mat,
			     DxfLAYER insertLayer, boolean onlyVisible);

  /**
   *  Transform this element.
   *  @param  mat         transformation matrix
   */
  public abstract void transformBy(Matrix4D mat);

  /**
   *  Set the layer.
   *  @param  l           new layer
   */
  public void setLayer(DxfLAYER l) {
    layer = l;
  }

  /**
   *  Set the color.
   *  @param  c           new color
   */
  public void setColor(short c) {
    color = c;
  }

  /**
   *  Test the visibilty.
   *  @param  insertLayer layer of the last INSERT
   *  @return true is visible<br>
   *          false is not visible
   */
  public boolean isVisible(DxfLAYER insertLayer) {
    if (layer != null  &&  !layer.isLayer0()) {
      return layer.getVisibility();
    }
    else if (insertLayer != null) {
      return insertLayer.getVisibility();
    }
    else {
      // always draw
      return true;
    }
  }

  /**
   *  Calculate the correct color.
   *  @param  insColor    color of last INSERT
   *  @param  insLayer    layer of last INSERT
   */
  public short calcColor(short insColor, DxfLAYER insLayer) {
    short retColor;

    if (color == 0) {
      /* BYBLOCK */
      retColor = insColor;
    }
    else {
      retColor = color;
    }

    if (retColor == -1) {
      /* BYLAYER */
      if (layer != null   &&   !layer.isLayer0()) {
	retColor = layer.getColor();
      }
      else if (insLayer != null) {
	retColor = insLayer.getColor();
      }
    }

    return retColor;
  }


  /**
   *  Extrude in z direction.
   *  @param  dist      extrusion length
   *  @return new DrawSet with extruded element
   */
  public DrawAble extrude(double dist) {
    return extrude(dist, new Vector3D(0, 0, 1));
  }

  /**
   *  Extrude in arbitrary direction.
   *  @param  dist      extrusion length
   *  @param  up        extrusion direction
   *  @return new DrawSet with extruded element
   */
  public abstract DrawAble extrude(double dist, Vector3D up);

}




