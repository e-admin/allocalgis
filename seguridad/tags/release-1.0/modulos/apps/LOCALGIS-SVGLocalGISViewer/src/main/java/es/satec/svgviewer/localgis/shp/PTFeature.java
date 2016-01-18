package es.satec.svgviewer.localgis.shp;

class PTFeature extends Feature
{

    public PTFeature(DPoint dpoint)
    {
        super(dpoint);
        dp = dpoint;
    }

    public DPoint dp;
    public int viewsize;
}