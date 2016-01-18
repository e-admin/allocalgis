package com.geopista.app.catastro.registroExpedientes.busqueda;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.paneles.PanelDatosBusqueda;
import com.geopista.app.catastro.registroExpedientes.paneles.TablaBusqueda;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.vividsolutions.jump.I18N;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 22-ene-2007
 * Time: 12:12:40
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que muestra todos los expedientes no cerrados del usuario.
 * Si el usuario tiene permiso de administrador la busqueda muestra todos los expedientes no cerrados.
 * La clase se encarga de cargar los paneles y los dialogos necesario para funcionar.
 * */

public class Busqueda extends JInternalFrame implements IMultilingue
{
    private JFrame desktop;
    private ArrayList expedientes;
    private ArrayList expedienteFiltrados;
    private JPanel buscarTodoPanel;
    private JButton consultaEstadoExpedienteButton;
	private JButton buscarJButton;
    private JButton aceptarJButton;
    private JButton nuevoJButton;
    private JButton cancelarJButton;
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
    public Busqueda(final JFrame desktop, ArrayList exp)
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
                        "Catastro.RegistroExpedientes.Busqueda.titulo"));
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
        
        consultaEstadoExpedienteButton = new JButton();  
        consultaEstadoExpedienteButton.setEnabled(false);
        consultaEstadoExpedienteButton.setToolTipText(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton.hint"));
        consultaEstadoExpedienteButton.setText(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.GestionDeExpediente.consultaEstadoExpedienteButton"));
       

        buscarJButton= new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.buscarJButton"));

        buscarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buscarJButtonActionPerformed();
            }
        });

        aceptarJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.aceptarJButton"));

        //Se linkea en el main para que tenga el contro del evento y pueda crear otro internal frame.
        nuevoJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.nuevoJButton"));

        cancelarJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.cancelarJButton"));
        cancelarJButton.addActionListener(new ActionListener()
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
        buscarTodoPanel.add(consultaEstadoExpedienteButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 580, 120, 30));
        buscarTodoPanel.add(aceptarJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 580, 80, 30));
        buscarTodoPanel.add(nuevoJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 580, 80, 30));
        buscarTodoPanel.add(cancelarJButton,new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 580, 80, 30));
        
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
        datosBusquedaPanel = new PanelDatosBusqueda("Catastro.RegistroExpedientes.Busqueda.datosBusquedaPanel",
                usuarios, codigoEntGeneradora, codigoEstados);
        datosBusquedaPanel.setDesktop(desktop);
        buscarTodoPanel.add(datosBusquedaPanel.getPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 610, 310));
    }

   /**
    * Inicializa la tabla donde se muestran los expedientes.
    */
    private void inicializaTablaBuscar()
    {
        tablaBusqueda= new TablaBusqueda("Catastro.RegistroExpedientes.Busqueda.tablaBusqueda", this.expedientes, this.usuarios);
        buscarTodoPanel.add(tablaBusqueda.getPanel(), new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 370, 610, 200));
        
        tablaBusqueda.getExpedientesTable().addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {

            	Expediente exp = getExpedienteSeleccionado();
            	if(exp.getIdEstado() == ConstantesRegExp.ESTADO_ENVIADO){
            		consultaEstadoExpedienteButton.setEnabled(true);
            	}
            	else{
            		consultaEstadoExpedienteButton.setEnabled(false);
            	}
            	
            }

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}
        });

    }

   /**
    * Devuelve el boton aceptar.
    *
    * @return JButton aceptarJButton
    */
    public JButton getAceptarJButton()
    {
        return aceptarJButton;
    }
    
    public JButton getConsultaEstadoExpedienteButton() {
		return consultaEstadoExpedienteButton;
	}



   /**
    * Devuelve el boton nuevo.
    *
    * @return JButton nuevoJButton
    */
    public JButton getNuevoJButton()
    {
        return nuevoJButton;
    }

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    private void cierraInternalFrame()
    {
        try
        {
            this.setClosed(true);
            if (expedientes!=null){
                expedientes.clear();
                expedientes=null;
            }
            if (expedienteFiltrados!=null){
                expedienteFiltrados.clear();
                expedienteFiltrados=null;
            }
            if (buscarTodoPanel!=null){
                getContentPane().remove(buscarTodoPanel);
                buscarTodoPanel.removeAll();
                buscarTodoPanel=null;
            }
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
                        "Catastro.RegistroExpedientes.Busqueda.titulo"));
        buscarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.buscarJButton"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.aceptarJButton"));
        nuevoJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.nuevoJButton"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.Busqueda.cancelarJButton"));
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
                    expAux= (ArrayList)ConstantesRegistroExp.clienteCatastro.getExpedientesUsuario("true", ConstantesRegistroExp.tipoConvenio, ConstantesRegistroExp.modoTrabajo);
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
     * Metodo que devuelve el expediente seleccionado por el usuario.
     *
     * @return Expediente
     */
    public Expediente getExpedienteSeleccionado()
    {

    	try{
        String idExp = tablaBusqueda.getExpedienteSeleccionado();
        for(Iterator it = expedienteFiltrados.iterator(); it.hasNext();){
            Expediente exp = (Expediente) it.next();
            if(exp.getNumeroExpediente().equals(idExp))
                return exp;
        }
        return null;
    	}catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return null;
		}
    }
}
