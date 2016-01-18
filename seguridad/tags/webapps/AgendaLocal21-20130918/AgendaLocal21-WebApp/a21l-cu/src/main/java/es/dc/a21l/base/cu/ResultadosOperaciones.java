/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name="resultadosOperaciones")
public enum ResultadosOperaciones {
	
	EXITO_GUARDAR("exitoGuardar"),
	EXITO_ASIGNAR_ROLES("exitoAsignarRoles"),
	EXITO_ASIGNAR_PERMISOS("exitoAsignarPermisos"),
	EXITO_CREAR("exitoCrear"),
	EXITO_BORRAR("exitoBorrar"),
	ERROR_BORRAR("errorBorrar"),
	ERROR_CREAR("errorCrear"),
	ERROR_GUARADAR("errorGuardar"),
	ENTIDAD_UTILIZADA("entidadUtilizada"),
	ERROR_BORRAR_PERMISOS("borrarSinPermisos"),
	ERROR_EDITAR_PERMISOS("editarSinPermisos");
	
	private String resultado;
	
	ResultadosOperaciones(String resultado){
		this.resultado=resultado;
	}
	
	@Override
    public String toString() {
		return resultado;
	}
	
    @XmlAttribute(name="resultado")
	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	


}