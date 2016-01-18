/**
 * Launcher.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.reports.launcher;

import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.SplashDialog;
import it.businesslogic.ireport.util.I18n;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import jcmdline.BooleanParam;
import jcmdline.CmdLineHandler;
import jcmdline.FileParam;
import jcmdline.HelpCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.PosixCmdLineParser;
import jcmdline.StringParam;
import jcmdline.VersionCmdLineHandler;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.security.SecurityManager;
import com.geopista.util.config.UserPreferenceStore;

public class Launcher {
	
	private static final String INFORMES_DIR = "informes";
	private static final String WORKDIR_RESOURCES_BASE = "ireport/workdir";
	
	static Logger logger;
    static {
       createDir();
       logger  = Logger.getLogger(Launcher.class);
    } 	
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	    
	public void start(String[] args) {

		// command line arguments
		//StringParam patternArg =
		//    new StringParam("config-file", "",
		//                    StringParam.PUBLIC);

		
		//System.getProperty
		
		initHome();
		
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
							"Localgis iReport",
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
		
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
			
			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {			
			e.printStackTrace();
		}
		
		final MainFrame _mainFrame = new MainFrame(map);		
		
		SwingUtilities.invokeLater( new Runnable() {
			public void run()
			{								
				_mainFrame.setVisible(true);
				
				//changeTheme(_mainFrame);
//				WizardDialog wd = new WizardDialog(new JFrame(),true);
//		        wd.setVisible(true);
//		        if (wd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
//		            Report report = wd.getReport();
//		            if (report != null) {
//		            }
//		        }
			}
		});
		
	}
	
	public void initHome(){
		
		SecurityManager sm = new SecurityManager();
		sm.setInitHeartBeat(false);

		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
		//aplicacion.stopHeartBeat();
		String iReportHomePath = 
				UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
					UserPreferenceConstants.DEFAULT_DATA_PATH,true) + File.separator + INFORMES_DIR;
		
		System.setProperty("user.dir", iReportHomePath);
		
		File iReportHomePathFile = new File(iReportHomePath);
		if (!iReportHomePathFile.exists()){
			iReportHomePathFile.mkdirs();
		}
		
		System.setProperty("ireport.home", iReportHomePath);

		//Creamos el directorio plugins
    	File file = new File(iReportHomePath+File.separator+"plugins");
    	if (! file.exists()) {
    		file.mkdirs();
    	}		
		
		ClassLoader cl = this.getClass().getClassLoader();

		String [] resources= new String[] {
				"/fonts/Balker.ttf",
				"/fonts/Domestic_Manners.ttf",
				"/fonts/Dustismo.ttf",
				"/fonts/dustismo_bold.ttf",
				"/fonts/dustismo_bold_italic.ttf",
				"/fonts/dustismo_italic.ttf",
				"/fonts/Dustismo_Roman.ttf",
				"/fonts/Dustismo_Roman_Bold.ttf",
				"/fonts/Dustismo_Roman_Italic.ttf",
				"/fonts/Dustismo_Roman_Italic_Bold.ttf",
				"/fonts/El_Abogado_Loco.ttf",
				"/fonts/flatline.ttf",
				"/fonts/It wasnt me.ttf",
				"/fonts/Junkyard.ttf",
				"/fonts/MarkedFool.ttf",
				"/fonts/PenguinAttack.ttf",
				"/fonts/progenisis.ttf",
				"/fonts/Swift.ttf",
				"/fonts/Wargames.ttf",
				"/fonts/Winks.ttf",
				"/templates/classicC.gif",
				"/templates/classicC.xml",
				"/templates/classicT.gif",
				"/templates/classicT.xml",
				"/templates/classic_landscapeT.gif",
				"/templates/classic_landscapeT.xml",
				"/templates/grayC.gif",
				"/templates/grayC.xml",
				"/templates/grayT.gif",
				"/templates/grayT.xml",
				"/templates/gray_landscapeT.gif",
				"/templates/gray_landscapeT.xml",
				"/templates/YBC.gif",
				"/templates/YBC.xml" };

		for (int i = 0; i < resources.length; i++){
			String resourceName = WORKDIR_RESOURCES_BASE + resources[i];
			String resourcePath = iReportHomePath + resources[i];
			String resourceBaseDir = resourcePath.substring(0, resourcePath.lastIndexOf('/'));

			File resourceFile = new File(resourcePath);
			if (resourceFile.exists()){
				continue;
			}
			File resourceDir = new File(resourceBaseDir);
			if (!resourceDir.exists()){
				resourceDir.mkdirs();
			}

			InputStream resourceInputStream = cl.getResourceAsStream(resourceName);
			if (resourceInputStream == null){
				continue;
			}
			
			FileOutputStream resourceFileOutput = null;
			try {
				resourceFileOutput = new FileOutputStream(resourcePath);
			} catch (FileNotFoundException e) {
				System.out.println("No se ha podido escribir el archivo: " + resourcePath);
				e.printStackTrace();
				continue;
			}


			byte [] buffer = new byte[1024];

			try {
				int numBytes = resourceInputStream.read(buffer);
				while (numBytes != -1){
					resourceFileOutput.write(buffer, 0, numBytes);
					numBytes = resourceInputStream.read(buffer);
				}
			} catch (IOException e) {
				System.out.println("Error al escribir el recurso: " + resourcePath);
				e.printStackTrace();
			}
			
			try {
				resourceFileOutput.close();
			} catch (IOException e) {
				System.out.println("Error al cerrar el archivo: " + resourcePath);
				e.printStackTrace();
			}
		}
	}

}
