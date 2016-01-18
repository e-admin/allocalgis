/**
 * FileUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileUtils {

    /**
     * Método para escribir el contenido de un fichero como String
     * @param content El contenido a escribir como un String
     * @param fileName El fichero a escribir
     * @param charset El juego de caracteres a usar
     * @throws FileNotFoundException Si el fichero no se encuentra
     */
    public static void writeContentToFile(String content, String fileName, String charset) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        if (charset != null) {
            byte[] bytes;
            try {
                bytes = content.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                bytes = content.getBytes();
            }
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } else {
            PrintWriter printWriter;
            printWriter = new PrintWriter(fileOutputStream);
            printWriter.write(content);
            printWriter.flush();
            printWriter.close();
        }
    }
    
    /**
     * Método para leer como un String el contenido de un fichero
     * @param fileName El fichero a leer
     * @return  El contenido de un fichero como un String 
     * @throws FileNotFoundException Si el fichero no se encuentra
     * @throws IOException Si ocurre algún error de entrada/salida
     */
    public static String readContentFromFile(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        FileInputStream fis;
        fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int size = bis.available();
        byte buffer[] = new byte[size];
        bis.read(buffer);
        String result = new String(buffer);
        bis.close();
        fis.close();
        return result;
    }

    /**
     * Método para leer como un String el contenido de un recurso
     * @param resourceName El recurso a leer
     * @return  El contenido de un fichero como un String 
     * @throws IOException Si ocurre algún error de entrada/salida
     */
    public static String readContentFromResource(String resourceName) throws IOException {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(resourceName);
        }
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            int size = bis.available();
            byte buffer[] = new byte[size];
            bis.read(buffer);
            String result = new String(buffer);
            bis.close();
            is.close();
            return result;
        }
        return null;
    }
}
