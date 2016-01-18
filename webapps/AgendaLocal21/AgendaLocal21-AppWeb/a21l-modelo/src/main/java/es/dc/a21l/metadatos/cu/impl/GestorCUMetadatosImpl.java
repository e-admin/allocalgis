/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.cu.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorFileSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.cu.impl.Fuente2FuenteDtoTransformer;
import es.dc.a21l.fuente.cu.impl.FuenteDto2FuenteTransformer;
import es.dc.a21l.fuente.cu.impl.FuenteDtoValidador;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.metadatos.cu.GestorCUMetadatos;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.metadatos.modelo.Metadatos;
import es.dc.a21l.metadatos.modelo.MetadatosRepositorio;
import es.dc.a21l.usuario.modelo.Usuario;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUMetadatosImpl implements GestorCUMetadatos{
	private static final Logger log = LoggerFactory.getLogger(GestorCUMetadatosImpl.class);
	private Mapper mapper;
    private MetadatosRepositorio metadatosRepositorio;
   
	@Autowired
	public void setMetadatosRepositorio(MetadatosRepositorio metadatosRepositorio) {
		this.metadatosRepositorio = metadatosRepositorio;
	}    
  
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    
	public MetadatosDto cargaPorIdIndicador(Long id) {
		Metadatos  metadatos=metadatosRepositorio.cargaPorIdIndicador(id);
		if(metadatos==null)
			return new MetadatosDto();
		return new Metadatos2MetadatosDtoTransformer(mapper).transform(metadatos);

    }
   
    public EncapsuladorFileSW guardaFichero(EncapsuladorFileSW fich, String path, EncapsuladorErroresSW erros) {
    	//Primero borro los ficheros que pueda haber en la carpeta
    	String directorio = path+fich.getIdIndicador()+"/";
    	if (!borrarConHijos(directorio,false)) {
    		log.debug("El fichero/carpeta " + directorio + " no se ha podido borrar o vaciar");
    		erros.addError(MetadatosFormErrorsEmun.ERROR_ELIMINAR_FICHERO);
    		return fich;
    	}
    	
    	log.debug("Path = " + path + fich.getNombre());
        File f = new File(path+fich.getIdIndicador()+"/"+fich.getNombre());
        
        try {
	        XMLReader reader = XMLReaderFactory.createXMLReader();
	        InputStream is = new ByteArrayInputStream(fich.getFich());
			InputSource input = new InputSource(is);
		    reader.parse(input);
        } catch ( Exception ex ) {
        	log.debug("El fichero/carpeta " + directorio + " no se ha podido borrar o vaciar");
    		erros.addError(MetadatosFormErrorsEmun.ERROR_FORMATO_FICHERO);
    		return fich;
        }
        
        f.getParentFile().mkdirs();
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            inputStream = new ByteArrayInputStream(fich.getFich());
            out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (Exception ex) {
            log.error("Error al crear archivo en temporales", ex);
            erros.addError(MetadatosFormErrorsEmun.ERROR_ALMACENAR_FICHERO);
    		return fich;
        }  finally {
        	try {
				out.close();
				inputStream.close();
			} catch (Exception e) {
				log.debug("No se pudo cerrar la conexion con el fichero");
			}
        }
    	return fich;
    }
    
    public boolean borrarFichero(String nombre, Long idIndicador, String path, EncapsuladorErroresSW erros) {
    	//Primero borro los ficheros que pueda haber en la carpeta
    	String directorio = path+idIndicador+"/";
    	if (!borrarConHijos(directorio,true)) {
    		log.debug("El fichero/carpeta " + directorio + " no se ha podido borrar o vaciar");
    		erros.addError(MetadatosFormErrorsEmun.ERROR_ELIMINAR_FICHERO);
    		return false;
    	}
    	return true;
    }
    
    private boolean borrarConHijos(String path, boolean borrarCarpeta) {  
        File file = new File(path);  
        if (!file.exists()) {  
            return true;  
        }  
        if (!file.isDirectory()) {  
            return file.delete();  
        }  
        if ( borrarCarpeta ) return borrarHijos(file) && file.delete();
        else return borrarHijos(file);  
    }  
      
    private boolean borrarHijos(File dir) {  
        File[] hijos = dir.listFiles();  
        boolean hijosBorrados = true;  
        for (int i = 0; hijos != null && i < hijos.length; i++) {  
            File child = hijos[i];  
            if (child.isDirectory()) {  
                hijosBorrados = borrarHijos(child) && hijosBorrados;  
            }  
            if (child.exists()) {  
            	hijosBorrados = child.delete() && hijosBorrados;  
            }  
        }  
        return hijosBorrados;  
    }
    
    public Boolean borra(Long id) {
    	return true;
    }
    
    public MetadatosDto borraPorIdIndicador(Long idIndicador, EncapsuladorErroresSW erros) {
    	MetadatosDto metadatosDto = cargaPorIdIndicador(idIndicador);
        metadatosRepositorio.borrarPorIdIndicador(idIndicador);
        return metadatosDto;
    }
    
    public MetadatosDto guarda(MetadatosDto metadatosDto, EncapsuladorErroresSW erros) {
        Metadatos metadatos = new MetadatosDto2MetadatosTransformer(mapper, metadatosRepositorio).transform(metadatosDto);
        
        Metadatos metadatos2 = metadatosRepositorio.guarda(metadatos);

        return new Metadatos2MetadatosDtoTransformer(mapper).transform(metadatos2);
    }
}