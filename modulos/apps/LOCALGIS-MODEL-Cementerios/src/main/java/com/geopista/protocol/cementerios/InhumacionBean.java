/**
 * InhumacionBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;


/**
 * Clase que implementa un objeto de tipo inhumacion
 */
public class InhumacionBean extends ElemFeatureBean implements Serializable{
	
	private int tipo_inhumacion;
	private String informe_inhumacion;
	private Date fecha_inhumacion;
	private String codigo;
	private int tipo_contenedor;
	private String bonificacion;
	private String precio_final;
	private DifuntoBean difunto;
	private int id_inhumacion;
	private TarifaBean tarifa;
	
	/**Métodos Getter y setter**/
	
	public TarifaBean getTarifa() {
		return tarifa;
	}
	public void setTarifa(TarifaBean tarifa) {
		this.tarifa = tarifa;
	}
	private ServicioBean servicio;
	
	public int getTipo_inhumacion() {
		return tipo_inhumacion;
	}
	public void setTipo_inhumacion(int tipo_inhumacion) {
		this.tipo_inhumacion = tipo_inhumacion;
	}
	public String getInforme_inhumacion() {
		return informe_inhumacion;
	}
	public void setInforme_inhumacion(String informe_inhumacion) {
		this.informe_inhumacion = informe_inhumacion;
	}
	public Date getFecha_inhumacion() {
		return fecha_inhumacion;
	}
	public void setFecha_inhumacion(Date fecha_inhumacion) {
		this.fecha_inhumacion = fecha_inhumacion;
	}
	public ServicioBean getServicio() {
		return servicio;
	}
	public void setServicio(ServicioBean servicio) {
		this.servicio = servicio;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getBonificacion() {
		return bonificacion;
	}
	public void setBonificacion(String bonificacion) {
		this.bonificacion = bonificacion;
	}
	public String getPrecio_final() {
		return precio_final;
	}
	public void setPrecio_final(String precio_final) {
		this.precio_final = precio_final;
	}
	public DifuntoBean getDifunto() {
		return difunto;
	}
	public void setDifunto(DifuntoBean difunto) {
		this.difunto = difunto;
	}
	public int getId_inhumacion() {
		return id_inhumacion;
	}
	public void setId_inhumacion(int id_inhumacion) {
		this.id_inhumacion = id_inhumacion;
	}
	public int getTipo_contenedor() {
		return tipo_contenedor;
	}
	public void setTipo_contenedor(int tipo_contenedor) {
		this.tipo_contenedor = tipo_contenedor;
	}
	
}
