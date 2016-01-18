// FrontEnd Plus GUI for JAD
// DeCompiled : CompactByteArray.class

package es.satec.regexp;


// Referenced classes of package sun.text:
//            Utility

public final class CompactByteArray
    implements Cloneable
{

    public static final int UNICODECOUNT = 0x10000;
    private static final int BLOCKSHIFT = 7;
    private static final int BLOCKCOUNT = 128;
    private static final int INDEXSHIFT = 9;
    private static final int INDEXCOUNT = 512;
    private static final int BLOCKMASK = 127;
    private byte values[];
    private short indices[];
    private boolean isCompact;
    private int hashes[];

    public int hashCode()
    {
        int i = 0;
        int j = Math.min(3, values.length / 16);
        for(int k = 0; k < values.length; k += j)
            i = i * 37 + values[k];

        return i;
    }

    public CompactByteArray()
    {
        this((byte)0);
    }

    public void compact()
    {
        if(!isCompact)
        {
            int i = 0;
            int j = 0;
            short word0 = -1;
            for(int k = 0; k < indices.length;)
            {
                indices[k] = -1;
                boolean flag = blockTouched(k);
                if(!flag && word0 != -1)
                {
                    indices[k] = word0;
                } else
                {
                    int i1 = 0;
                    int j1 = 0;
                    j1 = 0;
                    do
                    {
                        if(j1 >= i)
                            break;
                        if(hashes[k] == hashes[j1] && arrayRegionMatches(values, j, values, i1, 128))
                        {
                            indices[k] = (short)i1;
                            break;
                        }
                        j1++;
                        i1 += 128;
                    } while(true);
                    if(indices[k] == -1)
                    {
                        System.arraycopy(values, j, values, i1, 128);
                        indices[k] = (short)i1;
                        hashes[j1] = hashes[k];
                        i++;
                        if(!flag)
                            word0 = (short)i1;
                    }
                }
                k++;
                j += 128;
            }

            int l = i * 128;
            byte abyte0[] = new byte[l];
            System.arraycopy(values, 0, abyte0, 0, l);
            values = abyte0;
            isCompact = true;
            hashes = null;
        }
    }

    private void expand()
    {
        if(isCompact)
        {
            hashes = new int[512];
            byte abyte0[] = new byte[0x10000];
            for(int i = 0; i < 0x10000; i++)
            {
                byte byte0 = elementAt((char)i);
                abyte0[i] = byte0;
                touchBlock(i >> 7, byte0);
            }

            for(int j = 0; j < 512; j++)
                indices[j] = (short)(j << 7);

            values = null;
            values = abyte0;
            isCompact = false;
        }
    }

    private byte[] getArray()
    {
        return values;
    }

    public byte[] getStringArray()
    {
        return values;
    }

    public short[] getIndexArray()
    {
        return indices;
    }

    public CompactByteArray(byte byte0)
    {
        values = new byte[0x10000];
        indices = new short[512];
        hashes = new int[512];
        for(int i = 0; i < 0x10000; i++)
            values[i] = byte0;

        for(int j = 0; j < 512; j++)
        {
            indices[j] = (short)(j << 7);
            hashes[j] = 0;
        }

        isCompact = false;
    }

    public byte elementAt(char c)
    {
        return values[(indices[c >> 7] & 0xffff) + (c & 0x7f)];
    }

    public void setElementAt(char c, byte byte0)
    {
        if(isCompact)
            expand();
        values[c] = byte0;
        touchBlock(c >> 7, byte0);
    }

    public void setElementAt(char c, char c1, byte byte0)
    {
        if(isCompact)
            expand();
        for(int i = c; i <= c1; i++)
        {
            values[i] = byte0;
            touchBlock(i >> 7, byte0);
        }

    }

    private final boolean blockTouched(int i)
    {
        return hashes[i] != 0;
    }

    private final void touchBlock(int i, int j)
    {
        hashes[i] = hashes[i] + (j << 1) | 1;
    }

    static final boolean arrayRegionMatches(byte abyte0[], int i, byte abyte1[], int j, int k)
    {
        int l = i + k;
        int i1 = j - i;
        for(int j1 = i; j1 < l; j1++)
            if(abyte0[j1] != abyte1[j1 + i1])
                return false;

        return true;
    }

    public CompactByteArray(short aword0[], byte abyte0[])
    {
        if(aword0.length != 512)
            throw new IllegalArgumentException("Index out of bounds!");
        for(int i = 0; i < 512; i++)
        {
            short word0 = aword0[i];
            if(word0 < 0 || word0 >= abyte0.length + 128)
                throw new IllegalArgumentException("Index out of bounds!");
        }

        indices = aword0;
        values = abyte0;
        isCompact = true;
    }

    public Object clone()
    {
    	try {
        CompactByteArray compactbytearray;
        compactbytearray = (CompactByteArray)super.clone();
        compactbytearray.values = (byte[])values.clone();
        compactbytearray.indices = (short[])indices.clone();
        if(hashes != null)
            compactbytearray.hashes = (int[])hashes.clone();
        return compactbytearray;
    	} catch (CloneNotSupportedException clonenotsupportedexception) {
        throw new InternalError();
    	}
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(getClass() != obj.getClass())
            return false;
        CompactByteArray compactbytearray = (CompactByteArray)obj;
        for(int i = 0; i < 0x10000; i++)
            if(elementAt((char)i) != compactbytearray.elementAt((char)i))
                return false;

        return true;
    }

    public CompactByteArray(String s, String s1)
    {
        this(Utility.RLEStringToShortArray(s), Utility.RLEStringToByteArray(s1));
    }
}
