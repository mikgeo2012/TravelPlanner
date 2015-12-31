package planner.model;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * Object holding the coordinates of a Stop
 *
 * Created by mikhailgeorge on 12/23/15.
 */
public class Coords {
    private double dx;
    private double dy;

    public Coords(double x, double y) {
        this.dx = x;
        this.dy = y;
    }

    /**
     * Checks if two coordinates are equal
     *
     * @param o
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Coords) {
            Coords c = (Coords) o;
            double x = c.getX();
            double y = c.getY();

            return x == dx && y == dy;
        } else {
            return false;
        }
    }

    public double getX() {
        return dx;
    }

    public void setX(double x) {
        this.dx = x;
    }

    public double getY() {
        return dy;
    }

    public void setY(double y) {
        this.dy = y;
    }

    public LatLong makeLatLong() {
        return new LatLong(dx, dy);
    }
}
