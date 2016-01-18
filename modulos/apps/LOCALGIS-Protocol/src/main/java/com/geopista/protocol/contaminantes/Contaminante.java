/**
 * Contaminante.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.contaminantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-oct-2004
 * Time: 13:19:41
 */
public class Contaminante implements Cloneable {
    String id;
    String id_tipo;
    String id_razon;
    String numeroAdm;
    String asunto;
    Date fInicio;
    Date fFin;
    String tipovia;
    String nombrevia;
    String numerovia;
    String cpostal;
	String idMunicipio;
    Vector inspecciones;
    Vector infractores;



    //Extensiï¿½n para impresion
   private ListaEstructuras estructuraTipoRazon;
   private ListaEstructuras estructuraTipoContaminacion;
   private String locale;


	public Contaminante() {
	}

	public Contaminante(String id, String id_tipo, String id_razon, String numeroAdm, String asunto, Date fInicio, Date fFin, String tipovia, String nombrevia, String numerovia, String cpostal,String idMunicipio) {
		this.id = id;
		this.id_tipo = id_tipo;
		this.id_razon = id_razon;
		this.numeroAdm = numeroAdm;
		this.asunto = asunto;
		this.fInicio = fInicio;
		this.fFin = fFin;
		this.tipovia = tipovia;
		this.nombrevia = nombrevia;
		this.numerovia = numerovia;
		this.cpostal = cpostal;
		this.idMunicipio = idMunicipio;
	}

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCpostal() {
        return cpostal;
    }

    public void setCpostal(String cpostal) {
        this.cpostal = cpostal;
    }

    public Date getfFin() {
        return fFin;
    }

    public void setfFin(Date fFin) {
        this.fFin = fFin;
    }

    public Date getfInicio() {
        return fInicio;
    }

    public void setfInicio(Date fInicio) {
        this.fInicio = fInicio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_razon() {
        return id_razon;
    }

    public void setId_razon(String id_razon) {
        this.id_razon = id_razon;
    }

    public String getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(String id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNombrevia() {
        return nombrevia;
    }

    public void setNombrevia(String nombrevia) {
        this.nombrevia = nombrevia;
    }

    public String getNumerovia() {
        return numerovia;
    }

    public void setNumerovia(String numerovia) {
        this.numerovia = numerovia;
    }

    public String getNumeroAdm() {
        return numeroAdm;
    }

    public void setNumeroAdm(String numeroAdm) {
        this.numeroAdm = numeroAdm;
    }

 
    public void addInspeccion(Inspeccion inspeccion)
    {
        if (inspecciones==null) inspecciones= new Vector();
        if (inspeccion==null) return;
        inspecciones.add(inspeccion);
    }

    public Vector getInspecciones() {
        return inspecciones;
    }

    public void setInspecciones(Vector inspecciones) {
        this.inspecciones = inspecciones;
    }

    public String getTipovia() {
        return tipovia;
    }

    public void setTipovia(String tipovia) {
        this.tipovia = tipovia;
    }

    public Object clone()
      {
             Contaminante obj=null;
             try{
                    obj=(Contaminante)super.clone();
              }catch(CloneNotSupportedException ex){
              }

              obj.setAsunto(this.getAsunto());
              obj.setCpostal(this.getCpostal());
              obj.setfFin(this.getfFin());
              obj.setfInicio(this.getfInicio());
              obj.setId(this.getId());
              obj.setId_razon(this.getId_razon());
              obj.setId_tipo(this.getId_tipo());
              obj.setNombrevia(this.getNombrevia());
              obj.setNumeroAdm(this.getNumeroAdm());
              obj.setNumerovia(this.getNumerovia());
              obj.setTipovia(this.getTipovia());
              if (this.getInspecciones()!=null)
              {
                 //obj.setInspecciones((Vector)this.getInspecciones().clone());
                  Vector aux= new Vector();
                  for (Enumeration e=obj.getInspecciones().elements();e.hasMoreElements();)
                  {
                      Inspeccion inspeccion=(Inspeccion)e.nextElement();
                      Inspeccion auxIns=new Inspeccion();
                      auxIns.copy(inspeccion);
                      aux.add(auxIns);
                  }
                  obj.setInspecciones(aux);
              }
              if (this.getInfractores()!=null)
              {
                  Vector aux= new Vector();
                  for (Enumeration e=obj.getInfractores().elements();e.hasMoreElements();)
                  {
                      CPersonaJuridicoFisica infractor=(CPersonaJuridicoFisica)e.nextElement();
                      CPersonaJuridicoFisica auxIns=new CPersonaJuridicoFisica();
                      auxIns.copy(infractor);
                      aux.add(auxIns);
                  }
                  obj.setInfractores(aux);
              }

              return obj;
      }


	public String getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
    public String getFechaFin() {
        if (fFin == null) return null;
        return new SimpleDateFormat("dd-MM-yyyy").format(fFin);

    }
    public String getFechaInicio() {
        if (fInicio == null) return null;
        return new SimpleDateFormat("dd-MM-yyyy").format(fInicio);
    }
    public String getTipoContaminacion()
    {
       if (estructuraTipoContaminacion==null) return null;
      return (estructuraTipoContaminacion.getDomainNode(id_tipo).getTerm(locale));
    }
    public String getTipoRazon()
    {
       if (estructuraTipoRazon==null) return null;
       return (estructuraTipoRazon.getDomainNode(id_razon).getTerm(locale));
    }

    public void setEstructuraTipoRazon(ListaEstructuras estructuraTipoRazon )
    {
       this.estructuraTipoRazon=estructuraTipoRazon;
    }
    public void setEstructuraTipoContaminacion(ListaEstructuras estructuraTipoContaminacion )
    {
       this.estructuraTipoContaminacion=estructuraTipoContaminacion;
    }
    public void setLocale(String locale)
    {
        this.locale=locale;
    }

    public Vector getInfractores() {
        return infractores;
    }

    public void setInfractores(Vector infractores) {
        this.infractores = infractores;
    }
    public CPersonaJuridicoFisica getInfractor(long idPersona)
    {
        if (idPersona==0)return null;
        for (Enumeration e=infractores.elements();e.hasMoreElements();)
        {
            CPersonaJuridicoFisica persona= (CPersonaJuridicoFisica)e.nextElement();
            if (idPersona==persona.getIdPersona()) return persona;
        }
        return null;
    }
    public void addInfractor(CPersonaJuridicoFisica infractor)
    {
        if (infractores==null) infractores= new Vector();
        Vector auxVector= new Vector();

        for (Enumeration e= infractores.elements();e.hasMoreElements();)
        {
             CPersonaJuridicoFisica auxInfractor=(CPersonaJuridicoFisica)e.nextElement();
             if (!auxInfractor.getDniCif().equalsIgnoreCase(infractor.getDniCif()))
                auxVector.add(auxInfractor);
        }
        auxVector.add(infractor);
        setInfractores(auxVector);
    }
    public void deleteInfractor(CPersonaJuridicoFisica infractor)
    {
        if (infractores==null) infractores= new Vector();
        Vector auxVector= new Vector();
        for (Enumeration e= infractores.elements();e.hasMoreElements();)
        {
             CPersonaJuridicoFisica auxInfractor=(CPersonaJuridicoFisica)e.nextElement();
             if (!auxInfractor.getDniCif().equalsIgnoreCase(infractor.getDniCif()))
                auxVector.add(auxInfractor);
        }
        setInfractores(auxVector);
    }


}
