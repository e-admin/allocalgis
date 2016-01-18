/**
 * DGNElemGroup.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 07-jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
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
 * Clase utilizada para guardar un elemento de tipo Group.
 *
 * @author FJP
 */
public class DGNElemGroup extends DGNElemCore {
	/*                 typedef struct
	   {
	   Elm_hdr ehdr; // element header
	   Disp_hdr dhdr; // display header
	   short file_chars; // no. of chars. in file spec
	   char file_spec[65] //file specification
	   byte file_num; // file number
	   Fb_opts fb_opts; // file builder options mask
	   Fd_opts fd_opts; // file displayer options mask
	   byte disp_flags[16]; // display flags
	   short lev_flags[8][4]; // level on/off flags
	   long ref_org[3]; // origin in ref file uors
	   double trns_mtrx[9]; // transformation matrix
	   double cnvrs_fact; // conversion factor
	   Group Data Elements (Type 5)
	   long mast_org[3]; // origin in master file uors
	   short log_chars; // characters in logical name
	   char log_name[22]; // logical name (padded)
	   short desc_chars; // characters in description
	   char description[42]; /* description (padded)
	   short lev_sym_mask; // level symbology enable mask
	   short lev_sym[63]; // level symbology descriptor
	   long z_delta; // Z-direction delta
	   short clip_vertices; // clipping vertices
	   Point2d clip_poly[1]; // clipping polygon
	   } Ref_file_type5; */

	//DGNElemCore 	core=new DGNElemCore();
	public DGNPoint origin = new DGNPoint(); /*!< Origin of ellipse */
	public double primary_axis;

	//=new double[4];	/*!< Primary axis length */
	public double secondary_axis;

	//=new double[4]; /*!< Secondary axis length */
	public double rotation; /*!< Counterclockwise rotation in degrees */
	public double[] quat = new double[4]; //4
	public double startang;

	/*!< Start angle (degrees counterclockwise of primary axis) */
	public double sweepang; /*!< Sweep angle (degrees) */
}
