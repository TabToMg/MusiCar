package org.netbeans.lib.awtextra;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * LayoutManager that allows placing components at absolute coordinates.
 * IMPORTANT: This does not follow standard LayoutManager behavior;
 * it places components exactly where specified.
 */
public class AbsoluteLayout implements LayoutManager2 {
    
    private Map<Component, AbsoluteConstraints> constraintsMap = new HashMap<>();

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Not used by this layout
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        constraintsMap.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public void layoutContainer(Container parent) {
        for (Component comp : parent.getComponents()) {
            AbsoluteConstraints constraints = constraintsMap.get(comp);
            if (constraints != null) {
                int x = constraints.getX();
                int y = constraints.getY();
                int width = constraints.getWidth();
                int height = constraints.getHeight();

                // If size is not specified (-1), use preferred size
                if (width <= 0 || height <= 0) {
                    Dimension pref = comp.getPreferredSize();
                    if (width <= 0) width = pref.width;
                    if (height <= 0) height = pref.height;
                }

                comp.setBounds(x, y, width, height);
            }
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof AbsoluteConstraints) {
            constraintsMap.put(comp, (AbsoluteConstraints) constraints);
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        // No es necesario hacer nada
    }
}
