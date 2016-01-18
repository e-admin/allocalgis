package com.geopista.ui;

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.LangUtil;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.CategoryEventType;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.FirableTreeModelWrapper;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerTreeCellEditor;
import com.vividsolutions.jump.workbench.ui.PopupNodeProxy;
import com.vividsolutions.jump.workbench.ui.TreeUtil;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;

public class GeopistaLayerNamePanel extends JPanel implements LayerListener,
    LayerNamePanel, LayerNamePanelProxy, PopupNodeProxy {
    private Map nodeClassToPopupMenuMap = new HashMap();
    private Map nodeInstanceToPopupMenuMap = new HashMap();
    BorderLayout borderLayout1 = new BorderLayout();
    JTree tree = new JTree() {
            public boolean isPathEditable(TreePath path) {
                if (!isEditable()) {
                    return false;
                }

                return path.getLastPathComponent() instanceof Layerable ||
                path.getLastPathComponent() instanceof Category;
            }

            //Workaround for Java Bug 4199956 "JTree shows container can be expanded -
            //even when empty", posted by bertrand.allo in the Java Bug Database. [Jon Aquino]
            public boolean hasBeenExpanded(TreePath path) {
                return super.hasBeenExpanded(path) ||
                !this.getModel().isLeaf(path.getLastPathComponent());
            }
        };

    private GeopistaLayerTreeCellRenderer layerTreeCellRenderer;
    private TreeCellEditor cellEditor = new LayerTreeCellEditor(tree);
    private Object popupNode;
    private ArrayList listeners = new ArrayList();
    private LayerManagerProxy layerManagerProxy;
    JScrollPane scrollPane = new JScrollPane();
    private FirableTreeModelWrapper firableTreeModelWrapper;

    /**
     * @param layerNamePopupMenu null for no popup menu on layer nodes
     * @param categoryPopupMenu null for no popup menu on category nodes
     */
    public GeopistaLayerNamePanel(LayerManagerProxy layerManagerProxy,
        TreeModel treeModel, RenderingManager renderingManager,
        Map additionalNodeClassToTreeCellRendererMap) {
        //layerManagerProxy.getLayerManager().addLayerListener(this);
        this.layerManagerProxy = layerManagerProxy;

        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        firableTreeModelWrapper = new FirableTreeModelWrapper(treeModel);
        tree.setModel(firableTreeModelWrapper);
        layerTreeCellRenderer = new GeopistaLayerTreeCellRenderer(renderingManager);
        setCellRenderer(additionalNodeClassToTreeCellRendererMap);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        /*tree.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    handleCheckBoxClick(e);
                }
            });*/
        //tree.setCellEditor(cellEditor);
        //tree.setInvokesStopCellEditing(true);
        tree.setBackground(Color.WHITE);  // setBackground(getBackground());
        /*tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    fireLayerSelectionChanged();
                }
            });
        tree.getModel().addTreeModelListener(new TreeModelListener() {
                public void treeNodesChanged(TreeModelEvent e) {
                }

                public void treeNodesInserted(TreeModelEvent e) {
                    for (int i = 0; i < e.getChildren().length; i++) {
                        TreeUtil.visit(tree.getModel(),
                            e.getTreePath().pathByAddingChild(e.getChildren()[i]),
                            new TreeUtil.Visitor() {
                                public void visit(Stack path) {
                                    tree.makeVisible(new TreePath(
                                            path.toArray()));
                                }
                            });
                    }
                }

                public void treeNodesRemoved(TreeModelEvent e) {
                }

                public void treeStructureChanged(TreeModelEvent e) {
                }
            });*/
        TreeUtil.expandAll(tree, new TreePath(tree.getModel().getRoot()));
    }

    public void addPopupMenu(Class nodeClass, JPopupMenu popupMenu) {
        nodeClassToPopupMenuMap.put(nodeClass, popupMenu);
    }

    public void addPopupMenuContext(int idCapa, JPopupMenu popupMenu) {
        Integer idCapaInteger = new Integer(idCapa);
        nodeInstanceToPopupMenuMap.put(idCapaInteger, popupMenu);
    }

    

    private void setCellRenderer(Map additionalNodeClassToTreeCellRendererMap) {
        final Map map = createNodeClassToTreeCellRendererMap();
        map.putAll(additionalNodeClassToTreeCellRendererMap);
        tree.setCellRenderer(new TreeCellRenderer() {
                private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer() {

                        {
                            //Transparent. [Jon Aquino]
                            setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
                        }
                    };

                public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                    return ((TreeCellRenderer) LangUtil.ifNull(CollectionUtil.get(
                            value.getClass(), map), defaultRenderer)).getTreeCellRendererComponent(tree,
                        value, selected, expanded, leaf, row, hasFocus);
                }
            });
    }

    private Map createNodeClassToTreeCellRendererMap() {
        HashMap map = new HashMap();
        map.put(Layer.class, layerTreeCellRenderer);
        map.put(WMSLayer.class, layerTreeCellRenderer);
        map.put(Category.class, layerTreeCellRenderer);
        map.put(Map.Entry.class, layerTreeCellRenderer);

        return map;
    }

    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    tree_mouseReleased(e);
                    //tree_mouseReleasedInstance(e);
                }
            });
        ToolTipManager.sharedInstance().registerComponent(tree);
        //tree.setEditable(true);
        tree.setRootVisible(false);

        //Row height is set to -1 because otherwise, in Java 1.4, tree nodes will be
        //"chopped off" at the bottom [Jon Aquino]        
        tree.setRowHeight(-1);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tree.setShowsRootHandles(true);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.getViewport().add(tree);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    void tree_mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        TreePath popupPath = tree.getPathForLocation(e.getX(), e.getY());

        if (popupPath == null) {
            return;
        }

        popupNode = popupPath.getLastPathComponent();

        //#isAltDown returns true on a middle-click; #isMetaDown returns true on
        //a right-click[Jon Aquino]
        //Third check can't simply user JTree#isPathSelected because the node wrappers are value
        //objects and thus can't reliably be compared by reference (which is what #isPathSelected
        //seems to do). [Jon Aquino]
        if (!(e.isControlDown() || e.isShiftDown() ||
                selectedNodes(Object.class).contains(popupNode))) {
            tree.getSelectionModel().clearSelection();
        }

        tree.getSelectionModel().addSelectionPath(popupPath);

        if (getPopupMenu(popupNode.getClass()) != null) {
            getPopupMenu(popupNode.getClass()).show(e.getComponent(), e.getX(),
                e.getY());
        }
    }

    void tree_mouseReleasedInstance(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        TreePath popupPath = tree.getPathForLocation(e.getX(), e.getY());

        if (popupPath == null) {
            return;
        }

        popupNode = popupPath.getLastPathComponent();

        //#isAltDown returns true on a middle-click; #isMetaDown returns true on
        //a right-click[Jon Aquino]
        //Third check can't simply user JTree#isPathSelected because the node wrappers are value
        //objects and thus can't reliably be compared by reference (which is what #isPathSelected
        //seems to do). [Jon Aquino]
        if (!(e.isControlDown() || e.isShiftDown() ||
                selectedNodes(Object.class).contains(popupNode))) {
            tree.getSelectionModel().clearSelection();
        }

        tree.getSelectionModel().addSelectionPath(popupPath);
        if (getPopupMenuInstance(popupNode.hashCode()) != null) {
            getPopupMenuInstance(popupNode.hashCode()).show(e.getComponent(), e.getX(),
                e.getY());
        }
    }

    private JPopupMenu getPopupMenu(Class nodeClass) {
        return (JPopupMenu) CollectionUtil.get(nodeClass,
            nodeClassToPopupMenuMap);
    }

    private JPopupMenu getPopupMenuInstance(int idCode) {
        Integer idCodeInteger = new Integer(idCode);
        return (JPopupMenu) nodeInstanceToPopupMenuMap.get(idCodeInteger);
    }

    private void handleCheckBoxClick(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        TreePath path = tree.getPathForLocation(e.getX(), e.getY());

        if (path == null) {
            return;
        }

        Object node = path.getLastPathComponent();

        if (!(node instanceof Layerable)) {
            return;
        }

        Layerable layerable = (Layerable) node;
        Point layerNodeLocation = tree.getUI().getPathBounds(tree, path)
                                      .getLocation();

        //Initialize the LayerNameRenderer with the current node. checkBoxBounds
        //will be different for Layers and WMSLayers. [Jon Aquino]
        layerTreeCellRenderer.getLayerNameRenderer().getTreeCellRendererComponent(tree,
            path.getLastPathComponent(), false, false, false, 0, false);

        Rectangle checkBoxBounds = layerTreeCellRenderer.getLayerNameRenderer()
                                               .getCheckBoxBounds();
        checkBoxBounds.translate((int) layerNodeLocation.getX(),
            (int) layerNodeLocation.getY());

        if (checkBoxBounds.contains(e.getPoint())) {
            layerable.setVisible(!layerable.isVisible());
        }
    }
    public Layerable[] getSelectedLayerables() {
        return selectedLayerables(this);
    }
    public Layer[] getSelectedLayers() {
        return selectedLayers(this);
    }

    public static Layer[] selectedLayers(LayerNamePanel layerNamePanel) {
        return (Layer[]) layerNamePanel.selectedNodes(Layer.class).toArray(new Layer[] {
                
            });
    }
    public static Layerable[] selectedLayerables(LayerNamePanel layerNamePanel) {
        return (Layerable[]) layerNamePanel.selectedNodes(Layerable.class).toArray(new Layerable[] {
                
            });
    }
    public Collection getSelectedCategories() {
        return selectedNodes(Category.class);
    }

    public Collection selectedNodes(Class c) {
        return selectedNodes(c, tree);
    }

    public static Collection selectedNodes(Class c, JTree tree) {
        ArrayList selectedNodes = new ArrayList();
        TreePath[] selectionPaths = tree.getSelectionPaths();

        if (selectionPaths == null) {
            return new ArrayList();
        }

        for (int i = 0; i < selectionPaths.length; i++) {
            Object node = selectionPaths[i].getLastPathComponent();

            if (c.isInstance(node)) {
                selectedNodes.add(node);
            }
        }

        return selectedNodes;
    }

    private void setSelectedLayers(Layerable[] layers) {
        tree.getSelectionModel().clearSelection();

        for (int i = 0; i < layers.length; i++) {
            addSelectedLayer(layers[i]);
        }
    }

    protected void addSelectedLayer(Layerable layer) {
        tree.addSelectionPath(TreeUtil.findTreePath(layer, tree.getModel()));
    }

    public void layerChanged(LayerEvent e) {
        TreeModelEvent treeModelEvent = new TreeModelEvent(this,
                new Object[] { tree.getModel().getRoot(), e.getCategory() },
                new int[] { e.getLayerableIndex() },
                new Object[] { e.getLayerable() });

        if (e.getType() == LayerEventType.ADDED) {
            firableTreeModelWrapper.fireTreeNodesInserted(treeModelEvent);

            //firableTreeModelWrapper.fireTreeStructureChanged(treeModelEvent);
            if ((e.getType() == LayerEventType.ADDED) &&
                    (getSelectedLayers().length == 0) &&
                    e.getLayerable() instanceof Layer) {
                addSelectedLayer((Layer) e.getLayerable());
            }

            return;
        }

        if (e.getType() == LayerEventType.REMOVED) {
            firableTreeModelWrapper.fireTreeNodesRemoved(treeModelEvent);

            return;
        }

        if (e.getType() == LayerEventType.APPEARANCE_CHANGED) {
            firableTreeModelWrapper.fireTreeNodesChanged(treeModelEvent);

            return;
        }

        if (e.getType() == LayerEventType.METADATA_CHANGED) {
            firableTreeModelWrapper.fireTreeNodesChanged(treeModelEvent);

            return;
        }

        if (e.getType() == LayerEventType.VISIBILITY_CHANGED) {
            firableTreeModelWrapper.fireTreeNodesChanged(treeModelEvent);

            return;
        }

        Assert.shouldNeverReachHere();
    }

    public void categoryChanged(CategoryEvent e) {
        TreeModelEvent treeModelEvent = new TreeModelEvent(this,
                new Object[] { tree.getModel().getRoot() },
                new int[] { e.getCategoryIndex() +
                    indexOfFirstCategoryInTree() },
                new Object[] { e.getCategory() });

        if (e.getType() == CategoryEventType.ADDED) {
            firableTreeModelWrapper.fireTreeNodesInserted(treeModelEvent);

            return;
        }

        if (e.getType() == CategoryEventType.REMOVED) {
            firableTreeModelWrapper.fireTreeNodesRemoved(treeModelEvent);

            return;
        }

        if (e.getType() == CategoryEventType.METADATA_CHANGED) {
            firableTreeModelWrapper.fireTreeNodesChanged(treeModelEvent);

            return;
        }

        Assert.shouldNeverReachHere();
    }

    private int indexOfFirstCategoryInTree() {
        //Not 0 in ESE. [Jon Aquino]
        for (int i = 0;
                i < tree.getModel().getChildCount(tree.getModel().getRoot());
                i++) {
            if (tree.getModel().getChild(tree.getModel().getRoot(), i) instanceof Category) {
                return i;
            }
        }

        Assert.shouldNeverReachHere();

        return -1;
    }

    public void featuresChanged(FeatureEvent e) {
    }

    public void dispose() {
        //Layer events could still be fired after the TaskWindow containing this
        //LayerNamePanel is closed (e.g. by clones of the TaskWindow, or
        //by an attribute viewer). [Jon Aquino]
        layerManagerProxy.getLayerManager().removeLayerListener(this);
    }

    public JTree getTree() {
        return tree;
    }

    public void addListener(LayerNamePanelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LayerNamePanelListener listener) {
        listeners.remove(listener);
    }

    public void fireLayerSelectionChanged() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            LayerNamePanelListener l = (LayerNamePanelListener) i.next();
            l.layerSelectionChanged();
        }
    }

    public ILayerManager getLayerManager() {
        return layerManagerProxy.getLayerManager();
    }

    public static Layer chooseEditableLayer(LayerNamePanel panel) {
        for (Iterator i = Arrays.asList(panel.getSelectedLayers()).iterator();
                i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (layer.isEditable()) {
                return layer;
            }
        }

        if (panel.getLayerManager().getEditableLayers().isEmpty()) {
            return null;
        }

        return (Layer) panel.getLayerManager().getEditableLayers().iterator()
                            .next();
    }

    public Layer chooseEditableLayer() {
        return chooseEditableLayer(this);
    }

    public LayerNamePanel getLayerNamePanel() {
        return this;
    }

    protected FirableTreeModelWrapper getFirableTreeModelWrapper() {
        return firableTreeModelWrapper;
    }

    public Object getPopupNode() {
        return popupNode;
    }
	protected GeopistaLayerTreeCellRenderer getLayerTreeCellRenderer() {
		return layerTreeCellRenderer;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.LayerNamePanel#selectLayers(com.vividsolutions.jump.workbench.model.Layer[])
	 */
	public void selectLayerables(Layerable[] layers)
	{
		
		setSelectedLayers(layers);
	}

	@Override
	public void setTargetSelectedLayers(Layerable[] layers) {
		// TODO Auto-generated method stub
		
	}

}
