/**
 * DbfConsts.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.geotools.dbffile;
/** 
	* Constants for DbfFile
	*/
interface DbfConsts{
    static final int DBF_CENTURY=1900; //base century
    static final int DBF_MAXLEN=4096;
    static final int DBF_NAMELEN=11;
    static final int DBF3_MAXFIELDS=128;
    static final int DBF4_MAXFIELDS=255;
    static final int DBF_MAXFIELDS=255;
    static final int DBF_BUFFSIZE=32;
    static final int DBF_END_OF_DEFS=13;
    static final int DBF_OK=1;
    static final int DBF_READ_HEAD=-101;
    static final int DBF_BAD_DBFID=-102;
    static final int DBF_WRITE_HEAD=-103;
    static final int DBF_READ_DEFS=-111;
    static final int DBF_TOO_MANY_FIELDS=-112;
    static final int DBF_NO_FIELDS=-113;
    static final int DBF_BAD_EODEFS=-114;
    static final int DBF_WRITE_DEFS=-115;
    static final int DBF_BAD_ITYPE=-116;
    static final int DBF_CANNOT_DO_MEMO=-117;
    static final int DBF_BAD_INT_WIDTH=-118;
    static final int DBF_BAD_OFFSET=-119;
    static final int DBF_FLOATING_N=-120;
    static final int DBF_READ_DATA=-121;
    static final int DBF_UNPRINT_DATA=-122;
    static final int DBF_WRITE_DATA=-125;
    static final int DBF_INT_EXP=-126;
    static final int DBF_INT_REAL=-127;
    static final int DBF_INT_JUNK=-128;
    static final int DBF_REC_DELETED=-129;
    static final int DBF_ALL_DELETED=-130;
    static final int DBF_BAD_SIZEOF=-131;
    static final int DBF_REC_TOO_LONG=-132;
    static final int DBF_TOO_WIDE_FOR_INF=-133;
    static final int DBF_MALLOC_FIELD=-134;
    static final int DBF_MALLOC=-135;
    static final int DBF_GET_DATE=-150;
}
