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

import com.vividsolutions.jump.I18N;

/**
 * First level menus names of JUMP
 * @author Basile Chandesris - <chandesris@pt-consulting.lu>
 */
public interface MenuNames {
	public static String FILE = I18N.get("ui.MenuNames.FILE");
	public static String EDIT = I18N.get("ui.MenuNames.EDIT");
	public static String VIEW = I18N.get("ui.MenuNames.VIEW");
	public static String LAYER = I18N.get("ui.MenuNames.LAYER");
	public static String TOOLS = I18N.get("ui.MenuNames.TOOLS");
	public static String[] TOOLS_GENERATE = new String[]
	                                                   {I18N.get("ui.MenuNames.TOOLS"),
	                                                	I18N.get("ui.MenuNames.TOOLS.GENERATE")};
	public static String[] TOOLS_WARP = new String[]
                                                   {I18N.get("ui.MenuNames.TOOLS"),
                                                		   
                                                   I18N.get("ui.MenuNames.TOOLS.WARP")};
	public static String[] TOOLS_ANALYSIS = new String[]
	                                                   {
	                                                I18N.get("ui.MenuNames.TOOLS"),
	                                                I18N.get("ui.MenuNames.TOOLS.ANALYSIS")};
	                                                   
	public static String[] TOOLS_QA = new String[]
                                                 {
                                                I18N.get("ui.MenuNames.TOOLS"),
                                                I18N.get("ui.MenuNames.TOOLS.QA")};
	public static String WINDOW = I18N.get("ui.MenuNames.WINDOW");
	public static String HELP = I18N.get("ui.MenuNames.HELP");
}
