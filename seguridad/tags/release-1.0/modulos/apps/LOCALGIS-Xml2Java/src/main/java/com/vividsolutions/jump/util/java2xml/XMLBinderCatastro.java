package com.vividsolutions.jump.util.java2xml;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.LangUtil;
import com.vividsolutions.jump.util.StringUtil;


public class XMLBinderCatastro {
    private HashMap classToCustomConverterMap = new HashMap();
    private String pattern;
    private String length;

    public String formatDoubleValue(Object value){

        Locale loc = new Locale("es_ES");

        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df = (DecimalFormat)nf;
        
        df.applyPattern(pattern);

        return df.format(value).replace(".", ",");
        
    }
    
    public String formatLengthValue(Object value){
    	
    	if (length!=null){
    		String result = value.toString();    		
    		result = completarConCeros(result,new Integer(length).intValue());   
    		length = null;
    		return result;
    	}
    	else
    		return value.toString();
        
        

    }

    public XMLBinderCatastro() {

    	length= null;

        classToCustomConverterMap.put(char.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new String(value);
                    }

                    public String toXML(Object object) {

                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(Character.class, new CustomConverter() {
                    public Object toJava(String value) {

                        return new String(value);
                    }

                    public String toXML(Object object) {

                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(Class.class, new CustomConverter() {
                    public Object toJava(String value) {
                        try {
                            return Class.forName(value);
                        } catch (ClassNotFoundException e) {
                            Assert.shouldNeverReachHere();

                            return null;
                        }
                    }

                    public String toXML(Object object) {
                        return ((Class) object).getName();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	return ((Class) object).getName();
                    }
                });

        classToCustomConverterMap.put(Color.class, new CustomConverter() {
                    public Object toJava(String value) {
                        java.util.List parameters = StringUtil.fromCommaDelimitedString(value);

                        return new Color(Integer.parseInt(
                                (String) parameters.get(0)),
                                Integer.parseInt((String) parameters.get(1)),
                                Integer.parseInt((String) parameters.get(2)),
                                Integer.parseInt((String) parameters.get(3)));
                    }

                    public String toXML(Object object) {
                        Color color = (Color) object;
                        ArrayList parameters = new ArrayList();
                        parameters.add(new Integer(color.getRed()));
                        parameters.add(new Integer(color.getGreen()));
                        parameters.add(new Integer(color.getBlue()));
                        parameters.add(new Integer(color.getAlpha()));

                        return StringUtil.toCommaDelimitedString(parameters);
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	 Color color = (Color) object;
                         ArrayList parameters = new ArrayList();
                         parameters.add(new Integer(color.getRed()));
                         parameters.add(new Integer(color.getGreen()));
                         parameters.add(new Integer(color.getBlue()));
                         parameters.add(new Integer(color.getAlpha()));

                         return StringUtil.toCommaDelimitedString(parameters);
                    }
                });

        classToCustomConverterMap.put(Font.class, new CustomConverter() {
                    public Object toJava(String value) {
                        java.util.List parameters = StringUtil.fromCommaDelimitedString(value);

                        return new Font((String) parameters.get(0),
                                Integer.parseInt((String) parameters.get(1)),
                                Integer.parseInt((String) parameters.get(2)));
                    }

                    public String toXML(Object object) {
                        Font font = (Font) object;
                        ArrayList parameters = new ArrayList();
                        parameters.add(font.getName());
                        parameters.add(new Integer(font.getStyle()));
                        parameters.add(new Integer(font.getSize()));

                        return StringUtil.toCommaDelimitedString(parameters);
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	  Font font = (Font) object;
                          ArrayList parameters = new ArrayList();
                          parameters.add(font.getName());
                          parameters.add(new Integer(font.getStyle()));
                          parameters.add(new Integer(font.getSize()));

                          return StringUtil.toCommaDelimitedString(parameters);
                    }
                });

        classToCustomConverterMap.put(double.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Double(value);
                    }

                    public String toXML(Object object) {
                        
                        return formatDoubleValue(new Double(object.toString()));
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	 return formatDoubleValue(new Double(object.toString()));
                    }
                });

        classToCustomConverterMap.put(Double.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Double(value);
                    }

                    public String toXML(Object object) {
                        return formatDoubleValue(new Double(object.toString()));
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(int.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Integer(value);
                    }

                    public String toXML(Object object) {
                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(Integer.class,new CustomConverter() {
                    public Object toJava(String value) {
                        return new Integer(value);
                    }

                    public String toXML(Object object) {
                    	return formatLengthValue(object);
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	return formatLengthValue(object);
                    }
                });

        classToCustomConverterMap.put(long.class,   new CustomConverter() {
                    public Object toJava(String value) {
                        return new Long(value);
                    }

                    public String toXML(Object object) {
                        return object.toString();
                    }
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(Long.class,  new CustomConverter() {
                    public Object toJava(String value) {
                        return new Long(value);
                    }

                    public String toXML(Object object) {
                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(String.class,  new CustomConverter() {
                    public Object toJava(String value) {
                        return value;
                    }

                    public String toXML(Object object) {
                    	return formatLengthValue(object.toString().toUpperCase());
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(boolean.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Boolean(value);
                    }

                    public String toXML(Object object) {
                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                    
                });

        classToCustomConverterMap.put(Boolean.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Boolean(value);
                    }

                    public String toXML(Object object) {
                        return object.toString();
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                        return object.toString();
                    }
                });

        classToCustomConverterMap.put(java.sql.Time.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Time(Long.parseLong(value));
                    }

                    public String toXML(Object object) {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String fechaFormat = formatter.format(object);
                        return fechaFormat;
                    }
                    
                    public String toXMLCaseSensitive(Object object) {

                    	 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                         String fechaFormat = formatter.format(object);
                         return fechaFormat;
                    }
                });

        classToCustomConverterMap.put(java.util.Date.class, new CustomConverter() {
                    public Object toJava(String value) {
                        return new Date(value);
                    }

                    public String toXML(Object object) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String fechaFormat = formatter.format(object);
                        return fechaFormat;
                    }
                    public String toXMLCaseSensitive(Object object) {

                    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                         String fechaFormat = formatter.format(object);
                         return fechaFormat;
                    }
                });

        classToCustomConverterMap.put(float.class,   new CustomConverter() {
                    public Object toJava(String value) {
                        return new Float(value);
                    }

                    public String toXML(Object object) {
                        return formatDoubleValue(new Float(object.toString()));
                    }
                    public String toXMLCaseSensitive(Object object) {

                    	return formatDoubleValue(new Float(object.toString()));
                    }
                });

        classToCustomConverterMap.put(Float.class,  new CustomConverter() {
                    public Object toJava(String value) {
                        return new Float(value);
                    }

                    public String toXML(Object object) {
                         return formatDoubleValue(new Float(object.toString()));
                       
                    }
                    public String toXMLCaseSensitive(Object object) {

                    	 return formatDoubleValue(new Float(object.toString()));
                    }
                });

    }

    private String specFilename(Class c) {
        return StringUtil.classNameWithoutPackageQualifiers(c.getName()) + ".java2xml";
    }

    protected java.util.List specElements(Class c) throws XMLBinderException, JDOMException, IOException {
        InputStream stream = specResourceStream(c);

        if (stream == null) {
            throw new XMLBinderException("Could not find java2xml file for " +
                    c.getName() + " or its interfaces or superclasses");
        }

        try {
            Element root = new SAXBuilder().build(stream).getRootElement();

            if (!root.getAttributes().isEmpty()) {
                throw new XMLBinderException("Root element of " +
                        specFilename(c) + " should not have attributes");
            }

            if (!root.getName().equals("root")) {
                throw new XMLBinderException("Root element of " +
                        specFilename(c) + " should be named 'root'");
            }

            return root.getChildren();
        } finally {
            stream.close();
        }
    }

    private InputStream specResourceStream(Class c) {
        for (Iterator i = LangUtil.classesAndInterfaces(c).iterator(); i.hasNext();) {
            Class type = (Class) i.next();
            Assert.isTrue(type.isAssignableFrom(c));

            InputStream stream = c.getResourceAsStream(specFilename(type));

            if (stream != null) {
                return stream;
            }
        }

        return null;
    }

    public void addCustomConverter(Class c, CustomConverter converter) {
        classToCustomConverterMap.put(c, converter);
    }

    protected void visit(java.util.List specElements, SpecVisitor visitor, Class c,Object object) throws Exception {
        
        for (Iterator i = specElements.iterator(); i.hasNext();) {
            Element specElement = (Element) i.next();
            Attribute xmlName = specElement.getAttribute("xml-name");

            if (xmlName == null) {
                throw new XMLBinderException(StringUtil.classNameWithoutPackageQualifiers(
                        c.getName()) + ": Expected 'xml-name' attribute in <" + specElement.getName() + "> but found none");
            }

            Attribute javaName = specElement.getAttribute("java-name");
            Attribute attJavaName = specElement.getAttribute("java-name");
            Attribute javaCondition = specElement.getAttribute("java-condition");
            Attribute format = specElement.getAttribute("format");
            Attribute length = specElement.getAttribute("length");
            Attribute nullValue = specElement.getAttribute("null-value");
            Attribute mandatoryAtt = specElement.getAttribute("mandatory");
            Attribute defaultValueAtt = specElement.getAttribute("default-value");
            // se utiliza para identificar aquellos valores (String( que no tienen que convertise a upper
            Attribute caseSensitiveAtt = specElement.getAttribute("case-sensitive");
            
            this.length = null;

            
            if(mandatoryAtt!=null && defaultValueAtt!=null){
            	if(mandatoryAtt.getValue().equals("true")){
            		String defaultValue=defaultValueAtt.getValue();
            		System.out.println("El valor por defecto que tendremos que escribir será: "+defaultValue);
            		//obtenemos el valor del atributo java-name
                    String attJavaNameValue=attJavaName.getValue();
                    System.out.println("El campo al que tenemos que acceder es: "+attJavaName);
                    
                    //rellenamos un vector con las cadenas entre puntos que tiene el valor del java-name
                    StringTokenizer cadena= new StringTokenizer(attJavaNameValue,".");
                    ArrayList tokens=new ArrayList();
                    while(cadena.hasMoreTokens()){
                    	String token=cadena.nextToken();
                    	System.out.println("Token: "+token);
                    	tokens.add(token);
                    }//fin del while      
                    
                    Object value = null;
                    int numTokens=tokens.size();
                    
                    if(numTokens==1){
                      Method getterMethod=getter(object.getClass(), attJavaName.getValue());
                  	  value= getterMethod.invoke(object, new Object[] {  });
                  	  System.out.println("El valor del campo del objeto es: "+value);
                  	  
                  	  if(value==null){//tengo que escribir el default-value en el fichero de salida así que se lo asigno al campo del objeto
                  		Class type=getterMethod.getReturnType();
                  		this.callSetterMethod(object, attJavaName.getValue(), defaultValue, type.getName());
                  	  }
                  	  
                    }//fin if
                   
                    else{
                    	int numTokensLeft=numTokens;
                    	Object currentObject=object;
                    	
                    	 for(int j=0;j<numTokens;j++){
                    		 if(numTokensLeft>1){ //tenemos otro objeto dentro
                    			if(currentObject != null){
                    				 Method m=getter(currentObject.getClass(), (String) tokens.get(j));
                        			 currentObject=m.invoke(currentObject, new Object[] {  });
                    			 }                			 
                    		 }
                    		 else{ //ya no tenemos más objetos dentro
                    			 if (currentObject != null){
                    				 Method getterMethod=getter(currentObject.getClass(), (String) tokens.get(j));
                    				 value=getterMethod.invoke(currentObject, new Object[] {  });
                    				 System.out.println("El valor del campo del objeto es: "+value);
                                 	  
                                 	  if(value==null){//tengo que escribir el default-value así que se lo asigno
                                 		 Class type=getterMethod.getReturnType();
                                 		 this.callSetterMethod(currentObject, (String) tokens.get(j), defaultValue, type.getName());
                                 	  }
                                  }
                    		 }                		 
                    		 numTokensLeft--;
                         }//fin for
                    }//fin else      
            	}//fin if
            }//fin if default-value 
            
            
            if(nullValue != null){
                String valueLikeNull = nullValue.getValue();
                //obtenemos el valor del atributo java-name
                String attJavaNameValue=attJavaName.getValue();
                System.out.print("java name att value: "+attJavaName);
                
                //rellenamos un vector con las cadenas entre puntos que tiene el valor del atributo
                StringTokenizer cadena= new StringTokenizer(attJavaNameValue,".");
                ArrayList tokens=new ArrayList();
                while(cadena.hasMoreTokens()){
                	String token=cadena.nextToken();
                	System.out.println("Token: "+token);
                	tokens.add(token);
                }//fin del while      
                
                Object value = null;
                int numTokens=tokens.size();
                
                if(numTokens==1)
                	  value= getter(object.getClass(), attJavaName.getValue()).invoke(object, new Object[] {  });
             
               
                else{
                	int numTokensLeft=numTokens;
                	Object currentObject=object;
                	
                	 for(int j=0;j<numTokens;j++){
                		 if(numTokensLeft>1){ //tenemos otro objeto dentro
                			if(currentObject != null){
                				 Method m=getter(currentObject.getClass(), (String) tokens.get(j));
                    			 currentObject=m.invoke(currentObject, new Object[] {  });
                			 }                			 
                		 }
                		 else{ //ya no tenemos más objetos dentro
                			 if (currentObject != null){
                				 value= getter(currentObject.getClass(), (String) tokens.get(j)).invoke(currentObject, new Object[] {  });
                				 System.out.println("value: "+value);
                			 }
                		 }                		 
                		 numTokensLeft--;
                     }//fin for
                }//fin else             
                
                
                if(value!=null)
                	if(value.toString().equals(valueLikeNull))
                    continue;
            }//fin if null-value
            
            
            
            ArrayList method= null;
            if(javaName!=null) {
                String aux = javaName.getValue();

                int p = aux.indexOf(".");
                if(p!=-1) {
                    method = new ArrayList();
                    javaName.setValue(aux.substring(0,p));
                    aux = aux.substring(p+1,aux.length());
                    p = aux.indexOf(".");
                    while(p!=-1) {
                        method.add(aux.substring(0,p));
                        aux = aux.substring(p+1,aux.length());
                        p = aux.indexOf(".");
                    }
                    method.add(aux.substring(0,aux.length()));
                }
            }

            if(format != null){
                pattern = format.getValue();
            }
            else{
                pattern = "##0.00;##0.00";
            }
            
            if(length != null){
                this.length = length.getValue();
            }
            
            if(javaCondition!=null) {
                String aux = javaCondition.getValue();

                int p = aux.indexOf(".");
                if(p!=-1) {
                    method = new ArrayList();
                    javaCondition.setValue(aux.substring(0,p));
                    aux = aux.substring(p+1,aux.length());
                    p = aux.indexOf(".");
                    while(p!=-1){
                        method.add(aux.substring(0,p));
                        aux = aux.substring(p+1,aux.length());
                        p = aux.indexOf(".");
                    }
                    method.add(aux.substring(0,aux.length()));
                }
            }
            //javaName is null if tag does nothing other than add a level to the
            //hierarchy [Jon Aquino]
            if (specElement.getName().equals("element")) {
            	boolean caseSensitive = false;
            	if(caseSensitiveAtt!=null && caseSensitiveAtt.getValue().equals("true")){
            		caseSensitive = true;
            	}
                visitor.tagSpecFound(xmlName.getValue(),
                        (javaName != null) ? javaName.getValue() : null, (javaCondition != null) ? javaCondition.getValue() : null, method,
                        specElement.getChildren(),caseSensitive);
            }

            if (specElement.getName().equals("attribute")) {
                visitor.attributeSpecFound(xmlName.getValue(), javaName.getValue());
            }
        }
    }//fin del método visit
    
    
    
    /**Llama al método setter del atributo de un objeto dado, para ello comprueba previamente el tipo de dicho atributo
     * @param object Llamaremos al método set de este objeto
     * @param att Nombre del atributo de la clase
     * @param value Valor a asignar al atributo
     * @param type Tipo del atributo
     * @throws XMLBinderException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws NumberFormatException 
     */
    private void callSetterMethod(Object object, String att, String value, String type) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, XMLBinderException{
    	if(type.equals("int")||type.equals("java.lang.Integer")){
    		System.out.println("Llamada a setter-->tipo int, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Integer(value)});
    	}
    	else if(type.equals("long")||type.equals("java.lang.Long")){
    		System.out.println("Llamada a setter-->tipo long, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Long(value)});
    	}
    	else if(type.equals("float")||type.equals("java.lang.Float")){
    		System.out.println("Llamada a setter-->tipo float, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Float(value)});
    	}
    	else if(type.equals("double")||type.equals("java.lang.Double")){
    		System.out.println("Llamada a setter-->tipo double, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Double(value)});
    	}
    	else if(type.equals("char")||type.equals("java.lang.Character")){
    		System.out.println("Llamada a setter-->tipo char, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Character(value.toCharArray()[0])});
    	}
    	else if(type.equals("boolean")||type.equals("java.lang.Boolean")){
    		System.out.println("Llamada a setter-->tipo boolean, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {new Boolean(value)});
    	}
    	else if(type.equals("java.lang.String")){
    		System.out.println("Llamada a setter-->tipo String, att: "+att);
    		setter(object.getClass(), att).invoke(object, new Object[] {value});
    	}
    }//fin del método
    
    
    
    
    private Method getter(Class fieldClass, String field) throws XMLBinderException {
        Method[] methods = fieldClass.getMethods();

        //Exact match first [Jon Aquino]
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().toUpperCase().equals("GET" +
                    field.toUpperCase())) {
                continue;
            }

            if (methods[i].getParameterTypes().length != 0) {
                continue;
            }

            return methods[i];
        }

        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().toUpperCase().startsWith("GET" +field.toUpperCase()) &&
                    !methods[i].getName().toUpperCase().startsWith("IS" + field.toUpperCase())) {
                continue;
            }

            if (methods[i].getParameterTypes().length != 0) {
                continue;
            }

            return methods[i];
        }

        throw new XMLBinderException("Could not find getter named like '" +
                field + "' " + fieldClass);
    }
    
    
    
    

    public Object toJava(String text, Class c) {
        return (!text.equals("null")) ? ((CustomConverter) classToCustomConverterMap.get(customConvertableClass(c))).toJava(text) : null;
    }

    protected boolean specifyingTypeExplicitly(Class c) throws XMLBinderException {
        //The int and double classes are abstract. Filter them out. [Jon Aquino]

        if (hasCustomConverter(c)) {
            return false;
        }

        //In the handling of Maps, c may be the Object class. [Jon Aquino]
        return (c == Object.class) || Modifier.isAbstract(c.getModifiers()) || c.isInterface();
    }

    protected Class fieldClass(Method setter) {
        Assert.isTrue(setter.getParameterTypes().length == 1);

        return setter.getParameterTypes()[0];
    }

    public Method setter(Class c, String field) throws XMLBinderException {
        Method[] methods = c.getMethods();

        //Exact match first [Jon Aquino]
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().toUpperCase().equals("SET" + field.toUpperCase()) &&
                    !methods[i].getName().toUpperCase().equals("ADD" + field.toUpperCase())) {
                continue;
            }

            if (methods[i].getParameterTypes().length != 1) {
                continue;
            }

            return methods[i];
        }

        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().toUpperCase().startsWith("SET" + field.toUpperCase()) &&
                    !methods[i].getName().toUpperCase().startsWith("ADD" + field.toUpperCase())) {
                continue;
            }

            if (methods[i].getParameterTypes().length != 1) {
                continue;
            }

            return methods[i];
        }

        throw new XMLBinderException("Could not find setter named like '" + field + "' in class " + c);
    }

    protected String toXML(Object object) {
        return ((CustomConverter) classToCustomConverterMap.get(customConvertableClass(object.getClass()))).toXML(object);
    }
    protected String toXMLCaseSensitive(Object object) {
        return ((CustomConverter) classToCustomConverterMap.get(customConvertableClass(object.getClass()))).toXMLCaseSensitive(object);
    }

    protected boolean hasCustomConverter(Class fieldClass) {
        return customConvertableClass(fieldClass) != null;
    }

    /**
     * @param c class to convert
     * @return null if c doesn't have a custom converter
     */
    private Class customConvertableClass(Class c) {
        //Use #isAssignableFrom rather than #contains because some classes
        //may be interfaces. [Jon Aquino]
        for (Iterator i = classToCustomConverterMap.keySet().iterator(); i.hasNext();) {
            Class customConvertableClass = (Class) i.next();

            if (customConvertableClass.isAssignableFrom(c)) {
                return customConvertableClass;
            }
        }

        return null;
    }

    protected interface SpecVisitor {
        public void tagSpecFound(String xmlName, String javaName, String javaCondition, ArrayList method,
            java.util.List specChildElements, boolean caseSensitive) throws Exception;

        public void attributeSpecFound(String xmlName, String javaName)
            throws Exception;
    }

    /**
     * Sometimes you need to use a CustomConverter rather than a .java2xml
     * file i.e. when the class is from a third party (e.g. a Swing class) and you
     * can't add a .java2xml file to the jar.
     */
    public interface CustomConverter {
        public Object toJava(String value);

        public String toXML(Object object);
        
        public String toXMLCaseSensitive(Object object);
    }

    public static class XMLBinderException extends Exception {
        public XMLBinderException(String message) {
            super(message);
        }
    }
    
    public static String completarConCeros(String cadena, int longitud){

		while (cadena.length()< longitud){
			cadena = '0' + cadena;
		}
		return cadena;
	}

}
