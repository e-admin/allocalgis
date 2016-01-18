package com.geopista.app.catastro.intercambio.edicion.validacion;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import javax.swing.*;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.ArrayList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        String resultado = null;
        getDBConnection();
        ValidacionFinca validarFinca = new ValidacionFinca(datosVal, expediente.getTipoExpediente().getCodigoTipoExpediente());

        try
        {
        	resultado = validarFinca.validacionParcialFinca(conn, finca, String.valueOf(ConstantesRegistroExp.IdMunicipio));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado;
    }
    
    public static String validacionParcialSuelo(FincaCatastro finca)
    {
        String resultado = null;
        getDBConnection();
        ValidacionSuelo validarSuelo= new ValidacionSuelo(datosVal);

        try
        {
        	resultado = validarSuelo.validacionSuelo(conn, finca, String.valueOf(ConstantesRegistroExp.IdMunicipio));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{conn.close();}catch(Exception e){}
        }
        
        return resultado;
    }

    public static String validacionParcialCultivos(FincaCatastro finca)
    {
        String resultado = null;
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
        
        return resultado;
    }
    
    public static String validacionParcialDerechos(FincaCatastro finca)
    {
        String resultado = null;
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
        
        return resultado;
    }
    
    public static String validacionParcialRepartos(FincaCatastro finca)
    {
        String resultado = null;
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
        
        return resultado;
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

    public static void validacionTotal(final ArrayList fincas,final Expediente expediente){

        if(fincas!=null || fincas.size()>0){
            if(expediente.getTipoExpediente().getConvenio().equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
                validacionTotalTitularidad(fincas, expediente);
            else
                validacionTotalFisicoEconomica(fincas, expediente);
        }
        else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError",
                    "ErrorListaFincasVacias"));
        }

    }

    public static void validacionTotalFisicoEconomica(final ArrayList fincas,final Expediente expediente) {
        error=null;

        getDBConnection();  

        for(int i = 0; i<fincas.size();i++) {
            final FincaCatastro finca = (FincaCatastro)fincas.get(i);

            try {
                datosVal.calculaDatosValoracion(conn, finca.getDatosEconomicos().getCodigoCalculoValor(),
                        finca.getDatosEconomicos().getAnioAprobacion(),finca, String.valueOf(ConstantesRegistroExp.IdMunicipio));


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
                                	if (finca.TIPO_MOVIMIENTO_DELETE.equals("F")){
	                                    //String error = null;
	                                    progressDialog.report("Validando la finca");
	                                    ValidacionFinca valFinca = new ValidacionFinca(datosVal, expediente.getTipoExpediente().getCodigoTipoExpediente());
	                                    error = valFinca.validacionParcialFinca(conn,finca, String.valueOf(ConstantesRegistroExp.IdMunicipio));
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando los Suelos");
	                                        ValidacionSuelo valSuelo = new ValidacionSuelo(datosVal);
	                                        error = valSuelo.validacionSuelo(conn,finca, String.valueOf(ConstantesRegistroExp.IdMunicipio));
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando las Unidades Constructivas");
	                                        ValidacionUC valUC = new ValidacionUC(String.valueOf(ConstantesRegistroExp.IdMunicipio),
	                                                CriteriosValidacion.esNecesariaPonencia(finca),finca.getLstSuelos(),
	                                                finca.getDatosEconomicos().getAnioAprobacion());
	                                        error = valUC.ValidaListaUC(conn, finca.getLstUnidadesConstructivas(), datosVal);
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando las Construcciones");
	                                        ValidacionConstruccion valCons = new ValidacionConstruccion(String.valueOf(ConstantesRegistroExp.IdMunicipio),
	                                                CriteriosValidacion.esNecesariaPonencia(finca),finca.getDatosFisicos().getSupSobreRasante().floatValue(),
	                                                finca.getDatosFisicos().getSupTotal().floatValue(),finca.getDatosEconomicos().getAnioAprobacion());
	
	                                        error = valCons.validaListaCons(conn, finca.getLstConstrucciones(),finca.getLstUnidadesConstructivas(),
	                                                datosVal, finca.getLstBienesInmuebles());
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando los Cultivos");
	                                        ValidacionCultivos valCultivos = new ValidacionCultivos();
	                                        error = valCultivos.validacionCultivos(conn, finca);
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando los Bienes Inmuebles");
	                                        ValidacionBI valBI = new ValidacionBI(String.valueOf(ConstantesRegistroExp.IdMunicipio),CriteriosValidacion.esFincaBICE(finca),
	                                                CriteriosValidacion.existeUrbanaEnFinca(finca),CriteriosValidacion.existeRusticaEnFinca(finca), finca,
	                                                CriteriosValidacion.esNecesariaPonencia(finca),finca.getDatosEconomicos().getAnioAprobacion());
	                                        error = valBI.ValidaListaBI(conn,finca.getLstBienesInmuebles(), finca.getDirParcela(), datosVal);
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando los Derechos");
	                                        ValidacionDerecho valDer = new ValidacionDerecho();
	                                        error = valDer.validacionDerechos(conn, finca);
	                                    } 
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validando los Repartos");
	                                        ValidacionRepartos valRep = new ValidacionRepartos();
	                                        error = valRep.validacionRepartos(conn, finca,datosVal);
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validaciones Globales de la Finca");
	                                        error = valFinca.validacionesGlobalesFinca(conn, datosVal, finca, CriteriosValidacion.existeRusticaEnFinca(finca),
	                                                CriteriosValidacion.esNecesariaPonencia(finca),String.valueOf(ConstantesRegistroExp.IdMunicipio));
	                                    }
	                                    if(error==null)
	                                    {
	                                        progressDialog.report("Validaciones del Expediente");
	                                        ValidacionExpediente valExp = new ValidacionExpediente(expediente);
	                                        error = valExp.validacionesExpediente(conn, expediente);
	                                    }
	                                    if(error !=null)
	                                    {
	                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError",error));
	                                    }
	                                    else
	                                    {
	                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError",
	                                                "Correcto"));
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
    }

    public static void validacionTotalTitularidad(final ArrayList listaBI,final Expediente expediente) {
        error=null;

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

                                    progressDialog.report("Validando los Bienes Inmuebles");

                                   /* if( (bien.getLstTitulares() == null || bien.getLstTitulares().isEmpty()) &&
                                            (bien.getLstComBienes() == null || bien.getLstComBienes().isEmpty()))
                                        error = "Error.JAV1";
                                     */
                                    //if(error==null) {
                                        progressDialog.report("Validando los Derechos");
                                        ValidacionDerecho valDer = new ValidacionDerecho();
                                        error = valDer.validacionDerechos(conn, listaBI);
                                   // }


                                    if(error !=null)
                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError",error));
                                    else
                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ValidacionMensajesError", "Correcto"));

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
        //} fin for
    }
}
