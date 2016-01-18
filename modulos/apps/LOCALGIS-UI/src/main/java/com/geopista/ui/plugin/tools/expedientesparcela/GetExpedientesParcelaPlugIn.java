/**
 * GetExpedientesParcelaPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.app.licencias.CUtilidadesComponentes_LCGIII;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.Estado;
import com.geopista.ui.plugin.tools.expedientesparcela.vo.TipoExpediente;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * Clase que se ejecuta al pulsar en el plugin de Obtener Expedientes de una parcela.
 * 
 * @author fjcastro
 *
 */
public class GetExpedientesParcelaPlugIn extends AbstractPlugIn{

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();    
    
	public GetExpedientesParcelaPlugIn() {
    	Locale loc=Locale.getDefault();     

    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.tools.expedientesparcela.languages.GetExpedientesParcelaPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GetExpedientesParcelaPlugIn",bundle2);
	}

	/**
	 * Obtenemos, del fichero de properties, el nombre que aparecera en el menu 
	 */
    public String getName(){
    	String name = I18N.get("GetExpedientesParcelaPlugIn","GetExpedientesParcela");
        return name;
    }
    
    public boolean execute(PlugInContext context) throws Exception {
    	
    	// Obtenemos los datos de catastro de la parcela que hemos seleccionado:
    	
        Collection listaParcelas = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
        GeopistaFeature featureRefCat = new GeopistaFeature();

        Iterator bucle = listaParcelas.iterator();
        featureRefCat = (GeopistaFeature) bucle.next();
        
        CReferenciaCatastral referenciaCatastral = CUtilidadesComponentes_LCGIII.getReferenciaCatastral(featureRefCat);
		
    	System.out.println("RefCatastral2: "+referenciaCatastral.getReferenciaCatastral()+" / "+referenciaCatastral.getNombreVia() +" / "+ referenciaCatastral.getPuerta());

    	GetExpedientesParcelaSQL expParcelaSQL = new GetExpedientesParcelaSQL(aplicacion);
	    TipoExpediente[] tiposExpediente = expParcelaSQL.obtenerTiposExpedientes();
	    Estado[] estadosInforme = expParcelaSQL.obtenerEstadosInforme();
	    expParcelaSQL.cerrarConexion();
	    GetExpedientesParcelaDialog parcelaDialogo = new GetExpedientesParcelaDialog(aplicacion, tiposExpediente, estadosInforme, referenciaCatastral);
	    
	    return true;
    }
    
    /**
     * Inicializa el plugin.
     */
    public void initialize(PlugInContext context) throws Exception    {
        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
    	FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

        featureInstaller.addPopupMenuItem(popupMenu, this, this.getName(), false, null, GetExpedientesParcelaPlugIn.createEnableCheck(context.getWorkbenchContext()));
    }

    /**
     * Indicamos que solo esté activado cuando se seleccione 1 parcela
     */
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext){
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createExactlyNFeaturesInLayerParcelasMustBeSelectedCheck(1,Layer.class));
    }
    
}
