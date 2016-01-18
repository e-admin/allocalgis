/**
 * JNCSRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ermapper.ecw;

import java.util.StringTokenizer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import com.ermapper.util.JNCSDatasetPoint;
import com.ermapper.util.JNCSWorldPoint;

public class JNCSRenderer extends JNCSFile
{

    public JNCSRenderer()
        throws JNCSException
    {
        pRGBArray = null;
        outputDeviceCoords = new double[4];
        ecwImage = null;
        prevImageSizeX = -1;
        prevImageSizeY = -1;
        bHaveValidSetView = false;
        transparencyValue = 1.0F;
//        alphaComposite = null;
        memImSource = null;
    }

    public JNCSRenderer(String s, boolean flag)
        throws JNCSFileOpenFailedException, JNCSException
    {
        pRGBArray = null;
        outputDeviceCoords = new double[4];
        ecwImage = null;
        prevImageSizeX = -1;
        prevImageSizeY = -1;
        bHaveValidSetView = false;
        transparencyValue = 1.0F;
//        alphaComposite = null;
        memImSource = null;
        open(s, flag);
    }

    public void setTransparency(float f)
    {
        transparencyValue = f;
//        if(bHave1_2VM)
//            alphaComposite = AlphaComposite.getInstance(3, transparencyValue);
    }

    public int setView(int i, int ai[], int j, int k, int l, int i1, int j1, 
            int k1)
        throws JNCSFileNotOpenException, JNCSInvalidSetViewException
    {
        int l1 = j;
        int i2 = k;
        int j2 = l;
        int k2 = i1;
        if(j < 0)
            l1 = 0;
        if(k < 0)
            i2 = 0;
        if(l > super.width - 1)
            j2 = super.width - 1;
        if(i1 > super.height - 1)
            k2 = super.height - 1;
        int l2 = (int)Math.round(((double)(l1 - j) / (double)(l - j)) * (double)j1);
        int i3 = (int)Math.round(((double)(i2 - k) / (double)(i1 - k)) * (double)k1);
        int j3 = (int)Math.round(((double)(j2 - j) / (double)(l - j)) * (double)j1);
        int k3 = (int)Math.round(((double)(k2 - k) / (double)(i1 - k)) * (double)k1);
        super.setView(i, ai, l1, i2, j2, k2, j3 - l2, k3 - i3);
        bHaveValidSetView = true;
        if(!super.progressive)
        {
            dRendererSetViewDatasetTLX = l1;
            dRendererSetViewDatasetTLY = l1;
            dRendererSetViewDatasetBRX = j2;
            dRendererSetViewDatasetBRY = k2;
            dRendererSetViewWidth = j3 - l2;
            dRendererSetViewHeight = k3 - i3;
        }
        return 0;
    }

    public int setView(int i, int ai[], double d, double d1, double d2, double d3, int j, int k)
        throws JNCSFileNotOpenException, JNCSInvalidSetViewException
    {
        JNCSDatasetPoint jncsdatasetpoint = convertWorldToDataset(d, d1);
        JNCSDatasetPoint jncsdatasetpoint1 = convertWorldToDataset(d2, d3);
        double d4 = d;
        double d5 = d1;
        double d6 = d2;
        double d7 = d3;
        jncsdatasetpoint.x = (int)Math.floor((d4 - super.originX) / super.cellIncrementX);
        jncsdatasetpoint.y = (int)Math.floor((d5 - super.originY) / super.cellIncrementY);
        jncsdatasetpoint1.x = (int)Math.ceil((d6 - super.originX) / super.cellIncrementX);
        jncsdatasetpoint1.y = (int)Math.ceil((d7 - super.originY) / super.cellIncrementY);
        JNCSWorldPoint jncsworldpoint = convertDatasetToWorld(jncsdatasetpoint.x, jncsdatasetpoint.y);
        JNCSWorldPoint jncsworldpoint1 = convertDatasetToWorld(jncsdatasetpoint1.x, jncsdatasetpoint1.y);
        d4 = jncsworldpoint.x;
        d5 = jncsworldpoint.y;
        d6 = jncsworldpoint1.x;
        d7 = jncsworldpoint1.y;
        if(jncsdatasetpoint.x < 0)
            d4 = super.originX;
        if(jncsdatasetpoint.y < 0)
            d5 = super.originY;
        if(jncsdatasetpoint1.x > super.width - 1)
            d6 = super.originX + (double)(super.width - 1) * super.cellIncrementX;
        if(jncsdatasetpoint1.y > super.height - 1)
            d7 = super.originY + (double)(super.height - 1) * super.cellIncrementY;
        if(jncsdatasetpoint.x < 0 && jncsdatasetpoint1.x < 0 || jncsdatasetpoint.x > super.width - 1 && jncsdatasetpoint1.x > super.width - 1)
        {
            bHaveValidSetView = false;
            return 1;
        }
        if(jncsdatasetpoint.y < 0 && jncsdatasetpoint1.y < 0 || jncsdatasetpoint.y > super.height - 1 && jncsdatasetpoint1.y > super.height - 1)
        {
            bHaveValidSetView = false;
            return 1;
        }
        int l = (int)Math.round(((d4 - d) / (d2 - d)) * (double)j);
        int i1 = (int)Math.round(((d5 - d1) / (d3 - d1)) * (double)k);
        int j1 = (int)Math.round(((d6 - d) / (d2 - d)) * (double)j);
        int k1 = (int)Math.round(((d7 - d1) / (d3 - d1)) * (double)k);
        if(j1 - l > jncsdatasetpoint1.x - jncsdatasetpoint.x)
        {
            j1 = jncsdatasetpoint1.x;
            l = jncsdatasetpoint.x;
        }
        if(k1 - i1 > jncsdatasetpoint1.y - jncsdatasetpoint.y)
        {
            k1 = jncsdatasetpoint1.y;
            i1 = jncsdatasetpoint.y;
        }
        if(j1 - l < 1 || k1 - i1 < 1)
        {
            bHaveValidSetView = false;
            return 1;
        }
        super.setView(i, ai, d4, d5, d6, d7, j1 - l, k1 - i1);
        bHaveValidSetView = true;
        if(!super.progressive)
        {
            dRendererSetViewWorldTLX = d4;
            dRendererSetViewWorldTLY = d5;
            dRendererSetViewWorldBRX = d6;
            dRendererSetViewWorldBRY = d7;
            dRendererSetViewWidth = j1 - l;
            dRendererSetViewHeight = k1 - i1;
        }
        return 0;
    }

    public void refreshUpdate(int i, int j, double d, double d1, double d2, double d3)
    {
        ecwReadImage(i, j, d, d1, d2, d3);
        if(super.progImageClient != null)
            super.progImageClient.refreshUpdate(i, j, d, d1, d2, d3);
    }

    public void refreshUpdate(int i, int j, int k, int l, double d, int i1)
    {
        ecwReadImage(i, j, k, l, d, i1);
        if(super.progImageClient != null)
            super.progImageClient.refreshUpdate(i, j, k, l, d, i1);
    }

    private boolean ecwReadImage(int i, int j, double d, double d1, double d2, double d3)
    {
        Image image = null;
        boolean flag = false;
        if(prevImageSizeX != i || prevImageSizeY != j)
        {
            pRGBArray = new int[i * j];
            prevImageSizeX = i;
            prevImageSizeY = j;
            flag = true;
        }
        try
        {
            readImageRGBA(pRGBArray, i, j);
            memImSource = new ImageData(i, j, 32, new PaletteData(0xFF000000, 0xFF0000, 0xFF00));
            memImSource.setPixels(0, 0, i*j, pRGBArray, 0);
//            if(flag)
//            {
//                memImSource = null;
//                memImSource = new MemoryImageSource(i, j, ecwColorModel, pRGBArray, 0, i);
//            } else
//            {
//                memImSource.newPixels(pRGBArray, ecwColorModel, 0, i);
//            }
        }
        catch(JNCSException jncsexception)
        {
            System.out.print(String.valueOf(String.valueOf((new StringBuffer("readImageRGBA failed :")).append(jncsexception.toString()).append("\n"))));
            bHaveValidSetView = false;
            boolean flag1 = false;
            return flag1;
        }
		if (ecwImage != null && !ecwImage.isDisposed()) ecwImage.dispose();
		ecwImage = new Image(Display.getDefault(), memImSource);
//        image = Toolkit.getDefaultToolkit().createImage(memImSource);
        if(ecwImage == null)
        {
            ecwImage = image;
            dRendererWorldTLX = d;
            dRendererWorldTLY = d1;
            dRendererWorldBRX = d2;
            dRendererWorldBRY = d3;
        } else
        {
            synchronized(ecwImage)
            {
                ecwImage = image;
                dRendererWorldTLX = d;
                dRendererWorldTLY = d1;
                dRendererWorldBRX = d2;
                dRendererWorldBRY = d3;
            }
        }
        bHaveValidSetView = false;
        return true;
    }

    public void drawImage(GC gc, int i, int j, int k, int l, double d, 
            double d1, double d2, double d3)
    {
        if(!super.progressive && bHaveValidSetView)
            ecwReadImage(dRendererSetViewWidth, dRendererSetViewHeight, dRendererSetViewWorldTLX, dRendererSetViewWorldTLY, dRendererSetViewWorldBRX, dRendererSetViewWorldBRY);
        if(super.progressive && ecwImage == null)
            return;
        synchronized(ecwImage)
        {
            if(bHave1_2VM)
                if((double)transparencyValue == 1.0D);
            calculateDeviceCoords(i, j, i + k, j + l, d, d1, d2, d3, outputDeviceCoords);
            int i1 = (int)Math.round(outputDeviceCoords[2] - outputDeviceCoords[0]);
            int j1 = (int)Math.round(outputDeviceCoords[3] - outputDeviceCoords[1]);
            int k1 = ecwImage.getBounds().width;//.getWidth(imageobserver);
            int l1 = ecwImage.getBounds().height;//.getHeight(imageobserver);
            if(i1 != k1 || j1 != l1)
            {
                if(i1 > k1 && j1 > l1)
                {
                    double d4 = outputDeviceCoords[0] >= (double)i ? outputDeviceCoords[0] : i;
                    double d5 = outputDeviceCoords[1] >= (double)j ? outputDeviceCoords[1] : j;
                    double d6 = outputDeviceCoords[2] <= (double)(i + k) ? outputDeviceCoords[2] : i + k;
                    double d7 = outputDeviceCoords[3] <= (double)(j + l) ? outputDeviceCoords[3] : j + l;
                    double ad[] = calculateImageCoords(outputDeviceCoords[0], outputDeviceCoords[1], outputDeviceCoords[2], outputDeviceCoords[3], k1, l1, d4, d5, d6, d7);
                    ad[0] = Math.floor(ad[0]);
                    ad[1] = Math.floor(ad[1]);
                    ad[2] = Math.ceil(ad[2]);
                    ad[3] = Math.ceil(ad[3]);
                    double d8 = dRendererWorldTLX + ((dRendererWorldBRX - dRendererWorldTLX) / (double)k1) * ad[0];
                    double d9 = dRendererWorldTLY + ((dRendererWorldBRY - dRendererWorldTLY) / (double)l1) * ad[1];
                    double d10 = dRendererWorldTLX + ((dRendererWorldBRX - dRendererWorldTLX) / (double)k1) * ad[2];
                    double d11 = dRendererWorldTLY + ((dRendererWorldBRY - dRendererWorldTLY) / (double)l1) * ad[3];
                    double d12 = ((outputDeviceCoords[2] - outputDeviceCoords[0]) * (d8 - dRendererWorldTLX)) / (dRendererWorldBRX - dRendererWorldTLX) + outputDeviceCoords[0];
                    double d13 = ((outputDeviceCoords[3] - outputDeviceCoords[1]) * (d9 - dRendererWorldTLY)) / (dRendererWorldBRY - dRendererWorldTLY) + outputDeviceCoords[1];
                    double d14 = ((outputDeviceCoords[2] - outputDeviceCoords[0]) * (d10 - dRendererWorldTLX)) / (dRendererWorldBRX - dRendererWorldTLX) + outputDeviceCoords[0];
                    double d15 = ((outputDeviceCoords[3] - outputDeviceCoords[1]) * (d11 - dRendererWorldTLY)) / (dRendererWorldBRY - dRendererWorldTLY) + outputDeviceCoords[1];
                    gc.drawImage(ecwImage, (int)Math.floor(ad[0]), (int)Math.floor(ad[1]), (int)Math.ceil(ad[2]-ad[0]), (int)Math.ceil(ad[3]-ad[1]), (int)Math.round(d12), (int)Math.round(d13), (int)Math.round(d14-d12), (int)Math.round(d15-d13));
                } else
                {
                    gc.drawImage(ecwImage, 0, 0, k1, l1, (int)Math.round(outputDeviceCoords[0]), (int)Math.round(outputDeviceCoords[1]), (int)Math.round(outputDeviceCoords[2]-outputDeviceCoords[0]), (int)Math.round(outputDeviceCoords[3]-outputDeviceCoords[1]));
                }
            } else
            {
                gc.drawImage(ecwImage, (int)Math.round(outputDeviceCoords[0]), (int)Math.round(outputDeviceCoords[1]));
            }
            if(bHave1_2VM)
                if((double)transparencyValue == 1.0D);
        }
    }

    private static final double[] calculateImageCoords(double d, double d1, double d2, double d3, 
            double d4, double d5, double d6, double d7, double d8, double d9)
    {
        double ad[] = new double[4];
        double d10 = ((d8 - d6) * d4) / (d2 - d);
        double d11 = ((d9 - d7) * d5) / (d3 - d1);
        ad[0] = ((d6 - d) * d10) / (d8 - d6);
        ad[1] = ((d7 - d1) * d11) / (d9 - d7);
        ad[2] = ad[0] + d10;
        ad[3] = ad[1] + d11;
        return ad;
    }

    private final void calculateDeviceCoords(int i, int j, int k, int l, double d, double d1, double d2, double d3, double ad[])
    {
        ad[0] = ((dRendererWorldTLX - d) / (d2 - d)) * (double)(k - i);
        ad[1] = ((dRendererWorldTLY - d1) / (d3 - d1)) * (double)(l - j);
        ad[2] = ((dRendererWorldBRX - d) / (d2 - d)) * (double)(k - i);
        ad[3] = ((dRendererWorldBRY - d1) / (d3 - d1)) * (double)(l - j);
    }

    private final void convertImageToWorld(int i, int j, double d, double d1, double ad[])
    {
        ad[0] = dRendererWorldTLX + ((dRendererWorldBRX - dRendererWorldTLX) / (double)i) * d;
        ad[1] = dRendererWorldTLY + ((dRendererWorldBRY - dRendererWorldTLY) / (double)j) * d1;
    }

    private double dRendererSetViewWorldTLX;
    private double dRendererSetViewWorldTLY;
    private double dRendererSetViewWorldBRX;
    private double dRendererSetViewWorldBRY;
    private int dRendererSetViewDatasetTLX;
    private int dRendererSetViewDatasetTLY;
    private int dRendererSetViewDatasetBRX;
    private int dRendererSetViewDatasetBRY;
    private int dRendererSetViewWidth;
    private int dRendererSetViewHeight;
    private int pRGBArray[];
    private double outputDeviceCoords[];
    private double dRendererWorldTLX;
    private double dRendererWorldTLY;
    private double dRendererWorldBRX;
    private double dRendererWorldBRY;
    private Image ecwImage;
    private int prevImageSizeX;
    private int prevImageSizeY;
    private boolean bHaveValidSetView;
    private float transparencyValue;
//    private AlphaComposite alphaComposite;
    private static boolean bHave1_2VM = false;
    private ImageData memImSource;
//    private static DirectColorModel ecwColorModel = null;
    private static final boolean bOptimizeImageStretch = true;

    static 
    {
        String s = System.getProperty("java.version");
        StringTokenizer stringtokenizer = new StringTokenizer(s, ".");
        int i = Integer.parseInt(stringtokenizer.nextToken());
        int j = Integer.parseInt(stringtokenizer.nextToken());
        if(i >= 1 && j >= 2)
            bHave1_2VM = true;
//        ecwColorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
    }
}
