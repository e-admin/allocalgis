/*
 * Package reso.jump.legende pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 17 nov. 2004
 *
 * Développé dans le cadre du Projet APARAD 
 *  (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du Jaudy-Guindy-Bizien)
 *    Responsable : Erwan BOCHER
 *    Développeurs : Céline FOUREAU, Olivier BEDEL
 *
 * olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr
 * celine.foureau@uhb.fr ou celine.foureau@wanadoo.fr
 * 
 * Ce package hérite de la licence GPL de JUMP. Il est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. (http://www.cecill.info)
 * 
 */

package reso.jumpPlugIn.printLayoutPlugIn;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.SendToServerFrame;
import com.geopista.global.ServletConstants;
import com.geopista.util.UtilsPrintPlugin;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


public class SaveInServer implements ActionListener {

	private PrintLayoutFrame frame;
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private JFileChooser fileChooser;
 
    public SaveInServer(PrintLayoutFrame parent) {
		frame = parent;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			File file = saveLocal();

			if (!aplicacion.isLogged()) {
				aplicacion.setProfile("Geopista");
				aplicacion.login();
			}

			if (aplicacion.isLogged()) {

				if (aplicacion.getIdEntidad() != -1) {
					String sUrl = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) + ServletConstants.DOCUMENT_SERVLET_NAME;
					SendToServerFrame stsf = new SendToServerFrame(sUrl, frame, true, "impresion", "");
					
					String fileName = file.getName();
					if (!fileName.toLowerCase().endsWith(".jmp"))
						fileName = fileName + ".jmp";					
					stsf.setReportName(fileName);
					stsf.setVisible(true);
					
					stsf.executeSend(aplicacion, file, null);

				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public File saveLocal() {
		fileChooser = getFileChooser();

		if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(frame)) {
			File file = fileChooser.getSelectedFile();
			file = FileUtil.addExtensionIfNone(file, "jmp");
			StringWriter stringWriter = new StringWriter();

			try {
				new Java2XML().write(frame.getPage(), "page", stringWriter);
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				stringWriter.flush();
			}

			try {
				FileUtil.setContents(file.getAbsolutePath(),stringWriter.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return file;
		}
		return null;
	}
				
	public JFileChooser getFileChooser(){
		if (fileChooser==null){
			 fileChooser=new JFileChooser();
			 File dir=new File(UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,null,false));
		    	getFileChooser().setCurrentDirectory(dir);
		    	FileFilter fileFilter = GUIUtil.createFileFilter(UtilsPrintPlugin.getMessageI18N("PrintLayoutPlugin.PrintTemplates"), new String[]{"jmp"});
		    	fileChooser.addChoosableFileFilter(fileFilter);
	    }
		return fileChooser;
	}


}