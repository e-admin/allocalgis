package com.geopista.security.dnie.beans;
public class KeyValue {
	String key;
	String value;
 
	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}
 
	public String getValue() { return value; }
	public String getKey() { return key; }
 
	@Override
	public String toString() { return key; }
 
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyValue) {
			KeyValue kv = (KeyValue) obj;
			return (kv.value.equals(this.value));
		}
		return false;
	}
 
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}
}