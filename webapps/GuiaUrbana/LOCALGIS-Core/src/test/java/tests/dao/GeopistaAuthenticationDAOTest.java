/**
 * GeopistaAuthenticationDAOTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.localgis.web.core.dao.GeopistaAuthenticationDAO;

public class GeopistaAuthenticationDAOTest extends BaseDAOTest {

    private GeopistaAuthenticationDAO geopistaAuthenticationDAO = (GeopistaAuthenticationDAO) daoManager.getDao(GeopistaAuthenticationDAO.class);

    public void testIsValidUser() throws Exception {
        //assertEquals(geopistaAuthenticationDAO.isValidUser(ConfigurationTests.USER_VALID, encrytGeopistaPassword(ConfigurationTests.PASSWORD_USER_VALID), ConfigurationTests.ROL_USER_VALID, ConfigurationTests.ID_ENTIDAD), true);
        //assertEquals(geopistaAuthenticationDAO.isValidUser(ConfigurationTests.USER_INVALID, encrytGeopistaPassword(ConfigurationTests.PASSWORD_USER_INVALID), ConfigurationTests.ROL_USER_VALID, ConfigurationTests.ID_ENTIDAD), false);
    }

    /**
     * Cifra la password siguiendo el cifrado de geopista
     * 
     * @param password
     *            Password a cifrar
     * @return La password cifrada
     * @throws Exception
     *             Si ocurre algún error
     */
    /*private String encrytGeopistaPassword(String password) throws Exception {
        Cipher ecipher;
        String semilla = "GEOPISTA";
        byte[] rawkey = semilla.getBytes("UTF8");
        DESKeySpec keyspec = new DESKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyfactory.generateSecret(keyspec);
        byte[] utf8 = password.getBytes("UTF8");
        ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enc = ecipher.doFinal(utf8);
        //return new sun.misc.BASE64Encoder().encode(enc);
        return new String(new Base64().encode(enc));
    }*/

}
