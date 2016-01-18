/**
 * ElemCementerioBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 * elem_cementerio
 */
public class ElemCementerioBean implements Serializable{

	private static final long	serialVersionUID	= 3546643200656945977L;
	
	private long id = -1;
	private String idMunicipio;
	private String tipo;
	private String nombreCementerio;
	private String entidad;
	
	private String superPatron;
	private String patron;
	
	private Collection documentos;
	
	private Collection idFeatures;
	private Collection idLayers;
	
	private int idCementerio;
	

	public int getIdCementerio() {
		return idCementerio;
	}
	public void setIdCementerio(int idCementerio) {
		this.idCementerio = idCementerio;
	}

	private String bloqueado;
    private boolean borrado= false;
    
	
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombreCementerio() {
		return nombreCementerio;
	}
	public void setNombreCementerio(String nombreCementerio) {
		this.nombreCementerio = nombreCementerio;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Collection getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Collection documentos) {
		this.documentos = documentos;
	}

	public String getSuperPatron() {
		return superPatron;
	}
	public void setSuperPatron(String superPatron) {
		this.superPatron = superPatron;
	}

	public String getPatron() {
		return patron;
	}
	public void setPatron(String patron) {
		this.patron = patron;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

    public boolean isBorrado() {
        return borrado;
    }

    public boolean getBorrado() {
        return borrado;
    }
    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public void setBorrado(String borrado) {
        try{this.borrado= borrado.equalsIgnoreCase("1")?true:false;}
        catch(Exception e){this.borrado= false;}
    }

    public Object[] getIdFeatures() {
        return (idFeatures!=null?idFeatures.toArray():null);
    }

    public void setIdFeatures(Collection idFeatures) {
        this.idFeatures = idFeatures;
    }

    public void setIdFeatures(Object[] idFeatures) {
          if (idFeatures==null ) return;
          if (this.idFeatures==null) this.idFeatures=new Vector();
          for (int i=0;i<idFeatures.length;i++)
          {
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

	
}
