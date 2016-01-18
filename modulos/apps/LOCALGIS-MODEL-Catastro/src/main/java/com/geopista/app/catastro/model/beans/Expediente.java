/**
 * Expediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * Bean que encapsula todos los datos de los expedientes de la aplicacion.
 * */

public class Expediente implements Serializable {

    /**
	 * Identificador del expediente.
	 */
    private long idExpediente;
    
    /**
     * Entidad generadora.
     */
    private EntidadGeneradora entidadGeneradora;
    
    /**
     * Identificador del estado del expediente.
     */
    private long idEstado;
    
    /**
     * Identificador del tecnico del catastro.
     */
    private String idTecnicoCatastro;
    
    /**
     * Dirección del presentador.
     */
    private DireccionLocalizacion direccionPresentador;
    
    /**
     * Identificador del municipio.
     */
    private long idMunicipio;
    
    /**
     * Numero de expediente.
     */
    private String numeroExpediente;
    
    /**
     * Tipo de tramitacion del expediente.
     *  - Expediente situaciones finales  - true
     *  - Expediente variaciones - false
     */
    private Boolean tipoTramitaExpSitFinales;
    

	/**
     * Tipo del expediente.
     */
    private TipoExpediente tipoExpediente;
    
    /**
     * Fecha de alteración.
     */
    private Date fechaAlteracion;
    
    /**
     * Año del expediente de gerencia.
     */
    private Integer annoExpedienteGerencia;
    
    /**
     * Referencia del expediente de gerencia.
     */
    private String referenciaExpedienteGerencia;
    
    /**
     * Código de la entidad de registro del expediente de la DGC 
     * origen de la alteracion.
     */
    private Integer codigoEntidadRegistroDGCOrigenAlteracion;
    
    /**
     * Año del expediente origen de la alteración.
     */
    private int annoExpedienteAdminOrigenAlteracion;
    
    /**
     * Referencia del expediente origen de la alteración.
     */
    private String referenciaExpedienteAdminOrigen;
    
    /**
     * Fecha de registro.
     */
    private Date fechaRegistro;
    
    /**
     * Fecha de movimiento.
     */
    private Date fechaMovimiento;

    /**
     * Fecha de cierre.
     */

    private String horaMovimiento;

    private Date fechaDeCierre;
    
    /**
     * Tipo del documento origen de la alteración.
     */
    private String tipoDocumentoOrigenAlteracion;
    
    /**
     * Información del documento origen de la alteración.
     */
    private String infoDocumentoOrigenAlteracion;
    
    /**
     * Código descriptivo de la alteración.
     */
    private String codigoDescriptivoAlteracion;
    
    /**
     * Descripción de la alteración.
     */
    private String descripcionAlteracion;
    
    //Panel Datos Personales, el nombre concatenado. Max 60.
    /**
     * Nif del presentador
     */
    private String nifPresentador;
    
    /**
     * Nombre completo del presentador.
     */
    private String nombreCompletoPresentador;
    
    /**
     * Número de bienes inmuebles urbanos.
     */
    private int numBienesInmueblesUrbanos;

    /**
     * Número de bienes inmuebles rústicos.
     */
    private int numBienesInmueblesRusticos;

    /**
     * Número de bienes inmuebles con características especiales.
     */
    private int numBienesInmueblesCaractEsp;
    
    /**
     * Codigo de la provincia de la notaria.
     */
    private String codProvinciaNotaria;

        
    private boolean modoAcoplado;
    
   /**
     * Código de la población de la notaria.
     */
    private String codPoblacionNotaria;
    private String codNotaria;
    private String annoProtocoloNotarial;
    private String protocoloNotarial;

    private ArrayList listaReferencias = new ArrayList();
    private String tipoDeIntercambio;
    private String existenciaInformacionGrafica;
    
    
    //Todo estos no se sabe por ahora.
    private String codigoINEmunicipio;
    public DireccionLocalizacion m_Direccion;
    private boolean existeDeclaracionAlteracion;
    //private FincaLocal fincasLocales[];
    //private Fichero ficherosIntercambio[];
    //private FincaCatastro fincasCatastro;
    //public FincaCatastro m_FincaCatastro;
    
    
    public static final int REGISTRADO=1;
    public static final int ASOCIADO=2;
    public static final int RELLENADO=3;
    public static final int SINCRONIZADO=4;
    public static final int MODIFICADO=5;
    public static final int FINALIZADO=6;
    public static final int GENERADO=7;
    public static final int ENVIADO=8;
    public static final int CERRADO=9;

    public static final String TIPO_INTERCAMBIO_FINAL="F";
    public static final String TIPO_INTERCAMBIO_VARIACIONES="V";
    public static final String TIPO_INTERCAMBIO_REGISTRO="R";

    
    public Expediente(){}
    
    public long getIdExpediente()
    {
        return idExpediente;
    }
    
    public void setIdExpediente(long idExpediente)
    {
        this.idExpediente = idExpediente;
    }
    
    public EntidadGeneradora getEntidadGeneradora()
    {
        return entidadGeneradora;
    }
    
    public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora)
    {
        this.entidadGeneradora = entidadGeneradora;
    }
    
    public long getIdEstado()
    {
        return idEstado;
    }
    
    public void setIdEstado(long idEstado)
    {
        this.idEstado = idEstado;
    }
    
    public String getIdTecnicoCatastro()
    {
        return idTecnicoCatastro;
    }
    
    public void setIdTecnicoCatastro(String idTecnicoCatastro)
    {
        this.idTecnicoCatastro = idTecnicoCatastro;
    }
    
    public DireccionLocalizacion getDireccionPresentador()
    {
        return direccionPresentador;
    }
    
    public void setDireccionPresentador(DireccionLocalizacion direccionPresentador)
    {
        this.direccionPresentador = direccionPresentador;
    }
    
    public long getIdMunicipio()
    {
        return idMunicipio;
    }
    
    public void setIdMunicipio(long idMunicipio)
    {
        this.idMunicipio = idMunicipio;
    }
    
    public String getNumeroExpediente()
    {
        return numeroExpediente;
    }
    
    public void setNumeroExpediente(String numeroExpediente)
    {
        this.numeroExpediente = numeroExpediente;
    }
    
    public TipoExpediente getTipoExpediente()
    {
        return tipoExpediente;
    }
    
    public void setTipoExpediente(TipoExpediente tipoExpediente)
    {
        this.tipoExpediente = tipoExpediente;
    }
    
    public Date getFechaAlteracion()
    {
        return fechaAlteracion;
    }
    
    public void setFechaAlteracion(Date fechaAlteracion)
    {
        this.fechaAlteracion = fechaAlteracion;
    }
    
    public Integer getAnnoExpedienteGerencia(){
        return annoExpedienteGerencia;
    }
    
    public void setAnnoExpedienteGerencia(Integer annoExpedienteGerencia){
        this.annoExpedienteGerencia = annoExpedienteGerencia;
    }
    
    public String getReferenciaExpedienteGerencia()
    {
        return referenciaExpedienteGerencia;
    }
    
    public void setReferenciaExpedienteGerencia(String referenciaExpedienteGerencia)
    {
        this.referenciaExpedienteGerencia = referenciaExpedienteGerencia;
    }
    
    public Integer getCodigoEntidadRegistroDGCOrigenAlteracion(){
        return codigoEntidadRegistroDGCOrigenAlteracion;
    }
    
    public void setCodigoEntidadRegistroDGCOrigenAlteracion(Integer codigoEntidadRegistroDGCOrigenAlteracion){
        this.codigoEntidadRegistroDGCOrigenAlteracion = codigoEntidadRegistroDGCOrigenAlteracion;
    }

    public int getAnnoExpedienteAdminOrigenAlteracion()
    {
        return annoExpedienteAdminOrigenAlteracion;
    }
    
    public void setAnnoExpedienteAdminOrigenAlteracion(int annoExpedienteAdminOrigenAlteracion)
    {
        this.annoExpedienteAdminOrigenAlteracion = annoExpedienteAdminOrigenAlteracion;
    }
    
    public String getReferenciaExpedienteAdminOrigen()
    {
        return referenciaExpedienteAdminOrigen;
    }
    
    public void setReferenciaExpedienteAdminOrigen(String referenciaExpedienteAdminOrigen)
    {
        this.referenciaExpedienteAdminOrigen = referenciaExpedienteAdminOrigen;
    }
    
    public Date getFechaRegistro()
    {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro)
    {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Date getFechaMovimiento()
    {
        return fechaMovimiento;
    }
    
    public void setFechaMovimiento(Date fechaMovimiento)
    {
        this.fechaMovimiento = fechaMovimiento;
    }
    
    public String getHoraMovimiento()
    {
        return horaMovimiento;
    }
    
    public void setHoraMovimiento(String horaMovimiento)
    {
        this.horaMovimiento = horaMovimiento;
    }
    
    public Date getFechaDeCierre()
    {
        return fechaDeCierre;
    }
    
    public void setFechaDeCierre(Date fechaDeCierre)
    {
        this.fechaDeCierre = fechaDeCierre;
    }
    
    public String getTipoDocumentoOrigenAlteracion()
    {
        return tipoDocumentoOrigenAlteracion;
    }
    
    public void setTipoDocumentoOrigenAlteracion(String tipoDocumentoOrigenAlteracion)
    {
        this.tipoDocumentoOrigenAlteracion = tipoDocumentoOrigenAlteracion;
    }
    
    public String getInfoDocumentoOrigenAlteracion()
    {
        return infoDocumentoOrigenAlteracion;
    }
    
    public void setInfoDocumentoOrigenAlteracion(String infoDocumentoOrigenAlteracion)
    {
        this.infoDocumentoOrigenAlteracion = infoDocumentoOrigenAlteracion;
    }
    
    public String getCodigoDescriptivoAlteracion()
    {
        return codigoDescriptivoAlteracion;
    }
    
    public void setCodigoDescriptivoAlteracion(String codigoDescriptivoAlteracion)
    {
        this.codigoDescriptivoAlteracion = codigoDescriptivoAlteracion;
    }
    
    public String getDescripcionAlteracion()
    {
        return descripcionAlteracion;
    }
    
    public void setDescripcionAlteracion(String descripcionAlteracion)
    {
        this.descripcionAlteracion = descripcionAlteracion;
    }
    
    public String getNifPresentador()
    {
        return nifPresentador;
    }
    
    public void setNifPresentador(String nifPresentador)
    {
        this.nifPresentador = nifPresentador;
    }
    
    public String getNombreCompletoPresentador()
    {
        return nombreCompletoPresentador;
    }
    
    public void setNombreCompletoPresentador(String nombreCompletoPresentador)
    {
        this.nombreCompletoPresentador = nombreCompletoPresentador;
    }
    
    public int getNumBienesInmueblesUrbanos()
    {
        return numBienesInmueblesUrbanos;
    }
    
    public void setNumBienesInmueblesUrbanos(int numBienesInmueblesUrbanos)
    {
        this.numBienesInmueblesUrbanos = numBienesInmueblesUrbanos;
    }
    
    public int getNumBienesInmueblesRusticos()
    {
        return numBienesInmueblesRusticos;
    }
    
    public void setNumBienesInmueblesRusticos(int numBienesInmueblesRusticos)
    {
        this.numBienesInmueblesRusticos = numBienesInmueblesRusticos;
    }
    
    public int getNumBienesInmueblesCaractEsp()
    {
        return numBienesInmueblesCaractEsp;
    }
    
    public void setNumBienesInmueblesCaractEsp(int numBienesInmueblesCaractEsp)
    {
        this.numBienesInmueblesCaractEsp = numBienesInmueblesCaractEsp;
    }
    
    public String getCodProvinciaNotaria()
    {
        return codProvinciaNotaria;
    }
    
    public void setCodProvinciaNotaria(String codProvinciaNotaria)
    {
        this.codProvinciaNotaria = codProvinciaNotaria;
    }
    
    public String getCodPoblacionNotaria()
    {
        return codPoblacionNotaria;
    }
    
    public void setCodPoblacionNotaria(String codPoblacionNotaria)
    {
        this.codPoblacionNotaria = codPoblacionNotaria;
    }
    
    public String getCodNotaria()
    {
        return codNotaria;
    }
    
    public void setCodNotaria(String codNotaria)
    {
        this.codNotaria = codNotaria;
    }
    
    public String getAnnoProtocoloNotarial(){
        return annoProtocoloNotarial;
    }
    
    public void setAnnoProtocoloNotarial(String annoProtocoloNotarial){
        this.annoProtocoloNotarial = annoProtocoloNotarial;
    }
    
    public String getProtocoloNotarial()
    {
        return protocoloNotarial;
    }
    
    public void setProtocoloNotarial(String protocoloNotarial)
    {
        this.protocoloNotarial = protocoloNotarial;
    }

    public ArrayList getListaReferencias()
    {
        return listaReferencias;
    }

    public void setListaReferencias(ArrayList listaReferencias)
    {
        this.listaReferencias = listaReferencias;
    }
    
    public void addListaReferencia(FincaCatastro finca)
    {
        this.listaReferencias.add(finca);
    }

    public void addListaReferencia(BienInmuebleCatastro bienInmueble)
    {
        this.listaReferencias.add(bienInmueble);
    }
    
    public String getTipoDeIntercambio()
    {
        return tipoDeIntercambio;
    }
    
    public void setTipoDeIntercambio(String tipoDeIntercambio)
    {
        this.tipoDeIntercambio = tipoDeIntercambio;
    }
    
    public String getExistenciaInformacionGrafica()
    {
        return existenciaInformacionGrafica;
    }
    
    public void setExistenciaInformacionGrafica(String existenciaInformacionGrafica)
    {
        this.existenciaInformacionGrafica = existenciaInformacionGrafica;
    }
    
    public String getCodigoINEmunicipio()
    {
        return codigoINEmunicipio;
    }
    
    public void setCodigoINEmunicipio(String codigoINEmunicipio)
    {
        this.codigoINEmunicipio = codigoINEmunicipio;
    }
    
    public DireccionLocalizacion getM_Direccion()
    {
        return m_Direccion;
    }
    
    public void setM_Direccion(DireccionLocalizacion m_Direccion)
    {
        this.m_Direccion = m_Direccion;
    }
    
    public boolean isExisteDeclaracionAlteracion()
    {
        return existeDeclaracionAlteracion;
    }
    
    public void setExisteDeclaracionAlteracion(boolean existeDeclaracionAlteracion)
    {
        this.existeDeclaracionAlteracion = existeDeclaracionAlteracion;
    }
    
    public boolean equals(Object o)
    {
        if (!(o instanceof Expediente))
            return false;
        
        if (((Expediente)o).getIdExpediente()== this.idExpediente)
            return true;
        else
            return false;
    }    
    
    public Boolean isTipoTramitaExpSitFinales() {
		return tipoTramitaExpSitFinales;
	}

	public void setTipoTramitaExpSitFinales(Boolean tipoTramitaExpSitFinales) {
		this.tipoTramitaExpSitFinales = tipoTramitaExpSitFinales;
	}
	
	  public boolean isModoAcoplado() {
			return modoAcoplado;
		}

		public void setModoAcoplado(boolean modoAcoplado) {
			this.modoAcoplado = modoAcoplado;
		}
}
