/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.historico.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.historico.cu.GestorCUHistorico;
import es.dc.a21l.historico.cu.HistoricoDto;
import es.dc.a21l.historico.modelo.Historico;
import es.dc.a21l.historico.modelo.HistoricoRepositorio;

public class GestorCUHistoricoImpl implements GestorCUHistorico{
	private Mapper mapper;
    private HistoricoRepositorio historicoRepositorio;
    
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setHistoricoRepositorio(HistoricoRepositorio historicoRepositorio) {
        this.historicoRepositorio = historicoRepositorio;
    }
    
    public EncapsuladorListSW<HistoricoDto> cargarTodosPorIndicador(Long idIndicador) {
        List<Historico> listaVersiones = historicoRepositorio.cargarTodosPorIndicador(idIndicador);
        EncapsuladorListSW<HistoricoDto> result = new EncapsuladorListSW<HistoricoDto>();
        Historico2HistoricoDtoTransformer transformer = new Historico2HistoricoDtoTransformer(mapper);
        for (Historico version: listaVersiones){
        	result.add(transformer.transform(version));
        }
        return result;
    }
    
    public HistoricoDto cargarPorId(Long id){
    	return new Historico2HistoricoDtoTransformer(mapper).transform(historicoRepositorio.carga(id));
    }
    
	public HistoricoDto guardar(HistoricoDto historicoDto, EncapsuladorErroresSW errores) {
		if(historicoDto.getStrFecha()!=null){
			if(historicoRepositorio.existeRepetido(historicoDto.getIndicador().getId(), historicoDto.getStrFecha())){
				errores.addError(HistoricoFormErrorsEmun.FECHAREPETIDA);
				return null;
			}
		}
		Validador<HistoricoDto> historicoDtoValidador = new HistoricoDtoValidador();
		historicoDtoValidador.valida(historicoDto, errores);
		if (errores.getHashErrors())
			return null;

		Historico historico = new HistoricoDto2HistoricoTransformer(mapper,	historicoRepositorio).transform(historicoDto);
		Historico historicoResultado = historicoRepositorio.guarda(historico);
		historicoDto = new Historico2HistoricoDtoTransformer(mapper).transform(historicoResultado);
		System.gc();
		return historicoDto;
	}
	
	public Boolean borrar(Long id) {
		historicoRepositorio.borra(id);
		return true;
	}
	
	public void borrarPorIdIndicador(Long id, EncapsuladorErroresSW erros) {
        List<Historico> lista = new ArrayList<Historico>();
        lista = historicoRepositorio.cargarTodosPorIndicador(id);
        for ( Historico historico : lista ) {
        	historicoRepositorio.borra(historico.getId());
        }
    }
}