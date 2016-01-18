package com.geopista.app.inventario;
import com.geopista.app.inventario.panel.GestionDocumentalJPanel;
import com.geopista.protocol.inventario.BienBean;

public interface BienJDialog {
   public BienBean getBien();
   public GestionDocumentalJPanel getDocumentosJPanel();
   public void setVisible(boolean ver);
}
