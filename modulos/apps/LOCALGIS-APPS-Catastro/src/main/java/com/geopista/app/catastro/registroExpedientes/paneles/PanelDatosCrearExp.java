/**
 * PanelDatosCrearExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 18-dic-2006
 * Time: 16:20:24
 * To change this template use File | Settings | File Templates.
 */
 
/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosCrearExp extends JPanel implements IMultilingue {
    private String etiqueta;
    private JFrame desktop;
    private JLabel numeroExpedienteLabel;
    private JTextField numeroExpedienteJTField;
    private JLabel tipoDeExpedienteLabel;
    private JComboBox tipoDeExpedienteCBoxE;
	private JLabel estadoLabel;
    private JComboBox estadoCBox;
    private JLabel fechaRegistroLabel;
    private JTextField fechaRegistroJTfield;
    private JButton fechaRegistroButton;
    private JLabel tipoTramitacionLabel;
    private JComboBox tipoTramitacionCBox;
	private JLabel entidadGeneradoraLabel;
    private JComboBox entidadGeneradoraJCBox;


	private ArrayList lstEntidadesGeneradoras;
	/**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosCrearExp(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        //creacion de objetos.
        tipoDeExpedienteLabel = new JLabel();
        //tipoDeExpedienteCBoxE = new JComboBox();
        getTipoDeExpedienteCBoxE();
        estadoLabel = new JLabel();
        estadoCBox = new JComboBox();
        fechaRegistroLabel = new JLabel();
        fechaRegistroJTfield = new JTextField();
        fechaRegistroButton = new JButton();
        numeroExpedienteLabel = new JLabel();
        numeroExpedienteJTField = new JTextField();
        tipoTramitacionLabel = new JLabel();
        getTipoTramitacionCBox();
        //tipoTramitacionCBox = new JComboBox();
        entidadGeneradoraLabel = new JLabel();
        entidadGeneradoraJCBox = new JComboBox();

        //Inicializacion de objetos.
        tipoDeExpedienteCBoxE.setEditable(false);
        estadoCBox.setEditable(false);
        entidadGeneradoraJCBox.setEditable(false);
        tipoTramitacionCBox.setEditable(false);
        fechaRegistroJTfield.setEnabled(false);
        fechaRegistroJTfield.setText(UtilRegistroExp.showToday());
        fechaRegistroButton.setIcon(UtilRegistroExp.iconoZoom);
        fechaRegistroButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fechaAltButtonActionPerformed(evt);
            }
        });

        numeroExpedienteJTField.setEditable(true);
        numeroExpedienteJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(numeroExpedienteJTField,13);
            }
        });

        cargaEstadosEstructuras();
       
        renombrarComponentes();

        //Inicializamos el panel con los elementos.
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(numeroExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 195, 20));
        this.add(numeroExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 15, 150, -1));
        this.add(tipoDeExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, 195, 20));
        this.add(tipoDeExpedienteCBoxE, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 37, 150, -1));
        this.add(estadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 59, 195, 20));
        this.add(estadoCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 59, 150, -1));
        this.add(fechaRegistroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 81, 195, 20));
        this.add(fechaRegistroJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 81, 150, -1));
        this.add(fechaRegistroButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 81, 20, 20));  
        this.add(tipoTramitacionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 103, 195, 20));
        this.add(tipoTramitacionCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 103, 195, 20));
        this.add(entidadGeneradoraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 195, 20));
        this.add(entidadGeneradoraJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 125, 195, 20));
    }

    /**
     * Metodo que trata el evento lanzado por el boton de la fecha de alteracion y muestra el dialogo para selecionar
     * una fecha valida, recogiendola de la constante estatica calendarValue.
     *
     * @param evt Evento lanzado
     * */
    private void fechaAltButtonActionPerformed(ActionEvent evt)
    {
        UtilRegistroExp.showCalendarDialog(desktop);

		if ((ConstantesCatastro.calendarValue != null) && (!ConstantesCatastro.calendarValue.trim().equals("")))
        {
			fechaRegistroJTfield.setText(ConstantesCatastro.calendarValue);
		}
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongCampoEdit(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(numAnno.length()>maxLong)
        {
                UtilRegistroExp.retrocedeCaracter(comp, maxLong);
        }
    }

    /**
     * Carga el primer estado de las estruturas de estados cargadas al inicio de la aplicacion. Tambien carga los
     * tipos de expediente que se cargan al cargar la aplicacion.
     */
    private void cargaEstadosEstructuras()
    {
        estadoCBox.removeAllItems();
        String aux="";
        if(Estructuras.getListaEstadosExpediente().getDomainNode("1")!=null)
        {
            aux= Estructuras.getListaEstadosExpediente().getDomainNode("1")
                .getTerm(ConstantesCatastro.Locale);
        }
        estadoCBox.addItem(aux);
        tipoDeExpedienteCBoxE.removeAllItems();
        ArrayList auxTiposExp = ConstantesCatastro.tiposExpedientes;
        if(auxTiposExp!=null)
        {
            for(int i = 0; i< auxTiposExp.size();i++)
            {
                tipoDeExpedienteCBoxE.addItem(((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente());
            }
        }
        
    }
    
    /**
    * carga los datos de las entidades generadoras existente en la Base de datos.
    */
   public void cargarDatosEntidadGeneradora(ArrayList lstEntidadGeneradora){
   	
	   lstEntidadesGeneradoras = lstEntidadGeneradora;
	   
   	if(lstEntidadGeneradora != null && !lstEntidadGeneradora.isEmpty()){
   		for(int i=0; i<lstEntidadGeneradora.size(); i++){
   			EntidadGeneradora entidadGeneradora = (EntidadGeneradora)lstEntidadGeneradora.get(i);
   			entidadGeneradoraJCBox.addItem(entidadGeneradora.getNombre());
   		}
   		
   	}
   	
   }
    

    /**
     * Carga los datos en el combo de tipo de tramitacion.
     *
     * @return this
     * */
    private void cargarDatosTipoTramitaion(){
    	
    	String expSituacionFinal= I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion.final");
    	String expVariaciones = I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion.variacion");
    	
    	//if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_DESACOPLADO)){
    		tipoTramitacionCBox.addItem(expSituacionFinal);
    		tipoTramitacionCBox.addItem(expVariaciones);
//    	}
//    	else{
//    		tipoTramitacionCBox.addItem(expSituacionFinal);
//    	}
    	
    }
    
    
   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosPanel()
    {
        return this;
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     * */
    public void setEditable(boolean edit)
    {
        fechaRegistroButton.setEnabled(edit);
        numeroExpedienteJTField.setEditable(edit);
        tipoTramitacionCBox.setEnabled(edit);
    }

    /**
     * Asigna al atributo desktop el parametro pasado al metodo.
     *
     * @param desktop JFrame
     */
    public void setDesktop(JFrame desktop)
    {
        this.desktop = desktop;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        cargaEstadosEstructuras();
        tipoDeExpedienteLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoDeExpedienteLabel")));
        estadoLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.estadoLabel")));
        fechaRegistroLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.fechaRegistroLabel")));
        fechaRegistroButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.fechaRegistroButton.hint"));
        numeroExpedienteLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.numeroExpedienteLabel")));
        tipoTramitacionLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
        	"Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion")));
        entidadGeneradoraLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
        	"Catastro.RegistroExpedientes.PanelDatosCrearExp.entidadgeneradora")));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesCatastro que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        if(checkeaParametrosNecesarios())
        {
            hashDatos.put(ConstantesCatastro.expedienteTipoExpediente,tipoDeExpedienteCBoxE.getSelectedItem());
            hashDatos.put(ConstantesCatastro.expedienteIdEstado,Estructuras.getListaEstadosExpediente().getDomainNodeByTraduccion((String)estadoCBox.getSelectedItem()).getPatron());
            hashDatos.put(ConstantesCatastro.expedienteFechaRegistro,UtilRegistroExp.getDate(fechaRegistroJTfield.getText()));
            hashDatos.put(ConstantesCatastro.expedienteNumeroExpediente, numeroExpedienteJTField.getText());
            hashDatos.put(ConstantesCatastro.expedienteTipoTramitacion, recogerEstadoTipoTramitacion());
            hashDatos.put(ConstantesCatastro.expedienteEntidadGeneradora, recogerEntidadGeneradora());
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosCrearExp.msg1"));
            return false;
        }
    }
    
    private EntidadGeneradora recogerEntidadGeneradora(){
    	
    	EntidadGeneradora entidadGeneradora = null;
    	String nombreEntidadGeneradora = (String)entidadGeneradoraJCBox.getSelectedItem();
    	for(int i=0; i<lstEntidadesGeneradoras.size(); i++ ){
    		EntidadGeneradora entidadGeneradoraAux = (EntidadGeneradora)lstEntidadesGeneradoras.get(i);
    		if(entidadGeneradoraAux.getNombre().equals(nombreEntidadGeneradora)){
    			entidadGeneradora = entidadGeneradoraAux;
    		}
    	}
    	return entidadGeneradora;
    }
    
    /**
     *  
     * @return 
     * true- si el tipo de tramitacion es de variaciones
     * false - si el tipo de tramitación es de situaciones finales
     */
    private Boolean recogerEstadoTipoTramitacion(){
    	
    	if(tipoTramitacionCBox.getSelectedIndex() == 0){
    		// exp situacion final
    		return new Boolean(true);
    	}
    	else{
    		//exp variaciones
    		return new Boolean(false);
    	}
    }

    /**
     * Comprueba que los campos necesarios, marcados con asteriscos rojos, no son null o vacios.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaParametrosNecesarios()
    {
        return (!tipoDeExpedienteCBoxE.getSelectedItem().equals(""))&&(!estadoCBox.getSelectedItem().equals(""))
                &&(!fechaRegistroJTfield.getText().equals(""))&&(!numeroExpedienteJTField.getText().equals(""));
    }


    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesCatastro que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        tipoDeExpedienteCBoxE.removeAllItems();
        estadoCBox.removeAllItems();
        tipoDeExpedienteCBoxE.addItem((String)hashDatos.get(ConstantesCatastro.expedienteTipoExpediente));
        estadoCBox.addItem((String)hashDatos.get(ConstantesCatastro.expedienteIdEstado));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormat = formatter.format((Date)hashDatos.get(ConstantesCatastro.expedienteFechaRegistro));
        fechaRegistroJTfield.setText(fechaFormat);
        numeroExpedienteJTField.setText((String)hashDatos.get(ConstantesCatastro.expedienteNumeroExpediente));
       
        if(((Boolean)hashDatos.get(ConstantesCatastro.expedienteTipoTramitacion)).booleanValue()){
        	// el expediente se de Situaciones finales
        	tipoTramitacionCBox.setSelectedIndex(0);
        }
        else{
        	// el expediente se de tipo variaciones
        	tipoTramitacionCBox.setSelectedIndex(1);
        }
        
        EntidadGeneradora entidadGeneradora = (EntidadGeneradora)hashDatos.get(ConstantesCatastro.expedienteEntidadGeneradora);
        entidadGeneradoraJCBox.addItem(entidadGeneradora.getNombre());

        lstEntidadesGeneradoras = new ArrayList();
        lstEntidadesGeneradoras.add(entidadGeneradora);
 
     
    }
    
    public JComboBox getTipoTramitacionCBox() {
    	if(tipoTramitacionCBox == null){
    		tipoTramitacionCBox = new JComboBox();
    		 cargarDatosTipoTramitaion();
    		tipoTramitacionCBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent act)
                {
                	String tipoExp = (String)tipoDeExpedienteCBoxE.getSelectedItem();
                	ArrayList auxTiposExp = ConstantesCatastro.tiposExpedientes;
                    if(auxTiposExp!=null)
                    {
                    	 for(int i = 0; i< auxTiposExp.size();i++)
                         {
                         	if( ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO) && ((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente().equals(tipoExp)){
                         		if((((TipoExpediente)auxTiposExp.get(i)).getConvenio().equals(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)) &&
                         		!recogerEstadoTipoTramitacion()){
                         			tipoTramitacionCBox.hidePopup();
                         			
                         			 JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
                         					 I18N.get("RegistroExpedientes",
                                      "Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion.msg1"));
                         			tipoTramitacionCBox.setSelectedIndex(0);
                         		}
                         	}
                         }
                    }

                }
            });
    		
    	}
		return tipoTramitacionCBox;
	}

	public void setTipoTramitacionCBox(JComboBox tipoTramitacionCBox) {
		this.tipoTramitacionCBox = tipoTramitacionCBox;
	}
	
	public JComboBox getTipoDeExpedienteCBoxE() {
		if(tipoDeExpedienteCBoxE == null){
			tipoDeExpedienteCBoxE = new JComboBox();
			tipoDeExpedienteCBoxE.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent act)
                {
                	String tipoExp = (String)tipoDeExpedienteCBoxE.getSelectedItem();
                	ArrayList auxTiposExp = ConstantesCatastro.tiposExpedientes;
                    if(auxTiposExp!=null)
                    {
                        for(int i = 0; i< auxTiposExp.size();i++)
                        {
                        	if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
                        		
                        		if(((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente().equals(tipoExp)){
                        	
	                        		if((((TipoExpediente)auxTiposExp.get(i)).getConvenio().equals(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)) &&
	                        		!recogerEstadoTipoTramitacion()){
	                        			
	                        			
	                        			 JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
	                        					 I18N.get("RegistroExpedientes",
	                                     "Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion.msg1"));
	                        			 tipoDeExpedienteCBoxE.setSelectedIndex(0);
	                        		}
                        		}
                        	}
                        	else{

                        		if(tipoExp != null && tipoExp.equals(ConstantesCatastro.CONVENIO_CONSULTA) &&
                        				((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente().equals(tipoExp)){
                        			tipoDeExpedienteCBoxE.hidePopup();
                        			
                        			 JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
                        					 I18N.get("RegistroExpedientes",
                                     "Catastro.RegistroExpedientes.PanelDatosCrearExp.tipoTramitacion.msg2"));
                        			 tipoDeExpedienteCBoxE.setSelectedIndex(0);
                        		}
                        	}
                        }
                    }

                }
            });
		}
		return tipoDeExpedienteCBoxE;
	}

	public void setTipoDeExpedienteCBoxE(JComboBox tipoDeExpedienteCBoxE) {
		this.tipoDeExpedienteCBoxE = tipoDeExpedienteCBoxE;
	}


    public JComboBox getEntidadGeneradoraJCBox() {
		return entidadGeneradoraJCBox;
	}

	public void setEntidadGeneradoraJCBox(JComboBox entidadGeneradoraJCBox) {
		this.entidadGeneradoraJCBox = entidadGeneradoraJCBox;
	}
    
}
