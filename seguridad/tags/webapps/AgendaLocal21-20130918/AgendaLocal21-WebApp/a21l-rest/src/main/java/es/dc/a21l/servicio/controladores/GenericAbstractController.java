/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.servicio.servicios.ServicioConfiguracionGeneral;

public abstract class GenericAbstractController {
	
	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;

	@Autowired
	public void setServicioConfiguracionGeneral(ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
	}
	
	public ServicioConfiguracionGeneral getServicioConfiguracionGeneral() {
		return servicioConfiguracionGeneral;
	}
	
	public String getPathRealAplicacion(HttpServletRequest request, String path) {
		//TODO CAMBIAR ESTA LINEA PARA LA APP REAL
		return path;		
		//return request.getSession().getServletContext().getRealPath(path)+"/";
	}
	
	/**
	 * Comprime os arquivos pasados por parametro e devolve o array de bytes correspondente
	 * @param arquivos Arquivos a comprimir
	 * @return Array de bytes correspondente ao ficheiro comprimido
	 */
	public byte[] compressMultiple(File... arquivos){
		byte[] buffer = new byte[12 * 1024];
		File zipFile = null;
		ZipOutputStream zout = null;

		try{
			zipFile = File.createTempFile("ZIP", ".zip");
			FileOutputStream fout = new FileOutputStream(zipFile);
			zout = new ZipOutputStream(fout);

			for(File archivo : arquivos)
			{
				FileInputStream fin = new FileInputStream(archivo);
				zout.putNextEntry(new ZipEntry(archivo.getName()));
				int length;

				while((length = fin.read(buffer)) > 0)
					zout.write(buffer, 0, length);

				zout.closeEntry();
				fin.close();
			}

			zout.close();
		} catch(IOException e){
			return null;
		}
		
		return convertFileToByteArray(zipFile);
	}
	
	/**
	 * Convirte devolve o array de bytes correspondente ao arquivo pasado por parametro
	 * @param arquivo Arquivo a convertir
	 * @return Array de bytes correspondente
	 */
	private byte[] convertFileToByteArray(File arquivo) {
		byte[] b;
		
		try {
			RandomAccessFile raf = new RandomAccessFile(arquivo, "r");
			b = new byte[(int)raf.length()];
			raf.read(b);
		} catch (Exception e) {
			b = null;
		}
        return b ; 
	}
}
