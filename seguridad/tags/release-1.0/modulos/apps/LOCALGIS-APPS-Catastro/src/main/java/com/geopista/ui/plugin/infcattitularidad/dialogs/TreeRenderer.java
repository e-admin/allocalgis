package com.geopista.ui.plugin.infcattitularidad.dialogs;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.util.GeopistaFunctionUtils;



public class TreeRenderer extends DefaultTreeCellRenderer{
    
    public static final int TIPO_NOESPECIFICADO = 0;
    public static final int TIPO_EXPEDIENTE = 1;
    public static final int TIPO_PARCELA = 2;
    public static final int TIPO_CARGO = 3;
    public static final int TIPO_CULTIVO = 4;
    public static final int TIPO_NIF = 5;
    public static final int TIPO_LOCAL = 6;
    public static final int TIPO_SUELO = 7;
    public static final int TIPO_UC = 8;
    public static final int TIPO_DATOS = 9;

    
    public static final String ICONO_NOESPECIFICADO = "noespecificado.gif";
    public static final String ICONO_EXPEDIENTE = "expediente.gif";
    public static final String ICONO_PARCELA = "parcela.gif";
    public static final String ICONO_CARGO = "cargo.gif";
    public static final String ICONO_CULTIVO = "cultivo.gif";
    public static final String ICONO_NIF = "nif.gif";
    public static final String ICONO_LOCAL = "local.gif";
    public static final String ICONO_SUELO = "suelo.gif";
    public static final String ICONO_UC = "uc.gif";
    public static final String ICONO_DATOS = "datos.gif";
 

    
    
    public TreeRenderer() {}
    
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        
        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        
        
        if (row>=0)
        {
            StringBuffer sTitle = new StringBuffer();
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();      
            
            switch (getTypeNode(value))
            {
            case TIPO_EXPEDIENTE:
                setIcon(IconLoader.icon(ICONO_EXPEDIENTE));    
                //sTitle.append("Expediente:").append(" ").append(((Expediente)obj).getNumeroExpediente()); 
                break;
                
            case TIPO_PARCELA:
                setIcon(IconLoader.icon(ICONO_PARCELA)); 
                sTitle.append("PC1:").append(" ").append(((FincaCatastro)obj).getRefFinca().getRefCatastral1()).
                append(" ").append("PC2:").append(" ").append(((FincaCatastro)obj).getRefFinca().getRefCatastral2());; 
                break;
                
            case TIPO_CARGO:
                setIcon(IconLoader.icon(ICONO_CARGO)); 
                sTitle.append("Cargo:").append(" ").append(((BienInmuebleJuridico)obj).
                        getBienInmueble().getIdBienInmueble().getNumCargo()); 
                break;
                
            case TIPO_LOCAL:
                setIcon(IconLoader.icon(ICONO_LOCAL));                
               
                //Local común
                if (((ConstruccionCatastro)obj).getDatosEconomicos().getCodTipoValor()!=null
                        && !((ConstruccionCatastro)obj).getDatosEconomicos().getCodTipoValor().equals("")
                        && ((ConstruccionCatastro)obj).getDatosEconomicos().getCodModalidadReparto()!=null)
                {
                	
                    sTitle.append("Local :").append(" ").append(GeopistaFunctionUtils.completarConCeros(((ConstruccionCatastro)obj).getNumOrdenConstruccion(), 4));
                }
                else
                {
                    sTitle.append("Local - BLO:").append(((ConstruccionCatastro)obj).getDomicilioTributario().getBloque())
                    .append(" ").append("ESC:").append(((ConstruccionCatastro)obj).getDomicilioTributario().getEscalera())
                    .append(" ").append("PLA:").append(((ConstruccionCatastro)obj).getDomicilioTributario().getPlanta())
                    .append(" ").append("PUE:").append(((ConstruccionCatastro)obj).getDomicilioTributario().getPuerta());
                }
                
                break;
                
            case TIPO_CULTIVO:
                setIcon(IconLoader.icon(ICONO_CULTIVO)); 
                              
                //Cultivo común
                if (((Cultivo)obj).getIdCultivo().getNumOrden()==null
                        || ((Cultivo)obj).getIdCultivo().getNumOrden().equals(""))
                {
                    sTitle.append("Cultivo:").append(" ").append(((Cultivo)obj).getCodModalidadReparto());
                }
                else
                {
                    sTitle.append("Cultivo - ").append(((Cultivo)obj).getCodSubparcela())
                    .append(" ").append("CC:").append(((Cultivo)obj).getIdCultivo().getCalifCultivo());
                }
                
                break;
                
            case TIPO_NIF:
                setIcon(IconLoader.icon(ICONO_NIF)); 
                sTitle.append("NIF:").append(" ").append(((Persona)obj).getNif()); 
                break;
           
            case TIPO_SUELO:
                setIcon(IconLoader.icon(ICONO_SUELO)); 
                sTitle.append("Suelo:").append(" ").append(GeopistaFunctionUtils.completarConCeros(((SueloCatastro)obj).getNumOrden(),4)); 
                break;
                
            case TIPO_UC:
                setIcon(IconLoader.icon(ICONO_UC)); 
                sTitle.append("U.Constructiva:").append(" ").append(GeopistaFunctionUtils.completarConCeros(((UnidadConstructivaCatastro)obj).getCodUnidadConstructiva(),4)); 
                break;
                
            case TIPO_DATOS:
                setIcon(IconLoader.icon(ICONO_DATOS)); 
                sTitle.append(value.toString());
                break;
                
            case TIPO_NOESPECIFICADO:
            default:                   
                setIcon(IconLoader.icon(ICONO_NOESPECIFICADO));                
            }            
            
            if (sTitle!=null)
            {
                this.setText(sTitle.toString().replaceAll("null", " "));                
            }
            
        } else {
            setIcon(IconLoader.icon(ICONO_NOESPECIFICADO));
        }
                
        return this;
    }
    
    protected int getTypeNode(Object value) {
        
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)value;        
        
        if (node.getUserObject() instanceof FincaCatastro)
            return TIPO_PARCELA;
        else if (node.getUserObject() instanceof BienInmuebleJuridico)
            return TIPO_CARGO;
        else if (node.getUserObject() instanceof Cultivo)
            return TIPO_CULTIVO;
        else if (node.getUserObject() instanceof ConstruccionCatastro)
            return TIPO_LOCAL;
        else if (node.getUserObject() instanceof Persona)
            return TIPO_NIF;
        else if (node.getUserObject() instanceof UnidadConstructivaCatastro)
            return TIPO_UC;
        else if (node.getUserObject() instanceof SueloCatastro)
            return TIPO_SUELO;       
        else if (node.getUserObject() instanceof String)
            return TIPO_DATOS;
        else if (node.getUserObject() instanceof Expediente)
            return TIPO_EXPEDIENTE;
        else
            return TIPO_NOESPECIFICADO;
       
    }
}

