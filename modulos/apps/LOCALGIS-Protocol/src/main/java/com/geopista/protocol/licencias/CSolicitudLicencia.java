/**
 * CSolicitudLicencia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.protocol.licencias.actividad.DatosActividad;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.geopista.protocol.ocupacion.CDatosOcupacion;

/**
 * @author SATEC
 * @version $Revision: 1.5 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/11/15 15:32:28 $
 *          $Name:  $
 *          $RCSfile: CSolicitudLicencia.java,v $
 *          $Revision: 1.5 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CSolicitudLicencia implements java.io.Serializable {
	
	private Logger logger = Logger.getLogger(CSolicitudLicencia.class);

	private long idSolicitud;
	private CTipoLicencia tipoLicencia;
	private CTipoObra tipoObra;
	private CPersonaJuridicoFisica propietario;
	private CPersonaJuridicoFisica representante;
    private CPersonaJuridicoFisica promotor;
	private String numAdministrativo;
	private String codigoEntrada;
	private String unidadTramitadora;
	private String unidadDeRegistro;
	private String motivo;
	private String nombreComercial;
	private String asunto;
	private Date fecha;
	private Date fechaEntrada;
    private Date fechaLimiteObra;
	private double tasa;
	private String tipoViaAfecta;
	private String nombreViaAfecta;
	private String numeroViaAfecta;
	private String portalAfecta;
	private String plantaAfecta;
	private String letraAfecta;
	private String cpostalAfecta;
	private String municipioAfecta;
	private String provinciaAfecta;
	private String localidadAfecta;
	private String observaciones;
	private Vector anexos;
    private Vector documentacionEntregada;
	private Vector referenciasCatastrales;
	private CDatosOcupacion datosOcupacion;
	private String idMunicipio;
    private String observacionesDocumentacionEntregada;
    private double impuesto;
    private Date fechaResolucion;
    private Vector tecnicos;
    private Vector mejoras;
    private DatosActividad datosActividad;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /** guarda en la posicion 0, el id_representante, en la posicion 1, el id_tecnico y en
     * la posicion 2 el id_promotor que se van a borrar
     */
    //private long [] personas= new long[3];
    private long [] personas= new long[]{-1, -1, -1};

	public CSolicitudLicencia() {
	
	}


	public CSolicitudLicencia(CTipoLicencia tipoLicencia, CTipoObra tipoObra, CPersonaJuridicoFisica propietario, CPersonaJuridicoFisica representante, CPersonaJuridicoFisica tecnico, CPersonaJuridicoFisica promotor, String numAdministrativo, String codigoEntrada, String unidadTramitadora, String unidadDeRegistro, String motivo, String asunto, Date fecha, double tasa, String tipoViaAfecta, String nombreViaAfecta, String numeroViaAfecta, String portalAfecta, String plantaAfecta, String letraAfecta, String cpostalAfecta, String municipioAfecta, String provinciaAfecta, String observaciones, Vector anexos, Vector referenciasCatastrales) {
		this.tipoLicencia = tipoLicencia;
		this.tipoObra = tipoObra;
		this.propietario = propietario;
		this.representante = representante;
       	ponTecnico(tecnico);
		this.promotor = promotor;
		this.numAdministrativo = numAdministrativo;
		this.codigoEntrada = codigoEntrada;
		this.unidadTramitadora = unidadTramitadora;
		this.unidadDeRegistro = unidadDeRegistro;
		this.motivo = motivo;
		this.asunto = asunto;
		this.fecha = fecha;
		this.tasa = tasa;
		this.tipoViaAfecta = tipoViaAfecta;
		this.nombreViaAfecta = nombreViaAfecta;
		this.numeroViaAfecta = numeroViaAfecta;
		this.portalAfecta = portalAfecta;
		this.plantaAfecta = plantaAfecta;
		this.letraAfecta = letraAfecta;
		this.cpostalAfecta = cpostalAfecta;
		this.municipioAfecta = municipioAfecta;
		this.provinciaAfecta = provinciaAfecta;
		this.observaciones = observaciones;
		this.anexos = anexos;
		this.referenciasCatastrales = referenciasCatastrales;
	}

	public CSolicitudLicencia(CTipoLicencia tipoLicencia, CTipoObra tipoObra, CPersonaJuridicoFisica propietario, CPersonaJuridicoFisica representante, CPersonaJuridicoFisica tecnico, CPersonaJuridicoFisica promotor, String numAdministrativo, String codigoEntrada, String unidadTramitadora, String unidadDeRegistro, String motivo, String asunto, Date fecha, double tasa, String tipoViaAfecta, String nombreViaAfecta, String numeroViaAfecta, String portalAfecta, String plantaAfecta, String letraAfecta, String cpostalAfecta, String municipioAfecta, String provinciaAfecta, String localidadAfecta,String observaciones, Vector anexos, Vector referenciasCatastrales) {
		this.tipoLicencia = tipoLicencia;
		this.tipoObra = tipoObra;
		this.propietario = propietario;
		this.representante = representante;
       	ponTecnico(tecnico);
		this.promotor = promotor;
		this.numAdministrativo = numAdministrativo;
		this.codigoEntrada = codigoEntrada;
		this.unidadTramitadora = unidadTramitadora;
		this.unidadDeRegistro = unidadDeRegistro;
		this.motivo = motivo;
		this.asunto = asunto;
		this.fecha = fecha;
		this.tasa = tasa;
		this.tipoViaAfecta = tipoViaAfecta;
		this.nombreViaAfecta = nombreViaAfecta;
		this.numeroViaAfecta = numeroViaAfecta;
		this.portalAfecta = portalAfecta;
		this.plantaAfecta = plantaAfecta;
		this.letraAfecta = letraAfecta;
		this.cpostalAfecta = cpostalAfecta;
		this.municipioAfecta = municipioAfecta;
		this.provinciaAfecta = provinciaAfecta;
		this.localidadAfecta = localidadAfecta;
		this.observaciones = observaciones;
		this.anexos = anexos;
		this.referenciasCatastrales = referenciasCatastrales;
	}


	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

    public Date getFechaLimiteObra() {
        return fechaLimiteObra;
    }

    public void setFechaLimiteObra(Date fechaLimite) {
        this.fechaLimiteObra= fechaLimite;
    }
    
	public void setFechaLimiteObraString(String str){
		try {
			if(str!=null && str.length()>0){
				fechaLimiteObra = sdf.parse(str);
			}
			else {
				fechaLimiteObra = null;
			}
		} catch (Exception e) {
			logger.error("Formato de fecha incorrecto: " + str);
		}
	}
    
	public String getFechaLimiteObraString(){
		if(fechaLimiteObra==null){return null;}
		return sdf.format(fechaLimiteObra);
	}

	public Vector getReferenciasCatastrales() {
		return referenciasCatastrales;
	}

	public void setReferenciasCatastrales(Vector referenciasCatastrales) {
		this.referenciasCatastrales = referenciasCatastrales;
	}

	public long getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public CTipoLicencia getTipoLicencia() {
		return tipoLicencia;
	}

	public void setTipoLicencia(CTipoLicencia tipoLicencia) {
		this.tipoLicencia = tipoLicencia;
	}

	public CTipoObra getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(CTipoObra tipoObra) {
		this.tipoObra = tipoObra;
	}

	public CPersonaJuridicoFisica getPropietario() {
		return propietario;
	}

	public void setPropietario(CPersonaJuridicoFisica propietario) {
		this.propietario = propietario;
	}

	public CPersonaJuridicoFisica getRepresentante() {
		return representante;
	}

	public void setRepresentante(CPersonaJuridicoFisica representante) {
		this.representante = representante;
	}

    public CPersonaJuridicoFisica dameTecnico()
    {
        if (tecnicos==null || tecnicos.size()==0) return null;
		return (CPersonaJuridicoFisica)tecnicos.elementAt(0);
	}
	public void ponTecnico(CPersonaJuridicoFisica tecnico) {
        if (tecnico==null) return;
        if (tecnicos==null) tecnicos= new Vector();
		this.tecnicos.add(tecnico);
	}

	public CPersonaJuridicoFisica getPromotor() {
		return promotor;
	}

	public void setPromotor(CPersonaJuridicoFisica promotor) {
		this.promotor = promotor;
	}

	public String getNumAdministrativo() {
		return numAdministrativo;
	}

	public void setNumAdministrativo(String numAdministrativo) {
		this.numAdministrativo = numAdministrativo;
	}

	public String getCodigoEntrada() {
		return codigoEntrada;
	}

	public void setCodigoEntrada(String codigoEntrada) {
		this.codigoEntrada = codigoEntrada;
	}

	public String getUnidadTramitadora() {
		return unidadTramitadora;
	}

	public void setUnidadTramitadora(String unidadTramitadora) {
		this.unidadTramitadora = unidadTramitadora;
	}

	public String getUnidadDeRegistro() {
		return unidadDeRegistro;
	}

	public void setUnidadDeRegistro(String unidadDeRegistro) {
		this.unidadDeRegistro = unidadDeRegistro;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getFechaString(){
		if(fecha==null){return null;}
		return sdf.format(fecha);
	}
	
	public void setFechaString(String str){
		try {
			if(str!=null && str.length()>0){
				fecha = sdf.parse(str);
			}
			else {
				fecha = null;
			}
		} catch (Exception e) {
			logger.error("Formato de fecha incorrecto: " + str);
		}
	}

	public double getTasa() {
		return tasa;
	}

	public void setTasa(double tasa) {
		this.tasa = tasa;
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

	public String getPortalAfecta() {
		return portalAfecta;
	}

	public void setPortalAfecta(String portalAfecta) {
		this.portalAfecta = portalAfecta;
	}

	public String getPlantaAfecta() {
		return plantaAfecta;
	}

	public void setPlantaAfecta(String plantaAfecta) {
		this.plantaAfecta = plantaAfecta;
	}

	public String getLetraAfecta() {
		return letraAfecta;
	}

	public void setLetraAfecta(String letraAfecta) {
		this.letraAfecta = letraAfecta;
	}

	public String getCpostalAfecta() {
		return cpostalAfecta;
	}

	public void setCpostalAfecta(String cpostalAfecta) {
		this.cpostalAfecta = cpostalAfecta;
	}

	public String getMunicipioAfecta() {
		return municipioAfecta;
	}

	public void setMunicipioAfecta(String municipioAfecta) {
		this.municipioAfecta = municipioAfecta;
	}

	public String getProvinciaAfecta() {
		return provinciaAfecta;
	}

	public void setProvinciaAfecta(String provinciaAfecta) {
		this.provinciaAfecta = provinciaAfecta;
	}

	
	public String getLocalidadAfecta() {
		return localidadAfecta;
	}

	public void setLocalidadAfecta(String localidadAfecta) {
		this.localidadAfecta = localidadAfecta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Vector getAnexos() {
		return anexos;
	}

	public void setAnexos(Vector anexos) {
		this.anexos = anexos;
	}

    public Vector getDocumentacionEntregada() {
        return documentacionEntregada;
    }

    public void setDocumentacionEntregada(Vector documentacion) {
        this.documentacionEntregada= documentacion;
    }

    public long getIdRepresentanteToDelete() {
        return personas[0];
    }

    public void setIdRepresentanteToDelete(long id) {
        this.personas[0]= id;
    }

    public long getIdTecnicoToDelete() {
        return personas[1];
    }

    public void setIdTecnicoToDelete(long id) {
        this.personas[1]= id;
    }

    public long getIdPromotorToDelete() {
        return personas[2];
    }

    public void setIdPromotorToDelete(long id) {
        this.personas[2]= id;
    }

	public CDatosOcupacion getDatosOcupacion() {
		return datosOcupacion;
	}

	public void setDatosOcupacion(CDatosOcupacion datosOcupacion) {
		this.datosOcupacion = datosOcupacion;
	}

	public String getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

    public String getObservacionesDocumentacionEntregada() {
        return observacionesDocumentacionEntregada;
    }

    public void setObservacionesDocumentacionEntregada(String observaciones) {
        this.observacionesDocumentacionEntregada = observaciones;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto= impuesto;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion= fechaResolucion;
    }

    public void setTecnicos(Vector v){
        this.tecnicos= v;
    }

    public Vector getTecnicos(){
        return tecnicos;
    }

    public void setMejoras(Vector v){
        this.mejoras= v;
    }

    public Vector getMejoras(){
        return mejoras;
    }

    public void setDatosActividad(DatosActividad datos){
        this.datosActividad= datos;
    }

    public DatosActividad getDatosActividad(){
        return datosActividad;
    }
    
	public List<String> getRefCatastralesString() {
		if(referenciasCatastrales==null){ return null;}
		List<String> list = new ArrayList<String>();
		CReferenciaCatastral refCat = null;
		for (Iterator iterator = referenciasCatastrales.iterator(); iterator.hasNext();) {
			refCat = (CReferenciaCatastral) iterator.next();
			list.add(refCat.getReferenciaCatastral());
		}
		return list;
	}
	
	public void setRefCatastralesString(List<String> strList) {
		if(strList==null || strList.size()==0){ return ;}
		if(referenciasCatastrales==null){referenciasCatastrales=new Vector();}
		String str = null;
		boolean bRefRepetida = false;
		CReferenciaCatastral ref = null;
		for (int i = 0; i < strList.size(); i++) {
			str = strList.get(i);
			//comprobamos que no exista
			for (int j = 0; j < referenciasCatastrales.size() && bRefRepetida==false; j++) {
				ref = (CReferenciaCatastral) referenciasCatastrales.get(j);
				if(ref.getReferenciaCatastral().equalsIgnoreCase(str)){
					bRefRepetida = true;
				}
			}
			//simplemente se aï¿½ade la referencia catastral
			if(bRefRepetida==false){
				referenciasCatastrales.add(new CReferenciaCatastral(str, "", "", "", "", "", "", "" ,""));
			}
			bRefRepetida = false;
		}
	}

	public String getDNITitularString(){
		if(propietario==null) return null;
		return propietario.getDniCif();
	}
	
	public void setDNITitularString(String str){
		if(propietario==null) return;
		propietario.setDniCif(str);
	}
	
	public String getNombreTitularString(){
		if(propietario==null) return null;
		return propietario.getNombre();
	}
	
	public void setNombreTitularString(String str){
		if(propietario==null) return;
		propietario.setNombre(str);
	}
	
	public String getApellido1TitularString(){
		if(propietario==null) return null;
		return propietario.getApellido1();
	}
	
	public void setApellido1TitularString(String str){
		if(propietario==null) return;
		propietario.setApellido1(str);
	}
	
	public String getApellido2TitularString(){
		if(propietario==null) return null;
		return propietario.getApellido2();
	}
	
	public void setApellido2TitularString(String str){
		if(propietario==null) return;
		propietario.setApellido2(str);
	}
	
}