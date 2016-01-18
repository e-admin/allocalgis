/**
 * LocalgisLayerManagerWs.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.localgissigm;


public class LocalgisLayerManagerWs {

	public LocalgisLayerManagerWs(){}
	
	public LocalgisLayerManagerWs(String url, String user, String password){
		
	}
	
	public boolean createTable(String tableName){
//		TablesDBOperations tableOp = new TablesDBOperations();
//        
//        Table newTable = new Table();
//        newTable.setName(tableName);
//        newTable.setDescription(tableName);
//        newTable.setGeometryType(11); //OBTENR PROPERTIES O XML
//
//        if (!tableOp.existeTabla(newTable.getDescription())){
//        	if(tableOp.crearTablaBD (newTable))
//            {        
//                ColumnRow idColRow  = new ColumnRow();
//                ColumnDB idDB = new ColumnDB();
//                idDB.setName("id");
//                idDB.setLength(8);
//                idDB.setType(TablesDBPanel.COL_NUMERIC);
//                idDB.setDefaultValue("");
//                idDB.setDescription("");
//                idDB.setNotNull(true);
//                idDB.setUnique(true);
//                idColRow.setColumnaBD(idDB);
//                Column idColSis = new Column("id", "id", null);
//                idColRow.setColumnaSistema(idColSis);
//                tableOp.crearColumnaBD(newTable, idColRow);
//               
//                ColumnRow idMunicipioColRow  = new ColumnRow();
//                ColumnDB idMunicipioDB = new ColumnDB();
//                idMunicipioDB.setName("id_municipio");
//                idMunicipioDB.setLength(5);
//                idMunicipioDB.setType(TablesDBPanel.COL_NUMERIC);
//                idMunicipioDB.setDefaultValue("");
//                idMunicipioDB.setNotNull(true);
//                idMunicipioDB.setDescription("");
//                idMunicipioColRow.setColumnaBD(idMunicipioDB);
//                Column idMunicipioColSis = new Column("id_municipio", "id_municipio", null);
//                idMunicipioColRow.setColumnaSistema(idMunicipioColSis);
//                tableOp.crearColumnaBD(newTable, idMunicipioColRow);
//                
//                if (!(newTable.getGeometryType()<0))
//                {
//                    ColumnRow idGeometryColRow  = new ColumnRow();
//                    ColumnDB idGeometryDB = new ColumnDB();
//                    idGeometryDB.setName("GEOMETRY");
//                    idGeometryDB.setType(TablesDBPanel.COL_GEOMETRY);
//                    idGeometryDB.setDefaultValue("");
//                    idGeometryDB.setDescription("");
//                    idGeometryColRow.setColumnaBD(idGeometryDB);
//                    Column idGeometryColSis = new Column("GEOMETRY", "GEOMETRY", null);
//                    idGeometryColRow.setColumnaSistema(idGeometryColSis);
//                    tableOp.crearColumnaBD(newTable, idGeometryColRow);
//                }      
//                
//                ColumnRow idFeatureColRow  = new ColumnRow();
//                ColumnDB idFeatureDB = new ColumnDB();
//                idFeatureDB.setName("id_feature");
//                idFeatureDB.setLength(100);
//                idFeatureDB.setType(TablesDBPanel.COL_VARCHAR);
//                idFeatureDB.setDefaultValue("");
//                idFeatureDB.setNotNull(true);
//                idFeatureDB.setDescription("");
//                idFeatureColRow.setColumnaBD(idFeatureDB);
//                Column idFeatureColSis = new Column("id_feature", "id_feature", null);
//                idMunicipioColRow.setColumnaSistema(idFeatureColSis);
//                tableOp.crearColumnaBD(newTable, idFeatureColRow);
//             
//                ColumnRow styleTypeColRow  = new ColumnRow();
//                ColumnDB styleTypeDB = new ColumnDB();
//                styleTypeDB.setName("style_type");
//                styleTypeDB.setLength(100);
//                styleTypeDB.setType(TablesDBPanel.COL_VARCHAR);
//                styleTypeDB.setDefaultValue("");
//                styleTypeDB.setNotNull(false);
//                styleTypeDB.setDescription("");
//                styleTypeColRow.setColumnaBD(styleTypeDB);
//                Column styleTypeColSis = new Column("style_type", "style_type", null);
//                idMunicipioColRow.setColumnaSistema(styleTypeColSis);
//                tableOp.crearColumnaBD(newTable, styleTypeColRow);
//            }
        	return true;
//        }
//        return false;		
	}
	
	public static boolean createLayer(String url, String layerName){
		
		return true;
	}
	
}
