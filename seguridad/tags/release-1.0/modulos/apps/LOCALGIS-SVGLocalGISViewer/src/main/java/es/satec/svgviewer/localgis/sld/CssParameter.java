package es.satec.svgviewer.localgis.sld;

public class CssParameter {

    private String name = null;
    private String value = null;

    /**
     * constructor initializing the class with the <CssParameter>
     */
    CssParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name attribute's value of the CssParameter.
     * <p>
     * 
     * @return the value of the name attribute of the CssParameter
     */
    String getName() {
        return name;
    }

    /**
     * Sets the name attribute's value of the CssParameter.
     * <p>
     * 
     * @param name
     *            the value of the name attribute of the CssParameter
     */
    void setName( String name ) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

}
