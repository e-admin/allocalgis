/*----------------    FILE HEADER  ------------------------------------------
 
This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de
 
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.
 
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
 
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
Contact:
 
Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de
 
Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de
 
 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs.filterencoding;

import java.util.Stack;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Types;

import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.services.wfs.filterencoding.*;

/**
 * Abstract superclass for the generation of SQL-fragments from a Filter object.
 * FIXME:
 *   - featureFilter2SQL () lacks configurability (should be moved to the concrete implementation maybe)
 *   - function2SQL () lacks function name checks (might break the generated SQL)
 * @author Markus Schneider
 * @author Andreas Poth
 * @version 14.09.2002
 * @see Filter
 */
public abstract class AbstractSQLBuilder {
    
    protected FeatureType ft         = null;
    private Stack usedProperties    = null;
    private StringBuffer closedBr   = null;
    private Stack sqlFrag           = null;
    protected boolean not           = false;
    
    public AbstractSQLBuilder(FeatureType ft) {
        this.ft = ft;
        usedProperties = new Stack();
        sqlFrag = new Stack();
        closedBr = new StringBuffer();
    }        
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public String filter2SQL(Filter filter) throws Exception {
        
        if (filter instanceof FeatureFilter)
            return featureFilter2SQL((FeatureFilter) filter);
        
        // must be a ComplexFilter then
        return complexFilter2SQL((ComplexFilter) filter);
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public String featureFilter2SQL(FeatureFilter filter) {
        StringBuffer sb = new StringBuffer(500);
        if (filter.featureIds.size() == 0) return sb.toString();
        
        sb.append(" FROM ").append( ft.getMasterTable().getName() );
        sb.append(" WHERE ");
        for (int i = 0; i < filter.featureIds.size(); i++) {
            FeatureId fid = (FeatureId) filter.featureIds.get(i);
            String tmp = ft.getMasterTable().getIdField();
            sb.append( tmp + "=");
            if ( ft.getMasterTable().isIdFieldNumber() ) {
                sb.append(fid.getValue() );
            } else {
                sb.append( "'" + fid.getValue() + "' " );
            }
            if (i != filter.featureIds.size() -1) sb.append(" OR ");
        }
        return sb.toString();
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public String complexFilter2SQL(ComplexFilter filter) throws Exception {
        String s = operation2SQL(filter.getOperation()).toString();
        StringBuffer sb = new StringBuffer(1000);
        sb.append( " FROM ").append( ft.getMasterTable().getName());
        if ( s != null && s.length() > 2 ) {
            sb.append( " WHERE " ).append( s );
        }
        return sb.toString();
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer operation2SQL(Operation operation) throws Exception {
        StringBuffer sb = new StringBuffer(200);        
        switch (OperationDefines.getTypeById(operation.getOperatorId())) {
            case OperationDefines.TYPE_SPATIAL: {
                sb = spatialOperation2SQL((SpatialOperation) operation);
                break;
            }
            case OperationDefines.TYPE_COMPARISON: {
                sb = comparisonOperation2SQL((ComparisonOperation) operation);
                break;
            }
            case OperationDefines.TYPE_LOGICAL: {
                sb = logicalOperation2SQL((LogicalOperation) operation);
                break;
            }
            default: {
                break;
            }
        }
        return sb;
    }
    
    
    /**
     * Generates a SQL-fragment for the given object. As this depends very much
     * on the handling of geometry data by the concrete database in use, this method
     * is abstract and must be overwritten by any implementation.
     */
    public abstract StringBuffer spatialOperation2SQL(SpatialOperation operation)
                                                                    throws Exception;
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer comparisonOperation2SQL(ComparisonOperation operation)
                                                                throws Exception {
        StringBuffer sb = new StringBuffer(200);
        switch (operation.getOperatorId()) {
            case OperationDefines.PROPERTYISEQUALTO:
            case OperationDefines.PROPERTYISLESSTHAN:
            case OperationDefines.PROPERTYISGREATERTHAN:
            case OperationDefines.PROPERTYISLESSTHANOREQUALTO:
            case OperationDefines.PROPERTYISGREATERTHANOREQUALTO: {
                sb = propertyIsCOMPOperation2SQL((PropertyIsCOMPOperation) operation);
                break;
            }
            case OperationDefines.PROPERTYISLIKE: {
                sb = propertyIsLikeOperation2SQL((PropertyIsLikeOperation) operation);
                break;
            }
            case OperationDefines.PROPERTYISNULL: {
                sb = propertyIsNullOperation2SQL((PropertyIsNullOperation) operation);
                break;
            }
            case OperationDefines.PROPERTYISBETWEEN: {
                sb = propertyIsBetweenOperation2SQL((PropertyIsBetweenOperation) operation);
                break;
            }
        }
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer propertyIsCOMPOperation2SQL(PropertyIsCOMPOperation operation) 
                                                                    throws Exception {
        StringBuffer sb = new StringBuffer(200);
        sb.append(expression2SQL(operation.getFirstExpression()));
        switch (operation.getOperatorId()) {
            case OperationDefines.PROPERTYISEQUALTO: {
                if ( not ) {
                    sb.append(" <> ");
                } else {
                    sb.append(" = ");
                }
                break;
            }
            case OperationDefines.PROPERTYISLESSTHAN: {
                if ( not ) {
                    sb.append(" >= ");
                } else {
                    sb.append(" < ");
                }
                break;
            }
            case OperationDefines.PROPERTYISGREATERTHAN: {
                if ( not ) {
                    sb.append(" <= ");
                } else {
                    sb.append(" > ");
                }
                break;
            }
            case OperationDefines.PROPERTYISLESSTHANOREQUALTO: {
                if ( not ) {
                    sb.append(" > ");                    
                } else {
                    sb.append(" <= ");
                }
                break;
            }
            case OperationDefines.PROPERTYISGREATERTHANOREQUALTO: {
                if ( not ) {
                    sb.append(" < ");
                } else {
                    sb.append(" >= ");
                }
                break;
            }
        }
        sb.append(expression2SQL(operation.getSecondExpression()));
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     * Replacing and escape handling is based on a finite automaton with 2 states:
     * (escapeMode)
     *   - ' is appended as \', \ is appended as \\
     *   - every character (including the escapeChar) is simply appended
     *   - unset escapeMode
     * (escapeMode is false)
     *    - ' is appended as \', \ is appended as \\
     *   - escapeChar means: skip char, set escapeMode
     *   - wildCard means: append %
     *   - singleChar means: append ?
     */
    public StringBuffer propertyIsLikeOperation2SQL(PropertyIsLikeOperation operation) 
        throws Exception {

        StringBuffer sb = new StringBuffer(200);
        String s = operation.getLiteral().getValue();

        char escapeChar = operation.getEscapeChar();
        char wildCard   = operation.getWildCard();
        char singleChar = operation.getSingleChar();
        boolean escapeMode = false;
        int length = s.length();

        StringBuffer prop = propertyName2SQL( operation.getPropertyName() );
        //Buscamos el ultimo espacio
        String prop_s = prop.toString().trim();
        String campo="";
        if (prop_s.lastIndexOf(" ")>=0)
        {
            sb.append(prop_s.substring(0,prop_s.lastIndexOf(" ")));
            campo= " UPPER("+prop_s.substring(prop_s.lastIndexOf(" "))+") ";
        }
        else
            campo= " UPPER ("+prop+") " ;


        StringBuffer valor= new StringBuffer();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);

            if (escapeMode) {
                // ' must (even in escapeMode) be converted to \'
                if (c == '\'') valor.append("\'");
                // \ must (even in escapeMode) be converted to \\
                else if (c == '\\') valor.append("\\\\");
                else valor.append(c);
                escapeMode = false;
            }
            else {
                // escapeChar means: switch to escapeMode
                if (c == escapeChar) escapeMode = true;
                // wildCard must be converted to %
                else if (c == wildCard) valor.append('%');
                // singleChar must be converted to ?
                else if (c == singleChar) valor.append('?');
                // ' must be converted to \'
                else if (c == '\'') valor.append("$'$");
                // % must be converted to \'
                else if (c == '%') valor.append("$%$");
                // ? must be converted to \'
                //else if (c == '?') sb.append("$?$");
                // \ must (even in escapeMode) be converted to \\
                else if (c == '\\') valor.append("\\\\");
                else valor.append(c);
            }
        }
        //valor =new StringBuffer(valor.toString().replace(' ','%'));

        String[] remplazables = {"A","DE","LOS","EL","LA","EN"};
        ArrayList alValor=new ArrayList();
        alValor= replace(alValor, valor.toString().toUpperCase() ,remplazables);

        StringBuffer resultado=new StringBuffer();
        for (Iterator it = alValor.iterator();it.hasNext();)
        {
            String posibleValor=(String)it.next();
            if (resultado.toString().length()>0) resultado.append(" OR ");
            if ( not ) {
                      resultado.append(campo +" NOT LIKE  '");
                  } else {
                      resultado.append(campo +" LIKE '");
                  }

            resultado.append(posibleValor+"' ");
        }
        //resultado.append(") ");
        //sb.append(valor);
        //sb.append("') ").append( closedBr );
        sb.append(resultado.append(closedBr));
        System.out.println(sb.toString());
        closedBr = new StringBuffer();

        // de, el, la

        return sb;
    }

    private ArrayList replace( ArrayList posiblesValores,String valor, String[] remplazables)
    {
        posiblesValores.add(valor.replace(' ','%'));

        for (int i=0; i<remplazables.length;i++)
        {
                String remplazadoEs=replace(valor,remplazables[i],' ');
                if (remplazadoEs!=null)
                {
                    posiblesValores.add(remplazadoEs);
                    replace(posiblesValores,remplazadoEs,remplazables);
                }
                String remplazadoCo=replace(valor,remplazables[i],'%');
                if (remplazadoCo!=null)
                {
                    posiblesValores.add(remplazadoCo);
                    replace(posiblesValores,remplazadoCo,remplazables);
                }
        }
        return posiblesValores;
    }
    private String replace(String valor, String remplazable, char espacio)
    {
        String origen=valor.toUpperCase();
        remplazable=remplazable.toUpperCase();
        String remplazado=null;
        if (origen.indexOf(espacio+remplazable+espacio)>=0)
        {
            remplazado=origen.substring(0,origen.indexOf(espacio+remplazable+espacio))+"%"+
                       origen.substring(origen.indexOf(espacio+remplazable+espacio)+(espacio+remplazable+espacio).length());
            System.out.println("REMPLAZADO="+remplazado);
            origen=remplazado;
        }
        if (origen.indexOf(remplazable+espacio)==0)
        {
            remplazado="%"+origen.substring((remplazable+espacio).length());
            origen=remplazado;
        }
        if (origen.lastIndexOf(espacio+remplazable)>=0)
        {
            if (origen.lastIndexOf(espacio+remplazable)==origen.length()-(espacio+remplazable).length())
            {
                remplazado=origen.substring(0,origen.length()-(espacio+remplazable).length())+"%";
            }
        }
        return remplazado;
    }


    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer propertyIsNullOperation2SQL(PropertyIsNullOperation operation) 
                                                                throws Exception {
        StringBuffer sb = new StringBuffer(200);
        Expression expr = operation.getExpression();
        
        // expr can be PropertyName or Literal
        if (expr instanceof PropertyName)
            sb.append(propertyName2SQL((PropertyName) expr));
        else
            sb.append(literal2SQL((Literal) expr));
        
        sb.append(" = NULL");
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer propertyIsBetweenOperation2SQL(PropertyIsBetweenOperation operation) 
                                                                        throws Exception {
        StringBuffer sb = new StringBuffer(200);
        sb.append(expression2SQL(operation.getLowerBoundary())).append(" <= ").
        append(propertyName2SQL(operation.getPropertyName())).append(" AND ").
        append(propertyName2SQL(operation.getPropertyName())).append(" <= ").
        append(expression2SQL(operation.getUpperBoundary()));
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer logicalOperation2SQL(LogicalOperation operation) throws Exception {
        StringBuffer sb = new StringBuffer(500);
        int opId = operation.getOperatorId();
        
        // if a not operator is defined the AND and OR operator have to be inverted.
        // e.g. a > 2 AND b = 3  --> NOT --> a <= 2 OR b <> 3
        if ( not ) {
            switch ( opId ) {
                case OperationDefines.AND: opId = OperationDefines.OR; break;
                case OperationDefines.OR: opId = OperationDefines.AND; break;
            }
        }
        
        switch ( opId ) {
            case OperationDefines.AND: {
                for (int i = 0; i < operation.arguments.size(); i++) {
                    Operation argument = (Operation) operation.arguments.get(i);
                    sb.append("(").append(operation2SQL(argument)).append(")");
                    if (i != operation.arguments.size() - 1) sb.append(" AND ");
                }
                break;
            }
            case OperationDefines.OR: {
                for (int i = 0; i < operation.arguments.size(); i++) {
                    Operation argument = (Operation) operation.arguments.get(i);
                    sb.append("(").append(operation2SQL(argument)).append(")");
                    if (i != operation.arguments.size() - 1) sb.append(" OR ");
                }
                break;
            }
            case OperationDefines.NOT: {
                Operation argument = (Operation) operation.arguments.get(0);
                //sb.append("! (").append(operation2SQL(argument)).append(")");
                not = !not;
                sb.append( operation2SQL(argument) );                
                not = !not;
                break;
            }
        }
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer expression2SQL(Expression expr) throws Exception {
        StringBuffer sb = new StringBuffer(200);

        switch (expr.getExpressionId()) {
            case ExpressionDefines.EXPRESSION: {
                break;
            }
            case ExpressionDefines.PROPERTYNAME: {
                PropertyName propertyName = (PropertyName) expr;
                sb = propertyName2SQL(propertyName);
                break;
            }
            case ExpressionDefines.LITERAL: {
                Literal literal = (Literal) expr;
                sb = literal2SQL(literal);
                break;
            }
            case ExpressionDefines.FUNCTION: {
                Function function = (Function) expr;
                sb = function2SQL(function);
                break;
            }
            case ExpressionDefines.ADD:
            case ExpressionDefines.SUB:
            case ExpressionDefines.MUL:
            case ExpressionDefines.DIV: {
                ArithmeticExpression arithmeticExpression = (ArithmeticExpression) expr;
                sb = arithmeticExpression2SQL(arithmeticExpression);
                break;
            }
        }
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer propertyName2SQL(PropertyName propertyName) throws Exception {
        
        String s = propertyName.getValue();

        String[] fields = ft.getDatastoreField( s );
        if ( fields == null ) {
            throw new Exception ("Field: " + s + " is not known by the WFS!");
        }
        usedProperties.push( fields[0] );
        
        String tableName = getTableName( fields[0] );
        StringBuffer sb = null;
        if ( !tableName.equals( ft.getMasterTable().getName() ) &&
             !tableName.equals( ft.getMasterTable().getTargetName() ) ) {
            sb = getRelatedTableSQL(tableName, fields[0]);            
        } else {
            sb = new StringBuffer( " "+ fields[0] );
            closedBr.append("");
        }
        //return new StringBuffer( fields[0] );
        return sb;
    }
    
    private StringBuffer getRelatedTableSQL(String targetTable, String property)
    {
        closedBr.append(")");

        StringBuffer sb = new StringBuffer(800);  

        String refTable = ft.getReferencedBy( targetTable );
        TableDescription td = ft.getTableByName( refTable );
        // if table referenced by is the master table
        Reference[] ref = td.getReferences();
        int index = -1;
        for (int i = 0; i < ref.length; i++) {
            if (ref[i].getTargetTable().equals( targetTable ) ) {
                index = i;
                break; 
            }
        }
        // create a inner select statement to access a related table
        sqlFrag.push( td.getName() + "." +
                      ref[index].getTableField() +
                      " IN (SELECT " + targetTable + "." +
                      ref[index].getTargetField() + 
                      " FROM " + targetTable + " WHERE " ); 
        
        if ( refTable.equals( ft.getMasterTable().getName() ) ) {
            while ( !sqlFrag.empty() ) {
                sb.append( sqlFrag.pop() );
            }
            sb.append( property + " " );
        } else {
            // recursion if the table where the target table is referenced by 
            // isn't the master table
            sb.append( getRelatedTableSQL( refTable, property ) );
        }        
            
        return sb;
     }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer literal2SQL(Literal literal) {

        String property = (String)usedProperties.pop();
        int tp = ft.getDatastoreFieldType( property );
        StringBuffer sb = new StringBuffer();
        if ( tp == Types.BIT || tp == Types.CHAR || 
             tp == Types.LONGVARCHAR || tp == Types.OTHER || 
             tp == Types.VARCHAR ) {
            sb.append("'").append(literal.value).append("'");
        } 
       else 
        if ( tp == Types.DATE ) {
            sb.append( formatDate( literal.value ) );
        }
       else
        if ( tp == Types.TIME ) {
            sb.append( formatTime( literal.value ) );
        }        
       else
        if ( tp == Types.TIMESTAMP ) {
            sb.append( formatTimestamp( literal.value ) );
        }
       else {
             sb.append( literal.value );
        } 
        sb.append( closedBr );
        closedBr = new StringBuffer();
         
        return sb;
    }
    
    /**
    * abstract method that have to be implemented by extending classes to format
    * a database vendor specific date
    */ 
    public abstract String formatDate(String time);

   /**
    * abstract method that have to be implemented by extending classes to format
    * a database vendor specific time
    */ 
    public abstract String formatTime(String time);
    
    /**
    * abstract method that have to be implemented by extending classes to format
    * a database vendor specific timestamp
    */ 
    public abstract String formatTimestamp(String time);
 
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer function2SQL(Function function) throws Exception {
        StringBuffer sb = new StringBuffer(200);
        sb.append(function.name).append(" (");
        for (int i = 0; i < function.args.size(); i++) {
            Expression expr = (Expression) function.args.get(i);
            sb.append(expression2SQL(expr));
            if (i != function.args.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb;
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer arithmeticExpression2SQL(ArithmeticExpression expr) 
                                                                throws Exception {
        StringBuffer sb = new StringBuffer(200);
        sb.append("(").append(expression2SQL(expr.expr1));
        switch (expr.getExpressionId()) {
            case ExpressionDefines.ADD: {
                sb.append(" + ");
                break;
            }
            case ExpressionDefines.SUB: {
                sb.append(" - ");
                break;
            }
            case ExpressionDefines.MUL: {
                sb.append(" * ");
                break;
            }
            case ExpressionDefines.DIV: { 
                sb.append(" / ");
                break;
            }
        }
        sb.append(expression2SQL(expr.expr2)).append(")");
        return sb;
    }        
    
    /**
     * extracts the table name from a property. it is assumed that the property
     * name is constructed like this: table.propertyname or schema.table.propertyname.
     * if no table is specified with a property name, the name of the feature
     * types master table will be returned.
     */
    private String getTableName(String prop)
    {
        String tab = ft.getMasterTable().getName();
        
        int pos = prop.lastIndexOf('.');
        if ( pos > 0 ) {
            tab = prop.substring(0,pos);
        } 
        
		if ( tab != null ) {
			return tab.toUpperCase();
		} else {
			return null;
		}
    }
}
