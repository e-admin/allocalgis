package com.geopista.app.inventario;

import java.util.Enumeration;

import javax.swing.JInternalFrame;

import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;

public interface IMainInventario {
  
    public void startApp(boolean fromInicio); 
    public void setConnectionStatusMessage(boolean connected);
    public void setConnectionInitialStatusMessage (boolean connected);
    public boolean sugerenciasActionPerformed();    
    public boolean mostrarJInternalFrame(JInternalFrame internalFrame);
    public void resetSecurityPolicy();
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal) ;
    public boolean tienePermisos(String permiso);
    public void setPermisos(Enumeration e);
    public JInternalFrame getIFrame();

}
