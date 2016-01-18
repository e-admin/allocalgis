/**
 * ExportacionMasiva.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.exportacionMasiva;



import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Clase que extiende de JInternalFrame y crea una pantalla que permite al usuario exportar los ficheros modificados
 * que no hayan sido enviados previamente o cuyo estado sea finalizado y no se hayan enviado en ese estado aun.
 * La clase se encarga de cargar los paneles necesario para funcionar. Esta accion solo esta activa en el modo
 * desacoplado.
 * */

public class ExportacionMasiva extends JInternalFrame implements IMultilingue
{
    private JFrame desktop;
    private JPanel panelTodo;
    private JLabel tipoConvenioJLabel;
    private JTextField tipoConvenioJTField;
    private JLabel directorioJLabel;
    private JTextField directorioJTField;
    private JLabel periodoJLabel;
    private JTextField periodoInicialJTField;
	private JTextField periodoFinalJTField;
    private JButton aceptarJButton;
    private JButton cancelarJButton;
	private JFileChooser seleccionDirectorioJFchosser;
    private JButton seleccionDirectorioJButton;
    private Date fechaIniPeriodo;
    private String directorio;
    private JLabel jlFinEntrada;
    private JTextField jtfFinEntrada;
    private JLabel jlVARPAD;
    private JTextField jtfVARPAD;
    
    private ArrayList lstExp;
    private long fechaInicio;
	private long fechaFin;

    /**
     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
     * Se le pasa por parametro un JFrame
     *
     * @param desktop  JFrame
     */
    public ExportacionMasiva(final JFrame desktop, ArrayList listaExpedientes)
    {
        this.desktop= desktop;
        this.lstExp = listaExpedientes;
        
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
                        "Catastro.RegistroExpedientes.ExportacionMasiva.titulo"));
        setClosable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        inicializaPanel();
        recopilaDatosBD();
        cargaDatos();

        getContentPane().add(panelTodo);
        setSize(new Dimension(500,300));
        this.setMaximizable(false);
        int w=(this.desktop.getWidth()/2)- (this.getWidth()/2);
        int d= (this.desktop.getHeight()/2) - (this.getHeight()/2)-30;
        this.setLocation(w,d);
        this.setMaximumSize(new Dimension(500,300));
    }

   /**
    * Inicializa el panel de datos donde el usuario puede introducir el directorio donde desea guardar el fichero
    * generado.
    */
    private void inicializaPanel()
    {
        panelTodo = new JPanel();
        tipoConvenioJLabel = new JLabel(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.tipoConvenioJLabel"));
        tipoConvenioJTField = new JTextField();
        tipoConvenioJTField.setEditable(false);
        directorioJLabel = new JLabel(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.directorioJLabel"));
        directorioJTField = new JTextField();
        directorioJTField.setEditable(false);
        
        this.jlFinEntrada=new JLabel(I18N.get("RegistroExpedientes",
                "registroExp.nombreFicheroFinEntrada"));
        this.jtfFinEntrada=new JTextField();
        jlFinEntrada.setLabelFor(this.jtfFinEntrada);
        
        this.jlVARPAD=new JLabel(I18N.get("RegistroExpedientes",
        "registroExp.nombreFicheroVARPAD"));
        this.jtfVARPAD=new JTextField();
        jlVARPAD.setLabelFor(this.jtfVARPAD);
        
        
        periodoJLabel = new JLabel(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.periodoJLabel"));
        periodoInicialJTField = new JTextField();
        periodoInicialJTField.setEditable(true);
        periodoFinalJTField = new JTextField();
        periodoFinalJTField.setEditable(true);
        aceptarJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.aceptarJButton.hint"));
        cancelarJButton = new JButton(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.cancelarJButton.hint"));
        cancelarJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cierraInternalFrame();
            }
        });
        seleccionDirectorioJFchosser = new JFileChooser();
        seleccionDirectorioJButton= new JButton();
        seleccionDirectorioJButton.setIcon(UtilRegistroExp.iconoAbrirDirectorio);
        seleccionDirectorioJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.seleccionDirectorioJButton.hint"));
        seleccionDirectorioJButton.addActionListener(new java.awt.event.ActionListener()
        {
				public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    seleccionDirectorioJButtonActionPerformed();
				}
	    });


        panelTodo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelTodo.add(tipoConvenioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 150, 20));
        panelTodo.add(tipoConvenioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 270, 20));

        
        panelTodo.add(jlFinEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 20));
        panelTodo.add(jtfFinEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 270, 20));
        
        panelTodo.add(jlVARPAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 150, 20));
        panelTodo.add(jtfVARPAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 270, 20));
        
        
        panelTodo.add(directorioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 150, 20));
        panelTodo.add(directorioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 270, 20));
        panelTodo.add(seleccionDirectorioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, 30, 20));

        panelTodo.add(periodoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 150, 20));
        panelTodo.add(periodoInicialJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 90, 20));
        panelTodo.add(periodoFinalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 90, 20));
        
      

        panelTodo.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, 100, 30));
        panelTodo.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, 100, 30));
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
     * Devuelve el boton aceptar.
     *
     * @return JButton aceptarGuardar
     */
    public JButton getAceptarJButton()
    {
        return aceptarJButton;
    }

    /**
     * Devuelve un string indicando el directorio selecionado por el usuario.
     *
     * @return String directorio
     */
    public String getDirectorio()
    {
        return  directorio;
    }
    
    
    /**
     * Devuelve un string indicando el nombre del fichero Fin entrada selecionado por el usuario.
     *
     * @return String nombre del fichero
     */
    public String getNombreFinEntrada(){
    	String nombre=this.jtfFinEntrada.getText();
    	if(nombre!=null)
    		if(!nombre.equals(""))
    			return nombre;
    	
        return  "FinEntrada";//por defecto el nombre del fichero será: FinEntrada
    }//fin del método
    
    /**
     * Devuelve un string indicando el nombre del fichero VARPAD selecionado por el usuario.
     *
     * @return String nombre del fichero
     */
    public String getNombreVARPAD(){
    	String nombre=this.jtfVARPAD.getText();
    	if(nombre!=null)
    		if(!nombre.equals(""))
    			return nombre;
    	
        return  "VARPAD";//por defecto el nombre del fichero será: VARPAD
    }//fin del método
    
    
    
    

    /**
     * Renombra los elementos de la gui con el resourceBundle que se este utilizando.
     */
    public void renombrarComponentes()
    {
        this.setTitle(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.titulo"));
        tipoConvenioJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.tipoConvenioJLabel"));
        directorioJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.directorioJLabel"));
        periodoJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.periodoJLabel"));
        aceptarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.aceptarJButton"));
        aceptarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.aceptarJButton.hint"));
        cancelarJButton.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.cancelarJButton"));
        cancelarJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.ExportacionMasiva.cancelarJButton.hint"));
    }

    /**
     * Metodo que llama al cliente catastro para hacer una peticion a base de datos y obtener la fecha de inicio de
     * periodo que es la ultima vez que se envio un Fin entrada.
     */
    private void recopilaDatosBD()
    {
        try
        {
            fechaIniPeriodo = ConstantesRegExp.clienteCatastro.getFechaInicioPeriodoExp();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void buscarPeriodoListaExpedientes(ArrayList listaExpedientes){
    	
    	if(listaExpedientes != null && !listaExpedientes.isEmpty()){
	    	setFechaInicio(((Expediente)listaExpedientes.get(0)).getFechaRegistro().getTime());
	    	setFechaFin(((Expediente)listaExpedientes.get(0)).getFechaRegistro().getTime());
	    	for (int i=1; i<listaExpedientes.size(); i++){
	    		Expediente exp = (Expediente)listaExpedientes.get(i);
	    		
	    		if (exp.getFechaRegistro().getTime() < getFechaInicio()){
	    			setFechaInicio(exp.getFechaRegistro().getTime());
	    		}
	    		
	    		if(exp.getFechaRegistro().getTime() > getFechaFin()){
	    			setFechaFin(exp.getFechaRegistro().getTime());
	    		}
	    		
	    	}
    	}
    	
    	
    }
    /**
     * Carga los datos en los componentes del panel.
     */
    private void cargaDatos()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       /* String fechaIniP = formatter.format(fechaIniPeriodo);
        String fechaFinP = formatter.format(new Date(System.currentTimeMillis()));
        periodoInicialJTField.setText(fechaIniP);
        periodoFinalJTField.setText(fechaFinP);
        */
        buscarPeriodoListaExpedientes(getLstExp());
        String fechaIniP = formatter.format(new Date(getFechaInicio()));
        String fechaFinP = formatter.format(new Date(getFechaFin()));
        periodoInicialJTField.setText(fechaIniP);
        periodoFinalJTField.setText(fechaFinP);
        	
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO))
        {
            tipoConvenioJTField.setText(I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.ExportacionMasiva.convenioFisicoEconomico"));
        }
        else if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD))
        {
            tipoConvenioJTField.setText(I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.ExportacionMasiva.convenioTitularidad"));
        }
    }

    /**
     * Recoge el directorio seleccionado por el usuario y lo muestra en el panel.
     */
    private void seleccionDirectorioJButtonActionPerformed(){
    	/*seleccionDirectorioJFchosser.setDialogTitle(I18N.get("RegistroExpedientes",
        "Catastro.RegistroExpedientes.ExportacionMasiva.tituloDirectorioDialogo"));
    	 int option = seleccionDirectorioJFchosser.showSaveDialog(this);
    	 //botón aceptar
    	 if (option == JFileChooser.APPROVE_OPTION) {
    		 System.out.println("Se ha elegido la opción de aceptar");
    		 System.out.println("Nombre del fichero: "+seleccionDirectorioJFchosser.getSelectedFile().getName());
    		 System.out.println("Directorio: "+seleccionDirectorioJFchosser.getCurrentDirectory().toString());
    		 directorioJTField.setText(seleccionDirectorioJFchosser.getCurrentDirectory().toString()+"\\"+
    				 seleccionDirectorioJFchosser.getSelectedFile().getName());
    		 directorio=seleccionDirectorioJFchosser.getCurrentDirectory().toString();
    		 nombre=seleccionDirectorioJFchosser.getSelectedFile().getName();
    	      }//fin if*/
    	
    	 seleccionDirectorioJFchosser.setCurrentDirectory(new java.io.File("."));
         seleccionDirectorioJFchosser.setDialogTitle(I18N.get("RegistroExpedientes",
                             "Catastro.RegistroExpedientes.ExportacionMasiva.tituloDirectorioDialogo"));
         seleccionDirectorioJFchosser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
         seleccionDirectorioJFchosser.setAcceptAllFileFilterUsed(false);
         if (seleccionDirectorioJFchosser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
         {
           directorioJTField.setText(seleccionDirectorioJFchosser.getSelectedFile().toString() + "\\");  
           directorio=seleccionDirectorioJFchosser.getSelectedFile().toString();
           System.out.println("Directorio: "+directorio);
         }
    }//fin método
    
    public ArrayList getLstExp() {
		return lstExp;
	}

	public void setLstExp(ArrayList lstExp) {
		this.lstExp = lstExp;
	}

	public long getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(long fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public long getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(long fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	 public JTextField getPeriodoInicialJTField() {
			return periodoInicialJTField;
		}

		public void setPeriodoInicialJTField(JTextField periodoInicialJTField) {
			this.periodoInicialJTField = periodoInicialJTField;
		}

		public JTextField getPeriodoFinalJTField() {
			return periodoFinalJTField;
		}

		public void setPeriodoFinalJTField(JTextField periodoFinalJTField) {
			this.periodoFinalJTField = periodoFinalJTField;
		}

    
}//fin clase
