/**
 * PanelDatosDeclaracion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;

import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-ene-2007
 * Time: 16:41:35
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosDeclaracion extends JPanel implements IMultilingue
{
    //private static final JComboBox codCodigoDescriptivoAltJCBox = null;
	private String etiqueta;
    private JLabel tipoDocumentoOrigenAltLabel;
    private JRadioButton tipoDocumentoOrigenAltPublicoRButton;
    private JLabel tipoDocumentoOrigenAltPublicoLabel;
    private JRadioButton tipoDocumentoOrigenAltPrivadoRButton;
    private JLabel tipoDocumentoOrigenAltPrivadoLabel;
    private ButtonGroup tipoDocumentoOrigenAltGroupButton;
    private JLabel infDocumentoOrigenAltLabel;
    private JTextField infDocumentoOrigenAltJTfield;
    private JLabel numBienesInmueblesLabel;
    private JLabel numeroBienesInmuebleUrbLabel;
    private JTextField numeroBienesInmuebleUrbJTfield;
    private JLabel numeroBienesInmueblesRustLabel;
    private JTextField numeroBienesInmueblesRustJTfield;
    private JLabel numeroBienesInmueblesCELabel;
    private JTextField numeroBienesInmueblesCEJTfield;
    private JLabel codigoDescriptivoAltLabel;
    private JComboBox codCodigoDescriptivoAltJCBox;
	private ComboBoxEstructuras codigoDescriptivoAltJCBox;
	private JLabel descripcionAltLabel;
    private JTextPane descripcionAltJTPane;
    private JScrollPane descripcionAltJScrollPane;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosDeclaracion(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        tipoDocumentoOrigenAltLabel = new JLabel();
        tipoDocumentoOrigenAltPublicoRButton = new JRadioButton();
        tipoDocumentoOrigenAltPublicoLabel = new JLabel();
        tipoDocumentoOrigenAltPrivadoRButton = new JRadioButton();
        tipoDocumentoOrigenAltPrivadoLabel = new JLabel();
        tipoDocumentoOrigenAltGroupButton = new ButtonGroup();
        infDocumentoOrigenAltLabel = new JLabel();
        infDocumentoOrigenAltJTfield = new JTextField();
        numBienesInmueblesLabel = new JLabel();
        numeroBienesInmuebleUrbLabel = new JLabel();
        numeroBienesInmuebleUrbJTfield = new JTextField();
        numeroBienesInmueblesRustLabel = new JLabel();
        numeroBienesInmueblesRustJTfield = new JTextField();
        numeroBienesInmueblesCELabel = new JLabel();
        numeroBienesInmueblesCEJTfield = new JTextField();
        codigoDescriptivoAltLabel = new JLabel();
        codCodigoDescriptivoAltJCBox = new JComboBox();
        descripcionAltLabel = new JLabel();
        descripcionAltJTPane = new JTextPane();
        descripcionAltJScrollPane = new JScrollPane();

        inicializaComboBox();

        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        tipoDocumentoOrigenAltGroupButton.add(tipoDocumentoOrigenAltPublicoRButton);
        tipoDocumentoOrigenAltGroupButton.add(tipoDocumentoOrigenAltPrivadoRButton);
        tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPublicoRButton.getModel(), true);

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tipoDocumentoOrigenAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 195, 20));
        this.add(tipoDocumentoOrigenAltPublicoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 50, 20));
        this.add(tipoDocumentoOrigenAltPublicoRButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 20, 20));
        this.add(tipoDocumentoOrigenAltPrivadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 50, 20));
        this.add(tipoDocumentoOrigenAltPrivadoRButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 20, 20));

        this.add(infDocumentoOrigenAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 195, 20));
        this.add(infDocumentoOrigenAltJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 250, -1));

        this.add(numBienesInmueblesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 200, -1));

        this.add(numeroBienesInmuebleUrbLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, 20));
        this.add(numeroBienesInmuebleUrbJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 50, -1));
        this.add(numeroBienesInmueblesRustLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 60, 20));
        this.add(numeroBienesInmueblesRustJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 50, -1));
        this.add(numeroBienesInmueblesCELabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 140, 20));
        this.add(numeroBienesInmueblesCEJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 50, -1));

        this.add(codigoDescriptivoAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 115, 195, 20));
        this.add(codCodigoDescriptivoAltJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190,115, 50, 20));
        this.add(codigoDescriptivoAltJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(245,115, 235, 20));
        this.add(descripcionAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 195, 20));
        descripcionAltJScrollPane.setViewportView(descripcionAltJTPane);
        this.add(descripcionAltJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(230,140, 250, 50));
    }

    /**
     * Inicializa los eventos de los elementos de la gui si son necesarios.
     * En este caso inicializa los eventos para checkear la valided de los datos
     * introducidos en los campos.
     */
    private void inicializarEventos()
    {
        infDocumentoOrigenAltJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(infDocumentoOrigenAltJTfield, 50);
            }
        });

        numeroBienesInmuebleUrbJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmuebleUrbJTfield, 4);
            }
        });

        numeroBienesInmueblesRustJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmueblesRustJTfield, 4);
            }
        });

        numeroBienesInmueblesCEJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(numeroBienesInmueblesCEJTfield, 4);
            }
        });

        descripcionAltJTPane.setEditable(true);
        descripcionAltJTPane.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(descripcionAltJTPane,400);
            }
        });
        
                      
    }

    /**
     * Carga los datos del arrayList pasado por parametro en el comboBox de tipos de vias
     *
     */
    private void inicializaComboBox()
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Estructuras.cargarEstructura("Codigo Descripcion Alteracion");
        codigoDescriptivoAltJCBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        codigoDescriptivoAltJCBox.setEditable(false);        
        
        // se rellena el combo con los códigos de la lista del Codigo descriptivo de la alteración

        Vector vDomainNodes=Estructuras.getListaTipos().getListaSorted(app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"));
        ArrayList arrayKeys= new ArrayList();
        arrayKeys.add("");
        
        if (vDomainNodes!=null)
        {
            for (Enumeration e=vDomainNodes.elements();e.hasMoreElements();)
            {
                   DomainNode auxDomain= (DomainNode)e.nextElement();
                   arrayKeys.add(auxDomain.getPatron());
                  
            }
        }
     
        for(int i=0; i< arrayKeys.size();i++){
        	codCodigoDescriptivoAltJCBox.addItem(arrayKeys.get(i));	
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
                UtilRegistroExp.retrocedeCaracter(comp,maxLong);
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que solo contiene numeros.
     *
     * @param comp  El componente editable
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
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.msg1")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.msg2"));
                UtilRegistroExp.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
                UtilRegistroExp.retrocedeCaracter(comp,maxLong);
            }
        }
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
        infDocumentoOrigenAltJTfield.setEditable(edit);
        numeroBienesInmuebleUrbJTfield.setEditable(edit);
        numeroBienesInmueblesRustJTfield.setEditable(edit);
        numeroBienesInmueblesCEJTfield.setEditable(edit);
        descripcionAltJTPane.setEditable(edit);        
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        tipoDocumentoOrigenAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltLabel"));
        tipoDocumentoOrigenAltPublicoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltPublicoLabel"));
        tipoDocumentoOrigenAltPrivadoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.tipoDocumentoOrigenAltPrivadoLabel"));
        infDocumentoOrigenAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.infDocumentoOrigenAltLabel"));
        numBienesInmueblesLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numBienesInmueblesLabel"));
        numeroBienesInmuebleUrbLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmuebleUrbLabel"));
        numeroBienesInmueblesRustLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmueblesRustLabel"));
        numeroBienesInmueblesCELabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.numeroBienesInmueblesCELabel"));
        codigoDescriptivoAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.codigoDescriptivoAltLabel"));
        descripcionAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosDeclaracion.descripcionAltLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesCatastro que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido. En este caso el valor para uno de los atributos solo
     * puede ser P o R, y se comprueba cual es la eleccion del usuario para almacenar en la hash el valor adecuado con
     * su key adecuada.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        String tipoDocOrig;
        if(tipoDocumentoOrigenAltGroupButton.isSelected(tipoDocumentoOrigenAltPublicoRButton.getModel()))
        {
            tipoDocOrig = ConstantesCatastro.TIPO_DOCUMENTO_PUBLICO;
        }
        else
        {
            tipoDocOrig = ConstantesCatastro.TIPO_DOCUMENTO_PRIVADO;
        }
        
        hashDatos.put(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion, tipoDocOrig);
        hashDatos.put(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion, infDocumentoOrigenAltJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos, numeroBienesInmuebleUrbJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesRusticos, numeroBienesInmueblesRustJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp, numeroBienesInmueblesCEJTfield.getText());

        if(codigoDescriptivoAltJCBox.getSelectedPatron() != null)
            hashDatos.put(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion, codigoDescriptivoAltJCBox.getSelectedPatron());

        hashDatos.put(ConstantesCatastro.expedienteDescripcionAlteracion, descripcionAltJTPane.getText());

        return true;
    }
    
    public JComboBox getCodCodigoDescriptivoAltJCBox() {
		return codCodigoDescriptivoAltJCBox;
	}

	public void setCodCodigoDescriptivoAltJCBox(
			JComboBox codCodigoDescriptivoAltJCBox) {
		this.codCodigoDescriptivoAltJCBox = codCodigoDescriptivoAltJCBox;
	}
	
	public ComboBoxEstructuras getCodigoDescriptivoAltJCBox() {
		return codigoDescriptivoAltJCBox;
	}

	public void setCodigoDescriptivoAltJCBox(
			ComboBoxEstructuras codigoDescriptivoAltJCBox) {
		this.codigoDescriptivoAltJCBox = codigoDescriptivoAltJCBox;
	}


    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesCatastro que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        String tipoDocOrig = (String)hashDatos.get(ConstantesCatastro.expedienteTipoDocumentoOrigenAlteracion);
        if(tipoDocOrig.equalsIgnoreCase(ConstantesCatastro.TIPO_DOCUMENTO_PUBLICO))
        {
            tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPublicoRButton.getModel(), true);
        }
        else
        {
            tipoDocumentoOrigenAltGroupButton.setSelected(tipoDocumentoOrigenAltPrivadoRButton.getModel(), true);            
        }
        infDocumentoOrigenAltJTfield.setText((String)hashDatos.get(ConstantesCatastro.expedienteInfoDocumentoOrigenAlteracion));
        numeroBienesInmuebleUrbJTfield.setText((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesUrbanos));
        numeroBienesInmueblesRustJTfield.setText((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesRusticos));
        numeroBienesInmueblesCEJTfield.setText((String)hashDatos.get(ConstantesCatastro.expedienteNumBienesInmueblesCaractEsp));
        String codigoDescriptivo = (String)hashDatos.get(ConstantesCatastro.expedienteCodigoDescriptivoAlteracion);
        if(codigoDescriptivo!=null)
        {
            codigoDescriptivoAltJCBox.setSelectedPatron(codigoDescriptivo);
        }
        else
        {
            codigoDescriptivoAltJCBox.setSelectedPatron("");            
        }
        // se inicializa el combo del codCodigo descriptivo de alteracion
        String valorDescripcionCodigoDescriptivo = (String)codigoDescriptivoAltJCBox.getSelectedId();
		if(codigoDescriptivo!= null && !codigoDescriptivo.equals("")){
			codCodigoDescriptivoAltJCBox.setSelectedItem(codigoDescriptivo);
		}
		else{
			codCodigoDescriptivoAltJCBox.setSelectedIndex(0);
		}
        
        // cuando se selecciona un Cod en el combo de codigo descriptivo de alteracion se selecciona
        // la descripción en el otro combo de codigo descriptivo
        codCodigoDescriptivoAltJCBox.addActionListener(new ActionListener() 
        {
			
			public void actionPerformed(ActionEvent e) {
				String valorCodCodigoDescriptivo = (String)codCodigoDescriptivoAltJCBox.getSelectedItem();
				if(!valorCodCodigoDescriptivo.equals("")){
					codigoDescriptivoAltJCBox.setSelectedPatron(valorCodCodigoDescriptivo);
				}
				else{
					codigoDescriptivoAltJCBox.setSelectedIndex(0);
					
				}
				
			}
		});
        
        // cuando se selecciona una descripcion en el combo de codigo descriptivo de alteracion se selecciona
        // el codigo en el otro combo de codigo descriptivo
        codigoDescriptivoAltJCBox.addActionListener(new ActionListener() 
        {
			
			public void actionPerformed(ActionEvent e) {
				String valorDescripcionCodigoDescriptivo = (String)codigoDescriptivoAltJCBox.getSelectedPatron();
				if(valorDescripcionCodigoDescriptivo!= null && !valorDescripcionCodigoDescriptivo.equals("")){
					codCodigoDescriptivoAltJCBox.setSelectedItem(valorDescripcionCodigoDescriptivo);
				}
				else{
					codCodigoDescriptivoAltJCBox.setSelectedIndex(0);
					
				}
			}
		});
        
        
        
        
        descripcionAltJTPane.setText((String)hashDatos.get(ConstantesCatastro.expedienteDescripcionAlteracion));
    }    
}
