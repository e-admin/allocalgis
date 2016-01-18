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
 * Clase utilizada para guardar un elemento de tipo Arc. 
 *
 * @author Vicente Caballero Navarro
 */
public class DGNElemArc extends DGNElemCore {
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
