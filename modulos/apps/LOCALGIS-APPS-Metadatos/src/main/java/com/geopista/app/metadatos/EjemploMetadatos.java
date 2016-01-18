/**
 * EjemploMetadatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos;

import java.awt.HeadlessException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.geopista.app.metadatos.paneles.JPanelShowMetadato;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.metadatos.CI_Citation;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.metadatos.MD_DataIdentification;
import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.metadatos.OperacionesMetadatos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-oct-2004
 * Time: 10:16:37
 */
public class EjemploMetadatos extends javax.swing.JFrame{
    public EjemploMetadatos(String idMetadato) throws HeadlessException {
        //ESTE EJEMPLO MUESTRA COMO MOSTRAR UN METADATO: HAY QUE RELLENAR PREVIAMENTE
        //LAS CONSTANTES url,locale, idMunicipio
        ResourceBundle messages = ResourceBundle.getBundle("config.metadatos", new Locale(com.geopista.app.metadatos.init.Constantes.Locale));
        JPanelShowMetadato panelShowMetadato= new JPanelShowMetadato(messages,this);
        panelShowMetadato.load(idMetadato);
        getContentPane().add(panelShowMetadato,java.awt.BorderLayout.CENTER);
        setSize(800,600);
        show();
    }

    public static void main(String[] args) {
        try
        {
            String idApp="Metadatos";

            ///IMPORTANTE HAY QUE RELLENAR ESTAS 3 CONSTANTES
            com.geopista.app.metadatos.init.Constantes.url= "http://localhost:54321/Metadatos/";
            com.geopista.app.metadatos.init.Constantes.Locale = "es_ES";
            com.geopista.app.metadatos.init.Constantes.idMunicipio=1;
           com.geopista.security.SecurityManager.setsUrl(com.geopista.app.metadatos.init.Constantes.url);
            com.geopista.security.SecurityManager.login("syssuperuser", "sysgeopass",
                                                   idApp);

            //En el metadato hay ciertos campos obligatorios
            // en la base de datos están puesto como "not null"
            // por lo que deben estar rellenos. Si se quiere quitar
            // esta restricción borrar la constraint "not null" de la base de
            // datos.

            MD_Metadata metadato = new MD_Metadata();
            //Este campo es obligatorio
            metadato.setDatestamp(new Date());
            //El contacto es obligatorio para la base de datos
            CI_ResponsibleParty contacto = new CI_ResponsibleParty();
            contacto.setIndividualName("Angeles");
            metadato.setResponsibleParty(contacto);
            //Es obligatorio meter el rol del contacto, este
            // debe ser el id de un domainnode ¿¿Por ahora??
            // a lo mejor lo cambio al patrón como vosotros.
            metadato.setRolecode_id("417");


            //La Identificación necesita una citacion
            CI_Citation citacion = new CI_Citation();
            citacion.setTitle("Ejemplo de funcionamiento");
            //Metemos la identificacion
            MD_DataIdentification md_identification= new MD_DataIdentification();
            md_identification.setResumen("Este ejemplo demuestra como grabar un metadato");
            md_identification.setCitacion(citacion);
            metadato.setIdentificacion(md_identification);

            //Llamamos a la función que salva el metadato
            CResultadoOperacion result=(new OperacionesMetadatos(com.geopista.app.metadatos.init.Constantes.url)).salvarMetadato(metadato);


            if (result.getResultado()&&result.getVector()!=null&&result.getVector().size()>0)
            {
                metadato=(MD_Metadata)result.getVector().elementAt(0);
                System.out.println("Resultado correcto. Identificador del metadato:"+metadato.getFileidentifier());
            }
            else
                System.out.println("Resultado Operacion:"+result.getResultado()+" Mensaje: "+result.getDescripcion());

            //ahora muestro el metadato que acabo de crear
            new EjemploMetadatos(metadato.getFileidentifier());

            com.geopista.security.SecurityManager.logout();


        }catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
