package org.netbeans.lib.awtextra;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Restricciones para el layout AbsoluteLayout de NetBeans.
 * Define la posición y tamaño de un componente en coordenadas absolutas.
 */
public class AbsoluteConstraints {
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Constructor con posición (x, y)
     */
    public AbsoluteConstraints(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = -1;
        this.height = -1;
    }

    /**
     * Constructor con posición y tamaño (x, y, width, height)
     */
    public AbsoluteConstraints(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Dimension getSize() {
        if (width > 0 && height > 0) {
            return new Dimension(width, height);
        }
        return null;
    }
}
