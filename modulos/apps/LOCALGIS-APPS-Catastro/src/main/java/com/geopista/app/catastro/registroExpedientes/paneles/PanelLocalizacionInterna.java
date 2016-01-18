/**
 * PanelLocalizacionInterna.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.model.beans.Escalera;
import com.geopista.app.catastro.model.beans.Planta;
import com.geopista.app.catastro.model.beans.Puerta;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 10-ene-2007
 * Time: 12:35:52
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelLocalizacionInterna extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel bloqueLabel;
    private JTextField bloqueJTField;
	private JLabel escaleraLabel;
    private JLabel plantaLabel;
    private JLabel puertaLabel;
    private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
	private ArrayList lstEscalera = null;
	private ArrayList lstPlanta = null;
	private ArrayList lstPuerta = null;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelLocalizacionInterna(String label)
    {
        etiqueta = label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        bloqueLabel = new JLabel();
        bloqueJTField = new JTextField();
        escaleraLabel = new JLabel();
        plantaLabel = new JLabel();
        puertaLabel = new JLabel();

        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(bloqueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 20));
        this.add(bloqueJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 100, -1));
        this.add(plantaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 100, 20));
        this.add(getJComboBoxPlanta(), new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 120, -1));
        this.add(escaleraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 100, 20));
        this.add(getJComboBoxEscalera(), new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 48, 100, -1));
        this.add(puertaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 48, 100, 20));
        this.add(getJComboBoxPuerta(), new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 48, 120, -1));

        cargarDatosEscaleraPlantaPuerta();
      
    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
        bloqueJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(bloqueJTField,4);
            }
        });
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
            UtilRegistroExp.retrocedeCaracter(comp,maxLong);
        }
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosLIPanel()
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
        bloqueJTField.setEditable(edit);

    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        if(etiqueta!=null)
        {
            this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        }
        bloqueLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.bloqueLabel"));
        escaleraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.escaleraLabel"));
        plantaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.plantaLabel"));
        puertaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelLocalizacionInterna.puertaLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesRegistroExp que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        hashDatos.put(ConstantesCatastro.direccionBloque, bloqueJTField.getText());
        hashDatos.put(ConstantesCatastro.direccionEscalera, ((Escalera)jComboBoxEscalera.getSelectedItem()).getPatron());
        hashDatos.put(ConstantesCatastro.direccionPlanta, ((Planta)jComboBoxPlanta.getSelectedItem()).getPatron());
        hashDatos.put(ConstantesCatastro.direccionPuerta, ((Puerta)jComboBoxPuerta.getSelectedItem()).getPatron());
        
        return true;
    }

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        bloqueJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionBloque));
        
    	for(int h=0; h< getLstEscalera().size();h++){
    		Escalera escalera = (Escalera)getLstEscalera().get(h);
    		if(escalera.getPatron().equals((String)hashDatos.get(ConstantesCatastro.direccionEscalera))){
    			getjComboBoxEscalera().setSelectedIndex(h);
    		}
    	}
    	
    	for(int h=0; h< getLstPlanta().size();h++){
    		Planta planta = (Planta)getLstPlanta().get(h);
    		if(planta.getPatron().equals((String)hashDatos.get(ConstantesCatastro.direccionPlanta))){
    			getjComboBoxPlanta().setSelectedIndex(h);
    		}
    	}
    	
    	for(int h=0; h< getLstPuerta().size();h++){
    		Puerta puerta = (Puerta)getLstPuerta().get(h);
    		if(puerta.getPatron().equals((String)hashDatos.get(ConstantesCatastro.direccionPuerta))){
    			getjComboBoxPuerta().setSelectedIndex(h);
    		}
    	}
        
    }
    
    public JTextField getBloqueJTField() {
		return bloqueJTField;
	}

	public void setBloqueJTField(JTextField bloqueJTField) {
		this.bloqueJTField = bloqueJTField;
	}

	
	/**
     * This method initializes jTextFieldEscalera	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxEscalera()
    {
        if (jComboBoxEscalera  == null)
        {
        	jComboBoxEscalera = new JComboBox();
        	jComboBoxEscalera.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxEscalera;
    }
    /**
     * This method initializes jTextFieldPlanta   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxPlanta()
    {
        if (jComboBoxPlanta  == null)
        {
        	jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxPlanta;
    }
    
    /**
     * This method initializes jTextFieldPuerta	
     * 	
     * @return javax.swing.JTextField	
     */
    
    private JComboBox getJComboBoxPuerta()
    {
        if (jComboBoxPuerta  == null)
        {
        	jComboBoxPuerta = new JComboBox();
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxPuerta;
    }
    
    private void cargarDatosEscaleraPlantaPuerta(){
    
        EdicionOperations oper = new EdicionOperations();
        setLstEscalera( oper.obtenerEscalera());
        setLstPlanta(oper.obtenerPlanta());
        setLstPuerta(oper.obtenerPuerta());
    	
        EdicionUtils.cargarLista(getJComboBoxEscalera(), getLstEscalera());
        EdicionUtils.cargarLista(getJComboBoxPlanta(), getLstPlanta());
        EdicionUtils.cargarLista(getJComboBoxPuerta(), getLstPuerta());  
    }
    
    public ArrayList getLstEscalera() {
		return lstEscalera;
	}

	public void setLstEscalera(ArrayList lstEscalera) {
		this.lstEscalera = lstEscalera;
	}

	public ArrayList getLstPlanta() {
		return lstPlanta;
	}

	public void setLstPlanta(ArrayList lstPlanta) {
		this.lstPlanta = lstPlanta;
	}

	public ArrayList getLstPuerta() {
		return lstPuerta;
	}

	public void setLstPuerta(ArrayList lstPuerta) {
		this.lstPuerta = lstPuerta;
	}
	
	public JComboBox getjComboBoxEscalera() {
		return jComboBoxEscalera;
	}

	public void setjComboBoxEscalera(JComboBox jComboBoxEscalera) {
		this.jComboBoxEscalera = jComboBoxEscalera;
	}

	public JComboBox getjComboBoxPlanta() {
		return jComboBoxPlanta;
	}

	public void setjComboBoxPlanta(JComboBox jComboBoxPlanta) {
		this.jComboBoxPlanta = jComboBoxPlanta;
	}

	public JComboBox getjComboBoxPuerta() {
		return jComboBoxPuerta;
	}

	public void setjComboBoxPuerta(JComboBox jComboBoxPuerta) {
		this.jComboBoxPuerta = jComboBoxPuerta;
	}


    
}
