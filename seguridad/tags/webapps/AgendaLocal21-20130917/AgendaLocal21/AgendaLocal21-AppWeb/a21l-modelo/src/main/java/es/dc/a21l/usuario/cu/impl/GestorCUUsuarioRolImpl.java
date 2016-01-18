/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.usuario.cu.GestorCUUsuarioRol;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.cu.UsuarioRolDto;
import es.dc.a21l.usuario.modelo.RolRepositorio;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRol;
import es.dc.a21l.usuario.modelo.UsuarioRolRepositorio;

public class GestorCUUsuarioRolImpl implements GestorCUUsuarioRol {

	
	private UsuarioRolRepositorio usuarioRolRepositorio;
	private Mapper mapper;
	private UsuarioRepositorio usuarioRepositorio;
	private RolRepositorio rolRepositorio;
	
	
	@Autowired
	public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Autowired
	public void setRolRepositorio(RolRepositorio rolRepositorio) {
		this.rolRepositorio = rolRepositorio;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setUsuarioRolRepositorio(UsuarioRolRepositorio usuarioRolRepositorio) {
		this.usuarioRolRepositorio = usuarioRolRepositorio;
	}

	
	public EncapsuladorListSW<UsuarioRolDto> cargaPorUsuario(Long idUsuario){
		List<UsuarioRol> lista=usuarioRolRepositorio.cargaPorUsuario(idUsuario);
		EncapsuladorListSW<UsuarioRolDto> result= new EncapsuladorListSW<UsuarioRolDto>();
		UsuarioRol2UsuarioRolDtoTransformer transformer= new UsuarioRol2UsuarioRolDtoTransformer(mapper);
		for(UsuarioRol temp:lista){
			result.add(transformer.transform(temp));
		}
		return result;
	}
	
	public void actualizaListaRolesUsuario(Long idUsuario, List<Long> listaRoles){
		List<Long> idsRolesUser= usuarioRolRepositorio.cargaIdsRolesUsuario(idUsuario);
		List<Long> idsRolesUserClone= new ArrayList<Long>(idsRolesUser);
		
		idsRolesUserClone.removeAll(listaRoles);
		usuarioRolRepositorio.borraPorUsuarioYListaRoles(idUsuario, idsRolesUserClone);
		
		List<Long> listaRolesClone = new ArrayList<Long>(listaRoles);
		listaRolesClone.removeAll(idsRolesUser);
		
		UsuarioRol usuarioRol;
		Usuario usuario= usuarioRepositorio.carga(idUsuario);
		
		for(Long idRol:listaRolesClone){
			usuarioRol= new UsuarioRol();
			usuarioRol.setId(0L);
			usuarioRol.setUsuario(usuario);
			usuarioRol.setRol(rolRepositorio.carga(idRol));
			usuarioRolRepositorio.guarda(usuarioRol);	
		}
	}
	
	public UsuarioRolDto guarda(UsuarioRolDto usuarioRolDto,EncapsuladorErroresSW errores){
		Validador<UsuarioRolDto> validador= new UsuarioRolDtoValidador();
		validador.valida(usuarioRolDto, errores);
		if(errores.getHashErrors())
			return null;
		
		UsuarioRol usuarioRol= new UsuarioRolDto2UsuarioRolTransformer(mapper, usuarioRolRepositorio, usuarioRepositorio, rolRepositorio).transform(usuarioRolDto);
		
		usuarioRolRepositorio.guarda(usuarioRol);
		return new UsuarioRol2UsuarioRolDtoTransformer(mapper).transform(usuarioRol);
	}
}
