package com.geopista.app.catastro.gestorcatastral;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 12-ene-2007
 * Time: 14:00:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interfaz que implementan los elementos de la gui para cambiar sus etiquetas dinamicamente cuando el usuario cambie
 * el idioma de la aplicacion.
 */

public interface IMultilingue
{
    /**
     * Metodo que deben implementar para cambiar los label que poseen las interfaces.
     */
    public void renombrarComponentes();
}
