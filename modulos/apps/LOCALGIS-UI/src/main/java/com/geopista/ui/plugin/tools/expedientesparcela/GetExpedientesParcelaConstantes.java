/**
 * GetExpedientesParcelaConstantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela;

/**
 * Clase que almacena las constantes utilizadas en el plugin Obtener Expedientes de una Parcela
 * 
 * @author fjcastro
 *
 */
public class GetExpedientesParcelaConstantes {

	/* 
	 * idDictionary de TipoExpediente, que se saca de id_vocablos's de la tabla dictionary:
	 */
	
	public static Integer DICTIONARYOBRAMAYOR = 158;
	public static Integer DICTIONARYOBRAMENOR = 157;
	public static Integer DICTIONARYACTIVIDADCALIFICADA = 339;
	public static Integer DICTIONARYACTIVIDADNOCALIFICADA = 340;	
	
	/*
	 * idTipoExpediente para TipoExpediente, IDOBRAMAYOR, IDOBRAMENOR, IDACTIVIDADCALIFICADA E IDACTIVIDADNOCALIFICADA se sacan de la
	 * tabla tipo_licencia:
	 */
	
	public static Integer IDTODOS = -1;
	public static Integer IDOBRAMAYOR = 0;
	public static Integer IDOBRAMENOR = 1;
	public static Integer IDACTIVIDADCALIFICADA = 2;
	public static Integer IDACTIVIDADNOCALIFICADA = 3;
	
	/*
	 * idDictionary de Estado, que se saca de id_vocablo's de la tabla dictionary:
	 */
	
	public static Integer DICTIONARYESTADOAPERTURAEXPEDIENTE = 1152;
	public static Integer DICTIONARYESTADOMEJORADATOS = 1153;
	public static Integer DICTIONARYESTADOSOLICITUDINFORMES = 1154;
	public static Integer DICTIONARYESTADOSESPERAINFORMES = 1155;
	public static Integer DICTIONARYESTADOSEMISIONINFORMERES = 1156;
	public static Integer DICTIONARYESTADOSESPERAALEGACIONES = 1157;
	public static Integer DICTIONARYESTADOSACTUALIZAINFORMERES = 1158;
	public static Integer DICTIONARYESTADOSEMISIONPROPRES = 1159;
	public static Integer DICTIONARYESTADONOTIFDENEGACION = 1160;
	public static Integer DICTIONARYESTADOSFORMALIZALICENCIA = 1161;
	public static Integer DICTIONARYESTADOSNOTIFICACIONAPROB = 1162;
	public static Integer DICTIONARYESTADOSEJECUCION = 1163;
	public static Integer DICTIONARYESTADOSDURMIENTE = 1164;
	public static Integer DICTIONARYESTADOSPUBLICARBOP = 352;
	public static Integer DICTIONARYESTADOSREMISIONCP = 353;
	public static Integer DICTIONARYESTADOSREMISIONDGI = 354;
	public static Integer DICTIONARYESTADOSSOLICITUDACTA = 355;
	public static Integer DICTIONARYESTADOSLICENCIAFUNC = 356;

	/*
	 * idEstado para Estado, que se saca de la tabla estado:
	 */
	
	public static Integer IDESTADOTODOS = -1;
	public static Integer IDESTADOAPERTURAEXPEDIENTE = 1;
	public static Integer IDESTADOMEJORADATOS = 2;
	public static Integer IDESTADOSOLICITUDINFORMES = 3;
	public static Integer IDESTADOSESPERAINFORMES = 4;
	public static Integer IDESTADOSEMISIONINFORMERES = 5;
	public static Integer IDESTADOSESPERAALEGACIONES = 6;
	public static Integer IDESTADOSACTUALIZAINFORMERES = 7;
	public static Integer IDESTADOSEMISIONPROPRES = 8;
	public static Integer IDESTADONOTIFDENEGACION = 9;
	public static Integer IDESTADOSFORMALIZALICENCIA = 10;
	public static Integer IDESTADOSNOTIFICACIONAPROB = 11;
	public static Integer IDESTADOSEJECUCION = 12;
	public static Integer IDESTADOSDURMIENTE = 13;
	public static Integer IDESTADOSPUBLICARBOP = 14;
	public static Integer IDESTADOSREMISIONCP = 15;
	public static Integer IDESTADOSREMISIONDGI = 16;
	public static Integer IDESTADOSSOLICITUDACTA = 17;
	public static Integer IDESTADOSLICENCIAFUNC = 18;
	
	/*
	 * Numero de columnas de la tabla de expedientes:
	 */
	
	public final static int TAMANIO_TABLA = 7;
}
