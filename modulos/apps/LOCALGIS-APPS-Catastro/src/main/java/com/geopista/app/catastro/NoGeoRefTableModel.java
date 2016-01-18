/**
 * NoGeoRefTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.inforeferencia.GeopistaDatosImportarPadronConDomicilio;
import com.vividsolutions.jump.I18N;

/**
 * @author rubengomez
 * Modelo de tabla no georreferenciada
 */
public class NoGeoRefTableModel extends DefaultTableModel  {
	private static final long serialVersionUID = 1L;
	private static String[] tableModelNames = new String[] { "Nombre", "Direccion"};
    public static final int ROW_NOMBRE=0;
    public static final int ROW_DIRECCION=1;
    
    public NoGeoRefTableModel(){
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	tableModelNames = new String[] { I18N.get("Georreferenciacion","georeference.model.noGeorefTableModel.nameKey"), I18N.get("Georreferenciacion","georeference.model.noGeorefTableModel.nameAddress")};
    
    }
    private String deleteFirstZeroValues(String stringWithZeroValues){
		if(stringWithZeroValues.length() > 0 ){
			if(stringWithZeroValues.substring(0, 1).equals("0"))
				stringWithZeroValues = deleteFirstZeroValues(stringWithZeroValues.substring(1,stringWithZeroValues.length()));
		}
		return stringWithZeroValues;
	}
    
	public void setModelData(ArrayList<GeopistaDatosImportarPadronConDomicilio> vias) {
		
	    try
	    {
	        if (vias==null)return;
	        Iterator<GeopistaDatosImportarPadronConDomicilio> recorreVias = vias.iterator();
	        while (recorreVias.hasNext()) {
	             Object obj=recorreVias.next();
	             GeopistaDatosImportarPadronConDomicilio via =(GeopistaDatosImportarPadronConDomicilio) obj;
	             Object row[]= new Object[] {via,deleteFirstZeroValues(via.getData().getNombrePseudovia().trim())+" "+deleteFirstZeroValues(via.getData().getNumero().trim())+" "+deleteFirstZeroValues(via.getData().getNumeroSuperior().trim())+" "+deleteFirstZeroValues(via.getData().getPortal().trim())};
	             this.addRow(row);
	        }  
	        fireTableDataChanged();
	    }catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	public int getColumnCount() {
		return tableModelNames.length;
	}

	public String getColumnName(int c) {
		return tableModelNames[c];
	}
    public String setColumnName(int c, String sName) {
        return tableModelNames[c]=sName;
    }

	public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
	}

}    



