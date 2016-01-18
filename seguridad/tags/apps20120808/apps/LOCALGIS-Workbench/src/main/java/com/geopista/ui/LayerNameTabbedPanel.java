package com.geopista.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;

public class LayerNameTabbedPanel extends JPanel implements LayerNamePanel, LayerListener, LayerNamePanelListener
{
    protected final class FireableDefaultListModel extends DefaultListModel
	{
		public void fireContentsChanged(Object source,
		        int index0,
		        int index1)
		{
			super.fireContentsChanged(source,index0,index1);
		}
	}

	public GeopistaTreeLayerNamePanel treeLayerNamePanel = null;
    private JDnDList lista = null;
    private DnDListModel listModel = null;
	private GeopistaLayerNameRenderer renderer;
	private void jbInit()
	{

		listModel = new DnDListModel();

		lista = new JDnDList(listModel,this);
		renderer = ((GeopistaLayerTreeCellRenderer)this.treeLayerNamePanel.getLayerTreeCellRenderer()).getLayerNameRenderer();//GeopistaLayerNameRenderer();
		renderer.setCheckBoxVisible(true); 
		lista.setCellRenderer(renderer);
		lista.addListSelectionListener(new ListSelectionListener()
		{

			public void valueChanged(ListSelectionEvent e)
			{
				if (e.getValueIsAdjusting())return;
				if (ignoreEvents) 
				{

					return; //el flag indica que esta provocado por una actualización automática
				}
				// selecciona en el layernamepanel
				Object[] obj=((JList)e.getSource()).getSelectedValues();
				Layerable[] objLyr=new Layerable[obj.length];
				for (int i = 0; i < obj.length; i++)
				{
					objLyr[i]=(Layerable) obj[i];
				}
				treeLayerNamePanel.selectLayerables(objLyr);
			}
		});


		lista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				handleCheckBoxClick(e);
			}
			public void mouseReleased(MouseEvent e)
			{
				tree_mouseReleased(e);
			}
		});
		JScrollPane scrllLayerLegend = new JScrollPane();
		this.setLayout(new BorderLayout());
		lista.setVisibleRowCount(10);
		scrllLayerLegend.getViewport().add(lista);

		scrllLayerLegend.getVerticalScrollBar().setUnitIncrement(20);
		scrllLayerLegend.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrllLayerLegend.setViewportView(lista);
		scrllLayerLegend.setBorder(BorderFactory.createEtchedBorder());
		this.setSize(147, 200);

		lista.setBackground(java.awt.SystemColor.control);
		JTabbedPane tabs=new JTabbedPane();
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		tabs.addTab(AppContext.getApplicationContext().getI18nString("LayerNameTabbedPanel.LayersTab"),scrllLayerLegend); //$NON-NLS-1$
		tabs.addTab(AppContext.getApplicationContext().getI18nString("LayerNameTabbedPanel.ExplorerTab"),treeLayerNamePanel); //$NON-NLS-1$
		tabs.setSelectedIndex(0);
		tabs.setTabPlacement(JTabbedPane.BOTTOM);
		add(tabs);
		
	}

    public LayerNameTabbedPanel(GeopistaTreeLayerNamePanel treeLayerNamePanel)
    {
    	
      this.treeLayerNamePanel = treeLayerNamePanel;
      treeLayerNamePanel.getLayerManager().addLayerListener(this);
      treeLayerNamePanel.addListener(this);
      jbInit();
    }
   

    public Collection getSelectedCategories()
    {
      return treeLayerNamePanel.getSelectedCategories();
    }
  
    public Collection selectedNodes(Class c)
    {
      return treeLayerNamePanel.selectedNodes(c);
    }
    public Layer[] getSelectedLayers()
    {
      return treeLayerNamePanel.getSelectedLayers();
    }
    public Layerable[] getSelectedLayerables()
    {
      return treeLayerNamePanel.getSelectedLayerables();
    }
    
    private Object popupNode;
    void tree_mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            return;
        }
        int position=lista.locationToIndex(e.getPoint());
        Layerable layerable=null;
        if (position>-1)
        	layerable =(Layerable) lista.getModel().getElementAt(position);
	    if (layerable == null || 
	    		!lista.getCellBounds(position,position).contains(e.getPoint())) {
	    	popupNode= getLayerManager().getCategories().get(0);
	    }
	    else
        popupNode = layerable;
        if (!(e.isControlDown() || e.isShiftDown() ||
                selectedNodes(Object.class).contains(popupNode))) {
            lista.getSelectionModel().clearSelection();
           
        }

        lista.getSelectionModel().addSelectionInterval(position,position);
        Object[] selObj= lista.getSelectedValues();
       
       if (selObj.length>0)
       	{
       	Layerable[] selected=new Layerable[selObj.length];
       	for (int i = 0; i < selObj.length; i++)
		{
		selected[i] = (Layerable) selObj[i];
		}
        
       	
        treeLayerNamePanel.selectLayerables(selected);
       }
       

        if (treeLayerNamePanel.getPopupMenu(popupNode.getClass()) != null) {
            treeLayerNamePanel.getPopupMenu(popupNode.getClass()).show(e.getComponent(), e.getX(),
                e.getY());
        }
    }

    public Layer chooseEditableLayer()
    {
      return treeLayerNamePanel.chooseEditableLayer();
    }
    
    public void addListener(LayerNamePanelListener listener)
    {
      treeLayerNamePanel.addListener(listener);
    }
    public void removeListener(LayerNamePanelListener listener)
    {
      treeLayerNamePanel.removeListener(listener);
    }
    /**
     * The parent window is closing.
     */
    public void dispose()
    {
      treeLayerNamePanel.dispose();
    }

    public ILayerManager getLayerManager()
    {
      return treeLayerNamePanel.getLayerManager();
    }

      public void layerChanged(LayerEvent e) {
       

        if (e.getType() == LayerEventType.ADDED) {
        
        int layerPosition = e.getCategory().getLayerManager().getLayerables(Layerable.class).indexOf(e.getLayerable());
            
            listModel.add(layerPosition,e.getLayerable());
            
            return;
        }

        if (e.getType() == LayerEventType.REMOVED) {
          listModel.removeElement(e.getLayerable());
          
          return;
        }

        if (e.getType() == LayerEventType.APPEARANCE_CHANGED) {
        	listModel.fireContentsChanged(lista,0,lista.getModel().getSize());
            return;
        }

        if (e.getType() == LayerEventType.METADATA_CHANGED) {
        	int pos=listModel.indexOf(e.getLayerable());
        	listModel.fireContentsChanged(lista,pos,pos);
            return;
        }

        if (e.getType() == LayerEventType.VISIBILITY_CHANGED) {
        	listModel.fireContentsChanged(lista,0,lista.getModel().getSize());
            return;
        }

        Assert.shouldNeverReachHere();
    }

      public void categoryChanged(CategoryEvent e)
      {
      
      }

      public void featuresChanged(FeatureEvent e) {
      }

	private void handleCheckBoxClick(MouseEvent e) {
	    if (!SwingUtilities.isLeftMouseButton(e)) {
	        return;
	    }
	int position=lista.locationToIndex(e.getPoint());
	if (position ==-1)//No se ha pulsado en ningún elemento
	{
		lista.setSelectedIndex(-1);
		return;
	}
	    Layerable layerable =(Layerable) lista.getModel().getElementAt(position);
	    if (layerable == null) {
	        return;
	    }
	
	    Point layerNodeLocation= lista.getCellBounds(position,position).getLocation();
	    //Initialize the LayerNameRenderer with the current node. checkBoxBounds
	    //will be different for Layers and WMSLayers. [Jon Aquino]
	   renderer.getListCellRendererComponent(lista,layerable,position, false, false);
	
	   Rectangle checkBoxBounds=renderer.getCheckBoxBounds();
	    checkBoxBounds.translate((int) layerNodeLocation.getX(),
	        (int) layerNodeLocation.getY());
	
	    if (checkBoxBounds.contains(e.getPoint())) {
	        layerable.setVisible(!layerable.isVisible());
	    }
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.LayerNamePanel#selectLayers(com.vividsolutions.jump.workbench.model.Layer[])
	 */
	
	public void selectLayerables(Layerable[] layers)
	{
		if (layers.length==0)
			{
			lista.setSelectedIndex(-1);
			return;
			}
		int[] lastindices = lista.getSelectedIndices();
		int[] indices=new int[layers.length];
		
		for (int i = 0; i < layers.length; i++)
		{
		indices[i]=listModel.indexOf(layers[i]);
		}
		// Si son los mismos rompe la cadena de eventos.
		boolean iguales=false;
		if (lastindices!=null)
		{
			if (indices.length==lastindices.length)
			{
				iguales=true;
				for (int i = 0; i < indices.length; i++)
				{
					if (indices[i]!=lastindices[i])
						iguales=false;
				}
			}
		}
				
		if (iguales==false)
		{
		lista.setSelectedIndices(indices);
		}
		
		ignoreEvents=false;
		
	}

	/* Cambios en la selección del panel explorador
	 * 
	 */
	boolean ignoreEvents=false;
	private Layerable[] targetSelectedLayers=null;
	public void layerSelectionChanged()
	{
		/**
		 * cuidado con el bucle de eventos.
		 */
		
		ignoreEvents=true;
		if (targetSelectedLayers == null){
			targetSelectedLayers=treeLayerNamePanel.getSelectedLayerables();
		}
		selectLayerables(targetSelectedLayers);
		targetSelectedLayers=null;
		
		
	}
	
	public void setTargetSelectedLayers(Layerable[] layers){
		targetSelectedLayers = layers;
		this.treeLayerNamePanel.selectLayerables(layers);
	}
	
	
    
      
}  