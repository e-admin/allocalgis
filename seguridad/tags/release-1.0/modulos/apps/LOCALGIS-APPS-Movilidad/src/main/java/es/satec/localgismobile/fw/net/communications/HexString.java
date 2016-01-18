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
