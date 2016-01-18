package fr.michaelm.jump.query;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * QueryDialog
 * @author Michaël MICHAUD
 * @version 0.1.0 (4 Dec 2004)
 */ 
public class Condition  {
	QueryDialog query;
	Function ft;
	Operator op;
	Pattern pattern;   // only used for match & find functions
	PlugInContext context;
	boolean avanzado = false;

	/*public Condition(QueryDialog query, PlugInContext context) {
		this.query = query;
		this.ft=query.function;
		this.op=query.operator;
		if (op==Operator.MATC || op==Operator.FIND) {
			if (query.caseSensitive.getState())
				pattern = Pattern.compile((String)query.valueCB.getSelectedValue());
			else 
				pattern = Pattern.compile((String)query.valueCB.getSelectedValue(), Pattern.CASE_INSENSITIVE);
		}
		this.context = context;
	}*/
	
	public Condition(QueryDialog query, PlugInContext context, boolean avanzado) {
		this.query = query;
		this.avanzado = avanzado;
		if(avanzado){
			/*this.query.attribute = query.attributeAdvanced;
			this.query.value = query.valueAdvanced;
			this.query.attributeType = query.attributeTypeAdvanced;
			*/
			this.ft=query.functionAdvanced;
			this.op=query.operatorAdvanced;
			
		}
		else{
			/*this.query.attribute = query.attribute;
			this.query.value = query.value;
			this.query.attribute = query.attribute;
			this.query.attributeType = query.attributeType;
			*/
			this.ft=query.function;
			this.op=query.operator;
			
		}
		
		
		if (op==Operator.MATC || op==Operator.FIND) {
			if (query.caseSensitive.getState())
				pattern = Pattern.compile((String)query.valueAdvancedCB.getSelectedValue());
			else 
				pattern = Pattern.compile((String)query.valueAdvancedCB.getSelectedValue(), Pattern.CASE_INSENSITIVE);
		}
		this.context = context;
	}

	public String toString() {
		String att = query.attribute.trim().equals("")?"GEOMETRY":query.attribute;
		String func = ft.toString().trim().equals("")?"":"."+ft;
		String value = "";
		if(avanzado){
			att = query.attributeAdvanced.trim().equals("")?"GEOMETRY":query.attributeAdvanced;
			value = (String)query.valueAdvancedCB.getSelectedValue();

		}
		else{
			att = query.attribute.trim().equals("")?"GEOMETRY":query.attribute;
			value = (String)query.valueCB.getSelectedValue();
		}
		return "" + att + func + " " + op + " \"" + value + "\"";
		//query.valueCB.getSelectedValue() + "\"";
	}

	public boolean test(Feature feature) throws Exception {
		Object o = null;
		//System.out.print("Nature de l'attribut : ");
		char attributeTypeAux;
		String attributeAux;
		if(avanzado){
			attributeTypeAux = query.attributeTypeAdvanced ;
			attributeAux = query.attributeAdvanced;
		}
		else{
			attributeTypeAux = query.attributeType;
			attributeAux = query.attribute;
		}
		
		if(attributeTypeAux=='G') {
			//System.out.println(" géométrique");
			//System.out.println("Operator = " + op);
			o = feature.getGeometry();
			if(ft.type=='G') return test(gfunction((Geometry)o));
			else if(ft.type=='N') return test(nfunction((Geometry)o));
			else if(ft.type=='B') return test(bfunction((Geometry)o));
			else return false;
		}
		else {
			// System.out.println(" sémantique");
			// attributes which does not exist for this feature must have
			// been eliminated before the test procedure
			// (see QueryDialog#executeQuery())
			o = feature.getAttribute(attributeAux);
			if(o instanceof Boolean) return test(((Boolean)o).booleanValue());
			else if(o instanceof Integer) return test(((Integer)o).doubleValue());
			else if(o instanceof Long) return test(((Long)o).doubleValue());
			else if(o instanceof Double) return test(((Double)o).doubleValue());
			else if(o instanceof BigDecimal) return test(((BigDecimal)o).doubleValue());
			else if(o instanceof Date){
				if (ft.equals(ft.DNOF)){
					return testDate((Date)o);
				}else{
					return testParcialDate(dfunction((Date)o));
				}
			}
			else if(o instanceof String && ft.type=='S') return test(sfunction((String)o));
			else if(o instanceof String && ft.type=='N') return test(nfunction((String)o));
			else return false;
		}
	}

	private boolean test(boolean b) throws Exception {
		boolean value = false;
		if(avanzado){
			value=query.valueAdvancedCB.getSelectedIndex()==0?true:false;
		}
		else{
			value=query.valueCB.getSelectedIndex()==0?true:false;
		}
		//boolean value=query.valueCB.getSelectedIndex()==0?true:false;
		if (b==value && op==Operator.BEQ) return true;
		else return false;
	}


	private boolean testDate(Date d) throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date value = null;
		if(avanzado){
			value = (Date) formatter.parse((String)query.valueAdvancedCB.getSelectedValue());
		}
		else{
			value = (Date) formatter.parse((String)query.valueCB.getSelectedValue());
		}
		//Date value = (Date) formatter.parse((String)query.valueCB.getSelectedValue());

		if (op==Operator.DEQ && d.equals(value) ) return true;
		else if (op==Operator.DNE && !(d.equals(value)) )return true;
		else if (op==Operator.DAF && d.after(value)) return true;
		else if (op==Operator.DBF && d.before(value)) return true;
		else if (op==Operator.DAE && (d.after(value) || d.equals(value) )) return true;
		else if (op==Operator.DBE && (d.before(value) || d.equals(value) )) return true;
		else return false;
	}

	private boolean testParcialDate(long d) throws Exception{
		long longValue = -1;
		
		// catch date
		try{
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date value = null;
			if(avanzado){
				value = (Date) formatter.parse((String)query.valueAdvancedCB.getSelectedValue());
			}
			else{
				value = (Date) formatter.parse((String)query.valueCB.getSelectedValue());
			}
			//Date value = (Date) formatter.parse((String)query.valueCB.getSelectedValue());
			if (ft == Function.DAY) longValue = value.getDate();
			else if (ft == Function.MONTH) longValue = value.getMonth();
			else if (ft == Function.YEAR) longValue = value.getYear();
			else longValue = -1;
		}catch (Exception E){
			longValue = Integer.parseInt((String)query.valueCB.getSelectedValue());
		}
		
		if (op==Operator.DEQ && d==longValue) return true;
		else if (op==Operator.DNE && d!=longValue) return true;
		else if (op==Operator.DAF && d<longValue) return true;
		else if (op==Operator.DBE && d>longValue) return true;
		else if (op==Operator.DAE && d<=longValue) return true;
		else if (op==Operator.DBE && d>=longValue) return true;
		else return false;
		

	}

	private boolean test(double d) throws Exception {
		double value;
		if(avanzado){
			value = Double.parseDouble((String)query.valueAdvancedCB.getSelectedValue());
		}
		else{
			value = Double.parseDouble((String)query.valueCB.getSelectedValue());
		}
		//double value = Double.parseDouble((String)query.valueCB.getSelectedValue());
		if (op==Operator.EQ && d==value) return true;
		else if (op==Operator.NE && d!=value) return true;
		else if (op==Operator.LT && d<value) return true;
		else if (op==Operator.GT && d>value) return true;
		else if (op==Operator.LE && d<=value) return true;
		else if (op==Operator.GE && d>=value) return true;
		else return false;
	}

	private boolean test(String s) throws Exception {
		String value = "";
		if(avanzado){
			value = (String)query.valueAdvancedCB.getSelectedValue();
		}
		else{
			value = (String)query.valueCB.getSelectedValue();
		}
		//String value = (String)query.valueCB.getSelectedValue();
		if (query.caseSensitive.getState()) {
			if (op==Operator.EQUA) return s.equals(value);
			else if (op==Operator.DIFF) return !s.equals(value);
			else if (op==Operator.STAR) return s.startsWith(value);
			else if (op==Operator.ENDS) return s.endsWith(value);
			else if (op==Operator.MATC) return pattern.matcher(s).matches();
			else if (op==Operator.FIND) return pattern.matcher(s).find();
			else if (op==Operator.BEFO) return s.compareTo(value)<=0;
			else if (op==Operator.AFTE) return s.compareTo(value)>=0;
			else return false;
		}
		else {
			if (op==Operator.EQUA) return s.equalsIgnoreCase(value);
			else if (op==Operator.DIFF) return !s.equalsIgnoreCase(value);
			else if (op==Operator.STAR) return s.toUpperCase().startsWith(value.toUpperCase());
			else if (op==Operator.ENDS) return s.toUpperCase().endsWith(value.toUpperCase());
			else if (op==Operator.MATC) return pattern.matcher(s).matches();
			else if (op==Operator.FIND) return pattern.matcher(s).find();
			else if (op==Operator.BEFO) return s.compareToIgnoreCase(value)<=0;
			else if (op==Operator.AFTE) return s.compareToIgnoreCase(value)>=0;
			else return false;
		}
	}

	private boolean test(Geometry g) throws Exception {
		int pos;
		if(avanzado){
			pos = query.valueAdvancedCB.getSelectedIndex();
		}
		else{
			pos = query.valueCB.getSelectedIndex();
		}
		//int pos = query.valueCB.getSelectedIndex();
		// Target Geometry is the selection
		// System.out.println("position de la valeur sélectionnée : " + pos);
		if (pos==0) {
			for (Iterator it = query.selection.iterator() ; it.hasNext() ;) {
				Geometry p = (Geometry)it.next();
				if (op==Operator.INTER && g.intersects(p)) return true;
				else if (op==Operator.CONTA && g.contains(p)) return true;
				else if (op==Operator.WITHI && g.within(p)) return true;
				else if (op==Operator.WDIST && g.distance(p)<op.arg) return true;
				else if (op==Operator.TOUCH && g.touches(p)) return true;
				else if (op==Operator.CROSS && g.crosses(p)) return true;
				else if (op==Operator.OVERL && g.overlaps(p)) return true;
				else if (op==Operator.DISJO && g.disjoint(p)) return true;
				else;
			}
			return false;
		}
		else if (pos==1) {
			Layer[] ll = context.getLayerNamePanel().getSelectedLayers();
			for (int i = 0 ; i < ll.length ; i++) {
				FeatureCollection fc = ll[i].getFeatureCollectionWrapper();
				for (Iterator it = fc.iterator() ; it.hasNext() ;) {
					Geometry p = ((Feature)it.next()).getGeometry();
					if (op==Operator.INTER && g.intersects(p)) return true;
					else if (op==Operator.CONTA && g.contains(p)) return true;
					else if (op==Operator.WITHI && g.within(p)) return true;
					else if (op==Operator.WDIST && g.distance(p)<op.arg) return true;
					else if (op==Operator.TOUCH && g.touches(p)) return true;
					else if (op==Operator.CROSS && g.crosses(p)) return true;
					else if (op==Operator.OVERL && g.overlaps(p)) return true;
					else if (op==Operator.DISJO && g.disjoint(p)) return true;
					else;
				}
				return false;
			}
		}
		else if (pos==2) {
			List ll = context.getLayerManager().getLayers();
			for (int i = 0 ; i < ll.size() ; i++) {
				FeatureCollection fc = ((Layer)ll.get(i)).getFeatureCollectionWrapper();
				for (Iterator it = fc.iterator() ; it.hasNext() ;) {
					Geometry p = ((Feature)it.next()).getGeometry();
					if (op==Operator.INTER && g.intersects(p)) return true;
					else if (op==Operator.CONTA && g.contains(p)) return true;
					else if (op==Operator.WITHI && g.within(p)) return true;
					else if (op==Operator.WDIST && g.distance(p)<op.arg) return true;
					else if (op==Operator.TOUCH && g.touches(p)) return true;
					else if (op==Operator.CROSS && g.crosses(p)) return true;
					else if (op==Operator.OVERL && g.overlaps(p)) return true;
					else if (op==Operator.DISJO && g.disjoint(p)) return true;
					else;
				}
				return false;
			}
		}
		else {
			Layer layer = null;
			if(avanzado){
				layer = context.getLayerManager().getLayer((String)query.valueAdvancedCB.getSelectedValue());
			}
			else{
				layer = context.getLayerManager().getLayer((String)query.valueCB.getSelectedValue());
			}
			//Layer layer = context.getLayerManager().getLayer((String)query.valueCB.getSelectedValue());
			FeatureCollection fc = layer.getFeatureCollectionWrapper();
			for (Iterator it = fc.iterator() ; it.hasNext() ;) {
				Geometry p = ((Feature)it.next()).getGeometry();
				if (op==Operator.INTER && g.intersects(p)) return true;
				else if (op==Operator.CONTA && g.contains(p)) return true;
				else if (op==Operator.WITHI && g.within(p)) return true;
				else if (op==Operator.WDIST && g.distance(p)<op.arg) return true;
				else if (op==Operator.TOUCH && g.touches(p)) return true;
				else if (op==Operator.CROSS && g.crosses(p)) return true;
				else if (op==Operator.OVERL && g.overlaps(p)) return true;
				else if (op==Operator.DISJO && g.disjoint(p)) return true;
				else;
			}
			return false;
		}
		return false;
	}

	//**************************************************************************
	// apply functions
	//**************************************************************************

	private String sfunction(String s) {
		if (ft==Function.SNOF) return s;
		else if (ft==Function.TRIM) return s.trim();
		else if (ft==Function.SUBS && ft.args.length==1) {
			return s.substring(ft.args[0]);
		}
		else if (ft==Function.SUBS && ft.args.length==2) {
			return s.substring(ft.args[0], ft.args[1]);
		}
		else return s;
	}

	private double nfunction(String s) {
		if (ft==Function.LENG) return (double)s.length();
		else return 0.0;
	}

	private Geometry gfunction(Geometry g) {
		//System.out.println("geometric function");
		if (ft==Function.GNOF) return g;
		else if (ft==Function.CENT) return g.getInteriorPoint();
		else if (ft==Function.BUFF) return g.buffer(ft.args[0]);
		else return g;
	}

	private double nfunction(Geometry g) {
		//System.out.println("numeric function");
		if (ft==Function.LENG) return g.getLength();
		else if (ft==Function.AREA) return g.getArea();
		else if (ft==Function.NBPT) return (double)g.getNumPoints();
		else if (ft==Function.NBPA) {
			if (g.isEmpty()) return 0;
			else if (g instanceof GeometryCollection)
				return ((GeometryCollection)g).getNumGeometries();
			else return 1;
		}
		else return 0.0;
	}

	private boolean bfunction(Geometry g) {
		//System.out.println("boolean function");
		if (ft==Function.EMPT) return g.isEmpty();
		else if (ft==Function.SIMP) return g.isSimple();
		else if (ft==Function.VALI) return g.isValid();
		else return false;
	}

	private long dfunction (Date d){
		if (ft == Function.DAY) return d.getDate();
		else if (ft == Function.MONTH) return d.getMonth();
		else if (ft == Function.YEAR) return d.getYear() + 1900;
		else return -1;
	}
  
}
