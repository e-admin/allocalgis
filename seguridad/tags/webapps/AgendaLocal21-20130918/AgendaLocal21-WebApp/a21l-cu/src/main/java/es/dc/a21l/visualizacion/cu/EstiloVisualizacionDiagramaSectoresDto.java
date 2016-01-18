/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Balidea Consulting & Programming
 */

@SuppressWarnings("restriction")
@XmlRootElement(name="estiloVisualizacionDiagramaSectoresDto")
public class EstiloVisualizacionDiagramaSectoresDto extends EstiloVisualizacionDto{

    private Integer diametro;
    private String colores;
    @XmlAttribute(name="diametro")
    public Integer getDiametro() {
		return diametro;
	}
	public void setDiametro(Integer diametro) {
		this.diametro = diametro;
	}
	@XmlAttribute(name="colores")
	public String getColores() {
		return colores;
	}
	public void setColores(String colores) {
		this.colores = colores;
	}
}