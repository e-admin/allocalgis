/**
 * GeopistaWorkbenchFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.security.acl.AclNotFoundException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.SecurityManager;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.geopista.util.I18NUtils;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.Block;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.model.UndoableEditReceiver;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelListener;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.PrimaryInfoFrame;
import com.vividsolutions.jump.workbench.ui.RecursiveKeyListener;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.TrackedPopupMenu;
import com.vividsolutions.jump.workbench.ui.ViewportListener;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.plugin.CloneWindowPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.ChoosableStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 * This class is responsible for the main window of the GEOPISTA application.
 */
public class GeopistaWorkbenchFrame extends WorkbenchFrame implements
        LayerViewPanelContext, ViewportListener, WorkbenchGuiComponent,ISecurityPolicy
{

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    BorderLayout borderLayout1 = new BorderLayout();

    JLabel coordinateLabel = new JLabel();
    
    private JPopupMenu popupMenu = new TrackedPopupMenu();

    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = (JMenu) FeatureInstaller.installMnemonic(
            new JMenu(AppContext.getApplicationContext().getI18nString(
                    "GeopistaWorkbenchFrame.File")), menuBar); //$NON-NLS-1$

    JMenuItem exitMenuItem = FeatureInstaller.installMnemonic(new JMenuItem(AppContext
            .getApplicationContext().getI18nString("GeopistaWorkbenchFrame.Exit")), //$NON-NLS-1$
            fileMenu);

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel messageLabel = new JLabel();

    JPanel statusPanel = new JPanel();

    JLabel timeLabel = new JLabel();

    WorkbenchToolBar toolBar;

    HashMap toolBarMap = new HashMap();

    JMenu windowMenu = (JMenu) FeatureInstaller.installMnemonic(new JMenu(AppContext
            .getApplicationContext().getI18nString("GeopistaWorkbenchFrame.Window")), //$NON-NLS-1$
            menuBar);
    
    ConnectionStatus status;

    private TitledPopupMenu categoryPopupMenu = new TitledPopupMenu()
        {
            {
                addPopupMenuListener(new PopupMenuListener()
                    {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                            LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame())
                                    .getLayerNamePanel();
                            setTitle((panel.selectedNodes(Category.class).size() != 1) ? ("(" //$NON-NLS-1$
                                    + panel.selectedNodes(Category.class).size() + AppContext
                                    .getApplicationContext().getI18nString(
                                            "GeopistaWorkbenchFrame.categories_selected")) //$NON-NLS-1$
                                    : ((Category) panel.selectedNodes(Category.class)
                                            .iterator().next()).getName());
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                        {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e)
                        {
                        }
                    });
            }
        };

    private JDesktopPane desktopPane = new JDesktopPane();

    private boolean closeAllBoolean = true;

    // <<TODO:REMOVE>> Actually we're not using the three optimization
    // parameters
    // below. Remove. [Jon Aquino]
    private int envelopeRenderingThreshold = 500;

    private HTMLFrame outputFrame = new HTMLFrame(this)
        {
            public void setTitle(String title)
            {
                // Don't allow the title of the output frame to be changed.
            }

            {
                super.setTitle(AppContext.getApplicationContext().getI18nString(
                        "GeopistaWorkbenchFrame.output")); //$NON-NLS-1$
            }
        };

    private ImageIcon icon;

    private TitledPopupMenu layerNamePopupMenu = new TitledPopupMenu()
        {
            {
                addPopupMenuListener(new PopupMenuListener()
                    {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                            LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame())
                                    .getLayerNamePanel();
                            setTitle((panel.selectedNodes(Layer.class).size() != 1) ? ("(" //$NON-NLS-1$
                                    + panel.selectedNodes(Layer.class).size() + AppContext
                                    .getApplicationContext().getI18nString(
                                            "GeopistaWorkbenchFrame.layers_selected")) //$NON-NLS-1$
                                    : ((Layerable) panel.selectedNodes(Layer.class)
                                            .iterator().next()).getName());
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                        {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e)
                        {
                        }
                    });
            }
        };

    private TitledPopupMenu wmsLayerNamePopupMenu = new TitledPopupMenu()
        {
            {
                addPopupMenuListener(new PopupMenuListener()
                    {
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                        {
                            LayerNamePanel panel = ((LayerNamePanelProxy) getActiveInternalFrame())
                                    .getLayerNamePanel();
                            setTitle((panel.selectedNodes(WMSLayer.class).size() != 1) ? ("(" //$NON-NLS-1$
                                    + panel.selectedNodes(WMSLayer.class).size() + AppContext
                                    .getApplicationContext().getI18nString(
                                            "GeopistaWorkbenchFrame.WMS_layer_selected")) //$NON-NLS-1$
                                    : ((Layerable) panel.selectedNodes(WMSLayer.class)
                                            .iterator().next()).getName());
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                        {
                        }

                        public void popupMenuCanceled(PopupMenuEvent e)
                        {
                        }
                    });
            }
        };

    private JPopupMenu sessionMenu = null;

    private JMenuItem logMenuItem = null;

    private JCheckBoxMenuItem offlineMenuItem = null;

    private LayerNamePanelListener layerNamePanelListener = new LayerNamePanelListener()
        {
            public void layerSelectionChanged()
            {
                toolBar.updateEnabledState();
                updateEnabledStateNewToolBars();
            }
        };

    private LayerViewPanelListener layerViewPanelListener = new LayerViewPanelListener()
        {
            public void cursorPositionChanged(String x, String y)
            {
                coordinateLabel.setText("(" + x + ", " + y + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            public void selectionChanged()
            {
                toolBar.updateEnabledState();
                updateEnabledStateNewToolBars();
            }

            public void fenceChanged()
            {
                toolBar.updateEnabledState();
                updateEnabledStateNewToolBars();
            }

            public void painted(Graphics graphics)
            {
            }
        };

    // <<TODO:NAMING>> This name is not clear [Jon Aquino]
    private int maximumFeatureExtentForEnvelopeRenderingInPixels = 10;

    // <<TODO:NAMING>> This name is not clear [Jon Aquino]
    private int minimumFeatureExtentForAnyRenderingInPixels = 2;

    private StringBuffer log = new StringBuffer();

    private int taskSequence = 1;

    private WorkbenchContext workbenchContext;

    private JLabel memoryLabel = new JLabel();

    private String lastStatusMessage = ""; //$NON-NLS-1$

    private Set choosableStyleClasses = new HashSet();

    private JLabel wmsLabel = new JLabel();

    private ArrayList easyKeyListeners = new ArrayList();

    private Map nodeClassToLayerNamePopupMenuMap = CollectionUtil.createMap(new Object[] {
            Layerable.class, layerNamePopupMenu, WMSLayer.class,
            wmsLayerNamePopupMenu, Category.class, categoryPopupMenu });

    private int positionIndex = -1;

    private int primaryInfoFrameIndex = -1;

    private JLabel loginLabel = new JLabel();

    public GeopistaWorkbenchFrame(String title, ImageIcon icon,
            final WorkbenchContext workbenchContext) throws Exception
        {
            aplicacion.setMainFrame(this);
            setTitle(title);
            new Timer(1000, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        memoryLabel.setText(getMBCommittedMemory()
                                + AppContext.getApplicationContext().getI18nString(
                                        "GeopistaWorkbenchFrame.MB_committed")); //$NON-NLS-1$
                        memoryLabel.setToolTipText(LayerManager.layerManagerCount()
                                + AppContext.getApplicationContext().getI18nString(
                                        "GeopistaWorkbenchFrame.LayerManager") //$NON-NLS-1$
                                + StringUtil.s(LayerManager.layerManagerCount()));
                    }
                }).start();
            this.workbenchContext = workbenchContext;
            this.icon = icon;
            this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            toolBar = new WorkbenchToolBar(workbenchContext);
            try
            {
                jbInit();
                configureStatusLabel(messageLabel, 390);
                configureStatusLabel(coordinateLabel, 130);
                configureStatusLabel(memoryLabel, 80);
                configureStatusLabel(timeLabel, 200);
                configureStatusLabel(wmsLabel, 70);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            setConnectionStatusMessage(aplicacion.isOnline());
            setLoginStatusMessage(aplicacion.isLogged());
            // listen to connection state
            aplicacion.addAppContextListener(new AppContextListener()
                {

                    public void connectionStateChanged(GeopistaEvent e)
                    {
                        switch (e.getType())
                            {
                            case GeopistaEvent.DESCONNECTED:
                                setConnectionStatusMessage(false);
                                break;
                            case GeopistaEvent.RECONNECTED:
                                setConnectionStatusMessage(true);
                                break;
                            case GeopistaEvent.LOGGED_IN:
                                setLoginStatusMessage(true);
                                break;
                            case GeopistaEvent.LOGGED_OUT:
                                setLoginStatusMessage(false);
                                break;
                            }
                    }
                });
            new RecursiveKeyListener(this)
                {
                    public void keyTyped(KeyEvent e)
                    {
                        for (Iterator i = easyKeyListeners.iterator(); i.hasNext();)
                        {
                            KeyListener l = (KeyListener) i.next();
                            l.keyTyped(e);
                        }
                    }

                    public void keyPressed(KeyEvent e)
                    {
                        for (Iterator i = new ArrayList(easyKeyListeners).iterator(); i
                                .hasNext();)
                        {
                            KeyListener l = (KeyListener) i.next();
                            l.keyPressed(e);
                        }
                    }

                    public void keyReleased(KeyEvent e)
                    {
                        for (Iterator i = new ArrayList(easyKeyListeners).iterator(); i
                                .hasNext();)
                        {
                            KeyListener l = (KeyListener) i.next();
                            l.keyReleased(e);
                        }
                    }
                };
            installKeyboardShortcutListener();
        }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addEasyKeyListener(java.awt.event.KeyListener)
	 */
    
	public void addEasyKeyListener(KeyListener l)
    {
        easyKeyListeners.add(l);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#removeEasyKeyListener(java.awt.event.KeyListener)
	 */
    
	public void removeEasyKeyListener(KeyListener l)
    {
        easyKeyListeners.remove(l);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getMBCommittedMemory()
	 */
    
	public String getMBCommittedMemory()
    {
        // return new
        // DecimalFormat(AppContext.getApplicationContext().getI18nString("GeopistaWorkbenchFrame.formatoMBytes")).format(
        // //$NON-NLS-1$
        // (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
        // .freeMemory()) / (1024 * 1024d));
        return NumberFormat.getIntegerInstance().format(
                //$NON-NLS-1$
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                        / (1024 * 1024d));
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setEnvelopeRenderingThreshold(int)
	 */
    
	public void setEnvelopeRenderingThreshold(int newEnvelopeRenderingThreshold)
    {
        envelopeRenderingThreshold = newEnvelopeRenderingThreshold;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setMaximumFeatureExtentForEnvelopeRenderingInPixels(int)
	 */
    
	public void setMaximumFeatureExtentForEnvelopeRenderingInPixels(
            int newMaximumFeatureExtentForEnvelopeRenderingInPixels)
    {
        maximumFeatureExtentForEnvelopeRenderingInPixels = newMaximumFeatureExtentForEnvelopeRenderingInPixels;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#log(java.lang.String)
	 */
    
	public void log(String message)
    {
        log.append(new Date() + "  " + message //$NON-NLS-1$
                + System.getProperty("line.separator")); //$NON-NLS-1$
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getLog()
	 */
    
	public String getLog()
    {
        return log.toString();
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setMinimumFeatureExtentForAnyRenderingInPixels(int)
	 */
    
	public void setMinimumFeatureExtentForAnyRenderingInPixels(
            int newMinimumFeatureExtentForAnyRenderingInPixels)
    {
        minimumFeatureExtentForAnyRenderingInPixels = newMinimumFeatureExtentForAnyRenderingInPixels;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#displayLastStatusMessage()
	 */
    
	public void displayLastStatusMessage()
    {
        setStatusMessage(lastStatusMessage);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setStatusMessage(java.lang.String)
	 */
    
	public void setStatusMessage(String message)
    {
        lastStatusMessage = message;
        setStatusBarText(message);
        setStatusBarTextHighlighted(false, null);
    }

    private void setStatusBarText(String message)
    {
        // <<TODO:IMPROVE>> Treat null messages like "" [Jon Aquino]
        messageLabel.setText((message == "") ? " " : message); //$NON-NLS-1$ //$NON-NLS-2$
        messageLabel.setToolTipText(message);

        // Make message at least a space so that status bar won't collapse [Jon
        // Aquino]
    }

    /**
     * To highlight a message, call #warnUser.
     */
    private void setStatusBarTextHighlighted(boolean highlighted, Color color)
    {
        // Use #coordinateLabel rather than (unattached) dummy label because
        // dummy label's background does not change when L&F changes. [Jon
        // Aquino]
        messageLabel.setForeground(highlighted ? Color.black : coordinateLabel
                .getForeground());
        messageLabel.setBackground(highlighted ? color : coordinateLabel.getBackground());
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setLoginStatusMessage(boolean)
	 */
    
	public void setLoginStatusMessage(boolean logged)
    {
        if (logged)
        {
            loginLabel.setIcon(IconLoader.icon("lock_closed.png"));
            loginLabel.setToolTipText(aplicacion
                    .getI18nString("geopista.LoggedInStatusMessage"));
            /**Una vez que el usuario está logueado en la aplicación mostramos su nombre en el título de la ventana
            principal**/
            
            if (!this.getTitle().contains("Usuario:"))            
	            this.setTitle(this.getTitle()+" "+AppContext
	                    .getApplicationContext().getI18nString("geopista.usuario")+": "+SecurityManager.getPrincipal());
        } else
        {
            loginLabel.setIcon(IconLoader.icon("lock_open.png"));
            loginLabel.setToolTipText(aplicacion
                    .getI18nString("geopista.LoggedOutStatusMessage"));
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setConnectionStatusMessage(boolean)
	 */
    
	public void setConnectionStatusMessage(boolean connected)
    {
        if (!connected)
        {
            connectionLabel.setIcon(IconLoader.icon("no_network.png"));
            connectionLabel.setToolTipText(aplicacion
                    .getI18nString("geopista.OffLineStatusMessage"));

            getLogMenuItem().setEnabled(false);

        } else
        {
            connectionLabel.setIcon(IconLoader.icon("online.png"));
            connectionLabel.setToolTipText(aplicacion
                    .getI18nString("geopista.OnLineStatusMessage"));
            getLogMenuItem().setEnabled(true);
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setTimeMessage(java.lang.String)
	 */
    
	public void setTimeMessage(String message)
    {
        // <<TODO:IMPROVE>> Treat null messages like "" [Jon Aquino]
        timeLabel.setText((message == "") ? " " : message); //$NON-NLS-1$ //$NON-NLS-2$

        // Make message at least a space so that status bar won't collapse [Jon
        // Aquino]
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getActiveTaskComponent()
	 */
    
	public TaskComponent getActiveTaskComponent()
    {

        if (desktopPane.getSelectedFrame() instanceof TaskComponent)
            return (TaskComponent) desktopPane.getSelectedFrame();
        else
            return null;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getActiveInternalFrame()
	 */
    
	public JInternalFrame getActiveInternalFrame()
    {
        if (desktopPane.getSelectedFrame() instanceof JInternalFrame)
            return (JInternalFrame) desktopPane.getSelectedFrame();
        else
            return null;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getInternalFrames()
	 */
    
	public JInternalFrame[] getInternalFrames()
    {
        return desktopPane.getAllFrames();
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getCategoryPopupMenu()
	 */
    
	public TitledPopupMenu getCategoryPopupMenu()
    {
        return categoryPopupMenu;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getContext()
	 */
    
	public WorkbenchContext getContext()
    {
        return workbenchContext;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getDesktopPane()
	 */
    
	public Container getDesktopPane()
    {
        return desktopPane;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getEnvelopeRenderingThreshold()
	 */
    
	public int getEnvelopeRenderingThreshold()
    {
        return envelopeRenderingThreshold;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getLayerNamePopupMenu()
	 */
    
	public TitledPopupMenu getLayerNamePopupMenu()
    {
        return layerNamePopupMenu;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getWMSLayerNamePopupMenu()
	 */
    
	public TitledPopupMenu getWMSLayerNamePopupMenu()
    {
        return wmsLayerNamePopupMenu;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getLayerViewPanelListener()
	 */
    
	public LayerViewPanelListener getLayerViewPanelListener()
    {
        return layerViewPanelListener;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getNodeClassToPopupMenuMap()
	 */
    
	public Map getNodeClassToPopupMenuMap()
    {
        return nodeClassToLayerNamePopupMenuMap;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getLayerNamePanelListener()
	 */
    
	public LayerNamePanelListener getLayerNamePanelListener()
    {
        return layerNamePanelListener;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getMaximumFeatureExtentForEnvelopeRenderingInPixels()
	 */
    
	public int getMaximumFeatureExtentForEnvelopeRenderingInPixels()
    {
        return maximumFeatureExtentForEnvelopeRenderingInPixels;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getMinimumFeatureExtentForAnyRenderingInPixels()
	 */
    
	public int getMinimumFeatureExtentForAnyRenderingInPixels()
    {
        return minimumFeatureExtentForAnyRenderingInPixels;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getOutputFrame()
	 */
    
	public HTMLFrame getOutputFrame()
    {
        return outputFrame;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getToolBar()
	 */
    
	public WorkbenchToolBar getToolBar()
    {
        return toolBar;
    }

    // Aqui no creo que deba ir un TaskFrame por si se quiere activar otro tipo
    // de JinternalFrame
    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#activateFrame(javax.swing.JInternalFrame)
	 */
    
	public void activateFrame(JInternalFrame frame)
    {
        frame.moveToFront();
        frame.requestFocus();
        try
        {
            frame.setSelected(true);
            if (!(frame instanceof TaskFrame))
            {
                frame.setMaximum(false);
            }
        } catch (PropertyVetoException e)
        {
            warnUser(StringUtil.stackTrace(e));
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addInternalFrame(javax.swing.JInternalFrame)
	 */
    // REVISAR: No parece necesario evitar el JInternalFrame
    // public void addInternalFrame(final TaskComponent internalFrame) {
    
	public void addInternalFrame(final JInternalFrame internalFrame)
    {

        addInternalFrame(internalFrame, false, true);
    }

    // REVISAR: No parece necesario evitar el JInternalFrame
    // public void addInternalFrame(final TaskComponent internalFrame,
    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addInternalFrame(javax.swing.JInternalFrame, boolean, boolean)
	 */
    
	public void addInternalFrame(final JInternalFrame internalFrame, boolean alwaysOnTop,
            boolean autoUpdateToolBar)
    {
        if (internalFrame instanceof LayerManagerProxy)
        {
            setClosingBehaviour((LayerManagerProxy) internalFrame);
            installTitleBarModifiedIndicator((LayerManagerProxy) internalFrame);
        }

        // <<TODO:IMPROVE>> Listen for when the frame closes, and when it does,
        // activate the topmost frame. Because Swing does not seem to do this
        // automatically. [Jon Aquino]
        internalFrame.setFrameIcon(icon);

        // Call JInternalFrame#setVisible before JDesktopPane#add; otherwise,
        // the
        // TreeLayerNamePanel starts too narrow (100 pixels or so) for some
        // reason.
        // <<TODO>>Investigate. [Jon Aquino]
        internalFrame.moveToFront();
       
        internalFrame.setVisible(true);
        desktopPane.add((Component) internalFrame,
                alwaysOnTop ? JLayeredPane.DRAG_LAYER : JLayeredPane.DRAG_LAYER);


        if (autoUpdateToolBar)
        {
            internalFrame.addInternalFrameListener(new InternalFrameListener()
                {
                    public void internalFrameActivated(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();

                        // Associate current cursortool with the new frame [Jon
                        // Aquino]
                        toolBar.reClickSelectedCursorToolButton();
                        reClickSelectedCursorToolButtonNewToolBar();
                    }

                    public void internalFrameClosed(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }

                    public void internalFrameClosing(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }

                    public void internalFrameDeactivated(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }

                    public void internalFrameDeiconified(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }

                    public void internalFrameIconified(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }

                    public void internalFrameOpened(InternalFrameEvent e)
                    {
                        toolBar.updateEnabledState();
                        updateEnabledStateNewToolBars();
                    }
                });

            // Call #activateFrame *after* adding the listener. [Jon Aquino]
            activateFrame(internalFrame);
            position(internalFrame);
           // desktopPane.moveToFront(internalFrame); 
                 }
    }

    private void installTitleBarModifiedIndicator(final LayerManagerProxy internalFrame)
    {
        final JInternalFrame i = (JInternalFrame) internalFrame;
        new Block()
            {
                // Putting updatingTitle in a Block is better than making it an
                // instance variable, because this way there is one
                // updatingTitle
                // for each
                // internal frame, rather than one for all internal frames. [Jon
                // Aquino]
                private boolean updatingTitle = false;

                private GeopistaLayer modifiedLayer = null;

                private void updateTitle()
                {
                    if (updatingTitle)
                    {
                        return;
                    }
                    updatingTitle = true;
                    try
                    {
                        String newTitle = i.getTitle();
                        if (newTitle.charAt(0) == '*')
                        {
                            newTitle = newTitle.substring(1);
                        }
                        boolean mapModified = false;
                        if (internalFrame.getLayerManager() instanceof GeopistaLayerManager)
                        {
                            if (((GeopistaLayerManager) internalFrame.getLayerManager())
                                    .isDirty())
                            {
                                mapModified = true;
                            }

                        }

                        boolean styleModified = false;
                        if (modifiedLayer instanceof GeopistaLayer)
                        {
                            SLDStyle sldStyle = (SLDStyle) modifiedLayer
                                    .getStyle(SLDStyle.class);
                            if (sldStyle != null)
                            {
                                if (sldStyle.isPermanentChanged())
                                {
                                    styleModified = true;
                                }
                            }
                        }

                        if (!internalFrame.getLayerManager()
                                .getLayersWithModifiedFeatureCollections().isEmpty()
                                || mapModified || styleModified)
                        {
                            newTitle = '*' + newTitle;
                        }
                        i.setTitle(newTitle);
                    } finally
                    {
                        updatingTitle = false;
                    }
                }

                public Object yield()
                {
                    internalFrame.getLayerManager().addLayerListener(new LayerListener()
                        {
                            public void layerChanged(LayerEvent e)
                            {
                                if ((e.getType() == LayerEventType.METADATA_CHANGED)
                                        || (e.getType() == LayerEventType.REMOVED)
                                        || (e.getType() == LayerEventType.ADDED)
                                        || (e.getType() == LayerEventType.APPEARANCE_CHANGED))
                                {
                                    modifiedLayer = null;
                                    if (e.getLayerable() instanceof GeopistaLayer)
                                    {
                                        modifiedLayer = (GeopistaLayer) e.getLayerable();
                                    }
                                    updateTitle();
                                }
                            }

                            public void categoryChanged(CategoryEvent e)
                            {
                                updateTitle();
                            }

                            public void featuresChanged(FeatureEvent e)
                            {

                            }
                        });
                    i.addPropertyChangeListener(JInternalFrame.TITLE_PROPERTY,
                            new PropertyChangeListener()
                                {
                                    public void propertyChange(PropertyChangeEvent e)
                                    {
                                        updateTitle();
                                    }
                                });

                    return null;
                }
            }.yield();
    }

    private void setClosingBehaviour(final LayerManagerProxy internalFrame)
    {
        final JInternalFrame i = (JInternalFrame) internalFrame;
        i.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        i.addInternalFrameListener(new InternalFrameAdapter()
            {

                public void internalFrameClosing(InternalFrameEvent e)
                {
                    if (e.getSource() instanceof TaskFrame)
                    {
                        TaskFrame taskFrame = (TaskFrame) e.getSource();
                        LockManager lockManager = (LockManager) taskFrame
                                .getLayerViewPanel().getBlackboard().get(
                                        LockManager.LOCK_MANAGER_KEY);

                        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
                                aplicacion.getMainFrame(), workbenchContext
                                        .getErrorHandler());

                        final UnlockTaskThreat monitorThreat = new UnlockTaskThreat(
                                taskFrame, progressDialog);

                        if (lockManager.existLockedFeaturesInTask((LayerManager)taskFrame
                                .getLayerManager()))
                        {

                            progressDialog.setTitle(aplicacion
                                    .getI18nString("UnlockFeatures"));
                            progressDialog.addComponentListener(new ComponentAdapter()
                                {
                                    public void componentShown(ComponentEvent e)
                                    {
                                        // Wait for the dialog to appear before
                                        // starting the task. Otherwise
                                        // the task might possibly finish before
                                        // the
                                        // dialog appeared and the
                                        // dialog would never close. [Jon
                                        // Aquino]
                                        monitorThreat.start();
                                    }
                                });
                            GUIUtil.centreOnWindow(progressDialog);
                            progressDialog.setVisible(true);

                        }

                        if (monitorThreat.confirmResultError == 1)
                            return;
                    }

                    if (1 == getInternalFramesAssociatedWith(
                            (LayerManager)internalFrame.getLayerManager()).size())
                    {
                        if (confirmClose(
                                aplicacion
                                        .getI18nString("GeopistaWorkbenchFrame.CerrarMapa"), internalFrame //$NON-NLS-1$
                                        .getLayerManager()
                                        .getLayersWithModifiedFeatureCollections()))
                        {
                            GUIUtil.dispose(i, desktopPane);
                            boolean firingEvents = internalFrame.getLayerManager()
                                    .isFiringEvents();
                            internalFrame.getLayerManager().setFiringEvents(false);
                            internalFrame.getLayerManager().dispose();
                            internalFrame.getLayerManager().setFiringEvents(firingEvents);
                        }
                    } else
                    {
                        GUIUtil.dispose(i, desktopPane);
                    }
                }

            });
    }

    private Collection getInternalFramesAssociatedWith(LayerManager layerManager)
    {
        ArrayList internalFramesAssociatedWithLayerManager = new ArrayList();
        JInternalFrame[] internalFrames = getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++)
        {
            if (internalFrames[i] instanceof LayerManagerProxy
                    && (((LayerManagerProxy) internalFrames[i]).getLayerManager() == layerManager))
            {
                internalFramesAssociatedWithLayerManager.add(internalFrames[i]);
            }
        }

        return internalFramesAssociatedWithLayerManager;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addTaskFrame()
	 */
    
	public TaskFrame addTaskFrame()
    {
        TaskFrame f = addTaskFrame(createTask());

        return f;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#createTask()
	 */
    
	public Task createTask()
    {
        GeopistaMap task = new GeopistaMap();
        task.getLayerManager().addLayerListener(
                (LayerListener) workbenchContext.getIWorkbench());
        // LayerManager shouldn't automatically add categories in its
        // constructor.
        // Sometimes we want to create a LayerManager with no categories
        // (e.g. in OpenProjectPlugIn). [Jon Aquino]
        task.getLayerManager().addCategory(StandardCategoryNames.WORKING);
        task.getLayerManager().addCategory(
        		I18NUtils.i18n_getname(StandardCategoryNames.SYSTEM));
        task.setName(aplicacion.getI18nString("Map") + " " + taskSequence++); //$NON-NLS-1$ //$NON-NLS-2$
        // task.setSystemId(task.getName());
        // ponemos una l para saber si el mapa el local o de base de datos
        task.setSystemId("l" + String.valueOf(System.currentTimeMillis()));
        return task;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addTaskFrame(com.vividsolutions.jump.workbench.model.Task)
	 */
    
    public TaskFrame addTaskFrame(Task task)
    {
        return addTaskFrame(new GeopistaTaskFrame(task, workbenchContext));
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addTaskFrame(com.vividsolutions.jump.workbench.ui.TaskFrame)
	 */
    
	public TaskFrame addTaskFrame(TaskFrame taskFrame)
    {
        taskFrame.getTask().getLayerManager().addLayerListener(new LayerListener()
            {
                public void featuresChanged(FeatureEvent e)
                {
                }

                public void categoryChanged(CategoryEvent e)
                {
                    toolBar.updateEnabledState();
                    updateEnabledStateNewToolBars();
                }

                public void layerChanged(LayerEvent layerEvent)
                {
                    toolBar.updateEnabledState();
                    updateEnabledStateNewToolBars();
                }
            });
        addInternalFrame(taskFrame);
        taskFrame.getLayerViewPanel().getLayerManager().getUndoableEditReceiver().add(
                new UndoableEditReceiver.Listener()
                    {
                        public void undoHistoryChanged()
                        {
                            toolBar.updateEnabledState();
                            updateEnabledStateNewToolBars();
                        }

                        public void undoHistoryTruncated()
                        {
                            toolBar.updateEnabledState();
                            updateEnabledStateNewToolBars();
                            log(AppContext.getApplicationContext().getI18nString(
                                    "GeopistaWorkbenchFrame.Undo_history")); //$NON-NLS-1$
                        }
                    });
        //redimensiona los internalFrame al abrir un mapa
        desktopPane.getDesktopManager().resizeFrame(taskFrame, taskFrame.getX(),
                taskFrame.getY(), desktopPane.getWidth() - taskFrame.getX(),
                desktopPane.getHeight() - taskFrame.getY());

        return taskFrame;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#flash(com.vividsolutions.jump.workbench.ui.HTMLFrame)
	 */
    
	public void flash(final HTMLFrame frame)
    {
        final Color originalColor = frame.getBackgroundColor();
        new Timer(100, new ActionListener()
            {
                private int tickCount = 0;

                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        tickCount++;
                        frame.setBackgroundColor(((tickCount % 2) == 0) ? originalColor
                                : Color.yellow);
                        if (tickCount == 2)
                        {
                            Timer timer = (Timer) e.getSource();
                            timer.stop();
                        }
                    } catch (Throwable t)
                    {
                        handleThrowable(t);
                    }
                }
            }).start();
    }

    private void flashStatusMessage(final String message, final Color color)
    {
        new Timer(100, new ActionListener()
            {
                private int tickCount = 0;

                public void actionPerformed(ActionEvent e)
                {
                    tickCount++;

                    // This message is important, so overwrite whatever is on
                    // the
                    // status bar. [Jon Aquino]
                    setStatusBarText(message);
                    setStatusBarTextHighlighted((tickCount % 2) == 0, color);
                    if (tickCount == 4)
                    {
                        Timer timer = (Timer) e.getSource();
                        timer.stop();
                    }
                }
            }).start();
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#handleThrowable(java.lang.Throwable)
	 */
    
	public void handleThrowable(final Throwable t)
    {
        log(StringUtil.stackTrace(t));

        Component parent = this;
        Window[] ownedWindows = getOwnedWindows();
        for (int i = 0; i < ownedWindows.length; i++)
        {
            if (ownedWindows[i] instanceof Dialog && ownedWindows[i].isVisible()
                    && ((Dialog) ownedWindows[i]).isModal())
            {
                parent = ownedWindows[i];

                break;
            }
        }
        // if (lastFiveThrowableDates.size() == 5 && new Date().getTime()
        // - ((Date) lastFiveThrowableDates.get(0)).getTime() < 1000 * 60) {
        // flashStatusMessage(t.toString(), Color.red);
        // } else {
        handleThrowable(t, parent);
        // }
    }

    public static void handleThrowable(final Throwable t, final Component parent)
    {
        t.printStackTrace(System.err);
        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    ErrorDialog.show(parent, StringUtil.toFriendlyName(t.getClass()
                            .getName()), toMessage(t), StringUtil.stackTrace(t));
                }
            });
    }

    private ArrayList lastFiveThrowableDates = new ArrayList()
        {

            public boolean add(Object o)
            {
                if (size() == 5)
                {
                    remove(0);
                }
                return super.add(o);
            }
        };

    private JPanel dockingPanel = new JPanel()
        {
            public void doLayout()
            {
                super.doLayout();
                int maxy = 0;

                for (int i = 0; i < this.getComponentCount(); i++)
                {
                    if (this.getComponent(i).getBounds().getMaxY() > this.getHeight())
                    {
                        this.setPreferredSize(new Dimension(getParent().getWidth(),
                                (int) (getComponent(i).getBounds().getMaxY() + 5)));
                    }
                    maxy = Math.max(maxy, (int) getComponent(i).getBounds().getMaxY());
                }
                if (maxy < getHeight())
                    setPreferredSize(new Dimension(getParent().getWidth(), maxy + 5));
                getParent().doLayout();
            }
        };

    private JLabel connectionLabel = new JLabel();

    public static String toMessage(Throwable t)
    {
        String message;
        if (t.getLocalizedMessage() == null)
        {
            message = AppContext.getApplicationContext().getI18nString(
                    "GeopistaWorkbenchFrame.NoDescription"); //$NON-NLS-1$
        } else if (t.getLocalizedMessage().toLowerCase().indexOf(
                AppContext.getApplicationContext().getI18nString(
                        "GeopistaWorkbenchFrame.sidelocationconflict")) > -1) { //$NON-NLS-1$
            message = t.getLocalizedMessage()
                    + AppContext.getApplicationContext().getI18nString(
                            "GeopistaWorkbenchFrame.check_for_invalid_geometries"); //$NON-NLS-1$
        } else
        {
            message = t.getLocalizedMessage();
        }

        return message + " (" //$NON-NLS-1$
                + StringUtil.toFriendlyName(t.getClass().getName()) + ")"; //$NON-NLS-1$
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#hasInternalFrame(javax.swing.JInternalFrame)
	 */
    
	public boolean hasInternalFrame(JInternalFrame internalFrame)
    {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++)
        {
            if (frames[i] == internalFrame)
            {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#removeInternalFrame(javax.swing.JInternalFrame)
	 */
    
	public void removeInternalFrame(JInternalFrame internalFrame)
    {
        // Looks like #closeFrame is the proper way to remove an internal
        // frame.
        // It will activate the next frame. [Jon Aquino]
        desktopPane.getDesktopManager().closeFrame(internalFrame);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#warnUser(java.lang.String)
	 */
    
	public void warnUser(String warning)
    {
        log(AppContext.getApplicationContext().getI18nString(
                "GeopistaWorkbenchFrame.Warning") + warning); //$NON-NLS-1$
        flashStatusMessage(warning, Color.yellow);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#zoomChanged(com.vividsolutions.jts.geom.Envelope)
	 */
    
	public void zoomChanged(Envelope modelEnvelope)
    {
        toolBar.updateEnabledState();
        updateEnabledStateNewToolBars();
    }

    void exitMenuItem_actionPerformed(ActionEvent e)
    {
        try
        {
            unlockLostFeatures();
        } catch (LockException e1)
        {
            return;
        }
        closeApplication();
    }

    void this_componentShown(ComponentEvent e)
    {
        try
        {
            // If the first internal frame is not a TaskWindow (as may be the
            // case in
            // custom workbenches), #updateEnabledState() will ensure that the
            // cursor-tool buttons are disabled. [Jon Aquino]
            toolBar.updateEnabledState();
            updateEnabledStateNewToolBars();
        } catch (Throwable t)
        {
            handleThrowable(t);
        }
    }

    void this_windowClosing(WindowEvent e)
    {

        try
        {
            unlockLostFeatures();
        } catch (LockException e1)
        {
            return;
        }
        closeApplication();
    }

    void windowMenu_menuSelected(MenuEvent e)
    {
        // <<TODO:MAINTAINABILITY>> This algorithm is not robust. It assumes
        // the Window
        // menu has exactly one "regular" menu item (newWindowMenuItem). [Jon
        // Aquino]
        if (windowMenu.getItemCount() > 0
                && windowMenu.getItem(0) != null
                && windowMenu.getItem(0).getText().equals(
                        AbstractPlugIn.createName(CloneWindowPlugIn.class)))
        {
            JMenuItem newWindowMenuItem = windowMenu.getItem(0);
            windowMenu.removeAll();
            windowMenu.add(newWindowMenuItem);
            windowMenu.addSeparator();
        } else
        {
            // ezLink doesn't have a Clone Window menu [Jon Aquino]
            windowMenu.removeAll();
        }

        final JInternalFrame[] frames = (JInternalFrame[]) desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++)
        {
            JMenuItem menuItem = new JMenuItem();
            // Increase truncation threshold from 20 to 40, for eziLink [Jon
            // Aquino]
            menuItem.setText(GUIUtil.truncateString(frames[i].getTitle(), 40));
            associate(menuItem, frames[i]);
            windowMenu.add(menuItem);
        }
        if (windowMenu.getItemCount() == 0)
        {
            // For ezLink [Jon Aquino]
            windowMenu.add(new JMenuItem(AppContext.getApplicationContext()
                    .getI18nString("GeopistaWorkbenchFrame.noWindows"))); //$NON-NLS-1$
        }
    }

    private void associate(JMenuItem menuItem, final JInternalFrame frame)
    {
        menuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        activateFrame((JInternalFrame) frame);
                    } catch (Throwable t)
                    {
                        handleThrowable(t);
                    }
                }
            });
    }

    private void closeApplication()
    {
        if (confirmClose(
                aplicacion.getI18nString("GeopistaWorkbenchFrame.ExitJUMP"), getLayersWithModifiedFeatureCollections())) { //$NON-NLS-1$
            // PersistentBlackboardPlugIn listens for when the workbench is
            // hidden [Jon Aquino]
            setVisible(false);
            if (getCloseAll() == true)
            {
                // Invoke System#exit a fter all pending GUI events have been
                // fired
                // (e.g. the hiding of this WorkbenchFrame) [Jon Aquino]
                SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            System.exit(0);
                        }
                    });
            }
        }

        // Comento el else porque oculta la pantalla y no debe hcer eso
        /*
         * else { this.dispose(); }
         */
    }

    private Collection getLayersWithModifiedFeatureCollections()
    {
        ArrayList layersWithModifiedFeatureCollections = new ArrayList();
        for (Iterator i = getLayerManagers().iterator(); i.hasNext();)
        {
            LayerManager layerManager = (LayerManager) i.next();
            layersWithModifiedFeatureCollections.addAll(layerManager
                    .getLayersWithModifiedFeatureCollections());
        }

        return layersWithModifiedFeatureCollections;
    }

    private Collection getLayerManagers()
    {
        // Multiple windows may point to the same LayerManager, so use
        // a Set. [Jon Aquino]
        HashSet layerManagers = new HashSet();
        JInternalFrame[] internalFrames = getInternalFrames();
        for (int i = 0; i < internalFrames.length; i++)
        {
            if (internalFrames[i] instanceof LayerManagerProxy)
            {
                layerManagers.add(((LayerManagerProxy) internalFrames[i])
                        .getLayerManager());
            }
        }

        return layerManagers;
    }

    private void configureStatusLabel(JLabel label, int width)
    {
        label.setMinimumSize(new Dimension(width, (int) label.getMinimumSize()
                .getHeight()));
        label.setMaximumSize(new Dimension(width, (int) label.getMaximumSize()
                .getHeight()));
        label.setPreferredSize(new Dimension(width, (int) label.getPreferredSize()
                .getHeight()));
    }

    private void jbInit() throws Exception
    {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setIconImage(icon.getImage());
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseClicked(java.awt.event.MouseEvent e)
                {

                    if (e.getButton() == MouseEvent.BUTTON3)// drcho
                    {
                        if (aplicacion.isLogged())
                            getLogMenuItem().setText(
                                    AppContext.getApplicationContext().getI18nString(
                                            "LogoutOption"));
                        else
                            getLogMenuItem().setText(
                                    AppContext.getApplicationContext().getI18nString(
                                            "LoginOption"));

                        getSessionMenu().show(loginLabel, e.getX(), e.getY());
                    }
                }
            });
        this.addComponentListener(new java.awt.event.ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    this_componentShown(e);
                }
            });
        this.getContentPane().setLayout(borderLayout1);
        this.addWindowListener(new java.awt.event.WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    this_windowClosing(e);
                }
            });
        this.setJMenuBar(menuBar);

        // This size is chosen so that when the user hits the Info tool, the
        // window
        // fits between the lower edge of the TaskFrame and the lower edge of
        // the
        // WorkbenchFrame. See the call to #setSize in InfoFrame. [Jon Aquino]
        setSize(900, 665);

        // OUTLINE_DRAG_MODE is excruciatingly slow in JDK 1.4.1, so don't use
        // it.
        // (although it's supposed to be fixed in 1.4.2, which has not yet been
        // released). (see Sun Java Bug ID 4665237). [Jon Aquino]
        // desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        messageLabel.setOpaque(true);
        memoryLabel.setText("jLabel1"); //$NON-NLS-1$
        wmsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        wmsLabel.setText(" "); //$NON-NLS-1$
        this.getContentPane().add(statusPanel, BorderLayout.SOUTH);
        exitMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    exitMenuItem_actionPerformed(e);
                }
            });
        windowMenu.addMenuListener(new javax.swing.event.MenuListener()
            {
                public void menuCanceled(MenuEvent e)
                {
                }

                public void menuDeselected(MenuEvent e)
                {
                }

                public void menuSelected(MenuEvent e)
                {
                    windowMenu_menuSelected(e);
                }
            });
        coordinateLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));// .setBorder(BorderFactory.createLoweredBevelBorder());
        wmsLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));// .setBorder(BorderFactory.createLoweredBevelBorder());
        coordinateLabel.setText(" "); //$NON-NLS-1$
        statusPanel.setLayout(gridBagLayout1);
        // statusPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        messageLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));// setBorder(BorderFactory.createLoweredBevelBorder());
        messageLabel.setText(" "); //$NON-NLS-1$
        timeLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));// setBorder(BorderFactory.createLoweredBevelBorder());
        timeLabel.setText(" "); //$NON-NLS-1$
        memoryLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));// setBorder(BorderFactory.createLoweredBevelBorder());
        memoryLabel.setText(" "); //$NON-NLS-1$
        loginLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));
        connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.SoftBevelBorder.LOWERED));

        menuBar.add(windowMenu);

        getContentPane().add(dockingPanel, BorderLayout.NORTH);
        dockingPanel.add(toolBar);
        dockingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        getContentPane().add(desktopPane, BorderLayout.CENTER);

        // dockingPanel.setLayout(new BorderLayout());
        // dockingPanel.add(toolBar,BorderLayout.NORTH);
        //	    
        // getContentPane().add(dockingPanel, BorderLayout.CENTER);
        // dockingPanel.add(desktopPane, BorderLayout.CENTER);

        statusPanel.add(messageLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));
        statusPanel.add(timeLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));
        // Give memoryLabel the 1.0 weight. All the rest should have their
        // sizes
        // configured using #configureStatusLabel. [Jon Aquino]
        statusPanel.add(memoryLabel, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));
        statusPanel.add(wmsLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
                0, 0));
        statusPanel.add(coordinateLabel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                        0, 0), 0, 0));

        statusPanel.add(connectionLabel, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));
        statusPanel.add(loginLabel, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));
        
        
        //Gestion de conexiones y desconexiones contra el administrador
  		//de cartografia.
  		status=new ConnectionStatus(this,false);
  		status.init();	
  		aplicacion.getBlackboard().put(UserPreferenceConstants.CONNECT_STATUS,status);
        
        
    }

    private void position(JInternalFrame internalFrame)
    {
        final int STEP = 5;
        GUIUtil.Location location = null;
        if (internalFrame instanceof PrimaryInfoFrame)
        {
            primaryInfoFrameIndex++;

            int offset = (primaryInfoFrameIndex % 3) * STEP;
            location = new GUIUtil.Location(offset, true, offset, true);
        } else
        {
            positionIndex++;

            int offset = (positionIndex % 5) * STEP;
            location = new GUIUtil.Location(offset, false, offset, false);
        }
        GUIUtil.setLocation((Component) internalFrame, location, desktopPane);
        
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getChoosableStyleClasses()
	 */
    
	public Set getChoosableStyleClasses()
    {
        return Collections.unmodifiableSet(choosableStyleClasses);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addChoosableStyleClass(java.lang.Class)
	 */
    
	public void addChoosableStyleClass(Class choosableStyleClass)
    {
        Assert.isTrue(ChoosableStyle.class.isAssignableFrom(choosableStyleClass));
        choosableStyleClasses.add(choosableStyleClass);
    }

    private HashMap keyCodeAndModifiersToPlugInAndEnableCheckMap = new HashMap();

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addKeyboardShortcut(int, int, com.vividsolutions.jump.workbench.plugin.PlugIn, com.vividsolutions.jump.workbench.plugin.EnableCheck)
	 */
    
	public void addKeyboardShortcut(final int keyCode, final int modifiers,
            final PlugIn plugIn, final EnableCheck enableCheck)
    {
        // Overwrite existing shortcut [Jon Aquino]
        keyCodeAndModifiersToPlugInAndEnableCheckMap.put(keyCode + ":" //$NON-NLS-1$
                + modifiers, new Object[] { plugIn, enableCheck });
    }

    private void installKeyboardShortcutListener()
    {
        addEasyKeyListener(new KeyListener()
            {
                public void keyTyped(KeyEvent e)
                {
                }

                public void keyReleased(KeyEvent e)
                {
                }

                public void keyPressed(KeyEvent e)
                {
                    Object[] plugInAndEnableCheck = (Object[]) keyCodeAndModifiersToPlugInAndEnableCheckMap
                            .get(e.getKeyCode() + ":" + e.getModifiers()); //$NON-NLS-1$
                    if (plugInAndEnableCheck == null)
                    {
                        return;
                    }
                    PlugIn plugIn = (PlugIn) plugInAndEnableCheck[0];
                    EnableCheck enableCheck = (EnableCheck) plugInAndEnableCheck[1];
                    if (enableCheck != null && enableCheck.check(null) != null)
                    {
                        return;
                    }
                    // #toActionListener handles checking if the plugIn is a
                    // ThreadedPlugIn,
                    // and making calls to UndoableEditReceiver if necessary.
                    // [Jon
                    // Aquino 10/15/2003]
                    AbstractPlugIn.toActionListener(plugIn, workbenchContext,
                            new TaskMonitorManager()).actionPerformed(null);
                }
            });
    }

    private boolean confirmClose(String action, Collection modifiedLayers)
    {
        if (modifiedLayers.isEmpty())
        {
            return true;
        }
        MessageFormat fm = new MessageFormat(AppContext.getApplicationContext()
                .getI18nString("GeopistaWorkbenchFrame.MessageFormatCloseConfirmation")); //$NON-NLS-1$

        Object[] params = {
                new Integer(modifiedLayers.size()),
                StringUtil.toCommaDelimitedString(new ArrayList(modifiedLayers).subList(
                        0, Math.min(3, modifiedLayers.size()))) };
        String mensaje = fm.format(params, new StringBuffer(), null).toString();
        JOptionPane pane = new JOptionPane(StringUtil.split(mensaje, 80));
        pane.setOptions(new String[] { action,
                aplicacion.getI18nString("GeopistaWorkbenchFrame.Cancel") }); //$NON-NLS-1$
        pane
                .createDialog(this,
                        aplicacion.getI18nString("GeopistaWorkbenchFrame.Geopista")).setVisible(true); //$NON-NLS-1$

        return pane.getValue().equals(action);
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#setCloseAll(boolean)
	 */
    
	public void setCloseAll(boolean closeAllBoolean)
    {
        this.closeAllBoolean = closeAllBoolean;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getCloseAll()
	 */
    
	public boolean getCloseAll()
    {
        return this.closeAllBoolean;
    }

    // public WorkbenchToolBar addNewToolbar(String toolBarName)
    // {
    // WorkbenchToolBar toolBar = new WorkbenchToolBar(workbenchContext);
    // // Añade un contenedor para posibilitar su enganche adecuado.
    // JPanel nuevopanel=new JPanel(new BorderLayout());
    // dockingPanel.remove(desktopPane);
    //     
    // nuevopanel.add(desktopPane,BorderLayout.CENTER);
    // dockingPanel.add(nuevopanel, BorderLayout.CENTER);
    // dockingPanel=nuevopanel;
    // nuevopanel.add(toolBar, BorderLayout.NORTH);
    // toolBarMap.put(toolBarName,toolBar);
    // return toolBar;
    // }
    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#addNewToolbar(java.lang.String)
	 */
	public WorkbenchToolBar addNewToolbar(String toolBarName)
    {
        WorkbenchToolBar toolBar = new WorkbenchToolBar(workbenchContext);
        toolBar.setTaskMonitorManager(new TaskMonitorManager());
        dockingPanel.add(toolBar);
        toolBarMap.put(toolBarName, toolBar);
        return toolBar;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getToolBar(java.lang.String)
	 */
    
	public WorkbenchToolBar getToolBar(String toolBarName)
    {
        WorkbenchToolBar actualToolBar = (WorkbenchToolBar) toolBarMap.get(toolBarName);
        if (actualToolBar == null)
        {
            actualToolBar = addNewToolbar(toolBarName);
        }
        return actualToolBar;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#updateEnabledStateNewToolBars()
	 */
    
	public void updateEnabledStateNewToolBars()
    {

        Iterator toolBarMapIter = toolBarMap.keySet().iterator();
        while (toolBarMapIter.hasNext())
        {
            WorkbenchToolBar actualToolbar = (WorkbenchToolBar) toolBarMap
                    .get(toolBarMapIter.next());
            actualToolbar.updateEnabledState();
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#reClickSelectedCursorToolButtonNewToolBar()
	 */
    
	public void reClickSelectedCursorToolButtonNewToolBar()
    {
        Iterator toolBarMapIter = toolBarMap.keySet().iterator();
        while (toolBarMapIter.hasNext())
        {
            WorkbenchToolBar actualToolbar = (WorkbenchToolBar) toolBarMap
                    .get(toolBarMapIter.next());
            actualToolbar.reClickSelectedCursorToolButton();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.editor.WorkbenchGuiComponent#getMainFrame()
     */
    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getMainFrame()
	 */
    
	public JFrame getMainFrame()
    {
        return this;
    }

    /**
     * This method initializes sessionMenu
     * 
     * @return javax.swing.JPopupMenu
     */
    private JPopupMenu getSessionMenu()
    {
        if (sessionMenu == null)
        {
            sessionMenu = new JPopupMenu();
            sessionMenu.add(getLogMenuItem());
            sessionMenu.add(getOfflineMenuItem()); // Generated
        }
        return sessionMenu;
    }

    /**
     * This method initializes logMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getLogMenuItem()
    {
        if (logMenuItem == null)
        {
            logMenuItem = new JMenuItem();
            logMenuItem.setEnabled(aplicacion.isOnline());
            logMenuItem.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        if (aplicacion.isLogged())
                            aplicacion.logout();
                        else
                            aplicacion.login();
                    }
                });
        }
        return logMenuItem;
    }

    /**
     * This method initializes offlineMenuItem
     * 
     * @return javax.swing.JCheckBoxMenuItem
     */
    private JCheckBoxMenuItem getOfflineMenuItem()
    {
        if (offlineMenuItem == null)
        {
            offlineMenuItem = new JCheckBoxMenuItem();
            offlineMenuItem.setText(aplicacion.getI18nString("Offline_Mode")); // Generated
            offlineMenuItem.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        if (aplicacion.isOfflineMode())
                        {
                            aplicacion.setOfflineMode(false);
                            offlineMenuItem.setSelected(false);
                            getLogMenuItem().setEnabled(true);
                        } else
                        {
                            aplicacion.setOfflineMode(true);
                            offlineMenuItem.setSelected(true);
                            getLogMenuItem().setEnabled(false);
                        }
                    }
                });
        }
        return offlineMenuItem;
    }

    private void unlockLostFeatures() throws LockException
    {

        LockManager lockManager = (LockManager) aplicacion.getBlackboard().get(
                LockManager.LOCK_MANAGER_KEY);

        if (lockManager != null)
        {

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), workbenchContext.getErrorHandler());
 
            final UnlockAllFeaturesThreat monitorThreat = new UnlockAllFeaturesThreat(
                    progressDialog);

            if (lockManager.existLockedFeatures())
            {

                progressDialog.setTitle(aplicacion.getI18nString("UnlockFeatures"));
                progressDialog.addComponentListener(new ComponentAdapter()
                    {
                        public void componentShown(ComponentEvent e)
                        {
                            // Wait for the dialog to appear before
                            // starting the task. Otherwise
                            // the task might possibly finish before the
                            // dialog appeared and the
                            // dialog would never close. [Jon Aquino]
                            monitorThreat.start();
                        }
                    });
                GUIUtil.centreOnWindow(progressDialog);
                progressDialog.setVisible(true);

            }

            if (monitorThreat.confirmResultError == 1)
                throw new LockException(aplicacion.getI18nString("SalidaCancelada"));
        }
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getInternalTaskComponents()
	 */
    
	public TaskComponent[] getInternalTaskComponents()
    {
        JInternalFrame[] internalFrames = desktopPane.getAllFrames();
        TaskComponent[] taskComponents = new TaskComponent[internalFrames.length];
        for(int n=0; n<internalFrames.length; n++)
        {
            taskComponents[n] = (TaskComponent) internalFrames[n];
        }
        return  taskComponents;
    }

    /* (non-Javadoc)
	 * @see com.geopista.ui.IGeopistaWorkbenchFrame#getLayerViewPopupMenu()
	 */
    
	public JPopupMenu getLayerViewPopupMenu()
    {
        // TODO Auto-generated method stub
        return popupMenu;
    }

	 // Metodos que implementan la interfaz ISecurityPolicy

		@Override
		public void setPolicy() throws AclNotFoundException, Exception {		
		}
		@Override
		public void resetSecurityPolicy() {	
		}
		
		@Override
		public ApplicationContext getAplicacion() {
			return aplicacion;
		}
		@Override
		public String getIdApp() {
			return "Geopista";
		}
		@Override
		public String getIdMunicipio() {
			return String.valueOf(Constantes.idEntidad);
		}
		@Override
		public String getLogin() {
			return Constantes.url;
		}
		@Override
		public JFrame getFrame() {
			return this;
		}
	
}