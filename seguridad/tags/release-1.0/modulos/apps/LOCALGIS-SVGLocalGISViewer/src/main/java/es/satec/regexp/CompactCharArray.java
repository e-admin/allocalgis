// FrontEnd Plus GUI for JAD
// DeCompiled : CompactCharArray.class

package es.satec.regexp;


// Referenced classes of package sun.text:
//            Utility

public final class CompactCharArray
    implements Cloneable
{

    public static final int UNICODECOUNT = 0x10000;
    private static final int BLOCKSHIFT = 5;
    private static final int BLOCKCOUNT = 32;
    private static final int INDEXSHIFT = 11;
    private static final int INDEXCOUNT = 2048;
    private static final int BLOCKMASK = 31;
    private char values[];
    private char indices[];
    private int hashes[];
    private boolean isCompact;
    private char defaultValue;

    public int hashCode()
    {
        int i = 0;
        int j = Math.min(3, values.length / 16);
        for(int k = 0; k < values.length; k += j)
            i = i * 37 + values[k];

        return i;
    }

    public CompactCharArray()
    {
        this('\0');
    }

    public void compact()
    {
        if(!isCompact)
        {
            int i = 0;
            int j = 0;
            char c = '\uFFFF';
            for(int k = 0; k < indices.length;)
            {
                indices[k] = '\uFFFF';
                boolean flag = blockTouched(k);
                if(!flag && c != 65535)
                {
                    indices[k] = c;
                } else
                {
                    int i1 = 0;
                    int j1 = 0;
                    for(j1 = 0; j1 < i;)
                    {
                        if(hashes[k] == hashes[j1] && arrayRegionMatches(values, j, values, i1, 32))
                            indices[k] = (char)i1;
                        j1++;
                        i1 += 32;
                    }

                    if(indices[k] == '\uFFFF')
                    {
                        System.arraycopy(values, j, values, i1, 32);
                        indices[k] = (char)i1;
                        hashes[j1] = hashes[k];
                        i++;
                        if(!flag)
                            c = (char)i1;
                    }
                }
                k++;
                j += 32;
            }

            int l = i * 32;
            char ac[] = new char[l];
            System.arraycopy(values, 0, ac, 0, l);
            values = ac;
            isCompact = true;
            hashes = null;
        }
    }

    private void expand()
    {
        if(isCompact)
        {
            char ac[] = new char[0x10000];
            hashes = new int[2048];
            for(int i = 0; i < 0x10000; i++)
            {
                char c = elementAt((char)i);
                ac[i] = c;
                touchBlock(i >> 5, c);
            }

            for(int j = 0; j < 2048; j++)
                indices[j] = (char)(j << 5);

            values = null;
            values = ac;
            isCompact = false;
        }
    }

    public char[] getIndexArray()
    {
        return indices;
    }

    public char[] getStringArray()
    {
        return values;
    }

    public char elementAt(char c)
    {
        return values[(indices[c >> 5] & 0xffff) + (c & 0x1f)];
    }

    public CompactCharArray(char c)
    {
        values = new char[0x10000];
        indices = new char[2048];
        hashes = new int[2048];
        for(int i = 0; i < 0x10000; i++)
            values[i] = c;

        for(int j = 0; j < 2048; j++)
        {
            indices[j] = (char)(j << 5);
            hashes[j] = 0;
        }

        isCompact = false;
        defaultValue = c;
    }

    public void setElementAt(char c, char c1)
    {
        if(isCompact)
            expand();
        values[c] = c1;
        touchBlock(c >> 5, c1);
    }

    public void setElementAt(char c, char c1, char c2)
    {
        if(isCompact)
            expand();
        for(int i = c; i <= c1; i++)
        {
            values[i] = c2;
            touchBlock(i >> 5, c2);
        }

    }

    private char getArrayValue(int i)
    {
        return values[i];
    }

    private char getIndexArrayValue(int i)
    {
        return indices[i];
    }

    private final boolean blockTouched(int i)
    {
        return hashes[i] != 0;
    }

    private final void touchBlock(int i, int j)
    {
        hashes[i] = hashes[i] + (j << 1) | 1;
    }

    static final boolean arrayRegionMatches(char ac[], int i, char ac1[], int j, int k)
    {
        int l = i + k;
        int i1 = j - i;
        for(int j1 = i; j1 < l; j1++)
            if(ac[j1] != ac1[j1 + i1])
                return false;

        return true;
    }

    public CompactCharArray(char ac[], char ac1[])
    {
        if(ac.length != 2048)
            throw new IllegalArgumentException("Index out of bounds.");
        for(int i = 0; i < 2048; i++)
        {
            char c = ac[i];
            if(c < 0 || c >= ac1.length + 32)
                throw new IllegalArgumentException("Index out of bounds.");
        }

        indices = ac;
        values = ac1;
        isCompact = true;
    }

    public Object clone()
    {
    	try {
        CompactCharArray compactchararray;
        compactchararray = (CompactCharArray)super.clone();
        compactchararray.values = (char[])values.clone();
        compactchararray.indices = (char[])indices.clone();
        if(hashes != null)
            compactchararray.hashes = (int[])hashes.clone();
        return compactchararray;
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
        CompactCharArray compactchararray = (CompactCharArray)obj;
        for(int i = 0; i < 0x10000; i++)
            if(elementAt((char)i) != compactchararray.elementAt((char)i))
                return false;

        return true;
    }

    public CompactCharArray(String s, String s1)
    {
        this(Utility.RLEStringToCharArray(s), Utility.RLEStringToCharArray(s1));
    }
}
