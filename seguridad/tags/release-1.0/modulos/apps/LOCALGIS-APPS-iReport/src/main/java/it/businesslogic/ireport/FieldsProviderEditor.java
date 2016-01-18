/*
 * FieldsProviderEditor.java
 *
 * Created on December 7, 2006, 8:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.ReportQueryDialog;

/**
 *
 * @author gtoffoli
 */
public interface FieldsProviderEditor {
    
       /**
        * the method can be not executed in the AWT Event Dispatcher Thread.
        * Subsequent calls of this method can be placed. I suggest to catch a Throwable to
        * clean up if the thread is interrupted.
        */
        public void queryChanged(String newQuery);
}
