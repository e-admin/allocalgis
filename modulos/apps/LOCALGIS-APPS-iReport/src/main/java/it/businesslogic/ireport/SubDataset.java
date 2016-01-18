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
 * SubDataset.java
 * 
 * Created on 28 settembre 2005, 1.39
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent;

import java.util.Vector;

import org.syntax.jedit.IReportKeywordLookup;


/**
 *
 * @author Administrator
 */
public class SubDataset {
    
        private String name = "";
    
        private IReportKeywordLookup keywordLookup = new IReportKeywordLookup();
    
        private String query = "";
        
        private Vector fields = new Vector();
        
        private Vector parameters = new Vector();
    
        private Vector variables = new Vector();
        
        private Vector groups = new Vector();
        
        private Vector sortFields = new Vector();
        
        private java.util.Vector JRproperties = new Vector();
        
        private String whenResourceMissingType = "Null";
    
        private String scriptletClass = "";
    
        private String ResourceBundleBaseName = "";
        
        private String filterExpression = "";
        
        /**
         * the type of the query
         * iReport understands "sql", "hql" and "xPath";
         */
        private String queryLanguage = "sql";
        
        
        
        /** Getter for property query.
         * @return Value of property query.
         *
         */
        public java.lang.String getQuery() {
            return query;
        }

        /** Setter for property query.
         * @param query New value of property query.
         *
         */
        public void setQuery(java.lang.String query) {
            if ((this.query == null) ? query == null : this.query.equals(query)) return;
            this.query = query;
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.QUERY, SubDatasetObjectChangedEvent.MODIFIED, query, query));
        }
        
        /**********************************************************/
        // FIELDS
        
        /** Getter for property fields.
         * @return Value of property fields.
         *
         */
        public java.util.Vector getFields() {
            return fields;
        }
    
        public void addField(JRField field) {
            this.fields.add(field);
            getKeywordLookup().addKeyword("$F{"  + field.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.FIELD, SubDatasetObjectChangedEvent.ADDED, field, field));
        }
        
        public void removeField(JRField field) {
            this.fields.remove(field);
            getKeywordLookup().removeKeyword("$F{" + field.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.FIELD, SubDatasetObjectChangedEvent.DELETED, field, field));
        }
        
       /** Setter for property fields.
         * @param fields New value of property fields.
         *
         */
        public void setFields(java.util.Vector fields) {
            this.fields = fields;
        }
        
        
         /** Getter for property parameters.
         * @return Value of property parameters.
         *
         */
        public java.util.Vector getParameters() {
            return parameters;
        }

        /** Setter for property parameters.
         * @param parameters New value of property parameters.
         *
         */
        public void setParameters(java.util.Vector parameters) {
            this.parameters = parameters;
        }

        public void addParameter(JRParameter parameter) {
            this.parameters.add(parameter);
            getKeywordLookup().addKeyword("$P{" + parameter.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.PARAMETER, SubDatasetObjectChangedEvent.ADDED, parameter, parameter));
        }

        public void removeParameter(JRParameter parameter) {
            if (parameter.isBuiltin()) return;
            this.parameters.remove(parameter);
            getKeywordLookup().removeKeyword("$P{" + parameter.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.PARAMETER, SubDatasetObjectChangedEvent.DELETED, parameter, parameter));
        }

        
        
            /** Getter for property variables.
         * @return Value of property variables.
         *
         */
        public java.util.Vector getVariables() {
            return variables;
        }

        public void addVariable(JRVariable variable) {
            this.variables.add(variable);
            getKeywordLookup().addKeyword("$V{" + variable.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.VARIABLE, SubDatasetObjectChangedEvent.ADDED, variable, variable));
        }
        
        public void removeVariable(JRVariable variable) {
            if (variable.isBuiltin()) return;
            this.variables.remove(variable);
            getKeywordLookup().removeKeyword("$V{" + variable.getName() + "}");
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.VARIABLE, SubDatasetObjectChangedEvent.DELETED, variable, variable));
        }

        /** Setter for property variables.
         * @param variables New value of property variables.
         *
         */
        public void setVariables(java.util.Vector variables) {
            this.variables = variables;
        }

         /** Getter for property groups.
         * @return Value of property groups.
         *
         */
        public java.util.Vector getGroups() {
            return groups;
        }

        /** Setter for property groups.
         * @param groups New value of property groups.
         *
         */
        public void setGroups(java.util.Vector groups) {
            this.groups = groups;
        }

        /**********************************************************/
        // KEYWORDS LOOKUP  
        public IReportKeywordLookup getKeywordLookup() {
            return keywordLookup;
        }

        public void setKeywordLookup(IReportKeywordLookup keywordLookup) {
            this.keywordLookup = keywordLookup;
        }
        
        
        public String getWhenResourceMissingType() {
            return whenResourceMissingType;
        }

        public void setWhenResourceMissingType(String whenResourceMissingType) {
            this.whenResourceMissingType = whenResourceMissingType;
        }
        
        /** Getter for property scriptletClass.
     * @return Value of property scriptletClass.
     *
     */
    public java.lang.String getScriptletClass() {
        return scriptletClass;
    }
    
    /** Setter for property scriptletClass.
     * @param scriptletClass New value of property scriptletClass.
     *
     */
    public void setScriptletClass(java.lang.String scriptletClass) {
        this.scriptletClass = scriptletClass;
    }
    
    public String getResourceBundleBaseName() {
        return ResourceBundleBaseName;
    }

    public void setResourceBundleBaseName(String ResourceBundleBaseName) {
        this.ResourceBundleBaseName = ResourceBundleBaseName;
    }

       /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    public String toString()
    {
        return getName();
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    /**
     * Registers SubDatasetObjectChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSubDatasetObjectChangedListener(it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener listener) {
        if (getListenerList() == null ) {
            setListenerList(new javax.swing.event.EventListenerList());
        }
        getListenerList().add (it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener.class, listener);
    }

    /**
     * Removes SubDatasetObjectChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSubDatasetObjectChangedListener(it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener listener) {
        getListenerList().remove (it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    public void fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent event) {
        if (getListenerList() == null) return;
        Object[] listeners = getListenerList().getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener)listeners[i+1]).subDatasetObjectChanged (event);
            }
        }
    }

    public SubDataset()
    {
        addVariable(new JRVariable("PAGE_NUMBER","java.lang.Integer", true ));
        addVariable(new JRVariable("COLUMN_NUMBER","java.lang.Integer", true));
        addVariable(new JRVariable("REPORT_COUNT","java.lang.Integer", true));
        addVariable(new JRVariable("PAGE_COUNT","java.lang.Integer", true));
        addVariable(new JRVariable("COLUMN_COUNT","java.lang.Integer", true));
        
        addParameter(new JRParameter("REPORT_PARAMETERS_MAP","java.util.Map",false,"",true));
        addParameter(new JRParameter("REPORT_CONNECTION","java.sql.Connection",false,"",true));
        addParameter(new JRParameter("REPORT_MAX_COUNT","java.lang.Integer",false,"",true));
        addParameter(new JRParameter("REPORT_DATA_SOURCE","net.sf.jasperreports.engine.JRDataSource",false,"",true));
        addParameter(new JRParameter("REPORT_SCRIPTLET","net.sf.jasperreports.engine.JRAbstractScriptlet",false,"",true));
        addParameter(new JRParameter("REPORT_LOCALE","java.util.Locale",false,"",true));
        addParameter(new JRParameter("REPORT_RESOURCE_BUNDLE","java.util.ResourceBundle",false,"",true));
        addParameter(new JRParameter("REPORT_TIME_ZONE","java.util.TimeZone",false,"",true));
        addParameter(new JRParameter("REPORT_VIRTUALIZER","net.sf.jasperreports.engine.JRVirtualizer",false,"",true));
        addParameter(new JRParameter("REPORT_CLASS_LOADER","java.lang.ClassLoader",false,"",true));
        addParameter(new JRParameter("REPORT_URL_HANDLER_FACTORY","java.net.URLStreamHandlerFactory",false,"",true));
        addParameter(new JRParameter("REPORT_FORMAT_FACTORY","net.sf.jasperreports.engine.util.FormatFactory",false,"",true));
        addParameter(new JRParameter("IS_IGNORE_PAGINATION","java.lang.Boolean",false,"",true));
        
        addParameter(new JRParameter("HIBERNATE_SESSION","org.hibernate.Session",false,"",true));
        addParameter(new JRParameter("XML_DATA_DOCUMENT","org.w3c.dom.Document",false,"",true));
        addParameter(new JRParameter("JPA_ENTITY_MANAGER","javax.persistence.EntityManager",false,"",true));
        addParameter(new JRParameter("JPA_QUERY_HINTS_MAP","java.util.Map",false,"",true));
        addParameter(new JRParameter("MONDRIAN_CONNECTION","mondrian.olap.Connection",false,"",true));
        
        //addParameter(new JRParameter("XMLA_URL","java.util.String",false,"",true));
        //addParameter(new JRParameter("XMLA_DATASOURCE","java.util.String",false,"",true));
        //addParameter(new JRParameter("XMLA_CATALOG","java.util.String",false,"",true));
    }
    
    
    public void addJRProperty(JRProperty property) {
        this.JRproperties.add(property);
    }
        
    public void removeJRProperty(JRProperty property) {
        this.JRproperties.remove(property);
    }    
    
    /** Getter for property JRproperties.
     * @return Value of property JRproperties.
     *
     */
    public java.util.Vector getJRproperties() {
        return JRproperties;
    }
    
    /** Setter for property JRproperties.
     * @param JRproperties New value of property JRproperties.
     *
     */
    public void setJRproperties(java.util.Vector JRproperties) {
        this.JRproperties = JRproperties;
    }

    public String getQueryLanguage() {
        return queryLanguage;
    }

    public void setQueryLanguage(String queryLanguage) {
        if ((this.queryLanguage == null) ? queryLanguage == null : this.queryLanguage.equals(queryLanguage)) return;
        this.queryLanguage = queryLanguage;
        this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.QUERY_LANGUAGE, SubDatasetObjectChangedEvent.MODIFIED, queryLanguage, queryLanguage));
    }

    
    protected String language = "java";

    public javax.swing.event.EventListenerList getListenerList() {
        return listenerList;
    }

    public void setListenerList(javax.swing.event.EventListenerList listenerList) {
        this.listenerList = listenerList;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public Vector getSortFields() {
        return sortFields;
    }

    public void setSortFields(Vector sortFields) {
        this.sortFields = sortFields;
    }
    
    public void addSortField(SortField sortField) {
            this.getSortFields().add(sortField);
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.SORTFIELD, SubDatasetObjectChangedEvent.ADDED, sortField, sortField));
        }

        public void removeSortField(SortField sortField) {
            this.getSortFields().remove(sortField);
            this.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( new SubDatasetObjectChangedEvent(this,SubDatasetObjectChangedEvent.SORTFIELD, SubDatasetObjectChangedEvent.DELETED, sortField, sortField));
        }
}
