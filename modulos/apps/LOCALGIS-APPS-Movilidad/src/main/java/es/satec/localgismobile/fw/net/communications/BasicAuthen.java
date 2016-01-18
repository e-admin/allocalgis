/**
 * BasicAuthen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications;

/**
 * Soporte para
 * HTTP Basic Authentication - RFC 2617.
 */

public class BasicAuthen
{
    /**
     * This takes a username and password and creates a header that can
     * be added directly to the request header table.
     *
     * @param user usuario
     * @param pass password
     * @return Authorization header
     */

    public String createHeader(String user, String pass)
    {
        String d = user + ":" + pass;
        String header = "Basic " + encode(d);
        return header;
    }

    /**
     * Codificador en Base64
     *
     * @param d String a codificar
     * @return String en Base64
     */

    private String encode(String d)
    {
        String c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                   "abcdefghijklmnopqrstuvwxyz" +
                   "0123456789+/";

        byte [] code = c.getBytes();
        byte [] s = d.getBytes();

        int x;
        int y = d.length() - (d.length() % 3);

        byte [] coded = new byte[4];
        String dest = "";

        for(x = 0; x < y; x += 3)
        {
            coded[3] = code[s[x + 2] % 64];
            coded[0] = code[s[x] >> 2];

            coded[1] = new Integer((s[x] % 4) << 4).byteValue();
            coded[1] += s[x + 1] >> 4;
            coded[1] = code[coded[1]];

            coded[2] = new Integer((s[x + 1] % 16) << 2).byteValue();
            coded[2] += s[x + 2] / 64;
            coded[2] = code[coded[2]];

            dest += new String(coded);
        }

        x = y;

        if(s.length % 3 == 0) return dest;

        if(s.length % 3 == 1)
        {
            coded[2] = '=';
            coded[3] = '=';

            coded[0] = code[s[x] >> 2];
            coded[1] = code[new Integer((s[x] % 4) << 4).byteValue()];

            dest += new String(coded);
        }

        if(s.length % 3 == 2)
        {
            coded[3] = '=';

            coded[0] = code[s[x] >> 2];
            coded[1] = new Integer((s[x] % 4) << 4).byteValue();
            coded[1] += s[x + 1] >> 4;
            coded[1] = code[coded[1]];

            coded[2] = code[new Integer((s[x + 1] % 16) << 2).byteValue()];

            dest += new String(coded);
        }

        return dest;
    }
}


