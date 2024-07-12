package org.biojava.nbio.alignment.routines;

public class SequencePosition {

    private int x;
    private int y;
    private int gop;
    private int gep;

    /**
     * Constructor for a sequence position
     * @param x position in query
     * @param y position in target
     * @param gop gap opening penalty
     * @param gep gap extension penalty
     */
    public SequencePosition(int x, int y, int gop, int gep) {
        this.x = x;
        this.y = y;
        this.gop = gop;
        this.gep = gep;
    }

    /**
     * Constructor for a sequence position
     * @param x position in query
     * @param y position in target
     * @param gep gap extension penalty
     */
    public SequencePosition(int x, int y, int gep) {
        this.x = x;
        this.y = y;
        this.gop = 0;
        this.gep = gep;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGop() {
        return gop;
    }

    public void setGop(int gop) {
        this.gop = gop;
    }

    public int getGep() {
        return gep;
    }

    public void setGep(int gep) {
        this.gep = gep;
    }
}