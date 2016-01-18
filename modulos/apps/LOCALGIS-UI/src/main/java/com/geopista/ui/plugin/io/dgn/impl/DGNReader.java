/**
 * DGNReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14-jun-2004
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

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


/**
 * Clase dedicada a leer del fichero DGN.
 *
 * @author FJP
 */
public class DGNReader {
	// private static Logger logger = Logger.getLogger(DGNReader.class.getName());
	static int[][] abyDefaultPCT = {
			{ 255, 255, 255 },
			{ 0, 0, 255 },
			{ 0, 255, 0 },
			{ 255, 0, 0 },
			{ 255, 255, 0 },
			{ 255, 0, 255 },
			{ 255, 127, 0 },
			{ 0, 255, 255 },
			{ 64, 64, 64 },
			{ 192, 192, 192 },
			{ 254, 0, 96 },
			{ 160, 224, 0 },
			{ 0, 254, 160 },
			{ 128, 0, 160 },
			{ 176, 176, 176 },
			{ 0, 240, 240 },
			{ 240, 240, 240 },
			{ 0, 0, 240 },
			{ 0, 240, 0 },
			{ 240, 0, 0 },
			{ 240, 240, 0 },
			{ 240, 0, 240 },
			{ 240, 122, 0 },
			{ 0, 240, 240 },
			{ 240, 240, 240 },
			{ 0, 0, 240 },
			{ 0, 240, 0 },
			{ 240, 0, 0 },
			{ 240, 240, 0 },
			{ 240, 0, 240 },
			{ 240, 122, 0 },
			{ 0, 225, 225 },
			{ 225, 225, 225 },
			{ 0, 0, 225 },
			{ 0, 225, 0 },
			{ 225, 0, 0 },
			{ 225, 225, 0 },
			{ 225, 0, 225 },
			{ 225, 117, 0 },
			{ 0, 225, 225 },
			{ 225, 225, 225 },
			{ 0, 0, 225 },
			{ 0, 225, 0 },
			{ 225, 0, 0 },
			{ 225, 225, 0 },
			{ 225, 0, 225 },
			{ 225, 117, 0 },
			{ 0, 210, 210 },
			{ 210, 210, 210 },
			{ 0, 0, 210 },
			{ 0, 210, 0 },
			{ 210, 0, 0 },
			{ 210, 210, 0 },
			{ 210, 0, 210 },
			{ 210, 112, 0 },
			{ 0, 210, 210 },
			{ 210, 210, 210 },
			{ 0, 0, 210 },
			{ 0, 210, 0 },
			{ 210, 0, 0 },
			{ 210, 210, 0 },
			{ 210, 0, 210 },
			{ 210, 112, 0 },
			{ 0, 195, 195 },
			{ 195, 195, 195 },
			{ 0, 0, 195 },
			{ 0, 195, 0 },
			{ 195, 0, 0 },
			{ 195, 195, 0 },
			{ 195, 0, 195 },
			{ 195, 107, 0 },
			{ 0, 195, 195 },
			{ 195, 195, 195 },
			{ 0, 0, 195 },
			{ 0, 195, 0 },
			{ 195, 0, 0 },
			{ 195, 195, 0 },
			{ 195, 0, 195 },
			{ 195, 107, 0 },
			{ 0, 180, 180 },
			{ 180, 180, 180 },
			{ 0, 0, 180 },
			{ 0, 180, 0 },
			{ 180, 0, 0 },
			{ 180, 180, 0 },
			{ 180, 0, 180 },
			{ 180, 102, 0 },
			{ 0, 180, 180 },
			{ 180, 180, 180 },
			{ 0, 0, 180 },
			{ 0, 180, 0 },
			{ 180, 0, 0 },
			{ 180, 180, 0 },
			{ 180, 0, 180 },
			{ 180, 102, 0 },
			{ 0, 165, 165 },
			{ 165, 165, 165 },
			{ 0, 0, 165 },
			{ 0, 165, 0 },
			{ 165, 0, 0 },
			{ 165, 165, 0 },
			{ 165, 0, 165 },
			{ 165, 97, 0 },
			{ 0, 165, 165 },
			{ 165, 165, 165 },
			{ 0, 0, 165 },
			{ 0, 165, 0 },
			{ 165, 0, 0 },
			{ 165, 165, 0 },
			{ 165, 0, 165 },
			{ 165, 97, 0 },
			{ 0, 150, 150 },
			{ 150, 150, 150 },
			{ 0, 0, 150 },
			{ 0, 150, 0 },
			{ 150, 0, 0 },
			{ 150, 150, 0 },
			{ 150, 0, 150 },
			{ 150, 92, 0 },
			{ 0, 150, 150 },
			{ 150, 150, 150 },
			{ 0, 0, 150 },
			{ 0, 150, 0 },
			{ 150, 0, 0 },
			{ 150, 150, 0 },
			{ 150, 0, 150 },
			{ 150, 92, 0 },
			{ 0, 135, 135 },
			{ 135, 135, 135 },
			{ 0, 0, 135 },
			{ 0, 135, 0 },
			{ 135, 0, 0 },
			{ 135, 135, 0 },
			{ 135, 0, 135 },
			{ 135, 87, 0 },
			{ 0, 135, 135 },
			{ 135, 135, 135 },
			{ 0, 0, 135 },
			{ 0, 135, 0 },
			{ 135, 0, 0 },
			{ 135, 135, 0 },
			{ 135, 0, 135 },
			{ 135, 87, 0 },
			{ 0, 120, 120 },
			{ 120, 120, 120 },
			{ 0, 0, 120 },
			{ 0, 120, 0 },
			{ 120, 0, 0 },
			{ 120, 120, 0 },
			{ 120, 0, 120 },
			{ 120, 82, 0 },
			{ 0, 120, 120 },
			{ 120, 120, 120 },
			{ 0, 0, 120 },
			{ 0, 120, 0 },
			{ 120, 0, 0 },
			{ 120, 120, 0 },
			{ 120, 0, 120 },
			{ 120, 82, 0 },
			{ 0, 105, 105 },
			{ 105, 105, 105 },
			{ 0, 0, 105 },
			{ 0, 105, 0 },
			{ 105, 0, 0 },
			{ 105, 105, 0 },
			{ 105, 0, 105 },
			{ 105, 77, 0 },
			{ 0, 105, 105 },
			{ 105, 105, 105 },
			{ 0, 0, 105 },
			{ 0, 105, 0 },
			{ 105, 0, 0 },
			{ 105, 105, 0 },
			{ 105, 0, 105 },
			{ 105, 77, 0 },
			{ 0, 90, 90 },
			{ 90, 90, 90 },
			{ 0, 0, 90 },
			{ 0, 90, 0 },
			{ 90, 0, 0 },
			{ 90, 90, 0 },
			{ 90, 0, 90 },
			{ 90, 72, 0 },
			{ 0, 90, 90 },
			{ 90, 90, 90 },
			{ 0, 0, 90 },
			{ 0, 90, 0 },
			{ 90, 0, 0 },
			{ 90, 90, 0 },
			{ 90, 0, 90 },
			{ 90, 72, 0 },
			{ 0, 75, 75 },
			{ 75, 75, 75 },
			{ 0, 0, 75 },
			{ 0, 75, 0 },
			{ 75, 0, 0 },
			{ 75, 75, 0 },
			{ 75, 0, 75 },
			{ 75, 67, 0 },
			{ 0, 75, 75 },
			{ 75, 75, 75 },
			{ 0, 0, 75 },
			{ 0, 75, 0 },
			{ 75, 0, 0 },
			{ 75, 75, 0 },
			{ 75, 0, 75 },
			{ 75, 67, 0 },
			{ 0, 60, 60 },
			{ 60, 60, 60 },
			{ 0, 0, 60 },
			{ 0, 60, 0 },
			{ 60, 0, 0 },
			{ 60, 60, 0 },
			{ 60, 0, 60 },
			{ 60, 62, 0 },
			{ 0, 60, 60 },
			{ 60, 60, 60 },
			{ 0, 0, 60 },
			{ 0, 60, 0 },
			{ 60, 0, 0 },
			{ 60, 60, 0 },
			{ 60, 0, 60 },
			{ 60, 62, 0 },
			{ 0, 45, 45 },
			{ 45, 45, 45 },
			{ 0, 0, 45 },
			{ 0, 45, 0 },
			{ 45, 0, 0 },
			{ 45, 45, 0 },
			{ 45, 0, 45 },
			{ 45, 57, 0 },
			{ 0, 45, 45 },
			{ 45, 45, 45 },
			{ 0, 0, 45 },
			{ 0, 45, 0 },
			{ 45, 0, 0 },
			{ 45, 45, 0 },
			{ 45, 0, 45 },
			{ 45, 57, 0 },
			{ 0, 30, 30 },
			{ 30, 30, 30 },
			{ 0, 0, 30 },
			{ 0, 30, 0 },
			{ 30, 0, 0 },
			{ 30, 30, 0 },
			{ 30, 0, 30 },
			{ 30, 52, 0 },
			{ 0, 30, 30 },
			{ 30, 30, 30 },
			{ 0, 0, 30 },
			{ 0, 30, 0 },
			{ 30, 0, 0 },
			{ 30, 30, 0 },
			{ 30, 0, 30 },
			{ 192, 192, 192 },
			{ 28, 0, 100 }
		};
	private int LSB;
	private FileInputStream fin;

	// private LEDataInputStream input;
	private MappedByteBuffer bb;
	private int FALSE = 0;
	private int TRUE = 1;
	private DGNElemCore elemento;
	private DGNInfo info; // Contiene el path y otras cosas
	private Rectangle2D.Double m_BoundingBox;
	private DGNElemColorTable m_colorTable;

	/**
	 * Crea un nuevo DGNReader.
	 *
	 * @param pathFich DOCUMENT ME!
	 */
	public DGNReader(String pathFich) {
		info = new DGNInfo();

		DGNElemCore elemento = new DGNElemCore();
		int iArg;
		int bReportExtents = 0;
		byte[] achRaw = new byte[64];
		achRaw[63] = 1;

		double dfSFXMin = 0.0;
		double dfSFXMax = 0.0;
		double dfSFYMin = 0.0;
		double dfSFYMax = 0.0;

		info.fp = pathFich;
		info = DGNOpen(info, 0);

		bb.rewind();
		DGNSetSpatialFilter(info, dfSFXMin, dfSFYMin, dfSFXMax, dfSFYMax);

		int nLevel;
		int nType;
		int[] anLevelTypeCount = new int[128 * 64];
		int[] anLevelCount = new int[64];
		int[] anTypeCount = new int[128];
		double[] adfExtents = new double[6];
		int[] nCount = new int[1];
		nCount[0] = 0;

		DGNGetExtents(info, adfExtents); //extender
		//System.out.println("X Range:" + adfExtents[0] + ", " + adfExtents[3]);
		//System.out.println("Y Range:" + adfExtents[1] + ", " + adfExtents[4]);
		//System.out.println("Z Range:" + adfExtents[2] + ", " + adfExtents[5]);

		m_BoundingBox = new Rectangle2D.Double();
		m_BoundingBox.setRect(adfExtents[0], adfExtents[1],
			(adfExtents[3] - adfExtents[0]), (adfExtents[4] - adfExtents[1]));

		/* m_Renderer = new FRenderer(this); */
		DGNElementInfo[] pasEI; //=new DGNElementInfo[nCount+1];
		pasEI = DGNGetElementIndex(info, nCount);

		//System.out.println("Total Elements:" + nCount[0]);

		for (int i = 0; i < nCount[0]; i++) {
			anLevelTypeCount[(pasEI[i].level * 128) + pasEI[i].type]++;
			anLevelCount[pasEI[i].level]++;
			anTypeCount[pasEI[i].type]++;
		}

		////System.out.println("\n");
		//System.out.println("Per Type Report\n");
		//System.out.println("===============\n");

		//for (nType = 0; nType < 128; nType++) {
		//	if (anTypeCount[nType] != 0) {
		//		System.out.println("Type:" + DGNTypeToName(nType) + ":" +
		//			anTypeCount[nType] + "\n");
		//	}
		//}

		//System.out.println("\n");
		//System.out.println("Per Level Report\n");
		//System.out.println("================\n");
		for (nLevel = 0; nLevel < 64; nLevel++) {
			if (anLevelCount[nLevel] == 0) {
				continue;
			}

			//System.out.println("Level " + nLevel + "," + anLevelCount[nLevel] +"elements:\n");
			//for (nType = 0; nType < 128; nType++) {
			//	if (anLevelTypeCount[(nLevel * 128) + nType] != 0) {
					//System.out.println("  Type " + DGNTypeToName(nType) + ","anLevelTypeCount[(nLevel * 128) + nType] + "\n");
			//	}
			//}

			//System.out.println("\n");
		}

		bb.rewind();
	}

	/* public Color getColor(int indexColor)
	   {
	           int r,g,b;
	           // System.err.println("indexcolor = " + indexColor);
	           // Si no hay tabla de colores, interpretamos que todas las cosas son negras
	           if (m_colorTable == null) return new Color(0,0,0);
	
	           r = ByteUtils.getUnsigned(m_colorTable.color_info[indexColor][0]);
	           g = ByteUtils.getUnsigned(m_colorTable.color_info[indexColor][1]);
	           b = ByteUtils.getUnsigned(m_colorTable.color_info[indexColor][2]);
	
	           if ((r==255) && (g==255) & (b==255))
	           {
	                   r=g=b=0; // El color blanco lo devolvemos como negro.
	           }
	
	           return new Color(r,g,b);
	   } */

	/**
	 * Devuelve la información del DGN.
	 *
	 * @return DGNInfo Información.
	 */
	public DGNInfo getInfo() {
		return info;
	}

	/**
	 * Devuelve el número de elementos.
	 *
	 * @return Número de elementos.
	 */
	public int getNumEntities() {
		return info.element_count;
	}

	/**
	 * Devuelve el rectángulo del extent.
	 *
	 * @return Rectángulo.
	 */
	public Rectangle2D getBoundingBox() {
		return m_BoundingBox;
	}

	/************************************************************************/
	/*                           DGNGotoElement()                           */
	/************************************************************************/

	/**
	 * Seek to indicated element. Changes what element will be read on the next
	 * call to DGNReadElement().  Note that this function requires and index,
	 * and one will be built if not already available.
	 *
	 * @param element_id the element to seek to.  These values are sequentially
	 * 		  ordered starting at zero for the first element.
	 *
	 * @return returns TRUE on success or FALSE on failure.
	 */
	public int DGNGotoElement(int element_id) {
		DGNBuildIndex(info);

		if ((element_id < 0) || (element_id >= info.element_count)) {
			return FALSE;
		}

		// System.out.println("Posicionamos en " + info.element_index[element_id].offset);
		bb.position((int) info.element_index[element_id].offset);

		info.next_element_id = element_id;
		info.in_complex_group = FALSE;

		return TRUE;
	}

	/**
	 * DGNOpen
	 *
	 * @param info DOCUMENT ME!
	 * @param bUpdate DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DGNInfo DGNOpen(DGNInfo info, int bUpdate) {
		int pos = 0;
		byte[] abyHeader = new byte[512];
		info.next_element_id = 0;
		info.got_tcb = FALSE;
		info.scale = 1.0;
		info.origin_x = 0.0;
		info.origin_y = 0.0;
		info.origin_z = 0.0;
		info.index_built = FALSE;
		info.element_count = 0;
		info.element_index = null;
		info.got_bounds = FALSE;

		try {
			fin = new FileInputStream(info.fp);

			FileChannel fc = fin.getChannel();

			long sz = fc.size();
			int numReg;

			// Get the file's size and then map it into memory
			bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
			bb.order(ByteOrder.nativeOrder());
			bb.get(abyHeader, pos, abyHeader.length);

			info.ftall = (int) (sz / 16);

			if (bb.order() == ByteOrder.LITTLE_ENDIAN) {
				LSB = TRUE;
			}

			if (DGNTestOpen(abyHeader, abyHeader.length) != FALSE) {
				if (abyHeader[0] == (byte) 0xC8) {
					info.dimension = 3; //0xC8
				} else {
					info.dimension = 2;
				}

				info.has_spatial_filter = FALSE;
				info.sf_converted_to_uor = FALSE;
				info.select_complex_group = FALSE;
				info.in_complex_group = FALSE;
			}

			// fin.close();
		} catch (Exception e) {
			// logger.debug(e);
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * Comprobación si se puede abrir el fichero.
	 *
	 * @param pabyHeader Vector byte con el header.
	 * @param nByteCount número de bytes.
	 *
	 * @return Devuelve un enteor que muestra si no hay errores.
	 */
	private int DGNTestOpen(byte[] pabyHeader, int nByteCount) {
		if (nByteCount < 4) {
			return TRUE;
		}

		// Is it a cell library?
		if ((pabyHeader[0] == (byte) 0x08) && (pabyHeader[1] == (byte) 0x05) &&
				(pabyHeader[2] == (byte) 0x17) &&
				(pabyHeader[3] == (byte) 0x00)) {
			return TRUE;
		}

		// Is it not a regular 2D or 3D file?
		if (((pabyHeader[0] != (byte) 0x08) && (pabyHeader[0] != (byte) 0xC8)) ||
				(pabyHeader[1] != (byte) 0x09) ||
				(pabyHeader[2] != (byte) 0xFE) ||
				(pabyHeader[3] != (byte) 0x02)) {
			return FALSE;
		}

		return TRUE;
	}

	/**
	 * Lee una fila del elemento.
	 *
	 * @param info Información del DGN.
	 * @param core Elemento.
	 *
	 * @return Devuelve un entero que muestra si se ha calculado correctamente.
	 */
	private int DGNLoadRawElement(DGNInfo info, DGNElemCore core) {
		/* -------------------------------------------------------------------- */
		/*      Read the first four bytes to get the level, type, and word      */
		/*      count.                                                          */
		/* -------------------------------------------------------------------- */

		//int		nType, nWords, nLevel;
		int nType = 0;
		int nWords = 0;
		int nLevel = 0;

		try {
			//input=new LEDataInputStream(fin);
			// System.out.println("info.ftall" + info.ftall + " bb.position() = " + bb.position());
			for (int i = 0; i < 4; i++) {
				info.abyElem[i] = bb.get();

				//info.temporal[i]=input.readByte();
			}

			////System.out.println(ByteUtils.byteToUnsignedInt(info.abyElem[2])+";"+ByteUtils.byteToUnsignedInt(info.abyElem[3]));
			if ((info.abyElem[0] == -1) && (info.abyElem[1] == -1)) {
				return FALSE;
			}

			nWords = ByteUtils.byteToUnsignedInt(info.abyElem[2]) +
				(ByteUtils.byteToUnsignedInt(info.abyElem[3]) * 256);
			nType = ByteUtils.byteToUnsignedInt(info.abyElem[1]) & 0x7f;
			nLevel = ByteUtils.byteToUnsignedInt(info.abyElem[0]) & 0x3f;

			/* -------------------------------------------------------------------- */
			/*      Read the rest of the element data into the working buffer.      */
			/* -------------------------------------------------------------------- */
			if (((nWords * 2) + 4) > info.abyElem.length) {
				return FALSE;
			}

			//CPLAssert( nWords * 2 + 4 <= (int) sizeof(psDGN->abyElem) );
			//byte[] temp = new byte[131072];
			////System.out.println(nWords);
			for (int i = 0; i < (nWords * 2); i++) {
				info.abyElem[i + 4] = bb.get();
			}

			//	input.close();
			/* -------------------------------------------------------------------- */
			/*      Read the rest of the element data into the working buffer.      */
			/* -------------------------------------------------------------------- */
			/*     CPLAssert( nWords * 2 + 4 <= (int) sizeof(psDGN->abyElem) );
			   if( (int) VSIFRead( psDGN->abyElem + 4, 2, nWords, psDGN->fp ) != nWords )
			       return FALSE; */
			info.ftall = bb.position();
		} catch (Exception e) {
			// logger.debug(e);
			//System.err.println("Error al leer: nWords = " + nWords);
			//System.err.println("info.abyElem.length =  " + info.abyElem.length);
			//System.err.println("bb.position() =  " + bb.position());
			e.printStackTrace();

			return FALSE;
		}

		info.nElemBytes = (nWords * 2) + 4;
		info.next_element_id++;

		/* -------------------------------------------------------------------- */
		/*      Return requested info.                                          */
		/* -------------------------------------------------------------------- */
		//if( core.type != null )
		core.type = nType;

		//if( core.level != null )
		core.level = nLevel;

		return TRUE;
	}

	/**
	 * Calcula el filtro espacial al rectángulo del elemento.
	 *
	 * @param info Información del DGN.
	 */
	private void DGNSpatialFilterToUOR(DGNInfo info) {
		DGNPoint sMin = new DGNPoint();
		DGNPoint sMax = new DGNPoint();

		if ((info.sf_converted_to_uor == 1) ||
				(!(info.has_spatial_filter == 1)) || (!(info.got_tcb == 1))) {
			return;
		}

		sMin.x = info.sf_min_x_geo;
		sMin.y = info.sf_min_y_geo;
		sMin.z = 0;

		sMax.x = info.sf_max_x_geo;
		sMax.y = info.sf_max_y_geo;
		sMax.z = 0;

		DGNInverseTransformPoint(info, sMin);
		DGNInverseTransformPoint(info, sMax);

		info.sf_min_x = (long) (sMin.x + 2147483648.0);
		info.sf_min_y = (long) (sMin.y + 2147483648.0);
		info.sf_max_x = (long) (sMax.x + 2147483648.0);
		info.sf_max_y = (long) (sMax.y + 2147483648.0);

		info.sf_converted_to_uor = TRUE;
	}

	/**
	 * Calcula un punto aplicandole la transformación.
	 *
	 * @param info Información del DGN.
	 * @param punto Punto.
	 */
	private void DGNInverseTransformPoint(DGNInfo info, DGNPoint punto) {
		punto.x = (punto.x + info.origin_x) / info.scale;
		punto.y = (punto.y + info.origin_y) / info.scale;
		punto.z = (punto.z + info.origin_z) / info.scale;

		punto.x = Math.max(-2147483647, Math.min(2147483647, punto.x));
		punto.y = Math.max(-2147483647, Math.min(2147483647, punto.y));
		punto.z = Math.max(-2147483647, Math.min(2147483647, punto.z));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public DGNElemCore DGNReadElement() {
		DGNElemCore elemento = new DGNElemCore();
		int nType;
		int nLevel;
		int bInsideFilter;

		/* -------------------------------------------------------------------- */
		/*      Load the element data into the current buffer.  If a spatial    */
		/*      filter is in effect, loop until we get something within our     */
		/*      spatial constraints.                                            */
		/* -------------------------------------------------------------------- */
		do {
			bInsideFilter = TRUE;

			int fin_fichero = DGNLoadRawElement(info, elemento);

			if (fin_fichero == FALSE) {
				return null;
			}

			if (info.has_spatial_filter != FALSE) {
				//long	nXMin, nXMax, nYMin, nYMax;
				if (info.sf_converted_to_uor == FALSE) {
					DGNSpatialFilterToUOR(info);
				}

				if (DGNGetRawExtents(info, null, elemento) == null) {
					bInsideFilter = TRUE;
				} else if ((info.min_x > info.sf_max_x) ||
						(info.min_y > info.sf_max_y) ||
						(info.max_x < info.sf_min_x) ||
						(info.max_y < info.sf_min_y)) {
					bInsideFilter = FALSE;
				}

				if ((elemento.type == DGNFileHeader.DGNT_COMPLEX_CHAIN_HEADER) ||
						(elemento.type == DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER)) {
					info.in_complex_group = TRUE;
					info.select_complex_group = bInsideFilter;
				} else if ((info.abyElem[0] & (byte) 0x80) != FALSE) {
					if (info.in_complex_group == TRUE) {
						bInsideFilter = info.select_complex_group;
					}
				} else {
					info.in_complex_group = FALSE;
				}
			}
		} while (bInsideFilter == FALSE);

		elemento = DGNProcessElement(info, elemento.type, elemento.level);

		return elemento;
	}

	/**
	 * Devuelve los extent de una fila.
	 *
	 * @param info Información del DGN.
	 * @param pabyRawData Vector de byte.
	 * @param elemento Elemento.
	 *
	 * @return Vector de double.
	 */
	public double[] DGNGetRawExtents(DGNInfo info, byte[] pabyRawData,
		DGNElemCore elemento) {
		//byte[] pabyRawData = new byte[info.abyElem.length];
		if (pabyRawData == null) {
			pabyRawData = info.abyElem;
		}

		double[] tempo = new double[6];

		switch (elemento.type) {
			case DGNFileHeader.DGNT_LINE:
			case DGNFileHeader.DGNT_LINE_STRING:
			case DGNFileHeader.DGNT_SHAPE:
			case DGNFileHeader.DGNT_CURVE:
			case DGNFileHeader.DGNT_BSPLINE:
			case DGNFileHeader.DGNT_ELLIPSE:
			case DGNFileHeader.DGNT_ARC:
			case DGNFileHeader.DGNT_TEXT:
			case DGNFileHeader.DGNT_COMPLEX_CHAIN_HEADER:
			case DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER:

				byte[] temp = new byte[4];
				System.arraycopy(pabyRawData, 4, temp, 0, 4);
				tempo[0] = DGN_INT32(temp); //4

				System.arraycopy(pabyRawData, 8, temp, 0, 4);
				tempo[1] = DGN_INT32(temp);

				System.arraycopy(pabyRawData, 12, temp, 0, 4);
				tempo[2] = DGN_INT32(temp);

				System.arraycopy(pabyRawData, 16, temp, 0, 4);
				tempo[3] = DGN_INT32(temp);

				System.arraycopy(pabyRawData, 20, temp, 0, 4);
				tempo[4] = DGN_INT32(temp);

				System.arraycopy(pabyRawData, 24, temp, 0, 4);
				tempo[5] = DGN_INT32(temp);

				// System.out.println("tempo = " + tempo[0] + " " + tempo[1] + " " + tempo[2]);
				return tempo;

			default:
				return null;
		}
	}

	/**
	 * A partir de un vector de byte devuelve un double.
	 *
	 * @param p Vector de byte.
	 *
	 * @return double.
	 */
	private double DGN_INT32(byte[] p) {
		int x = 256;
		int x0;
		int x1;
		int x2;
		int x3;
		x0 = (int) p[0];
		x1 = (int) p[1];
		x2 = (int) p[2];
		x3 = (int) p[3];

		if (p[0] < 0) {
			x0 = x + (int) p[0];
		}

		if (p[1] < 0) {
			x1 = x + (int) p[1];
		}

		if (p[2] < 0) {
			x2 = x + (int) p[2];
		}

		if (p[3] < 0) {
			x3 = x + (int) p[3];
		}

		return (x2 + (x3 * 256) + (x1 * 65536 * 256) + (x0 * 65536));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param info DOCUMENT ME!
	 * @param nType DOCUMENT ME!
	 * @param nLevel DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DGNElemCore DGNProcessElement(DGNInfo info, int nType, int nLevel) {
		DGNElemCore elemento = new DGNElemCore();

		// if (info.next_element_id < 100)
		// 	System.out.println("nType = " + nType);       
		switch (nType) {
			case DGNFileHeader.DGNT_SHARED_CELL_DEFN:
				//System.err.println(DGNTypeToName(nType));
				elemento.stype = DGNFileHeader.DGNST_SHARED_CELL_DEFN;
				DGNParseCore(info, elemento);

				break;

			case DGNFileHeader.DGNT_CELL_HEADER: {
				// System.err.println("DGNT_CELL_HEADER");
				DGNElemCellHeader psCell = new DGNElemCellHeader();
				psCell.stype = DGNFileHeader.DGNST_CELL_HEADER;
				DGNParseCore(info, psCell);
				psCell.totlength = ByteUtils.getUnsigned(info.abyElem[36]) +
					(ByteUtils.getUnsigned(info.abyElem[37]) * 256);

				byte[] temp = new byte[psCell.name.length];
				System.arraycopy(psCell.name, 0, temp, 0, psCell.name.length);
				DGNRad50ToAscii(ByteUtils.byteToUnsignedInt(info.abyElem[38]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[39]) * 256), temp);

				System.arraycopy(psCell.name, 3, temp, 0, psCell.name.length -
					3); //esta linea puede tener problemas.
				DGNRad50ToAscii(ByteUtils.byteToUnsignedInt(info.abyElem[40]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[41]) * 256), temp);
				psCell.cclass = ByteUtils.byteToUnsignedInt(info.abyElem[42]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[43]) * 256);
				psCell.levels[0] = ByteUtils.byteToUnsignedInt(info.abyElem[44]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[45]) * 256);
				psCell.levels[1] = ByteUtils.byteToUnsignedInt(info.abyElem[46]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[47]) * 256);
				psCell.levels[2] = ByteUtils.byteToUnsignedInt(info.abyElem[48]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[49]) * 256);
				psCell.levels[3] = ByteUtils.byteToUnsignedInt(info.abyElem[50]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[51]) * 256);
				psCell.color = info.abyElem[35];

				if (info.dimension == 2) {
					byte[] temp1 = new byte[4];
					System.arraycopy(info.abyElem, 52, temp1, 0, 4);
					psCell.rnglow.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 56, temp1, 0, 4);
					psCell.rnglow.y = DGN_INT32(temp);
					System.arraycopy(info.abyElem, 60, temp1, 0, 4);
					psCell.rnghigh.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 64, temp1, 0, 4);
					psCell.rnghigh.y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 84, temp1, 0, 4);
					psCell.origin.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 88, temp1, 0, 4);
					psCell.origin.y = DGN_INT32(temp1);

					{
						double a;
						double b;
						double c;
						double d;
						double a2;
						double c2;
						System.arraycopy(info.abyElem, 68, temp1, 0, 4);
						a = DGN_INT32(temp1);
						System.arraycopy(info.abyElem, 72, temp1, 0, 4);
						b = DGN_INT32(temp1);
						System.arraycopy(info.abyElem, 76, temp1, 0, 4);
						c = DGN_INT32(temp1);
						System.arraycopy(info.abyElem, 80, temp1, 0, 4);
						d = DGN_INT32(temp1);
						a2 = a * a;
						c2 = c * c;
						psCell.xscale = Math.sqrt(a2 + c2) / 214748;
						psCell.yscale = Math.sqrt((b * b) + (d * d)) / 214748;
						psCell.rotation = Math.acos(a / Math.sqrt(a2 + c2));

						if (b <= 0) {
							psCell.rotation = (psCell.rotation * 180) / Math.PI;
						} else {
							psCell.rotation = 360 -
								((psCell.rotation * 180) / Math.PI);
						}
					}
				} else {
					byte[] temp1 = new byte[4];
					System.arraycopy(info.abyElem, 52, temp1, 0, 4);
					psCell.rnglow.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 56, temp1, 0, 4);
					psCell.rnglow.y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 60, temp1, 0, 4);
					psCell.rnglow.z = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 64, temp1, 0, 4);
					psCell.rnghigh.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 68, temp1, 0, 4);
					psCell.rnghigh.y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 72, temp1, 0, 4);
					psCell.rnghigh.z = DGN_INT32(temp1);

					System.arraycopy(info.abyElem, 112, temp1, 0, 4);
					psCell.origin.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 116, temp1, 0, 4);
					psCell.origin.y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 120, temp1, 0, 4);
					psCell.origin.z = DGN_INT32(temp1);
				}

				DGNTransformPoint(info, psCell.rnglow);
				DGNTransformPoint(info, psCell.rnghigh);
				DGNTransformPoint(info, psCell.origin);
				elemento = psCell;

				// DGNDumpElement(info,elemento,"");
			}

			break;

			case DGNFileHeader.DGNT_CELL_LIBRARY: {
				//System.err.println("DGNT_CELL_LIBRARY");

				DGNElemCellLibrary psCell = new DGNElemCellLibrary();
				int iWord;
				psCell.stype = DGNFileHeader.DGNST_CELL_LIBRARY;
				DGNParseCore(info, psCell);

				byte[] temp = new byte[psCell.name.length];
				System.arraycopy(psCell.name, 0, temp, 0, psCell.name.length);
				DGNRad50ToAscii(ByteUtils.byteToUnsignedInt(info.abyElem[32]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[33]) * 256), temp);
				System.arraycopy(psCell.name, 3, temp, 0, psCell.name.length);
				DGNRad50ToAscii(ByteUtils.byteToUnsignedInt(info.abyElem[34]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[35]) * 256), temp);
				psCell.properties = info.abyElem[38] +
					(info.abyElem[39] * 256);
				psCell.dispsymb = ByteUtils.byteToUnsignedInt(info.abyElem[40]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[41]) * 256);
				psCell.cclass = ByteUtils.byteToUnsignedInt(info.abyElem[42]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[43]) * 256);
				psCell.levels[0] = ByteUtils.byteToUnsignedInt(info.abyElem[44]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[45]) * 256);
				psCell.levels[1] = ByteUtils.byteToUnsignedInt(info.abyElem[46]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[47]) * 256);
				psCell.levels[2] = ByteUtils.byteToUnsignedInt(info.abyElem[48]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[49]) * 256);
				psCell.levels[3] = ByteUtils.byteToUnsignedInt(info.abyElem[50]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[51]) * 256);
				psCell.numwords = ByteUtils.byteToUnsignedInt(info.abyElem[36]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[37]) * 256);

				for (iWord = 0; iWord < 9; iWord++) {
					int iOffset = 52 + (iWord * 2);
					System.arraycopy(psCell.name, iWord * 3, temp, 0,
						psCell.description.length);
					DGNRad50ToAscii(ByteUtils.byteToUnsignedInt(
							info.abyElem[iOffset]) +
						(ByteUtils.byteToUnsignedInt(info.abyElem[iOffset + 1]) * 256),
						temp);
				}

				elemento = psCell;
			}

			break;

			case DGNFileHeader.DGNT_LINE: {
				DGNElemMultiPoint psLine = new DGNElemMultiPoint();
				psLine.stype = DGNFileHeader.DGNST_MULTIPOINT;
				DGNParseCore(info, psLine);
				psLine.num_vertices = 2;
				psLine.vertices = new DGNPoint[psLine.num_vertices];

				if (info.dimension == 2) {
					byte[] temp1 = new byte[4];
					System.arraycopy(info.abyElem, 36, temp1, 0, 4);
					psLine.vertices[0] = new DGNPoint();
					psLine.vertices[0].x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 40, temp1, 0, 4);
					psLine.vertices[0].y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 44, temp1, 0, 4);
					psLine.vertices[1] = new DGNPoint();
					psLine.vertices[1].x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 48, temp1, 0, 4);
					psLine.vertices[1].y = DGN_INT32(temp1);
				} else {
					byte[] temp1 = new byte[4];
					System.arraycopy(info.abyElem, 36, temp1, 0, 4);
					psLine.vertices[0] = new DGNPoint();
					psLine.vertices[0].x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 40, temp1, 0, 4);
					psLine.vertices[0].y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 44, temp1, 0, 4);
					psLine.vertices[0].z = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 48, temp1, 0, 4);
					psLine.vertices[1] = new DGNPoint();
					psLine.vertices[1].x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 52, temp1, 0, 4);
					psLine.vertices[1].y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 56, temp1, 0, 4);
					psLine.vertices[1].z = DGN_INT32(temp1);
				}

				DGNTransformPoint(info, psLine.vertices[0]);
				DGNTransformPoint(info, psLine.vertices[1]);
				elemento = psLine;
			}

			break;

			case DGNFileHeader.DGNT_LINE_STRING:
			case DGNFileHeader.DGNT_SHAPE: // regular
			case DGNFileHeader.DGNT_CURVE: // mal
			case DGNFileHeader.DGNT_BSPLINE: // aceptable
			 {
				DGNElemMultiPoint psLine = new DGNElemMultiPoint();
				int i;
				int count;
				int pntsize = info.dimension * 4;

				count = ByteUtils.byteToUnsignedInt(info.abyElem[36]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[37]) * 256);
				psLine.stype = DGNFileHeader.DGNST_MULTIPOINT;
				DGNParseCore(info, psLine);

				if (info.nElemBytes < (38 + (count * pntsize))) {
					//System.err.println("Error en los vertices de multipunto");
					count = (info.nElemBytes - 38) / pntsize;

					return null;
				}

				psLine.num_vertices = count;
				psLine.vertices = new DGNPoint[psLine.num_vertices];

				for (i = 0; i < psLine.num_vertices; i++) {
					byte[] temp1 = new byte[4];
					System.arraycopy(info.abyElem, 38 + (i * pntsize), temp1,
						0, 4);
					psLine.vertices[i] = new DGNPoint();
					psLine.vertices[i].x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 42 + (i * pntsize), temp1,
						0, 4);
					psLine.vertices[i].y = DGN_INT32(temp1);

					if (info.dimension == 3) {
						System.arraycopy(info.abyElem, 46 + (i * pntsize),
							temp1, 0, 4);
						psLine.vertices[i].z = DGN_INT32(temp1);
					}

					DGNTransformPoint(info, psLine.vertices[i]);
				}

				elemento = psLine;
			}

			break;

			case DGNFileHeader.DGNT_GROUP_DATA:

				if (nLevel == DGNFileHeader.DGN_GDL_COLOR_TABLE) {
					elemento = DGNParseColorTable(info);
				} else {
					elemento.stype = DGNFileHeader.DGNST_CORE;
					DGNParseCore(info, elemento);
				}

				// System.err.println("DGNT_GROUP_DATA (nType = 5)");
				// DGNDumpElement(info, elemento,"");
				break;

			case DGNFileHeader.DGNT_ELLIPSE: {
				DGNElemArc psEllipse = new DGNElemArc();

				psEllipse.stype = DGNFileHeader.DGNST_ARC;
				DGNParseCore(info, psEllipse);

				int[] fin = new int[1];
				fin[0] = 0;

				byte[] temp1 = new byte[8];
				System.arraycopy(info.abyElem, 36, temp1, 0, 8);

				fin[0] = 0;
				psEllipse.primary_axis = DGNParseIEEE(temp1);

				psEllipse.primary_axis *= info.scale;
				System.arraycopy(info.abyElem, 44, temp1, 0, 8);
				fin[0] = 0;

				psEllipse.secondary_axis = DGNParseIEEE(temp1);
				psEllipse.secondary_axis *= info.scale;

				if (info.dimension == 2) {
					System.arraycopy(info.abyElem, 52, temp1, 0, 4);
					psEllipse.rotation = DGN_INT32(temp1);
					psEllipse.rotation = psEllipse.rotation / 360000.0;
					System.arraycopy(info.abyElem, 56, temp1, 0, 8);
					fin[0] = 0;

					psEllipse.origin.x = DGNParseIEEE(temp1);
					System.arraycopy(info.abyElem, 64, temp1, 0, 8);
					fin[0] = 0;

					psEllipse.origin.y = DGNParseIEEE(temp1);
				} else {
					// leave quaternion for later 
					System.arraycopy(info.abyElem, 68, temp1, 0, 8);
					fin[0] = 0;
					psEllipse.origin.x = DGNParseIEEE(temp1);

					System.arraycopy(info.abyElem, 76, temp1, 0, 8);
					fin[0] = 0;
					psEllipse.origin.y = DGNParseIEEE(temp1);

					System.arraycopy(info.abyElem, 84, temp1, 0, 8);
					fin[0] = 0;
					psEllipse.origin.z = DGNParseIEEE(temp1);

					System.arraycopy(info.abyElem, 52, temp1, 0, 4);
					psEllipse.quat[0] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 56, temp1, 0, 4);
					psEllipse.quat[1] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 60, temp1, 0, 4);
					psEllipse.quat[2] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 64, temp1, 0, 4);
					psEllipse.quat[3] = DGN_INT32(temp1);
				}

				// System.err.println("Punto antes de transformar:" + psEllipse.origin.x + " " + psEllipse.origin.y);
				DGNTransformPoint(info, (psEllipse.origin));

				// System.err.println("Punto DESPUES de transformar:" + psEllipse.origin.x + " " + psEllipse.origin.y);
				psEllipse.startang = 0.0;
				psEllipse.sweepang = 360.0;
				elemento = psEllipse;

				// DGNDumpElement(info, elemento,"");
			}

			break;

			case DGNFileHeader.DGNT_ARC: {
				DGNElemArc psEllipse = new DGNElemArc();
				double nSweepVal;

				psEllipse.stype = DGNFileHeader.DGNST_ARC;
				DGNParseCore(info, psEllipse);

				int[] fin = new int[1];
				fin[0] = 0;

				byte[] temp1 = new byte[8];
				System.arraycopy(info.abyElem, 36, temp1, 0, 4);
				psEllipse.startang = DGN_INT32(temp1);
				psEllipse.startang = psEllipse.startang / 360000.0;

				/* if ((info.abyElem[41] & (byte) 0x80) != FALSE) {
				   info.abyElem[41] &= (byte) 0x7f;
				   System.arraycopy(info.abyElem, 40, temp1, 0, 4);
				   nSweepVal = -1 * DGN_INT32(temp1);
				   } else {
				       System.arraycopy(info.abyElem, 40, temp1, 0, 4);
				   }
				   nSweepVal = DGN_INT32(temp1); */
				if ((info.abyElem[41] & (byte) 0x80) != FALSE) {
					info.abyElem[41] &= (byte) 0x7f;
					System.arraycopy(info.abyElem, 40, temp1, 0, 4);
					nSweepVal = -1 * DGN_INT32(temp1);
				} else {
					System.arraycopy(info.abyElem, 40, temp1, 0, 4);
					nSweepVal = DGN_INT32(temp1);
				}

				if (nSweepVal == 0) {
					psEllipse.sweepang = 360.0;
				} else {
					psEllipse.sweepang = nSweepVal / 360000.0;
				}

				System.arraycopy(info.abyElem, 44, temp1, 0, 8);
				fin[0] = 0;
				psEllipse.primary_axis = DGNParseIEEE(temp1);

				psEllipse.primary_axis *= info.scale;
				System.arraycopy(info.abyElem, 52, temp1, 0, 8);
				fin[0] = 0;
				psEllipse.secondary_axis = DGNParseIEEE(temp1);

				psEllipse.secondary_axis *= info.scale;

				if (info.dimension == 2) {
					System.arraycopy(info.abyElem, 60, temp1, 0, 4);
					psEllipse.rotation = DGN_INT32(temp1);
					psEllipse.rotation = psEllipse.rotation / 360000.0;
					System.arraycopy(info.abyElem, 64, temp1, 0, 8);
					fin[0] = 0;
					psEllipse.origin.x = DGNParseIEEE(temp1);

					System.arraycopy(info.abyElem, 72, temp1, 0, 8);
					fin[0] = 0;
					psEllipse.origin.y = DGNParseIEEE(temp1);
				} else {
					// for now we don't try to handle quaternion 
					psEllipse.rotation = 0;
					System.arraycopy(info.abyElem, 76, temp1, 0, 8);
					fin[0] = 0;

					psEllipse.origin.x = DGNParseIEEE(temp1);
					System.arraycopy(info.abyElem, 84, temp1, 0, 8);
					fin[0] = 0;

					psEllipse.origin.y = DGNParseIEEE(temp1);
					System.arraycopy(info.abyElem, 92, temp1, 0, 8);
					fin[0] = 0;

					psEllipse.origin.z = DGNParseIEEE(temp1);
					System.arraycopy(info.abyElem, 60, temp1, 0, 4);
					psEllipse.quat[0] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 64, temp1, 0, 4);
					psEllipse.quat[1] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 68, temp1, 0, 4);
					psEllipse.quat[2] = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 72, temp1, 0, 4);
					psEllipse.quat[3] = DGN_INT32(temp1);
				}

				DGNTransformPoint(info, (psEllipse.origin));
				elemento = psEllipse;
			}

			break;

			case DGNFileHeader.DGNT_TEXT: {
				DGNElemText psText = new DGNElemText();
				int num_chars;
				int text_off;

				if (info.dimension == 2) {
					num_chars = ByteUtils.byteToUnsignedInt(info.abyElem[58]);
				} else {
					num_chars = ByteUtils.byteToUnsignedInt(info.abyElem[74]);
				}

				psText.stype = DGNFileHeader.DGNST_TEXT;
				DGNParseCore(info, psText);

				psText.font_id = ByteUtils.byteToUnsignedInt(info.abyElem[36]);
				psText.justification = ByteUtils.byteToUnsignedInt(info.abyElem[37]);

				byte[] temp1 = new byte[8];
				System.arraycopy(info.abyElem, 38, temp1, 0, 4);
				psText.length_mult = (DGN_INT32(temp1) * info.scale * 6.0) / 1000.0;
				System.arraycopy(info.abyElem, 42, temp1, 0, 4);
				psText.height_mult = (DGN_INT32(temp1) * info.scale * 6.0) / 1000.0;

				int[] fin = new int[1];
				fin[0] = 0;

				if (info.dimension == 2) {
					System.arraycopy(info.abyElem, 46, temp1, 0, 4);
					psText.rotation = DGN_INT32(temp1);
					psText.rotation = psText.rotation / 360000.0;
					System.arraycopy(info.abyElem, 50, temp1, 0, 4);
					psText.origin.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 54, temp1, 0, 4);
					psText.origin.y = DGN_INT32(temp1);
					text_off = 60;
				} else {
					/* leave quaternion for later */
					System.arraycopy(info.abyElem, 62, temp1, 0, 4);
					psText.origin.x = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 66, temp1, 0, 4);
					psText.origin.y = DGN_INT32(temp1);
					System.arraycopy(info.abyElem, 70, temp1, 0, 4);
					psText.origin.z = DGN_INT32(temp1);
					text_off = 76;
				}

				// System.err.println("Punto antes de transformar:" + psText.origin.x + " " + psText.origin.y);
				DGNTransformPoint(info, (psText.origin));

				// AQUI FALTA METER ALGO PARA SOPORTAR TEXTOS MULTYBYTE
				byte[] temp = new byte[num_chars];
				System.arraycopy(info.abyElem, text_off, temp, 0, num_chars);

				// fin[0] = 0;
				String strAux = null;

				try {
					psText.string = new String(temp, "ISO-8859-1");

					// System.err.println(strAux);
				} catch (Exception e) {
					e.printStackTrace();
				}

				elemento = psText;
			}

			break;

			case DGNFileHeader.DGNT_TCB:
				elemento = DGNParseTCB(info);
				elemento.level = nLevel;
				elemento.type = nType;

				break;

			case DGNFileHeader.DGNT_COMPLEX_CHAIN_HEADER:
			case DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER: {
				DGNElemComplexHeader psHdr = new DGNElemComplexHeader();

				psHdr.stype = DGNFileHeader.DGNST_COMPLEX_HEADER;
				DGNParseCore(info, psHdr);

				psHdr.totlength = ByteUtils.byteToUnsignedInt(info.abyElem[36]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[37]) * 256);
				psHdr.numelems = ByteUtils.byteToUnsignedInt(info.abyElem[38]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[39]) * 256);
				elemento = psHdr;
			}

			break;

			case DGNFileHeader.DGNT_TAG_VALUE: {
				DGNElemTagValue psTag = new DGNElemTagValue();

				psTag.stype = DGNFileHeader.DGNST_TAG_VALUE;
				DGNParseCore(info, psTag);

				int[] fin = new int[1];
				fin[0] = 0;

				byte[] temp1 = new byte[8];
				psTag.tagType = ByteUtils.byteToUnsignedInt(info.abyElem[74]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[75]) * 256);
				System.arraycopy(info.abyElem, 68, temp1, 0, 4);
				fin[0] = 0;
				psTag.tagSet = ByteUtils.bytesToInt(temp1, fin);

				psTag.tagSet = CPL_LSBWORD32(psTag.tagSet);
				psTag.tagIndex = ByteUtils.byteToUnsignedInt(info.abyElem[72]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[73]) * 256);
				psTag.tagLength = ByteUtils.byteToUnsignedInt(info.abyElem[150]) +
					(ByteUtils.byteToUnsignedInt(info.abyElem[151]) * 256);

				if (psTag.tagType == 1) {
					byte[] temp = new byte[info.abyElem.length - 154];
					System.arraycopy(info.abyElem, 4, temp1, 0, //154
						4); //info.abyElem.length - 154
					fin[0] = 0;
					psTag.tagValue.string = CPLStrdup(ByteUtils.bytesToString(
								temp1, fin).toCharArray());
				} else if (psTag.tagType == 3) {
					byte[] temp = new byte[4];
					System.arraycopy(info.abyElem, 154, temp1, 0, 4);
					fin[0] = 0;
					psTag.tagValue.integer = ByteUtils.bytesToInt(temp1, fin);

					psTag.tagValue.integer = CPL_LSBWORD32((int) psTag.tagValue.integer);
				} else if (psTag.tagType == 4) {
					byte[] temp = new byte[8];
					System.arraycopy(info.abyElem, 154, temp1, 0, 8);
					fin[0] = 0;
					psTag.tagValue.real = DGNParseIEEE(temp1);
				}

				elemento = psTag;
			}

			break;

			/* case DGNFileHeader.DGNT_APPLICATION_ELEM:
			   if (nLevel == 24) {
			       elemento = DGNParseTagSet(info);
			   } else {
			       elemento.stype = DGNFileHeader.DGNST_CORE;
			       DGNParseCore(info, elemento);
			   }
			   break; */
			default: {
				// System.err.println("Entra un " + nType + " en DGNProcessElement");
				elemento.stype = DGNFileHeader.DGNST_CORE;
				DGNParseCore(info, elemento);
			}

			break;
		}

		/* -------------------------------------------------------------------- */
		/*      If the element structure type is "core" or if we are running    */
		/*      in "capture all" mode, record the complete binary image of      */
		/*      the element.                                                    */
		/* -------------------------------------------------------------------- */
		if ((elemento.stype == DGNFileHeader.DGNST_CORE) ||
				((info.options & DGNFileHeader.DGNO_CAPTURE_RAW_DATA) != FALSE)) {
			elemento.raw_bytes = info.nElemBytes;

			elemento.raw_data = new byte[elemento.raw_bytes];

			int[] fin = new int[1];
			fin[0] = 0;
			System.arraycopy(info.abyElem, 0, elemento.raw_data, 0,
				elemento.raw_bytes);
		}

		/* -------------------------------------------------------------------- */
		/*      Collect some additional generic information.                    */
		/* -------------------------------------------------------------------- */
		elemento.element_id = info.next_element_id - 1;

		elemento.offset = info.ftall - info.nElemBytes;
		elemento.size = info.nElemBytes;

		// DGNDumpElement(info, elemento,"");
		return elemento;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param d DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private double DGNParseIEEE(double d) {
		byte[] temp = new byte[8];
		int[] f = { 0 };
		ByteUtils.doubleToBytes(d, temp, f);

		return DGNParseIEEE(temp);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param b DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private double DGNParseIEEE(byte[] b) {
		byte BYTE2 = b[7];
		byte BYTE3 = b[6];
		byte BYTE0 = b[5];
		byte BYTE1 = b[4];
		byte BYTE6 = b[3];
		byte BYTE7 = b[2];
		byte BYTE4 = b[1];
		byte BYTE5 = b[0];
		int sign;
		int exponent;
		int fraction;
		double value;
		int hi;
		int lo;
		int rndbits;

		/* -------------------------------------------------------------------- */
		/*        Save the sign of the double                                        */
		/* -------------------------------------------------------------------- */
		byte[] temp = new byte[4];
		byte[] temp1 = new byte[4];

		temp[3] = BYTE7;
		temp[2] = BYTE6;
		temp[1] = BYTE5;
		temp[0] = BYTE4;

		int[] f = { 0 };
		hi = ByteUtils.bytesToInt(temp, f);
		sign = hi & 0x80000000;

		/* -------------------------------------------------------------------- */
		/*        Adjust the exponent so that we may work with it                        */
		/* -------------------------------------------------------------------- */
		exponent = hi >> 23;
		exponent = exponent & 0x000000ff;

		if (exponent != 0) {
			exponent = exponent - 129 + 1023;
		}

		/* -------------------------------------------------------------------- */
		/*        Save the bits that we are discarding so we can round properly        */
		/* -------------------------------------------------------------------- */
		temp[3] = BYTE3;
		temp[2] = BYTE2;
		temp[1] = BYTE1;
		temp[0] = BYTE0;

		f[0] = 0;
		lo = ByteUtils.bytesToInt(temp, f);
		rndbits = lo & 0x00000007;

		lo = lo >> 3;
		lo = (lo & 0x1fffffff) | (hi << 29);

		if (rndbits != 0) {
			lo = lo | 0x00000001;
		}

		/* -------------------------------------------------------------------- */
		/*        Shift the hi-order int over 3 and insert the exponent and sign        */
		/* -------------------------------------------------------------------- */
		hi = hi >> 3;
		hi = hi & 0x000fffff;
		hi = hi | (exponent << 20) | sign;
		f[0] = 0;
		ByteUtils.intToBytes(hi, temp, f);
		f[0] = 0;
		ByteUtils.intToBytes(lo, temp1, f);

		byte[] result = new byte[8];
		result[7] = temp1[3];
		result[6] = temp1[2];
		result[5] = temp1[1];
		result[4] = temp1[0];
		result[3] = temp[3];
		result[2] = temp[2];
		result[1] = temp[1];
		result[0] = temp[0];

		f[0] = 0;

		value = ByteUtils.bytesToDouble(result, f);

		return value;
	}

	/**
	 * Cambia el double a IEEDouble.
	 *
	 * @param dbl double de entrada.
	 */
	private void DGN2IEEEDouble(double dbl) {
		double64 dt = new double64();

		dt.hi = 0;
		dt.lo = 0;

		long sign = 0;
		long exponent = 0;
		long rndbits;
		byte[] srclo = new byte[4];
		byte[] srchi = new byte[4];
		byte[] destlo = new byte[4];
		byte[] desthi = new byte[4];
		byte[] src = new byte[8];
		byte[] dest = new byte[8];

		for (int i = 0; i < 8; i++) {
			src[i] = 0;
			dest[i] = 0;
		}

		int[] fin = new int[1];

		fin[0] = 0;
		ByteUtils.doubleToBytes(dbl, src, fin);

		if (LSB == TRUE) {
			dest[2] = src[0];
			dest[3] = src[1];
			dest[0] = src[2];
			dest[1] = src[3];
			dest[6] = src[4];
			dest[7] = src[5];
			dest[4] = src[6];
			dest[5] = src[7];
		} else {
			dest[1] = src[0];
			dest[0] = src[1];
			dest[3] = src[2];
			dest[2] = src[3];
			dest[5] = src[4];
			dest[4] = src[5];
			dest[7] = src[6];
			dest[6] = src[7];
		}

		System.arraycopy(dest, 0, destlo, 0, 4);
		fin[0] = 0;
		dt.lo = ByteUtils.bytesToLong(destlo, fin);
		System.arraycopy(dest, 4, desthi, 0, 4);
		fin[0] = 0;
		dt.hi = ByteUtils.bytesToLong(desthi, fin);
		sign = dt.hi & 0x80000000;
		exponent = dt.hi >> 23;
		exponent = (long) exponent & 0x000000ff;

		if (exponent != FALSE) {
			exponent = exponent - 129 + 1023;
		}

		rndbits = dt.lo & 0x00000007;
		dt.lo = dt.lo >> 3;
		dt.lo = (dt.lo & 0x1fffffff) | (dt.hi << 29);

		if (rndbits != FALSE) {
			dt.lo = dt.lo | 0x00000001;
		}

		dt.hi = dt.hi >> 3;
		dt.hi = dt.hi & 0x000fffff;
		dt.hi = dt.hi | ((long) exponent << 20) | (long) sign;

		if (LSB == TRUE) {
			fin[0] = 0;
			ByteUtils.longToBytes(dt.lo, srclo, fin);
			fin[0] = 0;
			ByteUtils.longToBytes(dt.hi, srchi, fin);
			dest[0] = srchi[0];
			dest[1] = srchi[1];
			dest[2] = srchi[2];
			dest[3] = srchi[3];
			dest[4] = srclo[0];
			dest[5] = srclo[1];
			dest[6] = srclo[2];
			dest[7] = srclo[3];
		} else {
			//memcpy( dbl, &dt, 8 );
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param dbl DOCUMENT ME!
	 */
	private void IEEE2DGNDouble(double dbl) {
		double64 dt = new double64();

		dt.hi = 0;
		dt.lo = 0;

		long sign;
		long exponent;
		long rndbits;
		byte[] srclo = new byte[4];
		byte[] srchi = new byte[4];
		byte[] destlo = new byte[4];
		byte[] desthi = new byte[4];
		byte[] src = new byte[8];
		byte[] dest = new byte[8];

		for (int i = 0; i < 8; i++) {
			src[i] = '0';
			dest[i] = '0';
		}

		int[] fin = new int[1];

		if (LSB == TRUE) {
			fin[0] = 0;
			ByteUtils.doubleToBytes(dbl, src, fin);
			dest[0] = src[4];
			dest[1] = src[5];
			dest[2] = src[6];
			dest[3] = src[7];
			dest[4] = src[0];
			dest[5] = src[1];
			dest[6] = src[2];
			dest[7] = src[3];
		} else {
			//memcpy( &dt, dbl, 8 );
		}

		sign = dt.hi & 0x80000000;
		exponent = dt.hi >> 20;
		exponent = exponent & 0x000007ff;

		/* -------------------------------------------------------------------- */
		/*        An exponent of zero means a zero value.                                */
		/* -------------------------------------------------------------------- */
		if (exponent != FALSE) {
			exponent = exponent - 1023 + 129;
		}

		/* -------------------------------------------------------------------- */
		/*        In the case of overflow, return the largest number we can        */
		/* -------------------------------------------------------------------- */
		if (exponent > 255) {
			fin[0] = 0;
			ByteUtils.doubleToBytes(dbl, dest, fin);

			if (sign != FALSE) {
				dest[1] = 0xf;
			} else {
				dest[1] = 0x7f;
			}

			dest[0] = 0xf;
			dest[2] = 0xf;
			dest[3] = 0xf;
			dest[4] = 0xf;
			dest[5] = 0xf;
			dest[6] = 0xf;
			dest[7] = 0xf;

			return;
		}
		/* -------------------------------------------------------------------- */
		/*        In the case of of underflow return zero                                */
		/* -------------------------------------------------------------------- */
		else if ((exponent < 0) || ((exponent == 0) && (sign == 0))) {
			fin[0] = 0;
			ByteUtils.doubleToBytes(dbl, dest, fin);
			dest[0] = 0x00;
			dest[1] = 0x00;
			dest[2] = 0x00;
			dest[3] = 0x00;
			dest[4] = 0x00;
			dest[5] = 0x00;
			dest[6] = 0x00;
			dest[7] = 0x00;

			return;
		} else {
			/* -------------------------------------------------------------------- */
			/*            Shift the fraction 3 bits left and set the exponent and sign*/
			/* -------------------------------------------------------------------- */
			System.arraycopy(dest, 0, destlo, 0, 4);
			fin[0] = 0;
			dt.lo = ByteUtils.bytesToLong(destlo, fin);
			System.arraycopy(dest, 4, desthi, 0, 4);
			fin[0] = 0;
			dt.hi = ByteUtils.bytesToLong(desthi, fin);

			dt.hi = dt.hi << 3;
			dt.hi = dt.hi | (dt.lo >> 29);
			dt.hi = dt.hi & 0x007fffff;
			dt.hi = dt.hi | (exponent << 23) | sign;

			dt.lo = dt.lo << 3;
		}

		/* -------------------------------------------------------------------- */
		/*        Convert the double back to VAX format                                */
		/* -------------------------------------------------------------------- */
		fin[0] = 0;

		ByteUtils.longToBytes(dt.lo, srclo, fin);

		fin[0] = 0;
		ByteUtils.longToBytes(dt.hi, srchi, fin);

		if (LSB == TRUE) {
			dest[2] = srclo[0];
			dest[3] = srclo[1];
			dest[0] = srclo[2];
			dest[1] = srclo[3];
			dest[6] = srchi[0];
			dest[7] = srchi[1];
			dest[4] = srchi[2];
			dest[5] = srchi[3];
		} else {
			dest[1] = srclo[0];
			dest[0] = srclo[1];
			dest[3] = srclo[2];
			dest[2] = srclo[3];
			dest[5] = srchi[0];
			dest[4] = srchi[1];
			dest[7] = srchi[2];
			dest[6] = srchi[3];
		}

		fin[0] = 0;
		dbl = ByteUtils.bytesToDouble(dest, fin);

		////System.out.println("dbl=  " + dbl);
	}

	/************************************************************************/
	/*                       DGNElemTypeHasDispHdr()                        */
	/************************************************************************/

	/**
	 * Does element type have display header.
	 *
	 * @param nElemType element type (0-63) to test.
	 *
	 * @return TRUE if elements of passed in type have a display header after
	 * 		   the core element header, or FALSE otherwise.
	 */
	private int DGNElemTypeHasDispHdr(int nElemType) {
		switch (nElemType) {
			case 0:
			case DGNFileHeader.DGNT_TCB:
			case DGNFileHeader.DGNT_CELL_LIBRARY:
			case DGNFileHeader.DGNT_LEVEL_SYMBOLOGY:
			case 32:
			case 44:
			case 48:
			case 49:
			case 50:
			case 51:
			case 57:
			case 63:
				return FALSE;

			default:
				return TRUE;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param info DOCUMENT ME!
	 * @param elemento DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private int DGNParseCore(DGNInfo info, DGNElemCore elemento) {
		byte[] psData = info.abyElem; //0

		elemento.level = psData[0] & (byte) 0x3f;
		elemento.complex = psData[0] & (byte) 0x80;
		elemento.deleted = psData[1] & (byte) 0x80;
		elemento.type = psData[1] & (byte) 0x7f;

		if ((info.nElemBytes >= 36) &&
				(DGNElemTypeHasDispHdr(elemento.type) == TRUE)) {
			// (elemento.type != DGNFileHeader.DGNT_CELL_LIBRARY)) {
			elemento.graphic_group = psData[28] + (psData[29] * 256);
			elemento.properties = psData[32] + (psData[33] * 256);

			elemento.style = psData[34] & (byte) 0x7;
			elemento.weight = (psData[34] & (byte) 0xf8) >> 3;

			byte aux = psData[35];

			// System.out.println("aux = " + aux);
			elemento.color = ByteUtils.getUnsigned(aux);

			// elemento.color = psData[35];
			// System.out.println("elemento.color = " + elemento.color);
		} else {
			elemento.graphic_group = 0;
			elemento.properties = 0;
			elemento.style = 0;
			elemento.weight = 0;
			elemento.color = 0;
		}

		if ((elemento.properties & DGNFileHeader.DGNPF_ATTRIBUTES) != 0) {
			// if ((elemento.properties & DGNFileHeader.DGNPF_ATTRIBUTES) == TRUE) {
			int nAttIndex;

			nAttIndex = ByteUtils.getUnsigned(psData[30]) +
				(ByteUtils.getUnsigned(psData[31]) * 256);

			int numBytes = info.nElemBytes - (nAttIndex * 2) - 32;

			if ((numBytes > 0)) {
				// Como máximo guardamos 10 bytes (Total, lo queremos para el color....
				// if (numBytes > 10) numBytes = 10;
				elemento.attr_bytes = numBytes;

				elemento.attr_data = new byte[elemento.attr_bytes];

				// System.out.println("nAttIndex = " + nAttIndex + " numBytes = " + numBytes );
				System.arraycopy(psData, (nAttIndex * 2) + 32,
					elemento.attr_data, 0, elemento.attr_bytes);
			}
		}

		return TRUE;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param rad50 DOCUMENT ME!
	 * @param str DOCUMENT ME!
	 */
	private void DGNRad50ToAscii(int rad50, byte[] str) {
		byte cTimes;
		int value;
		int temp;
		byte ch = '\0';
		int i = 0;

		while (rad50 > 0) {
			value = rad50;
			cTimes = 0;

			while (value >= 40) {
				value /= 40;
				cTimes++;
			}

			byte[] abc = {
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
					'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
					'Y', 'Z'
				};
			byte[] num = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };

			/* Map 0..39 to ASCII */
			if (value == 0) {
				ch = ' '; /* space */
			} else if ((value >= 1) && (value <= 26)) {
				ch = abc[value - 1]; /* printable alpha A..Z */
			} else if (value == 27) {
				ch = '$'; /* dollar */
			} else if (value == 28) {
				ch = '.'; /* period */
			} else if (value == 29) {
				ch = ' '; /* unused char, emit a space instead */
			} else if ((value >= 30) && (value <= 39)) {
				ch = num[value - 30]; /* digit 0..9 */
			}

			str[i] = ch;
			i++;

			temp = 1;

			while (cTimes-- > 0)
				temp *= 40;

			rad50 -= (value * temp);
		}

		/* Do zero-terminate */
		str[i] = '\0';
	}

	/**
	 * Transforma el punto.
	 *
	 * @param info Información del DGN.
	 * @param psPoint Punto.
	 */
	private void DGNTransformPoint(DGNInfo info, DGNPoint psPoint) {
		psPoint.x = (psPoint.x * info.scale) - info.origin_x;

		// System.out.println("info.scale= "+info.scale+"info.origin_x= "+info.origin_x);
		psPoint.y = (psPoint.y * info.scale) - info.origin_y;

		// System.out.println("info.origin_y= "+info.origin_y);
		psPoint.z = (psPoint.z * info.scale) - info.origin_z;

		// System.out.println("x= "+psPoint.x+"y= "+psPoint.y);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param psDGN DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DGNElemCore DGNParseColorTable(DGNInfo psDGN) {
		DGNElemColorTable psColorTable = new DGNElemColorTable();

		psColorTable.stype = DGNFileHeader.DGNST_COLORTABLE;

		DGNParseCore(psDGN, psColorTable);

		psColorTable.screen_flag = ByteUtils.byteToUnsignedInt(psDGN.abyElem[36]) +
			(ByteUtils.byteToUnsignedInt(psDGN.abyElem[37]) * 256);

		int[] fin = new int[1];
		fin[0] = 0;

		byte[] temp = new byte[3];
		System.arraycopy(psDGN.abyElem, 38, temp, 0, 3);
		psColorTable.color_info[255] = temp;

		byte[] temp2 = new byte[765];
		System.arraycopy(psDGN.abyElem, 41, temp2, 0, 765);

		int k = 0;

		for (int i = 0; i < 255; i++) {
			for (int j = 0; j < 3; j++) {
				psColorTable.color_info[i][j] = temp2[k];
				k++;
			}

			// System.err.println("Color " + psColorTable.color_info[i][0] + " " + 
			//		psColorTable.color_info[i][1] + " " + psColorTable.color_info[i][2]);
		}

		if (psDGN.got_color_table == FALSE) {
			psDGN.color_table = psColorTable.color_info;

			psDGN.got_color_table = 1;
		}

		return psColorTable;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param psDGN DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DGNElemCore DGNParseTCB(DGNInfo psDGN) {
		DGNElemTCB psTCB = new DGNElemTCB();
		int iView;
		int[] fin = new int[1];
		psTCB.stype = DGNFileHeader.DGNST_TCB;
		DGNParseCore(psDGN, psTCB);

		if ((psDGN.abyElem[1214] & (byte) 0x40) != FALSE) {
			psTCB.dimension = 3;
		} else {
			psTCB.dimension = 2;
		}

		// psTCB.dimension = 3;
		fin[0] = 0;

		byte[] temp1 = new byte[8];
		System.arraycopy(psDGN.abyElem, 1112, temp1, 0, 4);
		psTCB.subunits_per_master = DGN_INT32(temp1);

		psTCB.master_units[0] = (char) (psDGN.abyElem[1120]);
		psTCB.master_units[1] = (char) (psDGN.abyElem[1121]);
		psTCB.master_units[2] = '\0';

		System.arraycopy(psDGN.abyElem, 1116, temp1, 0, 4);
		psTCB.uor_per_subunit = DGN_INT32(temp1);

		psTCB.sub_units[0] = (char) (psDGN.abyElem[1122]);
		psTCB.sub_units[1] = (char) (psDGN.abyElem[1123]);
		psTCB.sub_units[2] = '\0';

		/* Get global origin */
		System.arraycopy(psDGN.abyElem, 1240, temp1, 0, 8);

		fin[0] = 0;
		psTCB.origin_x = ByteUtils.bytesToDouble(temp1, fin);

		System.arraycopy(psDGN.abyElem, 1248, temp1, 0, 8);
		fin[0] = 0;
		psTCB.origin_y = ByteUtils.bytesToDouble(temp1, fin);

		fin[0] = 0;
		System.arraycopy(psDGN.abyElem, 1256, temp1, 0, 8);
		fin[0] = 0;
		psTCB.origin_z = ByteUtils.bytesToDouble(temp1, fin);

		/* Transform to IEEE */
		DGNParseIEEE(psTCB.origin_x);
		DGNParseIEEE(psTCB.origin_y);
		DGNParseIEEE(psTCB.origin_z);

		/* Convert from UORs to master units. */
		if ((psTCB.uor_per_subunit != 0) && (psTCB.subunits_per_master != 0)) {
			psTCB.origin_x = psTCB.origin_x / (psTCB.uor_per_subunit * psTCB.subunits_per_master);
			psTCB.origin_y = psTCB.origin_y / (psTCB.uor_per_subunit * psTCB.subunits_per_master);
			psTCB.origin_z = psTCB.origin_z / (psTCB.uor_per_subunit * psTCB.subunits_per_master);
		}

		// psDGN.dimension = psTCB.dimension;
		if (psDGN.got_tcb == FALSE) {
			psDGN.got_tcb = TRUE;

			/* psDGN.origin_x = psTCB.origin_x;
			   psDGN.origin_y = psTCB.origin_y;
			   psDGN.origin_z = psTCB.origin_z; */
			if ((psTCB.uor_per_subunit != 0) &&
					(psTCB.subunits_per_master != 0)) {
				psDGN.scale = 1.0 / (psTCB.uor_per_subunit * psTCB.subunits_per_master);
			}
		}

		/* System.err.println("Ojo: HAY TCB!!");
		   System.out.println("Datos del TCB: " + psTCB.origin_x + " " + psTCB.origin_y);
		   System.out.println("psTCB.uor_per_subunit: " + psTCB.uor_per_subunit + " psTCB.subunits_per_master: " + psTCB.subunits_per_master);
		   // System.out.println("psTCB.master_units = " + new String(psTCB.master_units) +
		   //                 " psTCB.sub_units = " + new String(psTCB.sub_units));
		   System.out.println("psTCB.origen = " + psTCB.origin_x + " " + psTCB.origin_y);
		   System.out.println("psDGN.scale = " + psDGN.scale);
		   System.out.println("psDGN.origen = " + psDGN.origin_x + " " + psDGN.origin_y);
		   System.out.println("psDGN.dimension = " + psDGN.dimension); */
		/* Collect views */
		for (iView = 0; iView < 8; iView++) {
			byte[] pabyRawView = new byte[psDGN.abyElem.length];
			fin[0] = 0;
			System.arraycopy(psDGN.abyElem, (46 + (iView * 118)), pabyRawView,
				0, psDGN.abyElem.length - (46 + (iView * 118)));

			psTCB.views[iView] = new DGNViewInfo();
			psTCB.views[iView].flags = ByteUtils.byteToUnsignedInt(pabyRawView[0]) +
				(ByteUtils.byteToUnsignedInt(pabyRawView[1]) * 256);

			byte[] temp2 = new byte[4];
			int f = 0;

			for (int j = 0; j < 8; j++) {
				System.arraycopy(pabyRawView, j + 2, temp2, 0, 1);
				fin[0] = 0;
				psTCB.views[iView].levels[f] = temp2[0];
				f++;
			}

			psTCB.views[iView].origin = new DGNPoint();
			System.arraycopy(pabyRawView, 10, temp1, 0, 4);
			psTCB.views[iView].origin.x = DGN_INT32(temp1);

			System.arraycopy(pabyRawView, 14, temp1, 0, 4);
			psTCB.views[iView].origin.y = DGN_INT32(temp1);
			System.arraycopy(pabyRawView, 18, temp1, 0, 4);
			psTCB.views[iView].origin.z = DGN_INT32(temp1);
			DGNTransformPoint(psDGN, (psTCB.views[iView].origin));

			psTCB.views[iView].delta = new DGNPoint();
			System.arraycopy(pabyRawView, 22, temp1, 0, 4);
			psTCB.views[iView].delta.x = DGN_INT32(temp1);
			System.arraycopy(pabyRawView, 26, temp1, 0, 4);
			psTCB.views[iView].delta.y = DGN_INT32(temp1);
			System.arraycopy(pabyRawView, 30, temp1, 0, 4);
			psTCB.views[iView].delta.z = DGN_INT32(temp1);

			psTCB.views[iView].delta.x *= psDGN.scale;
			psTCB.views[iView].delta.y *= psDGN.scale;
			psTCB.views[iView].delta.z *= psDGN.scale;

			//memcpy( psTCB.views[iView].transmatrx, pabyRawView + 34, sizeof(double) * 9 );
			psTCB.views[iView].transmatrx = new double[9];

			for (int k = 0; k < 9; k++) {
				System.arraycopy(pabyRawView, (34 + (8 * k)), temp1, 0, 8);

				//fin[0]=0;
				//double d=ByteUtils.bytesToDouble(temp1,fin);
				fin[0] = 0;
				psTCB.views[iView].transmatrx[k] = DGNParseIEEE(temp1);

				//nuevo mÃ©todo
			}

			System.arraycopy(pabyRawView, 106, temp1, 0, 8);
			fin[0] = 0;
			psTCB.views[iView].conversion = ByteUtils.bytesToLong(temp1, fin);
			fin[0] = 0;
			psTCB.views[iView].conversion = ByteUtils.bytesToDouble(temp1, fin);

			//memcpy( (psTCB.views[iView].conversion), pabyRawView + 106, sizeof(double) );
			DGNParseIEEE(psTCB.views[iView].conversion);
			System.arraycopy(pabyRawView, 114, temp1, 0, 8);
			psTCB.views[iView].activez = DGN_INT32(temp1);
		}

		// DGNDumpElement(psDGN, psTCB,"");
		return psTCB;
	}

	/**
	 * Cambia un entero a LitterIndian.
	 *
	 * @param x Entero de entrada.
	 *
	 * @return Entero de salida.
	 */
	private int CPL_LSBWORD32(int x) {
		return ((int) ((((int) (x) & (int) 0x000000ff) << 24) |
		(((int) (x) & (int) 0x0000ff00) << 8) |
		(((int) (x) & (int) 0x00ff0000) >> 8) |
		(((int) (x) & (int) 0xff000000) >> 24)));
	}

	/**
	 * Cambia el vector de char de entrada en el caso de ser null por el
	 * caracter vacio.
	 *
	 * @param pszString Vector de char.
	 *
	 * @return Vector de char.
	 */
	private char[] CPLStrdup(char[] pszString) {
		char[] pszReturn;

		if (pszString == null) {
			pszString[0] = ' ';
		}

		pszReturn = new char[pszString.length];

		//pszReturn = VSIStrdup( pszString );
		pszReturn = pszString;

		if (pszReturn == null) {
			//System.out.println("error");

			/*
			   CPLError( CE_Fatal, CPLE_OutOfMemory,
			                                     "CPLStrdup(): Out of memory allocating %d bytes.\n",
			                                     strlen(pszString) );*/
		}

		return (pszReturn);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param psDGN DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private DGNElemCore DGNParseTagSet(DGNInfo psDGN) {
		//DGNElemCore psElement;
		DGNElemTagSet psTagSet = new DGNElemTagSet();
		int nDataOffset;
		int iTag;

		//psTagSet = (DGNElemTagSet *) CPLCalloc(sizeof(DGNElemTagSet),1);
		//psElement = (DGNElemCore *) psTagSet;
		psTagSet.stype = DGNFileHeader.DGNST_TAG_SET;

		DGNParseCore(psDGN, psTagSet);

		/* -------------------------------------------------------------------- */
		/*      Parse the overall information.                                  */
		/* -------------------------------------------------------------------- */
		psTagSet.tagCount = ByteUtils.byteToUnsignedInt(psDGN.abyElem[44]) +
			(ByteUtils.byteToUnsignedInt(psDGN.abyElem[45]) * 256);
		psTagSet.flags = ByteUtils.byteToUnsignedInt(psDGN.abyElem[46]) +
			(ByteUtils.byteToUnsignedInt(psDGN.abyElem[47]) * 256);

		int[] fin = new int[1];
		fin[0] = 0;

		byte[] temp = new byte[elemento.attr_bytes];
		System.arraycopy(psDGN.abyElem, 48, temp, 0, psDGN.abyElem.length - 48);

		psTagSet.tagSetName = CPLStrdup(ByteUtils.bytesToString(temp, fin)
												 .toCharArray());

		/* -------------------------------------------------------------------- */
		/*      Get the tag set number out of the attributes, if available.     */
		/* -------------------------------------------------------------------- */
		psTagSet.tagSet = -1;

		if ((psTagSet.attr_bytes >= 8) &&
				(psTagSet.attr_data[0] == (byte) 0x03) &&
				(psTagSet.attr_data[1] == (byte) 0x10) &&
				(psTagSet.attr_data[2] == (byte) 0x2f) &&
				(psTagSet.attr_data[3] == (byte) 0x7d)) {
			psTagSet.tagSet = psTagSet.attr_data[4] +
				(psTagSet.attr_data[5] * 256);
		}

		/* -------------------------------------------------------------------- */
		/*      Parse each of the tag definitions.                              */
		/* -------------------------------------------------------------------- */
		//psTagSet.tagList = (DGNTagDef *)	CPLMalloc(sizeof(DGNTagDef) * psTagSet->tagCount);
		nDataOffset = 48 + psTagSet.tagSetName.length + 1 + 1;

		for (iTag = 0; iTag < psTagSet.tagCount; iTag++) {
			DGNTagDef tagDef = new DGNTagDef();

			//= psTagSet.tagList + iTag;
			tagDef.id = iTag;

			//CPLAssert( nDataOffset < psDGN.nElemBytes );
			/* collect tag name. */
			System.arraycopy(psDGN.abyElem, nDataOffset, temp, 0,
				psDGN.abyElem.length);
			fin[0] = 0;
			tagDef.name = CPLStrdup(ByteUtils.bytesToString(temp, fin)
											 .toCharArray());
			nDataOffset += ((tagDef.name.length) + 1);

			/* Get tag id */
			tagDef.id = ByteUtils.byteToUnsignedInt(psDGN.abyElem[nDataOffset]) +
				(ByteUtils.byteToUnsignedInt(psDGN.abyElem[nDataOffset + 1]) * 256);
			nDataOffset += 2;

			/* Get User Prompt */
			System.arraycopy(psDGN.abyElem, nDataOffset, temp, 0,
				psDGN.abyElem.length);
			fin[0] = 0;
			tagDef.prompt = CPLStrdup(ByteUtils.bytesToString(temp, fin)
											   .toCharArray());
			nDataOffset += ((tagDef.prompt.length) + 1);

			/* Get type */
			tagDef.type = ByteUtils.byteToUnsignedInt(psDGN.abyElem[nDataOffset]) +
				(ByteUtils.byteToUnsignedInt(psDGN.abyElem[nDataOffset + 1]) * 256);
			nDataOffset += 2;

			/* skip five zeros */
			nDataOffset += 5;

			/* Get the default */
			if (tagDef.type == 1) {
				System.arraycopy(psDGN.abyElem, nDataOffset, temp, 0,
					psDGN.abyElem.length);
				fin[0] = 0;
				tagDef.defaultValue.string = CPLStrdup(ByteUtils.bytesToString(
							temp, fin).toCharArray());
				nDataOffset += (tagDef.defaultValue.string.length + 1);
			} else if ((tagDef.type == 3) || (tagDef.type == 5)) {
				System.arraycopy(psDGN.abyElem, nDataOffset, temp, 0, 4);
				fin[0] = 0;
				tagDef.defaultValue.integer = ByteUtils.bytesToLong(temp, fin);

				//memcpy( (tagDef.defaultValue.integer),psDGN.abyElem + nDataOffset, 4 );
				tagDef.defaultValue.integer = CPL_LSBWORD32((int) tagDef.defaultValue.integer);
				nDataOffset += 4;
			} else if (tagDef.type == 4) {
				System.arraycopy(psDGN.abyElem, nDataOffset, temp, 0, 8);
				fin[0] = 0;
				tagDef.defaultValue.real = ByteUtils.bytesToDouble(temp, fin);

				//memcpy( (tagDef.defaultValue.real),	psDGN.abyElem + nDataOffset, 8 );
				DGNParseIEEE(tagDef.defaultValue.real);
				nDataOffset += 8;
			} else {
				nDataOffset += 4;
			}
		}

		return psTagSet;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param psDGN DOCUMENT ME!
	 * @param nOptions DOCUMENT ME!
	 */
	private void DGNSetOptions(DGNInfo psDGN, int nOptions) {
		psDGN.options = nOptions;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param psDGN DOCUMENT ME!
	 * @param dfXMin DOCUMENT ME!
	 * @param dfYMin DOCUMENT ME!
	 * @param dfXMax DOCUMENT ME!
	 * @param dfYMax DOCUMENT ME!
	 */
	private void DGNSetSpatialFilter(DGNInfo psDGN, double dfXMin,
		double dfYMin, double dfXMax, double dfYMax) {
		if ((dfXMin == 0.0) && (dfXMax == 0.0) && (dfYMin == 0.0) &&
				(dfYMax == 0.0)) {
			psDGN.has_spatial_filter = FALSE;

			return;
		}

		psDGN.has_spatial_filter = TRUE;
		psDGN.sf_converted_to_uor = FALSE;

		psDGN.sf_min_x_geo = dfXMin;
		psDGN.sf_min_y_geo = dfYMin;
		psDGN.sf_max_x_geo = dfXMax;
		psDGN.sf_max_y_geo = dfYMax;

		DGNSpatialFilterToUOR(psDGN);
	}

	/**
	 * Muestra por consola los elementos que contiene el DGN.
	 *
	 * @param psInfo Información del DGN.
	 * @param psElement elemento
	 * @param fp path del fichero.
	 */
	public void DGNDumpElement(DGNInfo psInfo, DGNElemCore psElement, String fp) {
		//DGNInfo *psInfo = (DGNInfo *) hDGN;
		//System.out.println("\n");
		System.out.println("Element:" + DGNTypeToName(psElement.type) +
			" Level:" + psElement.level + " id:" + psElement.element_id);

		if (psElement.complex != 0) {
			System.out.println("(Complex) ");
		}

		if (psElement.deleted != 0) {
			System.out.println("(DELETED) ");
		}

		System.out.println("  offset=" + psElement.offset + " size=" +
			psElement.size + " bytes\n");

		System.out.println("  graphic_group:" + psElement.graphic_group +
			" color:" + psElement.color + " weight:" + psElement.weight +
			"style:" + psElement.style + "\n");

		if (psElement.properties != 0) {
			int nClass;

			System.out.println("  properties=" + psElement.properties);

			if ((psElement.properties & DGNFileHeader.DGNPF_HOLE) != 0) {
				System.out.println(",HOLE");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_SNAPPABLE) != 0) {
				System.out.println(",SNAPPABLE");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_PLANAR) != 0) {
				System.out.println(",PLANAR");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_ORIENTATION) != 0) {
				System.out.println(",ORIENTATION");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_ATTRIBUTES) != 0) {
				System.out.println(",ATTRIBUTES");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_MODIFIED) != 0) {
				System.out.println(",MODIFIED");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_NEW) != 0) {
				System.out.println(",NEW");
			}

			if ((psElement.properties & DGNFileHeader.DGNPF_LOCKED) != 0) {
				System.out.println(",LOCKED");
			}

			nClass = psElement.properties & DGNFileHeader.DGNPF_CLASS;

			if (nClass == DGNFileHeader.DGNC_PATTERN_COMPONENT) {
				System.out.println(",PATTERN_COMPONENT");
			} else if (nClass == DGNFileHeader.DGNC_CONSTRUCTION_ELEMENT) {
				System.out.println(",CONSTRUCTION ELEMENT");
			} else if (nClass == DGNFileHeader.DGNC_DIMENSION_ELEMENT) {
				System.out.println(",DIMENSION ELEMENT");
			} else if (nClass == DGNFileHeader.DGNC_PRIMARY_RULE_ELEMENT) {
				System.out.println(",PRIMARY RULE ELEMENT");
			} else if (nClass == DGNFileHeader.DGNC_LINEAR_PATTERNED_ELEMENT) {
				System.out.println(",LINEAR PATTERNED ELEMENT");
			} else if (nClass == DGNFileHeader.DGNC_CONSTRUCTION_RULE_ELEMENT) {
				System.out.println(",CONSTRUCTION_RULE_ELEMENT");
			}

			// System.out.println("\n");
		}

		switch (psElement.stype) {
			case DGNFileHeader.DGNST_MULTIPOINT: {
				DGNElemMultiPoint psLine = new DGNElemMultiPoint();
				psLine = (DGNElemMultiPoint) psElement;

				int i;

				for (i = 0; i < psLine.num_vertices; i++)
					System.out.println(psLine.vertices[i].x + "," +
						psLine.vertices[i].y + "," + psLine.vertices[i].z);
			}

			break;

			case DGNFileHeader.DGNST_CELL_HEADER: {
				DGNElemCellHeader psCell = new DGNElemCellHeader();
				psCell = (DGNElemCellHeader) psElement;

				System.out.println("  totlength=" + psCell.totlength +
					", name=" + psCell.name.toString() + " class=" +
					psCell.cclass + " levels=" + psCell.levels[0] +
					psCell.levels[1] + psCell.levels[2] + psCell.levels[3] +
					"\n");
				System.out.println("  rnglow=(" + psCell.rnglow.x + "," +
					psCell.rnglow.y + "), rnghigh=(" + psCell.rnghigh.x + "," +
					psCell.rnghigh.y + ")\n");
				System.out.println("  origin=(" + psCell.origin.x + "," +
					psCell.origin.y + ")\n");
				System.out.println("  xscale=" + psCell.xscale + ",yscale=" +
					psCell.yscale + ", rotation=" + psCell.rotation + "\n");
			}

			break;

			case DGNFileHeader.DGNST_CELL_LIBRARY: {
				DGNElemCellLibrary psCell = new DGNElemCellLibrary();
				psCell = (DGNElemCellLibrary) psElement;

				System.out.println("  name=" + psCell.name.toString() +
					" class=" + psCell.cclass + " levels=" + psCell.levels[0] +
					psCell.levels[1] + psCell.levels[2] + psCell.levels[3] +
					" numwords=" + psCell.numwords + "\n");
				System.out.println("  dispsymb=" + psCell.dispsymb +
					", description=" + psCell.description + "\n");
			}

			break;

			case DGNFileHeader.DGNST_ARC: {
				DGNElemArc psArc = new DGNElemArc();
				psArc = (DGNElemArc) psElement;

				if (psInfo.dimension == 2) {
					System.out.println("  origin=(" + psArc.origin.x + "," +
						psArc.origin.y + "), rotation=" + psArc.rotation +
						"\n");
				} else {
					System.out.println("  origin=(" + psArc.origin.x + "," +
						psArc.origin.y + "," + psArc.origin.z + "), quat=" +
						psArc.quat[0] + "," + psArc.quat[1] + "," +
						psArc.quat[2] + "," + psArc.quat[3] + "\n");
				}

				System.out.println("  axes=(" + psArc.primary_axis + "," +
					psArc.secondary_axis + "), start angle=" + psArc.startang +
					", sweep=" + psArc.sweepang + "\n");
			}

			break;

			case DGNFileHeader.DGNST_TEXT: {
				DGNElemText psText = new DGNElemText();
				psText = (DGNElemText) psElement;

				System.out.println("  origin=(" + psText.origin.x +
					psText.origin.y + ") rotation=" + psText.rotation + "\n" +
					"  font=" + psText.font_id + " just=" +
					psText.justification + "length_mult=" + psText.length_mult +
					" height_mult=" + psText.height_mult + "\n" + "  string =" +
					new String(psText.string).toString() + "\n");
			}

			break;

			case DGNFileHeader.DGNST_COMPLEX_HEADER: {
				DGNElemComplexHeader psHdr = new DGNElemComplexHeader();
				psHdr = (DGNElemComplexHeader) psElement;

				System.out.println("  totlength=" + psHdr.totlength +
					"numelems=" + psHdr.numelems + "\n");
			}

			break;

			case DGNFileHeader.DGNST_COLORTABLE: {
				DGNElemColorTable psCT = new DGNElemColorTable();
				psCT = (DGNElemColorTable) psElement;

				int i;

				System.out.println("  screen_flag:" + psCT.screen_flag + "\n");

				for (i = 0; i < 256; i++) {
					System.out.println(i + ": (" + psCT.color_info[i][0] + "," +
						psCT.color_info[i][1] + "," + psCT.color_info[i][2] +
						")\n");
				}
			}

			break;

			case DGNFileHeader.DGNST_TCB: {
				DGNElemTCB psTCB = new DGNElemTCB();
				psTCB = (DGNElemTCB) psElement;

				int iView;

				//psTCB.dimension=psInfo.dimension;
				System.out.println("  dimension =" + psTCB.dimension + "\n");
				System.out.println("  uor_per_subunit =" +
					psTCB.uor_per_subunit); //+

				// " subunits = `" + psTCB.sub_units[0] + psTCB.sub_units[1] +
				// psTCB.sub_units[2] + "'\n");
				System.out.println("  subunits_per_master =" +
					psTCB.subunits_per_master); //+ " master_units = `" +

				// psTCB.master_units[0] + psTCB.master_units[1] +
				// psTCB.master_units[2] + "'\n");
				System.out.println("  origin = (" + psTCB.origin_x + "," +
					psTCB.origin_y + "," + psTCB.origin_z + ")\n");

				for (iView = 0; iView < 8; iView++) {
					DGNViewInfo psView = psTCB.views[iView];

					//DGNParseTCB(psInfo);
					System.out.println("View  " + iView + ": flags= " +
						Integer.toHexString(psView.flags) + " levels= " +
						psView.levels[0] + "" + psView.levels[1] + "" +
						psView.levels[2] + "" + psView.levels[3] + "" +
						psView.levels[4] + "" + psView.levels[5] + "" +
						psView.levels[6] + "" + psView.levels[7] + "\n");
					System.out.println("origin=( " + psView.origin.x + "," +
						psView.origin.y + "," + psView.origin.z +
						")\n        delta=(" + psView.delta.x + "," +
						psView.delta.y + "," + psView.delta.z + ")\n");

					System.out.println("trans=( " + psView.transmatrx[0] + "," +
						psView.transmatrx[1] + "," + psView.transmatrx[2] +
						"," + psView.transmatrx[3] + "," +
						psView.transmatrx[4] + "," + psView.transmatrx[5] +
						"," + psView.transmatrx[6] + "," +
						psView.transmatrx[7] + "," + psView.transmatrx[8] +
						")\n");
				}
			}

			break;

			case DGNFileHeader.DGNST_TAG_SET: {
				DGNElemTagSet psTagSet = new DGNElemTagSet();
				psTagSet = (DGNElemTagSet) psElement;

				int iTag;

				System.out.println("  tagSetName=" +
					psTagSet.tagSetName.toString() + " tagSet=" +
					psTagSet.tagSet + " tagCount=" + psTagSet.tagCount +
					" flags=" + psTagSet.flags + "\n");

				for (iTag = 0; iTag < psTagSet.tagCount; iTag++) {
					DGNTagDef psTagDef = psTagSet.tagList[iTag];

					System.out.println(psTagDef.id + ": name=" +
						psTagDef.name.toString() + ", type=" + psTagDef.type +
						", prompt=" + psTagDef.prompt.toString());

					if (psTagDef.type == 1) {
						System.out.println(", default=" +
							psTagDef.defaultValue.string.toString() + "\n");
					} else if ((psTagDef.type == 3) || (psTagDef.type == 5)) {
						System.out.println(", default=" +
							psTagDef.defaultValue.integer + "\n");
					} else if (psTagDef.type == 4) {
						System.out.println(", default=" +
							psTagDef.defaultValue.real + "\n");
					} else {
						System.out.println(", default=<unknown>\n");
					}
				}
			}

			break;

			case DGNFileHeader.DGNST_TAG_VALUE: {
				DGNElemTagValue psTag = new DGNElemTagValue();
				psTag = (DGNElemTagValue) psElement;

				System.out.println("  tagType=" + psTag.tagType + ", tagSet=" +
					psTag.tagSet + ", tagIndex=" + psTag.tagIndex +
					", tagLength=" + psTag.tagLength + "\n");

				if (psTag.tagType == 1) {
					System.out.println("  value=" +
						psTag.tagValue.string.toString() + "\n");
				} else if (psTag.tagType == 3) {
					System.out.println("  value=" + psTag.tagValue.integer +
						"\n");
				} else if (psTag.tagType == 4) {
					System.out.println("  value=" + psTag.tagValue.real + "\n");
				}
			}

			break;

			case DGNFileHeader.DGNST_GROUP_DATA: {
				// TODO
			}

			break;

			default:
				break;
		}

		if (psElement.attr_bytes > 0) {
			int iLink;

			System.out.println("Attributes:" + psElement.attr_bytes + "\n");

			for (iLink = 0; true; iLink++) {
				int[] nLinkType = new int[1];
				int[] nEntityNum = new int[1];
				int[] nMSLink = new int[1];
				int[] nLinkSize = new int[1];
				int i;
				nEntityNum[0] = 0;
				nMSLink[0] = 0;

				byte[] pabyData;

				pabyData = DGNGetLinkage(psInfo, psElement, iLink, nLinkType,
						nEntityNum, nMSLink, nLinkSize);

				if (pabyData == null) {
					break;
				}

				System.out.println("Type=0x" + nLinkType);

				if ((nMSLink[0] != 0) || (nEntityNum[0] != 0)) {
					System.out.println(", EntityNum=" + nEntityNum[0] +
						", MSLink=" + nMSLink[0]);
				}

				System.out.println("\n  0x");

				for (i = 0; i < nLinkSize[0]; i++)
					System.out.println(pabyData[i]);

				System.out.println("\n");
			}
		}
	}

	/**
	 * Returns requested linkage raw data.  A pointer to the raw data for the
	 * requested attribute linkage is returned as well as (potentially)
	 * various information about the linkage including the linkage type,
	 * database entity number and MSLink value, and the length of the raw
	 * linkage data in bytes. If the requested linkage (iIndex) does not exist
	 * a value of zero is  returned. The entity number is (loosely speaking)
	 * the index of the table within the current database to which the MSLINK
	 * value will refer.  The entity number should be used to lookup the table
	 * name in the MSCATALOG table.  The MSLINK value is the key value for the
	 * record in the target table.
	 *
	 * @param psDGN the file from which the element originated.
	 * @param psElement the element to report on.
	 * @param iIndex the zero based index of the linkage to fetch.
	 * @param pnLinkageType variable to return linkage type.  This may be one
	 * 		  of the predefined DGNLT_ values or a different value. This
	 * 		  pointer may be NULL.
	 * @param pnEntityNum variable to return the entity number in or NULL if
	 * 		  not required.
	 * @param pnMSLink variable to return the MSLINK value in, or NULL if not
	 * 		  required.
	 * @param pnLength variable to returned the linkage size in bytes or NULL.
	 *
	 * @return pointer to raw internal linkage data.  This data should not be
	 * 		   altered or freed.  NULL returned on failure.
	 */
	private byte[] DGNGetLinkage(DGNInfo psDGN, DGNElemCore psElement,
		int iIndex, int[] pnLinkageType, int[] pnEntityNum, int[] pnMSLink,
		int[] pnLength) {
		int nAttrOffset;
		int iLinkage;
		int nLinkSize;

		for (iLinkage = 0, nAttrOffset = 0;
				(nLinkSize = DGNGetAttrLinkSize(psDGN, psElement, nAttrOffset)) != 0;
				iLinkage++, nAttrOffset += nLinkSize) {
			if (iLinkage == iIndex) {
				int nLinkageType = 0;
				int nEntityNum = 0;
				int nMSLink = 0;

				//CPLAssert( nLinkSize > 4 );
				if ((psElement.attr_data[nAttrOffset + 0] == (byte) 0x00) &&
						((psElement.attr_data[nAttrOffset + 1] == (byte) 0x00) ||
						(psElement.attr_data[nAttrOffset + 1] == (byte) 0x80))) {
					nLinkageType = DGNFileHeader.DGNLT_DMRS;
					nEntityNum = psElement.attr_data[nAttrOffset + 2] +
						(psElement.attr_data[nAttrOffset + 3] * 256);
					nMSLink = psElement.attr_data[nAttrOffset + 4] +
						(psElement.attr_data[nAttrOffset + 5] * 256) +
						(psElement.attr_data[nAttrOffset + 6] * 65536);
				} else {
					nLinkageType = psElement.attr_data[nAttrOffset + 2] +
						(psElement.attr_data[nAttrOffset + 3] * 256);
				}

				// Possibly an external database linkage?
				if ((nLinkSize == 16) &&
						(nLinkageType != DGNFileHeader.DGNLT_SHAPE_FILL)) {
					nEntityNum = psElement.attr_data[nAttrOffset + 6] +
						(psElement.attr_data[nAttrOffset + 7] * 256);
					nMSLink = psElement.attr_data[nAttrOffset + 8] +
						(psElement.attr_data[nAttrOffset + 9] * 256) +
						(psElement.attr_data[nAttrOffset + 10] * 65536) +
						(psElement.attr_data[nAttrOffset + 11] * 65536 * 256);
				}

				if (pnLinkageType != null) {
					pnLinkageType[0] = nLinkageType;
				}

				if (pnEntityNum != null) {
					pnEntityNum[0] = nEntityNum;
				}

				if (pnMSLink != null) {
					pnMSLink[0] = nMSLink;
				}

				if (pnLength != null) {
					pnLength[0] = nLinkSize;
				}

				byte[] temp = new byte[psElement.attr_data.length -
					nAttrOffset];
				System.arraycopy(psElement.attr_data, nAttrOffset, temp, 0,
					psElement.attr_data.length - nAttrOffset);

				return temp;
			}
		}

		return null;
	}

	/**
	 * Devuelve el tamaño de los atributos de un elemento.
	 *
	 * @param psDGN Información del DGN.
	 * @param psElement elemento.
	 * @param nOffset indice donde se encuentra el elemento.
	 *
	 * @return Entero que representa el Tamaño.
	 */
	private int DGNGetAttrLinkSize(DGNInfo psDGN, DGNElemCore psElement,
		int nOffset) {
		if (psElement.attr_bytes < (nOffset + 4)) {
			return 0;
		}

		/* DMRS Linkage */
		if (((psElement.attr_data[nOffset + 0] == 0) &&
				(psElement.attr_data[nOffset + 1] == 0)) ||
				((psElement.attr_data[nOffset + 0] == 0) &&
				(psElement.attr_data[nOffset + 1] == (byte) 0x80))) {
			return 8;
		}

		/* If low order bit of second byte is set, first byte is length */
		if ((psElement.attr_data[nOffset + 1] & (byte) 0x10) != FALSE) {
			return (psElement.attr_data[nOffset + 0] * 2) + 2;
		}

		/* unknown */
		return 0;
	}

	/**
	 * A partir de un entero devuelve el un String con el tipo del elemento.
	 *
	 * @param nType tipo.
	 *
	 * @return String con el nombre del elemento.
	 */
	private String DGNTypeToName(int nType) {
		//char[]	szNumericResult=new char[16];
		switch (nType) {
			case DGNFileHeader.DGNT_CELL_LIBRARY:
				return "Cell Library";

			case DGNFileHeader.DGNT_CELL_HEADER:
				return "Cell Header";

			case DGNFileHeader.DGNT_LINE:
				return "Line";

			case DGNFileHeader.DGNT_LINE_STRING:
				return "Line String";

			case DGNFileHeader.DGNT_GROUP_DATA:
				return "Group Data";

			case DGNFileHeader.DGNT_SHAPE:
				return "Shape";

			case DGNFileHeader.DGNT_TEXT_NODE:
				return "Text Node";

			case DGNFileHeader.DGNT_DIGITIZER_SETUP:
				return "Digitizer Setup";

			case DGNFileHeader.DGNT_TCB:
				return "TCB";

			case DGNFileHeader.DGNT_LEVEL_SYMBOLOGY:
				return "Level Symbology";

			case DGNFileHeader.DGNT_CURVE:
				return "Curve";

			case DGNFileHeader.DGNT_COMPLEX_CHAIN_HEADER:
				return "Complex Chain Header";

			case DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER:
				return "Complex Shape Header";

			case DGNFileHeader.DGNT_ELLIPSE:
				return "Ellipse";

			case DGNFileHeader.DGNT_ARC:
				return "Arc";

			case DGNFileHeader.DGNT_TEXT:
				return "Text";

			case DGNFileHeader.DGNT_BSPLINE:
				return "B-Spline";

			case DGNFileHeader.DGNT_APPLICATION_ELEM:
				return "Application Element";

			case DGNFileHeader.DGNT_SHARED_CELL_DEFN:
				return "Shared Cell Definition";

			case DGNFileHeader.DGNT_SHARED_CELL_ELEM:
				return "Shared Cell Element";

			case DGNFileHeader.DGNT_TAG_VALUE:
				return "Tag Value";

			default:
				System.out.println(nType);

				return "Fallo";
		}
	}

	/**
	 * Muestra por consola la fila de un elemento.
	 *
	 * @param psDGN información del DGN.
	 * @param psCore elemento
	 * @param fpOut path del fichero DGN.
	 */
	private void DGNDumpRawElement(DGNInfo psDGN, DGNElemCore psCore,
		String fpOut) {
		int i;
		int iChar = 0;
		byte[] szLine = new byte[80];

		//System.out.println("  Raw Data (" + psCore.raw_bytes + " bytes):\n");
		for (i = 0; i < psCore.raw_bytes; i++) {
			byte[] szHex = new byte[3];

			if ((i % 16) == 0) {
				int[] f = new int[1];
				byte[] temp = new byte[4];
				f[0] = 0;
				ByteUtils.intToBytes(i, temp, f);
				System.arraycopy(temp, 0, szLine, 0, 4);

				////System.out.println( szLine.toString()+","+ i );
				iChar = 0;
			}

			szHex[0] = psCore.raw_data[i];
			szHex[1] = (byte) 0x00;

			//System.arraycopy(psCore.raw_data,0,szHex,0,3);
			////System.out.println( szHex.toString()+","+psCore.raw_data.toString() );
			//strncpy( szLine+8+iChar*2, szHex, 2 );/**no se */
			System.arraycopy(szHex, 0, szLine, 8 + (iChar * 2), 2);

			if ((psCore.raw_data[i] < 32) || (psCore.raw_data[i] > 127)) {
				szLine[42 + iChar] = '.';
			} else {
				szLine[42 + iChar] = psCore.raw_data[i];
			}

			if ((i == (psCore.raw_bytes - 1)) || (((i + 1) % 16) == 0)) {
				////System.out.println(szLine.toString()+"\n" );
				int[] f = new int[1];
				f[0] = 0;

				//ByteUtils.bytesToInt(szLine,f);
				//System.out.println(ByteUtils.bytesToInt(szLine, f) + " : ");
				//ByteUtils.print_bytes(szLine,8,74);
				byte[] temp = new byte[1];
				byte[] temp1 = new byte[16];
				int k = 0;

				for (int j = 1; j < 32; j = j + 2) {
					System.arraycopy(szLine, j + 7, temp, 0, 1);
					temp1[k] = temp[0];
					k++;
				}

				//System.out.println(ByteUtils.print_bytes(temp1));
				////System.out.println(ByteUtils.print_bytes(szLine,7,41));
				f[0] = 42;

				char[] tempchar = new char[16];

				for (int j = 0; j < 16; j++) {
					tempchar[j] = (char) szLine[42 + j];
				}

				//System.out.println(String.copyValueOf(tempchar));
			}

			iChar++;
		}
	}

	/**
	 * Devuelve el extent del elemento.
	 *
	 * @param psDGN Información del DGN.
	 * @param padfExtents doubles que representan el rectángulo del extent.
	 *
	 * @return Entero que muestra si se ha calculado correctamente.
	 */
	private int DGNGetExtents(DGNInfo psDGN, double[] padfExtents) {
		//DGNInfo	*psDGN = (DGNInfo *) hDGN;
		DGNPoint sMin = new DGNPoint();
		DGNPoint sMax = new DGNPoint();

		DGNBuildIndex(psDGN);

		if (psDGN.got_bounds == FALSE) {
			return FALSE;
		}

		double minX = psDGN.min_x;
		double minY = psDGN.min_y;
		double minZ = psDGN.min_z;
		//System.out.println("psDGN.min" + " x= " + psDGN.min_x + " y= " + psDGN.min_y);

		if (minX < 0) {
			minX = minX + (2 * ((long) Integer.MAX_VALUE + 1));
			//System.out.println("minX" + minX);
		}

		if (minY < 0) {
			minY = minY + (2 * ((long) Integer.MAX_VALUE + 1));
			//System.out.println("minY" + minY);
		}

		if (minZ < 0) {
			minZ = minZ + (2 * ((long) Integer.MAX_VALUE + 1));
		}

		sMin.x = minX - 2147483648.0;
		sMin.y = minY - 2147483648.0;
		sMin.z = minZ - 2147483648.0;

		DGNTransformPoint(psDGN, sMin);

		padfExtents[0] = sMin.x;
		padfExtents[1] = sMin.y;
		padfExtents[2] = sMin.z;

		double maxX = psDGN.max_x;
		double maxY = psDGN.max_y;
		double maxZ = psDGN.max_z;

		//System.out.println("psDGN.max"+ " x= "+psDGN.max_x+" y= "+psDGN.max_y);
		if (maxX < 0) {
			maxX = maxX + (2 * ((long) Integer.MAX_VALUE + 1));

			//System.out.println("maxX"+maxX);
		}

		if (maxY < 0) {
			maxY = maxY + (2 * ((long) Integer.MAX_VALUE + 1));

			//System.out.println("maxY"+maxY);
		}

		if (maxZ < 0) {
			maxZ = maxZ + (2 * ((long) Integer.MAX_VALUE + 1));
		}

		sMax.x = maxX - 2147483648.0;
		sMax.y = maxY - 2147483648.0;
		sMax.z = maxZ - 2147483648.0;

		DGNTransformPoint(psDGN, sMax);

		padfExtents[3] = sMax.x;
		padfExtents[4] = sMax.y;
		padfExtents[5] = sMax.z;

		return TRUE;
	}

	/**
	 * Construye un índice al DGN.
	 *
	 * @param psDGN Información del DGN.
	 */
	private void DGNBuildIndex(DGNInfo psDGN) {
		DGNElemCore elemento = new DGNElemCore();
		int nMaxElements;
		int nType;
		int nLevel;
		long nLastOffset;

		if (psDGN.index_built != FALSE) {
			return;
		}

		psDGN.index_built = TRUE;

		//DGNRewind( psDGN );
		nMaxElements = 0;

		//nLastOffset = VSIFTell( psDGN.fp );
		nLastOffset = bb.position(); //psDGN.ftall;

		while (DGNLoadRawElement(psDGN, elemento) != FALSE) {
			DGNElementInfo psEI; // = new DGNElementInfo();

			if (psDGN.element_count == nMaxElements) {
				int oldMax = nMaxElements;
				nMaxElements = (int) (nMaxElements * 1.5) + 500;

				DGNElementInfo[] nuevo = new DGNElementInfo[nMaxElements];

				for (int i = 0; i < oldMax; i++)
					nuevo[i] = psDGN.element_index[i];

				psDGN.element_index = nuevo;

				//psDGN.element_index = (DGNElementInfo *)	CPLRealloc( psDGN.element_index,
				//				nMaxElements * sizeof(DGNElementInfo) );
			}

			psDGN.element_index[psDGN.element_count] = new DGNElementInfo();
			psEI = psDGN.element_index[psDGN.element_count];
			psEI.level = elemento.level;
			psEI.type = elemento.type;
			psEI.flags = 0;
			psEI.offset = nLastOffset;

			if ((psDGN.abyElem[0] * (byte) 0x80) == (byte) 0x80) {
				psEI.flags |= DGNFileHeader.DGNEIF_COMPLEX;
			}

			if ((psDGN.abyElem[1] * (byte) 0x80) == (byte) 0x80) {
				psEI.flags |= DGNFileHeader.DGNEIF_DELETED;
			}

			if ((elemento.type == DGNFileHeader.DGNT_LINE) ||
					(elemento.type == DGNFileHeader.DGNT_LINE_STRING) ||
					(elemento.type == DGNFileHeader.DGNT_SHAPE) ||
					(elemento.type == DGNFileHeader.DGNT_CURVE) ||
					(elemento.type == DGNFileHeader.DGNT_BSPLINE)) {
				psEI.stype = DGNFileHeader.DGNST_MULTIPOINT;
			} else if ((elemento.type == DGNFileHeader.DGNT_GROUP_DATA) &&
					(elemento.level == DGNFileHeader.DGN_GDL_COLOR_TABLE)) {
				DGNElemCore psCT = DGNParseColorTable(psDGN);

				//DGNFreeElement( psDGN, psCT );
				//System.err.println("TABLA DE COLORES!!");
				psEI.stype = DGNFileHeader.DGNST_COLORTABLE;
				m_colorTable = (DGNElemColorTable) psCT;

				// DGNDumpElement(psDGN,psCT,"");
			} else if ((elemento.type == DGNFileHeader.DGNT_ELLIPSE) ||
					(elemento.type == DGNFileHeader.DGNT_ARC)) {
				psEI.stype = DGNFileHeader.DGNST_ARC;
			} else if ((elemento.type == DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER) ||
					(elemento.type == DGNFileHeader.DGNT_COMPLEX_CHAIN_HEADER)) {
				psEI.stype = DGNFileHeader.DGNST_COMPLEX_HEADER;
			} else if (elemento.type == DGNFileHeader.DGNT_TEXT) {
				psEI.stype = DGNFileHeader.DGNST_TEXT;
			} else if (elemento.type == DGNFileHeader.DGNT_TAG_VALUE) {
				psEI.stype = DGNFileHeader.DGNST_TAG_VALUE;
			} else if (elemento.type == DGNFileHeader.DGNT_APPLICATION_ELEM) {
				if (elemento.level == 24) {
					psEI.stype = DGNFileHeader.DGNST_TAG_SET;
				} else {
					psEI.stype = DGNFileHeader.DGNST_CORE;
				}
			} else if (elemento.type == DGNFileHeader.DGNT_TCB) {
				DGNElemCore psTCB = DGNParseTCB(psDGN);

				//DGNFreeElement( psDGN, psTCB );
				psEI.stype = DGNFileHeader.DGNST_TCB;
			} else {
				psEI.stype = DGNFileHeader.DGNST_CORE;
			}

			//DGNInfo tempo = new DGNInfo();
			double[] anRegion;

			if (((psEI.flags & DGNFileHeader.DGNEIF_DELETED) == FALSE) &
					((psEI.flags & DGNFileHeader.DGNEIF_COMPLEX) == FALSE) &
					((anRegion = DGNGetRawExtents(psDGN, null, elemento)) != null)) {
				//	#ifdef notdef
				// System.out.println( "element_count"+psDGN.element_count+"anRegion[]"+("xmin"+anRegion[0])+"xmax"+anRegion[1]+"ymin"+anRegion[3]+"ymax"+anRegion[4]+ 2147483648.0 );
				//#endif

				/*        double aux=0;
				   if (anRegion[0]>anRegion[3]){
				           aux=anRegion[3];
				           anRegion[3]=anRegion[0];
				           anRegion[0]=anRegion[3];
				   }
				   if (anRegion[1]>anRegion[4]){
				                                           aux=anRegion[4];
				                                           anRegion[4]=anRegion[1];
				                                           anRegion[1]=anRegion[4];
				                                   }*/
				if (psDGN.got_bounds != FALSE) {
					psDGN.min_x = Math.min(psDGN.min_x, anRegion[0]);
					psDGN.min_y = Math.min(psDGN.min_y, anRegion[1]);
					psDGN.min_z = Math.min(psDGN.min_z, anRegion[2]);
					psDGN.max_x = Math.max(psDGN.max_x, anRegion[3]);
					psDGN.max_y = Math.max(psDGN.max_y, anRegion[4]);
					psDGN.max_z = Math.max(psDGN.max_z, anRegion[5]);
				} else {
					psDGN.min_x = anRegion[0];
					psDGN.min_y = anRegion[1];
					psDGN.min_z = anRegion[2];
					psDGN.max_x = anRegion[3];
					psDGN.max_y = anRegion[4];
					psDGN.max_z = anRegion[5];

					//memcpy( (psDGN.min_x), anRegion, sizeof(long) * 6 );
					psDGN.got_bounds = TRUE;
				}

				// System.out.println("xmin"+anRegion[0]+"xmax"+anRegion[3]+"ymin"+anRegion[1]+"ymax"+anRegion[4]+ "      "+2147483648.0 );
			}

			psDGN.element_count++;

			//nLastOffset = VSIFTell( psDGN.fp );
			nLastOffset = bb.position(); // psDGN.ftall;
		}

		//DGNRewind( psDGN );
		psDGN.max_element_count = nMaxElements;
	}

	/**
	 * Devuelve el rectángulo que representa el extent del elemento.
	 *
	 * @param psDGN información del DGN.
	 * @param psElement elemento.
	 * @param psMin punto mínimo.
	 * @param psMax punto máximo.
	 *
	 * @return Entero para comprobar si se ha calculado correctamente.
	 */
	private int DGNGetElementExtents(DGNInfo psDGN, DGNElemCore psElement,
		DGNPoint psMin, DGNPoint psMax) {
		//DGNInfo	*psDGN = (DGNInfo *) hDGN;
		long[] anMin = new long[3];
		long[] anMax = new long[3];
		DGNInfo tempo = new DGNInfo();
		double[] bResult;

		/* -------------------------------------------------------------------- */
		/*      Get the extents if we have raw data in the element, or          */
		/*      loaded in the file buffer.                                      */
		/* -------------------------------------------------------------------- */
		if (psElement.raw_data != null) {
			bResult = DGNGetRawExtents(psDGN, psElement.raw_data, psElement);
		} else {
			if (psElement.element_id == (psDGN.next_element_id - 1)) {
				bResult = DGNGetRawExtents(psDGN, psDGN.abyElem, psElement);
			} else {
				/*CPLError(CE_Warning, CPLE_AppDefined,
				   "DGNGetElementExtents() fails because the requested element\n"
				   " does not have raw data available." );
				 */
				return FALSE;
			}
		}

		if (bResult == null) {
			return FALSE;
		}

		/* -------------------------------------------------------------------- */
		/*      Transform to user coordinate system and return.  The offset     */
		/*      is to convert from "binary offset" form to twos complement.     */
		/* -------------------------------------------------------------------- */
		psMin.x = tempo.min_x - 2147483648.0;
		psMin.y = tempo.min_y - 2147483648.0;
		psMin.z = tempo.min_z - 2147483648.0;

		psMax.x = tempo.max_x - 2147483648.0;
		psMax.y = tempo.max_y - 2147483648.0;
		psMax.z = tempo.max_z - 2147483648.0;

		DGNTransformPoint(psDGN, psMin);
		DGNTransformPoint(psDGN, psMax);

		return TRUE;
	}

	/**
	 * Devuelve los índices de los elementos del DGN.
	 *
	 * @param psDGN Información del DGN.
	 * @param pnElementCount Número de elementos.
	 *
	 * @return Índices.
	 */
	private DGNElementInfo[] DGNGetElementIndex(DGNInfo psDGN,
		int[] pnElementCount) {
		//DGNInfo	*psDGN = (DGNInfo *) hDGN;
		DGNBuildIndex(psDGN);

		if (pnElementCount[0] != -1) {
			pnElementCount[0] = psDGN.element_count;
		}

		return psDGN.element_index;
	}

	/************************************************************************/
	/*                           DGNLookupColor()                           */
	/************************************************************************/

	/**
	 * Translate color index into RGB values. If no color table has yet been
	 * encountered in the file a hard-coded "default" color table will be
	 * used.  This seems to be what Microstation uses as a color table when
	 * there isn't one in a DGN file but I am not absolutely convinced it is
	 * appropriate.
	 *
	 * @param color_index the color index to lookup.
	 *
	 * @return TRUE on success or FALSE on failure.  May fail if color_index is
	 * 		   out of range.
	 */
	public Color DGNLookupColor(int color_index) {
		int r;
		int g;
		int b;

		if ((color_index < 0) || (color_index > 255)) {
			return null;
		}

		if (info.got_color_table == 0) {
			r = abyDefaultPCT[color_index][0];
			g = abyDefaultPCT[color_index][1];
			b = abyDefaultPCT[color_index][2];
		} else {
			r = ByteUtils.getUnsigned(m_colorTable.color_info[color_index][0]);
			g = ByteUtils.getUnsigned(m_colorTable.color_info[color_index][1]);
			b = ByteUtils.getUnsigned(m_colorTable.color_info[color_index][2]);
		}
		if ((r == 255) && (g == 255) && (b == 255)) {
			r = g = b = 0; // El color blanco lo devolvemos como negro.
		}

		return new Color(r, g, b);
	}

	/************************************************************************/
	/*                        DGNGetShapeFillInfo()                         */
	/************************************************************************/

	/**
	 * Fetch fill color for a shape. This method will check for a 0x0041 user
	 * attribute linkaged with fill color information for the element.  If
	 * found the function returns TRUE, and places the fill color in pnColor,
	 * otherwise FALSE is returned and pnColor is not updated.
	 *
	 * @param psElem the element.
	 *
	 * @return index of color on success or -1 on failure.
	 */
	public int DGNGetShapeFillInfo(DGNElemCore psElem) {
		int iLink;
		int color_index = -1;

		for (iLink = 0; true; iLink++) {
			int[] nLinkType = new int[1];
			int[] nEntityNum = new int[1];
			int[] nMSLink = new int[1];
			int[] nLinkSize = new int[1];
			int i;
			nEntityNum[0] = 0;
			nMSLink[0] = 0;

			byte[] pabyData;

			pabyData = DGNGetLinkage(info, psElem, iLink, nLinkType,
					nEntityNum, nMSLink, nLinkSize);

			if (pabyData == null) {
				return -1;
			}

			if ((nLinkType[0] == DGNFileHeader.DGNLT_SHAPE_FILL) &&
					(nLinkSize[0] >= 7)) {
				color_index = ByteUtils.getUnsigned(pabyData[8]);

				break;
			}
		}

		return color_index;
	}

	/************************************************************************/
	/*                        DGNGetAssocID()                               */
	/************************************************************************/

	/**
	 * Fetch association id for an element. This method will check if an
	 * element has an association id, and if so returns it, otherwise
	 * returning -1.  Association ids are kept as a user attribute linkage
	 * where present.
	 *
	 * @param psElem the element.
	 *
	 * @return The id or -1 on failure.
	 */
	int DGNGetAssocID(DGNElemCore psElem) {
		int iLink;

		for (iLink = 0; true; iLink++) {
			int[] nLinkType = new int[1];
			int[] nEntityNum = new int[1];
			int[] nMSLink = new int[1];
			int[] nLinkSize = new int[1];
			int i;
			nEntityNum[0] = 0;
			nMSLink[0] = 0;

			byte[] pabyData;

			pabyData = DGNGetLinkage(info, psElem, iLink, nLinkType,
					nEntityNum, nMSLink, nLinkSize);

			if (pabyData == null) {
				return -1;
			}

			if ((nLinkType[0] == DGNFileHeader.DGNLT_ASSOC_ID) &&
					(nLinkSize[0] >= 8)) {
				return ByteUtils.getUnsigned(pabyData[4]) +
				(ByteUtils.getUnsigned(pabyData[5]) * 256) +
				(ByteUtils.getUnsigned(pabyData[6]) * 256 * 256) +
				(ByteUtils.getUnsigned(pabyData[7]) * 256 * 256 * 256);
			}
		}
	}
}
