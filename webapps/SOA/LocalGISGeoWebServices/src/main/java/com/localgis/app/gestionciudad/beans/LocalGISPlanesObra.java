/**
 * LocalGISPlanesObra.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.beans;

import java.util.Calendar;

import javax.naming.ConfigurationException;

public class LocalGISPlanesObra extends LocalGISNote {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// PARA ENVIO DE PLANES:
	private String municipio;
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
	private Calendar fechaAprobacion;
	private String[] documentosAdjuntos;
	private String[] serviciosAfectados;

	// PARA RESPUESTA DE PLANES:
	private String destinatario;
	private String recibi;
	private Calendar fechaRecibi;
	private String supervision;
	private String directorProyecto;
	private String autorProyecto;
	private String directorObra;
	private String empresaAdjudicataria;
	private Calendar fechaResolucion;
	private Double presupuestoAdjudicacion;
	private String coordinadorSegSalud;
	private Calendar actaReplanteo;
	private Calendar fechaComienzo;
	private Calendar fechaFinalizacion;
	private Calendar prorrogas;
	private Calendar actaRecepcion;
	private Double certificacionFinal;
	private Calendar resolucionCertificacion;
	private Calendar informacionCambiosEIEL;
	private Calendar reformados;
	private Double liquidacion;
	private Calendar fechaLiquidacion;
	private String detalles;
	private Calendar fechaSolicitud;
	private Calendar fechaAprobacionPrincipado;
	
	public LocalGISPlanesObra(){
		super();
	}
	
	public LocalGISPlanesObra(Integer planesObraID, String planesObraDescription, Calendar startPlanesObra,Document[] doculist,
			String plan, String anios, String nombre,
			String paraje, Double presupuestoEstimado,
			Double presupuestoDefinitivo, String existeProyecto,
			String infraestructura, String obraNueva, String estudioAmbiental, String datosEIEL,
			String permisos, String personaContacto, String telefonoContacto,
			Calendar fechaAprobacion, String[] documentosAdjuntos,
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar fechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra,
			String empresaAdjudicataria, Calendar fechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar actaReplanteo, Calendar fechaComienzo,
			Calendar fechaFinalizacion, Calendar prorrogas, Calendar actaRecepcion, 
			Double certificacionFinal, Calendar resolucionCertificacion, Calendar informacionCambiosEIEL,
			Calendar reformados, Double liquidacion, Calendar fechaLiquidacion, String detalles, Calendar fechaSolicitud, 
			Calendar fechaAprobacionPrincipado) throws ConfigurationException{
		super(1,1);
		this.setId(planesObraID);
		this.setDescription(planesObraDescription);
		this.setStartWarning(startPlanesObra);
		this.setListaDeDocumentos(doculist);
		this.plan = plan;
		this.anios = anios;
		this.nombre = nombre;
		this.paraje = paraje;
		this.presupuestoEstimado = presupuestoEstimado;
		this.presupuestoDefinitivo = presupuestoDefinitivo;
		this.existeProyecto = existeProyecto;
		this.infraestructura = infraestructura;
		this.estudioAmbiental = estudioAmbiental;
		this.datosEIEL = datosEIEL;
		this.permisos = permisos;
		this.personaContacto = personaContacto;
		this.telefonoContacto = telefonoContacto;
		this.fechaAprobacion = fechaAprobacion;
		this.documentosAdjuntos = documentosAdjuntos;
		this.serviciosAfectados = serviciosAfectados;
		this.destinatario = destinatario;
		this.recibi = recibi;
		this.fechaRecibi = fechaRecibi;
		this.supervision = supervision;
		this.directorProyecto = directorProyecto;
		this.autorProyecto = autorProyecto;
		this.directorObra = directorObra;
		this.empresaAdjudicataria = empresaAdjudicataria;
		this.fechaResolucion = fechaResolucion;
		this.presupuestoAdjudicacion = presupuestoAdjudicacion;
		this.coordinadorSegSalud = coordinadorSegSalud;
		this.actaReplanteo = actaReplanteo;
		this.fechaComienzo = fechaComienzo;
		this.fechaFinalizacion = fechaFinalizacion;
		this.prorrogas = prorrogas;
		this.actaRecepcion = actaRecepcion;
		this.certificacionFinal = certificacionFinal;
		this.resolucionCertificacion = resolucionCertificacion;
		this.informacionCambiosEIEL = informacionCambiosEIEL;
		this.reformados = reformados;
		this.liquidacion = liquidacion;
		this.fechaLiquidacion = fechaLiquidacion;
		this.detalles = detalles;
		this.obraNueva = obraNueva;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaAprobacionPrincipado = fechaAprobacionPrincipado;		
	}
	public LocalGISPlanesObra(Integer planesObraID, Integer idUserCreator, Integer idMunicipio, LayerFeatureBean[] featureRelation, Document[] documentList, 
			Calendar startPlanesObra, String planesObraDescription, String plan, String anios, String nombre,
			String paraje, Double presupuestoEstimado,
			Double presupuestoDefinitivo, String existeProyecto,
			String infraestructura, String obraNueva, String estudioAmbiental, String datosEIEL,
			String permisos, String personaContacto, String telefonoContacto,
			Calendar fechaAprobacion, String[] documentosAdjuntos,
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar fechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra,
			String empresaAdjudicataria, Calendar fechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar actaReplanteo, Calendar fechaComienzo,
			Calendar fechaFinalizacion, Calendar prorrogas, Calendar actaRecepcion, 
			Double certificacionFinal, Calendar resolucionCertificacion, Calendar informacionCambiosEIEL,
			Calendar reformados, Double liquidacion, Calendar fechaLiquidacion, String detalles, Calendar fechaSolicitud, 
			Calendar fechaAprobacionPrincipado, Integer idEntidad, String municipio) throws ConfigurationException{
		super(idUserCreator,idMunicipio, idEntidad);
		this.setId(planesObraID);
		this.setDescription(planesObraDescription);
		this.setStartWarning(startPlanesObra);
		this.setFeatureRelation(featureRelation);
		this.setListaDeDocumentos(documentList);
		
		this.municipio = municipio;
		this.plan = plan;
		this.anios = anios;
		this.nombre = nombre;
		this.paraje = paraje;
		this.presupuestoEstimado = presupuestoEstimado;
		this.presupuestoDefinitivo = presupuestoDefinitivo;
		this.existeProyecto = existeProyecto;
		this.infraestructura = infraestructura;
		this.estudioAmbiental = estudioAmbiental;
		this.datosEIEL = datosEIEL;
		this.permisos = permisos;
		this.personaContacto = personaContacto;
		this.telefonoContacto = telefonoContacto;
		this.fechaAprobacion = fechaAprobacion;
		this.documentosAdjuntos = documentosAdjuntos;
		this.serviciosAfectados = serviciosAfectados;
		this.destinatario = destinatario;
		this.recibi = recibi;
		this.fechaRecibi = fechaRecibi;
		this.supervision = supervision;
		this.directorProyecto = directorProyecto;
		this.autorProyecto = autorProyecto;
		this.directorObra = directorObra;
		this.empresaAdjudicataria = empresaAdjudicataria;
		this.fechaResolucion = fechaResolucion;
		this.presupuestoAdjudicacion = presupuestoAdjudicacion;
		this.coordinadorSegSalud = coordinadorSegSalud;
		this.actaReplanteo = actaReplanteo;
		this.fechaComienzo = fechaComienzo;
		this.fechaFinalizacion = fechaFinalizacion;
		this.prorrogas = prorrogas;
		this.actaRecepcion = actaRecepcion;
		this.certificacionFinal = certificacionFinal;
		this.resolucionCertificacion = resolucionCertificacion;
		this.informacionCambiosEIEL = informacionCambiosEIEL;
		this.reformados = reformados;
		this.liquidacion = liquidacion;
		this.fechaLiquidacion = fechaLiquidacion;
		this.detalles = detalles;
		this.obraNueva = obraNueva;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaAprobacionPrincipado = fechaAprobacionPrincipado;		
	}
	
	public LocalGISPlanesObra(LocalGISNote note) throws ConfigurationException{
		super(note.getUserCreator(),note.getIdMunicipio());
		setId(note.getId());
		setDescription(note.getDescription());
		setFeatureRelation(note.getFeatureRelation());
		setListaDeDocumentos(note.getListaDeDocumentos());
		setStartWarning(note.getStartWarning());
		setIdEntidad(note.getIdEntidad());
	}
	
	
	public LocalGISPlanesObra(Integer planesObraID, Integer idUserCreator, Integer idMunicipio, 
			Calendar startPlanesObra, String planesObraDescription, String plan, String anios, String nombre,
			String paraje, Double presupuestoEstimado,
			Double presupuestoDefinitivo, String existeProyecto,
			String infraestructura, String obraNueva, String estudioAmbiental, String datosEIEL,
			String permisos, String personaContacto, String telefonoContacto,
			Calendar fechaAprobacion, String[] documentosAdjuntos,
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar fechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra,
			String empresaAdjudicataria, Calendar fechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar actaReplanteo, Calendar fechaComienzo,
			Calendar fechaFinalizacion, Calendar prorrogas, Calendar actaRecepcion, 
			Double certificacionFinal, Calendar resolucionCertificacion, Calendar informacionCambiosEIEL,
			Calendar reformados, Double liquidacion, Calendar fechaLiquidacion, String detalles, Calendar fechaSolicitud, 
			Calendar fechaAprobacionPrincipado, Integer idEntidad, String municipio) throws ConfigurationException{
		super(idUserCreator,idMunicipio, startPlanesObra, idEntidad);
		this.setId(planesObraID);
		this.setDescription(planesObraDescription);
		
		this.municipio = municipio;
		this.plan = plan;
		this.anios = anios;
		this.nombre = nombre;
		this.paraje = paraje;
		this.presupuestoEstimado = presupuestoEstimado;
		this.presupuestoDefinitivo = presupuestoDefinitivo;
		this.existeProyecto = existeProyecto;
		this.infraestructura = infraestructura;
		this.estudioAmbiental = estudioAmbiental;
		this.datosEIEL = datosEIEL;
		this.permisos = permisos;
		this.personaContacto = personaContacto;
		this.telefonoContacto = telefonoContacto;
		this.fechaAprobacion = fechaAprobacion;
		this.documentosAdjuntos = documentosAdjuntos;
		this.serviciosAfectados = serviciosAfectados;
		this.destinatario = destinatario;
		this.recibi = recibi;
		this.fechaRecibi = fechaRecibi;
		this.supervision = supervision;
		this.directorProyecto = directorProyecto;
		this.autorProyecto = autorProyecto;
		this.directorObra = directorObra;
		this.empresaAdjudicataria = empresaAdjudicataria;
		this.fechaResolucion = fechaResolucion;
		this.presupuestoAdjudicacion = presupuestoAdjudicacion;
		this.coordinadorSegSalud = coordinadorSegSalud;
		this.actaReplanteo = actaReplanteo;
		this.fechaComienzo = fechaComienzo;
		this.fechaFinalizacion = fechaFinalizacion;
		this.prorrogas = prorrogas;
		this.actaRecepcion = actaRecepcion;
		this.certificacionFinal = certificacionFinal;
		this.resolucionCertificacion = resolucionCertificacion;
		this.informacionCambiosEIEL = informacionCambiosEIEL;
		this.reformados = reformados;
		this.liquidacion = liquidacion;
		this.fechaLiquidacion = fechaLiquidacion;
		this.detalles = detalles;
		this.obraNueva = obraNueva;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaAprobacionPrincipado = fechaAprobacionPrincipado;
	}
	
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
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
	public Calendar getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Calendar fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
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
	public Calendar getFechaRecibi() {
		return fechaRecibi;
	}
	public void setFechaRecibi(Calendar fechaRecibi) {
		this.fechaRecibi = fechaRecibi;
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
	public Double getLiquidacion() {
		return liquidacion;
	}
	public void setLiquidacion(Double liquidacion) {
		this.liquidacion = liquidacion;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public Calendar getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Calendar fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Calendar getActaReplanteo() {
		return actaReplanteo;
	}

	public void setActaReplanteo(Calendar actaReplanteo) {
		this.actaReplanteo = actaReplanteo;
	}

	public Calendar getFechaComienzo() {
		return fechaComienzo;
	}

	public void setFechaComienzo(Calendar fechaComienzo) {
		this.fechaComienzo = fechaComienzo;
	}

	public Calendar getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Calendar fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public Calendar getProrrogas() {
		return prorrogas;
	}

	public void setProrrogas(Calendar prorrogas) {
		this.prorrogas = prorrogas;
	}

	public Calendar getActaRecepcion() {
		return actaRecepcion;
	}

	public void setActaRecepcion(Calendar actaRecepcion) {
		this.actaRecepcion = actaRecepcion;
	}

	public Double getCertificacionFinal() {
		return certificacionFinal;
	}

	public void setCertificacionFinal(Double certificacionFinal) {
		this.certificacionFinal = certificacionFinal;
	}

	public Calendar getResolucionCertificacion() {
		return resolucionCertificacion;
	}

	public void setResolucionCertificacion(Calendar resolucionCertificacion) {
		this.resolucionCertificacion = resolucionCertificacion;
	}

	public Calendar getInformacionCambiosEIEL() {
		return informacionCambiosEIEL;
	}

	public void setInformacionCambiosEIEL(Calendar informacionCambiosEIEL) {
		this.informacionCambiosEIEL = informacionCambiosEIEL;
	}

	public Calendar getReformados() {
		return reformados;
	}

	public void setReformados(Calendar reformados) {
		this.reformados = reformados;
	}

	public Calendar getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Calendar fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public String getObraNueva() {
		return obraNueva;
	}

	public void setObraNueva(String obraNueva) {
		this.obraNueva = obraNueva;
	}

	public Calendar getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Calendar fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Calendar getFechaAprobacionPrincipado() {
		return fechaAprobacionPrincipado;
	}

	public void setFechaAprobacionPrincipado(Calendar fechaAprobacionPrincipado) {
		this.fechaAprobacionPrincipado = fechaAprobacionPrincipado;
	}

}
