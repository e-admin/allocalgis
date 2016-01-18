/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;

//Dto se utiliza para enviar formulario de busqueda avanzada debio  a la complejidad del paso de objetos sw 

@SuppressWarnings("restriction")
@XmlRootElement(name="indicadorBusquedaAvanzadaDto")
public class IndicadorBusquedaAvanzadaDto extends DtoBase{
	
	private String nombre;
	private String descripcion;
	private String categoriaContenedora;
	private String usuarioCreador;
	

	private List<Long> idsFiltroMD;
	private List<String> valoresFiltroMD;
	
	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@XmlAttribute(name="descripcion")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@XmlAttribute(name="categoriaContenedora")
	public String getCategoriaContenedora() {
		return categoriaContenedora;
	}
	public void setCategoriaContenedora(String categoriaContenedora) {
		this.categoriaContenedora = categoriaContenedora;
	}
	
	@XmlAttribute(name="usuarioCreador")
	public String getUsuarioCreador() {
		return usuarioCreador;
	}
	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
	
	@XmlElement(name="idsFiltroMD")
	public List<Long> getIdsFiltroMD() {
		return idsFiltroMD;
	}
	public void setIdsFiltroMD(List<Long> idsFiltroMD) {
		this.idsFiltroMD = idsFiltroMD;
	}
	
	@XmlElement(name="valoresFiltroMD")
	public List<String> getValoresFiltroMD() {
		return valoresFiltroMD;
	}
	

	public void setValoresFiltroMD(List<String> valoresFiltroMD) {
		this.valoresFiltroMD = valoresFiltroMD;
	}

}
