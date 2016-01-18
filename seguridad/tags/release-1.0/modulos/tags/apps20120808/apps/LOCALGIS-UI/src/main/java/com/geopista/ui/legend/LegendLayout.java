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
 * Created on 30-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.legend;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class LegendLayout extends GridBagLayout
{
	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(Container parent)
	{
		// Genera los constraints introduciendo columnas cada elemento nueva linea
		//invalidateLayout(parent);
		
		col=0;row=0;
		for (int i=0;i<parent.getComponentCount();i++)
			{
			GridBagConstraints contr= getConstraints(parent.getComponent(i));
		    configureComponent(parent.getComponent(i),contr);
			}
		super.layoutContainer(parent);
		col=0;row=0;
	}
	public void configureConstraints(Container parent)
	{
			// Genera los constraints introduciendo columnas cada elemento nueva linea
			//invalidateLayout(parent);
			
			col=0;row=0;
			for (int i=0;i<parent.getComponentCount();i++)
				{
				GridBagConstraints contr= getConstraints(parent.getComponent(i));
			    configureComponent(parent.getComponent(i),contr);
				}
			col=0;row=0;
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	int col=0,row=0;
	private void configureComponent(Component comp, GridBagConstraints contr)
	{
		contr = getNextConstraint(comp,contr);
		setConstraints(comp,contr);	
	}
	private GridBagConstraints getNextConstraint(Component comp, GridBagConstraints contr)
	{
		if (contr==null)
			contr=new GridBagConstraints();
	
		contr.gridx=col;
		contr.gridy=row++;
		contr.gridheight=1;
		contr.gridwidth=1;
		contr.weightx=1;
		contr.weighty=0;
	
		contr.anchor=GridBagConstraints.CENTER;
		contr.fill=GridBagConstraints.HORIZONTAL;
		if (comp instanceof BreakEntry)
		{
			col++;
			row=0;
		}		
		return contr;
	}
//	public void addLayoutComponent(Component comp, Object constraints)
//	{
//		if (comp instanceof BreakEntry)
//			col--; // si en el layout se ha cambiado de sitio de incrementaría dos veces
//		GridBagConstraints c=getNextConstraint(comp,(GridBagConstraints) constraints);
//		super.addLayoutComponent(comp, constraints);
//		//refreshLayout();		
//		//configureComponent(comp, (GridBagConstraints) constraints);
//	}
	/**
	 * 
	 */
	private void refreshLayout()
	{
		col=0;row=0;
		Iterator comps=this.comptable.keySet().iterator();
		while (comps.hasNext())
		{
		Component element = (Component) comps.next();
		GridBagConstraints contr= getConstraints(element);
	    configureComponent(element,contr);
		}
		col=0;row=0;
	}
	
	public void addLayoutComponent(Component comp, Object constraints)
	{
		
//		if (constraints==null)
//			constraints=getNextConstraint(comp,null);
		super.addLayoutComponent(comp, constraints);
	}
}
