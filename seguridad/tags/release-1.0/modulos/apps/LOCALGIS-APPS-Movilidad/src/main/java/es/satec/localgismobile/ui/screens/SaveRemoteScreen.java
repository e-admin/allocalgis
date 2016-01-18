package es.satec.localgismobile.ui.screens;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGParser;

import es.satec.localgismobile.core.Application;
import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.net.communications.HttpManager;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.svgviewer.localgis.MetaInfo;
import es.satec.svgviewer.localgis.SynchChangesUtils;

public class SaveRemoteScreen extends LocalGISWindow {

	private Label labelHeader = null;
	private List listCells = null;
	private Button buttonOK = null;
	private Button buttonCancel = null;
	
	private static Logger logger = (Logger) Logger.getInstance(SaveRemoteScreen.class);  //  @jve:decl-index=0:
	
	public SaveRemoteScreen(Shell parent, Vector modifiedCells) {
		super(parent);
		init(modifiedCells);
		show();
	}

	/**
	 * This method initializes sShell
	 */
	private void init(Vector modifiedCells) {
		shell.setText(Messages.getMessage("SaveRemoteScreen.titulo"));
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);

		labelHeader = new Label(shell, SWT.NONE);
		labelHeader.setText(Messages.getMessage("SaveRemoteScreen.cabecera"));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.CENTER;
		labelHeader.setLayoutData(gridData);

		listCells = new List(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 2;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.FILL;
		listCells.setLayoutData(gridData1);
		Enumeration en = modifiedCells.elements();
		while (en.hasMoreElements()) {
			listCells.add((String) en.nextElement());
		}

		buttonOK = new Button(shell, SWT.NONE);
		buttonOK.setText(Messages.getMessage("botones.aceptar"));
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.CENTER;
		buttonOK.setLayoutData(gridData2);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				saveCellsRemote();
			}
		});

		buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.setText(Messages.getMessage("botones.cancelar"));
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = GridData.CENTER;
		buttonCancel.setLayoutData(gridData3);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

	/**
	 * Busca las modificaciones en las celdas seleccionadas y las envia para almacenarlas
	 * en el servidor. 
	 */
	private void saveCellsRemote() {
		if (listCells.getSelectionCount() == 0) {
			logger.debug("No se ha seleccionado ninguna celda");
			MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("SaveRemoteScreen.noCeldasSeleccionadas"));
			mb.open();
		}
		else {
			
			SessionInfo session = SessionInfo.getInstance();
			String projectPath = session.getProjectInfo().getPath();
			int srid = session.getProjectInfo().getSrid();
			String[] selectedCells = listCells.getSelection();
			// Recorrido de las celdas seleccionadas
			for (int i=0; i<selectedCells.length; i++) {
				ByteArrayOutputStream baos = null;
				ByteArrayInputStream bais = null;
				FileOutputStream fos = null;
				try {
					// URL de conexion con el servidor
					String url = Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_PROTOCOL) + "://" +
						Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST) + ":" +
						Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80) +
						Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT) +
						Config.prLocalgis.getProperty(Config.PROPERTY_SAVE_MAP_QUERY);
					logger.debug("URL: " + url);

					// Apertura del documento svg de la celda
					SVGDocument svgDoc = parseSVGDocument(projectPath + File.separator + selectedCells[i] + ".svg");
	
					// Instanciar la metainformacion
					Vector metaInfos = new Vector();
					Vector enabledApplications = session.getProjectInfo().getEnabledApplications();
					if (enabledApplications != null) {
						Enumeration e = enabledApplications.elements();
						while (e.hasMoreElements()) {
							Application a = Applications.getInstance().getApplicationByName((String) e.nextElement());
							metaInfos.addElement(new MetaInfo(projectPath, selectedCells[i], a.getName(), a.getKeyAttribute(),
								Config.prLocalgis.getPropertyAsInt(SessionInfo.getInstance().getProjectInfo().getNumFicherosLicencias(), 1)));
						}
					}

					// Serializacion y envio de las modificaciones del svg
					logger.debug("Enviando cambios serializados para la celda " + selectedCells[i]);
					baos = new ByteArrayOutputStream();
					SynchChangesUtils.serializeToUpload(baos, svgDoc, metaInfos, srid);
					
					HttpManager httpManager = new HttpManager(url, "POST", "text/xml", false, null);
					httpManager.enviar(baos.toByteArray());

					byte[] respuesta = httpManager.getRespuestaBytes();
					// Lectura e interpretacion de la respuesta del servidor
					bais = new ByteArrayInputStream(respuesta);
					Parser p = new Parser();
					p.setNodeFactory(new DomNodeFactory());
					p.setInputStream(bais);
					p.parse();
					Document responseDoc = p.getDocument();
	
					int code = SynchChangesUtils.readResponseToResetSVGChanges(responseDoc, svgDoc, metaInfos);
					if (code == 0 || code == 1 || code == 3) {
						// Guardar el documento svg en local para que no haya inconsistencias
						logger.debug("Guardando en local");
						fos = new FileOutputStream(projectPath + File.separator + selectedCells[i] + ".svg");
						svgDoc.serializeSVG2XML(fos);
					}
					if ((code == 1) || (code == 3)) {
						// Ha habido errores al almacenar las modificaciones en remoto
						logger.error("Ha habido errores al almacenar las modificaciones en remoto");
						MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
						if (code ==1)
							mb.setMessage(Messages.getMessage("SaveRemoteScreen.celda") + " " + selectedCells[i] + ": "  + 
									Messages.getMessage("SaveRemoteScreen.errores.entidades"));
						else
							mb.setMessage(Messages.getMessage("SaveRemoteScreen.celda") + " " + selectedCells[i] + ": "  + 
									Messages.getMessage("SaveRemoteScreen.errores.entidades.permisos"));
						mb.open();
					}
					else if (code == 2) {
						// Error interno en el servidor
						logger.error("Se ha producido un error en el servidor");
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
						mb.setMessage(Messages.getMessage("SaveRemoteScreen.error.servidor"));
						mb.open();
						shell.close();
						return;
					}
				} catch (LoginException e) {
					ScreenUtils.stopHourGlass(shell);
					logger.error("No hay sesion de usuario", e);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					mb.setMessage(Messages.getMessage("SaveRemoteScreen.error.sesionUsuario"));
					if (mb.open() == SWT.OK) {
						new LoginScreen(shell, SWT.NONE);
					}
					else {
						shell.close();
					}
					return;
				} catch (NoConnectionException e) {
					ScreenUtils.stopHourGlass(shell);
					logger.error("No hay conexion", e);
					showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.noConexion"));
					shell.close();
					return;
				} catch (MalformedURLException e) {
					ScreenUtils.stopHourGlass(shell);
					logger.error("URL incorrecta", e);
					showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.urlServidor"));
					shell.close();
					return;
				} catch (FileNotFoundException e) {
					ScreenUtils.stopHourGlass(shell);
					logger.error("Fichero de celda no encontrado", e);
					showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.noFicheroCelda") + " " + selectedCells[i]);
				} catch (Exception e) {
					ScreenUtils.stopHourGlass(shell);
					logger.error("Error al enviar los cambios serializados", e);
					showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.enviar"));
					shell.close();
					return;
				} finally {
					try {
						if (baos != null) baos.close();
						if (bais != null) bais.close();
						if (fos != null) fos.close();
					} catch (IOException e) {}
					ScreenUtils.stopHourGlass(shell);
				}
			}
			
			logger.debug("Proceso de guardado concluido");
			MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
			mb.setMessage(Messages.getMessage("SaveRemoteScreen.finGuardado"));
			mb.open();
			shell.close();
		}
	}
	
	private SVGDocument parseSVGDocument(String filePath) throws FileNotFoundException {
		SVGDocument svgDoc = new SVGDocument();
		SVGParser svgParser = new SVGParser(new SVGAttr());
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			svgParser.load(svgDoc, fis);
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {}
			}
		}
		return svgDoc;
	}
	
	private void showErrorMessageBox(String message) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setText(Messages.getMessage("errores.error"));
		mb.setMessage(message);
		mb.open();
	}
}
