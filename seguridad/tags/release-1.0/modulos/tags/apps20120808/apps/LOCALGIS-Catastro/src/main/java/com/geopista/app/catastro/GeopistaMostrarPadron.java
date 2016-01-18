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

package com.geopista.app.catastro;

/**
 * @author cotesa
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaMostrarPadron extends JPanel implements WizardPanel
{

    private boolean grabarEstadisticas=false; //Indica si hay que grabar estadisticas o no
    private Boolean initEstadisticas=new Boolean(true);

    private Hashtable hashVias=null;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private int numeroRegistrosLeidos = 0;
    
    private String cadenaTexto = "";
    
    private Blackboard blackboardInformes = aplicacion.getBlackboard(); 
    
    private JScrollPane scpErrores = new JScrollPane();
    
    private JLabel lblImagen = new JLabel();
    
    private JLabel lblTipoFichero = new JLabel();
    
    private JComboBox cmbTipoInfo = new JComboBox();
    
    public Connection con = null;
    
    private JScrollPane jScrollPane2 = new JScrollPane();

    
    private WizardContext wizardContext = null;  
    
    /**
     * Inicializador de la clase
     * @param grabarEstadisticas -- Indica si se van a grabar o no
     * estadisticas
     */
    public GeopistaMostrarPadron(boolean grabarEstadisticas)
    {
        try
        {
            this.grabarEstadisticas=grabarEstadisticas;
            if (grabarEstadisticas) hashVias=new Hashtable();
            jbInit();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {
                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {
                            setLayout(null);
                            setSize(new Dimension(750, 600));
                            
                            scpErrores
                            .setBounds(new Rectangle(135, 55, 595, 295));
                            
                            lblImagen.setIcon(IconLoader
                                    .icon("inf_referencia.png"));
                            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                            lblImagen.setBorder(BorderFactory.createLineBorder(
                                    Color.black, 1));
                            
                            
                            jScrollPane2.setBounds(new Rectangle(135, 20, 595, 490));
                            
                            setSize(750, 600);
                            add(jScrollPane2, null);
                            add(cmbTipoInfo, null);
                            add(lblTipoFichero, null);
                            add(lblImagen, null);
                            add(scpErrores, null);
                            
                        } catch (Exception e)
                        {
                            
                        } finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
    }// jbinit
    
    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        //idMunicipio = Integer.parseInt(aplicacion.getString("geopista.DefaultCityId"));
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarPadron.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarPadron.ImportandoDatos"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {
                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        
                        try
                        {
                            if (con == null)
                            {
                                con = getDBConnection();
                            }
                            numeroRegistrosLeidos = 0;
                            cadenaTexto = "";

                            cmbTipoInfo.removeAllItems();

                            String selectedModule = (String) blackboardInformes
                            .get("tipoImportarPadron");

                            cmbTipoInfo.addItem(selectedModule);

                            scpErrores.setVisible(false);


                            setName(aplicacion
                                    .getI18nString("almacenar.padron.asistente"));

                            // Iniciamos la ayuda
                            //loadHelp("InformacionReferenciaDistritosCensalesGuardarBaseDatos");
                            // por actualizar


                            String rutaFichero = blackboardInformes.get("rutaFicheroImportar").toString();

                            numeroRegistrosLeidos = actualizarDatosPadronHabitantes(rutaFichero, progressDialog);

                            if(grabarEstadisticas && hashVias!=null)
                           {
                            	System.out.println("Grabando estadisticas");
                                 progressDialog.setTitle(aplicacion.getI18nString("importar.resultado.grabarestadisticas"));
                                 blackboardInformes.put("estadisticas",hashVias);
                                 blackboardInformes.put("progressDialog",progressDialog);
                                 blackboardInformes.put("procesados", new Integer(numeroRegistrosLeidos));
                                 blackboardInformes.put("initEstadisticas",initEstadisticas);
                                 Class.forName("com.geopista.app.inforeferencia.SaveStatisticals").newInstance();
                             }

                            // Ponemos los valores de Registros Leídos
                                             // y cadena de texto

                                             // ponemos en el panel los valores de
                                             // registros leidos y fecha y Hora

                            String fechaFinalizacion = "";

                            JEditorPane jedResumen = new JEditorPane("text/html", cadenaTexto);

                            jedResumen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                            jedResumen.setEditable(false);
                            jScrollPane2.getViewport().add(jedResumen, null);
                            DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
                            String date = (String) formatter.format(new Date());
                            fechaFinalizacion = date;
                            cadenaTexto = cadenaTexto
                            + aplicacion.getI18nString("importar.progreso.numero.leidos")
                            + numeroRegistrosLeidos;
                            cadenaTexto = cadenaTexto
                            + aplicacion.getI18nString("importar.progreso.numero.no.leidos")
                            + blackboardInformes.get("RegistrosErroneos");
                            cadenaTexto = cadenaTexto
                            + aplicacion.getI18nString("importar.progreso.fecha.fin")
                            + fechaFinalizacion;
                            if(grabarEstadisticas)
                            {
                                try
                                {
                                    cadenaTexto=cadenaTexto + aplicacion.getI18nString("importar.resultado.grabarestadisticas.grabadas")+
                                            " "+blackboardInformes.get("estgrabadas").toString()+" de "+numeroRegistrosLeidos;
                                    cadenaTexto=cadenaTexto + aplicacion.getI18nString("importar.resultado.grabarestadisticas.nograbadas")+
                                            " "+blackboardInformes.get("estnograbadas").toString()+" de "+numeroRegistrosLeidos;
                                }catch(Exception e)
                                {}
                            }
                            String mensaje = blackboardInformes.get("Mensaje").toString();
                            if (!mensaje.equals("")){
                                cadenaTexto = cadenaTexto
                                + aplicacion.getI18nString("importar.resultado.mensaje.error")
                                + mensaje;
                            }
                            mensaje = blackboardInformes.get("MensajeEstadistica").toString();
                            if (!mensaje.equals(""))
                               cadenaTexto=cadenaTexto+mensaje;
                            jedResumen.setText(cadenaTexto);

                        } catch (Exception e)
                        {   
                            e.printStackTrace();
                            
                        } finally
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
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return "2";
    }
    
    public String getInstructions()
    {
        return " ";
    }
    
    public boolean isInputValid()
    {
        return true;
    }
    

    private String nextID = null;
    
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    
    public String getNextID()
    {
        return nextID;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        this.wizardContext = wd;
    }
    
    /**
     * Establece la conexion con la base de datos
     * 
     * @return Connection, conexion
     */
    public static Connection getDBConnection() throws SQLException
    {
        
        ApplicationContext app = AppContext.getApplicationContext();
        Connection conn = app.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }
    
     /**
     * Método que devuelve -1 si ha habido algun error en la inserción
     *
     * @param rutaFichero
     * @param progressDialog
     * @return
     * @throws SQLException
     */
    public int actualizarDatosPadronHabitantes(String rutaFichero, TaskMonitorDialog progressDialog) throws SQLException
    {      
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        PadronHabitantesPostgre consultaBD = new PadronHabitantesPostgre();
        
        int registrosLeidos = 0;
        int registrosErroneos = 0;
        StringBuffer mensaje = new StringBuffer();
        StringBuffer mensajeEstadistica = new StringBuffer();

        Identificadores.put("Mensaje", mensaje);
        Identificadores.put("MensajeEstadistica",mensajeEstadistica);

        try{
            
            BufferedReader reader = new BufferedReader(new FileReader(rutaFichero));  
            String str ="";
            int n = 0;
            while ((str = reader.readLine()) != null)
            {      
                n++;
                
                progressDialog.report(registrosLeidos + registrosErroneos, Integer.valueOf(Identificadores.get("numFilas").toString()).intValue(),
                        aplicacion.getI18nString("ImportandoDatos"));
                
                GeopistaDatosImportarPadron datosClase = new GeopistaDatosImportarPadron();  
                datosClase.setCodProvincia(str.substring(0, 2));
                datosClase.setCodMunicipio(str.substring(2, 5));
                datosClase.setNombreHabitante(str.substring(5, 25));
                datosClase.setParticulaPrimerApellido(str.substring(25, 31));
                datosClase.setPrimerApellido(str.substring(31, 56));
                datosClase.setParticulaSegundoApellido(str.substring(56, 62));
                datosClase.setSegundoApellido(str.substring(62, 87));
                datosClase.setCodProvinciaNacimiento(str.substring(87, 89));
                datosClase.setCodMunicipioNacimiento(str.substring(89, 92));
                datosClase.setAnioNacimiento(str.substring(92, 96));
                datosClase.setMesNacimiento(str.substring(96, 98));
                datosClase.setDiaNacimiento(str.substring(98, 100));
                datosClase.setTipoIdentificador(str.substring(100, 101));
                datosClase.setLetraDocumentoExtranjeros(str.substring(101, 102));
                datosClase.setIdentificador(str.substring(102, 110));
                datosClase.setCodigoControl(str.substring(110, 111));
                datosClase.setNumeroDocumento(str.substring(111, 131));
                datosClase.setIdentifAyuntamiento(str.substring(131, 146));
                datosClase.setIdentifElectoral(str.substring(146, 157));
                datosClase.setTipoInformacion(str.substring(157, 158));
                datosClase.setCDev(str.substring(158, 160));
                datosClase.setAnioVar(str.substring(160, 164));
                datosClase.setMesVar(str.substring(164, 166));
                datosClase.setDiaVar(str.substring(166, 168));
                datosClase.setCVar(str.substring(168, 169));
                datosClase.setCausaVar(str.substring(169, 171));                 
                datosClase.setCodDistrito(str.substring(171, 173));
                datosClase.setCodSeccion(str.substring(173, 176));
                datosClase.setLetraSeccion(str.substring(176, 177));
                datosClase.setCodEntidadColectiva(str.substring(177, 179));
                datosClase.setCodEntidadSingular(str.substring(179, 181));
                datosClase.setDigControlEntidadSingular(str.substring(181, 182));
                datosClase.setCodNucleo(str.substring(182, 184));
                datosClase.setNombreCortoEntidadColectiva(str.substring(184, 209));
                datosClase.setNombreCortoEntidadSingular(str.substring(209, 234));
                datosClase.setNombreCortoNucleo(str.substring(234, 259));
                
                //Se utiliza aux para evitar que luego falle por buscar un numérico 
                String aux = str.substring(259, 264);
                if (aux.trim().equals(""))
                    aux = "0";                
                datosClase.setCodVia(aux);
                
                datosClase.setTipoVia(str.substring(264, 269));
                datosClase.setNombreCortoVia(str.substring(269, 294));
                datosClase.setCodPseudoVia(str.substring(294, 299));
                datosClase.setNombrePseudovia(str.substring(299, 349));
                datosClase.setTipoNumero(str.substring(349,350));
                
                aux = str.substring(350, 354);
                if (aux.trim().equals(""))
                    aux = "0";                 
                datosClase.setNumero(aux);
                
                datosClase.setCalificador(str.substring(354, 355));
                
                aux = str.substring(355, 359);
                if (aux.trim().equals(""))
                    aux = "0";     
                datosClase.setNumeroSuperior(aux);
                
                datosClase.setCalificadorSuperior(str.substring(359, 360));
                
                aux = str.substring(360, 363);
                if (aux.trim().equals(""))
                    aux = "0";     
                datosClase.setKilometro(aux);
                
                aux = str.substring(363, 364);
                if (aux.trim().equals(""))
                    aux = "0";     
                datosClase.setHectometro(aux);
                
                datosClase.setBloque(str.substring(364, 366));
                datosClase.setPortal(str.substring(366, 368));
                datosClase.setIdEscalera(str.substring(368, 370));
                datosClase.setPlanta(str.substring(370, 373));
                datosClase.setPuerta(str.substring(373, 377));
                datosClase.setTipoLocal(str.substring(377, 378));
                datosClase.setIdentifAyuntamientoDV(str.substring(378, 393));
                datosClase.setNumeroHojaPadronal(str.substring(393, 403));
                datosClase.setIdentifElectoralDV(str.substring(403, 414));
                datosClase.setNombreDV(str.substring(414, 434));
                datosClase.setParticula1DV(str.substring(434, 440));
                datosClase.setApellido1DV(str.substring(440, 465));
                datosClase.setParticula2DV(str.substring(465, 471));
                datosClase.setApellido2DV(str.substring(471, 496));
                datosClase.setSexo(str.substring(496, 497));
                datosClase.setProvinciaNacimientoDV(str.substring(497, 499));
                datosClase.setMunicipioNacimientoDV(str.substring(499, 502));
                datosClase.setAnioNacimientoDV(str.substring(502, 506));
                datosClase.setMesNacimientoDV(str.substring(506, 508));
                datosClase.setDiaNacimientoDV(str.substring(508, 510));
                datosClase.setTipoIdentificadorDV(str.substring(510, 511));
                datosClase.setLetraDocumentoExtranjerosDV(str.substring(511, 512));
                datosClase.setIdentificadorDV(str.substring(512, 520));
                datosClase.setCodigoControlDV(str.substring(520, 521));
                datosClase.setNumeroDocumentoDV(str.substring(521, 541));
                datosClase.setCodigoNivelEstudios(str.substring(541, 543));
                datosClase.setCodigoNacionalidad(str.substring(543, 546));
                datosClase.setCodProvinciaProcedenciaDestino(str.substring(546, 548));
                datosClase.setCodMunicipioProcedenciaDestino(str.substring(548, 551));
                datosClase.setCodConsulado(str.substring(551, 554));
                
                //System.out.println("Datos Clase:"+datosClase.getCodMunicipio());
                
                //Se comprueba la causa de variación
                char codigoVariacion = datosClase.getCVar().charAt(0);
                
                //System.out.println("Datos Habitante:"+datosClase);
                //System.out.println("Datos Habitante Nivel:"+datosClase.getCodigoNivelEstudios());
                //System.out.println("Datos Habitante Variacion:"+codigoVariacion);
                //System.out.println("Datos Habitante Calle:"+datosClase.getNombreCortoVia());
                //System.out.println("Datos Habitante Numero:"+datosClase.getNumero());
                switch (codigoVariacion)
                {
                    //Sin código de variación.
                    case ' ':{
                        //TODO descomentar esta lineas
                        datosClase = compruebaCamposObligatorios(datosClase, n, codigoVariacion);
                        mensaje.append(Identificadores.get("MensajeAuxiliar").toString());
                    
                        //si están todos los campos obligatorios, se continua
                        if (datosClase!=null)
                        {
                            //Comprobar si existe el domicilio. Si el resultado es 0  -> no existe
                            //                                  Si el resultado es -1 -> Error
                            int idDomicilio = consultaBD.findDomicilio (datosClase);
                            //Si no existe el domicilio, se crea
                            if (idDomicilio == 0)
                                idDomicilio = consultaBD.insertDomicilio (datosClase);
                            if (idDomicilio >0)
                            {
                                datosClase.setIdDomicilio(idDomicilio);
                                //Buscar el identificador del habitante a partir de los datos de identificación del fichero
                                int idHabitante= consultaBD.findIdHabitanteByDatosPadron(datosClase);
                            
                                //Si el habitante existe, actualiza los datos
                                if (idHabitante>0)
                                {
                                    if (consultaBD.updateHabitante(idHabitante, datosClase))
                                    {
                                        registrosLeidos++;                                       
                                        addEstadistica(datosClase);
                                    }
                                    else
                                    {
                                        registrosErroneos++;
                                        mensaje.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n)
                                            .append(": ").append(app.getI18nString("importar.resultado.mensaje.error.modificacion"))
                                            .append(app.getI18nString("importar.resultado.mensaje.no.actualizado"));
                                    }
                                }//si no existe el habitante, se crea
                                else
                                {
                                    idHabitante = consultaBD.insertHabitante (datosClase);
                                    if (idHabitante >0)
                                    {
                                        registrosLeidos++;
                                        addEstadistica(datosClase);
                                    }
                                    else
                                        registrosErroneos++;
                                }
                            } else registrosErroneos++;
                        }else registrosErroneos++;
                        break;
                    
                    }
                    //Alta: Tendrán contenido los datos de identificación de provincia y municipio
                    //      y todos los datos de variación.
                    case 'A':{
                        //TODO descomentar esta lineas
                        datosClase = compruebaCamposObligatorios(datosClase, n, codigoVariacion);
                        mensaje.append(Identificadores.get("MensajeAuxiliar").toString());
                        //si están todos los campos obligatorios, se continua
                        if (datosClase!=null)
                        {
                            //Comprobar si existe el domicilio. Si el resultado es 0  -> no existe
                            //									Si el resultado es -1 -> Error
                            int idDomicilio = consultaBD.findDomicilio (datosClase);
                            //Si no existe el domicilio, se crea
                            if (idDomicilio == 0)
                                 idDomicilio = consultaBD.insertDomicilio (datosClase);
                            //Insertar los datos en la tabla habitantes
                            if(idDomicilio > 0){
                                datosClase.setIdDomicilio(idDomicilio);
                                int idHabitante = consultaBD.insertHabitante (datosClase);
                                if (idHabitante >0)
                                {
                                    registrosLeidos++;
                                    addEstadistica(datosClase);
                                } else registrosErroneos++;
                            }  else registrosErroneos++;
                        } else registrosErroneos++;

                    
                    break;
                }   
                
                //Baja: Tendrán contenido exclusivamente los datos de identificación.
                //      Si la baja es por duplicado, los campos de datos de variación vienen
                //      cumplimentados con los datos del habitante con el que está duplicado.
                //      Los datos de variacion, por lo tanto, se ignoran.
                case 'B':{
                     boolean isDeleted = consultaBD.deleteHabitante(datosClase);
                     if (isDeleted)
                        registrosLeidos++;
                     else{
                        mensaje.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                            .append(": ").append(app.getI18nString("importar.resultado.mensaje.error.baja"))
                            .append(app.getI18nString("importar.resultado.mensaje.no.borrado"));
                        registrosErroneos++;
                     }
                     break;
                } 
                
                //Modificación: Todos los campos estarán cumplimentados.
                case 'M': {
                     //TODO descomentar esta lineas
                    datosClase = compruebaCamposObligatorios(datosClase, n, codigoVariacion);
                    mensaje.append(Identificadores.get("MensajeAuxiliar"));
                    
                    //si están todos los campos obligatorios, se continua
                    if (datosClase!=null){
                        //Comprobar si existe el domicilio. Si el resultado es 0  -> no existe
                        //									Si el resultado es -1 -> Error
                        int idDomicilio = consultaBD.findDomicilio (datosClase);
                        //Si no existe el domicilio, se crea
                        if (idDomicilio == 0)
                            idDomicilio = consultaBD.insertDomicilio (datosClase);
                        
                        //Actualiza los datos en la tabla habitantes
                        if(idDomicilio > 0){
                            datosClase.setIdDomicilio(idDomicilio);
                            //Buscar el identificador del habitante a partir de los datos de identificación del fichero
                            int idHabitante= consultaBD.findIdHabitanteByDatosPadron(datosClase);
                            //Si el habitante existe, actualiza los datos
                            System.out.println("Identificador de habitante:"+idHabitante);
                            if (idHabitante>0){                                
                                if (consultaBD.updateHabitante(idHabitante, datosClase))
                                {
                                    registrosLeidos++;
                                    addEstadistica(datosClase);
                                }
                                else
                                {
                                    registrosErroneos++;
                                    mensaje.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                                        .append(": ").append(app.getI18nString("importar.resultado.mensaje.error.modificacion"))
                                        .append(app.getI18nString("importar.resultado.mensaje.no.actualizado"));
                                }                                    
//                            }  else registrosErroneos++;
                            }else
                                {
                                    idHabitante = consultaBD.insertHabitante (datosClase);
                                    if (idHabitante >0)
                                    {
                                        registrosLeidos++;
                                        addEstadistica(datosClase);
                                    }
                                    else
                                        registrosErroneos++;
                                }
                        }  else {
                        	System.out.println("El habitante no existe en el municipio."+datosClase.getApellido1DV()+" "+datosClase.getApellido2DV());
                        	registrosErroneos++;
                        }
                    }else registrosErroneos++;
                    break;
                }
                
                default:{
                    registrosErroneos++;
                    mensaje.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                        .append(": ").append(app.getI18nString("importar.resultado.mensaje.error.nocorresponde"));
                    
                }
              
                }
                if(registrosLeidos + registrosErroneos>0 && ((registrosLeidos + registrosErroneos) % 100==0) &&
                      grabarEstadisticas && hashVias!=null && hashVias.size()>0)
                {
                         progressDialog.setTitle(aplicacion.getI18nString("importar.resultado.grabarestadisticas"));
                         blackboardInformes.put("estadisticas",hashVias);
                         blackboardInformes.put("progressDialog",progressDialog);
                          blackboardInformes.put("procesados", new Integer(registrosLeidos));
                          blackboardInformes.put("initEstadisticas",initEstadisticas);
                         Class.forName("com.geopista.app.inforeferencia.SaveStatisticals").newInstance();
                         hashVias=new Hashtable();
                         blackboardInformes.put("estadisticas",hashVias);
                         initEstadisticas=new Boolean(false);
                         progressDialog.setTitle(aplicacion.getI18nString("GeopistaMostrarPadron.ImportandoDatos"));
                         progressDialog.report(aplicacion.getI18nString("GeopistaMostrarPadron.ImportandoDatos"));


                }
            }
            
            Identificadores.put("RegistrosErroneos", registrosErroneos);
            Identificadores.put("RegistrosLeidos", registrosLeidos);
            Identificadores.put("Mensaje", mensaje.toString());
            return registrosLeidos;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return registrosLeidos;
        }
        
    }   
    
    
    private GeopistaDatosImportarPadron compruebaCamposObligatorios (GeopistaDatosImportarPadron registro, int n, char tipo)
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        PadronHabitantesPostgre consultaBD = new PadronHabitantesPostgre();
        
        StringBuffer mensajeAux = new StringBuffer();
        
        StringBuffer mensTipo = new StringBuffer();
        
        if (tipo=='A') 
            mensTipo.append(app.getI18nString("importar.resultado.mensaje.error.alta")).append(" ");
        else if (tipo=='M') 
            mensTipo.append(app.getI18nString("importar.resultado.mensaje.error.modificacion")).append(" ");
        else if (tipo ==' ') 
            mensTipo.append(app.getI18nString("importar.resultado.mensaje.error.sincodigo")).append(" ");
        
        
        boolean obligatorios = false; 
        
        int idMunicipioCompleto = Integer.parseInt(registro.getCodProvincia().concat(registro.getCodMunicipio()));
        
        
        //Buscar los identificadores de: distrito, seccion, entidad colectiva, nucleo y via
        int idDistrito = consultaBD.findIdDistritoByIne(idMunicipioCompleto, registro.getCodDistrito());
        if(idDistrito>0){
            
            registro.setIdDistrito(idDistrito);
            
            int idSeccion  = consultaBD.findIdSeccionByIne (idDistrito, registro.getCodSeccion());
            
            if (idSeccion>0){
                
                registro.setIdSeccion(idSeccion); 
                
                int idEntidadColectiva = consultaBD.findIdEntidadColectivaByIne (idMunicipioCompleto, registro.getCodEntidadColectiva());
                
                if (idEntidadColectiva>0){
                    
                    registro.setIdEntidadColectiva(idEntidadColectiva);
                    
                    int idEntidadSingular = consultaBD.findIdEntidadSingularByIne (idMunicipioCompleto, registro.getCodEntidadColectiva(), registro.getCodEntidadSingular());
                    
                    if (idEntidadSingular>0){
                        
                        registro.setIdEntidadSingular(idEntidadSingular);
                        
                        int idNucleo = consultaBD.findIdNucleoByIne (idEntidadSingular, registro.getCodNucleo());
                        
                        if (idNucleo>0){
                            
                            registro.setIdNucleo(idNucleo);
                            
                            int idVia = consultaBD.findIdViaByIne (idMunicipioCompleto, registro.getCodVia());
                            
                            if (idVia > 0){
                                
                                obligatorios=true;
                                registro.setIdVia(idVia);
                                
                                //No lo uso para nada
                                //int idPseudovia = consultaBD.findIdPseudoViaByIne(idMunicipioCompleto, registro.getCodPseudoVia());
                                
                                
                            }
                            else{
                                mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                                .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.via")+ "(Id via:"+registro.getCodVia()+")");
                                
                            }
                            
                        }
                        else{
                            mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                            .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.nucleo")+ "(Id Entidad singular:"+idEntidadSingular+ " Cod nucleo: "+ registro.getCodNucleo()+")");
                            
                        }
                    }
                    else{
                        mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                        .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.entidadsingular")+ "(Entidad colectiva: "+ registro.getCodEntidadColectiva()+" Entidad singular: "+ registro.getCodEntidadSingular()+")");
                        
                    }
                }
                else{
                    mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                    .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.entidadcolectiva"));
                    
                }
            }
            else{
                mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
                .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.seccion")+ "(idDistrito: "+idDistrito+", idSeccion: " +registro.getCodSeccion()+")");
                
            }
        }
        else{
            mensajeAux.append(app.getI18nString("importar.resultado.mensaje.linea")).append(n) 
            .append(": ").append(mensTipo).append(app.getI18nString("importar.resultado.mensaje.distrito"));
            
        }        
        
        Identificadores.put("MensajeAuxiliar", mensajeAux.toString());
        
        if (obligatorios)
            return registro;
        else
            return null;
    }

    public void addEstadistica(GeopistaDatosImportarPadron datosClase)
    {
        if (!grabarEstadisticas || datosClase==null) return;
        if (hashVias==null) hashVias=new Hashtable();
        Hashtable hashCalles = (Hashtable) hashVias.get(datosClase.getTipoVia()!=null?datosClase.getTipoVia().toUpperCase().trim():"");
        if (hashCalles==null)
        {
            hashCalles=new Hashtable();
            hashVias.put(datosClase.getTipoVia()!=null?datosClase.getTipoVia().toUpperCase().trim():"",hashCalles);
        }
        Hashtable hashRotulos = (Hashtable) hashCalles.get(datosClase.getNombreCortoVia()!=null?datosClase.getNombreCortoVia().toUpperCase().trim():"");
        if (hashRotulos==null)
        {
            hashRotulos=new Hashtable();
            hashCalles.put(datosClase.getNombreCortoVia()!=null?datosClase.getNombreCortoVia().toUpperCase().trim():"", hashRotulos);
        }
        Vector vectorDatos=(Vector)hashRotulos.get(datosClase.getNumero().toUpperCase().trim());
        if (vectorDatos==null)
        {
            vectorDatos=new Vector();
            hashRotulos.put(datosClase.getNumero()!=null?datosClase.getNumero().toUpperCase().trim():"",vectorDatos);
        }
        vectorDatos.add(datosClase);
    }




    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {

    }
    
} // de la clase general.


