/**
 * XML2JavaCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.util.java2xml;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.StringUtil;

public class XML2JavaCatastro extends XMLBinderCatastro{
    private ArrayList listeners = new ArrayList();

    public XML2JavaCatastro() {}

    public Object read(String xml, Class c) throws Exception {
        StringReader reader = new StringReader(xml);
        try {
            return read(reader, c);
        } finally {
            reader.close();
        }
    }

    public Object read(Reader reader, Class c) throws Exception {
        return read(new SAXBuilder().build(reader).getRootElement(), c);
    }

    private void read(final Element tag, final Object object, List specElements) throws Exception {
        Assert.isTrue(tag != null);
        visit(specElements, new XMLBinderCatastro.SpecVisitor() {
            private void fillerTagSpecFound(String xmlName, List specChildElements)
                throws Exception {
                if (tag.getChildren(xmlName).size() != 1) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected 1 <"
                            + xmlName
                            + "> tag but found "
                            + tag.getChildren(xmlName).size());
                }

                read(tag.getChild(xmlName), object, specChildElements);
            }

            private void normalTagSpecFound(
                String xmlName,
                String javaName,
                List specChildElements)
                throws Exception {
                setValuesFromTags(
                    object,
                    setter(object.getClass(), javaName),
                    tag.getChildren(xmlName));
                //The parent may specify additional tags for itself in the children. [Jon Aquino]
                for (Iterator i = tag.getChildren(xmlName).iterator(); i.hasNext();) {
                    Element childTag = (Element) i.next();
                    read(childTag, object, specChildElements);
                }
            }

            public void tagSpecFound(
                String xmlName,
                String javaName, String javaCondition,String javaCondition1, ArrayList method,
                List specChildElements, boolean caseSensitive)
                throws Exception {
                if (javaName == null) {
                    fillerTagSpecFound(xmlName, specChildElements);
                } else {
                    normalTagSpecFound(xmlName, javaName, specChildElements);
                }
            }

            public void attributeSpecFound(String xmlName, String javaName)
                throws Exception {
                if (tag.getAttribute(xmlName) == null) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected '"
                            + xmlName
                            + "' attribute but found none. Tag = "
                            + tag.getName()
                            + "; Attributes = "
                            + StringUtil.toCommaDelimitedString(tag.getAttributes()));
                }

                Method setter = setter(object.getClass(), javaName);
                setValue(
                    object,
                    setter,
                    toJava(
                        tag.getAttribute(xmlName).getValue(),
                        setter.getParameterTypes()[0]));
            }
        }, object.getClass(),object);
    }

    private Object read(Element tag, Class c) throws Exception {
        if (tag.getAttribute("null") != null
            && tag.getAttributeValue("null").equals("true")) {
            return null;
        }
        if (specifyingTypeExplicitly(c)) {
            if (tag.getAttribute("class") == null) {
                throw new XMLBinderCatastro.XMLBinderException(
                    "Expected <"
                        + tag.getName()
                        + "> to have 'class' attribute but found none");
            }
            return read(tag, Class.forName(tag.getAttributeValue("class")));
        }
        fireCreatingObject(c);
        if (hasCustomConverter(c)) {
            return toJava(tag.getTextTrim(), c);
        }

        Object object = c.newInstance();
        if (object instanceof Map) {
            for (Iterator i = tag.getChildren().iterator(); i.hasNext();) {
                Element mappingTag = (Element) i.next();
                if (!mappingTag.getName().equals("mapping")) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected <"
                            + tag.getName()
                            + "> to have <mapping> tag but found none");
                }
                if (mappingTag.getChildren().size() != 2) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected <"
                            + tag.getName()
                            + "> to have 2 tags under <mapping> but found "
                            + mappingTag.getChildren().size());
                }
                if (mappingTag.getChildren("key").size() != 1) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected <"
                            + tag.getName()
                            + "> to have 1 <key> tag under <mapping> but found "
                            + mappingTag.getChildren("key").size());
                }
                if (mappingTag.getChildren("value").size() != 1) {
                    throw new XMLBinderCatastro.XMLBinderException(
                        "Expected <"
                            + tag.getName()
                            + "> to have 1 <value> tag under <mapping> but found "
                            + mappingTag.getChildren("key").size());
                }
                ((Map) object).put(
                    read(mappingTag.getChild("key"), Object.class),
                    read(mappingTag.getChild("value"), Object.class));
            }
        } else {
            read(tag, object, specElements(object.getClass()));
        }

        return object;
    }

    private void fireCreatingObject(Class c) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            Listener l = (Listener) i.next();
            l.creatingObject(c);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void setValuesFromTags(Object object, Method setter, Collection tags)
        throws Exception {
        for (Iterator i = tags.iterator(); i.hasNext();) {
            Element tag = (Element) i.next();
            setValueFromTag(object, setter, tag);
        }
    }

    private void setValueFromTag(Object object, Method setter, Element tag)
        throws Exception {
        setValue(object, setter, read(tag, fieldClass(setter)));
    }

    private void setValue(Object object, Method setter, Object value)
        throws IllegalAccessException, InvocationTargetException {
        //If you get an InvocationTargetException, check the bottom of the stack
        //trace -- you should see the stack trace for the underlying exception.
        //[Jon Aquino]
        setter.invoke(object, new Object[] { value });
    }

    public static interface Listener {
        public void creatingObject(Class c);
    }

}
