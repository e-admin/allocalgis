package com.geopista.app.catastro.intercambio.importacion.utils;

public class ImportarUtils_LCGIII
{

    /**
     * Convierte un InputStream en String
     * @param is
     * @return
     */
	//LCGIII.Desplazado del paquete LOCALGIS-Catastro
    public String parseISToString(java.io.InputStream is){
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try{
            String line = null;
            while((line=din.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        return sb.toString();
    }
    
    
}

	
