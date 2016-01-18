/**
 * HexString.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
/* HexString - A small class to convert an array of bytes to a hexadecimal
 *             string.
 */

package es.satec.localgismobile.fw.net.communications;



/**
 * A static class used to transform an array of bytes into a hexadecimal string.
 * It only uses Little Endian notation.
 */

public abstract class HexString
{
    /**
     * Converts the array of bytes specified into a hexadecimal String.
     *
     * @param buf The array of bytes to convert
     * @return String object containing the converted string
     */

    public static String convert(byte [] buf, int length)
    {
        String T = "";

        for(int x = 0; x < length; x++)
        {
            int y = buf[x];
            if(y < 0) y += 256;
            String d = Integer.toHexString(y);
            if(d.length() == 1) T += "0";
            T += d;
        }
        return T;
    }
}
