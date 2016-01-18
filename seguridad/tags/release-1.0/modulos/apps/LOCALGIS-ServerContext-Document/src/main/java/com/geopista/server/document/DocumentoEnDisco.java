package com.geopista.server.document;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.CConstantesComando;
import com.geopista.server.administradorCartografia.ACException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 12-may-2006
 * Time: 11:07:47
 * To change this template use File | Settings | File Templates.
 */
public class DocumentoEnDisco {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DocumentoEnDisco.class);
    private static SimpleDateFormat sdf= new SimpleDateFormat("yyyyMM");

    /**
     * Guarda un docuemnto en disco
     * @param documento
     * @throws Exception
     */
    public static void guardar(DocumentBean documento) throws Exception{
        System.out.println("pathguardar: "+CConstantesComando.documentosPath);
    	
    	if (documento.getContent() == null) throw new ACException("Documento vacio");
        String sdfformat= sdf.format(documento.getFechaEntradaSistema());
        String path= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator;
        
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        FileOutputStream out = new FileOutputStream(path + documento.getId());
        out.write(documento.getContent());
        out.close();
    }

    /**
     * Borra un docuemnto de disco
     * @param documento
     * @throws Exception
     */
    public static void borrar(DocumentBean documento) throws Exception{
        String sdfformat= sdf.format(documento.getFechaEntradaSistema());
        String path= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator;

        borrar(path + documento.getId());
    }

    /**
     * Borra el fichero con path de disco
     * @param path del fichero
     * @throws Exception
     */
    public static void borrar(String path) throws Exception{
        if (!new File(path).exists()) return;
        new File(path).delete();
    }

    /**
     * Actualiza un documento en disco
     * @param documento
     * @throws Exception
     */
    public static void actualizar(DocumentBean documento) throws Exception{
        borrar(documento);
        guardar(documento);
    }

    /**
     * Retorna el contenido de un docuemnto de disco
     * @param documento
     * @return
     * @throws Exception
     */
    public static byte[] get(DocumentBean documento) throws Exception{
        String sdfformat= sdf.format(documento.getFechaEntradaSistema());
        String path= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator;
        File file= new File(path + documento.getId());
        if (!file.exists()) throw new ACException("Documento no existe "+path + documento.getId());

        return getContenido(file);
    }

    /**
     * Retorna el contenido de disco del fichero
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getContenido(File file) throws Exception{

        InputStream is= new FileInputStream(file);
        long length= file.length();

        if (length > Integer.MAX_VALUE) throw new ACException("El fichero es demasiado grande.");

        byte[] bytes= new byte[(int)length];

        // Leemos en bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Comprobamos haber leido todos los bytes del fichero
        if (offset < bytes.length) throw new IOException("No se ha podido leer completamente el fichero "+file.getName());

        is.close();

        return bytes;
    }

    /**
     * Guarda el documento en un directorio temporal
     * @param documento
     * @throws Exception
     */
    public static void guardarEnTemporal(DocumentBean documento) throws Exception{

        if (documento.getContent() == null) throw new ACException("Documento vacio o no existe "+documento.getFileName());
        String sdfformat= sdf.format(documento.getFechaEntradaSistema());
        String path= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator + "tmp" + File.separator;

        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        FileOutputStream out = new FileOutputStream(path + documento.getId());
        out.write(documento.getContent());
        out.close();
    }

    /**
     * Actualiza los ficheros del servidor con los ficheros almecenados en temporal
     * @param c lista de documentos a los que corresponden los ficheros guardados en temporal
     * @throws Exception
     */
    public static void actualizarConFicherosDeTemporal(Collection c) throws Exception{
        if (c == null) return;

        Object[] documentos= c.toArray();
        for (int i=0; i<documentos.length; i++){
            DocumentBean documento= (DocumentBean)documentos[i];
            String sdfformat= sdf.format(documento.getFechaEntradaSistema());
            String pathTmp= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator + "tmp" + File.separator;
            File file= new File(pathTmp + documento.getId());
            if (!file.exists()){
            	continue;
            }
            byte[] contenido= getContenido(file);

            /** copiamos a destino */
            String pathDestino= CConstantesComando.documentosPath + File.separator + sdfformat + File.separator;
            File newFile= new File(pathDestino + documento.getId());
            if (newFile.exists()){
                if (newFile.canWrite())
                    newFile.delete();
            }
            if (!newFile.exists()){
                if (!new File(pathDestino).exists()) {
                    new File(pathDestino).mkdirs();
                }

                FileOutputStream out = new FileOutputStream(pathDestino + documento.getId());
                out.write(contenido);
                out.close();
            }
            /** Una vez copiado en destino, lo borramos de temporal */
            try{borrar(pathTmp + documento.getId());}catch(Exception e){}
        }

    }



}
