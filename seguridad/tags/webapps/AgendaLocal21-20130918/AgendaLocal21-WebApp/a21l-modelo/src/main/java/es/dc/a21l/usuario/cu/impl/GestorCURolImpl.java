/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.cu.GestorCURol;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.modelo.Rol;
import es.dc.a21l.usuario.modelo.RolElementoJerarquia;
import es.dc.a21l.usuario.modelo.RolElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.modelo.RolRepositorio;

public class GestorCURolImpl implements GestorCURol {
	
	private Mapper mapper;
	private RolRepositorio rolRepositorio;
	private RolElementoJerarquiaRepositorio rolEltoRepositorio;
	private ElementoJerarquiaRepositorio elementoJerarquiaRepositorio;
	
	@Autowired
	public void setElementoJerarquiaRepositorio(
			ElementoJerarquiaRepositorio elementoJerarquiaRepositorio) {
		this.elementoJerarquiaRepositorio = elementoJerarquiaRepositorio;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setRolRepositorio(RolRepositorio rolRepositorio) {
		this.rolRepositorio = rolRepositorio;
	}
	
	@Autowired
	public void setRolElementoJerarquiaRepositorio(RolElementoJerarquiaRepositorio rolEltoRepositorio) {
		this.rolEltoRepositorio = rolEltoRepositorio;
	}

	public RolDto cargarPorId(Long id) {
		Rol rol = rolRepositorio.carga(id);
		if(rol == null)
			return null;
		
		RolDto rolDto = new Rol2RolDtoTransformer(mapper).transform(rol);
		
		List<RolElementoJerarquia> eltosJerarquia = rolEltoRepositorio.cargarElementosJerarquiaPorRol(id);
		List<Long> elementosJerarquia = new ArrayList<Long>();
		for(int i=0; i<eltosJerarquia.size(); i++){
			elementosJerarquia.add(eltosJerarquia.get(i).getElementoJerarquia().getId());
		}
		
		rolDto.setEltosJerarquia(elementosJerarquia);
		return rolDto;
	}
	
	public EncapsuladorListSW<RolDto> cargaTodos(){
		List<Rol> lista=rolRepositorio.cargarTodosRoles();
		Rol2RolDtoTransformer transformer= new Rol2RolDtoTransformer(mapper);
		
		EncapsuladorListSW<RolDto> result= new EncapsuladorListSW<RolDto>();
		for(Rol temp:lista){
			result.add(transformer.transform(temp));
		}
		return result;
	}
	
	public RolDto guardar(RolDto rolDto, EncapsuladorErroresSW errores){
		
	if(rolDto.getNombre()!=null && cargaExisteRolConNombreYIdDistinto(rolDto.getNombre(), rolDto.getId())){
		errores.addError(RolDtoFormErrorsEmun.NOMBRE_EN_USO);
	}
	Validador<RolDto> validador= new RolDtoValidador();
	validador.valida(rolDto, errores);
		
		if(errores.getHashErrors())
			return null;
		
		 Rol rol = rolRepositorio.guarda(new RolDto2RolTransformer(mapper, rolRepositorio).transform(rolDto));
		 rolRepositorio.guarda(rol);
		 return new Rol2RolDtoTransformer(mapper).transform(rol); 
	}
	
	
	public void guardaActualizacionPermisos(Long idRol, List<Long> listaIdsEJPermisos){
		 //Se trabaja con una copia de la lista para que no ser alterada
		List<Long> listaIdsEJPermisosClone= new ArrayList<Long>(listaIdsEJPermisos);
		
		List<Long> listaIdsEJ= rolEltoRepositorio.cargaIdsElementosJerarquiaPorRol(idRol);
		List<Long> listaIdsEJClone= new ArrayList<Long>(listaIdsEJ);
		listaIdsEJClone.removeAll(listaIdsEJPermisosClone);
		
		rolEltoRepositorio.borraPorListaEJyRol(listaIdsEJClone, idRol);
		
		
		listaIdsEJPermisosClone.removeAll(listaIdsEJ);
		
		RolElementoJerarquia rolElementoJerarquia;
		Rol rol= rolRepositorio.carga(idRol);
		
		for(Long temp:listaIdsEJPermisosClone){
			rolElementoJerarquia= new RolElementoJerarquia();
			rolElementoJerarquia.setId(0L);
			rolElementoJerarquia.setRol(rol);
			rolElementoJerarquia.setElementoJerarquia(elementoJerarquiaRepositorio.carga(temp));
			rolEltoRepositorio.guarda(rolElementoJerarquia);
		}
		
		
	}

	public Boolean borrar(Long id) {
		rolRepositorio.borra(id);
		return true;
	}
	
	public EncapsuladorListSW<RolDto> cargaRolesPorUsuario(Long idUsuario){
		List<Rol> lista = rolRepositorio.cargaRolesPorUsuario(idUsuario);
		EncapsuladorListSW<RolDto> result= new EncapsuladorListSW<RolDto>();
		Rol2RolDtoTransformer transformer= new Rol2RolDtoTransformer(mapper);
		for(Rol temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<RolDto> cargaRolesSinAsignarUsuario(Long idUsuario){
		List<Rol> lista= rolRepositorio.cargaRolesSinAsignarUsuario(idUsuario);
		EncapsuladorListSW<RolDto> result= new EncapsuladorListSW<RolDto>();
		Rol2RolDtoTransformer transformer= new Rol2RolDtoTransformer(mapper);
		for(Rol temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public Boolean cargaExisteRolConNombreYIdDistinto(String nombre,Long idRol){
		return rolRepositorio.cargaRolPorNombreYIdDistinto(nombre, idRol)!=null;
	}
}
