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
 * IReportCompiler.java
 *
 * Created on 6 giugno 2003, 0.44
 *
 */

package it.businesslogic.ireport;
import be.savat.components.PagesFrame;
import it.businesslogic.ireport.compiler.ErrorsCollector;
import it.businesslogic.ireport.compiler.ExtendedJRJdtCompiler;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.gui.locale.TimeZoneWrapper;
import it.businesslogic.ireport.gui.logpane.*;
import it.businesslogic.ireport.connection.*;
import it.businesslogic.ireport.gui.queryexecuters.QueryExecuterDef;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.compiler.xml.SourceLocation;
import it.businesslogic.ireport.compiler.xml.SourceTraceDigester;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRJdtCompiler;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.xml.sax.SAXException;

import com.geopista.app.reports.maps.MapImageFactory;

/**
 * Please note that this class is totally different from the old IReportCompiler.class
 * shipped with iReport 1.
 * @author  Administrator
 */
public class IReportCompiler implements Runnable, JRExportProgressMonitor
{

   public static final int CMD_COMPILE  = 0x01;
   public static final int CMD_EXPORT   = 0x02;
   public static final int CMD_COMPILE_SCRIPTLET = 0x04;

   public static final String OUTPUT_DIRECTORY     = "OUTPUT_DIRECTORY";
   public static final String OUTPUT_FORMAT        = "OUTPUT_FORMAT";
   public static final String USE_EMPTY_DATASOURCE = "USE_EMPTY_DATASOURCE";
   public static final String USE_CONNECTION = "USE_CONNECTION";
   public static final String CONNECTION = "CONNECTION";
   public static final String SCRIPTLET_OUTPUT_DIRECTORY = "SCRIPTLET_OUTPUT_DIRECTORY";
   public static final String COMPILER = "COMPILER";
   public static final String EMPTY_DATASOURCE_RECORDS = "EMPTY_DATASOURCE_RECORDS";

   private String constTabTitle = "";
   private javax.swing.JList threadList = null;

   static PrintStream myPrintStream = null;
   int filledpage=0;


   private String status="Starting";
   private IReportConnection iReportConnection;
   private int statusLevel = 0;

   private JReportFrame jrf;

   private int maxBufferSize = 100000;

   private MainFrame mainFrame;

   private int command;

   private HashMap properties;

   private Thread thread;

   private LogTextArea logTextArea = null;

   private String javaFile = "";
   static private StringBuffer outputBuffer = new StringBuffer();

   /**
    * added by Felix Firgau
    */
   private static Vector compileListener = new Vector();

   /** Creates a new instance of IReportCompiler */
   public IReportCompiler()
   {
      properties = new HashMap();
      command = 0;

      try {
        maxBufferSize = Integer.parseInt( System.getProperty("ireport.maxbufsize", "100000"));
      } catch (Exception ex)
      {
          maxBufferSize = 1000000;
      }
   }

   public void stopThread()
   {
       command = 0;
       if (thread != null && thread.isAlive())
       {
           try  {
                thread.interrupt();
           } catch (Exception ex)
           {
               ex.printStackTrace();
           }
       }
       removeThread();

       getLogTextArea().setTitle("Killed" + constTabTitle);
       getLogTextArea().setRemovable(true);
       System.gc();
       System.gc();
   }

   /** When an object implementing interface <code>Runnable</code> is used
    * to create a thread, starting the thread causes the object's
    * <code>run</code> method to be called in that separately executing
    * thread.
    * <p>
    * The general contract of the method <code>run</code> is that it may
    * take any action whatsoever.
    *
    * @see     java.lang.Thread#run()
    *
    */
   public void run()
   {

      if (threadList != null)
        synchronized(threadList)
      {
         javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)threadList.getModel();
         dlm.addElement(this);
         threadList.updateUI();
      }

      PrintStream out = System.out;
      PrintStream err = System.err;
      
      try {
          
      
      SourceTraceDigester digester = null;
      ErrorsCollector errorsCollector = new ErrorsCollector();
      
      File f_report_title = new File(this.getJrf().getReport().getFilename());
      constTabTitle = " [" + f_report_title.getName() + "]";

      String queryLanguage = this.jrf.getReport().getQueryLanguage();



      logTextArea = getMainFrame().getLogPane().createNewLog();
      status  = I18n.getString("iReportCompiler.status.starting", "Starting");

      logTextArea.setTitle(status + constTabTitle);

      String backupJRClasspath = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH);
      // System.getProperty("jasper.reports.compile.class.path");
      String backupSystemClasspath = System.getProperty("java.class.path");

      // Try to look for a good QueryExecutor...
       Vector configuredExecuters = MainFrame.getMainInstance().getQueryExecuters();
       for (int k=0; k<configuredExecuters.size(); ++k)
       {
           QueryExecuterDef qe = (QueryExecuterDef)configuredExecuters.get(k);
           if (qe.getLanguage().equals( queryLanguage ))
           {
               net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory." + qe.getLanguage(), qe.getClassName());
               getLogTextArea().logOnConsole(
                                I18n.getFormattedString("iReportCompiler.settingQueryExecuter", "Setting {0} as Query Executer Factory for language: {1}\n",
                                new Object[]{qe.getClassName(), ""+qe.getLanguage() }));

               break;
           }

       }


      boolean compilation_ok = true;
      long start = System.currentTimeMillis();
      // Redirect output stream....
      
      if (myPrintStream == null)
         myPrintStream  =new PrintStream(new FilteredStream(new ByteArrayOutputStream()));

      if (out != myPrintStream)
         System.setOut(myPrintStream);
      if (err != myPrintStream)
         System.setErr(myPrintStream);

      outputBuffer= new StringBuffer();


	//by Egon - DEBUG: Something is wrong here, please check. ok? thx.
	//1 - Line 148 - srcScriptletFileName = C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxmScriptlet.java -> scriptlet filename
	//2 - Line 157 - Misc.nvl( new File(fileName).getParent(), ".") =>  .  -> report directory

      // Add an entry in the thread list...
    //by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxml
      String fileName = jrf.getReport().getFilename();

	//by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxml
      String srcFileName = jrf.getReport().getFilename();
	//by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jasper
      fileName = Misc.changeFileExtension(fileName,"jasper");


      File f = new File(fileName);
      if (properties.get(this.OUTPUT_DIRECTORY) != null)
      {
		//by Egon - DEBUG: .\FirstJasper.jasper
         fileName = (String)properties.get(this.OUTPUT_DIRECTORY);
         if (!fileName.endsWith(f.separator))
         {
         	fileName += f.separator;
         }
         fileName += f.getName();
      }

	//by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxml
      String scriptletFileName = jrf.getReport().getFilename();
	//by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxml
      String srcScriptletFileName = jrf.getReport().getFilename();
	//by Egon - DEBUG: .\FirstJasper.
      //fileName = Misc.changeFileExtension(fileName,"");
	//by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxmScriptlet.java
      scriptletFileName = srcScriptletFileName.substring(0,scriptletFileName.length()-1)+"Scriptlet.java";
	//1 - by Egon - DEBUG: C:\jasperreports-0.5.3\demo\samples\jasper\FirstJasper.jrxmScriptlet.java
      srcScriptletFileName = scriptletFileName;

      File f2 = new File(scriptletFileName);
      if (properties.get(this.SCRIPTLET_OUTPUT_DIRECTORY) != null)
      {
         scriptletFileName = (String)properties.get(this.SCRIPTLET_OUTPUT_DIRECTORY) + f2.separatorChar + f2.getName();
      }


       String reportDirectory = new File(jrf.getReport().getFilename()).getParent();
       //String classpath = System.getProperty("jasper.reports.compile.class.path");
       String classpath = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH);



       if(classpath != null){
            classpath += File.pathSeparator + reportDirectory;
            //System.setProperty("jasper.reports.compile.class.path", classpath);
            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, classpath);
      } else if (System.getProperty("java.class.path") != null){
        classpath = System.getProperty("java.class.path");
        classpath += File.pathSeparator + reportDirectory;
        System.setProperty("java.class.path", classpath);
        }
       reportDirectory = reportDirectory.replace('\\', '/');
       if(!reportDirectory.endsWith("/")){
            reportDirectory += "/";//the file path separator must be present
       }
       if(!reportDirectory.startsWith("/")){
            reportDirectory = "/" + reportDirectory;//it's important to JVM 1.4.2 especially if contains windows drive letter
       }
       ReportClassLoader reportClassLoader = new ReportClassLoader(mainFrame.getReportClassLoader());
       reportClassLoader.setRelodablePaths( reportDirectory );
       reportClassLoader.rescanLibDirectory();

       /******************/

       try{
            Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[]{new URL("file://"+reportDirectory)},  reportClassLoader));
       } catch (MalformedURLException mue){
            mue.printStackTrace();
       }

       /******************/

       //.setContextClassLoader(reportClassLoader);

       if ((command & CMD_COMPILE_SCRIPTLET) != 0)
      {
         status  = I18n.getString("iReportCompiler.status.compilingScriptlet", "Compiling scriptlet");
         updateThreadList();
         start = System.currentTimeMillis();

         // Compile the scriptlet class...

         //String tempDirStr = System.getProperty("jasper.reports.compile.temp");
         String tempDirStr = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR);

         String oldCompileTemp  = tempDirStr;
         if (tempDirStr == null || tempDirStr.length() == 0 || mainFrame.isUsingCurrentFilesDirectoryForCompiles())
         {
            tempDirStr = mainFrame.getTranslatedCompileDirectory();
         }
         File tempDirFile = new File(tempDirStr);
         javaFile = srcScriptletFileName;
         javaFile = (new File(tempDirFile,javaFile)).getPath();

         javaFile = jrf.getReport().getScriptletFileName();



         if (Misc.getLastWriteTime(javaFile) > Misc.getLastWriteTime(Misc.changeFileExtension(javaFile, "class" )))
         {
         	 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\">" +
                         I18n.getFormattedString("iReportCompiler.compilingScriptlet", "Compiling scriptlet source file... {0}",
                                new Object[]{javaFile }) + "</font>",true);
	         try
	         {
	            //JasperCompileManager.compileReportToFile(srcFileName, fileName);
	            net.sf.jasperreports.engine.design.JRJdk13Compiler compiler = new net.sf.jasperreports.engine.design.JRJdk13Compiler();
	            String errors = compiler.compileClass( new File(javaFile),   Misc.getClassPath() );
	            if (errors != null && errors.length() > 0)
                    {
                        getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                                I18n.getFormattedString("iReportCompiler.errorsCompilingScriptlet", "Errors compiling {0}!",
                                new Object[]{javaFile }) +"</b></font>",true);
                        getLogTextArea().logOnConsole(errors);
                        compilation_ok = false;
                    }
                 }
	         /*
	         catch (Exception jrex)
	         {

	            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>Errors compiling "+scriptletFileName+"!</b></font>",true);
	            //1. load the java file
	            Vector source = new Vector();
	            try
	            {
	               java.io.BufferedReader is = new java.io.BufferedReader(new java.io.FileReader( javaFile ));
	               while (true)
	               {
	                  String line = is.readLine();
	                  if (line == null) break;
	                  source.addElement(line);
	               }
	               is.close();
	            } catch (Exception ioex)
	            {
	               // No file readed....
	            }
	            //2. write exception in a string
	            StringWriter sw = new StringWriter(0);
	            jrex.printStackTrace(new PrintWriter(sw));

	            System.out.println("\n\n\n");
	            myPrintStream.flush();
	            parseException( outputBuffer+sw.getBuffer()+"", source);

	         }
	         */
	         catch (Exception ex)
	         {
	            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                            I18n.getString("iReportCompiler.errorsCompilingScriptletJavaSource", "Error compiling the Scriptlet java source!") +
                            "</b></font>",true);
	            StringWriter sw = new StringWriter(0);
	            ex.printStackTrace(new PrintWriter(sw));
	            myPrintStream.flush();
	            parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
	            compilation_ok = false;
	         }
	         catch (Throwable ext)
	         {
	            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                            I18n.getString("iReportCompiler.errorsCompilingScriptletJavaSource", "Error compiling the Scriptlet java source!") +
                            "</b></font>",true);
	            StringWriter sw = new StringWriter(0);
	            ext.printStackTrace(new PrintWriter(sw));
	            myPrintStream.flush();
	            parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
	            compilation_ok = false;
	         }
	         finally
	         {
	            if(mainFrame.isUsingCurrentFilesDirectoryForCompiles())
	            {
	                /*
	               if( oldCompileTemp != null )
	               {
	                  System.setProperty("jasper.reports.compile.temp", oldCompileTemp);
	               }
	               else
	               {
	                  System.setProperty("jasper.reports.compile.temp", "");
	               }
	                 */
	            }//end if using current files directory for compiles
	         }//end finally
	         getLogTextArea().logOnConsole(outputBuffer.toString());
         	 outputBuffer=new StringBuffer();
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                         I18n.getFormattedString("iReportCompiler.compilationRunningTime", "Compilation running time: {0,number}!",
                                new Object[]{new Long(System.currentTimeMillis() - start)}) + "</b></font><hr>",true);
         }
      }

      if (!compilation_ok) {

          fireCompileListner(this, CL_COMPILE_FAIL, CLS_COMPILE_SCRIPTLET_FAIL);
          removeThread();
          return;
      }

      if ((command & CMD_COMPILE) != 0)
      {
         status  = I18n.getString("iReportCompiler.status.compilingReport", "Compiling report");
         updateThreadList();

         //System.setProperty("jasper.reports.compile.keep.java.file", "true");

         if (mainFrame.getProperties().getProperty("KeepJavaFile","true").equals("false") )
         {
         	net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_KEEP_JAVA_FILE, false);
         }
         else
         {
         	net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_KEEP_JAVA_FILE, true);
         }

         
         //System.setProperty("jasper.reports.compile.temp", "C:\\");
         // Compile report....
         javaFile = this.jrf.getReport().getName()+".java";

         //String tempDirStr = System.getProperty("jasper.reports.compile.temp");
         String tempDirStr = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR);

         String oldCompileTemp  = tempDirStr;
         if (tempDirStr == null || tempDirStr.length() == 0 || mainFrame.isUsingCurrentFilesDirectoryForCompiles())
         {
            tempDirStr = mainFrame.getTranslatedCompileDirectory();
         }
         File tempDirFile = new File(tempDirStr);
         javaFile = (new File(tempDirFile,javaFile)).getPath();

         URL img_url_comp = this.getClass().getResource("/it/businesslogic/ireport/icons/comp1_mini.jpg");

         if (jrf.getReport().getReportChanges() > 0)
        {
             getLogTextArea().logOnConsole(
                    I18n.getString("messages.unsavedChanges", "The report still has unsaved changes"),
                JOptionPane.WARNING_MESSAGE);
        }  
         
         getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url_comp  +"\"> &nbsp;" +
                         I18n.getFormattedString("iReportCompiler.compilingToFile", "Compiling to file... {0} -> {1}",
                                new Object[]{fileName, javaFile}) + "</font>",true);

         //String old_jr_classpath = Misc.nvl( System.getProperty("jasper.reports.compile.class.path"), "");
         //String old_defaul_compiler = Misc.nvl( System.getProperty("jasper.reports.compiler.class"), "");
         String old_jr_classpath = Misc.nvl(net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH), "");
         String old_defaul_compiler = Misc.nvl(net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS), "");

         try
         {
            if( mainFrame.isUsingCurrentFilesDirectoryForCompiles() )
            {
               //System.setProperty("jasper.reports.compile.temp", tempDirStr);
               net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR, tempDirStr);

            }

            //System.setProperty("jasper.reports.compile.class.path", Misc.nvl( new File(fileName).getParent(), ".")  + File.pathSeparator  + Misc.getClassPath());
            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, Misc.nvl( new File(fileName).getParent(), ".")  + File.pathSeparator  + Misc.getClassPath());

            //System.out.println("CP:" + System.getProperty("jasper.reports.compile.class.path"));
            //System.out.println("\nOLDCP:" +old_jr_classpath);

            String compiler_name  = I18n.getString("iReportCompiler.defaultCompiler", "JasperReports default compiler");
            String compiler_code = mainFrame.getProperties().getProperty("DefaultCompiler");

            JRJdtCompiler jdtCompiler = null; 
            if (this.getProperties().get(COMPILER) != null)
            {
                //System.setProperty("jasper.reports.compiler.class", ""+this.getProperties().get(COMPILER) );
                net.sf.jasperreports.engine.util.JRProperties. setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, ""+this.getProperties().get(COMPILER));
                compiler_name = I18n.getFormattedString("iReportCompiler.specialLanguageCompiler", "Special language compiler ({0})", new Object[]{this.getProperties().get(COMPILER)});
            }
            else if (compiler_code !=  null && !compiler_code.equals("0") && !compiler_code.equals(""))
            {
                if (compiler_code.equals("1"))
                {
                    //System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJdk13Compiler"  );
                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "net.sf.jasperreports.engine.design.JRJdk13Compiler");
                    compiler_name = I18n.getString("iReportCompiler.javaCompiler", "Java Compiler");
                }
                else if (compiler_code.equals("2"))
                {
                    //System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJdtCompiler"  );
                    
                    //net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "net.sf.jasperreports.engine.design.JRJdtCompiler" ); //"net.sf.jasperreports.engine.design.JRJdtCompiler"
                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "it.businesslogic.ireport.compiler.ExtendedJRJdtCompiler" );
                    compiler_name = I18n.getString("iReportCompiler.jdtCompiler", "JDT Compiler");
                    //Thread.currentThread().setContextClassLoader( reportClassLoader );
                    //ClassLoader cl = getClassLoader();
                    //System.out.println(  Thread.getC );
                
                    jdtCompiler = new ExtendedJRJdtCompiler();
                }
                else if (compiler_code.equals("3"))
                {
                    //System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRBshCompiler"  );
                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "net.sf.jasperreports.compilers.JRBshCompiler" );
                    compiler_name = I18n.getString("iReportCompiler.beanShellCompiler", "BeanShell Compiler");
                }
                else if (compiler_code.equals("4"))
                {
                    //System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJikesCompiler"  );
                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "net.sf.jasperreports.engine.design.JRJikesCompiler" );
                    compiler_name = I18n.getString("iReportCompiler.jikesCompiler", "Jikes Compiler");
                }
            }
            else
            {
                 //System.setProperty("jasper.reports.compiler.class","" );
                 //net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "" );
                
                 //Force to use the iReport compiler....
                 net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, "it.businesslogic.ireport.compiler.ExtendedJRJdtCompiler" );
                 
                 //Thread.currentThread().setContextClassLoader( reportClassLoader );
                 //ClassLoader cl = getClassLoader();
                 //System.out.println(  cl );
                 jdtCompiler = new ExtendedJRJdtCompiler();
            }


           // getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#000000\"><b>Using compiler "+ compiler_name  + " (" + System.getProperty("jasper.reports.compiler.class","" ) +")</b></font>",true);
            start = System.currentTimeMillis();
            
            digester = IReportCompiler.createDigester();
            JasperDesign jd = IReportCompiler.loadJasperDesign( new FileInputStream(srcFileName) , digester);
            
            //Modificacion para geopista
            // MapImageManager.init(jd);
            //Fin de modificacion
            
            if (jdtCompiler != null)
            {
                ((ExtendedJRJdtCompiler)jdtCompiler).setDigester(digester);
                ((ExtendedJRJdtCompiler)jdtCompiler).setErrorHandler(errorsCollector);
                
                JasperReport finalJR = jdtCompiler.compileReport( jd  );
                
                if (errorsCollector.getProblemItems().size() > 0 || finalJR == null)
                {
                   
                    throw new JRException("");
                }
                JRSaver.saveObject(finalJR,  fileName);
                
                //System.out.println("Report saved..." + finalJR + " " + errorsCollector.getProblemItems().size());
            }
            else
            {
                JasperCompileManager.compileReportToFile(jd, fileName);
            }
            
            if (errorsCollector != null)
            {
                    getJrf().setReportProblems( errorsCollector.getProblemItems() );
                    MainFrame.getMainInstance().getLogPane().getProblemsPanel().updateProblemsList();
            }
            
         }
         catch (JRValidationException e)
         {
             compilation_ok = false;
             
                for (Iterator it = e.getFaults().iterator(); it.hasNext();)
		{
			JRValidationFault fault = (JRValidationFault) it.next();
			Object source = fault.getSource();
                        SourceLocation sl = digester.getLocation( source );
                        if (sl == null)
                        {
                            errorsCollector.getProblemItems().add(new ProblemItem(ProblemItem.WARNING, fault.getMessage(), sl, null) );
                        }   
                        else
                        {
                            errorsCollector.getProblemItems().add(new ProblemItem(ProblemItem.WARNING, fault.getMessage(), sl, sl.getXPath()) );
                            System.out.println(fault + " " + fault.getMessage() + "\nLine: " + sl.getLineNumber() + ", Column: " + sl.getColumnNumber()  + " JRXML Element: " + sl.getXPath() );
                        }
                        
                        //
                }
                getJrf().setReportProblems( errorsCollector.getProblemItems() );
                MainFrame.getMainInstance().getLogPane().getProblemsPanel().updateProblemsList();
                
                StringWriter sw = new StringWriter(0);
                e.printStackTrace(new PrintWriter(sw));
                System.out.println("\n\n\n");
                myPrintStream.flush();
                parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
                
                
            
         } catch (JRException jrex)
         {
             if (errorsCollector != null && errorsCollector.getProblemItems() != null)
             {
                getJrf().setReportProblems( errorsCollector.getProblemItems() );
                MainFrame.getMainInstance().getLogPane().getProblemsPanel().updateProblemsList();
             }
             
             compilation_ok = false;
             getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                         I18n.getFormattedString("iReportCompiler.errorsCompiling", "Errors compiling {0}!",
                                new Object[]{fileName}) + "</b></font>",true);
                         
                                /*
            //1. load the java file
            Vector source = new Vector();
            try
            {
               java.io.BufferedReader is = new java.io.BufferedReader(new java.io.FileReader( javaFile ));
               while (true)
               {
                  String line = is.readLine();
                  if (line == null) break;
                  source.addElement(line);
               }
               is.close();

            } catch (Exception ioex)
            {
               // No file readed....
            }
            //2. write exception in a string
                                 
                                 */
            StringWriter sw = new StringWriter(0);
            jrex.printStackTrace(new PrintWriter(sw));

            System.out.println("\n\n\n");
            myPrintStream.flush();
            parseException( outputBuffer.toString()+sw.getBuffer()+"", null);

         }
         catch (Exception ex)
         {
            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                         I18n.getString("iReportCompiler.errorsCompilingReportJavaSource", "Error compiling the report java source!") + "</b></font>",true);
            StringWriter sw = new StringWriter(0);
            ex.printStackTrace(new PrintWriter(sw));
            myPrintStream.flush();
            parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
            compilation_ok = false;
         }
         catch (Throwable ext)
         {
            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#CC0000\"><b>" +
                         I18n.getString("iReportCompiler.errorsCompilingReportJavaSource", "Error compiling the report java source!") + "</b></font>",true);
            StringWriter sw = new StringWriter(0);
            ext.printStackTrace(new PrintWriter(sw));
            myPrintStream.flush();
            parseException( outputBuffer.toString()+sw.getBuffer()+"", null);
            compilation_ok = false;
         }
         finally
         {
            //System.setProperty("jasper.reports.compile.class.path", old_jr_classpath);
            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, old_jr_classpath );
            //System.setProperty("jasper.reports.compiler.class", old_defaul_compiler);
            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS, old_defaul_compiler );

            if(mainFrame.isUsingCurrentFilesDirectoryForCompiles())
            {
               if( oldCompileTemp != null )
               {
                  System.setProperty("jasper.reports.compile.temp", oldCompileTemp);
                  net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR, oldCompileTemp );
               }
               else
               {
                  System.setProperty("jasper.reports.compile.temp", "");
                  net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_TEMP_DIR, "" );
               }

               File javaSrcFile = new File(javaFile);
               if (  javaSrcFile.exists() )
               if (mainFrame.getProperties().getProperty("KeepJavaFile","true").equals("false") )
               {
                    javaSrcFile.delete();
               }
            }//end if using current files directory for compiles
         }//end finally
         getLogTextArea().logOnConsole(outputBuffer.toString());
         outputBuffer=new StringBuffer();
         getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                         I18n.getFormattedString("iReportCompiler.compilationRunningTime", "Compilation running time: {0,number}!",
                                new Object[]{new Long(System.currentTimeMillis() - start)}) + "</b></font><hr>",true);
                                
         if (errorsCollector != null && errorsCollector.getProblemItems().size() > 0)
         {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                   MainFrame.getMainInstance().getLogPane().setActiveLogComponent( MainFrame.getMainInstance().getLogPane().getProblemsPanel() );
                            }
                        });
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
         }
      }


      if (!compilation_ok) {

          fireCompileListner(this, CL_COMPILE_FAIL, CLS_COMPILE_SOURCE_FAIL);
          removeThread();
          return;
      }

      if  ((command & CMD_EXPORT) != 0)
      {

         status  = I18n.getString("iReportCompiler.status.generatingReport", "Generating report");
         updateThreadList();

         // Compile report....
         JasperPrint print = null;
         URL img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/rundb1_mini.jpg");

         getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                 I18n.getString("iReportCompiler.fillingReport","Filling report...")+ "</font>",true);



         statusLevel = 5;
         Map hm = it.businesslogic.ireport.gui.prompt.Prompter.promptForParameters(this.getJrf().getReport());

         hm.put("REPORT_LOCALE",  Misc.getLocaleFromString(mainFrame.getProperties().getProperty("reportLocale")));

         img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/world.png");
         getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;"+
                 I18n.getFormattedString("iReportCompiler.locale","Locale: <b>{0}</b>",
                 new Object[]{Misc.getLocaleFromString(mainFrame.getProperties().getProperty("reportLocale")).getDisplayName()}) + "</font>",true);

         String reportTimeZoneId = mainFrame.getProperties().getProperty("reportTimeZoneId");
         String timeZoneName = I18n.getString("timezone.default","Default");
         if (reportTimeZoneId != null && reportTimeZoneId.length() > 0 )
         {
            java.util.TimeZone tz = java.util.TimeZone.getTimeZone(reportTimeZoneId);
            hm.put("REPORT_TIME_ZONE", tz );
            timeZoneName = new TimeZoneWrapper( tz ) + "";
         }

         img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/timezone.png");
         getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                 I18n.getFormattedString("iReportCompiler.timeZone","Time zone: <b>{0}</b>",
                 new Object[]{timeZoneName}) + "</font>",true);



         int reportMaxCount = 0;
         try {
            reportMaxCount = Integer.parseInt(mainFrame.getProperties().getProperty("maxRecords","0"));
         } catch (Exception ex) {}

         if (reportMaxCount > 0)
         {
             img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/file-info.png");
             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                 I18n.getFormattedString("iReportCompiler.maxRecords","Max number of records: <b>{0,number}</b>",
                 new Object[]{new Integer(reportMaxCount)}) + "</font>",true);

             hm.put("REPORT_MAX_COUNT",  new Integer(reportMaxCount) );
         }


        // Thread.currentThread().setContextClassLoader( reportClassLoader );



         if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().isIgnorePagination())
         {
             hm.put("IS_IGNORE_PAGINATION",  Boolean.TRUE );
             img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/file-info.png");
             getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" +
                     I18n.getString("iReportCompiler.ignoringPagination","Ignoring pagination") + "</font>",true);

         }
         if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().isUseReportVirtualizer())
         {
             try {


                 net.sf.jasperreports.engine.JRVirtualizer virtualizer = null;
                 String rvName = mainFrame.getProperties().getProperty("ReportVirtualizer", "JRFileVirtualizer");
                 String vrTmpDirectory = mainFrame.getProperties().getProperty("ReportVirtualizerDirectory", mainFrame.getTranslatedCompileDirectory() );
                 int vrSize = Integer.parseInt( mainFrame.getProperties().getProperty("ReportVirtualizerSize","100"));

                 String msg = "";

                 if (rvName.equals("JRGzipVirtualizer"))
                 {
                     msg = I18n.getFormattedString("iReportCompiler.JRGzipVirtualizer",
                                                   "JRGzipVirtualizer Size: {0,number}<br>",
                                                   new Object[]{new Integer(vrSize)});
                     virtualizer = new net.sf.jasperreports.engine.fill.JRGzipVirtualizer(vrSize);
                 }
                 else if (rvName.equals("JRSwapFileVirtualizer"))
                 {
                     msg = I18n.getFormattedString("iReportCompiler.JRSwapFileVirtualizer",
                                                   "JRSwapFileVirtualizer Size: {0,number} Swap directory: {1};<br>" +
                                                   "  ReportVirtualizerBlockSize: {2}<br>ReportVirtualizerGrownCount: {3}<br>",
                                                   new Object[]{new Integer(vrSize), vrTmpDirectory,
                                                                mainFrame.getProperties().getProperty("ReportVirtualizerBlockSize","100"),
                                                                mainFrame.getProperties().getProperty("ReportVirtualizerGrownCount","100")});

                     //msg = " JRSwapFileVirtualizer " + " Size: " + vrSize +" Swap directory: " + vrTmpDirectory +";<br>";
                     //msg += " ReportVirtualizerBlockSize: " + mainFrame.getProperties().getProperty("ReportVirtualizerBlockSize","100")+"<br>";
                     //msg += " ReportVirtualizerGrownCount: " + mainFrame.getProperties().getProperty("ReportVirtualizerGrownCount","100")+"<br>";

                     JRSwapFile swapFile = new JRSwapFile(vrTmpDirectory,
                             Integer.parseInt( mainFrame.getProperties().getProperty("ReportVirtualizerBlockSize","100")),
                             Integer.parseInt( mainFrame.getProperties().getProperty("ReportVirtualizerGrownCount","100")));
                     virtualizer = new net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer(vrSize,swapFile);
                 }
                 else // default if (rvName.equals("JRFileVirtualizer"))
                 {
                     msg = I18n.getFormattedString("iReportCompiler.JRFileVirtualizer",
                                                   "JRFileVirtualizer Size: {0,number} Swap directory: {1};<br>",
                                                   new Object[]{new Integer(vrSize),vrTmpDirectory});
                    virtualizer = new net.sf.jasperreports.engine.fill.JRFileVirtualizer(vrSize, vrTmpDirectory );
                 }

                 img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/file-info.png");
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#000000\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;"
                         + I18n.getString("iReportCompiler.usingVirtualizer", "Using report virtualizer... ") +  msg+ "</font>",true);

                 hm.put("REPORT_VIRTUALIZER", virtualizer );

             } catch (Throwable ex)
             {
                 getLogTextArea().logOnConsole("<font face=\"SansSerif\" size=\"3\" color=\"#660000\">" +
                         I18n.getString("iReportCompiler.virtualizerNotAvailable", "WARNING: Report virtualizer not available.") +
                         "</font>",true);

             }
         }

         start = System.currentTimeMillis();

         if (properties.get(USE_EMPTY_DATASOURCE) != null && properties.get(USE_EMPTY_DATASOURCE).equals("true"))
         {
            try
            {
               int records = 1;
               try {

                   records = ((Integer)properties.get(EMPTY_DATASOURCE_RECORDS)).intValue();
               } catch (Exception ex)
               {
                   records = 1;
               }

               print = JasperFillManager.fillReport(fileName,hm,new JREmptyDataSource(records));


            }
            catch (OutOfMemoryError ex)
            {
                getLogTextArea().logOnConsole(
                        I18n.getString("iReportCompiler.outOfMemory", "Out of memory exception!\n")
                        );
            }
            catch (Exception ex)
            {
               getLogTextArea().logOnConsole(
                       I18n.getFormattedString("iReportCompiler.errorFillingPrint",
                                                   "Error filling print... {0}\n",
                                                   new Object[]{ex.getMessage()}));

               ex.printStackTrace();
               getLogTextArea().logOnConsole(outputBuffer.toString());
               outputBuffer = new StringBuffer();
            }
         }
         else if (properties.get(USE_CONNECTION) != null && properties.get(USE_CONNECTION).equals("true"))
         {
            IReportConnection connection = (IReportConnection)properties.get(CONNECTION);

            try
            {

               hm = connection.getSpecialParameters( hm );

               //changed by Felix Firgau for subclassing
               hm = MainFrame.getMainInstance().getSpecialParameters(hm, this.jrf.getReport());


               if (connection.isJDBCConnection())
               {
                  print = JasperFillManager.fillReport(fileName,hm, connection.getConnection());
               }
               else if (connection.isJRDataSource())
               {
                   JRDataSource ds = null;
                   if (connection instanceof JRDataSourceProviderConnection)
                   {

                        JasperReport jasper_report_obj = JasperManager.loadReport(fileName);
                        ds = (JRDataSource)((JRDataSourceProviderConnection)connection).getJRDataSource(jasper_report_obj);

                        if (ds == null) return;
                        print = JasperFillManager.fillReport(jasper_report_obj,hm,ds);

                        try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {

                            getLogTextArea().logOnConsole(
                                  I18n.getFormattedString("iReportCompiler.errorClosingDatasource",
                                                   "Error closing datasource: {0}\n",
                                                   new Object[]{ex.getMessage()}) );

                        }
                   }
                   else
                   {
                       ds = (JRDataSource)connection.getJRDataSource();
                       print = JasperFillManager.fillReport(fileName,hm,ds);
                   }
               }
               else
               {
                   if (connection instanceof JRHibernateConnection)
                   {
                       Session session = null;
                       Transaction transaction = null;
                       System.out.println();
                       getLogTextArea().logOnConsole(
                                  I18n.getString("iReportCompiler.HibernateSessionOpened",
                                                   "Hibernate session opened") );

                       try {
                            session = ((JRHibernateConnection)connection).createSession();
                            transaction = session.beginTransaction();
                            hm.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
                            print = JasperFillManager.fillReport(fileName,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                            if (transaction != null) try {  transaction.rollback(); } catch (Exception ex) { }
                            if (transaction != null) try {  session.close(); } catch (Exception ex) { }
                       }
                   }
                   else if (connection instanceof EJBQLConnection)
                   {
                       EntityManager em = null;
                       try {

                           getLogTextArea().logOnConsole(
                                  I18n.getString("iReportCompiler.CreatingEntityManager",
                                                   "Creating entity manager") );

                            em = ((EJBQLConnection)connection).getEntityManager();
                            hm.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
                            //Thread.currentThread().setContextClassLoader( reportClassLoader );
                            print = JasperFillManager.fillReport(fileName,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                           getLogTextArea().logOnConsole(
                                  I18n.getString("iReportCompiler.ClosingEntityManager",
                                                   "Closing entity manager") );
                            ((EJBQLConnection)connection).closeEntityManager();
                       }
                   }
                   else if (connection instanceof MondrianConnection)
                   {
                       mondrian.olap.Connection mCon = null;
                       try {
                           getLogTextArea().logOnConsole(
                                  I18n.getString("iReportCompiler.OpeningMondrianConnection",
                                                   "Opening Mondrian connection") );
                            mCon = ((MondrianConnection)connection).getMondrianConnection();
                            hm.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, mCon);
                            //Thread.currentThread().setContextClassLoader( reportClassLoader );
                            print = JasperFillManager.fillReport(fileName,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                           getLogTextArea().logOnConsole(
                                  I18n.getString("iReportCompiler.ClosingMondrianConnection",
                                                   "Closing Mondrian connection") );
                            ((MondrianConnection)connection).closeMondrianConnection();
                       }
                   }
                   else // Query Executor mode...
                   {
                       //Thread.currentThread().setContextClassLoader( reportClassLoader );
                       print = JasperFillManager.fillReport(fileName,hm);
                   }
               }

            } catch (Exception ex)
            {
               getLogTextArea().logOnConsole(
                       I18n.getFormattedString("iReportCompiler.errorFillingPrint",
                                                   "Error filling print... {0}\n",
                                                   new Object[]{ex.getMessage()}));
               ex.printStackTrace();
               getLogTextArea().logOnConsole(outputBuffer.toString());
               outputBuffer = new StringBuffer();
            }
            catch (Throwable ext)
            {
                getLogTextArea().logOnConsole(
                       I18n.getFormattedString("iReportCompiler.errorFillingPrint",
                                                   "Error filling print... {0}\n",
                                                   new Object[]{ext + " " + ext.getCause()}));
               ext.printStackTrace();
               getLogTextArea().logOnConsole(outputBuffer.toString());
               outputBuffer = new StringBuffer();
            }
            finally
            {
                connection.disposeSpecialParameters(hm);
                if (connection != null && connection instanceof JRDataSourceProviderConnection)
                {
                        try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {
                            getLogTextArea().logOnConsole(
                                  I18n.getFormattedString("iReportCompiler.errorClosingDatasource",
                                                   "Error closing datasource: {0}\n",
                                                   new Object[]{ex.getMessage()}) );
                        }

                }

            }
         }
         net.sf.jasperreports.view.JRViewer jrv = null;
         net.sf.jasperreports.engine.JRExporter exporter=null;

         getLogTextArea().logOnConsole(outputBuffer.toString());
         outputBuffer = new StringBuffer();

         if (print != null)
         {
            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\">" +
                         I18n.getFormattedString("iReportCompiler.fillingRunningTime", "<b>Report fill running time: {0,number}!</b> (pages generated: {1,number})",
                                new Object[]{new Long(System.currentTimeMillis() - start), new Integer(((List)print.getPages()).size())}) + "</font><hr>",true);

            status  = I18n.getString("iReportCompiler.status.exportingReport", "Exporting report");
            updateThreadList();

            start = System.currentTimeMillis();
            String  format = Misc.nvl(properties.get(OUTPUT_FORMAT),"pdf");
            String  viewer_program = "";

            //getLogTextArea().logOnConsole(properties.get(OUTPUT_FORMAT) + "Exporting\n");
            getLogTextArea().logOnConsole(outputBuffer.toString());
            outputBuffer = new StringBuffer();

            String exportingMessage = "";

            try
            {

               if (format.equalsIgnoreCase("pdf"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.JRPdfExporter();

                  if (this.getMainFrame().getProperties().getProperty("PDF_IS_ENCRYPTED") != null)
                  {
                      exporter.setParameter( JRPdfExporterParameter.IS_ENCRYPTED, new Boolean( this.getMainFrame().getProperties().getProperty("PDF_IS_ENCRYPTED") ) );
                  }
                  if (this.getMainFrame().getProperties().getProperty("PDF_IS_128_BIT_KEY") != null)
                  {
                      exporter.setParameter( JRPdfExporterParameter.IS_128_BIT_KEY, new Boolean( this.getMainFrame().getProperties().getProperty("PDF_IS_128_BIT_KEY") ) );
                  }
                  if (this.getMainFrame().getProperties().getProperty("PDF_USER_PASSWORD") != null)
                  {
                      exporter.setParameter( JRPdfExporterParameter.USER_PASSWORD, this.getMainFrame().getProperties().getProperty("PDF_USER_PASSWORD"));
                  }
                  if (this.getMainFrame().getProperties().getProperty("PDF_OWNER_PASSWORD") != null)
                  {
                      exporter.setParameter( JRPdfExporterParameter.OWNER_PASSWORD, this.getMainFrame().getProperties().getProperty("PDF_OWNER_PASSWORD"));
                  }
                  if (this.getMainFrame().getProperties().getProperty("PDF_PERMISSIONS") != null)
                  {
                      exporter.setParameter( JRPdfExporterParameter.PERMISSIONS, new Integer( this.getMainFrame().getProperties().getProperty("PDF_PERMISSIONS")));
                  }

                  fileName = Misc.changeFileExtension(fileName,"pdf");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.pdf", "Exporting pdf to file (using iText)...  {0}!",  new Object[]{fileName});
                  viewer_program = mainFrame.getProperties().getProperty("ExternalPDFViewer");
               }
               else if (format.equalsIgnoreCase("csv"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.JRCsvExporter();

                  if (this.getMainFrame().getProperties().getProperty("CSV_FIELD_DELIMITER") != null)
                  {
                      exporter.setParameter( JRCsvExporterParameter.FIELD_DELIMITER, this.getMainFrame().getProperties().getProperty("CSV_FIELD_DELIMITER") );
                  }

                  fileName = Misc.changeFileExtension(fileName,"csv");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.csv", "Exporting CSV to file... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalCSVViewer"), "");
               }
               else if (format.equalsIgnoreCase("html"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.JRHtmlExporter();

                  if (this.getMainFrame().getProperties().getProperty("HTML_IMAGES_DIR_NAME") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IMAGES_DIR_NAME, this.getMainFrame().getProperties().getProperty("HTML_IMAGES_DIR_NAME") ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_IS_OUTPUT_IMAGES_TO_DIR") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, new Boolean( this.getMainFrame().getProperties().getProperty("HTML_IS_OUTPUT_IMAGES_TO_DIR")) ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_IMAGES_URI") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IMAGES_URI, this.getMainFrame().getProperties().getProperty("HTML_IMAGES_URI") ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_HTML_HEADER") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.HTML_HEADER, this.getMainFrame().getProperties().getProperty("HTML_HTML_HEADER") ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_BETWEEN_PAGES_HTML") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.BETWEEN_PAGES_HTML, this.getMainFrame().getProperties().getProperty("HTML_BETWEEN_PAGES_HTML") ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_HTML_FOOTER") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.HTML_FOOTER, this.getMainFrame().getProperties().getProperty("HTML_HTML_FOOTER") ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(this.getMainFrame().getProperties().getProperty("HTML_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS"))); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_IS_WHITE_PAGE_BACKGROUND") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, new Boolean(this.getMainFrame().getProperties().getProperty("HTML_IS_WHITE_PAGE_BACKGROUND")) ); }
                  if (this.getMainFrame().getProperties().getProperty("HTML_IS_USING_IMAGES_TO_ALIGN") != null)
                  { exporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, new Boolean(this.getMainFrame().getProperties().getProperty("HTML_IS_USING_IMAGES_TO_ALIGN")) ); }

                  fileName = Misc.changeFileExtension(fileName,"html");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.html", "Exporting HTML to file... {0}!",  new Object[]{fileName});

                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalHTMLViewer"), "");
               }
               else if (format.equalsIgnoreCase("xls"))
               {

                  exporter = new  net.sf.jasperreports.engine.export.JRXlsExporter();

                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, new Boolean( this.getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET")) ); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS"))); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND")) ); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_DETECT_CELL_TYPE") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_DETECT_CELL_TYPE, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_DETECT_CELL_TYPE")) ); }


                  fileName = Misc.changeFileExtension(fileName,"xls");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.xls", "Exporting xls to file (using POI)... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalXLSViewer"), "");

               }
               else if (format.equalsIgnoreCase("xls2"))
               {

                  exporter = new  net.sf.jasperreports.engine.export.JExcelApiExporter();

                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, new Boolean( this.getMainFrame().getProperties().getProperty("XLS_IS_ONE_PAGE_PER_SHEET")) ); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS"))); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_WHITE_PAGE_BACKGROUND")) ); }
                  if (this.getMainFrame().getProperties().getProperty("XLS_IS_DETECT_CELL_TYPE") != null)
                  { exporter.setParameter( JRXlsExporterParameter.IS_DETECT_CELL_TYPE, new Boolean(this.getMainFrame().getProperties().getProperty("XLS_IS_DETECT_CELL_TYPE")) ); }


                  if (this.getMainFrame().getProperties().getProperty("XLS2_IS_FONT_SIZE_FIX_ENABLED") != null)
                  { exporter.setParameter( JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, new Boolean( this.getMainFrame().getProperties().getProperty("XLS2_IS_FONT_SIZE_FIX_ENABLED")) ); }

                  fileName = Misc.changeFileExtension(fileName,"xls");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.xls2", "Exporting xls to file (using JExcelApi)... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalXLSViewer"), "");

               }
               else if (format.equalsIgnoreCase("java2D"))
               {

                  //exporter = new  net.sf.jasperreports.engine.export.JRGraphics2DExporter();
                  //exportingMessage = " ";
                  exportingMessage = I18n.getString("iReportCompiler.exportingMessage.java2D", "Exporting to Java2D...");
                  viewer_program = null;
               }
               else if (format.equalsIgnoreCase("jrviewer"))
               {
                  exportingMessage = I18n.getString("iReportCompiler.exportingMessage.jrviewer", "Viewing with JasperReports Viewer");
                  exporter = null;
                  viewer_program = null;
               }
               else if (format.equalsIgnoreCase("txt"))
               {
                  exporter = new  it.businesslogic.ireport.export.JRTxtExporter();

                  if (this.getMainFrame().getProperties().getProperty("TXT_PAGE_ROWS") != null)
                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.PAGE_ROWS, this.getMainFrame().getProperties().getProperty("TXT_PAGE_ROWS") ); }
                  if (this.getMainFrame().getProperties().getProperty("TXT_PAGE_COLUMNS") != null)
                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.PAGE_COLUMNS, this.getMainFrame().getProperties().getProperty("TXT_PAGE_COLUMNS") ); }
                  if (this.getMainFrame().getProperties().getProperty("TXT_ADD_FORM_FEED") != null)
                  { exporter.setParameter( it.businesslogic.ireport.export.JRTxtExporterParameter.ADD_FORM_FEED, new Boolean(this.getMainFrame().getProperties().getProperty("TXT_ADD_FORM_FEED"))); }

                  fileName = Misc.changeFileExtension(fileName,"txt");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.txt", "Exporting txt (iReport) to file... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalTXTViewer"), "");
               }
               else if (format.equalsIgnoreCase("txtjr"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.JRTextExporter();

                  if (this.getMainFrame().getProperties().getProperty("JRTXT_PAGE_WIDTH") != null)
                  { exporter.setParameter( net.sf.jasperreports.engine.export.JRTextExporterParameter.PAGE_WIDTH, new Integer( this.getMainFrame().getProperties().getProperty("JRTXT_PAGE_WIDTH")) ); }
                  if (this.getMainFrame().getProperties().getProperty("JRTXT_PAGE_HEIGHT") != null)
                  { exporter.setParameter( net.sf.jasperreports.engine.export.JRTextExporterParameter.PAGE_HEIGHT, new Integer( this.getMainFrame().getProperties().getProperty("JRTXT_PAGE_HEIGHT")) ); }
                  if (this.getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_WIDTH") != null)
                  { exporter.setParameter( net.sf.jasperreports.engine.export.JRTextExporterParameter.CHARACTER_WIDTH, new Integer( this.getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_WIDTH")) ); }
                  if (this.getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_HEIGHT") != null)
                  { exporter.setParameter( net.sf.jasperreports.engine.export.JRTextExporterParameter.CHARACTER_HEIGHT, new Integer( this.getMainFrame().getProperties().getProperty("JRTXT_CHARACTER_HEIGHT")) ); }
                  if (this.getMainFrame().getProperties().getProperty("JRTXT_BETWEEN_PAGES_TEXT") != null)
                  { exporter.setParameter( net.sf.jasperreports.engine.export.JRTextExporterParameter.BETWEEN_PAGES_TEXT, this.getMainFrame().getProperties().getProperty("JRTXT_BETWEEN_PAGES_TEXT") ); }

                  fileName = Misc.changeFileExtension(fileName,"txt");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.txtjr", "Exporting txt (jasperReports) to file... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalTXTViewer"), "");
               }
               else if (format.equalsIgnoreCase("rtf"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.JRRtfExporter();

                  fileName = Misc.changeFileExtension(fileName,"rtf");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.rtf", "Exporting RTF to file... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalRTFViewer"), "");
               }
               else if (format.equalsIgnoreCase("odf"))
               {
                  exporter = new  net.sf.jasperreports.engine.export.oasis.JROdtExporter();

                  fileName = Misc.changeFileExtension(fileName,"odf");
                  exportingMessage = I18n.getFormattedString("iReportCompiler.exportingMessage.odf", "Exporting OpenOffice documento to file... {0}!",  new Object[]{fileName});
                  viewer_program = Misc.nvl( mainFrame.getProperties().getProperty("ExternalODFViewer"), "");
               }

               img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/printer_mini.png");

               getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\"><img align=\"right\" src=\""+  img_url  +"\"> &nbsp;" + exportingMessage + "</font>",true);


               if (exporter != null)
               {
                  exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,fileName);
                  exporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
                  exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, this);
                  
                  String reportEncoding = Misc.nvl( mainFrame.getProperties().getProperty("CHARACTER_ENCODING"),"");
                  if (reportEncoding.trim().length() > 0)
                  {
                      exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, reportEncoding);
                  }
                  
                  String offsetX = Misc.nvl( mainFrame.getProperties().getProperty("OFFSET_X"),"");
                  if (offsetX.trim().length() > 0)
                  {
                      try {
                          exporter.setParameter(JRExporterParameter.OFFSET_X, new Integer(offsetX));
                      } catch (Exception ex) {}
                  }
                  
                  String offsetY = Misc.nvl( mainFrame.getProperties().getProperty("OFFSET_Y"),"");
                  if (offsetY.trim().length() > 0)
                  {
                      try {
                          exporter.setParameter(JRExporterParameter.OFFSET_Y,new Integer( offsetY));
                      } catch (Exception ex) {}
                  }
                  
                  
                  exporter.exportReport();
                  getLogTextArea().logOnConsole(outputBuffer.toString());
            	  outputBuffer = new StringBuffer();
               }
               else if (format.equalsIgnoreCase("java2D") )
               {
                   if (print.getPages().size() == 0)
                  {
                      try {
                      SwingUtilities.invokeLater(new Runnable() {
                          public void run() {
                                  JOptionPane.showMessageDialog(MainFrame.getMainInstance(), I18n.getString("documentHasNoPages","The document has no pages"));
                          }
                      });
                      } catch (Exception ex){}
                  }
                  else
                  {
                    PagesFrame pd = new PagesFrame(print);
                    pd.setVisible(true);
                  }
               }
               else if (format.equalsIgnoreCase("jrviewer"))
               {
                  //jrv = new net.sf.jasperreports.view.JRViewer(print);
                  if (print.getPages().size() == 0)
                  {
                      try {
                      SwingUtilities.invokeLater(new Runnable() {
                          public void run() {
                                  JOptionPane.showMessageDialog(MainFrame.getMainInstance(), I18n.getString("documentHasNoPages","The document has no pages"));
                          }
                      });
                      } catch (Exception ex){}
                  }
                  else
                  {
                      JasperViewer jasperViewer = new JasperViewer(print,false);
                      jasperViewer.setTitle("iReport JasperViewer");
                      jasperViewer.setVisible(true);
                      
                  }
                  //net.sf.jasperreports.view.JasperViewer.viewReport( print, false);
               }
            } catch (Throwable ex2)
            {

               getLogTextArea().logOnConsole(
                       I18n.getFormattedString("iReportCompiler.errorExportingPrint",
                                                   "Error exporting print... {0}\n",
                                                   new Object[]{ex2.getMessage()}));
               ex2.printStackTrace();
               getLogTextArea().logOnConsole(outputBuffer.toString());
               outputBuffer = new StringBuffer();

            }

            getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\" color=\"#0000CC\"><b>" +
                         I18n.getFormattedString("iReportCompiler.exportRunningTime", "Export running time: {0,number}!",
                                new Object[]{new Long(System.currentTimeMillis() - start), new Integer(((List)print.getPages()).size())}) + "</b></font><hr>",true);

            // Export using the rigth program....

            Runtime rt = Runtime.getRuntime();
            if (viewer_program == null || viewer_program.equals(""))
            {

               if (format.equalsIgnoreCase("jrviewer") || format.equalsIgnoreCase("java2D"))
               {

               }
               else
                  getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                          I18n.getString("iReportCompiler.noExternalViewer","No external viewer specified for this type of print. Set it in the options frame!") +
                          "</font>",true);

            }
            else
            {
               try
               {
                  String execute_string = viewer_program + " "+fileName+"";
                  getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                         I18n.getFormattedString("iReportCompiler.executingString", "Executing: {0}",
                                new Object[]{execute_string}) + "</font>",true);
                  rt.exec( execute_string );
               } catch (Exception ex)
               {

                  getLogTextArea().logOnConsole("Error viewing report...\n");
                  ex.printStackTrace();
                  getLogTextArea().logOnConsole(outputBuffer.toString());
                  outputBuffer = new StringBuffer();
               }
               //getLogTextArea().logOnConsole("Finished...\n");
            }
         }
         else
         {
             getLogTextArea().logOnConsole("<font face=\"SansSerif\"  size=\"3\">" +
                         I18n.getString("iReportCompiler.printNotFilled", "Print not filled. Try to use an EmptyDataSource...") + "</font>",true);
            getLogTextArea().logOnConsole("\n");
         }
      }

      fireCompileListner(this, CL_COMPILE_OK, CLS_COMPILE_OK);
      removeThread();

      if (backupJRClasspath != null) {
          //System.setProperty("jasper.reports.compile.class.path",backupJRClasspath);
          net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, backupJRClasspath );
      }
      else
      {
          //System.getProperties().remove("jasper.reports.compile.class.path");
          net.sf.jasperreports.engine.util.JRProperties.restoreProperties();
      }

      if (backupSystemClasspath != null) System.setProperty("java.class.path",backupSystemClasspath);
      else System.getProperties().remove("java.class.path");

     } finally
     {
      System.gc();
      System.setOut(out);
      System.setErr(err);   
      System.gc();
     }
      
   }


   public void removeThread()
   {
       if (threadList != null)
         synchronized(threadList)
      {
         javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)threadList.getModel();
         dlm.removeElement(this);
         threadList.updateUI();
         getLogTextArea().setTitle( I18n.getString("iReportCompiler.status.finished", "Finished") + constTabTitle);
         getLogTextArea().setRemovable(true);
      }

   }
   /** Getter for property command.
    * @return Value of property command.
    *
    */
   public int getCommand()
   {
      return command;
   }

   /** Setter for property command.
    * @param command New value of property command.
    *
    */
   public void setCommand(int command)
   {
      this.command = command;
   }

   /** Getter for property iReportConnection.
    * @return Value of property iReportConnection.
    *
    */
   public it.businesslogic.ireport.IReportConnection getIReportConnection()
   {
      return iReportConnection;
   }

   /** Setter for property iReportConnection.
    * @param iReportConnection New value of property iReportConnection.
    *
    */
   public void setIReportConnection(it.businesslogic.ireport.IReportConnection iReportConnection)
   {
      this.iReportConnection = iReportConnection;
   }

   /** Getter for property jrf.
    * @return Value of property jrf.
    *
    */
   public JReportFrame getJrf()
   {
      return jrf;
   }

   /** Setter for property jrf.
    * @param jrf New value of property jrf.
    *
    */
   public void setJrf(JReportFrame jrf)
   {
      this.jrf = jrf;
   }

   /** Getter for property mainFrame.
    * @return Value of property mainFrame.
    *
    */
   public MainFrame getMainFrame()
   {
      return mainFrame;
   }

   /** Setter for property mainFrame.
    * @param mainFrame New value of property mainFrame.
    *
    */
   public void setMainFrame(MainFrame mainFrame)
   {
      this.mainFrame = mainFrame;
   }

   /** Getter for property properties.
    * @return Value of property properties.
    *
    */
   public HashMap getProperties()
   {
      return properties;
   }

   /** Setter for property properties.
    * @param properties New value of property properties.
    *
    */
   public void setProperties(HashMap properties)
   {
      this.properties = properties;
   }

   public String toString()
   {
      return status;
   }

   class FilteredStream extends FilterOutputStream
   {
      public FilteredStream(OutputStream aStream)
      {
         super(aStream);
      }

      public void write(byte b[]) throws IOException
      {
         String aString = new String(b);
         outputBuffer.append( aString );

         if (outputBuffer.length() > maxBufferSize) // 5000000
         {
             outputBuffer = outputBuffer.delete(0, outputBuffer.length()-maxBufferSize);
         }
      }

      public void write(byte b[], int off, int len) throws IOException
      {
         String aString = new String(b , off , len);
         outputBuffer.append( aString );
         if (outputBuffer.length() > maxBufferSize)
         {
             outputBuffer = outputBuffer.delete(0, outputBuffer.length()-maxBufferSize);
         }
         //getLogTextArea().logOnConsole(aString);
      }
   }

   public void start()
   {
      this.thread = new Thread(this);

	//using the report directory to load classes and resources
	try{
		  String reportDirectory = new File(jrf.getReport().getFilename()).getParent();

		  //set classpath
		  //String classpath = System.getProperty("jasper.reports.compile.class.path");
                  String classpath = net.sf.jasperreports.engine.util.JRProperties.getProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH );


		  if(classpath != null){

			  classpath += File.pathSeparator + reportDirectory;
			  //System.setProperty("jasper.reports.compile.class.path", classpath);
                          net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, classpath);

		  } else if( System.getProperty("java.class.path") != null){

			  classpath = System.getProperty("java.class.path");
			  classpath += File.pathSeparator + reportDirectory;
			  System.setProperty("java.class.path", classpath);
		  }

		  // Add all the hidden files.... (needed only by JWS)
		  if (MainFrame.getMainInstance().isUsingWS())
		  {
		  	try {
			   Enumeration e = MainFrame.getMainInstance().getReportClassLoader().getResources("META-INF/MANIFEST.MF");
			   while (e.hasMoreElements()) {
			      URL url = (URL) e.nextElement();
			      String newJar = ""+url.getFile();

			      if (newJar.endsWith("!/META-INF/MANIFEST.MF"))
			      {
			      	newJar = newJar.substring(0, newJar.length() - "!/META-INF/MANIFEST.MF".length());

			        newJar = java.net.URLDecoder.decode(newJar, "UTF-8");

			        MainFrame.getMainInstance().logOnConsole("JX:" + newJar);

			      	newJar = newJar.replace('\\', '/');
			  	if (newJar.startsWith("file://"))
			  	{
			  		newJar = newJar.substring(7);
			  	}

			  	if (newJar.startsWith("file:"))
			  	{
			  		newJar = newJar.substring(5);
			  	}

			  	if(!newJar.startsWith("/")){
				  newJar = "/" + newJar;//it's important to JVM 1.4.2 especially if contains windows drive letter
			  	}
			      }

			       if (classpath.indexOf(newJar + File.pathSeparator) < 0 &&
			           !classpath.endsWith(newJar))
			       {
			      	classpath +=  File.pathSeparator + newJar;
			       }
			     }
			  } catch (Exception exc) {
			  	MainFrame.getMainInstance().logOnConsole("exception ex:" + exc.getMessage());
			   	exc.printStackTrace();
			  }
			  System.setProperty("java.class.path", classpath);
		  }
		  //MainFrame.getMainInstance().logOnConsole("CLASSPATH" + classpath);

		  //include report directory for resource search path
		  reportDirectory = reportDirectory.replace('\\', '/');
		  if(!reportDirectory.endsWith("/")){
			  reportDirectory += "/";//the file path separator must be present
		  }
		  if(!reportDirectory.startsWith("/")){
			  reportDirectory = "/" + reportDirectory;//it's important to JVM 1.4.2 especially if contains windows drive letter
		  }

		  thread.setContextClassLoader(new URLClassLoader(new URL[]{
		  	  new URL("file://"+reportDirectory)
		  }, MainFrame.getMainInstance().getReportClassLoader()));

	 } catch (MalformedURLException mue){
	  mue.printStackTrace();
	}

      this.thread.start();
   }

   public void parseException(String exception, Vector sourceLines)
   {

      // Create a single outString...
      String outString = "";

      // For each row, looking for a file name followed by a row number...
      //javax.swing.JOptionPane.showMessageDialog(null,exception);
      StringTokenizer st = new StringTokenizer(exception, "\n");
      while (st.hasMoreElements())
      {

         String line = st.nextToken();
         if (line.startsWith(this.javaFile))
         {
            // The next line is an expression error....
            //getLogTextArea().logOnConsole(line+"\n");
            outString += Misc.toHTML(line+"\n");
            String lineNumber = line.substring( this.javaFile.length()+1);
            lineNumber = lineNumber.substring(0, lineNumber.indexOf(':'));
            int ln = Integer.parseInt(lineNumber);
            if (ln >= 3) ln -=3;

            // Take the element name....

            String reference = "";
            if (sourceLines.size() >= ln)
            {

               String lineCode = (String)sourceLines.elementAt(ln);
               //getLogTextArea().logOnConsole( "Line: " + ln + "> " + lineCode+"\n");
               if (lineCode.indexOf("            case") == 0 &&
               lineCode.indexOf(" : // ") >0)
               {
                  // We have found the comment where jasperReport puts the expression ref.
                  reference = lineCode.substring(lineCode.indexOf(" : // ")+6).trim();
               }
            }

            if (st.hasMoreElements())
            {
               try
               {
                  String error_line = st.nextToken();
                  if (error_line.startsWith("found"))
                  {
                     //getLogTextArea().logOnConsole( error_line+"\n");
                     outString += Misc.toHTML(error_line+"\n");
                     error_line = st.nextToken();
                  }
                  if (error_line.startsWith("required"))
                  {
                     //getLogTextArea().logOnConsole( error_line+"\n");
                     outString += Misc.toHTML(error_line+"\n");
                     error_line = st.nextToken();
                  }
                  if (error_line.startsWith("symbol"))
                  {
                     //getLogTextArea().logOnConsole( error_line+"\n");
                     outString += Misc.toHTML(error_line+"\n");
                     error_line = st.nextToken();
                  }
                  if (error_line.startsWith("location"))
                  {
                     //getLogTextArea().logOnConsole( error_line+"\n");
                     outString += Misc.toHTML(error_line+"\n");
                     error_line = st.nextToken();
                  }

                  // Find the link....
                  // Count space at begin of line...
                  String html = "";
                  for (int i=0; i<error_line.length(); ++i)
                  {
                     if (error_line.charAt(i) == ' ')
                     {
                        html += "&nbsp;";
                     }
                     else break;
                  }
                  error_line = error_line.trim();
                  html += "<a href=\"http://error:"+ jrf.getWindowID()+ "/"+reference+"\">"+Misc.toHTML(error_line)+"</a>";
                  //getLogTextArea().logOnConsole( html,true);
                  outString += html;
               } catch (Exception ex)
               {
                  //getLogTextArea().logOnConsole( "Error parsing\n!");
                  outString += Misc.toHTML("Error parsing\n!");
               }
            }
         }
         else if (line.startsWith("Warning :") && line.indexOf("y=")>0 && line.indexOf("height=")>0 && line.indexOf("band-height=")>0)
         {
            //getLogTextArea().logOnConsole("<a href=\"http://warning\">"+Misc.toHTML(line)+"</a>",true);
            outString += "<a href=\"http://warning:"+ jrf.getWindowID() +"\">"+Misc.toHTML(line)+"</a>";
         }
         else
         {
            //getLogTextArea().logOnConsole(line+"\n");
            outString += Misc.toHTML(line+"\n");
         }
      }
      getLogTextArea().logOnConsole(outString,true);
      //getLogTextArea().logOnConsole( "<a href=\"http://problem\">*****<hr><font face=\"Courier New\" size=\"3\">"+ exception +"</a>", true);
      outputBuffer = new StringBuffer();
      
     
   }

   public javax.swing.JComponent searchButton(javax.swing.JComponent root, String text)
   {
      if (root instanceof javax.swing.JButton && ((javax.swing.JButton)root).getText()!=null && ((javax.swing.JButton)root).getText().equals(text) ) return root;
      for (int i=0; i<root.getComponentCount(); ++i)
      {

         if (root.getComponent(i) instanceof javax.swing.JComponent)
         {
            getLogTextArea().logOnConsole(""+ ((javax.swing.JComponent)root.getComponent(i)) +"\n");
            javax.swing.JComponent res = searchButton((javax.swing.JComponent)root.getComponent(i),  text);
            if (res != null) return res;
         }
      }
      return null;
   }

   public void afterPageExport() {

       filledpage++;
       if (command == 0)
       {

       }


   }

    public javax.swing.JList getThreadList() {
        return threadList;
    }

    public void setThreadList(javax.swing.JList threadList) {
        this.threadList = threadList;
    }

    public void updateThreadList()
    {
        getLogTextArea().setTitle(status + constTabTitle);
        try {

         if (threadList != null) javax.swing.SwingUtilities.invokeAndWait(
            new java.lang.Runnable() {
                    public void run() {
                      synchronized(threadList) {
                        threadList.updateUI();
                      }
                    }
            } );
         } catch (Exception ex) {}
    }

    private ClassLoader getClassLoader()
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (classLoader != null)
		{
			//try
			//{
			//	Class.forName(net.sf.jasperreports.engine.design.JRJdtCompiler.class.getName(), true, Thread.currentThread().getContextClassLoader());
			//}
			//catch (ClassNotFoundException e)
			//{
			//	classLoader = null;
			//	//if (log.isWarnEnabled())
			//	//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRJdtCompiler class. Using JRJdtCompiler.class.getClassLoader() instead.");
			//}
		}

		if (classLoader == null)
		{
			//classLoader = JRClassLoader.class.getClassLoader();
		}

		return classLoader;
	}

    public LogTextArea getLogTextArea() {
        return logTextArea;
    }

    public void setLogTextArea(LogTextArea logTextArea) {
        this.logTextArea = logTextArea;
    }

    /**
     * added by Felix Firgau
     */
    public static final int CL_COMPILE_OK = 1;
    public static final int CL_COMPILE_FAIL = 2;

    public static final String CLS_COMPILE_OK = "compileok";
    public static final String CLS_COMPILE_SCRIPTLET_FAIL = "scriptletfail";
    public static final String CLS_COMPILE_SOURCE_FAIL = "sourcefail";


    /**
     * (FF) addCompileListener to notify about compiling actions
     * @param listener ActionListener
     */
    public static void addCompileListener(java.awt.event.ActionListener listener) {
      if(!compileListener.contains(listener))
        compileListener.add(listener);
    }

    /**
     * (FF) removeCompileListener removes notification
     * @param listener ActionListener
     */
    public static void removeCompileListener(java.awt.event.ActionListener listener) {
      compileListener.remove(listener);
    }

    /**
     * (FF) fireCompileListner fires compiling action notifications
     * @param id int
     * @param status String
     */
    public static void fireCompileListner(IReportCompiler ireportCompiler, int id, String status) {
      java.awt.event.ActionListener[] list = (java.awt.event.ActionListener[])compileListener.toArray(
       new java.awt.event.ActionListener[compileListener.size()]);

      java.awt.event.ActionEvent e = new java.awt.event.ActionEvent(ireportCompiler, id, status);
      for (int i = 0; i < list.length; i++) {
        java.awt.event.ActionListener listener = list[i];
        listener.actionPerformed(e);
      }
    }
    //End FF
    
    public static JasperDesign loadJasperDesign(InputStream fileStream, SourceTraceDigester digester) throws JRException
	{
		JRXmlLoader xmlLoader = new JRXmlLoader(digester);
		
		try
		{
			JasperDesign jasperDesign = xmlLoader.loadXML(fileStream);
			return jasperDesign;
		}
		finally
		{
			try
			{
				fileStream.close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
	}

	public static SourceTraceDigester createDigester() throws JRException
	{
		SourceTraceDigester digester = new SourceTraceDigester();
		try
		{
			JRXmlDigesterFactory.configureDigester(digester);
		}
		catch (SAXException e)
		{
			throw new JRException(e);
		}
		catch (ParserConfigurationException e)
		{
			throw new JRException(e);
		}
                return digester;
	}
}

