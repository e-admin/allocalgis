/**
 * LayerStyleData.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import java.io.Serializable;

public class LayerStyleData implements Serializable
{
  private String idStyle;
  private String layerPosition;
  private String idLayerFamily;
  private String styleName;
  private String idLayer;
  private boolean isActive;
  private boolean isVisible;
  private boolean isEditable;



/**
 * @return Returns the isActive.
 */
public boolean isActive()
{
    return isActive;
}
/**
 * @param isActive The isActive to set.
 */
public void setActive(boolean isActive)
{
    this.isActive = isActive;
}

  public boolean isVisible() {
	return isVisible;
}
public void setVisible(boolean isVisible) {
	this.isVisible = isVisible;
}

public boolean isEditable() {
	return isEditable;
}
public void setEditable(boolean isEditable) {
	this.isEditable = isEditable;
}

public String getIdStyle()
  {
    return idStyle;
  }

  public void setIdStyle(String newIdStyle)
  {
    idStyle = newIdStyle;
  }

  public String getLayerPosition()
  {
    return layerPosition;
  }

  public void setLayerPosition(String newLayerPosition)
  {
    layerPosition = newLayerPosition;
  }

  public String getIdLayerFamily()
  {
    return idLayerFamily;
  }

  public void setIdLayerFamily(String newIdLayerFamily)
  {
    idLayerFamily = newIdLayerFamily;
  }

  public String getStyleName()
  {
    return styleName;
  }

  public void setStyleName(String newStyleName)
  {
    styleName = newStyleName;
  }

  public String getIdLayer()
  {
    return idLayer;
  }

  public void setIdLayer(String newIdLayer)
  {
    idLayer = newIdLayer;
  }
}