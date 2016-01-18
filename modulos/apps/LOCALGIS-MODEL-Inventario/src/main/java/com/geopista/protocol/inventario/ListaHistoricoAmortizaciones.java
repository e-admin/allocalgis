/**
 * ListaHistoricoAmortizaciones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;


public class ListaHistoricoAmortizaciones implements Serializable{
	
    private Hashtable<Long, HistoricoAmortizacionesBean> hHistAmor;
    private int totalAmortizadosInmueblesUrbanos=0;
    private int totalInmueblesUrbanos=0;
    private int totalAmortizadosInmueblesRusticos=0;
    private int totalInmueblesRusticos=0;
    private int totalAmortizadosMuebles=0;
    private int totalMuebles=0;
    private int totalAmortizadosVehiculos=0;
    private int totalVehiculos=0;
    
    public  ListaHistoricoAmortizaciones()
    {
          this.hHistAmor = new Hashtable<Long, HistoricoAmortizacionesBean>();
    }
    public void add(HistoricoAmortizacionesBean  historicoamortizacionBean) {
        this.hHistAmor.put(historicoamortizacionBean.getIdBien()+historicoamortizacionBean.getAnio(), historicoamortizacionBean);
        if(historicoamortizacionBean.getTipo()!=null){
	        if(historicoamortizacionBean.getTipo().equals(Const.INMUEBLES_URBANOS))
	        	totalAmortizadosInmueblesUrbanos++;
	        else if (historicoamortizacionBean.getTipo().equals(Const.INMUEBLES_RUSTICOS))
	        	totalAmortizadosInmueblesRusticos++;
	        else if (historicoamortizacionBean.getTipo().equals(Const.MUEBLES))
	        	totalAmortizadosMuebles++;
	        else if (historicoamortizacionBean.getTipo().equals(Const.VEHICULOS))
	        	totalAmortizadosVehiculos++;
        }
    }

    public HistoricoAmortizacionesBean get(String historicoNumInventario)
    {
        return (HistoricoAmortizacionesBean)this.hHistAmor.get(historicoNumInventario);
    }
    public void remove(HistoricoAmortizacionesBean historicoNumInventario)
    {
        this.hHistAmor.remove(historicoNumInventario);
    }

    public Hashtable<Long, HistoricoAmortizacionesBean> getHistoricoAmortizaciones() {
        return hHistAmor;
    }

    public void sethHistoricoAmortizaciones(Hashtable<Long, HistoricoAmortizacionesBean> hHistAmor) {
        this.hHistAmor = hHistAmor;
    }
	public void addAll(Collection<HistoricoAmortizacionesBean> listaBienesAmort) {
		for (HistoricoAmortizacionesBean historicoAmortizacionesBean : listaBienesAmort) {
			add(historicoAmortizacionesBean);
		}
		
	}
	public int getTotalAmortizadosInmueblesUrbanos() {
		return totalAmortizadosInmueblesUrbanos;
	}
	public void setTotalAmortizadosInmueblesUrbanos(
			int totalAmortizadosInmueblesUrbanos) {
		this.totalAmortizadosInmueblesUrbanos = totalAmortizadosInmueblesUrbanos;
	}
	public int getTotalInmueblesUrbanos() {
		return totalInmueblesUrbanos;
	}
	public void setTotalInmueblesUrbanos(int totalInmueblesUrbanos) {
		this.totalInmueblesUrbanos = totalInmueblesUrbanos;
	}
	public int getTotalAmortizadosInmueblesRusticos() {
		return totalAmortizadosInmueblesRusticos;
	}
	public void setTotalAmortizadosInmueblesRusticos(
			int totalAmortizadosInmueblesRusticos) {
		this.totalAmortizadosInmueblesRusticos = totalAmortizadosInmueblesRusticos;
	}
	public int getTotalInmueblesRusticos() {
		return totalInmueblesRusticos;
	}
	public void setTotalInmueblesRusticos(int totalInmueblesRusticos) {
		this.totalInmueblesRusticos = totalInmueblesRusticos;
	}
	public int getTotalAmortizadosMuebles() {
		return totalAmortizadosMuebles;
	}
	public void setTotalAmortizadosMuebles(int totalAmortizadosMuebles) {
		this.totalAmortizadosMuebles = totalAmortizadosMuebles;
	}
	public int getTotalMuebles() {
		return totalMuebles;
	}
	public void setTotalMuebles(int totalMuebles) {
		this.totalMuebles = totalMuebles;
	}
	public int getTotalAmortizadosVehiculos() {
		return totalAmortizadosVehiculos;
	}
	public void setTotalAmortizadosVehiculos(int totalAmortizadosVehiculos) {
		this.totalAmortizadosVehiculos = totalAmortizadosVehiculos;
	}
	public int getTotalVehiculos() {
		return totalVehiculos;
	}
	public void setTotalVehiculos(int totalVehiculos) {
		this.totalVehiculos = totalVehiculos;
	}
}

