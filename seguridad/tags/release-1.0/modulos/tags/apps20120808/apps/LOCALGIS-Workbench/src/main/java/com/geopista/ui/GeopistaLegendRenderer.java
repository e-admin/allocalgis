package com.geopista.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerPanel;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

public class GeopistaLegendRenderer extends JPanel implements ListCellRenderer,
    TreeCellRenderer {
    //<<TODO>> See how the colour looks with other L&F's. [Jon Aquino]
    private final static Color UNSELECTED_EDITABLE_FONT_COLOR = Color.red;
    private final static Color SELECTED_EDITABLE_FONT_COLOR = Color.yellow;
    protected JCheckBox checkBox = new JCheckBox();
    private JPanel colorPanel = new JPanel();
    GridBagLayout gridBagLayout = new GridBagLayout();
    protected JLabel label = new JLabel();
    private boolean indicatingEditability = false;
    private boolean indicatingProgress = false;
    private int progressIconSize = 13;
    private Icon[] progressIcons = new Icon[] {
            GUIUtil.resize(IconLoader.icon("ClockN.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockNE.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockE.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockSE.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockS.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockSW.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockW.gif"), progressIconSize),
            GUIUtil.resize(IconLoader.icon("ClockNW.gif"), progressIconSize)
        };
    private Icon clearProgressIcon = GUIUtil.resize(IconLoader.icon("Clear.gif"),
            progressIconSize);
    private String PROGRESS_ICON_KEY = "PROGRESS_ICON";
    private DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
    private RenderingManager renderingManager;
    private JLabel progressIconLabel = new JLabel();
    private Font font = new JLabel().getFont();
    private Font editableFont = font.deriveFont(Font.BOLD);
    private JLabel wmsIconLabel = new JLabel(MapLayerPanel.ICON);

    public GeopistaLegendRenderer() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setIndicatingEditability(boolean indicatingEditability) {
        this.indicatingEditability = indicatingEditability;
    }

    public void setIndicatingProgress(boolean indicatingProgress,
        RenderingManager renderingManager) {
        this.indicatingProgress = indicatingProgress;
        this.renderingManager = renderingManager;
    }

    public JLabel getLabel() {
        return label;
    }

    /**
     * @return relative to this panel
     */
    public Rectangle getCheckBoxBounds() {
        int i = gridBagLayout.getConstraints(checkBox).gridx;
        int x = 0;

        for (int j = 0; j < i; j++) {
            x += getColumnWidth(j);
        }

        return new Rectangle(x, 0, getColumnWidth(i), getRowHeight());
    }

    /**
     * @param i zero-based
     */
    protected int getColumnWidth(int i) {
        validateTree();

        return gridBagLayout.getLayoutDimensions()[0][i];
    }

    protected int getRowHeight() {
        validateTree();

        return gridBagLayout.getLayoutDimensions()[1][0];
    }

    public void setCheckBoxVisible(boolean checkBoxVisible) {
        checkBox.setVisible(checkBoxVisible);
    }

    /**
     * Workaround for bug 4238829 in the Java bug database:
     * "JComboBox containing JPanel fails to display selected item at creation time"
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        validate();
    }

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return defaultListCellRenderer.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);
        }

        Map.Entry layerable = (Map.Entry) value;
        label.setText((String) layerable.getKey().toString());
        setToolTipText((String) layerable.getKey().toString());

        if (isSelected) {
            label.setForeground(list.getSelectionForeground());
            label.setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
        } else {
            label.setForeground(list.getForeground());
            label.setBackground(list.getBackground());
            setForeground(list.getForeground());
            setBackground(list.getBackground());
        }

        colorPanel.setVisible(true);
        checkBox.setSelected(true);

        //TODO: recordar quitar esto porque no se usa
        if (indicatingEditability && layerable instanceof Layer &&
                ((Layer) layerable).isEditable()) {
            label.setFont(editableFont);
            label.setForeground(isSelected ? SELECTED_EDITABLE_FONT_COLOR
                                           : UNSELECTED_EDITABLE_FONT_COLOR);
        } else {
            label.setFont(font);
        }

        /*wmsIconLabel.setVisible(layerable instanceof WMSLayer);

        //Only show the progress icon (clocks) for WMSLayers, not Layers.
        //Otherwise it's too busy. [Jon Aquino]
        if (layerable instanceof WMSLayer && indicatingProgress &&
                (renderingManager.getRenderer(layerable) != null) &&
                renderingManager.getRenderer(layerable).isRendering()) {
            layerable.getBlackboard().put(PROGRESS_ICON_KEY,
                layerable.getBlackboard().get(PROGRESS_ICON_KEY, 0) + 1);

            if (layerable.getBlackboard().getInt(PROGRESS_ICON_KEY) > (progressIcons.length -
                    1)) {
                layerable.getBlackboard().put(PROGRESS_ICON_KEY, 0);
            }

            progressIconLabel.setIcon(progressIcons[layerable.getBlackboard()
                                                             .getInt(PROGRESS_ICON_KEY)]);
        } else {
            progressIconLabel.setIcon(clearProgressIcon);
        }*/

        Color backgroundColor = list.getBackground();
        Color selectionBackgroundColor = list.getSelectionBackground();

        if (layerable instanceof Map.Entry) {
            Color backColor = ((BasicStyle) layerable.getValue()).getFillColor();
            colorPanel.setBackground(backColor);
            colorPanel.setMaximumSize(new Dimension(10,10));
            colorPanel.setMinimumSize(new Dimension(10,10));
        }

        return this;
    }

    private JList list(JTree tree) {
        JList list = new JList();
        list.setForeground(tree.getForeground());
        list.setBackground(tree.getBackground());
        list.setSelectionForeground(UIManager.getColor(
                "Tree.selectionForeground"));
        list.setSelectionBackground(UIManager.getColor(
                "Tree.selectionBackground"));

        return list;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row,
        boolean hasFocus) {
        Map.Entry layerable = (Map.Entry) value;
        getListCellRendererComponent(list(tree), layerable, -1, selected,
            hasFocus);

        if (selected) {
            label.setForeground(UIManager.getColor("Tree.selectionForeground"));
            label.setBackground(UIManager.getColor("Tree.selectionBackground"));
            setForeground(UIManager.getColor("Tree.selectionForeground"));
            setBackground(UIManager.getColor("Tree.selectionBackground"));
        } else {
            label.setForeground(tree.getForeground());
            label.setBackground(tree.getBackground());
            setForeground(tree.getForeground());
            setBackground(tree.getBackground());
        }

        if (indicatingEditability && layerable instanceof Layer) {
            if (((Layer) layerable).isEditable()) {
                label.setForeground(selected ? SELECTED_EDITABLE_FONT_COLOR
                                             : UNSELECTED_EDITABLE_FONT_COLOR);
            }
        }

        return this;
    }

    void jbInit() throws Exception {
        checkBox.setVisible(true);
        this.setLayout(gridBagLayout);
        label.setOpaque(false);
        label.setText("Layer Name Goes Here");
        checkBox.setOpaque(false);
        /*this.add(progressIconLabel,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 2), 0, 0));
        this.add(wmsIconLabel,
            new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 2), 0, 0));*/
        this.add(colorPanel,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));
        this.add(checkBox,
            new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(label,
            new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
    }
}

