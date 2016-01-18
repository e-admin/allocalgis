package com.geopista.app.licencias;

import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaOptionsPlugIn;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarPlugIn;
import com.geopista.ui.snap.GeopistaInstallGridPlugIn;
import com.geopista.security.GeopistaAcl;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaFeature;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * @author SATEC
 * @version $Revision: 1.5 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/07/04 07:01:29 $
 *          $Name:  $
 *          $RCSfile: CUtilidadesComponentes.java,v $
 *          $Revision: 1.5 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CUtilidadesComponentes {

	private static Logger logger = Logger.getLogger(CUtilidadesComponentes.class);
    public static Icon iconoZoom;
    public static Icon iconoFlechaDerecha;
    public static Icon iconoFlechaIzquierda;
    public static Icon iconoDeleteParcela;
    public static Icon iconoOK;
    public static Icon iconoAdd;
    public static Icon iconoRemove;
    public static Icon iconoAbrir;
    public static Icon iconoEditar;
    public static Icon iconoBloqueo;
    public static Icon iconoGenerarFicha;
    public static Icon iconoDocumento;
    public static Icon iconoDocumentoNegativo;
    public static Icon iconoExpediente;
    public static Icon iconoSolicitud;
    public static Icon iconoPersona;
    public static Icon iconoRepresentante;
    public static Icon iconoNotificacion;
    public static Icon iconoEvento;
    public static Icon iconoHistorico;
    public static Icon iconoDocumentacion;
    public static Icon iconoInformes;
    public static Feature[] cache=null;


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

	public static boolean showGeopistaEditor(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, boolean editable)

    {
          try
          {
    		geopistaEditor.showLayerName(false);
			ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
			geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);
            try {
			    GeopistaPrintPlugIn geopistaPrintPlugIn = new GeopistaPrintPlugIn();
			    geopistaEditor.addPlugIn(geopistaPrintPlugIn);
                SeriePrintPlugIn seriePrintPlugIn = new SeriePrintPlugIn();
			    geopistaEditor.addPlugIn(seriePrintPlugIn);
            }catch(Exception e)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al cargar el plugin de impresión. Contactar con Inzamac: " + sw.toString());
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

			GeopistaScaleBarPlugIn geopistaScaleBarPlugIn = new GeopistaScaleBarPlugIn();
			geopistaEditor.addPlugIn(geopistaScaleBarPlugIn);
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
            geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
            geopistaEditor.addCursorTool("Dynamic clip tool", "com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicClipTool");
			String sUrlPrefix = CConstantesComando.adminCartografiaUrl;
			AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(sUrlPrefix + "AdministradorCartografiaServlet");
			GeopistaLayer layer = acClient.loadLayer(new GeopistaMap(), "licencias", "es_ES", null, FilterLeaf.equal("1", new Integer(1)));

            if (layer.getName()==null || layer.getName().equals("error"))
            {
                     throw new PermissionException("PermissionException: " + com.geopista.server.administradorCartografia.Const.PERM_LAYER_READ);
            }
            logger.info("layer: " + layer);
            logger.info("layer.getName(): " + layer.getName());
            logger.info("layer.getSystemId(): " + layer.getSystemId());

            layer.setLayerManager(geopistaEditor.getContext().getLayerManager());
            geopistaEditor.getLayerManager().addLayer(layer.getName(), layer);
            layer.setEditable(true);
            layer.setActiva(true);
            panel.add(geopistaEditor);
        	return true;
          }catch(Exception e)
          {
              String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
             Throwable t=e.getCause();
              int i=0;
              while (t!=null&&i<10)
              {
                  sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
                  t=t.getCause();
                  i++;
              }
              JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
              logger.error("Error al cargar la capa de licencias: ",e);
              return false;
          }

	}
    public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map, boolean editable,TaskMonitor monitor) {
        return showGeopistaMap(padre, panel, geopistaEditor, id_map, editable, null, null,monitor);
    }

    public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map, boolean editable) {
        return showGeopistaMap(padre, panel, geopistaEditor, id_map, editable, null, null,null);
    }

    public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map, boolean editable, String visible, String novisible){
        return showGeopistaMap(padre, panel, geopistaEditor, id_map, editable,visible, novisible,null);
    	
    }
    public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map, boolean editable, String visible, String novisible,TaskMonitor monitor){
              try{
                geopistaEditor.showLayerName(false);
                ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
                geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);
                try {
                    GeopistaPrintPlugIn geopistaPrintPlugIn = new GeopistaPrintPlugIn();
                    geopistaEditor.addPlugIn(geopistaPrintPlugIn);
                    SeriePrintPlugIn seriePrintPlugIn = new SeriePrintPlugIn();
                    geopistaEditor.addPlugIn(seriePrintPlugIn);
                }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("Exception al cargar el plugin de impresión. Contactar con Inzamac: " + sw.toString());
                }
                if (editable){
                    GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
                    geopistaEditor.addPlugIn(geopistaEditingPlugIn);
                }
                //GeopistaInstallGridPlugIn geopistaInstallGridPlugIn = new GeopistaInstallGridPlugIn();
                //GeopistaOptionsPlugIn geopistaOptionsPlugIn = new GeopistaOptionsPlugIn();
                /** INZAMAC */  
                //geopistaEditor.addPlugIn(geopistaOptionsPlugIn);
                //geopistaEditor.addPlugIn(geopistaInstallGridPlugIn);

                GeopistaScaleBarPlugIn geopistaScaleBarPlugIn = new GeopistaScaleBarPlugIn();
                geopistaEditor.addPlugIn(geopistaScaleBarPlugIn);
                geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
                geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
                geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
                geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
                geopistaEditor.addCursorTool("Dynamic clip tool", "com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicClipTool");
                geopistaEditor.addCursorTool("Dynamic fence tool", "com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicFenceTool");

                 /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo */
                 if ((geopistaEditor.getLayerManager().getLayers() != null) && (geopistaEditor.getLayerManager().getLayers().size() > 0)){
                     ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
                     geopistaEditor.getSelectionManager().clear();
                     geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                     panel.add(geopistaEditor);
                     return true;
                 }
                 if (ShowMaps.showMap(CConstantesComando.adminCartografiaUrl,geopistaEditor, id_map, true,padre,monitor)!=null){
                    try{
                        for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();){
                            GeopistaLayer layer=(GeopistaLayer)it.next();
                            layer.setEditable(false);
                            layer.setActiva(false);
                        }
                    }catch(Exception e){}
                    ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
                    geopistaEditor.getSelectionManager().clear();
                    geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                    panel.add(geopistaEditor);
                    return true;
                }
                return false;

              }catch(Exception e){
                  String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
                  Throwable t=e.getCause();
                  int i=0;
                  while (t!=null&&i<10){
                      sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
                      t=t.getCause();
                      i++;
                  }
                  JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
                  logger.error("Error al cargar el mapa de licencias: ",e);
                  return false;
              }
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
			return sourceTask.getLayerManager();

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


	public static boolean showSearchDialog(JFrame frame, ResourceBundle literales) {

		CSearchJDialog dialog = new CSearchJDialog(frame, true, true,literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}

    public static boolean showSearchDialog(JFrame frame, String patronLicencia, String patronObra, String patronEstado,
                                           String dni, ResourceBundle literales) {

        CSearchJDialog dialog = new CSearchJDialog(frame, true, true, patronLicencia, patronObra, patronEstado, dni,
                                          literales);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return true;
    }

    public static boolean showSearchDialog(JFrame frame, String patronLicencia, String patronObra, String patronEstado,
                                           String dni, boolean editable, ResourceBundle literales) {

        CSearchJDialog dialog = new CSearchJDialog(frame, true, editable, patronLicencia, patronObra, patronEstado, dni,
                                           literales);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return true;
    }

    public static boolean showSearchDialog(JFrame frame, String patronLicencia, String patronObra, String patronEstado,
                                           String dni, boolean editable, ResourceBundle literales, boolean buscarObra) {

        CSearchJDialog dialog = new CSearchJDialog(frame, true, editable, patronLicencia, patronObra, patronEstado, dni,
                                           literales, buscarObra);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return true;
    }



	public static boolean showSearchDialog(JFrame frame, boolean editable,ResourceBundle literales) {

		CSearchJDialog dialog = new CSearchJDialog(frame, true, editable, literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}

	public static boolean showDialog(JDialog dialog) {

		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}


	public static java.util.Date showCalendarDialog(JFrame frame) {

		CCalendarJDialog dialog = new CCalendarJDialog(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
		return dialog.getFecha();
	}

	public static boolean showAddressDialog(JFrame frame, ResourceBundle literales) {

		CAddressJDialog dialog = new CAddressJDialog(frame, true, literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}

    public static boolean showReferenciaCatastralDialog(JFrame frame, ResourceBundle literales) {
        return showReferenciaCatastralDialog(frame, literales, -1);
    }

    public static boolean showReferenciaCatastralDialog(JFrame frame, ResourceBundle literales, int modoSeleccion) {
        CReferenciaCatastralJDialog dialog = new CReferenciaCatastralJDialog(frame, true,literales, modoSeleccion);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return true;
    }

    /*
	public static boolean showReferenciaCatastralDialog(JFrame frame, ResourceBundle literales) {

		CReferenciaCatastralJDialog dialog = new CReferenciaCatastralJDialog(frame, true,literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
	}
    */

	public static boolean showPersonaDialog(JFrame frame, ResourceBundle literales) {

		CPersonaJDialog dialog = new CPersonaJDialog(frame, true, literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
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


    public static CHistoricoJDialog showHistoricoDialog(JFrame frame, CHistorico historico, ResourceBundle literales) {

        CHistoricoJDialog dialog = new CHistoricoJDialog(frame, true, historico, literales);
        dialog.setLocation(20, 20);
        dialog.setResizable(false);
        dialog.show();

        return dialog;
    }


	public static boolean showDatosReferenciaCatastralDialog(JFrame frame, boolean b, ResourceBundle literales) {

		CDatosReferenciaCatastralJDialog dialog = new CDatosReferenciaCatastralJDialog(frame, true, b,literales);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();

		return true;
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
            /** charo comenta para hacer una prueba */

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
                    ((IMainLicencias)desktop).resetSecurityPolicy();
                    GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Licencias de Obra");
                    ((IMainLicencias)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
           }
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	public static boolean checkEstadosCombo(CExpedienteLicencia expedienteLicencia, JComboBox comboBox, int idTipoLicencia) {

		try {

			comboBox.setEnabled(false);

			if (expedienteLicencia==null){
				return false;
			}

			Vector estadosPermitidos= COperacionesLicencias.getEstadosPermitidos(expedienteLicencia, idTipoLicencia);
			for (int i = 0; i < estadosPermitidos.size(); i++) {
				CEstado estado = (CEstado) estadosPermitidos.elementAt(i);

				if (expedienteLicencia.getEstado().getIdEstado() == estado.getIdEstado()) {
					logger.debug("Estado permitido. IdEstado=" + expedienteLicencia.getEstado().getIdEstado());
					comboBox.setEnabled(true);
					return true;
				}


			}
            logger.debug("Estado no permitido.IdEstado=" + expedienteLicencia.getEstado().getIdEstado());
			return false;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}


	}
    public static void inicializar()
        {
            try
            {
                ClassLoader cl= (new CUtilidadesComponentes()).getClass().getClassLoader();
                iconoZoom = new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
                iconoFlechaDerecha = new javax.swing.ImageIcon(cl.getResource("img/flecha_derecha.gif"));
                iconoFlechaIzquierda = new javax.swing.ImageIcon(cl.getResource("img/flecha_izquierda.gif"));
                iconoDeleteParcela = new javax.swing.ImageIcon(cl.getResource("img/delete_parcela.gif"));
                iconoOK= new javax.swing.ImageIcon(cl.getResource("img/ok.gif"));
                iconoAdd= new javax.swing.ImageIcon(cl.getResource("img/add.gif"));
                iconoRemove= new javax.swing.ImageIcon(cl.getResource("img/remove.gif"));
                iconoAbrir= new javax.swing.ImageIcon(cl.getResource("img/abrir.gif"));
                iconoEditar= new javax.swing.ImageIcon(cl.getResource("img/editar.gif"));
                iconoBloqueo= new javax.swing.ImageIcon(cl.getResource("img/bloqueo.gif"));
                iconoGenerarFicha= new javax.swing.ImageIcon(cl.getResource("img/generar_ficha.gif"));
                iconoDocumento= new javax.swing.ImageIcon(cl.getResource("img/documento.jpg"));
                iconoDocumentoNegativo= new javax.swing.ImageIcon(cl.getResource("img/documento-.jpg"));
                iconoExpediente= new javax.swing.ImageIcon(cl.getResource("img/expediente.jpg"));
                iconoSolicitud= new javax.swing.ImageIcon(cl.getResource("img/solicitud.jpg"));
                iconoPersona= new javax.swing.ImageIcon(cl.getResource("img/persona.jpg"));
                iconoRepresentante= new javax.swing.ImageIcon(cl.getResource("img/representante.jpg"));
                iconoNotificacion= new javax.swing.ImageIcon(cl.getResource("img/notificacion.jpg"));
                iconoEvento= new javax.swing.ImageIcon(cl.getResource("img/evento.jpg"));
                iconoHistorico= new javax.swing.ImageIcon(cl.getResource("img/historico.jpg"));
                iconoDocumentacion= new javax.swing.ImageIcon(cl.getResource("img/documentacion.jpg"));
                iconoInformes= new javax.swing.ImageIcon(cl.getResource("img/informes.jpg"));

            }catch(Exception e)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al inicializar las imagenes: " + sw.toString());
            }
        }

    public static void mostrarMensajeBloqueoAceptacion(Component component, String literal, ResourceBundle literales){
        Object[] options = {literales.getString("Licencias.mensaje6")};
        JOptionPane.showOptionDialog(component,
                literal,
                literales.getString("Licencias.mensaje2"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                CUtilidadesComponentes.iconoBloqueo, //don't use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

    }

    public static int mostrarMensajeBloqueo(Component component, ResourceBundle literales, boolean fromMenu){
        if (fromMenu){
            Object[] options = {literales.getString("Licencias.mensaje3"), literales.getString("Licencias.mensaje4")};
            int n= JOptionPane.showOptionDialog(component,
                    literales.getString("Licencias.mensaje9"),
                    literales.getString("Licencias.mensaje2"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    CUtilidadesComponentes.iconoBloqueo, //don't use a custom Icon
                    options, //the titles of buttons
                    options[1]); //default button title

            return n;
        }else return 0;
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
                JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje6"));

            }else{
                // El expediente no tiene historico
                JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje7"));
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje5"));

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }

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
                    String datos= ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(historico.getEstado().getIdEstado()).toString())).getTerm(literales.getLocale().toString()) + ", " +
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
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje6"));

        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, literales.getString("CMantenimientoHistorico.mensaje5"));

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

        if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMayor){
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo1")  + " " + ((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
            titulo+= "\n";
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo2")  + " " + ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
        }else if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ObraMenor){
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo1")  + " " + ((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
            titulo+= "\n";
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo2")  + " " + ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
        }else if (solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.Actividades || solicitud.getTipoLicencia().getIdTipolicencia() == CConstantesLicencias.ActividadesNoCalificadas){
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo1")  + " " + ((DomainNode)Estructuras.getListaTiposLicenciaActividad().getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(literales.getLocale().toString());
            titulo+= "\n";
            titulo+= literales.getString("CMantenimientoHistorico.generarFicha.subtitulo2")  + " " + ((DomainNode)Estructuras.getListaTiposActividad().getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(literales.getLocale().toString());
        }

        // salto de linea
        titulo+= "\n\n";
        titulo+= literales.getString("CMantenimientoHistorico.generarFicha.columna1") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna2") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna3") + ", " +
                 literales.getString("CMantenimientoHistorico.generarFicha.columna4");

        return titulo;

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


    public static CReferenciaCatastral getReferenciaCatastral(Feature feature)
    {
            CReferenciaCatastral referenciaCatastral=new CReferenciaCatastral();
            /* NOTA: Estos atributos se recogen del schema.attributeNameToIndexMap. */
            /* Modificamos la recogida de atributos de la feature, para que los atributos nombre de via y
            tipo via se recojan de la tabla vias */
            /*
            referenciaCatastral.setReferenciaCatastral(CUtilidadesComponentes.checkNull(feature.getAttribute(2)));//"Referencia catastral")));
            referenciaCatastral.setTipoVia("");
            referenciaCatastral.setNombreVia(CUtilidadesComponentes.checkNull(feature.getAttribute(14)));//"Direccion no estructurada")));
            referenciaCatastral.setPrimerNumero(CUtilidadesComponentes.checkNull(feature.getAttribute(8)));//"Primer número")));
            referenciaCatastral.setPrimeraLetra(CUtilidadesComponentes.checkNull(feature.getAttribute(9)));//"Primera letra")));
            referenciaCatastral.setBloque(CUtilidadesComponentes.checkNull(feature.getAttribute(13)));//"Bloque")));
            referenciaCatastral.setEscalera("");
            referenciaCatastral.setPlanta("");
            referenciaCatastral.setPuerta("");
            referenciaCatastral.setCPostal("");
            */

        /* Recogemos el atributo Referencia Catastral */
        String referencia= CUtilidadesComponentes.checkNull(feature.getAttribute(2));

        Hashtable hash = new Hashtable();
        hash.put("c.referencia_catastral", referencia);        

        Vector referenciasCatastrales = COperacionesLicencias.getSearchedAddresses(hash);
        if ((referenciasCatastrales != null) && (referenciasCatastrales.size() > 0)){
            /** Recogemos los valores de las tablas parcelas y vias */
            CReferenciaCatastral refCatastral= (CReferenciaCatastral) referenciasCatastrales.elementAt(0);
            referenciaCatastral.setReferenciaCatastral(referencia);
            referenciaCatastral.setTipoVia(CUtilidadesComponentes.checkNull(refCatastral.getTipoVia()));
            referenciaCatastral.setNombreVia(CUtilidadesComponentes.checkNull(refCatastral.getNombreVia()));
            referenciaCatastral.setPrimerNumero(CUtilidadesComponentes.checkNull(refCatastral.getPrimerNumero()));
            referenciaCatastral.setPrimeraLetra(CUtilidadesComponentes.checkNull(refCatastral.getPrimeraLetra()));
            referenciaCatastral.setBloque(CUtilidadesComponentes.checkNull(refCatastral.getBloque()));
            referenciaCatastral.setEscalera(CUtilidadesComponentes.checkNull(refCatastral.getEscalera()));
            referenciaCatastral.setPlanta(CUtilidadesComponentes.checkNull(refCatastral.getPlanta()));
            referenciaCatastral.setPuerta(CUtilidadesComponentes.checkNull(refCatastral.getPuerta()));
            referenciaCatastral.setCPostal(CUtilidadesComponentes.checkNull(refCatastral.getCPostal()));
        }else{
            /** Recogemos los atributos del schema (attributeNameToIndexMap) de la feature */
            referenciaCatastral.setReferenciaCatastral(referencia);
            referenciaCatastral.setTipoVia("");
            referenciaCatastral.setNombreVia(CUtilidadesComponentes.checkNull(feature.getAttribute(14)));//"Direccion no estructurada")));
            referenciaCatastral.setPrimerNumero(CUtilidadesComponentes.checkNull(feature.getAttribute(8)));//"Primer número")));
            referenciaCatastral.setPrimeraLetra(CUtilidadesComponentes.checkNull(feature.getAttribute(9)));//"Primera letra")));
            referenciaCatastral.setBloque(CUtilidadesComponentes.checkNull(feature.getAttribute(13)));//"Bloque")));
            referenciaCatastral.setEscalera("");
            referenciaCatastral.setPlanta("");
            referenciaCatastral.setPuerta("");
            referenciaCatastral.setCPostal(CUtilidadesComponentes.checkNull(feature.getAttribute(15)));//"Codigo Postal")));
        }

         return referenciaCatastral;
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

    
    /**
     * Cuando se selecciona el emplazamiento de una licencia recién creada o bien se modifica el emplazamiento de una licencia ya existente
     * (la licencia puede ser de obra mayor, de obra menor o de actividad) se pinta de marrón la parcela que hayamos seleccionado para dicho 
     * emplazamiento. Para lograr esto lo que se hace es llamar a este método: addFeatureCapa.
     * Sin embargo, cuando se modifica el emplazamiento de una licencia creada previamente, cómo he comentado antes sí que se pinta de marrón
     * la nueva parcela, pero no se vuelve a quedar en rosa la parcela antigua. El motivo principal de este "fallo" en la funcionalidad es que
     * dicha parcela antigua puede ser el emplazamiento de otra licencia. Cuando se modifica el emplazamiento de una licencia cambiando la parcela
     * asignada no es posible comprobar si la antigua parcela es emplazamiento a su vez de otra licencia por lo que no podemos borrar su color 
     * marrón para dejarla en el tono rosado.
     */
    public static void addFeatureCapa(GeopistaEditor geopistaEditor, String layername, JTable table, DefaultTableModel model){
        GeopistaLayer layer= (GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layername);
        GeopistaSchema featureSchema= (GeopistaSchema)layer.getFeatureCollectionWrapper().getFeatureSchema();
        
        boolean isFiringEvents = layer.getLayerManager().isFiringEvents();
  	    
        
        for (int i = 0; i < table.getRowCount(); i++) {
            Object[] features= (Object[])model.getValueAt(i,10);
            if (features != null){
                for (int j=0; j<features.length; j++){
                    GeopistaFeature feature= (GeopistaFeature)features[j];
                    Feature currentFeature= new BasicFeature(featureSchema);
                    currentFeature.setGeometry(feature.getGeometry());                    
                    ((GeopistaFeature) currentFeature).setLayer(layer);
                    layer.getLayerManager().setFiringEvents(false);
                    try{
                    	layer.getFeatureCollectionWrapper().add(currentFeature);
                    }catch(Exception e){}
                   finally{
                	   layer.getLayerManager().setFiringEvents(isFiringEvents);
                   }//fin finally
                }//fin for
            }//fin if
        }//fin for
    }//fin método
        
        
        
        
        
        
        
       /*Posible método para utilizar en la siguiente situación: Cuando el usuario
		en la modificación de una licencia de obra o actividad modifica la/s parcela/s seleccionada/s como
		al emplazamiento de dicha licencia, este método podría servir para eliminar el sombreado de la
		antigua parcela seleccionada, que deberá pasar de estar sombreada a estar en su color rosa normal.
		public static void removeFeatureCapa(GeopistaEditor geopistaEditor, String layername, JTable table, DefaultTableModel model,
        		int selectedRow){
            GeopistaLayer layer= (GeopistaLayer)geopistaEditor.getLayerManager().getLayer(layername);
            GeopistaSchema featureSchema= (GeopistaSchema)layer.getFeatureCollectionWrapper().getFeatureSchema();           
            boolean isFiringEvents = layer.getLayerManager().isFiringEvents();
      	    
                Object[] features= (Object[])model.getValueAt(selectedRow,10);
                if (features != null){
                    for (int j=0; j<features.length; j++){
                        GeopistaFeature feature= (GeopistaFeature)features[j];
                        Feature currentFeature= new BasicFeature(featureSchema);
                        currentFeature.setGeometry(feature.getGeometry());
                        ((GeopistaFeature) currentFeature).setLayer(layer);
                        layer.getLayerManager().setFiringEvents(false);
                        try{
                        	layer.getFeatureCollectionWrapper().remove(currentFeature);
                        }catch(Exception e){}
                       finally{
                    	   layer.getLayerManager().setFiringEvents(isFiringEvents);
                       }//fin finally
                    }//fin for
                }//fin if
            
        }*///fin método
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    

    public static void ponSolar(GeopistaEditor geopistaEditor, Vector vRefCatastrales, String valor)
    {
        for (Enumeration e= vRefCatastrales.elements(); e.hasMoreElements();) {
            cambiaValorSolar( geopistaEditor, (String)e.nextElement(),valor);
        }
    }

    private static void cambiaValorSolar(GeopistaEditor geopistaEditor, String referenciaCatastral,String valor){
        try
        {
            GeopistaLayer layer= (GeopistaLayer)geopistaEditor.getLayerManager().getLayer("subparcelas");
            if (layer==null) return;
            GeopistaSchema featureSchema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();
            final String refCatAttName = featureSchema.getAttributeByColumn("referencia_catastral");
            final String constructAttName = featureSchema.getAttributeByColumn("constru");
            Comparator geopistaFeatureComparator = new Comparator(){
                    public int compare(Object o1, Object o2) {
                        Feature f1 = (Feature) o1;
                        Feature f2= (Feature) o2;
                        String f2ReferenciaCatastral = f2.getString(refCatAttName);
                        String f1ReferenciaCatastral = f1.getString(refCatAttName);
                        return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
                 }
            };

            List listaFeatures= layer.getFeatureCollectionWrapper().getFeatures();

            if (cache==null)
               cache=setSearchableList(listaFeatures, geopistaFeatureComparator);
            referenciaCatastral=referenciaCatastral.substring(0,7);
            Feature currentFeature = new BasicFeature(featureSchema);
            currentFeature.setAttribute(refCatAttName, referenciaCatastral);
            int featureIndex = Arrays.binarySearch(cache,currentFeature, geopistaFeatureComparator);
            //Esta función no da el primero hay que ir para alante y para atras
			if(featureIndex<0) return;
            for (int featureIndexDelante=featureIndex;cache.length>featureIndexDelante&&(referenciaCatastral.equalsIgnoreCase(((Feature)cache[featureIndexDelante]).getString(refCatAttName)));featureIndexDelante++)
            {
				((Feature)cache[featureIndexDelante]).setAttribute(constructAttName,valor);
            }
            for (int featureIndexAtras=featureIndex;featureIndexAtras>=0&&(referenciaCatastral.equalsIgnoreCase(((Feature)cache[featureIndexAtras]).getString(refCatAttName)));featureIndexAtras--)
            {
				((Feature)cache[featureIndexAtras]).setAttribute(constructAttName,valor);
            }

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private static Feature[] setSearchableList(List listaLayerParcelas,Comparator comparator)
    {
            Feature[] cache = new Feature[listaLayerParcelas.size()];
            int i=0;
            for (Iterator features = listaLayerParcelas.iterator(); features.hasNext();)
            {
                Feature feature = (Feature) features.next();
                cache[i++]=feature;
            }
            Arrays.sort(cache,comparator);
            return cache;
    }

    public static Object[] getFeatureSearched(GeopistaEditor geopistaEditor, String refCatastral) {
        try {
            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
            Collection c= CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);
            return c.toArray();
        } catch (Exception ex) {
            return null;
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

}
