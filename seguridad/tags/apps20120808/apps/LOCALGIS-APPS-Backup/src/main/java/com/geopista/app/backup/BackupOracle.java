package com.geopista.app.backup;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.PrintStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


import com.geopista.app.backup.consola.BackupConsola;

import oracle.sql.ARRAY;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import oracle.sql.Datum;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;

public class BackupOracle implements BackupAdapter {

    /**
     * Constantes que se corresponden con las claves de los datos que vamos a guardar en los objetos InformacionTabla
     */
    public static String KEY_NOMBRE_TABLA = "nombreTabla";
    public static String KEY_SECUENCIA_ASOCIADA = "nombreSecuencia";
    public static String KEY_COLUMNA_SECUENCIA = "columnaSecuencia";
    public static String KEY_COLUMNA_ID_MUNICIPIO = "columnaIdMunicipio";

    /**
     * Constante que determina la máxima longitud de una cadena en el script que vamos a generar
     */
    private final int MAXIMA_LONGITUD_CADENA = 2000;
    
    /**
     * Constantes de los nombres de los procedimientos
     */
    private final String PROC_ACTUALIZAR_SECUENCIA = "actualizar_secuencia";
    private final String PROC_DESHABILITAR_FOREIGN_KEYS = "deshabilitar_foreign_keys";
    private final String PROC_HABILITAR_FOREIGN_KEYS = "habilitar_foreign_keys";
    private final String PROC_CARGAR_BLOB = "cargar_blob";
    private final String PROC_CARGAR_CLOB = "cargar_clob";
    
    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerCadenaConexion(java.lang.String, int, java.lang.String)
     */
    public String obtenerCadenaConexion(String host, int puerto, String baseDatos) {
        // En el caso de oracle, la base de datos la hacemos corresponder con el SID
        return "jdbc:oracle:thin:@"+host+":"+ puerto+":"+baseDatos;
    }

    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerClaseDriver()
     */
    public String obtenerClaseDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerTablasComunes(java.sql.Connection)
     */
    public InformacionTabla[] obtenerTablasComunes(Connection conexion) throws SQLException {
        Map tablas = new HashMap();
        String sql =
            "select ut.table_name as tabla " +
            "from user_tables ut " +
            "minus " +
            "select ut.table_name as tabla " +
            "from user_tables ut, user_tab_columns utc " +
            "where utc.table_name = ut.table_name and " +
            "      utc.data_type = 'NUMBER' and " +
            "      utc.column_name like '%MUNICIPIO%'";

        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombreTabla = resultSet.getString("tabla");
            InformacionTabla informacionTabla = new InformacionTabla();
            informacionTabla.putInformacion(KEY_NOMBRE_TABLA, nombreTabla);
            tablas.put(nombreTabla, informacionTabla);
        }
        resultSet.close();
        statement.close();
        
        rellenarInformacionSecuencias(conexion, tablas);

        InformacionTabla[] tablasArray = new InformacionTabla[tablas.size()];
        tablas.values().toArray(tablasArray);
        return tablasArray;
    }

    
    /**
     * Método para rellenar la informacion de las secuencias asociadas a las tablas
     * @param conexion Conexión con la base de datos
     * @param tablas Map con las tablas que se desean rellenar
     * @throws SQLException Si ocurre algun error
     */
    
    private void rellenarInformacionSecuencias(Connection conexion, Map tablas)  throws SQLException {
        /*
         * 1. Obtenemos las secuencias asociadas a cada tabla y rellenamos las que necesitemos
         */
        String sql = 
            "select ua.table_name as tabla, " +
            "       seq.sequence_name as secuencia_asociada " +
            "from user_tables ua, user_sequences seq " +
            "where seq.sequence_name = 'SEQ_' || ua.table_name";
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombreTabla = resultSet.getString("tabla");
            String secuenciaAsociada = resultSet.getString("secuencia_asociada");
            InformacionTabla tabla = (InformacionTabla)tablas.get(nombreTabla);
            if (tabla != null) {
                tabla.putInformacion(KEY_SECUENCIA_ASOCIADA, secuenciaAsociada);
            }
        }
        resultSet.close();
        statement.close();

        /*
         * 2. Obtenemos las columnas que son clave primera y que en principio
         * podrian tener una secuencia asociada. Hay un problema con las claves
         * compuestas ya que podriamos tomar una u otra columna. De momento
         * tomamos la columna que ocupa la posicion 1 en la clave compuesta
         */
        sql = 
            "select c.table_name as tabla, " +
            "       cc.column_name as columna_secuencia " +
            "from user_constraints c, user_cons_columns cc " +
            "where c.table_name = cc.table_name and " +
            "      c.constraint_name = cc.constraint_name and " +
            "      c.constraint_type = 'P' and " +
            "      cc.position = 1";
        statement = conexion.createStatement();
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombreTabla = resultSet.getString("tabla");
            String columnaSecuencia = resultSet.getString("columna_secuencia");
            InformacionTabla tabla = (InformacionTabla)tablas.get(nombreTabla);
            if (tabla != null) {
                tabla.putInformacion(KEY_COLUMNA_SECUENCIA, columnaSecuencia);
            }
        }
        resultSet.close();
        statement.close();
    }
    
    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerTablasMunicipio(java.sql.Connection, int)
     */
    public InformacionTabla[] obtenerTablasMunicipio(Connection conexion) throws SQLException {
        Map tablas = new HashMap();
        String sql =
            "select ut.table_name as tabla, " +
            "       utc.column_name as columna_id_municipio " +
            "from user_tables ut, user_tab_columns utc " +
            "where utc.table_name = ut.table_name and " +
            "      utc.data_type = 'NUMBER' and " +
            "      utc.column_name like '%MUNICIPIO%'";

        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String nombreTabla = resultSet.getString("tabla");
            String columnaIdMunicipio = resultSet.getString("columna_id_municipio");
            InformacionTabla informacionTabla = new InformacionTabla();
            informacionTabla.putInformacion(KEY_NOMBRE_TABLA, nombreTabla);
            informacionTabla.putInformacion(KEY_COLUMNA_ID_MUNICIPIO, columnaIdMunicipio);
            tablas.put(nombreTabla, informacionTabla);
        }
        resultSet.close();
        statement.close();
        
        //Rellenamos la informacion de las secuencias en las tablas
        rellenarInformacionSecuencias(conexion, tablas);

        InformacionTabla[] tablasArray = new InformacionTabla[tablas.size()];
        tablas.values().toArray(tablasArray);
        return tablasArray;
    }

    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerSentenciaActualizarSecuencia(com.geopista.backup.InformacionTabla)
     */
    public String obtenerSentenciaActualizarSecuencia(InformacionTabla tabla) {
        String secuenciaAsociada = (String)tabla.getInformacion(KEY_SECUENCIA_ASOCIADA);
        String columnaSecuencia = (String)tabla.getInformacion(KEY_COLUMNA_SECUENCIA);
        if (secuenciaAsociada != null && columnaSecuencia != null) {
            return "exec "+PROC_ACTUALIZAR_SECUENCIA+"('" + tabla.getInformacion(KEY_NOMBRE_TABLA) + "', '"+secuenciaAsociada+"', '"+columnaSecuencia+"');";
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerSentenciaDeshabilitarConstraints(com.geopista.backup.InformacionTabla)
     */
    public String obtenerSentenciaDeshabilitarConstraints(InformacionTabla tabla,Connection connection) throws SQLException {
        return "exec "+PROC_DESHABILITAR_FOREIGN_KEYS+"('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"');";
    }

    /* (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#obtenerSentenciaHabilitarConstraints(com.geopista.backup.InformacionTabla)
     */
    public String obtenerSentenciaHabilitarConstraints(InformacionTabla tabla,Connection connection) throws SQLException {
        return "exec "+PROC_HABILITAR_FOREIGN_KEYS+"('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"');";
    }

    /*
     * (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#crearScriptInicial(java.sql.Connection, java.io.PrintStream, java.io.PrintStream)
     */
    public void crearScriptInicial(Connection conexion, FilterOutputStream printStreamSalida, PrintStream printStreamError) throws SQLException {
        /*
         * Creamos el procedimiento que nos permite actualizar una secuencia al
         * maximo valor de una columna de una tabla
         */
        crearScriptProcedimientoActualizarSecuencia(printStreamSalida);
        /*
         * Creamos el procedimiento que nos permite desactivar las foreign keys de una tabla
         */
        crearScriptProcedimientoDeshabilitarForeignKeys(printStreamSalida);
        /*
         * Creamos el procedimiento que nos permite activar las foreign keys de una tabla
         */
        crearScriptProcedimientoHabilitarForeignKeys(printStreamSalida);
        /*
         * Creamos el procedimiento que nos permite cargar un blob justo despues
         * de insertar un empty blob. El procedimiento añade los bytes al blob,
         * por lo que habrá que llamarlo varias veces para insertar el contenido
         * por bloques
         */
        crearScriptProcedimientoCargarBlob(printStreamSalida);
        /*
         * Creamos el procedimiento que nos permite cargar un clob justo despues
         * de insertar un empty blob. El procedimiento añade los bytes al clob,
         * por lo que habrá que llamarlo varias veces para insertar el contenido
         * por bloques
         */
        crearScriptProcedimientoCargarClob(printStreamSalida);
    }

    /**
     * Método para crear el script de creacion del procedimiento para actualizar
     * una secuencia al maximo valor de una columna de una tabla
     * @param printStreamSalida Flujo de salida
     */
    private void crearScriptProcedimientoActualizarSecuencia(FilterOutputStream printStreamSalida) {
        BackupConsola.writeFile( printStreamSalida,"create or replace procedure "+PROC_ACTUALIZAR_SECUENCIA+" (tabla in varchar2, nombre_secuencia in varchar2, columna_secuencia in varchar2) is");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    nuevo_valor number;");
        BackupConsola.writeFile( printStreamSalida,"    valor_actual number;");
        BackupConsola.writeFile( printStreamSalida,"    incremento_actual number;");
        BackupConsola.writeFile( printStreamSalida,"    minimo_actual number;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"begin");
        BackupConsola.writeFile( printStreamSalida,"    -- obtenemos el mayor valor de la tabla correpsondiente");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'select max('|| columna_secuencia || ') from ' || tabla into nuevo_valor;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    -- ponemos la secuencia al valor obtenido anteriormente");
        BackupConsola.writeFile( printStreamSalida,"    select increment_by, min_value into incremento_actual, minimo_actual from user_sequences where sequence_name = upper(nombre_secuencia);");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'alter sequence ' || nombre_secuencia ||' minvalue ' || least(nuevo_valor - incremento_actual - 1 , minimo_actual);");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'select ' || nombre_secuencia ||'.nextval from dual' into valor_actual;");
        BackupConsola.writeFile( printStreamSalida,"    if (nuevo_valor - valor_actual - incremento_actual) != 0 then");
        BackupConsola.writeFile( printStreamSalida,"        execute immediate 'alter sequence ' || nombre_secuencia ||' increment by '|| (nuevo_valor - valor_actual - incremento_actual);");
        BackupConsola.writeFile( printStreamSalida,"    end if;");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'select ' || nombre_secuencia ||'.nextval from dual' into valor_actual;");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'alter sequence ' || nombre_secuencia ||' increment by ' || incremento_actual;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    -- incrementamos una vez la secuencia para que la proxima vez nos de un nuevo valor");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate 'select ' || nombre_secuencia ||'.nextval from dual' into valor_actual;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"exception");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    when others then");
        BackupConsola.writeFile( printStreamSalida,"        return;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"end "+PROC_ACTUALIZAR_SECUENCIA+";");
        BackupConsola.writeFile( printStreamSalida,"/");
    }
    
    /**
     * Método para crear el script de creacion del procedimiento para desactivar
     * las foreign keys de una tabla
     * @param printStreamSalida Flujo de salida
     */
    private void crearScriptProcedimientoDeshabilitarForeignKeys(FilterOutputStream printStreamSalida) {
        BackupConsola.writeFile( printStreamSalida,"create or replace procedure "+PROC_DESHABILITAR_FOREIGN_KEYS+" (tabla in varchar2) is");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    type tipo_cursor_fk is ref cursor;");
        BackupConsola.writeFile( printStreamSalida,"    cursor_fk tipo_cursor_fk;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_obtener_fk varchar2(4000);");
        BackupConsola.writeFile( printStreamSalida,"    nombre_constraint user_constraints.constraint_name%type;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_desactivar_fk varchar2(4000);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"begin");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_obtener_fk := 'select fk.constraint_name from user_constraints fk where fk.constraint_type = ''R'' and fk.table_name = ''' || tabla || '''';");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    open cursor_fk for sentencia_obtener_fk;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    loop");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"        fetch cursor_fk into nombre_constraint;");
        BackupConsola.writeFile( printStreamSalida,"        exit when cursor_fk%notfound;");
        BackupConsola.writeFile( printStreamSalida,"        sentencia_desactivar_fk := 'alter table ' || tabla || ' disable constraint ' || nombre_constraint;");
        BackupConsola.writeFile( printStreamSalida,"        execute immediate sentencia_desactivar_fk;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    end loop;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"end "+PROC_DESHABILITAR_FOREIGN_KEYS+";");
        BackupConsola.writeFile( printStreamSalida,"/");
    }

    /**
     * Método para crear el script de creacion del procedimiento para desactivar
     * las foreign keys de una tabla
     * @param printStreamSalida Flujo de salida
     */
    private void crearScriptProcedimientoHabilitarForeignKeys(FilterOutputStream printStreamSalida) {
        BackupConsola.writeFile( printStreamSalida,"create or replace procedure "+PROC_HABILITAR_FOREIGN_KEYS+" (tabla in varchar2) is");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    type tipo_cursor_fk is ref cursor;");
        BackupConsola.writeFile( printStreamSalida,"    cursor_fk tipo_cursor_fk;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_obtener_fk varchar2(4000);");
        BackupConsola.writeFile( printStreamSalida,"    nombre_constraint user_constraints.constraint_name%type;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_activar_fk varchar2(4000);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"begin");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_obtener_fk := 'select fk.constraint_name from user_constraints fk where fk.constraint_type = ''R'' and fk.table_name = ''' || tabla || '''';");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    open cursor_fk for sentencia_obtener_fk;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    loop");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"        fetch cursor_fk into nombre_constraint;");
        BackupConsola.writeFile( printStreamSalida,"        exit when cursor_fk%notfound;");
        BackupConsola.writeFile( printStreamSalida,"        sentencia_activar_fk := 'alter table ' || tabla || ' enable constraint ' || nombre_constraint;");
        BackupConsola.writeFile( printStreamSalida,"        execute immediate sentencia_activar_fk;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    end loop;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"end "+PROC_HABILITAR_FOREIGN_KEYS+";");
        BackupConsola.writeFile( printStreamSalida,"/");
    }

    /**
     * Método para crear el script de creacion del procedimiento para cargar un blob
     * @param printStreamSalida Flujo de salida
     */
    private void crearScriptProcedimientoCargarBlob(FilterOutputStream printStreamSalida) {
        BackupConsola.writeFile( printStreamSalida,"create or replace procedure "+PROC_CARGAR_BLOB+" (tabla in varchar2, campo_blob in varchar2, contenido in long raw) is");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    ptr_blob blob;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_sql varchar2(2000);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"begin");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_sql := 'select '|| campo_blob || ' from ' || tabla || ' where rowid = (select max(rowid) from ' || tabla || ') for update';");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate sentencia_sql into ptr_blob;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.open(ptr_blob, dbms_lob.lob_readwrite);");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.writeappend(ptr_blob, dbms_lob.getlength(contenido), contenido);");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.close(ptr_blob);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"exception");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    when others then");
        BackupConsola.writeFile( printStreamSalida,"    if dbms_lob.isopen(ptr_blob) = 1 then");
        BackupConsola.writeFile( printStreamSalida,"        dbms_lob.close(ptr_blob);");
        BackupConsola.writeFile( printStreamSalida,"    end if;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"end "+PROC_CARGAR_BLOB+";");
        BackupConsola.writeFile( printStreamSalida,"/");
    }

    /**
     * Método para crear el script de creacion del procedimiento para cargar un clob
     * @param printStreamSalida Flujo de salida
     */
    private void crearScriptProcedimientoCargarClob(FilterOutputStream printStreamSalida) {
        BackupConsola.writeFile( printStreamSalida,"create or replace procedure "+PROC_CARGAR_CLOB+" (tabla in varchar2, campo_clob in varchar2, contenido in long) is");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    ptr_clob clob;");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_sql varchar2(2000);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"begin");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    sentencia_sql := 'select '|| campo_clob || ' from ' || tabla || ' where rowid = (select max(rowid) from ' || tabla || ') for update';");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    execute immediate sentencia_sql into ptr_clob;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.open(ptr_clob, dbms_lob.lob_readwrite);");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.writeappend(ptr_clob, dbms_lob.getlength(contenido), contenido);");
        BackupConsola.writeFile( printStreamSalida,"    dbms_lob.close(ptr_clob);");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"exception");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"    when others then");
        BackupConsola.writeFile( printStreamSalida,"    if dbms_lob.isopen(ptr_clob) = 1 then");
        BackupConsola.writeFile( printStreamSalida,"        dbms_lob.close(ptr_clob);");
        BackupConsola.writeFile( printStreamSalida,"    end if;");
        BackupConsola.writeFile( printStreamSalida,"");
        BackupConsola.writeFile( printStreamSalida,"end "+PROC_CARGAR_CLOB+";");
        BackupConsola.writeFile( printStreamSalida,"/");
    }

    /*
     * (non-Javadoc)
     * @see com.geopista.backup.BackupAdapter#crearScript(java.sql.Connection, java.io.PrintStream, java.io.PrintStream, com.geopista.backup.InformacionTabla, boolean, int)
     */
    public void crearScriptUTF8(Connection conexion, FilterOutputStream printStreamSalida, PrintStream printStreamError, InformacionTabla tabla, boolean tablaMunicipio, String municipios, FilterOutputStream salidaSql_UTF8) throws SQLException {
    	   
    }
    public void crearScript(Connection conexion, FilterOutputStream printStreamSalida, PrintStream printStreamError, InformacionTabla tabla, boolean tablaMunicipio, String municipios) throws SQLException {
    
        String sql = "select * from " + tabla.getInformacion(KEY_NOMBRE_TABLA) +" tabla";
        if (tablaMunicipio) {
        	if (municipios != "") {
        		String columnaIdMunicipio = (String)tabla.getInformacion(KEY_COLUMNA_ID_MUNICIPIO);
        		sql += " where \"" + columnaIdMunicipio + "\" IN (" + municipios + ", 0) or \"" + columnaIdMunicipio + "\" IS null";
        	}
        }
        
        Statement statement = conexion.createStatement();
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error al hacer la query: ");
            System.err.println(sql);
            throw e;
        }
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        /*
         * Obtenemos el numero de columnas y un array con los tipos y los
         * nombres de las mismas para luego componer las sentencias insert
         */
        int numColumnas = resultSetMetaData.getColumnCount();
        String[] nombreColumnas = new String[numColumnas];
        int[] tipoColumnas = new int[numColumnas];
        for (int i = 0; i < numColumnas; i++) {
            nombreColumnas[i] = resultSetMetaData.getColumnName(i+1);
            tipoColumnas[i] = resultSetMetaData.getColumnType(i+1);
        }
        /*
         * Para cada fila devuelta de la tabla creamos una sentencia insert que
         * luego imprimos en el fichero
         */
        while (resultSet.next()) {
            Map mapCamposBlob = new HashMap();
            Map mapCamposClob = new HashMap();
            String insert = "insert into " + tabla.getInformacion(KEY_NOMBRE_TABLA) + " (";
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert += ", ";
                }
                insert += ""+nombreColumnas[i]+"";
            }
            insert += ") values (";
            for (int i = 0; i < numColumnas; i++) {
                if (i != 0) {
                    insert += ", ";
                }
                insert += valorColumna(printStreamError, tabla, nombreColumnas[i], resultSet.getObject(i+1), tipoColumnas[i], mapCamposBlob, mapCamposClob);
            }
            insert += ");";
            BackupConsola.writeFile( printStreamSalida,insert);
            /*
             * Si en esta sentencia insert ha habido algun campo blob entonces llamamos al procedimiento para actualizar el blob
             */
            if (!mapCamposBlob.isEmpty()) {
                Iterator it = mapCamposBlob.keySet().iterator();
                while (it.hasNext()) {
                    String nombreColumna = (String) it.next();
                    Blob blob = (Blob)mapCamposBlob.get(nombreColumna);
                    crearScriptBlob(conexion, printStreamSalida, printStreamError, tabla, nombreColumna, blob);
                }
            }
            /*
             * Si en esta sentencia insert ha habido algun campo blob entonces llamamos al procedimiento para actualizar el blob
             */
            if (!mapCamposBlob.isEmpty()) {
                Iterator it = mapCamposBlob.keySet().iterator();
                while (it.hasNext()) {
                    String nombreColumna = (String) it.next();
                    Blob blob = (Blob)mapCamposBlob.get(nombreColumna);
                    crearScriptBlob(conexion, printStreamSalida, printStreamError, tabla, nombreColumna, blob);
                }
            }
            /*
             * Si en esta sentencia insert ha habido algun campo blob entonces llamamos al procedimiento para actualizar el blob
             */
            if (!mapCamposClob.isEmpty()) {
                Iterator it = mapCamposClob.keySet().iterator();
                while (it.hasNext()) {
                    String nombreColumna = (String) it.next();
                    Clob clob = (Clob)mapCamposClob.get(nombreColumna);
                    crearScriptClob(conexion, printStreamSalida, printStreamError, tabla, nombreColumna, clob);
                }
            }
        }
        resultSet.close();
        statement.close();
    }

    /**
     * Método para crear el script que inserta el contenido en un campo BLOB
     * @param conexion Conexion con la base de datos
     * @param printStreamSalida Flujo de salida
     * @param printStreamError Flujo de errores
     * @param tabla Tabla en la que esta el blob
     * @param nombreColumna Nombre de la columna del blob
     * @param blob Blob a insertar
     * @throws SQLException Si ocurre algun error
     */
    public void crearScriptBlob(Connection conexion, FilterOutputStream printStreamSalida, PrintStream printStreamError, InformacionTabla tabla, String nombreColumna, Blob blob) throws SQLException {
        try {
            BLOB blobOracle = (BLOB) blob;
            InputStream is = blobOracle.getBinaryStream();
            byte[] buffer = new byte[4096];
            StringBuffer stringBuffer = new StringBuffer();
            int contador = 0;
            for (int leidos = is.read(buffer); leidos != -1; leidos = is.read(buffer)) {
                for (int i = 0; i < leidos; i++) {
                    // para imprimir un cero o dos en caso de que se necesite
                    if ((buffer[i] & 0xF0) == 0x00) {
                        stringBuffer.append("0");
                    }
                    stringBuffer.append(Integer.toHexString(buffer[i] & 0xFF));
                    contador += 2;
                    // Cada MAXIMA_LONGITUD_CADENA imprimos una sentencia de ejecucion del procedimiento almacenado
                    if (contador >= MAXIMA_LONGITUD_CADENA) {
                        BackupConsola.writeFile( printStreamSalida,"exec cargar_blob ('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"', '"+nombreColumna+"', '"+stringBuffer.toString()+"');");
                        stringBuffer.delete(0, MAXIMA_LONGITUD_CADENA);
                        contador = 0;
                    }
                }
            }
            if (stringBuffer.length() > 0) {
                BackupConsola.writeFile( printStreamSalida,"exec "+PROC_CARGAR_BLOB+" ('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"', '"+nombreColumna+"', '"+stringBuffer.toString()+"');");
            }
        } catch (IOException e) {
            return;
        } catch (SQLException e) {
            return;
        }
    }

    /**
     * Método para crear el script que inserta el contenido en un campo CLOB
     * @param conexion Conexion con la base de datos
     * @param printStreamSalida Flujo de salida
     * @param printStreamError Flujo de errores
     * @param tabla Tabla en la que esta el blob
     * @param nombreColumna Nombre de la columna del blob
     * @param clob Clob a insertar
     * @throws SQLException Si ocurre algun error
     */
    public void crearScriptClob(Connection conexion, FilterOutputStream printStreamSalida, PrintStream printStreamError, InformacionTabla tabla, String nombreColumna, Clob clob) throws SQLException {
        try {
            CLOB clobOracle = (CLOB) clob;
            Reader reader = clobOracle.getCharacterStream();
            char[] charbuf = new char[4096];
            StringBuffer stringBuffer = new StringBuffer();
            int contador = 0;
            for (int leidos = reader.read(charbuf); leidos != -1; leidos = reader.read(charbuf)) {
                for (int i = 0; i < leidos; i++) {
                    if (charbuf[i] == '\'') {
                        stringBuffer.append("''");
                        contador += 2;
                    } else if (charbuf[i] == '\n') {
                        stringBuffer.append("\\n");
                        contador += 2;
                    } else {
                        stringBuffer.append(charbuf[i]);
                        contador++;
                    }
                    // Cada MAXIMA_LONGITUD_CADENA imprimos una sentencia de ejecucion del procedimiento almacenado
                    if (contador >= MAXIMA_LONGITUD_CADENA) {
                        BackupConsola.writeFile( printStreamSalida,"exec cargar_clob ('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"', '"+nombreColumna+"', '"+stringBuffer.toString()+"');");
                        stringBuffer.delete(0, MAXIMA_LONGITUD_CADENA);
                        contador = 0;
                    }
                }
            }
            if (stringBuffer.length() > 0) {
                BackupConsola.writeFile( printStreamSalida,"exec "+PROC_CARGAR_CLOB+" ('"+tabla.getInformacion(KEY_NOMBRE_TABLA)+"', '"+nombreColumna+"', '"+stringBuffer.toString()+"');");
            }
        } catch (IOException e) {
            return;
        } catch (SQLException e) {
            return;
        }
    }
    
    /**
     * Método para obtener la representación como String de los valores de las columnas
     * @param printStreamError Flujo para volcar los posibles errores
     * @param tabla Tabla que contiene la columna
     * @param nombreColumna Nombre de la columna
     * @param valor Valor de la columna 
     * @param tipo Tipo de la columna (ver java.sql.Types)
     * @param camposBlob Map donde guardaremos los campos Blobs que detectemos
     * @param camposClob Map donde guardaremos los campos Clobs que detectemos
     * @return La representación en String del valor de la columna segun su tipo
     */
    private String valorColumna(PrintStream printStreamError, InformacionTabla tabla, String nombreColumna, Object valor, int tipo, Map camposBlob, Map camposClob) {
        if (valor == null) {
            return "null";
        }
        /*
         * En otro caso el valor se traduce segun sea el tipo
         */
        else {
            switch (tipo) {
            case Types.BIT:
                return valor.toString();
            case Types.BLOB:
                //Metemos el campo Blob en el Map y creamos un blob vacio
                camposBlob.put(nombreColumna, (Blob) valor);
                return "EMPTY_BLOB()";
            case Types.BOOLEAN:
                return valorBoolean((Boolean) valor);
            case Types.CHAR:
                return "'"+valor.toString()+"'";
            case Types.CLOB:
                //Metemos el campo Clob en el Map y creamos un blob vacio
                camposClob.put(nombreColumna, (Clob) valor);
                return "EMPTY_CLOB()";
            case Types.DATE:
                return "'"+valor.toString()+"'";
            case Types.DOUBLE:
                return valor.toString();
            case Types.INTEGER:
                return valor.toString();
            case Types.NUMERIC:
                return valor.toString();
            case Types.OTHER:
                return "'"+valor.toString()+"'";
            case Types.SMALLINT:
                return valor.toString();
            case Types.STRUCT:
                return valorStruct((Struct) valor);
            case Types.TIMESTAMP:
                return valorTimestamp(valor);
            case Types.VARCHAR:
                return "'"+valor.toString()+"'";
            default:
                printStreamError.println("Tipo [" + tipo + "] no definido para la columna [" + nombreColumna + "] de la tabla [" + tabla.getInformacion(KEY_NOMBRE_TABLA) + "]");
                return null;
            }
        }
    }

    /**
     * Método para obtener la representación como String de un Boolean
     * @param valor Valor a representar
     * @return La representación en String
     */
    private String valorBoolean(Boolean valor) {
        if (valor.booleanValue()) {
            return "'true'";
        } else {
            return "'false'";
        }
    }

    /**
     * Método que devuelve la representacion en String de un struct 
     * @param struct El struct que se desea saber su representacion
     * @return La representación en String
     */
    private String valorStruct(Struct struct) {
        try {
            STRUCT structOracle = (STRUCT) struct;
            String nameStruct = structOracle.getDescriptor().getName();
            //De momento solo convertimos las estructuras que sean MDSYS.SDO_GEOMETRY
            if (!nameStruct.equals("MDSYS.SDO_GEOMETRY")) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("MDSYS.SDO_GEOMETRY(");
            Object[] atributos = structOracle.getAttributes();
            stringBuffer.append(atributos[0]);
            stringBuffer.append(", ");
            stringBuffer.append(atributos[1]);
            stringBuffer.append(", ");
            stringBuffer.append(atributos[2]);
            stringBuffer.append(", MDSYS.SDO_ELEM_INFO_ARRAY(");
            ARRAY elemInfoArray = (ARRAY)atributos[3];
            Datum[] elemInfoDatumArray = elemInfoArray.getOracleArray();
            for(int i = 0; i < elemInfoDatumArray.length; i++) {
                if (i != 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(elemInfoDatumArray[i].stringValue());
            }
            stringBuffer.append("), MDSYS.SDO_ORDINATE_ARRAY(");
            ARRAY ordinateArray = (ARRAY)atributos[4];
            Datum[] ordinateDatumArray = ordinateArray.getOracleArray();
            for(int i = 0; i < ordinateArray.length(); i++) {
                if (i != 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(ordinateDatumArray[i].stringValue());
            }
            stringBuffer.append("))");
            return stringBuffer.toString();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Método que devuelve la representacion en String de un timestamp 
     * @param timestamp El timestamp que se desea saber su representacion como un Object
     * @return La representación en String
     */
    private String valorTimestamp(Object timestamp) {
        TIMESTAMP timestampOracle = (TIMESTAMP) timestamp;
        try {
            return "timestamp'"+timestampOracle.timestampValue().toString()+"'";
        } catch (SQLException e) {
            return null;
        }
    }

	public Hashtable obtenerEntidades(Connection connection) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT id_entidad, nombreoficial FROM entidad_supramunicipal");
		Hashtable hashtable = new Hashtable();
		while(resultSet.next()) {
			Integer id = new Integer(resultSet.getInt("id_entidad"));
			String nombre = resultSet.getString("nombreoficial");
			hashtable.put(nombre, id);
		}
		return hashtable;
	}
	/**
	 * Obtiene la lista de entidades utilizadas junto con el código del municipio.
	 */
	public Hashtable<Integer, String> obtenerEntidadesYMunicipiosBackup(Connection connection) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT entidad_supramunicipal.id_entidad , id_municipio FROM entidad_supramunicipal, " +
				"entidades_municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and backup=1");
		Hashtable<Integer,String> listaEntidades = new Hashtable<Integer,String>();
		while(resultSet.next()) {
			listaEntidades.put(new Integer(resultSet.getInt("id_entidad")),resultSet.getString("id_municipio"));
		}
		resultSet.close();
        statement.close();
        return listaEntidades;
	
	}

	public String obtenerStringMunicipiosEntidad(Connection connection, int idEntidad) throws SQLException {
		Statement  statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT id_municipio FROM entidades_municipios WHERE id_entidad ="+idEntidad);
		String municipios = "";
		
		while(resultSet.next()) {
			Integer municipio = new Integer(resultSet.getInt("id_municipio"));
			if (!resultSet.isLast()){
				municipios = municipios + municipio.toString() + ", ";
			}
			else{
				municipios = municipios + municipio;
			}
		}
		return municipios;
	}

}
