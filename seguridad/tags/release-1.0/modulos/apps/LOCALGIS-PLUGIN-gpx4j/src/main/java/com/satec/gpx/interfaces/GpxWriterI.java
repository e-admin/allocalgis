package com.satec.gpx.interfaces;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.satec.gpx.gpx10.Gpx;
import com.satec.gpx.gpx11.GpxType;

public interface GpxWriterI<T> {
	public static final Class<Gpx> GPX10 = Gpx.class;
	public static final Class<GpxType> GPX11 = GpxType.class;
	
	/**
	 * Devuelve la versión de GPX que soporta la clase
	 * @return versión soportada
	 */
	public String getGpxVersion();
	
	/**
	 * Convierte, via "marshalling" un objeto Gpx/GpxType (1.0/1.1) en el XML definido por su esquema
	 * @param gpx Objeto GPX
	 * @return ByteArrayOutputStream conteniendo el XML bien formado, sin namespaces innecesarios
	 */
	public ByteArrayOutputStream convertGpx(T gpx);

	/**
	 * 
	 * @param gpx Objeto de entrada, pudiendo ser del esquema 1.0 o 1.1
	 * @param filename Nombre del archivo a generar
	 * @return <code>true</code> si se ha generado el archivo correctamente, <code>false</code> si no.
	 */
	public boolean writeGpxFile(T gpx, String filename);
	
	/**
	 * 
	 * @param gpx Objeto de entrada, pudiendo ser del esquema 1.0 o 1.1
	 * @param file Objeto File que representa un archivo a generar
	 * @return <code>true</code> si se ha generado el archivo correctamente, <code>false</code> si no.
	 */
	public boolean writeGpxFile(T gpx, File file);
}
