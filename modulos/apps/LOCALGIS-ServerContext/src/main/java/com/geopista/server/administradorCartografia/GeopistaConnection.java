/**
 * GeopistaConnection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.model.ExportMapBean;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.model.IWMSTemplate;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 05-sep-2005
 * Time: 11:42:54
 */
/**
 * Interface que  deben implementar todos los conectores de Base de datos.
 * Por ahora se implementan todas las funciones en un futuro se prodría
 * elevar algunas funciones a una clase común.
 */
public interface GeopistaConnection {
	public HashMap<String, String> obtenerCeldasProyectoExtraccion(String idProyectoExtract)throws SQLException;
	public void asignarCeldasProyectoExtraccion(String idProyectoExtract, HashMap<String, String> celdasUsuarios)throws SQLException;
	public List<ExtractionProject> getProyectosExtraccion(int idMapa, int idEntidad)throws SQLException;
	public void crearProyectoExtraccion(ExtractionProject eProyect, int idEntidad)throws SQLException;
	public void deleteProyectoExtraccion(ExtractionProject eProyect, int idEntidad)throws SQLException;
	public ListaUsuarios getUsuariosPermisosCapas(List<Integer> listaCapas, int iEntidad) throws SQLException;
	public ListaUsuarios getUsuarios(int iEntidad) throws SQLException;
    public void loadDomains() throws SQLException;
    public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,List lMunicipalities)throws IOException,SQLException, Exception;
    public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities)throws IOException,SQLException, Exception;
    public void returnLayer(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities, Integer srid)throws IOException,SQLException, Exception;
    public void loadFeatures(int iMunicipio,String sLayer,String sLocale, Geometry geom,FilterNode fn,ObjectOutputStream oos,ISesion sesion,boolean bValidateData,List lMunicipalities, Integer srid)throws IOException,SQLException, Exception;
    public int lockLayer(int iMunicipio, String sLayer, int iUser, Geometry geom) throws Exception;
    public int unlockLayer(int iMunicipio, String sLayer, int iUser);
    public FeatureLockResult lockFeature(int iMunicipio, List layers, List featuresIds, int iUserId) throws Exception;
    public List unlockFeature(int iMunicipio, List layers, List featuresIds, int iUser);
    public Geometry municipioGeometry(int iMunicipio);
    public void returnMap(int iEntidad,int iID, String sLocale, ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception;
    public void returnMaps(String sLocale,ISesion sesion, ObjectOutputStream oos)throws IOException,SQLException,Exception;
    public void returnLayerFamilyIDsByMap(int iMap, ObjectOutputStream oos)throws IOException,SQLException,Exception;
    public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion) throws Exception;
    public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion,boolean bValidateData) throws Exception;
    public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion,boolean bValidateData, Integer srid_destino) throws Exception;
    public int insertFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, ObjectOutputStream oos,ISesion sesion,boolean bValidateData, Integer srid_destino,Hashtable params) throws Exception;
    public void log(int iMunicipio, String sLayer, int iUserID, int iFeature, int iAction)throws SQLException;
    public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion)throws Exception;
    public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion, boolean bValidateData)throws Exception;
    public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion, boolean bValidateData, Integer srid_destino)throws Exception;
    public int updateFeature(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload upload, boolean bDelete, ObjectOutputStream oos, ISesion sesion, boolean bValidateData, Integer srid_destino,Hashtable params)throws Exception;
    public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData)throws Exception;
    public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData, boolean bValidateData)throws Exception;
    public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData, boolean bValidateData, Integer srid_destino)throws Exception;
    public int procesarLoteActualizacion(int iMunicipio,String sLayer,String sLocale,ACFeatureUpload[] aUpload, ObjectOutputStream oos, ISesion sesion, int iUserID, boolean bLoadData, boolean bValidateData, Integer srid_destino, Hashtable params)throws Exception;
    public void updateStyle(String sLayer, String sXML,ObjectOutputStream oos, ISesion sesion) throws Exception;
    public void returnModifiedFeatureIDs(int iMunicipio,String sLayer, long lDate, String sLocale, ObjectOutputStream oos)throws IOException,SQLException,Exception;
    public void returnLayerFamilyIDs(ObjectOutputStream oos)throws IOException,SQLException,Exception;
    public void returnLayerIDsByMap(int iMap,int iPosition,ObjectOutputStream oos,ISesion sesion)throws IOException,SQLException,Exception;
    public void returnLayerIDsByFamily(int iLayerFamily,ObjectOutputStream oos,ISesion sesion) throws IOException,SQLException,Exception;
    public void insertMap(ISesion sesion, String sIdMunicipio, IACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception;
    public void updateMap(ISesion sesion, String sIdMunicipio, IACMap acMap,String sLocale,ObjectOutputStream oos) throws Exception;
    public void deleteMap(ISesion sesion, int iId,ObjectOutputStream oos, String sLocale) throws Exception;
    /** Incidencia[365] El administrador de cartografia no funciona para otros idiomas  */
    //public void returnLayerFamilies(ObjectOutputStream oos)throws IOException,SQLException,Exception;
    public void returnLayerFamilies(ObjectOutputStream oos, String sLocale)throws IOException,SQLException,Exception;
    public void returnGeoRef(ObjectOutputStream oos, String tipoVia, String via, String numPoli, String sLocale,ISesion sesion) throws Exception;
    public void returnGeoRef(ObjectOutputStream oos, String tipoVia, String via, String numPoli, String sLocale,ISesion sesion,boolean bValidateData) throws Exception;
    /** REFACTORIZACION ORACLE */
    public void setSRID(NewSRID srid);
	public int getNextDictionaryId() throws Exception;
	public void insertCoverageLayer(int intValue, int idName, String url,
			String srs, String extension) throws Exception;
	public void insertVocablo(int idName, String string, String string2) throws Exception;
	public String getSRIDDefecto(boolean defecto, int idEntidad) throws FileNotFoundException,IOException,ACException,SQLException;
	public int getSRIDDefecto(boolean defecto, Connection conn, String idEntidad) throws FileNotFoundException,IOException,ACException;
    public void searchByAttribute(String idEntidad, int iMunicipio, String sLayer, String sLocale, FilterNode fn,
            ObjectOutputStream oos, ISesion sesion, String attributeName, String attributeValue,
            boolean bValidateData, Integer srid_destino) throws IOException, SQLException,
            Exception;
	public ArrayList<ExportMapBean> getMapas(String locale, String idEntidad) throws Exception;
	public ExportMapBean getLayersMapa(String sLocale, ExportMapBean exportMapBean) throws Exception;
	public List<Municipio> getMunicipiosEntidad(String string) throws Exception;


	//Añadimos para la gestión de plantillas
	public void returnWMSTemplateList(ObjectOutputStream oos,int idMunicipio)throws IOException,SQLException,Exception;
	public void returnWMSTemplate(ObjectOutputStream oos, int id)throws IOException,SQLException,Exception;
	public void returnWMSTemplateMaps(ObjectOutputStream oos, int id)throws IOException,SQLException,Exception;
	public void deleteWMSTemplate(ObjectOutputStream oos, int id, int idMunicipio)throws IOException,SQLException,Exception;
	public void updateWMSTemplate (ObjectOutputStream oos, IWMSTemplate wmsTemplate)throws IOException,SQLException,Exception;
	public void addWMSTemplate (ObjectOutputStream oos, IWMSTemplate wmsTemplate,int idMunicipio)throws IOException,SQLException,Exception;

}











