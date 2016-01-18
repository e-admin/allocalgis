package es.satec.svgviewer.localgis.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BASE64EncoderStream extends FilterOutputStream
{

    private byte buffer[];
    private int bufsize;
    private byte outbuf[];
    private int count;
    private int bytesPerLine;
    private int lineLimit;
    private boolean noCRLF;
    private static byte newline[] = {
        13, 10
    };
    private static final char pem_array[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };

    public BASE64EncoderStream(OutputStream outputstream, int i)
    {
        super(outputstream);
        bufsize = 0;
        count = 0;
        noCRLF = false;
        buffer = new byte[3];
        if(i == 0x7fffffff || i < 4)
        {
            noCRLF = true;
            i = 76;
        }
        i = (i / 4) * 4;
        bytesPerLine = i;
        lineLimit = (i / 4) * 3;
        if(noCRLF)
        {
            outbuf = new byte[i];
        } else
        {
            outbuf = new byte[i + 2];
            outbuf[i] = 13;
            outbuf[i + 1] = 10;
        }
    }

    public BASE64EncoderStream(OutputStream outputstream)
    {
        this(outputstream, 76);
    }

    public synchronized void write(byte abyte0[], int i, int j)
        throws IOException
    {
        int k;
        for(k = i + j; bufsize != 0 && i < k;)
            write(abyte0[i++]);

        int l = ((bytesPerLine - count) / 4) * 3;
        if(i + l < k)
        {
            int j1 = encodedSize(l);
            if(!noCRLF)
            {
                outbuf[j1++] = 13;
                outbuf[j1++] = 10;
            }
            out.write(encode(abyte0, i, l, outbuf), 0, j1);
            i += l;
            count = 0;
        }
        for(; i + lineLimit < k; i += lineLimit)
            out.write(encode(abyte0, i, lineLimit, outbuf));

        if(i + 3 < k)
        {
            int i1 = k - i;
            i1 = (i1 / 3) * 3;
            int k1 = encodedSize(i1);
            out.write(encode(abyte0, i, i1, outbuf), 0, k1);
            i += i1;
            count += k1;
        }
        for(; i < k; i++)
            write(abyte0[i]);

    }

    public void write(byte abyte0[])
        throws IOException
    {
        write(abyte0, 0, abyte0.length);
    }

    public synchronized void write(int i)
        throws IOException
    {
        buffer[bufsize++] = (byte)i;
        if(bufsize == 3)
        {
            encode();
            bufsize = 0;
        }
    }

    public synchronized void flush()
        throws IOException
    {
        if(bufsize > 0)
        {
            encode();
            bufsize = 0;
        }
        out.flush();
    }

    public synchronized void close()
        throws IOException
    {
        flush();
        if(count > 0 && !noCRLF)
        {
            out.write(newline);
            out.flush();
        }
        out.close();
    }

    private void encode()
        throws IOException
    {
        int i = encodedSize(bufsize);
        out.write(encode(buffer, 0, bufsize, outbuf), 0, i);
        count += i;
        if(count >= bytesPerLine)
        {
            if(!noCRLF)
                out.write(newline);
            count = 0;
        }
    }

    public static byte[] encode(byte abyte0[])
    {
        if(abyte0.length == 0)
            return abyte0;
        else
            return encode(abyte0, 0, abyte0.length, null);
    }

    private static byte[] encode(byte abyte0[], int i, int j, byte abyte1[])
    {
        if(abyte1 == null)
            abyte1 = new byte[encodedSize(j)];
        int k = i;
        int l;
        for(l = 0; j >= 3; l += 4)
        {
            int i1 = abyte0[k++] & 0xff;
            i1 <<= 8;
            i1 |= abyte0[k++] & 0xff;
            i1 <<= 8;
            i1 |= abyte0[k++] & 0xff;
            abyte1[l + 3] = (byte)pem_array[i1 & 0x3f];
            i1 >>= 6;
            abyte1[l + 2] = (byte)pem_array[i1 & 0x3f];
            i1 >>= 6;
            abyte1[l + 1] = (byte)pem_array[i1 & 0x3f];
            i1 >>= 6;
            abyte1[l + 0] = (byte)pem_array[i1 & 0x3f];
            j -= 3;
        }

        if(j == 1)
        {
            int j1 = abyte0[k++] & 0xff;
            j1 <<= 4;
            abyte1[l + 3] = 61;
            abyte1[l + 2] = 61;
            abyte1[l + 1] = (byte)pem_array[j1 & 0x3f];
            j1 >>= 6;
            abyte1[l + 0] = (byte)pem_array[j1 & 0x3f];
        } else
        if(j == 2)
        {
            int k1 = abyte0[k++] & 0xff;
            k1 <<= 8;
            k1 |= abyte0[k++] & 0xff;
            k1 <<= 2;
            abyte1[l + 3] = 61;
            abyte1[l + 2] = (byte)pem_array[k1 & 0x3f];
            k1 >>= 6;
            abyte1[l + 1] = (byte)pem_array[k1 & 0x3f];
            k1 >>= 6;
            abyte1[l + 0] = (byte)pem_array[k1 & 0x3f];
        }
        return abyte1;
    }

    private static int encodedSize(int i)
    {
        return ((i + 2) / 3) * 4;
    }

}
