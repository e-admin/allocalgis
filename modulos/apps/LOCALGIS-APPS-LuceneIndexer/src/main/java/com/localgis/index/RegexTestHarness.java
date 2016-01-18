/**
 * RegexTestHarness.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.index;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestHarness {

    public static void main(String[] args){

//        while (true) {

            //Pattern pattern =   Pattern.compile(console.readLine("%nEnter your regex: "));
        	//SELECT id, id_via, numvia, denominacion, fechaalta, fechabaja, id_municipio, transform("GEOMETRY", 4230) as "GEOMETRY", length, valido, fuente FROM TramosVia  WHERE TramosVia.ID_Municipio in (33002) and valido = 1
        	String PATTERN="[\"A-Za-z_]*\\.\"GEOMETRY\"";
            String TEXT="eielcabast_rd.\"GEOMETRY\", 4258)";
            Pattern pattern=Pattern.compile(PATTERN);
            System.out.println("Pattern: "+PATTERN);
            System.out.println("Text: "+TEXT);
            
            //Matcher matcher =  pattern.matcher(console.readLine("Enter input string to search: "));
            
            Matcher matcher =  pattern.matcher(TEXT);

            boolean found = false;
            while (matcher.find()) {
                System.out.println("I found the text" +matcher.group()+
                                   " starting at " + matcher.start()+
                                   "index %d and ending at index %d.%n"+matcher.end());
                found = true;
            }
            if(!found){
            	System.out.println("NO MATCH found.%n");
            }

        }
  //  }
}