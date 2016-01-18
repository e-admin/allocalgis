/**
 * DxfHeader.java
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


package com.geopista.ui.plugin.io.dxf.reader;


import com.geopista.ui.plugin.io.dxf.math.Point3D;



/**
 *  DxfHeader resembles the HEADER part of the DXF file.
 *  Only the relevant variables are read here.
 *
 *  @version  1.00beta (January 1999)
 */
public class DxfHeader extends DxfSection {
  /** DWGCODEPAGE shortcuts */
  static public final int  UC_ISO_1  =  0,
                           UC_ISO_2  =  1,
                           UC_DOS437 =  2,
                           UC_DOS850 =  3,
                           UC_DOS852 =  4,
                           UC_DOS855 =  5,
                           UC_DOS857 =  6,
                           UC_DOS860 =  7,
                           UC_DOS861 =  8,
                           UC_DOS863 =  9,
                           UC_DOS864 = 10,
                           UC_DOS865 = 11,
                           UC_DOS869 = 12,
                           UC_MAC    = 13,
                           UC_ALL    = 14;

  protected String      ACADVER;      /* [#1] */
  protected int         ATTMODE = 1;  /* [#70] */
  protected String      DWGCODEPAGE = "DOS437";  /* [#3] */
  protected Point3D     EXTMAX = new Point3D();       /* [#10,20,30] */
  protected Point3D     EXTMIN = new Point3D();	    /* [#10,20,30] */
  protected int	        FILLMODE;	    /* [#70] */
  protected Point3D     INSBASE = new Point3D();	    /* [#10,20,30] */
  protected double       LTSCALE = 1.0f;	    /* [#40] */
  protected int         PDMODE;	    /* [#70] */
  protected double       PDSIZE;	    /* [#40] */

  protected String      lastKeyword = "";
  protected int         codePage    = UC_DOS437;

  /**
   *  Read the HEADER section
   *  @param  grp group code reader
   *  @param  reporter  reporter for messages
   *  @exception DxfException on i/o and syntax errors
   */
  public void read(DxfGroups grp) throws DxfException {
    while (true) {
      grp.read();
      switch (grp.number) {
      case 0:
	if ("ENDSEC".equals(grp.valToString())) {
	  return;
	}
	else {
	  throw new DxfException("err!InHeader", new String[] { String.valueOf(grp.getLineNr()) });
	}

      case 9:
	lastKeyword = grp.valToString();
	break;

      case 1:
	if (lastKeyword.equals("$ACADVER")) {
	  ACADVER = grp.valToString();
	}
	break;

      case 3:
	if (lastKeyword.equals("$DWGCODEPAGE")) {
	  DWGCODEPAGE = grp.valToString();
	}
	break;

      case 10:
	if (lastKeyword.equals("$EXTMAX")) {
	  EXTMAX.x = grp.valToDouble();
	}
	else if (lastKeyword.equals("$EXTMIN")) {
	  EXTMIN.x = grp.valToDouble();
	}
	else if (lastKeyword.equals("$INSBASE")) {
	  INSBASE.x = grp.valToDouble();
	}
	break;

      case 20:
	if (lastKeyword.equals("$EXTMAX")) {
	  EXTMAX.y = grp.valToDouble();
	}
	else if (lastKeyword.equals("$EXTMIN")) {
	  EXTMIN.y = grp.valToDouble();
	}
	else if (lastKeyword.equals("$INSBASE")) {
	  INSBASE.y = grp.valToDouble();
	}
	break;

      case 30:
	if (lastKeyword.equals("$EXTMAX")) {
	  EXTMAX.z = grp.valToDouble();
	}
	else if (lastKeyword.equals("$EXTMIN")) {
	  EXTMIN.z = grp.valToDouble();
	}
	else if (lastKeyword.equals("$INSBASE")) {
	  INSBASE.z = grp.valToDouble();
	}
	break;

      case 40:
	if (lastKeyword.equals("$LTSCALE")) {
	  LTSCALE = grp.valToDouble();
	}
	else if (lastKeyword.equals("$PDSIZE")) {
	  PDSIZE = grp.valToDouble();
	}
	break;

      case 70:
	if (lastKeyword.equals("$FILLMODE")) {
	  FILLMODE = grp.valToInt();
	}
	else if (lastKeyword.equals("$PDMODE")) {
	  PDMODE = grp.valToInt();
	}
	break;
      }
    }
  }

  /**
   *  Get the codepage defined by the $DWGCODEPAGE variable in the header.
   *  @return code page shortcut
   */
  public int getCodePage() {
    if (codePage == -1) {
      // get it
      if (DWGCODEPAGE.equals("DOS850")) {
	codePage = UC_DOS850;
      }
      else if (DWGCODEPAGE.equals("DOS852")) {
	codePage = UC_DOS852;
      }
      else if (DWGCODEPAGE.equals("DOS855")) {
	codePage = UC_DOS855;
      }
      else if (DWGCODEPAGE.equals("DOS857")) {
	codePage = UC_DOS857;
      }
      else if (DWGCODEPAGE.equals("DOS860")) {
	codePage = UC_DOS860;
      }
      else if (DWGCODEPAGE.equals("DOS861")) {
	codePage = UC_DOS861;
      }
      else if (DWGCODEPAGE.equals("DOS863")) {
	codePage = UC_DOS863;
      }
      else if (DWGCODEPAGE.equals("DOS864")) {
	codePage = UC_DOS864;
      }
      else if (DWGCODEPAGE.equals("DOS865")) {
	codePage = UC_DOS865;
      }
      else if (DWGCODEPAGE.equals("DOS869")) {
	codePage = UC_DOS869;
      }
      else if (DWGCODEPAGE.equals("ISO8859_1")) {
	codePage = UC_ISO_1;
      }
      else if (DWGCODEPAGE.equals("ISO8859_2")) {
	codePage = UC_ISO_2;
      }
      else if (DWGCODEPAGE.equals("MAC_ROMAN")) {
	codePage = UC_MAC;
      }
      else {
	codePage = UC_DOS437;
      }
    }

    return codePage;
  }

  /**
   *  Get the variable $ACADVER. This is the version number of the DXF file.
   *  It is typically AC1009 for DXF 11/12.
   *  @return $ACADVER
   */
  public String getACADVER() {
    return ACADVER;
  }

  /**
   *  Get the variable $ATTMODE. This is defining the visibility of attributes
   *  (ATTDEF/ATTRIB).
   *  @return $ATTMODE <br>
   *          0: attributes are always invisible <br>
   *          1: attribute visibility by entities <br>
   *          2: attributes are always visible
   */
  public int getATTMODE() {
    return ATTMODE;
  }

  /**
   *  Get the variable $DWGCODEPAGE. This is defining the codepage taken
   *  when displaying TEXTs. Better use the getCodePage method.
   *  @return $DWGCODEPAGE
   *  @see DxfHeader$getCodePage
   */
  public String getDWGCODEPAGE() {
    return DWGCODEPAGE;
  }

  /**
   *  Get the maximum extension point $EXTMAX as defined in the header section.
   *  Not cloned for efficiency reasons.
   *  @return $EXTMAX
   */
  public Point3D getEXTMAX() {
    return EXTMAX;
  }

  /**
   *  Get the minimum extension point $EXTMIN as defined in the header section.
   *  Not cloned for efficiency reasons.
   *  @return $EXTMIN
   */
  public Point3D getEXTMIN() {
    return EXTMIN;
  }

  /**
   *  Get the fill mode variable $FILLMODE. It determines wether POLYLINEs,
   *  TRACEs and SOLIDs are drawn filled.
   *  @return $FILLMODE <br>
   *	      <code>false</code>: don't fill <br>
   *          <code>true</code>: fill
   */
  public boolean getFILLMODE() {
    return FILLMODE != 0;
  }

  /**
   *  Get the insertion base point $INSBASE. In DXF it is possible to
   *  insert a complete DXF file into another. <em>This is not supported yet</em>.
   *  This is the insertion point.
   *  Not cloned for efficiency reasons.
   *  @return $INSBASE
   */
  public Point3D getINSBASE() {
    return INSBASE;
  }

  /**
   *  Get the point drawing mode $PDMODE. It determines which symbols should
   *  be drawn for POINTs.
   *  <p>
   *  PDMODE is an addition of two values.
   *  Value 1 stands for the actual point marking:<br>
   *  0:  a dot at the point position<br>
   *  1:  nothing <br>
   *  2:  a + (plus) with the crossing at the point position <br>
   *  3:  a X (cross) with the crossing at the point position <br>
   *  4:  a <code>'</code> with the lower end at the point position.
   *  <p>
   *  Value 2 stands for a kind of border:<br>
   *  0:  no border <br>
   *  32: a circle with the center at the point position <br>
   *  64: a square with the center at the point position <br>
   *  96: a square around a circle with the center at the point position
   *  <br>
   *  E.g. a 1 means <em>no symbol</em>, 34 means a + (plus) with a circle.
   *  @return $PDMODE
   *  @see DxfHeader$getPDSIZE
   */
  public int getPDMODE() {
    return PDMODE;
  }

  /**
   *  Get the point drawing size $PDSIZE. This determines how big POINT
   *  symbols should be drawn.
   *  @return $PDSIZE
   *  @see DxfHeader$getPDMODE
   */
  public double getPDSIZE() {
    return PDSIZE;
  }

  /**
   *  Get the global scaling factor for LSTYLEs.
   *  @return  LTSCALE value
   */
  public double getLTSCALE() {
    return LTSCALE;
  }
}


