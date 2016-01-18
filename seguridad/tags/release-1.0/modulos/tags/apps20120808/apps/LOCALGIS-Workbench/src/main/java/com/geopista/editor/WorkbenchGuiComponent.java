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
 */
package com.geopista.editor;

import java.awt.Container;
import java.awt.MenuBar;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
/**
 * Interfaz que implementa el componente principal de las aplicaciones basadas
 * en el framework.
 * En la implementación inicial de JUMP el objeto debía ser un WorkbenchFrame
 * lo que impedía incluirlo como Component dentro de un Container.
 * 
 * Implementa todos los métodos añadidos al JFrame del WorkBenchFrame
 * 
 * @author juacas
 * Modificado el interfaz para que implemente ErrorHandler
 * y pueda ser utilizado en otros interfaces [JP]
 */
public interface WorkbenchGuiComponent extends ErrorHandler,LayerViewPanelContext
{
	public JFrame getMainFrame();
    public TitledPopupMenu getCategoryPopupMenu();
    public TitledPopupMenu getWMSLayerNamePopupMenu();
    public TitledPopupMenu getLayerNamePopupMenu();
    public WorkbenchToolBar getToolBar();
    public HTMLFrame getOutputFrame();
    public TaskComponent getActiveTaskComponent();
    public JInternalFrame getActiveInternalFrame();
    public Container getDesktopPane(); // definimos DesktopPane como el Component que contiene SplitPanel y el resto [Juan Pablo]
    public void addChoosableStyleClass(Class choosableStyleClass);
    public void setVisible(boolean visible);
    public void log(String message);
    public Map getNodeClassToPopupMenuMap();
    public void addComponentListener(ComponentListener l);
    public WorkbenchContext getContext();
    public void warnUser(String warning);
    public void removeInternalFrame(JInternalFrame internalFrame);
    public void handleThrowable(final Throwable t);
    public boolean hasInternalFrame(JInternalFrame internalFrame);
    public void setStatusMessage(String message);
    public void addWindowListener(WindowListener l);
    // He sustituido TaskComponent por JInternalFrame porque este método solo se usa para
    // crear subventanas que no son TaskComponent
    public void addInternalFrame(final JInternalFrame internalFrame, boolean alwaysOnTop, boolean autoUpdateToolBar);
    public void addInternalFrame(final JInternalFrame internalFrame);
    //public void activateFrame(TaskComponent frame); [Juan Pablo]
    public void activateFrame(JInternalFrame frame);
    public LayerNamePanelListener getLayerNamePanelListener();
    public String getLog();
    public String getTitle();
    public Set getChoosableStyleClasses();
    public JInternalFrame[] getInternalFrames();
    public TaskFrame addTaskFrame();
    public TaskFrame addTaskFrame(Task task);
    public MenuBar getMenuBar();
    public void setJMenuBar(JMenuBar menubar);
    public JMenuBar getJMenuBar();
    public void removeComponentListener(ComponentListener l);
    public String getMBCommittedMemory();
    public void addKeyboardShortcut(final int keyCode, final int modifiers, final PlugIn plugIn, final EnableCheck enableCheck);
    public LayerViewPanelListener getLayerViewPanelListener();
    public void toFront();
    public void setTimeMessage(String message);
    //public ViewportListener getViewportListener();
    public WorkbenchToolBar getToolBar(String toolBarName);
    public TaskComponent[] getInternalTaskComponents();
    public JPopupMenu getLayerViewPopupMenu();
    
}