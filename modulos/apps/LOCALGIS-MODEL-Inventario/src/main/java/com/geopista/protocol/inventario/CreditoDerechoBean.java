/**
 * CreditoDerechoBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 22-sep-2006
 * Time: 9:39:29
 * To change this template use File | Settings | File Templates.
 */
public class CreditoDerechoBean extends BienBean implements Serializable{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
     private String concepto;
     private String deudor;
     private String caracteristicas;
     private String destino;
     private String clase;
     private String subClase;
	private Double importe;
     private Date fechaVencimiento;
     private String conceptoDesc;
     
     private boolean arrendamiento=false;
     
     public CreditoDerechoBean(){
    	 super();
    	 setTipo(Const.PATRON_CREDITOS_DERECHOS_PERSONALES);
     }

	public String getConceptoDesc() {
        return conceptoDesc;
    }

    public void setConceptoDesc(String conceptoDesc) {
        this.conceptoDesc = conceptoDesc;
    }

    public String getSubClase() {
		return subClase;
	}

	public void setSubClase(String subClase) {
		this.subClase = subClase;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}
    
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getDeudor() {
        return deudor;
    }

    public void setDeudor(String deudor) {
        this.deudor = deudor;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = new Double(importe);
    }

	public boolean isArrendamiento() {
		return arrendamiento;
	}

	public void setArrendamiento(boolean arrendamiento) {
		this.arrendamiento = arrendamiento;
	}

   

}
