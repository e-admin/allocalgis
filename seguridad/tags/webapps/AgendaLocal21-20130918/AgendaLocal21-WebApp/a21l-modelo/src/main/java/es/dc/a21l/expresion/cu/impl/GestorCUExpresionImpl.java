/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorIntegerSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.GestorCUExpresion;
import es.dc.a21l.expresion.cu.TipoOperacionEmun;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;
import es.dc.a21l.expresion.modelo.Expresion;
import es.dc.a21l.expresion.modelo.ExpresionRepositorio;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.GestorCUAtributoFuenteDatos;
import es.dc.a21l.fuente.cu.ValorFDDto;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatosRepositorio;

public class GestorCUExpresionImpl implements GestorCUExpresion {

	private Mapper mapper;
	private ExpresionRepositorio expresionRepositorio;
	private AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio;
	private GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos;
	private String separadorDecimales = null;
	private static final Logger LOG = LoggerFactory
			.getLogger(GestorCUExpresionImpl.class);

	@Autowired
	public void setAtributoFuenteDatosRepositorio(
			AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio) {
		this.atributoFuenteDatosRepositorio = atributoFuenteDatosRepositorio;
	}

	@Autowired
	public void setGestorCUAtributoFuenteDatos(
			GestorCUAtributoFuenteDatos gestorCUAtributoFuenteDatos) {
		this.gestorCUAtributoFuenteDatos = gestorCUAtributoFuenteDatos;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setExpresionRepositorio(
			ExpresionRepositorio expresionRepositorio) {
		this.expresionRepositorio = expresionRepositorio;
	}

	public ExpresionDto cargaPorId(Long id) {
		return new Expresion2ExpresionDtoTransformer(mapper)
				.transform(expresionRepositorio.carga(id));
	}

	public ExpresionDto guarda(ExpresionDto expresionDto,
			EncapsuladorErroresSW errores) {

		Validador<ExpresionDto> validador = new ExpresionDtoValidador();

		validador.valida(expresionDto, errores);

		if (errores.getHashErrors())
			return null;

		Expresion expresion = new ExpresionDto2ExpresionTransformer(mapper,
				expresionRepositorio, atributoFuenteDatosRepositorio)
				.transform(expresionDto);
		expresionRepositorio.guarda(expresion);
		return new Expresion2ExpresionDtoTransformer(mapper)
				.transform(expresion);

	}

	public void borra(Long id) {
		Stack<Long> pilaElementosBorrar = new Stack<Long>();
		pilaElementosBorrar.push(id);
		while (!pilaElementosBorrar.isEmpty()) {
			Long elementoBorrar = pilaElementosBorrar.pop();
			ExpresionDto expresionDto = cargaPorId(elementoBorrar);
			if (expresionDto.getTipoOperandoIzq().equals(
					TipoOperandoEmun.EXPRESION)) {
				pilaElementosBorrar.push(expresionDto.getIdExpresionIzq());
			}
			if (expresionDto.getTipoOperandoDch().equals(
					TipoOperandoEmun.EXPRESION)) {
				pilaElementosBorrar.push(expresionDto.getIdExpresionDch());
			}

			if (expresionDto.getTipoOperandoIzq().equals(
					TipoOperandoEmun.FUENTE_DATOS)) {
				if (!expresionRepositorio
						.cargaEsUtilizadoAtributoPorMasDeUnaExpresion(expresionDto
								.getAtributoFuenteDatosDtoIzq().getId()))
					gestorCUAtributoFuenteDatos.borra(expresionDto
							.getAtributoFuenteDatosDtoIzq().getId());
			}
			if (expresionDto.getTipoOperandoDch().equals(
					TipoOperandoEmun.FUENTE_DATOS)) {
				if (!expresionRepositorio
						.cargaEsUtilizadoAtributoPorMasDeUnaExpresion(expresionDto
								.getAtributoFuenteDatosDtoDch().getId()))
					gestorCUAtributoFuenteDatos.borra(expresionDto
							.getAtributoFuenteDatosDtoDch().getId());
			}
			expresionRepositorio.borra(elementoBorrar);
		}

	}

	public ExpresionDto guardaExpresion(String expresion, Long idIndicador, String caracterSeparadorDecimales) {
		try {
			
			//Se asigna el caracter separador de decimales al atributo de la clase
			this.separadorDecimales = caracterSeparadorDecimales;
			
			List<String> trozos = TrocearExpresion(expresion);
			if (trozos == null)
				return null;
			ExpresionDto expresionDto = new ExpresionDto();

			TipoOperacionEmun operacion = TipoOperacionEmun
					.getPorRepresentacion(trozos.get(0));
			String operando1 = eliminarParentesisRedundantesExternos(trozos
					.get(1));

			expresionDto.setTipoOperacion(operacion);

			//OPERANDO IZQUIERDO
			expresionDto.setTipoOperandoIzq(identificarTipoOperando(operando1));

			if (expresionDto.getTipoOperandoIzq().equals(TipoOperandoEmun.LITERAL)) {
				
				//Existe un punto en el valor literal
				if (operando1.indexOf(".") != -1) {
					return null;
				}
				
				expresionDto.setLiteralIzq(devolverOperandoLiteral(operando1));
			}
				
			if (expresionDto.getTipoOperandoIzq().equals(TipoOperandoEmun.FUENTE_DATOS)) {
				expresionDto.setAtributoFuenteDatosDtoIzq(devolverOperandoFuenteDatos(operando1, idIndicador));
			}

			if (expresionDto.getTipoOperandoIzq().equals(TipoOperandoEmun.EXPRESION)) {
				expresionDto.setIdExpresionIzq(guardaExpresion(operando1, idIndicador, caracterSeparadorDecimales).getId());
			}
			
			//OPERANDO DERECHO
			if (!(operacion == TipoOperacionEmun.ABS)) {
				
				String operando2 = eliminarParentesisRedundantesExternos(trozos.get(2));
				expresionDto.setTipoOperandoDch(identificarTipoOperando(operando2));

				//Si es un LITERAL (valor introducido por el usuario)
				if (expresionDto.getTipoOperandoDch().equals(TipoOperandoEmun.LITERAL)) {
					
					//Existe un punto en el valor literal
					if (operando2.indexOf(".") != -1) {
						return null;
					}
					
					expresionDto.setLiteralDch(devolverOperandoLiteral(operando2));
				}
				
				if (expresionDto.getTipoOperandoDch().equals(TipoOperandoEmun.FUENTE_DATOS)) {
					expresionDto.setAtributoFuenteDatosDtoDch(devolverOperandoFuenteDatos(operando2, idIndicador));
				}

				if (expresionDto.getTipoOperandoDch().equals(TipoOperandoEmun.EXPRESION)) {
					expresionDto.setIdExpresionDch(guardaExpresion(operando2,idIndicador, caracterSeparadorDecimales).getId());
				}
				
			} else {
				expresionDto.setTipoOperandoDch(TipoOperandoEmun.SIN_OPERANDO);
			}

			// guarda
			EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
			return guarda(expresionDto, errores);
		} catch (Throwable e) {
			LOG.error("Ha ocurrido un error", e);
			return null;
		}
	}

	// devuelve -1 si no existen fuentes en la expresion
	public EncapsuladorIntegerSW cargaNumeroIteracionesExpresion(
			ExpresionDto expresionDto, String caracterSeparador,
			Map<TiposFuente, String> mapaPath) {

		if (expresionDto.getTipoOperandoIzq().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			return gestorCUAtributoFuenteDatos.cargaTamnhoColumna(
					expresionDto.getAtributoFuenteDatosDtoIzq(),
					caracterSeparador, mapaPath);
		}
		if (expresionDto.getTipoOperandoDch().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			return gestorCUAtributoFuenteDatos.cargaTamnhoColumna(
					expresionDto.getAtributoFuenteDatosDtoDch(),
					caracterSeparador, mapaPath);
		}

		if (expresionDto.getTipoOperandoIzq()
				.equals(TipoOperandoEmun.EXPRESION)) {
			return cargaNumeroIteracionesExpresion(
					cargaPorId(expresionDto.getIdExpresionIzq()),
					caracterSeparador, mapaPath);
		}

		if (expresionDto.getTipoOperandoDch()
				.equals(TipoOperandoEmun.EXPRESION)) {
			return cargaNumeroIteracionesExpresion(
					cargaPorId(expresionDto.getIdExpresionDch()),
					caracterSeparador, mapaPath);
		}

		EncapsuladorIntegerSW result = new EncapsuladorIntegerSW();
		result.setValor(-1);

		return result;

	}

	/*
	 * Método que obtiene el número de iteraciones necesarias para los cálculos
	 * de la expresión (non-Javadoc) Llama al método
	 * obtenerTamanhoColumnaEstructura(AtributoFuenteDatosDto
	 * atributoFuenteDatosDto, LinkedHashMap<String, AtributosMapDto>
	 * mapaTablas)
	 * 
	 * @see
	 * es.dc.a21l.expresion.cu.GestorCUExpresion#cargaNumeroIteracionesExpresion
	 * (es.dc.a21l.expresion.cu.ExpresionDto, java.lang.String, java.util.Map)
	 * Devuelve -1 si no existen fuentes en la expresión v.01.31
	 */
	public EncapsuladorIntegerSW cargaNumeroIteracionesExpresionDeEstructura(
			ExpresionDto expresionDto,
			LinkedHashMap<String, AtributosMapDto> mapaTablas) {

		if (expresionDto.getTipoOperandoIzq().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			return gestorCUAtributoFuenteDatos.obtenerTamanhoColumnaEstructura(
					expresionDto.getAtributoFuenteDatosDtoIzq(), mapaTablas);
		}
		if (expresionDto.getTipoOperandoDch().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			return gestorCUAtributoFuenteDatos.obtenerTamanhoColumnaEstructura(
					expresionDto.getAtributoFuenteDatosDtoDch(), mapaTablas);
		}

		if (expresionDto.getTipoOperandoIzq()
				.equals(TipoOperandoEmun.EXPRESION)) {
			return cargaNumeroIteracionesExpresionDeEstructura(
					cargaPorId(expresionDto.getIdExpresionIzq()), mapaTablas);
		}

		if (expresionDto.getTipoOperandoDch()
				.equals(TipoOperandoEmun.EXPRESION)) {
			return cargaNumeroIteracionesExpresionDeEstructura(
					cargaPorId(expresionDto.getIdExpresionDch()), mapaTablas);
		}

		EncapsuladorIntegerSW result = new EncapsuladorIntegerSW();
		result.setValor(-1);

		return result;

	}

	public EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> cargaValoresFuentesExpresion(
			ExpresionDto expresionDto, String caracterSeparador,
			Map<TiposFuente, String> mapaPath) {
		EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> result = new EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>>();
		return cargaValoresFuentesExpresionRecursivo(expresionDto, result,
				caracterSeparador, mapaPath);
	}

	/*
	 * Método que recibe un objeto ExpresionDto, carga los datos de las fuentes
	 * y lo evalua (non-Javadoc)
	 * 
	 * @see
	 * es.dc.a21l.expresion.cu.GestorCUExpresion#cargaEvaluacionExpresion(es
	 * .dc.a21l.expresion.cu.ExpresionDto, java.lang.String, java.util.Map,
	 * java.lang.Integer)
	 */
	public List<String> cargaEvaluacionExpresion(ExpresionDto expresionDto,
			String caracterSeparador, Map<TiposFuente, String> mapaPath,
			Integer tamanhoColumnaAmbito) {

		List<String> result = new ArrayList<String>();

		Integer tamanoColumna = cargaNumeroIteracionesExpresion(expresionDto,
				caracterSeparador, mapaPath).getValor();

		if (tamanoColumna == -1) {
			try {
				for (int i = 0; i < tamanhoColumnaAmbito; i++) {
					result.add(String.valueOf(evaluacionExpresion(expresionDto,
							tamanoColumna, null)));
				}
			} catch (Throwable e) {
				LOG.error("error evaluando expresion", e);
				result.add(null);
			}
			return result;
		}
		EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> mapaValores = cargaValoresFuentesExpresion(
				expresionDto, caracterSeparador, mapaPath);

		for (int i = 0; i < tamanoColumna; i++) {
			try {
				Double resultado = evaluacionExpresion(expresionDto, i,
						mapaValores);
				if (resultado != null) {
					result.add(String.valueOf(resultado));
				} else {
					result.add(null);
				}
			} catch (Throwable e) {
				result.add(null);
				continue;
			}
		}

		return result;
	}

	/*
	 * Método que carga valores de la estructura llamando al método
	 * cargaValoresEstructuraExpresionRecursivo v.01.31
	 */
	public EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> cargaValoresEstructuraExpresion(
			ExpresionDto expresionDto,
			LinkedHashMap<String, AtributosMapDto> mapaTablas) {
		EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> result = new EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>>();
		return cargaValoresEstructuraExpresionRecursivo(expresionDto, result,
				mapaTablas);
	}

	/*
	 * Método que recibe un objeto ExpresionDto, carga los datos de la
	 * estructura y lo evalua (non-Javadoc)
	 * 
	 * @see
	 * es.dc.a21l.expresion.cu.GestorCUExpresion#cargaEvaluacionExpresion(es
	 * .dc.a21l.expresion.cu.ExpresionDto, java.lang.String, java.util.Map,
	 * java.lang.Integer) v.01.31
	 */
	public List<String> cargaEvaluacionExpresionDeEstructura(
			ExpresionDto expresionDto,
			LinkedHashMap<String, AtributosMapDto> mapaTablas,
			Integer tamanhoColumnaAmbito) {

		List<String> result = new ArrayList<String>();

		// Obtiene el tamaño de la columna del primer operando que se encuentra
		// en la expresión
		Integer tamanoColumna = cargaNumeroIteracionesExpresionDeEstructura(
				expresionDto, mapaTablas).getValor();

		// En la expresión no se han encontrado ni operando ni expresiones
		if (tamanoColumna == -1) {

			try {
				for (int i = 0; i < tamanhoColumnaAmbito; i++) {
					result.add(String.valueOf(evaluacionExpresion(expresionDto,
							tamanoColumna, null)));
				}
			} catch (Throwable e) {
				LOG.error("error evaluando expresion", e);
				result.add(null);
			}
			return result;
		}

		EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> mapaValores = cargaValoresEstructuraExpresion(
				expresionDto, mapaTablas);

		for (int i = 0; i < tamanoColumna; i++) {

			try {
				Double resultado = evaluacionExpresion(expresionDto, i,
						mapaValores);
				if (resultado != null) {
					result.add(String.valueOf(resultado));
				} else {
					result.add(null);
				}

			} catch (Throwable e) {
				result.add(null);
				continue;
			}
		}

		return result;
	}

	// FUNCIONES INTERNAS DEL GESTOR

	private Double evaluacionExpresion(
			ExpresionDto expresionDto,
			Integer posicionColumna,
			EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> mapaValoresFuente)
			throws Throwable {
		Double operandoIzq = null;
		Double operandoDch = null;

		// Operando Izquierdo
		if (expresionDto.getTipoOperandoIzq().equals(TipoOperandoEmun.LITERAL))
			operandoIzq = expresionDto.getLiteralIzq();

		if (expresionDto.getTipoOperandoIzq().equals(
				TipoOperandoEmun.FUENTE_DATOS))
			operandoIzq = (mapaValoresFuente
					.get(expresionDto.getAtributoFuenteDatosDtoIzq().getId())
					.get(posicionColumna).getTexto() == null) ? null : Double
					.valueOf(mapaValoresFuente
							.get(expresionDto.getAtributoFuenteDatosDtoIzq()
									.getId()).get(posicionColumna).getTexto());

		if (expresionDto.getTipoOperandoIzq()
				.equals(TipoOperandoEmun.EXPRESION))
			operandoIzq = evaluacionExpresion(
					cargaPorId(expresionDto.getIdExpresionIzq()),
					posicionColumna, mapaValoresFuente);

		// Operando Derecho
		if (expresionDto.getTipoOperandoDch().equals(TipoOperandoEmun.LITERAL))
			operandoDch = expresionDto.getLiteralDch();

		if (expresionDto.getTipoOperandoDch().equals(
				TipoOperandoEmun.FUENTE_DATOS))
			operandoDch = (mapaValoresFuente
					.get(expresionDto.getAtributoFuenteDatosDtoDch().getId())
					.get(posicionColumna).getTexto() == null) ? null : Double
					.valueOf(mapaValoresFuente
							.get(expresionDto.getAtributoFuenteDatosDtoDch()
									.getId()).get(posicionColumna).getTexto());

		if (expresionDto.getTipoOperandoDch()
				.equals(TipoOperandoEmun.EXPRESION))
			operandoDch = evaluacionExpresion(
					cargaPorId(expresionDto.getIdExpresionDch()),
					posicionColumna, mapaValoresFuente);

		return opera(expresionDto.getTipoOperacion(),
				(operandoIzq != null) ? operandoIzq.doubleValue() : null,
				(operandoDch != null) ? operandoDch.doubleValue() : null);
	}

	private Double opera(TipoOperacionEmun tipoOperacionEmun,
			Double operandoIzq, Double operandoDrch) throws Throwable {
		if (operandoIzq != null && operandoDrch != null) {
			// Integer precision= obtenerPrecisionMayorOperandos(operandoIzq,
			// operandoDrch);
			Integer precision = 10;
			if (tipoOperacionEmun.equals(TipoOperacionEmun.SUMA))
				return redondearAPrecision(precision, operandoIzq
						+ operandoDrch);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.RESTA))
				return redondearAPrecision(precision, operandoIzq
						- operandoDrch);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.PRODUCTO))
				return redondearAPrecision(precision, operandoIzq
						* operandoDrch);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.DIVISION))
				return redondearAPrecision(precision, operandoIzq
						/ operandoDrch);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.MODULO))
				return redondearAPrecision(precision, operandoIzq
						% operandoDrch);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.ABS))
				return Math.abs(operandoIzq);

			if (tipoOperacionEmun.equals(TipoOperacionEmun.EXPONENTE))
				return redondearAPrecision(precision,
						Math.pow(operandoIzq, operandoDrch));
		} else {
			if (operandoIzq != null
					&& tipoOperacionEmun.equals(TipoOperacionEmun.ABS)) {
				return Math.abs(operandoIzq);
			}
		}

		return null;
	}

	private Double redondearAPrecision(Integer precision, Double numero) {
		BigDecimal big = new BigDecimal(numero + "");
		return big.setScale(precision, RoundingMode.HALF_UP).doubleValue();
	}

	private Integer obtenerPrecisionMayorOperandos(Double operandoIzq,
			Double operandoDch) {
		Long izqL = Math.round(operandoIzq);
		String izqS = String
				.valueOf(Math.abs(operandoIzq - izqL.doubleValue()));
		Integer resultIzq = izqS.length() - 2;

		Long dchL = Math.round(operandoDch);
		String dchS = String
				.valueOf(Math.abs(operandoDch - dchL.doubleValue()));
		Integer resultDch = dchS.length() - 2;

		if (resultDch < 0 && resultIzq < 0)
			return 5;

		return resultDch > resultIzq ? resultDch : resultIzq;

	}

	private EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> cargaValoresFuentesExpresionRecursivo(
			ExpresionDto expresionDto,
			EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> mapaResultado,
			String caracterSeparador, Map<TiposFuente, String> mapaPath) {

		if (expresionDto.getTipoOperandoIzq().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			mapaResultado.put(expresionDto.getAtributoFuenteDatosDtoIzq()
					.getId(), gestorCUAtributoFuenteDatos.cargaValores(
					expresionDto.getAtributoFuenteDatosDtoIzq(),
					caracterSeparador, mapaPath));
		}
		if (expresionDto.getTipoOperandoDch().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			mapaResultado.put(expresionDto.getAtributoFuenteDatosDtoDch()
					.getId(), gestorCUAtributoFuenteDatos.cargaValores(
					expresionDto.getAtributoFuenteDatosDtoDch(),
					caracterSeparador, mapaPath));
		}

		if (expresionDto.getTipoOperandoIzq()
				.equals(TipoOperandoEmun.EXPRESION)) {
			cargaValoresFuentesExpresionRecursivo(
					cargaPorId(expresionDto.getIdExpresionIzq()),
					mapaResultado, caracterSeparador, mapaPath);
		}

		if (expresionDto.getTipoOperandoDch()
				.equals(TipoOperandoEmun.EXPRESION)) {
			cargaValoresFuentesExpresionRecursivo(
					cargaPorId(expresionDto.getIdExpresionDch()),
					mapaResultado, caracterSeparador, mapaPath);
		}

		return mapaResultado;

	}

	/*
	 * Método que carga los valores de las columnas (tabla - atributo) de la
	 * estructura de datos de manera recursiva Recibe como argumento la
	 * expresión, la estructua donde se almacenan los valores y la estructura de
	 * donde se leen los valores v.01.31
	 */
	private EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> cargaValoresEstructuraExpresionRecursivo(
			ExpresionDto expresionDto,
			EncapsuladorMapSW<Long, EncapsuladorListSW<EncapsuladorStringSW>> mapaResultado,
			LinkedHashMap<String, AtributosMapDto> mapaTablas) {

		if (expresionDto.getTipoOperandoIzq().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			mapaResultado.put(expresionDto.getAtributoFuenteDatosDtoIzq()
					.getId(), gestorCUAtributoFuenteDatos
					.cargaValoresEstructura(
							expresionDto.getAtributoFuenteDatosDtoIzq(),
							mapaTablas));
		}
		if (expresionDto.getTipoOperandoDch().equals(
				TipoOperandoEmun.FUENTE_DATOS)) {
			mapaResultado.put(expresionDto.getAtributoFuenteDatosDtoDch()
					.getId(), gestorCUAtributoFuenteDatos
					.cargaValoresEstructura(
							expresionDto.getAtributoFuenteDatosDtoDch(),
							mapaTablas));
		}

		if (expresionDto.getTipoOperandoIzq()
				.equals(TipoOperandoEmun.EXPRESION)) {
			cargaValoresEstructuraExpresionRecursivo(
					cargaPorId(expresionDto.getIdExpresionIzq()),
					mapaResultado, mapaTablas);
		}

		if (expresionDto.getTipoOperandoDch()
				.equals(TipoOperandoEmun.EXPRESION)) {
			cargaValoresEstructuraExpresionRecursivo(
					cargaPorId(expresionDto.getIdExpresionDch()),
					mapaResultado, mapaTablas);
		}

		return mapaResultado;
	}

	/*
	 * MÉTODOS CREADOS PARA LA CARGA INICIAL DE COLUMNAS EN LA ESTRUCTURA
	 * MAPATABLAS QUE LUEGO SE LEERÁN PARA EL CÁLCULO DE LAS FÓRMULAS
	 */

	/*
	 * Método que por cada ATRIBUTO de la lista de atributos que sea fórmula, la
	 * analiza y carga la lista de valores (de las fuentes) de cada atributo que
	 * participa en dicha fórmula v.01.31
	 */
	public LinkedHashMap<String, AtributosMapDto> cargaInicialValoresDeFuentesExpresionEnEstructura(
			ExpresionDto expresionDto,
			LinkedHashMap<String, AtributosMapDto> mapaTablas,
			String caracterSeparador, Map<TiposFuente, String> mapaPath) {
		return cargarValoresDeFuentesExpresionEnEstructura(expresionDto,
				mapaTablas, caracterSeparador, mapaPath);
	}

	/*
	 * Método que carga los valores de las columnas (tabla - atributo) de las
	 * fuentes de datos externas de manera recursiva. Almacena los valores en la
	 * estructura final LinkedHashMap<String, AtributosMapDto> mapaTablas
	 * v.01.31
	 */
	private LinkedHashMap<String, AtributosMapDto> cargarValoresDeFuentesExpresionEnEstructura(
			ExpresionDto expresionDto,
			LinkedHashMap<String, AtributosMapDto> mapaTablas,
			String caracterSeparador, Map<TiposFuente, String> mapaPath) {

		AtributoFuenteDatosDto atributoIzquierdo = expresionDto
				.getAtributoFuenteDatosDtoIzq();
		TipoOperandoEmun operandoIzquierdo = expresionDto.getTipoOperandoIzq();
		if (operandoIzquierdo.equals(TipoOperandoEmun.FUENTE_DATOS)) {
			introducirValoresAtributoEnEstructura(mapaTablas,
					caracterSeparador, mapaPath, atributoIzquierdo);
		}

		AtributoFuenteDatosDto atributoDerecho = expresionDto
				.getAtributoFuenteDatosDtoDch();
		TipoOperandoEmun operandoDerecho = expresionDto.getTipoOperandoDch();
		if (operandoDerecho.equals(TipoOperandoEmun.FUENTE_DATOS)) {
			introducirValoresAtributoEnEstructura(mapaTablas,
					caracterSeparador, mapaPath, atributoDerecho);
		}

		if (operandoIzquierdo.equals(TipoOperandoEmun.EXPRESION)) {
			cargarValoresDeFuentesExpresionEnEstructura(
					cargaPorId(expresionDto.getIdExpresionIzq()), mapaTablas,
					caracterSeparador, mapaPath);
		}

		if (operandoDerecho.equals(TipoOperandoEmun.EXPRESION)) {
			cargarValoresDeFuentesExpresionEnEstructura(
					cargaPorId(expresionDto.getIdExpresionDch()), mapaTablas,
					caracterSeparador, mapaPath);
		}

		return mapaTablas;

	}

	/*
	 * Método interno que almacena en la estructura de datos la lista de valores
	 * del atributo concreto que participa de la expresión (FÓRMULA) v.01.31
	 */
	private void introducirValoresAtributoEnEstructura(
			LinkedHashMap<String, AtributosMapDto> mapaTablas,
			String caracterSeparador, Map<TiposFuente, String> mapaPath,
			AtributoFuenteDatosDto atributo) {

		boolean existeTabla = existeTablaEnEstructura(atributo, mapaTablas);
		String strNombreTabla = atributo.getTabla().getNombre();
		ValorFDDto valoresColumna = new ValorFDDto();
		valoresColumna.setValores(gestorCUAtributoFuenteDatos.cargaValores(
				atributo, caracterSeparador, mapaPath));
		if (existeTabla) {
			if (!existeAtributoEnTablaEstructura(strNombreTabla, mapaTablas,
					atributo.getNombre() + "_formula")) {
				LinkedHashMap<String, ValorFDDto> mapaAtributosTabla = mapaTablas
						.get(strNombreTabla).getContenido();
				mapaAtributosTabla.put(atributo.getNombre() + "_formula",
						valoresColumna);
			}
		} else {
			AtributosMapDto mapaAtributosTabla = new AtributosMapDto();
			LinkedHashMap<String, ValorFDDto> mapaColumnaTabla = new LinkedHashMap<String, ValorFDDto>();
			mapaColumnaTabla.put(atributo.getNombre() + "_formula",
					valoresColumna);
			mapaAtributosTabla.setContenido(mapaColumnaTabla);
			mapaTablas.put(strNombreTabla, mapaAtributosTabla);
		}
	}

	/*
	 * Método interno que comprueba si en la estructura de datos
	 * LinkedHashMap<String, AtributosMapDto> existe una entrada para una tabla
	 * concreta v.01.31
	 */
	private boolean existeTablaEnEstructura(AtributoFuenteDatosDto atributo,
			LinkedHashMap<String, AtributosMapDto> mapaTablas) {

		boolean existe = false;

		String strNombreTabla = atributo.getTabla().getNombre();

		AtributosMapDto atributosTablaEnEstructura = mapaTablas
				.get(strNombreTabla);
		if (atributosTablaEnEstructura != null)
			return true;

		return existe;
	}

	/*
	 * Método que comprueba una estructura del tipo LinkedHashMap<String,
	 * AtributosMapDto> v.01.31
	 */
	private boolean existeAtributoEnTablaEstructura(String strNombreTabla,
			LinkedHashMap<String, AtributosMapDto> mapaTablas,
			String strNomAtributoInsertar) {

		AtributosMapDto atributosTablaEnEstructura = mapaTablas
				.get(strNombreTabla);
		Set<String> setAtributosTabla = atributosTablaEnEstructura
				.getContenido().keySet();
		Iterator<String> itAtributosTabla = setAtributosTabla.iterator();
		String strAtributoTablaResultado = null;
		while (itAtributosTabla.hasNext()) {
			strAtributoTablaResultado = itAtributosTabla.next();
			if (strAtributoTablaResultado.equals(strNomAtributoInsertar))
				return true;
		}
		return false;
	}

	private TipoOperandoEmun identificarTipoOperando(String operando) {

		if (operando.startsWith("\"") && operando.endsWith("\"")
				&& numeroAparicionesSecuencia(operando, "\"") == 2)
			return TipoOperandoEmun.LITERAL;
		if (operando.startsWith("[") && operando.endsWith("]")
				&& numeroAparicionesSecuencia(operando, "[") == 1
				&& numeroAparicionesSecuencia(operando, "]") == 1)
			return TipoOperandoEmun.FUENTE_DATOS;

		return TipoOperandoEmun.EXPRESION;
	}

	private Integer numeroAparicionesSecuencia(String cadena, String secuencia) {
		Integer lastPosition = 0;
		Integer resultado = 0;
		while ((lastPosition = cadena.indexOf(secuencia, lastPosition)) != -1) {
			resultado++;
			lastPosition++;
		}
		return resultado;
	}

	private Double devolverOperandoLiteral(String operando) {
		String cadenaAParsear = operando.replaceAll("\"", "");
		//cadenaAParsear = cadenaAParsear.replaceAll("\\.", "");
		cadenaAParsear = cadenaAParsear.replaceAll(this.separadorDecimales, ".");
		return Double.valueOf(cadenaAParsear);
	}

	private String getSeparadorDecimalesConfigurado() {
		if (separadorDecimales == null) {
			separadorDecimales = obtenerSeparadorDecimalesDesdeConfiguracion();
		}
		return separadorDecimales;
	}

	private String obtenerSeparadorDecimalesDesdeConfiguracion() {
		// TODO Auto-generated method stub
		return ",";
	}

	private AtributoFuenteDatosDto devolverOperandoFuenteDatos(String operando,
			Long idIndicador) {
		EncapsuladorErroresSW errores = new EncapsuladorErroresSW();
		// se elimninan los corchetes
		return gestorCUAtributoFuenteDatos.guardaOCargaPorCadenaExpresion(
				operando.substring(1, operando.length() - 1), idIndicador,
				errores);
	}

	// devuelve en la primera posicion la operacion en la segunda el primer
	// operando y en la tercera el segundo si existe
	private List<String> TrocearExpresion(String expresion) {
		expresion = eliminarParentesisRedundantesExternos(expresion);
		Map<TipoOperacionEmun, List<Integer>> posicionOperacionessExternas = obtenerPosicionOperancionesExternas(expresion);
		Map<TipoOperacionEmun, Integer> mapaOperacion = obtenerOperacionExterna(posicionOperacionessExternas);

		if (mapaOperacion.isEmpty() || mapaOperacion.size() != 1)
			return null;

		List<String> result = new ArrayList<String>();

		for (TipoOperacionEmun tipoOperacionEmun : mapaOperacion.keySet()) {
			result.add(0, tipoOperacionEmun.getRepresentacion());

			if (tipoOperacionEmun.equals(TipoOperacionEmun.ABS)) {
				result.add(1, expresion.substring(
						mapaOperacion.get(tipoOperacionEmun) + 4,
						expresion.length() - 1));
				return result;
			}
			result.add(1, expresion.substring(0,
					mapaOperacion.get(tipoOperacionEmun)));
			result.add(2, expresion.substring(
					mapaOperacion.get(tipoOperacionEmun) + 1,
					expresion.length()));
		}

		return result;

	}

	private Map<TipoOperacionEmun, Integer> obtenerOperacionExterna(
			Map<TipoOperacionEmun, List<Integer>> mapa) {
		Map<TipoOperacionEmun, Integer> result = new HashMap<TipoOperacionEmun, Integer>();

		if (!mapa.get(TipoOperacionEmun.ABS).isEmpty()) {
			result.put(TipoOperacionEmun.ABS, mapa.get(TipoOperacionEmun.ABS)
					.get(0));
			return result;
		}

		if (!mapa.get(TipoOperacionEmun.SUMA).isEmpty()) {
			if (!mapa.get(TipoOperacionEmun.RESTA).isEmpty()) {
				if (mapa.get(TipoOperacionEmun.RESTA).get(0) < mapa.get(
						TipoOperacionEmun.SUMA).get(0)) {
					result.put(TipoOperacionEmun.RESTA,
							mapa.get(TipoOperacionEmun.RESTA).get(0));
					return result;
				}
			}
			result.put(TipoOperacionEmun.SUMA, mapa.get(TipoOperacionEmun.SUMA)
					.get(0));
			return result;

		}

		if (!mapa.get(TipoOperacionEmun.RESTA).isEmpty()) {
			result.put(TipoOperacionEmun.RESTA,
					mapa.get(TipoOperacionEmun.RESTA).get(0));
			return result;
		}

		if (!mapa.get(TipoOperacionEmun.PRODUCTO).isEmpty()) {
			if (!mapa.get(TipoOperacionEmun.DIVISION).isEmpty()) {
				if (mapa.get(TipoOperacionEmun.DIVISION).get(0) < mapa.get(
						TipoOperacionEmun.PRODUCTO).get(0)) {
					result.put(TipoOperacionEmun.DIVISION,
							mapa.get(TipoOperacionEmun.DIVISION).get(0));
					return result;
				}
			}
			result.put(TipoOperacionEmun.PRODUCTO,
					mapa.get(TipoOperacionEmun.PRODUCTO).get(0));
			return result;

		}

		if (!mapa.get(TipoOperacionEmun.DIVISION).isEmpty()) {
			result.put(TipoOperacionEmun.DIVISION,
					mapa.get(TipoOperacionEmun.DIVISION).get(0));
			return result;
		}

		if (!mapa.get(TipoOperacionEmun.EXPONENTE).isEmpty()) {
			result.put(TipoOperacionEmun.EXPONENTE,
					mapa.get(TipoOperacionEmun.EXPONENTE).get(0));
			return result;
		}

		if (!mapa.get(TipoOperacionEmun.MODULO).isEmpty()) {
			result.put(TipoOperacionEmun.MODULO,
					mapa.get(TipoOperacionEmun.MODULO).get(0));
			return result;
		}

		return result;
	}

	private String eliminarParentesisRedundantesExternos(String expresion) {
		while (expresion.startsWith("(")
				&& expresion.endsWith(")")
				&& esParentesisRedundante(expresion.substring(1,
						expresion.length() - 1)))
			expresion = expresion.substring(1, expresion.length() - 1);
		return expresion;
	}

	private Boolean esParentesisRedundante(String criterio) {
		Character c;
		Stack<Character> pila = new Stack<Character>();
		for (Integer i = 0; i < criterio.length(); i++) {
			c = criterio.charAt(i);
			if (c.equals('(')) {
				pila.push(c);
			}
			if (c.equals(')')) {
				if (!pila.empty()) {
					pila.pop();
				} else {
					return false;
				}
			}
		}
		if (pila.empty()) {
			return true;
		} else {
			return false;
		}
	}

	private Map<TipoOperacionEmun, List<Integer>> obtenerPosicionOperancionesExternas(
			String expresion) {
		Map<TipoOperacionEmun, List<Integer>> result = new HashMap<TipoOperacionEmun, List<Integer>>();
		Map<Integer, Integer> mapaParentesis = obtenerPosicionesParentesisExternos(expresion);

		Integer lastPosicionView = 0;
		for (TipoOperacionEmun tipoOperacionEmun : TipoOperacionEmun
				.getListaValoresEvaluacion()) {
			lastPosicionView = 0;
			List<Integer> listaPosicines = new ArrayList<Integer>();

			if (tipoOperacionEmun.equals(TipoOperacionEmun.ABS)) {
				if ((lastPosicionView = expresion.indexOf(
						tipoOperacionEmun.getRepresentacion() + "(",
						lastPosicionView)) != -1) {
					if (!estaEntreParentesis(mapaParentesis, lastPosicionView)
							&& mapaParentesis.get(lastPosicionView + 3) == expresion
									.length() - 1) {
						listaPosicines.add(lastPosicionView);
						result.put(tipoOperacionEmun, listaPosicines);
						return result;
					}
				}
				result.put(tipoOperacionEmun, listaPosicines);
				continue;
			}

			while ((lastPosicionView = expresion.indexOf(
					tipoOperacionEmun.getRepresentacion(), lastPosicionView)) != -1) {
				if (!estaEntreParentesis(mapaParentesis, lastPosicionView)
						&& !esNumeroLaCadena(expresion.substring(
								lastPosicionView, lastPosicionView + 2)))
					listaPosicines.add(lastPosicionView);
				lastPosicionView++;

			}
			result.put(tipoOperacionEmun, listaPosicines);

		}

		return result;

	}

	private Boolean esNumeroLaCadena(String cadena) {
		try {
			Long.valueOf(cadena);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private Boolean estaEntreParentesis(Map<Integer, Integer> mapaParentesis,
			Integer posicion) {
		for (Integer i : mapaParentesis.keySet()) {
			if (i < posicion && mapaParentesis.get(i) > posicion)
				return true;
		}
		return false;
	}

	private Map<Integer, Integer> obtenerPosicionesParentesisExternos(
			String expresion) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		if (expresion.isEmpty())
			return result;

		List<Integer> pilaParentesis = new ArrayList<Integer>();
		Character c;
		for (Integer i = 0; i < expresion.length(); i++) {
			c = expresion.charAt(i);
			if (c.equals('('))
				pilaParentesis.add(i);
			if (c.equals(')')) {
				if (pilaParentesis.size() == 1)
					result.put(pilaParentesis.get(0), i);

				pilaParentesis.remove(pilaParentesis.size() - 1);
			}
		}

		return result;
	}

}
