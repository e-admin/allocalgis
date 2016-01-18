/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * 
 * www.geopista.com
 * 
 * Created on 31-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.legend.LegendPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.OneLayerAttributeTab;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ShowLegendPlugIn extends AbstractPlugIn
{
	public String getName() {
		return "View Legend";
	}
//	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private String toolBarCategory = "showLegendPlugIn.category";

	public void initialize(PlugInContext context) throws Exception
	{
		I18N.setPlugInRessource(this.getName(),"GeoPistai18n");	
		I18N.setPlugInRessource(this.getName()+"config","GeoPista");	
		String pluginCategory = I18N.get(this.getName()+"config",toolBarCategory);
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
		.addPlugIn(this.getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());
	}

	public boolean execute(final PlugInContext context)
	throws Exception {
		reportNothingToUndoYet(context);

		//Don't add GeometryInfoFrame because the HTML will probably be too much for the
		//editor pane (too many features). [Jon Aquino]
		final ViewLegendFrame frame = new ViewLegendFrame(context);
		// REVISAR: Cast to WorkbenchFrame for the MDI model
		((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame( frame);
		frame.position();
		return true;
	}

	public MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck().add(checkFactory.createTaskWindowMustBeActiveCheck());
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("leyenda.gif");
	}

	private class ViewLegendFrame extends JInternalFrame
	implements LayerManagerProxy, SelectionManagerProxy,
	LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long	serialVersionUID	= 3257291344102635570L;
		private LayerManager layerManager;
		private OneLayerAttributeTab attributeTab;
		private LegendPanel lgnpan;
		private JScrollPane scrollpane;
		PlugInContext context;
		protected int symbolHeight;
		protected int symbolWidth;

		ViewLegendFrame(final PlugInContext context) {
			this.layerManager = context.getLayerManager();
			this.context=context;
//			addInternalFrameListener(new InternalFrameAdapter() {
//			public void internalFrameClosed(InternalFrameEvent e) {
//			//
//			ViewLegendFrame.this.dispose();
//			}
//			});
			jbInit();


			context.getLayerManager().addLayerListener(new LayerListener() {
				public void layerChanged(LayerEvent e) {
					updateTitle();


					lgnpan.reset();
					lgnpan.setViewport(context.getLayerViewPanel().getViewport());
					lgnpan.initializeFromLayers(context.getLayerManager().getLayerables(GeopistaLayer.class));

				}

				public void categoryChanged(CategoryEvent e) {
				}

				public void featuresChanged(FeatureEvent e) {
				}
			});
			if(context.getTask().getTaskComponent()!=null){
				context.getTask().getTaskComponent().addInternalFrameListener(
						new InternalFrameAdapter()
						{
							public void internalFrameClosed(InternalFrameEvent e)
							{
								//
								ViewLegendFrame.this.dispose();
							}
						});
			}
		}
		private void jbInit()
		{
			setResizable(true);
			setClosable(true);
			setMaximizable(true);
			setIconifiable(true);
			getContentPane().setLayout(new BorderLayout());
			symbolHeight=20;
			symbolWidth=30;
			lgnpan = new LegendPanel(symbolWidth,symbolHeight);
			lgnpan.setRuleFontDivisor(2.5f);
			lgnpan.setLayerFontDivisor(1.5f);
			lgnpan.setEditingMode(false);
			
			lgnpan.setViewport(context.getLayerViewPanel().getViewport());
			
			scrollpane=new JScrollPane();
			lgnpan.initializeFromLayers(context.getLayerManager().getLayerables(GeopistaLayer.class));

			updateTitle();

			scrollpane.getViewport().add(lgnpan);
			getContentPane().add(scrollpane,BorderLayout.CENTER);
			JPanel botones=new JPanel();
			JButton butMas=new JButton(IconLoader.icon("Up.gif"));
			JButton butMenos=new JButton(IconLoader.icon("Down.gif"));
			butMas.addActionListener(new ActionListener(){/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
				public void actionPerformed(ActionEvent e)
				{
					symbolHeight=(int) (symbolHeight*1.2);
					symbolWidth=(int) (symbolWidth*1.2);
					lgnpan.setSymbolSize(symbolWidth,symbolHeight);

				}

			});
			butMenos.addActionListener(new ActionListener(){/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
				public void actionPerformed(ActionEvent e)
				{
					symbolHeight=(int) (symbolHeight*0.8);
					symbolWidth=(int) (symbolWidth*0.8);
					lgnpan.setSymbolSize(symbolWidth,symbolHeight);

				}

			});
			botones.resize(botones.getWidth()*2, botones.getHeight()*2);

			butMas.resize(butMas.getWidth()*2, butMas.getHeight()*2);
			botones.add(butMas);
			botones.add(butMenos);
			getContentPane().add(botones,BorderLayout.NORTH);
			getContentPane().repaint();
			setSize(300, 450);
		}
		public void position()
		{
			Rectangle rect=null;
			if (context.getTask().getTaskComponent() instanceof JInternalFrame)
			{
				rect = ((JInternalFrame)context.getTask().getTaskComponent()).getBounds();
				int x=(int) (context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-rect.getMaxX());
				if (x < 200)
					this.setLocation(context.getWorkbenchGuiComponent().getDesktopPane().getWidth()-getWidth(),0);
				else
					this.setLocation((int) rect.getMaxX(),0);
			}
		}
		public LayerViewPanel getLayerViewPanel() {
			return (LayerViewPanel)getTaskComponent().getLayerViewPanel();
		}

		public LayerManager getLayerManager() {
			return layerManager;
		}

		private void updateTitle() {
			this.setTitle(I18N.get(ShowLegendPlugIn.this.getName(),"ShowLegend.Legend")+"("+context.getTask().getName()+")");
		}

		public TaskComponent getTaskComponent() {
			return context.getTask().getTaskComponent();
		}

		public SelectionManager getSelectionManager() {
			return attributeTab.getPanel().getSelectionManager();
		}

		public LayerNamePanel getLayerNamePanel() {
			return attributeTab;
		}


	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.model.LayerManagerProxy#getLayerManager()
	 */
	public LayerManager getLayerManager()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.SelectionManagerProxy#getSelectionManager()
	 */
	public SelectionManager getSelectionManager()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy#getLayerNamePanel()
	 */
	public LayerNamePanel getLayerNamePanel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.TaskFrameProxy#getTaskFrame()
	 */
	public TaskComponent getTaskFrame()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy#getLayerViewPanel()
	 */
	public LayerViewPanel getLayerViewPanel()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
