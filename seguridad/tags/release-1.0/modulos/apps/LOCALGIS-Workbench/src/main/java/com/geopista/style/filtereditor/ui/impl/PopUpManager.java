/*
 * Created on 22-sep-2004
 *
 */
package com.geopista.style.filtereditor.ui.impl;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.Expression;
import com.geopista.style.filtereditor.model.Operator;
import com.geopista.style.filtereditor.model.OperatorIdentifiers;
import com.geopista.style.filtereditor.model.impl.BBOXOp;
import com.geopista.style.filtereditor.model.impl.BinaryComparationOp;
import com.geopista.style.filtereditor.model.impl.BinaryLogicOp;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;
import com.geopista.style.filtereditor.model.impl.Literal;
import com.geopista.style.filtereditor.model.impl.PropertyIsBetweenOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsLikeOp;
import com.geopista.style.filtereditor.model.impl.PropertyIsNullOp;
import com.geopista.style.filtereditor.model.impl.PropertyName;
import com.geopista.style.filtereditor.model.impl.UnaryLogicOp;
import com.geopista.style.filtereditor.model.impl.UnknownOp;
import com.geopista.style.filtereditor.ui.impl.actions.AddBBOXOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.AddDistanceBufferOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.AddLiteralChildAction;
import com.geopista.style.filtereditor.ui.impl.actions.AddOperatorChildAction;
import com.geopista.style.filtereditor.ui.impl.actions.AddPropertyNameChildAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeBBOXOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeDistanceBufferOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeLiteralAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeOperatorTypeAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeOperatorTypeToBBOXOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangeOperatorTypeToDistanceBufferOpAction;
import com.geopista.style.filtereditor.ui.impl.actions.ChangePropertyNameAction;
import com.geopista.style.filtereditor.ui.impl.actions.DeleteNodeAction;

/**
 * @author enxenio s.l.
 *
 */
public class PopUpManager {
	
	public static JPopupMenu getPopUp(MutableTreeNode node, ActionListener actionListener) {
		JMenuItem menuItem;
		JMenu menu;
		JMenu subMenu;
		JMenu subSubMenu;
		
		JPopupMenu popup = new JPopupMenu();
		if (node.getParent() != null) { 
			menuItem = new JMenuItem("Borrar");
			menuItem.setAction(new DeleteNodeAction("Borrar"));
			menuItem.addActionListener(actionListener);
			popup.add(menuItem);
		}
		if (node instanceof Operator) {
			if ((node instanceof DistanceBufferOp)||(node instanceof BBOXOp)) {
				menuItem = new JMenuItem("Modificar");
				if (node instanceof DistanceBufferOp) {
					menuItem.setAction(new ChangeDistanceBufferOpAction("Modificar"));
				}
				else {
					menuItem.setAction(new ChangeBBOXOpAction("Modificar"));
				}
				menuItem.addActionListener(actionListener);
				popup.add(menuItem);
			}
			menu = new JMenu("Cambiar a");
				subMenu = new JMenu("Lógico");
					menuItem = new JMenuItem("AND");
					menuItem.setAction(new ChangeOperatorTypeAction("AND", OperatorIdentifiers.AND));
					menuItem.addActionListener(actionListener);
					subMenu.add(menuItem);
					menuItem = new JMenuItem("OR");
					menuItem.setAction(new ChangeOperatorTypeAction("OR", OperatorIdentifiers.OR));
					menuItem.addActionListener(actionListener);
					subMenu.add(menuItem);
					menuItem = new JMenuItem("NOT");
					menuItem.setAction(new ChangeOperatorTypeAction("NOT", OperatorIdentifiers.NOT));
					menuItem.addActionListener(actionListener);
					subMenu.add(menuItem);
				menu.add(subMenu);	
				if (!(node instanceof BinaryLogicOp)&&!(node instanceof UnaryLogicOp)) {		 
					subMenu = new JMenu("Comparación");
						menuItem = new JMenuItem("=");
						menuItem.setAction(new ChangeOperatorTypeAction("=", OperatorIdentifiers.PROPERTYISEQUALTO));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("<");
						menuItem.setAction(new ChangeOperatorTypeAction("<", OperatorIdentifiers.PROPERTYISLESSTHAN));	
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("<=");
						menuItem.setAction(new ChangeOperatorTypeAction("<=", OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem(">");
						menuItem.setAction(new ChangeOperatorTypeAction(">", OperatorIdentifiers.PROPERTYISGREATERTHAN));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem(">=");
						menuItem.setAction(new ChangeOperatorTypeAction(">=", OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("like");
						menuItem.setAction(new ChangeOperatorTypeAction("like", OperatorIdentifiers.PROPERTYISLIKE));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("is null");
						menuItem.setAction(new ChangeOperatorTypeAction("is null", OperatorIdentifiers.PROPERTYISNULL));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("between");
						menuItem.setAction(new ChangeOperatorTypeAction("between", OperatorIdentifiers.PROPERTYISBETWEEN));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
					menu.add(subMenu);			 
					subMenu = new JMenu("Espacial");
						menuItem = new JMenuItem("dwithin");
						menuItem.setAction(new ChangeOperatorTypeToDistanceBufferOpAction("dwithin", OperatorIdentifiers.DWITHIN));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("beyond");
						menuItem.setAction(new ChangeOperatorTypeToDistanceBufferOpAction("beyond", OperatorIdentifiers.BEYOND));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
						menuItem = new JMenuItem("bbox");
						menuItem.setAction(new ChangeOperatorTypeToBBOXOpAction("bbox"));
						menuItem.addActionListener(actionListener);
						subMenu.add(menuItem);
					menu.add(subMenu);
				}			 
			popup.add(menu);
			if (activateAddChildMenu(node)) {
				menu = new JMenu("Añadir hijo");
					if ((node instanceof BinaryLogicOp)||(node instanceof UnaryLogicOp)) {
						subMenu = new JMenu("Operador");
							subSubMenu = new JMenu("Lógico");
								menuItem = new JMenuItem("AND");
								menuItem.setAction(new AddOperatorChildAction("AND", OperatorIdentifiers.AND));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("OR");
								menuItem.setAction(new AddOperatorChildAction("OR", OperatorIdentifiers.OR));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("NOT");
								menuItem.setAction(new AddOperatorChildAction("NOT", OperatorIdentifiers.NOT));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
							subMenu.add(subSubMenu);			 
							subSubMenu = new JMenu("Comparación");
								menuItem = new JMenuItem("=");
								menuItem.setAction(new AddOperatorChildAction("=", OperatorIdentifiers.PROPERTYISEQUALTO));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("<");
								menuItem.setAction(new AddOperatorChildAction("<", OperatorIdentifiers.PROPERTYISLESSTHAN));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("<=");
								menuItem.setAction(new AddOperatorChildAction("<=", OperatorIdentifiers.PROPERTYISLESSTHANOREQUALTO));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem(">");
								menuItem.setAction(new AddOperatorChildAction(">", OperatorIdentifiers.PROPERTYISGREATERTHAN));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem(">=");
								menuItem.setAction(new AddOperatorChildAction(">=", OperatorIdentifiers.PROPERTYISGREATERTHANOREQUALTO));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("like");
								menuItem.setAction(new AddOperatorChildAction("like", OperatorIdentifiers.PROPERTYISLIKE));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("is null");
								menuItem.setAction(new AddOperatorChildAction("is null", OperatorIdentifiers.PROPERTYISNULL));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("between");
								menuItem.setAction(new AddOperatorChildAction("between", OperatorIdentifiers.PROPERTYISBETWEEN));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
							subMenu.add(subSubMenu);			 
							subSubMenu = new JMenu("Espacial");
								menuItem = new JMenuItem("dwithin");
								menuItem.setAction(new AddDistanceBufferOpAction("dwithin", OperatorIdentifiers.DWITHIN));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("beyond");
								menuItem.setAction(new AddDistanceBufferOpAction("beyond", OperatorIdentifiers.BEYOND));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
								menuItem = new JMenuItem("bbox");
								menuItem.setAction(new AddBBOXOpAction("bbox", OperatorIdentifiers.BBOX));
								menuItem.addActionListener(actionListener);
								subSubMenu.add(menuItem);
							subMenu.add(subSubMenu);			 
						menu.add(subMenu);
					}
					if (!(node instanceof BinaryLogicOp)&&!(node instanceof UnaryLogicOp)) {
						subMenu = new JMenu("Expresión");
							menuItem = new JMenuItem("Property Name");
							menuItem.setAction(new AddPropertyNameChildAction("Property Name"));
							menuItem.addActionListener(actionListener);
							subMenu.add(menuItem);
							menuItem = new JMenuItem("Literal");
							menuItem.setAction(new AddLiteralChildAction("Literal"));
							menuItem.addActionListener(actionListener);
							subMenu.add(menuItem);
						menu.add(subMenu);
					}
				popup.add(menu);
				}
		}
		else if (node instanceof Expression) {
			menuItem = new JMenuItem("Cambiar");
			if (node instanceof Literal) {
				menuItem.setAction(new ChangeLiteralAction("Modificar"));
			}
			else if (node instanceof PropertyName) {
				menuItem.setAction(new ChangePropertyNameAction("Modificar"));
			}
			menuItem.addActionListener(actionListener);
			popup.add(menuItem);
		}
		return popup;
	}
	
	private static boolean activateAddChildMenu(MutableTreeNode node) {
		boolean activate = true;
		if ((node instanceof BinaryComparationOp)||(node instanceof PropertyIsLikeOp)) {
			if (node.getChildCount() == 2) {
				activate = false;
			}
		}
		else if ((node instanceof UnaryLogicOp) ||(node instanceof PropertyIsNullOp) || (node instanceof DistanceBufferOp) || (node instanceof BBOXOp)){
			if (node.getChildCount() == 1) {
				activate = false;
			}
		}
		else if (node instanceof PropertyIsBetweenOp) {
			if (node.getChildCount() == 3) {
				activate = false;
			}
		}
		else if  (node instanceof UnknownOp) {
			activate = false;
		}
		return activate;
	}
}
