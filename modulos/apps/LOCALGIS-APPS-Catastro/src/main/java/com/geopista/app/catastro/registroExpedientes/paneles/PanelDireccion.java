/**
 * PanelDireccion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;


import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 10-ene-2007
 * Time: 11:48:33
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDireccion  extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JLabel codigoViaLabel;
    private JLabel busquedaViasLabel;
    private JButton busquedaViaButton;
    private JTextField codigoViaJTField;
    private JLabel tipoViaLabel;
    private ComboBoxEstructuras tipoViaJCBox;
    private JLabel nombreViaLabel;
    private JTextField nombreViaJTField;
    private JLabel primerNumeroLabel;
    private JTextField primerNumeroJTField;
    private JLabel primeraLetraLabel;
	private JTextField primeraLetraJTField;
    private JLabel segundoNumeroLabel;
    private JTextField segundoNumeroJTField;
    private JLabel segundaLetraLabel;
    private JTextField segundaLetraJTField;
    private JLabel kilometroLabel;
    private JTextField kilometroJTField;
    private JLabel dirNoEstrucLabel;
    private JTextField dirNoEstrucJTField;
    private boolean hayLocalizaInter;
    private PanelLocalizacionInterna localizacionInterPanel;

   

	/**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     * @param hayLocalizaInter para mostrar el panel de localizacion interna o no.
     */
    public PanelDireccion(String label, boolean hayLocalizaInter)
    {
        etiqueta = label;
        this.hayLocalizaInter = hayLocalizaInter;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
    	codigoViaLabel = new JLabel();
    	codigoViaJTField = new JTextField();
        tipoViaLabel = new JLabel();
        nombreViaLabel = new JLabel();
        nombreViaJTField = new JTextField();
        primerNumeroLabel = new JLabel();
        primerNumeroJTField = new JTextField();
        primeraLetraLabel = new JLabel();
        primeraLetraJTField = new JTextField();
        segundoNumeroLabel = new JLabel();
        segundoNumeroJTField = new JTextField();
        segundaLetraLabel = new JLabel();
        segundaLetraJTField = new JTextField();
        kilometroLabel = new JLabel();
        kilometroJTField = new JTextField();
        dirNoEstrucLabel = new JLabel();
        dirNoEstrucJTField = new JTextField();
        busquedaViaButton = new JButton();
        busquedaViasLabel = new JLabel();
        
        busquedaViaButton.setIcon(UtilRegistroExp.iconoZoom);
        busquedaViaButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                busquedaViasButtonActionPerformed();
            }
        });
        
        
        inicializaComboBox();
        if(hayLocalizaInter)
        {
            localizacionInterPanel = new PanelLocalizacionInterna(null);
        }
        
        tipoViaJCBox.setEditable(false);
        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        this.add(codigoViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 20));
        this.add(codigoViaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 50, -1));
        this.add(busquedaViasLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 150, 20));
        this.add(busquedaViaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 20, 20));
        this.add(tipoViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, 110, 20));
        this.add(tipoViaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 45, 340, -1));
        this.add(nombreViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 110, 20));
        this.add(nombreViaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 340, -1));

        this.add(primerNumeroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, 150, 20));
        this.add(primerNumeroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 95, 50, -1));
        this.add(primeraLetraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 95, 180, 20));
        this.add(primeraLetraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 95, 30, -1));
        
        this.add(segundoNumeroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 150, 20));
        this.add(segundoNumeroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 50, -1));
        this.add(segundaLetraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 180, 20));
        this.add(segundaLetraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 30, -1));

       
        if(hayLocalizaInter)
        {
            this.add(localizacionInterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 470, 68));
        }

       
        this.add(kilometroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 213, 110, 20));
        this.add(kilometroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 213, 50, -1));
        
        this.add(dirNoEstrucLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 160, 20));
        this.add(dirNoEstrucJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 290, -1));


    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
    	 codigoViaJTField.addCaretListener(new CaretListener()
         {
             public void caretUpdate(CaretEvent evt)
             {
                 chequeaLongYNumCampoEdit(codigoViaJTField,5);
             }
         });
    	 
        nombreViaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreViaJTField,25);
            }
        });

        kilometroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(kilometroJTField,5);
            }
        });

        primerNumeroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(primerNumeroJTField,4);
            }
        });

        primeraLetraJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYLetra(primeraLetraJTField,1);
            }
        });

        segundoNumeroJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(segundoNumeroJTField,4);
            }
        });

        segundaLetraJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYLetra(segundaLetraJTField,1);
            }
        });

        dirNoEstrucJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(dirNoEstrucJTField,25);
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
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que el valor es numerico.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongYNumCampoEdit(final JTextComponent comp, final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            int x=-1;
            try
            {
                x= Integer.parseInt(numAnno);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg2")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg3"));
                UtilRegistroExp.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
                UtilRegistroExp.retrocedeCaracter(comp,maxLong);
            }
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que es una letra
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongYLetra(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            if(numAnno.length()==maxLong&&((numAnno.toUpperCase().charAt(0)<'A')||(numAnno.toUpperCase().charAt(0)>'Z')))
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg4"));
                UtilRegistroExp.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong)
            {
                UtilRegistroExp.retrocedeCaracter(comp,maxLong);
            }
        }
    }

   /**
    * Devuelve el comboBox tipos de vias
    *
    * @return tipoViaJCBox
    * */
    public ComboBoxEstructuras getTipoViaJCBox()
    {
        return tipoViaJCBox;
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosDirPanel()
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
        nombreViaJTField.setEditable(edit);
        busquedaViaButton.setEnabled(edit);
        primerNumeroJTField.setEditable(edit);
        primeraLetraJTField.setEditable(edit);
        segundoNumeroJTField.setEditable(edit);
        segundaLetraJTField.setEditable(edit);
        kilometroJTField.setEditable(edit);
        dirNoEstrucJTField.setEditable(edit);
        if(hayLocalizaInter)
        {
            localizacionInterPanel.setEditable(edit);
        }
     }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        codigoViaLabel.setText(I18N.get("RegistroExpedientes",
        				"Catastro.RegistroExpedientes.PanelDireccion.codigoViaLabel"));
        busquedaViasLabel.setText(I18N.get("RegistroExpedientes",
						"Catastro.RegistroExpedientes.PanelDireccion.busquedaViaLabel"));
        tipoViaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.tipoViaLabel")));
        nombreViaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.nombreViaLabel")));
        primerNumeroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.primerNumeroLabel"));
        primeraLetraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.primeraLetraLabel"));
        segundoNumeroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.segundoNumeroLabel"));
        segundaLetraLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.segundaLetraLabel"));
        kilometroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.kilometroLabel"));
        dirNoEstrucLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.dirNoEstrucLabel"));
        busquedaViaButton.setToolTipText(I18N.get("RegistroExpedientes",
        "Catastro.RegistroExpedientes.PanelDireccion.viasBusquedaButton.hint"));
        if(hayLocalizaInter)
        {
            localizacionInterPanel.renombrarComponentes();
        }
    }

    
    /**
     * Metodo que trata el evento lanzado por el boton de la busqueda de vias y muestra 
     * un nuevo panel con las vias encontradas con el patron de búsqueda del de tipo de via y nombre
     *
     * @param evt Evento lanzado
     * */
    private void busquedaViasButtonActionPerformed()
    {
    	
    	String tipoVia = "";
        if(tipoViaJCBox.getSelectedPatron()!=null){
        	tipoVia = tipoViaJCBox.getSelectedPatron().toString();
        }
        
        Integer codigoVia = null;
        if(codigoViaJTField.getText().equals("")){
        	codigoVia = new Integer(0);
        }
        else{
        	codigoVia = new Integer(codigoViaJTField.getText());
        }
        
        ViasSistemaDialog dialog = new ViasSistemaDialog(nombreViaJTField.getText(),tipoVia,codigoVia);       
        dialog.setVisible(true);             
        tipoViaJCBox.setSelectedPatron(dialog.getTipoVia());
        nombreViaJTField.setText(dialog.getVia());
        if(dialog.getCodigoVia() == -1 || dialog.getCodigoVia() == 0){
        	codigoViaJTField.setText("");
        }
        else{
        	codigoViaJTField.setText(new Integer(dialog.getCodigoVia()).toString());
        }

        
        
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
        if(checkeaParametrosNecesarios())
        {
        	 if(!codigoViaJTField.getText().equals("")){
        		 hashDatos.put(ConstantesCatastro.direccionCodigoVia, (String)codigoViaJTField.getText());
             }
             else{
            	 hashDatos.put(ConstantesCatastro.direccionCodigoVia, "-1");
             }
        	//hashDatos.put(ConstantesRegistroExp.direccionCodigoVia, (String)codigoViaJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionTipoVia, (String)tipoViaJCBox.getSelectedPatron());
            hashDatos.put(ConstantesCatastro.direccionNombreVia, nombreViaJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionPrimerNumero, primerNumeroJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionPrimeraLetra, primeraLetraJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionSegundoNumero, segundoNumeroJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionSegundaLetra, segundaLetraJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionKilometro, kilometroJTField.getText());
            hashDatos.put(ConstantesCatastro.direccionDireccionNoEstructurada, dirNoEstrucJTField.getText());
            if(hayLocalizaInter)
            {
                return localizacionInterPanel.recopilaDatosPanel(hashDatos);
            }
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDireccion.msg1"));
            return false;
        }
    }

    /**
     * Comprueba que los campos necesarios, marcados con asteriscos rojos, no son null o vacios.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaParametrosNecesarios()
    {
       
        return  (!tipoViaJCBox.getSelectedItem().equals(""))&&(!nombreViaJTField.getText().equals(""));
    }



    /**
     * Carga los datos del arrayList pasado por parametro en el comboBox de tipos de vias
     *
     */
    private void inicializaComboBox()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
       /* Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        tipoViaJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
        tipoViaJCBox.setSelectedPatron(patron)
        */
        
        Estructuras.cargarEstructura("Tipo de vía");
        tipoViaJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        
    }

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        if(tipoViaJCBox.getItemCount()==0)
        {
            tipoViaJCBox.addItem(((String)hashDatos.get(ConstantesCatastro.direccionTipoVia)));
        }
        else
        {
            tipoViaJCBox.setSelectedPatron(((String)hashDatos.get(ConstantesCatastro.direccionTipoVia)));
        }
        if(!((String)hashDatos.get(ConstantesCatastro.direccionCodigoVia)).equals("") &&
        		Integer.valueOf((String)hashDatos.get(ConstantesCatastro.direccionCodigoVia)) < 0){
        	 codigoViaJTField.setText("");
        }
        else{
        	 codigoViaJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionCodigoVia));
        }
       
        nombreViaJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionNombreVia));
        primerNumeroJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionPrimerNumero));
        primeraLetraJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionPrimeraLetra));
        segundoNumeroJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionSegundoNumero));
        segundaLetraJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionSegundaLetra));
        kilometroJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionKilometro));
        dirNoEstrucJTField.setText((String)hashDatos.get(ConstantesCatastro.direccionDireccionNoEstructurada));
        if(hayLocalizaInter)
        {
            localizacionInterPanel.inicializaDatos(hashDatos);
        }
    }
    
    public JTextField getNombreViaJTField() {
		return nombreViaJTField;
	}

	public void setNombreViaJTField(JTextField nombreViaJTField) {
		this.nombreViaJTField = nombreViaJTField;
	}

	public JTextField getPrimerNumeroJTField() {
		return primerNumeroJTField;
	}

	public void setPrimerNumeroJTField(JTextField primerNumeroJTField) {
		this.primerNumeroJTField = primerNumeroJTField;
	}

	public JTextField getPrimeraLetraJTField() {
		return primeraLetraJTField;
	}

	public void setPrimeraLetraJTField(JTextField primeraLetraJTField) {
		this.primeraLetraJTField = primeraLetraJTField;
	}

	public JTextField getSegundoNumeroJTField() {
		return segundoNumeroJTField;
	}

	public void setSegundoNumeroJTField(JTextField segundoNumeroJTField) {
		this.segundoNumeroJTField = segundoNumeroJTField;
	}

	public JTextField getSegundaLetraJTField() {
		return segundaLetraJTField;
	}

	public void setSegundaLetraJTField(JTextField segundaLetraJTField) {
		this.segundaLetraJTField = segundaLetraJTField;
	}

	public JTextField getKilometroJTField() {
		return kilometroJTField;
	}

	public void setKilometroJTField(JTextField kilometroJTField) {
		this.kilometroJTField = kilometroJTField;
	}

	public JTextField getDirNoEstrucJTField() {
		return dirNoEstrucJTField;
	}

	public void setDirNoEstrucJTField(JTextField dirNoEstrucJTField) {
		this.dirNoEstrucJTField = dirNoEstrucJTField;
	}
	
	 public PanelLocalizacionInterna getLocalizacionInterPanel() {
		return localizacionInterPanel;
	}

	public void setLocalizacionInterPanel(
			PanelLocalizacionInterna localizacionInterPanel) {
		this.localizacionInterPanel = localizacionInterPanel;
	}

	 public JTextField getCodigoViaJTField() {
		return codigoViaJTField;
	}

	public void setCodigoViaJTField(JTextField codigoViaJTField) {
		this.codigoViaJTField = codigoViaJTField;
	}

		
}
