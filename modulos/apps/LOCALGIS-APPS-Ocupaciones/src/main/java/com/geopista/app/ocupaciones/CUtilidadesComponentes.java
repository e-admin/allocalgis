/**
 * CUtilidadesComponentes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.ocupaciones.panel.CAddressJDialog;
import com.geopista.app.ocupaciones.panel.CCalendarJDialog;
import com.geopista.app.ocupaciones.panel.CDatosReferenciaCatastralJDialog;
import com.geopista.app.ocupaciones.panel.CHistoricoJDialog;
import com.geopista.app.ocupaciones.panel.CPersonaJDialog;
import com.geopista.app.ocupaciones.panel.CSearchJDialog;
import com.geopista.app.ocupaciones.panel.CSearchLicenciasObraJDialog;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CHistorico;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.security.GeopistaAcl;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.style.sld.ui.plugin.ChangeSLDStylesPlugIn;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaOptionsPlugIn;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarPlugIn;
import com.geopista.ui.snap.GeopistaInstallGridPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

/**
 * @author SATEC
 * @version $Revision: 1.4 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2010/07/07 11:16:05 $
 *          $Name:  $
 *          $RCSfile: CUtilidadesComponentes.java,v $
 *          $Revision: 1.4 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CUtilidadesComponentes {

	private static Logger logger = Logger.getLogger(CUtilidadesComponentes.class);
    public static Icon iconoZoom;
    public static Icon iconoFlecha;
    public static Icon iconoDobleFlecha;
    public static Icon iconoDeleteParcela;
    public static Icon iconoOK;
    public static Icon iconoAdd;
    public static Icon iconoRemove;
    public static Icon iconoAbrir;
    public static Icon iconoBloqueo;
    public static Icon iconoGenerarFicha;
    public static Icon iconoCalculadora;
    public static Icon iconoCalle;



    public static void inicializar()
    {
        try
        {
            ClassLoader cl =(new CUtilidadesComponentes()).getClass().getClassLoader();
            iconoZoom = new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
            iconoFlecha = new javax.swing.ImageIcon(cl.getResource("img/flecha_derecha.gif"));
            iconoDobleFlecha = new javax.swing.ImageIcon(cl.getResource("img/flecha_doble_derecha.gif"));
            iconoDeleteParcela = new javax.swing.ImageIcon(cl.getResource("img/delete_parcela.gif"));
            iconoOK= new javax.swing.ImageIcon(cl.getResource("img/ok.gif"));
            iconoAdd= new javax.swing.ImageIcon(cl.getResource("img/add.gif"));
            iconoRemove= new javax.swing.ImageIcon(cl.getResource("img/remove.gif"));
            iconoAbrir= new javax.swing.ImageIcon(cl.getResource("img/abrir.gif"));
            iconoBloqueo= new javax.swing.ImageIcon(cl.getResource("img/bloqueo.gif"));
            iconoGenerarFicha= new javax.swing.ImageIcon(cl.getResource("img/generar_ficha.gif"));
            iconoCalculadora= new javax.swing.ImageIcon(cl.getResource("img/calculadora.gif"));
            iconoCalle= new javax.swing.ImageIcon(cl.getResource("img/calle.jpg"));

        }catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception al inicializar las imagenes: " + sw.toString());
        }
    }
	public static String checkNull(Object o) {
		if (o == null) {
			return "";
		}

		return o.toString();
	}


	public static boolean selectBeanFeatures(GeopistaEditor geopistaEditor, Vector referenciasCatastrales) {

		try {
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				String referenciaCatastral = (String) referenciasCatastrales.elementAt(i);

				Collection collection = geopistaEditor.searchByAttribute("parcelas", "ID_Parcela", referenciaCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();

					Layer layer = geopistaEditor.getLayerManager().getLayer("parcelas");
					geopistaEditor.select(layer, feature);
				}
			}


			return true;
		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
			return false;
		}
	}


	public static Feature getBeanFeature(GeopistaEditor geopistaEditor, String referenciaCatastral) {

		try {


			Collection collection = geopistaEditor.searchByAttribute("parcelas", "ID_Parcela", referenciaCatastral);
			Iterator it = collection.iterator();
			if (it.hasNext()) {
				Feature feature = (Feature) it.next();
				logger.info("feature: " + feature);
				return feature;
			}

			logger.warn("Feature not found.");

			return null;

		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
			return null;
		}
	}

	public static boolean showGeopistaEditor(JFrame desktop, final JPanel panel, final GeopistaEditor geopistaEditor,
                                             final boolean editable) {
//*    **para sacar la ventana de espera**
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop, null);
            progressDialog.setTitle(CMainOcupaciones.literales.getString("Licencias.Tag1"));
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                                try {
                                    progressDialog.report(CMainOcupaciones.literales.getString("Licencias.Tag1"));
                                    geopistaEditor.showLayerName(true);
                                    ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
			                        geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);
                                   try
                                    {
                                         com.geopista.ui.plugin.GeopistaPrintPlugIn geopistaPrintPlugIn = new com.geopista.ui.plugin.GeopistaPrintPlugIn();
			                             geopistaEditor.addPlugIn(geopistaPrintPlugIn);
                                        com.geopista.ui.plugin.SeriePrintPlugIn seriePrintPlugIn = new com.geopista.ui.plugin.SeriePrintPlugIn();
                                        geopistaEditor.addPlugIn(seriePrintPlugIn);
                                    }catch(Exception e)
                                    {
			                            logger.error("Exception: " ,e);
                                    }
                                    if (editable)
                                    {
                                            GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
                                            geopistaEditor.addPlugIn(geopistaEditingPlugIn);
                                    }

                                    GeopistaInstallGridPlugIn geopistaInstallGridPlugIn = new GeopistaInstallGridPlugIn();
                                    GeopistaOptionsPlugIn geopistaOptionsPlugIn = new GeopistaOptionsPlugIn();
                                    geopistaEditor.addPlugIn(geopistaOptionsPlugIn);
                                    geopistaEditor.addPlugIn(geopistaInstallGridPlugIn);

                                    ChangeSLDStylesPlugIn changeSLDStylesPlugIn = new ChangeSLDStylesPlugIn();
                                    geopistaEditor.addPlugIn(changeSLDStylesPlugIn);


                                    GeopistaScaleBarPlugIn geopistaScaleBarPlugIn = new GeopistaScaleBarPlugIn();
                                    geopistaEditor.addPlugIn(geopistaScaleBarPlugIn);

                                    //geopistaEditor.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.ViewAttributesPlugIn","Row.gif");


                                    geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
                                    geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
                                    geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");

                                    if (editable) geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

                                    String sUrlPrefix = CConstantesComando.adminCartografiaUrl;
                                    AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(sUrlPrefix + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME);
									ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
                                    Integer srid = new Integer(acClient.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad())));
                                    if (geopistaEditor.getLayerManager().getLayer("parcelas")==null)
                                    {
                                        GeopistaLayer layer=acClient.loadLayer(new GeopistaMap(),"parcelas","es_ES",null,FilterLeaf.equal("1",new Integer(1)),srid);
                                        logger.info("layer.getName(): " + layer.getName());
                                        layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
                                        geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
                                        layer.setActiva(false);
                                    }
                                    if (geopistaEditor.getLayerManager().getLayer("ocupaciones")==null)
                                    {
                                        GeopistaLayer layer= acClient.loadLayer(null,"ocupaciones", "es_ES", null, FilterLeaf.equal("1", new Integer(1)),srid);
                                        logger.info("layer: " + layer);
                                        logger.info("layer.getName(): " + layer.getName());
                                        layer.addStyle(new BasicStyle(new Color(255, 128, 69)));
                                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.SquareVertexStyle());
                                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle());
                                        layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
                                        geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
                                        layer.setActiva(true);
                                        layer.setEditable(true);
                                    }
                                    if (geopistaEditor.getLayerManager().getLayer("numeros_policia")==null)
                                    {
                                        GeopistaLayer layer= acClient.loadLayer(null,"numeros_policia", "es_ES", null, FilterLeaf.equal("1", new Integer(1)),srid);
                                        logger.info("layer: " + layer);
                                        logger.info("layer.getName(): " + layer.getName());
                                        layer.addStyle(new BasicStyle(new Color(132, 20, 30)));
                                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.SquareVertexStyle());
                                        layer.addStyle(new com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle());
                                        layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
                                        geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
                                        layer.setActiva(false);
                                    }
                                    geopistaEditor.getSelectionManager().clear();
                                    geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                                    panel.add(geopistaEditor);
                		} catch (Exception ex) {
                            logger.error("Exception: " , ex);
                    	}
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);
       return true;
	}


	public static LayerManager getLayerManagerFromFile(Layer layer, String fileName) throws Exception {
		FileReader reader = new FileReader(new File(fileName));
		try {

			Task sourceTask = (Task) new XML2Java().read(reader, Task.class);
			sourceTask.setProjectFile(new File(fileName));
			Category categoria = (Category) sourceTask.getCategories().iterator().next();
			logger.debug("categoria.getName(): " + categoria.getName());
			logger.debug("categoria.getLayerables.size(): " + categoria.getLayerables().size());
			logger.debug("categoria.getLayerManager().getLayers().size(): " + categoria.getLayerManager().getLayers().size());

			java.util.List list = categoria.getLayerables();
			logger.info("list: " + list);
			Iterator it = list.iterator();
			Layer layer0 = null;
			while (it.hasNext()) {
				layer0 = (Layer) it.next();
				logger.debug("layer0.getName(): " + layer0.getName());
			}

			logger.debug("layer: " + layer);
			logger.debug("layer0: " + layer0);
			logger.debug("layer0.getName(): " + layer0.getName());

			layer.setLayerManager(sourceTask.getLayerManager());
			sourceTask.getLayerManager().addLayer("23", layer);
//			Layer auxLayer=(Layer)categoria.getLayerManager().getLayers().iterator().next();
			layer.setStyles(layer0.cloneStyles());
			return (LayerManager) sourceTask.getLayerManager();

		} finally {
			reader.close();
		}
	}


	public static boolean clearTable(DefaultTableModel tableModel) {

		try {
			int count = tableModel.getRowCount();
			logger.info("count: " + count);

			for (int i = 0; i < count; i++) {
				tableModel.removeRow(0);
			}


			return true;

		} catch (Exception ex) {
			logger.info("Exception: " + ex.toString());
			return false;
		}
	}


	public static boolean showSearchDialog(JFrame frame) {

		CSearchJDialog dialog = new CSearchJDialog(frame, true, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}

	public static CSearchJDialog showSearchDialog(JFrame frame, boolean editable) {

		CSearchJDialog dialog = new CSearchJDialog(frame, true, editable);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return dialog;
	}

	public static boolean showDialog(JDialog dialog) {

		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}


    public static CSearchLicenciasObraJDialog showSearchLicenciasObraDialog(JFrame frame) {

    	CSearchLicenciasObraJDialog dialog = new CSearchLicenciasObraJDialog(frame, true);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return dialog;
    }


	public static CCalendarJDialog showCalendarDialog(JFrame frame) {

		CCalendarJDialog dialog = new CCalendarJDialog(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return dialog;
	}

	public static CAddressJDialog showAddressDialog(JFrame frame) {

		CAddressJDialog dialog = new CAddressJDialog(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return dialog;
	}

	public static CPersonaJDialog showPersonaDialog(JFrame frame) {

		CPersonaJDialog dialog = new CPersonaJDialog(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return dialog;
	}


	public static boolean showHistoricoDialog(JFrame frame) {

		CHistoricoJDialog dialog = new CHistoricoJDialog(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}


    public static CHistoricoJDialog showHistoricoDialog(JFrame frame, CHistorico historico) {

        CHistoricoJDialog dialog = new CHistoricoJDialog(frame, true, historico);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return dialog;
    }



    public static CDatosReferenciaCatastralJDialog showDatosReferenciaCatastralDialog(JFrame frame, CReferenciaCatastral referencia, boolean mostrarCheck) {

        CDatosReferenciaCatastralJDialog dialog = new CDatosReferenciaCatastralJDialog(frame, true, referencia, mostrarCheck);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return dialog;
    }


	public static boolean validarNif(String nif) {

		try {

			if ((nif == null) || (nif.trim().equals(""))) {
				logger.info("Nif vacio. nif: " + nif);
				return false;
			}

			nif = nif.trim().toUpperCase();

			if ((nif.indexOf("-") != -1) || (nif.indexOf(" ") != -1) || (nif.indexOf("_") != -1)) {
				logger.info("No se permiten guiones ni espacios. nif: " + nif);
				return false;
			}

			int dni = 0;
			String letra = "";

			try {

				letra = nif.substring(nif.length() - 1, nif.length());
				logger.info("letra: " + letra);

				nif = nif.substring(0, nif.length() - 1);
				logger.info("nif: " + nif);

				dni = Integer.parseInt(nif);
				logger.info("dni: " + dni);

			} catch (Exception ex) {
				logger.info("Es necesario formato 12345678X. nif: " + nif + " " + ex.toString());
				return false;


			}


			String cadena = new String("TRWAGMYFPDXBNJZSQVHLCKE");
			char[] letras = cadena.toCharArray();

			int res = dni % 23;
			String letraValida = "" + letras[res];

			if (!letra.equalsIgnoreCase(letraValida)) {
				logger.info("Letra incorrecta.");
				return false;

			}

			logger.info("Nif valido.");

			return true;

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.toString());
			return false;
		}
	}


	public static void main(String[] args) {
		validarNif("50458169X");
	}


	public static boolean GetURLFile(String urlString, String localFileName, String proxyServer, int proxyPort) {
		BufferedInputStream fileStream = null;
		RandomAccessFile outFile = null;

		try {
			URL theUrl;

			if ((proxyServer.length() > 0) && (proxyPort > 0)) {
				// use HTTP proxy, even for FTP downloads
				theUrl = new URL("http", proxyServer, proxyPort, urlString);
			} else {
				theUrl = new URL(urlString);
			}


			System.out.println("Attempting to connect to " + theUrl);

			// go get the file
			URLConnection con = theUrl.openConnection();

			// if we were able to connect (we would have errored out
			// by now if not), try to get the file.
			// Use a BufferedInputStream instead of a BufferedReader,
			// because a BufferedReader won't retrieve non-text files
			// properly
			fileStream = new BufferedInputStream(con.getInputStream());

			// if we got the remote file, create the local file that
			// we can write the information to
			outFile = new RandomAccessFile(localFileName, "rw");

			System.out.println("Downloading to local file " + localFileName);

			// write to the file in bytes (in case it's not text)
			int howManyBytes;
			byte[] bytesIn = new byte[4096];
			while ((howManyBytes = fileStream.read(bytesIn)) >= 0) {
				outFile.write(bytesIn, 0, howManyBytes);
				//stringBuf.append(bytesIn, 0, howManyBytes); // to send to a StringBuffer
				//System.out.write(bytesIn, 0, howManyBytes);  // to send to the console
			}

			// close up the streams
			fileStream.close();
			outFile.close();

			System.out.println("Finished downloading file to " + localFileName);
			return true;


		} catch (MalformedURLException e) {
			System.out.println("ERROR: Invalid URL: " + urlString);
		} catch (NoRouteToHostException e) {
			System.out.println("ERROR: URL cannot be reached: " + urlString);
		} catch (ConnectException e) {
			System.out.println("ERROR: Connection error: " + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException. Fichero/Path no se encuentra en origen/destino.");
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println(e.getMessage());
		} finally {
			// make sure the streams got closed, in case of an error
			try {
				fileStream.close();
				outFile.close();
			} catch (Exception e) {
			}
		}

		//if we got here, there was some kind of error
		return false;

	}


	public static Collection searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value) {
		Collection finalFeatures = new ArrayList();

		List allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext()) {
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreAtributo = localFeature.getString(attributeName).trim();

			if (nombreAtributo.equals(value)) {
				finalFeatures.add(localFeature);
			}
		}

		return finalFeatures;
	}

    public static void menuLicenciasSetEnabled(boolean b, JFrame desktop) {
        try {
            desktop.getJMenuBar().getMenu(0).setEnabled(b);
            desktop.getJMenuBar().getMenu(1).setEnabled(b);
            desktop.getJMenuBar().getMenu(2).setEnabled(b);
            desktop.getJMenuBar().getMenu(3).setEnabled(b);
            desktop.getJMenuBar().getMenu(4).setEnabled(b);
            desktop.getJMenuBar().getMenu(5).setEnabled(b);

            if (b){
                ((CMainOcupaciones)desktop).resetSecurityPolicy();
                GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Ocupaciones");
                ((CMainOcupaciones)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
           }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }

    public static boolean checkEstadosCombo(CExpedienteLicencia expedienteLicencia, JComboBox comboBox) {

        try {

            comboBox.setEnabled(false);

            if (expedienteLicencia==null){
                return false;
            }

            Vector estadosPermitidos = COperacionesLicencias.getEstadosPermitidos(expedienteLicencia,
                                                                    CConstantesComando.TIPO_OCUPACION);
            for (int i = 0; i < estadosPermitidos.size(); i++) {
                CEstado estado = (CEstado) estadosPermitidos.elementAt(i);

                if (expedienteLicencia.getEstado().getIdEstado() == estado.getIdEstado()) {
                    logger.info("Estado permitido.");
                    comboBox.setEnabled(true);
                    return true;
                }


            }
            logger.info("Estado no permitido.");
            return false;

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }


    }

    public static String obtenerDescripcionEstado(CExpedienteLicencia expedienteLicencia, int tipo) {

        try {

            if (expedienteLicencia==null){
                return null;
            }

            Vector estadosPermitidos = COperacionesLicencias.getEstadosPermitidos(expedienteLicencia, tipo);
            for (int i = 0; i < estadosPermitidos.size(); i++) {
                CEstado estado = (CEstado) estadosPermitidos.elementAt(i);

                if (expedienteLicencia.getEstado().getIdEstado() == estado.getIdEstado()) {
                    return estado.getDescripcion();
                }
            }
            return null;

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;
        }


    }

    public static String getLabelConAsterisco(String s){
        String labelText= "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + " " + s + "</p></html>";

        return labelText;
    }

    public static String getLabelConAsterisco(String s1, String s2){
        String labelText= "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + s1 + " / " + "<FONT COLOR=\"#FF0000\">*</FONT>" + s2 + "</p></html>";

        return labelText;
    }


    public static String annadirAsterisco(String s){
        String asterisco= "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\" size=\"5\"><b>*</b></FONT></p></html>";

        return asterisco;
    }


    public static boolean expedienteBloqueado(String numExpediente, String locale){
        CResultadoOperacion ro = COperacionesLicencias.getExpedienteLicencia(numExpediente, locale, null);
        if (ro != null) {
            if ((ro.getSolicitudes() != null) && (ro.getExpedientes() != null)) {
                CSolicitudLicencia solicitud = (CSolicitudLicencia) ro.getSolicitudes().get(0);
                CExpedienteLicencia expediente = (CExpedienteLicencia) ro.getExpedientes().get(0);

                if ((solicitud != null) && (expediente != null)) {
                    if (expediente.bloqueado()){
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static int mostrarMensajeBloqueo(Component component, boolean fromMenu){
        if (fromMenu){
            Object[] options = {CMainOcupaciones.literales.getString("Bloqueo.mensaje1"), CMainOcupaciones.literales.getString("Bloqueo.mensaje2")};
            int n= JOptionPane.showOptionDialog(component,
                    CMainOcupaciones.literales.getString("Bloqueo.mensaje3"),
                    CMainOcupaciones.literales.getString("Bloqueo.mensaje4"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    CUtilidadesComponentes.iconoBloqueo, //don't use a custom Icon
                    options, //the titles of buttons
                    options[1]); //default button title

            return n;
        }else return 0;
    }


    public static void mostrarMensajeBloqueoAceptacion(Component component, String literal){
        Object[] options = {CMainOcupaciones.literales.getString("Bloqueo.mensaje6")};
        int n= JOptionPane.showOptionDialog(component,
                literal,
                CMainOcupaciones.literales.getString("Bloqueo.mensaje4"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                CUtilidadesComponentes.iconoBloqueo, //don't use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

    }


    public static void generarFichaHistorico(JFrame frame, Vector vHistorico, CExpedienteLicencia expediente, CSolicitudLicencia solicitud, File selectedFile, ResourceBundle literales){
        try{
            // Eliminamos la extension, si la tiene
            String fileName= selectedFile.getName();
            String path= selectedFile.getAbsolutePath();
            int index= path.indexOf(fileName);
            if (index != -1){
                path= path.substring(0, index);
            }

            // Siempre sera un fichero .txt
            index= fileName.indexOf(".");
            if (index != -1){
                String name= fileName.substring(0, index);
                name+= ".txt";
                selectedFile= new File(path+name);
            }else{
                // Annadimos la extension .txt
                fileName+= ".txt";
                selectedFile= new File(path+fileName);
            }
            // Si ya existe, borramos el fichero de datos
            if (selectedFile.exists()) {
              selectedFile.delete();
            }

            // creamos el fichero de datos delimitado por comas
            if (vHistorico != null){
                // creamos el fichero de datos delimitado por comas
                Writer stream= new FileWriter(selectedFile, true);

                String cabecera= CUtilidadesComponentes.getCabeceraFicha(expediente, solicitud,
                                                literales);
                stream.write(cabecera);
                stream.write(10);
                stream.write(10);

                for (Enumeration e= vHistorico.elements(); e.hasMoreElements();){
                    CHistorico historico= (CHistorico)e.nextElement();
                    String datos= ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(
                                  historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()) + ", " +
                                  historico.getUsuario() + ", " +
                                  CUtilidades.formatFechaH24(historico.getFechaHistorico()) + ", " +
                                  historico.getApunte();

                    stream.write(datos);
                    // salto de linea
                    stream.write(10);
                }
                stream.close();
                JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje3"));

            }else{
                // El expediente no tiene historico
                JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje4"));
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje2"));

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

    }

    public static String getCabeceraFicha(CExpedienteLicencia expediente, CSolicitudLicencia solicitud,
                                                 ResourceBundle literales){

        String titulo= literales.getString("CMantenimientoHistorico.generarFicha.titulo1")  + " " + expediente.getNumExpediente();
        titulo+= "\n";
        String emplazamiento= "";
        Vector vRef= solicitud.getReferenciasCatastrales();
        if (vRef != null){
            boolean encontrado= false;
            Enumeration e = vRef.elements();
            while ((e.hasMoreElements()) && (!encontrado)){
                CReferenciaCatastral refCatastral= (CReferenciaCatastral)e.nextElement();
                if (refCatastral != null){
                    emplazamiento= CUtilidades.componerCampo(refCatastral.getTipoVia(), refCatastral.getNombreVia(), refCatastral.getPrimerNumero());
                    if (emplazamiento.trim().length() > 0) encontrado= true;
                }
            };
        }

        titulo+= literales.getString("CMantenimientoHistorico.generarFicha.titulo2")  + " " +  emplazamiento;
        titulo+= "\n";
        titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo2")  + " ";
        String tipoOcupacion= "";
        try{
            if (expediente.getDatosOcupacion() != null){
                tipoOcupacion= ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(expediente.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(literales.getLocale().toString());
            }else if (solicitud.getDatosOcupacion() != null){
                tipoOcupacion= ((DomainNode)Estructuras.getListaTipoOcupacion().getDomainNode(new Integer(solicitud.getDatosOcupacion().getTipoOcupacion()).toString())).getTerm(literales.getLocale().toString());
            }
        }catch(Exception e){logger.error("ERROR al recoger los datos de ocupación del expediente.");}
        titulo+= tipoOcupacion;

        // salto de linea
        titulo+= "\n\n";
        titulo+= literales.getString("CMantenimientoHistorico.generarFicha.columna1") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna2") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna3") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna4");

        return titulo;

    }


    public static void generarFichaHistoricoMenu(JFrame frame, Vector vHist, Vector vExp, Vector vSol, File selectedFile,
                                                 ResourceBundle literales){
        try{
            // Eliminamos la extension, si la tiene
            String fileName= selectedFile.getName();
            String path= selectedFile.getAbsolutePath();
            int index= path.indexOf(fileName);
            if (index != -1){
                path= path.substring(0, index);
            }

            // Siempre sera un fichero .txt
            index= fileName.indexOf(".");
            if (index != -1){
                String name= fileName.substring(0, index);
                name+= ".txt";
                selectedFile= new File(path+name);
            }else{
                // Annadimos la extension .txt
                fileName+= ".txt";
                selectedFile= new File(path+fileName);
            }
            // Si ya existe, borramos el fichero de datos
            if (selectedFile.exists()) {
              selectedFile.delete();
            }

            // creamos el fichero de datos delimitado por comas
            Writer stream= new FileWriter(selectedFile, true);

            Hashtable hExpedientes= new Hashtable();

            /** Recogemos los historicos por numero de expediente, ya que pueden venir desordenados,
               debido a que la búsqueda se ordena por fecha. */
            for (int i= 0; i < vExp.size(); i++){
                com.geopista.protocol.licencias.CHistorico historico= (com.geopista.protocol.licencias.CHistorico)vHist.get(i);
                CExpedienteLicencia expediente= (CExpedienteLicencia)vExp.get(i);
                CSolicitudLicencia solicitud= (CSolicitudLicencia)vSol.get(i);

                if ((historico != null) && (expediente != null) && (solicitud != null)){
                    if (hExpedientes.get(expediente.getNumExpediente()) == null){
                        Vector v= new Vector();
                        v.add(0, expediente);
                        v.add(1, solicitud);
                        v.add(v.size(), historico);
                        hExpedientes.put(expediente.getNumExpediente(), v);

                    }else{
                        Vector v= (Vector)hExpedientes.get(expediente.getNumExpediente());
                        v.add(v.size(), historico);
                        hExpedientes.put(expediente.getNumExpediente(), v);
                    }
                }
            }

            /** Generamos el listado */
            for (Enumeration e= hExpedientes.keys(); e.hasMoreElements();){
                String key= (String)e.nextElement();
                Vector v= (Vector)hExpedientes.get(key);
                CExpedienteLicencia expediente= (CExpedienteLicencia)v.get(0);
                CSolicitudLicencia solicitud= (CSolicitudLicencia)v.get(1);
                String cabecera= getCabeceraFicha(expediente, solicitud,literales);
                stream.write(cabecera);
                stream.write(10);
                stream.write(10);

                for (int i= 2; i < v.size(); i++){
                    CHistorico historico= (CHistorico)v.get(i);
                    String datos= ((DomainNode)Estructuras.getListaEstadosOcupacion().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()) + ", " +
                                  historico.getUsuario() + ", " +
                                  CUtilidades.formatFechaH24(historico.getFechaHistorico()) + ", " +
                                  historico.getApunte();

                    stream.write(datos);
                    // salto de linea
                    stream.write(10);
                }
                stream.write(10);
            }
            stream.close();
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje3"));

        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje2"));

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }
    }


    public static boolean hayDatosPersonaJuridicoFisica(String dni, String nombre, String nombreVia, String numeroVia, String cpostal, String municipio, String provincia){
        /** Comprobamos si se han insertado alguno de los datos obligatorios de la persona */
        if ((dni.length() > 0) || (nombre.length() > 0) ||
            (nombreVia.length() > 0) || (numeroVia.length() > 0) ||
            (cpostal.length() > 0) || (municipio.length() > 0) ||
            (provincia.length() > 0)) return true;

        return false;
    }

    public static boolean isWindows(){
           String osName = System.getProperty ("os.name");
           osName = osName.toLowerCase();
           return osName.indexOf ("windows") != -1;
    }	//	isWindows

    public static String obtenerTipoLicencia(CSolicitudLicencia solicitudLicencia) {

        try {

            if (solicitudLicencia==null){
                return null;
            }

            Vector tiposPermitidos = COperacionesLicencias.getTiposLicencia();
            for (int i = 0; i < tiposPermitidos.size(); i++) {
                CTipoLicencia tipoLicencia = (CTipoLicencia) tiposPermitidos.elementAt(i);

                if (solicitudLicencia.getTipoLicencia().getIdTipolicencia() == tipoLicencia.getIdTipolicencia()) {
                    return tipoLicencia.getDescripcion();
                }
            }
            return null;

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;
        }


    }

    public static String getAnexoId(String fileName, Long idSolicitud){    	
    	try {
            if (fileName!=null){
            	return COperacionesLicencias.getAnexoId(fileName, idSolicitud);  
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;
        }
    	return null;
    }

}
