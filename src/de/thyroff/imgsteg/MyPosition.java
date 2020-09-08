package de.thyroff.imgsteg;

import java.util.Objects;

public class MyPosition {
    short x, y;
    Channel c;
    short offset;

    public MyPosition(short x, short y, Channel c, short offset) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyPosition)) return false;
        MyPosition that = (MyPosition) o;
        return x == that.x &&
                y == that.y &&
                offset == that.offset &&
                c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, c, offset);
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public Channel getC() {
        return c;
    }

    public void setC(Channel c) {
        this.c = c;
    }

    public short getOffset() {
        return offset;
    }

    public void setOffset(short offset) {
        this.offset = offset;
    }
}