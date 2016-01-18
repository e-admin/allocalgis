/**
 * AbstractFeatureTextWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import com.vividsolutions.jump.feature.Feature;

public abstract class AbstractFeatureTextWriter {
    private String description;
    private String shortDescription;
    private boolean wrapping;
    public AbstractFeatureTextWriter(boolean wrapping, String shortDescription, String description) {
        this.wrapping = wrapping;
        this.shortDescription = shortDescription;
        this.description = description;
    }
    public abstract String write(Feature feature);
    /**
     * Returns a short (2-3 letters) description to display on the button.
     */
    public String getShortDescription() { return shortDescription; }
    /**
     * Returns a description to display on the tooltip.
     */
    public String getDescription() { return description; }
    /**
     * Returns whether to wrap the text.
     */
    public boolean isWrapping() { return wrapping; }
}