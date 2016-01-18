/**
 * GeopistaPrintableLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.actividad;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.geopista.app.utilidades.xml.GeopistaTranslationInfo;
import com.geopista.app.utilidades.xml.GeopistaTranslator;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-abr-2005
 * Time: 15:56:49
 */
public class GeopistaPrintableLicencias extends javax.swing.JPanel implements Printable{
    private static Logger logger = Logger.getLogger(GeopistaPrintableLicencias.class);

    public final static int FICHA_ADMINISTRADOR = 0;
    public final static int FICHA_CONTAMINANTE_ACTIVIDAD = 1;
    public final static int FICHA_CONTAMINANTE_ARBOLADO = 2;
    public final static int FICHA_CONTAMINANTE_VERTEDERO = 3;
    public final static int FICHA_ACTIVIDAD_CONSULTA = 4;
    public final static int FICHA_ACTIVIDAD_MODIFICACION = 5;
    public final static int FICHA_LICENCIAS_CONSULTA = 6;
    public final static int FICHA_LICENCIAS_MODIFICACION = 7;
    public final static int FICHA_LICENCIAS_OBRA_MENOR_CONSULTA = 8;
    public final static int FICHA_LICENCIAS_OBRA_MENOR_MODIFICACION = 9;
    public final static int FICHA_LICENCIAS_PLANOS = 10;
    public final static int FICHA_OCUPACIONES_CONSULTA = 11;
    public final static int FICHA_OCUPACIONES_MODIFICACION = 12;
    public final static int FICHA_OCUPACIONES_PLANOS = 13;
    public final static int FICHA_OCUPACIONES_UTILIDADES = 14;
    public final static int FICHA_CONTAMINANTE_PLANOS = 15;
    public final static int FICHA_LICENCIAS_ACTIVIDAD_CONSULTA = 16;
    public final static int FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION = 17;

    public static final int supertitulo=0;
    public static final int titulo=1;
    public static final int detalle=2;
    private int derecha=0;
    private int pagina=0;
    private int altura=0;
    private Object objeto;
    private Class clase;
    private Image plano;
    private GeopistaTranslator traductor;
    private int tipoFicha = -1;

    private ResourceBundle rbI18N = null;

    public int print(Graphics g, PageFormat format, int pageIndex) throws PrinterException {

        try
        {
            switch (tipoFicha) {
                case FICHA_ADMINISTRADOR:
                    break;
                case FICHA_ACTIVIDAD_CONSULTA:
                case FICHA_ACTIVIDAD_MODIFICACION:
                case FICHA_LICENCIAS_CONSULTA:
                case FICHA_LICENCIAS_MODIFICACION:
                case FICHA_LICENCIAS_PLANOS:
                    rbI18N = com.geopista.app.licencias.CMainLicencias.literales;
                    break;
                case FICHA_LICENCIAS_ACTIVIDAD_CONSULTA:
                case FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION:
                    rbI18N = com.geopista.app.licencias.actividad.MainActividad.literales;
                    break;
                case FICHA_LICENCIAS_OBRA_MENOR_CONSULTA:
                case FICHA_LICENCIAS_OBRA_MENOR_MODIFICACION:
                    rbI18N = com.geopista.app.licencias.CMainLicencias.literales;
                    break;
            }

            Graphics2D g2d = (Graphics2D)g;
            init();
            pintarEncabezadoPagina(g2d,format);
            pintarPiePagina(g2d,format);
            Vector vectorNodos= traductor.getNodes(traductor.getRoot());
            for(Enumeration e =vectorNodos.elements(); e.hasMoreElements();)
            {
                Node xmlHoja = (Node)e.nextElement();
                boolean bTipoCampo=false;
                boolean bTipoVector=false;
                boolean bTipoDetalleCampoN=false;  // FRAN
                boolean bucleVector = false;
                //Object vObj = null;
                Object antObj = objeto;
                /* // para intentar anidar vectores.
                Vector antObj = new Vector();
                Vector antXmlHoja = new Vector();
                Vector antPos = new Vector();
                */
                //antObj.add(objeto);
                int vPos = 0;
                Node vXmlHoja = null;
                Enumeration venum = null;
                do {
                    bTipoCampo=false;
                    bTipoVector=false;
                    bTipoDetalleCampoN=false;  // FRAN
                    String sValor="";
                    String sCampo[] = null;  // FRAN
                    Object sVObj = null;
                    try{bTipoCampo=(traductor.getAtributo(xmlHoja, "tipo").equalsIgnoreCase("Campo"));}catch (Exception ex){}
                    try{bTipoVector=(traductor.getAtributo(xmlHoja, "tipo").equalsIgnoreCase("Vector"));}catch (Exception ex){}
                    // FRAN
                    try{
                        bTipoDetalleCampoN=(traductor.getAtributo(xmlHoja, "tipo").equalsIgnoreCase("DetalleCampo"));
                        sCampo = traductor.getAtributo(xmlHoja, "campo").split(":");
                    }catch (Exception ex){}
                    String sClase = "";
                    try{
                        sClase = traductor.getAtributo(xmlHoja, "clase");
                        sClase = (sClase==null)?"":sClase;
                    }catch (Exception ex){
                        sClase = "";
                    }
                    //FRAN

                    if (bTipoCampo && clase!=null && objeto!=null)
                    {
                        try
                        {
                            Class[] argumentTypes = {  };
                            Method aMethod =clase.getMethod(traductor.devuelveValor(xmlHoja),argumentTypes);
                            Object[] arguments = { };
//                            Object sVObj = null;
                            if (sClase.equalsIgnoreCase("Date") || sClase.equalsIgnoreCase("Hora")) {
                                sVObj=(aMethod.invoke(objeto, arguments)!=null?aMethod.invoke(objeto, arguments):null);
                            }
                            else {
                                sValor=(aMethod.invoke(objeto, arguments)!=null?aMethod.invoke(objeto, arguments).toString():"");
                            }
                        }catch(Exception ex)
                        {
                            logger.error("Error: ",ex);
                        }
                    }
                    //FRAN
                    else if (bTipoDetalleCampoN && clase!=null && objeto!=null)
                    {
                        try
                        {
                            Class[] argumentTypes = {  };
                            Object[] arguments = { };
                            Method aMethod = null;
                            Object mobj = null;
                            aMethod = clase.getMethod(sCampo[0],argumentTypes);
                            mobj = aMethod.invoke(objeto, arguments);
                            int i = 1;

                            while ((i < sCampo.length) && (mobj != null)) {
                                aMethod = mobj.getClass().getMethod(sCampo[i],argumentTypes);
                                mobj = aMethod.invoke(mobj, arguments);
                                i++;
                            }
//                            Object sVObj = null;
                            if (mobj != null) {
                                aMethod = mobj.getClass().getMethod(traductor.devuelveValor(xmlHoja),argumentTypes);
                                if (sClase.equalsIgnoreCase("Date") || sClase.equalsIgnoreCase("Hora")) {
                                    sVObj=(aMethod.invoke(mobj, arguments)!=null?aMethod.invoke(mobj, arguments):null);
                                }
                                else {
                                    sValor=(aMethod.invoke(mobj, arguments)!=null?aMethod.invoke(mobj, arguments).toString():"");
                                }
                            }
                            else
                                sValor = "";
                        }catch(Exception ex)
                        {
                            logger.error("Error: ",ex);
                        }
                    }
                    else if (bTipoVector && clase!=null && objeto!=null)
                   {
                       try
                       {
                           Class[] argumentTypes = {  };
                           Object[] arguments = { };
                           Method aMethod = null;
                           Object mobj = null;
                           sCampo = traductor.getAtributo(xmlHoja, "source").split(":");
                           aMethod = clase.getMethod(sCampo[0],argumentTypes);
                           mobj = aMethod.invoke(objeto, arguments);
                           Object obj = null;
                           int i = 1;

                           while ((i < sCampo.length-1) && (mobj != null)) {
                               aMethod = mobj.getClass().getMethod(sCampo[i],argumentTypes);
                               mobj = aMethod.invoke(mobj, arguments);
                               i++;
                           }
                           if (i < sCampo.length) {
                               aMethod =mobj.getClass().getMethod(sCampo[i],argumentTypes);
                               obj=(aMethod.invoke(mobj, arguments)!=null?aMethod.invoke(mobj, arguments):null);
                           }
                           else {
                               obj = mobj;
                           }
                           if (obj!=null && obj instanceof Vector)
                           {
                               venum=((Vector)obj).elements();
                               /* // para intentar anidar vectores.
                               antObj.add(objeto);
                               antXmlHoja.add(xmlHoja);
                               antPos.add(new Integer(vPos));
                               */

                               if ((venum != null) && (venum.hasMoreElements())) {
                                   bucleVector = true;
                                   antObj = objeto;
                                   objeto = venum.nextElement();
                                   clase = objeto.getClass();
                                   vXmlHoja = xmlHoja;
                                   xmlHoja = vXmlHoja.getChildNodes().item(vPos++);
                               }
                           }
                           }catch(Exception ex)
                           {
                                //logger.error("Error: ",ex);
                            }
                            continue;
                    }
                    // FRAN
                    else
                        sValor=traductor.devuelveValor(xmlHoja);

                    if ((bTipoCampo || bTipoDetalleCampoN) && clase!=null && objeto!=null) {
                        if (sClase.equalsIgnoreCase("Domain")) {
                            String sDomSource = traductor.getAtributo(xmlHoja, "domsource");
//                                Object oEstruct = null;
                            if ((sValor != null) && (!sValor.equals(""))) {
                                switch (tipoFicha) {
                                    case FICHA_ADMINISTRADOR:
                                        break;
                                    case FICHA_ACTIVIDAD_CONSULTA:
                                    case FICHA_ACTIVIDAD_MODIFICACION:
                                    case FICHA_LICENCIAS_CONSULTA:
                                    case FICHA_LICENCIAS_MODIFICACION:
                                    case FICHA_LICENCIAS_PLANOS:
                                        if (sDomSource.equalsIgnoreCase("TiposTramitacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposTramitacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("EstadosExpediente"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaEstados().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposObra"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposObra().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposActividad"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposActividad().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposFinalizacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposFinalizacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("ViasNotificacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaViasNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposViaINE"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposViaINE().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposNotificacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("EstadosNotificacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaEstadosNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionObligatoriaObraMayor")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaDocumentacionObligatoriaObraMayor().getLista();
                                            Enumeration _e=hDomainNodes.elements();
                                            while (_e.hasMoreElements())
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getIdNode().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionMejorasObraMayor") ||
                                                sDomSource.equalsIgnoreCase("DocumentacionAlegacionObraMayor") ) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor)) {
                                                    if ((tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_CONSULTA) || (tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION))
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                                    else
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                                }
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionAnexoObraMayor")) {
                                        	Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                        	for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                        	{
                                        		DomainNode auxDomain= (DomainNode)_e.nextElement();
                                        		if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                        			sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                        	}
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionTipoNotificacion")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposNotificacion().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionEstadoNotificacion")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaEstadosNotificacion().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionTipoInforme")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposInforme().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else
                                            sValor= "";
                                        break;
                                    case FICHA_LICENCIAS_ACTIVIDAD_CONSULTA:
                                    case FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION:
                                    case FICHA_LICENCIAS_OBRA_MENOR_CONSULTA:
                                    case FICHA_LICENCIAS_OBRA_MENOR_MODIFICACION:
                                        if (sDomSource.equalsIgnoreCase("TiposTramitacion"))
                                            if ((tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_CONSULTA) || (tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION)) {
                                                Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposTramitacion().getLista();
                                                for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                                {
                                                    DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                    if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                                }
                                            }
                                            else {
                                                sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposTramitacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                            }
                                        else if (sDomSource.equalsIgnoreCase("EstadosExpediente"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaEstados().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposActividad"))
                                            if ((tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_CONSULTA) || (tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION)) {
                                                Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposActividad().getLista();
                                                for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                                {
                                                    DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                    if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                                }
                                            }
                                            else {
                                                sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposObraMenor().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                            }
                                        else if (sDomSource.equalsIgnoreCase("TiposFinalizacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposFinalizacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("ViasNotificacion")) {
                                            if ((tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_CONSULTA) || (tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION)) {
                                                Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaViasNotificacion().getLista();
                                                for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                                {
                                                    DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                    if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                                }
                                            }
                                            else {
                                               sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaViasNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("TiposViaINE"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposViaINE().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("TiposNotificacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaTiposNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("EstadosNotificacion"))
                                            sValor = ((DomainNode)com.geopista.app.licencias.estructuras.Estructuras.getListaEstadosNotificacion().getDomainNode(sValor)).getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());//currentLocale.toString());
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionObligatoriaObraMenor")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaDocumentacionObligatoriaObraMayor().getLista();    // TODO: a lo mejor debería ser de obra menor.
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getIdNode().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionAnexoObraMenor")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionMejorasObraMenor")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionAlegacionObraMenor")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionTipoNotificacion")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposNotificacion().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionEstadoNotificacion")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaEstadosNotificacion().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionTipoInforme")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposInforme().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor)) {
                                                    if ((tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_CONSULTA) || (tipoFicha==FICHA_LICENCIAS_ACTIVIDAD_MODIFICACION))
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                                    else
                                                        sValor = auxDomain.getTerm(com.geopista.app.licencias.CMainLicencias.literales.getLocale().toString());
                                                }
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionAnexoActividad")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaTiposAnexo().getLista();
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getPatron().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                            }
                                        }
                                        else if (sDomSource.equalsIgnoreCase("DocumentacionObligatoriaActividad")) {
                                            Hashtable hDomainNodes = com.geopista.app.licencias.estructuras.Estructuras.getListaDocumentacionObligatoriaObraMayor().getLista();    // TODO: a lo mejor debería ser de obra menor.
                                            for (Enumeration _e=hDomainNodes.elements();_e.hasMoreElements();)
                                            {
                                                DomainNode auxDomain= (DomainNode)_e.nextElement();
                                                if (auxDomain.getIdNode().equalsIgnoreCase(sValor))
                                                    sValor = auxDomain.getTerm(com.geopista.app.licencias.actividad.MainActividad.literales.getLocale().toString());
                                            }
                                        }
                                        else
                                            sValor= "";
                                        break;                                    
                                }
                            }
                            else
                                sValor = "";
                        }
                        else if (sClase.equalsIgnoreCase("Date")) {
                            try {
                                sValor = (new SimpleDateFormat("dd-MM-yyyy").format(sVObj));
                            } catch (Exception exc) {}
                        }
                        else if (sClase.equalsIgnoreCase("IBool")) {
                            sValor = (sValor.equalsIgnoreCase("0"))?"NO":"SI";
                        }
                        else if (sClase.equalsIgnoreCase("Bool")) {
                            sValor = (sValor.equalsIgnoreCase("false"))?"NO":"SI";
                        }
                        else if (sClase.equalsIgnoreCase("IdInspector")) {
                            sValor = getNombreInspector(sValor);   // TODO: implementar relamente este contenido.
                        }
                        else if (sClase.equalsIgnoreCase("Hora")) {
                            try {
                                sValor = (new SimpleDateFormat("HH:mm").format(sVObj));
                            }
                            catch (Exception exc) {}
                        }
                    }


                   if (xmlHoja.getNodeName().equalsIgnoreCase("SuperTitulo"))
                        printSuperTitulo(g2d,format, pageIndex,sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("Cabecera"))
                        printCabecera(g2d,format, pageIndex, sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("Plano"))
                    {
                        double ancho=format.getImageableWidth();
                        double alto=format.getImageableHeight();
                        try{ancho=new Double(traductor.getAtributo(xmlHoja, "width")).doubleValue();}catch (Exception ex){}
                        try{alto=new Double(traductor.getAtributo(xmlHoja, "height")).doubleValue();}catch (Exception ex){}
                        printPlano(g2d,format, pageIndex, ancho,alto);
                    }
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("Titulo"))
                        printTitulo(g2d,format, pageIndex, sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("TituloDetalle"))
                        printTituloDetalle(g2d,format, pageIndex, sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("Detalle"))
                        printDetalle(g2d,format, pageIndex, sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("Pie"))
                        printPie(g2d,format, pageIndex, sValor);
                    else if (xmlHoja.getNodeName().equalsIgnoreCase("p"))
                        printSaltoLinea(g2d,format, pageIndex);

                    else if (xmlHoja.getNodeName().equalsIgnoreCase("SuperTituloI18N")) {
                         String sI18N = rbI18N.getString(sValor);
                         printSuperTitulo(g2d,format, pageIndex,sI18N);
                    }
                     else if (xmlHoja.getNodeName().equalsIgnoreCase("CabeceraI18N")) {
                         String sI18N = rbI18N.getString(sValor);
                         printCabecera(g2d,format, pageIndex, sI18N);
                     }
                     else if (xmlHoja.getNodeName().equalsIgnoreCase("TituloI18N")) {
                         String sI18N = rbI18N.getString(sValor);
                         printTitulo(g2d,format, pageIndex, sI18N);
                     }
                     else if (xmlHoja.getNodeName().equalsIgnoreCase("TituloDetalleI18N")) {
                         String sI18N = rbI18N.getString(sValor);
                         printTituloDetalle(g2d,format, pageIndex, sI18N);
                     }
                     else if (xmlHoja.getNodeName().equalsIgnoreCase("PieI18N")) {
                         String sI18N = rbI18N.getString(sValor);
                         printPie(g2d,format, pageIndex, sI18N);
                     }

                    if (bucleVector) {
                        xmlHoja = vXmlHoja.getChildNodes().item(vPos);
                        if (xmlHoja == null) {
                            if (venum.hasMoreElements()) {
                                objeto = venum.nextElement();
                                vPos = 0;
                                xmlHoja = vXmlHoja.getChildNodes().item(vPos);
                            }
                            else {
                                bucleVector = false;
                            }
                        }
                        else
                            vPos++;
                    }

                    /* // para intentar anidar vectores.
                    if (!bucleVector) {
                        if (antObj.size() > 1) {
                            bucleVector = true;
                            int pos = antObj.size();
                            objeto = antObj.elementAt(pos);
                            xmlHoja = (Node)antXmlHoja.elementAt(antXmlHoja.size());
                            vPos = ((Integer) antPos.elementAt(pos)).intValue();
                            antObj.removeElementAt(pos);
                            antXmlHoja.removeElementAt(pos);
                            antPos.removeElementAt(pos);
                            clase = objeto.getClass();
                        }
                    }
                    */
            } while (bucleVector);
            /* // para intentar anidar vectores.
            if (antObj.size() > 1) {
                int pos = antObj.size();
                objeto = antObj.elementAt(pos);
                antObj.removeElementAt(pos);
                antXmlHoja.removeElementAt(antXmlHoja.size());
                antPos.removeElementAt(pos);
            }
            */
            objeto = antObj;
            if (objeto != null)
                clase = objeto.getClass();
        }

     }catch(Exception e)
        {
            logger.error("Error al imprimir el plano",e);

        }

          if (pageIndex>getPaginasDocumento())
              return Printable.NO_SUCH_PAGE;
          return Printable.PAGE_EXISTS;
    }
    private void  pintarPiePagina(Graphics2D g2d,PageFormat format)
    {
        //Pintamos el pie de pagina
        Element piePagina= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "piepagina");
        Element piePagina2= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "piepaginaI18N");
        double moverY=0;
        if (piePagina!=null)
        {
           moverY=(format.getImageableHeight()-altura-20);
           g2d.translate(-derecha,moverY);
           g2d.drawLine(0,0, new Double(format.getImageableWidth()).intValue(),0);

           String sPiePagina = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "piepagina");
           derecha=0;
           if (sPiePagina!=null)
           {
                int alturaOld=altura;
                altura=new Double(format.getImageableHeight()-20).intValue();
                printEncabezado(g2d,format,-1,sPiePagina);
                altura=alturaOld;
           }
           g2d.translate(0,-moverY);

        }
        else if (piePagina2!=null)
        {
           moverY=(format.getImageableHeight()-altura-20);
           g2d.translate(-derecha,moverY);
           g2d.drawLine(0,0, new Double(format.getImageableWidth()).intValue(),0);

           String sPiePagina = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "piepaginaI18N");
           String sI18N = rbI18N.getString(sPiePagina);
           derecha=0;
           if (sPiePagina!=null)
           {
                int alturaOld=altura;
                altura=new Double(format.getImageableHeight()-20).intValue();
                printEncabezado(g2d,format,-1,sI18N);//sPiePagina);
                altura=alturaOld;
           }
           g2d.translate(0,-moverY);

        }
    }
    private void  pintarEncabezadoPagina(Graphics2D g2d,PageFormat format)
    {
         Element encabezado= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "encabezado");
         Element encabezado2= GeopistaTranslator.recuperarHijo(traductor.getRoot(), "encabezadoI18N");
         if (encabezado!=null)
        {
            String sEncabezado = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "encabezado");
            if (sEncabezado!=null)
            {
                g2d.translate(format.getImageableX(), format.getImageableY());
                printEncabezado(g2d,format,-1,sEncabezado);
                g2d.drawLine(0, 0
                          , new Double(format.getImageableWidth()).intValue(),0);
                g2d.translate(0, 10);
                altura+=10;
            }
            else
            {
                g2d.translate(format.getImageableX(), format.getImageableY());
                g2d.drawLine(new Double(format.getImageableX()).intValue(), new Double(format.getImageableY()-10).intValue()
                     , new Double(format.getImageableWidth()-derecha).intValue(),new Double(format.getImageableY()-10).intValue());
                g2d.translate(0, 10);
                altura+=10;
            }
        }
        else if (encabezado2!=null)

       {
           String sEncabezado = GeopistaTranslator.recuperarHoja(traductor.getRoot(), "encabezadoI18N");
            String sI18N = rbI18N.getString(sEncabezado);
           if (sEncabezado!=null)
           {
               g2d.translate(format.getImageableX(), format.getImageableY());
               printEncabezado(g2d,format,-1,sI18N);
               g2d.drawLine(0, 0
                         , new Double(format.getImageableWidth()).intValue(),0);
               g2d.translate(0, 10);
               altura+=10;
           }
           else
           {
               g2d.translate(format.getImageableX(), format.getImageableY());
               g2d.drawLine(new Double(format.getImageableX()).intValue(), new Double(format.getImageableY()-10).intValue()
                    , new Double(format.getImageableWidth()-derecha).intValue(),new Double(format.getImageableY()-10).intValue());
               g2d.translate(0, 10);
               altura+=10;
           }
       }
       else
         g2d.translate(format.getImageableX(), format.getImageableY());
    }

    public void printPlano(Graphics2D g2d, PageFormat pageFormat, int pageIndex, double ancho, double largo)
    {
        if (plano!=null)
        {
             if ((derecha>0)&&g2d!=null)
             {
                g2d.translate(-derecha,0);
                derecha=0;
             }
             if (ancho>pageFormat.getImageableWidth()-4) ancho=pageFormat.getImageableWidth()-4;
             if (largo>pageFormat.getImageableHeight()-4) largo=pageFormat.getImageableHeight()-4;
             //encuadramos la pagina en el rectangulo
             if ((ancho/plano.getWidth(this)> (largo/plano.getHeight(this))))
                  ancho=(plano.getWidth(this)*largo)/plano.getHeight(this);
             else
                  largo=(plano.getHeight(this)*ancho)/plano.getWidth(this);
             if ((altura+largo+4)>pageFormat.getImageableHeight())
             {
                 //pintarPiePagina(g2d,pageFormat);
                 pagina++;
                 altura=0;
             }

             if (pagina==pageIndex)
             {
                try
                {
                 Shape s = new Rectangle(0,altura,new Double(ancho+4).intValue(),new Double(largo+4).intValue());
                 g2d.setPaint(Color.black);
                 g2d.draw(s);
                 g2d.drawImage(plano,2,altura+2, new Double(ancho).intValue(),new Double(largo).intValue(),this);
                 g2d.translate(0,altura+new Double(largo).intValue()+6);
             }catch(Exception e)
             {
                logger.error("Error al pintar el plano:",e);
                }
             }
              setAltura(altura+new Double(largo).intValue()+6);
        }
    }
    public int printEncabezado(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.PLAIN, 8);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font, true);
    }
    public int printDetalle(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.PLAIN, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena,detalle,font);
    }
    public int printTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 12);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }
    public int printPie(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
   {
       Font font = new Font("Verdana", Font.PLAIN, 8);
       if (g2d!=null) g2d.setFont(font);
       return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
   }

    public int printTituloDetalle(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        if ((cadena!=null)&&(cadena.indexOf(":")<0)) cadena+=":";
        Font font = new Font("Verdana", Font.BOLD, 10);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena+" ", titulo,font );
    }
    public int printSuperTitulo(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 14);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }
    public int printSaltoLinea(Graphics2D g2d, PageFormat pageFormat, int pageIndex)
    {
        Font font = new Font("Verdana", Font.BOLD, 12);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, " ", supertitulo,font);
    }
    public int printCabecera(Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena)
    {
        Font font = new Font("Verdana", Font.BOLD, 16);
        if (g2d!=null) g2d.setFont(font);
        return printString ( g2d, pageFormat,  pageIndex, cadena, supertitulo,font);
    }

    public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont)
    {
        return printString(g2d, pageFormat, pageIndex, cadena,  tipo,  dataFont, false);
    }
    public int printString (Graphics2D g2d, PageFormat pageFormat, int pageIndex, String cadena, int tipo, Font dataFont, boolean bPiePagina)
    {
          if (tipo==supertitulo|| tipo==titulo)
         {
             if ((derecha>0)&&g2d!=null)
             {
                g2d.translate(-derecha,0);
                derecha=0;
             }
         }
         if (cadena==null || cadena.length()<=0)
            cadena="  ";
         if (g2d!=null)
             System.out.println("PAGINA: "+pageIndex+" GRAFICO X: "+g2d.getTransform().getTranslateX()+" -  Y:"+g2d.getTransform().getTranslateY()+" ALTURA:"+altura+" CADENA:"+cadena);

         int imover=0;
         if (g2d!=null) g2d.setPaint(Color.black);
         Point2D.Float pen = new Point2D.Float();
         AttributedCharacterIterator charIterator = new AttributedString(cadena,dataFont.getAttributes()).getIterator();
         LineBreakMeasurer measurer;
         if (g2d!=null)
             measurer= new LineBreakMeasurer(charIterator,g2d.getFontRenderContext());
         else
             measurer= new LineBreakMeasurer(charIterator,new FontRenderContext(new AffineTransform(),false,false));

         float wrappingWidth = (float) pageFormat.getImageableWidth()-derecha;
         int bSegunda=1;
         boolean bQuitaMargen=false;
         while (measurer.getPosition() < charIterator.getEndIndex()) {
            if (bSegunda==2 && derecha>0)
            {
                bQuitaMargen=true;
                if (g2d!=null) g2d.translate(-derecha,0);
                wrappingWidth+=derecha;

            }

            bSegunda++;
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            if ((altura+layout.getAscent()+layout.getDescent() + layout.getLeading()+20>pageFormat.getImageableHeight())
                    && !bPiePagina) //pasamos de pagina
            {
                 //pintarPiePagina(g2d,pageFormat);
                 pagina++;
                 altura=0;
           }
            pen.y += layout.getAscent();
            float dx = layout.isLeftToRight()? 0 :
                    (wrappingWidth - layout.getAdvance());
            if (pagina==pageIndex || bPiePagina)
                if (g2d!=null)layout.draw(g2d, pen.x + dx, pen.y);

            pen.y += layout.getDescent() + layout.getLeading();
            imover+=layout.getBounds().getBounds2D().getWidth();

       }
        if (bQuitaMargen)
        {
             if (g2d!=null) g2d.translate(derecha,0);
        }
       if (tipo==titulo)
       {
            imover+=10;
             if (g2d!=null) g2d.translate(imover,0);
            derecha+=imover;
       }
       else
       {
            if (pagina==pageIndex || bPiePagina)
            {
                 if (g2d!=null) g2d.translate(0,pen.y);
            }
           altura+=pen.y+4;
       }

       return Printable.PAGE_EXISTS;
     }
    public void init()
    {
         derecha=0;
         pagina=0;
         altura=0;
    }

    public int getPaginasDocumento() {
        return pagina;
    }
    public void setAltura(int iAltura)
    {
        altura=iAltura;
    }

    public void printObjeto(String urlXml, Object objeto, Class clase, LayerViewPanel layerViewPanel, int tipoFicha) throws Exception
    {
        this.tipoFicha = tipoFicha;
              int altoPanel = layerViewPanel.getViewport().getPanel().getHeight();
              int anchoPanel = layerViewPanel.getViewport().getPanel().getWidth();
              Image image = layerViewPanel.createImage(anchoPanel, altoPanel);

              GeopistaLayerNamePanel legendPanel = new GeopistaLayerNamePanel(
                   layerViewPanel,new GeopistaLayerTreeModel(layerViewPanel),layerViewPanel.getRenderingManager(),
                   new HashMap());

              Graphics g = image.getGraphics();
              layerViewPanel.getViewport().getPanel().printAll(g);
              printObjeto(urlXml, objeto , clase, image, tipoFicha);
    }

    public void printObjeto(String urlXml, Object objeto, Class clase, Image plano, int tipoFicha) throws Exception
    {
        this.tipoFicha = tipoFicha;
            this.objeto=objeto;
            this.plano=plano;
            this.clase=clase;

             ClassLoader cl =(new GeopistaPrintableLicencias()).getClass().getClassLoader();
            java.net.URL url=cl.getResource(urlXml);

            traductor= new  GeopistaTranslator(new GeopistaTranslationInfo(url,false));
            traductor.parsear();


          PrinterJob printerJob=PrinterJob.getPrinterJob();
          //Imprimo en formato A$
          PageFormat format = new PageFormat();
          format.setOrientation(PageFormat.PORTRAIT);
          Paper paper = format.getPaper();
//          paper.setSize(587, 842); // Svarer til A4
          paper.setSize(594.936, 841.536); // A4
//          paper.setImageableArea(44, 52, 500, 738);
          paper.setImageableArea(44, 52, 506, 738);
          format.setPaper(paper);
          //Obtengo un vector con los graficos a imprimir
          //printPanel(null, format, -1);
          int paginas = 9999;
          logger.warn("Se van a imprimir: "+paginas+" pagina/s");
          //Relleno los gráficos
          Book book = new Book();
          book.append(this,format,paginas);

          printerJob.setPageable(book);
          boolean doPrint=printerJob.printDialog();
          if (doPrint)
          {
              try{ printerJob.print();}catch (Exception e){
                  logger.error("Excepcion al imprimir "+e.toString());
             }
          }
    }

    public String getNombreInspector(String idInspector)
    {
        // TODO: en la llamada de Operacionescontaminantes.java se llama a un servlet. ¿De dónde obtengo los datos?
/*           if (listaInspectores==null || idInspector==null) return null;
           for (Enumeration e=listaInspectores.elements();e.hasMoreElements();)
           {
               com.geopista.protocol.contaminantes.Inspector aux=(com.geopista.protocol.contaminantes.Inspector) e.nextElement();
               if (idInspector.equalsIgnoreCase(aux.getId()))
                   return (aux.getNombre()+" "+aux.getApellido1()+" "+aux.getApellido2());
           }
*/           return "";
    }



}
