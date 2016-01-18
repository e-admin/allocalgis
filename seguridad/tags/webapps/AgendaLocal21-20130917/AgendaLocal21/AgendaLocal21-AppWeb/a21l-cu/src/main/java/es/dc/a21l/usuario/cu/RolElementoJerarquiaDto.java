/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu;

import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;

@SuppressWarnings("restriction")
@XmlRootElement(name="rolElementoJerarquiaDto")
public class RolElementoJerarquiaDto extends DtoBase {
	
	private RolDto rolDto;
	private Long idElementoJerarquia;
	
	public RolElementoJerarquiaDto(){
		this.rolDto = new RolDto();
	}
	
	
	public RolDto getRolDto() {
		return rolDto;
	}

	public void setRolDto(RolDto rolDto) {
		this.rolDto = rolDto;
	}

	public Long getIdElementoJerarquia() {
		return idElementoJerarquia;
	}

	public void setIdElementoJerarquia(Long idElementoJerarquia) {
		this.idElementoJerarquia = idElementoJerarquia;
	}
	
}
