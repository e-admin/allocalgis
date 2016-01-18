/**
 * CheckBoxNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.beans;

public class CheckBoxNode {
	  private String text;

	  private boolean selected;

	private boolean enabled;

	public CheckBoxNode(String text, boolean selected, boolean enabled) {
	    this.text = text;
	    this.selected = selected;
	    this.enabled = enabled;
	  }

	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean newValue) {
	    selected = newValue;
	  }

	  public String getText() {
	    return text;
	  }

	  public void setText(String newValue) {
	    text = newValue;
	  }

	  public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	  public String toString() {
	    return getClass().getName() + "[" + text + "/" + selected + "]";
	  }
	}
