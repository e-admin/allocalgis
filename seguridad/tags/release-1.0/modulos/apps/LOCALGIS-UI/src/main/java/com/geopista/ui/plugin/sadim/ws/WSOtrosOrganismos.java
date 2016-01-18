package com.geopista.ui.plugin.sadim.ws;

public interface WSOtrosOrganismos extends java.rmi.Remote {
    public java.lang.String obtenerInformacionCentroAsistencial(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException;
    public java.lang.String obtenerInformacionEducacion(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException;
    public java.lang.String obtenerInformacionIneSadei(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException;
    public java.lang.String obtenerInformacionCentroSanitario(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException;
    public java.lang.String obtenerInformacionTurismo(java.lang.String codprov, java.lang.String codmunic, int fecha) throws java.rmi.RemoteException;
}
