package com.geopista.protocol.licencias;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.tipos.CTipoFinalizacion;
import com.geopista.protocol.licencias.tipos.CTipoTramitacion;
import com.geopista.protocol.ocupacion.CDatosOcupacion;

/**
 * @author SATEC
 * @version $Revision: 1.4 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2009/09/14 14:26:19 $
 *          $Name:  $
 *          $RCSfile: CExpedienteLicencia.java,v $
 *          $Revision: 1.4 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CExpedienteLicencia implements java.io.Serializable {

	private Logger logger = Logger.getLogger(CExpedienteLicencia.class);
	
	private String numExpediente;
	private long idSolicitud;
	private CTipoTramitacion tipoTramitacion;
	private CTipoFinalizacion tipoFinalizacion;
	private String servicioEncargado;
	private String asunto;
	private String silencioAdministrativo;
	private String formaInicio;
	private int numFolios;
	private Date fechaApertura;
	private String responsable;
	private Date plazoVencimiento;
	private String habiles;
	private int numDias;
	private String observaciones;
	private String silencioEvent;
	private String plazoEvent;
	private String silencioTriggered;
	private Date fechaCambioEstado;
	private CEstado estado;
	private Vector notificaciones;
    private Vector eventos;
    private Vector historico;
    private String VU= "0";
	private String CNAE;
	private CDatosOcupacion datosOcupacion;
    private Alegacion alegacion;
    private String bloqueado= "0";
    private Vector informes;
    private Vector callesAfec;
    private Resolucion resolucion;
    private boolean bloqueaUsuario= false;

    private SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    private String idSigem = null;

    public Vector getInformes() {
        return informes;
    }

    public void setInformes(Vector informes) {
        this.informes = informes;
    }

    public Vector getCallesAfec() {
        return callesAfec;
    }

    public void setCallesAfec(Vector callesAfec) {
        this.callesAfec = callesAfec;
    }

    public void addInformes(Informe informe)
    {
        if (this.informes==null) this.informes=new Vector();
        this.informes.add(informe);
    }
    public Informe getInforme(String idInforme)
    {
        if (this.informes==null || idInforme==null) return null;
        for (Enumeration e=informes.elements();e.hasMoreElements();)
        {
            Informe auxInforme=(Informe) e.nextElement();
            if (idInforme.equals(auxInforme.getId()))
                return auxInforme;
        }
        return null;
    }

	//Extensión para consultas rapidas
	private String tipoLicenciaDescripcion;
	private String nifPropietario;
	private String tipoViaAfecta;
	private String nombreViaAfecta;
	private String numeroViaAfecta;
    private String patronTipoObraSolicitud;

    //Extensión para impresion
    private ListaEstructuras estructuraEstado;
    private ListaEstructuras estructuraTipoOcupacion;
    private ListaEstructuras estructuraTipoObra;
    private ListaEstructuras estructuraTipoLicencia;
    private String locale;
    private CSolicitudLicencia solicitud;



	public CExpedienteLicencia() {
	}

	public CExpedienteLicencia(CEstado estado) {
		this.estado=estado;
	}

	public CExpedienteLicencia(String numExpediente, long idSolicitud, CTipoTramitacion tipoTramitacion, CTipoFinalizacion tipoFinalizacion, String servicioEncargado, String asunto, String silencioAdministrativo, String formaInicio, int numFolios, Date fechaApertura, String responsable, Date plazoVencimiento, String habiles, int numDias, String observaciones,CEstado estado, Vector notificaciones) {
		this.numExpediente = numExpediente;
		this.idSolicitud = idSolicitud;
		this.tipoTramitacion = tipoTramitacion;
		this.tipoFinalizacion = tipoFinalizacion;
		this.servicioEncargado = servicioEncargado;
		this.asunto = asunto;
		this.silencioAdministrativo = silencioAdministrativo;
		this.formaInicio = formaInicio;
		this.numFolios = numFolios;
		this.fechaApertura = fechaApertura;
		this.responsable = responsable;
		this.plazoVencimiento = plazoVencimiento;
		this.habiles = habiles;
		this.numDias = numDias;
        this.observaciones = observaciones;
		this.estado=estado;
        this.notificaciones= notificaciones;

	}

    public Resolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resolucion resolucion) {
        this.resolucion = resolucion;
    }
    
    public String getAsuntoResolucion(){
    	if(resolucion!=null){
    		return resolucion.getAsunto();
    	}
    	return null;
    }
    
    public String getFechaResolucion(){
    	if(resolucion!=null){
    		return formato.format(resolucion.getFecha());
    	}
    	return null;
    }
    
    public void setFechaResolucion(String str){
    	try {
    		if(str!=null && str.length()>0){
    			resolucion.setFecha(formato.parse(str));
    		}
    		else {
    			resolucion.setFecha(null);
    		}
    	} catch (Exception e) { logger.error("Formato fecha incorrecto: " + str);}
    }
    
    public String getFechaResolucionString(){
    	if(resolucion!=null){
    		return sdf.format(resolucion.getFecha());
    	}
    	return null;
    }
    
    public void setFechaResolucionString(String str){
    	try {
    		if(str!=null && str.length()>0){
    			resolucion.setFecha(sdf.parse(str));
    		}
    		else {
    			resolucion.setFecha(null);
    		}
    	} catch (Exception e) { logger.error("Formato fecha incorrecto: " + str);}
    }

	public String getCNAE() {
		return CNAE;
	}

	public void setCNAE(String CNAE) {
		this.CNAE = CNAE;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public CTipoTramitacion getTipoTramitacion() {
		return tipoTramitacion;
	}

	public void setTipoTramitacion(CTipoTramitacion tipoTramitacion) {
		this.tipoTramitacion = tipoTramitacion;
	}

	public CTipoFinalizacion getTipoFinalizacion() {
		return tipoFinalizacion;
	}

	public void setTipoFinalizacion(CTipoFinalizacion tipoFinalizacion) {
		this.tipoFinalizacion = tipoFinalizacion;
	}

	public String getServicioEncargado() {
		return servicioEncargado;
	}

	public void setServicioEncargado(String servicioEncargado) {
		this.servicioEncargado = servicioEncargado;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getSilencioAdministrativo() {
		return silencioAdministrativo;
	}

	public void setSilencioAdministrativo(String silencioAdministrativo) {
		this.silencioAdministrativo = silencioAdministrativo;
	}

	public String getFormaInicio() {
		return formaInicio;
	}

	public void setFormaInicio(String formaInicio) {
		this.formaInicio = formaInicio;
	}

	public int getNumFolios() {
		return numFolios;
	}

	public void setNumFolios(int numFolios) {
		this.numFolios = numFolios;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}
    public String getFechaAperturaString() {
        if (fechaApertura==null) return null;
		return sdf.format(fechaApertura);
	}
    public void setFechaAperturaString(String str){
    	try {
    		if(str!=null && str.length()>0){
    			fechaApertura = sdf.parse(str);
    		}
    		else {
    			fechaApertura = null;
    		}
    	} catch (Exception e) {logger.error("Formato fecha incorrecto: " + str);}
    }

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Date getPlazoVencimiento() {
		return plazoVencimiento;
	}
    public String getPlazoVencimientoString() {
        if (plazoVencimiento==null) return null;
		return formato.format(plazoVencimiento);
	}


	public void setPlazoVencimiento(Date plazoVencimiento) {
		this.plazoVencimiento = plazoVencimiento;
	}

	public String getHabiles() {
		return habiles;
	}

	public void setHabiles(String habiles) {
		this.habiles = habiles;
	}

	public int getNumDias() {
		return numDias;
	}

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
	}

	public Vector getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(Vector notificaciones) {
		this.notificaciones = notificaciones;
	}
    public void setLocale(String locale)
    {
        this.locale=locale;
    }
    public void setEstructuraEstado(ListaEstructuras estructuraEstado)
    {
        this.estructuraEstado=estructuraEstado;
    }
    public void setEstructuraTipoLicencia(ListaEstructuras estructuraTipoLicencia)
    {
        this.estructuraTipoLicencia=estructuraTipoLicencia;
    }

    public String getEstadoString()
    {
        if (getEstado()==null||estructuraEstado==null) return null;
        return (estructuraEstado.getDomainNode(new Integer(getEstado().getIdEstado()).toString())).getTerm(locale);
    }
    public String getTipoLicenciaString()
    {
        if (solicitud==null || solicitud.getTipoLicencia()==null || estructuraTipoLicencia==null) return null;
        return (estructuraTipoLicencia.getDomainNode(new Integer(solicitud.getTipoLicencia().getIdTipolicencia()).toString())).getTerm(locale);
    }
    
    public String getTipoTramitacionString(){
    	if (tipoTramitacion==null || Estructuras.getListaTiposTramitacion()==null) {return null;}
    	DomainNode domainIdTramitacion = ((DomainNode)Estructuras.getListaTiposTramitacion().getDomainNode(new Integer(tipoTramitacion.getIdTramitacion()).toString()));
    	if(domainIdTramitacion==null){return null;}
    	return domainIdTramitacion.getTerm(locale);
    }
    
    public String getIdTipoTramitacion(){
    	if(tipoTramitacion==null){return "";}
    	return String.valueOf(tipoTramitacion.getIdTramitacion());
    }
    
    public void setIdTipoTramitacion(String str){
    	if(tipoTramitacion==null){return;}
    	tipoTramitacion.setIdTramitacion(Integer.parseInt(str));
    }

	public CEstado getEstado() {
		return estado;
	}

	public void setEstado(CEstado estado) {
		this.estado = estado;
	}

	public String getSilencioEvent() {
		return silencioEvent;
	}

	public void setSilencioEvent(String silencioEvent) {
		this.silencioEvent = silencioEvent;
	}

	public String getPlazoEvent() {
		return plazoEvent;
	}

	public void setPlazoEvent(String plazoEvent) {
		this.plazoEvent = plazoEvent;
	}

	public String getSilencioTriggered() {
		return silencioTriggered;
	}

	public void setSilencioTriggered(String silencioTriggered) {
		this.silencioTriggered = silencioTriggered;
	}

	public Date getFechaCambioEstado() {
		return fechaCambioEstado;
	}

	public void setFechaCambioEstado(Date fechaCambioEstado) {
		this.fechaCambioEstado = fechaCambioEstado;
	}

    public Vector getEventos() {
        return eventos;
    }

    public void setEventos(Vector eventos) {
        this.eventos = eventos;
    }

    public Vector getHistorico() {
        return historico;
    }

    public void setHistorico(Vector historico) {
        this.historico = historico;
    }

    public String getVU() {
        return VU;
    }

    public void setVU(String vu) {
        this.VU= vu;
    }

	public String getTipoLicenciaDescripcion() {
		return tipoLicenciaDescripcion;
	}

	public void setTipoLicenciaDescripcion(String tipoLicenciaDescripcion) {
		this.tipoLicenciaDescripcion = tipoLicenciaDescripcion;
	}

	public String getNifPropietario() {
		return nifPropietario;
	}

	public void setNifPropietario(String nifPropietario) {
		this.nifPropietario = nifPropietario;
	}

	public String getTipoViaAfecta() {
		return tipoViaAfecta;
	}

	public void setTipoViaAfecta(String tipoViaAfecta) {
		this.tipoViaAfecta = tipoViaAfecta;
	}

	public String getNombreViaAfecta() {
		return nombreViaAfecta;
	}

	public void setNombreViaAfecta(String nombreViaAfecta) {
		this.nombreViaAfecta = nombreViaAfecta;
	}

	public String getNumeroViaAfecta() {
		return numeroViaAfecta;
	}

	public void setNumeroViaAfecta(String numeroViaAfecta) {
		this.numeroViaAfecta = numeroViaAfecta;
	}

	public CDatosOcupacion getDatosOcupacion() {
		return datosOcupacion;
	}

	public void setDatosOcupacion(CDatosOcupacion datosOcupacion) {
		this.datosOcupacion = datosOcupacion;
	}

    public Alegacion getAlegacion(){
        return alegacion;
    }

    public void setAlegacion(Alegacion alegacion){
        this.alegacion= alegacion;
    }

    public void setSolicitud(CSolicitudLicencia solicitud)
    {
        this.solicitud=solicitud;
    }
    public CSolicitudLicencia getSolicitud() 
    {
        return solicitud;
    }
    public void setEstructuraTipoOcupacion(ListaEstructuras tipoOcupacion)
    {
        estructuraTipoOcupacion=tipoOcupacion;
    }
    public void setEstructuraTipoObra(ListaEstructuras tipoObra)
    {
       estructuraTipoObra=tipoObra;
    }

    public String getTipoOcupacionSolicitud()
    {
       if (estructuraTipoOcupacion==null || datosOcupacion==null) return null;
       try{return (estructuraTipoOcupacion.getDomainNode(new Integer(datosOcupacion.getTipoOcupacion()).toString())).getTerm(locale);}catch(Exception e){return null;}
    }
    public String getNifPropietarioSolicitud()
    {
       if (solicitud==null || solicitud.getPropietario()==null) return null;
       return solicitud.getPropietario().getDniCif();
    }
    public String getTipoObraSolicitud()
    {
        if (estructuraTipoObra==null || solicitud==null) return null;
        try{return (estructuraTipoObra.getDomainNode(new Integer(solicitud.getTipoObra().getIdTipoObra()).toString())).getTerm(locale);}catch(Exception e){return null;}
    }
    public String getIdTipoObra()
    {
        if (solicitud==null || solicitud.getTipoObra()==null) return "";
        return String.valueOf(solicitud.getTipoObra().getIdTipoObra());
    }
    public void setIdTipoObra(String str)
    {
        if (solicitud==null || solicitud.getTipoObra()==null) return;
        solicitud.getTipoObra().setIdTipoObra(Integer.parseInt(str));
    }
    public String getIdTipoLicencia()
    {
    	 if (solicitud==null || solicitud.getTipoLicencia()==null) return "";
         return String.valueOf(solicitud.getTipoLicencia().getIdTipolicencia());
    }
    public void setIdTipoLicencia(String str)
    {
        if (solicitud==null || solicitud.getTipoLicencia()==null) return;
        solicitud.getTipoLicencia().setIdTipolicencia(Integer.parseInt(str));
    }
    public String getIdEstado()
    {
        if (estado==null) return "";
        return String.valueOf(estado.getIdEstado());
    }
    public void setIdEstado(String str)
    {
        if (estado==null) return;
        estado.setIdEstado(Integer.parseInt(str));
    }
    public String getNombrePropietarioSolicitud()
    {
       if (solicitud==null || solicitud.getPropietario()==null) return null;
       return solicitud.getPropietario().getNombre()+" "+ solicitud.getPropietario().getApellido1()+" "+solicitud.getPropietario().getApellido2();
    }
    public String getAsusntoSolicitud()
     {
        if (solicitud==null) return null;
        return solicitud.getAsunto();
     }

    public String getMotivoSolicitud()
    {
       if (solicitud==null) return null;
       return solicitud.getMotivo();
    }
     public String getObservacionesSolicitud()
    {
       if (solicitud==null) return null;
       return solicitud.getObservaciones();
    }
    public String getNombreViaSolicitud()
    {
          if (solicitud==null) return null;
          return solicitud.getNombreViaAfecta();
    }
    public String getNumeroViaSolicitud()
    {
          if (solicitud==null) return null;
          return solicitud.getNumeroViaAfecta();
    }

    public String getNumSillasOcu()
    {
          if (datosOcupacion==null) return null;
          return new Integer(datosOcupacion.getNumSillas()).toString();
    }
    public String getNumMesasOcu()
    {
          if (datosOcupacion==null) return null;
          return new Integer(datosOcupacion.getNumMesas()).toString();
    }
    public String getAfectaAceraOcu()
    {
          if (datosOcupacion==null || datosOcupacion.getAfectaAcera()==null) return null;
          return (datosOcupacion.getAfectaAcera().equals("1")?"Si":"No");
    }
    public String getAfectaAparcamientoOcu()
    {
          if (datosOcupacion==null || datosOcupacion.getAfectaAparcamiento()==null) return null;
          return (datosOcupacion.getAfectaAparcamiento().equals("1")?"Si":"No");
    }
    public String getAfectaCalzadaOcu()
    {
          if (datosOcupacion==null || datosOcupacion.getAfectaCalzada()==null) return null;
          return (datosOcupacion.getAfectaCalzada().equals("1")?"Si":"No");
    }
    public String getAreaOcu()
    {
          if (datosOcupacion==null) return null;
          return new Double(datosOcupacion.getAreaOcupacion()).toString();
    }
    public String getHoraInicioOcu()
    {
          if (datosOcupacion==null || datosOcupacion.getHoraInicio()==null) return null;
          return new SimpleDateFormat("HH:mm").format(datosOcupacion.getHoraInicio());
    }
    public String getHoraFinOcu()
    {
          if (datosOcupacion==null || datosOcupacion.getHoraFin()==null) return null;
          return new SimpleDateFormat("HH:mm").format(datosOcupacion.getHoraFin());
    }

    public boolean bloqueado(){
        if (getBloqueado() != null){
            if (getBloqueado().equalsIgnoreCase("1"))
                return true;
        }
        return false;        
    }

    public void setBloqueado(String s){
        this.bloqueado= s;
    }

    public String getBloqueado(){
        return bloqueado;
    }

    public void setPatronTipoObraSolicitud(String patron){
        this.patronTipoObraSolicitud= patron;
    }

    public String getPatronTipoObraSolicitud(){
        return patronTipoObraSolicitud;
    }

    public void setBloqueaUsuario(boolean b){
        this.bloqueaUsuario= b;
    }

    public boolean bloqueaUsuario(){
        return bloqueaUsuario;
    }

	public String getIdSigem() {
		return idSigem;
	}

	public void setIdSigem(String idSigem) {
		this.idSigem = idSigem;
	}

}