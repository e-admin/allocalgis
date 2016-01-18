/**
 * FinderPlanesObraConditions.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork;

import java.util.Calendar;

public class FinderPlanesObraConditions {

	private String description;
	private Calendar from;
	private Calendar to;
	private Integer startElement;
	private Integer range;
	private Integer idMunicipio;
	private Integer idEntidad;
	private String orderByColumns; // nombreColumn:ASC;nombreColumn:DESC
	private String features; //idLayer:idFeature;idLayer:idFeature;
	
	// PARA ENVIO DE PLANES:
	private String plan;
	private String anios;
	private String nombre;
	private String paraje;
	private Double presupuestoEstimado;
	private Double presupuestoDefinitivo;
	private String existeProyecto;
	private String infraestructura;
	private String obraNueva;
	private String estudioAmbiental;
	private String datosEIEL;
	private String permisos;
	private String personaContacto;
	private String telefonoContacto;
	private Calendar fromFechaAprobacion;
	private Calendar toFechaAprobacion;
	private String[] documentosAdjuntos;
	private String[] serviciosAfectados;

	// PARA RESPUESTA DE PLANES:
	private String destinatario;
	private String recibi;
	private Calendar fromFechaRecibi;
	private Calendar toFechaRecibi;
	private String supervision;
	private String directorProyecto;
	private String autorProyecto;
	private String directorObra;
	private String empresaAdjudicataria;
	private Calendar fromFechaResolucion;
	private Calendar toFechaResolucion;
	private Double presupuestoAdjudicacion;
	private String coordinadorSegSalud;
	private Calendar fromActaReplanteo;
	private Calendar toActaReplanteo;
	private Calendar fromFechaComienzo;
	private Calendar toFechaComienzo;
	private Calendar fromFechaFinalizacion;
	private Calendar toFechaFinalizacion;
	private Calendar fromProrrogas;
	private Calendar toProrrogas;
	private Calendar fromActaRecepcion;
	private Calendar toActaRecepcion;
	private Double certificacionFinal;
	private Calendar fromResolucionCertificacion;
	private Calendar toResolucionCertificacion;
	private Calendar fromInformacionCambiosEIEL;
	private Calendar toInformacionCambiosEIEL;
	private Calendar fromReformados;
	private Calendar toReformados;
	private Double liquidacion;
	private Calendar fromFechaLiquidacion;
	private Calendar toFechaLiquidacion;
	private String detalles;
	private Calendar fromFechaAprobacionPrincipado;
	private Calendar toFechaAprobacionPrincipado;
	private Calendar fromFechaSolicitud;
	private Calendar toFechaSolicitud;
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getFrom() {
		return from;
	}
	public void setFrom(Calendar from) {
		this.from = from;
	}
	public Calendar getTo() {
		return to;
	}
	public void setTo(Calendar to) {
		this.to = to;
	}
	public Integer getStartElement() {
		return startElement;
	}
	public void setStartElement(Integer startElement) {
		this.startElement = startElement;
	}
	public Integer getRange() {
		return range;
	}
	public void setRange(Integer range) {
		this.range = range;
	}
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Integer getIdEntidad() {
		return idEntidad;
	}
	public void setIdEntidad(Integer idEntidad) {
		this.idEntidad = idEntidad;
	}
	public String getOrderByColumns() {
		return orderByColumns;
	}
	public void setOrderByColumns(String orderByColumns) {
		this.orderByColumns = orderByColumns;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getAnios() {
		return anios;
	}
	public void setAnios(String anios) {
		this.anios = anios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getParaje() {
		return paraje;
	}
	public void setParaje(String paraje) {
		this.paraje = paraje;
	}
	public Double getPresupuestoEstimado() {
		return presupuestoEstimado;
	}
	public void setPresupuestoEstimado(Double presupuestoEstimado) {
		this.presupuestoEstimado = presupuestoEstimado;
	}
	public Double getPresupuestoDefinitivo() {
		return presupuestoDefinitivo;
	}
	public void setPresupuestoDefinitivo(Double presupuestoDefinitivo) {
		this.presupuestoDefinitivo = presupuestoDefinitivo;
	}
	public String getExisteProyecto() {
		return existeProyecto;
	}
	public void setExisteProyecto(String existeProyecto) {
		this.existeProyecto = existeProyecto;
	}
	public String getInfraestructura() {
		return infraestructura;
	}
	public void setInfraestructura(String infraestructura) {
		this.infraestructura = infraestructura;
	}
	public String getEstudioAmbiental() {
		return estudioAmbiental;
	}
	public void setEstudioAmbiental(String estudioAmbiental) {
		this.estudioAmbiental = estudioAmbiental;
	}
	public String getDatosEIEL() {
		return datosEIEL;
	}
	public void setDatosEIEL(String datosEIEL) {
		this.datosEIEL = datosEIEL;
	}
	public String getPermisos() {
		return permisos;
	}
	public void setPermisos(String permisos) {
		this.permisos = permisos;
	}
	public String getPersonaContacto() {
		return personaContacto;
	}
	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}
	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}
	public Calendar getFromFechaAprobacion() {
		return fromFechaAprobacion;
	}
	public void setFromFechaAprobacion(Calendar fromFechaAprobacion) {
		this.fromFechaAprobacion = fromFechaAprobacion;
	}
	public Calendar getToFechaAprobacion() {
		return toFechaAprobacion;
	}
	public void setToFechaAprobacion(Calendar toFechaAprobacion) {
		this.toFechaAprobacion = toFechaAprobacion;
	}
	public String[] getDocumentosAdjuntos() {
		return documentosAdjuntos;
	}
	public void setDocumentosAdjuntos(String[] documentosAdjuntos) {
		this.documentosAdjuntos = documentosAdjuntos;
	}
	public String[] getServiciosAfectados() {
		return serviciosAfectados;
	}
	public void setServiciosAfectados(String[] serviciosAfectados) {
		this.serviciosAfectados = serviciosAfectados;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getRecibi() {
		return recibi;
	}
	public void setRecibi(String recibi) {
		this.recibi = recibi;
	}
	public Calendar getFromFechaRecibi() {
		return fromFechaRecibi;
	}
	public void setFromFechaRecibi(Calendar fromFechaRecibi) {
		this.fromFechaRecibi = fromFechaRecibi;
	}
	public Calendar getToFechaRecibi() {
		return toFechaRecibi;
	}
	public void setToFechaRecibi(Calendar toFechaRecibi) {
		this.toFechaRecibi = toFechaRecibi;
	}
	public String getSupervision() {
		return supervision;
	}
	public void setSupervision(String supervision) {
		this.supervision = supervision;
	}
	public String getDirectorProyecto() {
		return directorProyecto;
	}
	public void setDirectorProyecto(String directorProyecto) {
		this.directorProyecto = directorProyecto;
	}
	public String getAutorProyecto() {
		return autorProyecto;
	}
	public void setAutorProyecto(String autorProyecto) {
		this.autorProyecto = autorProyecto;
	}
	public String getDirectorObra() {
		return directorObra;
	}
	public void setDirectorObra(String directorObra) {
		this.directorObra = directorObra;
	}
	public String getEmpresaAdjudicataria() {
		return empresaAdjudicataria;
	}
	public void setEmpresaAdjudicataria(String empresaAdjudicataria) {
		this.empresaAdjudicataria = empresaAdjudicataria;
	}
	public Calendar getFromFechaResolucion() {
		return fromFechaResolucion;
	}
	public void setFromFechaResolucion(Calendar fromFechaResolucion) {
		this.fromFechaResolucion = fromFechaResolucion;
	}
	public Calendar getToFechaResolucion() {
		return toFechaResolucion;
	}
	public void setToFechaResolucion(Calendar toFechaResolucion) {
		this.toFechaResolucion = toFechaResolucion;
	}
	public Double getPresupuestoAdjudicacion() {
		return presupuestoAdjudicacion;
	}
	public void setPresupuestoAdjudicacion(Double presupuestoAdjudicacion) {
		this.presupuestoAdjudicacion = presupuestoAdjudicacion;
	}
	public String getCoordinadorSegSalud() {
		return coordinadorSegSalud;
	}
	public void setCoordinadorSegSalud(String coordinadorSegSalud) {
		this.coordinadorSegSalud = coordinadorSegSalud;
	}
	public Calendar getFromActaReplanteo() {
		return fromActaReplanteo;
	}
	public void setFromActaReplanteo(Calendar fromActaReplanteo) {
		this.fromActaReplanteo = fromActaReplanteo;
	}
	public Calendar getToActaReplanteo() {
		return toActaReplanteo;
	}
	public void setToActaReplanteo(Calendar toActaReplanteo) {
		this.toActaReplanteo = toActaReplanteo;
	}
	public Calendar getFromFechaComienzo() {
		return fromFechaComienzo;
	}
	public void setFromFechaComienzo(Calendar fromFechaComienzo) {
		this.fromFechaComienzo = fromFechaComienzo;
	}
	public Calendar getToFechaComienzo() {
		return toFechaComienzo;
	}
	public void setToFechaComienzo(Calendar toFechaComienzo) {
		this.toFechaComienzo = toFechaComienzo;
	}
	public Calendar getFromFechaFinalizacion() {
		return fromFechaFinalizacion;
	}
	public void setFromFechaFinalizacion(Calendar fromFechaFinalizacion) {
		this.fromFechaFinalizacion = fromFechaFinalizacion;
	}
	public Calendar getToFechaFinalizacion() {
		return toFechaFinalizacion;
	}
	public void setToFechaFinalizacion(Calendar toFechaFinalizacion) {
		this.toFechaFinalizacion = toFechaFinalizacion;
	}
	public Calendar getFromProrrogas() {
		return fromProrrogas;
	}
	public void setFromProrrogas(Calendar fromProrrogas) {
		this.fromProrrogas = fromProrrogas;
	}
	public Calendar getToProrrogas() {
		return toProrrogas;
	}
	public void setToProrrogas(Calendar toProrrogas) {
		this.toProrrogas = toProrrogas;
	}
	public Calendar getFromActaRecepcion() {
		return fromActaRecepcion;
	}
	public void setFromActaRecepcion(Calendar fromActaRecepcion) {
		this.fromActaRecepcion = fromActaRecepcion;
	}
	public Calendar getToActaRecepcion() {
		return toActaRecepcion;
	}
	public void setToActaRecepcion(Calendar toActaRecepcion) {
		this.toActaRecepcion = toActaRecepcion;
	}
	public Double getCertificacionFinal() {
		return certificacionFinal;
	}
	public void setCertificacionFinal(Double certificacionFinal) {
		this.certificacionFinal = certificacionFinal;
	}
	public Calendar getFromResolucionCertificacion() {
		return fromResolucionCertificacion;
	}
	public void setFromResolucionCertificacion(Calendar fromResolucionCertificacion) {
		this.fromResolucionCertificacion = fromResolucionCertificacion;
	}
	public Calendar getToResolucionCertificacion() {
		return toResolucionCertificacion;
	}
	public void setToResolucionCertificacion(Calendar toResolucionCertificacion) {
		this.toResolucionCertificacion = toResolucionCertificacion;
	}
	public Calendar getFromInformacionCambiosEIEL() {
		return fromInformacionCambiosEIEL;
	}
	public void setFromInformacionCambiosEIEL(Calendar fromInformacionCambiosEIEL) {
		this.fromInformacionCambiosEIEL = fromInformacionCambiosEIEL;
	}
	public Calendar getToInformacionCambiosEIEL() {
		return toInformacionCambiosEIEL;
	}
	public void setToInformacionCambiosEIEL(Calendar toInformacionCambiosEIEL) {
		this.toInformacionCambiosEIEL = toInformacionCambiosEIEL;
	}
	public Calendar getFromReformados() {
		return fromReformados;
	}
	public void setFromReformados(Calendar fromReformados) {
		this.fromReformados = fromReformados;
	}
	public Calendar getToReformados() {
		return toReformados;
	}
	public void setToReformados(Calendar toReformados) {
		this.toReformados = toReformados;
	}
	public Double getLiquidacion() {
		return liquidacion;
	}
	public void setLiquidacion(Double liquidacion) {
		this.liquidacion = liquidacion;
	}
	public Calendar getFromFechaLiquidacion() {
		return fromFechaLiquidacion;
	}
	public void setFromFechaLiquidacion(Calendar fromFechaLiquidacion) {
		this.fromFechaLiquidacion = fromFechaLiquidacion;
	}
	public Calendar getToFechaLiquidacion() {
		return toFechaLiquidacion;
	}
	public void setToFechaLiquidacion(Calendar toFechaLiquidacion) {
		this.toFechaLiquidacion = toFechaLiquidacion;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public String getObraNueva() {
		return obraNueva;
	}
	public void setObraNueva(String obraNueva) {
		this.obraNueva = obraNueva;
	}
	public Calendar getFromFechaAprobacionPrincipado() {
		return fromFechaAprobacionPrincipado;
	}
	public void setFromFechaAprobacionPrincipado(
			Calendar fromFechaAprobacionPrincipado) {
		this.fromFechaAprobacionPrincipado = fromFechaAprobacionPrincipado;
	}
	public Calendar getToFechaAprobacionPrincipado() {
		return toFechaAprobacionPrincipado;
	}
	public void setToFechaAprobacionPrincipado(Calendar toFechaAprobacionPrincipado) {
		this.toFechaAprobacionPrincipado = toFechaAprobacionPrincipado;
	}
	public Calendar getFromFechaSolicitud() {
		return fromFechaSolicitud;
	}
	public void setFromFechaSolicitud(Calendar fromFechaSolicitud) {
		this.fromFechaSolicitud = fromFechaSolicitud;
	}
	public Calendar getToFechaSolicitud() {
		return toFechaSolicitud;
	}
	public void setToFechaSolicitud(Calendar toFechaSolicitud) {
		this.toFechaSolicitud = toFechaSolicitud;
	}

}
