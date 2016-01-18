/**
 * PanelDatosAsociarParcelasBienesInmuebles.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 31-ene-2007
 * Time: 17:16:48
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosAsociarParcelasBienesInmuebles extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JLabel numExpedienteLabel;
    private JTextField numExpedienteJTfield;
    private JLabel referenciaCatastralLabel;
    private JButton referenciaCatastralJButton;
    private JLabel dirTipoViaNombreViaLabel;
    private JButton dirTipoViaNombreViaJButton;
    private JLabel nifTitularJLabel;
    private JButton nifTitularJButton;
    private String convenioExp;
    private JLabel poligonoJLabel;
    private JButton poligonoJButton;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     * @param numExp El numero del expediente
     * @param convenioExp El convenio del Expediente
     */
    public PanelDatosAsociarParcelasBienesInmuebles(String label,String numExp, String convenioExp)
    {
        etiqueta= label;
        this.convenioExp= convenioExp;        
        inicializaPanel(numExp);

    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     *
     * @param numExp Numero de expediente
     */
    private void inicializaPanel(String numExp)
    {
        numExpedienteLabel = new JLabel();
        numExpedienteJTfield = new JTextField();
        referenciaCatastralLabel = new JLabel();
        referenciaCatastralJButton = new JButton();
        dirTipoViaNombreViaLabel = new JLabel();
        dirTipoViaNombreViaJButton = new JButton();
        numExpedienteJTfield.setText(numExp);
        referenciaCatastralJButton.setIcon(UtilRegistroExp.iconoZoom);
        dirTipoViaNombreViaJButton.setIcon(UtilRegistroExp.iconoZoom);
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                    &&convenioExp.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            nifTitularJLabel = new JLabel();
            nifTitularJButton = new JButton();
            nifTitularJButton.setIcon(UtilRegistroExp.iconoZoom);
        }

        poligonoJLabel = new JLabel();
        poligonoJButton = new JButton();
        poligonoJButton.setIcon(UtilRegistroExp.iconoZoom);

        setEditable(false);
        renombrarComponentes();            

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(numExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 195, 20));
        this.add(numExpedienteJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 250, 20));


        this.add(referenciaCatastralLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, 195, 20));
        this.add(referenciaCatastralJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 75, 20, 20));

        this.add(dirTipoViaNombreViaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 105, 195, 20));
        this.add(dirTipoViaNombreViaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 105, 20, 20));

        this.add(poligonoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 75, 195, 20));
        this.add(poligonoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 75, 20, 20));


        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                    &&convenioExp.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            this.add(nifTitularJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 105, 195, 20));
            this.add(nifTitularJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 105, 20, 20));            
        }

    }

   /**
    * Devuelve el panel.
    *
    * @return this
    */
    public JPanel getDatosPanel()
    {
        return this;
    }

    /**
     * Devuelve el boton de busqueda por referencias catastrales para que el JInternalFrame que le llame lo linke
     * y muestre al dialogo para buscar referencias catastrales.
     *
     * @return JButton referenciaCatastralJButton
     */
    public JButton getReferenciaCatastralJButton()
    {
        return referenciaCatastralJButton;
    }

    /**
     * Devuelve el boton de busqueda de referencias catastrales por calle y tipo para que el JInternalFrame que le llame
     * lo linke y muestre el dialogo para buscar las referencias catastrales.
     *
     * @return JButton dirTipoViaNombreViaJButton
     */
    public JButton getDirTipoViaNombreViaJButton()
    {
        return dirTipoViaNombreViaJButton;
    }

    /**
     * Devuelve el boton de busqueda de referencias catastrales por nif de titular para que el JInternalFrame que le llame
     * lo linke y muestre el dialogo para buscar las referencias catastrales.
     *
     * @return JButton nifTitularJButton
     */
    public JButton getNifTitularJButton()
    {
        return nifTitularJButton;
    }

    /**
     * Devuelve el boton de busqueda de referencias catastrales por poligono para parcelas rusticas para que el
     * JInternalFrame que le llame lo linke y muestre el dialogo para buscar las referencias catastrales.
     *
     * @return JButton poligonoJButton
     */
    public JButton getPoligonoJButton()
    {
        return poligonoJButton;
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     */
    public void setEditable(boolean edit)
    {
        numExpedienteJTfield.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        numExpedienteLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.numExpedienteLabel"));
        referenciaCatastralLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.referenciaCatastralLabel"));
        referenciaCatastralJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.numExpedienteLabel.hint"));
        dirTipoViaNombreViaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.dirTipoViaNombreViaLabel"));
        dirTipoViaNombreViaJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.dirTipoViaNombreViaJButton.hint"));
        if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)
                ||(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_FISICO_ECONOMICO)
                    &&convenioExp.equalsIgnoreCase(DatosConfiguracion.CONVENIO_TITULARIDAD)))
        {
            nifTitularJLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.nifTitularJLabel"));
            nifTitularJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.nifTitularJButton.hint"));
        }
        poligonoJLabel.setText(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.poligonoJLabel"));
        poligonoJButton.setToolTipText(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosAsociarParcelasBienesInmuebles.poligonoJButton.hint"));

    }
}
