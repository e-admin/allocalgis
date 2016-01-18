/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.fuente.modelo.Fuente;
import es.dc.a21l.fuente.modelo.FuenteRepositorio;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class GestorCUUsuarioImpl implements GestorCUUsuario{
	
    private Mapper mapper;
    private UsuarioRepositorio usuarioRepositorio;
    private FuenteRepositorio fuenteRepositorio;
  
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
    @Autowired
    public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }
    @Autowired
    public void setFuenteRepositorio(FuenteRepositorio fuenteRepositorio) {
		this.fuenteRepositorio = fuenteRepositorio;
	}
    
	public EncapsuladorListSW<UsuarioDto> cargaTodos() {
        EncapsuladorListSW<UsuarioDto> result = new EncapsuladorListSW<UsuarioDto>();
        Usuario2UsuarioDtoTransformer transformer = new Usuario2UsuarioDtoTransformer(mapper);
        List<Usuario> usuarios = usuarioRepositorio.cargarTodosUsuarios();
        for (Usuario usuario : usuarios) {
        	UsuarioDto usuarioDto=transformer.transform(usuario);
        	usuarioDto.setTieneFuentes(fuenteRepositorio.isUsuarioConFuentes(usuarioDto.getId()));
			result.add(usuarioDto);
		}
        return result;
    	
    }


    public Boolean borra(Long id,Long idUsuarioBorra) {
    	ArrayList<Fuente> listaFuentes=(ArrayList<Fuente>)fuenteRepositorio.cargaFuentesAsociadasUsuario(id);
    	for(Fuente fuente:listaFuentes){
    		fuente.setRegistradaPor(usuarioRepositorio.carga(idUsuarioBorra));
    		fuenteRepositorio.guarda(fuente);
    	}
    	if(carga(id).getEsAdmin() && usuarioRepositorio.cargaExisteUnicoAdmin())
    		return false;
        usuarioRepositorio.borra(id);
        return true;
    }

    public UsuarioDto carga(Long id) {
    	Usuario usuario=usuarioRepositorio.carga(id);
    	UsuarioDto usuarioDto=new Usuario2UsuarioDtoTransformer(mapper).transform(usuario);
        return usuarioDto;
    }

    public UsuarioDto guarda(UsuarioDto usuarioDto, EncapsuladorErroresSW erros) {
       
        //fecha de creacion e integridad de login
        if(usuarioDto.getId()==0){
        	if(usuarioDto.getLogin()!=null &&cargaExisteUsuarioConLogin(usuarioDto.getLogin())){
        		erros.addError(UsuarioDtoFormErrorsEmun.LOGIN_EN_USO);
        	}else{
        		usuarioDto.setFechaRegistro(new Date());
        		usuarioDto.setActivo(true);
        	}
        	
        }else{
        	if(carga(usuarioDto.getId()).getEsAdmin() && usuarioRepositorio.cargaExisteUnicoAdmin())
        		erros.addError(UsuarioDtoFormErrorsEmun.UNICO_ADMIN);
        }
    	
    	Validador<UsuarioDto> usuarioDtoValidador = new UsuarioDtoValidador();
        usuarioDtoValidador.valida(usuarioDto, erros);
        if (erros.getHashErrors()) return null;
        
        Usuario usuario = new UsuarioDto2UsuarioTransformer(mapper, usuarioRepositorio).transform(usuarioDto);
        usuarioRepositorio.guarda(usuario);

        return new Usuario2UsuarioDtoTransformer(mapper).transform(usuario);
    }
    
    public UsuarioDto guardaUsuarioPass(UsuarioDto usuarioDto, EncapsuladorErroresSW erros) {
        Validador<UsuarioDto> usuarioDtoValidador = new UsuarioDtoValidador();
        usuarioDtoValidador.valida(usuarioDto, erros);
        if (erros.getHashErrors()) return null;
        
        Usuario usuario = new UsuarioDto2UsuarioTransformer(mapper, usuarioRepositorio).transform(usuarioDto);
        usuarioRepositorio.guarda(usuario);

        return new Usuario2UsuarioDtoTransformer(mapper).transform(usuario);
    }

    public boolean existe(Long idUsuario) {        
        return usuarioRepositorio.existe(idUsuario);
    }
	
	public UsuarioDto cargaUsuarioPorLogin(String login) {
		Usuario usuario= usuarioRepositorio.cargaUsuarioPorLogin(login);
		if(usuario==null)
			return null;
		return new Usuario2UsuarioDtoTransformer(mapper).transform(usuario);
	}
	
	public Boolean cargaExisteUsuarioConLogin(String login){
		 return usuarioRepositorio.cargaUsuarioPorLogin(login)!=null;
	}
	
	public Boolean esAdmin(Long idUsuario){
		return usuarioRepositorio.cargarUsuarioAdmin(idUsuario) != null;
	}
 
}
