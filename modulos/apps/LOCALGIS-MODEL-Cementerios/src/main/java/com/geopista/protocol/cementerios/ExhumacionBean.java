/**
 * ExhumacionBean.java
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
 * Clase que implementa un objeto de tipo exhumacion
 */
public class ExhumacionBean extends ElemFeatureBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int tipo_exhumacion;
	private String informe_exhumacion;
	private Date fecha_exhumacion;
	private String codigo;
	private int tipo_contenedor;
	private String bonificacion;
	private String precio_final;
	private DifuntoBean difunto;
	private int id_exhumacion;
	private boolean red_restos = false;
	private boolean traslado = false;
	
	private TarifaBean tarifa;
	
	/**Métodos Getter y setter**/
	
	public TarifaBean getTarifa() {
		return tarifa;
	}
	public void setTarifa(TarifaBean tarifa) {
		this.tarifa = tarifa;
	}
	private ServicioBean servicio;

	public int getTipo_exhumacion() {
		return tipo_exhumacion;
	}
	public void setTipo_exhumacion(int tipo_exhumacion) {
		this.tipo_exhumacion = tipo_exhumacion;
	}
	public String getInforme_exhumacion() {
		return informe_exhumacion;
	}
	public void setInforme_exhumacion(String informe_exhumacion) {
		this.informe_exhumacion = informe_exhumacion;
	}
	public Date getFecha_exhumacion() {
		return fecha_exhumacion;
	}
	public void setFecha_exhumacion(Date fecha_exhumacion) {
		this.fecha_exhumacion = fecha_exhumacion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getTipo_contenedor() {
		return tipo_contenedor;
	}
	public void setTipo_contenedor(int tipo_contenedor) {
		this.tipo_contenedor = tipo_contenedor;
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
	public int getId_exhumacion() {
		return id_exhumacion;
	}
	public void setId_exhumacion(int id_exhumacion) {
		this.id_exhumacion = id_exhumacion;
	}
	public boolean isRed_restos() {
		return red_restos;
	}
	public void setRed_restos(boolean red_restos) {
		this.red_restos = red_restos;
	}
	public boolean isTraslado() {
		return traslado;
	}
	public void setTraslado(boolean traslado) {
		this.traslado = traslado;
	}
	public ServicioBean getServicio() {
		return servicio;
	}
	public void setServicio(ServicioBean servicio) {
		this.servicio = servicio;
	}
	
	
}
