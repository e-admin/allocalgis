/**
 * Validacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class Validacion
{

    /**
     * Conexión a base de datos
     */
    private static Connection conn = null;

    private static DatosValoracion datosVal;

    public static String error;

    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private static boolean MensajesErroresCargados= false; // configuracion

    static private Validacion val = new Validacion();
    static public Validacion getValidation()
    {
        return val;
    }

    public Validacion()
    {
        initializeProperties();
        datosVal = new DatosValoracion();
    }

    /**
     * Obtiene una conexión a la base de datos
     */
    private static void getDBConnection ()
    {
        try
        {
            conn=  aplicacion.getConnection();
            conn.setAutoCommit(false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public static String validacionParcialFinca(FincaCatastro finca, Expediente expediente)
    {
        StringBuffer resultado = null;
        getDBConnection();
        ValidacionFinca validarFinca = new ValidacionFinca(datosVal, expediente.getTipoExpediente().getCodigoTipoExpediente());

        try
        {
        	resultado = validarFinca.validacionParcialFinca(conn, finca, String.valueOf(ConstantesCatastro.IdMunicipio));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado.toString();
    }
    
    public static String validacionParcialSuelo(FincaCatastro finca)
    {
        StringBuffer resultado = null;
        getDBConnection();
        ValidacionSuelo validarSuelo= new ValidacionSuelo(datosVal);

        try
        {
        	resultado = validarSuelo.validacionSuelo(conn, finca, String.valueOf(ConstantesCatastro.IdMunicipio));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado.toString();
    }

    public static String validacionParcialCultivos(FincaCatastro finca)
    {
        StringBuffer resultado = null;
        getDBConnection();
        ValidacionCultivos validarCultivo= new ValidacionCultivos();

        try
        {
        	resultado = validarCultivo.validacionCultivos(conn, finca);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado.toString();
    }
    
    public static String validacionParcialDerechos(FincaCatastro finca)
    {
        StringBuffer resultado = null;
        getDBConnection();
        ValidacionDerecho validarDerechos= new ValidacionDerecho();

        try
        {
        	resultado = validarDerechos.validacionDerechos(conn, finca);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado.toString();
    }
    
    public static String validacionParcialRepartos(FincaCatastro finca)
    {
        StringBuffer resultado = null;
        getDBConnection();
        ValidacionRepartos validarRepartos = new ValidacionRepartos();

        try
        {
        	resultado = validarRepartos.validacionRepartos(conn, finca, datosVal);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado.toString();
    }
    
    /**
    * Obtiene el recurso de configuración por defecto del sistema
    */
    private void initializeProperties()
    {
        if(!MensajesErroresCargados)
        {
            Locale loc= I18N.getLocaleAsObject();
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.edicion.validacion.language.MensajesError",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("ValidacionMensajesError",bundle);
            MensajesErroresCargados=true;
        }
    }

    public static StringBuffer validacionTotal(final ArrayList fincas,final Expediente expediente){
    	StringBuffer sb = null;
        if(fincas!=null || fincas.size()>0){
            if(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                sb = validacionTotalTitularidad(fincas, expediente);
            else
                sb = validacionTotalFisicoEconomica(fincas, expediente);
        }
        else
        {
        	sb = new StringBuffer();
        	sb.append(I18N.get("ValidacionMensajesError","ErrorListaFincasVacias")+"\n\n\r");
//            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError",
//                    "ErrorListaFincasVacias"));
        }
        return sb;
    }

    public static StringBuffer validacionTotalFisicoEconomica(final ArrayList fincas,final Expediente expediente) {
        error=null;
        final ArrayList lstsbVal = new ArrayList();
        getDBConnection();  

        for(int i = 0; i<fincas.size();i++) {
            final FincaCatastro finca = (FincaCatastro)fincas.get(i);

            try {
                datosVal.calculaDatosValoracion(conn, finca.getDatosEconomicos().getCodigoCalculoValor(),
                        finca.getDatosEconomicos().getAnioAprobacion(),finca, String.valueOf(ConstantesCatastro.IdMunicipio));


                final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

                progressDialog.setTitle("Validando");
                progressDialog.addComponentListener(new ComponentAdapter()
                {
                    public void componentShown(ComponentEvent e)
                    {

                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
   
                                	if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
                                		//Expediente de situaciones finales
                                		StringBuffer sbVal = null;
                                		StringBuffer sbValTotal = null;
	                                	if (finca.TIPO_MOVIMIENTO_DELETE.equals("F")){
		                                    //String error = null;
		                                    progressDialog.report("Validando la finca");
		                                    ValidacionFinca valFinca = new ValidacionFinca(datosVal, expediente.getTipoExpediente().getCodigoTipoExpediente());
		                                    StringBuffer sbValFinca = valFinca.validacionParcialFinca(conn,finca, String.valueOf(ConstantesCatastro.IdMunicipio));
		                                    
		                                    progressDialog.report("Validando los Suelos");
		                                    ValidacionSuelo valSuelo = new ValidacionSuelo(datosVal);
		                                    StringBuffer sbValSuelo = valSuelo.validacionSuelo(conn,finca, String.valueOf(ConstantesCatastro.IdMunicipio));
		                                    
		                                    progressDialog.report("Validando las Unidades Constructivas");
		                                    ValidacionUC valUC = new ValidacionUC(String.valueOf(ConstantesCatastro.IdMunicipio),
		                                    CriteriosValidacion.esNecesariaPonencia(finca),finca.getLstSuelos(),
		                                                finca.getDatosEconomicos().getAnioAprobacion());
		                                    StringBuffer sbValUC = valUC.ValidaListaUC(conn, finca.getLstUnidadesConstructivas(), datosVal);
		                                
		                                    progressDialog.report("Validando las Construcciones");
		                                    ValidacionConstruccion valCons = new ValidacionConstruccion(String.valueOf(ConstantesCatastro.IdMunicipio),
		                                    CriteriosValidacion.esNecesariaPonencia(finca),finca.getDatosFisicos().getSupSobreRasante().floatValue(),
		                                                finca.getDatosFisicos().getSupTotal().floatValue(),finca.getDatosEconomicos().getAnioAprobacion());
		
		                                    StringBuffer sbValCons = valCons.validaListaCons(conn, finca.getLstConstrucciones(),finca.getLstUnidadesConstructivas(),
		                                                datosVal, finca.getLstBienesInmuebles());
		                                    
		                                    progressDialog.report("Validando los Cultivos");
		                                    ValidacionCultivos valCultivos = new ValidacionCultivos();
		                                    StringBuffer sbValCultivo = valCultivos.validacionCultivos(conn, finca);
		                                    
		                                    progressDialog.report("Validando los Bienes Inmuebles");
		                                    ValidacionBI valBI = new ValidacionBI(String.valueOf(ConstantesCatastro.IdMunicipio),CriteriosValidacion.esFincaBICE(finca),
		                                    CriteriosValidacion.existeUrbanaEnFinca(finca),CriteriosValidacion.existeRusticaEnFinca(finca), finca,
		                                    CriteriosValidacion.esNecesariaPonencia(finca),finca.getDatosEconomicos().getAnioAprobacion());
		                                    StringBuffer sbValBI = valBI.ValidaListaBI(conn,finca.getLstBienesInmuebles(), finca.getDirParcela(), datosVal);
		                                    
		                                    progressDialog.report("Validando los Derechos");
		                                    ValidacionDerecho valDer = new ValidacionDerecho();
		                                    StringBuffer sbValDerecho = valDer.validacionDerechos(conn, finca);
		                                     
		                                    progressDialog.report("Validando los Repartos");
		                                    ValidacionRepartos valRep = new ValidacionRepartos();
		                                    StringBuffer sbValReparto = valRep.validacionRepartos(conn, finca,datosVal);
		                                    
		                                    progressDialog.report("Validaciones Globales de la Finca");
		                                    StringBuffer sbValGlobalesFinca = valFinca.validacionesGlobalesFinca(conn, datosVal, finca, CriteriosValidacion.existeRusticaEnFinca(finca),
		                                    CriteriosValidacion.esNecesariaPonencia(finca),String.valueOf(ConstantesCatastro.IdMunicipio));
		                                    
		                                    progressDialog.report("Validaciones del Expediente");
		                                    ValidacionExpediente valExp = new ValidacionExpediente(expediente);
		                                    StringBuffer sbValExpediente = valExp.validacionesExpediente(conn, expediente);
		                                    
		                                    
		                                    
		                                    if(sbValFinca != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValFinca.toString());
		                                    }
		                                    if(sbValSuelo != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValSuelo.toString());
		                                    }
		                                    if(sbValUC != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValUC.toString());
		                                    }
		                                    if(sbValCons != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValCons.toString());
		                                    }
		                                    if(sbValCultivo != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValCultivo.toString());
		                                    }
		                                    if(sbValBI != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValBI.toString());
		                                    }
		                                    if(sbValDerecho != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValDerecho.toString());
		                                    }
		                                    if(sbValReparto != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValReparto.toString());
		                                    }
		                                    if(sbValGlobalesFinca != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValGlobalesFinca.toString());
		                                    }
		                                    if(sbValExpediente != null){
		                                    	if(sbVal == null){
		                                    		sbVal = new StringBuffer();
		                                    	}
		                                    	sbVal.append(sbValExpediente.toString());
		                                    }
		                                    
		                                    
		                                    if(sbVal == null){
		                                    	sbValTotal = new StringBuffer();
		                                    	sbValTotal.append( I18N.get("ValidacionMensajesError","finca") + " " +
                                    					finca.getRefFinca().getRefCatastral()+ "\n"+ I18N.get("ValidacionMensajesError", "Correcto"));
		                                    }
		                                    else{
		                                    	sbValTotal = new StringBuffer();
		                                    	sbValTotal.append(I18N.get("ValidacionMensajesError", "finca") + " "+
                                    					finca.getRefFinca().getRefCatastral()+ "\n" );
		                                    	sbValTotal.append("--------------------------------------------------------------\n\n\r");
		                                    	sbValTotal.append(sbVal.toString());
		                                    }
		                                    lstsbVal.add(sbValTotal);		
		                                }
                                	}
                                	
                                }	
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                    }
                });
                GUIUtil.centreOnWindow(progressDialog);
                progressDialog.setVisible(true);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try{conn.close();}catch(Exception e){}
            }
            
        }
        StringBuffer valTotal = new StringBuffer();
        for(int i=0; i<lstsbVal.size(); i++){
        	valTotal.append(((StringBuffer)lstsbVal.get(i)).toString());
        }
        return valTotal;
    }

    public static StringBuffer validacionTotalTitularidad(final ArrayList listaBI,final Expediente expediente) {
        error=null;

        final ArrayList lstsbVal = new ArrayList();
        getDBConnection();

        //for(int i = 0; i<listaBI.size();i++) {
        //    final BienInmuebleJuridico bien = (BienInmuebleJuridico) listaBI.get(i);

            try{

                final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

                progressDialog.setTitle("Validando");
                progressDialog.addComponentListener(new ComponentAdapter() {
                    public void componentShown(ComponentEvent e){

                        new Thread(new Runnable() {
                            public void run(){
                                try{
                                	StringBuffer sbVal = null;
                                    progressDialog.report("Validando los Bienes Inmuebles");

                                    if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
                                    	//expediente de situaciones finales
                                        progressDialog.report("Validando los Derechos");
                                        ValidacionDerecho valDer = new ValidacionDerecho();
                                        sbVal = valDer.validacionDerechos(conn, listaBI);
                                    }
                                    if(sbVal ==null){
                                    	sbVal = new StringBuffer();
                                    	sbVal.append( I18N.get("ValidacionMensajesError", "Correcto"));
                                    }
                                    
                                    lstsbVal.add(sbVal);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                    }
                });
                GUIUtil.centreOnWindow(progressDialog);
                progressDialog.setVisible(true);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                try{conn.close();}catch(Exception e){}
            }
            return (StringBuffer)lstsbVal.get(0);
        //} fin for
    }
}
