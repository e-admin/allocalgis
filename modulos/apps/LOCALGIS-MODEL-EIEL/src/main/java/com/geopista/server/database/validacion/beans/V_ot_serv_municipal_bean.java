/**
 * V_ot_serv_municipal_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_ot_serv_municipal_bean {
   
	  String provincia="-";
	  String municipio="-";
	  String sw_inf_grl="-";
	  String sw_inf_tur="-";
	  String sw_gb_elec="-";
	  String ord_soterr="-";
	  String en_eolica="-";
	  int kw_eolica;
	  String en_solar="-";
	  int kw_solar;	
	  String pl_mareo="-";
	  int kw_mareo;	
	  String ot_energ="-";
	  int kw_energ;
	  String cob_serv_telf_m="-";
	  String tv_dig_calbe="-";
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getSw_inf_grl() {
		return sw_inf_grl;
	}
	public void setSw_inf_grl(String swInfGrl) {
		sw_inf_grl = swInfGrl;
	}
	public String getSw_inf_tur() {
		return sw_inf_tur;
	}
	public void setSw_inf_tur(String swInfTur) {
		sw_inf_tur = swInfTur;
	}
	public String getSw_gb_elec() {
		return sw_gb_elec;
	}
	public void setSw_gb_elec(String swGbElec) {
		sw_gb_elec = swGbElec;
	}
	public String getOrd_soterr() {
		return ord_soterr;
	}
	public void setOrd_soterr(String ordSoterr) {
		ord_soterr = ordSoterr;
	}
	public String getEn_eolica() {
		return en_eolica;
	}
	public void setEn_eolica(String enEolica) {
		en_eolica = enEolica;
	}
	public int getKw_eolica() {
		return kw_eolica;
	}
	public void setKw_eolica(int kwEolica) {
		kw_eolica = kwEolica;
	}
	public String getEn_solar() {
		return en_solar;
	}
	public void setEn_solar(String enSolar) {
		en_solar = enSolar;
	}
	public int getKw_solar() {
		return kw_solar;
	}
	public void setKw_solar(int kwSolar) {
		kw_solar = kwSolar;
	}
	public String getPl_mareo() {
		return pl_mareo;
	}
	public void setPl_mareo(String plMareo) {
		pl_mareo = plMareo;
	}
	public int getKw_mareo() {
		return kw_mareo;
	}
	public void setKw_mareo(int kwMareo) {
		kw_mareo = kwMareo;
	}
	public String getOt_energ() {
		return ot_energ;
	}
	public void setOt_energ(String otEnerg) {
		ot_energ = otEnerg;
	}
	public int getKw_energ() {
		return kw_energ;
	}
	public void setKw_energ(int kwEnerg) {
		kw_energ = kwEnerg;
	}
	public String getCob_serv_telf_m() {
		return cob_serv_telf_m;
	}
	public void setCob_serv_telf_m(String cobServTelfM) {
		cob_serv_telf_m = cobServTelfM;
	}
	public String getTv_dig_calbe() {
		return tv_dig_calbe;
	}
	public void setTv_dig_calbe(String tvDigCalbe) {
		tv_dig_calbe = tvDigCalbe;
	}
	
	
}
