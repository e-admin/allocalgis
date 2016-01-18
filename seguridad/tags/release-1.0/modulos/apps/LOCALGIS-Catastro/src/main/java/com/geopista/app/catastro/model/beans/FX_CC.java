package com.geopista.app.catastro.model.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;


public class FX_CC implements Serializable
{
    private String ASC;
    private String DXF;
    
    public FX_CC()
    {
        
    }
    public FX_CC(String asc, String dxf)
    {
        this.ASC = asc;
        this.DXF = dxf;
    }
    public FX_CC(File fileAsc, File fileDxf)
    {        
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream in = new FileInputStream(fileDxf);
            
            byte[] bytes = new byte[2048];                                                
            int len;
            
            while (( len = in.read(bytes)) > 0)
            {
            	String append = new String(bytes);
            	if (len < 2048){
            		 append = append.substring(1, len);
            	}
            	
                sb.append(append);
            }
            
            //this.DXF=ImportarUtils.asciiToBase64(sb.toString());
            this.DXF=sb.toString();
            
            in.close();
            in = null;
            sb = null;
            sb = new StringBuffer();
            in = new FileInputStream(fileAsc);
          
            while (( len = in.read(bytes)) > 0)
            {
            	String append = new String(bytes);
            	if (len < 2048){
            		 append = append.substring(1, len);
            	}
                sb.append(append);
            }
            
            //this.ASC=ImportarUtils.asciiToBase64(sb.toString());
            this.ASC=sb.toString();
            in.close();
            in = null;
            
        } 
        catch (Exception e)
        {
            new FX_CC();
        }
    }
    
    /**
     * @return Returns the ASC.
     */
    public String getASC()
    {
        return ASC;
    }
    
    /**
     * @param asc The ASC to set.
     */
    public void setASC(String asc)
    {
        ASC = asc;
    }
    
    /**
     * @return Returns the DXF.
     */
    public String getDXF()
    {
        return DXF;
    }
    
    /**
     * @param dxf The DXF to set.
     */
    public void setDXF(String dxf)
    {
        DXF = dxf;
    }
    
    public String toXML ()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<fxcc><asc>").append(ASC)
        .append("</asc><dxf>").append(DXF).append("</dxf></fxcc>");
        
        return sb.toString();
    }
    
}
