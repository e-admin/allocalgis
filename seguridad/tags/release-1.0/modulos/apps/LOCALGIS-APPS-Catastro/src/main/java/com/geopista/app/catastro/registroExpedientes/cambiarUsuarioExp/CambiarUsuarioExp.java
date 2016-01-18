package com.geopista.app.catastro.registroExpedientes.cambiarUsuarioExp;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosBusqueda;
import com.geopista.app.catastro.registroExpedientes.paneles.TablaBusqueda;
import com.geopista.app.catastro.registroExpedientes.paneles.DialogoBuscarUsuarios;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Date;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 04-abr-2007
 * Time: 10:06:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que muestra todos los expedientes no cerrados del usuario.
 * Si el usuario tiene permiso de administrador la busqueda muestra todos los expedientes no cerrados.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */

public class CambiarUsuarioExp extends JInternalFrame implements IMultilingue
{
    private JFrame desktop;
    private ArrayList expedientes;
    private ArrayList expedienteFiltrados;
    private JPanel buscarTodoPanel;
    private JButton buscarJButton;
    private JButton asociarUsuarioJButton;
    private JButton salirJButton;
    private PanelDatosBusqueda datosBusquedaPanel;
    private TablaBusqueda tablaBusqueda;
    private Hashtable usuarios;
    private ArrayList codigoEntGeneradora;
    private ArrayList codigoEstados;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame y la lista de expedientes a mostrar.
     *
     * @param desktop  JFrame
     * @param exp    lista de expedientes
     */
    public CambiarUsuarioExp(final JFrame desktop, ArrayList exp)
    {
        this.desktop= desktop;
        this.expedientes= exp;
        this.expedienteFiltrados= exp;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this.desktop);
        inicializaElementos();
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame();
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        buscarTodoPanel= new JPanel();
        buscarTodoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        buscarJButton= new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.buscarJButton"));

        buscarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buscarJButtonActionPerformed();
            }
        });

        asociarUsuarioJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.asociarUsuarioJButton"));

        asociarUsuarioJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                asociarUsuarioJButtonActionPerformed();
            }
        });

        salirJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.salirJButton"));
        salirJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cierraInternalFrame();
            }
        });
        recopilaDatosBD();
        inicializaPanelBuscar();
        inicializaTablaBuscar();

        buscarTodoPanel.add(buscarJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 335, 120, 30));
        buscarTodoPanel.add(asociarUsuarioJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 580, 120, 30));
        buscarTodoPanel.add(salirJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 580, 120, 30));
        buscarTodoPanel.setPreferredSize(new Dimension(650,660));
        getContentPane().add(buscarTodoPanel);
        setSize(new Dimension(650,660));
        this.setMaximizable(false);
        int w=(this.desktop.getWidth()/2)- (this.getWidth()/2);
        int d= (this.desktop.getHeight()/2) - (this.getHeight()/2)-30;
        this.setLocation(w,d);
        this.setMaximumSize(new Dimension(650,660));
    }

   /**
    * Inicializa el panel de datos donde se puede introducir datos para filtrar el resultado.
    */
    private void inicializaPanelBuscar()
    {
        datosBusquedaPanel = new PanelDatosBusqueda("Catastro.RegistroExpedientes.CambiarUsuarioExp.datosBusquedaPanel",
                usuarios, codigoEntGeneradora, codigoEstados);
        datosBusquedaPanel.setDesktop(desktop);
        buscarTodoPanel.add(datosBusquedaPanel.getPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 610, 310));
    }

   /**
    * Inicializa la tabla donde se muestran los expedientes.
    */
    private void inicializaTablaBuscar()
    {
        tablaBusqueda= new TablaBusqueda("Catastro.RegistroExpedientes.CambiarUsuarioExp.tablaBusqueda", this.expedientes, this.usuarios);
        buscarTodoPanel.add(tablaBusqueda.getPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 370, 610, 200));
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        try
        {
            this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        UtilRegistroExp.menuBarSetEnabled(true, this.desktop);
    }

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.titulo"));
        buscarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.buscarJButton"));
        asociarUsuarioJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.asociarUsuarioJButton"));
        salirJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.salirJButton"));
        datosBusquedaPanel.renombrarComponentes();
        tablaBusqueda.renombrarComponentes();
        tablaBusqueda.cargaDatosTabla(this.expedientes,this.usuarios);
    }

    /**
     * Metodo que recopila datos de la base de datos para que luego se puedan mostrar en el panel y el usuario pueda
     * seleccionar entre ellos para hacer el filtrado.
     */
    private void recopilaDatosBD()
    {
        try
        {
            usuarios = ConstantesRegistroExp.clienteCatastro.getUsuariosConExpediente();
            codigoEntGeneradora = ConstantesRegistroExp.clienteCatastro.getCodigoEntidadGeneradora();
            codigoEstados = ConstantesRegistroExp.clienteCatastro.getCodigoEstados();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que filtra los expedientes que se estran mostrando con los parametros de busqueda que el usuario ha
     * introducido.
     */
    private void buscarJButtonActionPerformed()
    {
        Hashtable hashDatos = new Hashtable();
        if(datosBusquedaPanel.recopilaDatosPanel(hashDatos))
        {
            String numeroExpediente = (String)hashDatos.get(ConstantesRegistroExp.expedienteNumeroExpediente);
            String tipoExpediente = (String)hashDatos.get(ConstantesRegistroExp.expedienteTipoExpediente);
            String idEstado = (String)hashDatos.get(ConstantesRegistroExp.expedienteIdEstado);
            String idTecnicoCatastro = (String)hashDatos.get(ConstantesRegistroExp.expedienteIdTecnicoCatastro);
            Date fechaRegistroInicial = UtilRegistroExp.getDate((String)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro + "Inicial"));
            Date fechaRegistroFinal = UtilRegistroExp.getDate((String)hashDatos.get(ConstantesRegistroExp.expedienteFechaRegistro + "Final"));
            String annoExpedienteAdminOrigenAlt_Ejercicio = (String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteAdminOrigenAlteracion);
            String codigoEntidadGeneradora_Control = (String)hashDatos.get(ConstantesRegistroExp.entidadGeneradoraCodigo);
            String referenciaCatastral = (String)hashDatos.get(ConstantesRegistroExp.expedienteReferenciasCatastrales);
            String nifPresentador = (String)hashDatos.get(ConstantesRegistroExp.expedienteNifPresentador);
            String nombreCompletoPresentador = (String)hashDatos.get(ConstantesRegistroExp.expedienteNombreCompletoPresentador);
            ArrayList expAux = new ArrayList();
            expAux.addAll(expedientes);
            String ultimoEstado = (String)codigoEstados.get(codigoEstados.size()-1);
            if(idEstado!=null&&idEstado.equalsIgnoreCase(ultimoEstado))
            {
                expAux.removeAll(expedientes);
                try
                {
                    expAux= (ArrayList)ConstantesRegistroExp.clienteCatastro.getExpedientesUsuario("true", ConstantesRegistroExp.tipoConvenio);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            boolean siguiente;
            for(int i= 0; i<expAux.size();i++)
            {
                siguiente = true;
                Expediente exp= (Expediente)expAux.get(i);
                if(siguiente&&((numeroExpediente!=null)&&(!numeroExpediente.equals("")))&&(exp.getNumeroExpediente()!=null&&
                        !(exp.getNumeroExpediente().indexOf(numeroExpediente)>=0))|| exp.getNumeroExpediente()==null)
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(tipoExpediente!=null)&&(!tipoExpediente.equals("")))&&(exp.getTipoExpediente()!=null&&
                        !exp.getTipoExpediente().getCodigoTipoExpediente().equalsIgnoreCase(tipoExpediente))||exp.getTipoExpediente()==null)
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(idEstado!=null)&&(!idEstado.equals("")))&&(exp.getIdEstado()>0&&
                        exp.getIdEstado()!=Long.parseLong(idEstado))|| exp.getIdEstado()<=0)
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(idTecnicoCatastro!=null)&&(!idTecnicoCatastro.equals("")))&&(usuarios.get(String.valueOf(exp.getIdTecnicoCatastro()))!=null
                        &&!((String)usuarios.get(String.valueOf(exp.getIdTecnicoCatastro()))).equalsIgnoreCase(idTecnicoCatastro)
                        ||usuarios.get(String.valueOf(exp.getIdTecnicoCatastro()))==null))
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&& checkeRangoFechar(fechaRegistroInicial,fechaRegistroFinal,exp)))
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(annoExpedienteAdminOrigenAlt_Ejercicio!=null)&&(!annoExpedienteAdminOrigenAlt_Ejercicio.equals("")))
                        &&(exp.getAnnoExpedienteAdminOrigenAlteracion()>0&&exp.getAnnoExpedienteAdminOrigenAlteracion()!=Integer.parseInt(annoExpedienteAdminOrigenAlt_Ejercicio))
                        || exp.getAnnoExpedienteAdminOrigenAlteracion()<=0)
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(codigoEntidadGeneradora_Control!=null)&&(!codigoEntidadGeneradora_Control.equals("")))
                        &&(exp.getEntidadGeneradora().getCodigo()>0&&exp.getEntidadGeneradora().getCodigo()!=Integer.parseInt(codigoEntidadGeneradora_Control))
                        ||exp.getEntidadGeneradora().getCodigo()<=0)
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(referenciaCatastral!=null)&&(!referenciaCatastral.equals("")))
                        &&((exp.getListaReferencias().size()>0)&&
                        !checkeaContainsRefCatas(exp.getListaReferencias(),referenciaCatastral)
                        ||(exp.getListaReferencias().size()==0)))
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(nifPresentador!=null)&&(!nifPresentador.equals("")))&&(exp.getNifPresentador()!=null&&
                        !(exp.getNifPresentador().indexOf(nifPresentador)>=0)
                        ||exp.getNifPresentador()==null))
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
                if((siguiente&&(nombreCompletoPresentador!=null)&&(!nombreCompletoPresentador.equals("")))&&(exp.getNombreCompletoPresentador()!=null&&
                        exp.getNombreCompletoPresentador().indexOf(nombreCompletoPresentador)<0
                        ||exp.getNombreCompletoPresentador()==null))
                {
                    expAux.remove(i);
                    siguiente= false;
                    i--;
                }
            }
            expedienteFiltrados= expAux;
            tablaBusqueda.cargaDatosTabla(expAux, usuarios);
        }
    }

    /**
     * Metodos que checkea si las referencias del expediente que se esta filtrando contienen el patro que el usuario
     * ha introducido.
     *
     * @param refCatas ArrayList referencias del expediente
     * @param patron String patron del usuario
     * @return boolean
     */
    private boolean checkeaContainsRefCatas(ArrayList refCatas, String patron)
    {
        boolean encontrado= false;
        int i= 0;
        while(!encontrado && i< refCatas.size())
        {
            if(refCatas.get(i) instanceof FincaCatastro)
            {
                encontrado = (((FincaCatastro)refCatas.get(i)).getRefFinca().getRefCatastral().indexOf(patron)>=0);
            }
            else if(refCatas.get(i) instanceof BienInmuebleCatastro)
            {
                encontrado = (((BienInmuebleCatastro)refCatas.get(i)).getIdBienInmueble().getIdBienInmueble().indexOf(patron)>=0);
            }
            i++;
        }
        return encontrado;
    }

    /**
     * Metodo que checkea si la fecha de registro del expediente se encuentra en el rango que ha introducido el usuario.
     *
     * @param fechaRegistroInicial Date
     * @param fechaRegistroFinal Date
     * @param exp Expediente
     * @return boolean
     */
    private boolean checkeRangoFechar(Date fechaRegistroInicial, Date fechaRegistroFinal, Expediente exp)
    {
        boolean valida= true;
        if(((fechaRegistroInicial!=null)&&(fechaRegistroFinal!=null))&&(exp.getFechaRegistro()!=null&&
                        (fechaRegistroInicial.getTime()<=exp.getFechaRegistro().getTime()&&
                        fechaRegistroFinal.getTime()>=exp.getFechaRegistro().getTime())))
        {
            valida = false;
        }
        else if(((fechaRegistroInicial!=null)&&(fechaRegistroFinal==null))&&(exp.getFechaRegistro()!=null&&
                        fechaRegistroInicial.getTime()<=exp.getFechaRegistro().getTime()))
        {
            valida = false;
        }
        else if(((fechaRegistroInicial==null)&&(fechaRegistroFinal!=null))&&(exp.getFechaRegistro()!=null&&
                        fechaRegistroFinal.getTime()>=exp.getFechaRegistro().getTime()))
        {
            valida= false;
        }
        else if((fechaRegistroInicial==null)&&(fechaRegistroFinal==null))
        {
            valida = false;
        }
        return valida;
    }

    /**
     * Metodo que lanza un dialogo para que el usuario seleccione el nuevo tecnico asociado al
     * expediente. Para ello tiene que seleccionar un expediente en la tabla.
     */
    private void asociarUsuarioJButtonActionPerformed()
    {
        Expediente exp;
        /*int numExp= tablaBusqueda.getExpedienteSeleccionado();
        if(numExp>=0)
        {
            exp = (Expediente)expedienteFiltrados.get(numExp);
            DialogoBuscarUsuarios dialogo = new DialogoBuscarUsuarios(desktop,true, usuarios,exp);
            dialogo.setLocation(this.getX()+(this.getWidth()/2-dialogo.getWidth()/2),this.getY()+(this.getHeight()/2-dialogo.getHeight()/2));
            dialogo.setResizable(false);
            dialogo.show();
            tablaBusqueda.cargaDatosTabla(expedientes,usuarios);
            datosBusquedaPanel.cargaEstructurasBD();
        } */
        String numExp= tablaBusqueda.getExpedienteSeleccionado();
        if(numExp != null){
            for(Iterator it = expedienteFiltrados.iterator(); it.hasNext();){
                exp = (Expediente) it.next();
                if(exp.getNumeroExpediente().equals(numExp)){
                    DialogoBuscarUsuarios dialogo = new DialogoBuscarUsuarios(desktop,true, usuarios,exp);
                    dialogo.setLocation(this.getX()+(this.getWidth()/2-dialogo.getWidth()/2),this.getY()+(this.getHeight()/2-dialogo.getHeight()/2));
                    dialogo.setResizable(false);
                    dialogo.show();
                    tablaBusqueda.cargaDatosTabla(expedientes,usuarios);
                    datosBusquedaPanel.cargaEstructurasBD();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(desktop,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.CambiarUsuarioExp.msg1"));
        }
    }  

}

