/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name="categoriaDto")
public class CategoriaDto extends ElementoJerarquiaDto {

	private Long idCategoriaPadre;
	
	//Atributos JSP
	private Boolean permisoUserNoAdmin;

	@XmlAttribute(name="idCategoriaPadre")
	public Long getIdCategoriaPadre() {
		return idCategoriaPadre;
	}

	public void setIdCategoriaPadre(Long idCategoriaPadre) {
		this.idCategoriaPadre = idCategoriaPadre;
	}

	@XmlAttribute(name="permisoUserNoAdmin")
	public Boolean getPermisoUserNoAdmin() {
		return permisoUserNoAdmin;
	}

	public void setPermisoUserNoAdmin(Boolean permisoUserNoAdmin) {
		this.permisoUserNoAdmin = permisoUserNoAdmin;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CategoriaDto)
			return this.getId()==((CategoriaDto)obj).getId();
		return super.equals(obj);
	}
}
