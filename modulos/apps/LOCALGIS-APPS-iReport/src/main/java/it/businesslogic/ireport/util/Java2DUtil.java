/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * Java2DUtil.java
 * 
 */

package it.businesslogic.ireport.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Stack;

public class Java2DUtil
{
  private static Stack clipBoundsStack = new Stack();
  private static Stack transforms = new Stack();

  public static void setClip(Graphics g, int x, int y, int width, int height)
  {
    setClip(g, new Rectangle(x, y, width, height));
  }

  public static void setClip(Graphics g, Rectangle clipBounds)
  {
    Rectangle currentClipBounds;

    clipBounds = new Rectangle(clipBounds);
    clipBounds.width += 1;
    clipBounds.height += 1;

    currentClipBounds = g.getClipBounds();
    if(currentClipBounds != null)
    {
      clipBounds = clipBounds.intersection(g.getClipBounds());
    }

    clipBoundsStack.push(currentClipBounds);
    g.setClip(clipBounds);
  }

  public static void resetClip(Graphics g)
  {
    g.setClip((Shape) clipBoundsStack.pop());
  }
  
    public static void setTransform(Graphics2D g2, AffineTransform transform)
  {
    AffineTransform current;


    current = g2.getTransform();
    transforms.push(current);
    g2.setTransform(transform);
  }


  public static void resetTransform(Graphics2D g2)
  {
    if(transforms.empty())
    {
      return;
    }


    g2.setTransform((AffineTransform) transforms.pop());
  }
}
