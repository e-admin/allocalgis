/*
 * 
 * Created on 18-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.legend;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.util.ArrayList;

/**
 * Layout de la leyenda que organiza los elementos en columnas marcadas por
 * Componentes BreakEntry
 * @author juacas
 *
 */
public class LegendLayout2 implements LayoutManager
{
ArrayList comps=new ArrayList();
private boolean	elastic;
	/**
	 * 
	 */
	public LegendLayout2()
	{
	super();
	// TODO Auto-generated constructor stub
	}
public void setElasticMode(boolean elastic)
{
this.elastic=elastic;
}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	public void addLayoutComponent(String arg0, Component arg1)
	{
	comps.add(arg1);
	
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component arg0)
	{
	comps.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container cont)
	{
	int width=0;
	int height=0;
	int totalwidth=0;
	int totalheight=0;
	Component comp[]=cont.getComponents();
	for (int i = 0; i < comp.length; i++)
		{
//		if (!comps.contains(comp[i]))
//			continue;
		if (comp[i] instanceof BreakEntry && i!=comp.length-1)
			{
			totalheight=Math.max(totalheight,height);
			height=0;
			totalwidth=totalwidth+width;
			width=0;
			}
		
			if(comp[i].isVisible())
			{
			height=height+comp[i].getPreferredSize().height;
			width=Math.max(width, comp[i].getPreferredSize().width);
			}
		}
	totalheight=Math.max(totalheight,height);
	totalwidth=totalwidth+width;
	return new Dimension(totalwidth,totalheight);
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(Container cont)
	{
	int width=0;
	int height=0;
	int totalwidth=0;
	int totalheight=0;
	Component comp[]=cont.getComponents();
	
	for (int i = 0; i < comp.length; i++)
		{
//		if (!comps.contains(comp[i]))
//			continue;
		if (comp[i] instanceof BreakEntry && i!=comp.length-1)
			{
			totalheight=Math.max(totalheight,height);
			height=0;
			totalwidth=totalwidth+width;
			width=0;
			}
			
				if(comp[i].isVisible()){
			
			int compheight=comp[i].getMinimumSize().height;
			height=height+compheight;
			width=Math.max(width, comp[i].getMinimumSize().width);
			}
		}
	totalheight=Math.max(totalheight,height);
	return new Dimension(totalwidth,totalheight);
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(Container container)
	{
	int columns=1;
	
	if(elastic) // reconfigure layout if elasticmode
		layoutElastic(container);
	//Count columns
	Component comps[]= container.getComponents();
	for (int i = 0; i < comps.length-1; i++)
		{
		Component component=comps[i];
//		if (!this.comps.contains(comps[i]))
//			continue;
		if (comps[i] instanceof BreakEntry)
			{
			columns++;
			}
		}
	// get container sizes
	Dimension conDim=container.getSize();
//	Dimension mindim=minimumLayoutSize(container);
//	if (conDim.height<mindim.height || conDim.width<mindim.width)
//		conDim=mindim;
	int width=conDim.width/columns;
	
	// Layout
	int x=0,y=0;
	for (int i = 0; i < comps.length; i++)
		{
		Component component = comps[i];
//		if (!this.comps.contains(comps[i]))
//			continue;
		if (component instanceof BreakEntry && i!=comps.length-1)
			{
			x=x+width;
			y=0;
			//component.setVisible(false);
			}
		if(component.isVisible())
			{
			int height=component.getPreferredSize().height;
			component.setLocation(x,y);
			component.setSize(width,height);
			y=y+height;
			}
		
		}
	}

private void layoutElastic(Container cont)
{
if (cont.getComponentCount()==0)return;
Dimension containerDim= cont.getSize();

// remove breaks
Component [] comps=cont.getComponents();
for (int i = 0; i < comps.length; i++)
	{
	Component component = comps[i];
	if (component instanceof BreakEntry)
		cont.remove(component);
	}

Dimension compPreferredDim=cont.getComponent(0).getPreferredSize();
double ratioCont=((double)containerDim.width)/containerDim.height;
double ratioLay=((double)compPreferredDim.width)/compPreferredDim.height;
int columnsize=(int) Math.floor(Math.sqrt(
		(double)(ratioLay*cont.getComponentCount())/
		(double)(ratioCont)));
//int columnsize=containerDim.height/compPreferredDim.height;
// insert breaks
if (columnsize==0)return;

int numcolumns=cont.getComponentCount()/columnsize +1;
if (numcolumns==0)numcolumns=1;
for (int i=numcolumns-1;i>0;i--)
	{
	BreakEntry be=new BreakEntry();
	be.setVisible(false); // en el modo automático no se pueden utilizar los BreakEntry manualmente
	
	cont.add(be,i*columnsize);
	}

// set sizes
comps=cont.getComponents();
for (int i = 0; i < comps.length; i++)
	{
	Component component = comps[i];
	if (! (component instanceof BreakEntry))
	component.setSize(new Dimension(containerDim.width/numcolumns,containerDim.height/columnsize));
	}

}
}
