package com.geopista.ui.plugin.profiles3D.panels;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
/**
 * 
 * @author jvaca
 *
 */
public class ChartPanelTransferable implements Transferable
{
    DataFlavor[] df=null;
    ChartPanel pan=null;
    public ChartPanelTransferable(ChartPanel pan){
        this.pan=pan;
        df=new DataFlavor[]{
                new DataFlavor(ChartPanel.class, "ChartPanel"),
                DataFlavor.imageFlavor};
        
    }
    public DataFlavor[] getTransferDataFlavors(){
        return df;
    }
    
    
    public boolean isDataFlavorSupported(DataFlavor flavor){
        return (flavor.equals(df[0]))||(flavor.equals(df[1])) ;
    }
    
    
    public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException, IOException{
        if(flavor.equals(df[0])){
            
            return pan;
        }else{
            BufferedImage image = pan.getChart().createBufferedImage(800,500);
            return image;
        }
        
    }
    
    
    
}
