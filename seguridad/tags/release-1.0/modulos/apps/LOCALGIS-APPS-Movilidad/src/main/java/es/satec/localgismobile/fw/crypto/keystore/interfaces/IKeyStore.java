package es.satec.localgismobile.fw.crypto.keystore.interfaces;

import es.satec.localgismobile.fw.crypto.keystore.exceptions.KeyNotFoundException;
import es.satec.localgismobile.fw.crypto.keystore.exceptions.KeyStoreAccessException;

public interface IKeyStore {
	public byte[] loadKey() throws KeyStoreAccessException, KeyNotFoundException;
	public void saveKey(byte[] password) throws KeyStoreAccessException;

}
