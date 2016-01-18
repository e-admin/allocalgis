/*
 * Created on 16-jul-2003
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

import java.nio.MappedByteBuffer;


/**
 * Clase Header del DGN.
 *
 * @author Vicente Caballero Navarro
 */
public class DGNFileHeader {
	/** DGNElemCore style: Element uses DGNElemCore structure */
	public static final int DGNST_CORE = 1;

	/** DGNElemCore style: Element uses DGNElemMultiPoint structure */
	public static final int DGNST_MULTIPOINT = 2;

	/** DGNElemCore style: Element uses DGNElemColorTable structure */
	public static final int DGNST_COLORTABLE = 3;

	/** DGNElemCore style: Element uses DGNElemTCB structure */
	public static final int DGNST_TCB = 4;

	/** DGNElemCore style: Element uses DGNElemArc structure */
	public static final int DGNST_ARC = 5;

	/** DGNElemCore style: Element uses DGNElemText structure */
	public static final int DGNST_TEXT = 6;

	/** DGNElemCore style: Element uses DGNElemComplexHeader structure */
	public static final int DGNST_COMPLEX_HEADER = 7;

	/** DGNElemCore style: Element uses DGNElemCellHeader structure */
	public static final int DGNST_CELL_HEADER = 8;

	/** DGNElemCore style: Element uses DGNElemTagValue structure */
	public static final int DGNST_TAG_VALUE = 9;

	/** DGNElemCore style: Element uses DGNElemTagSet structure */
	public static final int DGNST_TAG_SET = 10;

	/** DGNElemCore style: Element uses DGNElemCellLibrary structure */
	public static final int DGNST_CELL_LIBRARY = 11;

	/** DGNElemCore style: Element uses DGNElemGroup structure */
	public static final int DGNST_GROUP_DATA = 12;
	public static final int DGNST_SHARED_CELL_DEFN = 13;

	/* -------------------------------------------------------------------- */
	/*      Element types                                                   */
	/* -------------------------------------------------------------------- */
	public static final int DGNT_NULL = 0;
	public static final int DGNT_CELL_LIBRARY = 1;
	public static final int DGNT_CELL_HEADER = 2;
	public static final int DGNT_LINE = 3;
	public static final int DGNT_LINE_STRING = 4;
	public static final int DGNT_GROUP_DATA = 5;
	public static final int DGNT_SHAPE = 6;
	public static final int DGNT_TEXT_NODE = 7;
	public static final int DGNT_DIGITIZER_SETUP = 8;
	public static final int DGNT_TCB = 9;
	public static final int DGNT_LEVEL_SYMBOLOGY = 10;
	public static final int DGNT_CURVE = 11;
	public static final int DGNT_COMPLEX_CHAIN_HEADER = 12;
	public static final int DGNT_COMPLEX_SHAPE_HEADER = 14;
	public static final int DGNT_ELLIPSE = 15;
	public static final int DGNT_ARC = 16;
	public static final int DGNT_TEXT = 17;
	public static final int DGNT_BSPLINE = 21;
	public static final int DGNT_SHARED_CELL_DEFN = 34;
	public static final int DGNT_SHARED_CELL_ELEM = 35; // REVISAR
	public static final int DGNT_TAG_VALUE = 37;
	public static final int DGNT_APPLICATION_ELEM = 66;

	/* -------------------------------------------------------------------- */
	/*      Line Styles                                                     */
	/* -------------------------------------------------------------------- */
	public static final int DGNS_SOLID = 0;
	public static final int DGNS_DOTTED = 1;
	public static final int DGNS_MEDIUM_DASH = 2;
	public static final int DGNS_LONG_DASH = 3;
	public static final int DGNS_DOT_DASH = 4;
	public static final int DGNS_SHORT_DASH = 5;
	public static final int DGNS_DASH_DOUBLE_DOT = 6;
	public static final int DGNS_LONG_DASH_SHORT_DASH = 7;

	/* -------------------------------------------------------------------- */
	/*      Class                                                           */
	/* -------------------------------------------------------------------- */
	public static final int DGNC_PRIMARY = 0;
	public static final int DGNC_PATTERN_COMPONENT = 1;
	public static final int DGNC_CONSTRUCTION_ELEMENT = 2;
	public static final int DGNC_DIMENSION_ELEMENT = 3;
	public static final int DGNC_PRIMARY_RULE_ELEMENT = 4;
	public static final int DGNC_LINEAR_PATTERNED_ELEMENT = 5;
	public static final int DGNC_CONSTRUCTION_RULE_ELEMENT = 6;

	/* -------------------------------------------------------------------- */
	/*      Group Data level numbers.                                       */
	/*                                                                      */
	/*      These are symbolic values for the typ 5 (DGNT_GROUP_DATA)       */
	/*      level values that have special meanings.                        */
	/* -------------------------------------------------------------------- */
	public static final int DGN_GDL_COLOR_TABLE = 1;
	public static final int DGN_GDL_NAMED_VIEW = 3;
	public static final int DGN_GDL_REF_FILE = 9;

	/* -------------------------------------------------------------------- */
	/*      Word 17 property flags.                                         */
	/* -------------------------------------------------------------------- */
	public static final int DGNPF_HOLE = 0x8000;
	public static final int DGNPF_SNAPPABLE = 0x4000;
	public static final int DGNPF_PLANAR = 0x2000;
	public static final int DGNPF_ORIENTATION = 0x1000;
	public static final int DGNPF_ATTRIBUTES = 0x0800;
	public static final int DGNPF_MODIFIED = 0x0400;
	public static final int DGNPF_NEW = 0x0200;
	public static final int DGNPF_LOCKED = 0x0100;
	public static final int DGNPF_CLASS = 0x000f;

	/* -------------------------------------------------------------------- */
	/*      DGNElementInfo flag values.                                     */
	/* -------------------------------------------------------------------- */
	public static final int DGNEIF_DELETED = 0x01;
	public static final int DGNEIF_COMPLEX = 0x02;

	/* -------------------------------------------------------------------- */
	/*      Justifications                                                  */
	/* -------------------------------------------------------------------- */
	public static final int DGNJ_LEFT_TOP = 0;
	public static final int DGNJ_LEFT_CENTER = 1;
	public static final int DGNJ_LEFT_BOTTOM = 2;
	public static final int DGNJ_LEFTMARGIN_TOP = 3;

	/* text node header only */
	public static final int DGNJ_LEFTMARGIN_CENTER = 4;

	/* text node header only */
	public static final int DGNJ_LEFTMARGIN_BOTTOM = 5;

	/* text node header only */
	public static final int DGNJ_CENTER_TOP = 6;
	public static final int DGNJ_CENTER_CENTER = 6;
	public static final int DGNJ_CENTER_BOTTOM = 8;
	public static final int DGNJ_RIGHTMARGIN_TOP = 9;

	/* text node header only */
	public static final int DGNJ_RIGHTMARGIN_CENTER = 10;

	/* text node header only */
	public static final int DGNJ_RIGHTMARGIN_BOTTOM = 11;

	/* text node header only */
	public static final int DGNJ_RIGHT_TOP = 12;
	public static final int DGNJ_RIGHT_CENTER = 13;
	public static final int DGNJ_RIGHT_BOTTOM = 14;

	/* -------------------------------------------------------------------- */
	/*      DGN file reading options.                                       */
	/* -------------------------------------------------------------------- */
	public static final int DGNO_CAPTURE_RAW_DATA = 0x01;

	/* -------------------------------------------------------------------- */
	/*      Known attribute linkage types, including my synthetic ones.     */
	/* -------------------------------------------------------------------- */
	public static final int DGNLT_DMRS = 0x0000;
	public static final int DGNLT_INFORMIX = 0x3848;
	public static final int DGNLT_ODBC = 0x5e62;
	public static final int DGNLT_ORACLE = 0x6091;
	public static final int DGNLT_RIS = 0x71FB;
	public static final int DGNLT_SYBASE = 0x4f58;
	public static final int DGNLT_XBASE = 0x1971;
	public static final int DGNLT_SHAPE_FILL = 0x0041;
	public static final int DGNLT_ASSOC_ID = 0x7D2F;

	/* -------------------------------------------------------------------- */
	/*      File creation options.                                          */
	/* -------------------------------------------------------------------- */
	public static final int DGNCF_USE_SEED_UNITS = 0x01;
	public static final int DGNCF_USE_SEED_ORIGIN = 0x02;
	public static final int DGNCF_COPY_SEED_FILE_COLOR_TABLE = 0x04;
	public static final int DGNCF_COPY_WHOLE_SEED_FILE = 0x08;
	public static final int SIZE_LONG = 4;

	/*
	   int         offset;
	   int         size;
	   int         element_id;
	   int         stype;
	   int                level;
	   int                type;
	   int                complex;
	   int                deleted;
	   int                graphic_group;
	   int                properties;
	   int         color;
	   int         weight;
	   int         style;
	   int                attr_bytes;
	   byte attr_data;
	   int         raw_bytes;
	   byte raw_data;
	   //} DGNElemCore;
	 */

	/** File Length; */
	int myFileLength = 0;

	/** Version of the file. */
	int myVersion = 1000;

	/*
	   public static final int SHAPE_NULL = 0;
	    public static final int SHAPE_POINT = 1;
	    public static final int SHAPE_POLYLINE = 3;
	    public static final int SHAPE_POLYGON = 5;
	    public static final int SHAPE_MULTIPOINT = 8;
	    public static final int SHAPE_POINTZ = 11;
	    public static final int SHAPE_POLYLINEZ = 13;
	    public static final int SHAPE_POLYGONZ = 15;
	    public static final int SHAPE_MULTIPOINTZ = 18;
	    public static final int SHAPE_POINTM = 21;
	    public static final int SHAPE_POLYLINEM = 23;
	    public static final int SHAPE_POLYGONM = 25;
	    public static final int SHAPE_MULTIPOINTM = 28;
	    public static final int SHAPE_MULTIPATCH = 31;
	 */
	int myDGNType = 0;
	double myXmin = 0;
	double myYmin = 0;
	double myXmax = 0;
	double myYmax = 0;
	double myZmin = 0;
	double myZmax = 0;
	double myMmin = 0;
	double myMmax = 0;

	// notify about warnings.
	private boolean myWarning = true;

	/**
	 * ShapeFileHeader constructor comment.
	 */
	public DGNFileHeader() {
		super();
	}

	/**
	 * Return the version of the file.
	 *
	 * @return DOCUMENT ME!
	 */
	public int getVersion() {
		return myVersion;
	}

	/**
	 * Devuelve el rect·ngulo del fichero.
	 *
	 * @return DOCUMENT ME!
	 */
	public java.awt.geom.Rectangle2D.Double getFileExtents() {
		return new java.awt.geom.Rectangle2D.Double(myXmin, myYmin,
			myXmax - myXmin, myYmax - myYmin);
	}

	/**
	 * Print warnings to system.out.
	 *
	 * @param inWarning DOCUMENT ME!
	 */
	public void setWarnings(boolean inWarning) {
		myWarning = inWarning;
	}

	/**
	 * Return the length of the header in 16 bit words..
	 *
	 * @return DOCUMENT ME!
	 */
	public int getHeaderLength() {
		return 50;
	}

	/**
	 * Return the number of 16 bit words in the shape file as recorded in the
	 * header
	 *
	 * @return DOCUMENT ME!
	 */
	public int getFileLength() {
		return myFileLength;
	}

	/**
	 * Read the header from the shape file.
	 *
	 * @param in DOCUMENT ME!
	 */
	public void readHeader(MappedByteBuffer in) {
		//in.order(ByteOrder.BIG_ENDIAN);

		/*
		   offset=in.getInt();
		   System.out.println("offset     "+offset);
		   size=in.getInt();
		   System.out.println("size        "+size);
		                   element_id=in.getInt();
		   System.out.println("element_id          "+element_id);
		                   stype=in.getInt();
		   System.out.println("stype        "+stype);
		                   level=in.getInt();
		   System.out.println("level          "+level);
		                   type=in.getInt();
		   System.out.println("type          "+type);
		                   complex=in.getInt();
		   System.out.println("complex          "+complex);
		                   deleted=in.getInt();
		   System.out.println("deleted          "+deleted);
		                   graphic_group=in.getInt();
		   System.out.println("graphic_group          "+graphic_group);
		                   properties=in.getInt();
		   System.out.println("properties          "+properties);
		                   color=in.getInt();
		   System.out.println("color          "+color);
		                   weight=in.getInt();
		   System.out.println("weight          "+weight);
		                   style=in.getInt();
		   System.out.println("style          "+style);
		                   attr_bytes=in.getInt();
		   System.out.println("attr_bytes          "+attr_bytes);
		                   //attr_data=in.get(attr_bytes);
		   //System.out.println("attr_data          "+attr_data);
		                   raw_bytes=in.getInt();
		   System.out.println("raw_bytes          "+raw_bytes);
		                   //raw_data=in.get(raw_bytes);
		   //System.out.println("raw_data          "+raw_data);
		 */

		// the first four bytes are integers
		// in.setLittleEndianMode(false);

		/*  in.order(ByteOrder.BIG_ENDIAN);
		   myFileCode = in.getInt();
		   if (myFileCode != 9994) warn("File Code = "+myFileCode+" Not equal to 9994");
		   // From 4 to 8 are unused.
		   myUnused1 = in.getInt();
		   // From 8 to 12 are unused.
		   myUnused2 = in.getInt();
		   // From 12 to 16 are unused.
		   myUnused3 = in.getInt();
		   // From 16 to 20 are unused.
		   myUnused4 = in.getInt();
		   // From 20 to 24 are unused.
		   myUnused5 = in.getInt();
		   // From 24 to 28 are the file length.
		   myFileLength = in.getInt();
		   // From 28 to 32 are the File Version.
		   in.order(ByteOrder.LITTLE_ENDIAN);
		   myVersion = in.getInt();
		   // From 32 to 36 are the Shape Type.
		   myShapeType = in.getInt();
		   // From 36 to 44 are Xmin.
		   myXmin = in.getDouble(); // Double.longBitsToDouble(in.getLong());
		   // From 44 to 52 are Ymin.
		   myYmin = in.getDouble();
		   // From 52 to 60 are Xmax.
		   myXmax = in.getDouble();
		   // From 60 to 68 are Ymax.
		   myYmax = in.getDouble();
		   // From 68 to 76 are Zmin.
		   myZmin = in.getDouble();
		   // From 76 to 84 are Zmax.
		   myZmax = in.getDouble();
		   // From 84 to 92 are Mmin.
		   myMmin = in.getDouble();
		   // From 92 to 100 are Mmax.
		   myMmax = in.getDouble();
		   // that is all 100 bytes of the header.
		 */
	}

	/**
	 * Muestra por consola el warning.
	 *
	 * @param inWarn DOCUMENT ME!
	 */
	private void warn(String inWarn) {
		if (myWarning) {
			System.out.print("WARNING: ");
			System.out.println(inWarn);
		}
	}
}
