  /*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * MainFrame.java
 *
 * Created on 5 febbraio 2003, 12.45
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.CompatibilitySupport;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.DocumentTreeEntry;
import it.businesslogic.ireport.FontListLoader;
import it.businesslogic.ireport.IReportCompiler;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.ImageReportElement;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.PluginEntry;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportClassLoader;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.ReportElementType;
import it.businesslogic.ireport.ReportReader;
import it.businesslogic.ireport.ReportWriter;
import it.businesslogic.ireport.TextReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.UndoOperation;
import it.businesslogic.ireport.compiler.xml.SourceTraceDigester;
import it.businesslogic.ireport.connection.JDBCConnection;
import it.businesslogic.ireport.crosstab.gui.CrosstabEditorPanel;
import it.businesslogic.ireport.crosstab.gui.CrosstabMeasuresView;
import it.businesslogic.ireport.gui.box.JBoxButton;
import it.businesslogic.ireport.gui.command.FormatCommand;
import it.businesslogic.ireport.gui.docking.GenericDragTargetListener;
import it.businesslogic.ireport.gui.documentstructure.CrosstabStructurePanel;
import it.businesslogic.ireport.gui.documentstructure.DocumentStructurePanel;
import it.businesslogic.ireport.gui.event.FontsListChangedEvent;
import it.businesslogic.ireport.gui.event.InternalFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.InternalFrameActivatedListener;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportListener;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.event.StyleChangedEvent;
import it.businesslogic.ireport.gui.event.StyleChangedListener;
import it.businesslogic.ireport.gui.event.TemplateChangedEvent;
import it.businesslogic.ireport.gui.event.ValueChangedEvent;
import it.businesslogic.ireport.gui.event.ValueChangedListener;
import it.businesslogic.ireport.gui.fonts.FontLoaderDialog;
import it.businesslogic.ireport.gui.fonts.FontsPathDialog;
import it.businesslogic.ireport.gui.library.LibraryPanel;
import it.businesslogic.ireport.gui.locale.LocaleResourceFilesDialog;
import it.businesslogic.ireport.gui.locale.TimeZoneWrapper;
import it.businesslogic.ireport.gui.logpane.LogPane;
import it.businesslogic.ireport.gui.queryexecuters.QueryExecuterDef;
import it.businesslogic.ireport.gui.queryexecuters.QueryExecutersDialog;
import it.businesslogic.ireport.gui.sheet.DocumentSheetPanel;
import it.businesslogic.ireport.gui.style.StylesDialog;
import it.businesslogic.ireport.gui.style.StylesView;
import it.businesslogic.ireport.gui.subdataset.SubDatasetsDialog;
import it.businesslogic.ireport.gui.wizard.ReportGroupWizard;
import it.businesslogic.ireport.gui.wizard.UserChoicesWizardTemplate;
import it.businesslogic.ireport.gui.wizard.UserTemplatesDialog;
import it.businesslogic.ireport.rmi.IReportServerImpl;
import it.businesslogic.ireport.undo.RemoveMarginsOperation;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.I18nOptionPane;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import jcmdline.BooleanParam;
import jcmdline.CmdLineHandler;
import jcmdline.FileParam;
import jcmdline.HelpCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.PosixCmdLineParser;
import jcmdline.StringParam;
import jcmdline.VersionCmdLineHandler;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.xerces.parsers.DOMParser;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.plaf.PlafManager;
import org.flexdock.view.View;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.SendToServerFrame;
import com.geopista.app.reports.maps.MapImageFactory;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.reports.parameters.IReportParameterManager;
import com.geopista.util.config.UserPreferenceStore;
/**
 * This class is the core of the GUI of iReport. From this class we control all
 * events related to the open and close files, handling of properties files,
 * handling JMDIDestopPane, Menus....
 *
 * @author  Administrator
 */
public class MainFrame extends javax.swing.JFrame
        implements ReportListener, Runnable, LanguageChangedListener, StyleChangedListener, ReportDocumentStatusChangedListener
{

    ////////////////////////////
    
//iR20     GhostGlassPane    glassPane = null;
    
//iR20     public void installLayer()
//iR20     {
//iR20         glassPane = new GhostGlassPane();
//iR20         setGlassPane(glassPane);
//iR20         
//iR20     
//iR20         desktop.getContentPane().add( new IRLayerPane((JComponent)desktop.getContentPane(), glassPane), new Integer(230));
//iR20     }
    
    
    
    /////////////////////////////
	
	AppContext aplicacion;
    
    public int docCounter=0;

    private boolean noExit = false;

    private Thread checkModifiedFielsThread = null;
    /**
     *   "iReport " + version
     *   To get the version, MainFrame.constTitle.substring(8);
     *   To show the title of a window, use the rebranded title
     *   with getRebrandedTitle();
     */
    public static String constTitle = "iReport 2.0.1 ";
    
    public static String getRebrandedTitle()
    {
        String s = getBrandingProperties().getProperty("ireport.title");
        if (s == null)
        {
            s = constTitle;
        }
        
        
        String newVersion = getBrandingProperties().getProperty("ireport.version");
        
        java.text.MessageFormat mf = null;
        if (newVersion.indexOf("{0}") >=0)
        {
            mf = new java.text.MessageFormat(newVersion, I18n.getCurrentLocale());
            newVersion = mf.format(new Object[]{constTitle.substring(8)} );
            getBrandingProperties().setProperty("ireport.version", newVersion);
        }
        
        mf = new java.text.MessageFormat(s, I18n.getCurrentLocale());         
        return mf.format(new Object[]{
            getBrandingProperties().getProperty("ireport.version"),
            constTitle,
            getBrandingProperties().getProperty("ireport.name")
        });
    }
    
    private String homeDirectory = ".";
    private java.util.Vector toolBarControls;
    private java.util.HashMap pluginEntries;
    private ElementPropertiesDialog elementPropertiesDialog;
    private EventsForm eventsForm;
    private ValuesDialog valuesDialog;
    private GroupsDialog groupsDialog;
    private BandsDialog bandsDialog;
    private FontsDialog fontsDialog;
    private LocaleResourceFilesDialog localeFilesDialog;
    private ReportQueryDialog reportQueryDialog;
    private PluginConfigurationDialog pluginConfigurationDialog = null;
    private ToolbarFormatPanel jToolbarFormatPanel = null;
    private ToolbarConnectionPanel jToolbarConnectionPanel = null;

    private Vector ttfFonts;
    private Vector connections;
    private Properties properties;
    private Vector recentFilesList;
    private Vector favoriteFilesList;
    private Vector recentProjectsList;
    private Vector clipboards[];
    private Vector classpath;
    private Vector fontspath;

    private List userChoicesWizardTemplates = null;

    private it.businesslogic.ireport.ReportElement styleClipboards[];
    private int activeClipboard = 0;
    private int activeStyleClipboard = 0;
    private boolean catchFormActivated = true;
    private boolean embeddedIreport = false;
    private boolean usingWS = false;

    private LibraryPanel libraryPanel = null;
    private CrosstabMeasuresView crosstabMeasuresView = null;

    private DocumentStructurePanel documentStructurePanel = null;
    private CrosstabStructurePanel crosstabStructurePanel = null;
    private DocumentSheetPanel reportSheetPanel = null;
    //private ReportElementSheetPanel reportElementSheetPanel = null;
    private StylesView stylesView = null;


    public static ReportClassLoader reportClassLoader = null;

    private static MainFrame mainInstance =  null;

    private boolean dontHandleEvent;

    private StringBuffer outputBuffer;

    private javax.swing.event.EventListenerList listenerList =  null;

    private int rightColumnWidth = 0;

    private Vector chartSeriesClipBoard = new Vector();
    private it.businesslogic.ireport.chart.Dataset chartDatasetClipBoard = null;
    private Vector measuresClipBoard = new Vector();
    private Vector queryExecuters = new Vector();

    private boolean magnetEnabled = true;
    
    private static java.util.Properties brandingProperties = new java.util.Properties();
    
    static {
        
        loadRebrandingProperties();
    }


    /**
     * Variable currentZoomFactor is used to remember the zoomFactor
     * while reloading a file after changing it from outside or
     * via the option Edit XML source.
     */
    private double currentZoomFactor = 1.0;

    public static String IREPORT_HOME_DIR;

    public static String IREPORT_USER_HOME_DIR;

    public static String IREPORT_TMP_DIR;

    public static String IREPORT_RECENT_FILES_FILE;

    public static String IREPORT_EXPRESSIONS_FILE;

    public static String IREPORT_FAVORITE_FILES_FILE;

    public static String IREPORT_RECENT_PROJECTS_FILE;

    public static String IREPORT_CONFIG_FILE;

    public static String IREPORT_STYLE_LIBRARY;

    public static String IREPORT_DEFAULT_HOME_DIR;

    public static final int IREPORT_JAVA_VIEWER = 1;

    public static final int IREPORT_PDF_VIEWER = 2;

    public static final int IREPORT_HTML_VIEWER = 4;

    public static final int IREPORT_XLS_VIEWER = 8;

    public static final int IREPORT_CSV_VIEWER = 16;

    public static final int IREPORT_JASPER_VIEWER = 32;

    public static final int IREPORT_TXT_VIEWER = 64;

    public static final int IREPORT_TXT_JR_VIEWER = 128;

    public static final int IREPORT_RTF_VIEWER = 256;

    public static final int IREPORT_XLS2_VIEWER = 512;
    
    public static final int IREPORT_ODF_VIEWER = 1024;

    private LogPane logPane = null;
    private JBoxButton jBoxButton = null;
    
    private java.util.List connectionImplementations = new java.util.ArrayList();



    /**
     * Viewes ....
     */
    View viewFiles = null;
    View viewDocumentStructure = null;
    View viewPropertySheet = null;
    View viewThreads = null;
    View viewLibrary = null;
    View logPaneView = null;
    View stylesPanleView = null;
    View crosstabStructureView = null;
    View crosstabMeasuresPanelView = null;


    View desktop = null;

    /**
     * This is the set of style availables in the library...
     */
    private Vector styleLibrarySet = new Vector();

    /**
     * Return the main instance of the MainFrame. This method assume that there is only
     * one iReport instanced per JVM, so the first istance of MainFrame is the only possible.
     */
    public static MainFrame getMainInstance() {
        return mainInstance;
    }

    static {

        reportClassLoader = new ReportClassLoader();

    } //end static section for simple init
        /*
         *  You can override ireport home setting ireport.home variable or using line command options.
         *
         */
    public void setiReportPaths(String ireport_home, String ireport_user_home, String ireport_tmp_dir) {

        // Setting User home directory
        if ( ireport_user_home == null) {
            ireport_user_home =  System.getProperty("user.home") + File.separator + ".ireport";
            IREPORT_USER_HOME_DIR = ireport_user_home;

            //setHomeDirectory(Misc.nvl(System.getProperty("ireport.home"), IREPORT_DEFAULT_HOME_DIR));
        }
        else {
            if (ireport_user_home.endsWith(File.separator)) {
                ireport_user_home = ireport_user_home.substring(0, ireport_user_home.length()-1);
            }
            IREPORT_USER_HOME_DIR = ireport_user_home;
        }

        // Setting iReport directory
        if ( ireport_home == null) {
            ireport_home =  System.getProperty("ireport.home",".");
            IREPORT_HOME_DIR = ireport_home;
            setHomeDirectory(IREPORT_HOME_DIR);
        }
        else {
            if (ireport_home.endsWith(File.separator)) {
                ireport_home = ireport_home.substring(0, ireport_home.length()-1);
            }
            IREPORT_HOME_DIR = ireport_home;
        }

        // Setting temp dir...
        if (IREPORT_TMP_DIR == null || IREPORT_TMP_DIR.trim().length() == 0) {
            if ( ireport_tmp_dir == null) {
                ireport_user_home =  System.getProperty("user.home") + File.separator + ".ireport";
                IREPORT_TMP_DIR = ireport_home;

                //setHomeDirectory(Misc.nvl(System.getProperty("ireport.home"), IREPORT_DEFAULT_HOME_DIR));
            }
            else {
                if (ireport_tmp_dir.endsWith(File.separator)) {
                    ireport_tmp_dir = ireport_tmp_dir.substring(0, ireport_tmp_dir.length()-1);
                }
                IREPORT_TMP_DIR = ireport_tmp_dir;
            }
        }

        IREPORT_DEFAULT_HOME_DIR = IREPORT_HOME_DIR;
        setHomeDirectory(IREPORT_HOME_DIR);

        IREPORT_RECENT_FILES_FILE = IREPORT_USER_HOME_DIR + File.separator + "recentFiles.xml";
        IREPORT_EXPRESSIONS_FILE = IREPORT_USER_HOME_DIR + File.separator + "expressions.xml";
        IREPORT_RECENT_PROJECTS_FILE = IREPORT_USER_HOME_DIR + File.separator + "recentProjects.xml";
        IREPORT_FAVORITE_FILES_FILE = IREPORT_USER_HOME_DIR + File.separator + "favoriteFiles.xml";
        IREPORT_CONFIG_FILE = IREPORT_USER_HOME_DIR + File.separator + "config.xml";
        IREPORT_STYLE_LIBRARY = IREPORT_USER_HOME_DIR + File.separator + "styleLibrary.xml";
        //System.out.println(IREPORT_DEFAULT_HOME_DIR);
        //System.out.println(IREPORT_HOME_DIR);

        getReportClassLoader().addNoRelodablePath( IREPORT_USER_HOME_DIR + File.separator + "classes");

        try {
            //nothing in here yet.
        }
        catch(Throwable e) {
            e.printStackTrace(System.err);
        }
    }

    public MainFrame() {
        this(new HashMap());
    }
    /** Creates new form MainFrame */
    public MainFrame(final Map args) {
    	
        if (mainInstance==null) {
            mainInstance = this;
        }

        // Inicializo el contexto de GeoPista
        aplicacion = (AppContext) AppContext.getApplicationContext();
        
        // loading rebranding stuff...
        
        
        //Thread.currentThread().setContextClassLoader(this.getReportClassLoader());

        final  SplashDialog sp;

        if (args.get("splash") != null)
        {
         sp = (SplashDialog)args.get("splash");
        }
        else
        {
            sp = null;
        }

        properties = new Properties();
        outputBuffer = new StringBuffer();
        connections = new Vector();
        recentFilesList = new Vector();
        favoriteFilesList = new Vector();
        recentProjectsList = new Vector();
        pluginEntries = new java.util.HashMap();

        setiReportPaths((String)args.get("ireport-home"),
        (String)args.get("user-home"),
        (String)args.get("tmp-dir"));

        if (args.get("embedded") != null &&
            args.get("embedded").equals("true")) this.setNoExit(true);

        if (args.get("webstart") != null &&
            args.get("webstart").equals("true")) this.setUsingWS(true);

        //Added by Felix Firgau for I18n on Feb 10th 2006
        //it.businesslogic.ireport.util.I18n.setCurrentLocale( properties.getProperty("Language"), properties.getProperty("Country") );
        //Modified by Felix Firgau for I18n on Feb 8th 2006

        // Set the classloader...
        //Thread.currentThread().setContextClassLoader( reportClassLoader );
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(10,"Loading iReport configuration");
        if (sp!=null) sp.updateLoadingStatus(10,it.businesslogic.ireport.util.I18n.getString("loadingIReportConfig","Loading iReport configuration"));
        //Modification end

        addDefaultConnectionImplementations();
        
        if (args.containsKey("config-file")) {
            loadiReportConfiguration((String)args.get("config-file"));
        }
        else {
            loadiReportConfiguration();
        }

        addGeopistaConnection();

        it.businesslogic.ireport.util.I18n.setCurrentLocale( properties.getProperty("Language"), properties.getProperty("Country") );

        if (args.get("beanClass") != null)
        {
            getProperties().put("beanClass",(String)args.get("beanClass"));
        }

     
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(20,"Setting locale");
        if (sp!=null) sp.updateLoadingStatus(20,it.businesslogic.ireport.util.I18n.getString("setLocale","Setting locale"));
        //it.businesslogic.ireport.util.I18n.setCurrentLocale( properties.getProperty("Language"), properties.getProperty("Country") );
        //Modification end

        //Misc.setPLAF( properties.getProperty("LookAndFeel") );

        // Modificacion para Geopista
        //Modified by Felix Firgau for faster loading on Oct 11th 2006
        if (args.get("noPlaf") == null || args.get("noPlaf").equals("false")){
        	//PlafManager.setPreferredTheme("win32");
        }
        // Fin de modificacion para Geopista
        
//        if (args.get("noPlaf") == null || args.get("noPlaf").equals("false")) {
//          Misc.setPLAF(properties.getProperty("LookAndFeel"));
//          PlafManager.setPreferredTheme("win32");
//        }
        

        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(30,"Init main frame components");
        if (sp!=null) sp.updateLoadingStatus(30,it.businesslogic.ireport.util.I18n.getString("initMainFrame","Init main frame components"));
        //Modification end

        jToolbarFormatPanel = new ToolbarFormatPanel(this);
        jToolbarConnectionPanel = new ToolbarConnectionPanel(this);
        
        initComponents();
        
        this.setTitle(this.getRebrandedTitle());
        //getDockingContainerRight().setPosition( DockingContainer.POSITION_RIGHT);
        logPane = new LogPane();
        //jPanelSouth.add(logPane, java.awt.BorderLayout.CENTER);

        this.jCheckBoxMenuItemGrid.setSelected( properties.getProperty("showGrid","false").equals("true") );
        this.jCheckBoxMenuItemSnapToGrid.setSelected( properties.getProperty("snapToGrid","false").equals("true") );
        this.jCheckBoxMenuItemEMM.setSelected( properties.getProperty("EMM","false").equals("true") );
        this.jCheckBoxMenuItemReportVirtualizer.setSelected( properties.getProperty("ReportVirtualizer","false").equals("true") );
        this.jCheckBoxMenuItemIgnorePagination.setSelected( properties.getProperty("IgnorePagination","false").equals("true") );
        this.jListThreads.setModel(new javax.swing.DefaultListModel());
        this.jListThreads.setPreferredSize(new Dimension(10,10));
        I18n.addOnLanguageChangedListener( this );

        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(60,"Loading fonts");

        if (properties.getProperty("LoadFontOnStartup","true").equals("true") )
        {
            if (sp!=null) sp.updateLoadingStatus(60,it.businesslogic.ireport.util.I18n.getString("loadingFonts","Loading fonts"));
            //Modification end
            this.ttfFonts = FontListLoader.loadTTFFonts();
        }
        if (ttfFonts == null) ttfFonts = new Vector();

        System.setProperty("org.xml.sax.driver","org.apache.xerces.parsers.SAXParser");

        //System.out.println("Configurando componentes GUI");

        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(75,"Configuring GUI components");
        if (sp!=null) sp.updateLoadingStatus(70,it.businesslogic.ireport.util.I18n.getString("configGUIComponents","Configuring GUI components"));

        //Modification end of Felix Firgau

        //jMDIDesktopPane.addInternalFrameActivatedListener(this);

        // Modificacion para Geopista
        this.setTitle( getRebrandedTitle());
        // Fin de modificacion

        toolBarControls = new java.util.Vector();

        elementPropertiesDialog = new ElementPropertiesDialog(this,false);
        elementPropertiesDialog.updateFonts();
        valuesDialog = new ValuesDialog(this,false);
        groupsDialog = new GroupsDialog(this,false);
        bandsDialog = new BandsDialog(this,false);
        fontsDialog = new FontsDialog(this,false);
        reportQueryDialog = new ReportQueryDialog(this,false);
        localeFilesDialog = new LocaleResourceFilesDialog(this,false);
        eventsForm = new EventsForm();

        jNumberComboBoxZoom.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                jNumberComboBoxZoomValueChanged(evt);
            }
        });

        //user pressed enter
        jNumberComboBoxZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //simulate focus lost to force zoom update
                jNumberComboBoxZoom.focusLost(new FocusEvent(MainFrame.this, FocusEvent.COMPONENT_SHOWN));
            }
        });

        jMDIDesktopPane.addInternalFrameActivatedListener(new InternalFrameActivatedListener() {
            public void internalFrameActivated( InternalFrameActivatedEvent evt) {
                jMDIDesktopPaneInternalFrameActivated(evt);
            }
        });

        getToolBarControls().addElement( this.jButtonNew);
        getToolBarControls().addElement( this.jButtonOpen);
        getToolBarControls().addElement( this.jButtonSave);
        getToolBarControls().addElement( null);
        getToolBarControls().addElement( this.jButtonCut);
        getToolBarControls().addElement( this.jButtonCopy);
        getToolBarControls().addElement( this.jButtonPaste);
        getToolBarControls().addElement( null);
        getToolBarControls().addElement( this.jToggleButtonPointer);
        getToolBarControls().addElement( this.jToggleButtonLineTool);
        getToolBarControls().addElement( this.jToggleButtonRectTool);
        //getToolBarControls().addElement( this.jToggleButtonRectRoundTool);
        getToolBarControls().addElement( this.jToggleButtonEllipseTool);
        getToolBarControls().addElement( this.jToggleButtonImageTool);
        getToolBarControls().addElement( this.jToggleButtonStaticTextTool);
        getToolBarControls().addElement( this.jToggleButtonTextFieldTool);
        getToolBarControls().addElement( this.jToggleButtonFrameTool);
        getToolBarControls().addElement( this.jToggleButtonSubreportTool);
        getToolBarControls().addElement( this.jToggleButtonCrosstabTool);
        getToolBarControls().addElement( this.jToggleButtonChartTool);
        getToolBarControls().addElement( this.jToggleButtonBarcodeTool);
        getToolBarControls().addElement( null);
        getToolBarControls().addElement( this.jButtonParameters);
        getToolBarControls().addElement( this.jButtonGroups);
        getToolBarControls().addElement( this.jButtonBands);
        getToolBarControls().addElement( this.jButtonDatabase);
        getToolBarControls().addElement( null);
        getToolBarControls().addElement( this.jPanelZoom);
        getToolBarControls().addElement( this.jButtonLens1);
        getToolBarControls().addElement( this.jButtonLens2);
        getToolBarControls().addElement( null);
        getToolBarControls().addElement( this.jButtonCompiler);

        getToolBarControls().addElement(this.jButtonRun1);
        getToolBarControls().addElement(this.jButtonRun2);
        getToolBarControls().addElement( null);
        //toolBarControls.addElement(this.jButtonAlignLeft);
        //toolBarControls.addElement(this.jButtonAlignCenter);
        //toolBarControls.addElement(this.jButtonAlignRight);
        //toolBarControls.addElement(this.jButtonAlignJustify);

        jBoxButton = new JBoxButton();
        jBoxButton.setMinimumSize(new java.awt.Dimension(36, 22));
        jBoxButton.setMaximumSize(new java.awt.Dimension(36, 22));
        jBoxButton.setPreferredSize(new java.awt.Dimension(36, 22));
        jBoxButton.setEnabled(false);
        jBoxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBoxButtonActionPerformed(evt);
            }
        });

        jToolBarText.add(jBoxButton, jToolBarText.getComponentCount()-1);

        jNumberComboBoxZoom.addEntry("50%",50);
        jNumberComboBoxZoom.addEntry("75%",75);
        jNumberComboBoxZoom.addEntry("100%",100);
        jNumberComboBoxZoom.addEntry("150%",150);
        jNumberComboBoxZoom.addEntry("200%",200);
        jNumberComboBoxZoom.addEntry("400%",400);
        jNumberComboBoxZoom.addEntry("800%",800);
        jNumberComboBoxZoom.addEntry("Width",100);
        jNumberComboBoxZoom.addEntry("Height",100);
        jNumberComboBoxZoom.addEntry("Whole page",100);
        //jNumberComboBoxZoom.addEntry("Fit to page",100); // deactivate since July 16 2004

        //java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //this.setSize(dim);

        //System.out.println("Cargando tipos de fuentes");
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //if (sp!=null) sp.updateLoadingStatus(80,"Loading available font family names");
        if (sp!=null) sp.updateLoadingStatus(75,it.businesslogic.ireport.util.I18n.getString("loadingAvFontFamNames","Loading available font family names"));
        //Modification end

        String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        //System.out.println("Numero de fonts disponibles:"+fontFamilies.length);
        jComboBoxFont.addItem("");
        for (int i=0; i<fontFamilies.length; ++i)
        {
            jComboBoxFont.addItem(fontFamilies[i]);
            if (fontFamilies[i].equalsIgnoreCase("SansSerif"))
            {
                jComboBoxFont.setSelectedIndex(i);
            }
        }

        jNumberComboBoxSize.addEntry("3",3);
        jNumberComboBoxSize.addEntry("5",5);
        jNumberComboBoxSize.addEntry("8",8);
        jNumberComboBoxSize.addEntry("10",10);
        jNumberComboBoxSize.addEntry("12",12);
        jNumberComboBoxSize.addEntry("14",14);
        jNumberComboBoxSize.addEntry("18",18);
        jNumberComboBoxSize.addEntry("24",24);
        jNumberComboBoxSize.addEntry("36",36);
        jNumberComboBoxSize.addEntry("48",48);

        jNumberComboBoxSize.setSelectedIndex(5);

        //jPanelToolBarComponentResized(new java.awt.event.ComponentEvent(jPanelToolBar,0));

        //System.out.println("Loading Media Tracker");
        java.awt.Image def = Misc.loadImageFromResources("it/businesslogic/ireport/icons/layout/img.gif");
        try {
            java.awt.MediaTracker mt = new java.awt.MediaTracker( new javax.swing.JPanel() );
            mt.addImage(def,0);
            mt.waitForID(0);
        } catch (Exception ex) {
            /* Do nothing */
        }
        //System.out.println("Media Tracker Loaded");

        ImageReportElement.setImgDef(def);
        //jNumberComboBoxZoom.setPostfix("%");
        //Modified by Felix Firgau for I18n on Feb 9th 2006
        javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode(it.businesslogic.ireport.util.I18n.getString("openedFiles", "Opened files"));//End
        javax.swing.tree.DefaultTreeModel model = new javax.swing.tree.DefaultTreeModel(root);

        javax.swing.ToolTipManager.sharedInstance().registerComponent(jTreeFiles);
        jTreeFiles.setModel( model );
        jTreeFiles.setCellRenderer( new ProjectExplorerTreeCellRenderer());

        jTreeFiles.setDropTarget(new DropTarget(jTreeFiles, new GenericDragTargetListener()));
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //--- dockingContainerLeft.addPanel(it.businesslogic.ireport.util.I18n.getString("files", "Files"),jPanelFiles,true);//Modification end

        documentStructurePanel = new DocumentStructurePanel();
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //--- dockingContainerLeft.addPanel(it.businesslogic.ireport.util.I18n.getString("documentStructure", "Documentstructure"),documentStructurePanel,true);//Modification end
        this.addReportListener( documentStructurePanel );

        crosstabStructurePanel = new CrosstabStructurePanel();
        //--- jTabbedPaneExplorer.addTab("Document structure",crosstabStructurePanel);
        this.addReportListener( crosstabStructurePanel );

        reportSheetPanel = new DocumentSheetPanel();

        //dockingContainerRight.addPanel(it.businesslogic.ireport.util.I18n.getString("elementProperties","Element properties"),reportElementSheetPanel, true);

        libraryPanel = new LibraryPanel();
        //jTabbedPaneRight.addTab("Library",libraryPanel);
        //Modified by Felix Firgau for I18n on Feb 8th 2006
        //---  dockingContainerRight.addPanel(it.businesslogic.ireport.util.I18n.getString("library"),libraryPanel,true);//Modification end

        stylesView = new StylesView();

        //dockingContainerRight.insertPanel(1,it.businesslogic.ireport.util.I18n.getString("stylesLibrary","Styles Library"),stylesView, dockingContainerRight.INSERT_MODE_SHAREDPOSTION, true);

        crosstabMeasuresView = new CrosstabMeasuresView();


        // New docking code

        final org.flexdock.docking.defaults.DefaultDockingPort defaultDockingPort = new org.flexdock.docking.defaults.DefaultDockingPort();

        //System.out.println("Creating Desktop Page");

        //Modificado para que no diera la excepcion de la
        //eventQueue. Se introduce el metodo invokeLater.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {        
	        desktop = createDesktopPage();
	
	        //System.out.println("Desktop Page loaded");
	        
	        viewFiles = createView("Files",it.businesslogic.ireport.util.I18n.getString("files", "Files"), true, true, jPanelFiles);
	        viewDocumentStructure = createView("Structure",it.businesslogic.ireport.util.I18n.getString("documentStructure", "Document structure"), true, true, documentStructurePanel);
	        viewPropertySheet = createView("Props",it.businesslogic.ireport.util.I18n.getString("properties","Properties"), true, true, getReportSheetPanel());
	        jScrollProcesses.setSize(100,100);
	        viewThreads = createView("Threads",it.businesslogic.ireport.util.I18n.getString("threads","Threads"), true, true, jScrollProcesses);
	        viewLibrary = createView("Library",it.businesslogic.ireport.util.I18n.getString("library","Library"), true, true, libraryPanel);
	        logPaneView = createView("logPane",it.businesslogic.ireport.util.I18n.getString("output","Output"), true, true, logPane);
	         stylesPanleView = createView("Styles",it.businesslogic.ireport.util.I18n.getString("stylesLibrary","Styles Library"), true, true, stylesView);
	        crosstabMeasuresPanelView = createView("crosstabObjects",it.businesslogic.ireport.util.I18n.getString("crosstabObjects","Crosstab objects"), false, true, crosstabMeasuresView);
	        crosstabStructureView = createView("crosstabStructure",it.businesslogic.ireport.util.I18n.getString("crosstabStructure","Crosstab structure"), false, true, crosstabStructurePanel);
	
	        jPanelMaster.add(defaultDockingPort, BorderLayout.CENTER);
	
	        //System.out.println("Updating UI");
	
	        jPanelMaster.updateUI();
	
	        //System.out.println("UI updated");
	
	        // ------------- ALL TASK THAT DOES NOT NEED TO BE IN THE EDT
	
	        //if (sp!=null) sp.updateLoadingStatus(80,"Loading styles"); // I18N Done on 6th April 06
	        if (sp!=null) sp.updateLoadingStatus(80,it.businesslogic.ireport.util.I18n.getString("loadingStyles", "Loading styles"));
	        loadStyleLibrary();
	        stylesView.loadLibraryStyles();
	
	
	        //System.out.println("Loading file list");
	
	        //if (sp!=null) sp.updateLoadingStatus(85,"Loading file lists"); // I18N Done on 6th April 06
	
	        if (sp!=null) sp.updateLoadingStatus(85, it.businesslogic.ireport.util.I18n.getString("loadingFileLists","Loading Loading file lists"));
	
	        loadFileLists();
	        loadExpressionsList();
	
	        updateRecentFileMenu(jMenuRecentFiles, getRecentFilesList() );
	        //updateRecentProjectMenu(jMenuRecentProjects, getRecentProjectsList());
	
	        //IRCompilerDocument doc = new IRCompilerDocument();
	
	        jMenuItemUndo.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemRedo.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
	        jCheckBoxMenuItemEMM.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemCut.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemCopy.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemPaste.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
	
	        jMenuItemSave.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemOpen.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemNewDocument.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
	        jMenuItemPrint.setAccelerator( javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
	
	        jToolbarConnectionPanel.updateConnections();
	
	        clipboards = new Vector[5];
	        for (int i=0; i<5; ++i) {
	            clipboards[i] = new Vector();
	        }
	
	        styleClipboards = new it.businesslogic.ireport.ReportElement[5];
	        for (int i=0; i<5; ++i) {
	            styleClipboards[i] = null;
	        }
	
	        //System.out.println("Generanting Report Viewer");
	
	        MainFrame.this.setReportViewer(MainFrame.this.getReportViewer());
	        MainFrame.this.disableSaveAllSpecific();
	        MainFrame.this.disableSaveSpecific();
	
	        String irHomeMessageKey = "messages.ireportHome";
	        String irHomeMessageDefault =  "iReport home (ireport.home system property): {0}";
	        if (args.containsKey("ireport-home"))
	        {
	            irHomeMessageKey = "messages.ireportHome2";
	            irHomeMessageDefault = "iReport home (from command line): {0}";
	        }
	
	
	        getLogPane().getMainLogTextArea().logOnConsole(
	                I18n.getFormattedString(irHomeMessageKey, irHomeMessageDefault, new Object[]{ "" + MainFrame.this.IREPORT_HOME_DIR} ),
	                JOptionPane.INFORMATION_MESSAGE);
	
	        irHomeMessageKey = "messages.userHome";
	        irHomeMessageDefault =  "User home (user.home system property): {0}";
	        if (args.containsKey("ireport-home"))
	        {
	            irHomeMessageKey = "messages.userHome2";
	            irHomeMessageDefault = "User home (from command line): {0}";
	        }
	
	        getLogPane().getMainLogTextArea().logOnConsole(
	                I18n.getFormattedString(irHomeMessageKey, irHomeMessageDefault, new Object[]{ "" + MainFrame.this.IREPORT_USER_HOME_DIR} ),
	                JOptionPane.INFORMATION_MESSAGE);
	        
	        getLogPane().getMainLogTextArea().logOnConsole(
	                I18n.getFormattedString("messages.compileDir", "iReport default compile directory: {0}", new Object[]{ "" + MainFrame.this.getDefaultCompilationDirectory()} ),
	                JOptionPane.INFORMATION_MESSAGE);
	        
	        getLogPane().getMainLogTextArea().logOnConsole(
	                I18n.getFormattedString("messages.userHome3",
	                    "iReport user home directory (iReport compiles here if there are no settings or the user hasn\'t selected to compile to the report directory): {0}",
	                    new Object[]{ IREPORT_DEFAULT_HOME_DIR} ),
	                JOptionPane.INFORMATION_MESSAGE);
	        
	        if (isUsingWS())
	        {
	                getLogPane().getMainLogTextArea().logOnConsole(
	                I18n.getString("messages.jwsactive", "JWS mode is active"),
	                JOptionPane.INFORMATION_MESSAGE);
	        }
	
	        jToolBarText.setVisible( getProperties().getProperty("toolbarTextVisible","false").equalsIgnoreCase("true") );
	        jToolBarFormat.setVisible( getProperties().getProperty("toolbarFormattingVisible","false").equalsIgnoreCase("true") );
	        jToolBarConnections.setVisible( getProperties().getProperty("toolbarConnectionsVisible","true").equalsIgnoreCase("true") );
	
	        jCheckBoxMenuItemElementsFormatting.setSelected( getProperties().getProperty("toolbarFormattingVisible","false").equalsIgnoreCase("true") );
	        jCheckBoxMenuItemTextFormatting.setSelected( getProperties().getProperty("toolbarTextVisible","false").equalsIgnoreCase("true") );

            }});

        try {



             SwingUtilities.invokeLater(new Runnable() {
                 public void run() {

                	//System.out.println("Invoking Later");
                    jPanelMaster.updateUI();
                    defaultDockingPort.updateUI();
                	//System.out.println("Docking");
                    DockingManager.dock(desktop, (DockingPort)defaultDockingPort);

                    boolean createLayout = true;

                    try {
                        if (getProperties().getProperty("RestoreLayout","true").equals("true") && DockingManager.loadLayoutModel(false))
                        {
                            createLayout = false;
                            DockingManager.registerDockable((Dockable)logPaneView);
                            DockingManager.registerDockable((Dockable)viewThreads);
                            DockingManager.registerDockable((Dockable)viewPropertySheet);
                            DockingManager.registerDockable((Dockable)viewDocumentStructure);
                            DockingManager.registerDockable((Dockable)viewLibrary);
                            DockingManager.registerDockable((Dockable)stylesPanleView);

                        }
                    } catch (Exception ex)
                    {

                    }

                    if (createLayout)
                    {
                    	System.out.println("Creating Layout");

                    //org.flexdock.docking.DockingManager.dock(desktop, (org.flexdock.docking.DockingPort)defaultDockingPort);


                    DockingManager.dock((Dockable)logPaneView, (Dockable)desktop, DockingConstants.SOUTH_REGION, 0.3f);
                    //desktop.dock(logPaneView, DockingConstants.SOUTH_REGION); //, .01f);
                    //org.flexdock.docking.DockingManager.setSplitProportion(desktop.getComponent(), 0.2f);


                    DockingManager.dock((Dockable)viewThreads, (Dockable)logPaneView, DockingConstants.EAST_REGION, 0.2f);
                    //logPaneView.dock(viewThreads, DockingConstants.EAST_REGION, 0.8f);
                    //org.flexdock.docking.DockingManager.setSplitProportion(viewThreads.getComponent(), 0.8f);

                    DockingManager.dock((Dockable)viewPropertySheet, (Dockable)desktop, DockingConstants.EAST_REGION, 0.25f);
                    //desktop.dock(viewPropertySheet, DockingConstants.EAST_REGION);
                    //org.flexdock.docking.DockingManager.setSplitProportion(viewPropertySheet.getComponent(), 0.75f);

                    DockingManager.dock((Dockable)viewFiles, (Dockable)desktop, DockingConstants.WEST_REGION, 0.25f);
                    //desktop.dock(viewFiles, DockingConstants.WEST_REGION);
                    //org.flexdock.docking.DockingManager.setSplitProportion(viewFiles.getComponent(), 0.1f);

                    DockingManager.dock((Dockable)viewDocumentStructure, (Dockable)viewFiles, DockingConstants.SOUTH_REGION, 0.5f);
                    //viewFiles.dock(viewDocumentStructure, DockingConstants.SOUTH_REGION);
                    //org.flexdock.docking.DockingManager.setSplitProportion(viewDocumentStructure.getComponent(), 0.5f);

                    DockingManager.dock((Dockable)viewLibrary, (Dockable)viewPropertySheet, DockingConstants.SOUTH_REGION, 0.5f);
                    //viewPropertySheet.dock(viewLibrary, DockingConstants.SOUTH_REGION);
                    //org.flexdock.docking.DockingManager.setSplitProportion(viewLibrary.getComponent(), 0.5f);
                    //defaultDockingPort.doLayout();

                    DockingManager.dock((Dockable)stylesPanleView, (Dockable)viewLibrary, DockingConstants.CENTER_REGION);
                    //viewLibrary.dock(stylesPanleView, DockingConstants.CENTER_REGION);
                    }

                    DockingManager.setAutoPersist(true);

                    viewLibrary.setActive(true);
                    viewFiles.setActive(true);

                    // -----------
                    //System.out.println("Loading Plugins");
                    //Modified by Felix Firgau for I18n on Feb 8th 2006
                    //if (sp!=null) sp.updateLoadingStatus(90,"Loading plugins");
                    if (sp!=null) sp.updateLoadingStatus(90,it.businesslogic.ireport.util.I18n.getString("loadingPlugIns", "Loading plugins"));
                    //Modification end

                    loadPlugins(IREPORT_HOME_DIR + File.separator + "plugins"  );


                    //it.businesslogic.ireport.util.I18n.setCurrentLocale( properties.getProperty("Language"), properties.getProperty("Country") );

                    //System.out.println("Loading Files");

                    //Modified by Felix Firgau for I18n on Feb 8th 2006
                    //if (sp!=null) sp.updateLoadingStatus(95,"Opening files");
                    if (sp!=null) sp.updateLoadingStatus(95,it.businesslogic.ireport.util.I18n.getString("openingFiles","Opening files"));
                    //Modification end

                    JReportFrame jrf = null;
                    if (args.containsKey("files")) {
                        Iterator iter = ((Collection)args.get("files")).iterator();
                        while (iter.hasNext()) {
                            try {
                                File f = (File)iter.next();
                                //Modified by Felix Firgau for I18n on Feb 8th 2006
                                //if (sp!=null) sp.updateLoadingStatus(98,"Opening " + f.getName());
                                if (sp!=null) sp.updateLoadingStatus(98,it.businesslogic.ireport.util.I18n.getString("opening","Opening ") + f.getName());
                                //Modification end

                                Report report = new Report(f.getPath());
                                jrf = openNewReportWindow( report );

                            } catch (Exception ex) { }
                        }
                    }


                    //jSplitPaneHelp.setDividerSize(6);
                    //if (rightColumnWidth > 0) jSplitPaneHelp.setDividerLocation(jSplitPaneHelp.getWidth()-8-200);
                    //dockingContainerRight.setVisible(true);
                    //dockingContainerRight.setPreferredDividerLocation(jSplitPaneHelp.getDividerLocation());



                    try {
                        if (jrf != null)
                        {
                            jrf.setSelected(true);
                            setActiveReportForm(jrf);
                        }
                        else
                        {
                            setActiveReportForm(null);
                        }
                    } catch (Exception ex)
                    {
                    }

                    if (MainFrame.getMainInstance().getProperties().getProperty( "enableRMIServer" ,"false").equals("true") ||
                       (args.get("embedded") != null && args.get("embedded").equals("true")))
                    {
                        if (sp!=null) sp.updateLoadingStatus(99,it.businesslogic.ireport.util.I18n.getString("runningRMIServer","Running RMI server... "));
                        IReportServerImpl.runServer();
                        //IReportTCPServer.runServer();
                    }
                    setCrosstabActive(null);
                    it.businesslogic.ireport.util.I18n.setCurrentLocale( properties.getProperty("Language"), properties.getProperty("Country") );

                    
//iR20                     installLayer();
                    if (sp!=null) sp.setVisible(false);

                    // Load previous docking state
        //try {
        //    DockingManager.loadLayoutModel();
        //    DockingManager.restoreLayout();
        //} catch(Exception ex) {
        //    ex.printStackTrace();
        //}



            }}); // End later invocation
        //} catch (InvocationTargetException ex) {
        //    ex.printStackTrace();
        //} catch (InterruptedException ex) {
        //    ex.printStackTrace();
        //} // End later invocation
            
            

         } catch (Exception ex) {
             ex.printStackTrace();
         }

        // Other stuffs....
        net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory.xmla-mdx",
                    "net.sf.jasperreports.engine.query.JRXmlaQueryExecuterFactory");

                    
       this.getRootPane().updateUI();
        
    }

    /**
     * Retrieve the first unused mane like untitled_report_XXX
     */
    public String getFirstNameFree() {
        JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();

        for (int k=1; ; k++) {
            String name = it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_") + k;//I18N on April 28th 2006
            boolean found = false;
            for (int i=0; i<frames.length ; ++i) {
                if (frames[i] instanceof JReportFrame) {
                    JReportFrame jrf = (JReportFrame)frames[i];
                    if (jrf.getReport().getName().equalsIgnoreCase(name)) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return name;
            }
        }
    }


    public void setFileListActivatedFrame(JReportFrame jrf) {
        // remove the frame from the documnets list....
        DefaultTreeModel dtm = (DefaultTreeModel)jTreeFiles.getModel();
        Enumeration opened_entries = ((DefaultMutableTreeNode)dtm.getRoot()).children();
        while (opened_entries.hasMoreElements()) {
            DefaultMutableTreeNode documentEntryNode = (DefaultMutableTreeNode)opened_entries.nextElement();
            if (documentEntryNode.getUserObject() instanceof DocumentTreeEntry) {
                DocumentTreeEntry dte = (DocumentTreeEntry)documentEntryNode.getUserObject();
                if (dte.getJrf() == jrf) {
                    jTreeFiles.setSelectionPath(new TreePath(new Object[]
                    {dtm.getRoot(), documentEntryNode}));
                    jTreeFiles.updateUI();
                    break;
                }
            }

        }
    }

    public void updateMenuWindowList() {
        // Remove all menus...
        java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
        int i = 0;
        for (int k = 0;  k<menus.length ; ++k) {
            if (menus[k] instanceof JRadioButtonMenuItemMDIFrame)
            {
                this.jMenuWindow.remove((JMenuItem)menus[k]);
                buttonGroupFramesList.remove((JMenuItem)menus[k]);
            }
            /*&&
            ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() != null &&
            ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame().isValid() &&
            ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame().isVisible()) {
                JRadioButtonMenuItemMDIFrame menuItem = (JRadioButtonMenuItemMDIFrame)menus[k];
                menuItem.setText( (i+1) + ".*** " + menuItem.getFrame().getTitle() );
                menuItem.setMnemonic((int)(((i+1)+"").charAt(0)) );
                menuItem.setFont(new java.awt.Font("Tahoma", 0, 11));
                menuItem.setAccelerator( javax.swing.KeyStroke.getKeyStroke((int)(((i+1)+"").charAt(0)),java.awt.Event.META_MASK));
                menuItem.updateUI();
                i++;

                System.out.println("Menu "+ menuItem.getFrame().getTitle());
            }
            */
        }

        i=0;
        JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int k=0; k<frames.length; ++k)
        {
                JRadioButtonMenuItemMDIFrame menuItem = new JRadioButtonMenuItemMDIFrame((JMDIFrame)frames[k],frames[k].getTitle());
                menuItem.setText( (i+1) + ". " + menuItem.getFrame().getTitle() );
                menuItem.setMnemonic((int)(((i+1)+"").charAt(0)) );
                menuItem.setAccelerator( javax.swing.KeyStroke.getKeyStroke((int)(((i+1)+"").charAt(0)),java.awt.Event.META_MASK));
                menuItem.updateUI();
                menuItem.setSelected(frames[i].isSelected());
                this.jMenuWindow.insert(menuItem,  this.jMenuWindow.getMenuComponentCount());
                menuItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jMenuWindowListActionPerformed(evt);
                        }
                    });
                buttonGroupFramesList.add(menuItem);
                i++;
        }

    }
    
    private void jMenuItemLocalGISLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPluginConfigActionPerformed
    	ReportsManager.getInstance().logout();
    }
    
    private boolean cancel;
    //private static File imagen = null;
    //private static String nameImage = null;
    
    private void jMenuItemLocalGISSendToServerActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
			if(jrf!=null){			
				Report report = jrf.getReport();
				Vector elements = report.getElements();
				ImageReportElement element = null;
				
				ArrayList listImagenes=new ArrayList();
				
				for(int i=0;i<elements.size();i++){
					try{
						element = (ImageReportElement) elements.get(i);
					}catch(Exception e){
						continue;
					}
					String imageExpression = element.getImageExpression();
					if (!(imageExpression.startsWith("$F{IMG_") || imageExpression.startsWith("IMG_"))){
						String nameImage = imageExpression.substring(imageExpression.lastIndexOf('\\') + 1, imageExpression.length()-1);
						JRField image = new JRField("IMG_"+nameImage, "java.lang.String");
						if(!report.getFields().contains(image))
							report.addField(image);
						element.setImageExpression("$F{IMG_"+nameImage+"}");
						/*File imagen = new File(imageExpression.substring(1, imageExpression.length()-1));
						this.imagen = imagen;
						this.nameImage = nameImage;*/
						listImagenes.add(imageExpression);
					}					
				}
				/*if(report.isModified()){
					saveAs(jrf);
					if(this.cancel) return;
				}*/
				/*conn = aplicacion.getConnection();*/

                if (!aplicacion.isLogged())
                {
                    aplicacion.setProfile("Geopista");
                    aplicacion.login();
                }
                
                
				if(aplicacion.isLogged()){
				//if (AppContext.getApplicationContext().isOnlyLogged()){ 
					if(aplicacion.getIdEntidad()!= -1){
				        String sUrl = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + ServletConstants.DOCUMENT_SERVLET_NAME;
				        SendToServerFrame stsf = new SendToServerFrame(sUrl,this,true);
				    	stsf.setModal(true);
				    	String reportName = report.getName();
				    	if (!reportName.toLowerCase().endsWith(".jrxml") && !reportName.toLowerCase().endsWith(".xml"))
				    		reportName = reportName + ".jrxml";
				        stsf.setReportName(reportName);
				        stsf.setVisible(true);

						File file = new java.io.File(report.getFilename());
						
				        stsf.executeSend(aplicacion, file, listImagenes);
				        //stsf.executeSend(aplicacion, report, listImagenes);
				      
				        /*
				        if (stsf.getDialogResult() == javax.swing.JOptionPane.OK_OPTION){
				        	String dir = "";String subdir = "";String path = "";
					    	dir = stsf.getDir();
					    	subdir = stsf.getSubdir();
					    	ArrayList files = stsf.getFiles();					    	
					    	if(dir.equals("")) 
					    		ErrorDialog.show(aplicacion.getMainFrame(), "Error enviando plantilla", 
					    				"No se ha seleccionado el destino", 
					    				"Debe seleccionar un directorio y/o un subdirectorio donde se guardará la plantilla");
					    	else if(subdir.equals("")) path = dir ;
					    	else path = dir + "/" + subdir;
					    	
					    	report.setName(stsf.getReportName());
					    	
					    	File file = new java.io.File(report.getFilename());
					    	
					    	if(files!=null && files.contains(report.getName())){
		                        int response = JOptionPane.showConfirmDialog(aplicacion.getMainFrame(),	
		                        		"Se sobreescribirá la plantilla del servidor", 
		                        		"Sobreescribir plantilla?", JOptionPane.YES_NO_OPTION);
		                        if (response != JOptionPane.YES_OPTION) return;
				            }
					    	DocumentClient.sendFile(file,path,report.getName());	
					    	
					    	Iterator it=listImagenes.iterator();
					    	while (it.hasNext()){
					    		String imageExpression=(String)it.next();
					    		File imagen = new File(imageExpression.substring(1, imageExpression.length()-1));
								String nameImage = imageExpression.substring(imageExpression.lastIndexOf('\\') + 1, imageExpression.length()-1);						   
					    		
					    		DocumentClient.sendFile(imagen, "eiel"+"/"+"img",nameImage);
						    	
					    	}
					    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Fichero enviado al servidor");
				        }	*/			    					                        
					}					
				}
			}
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Error al enviar el informe al servidor");
			e.printStackTrace();
		}
    }    
    
    

    public void jMenuWindowListActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() != null &&
        evt.getSource() instanceof JRadioButtonMenuItemMDIFrame) {
            JRadioButtonMenuItemMDIFrame mdm = (JRadioButtonMenuItemMDIFrame)evt.getSource();
            try {

                mdm.getFrame().setSelected(true);
            } catch (Exception ex)
            {}
        }

    }

    public void jMDIDesktopPaneInternalFrameActivated(InternalFrameActivatedEvent evt) {
        if (evt.getJMDIFrame() != null && evt.getAction() == InternalFrameActivatedEvent.CLOSED) {
            // remove the frame from the documnets list....
            DefaultTreeModel dtm = (DefaultTreeModel)jTreeFiles.getModel();
            Enumeration opened_entries = ((DefaultMutableTreeNode)dtm.getRoot()).children();
            while (opened_entries.hasMoreElements()) {
                DefaultMutableTreeNode documentEntryNode = (DefaultMutableTreeNode)opened_entries.nextElement();
                if (documentEntryNode.getUserObject() instanceof DocumentTreeEntry) {
                    DocumentTreeEntry dte = (DocumentTreeEntry)documentEntryNode.getUserObject();
                    if (dte.getJrf() == evt.getJMDIFrame()) {
                        ((DefaultMutableTreeNode)dtm.getRoot()).remove(documentEntryNode);
                        jTreeFiles.updateUI();

                        break;
                    }
                }

            }
            // Remove from window list...
            java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
            for (int k = menus.length-1;  k>=0 ; --k) {
                if (menus[k] instanceof JRadioButtonMenuItemMDIFrame &&
                ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() == evt.getJMDIFrame()) {
                    this.jMenuWindow.remove(menus[k]);
                }
            }
            updateMenuWindowList();
            return;
        }

        if (evt.getJMDIFrame() == null) {
            this.setTitle(getRebrandedTitle());
            setCrosstabActive( null);
            setActiveReportForm( null );
            updateMenuWindowList();
            return;
        }


        this.setTitle(getRebrandedTitle() + " ["+ evt.getJMDIFrame().getTitle() + "]");
        jMDIMenuBar.restoreButtonsPosition();
        //System.out.println("Value setted!");
        if (evt.getJMDIFrame() instanceof JReportFrame) {

            JReportFrame jrf = (JReportFrame)evt.getJMDIFrame();

            if (jrf.getSelectedCrosstabEditorPanel() != null)
            {
                this.jNumberComboBoxZoom.setValue(jrf.getSelectedCrosstabEditorPanel().getZoomFactor()*100);
            }
            else
            {
                this.jNumberComboBoxZoom.setValue(jrf.getZoomFactor()*100);
            }

            // Set the fit to page value...
            setFileListActivatedFrame(jrf);
            //updateFitToPage(jrf);

            // Enable all buttons and menus...
            setActiveReportForm( jrf );
            if (jrf.getSelectedCrosstabEditorPanel() != null)
            {
                setCrosstabActive(jrf.getSelectedCrosstabEditorPanel().getCrosstabElement());
            }
            else
            {
                setCrosstabActive( null);
            }
        }
        else {
            //System.out.println("Chiusetutte lengthfinestre!");
            setCrosstabActive( null);
            setActiveReportForm( null );
        }
    }

    public void renewTitle(JReportFrame jrf)
    {

        //String title = jrf.getReport().getName() + " "+
        //        jrf.getReport().getWidth()+"x"+jrf.getReport().getHeight()+" "+
        //        "(" + (jrf.getReport().getReportChanges()>0 ?"Modified" : "Unchanged") + ") " +
        //        jrf.getReport().getFilename()+(jrf.getReport().isReadOnly()?" (READ ONLY) ":"");

        // composition of title  WxH ( Modified | Unchanged ) [(READ ONLY)]
        String title = jrf.getReport().getName() + " "+
                jrf.getReport().getWidth()+"x"+jrf.getReport().getHeight()+" "+
                "(" + (jrf.getReport().getReportChanges()>0
                		? I18n.getString("mainFrame.report.modified","Modified")
                		: I18n.getString("mainFrame.report.unchanged","Unchanged")) + ") " +
                		//?"Modified"
                        //: "Unchanged") + ") " +
                jrf.getReport().getFilename()+(jrf.getReport().isReadOnly()
                		?" ("+ I18n.getString("mainFrame.report.readOnly","READ ONLY")+") "
                		//?" (READ ONLY) "
                		:"");

        this.setTitle(getRebrandedTitle() +
                " [" + title + "]");
    }

    public void setActiveReportForm(JReportFrame jrf)
    {
        boolean enabled = true;
        if (jrf == null)
        {
            enabled = false;
        }
        else
        {
            renewTitle(jrf);
        }
        if(jrf != null && jrf.getReport().isReadOnly())
        {
            enabled = false;
        }



        if (jrf != null)
        {
            java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
            for (int k = menus.length-1;  k>=0 ; --k)
            {
                if (menus[k] instanceof JRadioButtonMenuItemMDIFrame &&
                        ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() == jrf)
                {
                    ((JRadioButtonMenuItemMDIFrame)menus[k]).setSelected(true);
                }
            }

        }

        this.elementPropertiesDialog.setJReportFrame(jrf);
        this.valuesDialog.setJReportFrame(jrf);
        this.groupsDialog.setJReportFrame(jrf);
        this.bandsDialog.setJReportFrame(jrf);
        this.fontsDialog.setJReportFrame(jrf);
        this.eventsForm.setJReportFrame(jrf);


        if (jrf == null) this.reportQueryDialog.setVisible(false);
        else this.reportQueryDialog.setSubDataset(jrf.getReport() );

        this.libraryPanel.setJReportFrame(jrf);
        this.localeFilesDialog.setJReportFrame(jrf);
        //getDocumentStructurePanel().updateDocumentStructureTree( jrf);

        updateUndoMenu(jrf);
        updateCutAndPasteMenu(jrf);
        // Toolbar...
        this.jButtonBands.setEnabled(enabled);
        this.jButtonDatabase.setEnabled(enabled);
        this.jMenuItemReportQuery2.setEnabled(enabled);
        this.jButtonParameters.setEnabled(enabled);
        this.jButtonGroups.setEnabled(enabled);
        this.jButtonCompiler.setEnabled(enabled);
        this.jButtonRun1.setEnabled(enabled);
        this.jButtonRun2.setEnabled(enabled);

        boolean frameOpened = false;
        if (jrf != null) frameOpened = true;

        jMenuItemSave.setEnabled(enabled);
        jMenuItemSaveAs.setEnabled(frameOpened);
        jMenuAdd.setEnabled(enabled);
        jMenuItemClose.setEnabled(frameOpened);
        jMenuItemCloseAll.setEnabled(frameOpened);
        jMenuItemCloseAllExceptThis.setEnabled(frameOpened);
        jMenuItemCloseAllExceptThisFromList.setEnabled(frameOpened);
        jMenuItemCloseAllFromList.setEnabled(frameOpened);
        jMenuItemCloseFromList.setEnabled(frameOpened);
        jMenuItemRevertToSaved.setEnabled(enabled);
        jMenuItemCompile.setEnabled(frameOpened);
        jMenuItemExecute.setEnabled(frameOpened);
        jMenuItemExecuteDB.setEnabled(frameOpened);
        jMenuWindow.setEnabled(frameOpened);
        jButtonLens1.setEnabled(frameOpened);
        jButtonLens2.setEnabled(frameOpened);
        jNumberComboBoxZoom.setEnabled(frameOpened);
        jMenuItemPrint.setEnabled(frameOpened);


        // Enable or disable all element in toolbars...
        for (int cc = 0; cc < jToolBarText.getComponentCount(); ++cc)
        {
            java.awt.Component component = jToolBarText.getComponentAtIndex(cc);
            if (component instanceof javax.swing.JButton)
            {
                ((javax.swing.JButton)component).setEnabled( enabled );
            }
            else if (component instanceof javax.swing.JComboBox)
            {
                ((javax.swing.JComboBox)component).setEnabled( enabled );
            }
            else if (component instanceof JBoxButton)
            {
                ((JBoxButton)component).setEnabled( enabled );
            }
        }

        // Enable or disable all element in toolbars...
        for (int cc = 0; cc < jToolBarFormat.getComponentCount(); ++cc)
        {
            java.awt.Component component = jToolBarFormat.getComponentAtIndex(cc);
            if (component instanceof javax.swing.JButton)
            {
                ((javax.swing.JButton)component).setEnabled( enabled );
            }
            else if (component instanceof javax.swing.JComboBox)
            {
                ((javax.swing.JComboBox)component).setEnabled( enabled );
            }
            else if (component instanceof javax.swing.JToggleButton)
            {
                ((javax.swing.JToggleButton)component).setEnabled( enabled );
            }
        }
        /*
        this.jButtonAlignLeft.setEnabled(enabled);
        this.jButtonAlignCenter.setEnabled(enabled);
        this.jButtonAlignRight.setEnabled(enabled);
        this.jButtonAlignJustify.setEnabled(enabled);
         */

        this.jToggleButtonEllipseTool.setEnabled(enabled);
        this.jToggleButtonImageTool.setEnabled(enabled);
        this.jToggleButtonLineTool.setEnabled(enabled);
        // Mofificacion para geopista
        this.jToggleButtonMapImageTool.setEnabled(enabled);
        // Fin de modificacion para geopista

        this.jToggleButtonPointer.setEnabled(enabled);
       //this.jToggleButtonRectRoundTool.setEnabled(enabled);
        this.jToggleButtonRectTool.setEnabled(enabled);
        this.jButtonSave.setEnabled(enabled);
        this.jToggleButtonStaticTextTool.setEnabled(enabled);
        this.jToggleButtonFrameTool.setEnabled(enabled);
        this.jToggleButtonSubreportTool.setEnabled(enabled);
        this.jToggleButtonCrosstabTool.setEnabled(enabled);
        this.jToggleButtonChartTool.setEnabled(enabled);
        this.jToggleButtonBarcodeTool.setEnabled(enabled);
        this.jToggleButtonTextFieldTool.setEnabled(enabled);
        this.jMenuItemXMLSource.setEnabled(enabled);
        this.jCheckBoxMenuItemEMM.setEnabled(enabled);
        this.jCheckBoxMenuItemReportVirtualizer.setEnabled(enabled);
        this.jCheckBoxMenuItemIgnorePagination.setEnabled(enabled);
        this.jCheckBoxMenuItemGrid.setEnabled(enabled);
        this.jCheckBoxMenuItemSnapToGrid.setEnabled(enabled);
        this.jMenuAdd.setEnabled(enabled);

        this.jMenuItemInsertPageBreak.setEnabled(enabled);
        this.jMenuItemBands.setEnabled(enabled);
        this.jMenuItemGroups.setEnabled(enabled);
        this.jMenuItemReportProperties.setEnabled(enabled);
        this.jMenuItemProperties.setEnabled(enabled);
        this.jMenuItemElementProperties.setEnabled(enabled);
        this.jMenuItemFields.setEnabled(enabled);
        this.jMenuItemAddGroup.setEnabled(enabled);
        this.jMenuItemVariables.setEnabled(enabled);
        this.jMenuItemSubDataset.setEnabled(enabled);
        this.jMenuItemSubDataset1.setEnabled(enabled);
        this.jMenuItemParameters.setEnabled(enabled);
        this.jMenuItemReportQuery.setEnabled(enabled);
        this.jMenuItemReportQuery2.setEnabled(enabled);
        this.jMenuItemFonts.setEnabled(enabled);
        this.jMenuItemStyles.setEnabled(enabled);
        this.jMenuItemScriptletEditor.setEnabled(enabled);
        //this.jMenuItemReportSeries.setEnabled(enabled);
        this.jMenuItemReportImports.setEnabled(enabled);
        this.jMenuInternationalization.setEnabled(enabled);
        this.jMenuItemRemoveMargins.setEnabled(enabled);

        jMDIDesktopPane.setPreferredSize(new java.awt.Dimension(100,10));
        jMDIDesktopPane.setMinimumSize(new java.awt.Dimension(100,10));
        //jMDIDesktopPane.setPreferredSize(new Dimension(100,100));
        setActiveTool(this.jToggleButtonPointer);
        if (!enabled)
        {
            this.jToggleButtonPointer.setSelected(false);
        }
        if (jrf != null)
        {
            Report r = jrf.getReport();
            if( r.isModified() )
            {
                this.enableSaveSpecific();
                this.enableSaveAllSpecific();
                //end if report modified
            }
            else
            {
                this.disableSaveSpecific();
                if ( this.isSaveAllRequired() )
                {
                    this.enableSaveAllSpecific();
                }
                else
                {
                    this.disableSaveAllSpecific();
                }
                //end else report modified
            }
        }//end if jrf is null

        fireReportFrameActivatedListenerReportFrameActivated( new ReportFrameActivatedEvent(this, jrf) );

    }//end setActiveReportForm

    public boolean isEMMActive()
    {
        return this.jCheckBoxMenuItemEMM.isSelected();
    }

    public boolean isUseReportVirtualizer()
    {
        return this.jCheckBoxMenuItemReportVirtualizer.isSelected();
    }
    public boolean isIgnorePagination()
    {
        return this.jCheckBoxMenuItemIgnorePagination.isSelected();
    }

    public void setActiveTool( int tool )
    {
        if (tool == 0)
            setActiveTool(this.jToggleButtonPointer);
    }
    private void setActiveTool( java.awt.Component tool )
    {
        try
        {
            this.jToggleButtonEllipseTool.setSelected(  (tool ==this.jToggleButtonEllipseTool) );
            this.jToggleButtonImageTool.setSelected(  (tool ==this.jToggleButtonImageTool) );
            // Modificacion para geopista
            this.jToggleButtonMapImageTool.setSelected(  (tool ==this.jToggleButtonMapImageTool) );
            // Fin de modificacion para geopista
            this.jToggleButtonLineTool.setSelected(  (tool ==this.jToggleButtonLineTool) );
            this.jToggleButtonPointer.setSelected(  (tool ==this.jToggleButtonPointer) );
            //this.jToggleButtonRectRoundTool.setSelected(  (tool ==this.jToggleButtonRectRoundTool) );
            this.jToggleButtonRectTool.setSelected(  (tool ==this.jToggleButtonRectTool) );
            this.jToggleButtonStaticTextTool.setSelected(  (tool ==this.jToggleButtonStaticTextTool) );
            this.jToggleButtonFrameTool.setSelected(  (tool ==this.jToggleButtonFrameTool) );
            this.jToggleButtonSubreportTool.setSelected(  (tool ==this.jToggleButtonSubreportTool) );
            this.jToggleButtonCrosstabTool.setSelected(  (tool ==this.jToggleButtonCrosstabTool) );
            this.jToggleButtonChartTool.setSelected(  (tool ==this.jToggleButtonChartTool) );
            this.jToggleButtonBarcodeTool.setSelected(  (tool ==this.jToggleButtonBarcodeTool) );
            this.jToggleButtonTextFieldTool.setSelected(  (tool ==this.jToggleButtonTextFieldTool) );
        }
        catch (Exception ex)
        {
            /* Do nothing */
        }
    }

    // Robert says:
    // As Fit To Page is calculated every time is it called,
    // There is not need for this method
    // Giulio do you agree?

    public void updateFitToPage(JReportFrame jrf)
    {
        int w = jrf.getReport().getWidth();
        double s = (jrf.getReportPanel().getWidth()-20)/(double)w;
        jNumberComboBoxZoom.addEntry("Fit to page", s*100);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupExportType = new javax.swing.ButtonGroup();
        buttonGroupFramesList = new javax.swing.ButtonGroup();
        jPopupMenuThreads = new javax.swing.JPopupMenu();
        jMenuItemKill = new javax.swing.JMenuItem();
        jPopupMenuFiles = new javax.swing.JPopupMenu();
        jMenuItemSaveFromList = new javax.swing.JMenuItem();
        jMenuItemSaveAsFromList = new javax.swing.JMenuItem();
        jMenuItemSaveAllFromList = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JSeparator();
        jMenuItemCloseFromList = new javax.swing.JMenuItem();
        jMenuItemCloseAllFromList = new javax.swing.JMenuItem();
        jMenuItemCloseAllExceptThisFromList = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JSeparator();
        jMenuItemRevertToSavedFromList = new javax.swing.JMenuItem();
        jPanelFiles = new javax.swing.JPanel();
        jScrollPaneFiles = new javax.swing.JScrollPane();
        jTreeFiles = new javax.swing.JTree();
        jMDIDesktopPane = new it.businesslogic.ireport.gui.JMDIDesktopPane();
        jScrollProcesses = new javax.swing.JScrollPane();
        jListThreads = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jToolBarText = new javax.swing.JToolBar();
        jComboBoxFont = new javax.swing.JComboBox();
        jNumberComboBoxSize = new it.businesslogic.ireport.gui.JNumberComboBox();
        jButtonIncreaseSize = new javax.swing.JButton();
        jButtonDecreaseSize = new javax.swing.JButton();
        jButtonBold = new javax.swing.JButton();
        jButtonItalic = new javax.swing.JButton();
        jButtonUnderline = new javax.swing.JButton();
        jButtonStrikethrought = new javax.swing.JButton();
        jButtonAlignLeft = new javax.swing.JButton();
        jButtonAlignJustify = new javax.swing.JButton();
        jButtonAlignCenter = new javax.swing.JButton();
        jButtonAlignRight = new javax.swing.JButton();
        jButtonAlignTop = new javax.swing.JButton();
        jButtonAlignMiddle = new javax.swing.JButton();
        jButtonAlignBottom = new javax.swing.JButton();
        jToolBarConnections = jToolbarConnectionPanel.getToolBar();
        Filler1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jToolBarFormat = jToolbarFormatPanel.getToolBar();
        Filler = new javax.swing.JButton();
        jPanelMaster = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonNew = new javax.swing.JButton();
        jButtonWizard = new javax.swing.JButton();
        jButtonOpen = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAll = new javax.swing.JButton();
        jSeparator22 = new javax.swing.JSeparator();
        jButtonCut = new javax.swing.JButton();
        jButtonCopy = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jSeparator23 = new javax.swing.JSeparator();
        jToggleButtonPointer = new javax.swing.JToggleButton();
        jToggleButtonLineTool = new javax.swing.JToggleButton();
        jToggleButtonRectTool = new javax.swing.JToggleButton();
        jToggleButtonEllipseTool = new javax.swing.JToggleButton();
        jToggleButtonStaticTextTool = new javax.swing.JToggleButton();
        jToggleButtonTextFieldTool = new javax.swing.JToggleButton();
        jToggleButtonImageTool = new javax.swing.JToggleButton();
        jToggleButtonMapImageTool = new javax.swing.JToggleButton();
        jToggleButtonBarcodeTool = new javax.swing.JToggleButton();
        jToggleButtonFrameTool = new javax.swing.JToggleButton();
        jToggleButtonChartTool = new javax.swing.JToggleButton();
        jToggleButtonSubreportTool = new javax.swing.JToggleButton();
        jToggleButtonCrosstabTool = new javax.swing.JToggleButton();
        jSeparator30 = new javax.swing.JSeparator();
        jButtonParameters = new javax.swing.JButton();
        jButtonBands = new javax.swing.JButton();
        jButtonGroups = new javax.swing.JButton();
        jButtonDatabase = new javax.swing.JButton();
        jSeparator31 = new javax.swing.JSeparator();
        jPanelZoom = new javax.swing.JPanel();
        jNumberComboBoxZoom = new it.businesslogic.ireport.gui.JNumberComboBox();
        jButtonLens1 = new javax.swing.JButton();
        jButtonLens2 = new javax.swing.JButton();
        jSeparator32 = new javax.swing.JSeparator();
        jButtonCompiler = new javax.swing.JButton();
        jButtonRun1 = new javax.swing.JButton();
        jButtonRun2 = new javax.swing.JButton();
        jMDIMenuBar = new it.businesslogic.ireport.gui.JMDIMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNewDocument = new javax.swing.JMenuItem();
        jMenuItemWizard = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuItemSaveAll = new javax.swing.JMenuItem();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenuItemCloseAll = new javax.swing.JMenuItem();
        jMenuItemCloseAllExceptThis = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemPrint = new javax.swing.JMenuItem();
        jMenuItemRevertToSaved = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        jMenuRecentFiles = new javax.swing.JMenu();
        jSeparator15 = new javax.swing.JSeparator();
        jMenuItemQuit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemUndo = new javax.swing.JMenuItem();
        jMenuItemRedo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemInsertPageBreak = new javax.swing.JMenuItem();
        jMenuAdd = new javax.swing.JMenu();
        jMenuItemLine = new javax.swing.JMenuItem();
        jMenuItemRectangle = new javax.swing.JMenuItem();
        jMenuItemRoundRectangle = new javax.swing.JMenuItem();
        jMenuItemEllipse = new javax.swing.JMenuItem();
        jMenuItemImage = new javax.swing.JMenuItem();
        jMenuItemStaticText = new javax.swing.JMenuItem();
        jMenuItemTextField = new javax.swing.JMenuItem();
        jMenuItemChart = new javax.swing.JMenuItem();
        jMenuItemSubreport = new javax.swing.JMenuItem();
        jMenuItemAddGroup = new javax.swing.JMenuItem();
        jMenuItemElementProperties = new javax.swing.JMenuItem();
        jMenuItemXMLSource = new javax.swing.JMenuItem();
        jMenuInternationalization = new javax.swing.JMenu();
        jMenuLocaleFiles = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JSeparator();
        jMenuItemReportProperties = new javax.swing.JMenuItem();
        jMenuItemProperties = new javax.swing.JMenuItem();
        jMenuItemReportImports = new javax.swing.JMenuItem();
        jMenuItemScriptletEditor = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JSeparator();
        jMenuItemReportQuery = new javax.swing.JMenuItem();
        jMenuItemSubDataset = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JSeparator();
        jMenuItemRemoveMargins = new javax.swing.JMenuItem();
        jMenuView = new javax.swing.JMenu();
        jCheckBoxMenuItemGrid = new javax.swing.JCheckBoxMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuPanels = new javax.swing.JMenu();
        jCheckBoxMenuItemExplorer = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemDocumentStructure = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemElementProperties = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemLibrary = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemStylesLibrary = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemOutput = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemThreadList = new javax.swing.JCheckBoxMenuItem();
        jMenuToolBars = new javax.swing.JMenu();
        jCheckBoxMenuItemTextFormatting = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemElementsFormatting = new javax.swing.JCheckBoxMenuItem();
        jSeparator12 = new javax.swing.JSeparator();
        jMenuItemFields = new javax.swing.JMenuItem();
        jMenuItemVariables = new javax.swing.JMenuItem();
        jMenuItemParameters = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItemBands = new javax.swing.JMenuItem();
        jMenuItemGroups = new javax.swing.JMenuItem();
        jMenuFormat = new javax.swing.JMenu();
        jMenuItemStyles = new javax.swing.JMenuItem();
        jMenuItemFonts = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JSeparator();
        jMenuAlign = new javax.swing.JMenu();
        jMenuItemAlignLeft = new javax.swing.JMenuItem();
        jMenuItemAlignRight = new javax.swing.JMenuItem();
        jMenuItemAlignTop = new javax.swing.JMenuItem();
        jMenuItemAlignBottom = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JSeparator();
        jMenuItemAlignVerticalAxis = new javax.swing.JMenuItem();
        jMenuItemAlignHorizontalAxis = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JSeparator();
        jMenuItemAlignToBandTop = new javax.swing.JMenuItem();
        jMenuItemAlignToBandBottom = new javax.swing.JMenuItem();
        jMenuSize = new javax.swing.JMenu();
        jMenuItemSameWidth = new javax.swing.JMenuItem();
        jMenuItemSameWidthMax = new javax.swing.JMenuItem();
        jMenuItemSameWidthMin = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JSeparator();
        jMenuItemSameHeight = new javax.swing.JMenuItem();
        jMenuItemSameHeightMin = new javax.swing.JMenuItem();
        jMenuItemSameHeightMax = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JSeparator();
        jMenuItemSameSize = new javax.swing.JMenuItem();
        jMenuPosition = new javax.swing.JMenu();
        jMenuItemCenterH = new javax.swing.JMenuItem();
        jMenuItemCenterV = new javax.swing.JMenuItem();
        jMenuItemCenterInBand = new javax.swing.JMenuItem();
        jMenuItemCenterBackground = new javax.swing.JMenuItem();
        jMenuItemJoinLeft = new javax.swing.JMenuItem();
        jMenuItemJoinRight = new javax.swing.JMenuItem();
        jMenuItemLeftMargin = new javax.swing.JMenuItem();
        jMenuItemRightMargin = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuHSpacing = new javax.swing.JMenu();
        jMenuItemHSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemHSIncrease = new javax.swing.JMenuItem();
        jMenuItemHSDecrease = new javax.swing.JMenuItem();
        jMenuItemHSRemove = new javax.swing.JMenuItem();
        jMenuVSpacing = new javax.swing.JMenu();
        jMenuItemVSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemVSIncrease = new javax.swing.JMenuItem();
        jMenuItemVSDecrease = new javax.swing.JMenuItem();
        jMenuItemVSRemove = new javax.swing.JMenuItem();
        jMenuItemOrganize = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        jMenuItemBringToFront = new javax.swing.JMenuItem();
        jMenuItemSendToBack = new javax.swing.JMenuItem();
        jMenuDatabase = new javax.swing.JMenu();
        jMenuItemConnections = new javax.swing.JMenuItem();
        jMenuItemActiveConnection = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuItemReportQuery2 = new javax.swing.JMenuItem();
        jMenuItemSubDataset1 = new javax.swing.JMenuItem();
        jMenuBuild = new javax.swing.JMenu();
        jMenuItemCompile = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        jMenuItemExecute = new javax.swing.JMenuItem();
        jMenuItemExecuteDB = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JSeparator();
        jMenuItemCreateFromTemplate = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JSeparator();
        jRadioButtonMenuItemPreviewPDF = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewHTML = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewJAVA = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewXLS = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewXLS2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewCSV = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewTXT = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewTXTJR = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewRTF = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewODF = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemPreviewInternalViewer = new javax.swing.JRadioButtonMenuItem();
        jSeparator14 = new javax.swing.JSeparator();
        jMenuItemActiveConnection1 = new javax.swing.JMenuItem();
        jMenuItemReportLocale = new javax.swing.JMenuItem();
        jMenuItemReportTimeZone = new javax.swing.JMenuItem();
        jCheckBoxMenuItemIgnorePagination = new javax.swing.JCheckBoxMenuItem();
        jMenuItemMaxRecords = new javax.swing.JMenuItem();
        jCheckBoxMenuItemReportVirtualizer = new javax.swing.JCheckBoxMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jCheckBoxMenuItemSnapToGrid = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemEMM = new javax.swing.JCheckBoxMenuItem();
        jSeparator13 = new javax.swing.JSeparator();
        jMenuItemOptions = new javax.swing.JMenuItem();
        jMenuItemCompatibility = new javax.swing.JMenuItem();
        jMenuItemClasspath = new javax.swing.JMenuItem();
        jMenuItemFontspath = new javax.swing.JMenuItem();
        jMenuItemReloadFonts = new javax.swing.JMenuItem();
        jMenuItemExpressions = new javax.swing.JMenuItem();
        jMenuItemExportOptions = new javax.swing.JMenuItem();
        jMenuItemQueryExecuters = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        jMenuItemPluginConfig = new javax.swing.JMenuItem();
        jMenuPlugins = new javax.swing.JMenu();
        jMenuLocalGIS = new javax.swing.JMenu();

        jMenuWindow = new javax.swing.JMenu();
        jMenuItemCascade = new javax.swing.JMenuItem();
        jMenuItemTileHorizontal = new javax.swing.JMenuItem();
        jMenuItemTileVertical = new javax.swing.JMenuItem();
        jMenuItemtileAnodine = new javax.swing.JMenuItem();
        jMenuItemNextWin = new javax.swing.JMenuItem();
        jMenuItemPrevWin = new javax.swing.JMenuItem();
        jMenuItemRightSide = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JSeparator();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemHomePage = new javax.swing.JMenuItem();
        jMenuItemHelp = new javax.swing.JMenuItem();
        jMenuItemForum = new javax.swing.JMenuItem();
        jMenuItemAbout = new javax.swing.JMenuItem();
        jMenuItemLocalGISLogout = new javax.swing.JMenuItem();
        jMenuItemLocalGISSendToServer = new javax.swing.JMenuItem();

        jMenuItemKill.setText("Kill this thread");
        jMenuItemKill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemKillActionPerformed(evt);
            }
        });
        jPopupMenuThreads.add(jMenuItemKill);

        jMenuItemSaveFromList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/save.png"))); // NOI18N
        jMenuItemSaveFromList.setMnemonic('s');
        jMenuItemSaveFromList.setText("Save");
        jMenuItemSaveFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemSaveFromList);

        jMenuItemSaveAsFromList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/save.png"))); // NOI18N
        jMenuItemSaveAsFromList.setText("Save as...");
        jMenuItemSaveAsFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemSaveAsFromList);

        jMenuItemSaveAllFromList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/saveall.png"))); // NOI18N
        jMenuItemSaveAllFromList.setText("Save all");
        jMenuItemSaveAllFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAllFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemSaveAllFromList);
        jPopupMenuFiles.add(jSeparator24);

        jMenuItemCloseFromList.setText("Close");
        jMenuItemCloseFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemCloseFromList);

        jMenuItemCloseAllFromList.setText("Close all");
        jMenuItemCloseAllFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseAllFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemCloseAllFromList);

        jMenuItemCloseAllExceptThisFromList.setText("Close all except this");
        jMenuItemCloseAllExceptThisFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseAllExceptThisFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemCloseAllExceptThisFromList);
        jPopupMenuFiles.add(jSeparator27);

        jMenuItemRevertToSavedFromList.setText("Revert to saved");
        jMenuItemRevertToSavedFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRevertToSavedFromListActionPerformed(evt);
            }
        });
        jPopupMenuFiles.add(jMenuItemRevertToSavedFromList);

        jPanelFiles.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFiles.setLayout(new java.awt.BorderLayout());

        jScrollPaneFiles.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneFiles.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPaneFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jScrollPaneFilesMousePressed(evt);
            }
        });

        jTreeFiles.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeFilesValueChanged(evt);
            }
        });
        jTreeFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTreeFilesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTreeFilesMousePressed(evt);
            }
        });
        jScrollPaneFiles.setViewportView(jTreeFiles);

        jPanelFiles.add(jScrollPaneFiles, java.awt.BorderLayout.CENTER);

        jListThreads.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListThreads.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListThreadsMouseClicked(evt);
            }
        });
        jScrollProcesses.setViewportView(jListThreads);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(getIconImage());
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBarText.setMinimumSize(new java.awt.Dimension(26, 26));
        jToolBarText.setPreferredSize(new java.awt.Dimension(26, 26));

        jComboBoxFont.setFont(new java.awt.Font("Arial", 0, 11));
        jComboBoxFont.setEnabled(false);
        jComboBoxFont.setMaximumSize(new java.awt.Dimension(200, 32767));
        jComboBoxFont.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBoxFont.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFontItemStateChanged(evt);
            }
        });
        jToolBarText.add(jComboBoxFont);

        jNumberComboBoxSize.setEnabled(false);
        jNumberComboBoxSize.setMaximumSize(new java.awt.Dimension(60, 32767));
        jNumberComboBoxSize.setMinimumSize(new java.awt.Dimension(40, 22));
        jNumberComboBoxSize.setPreferredSize(new java.awt.Dimension(40, 22));
        jNumberComboBoxSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jNumberComboBoxSizeItemStateChanged(evt);
            }
        });
        jNumberComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberComboBoxSizeActionPerformed(evt);
            }
        });
        jToolBarText.add(jNumberComboBoxSize);

        jButtonIncreaseSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/increase_font_size.png"))); // NOI18N
        jButtonIncreaseSize.setBorder(null);
        jButtonIncreaseSize.setBorderPainted(false);
        jButtonIncreaseSize.setEnabled(false);
        jButtonIncreaseSize.setFocusPainted(false);
        jButtonIncreaseSize.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonIncreaseSize.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonIncreaseSize.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonIncreaseSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIncreaseSizeActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonIncreaseSize);

        jButtonDecreaseSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/decrease_font_size.png"))); // NOI18N
        jButtonDecreaseSize.setBorder(null);
        jButtonDecreaseSize.setBorderPainted(false);
        jButtonDecreaseSize.setEnabled(false);
        jButtonDecreaseSize.setFocusPainted(false);
        jButtonDecreaseSize.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonDecreaseSize.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonDecreaseSize.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonDecreaseSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecreaseSizeActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonDecreaseSize);

        jToolBarText.addSeparator();
        jButtonBold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/bold.png"))); // NOI18N
        jButtonBold.setBorder(null);
        jButtonBold.setBorderPainted(false);
        jButtonBold.setEnabled(false);
        jButtonBold.setFocusPainted(false);
        jButtonBold.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonBold.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonBold.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoldActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonBold);

        jButtonItalic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/italic.png"))); // NOI18N
        jButtonItalic.setBorder(null);
        jButtonItalic.setBorderPainted(false);
        jButtonItalic.setEnabled(false);
        jButtonItalic.setFocusPainted(false);
        jButtonItalic.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonItalic.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonItalic.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItalicActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonItalic);

        jButtonUnderline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/underline.png"))); // NOI18N
        jButtonUnderline.setBorder(null);
        jButtonUnderline.setBorderPainted(false);
        jButtonUnderline.setEnabled(false);
        jButtonUnderline.setFocusPainted(false);
        jButtonUnderline.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonUnderline.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonUnderline.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUnderlineActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonUnderline);

        jButtonStrikethrought.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/strikethrought.png"))); // NOI18N
        jButtonStrikethrought.setBorder(null);
        jButtonStrikethrought.setBorderPainted(false);
        jButtonStrikethrought.setEnabled(false);
        jButtonStrikethrought.setFocusPainted(false);
        jButtonStrikethrought.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonStrikethrought.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonStrikethrought.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonStrikethrought.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStrikethroughtActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonStrikethrought);

        jButtonAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_left.png"))); // NOI18N
        jButtonAlignLeft.setBorder(null);
        jButtonAlignLeft.setBorderPainted(false);
        jButtonAlignLeft.setEnabled(false);
        jButtonAlignLeft.setFocusPainted(false);
        jButtonAlignLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignLeft.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignLeft.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignLeftActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignLeft);

        jButtonAlignJustify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_justified.png"))); // NOI18N
        jButtonAlignJustify.setBorder(null);
        jButtonAlignJustify.setBorderPainted(false);
        jButtonAlignJustify.setEnabled(false);
        jButtonAlignJustify.setFocusPainted(false);
        jButtonAlignJustify.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignJustify.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignJustify.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignJustify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignJustifyActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignJustify);

        jButtonAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_center.png"))); // NOI18N
        jButtonAlignCenter.setBorder(null);
        jButtonAlignCenter.setBorderPainted(false);
        jButtonAlignCenter.setEnabled(false);
        jButtonAlignCenter.setFocusPainted(false);
        jButtonAlignCenter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignCenter.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignCenter.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignCenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignCenterActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignCenter);

        jButtonAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_right.png"))); // NOI18N
        jButtonAlignRight.setBorder(null);
        jButtonAlignRight.setBorderPainted(false);
        jButtonAlignRight.setEnabled(false);
        jButtonAlignRight.setFocusPainted(false);
        jButtonAlignRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignRight.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignRight.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignRightActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignRight);

        jToolBarText.addSeparator();
        jButtonAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_top.png"))); // NOI18N
        jButtonAlignTop.setBorder(null);
        jButtonAlignTop.setBorderPainted(false);
        jButtonAlignTop.setEnabled(false);
        jButtonAlignTop.setFocusPainted(false);
        jButtonAlignTop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignTop.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignTop.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignTopActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignTop);

        jButtonAlignMiddle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_middle.png"))); // NOI18N
        jButtonAlignMiddle.setBorder(null);
        jButtonAlignMiddle.setBorderPainted(false);
        jButtonAlignMiddle.setEnabled(false);
        jButtonAlignMiddle.setFocusPainted(false);
        jButtonAlignMiddle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignMiddle.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignMiddle.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignMiddleActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignMiddle);

        jButtonAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/text/align_bottom.png"))); // NOI18N
        jButtonAlignBottom.setBorder(null);
        jButtonAlignBottom.setBorderPainted(false);
        jButtonAlignBottom.setEnabled(false);
        jButtonAlignBottom.setFocusPainted(false);
        jButtonAlignBottom.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAlignBottom.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonAlignBottom.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonAlignBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlignBottomActionPerformed(evt);
            }
        });
        jToolBarText.add(jButtonAlignBottom);

        Filler1.setBackground(new java.awt.Color(255, 255, 255));
        Filler1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/null.gif"))); // NOI18N
        Filler1.setBorderPainted(false);
        Filler1.setContentAreaFilled(false);
        Filler1.setEnabled(false);
        Filler1.setFocusPainted(false);
        Filler1.setFocusable(false);
        Filler1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        Filler1.setMaximumSize(new java.awt.Dimension(0, 22));
        Filler1.setMinimumSize(new java.awt.Dimension(0, 22));
        Filler1.setPreferredSize(new java.awt.Dimension(0, 22));
        jToolBarConnections.add(Filler1);

        jToolBarText.add(jToolBarConnections);

        jPanel1.add(jToolBarText, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        Filler.setBackground(new java.awt.Color(255, 255, 255));
        Filler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/null.gif"))); // NOI18N
        Filler.setBorderPainted(false);
        Filler.setContentAreaFilled(false);
        Filler.setEnabled(false);
        Filler.setFocusPainted(false);
        Filler.setFocusable(false);
        Filler.setMargin(new java.awt.Insets(0, 0, 0, 0));
        Filler.setMaximumSize(new java.awt.Dimension(0, 22));
        Filler.setMinimumSize(new java.awt.Dimension(0, 22));
        Filler.setPreferredSize(new java.awt.Dimension(0, 22));
        jToolBarFormat.add(Filler);

        jPanel2.add(jToolBarFormat, java.awt.BorderLayout.NORTH);

        jPanelMaster.setMinimumSize(new java.awt.Dimension(750, 100));
        jPanelMaster.setPreferredSize(new java.awt.Dimension(750, 550));
        jPanelMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelMasterMouseClicked(evt);
            }
        });
        jPanelMaster.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jPanelMaster, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/new.png"))); // NOI18N
        jButtonNew.setToolTipText("New report");
        jButtonNew.setBorder(null);
        jButtonNew.setBorderPainted(false);
        jButtonNew.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonNew.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonNew.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonNew);

        jButtonWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/wizard.png"))); // NOI18N
        jButtonWizard.setToolTipText("New report");
        jButtonWizard.setBorder(null);
        jButtonWizard.setBorderPainted(false);
        jButtonWizard.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonWizard.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonWizard.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed1(evt);
            }
        });
        jToolBar1.add(jButtonWizard);

        jButtonOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/open.png"))); // NOI18N
        jButtonOpen.setToolTipText("Open report");
        jButtonOpen.setBorder(null);
        jButtonOpen.setBorderPainted(false);
        jButtonOpen.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonOpen.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonOpen.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonOpen);

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/save.png"))); // NOI18N
        jButtonSave.setToolTipText("Save report");
        jButtonSave.setBorder(null);
        jButtonSave.setBorderPainted(false);
        jButtonSave.setEnabled(false);
        jButtonSave.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonSave.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonSave.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonSave);

        jButtonSaveAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/saveall.png"))); // NOI18N
        jButtonSaveAll.setToolTipText("Save report");
        jButtonSaveAll.setBorder(null);
        jButtonSaveAll.setBorderPainted(false);
        jButtonSaveAll.setEnabled(false);
        jButtonSaveAll.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonSaveAll.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonSaveAll.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonSaveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed1(evt);
            }
        });
        jToolBar1.add(jButtonSaveAll);

        jSeparator22.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator22.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator22.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator22.setPreferredSize(new java.awt.Dimension(2, 20));
        jToolBar1.add(jSeparator22);

        jButtonCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/cut.png"))); // NOI18N
        jButtonCut.setToolTipText("Cut");
        jButtonCut.setBorder(null);
        jButtonCut.setBorderPainted(false);
        jButtonCut.setEnabled(false);
        jButtonCut.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonCut.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonCut.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCutActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCut);

        jButtonCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/copy.png"))); // NOI18N
        jButtonCopy.setToolTipText("Copy");
        jButtonCopy.setBorder(null);
        jButtonCopy.setBorderPainted(false);
        jButtonCopy.setEnabled(false);
        jButtonCopy.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonCopy.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonCopy.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCopy);

        jButtonPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/paste.png"))); // NOI18N
        jButtonPaste.setToolTipText("Paste");
        jButtonPaste.setBorder(null);
        jButtonPaste.setBorderPainted(false);
        jButtonPaste.setEnabled(false);
        jButtonPaste.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonPaste.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonPaste.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPaste);

        jSeparator23.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator23.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator23.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator23.setPreferredSize(new java.awt.Dimension(2, 20));
        jToolBar1.add(jSeparator23);

        jToggleButtonPointer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/pointer.png"))); // NOI18N
        jToggleButtonPointer.setToolTipText("Pointer");
        jToggleButtonPointer.setBorder(null);
        jToggleButtonPointer.setBorderPainted(false);
        jToggleButtonPointer.setEnabled(false);
        jToggleButtonPointer.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonPointer.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonPointer.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonPointer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonPointerItemStateChanged(evt);
            }
        });
        jToggleButtonPointer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPointerActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonPointer);

        jToggleButtonLineTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/line.png"))); // NOI18N
        jToggleButtonLineTool.setToolTipText("Line tool");
        jToggleButtonLineTool.setBorder(null);
        jToggleButtonLineTool.setBorderPainted(false);
        jToggleButtonLineTool.setEnabled(false);
        jToggleButtonLineTool.setFocusPainted(false);
        jToggleButtonLineTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonLineTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonLineTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonLineTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonLineToolItemStateChanged(evt);
            }
        });
        jToggleButtonLineTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonLineToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonLineTool);

        jToggleButtonRectTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/rectangle.png"))); // NOI18N
        jToggleButtonRectTool.setToolTipText("Rectangle tool");
        jToggleButtonRectTool.setBorder(null);
        jToggleButtonRectTool.setBorderPainted(false);
        jToggleButtonRectTool.setEnabled(false);
        jToggleButtonRectTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonRectTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonRectTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonRectTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonRectToolItemStateChanged(evt);
            }
        });
        jToggleButtonRectTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonRectToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonRectTool);

        jToggleButtonEllipseTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/circle.png"))); // NOI18N
        jToggleButtonEllipseTool.setToolTipText("Ellipse tool");
        jToggleButtonEllipseTool.setBorder(null);
        jToggleButtonEllipseTool.setBorderPainted(false);
        jToggleButtonEllipseTool.setEnabled(false);
        jToggleButtonEllipseTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonEllipseTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonEllipseTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonEllipseTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonEllipseToolItemStateChanged(evt);
            }
        });
        jToggleButtonEllipseTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonEllipseToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonEllipseTool);

        jToggleButtonStaticTextTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/text.png"))); // NOI18N
        jToggleButtonStaticTextTool.setToolTipText("Static text tool");
        jToggleButtonStaticTextTool.setBorder(null);
        jToggleButtonStaticTextTool.setBorderPainted(false);
        jToggleButtonStaticTextTool.setEnabled(false);
        jToggleButtonStaticTextTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonStaticTextTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonStaticTextTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonStaticTextTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonStaticTextToolItemStateChanged(evt);
            }
        });
        jToggleButtonStaticTextTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonStaticTextToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonStaticTextTool);

        jToggleButtonTextFieldTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/field.png"))); // NOI18N
        jToggleButtonTextFieldTool.setToolTipText("Textfield tool");
        jToggleButtonTextFieldTool.setBorder(null);
        jToggleButtonTextFieldTool.setBorderPainted(false);
        jToggleButtonTextFieldTool.setEnabled(false);
        jToggleButtonTextFieldTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonTextFieldTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonTextFieldTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonTextFieldTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonTextFieldToolItemStateChanged(evt);
            }
        });
        jToggleButtonTextFieldTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonTextFieldToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonTextFieldTool);

        jToggleButtonImageTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/picture.png"))); // NOI18N
        jToggleButtonImageTool.setToolTipText("Image tool");
        jToggleButtonImageTool.setBorder(null);
        jToggleButtonImageTool.setBorderPainted(false);
        jToggleButtonImageTool.setEnabled(false);
        jToggleButtonImageTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonImageTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonImageTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonImageTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonImageToolItemStateChanged(evt);
            }
        });
        jToggleButtonImageTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonImageToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonImageTool);

        jToggleButtonMapImageTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/geopista/reports/icons/toolbar/mapa.png"))); // NOI18N
        jToggleButtonMapImageTool.setToolTipText("Map Tool");
        jToggleButtonMapImageTool.setBorder(null);
        jToggleButtonMapImageTool.setBorderPainted(false);
        jToggleButtonMapImageTool.setEnabled(false);
        jToggleButtonMapImageTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonMapImageTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonMapImageTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonMapImageTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonMapImageToolItemStateChanged(evt);
            }
        });
        jToggleButtonMapImageTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonMapImageToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonMapImageTool);

        jToggleButtonBarcodeTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/barcode.png"))); // NOI18N
        jToggleButtonBarcodeTool.setToolTipText("Barcode tool");
        jToggleButtonBarcodeTool.setBorder(null);
        jToggleButtonBarcodeTool.setBorderPainted(false);
        jToggleButtonBarcodeTool.setEnabled(false);
        jToggleButtonBarcodeTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonBarcodeTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonBarcodeTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonBarcodeTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonBarcodeToolItemStateChanged(evt);
            }
        });
        jToggleButtonBarcodeTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonBarcodeToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonBarcodeTool);

        jToggleButtonFrameTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/frame.png"))); // NOI18N
        jToggleButtonFrameTool.setToolTipText("Frame tool");
        jToggleButtonFrameTool.setBorder(null);
        jToggleButtonFrameTool.setBorderPainted(false);
        jToggleButtonFrameTool.setEnabled(false);
        jToggleButtonFrameTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFrameTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFrameTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonFrameTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonFrameToolItemStateChanged(evt);
            }
        });
        jToggleButtonFrameTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonFrameToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonFrameTool);

        jToggleButtonChartTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/chart.png"))); // NOI18N
        jToggleButtonChartTool.setToolTipText("Chart tool");
        jToggleButtonChartTool.setBorder(null);
        jToggleButtonChartTool.setBorderPainted(false);
        jToggleButtonChartTool.setEnabled(false);
        jToggleButtonChartTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonChartTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonChartTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonChartTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonChartToolItemStateChanged(evt);
            }
        });
        jToggleButtonChartTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonChartToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonChartTool);

        jToggleButtonSubreportTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/subreport.png"))); // NOI18N
        jToggleButtonSubreportTool.setToolTipText("Subreport tool");
        jToggleButtonSubreportTool.setBorder(null);
        jToggleButtonSubreportTool.setBorderPainted(false);
        jToggleButtonSubreportTool.setEnabled(false);
        jToggleButtonSubreportTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonSubreportTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonSubreportTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonSubreportTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonSubreportToolItemStateChanged(evt);
            }
        });
        jToggleButtonSubreportTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSubreportToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonSubreportTool);

        jToggleButtonCrosstabTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/crosstab.png"))); // NOI18N
        jToggleButtonCrosstabTool.setToolTipText("Crosstab tool");
        jToggleButtonCrosstabTool.setBorder(null);
        jToggleButtonCrosstabTool.setBorderPainted(false);
        jToggleButtonCrosstabTool.setEnabled(false);
        jToggleButtonCrosstabTool.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonCrosstabTool.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonCrosstabTool.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonCrosstabTool.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonCrosstabToolItemStateChanged(evt);
            }
        });
        jToggleButtonCrosstabTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonCrosstabToolActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonCrosstabTool);

        jSeparator30.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator30.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator30.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator30.setPreferredSize(new java.awt.Dimension(2, 20));
        jToolBar1.add(jSeparator30);

        jButtonParameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/values.png"))); // NOI18N
        jButtonParameters.setToolTipText("Parameters");
        jButtonParameters.setBorder(null);
        jButtonParameters.setBorderPainted(false);
        jButtonParameters.setEnabled(false);
        jButtonParameters.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonParameters.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonParameters.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonParametersActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonParameters);

        jButtonBands.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/bands.png"))); // NOI18N
        jButtonBands.setToolTipText("Bands");
        jButtonBands.setBorder(null);
        jButtonBands.setBorderPainted(false);
        jButtonBands.setEnabled(false);
        jButtonBands.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonBands.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonBands.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonBands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBandsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonBands);

        jButtonGroups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/groups.png"))); // NOI18N
        jButtonGroups.setToolTipText("Groups");
        jButtonGroups.setBorder(null);
        jButtonGroups.setBorderPainted(false);
        jButtonGroups.setEnabled(false);
        jButtonGroups.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonGroups.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonGroups.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGroupsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonGroups);

        jButtonDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/database.png"))); // NOI18N
        jButtonDatabase.setToolTipText("Database");
        jButtonDatabase.setBorder(null);
        jButtonDatabase.setBorderPainted(false);
        jButtonDatabase.setEnabled(false);
        jButtonDatabase.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonDatabase.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonDatabase.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatabaseActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDatabase);

        jSeparator31.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator31.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator31.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator31.setPreferredSize(new java.awt.Dimension(2, 20));
        jSeparator31.setRequestFocusEnabled(false);
        jToolBar1.add(jSeparator31);

        jPanelZoom.setMaximumSize(new java.awt.Dimension(70, 22));
        jPanelZoom.setMinimumSize(new java.awt.Dimension(70, 22));
        jPanelZoom.setPreferredSize(new java.awt.Dimension(70, 22));
        jPanelZoom.setLayout(new java.awt.BorderLayout());

        jNumberComboBoxZoom.setMaximumRowCount(12);
        jNumberComboBoxZoom.setMinimumSize(new java.awt.Dimension(70, 18));
        jNumberComboBoxZoom.setPostfix("%");
        jNumberComboBoxZoom.setPreferredSize(new java.awt.Dimension(70, 22));
        jNumberComboBoxZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberComboBoxZoomActionPerformed(evt);
            }
        });
        jPanelZoom.add(jNumberComboBoxZoom, java.awt.BorderLayout.CENTER);

        jToolBar1.add(jPanelZoom);

        jButtonLens1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/lens_in.png"))); // NOI18N
        jButtonLens1.setToolTipText("Zoom in");
        jButtonLens1.setBorder(null);
        jButtonLens1.setBorderPainted(false);
        jButtonLens1.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonLens1.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonLens1.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonLens1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRun2ActionPerformed1(evt);
            }
        });
        jToolBar1.add(jButtonLens1);
        jButtonLens1.getAccessibleContext().setAccessibleDescription("");

        jButtonLens2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/lens_out.png"))); // NOI18N
        jButtonLens2.setToolTipText("Zoom out");
        jButtonLens2.setBorder(null);
        jButtonLens2.setBorderPainted(false);
        jButtonLens2.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonLens2.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonLens2.setOpaque(false);
        jButtonLens2.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonLens2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLens1jButtonRun2ActionPerformed1(evt);
            }
        });
        jToolBar1.add(jButtonLens2);

        jSeparator32.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator32.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator32.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator32.setPreferredSize(new java.awt.Dimension(2, 20));
        jToolBar1.add(jSeparator32);

        jButtonCompiler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/compile.png"))); // NOI18N
        jButtonCompiler.setToolTipText("Compile the report");
        jButtonCompiler.setBorder(null);
        jButtonCompiler.setBorderPainted(false);
        jButtonCompiler.setEnabled(false);
        jButtonCompiler.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonCompiler.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonCompiler.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonCompiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCompilerActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCompiler);

        jButtonRun1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/run1.png"))); // NOI18N
        jButtonRun1.setToolTipText("Run report using an empty datasource");
        jButtonRun1.setBorder(null);
        jButtonRun1.setBorderPainted(false);
        jButtonRun1.setEnabled(false);
        jButtonRun1.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonRun1.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonRun1.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonRun1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRun1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonRun1);

        jButtonRun2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/newtoolbar/run2.png"))); // NOI18N
        jButtonRun2.setToolTipText("Run report using a connection");
        jButtonRun2.setBorder(null);
        jButtonRun2.setBorderPainted(false);
        jButtonRun2.setEnabled(false);
        jButtonRun2.setMaximumSize(new java.awt.Dimension(22, 22));
        jButtonRun2.setMinimumSize(new java.awt.Dimension(22, 22));
        jButtonRun2.setPreferredSize(new java.awt.Dimension(22, 22));
        jButtonRun2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRun2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonRun2);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jMenuFile.setMnemonic('f');
        jMenuFile.setText("File");
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuItemNewDocument.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/new.png"))); // NOI18N
        jMenuItemNewDocument.setText("New document");
        jMenuItemNewDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewDocumentActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemNewDocument);

        jMenuItemWizard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/wizard.png"))); // NOI18N
        jMenuItemWizard.setText("Report wizard...");
        jMenuItemWizard.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/wizard.png"))); // NOI18N
        jMenuItemWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemWizardActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemWizard);

        jMenuItemOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/open.png"))); // NOI18N
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);

        jMenuItemSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/save.png"))); // NOI18N
        jMenuItemSave.setMnemonic('s');
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuItemSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/save.png"))); // NOI18N
        jMenuItemSaveAs.setText("Save as...");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSaveAs);

        jMenuItemSaveAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/saveall.png"))); // NOI18N
        jMenuItemSaveAll.setText("Save all");
        jMenuItemSaveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAllActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSaveAll);

        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);

        jMenuItemCloseAll.setText("Close all");
        jMenuItemCloseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseAllActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemCloseAll);

        jMenuItemCloseAllExceptThis.setText("Close all except this");
        jMenuItemCloseAllExceptThis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseAllExceptThisActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemCloseAllExceptThis);
        jMenuFile.add(jSeparator3);

        jMenuItemPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/printer.png"))); // NOI18N
        jMenuItemPrint.setText("Print design");
        jMenuItemPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPrintActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemPrint);

        jMenuItemRevertToSaved.setText("Revert to saved");
        jMenuItemRevertToSaved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRevertToSavedActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemRevertToSaved);
        jMenuFile.add(jSeparator16);

        jMenuRecentFiles.setText("Recent Files");
        jMenuFile.add(jMenuRecentFiles);
        jMenuFile.add(jSeparator15);

        jMenuItemQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/null.gif"))); // NOI18N
        jMenuItemQuit.setMnemonic('q');
        jMenuItemQuit.setText("Quit");
        jMenuItemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemQuitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemQuit);

        jMDIMenuBar.add(jMenuFile);

        jMenuEdit.setMnemonic('e');
        jMenuEdit.setText("Edit");
        jMenuEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEditActionPerformed(evt);
            }
        });

        jMenuItemUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/undo.png"))); // NOI18N
        jMenuItemUndo.setText("Undo");
        jMenuItemUndo.setEnabled(false);
        jMenuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUndoActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemUndo);

        jMenuItemRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/redo.png"))); // NOI18N
        jMenuItemRedo.setText("Redo");
        jMenuItemRedo.setEnabled(false);
        jMenuItemRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRedoActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemRedo);
        jMenuEdit.add(jSeparator1);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png"))); // NOI18N
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setEnabled(false);
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemCut);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png"))); // NOI18N
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setEnabled(false);
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemCopy);

        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png"))); // NOI18N
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setEnabled(false);
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemPaste);

        jMenuItemDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png"))); // NOI18N
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.setEnabled(false);
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemDelete);
        jMenuEdit.add(jSeparator2);

        jMenuItemInsertPageBreak.setText("Insert page/column break");
        jMenuItemInsertPageBreak.setEnabled(false);
        jMenuItemInsertPageBreak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddGroupActionPerformed1(evt);
            }
        });
        jMenuEdit.add(jMenuItemInsertPageBreak);

        jMenuAdd.setText("Insert element...");
        jMenuAdd.setEnabled(false);

        jMenuItemLine.setText("Line");
        jMenuItemLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLineActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemLine);

        jMenuItemRectangle.setText("Rectangle");
        jMenuItemRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRectangleActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemRectangle);

        jMenuItemRoundRectangle.setText("Rounded rectangle");
        jMenuItemRoundRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRoundRectangleActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemRoundRectangle);

        jMenuItemEllipse.setText("Ellipse");
        jMenuItemEllipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEllipseActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemEllipse);

        jMenuItemImage.setText("Image");
        jMenuItemImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImageActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemImage);

        jMenuItemStaticText.setText("Static text");
        jMenuItemStaticText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStaticTextActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemStaticText);

        jMenuItemTextField.setText("Text field");
        jMenuItemTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextFieldActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemTextField);

        jMenuItemChart.setText("Chart");
        jMenuItemChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChartActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemChart);

        jMenuItemSubreport.setText("Subreport");
        jMenuItemSubreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubreportActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemSubreport);

        jMenuEdit.add(jMenuAdd);

        jMenuItemAddGroup.setText("New report group wizard");
        jMenuItemAddGroup.setEnabled(false);
        jMenuItemAddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddGroupActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemAddGroup);

        jMenuItemElementProperties.setText("Element properties");
        jMenuItemElementProperties.setEnabled(false);
        jMenuItemElementProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementPropertiesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemElementProperties);

        jMenuItemXMLSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/null.gif"))); // NOI18N
        jMenuItemXMLSource.setText("XML source...");
        jMenuItemXMLSource.setEnabled(false);
        jMenuItemXMLSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemXMLSourceActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemXMLSource);

        jMenuInternationalization.setText("Internationalization");
        jMenuInternationalization.setEnabled(false);

        jMenuLocaleFiles.setText("Locale resource files");
        jMenuLocaleFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLocaleFilesActionPerformed(evt);
            }
        });
        jMenuInternationalization.add(jMenuLocaleFiles);

        jMenuEdit.add(jMenuInternationalization);
        jMenuEdit.add(jSeparator25);

        jMenuItemReportProperties.setText("Report properties");
        jMenuItemReportProperties.setEnabled(false);
        jMenuItemReportProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportPropertiesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemReportProperties);

        jMenuItemProperties.setText("Custom properties");
        jMenuItemProperties.setEnabled(false);
        jMenuItemProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPropertiesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemProperties);

        jMenuItemReportImports.setText("Report series");
        jMenuItemReportImports.setEnabled(false);
        jMenuItemReportImports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportImportsActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemReportImports);

        jMenuItemScriptletEditor.setText("ScriptletEditor");
        jMenuItemScriptletEditor.setEnabled(false);
        jMenuItemScriptletEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemScriptletEditorActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemScriptletEditor);
        jMenuEdit.add(jSeparator26);

        jMenuItemReportQuery.setText("Report query");
        jMenuItemReportQuery.setEnabled(false);
        jMenuItemReportQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportQueryActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemReportQuery);

        jMenuItemSubDataset.setText("Subdatasets");
        jMenuItemSubDataset.setEnabled(false);
        jMenuItemSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubDatasetActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemSubDataset);
        jMenuEdit.add(jSeparator28);

        jMenuItemRemoveMargins.setText("Item");
        jMenuItemRemoveMargins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRemoveMarginsActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemRemoveMargins);

        jMDIMenuBar.add(jMenuEdit);

        jMenuView.setMnemonic('v');
        jMenuView.setText("View");
        jMenuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuViewActionPerformed(evt);
            }
        });
        jMenuView.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenuViewStateChanged(evt);
            }
        });

        jCheckBoxMenuItemGrid.setText("Show grid");
        jCheckBoxMenuItemGrid.setEnabled(false);
        jCheckBoxMenuItemGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemGridActionPerformed(evt);
            }
        });
        jMenuView.add(jCheckBoxMenuItemGrid);
        jMenuView.add(jSeparator4);

        jMenuPanels.setText("Docking panes");

        jCheckBoxMenuItemExplorer.setSelected(true);
        jCheckBoxMenuItemExplorer.setText("Files");
        jCheckBoxMenuItemExplorer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemExplorerActionPerformed(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemExplorer);

        jCheckBoxMenuItemDocumentStructure.setSelected(true);
        jCheckBoxMenuItemDocumentStructure.setText("Document structure");
        jCheckBoxMenuItemDocumentStructure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemDocumentStructureActionPerformed1(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemDocumentStructure);

        jCheckBoxMenuItemElementProperties.setSelected(true);
        jCheckBoxMenuItemElementProperties.setText("Element properties");
        jCheckBoxMenuItemElementProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemElementPropertiesActionPerformed1(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemElementProperties);

        jCheckBoxMenuItemLibrary.setSelected(true);
        jCheckBoxMenuItemLibrary.setText("Library");
        jCheckBoxMenuItemLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemLibraryActionPerformed(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemLibrary);

        jCheckBoxMenuItemStylesLibrary.setSelected(true);
        jCheckBoxMenuItemStylesLibrary.setText("Styles Library");
        jCheckBoxMenuItemStylesLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemStylesLibraryActionPerformed(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemStylesLibrary);

        jCheckBoxMenuItemOutput.setSelected(true);
        jCheckBoxMenuItemOutput.setText("Show output window");
        jCheckBoxMenuItemOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemOutputActionPerformed(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemOutput);

        jCheckBoxMenuItemThreadList.setSelected(true);
        jCheckBoxMenuItemThreadList.setText("Threads list");
        jCheckBoxMenuItemThreadList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemExplorerActionPerformed1(evt);
            }
        });
        jMenuPanels.add(jCheckBoxMenuItemThreadList);

        jMenuView.add(jMenuPanels);

        jMenuToolBars.setText("Toolbars");

        jCheckBoxMenuItemTextFormatting.setSelected(true);
        jCheckBoxMenuItemTextFormatting.setText("Text formatting");
        jCheckBoxMenuItemTextFormatting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemTextFormattingActionPerformed(evt);
            }
        });
        jMenuToolBars.add(jCheckBoxMenuItemTextFormatting);

        jCheckBoxMenuItemElementsFormatting.setSelected(true);
        jCheckBoxMenuItemElementsFormatting.setText("Elements formatting");
        jCheckBoxMenuItemElementsFormatting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemElementsFormattingActionPerformed(evt);
            }
        });
        jMenuToolBars.add(jCheckBoxMenuItemElementsFormatting);

        jMenuView.add(jMenuToolBars);
        jMenuView.add(jSeparator12);

        jMenuItemFields.setText("Report fields");
        jMenuItemFields.setEnabled(false);
        jMenuItemFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFieldsActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemFields);

        jMenuItemVariables.setText("Report variables");
        jMenuItemVariables.setEnabled(false);
        jMenuItemVariables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVariablesActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemVariables);

        jMenuItemParameters.setText("Report parameters");
        jMenuItemParameters.setEnabled(false);
        jMenuItemParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemParametersActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemParameters);
        jMenuView.add(jSeparator6);

        jMenuItemBands.setText("Bands");
        jMenuItemBands.setEnabled(false);
        jMenuItemBands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBandsActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemBands);

        jMenuItemGroups.setText("Report groups");
        jMenuItemGroups.setEnabled(false);
        jMenuItemGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGroupsActionPerformed(evt);
            }
        });
        jMenuView.add(jMenuItemGroups);

        jMDIMenuBar.add(jMenuView);

        jMenuFormat.setMnemonic('o');
        jMenuFormat.setText("Format");

        jMenuItemStyles.setText("Styles");
        jMenuItemStyles.setEnabled(false);
        jMenuItemStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStylesActionPerformed(evt);
            }
        });
        jMenuFormat.add(jMenuItemStyles);

        jMenuItemFonts.setText("Report fonts");
        jMenuItemFonts.setEnabled(false);
        jMenuItemFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFontsActionPerformed(evt);
            }
        });
        jMenuFormat.add(jMenuItemFonts);
        jMenuFormat.add(jSeparator29);

        jMenuAlign.setText("Align");
        jMenuAlign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAlignActionPerformed(evt);
            }
        });

        jMenuItemAlignLeft.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_left.png"))); // NOI18N
        jMenuItemAlignLeft.setText("Align left");
        jMenuItemAlignLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignLeftActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignLeft);

        jMenuItemAlignRight.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_right.png"))); // NOI18N
        jMenuItemAlignRight.setText("Align right");
        jMenuItemAlignRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignRightActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignRight);

        jMenuItemAlignTop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_top.png"))); // NOI18N
        jMenuItemAlignTop.setText("Align top");
        jMenuItemAlignTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignTopActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignTop);

        jMenuItemAlignBottom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_bottom.png"))); // NOI18N
        jMenuItemAlignBottom.setText("Align bottom");
        jMenuItemAlignBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignBottomActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignBottom);
        jMenuAlign.add(jSeparator19);

        jMenuItemAlignVerticalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_center_axis.png"))); // NOI18N
        jMenuItemAlignVerticalAxis.setText("Align vertical axis");
        jMenuItemAlignVerticalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignVerticalAxisActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignVerticalAxis);

        jMenuItemAlignHorizontalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter_axis.png"))); // NOI18N
        jMenuItemAlignHorizontalAxis.setText("Align horizontal axis");
        jMenuItemAlignHorizontalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignHorizontalAxisActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignHorizontalAxis);
        jMenuAlign.add(jSeparator20);

        jMenuItemAlignToBandTop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignToBandTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/format/align_top_band.png"))); // NOI18N
        jMenuItemAlignToBandTop.setText("Align to band top");
        jMenuItemAlignToBandTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignToBandTopActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignToBandTop);

        jMenuItemAlignToBandBottom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAlignToBandBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/format/align_bottom_band.png"))); // NOI18N
        jMenuItemAlignToBandBottom.setText("Align to band bottom");
        jMenuItemAlignToBandBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAlignToBandBottomActionPerformed(evt);
            }
        });
        jMenuAlign.add(jMenuItemAlignToBandBottom);

        jMenuFormat.add(jMenuAlign);

        jMenuSize.setText("Size");

        jMenuItemSameWidth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize.png"))); // NOI18N
        jMenuItemSameWidth.setText("Same width");
        jMenuItemSameWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameWidthActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameWidth);

        jMenuItemSameWidthMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_plus.png"))); // NOI18N
        jMenuItemSameWidthMax.setText("Same width (max)");
        jMenuItemSameWidthMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameWidthMaxActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameWidthMax);

        jMenuItemSameWidthMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_min.png"))); // NOI18N
        jMenuItemSameWidthMin.setText("Same width (min)");
        jMenuItemSameWidthMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameWidthMinActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameWidthMin);
        jMenuSize.add(jSeparator17);

        jMenuItemSameHeight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize.png"))); // NOI18N
        jMenuItemSameHeight.setText("Same height");
        jMenuItemSameHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameHeightActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameHeight);

        jMenuItemSameHeightMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_min.png"))); // NOI18N
        jMenuItemSameHeightMin.setText("Same height (min)");
        jMenuItemSameHeightMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameHeightMinActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameHeightMin);

        jMenuItemSameHeightMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_plus.png"))); // NOI18N
        jMenuItemSameHeightMax.setText("Same height (max)");
        jMenuItemSameHeightMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameHeightMaxActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameHeightMax);
        jMenuSize.add(jSeparator18);

        jMenuItemSameSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_size.png"))); // NOI18N
        jMenuItemSameSize.setText("Same size");
        jMenuItemSameSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSameSizeActionPerformed(evt);
            }
        });
        jMenuSize.add(jMenuItemSameSize);

        jMenuFormat.add(jMenuSize);

        jMenuPosition.setText("Position");

        jMenuItemCenterH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_hcenter.png"))); // NOI18N
        jMenuItemCenterH.setText("Center horizontally (band based)");
        jMenuItemCenterH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCenterHActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemCenterH);

        jMenuItemCenterV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter.png"))); // NOI18N
        jMenuItemCenterV.setText("Center vertically (band based)");
        jMenuItemCenterV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCenterVActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemCenterV);

        jMenuItemCenterInBand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_ccenter.png"))); // NOI18N
        jMenuItemCenterInBand.setText("Center in band");
        jMenuItemCenterInBand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCenterInBandActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemCenterInBand);

        jMenuItemCenterBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_bcenter.png"))); // NOI18N
        jMenuItemCenterBackground.setText("Center in background");
        jMenuItemCenterBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCenterBackgroundActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemCenterBackground);

        jMenuItemJoinLeft.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemJoinLeft.setText("Join sides left");
        jMenuItemJoinLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemJoinLeftActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemJoinLeft);

        jMenuItemJoinRight.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemJoinRight.setText("Join sides right");
        jMenuItemJoinRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemJoinRightActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemJoinRight);

        jMenuItemLeftMargin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemLeftMargin.setText("Join left page margin");
        jMenuItemLeftMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLeftMarginActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemLeftMargin);

        jMenuItemRightMargin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRightMargin.setText("Join right page margin");
        jMenuItemRightMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRightMarginActionPerformed(evt);
            }
        });
        jMenuPosition.add(jMenuItemRightMargin);

        jMenuFormat.add(jMenuPosition);
        jMenuFormat.add(jSeparator5);

        jMenuHSpacing.setText("Horizontal spacing...");

        jMenuItemHSMakeEqual.setText("Make equal");
        jMenuItemHSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHSMakeEqualActionPerformed(evt);
            }
        });
        jMenuHSpacing.add(jMenuItemHSMakeEqual);

        jMenuItemHSIncrease.setText("Increase");
        jMenuItemHSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHSIncreaseActionPerformed(evt);
            }
        });
        jMenuHSpacing.add(jMenuItemHSIncrease);

        jMenuItemHSDecrease.setText("Decrease");
        jMenuItemHSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHSDecreaseActionPerformed(evt);
            }
        });
        jMenuHSpacing.add(jMenuItemHSDecrease);

        jMenuItemHSRemove.setText("Remove");
        jMenuItemHSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHSRemoveActionPerformed(evt);
            }
        });
        jMenuHSpacing.add(jMenuItemHSRemove);

        jMenuFormat.add(jMenuHSpacing);

        jMenuVSpacing.setText("Vertical spacing");

        jMenuItemVSMakeEqual.setText("Make equal");
        jMenuItemVSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVSMakeEqualActionPerformed(evt);
            }
        });
        jMenuVSpacing.add(jMenuItemVSMakeEqual);

        jMenuItemVSIncrease.setText("Increase");
        jMenuItemVSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVSIncreaseActionPerformed(evt);
            }
        });
        jMenuVSpacing.add(jMenuItemVSIncrease);

        jMenuItemVSDecrease.setText("Decrease");
        jMenuItemVSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVSDecreaseActionPerformed(evt);
            }
        });
        jMenuVSpacing.add(jMenuItemVSDecrease);

        jMenuItemVSRemove.setText("Remove");
        jMenuItemVSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVSRemoveActionPerformed(evt);
            }
        });
        jMenuVSpacing.add(jMenuItemVSRemove);

        jMenuFormat.add(jMenuVSpacing);

        jMenuItemOrganize.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOrganize.setText("Organize as a table");
        jMenuItemOrganize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOrganizeActionPerformed(evt);
            }
        });
        jMenuFormat.add(jMenuItemOrganize);
        jMenuFormat.add(jSeparator8);

        jMenuItemBringToFront.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtofront.png"))); // NOI18N
        jMenuItemBringToFront.setText("Bring to front");
        jMenuItemBringToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBringToFrontActionPerformed(evt);
            }
        });
        jMenuFormat.add(jMenuItemBringToFront);

        jMenuItemSendToBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtoback.png"))); // NOI18N
        jMenuItemSendToBack.setText("Send to back");
        jMenuItemSendToBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSendToBackActionPerformed(evt);
            }
        });
        jMenuFormat.add(jMenuItemSendToBack);

        jMDIMenuBar.add(jMenuFormat);

        jMenuDatabase.setMnemonic('d');
        jMenuDatabase.setText("Data");

        jMenuItemConnections.setText("Connections / Datasources");
        jMenuItemConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConnectionsActionPerformed(evt);
            }
        });
        jMenuDatabase.add(jMenuItemConnections);

        jMenuItemActiveConnection.setText("Set active connection");
        jMenuItemActiveConnection.setToolTipText("Set the connection that must be used to fill this report");
        jMenuItemActiveConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemActiveConnectionActionPerformed(evt);
            }
        });
        jMenuDatabase.add(jMenuItemActiveConnection);
        jMenuDatabase.add(jSeparator9);

        jMenuItemReportQuery2.setText("Report query");
        jMenuItemReportQuery2.setEnabled(false);
        jMenuItemReportQuery2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportQuery2ActionPerformed(evt);
            }
        });
        jMenuDatabase.add(jMenuItemReportQuery2);

        jMenuItemSubDataset1.setText("Subdatasets");
        jMenuItemSubDataset1.setEnabled(false);
        jMenuItemSubDataset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubDatasetActionPerformed(evt);
            }
        });
        jMenuDatabase.add(jMenuItemSubDataset1);

        jMDIMenuBar.add(jMenuDatabase);

        jMenuBuild.setMnemonic('b');
        jMenuBuild.setText("Build");
        jMenuBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBuildActionPerformed(evt);
            }
        });

        jMenuItemCompile.setText("Compile");
        jMenuItemCompile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jMenuItemCompileItemStateChanged(evt);
            }
        });
        jMenuItemCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCompileActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemCompile);
        jMenuBuild.add(jSeparator10);

        jMenuItemExecute.setText("Execute (empty datasource)");
        jMenuItemExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExecuteActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemExecute);

        jMenuItemExecuteDB.setText("Execute report (using active conn.) ");
        jMenuItemExecuteDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExecuteDBActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemExecuteDB);
        jMenuBuild.add(jSeparator33);

        jMenuItemCreateFromTemplate.setText("Create from template");
        jMenuItemCreateFromTemplate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jMenuItemCompileItemStateChanged1(evt);
            }
        });
        jMenuItemCreateFromTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCompileActionPerformed1(evt);
            }
        });
        jMenuBuild.add(jMenuItemCreateFromTemplate);
        jMenuBuild.add(jSeparator11);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewPDF);
        jRadioButtonMenuItemPreviewPDF.setSelected(true);
        jRadioButtonMenuItemPreviewPDF.setText("PDF preview");
        jRadioButtonMenuItemPreviewPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewPDFActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewPDF);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewHTML);
        jRadioButtonMenuItemPreviewHTML.setText("HTML preview");
        jRadioButtonMenuItemPreviewHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewHTMLActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewHTML);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewJAVA);
        jRadioButtonMenuItemPreviewJAVA.setText("JAVA 2D preview");
        jRadioButtonMenuItemPreviewJAVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewJAVAActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewJAVA);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewXLS);
        jRadioButtonMenuItemPreviewXLS.setText("Excel preview");
        jRadioButtonMenuItemPreviewXLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewXLSActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewXLS);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewXLS2);
        jRadioButtonMenuItemPreviewXLS2.setText("Excel preview (using JExcelApi)");
        jRadioButtonMenuItemPreviewXLS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewXLS2ActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewXLS2);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewCSV);
        jRadioButtonMenuItemPreviewCSV.setText("CSV preview");
        jRadioButtonMenuItemPreviewCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewCSVActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewCSV);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewTXT);
        jRadioButtonMenuItemPreviewTXT.setText("Text preview (Experimental)");
        jRadioButtonMenuItemPreviewTXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewTXTActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewTXT);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewTXTJR);
        jRadioButtonMenuItemPreviewTXTJR.setSelected(true);
        jRadioButtonMenuItemPreviewTXTJR.setText("TXT JR preview");
        jRadioButtonMenuItemPreviewTXTJR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewTXTJRActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewTXTJR);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewRTF);
        jRadioButtonMenuItemPreviewRTF.setSelected(true);
        jRadioButtonMenuItemPreviewRTF.setText("RTF preview");
        jRadioButtonMenuItemPreviewRTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewRTFActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewRTF);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewODF);
        jRadioButtonMenuItemPreviewODF.setText("OpenOffice (ODF) preview");
        jRadioButtonMenuItemPreviewODF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewODFActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewODF);

        buttonGroupExportType.add(jRadioButtonMenuItemPreviewInternalViewer);
        jRadioButtonMenuItemPreviewInternalViewer.setText("JRViewer preview");
        jRadioButtonMenuItemPreviewInternalViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemPreviewInternalViewerActionPerformed(evt);
            }
        });
        jMenuBuild.add(jRadioButtonMenuItemPreviewInternalViewer);
        jMenuBuild.add(jSeparator14);

        jMenuItemActiveConnection1.setText("Set active connection");
        jMenuItemActiveConnection1.setToolTipText("Set the connection that must be used to fill this report");
        jMenuItemActiveConnection1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemActiveConnectionActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemActiveConnection1);

        jMenuItemReportLocale.setText("Item");
        jMenuItemReportLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportLocaleActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemReportLocale);

        jMenuItemReportTimeZone.setText("Time Zone");
        jMenuItemReportTimeZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReportTimeZoneActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemReportTimeZone);

        jCheckBoxMenuItemIgnorePagination.setText("Ignore pagination");
        jCheckBoxMenuItemIgnorePagination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemIgnorePaginationActionPerformed(evt);
            }
        });
        jMenuBuild.add(jCheckBoxMenuItemIgnorePagination);

        jMenuItemMaxRecords.setText("Max records");
        jMenuItemMaxRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMaxRecordsActionPerformed(evt);
            }
        });
        jMenuBuild.add(jMenuItemMaxRecords);

        jCheckBoxMenuItemReportVirtualizer.setText("Use Report virtualizer");
        jCheckBoxMenuItemReportVirtualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemReportVirtualizerActionPerformed(evt);
            }
        });
        jMenuBuild.add(jCheckBoxMenuItemReportVirtualizer);

        jMDIMenuBar.add(jMenuBuild);

        jMenuTools.setMnemonic('t');
        jMenuTools.setText("Options");

        jCheckBoxMenuItemSnapToGrid.setText("Snap To Grid");
        jCheckBoxMenuItemSnapToGrid.setEnabled(false);
        jCheckBoxMenuItemSnapToGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemSnapToGridActionPerformed(evt);
            }
        });
        jMenuTools.add(jCheckBoxMenuItemSnapToGrid);

        jCheckBoxMenuItemEMM.setText("Disable elements mouse move");
        jCheckBoxMenuItemEMM.setEnabled(false);
        jCheckBoxMenuItemEMM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemEMMActionPerformed(evt);
            }
        });
        jMenuTools.add(jCheckBoxMenuItemEMM);
        jMenuTools.add(jSeparator13);

        jMenuItemOptions.setText("Settings...");
        jMenuItemOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOptionsActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemOptions);

        jMenuItemCompatibility.setText("Compatibility...");
        jMenuItemCompatibility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCompatibilityActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemCompatibility);

        jMenuItemClasspath.setText("Classpath");
        jMenuItemClasspath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClasspathActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemClasspath);

        jMenuItemFontspath.setText("Fonts path");
        jMenuItemFontspath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClasspathActionPerformed1(evt);
            }
        });
        jMenuTools.add(jMenuItemFontspath);

        jMenuItemReloadFonts.setText("Reload fonts");
        jMenuItemReloadFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReloadFontsActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemReloadFonts);

        jMenuItemExpressions.setText("Formulas");
        jMenuItemExpressions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportOptionsActionPerformed1(evt);
            }
        });
        jMenuTools.add(jMenuItemExpressions);

        jMenuItemExportOptions.setText("Export options");
        jMenuItemExportOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportOptionsActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemExportOptions);

        jMenuItemQueryExecuters.setText("Query executers");
        jMenuItemQueryExecuters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExpressionsjMenuItemExportOptionsActionPerformed1(evt);
            }
        });
        jMenuTools.add(jMenuItemQueryExecuters);
        jMenuTools.add(jSeparator7);

        jMenuItemPluginConfig.setText("Configure plugins");
        jMenuItemPluginConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPluginConfigActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemPluginConfig);

        jMDIMenuBar.add(jMenuTools);

        jMenuPlugins.setText("Plugins");
        jMDIMenuBar.add(jMenuPlugins);

        jMDIMenuBar.add(jMenuLocalGIS);

        jMenuItemLocalGISLogout.setText("Logout");
        jMenuItemLocalGISLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLocalGISLogoutActionPerformed(evt);
            }
        });
        
        jMenuItemLocalGISSendToServer.setText("Send to Server");
        jMenuItemLocalGISSendToServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jMenuItemLocalGISSendToServerActionPerformed(evt);
            }
        });
        
        jMenuLocalGIS.add(jMenuItemLocalGISSendToServer);
        jMenuLocalGIS.add(jMenuItemLocalGISLogout);
        
        jMenuWindow.setMnemonic('w');
        jMenuWindow.setText("Window");

        jMenuItemCascade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_cascade.gif"))); // NOI18N
        jMenuItemCascade.setText("Cascade");
        jMenuItemCascade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCascadeActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemCascade);

        jMenuItemTileHorizontal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_htile.gif"))); // NOI18N
        jMenuItemTileHorizontal.setText("Tile horizontal");
        jMenuItemTileHorizontal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTileHorizontalActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemTileHorizontal);

        jMenuItemTileVertical.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_vtile.gif"))); // NOI18N
        jMenuItemTileVertical.setText("Tile vertical");
        jMenuItemTileVertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTileVerticalActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemTileVertical);

        jMenuItemtileAnodine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_atile.gif"))); // NOI18N
        jMenuItemtileAnodine.setText("Tile anodine");
        jMenuItemtileAnodine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemtileAnodineActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemtileAnodine);

        jMenuItemNextWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_next.gif"))); // NOI18N
        jMenuItemNextWin.setText("Next Window");
        jMenuItemNextWin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNextWinActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemNextWin);

        jMenuItemPrevWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/win_prev.gif"))); // NOI18N
        jMenuItemPrevWin.setText("Previous Window");
        jMenuItemPrevWin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPrevWinActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemPrevWin);

        jMenuItemRightSide.setText("Show/Hide right side");
        jMenuItemRightSide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRightSideActionPerformed(evt);
            }
        });
        jMenuWindow.add(jMenuItemRightSide);
        jMenuWindow.add(jSeparator21);

        jMDIMenuBar.add(jMenuWindow);

        jMenuHelp.setMnemonic('h');
        jMenuHelp.setText("Help");

        jMenuItemHomePage.setText("iReport home page");
        jMenuItemHomePage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemForumjMenuItemHelpActionPerformed1(evt);
            }
        });
        jMenuHelp.add(jMenuItemHomePage);

        jMenuItemHelp.setText("Help...");
        jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemHelp);

        jMenuItemForum.setText("Forum...");
        jMenuItemForum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpActionPerformed1(evt);
            }
        });
        jMenuHelp.add(jMenuItemForum);

        jMenuItemAbout.setText("About iReport...");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMDIMenuBar.add(jMenuHelp);

        setJMenuBar(jMDIMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButtonMapImageToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonMapImageToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
                    jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.MAP_IMAGE_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonMapImageTool);
        }
    }//GEN-LAST:event_jToggleButtonMapImageToolItemStateChanged

    private void jToggleButtonMapImageToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonMapImageToolActionPerformed
        jToggleButtonMapImageTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonMapImageToolActionPerformed

    private void jRadioButtonMenuItemPreviewODFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewODFActionPerformed
        this.setReportViewer(IREPORT_ODF_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewODFActionPerformed

    private void jPanelMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelMasterMouseClicked



    }//GEN-LAST:event_jPanelMasterMouseClicked



    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged

    }//GEN-LAST:event_formWindowStateChanged

    private void jMenuItemAddGroupActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddGroupActionPerformed1

        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

             this.setActiveTool( null );
             activeFrame.setNewObjectType( ReportElementType.BREAK_ELEMENT );
        }

    }//GEN-LAST:event_jMenuItemAddGroupActionPerformed1

    private void jMenuItemCompileActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCompileActionPerformed1

        UserTemplatesDialog utd = new UserTemplatesDialog(this, true);
        utd.setVisible(true);

    }//GEN-LAST:event_jMenuItemCompileActionPerformed1

    private void jMenuItemCompileItemStateChanged1(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jMenuItemCompileItemStateChanged1
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemCompileItemStateChanged1

    private void jMenuItemReloadFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReloadFontsActionPerformed

        Thread fontsThread = new Thread(new Runnable()
        {
            public void run()
            {
                reloadFontsLists();
            }
        });

        fontsThread.start();

    }//GEN-LAST:event_jMenuItemReloadFontsActionPerformed

    private void jMenuItemClasspathActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClasspathActionPerformed1

        FontsPathDialog cpd = new FontsPathDialog(this, true);
        cpd.setFontspath( getFontspath(), getClasspath() );
        cpd.setVisible(true);
        if (cpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION )
        {
            setFontspath( cpd.getFontspath() );
        }
    }//GEN-LAST:event_jMenuItemClasspathActionPerformed1

    private void jMenuItemForumjMenuItemHelpActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemForumjMenuItemHelpActionPerformed1
            openUrl("http://jasperforge.org/sf/projects/ireport");
    }//GEN-LAST:event_jMenuItemForumjMenuItemHelpActionPerformed1

    private void jMenuItemHelpActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHelpActionPerformed1

            openUrl("http://www.jasperforge.org/index.php?option=com_joomlaboard&Itemid=215&func=showcat&catid=9");

    }//GEN-LAST:event_jMenuItemHelpActionPerformed1

    private void jButtonSaveActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed1

        jMenuItemSaveAllActionPerformed(evt);
    }//GEN-LAST:event_jButtonSaveActionPerformed1

    private void jButtonNewActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed1

        jMenuItemWizardActionPerformed(evt);
    }//GEN-LAST:event_jButtonNewActionPerformed1

    private void jMenuItemExpressionsjMenuItemExportOptionsActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExpressionsjMenuItemExportOptionsActionPerformed1


        QueryExecutersDialog qed = new QueryExecutersDialog(this, true);
        qed.setVisible(true);

    }//GEN-LAST:event_jMenuItemExpressionsjMenuItemExportOptionsActionPerformed1

    private void jCheckBoxMenuItemExplorerActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemExplorerActionPerformed1

         if (jCheckBoxMenuItemThreadList.isSelected()) {
                       //DockingManager.dock((Dockable)viewThreads, (Dockable)desktop,  DockingConstants.EAST_REGION);
                       Misc.dockAt(viewThreads, DockingConstants.SOUTH_REGION, 4);
         }
         else {
             DockingManager.close(viewThreads );
         }
    }//GEN-LAST:event_jCheckBoxMenuItemExplorerActionPerformed1

    private void jMenuItemReportTimeZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportTimeZoneActionPerformed

        it.businesslogic.ireport.gui.locale.TimeZoneDialog fd = new it.businesslogic.ireport.gui.locale.TimeZoneDialog(this,true);
        fd.setReportTimeZoneId( this.getProperties().getProperty("reportTimeZoneId", "") );
        fd.setVisible(true);

        if (fd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            String reportTimeZoneId = fd.getReportTimeZoneId();
            if (reportTimeZoneId == null)
            {
             this.getProperties().remove("reportTimeZoneId");
            }
            else
            {
                this.getProperties().setProperty("reportTimeZoneId", reportTimeZoneId);
            }
            saveiReportConfiguration();
 	    updateJMenuItemReportTimeZone();
        }

    }//GEN-LAST:event_jMenuItemReportTimeZoneActionPerformed

    private void jMenuItemExportOptionsActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportOptionsActionPerformed1

        CustomExpressionsDialog ced = new CustomExpressionsDialog(this, true);
        ced.setExpressions( ExpressionEditor.defaultExpressions);
        ced.setVisible(true);
        if (ced.getDialogResult() == JOptionPane.OK_OPTION)
        {
            saveExpressionsList( ced.getExpressions() );
        }
    }//GEN-LAST:event_jMenuItemExportOptionsActionPerformed1

    private void jMenuItemAddGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddGroupActionPerformed

        ReportGroupWizard rgw = new ReportGroupWizard();
        rgw.startWizard();
    }//GEN-LAST:event_jMenuItemAddGroupActionPerformed

    private void jCheckBoxMenuItemStylesLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemStylesLibraryActionPerformed
    if (jCheckBoxMenuItemStylesLibrary.isSelected())
        {
                     Misc.dockAt(stylesPanleView, DockingConstants.EAST_REGION, 2);
                     //  this.getDockingContainerRight().addPanel("Styles library", this.stylesView, true);
        }
        else
        {
           DockingManager.close(stylesPanleView);
        }
        getProperties().put("panelStylesLibrary",  ""+jCheckBoxMenuItemStylesLibrary.isSelected());
        this.saveiReportConfiguration();
    }//GEN-LAST:event_jCheckBoxMenuItemStylesLibraryActionPerformed

    private void jCheckBoxMenuItemLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemLibraryActionPerformed
        if (jCheckBoxMenuItemLibrary.isSelected())
        {
                 Misc.dockAt(viewLibrary, DockingConstants.EAST_REGION, 1);
           //  this.getDockingContainerRight().addPanel("Library", this.libraryPanel, true);
        }
        else
        {
           DockingManager.close(viewLibrary);
        }
        getProperties().put("panelLibrary",  ""+jCheckBoxMenuItemLibrary.isSelected());
        this.saveiReportConfiguration();


    }//GEN-LAST:event_jCheckBoxMenuItemLibraryActionPerformed

    private void jCheckBoxMenuItemDocumentStructureActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemDocumentStructureActionPerformed1
        if (jCheckBoxMenuItemDocumentStructure.isSelected())
        {
            if (getActiveReportFrame() == null || getActiveReportFrame().getSelectedCrosstabEditorPanel() == null)
            {
                  Misc.dockAt(viewDocumentStructure, DockingConstants.WEST_REGION, 1);
            }
            else
            {
                jCheckBoxMenuItemDocumentStructure.setSelected(true);
            }
        }
        else
        {
            DockingManager.close(viewDocumentStructure);
        }
        getProperties().put("panelDocumentStructure",  ""+jCheckBoxMenuItemDocumentStructure.isSelected());
        this.saveiReportConfiguration();
    }//GEN-LAST:event_jCheckBoxMenuItemDocumentStructureActionPerformed1

    private void jMenuViewStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenuViewStateChanged

    }//GEN-LAST:event_jMenuViewStateChanged

    private void jMenuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuViewActionPerformed



    }//GEN-LAST:event_jMenuViewActionPerformed

    private void jCheckBoxMenuItemElementPropertiesActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemElementPropertiesActionPerformed1
        if (jCheckBoxMenuItemElementProperties.isSelected())
        {
                 Misc.dockAt(viewPropertySheet, DockingConstants.EAST_REGION, 0);
           // this.getDockingContainerRight().insertPanel(0, "Element properties", this.reportElementSheetPanel, true);
        }
        else
        {
           DockingManager.close(viewPropertySheet);
        }
        getProperties().put("panelElementProperties",  ""+jCheckBoxMenuItemElementProperties.isSelected());
        this.saveiReportConfiguration();
    }//GEN-LAST:event_jCheckBoxMenuItemElementPropertiesActionPerformed1

    private void jRadioButtonMenuItemPreviewXLS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewXLS2ActionPerformed
        this.setReportViewer(IREPORT_XLS2_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewXLS2ActionPerformed

    private void jButtonLens1jButtonRun2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLens1jButtonRun2ActionPerformed1
        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

             double value = jNumberComboBoxZoom.getValue();

             if (value > 800) value = 800;
             else if (value > 700) value = 700;
             else if (value > 600) value = 600;
             else if (value > 500) value = 500;
             else if (value > 400) value = 400;
             else if (value > 350) value = 350;
             else if (value > 300) value = 300;
             else if (value > 250) value = 250;
             else if (value > 200) value = 200;
             else if (value > 150) value = 150;
             else if (value > 100) value = 100;
             else if (value > 50) value = 50;

             jNumberComboBoxZoom.setValue(value);
        }
    }//GEN-LAST:event_jButtonLens1jButtonRun2ActionPerformed1

    private void jButtonRun2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRun2ActionPerformed1

        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

             double value = jNumberComboBoxZoom.getValue();

             if (value < 50) value = 50;
             else if (value < 100) value = 100;
             else if (value < 150) value = 150;
             else if (value < 200) value = 200;
             else if (value < 250) value = 250;
             else if (value < 300) value = 300;
             else if (value < 350) value = 350;
             else if (value < 400) value = 400;
             else if (value < 500) value = 500;
             else if (value < 600) value = 600;
             else if (value < 700) value = 700;
             else value = 800;

             jNumberComboBoxZoom.setValue(value);
        }

    }//GEN-LAST:event_jButtonRun2ActionPerformed1

    private void jToggleButtonCrosstabToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonCrosstabToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                if (jMDIDesktopPane.getSelectedFrame() != null &&
                jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                    JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                    jrf.setNewObjectType( ReportElementType.CROSSTAB_ELEMENT);
                    jrf.setLensMode(false);
                }
                setActiveTool(jToggleButtonCrosstabTool);
            }
    }//GEN-LAST:event_jToggleButtonCrosstabToolItemStateChanged

    private void jToggleButtonCrosstabToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonCrosstabToolActionPerformed
            jToggleButtonCrosstabTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonCrosstabToolActionPerformed

    private void jMenuItemStylesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStylesActionPerformed
        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

             StylesDialog sdd = new StylesDialog( this, true);
             sdd.setVisible(true);

        }
    }//GEN-LAST:event_jMenuItemStylesActionPerformed

    private void jMenuItemSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubDatasetActionPerformed

         JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

             SubDatasetsDialog sdd = new SubDatasetsDialog( this, true);
             sdd.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItemSubDatasetActionPerformed

    private void jToggleButtonFrameToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonFrameToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.FRAME_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonFrameTool);
        }
    }//GEN-LAST:event_jToggleButtonFrameToolItemStateChanged

    private void jToggleButtonFrameToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonFrameToolActionPerformed
        jToggleButtonFrameTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonFrameToolActionPerformed

    private void jMenuItemMaxRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMaxRecordsActionPerformed


        it.businesslogic.ireport.gui.MaxRecordsDialog fd = new it.businesslogic.ireport.gui.MaxRecordsDialog(this,true);
        try {
        fd.setMaxRecords( Integer.parseInt( this.getProperties().getProperty("maxRecords", "0") ));
        } catch (Exception ex) {}

        fd.setVisible(true);

        if (fd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            int maxRecords = fd.getMaxRecords();

            this.getProperties().setProperty("maxRecords", ""+maxRecords);
            saveiReportConfiguration();
            updateJMenuItemReportMaxRecords();
        }

    }//GEN-LAST:event_jMenuItemMaxRecordsActionPerformed

    private void jMenuBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBuildActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuBuildActionPerformed

    private void jRadioButtonMenuItemPreviewRTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewRTFActionPerformed
        this.setReportViewer(IREPORT_RTF_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewRTFActionPerformed

    private void jRadioButtonMenuItemPreviewTXTJRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewTXTJRActionPerformed
        this.setReportViewer(IREPORT_TXT_JR_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewTXTJRActionPerformed

    private void jCheckBoxMenuItemIgnorePaginationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemIgnorePaginationActionPerformed

        getProperties().put("IgnorePagination",  ""+jCheckBoxMenuItemIgnorePagination.isSelected());
     	this.saveiReportConfiguration();

    }//GEN-LAST:event_jCheckBoxMenuItemIgnorePaginationActionPerformed

    private void jCheckBoxMenuItemReportVirtualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemReportVirtualizerActionPerformed

        getProperties().put("ReportVirtualizer",  ""+jCheckBoxMenuItemReportVirtualizer.isSelected());
     	this.saveiReportConfiguration();

    }//GEN-LAST:event_jCheckBoxMenuItemReportVirtualizerActionPerformed

    private void jMenuItemPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPrintActionPerformed


        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {

                java.awt.Image img = activeFrame.getImage();
                HashMap hm = new HashMap();
                hm.put("report", activeFrame.getReport());
                hm.put("design_img",img);
                try {
                    net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("it/businesslogic/ireport/res/ireport_design.jrxml"));
                    net.sf.jasperreports.engine.JasperPrint print = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr,hm,new net.sf.jasperreports.engine.JREmptyDataSource());
                    net.sf.jasperreports.view.JasperViewer jasperViewer = new net.sf.jasperreports.view.JasperViewer(print,false);
                    jasperViewer.setTitle("iReport JasperViewer");
                    jasperViewer.setVisible(true);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
        }

    }//GEN-LAST:event_jMenuItemPrintActionPerformed

    private void jMenuItemRightSideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRightSideActionPerformed

        /*
        if (jSplitPaneHelp.getDividerSize() == 0)
        {

            jSplitPaneHelp.setDividerSize(6);
            jSplitPaneHelp.setDividerLocation( jSplitPaneHelp.getWidth()-8-rightColumnWidth );
            dockingContainerRight.setSize(400,10);
            dockingContainerRight.setVisible(true);
        }
        else
        {
            rightColumnWidth = dockingContainerRight.getWidth();
            jSplitPaneHelp.setDividerSize(0);
            dockingContainerRight.setVisible(false);
        }
         */

    }//GEN-LAST:event_jMenuItemRightSideActionPerformed

    private void jMenuAlignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAlignActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuAlignActionPerformed

    private void jMenuItemSaveAllFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAllFromListActionPerformed
        jMenuItemSaveAllActionPerformed( evt );
    }//GEN-LAST:event_jMenuItemSaveAllFromListActionPerformed

    private void jMenuItemSaveAsFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsFromListActionPerformed

        //JReportFrame jrf = getSelectedJRFFromFilesTree();
        //if (jrf != null) {
        //    saveAs( jrf );
        //}

        List frames = getSelectedJRFsFromFilesTree();
        for (int i=0; i<frames.size(); ++i)
        {
            JReportFrame jrf = (JReportFrame)frames.get(i);
            try {
                 saveAs( jrf );
            } catch (Exception ex)
            {};
        }

    }//GEN-LAST:event_jMenuItemSaveAsFromListActionPerformed

    protected void jMenuItemSaveFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveFromListActionPerformed

        //JReportFrame jrf = getSelectedJRFFromFilesTree();
        //if (jrf != null) {
        //    save( jrf );
        //}

        List frames = getSelectedJRFsFromFilesTree();
        for (int i=0; i<frames.size(); ++i)
        {
            JReportFrame jrf = (JReportFrame)frames.get(i);
            try {
                 save( jrf );
            } catch (Exception ex)
            {};
        }

    }//GEN-LAST:event_jMenuItemSaveFromListActionPerformed

    private void jMenuItemRevertToSavedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRevertToSavedActionPerformed

        JReportFrame activeFrame = this.getActiveReportFrame();
        if (activeFrame != null) {
                        //
                        try {
                            if (activeFrame.getReport().getFilename() != null && new File(activeFrame.getReport().getFilename()).exists() )
                            {
                                String filename = activeFrame.getReport().getFilename();
                                activeFrame.getReport().setReportChanges(0);
                                jMDIMenuBar.closeFrame( activeFrame, true);
                                openFile(filename);
                            }
                        } catch (Exception ex)
                        {};
                        //dtn.getJrf().updateUI();
                        //jMDIDesktopPane.internalFrameActivated(dtn.getJrf());
                        //this.logOnConsole("Activated :"+dtn.getJrf().getReport().getFilename()+"\n");
                    }
    }//GEN-LAST:event_jMenuItemRevertToSavedActionPerformed

    private void jMenuItemRevertToSavedFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRevertToSavedFromListActionPerformed

        List frames = getSelectedJRFsFromFilesTree();
        for (int i=0; i<frames.size(); ++i)
        {
            JReportFrame jrf = (JReportFrame)frames.get(i);
            try {
                 if (jrf != null && jrf.getReport().getFilename() != null && new File(jrf.getReport().getFilename()).exists())
                 {
                    String filename = jrf.getReport().getFilename();
                    jrf.getReport().setReportChanges(0);
                    jMDIMenuBar.closeFrame( jrf, true);
                    openFile(filename);
                 }
            } catch (Exception ex)
            {};
        }

        //JReportFrame jrf = getSelectedJRFFromFilesTree();
        //if (jrf != null && jrf.getReport().getFilename() != null && new File(jrf.getReport().getFilename()).exists())
        //{
        //    String filename = jrf.getReport().getFilename();
        //    jrf.getReport().setReportChanges(0);
        //    jMDIMenuBar.closeFrame( jrf, true);
        //    openFile(filename);
        //}


    }//GEN-LAST:event_jMenuItemRevertToSavedFromListActionPerformed

    private void jMenuItemCloseAllExceptThisFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseAllExceptThisFromListActionPerformed

        if (this.getActiveReportFrame() == null) return;
        JInternalFrame activeF = this.getActiveReportFrame();

        JReportFrame jrf = getSelectedJRFFromFilesTree();

        if (jrf != null) {
            //
            try {
                activeF = jrf;
            } catch (Exception ex)
            {};
            //dtn.getJrf().updateUI();
            //jMDIDesktopPane.internalFrameActivated(dtn.getJrf());
            //this.logOnConsole("Activated :"+dtn.getJrf().getReport().getFilename()+"\n");
        }


        JInternalFrame[] frames = this.jMDIDesktopPane.getAllFrames();
        for (int i=0; i<frames.length; ++i)
        {
            if (frames[i] != activeF)
            {
                jMDIMenuBar.closeFrame( frames[i] );
            }
        }

    }//GEN-LAST:event_jMenuItemCloseAllExceptThisFromListActionPerformed

    private void jMenuItemCloseAllFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseAllFromListActionPerformed
        jMenuItemCloseAllActionPerformed( evt );
    }//GEN-LAST:event_jMenuItemCloseAllFromListActionPerformed

    private void jMenuItemCloseFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseFromListActionPerformed

        List frames = getSelectedJRFsFromFilesTree();
        for (int i=0; i<frames.size(); ++i)
        {
            JReportFrame jrf = (JReportFrame)frames.get(i);
            try {
                jMDIMenuBar.closeFrame(jrf );
            } catch (Exception ex)
            {};
        }
    }//GEN-LAST:event_jMenuItemCloseFromListActionPerformed

    private void jMenuItemCloseAllExceptThisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseAllExceptThisActionPerformed

        if (this.getActiveReportFrame() == null) return;
        JInternalFrame activeF = this.getActiveReportFrame();
        JInternalFrame[] frames = this.jMDIDesktopPane.getAllFrames();
        for (int i=0; i<frames.length; ++i)
        {
            if (frames[i] != activeF)
            {
                jMDIMenuBar.closeFrame( frames[i] );
            }
        }
    }//GEN-LAST:event_jMenuItemCloseAllExceptThisActionPerformed

    private void jMenuItemCloseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseAllActionPerformed

        JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int i=0; i<frames.length; ++i)
        {
            if (frames[i] instanceof JReportFrame)
            {
                jMDIMenuBar.closeFrame( frames[i]);
            }
        }


    }//GEN-LAST:event_jMenuItemCloseAllActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed

        jMDIMenuBar.closeFrame( this.getActiveReportFrame() );

    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemExportOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportOptionsActionPerformed
        it.businesslogic.ireport.gui.export.ExportPreferencesDialog epf = new it.businesslogic.ireport.gui.export.ExportPreferencesDialog(this, true);
        epf.setVisible(true);

    }//GEN-LAST:event_jMenuItemExportOptionsActionPerformed

    private void jMenuItemClasspathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClasspathActionPerformed

        ClassPathDialog cpd = new ClassPathDialog(this, true);
        cpd.setClasspath( getClasspath() );
        cpd.setVisible(true);
        if (cpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION )
        {
            setClasspath( cpd.getClasspath() );
        }

    }//GEN-LAST:event_jMenuItemClasspathActionPerformed

    private void jMenuItemCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCompileActionPerformed

        jButtonCompilerActionPerformed( evt );

    }//GEN-LAST:event_jMenuItemCompileActionPerformed

    private void jMenuItemCompileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jMenuItemCompileItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemCompileItemStateChanged

    private void jMenuItemRemoveMarginsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRemoveMarginsActionPerformed

        JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

        if (jrf == null) return;

        UndoOperation uo = new RemoveMarginsOperation(jrf,
                        jrf.getReport().getTopMargin(),
                        jrf.getReport().getBottomMargin(),
                        jrf.getReport().getLeftMargin(),
                        jrf.getReport().getRightMargin());
        uo.redo();

        jrf.addUndoOperation( uo );
    }//GEN-LAST:event_jMenuItemRemoveMarginsActionPerformed

    private void jMenuItemReportLocaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportLocaleActionPerformed

        //it.businesslogic.ireport.gui.locale.NewLocaleFileDialog fd = new it.businesslogic.ireport.gui.locale.NewLocaleFileDialog(this,true);
        it.businesslogic.ireport.gui.locale.LocaleSelectorDialog fd = new it.businesslogic.ireport.gui.locale.LocaleSelectorDialog(this, true);

        String s = this.getProperties().getProperty("reportLocale");
        if (s != null)
        {
            fd.setSelectedLocaleId( s );
        }
        fd.setVisible(true);

        if (fd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            //String reportLocale = fd.getText();
            String reportLocale = fd.getSelectedLocaleId();
            if (reportLocale == null) reportLocale = "";
            this.getProperties().setProperty("reportLocale", reportLocale);
            saveiReportConfiguration();
            updateJMenuItemReportLocaleText();
        }


    }//GEN-LAST:event_jMenuItemReportLocaleActionPerformed

    public void updateJMenuItemReportLocaleText()
    {
    	Locale loc = Misc.getLocaleFromString( this.getProperties().getProperty("reportLocale"));
    	jMenuItemReportLocale.setText(it.businesslogic.ireport.util.I18n.getString("setReportLocale", "Set report locale -" ) + " " + loc.getDisplayName() );
    }
    public void updateJMenuItemReportTimeZone()
    {
        String s = this.getProperties().getProperty("reportTimeZoneId");
        String timezoneName = "default";
        if (s != null && s.length() > 0)
        {
            try {
                TimeZone tz = TimeZone.getTimeZone(s);
                timezoneName = new TimeZoneWrapper( tz ) + "";
            } catch (Exception ex)
            {

            }
        }

    	jMenuItemReportTimeZone.setText(it.businesslogic.ireport.util.I18n.getString("setReportTimeZone", "Set report time zone -" ) + " " +  timezoneName);
    }



    public void updateJMenuItemReportMaxRecords()
    {
    	int maxRecords = 0;
        try {
            maxRecords = Integer.parseInt( this.getProperties().getProperty("maxRecords","0"));
        } catch (Exception ex)
        { }

        if (maxRecords > 0)
        {
            jMenuItemMaxRecords.setText(it.businesslogic.ireport.util.I18n.getFormattedString("maxRecords", "Max records ({0,number})", new Object[]{new Integer(maxRecords)})  );
        }
        else
        {
            jMenuItemMaxRecords.setText(it.businesslogic.ireport.util.I18n.getString("maxRecordsNotSet", "maxRecordsNotSet")  );
        }
    }


    private void jCheckBoxMenuItemEMMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemEMMActionPerformed

		getProperties().put("EMM",  ""+jCheckBoxMenuItemEMM.isSelected());
        	this.saveiReportConfiguration();

    }//GEN-LAST:event_jCheckBoxMenuItemEMMActionPerformed

    private void jMenuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHelpActionPerformed

        openUrl("http://jasperforge.org/sf/wiki/do/viewPage/projects.ireport/wiki/HomePage" );

    }//GEN-LAST:event_jMenuItemHelpActionPerformed

    public void openUrl(String url)
    {
        Misc.openURL(url);
        /*
        String operatingSystem = System.getProperty("os.name");


            try {
                 if (operatingSystem.toLowerCase().indexOf("windows") > -1)
                 {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                 }
                 else
                 {
                        // Try with htmlview...
                        Runtime.getRuntime().exec("htmlview " + url + " &");
                 }
            } catch (Exception ex)
            {
                javax.swing.JOptionPane.showMessageDialog( this,"Unable to open " + url, "", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
         */
    }

    private void jListThreadsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListThreadsMouseClicked
        if (evt.getButton() == evt.BUTTON3) {

            this.jPopupMenuThreads.show(jListThreads, evt.getPoint().x, evt.getPoint().y);
        }
    }//GEN-LAST:event_jListThreadsMouseClicked

    private void jMenuItemKillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemKillActionPerformed

        Object obj = this.jListThreads.getSelectedValue();
        if (obj != null)
        {
            IReportCompiler ic = (IReportCompiler)obj;
            ic.stopThread();
        }

    }//GEN-LAST:event_jMenuItemKillActionPerformed

    private void jMenuLocaleFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLocaleFilesActionPerformed
        this.getLocaleFilesDialog().setVisible(true);
    }//GEN-LAST:event_jMenuLocaleFilesActionPerformed

    private void jMenuItemReportImportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportImportsActionPerformed
       JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

       ReportImportsDialog rsd = new ReportImportsDialog(this, true);
       rsd.setJReportFrame( jrf );
       rsd.setVisible(true);


    }//GEN-LAST:event_jMenuItemReportImportsActionPerformed

    private void jButtonAlignBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignBottomActionPerformed
        setElementsValue("setVerticalAlign", "Bottom");
    }//GEN-LAST:event_jButtonAlignBottomActionPerformed

    private void jButtonAlignMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignMiddleActionPerformed
         setElementsValue("setVerticalAlign", "Middle");
    }//GEN-LAST:event_jButtonAlignMiddleActionPerformed

    private void jButtonAlignTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignTopActionPerformed
        setElementsValue("setVerticalAlign", "Top");
    }//GEN-LAST:event_jButtonAlignTopActionPerformed

    private void jButtonFormatCenterVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormatCenterVActionPerformed
        jMenuItemAlignVerticalAxisActionPerformed(evt);
    }//GEN-LAST:event_jButtonFormatCenterVActionPerformed

    private void jButtonFormatCenterHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormatCenterHActionPerformed
        jMenuItemAlignHorizontalAxisActionPerformed(evt);
    }//GEN-LAST:event_jButtonFormatCenterHActionPerformed

    private void jButtonStrikethroughtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStrikethroughtActionPerformed
        JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        if (jrf == null) return;
        // Set the new value for all selected elements...
        boolean bool = false;
        Enumeration e = jrf.getSelectedElements().elements();
        while (e.hasMoreElements()) {
            ReportElement repele = (ReportElement)e.nextElement();
            if (repele instanceof TextReportElement)
            {
                TextReportElement element = (TextReportElement)repele;
                bool = ((TextReportElement)repele).isStrikeTrought();
                break;
            }
        }

        setElementsValue("setStrikeTrought", new Boolean(!bool), Boolean.TYPE);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jButtonStrikethroughtActionPerformed

    private void jButtonUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUnderlineActionPerformed

        JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        if (jrf == null) return;
        // Set the new value for all selected elements...
        boolean bool = false;
        Enumeration e = jrf.getSelectedElements().elements();
        while (e.hasMoreElements()) {
            ReportElement repele = (ReportElement)e.nextElement();
            if (repele instanceof TextReportElement)
            {
                TextReportElement element = (TextReportElement)repele;
                bool = ((TextReportElement)repele).isUnderline();
                break;
            }
        }

        setElementsValue("setUnderline", new Boolean(!bool), Boolean.TYPE);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jButtonUnderlineActionPerformed

    private void jButtonItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItalicActionPerformed
       JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        if (jrf == null) return;
        // Set the new value for all selected elements...
        boolean bool = false;
        Enumeration e = jrf.getSelectedElements().elements();
        while (e.hasMoreElements()) {
            ReportElement repele = (ReportElement)e.nextElement();
            if (repele instanceof TextReportElement)
            {
                TextReportElement element = (TextReportElement)repele;
                bool = ((TextReportElement)repele).isItalic();
                break;
            }
        }

        setElementsValue("setItalic", new Boolean(!bool), Boolean.TYPE);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jButtonItalicActionPerformed

    private void jButtonBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoldActionPerformed

        JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        if (jrf == null) return;
        // Set the new value for all selected elements...
        boolean bool = false;
        Enumeration e = jrf.getSelectedElements().elements();
        while (e.hasMoreElements()) {
            ReportElement repele = (ReportElement)e.nextElement();
            if (repele instanceof TextReportElement)
            {
                TextReportElement element = (TextReportElement)repele;
                bool = ((TextReportElement)repele).isBold();
                break;
            }
        }

        setElementsValue("setBold", new Boolean(!bool), Boolean.TYPE);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jButtonBoldActionPerformed

    private void jButtonDecreaseSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecreaseSizeActionPerformed
        setElementsValue("modifyFontSize", new Integer(-2), Integer.TYPE);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jButtonDecreaseSizeActionPerformed

    private void jButtonIncreaseSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIncreaseSizeActionPerformed

       setElementsValue("modifyFontSize", new Integer(2), Integer.TYPE);

    }//GEN-LAST:event_jButtonIncreaseSizeActionPerformed

    private void jComboBoxFontItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxFontItemStateChanged
        if (!this.isDontHandleEvent() && jComboBoxFont.getSelectedItem() != null)
        {
            setElementsValue("setFontName", ""+ jComboBoxFont.getSelectedItem());
        }
    }//GEN-LAST:event_jComboBoxFontItemStateChanged

    private void jNumberComboBoxSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeItemStateChanged
        if (!this.isDontHandleEvent())
        {
            setElementsValue("setFontSize", new Integer((int)jNumberComboBoxSize.getValue()), Integer.TYPE);
            //this.getElementPropertiesDialog().updateSelection();
        }
    }//GEN-LAST:event_jNumberComboBoxSizeItemStateChanged

    private void jNumberComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeActionPerformed
        if (!this.isDontHandleEvent())
        {
            setElementsValue("setFontSize", new Integer((int)jNumberComboBoxSize.getValue()), Integer.TYPE);
        }
    }//GEN-LAST:event_jNumberComboBoxSizeActionPerformed

    private void jCheckBoxMenuItemElementsFormattingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemElementsFormattingActionPerformed
        jToolBarFormat.setVisible( jCheckBoxMenuItemElementsFormatting.isSelected());
        getProperties().put("toolbarFormattingVisible",  ""+jCheckBoxMenuItemElementsFormatting.isSelected());
        this.saveiReportConfiguration();
    }//GEN-LAST:event_jCheckBoxMenuItemElementsFormattingActionPerformed

    private void jCheckBoxMenuItemTextFormattingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemTextFormattingActionPerformed
        jToolBarText.setVisible( jCheckBoxMenuItemTextFormatting.isSelected());
        getProperties().put("toolbarTextVisible",  ""+jCheckBoxMenuItemTextFormatting.isSelected());
        this.saveiReportConfiguration();
    }//GEN-LAST:event_jCheckBoxMenuItemTextFormattingActionPerformed

    private void jButtonAlignRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignRightActionPerformed
        setElementsValue("setAlign", "Right");
    }//GEN-LAST:event_jButtonAlignRightActionPerformed

    private void jButtonAlignCenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignCenterActionPerformed
        setElementsValue("setAlign", "Center");
    }//GEN-LAST:event_jButtonAlignCenterActionPerformed

    private void jButtonAlignJustifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignJustifyActionPerformed
        setElementsValue("setAlign", "Justified");
    }//GEN-LAST:event_jButtonAlignJustifyActionPerformed

    private void jButtonAlignLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlignLeftActionPerformed
        setElementsValue("setAlign", "Left");
    }//GEN-LAST:event_jButtonAlignLeftActionPerformed

    public void jMenuItemOrganizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOrganizeActionPerformed
        FormatCommand.getCommand( OperationType.ORGANIZE_AS_A_TABLE ).execute();
    }//GEN-LAST:event_jMenuItemOrganizeActionPerformed

    public void jMenuItemLeftMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLeftMarginActionPerformed
        FormatCommand.getCommand( OperationType.ALIGN_TO_LEFT_MARGIN).execute();
    }//GEN-LAST:event_jMenuItemLeftMarginActionPerformed

    public void jMenuItemRightMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRightMarginActionPerformed
        FormatCommand.getCommand( OperationType.ALIGN_TO_RIGHT_MARGIN).execute();
    }//GEN-LAST:event_jMenuItemRightMarginActionPerformed

    private void jNumberComboBoxZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberComboBoxZoomActionPerformed
        //System.out.println( "Zoom: " + jNumberComboBoxZoom.getValue());
    }//GEN-LAST:event_jNumberComboBoxZoomActionPerformed

    private void jMenuItemPluginConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPluginConfigActionPerformed
        getPluginConfigurationDialog().setVisible(true);
    }//GEN-LAST:event_jMenuItemPluginConfigActionPerformed

    private void jScrollPaneFilesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPaneFilesMousePressed

    }//GEN-LAST:event_jScrollPaneFilesMousePressed

    private void jTreeFilesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeFilesMousePressed
        if (evt.getButton() == evt.BUTTON3) {

            Misc.ensurePathIsSelected(jTreeFiles.getPathForLocation(evt.getX(), evt.getY() ), jTreeFiles);
            List lst = getSelectedJRFsFromFilesTree();
            if (lst != null && lst.size() > 0) {
                    this.jMenuItemCloseAllExceptThisFromList.setEnabled( lst.size() == 1 );
                    this.jPopupMenuFiles.show(jTreeFiles, evt.getPoint().x, evt.getPoint().y);
                }
        }
    }//GEN-LAST:event_jTreeFilesMousePressed

    private void jRadioButtonMenuItemPreviewTXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewTXTActionPerformed
        this.setReportViewer(IREPORT_TXT_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewTXTActionPerformed

	private void jToggleButtonBarcodeToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonBarcodeToolItemStateChanged
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                if (jMDIDesktopPane.getSelectedFrame() != null &&
                jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                    JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                    jrf.setNewObjectType( ReportElementType.BARCODE_ELEMENT);
                    jrf.setLensMode(false);
                }
                setActiveTool(jToggleButtonBarcodeTool);
            }
	}//GEN-LAST:event_jToggleButtonBarcodeToolItemStateChanged

	private void jToggleButtonBarcodeToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonBarcodeToolActionPerformed
            jToggleButtonBarcodeTool.setSelected(true);
	}//GEN-LAST:event_jToggleButtonBarcodeToolActionPerformed

    private void jMenuItemChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChartActionPerformed
        jToggleButtonChartToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonChartTool,0,jToggleButtonChartTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemChartActionPerformed

    private void jMenuItemImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImageActionPerformed
        jToggleButtonImageToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonImageTool,0,jToggleButtonImageTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemImageActionPerformed

    private void jToggleButtonChartToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonChartToolActionPerformed
        jToggleButtonChartTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonChartToolActionPerformed

    private void jMenuItemScriptletEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemScriptletEditorActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            if (jrf == null) return;

            if (jrf.getReport().getScriptletHandling() != Report.SCRIPTLET_IREPORT_INTERNAL_HANDLING ) {
                if (JOptionPane.showConfirmDialog(this,
                I18n.getString("messages.scriptletEditor","To use the scriptlet editor, you must leave iReport to handle the scriptlet class code internally.\n"+
                "You can set how handle scriptlet in report properties form.\n\nDo you want activate this option now?"),
                I18n.getString("messages.scriptletEditorTitle","Scriptlet editor"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) != JOptionPane.OK_OPTION) {
                    return;
                }

                jrf.getReport().setScriptletHandling( Report.SCRIPTLET_IREPORT_INTERNAL_HANDLING );
            }

            this.getEventsForm().setVisible(true);
            this.getEventsForm().setJReportFrame( jrf );
        }
    }//GEN-LAST:event_jMenuItemScriptletEditorActionPerformed

    private void jMenuItemPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPropertiesActionPerformed

        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            DatasetPropertiesDialog pd = new DatasetPropertiesDialog(this, true);
            pd.setDataset( jrf.getReport() );
            pd.updateProperties();

            pd.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItemPropertiesActionPerformed

   private void jRadioButtonMenuItemPreviewCSVActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItemPreviewCSVActionPerformed
   {//GEN-HEADEREND:event_jRadioButtonMenuItemPreviewCSVActionPerformed
       this.setReportViewer(IREPORT_CSV_VIEWER);
   }//GEN-LAST:event_jRadioButtonMenuItemPreviewCSVActionPerformed

   private void jRadioButtonMenuItemPreviewXLSActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItemPreviewXLSActionPerformed
   {//GEN-HEADEREND:event_jRadioButtonMenuItemPreviewXLSActionPerformed
       this.setReportViewer(IREPORT_XLS_VIEWER);
   }//GEN-LAST:event_jRadioButtonMenuItemPreviewXLSActionPerformed

   private void jRadioButtonMenuItemPreviewJAVAActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItemPreviewJAVAActionPerformed
   {//GEN-HEADEREND:event_jRadioButtonMenuItemPreviewJAVAActionPerformed
       this.setReportViewer(IREPORT_JAVA_VIEWER);
   }//GEN-LAST:event_jRadioButtonMenuItemPreviewJAVAActionPerformed

   private void jRadioButtonMenuItemPreviewHTMLActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItemPreviewHTMLActionPerformed
   {//GEN-HEADEREND:event_jRadioButtonMenuItemPreviewHTMLActionPerformed
       this.setReportViewer(IREPORT_HTML_VIEWER);
   }//GEN-LAST:event_jRadioButtonMenuItemPreviewHTMLActionPerformed

   private void jMenuItemQuitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemQuitActionPerformed
   {//GEN-HEADEREND:event_jMenuItemQuitActionPerformed
       this.closeApplication();
   }//GEN-LAST:event_jMenuItemQuitActionPerformed

   public void paint(java.awt.Graphics g)
   {
       try {
                   super.paint(g);
        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }
   }

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // Checking for documents update outside iReport...

        if (!catchFormActivated) return;


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame.this.run();
                } catch (Throwable ex)
                {
                    ex.printStackTrace();
                }
            }
          } );
//        if (checkModifiedFielsThread == null || !checkModifiedFielsThread.isAlive()) {
//            checkModifiedFielsThread= new Thread( this );
//
//            checkModifiedFielsThread.start();
            //run();
//        }
    }//GEN-LAST:event_formWindowActivated


    public void openFiles(java.util.List fileList)
    {
            java.util.Iterator iterator = fileList.iterator();
            while (iterator.hasNext())
            {
              java.io.File file = (java.io.File)iterator.next();
              if (file.getName().toLowerCase().endsWith(".jrxml") ||
                  file.getName().toLowerCase().endsWith(".jasper"))
              {
                openFile(file);
              }
            }
    }

    public JReportFrame openFile(String filename){

        return openFile(new File(filename));
    }

    public JReportFrame openFile(File file){

        JReportFrame reportFrame;
        reportFrame = findReportFrameByFile(file);


        if( reportFrame == null ){
            try {
                Report report = new Report(file.getPath());
                report.setUsingMultiLineExpressions( false ); //this.isUsingMultiLineExpressions());
                reportFrame = openNewReportWindow( report );
                report.addReportDocumentStatusChangedListener( this );
                setActiveReportForm(reportFrame);
            } catch (Exception e ) {
                e.printStackTrace();
                getLogPane().getMainLogTextArea().logOnConsole(
                    e.getMessage() + "\n",
                    JOptionPane.ERROR_MESSAGE);
            }

        } else {

            try {
            setActiveReportForm(reportFrame);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        return reportFrame;
    }

    /**
     * openFromStream
     * opens a new Report from an InputStream
     *
     * Author: Felix Firgau
     *
     * @param inputStream InputStream
     * @return JReportFrame
     */
    public JReportFrame openFromStream(java.io.InputStream inputStream)
    {
        try {
            JReportFrame reportFrame ;
            Report report = new Report () ;
            ReportReader rr = new ReportReader ( report ) ;
            rr.readFromStream ( inputStream ) ;

            report.setUsingMultiLineExpressions ( false ) ; //this.isUsingMultiLineExpressions());
            reportFrame = openNewReportWindow ( report ) ;
            report.addReportDocumentStatusChangedListener ( this ) ;
            setActiveReportForm ( reportFrame ) ;

            return reportFrame;
        }
        catch ( IOException ex ) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Test if the passed string is empty.
     * @param str
     * @return Returns true if empty.
     */
    private boolean isEmptyString(String str){
        return (str == null || str.trim().length() == 0);
    }

    /**
     * Search for a opened file in the iReport Designer.
     *
     * @param file File to search.
     * @return The JReportFrame.
     */
    private JReportFrame findReportFrameByFile(File fileToSearch){

        if(fileToSearch != null){

            javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
            JReportFrame jrf;
            String  frameFilename;

            for (int i=0; i< frames.length; ++i) {
                if (frames[i]  instanceof JReportFrame) {
                    jrf = (JReportFrame)frames[i];

                    frameFilename = jrf.getReport().getFilename();
                    if (!isEmptyString(frameFilename) && fileToSearch.equals(new File(frameFilename))) {
                        return jrf;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Search for a opened file in the iReport Designer.
     *
     * @param filename Filename to find.
     * @return The JReportFrame.
     */
    private JReportFrame findReportFrameByFilename(String filename){

        if(!isEmptyString(filename)){
            return findReportFrameByFile(new File(filename));
        }

        return null;
    }


    public void checkForModifiedFiles() {


        javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        String filename;
        JReportFrame jrf;
        long time;
        Report report;
        int res;

        try {
        for (int i=0; i< frames.length; ++i) {

            if (frames[i]  instanceof JReportFrame) {

                jrf = (JReportFrame)frames[i];
                filename = jrf.getReport().getFilename();

                if ( !isEmptyString(filename) ) {

                    time = Misc.getLastWriteTime(filename);

                    if (jrf.getReport().getLoadTime() < time ) {

                        //System.out.println("Trovato: "+filename);
                        res = javax.swing.JOptionPane.CANCEL_OPTION;

                        if (getProperties().getProperty("AutoReload","false").equals("true"))
                        {
                            res = javax.swing.JOptionPane.OK_OPTION;
                        }
                        else
                        {
                            res = javax.swing.JOptionPane.showConfirmDialog(this,
                                    I18n.getFormattedString("messages.fileModifiedOutsideIReport",
                                    "{0} \n\nThis file has been modified outside iReport.Do you want reload it?", new Object[]{filename}),
                                    "iReport",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }

                        if (res == javax.swing.JOptionPane.OK_OPTION) {

                            //setCurrentZoomFactor( jrf.getZoomFactor() );
                            int positionInList = 0;
                            DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.jTreeFiles.getModel().getRoot();
                            for (positionInList=0; positionInList<root.getChildCount(); ++positionInList)
                            {
                                DefaultMutableTreeNode documentEntryNode = (DefaultMutableTreeNode)root.getChildAt(positionInList);
                                DocumentTreeEntry dte = (DocumentTreeEntry)documentEntryNode.getUserObject();
                                if (dte.getJrf() == jrf)
                                {
                                    break;
                                }
                            }

                            jrf.setDefaultCloseOperation( jrf.DISPOSE_ON_CLOSE );
                            jrf.doDefaultCloseAction();
                            openFile(filename);


                            if (positionInList < root.getChildCount()-1)
                            {
                                // Move the last entry to the correct position...
                                DefaultMutableTreeNode documentEntryNode = (DefaultMutableTreeNode)root.getChildAt( root.getChildCount()-1 );
                                root.remove(documentEntryNode);
                                root.insert(documentEntryNode, positionInList);
                                jTreeFiles.updateUI();
                            }
                            //setCurrentZoomFactor( 1.0 ); //reset

                        } else
                        {
                            jrf.getReport().setLoadTime(time);
                        }
                    }else if (time < 0) //File removed!
                    {
                            //jrf.getReport().setLoadTime( new java.util.Date().getTime() );
                            jrf.getReport().incrementReportChanges();
                    }
                }
            }
        }
        } catch (Throwable  ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     *Used to determine if a save all is required.
     */
    public boolean isSaveAllRequired() {
        boolean ret = false;
        javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int i=0; i< frames.length; ++i) {
            if (frames[i]  instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)frames[i];
                Report checkMe = jrf.getReport();
                if( checkMe == null ) {
                    ret = ( ret || false );
                }
                else {
                    ret = ( ret || checkMe.isModified() );
                }
            }//end if instance of JReportFrame
        }//end for frames
        return ret;
    }//end isSaveAllRequired

    private void jMenuItemXMLSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemXMLSourceActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            if (jrf.getReport().getFilename() == null ||
            jrf.getReport().getFilename().equals("")) {
                int res = javax.swing.JOptionPane.showConfirmDialog(this,
                        I18n.getString("messages.mustSaveFile",
                        "You must save your file before editing it with an external editor!\nSave it now ?"),"",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                if (res == javax.swing.JOptionPane.OK_OPTION) {
                    jMenuItemSaveActionPerformed(evt);
                }
                else
                    return;
            }
            else {
                int res = javax.swing.JOptionPane.OK_OPTION;
                if (jrf.getReport().isModified())
                {
                    res = javax.swing.JOptionPane.showConfirmDialog(this,
                            I18n.getString("messages.fileModifiedLaunchingEditor",
                            "The document has been modified!\nWould you like to save it before launching the external editor?"),"",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
                if (res == javax.swing.JOptionPane.OK_OPTION) {
                    jMenuItemSaveActionPerformed(evt);
                }
            }
            if (jrf.getReport().getFilename() == null ||
            jrf.getReport().getFilename().equals("")) return;

            Runtime rt = Runtime.getRuntime();
            String editor = "notepad.exe";
            try {
                if (this.getProperties().getProperty("ExternalEditor")!=null)
                    editor = (String)this.getProperties().getProperty("ExternalEditor");
                if (editor == null || editor.equals("")) {

                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.useNotepad", "Using notepad.exe as default editor!\n"),
                    JOptionPane.WARNING_MESSAGE);

                    editor = "notepad.exe";
                }

                // WRONG ON UNIX: rt.exec(editor+ " \"" +jrf.getReport().getFilename()+"\"");
                // String tokenizer wasn't parsing the command and arguments
		// properly, so pass them in as separate elements.
		rt.exec(new String[] { editor, jrf.getReport().getFilename() });
            } catch (Exception ex) {

                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getFormattedString("messages.errorExecutingEditor",
                        "An exception is occured executing:\n{0} \"{1}\"",new Object[]{editor,jrf.getReport().getFilename()}) ,"",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItemXMLSourceActionPerformed

    public void jMenuItemVSRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVSRemoveActionPerformed
        //formatMenuPerformed( OperationType.REMOVE_SPACE_V);
        FormatCommand.getCommand(OperationType.REMOVE_SPACE_V ).execute();
    }//GEN-LAST:event_jMenuItemVSRemoveActionPerformed

    public void jMenuItemVSDecreaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVSDecreaseActionPerformed
         FormatCommand.getCommand(OperationType.DECREASE_SPACE_V).execute();
    }//GEN-LAST:event_jMenuItemVSDecreaseActionPerformed

    public void jMenuItemVSMakeEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVSMakeEqualActionPerformed
         FormatCommand.getCommand(OperationType.EQUALS_SPACE_V).execute();
    }//GEN-LAST:event_jMenuItemVSMakeEqualActionPerformed

    public void jMenuItemHSRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHSRemoveActionPerformed
         FormatCommand.getCommand(OperationType.REMOVE_SPACE_H).execute();
    }//GEN-LAST:event_jMenuItemHSRemoveActionPerformed

    public void jMenuItemHSIncreaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHSIncreaseActionPerformed
         FormatCommand.getCommand(OperationType.INCREASE_SPACE_H).execute();
    }//GEN-LAST:event_jMenuItemHSIncreaseActionPerformed

    public void jMenuItemHSMakeEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHSMakeEqualActionPerformed
         FormatCommand.getCommand(OperationType.EQUALS_SPACE_H).execute();
    }//GEN-LAST:event_jMenuItemHSMakeEqualActionPerformed

    public void jMenuItemJoinRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemJoinRightActionPerformed
         FormatCommand.getCommand(OperationType.JOIN_RIGHT).execute();
    }//GEN-LAST:event_jMenuItemJoinRightActionPerformed

    public void jMenuItemJoinLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemJoinLeftActionPerformed
         FormatCommand.getCommand(OperationType.JOIN_LEFT).execute();
    }//GEN-LAST:event_jMenuItemJoinLeftActionPerformed

    public void jMenuItemCenterBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCenterBackgroundActionPerformed
         FormatCommand.getCommand(OperationType.CENTER_IN_BACKGROUND).execute();
    }//GEN-LAST:event_jMenuItemCenterBackgroundActionPerformed

    public void jMenuItemCenterInBandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCenterInBandActionPerformed
         FormatCommand.getCommand(OperationType.CENTER_IN_BAND).execute();
    }//GEN-LAST:event_jMenuItemCenterInBandActionPerformed

    public void jMenuItemCenterVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCenterVActionPerformed
        FormatCommand.getCommand(OperationType.CENTER_IN_BAND_V).execute();
    }//GEN-LAST:event_jMenuItemCenterVActionPerformed

    public void jMenuItemCenterHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCenterHActionPerformed
        FormatCommand.getCommand(OperationType.CENTER_IN_BAND_H).execute();
    }//GEN-LAST:event_jMenuItemCenterHActionPerformed

    public void jMenuItemSameSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameSizeActionPerformed
        FormatCommand.getCommand(OperationType.SAME_SIZE).execute();
    }//GEN-LAST:event_jMenuItemSameSizeActionPerformed

    public void jMenuItemSameHeightMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameHeightMaxActionPerformed
        FormatCommand.getCommand(OperationType.SAME_HEIGHT_MAX).execute();
    }//GEN-LAST:event_jMenuItemSameHeightMaxActionPerformed

    public void jMenuItemSameHeightMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameHeightMinActionPerformed
        FormatCommand.getCommand(OperationType.SAME_HEIGHT_MIN).execute();
    }//GEN-LAST:event_jMenuItemSameHeightMinActionPerformed

    public void jMenuItemSameHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameHeightActionPerformed
        FormatCommand.getCommand(OperationType.SAME_HEIGHT).execute();
    }//GEN-LAST:event_jMenuItemSameHeightActionPerformed

    public void jMenuItemSameWidthMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameWidthMinActionPerformed
        FormatCommand.getCommand(OperationType.SAME_WIDTH_MIN).execute();
    }//GEN-LAST:event_jMenuItemSameWidthMinActionPerformed

    public void jMenuItemSameWidthMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameWidthMaxActionPerformed
        FormatCommand.getCommand(OperationType.SAME_WIDTH_MAX).execute();
    }//GEN-LAST:event_jMenuItemSameWidthMaxActionPerformed

    public void jMenuItemSameWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSameWidthActionPerformed
        FormatCommand.getCommand(OperationType.SAME_WIDTH).execute();
    }//GEN-LAST:event_jMenuItemSameWidthActionPerformed

    public void jMenuItemAlignToBandBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignToBandBottomActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_BOTTOM_TO_BAND).execute();
    }//GEN-LAST:event_jMenuItemAlignToBandBottomActionPerformed

    public void jMenuItemAlignToBandTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignToBandTopActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_TOP_TO_BAND).execute();
    }//GEN-LAST:event_jMenuItemAlignToBandTopActionPerformed

    public void jMenuItemAlignHorizontalAxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignHorizontalAxisActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_HORIZONTAL_AXIS).execute();
    }//GEN-LAST:event_jMenuItemAlignHorizontalAxisActionPerformed

    public void jMenuItemAlignVerticalAxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignVerticalAxisActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_VERTICAL_AXIS).execute();
    }//GEN-LAST:event_jMenuItemAlignVerticalAxisActionPerformed

    public void jMenuItemAlignBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignBottomActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_BOTTOM).execute();
    }//GEN-LAST:event_jMenuItemAlignBottomActionPerformed

    public void jMenuItemAlignTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignTopActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_TOP).execute();
    }//GEN-LAST:event_jMenuItemAlignTopActionPerformed

    public void jMenuItemAlignRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignRightActionPerformed
        FormatCommand.getCommand(OperationType.ALIGN_RIGHT).execute();

    }//GEN-LAST:event_jMenuItemAlignRightActionPerformed

    /*
    public void formatMenuPerformed(int operationType) {
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.makeOperation( operationType );
            if (jrf.getSelectedElements().size()>0)
                updateDocumentStructureTree(jrf);
        }
    }
    */

    public void jMenuItemAlignLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAlignLeftActionPerformed
        // Align all element to left....
        FormatCommand.getCommand(OperationType.ALIGN_LEFT).execute();
    }//GEN-LAST:event_jMenuItemAlignLeftActionPerformed

    private void jRadioButtonMenuItemPreviewInternalViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewInternalViewerActionPerformed
        this.setReportViewer(IREPORT_JASPER_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewInternalViewerActionPerformed

    public void jMenuItemSendToBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSendToBackActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.sendToBack();
            if (jrf.getSelectedElements().size()>0)
                getDocumentStructurePanel().updateDocumentStructureTree(jrf);
        }
    }//GEN-LAST:event_jMenuItemSendToBackActionPerformed

    public void jMenuItemBringToFrontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBringToFrontActionPerformed

        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.bringToFront();
            if (jrf.getSelectedElements().size()>0)
                getDocumentStructurePanel().updateDocumentStructureTree(jrf);
        }
    }//GEN-LAST:event_jMenuItemBringToFrontActionPerformed

    private void jMenuItemWizardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWizardActionPerformed

      newWizard();
    }//GEN-LAST:event_jMenuItemWizardActionPerformed

    public Report newWizard() {
        /*
        if (this.getProperties().get("DefaultConnection") == null ||
        !( this.getProperties().get("DefaultConnection") instanceof it.businesslogic.ireport.connection.JDBCConnection) ) {
            javax.swing.JOptionPane.showMessageDialog(this, "Before start the Wizard, you must configure a JDBC connection\nand set it the active connection.\n\nIf you have already created and tested a JDBC connection go to the menu\nbuild->set active connection and select your entry.\nIf you have not yet setup a JDBC connection go to\nDatasources->Connection/Datasources to create one.","",javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        */

        WizardDialog wd = new WizardDialog(this,true);
        wd.setVisible(true);
        if (wd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Report report = wd.getReport();
            if (report != null) {
            	// Modificacion para Geopista
                IReportParameterManager.addDefaultGeopistaParameters(report);
                // Fin de modificacion
                // Name the report...
                openNewReportWindow(report);
                
                return report;
            }
        }
      return null;
    }

    private void jMenuItemReportPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportPropertiesActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            ReportPropertiesFrame rpf = new ReportPropertiesFrame(this,true);
            rpf.setModal(true);

            rpf.setBottomMargin( jrf.getReport().getBottomMargin());

            //rpf.setGlobalUnit( jrf.getReport().getGlobalUnit());

            rpf.setLeftMargin(jrf.getReport().getLeftMargin());
            rpf.setOrientation( jrf.getReport().getOrientation());
            rpf.setPrintOrder( jrf.getReport().getPrintOrder());
            rpf.setReportFormat( jrf.getReport().getReportFormat());
            rpf.setReportHeight( jrf.getReport().getHeight());
            rpf.setReportName( jrf.getReport().getName());
            rpf.setReportWidth( jrf.getReport().getWidth());
            rpf.setRightMargin( jrf.getReport().getRightMargin());
            rpf.setScriptletClass( jrf.getReport().getScriptletClass());
            rpf.setScriptletHandling( jrf.getReport().getScriptletHandling() );
            rpf.setSummaryOnNewPage( jrf.getReport().isIsSummaryNewPage());
            rpf.setTitleOnNewPage(jrf.getReport().isIsTitleNewPage());
            rpf.setTopMargin( jrf.getReport().getTopMargin());
            rpf.setWhenNoDataType( jrf.getReport().getWhenNoDataType());
            rpf.setXmlEncoding( jrf.getReport().getEncoding());
            rpf.setFloatColumnFooter( jrf.getReport().isFloatColumnFooter());
            rpf.setResourceBundleBaseName( jrf.getReport().getResourceBundleBaseName() );
            rpf.setLanguage( jrf.getReport().getLanguage() );
            rpf.setWhenResourceMissingType( jrf.getReport().getWhenResourceMissingType());
            rpf.setIgnorePagination( jrf.getReport().isIgnorePagination());
            rpf.setFormatFactoryClass(jrf.getReport().getFormatFactoryClass());

            rpf.setColumns( jrf.getReport().getColumnCount());
            rpf.setColumnsSpacing( jrf.getReport().getColumnSpacing());
            rpf.setColumnsWidth( jrf.getReport().getColumnWidth());

            //System.out.println("Il nome :"+rpf.getReportName()+" (old)");
            rpf.setVisible(true);
            if (rpf.getDialogResult() == JOptionPane.OK_OPTION) {
                // Change report...
                jrf.getReport().incrementReportChanges();
                //javax.swing.JOptionPane.showMessageDialog(null,""+rpf.getReportName());
                // First all fields that don't modify layout...
                jrf.getReport().setWidth(rpf.getReportWidth());
                jrf.getReport().setHeight(rpf.getReportHeight());
                jrf.getReport().setOrientation(rpf.getOrientation());
                jrf.getReport().setName(rpf.getReportName());
                int oldTop = jrf.getReport().getTopMargin();
                jrf.getReport().setTopMargin(rpf.getTopMargin());
                int differenceY = jrf.getReport().getTopMargin() - oldTop;

                int oldLeft = jrf.getReport().getLeftMargin();
                jrf.getReport().setLeftMargin(rpf.getLeftMargin());
                int differenceX = jrf.getReport().getLeftMargin() - oldLeft;
                if (differenceX != 0 || differenceY != 0) {
                    Enumeration e = jrf.getReport().getElements().elements();
                    while (e.hasMoreElements()) {
                        ReportElement re = (ReportElement)e.nextElement();
                        re.trasform(new java.awt.Point(differenceX,differenceY), TransformationType.TRANSFORMATION_MOVE);
                    }
                }

                jrf.getReport().setLanguage(rpf.getLanguage());
                jrf.getReport().setRightMargin(rpf.getRightMargin());
                jrf.getReport().setBottomMargin(rpf.getBottomMargin());
                jrf.getReport().setColumnCount(rpf.getColumns());
                jrf.getReport().setColumnWidth(rpf.getColumnsWidth());
                jrf.getReport().setColumnSpacing(rpf.getColumnsSpacing());
                jrf.getReport().setIsSummaryNewPage(rpf.isSummaryOnNewPage());
                jrf.getReport().setIsTitleNewPage(rpf.isTitleOnNewPage());
                jrf.getReport().setWhenNoDataType(rpf.getWhenNoDataType());
                jrf.getReport().setScriptletClass(rpf.getScriptletClass());
                jrf.getReport().setEncoding(rpf.getXmlEncoding());
                jrf.getReport().setPrintOrder(rpf.getPrintOrder());
                jrf.getReport().setReportFormat(rpf.getReportFormat());
                jrf.getReport().setFloatColumnFooter(rpf.isFloatColumnFooter());
                jrf.getReport().setScriptletHandling( rpf.getScriptletHandling() );
                jrf.getReport().setResourceBundleBaseName( rpf.getResourceBundleBaseName() );
                jrf.getReport().setWhenResourceMissingType( rpf.getWhenResourceMissingType());
                jrf.getReport().setIgnorePagination(rpf.isIgnorePagination());
                jrf.getReport().setFormatFactoryClass(rpf.getFormatFactoryClass());

                this.setTitle(getRebrandedTitle() + " ["+ jrf.getReport().getName() + " "+ jrf.getReport().getWidth()+"x"+jrf.getReport().getHeight()+" "+jrf.getReport().getFilename()+" "+(jrf.getReport().isReadOnly()?"(READ ONLY) ":"")+"]");
                jrf.updateTitle();
                // Aggiorniamo il menu attivo...
                java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
                int frame = 0;
                for (int k = 0;  k<menus.length ; ++k) {
                    //javax.swing.JOptionPane.showMessageDialog(this,"Menu: " + menus[k] );


                    if (menus[k] instanceof JRadioButtonMenuItemMDIFrame) {
                        frame++;

                        JRadioButtonMenuItemMDIFrame mri = (JRadioButtonMenuItemMDIFrame)menus[k];
                        //((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() == jrf)
                        if ( mri.isSelected() ) {
                            //javax.swing.JOptionPane.showMessageDialog( this, "Selected" + frame +" "+text);
                            mri.setText(frame + ". " + jrf.getTitle());
                            break;
                        }
                    }
                }

                jrf.setIsDocDirty(true);
                jrf.updateScrollBars();
                jrf.getJPanelReport().repaint();
                if (getReportSheetPanel().getCurrentMode() == DocumentSheetPanel.SHOW_DOCUMENT_SHEET)
                {
                    getReportSheetPanel().getReportSheetPanel().updateSelection(jrf);
                }
            }
        }
    }//GEN-LAST:event_jMenuItemReportPropertiesActionPerformed

    private void jMenuItemGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGroupsActionPerformed
        jButtonGroupsActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemGroupsActionPerformed

    public void jMenuItemBandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBandsActionPerformed
        jButtonBandsActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemBandsActionPerformed

    private void jMenuItemElementPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementPropertiesActionPerformed
        this.getElementPropertiesDialog().setVisible(true);
        //this.getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jMenuItemElementPropertiesActionPerformed

    private void jMenuItemParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemParametersActionPerformed
        jButtonParametersActionPerformed(evt);
        this.getValuesDialog().gotoTab("Parameters");
    }//GEN-LAST:event_jMenuItemParametersActionPerformed

    private void jMenuItemVariablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVariablesActionPerformed
        jButtonParametersActionPerformed(evt);
        this.getValuesDialog().gotoTab("Variables");
    }//GEN-LAST:event_jMenuItemVariablesActionPerformed

    private void jMenuItemFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFieldsActionPerformed
        jButtonParametersActionPerformed(evt);
        this.getValuesDialog().gotoTab("Fields");
    }//GEN-LAST:event_jMenuItemFieldsActionPerformed

    private void jMenuItemReportQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportQueryActionPerformed
        jButtonDatabaseActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemReportQueryActionPerformed

    private void jMenuItemExecuteDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExecuteDBActionPerformed
        jButtonRun2ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemExecuteDBActionPerformed

    private void jMenuItemExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExecuteActionPerformed
        jButtonRun1ActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemExecuteActionPerformed

    private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed
        jMenuItemPasteActionPerformed(evt);
    }//GEN-LAST:event_jButtonPasteActionPerformed

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed
        jMenuItemCopyActionPerformed(evt);
    }//GEN-LAST:event_jButtonCopyActionPerformed

    private void jButtonCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCutActionPerformed
        jMenuItemCutActionPerformed(evt);
    }//GEN-LAST:event_jButtonCutActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.paste();
        }
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.copy();
            if (getClipBoard().size()>0) {
                this.jMenuItemPaste.setEnabled(true);
                this.jButtonPaste.setEnabled(true);
            }
            else {
                this.jMenuItemPaste.setEnabled(false);
                this.jButtonPaste.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            jrf.cut();
        }
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jButtonRun2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRun2ActionPerformed
      compileAndRun2();
    }//GEN-LAST:event_jButtonRun2ActionPerformed

    public IReportCompiler compileAndRun2()
    {
        // 1. Get active report...
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            if (jrf.getReport().getFilename() == null || jrf.getReport().getFilename().trim().equals("") ||
                    (Misc.nvl(this.getProperties().getProperty("SaveBeforeCompiling"), "true").equals("true") && jrf.getReport().isModified()))
            {
                jMenuItemSaveActionPerformed(new java.awt.event.ActionEvent(jMenuItemSave,0,"Save"));
                if (jrf.getReport().isModified())
                {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user"),
                    JOptionPane.WARNING_MESSAGE);
                }
            }
            //1. Save the report if needed....
            
            
            if( jrf.getReport().getFilename() == null ||
                jrf.getReport().getFilename().trim().equals("")) {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user"),
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }

            //clearConsole();
            //1. Compile....
            IReportCompiler ic = new IReportCompiler();

            ic.setThreadList( jListThreads );
            ic.setCommand( IReportCompiler.CMD_COMPILE | IReportCompiler.CMD_EXPORT);

            if (jrf.getReport().getScriptletHandling() == jrf.getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
                ic.setCommand( ic.getCommand()  | IReportCompiler.CMD_COMPILE_SCRIPTLET);
            }


            HashMap hm = new HashMap();
            hm.put( ic.USE_EMPTY_DATASOURCE, "false");

            if (this.getProperties().get("DefaultConnection") == null ) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            I18n.getString("messages.connectionNotConfigured",
                            "You must configure a JDBC connection\nand set it the active connection.\n\nIf you have already created and tested a JDBC connection go to the menu\nbuild->set active connection and select your entry.\nIf you have not yet setup a JDBC connection go to\nDatasources->Connection/Datasources to create one."),
                            "",javax.swing.JOptionPane.ERROR_MESSAGE);
                    return null;
            }

            IReportConnection connection = (IReportConnection)this.getProperties().get("DefaultConnection");


            //if (connection instanceof  it.businesslogic.ireport.connection.JDBCConnection)
            //{
            //    javax.swing.JOptionPane.showMessageDialog(null, ((it.businesslogic.ireport.connection.JDBCConnection)connection).getJDBCDriver());
            //}

            hm.put( ic.USE_CONNECTION, "true");
            hm.put( ic.CONNECTION, connection);
            hm.put( ic.OUTPUT_DIRECTORY, getTranslatedCompileDirectory());

            if (jrf.getReport().getLanguage().equals("groovy"))
            {
                hm.put( ic.COMPILER, "net.sf.jasperreports.compilers.JRGroovyCompiler");
            }

            ic.setProperties(hm);

            if (jRadioButtonMenuItemPreviewCSV.isSelected()) hm.put( ic.OUTPUT_FORMAT, "csv");
            else if (jRadioButtonMenuItemPreviewHTML.isSelected()) hm.put( ic.OUTPUT_FORMAT, "html");
            else if (jRadioButtonMenuItemPreviewXLS.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls");
            else if (jRadioButtonMenuItemPreviewXLS2.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls2");
            else if (jRadioButtonMenuItemPreviewJAVA.isSelected()) hm.put( ic.OUTPUT_FORMAT, "java2D");
            else if (jRadioButtonMenuItemPreviewInternalViewer.isSelected()) hm.put( ic.OUTPUT_FORMAT, "jrviewer");
            else if (jRadioButtonMenuItemPreviewTXT.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txt");
            else if (jRadioButtonMenuItemPreviewTXTJR.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txtjr");
            else if (jRadioButtonMenuItemPreviewRTF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "rtf");
            else if (jRadioButtonMenuItemPreviewODF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "odf");
            else
                hm.put( ic.OUTPUT_FORMAT, "pdf");

            ic.setProperties(hm);
            ic.setJrf(jrf);
            ic.setMainFrame(this);

            //Modificacion para geopista
            try {
            	String srcFileName = jrf.getReport().getFilename();
            	SourceTraceDigester digester = IReportCompiler.createDigester();
            	JasperDesign jd = IReportCompiler.loadJasperDesign( new FileInputStream(srcFileName) , digester);
            	JRReport report = jd;
            	MapImageFactory.init(jd);
            } catch (Exception e){
            	e.printStackTrace();
            }
            //Fin de modificacion
            
            ic.start();
            return ic;
        }
        return null;
    }

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed

        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            jrf.delete();
        }

    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItemRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRedoActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            jrf.redo();
            this.updateUndoMenu(jrf);


                if (jrf.getSelectedElements().size()>0)
                    getDocumentStructurePanel().updateDocumentStructureTree(jrf);
            }

    }//GEN-LAST:event_jMenuItemRedoActionPerformed

    private void jMenuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUndoActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            jrf.undo();
            this.updateUndoMenu(jrf);

            if (jrf.getSelectedElements().size()>0)
                getDocumentStructurePanel().updateDocumentStructureTree(jrf);
        }
    }//GEN-LAST:event_jMenuItemUndoActionPerformed

    private void jTreeFilesValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeFilesValueChanged
        // Select all selected elements...


    }//GEN-LAST:event_jTreeFilesValueChanged

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed

        IReportAbout ra = new IReportAbout(this, true);
        ra.setVisible(true);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jButtonRun1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRun1ActionPerformed
      compileAndRun1(evt != null && (evt.getModifiers() & evt.SHIFT_MASK) != 0);
    }//GEN-LAST:event_jButtonRun1ActionPerformed

    public IReportCompiler compileAndRun1() {
        return compileAndRun1(false);
    }

    public IReportCompiler compileAndRun1(boolean emptyRecord) {
        // 1. Get active report...
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            if (jrf.getReport().getFilename() == null || jrf.getReport().getFilename().trim().equals("") ||
                    (Misc.nvl(this.getProperties().getProperty("SaveBeforeCompiling"), "true").equals("true") && jrf.getReport().isModified()))
            {
                jMenuItemSaveActionPerformed(new java.awt.event.ActionEvent(jMenuItemSave,0,"Save"));
                if (jrf.getReport().isModified())
                {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user"),
                    JOptionPane.WARNING_MESSAGE);
                }
            }
            //1. Save the report if needed....
            
            
            if( jrf.getReport().getFilename() == null ||
                jrf.getReport().getFilename().trim().equals("")) {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user."),
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }

            //clearConsole();
            //1. Compile....
            IReportCompiler ic = new IReportCompiler();

            ic.setThreadList( jListThreads );
            ic.setCommand( IReportCompiler.CMD_COMPILE | IReportCompiler.CMD_EXPORT);

            if (jrf.getReport().getScriptletHandling() == jrf.getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
                ic.setCommand( ic.getCommand()  | IReportCompiler.CMD_COMPILE_SCRIPTLET);
            }

            HashMap hm = new HashMap();
            hm.put( ic.USE_EMPTY_DATASOURCE, "true");
            Integer records = new Integer(1);

            if (emptyRecord)
            {
                String s = null;

                while (true)
                {
                    s = JOptionPane.showInputDialog(this,
                            I18n.getString("messages.setRecordNumber","Set the number of empty records"),"1");

                    if (s == null) break;
                    try {
                        int recs = Integer.parseInt(s);
                        records = new Integer(recs);
                        break;
                    } catch (Exception ex)
                    {

                    }
                }
            }

            hm.put( ic.EMPTY_DATASOURCE_RECORDS, records);
            hm.put( ic.OUTPUT_DIRECTORY, getTranslatedCompileDirectory());

            if (jrf.getReport().getLanguage().equals("groovy"))
            {
                hm.put( ic.COMPILER, "net.sf.jasperreports.compilers.JRGroovyCompiler");
            }

            ic.setProperties(hm);

            if (jRadioButtonMenuItemPreviewCSV.isSelected()) hm.put( ic.OUTPUT_FORMAT, "csv");
            else if (jRadioButtonMenuItemPreviewHTML.isSelected()) hm.put( ic.OUTPUT_FORMAT, "html");
            else if (jRadioButtonMenuItemPreviewXLS.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls");
            else if (jRadioButtonMenuItemPreviewXLS2.isSelected()) hm.put( ic.OUTPUT_FORMAT, "xls2");
            else if (jRadioButtonMenuItemPreviewJAVA.isSelected()) hm.put( ic.OUTPUT_FORMAT, "java2D");
            else if (jRadioButtonMenuItemPreviewInternalViewer.isSelected()) hm.put( ic.OUTPUT_FORMAT, "jrviewer");
            else if (jRadioButtonMenuItemPreviewTXT.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txt");
            else if (jRadioButtonMenuItemPreviewTXTJR.isSelected()) hm.put( ic.OUTPUT_FORMAT, "txtjr");
            else if (jRadioButtonMenuItemPreviewRTF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "rtf");
            else if (jRadioButtonMenuItemPreviewODF.isSelected()) hm.put( ic.OUTPUT_FORMAT, "odf");
            else
                hm.put( ic.OUTPUT_FORMAT, "pdf");

            ic.setProperties(hm);
            ic.setJrf(jrf);
            ic.setMainFrame(this);

            ic.start();
            return ic;
        }
        return null;
    }

    private void jMenuItemPrevWinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPrevWinActionPerformed
        // Remove all menus...
        java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
        int firstFrame = -1;
        for (int k = 0;  k<menus.length ; ++k) {
            if (menus[k] instanceof JRadioButtonMenuItemMDIFrame) {
                if (firstFrame == -1) firstFrame = k;
                if ( ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() == jMDIDesktopPane.getSelectedFrame()) {
                    // The next if exists, is the right menu..
                    k--;
                    if (k>=firstFrame && menus[k] instanceof JRadioButtonMenuItemMDIFrame) {
                        try {
                            ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame().setSelected(true);
                        } catch (Exception ex)
                        {}
                    }
                    else if (menus[menus.length-1] instanceof JRadioButtonMenuItemMDIFrame) {
                        try {
                            ((JRadioButtonMenuItemMDIFrame)menus[menus.length-1]).getFrame().setSelected(true);
                        } catch (Exception ex)
                        {}
                    }
                    return;
                }
            }
        }
    }//GEN-LAST:event_jMenuItemPrevWinActionPerformed

    private void jMenuItemNextWinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNextWinActionPerformed


        // Remove all menus...
        java.awt.Component[] menus = this.jMenuWindow.getMenuComponents();
        int firstFrame = -1;
        for (int k = 0;  k<menus.length ; ++k) {
            if (menus[k] instanceof JRadioButtonMenuItemMDIFrame) {
                if (firstFrame == -1) firstFrame = k;
                if ( ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame() == jMDIDesktopPane.getSelectedFrame()) {
                    // The next if exists, is the right menu..
                    k++;
                    if (k<menus.length && menus[k] instanceof JRadioButtonMenuItemMDIFrame) {
                        try {
                            ((JRadioButtonMenuItemMDIFrame)menus[k]).getFrame().setSelected(true);
                        } catch (Exception ex)
                        {}
                    }
                    else if (firstFrame != -1) {
                        try {
                            ((JRadioButtonMenuItemMDIFrame)menus[firstFrame]).getFrame().setSelected(true);
                        } catch (Exception ex)
                        {}
                    }
                    return;
                }
            }
        }

        /*
        JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        if (frames.length < 2) return;
        for (int i=0; i<frames.length; ++i)
        {
            if (frames[i] == jMDIDesktopPane.getSelectedFrame())
            {
                i++;
                if (i == frames.length) i=0;
                try {
                frames[i].setSelected(true);
                } catch (Exception ex){}
                return;
            }
        }
         */

    }//GEN-LAST:event_jMenuItemNextWinActionPerformed

    private void jMenuItemtileAnodineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemtileAnodineActionPerformed
        jMDIDesktopPane.tileAnodine();
    }//GEN-LAST:event_jMenuItemtileAnodineActionPerformed

    private void jMenuItemTileVerticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTileVerticalActionPerformed
        jMDIDesktopPane.tileVertically();
    }//GEN-LAST:event_jMenuItemTileVerticalActionPerformed

    private void jMenuItemTileHorizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTileHorizontalActionPerformed
        jMDIDesktopPane.tileHorizontally();
    }//GEN-LAST:event_jMenuItemTileHorizontalActionPerformed

    private void jMenuItemCascadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCascadeActionPerformed
        // Dispose all frames in cascade....
        // The active frame must be the last....
        jMDIDesktopPane.cascade();
    }//GEN-LAST:event_jMenuItemCascadeActionPerformed

    private void jRadioButtonMenuItemPreviewPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemPreviewPDFActionPerformed
        this.setReportViewer(IREPORT_PDF_VIEWER);
    }//GEN-LAST:event_jRadioButtonMenuItemPreviewPDFActionPerformed

    private void jTreeFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeFilesMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            TreePath path = jTreeFiles.getSelectionPath();
            if (path == null) return;
            javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode)path.getLastPathComponent();
            if (node instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode nodem = (DefaultMutableTreeNode)node;
                Object obj = nodem.getUserObject();
                if (obj != null && obj instanceof DocumentTreeEntry) {
                    DocumentTreeEntry dtn = (DocumentTreeEntry)obj;
                    if (dtn.getJrf() != null) {
                        //
                        try {
                            dtn.getJrf().setSelected(true);
                        } catch (Exception ex)
                        {};
                        //dtn.getJrf().updateUI();
                        //jMDIDesktopPane.internalFrameActivated(dtn.getJrf());
                        //this.logOnConsole("Activated :"+dtn.getJrf().getReport().getFilename()+"\n");
                    }
                }
            }
        }
        //else System.out.println("Mouse clicked...");


    }//GEN-LAST:event_jTreeFilesMouseClicked

    private void jMenuItemFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFontsActionPerformed

         JOptionPane.showMessageDialog(this,it.businesslogic.ireport.util.I18n.getString("reportFontsDeprecated","Report fonts are deprecated.\nWe strongly suggest to use styles instead."),
        												I18n.getString("messages.reportFontsDeprecated.title","Report fonts"),
        												//"Report fonts",
        												JOptionPane.WARNING_MESSAGE);


        this.fontsDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItemFontsActionPerformed

    private void jMenuItemActiveConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemActiveConnectionActionPerformed
        ActiveConnectionDialog acd = new ActiveConnectionDialog(this, true);
        acd.setVisible(true);
        if (acd.getDialogResult() == JOptionPane.OK_OPTION) {
            this.getProperties().put( "DefaultConnection",acd.getIReportConnection());

            this.saveiReportConfiguration();
            jToolbarConnectionPanel.updateConnections();

        }
    }//GEN-LAST:event_jMenuItemActiveConnectionActionPerformed

    public void setActiveConnection(IReportConnection conn )
    {
        if (conn != null)
        {
            this.getProperties().put( "DefaultConnection",conn);
            this.saveiReportConfiguration();
            jToolbarConnectionPanel.updateConnections();
        }
    }

    private void jToggleButtonChartToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonChartToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.CHART_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonChartTool);
        }
    }//GEN-LAST:event_jToggleButtonChartToolItemStateChanged

    private void jMenuItemConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConnectionsActionPerformed

        try{
        ConnectionsDialog cd = new ConnectionsDialog(this,true);
        cd.setVisible(true);
        jToolbarConnectionPanel.updateConnections();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jMenuItemConnectionsActionPerformed

    private void jMenuItemReportQuery2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReportQuery2ActionPerformed
        this.jButtonDatabaseActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemReportQuery2ActionPerformed

    private void jMenuItemCompatibilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCompatibilityActionPerformed
        CompatibilityDialog cd = new CompatibilityDialog(this,true);
        cd.setVisible(true);
    }//GEN-LAST:event_jMenuItemCompatibilityActionPerformed

    private void jMenuItemOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOptionsActionPerformed
        OptionsDialog od = new OptionsDialog(this,true);
        od.loadConfiguration();
        od.setVisible(true);
    }//GEN-LAST:event_jMenuItemOptionsActionPerformed

    private void jCheckBoxMenuItemOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemOutputActionPerformed



        if (jCheckBoxMenuItemOutput.isSelected()) {
            Misc.dockAt(logPaneView, DockingConstants.SOUTH_REGION, 0);
        }
        else {
            DockingManager.close(logPaneView );
        }


    }//GEN-LAST:event_jCheckBoxMenuItemOutputActionPerformed

    private void jCheckBoxMenuItemExplorerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemExplorerActionPerformed

         if (jCheckBoxMenuItemExplorer.isSelected())
        {
             Misc.dockAt(viewFiles, DockingConstants.WEST_REGION, 0);
        }
        else
        {
           DockingManager.close(viewFiles);
        }
        getProperties().put("panelFiles",  ""+jCheckBoxMenuItemExplorer.isSelected());
        this.saveiReportConfiguration();

    }//GEN-LAST:event_jCheckBoxMenuItemExplorerActionPerformed

    public void jMenuItemHSDecreaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHSDecreaseActionPerformed
        FormatCommand.getCommand( OperationType.DECREASE_SPACE_H).execute();
    }//GEN-LAST:event_jMenuItemHSDecreaseActionPerformed

    public void jMenuItemVSIncreaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVSIncreaseActionPerformed
        FormatCommand.getCommand( OperationType.INCREASE_SPACE_V).execute();
    }//GEN-LAST:event_jMenuItemVSIncreaseActionPerformed

    private void jButtonDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatabaseActionPerformed
        this.getReportQueryDialog().setSubDataset( this.getActiveReportFrame().getReport() );
        this.getReportQueryDialog().setVisible(true);
    }//GEN-LAST:event_jButtonDatabaseActionPerformed

    private void jMenuItemSaveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAllActionPerformed
      saveAll();
    }

    /**
     * save all open reports
     */
    public void saveAll() {
      saveAll(jMDIDesktopPane.getAllFrames());
    }

    /**
     * save all reportframes
     *
     * @param frames JInternalFrame[]
     */
    public void saveAll(javax.swing.JInternalFrame[] frames) {
        for (int i=0; i< frames.length; ++i) {
            if (frames[i]  instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)frames[i];
                if (jrf.getReport().getFilename() == null ||
                jrf.getReport().getFilename().trim().equals("")) {
                    // Choose a file name....
                    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(getCurrentDirectory());

                    jfc.setDialogTitle("Save report as XML jasperreports file....");
                    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                        public boolean accept(java.io.File file) {
                            String filename = file.getName();
                            return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                        }
                        public String getDescription() {
                            return "JasperReports XML *.xml, *.jrxml";
                        }
                    });


                    jfc.setMultiSelectionEnabled(false);

                    jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
                    if  (jfc.showSaveDialog(this) != javax.swing.JOptionPane.OK_OPTION) {
                        //if we saved or didn't save the current file
                        javax.swing.JInternalFrame current = jMDIDesktopPane.getSelectedFrame();
                        if( current instanceof JReportFrame ) {
                            JReportFrame crf = (JReportFrame)current;
                            if( crf.getReport().isModified() ) {
                                this.enableSaveSpecific();
                            }
                            else {
                                this.disableSaveSpecific();
                            }
                        }
                        return;
                    }
                    setCurrentDirectory( jfc.getSelectedFile(), true);
                    jrf.getReport().setFilename( jfc.getSelectedFile().getPath());
                }

                //saveBackup( jrf.getReport().getFilename());
                jrf.getReport().saveXMLFile();
                //jrf.getReport().in(false);
                //this.addToRecentFileList(rp.filename);
                //saveProperties();
                //this.updateRecentFileList();
            }//end if instance of
        }//end for loop frames
        this.disableSaveAllSpecific();
        this.disableSaveSpecific();
    }//GEN-LAST:event_jMenuItemSaveAllActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            saveAs(jrf);
        }
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed


    public void saveAs(JReportFrame jrf)
    {
            String reportName = jrf.getReport().getName();

            // Choose a new file name....
            // -----------------------------------------------------------------

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(getCurrentDirectory());
            jfc.setDialogTitle("Save report as XML jasperreports file as....");

            // Handling the new File, that gets saved for the first time
            // Propose a new file name based on the report name
            if(jrf.getReport().getFilename() != null)
              jfc.setSelectedFile( new java.io.File(jrf.getReport().getFilename())) ;

            if (jrf.getReport().getFilename() == null ||
                    jrf.getReport().getFilename().trim().equals(""))
            {
                if ( !( (reportName == null) || (reportName.trim().equals(""))) )
                {
                    File file = new java.io.File( reportName + ".jrxml");
                    jfc.setSelectedFile( file );
                }
            }

            jfc.setFileFilter( new javax.swing.filechooser.FileFilter()
            {
                public boolean accept(java.io.File file)
                {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription()
                {
                    return "JasperReports XML *.xml, *.jrxml";
                }
            });
            jfc.setMultiSelectionEnabled(false);
            jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);

            // Show the dialog:
            if  (jfc.showSaveDialog(this) != javax.swing.JOptionPane.OK_OPTION)
            {
                this.cancel=true;
            	return;
            }

            setCurrentDirectory( jfc.getSelectedFile(), true);

            //file exists?
            if(jfc.getSelectedFile().exists())
            {
                //is the same file to save?
                if( new File(jrf.getReport().getFilename()).equals(jfc.getSelectedFile()))
                {
                    // Synchronize Report Name ?
                    boolean sync = false;
                    String filename = jfc.getSelectedFile().getName();
                    if (filename.lastIndexOf(".") > 0)
                    {
                        filename = filename.substring(0, filename.lastIndexOf(".")  );
                    }

                    if (! reportName.toLowerCase().equals (filename.toLowerCase() ) )
                    {
                        if ( isSynchronizeReportName() )
                        {
                            sync = true;
                        }
                        else
                        {
                            if ( isShowOptionDialogSynchronize( jrf.getReport().getName(), filename ) )
                            {
                                sync = true;
                            }
                        }
                    }
                    if (sync)
                    {
                        jrf.getReport().setName( filename );
                    }
                    //save only
                    jrf.getReport().saveXMLFile();
                    //jrf.getReport().setModified(false);
                    return;
                }
                else
                {
                    //is different file to save?
                    //confirm overwrite
                    int ret = I18nOptionPane.showOptionDialog(this,
                            "overwriteFile",
                            "saveAs",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, new String[]{"yes", "no"},
                            "no"
                                    );

                            //if yes
                            if(ret == 0)
                            {
                                //is that file opened?
                                JReportFrame reportFrame = findReportFrameByFile(jfc.getSelectedFile());
                                if(reportFrame != null)
                                {
                                    //close file
                                    reportFrame.setDefaultCloseOperation( jrf.DISPOSE_ON_CLOSE );
                                    reportFrame.doDefaultCloseAction();
                                }
                            }
                            else
                            {
                                //canceled
                                return;
                            }

                }
            }
            else
            {
                // File does not exist yet.

            }

            String f = jfc.getSelectedFile().getPath();
            if (!f.toLowerCase().endsWith(".jrxml" ) &&
                    !f.toLowerCase().endsWith(".xml" ))
            {
                f += ".jrxml";
            }

            //saveBackup( jrf.getReport().getFilename());
            jrf.getReport().checkReadOnlyFlag(f);
            if(jrf.getReport().isReadOnly())
            {
                javax.swing.JOptionPane.showMessageDialog(this,
                        it.businesslogic.ireport.util.I18n.getString("selectedFileReadOnly", "Selected File is read only and cannot be used."),
                        it.businesslogic.ireport.util.I18n.getString("save","Save"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Begin Modification, Robert Lamping, May 3, 2005

            String filename = jfc.getSelectedFile().getName();
            if (filename.lastIndexOf(".") > 0)
                {
                    filename = filename.substring(0, filename.lastIndexOf(".")  );
                }

            if (reportName.trim().equals("") )
            {
                // if report name is empty, then use the filename without the last extension as
                // the new report name.
                jrf.getReport().setName(filename);
            }
            else
            {
                // If different file name was entered than the report name
                // and the report name is not null or empty. then:
                // Ask whether it should be brought in sync.

                // filename always includes a "."
                // so take everything until the last occurance of "."
                // and compare this to the reportname
                // if not equal, than ask to synchronize.


                if (! reportName.toLowerCase().equals(filename.toLowerCase() ) )
                {
                    // Ask to synchronize
                    // TODO: I18N hardcoded text
                    if ( isSynchronizeReportName() )
                    {
                        jrf.getReport().setName(filename);
                        
                        getLogPane().getMainLogTextArea().logOnConsole(
                            I18n.getString("messages.nameSync", "Report name synchronized with file name\n"),
                        JOptionPane.INFORMATION_MESSAGE);

                    }
                    else
                    {
                        if ( isShowOptionDialogSynchronize( reportName, filename ) )
                        {
                            jrf.getReport().setName(filename);
                        }
                    }
                }

            }
            // End Modification, Robert Lamping, May 3, 2005

            jrf.getReport().setFilename(f);
            jrf.getReport().saveXMLFile();

            //jrf.getReport().setModified(false);
            jrf.updateTitle();
            updateMenuWindowList();

            javax.swing.tree.DefaultTreeModel dtm = (javax.swing.tree.DefaultTreeModel) jTreeFiles.getModel();
            dtm.reload();

            setActiveReportForm(jrf);
            this.disableSaveSpecific();
            if(this.isSaveAllRequired())
            {
                this.enableSaveAllSpecific();
            }
            else
            {
                this.disableSaveAllSpecific();
            }
            recentFilesList.insertElementAt(jrf.getReport().getFilename(),0);
            this.updateRecentFileMenu(jMenuRecentFiles, recentFilesList );
            //saveFileList(recentFilesList, "recentFiles.xml");
            saveFileList(recentFilesList, IREPORT_RECENT_FILES_FILE);
            //          this.addToRecentFileList(rp.filename);
            //saveProperties();
            //this.updateRecentFileList();

    }


    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
        this.jMenuItemOpenActionPerformed(evt);
    }//GEN-LAST:event_jButtonOpenActionPerformed

    private void jButtonCompilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompilerActionPerformed
     compile();
    }

    public IReportCompiler compile() {
        return compile(jListThreads);
    }

    public IReportCompiler compile(javax.swing.JList jListThreads) {
      // 1. Get active report...
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
            if (jrf.getReport().getFilename() == null || jrf.getReport().getFilename().trim().equals("") ||
                    (Misc.nvl(this.getProperties().getProperty("SaveBeforeCompiling"), "true").equals("true") && jrf.getReport().isModified()))
            {
                jMenuItemSaveActionPerformed(new java.awt.event.ActionEvent(jMenuItemSave,0,"Save"));
                if (jrf.getReport().isModified())
                {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user"),
                    JOptionPane.WARNING_MESSAGE);
                }
            }
                
            //1. Save the report if needed....
            
            
            if( jrf.getReport().getFilename() == null ||
                jrf.getReport().getFilename().trim().equals("")) {
                    getLogPane().getMainLogTextArea().logOnConsole(
                        I18n.getString("messages.actionAbortedByUser", "Action aborted by user"),
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }

            //clearConsole();
            //1. Compile....
            IReportCompiler ic = new IReportCompiler();
            ic.setThreadList( jListThreads );

            HashMap hm = new HashMap();
            hm.put( ic.OUTPUT_DIRECTORY, getTranslatedCompileDirectory());

            if (jrf.getReport().getLanguage().equals("groovy"))
            {
                hm.put( ic.COMPILER, "net.sf.jasperreports.compilers.JRGroovyCompiler");
            }

            ic.setProperties(hm);
            ic.setCommand( IReportCompiler.CMD_COMPILE);

            if (jrf.getReport().getScriptletHandling() == jrf.getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
                ic.setCommand( ic.getCommand() | IReportCompiler.CMD_COMPILE_SCRIPTLET);
            }
            ic.setJrf(jrf);
            ic.setMainFrame(this);
            ic.start();
            return ic;
        }
        return null;


    }//GEN-LAST:event_jButtonCompilerActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        jMenuItemSaveActionPerformed(evt);
    }//GEN-LAST:event_jButtonSaveActionPerformed

    public void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();

            save( jrf );
        }
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    public void save(JReportFrame jrf)
    {
            String reportName = jrf.getReport().getName();

            if (jrf.getReport().getFilename() == null ||
            jrf.getReport().getFilename().trim().equals("")) {
                // Choose a file name....
                javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(getCurrentDirectory());
                jfc.setDialogTitle("Save report as XML jasperreports file....");

                // Begin Modification, Robert Lamping, May 3, 2005
                // filename is empty, so default it to the report name
                // whether or not the (future) option to keep report name and file name equal

                File file = new java.io.File( reportName + ".jrxml");
                jfc.setSelectedFile( file );
                // End Modification, Robert Lamping, May 3, 2005

                jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                    public boolean accept(java.io.File file) {
                        String filename = file.getName();
                        return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                    }
                    public String getDescription() {
                        return "JasperReports XML *.xml, *.jrxml";
                    }
                });

                jfc.setMultiSelectionEnabled(false);

                jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
                if  (jfc.showSaveDialog( this) != javax.swing.JOptionPane.OK_OPTION) return;
                setCurrentDirectory( jfc.getSelectedFile(), true);
                String f = jfc.getSelectedFile().getPath();
                if (!f.toLowerCase().endsWith(".jrxml" ) &&
                !f.toLowerCase().endsWith(".xml" )) {
                    f += ".jrxml";
                }
                jrf.getReport().checkReadOnlyFlag(f);
                if(jrf.getReport().isReadOnly()){
                    javax.swing.JOptionPane.showMessageDialog(this,
                    it.businesslogic.ireport.util.I18n.getString("selectedFileReadOnly", "Selected File is read only and cannot be used."),
                    it.businesslogic.ireport.util.I18n.getString("save","Save"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    return;
                }


                // Begin Modification, Robert Lamping, May 3, 2005
                // If a file with the same name exists as f
                // then the ask whether the file must be overwritten.

                //file exists?
                if(jfc.getSelectedFile().exists()) {

                    //confirm overwrite
                    // TODO: Show Filename in OptionDialog
                    int ret = I18nOptionPane.showOptionDialog(this,
                            "overwriteFile",
                            "save",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, new String[]{"yes", "no"},
                            "no",
                            jfc.getSelectedFile().getPath()  );

                            //if yes
                            if(ret == 0){

                                //is that file opened?
                                JReportFrame reportFrame = findReportFrameByFile(jfc.getSelectedFile());
                                if(reportFrame != null){
                                    //close file
                                    reportFrame.setDefaultCloseOperation( jrf.DISPOSE_ON_CLOSE );
                                    reportFrame.doDefaultCloseAction();
                                }
                            } else {

                                //canceled
                                return;
                            }
                }


                String filename = jfc.getSelectedFile().getName();
                if (filename.lastIndexOf(".") > 0)
                {
                    filename = filename.substring(0, filename.lastIndexOf(".")  );
                }

                if (reportName.trim().equals ("") )
                {
                    // if report name is empty, then use the filename without the last extension as
                    // the new report name.
                    jrf.getReport ().setName (filename);
                }
                else
                {
                    // If different file name was entered than the report name
                    // and the report name is not null or empty. then:
                    // Ask whether it should be brought in sync.

                    // filename always includes a "."
                    // so take everything until the last occurance of "."
                    // and compare this to the reportname
                    // if not equal, than ask to synchronize.


                    if (! reportName.toLowerCase().equals (filename.toLowerCase() ) )
                    {
                        // Ask to synchronize
                        // TODO: I18N hardcoded text
                        if ( isSynchronizeReportName() )
                        {
                            jrf.getReport().setName(filename);
                            getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getString("messages.nameSync", "Report name synchronized with file name\n"),
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            // ask user whether to synchronize or not
                            if ( isShowOptionDialogSynchronize( reportName, filename ) )
                            {
                                jrf.getReport().setName(filename);
                            }
                        }
                    }

                }
                // End Modification, Robert Lamping, May 3, 2005

                jrf.getReport().setFilename( f );
                recentFilesList.insertElementAt(jrf.getReport().getFilename(),0);
                this.updateRecentFileMenu(jMenuRecentFiles, recentFilesList );
                //saveFileList(recentFilesList, "recentFiles.xml");
                saveFileList(recentFilesList, IREPORT_RECENT_FILES_FILE);
            }
            else
            {

                // Begin Modification, Robert Lamping, May 3, 2005
                File file = new java.io.File( jrf.getReport().getFilename() );
                // strip the path:
                String filename = file.getName();
                if (filename.lastIndexOf(".") > 0)
                {
                    filename = filename.substring(0, filename.lastIndexOf(".")  );
                }

                if (reportName.trim().equals("") )
                {
                    // if report name is empty, then use the filename without the last extension as
                    // the new report name.
                    jrf.getReport().setName(filename);
                }
                else
                {
                    // If different file name was entered than the report name
                    // and the report name is not null or empty. then:
                    // Ask whether it should be brought in sync.

                    // filename always includes a "."
                    // so take everything until the last occurance of "."
                    // and compare this to the reportname
                    // if not equal, than ask to synchronize.
                    if (! reportName.toLowerCase().equals(filename.toLowerCase()) )
                    {
                        if ( isSynchronizeReportName() )
                        {
                            jrf.getReport().setName(filename);
                            getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getString("messages.nameSync", "Report name synchronized with file name\n"),
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                             // ask user whether to synchronize or not
                            if ( isShowOptionDialogSynchronize( reportName, filename ) )
                            {
                                jrf.getReport().setName(filename);
                            }
                        }
                    }

                }
                // End Modification, Robert Lamping, May 3, 2005

            }


            if(!jrf.getReport().isReadOnly() && !getProperties().getProperty("BackupPolicy","3").equals("1") )
            {
                saveBackup( jrf.getReport().getFilename());
            }
            jrf.getReport().saveXMLFile();
            //jrf.getReport().setReportChanges(0);
            //jrf.getReport().setModified(false);
            this.disableSaveSpecific();
            if(this.isSaveAllRequired())
            {
                this.enableSaveAllSpecific();
            }
            else
            {
                this.disableSaveAllSpecific();
            }
            //this.addToRecentFileList(rp.filename);
            //saveProperties();
            //this.updateRecentFileList();
            this.jTreeFiles.updateUI();

    }

    /**
     * saveToStream
     * saves a Report to an OutputStream
     *
     * Author: Felix Firgau
     *
     * @param outputStream OutputStream
     */
    public void saveToStream(java.io.OutputStream outputStream) {
        JReportFrame rf = getActiveReportFrame () ;
        if ( rf == null )
            return ;
        Report activeReport = rf.getReport();
        ReportWriter rw = new ReportWriter(activeReport);
        rw.writeToOutputStream(outputStream);
    }

    private void jCheckBoxMenuItemSnapToGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemSnapToGridActionPerformed
        // Set SnapToGrid to all reports form..
        javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int i=0; i< frames.length; ++i) {
            if (frames[i]  instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)frames[i];
                jrf.setSnapToGrid(jCheckBoxMenuItemSnapToGrid.isSelected() );
                this.getProperties().setProperty( "snapToGrid", jCheckBoxMenuItemSnapToGrid.isSelected() + "" );
                this.saveiReportConfiguration();
            }
        }
    }//GEN-LAST:event_jCheckBoxMenuItemSnapToGridActionPerformed

    private void jCheckBoxMenuItemGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemGridActionPerformed

        javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int i=0; i< frames.length; ++i) {
            if (frames[i]  instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)frames[i];
                jrf.setShowGrid(jCheckBoxMenuItemGrid.isSelected() );
                this.getProperties().setProperty( "showGrid", jCheckBoxMenuItemGrid.isSelected() + "" );
                this.saveiReportConfiguration();
            }
        }
    }//GEN-LAST:event_jCheckBoxMenuItemGridActionPerformed

    private void jButtonBandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBandsActionPerformed
        getBandsDialog().setVisible(true);
    }//GEN-LAST:event_jButtonBandsActionPerformed

    private void jButtonGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGroupsActionPerformed
        getGroupsDialog().setVisible(true);
    }//GEN-LAST:event_jButtonGroupsActionPerformed

    private void jButtonParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonParametersActionPerformed
        // Show values frame...
        getValuesDialog().setVisible(true);

    }//GEN-LAST:event_jButtonParametersActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
      open();
    }

    /**
     * opens one or more existing report. Shows file chooser dialog.
     * returns the newly opened ReportFrames (maybe more than one) or null if file chooser is canceled.
     *
     * @return JReportFrame[]
     */
    public JReportFrame[] open()
    {
        // Select an XMl file...
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(getCurrentDirectory());

        jfc.setDialogTitle("Load XML jasperreports file....");

        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".jasper") || file.isDirectory()) ;
            }
            public String getDescription() {
                return "JasperReports compiled *.jasper";
            }
        });

        jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
            }
            public String getDescription() {
                return "JasperReports XML *.xml, *.jrxml";
            }
        });



        jfc.setMultiSelectionEnabled(true);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            JReportFrame[] result = new JReportFrame[files.length];

            for (int i=0; i<files.length; ++i) {
                result[i] = openFile(files[i]);
            }
            setCurrentDirectory( jfc.getSelectedFile(), true);
            return result;
        }
        return null;

    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jToggleButtonSubreportToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonSubreportToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.SUBREPORT_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonSubreportTool);
        }
    }//GEN-LAST:event_jToggleButtonSubreportToolItemStateChanged

    private void jToggleButtonSubreportToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSubreportToolActionPerformed
        jToggleButtonSubreportTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonSubreportToolActionPerformed

    private void jToggleButtonTextFieldToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonTextFieldToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.TEXTFIELD_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonTextFieldTool);
        }
    }//GEN-LAST:event_jToggleButtonTextFieldToolItemStateChanged

    private void jToggleButtonTextFieldToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonTextFieldToolActionPerformed
        jToggleButtonTextFieldTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonTextFieldToolActionPerformed

    private void jToggleButtonStaticTextToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonStaticTextToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.STATICTEXT_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonStaticTextTool);
        }
    }//GEN-LAST:event_jToggleButtonStaticTextToolItemStateChanged

    private void jToggleButtonStaticTextToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonStaticTextToolActionPerformed
        jToggleButtonStaticTextTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonStaticTextToolActionPerformed

    private void jToggleButtonImageToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonImageToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.IMAGE_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonImageTool);
        }
    }//GEN-LAST:event_jToggleButtonImageToolItemStateChanged

    private void jToggleButtonImageToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonImageToolActionPerformed
        jToggleButtonImageTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonImageToolActionPerformed

    private void jToggleButtonEllipseToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonEllipseToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.ELLIPSE_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonEllipseTool);
        }
    }//GEN-LAST:event_jToggleButtonEllipseToolItemStateChanged

    private void jToggleButtonEllipseToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonEllipseToolActionPerformed
        jToggleButtonEllipseTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonEllipseToolActionPerformed

    private void jToggleButtonRectToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonRectToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.RECTANGLE_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonRectTool);
        }
    }//GEN-LAST:event_jToggleButtonRectToolItemStateChanged

    private void jToggleButtonRectToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonRectToolActionPerformed
        jToggleButtonRectTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonRectToolActionPerformed

    private void jToggleButtonLineToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonLineToolActionPerformed
        jToggleButtonLineTool.setSelected(true);
    }//GEN-LAST:event_jToggleButtonLineToolActionPerformed

    private void jToggleButtonLineToolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonLineToolItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.LINE_ELEMENT);
                jrf.setLensMode(false);
            }
            setActiveTool(jToggleButtonLineTool);
        }
    }//GEN-LAST:event_jToggleButtonLineToolItemStateChanged

    private void jToggleButtonPointerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonPointerActionPerformed
        jToggleButtonPointer.setSelected(true);
    }//GEN-LAST:event_jToggleButtonPointerActionPerformed

    private void jToggleButtonPointerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonPointerItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

            if (jMDIDesktopPane.getSelectedFrame() != null &&
            jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
                jrf.setNewObjectType( ReportElementType.NONE);
                jrf.setLensMode(false);
            }
            if (jToggleButtonPointer.isEnabled()) {
                setActiveTool( jToggleButtonPointer );
            }
        }
    }//GEN-LAST:event_jToggleButtonPointerItemStateChanged

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        evt.setSource(jButtonNew);
        this.jMenuItemNewDocumentActionPerformed(evt);
    }//GEN-LAST:event_jButtonNewActionPerformed

    public void setCutCopyEnabled(boolean enabled) {
        this.jButtonCut.setEnabled(enabled);
        this.jMenuItemCut.setEnabled(enabled);

        this.jButtonCopy.setEnabled(enabled);
        this.jMenuItemCopy.setEnabled(enabled);

        this.jMenuItemDelete.setEnabled(enabled);

        getDocumentStructurePanel().setCutCopyEnabled(enabled);
    }

    public void setPasteEnebled(boolean enabled) {
        this.jButtonPaste.setEnabled(enabled);
        this.jMenuItemPaste.setEnabled(enabled);

        getDocumentStructurePanel().setPasteEnebled(enabled);
    }

    public void setUndoEnebled(boolean enabled) {
        this.jMenuItemUndo.setEnabled(enabled);
    }

    public void setRedoEnebled(boolean enabled) {
        this.jMenuItemRedo.setEnabled(enabled);
    }

    public void setSaveEnebled(boolean enabled) {
        this.jButtonSave.setEnabled( enabled);
        this.jMenuItemSave.setEnabled( enabled);
    }

    private void jMenuItemSubreportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubreportActionPerformed
        jToggleButtonSubreportToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonSubreportTool,0,jToggleButtonSubreportTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemSubreportActionPerformed

    private void jMenuItemTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextFieldActionPerformed
        jToggleButtonTextFieldToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonTextFieldTool,0,jToggleButtonTextFieldTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemTextFieldActionPerformed

    private void jMenuItemStaticTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStaticTextActionPerformed
        jToggleButtonStaticTextToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonStaticTextTool,0,jToggleButtonStaticTextTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemStaticTextActionPerformed

    private void jMenuItemEllipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEllipseActionPerformed
        jToggleButtonEllipseToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonEllipseTool,0,jToggleButtonEllipseTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemEllipseActionPerformed

    private void jMenuItemRoundRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRoundRectangleActionPerformed
        //jToggleButtonRectRoundToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonRectRoundTool,0,jToggleButtonRectRoundTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemRoundRectangleActionPerformed

    private void jMenuItemRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRectangleActionPerformed
        jToggleButtonRectToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonRectTool,0,jToggleButtonRectTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemRectangleActionPerformed

    private void jMenuItemLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLineActionPerformed
        jToggleButtonLineToolItemStateChanged(new java.awt.event.ItemEvent(jToggleButtonLineTool,0,jToggleButtonLineTool, java.awt.event.ItemEvent.SELECTED));
    }//GEN-LAST:event_jMenuItemLineActionPerformed

    private void jMenuEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEditActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jMenuEditActionPerformed

    private void jNumberComboBoxZoomValueChanged(ValueChangedEvent evt) {
        applyComboBoxZoomFactor();
    }

    private void applyComboBoxZoomFactor(){

        JInternalFrame jif = jMDIDesktopPane.getSelectedFrame();
        if (jif instanceof JReportFrame) {
            double new_factor = 0;
            String s = this.jNumberComboBoxZoom.getSelectedItem()+"";

            /* Code Begin Robert Lamping, 17 july 2004
             * Add + modification
             * Fit to Page renamed to Width
             * Added:  Whole Page, Height
             */
            if (s.equalsIgnoreCase("Width")) {
                new_factor = (double)(((JReportFrame)jif).getJPanelReport().getWidth()-20) / (double)(((JReportFrame)jif).getReport().getWidth());
                ((JReportFrame)jif).setZoomFactor(new_factor);
                new_factor = (int)(100.0*new_factor);
                jNumberComboBoxZoom.setValue( new_factor );
                ((JReportFrame)jif).centerSelectedElements();

            } else if (s.equalsIgnoreCase("Whole page")) {
                int height = ((JReportFrame) jif).getReport().getDesignHeight();
                new_factor = Math.min( (double)(((JReportFrame)jif).getJPanelReport().getWidth()-20) / (double)(((JReportFrame)jif).getReport().getWidth()),
                (double)(((JReportFrame)jif).getJPanelReport().getHeight()-20) / (double) height );
                ((JReportFrame)jif).setZoomFactor(new_factor);
                new_factor = (int)(100.0*new_factor);
                jNumberComboBoxZoom.setValue( new_factor );

            } else if (s.equalsIgnoreCase("Height")) {
                int height = ((JReportFrame) jif).getReport().getDesignHeight();
                new_factor = (double)(((JReportFrame)jif).getJPanelReport().getHeight()-20) / (double) height ;
                ((JReportFrame)jif).setZoomFactor(new_factor);
                new_factor = (int)(100.0*new_factor);
                jNumberComboBoxZoom.setValue( new_factor );
                ((JReportFrame)jif).centerSelectedElements();

            /* Code End Robert Lamping, 17 july 2004  */
            } else  {

                /* Code Begin Robert Lamping, 18 july 2004  */
                Point defaultCenter = ((JReportFrame)jif).getCurrentCenter();

                ((JReportFrame)jif).setZoomFactor( (double)(this.jNumberComboBoxZoom.getValue()/100.0));

                // Move selected items in the middle if possible.
                ((JReportFrame)jif).centerSelectedElements( defaultCenter ) ;
                /* Code End Robert Lamping, 18 july 2004  */

            }
            this.setCurrentZoomFactor(currentZoomFactor);

        }

    }

    /**
     *  This method adjust JImageButtons position in according with buttons array.
     *  A null entry is interpreted as a 8 pixel space.

    public boolean addToolbarComponent(javax.swing.JComponent ctl)
    {
        if (getToolBarControls() == null ) return false;
        if (getToolBarControls().contains( ctl)) return false;
        getToolBarControls().add(ctl);
        updateToolBarUI();
        return true;
    }

    private void updateToolBarUI()
    {
        // Add your handling code here:
        if (getToolBarControls() == null ) return;
        int actualWidth=0;
        int actualHeight=0;
        int availWidth = this.jPanelToolBar.getWidth();

        java.util.Enumeration e = getToolBarControls().elements();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj == null || ! (obj instanceof javax.swing.JComponent)) {
                if (actualWidth > 0) {
                    actualWidth += 8;
                }
            }
            else {
                javax.swing.JComponent ctl = (javax.swing.JComponent)obj;
                if ((actualWidth + ctl.getWidth()) > availWidth && (ctl.getWidth()+0 <= availWidth)) {
                    // Go to the next row...
                    actualHeight += 32;
                    actualWidth = 0;
                }
                ctl.setLocation(actualWidth,actualHeight);
                actualWidth += ctl.getWidth();
            }
        }
        jPanelToolBar.setSize( this.jPanelToolBar.getWidth(), (actualHeight+34));
        jPanelToolBar.setPreferredSize(new java.awt.Dimension( this.jPanelToolBar.getWidth(), (actualHeight)+34));
        jPanelToolBar.updateUI();
    }
  */
    private void jMenuItemNewDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewDocumentActionPerformed

        if (( evt.getSource() == jButtonNew || getProperties().getProperty("DontUseTemplateFromMenu", "true").equals("false")) &&
            getProperties().getProperty( "DefaultTemplate") != null)
        {
            File f = new File( getProperties().getProperty( "DefaultTemplate"));
            if (f.exists())
            {
                newReport(f);
                return;
            }

        }
        newReport();
    }//GEN-LAST:event_jMenuItemNewDocumentActionPerformed

    /**
     * newReport
     * generates a new report from a template file
     * returns the new report or null if an error occurs
     *
     * @param template File
     * @return Report
     */
    public Report newReport(File template)
    {
        try {
            Report report = new Report(template.getPath());
            report.setFilename( null );
            report.setName( getFirstNameFree() );
            report.incrementReportChanges();
            // Modificacion para Geopista
            IReportParameterManager.addDefaultGeopistaParameters(report);
            // Fin de modificacion
            openNewReportWindow( report );
            return report;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * newReport
     * generates a new report and shows property dialog
     * returns the new report or null if property dialog is canceled
     *
     * @return Report
     */
    public Report newReport()
    {
        // Get info about the new report....
        // 1. Display the ReportProperties frame in dialog mode.
        //    Will be proposed default values...
        ReportPropertiesFrame rpf = new ReportPropertiesFrame(this,true);
        rpf.setModal(true);
        // find the first name free...
        String name = getFirstNameFree();
        rpf.setReportName( name);
        rpf.setVisible(true);
        if (rpf.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            // The user has clicked on OK...
            // Storing in a new report the report characteristics.
            Report newReport = new Report();
            newReport.setUsingMultiLineExpressions(false); //this.isUsingMultiLineExpressions());
            newReport.setWidth(rpf.getReportWidth());
            newReport.setHeight(rpf.getReportHeight());
            newReport.setOrientation(rpf.getOrientation());
            newReport.setName(rpf.getReportName());
            newReport.setTopMargin(rpf.getTopMargin());
            newReport.setLeftMargin(rpf.getLeftMargin());
            newReport.setRightMargin(rpf.getRightMargin());
            newReport.setBottomMargin(rpf.getBottomMargin());
            newReport.setColumnCount(rpf.getColumns());
            newReport.setColumnWidth(rpf.getColumnsWidth());
            newReport.setColumnSpacing(rpf.getColumnsSpacing());
            newReport.setIsSummaryNewPage(rpf.isSummaryOnNewPage());
            newReport.setIsTitleNewPage(rpf.isTitleOnNewPage());
            newReport.setWhenNoDataType(rpf.getWhenNoDataType());
            newReport.setScriptletClass(rpf.getScriptletClass());
            newReport.setEncoding(rpf.getXmlEncoding());
            newReport.setPrintOrder(rpf.getPrintOrder());
            newReport.setReportFormat(rpf.getReportFormat());
            newReport.setFloatColumnFooter(rpf.isFloatColumnFooter());
            newReport.setResourceBundleBaseName( rpf.getResourceBundleBaseName() );
            newReport.setWhenResourceMissingType( rpf.getWhenResourceMissingType());
            newReport.setIgnorePagination(rpf.isIgnorePagination());
            newReport.setFormatFactoryClass(rpf.getFormatFactoryClass());

            /* todo newReport.setPageName(rpf.getPageName());  */
            newReport.setLanguage( rpf.getLanguage() );

            // Modificacion para Geopista
            IReportParameterManager.addDefaultGeopistaParameters(newReport);
            // Fin de modificacion
            openNewReportWindow( newReport );
            return newReport;
        }
        return null;
    }

    /**
     * setup and configure a new JReportFrame for subclassing JReportFrame
     * Implemented by Felix Firgau on February 21st 2007
     * @param jrf JReportFrame
     */
    public void setupNewReportWindow(JReportFrame jrf) {
      jrf.addReportListener(this);
      //this.addReportListener( this.);
      Report newReport = jrf.getReport();
      newReport.addSubDatasetObjectChangedListener( this.getLibraryPanel());
      newReport.addSubDatasetObjectChangedListener( this.getValuesDialog());
      newReport.addReportSubDatasetChangedListener( this.getLibraryPanel());
      
      newReport.addSubDatasetObjectChangedListener( this.getDocumentStructurePanel());
      newReport.addReportSubDatasetChangedListener( this.getDocumentStructurePanel());
      newReport.addSubDatasetObjectChangedListener( this.getReportSheetPanel());
      newReport.addReportSubDatasetChangedListener( this.getReportSheetPanel());
      
      newReport.addStyleChangedListener( this );

      // this is for JMDIFrame design...
      // jMDIMenuBar could be retrived from the jrf parent, contained in the JFrame
      // that store the JDesktopPane parent...
      jrf.setMenu( this.jMDIMenuBar );
      // Set default, but we starting the frame maximized by default...
      jrf.setBounds(10,10,700,500);
      // We must set the frame visible before adding it to the jMDIDesktopPane...

      jrf.setShowGrid( this.getProperties().getProperty( "showGrid", "false" ).equals("true"));
      jrf.setSnapToGrid( this.getProperties().getProperty( "snapToGrid", "false" ).equals("true"));

      jrf.setVisible(true);


      if (!isEmptyString(jrf.getReport().getFilename()) ) {
          recentFilesList.remove( jrf.getReport().getFilename());
          recentFilesList.insertElementAt(jrf.getReport().getFilename(),0);
          this.updateRecentFileMenu(jMenuRecentFiles, recentFilesList );
          //saveFileList(recentFilesList, "recentFiles.xml");
          saveFileList(recentFilesList, IREPORT_RECENT_FILES_FILE);
      }

      jMDIDesktopPane.add(jrf, javax.swing.JLayeredPane.DEFAULT_LAYER);

      jMDIDesktopPane.getDesktopManager().activateFrame(jrf);

      // Count all frames...

      int f_num = jMDIDesktopPane.getAllFrames().length;
      JRadioButtonMenuItemMDIFrame menuItem = new JRadioButtonMenuItemMDIFrame((JMDIFrame)jrf, f_num + ". " + jrf.getTitle() );
      menuItem.setSelected(true);
      menuItem.setMnemonic((int)((f_num+"").charAt(0)) );
      menuItem.setAccelerator( javax.swing.KeyStroke.getKeyStroke((int)((f_num+"").charAt(0)),java.awt.Event.META_MASK));

      this.jMenuWindow.insert(menuItem, this.jMenuWindow.getMenuComponentCount());
      menuItem.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
              jMenuWindowListActionPerformed(evt);
          }
      });
      buttonGroupFramesList.add(menuItem);
      //updateMenuWindowList();

      // After activation, we try to maximize the window....
      try {
          jrf.setSelected(true);
          jrf.setZoomFactor( this.getCurrentZoomFactor() );
          //if (jMDIMenuBar.getMaximizedFrame()  != null)
          jrf.setMaximum(true);
          String report_name = jrf.getReport().getName();
          DocumentTreeEntry dte = new DocumentTreeEntry(jrf,jrf.getReport().getName());
          javax.swing.tree.DefaultTreeModel dtm = (javax.swing.tree.DefaultTreeModel) jTreeFiles.getModel();
          ((javax.swing.tree.DefaultMutableTreeNode)dtm.getRoot()).add(
          new javax.swing.tree.DefaultMutableTreeNode(dte));
          setFileListActivatedFrame(jrf);
          this.jTreeFiles.updateUI();

      } catch (java.beans.PropertyVetoException ex) {
          /* Nothing to do */
      } catch (Exception ex2) {
          /* Do nothing */
      }

      //set zoomfactor in toolbar
      this.jNumberComboBoxZoom.setValue( (int) (this.getCurrentZoomFactor()*100) );

      // This call fix a bug in the code to show control box buttons on menu panel...
      jMDIMenuBar.restoreButtonsPosition();
      if( !isEmptyString(newReport.getFilename())) {
          if(newReport.isModified()) {
              this.enableSaveSpecific();
              this.enableSaveAllSpecific();
          }
          else {
              this.disableSaveSpecific();
              if(this.isSaveAllRequired()) {
                  this.enableSaveAllSpecific();
              }
              else {
                  this.disableSaveAllSpecific();
              }
          }
      }
      else {
          this.enableSaveSpecific();
          this.enableSaveAllSpecific();
      }
    }

    public JReportFrame openNewReportWindow( Report newReport ) {
        // Create a new document frame....
        // ...that rappresent the report document created...
        JReportFrame jrf = new JReportFrame(newReport);
        setupNewReportWindow(jrf); //FF see above
        return jrf;
    }
    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed
        // Add your handling code here:
        jMDIMenuBar.restoreButtonsPosition();
    }//GEN-LAST:event_jMenuFileActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        // This try is of java <1.4 comatibility

        try {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {

                        setExtendedState(MAXIMIZED_BOTH);




                    } catch (java.lang.NoSuchMethodError ex) {
                        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                        java.awt.Point p = new java.awt.Point();
                        p.x = Math.max( 0, (dim.width -getWidth())/2 );
                        p.y = Math.max( 0, (dim.height -getHeight())/2 );
                        setLocation(p);
                    }

                    try {

                            if (getProperties().getProperty("RestoreLayout","true").equals("true"))
                            {
                                DockingManager.restoreLayout();
                                if (DockingManager.isDocked( (Dockable)crosstabMeasuresPanelView))
                                {
                                    DockingManager.undock((Dockable)crosstabMeasuresPanelView);
                                }
                                if (DockingManager.isDocked( (Dockable)crosstabStructureView))
                                {
                                    DockingManager.undock((Dockable)crosstabStructureView);
                                }

                                jCheckBoxMenuItemDocumentStructure.setState(viewDocumentStructure.isShowing());
                                jCheckBoxMenuItemElementProperties.setState(viewPropertySheet.isShowing());
                                jCheckBoxMenuItemExplorer.setState( viewFiles.isShowing());

                                jCheckBoxMenuItemLibrary.setState( viewLibrary.isShowing());
                                jCheckBoxMenuItemStylesLibrary.setState( stylesPanleView.isShowing());
                                jCheckBoxMenuItemThreadList.setState( viewThreads.isShowing() );
                                jCheckBoxMenuItemOutput.setState( logPaneView.isShowing() );


                               if (!viewLibrary.isShowing())
                                {
                                    java.awt.Component comp = viewLibrary.getComponent();
                                    java.awt.Container parent = comp.getParent();
                                     if( parent != null && parent instanceof javax.swing.JTabbedPane )
                                     {
                                         ((javax.swing.JTabbedPane)parent).setSelectedIndex(0);
                                     }
                                }

                            }
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {}

        //this.setSize(dim);
        //this.getContentPane().repaint();
        //this.update(this.getGraphics());
        //this.setState(java.awt.Frame.MAXIMIZED_HORIZ);
    }//GEN-LAST:event_formWindowOpened

    /** Exit the Application */
    protected void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm

        if (isNoExit())
        {
            this.setVisible(false);
            return;
        }
        closeApplication();
    }//GEN-LAST:event_exitForm

    private void closeApplication() {
        try {
            this.saveiReportConfiguration();
        }
        catch(Throwable e) {
            e.printStackTrace(System.err);
        }


        //try {
        //    DockingManager.storeLayoutModel();
        //} catch(Exception ex) {
        //    ex.printStackTrace();
        //}


        if( this.isSaveAllRequired() && !getProperties().getProperty("AskToSave","true").equals("false")) {
            String message = I18n.getString("messages.unsavedFiles","There are unsaved files.  Would you like to save them before exiting?");
            String caption = I18n.getString("messages.unsavedFilesCaption","Unsaved files");
            int ret = javax.swing.JOptionPane.showConfirmDialog(this, message, caption, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
            switch(ret) {
                case javax.swing.JOptionPane.YES_OPTION:
                    java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent(jMenuItemSaveAll,0, this.jMenuItemSaveAll.getActionCommand());
                    jMenuItemSaveAllActionPerformed(ae);
                    break;
                case javax.swing.JOptionPane.NO_OPTION:
                    break;
                case javax.swing.JOptionPane.CANCEL_OPTION:
                    return;
            }
        }
        this.dispose();
        if(!isEmbedded()) {
            System.exit(0);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // command line arguments
        //StringParam patternArg =
        //    new StringParam("config-file", "",
        //                    StringParam.PUBLIC);


        System.setProperty("sun.swing.enableImprovedDragGesture","");

        FileParam configFileOpt =
        new FileParam("config-file",
        I18n.getString("cmdline.configFile", "the configuration file to use"),
        FileParam.IS_FILE & FileParam.IS_READABLE & FileParam.IS_WRITEABLE,
        FileParam.OPTIONAL);

        FileParam ireportHomeDirOpt =
        new FileParam("ireport-home",
        I18n.getString("cmdline.ireportHome", "iReport home directory"),
        FileParam.IS_DIR & FileParam.IS_READABLE & FileParam.IS_WRITEABLE,
        FileParam.OPTIONAL);

        FileParam userHomeDirOpt =
        new FileParam("user-home",
        I18n.getString("cmdline.userHome", "User home directory"),
        FileParam.IS_DIR & FileParam.IS_READABLE & FileParam.IS_WRITEABLE,
        FileParam.OPTIONAL);

        FileParam tempDirOpt =
        new FileParam("temp-dir",
        I18n.getString("cmdline.tmpDir", "Dir where store temporary java sources"),
        FileParam.IS_DIR & FileParam.IS_READABLE & FileParam.IS_WRITEABLE,
        FileParam.OPTIONAL);

        BooleanParam configSplashOpt =
        new BooleanParam("no-splash",
        I18n.getString("cmdline.noSplash", "not show the spash window"));

        BooleanParam configEmbeddedOpt =
        new BooleanParam("embedded",
        I18n.getString("cmdline.embedded", "avoid exit when the main window is closed"));

        BooleanParam configUseWebStartOpt =
        new BooleanParam("webstart",
        I18n.getString("cmdline.webstart", "enable a special class path management when Java Web Start is used to run iReport"));

        StringParam configBeanClassOpt =
        new StringParam("beanClass",
        I18n.getString("cmdline.beanClass", "show this class when open extended bean data source query editor"));

        FileParam filesArg =
        new FileParam("files",
        I18n.getString("cmdline.files", "xml file(s) to edit, supports use of wildcards "),
        FileParam.IS_FILE & FileParam.IS_READABLE,
        FileParam.OPTIONAL, FileParam.MULTI_VALUED);



        // command line options
        //BooleanParam ignorecaseOpt =
        //    new BooleanParam("ignoreCase", "ignore case while matching");
        //BooleanParam listfilesOpt =
        //    new BooleanParam("listFiles", "list filenames containing pattern");

        // a help text because we will use a HelpCmdLineHandler so our
        // command will display verbose help with the -help option
        String helpText = I18n.getString("cmdline.helpText","iReport line command options");
        CmdLineHandler cl =
        new VersionCmdLineHandler( MainFrame.getRebrandedTitle(),
        new HelpCmdLineHandler(helpText,
        "iReport",
        "Designer for JasperReports",
        new Parameter[] { configFileOpt, ireportHomeDirOpt, userHomeDirOpt, tempDirOpt, configSplashOpt, configEmbeddedOpt, configUseWebStartOpt, configBeanClassOpt},
        new Parameter[] { filesArg } ));

        cl.setParser(new PosixCmdLineParser());

        cl.setDieOnParseError(false);

        if (!cl.parse(args)) {
            // This prevent a call to exit from the parser.
            System.out.println(cl.getUsage(true));
            System.out.println(cl.getParseError());
            return;
        }

        Map map = new HashMap();
        if (configFileOpt.isSet()) {
            map.put("config-file", configFileOpt.getFile().getPath());
        }

        if (!configSplashOpt.isTrue()) {
            SplashDialog sp = new SplashDialog(null, false);
            sp.setVisible(true);
            map.put("splash", sp);
        }

        if (ireportHomeDirOpt.isSet()) {
            map.put("ireport-home", ireportHomeDirOpt.getFile().getPath());
        }

        if (userHomeDirOpt.isSet()) {
            map.put("user-home", userHomeDirOpt.getFile().getPath());
        }

        if (tempDirOpt.isSet()) {
            map.put("temp-dir", tempDirOpt.getFile().getPath());
        }

        if (filesArg.isSet()) {
            map.put("files", filesArg.getFiles());
        }

        if (configBeanClassOpt.isSet())
        {
            map.put("beanClass", configBeanClassOpt.getValue());
        }

        if (configEmbeddedOpt.isTrue())
        {
            map.put("embedded", "true");
        }

        if (configUseWebStartOpt.isTrue())
        {
            map.put("webstart", "true");
        }

        // Clear previus loaded boundle
        I18n.setCurrentLocale(java.util.Locale.getDefault());

        MainFrame.reportClassLoader.rescanLibDirectory();
        Thread.currentThread().setContextClassLoader( MainFrame.reportClassLoader );
        final MainFrame _mainFrame = new MainFrame(map);
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                _mainFrame.setVisible(true);
            }
        });
    }



    private static byte[] getBytesFromInputStream(java.io.InputStream in, int length)  throws java.io.IOException {
        java.io.DataInputStream din = new java.io.DataInputStream(in);
        byte[] bytecodes = new byte[length];
        try {
            din.readFully(bytecodes);
        } finally {
            if (din != null) din.close();
        }
        return bytecodes;
    }

    /** Gets the image to be displayed in the minimized icon
     * for this frame.
     * @return    the icon image for this frame, or <code>null</code>
     *                    if this frame doesn't have an icon image.
     * @see       #setIconImage(Icon)
     *
     */
    public java.awt.Image getIconImage() {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            //java.io.InputStream in = new java.io.FileInputStream( cl.getResource("it/businesslogic/ireport/icons/iconsmall.jpg").getPath() );
            java.io.InputStream in = cl.getResourceAsStream("it/businesslogic/ireport/icons/ireport_icon.png");
            byte[] data = getBytesFromInputStream(in, in.available());
            return java.awt.Toolkit.getDefaultToolkit().createImage(data);
        } catch (Exception ex)
        {ex.getMessage();
         ex.printStackTrace();
        }
        return null;
    }

    /** Getter for property elementPropertiesFrame.
     * @return Value of property elementPropertiesFrame.
     *
     */
    public ElementPropertiesDialog getElementPropertiesDialog() {
    	//
    	//ElementPropertiesDialog element= new ElementPropertiesDialog(new javax.swing.JFrame(), true);    	
    	//return element;
        return elementPropertiesDialog;
    }

    /** Setter for property elementPropertiesFrame.
     * @param elementPropertiesFrame New value of property elementPropertiesFrame.
     *
     */
    public void setElementPropertiesDialog(ElementPropertiesDialog elementPropertiesDialog) {
        this.elementPropertiesDialog = elementPropertiesDialog;
    }

    /** Getter for property valuesDialog.
     * @return Value of property valuesDialog.
     *
     */
    public it.businesslogic.ireport.gui.ValuesDialog getValuesDialog() {
        return valuesDialog;
    }

    /** Setter for property valuesDialog.
     * @param valuesDialog New value of property valuesDialog.
     *
     */
    public void setValuesDialog(it.businesslogic.ireport.gui.ValuesDialog valuesDialog) {
        this.valuesDialog = valuesDialog;
    }

    /** Getter for property groupsDialog.
     * @return Value of property groupsDialog.
     *
     */
    public it.businesslogic.ireport.gui.GroupsDialog getGroupsDialog() {
        return groupsDialog;
    }

    /** Setter for property groupsDialog.
     * @param groupsDialog New value of property groupsDialog.
     *
     */
    public void setGroupsDialog(it.businesslogic.ireport.gui.GroupsDialog groupsDialog) {
        this.groupsDialog = groupsDialog;
    }

    /** Getter for property bandsDialog.
     * @return Value of property bandsDialog.
     *
     */
    public BandsDialog getBandsDialog() {
        return bandsDialog;
    }

    /** Setter for property bandsDialog.
     * @param bandsDialog New value of property bandsDialog.
     *
     */
    public void setBandsDialog(BandsDialog bandsDialog) {
        this.bandsDialog = bandsDialog;
    }

    /** Getter for property reportQueryDialog.
     * @return Value of property reportQueryDialog.
     *
     */
    public it.businesslogic.ireport.gui.ReportQueryDialog getReportQueryDialog() {
        return reportQueryDialog;
    }

    /** Setter for property reportQueryDialog.
     * @param reportQueryDialog New value of property reportQueryDialog.
     *
     */
    public void setReportQueryDialog(it.businesslogic.ireport.gui.ReportQueryDialog reportQueryDialog) {
        this.reportQueryDialog = reportQueryDialog;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Filler;
    private javax.swing.JButton Filler1;
    private javax.swing.ButtonGroup buttonGroupExportType;
    private javax.swing.ButtonGroup buttonGroupFramesList;
    private javax.swing.JButton jButtonAlignBottom;
    private javax.swing.JButton jButtonAlignCenter;
    private javax.swing.JButton jButtonAlignJustify;
    private javax.swing.JButton jButtonAlignLeft;
    private javax.swing.JButton jButtonAlignMiddle;
    private javax.swing.JButton jButtonAlignRight;
    private javax.swing.JButton jButtonAlignTop;
    private javax.swing.JButton jButtonBands;
    private javax.swing.JButton jButtonBold;
    private javax.swing.JButton jButtonCompiler;
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonCut;
    private javax.swing.JButton jButtonDatabase;
    private javax.swing.JButton jButtonDecreaseSize;
    private javax.swing.JButton jButtonGroups;
    private javax.swing.JButton jButtonIncreaseSize;
    private javax.swing.JButton jButtonItalic;
    private javax.swing.JButton jButtonLens1;
    private javax.swing.JButton jButtonLens2;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonOpen;
    private javax.swing.JButton jButtonParameters;
    private javax.swing.JButton jButtonPaste;
    private javax.swing.JButton jButtonRun1;
    private javax.swing.JButton jButtonRun2;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAll;
    private javax.swing.JButton jButtonStrikethrought;
    private javax.swing.JButton jButtonUnderline;
    private javax.swing.JButton jButtonWizard;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemDocumentStructure;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemEMM;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemElementProperties;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemElementsFormatting;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemExplorer;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemGrid;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemIgnorePagination;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemLibrary;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemOutput;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemReportVirtualizer;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemSnapToGrid;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemStylesLibrary;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemTextFormatting;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemThreadList;
    private javax.swing.JComboBox jComboBoxFont;
    private javax.swing.JList jListThreads;
    private it.businesslogic.ireport.gui.JMDIDesktopPane jMDIDesktopPane;
    private it.businesslogic.ireport.gui.JMDIMenuBar jMDIMenuBar;
    private javax.swing.JMenu jMenuAdd;
    private javax.swing.JMenu jMenuAlign;
    private javax.swing.JMenu jMenuBuild;
    private javax.swing.JMenu jMenuDatabase;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuFormat;
    private javax.swing.JMenu jMenuHSpacing;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenu jMenuInternationalization;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemActiveConnection;
    private javax.swing.JMenuItem jMenuItemActiveConnection1;
    private javax.swing.JMenuItem jMenuItemAddGroup;
    private javax.swing.JMenuItem jMenuItemAlignBottom;
    private javax.swing.JMenuItem jMenuItemAlignHorizontalAxis;
    private javax.swing.JMenuItem jMenuItemAlignLeft;
    private javax.swing.JMenuItem jMenuItemAlignRight;
    private javax.swing.JMenuItem jMenuItemAlignToBandBottom;
    private javax.swing.JMenuItem jMenuItemAlignToBandTop;
    private javax.swing.JMenuItem jMenuItemAlignTop;
    private javax.swing.JMenuItem jMenuItemAlignVerticalAxis;
    private javax.swing.JMenuItem jMenuItemBands;
    private javax.swing.JMenuItem jMenuItemBringToFront;
    private javax.swing.JMenuItem jMenuItemCascade;
    private javax.swing.JMenuItem jMenuItemCenterBackground;
    private javax.swing.JMenuItem jMenuItemCenterH;
    private javax.swing.JMenuItem jMenuItemCenterInBand;
    private javax.swing.JMenuItem jMenuItemCenterV;
    private javax.swing.JMenuItem jMenuItemChart;
    private javax.swing.JMenuItem jMenuItemClasspath;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemCloseAll;
    private javax.swing.JMenuItem jMenuItemCloseAllExceptThis;
    private javax.swing.JMenuItem jMenuItemCloseAllExceptThisFromList;
    private javax.swing.JMenuItem jMenuItemCloseAllFromList;
    private javax.swing.JMenuItem jMenuItemCloseFromList;
    private javax.swing.JMenuItem jMenuItemCompatibility;
    private javax.swing.JMenuItem jMenuItemCompile;
    private javax.swing.JMenuItem jMenuItemConnections;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCreateFromTemplate;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemElementProperties;
    private javax.swing.JMenuItem jMenuItemEllipse;
    private javax.swing.JMenuItem jMenuItemExecute;
    private javax.swing.JMenuItem jMenuItemExecuteDB;
    private javax.swing.JMenuItem jMenuItemExportOptions;
    private javax.swing.JMenuItem jMenuItemExpressions;
    private javax.swing.JMenuItem jMenuItemFields;
    private javax.swing.JMenuItem jMenuItemFonts;
    private javax.swing.JMenuItem jMenuItemFontspath;
    private javax.swing.JMenuItem jMenuItemForum;
    private javax.swing.JMenuItem jMenuItemGroups;
    private javax.swing.JMenuItem jMenuItemHSDecrease;
    private javax.swing.JMenuItem jMenuItemHSIncrease;
    private javax.swing.JMenuItem jMenuItemHSMakeEqual;
    private javax.swing.JMenuItem jMenuItemHSRemove;
    private javax.swing.JMenuItem jMenuItemHelp;
    private javax.swing.JMenuItem jMenuItemHomePage;
    private javax.swing.JMenuItem jMenuItemImage;
    private javax.swing.JMenuItem jMenuItemInsertPageBreak;
    private javax.swing.JMenuItem jMenuItemJoinLeft;
    private javax.swing.JMenuItem jMenuItemJoinRight;
    private javax.swing.JMenuItem jMenuItemKill;
    private javax.swing.JMenuItem jMenuItemLeftMargin;
    private javax.swing.JMenuItem jMenuItemLine;
    private javax.swing.JMenuItem jMenuItemMaxRecords;
    private javax.swing.JMenuItem jMenuItemNewDocument;
    private javax.swing.JMenuItem jMenuItemNextWin;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemOptions;
    private javax.swing.JMenuItem jMenuItemOrganize;
    private javax.swing.JMenuItem jMenuItemParameters;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemPluginConfig;
    private javax.swing.JMenuItem jMenuItemPrevWin;
    private javax.swing.JMenuItem jMenuItemPrint;
    private javax.swing.JMenuItem jMenuItemProperties;
    private javax.swing.JMenuItem jMenuItemQueryExecuters;
    private javax.swing.JMenuItem jMenuItemQuit;
    private javax.swing.JMenuItem jMenuItemRectangle;
    private javax.swing.JMenuItem jMenuItemRedo;
    private javax.swing.JMenuItem jMenuItemReloadFonts;
    private javax.swing.JMenuItem jMenuItemRemoveMargins;
    private javax.swing.JMenuItem jMenuItemReportImports;
    private javax.swing.JMenuItem jMenuItemReportLocale;
    private javax.swing.JMenuItem jMenuItemReportProperties;
    private javax.swing.JMenuItem jMenuItemReportQuery;
    private javax.swing.JMenuItem jMenuItemReportQuery2;
    private javax.swing.JMenuItem jMenuItemReportTimeZone;
    private javax.swing.JMenuItem jMenuItemRevertToSaved;
    private javax.swing.JMenuItem jMenuItemRevertToSavedFromList;
    private javax.swing.JMenuItem jMenuItemRightMargin;
    private javax.swing.JMenuItem jMenuItemRightSide;
    private javax.swing.JMenuItem jMenuItemRoundRectangle;
    private javax.swing.JMenuItem jMenuItemSameHeight;
    private javax.swing.JMenuItem jMenuItemSameHeightMax;
    private javax.swing.JMenuItem jMenuItemSameHeightMin;
    private javax.swing.JMenuItem jMenuItemSameSize;
    private javax.swing.JMenuItem jMenuItemSameWidth;
    private javax.swing.JMenuItem jMenuItemSameWidthMax;
    private javax.swing.JMenuItem jMenuItemSameWidthMin;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAll;
    private javax.swing.JMenuItem jMenuItemSaveAllFromList;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JMenuItem jMenuItemSaveAsFromList;
    private javax.swing.JMenuItem jMenuItemSaveFromList;
    private javax.swing.JMenuItem jMenuItemScriptletEditor;
    private javax.swing.JMenuItem jMenuItemSendToBack;
    private javax.swing.JMenuItem jMenuItemStaticText;
    private javax.swing.JMenuItem jMenuItemStyles;
    private javax.swing.JMenuItem jMenuItemSubDataset;
    private javax.swing.JMenuItem jMenuItemSubDataset1;
    private javax.swing.JMenuItem jMenuItemSubreport;
    private javax.swing.JMenuItem jMenuItemTextField;
    private javax.swing.JMenuItem jMenuItemTileHorizontal;
    private javax.swing.JMenuItem jMenuItemTileVertical;
    private javax.swing.JMenuItem jMenuItemUndo;
    private javax.swing.JMenuItem jMenuItemVSDecrease;
    private javax.swing.JMenuItem jMenuItemVSIncrease;
    private javax.swing.JMenuItem jMenuItemVSMakeEqual;
    private javax.swing.JMenuItem jMenuItemVSRemove;
    private javax.swing.JMenuItem jMenuItemVariables;
    private javax.swing.JMenuItem jMenuItemWizard;
    private javax.swing.JMenuItem jMenuItemXMLSource;
    private javax.swing.JMenuItem jMenuItemtileAnodine;
    private javax.swing.JMenuItem jMenuLocaleFiles;
    private javax.swing.JMenu jMenuPanels;
    private javax.swing.JMenu jMenuPlugins;
    private javax.swing.JMenu jMenuLocalGIS;
    private javax.swing.JMenuItem jMenuItemLocalGISLogout;
    private javax.swing.JMenuItem jMenuItemLocalGISSendToServer;

    private javax.swing.JMenu jMenuPosition;
    private javax.swing.JMenu jMenuRecentFiles;
    private javax.swing.JMenu jMenuSize;
    private javax.swing.JMenu jMenuToolBars;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JMenu jMenuVSpacing;
    private javax.swing.JMenu jMenuView;
    private javax.swing.JMenu jMenuWindow;
    private it.businesslogic.ireport.gui.JNumberComboBox jNumberComboBoxSize;
    private it.businesslogic.ireport.gui.JNumberComboBox jNumberComboBoxZoom;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelFiles;
    private javax.swing.JPanel jPanelMaster;
    private javax.swing.JPanel jPanelZoom;
    private javax.swing.JPopupMenu jPopupMenuFiles;
    private javax.swing.JPopupMenu jPopupMenuThreads;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewCSV;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewHTML;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewInternalViewer;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewJAVA;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewODF;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewPDF;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewRTF;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewTXT;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewTXTJR;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewXLS;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemPreviewXLS2;
    private javax.swing.JScrollPane jScrollPaneFiles;
    private javax.swing.JScrollPane jScrollProcesses;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JToggleButton jToggleButtonBarcodeTool;
    private javax.swing.JToggleButton jToggleButtonChartTool;
    private javax.swing.JToggleButton jToggleButtonCrosstabTool;
    private javax.swing.JToggleButton jToggleButtonEllipseTool;
    private javax.swing.JToggleButton jToggleButtonFrameTool;
    private javax.swing.JToggleButton jToggleButtonImageTool;
    private javax.swing.JToggleButton jToggleButtonLineTool;
    private javax.swing.JToggleButton jToggleButtonMapImageTool;
    private javax.swing.JToggleButton jToggleButtonPointer;
    private javax.swing.JToggleButton jToggleButtonRectTool;
    private javax.swing.JToggleButton jToggleButtonStaticTextTool;
    private javax.swing.JToggleButton jToggleButtonSubreportTool;
    private javax.swing.JToggleButton jToggleButtonTextFieldTool;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBarConnections;
    private javax.swing.JToolBar jToolBarFormat;
    private javax.swing.JToolBar jToolBarText;
    private javax.swing.JTree jTreeFiles;
    // End of variables declaration//GEN-END:variables


    /**
     * Load expressions from IREPORT_EXPRESSIONS_FILE and set the static vector in
     * the ExpressionEditor.
     */
    public void loadExpressionsList() {

        Vector v = Misc.loadExpressionsList(IREPORT_EXPRESSIONS_FILE);
        ExpressionEditor.defaultExpressions = v;
    }

    /**
     * Load expressions from IREPORT_EXPRESSIONS_FILE and set the static vector in
     * the ExpressionEditor.
     */
    public void saveExpressionsList(Vector v) {

        Misc.saveExpressionsList(v, IREPORT_EXPRESSIONS_FILE);
        ExpressionEditor.defaultExpressions = v;
    }




    /* This method load:
     *  recent file list
     *  recent project list
     *  favorites files list
     */
    public void loadFileLists() {

        loadFileList(this.getRecentFilesList(), IREPORT_RECENT_FILES_FILE);
        //loadFileList(this.getRecentProjectsList(), IREPORT_RECENT_PROJECTS_FILE);
        // First we must implement it....
        //loadFileList(this.getFavoriteFilesList() , IREPORT_FAVORITE_FILES_FILE);
    }



    private void loadFileList(Vector v, String xmlFile) {
        File file = null;
        try {
            file = new File(xmlFile);
        } catch (Exception ex)
        {}
        if (file == null || !file.exists() || file.isDirectory()) {
            try {
                outputBuffer.setLength(0);
                getLogPane().getMainLogTextArea().logOnConsole(
                            I18n.getFormattedString("messages.errorLoadingConfig", "Error loading Configuration file: {0}\n", new Object[]{xmlFile} ),
                        JOptionPane.ERROR_MESSAGE);
                
            } catch (Exception exsx)
            {}
            return;
        }
        //  Create a Xerces DOM Parser
        DOMParser parser = new DOMParser();
        //  Parse the Document
        //  and traverse the DOM
        try {

            parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                /* Code by Teodor Danciu */
                public org.xml.sax.InputSource resolveEntity(
                String publicId,
                String systemId
                ) throws SAXException//, java.io.IOException
                {
                    org.xml.sax.InputSource inputSource = null;

                    if (systemId != null) {
                        String dtd = null;

                        if (
                        systemId.equals("http://ireport.sourceforge.net/dtds/iReportFilesList.dtd")
                        ) {
                            dtd = "it/businesslogic/ireport/dtds/iReportFilesList.dtd";
                        }
                        else {
                            return new org.xml.sax.InputSource(systemId);
                        }

                        ClassLoader classLoader = this.getClass().getClassLoader();

                        java.net.URL url = null;


                        if (classLoader != null) {
                            url = classLoader.getResource(dtd);
                        }
                        if (url == null) {
                            classLoader = this.getClass().getClassLoader();
                        }

                        java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                        if (is != null) {
                            java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                            inputSource = new org.xml.sax.InputSource(isr);
                        }

                    }

                    return inputSource;
                }
            });
            /* End Code by Teodor Danciu */
            parser.parse( new java.io.File(xmlFile).toURI().toString() );
            Document document = parser.getDocument();

            // Traverse the tree until we don't find a iReportFilesList element...
            Node fileList = goToNodeElement("iReportFilesList",document.getDocumentElement());
            if (fileList == null) return;
            NodeList list = fileList.getChildNodes();
            for (int i=0; i < list.getLength(); ++i) {
                Node child = list.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("iReportFile")) {
                    String fileName = Report.readPCDATA(child);
                    if (fileName != null && fileName.trim().length()>0) {
                        v.addElement( fileName );
                    }
                }

            }

        } catch (Exception ex) {
        }
    }

    static public Node goToNodeElement(String name, Node node ) {
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("iReportFilesList")) {
            return node;
        }

        NodeList list = node.getChildNodes();
        for (int i=0; i < list.getLength(); ++i) {
            Node child = goToNodeElement(name, list.item(i) );
            if (child != null) return child;
        }

        return null;
    }

    /* iReport configuration loader...
     */
    public void loadiReportConfiguration() {
        loadiReportConfiguration( IREPORT_CONFIG_FILE );
    }



     /* iReport configuration loader...
      */
    public void loadiReportConfiguration(String xmlFile) {
        // Adding default properties...
        properties.put("RecentFilesLength","10");
        properties.put("ViewRules","true");
        properties.put("GridSize","10");
        properties.put("ViewBorderForTextElements","true");
        properties.put("DefaultTemplate","");
        properties.put("DefaultCompilationDirectory",".");
        properties.put("BackupPolicy","2");
        properties.put("DefaultBackupDirectory","");
        properties.put("ExternalEditor","");
        properties.put("ExternalPDFViewer","");
        properties.put("ExternalHTMLViewer","");
        properties.put("ExternalXLSViewer","");
        properties.put("ExternalCSVViewer","");
        properties.put("ViewBorderForTextElements","true");
        properties.put("DefaultUnit","cm");
        properties.put("SaveBeforeCompiling","true");
        properties.put("LookAndFeel", "System");
        properties.put("UseAutoRegiesterFields", "true");
        properties.put("Language", java.util.Locale.getDefault().getLanguage());
        if (java.util.Locale.getDefault().getCountry() != null){
        	properties.put("Country", java.util.Locale.getDefault().getCountry());
        }
        else {
        	properties.put("Country", "");
        }
        properties.put("Compatibility", "" + CompatibilitySupport.LAST_AVAILABLE_VERSION);
        properties.put("toolbarFormattingVisible","true");
        properties.put("toolbarTextVisible","true");
        properties.put("showGrid","false");
        properties.put("snapToGrid","false");

        File file = null;
        try {
            file = new File(xmlFile);
        } catch (Exception ex)
        {}
        if (file == null || !file.exists() || file.isDirectory()) {
            try {
                outputBuffer.setLength(0);
                
                /*getLogPane().getMainLogTextArea().logOnConsole(
                            I18n.getFormattedString("messages.errorLoadingConfigDefParams", "Error loading Configuration file: {0}\nUsing default parameters.", new Object[]{xmlFile} ),
                        JOptionPane.ERROR_MESSAGE);*/

            } catch (Exception exsx)
            {}
            return;
        }
        //  Create a Xerces DOM Parser
        DOMParser parser = new DOMParser();
        //  Parse the Document
        //  and traverse the DOM
        try {

            parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                /* Code by Teodor Danciu */
                public org.xml.sax.InputSource resolveEntity(
                String publicId,
                String systemId
                ) throws SAXException//, java.io.IOException
                {
                    org.xml.sax.InputSource inputSource = null;

                    if (systemId != null) {
                        String dtd = null;

                        if ( systemId.equals("http://ireport.sourceforge.net/dtds/iReportProperties.dtd") ) {
                            dtd = "it/businesslogic/ireport/dtds/iReportProperties.dtd";
                        } else {
                            return new org.xml.sax.InputSource(systemId);
                        }


                        ClassLoader classLoader = this.getClass().getClassLoader();

                        java.net.URL url = null;


                        if (classLoader != null) {
                            url = classLoader.getResource(dtd);
                        }
                        if (url == null) {
                            classLoader = this.getClass().getClassLoader();
                        }

                        java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                        if (is != null) {
                            java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                            inputSource = new org.xml.sax.InputSource(isr);
                        }

                    }

                    return inputSource;
                }
            });
            /* End Code by Teodor Danciu */
            parser.parse( new java.io.File(xmlFile).toURI().toString() );
            Document document = parser.getDocument();

            traverse(document.getDocumentElement());
            
//            String defaultConnection = Misc.nvl(properties.get("DefaultConnection"),"");
//            
//            //properties.put("DefaultConnection", null); // No default connection...
//            if (!defaultConnection.equals("")) {
//                // Looking for a connection width this name..,
//                Enumeration e = getConnections().elements();
//                while (e.hasMoreElements()) {
//                    IReportConnection irc = (IReportConnection)e.nextElement();
//                    if (Misc.nvl(irc.getName(),"").equals( defaultConnection )) {
//                        properties.put("DefaultConnection", irc);
//                        break;
//                    }
//                }
//            }
//            else if (getConnections().size() > 0)
//            {
//            	IReportConnection irc = (IReportConnection)getConnections().elementAt(0);
//            	properties.put("DefaultConnection", irc);
//            }
 
            String currentDirectory = Misc.nvl(properties.get("CurrentDirectory"),"");
            if (!currentDirectory.equals("")) {
                this.setCurrentDirectory(new java.io.File( currentDirectory ), false);
            }

            //I changed this from the working directory to the ireport IREPORT_DEFAULT_HOME_DIR
            // Now is set in setiReportPaths ... setHomeDirectory(Misc.nvl(System.getProperty("ireport.home"), IREPORT_DEFAULT_HOME_DIR));
            this.setDefaultCompilationDirectory(properties.getProperty("DefaultCompilationDirectory"));

            // We force to use classic expressions
            properties.put("usingMultiLineExpressions", "false");

            CompatibilitySupport.version = Integer.parseInt( properties.getProperty("Compatibility") );

            // Write iReport location...
            try {
                String locationFileName = IREPORT_USER_HOME_DIR + File.separator + "ireport" + MainFrame.constTitle.substring(8) + ".location";
                if (!(new File(locationFileName).exists()))
                {
                    PrintWriter pw = new PrintWriter( new FileOutputStream( locationFileName ));
                    String s = new File(IREPORT_HOME_DIR).getAbsolutePath();
                    pw.print(s);
                    pw.close();
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

        } catch (SAXException e) {
            System.err.println(e);
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }

    /* iReport configuration loader...
     */
    public void loadStyleLibrary() {
        loadStyleLibrary( IREPORT_STYLE_LIBRARY );
    }

    /* iReport configuration loader...
     */
    public void loadStyleLibrary(String fileName) {
        setStyleLibrarySet( Misc.loadStyleLibrary(fileName) );
    }

    /* iReport style library configuration saver...
     */
    public void saveStyleLibrary() {

        saveStyleLibrary(IREPORT_STYLE_LIBRARY);

    }

    /* iReport style library configuration saver...
     */
    public void saveStyleLibrary(String fileName) {

        Misc.saveStyleLibrary(fileName, getStyleLibrarySet());

    }

    /**
     *  Set the current directory.
     *  If save = true, the config file is updates...
     */
    public File setCurrentDirectory( File f, boolean save) {
        String currentDirectory = "";
        if( f == null ) return f;

        try {
            if( f.isDirectory() ) {
                currentDirectory = f.getAbsolutePath();
            } else {
                currentDirectory = f.getParentFile().getAbsolutePath();
            }

            if (save) {
                getProperties().put("CurrentDirectory", currentDirectory);
                this.saveiReportConfiguration();
            }
        } catch (Exception ex) {

        }
        return f;
    }

    private void traverse(Node node) {
        //System.out.println("traverse");
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("iReportProperties")) {
            //System.out.println("iReportProperties");
            // Get childs....
            NodeList children = node.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);
                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("iReportProperty")) {

                        // Take the CDATA...
                        String paramName = "";
                        NamedNodeMap nnm = nodeChild.getAttributes();
                        if ( nnm.getNamedItem("name") != null) paramName = nnm.getNamedItem("name").getNodeValue();
                        //System.out.println("     iReportProperty "+nnm.getNamedItem("name").getNodeValue());
                        properties.put(paramName,  readPCDATA(nodeChild) );

                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("iReportConnection")) {
                        // Take the CDATA...
                        String connectionName = "";
                        String connectionClass = "";
                        HashMap hm = new HashMap();
                        NamedNodeMap nnm = nodeChild.getAttributes();
                        if ( nnm.getNamedItem("name") != null) connectionName = nnm.getNamedItem("name").getNodeValue();
                        if ( nnm.getNamedItem("connectionClass") != null) connectionClass = nnm.getNamedItem("connectionClass").getNodeValue();

                        // Get all connections parameters...
                        NodeList list_child = nodeChild.getChildNodes();
                        for (int ck=0; ck< list_child.getLength(); ck++) {
                            String parameterName = "";
                            Node child_child = (Node)list_child.item(ck);
                            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("connectionParameter")) {

                                NamedNodeMap nnm2 = child_child.getAttributes();
                                if ( nnm2.getNamedItem("name") != null)
                                    parameterName = nnm2.getNamedItem("name").getNodeValue();
                                hm.put( parameterName,readPCDATA(child_child));
                            }
                        }
                        try {
                            IReportConnection con = (IReportConnection) Class.forName(connectionClass).newInstance();
                            con.loadProperties(hm);
                            con.setName(connectionName);
                            connections.addElement( con );
                        } catch (Exception ex) {
                            try {
                                
                                getLogPane().getMainLogTextArea().logOnConsole(
                                            I18n.getFormattedString("messages.errorLoadingConnection", "Error loading: {0}\n{1}\n", new Object[]{connectionName, ex.getMessage()} ),
                                JOptionPane.ERROR_MESSAGE);
                                
                                

                            } catch (Exception exsx)
                            {}
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("QueryExecuter")) {
                        // Take the CDATA...
                        QueryExecuterDef qe = new QueryExecuterDef();

                        HashMap hm = new HashMap();
                        NamedNodeMap nnm = nodeChild.getAttributes();
                        if ( nnm.getNamedItem("factory") != null) qe.setClassName( nnm.getNamedItem("factory").getNodeValue());
                        if ( nnm.getNamedItem("fieldsProvider") != null) qe.setFieldsProvider( nnm.getNamedItem("fieldsProvider").getNodeValue());
                        qe.setLanguage( readPCDATA(nodeChild) );

                        getQueryExecuters().add(qe);
                    }
                }
            }
        }
    }
    /** Getter for property connections.
     * @return Value of property connections.
     *
     */
    public java.util.Vector getConnections() {
        return connections;
    }

    /** Setter for property connections.
     * @param connections New value of property connections.
     *
     */
    public void setConnections(java.util.Vector connections) {
        this.connections = connections;
    }

//    public String getI18nString(String s) {
//        return it.businesslogic.ireport.util.I18n.getString(s, it.businesslogic.ireport.util.I18n.getCurrentLocale());
//    }

    /** Getter for property properties.
     * @return Value of property properties.
     *
     */
    public Properties getProperties() {
        return properties;
    }

    /** Setter for property properties.
     * @param properties New value of property properties.
     *
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private String readPCDATA(Node textNode) {
        NodeList list_child = textNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child_child = (Node)list_child.item(ck);
            if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
            child_child.getNodeType() == Node.TEXT_NODE) {
                return (String)child_child.getNodeValue();
            }
        }
        return "";
    }

    public boolean saveiReportConfiguration() {
        // Get the path of this class...
        // this.getClass().getResource("MainFrame");
        File dir = new File(IREPORT_USER_HOME_DIR);
        try {
            if (dir.exists()) {
                if (!dir.isDirectory() ) {
                    javax.swing.JOptionPane.showMessageDialog( this,
                            I18n.getFormattedString("messages.errorIsDirectory",
                            "{0} is not a directory!\nPlease rename this file and retry to save config!",new Object[]{dir.getPath() + ""}),"",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            else {
                dir.mkdirs();
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog( this,
                    I18n.getFormattedString("messages.errorSavingConfigDialog",
                    "An error is occurred saving iReport config:\n{0}\nPlease try to save config again!", new Object[]{""+ex.getMessage()}),"",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return saveiReportConfiguration( IREPORT_CONFIG_FILE );
    }
    public boolean saveiReportConfiguration(String xmlFile) {
        try {
            //PrintWriter pw = new PrintWriter( new FileWriter(xmlFile) );

            PrintWriter pw = new PrintWriter( new OutputStreamWriter(new
                        FileOutputStream(xmlFile),"UTF-8"));

            // Find iReport configuration in the home directory...
            // 1. Save all properties using am XML style...

            if (getConnections().size() > 0 && properties.get("DefaultConnection") == null)
            {
            	IReportConnection irc = (IReportConnection)getConnections().elementAt(0);
            	properties.put("DefaultConnection", irc);
            }
/*
            this.getProperties().setProperty("RightColumnVisible",""+ (jSplitPaneHelp.getDividerSize() != 0));
            if (jSplitPaneHelp.getDividerSize() != 0)
            {
                this.getProperties().setProperty("RightColumnWidth",""+ dockingContainerRight.getWidth());
            }
*/
            //System.out.println("divider location:" + jSplitPaneHelp.getDividerLocation() + "   " + (jSplitPaneHelp.getWidth()-8-rightColumnWidth));


            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!-- iReport 2 Configuration file - " + new Date() + " -->");
            pw.println("<!DOCTYPE iReportProperties PUBLIC \"-//iReport/DTD iReport Configuration//EN\" \"http://ireport.sourceforge.net/dtds/iReportProperties.dtd\">");
        pw.println("<iReportProperties>");

            Enumeration e = this.getProperties().keys();
            while (e.hasMoreElements()) {
                String key = (String)e.nextElement();
                pw.println("\t<iReportProperty name=\""+ key +"\"><![CDATA["+ this.getProperties().get(key) +"]]></iReportProperty>");
            }

            e = this.getConnections().elements();
            while (e.hasMoreElements()) {
                IReportConnection con = (IReportConnection)e.nextElement();
                con.save(pw);
            }

            e = this.getQueryExecuters().elements();
            while (e.hasMoreElements()) {
                QueryExecuterDef qe = (QueryExecuterDef)e.nextElement();
                pw.println("\t<QueryExecuter factory=\""+ qe.getClassName() +"\" fieldsProvider=\""+ qe.getFieldsProvider() +"\"><![CDATA["+ qe.getLanguage() +"]]></QueryExecuter>");
            }

            pw.println("</iReportProperties>");
            pw.close();
        } catch (Exception ex) {
            //ex.printStackTrace(s
            try {
                
                getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.errorSavingConfig", "Error saving Configuration file: {0}\n{1}\n", new Object[]{xmlFile, ex.getMessage()} ),
                                JOptionPane.ERROR_MESSAGE);

            } catch (Exception exsx)
            {}
            return false;
        }
        try {
            
                getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.configUpdated", "iReport configuration file {0} successfully updated!\n", new Object[]{xmlFile} ),
                                JOptionPane.INFORMATION_MESSAGE);
        
        } catch (Exception ex)
        { }
        return true;
    }



    public boolean saveFileList( Vector v, String xmlFile) {
        // Get the path of this class...
        // this.getClass().getResource("MainFrame");

        File dir = new File( IREPORT_USER_HOME_DIR );
        try {
            if (dir.exists()) {
                if (!dir.isDirectory() ) {
                    javax.swing.JOptionPane.showMessageDialog( this,
                            I18n.getFormattedString("messages.errorIsDirectory",
                            "{0} is not a directory!\nPlease rename this file and retry to save config!",new Object[]{dir.getPath() + ""}),"",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            else {
                dir.mkdirs();
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog( this,
                    I18n.getFormattedString("messages.errorSavingConfigDialog",
                    "An error is occurred saving iReport config:\n{0}\nPlease try to save config again!", new Object[]{""+ex.getMessage()}),"",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //xmlFile = dir.getPath()+ File.separatorChar + xmlFile;

        try {
            PrintWriter pw = new PrintWriter( new FileWriter(xmlFile) );
            // Find iReport configuration in the home directory...
            // 1. Save all properties using am XML style...
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!-- iReport 2 file list - " + new Date() + " -->");
            pw.println("<!DOCTYPE iReportFilesList PUBLIC \"-//iReport/DTD iReport Configuration//EN\" \"http://ireport.sourceforge.net/dtds/iReportFilesList.dtd\">");
            pw.println("<iReportFilesList>");

            Enumeration e = v.elements();
            int i=0;
            int max_files = 10;
            try {
                max_files = Integer.parseInt(getProperties().getProperty("RecentFilesLength"));
            } catch (Exception ex) {
                getProperties().setProperty("RecentFilesLength","10");
            }

            while (i < max_files && e.hasMoreElements()) {
                String file = (String)e.nextElement();
                pw.println("\t<iReportFile><![CDATA["+ file +"]]></iReportFile>");
                i++;
            }
            pw.println("</iReportFilesList>");
            pw.close();
        } catch (Exception ex) {
            //ex.printStackTrace(s
            try {
                
                getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.errorSavingIReportFile","Error saving iReport file: {0}\n{1}\n", new Object[]{xmlFile,ex.getMessage()} ),
                                JOptionPane.ERROR_MESSAGE);
                
            } catch (Exception exsx)
            {}
            return false;
        }
        try {
            
            getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.fileUpdated","iReport file {0} successfully updated!\n", new Object[]{xmlFile} ),
                                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex)
        { }
        return true;
    }



    public void logOnConsole(String noHTML) {
        getLogPane().getMainLogTextArea().logOnConsole(noHTML,false);
    }

    public void logOnConsole(String s, boolean isHTML) {
        getLogPane().getMainLogTextArea().logOnConsole(s,isHTML);
    }
    
    

    /** Getter for property ttfFonts.
     * @return Value of property ttfFonts.
     *
     */
    public java.util.Vector getTtfFonts() {
        return ttfFonts;
    }

    /** Setter for property ttfFonts.
     * @param ttfFonts New value of property ttfFonts.
     *
     */
    public void setTtfFonts(java.util.Vector ttfFonts) {
        this.ttfFonts = ttfFonts;
    }


    public void reportBandChanged(ReportBandChangedEvent evt) {
        evt.getJReportFrame().getReport().incrementReportChanges();
        fireReportListenerReportBandChanged( evt );
    }

    public void reportElementsChanged(ReportElementChangedEvent evt) {

        fireReportListenerReportElementsChanged( evt );
        //we need to set the report to show it has been modified.
        Report mod = evt.getJReportFrame().getReport();
        mod.incrementReportChanges();
        //mod.setModified(true);
        this.enableSaveSpecific();
        this.enableSaveAllSpecific();

    }//end reportElementsChanged

    /**
     *Used to enable anything strictly save related.  Not save all.
     */
    public void enableSaveSpecific() {
        //TODO add events to update gui when changes occur in expressions.
        //this had to be commented out until that day arrives.
        //brainjava will be working on this
       /*
       this.jButtonSave.setEnabled(true);
       this.jMenuItemSave.setEnabled(true);
        **/
    }

    /**
     *Used to disable anything Save.  Not save all.
     */
    public void disableSaveSpecific() {

        //TODO add more events so code can be comented back in
        //this had to be removed until events can be added to
        //the expression updates so the gui will know changes have
        //occurred
       /*
       this.jButtonSave.setEnabled(false);
       this.jMenuItemSave.setEnabled(false);
        **/
    }//end disableSaveSpecific

    /**
     *Used to enable anything strictly save all related.  Not save.
     */
    public void enableSaveAllSpecific() {
        this.jMenuItemSaveAll.setEnabled(true);
        this.jButtonSaveAll.setEnabled(true);
    }//end enableSaveAllSpecific

    /**
     *Used to disable anything Save.  Not save all.
     */
    public void disableSaveAllSpecific() {
        this.jMenuItemSaveAll.setEnabled(false);
        this.jButtonSaveAll.setEnabled(false);
    }//end disableSaveAllSpecific



    /**     This method is called when a new element is selected,
     *     or deselected.
     *
     */
    public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt) {

        
        Vector activeSelection = this.getActiveElementSelection(getActiveReportFrame());

        setDontHandleEvent(true);

        try {

            // we have to synchronize the selection with the toolbar state...

            boolean sameFontName = true;
            boolean sameFontSize = true;

            boolean isTheFirstElement = true;



            if (activeSelection.size() == 0)
            {
                ElementPropertiesDialog.setComboBoxText(isTheFirstElement, "SansSerif" , jComboBoxFont);
                ElementPropertiesDialog.setElementComboNumber(isTheFirstElement, 10, jNumberComboBoxSize );
            }
            else
            {
                for (int i=0; i<activeSelection.size(); ++i)
                {
                    ReportElement re = (ReportElement)activeSelection.elementAt(i);
                    if (re instanceof TextReportElement)
                    {
                        TextReportElement tre = (TextReportElement)re;

                        String fontName = tre.getFontName();
                        if (fontName == null || fontName.trim().length() == 0) fontName ="SansSerif";

                        if (sameFontName) sameFontName = ElementPropertiesDialog.setComboBoxText(isTheFirstElement, fontName , jComboBoxFont);
                        if (sameFontSize) sameFontSize = ElementPropertiesDialog.setElementComboNumber(isTheFirstElement, tre.getFontSize() , jNumberComboBoxSize );

                        isTheFirstElement = false;
                    }
                }
            }

        } catch (Exception ex)
        {

        }
        finally
        {
            setDontHandleEvent(false);
        }

        fireReportListenerReportElementsSelectionChanged( evt );
        
    }

    /** Getter for property dontHandleEvent.
     * @return Value of property dontHandleEvent.
     *
     */
    public boolean isDontHandleEvent() {
        return dontHandleEvent;
    }

    /** Setter for property dontHandleEvent.
     * @param dontHandleEvent New value of property dontHandleEvent.
     *
     */
    public void setDontHandleEvent(boolean dontHandleEvent) {
        this.dontHandleEvent = dontHandleEvent;
    }

    /** Getter for property recentFilesList.
     * @return Value of property recentFilesList.
     *
     */
    public java.util.Vector getRecentFilesList() {
        return recentFilesList;
    }

    /** Setter for property recentFilesList.
     * @param recentFilesList New value of property recentFilesList.
     *
     */
    public void setRecentFilesList(java.util.Vector recentFilesList) {
        this.recentFilesList = recentFilesList;
    }

    /** Getter for property recentProjectsList.
     * @return Value of property recentProjectsList.
     *
     */
    public java.util.Vector getRecentProjectsList() {
        return recentProjectsList;
    }

    /** Setter for property recentProjectsList.
     * @param recentProjectsList New value of property recentProjectsList.
     *
     */
    public void setRecentProjectsList(java.util.Vector recentProjectsList) {
        this.recentProjectsList = recentProjectsList;
    }

    /** Getter for property favoriteFilesList.
     * @return Value of property favoriteFilesList.
     *
     */
    public java.util.Vector getFavoriteFilesList() {
        return favoriteFilesList;
    }

    /** Setter for property favoriteFilesList.
     * @param favoriteFilesList New value of property favoriteFilesList.
     *
     */
    public void setFavoriteFilesList(java.util.Vector favoriteFilesList) {
        this.favoriteFilesList = favoriteFilesList;
    }

    private void jMenuItemRecentFileActionPerformed(java.awt.event.ActionEvent evt ) {
        if (evt.getSource() instanceof JMenuItem) {
            JMenuItem menu = (JMenuItem)evt.getSource();

            // Is a recent file?!
            for (int i=0; i< jMenuRecentFiles.getMenuComponentCount(); ++ i) {
                if (jMenuRecentFiles.getMenuComponent(i) ==  menu) {

                    // If not already Opened, open entry # i
                    JReportFrame frameReport = openFile( (String)recentFilesList.elementAt(i));
                    if(frameReport != null){

                        Report report = frameReport.getReport();

                        if( report.isModified() ) {
                            this.enableSaveSpecific();
                            this.enableSaveAllSpecific();
                        }
                        else {
                            this.disableSaveSpecific();
                            if( this.isSaveAllRequired() ) {
                                this.enableSaveAllSpecific();
                            }
                            else {
                                this.disableSaveAllSpecific();
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void updateRecentFileMenu(JMenu menu, Vector v) {
        menu.removeAll();
        Enumeration e = v.elements();
        int i=1;

        int k=0;
        int max_files = 10;
        try {
            max_files = Integer.parseInt(getProperties().getProperty("RecentFilesLength"));
        } catch (Exception ex) {
            getProperties().setProperty("RecentFilesLength","10");
        }

        while (k < max_files && e.hasMoreElements()) {

            JMenuItem subMenu = new JMenuItem( i+". "+Misc.getShortFileName((String)e.nextElement()));
            subMenu.addActionListener( new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemRecentFileActionPerformed(evt);
                }

            });
            menu.add(subMenu);
            i++;
            k++;
        }
    }

    public void updateRecentProjectMenu(JMenu menu, Vector v) {
    }
    public void updateUndoMenu(JReportFrame jrf) {

        if (jrf == null) {
            this.jMenuItemUndo.setEnabled(false);
            this.jMenuItemRedo.setEnabled(false);
            return;
        }
        if (jrf.getUndoOperation() != null) {
            this.jMenuItemUndo.setEnabled(true);
            this.jMenuItemUndo.setText(it.businesslogic.ireport.util.I18n.getString("undo","Undo")+ " " + jrf.getUndoOperation());
        }
        else {
            this.jMenuItemUndo.setEnabled(false);
            this.jMenuItemUndo.setText(it.businesslogic.ireport.util.I18n.getString("undo","Undo"));
        }

        if (jrf.getRedoOperation() != null) {
            this.jMenuItemRedo.setEnabled(true);
            this.jMenuItemRedo.setText(it.businesslogic.ireport.util.I18n.getString("redo","Redo")+ " " + jrf.getRedoOperation());
        }
        else {
            this.jMenuItemRedo.setEnabled(false);
            this.jMenuItemRedo.setText(it.businesslogic.ireport.util.I18n.getString("redo","Redo"));
        }
    }

    public void setCrosstabActive(CrosstabReportElement crosstabReportElement)
    {
        boolean bMode = this.getActiveReportFrame() != null && crosstabReportElement == null;
        JReportFrame jrf = this.getActiveReportFrame();
        this.jToggleButtonCrosstabTool.setEnabled(bMode);
        this.jToggleButtonSubreportTool.setEnabled(bMode);
        this.jToggleButtonChartTool.setEnabled(bMode);

        jToolbarFormatPanel.setCrosstabMode( bMode);

        jMenuItemInsertPageBreak.setEnabled(bMode);
        jMenuItemAlignToBandBottom.setEnabled(bMode);
        jMenuItemAlignToBandTop.setEnabled(bMode);
        jMenuItemBands.setEnabled(bMode);
        jMenuItemCenterBackground.setEnabled(bMode);
        //jMenuItemCenterH.setEnabled(bMode);
        //jMenuItemCenterInBand.setEnabled(bMode);
        //jMenuItemCenterV.setEnabled(bMode);
        jMenuItemLeftMargin.setEnabled(bMode);
        jMenuItemOrganize.setEnabled(bMode);
        jMenuItemRemoveMargins.setEnabled(bMode);
        jMenuItemRightMargin.setEnabled(bMode);
        jMenuItemRightSide.setEnabled(bMode);

        this.getElementPropertiesDialog().clearExpressionEditorContext();

        setActiveTool(0);

        crosstabMeasuresView.setCrosstabReportElement(crosstabReportElement);

        if (DockingManager.isDocked( (Dockable)crosstabMeasuresPanelView) && crosstabReportElement == null)
        {

                SwingUtilities.invokeLater( new Runnable() {
                    public void run()
                    {
                        DockingManager.close( crosstabMeasuresPanelView);
                        /*
                        if (getActiveReportFrame().isMaximum())
                        {
                            try {
                                getActiveReportFrame().setMaximum(false);
                            getActiveReportFrame().setMaximum(true);
                            } catch (PropertyVetoException ex) {
                                ex.printStackTrace();
                            }
                        }
                         */
                    }
                });

        }

        if (DockingManager.isDocked( (Dockable)crosstabStructureView) && crosstabReportElement == null)
        {
                SwingUtilities.invokeLater( new Runnable() {
                    public void run()
                    {
                        DockingManager.close( crosstabStructureView);
                        /*
                        if (getActiveReportFrame().isMaximum())
                        {
                        try {
                            getActiveReportFrame().setMaximum(false);
                            getActiveReportFrame().setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            ex.printStackTrace();
                        }
                        }
                         */
                    }
                });

        }

        // dockingContainerRight.removePanel(crosstabMeasuresView);
        if (crosstabReportElement != null)
        {
            if (!DockingManager.isDocked( (Dockable)crosstabMeasuresPanelView))
            {
                desktop.dock(crosstabMeasuresPanelView, DockingConstants.EAST_REGION, 0.2f);
            }
            //CR-rg.flexdock.docking.DockingManager.setSplitProportion(crosstabMeasuresPanelView.getComponent(), 0.8f);
            crosstabMeasuresPanelView.setActive(true);
            // dockingContainerRight.insertPanel(0,"Crosstab objects", crosstabMeasuresView, dockingContainerRight.INSERT_MODE_NEWPOSITION, false);
            // dockingContainerRight.setSelectedComponent( crosstabMeasuresView );
            Vector elements = new Vector();
            if (jrf != null && jrf.getSelectedCrosstabEditorPanel() != null)
            {
                elements = jrf.getSelectedCrosstabEditorPanel().getSelectedElements();
            }
            fireReportListenerReportElementsSelectionChanged(new ReportElementsSelectionEvent(jrf,crosstabReportElement, elements));
        }
        else if (jrf != null)
        {
                        fireReportListenerReportElementsSelectionChanged(new ReportElementsSelectionEvent(jrf,crosstabReportElement,jrf.getSelectedElements()));
        }


        // dockingContainerLeft.removePanel(crosstabStructurePanel);
        crosstabStructurePanel.saveSelection();

        if (crosstabReportElement != null)
        {
            crosstabStructurePanel.updateDocumentStructureTree(getActiveReportFrame(),crosstabReportElement);

            if (!DockingManager.isDocked( (Dockable)crosstabStructureView))
            {
                crosstabMeasuresPanelView.dock((Dockable)crosstabStructureView, DockingConstants.SOUTH_REGION, 0.5f);
                DockingManager.setSplitProportion((Dockable)crosstabMeasuresPanelView, 0.5f  );
            }

            //Misc.dockAt(crosstabStructureView, DockingConstants.EAST_REGION,0);

            /*
            //boolean b = org.flexdock.util.DockingUtility.dockRelative(crosstabStructureView, viewDocumentStructure, DockingConstants.SOUTH_REGION);

            if (!b)
            {
                b = DockingManager.dock((Dockable)crosstabStructureView, crosstabMeasuresPanelView.getDockingPort(),DockingConstants.SOUTH_REGION);
                System.out.println( "Docking on crosstabMeasuresPanelView CENTER " +  b);
            }

            //crosstabMeasuresPanelView.dock( crosstabStructureView, DockingConstants.SOUTH_REGION);
            if (b)
            {

                java.awt.Component comp = crosstabStructureView.getComponent();
                //System.out.println( "Take " +  comp);
		java.awt.Container parent = comp.getParent();
                //System.out.println( "Take parent1" +  parent);
		if( parent instanceof javax.swing.JTabbedPane ) {
			parent = parent.getParent();
                        //System.out.println( "Take parent2 (JTabbedPane) " +  parent);
		}

		if (!(parent  instanceof DockingPort))
                {
                    //System.out.println( "Not a Docking port");
                }
                else
                {
                    java.awt.Container grandParent = parent.getParent();
                    if(grandParent instanceof JSplitPane)
                    {
                            javax.swing.JSplitPane jsp = (javax.swing.JSplitPane)grandParent;
                            System.out.println( "JSplitPane found " + jsp.getOrientation() + " H=" + jsp.HORIZONTAL_SPLIT + " V=" + jsp.VERTICAL_SPLIT);
                            jsp.setDividerLocation(0.5f);
                            jsp.setResizeWeight(0.5f);
                    }
                }

            }
            */
            //crosstabStructureView.setActive(true);
            // dockingContainerLeft.insertPanel(0, "Crosstab structure", crosstabStructurePanel, false);
            // dockingContainerLeft.setSelectedComponent( crosstabStructurePanel );
        }

        desktop.doLayout();
        // dockingContainerLeft.updateUI();
    }

    public void updateCutAndPasteMenu(JReportFrame jrf) {

        if (jrf == null) {
            setCutCopyEnabled(false);
            setPasteEnebled(false);
            return;
        }

        Vector selection = getActiveElementSelection(jrf);
        if (selection != null && selection.size() > 0) {
            setCutCopyEnabled(true);
            if (getClipBoard().size()>0)
                setPasteEnebled(true);
        }
        else {
            setCutCopyEnabled(false);
            if (getClipBoard().size()>0)
                setPasteEnebled(true);
        }
    }



    public Vector getActiveElementSelection(JReportFrame jrf)
    {
        if (jrf == null) return new Vector();
        if (jrf.getSelectedCrosstabEditorPanel() == null)
        {
            return jrf.getSelectedElements();
        }
        else
        {
            return jrf.getSelectedCrosstabEditorPanel().getSelectedElements();
        }
    }


    public void undo() {
        jMenuItemUndoActionPerformed(new java.awt.event.ActionEvent(jMenuItemUndo,0,""));
    }

    public void redo() {
        jMenuItemRedoActionPerformed(new java.awt.event.ActionEvent(jMenuItemUndo,0,""));

    }

    /**
     *     Update the jTreeDocumen (the tree with the opened files)
     */
    public void updateOpenedDocumentsList() {

        try {
        jTreeFiles.updateUI();
        } catch (Exception ex1) {
            ex1.printStackTrace ();
        }
    }

    /** Getter for property activeClipboard.
     * @return Value of property activeClipboard.
     *
     */
    public int getActiveClipboard() {
        return activeClipboard;
    }

    /** Setter for property activeClipboard.
     * @param activeClipboard New value of property activeClipboard.
     *
     */
    public void setActiveClipboard(int activeClipboard) {
        this.activeClipboard = activeClipboard;
    }

    public Vector getClipBoard() {
        return clipboards[getActiveClipboard()];
    }

    public void setClipBoardContent(Vector elements) {
        clipboards[getActiveClipboard()] = elements;

    }

    /** Getter for property homeDirectory.
     * @return Value of property homeDirectory.
     *
     */
    public java.lang.String getHomeDirectory() {
        return homeDirectory;
    }

    /** Setter for property homeDirectory.
     * @param homeDirectory New value of property homeDirectory.
     *
     */
    public void setHomeDirectory(java.lang.String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getCurrentDirectory() {
        return "" + Misc.nvl( getProperties().getProperty("CurrentDirectory"), ".");
    }

    public void run() {

        //System.out.println("Avvio controllo " + (new java.util.Date()) );
        //System.out.flush();

        if (catchFormActivated)
        {
            catchFormActivated = false;
            this.checkForModifiedFiles();
            catchFormActivated = true;
        }

    }


    public void saveBackup(String filename) {
        java.io.File f = new java.io.File(filename);
        if ( f.exists()) {
            String path = f.getParent();
            String file = f.getName();

            if (getProperties().getProperty("DefaultBackupDirectory") != null &&
            (""+getProperties().getProperty("DefaultBackupDirectory")).length()>0)
                path = getProperties().getProperty("DefaultBackupDirectory");

            if (getProperties().getProperty("BackupPolicy") != null &&
            (""+getProperties().getProperty("BackupPolicy")).equalsIgnoreCase("1")) {
                return;
            } else if (getProperties().getProperty("BackupPolicy") != null &&
            (""+getProperties().getProperty("BackupPolicy")).equalsIgnoreCase("2")) {
                file = Misc.changeFileExtension(file, "bak");
            } else {
                file += ".bak";
            }
            try {
                //String back_path = getProperties().getProperty( "
                File f2 = new java.io.File( path, file); // + ((path.endsWith(java.io.File.separatorChar+"")) ? "" : ""+java.io.File.separatorChar) + file);
                if (f2.exists()) {
                    if (!f2.delete()) {
                        
                        getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.cannotDelete","Can not delete {0}", new Object[]{f2+""} ),
                                JOptionPane.ERROR_MESSAGE);
                        
                        
                    }
                }

                f.renameTo( f2 );
                
                
                 getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.fileRenamed","Renamed {0} to {1}", new Object[]{f+"", f2+""} ),
                                JOptionPane.INFORMATION_MESSAGE);

                //com.ms.wfc.io.File.copyOver( filename, com.ms.wfc.io.File.combine(path,file));
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getFormattedString("messages.errorSavingBackup",
                        "An exception is occurred saving backup copy.\n{0}", new Object[]{""+ex.getMessage()}),
                        "",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* Thanks to James Fu */
    private void setElementValue(Object element, String method, String value) {
       setElementValue(element, method, value, String.class);
    }
    /* Thanks to James Fu */
    public void setElementsValue(String method,String value ) {
        setElementsValue( method, value, String.class);
    }


    private void setElementValue(Object element, String method, Object value, Class clazz) {

            try {
                java.lang.reflect.Method setMethod = element.getClass().getMethod(method, new Class[]
                {clazz});
                setMethod.invoke(element, new Object[]
                {value});



            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }


    /* Thanks to James Fu */
    public void setElementsValue(String method, Object value, Class clazz ) {
        JReportFrame jrf = (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        if (jrf == null) return;
        // Set the new value for all selected elements...
        Enumeration e =  getActiveElementSelection(jrf).elements();
        boolean adjusted = false;
        while (e.hasMoreElements()) {
            ReportElement repele = (ReportElement)e.nextElement();
            if (repele instanceof TextReportElement)
            {
                TextReportElement element = (TextReportElement)repele;
                setElementValue(element, method, value, clazz);
                element.setFont(null);
                if (IReportFont.adjustPdfFontName(element))
                {
                    adjusted = true;
                }
            }
        }

        ReportElementChangedEvent changedEvent = new ReportElementChangedEvent(jrf ,(jrf.getSelectedCrosstabEditorPanel() != null)  ? jrf.getSelectedCrosstabEditorPanel().getCrosstabElement() : null , getActiveElementSelection(jrf) , ReportElementChangedEvent.CHANGED);
        //changedEvent.setEventSource( this );
        //changedEvent.setPropertyChanged( "fontSize" );
        //changedEvent.setNewValue(value);
        jrf.fireReportListenerReportElementsChanged(changedEvent);
        this.getElementPropertiesDialog().updateSelection();


        CrosstabEditorPanel pa = getActiveReportFrame().getSelectedCrosstabEditorPanel();
        if (pa == null)
            jrf.getJPanelReport().repaint();
        else
            pa.repaint();
    }

    //TODO         */
    ////////////////////////////////////////
    /*
     *
    The methods below which are being used for pref settings need to be moved
    to another location.  This is just a quick fix sort of thing.
       wade
     */
    ///////////////////////////////////////

    /**
     *Used to set the application preference/option/property UseMultiLineExpressions
     *@pram useMultiLineExpressions whether to use or not to use them.
     */
    public void setUsingMultiLineExpressions(boolean useMultiLineExpressions) {

        String boolVal = ""+false; //""+useMultiLineExpressions;

        this.properties.setProperty("usingMultiLineExpressions", boolVal);
    }//end setUseMultiLineExpressions

    /**
     *Used to tell whether or not to be using multi line expressions.
     *@return whether multiline expressions are being used or not.
     */
    public boolean isUsingMultiLineExpressions() {
        boolean ret = false;
                /*
                String prop = this.properties.getProperty("usingMultiLineExpressions");
                if(prop != null) {
                        ret = prop.trim().equalsIgnoreCase("true");
                }
                else {
                        ret = false;
                }
                 */
        return ret;
    }//end isUsingMultiLineExpressions

    /**
     *Method used to tell compiler to use the directory the file being compiled in
     *for the compile directory.  This is for simplicity of development.
     *@param useCurrentFilesDirectoryForCompiles speaks for itself
     */
    public void setUsingCurrentFilesDirectoryForCompiles(boolean usingCurrentFilesDirectoryForCompiles) {
        this.properties.setProperty("useCurrentFilesDirectoryForCompiles", ""+usingCurrentFilesDirectoryForCompiles);
    }

    /**
     *Used to tell if the compiler should use the current files directory for the compile.
     *@return whether or not to use the current files directory for the compile.
     */
    public boolean isUsingCurrentFilesDirectoryForCompiles() {
        boolean ret = false;
        ret = this.properties.getProperty("useCurrentFilesDirectoryForCompiles", "false").trim().equalsIgnoreCase("true");
        return ret;
    }

    /**
     *Used to set the default compilation directory
     *@param defaultCompilationDirectory
     */
    public void setDefaultCompilationDirectory(String defaultCompilationDirectory) {
        IREPORT_TMP_DIR = defaultCompilationDirectory;
    }

    /**
     *Method used to get the default compilation directory
     *@return the default compilation directory for ireport
     */
    public String getDefaultCompilationDirectory() {

        if (IREPORT_TMP_DIR != null) return IREPORT_TMP_DIR;
        return IREPORT_HOME_DIR;
                /*
                String ret = this.properties.getProperty("ireport.temp", IREPORT_TMP_DIR);
                if( ret.trim().length() > 0 ) {

                }
                else {
                        ret = IREPORT_TMP_DIR;
                }
                return ret;
                 */

    }

    /**
     *Method used to translate properties that dictate where the compilation directory
     *should be so that the correct directory can be obtained and used.
     *@return the compilation directory to use for the reports.
     */
    public String getTranslatedCompileDirectory() {
        String ret = null;
        if( this.isUsingCurrentFilesDirectoryForCompiles() ) {
            javax.swing.JInternalFrame f = this.jMDIDesktopPane.getSelectedFrame();
            if( f != null && f instanceof JReportFrame ) {
                JReportFrame rf = (JReportFrame)f;
                String fileName = rf.getReport().getFilename();
                if( fileName != null ) {
                    java.io.File rfile = new java.io.File(fileName);
                    ret = rfile.getParent();
                }
                else {
                    ret = this.getDefaultCompilationDirectory();
                }

            }
            else {
                ret = this.getDefaultCompilationDirectory();
            }
        }
        else {
            ret = this.getDefaultCompilationDirectory();
        }
        return ret;
    }

    /**
     *Used to hold the viewer setting.
     *@param reportViewer the viewer to use
     */
    public void setReportViewer(int reportViewer) {
        switch( reportViewer ) {
            case IREPORT_JAVA_VIEWER:
                this.jRadioButtonMenuItemPreviewJAVA.setSelected(true);
                break;
            case IREPORT_PDF_VIEWER:
                this.jRadioButtonMenuItemPreviewPDF.setSelected(true);
                break;
            case IREPORT_HTML_VIEWER:
                this.jRadioButtonMenuItemPreviewHTML.setSelected(true);
                break;
            case IREPORT_XLS_VIEWER:
                this.jRadioButtonMenuItemPreviewXLS.setSelected(true);
                break;
            case IREPORT_XLS2_VIEWER:
                this.jRadioButtonMenuItemPreviewXLS2.setSelected(true);
                break;
            case IREPORT_CSV_VIEWER:
                this.jRadioButtonMenuItemPreviewCSV.setSelected(true);
                break;
            case IREPORT_TXT_VIEWER:
                this.jRadioButtonMenuItemPreviewTXT.setSelected(true);
                break;
            case IREPORT_TXT_JR_VIEWER:
                this.jRadioButtonMenuItemPreviewTXTJR.setSelected(true);
                break;
            case IREPORT_RTF_VIEWER:
                this.jRadioButtonMenuItemPreviewRTF.setSelected(true);
                break;
            case IREPORT_ODF_VIEWER:
                this.jRadioButtonMenuItemPreviewODF.setSelected(true);
                break;

            case IREPORT_JASPER_VIEWER:
            default:
                reportViewer = IREPORT_JASPER_VIEWER;
                this.jRadioButtonMenuItemPreviewInternalViewer.setSelected(true);
                break;
        }//end switch report viewer
        this.properties.setProperty("DefaultReportViewer", ""+reportViewer);

    }//end setReportViewer

    /**
     *Used to get which viewer is in use
     *@returns the in representing the viewer in use.
     */
    public int getReportViewer() {
        int ret = IREPORT_JASPER_VIEWER;
        try {
            ret = Integer.parseInt(this.properties.getProperty("DefaultReportViewer", ""+IREPORT_JASPER_VIEWER));
        }
        catch(Throwable e) {
            //hmm, don't guess we care...we'll correct it internally.
        }
        switch( ret ) {
            case IREPORT_JAVA_VIEWER:
                break;
            case IREPORT_PDF_VIEWER:
                break;
            case IREPORT_HTML_VIEWER:
                break;
            case IREPORT_XLS_VIEWER:
                break;
            case IREPORT_XLS2_VIEWER:
                break;
            case IREPORT_CSV_VIEWER:
                break;
            case IREPORT_TXT_VIEWER:
                break;
            case IREPORT_TXT_JR_VIEWER:
                break;
            case IREPORT_RTF_VIEWER:
                break;
            case IREPORT_ODF_VIEWER:
                break;
            case IREPORT_JASPER_VIEWER:
            default:
                ret = IREPORT_JASPER_VIEWER;
                break;
        }//end switch report viewer
        return ret;
    }//end getReportViewer
    public void setEmbeddedIreport(boolean status) {
        this.embeddedIreport = status;
    }//end setEmbeddedIreport
    public boolean isEmbedded() {
        return this.embeddedIreport;
    }//end isEmbedded

    private static Locale lastLocale = null;
    public void applyI18n(){

        if (lastLocale != null && lastLocale == I18n.getCurrentLocale()) return;
                lastLocale = I18n.getCurrentLocale();

                try {


        // Start autogenerated code ----------------------
        jCheckBoxMenuItemDocumentStructure.setText(I18n.getString("mainFrame.checkBoxMenuItemDocumentStructure","Document structure"));
        jCheckBoxMenuItemElementProperties.setText(I18n.getString("mainFrame.checkBoxMenuItemElementProperties","Element properties"));
        jCheckBoxMenuItemExplorer.setText(I18n.getString("mainFrame.checkBoxMenuItemExplorer","Files"));
        jCheckBoxMenuItemLibrary.setText(I18n.getString("mainFrame.checkBoxMenuItemLibrary","Library"));
        jCheckBoxMenuItemStylesLibrary.setText(I18n.getString("mainFrame.checkBoxMenuItemStylesLibrary","Styles Library"));
        jCheckBoxMenuItemThreadList.setText(I18n.getString("mainFrame.checkBoxMenuItemThreadList","Threads list"));
        // End autogenerated code ----------------------
        // Start autogenerated code ----------------------
        jMenuItemInsertPageBreak.setText(I18n.getString("mainFrame.menuItemInsertPageBreak","Insert page/column break"));
        jMenuItemChart.setText(I18n.getString("mainFrame.menuItemChart","Chart"));
        jMenuItemForum.setText(I18n.getString("mainFrame.menuItemForum","Forum..."));
        
        String homePage = I18n.getString("mainFrame.menuItemHomePage","iReport home page");
        homePage = Misc.string_replace( getBrandingProperties().getProperty("ireport.name"), "iReport", homePage);
        
        
        jMenuItemHomePage.setText(homePage);
        jMenuItemLeftMargin.setText(I18n.getString("mainFrame.menuItemLeftMargin","Join left page margin"));

        jMenuItemQueryExecuters.setText(I18n.getString("mainFrame.menuItemQueryExecuters","Query executers"));
        jMenuItemReloadFonts.setText(I18n.getString("mainFrame.menuItemReloadFonts","Reload fonts"));
        jMenuItemRightMargin.setText(I18n.getString("mainFrame.menuItemRightMargin","Join right page margin"));
        jMenuItemSubDataset.setText(I18n.getString("mainFrame.menuItemSubDataset","Subdatasets"));
        jMenuItemSubDataset1.setText(I18n.getString("mainFrame.menuItemSubDataset1","Subdatasets"));
        jMenuPanels.setText(I18n.getString("mainFrame.menuPanels","Docking panes"));

        jMenuItemCreateFromTemplate.setText(I18n.getString("mainFrame.menuItemCreateFromTemplate","Create from template"));
        // End autogenerated code ----------------------

      //added by Felix Firgau on Feb 06th 2006
      jButtonNew.setToolTipText(it.businesslogic.ireport.util.I18n.getString("newDocument", "New document"));
      jButtonOpen.setToolTipText(it.businesslogic.ireport.util.I18n.getString("open", "Open"));
      jButtonSave.setToolTipText(it.businesslogic.ireport.util.I18n.getString("save", "Save"));
      jButtonCut.setToolTipText(it.businesslogic.ireport.util.I18n.getString("cut", "Cut"));
      jButtonCopy.setToolTipText(it.businesslogic.ireport.util.I18n.getString("copy", "Copy"));
      jButtonPaste.setToolTipText(it.businesslogic.ireport.util.I18n.getString("paste", "Paste"));
      jToggleButtonLineTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("line", "Line"));
      jToggleButtonRectTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("rectangle", "Rectangle"));
      //jToggleButtonRectRoundTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("roundedRectangle", "Rounded rectangle"));
      jToggleButtonEllipseTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("ellipse", "Ellipse"));
      jToggleButtonImageTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("image", "Image"));
      jToggleButtonStaticTextTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("staticText", "Static text"));
      jToggleButtonTextFieldTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("textField", "Textfield"));
      jToggleButtonFrameTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("frame", "Frame"));
      jToggleButtonSubreportTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("subreport", "Subreport"));
      jButtonBands.setToolTipText(it.businesslogic.ireport.util.I18n.getString("bands", "Bands"));
      jButtonGroups.setToolTipText(it.businesslogic.ireport.util.I18n.getString("reportGroups", "Report groups"));
      jButtonParameters.setToolTipText(it.businesslogic.ireport.util.I18n.getString("parameters", "Report fields, variables and parameters"));
      jButtonDatabase.setToolTipText(it.businesslogic.ireport.util.I18n.getString("database", "Database"));
      jButtonCompiler.setToolTipText(it.businesslogic.ireport.util.I18n.getString("compile", "Compile"));
      jButtonRun1.setToolTipText(it.businesslogic.ireport.util.I18n.getString("executeReport", "Execute report"));
      jButtonRun2.setToolTipText(it.businesslogic.ireport.util.I18n.getString("executeReportUsingActiveConnection", "Execute report using active connection"));
      jToggleButtonChartTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("chartTool", "Chart tool"));
      jToggleButtonBarcodeTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("barcodeTool", "Barcode tool"));
      jToggleButtonCrosstabTool.setToolTipText(it.businesslogic.ireport.util.I18n.getString("crossTab", "Crosstab"));
      jButtonLens1.setToolTipText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
      jButtonLens2.setToolTipText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
      jComboBoxFont.setToolTipText(it.businesslogic.ireport.util.I18n.getString("font", "Font"));
      jNumberComboBoxSize.setToolTipText(it.businesslogic.ireport.util.I18n.getString("fontSize", "Fontsize"));
      jButtonIncreaseSize.setToolTipText(it.businesslogic.ireport.util.I18n.getString("fontSizeInc", "Increase fonzsize"));
      jButtonDecreaseSize.setToolTipText(it.businesslogic.ireport.util.I18n.getString("fontSizeDec", "Decrease fonzsize"));
      jButtonBold.setToolTipText(it.businesslogic.ireport.util.I18n.getString("bold", "Bold"));
      jButtonItalic.setToolTipText(it.businesslogic.ireport.util.I18n.getString("italic", "Italic"));
      jButtonUnderline.setToolTipText(it.businesslogic.ireport.util.I18n.getString("underlined", "Underlined"));
      jButtonStrikethrought.setToolTipText(it.businesslogic.ireport.util.I18n.getString("striked", "Striked"));
      jButtonAlignLeft.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignLeft", "Align left"));
      jButtonAlignRight.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignRight", "Align right"));
      jButtonAlignJustify.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignJustify", "Align justify"));
      jButtonAlignCenter.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignCenter", "Align center"));
      jButtonAlignTop.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignTop", "Align top"));
      jButtonAlignMiddle.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignVerticalAxis", "Align on vertical axis"));
      jButtonAlignBottom.setToolTipText(it.businesslogic.ireport.util.I18n.getString("alignBottom", "Align bottom"));
      jMenuItemPrint.setText(it.businesslogic.ireport.util.I18n.getString("printDesign", "Print design"));
      jMenuToolBars.setText(it.businesslogic.ireport.util.I18n.getString("toolBars", "Toolbars"));
      //Modification end of Felix Firgau on Feb 06th 2006

      //Added by Felix Firgau on Feb 07th 2006
      jCheckBoxMenuItemElementsFormatting.setText(it.businesslogic.ireport.util.I18n.getString("elementsFormating", "Elements formating"));
      jCheckBoxMenuItemTextFormatting.setText(it.businesslogic.ireport.util.I18n.getString("textFormating", "Text formating"));
      //Modification end of Felix Firgau on Feb 07th 2006

      //Added by Felix Firgau on Feb 8th 2006
      jMenuItemImage.setText(it.businesslogic.ireport.util.I18n.getString("image", "Image"));
      jMenuItemExportOptions.setText(it.businesslogic.ireport.util.I18n.getString("exportOptions", "Export options"));
      //End

      //Added by Felix Firgau on Feb 9th 2006
      jMenuItemOrganize.setText(it.businesslogic.ireport.util.I18n.getString("organizeAsTable", "Organize as table"));
      jMenuItemKill.setText(it.businesslogic.ireport.util.I18n.getString("killThread", "Kill thread"));
      //End

      //Added by Felix Firgau on Feb 14th 2006
      jMenuItemRightSide.setText(it.businesslogic.ireport.util.I18n.getString("showHideRightSide", "Show/hide right side"));
      jMenuItemStyles.setText(it.businesslogic.ireport.util.I18n.getString("styles", "Styles"));
      //End

        jMenuFile.setText(it.businesslogic.ireport.util.I18n.getString("file","File"));
        jMenuItemNewDocument.setText(it.businesslogic.ireport.util.I18n.getString("newDocument","New document"));
        jMenuItemWizard.setText(it.businesslogic.ireport.util.I18n.getString("reportWizard","Report Wizard"));
        jMenuItemOpen.setText(it.businesslogic.ireport.util.I18n.getString("open","Open"));
        //jMenuItemQOpen.setText(it.businesslogic.ireport.util.I18n.getString("quickOpen","Quick open"));
        jMenuItemClose.setText(it.businesslogic.ireport.util.I18n.getString("close","Close"));
        jMenuItemCloseAll.setText(it.businesslogic.ireport.util.I18n.getString("closeAll","Close all"));
        jMenuItemCloseAllExceptThis.setText(it.businesslogic.ireport.util.I18n.getString("closeAllExceptThis","Close all except this"));
        jMenuItemRevertToSaved.setText(it.businesslogic.ireport.util.I18n.getString("revertToSaved","Revert to saved"));
        jMenuItemCloseFromList.setText(it.businesslogic.ireport.util.I18n.getString("close","Close"));
        jMenuItemCloseAllFromList.setText(it.businesslogic.ireport.util.I18n.getString("closeAll","Close all"));
        jMenuItemCloseAllExceptThisFromList.setText(it.businesslogic.ireport.util.I18n.getString("closeAllExceptThis","Close all except this"));
        jMenuItemRevertToSavedFromList.setText(it.businesslogic.ireport.util.I18n.getString("revertToSaved","Revert to saved"));
        jMenuItemSaveFromList.setText(it.businesslogic.ireport.util.I18n.getString("save","Save"));
        jMenuItemSaveAsFromList.setText(it.businesslogic.ireport.util.I18n.getString("saveAs","Save as..."));
        jMenuItemSaveAllFromList.setText(it.businesslogic.ireport.util.I18n.getString("saveAll","Save all"));
        jMenuItemSave.setText(it.businesslogic.ireport.util.I18n.getString("save","Save"));
        jMenuItemSaveAs.setText(it.businesslogic.ireport.util.I18n.getString("saveAs","Save as..."));
        jMenuItemSaveAll.setText(it.businesslogic.ireport.util.I18n.getString("saveAll","Save all"));
        jMenuRecentFiles.setText(it.businesslogic.ireport.util.I18n.getString("recentFiles","Recent files"));
        //jMenuRecentProjects.setText(it.businesslogic.ireport.util.I18n.getString("recentProjects","Recent projects"));
        //jMenuItemFavorites.setText(it.businesslogic.ireport.util.I18n.getString("favoriteFiles","Favorites files"));
        jMenuItemQuit.setText(it.businesslogic.ireport.util.I18n.getString("quit","Quit"));
        jMenuEdit.setText(it.businesslogic.ireport.util.I18n.getString("edit","Edit"));
        jMenuItemUndo.setText(it.businesslogic.ireport.util.I18n.getString("undo","Undo"));
        jMenuItemRedo.setText(it.businesslogic.ireport.util.I18n.getString("redo","Redo"));
        jMenuItemCut.setText(it.businesslogic.ireport.util.I18n.getString("cut","Cut"));
        jMenuItemCopy.setText(it.businesslogic.ireport.util.I18n.getString("copy","Copy"));
        jMenuItemPaste.setText(it.businesslogic.ireport.util.I18n.getString("paste","Paste"));
        jMenuItemDelete.setText(it.businesslogic.ireport.util.I18n.getString("delete","Delete"));
        jCheckBoxMenuItemEMM.setText(it.businesslogic.ireport.util.I18n.getString("disableElementsMouseMove","Disable elements mouse move"));
        jCheckBoxMenuItemReportVirtualizer.setText(it.businesslogic.ireport.util.I18n.getString("useReportVirtualizer","Use Report Virtualizer"));
        jCheckBoxMenuItemIgnorePagination.setText(it.businesslogic.ireport.util.I18n.getString("ignorePagination","Ignore pagination"));
        jCheckBoxMenuItemSnapToGrid.setText(it.businesslogic.ireport.util.I18n.getString("snapToGrid","Snap to grid"));
        jMenuAdd.setText(it.businesslogic.ireport.util.I18n.getString("insertElement","Insert element"));
        jMenuItemLine.setText(it.businesslogic.ireport.util.I18n.getString("line","Line"));
        jMenuItemRectangle.setText(it.businesslogic.ireport.util.I18n.getString("rectangle","Rectangle"));
        jMenuItemRoundRectangle.setText(it.businesslogic.ireport.util.I18n.getString("roundedRectangle","Rounded rectangle"));
        jMenuItemEllipse.setText(it.businesslogic.ireport.util.I18n.getString("ellipse","Ellipse"));
        jMenuItemStaticText.setText(it.businesslogic.ireport.util.I18n.getString("staticText","Static text"));
        jMenuItemTextField.setText(it.businesslogic.ireport.util.I18n.getString("textField","Textfield"));
        jMenuItemSubreport.setText(it.businesslogic.ireport.util.I18n.getString("subreport","Subreport"));
        jMenuItemXMLSource.setText(it.businesslogic.ireport.util.I18n.getString("editXmlSource","Edit XML source"));
        jMenuView.setText(it.businesslogic.ireport.util.I18n.getString("view","View"));
        jCheckBoxMenuItemGrid.setText(it.businesslogic.ireport.util.I18n.getString("showGrid","Show grid"));
        jCheckBoxMenuItemOutput.setText(it.businesslogic.ireport.util.I18n.getString("showOutputWindow","Show output window"));
        //jCheckBoxMenuItemExplorer.setText(it.businesslogic.ireport.util.I18n.getString("showProjectBrowser","Show project browser"));
        jMenuItemReportProperties.setText(it.businesslogic.ireport.util.I18n.getString("reportProperties","Report properties"));
        jMenuItemProperties.setText(it.businesslogic.ireport.util.I18n.getString("customProperties","Custom properties"));
        jMenuItemReportQuery.setText(it.businesslogic.ireport.util.I18n.getString("reportQuery","Report query"));
        jMenuItemFields.setText(it.businesslogic.ireport.util.I18n.getString("reportFields","Report fields"));
        jMenuItemVariables.setText(it.businesslogic.ireport.util.I18n.getString("reportVariables","Report variables"));
        jMenuItemParameters.setText(it.businesslogic.ireport.util.I18n.getString("reportParameters","Report parameters"));
        jMenuItemFonts.setText(it.businesslogic.ireport.util.I18n.getString("reportFonts","Report fonts"));
        jMenuItemBands.setText(it.businesslogic.ireport.util.I18n.getString("bands","Bands"));
        jMenuItemGroups.setText(it.businesslogic.ireport.util.I18n.getString("reportGroups","Report groups"));
        jMenuItemElementProperties.setText(it.businesslogic.ireport.util.I18n.getString("elementProperties","Element properties"));
        //jMenuItemElementsBrowser.setText(it.businesslogic.ireport.util.I18n.getString("elementsBrowser","Elements browser"));
        jMenuItemScriptletEditor.setText(it.businesslogic.ireport.util.I18n.getString("scriptletEditor","Scriptlet editor"));
        //jMenuItemReportSeries.setText(it.businesslogic.ireport.util.I18n.getString("reportSeries","Report series"));
        jMenuItemReportImports.setText(it.businesslogic.ireport.util.I18n.getString("reportImports","Report imports"));
        //jMenuSearch.setText(it.businesslogic.ireport.util.I18n.getString("search","Search"));
        //jMenuItemFindNext.setText(it.businesslogic.ireport.util.I18n.getString("findNext","Find next"));
        //jMenuItemFindPrev.setText(it.businesslogic.ireport.util.I18n.getString("findPrevious","Find Prev"));
        //jMenuItemReplace.setText(it.businesslogic.ireport.util.I18n.getString("replace","Replace"));
        //jMenuProjects.setText(it.businesslogic.ireport.util.I18n.getString("project","Project"));
        //jMenuItemNewProject.setText(it.businesslogic.ireport.util.I18n.getString("newProject","New project"));
        //jMenuItemOpenProject.setText(it.businesslogic.ireport.util.I18n.getString("openProject","Open project"));
        //jMenuItemCloseProject.setText(it.businesslogic.ireport.util.I18n.getString("closeProject","Close project"));
        //jMenuItemAddFileToProject.setText(it.businesslogic.ireport.util.I18n.getString("addReportToProject","Add report to project..."));
        //jMenuItemProjectOptions.setText(it.businesslogic.ireport.util.I18n.getString("projectOptions","Project options..."));
        jMenuBuild.setText(it.businesslogic.ireport.util.I18n.getString("build","Build"));
        jMenuItemCompile.setText(it.businesslogic.ireport.util.I18n.getString("compile","Compile"));
        //jMenuItemCompileAll.setText(it.businesslogic.ireport.util.I18n.getString("compileAll","Compile All"));
        jMenuItemExecute.setText(it.businesslogic.ireport.util.I18n.getString("executeReport","Execute report"));
        jMenuItemExecuteDB.setText(it.businesslogic.ireport.util.I18n.getString("executeReportUsingActiveConnection","Execute report (using active conn.) "));
        jRadioButtonMenuItemPreviewPDF.setText(it.businesslogic.ireport.util.I18n.getString("pdfPreview","PDF preview"));
        jRadioButtonMenuItemPreviewHTML.setText(it.businesslogic.ireport.util.I18n.getString("htmlPreview","HTML preview"));
        jRadioButtonMenuItemPreviewJAVA.setText(it.businesslogic.ireport.util.I18n.getString("java2DPreview","JAVA 2D preview"));
        jRadioButtonMenuItemPreviewXLS.setText(it.businesslogic.ireport.util.I18n.getString("excelPreview","Excel preview"));
        jRadioButtonMenuItemPreviewXLS2.setText(it.businesslogic.ireport.util.I18n.getString("excelPreview2","Excel preview (using JExcelApi)"));
        jRadioButtonMenuItemPreviewCSV.setText(it.businesslogic.ireport.util.I18n.getString("csvPreview","CSV preview"));
        jRadioButtonMenuItemPreviewTXTJR.setText(it.businesslogic.ireport.util.I18n.getString("txtPreview","Text preview (JasperReports)"));
        jRadioButtonMenuItemPreviewRTF.setText(it.businesslogic.ireport.util.I18n.getString("rtfPreview","RTF preview"));
        jRadioButtonMenuItemPreviewODF.setText(it.businesslogic.ireport.util.I18n.getString("odfPreview","OpenOffice (ODF) preview"));
        jRadioButtonMenuItemPreviewTXT.setText(it.businesslogic.ireport.util.I18n.getString("textPreview","Text preview (Experimental)"));
        jRadioButtonMenuItemPreviewInternalViewer.setText(it.businesslogic.ireport.util.I18n.getString("jrViewerPreview","JRViewer preview"));
        jMenuItemActiveConnection.setText(it.businesslogic.ireport.util.I18n.getString("setActiveConnection","Set active connection"));
        jMenuItemActiveConnection1.setText(it.businesslogic.ireport.util.I18n.getString("setActiveConnection","Set active connection"));
        updateJMenuItemReportLocaleText();
        updateJMenuItemReportMaxRecords();
        updateJMenuItemReportTimeZone();
        jMenuItemActiveConnection.setToolTipText(it.businesslogic.ireport.util.I18n.getString("setTheConnection","Set the connection that must be used to fill this report"));
        jMenuFormat.setText(it.businesslogic.ireport.util.I18n.getString("format","Format"));
        jMenuAlign.setText(it.businesslogic.ireport.util.I18n.getString("align", "Align..."));
        jMenuItemAlignLeft.setText(it.businesslogic.ireport.util.I18n.getString("alignLeft", "Align left"));
        jMenuItemAlignRight.setText(it.businesslogic.ireport.util.I18n.getString("alignRight", "Align right"));
        jMenuItemAlignTop.setText(it.businesslogic.ireport.util.I18n.getString("alignTop", "Align top"));
        jMenuItemAlignBottom.setText(it.businesslogic.ireport.util.I18n.getString("alignBottom", "Align bottom"));
        jMenuItemAlignVerticalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignVerticalAxis", "Align vertical axis"));
        jMenuItemAlignHorizontalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignHorizontalAxis", "Align horizontal axis"));
        jMenuItemAlignToBandTop.setText(it.businesslogic.ireport.util.I18n.getString("alignToBandTop", "Align to band top"));
        jMenuItemAlignToBandBottom.setText(it.businesslogic.ireport.util.I18n.getString("alignToBandBottom", "Align to band bottom"));
        jMenuSize.setText(it.businesslogic.ireport.util.I18n.getString("size", "Size..."));
        jMenuItemSameWidth.setText(it.businesslogic.ireport.util.I18n.getString("sameWidth", "Same width"));
        jMenuItemSameWidthMax.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMax", "Same width (max)"));
        jMenuItemSameWidthMin.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMin", "Same width (min)"));
        jMenuItemSameHeight.setText(it.businesslogic.ireport.util.I18n.getString("sameHeight", "Same height"));
        jMenuItemSameHeightMin.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMin", "Same height (min)"));
        jMenuItemSameHeightMax.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMax", "Same height (max)"));
        jMenuItemSameSize.setText(it.businesslogic.ireport.util.I18n.getString("sameSize", "Same size"));
        jMenuPosition.setText(it.businesslogic.ireport.util.I18n.getString("position", "Position..."));
        jMenuItemCenterH.setText(it.businesslogic.ireport.util.I18n.getString("centerHorizontallyBandBased", "Center horizontally (band/cell based)"));
        jMenuItemCenterV.setText(it.businesslogic.ireport.util.I18n.getString("centerVerticallyBandBased", "Center vertically (band/cell based)"));
        jMenuItemCenterInBand.setText(it.businesslogic.ireport.util.I18n.getString("centerInBand", "Center in band/cell"));
        jMenuItemCenterBackground.setText(it.businesslogic.ireport.util.I18n.getString("centerInBackground", "Center in background"));
        jMenuItemJoinLeft.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesLeft", "Join sides left"));
        jMenuItemJoinRight.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesRight", "Join sides right"));
        jMenuHSpacing.setText(it.businesslogic.ireport.util.I18n.getString("horizontalSpacing", "Horizontal spacing..."));
        jMenuItemHSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemHSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemHSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemHSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuVSpacing.setText(it.businesslogic.ireport.util.I18n.getString("verticalSpacing", "Vertical spacing"));
        jMenuItemVSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemVSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemVSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemVSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuItemBringToFront.setText(it.businesslogic.ireport.util.I18n.getString("bringToFront", "Bring to front"));
        jMenuItemSendToBack.setText(it.businesslogic.ireport.util.I18n.getString("sendToBack", "Send to back"));
        jMenuDatabase.setText(it.businesslogic.ireport.util.I18n.getString("datasourceMenu", "Data"));
        jMenuItemConnections.setText(it.businesslogic.ireport.util.I18n.getString("connectionsDatasources", "Connections / Datasources"));
        jMenuItemReportQuery2.setText(it.businesslogic.ireport.util.I18n.getString("reportQuery", "Report query"));
        jMenuTools.setText(it.businesslogic.ireport.util.I18n.getString("optionsMenu", "Options"));
        jMenuItemOptions.setText(it.businesslogic.ireport.util.I18n.getString("options", "Settings..."));
        jMenuItemCompatibility.setText(it.businesslogic.ireport.util.I18n.getString("compatibility", "Compatibility..."));
        jMenuWindow.setText(it.businesslogic.ireport.util.I18n.getString("window", "Window"));
        jMenuItemCascade.setText(it.businesslogic.ireport.util.I18n.getString("cascade", "Cascade"));
        jMenuItemTileHorizontal.setText(it.businesslogic.ireport.util.I18n.getString("tileHorizontal", "Tile horizontal"));
        jMenuItemTileVertical.setText(it.businesslogic.ireport.util.I18n.getString("tileVertical", "Tile vertical"));
        jMenuItemtileAnodine.setText(it.businesslogic.ireport.util.I18n.getString("tileAnodine", "Tile anodine"));
        jMenuItemNextWin.setText(it.businesslogic.ireport.util.I18n.getString("nextWindow", "Next Window"));
        jMenuItemPrevWin.setText(it.businesslogic.ireport.util.I18n.getString("previousWindow", "Previous Window"));
        jMenuHelp.setText(it.businesslogic.ireport.util.I18n.getString("help", "Help"));
        jMenuItemHelp.setText(it.businesslogic.ireport.util.I18n.getString("help", "Help"));
        
        
        String aboutMenuItemText = I18n.getString("aboutiReport", "About iReport...");
        aboutMenuItemText = Misc.string_replace( getBrandingProperties().getProperty("ireport.name"), "iReport", aboutMenuItemText);
        
        jMenuItemAbout.setText(aboutMenuItemText);
        
        //jMenuItemFind.setText(it.businesslogic.ireport.util.I18n.getString("find", "Find..."));
        jMenuItemPluginConfig.setText(it.businesslogic.ireport.util.I18n.getString("configurePlugins", "Configure plugins"));
        jMenuPlugins.setText(it.businesslogic.ireport.util.I18n.getString("plugins", "Plugins"));
        jMenuLocalGIS.setText(it.businesslogic.ireport.util.I18n.getString("localgis", "LocalGIS"));
        jMenuInternationalization.setText(it.businesslogic.ireport.util.I18n.getString("internationalization", "Internationalization"));
        //jMenuInternationalizationWizard.setText(it.businesslogic.ireport.util.I18n.getString("internationalizationWizard", "Internationalization Wizard"));
        jMenuLocaleFiles.setText(it.businesslogic.ireport.util.I18n.getString("internationalizationFiles", "Locale files"));
        jMenuItemRemoveMargins.setText(it.businesslogic.ireport.util.I18n.getString("removeMargins", "Remove margins"));
        jMenuItemClasspath.setText(it.businesslogic.ireport.util.I18n.getString("classPath", "Classpath"));
        jMenuItemAddGroup.setText(it.businesslogic.ireport.util.I18n.getString("newReportGroupWizard","New report group wizard"));
        jMenuItemFontspath.setText(it.businesslogic.ireport.util.I18n.getString("pathToFonts", "Fonts path"));
        jMenuItemExpressions.setText(it.businesslogic.ireport.util.I18n.getString("Formulas", "Formulas"));
        jMenuItemPluginConfig.setText(it.businesslogic.ireport.util.I18n.getString("configurePlugins", "Configure plugins"));




        viewFiles.setTitle(it.businesslogic.ireport.util.I18n.getString("files", "Files"), true);
        viewDocumentStructure.setTitle(it.businesslogic.ireport.util.I18n.getString("documentStructure", "Document structure"), true);
        viewPropertySheet.setTitle(it.businesslogic.ireport.util.I18n.getString("properties","Properties"), true);
        viewThreads.setTitle(it.businesslogic.ireport.util.I18n.getString("threads","Threads"), true);
        viewLibrary.setTitle(it.businesslogic.ireport.util.I18n.getString("library","Library"), true);
        logPaneView.setTitle(it.businesslogic.ireport.util.I18n.getString("output","Output console"), true);
        stylesPanleView.setTitle(it.businesslogic.ireport.util.I18n.getString("stylesLibrary","Styles Library"), true);
        crosstabMeasuresPanelView.setTitle(it.businesslogic.ireport.util.I18n.getString("crosstabObjects","Crosstab objects"), true);
        crosstabStructureView.setTitle(it.businesslogic.ireport.util.I18n.getString("crosstabStructure","Crosstab structure"), true);

        viewFiles.updateUI();
        viewDocumentStructure.updateUI();
        viewPropertySheet.updateUI();
        viewThreads.updateUI();
        viewLibrary.updateUI();
        logPaneView.updateUI();
        stylesPanleView.updateUI();
        crosstabMeasuresPanelView.updateUI();
        crosstabStructureView.updateUI();

        ((DefaultMutableTreeNode)this.jTreeFiles.getModel().getRoot()).setUserObject( it.businesslogic.ireport.util.I18n.getString("openedFiles", "Opened files"));

        //if (libraryPanel != null && jTabbedPaneExplorer.indexOfComponent(libraryPanel) >= 0)
        //        jTabbedPaneExplorer.setTitleAt(jTabbedPaneExplorer.indexOfComponent(libraryPanel), it.businesslogic.ireport.util.I18n.getString("gui.library", "Library"));

         } catch (Exception ex)
         {

         }
    }



    /** Getter for property eventsForm.
     * @return Value of property eventsForm.
     *
     */
    public it.businesslogic.ireport.gui.EventsForm getEventsForm() {
        return eventsForm;
    }

    /** Setter for property eventsForm.
     * @param eventsForm New value of property eventsForm.
     *
     */
    public void setEventsForm(it.businesslogic.ireport.gui.EventsForm eventsForm) {
        this.eventsForm = eventsForm;
    }



    public void loadPlugins(String plugins_dir) {
        // Adding default properties...

        ReportClassLoader rcl = this.getReportClassLoader();
        //rcl.rescanLibDirectory();

        java.util.Vector plugin_files_v = new java.util.Vector();
        File plugDir = null;
        try {
            plugDir = new File(plugins_dir);
        } catch (Exception ex)
        {}
        if (plugDir == null || !plugDir.exists() || plugDir.isFile()) {
            try {
                
                getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getFormattedString("messages.errorScanningPluginDir","Error scanning iReport plugin directory: {0}\n", new Object[]{""+plugins_dir} ),
                                JOptionPane.ERROR_MESSAGE);
                

            } catch (Exception exsx)
            {}

        } else
        {

        // Loading all xml files....

	        File plugins_files[] = plugDir.listFiles();

	        for (int i=0; i<plugins_files.length; ++i)
	        {
	            if (!plugins_files[i].getName().endsWith(".xml")) continue;
	            plugin_files_v.add(plugins_files[i]);
	        }
	}

        try {

            //Enumeration enum_pl = this.getClass().getClassLoader().getResources("ireport/plugin.xml");
            //logOnConsole("Looking into: " + "\n");
            //for (int i=0; i< rcl.getCachedItems().size(); ++i)
            //{
            //    logOnConsole(rcl.getCachedItems().get(i)+ "\n");
            //}

            Enumeration enum_pl = rcl.getResources("ireport/plugin.xml");


        while (enum_pl.hasMoreElements())
        {
            Object oobj = enum_pl.nextElement();
            if (!plugin_files_v.contains(oobj))
            {
                plugin_files_v.add(oobj);
            }
        }


        } catch (Exception ex) {
            
            getLogPane().getMainLogTextArea().logOnConsole(
                                I18n.getString("messages.errorLookingForPlugins","Error searching ireport/plugin.xml resources\n"),
                                JOptionPane.ERROR_MESSAGE);
            
        }

        for (int i=0; i<plugin_files_v.size(); ++i) {

            Object source = plugin_files_v.elementAt(i);
            //  Create a Xerces DOM Parser
            DOMParser parser = new DOMParser();
            //  Parse the Document
            //  and traverse the DOM
            try {

                parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                    /* Code by Teodor Danciu */
                    public org.xml.sax.InputSource resolveEntity(
                    String publicId,
                    String systemId
                    ) throws SAXException//, java.io.IOException
                    {
                        org.xml.sax.InputSource inputSource = null;

                        if (systemId != null) {
                            String dtd = null;

                            if ( systemId.equals("http://ireport.sourceforge.net/dtds/iReportPlugin.dtd") ) {
                                dtd = "it/businesslogic/ireport/dtds/iReportPlugin.dtd";
                            } else {
                                return new org.xml.sax.InputSource(systemId);
                            }


                            ClassLoader classLoader = this.getClass().getClassLoader();

                            java.net.URL url = null;


                            if (classLoader != null) {
                                url = classLoader.getResource(dtd);
                            }
                            if (url == null) {
                                classLoader = this.getClass().getClassLoader();
                            }

                            java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                            if (is != null) {
                                java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                                inputSource = new org.xml.sax.InputSource(isr);
                            }

                        }

                        return inputSource;
                    }
                });
                /* End Code by Teodor Danciu */
                InputStream input_source = null;
                if ( source instanceof java.io.File )
                {
                    input_source = new FileInputStream((java.io.File)source);

                } else if ( source instanceof java.net.URL){

                    input_source = ((java.net.URL)source).openStream();

                }

                parser.parse(new org.xml.sax.InputSource( input_source ));
                Document document = parser.getDocument();

                //System.out.println("traverse");
                Node node = document.getDocumentElement();

                PluginEntry pe = new PluginEntry();
                pe.setMainFrame( this );

                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("iReportPlugin")) {
                    //System.out.println("iReportProperties");
                    // Get childs....
                    NamedNodeMap nnm_plugin = node.getAttributes();
                    if ( nnm_plugin.getNamedItem("name") != null) pe.setName(nnm_plugin.getNamedItem("name").getNodeValue());
                    if ( nnm_plugin.getNamedItem("class") != null) pe.setClassName( nnm_plugin.getNamedItem("class").getNodeValue() );
                    if ( nnm_plugin.getNamedItem("loadOnStartup") != null) pe.setLoadOnStartup( Misc.nvl(nnm_plugin.getNamedItem("loadOnStartup").getNodeValue().trim(),"false").toLowerCase().equals("true") );
                    if ( nnm_plugin.getNamedItem("hide") != null) pe.setHide(  Misc.nvl(nnm_plugin.getNamedItem("hide").getNodeValue().trim(),"false").toLowerCase().equals("true") );
                    if ( nnm_plugin.getNamedItem("configurable") != null) pe.setConfigurable( Misc.nvl(nnm_plugin.getNamedItem("configurable").getNodeValue().trim(),"false").toLowerCase().equals("true") );

                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        for (int k=0; k< children.getLength(); k++) {
                            Node nodeChild = (Node)children.item(k);
                            if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("IconFile")) {

                                // Take the CDATA...
                                // Load icon...
                                String iconName = readPCDATA(nodeChild).trim();
                                try {
                                    pe.setIcon( new javax.swing.ImageIcon(getReportClassLoader().getResource(iconName) ));
                                } catch (Exception ex) { ex.printStackTrace(); }
                            }
                            else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("Description")) {
                                String description = readPCDATA(nodeChild).trim();
                                pe.setDescription( description );
                            }
                        }
                    }
                }


                if (!pe.isHide()) {
                    javax.swing.JMenuItem pluginItem = new javax.swing.JMenuItem();
                    pluginItem.setText( pe.getName() );
                    if (pe.getIcon() != null) {
                        pluginItem.setIcon( pe.getIcon() );
                    }

                    pluginEntries.put( pluginItem, pe );

                    pluginItem.addActionListener( new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jMenuItemPluginActionPerformed(evt);
                        }

                    });

                    jMenuPlugins.add( pluginItem );
                }

                if (pe.isLoadOnStartup()) {

                    pe.getPlugin().call();
                }

            } catch (SAXException e) {
                System.err.println(e);
            } catch (java.io.IOException e) {
                System.err.println(e);
            }

        } // End cycle on iReport plugin files...
    }


    private void jMenuItemPluginActionPerformed(java.awt.event.ActionEvent evt) {

        if (this.pluginEntries.containsKey( evt.getSource() ) ) {
            PluginEntry pe = (PluginEntry)this.pluginEntries.get( evt.getSource() );
            pe.getPlugin().call();
        }

    }

    /** Getter for property pluginConfigurationDialog.
     * @return Value of property pluginConfigurationDialog.
     *
     */
    public it.businesslogic.ireport.gui.PluginConfigurationDialog getPluginConfigurationDialog() {

        if (pluginConfigurationDialog == null) {
            pluginConfigurationDialog = new PluginConfigurationDialog(this);
            pluginConfigurationDialog.setPlugins( this.getPluginEntries().values() );
        }

        return pluginConfigurationDialog;
    }

    /** Setter for property pluginConfigurationDialog.
     * @param pluginConfigurationDialog New value of property pluginConfigurationDialog.
     *
     */
    public void setPluginConfigurationDialog(it.businesslogic.ireport.gui.PluginConfigurationDialog pluginConfigurationDialog) {
        this.pluginConfigurationDialog = pluginConfigurationDialog;
    }

    /** Getter for property pluginEntries.
     * @return Value of property pluginEntries.
     *
     */
    public java.util.HashMap getPluginEntries() {
        return pluginEntries;
    }

    /** Setter for property pluginEntries.
     * @param pluginEntries New value of property pluginEntries.
     *
     */
    public void setPluginEntries(java.util.HashMap pluginEntries) {
        this.pluginEntries = pluginEntries;
    }

    public void languageChanged(LanguageChangedEvent evt) {

        nickyb.sqleonardo.common.util.I18n.setBaseName( I18n.baseName );
        nickyb.sqleonardo.common.util.I18n.setLocalPackageName( I18n.localPackageName );
        nickyb.sqleonardo.common.util.I18n.setCurrentLocale( evt.getLocale() );

        this.applyI18n();
    }

    /**
     * Return the active Report frame. return null if no report frame is actually activated.
     */
    public JReportFrame getActiveReportFrame() {
        if (jMDIDesktopPane.getSelectedFrame() != null &&
        jMDIDesktopPane.getSelectedFrame() instanceof JReportFrame) {
            return (JReportFrame)jMDIDesktopPane.getSelectedFrame();
        }
        return null;
    }

    /** Getter for property reportClassLoader.
     * @return Value of property reportClassLoader.
     *
     */
    public it.businesslogic.ireport.ReportClassLoader getReportClassLoader() {
        if (reportClassLoader == null)
        {
           reportClassLoader = new ReportClassLoader();
        }
        reportClassLoader.rescanLibDirectory();
        return reportClassLoader;
    }

    /** Setter for property reportClassLoader.
     * @param reportClassLoader New value of property reportClassLoader.
     *
     */
    public void setReportClassLoader(it.businesslogic.ireport.ReportClassLoader reportClassLoader) {
        this.reportClassLoader = reportClassLoader;
    }

    /**
     * Getter for property activeStyleClipboard.
     * @return Value of property activeStyleClipboard.
     */
    public int getActiveStyleClipboard() {
        return activeStyleClipboard;
    }

    /**
     * Setter for property activeStyleClipboard.
     * @param activeStyleClipboard New value of property activeStyleClipboard.
     */
    public void setActiveStyleClipboard(int activeStyleClipboard) {
        this.activeStyleClipboard = activeStyleClipboard;
    }


    public it.businesslogic.ireport.ReportElement getStyleClipboard() {
        return styleClipboards[getActiveStyleClipboard()];
    }

    public void setStyleClipbardContent(it.businesslogic.ireport.ReportElement element) {
        styleClipboards[getActiveStyleClipboard()] = element;

    }

    // Hack for MacOS X suggested by Gary Nunes
    public java.awt.Dimension getMinimumSize() {
        return new java.awt.Dimension(50, 50); // arbitrary minimum size value
    }

    /**
     * Getter for property currentZoomFactor.
     * @return Value of property currentZoomFactor.
     */
    public double getCurrentZoomFactor() {
        return currentZoomFactor;
    }

    /**
     * Setter for property currentZoomFactor.
     * @param currentZoomFactor New value of property currentZoomFactor.
     */
    public void setCurrentZoomFactor(double currentZoomFactor) {
        this.currentZoomFactor = currentZoomFactor;
    }

    public void setComboBoxZoomFactor(double zoomFactor) {
        this.jNumberComboBoxZoom.setValue(zoomFactor);
        //logOnConsole(this.jNumberComboBoxZoom.getValue() + "\n" ) ;
    }

    /**
     * Getter for property libraryPanel.
     * @return Value of property libraryPanel.
     */
    public LibraryPanel getLibraryPanel() {
        return this.libraryPanel;
    }


    /**
     * Getter for the LibraryView
     * this is used to insert tabs for Plugins
     * @return View
     */
    public View getLibraryView() {
      return this.viewLibrary;
    }

    /**
     * Setter for property libraryPanel.
     * @param libraryPanel New value of property libraryPanel.
     */
    public void setLibraryPanel(LibraryPanel libraryPanel) {
        this.libraryPanel = libraryPanel;
    }

    public void clearConsole()
    {
        getLogPane().getMainLogTextArea().clearConsole();
    }

    public LocaleResourceFilesDialog getLocaleFilesDialog() {
        return localeFilesDialog;
    }

    public void setLocaleFilesDialog(LocaleResourceFilesDialog localeFilesDialog) {
        this.localeFilesDialog = localeFilesDialog;
    }

    /**
     * Registers ReportListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addReportListener(it.businesslogic.ireport.gui.event.ReportListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.ReportListener.class, listener);
    }

    /**
     * Removes ReportListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeReportListener(it.businesslogic.ireport.gui.event.ReportListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.ReportListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireReportListenerReportElementsSelectionChanged(it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportElementsSelectionChanged (event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireReportListenerReportElementsChanged(it.businesslogic.ireport.gui.event.ReportElementChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportElementsChanged (event);
            }
        }
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireReportListenerReportBandChanged(it.businesslogic.ireport.gui.event.ReportBandChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportBandChanged (event);
            }
        }
    }

    public Vector getClasspath() {

        if (classpath == null || classpath.size() == 0)
        {
            classpath = new Vector();

            // Each line is a path/jar...
            StringTokenizer  st = new StringTokenizer( getProperties().getProperty("classpath",""),"\n");
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                if (token != null && token.trim().length() > 0)
                {
                    classpath.addElement( token.trim() );
                }
            }

        }
        return classpath;
    }

    public void setClasspath(Vector classpath) {
        this.classpath = classpath;
        String cp_property = "";
        for (int i=0; i<classpath.size(); ++i)
        {
            cp_property += classpath.elementAt(i) +"\n";
        }
        getProperties().setProperty("classpath",cp_property);
    }



    public Vector getFontspath() {

        if (fontspath == null || fontspath.size() == 0)
        {
            fontspath = new Vector();

            // Each line is a path/jar...
            StringTokenizer  st = new StringTokenizer( getProperties().getProperty("fontspath",""),"\n");
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                if (token != null && token.trim().length() > 0)
                {
                    fontspath.addElement( token.trim() );
                }
            }

        }
        return fontspath;
    }

    public void setFontspath(Vector fontspath) {
        this.fontspath = fontspath;
        String cp_property = "";
        for (int i=0; i<fontspath.size(); ++i)
        {
            cp_property += fontspath.elementAt(i) +"\n";
        }
        getProperties().setProperty("fontspath",cp_property);
    }

    // Indicate whether Report Name must be synchronized
    public boolean isSynchronizeReportName()
    {
        return getProperties().getProperty("SynchronizeReportName", "").equals("true") ;
    }

    public boolean isShowOptionDialogSynchronize(  String reportName, String filename ) {

        return false;
        /*
        int ret = I18nOptionPane.showOptionDialog(this,
                                    "gui.MainFrame.SynchronizeReportName",
                                    "",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null, new String[]{"yes", "no"},
                                    "no",
                                    "Report Name: " + reportName + "\n" + "File Name: " + filename  );

                                    return (ret==0);
        */
    }

    public JReportFrame getSelectedJRFFromFilesTree()
    {
        TreePath path = jTreeFiles.getSelectionPath();
        if (path == null) return null;
        javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode)path.getLastPathComponent();
        if (node instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode nodem = (DefaultMutableTreeNode)node;
                Object obj = nodem.getUserObject();
                if (obj != null && obj instanceof DocumentTreeEntry) {
                    DocumentTreeEntry dtn = (DocumentTreeEntry)obj;
                    return dtn.getJrf();
                }
        }

        return null;
    }

    /**
     * List of JReportFrames selected in the files tree
     */
    public List getSelectedJRFsFromFilesTree()
    {
        TreePath[] paths = jTreeFiles.getSelectionPaths();
        List jReportFrames = new java.util.ArrayList();
        if (paths == null) return jReportFrames;
        for (int i=0; i<paths.length; ++i)
        {
            TreePath path = paths[i];
            if (path == null) continue;

            javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode)path.getLastPathComponent();
            if (node instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode nodem = (DefaultMutableTreeNode)node;
                    Object obj = nodem.getUserObject();
                    if (obj != null && obj instanceof DocumentTreeEntry) {
                        DocumentTreeEntry dtn = (DocumentTreeEntry)obj;
                        jReportFrames.add( dtn.getJrf() );
                    }
            }
        }
        return jReportFrames;
    }

    public JMDIDesktopPane getJMDIDesktopPane() {
        return jMDIDesktopPane;
    }

    public void updateAntialiasMode()
    {
        javax.swing.JInternalFrame[] frames = jMDIDesktopPane.getAllFrames();
        for (int i=0; i< frames.length; ++i) {
            if (frames[i]  instanceof JReportFrame) {
                JReportFrame jrf = (JReportFrame)frames[i];
                jrf.updateAntialiasMode();
            }
        }
    }

    public Vector getChartSeriesClipBoard() {
        return chartSeriesClipBoard;
    }

    public void setChartSeriesClipBoard(Vector chartSeriesClipBoard) {
        this.chartSeriesClipBoard = chartSeriesClipBoard;
    }

    public it.businesslogic.ireport.chart.Dataset getChartDatasetClipBoard() {
        return chartDatasetClipBoard;
    }

    public void setChartDatasetClipBoard(it.businesslogic.ireport.chart.Dataset chartDatasetClipBoard) {
        this.chartDatasetClipBoard = chartDatasetClipBoard;
    }

    public LogPane getLogPane() {
        return logPane;
    }

    public void setLogPane(LogPane logPane) {
        this.logPane = logPane;
    }

    public DocumentStructurePanel getDocumentStructurePanel() {
        return documentStructurePanel;
    }

    public void setDocumentStructurePanel(DocumentStructurePanel documentStructurePanel) {
        this.documentStructurePanel = documentStructurePanel;
    }

    public void styleChanged(StyleChangedEvent evt)
    {
        if (this.getActiveReportFrame() != null)
        {
            this.getActiveReportFrame().getReportPanel().repaint();
            this.getElementPropertiesDialog().updateSelection();
        }
    }
    
    public void templateChanged(TemplateChangedEvent evt)
    {
        if (this.getActiveReportFrame() != null)
        {
            this.getActiveReportFrame().getReportPanel().repaint();
            this.getElementPropertiesDialog().updateSelection();
        }
    }

    public Vector getMeasuresClipBoard() {
        return measuresClipBoard;
    }

    public void setMeasuresClipBoard(Vector measuresClipBoard) {
        this.measuresClipBoard = measuresClipBoard;
    }

    /**
     * Registers ReportFrameActivatedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addReportFrameActivatedListener(it.businesslogic.ireport.gui.event.ReportFrameActivatedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.ReportFrameActivatedListener.class, listener);
    }

    /**
     * Removes ReportFrameActivatedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeReportFrameActivatedListener(it.businesslogic.ireport.gui.event.ReportFrameActivatedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.ReportFrameActivatedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireReportFrameActivatedListenerReportFrameActivated(it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportFrameActivatedListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportFrameActivatedListener)listeners[i+1]).reportFrameActivated (event);
            }
        }
    }


    public Vector getSelectedElements()
    {
        JReportFrame newJrf = getActiveReportFrame();
        Vector selection = new Vector();
        if (newJrf != null)
        {
             if (newJrf.getSelectedCrosstabEditorPanel() == null)
             {
                selection.addAll( newJrf.getSelectedElements() );
             }
             else
             {
                selection.addAll( newJrf.getSelectedCrosstabEditorPanel().getSelectedElements() );
             }
        }
        return selection;
    }

    /*
    public it.businesslogic.ireport.gui.docking.DockingContainer getDockingContainerRight() {
        return dockingContainerRight;
    }

    public void setDockingContainerRight(it.businesslogic.ireport.gui.docking.DockingContainer dockingContainerRight) {
        this.dockingContainerRight = dockingContainerRight;
    }

    public it.businesslogic.ireport.gui.docking.DockingContainer getDockingContainerLeft() {
        return dockingContainerLeft;
    }

    */

    public java.util.Vector getToolBarControls() {
        return toolBarControls;
    }

    public StylesView getStylesView() {
        return stylesView;
    }

    public void setStylesView(StylesView stylesView) {
        this.stylesView = stylesView;
    }

    public Vector getStyleLibrarySet() {
        return styleLibrarySet;
    }

    public void setStyleLibrarySet(Vector styleLibrarySet) {
        this.styleLibrarySet = styleLibrarySet;
    }

    /**
     * Used to update dynamically the LAF
     */
    public void upadateLAF()
    {
        PlafManager.setPreferredTheme("win32");
        SwingUtilities.updateComponentTreeUI(MainFrame.getMainInstance());
        SwingUtilities.updateComponentTreeUI(getElementPropertiesDialog());
        SwingUtilities.updateComponentTreeUI(getBandsDialog());
        SwingUtilities.updateComponentTreeUI(getGroupsDialog());
        SwingUtilities.updateComponentTreeUI(getLocaleFilesDialog());
        SwingUtilities.updateComponentTreeUI(getReportQueryDialog());
        SwingUtilities.updateComponentTreeUI(getPluginConfigurationDialog());
}

    /**
     * This method can be used by plugins that needs to add new custom fonts...
     */
    public void updateFontsLists()
    {
        fireFontsListChangedListenerFontsListChanged(new FontsListChangedEvent( getTtfFonts()));
    }


    /**
     * Reload the fonts list...
     *
     */
    public void reloadFontsLists()
    {
        FontLoaderDialog fll = new FontLoaderDialog(this, false);
        this.ttfFonts = FontListLoader.loadTTFFonts(fll);

        try {
            SwingUtilities.invokeAndWait(new Runnable()
            {
               public void run()
               {
                   fireFontsListChangedListenerFontsListChanged(new FontsListChangedEvent( getTtfFonts()));
               }
            });

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Registers FontsListChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addFontsListChangedListener(it.businesslogic.ireport.gui.event.FontsListChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.FontsListChangedListener.class, listener);
    }

    /**
     * Removes FontsListChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeFontsListChangedListener(it.businesslogic.ireport.gui.event.FontsListChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.FontsListChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireFontsListChangedListenerFontsListChanged(FontsListChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.FontsListChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.FontsListChangedListener)listeners[i+1]).fontsListChanged (event);
            }
        }
    }

    public void reportDocumentStatusChanged(ReportDocumentStatusChangedEvent evt) {

        jTreeFiles.updateUI();

    }


    private void jBoxButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setElementsValue("setBox", jBoxButton.getLastBox(), it.businesslogic.ireport.Box.class);
    }

    /**
     * When a view is closed, we should update the menu...
     *
     */
    protected void closedView(View view)
    {
        if (view == viewFiles) jCheckBoxMenuItemExplorer.setSelected(false);
        if (view == viewDocumentStructure) jCheckBoxMenuItemDocumentStructure.setSelected(false);
        if (view == viewPropertySheet) jCheckBoxMenuItemElementProperties.setSelected(false);
        if (view == viewThreads) jCheckBoxMenuItemThreadList.setSelected(false);
        if (view == viewLibrary) jCheckBoxMenuItemLibrary.setSelected(false);
        if (view == logPaneView) jCheckBoxMenuItemOutput.setSelected(false);
        if (view == stylesPanleView) jCheckBoxMenuItemStylesLibrary.setSelected(false);
        // Can not be closed... if (view == crosstabStructureView = null;
        // Can not be closed... if (view == crosstabMeasuresView = null;
    }

    private View createView(String id, String text, boolean closable, boolean pin, Component c) {
		View view = new View(id, text);

		if (closable)
                {
                    view.getTitlebar().addAction(DockingConstants.CLOSE_ACTION);
                    if ( view.getTitlebar().getActionButton(DockingConstants.CLOSE_ACTION) != null)
                    {
                        view.getTitlebar().getActionButton(DockingConstants.CLOSE_ACTION).addActionListener(
                        new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					View viewx = (View)javax.swing.SwingUtilities.getAncestorOfClass(View.class, (java.awt.Component)e.getSource());
			    		closedView( viewx );
                                        //System.out.println( "Closed: " + viewx.getPersistentId());
				}
			}
                    );
                    }
                }
		if (pin) view.addAction(DockingConstants.PIN_ACTION);



		JPanel p = new JPanel();
		//		p.setBackground(Color.WHITE);
                p.setLayout(new BorderLayout());
		p.setBorder(new LineBorder(java.awt.Color.GRAY, 1));


		p.add(c, BorderLayout.CENTER);

		view.setContentPane(p);
                return view;
	}

    private View createDesktopPage() {
		String id = "Desktop";
        //System.out.println("Creating view");        
        View view = new View(id, null, null);
        //System.out.println("View created");        
		view.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		view.setTitlebar(null);
		view.setContentPane(jMDIDesktopPane);
        view.setSize(1000,100);    
		return view;
	}

    public View getDesktopView()
    {
        return desktop;
    }

    public Vector getQueryExecuters() {
        return queryExecuters;
    }

    public void setQueryExecuters(Vector queryExecuters) {
        this.queryExecuters = queryExecuters;
    }

    public boolean isNoExit() {
        return noExit;
    }

    public void setNoExit(boolean noExit) {
        this.noExit = noExit;
    }

    public boolean isUsingWS() {
        return usingWS;
    }

    public void setUsingWS(boolean usingWS) {
        this.usingWS = usingWS;
    }

    public List getUserChoicesWizardTemplates() {

        if (userChoicesWizardTemplates == null)
        {
            userChoicesWizardTemplates = UserChoicesWizardTemplate.loadWizardTemplates();
        }

        return userChoicesWizardTemplates;
    }

    public void setUserChoicesWizardTemplates(List userChoicesWizardTemplates) {
        this.userChoicesWizardTemplates = userChoicesWizardTemplates;
    }
    /**
     * getSpecialParamters
     * added by Felix Firgau on March 3rd 2007
     * Returns a HashMap with parameters for a Datasource of generated a Report.
     * This method is used for subclassing.
     * @param hm Map
     * @param report Report
     * @return Map
     */
    public Map getSpecialParameters(Map hm, Report report) {
      return hm;
    }

    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
        fireReportListenerReportBandsSelectionChanged(evt);
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
        fireReportListenerReportObjectsSelectionChanged(evt);
    }

/** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportBandsSelectionChanged(it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportBandsSelectionChanged(event);
            }
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportObjectsSelectionChanged(it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportObjectsSelectionChanged(event);
            }
        }
    }

    public boolean getMagnetEnabled() {
        return magnetEnabled;
    }

    public void setMagnetEnabled(boolean magnetEnabled) {
        this.magnetEnabled = magnetEnabled;
    }

    public DocumentSheetPanel getReportSheetPanel() {
        return reportSheetPanel;
    }

    /** 
     * Get the list of ConnectionImplementations.
     * To add new connection implementations, please use the method #addConnectionImplementation
     */
    public java.util.List getConnectionImplementations() {
        return connectionImplementations;
    }

    public void setConnectionImplementations(java.util.List connectionImplementations) {
        this.connectionImplementations = connectionImplementations;
    }
    
    /** 
     * This method provides the preferred way to register a new connection type
     */
    public boolean addConnectionImplementation(String className)
    {
        if (getConnectionImplementations().contains(className)) return true;
        
        try {
            
            Class.forName(className, true, this.getReportClassLoader());
            getConnectionImplementations().add(className);
            return true;
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
        return false;
    }
    
    /** 
     * This method provides the preferred way to register a new connection type
     * Return false if a custom QueryExecuter is already defined for the specified 
     * language and overrideSameLanguage is false.
     *
     * if qed is null, the method returns false.
     */
    public boolean addQueryExecuterDef(QueryExecuterDef qed, boolean overrideSameLanguage)
    {
        if (qed == null) return false;
        // Look for another qed with the same language...
        boolean found = false; // Override the QE at that position...
        
        for (int i=0; i<getQueryExecuters().size(); ++i)
        {
            QueryExecuterDef tqe = (QueryExecuterDef)getQueryExecuters().get(i);
            if (tqe.getLanguage().equals( qed.getLanguage()) )
            {
                if (overrideSameLanguage)
                {
                   getQueryExecuters().setElementAt(qed,i);
                   found = true;
                   break;
                }
                else
                {
                    return false;
                }
            }
        }
        
        if (!found)
        {
            getQueryExecuters().add( qed );
        }
        
        // register the QE in the JasperServer properties...
        net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory." + qed.getLanguage(), qed.getClassName());
            
        return true;
    }
    
    public void addDefaultConnectionImplementations()
    {
        addConnectionImplementation("it.businesslogic.ireport.connection.JDBCConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRXMLDataSourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JavaBeanDataSourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRCSVDataSourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRDataSourceProviderConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRCustomDataSourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JREmptyDatasourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRHibernateConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRSpringLoadedHibernateConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.EJBQLConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.JRXMLADataSourceConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.MondrianConnection");
        //addConnectionImplementation("it.businesslogic.ireport.connection.NullConnection");
        //addConnectionImplementation("it.businesslogic.ireport.connection.JRCustomConnection");
        addConnectionImplementation("it.businesslogic.ireport.connection.QueryExecuterConnection");
    }

    public static java.util.Properties getBrandingProperties() {
        return brandingProperties;
    }

    public static void setBrandingProperties(java.util.Properties v_brandingProperties) {
        brandingProperties = v_brandingProperties;
    }
    
    public static void loadRebrandingProperties()
    {
        Misc.addProperties("/it/businesslogic/ireport/res/default.branding.properties", getBrandingProperties());
        Misc.addProperties("/ireport/branding.properties", getBrandingProperties());
    }   
    
    public void addGeopistaConnection(){
    	/*
         * Cambio albegarcia: Leemos cada vez la conexion de Geopista de las
         * preferencias del usuario, ya que si queda almacenada en la
         * configuracion del usuario (en Documents And
         * Settings/usuario/.iReport), los cambios en el configurador local no
         * tienen efectos, ya que no se leera mas veces del registro
         */        
        String defaultConnection = "LocalGIS BD";
        if (!defaultConnection.equals("")) {
        	/*
             * Nos recorremos todas las conexiones que tengamos y si encontramos
             * la conexion de geopista lo eliminamos para volverlo a leer del
             * registro
             */
            int sizeOfConnections = connections.size();
            for (int i = 0; i < sizeOfConnections; i++) {
        		IReportConnection irc = (IReportConnection)connections.get(i);
        		if (Misc.nvl(irc.getName(),"").equals( defaultConnection )) {
        		    connections.removeElementAt(i);
        			break;
        		}
        	}
        }
        
        //String urlBD = aplicacion.getString("conexion.url.jasper");
        String url=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL,"",false);
        JDBCConnection geopistaJdbcConnection = new JDBCConnection();
        geopistaJdbcConnection.setUrl(url);
        geopistaJdbcConnection.setUsername(aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER));
        
        String passwordToRecover=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);        		
        String passwordToShow=passwordToRecover;
        try {
			EncriptarPassword ec=new EncriptarPassword(passwordToRecover);
			passwordToShow=ec.desencriptar();
		} catch (Exception e) {
			passwordToShow=new String(passwordToRecover);
		}
        geopistaJdbcConnection.setPassword(passwordToShow);
        geopistaJdbcConnection.setSavePassword(true);
        if (url.startsWith("jdbc:rmi")) {
            geopistaJdbcConnection.setJDBCDriver("org.objectweb.rmijdbc.Driver");
        } else {
            geopistaJdbcConnection.setJDBCDriver(aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER));
        }
        geopistaJdbcConnection.setName(defaultConnection);
        properties.put("DefaultConnection", geopistaJdbcConnection);
        connections.add(geopistaJdbcConnection);
    }
    
    

}//end class MainFrame
