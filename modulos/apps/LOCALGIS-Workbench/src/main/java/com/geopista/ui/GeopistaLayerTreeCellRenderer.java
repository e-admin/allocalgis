/**
 * GeopistaLayerTreeCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;



import java.awt.Component;
import java.awt.Font;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.LayerTreeModel;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager;



public class GeopistaLayerTreeCellRenderer implements TreeCellRenderer {
    //<<TODO:NAMING>> Rename class to LayerRenderer [Jon Aquino]
    private GeopistaLayerNameRenderer layerNameRenderer = new GeopistaLayerNameRenderer();
    private JLabel rootRendererComponent = new JLabel("Root");
    private DefaultTreeCellRenderer categoryRenderer = new DefaultTreeCellRenderer();
   
    public GeopistaLayerTreeCellRenderer(IRenderingManager renderingManager) {
        layerNameRenderer.setCheckBoxVisible(true);
        layerNameRenderer.setIndicatingEditability(false);
        layerNameRenderer.setIndicatingProgress(true, renderingManager);
    }

    public GeopistaLayerNameRenderer getLayerNameRenderer() {
        return layerNameRenderer;
    }

    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {
        Object node = value;
        if (node instanceof LayerTreeModel.Root) {
            return getTreeCellRendererComponent((LayerTreeModel.Root) node);
        }

        if (node instanceof Category) {
            categoryRenderer.setBackgroundNonSelectionColor(tree.getBackground());

            JLabel categoryRendererComponent =
                (JLabel) categoryRenderer.getTreeCellRendererComponent(
                    tree,
                    value,
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);
            categoryRendererComponent.setFont(new JLabel().getFont().deriveFont(Font.BOLD));
            categoryRendererComponent.setText(((Category) node).getName());
            if (expanded) {
                categoryRendererComponent.setIcon(UIManager.getIcon("Tree.openIcon"));
            } else {
                categoryRendererComponent.setIcon(UIManager.getIcon("Tree.closedIcon"));
            }

            return categoryRendererComponent;
        }

        if (node instanceof Layerable) {
            return layerNameRenderer.getTreeCellRendererComponent(
                tree,
                value,
                selected,
                expanded,
                leaf,
                row,
                hasFocus);
        }

        if (node instanceof Map.Entry) {

            GeopistaLegendRenderer legend = new GeopistaLegendRenderer();
            return legend.getTreeCellRendererComponent(
                tree,
                value,
                selected,
                expanded,
                leaf,
                row,
                hasFocus);
        }

        Assert.shouldNeverReachHere(node.getClass().toString());

        return null;
    }

    private Component getTreeCellRendererComponent(LayerTreeModel.Root root) {
        return rootRendererComponent;
    }
}
