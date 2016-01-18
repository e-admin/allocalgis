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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class EditablePanel extends JPanel
{

	private JPanel westPanel = null;
	private JPanel southPanel = null;
	private JPanel northPanel = null;
	private JPanel eastPanel = null;
	

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		// TODO Auto-generated method stub
		super.paint(g);
		
		Rectangle b=getBounds();
		((Graphics2D)g).setStroke(
				new BasicStroke(1,
								BasicStroke.CAP_BUTT,
								BasicStroke.JOIN_BEVEL,
								1,
								new float[]{4,4},
								0)) ;
		g.drawRect(0,0,b.width-1,b.height-1);
		g.setColor(Color.darkGray);
		g.fill3DRect(0,0,10,10,true);
		g.fill3DRect(b.width-10,0,10,10,true);
		g.fill3DRect(b.width-10,b.height-10,10,10,true);
		g.fill3DRect(0,b.height-10,10,10,true);
		//g.fill3DRect(b.width/2-5,b.height/2-5,10,10,true);
	}
	/**
	 * This is the default constructor
	 */
	public EditablePanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	int mousex=0,mousey=0;
	protected int zoneDragged=-1;
	protected void initialize() {
		this.setLayout(new BorderLayout());
		
		this.setOpaque(false);
		this.add(getWestPanel(), java.awt.BorderLayout.WEST);
		this.add(getSouthPanel(), java.awt.BorderLayout.SOUTH);
		this.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
		this.add(getEastPanel(), java.awt.BorderLayout.EAST);
		this.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				content.setVisible(true);
				zoneDragged=-1;
			}
			
			public void mousePressed(MouseEvent e)
			{
				mousex=e.getX();
				mousey=e.getY();
				content.setVisible(false);
				zoneDragged=activeHandle(e.getPoint());
				
			}

		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e)
			{
			switch(activeHandle(e.getPoint()))
			{
			case -1:
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				break;
			case 0:
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
				break;
			case 1:
				setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				break;
			case 2:
				setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				break;
			case 3:
				setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
				break;
			}
			
			}
			
			public void mouseDragged(java.awt.event.MouseEvent e) {    
				int x,y,w,h;

				Rectangle bounds = getBounds();
				Rectangle newbounds= (Rectangle) bounds.clone();
				if (zoneDragged==-1)
					zoneDragged=activeHandle(e.getPoint());
				
				switch(zoneDragged)
				{
				case -1:
					break;
				case 0:
					newbounds= new Rectangle(bounds.x+e.getX(),
							bounds.y+e.getPoint().y,
							(int) bounds.width/*-e.getX()*/,
							(int)bounds.height/*-e.getPoint().y*/);
					break;
				case 1:
					newbounds= new Rectangle(bounds.x,bounds.y+e.getPoint().y, e.getX(),bounds.height-e.getPoint().y);
						break;
				case 2:
					newbounds.width=e.getX();
					newbounds.height=e.getPoint().y;
					break;
				case 3:
					newbounds= new Rectangle(bounds.x+e.getX(),bounds.y, bounds.width-e.getX(),e.getPoint().y);
//					break;
				}
//				Rectangle bounds = getBounds();
//				Rectangle newbounds= (Rectangle) bounds.clone();
//				// e.getPoint es relativo al componente no al contenedor
//				if (bounds.width-e.getX() <10 && bounds.height-e.getPoint().y <10) //abajo derecha
//				{
//					newbounds.width=e.getX();
//					newbounds.height=e.getPoint().y;
//				}
//				else	
//				if (e.getX()<10 && e.getPoint().y<10) // arriba izda (PUNTO DE ARRASTRE)
//					newbounds= new Rectangle(bounds.x+e.getX(),
//							bounds.y+e.getPoint().y,
//							(int) bounds.width/*-e.getX()*/,
//							(int)bounds.height/*-e.getPoint().y*/);
//				else	
//				if (bounds.width-e.getX() <10 && e.getPoint().y<10)//derecha arriba
//					newbounds= new Rectangle(bounds.x,bounds.y+e.getPoint().y, e.getX(),bounds.height-e.getPoint().y);
//				else
//				if (e.getX() <10 && bounds.height-e.getPoint().y <10) // izda abajo
//					newbounds= new Rectangle(bounds.x+e.getX(),bounds.y, bounds.width-e.getX(),e.getPoint().y);
////				else// bandas de desplazamiento
////				if (e.getX() <10) //banda izda
////				{
////					mousex=0;
////					newbounds= new Rectangle(bounds.x+e.getX()-mousex,
////							bounds.y+e.getY()-mousey, bounds.width,bounds.height);
////					mousex=e.getX();
////					mousey=e.getY();
////				}
////				else
////				if (bounds.width-e.getX() <10)//banda izda
////				{
////					mousex=0;mousey=0;
////						newbounds= new Rectangle(bounds.x+e.getX()-mousex,
////								bounds.y+e.getY()-mousey, bounds.width,bounds.height);
////						mousex=e.getX();
////						mousey=e.getY();
////				}
				setBounds(newbounds);
				validate();
			}
		});
		this.setSize(309, 295);
	}
	Component content;
	public Component add(Component comp)
	{
		content=comp;
		add(comp,BorderLayout.CENTER);
		return comp;
	}
	/**
	 * This method initializes westPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getWestPanel() {
		if (westPanel == null) {
			westPanel = new JPanel();
			westPanel.setOpaque(false);
		}
		return westPanel;
	}
	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			southPanel.setOpaque(false);
		}
		return southPanel;
	}
	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			northPanel.setOpaque(false);
		}
		return northPanel;
	}
	/**
	 * This method initializes eastPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getEastPanel() {
		if (eastPanel == null) {
			eastPanel = new JPanel();
			eastPanel.setOpaque(false);
		}
		return eastPanel;
	}
	private int activeHandle(Point e)
	{
		Rectangle bounds = getBounds();
		Rectangle newbounds= (Rectangle) bounds.clone();
		// e.getPoint es relativo al componente no al contenedor
		if (bounds.width-e.x <10 && bounds.height-e.y <10) //abajo derecha
		{
			newbounds.width=e.x;
			newbounds.height=e.y;
			return 2;
		}
		else	
		if (e.x<10 && e.y<10) // arriba izda (PUNTO DE ARRASTRE)
			return 0;
		else	
		if (bounds.width-e.x <10 && e.y<10)// arriba derecha
			return 1;
		else
		if (e.x <10 && bounds.height-e.y <10) // abajo izda 
			return 3;
		
		return -1;
	}
    	public static void main(String[] args)
	{
		final JFrame fr = new JFrame("ere");
		fr.setSize(500,500);
		final EditablePanel ed=new EditablePanel();
		LegendPanel pan=new LegendPanel();
		LegendPanel.testInicialization(pan);
		
		ed.add(pan);
		
		final EditablePanel ed2=new EditablePanel();
		JTextPane txtPane= new JTextPane();
		txtPane.insertComponent(new JButton("test"));
		txtPane.setOpaque(false);
		try{
			txtPane.getDocument().insertString(0,"prueba",null);
		}catch ( BadLocationException e)
		{
			e.printStackTrace();
		}
		ed2.add(txtPane);
		fr.getContentPane().setLayout(null);
		JButton bot=new JButton("ToFront");
		bot.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				fr.getContentPane().add(fr.getContentPane().getComponent(fr.getContentPane().getComponentCount()-1),0);
				fr.getContentPane().validate();
			}});
		fr.getContentPane().add(bot);
		bot.setBounds(0,0,60,30);
		ed.setBounds(100,40,400,400);
		ed2.setBounds(20,40,100,100);
		fr.getContentPane().add(ed);
		fr.getContentPane().add(ed2);
		fr.setVisible(true);
		fr.getContentPane().setBackground(Color.gray);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
