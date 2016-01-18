package com.geopista.protocol.contaminantes;


import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-oct-2004
 * Time: 10:22:08
 * To change this template use File | Settings | File Templates.
 */
public class CConstantes {
    public static String PLANTILLAS_PATH_ARBOLADO="plantillas/contaminantes/arbolado/";
    public static String PLANTILLAS_PATH_VERTEDEROS="plantillas/contaminantes/vertederos/";
    public static String PLANTILLAS_PATH_ACTIVIDADES="plantillas/contaminantes/actividades/";
    public static String IMAGE_PATH="img/";

    // Formato del nombre de las plantillas para los informes
    public static String PLANTILLAS_STARTS_WITH= "plantilla";

    //** Anexos */
    public static String anexosActividadesContaminantesUrl = "http://localhost:54321/anexos/contaminantes/";
    //******************************************
    //** Operaciones sobre los anexos
    //****************************************
    public static final int CMD_ANEXO_ADDED = 201;
    public static final int CMD_ANEXO_DELETED = 202;
    //******************************************
    //** Operaciones sobre las inspecciones
    //****************************************
    public static final int CMD_INSPECCION_ADDED = 201;
    public static final int CMD_INSPECCION_DELETED = 202;
    public static final int CMD_INSPECCION_MODIFIED = 203;

}
