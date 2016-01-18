package org.deegree.model.filterencoding;

 
/**
 * 
 * @author rogerp
 *  Excepcion introducida para capturar el caso de que una propiedad no
 *  existe en la feature.
 */
public class FilterEvaluationNoPropertyException extends FilterEvaluationException {

	 public FilterEvaluationNoPropertyException (String msg) {
	        super (msg);
	    }
}
