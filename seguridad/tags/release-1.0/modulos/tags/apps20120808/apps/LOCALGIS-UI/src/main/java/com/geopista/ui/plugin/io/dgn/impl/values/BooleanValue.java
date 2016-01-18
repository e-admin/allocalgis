package com.geopista.ui.plugin.io.dgn.impl.values;



/**
 * Wrapper para booleanos
 *
 * @author Fernando González Cortés
 */
public class BooleanValue extends Value {
	private boolean value;

	/**
	 * Creates a new BooleanValue object.
	 *
	 * @param value Valor booleano que tendrá este objeto
	 */
	public BooleanValue(boolean value) {
		this.value = value;
	}

	/**
	 * Creates a new BooleanValue object.
	 */
	public BooleanValue() {
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param value DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IncompatibleTypesException DOCUMENT ME!
	 */
	public Value equals(BooleanValue value) throws IncompatibleTypesException {
		return ValueFactory.createValue(this.value == value.value);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param value DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IncompatibleTypesException DOCUMENT ME!
	 */
	public Value equals(Value value) throws IncompatibleTypesException {
		return value.equals(this);
	}

	/**
	 * Establece el valor de este objeto
	 *
	 * @param value
	 */
	public void setValue(boolean value) {
		this.value = value;
	}

	/**
	 * Obtiene el valor de este objeto
	 *
	 * @return
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "" + value;
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.instruction.BooleanValue)
	 */
	public Value and(BooleanValue value) throws IncompatibleTypesException {
		BooleanValue ret = new BooleanValue();
		ret.setValue(this.value && value.getValue());

		return ret;
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.instruction.BooleanValue)
	 */
	public Value or(BooleanValue value) throws IncompatibleTypesException {
		BooleanValue ret = new BooleanValue();
		ret.setValue(this.value || value.getValue());

		return ret;
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.instruction.Value)
	 */
	public Value and(Value v) throws IncompatibleTypesException {
		return v.and((BooleanValue) this);
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Operations#and(com.hardcode.gdbms.engine.instruction.Value)
	 */
	public Value or(Value v) throws IncompatibleTypesException {
		return v.or((BooleanValue) this);
	}
}
