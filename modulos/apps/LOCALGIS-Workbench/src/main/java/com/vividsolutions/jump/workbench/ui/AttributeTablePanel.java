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
package com.vividsolutions.jump.workbench.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;
import com.geopista.model.IGeopistaLayer;
import com.geopista.style.sld.ui.impl.ComboEditor;
import com.geopista.style.sld.ui.impl.ComboRender;
import com.geopista.style.sld.ui.impl.JComboEditorRenderTableDomain;
import com.geopista.ui.plugin.edit.EditSelectedFeaturePlugIn;
import com.vividsolutions.jump.util.FlexibleDateParser;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

/**
 *  Implements an AttributeTable panel.
 *  Table-size changes are absorbed by the last column.
 *  Rows are striped for non-editable table.
 */

public class AttributeTablePanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private class MyTable extends JTable {
        public MyTable() {
            //We want table-size changes to be absorbed by the last column.
            //By default, AUTO_RESIZE_LAST_COLUMN will not achieve this
            //(it works for column-size changes only). But I am overriding
            //#sizeColumnsToFit (for J2SE 1.3) and
            //JTableHeader#getResizingColumn (for J2SE 1.4)
            //#so that it will work for table-size changes. [Jon Aquino]
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            GUIUtil.doNotRoundDoubles(this);
            setDefaultEditor(Date.class, new FlexibleDateParser.CellEditor());
        }
        //Row-stripe colour recommended in
        //Java Look and Feel Design Guidelines: Advanced Topics [Jon Aquino]
        private final Color LIGHT_GRAY = new Color(230, 230, 230);
        private DefaultTableCellRenderer defaultTableCellRenderer =
            new DefaultTableCellRenderer();
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (!isEditButtonColumn(column)) {
                JComponent renderer = (JComponent) super.getCellRenderer(row, column);
                //If not editable, use row striping, as recommended in
                //Java Look and Feel Design Guidelines: Advanced Topics [Jon Aquino]
                renderer.setBackground(
                    (AttributeTablePanel.this.getModel().getLayer().isEditable()
                        || ((row % 2) == 0))
                        ? Color.white
                        : LIGHT_GRAY);
                return (TableCellRenderer) renderer;
            }
            return new TableCellRenderer() {
                private JButton button = new JButton(IconLoader.icon("Pencil.gif"));
                public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                    button.setToolTipText("Edit Geometry");
                    return button;
                }
            };
        }
    };
    private boolean columnWidthsInitialized = false;
    private MyTable table = new MyTable();
    private TableCellRenderer headerRenderer = new TableCellRenderer() {
        private Icon clearIcon = IconLoader.icon("Clear.gif");
        private Icon downIcon = IconLoader.icon("Down.gif");
        private TableCellRenderer originalRenderer =
            table.getTableHeader().getDefaultRenderer();
        private Icon upIcon = IconLoader.icon("Up.gif");
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
            JLabel label =
                (JLabel) originalRenderer.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column);
            if ((getModel().getSortedColumnName() == null)
                || !getModel().getSortedColumnName().equals(table.getColumnName(column))) {
                label.setIcon(clearIcon);
            } else if (getModel().isSortAscending()) {
                label.setIcon(upIcon);
            } else {
                label.setIcon(downIcon);
            }
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            return label;
        }
    };
    private LayerNameRenderer layerNameRenderer = new LayerNameRenderer();
    private ArrayList listeners = new ArrayList();
    private WorkbenchContext workbenchContext;
    EditSelectedFeaturePlugIn editFeaturePlugIn = new EditSelectedFeaturePlugIn() {
        protected Layer layer(PlugInContext context) {
                //Hopefully nobody will ever delete or rename the superclass' #layer method.
        //[Jon Aquino]
    return getModel().getLayer();
        }
    };
    public AttributeTablePanel(
        final LayerTableModel model,
        final WorkbenchContext workbenchContext) {
        this();
        updateGrid(model.getLayer());
        model.getLayer().getLayerManager().addLayerListener(new LayerListener() {
            public void categoryChanged(CategoryEvent e) {}
            public void featuresChanged(FeatureEvent e) {}
            public void layerChanged(LayerEvent e) {
                if (e.getLayerable() != model.getLayer()) {
                    return;
                }
                if (e.getType() == LayerEventType.METADATA_CHANGED) {
                    //If layer becomes editable, apply row striping and remove gridlines,
                    //as recommended in Java Look and Feel Design Guidelines: Advanced Topics [Jon Aquino]
                    updateGrid(model.getLayer());
                    repaint();
                }
            }
        });
        try {
            JList list = new JList();
            list.setBackground(new JLabel().getBackground());
            layerNameRenderer.getListCellRendererComponent(
                list,
                model.getLayer(),
                -1,
                false,
                false);
            table.setModel(model);
            model.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                        //Structure changed (LayerTableModel specifies HEADER_ROW).
                        //Add this listener after the table adds its listeners
                        //(in table.setModel above) so that this listener will initialize the column
                        //widths after the table re-adds the columns. [Jon Aquino]
                        initColumnWidths();
                    }
                }
            });
            layerNameRenderer.getLabel().setFont(
                layerNameRenderer.getLabel().getFont().deriveFont(Font.BOLD));
            model.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    updateLabel();
                }
            });
            updateLabel();
            this.workbenchContext = workbenchContext;
            table.setSelectionModel(new SelectionModelWrapper(this));
            table.getTableHeader().setDefaultRenderer(headerRenderer);
            setCellEditors(model.getLayer());
            initColumnWidths();
            setToolTips();
            setBorder(
                BorderFactory.createMatteBorder(
                    0,
                    5,
                    0,
                    0,
                    new FeatureInfoWriter().sidebarColor(model.getLayer())));
            table.getTableHeader().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    try {
                        int column = table.columnAtPoint(e.getPoint());
                        if (isEditButtonColumn(column)) {
                            return;
                        }
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            model.sort(table.getColumnName(column));
                        }
                    } catch (Throwable t) {
                        workbenchContext.getErrorHandler().handleThrowable(t);
                    }
                }
            });
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    try {
                        int column = table.columnAtPoint(e.getPoint());
                        int row = table.rowAtPoint(e.getPoint());
                        if (isEditButtonColumn(column)) {
                            PlugInContext context =
                                new PlugInContext(
                                    workbenchContext,
                                    null,
                                    model.getLayer(),
                                    null,
                                    null);
                            editFeaturePlugIn.initialize(context);
                            model
                                .getLayer()
                                .getLayerManager()
                                .getUndoableEditReceiver()
                                .startReceiving();
                            try {
                                editFeaturePlugIn.execute(
                                    context,
                                    model.getFeature(row),
                                    model.getLayer().isEditable());
                            } finally {
                                model
                                    .getLayer()
                                    .getLayerManager()
                                    .getUndoableEditReceiver()
                                    .stopReceiving();
                            }
                            return;
                        }
                    } catch (Throwable t) {
                        workbenchContext.getErrorHandler().handleThrowable(t);
                    }
                }
            });
        } catch (Throwable t) {
            workbenchContext.getErrorHandler().handleThrowable(t);
        }
    }
    private AttributeTablePanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void updateGrid(Layer layer) {
        table.setShowGrid(layer.isEditable());
    }
    private boolean isEditButtonColumn(int column) {
        return getModel().getColumnName(0).equals(table.getColumnName(column));
    }
    private void updateLabel() {
        layerNameRenderer.getLabel().setText(
            getModel().getLayer().getName()
                + " ("
                + getModel().getRowCount()
                + " feature"
                + ((getModel().getRowCount() == 1) ? "" : "s")
                + ")");
    }
    public LayerTableModel getModel() {
        return (LayerTableModel) table.getModel();
    }
    public JTable getTable() {
        return table;
    }
    public void addListener(AttributeTablePanelListener listener) {
        listeners.add(listener);
    }
    void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        this.add(
            layerNameRenderer,
            new GridBagConstraints(
                0,
                0,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0));

        this.add(
            table.getTableHeader(),
            new GridBagConstraints(
                0,
                1,
                1,
                1,
                0,
                0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0));
        //Pad table on the right with 200 pixels so that user has some space
        //in which to resize last column. [Jon Aquino]                
        this.add(
            table,
            new GridBagConstraints(
                0,
                2,
                1,
                1,
                0,
                0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 200),
                0,
                0));
    }
    private void initColumnWidths() {
        GUIUtil.chooseGoodColumnWidths(table);
        int editButtonWidth = 15;
        table.getColumnModel().getColumn(0).setMinWidth(editButtonWidth);
        table.getColumnModel().getColumn(0).setMaxWidth(editButtonWidth);
        table.getColumnModel().getColumn(0).setPreferredWidth(editButtonWidth);
        columnWidthsInitialized = true;
    }
    private void setToolTips() {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (column == -1) {
                    return;
                }
                table.setToolTipText(
                    table.getColumnName(column)
                        + " ["
                        + getModel().getLayer().getName()
                        + "]");
            }
        });
    }
    /**
     *  Called when the user creates a new selection, rather than adding to the
     *  existing selection
     */
    private void fireSelectionReplaced() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            AttributeTablePanelListener listener = (AttributeTablePanelListener) i.next();
            listener.selectionReplaced(this);
        }
    }
    private static class SelectionModelWrapper implements ListSelectionModel {
        private AttributeTablePanel panel;
        private ListSelectionModel selectionModel;
        public SelectionModelWrapper(AttributeTablePanel panel) {
            this.panel = panel;
            selectionModel = panel.table.getSelectionModel();
        }
        public void setAnchorSelectionIndex(int index) {
            selectionModel.setAnchorSelectionIndex(index);
        }
        public void setLeadSelectionIndex(int index) {
            selectionModel.setLeadSelectionIndex(index);
        }
        public void setSelectionInterval(int index0, int index1) {
            selectionModel.setSelectionInterval(index0, index1);
            panel.fireSelectionReplaced();
        }
        public void setSelectionMode(int selectionMode) {
            selectionModel.setSelectionMode(selectionMode);
        }
        public void setValueIsAdjusting(boolean valueIsAdjusting) {
            selectionModel.setValueIsAdjusting(valueIsAdjusting);
        }
        public int getAnchorSelectionIndex() {
            return selectionModel.getAnchorSelectionIndex();
        }
        public int getLeadSelectionIndex() {
            return selectionModel.getLeadSelectionIndex();
        }
        public int getMaxSelectionIndex() {
            return selectionModel.getMaxSelectionIndex();
        }
        public int getMinSelectionIndex() {
            return selectionModel.getMinSelectionIndex();
        }
        public int getSelectionMode() {
            return selectionModel.getSelectionMode();
        }
        public boolean getValueIsAdjusting() {
            return selectionModel.getValueIsAdjusting();
        }
        public boolean isSelectedIndex(int index) {
            return selectionModel.isSelectedIndex(index);
        }
        public boolean isSelectionEmpty() {
            return selectionModel.isSelectionEmpty();
        }
        public void addListSelectionListener(ListSelectionListener x) {
            selectionModel.addListSelectionListener(x);
        }
        public void addSelectionInterval(int index0, int index1) {
            selectionModel.addSelectionInterval(index0, index1);
        }
        public void clearSelection() {
            selectionModel.clearSelection();
        }
        public void insertIndexInterval(int index, int length, boolean before) {
            selectionModel.insertIndexInterval(index, length, before);
        }
        public void removeIndexInterval(int index0, int index1) {
            selectionModel.removeIndexInterval(index0, index1);
        }
        public void removeListSelectionListener(ListSelectionListener x) {
            selectionModel.removeListSelectionListener(x);
        }
        public void removeSelectionInterval(int index0, int index1) {
            selectionModel.removeSelectionInterval(index0, index1);
        }
    }
    public Collection getSelectedFeatures() {
        ArrayList selectedFeatures = new ArrayList();
        int[] selectedRows = table.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            selectedFeatures.add(getModel().getFeature(selectedRows[i]));
        }
        return selectedFeatures;
    }
    public LayerNameRenderer getLayerNameRenderer() {
        return layerNameRenderer;
    }
    
    // migueldedios
	private void setCellEditors(final Layer layer) {
		GeopistaSchema MySchema;

		if (layer.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema) {
			MySchema = (GeopistaSchema) layer.getFeatureCollectionWrapper()
					.getFeatureSchema();
			MySchema.setGeopistalayer((IGeopistaLayer) layer);
			Domain domain;
			// Atributo de la tabla
			String attName;
			int domainType;

			for (int i = 0; i < MySchema.getAttributeCount(); i++) {
				if (MySchema instanceof GeopistaSchema
						&& MySchema.getColumnByAttribute(i).getDomain() != null) {
					domain = MySchema.getColumnByAttribute(i).getDomain();
					domainType = domain.getType();

					if (domainType == Domain.CODEDENTRY
							|| domainType == Domain.CODEBOOK
							|| domainType == Domain.TREE) {
						attName = MySchema.getAttribute(MySchema
								.getColumnByAttribute(i));
						int index = MySchema.getAttributeIndex(attName);

						try {
							JComboEditorRenderTableDomain EditorCombo = new JComboEditorRenderTableDomain(
									attName, (TreeDomain) domain, MySchema);

							ComboEditor elEditor = new ComboEditor(EditorCombo
									.getCombo(), EditorCombo.getValues());
							ComboRender elRender = new ComboRender(EditorCombo
									.getCombo(), EditorCombo.getValues());

							table.getColumnModel().getColumn(index + 1)
									.setCellEditor(elEditor);
							table.getColumnModel().getColumn(index + 1)
									.setCellRenderer(elRender);
							table.repaint();

						} catch (Exception ex) {
							if (MySchema.getGeopistalayer().getFeatureCollectionWrapper().getFeatures().size()!=0)
								ex.printStackTrace();
						}

					}
				}
			}
		}
	}
    
}
