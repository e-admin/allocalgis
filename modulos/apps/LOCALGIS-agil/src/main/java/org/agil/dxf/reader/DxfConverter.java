/**
 * DxfConverter.java
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

package org.agil.dxf.reader;

/**
 *  Interface for a converter of DXF entities.
 *  This is <em>the</em> interface for converting DXF files after they are read.
 *  You must extend this class and define the convert methods below to do whatever
 *  you need.

 *
 *  @version 1.00beta0
 */
public interface DxfConverter {
  /**
   *  Convert a DXF file.
   *  @param  dxf         DXF file to convert.
   */
  public void convert(DxfFile dxf);

  /**
   *  Convert a DXF 3DFACE.
   *  @param  face        3DFACE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(Dxf3DFACE face, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF ARC.
   *  @param  arc         ARC
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfARC arc, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF BLOCK.
   *  @param  block       BLOCK
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfBLOCK block, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF CIRCLE.
   *  @param  circle      CIRCLE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfCIRCLE circle, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF DIMENSION.
   *  @param  dim         DIMENSION
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfDIMENSION dim, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF INSERT.
   *  @param  insert      INSERT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfINSERT insert, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF LINE.
   *  @param  line        LINE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfLINE line, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF POINT.
   *  @param  point       POINT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfPOINT point, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF POLYLINE.
   *  @param  poly        POLYLINE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfPOLYLINE poly, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF SOLID.
   *  @param  solid       SOLID
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfSOLID solid, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF TEXT.
   *  @param  text        TEXT
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfTEXT text, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF TRACE.
   *  @param  trace       TRACE
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfTRACE trace, DxfFile dxf, Object collector);

  /**
   *  Convert a DXF ENTITY Set.
   *  @param  set         ENTITY set
   *  @param  dxf         DXF file object for looking up layers etc.
   *  @param  collector   normally used to collect the converted stuff,
   *                      but may  be used to collect any other info
   */
  public void convert(DxfEntitySet set, DxfFile dxf, Object collector);

  /**Convierte un DXF ATTRIB, que es un atributo, visible en forma de texto
   * o guardado en memoria.
   */
  public void convert(DxfATTRIB converter, DxfFile dxf, Object collector) ;

}
