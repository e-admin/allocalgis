package es.satec.svgviewer.localgis.shp;

import java.io.*;

class NetInputStream extends DataInputStream
{

    public NetInputStream(InputStream inputstream)
    {
        super(inputstream);
        br = new BufferedReader(new InputStreamReader(inputstream));
        D4 = false;
        csp_flag = false;
        csp_scale = 1.0D;
        csp_ixbase = 0;
        csp_iybase = 0;
        csp_ptflag = 1;
    }

    public String readBRLine()
    {
        try
        {
            return br.readLine();
        }
        catch(Exception _ex)
        {
            return null;
        }
    }

    public boolean eof()
    {
        return D4;
    }

    public void set_csp_flag(boolean flag)
    {
        csp_flag = flag;
    }

    public void set_csp_scale(double d)
    {
        csp_scale = d;
    }

    public void set_csp_ixbase(int i)
    {
        csp_ixbase = i;
    }

    public void set_csp_iybase(int i)
    {
        csp_iybase = i;
    }

    public int csp_readInt()
    {
        try
        {
            int i = readUnsignedByte();
            int j = readUnsignedByte();
            int k = j;
            k <<= 8;
            k += i;
            return k;
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0;
    }

    public double csp_readDouble()
    {
        int i = csp_readInt();
        int j;
        if(csp_ptflag == 1)
        {
            j = csp_ixbase + i;
            csp_ptflag = 2;
        } else
        {
            j = csp_iybase + i;
            csp_ptflag = 1;
        }
        double d = (new Integer(j)).doubleValue();
        d /= csp_scale;
        return d;
    }

    public int read4BInt()
    {
        try
        {
            return readInt();
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0;
    }

    public int readBInt()
    {
        if(csp_flag)
            return csp_readInt();
        try
        {
            return readInt();
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0;
    }

    public double readBDouble()
    {
        if(csp_flag)
            return csp_readDouble();
        try
        {
            return readDouble();
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0.0D;
    }

    public short readLShort()
    {
        try
        {
            int i = readByte() & 0xff;
            byte byte0 = readByte();
            return (short)(byte0 << 8 | i);
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0;
    }

    public int readLInt()
    {
        if(csp_flag)
            return csp_readInt();
        try
        {
            int i = 0;
            for(int j = 0; j < 32; j += 8)
                i |= (readByte() & 0xff) << j;

            return i;
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0;
    }

    public double readLDouble()
    {
        if(csp_flag)
            return csp_readDouble();
        try
        {
            long l = 0L;
            for(int i = 0; i < 64; i += 8)
                l |= (long)(readByte() & 0xff) << i;

            return Double.longBitsToDouble(l);
        }
        catch(IOException _ex)
        {
            D4 = true;
        }
        return 0.0D;
    }

    public DPoint readDPoint()
    {
        return new DPoint(readLDouble() * ShpReader.scale, readLDouble() * ShpReader.scale);
    }

    public DRectangle readDRectangle()
    {
        return new DRectangle(readDPoint(), readDPoint());
    }

    public void skip(int i)
    {
        if(csp_flag)
            return;
        try
        {
            skipBytes(i);
            return;
        }
        catch(IOException _ex)
        {
            return;
        }
    }

    public void close()
    {
        try
        {
            br.close();
        }
        catch(Exception _ex) { }
        br = null;
        try
        {
            super.close();
            return;
        }
        catch(Exception _ex)
        {
            return;
        }
    }

    BufferedReader br;
    boolean D4;
    public boolean csp_flag;
    public double csp_scale;
    public int csp_ixbase;
    public int csp_iybase;
    public int csp_ptflag;
}