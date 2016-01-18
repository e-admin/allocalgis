/**
 * JNCSFile.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ermapper.ecw;

import com.ermapper.util.JNCSDatasetPoint;
import com.ermapper.util.JNCSWorldPoint;

/**
 * Clase que representa un fichero ECW.
 */
public class JNCSFile implements JNCSProgressiveUpdate {

    private native int ECWOpen(String s, boolean flag);
    
    private native int ECWOpenArray(String s, boolean flag, byte[] b);

    private native void ECWClose(boolean flag);

    private native int ECWSetView(int i, int ai[], int j, int k, int l, int i1, double d, double d1, double d2, double d3, 
            int j1, int k1);

    private native int ECWReadLineRGBA(int ai[]);

    private native int ECWReadImageRGBA(int ai[], int i, int j);

    //private native short ECWGetPercentComplete();

    private static native String ECWGetErrorString(int i);

    private static native int NCSJNIInit();

    //private static native String ECWGetLibVersion();

    /**
     * Carga la libreria jecwppc.dll que contiene el wrapper y llama a la
     * función de inicialización
     * @throws JNCSNativeLibraryException
     */
    static void initClass() throws JNCSNativeLibraryException {

//         final String LIB_PATH = Global.APP_PATH + File.separator + "lib" + File.separator;
         try {
//        	 System.load("C:\\trabajo\\LOCALGIS\\LOCALGIS_DOS\\3Codigo\\Modulos\\LocalGISClientMobile\\src\\main\\lib\\NCSUtil.dll");
//        	 System.load("C:\\trabajo\\LOCALGIS\\LOCALGIS_DOS\\3Codigo\\Modulos\\LocalGISClientMobile\\src\\main\\lib\\NCScnet.dll");
//        	 System.load("C:\\trabajo\\LOCALGIS\\LOCALGIS_DOS\\3Codigo\\Modulos\\LocalGISClientMobile\\src\\main\\lib\\NCSEcw.dll");
//        	 System.load("C:\\trabajo\\LOCALGIS\\LOCALGIS_DOS\\3Codigo\\Modulos\\LocalGISClientMobile\\src\\main\\lib\\jecw.dll");
        	 
//        	 String path = System.getProperty("java.library.path");
//             String otherPath = path + ";" + LIB_PATH;
//
//             // Añadir a lo que ya había el directorio donde se encuentra la dll que nos interesa
//             Properties props = System.getProperties();
//             props.put("java.library.path", otherPath);
//
//             System.setProperties(props);
//
//             System.loadLibrary("NCSUtil");
//             System.loadLibrary("NCScnet");
//             System.loadLibrary("NCSEcw");
//             System.loadLibrary("jecwppc");
//             
//             // Devolver la variable a su valor original
//             props.put("java.library.path", path);
//             System.setProperties(props);
             
             
             bUseNativeMethods = true;
             bUnsatisfiedLink = false;
             int i = NCSJNIInit();
             if(i != 0)
             {
                 System.err.println("JNCSFile classes found on PATH failed to initialize correctly. Attempting to locate other libecw.so....");
             }
         }
         catch(Exception e)
         {
         	e.printStackTrace();
         }
    }

    /**
     * Crea una instancia de la clase sin asociarla a un fichero.
     */
    public JNCSFile() throws JNCSException {
        bSetViewIsWorld = false;
        progImageClient = null;
        initClass();
        cellSizeUnits = 0;
        bIsOpen = false;
    }

    /**
     * Crea una instancia de la clase y abre el fichero especificado.
     * @param s Ruta de la imagen ECW a abrir
     * @param flag Indica si la carga de la imagen debe ser progresiva o bloqueante
     * @throws JNCSException Si se produce un error al abrir el fichero
     */
    public JNCSFile(String s, boolean flag) throws JNCSException {
        bSetViewIsWorld = false;
        progImageClient = null;
        initClass();
        open(s, flag);
    }

    /**
     * Llama al close del ECW para liberar la memoria de C.
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        if(bIsOpen) ECWClose(false);
        super.finalize();
    }

    /**
     * Abre el fichero de imagen
     * @param s Ruta de la imagen
     * @param flag Indica si la carga de la imagen debe ser progresiva o bloqueante
     * @return 0 si todo va bien
     * @throws JNCSFileOpenFailedException Si se produce un error al abrir el fichero
     */
    public int open(String s, boolean flag) throws JNCSFileOpenFailedException {
        
    	if(s == null) throw new IllegalArgumentException();
        
        //int i = ECWOpen(s, flag);
        int i = ECWOpenArray(s, flag, s.getBytes());
        
        if(i != 0) {
            bIsOpen = false;
            String s1 = JNCSError.getError(i);
            throw new JNCSFileOpenFailedException(s1);
        }
        else {
            bIsOpen = true;
            progressive = flag;
            visible = true;
            return 0;
        }
        
    }

    /**
     * Cierra el fichero de imagen.
     * @param flag Parámetro para la función NCScbmCloseFileViewEx. true para forzar a cerrarlo y que no lo cachee.
     */
    public void close(boolean flag) {
        ECWClose(flag);
        if(!flag);
    }
    
    /**
     * Añade un listener para la carga progresiva
     * @param jncsprogressiveupdate El listener
     */
    public void addProgressiveUpdateListener(JNCSProgressiveUpdate jncsprogressiveupdate) {
        progImageClient = jncsprogressiveupdate;
    }

    public void refreshUpdate(int i, int j, double d, double d1, double d2, double d3) {
        if(progImageClient != null)
            progImageClient.refreshUpdate(i, j, d, d1, d2, d3);
    }

    public void refreshUpdate(int i, int j, int k, int l, int i1, int j1) {
        if(progImageClient != null)
            progImageClient.refreshUpdate(i, j, k, l, i1, j1);
    }

    /**
     * Crea una vista en un fichero ECW abierto dando las coordenadas de la vista
     * @param nBands Número de bandas en bandList
     * @param bandList Array de índices de bandas
     * @param tlx coordenada X arriba-izquierda de la vista
     * @param tly coordenada Y arriba-izquierda de la vista
     * @param brx coordenada X abajo-derecha de la vista
     * @param bry coordenada Y abajo-derecha de la vista
     * @param width ancho de la vista en pixels
     * @param height alto de la vista en pixels
     * @throws JNCSFileNotOpenException Si el fichero no está abierto
     * @throws JNCSInvalidSetViewException Si la vista no es válida
     */
    
    public int setView(int nBands, int bandList[], int tlx, int tly, int brx, int bry, int width, int height)
        throws JNCSFileNotOpenException, JNCSInvalidSetViewException
    {
        int l1 = ECWSetView(nBands, bandList, tlx, tly, brx, bry, 0.0D, 0.0D, 0.0D, 0.0D, width, height);
        if(l1 != 0) {
            //String s = ECWGetErrorString(l1);
            String s = JNCSError.getError(l1);
            throw new JNCSInvalidSetViewException(s);
        }
        else {
            bSetViewIsWorld = false;
            return 0;
        }
    }

    /**
     * Crea una vista en un fichero ECW abierto dando las coordenadas del mundo real
     * @param nBands Número de bandas en bandList
     * @param bandList Array de índices de bandas
     * @param dWorldTLX coordenada X arriba-izquierda)
     * @param dWorldTLY coordenada Y arriba-izquierda
     * @param dWorldBRX coordenada X abajo-derecha)
     * @param dWorldBRY coordenada Y abajo-derecha
     * @param width ancho de la vista en pixels
     * @param height alto de la vista en pixels
     * @throws JNCSFileNotOpenException Si el fichero no está abierto
     * @throws JNCSInvalidSetViewException Si la vista no es válida
     */
    public int setView(int nBands, int bandList[], double dWorldTLX, double dWorldTLY, double dWorldBRX, double dWorldBRY, int width, int height)
        throws JNCSFileNotOpenException, JNCSInvalidSetViewException
    {
        JNCSDatasetPoint jncsdatasetpoint = convertWorldToDataset(dWorldTLX, dWorldTLY);
        JNCSDatasetPoint jncsdatasetpoint1 = convertWorldToDataset(dWorldBRX, dWorldBRY);
        int l = ECWSetView(nBands, bandList, jncsdatasetpoint.x, jncsdatasetpoint.y, jncsdatasetpoint1.x, jncsdatasetpoint1.y, dWorldTLX, dWorldTLY, dWorldBRX, dWorldBRY, width, height);
        if(l != 0) {
            //String s = ECWGetErrorString(l);
            String s = JNCSError.getError(l);
            throw new JNCSInvalidSetViewException(s);
        }
        else {
            bSetViewIsWorld = true;
            return 0;
        }
    }

    /**
     * Lee una línea del fichero ECW en formato RGBA
     * @param buffer Buffer donde se almacenan los datos de la línea
     * @throws JNCSException
     */
    public int readLineRGBA(int buffer[]) throws JNCSException {
        int i = ECWReadLineRGBA(buffer);
        if(i != 0) {
            String s = JNCSError.getError(i);
            throw new JNCSException(s);
        }
        else {
            return 0;
        }
    }

    /*public int readLineBGRA(int ai[])
        throws JNCSException
    {
        throw new JNCSException("Not Yet Implemented!");
    }

    public int readLineBIL(int ai[])
        throws JNCSException
    {
        throw new JNCSException("Not Yet Implemented!");
    }

    public int readLineBIL(double ad[])
        throws JNCSException
    {
        throw new JNCSException("Not Yet Implemented!");
    }*/

    /**
     * Lee la imagen ECW completa en formato RGBA
     * @param buffer Buffer donde se almacenan los datos de la imagen
     * @param i Ancho de la imagen
     * @param j Alto de la imagen
     */
    public int readImageRGBA(int buffer[], int i, int j) throws JNCSException {
        int k = ECWReadImageRGBA(buffer, i, j);
        if(k != 0) {
            String s = JNCSError.getError(k);
            throw new JNCSException(s);
        }
        else {
            return 0;
        }
    }

    /**
     * Obtiene una cadena que corresponde a un error a través del entero que lo representa
     * @return String Cadena de error
     * @param error	Entero que representa el error
     */
    public String getLastErrorText(int i)
    {
        return JNCSError.getError(i);
    }

    /**
     * Convierte una coordenada del mundo real a coordenadas de la vista
     * @return JNCSDatasetPoint	Clase que representa a un punto en la imagen
     * @param x	Coordenada X del mundo real
     * @param y Coordenada Y del mundo real
     * @throws JNCSFileNotOpenException Si el fichero no está abierto
     */
    public JNCSDatasetPoint convertWorldToDataset(double x, double y) throws JNCSFileNotOpenException
    {
        int i;
        int j;
        if(bIsOpen) {
            i = (int)Math.round((x - originX) / cellIncrementX);
            j = (int)Math.round((y - originY) / cellIncrementY);
        }
        else {
            throw new JNCSFileNotOpenException();
        }
        return new JNCSDatasetPoint(i, j);
    }

    /**
     * Convierte una coordenada de la vista a coordenadas del mundo real
     * @return JNCSWorldPoint Clase que representa una coordenada del mundo real
     * @param i	Coordenada X de la imagen
     * @param j Coordenada Y de la imagen
     * @throws JNCSFileNotOpenException Si el fichero no está abierto
     */
    public JNCSWorldPoint convertDatasetToWorld(int i, int j) throws JNCSFileNotOpenException {
        double d;
        double d1;
        if(bIsOpen) {
            d = originX + (double)i * cellIncrementX;
            d1 = originY + (double)j * cellIncrementY;
        }
        else {
            throw new JNCSFileNotOpenException();
        }
        return new JNCSWorldPoint(d, d1);
    }

//    public short getPercentComplete()
//    {
//        return ECWGetPercentComplete();
//    }
//
//    /**
//     * Obtiene una cadena con la versión de la libreria
//     * @return versión
//     */
//
//    public static String getLibVersion()
//    {
//        return ECWGetLibVersion();
//    }
//
//    private static void debug(String s)
//    {
//        if(debug)
//            System.out.println(s);
//    }

    public boolean isVisible() {
    	return visible;
    }
    
    public void setVisible(boolean visible) {
    	this.visible = visible;
    }
    
    private static boolean bUseNativeMethods = false;
    private static boolean bSecurityError = false;
    private static boolean bUnsatisfiedLink = false;
    static boolean bHaveClassInit = false;
    static boolean debug = false;
    public static final int ECW_CELL_UNITS_INVALID = 0;
    public static final int ECW_CELL_UNITS_METERS = 1;
    public static final int ECW_CELL_UNITS_DEGREES = 2;
    public static final int ECW_CELL_UNITS_FEET = 3;
    public int numBands;
    public int width;
    public int height;
    public double originX;
    public double originY;
    public double cellIncrementX;
    public double cellIncrementY;
    public int cellSizeUnits;
    public double compressionRate;
    public boolean progressive;
    public String fileName;
    public String datum;
    public String projection;
    public boolean bIsOpen;
    private long nativeDataPointer;
    private static final boolean doGarbageCollectionOnClose = false;
    private static final int ECW_OK = 0;
    private int nFileSetViewDatasetTLX;
    private int nFileSetViewDatasetTLY;
    private int nFileSetViewDatasetBRX;
    private int nFileSetViewDatasetBRY;
    private int nFileSetViewWidth;
    private int nFileSetViewHeight;
    private double dFileSetViewWorldTLX;
    private double dFileSetViewWorldTLY;
    private double dFileSetViewWorldBRX;
    private double dFileSetViewWorldBRY;
    private boolean bSetViewIsWorld;
    protected JNCSProgressiveUpdate progImageClient;
    private boolean visible;
}
