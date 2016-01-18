package com.localgis.mapscript;

import java.io.*;
import edu.umn.gis.mapscript.*;


public class MapScriptTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("loading mapscript library...");
		System.loadLibrary("mapscript");
		System.out.println("mapscript library LOADED...");
		mapObj  map;
		imageObj image;
		map=new mapObj("c:\\tmp\\localgis_public_77_252.map");
		System.out.println("Created new mapObj()...");
		image=map.draw();
		image.save("eiel_municipio.png",map);
		}
}
