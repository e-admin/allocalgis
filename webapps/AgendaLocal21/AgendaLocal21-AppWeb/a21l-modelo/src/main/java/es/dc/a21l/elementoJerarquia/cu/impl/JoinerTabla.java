/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.utils.Par;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.ValorFDDto;

public class JoinerTabla extends Joiner<AtributosMapDto, HashMap<String, String>> {

	@Override
	protected HashMap<String, String> getFila(int i, AtributosMapDto tabla) {
		HashMap<String, String> resultado = new HashMap<String, String>();
		LinkedHashMap<String, ValorFDDto> datosEnColumnas = tabla.getContenido();
		Set<String> columnas = datosEnColumnas.keySet();
		for (String columna : columnas) {
			List<EncapsuladorStringSW> valoresColumna = datosEnColumnas.get(columna).getValores();
			EncapsuladorStringSW valor = valoresColumna.get(i);
			resultado.put(columna, valor.getTexto());
		}
		return resultado;
	}

	@Override
	protected int getNumeroFilasDe(AtributosMapDto tabla) {
		LinkedHashMap<String, ValorFDDto> datosEnColumnas = tabla.getContenido();
		Set<String> columnas = datosEnColumnas.keySet();
		for (String columna : columnas) {
			return datosEnColumnas.get(columna).getValores().size();
		}
		return 0;
	}

	@Override
	protected void anadirFilaATabla(AtributosMapDto tabla, HashMap<String, String> fila) {
		LinkedHashMap<String, ValorFDDto> datosEnColumnas = tabla.getContenido();
		String filaInsertada = "";
		for (String columnaEnFila : fila.keySet()) {
			if (!datosEnColumnas.containsKey(columnaEnFila)) {
				datosEnColumnas.put(columnaEnFila, new ValorFDDto());
			}
			String valor = fila.get(columnaEnFila);
			datosEnColumnas.get(columnaEnFila).addValor(new EncapsuladorStringSW(valor));
			filaInsertada += " | " + columnaEnFila + ": " + representarConElipsis(valor, 20) + " | ";
		}
		System.out.println("Insertada fila: " + filaInsertada);
	}

	@Override
	protected HashMap<String, String> concatenarFilas(HashMap<String, String> filaOrigen,
			HashMap<String, String> filaDestino) {
		HashMap<String, String> resultado = new HashMap<String, String>();
		resultado.putAll(filaOrigen);
		resultado.putAll(filaDestino);
		return resultado;
	}

	@Override
	protected boolean matchea(HashMap<String, String> filaOrigen, HashMap<String, String> filaDestino,
			List<Par<String, String>> atributosAMatchear) {
		for (Par<String, String> match : atributosAMatchear) {
			String columnaOrigen = match.getValor1();
			String columnaDestino = match.getValor2();
			String valorEnTablaOrigen = filaOrigen.get(columnaOrigen);
			String valorEnTablaDestino = filaDestino.get(columnaDestino);
			boolean atributosMatchean = sonIguales(valorEnTablaOrigen, valorEnTablaDestino);
			if (!atributosMatchean){
				return false;
			}
		}
		return true;
	}

	private boolean sonIguales(String o1, String o2) {
		if (o1 == null)
			return o2 == null;
		if (o2 == null)
			return false;
		return o1.equals(o2);
	}

	@Override
	protected AtributosMapDto crearNuevaTabla() {
		return new AtributosMapDto();
	}

	@Override
	protected String getNombreTablaJoin(String nombreTablaOrigen, String nombreTablaDestino) {
		return nombreTablaOrigen;
	}

	@Override
	protected String imprimirFila(HashMap<String, String> filaOrigen) {
		String resultado = "";
		for (String clave : filaOrigen.keySet()) {
			String valor = filaOrigen.get(clave);
			valor = representarConElipsis(valor, 20);
			resultado += " | " + clave + ": " + valor + " | ";
		}
		return resultado;
	}

	private String representarConElipsis(String valor, int tamanoMaximo) {
		if (valor == null)
			valor = "<null>";
		if (valor.length() > tamanoMaximo)
			valor = valor.substring(0, tamanoMaximo) + "...";
		return valor;
	}
}