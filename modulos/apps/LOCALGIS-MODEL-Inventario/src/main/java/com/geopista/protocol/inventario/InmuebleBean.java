/**
 * InmuebleBean.java
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

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 13-jul-2006
 * Time: 13:04:27
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que implementa un objeto de tipo inmueble (rustico, urbano)
 */
public class InmuebleBean extends BienBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String direccion;
    private String linderoNorte;
    private String linderoSur;
    private String linderoEste;
    private String linderoOeste;
    private RegistroBean registro;
    private String registroDesc;
    private String calificacion;
    private Double edificabilidad;
    private String tipoConstruccion;
    private Date fechaObra;
    private String estadoConservacion;
    private String cubierta;
    private String carpinteria;
    private String fachada;
    private double superficieRegistralSuelo= -1;
    private double superficieCatastralSuelo= -1;
    private double superficieRealSuelo= -1;
    private double valorAdquisicionSuelo= -1;
    private double valorCatastralSuelo= -1;
    private double valorActualSuelo= -1;
    private double superficieRegistralConstruccion= -1;
    private double superficieCatastralConstruccion= -1;
    private double superficieRealConstruccion= -1;
    private double valorAdquisicionConstruccion= -1;
    private double valorCatastralConstruccion= -1;
    private double valorActualConstruccion= -1;
    private double valorAdquisicionInmueble= -1;
    private double valorActualInmueble= -1;
    private String derechosRealesFavor;
    private double valorDerechosFavor= -1;
    private String derechosRealesContra;
    private double valorDerechosContra= -1;
    private String derechosPersonales;
    private String refCatastral;
    private InmuebleUrbanoBean inmuebleUrbano;
    private String destino;
    private String propiedad;
    private InmuebleRusticoBean inmuebleRustico;
   
    private String numPlantas;
    private String tipoConstruccionDesc;
    private String estadoConservacionDesc;
    private String fachadaDesc;
    private String cubiertaDesc;
    private String carpinteriaDesc;
    private double superficieConstruidaConstruccion= -1;
    private double superficieEnPlantaConstruccion= -1;
    private double superficieOcupadaConstruccion= -1;
    private Date fechaAdquisicionSuelo;
    private Collection referenciasCatastrales;
    private Collection usosFuncionales;
    private String numeroOrden;
    private String numeroPropiedad;
    private double valorCatastralInmueble =-1;
    private Integer anioValorCatastral;
    private String edificabilidadDesc;
    private Date fechaAdquisicionObra;
    private String clase;
    private boolean bic;
    private boolean catalogado;
    private Collection aprovechamientos;

    /**
     * @return the numeroOrden
     */
    public String getNumeroOrden()
    {
        return numeroOrden;
    }
    
    public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
     * @param numeroOrden the numeroOrden to set
     */
    public void setNumeroOrden(String numeroOrden)
    {
        this.numeroOrden = numeroOrden;
    }

    /**
     * @return the numeroPropiedad
     */
    public String getNumeroPropiedad()
    {
        return numeroPropiedad;
    }

    /**
     * @param numeroPropiedad the numeroPropiedad to set
     */
    public void setNumeroPropiedad(String numeroPropiedad)
    {
        this.numeroPropiedad = numeroPropiedad;
    }

    public Collection getUsosFuncionales() {
        return usosFuncionales;
    }

    public void setUsosFuncionales(Collection usosFuncionales) {
        this.usosFuncionales = usosFuncionales;
    }


    public Collection getReferenciasCatastrales() {
        return referenciasCatastrales;
    }

    public void setReferenciasCatastrales(Collection referenciasCatastrales) {
        this.referenciasCatastrales = referenciasCatastrales;
    }

  
    public Date getFechaAdquisicionSuelo() {
        return fechaAdquisicionSuelo;
    }

    public void setFechaAdquisicionSuelo(Date fechaAdquisicionSuelo) {
        this.fechaAdquisicionSuelo = fechaAdquisicionSuelo;
    }

    public double getSuperficieConstruidaConstruccion() {
        return superficieConstruidaConstruccion;
    }

    public void setSuperficieConstruidaConstruccion(double superficieConstruidaConstruccion) {
        this.superficieConstruidaConstruccion = superficieConstruidaConstruccion;
    }

    public double getSuperficieEnPlantaConstruccion() {
        return superficieEnPlantaConstruccion;
    }

    public void setSuperficieEnPlantaConstruccion(double superficieEnPlantaConstruccion) {
        this.superficieEnPlantaConstruccion = superficieEnPlantaConstruccion;
    }

    public double getSuperficieOcupadaConstruccion() {
        return superficieOcupadaConstruccion;
    }

    public void setSuperficieOcupadaConstruccion(double superficieOcupadaConstruccion) {
        this.superficieOcupadaConstruccion = superficieOcupadaConstruccion;
    }

    public String getTipoConstruccionDesc() {
        return tipoConstruccionDesc;
    }

    public void setTipoConstruccionDesc(String tipoConstruccionDesc) {
        this.tipoConstruccionDesc = tipoConstruccionDesc;
    }

    public String getEstadoConservacionDesc() {
        return estadoConservacionDesc;
    }

    public void setEstadoConservacionDesc(String estadoConservacionDesc) {
        this.estadoConservacionDesc = estadoConservacionDesc;
    }

    public String getFachadaDesc() {
        return fachadaDesc;
    }

    public void setFachadaDesc(String fachadaDesc) {
        this.fachadaDesc = fachadaDesc;
    }

    public String getCubiertaDesc() {
        return cubiertaDesc;
    }

    public void setCubiertaDesc(String cubiertaDesc) {
        this.cubiertaDesc = cubiertaDesc;
    }

    public String getCarpinteriaDesc() {
        return carpinteriaDesc;
    }

    public void setCarpinteriaDesc(String carpinteriaDesc) {
        this.carpinteriaDesc = carpinteriaDesc;
    }

    public String getFachada() {
        return fachada;
    }

    public void setFachada(String fachada) {
        this.fachada = fachada;
    }

    public String getNumPlantas() {
        return numPlantas;
    }

    public void setNumPlantas(String numPlantas) {
        this.numPlantas = numPlantas;
    }

    public String getRefCatastral() {
        return refCatastral;
    }

    public void setRefCatastral(String refCatastral) {
        this.refCatastral = refCatastral;
    }

   
    public InmuebleRusticoBean getInmuebleRustico() {
        return inmuebleRustico;
    }

    public void setInmuebleRustico(InmuebleRusticoBean inmuebleRustico) {
        this.inmuebleRustico = inmuebleRustico;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

  

    public InmuebleUrbanoBean getInmuebleUrbano() {
        return inmuebleUrbano;
    }

    public void setInmuebleUrbano(InmuebleUrbanoBean inmuebleUrbano) {
        this.inmuebleUrbano = inmuebleUrbano;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLinderoNorte() {
        return linderoNorte;
    }

    public void setLinderoNorte(String linderoNorte) {
        this.linderoNorte = linderoNorte;
    }

    public String getLinderoSur() {
        return linderoSur;
    }

    public void setLinderoSur(String linderoSur) {
        this.linderoSur = linderoSur;
    }

    public String getLinderoEste() {
        return linderoEste;
    }

    public void setLinderoEste(String linderoEste) {
        this.linderoEste = linderoEste;
    }

    public String getLinderoOeste() {
        return linderoOeste;
    }

    public void setLinderoOeste(String linderoOeste) {
        this.linderoOeste = linderoOeste;
    }

    public String getRegistroTomo() {
        return (registro==null?null:registro.getTomo());
    }

    public void setRegistroTomo(String registroTomo) {
        if (registro==null) this.registro=new RegistroBean();
        registro.setTomo(registroTomo);
    }

    public String getRegistroFolio() {
        return (registro==null?null:registro.getFolio());
    }

    public void setRegistroFolio(String registroFolio) {
        if (registro==null) this.registro=new RegistroBean();
        registro.setFolio(registroFolio);
    }

    public String getRegistroLibro() {
        return (registro==null?null:registro.getLibro());
    }

    public void setRegistroLibro(String registroLibro) {
        if (registro==null) this.registro=new RegistroBean();
         registro.setLibro(registroLibro);
    }

    public String getRegistroFinca() {
        return (registro==null?null:registro.getFinca());
    }

    public void setRegistroFinca(String registroFinca) {
        if (registro==null) this.registro=new RegistroBean();
             registro.setFinca(registroFinca);
    }

    public String getRegistroInscripcion() {
        return (registro==null?null:registro.getInscripcion());
    }

    public void setRegistroInscripcion(String registroInscripcion) {
        if (registro==null) this.registro=new RegistroBean();
               registro.setInscripcion(registroInscripcion);
    }

    public String getRegistroProtocolo() {
        return (registro==null?null:registro.getProtocolo());
    }

    public void setRegistroProtocolo(String registroProtocolo) {
        if (registro==null) this.registro=new RegistroBean();
        registro.setProtocolo(registroProtocolo);
    }

    public String getRegistroNotario() {
        return (registro==null?null:registro.getNotario());
    }

    public void setRegistroNotario(String registroNotario) {
        if (registro==null) this.registro=new RegistroBean();
        registro.setNotario(registroNotario);
    }

    public String getRegistroPropiedad() {
        return (registro==null?null:registro.getPropiedad());
    }

    public void setRegistroPropiedad(String propiedad) {
        if (this.registro==null) this.registro=new RegistroBean();
        this.registro.setPropiedad(propiedad);

    }

    public RegistroBean getRegistro() {
          return registro;
      }

      public void setRegistro(RegistroBean registro) {
          this.registro= registro;

      }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public Double getEdificabilidad() {
        return edificabilidad;
    }

    public void setEdificabilidad(double edificabilidad) {
        this.edificabilidad = new Double(edificabilidad);
    }

    public String getTipoConstruccion() {
        return tipoConstruccion;
    }

    public void setTipoConstruccion(String tipoConstruccion) {
        this.tipoConstruccion = tipoConstruccion;
    }

    public Date getFechaObra() {
        return fechaObra;
    }

    public void setFechaObra(Date fechaObra) {
        this.fechaObra = fechaObra;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }

    public String getCubierta() {
        return cubierta;
    }

    public void setCubierta(String cubierta) {
        this.cubierta = cubierta;
    }

    public String getCarpinteria() {
        return carpinteria;
    }

    public void setCarpinteria(String carpinteria) {
        this.carpinteria = carpinteria;
    }

    public double getSuperficieRegistralSuelo() {
        return superficieRegistralSuelo;
    }

    public void setSuperficieRegistralSuelo(double superficieRegistralSuelo) {
        this.superficieRegistralSuelo = superficieRegistralSuelo;
    }

    public double getSuperficieCatastralSuelo() {
        return superficieCatastralSuelo;
    }

    public void setSuperficieCatastralSuelo(double superficieCatastralSuelo) {
        this.superficieCatastralSuelo = superficieCatastralSuelo;
    }

    public double getSuperficieRealSuelo() {
        return superficieRealSuelo;
    }

    public void setSuperficieRealSuelo(double superficieRealSuelo) {
        this.superficieRealSuelo = superficieRealSuelo;
    }

    public double getValorAdquisicionSuelo() {
        return valorAdquisicionSuelo;
    }

    public void setValorAdquisicionSuelo(double valorAdquisicionSuelo) {
        this.valorAdquisicionSuelo = valorAdquisicionSuelo;
    }

    public double getValorCatastralSuelo() {
        return valorCatastralSuelo;
    }

    public void setValorCatastralSuelo(double valorCatastralSuelo) {
        this.valorCatastralSuelo = valorCatastralSuelo;
    }

    public double getValorActualSuelo() {
        return valorActualSuelo;
    }

    public void setValorActualSuelo(double valorActualSuelo) {
        this.valorActualSuelo = valorActualSuelo;
    }

    public double getSuperficieRegistralConstruccion() {
        return superficieRegistralConstruccion;
    }

    public void setSuperficieRegistralConstruccion(double superficieRegistralConstruccion) {
        this.superficieRegistralConstruccion = superficieRegistralConstruccion;
    }

    public double getSuperficieCatastralConstruccion() {
        return superficieCatastralConstruccion;
    }

    public void setSuperficieCatastralConstruccion(double superficieCatastralConstruccion) {
        this.superficieCatastralConstruccion = superficieCatastralConstruccion;
    }

    public double getSuperficieRealConstruccion() {
        return superficieRealConstruccion;
    }

    public void setSuperficieRealConstruccion(double superficieRealConstruccion) {
        this.superficieRealConstruccion = superficieRealConstruccion;
    }

    public double getValorAdquisicionConstruccion() {
        return valorAdquisicionConstruccion;
    }

    public void setValorAdquisicionConstruccion(double valorAdquisicionConstruccion) {
        this.valorAdquisicionConstruccion = valorAdquisicionConstruccion;
    }

    public double getValorCatastralConstruccion() {
        return valorCatastralConstruccion;
    }

    public void setValorCatastralConstruccion(double valorCatastralConstruccion) {
        this.valorCatastralConstruccion = valorCatastralConstruccion;
    }

    public double getValorActualConstruccion() {
        return valorActualConstruccion;
    }

    public void setValorActualConstruccion(double valorActualConstruccion) {
        this.valorActualConstruccion = valorActualConstruccion;
    }

    public double getValorAdquisicionInmueble() {
        return valorAdquisicionInmueble;
    }

    public void setValorAdquisicionInmueble(double valorAdquisicionInmueble) {
        this.valorAdquisicionInmueble = valorAdquisicionInmueble;
    }

    public double getValorActualInmueble() {
        return valorActualInmueble;
    }

    public void setValorActualInmueble(double valorActualInmueble) {
        this.valorActualInmueble = valorActualInmueble;
    }

    public String getDerechosRealesFavor() {
        return derechosRealesFavor;
    }

    public void setDerechosRealesFavor(String derechosRealesFavor) {
        this.derechosRealesFavor = derechosRealesFavor;
    }

    public double getValorDerechosFavor() {
        return valorDerechosFavor;
    }

    public void setValorDerechosFavor(double valorDerechosFavor) {
        this.valorDerechosFavor = valorDerechosFavor;
    }

    public String getDerechosRealesContra() {
        return derechosRealesContra;
    }

    public void setDerechosRealesContra(String derechosRealesContra) {
        this.derechosRealesContra = derechosRealesContra;
    }

    public double getValorDerechosContra() {
        return valorDerechosContra;
    }

    public void setValorDerechosContra(double valorDerechosContra) {
        this.valorDerechosContra = valorDerechosContra;
    }

    public String getDerechosPersonales() {
        return derechosPersonales;
    }

    public void setDerechosPersonales(String derechosPersonales) {
        this.derechosPersonales = derechosPersonales;
    }

    public boolean isUrbano(){
        if ((getTipo() != null) && (getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS))) return true;
        return false;
    }
    public boolean isRustico(){
        if ((getTipo() != null) && (getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS))) return true;
        return false;
    }

	public String getRegistroDesc() {
		return registroDesc;
	}

	public void setRegistroDesc(String registroDesc) {
		this.registroDesc = registroDesc;
	}

	public double getValorCatastralInmueble() {
		return valorCatastralInmueble;
	}

	public void setValorCatastralInmueble(double valorCatastralInmueble) {
		this.valorCatastralInmueble = valorCatastralInmueble;
	}

	public Integer getAnioValorCatastral() {
		return anioValorCatastral;
	}

	public void setAnioValorCatastral(Integer anioValorCatastral) {
		this.anioValorCatastral = anioValorCatastral;
	}

	public String getEdificabilidadDesc() {
		return edificabilidadDesc;
	}

	public void setEdificabilidadDesc(String edificabilidadDesc) {
		this.edificabilidadDesc = edificabilidadDesc;
	}

	public Date getFechaAdquisicionObra() {
		return fechaAdquisicionObra;
	}

	public void setFechaAdquisicionObra(Date fechaAdquisicionObra) {
		this.fechaAdquisicionObra = fechaAdquisicionObra;
	}

	public void setEdificabilidad(Double edificabilidad) {
		this.edificabilidad = edificabilidad;
	}

	public boolean isBic() {
		return bic;
	}

	public void setBic(boolean bic) {
		this.bic = bic;
	}

	public boolean isCatalogado() {
		return catalogado;
	}

	public void setCatalogado(boolean catalogado) {
		this.catalogado = catalogado;
	}

	public void setAprovechamientos(Collection aprovechamientos) {
		this.aprovechamientos = aprovechamientos;
	}

	public Collection getAprovechamientos() {
		return aprovechamientos;
	}


}
