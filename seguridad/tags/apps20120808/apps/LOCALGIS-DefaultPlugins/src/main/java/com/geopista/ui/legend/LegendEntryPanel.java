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
 * Created on 28-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.legend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.deegree.graphics.sld.Rule;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public abstract class LegendEntryPanel extends JPanel
{
	private static final int	MIN_SYMBOL_HEIGTH	= 10;
	public void setSize(Dimension arg0)
	{
		//adjust symbol size
		//if (elasticMode)
		setSymbolHeigth(arg0.height);
		super.setSize(arg0);
	}
	boolean elasticMode=true;

	public LegendEntryPanel(int w,int h, boolean elasticMode)
	{
		this.elasticMode=elasticMode;
		setSymbolSize(w,h);
	}
	/**
	 * 
	 */
	public LegendEntryPanel() {

		// TODO Auto-generated constructor stub
	}
	class LegendEntryPanelTransferable implements Transferable
	{
		DataFlavor[] df= null;
		/**
		 * 
		 */
		public LegendEntryPanelTransferable() {

			try
			{
				df =new DataFlavor[]{
						new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType),
						DataFlavor.imageFlavor };
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public DataFlavor[] getTransferDataFlavors()
		{															
			return df;
		}
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return (flavor.equals(df[0]) || flavor.equals(df[1]));
		}
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if (flavor.equals(df[0]))
				return LegendEntryPanel.this;
			else
				//if (flavor.equals(df[1]))
			{// genera una imagen para otras aplicaciones
				LegendEntryPanel me = LegendEntryPanel.this;
				Image im=me.createImage(me.getWidth(),me.getHeight());

				if (im!=null)
				{
					Graphics g=im.getGraphics();
					g.setColor(me.getParent().getBackground());
					g.fillRect(0,0,me.getWidth(),me.getHeight());
					LegendEntryPanel.this.print(g);
				}
				return im;
			}
//			else
//			{// plain text
//			LegendEntryPanel me = LegendEntryPanel.this;
//			return me.getRuleDescriptionLabel().getText()+" "+ me.getFilterLabel().getText();
//			}
		}
	}


	protected SymbolizerPanel symbolPanel = null;
	private JPanel spacerPanel = null;
	protected JPanel textPanel;
	private JPanel leftPanel = null;
	private JCheckBox checkBox = null;
	private int symbolWidth=64;
	private int symbolHeight=32;
	private boolean editingMode=false;
	private boolean indentation=false;
	static final float	SYMBOLPANEL_RATIO	= 1.5f;
	private static final float	OVERALL_PREFERRED_RATIO	= 10;
	protected  float font_divisor=2f;
	public void setFont_divisor(float font_divisor)
	{
		this.font_divisor = font_divisor;
		resizeTextPanel();
	}
	/**
	 * This method initializes spacerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getSpacerPanel() {
		if (spacerPanel == null) {
			spacerPanel = new JPanel();
			spacerPanel.setOpaque(true);
			spacerPanel.setPreferredSize(new Dimension(getSymbolPanel().getWidth(),1));
			spacerPanel.setMinimumSize(new Dimension(getSymbolPanel().getHeight(),1));
			//spacerPanel.setVisible(indentation);
			spacerPanel.setBackground(Color.white);//BLACK);
		}
		return spacerPanel;
	}
	protected JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
			leftPanel.setOpaque(true);
			if (elasticMode==false)
				leftPanel.setMinimumSize(new Dimension(32,63));		
			leftPanel.add(getSpacerPanel());
			leftPanel.add(getCheckBox());
			leftPanel.add(getSymbolPanel());
			setSymbolSize(getSymbolWidth(),getSymbolHeight());
			leftPanel.setBackground(Color.white);
		}
		return leftPanel;
	}
	/**
	 * @return
	 */
	protected abstract SymbolizerPanel getSymbolPanel();
	/**
	 * @return
	 */
	protected JCheckBox getCheckBox()
	{
		if (checkBox==null)
		{
			checkBox=new JCheckBox();
			checkBox.setSelected(true);
			checkBox.setVisible(editingMode);
			checkBox.setOpaque(true);
			checkBox.setBackground(Color.white);
		}
		return checkBox;
	}

	/**
	 * This is the default constructor
	 */
	public LegendEntryPanel(Rule rule) {
		super();

	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	void initialize() {

		BorderLayout borderLayout= new BorderLayout();
		this.setLayout(borderLayout);
		this.setOpaque(true);
		//	this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaption,1));
		this.add(getLeftPanel(), BorderLayout.WEST);
		this.add(getTextPanel(), BorderLayout.CENTER);
//		if (elasticMode==false)
//		this.setPreferredSize(new Dimension(220,getSymbolHeight()));
		configureDnD();		

	}
	/**
	 * 
	 */
	private void configureDnD()
	{
		// configures Drag Source behaviour
		DragSource dragsource=DragSource.getDefaultDragSource();
		dragsource.createDefaultDragGestureRecognizer(this, 
				DnDConstants.ACTION_COPY,
				new DragGestureListener(){

			public void dragGestureRecognized(DragGestureEvent dge)
			{	
				Transferable trans=null;
				trans = new LegendEntryPanelTransferable();
				dge.startDrag(null, trans, new DragSourceAdapter(){});
			}

		});
		// Configura como destino de drop para insertar encima un componente

		DropTarget target = new DropTarget(this,
				new DropTargetAdapter(){
			public boolean idDragAcceptable(DropTargetDragEvent ev)
			{
				try
				{
					if (ev.isDataFlavorSupported(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType)))
					{
						//	if (ev.getDropTargetContext().getComponent() instanceof LegendEntryPanel) 
						if (editingMode){

							return true;
						}
					}
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			public boolean idDropAcceptable(DropTargetDropEvent ev)
			{
				try
				{
					if (ev.isDataFlavorSupported(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType)))
					{
						if (editingMode && ev.getDropTargetContext().getComponent() instanceof LegendEntryPanel) 
						{
							LegendEntryPanel lgnSource;

							try
							{
								lgnSource = (LegendEntryPanel) ev.getTransferable().getTransferData(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType));
								if (lgnSource!=LegendEntryPanel.this)
									return true;
							} catch (UnsupportedFlavorException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					}
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			public void dragEnter(DropTargetDragEvent ev)
			{
				if (!idDragAcceptable(ev))
				{
					ev.rejectDrag();
				}
			}
			public void drop(DropTargetDropEvent ev)
			{
				if (!idDropAcceptable(ev))
				{
					ev.rejectDrop();
					return;
				}
				try
				{
					ev.acceptDrop(DnDConstants.ACTION_COPY);

					// cambia la posición de los componentes

					LegendEntryPanel lgnSource;

					lgnSource = (LegendEntryPanel) ev.getTransferable().getTransferData(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType));


					getParent().remove(lgnSource);


					int myPosition=0;
					for (myPosition=0;myPosition<getParent().getComponentCount();myPosition++)
					{
						if (getParent().getComponent(myPosition)== LegendEntryPanel.this)
							break;
					}					

					getParent().add(lgnSource, myPosition);
					//((LegendLayout)getParent().getLayout()).configureConstraints(getParent());
					getParent().validate();
					ev.dropComplete(true);
					return;

				} catch (UnsupportedFlavorException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ev.rejectDrop();

			}});
		target.setDefaultActions(DnDConstants.ACTION_COPY);
	}
	/**
	 * @return
	 */
	protected abstract JPanel getTextPanel();
	/**
	 * @param checkBoxes The checkBoxes to set.
	 */
	public void setEditingMode(boolean checkBoxes)
	{
		this.editingMode = checkBoxes;
		getCheckBox().setVisible(checkBoxes);
		setVisible(checkBoxes || getCheckBox().isSelected());
	}
	/**
	 * @param indentation The indentation to set.
	 */
	public void setIndentation(boolean indentation)
	{
		this.indentation = indentation;
		getSpacerPanel().setVisible(indentation);
	}
	/**
	 * @return Returns the symbolHeigth.
	 */
	protected int getSymbolHeight()
	{
		return Math.max(symbolHeight,MIN_SYMBOL_HEIGTH);
	}
	/**
	 * @param symbolHeigth The symbolHeigth to set.
	 */
	public void setSymbolHeigth(int symbolHeigth)
	{
		setSymbolSize((int) (symbolHeigth*SYMBOLPANEL_RATIO),symbolHeigth);
	}
	/**
	 * @param i
	 * @param j
	 */
	public void setSymbolSize(int i, int j)
	{
		this.symbolHeight=j;
		this.symbolWidth=i;
		Dimension dim;
		if (elasticMode==false)
			dim=new Dimension(i,j);
		else
			dim=null;
		getSymbolPanel().setSize(i,j);
		getSymbolPanel().setMinimumSize(dim);
		getSymbolPanel().setPreferredSize(dim);
		resizeTextPanel();
		validate();

	}
	/**
	 * 
	 */
	protected abstract void resizeTextPanel();
	/**
	 * @return Returns the symbolWidth.
	 */
	protected int getSymbolWidth()
	{
		return symbolWidth;
	}
	/**
	 * @param symbolWidth The symbolWidth to set.
	 */
	public void setSymbolWidth(int symbolWidth)
	{
		setSymbolSize(symbolWidth,(int) (symbolWidth/SYMBOLPANEL_RATIO));
	}
	/**
	 * @param symbolPanel The symbolPanel to set.
	 */
	protected void setSymbolPanel(SymbolizerPanel symbolPanel)
	{
		this.symbolPanel = symbolPanel;
	}
	/**
	 * @param textPanel The textPanel to set.
	 */
	protected void setTextPanel(JPanel textPanel)
	{
		this.textPanel = textPanel;
	}
	public void setSelected(boolean b)
	{
		getCheckBox().setSelected(b);
	}
	public boolean isSelected()
	{
		return getCheckBox().isSelected();
	}
	protected void setElasticMode(boolean elasticMode)
	{
		this.elasticMode = elasticMode;
	}
	public void setOpaque(boolean arg0)
	{
		super.setOpaque(arg0);
		//getTextPanel().setOpaque(arg0);
		getSpacerPanel().setOpaque(arg0);
	}
	public Dimension getPreferredSize()
	{
		int symbh=getSymbolHeight();
		Dimension dim=new Dimension((int) (symbh*OVERALL_PREFERRED_RATIO),symbh);
		return dim;
	}
	/**
	 * @return
	 */
	public boolean isEditingMode()
	{
		return editingMode;
	}
}
