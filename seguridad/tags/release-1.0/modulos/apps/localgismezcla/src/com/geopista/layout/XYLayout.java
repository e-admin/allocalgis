package com.geopista.layout;



import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;

public class XYLayout 
    implements LayoutManager2, Serializable
{

    public XYLayout()
    {
        info = new Hashtable();
    }

    public XYLayout(int i, int j)
    {
        info = new Hashtable();
        width = i;
        height = j;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int i)
    {
        width = i;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int i)
    {
        height = i;
    }

    public String toString()
    {
        return "XYLayout[width=" + width + ",height=" + height + "]";
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
        info.remove(component);
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return getLayoutSize(container, true);
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return getLayoutSize(container, false);
    }

    public void layoutContainer(Container container)
    {
        Insets insets = container.getInsets();
        int i = container.getComponentCount();
        for(int j = 0; j < i; j++)
        {
            Component component = container.getComponent(j);
            if(component.isVisible())
            {
                Rectangle rectangle = getComponentBounds(component, true);
                component.setBounds(insets.left + rectangle.x, insets.top + rectangle.y, rectangle.width, rectangle.height);
            }
        }

    }

    public void addLayoutComponent(Component component, Object obj)
    {
        if(obj instanceof XYConstraints)
            info.put(component, obj);
    }

    public Dimension maximumLayoutSize(Container container)
    {
        return new Dimension(0x7fffffff, 0x7fffffff);
    }

    public float getLayoutAlignmentX(Container container)
    {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container container)
    {
        return 0.5F;
    }

    public void invalidateLayout(Container container)
    {
    }

    Rectangle getComponentBounds(Component component, boolean flag)
    {
        XYConstraints xyconstraints = (XYConstraints)info.get(component);
        if(xyconstraints == null)
            xyconstraints = defaultConstraints;
        Rectangle rectangle = new Rectangle(xyconstraints.x, xyconstraints.y, xyconstraints.width, xyconstraints.height);
        if(rectangle.width <= 0 || rectangle.height <= 0)
        {
            Dimension dimension = flag ? component.getPreferredSize() : component.getMinimumSize();
            if(rectangle.width <= 0)
                rectangle.width = dimension.width;
            if(rectangle.height <= 0)
                rectangle.height = dimension.height;
        }
        return rectangle;
    }

    Dimension getLayoutSize(Container container, boolean flag)
    {
        Dimension dimension = new Dimension(0, 0);
        if(width <= 0 || height <= 0)
        {
            int i = container.getComponentCount();
            for(int j = 0; j < i; j++)
            {
                Component component = container.getComponent(j);
                if(component.isVisible())
                {
                    Rectangle rectangle = getComponentBounds(component, flag);
                    dimension.width = Math.max(dimension.width, rectangle.x + rectangle.width);
                    dimension.height = Math.max(dimension.height, rectangle.y + rectangle.height);
                }
            }

        }
        if(width > 0)
            dimension.width = width;
        if(height > 0)
            dimension.height = height;
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    private static final long serialVersionUID = 200L;
    int width;
    int height;
    Hashtable info;
    static final XYConstraints defaultConstraints = new XYConstraints();

}
