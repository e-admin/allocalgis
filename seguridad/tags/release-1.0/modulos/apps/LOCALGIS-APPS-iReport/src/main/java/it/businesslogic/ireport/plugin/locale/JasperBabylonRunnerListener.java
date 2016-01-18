/*
 * JasperBabylonRunnerListener.java
 *
 * Created on November 29, 2006, 6:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.plugin.locale;

/**
 *
 * @author gtoffoli
 */
public interface JasperBabylonRunnerListener {
        public void listLocalesComplete(java.util.List list);
        public void getLocaleComplete(java.util.Properties props);
        public void putLocaleComplete(String returnMessage);
        public void operationError(int operation, String message);

}
