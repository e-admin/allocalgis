/**
 * PointPlacement_Impl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.deegree_impl.graphics.sld;



import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;

// Referenced classes of package org.deegree_impl.graphics.sld:
//            StyleFactory

public class PointPlacement_Impl
    implements PointPlacement, Marshallable
{

    PointPlacement_Impl()
    {
        rotation = null;
        anchorPoint = null;
        displacement = null;
        auto = false;
    }

    public PointPlacement_Impl(ParameterValueType anchorPoint[], ParameterValueType displacement[], ParameterValueType rotation, boolean auto)
    {
        this.rotation = null;
        this.anchorPoint = null;
        this.displacement = null;
        this.auto = false;
        this.anchorPoint = anchorPoint;
        this.displacement = displacement;
        this.rotation = rotation;
        this.auto = auto;
    }

    public double[] getAnchorPoint(Feature feature)
        throws FilterEvaluationException
    {
        double anchorPointVal[] = {
            0.0D, 0.5D
        };
        if(anchorPoint != null)
        {
            anchorPointVal[0] = Double.parseDouble(anchorPoint[0].evaluate(feature));
            anchorPointVal[1] = Double.parseDouble(anchorPoint[1].evaluate(feature));
        }
        return anchorPointVal;
    }

    public void setAnchorPoint(double anchorPoint[])
    {
        ParameterValueType pvt = null;
        ParameterValueType pvtArray[] = new ParameterValueType[anchorPoint.length];
        for(int i = 0; i < anchorPoint.length; i++)
        {
            pvt = StyleFactory.createParameterValueType("" + anchorPoint[i]);
            pvtArray[i] = pvt;
        }

        this.anchorPoint = pvtArray;
    }

    public double[] getDisplacement(Feature feature)
        throws FilterEvaluationException
    {
        double displacementVal[] = {
            0.0D, 0.0D
        };
        if(displacement != null)
        {
            displacementVal[0] = Double.parseDouble(displacement[0].evaluate(feature));
            displacementVal[1] = Double.parseDouble(displacement[1].evaluate(feature));
        }
        return displacementVal;
    }

    public void setDisplacement(double displacement[])
    {
        ParameterValueType pvt = null;
        ParameterValueType pvtArray[] = new ParameterValueType[displacement.length];
        for(int i = 0; i < displacement.length; i++)
        {
            pvt = StyleFactory.createParameterValueType("" + displacement[i]);
            pvtArray[i] = pvt;
        }

        this.displacement = pvtArray;
    }

    public double getRotation(Feature feature)
        throws FilterEvaluationException
    {
        double rot = 0.0D;
        if(rotation != null)
            rot = Double.parseDouble(rotation.evaluate(feature));
        return rot;
    }

    public void setRotation(double rotation)
    {
        ParameterValueType pvt = null;
        pvt = StyleFactory.createParameterValueType("" + rotation);
        this.rotation = pvt;
    }

    public boolean isAuto()
    {
        return auto;
    }

    public void setAuto(boolean auto)
    {
        this.auto = auto;
    }

    public String exportAsXML()
    {
        Debug.debugMethodBegin();
        StringBuffer sb = new StringBuffer(1000);
        sb.append("<PointPlacement");
        if(auto)
            sb.append(" auto='true'");
        sb.append(">");
        if(anchorPoint != null && anchorPoint.length > 1)
        {
            sb.append("<AnchorPoint>").append("<AnchorPointX>");
            sb.append(((Marshallable)anchorPoint[0]).exportAsXML());
            sb.append("</AnchorPointX>").append("<AnchorPointY>");
            sb.append(((Marshallable)anchorPoint[1]).exportAsXML());
            sb.append("</AnchorPointY>").append("</AnchorPoint>");
        }
        if(displacement != null && displacement.length > 1)
        {
            sb.append("<Displacement>").append("<DisplacementX>");
            sb.append(((Marshallable)displacement[0]).exportAsXML());
            sb.append("</DisplacementX>").append("<DisplacementY>");
            sb.append(((Marshallable)displacement[1]).exportAsXML());
            sb.append("</DisplacementY>").append("</Displacement>");
        }
        if(rotation != null)
        {
            sb.append("<Rotation>");
            sb.append(((Marshallable)rotation).exportAsXML());
            sb.append("</Rotation>");
        }
        sb.append("</PointPlacement>");
        Debug.debugMethodEnd();
        return sb.toString();
    }

    private ParameterValueType rotation;
    private ParameterValueType anchorPoint[];
    private ParameterValueType displacement[];
    private boolean auto;
}