/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErrorSW;
import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorStringSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.GestorCUCriterio;
import es.dc.a21l.criterio.cu.TipoOperacionCriterioEmun;
import es.dc.a21l.criterio.cu.TipoOperandoCriterioEmun;
import es.dc.a21l.criterio.modelo.Criterio;
import es.dc.a21l.criterio.modelo.CriterioRepositorio;
import es.dc.a21l.elementoJerarquia.cu.impl.IndicadorDtoFormErrorsEmun;
import es.dc.a21l.fuente.modelo.AtributoRepositorio;


public class GestorCUCriterioImpl implements GestorCUCriterio {

	private Mapper mapper;
	private CriterioRepositorio criterioRepositorio;
	private AtributoRepositorio atributoRepositorio;
	private static final Logger LOG = LoggerFactory.getLogger(GestorCUCriterioImpl.class);
	private String separadorDecimales = null;
	
	@Autowired
	public void setAtributoRepositorio(AtributoRepositorio atributoRepositorio) {
		this.atributoRepositorio = atributoRepositorio;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setCriterioRepositorio(CriterioRepositorio criterioRepositorio) {
		this.criterioRepositorio = criterioRepositorio;
	}
	
	public CriterioDto cargaPorId(Long id){
		return new Criterio2CriterioDtoTransformer(mapper).transform(criterioRepositorio.carga(id));
	}
	
	
	public CriterioDto guarda(CriterioDto criterioDto,EncapsuladorErroresSW errores){
		
		Validador<CriterioDto> validador= new CriterioDtoValidador();
		validador.valida(criterioDto, errores);
		
		if(errores.getHashErrors())
			return null;
		
		Criterio criterio = criterioRepositorio.guarda(new CriterioDto2CriterioTransformer(mapper, criterioRepositorio, atributoRepositorio).transform(criterioDto));
		
		return new Criterio2CriterioDtoTransformer(mapper).transform(criterio);
	}
	
	
	public CriterioDto guardaNuevoCriterio(Long idAtributo, String cadenaCriterio,String tipoColumna,EncapsuladorErroresSW errores, String caracterSeparadorDecimales){
		
		this.separadorDecimales=caracterSeparadorDecimales;
		
		CriterioDto result= guardaCriterio(cadenaCriterio.replaceAll(" ", ""),tipoColumna,errores);
		if(result==null)
			return null;
		result.setCadenaCriterio(cadenaCriterio);
		result.setIdAtributo(idAtributo);
		return guarda(result, errores);
	}
	
	
	public Set<Integer> cargaPosicionesNoValidas(CriterioDto criterioDto,List<String> columna){
		Set<Integer> result= new HashSet<Integer>();
		Double valor=null;
		String temp=null;
		
		for(int i=0;i<columna.size();i++){
			temp=columna.get(i);
			try{
				valor=Double.valueOf(temp);
				if(!cargaEvaluacionCriterio(criterioDto, valor))
					result.add(i);
				
			}catch (Exception e) {
				LOG.error("error evaluando el criterio", e);
				continue;
			}
		}
		
		return result;
	}
	
	
	public Set<Integer> cargaPosicionesNoValidasESW(CriterioDto criterioDto,List<EncapsuladorStringSW> columna, TipoAtributoFD tipoAtributo){
		Set<Integer> result= new HashSet<Integer>();
		Double valor=null;
		String temp=null;
		
		for(int i=0;i<columna.size();i++){
			temp=columna.get(i).getTexto();
			try{
				if (!esCadena(temp) || tipoAtributo==TipoAtributoFD.VALORFDNUMERICO){
					valor=Double.valueOf(temp);
					if(!cargaEvaluacionCriterio(criterioDto, valor))
						result.add(i);
				}
				else{
					if(!cargaEvaluacionCriterioTexto(criterioDto, temp))
						result.add(i);
				}
			}
			catch (NumberFormatException e) {
				LOG.error("El valor no puede ser convertido a Double");
				continue;
			}
			catch (Exception e) {
				LOG.error("error evaluando el criterio", e);
				continue;
			}
		}
		
		return result;
	}
	
	
	
	public Boolean cargaEvaluacionCriterio(CriterioDto criterioDto, Double valor) throws Exception{
			return opera(criterioDto, valor, TipoOperacionCriterioEmun.getComparaciones());
	}
	
	public Boolean cargaEvaluacionCriterioTexto(CriterioDto criterioDto, String valor) throws Exception{
		return operaTexto(criterioDto, valor, TipoOperacionCriterioEmun.getComparaciones());
	}
	
	public void borraPorIdIndicador(Long id) {
		List<Criterio> lista = new ArrayList<Criterio>();
	    lista = criterioRepositorio.cargaPorIdIndicador(id);
	    for ( Criterio crit : lista ) {
	    	criterioRepositorio.borra(crit.getId());
	    }
	}
	
	public EncapsuladorListSW<CriterioDto> cargarPorIdIndicador(Long idIndicador) {
		List<Criterio> lista = criterioRepositorio.cargaPorIdIndicador(idIndicador);
		Criterio2CriterioDtoTransformer transformer = new Criterio2CriterioDtoTransformer(mapper);
		EncapsuladorListSW<CriterioDto> result = new EncapsuladorListSW<CriterioDto>();
		for ( Criterio crit : lista ) {
			result.add(transformer.transform(crit));
		}
		return result;
	}
	
	public CriterioDto borra(Long id) {
		criterioRepositorio.borra(id);
		Criterio2CriterioDtoTransformer transformer = new Criterio2CriterioDtoTransformer(mapper);
		return transformer.transform(criterioRepositorio.carga(id));
	}
	
	public CriterioDto cargaPorAtributo(Long idAtributo){
		Criterio criterio= criterioRepositorio.cargaPorAtributo(idAtributo);
		if(criterio==null)
			return null;
		return new Criterio2CriterioDtoTransformer(mapper).transform(criterio);
	}
	
	
	//FUNCIONES INTERNAS
		
	private Boolean opera(CriterioDto criterioDto, Double valor, List<TipoOperacionCriterioEmun> operadoresAritmeticos) throws Exception{
		Double operandoIzq=null;
		Double operandoDch=null;
		Boolean valorIzq=null;
		Boolean valorDch=null;
		
		//OperandoIzq
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO))
			operandoIzq=valor;
			
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL))
			operandoIzq=criterioDto.getLiteralIzq();
			
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO))
			valorIzq=opera(cargaPorId(criterioDto.getIdCriterioIzq()), valor, operadoresAritmeticos);
		
		
		//OperandoDch
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO))
			operandoDch=valor;
			
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL))
			operandoDch=criterioDto.getLiteralDch();
			
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO))
			valorDch=opera(cargaPorId(criterioDto.getIdCriterioDch()), valor, operadoresAritmeticos);
		
		
		//Operador
		if(operadoresAritmeticos.contains(criterioDto.getTipoOperacion()))
			return operaAritmetica(criterioDto.getTipoOperacion(), operandoIzq, operandoDch);
		
		return operaLogica(criterioDto.getTipoOperacion(), valorIzq, valorDch);
		
		
	}
	
	private Boolean operaTexto(CriterioDto criterioDto, String valor, List<TipoOperacionCriterioEmun> operadoresTexto) throws Exception{
		String operandoIzq=null;
		String operandoDch=null;
		Boolean valorIzq=null;
		Boolean valorDch=null;
		
		//OperandoIzq
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO))
			operandoIzq=valor;
			
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL))
			operandoIzq=criterioDto.getStrLiteralIzq();
			
		if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO))
			valorIzq=operaTexto(cargaPorId(criterioDto.getIdCriterioIzq()), valor, operadoresTexto);
		
		
		//OperandoDch
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO))
			operandoDch=valor;
			
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL))
			operandoDch=criterioDto.getStrLiteralDch();
			
		if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO))
			valorDch=operaTexto(cargaPorId(criterioDto.getIdCriterioDch()), valor, operadoresTexto);
		
		
		//Operador
		if(operadoresTexto.contains(criterioDto.getTipoOperacion()))
			return operaTexto(criterioDto.getTipoOperacion(), operandoIzq, operandoDch);
		
		return operaLogica(criterioDto.getTipoOperacion(), valorIzq, valorDch);
		
		
	}
	
	private Boolean operaLogica(TipoOperacionCriterioEmun operacion,Boolean valorIzq,Boolean valorDch) throws Exception{
		
		if(operacion.equals(TipoOperacionCriterioEmun.AND))
			return valorIzq & valorDch;
		
		if(operacion.equals(TipoOperacionCriterioEmun.OR))
			return valorIzq | valorDch;
		
		if(operacion.equals(TipoOperacionCriterioEmun.NOT))
			return !valorIzq;
		
		throw new Exception("Fallo Operando Logica");
		
	}
	
	private Boolean operaAritmetica(TipoOperacionCriterioEmun operacion,Double valorIzq ,Double valorDch) throws Exception{
		int resultado= valorIzq.compareTo(valorDch);
		
		if(operacion.equals(TipoOperacionCriterioEmun.MAYOR))
			return resultado>0;
			
		if(operacion.equals(TipoOperacionCriterioEmun.MAYOR_IGUAL))
			return resultado>=0;
			
		if(operacion.equals(TipoOperacionCriterioEmun.MENOR))
			return resultado<0;
			
		if(operacion.equals(TipoOperacionCriterioEmun.MENOR_IGUAL))
			return resultado<=0;
			
		if(operacion.equals(TipoOperacionCriterioEmun.IGUAL))
			return resultado==0;
		
		if(operacion.equals(TipoOperacionCriterioEmun.DISTINTO))
			return resultado!=0;
			
		throw new Exception("Fallo Operando Aritmetica");
	}
	
	private Boolean operaTexto(TipoOperacionCriterioEmun operacion, String valorIzq ,String valorDch) throws Exception{
		
		int resultado= valorIzq.toUpperCase().compareTo(valorDch.toUpperCase());
		
		if(operacion.equals(TipoOperacionCriterioEmun.IGUAL))
			return resultado==0;
		
		if(operacion.equals(TipoOperacionCriterioEmun.DISTINTO))
			return resultado!=0;
			
		throw new Exception("Fallo Operando Texto");
	}
	
	private CriterioDto guardaCriterio(String expresion,String tipoColumna,EncapsuladorErroresSW errores){
		try {
			List<String> trozos= TrocearExpresion(expresion);
			if ( trozos == null )
				return null;
			CriterioDto criterioDto= new CriterioDto();
			
			TipoOperacionCriterioEmun operacion= TipoOperacionCriterioEmun.getPorRepresentacion(trozos.get(0));
			criterioDto.setTipoOperacion(operacion);
			
			//OPERANDO IZQUIERDO
			String operando1=eliminarParentesisRedundantesExternos(trozos.get(1));
			criterioDto.setTipoOperandoIzq(identificarTipoOperando(operando1));
			
			if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL)) {

				if (esCadena(operando1)) {
					if(TipoAtributoFD.VALORFDNUMERICO.getDescripcion().equals(tipoColumna)) {
						errores.setHashErrors(true);
						EncapsuladorErrorSW error=new EncapsuladorErrorSW();
						errores.getListaErrores().add(error);
						return null;
					} else {
						criterioDto.setStrLiteralIzq(operando1.substring(1,operando1.length()-1));
						criterioDto.setLiteralIzq(null);
					}
				}
				else {
					//Existe un punto en el valor literal
					if (operando1.indexOf(".") != -1) {
						return null;
					}
					criterioDto.setLiteralIzq(devolverOperandoLiteral(operando1));
					criterioDto.setStrLiteralIzq(null);
				}
			}
			if(criterioDto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO))
				criterioDto.setIdCriterioIzq(guardaCriterio(operando1,tipoColumna,errores).getId());
			
			//operando derecho
			if(!operacion.equals(TipoOperacionCriterioEmun.NOT)){
				
				String operando2=eliminarParentesisRedundantesExternos(trozos.get(2));
				criterioDto.setTipoOperandoDch(identificarTipoOperando(operando2));
				
				if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL)){
					
					if (esCadena(operando2)) {
						if(TipoAtributoFD.VALORFDNUMERICO.getDescripcion().equals(tipoColumna)){
							errores.setHashErrors(true);
							EncapsuladorErrorSW error=new EncapsuladorErrorSW();
							errores.getListaErrores().add(error);
							return null;
						}else{
							criterioDto.setStrLiteralDch(operando2.substring(1,operando2.length()-1));
							criterioDto.setLiteralDch(null);
						}
					}
					else{
						
						//Existe un punto en el valor literal
						if (operando2.indexOf(".") != -1) {
							return null;
						}
						
						criterioDto.setLiteralDch(devolverOperandoLiteral(operando2));
						criterioDto.setStrLiteralDch(null);
					}
				}
				if(criterioDto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO))
					criterioDto.setIdCriterioDch(guardaCriterio(operando2,tipoColumna,errores).getId());
				
			}else{
				criterioDto.setTipoOperandoDch(TipoOperandoCriterioEmun.SIN_OPERANDO);
			}
	
			return guarda(criterioDto, errores);
		} catch (Throwable e) {
			LOG.error(e.getMessage());
			return null;
		}
		
	}
	
	private List<String> TrocearExpresion(String expresion){
		expresion=eliminarParentesisRedundantesExternos(expresion);
		Map<TipoOperacionCriterioEmun,List<Integer>> posicionOperacionessExternas=obtenerPosicionOperancionesExternas(expresion);
		Map<TipoOperacionCriterioEmun, Integer> mapaOperacion= obtenerOperacionExterna(posicionOperacionessExternas);
		
		if(mapaOperacion.isEmpty() || mapaOperacion.size()!=1)
			return null;
		
		List<String> result= new ArrayList<String>();
		
		for(TipoOperacionCriterioEmun tipoOperacionEmun:mapaOperacion.keySet()){
			result.add(0, tipoOperacionEmun.getRepresentacion());
			
			if(tipoOperacionEmun.equals(TipoOperacionCriterioEmun.NOT)){
				result.add(1, expresion.substring(mapaOperacion.get(tipoOperacionEmun)+tipoOperacionEmun.getRepresentacion().length()+1,expresion.length()-1));
				return result;
			}
			result.add(1,expresion.substring(0, mapaOperacion.get(tipoOperacionEmun)));
			result.add(2,expresion.substring(mapaOperacion.get(tipoOperacionEmun)+tipoOperacionEmun.getRepresentacion().length(),expresion.length()));
		}
		
		return result;
					
	}
	
	
	private String eliminarParentesisRedundantesExternos(String expresion){
		while(expresion.startsWith("(") && expresion.endsWith(")") && esParentesisRedundante(expresion.substring(1, expresion.length()-1)))
			expresion=expresion.substring(1, expresion.length()-1);
		return expresion;
	}
	
	
	private Map<TipoOperacionCriterioEmun,List<Integer>> obtenerPosicionOperancionesExternas(String expresion){
		Map<TipoOperacionCriterioEmun,List<Integer>> result= new HashMap<TipoOperacionCriterioEmun, List<Integer>>();
		Map<Integer, Integer> mapaParentesis= obtenerPosicionesParentesisExternos(expresion);
		
		Integer lastPosicionView=0;
		for(TipoOperacionCriterioEmun tipoOperacionEmun:TipoOperacionCriterioEmun.getListaValoresEvaluacion()){
			lastPosicionView=0;
			List<Integer> listaPosicines= new ArrayList<Integer>();
			
			if(tipoOperacionEmun.equals(TipoOperacionCriterioEmun.NOT)){
				if((lastPosicionView=expresion.indexOf(tipoOperacionEmun.getRepresentacion()+"(", lastPosicionView))!=-1){
					if(!estaEntreParentesis(mapaParentesis, lastPosicionView) && mapaParentesis.get(lastPosicionView+TipoOperacionCriterioEmun.NOT.getRepresentacion().length())==expresion.length()-1){
						listaPosicines.add(lastPosicionView);
						result.put(tipoOperacionEmun, listaPosicines);
						return result;
					}
				}
				result.put(tipoOperacionEmun, listaPosicines);
				continue;
			}
			
			while((lastPosicionView=expresion.indexOf(tipoOperacionEmun.getRepresentacion(), lastPosicionView))!=-1){
				if(!estaEntreParentesis(mapaParentesis, lastPosicionView) && operacionInequivoca(tipoOperacionEmun, expresion.substring(lastPosicionView-1, lastPosicionView+2)))
					listaPosicines.add(lastPosicionView);
				lastPosicionView++;
					
			}
			result.put(tipoOperacionEmun, listaPosicines);
			
		}
		
		return result;
		
	}
	
	private Boolean operacionInequivoca(TipoOperacionCriterioEmun tipoOperacionEmun,String operacion){
		if(tipoOperacionEmun.equals(TipoOperacionCriterioEmun.MAYOR) || tipoOperacionEmun.equals(TipoOperacionCriterioEmun.MENOR) || tipoOperacionEmun.equals(TipoOperacionCriterioEmun.IGUAL)){
			if(operacion.contains(TipoOperacionCriterioEmun.MAYOR_IGUAL.getRepresentacion()) || operacion.contains(TipoOperacionCriterioEmun.MENOR_IGUAL.getRepresentacion()) || operacion.contains(TipoOperacionCriterioEmun.DISTINTO.getRepresentacion()) )
				return false;
		}
		
		return true;
	}
	
	private Map<Integer,Integer> obtenerPosicionesParentesisExternos(String expresion){
		Map<Integer,Integer> result= new HashMap<Integer, Integer>();
		
		if(expresion.isEmpty())
			return result;
		
		List<Integer> pilaParentesis=new ArrayList<Integer>();
		Character c;
		for(Integer i=0;i<expresion.length();i++){
			c=expresion.charAt(i);
			if(c.equals('('))
				pilaParentesis.add(i);
			if(c.equals(')')){
				if(pilaParentesis.size()==1)
					result.put(pilaParentesis.get(0), i);
				
				pilaParentesis.remove(pilaParentesis.size()-1);
			}
		}
			
		return result;
	}
	private Boolean esParentesisRedundante(String criterio){
		Character c;
		Stack<Character>pila=new Stack<Character>();
		for(Integer i=0;i<criterio.length();i++){
			c=criterio.charAt(i);
			if(c.equals('(')){
				pila.push(c);
			}
			if(c.equals(')')){
				if(!pila.empty()){
					pila.pop();
				}else{
					return false;
				}
			}
		}
		if(pila.empty()){
			return true;
		}else{
			return false;
		}
	}
	private Boolean estaEntreParentesis(Map<Integer, Integer> mapaParentesis,Integer posicion){
		for(Integer i:mapaParentesis.keySet()){
			if(i<posicion && mapaParentesis.get(i)>posicion)
				return true;
		}
		return false;
	}
	
	
	private Map<TipoOperacionCriterioEmun, Integer> obtenerOperacionExterna(Map<TipoOperacionCriterioEmun, List<Integer>> mapa){
		Map<TipoOperacionCriterioEmun, Integer> result= new HashMap<TipoOperacionCriterioEmun, Integer>();
		
		if(!mapa.get(TipoOperacionCriterioEmun.NOT).isEmpty()){
			result.put(TipoOperacionCriterioEmun.NOT, mapa.get(TipoOperacionCriterioEmun.NOT).get(0));
			return result;
		}
		
		if(!mapa.get(TipoOperacionCriterioEmun.AND).isEmpty()){
			if(!mapa.get(TipoOperacionCriterioEmun.OR).isEmpty()){
				if(mapa.get(TipoOperacionCriterioEmun.OR).get(0)<mapa.get(TipoOperacionCriterioEmun.AND).get(0))
					result.put(TipoOperacionCriterioEmun.OR, mapa.get(TipoOperacionCriterioEmun.OR).get(0));
					return result;
			}
			result.put(TipoOperacionCriterioEmun.AND, mapa.get(TipoOperacionCriterioEmun.AND).get(0));
			return result;
		}
		
		if(!mapa.get(TipoOperacionCriterioEmun.OR).isEmpty()){
			result.put(TipoOperacionCriterioEmun.OR, mapa.get(TipoOperacionCriterioEmun.OR).get(0));
			return result;
		}
		
		//EVALUAR LA OPERACION EXTERNA MAS A LA IZQUIERDA
		
		TipoOperacionCriterioEmun operacionE=null;
		Integer posicionIZ=null;
		
		for(TipoOperacionCriterioEmun op: TipoOperacionCriterioEmun.getComparaciones()){
			if(!(mapa.get(op).isEmpty()) && (posicionIZ==null || posicionIZ>mapa.get(op).get(0))){
				posicionIZ=mapa.get(op).get(0);
				operacionE=op;
			}
		}
		
		result.put(operacionE, mapa.get(operacionE).get(0));
		return result;
	}
	
	private TipoOperandoCriterioEmun identificarTipoOperando(String operando){

		if(operando.startsWith("\"") && operando.endsWith("\"") && numeroAparicionesSecuencia(operando, "\"")==2)
			return TipoOperandoCriterioEmun.LITERAL;
		if(operando.startsWith("[") && operando.endsWith("]") && numeroAparicionesSecuencia(operando, "[")==1 && numeroAparicionesSecuencia(operando, "]")==1)
			return TipoOperandoCriterioEmun.PARAMETRO_ESPERADO;
		
		return TipoOperandoCriterioEmun.CRITERIO;
	}
	
	private Integer numeroAparicionesSecuencia(String cadena,String secuencia){
		Integer lastPosition=0;
		Integer resultado=0;
		while((lastPosition=cadena.indexOf(secuencia, lastPosition))!=-1){
			resultado++;
			lastPosition++;
		}
		return resultado;
	}
	
	private Double devolverOperandoLiteral(String operando){
		
		String cadenaAParsear = operando.replaceAll("\"", "");
		//cadenaAParsear = cadenaAParsear.replaceAll("\\.", "");
		cadenaAParsear = cadenaAParsear.replaceAll(this.separadorDecimales, ".");
		return Double.valueOf(cadenaAParsear);
		//return Double.valueOf(operando.substring(1,operando.length()-1));
	}
	
	private boolean esCadena(String operando){
		try {			
			String cadenaAParsear = operando.replaceAll("\"", "");
			cadenaAParsear = cadenaAParsear.replaceAll(this.separadorDecimales, ".");
			Double.valueOf(cadenaAParsear);
			return false;
		}catch(NumberFormatException e){
			return true;
		}
	}
}
