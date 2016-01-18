package fr.michaelm.bsheditor;

import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import java.io.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.tree.*;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import buoy.event.*;
import buoy.widget.*;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.SyntaxStyle;
import org.syntax.jedit.TextAreaDefaults;
import org.syntax.jedit.tokenmarker.Token;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
//import org.gjt.sp.jedit.textarea.JEditTextArea;
import bsh.Interpreter;

/**
 * BeanShell script editor has :
 * <ul>
 * <li>A Script editor text area with new, open, save and save as commands</li>
 * <li>A command line textfield to write and run one line beanshell scripts</li>
 * <li>An output text area</li>
 * <li>A Tree panel to easily access your script files directory (open your
 * script file with a double-clicking in the tree panel)</li>
 * <li>Easy loading/import of jar files located in a given jar directory
 * (right-click on a jar file name to addClassPath and/or import package in the
 * script file).</li>
 * </ul>
 * <p>
 * This editor is ©2004 Michaël MICHAUD and published under the LGPL
 * <p>
 * It is based on (and would be nothing without) :
 * <ul>
 * <li>BeanShell, the java-based script library from Pat Niemeyer (LGPL)</li>
 * <li>Buoy, the user interface library from Peter Eastman (Public Domain)</li>
 * <li>JEditSyntax package from Slava Pestov (MIT licence)</li>
 * </ul>
 * <p>[TODO] Help and documentation
 *
 * @author Michaël MICHAUD
 * @version 0.2 (20/03/2009)
 */
//0.1 (2004-11-13) initial release
//0.2 (2009-03-20) makes the code java 5.0 compatible (M. Michaud)
public class BeanShellEditor extends BFrame {
   
   /**
    * Properties of the BeanShellEditor are :
    * <ul>
    * <li>scriptsFolder directory path</li>
    * <li>jarsFolder directory path</li>
    * <li>start.bsh file path</li>
    */
    private Properties properties;
   
   /**
    * Default interpreter. A new Interpreter is created each time the main bsh
    * editor is run.
    */
    private Interpreter interpreter = new Interpreter();
    
    private Logger log = Logger.getLogger("BshEditor");
    private StreamHandler outputStreamHandler;
    private final java.util.logging.Formatter formatter = new SimpleFormatter(){
        public String format(LogRecord record) {
            if (record.getLevel().intValue()>800) return "\n"+record.getMessage() + "!!!\n";
            else if (record.getLevel().intValue()==500) return "\n\t"+record.getMessage();
            else if (record.getLevel().intValue()==400) return "\n\t\t"+record.getMessage();
            else if (record.getLevel().intValue()==300) return "\n\t\t\t"+record.getMessage();
            else return "\n"+record.getMessage();
        }
    };
    
   /**
    * Get the default locale of the jvm
    * This value can be overloaded by -I18N argument of the main or constructor
    */
    private Locale locale;
    private ResourceBundle i18n;

   
   /**
    * Map defining a set of initial variables
    */
    Map bshEditorInitVariables = new HashMap();
   
    // MENU BAR
    BMenuBar menuBar;
      BMenu fileMenu;
        BMenuItem newScriptMenuItem;
        BMenuItem openScriptMenuItem;
        BMenuItem saveScriptMenuItem;
        BMenuItem saveScriptAsMenuItem;
            boolean overwriting;
            
      BMenu optionMenu;
        BMenuItem chooseScriptsFolder;
          File scriptsFolder;
        BMenuItem chooseJarsFolder;
          File jarsFolder;
            Map jarFileMap;
            Set importsSet;
          File extFolder;
        BMenuItem chooseStartFile;
          File startFile;
        BCheckBoxMenuItem startFileCBMI;
            boolean alwaysStartFile = true;
        BCheckBoxMenuItem verboseCBMI;
            boolean verbose = false;
      
      BMenu helpMenu;
          BMenuItem infoMenuItem;
          BMenuItem helpMenuItem;
    
    // The main container for the UI contains
    // - a zone for toolbars (north)
    // - a split panel (center)
    BorderContainer bc;
    
    // TOOLBAR
    // TODO : replace button text by icons
    ColumnContainer toolBars;
    RowContainer mainToolBar;
        BButton newButton;
        BButton openButton;
        BButton saveButton;
        BButton saveAsButton;
        BButton runScriptButton;
    RowContainer pluginsToolBar;
        HashMap plugins;
    
    BSplitPane mainPanel;
    
    // LEFT PANEL
    BTabbedPane leftTabbedPane;
        // Script files tree
        BScrollPane bshScriptsScrollPane;
        BTree bshScriptsTree;
        // Jars list
        BScrollPane jarsScrollPane;
        ColumnContainer jarsContainer;
        // Variables list
        BScrollPane varsScrollPane;
        ColumnContainer varListContainer;
        BList variablesList;
        
    // MAIN PANEL
    BSplitPane rightPanel;
        BorderContainer northPanel;
            BLabel scriptTitle;
            JEditTextArea jEditTextArea;
        BorderContainer southPanel;
            FormContainer commandLineContainer;
                BLabel commandLineLabel;
                BTextField commandLine;
                  List commands = new ArrayList();
                  int commandIndex = 0;
                BButton runCommand;
                BButton clearOutput;
            BScrollPane outputScrollPane;
                BTextArea outputTextArea;
                  PrintStream out;
    
   /**
    * Main method : create a BeanShellEditor.
    * <p>When BeanShellEditor is launched as a standalone application, the
    * WindowClosingEvent shut the jvm down
    * @param args if args[0] is -i18n, args[1] and args[2] must be the language
    * and the country 2 letters code
    */
    public static void main(String[] args) {
        try {
            Locale loc;
            if (args.length>2 && args[0].equalsIgnoreCase("-i18n")) {
                loc = new Locale(args[1], args[2]);
            }
            else loc = Locale.getDefault();
            BeanShellEditor bshEditor = new BeanShellEditor(loc);
            bshEditor.addEventLink(WindowClosingEvent.class, bshEditor, "exitJvm");
        }
        catch(Exception e) {e.printStackTrace();}
    }
    
   /**
    * Default constructor with no argument.
    * <p>Used by main() method for standalone applications.
    */
    public BeanShellEditor(Locale loc) {
        super("BeanShell Editor");
        addEventLink(WindowClosingEvent.class, this, "exit");
        addEventLink(WindowResizedEvent.class, this, "resize");
        locale = loc==null?Locale.getDefault():loc;
        i18n = ResourceBundle.getBundle("BeanShellEditor_i18n", locale);
        initUI();
        initProperties();
        pack();
        setVisible(true);
        initInterpreter();
        //startingScript();
    }
    
   /**
    * Constructor with a map argument used to create a plugin BeanShellEditor.
    * <p>
    * When BeanShellEditor is used as a plugin, vars may be used to initialize
    * beanshell variables.<p>
    * Typically, if BeanShellEditor is a plugin of MyApplication, one should
    * initialize BeanShellEditor the following way :<ol>
    * <li>MyApplication myApp = new MyApplication();</li>
    * <li>Map map = new HashMap();</li>
    * <li>map.put("application", myApp);</li>
    * <li>BeanShellEditor bshEditor = new BeanShellEditor(map);</li>
    * </ol>
    * This way, BeanShellEditor may use the word "application" to access to
    * the object myApp.
    * @param vars parameter used to map interpreter variable names with objects
    * from the main application when BeanShellEditor is used as a plugin
    */
    public BeanShellEditor(Map vars, Locale loc) {
        super("BeanShell Editor");
        addEventLink(WindowClosingEvent.class, this, "exit");
        addEventLink(WindowResizedEvent.class, this, "resize");
        locale = loc==null?Locale.getDefault():loc;
        i18n = ResourceBundle.getBundle("BeanShellEditor_i18n", locale);
        initUI();
        initProperties();
        pack();
        setVisible(true);
        bshEditorInitVariables.putAll(vars);
        initInterpreter();
    }

   /**
    * If the file defined by the start property exists, initialize the
    * interpreter with the starting script
    * this private method is just called once by the constructor
    */
    private void startingScript() {
        String start = properties.getProperty("start");
	    try {
            if (start!=null && new File(start).exists()) {
                interpreter.source(start);
            }
        } catch(java.io.FileNotFoundException fnfe) {
            fnfe.printStackTrace(new PrintWriter(out, true));
        } catch(java.io.IOException ioe) {
            ioe.printStackTrace(new PrintWriter(out, true));
        } catch(bsh.EvalError ee) {
            ee.printStackTrace(new PrintWriter(out, true));
        }
    }

   /**
    * User Interface Initialization
    */
    private void initUI() {
        // MENUBAR ELEMENTS INITIALIZATION
        menuBar = new BMenuBar();
            fileMenu = new BMenu(i18n.getString("menu.file"));
                newScriptMenuItem = new BMenuItem(i18n.getString("menu.file.new"));
                    fileMenu.add(newScriptMenuItem);
                    newScriptMenuItem.addEventLink(CommandEvent.class, this, "newScript");
                openScriptMenuItem = new BMenuItem(i18n.getString("menu.file.open"));
                    fileMenu.add(openScriptMenuItem);
                    openScriptMenuItem.addEventLink(CommandEvent.class, this, "open");
                saveScriptMenuItem = new BMenuItem(i18n.getString("menu.file.save"));
                    fileMenu.add(saveScriptMenuItem);
                    saveScriptMenuItem.addEventLink(CommandEvent.class, this, "save");
                saveScriptAsMenuItem = new BMenuItem(i18n.getString("menu.file.saveas"));
                    fileMenu.add(saveScriptAsMenuItem);
                    saveScriptAsMenuItem.addEventLink(CommandEvent.class, this, "saveAs");
            optionMenu = new BMenu(i18n.getString("menu.options"));
                chooseScriptsFolder = new BMenuItem(i18n.getString("menu.options.scriptfolder"));
                    scriptsFolder = new File(".");
                    optionMenu.add(chooseScriptsFolder);
                    chooseScriptsFolder.addEventLink(CommandEvent.class, this, "chooseScriptsFolder");
                chooseJarsFolder = new BMenuItem(i18n.getString("menu.options.extlib"));
                    jarsFolder = new File(".");
                    //jarFiles = new TreeSet();
                    jarFileMap = new HashMap();
                    importsSet = new TreeSet();
                    optionMenu.add(chooseJarsFolder);
                    chooseJarsFolder.addEventLink(CommandEvent.class, this, "changeJarsFolder");
                chooseStartFile = new BMenuItem(i18n.getString("menu.options.startingscript"));
                    startFile = new File("start.bsh");
                    optionMenu.add(chooseStartFile);
                    chooseStartFile.addEventLink(CommandEvent.class, this, "chooseStartFile");
                startFileCBMI = new BCheckBoxMenuItem();
                    startFileCBMI.setText(i18n.getString("menu.options.startingscriptalways"));
                    startFileCBMI.setState(true);
                    optionMenu.add(startFileCBMI);
                    startFileCBMI.addEventLink(CommandEvent.class, this, "changeAlwaysStartFile");
                verboseCBMI = new BCheckBoxMenuItem();
                    verboseCBMI.setText(i18n.getString("menu.options.verboseoutput"));
                    optionMenu.add(verboseCBMI);
                    verboseCBMI.addEventLink(CommandEvent.class, this, "changeVerbose");
            helpMenu = new BMenu(i18n.getString("menu.help"));
                infoMenuItem = new BMenuItem(i18n.getString("menu.help.help"));
                    helpMenu.add(infoMenuItem);
                helpMenuItem = new BMenuItem(i18n.getString("menu.help.info"));
                    helpMenu.add(helpMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        this.setMenuBar(menuBar);
        
        // TOOLBAR ELEMENTS INITIALIZATION
        toolBars = new ColumnContainer();
        toolBars.setDefaultLayout(new LayoutInfo(LayoutInfo.NORTHWEST, LayoutInfo.NONE, new Insets(3,3,3,3), null));
        // Standard toolbar
        mainToolBar = new RowContainer();
            ImageIcon newImageIcon = new ImageIcon(BeanShellEditor.class.getResource("images/New24.gif"));
            newButton = new BButton(newImageIcon);
            ImageIcon openImageIcon = new ImageIcon(BeanShellEditor.class.getResource("images/Open24.gif"));
            openButton = new BButton(openImageIcon);
            ImageIcon saveImageIcon = new ImageIcon(BeanShellEditor.class.getResource("images/Save24.gif"));
            saveButton = new BButton(saveImageIcon);
            ImageIcon saveAsImageIcon = new ImageIcon(BeanShellEditor.class.getResource("images/SaveAs24.gif"));
            saveAsButton = new BButton(saveAsImageIcon);
            ImageIcon runAsImageIcon = new ImageIcon(BeanShellEditor.class.getResource("images/Run24.gif"));
            runScriptButton = new BButton(i18n.getString("toolbar.button.run"), runAsImageIcon);
        mainToolBar.add(newButton);
            newButton.addEventLink(CommandEvent.class, this, "newScript");
        mainToolBar.add(openButton);
            openButton.addEventLink(CommandEvent.class, this, "open");
        mainToolBar.add(saveButton);
            saveButton.addEventLink(CommandEvent.class, this, "save");
        mainToolBar.add(saveAsButton);
            saveAsButton.addEventLink(CommandEvent.class, this, "saveAs");
        mainToolBar.add(runScriptButton);
            runScriptButton.addEventLink(CommandEvent.class, this, "runScript");
        toolBars.add(mainToolBar);
        // Plugin toolbar
        pluginsToolBar = new RowContainer();
            plugins = new HashMap();
        toolBars.add(pluginsToolBar);
        
        // LEFT PANEL ELEMENTS INITIALIZATION
        leftTabbedPane = new BTabbedPane(BTabbedPane.TOP);
            bshScriptsScrollPane = new BScrollPane();
                refreshScriptsTree(scriptsFolder);
            jarsScrollPane = new BScrollPane();
                jarsContainer = new ColumnContainer();
                jarsContainer.setDefaultLayout(new LayoutInfo(LayoutInfo.NORTHWEST, LayoutInfo.NONE, new Insets(3,3,3,3), null));
                jarsScrollPane.setContent(jarsContainer);
            // namespace
            varsScrollPane = new BScrollPane();
                variablesList = new BList();
                varListContainer = new ColumnContainer();
                varListContainer.setDefaultLayout(new LayoutInfo(LayoutInfo.NORTHWEST, LayoutInfo.NONE, new Insets(0,0,0,0), null));
                varsScrollPane.setContent(varListContainer);
                //varsScrollPane.setContent(variablesList);
        
        leftTabbedPane.add(bshScriptsScrollPane,
            "<html><b>"+i18n.getString("manager.scriptfolder")+"</b><br></html>",null,0);
        leftTabbedPane.add(jarsScrollPane,
            "<html><b>"+i18n.getString("manager.libraries")+"</b><br></html>",null,1);
        leftTabbedPane.add(varsScrollPane,
            "<html><b>"+i18n.getString("manager.variables")+"</b><br></html>",null,2);
        
        // MAIN PANEL ELEMENTS INITIALIZATION
        rightPanel = new BSplitPane(BSplitPane.VERTICAL);
        rightPanel.setOneTouchExpandable(true);
            // NORTHPANEL CONTAINS :
            // - A TITLE LABEL CONTAINING THE FILE NAME
            // - THE EDITOR (JEditTextArea)
            // - A LABEL CONTAINING THE LINE NUMBER FOR DEBUGGING PURPOSES
            northPanel = new BorderContainer();
            northPanel.setDefaultLayout(new LayoutInfo(LayoutInfo.NORTH, LayoutInfo.BOTH, new Insets(3,3,3,3), null));
                scriptTitle = new BLabel(i18n.getString("editor.title.new"));
                TextAreaDefaults defaults = TextAreaDefaults.getDefaults();
                defaults.cols = 48;
                defaults.rows = 12;
                defaults.electricScroll = 0;
                SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];
                    styles[Token.COMMENT1] = new SyntaxStyle(new Color(0xDD0000),true,false);
                    styles[Token.COMMENT2] = new SyntaxStyle(new Color(0xFF0000),true,false);
                    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(0x000066),false,true);
                    styles[Token.KEYWORD2] = new SyntaxStyle(Color.black,false,true);
                    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(0x009900),false,true);
                    styles[Token.LITERAL1] = new SyntaxStyle(new Color(0xCC0099),false,false);
                    styles[Token.LITERAL2] = new SyntaxStyle(new Color(0xCC0099),false,true);
                    styles[Token.LABEL] = new SyntaxStyle(new Color(0x990033),false,true);
                    styles[Token.OPERATOR] = new SyntaxStyle(Color.black,false,true);
                    styles[Token.INVALID] = new SyntaxStyle(Color.red,false,true);
                defaults.styles = styles;
                jEditTextArea = new JEditTextArea(defaults);
                jEditTextArea.setText(i18n.getString("editor.writehere"));
                jEditTextArea.setTokenMarker(new JavaTokenMarker());
                final BLabel caretPos = new BLabel(i18n.getString("editor.status.numbering"));
                northPanel.add(scriptTitle, BorderContainer.NORTH);
                northPanel.add(new AWTWidget(jEditTextArea), BorderContainer.CENTER);
                northPanel.add(caretPos, BorderContainer.SOUTH);
                jEditTextArea.setFirstLine(0);
                jEditTextArea.addCaretListener(new javax.swing.event.CaretListener(){
                    public void caretUpdate(javax.swing.event.CaretEvent e){
                        caretPos.setText(i18n.getString("editor.status.numbering") + (jEditTextArea.getCaretLine()+1));
                    }
                });

            southPanel = new BorderContainer();
                southPanel.setDefaultLayout(new LayoutInfo(LayoutInfo.NORTHWEST, LayoutInfo.BOTH, new Insets(3,3,3,3), null));
                commandLineContainer = new FormContainer(new double[]{0,1,0,0},new double[]{0});
                commandLineLabel = new BLabel(i18n.getString("commandline.title"));
                commandLine = new BTextField(32);
                    commandLine.addEventLink(KeyPressedEvent.class, this, "runCommand");
                runCommand = new BButton(i18n.getString("commandline.button.run"));
                    runCommand.addEventLink(CommandEvent.class, this, "runCommand");
                clearOutput = new BButton(i18n.getString("commandline.button.clearoutput"));
                    clearOutput.addEventLink(CommandEvent.class, this, "clearOutput");
                commandLineContainer.add(commandLineLabel,0,0);
                commandLineContainer.add(commandLine,1,0, new LayoutInfo(LayoutInfo.WEST, LayoutInfo.HORIZONTAL, new Insets(3,3,3,3), null));
                commandLineContainer.add(runCommand,2,0);
                commandLineContainer.add(clearOutput,3,0);
                outputTextArea = new BTextArea(2,100);
                outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                outputTextArea.setWrapStyle(BTextArea.WRAP_WORD);
                    out = new PrintStream(new Output(outputTextArea));
                outputScrollPane = new BScrollPane(outputTextArea);
                outputScrollPane.setPreferredViewSize(new Dimension(400,100));
                southPanel.add(commandLineContainer,BorderContainer.NORTH);
                southPanel.	setChildLayout(BorderContainer.NORTH, new LayoutInfo(LayoutInfo.WEST, LayoutInfo.HORIZONTAL, new Insets(3,3,3,3), null));
                southPanel.add(outputScrollPane, BorderContainer.CENTER);
        rightPanel.add(northPanel,0);
        rightPanel.add(southPanel,1);
        
        mainPanel = new BSplitPane(BSplitPane.HORIZONTAL);
            mainPanel.setOneTouchExpandable(true);
            //mainPanel.setDividerLocation(0.15);
        mainPanel.add(leftTabbedPane,0);
        mainPanel.add(rightPanel,1);
        mainPanel.layoutChildren();
        
        bc = new BorderContainer();
        bc.add(toolBars, BorderContainer.NORTH);
        bc.add(mainPanel, BorderContainer.CENTER);
        
        this.setContent(bc);
        layoutChildren();
    }
    
    
    private void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("BeanShellEditor.properties"));
            extFolder = new File(properties.getProperty("extFolder","ext"));
            if (!extFolder.exists()) extFolder = new File(".");
            scriptsFolder  = new File(properties.getProperty("scriptsFolder","."));
            if (!scriptsFolder.exists()) scriptsFolder = new File(".");
            jarsFolder = new File(properties.getProperty("jarsFolder","."));
            if (!jarsFolder.exists()) jarsFolder = new File(".");
            refreshScriptsTree(scriptsFolder);
            refreshJarsList();
        } catch(FileNotFoundException fnfe) {
            // if the file doesn't exist, refreshPropertiesFile()
            // will create it with default properties
            refreshPropertiesFile();
        }
        catch(IOException ioe) {
            ioe.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void initInterpreter() {
        try {
            interpreter = new Interpreter();
            interpreter.set("bshEditor", this);
            interpreter.set("log", log);
            // Reset the outputStreamHandler associated with the log object
            if (outputStreamHandler!=null) log.removeHandler(outputStreamHandler);
            out = new PrintStream(new Output(outputTextArea));
            outputStreamHandler  = new StreamHandler(out, formatter) {
                public void publish(LogRecord record) {super.publish(record); this.flush();}
            };
            log.addHandler(outputStreamHandler);
            log.setLevel(Level.ALL);
            outputStreamHandler.setLevel(Level.ALL);
            interpreter.eval("void addPlugin(arg) {" + "\nbshEditor.addPlugin(arg);}");
            interpreter.setOut(out);
            interpreter.setErr(out);
            Iterator it = bshEditorInitVariables.keySet().iterator();
            while (it.hasNext()) {
                String key = (String)it.next();
                interpreter.set(key, bshEditorInitVariables.get(key));
            }
            if (alwaysStartFile) {
                startingScript();
                //String start = properties.getProperty("start");
                //if (start!=null && new File(start).exists()) {interpreter.source(start);}
            }
            refreshVarList();
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void newScript(){
        scriptTitle.setText("New script");
        jEditTextArea.setText("");
    }
    
    private void open() {
        BFileChooser chooser = new BFileChooser(BFileChooser.OPEN_FILE,"Select a bsh script");
        chooser.showDialog(this);
        File script = chooser.getSelectedFile();
        openFile(script);
    }
    
    private void openFile(File script) {
        try {
            long fileLength = script.length();
            if(fileLength>1048576){
                outputTextArea.append("\nERROR : Script files of more than 1048576 bytes can't be read !!");
                return;
            }
            FileReader fr = new FileReader(script);
            char[] buff = new char[(int)fileLength];
            fr.read(buff,0,(int)fileLength);
            String string = new String(buff);
            scriptTitle.setText(script.getAbsolutePath());
            jEditTextArea.setText(string);
            fr.close();
            if (verbose) outputTextArea.append("\nOpen " + script.getAbsolutePath());
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void save() {
        if (scriptTitle.getText().equals("newScript")) {saveAs();}
        else {
            File file = new File(scriptTitle.getText());
            try {
                FileWriter fr = new FileWriter(file);
                fr.write(jEditTextArea.getText(),0,jEditTextArea.getText().length());
                fr.flush();
                fr.close();
            }
            catch(Exception e) {
                e.printStackTrace(new PrintWriter(out, true));
            }
        }
    }
    
    private void saveAs() {
        BFileChooser chooser = new BFileChooser(BFileChooser.SAVE_FILE,"Save current script as...");
        chooser.showDialog(this);
        File file = chooser.getSelectedFile();
        if (file.exists()) {
            getBoolean("Overwrite existing file ?");
            if (!overwriting) return;
        }
        try {
            FileWriter fr = new FileWriter(file);
            fr.write(jEditTextArea.getText(),0,jEditTextArea.getText().length());
            fr.flush();
            fr.close();
            scriptTitle.setText(file.getAbsolutePath());
            refreshScriptsTree(scriptsFolder);
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
   /**
    * Execute a script.
    * <p>For each script execution, BeanShellEditor :
    * <ul>
    * <li>initialize a new interpreter (create a new interpreter with some
    * default variables and the variables passed to the constructor if any)</li>
    * <li>evaluate the starting script if any</li>
    * <li>evaluate the script</li>
    * <li>reset the commandline</li>
    */
    public void runScript() {
        try {
            initInterpreter();
            interpreter.eval(jEditTextArea.getText());
            mainPanel.layoutChildren();
            commandLine.setText("");
            variablesList.removeAll();
            refreshVarList();
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
   /**
    * Used to execute a particular script file from a new button added in the ToolBar.
    * <p>The pluginName must be the relative path of the script file without the
    * extension.
    * <p>[NOTE] I never used it, may be removed in future versions.
    */
    public void addPlugin(String pluginName) {
        BButton pluginButton = new BButton(pluginName);
        plugins.put(pluginName, pluginButton);
        pluginButton.addEventLink(CommandEvent.class, this, "runPlugin");
        refreshPluginsToolbar();
        if (verbose) outputTextArea.append("\nPlugin \"" + pluginName + "\" has been added");
    }
    
    public void removePlugin(String pluginName) {
        plugins.remove(pluginName);
        refreshPluginsToolbar();
    }
    
    private void runPlugin(CommandEvent event) {
        try {
            String bsh = ((BButton)event.getWidget()).getText();
            interpreter.source(scriptsFolder.getPath()+"/"+bsh+".bsh");
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void refreshPluginsToolbar() {
        Iterator it = plugins.keySet().iterator();
        while (it.hasNext()) {pluginsToolBar.add((BButton)plugins.get(it.next()));}
        bc.layoutChildren();
    }
    
   /**
    * List of variables and methods available for the current interpreter
    */
    private void refreshVarList() {
        try {
            String[] var = (String[])interpreter.eval("this.variables");
            Arrays.sort(var);
            varListContainer.removeAll();
            varListContainer.add(new BLabel("VARIABLES :"));
            for (int i = 0 ; i < var.length ; i++) {
                BLabel l = new BLabel(var[i]);
                varListContainer.add(l);
                l.addEventLink(MouseClickedEvent.class, this, "insertVar");
            }
            varListContainer.add(new BLabel(" "));  
            varListContainer.add(new BLabel("METHODES :"));
            String[] meth = (String[])interpreter.eval("this.methods");
            Arrays.sort(meth);
            for (int i = 0 ; i < meth.length ; i++) {
                BLabel l = new BLabel("#"+meth[i]);
                varListContainer.add(l);
                l.addEventLink(MouseClickedEvent.class, this, "insertMeth");
            }
        } catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void insertVar(MouseClickedEvent ev) {
        if (ev.getClickCount() == 1){
            String oldText = commandLine.getText();
            int pos = commandLine.getCaretPosition();
            String insert = ((BLabel)ev.getWidget()).getText();
            commandLine.setText(oldText.substring(0,pos)+ insert + oldText.substring(pos, oldText.length()));
        }
    }
    
    private void insertMeth(MouseClickedEvent ev) {
        if (ev.getClickCount() == 1){
            String oldText = commandLine.getText();
            int pos = commandLine.getCaretPosition();
            String insert = ((BLabel)ev.getWidget()).getText();
            insert = insert.substring(1,insert.length())+"()";
            commandLine.setText(oldText.substring(0,pos)+ insert + oldText.substring(pos, oldText.length()));
        }
    }
    
   /**
    * Run a command line when the user press the "run" button beside the command
    * line or press the "return" key.
    * <p>What is the difference between the script editor and the command line ?
    * <p>Each time you run a script, you evaluate it in a new interpreter
    * (a "new" interpreter has a few default variables, the application
    * variables passed to the constructor, and will first execute the starting
    * script if any).<p>
    * On the other hand, when you run a command line, all the methods and 
    * variables defined during the last script run are available.<p>
    * For example :
    * Run a script containing<ul>
    * <li>fact(int n){result = 1;while(n>0)result=result*n--;print(result);}</li>
    * </ul>
    * <p>Then, in the command line editor, run<ul>
    * <li>fact(6);</li>
    * <li>fact(10);</li>
    * <li>...</li>
    * </ul>
    */
    public void runCommand(WidgetEvent event) {
        if (event instanceof CommandEvent) {
            try {
                interpreter.eval(commandLine.getText());
                commands.add(commandLine.getText());
                commandIndex = commands.size();
                mainPanel.layoutChildren();
                commandLine.setText("");
                variablesList.removeAll();
                refreshVarList();
            }
            catch(Exception e) {
                e.printStackTrace(new PrintWriter(out, true));
            }
        }
        else if (event instanceof KeyPressedEvent) {
            KeyPressedEvent k = (KeyPressedEvent)event;
            if (k.getKeyCode()==KeyPressedEvent.VK_ENTER) {
                try {
                    interpreter.eval(commandLine.getText());
                    commands.add(commandLine.getText());
                    commandIndex = commands.size();
                    mainPanel.layoutChildren();
                    commandLine.setText("");
                    variablesList.removeAll();
                    refreshVarList();
                }
                catch(Exception e) {
                    e.printStackTrace(new PrintWriter(out, true));
                }
            }
            else if (k.getKeyCode()==KeyPressedEvent.VK_DOWN && commands.size()>0) {
                commandIndex = (commandIndex==(commands.size()))?commandIndex:++commandIndex;
                if (commandIndex==commands.size()) commandLine.setText("");
                else commandLine.setText((String)commands.get(commandIndex));
            }
            else if (k.getKeyCode()==KeyPressedEvent.VK_UP && commands.size()>0) {
                commandIndex = (commandIndex==0)?0:--commandIndex;
                commandLine.setText((String)commands.get(commandIndex));
            }
        }
    }
    
    public void clearOutput() {
        outputTextArea.setText("");
    }
    
    private void refreshScriptsTree(File folder) {
        bshScriptsTree = new BTree(new DefaultMutableTreeNode(scriptsFolder.getName()));
        bshScriptsTree.setRootNodeShown(true);
        bshScriptsScrollPane.setContent(bshScriptsTree);
        
        DefaultTreeCellRenderer treeRenderer = new DefaultTreeCellRenderer();
        bshScriptsTree.setCellRenderer(treeRenderer);

        File[] ff = folder.listFiles();
        for (int i = 0 ; i < ff.length ; i++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(ff[i].getName());
            TreePath path = bshScriptsTree.addNode(bshScriptsTree.getRootNode(), node);
            if(ff[i].isDirectory()) {
                node.setAllowsChildren(true);
                File[] fff = ff[i].listFiles();
                for (int j = 0 ; j < fff.length ; j++) {
                    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(fff[j].getName());
                    TreePath path1 = bshScriptsTree.addNode(path, node1);
                    if(fff[j].isDirectory()) {
                        node1.setAllowsChildren(true);
                        File[] ffff = fff[j].listFiles();
                        for (int k = 0 ; k < ffff.length ; k++) {
                            if(ffff[k].isDirectory()) continue;
                            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(ffff[k].getName());
                            node2.setAllowsChildren(false);
                            bshScriptsTree.addNode(path1, node2);
                        }
                    }
                    else {
                        node1.setAllowsChildren(false);
                    }
                }
            }
            else {
                node.setAllowsChildren(false);
                
            }
        }
        bshScriptsTree.addEventLink(MouseClickedEvent.class, new Object() {
            void processEvent(MouseClickedEvent ev) {
                if (ev.getClickCount() == 2){
                    TreePath path = bshScriptsTree.findNode(ev.getPoint());
                    if (path != null && bshScriptsTree.isLeafNode(path)) {
                        StringBuffer filePath = new StringBuffer(scriptsFolder.getAbsolutePath()+"/");
                        for (int i = 1 ; i < path.getPath().length ; i++) {
                            filePath.append("/"+path.getPathComponent(i));
                        }
                        openFile(new File(filePath.toString()));
                    }
                }
                bshScriptsScrollPane.layoutChildren();
            }
        });
    }
    
    private void chooseScriptsFolder() {
        BFileChooser chooser = new BFileChooser(BFileChooser.SELECT_FOLDER,
            "Select folder for bsh scripts",
            new File(System.getProperty("user.dir"))
        );
        chooser.showDialog(this);
        File f =  chooser.getSelectedFile();
        if (f == null) return;
        scriptsFolder = f;
        properties.put("scriptsFolder", scriptsFolder.getPath());
        refreshScriptsTree(scriptsFolder);
        refreshPropertiesFile();
        if (verbose) outputTextArea.append("\nNew script folder = " +
                                           scriptsFolder.getAbsolutePath());
    }
    
    private void changeJarsFolder() {
        BFileChooser chooser = new BFileChooser(
            BFileChooser.SELECT_FOLDER,
            "Select jars folder",
            new File(System.getProperty("user.dir"))
        );
        chooser.showDialog(this);
        File f =  chooser.getSelectedFile();
        if (f == null) return;
        jarsFolder = f;
        properties.put("jarsFolder", jarsFolder.getPath());
        refreshJarsList();
        refreshPropertiesFile();
        if (verbose) outputTextArea.append("\nNew jars folder = " + jarsFolder.getAbsolutePath());
        jarsContainer.layoutChildren();
    }
    
    private void chooseStartFile() {
        BFileChooser chooser = new BFileChooser(
            BFileChooser.OPEN_FILE,
            "Select start script",
            new File(System.getProperty("user.dir"))
        );
        chooser.showDialog(this);
        File f =  chooser.getSelectedFile();
        if (f == null) return;
        startFile = f;
        properties.put("start", f.getPath());
        refreshPropertiesFile();
        if (verbose) outputTextArea.append("\nNew start file = " + f.getPath());
    }
    
   /**
    * Refresh the jarFiles Set containing all the jar files from ext folder
    * and from jarsFolder.
    */
    private void refreshJarsList() {
        jarFileMap.clear();
        jarsContainer.removeAll();
        File[] ff = jarsFolder.listFiles();
        for (int i = 0 ; i < ff.length ; i++) {
            if(ff[i].getName().toUpperCase().endsWith(".JAR")) {
                addJarFile(ff[i]);
            }
        }
        File f = new File("ext");
        if (!f.exists()) return;
        ff = f.listFiles();
        for (int i = 0 ; i < ff.length ; i++) {
            if(ff[i].getName().toUpperCase().endsWith(".JAR")) {
                addJarFile(ff[i]);
            }
        }
        //importPackages();
    }
    
    private void addJarFile(File jarFile) {
        jarFileMap.put(jarFile.getName(), jarFile.getAbsolutePath());
        BLabel label = new BLabel(jarFile.getName());
        
        BPopupMenu popup = new BPopupMenu();
        BMenuItem item = new BMenuItem("addClassPath(\""+jarFile.getPath().replaceAll("\\\\","\\\\\\\\")+"\");");
        item.addEventLink(CommandEvent.class, this, "insertImport");
        popup.add(item);
        Set imports = new TreeSet();
        try {
            JarFile jar = new JarFile((String)jarFileMap.get(label.getText()));
            Enumeration enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                String name = ((JarEntry)enumeration.nextElement()).getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0,name.length()-6).replaceAll("/",".");
                    int index = Math.max(name.lastIndexOf("."),0);
                    name = name.substring(0, index);
                    imports.add(name);
                }
            }
            Iterator it = imports.iterator();
            while(it.hasNext()) {
                item = new BMenuItem("import " + it.next() + ".*;");
                item.addEventLink(CommandEvent.class, this, "insertImport");
                popup.add(item);
            }
        } catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
        
        label.addEventLink(WidgetMouseEvent.class, popup, "show");
        
        jarsContainer.add(label);
    }
    
    private void insertImport(CommandEvent event){
        try {
            String s = ((BMenuItem)event.getWidget()).getText()+"\n";
            jEditTextArea.getDocument().insertString(jEditTextArea.getCaretPosition(),s,null);
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    
    private void changeVerbose(CommandEvent e) {
        verbose = ((BCheckBoxMenuItem)e.getWidget()).getState();
    }
    
    private void changeAlwaysStartFile(CommandEvent e) {
        alwaysStartFile = ((BCheckBoxMenuItem)e.getWidget()).getState();
    }
    
    private void refreshPropertiesFile() {
        try {
            properties.store(new FileOutputStream("BeanShellEditor.properties"), null);
        }
        catch(Exception e) {
            e.printStackTrace(new PrintWriter(out, true));
        }
    }
    
    private void getBoolean(String question) {
        BDialog dialog = new BDialog(this, question, true);
        RowContainer row = new RowContainer();
            row.setDefaultLayout(new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.HORIZONTAL, new Insets(3,3,3,3), null));
        BButton yes = new BButton("Yes");
        yes.setName("Yes");
            yes.addEventLink(CommandEvent.class, this, "setOverwriting");
        BButton no = new BButton("No");
        no.setName("No");
            no.addEventLink(CommandEvent.class, this, "setOverwriting");
        row.add(yes);
        row.add(no);
        dialog.setContent(row);
        dialog.setBounds(new Rectangle(400,320,200,100));
        dialog.pack();
        dialog.setVisible(true);
    }
    
    private void setOverwriting(CommandEvent event) {
        overwriting = event.getWidget().getName().equals("Yes");
        ((BDialog)event.getWidget().getParent().getParent()).dispose();
    }
    
    private void exit(){
        this.dispose();
    }
    
    private void exitJvm(){System.exit(0);}
    
    private void resize() {
        layoutChildren();
        jarsScrollPane.layoutChildren();
    };
    
    public OutputStream getBshOutputStream(){return out;}
    public StreamHandler getOutputStreamHandler(){return outputStreamHandler;}
    
   /**
    * Get a default logger.
    * <p>A default logger is set up with a special text formatter and accessible
    * from the BeanShellEditor.
    * <p>To test it you may type the following lines in the editor and run it :
    * <ul>
    * <li>log.warning("This is a warning-level log test");</li>
    * <li>log.info("This is a info-level log test");</li>
    * <li>log.fine("This is a fine-level log test");</li>
    * <li>log.finer("This is a finer-level log test");</li>
    */
    public Logger getLog() {return log;}
    
    private class Output extends OutputStream {
        BTextArea textArea;
        
        private Output(BTextArea textArea) {this.textArea = textArea;}
      
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
        }
      
        public void write(byte b[], int off, int len) {
            textArea.append(new String(b, off, len));
        }
        
    }
    
    
}
