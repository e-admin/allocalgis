/**
 * DataException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.exception;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class DataException extends Exception
{   
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public DataException()
    {
        super();        
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","DataError.Titulo"), I18N.get("GestorCapas","DataError.Aviso"), StringUtil.stackTrace(this));
    }
    
    public DataException(String msg)
    {
        super(msg);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public DataException(String msg, Throwable cause)
    {
        super(msg, cause);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public DataException (Throwable cause)
    {
        super(cause);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("GestorCapas","DataError.Titulo"), I18N.get("GestorCapas","DataError.Aviso"), StringUtil.stackTrace(this));
    }    
}
