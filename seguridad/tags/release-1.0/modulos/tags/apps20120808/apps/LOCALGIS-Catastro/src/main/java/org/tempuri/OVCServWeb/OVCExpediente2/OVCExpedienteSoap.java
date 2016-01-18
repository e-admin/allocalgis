/**
 * OVCExpedienteSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri.OVCServWeb.OVCExpediente2;

public interface OVCExpedienteSoap extends java.rmi.Remote {
    public es.meh.catastro.www.Expedientes_Out generaExpediente(es.meh.catastro.www.Expedientes_In xmlPeticion) throws java.rmi.RemoteException;
    public es.meh.catastro.www.ConsultaCatastro_Out consultaCatastro(es.meh.catastro.www.ConsultaCatastro_In xmlPeticion) throws java.rmi.RemoteException;
    public es.meh.catastro.www.ActualizaCatastro_Out actualizaCatastro(es.meh.catastro.www.ActualizaCatastro_In xmlPeticion) throws java.rmi.RemoteException;
    public es.meh.catastro.www.ConsultaExpediente_Out consultaExpediente(es.meh.catastro.www.ConsultaExpediente_In xmlPeticion) throws java.rmi.RemoteException;
}
