package com.geopista.ui.plugin.io.dgn.impl.values;


import java.util.Date;


/**
 * Factoría abstracta de objetos value que dado un tipo básico, devuelve el
 * wrapper apropiado
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ValueFactory {
    /**
     * Crea un objeto de tipo Value a partir de un int
     *
     * @param n valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(int n) {
        IntValue ret = new IntValue();
        ret.setValue(n);

        return ret;
    }

    /**
     * Crea un objeto de tipo Value a partir de un long
     *
     * @param l valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(long l) {
        LongValue ret = new LongValue();
        ret.setValue(l);

        return ret;
    }

    /**
     * Crea un objeto de tipo Value a partir de un String
     *
     * @param s valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(String s) {
        StringValue ret = new StringValue();
        ret.setValue(s);

        return ret;
    }

    /**
     * Crea un objeto de tipo Value a partir de un float
     *
     * @param f valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(float f) {
        FloatValue ret = new FloatValue();
        ret.setValue(f);

        return ret;
    }

    /**
     * Crea un objeto de tipo Value a partir de un double
     *
     * @param d valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(double d) {
        DoubleValue ret = new DoubleValue();
        ret.setValue(d);

        return ret;
    }

    /**
     * Crea un objeto de tipo Date a partir de un Date
     *
     * @param d valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(Date d) {
        DateValue ret = new DateValue();
        ret.setValue(d);

        return ret;
    }

    /**
     * Crea un objeto de tipo Value a partir de un booleano
     *
     * @param b valor que se quiere representar
     *
     * @return objeto Value con el valor que se pasa como parámetro
     */
    public static Value createValue(boolean b) {
        BooleanValue ret = new BooleanValue();
        ret.setValue(b);

        return ret;
    }

    /**
     * Crea un Value a partir de un literal encontrado en una instrucción y su
     * tipo
     *
     * @param text Texto del valor
     * @param type Tipo del valor
     *
     * @return Objeto Value del tipo adecuado
     *
     * @throws SemanticException Si el tipo del literal no está soportado
     */
    public static Value createValue(String text, int type)
        throws SemanticException {
        switch (type) {
        case SQLEngineConstants.STRING_LITERAL:

            StringValue r1 = new StringValue();
            r1.setValue(text.substring(1, text.length()-1));

            return r1;

        case SQLEngineConstants.INTEGER_LITERAL:

            try {
                IntValue r2 = new IntValue();
                r2.setValue(Integer.parseInt(text));

                return r2;
            } catch (NumberFormatException e) {
                LongValue r2 = new LongValue();
                r2.setValue(Long.parseLong(text));

                return r2;
            }

        case SQLEngineConstants.FLOATING_POINT_LITERAL:

            try {
                FloatValue r2 = new FloatValue();
                r2.setValue(Float.parseFloat(text));

                return r2;
            } catch (NumberFormatException e) {
                DoubleValue r2 = new DoubleValue();
                r2.setValue(Double.parseDouble(text));

                return r2;
            }

        default:
            throw new SemanticException("Unexpected literal type: " + text +
                "->" + type);
        }
    }
    // Se usa para recuperar Value de un XMLEntity
    public static Value createValue(String text, String className) throws SemanticException
    {
    	if (className.equals("com.hardcode.gdbms.engine.values.BooleanValue"))
    		return createValue(Boolean.getBoolean(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.DateValue"))
    		return createValue(Date.parse(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.DoubleValue"))
    		return createValue(Double.parseDouble(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.FloatValue"))
    		return createValue(Float.parseFloat(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.IntValue"))
    		return createValue(Integer.parseInt(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.LongValue"))
    		return createValue(Long.parseLong(text));
    	if (className.equals("com.hardcode.gdbms.engine.values.StringValue"))
    		return createValue(text);
    // default:
        throw new SemanticException("Unexpected className in createValue (GDBMS) text: " + text +
            "-> className: " + className);

    }
    
    public static Value createNullValue(){
    	return new NullValue();
    }
    
}
