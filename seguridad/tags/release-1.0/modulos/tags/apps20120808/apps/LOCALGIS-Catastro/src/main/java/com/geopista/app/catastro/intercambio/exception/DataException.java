package com.geopista.app.catastro.intercambio.exception;

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
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), I18N.get("Importacion","DataError.Aviso"), StringUtil.stackTrace(this));
    }
    
    public DataException(String msg)
    {
        super(msg);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public DataException(String msg, Throwable cause)
    {
        super(msg, cause);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public DataException (Throwable cause)
    {
        super(cause);
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), I18N.get("Importacion","DataError.Aviso"), StringUtil.stackTrace(this));
    }    
}
