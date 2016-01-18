package com.localgis.index;

import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTestHarness {

    public static void main(String[] args){

//        while (true) {

            //Pattern pattern =   Pattern.compile(console.readLine("%nEnter your regex: "));
        	//SELECT id, id_via, numvia, denominacion, fechaalta, fechabaja, id_municipio, transform("GEOMETRY", 4230) as "GEOMETRY", length, valido, fuente FROM TramosVia  WHERE TramosVia.ID_Municipio in (33002) and valido = 1
        	String PATTERN="[\"A-Za-z_]*\\.\"GEOMETRY\"";
            String TEXT="eielcabast_rd.\"GEOMETRY\", 4230)";
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