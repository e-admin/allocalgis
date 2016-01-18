package com.geopista.protocol.document;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 24-abr-2006
 * Time: 14:40:12
 */
public class Const {
    public static final int ACTION_GET_DOCUMENTS=0;
    public static final int ACTION_ATTACH_DOCUMENT=1;
    public static final int ACTION_ATTACH_BYTESTREAM=2;
    public static final int ACTION_DETACH_DOCUMENT=3;
    public static final int ACTION_GET_ATTACHED_DOCUMENTS=4;
    public static final int ACTION_GET_ATTACHED_BYTESTREAM=5;
    public static final int ACTION_UPDATE_DOCUMENT=6;
    public static final int ACTION_UPDATE_BYTESTREAM=7;
    public static final int ACTION_LINK_DOCUMENT= 8;
    public static final int ACTION_GET_ATTACHED_INVENTARIO_DOCUMENTS= 9;
    public static final int ACTION_ATTACH_INVENTARIO_BYTESTREAM= 10;
    public static final int ACTION_ATTACH_INVENTARIO_DOCUMENT= 11;
    public static final int ACTION_LINK_INVENTARIO_DOCUMENT= 12;
    public static final int ACTION_DETACH_INVENTARIO_DOCUMENT= 13;
    public static final int ACTION_GET_TIPO_DOCUMENT= 14;
    public static final int ACTION_GET_ATTACHED_GESTIONCIUDAD_DOCUMENTS= 15;
    public static final int ACTION_ATTACH_GESTIONCIUDAD_BYTESTREAM= 16;
    public static final int ACTION_ATTACH_GESTIONCIUDAD_DOCUMENT= 17;
    public static final int ACTION_LINK_GESTIONCIUDAD_DOCUMENT= 18;
    public static final int ACTION_DETACH_GESTIONCIUDAD_DOCUMENT= 19;
    public static final int ACTION_GET_GESTIONCIUDAD_DOCUMENTS = 20;
    public static final int ACTION_FIND_DOCUMENTS = 21;
    public static final int ACTION_SEND_FILE = 22;    
    public static final int ACTION_GET_DIR = 23;
    public static final int ACTION_GET_SUBDIR = 24;
    public static final int ACTION_GET_FILES = 25;
    public static final int ACTION_GET_ATTACHED_CEMENTERIO_DOCUMENTS= 26;
    public static final int ACTION_ATTACH_CEMENTERIO_BYTESTREAM= 27;
    public static final int ACTION_ATTACH_CEMENTERIO_DOCUMENT= 28;
    public static final int ACTION_LINK_CEMENTERIO_DOCUMENT= 29;
    public static final int ACTION_DETACH_CEMENTERIO_DOCUMENT= 30;
	
    public static final String KEY_ID_FEATURE= "id_feature";
    public static final String KEY_ID_LAYER= "id_layer";
    public static final String KEY_FEATURE= "feature";
    public static final String KEY_ARRAYLIST_IDFEATURES= "arrayList.idFeatures";
    public static final String KEY_ARRAYLIST_IDLAYERS= "arrayList.idLayers";
    public static final String KEY_DOCUMENT= "document";
    public static final String KEY_DOCUMENT_FILE="document_file";
    public static final String KEY_IS_IMGDOCTEXT= "document.is_imgdoctext";

	public static final String KEY_ID_CEMENTERIO= "id_cementerio";
    public static final String KEY_ID_INVENTARIO= "id_inventario";
    public static final String KEY_ID_MUNICIPIO= "idMunicipio";
    public static final String KEY_DOCUMENTOS_INVENTARIO= "docs_inventario";
	public static final String KEY_DOCUMENTOS_CEMENTERIO= "docs_cementerio";    
    public static final String KEY_ID_GESTIONCIUDADNOTE= "id_warning";    
    public static final String KEY_ID_ENTIDAD= "idEntidad";
    public static final String KEY_DOCS = "documentos";
    public static final String KEY_TEMPLATES = "plantillas";
    public static final String KEY_PATH = "path";
    public static final String KEY_REPORT_NAME = "name";
	
    public static final String KEY_ID_SUPERPATRON= "id_superpatron";
	public static final String KEY_ID_PATRON= "id_patron";
    public static final int ACL_GENERAL=12;
}
