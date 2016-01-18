package com.vividsolutions.jump.util.java2xml;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Clase que extiene java2xml para permitir que cuando un Element sea null no escriba el tag correspondiente.
 * */

public class Java2XMLCatastro extends XMLBinderCatastro{

    public Java2XMLCatastro(){ }

     public String write(Object object, String rootTagName) throws Exception {
        StringWriter writer = new StringWriter();

        try {
            write(object, rootTagName, writer);

            return writer.toString();
        } finally {
            writer.close();
        }
    }

    public void write(Object object, String rootTagName, Writer writer) throws Exception {
        Document document = new Document(new Element(rootTagName));
        write(object, document.getRootElement(), specElements(object.getClass()));

        
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        Format formato = xmlOutputter.getFormat();

        formato.setOmitDeclaration(true);
        
        xmlOutputter.setFormat(formato);
        xmlOutputter.output(document, writer);
    }

    protected void write(final Object object, final Element tag, List specElements) throws Exception {
        try {
            visit(specElements, new SpecVisitor() {
                public void tagSpecFound(String xmlName, String javaName, String javaCondition, ArrayList method,
                                         List specChildElements, boolean caseSensitive) throws Exception {

                    Collection childTags = new ArrayList();
                    boolean condicionTrue= true;

                    if(javaName != null){
                        boolean continua = true;
                        if(javaCondition!=null)
                            continua = evaluaCondicion(object, javaCondition);

                        if(continua){
                            if(method==null ||method.size()==0){
                                childTags.addAll(writeChildTags(tag, xmlName,
                                        getter(object.getClass(), javaName).invoke(object, new Object[] {  }),
                                        specifyingTypeExplicitly(fieldClass(setter(object.getClass(), javaName))), caseSensitive));
                            }
                            else {
                                //Obtencion del valor del elemento.
                                Object auxObject = object;
                                String atributoFinal = javaName;
                                if(getter(object.getClass(), javaName).invoke(object, new Object[] {  })!=null) {
                                    auxObject =getter(object.getClass(), javaName).invoke(object, new Object[] {  });
                                    atributoFinal= (String)method.get(0);
                                    for(int i = 1;i<method.size();i++){
                                        if(getter(auxObject.getClass(), atributoFinal).invoke(auxObject, new Object[] {  })!=null){
                                            auxObject = getter(auxObject.getClass(), atributoFinal).invoke(auxObject,new Object[] {  });
                                            atributoFinal = (String)method.get(i);
                                        }
                                    }
                                }
                                childTags.addAll(writeChildTags(tag, xmlName,
                                        getter(auxObject.getClass(), atributoFinal).invoke(auxObject, new Object[] {  }),
                                        specifyingTypeExplicitly(fieldClass(setter(auxObject.getClass(),atributoFinal))), caseSensitive));
                            }
                        }
                    }
                    else if(javaCondition!=null){
                        Object auxObject = object;
                        String atributoFinal = javaCondition;
                        if(method!=null&&method.size()>0&&getter(object.getClass(), javaCondition).invoke(object, new Object[] {  })!=null){
                            auxObject =getter(object.getClass(), javaCondition).invoke(object, new Object[] {  });
                            atributoFinal= (String)method.get(0);
                            for(int i = 1;i<method.size()-1;i++) {
                                if(getter(auxObject.getClass(), atributoFinal).invoke(auxObject, new Object[] {  })!=null){
                                    auxObject = getter(auxObject.getClass(), atributoFinal).invoke(auxObject, new Object[] {  });
                                    atributoFinal = (String)method.get(i);
                                }
                            }
                        }
                        condicionTrue = evaluaCondicion(auxObject, atributoFinal);
                        if(condicionTrue) {
                            Element childTag = new Element(xmlName);
                            tag.addContent(childTag);
                            childTags.add(childTag);
                        }
                    }
                    else {
                        Element childTag = new Element(xmlName);
                        tag.addContent(childTag);
                        childTags.add(childTag);
                    }

                    //The parent may specify additional tags for itself in the children. [Jon Aquino]
                    for (Iterator i = childTags.iterator(); i.hasNext();) {
                        Element childTag = (Element) i.next();
                        write(object, childTag, specChildElements);
                    }
                    if(javaName==null && condicionTrue) {
                        Element aBorrar =tag.getChild(xmlName);
                        if(aBorrar.getContentSize()==0) {
                            tag.removeChild(xmlName);
                        }
                    }
                }

                public void attributeSpecFound(String xmlName, String javaName) throws Exception {
                    writeAttribute(tag, xmlName,
                            getter(object.getClass(), javaName).invoke(object, new Object[] {  }));
                }
            }, object.getClass(),object);
        }
        catch (Exception e) {
            System.out.println("Java2XML: Exception writing " + object.getClass());
            throw e;
        }
    }

    protected void writeAttribute(Element tag, String name, Object value)
            throws XMLBinderCatastro.XMLBinderException {
        if (value == null) {
            throw new XMLBinderCatastro.XMLBinderException("Cannot store null value as " +
                    "attribute. Store as element instead. (" + name + ").");
        }

        tag.setAttribute(new Attribute(name, toXML(value)));
    }

    protected Collection writeChildTags(Element tag, String name, Object value, boolean specifyingType, boolean caseSensitive) throws Exception {
        ArrayList childTags = new ArrayList();

        if (value instanceof Collection) {
            //Might or might not need to specify type, depending on how
            //concrete the setter's parameter is. [Jon Aquino]
            for (Iterator i = ((Collection) value).iterator(); i.hasNext();) {
                Object item = i.next();
                Element hijo= writeChildTag(tag, name, item, specifyingType, caseSensitive);
                if(hijo!=null)
                    childTags.add(hijo);

            }
        }
        else{
            Element hijo = writeChildTag(tag, name, value, specifyingType, caseSensitive);
            if(hijo!=null)
                childTags.add(hijo);

        }

        return childTags;
    }

    protected Element writeChildTag(Element tag, String name, Object value, boolean specifyingType, boolean caseSensitive) throws Exception {
        Element childTag=null;

        if((value!=null &&(value instanceof String && ((String)value).equalsIgnoreCase("")))){
            value = null;
        }
        if(value!=null){
            childTag = new Element(name);


            if ((value != null) && specifyingType) {
                childTag.setAttribute(new Attribute("class",
                        value.getClass().getName()));
            }

            if (hasCustomConverter(value.getClass())) {
            	if(caseSensitive){
            		//cuando es el identificador de dialogo idd no necesitamos que se ponga en mayusculas
            		childTag.setText(toXMLCaseSensitive(value));
            	}
            	else{
            		childTag.setText(toXML(value));
            	}
            }
            else if (value instanceof Map) {
                for (Iterator i = ((Map) value).keySet().iterator(); i.hasNext();) {
                    Object key = i.next();
                    Element mappingTag = new Element("mapping");
                    childTag.addContent(mappingTag);
                    writeChildTag(mappingTag, "key", key, true, caseSensitive);
                    writeChildTag(mappingTag, "value", ((Map) value).get(key), true, caseSensitive);
                }
            }
            else {
                write(value, childTag, specElements(value.getClass()));
            }

            tag.addContent(childTag);

        }
        return childTag;
    }

    private boolean evaluaCondicion(Object objeto, String metodo) throws XMLBinderException {
        Method[] methods = objeto.getClass().getMethods();

        //Exact match first [Jon Aquino]
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().toUpperCase().equals( metodo.toUpperCase())) {
                continue;
            }
            try{
                Object result = methods[i].invoke(objeto,new Object[] {  });
                return ((Boolean)result).booleanValue();
            }
            catch(Exception e) {}
        }

        throw new XMLBinderException("Could not find getter named like '" + metodo + "' " + objeto.getClass());
    }

    protected Method getter(Class fieldClass, String field) throws XMLBinderException {
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
}
