/**
 * LicenseMetadataSVG.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.licencias;

import java.util.ArrayList;
import java.util.List;

import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.MetadataSVG;
import com.geopista.ui.dialogs.global.Utils;


public class LicenseMetadataSVG extends MetadataSVG{

	//datos recogidos del SVG
	private String idFeatStr;
	private String licenseFileName;
	private int numFichLic;
	//datos recogidos de BBDD
	private CExpedienteLicencia expLicencia;
	private List<BienBean> inventarios;
	private String refCatastral;

	public LicenseMetadataSVG(String encabezado, String grupo, String path, String numCelda, String idFeatStr, String nombreMetadato, int numFichLic, String attRefCatValue) {
		super(encabezado,grupo,path,numCelda,nombreMetadato);
		this.idFeatStr = idFeatStr;
		this.numFichLic = numFichLic;
		this.refCatastral = attRefCatValue;
		this.expLicencia = null;
		this.inventarios = new ArrayList<BienBean>();
		this.licenseFileName = updateLicenseFileName();
	}

	public String getRefCatastral() {
		return refCatastral;
	}

	public void setRefCatastral(String refCatastral) {
		this.refCatastral = refCatastral;
	}

	public List<BienBean> getInventarios() {
		return inventarios;
	}
	
	public void setInventarios(List<BienBean> inventarios) {
		this.inventarios = inventarios;
	}
	
	public void addInventario(BienBean inventario) {
		this.inventarios.add(inventario);
	}

	public CExpedienteLicencia getExpLicencia() {
		return expLicencia;
	}

	public void setExpLicencia(CExpedienteLicencia expLicencia) {
		this.expLicencia = expLicencia;
	}

	public String getIdFeatStr() {
		return idFeatStr;
	}

	public void setIdFeatStr(String idFeatStr) {
		this.idFeatStr = idFeatStr;
	}
	
	public String getLicenseFileName() {
		return licenseFileName;
	}
	
	/**
	 * Calcula el nombre del fichero para la feature indicada
	 */
	private String updateLicenseFileName() {
//		long idFeat = Long.parseLong(idFeatStr);
//		long fileNumber = idFeat % numFichLic;
//		return numCelda + nombreLicencia + "_meta" + fileNumber + ".svg";
		
		char[] cSeq = null;
		//inventario de patrimonio
		if(Utils.isInArray(Constants.TIPOS_INVENTARIO, nombreMetadato)){
			cSeq = idFeatStr.toCharArray();
		}
		//resto de licencias
		else {
			if(refCatastral!=null){ //si existe pillamos la referencia
				cSeq = refCatastral.toCharArray();
			}
			else { //sino el identificador
				cSeq = idFeatStr.toCharArray();
			}
		}

		long idLong = 0;
		for (int i = 0; i < cSeq.length; i++) {
			idLong += cSeq[i];
		}
		long fileNumber = idLong % numFichLic;
		return numCelda + nombreMetadato + "_meta" + fileNumber + ".svg";
		
//		char[] cSeq = null;
//		if(refCatastral!=null){ //si existe pillamos la referencia
//			cSeq = refCatastral.toCharArray();
//		}
//		else { //sino el identificador
//			cSeq = idFeatStr.toCharArray();
//		}
//		long idLong = 0;
//		for (int i = 0; i < cSeq.length; i++) {
//			idLong += cSeq[i];
//		}
//		long fileNumber = idLong % numFichLic;
//		return numCelda + nombreLicencia + "_meta" + fileNumber + ".svg";
	}
	
}
