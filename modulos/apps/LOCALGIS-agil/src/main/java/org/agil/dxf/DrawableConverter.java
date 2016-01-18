/**
 * DrawableConverter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */

package org.agil.dxf;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.agil.dxf.math.Matrix4D;
import org.agil.dxf.math.Point3D;
import org.agil.dxf.math.Vector3D;
import org.agil.dxf.reader.Dxf3DFACE;
import org.agil.dxf.reader.DxfARC;
import org.agil.dxf.reader.DxfATTRIB;
import org.agil.dxf.reader.DxfBLOCK;
import org.agil.dxf.reader.DxfCIRCLE;
import org.agil.dxf.reader.DxfConverter;
import org.agil.dxf.reader.DxfDIMENSION;
import org.agil.dxf.reader.DxfEntity;
import org.agil.dxf.reader.DxfEntitySet;
import org.agil.dxf.reader.DxfException;
import org.agil.dxf.reader.DxfFile;
import org.agil.dxf.reader.DxfINSERT;
import org.agil.dxf.reader.DxfLINE;
import org.agil.dxf.reader.DxfPOINT;
import org.agil.dxf.reader.DxfPOLYLINE;
import org.agil.dxf.reader.DxfSOLID;
import org.agil.dxf.reader.DxfTEXT;
import org.agil.dxf.reader.DxfTRACE;
import org.agil.dxf.reader.DxfVERTEX;


/**
 *  Converts DXF entities into a drawable representation.
 *  This is a non-trivial example how to convert a DXF file.
 *  <p>
 *  To decouple the DXF file representation this is extending
 *  the GoF <em>Strategy</em> pattern. This is the strategy, whereas
 *  the DxfConvertable classes are  the context where the
 *  startegy works.
 *  <p>
 *  For every entity in the DXF file where a conversion makes sense
 *  the DxfConverter class defines a dedicated convert method.
 *  Every convert method (except for file) has three parameters.
 *  The first is the entity to convert, the second is the DxfFile
 *  for references (to LAYERs etc.), the last is any Object where
 *  we can collect the converted entities. Here we use a DrawSet
 *  which is a set of DrawAbles (which can be lines, polygons and
 *  other draw sets as well).
 *  <p>
 *  Because DxfFile implements DxfReporter it can be used for
 *  status messages, too.
 *  <p>
 *  To avoid multiple conversion of the same entities BLOCKs are
 *  only converted once and then cached. That is not as easy as it seems
 *  because some of the properties of the BLOCK can change with the
 *  INSERT used.
 *  <p>
 *  Because the reader does not read layouts and views defined in the
 *  file we don't convert paper entities (they are defined in 'paper
 *  space' which means they are only visible in layouts).
 *  <p>
 *  DXF was a 2D format in it's youth. Therefore there are some
 *  relicts to be found of that. Some flat entities can be extruded
 *  in a the 'upward' direction. The upward direction also defines
 *  a special transformation. It is somewhat tricky to find out
 *  for which entity it is used and for which not (even if it's defined).
 *  Not all output filters handle this correctly. Here it is handled
 *  in the finalConv methods.
 *
 *
 *  @version 1.00
 */
public class DrawableConverter implements DxfConverter, FontFinder {
  // set the following to true if only entities lying in
  // model space should be converted
  private final static boolean DISCARD_PAPER_ENTITIES = true;

  // strip 8 from font names
  private final static boolean STRIP_FONT_8 = true;

  private final static int   CIRCLE_SEGMENTS = 36; // segments used to draw a circle

  private Hashtable blockHash = new Hashtable(89); // hash table to cache converted BLOCKs
  private Hashtable fontHash  = new Hashtable(89); // hash table to cache found fonts
  private DrawSet   complete;                      // the complete file as a draw set

  // RRI: En vez "fonts/Dxf" uso "/fonts/Dxf" para que no use el nombre del package
  private static String fontFileHead = "/dxf/fonts/Dxf";  // font dir & head
  private static String fontFileExt  = ".dvft";   // font file extension

  private static boolean txtTested    = false;
  private static DxfFont txtFont      = null;   // standard (default) font

  /**
   *  Constructor.
   *  Determine the position where we expect fonts and try to
   *  load the default font TXT. If this is not possible this
   *  viewer is considered a version without fonts and therefore
   *  TEXT entities are not converted.
   *  <p>
   *  So for TEXTs to be converted you have at least to provide
   *  the de.escape.quincunx.dxf.fonts.DxfTXT class. If you don't
   *  need more classes you don't have to pack them with the
   *  viewer.
   *  <p>
   *  In the de.escape.quincunx.dxf.fonts package I have packed
   *  all classes I was able to convert from AutoCAD SHX files
   *  into Java classes so far. It is a complicated task and
   *  I am still looking forward to the day when I know the
   *  format of SHX files to import fonts directly...
   */
  public DrawableConverter() {
  }

  /**Este metodo se encarga de cargar la fuente por defecto, txt*/
  public void loadDefaultFont(){

	if (!txtTested){
			try {

				txtFont = getFont("TXT");//Quitar esto de aqui, y anadir un  metodo
				//loadFonts, que pueda ser llamado desde el exterior. De esta forma, si
				//se usa desde un applet, se pueden invocar previamente privilegios de seguridad

			} catch (DxfException x) {

				System.out.println("No se han localizado las fuentes");
				x.printStackTrace();
			}

			txtTested = true;
	}
  }



  /**
   *  Convert a DXF file. This is very similar to the conversion
   *  of a normal entity set but here we also show the
   *  progress and remember the to level draw set for later access.
   *  <p>
   *  This is the entry method for conversion but it is called indirectly
   *  via <code>dxffile.convert(new DrawableConverter())</code>.
   *
   *  @param  dxf         DXF file to convert.
   */
  public void convert(DxfFile dxf) {
	DxfEntitySet entities = dxf.getEntities().getEntitySet(); // top level entities

	if (entities != null) {
	  int nrEntities = entities.getNrOfEntities();  // for progress
	  complete = new DrawSet(nrEntities); // the top level draw set

	  int nr = 0;

	  // --- now just go through all top level entities and convert them ---
	  for (Enumeration e = entities.getEntities();   e.hasMoreElements();  nr++) {
		DxfEntity ent = (DxfEntity)e.nextElement();
		if (DXFCanvas.debugLevel > 10) {
		  System.out.println("Converting "+ent);
		}
		// convert the entity
		ent.convert(this, dxf, complete);
	  }//for
	}//if
  }

  /**
   *  Convert a DXF 3DFACE.
   *  <p>
   *  3DFACE, TRACE and SOLID normally have 4 corners. If they have less
   *  corners the last corners are equal!
   *  <p>
   *  3DFACEs can have hidden lines which we silently ignore here.
   *  <p>
   *  The corners here usually define do not define a normal direction. You
   *  may have luck with that but you can't count on it.
   *
   *  @param  face        3DFACE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(Dxf3DFACE face, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (face.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("3DFACE.convert()");

	// --- get number of significant points ---
	int maxp;
	Point3D[] corner = face.getCorners();

	for (maxp = 3;   maxp >= 1;   maxp--) {
	  if (corner[maxp].x != corner[maxp-1].x   ||
	  corner[maxp].y != corner[maxp-1].y   ||
	  corner[maxp].z != corner[maxp-1].z) {
	break;
	  }
	}

	// --- add the corners to a polygon ---
	DrawPolygon poly = new DrawPolygon(maxp+1);
	for (int i = 0;   i <= maxp;   i++) {
	  poly.addPoint(corner[i]);
	}
	poly.calcNormal();
	//    System.out.println("/3DFACE.convert()");

	// --- final stuff ---
	setEntityInfo(poly, face, dxf);
	((DrawSet)collector).addDrawable(finalConv(poly, face, false));
  }

  /**
   *  Convert a DXF ARC.
   *  <p>
   *  A DXF ARC is defined by a center, a radius and a start and end angle.
   *  <p>
   *  Here it is converted in a polyline with a maximum of 36 corners.
   *
   *  @param  arc         ARC
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfARC arc, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (arc.getTile()) {
	return;			// paper space entities are not converted
	  }
	}

	//    System.out.println("ARC.convert()");
	DrawLines line;
	double radius     = arc.getRadius();
	double startAngle = arc.getStartAngle();
	double endAngle   = arc.getEndAngle();
	Point3D center   = arc.getCenter();

	if (radius > 0) {
	  while (endAngle <= startAngle) {
	endAngle += (double)(2*Math.PI);
	  }
	  // it really happens that endAngle-startAngle > 2*pi !
	  while (endAngle - startAngle > (double)(2*Math.PI+1e-8)) {
	endAngle -= (double)(2*Math.PI);
	  }

	  int nrSegments = (int)Math.ceil((endAngle-startAngle)*CIRCLE_SEGMENTS/2.0/Math.PI);

	  if (nrSegments < 2) {
	// draw at least 3 points
	nrSegments = 2;
	  }
	  line = new DrawLines(nrSegments+1);

	  double delta = (endAngle - startAngle)/nrSegments;
	  if (delta == 0) {
	nrSegments = CIRCLE_SEGMENTS;
	delta = (double)(2*Math.PI/nrSegments);
	  }

	  double angle = startAngle;
	  for (int i = 0;   i < nrSegments+1;  i++, angle+=delta) {
	line.addPoint(center.x + (double)(radius*Math.cos(angle)),
			  center.y + (double)(radius*Math.sin(angle)),
			  center.z);
	  }
	}
	else {
	  // so draw a point
	  line = new DrawLines(1);
	  line.addPoint(center);
	}

	//    System.out.println("/ARC.convert()");
	setEntityInfo(line, arc, dxf);
	((DrawSet)collector).addDrawable(finalConv(line, arc));
  }

  public void convert(DxfATTRIB attrib,DxfFile dxf,Object collector){
  }

  /**
   *  Convert a DXF BLOCK.
   *  <p>
   *  A DXF BLOCK is a set of entities. It is defined in a special
   *  SECTION in the DXF file (SECTION BLOCKS) and can be inserted
   *  in the ENTITY section by the INSERT entity.
   *  <p>
   *  BLOCKs cannot be defined recursively but used recursively
   *  becauise they can contain INSERTs (no need to care for here).
   *  Of course they cannot insert themselves.
   *  <p>
   *  There are a special kind of BLOCKs which insert other DXF files.
   *  They are not handled correctly (main reason is that I never had
   *  test data which used this feature).
   *  <p>
   *  A BLOCK defines an insertion point which must fit on the insertion
   *  point of the INSERT. This is the only transformation necessary
   *  here.
   *  <p>
   *  Because DxfBLOCKs collect their entities using a DxfEntitySet
   *  which themselves define a convert method the main part
   *  of this method is used to cache BLOCKs after they are converted
   *  once so there is no need to convert them again and again.
   *
   *  @param  block       BLOCK
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfBLOCK block, DxfFile dxf, Object collector) {
	DrawSet converted = (DrawSet)blockHash.get(block.getBlockName());

	if (converted == null) {
	  //    System.out.println("BLOCK.convert(), this = "+this);
	  // --- creat, if first call ---
	  /* !!! dont use arbit mat!
	  Matrix4D  tmat = calcArbitMat();
	  Point3D   refPoint = block.getReferencePoint();
	  if (tmat != null) {
	tmat.translate(-refPoint.x, -refPoint.y, -refPoint.z);
	  }
	  else if (refPoint.x != 0   ||   refPoint.y != 0   ||   refPoint.z != 0) {
	tmat = new Matrix4D(1, 0, 0, -refPoint.x,
				0, 1, 0, -refPoint.y,
				0, 0, 1, -refPoint.z,
				0, 0, 0, 1);
	  }
	  instead: */
	  Point3D   refPoint = block.getReferencePoint();
	  Matrix4D tmat = new Matrix4D(1, 0, 0, -refPoint.x,
				   0, 1, 0, -refPoint.y,
				   0, 0, 1, -refPoint.z,
				   0, 0, 0, 1);

	  // use a new DrawSet to collect the converted entities
	  converted = new DrawSet(tmat, 1);
	  block.getEntities().convert(this, dxf, converted);
	  setEntityInfo(converted, block, dxf);

	  // remember this block so there is no need to convert again
	  blockHash.put(block.getBlockName(), converted);
	  //    System.out.println("/BLOCK.convert()");
	}

	// this is as easy as can be
	((DrawSet)collector).addDrawable(converted);
  }

  /**
   *  Convert a DXF CIRCLE.
   *
   *  CIRCLEs define a center point and a radius. Here they are converted
   *  into 36-corner polyons.
   *
   *  @param  circle      CIRCLE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfCIRCLE circle, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (circle.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("CIRCLE.convert()");
	DrawLines line;
	double radius     = circle.getRadius();
	Point3D center   = circle.getCenter();

	if (radius > 0) {
	  line = new DrawLines(CIRCLE_SEGMENTS);
	  double delta = (double)(2*Math.PI/CIRCLE_SEGMENTS);
	  double angle = 0;
	  for (int i = 0;   i < CIRCLE_SEGMENTS;  i++, angle+=delta) {
	line.addPoint(center.x + (double)(radius*Math.cos(angle)),
			   center.y + (double)(radius*Math.sin(angle)),
			   center.z);
	  }
	  line.close();
	}
	else {
	  // just a point
	  line = new DrawLines(1);
	  line.addPoint(center);
	}

	//    System.out.println("/CIRCLE.convert()");
	setEntityInfo(line, circle, dxf);
	((DrawSet)collector).addDrawable(finalConv(line, circle));
  }

  /**
   *  Convert a DXF DIMENSION.
   *  <p>
   *  A DIMENSION is a complicated thing in most CAD formats. But
   *  DXF is really easy here because the DIMENSION is stored
   *  in two ways in the DXF file. As a complicated description
   *  and as the insertion of a BLOCK. Here the BLOCK is used
   *  and so the conversion of a DIMENSION is somewhat like the
   *  conversion of a very simple INSERT.
   *
   *  @param  dim         DIMENSION
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfDIMENSION dim, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (dim.getTile()) {
	return;			// paper space entities are not converted
	  }
	}

	// --- get the block and convert it ---
	DxfEntity block = dxf.getBlock(dim.getBlockName());
	if (block == null) {
	  // do nothing
	}
	else {
	  Point3D insert = dim.getInsertPoint();
	  DrawSet set = new DrawSet(new Matrix4D(1, 0, 0, insert.x,
						 0, 1, 0, insert.y,
						 0, 0, 1, insert.z,
						 0, 0, 0, 1),
				1);
	  block.convert(this, dxf, set);
	  setEntityInfo(set, dim, dxf);
	  ((DrawSet)collector).addDrawable(set);
	}
  }

  /**
   *  Convert a DXF INSERT.
   *  <p>
   *  INSERT inserts a BLOCK with some additional transformations.
   *  It is possible to insert the BLOCK multiple times in an
   *  array like fashion but that is not supported yet.
   *
   *  @param  insert      INSERT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfINSERT insert, DxfFile dxf, Object collector) {

	if (DISCARD_PAPER_ENTITIES) {
	  if (insert.getTile()) {
		return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("INSERT.convert()");
	DxfEntity block = dxf.getBlock(insert.getBlockName());
	if (block == null) {
	  // UNKNOWN BLOCK!
	  //      System.out.println("/INSERT.convert()");
	}else {
	  // --- collect trafos (this is difficile) ---
	  Matrix4D tmat = new Matrix4D(),
						   rotMat1,
						   rotMat2;


	  tmat.scale(insert.getScaleX(),
					insert.getScaleY(),
					insert.getScaleZ());

	  tmat.zrot(insert.getRotation());
	  rotMat2 = insert.calcArbitMat();

	  if (insert.getRows() > 1   ||   insert.getColumns() > 1) {
		// Multiple insertions
		// !!! not yet
		// System.out.println("/INSERT.convert()");
	  }else {
		tmat.translateLeft(new Vector3D(insert.getInsertPoint()));
		if (rotMat2 != null) {
		  tmat.multLeftBy(rotMat2);
		}

		// now make a DrawSet with the correct trafo and collect the
		// BLOCK to it
		DrawSet set = new DrawSet(tmat, 1);
		block.convert(this, dxf, set);

		//	System.out.println("/INSERT.convert()");
		setEntityInfo(set, insert, dxf);
		((DrawSet)collector).addDrawable(set);
	  }//else
	}
  }

  /**
   *  Convert a DXF LINE.
   *  <p>
   *  LINEs just have to corners so the conversion is as easy as can be.
   *
   *  @param  line        LINE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfLINE line, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (line.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("LINE.convert()");
	DrawLines dline = new DrawLines(2);
	dline.addPoint(line.getStartPoint());
	dline.addPoint(line.getEndPoint());
	//    System.out.println("/LINE.convert()");
	setEntityInfo(dline, line, dxf);
	((DrawSet)collector).addDrawable(finalConv(dline, line, false));
  }

  /**
   *  Convert a DXF POINT.
   *  <p>
   *  Correctly the symbol to be drawn at the point position has
   *  to be determined by the PDMODE and PDSIZE variables from
   *  the header section but we don't care for that and just
   *  draw a dot. So everything is really easy.
   *
   *  @param  point       POINT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfPOINT point, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (point.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("POINT.convert()");
	DrawLines line = new DrawLines(1);
	line.addPoint(point.getPosition());
	//    System.out.println("/POINT.convert()");
	setEntityInfo(line, point, dxf);
	((DrawSet)collector).addDrawable(finalConv(line, point, false));
  }

  /**
   *  Convert a net type polyline.
   *  <p>
   *  POLYLINEs come in different flavours. This is the net type.
   *  It consists of a point set and lines connecting them which
   *  are defined as indices into the set. A negative index
   *  means an invisble connection, a positive a visible.
   *  <p>
   *  Each VERTEX holds exactly 4 indices. This is often done wrong
   *  by DXF output filters.
   *  <p>
   *  This method tries to work around some common errors.
   *
   *  @param  poly        POLYLINE (net type)
   *  @param  dxf         DXF file object for looking up layers etc.
   */
  private static DrawAble convertPolyNet(DxfPOLYLINE poly, DxfFile dxf) {
	//    System.out.println("POLYLINE.polyNet()");
	// often made wrong, so we have to correct...
	int nrKnots = (poly.getNrCtrlU() <= 0) ? 16 : poly.getNrCtrlU();
	int nrK = 0;
	Point3D[] knot = new Point3D[nrKnots];
	int nrFaces = (poly.getNrCtrlV() <= 0) ? 4 : poly.getNrCtrlV();
	int nrF = 0;
	int[] vertex = new int[DxfVERTEX.PFACEVMAX * nrFaces];
	int nrV = 0;
	boolean lastWasInvisible = true;

	DxfEntitySet vertices = poly.getVertices();

	// === first count the number of knots and faces because this is often incorrect ===
	for (Enumeration e = vertices.getEntities();   e.hasMoreElements();   ) {
	  DxfVERTEX v = (DxfVERTEX)e.nextElement();

	  //      System.out.println("v.type = "+v.type);
	  if ((v.getType() & DxfVERTEX.WEB_3D) != 0) {
	// knot
	if (nrK == nrKnots) {
	  // more than stated in the file
	  Point3D[] tmp = new Point3D[2*nrKnots];
	  System.arraycopy(knot, 0, tmp, 0, nrKnots);
	  nrKnots *= 2;
	  knot = tmp;
	}
	knot[nrK++] = v.getPosition();

	  }
	  else {
	// vertex
	if (nrF++ == nrFaces) {
	  // more than stated in the file
	  int[] tmp = new int[2 * DxfVERTEX.PFACEVMAX * nrFaces];
	  System.arraycopy(vertex, 0, tmp, 0, nrFaces);
	  nrFaces *= 2;
	  vertex = tmp;
	}
	lastWasInvisible = true;

	for (int k = 0;   k < DxfVERTEX.PFACEVMAX;   k++) {
	  int[] index = v.getReferences();
	  vertex[nrV++] = index[k];
	  if (index[k] > 0) {
		if (lastWasInvisible) {
		  lastWasInvisible = false;
		}
	  }
	  else if (index[k] < 0) {
		lastWasInvisible = true;
	  }
	  else {
		nrV += DxfVERTEX.PFACEVMAX - k - 1;
		break;
	  }
	}
	  }
	}

	// === now the correct numbers are known ===
	if (nrK != nrKnots) {
	  nrKnots = nrK;
	}
	if (nrF != nrFaces) {
	  nrFaces = nrF;
	}

	//    System.out.println("nrFaces = "+nrFaces+", nrKnots = "+nrKnots+", nrV = "+nrV);

	// === now do the conversion ===
	DrawSet set = (nrFaces > 0) ? new DrawSet(nrFaces) : null;
	DrawLines line = new DrawLines(1);
	for (nrF = 0,  nrV = 0;   nrF < nrFaces;   nrF++,  nrV += DxfVERTEX.PFACEVMAX) {
	  lastWasInvisible = true;
	  //      System.out.println("nrF = "+nrF);
	  for (int k = 0;   k < DxfVERTEX.PFACEVMAX;   k++) {
	if (vertex[nrV+k] < -nrKnots  ||  vertex[nrV+k] > nrKnots) {
	  /* sigh */
	  /* nothing we can do */
	  //	  System.out.println("OUCH! vertex["+nrV+"+"+k+"] = "+ vertex[nrV+k]);
	  break;
	}
	//	System.out.println("vertex["+nrV+"+"+k+"] = "+ vertex[nrV+k]);
	if (vertex[nrV+k] > 0) {
	  if (lastWasInvisible) {
		line = new DrawLines(DxfVERTEX.PFACEVMAX-k+1);
		lastWasInvisible = false;
	  }
	  line.addPoint(knot[vertex[nrV+k]-1]);
	}
	else if (vertex[nrV+k] < 0) {
	  if (!lastWasInvisible) {
		lastWasInvisible = true;
		line.addPoint(knot[-vertex[nrV+k]-1]);
		set.addDrawable(line);
	  }
	}
	  }
	  //      System.out.println("lastWasInvisible = "+lastWasInvisible);
	  if (!lastWasInvisible) {
	//	System.out.println("LAST: vertex["+nrV+"] = "+ vertex[nrV]);
	if (vertex[nrV] < 0) {
	  line.addPoint(knot[-vertex[nrV]-1]);
	}
	else {
	  line.addPoint(knot[vertex[nrV]-1]);
	}
	set.addDrawable(line);
	  }
	}

	//    System.out.println("/POLYLINE.polyNet()");
	return set;
  }


  /**
   *  Convert a line type POLYLINE.
   *  <p>
   *  POLYLINEs come in different flavours. This is the net type.
   *  It can come as a set of points connected by straight lines,
   *  by arcs and as a spline (where the points define the control points).
   *  But the spline is also stored as a straight line connection so
   *  here this is taken directly.
   *  <p>
   *  This is the only entity in DXF which allows the definition of
   *  a width. This width my vary along the line. It is not considered
   *  here yet.
   *
   *  @param  poly        POLYLINE (line type)
   *  @param  dxf         DXF file object for looking up layers etc.
   */
  private static DrawAble convertPolyLine(DxfPOLYLINE poly, DxfFile dxf) {
	//    System.out.println("POLYLINE.polyLine()");
	boolean isSpline = ((poly.getType() & DxfPOLYLINE.B_SPLINE) != 0);
	boolean isClosed = ((poly.getType() & DxfPOLYLINE.CLOSED) != 0);
	DrawLines line = new DrawLines();

	DxfEntitySet vertices = poly.getVertices();

	if (isSpline) {
	  // just take straight line representation
	  for (Enumeration e = vertices.getEntities();   e.hasMoreElements();   ) {
	DxfVERTEX v = (DxfVERTEX)e.nextElement();
	if ((v.getType() & DxfVERTEX.CONTROL) == 0) {
	  // skip others
	  line.addPoint(v.getPosition());
	}
	  }
	}
	else {
	  // this is more difficult because the points are possibly interconnected
	  // by arcs
	  DxfVERTEX vertex,
			firstVertex = null,
			nextVertex = null;
	  int       nr         = vertices.getNrOfEntities();

	  for (Enumeration e = vertices.getEntities();   nr-- > 0;  ) {
	if (nextVertex == null) {
	  nextVertex = (DxfVERTEX)e.nextElement();
	}
	vertex = nextVertex;
	if (firstVertex == null) {
	  firstVertex = vertex;
	}
	if (!e.hasMoreElements()) {  // this is equivalent to nr==0 here
	  nextVertex = isClosed ? firstVertex : null;
	}
	else {
	  nextVertex = (DxfVERTEX)e.nextElement();
	}

	if (vertex.getRounding() == 0   ||   nextVertex == null) {
	  // straight line
	  line.addPoint(vertex.getPosition());
	}
	else {
	  // arc connection
	  // DXF-Standard p. 323
	  double   alpha,
				  start,
				  radius,
				  dist;
	  double[] vec = new double[2],
		  mid = new double[2];
	  int     segs;
	  Point3D thisPosition = vertex.getPosition(),
				  nextPosition = nextVertex.getPosition();

	  alpha  = (double)(4.0 * Math.atan(vertex.getRounding()));
	  vec[0] = nextPosition.x - thisPosition.x;
	  vec[1] = nextPosition.y - thisPosition.y;
	  dist   = (double)(Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]));

	  if (dist == 0) {
		continue;
	  }

	  vec[0] /= dist;
	  vec[1] /= dist;

	  // now everything's similar to ARC
	  radius = dist / (double)(2.0 * Math.sin(alpha/2));

	  mid[0] = (thisPosition.x + nextPosition.x)/2 -
				   (radius - vertex.getRounding()*dist/2)*vec[1];
	  mid[1] = (thisPosition.y + nextPosition.y)/2 +
				   (radius - vertex.getRounding()*dist/2)*vec[0];

	  segs   = (int)Math.ceil(CIRCLE_SEGMENTS*Math.abs(alpha)/Math.PI);

	  start  = (double)Math.atan2(thisPosition.y-mid[1], thisPosition.x-mid[0]);

	  alpha /= segs;
	  if (radius < 0) {
		radius = -radius;
	  }

	  for (int j = 0;   j < segs;   j++, start+=alpha) {
		line.addPoint(mid[0] + radius*(double)Math.cos(start),
			  mid[1] + radius*(double)Math.sin(start),
			  thisPosition.z);
	  }
	}
	  }
	}
	if (isClosed) {
	  line.close();
	}

	//    System.out.println("/POLYLINE.polyLine()");
	return finalConv(line, poly);
  }

  /**
   *  Convert a net type polyline.
   *  <p>
   *  POLYLINEs come in different flavours. This is the net type.
   *  It's somewhat like a two-dimensional array of knots defining a face.
   *  Here spline faces are handled, too. For every spline POLYLINE there
   *  is an additional simple knot-on-surface representation stored so we
   *  don't fool around with splines but take the representation
   *  directly.
   *
   *  @param  poly        POLYLINE (net type)
   *  @param  dxf         DXF file object for looking up layers etc.
   */
  private static DrawAble convertPolyWeb(DxfPOLYLINE poly, DxfFile dxf) {
	//    System.out.println("POLYLINE.polyWeb()");
	DxfEntitySet vertices = poly.getVertices();

	Enumeration e = vertices.getEntities();
	if (!e.hasMoreElements()) {
	  //      System.out.println("/POLYLINE.polyWeb()");
	  return null;
	}
	else {
	  int m,
	  n;
	  int nrN,
	  nrM;
	  int x,      /* counter in u dir */
	  y;      /* counter in v dir */
	  int v = 0;  /* vertex counter */
	  boolean isClosedM = ((poly.getType() & DxfPOLYLINE.CLOSED) != 0);
	  boolean isClosedN = ((poly.getType() & DxfPOLYLINE.WEB_CLOSED) != 0);

	  if ((poly.getType() & DxfPOLYLINE.B_SPLINE) != 0) {
	m = poly.getNrApproxM();
	n = poly.getNrApproxN();
	  }
	  else {
	m = poly.getNrCtrlU();
	n = poly.getNrCtrlV();
	  }
	  nrM = m + ((isClosedM) ? 1 : 0);
	  nrN = n + ((isClosedN) ? 1 : 0);

	  // nrN lines in m dir...
	  DrawLines[] linesM = new DrawLines[nrN];

	  // ... with nrM points each
	  for (y = 0;   y < nrN;   y++) {
	linesM[y] = new DrawLines(nrM);
	  }

	  // nrM lines in n dir...
	  DrawLines[] linesN = new DrawLines[nrM];

	  // ...with nrN points each
	  for (x = 0;   x < nrM;   x++) {
	linesN[x] = new DrawLines(nrN);
	  }

	  // the draw set to collect the lines
	  DrawSet set = new DrawSet(nrM+nrN);

	  // Actual vertex
	  DxfVERTEX vertex;

	  for (x = 0;   x < m;   x++) {
	// m lines with n points
	for (y = 0;   y < n;   y++) {
	  // skip the control points of splines
	  do {
		vertex = (DxfVERTEX)e.nextElement();
	  } while ((vertex.getType() & DxfVERTEX.CONTROL) != 0);

	  linesM[y].addPoint(vertex.getPosition());
	  linesN[x].addPoint(vertex.getPosition());
	}
	if (isClosedN) {
	  linesN[x].close();
	}
	set.addDrawable(linesN[x]);
	  }
	  for (y = 0;   y < n;   y++) {
	if (isClosedM) {
	  linesM[y].close();
	}
	set.addDrawable(linesM[y]);
	  }

	  //      System.out.println("/POLYLINE.polyWeb()");
	  return set;
	}
  }

  /**
   *  Convert a DXF POLYLINE.
   *  <p>
   *  POLYLINE is really complicated. It comes in different flavours
   *  so the conversion is divide into different methods.
   *
   *  @param  poly        POLYLINE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfPOLYLINE poly, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (poly.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	DrawAble ret;
	int      type = poly.getType();

	if ((type & DxfPOLYLINE.NET) != 0) {
	  // net type POLYLINE
	  ret = convertPolyNet(poly, dxf);
	}
	else if ((type & DxfPOLYLINE.ANY_WEB) != 0) {
	  // web like POLYLINE
	  ret = convertPolyWeb(poly, dxf);
	}
	else {
	  // "simple" POLYLINE
	  ret = convertPolyLine(poly, dxf);
	}
	setEntityInfo(ret, poly, dxf);

	if (ret instanceof DrawLines) {
	  // flat polylines can be extruded
	  ((DrawSet)collector).addDrawable(finalConv((DrawLines)ret, poly));
	}
	else {
	  // others not
	  ((DrawSet)collector).addDrawable(ret);
	}

  }

  /**
   *  Convert a DXF SOLID.
   *  <p>
   *  SOLID, 3DFACE and TRACE normally have 4 corners. If they have less
   *  corners the last corners are equal!
   *  <p>
   *  In the DXF file the corners are stored in a non-standard order
   *  but that is taken care of when reading. But some output filters
   *  make this wrong so it can happen that two corners are swapped
   *  and so the SOLID forms a tie. There is no workaround for this
   *  implemented here but it shouldn't be to difficult because SOLIDs
   *  are known to be flat.
   *  <p>
   *  The corners here define usually do not define a normal direction. You
   *  may have luck with that but you can't count on it.
   *  <p>
   *  @param  solid       SOLID
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfSOLID solid, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (solid.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	//    System.out.println("SOLID.convert()");

	/* --- get number of significant points --- */
	int     maxp;
	Point3D[] corner = solid.getCorners();

	for (maxp = 3;   maxp >= 1;   maxp--) {
	  if (corner[maxp].x != corner[maxp-1].x   ||
	  corner[maxp].y != corner[maxp-1].y   ||
	  corner[maxp].z != corner[maxp-1].z) {
	break;
	  }
	}
	DrawPolygon poly = new DrawPolygon(maxp+1);
	/* the last two corners are swapped refered to 3DFACE,
	   and a polygon, so let's make it tricky */
	switch (maxp) {
	case 3:
	  poly.addPoint(corner[2]);
	  /* FALLTHROUGH */
	case 2:
	  poly.addPoint(corner[3]);
	  /* FALLTHROUGH */
	case 1:
	  poly.addPoint(corner[1]);
	  /* FALLTHROUGH */
	case 0:
	  poly.addPoint(corner[0]);
	  break;
	}

	//    System.out.println("/SOLID.convert()");
	setEntityInfo(poly, solid, dxf);
	((DrawSet)collector).addDrawable(finalConv(poly, solid));
  }

  /**
   *  Convert a DXF TEXT.
   *  <p>
   *  TEXTs are only converted if we have fonts available.
   *  <p>
   *  To avoid memory bloat TEXTs are converted using a flyweight pattern concept.
   *  The line representation of every used letter of a font is only stored once
   *  in the viewer (even if it is used more often).
   *  Because this is somewhat more complicated the actual TEXT conversion
   *  is put in the DrawText class in the fonts package.
   *
   *  @param  text        TEXT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   *  @see    de.escape.quincunx.dxf.fonts.DrawText
   */
  public void convert(DxfTEXT text, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (text.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	if (fontsAvailable()) {
	  // only if we have any fonts
	  try {
	DrawText dtext = new DrawText(null, text, dxf, this);
	setEntityInfo(dtext, text, dxf);
	DrawAble extruded = dtext.extrude(text.getExtrusion());
	((DrawSet)collector).addDrawable(extruded);

	  } catch (DxfException x) {
	// nothing
	  }
	}
	else {
	  // nothing
	}
  }

  /**
   *  Convert a DXF TRACE.
   *  <p>
   *  TRACE is essentially the same as SOLID so the SOLID conversion
   *  method is used.
   *
   *  @param  trace       TRACE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfTRACE trace, DxfFile dxf, Object collector) {
	if (DISCARD_PAPER_ENTITIES) {
	  if (trace.getTile()) {
	return;			// paper space entities are not converted
	  }
	}
	// TRACE is handled the same as solid
	convert((DxfSOLID)trace, dxf, collector);
  }

  /**
   *  Convert a DXF ENTITY Set.
   *  <p>
   *  The reader uses the DxfEntitySet class in several places because
   *  they can all be handled the same.
   *  <p>
   *  Here a DrawSet is defined (as a subset the the collector parameter
   *  which is a DrawSet, too) and the entities are converted and added.
   *
   *  @param  set         ENTITY set
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfEntitySet set, DxfFile dxf, Object collector) {
	//    System.out.println("DxfEntitSet::convert(), nrEntities = "+nrEntities);
	DrawSet   drawset = new DrawSet(set.getNrOfEntities());

	for (Enumeration e = set.getEntities();   e.hasMoreElements();   ) {
	  ((DxfEntity)e.nextElement()).convert(this, dxf, drawset);
	}

	((DrawSet)collector).addDrawable(drawset);
  }

  /**
   * Final conversion defined by extrusion, upward direction, height etc.
   * @param     dl           converted element so far
   * @param     entity       entity to use for information
   * @param     doTransform  do the upward dir trafo
   * @return    extruded and transformed element
   */
  private static DrawAble finalConv(DrawLines dl, DxfEntity entity, boolean doTransform) {
	//    System.out.println("finalConv()");
	if (dl == null) {
	  return null;
	}

	DrawAble dr;

	if (doTransform) {
	  // extrude first in z, then transform
	  if (entity.getExtrusion() != 0) {
	dr = dl.extrude(entity.getExtrusion());
	  }
	  else {
	dr = dl;
	  }

	  Matrix4D mat = entity.calcArbitMat();
	  if (mat != null) {
	//	System.out.println("upward = "+upward+"\nmat = "+mat);
	dr.transformBy(mat);
	  }
	}
	else {
	  // extrude in upward direction
	  if (entity.getExtrusion() != 0) {
	dr = dl.extrude(entity.getExtrusion(), entity.getUpwardVector());
	  }
	  else {
	dr = dl;
	  }
	}

	//    System.out.println("/finalConv()");
	return dr;
  }


  /**
   *  Final conversion defined by extrusion etc. Always
   *  do the upward dir trafo.
   *  @param     dl           converted element so far
   *  @param     entity       entity to use for information
   *  @return    extruded and transformed element
   */
  private static DrawAble finalConv(DrawLines dl, DxfEntity entity) {
	return finalConv(dl, entity, true);
  }

  /**
   *  Set the layer, color etc.  information
   *  @param  d       where to set the information
   *  @param  entity  where to get entity specific information
   *  @param  dxf     where to get global information
   */
  private static void setEntityInfo(DrawAble d, DxfEntity entity, DxfFile dxf) {
	if (d != null) {
	  d.setLayer(dxf.getLayer(entity.getLayerName()));
	  d.setColor(entity.getColor());
	}
  }

  /**
   *  Get the converted entities.
   *  @return  entities converted to drawable representation
   */
  public DrawSet getModel() {
	return complete;
  }

  /**
   *  Get a font for a given name.
   *  I do not know where the fonts with the 8 (i.e. TXT8 vs. TXT) are for.
   *  They seem to be the same as the fonts without 8 so the 8 is stripped
   *  here.
   *
   *  @param  fontName  font name
   *  @return corresponding font (or standard font)
   *  @exception  DxfException if no fonts are available
   */
  public DxfFont getFont(String fontName) throws DxfException {

/* Esto no tiene sentido desde applets Netscape
	if (txtTested   &&  txtFont == null) {
		throw new DxfException("err!Fonts");
	}
*/

	//Obtiene el nombre reducido de la fuente y lo busca en el hash
	//de fuentes: poner en una funcion getDxfFont(fontName)
	String reducedName = (STRIP_FONT_8  &&  fontName.endsWith("8"))  ?
								fontName.substring(0, fontName.length()-1).toUpperCase() :
								fontName.toUpperCase();

	DxfFont font = (DxfFont)fontHash.get(reducedName);

	if (font == null) {

		 // first request
		 InputStream input;


		  if (DXFCanvas.debugLevel > 10) {
			System.out.println("Looking for "+fontFileHead + reducedName + fontFileExt);
		  }

		  //Si la fuente no esta en cache, se intenta cargar. Aqui es donde habria que
		  //hacer distincion entre Applets y aplicaciones.
		  //Con Netscape el getClass().getResourceAsStream() devuelve una excepcion
		  try {

				//System.out.println("Debuggeando "+fontFileHead+reducedName+fontFileExt);
				input = getClass().getResourceAsStream(
					fontFileHead + reducedName + fontFileExt);


		  } catch (SecurityException x) {

				// Netscape throws a security  exception here so we cannot use fonts!

				//Netscape lazna una excepcion de seguridad cuando se trata de acceder a fuentes
				//desde aqui
				System.out.println("Se ha producido una excepcion de seguridad "+x.toString());
				return null;
		  }


//Llegados a este punto se puede haber encontrado o no la fuente, pero no
//tiene nada que ver con problemas de seguridad

		  if (DXFCanvas.debugLevel > 10) {
			System.out.println(input == null  ?   "\tNot found!"  :  "\tFound");
		  }

		  if (input == null) {
				// No font available
				font = txtFont;
		  }else {

				try {
					font = new DxfFontFromStream(new BufferedInputStream(input));

				} catch (IOException x) {
					font = txtFont;
				}
		  }


		  if (font != null) {
			fontHash.put(reducedName, font); // store in hashtable
		  }
	}//fin del procesado de la primera peticion de la fuente

	return font;
  }


  /**
   *  Do we have any fonts available (i.e. at least the default font
   *  TXT)?
   *  @return are there any fonts?
   */
  public boolean fontsAvailable() {
	return txtFont != null;
  }

  /**Este metodo permite cargar la fuente por defecto desde el exterior.
   * Es util cuando se ejecute desde un applet Netscape, para solicitar privilegios
   * de seguridad de acceso a recursos previamente.
   */
  public void setTxtFont(DxfFont txtFont){
		this.txtFont = txtFont;
  }

}
