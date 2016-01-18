/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * Dataset.java
 * 
 * Created on 8 luglio 2005, 17.49
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.JRSubreportParameter;
import it.businesslogic.ireport.SubDataset;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class Dataset {
    
    
    private String resetType="Report";
    private String resetGroup="";
    private String incrementType="None";
    private String incrementGroup="";
    private String incrementWhenExpression="";
    private String connectionExpression="";
    private String dataSourceExpression="";
    private String parametersMapExpression="";
    private boolean useConnection = false;
    private Vector subreportParameters =  new Vector();
    
    private SubDataset subDataset = null;
    
        
    /** Creates a new instance of Dataset */
    public Dataset() {
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }

    public String getResetGroup() {
        return resetGroup;
    }

    public void setResetGroup(String resetGroup) {
        this.resetGroup = resetGroup;
    }

    public String getIncrementType() {
        return incrementType;
    }

    public void setIncrementType(String incrementType) {
        this.incrementType = incrementType;
    }

    public String getIncrementGroup() {
        return incrementGroup;
    }

    public void setIncrementGroup(String incrementGroup) {
        this.incrementGroup = incrementGroup;
    }
    
    public Dataset cloneMe()
    {   
        Dataset ds = new Dataset();
        copyBaseDataset(ds);
        return ds;
    }
    
    public void copyBaseDataset(Dataset ds)
    {
        ds.setIncrementType( getIncrementType());
        ds.setIncrementGroup( getIncrementGroup());
        ds.setResetType( getResetType());
        ds.setResetGroup( getResetGroup());
        ds.setUseConnection( isUseConnection());
        ds.setSubDataset( this.getSubDataset());
        ds.setParametersMapExpression( getParametersMapExpression());
        ds.setConnectionExpression( getConnectionExpression());
        ds.setDataSourceExpression( getDataSourceExpression());
        for (int i=0; i< getSubreportParameters().size(); ++i)
        {
            ds.getSubreportParameters().add( ((JRSubreportParameter)getSubreportParameters().elementAt(i)).cloneMe() );
        }
    }
    
        public String getConnectionExpression() {
        return connectionExpression;
    }

    public void setConnectionExpression(String connectionExpression) {
        this.connectionExpression = connectionExpression;
    }

    public String getDataSourceExpression() {
        return dataSourceExpression;
    }

    public void setDataSourceExpression(String dataSourceExpression) {
        this.dataSourceExpression = dataSourceExpression;
    }

    public String getParametersMapExpression() {
        return parametersMapExpression;
    }

    public void setParametersMapExpression(String parametersMapExpression) {
        this.parametersMapExpression = parametersMapExpression;
    }

    public boolean isUseConnection() {
        return useConnection;
    }

    public void setUseConnection(boolean useConnection) {
        this.useConnection = useConnection;
    }

    public Vector getSubreportParameters() {
        return subreportParameters;
    }

    public void setSubreportParameters(Vector subreportParameters) {
        this.subreportParameters = subreportParameters;
    }

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
    }

    public String getIncrementWhenExpression() {
        return incrementWhenExpression;
    }

    public void setIncrementWhenExpression(String incrementWhenExpression) {
        this.incrementWhenExpression = incrementWhenExpression;
    }

}
