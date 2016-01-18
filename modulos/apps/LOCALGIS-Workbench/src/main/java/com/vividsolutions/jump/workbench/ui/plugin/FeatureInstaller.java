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
package com.vividsolutions.jump.workbench.ui.plugin;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.ui.plugin.RegisterPlugInManager;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
/**
 * Adds a plug-in to the JUMP Workbench as a menu item. 
 */
//TODO - Refactoring: Rename this class to PlugInMenuInstaller [Jon Aquino 10/22/2003]
public class FeatureInstaller {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(FeatureInstaller.class);
  private interface Menu {

        void insert(JMenuItem menuItem, int i);

        String getText();

        int getItemCount();

        void add(JMenuItem menuItem);

    }
    private WorkbenchContext workbenchContext;
    private TaskMonitorManager taskMonitorManager = new TaskMonitorManager();
    private EnableCheckFactory checkFactory;
    

    private AppContext appContext=(AppContext) AppContext.getApplicationContext();



    private String temp = appContext.getI18nString("fuentes");
    private String[] fuentesArray = temp.split("\\,");

    private int index = Integer.parseInt(UserPreferenceStore.getUserPreference("menu.fuente","0",true));
    private String fuente = fuentesArray[index];

    private int estilo = Integer.parseInt(UserPreferenceStore.getUserPreference("menu.estilo","0",true));
    
    
    private int tamanno;
    

    public FeatureInstaller(WorkbenchContext workbenchContext) {
        this.workbenchContext = workbenchContext;
        checkFactory = new EnableCheckFactory(workbenchContext);
        tamanno=12;
        try
        {
            tamanno = Integer.parseInt(UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_MENU_SIZE,"12",true));
        }catch(Exception e)
        {}
    }
    /** @deprecated Use the EnableCheckFactory methods instead */
    public MultiEnableCheck createLayersSelectedCheck() {
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
            .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }
    /** @deprecated Use the EnableCheckFactory methods instead */
    public MultiEnableCheck createOneLayerSelectedCheck() {
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
            .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }
    /** @deprecated Use the EnableCheckFactory methods instead */
    public MultiEnableCheck createVectorsExistCheck() {
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
            .add(checkFactory.createAtLeastNVectorsMustBeDrawnCheck(1));
    }
    /** @deprecated Use the EnableCheckFactory methods instead */
    public MultiEnableCheck createFenceExistsCheck() {
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
            .add(checkFactory.createFenceMustBeDrawnCheck());
    }
    public void addMenuSeparator(String menu) {
    	
        addMenuSeparator(new String[] { menu });
    }
    public void addMenuSeparator(String[] menuPath) {
    translateMenuPath(menuPath);
        JMenu mainMenu = menuBarMenu(menuPath[0]);
        if (mainMenu==null)
        	{
			logger.debug("addMenuSeparator(String[] menuPath = " + menuPath
					+ ") - Menu inexistente.");

        	return;
        	}
        addMenuSeparator(createMenusIfNecessary(mainMenu, behead(menuPath)));
    }
    /**
	 * @param menuPath
	 */
	private void translateMenuPath(String[] menuPath)
	{
	for (int i = 0; i < menuPath.length; i++)
		{
		String string = menuPath[i];
		menuPath[i]=I18N.get(string);
		}
	
	}
	public void addMenuSeparator(JMenu menu) {
        Component separator = null;
        Component exitMenu = null;
        if (menu.getText().equals(AppContext.getApplicationContext().getI18nString("File"))) {
            //Ensure separator and Exit appear last
            separator = menu.getMenuComponent(menu.getMenuComponentCount() - 2);
            exitMenu = menu.getMenuComponent(menu.getMenuComponentCount() - 1);
            menu.remove(separator);
            menu.remove(exitMenu);
        }
        menu.addSeparator();
        if (menu.getText().equals(AppContext.getApplicationContext().getI18nString("File"))) {
            menu.add(separator);
            menu.add(exitMenu);
        }
    }
    private void associate(JMenuItem menuItem, PlugIn plugIn) {
        menuItem.addActionListener(
            AbstractPlugIn.toActionListener(
                plugIn,
                workbenchContext,
                taskMonitorManager));
    }
    private String[] behead(String[] a1) {
        String[] a2 = new String[a1.length - 1];
        System.arraycopy(a1, 1, a2, 0, a2.length);
        return a2;
    }
    public void addMainMenuItem(
        PlugIn executable,
        String menuName,
        String menuItemName,
        Icon icon,
        EnableCheck enableCheck) {
        addMainMenuItem(
            executable,
            new String[] { menuName },
            menuItemName,
            false,
            icon,
            enableCheck);
    }
    public void addLayerViewMenuItem(
        PlugIn executable,
        String menuName,
        String menuItemName) {
        addLayerViewMenuItem(executable, new String[] { menuName }, menuItemName);
    }
    public void addLayerNameViewMenuItem(
        PlugIn executable,
        String menuName,
        String menuItemName) {
        addLayerNameViewMenuItem(executable, new String[] { menuName }, menuItemName);
    }
    /**
     * Add a menu item to the main menu that is enabled only if the active internal
     * frame is a LayerViewPanelProxy.
     */
    public void addLayerViewMenuItem(
        PlugIn executable,
        String[] menuPath,
        String menuItemName) {
        addMainMenuItem(
            executable,
            menuPath,
            menuItemName,
            false,
            null,
            checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck());
    }
    /**
     * Add a menu item to the main menu that is enabled only if the active internal
     * frame is a LayerViewPanelProxy and a LayerNamePanelProxy.
     */
    public void addLayerNameViewMenuItem(
        PlugIn executable,
        String[] menuPath,
        String menuItemName) {
        addMainMenuItem(
            executable,
            menuPath,
            menuItemName,
            false,
            null,
            new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()));
    }
    /**
     *@param  menuPath        separate items with slashes; items will be created if they do not already exist
     *@param  menuActionListener  listener for the menu (not the menu item);
     *      useful for setting the menu item's enabled state; can be set to null
     *@param  checkBox whether to create a JCheckBoxMenuItem or a JMenuItem
     *@return the created JMenuItem
     *@see GUIUtil#toSmallIcon
     */
    public void addMainMenuItem(
        PlugIn executable,
        String[] menuPath,
        String menuItemName,
        boolean checkBox,
        Icon icon,
        EnableCheck enableCheck) {
        
        translateMenuPath(menuPath);
        
        Map properties = extractProperties(menuItemName);
        menuItemName = removeProperties(menuItemName);
        
        Blackboard blackboard = appContext.getBlackboard();
        RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
        if(registerPlugInManager == null)
        {
            registerPlugInManager = new RegisterPlugInManager();
            blackboard.put("RegisterPlugInManager",registerPlugInManager);
        }
        
        
//      Comprobamos si el PlugIn esta ya instalado en este menu y si
//      es asi no lo volvemos a instalar
        
        
        if(registerPlugInManager.isRegisteredPlugIn(workbenchContext.getIWorkbench().getGuiComponent().getJMenuBar(),executable.getClass()))
        {
            return;
        }
//      Si el PlugIn no esta instalado lo registramos en la lista de PlugIn
        registerPlugInManager.registerPlugIn(workbenchContext.getIWorkbench().getGuiComponent().getJMenuBar(),executable.getClass());

        JMenu menu = menuBarMenu(menuPath[0]);
        
        if (menu == null) {
            menu = (JMenu)installMnemonic(new JMenu(menuPath[0]), menuBar());
            addToMenuBar(menu);
        }
        
        JMenu parent = createMenusIfNecessary(menu, behead(menuPath));
        final JMenuItem menuItem = installMnemonic(
            checkBox ? new JCheckBoxMenuItem(menuItemName) : new JMenuItem(menuItemName), parent);
        menuItem.setIcon(icon);
        associate(menuItem, executable);

        menuItem.setFont(new Font(fuente,estilo,tamanno));
        if (properties.get("pos") != null) {
            parent.insert(menuItem, Integer.parseInt((String)properties.get("pos")));
        } else if (parent.getText().equals(AppContext.getApplicationContext().getI18nString("File")) && (parent == menu)) {
            //If menu is File, insert menu item just before Exit and its separator [Jon Aquino]
        if (menu.getItemCount()>1 && menu.getItem(menu.getItemCount()-1).getText().equals(AppContext.getApplicationContext().getI18nString("Exit")))
        	// It there is no Exit menu just append menuItem at the end 
            menu.insert(menuItem, menu.getItemCount() - 2);
        	else
        	menu.insert(menuItem, menu.getItemCount());
        } else {
            parent.add(menuItem);
        }
        if (enableCheck != null) {
            addMenuItemShownListener(menuItem, toMenuItemShownListener(enableCheck));
        }
    }
  private Menu createMenu(final JMenu menu) {
        return new Menu() {

            public void insert(JMenuItem menuItem, int i) {
                menu.insert(menuItem, i);
            }

            public String getText() {
                return menu.getText();
            }

            public int getItemCount() {
                return menu.getItemCount();
            }

            public void add(JMenuItem menuItem) {
                menu.add(menuItem);
            }
        };
    }
    private Map extractProperties(String menuItemName) {
		if (menuItemName.indexOf('{') == -1) { return new HashMap(); }
        Map properties = new HashMap();
        String s = menuItemName.substring(menuItemName.indexOf('{')+1, menuItemName.indexOf('}'));
        for (Iterator i = StringUtil.fromCommaDelimitedString(s).iterator(); i.hasNext(); ) {
            String property = (String) i.next();
            properties.put(property.substring(0, property.indexOf(':')).trim(), property.substring(property.indexOf(':')+1, property.length()).trim());
        }
        return properties;
	}
	public static String removeProperties(String menuItemName) {
		return menuItemName.indexOf('{') > -1 ? menuItemName.substring(0, menuItemName.indexOf('{')) : menuItemName;
	}
	public static JMenuItem installMnemonic(JMenuItem menuItem, MenuElement parent) {
		String text = menuItem.getText();
        StringUtil.replaceAll(text, "&&", "##");      
        int ampersandPosition = text.indexOf('&');
        if (-1 < ampersandPosition && ampersandPosition+1 < text.length()) {
           menuItem.setMnemonic(text.charAt(ampersandPosition+1));
           text = StringUtil.replace(text, "&", "", false);
        }
        else {
            installDefaultMnemonic(menuItem, parent);
        }
        //Double-ampersands get converted to single-ampersands. [Jon Aquino]        
        StringUtil.replaceAll(text, "##", "&");
        menuItem.setText(text);        
        return menuItem;
	}
	private static void installDefaultMnemonic(JMenuItem menuItem, MenuElement parent) {        		
		outer: for (int i = 0; i < menuItem.getText().length(); i++) {
		    //Swing stores mnemonics in upper case [Jon Aquino]
		    char candidate = Character.toUpperCase(menuItem.getText().charAt(i));
		    if (!Character.isLetter(candidate)) { continue; }
		    for (Iterator j = menuItems(parent).iterator(); j.hasNext(); ) {
                JMenuItem other = (JMenuItem) j.next();
		        if (other.getMnemonic() == candidate) {
                    continue outer;
		        } 
		    }
            menuItem.setMnemonic(candidate);
            return;
		}
		menuItem.setMnemonic(menuItem.getText().charAt(0));
	}
	private static Collection menuItems(MenuElement element) {
        ArrayList menuItems = new ArrayList();                                 
		if (element instanceof JMenuBar) {
            for (int i = 0; i < ((JMenuBar)element).getMenuCount(); i++) {
                CollectionUtil.addIfNotNull(((JMenuBar)element).getMenu(i), menuItems);
            }
        }
        else if (element instanceof JMenu) {
            for (int i = 0; i < ((JMenu)element).getItemCount(); i++) {
                CollectionUtil.addIfNotNull(((JMenu)element).getItem(i), menuItems);
            }
        }
        else if (element instanceof JPopupMenu) {
            MenuElement[] children = ((JPopupMenu)element).getSubElements();
            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JMenuItem) {
                    menuItems.add(children[i]);
                }
            }            
        }
        else {
        	if (element!=null)
        		Assert.shouldNeverReachHere(element.getClass().getName());
        }
        return menuItems;
	}
	private MenuItemShownListener toMenuItemShownListener(final EnableCheck enableCheck) {
        return new MenuItemShownListener() {
            public void menuItemShown(JMenuItem menuItem) {
                String errorMessage = null;
                try {
                    errorMessage = enableCheck.check(menuItem);
                } catch (Exception e) {
                    System.out.println(menuItem.getText());
                    workbenchContext.getIWorkbench().getGuiComponent().handleThrowable(e);
                }
                if (errorMessage != null) {
                    menuItem.setEnabled(false);
                    //<<TODO:AESTHETICS>> Wrap lines of tooltip text using HTML [Jon Aquino]
                    menuItem.setToolTipText(errorMessage);
                    return;
                }
                menuItem.setEnabled(true);
                //<<TODO:AESTHETICS>> Wrap lines of tooltip text using HTML [Jon Aquino]
                menuItem.setToolTipText(null);
            }
        };
    }
    /**
     * @return the leaf
     */
    private JMenu createMenusIfNecessary(JMenu parent, String[] menuPath) {
        if (menuPath.length == 0) {
            return parent;
        }
        JMenu child = (JMenu) childMenuItem(menuPath[0], parent);
        if (child == null) {
            child = (JMenu)installMnemonic(new JMenu(menuPath[0]), parent);
            child.setFont(new Font(fuente,estilo,tamanno));
            parent.add(child);
        }
        return createMenusIfNecessary(child, behead(menuPath));
    }
    /**
     * Workaround for Java Bug 4809393: "Menus disappear prematurely after
     * displaying modal dialog" Evidently fixed in Java 1.5. The workaround is
     * to wrap #actionPerformed with SwingUtilities#invokeLater.
     */
    public void addMainMenuItemWithJava14Fix(PlugIn executable,
            String[] menuPath, String menuItemName, boolean checkBox,
            Icon icon, EnableCheck enableCheck) {
        addMainMenuItem(executable, menuPath, menuItemName, checkBox, icon,
                enableCheck);
        JMenuItem menuItem = FeatureInstaller.childMenuItem(FeatureInstaller
                .removeProperties(menuItemName),
                ((JMenu) createMenusIfNecessary(menuBarMenu(menuPath[0]),
                        behead(menuPath))));
        if (menuItem!=null){
        	final ActionListener listener = abstractPlugInActionListener(menuItem
        			.getActionListeners());
        	menuItem.removeActionListener(listener);
        	menuItem.addActionListener(new ActionListener() {
        		public void actionPerformed(final ActionEvent e) {
        			SwingUtilities.invokeLater(new Runnable() {
        				public void run() {
        					listener.actionPerformed(e);
        				}
        			});
        		}
        	});
        }
    }

    public void addMenuItemShownListener(
        final JMenuItem menuItem,
        final MenuItemShownListener menuItemShownListener) {
        JMenu menu = (JMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();
        menu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                menuItemShownListener.menuItemShown(menuItem);
            }
            public void menuDeselected(MenuEvent e) {
            }
            public void menuCanceled(MenuEvent e) {
            }
        });
    }
    /**
     * @param enableCheck null to leave unspecified
     */
    public void addPopupMenuItem(
        JPopupMenu popupMenu,
        PlugIn executable,
        String menuItemName,
        boolean checkBox,
        Icon icon,
        EnableCheck enableCheck) {
        JMenuItem menuItem = installMnemonic(
            checkBox ? new JCheckBoxMenuItem(menuItemName) : new JMenuItem(menuItemName), popupMenu);
        menuItem.setIcon(icon);
        addPopupMenuItem(popupMenu, executable, menuItem, enableCheck);
    }
    private void addPopupMenuItem(
        JPopupMenu popupMenu,
        final PlugIn executable,
        final JMenuItem menuItem,
        final EnableCheck enableCheck) {
        
        Blackboard blackboard = appContext.getBlackboard();
        RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
        if(registerPlugInManager == null)
        {
            registerPlugInManager = new RegisterPlugInManager();
            blackboard.put("RegisterPlugInManager",registerPlugInManager);
        }
        
        
//      Comprobamos si el PlugIn esta ya instalado en este menu y si
//      es asi no lo volvemos a instalar
        
        
        if(registerPlugInManager.isRegisteredPlugIn(popupMenu,executable))
        {
            return;
        }
//      Si el PlugIn no esta instalado lo registramos en la lista de PlugIn
        registerPlugInManager.registerPlugIn(popupMenu,executable);

        associate(menuItem, executable);
        menuItem.setFont(new Font(fuente,estilo,tamanno));
        if (popupMenu!=null){
        	popupMenu.add(menuItem);
	        if (enableCheck != null) {
	            popupMenu.addPopupMenuListener(new PopupMenuListener() {
	                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	                    toMenuItemShownListener(enableCheck).menuItemShown(menuItem);
	                }
	                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	                }
	                public void popupMenuCanceled(PopupMenuEvent e) {
	                }
	            });
	        }
        }
    }
    public JMenuBar menuBar() {
        return workbenchContext.getIWorkbench().getGuiComponent().getJMenuBar();
    }
    /**
     *@return    the menu with the given name, or null if no such menu exists
     */
    public JMenu menuBarMenu(String childName) {
        MenuElement[] subElements = menuBar().getSubElements();
        for (int i = 0; i < subElements.length; i++) {
            if (!(subElements[i] instanceof JMenuItem)) {
                continue;
            }
            JMenuItem menuItem = (JMenuItem) subElements[i];
            if (menuItem.getText().equals(childName)) {
                return (JMenu) menuItem;
            }
        }
        return null;
    }
    public static JMenuItem childMenuItem(String childName, MenuElement menu) {
    if (menu instanceof JMenu) {
        return childMenuItem(childName, ((JMenu) menu).getPopupMenu());
    }
    if(menu!=null){
    	MenuElement[] childMenuItems = menu.getSubElements();
    	for (int i = 0; i < childMenuItems.length; i++) {
    		if (childMenuItems[i] instanceof JMenuItem
    				&& ((JMenuItem) childMenuItems[i]).getText().equals(
    						childName)) {
    			return ((JMenuItem) childMenuItems[i]);
    		}
    	}
    }
    return null;
    }
  
//    private JMenuItem childMenuItem(String childName, JMenu menu) {
//        for (int i = 0; i < menu.getItemCount(); i++) {
//            if (menu.getItem(i) == null) {
//                continue;
//            }
//            if (menu.getItem(i).getText().equals(childName)) {
//                return menu.getItem(i);
//            }
//        }
//        return null;
//    }
    private void addToMenuBar(JMenu menu) {
        menu.setFont(new Font(fuente,estilo,tamanno));

        menuBar().add(menu);

        //Ensure Window and Help are placed at the end. Remove #windowMenu and #helpMenu
        //*after* adding #menu, because #menu might be the Window or Help menu! [Jon Aquino]
        JMenu windowMenu = menuBarMenu(AppContext.getApplicationContext().getI18nString("GeopistaWorkbenchFrame.Window"));
        JMenu helpMenu = menuBarMenu(AppContext.getApplicationContext().getI18nString("Help"));


        //Customized workbenches may not have Window or Help menus [Jon Aquino]
        if (windowMenu != null) {
            menuBar().remove(windowMenu);
        }
        if (helpMenu != null) {
            menuBar().remove(helpMenu);
        }
        if (windowMenu != null) {
            windowMenu.setFont(new Font(fuente,estilo,tamanno));
            menuBar().add(windowMenu);
        }
        if (helpMenu != null) {
            helpMenu.setFont(new Font(fuente,estilo,tamanno));
            menuBar().add(helpMenu);
        }
    }
    private ActionListener abstractPlugInActionListener(
            ActionListener[] actionListeners) {
        for (int i = 0; i < actionListeners.length; i++) {
            if (actionListeners[i].getClass().getName().indexOf(
                    AbstractPlugIn.class.getName()) > -1) {
                return actionListeners[i];
            }
        }
        Assert.shouldNeverReachHere();
        return null;
    }
    
    /**
     * Borrado de los listeners para limpieza.
      @baseSatec Destruccion del elemento.    
     */    
    public void destroy(){
    	checkFactory=null;
    	
    	//Eliminamos el Plugin del Blackboard
        Blackboard blackboard = appContext.getBlackboard();
        RegisterPlugInManager registerPlugInManager = (RegisterPlugInManager) blackboard.get("RegisterPlugInManager");
        if(registerPlugInManager != null)
        {
        	registerPlugInManager.destroy();
        	blackboard.remove("RegisterPlugInManager");
        }
    }
}
