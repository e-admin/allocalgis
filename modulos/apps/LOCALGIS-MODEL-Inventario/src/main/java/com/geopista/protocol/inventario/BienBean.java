/**
 * BienBean.java
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
import java.util.Iterator;
import java.util.Vector;

import com.geopista.protocol.document.Documentable;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 07-sep-2006
 * Time: 15:38:56
 */
public class BienBean implements Serializable, Documentable, Versionable{
	private static final long	serialVersionUID	= 3546643200656945977L;
	
	private boolean hasActiveVersion;
    private long id=-1;
    private String organizacion;
    private String tipo;
    private String numInventario;
    private String nombre;
    private String uso;
    private String descripcion;
    private Date fechaAlta;
    private Date fechaBaja;
    private Date fechaUltimaModificacion;
    private Date fechaAprobacionPleno;
    private boolean borrado= false;
    private boolean eliminado= false;
    private String idMunicipio;
    private Collection idFeatures;
    private Collection idLayers;
    private Collection observaciones;
    private Collection documentos;
    private String bloqueado;
    private CuentaContable cuentaContable;
    private CuentaAmortizacion cuentaAmortizacion;
    private Seguro seguro;
    private Collection mejoras;
    private String superPatron;
    private boolean versionado;  //Flag que nos indica si un bien corresponde a la pestaña de versionado o a la general
	private long revisionActual;
    private long revisionExpirada;
    private boolean patrimonioMunicipalSuelo;
	private Date fechaAdquisicion;
    private String adquisicion;
    private String frutos;
    private Double importeFrutos;
    private String refCatastralOrigen; //referencia catastral que viene en el XML de origen
    private Collection <BienRevertible> bienesRevertibles;
    private String observacionesFechaAdquisicion;
    private String tipoAmortizacion;
    protected boolean patrimonioMunicipal;
    
    private Date fechaVersion;
    private String autor;

	private String coordenadasXY;

	private int tipoGeorreferenciacion=0;

  //  private InventarioEIELBean eiel;	
	
	/** Tipos de Georreferenciacion*/
	public static final int GEO_CATASTRAL=0;
	public static final int GEO_XY=1;
	public static final int GEO_VIA=2;
    
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

	
    
	
	
    public boolean isVersionado() {
		return versionado;
	}

	public void setVersionado(boolean versionado) {
		this.versionado = versionado;
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

    public String getSuperPatron() {
		return superPatron;
	}

	public void setSuperPatron(String superPatron) {
		this.superPatron = superPatron;
	}

	public Collection getMejoras() {
        return mejoras;
    }

    public void setMejoras(Collection mejoras) {
        this.mejoras = mejoras;
    }
    

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }
       
    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CuentaAmortizacion getCuentaAmortizacion() {
        return cuentaAmortizacion;
    }

    public void setCuentaAmortizacion(CuentaAmortizacion cuentaAmortizacion) {
        this.cuentaAmortizacion = cuentaAmortizacion;
    }

    public Collection getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Collection documentos) {
        this.documentos = documentos;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getIdString() {
        return String.valueOf(id);
    }

    public void setIdString(String id) {
       try {
		this.id = Long.parseLong(id);
	} catch (NumberFormatException e) {
		this.id=0;
	}  
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumInventario() {
        return numInventario;
    }

    public void setNumInventario(String numInventario) {
        this.numInventario = numInventario;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public boolean getBorrado() {
        return borrado;
    }
    public boolean isEliminado() {
        return eliminado;
    }
    public boolean getEliminado() {
        return eliminado;
    }
    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public void setBorrado(String borrado) {
        try{
        	this.borrado= borrado.equalsIgnoreCase("1")?true:false;
        	this.eliminado= borrado.equalsIgnoreCase("2")?true:false;        
        }
        catch(Exception e){
        	this.borrado= false;
        	this.eliminado=false;
        	}
    }
    
    


    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
     public Object[] getIdFeatures() {
        return (idFeatures!=null?idFeatures.toArray():null);
    }

    public void setIdFeatures(Collection idFeatures) {
        this.idFeatures = idFeatures;
    }
    
    public void setObjectFeatures(Object[] idFeatures) {
    	Vector features = new Vector();
    	if (idFeatures!=null)
    		for (int i=0;i<idFeatures.length;i++)
    			features.add(idFeatures[i]);
        this.idFeatures = features;
    }

    public void setIdFeatures(Object[] idFeatures) {
          if (idFeatures==null ) return;
          if (this.idFeatures==null) this.idFeatures=new Vector();
          for (int i=0;i<idFeatures.length;i++)
          {
        	  //Verificamos si la feature ya existe. Si es así no la insertamos
        	  //
        	  boolean encontrado=false;
        	  Iterator<Object> it=this.idFeatures.iterator();
				while (it.hasNext()){
					String geopistaFeatureId=(String)it.next();
					if (geopistaFeatureId.equals((String)idFeatures[i]))
						encontrado=true;	
				}
        	  if(!encontrado)
        		  this.idFeatures.add(idFeatures[i]);
          }
      }

    public void addFeature(Object obj)
    {
        if (obj==null) return;
        if (idFeatures==null) idFeatures= new Vector();
        idFeatures.add(obj);
    }
    public Object[] getIdLayers() {
         return (idLayers!=null?idLayers.toArray():null);
    }

    public void setIdLayers(Collection idLayers) {
        this.idLayers = idLayers;
    }

    public void setIdLayers(Object[] idLayers) {
        if (idLayers==null ) return;
        if (this.idLayers==null) this.idLayers=new Vector();
        for (int i=0;i<idLayers.length;i++)
        {
            this.idLayers.add(idLayers[i]);
        }
    }

    public void addLayer(Object obj)
    {
        if (obj==null) return;
        if (idLayers==null) idLayers= new Vector();
        idLayers.add(obj);
    }
    public Collection getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(Collection observaciones) {
        this.observaciones = observaciones;
    }
    public void addObservacion(Observacion observacion) {
    	if (this.observaciones==null) observaciones = new Vector<Observacion>(); 
        observaciones.add(observacion);
    }
    public void addObservaciones(Collection observaciones) {
    	if (this.observaciones==null) this.observaciones = observaciones;
    	else
    		this.observaciones.addAll(observaciones);
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado= bloqueado;
    }
    
    public boolean isPatrimonioMunicipal() {
        return patrimonioMunicipalSuelo;
    }
    public boolean getPatrimonioMunicipalSuelo() {
        return patrimonioMunicipalSuelo;
    }

    public void setPatrimonioMunicipalSuelo(String patrimonioMunicipalSuelo) {
         try{this.patrimonioMunicipalSuelo= patrimonioMunicipalSuelo.equalsIgnoreCase("1")?true:false;}catch(Exception e){this.patrimonioMunicipalSuelo= false;}
    }
    public void setPatrimonioMunicipal(boolean patrimonioMunicipal) {
        this.patrimonioMunicipalSuelo= patrimonioMunicipal;
   }
    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(String adquisicion) {
        this.adquisicion = adquisicion;
    }

	public String getFrutos() {
		return frutos;
	}

	public void setFrutos(String frutos) {
		this.frutos = frutos;
	}

	public Double getImporteFrutos() {
		return importeFrutos;
	}

	public void setImporteFrutos(double importeFrutos) {
		this.importeFrutos = new Double(importeFrutos);
	}

	public void setPatrimonioMunicipalSuelo(boolean patrimonioMunicipalSuelo) {
		this.patrimonioMunicipalSuelo = patrimonioMunicipalSuelo;
	}

	public Collection<BienRevertible> getBienesRevertibles() {
		return bienesRevertibles;
	}

	public void setBienesRevertibles(Collection<BienRevertible> bienesRevertibles) {
		this.bienesRevertibles = bienesRevertibles;
	}
	
    
	public Date getFechaAprobacionPleno() {
		return fechaAprobacionPleno;
	}

	public void setFechaAprobacionPleno(Date fechaAprobacionPleno) {
		this.fechaAprobacionPleno = fechaAprobacionPleno;
	}

	public void clone (BienBean auxBean){
		auxBean.setId(this.id);
		auxBean.setOrganizacion(this.organizacion);
		auxBean.setTipo(this.tipo);
		auxBean.setNumInventario(this.numInventario);
		auxBean.setNombre(this.nombre);
		auxBean.setUso(this.uso);
		auxBean.setDescripcion(this.descripcion);
		auxBean.setFechaAlta(this.fechaAlta);
		auxBean.setFechaAprobacionPleno(this.fechaAprobacionPleno);
		auxBean.setFechaBaja(this.fechaBaja);
		auxBean.setFechaUltimaModificacion(this.fechaUltimaModificacion);
		auxBean.setBorrado(this.borrado);
		auxBean.setIdMunicipio(this.idMunicipio);
		auxBean.setIdFeatures(this.idFeatures);
		auxBean.setIdLayers(this.idLayers);
		auxBean.setObservaciones(this.observaciones);
		auxBean.setDocumentos(this.documentos);
		auxBean.setBloqueado(this.bloqueado);
		auxBean.setCuentaContable(this.cuentaContable);
		auxBean.setCuentaAmortizacion(this.cuentaAmortizacion);
		auxBean.setSeguro(this.seguro);
		auxBean.setMejoras(this.mejoras);
		auxBean.setSuperPatron(this.superPatron);
		auxBean.setVersionado(this.versionado);
		auxBean.setRevisionActual(this.revisionActual);
		auxBean.setRevisionExpirada(this.revisionExpirada);
		auxBean.setPatrimonioMunicipalSuelo(this.patrimonioMunicipalSuelo);
		auxBean.setFechaAdquisicion(this.fechaAdquisicion);
		auxBean.setAdquisicion(this.adquisicion);
		auxBean.setFrutos(this.frutos);
		if (this.importeFrutos!=null)
			auxBean.setImporteFrutos(this.importeFrutos);
		auxBean.setBienesRevertibles(this.bienesRevertibles);
		auxBean.setFechaVersion(this.fechaVersion);
		auxBean.setAutor(this.autor);
	}

	public String getRefCatastralOrigen() {
		return refCatastralOrigen;
	}

	public void setRefCatastralOrigen(String refCatastralOrigen) {
		this.refCatastralOrigen = refCatastralOrigen;
	}

	public void setTipoGeorreferenciacion(int tipoGeorreferenciacion){
		this.tipoGeorreferenciacion=tipoGeorreferenciacion;
	}
	public int getTipoGeorreferenciacion(){
		return tipoGeorreferenciacion;
	}
	
	public String getCoordenadasXY(){
		return coordenadasXY;
	}
	
	public void setCoordenadasXY(String coordenadasXY) {
		this.coordenadasXY = coordenadasXY;
	}

	public void setImporteFrutos(Double importeFrutos) {
		this.importeFrutos = importeFrutos;
	}
	
	public void setActiveVersion(boolean hasActiveVersion) {
		this.hasActiveVersion = hasActiveVersion;
	}
	public boolean hasActiveVersion() {
		return hasActiveVersion;
	}
	
	public void setObservacionesFechaAdquisicion(
			String observacionesFechaAdquisicion) {
		this.observacionesFechaAdquisicion = observacionesFechaAdquisicion;
	}
	public String getObservacionesFechaAdquisicion() {
		return observacionesFechaAdquisicion;
	}
	
	public void setTipoAmortizacion(String tipoAmortizacion) {
		this.tipoAmortizacion = tipoAmortizacion;
	}
	public String getTipoAmortizacion() {
		return tipoAmortizacion;
	}
	
    public boolean isPMS(){
    	return patrimonioMunicipal;
    }
}
