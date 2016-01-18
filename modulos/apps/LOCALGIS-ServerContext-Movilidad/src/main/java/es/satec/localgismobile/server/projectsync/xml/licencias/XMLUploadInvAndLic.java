/**
 * XMLUploadInvAndLic.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.licencias;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.ui.dialogs.global.Constants_LCGIII;

import es.satec.localgismobile.server.projectsync.beans.ResultString;
import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.XMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.ItemXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.MetadataXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.TabXMLUpload;

public class XMLUploadInvAndLic extends XMLUpload {

	private static Logger logger = Logger.getLogger(XMLUploadInvAndLic.class);

	private String urlInventario;
	private InventarioClient inventarioClient;

	public XMLUploadInvAndLic() {
	}

	public XMLUploadInvAndLic(String urlInventario,
			InventarioClient inventarioClient) {
		this.urlInventario = urlInventario;
		this.inventarioClient = inventarioClient;
	}

	public String getUrlInventario() {
		return urlInventario;
	}

	public void setUrlInventario(String urlInventario) {
		this.urlInventario = urlInventario;
	}

	public InventarioClient getInventarioClient() {
		return inventarioClient;
	}

	public void setInventarioClient(InventarioClient inventarioClient) {
		this.inventarioClient = inventarioClient;
	}

	/**
	 * Inserta una feature de tipo bien
	 * 
	 * @param metadata
	 * @param results
	 * @throws Exception
	 * 
	 */
	public void insertaBien(MetadataXMLUpload metadata, ResultString results)
			throws Exception {
		String idFeat = metadata.getIdFeatures().get("id");
		// obtenemos los datos de búsqueda del inventario
		String patron = "";
		try {
			patron = getAttInvPatrimonio(metadata.getTabList(), "Tipo");
		} catch (Exception e) {
			String errorMsg = "Error id feature " + metadata.getIdFeatures().get("id")
					+ ": " + e.getMessage();
			logger.error(errorMsg);
			throw new Exception(errorMsg);
		}

		logger.info("Rellenando feature con patron: " + patron
				+ ", Id feature:" + idFeat);
		// relleno de la feature
		GeopistaFeature geoFeat = new GeopistaFeature();
		geoFeat.setSystemId(idFeat);
		GeopistaLayer geoLayer = new GeopistaLayer();
		geoLayer.setSystemId(metadata.getLayerId());
		geoFeat.setLayer(geoLayer);

		logger.info("Filling Reflect");
		// relleno del inventario
		// obtenemos un hijo de BienBean (MuebleBean, ViaBean,...)
		BienBean inventario = null;
		try {
			inventario = (BienBean) Const.CLASE_PATRONES.get(patron)
					.newInstance();
		//	inventario.setId(-1);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		inventario.setDocumentos(new ArrayList());
		Hashtable changedAttributes = reflectSetMethod(inventario, metadata);
		// insertamos el superpatron
		try {
			if (Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO.equals(inventario
					.getSuperPatron())) {
				Method method = inventario.getClass().getMethod(
						"setPatrimonioMunicipalSuelo",
						new Class[] { String.class });
				if (method != null) {
					method.invoke(inventario, "1");
				}
				logger.info("El bien es de tipo 'PatrimonioMunicipalSuel'");
			}
		} catch (Exception ex) {
		}

		logger.info("Insertando en Base de datos");

		// ****************************************************
		// inserción en BBDD del elemento. Devolvemos la clave
		// ****************************************************
		Object[] features = { geoFeat };
		Object resOp = (BienBean) inventarioClient.insertInventario(features,
				inventario);
		BienBean resultado = (BienBean) resOp;

		logger.info("Bien insertado");
		// Una vez insertado el elemento verificamos si hay que
		// actualizar algun dato que
		// se ha insertado en el servidor tal como el identificador.
		String cadenaReplace = reflectGetMethod(resultado, changedAttributes);

		// ************************************
		// Devolvemos el resultado insertado
		// ************************************
		results.setText(results.getText() + "\n<insertMetadata id=\""
				+ metadata.getId() + "\"" + " " + cadenaReplace + " />");
		// + "\" v1=\"" + resultado.getId() + "\"/>");

		logger.info("Resultado de la insercion: " + resOp);
	}

	public void modificarBien(MetadataXMLUpload metadata, ResultString results)
			throws Exception {

		// obtenemos los datos de búsqueda del inventario
		String idStr = "";
		String patron = "";
		String superPatron = "";
		try {
			idStr = getAttInvPatrimonio(metadata.getTabList(), "IdString");
			patron = getAttInvPatrimonio(metadata.getTabList(), "Tipo");
			superPatron = getAttInvPatrimonio(metadata.getTabList(),
					"SuperPatron");
		} catch (Exception e) {
			String errorMsg = "Error id feature " + metadata.getIdFeatures().get("id")
					+ ": " + e.getMessage();
			logger.error(errorMsg);
			throw new Exception(errorMsg);
		}
		long idBien = 0;
		// si el id no está bien hacemos una inserción
		try {
			idBien = Long.parseLong(idStr);
		} catch (Exception ex) {
			insertaBien(metadata, results);
			return;
		}

		int action = Const.ACCION_PATRONES.get(patron);

		// obtenemos el inventario a modificar
		logger.info("inventarioClient.getBienInventario(action, patron, idBien) "
				+ action + " " + superPatron + " " + patron + " " + idBien);
		BienBean inventario = (BienBean) inventarioClient.getBienInventario(
				action, superPatron, patron, idBien, -1, -1);
		if (inventario.getDocumentos() == null) {
			inventario.setDocumentos(new ArrayList());
		}

		// modificación del inventario
		reflectSetMethod(inventario, metadata);

		// actualización del inventario en BBDD
		Object resOp = inventarioClient.updateInventario(inventario);
		logger.info("Resultado de la modificacion: " + resOp);
	}

	public void borrarBien(MetadataXMLUpload metadata, ResultString results)
			throws Exception {
		String idStr = "";
		try {
			idStr = getAttInvPatrimonio(metadata.getTabList(), "IdString");
		} catch (Exception e) {
			String errorMsg = "Error id feature " + metadata.getIdFeatures().get("id")
					+ ": " + e.getMessage();
			logger.error(errorMsg);
			throw new Exception(errorMsg);
		}
		long idBien = Long.parseLong(idStr);

		// relleno del inventario
		BienBean inventario = new BienBean(); // obtenemos un hijo
												// de BienBean
												// (MuebleBean,
												// ViaBean,...)
		inventario.setId(idBien);

		// borrado en BBDD
		// ojo: el borrado no es eliminación, unicamente se marca
		// como borrado pero no se elimina
		Object resOp = (BienBean) inventarioClient.borrarInventario(inventario);
	}

	public void modificarLicencia(MetadataXMLUpload metadata, String systemIdLayer, CExpedienteLicencia cExpLic, String numExpediente){
		Vector<CTipoLicencia> tiposLicencia = new Vector<CTipoLicencia>();
		boolean bModificacionLicencias = true;
		if (systemIdLayer.equals(Constants_LCGIII.LIC_OBRA_MAYOR)) {
			tiposLicencia.add(new CTipoLicencia(
					Constants_LCGIII.ObraMayor_Value, "", ""));
		} else if (systemIdLayer
				.equals(Constants_LCGIII.LIC_OBRA_MENOR)) {
			tiposLicencia.add(new CTipoLicencia(
					Constants_LCGIII.ObraMenor_Value, "", ""));
		} else if (systemIdLayer
				.equals(Constants_LCGIII.LIC_ACTIVIDAD)) {
			tiposLicencia.add(new CTipoLicencia(
					Constants_LCGIII.Actividades_Value, "", ""));
		} else {
			bModificacionLicencias = false;
		}

		if (bModificacionLicencias == true) {
			// obtenemos el expediente que vamos a modificar
			CResultadoOperacion resOp = COperacionesLicencias
					.getExpedienteLicencia(numExpediente, ConstantsXMLUpload.LOCALE,
							tiposLicencia);
			Vector expedientes = null;
			Vector solicitudes = null;
			CSolicitudLicencia cSolLic = null;
			if (resOp != null) {
				expedientes = resOp.getExpedientes();
				if (expedientes != null && expedientes.size() > 0) {
					cExpLic = (CExpedienteLicencia) expedientes.get(0);
					cExpLic.setLocale(ConstantsXMLUpload.LOCALE);
					solicitudes = resOp.getSolicitudes();
					if (solicitudes != null && solicitudes.size() > 0) {
						cSolLic = (CSolicitudLicencia) solicitudes
								.get(0);
						cExpLic.setSolicitud(cSolLic);
					}
					// modificación del expediente
					try {
						reflectSetMethod(cExpLic, metadata);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// actualización del expediente en BBDD
					CResultadoOperacion resOpMod = COperacionesLicencias
							.modificarExpediente(
									cExpLic.getSolicitud(), cExpLic);
					logger.info("Resultado de la modificacion: "
							+ resOpMod.getResultado());
				}
			}
		}
	}
	
	/**
	 * Busca el atributo del inventario de patrimonio en el xml metadata
	 * 
	 * @param tabList
	 * @return
	 * @throws Exception
	 */
	public String getAttInvPatrimonio(List<TabXMLUpload> tabList, String attName)
			throws Exception {
		TabXMLUpload tabXMLUpload = null;
		List<ItemXMLUpload> itemList = null;
		ItemXMLUpload itemXMLUpload = null;
		String res = "";
		for (Iterator iterator = tabList.iterator(); iterator.hasNext();) {
			tabXMLUpload = (TabXMLUpload) iterator.next();
			if (tabXMLUpload.getClassId().equals(Constants_LCGIII.CLASSID_INVENTARIO)) {
				itemList = tabXMLUpload.getItemList();
				for (Iterator iterator2 = itemList.iterator(); iterator2
						.hasNext();) {
					itemXMLUpload = (ItemXMLUpload) iterator2.next();
					if (itemXMLUpload.getReflectMethod().equals(attName)) {
						res = itemXMLUpload.getValue();
						return res;
					}
				}
			}
		}

		throw new Exception("No se ha encontrado el atributo " + attName);
	}

	/**
	 * Ejecutar el set correspondiente al bean añadiendo el valor del nodo XML
	 * 
	 * @param itemValue
	 * @param elem
	 * @param strMethod
	 * @param objectInvoke
	 * @throws Exception
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public Hashtable reflectSetMethod(Object expOBien,
			MetadataXMLUpload metadata) throws Exception {
		List<TabXMLUpload> tabList = metadata.getTabList();
		TabXMLUpload tabXMLUpload = null;
		String strMethod = null;
		List<ItemXMLUpload> itemList = null;
		ItemXMLUpload itemXMLUpload = null;
		Object objectInvoke = null;
		Hashtable changedAttributes = null;

		String classId = null;
		for (int i = 0; i < tabList.size(); i++) {
			tabXMLUpload = tabList.get(i);
			classId = tabXMLUpload.getClassId();
			// asociamos el objeto al tipo indicado
			if (classId.equals(Constants_LCGIII.CLASSID_EXPEDIENTE)
					|| classId.equals(Constants_LCGIII.CLASSID_INVENTARIO)) {
				objectInvoke = expOBien;
			} else if (classId.equals(Constants_LCGIII.CLASSID_SOLICITUD)) {
				objectInvoke = ((CExpedienteLicencia) expOBien).getSolicitud();
			} else if (classId.equals(Constants_LCGIII.CLASSID_ACTIVIDAD)) {
				CSolicitudLicencia solicitud = ((CExpedienteLicencia) expOBien)
						.getSolicitud();
				if (solicitud != null) {
					objectInvoke = solicitud.getDatosActividad();
				}
			}
			itemList = tabXMLUpload.getItemList();
			logger.info("Reflecting items");
			changedAttributes = reflectItems(objectInvoke, itemList);
		}

		return changedAttributes;
	}

}
