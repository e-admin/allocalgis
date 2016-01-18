/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface.Actions pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 3 sept. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

import com.geopista.app.AppContext;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author FOUREAU_C
 */
public class Print implements ActionListener {
	private PrintLayoutFrame frame;
	public Print(PrintLayoutFrame parent) {
		frame = parent;
	}


	public void actionPerformed(ActionEvent e) {


		frame.getPrinterJob().setPrintable( (Printable) frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint(), frame.getPageFormat());
		if (frame.getPrinterJob().printDialog()) {
			{
				AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

				progressDialog.setTextTitle(UtilsPrintPlugin.getMessageI18N("PrintMapDialogTitle"));
				progressDialog.report(UtilsPrintPlugin.getMessageI18N("Print")); 
				progressDialog.addComponentListener(new ComponentAdapter()
				{
					public void componentShown(ComponentEvent e)
					{

						// Wait for the dialog to appear before starting the
						// task. Otherwise
						// the task might possibly finish before the dialog
						// appeared and the
						// dialog would never close. [Jon Aquino]
						new Thread(new Runnable()
						{
							public void run()
							{
								try
								{
									// le dialogue d’impression
									try {
										JFrame f = new JFrame();
										f.getContentPane().add(frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint());
										f.pack();
										f.show();
										frame.getPrinterJob().print();
										frame.toBack();
										JOptionPane.showMessageDialog(frame, "Impresion realizada");
										f.remove(frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint());
										f.dispose();
									} catch (PrinterException exception) {

										if (exception.getMessage().indexOf("accepting job") != -1) {
											// recommend prompting the user at this point if they want to force it
											// so they'll know there may be a problem.
											try {
												// try printing again but ignore the not-accepting-jobs attribute
												PrintService printer = frame.getPrinterJob().getPrintService();
												DocPrintJob job = printer.createPrintJob();

												DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
												SimpleDoc doc = new SimpleDoc((Printable) frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().getPageForPrint(), flavor, null);
												ForcedAcceptPrintService.setupPrintJob(job); // add secret ingredient
												job.print(doc,null);
											} catch (PrintException e) {
												// ok, you really can't print
												JOptionPane.showMessageDialog(frame, e);
											}
										}
										else
											JOptionPane.showMessageDialog(frame, exception);
									}


								} catch (Exception e)
								{
									e.printStackTrace();
								} finally
								{
									progressDialog.setVisible(false);
								}
							}
						}).start();
					}
				});
				GUIUtil.centreOnWindow(progressDialog);

				progressDialog.allowCancellationRequests();
				progressDialog.setVisible(true);
				progressDialog.toFront();
				progressDialog.requestFocus();
			}
		}
	}
}
