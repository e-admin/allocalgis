/**
 * EncodeUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodeUtil {

	public static String encodeURl(String originalURL) throws UnsupportedEncodingException

    {
 
        String mapaEncode = originalURL.replaceAll(" ", "%20");

        String encoding = "ISO8859-1";

        mapaEncode = mapaEncode.replaceAll("á", URLEncoder.encode("á",encoding));

        mapaEncode = mapaEncode.replaceAll("é", URLEncoder.encode("é",encoding));

        mapaEncode = mapaEncode.replaceAll("í", URLEncoder.encode("í",encoding));

        mapaEncode = mapaEncode.replaceAll("ó", URLEncoder.encode("ó",encoding));

        mapaEncode = mapaEncode.replaceAll("ú", URLEncoder.encode("ú",encoding));

        mapaEncode = mapaEncode.replaceAll("à", URLEncoder.encode("à",encoding));

        mapaEncode = mapaEncode.replaceAll("è", URLEncoder.encode("è",encoding));

        mapaEncode = mapaEncode.replaceAll("ì", URLEncoder.encode("ì",encoding));

        mapaEncode = mapaEncode.replaceAll("ò", URLEncoder.encode("ò",encoding));

        mapaEncode = mapaEncode.replaceAll("ù", URLEncoder.encode("ù",encoding));

        mapaEncode = mapaEncode.replaceAll("ç", URLEncoder.encode("ç",encoding));

        mapaEncode = mapaEncode.replaceAll("ñ", URLEncoder.encode("ñ",encoding));

        mapaEncode = mapaEncode.replaceAll("ä", URLEncoder.encode("ä",encoding));

        mapaEncode = mapaEncode.replaceAll("ë", URLEncoder.encode("ë",encoding));

        mapaEncode = mapaEncode.replaceAll("ï", URLEncoder.encode("ï",encoding));

        mapaEncode = mapaEncode.replaceAll("ö", URLEncoder.encode("ö",encoding));

        mapaEncode = mapaEncode.replaceAll("ü", URLEncoder.encode("ü",encoding));

 

        mapaEncode = mapaEncode.replaceAll("Á", URLEncoder.encode("Á",encoding));

        mapaEncode = mapaEncode.replaceAll("É", URLEncoder.encode("É",encoding));

        mapaEncode = mapaEncode.replaceAll("Í", URLEncoder.encode("Í",encoding));

        mapaEncode = mapaEncode.replaceAll("Ó", URLEncoder.encode("Ó",encoding));

        mapaEncode = mapaEncode.replaceAll("Ú", URLEncoder.encode("Ú",encoding));

        mapaEncode = mapaEncode.replaceAll("À", URLEncoder.encode("À",encoding));

        mapaEncode = mapaEncode.replaceAll("È", URLEncoder.encode("È",encoding));

        mapaEncode = mapaEncode.replaceAll("Ì", URLEncoder.encode("Ì",encoding));

        mapaEncode = mapaEncode.replaceAll("Ò", URLEncoder.encode("Ò",encoding));

        mapaEncode = mapaEncode.replaceAll("Ù", URLEncoder.encode("Ù",encoding));

        mapaEncode = mapaEncode.replaceAll("Ñ", URLEncoder.encode("Ñ",encoding));

        mapaEncode = mapaEncode.replaceAll("Ä", URLEncoder.encode("Ä",encoding));

        mapaEncode = mapaEncode.replaceAll("Ë", URLEncoder.encode("Ë",encoding));

        mapaEncode = mapaEncode.replaceAll("Ï", URLEncoder.encode("Ï",encoding));

        mapaEncode = mapaEncode.replaceAll("Ö", URLEncoder.encode("Ö",encoding));

        mapaEncode = mapaEncode.replaceAll("Ü", URLEncoder.encode("Ü",encoding));

 

        return mapaEncode;

    }


}

