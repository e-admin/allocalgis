package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyUtil, TinyPoint

public class TinyRect
{

    public int xmin;
    public int ymin;
    public int xmax;
    public int ymax;

    public TinyRect()
    {
        xmin = ymin = xmax = ymax = 0x80000000;
    }

    public TinyRect(TinyRect tinyrect)
    {
        xmin = tinyrect.xmin;
        ymin = tinyrect.ymin;
        xmax = tinyrect.xmax;
        ymax = tinyrect.ymax;
    }

    public TinyRect(int i, int j, int k, int l)
    {
        if(i < k)
        {
            xmin = i;
            xmax = k;
        } else
        {
            xmin = k;
            xmax = i;
        }
        if(j < l)
        {
            ymin = j;
            ymax = l;
        } else
        {
            ymin = l;
            ymax = j;
        }
    }

    public TinyRect grow(int i, int j)
    {
        return new TinyRect(xmin - i, ymin - j, xmax + i, ymax + j);
    }

    public void setEmpty()
    {
        xmin = xmax = ymin = ymax = 0x80000000;
    }

    public void union(TinyRect tinyrect)
    {
        if(tinyrect != null && !tinyrect.isEmpty())
        {
            if(isEmpty())
            {
                xmin = tinyrect.xmin;
                xmax = tinyrect.xmax;
                ymin = tinyrect.ymin;
                ymax = tinyrect.ymax;
                return;
            }
            xmin = TinyUtil.min(xmin, tinyrect.xmin);
            xmax = TinyUtil.max(xmax, tinyrect.xmax);
            ymin = TinyUtil.min(ymin, tinyrect.ymin);
            ymax = TinyUtil.max(ymax, tinyrect.ymax);
        }
    }

    public TinyRect intersection(TinyRect tinyrect)
    {
        int i = TinyUtil.max(xmin, tinyrect.xmin);
        int j = TinyUtil.min(xmax, tinyrect.xmax);
        int k = TinyUtil.max(ymin, tinyrect.ymin);
        int l = TinyUtil.min(ymax, tinyrect.ymax);
        return new TinyRect(i, k, j, l);
    }

    public void add(TinyPoint tinypoint)
    {
        if(isEmpty())
        {
            xmin = xmax = tinypoint.x;
            ymin = ymax = tinypoint.y;
            return;
        }
        if(tinypoint.x < xmin)
        {
            xmin = tinypoint.x;
        } else
        if(tinypoint.x > xmax)
        {
            xmax = tinypoint.x;
        }
        if(tinypoint.y < ymin)
        {
            ymin = tinypoint.y;
            return;
        }
        if(tinypoint.y > ymax)
        {
            ymax = tinypoint.y;
        }
    }

    public boolean isEmpty()
    {
        return xmin == 0x80000000;
    }

    public void translate(int i, int j)
    {
        if(!isEmpty())
        {
            xmin += i;
            xmax += i;
            ymin += j;
            ymax += j;
        }
    }

    public boolean contains(TinyPoint tinypoint)
    {
        return xmin <= tinypoint.x && tinypoint.x <= xmax && ymin <= tinypoint.y && tinypoint.y <= ymax;
    }

    public boolean intersects(TinyRect tinyrect)
    {
        return xmin <= tinyrect.xmax && tinyrect.xmin <= xmax && ymin <= tinyrect.ymax && tinyrect.ymin <= ymax;
    }
}
