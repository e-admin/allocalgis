package com.geopista.app.licencias;

import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.security.GeopistaPrincipal;

public class CConstantesLicencias_LCGIII {

    public static CPersonaJuridicoFisica persona = null;
    public static CPersonaJuridicoFisica representante = null;
    public static CPersonaJuridicoFisica tecnico = null;
    public static CPersonaJuridicoFisica promotor = null;

    /** Usuario de login */
    public static GeopistaPrincipal principal= new GeopistaPrincipal();

    /** Referencia Catastral Selecciona */
    public static CReferenciaCatastral referencia = new CReferenciaCatastral();

}