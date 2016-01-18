package com.geopista.app.catastro.intercambio.exception;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.dialogs.ErrorNewAttemptDialog;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

public class ErrorNewAttemptException extends Exception
{   
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public ErrorNewAttemptException()
    {
        super();        
        ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), I18N.get("Importacion","DataError.Aviso"), StringUtil.stackTrace(this));
    }
    
    public ErrorNewAttemptException(String msg)
    {
        super(msg);
        ErrorNewAttemptDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public ErrorNewAttemptException(String msg, Throwable cause)
    {
        super(msg, cause);
        ErrorNewAttemptDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), msg, StringUtil.stackTrace(this));
    }
    public ErrorNewAttemptException (Throwable cause)
    {
        super(cause);
        ErrorNewAttemptDialog.show(aplicacion.getMainFrame(), I18N.get("Importacion","DataError.Titulo"), I18N.get("Importacion","DataError.Aviso"), StringUtil.stackTrace(this));
    }    
}
