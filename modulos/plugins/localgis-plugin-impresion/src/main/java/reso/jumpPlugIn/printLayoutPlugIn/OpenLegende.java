/*
 * Package reso.jump.legende pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 25 nov. 2004
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
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFileChooser;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PreviewPanel;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



/**
 * @author BREMOND_I
 */


public class OpenLegende implements ActionListener {

	private PrintLayoutFrame frame;
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	private PageFormat pageFormat;
	private JFileChooser fileChooser;
	private boolean forceSelect;
	private TaskMonitorDialog progressDialog;

	
	public OpenLegende(PrintLayoutFrame parent) {
		initOpenLegende(parent, null, null);
	}
	public OpenLegende(PrintLayoutFrame parent, JFileChooser fChooser) {
		initOpenLegende(parent, fChooser, null);
	}
	public OpenLegende(PrintLayoutFrame parent, JFileChooser fChooser, PageFormat pFormat) {
		initOpenLegende(parent, fChooser, pFormat);
	}
	
	
	private void initOpenLegende (PrintLayoutFrame parent, JFileChooser fChooser, PageFormat pFormat) {
		frame = parent;
		fileChooser = fChooser;
		forceSelect = (fileChooser == null);
		pageFormat = pFormat;
	}
	
	public PrintLayoutFrame getFrame() {
		return frame;
	}

	public void actionPerformed(ActionEvent e) {
		if (forceSelect) {
			if (progressDialog != null)
				progressDialog.report(UtilsPrintPlugin.getMessageI18N("PrintPlugin.descAccion.buscarPlantillas")); 
			try{
				//Forzar descarga de plantillas: establecer ruta descarga
				String path = ConstantesLocalGISPlantillas.PATH_IMPRESION + File.separator;
		    	AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.idAppType, path);
				DownloadFromServer dfs = new DownloadFromServer();
				dfs.getServerPlantillas("jmp");	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//Mostrar panel para seleccion plantilla
			fileChooser = getFileChooser();
			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(aplicacion.getMainFrame()))
				loadFileOpenLegende ();
		}
		else	//Cargamos fichero seleccionado indicado
			loadFileOpenLegende ();
	}
	
	public JFileChooser getFileChooser() {
		if (fileChooser==null)
			fileChooser=new JFileChooser();
		return fileChooser;
	}
	
	public void setProgressDialog(TaskMonitorDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	private void loadFileOpenLegende () 
	{
		if (progressDialog != null)
			progressDialog.report(UtilsPrintPlugin.getMessageI18N("PrintPlugin.descAccion.cargarPlantilla")); 
		
		File file = getFileChooser().getSelectedFile(); 
		if (file != null) {
			file = FileUtil.addExtensionIfNone(file, "jmp");
	
			FileReader reader = null;
			try {
				reader = new FileReader(file);
			} catch (FileNotFoundException e1) {
				// TODO Bloc catch auto-généré
				e1.printStackTrace();
			}
			Page page;
			page = null;
			try {
				page = (Page) new XML2Java().read(reader, Page.class);
			} catch (Exception e2) {
				// TODO Bloc catch auto-généré
				e2.printStackTrace();
			}
	
			if (page != null)
			{
				//Actualizar titulo ventana para incluir nombre de plantilla y la primera carga
				if (forceSelect)
					frame.setTitle(frame.getTitle() + ": " + file.getName());
				
				//Establecer formato de pagina: enviado o el de la plantilla
				if (pageFormat != null) 
					frame.setPageFormat(frame.getPrinterJob().defaultPage(pageFormat));
				else {
					Paper paper = new Paper();
					if (page.getLargeur() < page.getHauteur()){ // vertical
						frame.getPageFormat().setOrientation(PrintLayoutFrame.LARGEUR_PAGE);
						paper.setImageableArea(page.getLeft(), page.getTop(), page.getLargeurSansMarge(), page.getHauteurSansMarge());
						paper.setSize(page.getLargeur(), page.getHauteur());
					}
					else{ // horizontal
						frame.getPageFormat().setOrientation(PageFormat.LANDSCAPE);
						paper.setImageableArea(page.getLeft(), page.getTop(), page.getHauteurSansMarge(), page.getLargeurSansMarge());
						paper.setSize(page.getHauteur(), page.getLargeur());
					}
					frame.getPageFormat().setPaper(paper);
				}
				//Actualizar vista segun formato cargado
				frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().resize(frame.getPageFormat());
			    frame.getPrintLayoutPreviewPanel().setPreview(new PreviewPanel(frame, frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage()));
			    
			    //Cargar elementos de la pagina
				List list = page.getGraphicElement();
				for (int i=1; i<=list.size(); i++) {
					frame.setAjouter((GraphicElements) list.get(i-1));
					frame.getPage().posGraphicElement(frame);
				}
			}
			
			frame.pack();
			frame.repaint();
			frame.setAlwaysOnTop(true);
			//frame.toFront();
			frame.show();
			
			//TODO: tratamiento retardo no se si es aqui o donde????
	//		int numIntentos = 0;
	//		int maxIntentos = 2000;
	//		int sleepTime = 30;
	//		boolean operacionRealizada = false;
	//		while (numIntentos < maxIntentos)
	//		{
	//			numIntentos ++;
	//			try {
	//				System.out.println("Intento: " +numIntentos);
	//				frame.repaint();
	//				Thread.sleep(sleepTime);
	//			}catch(Exception e){
	//				throw new RuntimeException(e);
	//			}
	//		}
		}
	}
}