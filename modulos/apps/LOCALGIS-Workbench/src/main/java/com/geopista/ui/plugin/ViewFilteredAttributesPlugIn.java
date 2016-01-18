/**
 * ViewFilteredAttributesPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.OneLayerFilteredAttributeTab;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.CloneableInternalFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class ViewFilteredAttributesPlugIn extends AbstractPlugIn {
	public ViewFilteredAttributesPlugIn() {
	}

	public String getName() {
		return "View / Edit Attributes";
	}
	
	HashMap attributes=new HashMap();

	public boolean execute(final PlugInContext context) throws Exception {
		reportNothingToUndoYet(context);

		//Don't add GeometryInfoFrame because the HTML will probably be too much for the
		//editor pane (too many features). [Jon Aquino]
		String capaSeleccionada=((Layer)context.getSelectedLayer(0)).getName();
		if (attributes.get(capaSeleccionada)!=null){
			ViewAttributesFrame frame2=(ViewAttributesFrame)attributes.get(capaSeleccionada);
			if (!frame2.isClosed()){
				 frame2.moveToFront();
                 frame2.setVisible(true);
                 frame2.toFront();
				return true;
			}
		}
		
		final ViewAttributesFrame frame = new ViewAttributesFrame(context);
		// REVISAR: Cast to WorkbenchFrame for the MDI model
		System.out.println("Capa Seleccionada:"+context.getSelectedLayer(0));
		((WorkbenchGuiComponent) context.getWorkbenchGuiComponent()).addInternalFrame(frame);	
		
		attributes.put(capaSeleccionada,frame);
		
		
		return true;
	}

	public MultiEnableCheck createEnableCheck(
	        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                     .add(new EnableCheck() {
                public String check(JComponent component) {
                    return ((workbenchContext.getLayerManager().getLayers().isEmpty())
                    ? "Already at start" : null);
                }
            });
//			final WorkbenchContext workbenchContext) {
//		EnableCheckFactory checkFactory = new EnableCheckFactory(
//				workbenchContext);
//
//		return new MultiEnableCheck()//.add(checkFactory.createTaskWindowMustBeActiveCheck())
//				.add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
//		
//					//	createExactlyNLayersMustBeSelectedCheck(1));
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("Row.gif");
	}

	private class ViewAttributesFrame extends JInternalFrame implements
			LayerManagerProxy, SelectionManagerProxy, LayerNamePanelProxy,
			TaskFrameProxy, LayerViewPanelProxy {
		private LayerManager layerManager;
		private OneLayerFilteredAttributeTab attributeTab;

		public ViewAttributesFrame(final PlugInContext context) {
			this.layerManager = context.getLayerManager();
			addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					//Assume that there are no other views on the model [Jon Aquino]
					attributeTab.getModel().dispose();
					String capaSeleccionada=((Layer)context.getSelectedLayer(0)).getName();
					attributes.remove(capaSeleccionada);
						
				}
			});
			setResizable(true);
			setClosable(true);
			setMaximizable(true);
			setIconifiable(true);
			getContentPane().setLayout(new BorderLayout());
			attributeTab = new OneLayerFilteredAttributeTab(context
					.getWorkbenchContext(),
			//(TaskComponent) context.getActiveInternalFrame(), this).setLayer(context.getSelectedLayer(
					(TaskComponent) context.getActiveTaskComponent(), this)
					.setLayer(context.getSelectedLayer(0));
			addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameOpened(InternalFrameEvent e) {
					attributeTab.getToolBar().updateEnabledState();
				}
			});
			getContentPane().add(attributeTab, BorderLayout.CENTER);
			setSize(500, 300);
			updateTitle(attributeTab.getLayer());
			context.getLayerManager().addLayerListener(new LayerListener() {
				public void layerChanged(LayerEvent e) {
					if (attributeTab.getLayer() != null) {
						updateTitle(attributeTab.getLayer());
					}
				}

				public void categoryChanged(CategoryEvent e) {
				}

				public void featuresChanged(FeatureEvent e) {
				}
			});
			Assert.isTrue(!(this instanceof CloneableInternalFrame),
					"There can be no other views on the InfoModel");
		}

		public LayerViewPanel getLayerViewPanel() {
			return (LayerViewPanel)getTaskComponent().getLayerViewPanel();
		}

		public LayerManager getLayerManager() {
			return layerManager;
		}

		private void updateTitle(Layer layer) {
			setTitle((layer.isEditable() ? "Edit" : "View") + " Attributes: "
					+ layer.getName());
		}

		public TaskComponent getTaskComponent() {
			return attributeTab.getTaskFrame();
		}

		public SelectionManager getSelectionManager() {
			return attributeTab.getPanel().getSelectionManager();
		}

		public LayerNamePanel getLayerNamePanel() {
			return attributeTab;
		}

	}

	public void initialize(PlugInContext context) throws Exception {

		JPopupMenu layerNamePopupMenu = context.getWorkbenchContext()
				.getIWorkbench().getGuiComponent().getLayerNamePopupMenu();

		FeatureInstaller featureInstaller = new FeatureInstaller(context
				.getWorkbenchContext());
		featureInstaller.addPopupMenuItem(layerNamePopupMenu, this, AppContext
				.getApplicationContext().getI18nString(this.getName()), false,
				null, this.createEnableCheck(context.getWorkbenchContext()));
		//null);

	}
}
