package com.geopista.app.backup;


import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;


public interface BackupAdapter {
	/**
     * Método que devuelve la informacón de los municipios que actualmente se encuentran
     * en la base de datos
	 * @param connection 
     * @return Hashtable cuya clave es el identificador del municipio y su valor el nombre
	 * @throws SQLException 
     */
    
	public Hashtable obtenerEntidades(Connection connection) throws SQLException;
	
	/***
	 * Obtiene una hashtable donde la key es el id de la entidad y el valor es el codigo del municipio
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public Hashtable<Integer, String> obtenerEntidadesYMunicipiosBackup(Connection connection) throws SQLException;/**
     * Método que devuelve la cadena de conexion a partir de host, puerto y base de datos (o SID)
     * @param host Host donde esta la base de datos
     * @param puerto Puerto de escucha de la base de datos
     * @param baseDatos Nombre de la base de datos (o SID)
     * @return La cadena de conexion
     */
    public String obtenerCadenaConexion(String host, int puerto, String baseDatos);
    
    /**
     * Método que devuelve la clase del driver
     * @return La clase del driver
     */
    public String obtenerClaseDriver();

    /**
     * Método que devuelve las tablas de la base de datos que no son dependientes del municipio
     * @param conexion Conexion abierta con la base de datos
     * @return Un array con la informacion de las tablas comunes
     * @throws SQLException Si ocurre algun error SQL
     */
    public InformacionTabla[] obtenerTablasComunes(Connection conexion) throws SQLException;
    
    /**
     * Método que devuelve las tablas de la base de datos que  son dependientes del municipio
     * @param conexion Conexion abierta con la base de datos
     * @param idMunicipio Identificador del municipio
     * @return Un array con la informacion de las tablas comunes
     * @throws SQLException Si ocurre algun error SQL
     */
    public InformacionTabla[] obtenerTablasMunicipio(Connection conexion) throws SQLException;

    /**
     * Método para obtener la sentencia para deshabilitar las constraints de una tabla
     * @param tabla La tabla de la que se quieren deshabilitar las constraints
     * @param connection 
     * @return La sentencia
     * @throws SQLException 
     */
    public String obtenerSentenciaDeshabilitarConstraints(InformacionTabla tabla,Connection connection) throws SQLException;

    /**
     * Método para obtener la sentencia para habilitar las constraints de una tabla
     * @param tabla La tabla de la que se quieren habilitar las constraints
     * @param connection 
     * @return La sentencia
     * @throws SQLException 
     */
    public String obtenerSentenciaHabilitarConstraints(InformacionTabla tabla,Connection connection) throws SQLException;

    /**
     * Método que devuelve la sentencia a ejecutar para actualizar una secuencia al último valor de algun campo de alguna tabla
     * @param tabla Informacion de la tabla
     * @return La sentencia que obtiene actualiza la secuencia al ultimo valor
     */
    public String obtenerSentenciaActualizarSecuencia(InformacionTabla tabla);

    /**
     * Método que crea un script con las cosas iniciales a realizar antes de hacer el resto
     * @param conexion Conexion con la base de datos abierta previamente
     * @param printStreamSalidaComun Flujo de salida
     * @param printStreamError Flujo de salida para los errores
     * @throws SQLException Si ocurre alguna excepción
     */
    public void crearScriptInicial(Connection conexion, FilterOutputStream printStreamSalidaComun, PrintStream printStreamError) throws SQLException;

    /**
     * Método que crea un script de una tabla determinada y lo vuelva a un fichero
     * @param conexion Conexion con la base de datos abierta previamente
     * @param printStreamSalidaComun Flujo de salida
     * @param printStreamError Flujo de salida para los errores
     * @param tabla Tabla de la que se desea crear el script
     * @param tablaMunicipio Indica si es una tabla de municipio o no
     * @param idMunicipio Identificador del municipio en el caso de que sea una tabla de municipio
     * @throws SQLException Si ocurre alguna excepción
     */
    public void crearScript(Connection conexion, FilterOutputStream printStreamSalidaComun, PrintStream printStreamError, InformacionTabla tabla, boolean tablaMunicipio, String municipios) throws SQLException;

    public void crearScriptUTF8(Connection conexion, FilterOutputStream printStreamSalidaComun, PrintStream printStreamError, InformacionTabla tabla, boolean tablaMunicipio, String municipios, FilterOutputStream salidaSql_UTF8) throws SQLException;
    //El metodo crearScriptUTF8 es una creacion a partir de crearScript
    public String obtenerStringMunicipiosEntidad(Connection connection, int idEntidad) throws SQLException;
}
