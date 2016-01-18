



/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.geopista.io.datasource.wms;

import java.util.Arrays;

/**
 * A Utility class to select the optimal Map Format based on some preferences.
 * @author  Chris Hodgson chodgson@refractions.net
 */
public class MapImageFormatChooser {
  
  private boolean transparencyRequired;
  private boolean useLossy;
  
  /** 
   * Creates a new instance of MapImageFormatChooser.
   */
  public MapImageFormatChooser() {
    this.transparencyRequired = false;
    this.useLossy = false;
  }
  
  /*
   * Returns true if the specified format is known by the MapFormatChooser, false 
   * otherwise. The MapFormatChooser can only reliably select between formats 
   * which it knows; it will only return an unknown format if there are no known 
   * formats to select from.
   * @param format the format which is in question
   * @return true if the specified format is known by the MapFormatChooser, false 
   *         otherwise
   */
  static public boolean isKnownFormat( String format ) {
    if( format.equals( "image/gif" ) || format.equals( "image/png" ) || format.equals( "image/jepg" ) ) {
      return true;
    }
    return false;
  }
  
  /*
   * Sets whether tranparency is required in the image format.
   * If transparency is required it takes priority over lossy compression.
   * However, if no format that supports transparency is available, the next best
   * format will be selected.
   * @param transparencyRequired true if the image format chosen needs to support 
   *                             transparency
   */
  public void setTransparencyRequired( boolean transparencyRequired ) {
    this.transparencyRequired = transparencyRequired;
  }
  
  /*
   * Sets whether lossy compression is preferred over non-lossy compression.
   * Lossy compression would generally only be preferred over a slow connection,
   * and also note that image formats which use lossy compression generally don't
   * support transparency - in this case the transparency requirement takes priority.
   * @param useLossy true if lossy compression is preferrable
   */
  public void setPreferLossyCompression( boolean useLossy ) {
    this.useLossy = useLossy;
  }
  
  /*
   * Returns a format String from the Array of available formats which best
   * matches the requirements and preferences specified. If there are no image
   * formats available which can be used by the WMS image handling code, then 
   * null is returned.
   * @param formats the array of available formats to choose from
   * @return the chosen format string from Array of available formats, or null 
   *         if none of the available formats are known
   */
  public String chooseFormat( String[] formats ) {
    if( formats.length == 0 ) {
      throw new IllegalArgumentException();
    }
    String[] order = new String[6];
    if( transparencyRequired ) {
      order[0] = "image/png";
      order[1] = "PNG";
      order[2] = "image/gif";
      order[3] = "GIF";
      order[4] = "image/jpeg";
      order[5] = "JPG";
    } else if( useLossy ) {
      order[0] = "image/jpeg";
      order[1] = "JPG";
      order[2] = "image/png";
      order[3] = "PNG";
      order[4] = "image/gif";
      order[5] = "GIF";
    } else {
      order[0] = "image/png";
      order[1] = "PNG";
      order[2] = "image/jpeg";
      order[3] = "JPG";
      order[4] = "image/gif";
      order[5] = "GIF";
    }
    Arrays.sort( formats );
    for( int i = 0; i < order.length; i++ ) {
      if( Arrays.binarySearch( formats, order[i] ) >= 0 ) {
        return order[i];
      }
    }
    return null;
  }

}
