/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.util.HashMap;
import java.util.List;

import es.dc.a21l.base.utils.MapaRelaciones;
import es.dc.a21l.base.utils.Par;

public abstract class Joiner<TTabla, TFila> {

	public void run(HashMap<String, TTabla> tablas, MapaRelaciones<String, String> relaciones) {
		
		for (Par<String, String> claveRelacion : relaciones.keySet()) {
			List<Par<String, String>> atributosRelacion = relaciones.get(claveRelacion);
			String nombreTablaOrigen = claveRelacion.getValor1();
			TTabla tablaOrigen = tablas.get(nombreTablaOrigen);
			String nombreTablaDestino = claveRelacion.getValor2();
			TTabla tablaDestino = tablas.get(nombreTablaDestino);
			tablas.put(getNombreTablaJoin(nombreTablaOrigen, nombreTablaDestino),
					hacerJoin(tablaOrigen, tablaDestino, atributosRelacion));
		}
	}

	private TTabla hacerJoin(TTabla tablaOrigen, TTabla tablaDestino, List<Par<String, String>> atributos) {
		
		TTabla resultado = crearNuevaTabla();
		int numeroDeFilasEnOrigen = getNumeroFilasDe(tablaOrigen);
		int numeroMatches = 0;
		for (int i = 0; i < numeroDeFilasEnOrigen; i++) {
			TFila filaOrigen = getFila(i, tablaOrigen);
			int numeroDeFilasEnDestino = getNumeroFilasDe(tablaDestino);
			for (int j = 0; j < numeroDeFilasEnDestino; j++) {
				TFila filaDestino = getFila(j, tablaDestino);
				if (matchea(filaOrigen, filaDestino, atributos)) {
					anadirFilaATabla(resultado, concatenarFilas(filaOrigen, filaDestino));
					System.out.println("MATCH ENCONTRADO:\n\t" + imprimirFila(filaOrigen) + "\n\t" + imprimirFila(filaDestino));
					numeroMatches++;
				}
			}
		}
		System.out.println("Matcheadas " + numeroMatches + " filas.");
		return resultado;
	}

	protected abstract String imprimirFila(TFila filaOrigen);

	protected abstract String getNombreTablaJoin(String nombreTablaOrigen, String nombreTablaDestino);

	protected abstract boolean matchea(TFila filaOrigen, TFila filaDestino, List<Par<String, String>> atributos);

	protected abstract TTabla crearNuevaTabla();

	protected abstract TFila getFila(int i, TTabla tablaOrigen);

	protected abstract int getNumeroFilasDe(TTabla tablaOrigen);

	protected abstract void anadirFilaATabla(TTabla resultado, TFila concatenarFilas);

	protected abstract TFila concatenarFilas(TFila filaOrigen, TFila filaDestino);

}