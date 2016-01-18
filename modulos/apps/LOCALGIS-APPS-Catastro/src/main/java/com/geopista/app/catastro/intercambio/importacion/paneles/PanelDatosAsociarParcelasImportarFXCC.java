/**
 * PanelDatosAsociarParcelasImportarFXCC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
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

public class PanelDatosAsociarParcelasImportarFXCC extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JLabel referenciaCatastralLabel;
    private JButton referenciaCatastralJButton;
    private JLabel dirTipoViaNombreViaLabel;
    private JButton dirTipoViaNombreViaJButton;
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
    public PanelDatosAsociarParcelasImportarFXCC(String label)
    {
        etiqueta= label;   
        inicializaPanel();

    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     *
     * @param numExp Numero de expediente
     */
    private void inicializaPanel()
    {
        referenciaCatastralLabel = new JLabel();
        referenciaCatastralJButton = new JButton();
        dirTipoViaNombreViaLabel = new JLabel();
        dirTipoViaNombreViaJButton = new JButton();
        poligonoJLabel = new JLabel();
        poligonoJButton = new JButton();
        referenciaCatastralJButton.setIcon(UtilRegistroExp.iconoZoom);
        dirTipoViaNombreViaJButton.setIcon(UtilRegistroExp.iconoZoom);
        poligonoJButton.setIcon(UtilRegistroExp.iconoZoom);

        referenciaCatastralJButton.setPreferredSize(new Dimension(20, 20));
        dirTipoViaNombreViaJButton.setPreferredSize(new Dimension(20, 20));
        poligonoJButton.setPreferredSize(new Dimension(20, 20));
       
        renombrarComponentes();  
        
        this.setLayout(new GridBagLayout());
        
        this.add(referenciaCatastralLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(referenciaCatastralJButton, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 20, 0, 0), 0, 0));

        this.add(poligonoJLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(poligonoJButton, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 20, 0, 0), 0, 0));
        
        this.add(dirTipoViaNombreViaLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(dirTipoViaNombreViaJButton,new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 20, 0, 0), 0, 0));

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
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("Importacion",etiqueta)));
        referenciaCatastralLabel.setText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.referenciaCatastralLabel"));
		referenciaCatastralJButton.setToolTipText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.numExpedienteLabel.hint"));
		dirTipoViaNombreViaLabel.setText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.dirTipoViaNombreViaLabel"));
		dirTipoViaNombreViaJButton.setToolTipText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.dirTipoViaNombreViaJButton.hint"));
		poligonoJLabel.setText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.poligonoJLabel"));
		poligonoJButton.setToolTipText(I18N.get("Importacion","importar.fichero.fxcc.panelBusqueda.poligonoJButton.hint"));
		 
    }
}
