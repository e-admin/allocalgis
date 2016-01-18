package es.satec.localgismobile.utils;

import java.io.File;

public class LocalGISUtils {

	public static String slashify(String absolutePath, boolean directory) {
		String p = absolutePath;
		if (File.separatorChar != '/') {
			p = p.replace(File.separatorChar, '/');
		}
		if (!p.startsWith("/")) {
			p = "/" + p;
		}
		if (!p.endsWith("/") && directory) {
			p = p + "/";
		}
		return p;
	}
	//Borra un directorio completo
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            
            //Borra todos los ficheros del directorio
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        //Ahora que el directorio está vacío, se puede borrar
        return dir.delete();
    }


}
