package com.tinyline.tiny2d;


// Referenced classes of package com.tinyline.tiny2d:
//            TinyUtil

public class TinyPoint
{

    public int x;
    public int y;

    public TinyPoint()
    {
        x = y = 0;
    }

    public TinyPoint(int i, int j)
    {
        x = i;
        y = j;
    }

    public int distance(TinyPoint tinypoint)
    {
        int i = tinypoint.x - x;
        int j = tinypoint.y - y;
        return TinyUtil.a(i, j);
    }
}
