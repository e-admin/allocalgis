/*
 * Created on 17-jul-2003
 *
 * Copyright (c) 2003
 * Francisco Jos√© Pe√±arrubia Mart√≠nez
 * IVER Tecnolog√≠as de la Informaci√≥n S.A.
 * Salamanca 50
 * 46005 Valencia (        SPAIN )
 * +34 963163400
 * mailto:fran@iver.es
 * http://www.iver.es
 */
/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib·Òez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.geopista.ui.plugin.io.dgn.impl;

/**
 * Clase que almacena informaciÛn del elemento.
 *
 * @author Vicente Caballero Navarro
 */
public class DGNInfo {
	public String fp;
	public int ftall;
	public int next_element_id;
	public int nElemBytes;
	public byte[] abyElem = new byte[131076]; //131076

	//byte[] temporal=new byte[4];
	public int got_tcb;
	public int dimension;
	public int options;
	public double scale;
	public double origin_x;
	public double origin_y;
	public double origin_z;
	public int index_built;
	public int element_count;
	public int max_element_count;
	public DGNElementInfo[] element_index;
	public int got_color_table;
	public byte[][] color_table = new byte[256][3]; //256 3
	public int got_bounds;
	public double min_x;
	public double min_y;
	public double min_z;
	public double max_x;
	public double max_y;
	public double max_z;
	public int has_spatial_filter;
	public int sf_converted_to_uor;
	public int select_complex_group;
	public int in_complex_group;
	public long sf_min_x;
	public long sf_min_y;
	public long sf_max_x;
	public long sf_max_y;
	public double sf_min_x_geo;
	public double sf_min_y_geo;
	public double sf_max_x_geo;
	public double sf_max_y_geo;
}
