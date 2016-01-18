package com.geopista.ui.plugin;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaConsultaInformacionParcelaOVC;
import com.geopista.app.catastro.GeopistaPeticionDatosConsultaCatastro;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaUtil;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class consultarOVCPlugIn extends AbstractPlugIn
{

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaNewMapPlugIn.category";

    private String refCat;

    public consultarOVCPlugIn()
        {
        }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
                workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
                .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
                .add(
                        checkFactory
                                .createExactlyOneFeaturesInLayerParcelasMustBeSelectedCheck());
    }

    public boolean execute(PlugInContext context)
    {

        // Supongo que viene una referencia catastral
        GeopistaPeticionDatosConsultaCatastro datosPeticionCatastro = new GeopistaPeticionDatosConsultaCatastro();
        GeopistaConsultaInformacionParcelaOVC consultaDatos = new GeopistaConsultaInformacionParcelaOVC();
        // Referencia Catastral de Ejemplo

        Collection listaParcelas = context.getLayerViewPanel().getSelectionManager()
                .getFeaturesWithSelectedItems();
        GeopistaFeature featureRefCat = new GeopistaFeature();

        Iterator bucle = listaParcelas.iterator();
        featureRefCat = (GeopistaFeature) bucle.next();
        GeopistaSchema nombreEsquema = (GeopistaSchema) featureRefCat.getSchema();
        String nombreAtributo = nombreEsquema
                .getAttributeByColumn("referencia_catastral");
        refCat = (String) featureRefCat.getAttribute(nombreAtributo);

        try
        {
            datosPeticionCatastro = consultaDatos.obtenerInformacionOVC(featureRefCat);
            consultaDatos.crearXMLConsultaCatastro(datosPeticionCatastro);
        } catch (IndexOutOfBoundsException e)
        {
            ErrorDialog
                    .show(
                            aplicacion.getMainFrame(),
                            aplicacion
                                    .getI18nString("ConsultarOVCPlugIn.DatosParcelasIncorrectos"),
                            aplicacion
                                    .getI18nString("ConsultarOVCPlugIn.DatosParcelasIncorrectos"),
                            StringUtil.stackTrace(e));
        } catch (KeyStoreException e)
        {
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("ConsultarOVCPlugIn.KeyStoreError"), aplicacion
                    .getI18nString("ConsultarOVCPlugIn.ProblemaConOVC"), StringUtil
                    .stackTrace(e));
        }
        
        catch (FileNotFoundException e)
        {
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("ConsultarOVCPlugIn.KeyFileError"), aplicacion
                    .getI18nString("ConsultarOVCPlugIn.ProblemaConOVC"), StringUtil
                    .stackTrace(e));
        }

        return true;
    }

    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());
        featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("File"),
                GeopistaUtil.i18n_getname(this.getName()) + "...", null,
                createEnableCheck(context.getWorkbenchContext()));

    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("Nuevo_mapa.GIF");
    }
}
