/**
 * BienRevertible.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.Documentable;

/**
 * Esta clase contiene la información relativa a los Bienes revertibles.
 * @author angeles
 *
 */

public class BienRevertible implements Serializable,Versionable, Documentable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String numInventario;
	private String organizacion;
	private Date fechaAlta;
	private Date fechaUltimaModificacion;
	private String nombre;
	private Date fechaInicio;
	private Date fechaVencimiento;
	private Date fechaTransmision;
	private Date fechaVersion;
	private Double importe;
	private String poseedor;
	private String tituloPosesion;
	private String condicionesReversion;
	private String detalles;
	private String catTransmision;
	private Date fecha_aprobacion_pleno;
	private String descripcion_bien;
	private Date fecha_adquisicion;
	private String adquisicion;
	private String diagnosis;
	private boolean patrimonioMunicipalSuelo;
	private String clase;
	private String autor;
	private Collection<BienBean> bienes;
	private Collection<Observacion> observaciones;
	private CuentaAmortizacion cuentaAmortizacion = new CuentaAmortizacion();
	private boolean versionado; 
	private long revisionActual;
    private long revisionExpirada;
	private Date fechaBaja;
	private boolean borrado= false;
	private Collection<DocumentBean> documentos;
	private Seguro seguro;

    
    public Collection<Observacion> getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(Collection<Observacion> observaciones) {
		this.observaciones = observaciones;
	}
	public void addObservacion(Observacion observacion) {
    	if (this.observaciones==null) observaciones = new Vector<Observacion>(); 
        observaciones.add(observacion);
    }
	public void addObservaciones(Collection<Observacion> observaciones) {
		if (this.observaciones==null) this.observaciones = observaciones;
		else
			this.observaciones.addAll(observaciones);
	}
	public CuentaAmortizacion getCuentaAmortizacion() {
		return cuentaAmortizacion;
	}
	public void setCuentaAmortizacion(CuentaAmortizacion cuentaAmortizacion) {
		this.cuentaAmortizacion = cuentaAmortizacion;
	}

	
    public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNumInventario() {
		return numInventario;
	}
	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Date getFechaTransmision() {
		return fechaTransmision;
	}
	public void setFechaTransmision(Date fechaTransmision) {
		this.fechaTransmision = fechaTransmision;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = new Double(importe);
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getPoseedor() {
		return poseedor;
	}
	public void setPoseedor(String poseedor) {
		this.poseedor = poseedor;
	}
	public String getTituloPosesion() {
		return tituloPosesion;
	}
	public void setTituloPosesion(String tituloPosesion) {
		this.tituloPosesion = tituloPosesion;
	}
	public String getCondicionesReversion() {
		return condicionesReversion;
	}
	public void setCondicionesReversion(String condicionesReversion) {
		this.condicionesReversion = condicionesReversion;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public String getCatTransmision() {
		return catTransmision;
	}
	public void setCatTransmision(String catTransmision) {
		this.catTransmision = catTransmision;
	}
	public Collection<BienBean> getBienes() {
		return bienes;
	}
	public void setBienes(Collection<BienBean> bienes) {
		this.bienes = bienes;
	}
	public boolean isBorrado() {
		return borrado;
	}
	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}
	 public void setBorrado(String borrado) {
	        try{this.borrado= borrado.equalsIgnoreCase("1")?true:false;}
	        catch(Exception e){this.borrado= false;}
	    }
	
	public void addBien(BienBean bien){
		if (bienes==null) bienes=new Vector<BienBean>();
		bienes.add(bien);
		
	}
	public long getRevisionActual() {
		return revisionActual;
	}
	public void setRevisionActual(long revisionActual) {
		this.revisionActual = revisionActual;
	}
	public long getRevisionExpirada() {
		return revisionExpirada;
	}
	public void setRevisionExpirada(long revisionExpirada) {
		this.revisionExpirada = revisionExpirada;
	}
	public boolean isVersionado() {
		return versionado;
	}
	public void setVersionado(boolean versionado) {
		this.versionado = versionado;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaVersion() {
		return fechaVersion;
	}
	public void setFechaVersion(Date fechaVersion) {
		this.fechaVersion = fechaVersion;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public String getOrganizacion() {
		return organizacion;
	}
	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}
	public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha_aprobacion_pleno() {
		return fecha_aprobacion_pleno;
	}
	public void setFecha_aprobacion_pleno(Date fechaAprobacionPleno) {
		fecha_aprobacion_pleno = fechaAprobacionPleno;
	}
	public String getDescripcion_bien() {
		return descripcion_bien;
	}
	public void setDescripcion_bien(String descripcionBien) {
		descripcion_bien = descripcionBien;
	}
	public Date getFecha_adquisicion() {
		return fecha_adquisicion;
	}
	public void setFecha_adquisicion(Date fechaAdquisicion) {
		fecha_adquisicion = fechaAdquisicion;
	}
	public String getAdquisicion() {
		return adquisicion;
	}
	public void setAdquisicion(String adquisicion) {
		this.adquisicion = adquisicion;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public boolean isPatrimonioMunicipalSuelo() {
		return patrimonioMunicipalSuelo;
	}
	public void setPatrimonioMunicipalSuelo(boolean patrimonioMunicipalSuelo) {
		this.patrimonioMunicipalSuelo = patrimonioMunicipalSuelo;
	}

	public void setPatrimonioMunicipalSuelo(String patrimonioMunicipalSuelo) {
        try{this.patrimonioMunicipalSuelo= patrimonioMunicipalSuelo.equalsIgnoreCase("1")?true:false;}catch(Exception e){this.patrimonioMunicipalSuelo= false;}
   }
	public Collection<DocumentBean> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Collection<DocumentBean> documentos) {
		this.documentos = documentos;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public Seguro getSeguro() {
		return seguro;
	}
	public void setSeguro(Seguro seguro) {
		this.seguro = seguro;
	}
	
	
}
